package com.ambravate.arcane.academy.profile.domain;

public record LeaderboardEntry(
    int rank,
    String username,
    int xpEarned,
    int globalXp,
    int streakDays,
    String rankTitle,
    int topicCount,
    int badgeCount
) {}
