package com.ambravate.arcane.academy.common.domain;

/**
 * Determines how a lesson's solo practice is assessed.
 *
 * <ul>
 *   <li>{@code DETERMINISTIC} — existing code-test or written-response path (default).</li>
 *   <li>{@code RUBRIC_REFLECTION} — self-checked rubric + confidence rating; always passes;
 *       model-answer exemplar is revealed after submission.</li>
 *   <li>{@code PATTERN_MATCH} — keyword-count scoring (WEAK / GOOD / EXCELLENT bands);
 *       GOOD or above counts as a pass.</li>
 *   <li>{@code AI_REVIEW} — Claude AI review; limited to LEAD tier; 3 uses/month;
 *       downgrades to {@code RUBRIC_REFLECTION} when quota exhausted.</li>
 * </ul>
 */
public enum SoloAssessmentType {
    DETERMINISTIC,
    RUBRIC_REFLECTION,
    PATTERN_MATCH,
    AI_REVIEW
}
