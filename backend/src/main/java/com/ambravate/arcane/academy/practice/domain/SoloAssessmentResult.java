package com.ambravate.arcane.academy.practice.domain;

import com.ambravate.arcane.academy.common.dto.BadgeDto;

import java.util.List;

/**
 * Domain result of a solo-practice submission.
 * Carries fields for all assessment types; unused fields are null / zero.
 *
 * @param passed           true when the submission counts as a pass
 * @param testResults      populated for DETERMINISTIC code/written paths
 * @param xpEarned         XP awarded (0 for non-passing attempts)
 * @param feedback         mentor / AI / scoring feedback text
 * @param errorType        null | COMPILE_ERROR | RUNTIME_ERROR | TEST_FAILURE | TEXT_CHECK_FAILURE
 * @param newBadges        badges newly earned this submission
 * @param band             WEAK | GOOD | EXCELLENT — only set for PATTERN_MATCH
 * @param modelAnswerHtml  exemplar answer HTML; always returned for non-DETERMINISTIC types
 * @param matchedKeywords  keywords found in the answer — only set for PATTERN_MATCH
 * @param aiReviewsRemaining remaining AI reviews for the current month
 * @param usedAi           true when the Claude API was called for this submission
 */
public record SoloAssessmentResult(
        boolean passed,
        List<TestResult> testResults,
        int xpEarned,
        String feedback,
        String errorType,
        List<BadgeDto> newBadges,
        String band,
        String modelAnswerHtml,
        List<String> matchedKeywords,
        int aiReviewsRemaining,
        boolean usedAi
) {}
