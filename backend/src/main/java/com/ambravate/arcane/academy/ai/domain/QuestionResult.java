// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.ai.domain;

public record QuestionResult(String questionId, String subChunkId, boolean correct,
                             String userAnswer, String correctAnswer, String explanationHtml) {

}
