package com.ambravate.polymath.academy.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * React practice submission.
 *
 * <p>Unlike Java (server compiles + runs) or Tailwind (server parses HTML with Jsoup),
 * React submissions cannot be executed server-side without a JavaScript engine.
 * The frontend's iframe sandbox runs the tests against the rendered DOM and reports
 * the results back here. The backend then performs lightweight structural validation
 * on the source as a sanity check before awarding XP.
 *
 * <p>This is a deliberate trade-off documented in CLAUDE.md §0:
 * single-player learning platform, no leaderboard fraud incentive in v1.
 * If/when a leaderboard ships, server-side verification (Playwright headless)
 * should be added.
 */
@Getter @Setter @NoArgsConstructor
public class ReactSubmitRequest {

    /** The learner's JSX source code (the editor contents). */
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
        /** Human-readable summary of what the test saw (e.g. "h1 contained 'Hello'"). */
        private String actual;
    }
}
