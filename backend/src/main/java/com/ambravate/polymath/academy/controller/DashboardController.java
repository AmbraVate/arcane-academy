package com.ambravate.polymath.academy.controller;

import com.ambravate.polymath.academy.dto.DashboardDto;
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
            @AuthenticationPrincipal UserPrincipal user) {
        DashboardService.DashboardData data = dashboardService.getDashboard(user.getId());

        List<DashboardDto.ChunkHealthDto> healthDtos = data.chunkHealth().stream().map(h ->
                DashboardDto.ChunkHealthDto.builder()
                        .chunkId(h.chunkId()).title(h.title()).glyph(h.glyph())
                        .status(h.status()).memoryStrength(h.memoryStrength())
                        .healthColor(h.healthColor())
                        .totalSubChunks(h.totalSubChunks())
                        .completedSubChunks(h.completedSubChunks())
                        .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok(DashboardDto.builder()
                .totalXp(data.totalXp()).rank(data.rank())
                .streakDays(data.streakDays()).streakAtRisk(data.streakAtRisk())
                .currentPath(data.currentPath().name())
                .diagnosticCompleted(data.diagnosticCompleted())
                .reviewsDue(data.reviewsDue())
                .dailyGoalMinutes(data.dailyGoalMinutes())
                .overallProgress(data.overallProgress())
                .chunkHealth(healthDtos)
                .build());
    }

    @GetMapping("/reviews-due")
    public ResponseEntity<Map<String, Integer>> getReviewsDue(
            @AuthenticationPrincipal UserPrincipal user) {
        DashboardService.DashboardData data = dashboardService.getDashboard(user.getId());
        return ResponseEntity.ok(Map.of("count", data.reviewsDue()));
    }
}
