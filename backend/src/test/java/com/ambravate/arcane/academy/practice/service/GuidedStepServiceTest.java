package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.common.domain.GuidedStep;
import com.ambravate.arcane.academy.common.domain.GuidedStepInputType;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.content.repository.GuidedStepRepository;
import com.ambravate.arcane.academy.practice.dto.GuidedStepCheckResponse;
import com.ambravate.arcane.academy.practice.dto.GuidedStepDto;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.practice.runner.JavaCodeRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GuidedStepService} deterministic marking logic.
 *
 * Every match mode is exercised with both a passing and a failing answer.
 * The test doubles use Mockito — no Spring context needed (fast).
 */
class GuidedStepServiceTest {

    private GuidedStepService service;
    private GuidedStepRepository stepRepo;
    private UserChunkProgressRepository progressRepo;
    private JavaCodeRunner codeRunner;

    @BeforeEach
    void setUp() {
        stepRepo    = mock(GuidedStepRepository.class);
        progressRepo = mock(UserChunkProgressRepository.class);
        codeRunner  = mock(JavaCodeRunner.class);
        service = new GuidedStepService(stepRepo, progressRepo, codeRunner, new ObjectMapper());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private GuidedStep step(String markingRuleJson) {
        return GuidedStep.builder()
                .id("step_1")
                .lessonId("lesson_1")
                .sortOrder(1)
                .inputType(GuidedStepInputType.SHORT_TEXT)
                .markingRuleJson(markingRuleJson)
                .hintHtml("<p>Here is a hint.</p>")
                .reflectionPromptHtml("<p>Great work!</p>")
                .build();
    }

    private UserChunkProgress progress() {
        return UserChunkProgress.builder()
                .userId("user_1")
                .lessonId("lesson_1")
                .build();
    }

    private void stubStep(GuidedStep s) {
        when(stepRepo.findById("step_1")).thenReturn(Optional.of(s));
        when(stepRepo.findByLessonIdOrderBySortOrderAsc("lesson_1")).thenReturn(List.of(s));
    }

    private void stubProgress() {
        when(progressRepo.findByUserIdAndLessonId("user_1", "lesson_1"))
                .thenReturn(Optional.of(progress()));
    }

    // ── EXACT ─────────────────────────────────────────────────────────────────

    @Test
    void exact_passesOnExactMatch() {
        stubStep(step("""
                {"matchMode":"EXACT","accepted":["age"],"rejectedFeedback":"Wrong"}"""));
        stubProgress();
        assertThat(check("age").isPassed()).isTrue();
    }

    @Test
    void exact_failsOnDifferentCase() {
        stubStep(step("""
                {"matchMode":"EXACT","accepted":["age"],"rejectedFeedback":"Wrong"}"""));
        stubProgress();
        assertThat(check("Age").isPassed()).isFalse();
    }

    @Test
    void exact_failsOnWrongAnswer() {
        stubStep(step("""
                {"matchMode":"EXACT","accepted":["age"],"rejectedFeedback":"Try again"}"""));
        stubProgress();
        GuidedStepCheckResponse resp = check("score");
        assertThat(resp.isPassed()).isFalse();
        assertThat(resp.getFeedback()).isEqualTo("Try again");
    }

    // ── NORMALIZED ───────────────────────────────────────────────────────────

    @ParameterizedTest
    @CsvSource({ "age", "AGE", " Age ", "  age  " })
    void normalized_passesVariantCasesAndSpaces(String answer) {
        stubStep(step("""
                {"matchMode":"NORMALIZED","accepted":["age"]}"""));
        stubProgress();
        assertThat(check(answer).isPassed()).isTrue();
    }

    @Test
    void normalized_passesMultipleAccepted() {
        stubStep(step("""
                {"matchMode":"NORMALIZED","accepted":["age","personage","userage"]}"""));
        stubProgress();
        assertThat(check("personAge").isPassed()).isTrue();
    }

    @Test
    void normalized_failsUnknownAnswer() {
        stubStep(step("""
                {"matchMode":"NORMALIZED","accepted":["age"]}"""));
        stubProgress();
        assertThat(check("x").isPassed()).isFalse();
    }

    // ── REGEX ─────────────────────────────────────────────────────────────────

    @Test
    void regex_passesMatchingPattern() {
        stubStep(step("""
                {"matchMode":"REGEX","accepted":["[a-z]+Age"]}"""));
        stubProgress();
        assertThat(check("personAge").isPassed()).isTrue();
    }

    @Test
    void regex_failsNonMatchingPattern() {
        stubStep(step("""
                {"matchMode":"REGEX","accepted":["\\\\d+"]}"""));
        stubProgress();
        assertThat(check("abc").isPassed()).isFalse();
    }

    @Test
    void regex_survivesInvalidPattern() {
        stubStep(step("""
                {"matchMode":"REGEX","accepted":["[invalid(["]}"""));
        stubProgress();
        // should not throw; just return false
        assertThat(check("anything").isPassed()).isFalse();
    }

    // ── CONTAINS ─────────────────────────────────────────────────────────────

    @Test
    void contains_passesWhenAnswerContainsKeyword() {
        stubStep(step("""
                {"matchMode":"CONTAINS","accepted":["type","name","value"]}"""));
        stubProgress();
        // one keyword match is sufficient
        assertThat(check("The type tells the compiler what kind of data.").isPassed()).isTrue();
    }

    @Test
    void contains_isCaseInsensitive() {
        stubStep(step("""
                {"matchMode":"CONTAINS","accepted":["variable"]}"""));
        stubProgress();
        assertThat(check("A VARIABLE stores data.").isPassed()).isTrue();
    }

    @Test
    void contains_failsWhenNonePresent() {
        stubStep(step("""
                {"matchMode":"CONTAINS","accepted":["type","name","value"]}"""));
        stubProgress();
        assertThat(check("I am not sure.").isPassed()).isFalse();
    }

    // ── Allowed list ──────────────────────────────────────────────────────────

    @Test
    void allowed_alsoCausesPass() {
        stubStep(step("""
                {"matchMode":"NORMALIZED","accepted":["age"],"allowed":["myage","userage"]}"""));
        stubProgress();
        assertThat(check("myAge").isPassed()).isTrue();
    }

    // ── Reflection and next-step ──────────────────────────────────────────────

    @Test
    void onPass_includesReflectionAndNextStep() {
        GuidedStep s1 = step("""
                {"matchMode":"EXACT","accepted":["age"]}""");
        GuidedStep s2 = GuidedStep.builder()
                .id("step_2").lessonId("lesson_1").sortOrder(2)
                .inputType(GuidedStepInputType.SHORT_TEXT)
                .markingRuleJson("{\"matchMode\":\"EXACT\",\"accepted\":[\"ok\"]}")
                .build();
        when(stepRepo.findById("step_1")).thenReturn(Optional.of(s1));
        when(stepRepo.findByLessonIdOrderBySortOrderAsc("lesson_1"))
                .thenReturn(List.of(s1, s2));
        stubProgress();

        GuidedStepCheckResponse resp = check("age");
        assertThat(resp.isPassed()).isTrue();
        assertThat(resp.getReflectionPrompt()).isEqualTo("<p>Great work!</p>");
        assertThat(resp.getNextStepId()).isEqualTo("step_2");
    }

    private static final String EXACT_AGE = "{\"matchMode\":\"EXACT\",\"accepted\":[\"age\"]}";

    @Test
    void onPass_noNextStepWhenLastStep() {
        stubStep(step(EXACT_AGE));
        stubProgress();
        GuidedStepCheckResponse resp = check("age");
        assertThat(resp.getNextStepId()).isNull();
    }

    @Test
    void onFail_noReflectionOrNextStep() {
        stubStep(step(EXACT_AGE));
        stubProgress();
        GuidedStepCheckResponse resp = check("score");
        assertThat(resp.isPassed()).isFalse();
        assertThat(resp.getReflectionPrompt()).isNull();
        assertThat(resp.getNextStepId()).isNull();
    }

    // ── getSteps ──────────────────────────────────────────────────────────────

    @Test
    void getSteps_returnsCompletedFlagForFinishedSteps() {
        GuidedStep s = step(null);
        UserChunkProgress p = progress();
        p.setGuidedStepsCompletedJson("[\"step_1\"]");
        when(stepRepo.findByLessonIdOrderBySortOrderAsc("lesson_1")).thenReturn(List.of(s));
        when(progressRepo.findByUserIdAndLessonId("user_1", "lesson_1")).thenReturn(Optional.of(p));

        List<GuidedStepDto> dtos = service.getSteps("user_1", "lesson_1");
        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).isCompleted()).isTrue();
    }

    @Test
    void getSteps_returnsNotCompletedWhenNoProgress() {
        GuidedStep s = step(null);
        when(stepRepo.findByLessonIdOrderBySortOrderAsc("lesson_1")).thenReturn(List.of(s));
        when(progressRepo.findByUserIdAndLessonId("user_1", "lesson_1")).thenReturn(Optional.empty());

        List<GuidedStepDto> dtos = service.getSteps("user_1", "lesson_1");
        assertThat(dtos.get(0).isCompleted()).isFalse();
    }

    // ── allStepsCompleted ─────────────────────────────────────────────────────

    @Test
    void allStepsCompleted_trueWhenNoSteps() {
        when(stepRepo.findByLessonIdOrderBySortOrderAsc("lesson_1")).thenReturn(List.of());
        assertThat(service.allStepsCompleted("user_1", "lesson_1")).isTrue();
    }

    @Test
    void allStepsCompleted_trueWhenAllPassed() {
        GuidedStep s = step(null);
        UserChunkProgress p = progress();
        p.setGuidedStepsCompletedJson("[\"step_1\"]");
        when(stepRepo.findByLessonIdOrderBySortOrderAsc("lesson_1")).thenReturn(List.of(s));
        when(progressRepo.findByUserIdAndLessonId("user_1", "lesson_1")).thenReturn(Optional.of(p));
        assertThat(service.allStepsCompleted("user_1", "lesson_1")).isTrue();
    }

    @Test
    void allStepsCompleted_falseWhenSomeMissing() {
        GuidedStep s1 = step(null);
        GuidedStep s2 = GuidedStep.builder().id("step_2").lessonId("lesson_1")
                .sortOrder(2).inputType(GuidedStepInputType.SHORT_TEXT).build();
        UserChunkProgress p = progress();
        p.setGuidedStepsCompletedJson("[\"step_1\"]"); // step_2 not yet completed
        when(stepRepo.findByLessonIdOrderBySortOrderAsc("lesson_1")).thenReturn(List.of(s1, s2));
        when(progressRepo.findByUserIdAndLessonId("user_1", "lesson_1")).thenReturn(Optional.of(p));
        assertThat(service.allStepsCompleted("user_1", "lesson_1")).isFalse();
    }

    // ── Null / empty answer safety ────────────────────────────────────────────

    @Test
    void checkStep_handlesNullAnswer() {
        stubStep(step(EXACT_AGE));
        stubProgress();
        assertThat(check(null).isPassed()).isFalse();
    }

    @Test
    void checkStep_handlesEmptyAnswer() {
        stubStep(step("{\"matchMode\":\"NORMALIZED\",\"accepted\":[\"age\"]}"));
        stubProgress();
        assertThat(check("").isPassed()).isFalse();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private GuidedStepCheckResponse check(String answer) {
        return service.checkStep("user_1", "lesson_1", "step_1", answer);
    }
}
