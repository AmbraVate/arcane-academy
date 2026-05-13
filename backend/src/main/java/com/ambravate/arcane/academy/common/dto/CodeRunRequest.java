package com.ambravate.arcane.academy.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CodeRunRequest {
    @NotBlank
    @Size(max = 5000)
    private String code;
    private String testInput;
}
