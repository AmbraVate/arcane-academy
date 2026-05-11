// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.ai.domain;

import com.ambravate.arcane.academy.common.domain.Question;
import java.util.List;

public record ReviewSessionQuestions(List<Question> questions, String sessionId) {

  public ReviewSessionQuestions(List<Question> questions) {
    this(questions, null);
  }
}
