// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.practice.domain;

import com.ambravate.arcane.academy.common.domain.LearnerPath;
import java.util.Map;

public record DiagnosticResult(
    LearnerPath recommendedPath,
    Map<String, String> chunkRecommendations,
    double overallScore
) {

}
