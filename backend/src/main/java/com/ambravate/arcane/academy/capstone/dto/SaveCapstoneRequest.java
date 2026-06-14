package com.ambravate.arcane.academy.capstone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SaveCapstoneRequest(
        @NotBlank String moduleId,
        @NotBlank @Size(max = 255) String title,
        String description,
        String codeContent,
        @Size(max = 500) String githubUrl
) {}
