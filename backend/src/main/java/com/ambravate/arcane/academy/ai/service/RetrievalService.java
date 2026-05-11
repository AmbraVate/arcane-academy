package com.ambravate.arcane.academy.ai.service;

import com.ambravate.arcane.academy.ai.domain.AnswerPair;
import com.ambravate.arcane.academy.ai.domain.GradeResult;
import com.ambravate.arcane.academy.ai.domain.QuestionResult;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.QuestionTier;
import com.ambravate.arcane.academy.common.domain.ReviewSession;
import com.ambravate.arcane.academy.common.domain.SessionType;
import com.ambravate.arcane.academy.common.domain.UserLearnerProfile;
import com.ambravate.arcane.academy.common.repository.QuestionRepository;
import com.ambravate.arcane.academy.common.repository.ReviewSessionRepository;
import com.ambravate.arcane.academy.common.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.common.telemetry.service.TelemetryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetrievalService {

    private final QuestionRepository questionRepository;
    private final UserLearnerProfileRepository profileRepository;
    private final ReviewSessionRepository sessionRepository;
    private final TelemetryService telemetry;

    /**
     * Generate a retrieval check for a sub-chunk: 3-5 questions distributed by tier.
     * Tier distribution: ~20% Recall, ~50% Application, ~30% Discrimination
     */
    public List<Question> generateRetrievalCheck(String userId, String subChunkId) {
        LearnerPath path = getPath(userId);
        List<Question> pool = questionRepository.findBySubChunkId(subChunkId).stream()
                .filter(q -> q.getMinPath().ordinal() <= path.ordinal())
                .toList();

        List<Question> recall = pool.stream().filter(q -> q.getTier() == QuestionTier.RECALL).collect(Collectors.toList());
        List<Question> application = pool.stream().filter(q -> q.getTier() == QuestionTier.APPLICATION).collect(Collectors.toList());
        List<Question> discrimination = pool.stream().filter(q -> q.getTier() == QuestionTier.DISCRIMINATION).collect(Collectors.toList());

        Collections.shuffle(recall);
        Collections.shuffle(application);
        Collections.shuffle(discrimination);

        List<Question> selected = new ArrayList<>();
        // 1 recall, 2 application, 1 discrimination (4 questions minimum)
        if (!recall.isEmpty()) selected.add(recall.getFirst());
        selected.addAll(application.stream().limit(2).toList());
        if (!discrimination.isEmpty()) selected.add(discrimination.getFirst());

        // Add one more if we have extras
        if (application.size() > 2) selected.add(application.get(2));

        Collections.shuffle(selected);
        log.info("[Retrieval] Generated {} questions for subChunk={} user={}", selected.size(), subChunkId, userId);
        return selected;
    }

    /**
     * Grade a list of answers against question correct answers.
     * Returns score as 0.0-1.0.
     */
    public GradeResult gradeAnswers(List<AnswerPair> answers) {
        int correct = 0;
        List<QuestionResult> results = new ArrayList<>();

        for (AnswerPair pair : answers) {
            Question question = questionRepository.findById(pair.questionId()).orElse(null);
            if (question == null) continue;

            boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(pair.answer().trim());
            if (isCorrect) correct++;

            results.add(new QuestionResult(
                    question.getId(), question.getSubChunkId(), isCorrect,
                    pair.answer().trim(), question.getCorrectAnswer(), question.getExplanationHtml()
            ));
        }

        double score = answers.isEmpty() ? 0.0 : (double) correct / answers.size();
        return new GradeResult(score, correct, answers.size(), results);
    }

    /**
     * Create and save a review session with results.
     */
    @Transactional
    public ReviewSession saveSession(String userId, SessionType type, GradeResult result) {
        ReviewSession session = ReviewSession.builder()
                .userId(userId)
                .sessionType(type)
                .startedAt(Instant.now())
                .completedAt(Instant.now())
                .totalQuestions(result.total())
                .correctAnswers(result.correct())
                .score(result.score())
                .build();
        ReviewSession saved = sessionRepository.save(session);

        // Telemetry: one event per session, plus per-answer grade events for granular dashboards
        String cadence = type.name();
        telemetry.reviewSessionCompleted(userId, cadence, result.correct(), result.total());
        for (QuestionResult qr : result.results()) {
            telemetry.reviewGradeGiven(userId, cadence, qr.correct() ? 1.0 : 0.0);
        }

        return saved;
    }

    private LearnerPath getPath(String userId) {
        return profileRepository.findByUserId(userId)
                .map(UserLearnerProfile::getCurrentPath)
                .orElse(LearnerPath.FOUNDATION);
    }

}
