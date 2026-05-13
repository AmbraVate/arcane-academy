// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.common.domain;

import java.util.List;

public record DiagnosticSession(
    String sessionId,
    List<Question> questions
) {}
