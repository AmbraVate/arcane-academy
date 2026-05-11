// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.gamification.domain;

public record LeaderboardEntry(
    int rank,
    String username,
    int xpEarned,         // XP earned in the leaderboard window (or topic for all-time)
    int globalXp,         // user.totalXp across all topics
    int streakDays,
    String rankTitle,
    int topicCount,       // -1 if not applicable (only set for polymath board)
    int badgeCount
) {

}
