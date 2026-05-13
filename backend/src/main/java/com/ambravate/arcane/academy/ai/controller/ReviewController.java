package com.ambravate.arcane.academy.ai.controller;

import com.ambravate.arcane.academy.ai.domain.AnswerPair;
import com.ambravate.arcane.academy.ai.domain.ReviewResult;
import com.ambravate.arcane.academy.ai.domain.ReviewSessionQuestions;
import com.ambravate.arcane.academy.common.dto.AnswerRequest;
import com.ambravate.arcane.academy.common.dto.QuestionDto;
import com.ambravate.arcane.academy.ai.dto.RetrievalResultDto;
import com.ambravate.arcane.academy.ai.dto.ReviewResultDto;
import com.ambravate.arcane.academy.ai.dto.ReviewSessionDto;

import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.ai.service.InterleavingService;
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
      @AuthenticationPrincipal UserPrincipal user
  ) {
    ReviewSessionQuestions session = interleavingService.generateDailyReview(
        user.getId());
    return ResponseEntity.ok(toSessionDto(session, "DAILY_REVIEW"));
  }

  @GetMapping("/interleaved/{subChunkId}")
  public ResponseEntity<ReviewSessionDto> getInterleavedReview(
      @PathVariable String subChunkId,
      @AuthenticationPrincipal UserPrincipal user
  ) {
    ReviewSessionQuestions session =
        interleavingService.generateInterleavedReview(user.getId(), subChunkId);
    return ResponseEntity.ok(toSessionDto(session, "INTERLEAVED_REVIEW"));
  }

  @PostMapping("/{sessionId}/submit")
  public ResponseEntity<ReviewResultDto> submitReview(
      @PathVariable String sessionId,
      @RequestBody AnswerRequest request,
      @AuthenticationPrincipal UserPrincipal user
  ) {
    List<AnswerPair> answers = request.getAnswers().stream()
        .map(a -> new AnswerPair(a.getQuestionId(), a.getAnswer()))
        .collect(Collectors.toList());

    ReviewResult result = interleavingService.submitReview(
        user.getId(), sessionId, answers);

    return ResponseEntity.ok(ReviewResultDto.builder()
        .score(result.score())
        .correct(result.correct())
        .total(result.total())
        .results(result.results().stream().map(r -> RetrievalResultDto.QuestionResultDto.builder()
            .questionId(r.questionId()).correct(r.correct())
            .userAnswer(r.userAnswer()).correctAnswer(r.correctAnswer())
            .explanationHtml(r.explanationHtml())
            .build()).collect(Collectors.toList()))
        .newBadges(result.newBadges())
        .build());
  }

  private ReviewSessionDto toSessionDto(
      ReviewSessionQuestions session,
      String type
  ) {
    var questionDtos = session.questions()
        .stream()
        .map(q -> {
      List<String> options = null;
      if (q.getOptionsJson() != null) {
        try {
          options = objectMapper.readValue(q.getOptionsJson(), List.class);
        } catch (Exception ignored) {
        }
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
