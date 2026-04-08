package com.ambravate.polymath.academy.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BossAnswerResponse {
    private boolean correct;
    private String explanation;
    private String correctAnswer;
}
