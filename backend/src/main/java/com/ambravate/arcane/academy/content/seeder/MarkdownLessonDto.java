package com.ambravate.arcane.academy.content.seeder;

import lombok.Builder;
import lombok.Getter;

/**
 * Parsed result of a single Markdown lesson file.
 *
 * <p>Contains both module/topic metadata (from YAML frontmatter) and all lesson
 * section HTML fields (from the Markdown body). The {@link MarkdownContentSeeder}
 * uses this to upsert the full entity graph (Module → Topic → Lesson).
 */
@Getter
@Builder
public class MarkdownLessonDto {

    // ── Module-level (from frontmatter) ───────────────────────────────────────

    /** Stable module identifier, e.g. {@code foundations_of_computation}. */
    private String moduleId;
    private String moduleTitle;
    /** Single glyph / emoji shown on the module card, e.g. {@code ⚡}. */
    private String moduleGlyph;
    private int    moduleSortOrder;
    /** Domain identifier, e.g. {@code software_engineering}. */
    private String domainId;
    /** Learner path tier string, e.g. {@code APPRENTICE}. Parsed to enum by seeder. */
    private String tier;

    // ── Topic-level (from frontmatter) ────────────────────────────────────────

    /**
     * Short slug for the topic within its module — the seeder prefixes this with
     * {@code moduleId + "-"} to form the full {@code topicId}.
     * Null means use the default topic ({@code moduleId + "-default-topic"}).
     */
    private String topicSlug;
    private String topicTitle;
    private int    topicSortOrder;

    // ── Lesson-level (from frontmatter) ───────────────────────────────────────

    private String id;
    private String title;
    private int    sortOrder;
    private int    xpReward;
    /** {@code NONE | JAVA | TAILWIND | REACT | SQL | R} */
    private String practiceType;
    /** {@code KNOWLEDGE | GUIDED | PRACTICE | INVESTIGATION | SYNTHESIS | MASTERY} */
    private String questType;
    private String feynmanPrompt;
    /** JSON array of strings, e.g. {@code ["Declare variables","Explain types"]}. */
    private String learningObjectivesJson;
    /** JSON array of domain names, e.g. {@code ["psychology","mathematics"]}. */
    private String integrationDomainsJson;

    // ── Lesson sections (from Markdown body) ──────────────────────────────────

    private String hookHtml;
    private String loreIntroHtml;
    /** Concept Introduction subsection of Core Learning. */
    private String explanationHtml;
    private String whyItMattersHtml;
    private String workedExamplesHtml;
    /** JSON array of mistake strings, extracted from list items in the section. */
    private String commonMistakesJson;
    private String mentalModelHtml;
    private String miniSummaryHtml;
    private String guidedPracticeHtml;
    private String soloPracticeHtml;
    private String integrationPrompt;
    private String loreConclusionHtml;

    // ── Phase 3 — guided steps (from frontmatter) ─────────────────────────────

    /** Ordered list of guided step configs; empty if no steps defined. */
    private java.util.List<GuidedStepConfig> guidedSteps;

    // ── Phase 4 — solo assessment (from frontmatter) ──────────────────────────

    /**
     * Solo assessment configuration parsed from the {@code soloAssessment} frontmatter block.
     * Null if the lesson uses the default DETERMINISTIC behaviour.
     */
    private SoloAssessmentConfig soloAssessment;

    // ── Retrieval questions (from frontmatter microCheckpoint) ─────────────────

    /**
     * Multiple-choice retrieval questions parsed from the {@code microCheckpoint} list.
     * Empty list if no questions are defined in frontmatter.
     */
    private java.util.List<MicroCheckpointConfig> microCheckpoints;

    /**
     * One guided step parsed from the YAML frontmatter {@code guidedSteps} list.
     *
     * @param id                   stable string ID (e.g. {@code var_step_1})
     * @param sortOrder            position in the workbook
     * @param instructionHtml      rendered HTML instruction
     * @param inputType            FILL_BLANK | MULTIPLE_CHOICE | SHORT_TEXT | CODE | DRAG_DROP | SEQUENCE
     * @param inputConfigJson      serialised JSON config for the input widget
     * @param markingRuleJson      serialised JSON marking rule
     * @param hintHtml             optional hint rendered to HTML
     * @param reflectionPromptHtml optional reflection shown on pass
     */
    public record GuidedStepConfig(
            String id,
            int    sortOrder,
            String instructionHtml,
            String inputType,
            String inputConfigJson,
            String markingRuleJson,
            String hintHtml,
            String reflectionPromptHtml) {}

    /**
     * Solo assessment configuration from the {@code soloAssessment} YAML block.
     *
     * @param type              RUBRIC_REFLECTION | PATTERN_MATCH | AI_REVIEW
     * @param rubricItemsJson   JSON array of checklist strings (RUBRIC_REFLECTION)
     * @param keywordsJson      JSON array of keywords (PATTERN_MATCH)
     * @param modelAnswerHtml   rendered HTML exemplar shown after submission
     */
    public record SoloAssessmentConfig(
            String type,
            String rubricItemsJson,
            String keywordsJson,
            String modelAnswerHtml) {}

    /**
     * A single retrieval question from the {@code microCheckpoint} YAML list.
     *
     * @param questionHtml   question text rendered to HTML
     * @param options        ordered answer choices
     * @param correctIndex   0-based index of the correct option
     * @param explanationHtml feedback rendered to HTML (shown after answering)
     * @param tier           RECALL | APPLICATION | DISCRIMINATION (defaults APPLICATION)
     */
    public record MicroCheckpointConfig(
            String questionHtml,
            java.util.List<String> options,
            int    correctIndex,
            String explanationHtml,
            String tier) {}
}
