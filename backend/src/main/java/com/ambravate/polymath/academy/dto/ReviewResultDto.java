package com.ambravate.polymath.academy.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewResultDto {
    private double score;
    private int correct;
    private int total;
    private List<RetrievalResultDto.QuestionResultDto> results;
    private List<BadgeDto> newBadges;
}
