package com.ambravate.arcane.academy.ai.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FeynmanResultDto {
    private double accuracy;
    private double completeness;
    private double simplicity;
    private double connection;
    private double overallScore;
    private String feedback;
    private int xpEarned;
}
