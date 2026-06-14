package com.ambravate.arcane.academy.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CI lint for every {@code .md} lesson file under {@code src/main/resources/content/}.
 *
 * <p>Validates:
 * <ol>
 *   <li>YAML frontmatter is present and parseable.</li>
 *   <li>Required fields — {@code id}, {@code domainId}, {@code tier}, {@code moduleId},
 *       {@code title} — are non-blank.</li>
 *   <li>Tier is one of {@code APPRENTICE | JUNIOR | SENIOR | LEAD}.</li>
 *   <li>All required H1 body sections are present:
 *       Hook, Lore Introduction, Core Learning, Integration, Lore Conclusion.</li>
 *   <li>All required H2 sub-sections inside Core Learning are present:
 *       Concept Introduction, Why It Matters, Mental Model, Mini Summary.</li>
 *   <li>Body word count (excluding fenced code blocks) falls within the tier band:
 *       <ul>
 *         <li>APPRENTICE — 400–1 800 words</li>
 *         <li>JUNIOR     — 600–2 500 words</li>
 *         <li>SENIOR     — 800–3 500 words</li>
 *         <li>LEAD       — 1 000 + words (no upper limit)</li>
 *       </ul>
 *   </li>
 *   <li>If {@code soloAssessment.type} is set, it must be a valid enum name.</li>
 *   <li>If {@code soloAssessment.type} is {@code RUBRIC_REFLECTION}, at least one
 *       rubric item and a {@code modelAnswer} must be present.</li>
 *   <li>Every entry in {@code guidedSteps} must have an {@code id} and a
 *       {@code markingRule}.</li>
 * </ol>
 *
 * <p>Files whose name starts with {@code _} are index/meta files and are skipped.
 * No Spring context is needed — this test runs with plain classpath scanning.
 */
@DisplayName("Markdown lesson content validation — CI lint")
class MarkdownLessonValidationTest {

    // ── Constants ─────────────────────────────────────────────────────────────

    private static final Set<String> VALID_TIERS =
            Set.of("APPRENTICE", "JUNIOR", "SENIOR", "LEAD");

    private static final Set<String> VALID_SOLO_TYPES =
            Set.of("RUBRIC_REFLECTION", "PATTERN_MATCH", "AI_REVIEW", "DETERMINISTIC");

    /** Tier → [minWords, maxWords]; MAX_VALUE = no upper limit */
    private static final Map<String, int[]> WORD_BANDS = Map.of(
            "APPRENTICE", new int[]{400,  1800},
            "JUNIOR",     new int[]{600,  2500},
            "SENIOR",     new int[]{800,  3500},
            "LEAD",       new int[]{1000, Integer.MAX_VALUE}
    );

    private static final List<String> REQUIRED_H1 = List.of(
            "hook", "lore introduction", "core learning", "integration", "lore conclusion");

    private static final List<String> REQUIRED_H2_CORE = List.of(
            "concept introduction", "why it matters", "mental model", "mini summary");

    // Regex: a fenced code block (``` ... ```)
    private static final Pattern CODE_FENCE =
            Pattern.compile("```.*?```", Pattern.DOTALL);

    // ── Test source ───────────────────────────────────────────────────────────

    static Stream<Arguments> markdownFiles() throws IOException {
        var resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:content/**/*.md");
        return Arrays.stream(resources)
                .filter(r -> r.getFilename() != null && !r.getFilename().startsWith("_"))
                .sorted(Comparator.comparing(Resource::getFilename))
                .map(r -> Arguments.of(r.getFilename(), r));
    }

    // ── Main test ─────────────────────────────────────────────────────────────

    @ParameterizedTest(name = "{0}")
    @MethodSource("markdownFiles")
    @DisplayName("validates")
    void validateMarkdownLesson(String filename, Resource resource) throws Exception {
        String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        List<String> violations = new ArrayList<>();

        validate(content, violations);

        assertThat(violations)
                .as("Violations in %s", filename)
                .isEmpty();
    }

    // ── Validation pipeline ───────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private void validate(String content, List<String> v) {

        // 1. Frontmatter block present
        if (!content.startsWith("---")) {
            v.add("No YAML frontmatter — file must start with ---");
            return;
        }
        int fmEnd = content.indexOf("\n---", 3);
        if (fmEnd < 0) {
            v.add("Malformed frontmatter — no closing --- found");
            return;
        }
        String yamlBlock = content.substring(4, fmEnd);
        String body = content.substring(fmEnd + 4).strip();

        // 2. Parse YAML
        Map<String, Object> fm;
        try {
            fm = parseYaml(yamlBlock);
        } catch (Exception e) {
            v.add("YAML parse error in frontmatter: " + e.getMessage());
            return;
        }

        // 3. Required fields
        for (String field : List.of("id", "domainId", "tier", "moduleId", "title")) {
            if (blank(str(fm, field))) v.add("Required frontmatter field missing or blank: " + field);
        }

        // 4. Valid tier
        String tier = str(fm, "tier");
        if (!blank(tier) && !VALID_TIERS.contains(tier.toUpperCase())) {
            v.add("Invalid tier '" + tier + "' — must be one of " + VALID_TIERS);
        }

        // 5. Solo assessment config
        Object saRaw = fm.get("soloAssessment");
        if (saRaw instanceof Map<?, ?> saMap) {
            validateSoloAssessment((Map<String, Object>) saMap, v);
        }

