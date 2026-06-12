package com.ambravate.arcane.academy.content.controller;

import com.ambravate.arcane.academy.content.dto.ModuleDetailDto;
import com.ambravate.arcane.academy.content.dto.ModuleSummaryDto;
import com.ambravate.arcane.academy.content.dto.TopicDto;
import com.ambravate.arcane.academy.common.domain.Topic;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.TopicRepository;
import com.ambravate.arcane.academy.practice.dto.LessonSummaryDto;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.content.domain.ModuleWithStatus;

import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.content.service.ModuleGraphService;
import com.ambravate.arcane.academy.ai.service.SpacingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleGraphService moduleGraphService;
    private final LessonRepository lessonRepository;
    private final LearningModuleRepository learningModuleRepository;
    private final UserChunkProgressRepository progressRepository;
    private final SpacingService spacingService;
    private final TopicRepository topicRepository;

    @GetMapping
    public ResponseEntity<List<ModuleSummaryDto>> getAllModules(
            @AuthenticationPrincipal UserPrincipal user) {
        List<ModuleWithStatus> modules = moduleGraphService.getAllModulesWithStatus(user.getId());
        Map<String, UserChunkProgress> progressMap = progressRepository.findByUserId(user.getId()).stream()
                .collect(Collectors.toMap(UserChunkProgress::getLessonId, p -> p, (a, b) -> a));

        List<ModuleSummaryDto> result = modules.stream().map(mws -> {
            LearningModule m = mws.module();
            List<Lesson> lessons = lessonRepository.findByModuleIdOrderBySortOrderAsc(m.getId());
            int completed = (int) lessons.stream().filter(l -> {
                UserChunkProgress p = progressMap.get(l.getId());
                return p != null && (p.getStatus() == LessonStatus.COMPLETE || p.getStatus() == LessonStatus.SKIPPED);
            }).count();

            double avgStrength = lessons.stream()
                    .map(l -> progressMap.get(l.getId()))
                    .filter(Objects::nonNull)
                    .mapToDouble(spacingService::computeDecayedStrength)
                    .average().orElse(0.0);

            String health = avgStrength > 0.7 ? "GREEN" : avgStrength >= 0.4 ? "YELLOW" : "RED";

            return ModuleSummaryDto.builder()
                    .id(m.getId()).title(m.getTitle()).glyph(m.getGlyph())
                    .status(mws.status())
                    .totalLessons(lessons.size()).completedLessons(completed)
                    .memoryStrength(avgStrength).healthColor(health)
                    .prerequisiteIds(m.getPrerequisites().stream().map(LearningModule::getId).toList())
                    .build();
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<ModuleDetailDto> getModuleDetail(
            @PathVariable String moduleId,
            @AuthenticationPrincipal UserPrincipal user) {

        List<Lesson> lessons = lessonRepository.findByModuleIdOrderBySortOrderAsc(moduleId);
        List<TopicDto> topicDtos = topicRepository.findByModuleIdOrderBySortOrderAsc(moduleId)
                .stream().map(t -> TopicDto.builder()
                        .id(t.getId()).title(t.getTitle())
                        .purposeHtml(t.getPurposeHtml()).sortOrder(t.getSortOrder())
                        .build())
                .toList();

        // ── Public (unauthenticated) path ────────────────────────────────────────
        if (user == null) {
            LearningModule module = learningModuleRepository.findById(moduleId)
                    .orElseThrow(() -> new NoSuchElementException("Module not found: " + moduleId));

            // All lessons LOCKED for public browsing (curriculum view only)
            List<LessonSummaryDto> publicLessons = new ArrayList<>();
            for (int i = 0; i < lessons.size(); i++) {
                Lesson l = lessons.get(i);
                publicLessons.add(LessonSummaryDto.builder()
                        .id(l.getId()).title(l.getTitle()).sortOrder(l.getSortOrder())
                        .status("LOCKED")
                        .currentPhase("HOOK")
                        .memoryStrength(0.0).healthColor("GREEN")
                        .feynmanCompleted(false).xpReward(l.getXpReward())
                        .practiceType(l.getPracticeType() != null ? l.getPracticeType().name() : "JAVA")
                        .learningObjectiveCount(0)
                        .hasChallenge(false).hasMiniProject(false)
                        .topicId(l.getTopicId()).topicTitle(null)
                        .build());
            }
            return ResponseEntity.ok(ModuleDetailDto.builder()
                    .id(module.getId()).domainId(module.getTrackId())
                    .title(module.getTitle())
                    .glyph(module.getGlyph()).status("NOT_STARTED")
                    .topics(topicDtos).lessons(publicLessons).build());
        }

        // ── Authenticated path ────────────────────────────────────────────────────
        List<ModuleWithStatus> allModules = moduleGraphService.getAllModulesWithStatus(user.getId());
        ModuleWithStatus mws = allModules.stream()
                .filter(m -> m.module().getId().equals(moduleId))
                .findFirst().orElseThrow(() -> new NoSuchElementException("Module not found: " + moduleId));

        Map<String, UserChunkProgress> progressMap = progressRepository.findByUserId(user.getId()).stream()
                .collect(Collectors.toMap(UserChunkProgress::getLessonId, p -> p, (a, b) -> a));

        Map<String, Topic> topicById = topicRepository.findByModuleIdOrderBySortOrderAsc(moduleId)
                .stream().collect(Collectors.toMap(Topic::getId, t -> t, (a, b) -> a));

        Set<String> completedLessonIds = progressMap.values().stream()
                .filter(p -> p.getStatus() == LessonStatus.COMPLETE
                          || p.getStatus() == LessonStatus.SKIPPED)
                .map(UserChunkProgress::getLessonId)
                .collect(Collectors.toSet());

        List<LessonSummaryDto> lessonDtos = lessons.stream().map(l -> {
            UserChunkProgress p = progressMap.get(l.getId());
            double strength = p != null ? spacingService.computeDecayedStrength(p) : 0.0;
            String health = strength > 0.7 ? "GREEN" : strength >= 0.4 ? "YELLOW" : "RED";

            String effectiveStatus;
            if ("LOCKED".equals(mws.status())) {
                effectiveStatus = "LOCKED";
            } else if (p != null && (p.getStatus() == LessonStatus.COMPLETE
                    || p.getStatus() == LessonStatus.SKIPPED
                    || p.getStatus() == LessonStatus.IN_PROGRESS)) {
                effectiveStatus = p.getStatus().name();
            } else if (l.getSortOrder() > 1) {
                boolean allPriorDone = lessons.stream()
                        .filter(other -> other.getSortOrder() < l.getSortOrder())
                        .allMatch(other -> completedLessonIds.contains(other.getId()));
                effectiveStatus = allPriorDone
                        ? (p != null ? p.getStatus().name() : "NOT_STARTED")
                        : "LOCKED";
            } else {
                effectiveStatus = p != null ? p.getStatus().name() : "NOT_STARTED";
            }

            int objCount = countJsonArraySize(l.getLearningObjectivesJson());

            return LessonSummaryDto.builder()
                    .id(l.getId()).title(l.getTitle()).sortOrder(l.getSortOrder())
                    .status(effectiveStatus)
                    .currentPhase(p != null ? p.getCurrentPhase().name() : "HOOK")
                    .memoryStrength(strength).healthColor(health)
                    .feynmanCompleted(p != null && p.isFeynmanCompleted())
                    .xpReward(l.getXpReward())
                    .practiceType(l.getPracticeType() != null ? l.getPracticeType().name() : "JAVA")
                    .learningObjectiveCount(objCount)
                    .hasChallenge(l.getChallengeHtml() != null && !l.getChallengeHtml().isBlank())
                    .hasMiniProject(l.getMiniProjectHtml() != null && !l.getMiniProjectHtml().isBlank())
                    .topicId(l.getTopicId())
                    .topicTitle(l.getTopicId() != null && topicById.containsKey(l.getTopicId())
                            ? topicById.get(l.getTopicId()).getTitle() : null)
                    .build();
        }).collect(Collectors.toList());

        return ResponseEntity.ok(ModuleDetailDto.builder()
                .id(mws.module().getId()).domainId(mws.module().getTrackId())
                .title(mws.module().getTitle())
                .glyph(mws.module().getGlyph()).status(mws.status())
                .topics(topicDtos).lessons(lessonDtos).build());
    }

    private int countJsonArraySize(String json) {
        if (json == null || json.isBlank() || json.equals("[]")) return 0;
        String trimmed = json.trim();
        if (!trimmed.startsWith("[")) return 0;
        int depth = 0, count = 1;
        for (char c : trimmed.toCharArray()) {
            if (c == '[' || c == '{') depth++;
            else if (c == ']' || c == '}') depth--;
            else if (c == ',' && depth == 1) count++;
        }
        return count;
    }

}
