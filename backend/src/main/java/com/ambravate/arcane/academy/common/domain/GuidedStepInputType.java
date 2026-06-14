package com.ambravate.arcane.academy.common.domain;

/**
 * Describes how the learner interacts with a guided step.
 *
 * <ul>
 *   <li>{@link #FILL_BLANK}      — complete a partial code snippet or sentence</li>
 *   <li>{@link #MULTIPLE_CHOICE} — select one option from a list</li>
 *   <li>{@link #SHORT_TEXT}      — write a short prose answer (assessed by keyword matching)</li>
 *   <li>{@link #CODE}            — write or complete code in the Monaco editor</li>
 *   <li>{@link #DRAG_DROP}       — match items to targets by dragging</li>
 *   <li>{@link #SEQUENCE}        — arrange items in the correct order</li>
 * </ul>
 */
public enum GuidedStepInputType {
    FILL_BLANK, MULTIPLE_CHOICE, SHORT_TEXT, CODE, DRAG_DROP, SEQUENCE
}
