// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.ai.domain;

public record FeynmanResult(double accuracy, double completeness, double simplicity,
                            double connection, double overallScore, String feedback, int xpEarned) {

}
