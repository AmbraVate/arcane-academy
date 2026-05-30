package com.ambravate.arcane.academy.content.seeder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses a Markdown lesson file into a {@link MarkdownLessonDto}.
 *
 * <h2>File format</h2>
 * <pre>
 * ---
 * # required frontmatter fields
 * moduleId:   foundations_of_computation
 * moduleTitle: "Module 1: Foundations of Computation"
 * moduleGlyph: "⚡"
 * moduleSortOrder: 1
 * domainId:   software_engineering
 * tier:       APPRENTICE
 * topicSlug:  variables_and_state      # optional; omit to use default topic
 * topicTitle: "Variables and State"    # optional
 * topicSortOrder: 1
 * id:         variables_intro
 * title:      "What is a Variable?"
 * sortOrder:  1
 * xpReward:   50
 * practiceType: NONE
 * ---
 *
 * # Hook
 * Engaging opening hook prose...
 *
 * # Lore Introduction
 * Narrative lore that contextualises the lesson...
 *
 * # Core Learning
 *
 * ## Concept Introduction
 * Core concept explanation...
 *
 * ## Why It Matters
 * Practical relevance...
 *
 * ## Worked Examples
 * Annotated examples...
 *
 * ## Common Mistakes
 * - Forgetting to initialise before use
 * - Using the wrong type
 *
 * ## Mental Model
 * Analogy / mental model prose...
 *
 * ## Mini Summary
 * Recap of the key ideas...
 *
 * # Guided Practice Quest
 * Guided practice instructions...
 *
 * # Solo Practice Quest
 * Solo practice instructions...
 *
 * # Integration
 * Cross-domain integration prompt...
 *
 * # Lore Conclusion
 * Narrative payoff...
 * </pre>
 *
 * <p>Section headings are matched case-insensitively. All prose sections are
 * rendered to HTML via {@link MarkdownConverter}. The {@code Common Mistakes}
 * sub-section is stored as a JSON array extracted from Markdown list items.
 */
@Component
@RequiredArgsConstructor
public class MarkdownLessonParser {

    private final MarkdownConverter markdown;
    private final ObjectMapper objectMapper;

    // ── Patterns ──────────────────────────────────────────────────────────────

    private static final Pattern FRONTMATTER = Pattern.compile(
            "\\A---\\r?\\n(.*?)\\r?\\n---\\r?\\n?(.*)", Pattern.DOTALL);
    private static final Pattern H1 = Pattern.compile(
            "^# (.+)$", Pattern.MULTILINE);
    private static final Pattern H2 = Pattern.compile(
            "^## (.+)$", Pattern.MULTILINE);
    private static final Pattern LIST_ITEM = Pattern.compile(
            "^[-*+] (.+)$", Pattern.MULTILINE);

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Parses {@code fileContent} into a {@link MarkdownLessonDto}.
     *
     * @throws IllegalArgumentException if required frontmatter fields are missing
     *                                  or the frontmatter block is absent.
     */
    public MarkdownLessonDto parse(String fileContent) {
        Matcher m = FRONTMATTER.matcher(fileContent == null ? "" : fileContent);
        if (!m.matches()) {
            throw new IllegalArgumentException(
                    "Missing or malformed YAML frontmatter — file must start with a --- block");
        }
        Map<String, Object> fm = parseFrontmatter(m.group(1));
        String body = m.group(2);

        // Top-level sections (H1)
        Map<String, String> h1 = splitSections(body, H1);

        // Core Learning sub-sections (H2) — fall back to empty map if section absent
        Map<String, String> h2 = splitSections(h1.getOrDefault("core learning", ""), H2);

        return MarkdownLessonDto.builder()
                // Module
                .moduleId(requireString(fm, "moduleId"))
                .moduleTitle(requireString(fm, "moduleTitle"))
                .moduleGlyph(getString(fm, "moduleGlyph", "📚"))
                .moduleSortOrder(getInt(fm, "moduleSortOrder", 0))
                .domainId(requireString(fm, "domainId"))
                .tier(requireString(fm, "tier"))
                // Topic
                .topicSlug(getString(fm, "topicSlug", null))
                .topicTitle(getString(fm, "topicTitle", null))
                .topicSortOrder(getInt(fm, "topicSortOrder", 0))
                // Lesson identity
                .id(requireString(fm, "id"))
                .title(requireString(fm, "title"))
                .sortOrder(getInt(fm, "sortOrder", 0))
                .xpReward(getInt(fm, "xpReward", 50))
                .practiceType(getString(fm, "practiceType", "NONE"))
                .questType(getString(fm, "questType", null))
                .feynmanPrompt(getString(fm, "feynmanPrompt", null))
                .learningObjectivesJson(toJsonList(fm, "learningObjectives"))
                .integrationDomainsJson(toJsonList(fm, "integrationDomains"))
                // Sections
                .hookHtml(renderSection(h1, "hook"))
                .loreIntroHtml(renderSection(h1, "lore introduction"))
                .explanationHtml(renderSection(h2, "concept introduction"))
                .whyItMattersHtml(renderSection(h2, "why it matters"))
                .workedExamplesHtml(renderSection(h2, "worked examples"))
                .commonMistakesJson(extractListAsJson(h2.get("common mistakes")))
                .mentalModelHtml(renderSection(h2, "mental model"))
                .miniSummaryHtml(renderSection(h2, "mini summary"))
                .guidedPracticeHtml(renderSection(h1, "guided practice quest", "guided practice"))
                .soloPracticeHtml(renderSection(h1, "solo practice quest", "solo practice"))
                .integrationPrompt(renderSection(h1, "integration"))
                .loreConclusionHtml(renderSection(h1, "lore conclusion"))
                // Phase 3 — guided steps from frontmatter
                .guidedSteps(parseGuidedSteps(fm))
                // Phase 4 — solo assessment from frontmatter
                .soloAssessment(parseSoloAssessment(fm))
                .build();
    }

