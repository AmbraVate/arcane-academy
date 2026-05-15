package com.ambravate.polymath.academy.dto.admin;

public record UserStatsDto(
        String userId,
        String username,
        String email,
        int totalXp,
        String rank,
        int streakDays,
        long subChunksCompleted,
        long chunksCompleted,
        long badgesEarned,
        long reviewSessionsCompleted,
        String joinedAt,
        String lastLoginAt,
        boolean blocked,
        String role
) {}
