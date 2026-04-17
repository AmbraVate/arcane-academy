package com.ambravate.polymath.academy.controller;

import com.ambravate.polymath.academy.dto.*;
import com.ambravate.polymath.academy.model.DiagnosticResult;
import com.ambravate.polymath.academy.model.DiagnosticSession;
import com.ambravate.polymath.academy.model.Question;
import com.ambravate.polymath.academy.model.UserLearnerProfile;
import com.ambravate.polymath.academy.repository.UserLearnerProfileRepository;
import com.ambravate.polymath.academy.security.UserPrincipal;
import com.ambravate.polymath.academy.service.DiagnosticService;
import com.ambravate.polymath.academy.service.RetrievalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diagnostic")
@RequiredArgsConstructor
public class DiagnosticController {

    private final DiagnosticService diagnosticService;
    private final UserLearnerProfileRepository profileRepository;
    private final ObjectMapper objectMapper;

    @PostMapping("/start")
    public ResponseEntity<ReviewSessionDto> startDiagnostic(
            @RequestParam(defaultValue = "java") String topicId,
            @AuthenticationPrincipal UserPrincipal user) {
        DiagnosticSession session = diagnosticService.startEntryDiagnostic(user.getId(), topicId);

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

        return ResponseEntity.ok(ReviewSessionDto.builder()
                .sessionId(session.sessionId())
                .sessionType("DIAGNOSTIC")
                .questions(questionDtos)
                .estimatedMinutes(15)
                .build());
    }

    @PostMapping("/submit")
    public ResponseEntity<DiagnosticResultDto> submitDiagnostic(
            @RequestParam(defaultValue = "java") String topicId,
            @RequestBody AnswerRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        List<RetrievalService.AnswerPair> answers = request.getAnswers().stream()
                .map(a -> new RetrievalService.AnswerPair(a.getQuestionId(), a.getAnswer()))
                .collect(Collectors.toList());

        DiagnosticResult result = diagnosticService.submitDiagnostic(user.getId(), answers, topicId);

        return ResponseEntity.ok(DiagnosticResultDto.builder()
                .recommendedPath(result.recommendedPath().name())
                .chunkRecommendations(result.chunkRecommendations())
                .overallScore(result.overallScore())
                .build());
    }

    @PostMapping("/skip")
    public ResponseEntity<Void> skipDiagnostic(
            @RequestParam(defaultValue = "java") String topicId,
            @AuthenticationPrincipal UserPrincipal user) {
        diagnosticService.skipDiagnostic(user.getId(), topicId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/results")
    public ResponseEntity<DiagnosticResultDto> getResults(
            @AuthenticationPrincipal UserPrincipal user) {
        UserLearnerProfile profile = profileRepository.findByUserId(user.getId()).orElse(null);
        if (profile == null || !profile.isDiagnosticCompleted()) {
            return ResponseEntity.ok(DiagnosticResultDto.builder()
                    .recommendedPath("FOUNDATION")
                    .chunkRecommendations(Map.of())
                    .overallScore(0.0).build());
        }

        Map<String, String> recommendations = Map.of();
        if (profile.getDiagnosticResultsJson() != null) {
            try {
                recommendations = objectMapper.readValue(profile.getDiagnosticResultsJson(),
                        new com.fasterxml.jackson.core.type.TypeReference<>() {});
            } catch (Exception ignored) {}
        }

        return ResponseEntity.ok(DiagnosticResultDto.builder()
                .recommendedPath(profile.getCurrentPath().name())
                .chunkRecommendations(recommendations)
                .overallScore(0.0).build());
    }
}
