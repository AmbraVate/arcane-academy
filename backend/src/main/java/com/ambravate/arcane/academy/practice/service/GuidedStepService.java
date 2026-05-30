package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.common.domain.GuidedStep;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.content.repository.GuidedStepRepository;
import com.ambravate.arcane.academy.practice.dto.CodeRunResponse;
import com.ambravate.arcane.academy.practice.dto.GuidedStepCheckResponse;
import com.ambravate.arcane.academy.practice.dto.GuidedStepDto;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.practice.runner.JavaCodeRunner;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Handles the Guided Practice step workbook.
 *
 * <p>Marking is entirely <em>deterministic</em> — no AI in the happy path.
 * The five match modes map to the following logic:
 *
 * <table border="1" summary="">
 *   <tr><th>Mode</th><th>Logic</th></tr>
 *   <tr><td>EXACT</td><td>answer.trim() must equal an accepted string (case-sensitive)</td></tr>
 *   <tr><td>NORMALIZED</td><td>lowercase + collapse-whitespace equality</td></tr>
 *   <tr><td>REGEX</td><td>answer.trim() matches any accepted regex pattern</td></tr>
 *   <tr><td>CONTAINS</td><td>lower-cased answer contains every accepted keyword</td></tr>
 *   <tr><td>OUTPUT_MATCH</td><td>run code, compare trimmed stdout to accepted strings</td></tr>
 * </table>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GuidedStepService {

    private final GuidedStepRepository         guidedStepRepository;
    private final UserChunkProgressRepository  progressRepository;
    private final JavaCodeRunner               codeRunner;
    private final ObjectMapper                 objectMapper;

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Returns the ordered steps for a lesson with per-user completion flags.
     */
    public List<GuidedStepDto> getSteps(String userId, String lessonId) {
        List<GuidedStep> steps = guidedStepRepository.findByLessonIdOrderBySortOrderAsc(lessonId);
        Set<String> completed = loadCompletedStepIds(userId, lessonId);

        return steps.stream()
                .map(s -> GuidedStepDto.builder()
                        .id(s.getId())
                        .sortOrder(s.getSortOrder())
                        .instructionHtml(s.getInstructionHtml())
                        .inputType(s.getInputType().name())
                        .inputConfig(parseJson(s.getInputConfigJson()))
                        .hintHtml(s.getHintHtml())
                        .completed(completed.contains(s.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Marks the learner's answer and, on pass, records step completion.
     */
    @Transactional
    public GuidedStepCheckResponse checkStep(String userId, String lessonId,
                                             String stepId, String answer) {
        GuidedStep step = guidedStepRepository.findById(stepId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Step not found: " + stepId));

        if (!step.getLessonId().equals(lessonId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Step does not belong to lesson " + lessonId);
        }

        MarkingRule rule = parseMarkingRule(step.getMarkingRuleJson());
        boolean passed = mark(step, answer, rule);

        String feedback;
        String nextStepId = null;
        String reflectionPrompt = null;

        if (passed) {
            feedback = "Correct!";
            reflectionPrompt = step.getReflectionPromptHtml();
            nextStepId = findNextStepId(lessonId, step.getSortOrder());
            recordStepCompleted(userId, lessonId, stepId);
            log.debug("[GuidedStep] Step passed | user={} lesson={} step={}", userId, lessonId, stepId);
        } else {
            feedback = (rule.rejectedFeedback() != null && !rule.rejectedFeedback().isBlank())
                    ? rule.rejectedFeedback()
                    : "Not quite — try again.";
        }

        return GuidedStepCheckResponse.builder()
                .passed(passed)
                .feedback(feedback)
                .reflectionPrompt(reflectionPrompt)
                .nextStepId(nextStepId)
                .build();
    }

    /**
     * Returns {@code true} when the lesson has guided steps AND the user has
     * completed all of them.  Used by {@link EncodingService#advancePhase} to
     * guard GUIDED_PRACTICE → SOLO_PRACTICE (or RETRIEVAL_CHECK).
     */
    public boolean allStepsCompleted(String userId, String lessonId) {
        List<GuidedStep> allSteps = guidedStepRepository.findByLessonIdOrderBySortOrderAsc(lessonId);
        if (allSteps.isEmpty()) return true; // no steps — nothing to block advance
        Set<String> completed = loadCompletedStepIds(userId, lessonId);
        return allSteps.stream().allMatch(s -> completed.contains(s.getId()));
    }

    /** Returns {@code true} if the lesson has at least one guided step. */
    public boolean hasSteps(String lessonId) {
        return guidedStepRepository.countByLessonId(lessonId) > 0;
    }

    // ── Marking ───────────────────────────────────────────────────────────────

    private boolean mark(GuidedStep step, String answer, MarkingRule rule) {
        List<String> allAccepted = concat(rule.accepted(), rule.allowed());
        if (allAccepted.isEmpty()) return false; // no accepted answers configured

        return switch (rule.matchMode()) {
            case "EXACT" -> {
                String trimmed = answer == null ? "" : answer.trim();
                yield allAccepted.stream().anyMatch(a -> a.equals(trimmed));
            }
            case "NORMALIZED" -> {
                String norm = normalize(answer);
                yield allAccepted.stream().anyMatch(a -> normalize(a).equals(norm));
            }
            case "REGEX" -> {
                String trimmed = answer == null ? "" : answer.trim();
                yield allAccepted.stream().anyMatch(a -> {
                    try { return trimmed.matches(a); }
                    catch (Exception e) { return false; }
                });
            }
            case "CONTAINS" -> {
                String lower = (answer == null ? "" : answer).toLowerCase(Locale.ROOT);
                yield allAccepted.stream().anyMatch(a ->
                        lower.contains(a.toLowerCase(Locale.ROOT)));
            }
            case "OUTPUT_MATCH" -> {
                if (answer == null || answer.isBlank()) yield false;
                CodeRunResponse result = codeRunner.run(answer, null);
                if (result.getOutput() == null) yield false;
                String trimmedOutput = result.getOutput().trim();
                yield allAccepted.stream().anyMatch(a -> a.trim().equals(trimmedOutput));
            }
            default -> {
                log.warn("[GuidedStep] Unknown matchMode '{}' — treating as no-match", rule.matchMode());
                yield false;
            }
        };
    }

    // ── Completion tracking ───────────────────────────────────────────────────

    private Set<String> loadCompletedStepIds(String userId, String lessonId) {
        return progressRepository.findByUserIdAndLessonId(userId, lessonId)
                .map(p -> parseStringSet(p.getGuidedStepsCompletedJson()))
                .orElse(Collections.emptySet());
    }

    private void recordStepCompleted(String userId, String lessonId, String stepId) {
        progressRepository.findByUserIdAndLessonId(userId, lessonId).ifPresent(progress -> {
            Set<String> completed = new LinkedHashSet<>(
                    parseStringSet(progress.getGuidedStepsCompletedJson()));
            if (completed.add(stepId)) {
                progress.setGuidedStepsCompletedJson(writeJson(new ArrayList<>(completed)));
                progressRepository.save(progress);
            }
        });
    }

    private String findNextStepId(String lessonId, int currentSortOrder) {
        return guidedStepRepository.findByLessonIdOrderBySortOrderAsc(lessonId)
                .stream()
                .filter(s -> s.getSortOrder() > currentSortOrder)
                .findFirst()
                .map(GuidedStep::getId)
                .orElse(null);
    }

    // ── Parsing helpers ───────────────────────────────────────────────────────

    private MarkingRule parseMarkingRule(String json) {
        if (json == null || json.isBlank()) {
            return new MarkingRule("NORMALIZED", List.of(), List.of(), null);
        }
        try {
            Map<String, Object> m = objectMapper.readValue(json, new TypeReference<>() {});
            String matchMode = String.valueOf(m.getOrDefault("matchMode", "NORMALIZED")).toUpperCase(Locale.ROOT);
            List<String> accepted = toStringList(m.get("accepted"));
            List<String> allowed  = toStringList(m.get("allowed"));
            String rejectedFeedback = m.containsKey("rejectedFeedback")
                    ? String.valueOf(m.get("rejectedFeedback")) : null;
            return new MarkingRule(matchMode, accepted, allowed, rejectedFeedback);
        } catch (Exception e) {
            log.warn("[GuidedStep] Failed to parse marking rule: {}", e.getMessage());
            return new MarkingRule("NORMALIZED", List.of(), List.of(), null);
        }
    }

    private Set<String> parseStringSet(String json) {
        if (json == null || json.isBlank()) return new LinkedHashSet<>();
        try {
            List<String> list = objectMapper.readValue(json, new TypeReference<>() {});
            return new LinkedHashSet<>(list);
        } catch (Exception e) {
            return new LinkedHashSet<>();
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> toStringList(Object val) {
        if (val instanceof List<?> list) {
            return list.stream().map(Object::toString).collect(Collectors.toList());
        }
        return List.of();
    }

    private Object parseJson(String json) {
        if (json == null || json.isBlank()) return Map.of();
        try { return objectMapper.readValue(json, Object.class); }
        catch (Exception e) { return Map.of(); }
    }

    private String writeJson(Object obj) {
        try { return objectMapper.writeValueAsString(obj); }
        catch (Exception e) { return "[]"; }
    }

    private static String normalize(String s) {
        if (s == null) return "";
        return s.toLowerCase(Locale.ROOT).replaceAll("\\s+", " ").trim();
    }

    private static List<String> concat(List<String> a, List<String> b) {
        List<String> out = new ArrayList<>(a == null ? List.of() : a);
        if (b != null) out.addAll(b);
        return out;
    }

    // ── Value types ───────────────────────────────────────────────────────────

    private record MarkingRule(
            String       matchMode,
            List<String> accepted,
            List<String> allowed,
            String       rejectedFeedback) {}
}
