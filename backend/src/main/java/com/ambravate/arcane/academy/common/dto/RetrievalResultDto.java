package com.ambravate.arcane.academy.common.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RetrievalResultDto {
    private double score;
    private int correct;
    private int total;
    private List<QuestionResultDto> results;
    private boolean passed;
    private int xpEarned;
    private List<BadgeDto> newBadges;
    private String recommendation;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class QuestionResultDto {
        private String questionId;
        private boolean correct;
        private String userAnswer;
        private String correctAnswer;
        private String explanationHtml;
    }
}
