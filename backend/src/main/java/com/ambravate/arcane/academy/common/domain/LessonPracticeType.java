package com.ambravate.arcane.academy.common.domain;

/** Determines which editor and submission pipeline the student uses in GUIDED_PRACTICE. */
public enum LessonPracticeType {
    /** Standard Java compilation + test runner (used by Software Engineering content). */
    JAVA,
    /** No coding practice — written response, explanation, or retrieval check only. */
    NONE
}
