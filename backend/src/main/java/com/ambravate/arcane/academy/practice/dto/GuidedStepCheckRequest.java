package com.ambravate.arcane.academy.practice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuidedStepCheckRequest {

    /**
     * The learner's answer.  May be empty string (intentional blank submission),
     * so {@code @NotNull} rather than {@code @NotBlank}.
     */
    @NotNull
    private String answer;
}
