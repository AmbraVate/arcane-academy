package com.ambravate.polymath.academy.model;

public enum LearnerPath {
    FOUNDATION,
    PRACTITIONER,
    EXPERT,
    /**
     * Final integrative project tier — sits above EXPERT.
     * Used by capstone chunks (e.g. React's "Guild Portal" rx-d) that
     * synthesize all prior content into one deployable artefact.
     * Behaves like EXPERT for unlock/diagnostic purposes.
     */
    CAPSTONE
}
