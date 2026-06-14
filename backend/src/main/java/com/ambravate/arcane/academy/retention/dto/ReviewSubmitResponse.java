package com.ambravate.arcane.academy.retention.dto;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewSubmitResponse {
    private List<LessonResult> results;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class LessonResult {
        private String lessonId;
        private List<QuestionResult> questions;
        private String newStabilityState;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class QuestionResult {
        private String questionId;
        private boolean correct;
        private String correctAnswer;
        private String explanationHtml;
    }
}