    // ── Section splitting ─────────────────────────────────────────────────────

    /**
     * Splits {@code body} into named sections using {@code headingPattern}.
     * The key is the heading text in lower-case; the value is the content below
     * it (stripped), not including the heading line itself.
     */
    Map<String, String> splitSections(String body, Pattern headingPattern) {
        if (body == null || body.isBlank()) return Collections.emptyMap();

        Map<String, String> sections = new LinkedHashMap<>();
        List<String> names = new ArrayList<>();
        List<Integer> starts = new ArrayList<>();

        Matcher m = headingPattern.matcher(body);
        while (m.find()) {
            names.add(m.group(1).strip().toLowerCase());
            starts.add(m.start());
        }

        for (int i = 0; i < names.size(); i++) {
            int from = starts.get(i);
            int to   = (i + 1 < starts.size()) ? starts.get(i + 1) : body.length();
            String block = body.substring(from, to);
            int nl = block.indexOf('\n');
            String content = nl >= 0 ? block.substring(nl + 1) : "";
            sections.put(names.get(i), content.strip());
        }

        return sections;
    }

    // ── Rendering ─────────────────────────────────────────────────────────────

    /**
     * Renders the first non-blank section found under any of {@code keys}.
     * Returns {@code null} if none of the keys are present or all are blank.
     */
    private String renderSection(Map<String, String> sections, String... keys) {
        for (String key : keys) {
            String content = sections.get(key);
            if (content != null && !content.isBlank()) {
                return markdown.toHtml(content);
            }
        }
        return null;
    }

