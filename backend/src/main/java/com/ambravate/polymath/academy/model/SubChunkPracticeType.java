package com.ambravate.polymath.academy.model;

/** Determines which editor and submission pipeline the student uses in GUIDED_PRACTICE. */
public enum SubChunkPracticeType {
    /** Standard Java compilation + test runner. */
    JAVA,
    /** HTML/Tailwind editor with live preview; validated by CSS class inspection. */
    TAILWIND,
    /** No coding practice — explanation and retrieval check only. */
    NONE
}
