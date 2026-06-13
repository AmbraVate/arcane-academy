package com.ambravate.arcane.academy.profile.controller;

import com.ambravate.arcane.academy.profile.dto.ModuleHealthDto;
import com.ambravate.arcane.academy.profile.dto.DashboardDto;
import com.ambravate.arcane.academy.common.domain.DashboardData;
import com.ambravate.arcane.academy.common.domain.Domain;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.content.repository.DomainRepository;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.profile.service.DashboardService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

  private final DashboardService dashboardService;
  private final LearningModuleRepository learningModuleRepository;
  private final LessonRepository lessonRepository;
  private final DomainRepository domainRepository;
  private final ObjectMapper objectMapper;

  @GetMapping
  public ResponseEntity<DashboardDto> getDashboard(
      @RequestParam(defaultValue = "java") String domainId,
      @AuthenticationPrincipal UserPrincipal user
  ) {
    DashboardData data = dashboardService.getDashboard(user.getId(), domainId);

    List<ModuleHealthDto> healthDtos = data.moduleHealth()
        .stream()
        .map(moduleHealth ->
            ModuleHealthDto.builder()
                .moduleId(moduleHealth.moduleId())
                .title(moduleHealth.title())
                .glyph(moduleHealth.glyph())
                .status(moduleHealth.status())
                .memoryStrength(moduleHealth.memoryStrength())
                .healthColor(moduleHealth.healthColor())
                .totalLessons(moduleHealth.totalLessons())
                .completedLessons(moduleHealth.completedLessons())
                .tier(moduleHealth.tier())
                .build()
        ).collect(Collectors.toList());

    Map<String, String> tierLabels = parseTierLabels(domainId);

    return ResponseEntity.ok(
        DashboardDto.aDashboardDto()
            .withTotalXp(data.totalXp())
            .withRank(data.rank())
            .withStreakDays(data.streakDays())
            .withStreakAtRisk(data.streakAtRisk())
            .withCurrentPath(data.currentPath().name())
            .withDiagnosticCompleted(data.diagnosticCompleted())
            .withDiagnosticCompletedAt(data.diagnosticCompletedAt())
            .withReviewsDue(data.reviewsDue())
            .withDailyGoalMinutes(data.dailyGoalMinutes())
            .withOverallProgress(data.overallProgress())
            .withModuleHealth(healthDtos)
            .withTierLabels(tierLabels)
            .build()
    );
  }

  @GetMapping("/reviews-due")
  public ResponseEntity<Map<String, Integer>> getReviewsDue(
      @AuthenticationPrincipal UserPrincipal user
  ) {
    DashboardData data = dashboardService.getDashboard(user.getId());
    return ResponseEntity.ok(Map.of("count", data.reviewsDue()));
  }

  /**
   * Public endpoint — returns domain module structure (titles, tier, lesson counts)
   * with all statuses LOCKED (first module AVAILABLE as teaser). No user data.
   */
  @GetMapping("/public")
  public ResponseEntity<DashboardDto> getPublicDashboard(
      @RequestParam(defaultValue = "java") String domainId
  ) {
    List<LearningModule> modules = learningModuleRepository.findByTrackIdOrderBySortOrderAsc(domainId);
    List<String> moduleIds = modules.stream().map(LearningModule::getId).toList();

    // Batch-fetch lesson counts to avoid N+1
    Map<String, Long> countByModule = lessonRepository.findByModuleIdIn(moduleIds)
        .stream().collect(Collectors.groupingBy(
            com.ambravate.arcane.academy.common.domain.Lesson::getModuleId,
            Collectors.counting()
        ));

    // All modules are LOCKED for public browsing (curriculum view only)
    List<ModuleHealthDto> healthDtos = modules.stream().map(m -> {
      return ModuleHealthDto.builder()
          .moduleId(m.getId())
          .title(m.getTitle())
          .glyph(m.getGlyph())
          .status("LOCKED")
          .memoryStrength(0.0)
          .healthColor("GREEN")
          .totalLessons(countByModule.getOrDefault(m.getId(), 0L).intValue())
          .completedLessons(0)
          .tier(m.getTier().name())
          .build();
    }).toList();

    return ResponseEntity.ok(
        DashboardDto.aDashboardDto()
            .withTotalXp(0)
            .withRank("Novice")
            .withStreakDays(0)
            .withStreakAtRisk(false)
            .withCurrentPath("APPRENTICE")
            .withDiagnosticCompleted(false)
            .withDiagnosticCompletedAt(null)
            .withReviewsDue(0)
            .withDailyGoalMinutes(0)
            .withOverallProgress(0.0)
            .withModuleHealth(healthDtos)
            .withTierLabels(parseTierLabels(domainId))
            .build()
    );
  }

  private Map<String, String> parseTierLabels(String domainId) {
    return domainRepository.findById(domainId)
        .map(Domain::getTierLabels)
        .filter(json -> json != null && !json.isBlank())
        .flatMap(json -> {
            try {
                return java.util.Optional.of(
                    objectMapper.readValue(json, new TypeReference<Map<String, String>>() {})
                );
            } catch (Exception e) {
                log.warn("Failed to parse tier_labels for domain {}: {}", domainId, e.getMessage());
                return java.util.Optional.<Map<String, String>>empty();
            }
        })
        .orElse(null);
  }
}
