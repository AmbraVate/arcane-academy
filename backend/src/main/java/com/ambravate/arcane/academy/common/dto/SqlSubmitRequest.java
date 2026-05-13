package com.ambravate.arcane.academy.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * SQL practice submission.
 *
 * <p>Tests run inside the frontend iframe via sql.js (SQLite-WASM, loaded from CDN).
 * The harness creates an in-memory database, runs the seed SQL, executes the
 * learner's query, and compares the result rows to expected rows. Per-test
 * pass/fail is reported back here, where the backend performs a structural sanity
 * check on the SQL source before awarding XP.
 *
 * <p>Same trust domain as React (see {@link ReactSubmitRequest}): client-trusted
 * test results, server-side structural validation. Documented in CLAUDE.md §0.
 */
@Getter @Setter @NoArgsConstructor
public class SqlSubmitRequest {

    /** The learner's SQL query (the editor contents). */
    private String code;

    /**
     * Per-test pass/fail reported by the in-iframe test runner.
     * Order matches the sub-chunk's guidedPracticeTests order.
     */
    private List<ClientTestResult> clientTestResults;

    @Getter @Setter @NoArgsConstructor
    public static class ClientTestResult {
        private String label;
        private boolean passed;
        /** Human-readable summary of what the test saw (e.g. "got 4 rows, expected 4"). */
        private String actual;
    }
}
