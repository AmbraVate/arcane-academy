// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.ai.domain;

import java.util.List;

public record GradeResult(double score, int correct, int total, List<QuestionResult> results) {

}
