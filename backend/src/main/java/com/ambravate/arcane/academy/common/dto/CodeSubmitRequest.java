package com.ambravate.arcane.academy.common.dto;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CodeSubmitRequest {
    @NotBlank @Size(max = 5000)
    private String code;
}
