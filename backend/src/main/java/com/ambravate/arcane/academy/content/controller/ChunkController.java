package com.ambravate.arcane.academy.content.controller;

import com.ambravate.arcane.academy.content.dto.ChunkDetailDto;
import com.ambravate.arcane.academy.content.dto.ChunkSummaryDto;
import com.ambravate.arcane.academy.practice.dto.SubChunkSummaryDto;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.content.domain.ChunkWithStatus;

import com.ambravate.arcane.academy.common.repository.SubChunkRepository;
import com.ambravate.arcane.academy.common.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.content.service.ChunkGraphService;
import com.ambravate.arcane.academy.ai.service.SpacingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

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
                    .prerequisiteIds(parsePrereqs(c.getPrerequisiteIds()))
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

        List<SubChunkSummaryDto> subDtos = subs.stream().map(sc -> {
            UserChunkProgress p = progressMap.get(sc.getId());
            double strength = p != null ? spacingService.computeDecayedStrength(p) : 0.0;
            String health = strength > 0.7 ? "GREEN" : strength >= 0.4 ? "YELLOW" : "RED";

            return SubChunkSummaryDto.builder()
                    .id(sc.getId()).title(sc.getTitle()).sortOrder(sc.getSortOrder())
                    .status(p != null ? p.getStatus().name() : "NOT_STARTED")
                    .currentPhase(p != null ? p.getCurrentPhase().name() : "HOOK")
                    .memoryStrength(strength).healthColor(health)
                    .feynmanCompleted(p != null && p.isFeynmanCompleted())
                    .xpReward(sc.getXpReward())
                    .build();
        }).collect(Collectors.toList());

        return ResponseEntity.ok(ChunkDetailDto.builder()
                .id(cws.chunk().getId()).topicId(cws.chunk().getTopicId())
                .title(cws.chunk().getTitle())
                .glyph(cws.chunk().getGlyph()).status(cws.status())
                .subChunks(subDtos).build());
    }

    private List<String> parsePrereqs(String json) {
        if (json == null || json.isBlank()) return List.of();
        try { return objectMapper.readValue(json, new TypeReference<>() {}); } catch (Exception e) { return List.of(); }
    }
}
