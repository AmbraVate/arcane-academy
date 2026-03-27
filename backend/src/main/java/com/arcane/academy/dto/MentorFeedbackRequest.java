// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.arcane.academy.dto;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MentorFeedbackRequest {
    @NotBlank private String questTitle;
    @NotBlank private String topic;
    @NotBlank private String problemDescription;
    @NotBlank @Size(max = 5000) private String code;
    private String failedTests;
}
