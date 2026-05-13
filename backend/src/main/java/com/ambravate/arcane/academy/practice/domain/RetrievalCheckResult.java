// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.practice.domain;

import com.ambravate.arcane.academy.ai.domain.QuestionResult;
import com.ambravate.arcane.academy.common.dto.BadgeDto;
import java.util.List;

public record RetrievalCheckResult(double score, int correct, int total,
                                   List<QuestionResult> results,
                                   boolean passed, int xpEarned, List<BadgeDto> newBadges,
                                   String recommendation) {

}
