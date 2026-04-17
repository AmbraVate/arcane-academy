package com.ambravate.polymath.academy.controller;

import com.ambravate.polymath.academy.dto.*;
import com.ambravate.polymath.academy.model.Question;
import com.ambravate.polymath.academy.security.UserPrincipal;
import com.ambravate.polymath.academy.service.InterleavingService;
import com.ambravate.polymath.academy.service.RetrievalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final InterleavingService interleavingService;
    private final ObjectMapper objectMapper;

    @GetMapping("/daily")
    public ResponseEntity<ReviewSessionDto> getDailyReview(
            @AuthenticationPrincipal UserPrincipal user) {
        InterleavingService.ReviewSessionQuestions session = interleavingService.generateDailyReview(user.getId());
        return ResponseEntity.ok(toSessionDto(session, "DAILY_REVIEW"));
    }

    @GetMapping("/interleaved/{subChunkId}")
    public ResponseEntity<ReviewSessionDto> getInterleavedReview(
            @PathVariable String subChunkId,
            @AuthenticationPrincipal UserPrincipal user) {
        InterleavingService.ReviewSessionQuestions session =
                interleavingService.generateInterleavedReview(user.getId(), subChunkId);
        return ResponseEntity.ok(toSessionDto(session, "INTERLEAVED_REVIEW"));
    }

    @PostMapping("/{sessionId}/submit")
    public ResponseEntity<ReviewResultDto> submitReview(
            @PathVariable String sessionId,
            @RequestBody AnswerRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        List<RetrievalService.AnswerPair> answers = request.getAnswers().stream()
                .map(a -> new RetrievalService.AnswerPair(a.getQuestionId(), a.getAnswer()))
                .collect(Collectors.toList());

        InterleavingService.ReviewResult result = interleavingService.submitReview(
                user.getId(), sessionId, answers);

        return ResponseEntity.ok(ReviewResultDto.builder()
                .score(result.score()).correct(result.correct()).total(result.total())
                .results(result.results().stream().map(r -> RetrievalResultDto.QuestionResultDto.builder()
                        .questionId(r.questionId()).correct(r.correct())
                        .userAnswer(r.userAnswer()).correctAnswer(r.correctAnswer())
                        .explanationHtml(r.explanationHtml())
                        .build()).collect(Collectors.toList()))
                .newBadges(result.newBadges())
                .build());
    }

    private ReviewSessionDto toSessionDto(InterleavingService.ReviewSessionQuestions session, String type) {
        List<QuestionDto> questionDtos = session.questions().stream().map(q -> {
            List<String> options = null;
            if (q.getOptionsJson() != null) {
                try { options = objectMapper.readValue(q.getOptionsJson(), List.class); } catch (Exception ignored) {}
            }
            return QuestionDto.builder()
                    .id(q.getId()).tier(q.getTier().name()).type(q.getType().name())
                    .questionHtml(q.getQuestionHtml()).codeSnippet(q.getCodeSnippet())
                    .options(options).build();
        }).collect(Collectors.toList());

        return ReviewSessionDto.builder()
                .sessionId(session.sessionId())
                .sessionType(type)
                .questions(questionDtos)
                .estimatedMinutes(Math.max(5, questionDtos.size() * 2))
                .build();
    }
}
