package com.ambravate.arcane.academy.practice.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Result of checking a single guided step answer.
 *
 * <ul>
 *   <li>{@link #passed} — whether the answer was accepted.</li>
 *   <li>{@link #feedback} — always present; explains what was right/wrong.</li>
 *   <li>{@link #reflectionPrompt} — only present on a pass; prompts deeper thinking.</li>
 *   <li>{@link #nextStepId} — ID of the following step, or {@code null} if this was last.</li>
 * </ul>
 */
@Getter
@Builder
public class GuidedStepCheckResponse {
    private boolean passed;
    private String  feedback;
    /** Non-null when {@link #passed} is {@code true} and the step has a reflection. */
    private String  reflectionPrompt;
    /** Non-null when {@link #passed} is {@code true}; null if this is the final step. */
    private String  nextStepId;
}
