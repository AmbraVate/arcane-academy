// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.arcane.academy.dto;
import lombok.*;
import java.util.List;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SubmitResponse {
    private boolean allPassed;
    private List<TestResult> testResults;
    private int xpEarned;
    private String mentorFeedback;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class TestResult {
        private String label;
        private boolean passed;
        private String actualOutput;
        private String expectedOutput;
    }
}
