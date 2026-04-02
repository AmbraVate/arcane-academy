package com.arcane.academy.dto;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProgressResponse {
    private int xpEarned;
    private int totalXp;
    private String rank;
    private int streakDays;
}
