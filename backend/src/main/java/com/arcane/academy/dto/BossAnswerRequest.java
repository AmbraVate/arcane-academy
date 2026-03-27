package com.arcane.academy.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BossAnswerRequest {
    private String questionId;
    private String answer;
}
