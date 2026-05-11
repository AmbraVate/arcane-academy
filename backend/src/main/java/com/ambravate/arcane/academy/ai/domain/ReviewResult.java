// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.ai.domain;

import com.ambravate.arcane.academy.common.dto.BadgeDto;
import java.util.List;

public record ReviewResult(double score, int correct, int total,
                           List<QuestionResult> results,
                           List<BadgeDto> newBadges) {

}
