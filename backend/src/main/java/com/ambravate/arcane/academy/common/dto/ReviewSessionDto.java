package com.ambravate.arcane.academy.common.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewSessionDto {
    private String sessionId;
    private String sessionType;
    private List<QuestionDto> questions;
    private int estimatedMinutes;
}
