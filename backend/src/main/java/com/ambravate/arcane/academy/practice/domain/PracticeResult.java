// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.practice.domain;

import com.ambravate.arcane.academy.common.dto.BadgeDto;
import java.util.List;

public record PracticeResult(boolean allPassed, List<TestResult> testResults, int xpEarned,
                             String mentorFeedback, String errorType, List<BadgeDto> newBadges) {

}
