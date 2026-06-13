// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.ai.domain;

public record FeynmanResult(double accuracy, double completeness, double simplicity,
                            double connection, double overallScore, String feedback, int xpEarned,
                            /** "PATTERN_MATCH" or "CODE_EXECUTION" — tells the frontend which result view to render. */
                            String teachBackMode) {

    /** Convenience factory for pattern-match results (existing Feynman path). */
    public static FeynmanResult patternMatch(double accuracy, double completeness, double simplicity,
                                             double connection, double overallScore, String feedback, int xpEarned) {
        return new FeynmanResult(accuracy, completeness, simplicity, connection, overallScore, feedback, xpEarned, "PATTERN_MATCH");
    }

    /** Convenience factory for code-execution results. */
    public static FeynmanResult codeExecution(double accuracy, double completeness, double simplicity,
                                              double connection, double overallScore, String feedback, int xpEarned) {
        return new FeynmanResult(accuracy, completeness, simplicity, connection, overallScore, feedback, xpEarned, "CODE_EXECUTION");
    }
}
