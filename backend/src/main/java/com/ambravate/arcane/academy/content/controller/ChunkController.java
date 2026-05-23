package com.ambravate.arcane.academy.content.controller;

import com.ambravate.arcane.academy.content.dto.ChunkDetailDto;
import com.ambravate.arcane.academy.content.dto.ChunkSummaryDto;
import com.ambravate.arcane.academy.practice.dto.SubChunkSummaryDto;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.content.domain.ChunkWithStatus;

import com.ambravate.arcane.academy.content.repository.SubChunkRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.content.service.ChunkGraphService;
import com.ambravate.arcane.academy.ai.service.SpacingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chunks")
@RequiredArgsConstructor
public class ChunkController {

    private final ChunkGraphService chunkGraphService;
    private final SubChunkRepository subChunkRepository;
    private final UserChunkProgressRepository progressRepository;
    private final SpacingService spacingService;

    @GetMapping
    public ResponseEntity<List<ChunkSummaryDto>> getAllChunks(
            @AuthenticationPrincipal UserPrincipal user) {
        List<ChunkWithStatus> chunks = chunkGraphService.getAllChunksWithStatus(user.getId());
        Map<String, UserChunkProgress> progressMap = progressRepository.findByUserId(user.getId()).stream()
                .collect(Collectors.toMap(UserChunkProgress::getSubChunkId, p -> p, (a, b) -> a));

        List<ChunkSummaryDto> result = chunks.stream().map(cws -> {
            Chunk c = cws.chunk();
            List<SubChunk> subs = subChunkRepository.findByChunkIdOrderBySortOrderAsc(c.getId());
            int completed = (int) subs.stream().filter(sc -> {
                UserChunkProgress p = progressMap.get(sc.getId());
                return p != null && (p.getStatus() == SubChunkStatus.COMPLETE || p.getStatus() == SubChunkStatus.SKIPPED);
            }).count();

            double avgStrength = subs.stream()
                    .map(sc -> progressMap.get(sc.getId()))
                    .filter(Objects::nonNull)
                    .mapToDouble(spacingService::computeDecayedStrength)
                    .average().orElse(0.0);

            String health = avgStrength > 0.7 ? "GREEN" : avgStrength >= 0.4 ? "YELLOW" : "RED";

            return ChunkSummaryDto.builder()
                    .id(c.getId()).title(c.getTitle()).glyph(c.getGlyph())
                    .status(cws.status())
                    .totalSubChunks(subs.size()).completedSubChunks(completed)
                    .memoryStrength(avgStrength).healthColor(health)
                    .prerequisiteIds(c.getPrerequisites().stream().map(Chunk::getId).toList())
                    .build();
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{chunkId}")
    public ResponseEntity<ChunkDetailDto> getChunkDetail(
            @PathVariable String chunkId,
            @AuthenticationPrincipal UserPrincipal user) {
        List<ChunkWithStatus> allChunks = chunkGraphService.getAllChunksWithStatus(user.getId());
        ChunkWithStatus cws = allChunks.stream()
                .filter(c -> c.chunk().getId().equals(chunkId))
                .findFirst().orElseThrow(() -> new NoSuchElementException("Chunk not found: " + chunkId));

        List<SubChunk> subs = subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunkId);
        Map<String, UserChunkProgress> progressMap = progressRepository.findByUserId(user.getId()).stream()
                .collect(Collectors.toMap(UserChunkProgress::getSubChunkId, p -> p, (a, b) -> a));

        // Build set of completed/skipped sub-chunk IDs for sequential-lock computation
        Set<String> completedSubChunkIds = progressMap.values().stream()
                .filter(p -> p.getStatus() == SubChunkStatus.COMPLETE
                          || p.getStatus() == SubChunkStatus.SKIPPED)
                .map(UserChunkProgress::getSubChunkId)
                .collect(Collectors.toSet());

        List<SubChunkSummaryDto> subDtos = subs.stream().map(sc -> {
            UserChunkProgress p = progressMap.get(sc.getId());
            double strength = p != null ? spacingService.computeDecayedStrength(p) : 0.0;
            String health = strength > 0.7 ? "GREEN" : strength >= 0.4 ? "YELLOW" : "RED";

            // Determine effective status: sub-chunks after the first are LOCKED
            // until all prior siblings (by sortOrder) are complete/skipped.
            String effectiveStatus;
            if (p != null && (p.getStatus() == SubChunkStatus.COMPLETE
                    || p.getStatus() == SubChunkStatus.SKIPPED
                    || p.getStatus() == SubChunkStatus.IN_PROGRESS)) {
                // Already started or finished — never retroactively lock
                effectiveStatus = p.getStatus().name();
            } else if (sc.getSortOrder() > 1) {
                boolean allPriorDone = subs.stream()
                        .filter(other -> other.getSortOrder() < sc.getSortOrder())
                        .allMatch(other -> completedSubChunkIds.contains(other.getId()));
                effectiveStatus = allPriorDone
                        ? (p != null ? p.getStatus().name() : "NOT_STARTED")
                        : "LOCKED";
            } else {
                effectiveStatus = p != null ? p.getStatus().name() : "NOT_STARTED";
            }

            // Count learning objectives from JSON (fast path: count "," occurrences + 1 in a non-empty array)
            int objCount = countJsonArraySize(sc.getLearningObjectivesJson());

            return SubChunkSummaryDto.builder()
                    .id(sc.getId()).title(sc.getTitle()).sortOrder(sc.getSortOrder())
                    .status(effectiveStatus)
                    .currentPhase(p != null ? p.getCurrentPhase().name() : "HOOK")
                    .memoryStrength(strength).healthColor(health)
                    .feynmanCompleted(p != null && p.isFeynmanCompleted())
                    .xpReward(sc.getXpReward())
                    .practiceType(sc.getPracticeType() != null ? sc.getPracticeType().name() : "JAVA")
                    .learningObjectiveCount(objCount)
                    .hasChallenge(sc.getChallengeHtml() != null && !sc.getChallengeHtml().isBlank())
                    .hasMiniProject(sc.getMiniProjectHtml() != null && !sc.getMiniProjectHtml().isBlank())
                    .build();
        }).collect(Collectors.toList());

        return ResponseEntity.ok(ChunkDetailDto.builder()
                .id(cws.chunk().getId()).topicId(cws.chunk().getTopicId())
                .title(cws.chunk().getTitle())
                .glyph(cws.chunk().getGlyph()).status(cws.status())
                .subChunks(subDtos).build());
    }

    /** Fast estimate of a JSON array's length without full parsing. Returns 0 for null/blank/empty. */
    private int countJsonArraySize(String json) {
        if (json == null || json.isBlank() || json.equals("[]")) return 0;
        String trimmed = json.trim();
        if (!trimmed.startsWith("[")) return 0;
        // Count commas at depth-1 (simple heuristic, accurate for flat string arrays)
        int depth = 0, count = 1;
        for (char c : trimmed.toCharArray()) {
            if (c == '[' || c == '{') depth++;
            else if (c == ']' || c == '}') depth--;
            else if (c == ',' && depth == 1) count++;
        }
        return count;
    }

}
