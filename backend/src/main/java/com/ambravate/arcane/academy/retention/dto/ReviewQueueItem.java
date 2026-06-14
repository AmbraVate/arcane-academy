package com.ambravate.arcane.academy.retention.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewQueueItem {
    private String lessonId;
    private String lessonTitle;
    private double retentionScore;
    private String stabilityState;
    private LocalDate nextReviewDate;
    private List<ReviewQuestion> questions;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ReviewQuestion {
        private String questionId;
        private String questionText;
        private List<String> options;
        private String inputType;
    }
}
