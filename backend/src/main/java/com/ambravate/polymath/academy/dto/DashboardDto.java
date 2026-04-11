package com.ambravate.polymath.academy.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardDto {
    private int totalXp;
    private String rank;
    private int streakDays;
    private boolean streakAtRisk;
    private String currentPath;
    private boolean diagnosticCompleted;
    private int reviewsDue;
    private int dailyGoalMinutes;
    private double overallProgress;
    private List<ChunkHealthDto> chunkHealth;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ChunkHealthDto {
        private String chunkId;
        private String title;
        private String glyph;
        private String status;
        private double memoryStrength;
        private String healthColor;
        private int totalSubChunks;
        private int completedSubChunks;
    }
}
