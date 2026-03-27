// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.arcane.academy.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BossAnswerResponse {
    private boolean correct;
    private String explanation;
    private String correctAnswer;
}
