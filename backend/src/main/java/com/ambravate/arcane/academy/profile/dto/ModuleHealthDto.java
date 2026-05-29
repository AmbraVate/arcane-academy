package com.ambravate.arcane.academy.profile.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ModuleHealthDto {
    private String moduleId;
    private String title;
    private String glyph;
    private String status;
    private double memoryStrength;
    private String healthColor;
    private int totalLessons;
    private int completedLessons;
    private String tier;
}
