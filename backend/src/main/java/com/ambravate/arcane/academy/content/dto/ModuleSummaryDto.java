package com.ambravate.arcane.academy.content.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ModuleSummaryDto {
    private String id;
    private String title;
    private String glyph;
    private String status;
    private int totalLessons;
    private int completedLessons;
    private double memoryStrength;
    private String healthColor;
    private List<String> prerequisiteIds;
}
