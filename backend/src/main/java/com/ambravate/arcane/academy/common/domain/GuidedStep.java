package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * A single step in a lesson's Guided Practice Quest workbook.
 *
 * <h2>Marking rule JSON schema</h2>
 * <pre>
 * {
 *   "matchMode": "EXACT | NORMALIZED | REGEX | CONTAINS | OUTPUT_MATCH",
 *   "accepted":  ["answer1", "answer2"],   // all trigger a pass
 *   "allowed":   ["okAnswer"],             // pass with different feedback
 *   "rejectedFeedback": "Try again — think about..."
 * }
 * </pre>
 *
 * <h2>Input config JSON (examples)</h2>
 * <pre>
 * FILL_BLANK:       { "placeholder": "variable name" }
 * MULTIPLE_CHOICE:  { "options": ["age", "number", "x", "value"] }
 * SHORT_TEXT:       { "minWords": 8 }
 * CODE:             { "language": "java", "starterCode": "int x = ___;" }
 * DRAG_DROP:        { "items": [...], "targets": [...] }
 * SEQUENCE:         { "items": [...] }
 * </pre>
 */
@Entity
@Table(name = "guided_steps",
    indexes = @Index(name = "idx_guided_steps_lesson_id", columnList = "lesson_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GuidedStep {

    @Id
    private String id;

    @Column(name = "lesson_id", nullable = false)
    private String lessonId;

    @Column(name = "sort_order")
    @Builder.Default
    private int sortOrder = 0;

    @Column(name = "instruction_html", columnDefinition = "TEXT")
    private String instructionHtml;

    @Enumerated(EnumType.STRING)
    @Column(name = "input_type", nullable = false)
    @Builder.Default
    private GuidedStepInputType inputType = GuidedStepInputType.SHORT_TEXT;

    /** Serialised JSON config for the input widget (structure depends on {@link #inputType}). */
    @Column(name = "input_config_json", columnDefinition = "TEXT")
    private String inputConfigJson;

    /**
     * Serialised JSON marking rule (matchMode, accepted[], allowed[], rejectedFeedback).
     * Null means the step has no automated checking — a teacher must review it manually.
     */
    @Column(name = "marking_rule_json", columnDefinition = "TEXT")
    private String markingRuleJson;

    /** Optional hint shown progressively on learner request. */
    @Column(name = "hint_html", columnDefinition = "TEXT")
    private String hintHtml;

    /** Reflection prompt revealed after the learner passes this step. */
    @Column(name = "reflection_prompt_html", columnDefinition = "TEXT")
    private String reflectionPromptHtml;
}
