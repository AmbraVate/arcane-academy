package com.ambravate.arcane.academy.ai.domain;

/**
 * FSRS card state in the spaced-repetition lifecycle.
 *
 * <p>Transitions:
 * <pre>
 *  NEW ──(first review)──► REVIEW
 *                          REVIEW ──(pass)──► REVIEW (increased stability)
 *                          REVIEW ──(fail)──► RELEARNING (lapse)
 *                          RELEARNING ──(pass)──► REVIEW (restored)
 * </pre>
 *
 * <p>The LEARNING state (intra-day steps of minutes/hours) is intentionally
 * omitted from this implementation; Arcane Academy schedules in whole days only.
 */
public enum FsrsState {
    /** Card has never been reviewed; no stability or difficulty has been established. */
    NEW,
    /** Card is actively being reviewed on a daily+ schedule. */
    REVIEW,
    /** Card was forgotten in REVIEW and is recovering (lapse counted). */
    RELEARNING
}
