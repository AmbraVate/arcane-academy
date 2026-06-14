package com.ambravate.arcane.academy.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FeynmanRequest {
    @NotBlank
    @Size(max = 10000)
    private String explanation;
}
