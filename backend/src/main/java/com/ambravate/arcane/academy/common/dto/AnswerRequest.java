package com.ambravate.arcane.academy.common.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AnswerRequest {

    @NotNull
    @NotEmpty
    @Valid
    private List<AnswerItem> answers;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class AnswerItem {
        @NotBlank
        private String questionId;
        @NotBlank
        private String answer;
    }
}
