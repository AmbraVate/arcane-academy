package com.ambravate.arcane.academy.profile.controller;

import com.ambravate.arcane.academy.profile.dto.ModuleHealthDto;
import com.ambravate.arcane.academy.profile.dto.DashboardDto;
import com.ambravate.arcane.academy.common.domain.DashboardData;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.profile.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

  private final DashboardService dashboardService;
  private final LearningModuleRepository learningModuleRepository;
  private final LessonRepository lessonRepository;

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

    // First module of the first tier is shown as AVAILABLE (teaser), rest LOCKED
    final boolean[] first = {true};
    List<ModuleHealthDto> healthDtos = modules.stream().map(m -> {
      String status = first[0] ? "NOT_STARTED" : "LOCKED";
      first[0] = false;
      return ModuleHealthDto.builder()
          .moduleId(m.getId())
          .title(m.getTitle())
          .glyph(m.getGlyph())
          .status(status)
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
            .build()
    );
  }
}
