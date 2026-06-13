package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lessons")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Lesson {

    @Id
    private String id;

    @Column(name = "module_id", nullable = false)
    private String moduleId;

    @Column(nullable = false)
    private String title;

    private int sortOrder;

    @Column(columnDefinition = "TEXT")
    private String hookHtml;

    @Column(columnDefinition = "TEXT")
    private String explanationHtml;

    @Column(columnDefinition = "TEXT")
    private String storyJson;

    @Column(columnDefinition = "TEXT")
    private String guidedPracticeHtml;

    @Column(columnDefinition = "TEXT")
    private String guidedPracticeStarterCode;

    @Column(columnDefinition = "TEXT")
    private String guidedPracticeTestsJson;

    private String filename;

    @Column(columnDefinition = "TEXT")
    private String soloPracticeHtml;

    @Column(columnDefinition = "TEXT")
    private String feynmanPrompt;

    @Builder.Default
    private int xpReward = 50;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LessonPracticeType practiceType = LessonPracticeType.JAVA;

    @Column(columnDefinition = "TEXT")
    private String rabbitHoleTermsJson;

    @Column(columnDefinition = "TEXT")
    private String modelAnswer;

    @Column(columnDefinition = "TEXT")
    private String guidedPracticeModelAnswer;

    @Column(columnDefinition = "TEXT")
    private String learningObjectivesJson;

    @Column(columnDefinition = "TEXT")
    private String challengeHtml;

    @Column(columnDefinition = "TEXT")
    private String challengeStarterCode;

    @Column(columnDefinition = "TEXT")
    private String challengeTestsJson;

    @Column(columnDefinition = "TEXT")
    private String miniProjectHtml;

    @Column(columnDefinition = "TEXT")
    private String commonMistakesJson;

    @Column(columnDefinition = "TEXT")
    private String assessmentCriteriaJson;

    @Column(columnDefinition = "TEXT")
    private String downloadablesJson;

    @Column(columnDefinition = "TEXT")
    private String integrationPrompt;

    @Enumerated(EnumType.STRING)
    private QuestType questType;

    /**
     * The Topic this lesson belongs to within its Module.
     * Nullable for backward compatibility — all lessons receive a value via
     * the V29 migration backfill; new lessons are assigned at seed time.
     */
    @Column(name = "topic_id")
    private String topicId;

    // ── Phase 2 — Markdown section fields ────────────────────────────────────
    // Null for JSON-seeded lessons; populated for Markdown-authored lessons.

    /** Lore Introduction — narrative hook that follows the lesson title card. */
    @Column(name = "lore_intro_html", columnDefinition = "TEXT")
    private String loreIntroHtml;

    /** Why It Matters — practical relevance of the concept. */
    @Column(name = "why_it_matters_html", columnDefinition = "TEXT")
    private String whyItMattersHtml;

    /** Worked Examples — annotated code / worked problems. */
    @Column(name = "worked_examples_html", columnDefinition = "TEXT")
    private String workedExamplesHtml;

    /** Mental Model — a memorable analogy or visualisation. */
    @Column(name = "mental_model_html", columnDefinition = "TEXT")
    private String mentalModelHtml;

    /** Mini Summary — compact recap of Core Learning. */
    @Column(name = "mini_summary_html", columnDefinition = "TEXT")
    private String miniSummaryHtml;

    /** Lore Conclusion — narrative payoff shown on the COMPLETE screen. */
    @Column(name = "lore_conclusion_html", columnDefinition = "TEXT")
    private String loreConclusionHtml;

    /**
     * JSON array of domain names this lesson integrates with — used by the
     * Knowledge Graph (Phase 7) to draw cross-domain edges.
     * Example: {@code ["psychology","mathematics"]}
     */
    @Column(name = "integration_domains_json", columnDefinition = "TEXT")
    private String integrationDomainsJson;

    // ── Phase 4 — Solo assessment types ──────────────────────────────────────

    /**
     * Null → default DETERMINISTIC behaviour (existing code/written paths).
     * Set explicitly on Markdown-authored lessons to unlock richer assessment.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "solo_assessment_type")
    private SoloAssessmentType soloAssessmentType;

    /**
     * JSON array of rubric checklist strings — used by RUBRIC_REFLECTION.
     * Example: {@code ["Declares three variables","Prints each variable"]}
     */
    @Column(name = "rubric_items_json", columnDefinition = "TEXT")
    private String rubricItemsJson;

    /**
     * JSON array of keywords for PATTERN_MATCH scoring.
     * Example: {@code ["variable","type","int","String"]}
     */
    @Column(name = "keywords_json", columnDefinition = "TEXT")
    private String keywordsJson;

    /**
     * Model-answer exemplar shown to the learner after their first solo
     * practice submission (all non-DETERMINISTIC types).
     */
    @Column(name = "solo_model_answer_html", columnDefinition = "TEXT")
    private String soloModelAnswerHtml;

    // ── Phase 4 (Retro) — Teach Back code execution ───────────────────────────

    /**
     * Expected stdout for the Teach Back code execution path.
     * Only used when practiceType == JAVA.
     * NULL → fall back to pattern-match Feynman scoring.
     */
    @Column(name = "teach_back_expected_output", columnDefinition = "TEXT")
    private String teachBackExpectedOutput;
}
