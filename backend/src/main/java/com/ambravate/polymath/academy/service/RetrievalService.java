package com.ambravate.polymath.academy.service;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
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
     * Generate a retrieval check for a sub-chunk: exactly 4 randomly selected questions.
     *
     * <p>Target distribution: 1 RECALL + 2 APPLICATION + 1 DISCRIMINATION. Each tier's
     * question pool is independently shuffled before selection, so repeated calls to this
     * method return a different set whenever the pool is larger than the quota. If a tier
     * has fewer questions than its quota, the remaining slots are filled from the other
     * tiers to always return exactly 4 questions (or as many as exist in the pool).</p>
     */
    public List<Question> generateRetrievalCheck(String userId, String subChunkId) {
        LearnerPath path = getPath(userId);
        List<Question> pool = questionRepository.findBySubChunkId(subChunkId).stream()
                .filter(q -> q.getMinPath().ordinal() <= path.ordinal())
                .collect(Collectors.toList());

        // Partition by tier and shuffle each bucket independently for true randomisation
        List<Question> recall        = new ArrayList<>(pool.stream().filter(q -> q.getTier() == QuestionTier.RECALL).toList());
        List<Question> application   = new ArrayList<>(pool.stream().filter(q -> q.getTier() == QuestionTier.APPLICATION).toList());
        List<Question> discrimination = new ArrayList<>(pool.stream().filter(q -> q.getTier() == QuestionTier.DISCRIMINATION).toList());

        Collections.shuffle(recall);
        Collections.shuffle(application);
        Collections.shuffle(discrimination);

        // Quotas: 1 RECALL, 2 APPLICATION, 1 DISCRIMINATION
        List<Question> selected = new ArrayList<>();
        selected.addAll(recall.stream().limit(1).toList());
        selected.addAll(application.stream().limit(2).toList());
        selected.addAll(discrimination.stream().limit(1).toList());

        // Fill up to 4 from any remaining unseen questions if a tier was short
        if (selected.size() < 4) {
            Set<String> usedIds = selected.stream().map(Question::getId).collect(Collectors.toSet());
            List<Question> overflow = new ArrayList<>(pool.stream()
                    .filter(q -> !usedIds.contains(q.getId()))
                    .toList());
            Collections.shuffle(overflow);
            int needed = 4 - selected.size();
            selected.addAll(overflow.stream().limit(needed).toList());
        }

        // Present questions in a random order
        Collections.shuffle(selected);
        log.info("[Retrieval] Generated {} questions for subChunk={} user={} (pool: {} recall, {} application, {} discrimination)",
                selected.size(), subChunkId, userId, recall.size(), application.size(), discrimination.size());
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

    public record AnswerPair(String questionId, String answer) {}
    public record QuestionResult(String questionId, String subChunkId, boolean correct,
                                 String userAnswer, String correctAnswer, String explanationHtml) {}
    public record GradeResult(double score, int correct, int total, List<QuestionResult> results) {}
}
