// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.ai.domain;

public record QuestionResult(String questionId, String lessonId, boolean correct,
                             String userAnswer, String correctAnswer, String explanationHtml) {

}
