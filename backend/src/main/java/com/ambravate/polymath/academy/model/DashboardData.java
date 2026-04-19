// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.polymath.academy.model;

import java.time.Instant;
import java.util.List;

public record DashboardData(
    int totalXp,
    String rank,
    int streakDays,
    boolean streakAtRisk,
    LearnerPath currentPath,
    boolean diagnosticCompleted,
    Instant diagnosticCompletedAt,
    int reviewsDue,
    int dailyGoalMinutes,
    double overallProgress,
    List<ChunkHealth> chunkHealth
) {}
