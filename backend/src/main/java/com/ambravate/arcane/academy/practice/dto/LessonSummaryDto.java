package com.ambravate.arcane.academy.practice.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LessonSummaryDto {
    private String id;
    private String title;
    private int sortOrder;
    private String status;
    private String currentPhase;
    private double memoryStrength;
    private String healthColor;
    private boolean feynmanCompleted;
    private int xpReward;
    private String practiceType;
    private int learningObjectiveCount;
    private boolean hasChallenge;
    private boolean hasMiniProject;
}
