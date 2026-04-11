package com.ambravate.polymath.academy.dto;

import lombok.*;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DiagnosticResultDto {
    private String recommendedPath;
    private Map<String, String> chunkRecommendations;
    private double overallScore;
}
