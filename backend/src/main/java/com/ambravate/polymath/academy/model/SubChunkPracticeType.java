package com.ambravate.polymath.academy.model;

/** Determines which editor and submission pipeline the student uses in GUIDED_PRACTICE. */
public enum SubChunkPracticeType {
    /** Standard Java compilation + test runner. */
    JAVA,
    /** HTML/Tailwind editor with live preview; validated by CSS class inspection. */
    TAILWIND,
    /**
     * React/JSX editor with iframe preview (React + Babel CDNs).
     * Tests run client-side against the rendered DOM; results posted to the
     * backend for XP awarding (see ReactPracticeService).
     */
    REACT,
    /** No coding practice — explanation and retrieval check only. */
    NONE
}
