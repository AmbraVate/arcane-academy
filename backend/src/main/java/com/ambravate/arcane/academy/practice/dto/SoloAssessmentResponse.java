package com.ambravate.arcane.academy.practice.dto;

import com.ambravate.arcane.academy.common.dto.BadgeDto;
import lombok.*;

import java.util.List;

/**
 * HTTP response for {@code POST /api/encoding/{lessonId}/solo-practice/submit}.
 * Replaces {@link SubmitResponse} for all solo-practice assessment types.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SoloAssessmentResponse {

    /** True when the attempt counts as a pass for this lesson's solo practice. */
    private boolean passed;

    /**
     * Keyword-band score — only set for PATTERN_MATCH assessments.
     * Values: {@code WEAK | GOOD | EXCELLENT}
     */
    private String band;

    /** Feedback text shown to the learner (AI / scoring / mentor voice). */
    private String feedback;

    /**
     * Rendered model-answer HTML — returned for all non-DETERMINISTIC types
     * after the first submission attempt.
     */
    private String modelAnswerHtml;

    /** Keywords matched in the answer — only set for PATTERN_MATCH. */
    private List<String> matchedKeywords;

    /**
     * Error classification for DETERMINISTIC code paths.
     * Values: {@code null | COMPILE_ERROR | RUNTIME_ERROR | TEST_FAILURE}
     */
    private String errorType;

    private int xpEarned;

    private List<BadgeDto> newBadges;

    /** Remaining AI-review quota for the current month. Only meaningful for AI_REVIEW. */
    private int aiReviewsRemaining;

    /** True when the Claude API was actually called for this submission. */
    private boolean usedAi;

    /** Test-case results — only populated for DETERMINISTIC code paths. */
    private List<SubmitResponse.TestResult> testResults;
}
