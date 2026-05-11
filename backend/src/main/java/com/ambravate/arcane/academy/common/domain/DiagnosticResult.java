// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.common.domain;

import java.util.Map;

public record DiagnosticResult(
    LearnerPath recommendedPath,
    Map<String, String> chunkRecommendations,
    double overallScore
) {

}
