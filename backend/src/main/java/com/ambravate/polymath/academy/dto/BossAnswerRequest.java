package com.ambravate.polymath.academy.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BossAnswerRequest {
    private String questionId;
    private String answer;
}
