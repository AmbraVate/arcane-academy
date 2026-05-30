package com.ambravate.arcane.academy.practice.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * A single guided-step card returned to the learner.
 *
 * <p>{@link #hintHtml} is always included so the frontend can reveal it
 * client-side on demand without a round-trip.
 *
 * <p>{@link #completed} is {@code true} when the learner has already passed
 * this step in a previous session — used for mid-quest resume.
 */
@Getter
@Builder
public class GuidedStepDto {
    private String  id;
    private int     sortOrder;
    private String  instructionHtml;
    private String  inputType;
    /** Parsed input configuration (structure depends on {@code inputType}). */
    private Object  inputConfig;
    private String  hintHtml;
    private boolean completed;
}
