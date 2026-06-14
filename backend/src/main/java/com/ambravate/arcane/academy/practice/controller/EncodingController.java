package com.ambravate.arcane.academy.practice.controller;

import com.ambravate.arcane.academy.ai.domain.AnswerPair;
import com.ambravate.arcane.academy.ai.domain.FeynmanResult;
import com.ambravate.arcane.academy.common.dto.AnswerRequest;
import com.ambravate.arcane.academy.practice.dto.CodeSubmitRequest;
import com.ambravate.arcane.academy.practice.dto.SoloSubmitRequest;
import com.ambravate.arcane.academy.practice.dto.SoloAssessmentResponse;
import com.ambravate.arcane.academy.ai.dto.FeynmanRequest;
import com.ambravate.arcane.academy.ai.dto.FeynmanResultDto;
import com.ambravate.arcane.academy.common.dto.QuestionDto;
import com.ambravate.arcane.academy.ai.dto.RetrievalResultDto;
import com.ambravate.arcane.academy.practice.dto.LessonEncodingDto;
import com.ambravate.arcane.academy.practice.dto.SubmitResponse;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.SoloAssessmentType;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.practice.domain.PracticeResult;
import com.ambravate.arcane.academy.practice.domain.RetrievalCheckResult;
import com.ambravate.arcane.academy.practice.domain.LessonSession;
import com.ambravate.arcane.academy.practice.domain.SoloAssessmentResult;
import com.ambravate.arcane.academy.practice.service.EncodingService;
import com.ambravate.arcane.academy.ai.service.FeynmanService;
import com.ambravate.arcane.academy.ai.service.RetrievalService;

import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.practice.service.GuidedStepService;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;

@RestController
@RequestMapping("/api/encoding")
@RequiredArgsConstructor
public class EncodingController {

    private final EncodingService   encodingService;
    private final RetrievalService  retrievalService;
    private final FeynmanService    feynmanService;
    private final GuidedStepService guidedStepService;
    private final LearningModuleRepository moduleRepository;
    private final ObjectMapper objectMapper;

