package com.ambravate.polymath.academy.controller;

import com.ambravate.polymath.academy.dto.ChunkHealthDto;
import com.ambravate.polymath.academy.dto.DashboardDto;
import com.ambravate.polymath.academy.model.DashboardData;
import com.ambravate.polymath.academy.security.UserPrincipal;
import com.ambravate.polymath.academy.service.DashboardService;
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

  @GetMapping
  public ResponseEntity<DashboardDto> getDashboard(
      @AuthenticationPrincipal UserPrincipal user
  ) {
    DashboardData data = dashboardService.getDashboard(user.getId());

    List<ChunkHealthDto> healthDtos = data.chunkHealth()
        .stream()
        .map(chunkHealth ->
            ChunkHealthDto.aChunkHealthDto()
                .withChunkId(chunkHealth.chunkId())
                .withTitle(chunkHealth.title())
                .withGlyph(chunkHealth.glyph())
                .withStatus(chunkHealth.status())
                .withMemoryStrength(chunkHealth.memoryStrength())
                .withHealthColor(chunkHealth.healthColor())
                .withTotalSubChunks(chunkHealth.totalSubChunks())
                .withCompletedSubChunks(chunkHealth.completedSubChunks())
                .withTier(chunkHealth.tier())
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
            .withReviewsDue(data.reviewsDue())
            .withDailyGoalMinutes(data.dailyGoalMinutes())
            .withOverallProgress(data.overallProgress())
            .withChunkHealth(healthDtos)
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
}
