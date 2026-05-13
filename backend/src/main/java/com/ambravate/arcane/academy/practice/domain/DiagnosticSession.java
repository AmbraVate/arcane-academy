// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.practice.domain;

import com.ambravate.arcane.academy.common.domain.Question;
import java.util.List;

public record DiagnosticSession(
    String sessionId,
    List<Question> questions
) {}
