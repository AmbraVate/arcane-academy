package com.ambravate.arcane.academy.practice.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

/**
 * Unified request body for all solo-practice submission types.
 *
 * <p>Which fields are used depends on the lesson's {@code soloAssessmentType}:
 * <ul>
 *   <li>{@code DETERMINISTIC (code)} — {@link #code}</li>
 *   <li>{@code DETERMINISTIC (written NONE)} — {@link #answer}</li>
 *   <li>{@code RUBRIC_REFLECTION} — {@link #answer}, {@link #checkedRubricItems},
 *       {@link #confidence}</li>
 *   <li>{@code PATTERN_MATCH} — {@link #answer}</li>
 *   <li>{@code AI_REVIEW} — {@link #answer}</li>
 * </ul>
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SoloSubmitRequest {

    /** Source code — used for DETERMINISTIC code-test path (JAVA / SQL / etc.). */
    @Size(max = 5000)
    private String code;

    /** Text answer — used for written / rubric / pattern-match / AI-review paths. */
    @Size(max = 10000)
    private String answer;

    /** Rubric items the learner checked off — used for RUBRIC_REFLECTION. */
    private List<String> checkedRubricItems;

    /**
     * Self-reported confidence level — used for RUBRIC_REFLECTION.
     * Expected values: {@code NOT_CONFIDENT | SOMEWHAT | CONFIDENT | VERY_CONFIDENT}
     */
    @Size(max = 30)
    private String confidence;
}
