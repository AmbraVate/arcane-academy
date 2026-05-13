package com.ambravate.arcane.academy.practice.controller;

import com.ambravate.arcane.academy.ai.domain.AnswerPair;
import com.ambravate.arcane.academy.ai.domain.FeynmanResult;
import com.ambravate.arcane.academy.common.dto.AnswerRequest;
import com.ambravate.arcane.academy.practice.dto.CodeSubmitRequest;
import com.ambravate.arcane.academy.ai.dto.FeynmanRequest;
import com.ambravate.arcane.academy.ai.dto.FeynmanResultDto;
import com.ambravate.arcane.academy.common.dto.QuestionDto;
import com.ambravate.arcane.academy.ai.dto.RetrievalResultDto;
import com.ambravate.arcane.academy.practice.dto.SubChunkEncodingDto;
import com.ambravate.arcane.academy.practice.dto.SubmitResponse;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkPracticeType;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.practice.domain.PracticeResult;
import com.ambravate.arcane.academy.practice.domain.RetrievalCheckResult;
import com.ambravate.arcane.academy.practice.domain.SubChunkSession;
import com.ambravate.arcane.academy.practice.service.EncodingService;
import com.ambravate.arcane.academy.ai.service.FeynmanService;
import com.ambravate.arcane.academy.practice.service.ReactPracticeService;
import com.ambravate.arcane.academy.ai.service.RetrievalService;
import com.ambravate.arcane.academy.practice.service.SqlPracticeService;

import com.ambravate.arcane.academy.common.repository.ChunkRepository;
import com.ambravate.arcane.academy.common.security.UserPrincipal;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/encoding")
@RequiredArgsConstructor
public class EncodingController {

    private final EncodingService encodingService;
    private final RetrievalService retrievalService;
    private final FeynmanService feynmanService;
    private final ChunkRepository chunkRepository;
    private final ObjectMapper objectMapper;

    @PostMapping("/{subChunkId}/start")
    public ResponseEntity<SubChunkEncodingDto> startSubChunk(
            @PathVariable String subChunkId,
            @AuthenticationPrincipal UserPrincipal user) {
        SubChunkSession session = encodingService.startSubChunk(user.getId(), subChunkId);
        return ResponseEntity.ok(toDto(session));
    }

