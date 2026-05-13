package com.ambravate.arcane.academy.common.dto;

import lombok.*;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(
    setterPrefix = "with",
    builderMethodName = "aDashboardDto"
)
public class DashboardDto {
    private int totalXp;
    private String rank;
    private int streakDays;
    private boolean streakAtRisk;
    private String currentPath;
    private boolean diagnosticCompleted;
    private Instant diagnosticCompletedAt;
    private int reviewsDue;
    private int dailyGoalMinutes;
    private double overallProgress;
    private List<ChunkHealthDto> chunkHealth;
}
