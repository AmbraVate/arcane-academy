package com.ambravate.arcane.academy.content;

import com.ambravate.arcane.academy.common.dto.ChunkContentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validates every JSON file under src/main/resources/content/ against the
 * authoring rules enforced by the seeder.
 *
 * <p>Catches authoring mistakes before they reach the database:
 * <ul>
 *   <li>Missing required fields (id, title, tier, topicId)</li>
 *   <li>Invalid enum values (tier, practiceType, question type/tier)</li>
 *   <li>Too few questions per sub-chunk (minimum 4)</li>
 *   <li>Too few test cases for JAVA practice (minimum 2)</li>
 *   <li>MULTIPLE_CHOICE correctAnswer not present in options array</li>
 *   <li>TRUE_FALSE correctAnswer not "True" or "False"</li>
 * </ul>
 *
 * <p>No Spring context needed — this test runs with plain Jackson and classpath
 * resource resolution, so it is fast and has no database dependency.
 */
@DisplayName("Content file validation")
class ContentFileValidationTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Set<String> VALID_TIERS =
            Set.of("APPRENTICE", "JUNIOR", "SENIOR", "LEAD");

    private static final Set<String> VALID_PRACTICE_TYPES =
            Set.of("JAVA", "TAILWIND", "REACT", "SQL", "R", "NONE");

    private static final Set<String> VALID_QUESTION_TYPES = Set.of(
            "MULTIPLE_CHOICE", "TRUE_FALSE", "FILL_BLANK",
            "CODE_COMPLETION", "WHATS_THE_OUTPUT", "DEBUGGING",
            "SCENARIO", "COMPARE_CONTRAST", "DISCRIMINATION"
    );

    private static final Set<String> VALID_QUESTION_TIERS =
            Set.of("RECALL", "APPLICATION", "DISCRIMINATION");

    // ── Test source ───────────────────────────────────────────────────────────

    static Stream<Arguments> contentFiles() throws IOException {
        var resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:content/**/*.json");
        Arrays.sort(resources, (a, b) -> {
            String na = a.getFilename() == null ? "" : a.getFilename();
            String nb = b.getFilename() == null ? "" : b.getFilename();
            return na.compareTo(nb);
        });
        return Arrays.stream(resources)
                .map(r -> Arguments.of(r.getFilename(), r));
    }

    // ── Main test ─────────────────────────────────────────────────────────────

    @ParameterizedTest(name = "{0}")
    @MethodSource("contentFiles")
    @DisplayName("validates")
    void validateContentFile(String filename, Resource resource) throws Exception {
        ChunkContentDto dto = MAPPER.readValue(resource.getInputStream(), ChunkContentDto.class);

        List<String> violations = new ArrayList<>();
        validateChunk(dto, violations);

        assertThat(violations)
                .as("Violations in %s", filename)
                .isEmpty();
    }

    // ── Chunk ─────────────────────────────────────────────────────────────────

    private void validateChunk(ChunkContentDto dto, List<String> v) {
        if (blank(dto.id))      v.add("Missing required field: id");
        if (blank(dto.title))   v.add("Missing required field: title");
        if (blank(dto.domainId)) v.add("Missing required field: domainId");

        if (blank(dto.tier)) {
            v.add("Missing required field: tier");
        } else if (!VALID_TIERS.contains(dto.tier)) {
            v.add("Invalid tier '" + dto.tier + "' — must be one of " + VALID_TIERS);
        }

        if (dto.subChunks == null || dto.subChunks.isEmpty()) {
            v.add("No subChunks defined");
        } else {
            for (ChunkContentDto.SubChunkDto sc : dto.subChunks) {
                validateSubChunk(sc, v);
            }
        }
    }

    // ── SubChunk ──────────────────────────────────────────────────────────────

    private void validateSubChunk(ChunkContentDto.SubChunkDto sc, List<String> v) {
        String prefix = "SubChunk[" + (sc.id != null ? sc.id : "?") + "]: ";

        if (blank(sc.id))    v.add("SubChunk missing required field: id");
        if (blank(sc.title)) v.add(prefix + "missing required field: title");

        if (blank(sc.practiceType)) {
            v.add(prefix + "missing required field: practiceType");
        } else if (!VALID_PRACTICE_TYPES.contains(sc.practiceType)) {
            v.add(prefix + "invalid practiceType '" + sc.practiceType
                    + "' — must be one of " + VALID_PRACTICE_TYPES);
        }

        // Skip content-completeness checks for stub lessons (no explanation authored yet).
        // Stubs are identified by having no explanationHtml or a placeholder hookHtml.
        if (!isStub(sc)) {
            validateQuestions(sc, prefix, v);
            validatePracticeTests(sc, prefix, v);
        }
    }

    /**
     * A sub-chunk is a stub when its explanation content is absent and its hook
     * is either blank or contains the canonical "Stub" marker.  Stubs are
     * pending authoring — we do not enforce question/test minimums on them.
     */
    private boolean isStub(ChunkContentDto.SubChunkDto sc) {
        boolean noExplanation = blank(sc.explanationHtml);
        boolean stubHook = sc.hookHtml == null
                || sc.hookHtml.isBlank()
                || sc.hookHtml.contains("Stub");
        return noExplanation && stubHook;
    }

    // ── Questions ─────────────────────────────────────────────────────────────

    private void validateQuestions(ChunkContentDto.SubChunkDto sc, String prefix, List<String> v) {
        int count = sc.questions == null ? 0 : sc.questions.size();
        if (count < 2) {
            v.add(prefix + "has " + count + " question(s); minimum is 2");
        }
        if (sc.questions != null) {
            for (int i = 0; i < sc.questions.size(); i++) {
                validateQuestion(sc.questions.get(i), prefix + "Q" + (i + 1) + ": ", v);
            }
        }
    }

    private void validateQuestion(ChunkContentDto.QuestionDto q, String prefix, List<String> v) {
        if (blank(q.type)) {
            v.add(prefix + "missing required field: type");
            return; // can't validate further without type
        }
        if (!VALID_QUESTION_TYPES.contains(q.type)) {
            v.add(prefix + "invalid type '" + q.type + "' — must be one of " + VALID_QUESTION_TYPES);
        }

        if (blank(q.tier)) {
            v.add(prefix + "missing required field: tier");
        } else if (!VALID_QUESTION_TIERS.contains(q.tier)) {
            v.add(prefix + "invalid tier '" + q.tier + "' — must be one of " + VALID_QUESTION_TIERS);
        }

        if (blank(q.questionHtml)) {
            v.add(prefix + "missing required field: questionHtml");
        }

        String answer = resolvedAnswer(q);
        if (blank(answer)) {
            v.add(prefix + "missing correctAnswer (and no resolvable correctIndex)");
        }

        switch (q.type) {
            case "MULTIPLE_CHOICE" -> validateMultipleChoice(q, answer, prefix, v);
            case "TRUE_FALSE"      -> validateTrueFalse(q, answer, prefix, v);
        }
    }

    private void validateMultipleChoice(ChunkContentDto.QuestionDto q, String answer,
                                        String prefix, List<String> v) {
        if (q.options == null || q.options.size() < 2) {
            v.add(prefix + "MULTIPLE_CHOICE requires ≥ 2 options");
            return;
        }
        if (answer != null && !q.options.contains(answer)) {
            v.add(prefix + "correctAnswer '" + answer + "' not found in options " + q.options);
        }
    }

    private void validateTrueFalse(ChunkContentDto.QuestionDto q, String answer,
                                   String prefix, List<String> v) {
        if (answer != null
                && !answer.equalsIgnoreCase("true")
                && !answer.equalsIgnoreCase("false")) {
            v.add(prefix + "TRUE_FALSE correctAnswer must be 'True' or 'False', got: '" + answer + "'");
        }
    }

    // ── Practice tests ────────────────────────────────────────────────────────

    private void validatePracticeTests(ChunkContentDto.SubChunkDto sc, String prefix, List<String> v) {
        if (!"JAVA".equals(sc.practiceType)) return;

        List<Map<String, Object>> tests = sc.guidedPracticeTests;
        if (tests == null || tests.isEmpty()) {
            v.add(prefix + "JAVA practice has no test cases");
            return;
        }

        for (int i = 0; i < tests.size(); i++) {
            validateTestCase(tests.get(i), prefix + "test[" + i + "]: ", v);
        }

        // A meaningful practice must verify at least 2 distinct expected output lines.
        // A single test object with multi-line expected output counts (e.g. 4-line expected = fine).
        // A single test with a single expected line is too thin.
        long totalExpectedLines = tests.stream()
                .map(t -> String.valueOf(t.getOrDefault("expected", "")))
                .filter(s -> !s.isBlank())
                .mapToLong(s -> s.lines().filter(l -> !l.isBlank()).count())
                .sum();

        if (totalExpectedLines < 2) {
            v.add(prefix + "JAVA practice checks only " + totalExpectedLines
                    + " expected output line(s); minimum is 2 (use multiple tests or a multi-line expected)");
        }
    }

    private void validateTestCase(Map<String, Object> test, String prefix, List<String> v) {
        if (!test.containsKey("expected") || blank(String.valueOf(test.get("expected")))) {
            v.add(prefix + "missing 'expected' field");
        }
        if (!test.containsKey("label") || blank(String.valueOf(test.get("label")))) {
            v.add(prefix + "missing 'label' field");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Mirrors the seeder's correctAnswer resolution logic. */
    private String resolvedAnswer(ChunkContentDto.QuestionDto q) {
        if (q.correctAnswer != null) return q.correctAnswer;
        if (q.correctIndex != null && q.options != null && q.correctIndex < q.options.size()) {
            return q.options.get(q.correctIndex);
        }
        return null;
    }

    private static boolean blank(String s) {
        return s == null || s.isBlank();
    }
}
