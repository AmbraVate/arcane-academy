package com.ambravate.arcane.academy.practice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * R practice submission.
 *
 * <p>Tests run inside the frontend iframe via WebR (R compiled to WebAssembly,
 * loaded from CDN). The harness loads any seed dataset, executes the learner's
 * R code, and compares evaluated outputs (data frames, vectors, scalars, plots
 * via image hashes) against expected values. Per-test pass/fail is reported
 * back here, where the backend performs a structural sanity check on the R
 * source before awarding XP.
 *
 * <p>Same trust domain as SQL (see {@link SqlSubmitRequest}): client-trusted
 * test results, server-side structural validation. Documented in CLAUDE.md §0.
 */
@Getter @Setter @NoArgsConstructor
public class RSubmitRequest {

    /** The learner's R code (the editor contents). */
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
        /** Human-readable summary of what the test saw (e.g. "got mean=4.2, expected 4.2"). */
        private String actual;
    }
}
