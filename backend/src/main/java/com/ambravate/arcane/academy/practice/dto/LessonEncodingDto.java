package com.ambravate.arcane.academy.practice.dto;

import com.ambravate.arcane.academy.common.dto.QuestionDto;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LessonEncodingDto {
    private String lessonId;
    private String moduleId;
    private String domainId;
    private String title;
    private String phase;
    private String status;

    private String hookHtml;
    private String explanationHtml;
    private Object storyBeats;
    private String guidedPracticeHtml;
    private String soloPracticeHtml;
    private String starterCode;
    private Object testCaseLabels;
    private String filename;
    private List<QuestionDto> retrievalQuestions;
    private String feynmanPrompt;
    private int xpReward;
    private String practiceType;

    private Object rabbitHoleTerms;
    private String modelAnswer;
    private String guidedPracticeModelAnswer;

    private List<String> learningObjectives;
    private Object challenge;
    private String miniProject;
    private List<String> commonMistakes;
    private List<String> assessmentCriteria;
    private Object downloadables;

    private String integrationPrompt;
    private String questType;

    // Phase 2 — Markdown section fields (null for JSON-seeded lessons)
    private String loreIntroHtml;
    private String whyItMattersHtml;
    private String workedExamplesHtml;
    private String mentalModelHtml;
    private String miniSummaryHtml;
    private String loreConclusionHtml;

    // Phase 3 — guided step engine
    /** True when this lesson has at least one guided step; the frontend switches to GuidedStepper. */
    private boolean hasGuidedSteps;

    // Phase 4 — solo assessment types
    /**
     * Assessment type for solo practice.
     * Values: {@code DETERMINISTIC | RUBRIC_REFLECTION | PATTERN_MATCH | AI_REVIEW}
     * Null → DETERMINISTIC (legacy behaviour).
     */
    private String soloAssessmentType;

    /** Rubric checklist items — present when soloAssessmentType == RUBRIC_REFLECTION. */
    private List<String> rubricItems;

    /**
     * Remaining AI-review quota for the current month.
     * Only meaningful when soloAssessmentType == AI_REVIEW; 0 otherwise.
     */
    private int aiReviewsRemaining;

    // ── Phase 4 (Retro) — Teach Back code execution ───────────────────────────

    /**
     * When non-null, the Teach Back step for this JAVA lesson will run the learner's code
     * and compare stdout to this value. The frontend shows a code editor instead of a textarea.
     * Never exposed as the expected output itself — kept opaque ("__code_teach_back__").
     */
    private String teachBackMode;
}
