package com.ambravate.arcane.academy.retention.service;

import com.ambravate.arcane.academy.ai.domain.AnswerPair;
import com.ambravate.arcane.academy.ai.domain.GradeResult;
import com.ambravate.arcane.academy.ai.domain.QuestionResult;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.ai.service.RetrievalService;
import com.ambravate.arcane.academy.retention.domain.ConceptRetention;
import com.ambravate.arcane.academy.retention.domain.QuestionAttempt;
import com.ambravate.arcane.academy.retention.domain.StabilityState;
import com.ambravate.arcane.academy.retention.dto.ReviewQueueItem;
import com.ambravate.arcane.academy.retention.dto.ReviewSubmitRequest;
import com.ambravate.arcane.academy.retention.dto.ReviewSubmitResponse;
import com.ambravate.arcane.academy.retention.repository.ConceptRetentionRepository;
import com.ambravate.arcane.academy.retention.repository.QuestionAttemptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetentionService {

    private final ConceptRetentionRepository retentionRepo;
    private final QuestionAttemptRepository  attemptRepo;
    private final QuestionRepository         questionRepository;
    private final LessonRepository           lessonRepository;
    private final RetrievalService           retrievalService;

    private static final int  QUEUE_CAP        = 8;
    private static final int  MAX_HISTORY      = 4;

    // ── Knowledge check (in-lesson, silent) ──────────────────────────────────

    @Transactional
    public void recordKnowledgeCheck(String userId, String lessonId,
                                     List<QuestionResult> results) {
        if (results == null || results.isEmpty()) return;

        // Persist raw attempts
        results.forEach(r -> attemptRepo.save(QuestionAttempt.builder()
                .userId(userId).lessonId(lessonId).questionId(r.questionId())
                .score((short) (r.correct() ? 1 : 0))
                .retrieval(false).intervalDay(null).build()));

        double lessonScore = results.stream()
                .mapToInt(r -> r.correct() ? 1 : 0).average().orElse(0.0);

        ConceptRetention cr = retentionRepo.findByUserIdAndLessonId(userId, lessonId)
                .orElseGet(() -> ConceptRetention.builder()
                        .userId(userId).lessonId(lessonId)
                        .attemptHistory("[]").consecutiveCorrect((short) 0)
                        .stabilityState(StabilityState.UNVERIFIED).build());

        // Initial score is lesson score * 0.7 (downweighted until retrieval confirms it)
        cr.setRetentionScore(bd(lessonScore * 0.7));
        cr.setStabilityState(StabilityState.UNVERIFIED);
        cr.setConsecutiveCorrect((short) 0);
        cr.setNextReviewDate(LocalDate.now().plusDays(1));

        retentionRepo.save(cr);
        log.debug("[Retention] KnowledgeCheck recorded userId={} lessonId={} score={}", userId, lessonId, lessonScore);
    }

    // ── Teach Back ────────────────────────────────────────────────────────────

    @Transactional
    public void recordTeachBack(String userId, String lessonId, double teachBackScore) {
        retentionRepo.findByUserIdAndLessonId(userId, lessonId).ifPresent(cr -> {
            cr.setTeachBackScore(bd(teachBackScore));
            if (teachBackScore >= 0.9) {
                cr.setRetentionScore(clamp(cr.getRetentionScore().doubleValue() + 0.10));
            }
            retentionRepo.save(cr);
            log.debug("[Retention] TeachBack recorded userId={} lessonId={} tbScore={}", userId, lessonId, teachBackScore);
        });
    }

    // ── Daily review submission ───────────────────────────────────────────────

    @Transactional
    public ReviewSubmitResponse recordReview(String userId,
                                             ReviewSubmitRequest request) {
        List<ReviewSubmitResponse.LessonResult> lessonResults = new ArrayList<>();

        for (ReviewSubmitRequest.LessonAnswers la : request.getLessons()) {
            String lessonId = la.getLessonId();
            List<AnswerPair> pairs = la.getAnswers().stream()
                    .map(a -> new AnswerPair(a.getQuestionId(), a.getAnswer()))
                    .toList();

            GradeResult graded = retrievalService.gradeAnswers(pairs);
            double reviewScore = graded.score();

            // Determine next interval from existing retention record
            ConceptRetention cr = retentionRepo.findByUserIdAndLessonId(userId, lessonId)
                    .orElseGet(() -> ConceptRetention.builder()
                            .userId(userId).lessonId(lessonId)
                            .attemptHistory("[]").consecutiveCorrect((short) 0)
                            .stabilityState(StabilityState.UNVERIFIED)
                            .retentionScore(BigDecimal.ZERO).build());

            // Determine the interval this retrieval is at (based on consecutive_correct before this attempt)
            short prevConsec = cr.getConsecutiveCorrect();
            short intervalDay = intervalForConsecutive(prevConsec);

            // Persist attempts
            graded.results().forEach(r -> attemptRepo.save(QuestionAttempt.builder()
                    .userId(userId).lessonId(lessonId).questionId(r.questionId())
                    .score((short) (r.correct() ? 1 : 0))
                    .retrieval(true).intervalDay(intervalDay).build()));

            // Update attempt history (rolling last MAX_HISTORY)
            List<Integer> history = parseHistory(cr.getAttemptHistory());
            history.add(reviewScore >= 0.6 ? 1 : 0);  // treat partial credit as pass
            if (history.size() > MAX_HISTORY) history.remove(0);
            cr.setAttemptHistory(serializeHistory(history));

            // Update consecutive_correct
            boolean passed = reviewScore >= 0.6;
            short newConsec = passed ? (short) (prevConsec + 1) : 0;
            cr.setConsecutiveCorrect(newConsec);

            // Update retention score
            double currentScore = cr.getRetentionScore().doubleValue();
            if (passed) {
                cr.setRetentionScore(clamp(currentScore + 0.15));
            } else {
                cr.setRetentionScore(clamp(currentScore - 0.20));
            }

            // Compute new stability and schedule
            cr.setStabilityState(computeStability(history, newConsec));
            cr.setNextReviewDate(computeNextReviewDate(newConsec, passed));

            retentionRepo.save(cr);
            log.debug("[Retention] Review recorded userId={} lessonId={} score={} consec={} stability={}",
                    userId, lessonId, reviewScore, newConsec, cr.getStabilityState());

            lessonResults.add(ReviewSubmitResponse.LessonResult.builder()
                    .lessonId(lessonId)
                    .newStabilityState(cr.getStabilityState().name())
                    .questions(graded.results().stream()
                            .map(r -> ReviewSubmitResponse.QuestionResult.builder()
                                    .questionId(r.questionId())
                                    .correct(r.correct())
                                    .correctAnswer(r.correctAnswer())
                                    .explanationHtml(r.explanationHtml())
                                    .build())
                            .toList())
                    .build());
        }

        return ReviewSubmitResponse.builder().results(lessonResults).build();
    }

    // ── Review queue ─────────────────────────────────────────────────────────

    @Transactional
    public List<ReviewQueueItem> getReviewQueue(String userId) {
        LocalDate today = LocalDate.now();

        List<ConceptRetention> due = retentionRepo.findDueForReview(userId, today);

        // Apply missed-login weakening lazily
        due.forEach(cr -> {
            if (cr.getNextReviewDate() != null && cr.getNextReviewDate().isBefore(today.minusDays(1))) {
                cr.setStabilityState(StabilityState.WEAKENED);
                cr.setConsecutiveCorrect((short) 0);
                cr.setRetentionScore(clamp(cr.getRetentionScore().doubleValue() - 0.20));
                cr.setNextReviewDate(today);
                retentionRepo.save(cr);
            }
        });

        List<ReviewQueueItem> queue = new ArrayList<>();
        int totalQuestions = 0;

        for (ConceptRetention cr : due) {
            if (totalQuestions >= QUEUE_CAP) break;

            List<Question> questions = questionRepository.findByLessonId(cr.getLessonId());
            if (questions.isEmpty()) continue;

            // Shuffle and cap so we don't overload a single lesson
            Collections.shuffle(questions);
            int take = Math.min(questions.size(), QUEUE_CAP - totalQuestions);
            List<Question> selected = questions.subList(0, take);

            String lessonTitle = lessonRepository.findById(cr.getLessonId())
                    .map(l -> l.getTitle()).orElse(cr.getLessonId());

            List<ReviewQueueItem.ReviewQuestion> qDtos = selected.stream()
                    .map(q -> ReviewQueueItem.ReviewQuestion.builder()
                            .questionId(q.getId())
                            .questionText(q.getQuestionHtml())
                            .options(parseOptions(q.getOptionsJson()))
                            .inputType(q.getType() != null ? q.getType().name() : "MULTIPLE_CHOICE")
                            .build())
                    .toList();

            queue.add(ReviewQueueItem.builder()
                    .lessonId(cr.getLessonId())
                    .lessonTitle(lessonTitle)
                    .retentionScore(cr.getRetentionScore().doubleValue())
                    .stabilityState(cr.getStabilityState().name())
                    .nextReviewDate(cr.getNextReviewDate())
                    .questions(qDtos)
                    .build());

            totalQuestions += qDtos.size();
        }

        return queue;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private StabilityState computeStability(List<Integer> history, short consecutiveCorrect) {
        if (history.isEmpty()) return StabilityState.UNVERIFIED;

        // Last answer was wrong → weakened
        if (history.get(history.size() - 1) == 0) return StabilityState.WEAKENED;

        if (consecutiveCorrect >= 3) return StabilityState.RETAINED;
        if (consecutiveCorrect >= 2) return StabilityState.STABLE;

        // Alternating pattern (high variance) → unstable
        if (history.size() >= 3) {
            int alternations = 0;
            for (int i = 1; i < history.size(); i++) {
                if (!history.get(i).equals(history.get(i - 1))) alternations++;
            }
            if (alternations >= history.size() - 1) return StabilityState.UNSTABLE;
        }

        return StabilityState.UNVERIFIED;
    }

    private LocalDate computeNextReviewDate(short consecutiveCorrect, boolean passed) {
        if (!passed) return LocalDate.now().plusDays(1);
        return switch (consecutiveCorrect) {
            case 1  -> LocalDate.now().plusDays(7);
            case 2  -> LocalDate.now().plusDays(14);
            default -> consecutiveCorrect >= 3 ? LocalDate.now().plusDays(30) : LocalDate.now().plusDays(1);
        };
    }

    private short intervalForConsecutive(short consecutiveCorrect) {
        return switch (consecutiveCorrect) {
            case 0 -> 1;
            case 1 -> 7;
            case 2 -> 14;
            default -> 30;
        };
    }

    private List<Integer> parseHistory(String json) {
        if (json == null || json.isBlank() || json.equals("[]")) return new ArrayList<>();
        String stripped = json.replaceAll("[\\[\\]\\s]", "");
        if (stripped.isEmpty()) return new ArrayList<>();
        return Arrays.stream(stripped.split(","))
                .map(String::trim).filter(s -> !s.isEmpty())
                .map(s -> { try { return Integer.parseInt(s); } catch (NumberFormatException e) { return 0; } })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private String serializeHistory(List<Integer> history) {
        return "[" + history.stream().map(String::valueOf).collect(Collectors.joining(",")) + "]";
    }

    private BigDecimal clamp(double value) {
        return bd(Math.max(0.0, Math.min(1.0, value)));
    }

    private BigDecimal bd(double value) {
        return BigDecimal.valueOf(value).setScale(3, RoundingMode.HALF_UP);
    }

    @SuppressWarnings("unchecked")
    private List<String> parseOptions(String optionsJson) {
        if (optionsJson == null || optionsJson.isBlank()) return List.of();
        try {
            // Simple JSON array parse: ["a","b","c"]
            String stripped = optionsJson.trim();
            if (!stripped.startsWith("[")) return List.of();
            stripped = stripped.substring(1, stripped.length() - 1);
            List<String> opts = new ArrayList<>();
            for (String part : stripped.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")) {
                String cleaned = part.trim().replaceAll("^\"|\"$", "");
                opts.add(cleaned);
            }
            return opts;
        } catch (Exception e) {
            return List.of();
        }
    }
}
