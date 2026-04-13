// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.polymath.academy.model;

import java.util.List;

public record DiagnosticSession(
    String sessionId,
    List<Question> questions
) {}
