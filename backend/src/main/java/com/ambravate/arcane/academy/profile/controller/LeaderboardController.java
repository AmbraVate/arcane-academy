package com.ambravate.arcane.academy.profile.controller;

import com.ambravate.arcane.academy.profile.domain.LeaderboardEntry;
import com.ambravate.arcane.academy.profile.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Read-only leaderboard endpoints. Moved from gamification to profile so that
 * leaderboard's read access to practice repositories does not create a module cycle.
 */
@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/topic/{topicId}/weekly")
    public ResponseEntity<List<LeaderboardEntry>> topicWeekly(
        @PathVariable String topicId,
        @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(leaderboardService.topicWeekly(topicId, clamp(limit)));
    }

    @GetMapping("/topic/{topicId}/all-time")
    public ResponseEntity<List<LeaderboardEntry>> topicAllTime(
        @PathVariable String topicId,
        @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(leaderboardService.topicAllTime(topicId, clamp(limit)));
    }

    @GetMapping("/polymath")
    public ResponseEntity<List<LeaderboardEntry>> polymath(
        @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(leaderboardService.polymath(clamp(limit)));
    }

    private int clamp(int limit) {
        return Math.max(1, Math.min(100, limit));
    }
}
