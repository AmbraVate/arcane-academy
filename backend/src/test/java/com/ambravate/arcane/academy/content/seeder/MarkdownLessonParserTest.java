package com.ambravate.arcane.academy.content.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link MarkdownLessonParser}.
 *
 * Covers:
 * - Full frontmatter parsing (required + optional fields)
 * - All H1 top-level section mappings
 * - Core Learning H2 sub-section mappings
 * - Common Mistakes list extraction → JSON array
 * - Section aliases (e.g. "Guided Practice" and "Guided Practice Quest")
 * - Error cases (missing frontmatter, missing required fields)
 * - Null/blank safety for optional sections
 */
class MarkdownLessonParserTest {

    private MarkdownLessonParser parser;

    @BeforeEach
    void setUp() {
        parser = new MarkdownLessonParser(new MarkdownConverter(), new ObjectMapper());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static String minimalFrontmatter() {
        return """
                ---
                moduleId: test_module
                moduleTitle: "Test Module"
                domainId: software_engineering
                tier: APPRENTICE
                id: lesson_001
                title: "Test Lesson"
                ---
                """;
    }

    private static String fullFrontmatter() {
        return """
                ---
                moduleId: foundations
                moduleTitle: "Foundations of Computation"
                moduleGlyph: "⚡"
                moduleSortOrder: 1
                domainId: software_engineering
                tier: APPRENTICE
                topicSlug: variables_and_state
                topicTitle: "Variables and State"
                topicSortOrder: 2
                id: variables_intro
                title: "What is a Variable?"
                sortOrder: 3
                xpReward: 75
                practiceType: JAVA
                questType: KNOWLEDGE
                feynmanPrompt: "Explain variables as if to a 10-year-old."
                learningObjectives:
                  - Declare a variable
                  - Explain what a type is
                integrationDomains:
                  - psychology
                  - mathematics
                ---
                """;
    }

    // ── Frontmatter — required fields ─────────────────────────────────────────

    @Test
    void parsesRequiredFrontmatterFields() {
        String content = minimalFrontmatter();
        MarkdownLessonDto dto = parser.parse(content);

        assertThat(dto.getModuleId()).isEqualTo("test_module");
        assertThat(dto.getModuleTitle()).isEqualTo("Test Module");
        assertThat(dto.getDomainId()).isEqualTo("software_engineering");
        assertThat(dto.getTier()).isEqualTo("APPRENTICE");
        assertThat(dto.getId()).isEqualTo("lesson_001");
        assertThat(dto.getTitle()).isEqualTo("Test Lesson");
    }

    @Test
    void appliesDefaultsForOptionalFrontmatterFields() {
        MarkdownLessonDto dto = parser.parse(minimalFrontmatter());

        assertThat(dto.getModuleGlyph()).isEqualTo("📚");
        assertThat(dto.getModuleSortOrder()).isZero();
        assertThat(dto.getTopicSlug()).isNull();
        assertThat(dto.getTopicTitle()).isNull();
        assertThat(dto.getTopicSortOrder()).isZero();
        assertThat(dto.getSortOrder()).isZero();
        assertThat(dto.getXpReward()).isEqualTo(50);
        assertThat(dto.getPracticeType()).isEqualTo("NONE");
        assertThat(dto.getQuestType()).isNull();
        assertThat(dto.getFeynmanPrompt()).isNull();
        assertThat(dto.getLearningObjectivesJson()).isNull();
        assertThat(dto.getIntegrationDomainsJson()).isNull();
    }

    @Test
    void parsesAllOptionalFrontmatterFields() throws Exception {
        MarkdownLessonDto dto = parser.parse(fullFrontmatter());

        assertThat(dto.getModuleGlyph()).isEqualTo("⚡");
        assertThat(dto.getModuleSortOrder()).isEqualTo(1);
        assertThat(dto.getTopicSlug()).isEqualTo("variables_and_state");
        assertThat(dto.getTopicTitle()).isEqualTo("Variables and State");
        assertThat(dto.getTopicSortOrder()).isEqualTo(2);
        assertThat(dto.getSortOrder()).isEqualTo(3);
        assertThat(dto.getXpReward()).isEqualTo(75);
        assertThat(dto.getPracticeType()).isEqualTo("JAVA");
        assertThat(dto.getQuestType()).isEqualTo("KNOWLEDGE");
        assertThat(dto.getFeynmanPrompt()).isEqualTo("Explain variables as if to a 10-year-old.");

        // JSON arrays
        ObjectMapper om = new ObjectMapper();
        assertThat(om.<java.util.List<String>>readValue(dto.getLearningObjectivesJson(),
                new com.fasterxml.jackson.core.type.TypeReference<>() {}))
                .containsExactly("Declare a variable", "Explain what a type is");
        assertThat(om.<java.util.List<String>>readValue(dto.getIntegrationDomainsJson(),
                new com.fasterxml.jackson.core.type.TypeReference<>() {}))
                .containsExactly("psychology", "mathematics");
    }

    // ── Top-level sections ────────────────────────────────────────────────────

    @Test
    void parsesHookSection() {
        String content = minimalFrontmatter() + """

                # Hook
                Imagine memory as a magical cabinet.
                """;
        MarkdownLessonDto dto = parser.parse(content);
        assertThat(dto.getHookHtml()).contains("magical cabinet");
    }

    @Test
    void parsesLoreIntroSection() {
        String content = minimalFrontmatter() + """

                # Lore Introduction
                In the realm of Aetheria, scribes inscribe runes...
                """;
        MarkdownLessonDto dto = parser.parse(content);
        assertThat(dto.getLoreIntroHtml()).contains("Aetheria");
    }

    @Test
    void parsesGuidedPracticeQuestAlias() {
        String content = minimalFrontmatter() + """

                # Guided Practice Quest
                Write a program that declares two variables.
                """;
        MarkdownLessonDto dto = parser.parse(content);
        assertThat(dto.getGuidedPracticeHtml()).contains("declares two variables");
    }

    @Test
    void parsesGuidedPracticeShortAlias() {
        String content = minimalFrontmatter() + """

                # Guided Practice
                Write a program that declares two variables.
                """;
        MarkdownLessonDto dto = parser.parse(content);
        assertThat(dto.getGuidedPracticeHtml()).contains("declares two variables");
    }

    @Test
    void parsesSoloPracticeQuestAlias() {
        String content = minimalFrontmatter() + """

                # Solo Practice Quest
                Build it from scratch.
                """;
        MarkdownLessonDto dto = parser.parse(content);
        assertThat(dto.getSoloPracticeHtml()).contains("from scratch");
    }

    @Test
    void parsesIntegrationSection() {
        String content = minimalFrontmatter() + """

                # Integration
                Variables connect to psychology through working memory...
                """;
        MarkdownLessonDto dto = parser.parse(content);
        assertThat(dto.getIntegrationPrompt()).contains("working memory");
    }

    @Test
    void parsesLoreConclusionSection() {
        String content = minimalFrontmatter() + """

                # Lore Conclusion
                And so the apprentice sealed their first variable into the Grimoire.
                """;
        MarkdownLessonDto dto = parser.parse(content);
        assertThat(dto.getLoreConclusionHtml()).contains("Grimoire");
    }

    // ── Core Learning sub-sections ────────────────────────────────────────────

    @Test
    void parsesConceptIntroductionSubsection() {
        String content = minimalFrontmatter() + """

                # Core Learning

                ## Concept Introduction
                A **variable** stores a named value.
                """;
        MarkdownLessonDto dto = parser.parse(content);
        assertThat(dto.getExplanationHtml()).contains("variable");
        assertThat(dto.getExplanationHtml()).contains("<strong>");
    }

    @Test
    void parsesWhyItMattersSubsection() {
        String content = minimalFrontmatter() + """

                # Core Learning

                ## Why It Matters
                Variables are the foundation of every program.
                """;
        assertThat(parser.parse(content).getWhyItMattersHtml()).contains("foundation");
    }

    @Test
    void parsesWorkedExamplesSubsection() {
        String content = minimalFrontmatter() + """

                # Core Learning

                ## Worked Examples
                ```java
                int x = 5;
                ```
                """;
        // CommonMark renders fenced blocks as <pre><code class="language-java">…</code></pre>
        assertThat(parser.parse(content).getWorkedExamplesHtml()).contains("<pre>").contains("int x = 5");
    }

    @Test
    void parsesMentalModelSubsection() {
        String content = minimalFrontmatter() + """

                # Core Learning

                ## Mental Model
                Think of a variable as a labelled box.
                """;
        assertThat(parser.parse(content).getMentalModelHtml()).contains("labelled box");
    }

    @Test
    void parsesMiniSummarySubsection() {
        String content = minimalFrontmatter() + """

                # Core Learning

                ## Mini Summary
                Variables store named values with a declared type.
                """;
        assertThat(parser.parse(content).getMiniSummaryHtml()).contains("named values");
    }

    // ── Common Mistakes list extraction ───────────────────────────────────────

    @Test
    void extractsCommonMistakesAsJsonArray() throws Exception {
        String content = minimalFrontmatter() + """

                # Core Learning

                ## Common Mistakes
                - Forgetting to initialise the variable
                - Using the wrong data type
                - Shadowing an outer variable
                """;
        String json = parser.parse(content).getCommonMistakesJson();
        assertThat(json).isNotNull();
        ObjectMapper om = new ObjectMapper();
        java.util.List<String> items = om.readValue(json,
                new com.fasterxml.jackson.core.type.TypeReference<>() {});
        assertThat(items).containsExactly(
                "Forgetting to initialise the variable",
                "Using the wrong data type",
                "Shadowing an outer variable");
    }

    @Test
    void returnsNullCommonMistakesWhenSectionIsAbsent() {
        assertThat(parser.parse(minimalFrontmatter()).getCommonMistakesJson()).isNull();
    }

    @Test
    void returnsNullCommonMistakesWhenSectionHasNoListItems() {
        String content = minimalFrontmatter() + """

                # Core Learning

                ## Common Mistakes
                There are no common mistakes for this lesson.
                """;
        assertThat(parser.parse(content).getCommonMistakesJson()).isNull();
    }

    // ── Null safety for absent sections ──────────────────────────────────────

    @Test
    void allSectionFieldsNullWhenBodyIsEmpty() {
        MarkdownLessonDto dto = parser.parse(minimalFrontmatter());

        assertThat(dto.getHookHtml()).isNull();
        assertThat(dto.getLoreIntroHtml()).isNull();
        assertThat(dto.getExplanationHtml()).isNull();
        assertThat(dto.getWhyItMattersHtml()).isNull();
        assertThat(dto.getWorkedExamplesHtml()).isNull();
        assertThat(dto.getCommonMistakesJson()).isNull();
        assertThat(dto.getMentalModelHtml()).isNull();
        assertThat(dto.getMiniSummaryHtml()).isNull();
        assertThat(dto.getGuidedPracticeHtml()).isNull();
        assertThat(dto.getSoloPracticeHtml()).isNull();
        assertThat(dto.getIntegrationPrompt()).isNull();
        assertThat(dto.getLoreConclusionHtml()).isNull();
    }

    // ── Error cases ───────────────────────────────────────────────────────────

    @Test
    void throwsWhenFrontmatterBlockIsMissing() {
        assertThatThrownBy(() -> parser.parse("No frontmatter here."))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("frontmatter");
    }

    @Test
    void throwsWhenFrontmatterIsNull() {
        assertThatThrownBy(() -> parser.parse(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"moduleId", "moduleTitle", "domainId", "tier", "id", "title"})
    void throwsWhenRequiredFrontmatterFieldIsMissing(String missingField) {
        // Build a valid frontmatter string and remove one required field
        String content = """
                ---
                moduleId: test_module
                moduleTitle: "Test Module"
                domainId: software_engineering
                tier: APPRENTICE
                id: lesson_001
                title: "Test Lesson"
                ---
                """.replace(missingField + ":", "REMOVED_" + missingField + ":");

        assertThatThrownBy(() -> parser.parse(content))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(missingField);
    }

    // ── extractListAsJson (package-private) ───────────────────────────────────

    @Test
    void extractListAsJsonHandlesMixedBulletStyles() throws Exception {
        String section = "- item one\n* item two\n+ item three";
        String json = parser.extractListAsJson(section);
        assertThat(new ObjectMapper().<java.util.List<String>>readValue(
                json, new com.fasterxml.jackson.core.type.TypeReference<>() {}))
                .containsExactly("item one", "item two", "item three");
    }

    @Test
    void extractListAsJsonReturnsNullForNullInput() {
        assertThat(parser.extractListAsJson(null)).isNull();
    }

    @Test
    void extractListAsJsonReturnsNullForBlankInput() {
        assertThat(parser.extractListAsJson("   ")).isNull();
    }

    // ── splitSections (package-private) ──────────────────────────────────────

    @Test
    void splitSectionsHandlesWindowsLineEndings() {
        String body = "# Hook\r\nHook content.\r\n\r\n# Lore Introduction\r\nLore content.";
        var sections = parser.splitSections(body,
                java.util.regex.Pattern.compile("^# (.+)$", java.util.regex.Pattern.MULTILINE));
        assertThat(sections).containsKeys("hook", "lore introduction");
        assertThat(sections.get("hook")).isEqualTo("Hook content.");
    }

    @Test
    void splitSectionsReturnsEmptyMapForBlankBody() {
        assertThat(parser.splitSections("",
                java.util.regex.Pattern.compile("^# (.+)$", java.util.regex.Pattern.MULTILINE)))
                .isEmpty();
    }
}
