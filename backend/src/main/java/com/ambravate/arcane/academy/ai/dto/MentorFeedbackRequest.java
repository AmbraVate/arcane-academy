package com.ambravate.arcane.academy.ai.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MentorFeedbackRequest {
    @NotBlank @Size(max = 200)  private String questTitle;
    @NotBlank @Size(max = 200)  private String topic;
    @NotBlank @Size(max = 5000) private String problemDescription;
    @NotBlank @Size(max = 5000) private String code;
              @Size(max = 2000) private String failedTests;
}