    @PostMapping("/{subChunkId}/advance")
    public ResponseEntity<SubChunkEncodingDto> advancePhase(
            @PathVariable String subChunkId,
            @AuthenticationPrincipal UserPrincipal user) {
        SubChunkSession session = encodingService.advancePhase(user.getId(), subChunkId);

        SubChunkEncodingDto dto = toDto(session);

        // If entering RETRIEVAL_CHECK, generate questions
        if ("RETRIEVAL_CHECK".equals(dto.getPhase())) {
            List<Question> questions = retrievalService.generateRetrievalCheck(user.getId(), subChunkId);
            dto.setRetrievalQuestions(questions.stream().map(this::toQuestionDto).collect(Collectors.toList()));
        }

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{subChunkId}/guided-practice/submit")
    public ResponseEntity<SubmitResponse> submitGuidedPractice(
            @PathVariable String subChunkId,
            @RequestBody CodeSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        PracticeResult result = encodingService.submitGuidedPractice(
                user.getId(), subChunkId, request.getCode());

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

    @PostMapping("/{subChunkId}/solo-practice/submit")
    public ResponseEntity<SubmitResponse> submitSoloPractice(
            @PathVariable String subChunkId,
            @RequestBody CodeSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        PracticeResult result = encodingService.submitSoloPractice(
                user.getId(), subChunkId, request.getCode());

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

    @PostMapping("/{subChunkId}/retrieval-check/submit")
    public ResponseEntity<RetrievalResultDto> submitRetrievalCheck(
            @PathVariable String subChunkId,
            @RequestBody AnswerRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        List<AnswerPair> answers = request.getAnswers().stream()
                .map(a -> new AnswerPair(a.getQuestionId(), a.getAnswer()))
                .collect(Collectors.toList());

        RetrievalCheckResult result = encodingService.submitRetrievalCheck(
                user.getId(), subChunkId, answers);

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

    @PostMapping("/{subChunkId}/feynman/submit")
    public ResponseEntity<FeynmanResultDto> submitFeynman(
            @PathVariable String subChunkId,
            @RequestBody FeynmanRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        FeynmanResult result = feynmanService.evaluateExplanation(
                user.getId(), subChunkId, request.getExplanation());

        return ResponseEntity.ok(FeynmanResultDto.builder()
                .accuracy(result.accuracy()).completeness(result.completeness())
                .simplicity(result.simplicity()).connection(result.connection())
                .overallScore(result.overallScore()).feedback(result.feedback())
                .xpEarned(result.xpEarned()).build());
    }

    @GetMapping("/{subChunkId}/feynman/prompt")
    public ResponseEntity<java.util.Map<String, String>> getFeynmanPrompt(
            @PathVariable String subChunkId) {
        String prompt = feynmanService.getPrompt(subChunkId);
        return ResponseEntity.ok(java.util.Map.of("prompt", prompt));
    }

    private SubChunkEncodingDto toDto(SubChunkSession session) {
        SubChunk sc = session.subChunk();
        UserChunkProgress p = session.progress();

        String topicId = chunkRepository.findById(sc.getChunkId())
                .map(Chunk::getTopicId)
                .orElse("java");

        SubChunkEncodingDto dto = SubChunkEncodingDto.builder()
                .subChunkId(sc.getId()).chunkId(sc.getChunkId()).topicId(topicId).title(sc.getTitle())
                .phase(p.getCurrentPhase().name()).status(p.getStatus().name())
                .xpReward(sc.getXpReward()).filename(sc.getFilename())
                .practiceType(sc.getPracticeType() != null ? sc.getPracticeType().name() : "JAVA")
                .build();

        // Populate phase-specific content
        switch (p.getCurrentPhase()) {
            case HOOK -> dto.setHookHtml(sc.getHookHtml());
            case EXPLANATION -> {
                dto.setExplanationHtml(sc.getExplanationHtml());
                dto.setStoryBeats(parseJson(sc.getStoryJson()));
            }
            case GUIDED_PRACTICE -> {
                dto.setGuidedPracticeHtml(sc.getGuidedPracticeHtml());
                dto.setStarterCode(sc.getGuidedPracticeStarterCode());
                dto.setTestCaseLabels(testCasesFor(sc));
            }
            case SOLO_PRACTICE -> {
                dto.setSoloPracticeHtml(sc.getSoloPracticeHtml());
                // Guided practice HTML doubles as the "peek" hint — no starter code
                dto.setGuidedPracticeHtml(sc.getGuidedPracticeHtml());
                dto.setTestCaseLabels(testCasesFor(sc));
            }
            case RETRIEVAL_CHECK -> dto.setFeynmanPrompt(sc.getFeynmanPrompt());
            case COMPLETE -> {}
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

    private Object extractTestLabels(String json) {
        if (json == null) return List.of();
        try {
            List<java.util.Map<String, Object>> tests = objectMapper.readValue(json,
                    new com.fasterxml.jackson.core.type.TypeReference<>() {});
            return tests.stream()
                    .map(tc -> java.util.Map.of("label", tc.getOrDefault("label", "")))
                    .collect(Collectors.toList());
        } catch (Exception e) { return List.of(); }
    }

    /**
     * For JAVA/TAILWIND practice, only test labels are sent to the client (the
     * server holds the full specs and runs them). For REACT and SQL practice,
     * the full test specs are sent because tests run inside the iframe — see
     * {@link ReactPracticeService} and
     * {@link SqlPracticeService}.
     */
    private Object testCasesFor(SubChunk sc) {
        if (sc.getPracticeType() == SubChunkPracticeType.REACT
                || sc.getPracticeType() == SubChunkPracticeType.SQL) {
            String json = sc.getGuidedPracticeTestsJson();
            if (json == null) return List.of();
            try {
                return objectMapper.readValue(json,
                        new com.fasterxml.jackson.core.type.TypeReference<List<java.util.Map<String, Object>>>() {});
            } catch (Exception e) { return List.of(); }
        }
        return extractTestLabels(sc.getGuidedPracticeTestsJson());
    }
}
