package com.ambravate.arcane.academy.content.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChunkSummaryDto {
    private String id;
    private String title;
    private String glyph;
    private String status;
    private int totalSubChunks;
    private int completedSubChunks;
    private double memoryStrength;
    private String healthColor;
    private List<String> prerequisiteIds;
}
