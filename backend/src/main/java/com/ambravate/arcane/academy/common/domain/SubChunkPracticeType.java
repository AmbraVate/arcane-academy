package com.ambravate.arcane.academy.common.domain;

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
    /**
     * SQL editor with sql.js (SQLite-WASM via CDN) running in an iframe.
     * Tests run client-side: the seed SQL builds an in-memory database,
     * the user's query executes, and the result rows are compared to an
     * expected row set or to a reference query. Results are posted back
     * to the backend for XP awarding (see SqlPracticeService).
     */
    SQL,
    /** No coding practice — explanation and retrieval check only. */
    NONE
}
