package com.ambravate.arcane.academy.gamification.controller;

import com.ambravate.arcane.academy.gamification.service.LeaderboardService;
import com.ambravate.arcane.academy.gamification.domain.LeaderboardEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Read-only leaderboard endpoints.
 * <ul>
 *   <li>{@code GET /api/leaderboard/topic/{topicId}/weekly} — top 20 in topic, this ISO-week</li>
 *   <li>{@code GET /api/leaderboard/topic/{topicId}/all-time} — top 20 in topic, since signup</li>
 *   <li>{@code GET /api/leaderboard/polymath} — top polymaths by topic breadth (tie-break: total XP)</li>
 * </ul>
 *
 * <p>Only users with {@code publicProfileEnabled=true} appear in any board.
 */
@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/topic/{topicId}/weekly")
    public ResponseEntity<List<LeaderboardEntry>> topicWeekly(
        @PathVariable String topicId,
        @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(leaderboardService.topicWeekly(topicId, clamp(limit)));
    }

    @GetMapping("/topic/{topicId}/all-time")
    public ResponseEntity<List<LeaderboardEntry>> topicAllTime(
        @PathVariable String topicId,
        @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(leaderboardService.topicAllTime(topicId, clamp(limit)));
    }

    @GetMapping("/polymath")
    public ResponseEntity<List<LeaderboardEntry>> polymath(
        @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(leaderboardService.polymath(clamp(limit)));
    }

    /** Defensive cap so a malicious client can't request 1M rows. */
    private int clamp(int limit) {
        return Math.max(1, Math.min(100, limit));
    }
}