    @PostMapping("/{lessonId}/start")
    public ResponseEntity<LessonEncodingDto> startLesson(
            @PathVariable String lessonId,
            @AuthenticationPrincipal UserPrincipal user) {
        LessonSession session = encodingService.startLesson(user.getId(), lessonId);
        LessonEncodingDto dto = toDto(session, user.getId());

        if ("RETRIEVAL_CHECK".equals(dto.getPhase()) && !session.progress().isRetrievalCheckSubmitted()) {
            List<Question> questions = retrievalService.generateRetrievalCheck(user.getId(), lessonId);
            // Only set questions when the pool is non-empty. An empty pool (null) tells
            // the frontend to show a "no questions" skip path rather than "already done".
            if (!questions.isEmpty()) {
                dto.setRetrievalQuestions(questions.stream().map(this::toQuestionDto).collect(Collectors.toList()));
            }
        }

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{lessonId}/advance")
    public ResponseEntity<LessonEncodingDto> advancePhase(
            @PathVariable String lessonId,
            @AuthenticationPrincipal UserPrincipal user) {
        LessonSession session = encodingService.advancePhase(user.getId(), lessonId);
        LessonEncodingDto dto = toDto(session, user.getId());

        if ("RETRIEVAL_CHECK".equals(dto.getPhase())) {
            List<Question> questions = retrievalService.generateRetrievalCheck(user.getId(), lessonId);
            if (!questions.isEmpty()) {
                dto.setRetrievalQuestions(questions.stream().map(this::toQuestionDto).collect(Collectors.toList()));
            }
        }

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{lessonId}/guided-practice/submit")
    public ResponseEntity<SubmitResponse> submitGuidedPractice(
            @PathVariable String lessonId,
            @Valid @RequestBody CodeSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        PracticeResult result = encodingService.submitGuidedPractice(
                user.getId(), lessonId, request.getCode());

        return ResponseEntity.ok(SubmitResponse.builder()
                .allPassed(result.allPassed())
                .testResults(result.testResults().stream().map(tr ->
                        SubmitResponse.TestResult.builder()
                                .label(tr.label()).passed(tr.passed())
                                .actualOutput(tr.actualOutput()).expectedOutput(tr.expectedOutput())
                                .build()
                ).collect(Collectors.toList()))
                .xpEarned(result.xpEarned())
                .mentorFeedback(result.mentorFeedback())
                .errorType(result.errorType())
                .newBadges(result.newBadges())
                .build());
    }

    @PostMapping("/{lessonId}/solo-practice/submit")
    public ResponseEntity<SoloAssessmentResponse> submitSoloPractice(
            @PathVariable String lessonId,
            @Valid @RequestBody SoloSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {

        SoloAssessmentResult result = encodingService.submitSoloPractice(
                user.getId(), lessonId, request);

        return ResponseEntity.ok(SoloAssessmentResponse.builder()
                .passed(result.passed())
                .band(result.band())
                .feedback(result.feedback())
                .modelAnswerHtml(result.modelAnswerHtml())
                .matchedKeywords(result.matchedKeywords())
                .errorType(result.errorType())
                .xpEarned(result.xpEarned())
                .newBadges(result.newBadges())
                .aiReviewsRemaining(result.aiReviewsRemaining())
                .usedAi(result.usedAi())
                .testResults(result.testResults() == null ? List.of() :
                        result.testResults().stream().map(tr ->
                                SubmitResponse.TestResult.builder()
                                        .label(tr.label()).passed(tr.passed())
                                        .actualOutput(tr.actualOutput())
                                        .expectedOutput(tr.expectedOutput())
                                        .build())
                        .collect(Collectors.toList()))
                .build());
    }

    @PostMapping("/{lessonId}/retrieval-check/submit")
    public ResponseEntity<RetrievalResultDto> submitRetrievalCheck(
            @PathVariable String lessonId,
            @Valid @RequestBody AnswerRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        List<AnswerPair> answers = request.getAnswers().stream()
                .map(a -> new AnswerPair(a.getQuestionId(), a.getAnswer()))
                .collect(Collectors.toList());

        RetrievalCheckResult result = encodingService.submitRetrievalCheck(
                user.getId(), lessonId, answers);

        return ResponseEntity.ok(RetrievalResultDto.builder()
                .score(result.score()).correct(result.correct()).total(result.total())
                .results(result.results().stream().map(r -> RetrievalResultDto.QuestionResultDto.builder()
                        .questionId(r.questionId()).correct(r.correct())
                        .userAnswer(r.userAnswer()).correctAnswer(r.correctAnswer())
                        .explanationHtml(r.explanationHtml())
                        .build()).collect(Collectors.toList()))
                .passed(result.passed()).xpEarned(result.xpEarned())
                .newBadges(result.newBadges()).recommendation(result.recommendation())
                .build());
    }

    @PostMapping("/{lessonId}/feynman/submit")
    public ResponseEntity<FeynmanResultDto> submitFeynman(
            @PathVariable String lessonId,
            @Valid @RequestBody FeynmanRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        FeynmanResult result = feynmanService.evaluateExplanation(
                user.getId(), lessonId, request.getExplanation());

        return ResponseEntity.ok(FeynmanResultDto.builder()
                .accuracy(result.accuracy()).completeness(result.completeness())
                .simplicity(result.simplicity()).connection(result.connection())
                .overallScore(result.overallScore()).feedback(result.feedback())
                .xpEarned(result.xpEarned()).build());
    }

    @GetMapping("/{lessonId}/feynman/prompt")
    public ResponseEntity<java.util.Map<String, String>> getFeynmanPrompt(
            @PathVariable String lessonId) {
        String prompt = feynmanService.getPrompt(lessonId);
        return ResponseEntity.ok(java.util.Map.of("prompt", prompt));
    }

    private LessonEncodingDto toDto(LessonSession session, String userId) {
        Lesson l = session.lesson();
        UserChunkProgress p = session.progress();

        String domainId = moduleRepository.findById(l.getModuleId())
                .map(LearningModule::getTrackId)
                .orElse("software-engineering");

        // Phase 4 — solo assessment metadata
        String soloType = l.getSoloAssessmentType() != null
                ? l.getSoloAssessmentType().name() : null;
        List<String> rubricItems = parseStringList(l.getRubricItemsJson());
        int aiRemaining = 0;
        if (l.getSoloAssessmentType() == SoloAssessmentType.AI_REVIEW) {
            aiRemaining = encodingService.getAiReviewsRemaining(userId, domainId);
        }

        LessonEncodingDto dto = LessonEncodingDto.builder()
                .lessonId(l.getId()).moduleId(l.getModuleId()).domainId(domainId).title(l.getTitle())
                .phase(p.getCurrentPhase().name()).status(p.getStatus().name())
                .xpReward(l.getXpReward()).filename(l.getFilename())
                .practiceType(l.getPracticeType() != null ? l.getPracticeType().name() : "JAVA")
                .rabbitHoleTerms(parseJson(l.getRabbitHoleTermsJson()))
                .learningObjectives(parseStringList(l.getLearningObjectivesJson()))
                .challenge(buildChallengeMap(l))
                .miniProject(l.getMiniProjectHtml())
                .commonMistakes(parseStringList(l.getCommonMistakesJson()))
                .assessmentCriteria(parseStringList(l.getAssessmentCriteriaJson()))
                .downloadables(parseJson(l.getDownloadablesJson()))
                .questType(l.getQuestType() != null ? l.getQuestType().name() : null)
                .hasGuidedSteps(guidedStepService.hasSteps(l.getId()))
                .soloAssessmentType(soloType)
                .rubricItems(rubricItems.isEmpty() ? null : rubricItems)
                .aiReviewsRemaining(aiRemaining)
                .build();

        switch (p.getCurrentPhase()) {
            case HOOK -> dto.setHookHtml(l.getHookHtml());
            case EXPLANATION -> {
                // Phase 2 section fields — null-safe; omitted for JSON-seeded lessons
                dto.setLoreIntroHtml(l.getLoreIntroHtml());
                dto.setExplanationHtml(l.getExplanationHtml());
                dto.setStoryBeats(parseJson(l.getStoryJson()));
                dto.setWhyItMattersHtml(l.getWhyItMattersHtml());
                dto.setWorkedExamplesHtml(l.getWorkedExamplesHtml());
                dto.setMentalModelHtml(l.getMentalModelHtml());
                dto.setMiniSummaryHtml(l.getMiniSummaryHtml());
            }
            case GUIDED_PRACTICE -> {
                dto.setStoryBeats(parseJson(l.getStoryJson()));
                dto.setGuidedPracticeHtml(l.getGuidedPracticeHtml());
                dto.setStarterCode(l.getGuidedPracticeStarterCode());
                dto.setTestCaseLabels(testCasesFor(l));
                dto.setGuidedPracticeModelAnswer(l.getGuidedPracticeModelAnswer());
            }
            case SOLO_PRACTICE -> {
                dto.setStoryBeats(parseJson(l.getStoryJson()));
                dto.setSoloPracticeHtml(l.getSoloPracticeHtml());
                dto.setGuidedPracticeHtml(l.getGuidedPracticeHtml());
                dto.setTestCaseLabels(testCasesFor(l));
                dto.setModelAnswer(l.getModelAnswer());
            }
            case RETRIEVAL_CHECK -> dto.setFeynmanPrompt(l.getFeynmanPrompt());
            case INTEGRATION -> dto.setIntegrationPrompt(l.getIntegrationPrompt());
            case COMPLETE -> {
                dto.setStoryBeats(parseJson(l.getStoryJson()));
                dto.setLoreConclusionHtml(l.getLoreConclusionHtml());
            }
        }

        return dto;
    }

    private QuestionDto toQuestionDto(Question q) {
        List<String> options = null;
        if (q.getOptionsJson() != null) {
            try { options = objectMapper.readValue(q.getOptionsJson(), List.class); } catch (Exception ignored) {}
        }
        return QuestionDto.builder()
                .id(q.getId()).tier(q.getTier().name()).type(q.getType().name())
                .questionHtml(q.getQuestionHtml()).codeSnippet(q.getCodeSnippet())
                .options(options).build();
    }

    private Object parseJson(String json) {
        if (json == null) return List.of();
        try { return objectMapper.readValue(json, Object.class); } catch (Exception e) { return List.of(); }
    }

    private List<String> parseStringList(String json) {
        if (json == null || json.isBlank()) return List.of();
        try { return objectMapper.readValue(json, new TypeReference<>() {}); } catch (Exception e) { return List.of(); }
    }

    private Map<String, Object> buildChallengeMap(Lesson l) {
        if (l.getChallengeHtml() == null || l.getChallengeHtml().isBlank()) return null;
        List<Object> tests = List.of();
        if (l.getChallengeTestsJson() != null && !l.getChallengeTestsJson().isBlank()) {
            try { tests = objectMapper.readValue(l.getChallengeTestsJson(), new TypeReference<>() {}); } catch (Exception ignored) {}
        }
        return Map.of(
            "html", l.getChallengeHtml(),
            "starterCode", l.getChallengeStarterCode() != null ? l.getChallengeStarterCode() : "",
            "tests", tests
        );
    }

    private Object extractTestLabels(String json) {
        if (json == null) return List.of();
        try {
            List<java.util.Map<String, Object>> tests = objectMapper.readValue(json,
                    new TypeReference<>() {});
            return tests.stream()
                    .map(tc -> java.util.Map.of("label", tc.getOrDefault("label", "")))
                    .collect(Collectors.toList());
        } catch (Exception e) { return List.of(); }
    }

    private Object testCasesFor(Lesson l) {
        return extractTestLabels(l.getGuidedPracticeTestsJson());
    }
}
