package com.ambravate.arcane.academy.retention.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReviewSubmitRequest {

    @NotEmpty
    private List<LessonAnswers> lessons;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class LessonAnswers {
        @NotBlank private String lessonId;
        @NotEmpty private List<QuestionAnswer> answers;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class QuestionAnswer {
        @NotBlank private String questionId;
        private String answer;
    }
}