        // 6. Guided steps
        Object stepsRaw = fm.get("guidedSteps");
        if (stepsRaw instanceof List<?> stepList) {
            for (int i = 0; i < stepList.size(); i++) {
                if (stepList.get(i) instanceof Map<?, ?> stepMap) {
                    validateGuidedStep(i, (Map<String, Object>) stepMap, v);
                }
            }
        }

        // 7. Required H1 sections
        Set<String> h1Titles = extractHeadings(body, "^# (.+)$");
        for (String req : REQUIRED_H1) {
            if (!h1Titles.contains(req)) {
                v.add("Missing required section: '# " + toTitleCase(req) + "'");
            }
        }

        // 8. Required H2 sub-sections inside Core Learning
        String coreBody = extractSectionBody(body, "core learning");
        if (coreBody != null) {
            Set<String> h2Titles = extractHeadings(coreBody, "^## (.+)$");
            for (String req : REQUIRED_H2_CORE) {
                if (!h2Titles.contains(req)) {
                    v.add("Missing required Core Learning sub-section: '## " + toTitleCase(req) + "'");
                }
            }
        }

        // 9. Word count band
        if (!blank(tier)) {
            int wordCount = countWords(body);
            int[] band = WORD_BANDS.get(tier.toUpperCase());
            if (band != null) {
                if (wordCount < band[0]) {
                    v.add("Body word count " + wordCount + " is below the " + tier +
                          " minimum of " + band[0] + " words");
                }
                if (band[1] != Integer.MAX_VALUE && wordCount > band[1]) {
                    v.add("Body word count " + wordCount + " exceeds the " + tier +
                          " maximum of " + band[1] + " words");
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void validateSoloAssessment(Map<String, Object> sa, List<String> v) {
        String type = str(sa, "type");
        if (!blank(type) && !VALID_SOLO_TYPES.contains(type.toUpperCase())) {
            v.add("Invalid soloAssessment.type '" + type + "' — must be one of " + VALID_SOLO_TYPES);
        }
        if ("RUBRIC_REFLECTION".equalsIgnoreCase(type)) {
            Object items = sa.get("rubricItems");
            if (!(items instanceof List<?> list) || list.isEmpty()) {
                v.add("soloAssessment RUBRIC_REFLECTION requires at least one rubricItem");
            }
            if (blank(str(sa, "modelAnswer"))) {
                v.add("soloAssessment RUBRIC_REFLECTION requires a modelAnswer");
            }
        }
        if ("PATTERN_MATCH".equalsIgnoreCase(type)) {
            Object kw = sa.get("keywords");
            if (!(kw instanceof List<?> list) || list.isEmpty()) {
                v.add("soloAssessment PATTERN_MATCH requires at least one keyword");
            }
        }
    }

    private void validateGuidedStep(int index, Map<String, Object> step, List<String> v) {
        String id = str(step, "id");
        String label = !blank(id) ? id : "index " + index;
        if (blank(id)) v.add("guidedSteps[" + index + "] missing required field: id");
        if (step.get("markingRule") == null) {
            v.add("guidedSteps[" + label + "] missing markingRule");
        }
    }

    // ── Body analysis ─────────────────────────────────────────────────────────

    /**
     * Extracts lower-cased heading titles matching {@code headingPattern}
     * (must have one capture group for the heading text).
     */
    private Set<String> extractHeadings(String body, String headingPattern) {
        if (body == null || body.isBlank()) return Set.of();
        Set<String> titles = new LinkedHashSet<>();
        Matcher m = Pattern.compile(headingPattern, Pattern.MULTILINE).matcher(body);
        while (m.find()) titles.add(m.group(1).strip().toLowerCase());
        return titles;
    }

    /**
     * Returns the body content of the H1 section named {@code sectionTitle}
     * (case-insensitive), or {@code null} if not found.
     */
    private String extractSectionBody(String body, String sectionTitle) {
        Pattern h1 = Pattern.compile("^# (.+)$", Pattern.MULTILINE);
        Matcher m = h1.matcher(body);
        int start = -1;
        while (m.find()) {
            if (m.group(1).strip().equalsIgnoreCase(sectionTitle)) {
                start = m.end();
            } else if (start >= 0) {
                return body.substring(start, m.start()).strip();
            }
        }
        return start >= 0 ? body.substring(start).strip() : null;
    }

    /**
     * Counts words in {@code body} after stripping fenced code blocks.
     * Inline code, punctuation, and Markdown markers count as words.
     */
    private int countWords(String body) {
        // Remove fenced code blocks first
        String stripped = CODE_FENCE.matcher(body).replaceAll(" ");
        // Collapse whitespace and count tokens
        String trimmed = stripped.strip();
        if (trimmed.isEmpty()) return 0;
        return trimmed.split("\\s+").length;
    }

    // ── YAML helpers ──────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseYaml(String block) {
        Object loaded = new Yaml().load(block);
        if (loaded instanceof Map<?, ?> m) return (Map<String, Object>) m;
        return Collections.emptyMap();
    }

    private String str(Map<String, Object> map, String key) {
        Object v = map.get(key);
        return v != null ? v.toString().strip() : null;
    }

    private boolean blank(String s) {
        return s == null || s.isBlank();
    }

    private String toTitleCase(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