    /**
     * Extracts Markdown list items ({@code - item}, {@code * item},
     * {@code + item}) from {@code sectionContent} and serialises them as a JSON
     * array.  Returns {@code null} if no items are found.
     */
    String extractListAsJson(String sectionContent) {
        if (sectionContent == null || sectionContent.isBlank()) return null;
        Matcher m = LIST_ITEM.matcher(sectionContent);
        List<String> items = new ArrayList<>();
        while (m.find()) items.add(m.group(1).strip());
        if (items.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    // ── YAML helpers ──────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseFrontmatter(String yamlBlock) {
        Yaml yaml = new Yaml();
        Object loaded = yaml.load(yamlBlock);
        if (loaded instanceof Map<?, ?> m) return (Map<String, Object>) m;
        return Collections.emptyMap();
    }

    private String requireString(Map<String, Object> fm, String key) {
        Object val = fm.get(key);
        if (val == null || val.toString().isBlank()) {
            throw new IllegalArgumentException(
                    "Required frontmatter field missing or blank: " + key);
        }
        return val.toString().strip();
    }

    private String getString(Map<String, Object> fm, String key, String defaultVal) {
        Object val = fm.get(key);
        return (val != null && !val.toString().isBlank()) ? val.toString().strip() : defaultVal;
    }

    private int getInt(Map<String, Object> fm, String key, int defaultVal) {
        Object val = fm.get(key);
        if (val instanceof Number n) return n.intValue();
        return defaultVal;
    }

    @SuppressWarnings("unchecked")
    private String toJsonList(Map<String, Object> fm, String key) {
        Object val = fm.get(key);
        if (val instanceof List<?> list && !list.isEmpty()) {
            try {
                return objectMapper.writeValueAsString(list);
            } catch (JsonProcessingException e) {
                return null;
            }
        }
        return null;
    }

    // ── Phase 3 — guided step parsing ────────────────────────────────────────

    /**
     * Parses the {@code guidedSteps} list from frontmatter.
     *
     * Each step map supports:
     * <pre>
     * - id: var_step_1
     *   sortOrder: 1
     *   inputType: FILL_BLANK       # default SHORT_TEXT
     *   instruction: "Markdown instruction..."
     *   inputConfig:                # optional map
     *     placeholder: "variable name"
     *   markingRule:
     *     matchMode: NORMALIZED
     *     accepted: [age]
     *     rejectedFeedback: "Try again..."
     *   hint: "Optional hint Markdown..."
     *   reflectionPrompt: "Reflection Markdown..."
     * </pre>
     */
    @SuppressWarnings("unchecked")
    private List<MarkdownLessonDto.GuidedStepConfig> parseGuidedSteps(
            Map<String, Object> fm) {
        Object raw = fm.get("guidedSteps");
        if (!(raw instanceof List<?> list) || list.isEmpty()) return List.of();

        List<MarkdownLessonDto.GuidedStepConfig> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object item = list.get(i);
            if (!(item instanceof Map<?, ?> rawMap)) continue;
            Map<String, Object> m = (Map<String, Object>) rawMap;

            String id = getString(m, "id", null);
            if (id == null || id.isBlank()) {
                log.warn("[MarkdownLessonParser] Guided step at index {} missing 'id' — skipping", i);
                continue;
            }
            int sortOrder  = getInt(m, "sortOrder", i + 1);
            String inputType = getString(m, "inputType", "SHORT_TEXT").toUpperCase(java.util.Locale.ROOT);
            String instructionHtml = renderMarkdown(getString(m, "instruction", null));
            String hintHtml        = renderMarkdown(getString(m, "hint", null));
            String reflectionHtml  = renderMarkdown(getString(m, "reflectionPrompt", null));
            String inputConfigJson = toJsonOrNull(m.get("inputConfig"));
            String markingRuleJson = toJsonOrNull(m.get("markingRule"));

            result.add(new MarkdownLessonDto.GuidedStepConfig(
                    id, sortOrder, instructionHtml, inputType,
                    inputConfigJson, markingRuleJson, hintHtml, reflectionHtml));
        }
        return result;
    }

    // ── Phase 4 — solo assessment parsing ────────────────────────────────────

    /**
     * Parses the {@code soloAssessment} block from frontmatter.
     *
     * <pre>
     * soloAssessment:
     *   type: RUBRIC_REFLECTION   # RUBRIC_REFLECTION | PATTERN_MATCH | AI_REVIEW
     *   rubricItems:
     *     - Declares a String variable
     *     - Prints output correctly
     *   keywords:
     *     - variable
     *     - type
     *   modelAnswer: |
     *     **Model answer prose or code…**
     * </pre>
     */
    @SuppressWarnings("unchecked")
    private MarkdownLessonDto.SoloAssessmentConfig parseSoloAssessment(Map<String, Object> fm) {
        Object raw = fm.get("soloAssessment");
        if (!(raw instanceof Map<?, ?> rawMap)) return null;
        Map<String, Object> sa = (Map<String, Object>) rawMap;

        String type          = getString(sa, "type", null);
        if (type == null) return null;

        String rubricItemsJson = null;
        Object rubricRaw = sa.get("rubricItems");
        if (rubricRaw instanceof List<?> list && !list.isEmpty()) {
            try { rubricItemsJson = objectMapper.writeValueAsString(list); }
            catch (Exception e) { log.warn("[MarkdownLessonParser] Failed to serialise rubricItems", e); }
        }

        String keywordsJson = null;
        Object kwRaw = sa.get("keywords");
        if (kwRaw instanceof List<?> list && !list.isEmpty()) {
            try { keywordsJson = objectMapper.writeValueAsString(list); }
            catch (Exception e) { log.warn("[MarkdownLessonParser] Failed to serialise keywords", e); }
        }

        String modelAnswerHtml = renderMarkdown(getString(sa, "modelAnswer", null));

        return new MarkdownLessonDto.SoloAssessmentConfig(
                type.toUpperCase(java.util.Locale.ROOT),
                rubricItemsJson, keywordsJson, modelAnswerHtml);
    }

    private String renderMarkdown(String md) {
        return (md == null || md.isBlank()) ? null : markdown.toHtml(md);
    }

    private String toJsonOrNull(Object val) {
        if (val == null) return null;
        try {
            return objectMapper.writeValueAsString(val);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    // package-private for use in parser unit tests
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MarkdownLessonParser.class);
}
