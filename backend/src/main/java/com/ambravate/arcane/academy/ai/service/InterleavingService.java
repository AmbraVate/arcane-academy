package com.ambravate.arcane.academy.ai.service;

import com.ambravate.arcane.academy.ai.domain.AnswerPair;
import com.ambravate.arcane.academy.ai.domain.GradeResult;
import com.ambravate.arcane.academy.ai.domain.QuestionResult;
import com.ambravate.arcane.academy.ai.domain.ReviewResult;
import com.ambravate.arcane.academy.ai.domain.ReviewSessionQuestions;
import com.ambravate.arcane.academy.common.dto.BadgeDto;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.ReviewSession;
import com.ambravate.arcane.academy.common.domain.SessionType;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.domain.UserLearnerProfile;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.content.repository.SubChunkRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import com.ambravate.arcane.academy.ai.service.SpacingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterleavingService {

    private final QuestionRepository questionRepository;
    private final UserChunkProgressRepository progressRepository;
    private final UserLearnerProfileRepository profileRepository;
    private final SubChunkRepository subChunkRepository;
    private final RetrievalService retrievalService;
    private final SpacingService spacingService;
    private final GamificationFacade gamification;

    /**
     * Generate an interleaved review after completing a sub-chunk.
     * Pulls 5-10 questions from current + previous sub-chunks, weighted by:
     * 1. Recency (recently learned = more questions)
     * 2. Weakness (lower memoryStrength = more questions)
     * 3. Cross-chunk connections
     */
    public ReviewSessionQuestions generateInterleavedReview(String userId, String currentSubChunkId) {
        LearnerPath path = getPath(userId);

        // Get all completed sub-chunks for this user
        List<UserChunkProgress> completed = progressRepository.findByUserId(userId).stream()
                .filter(p -> p.getStatus() == SubChunkStatus.COMPLETE || p.getStatus() == SubChunkStatus.SKIPPED)
                .toList();

        if (completed.isEmpty()) return new ReviewSessionQuestions(List.of());

        // Collect sub-chunk IDs with weights
        Map<String, Double> weights = new HashMap<>();
        for (UserChunkProgress p : completed) {
            double decayedStrength = spacingService.computeDecayedStrength(p);
            double weaknessWeight = 1.0 - decayedStrength;

            // Recency bonus: completed within last 3 days gets a boost
            double recencyBonus = 0.0;
            if (p.getCompletedAt() != null) {
                long hoursSince = Duration.between(p.getCompletedAt(), Instant.now()).toHours();
                if (hoursSince < 72) recencyBonus = 0.3;
            }

            weights.put(p.getSubChunkId(), weaknessWeight + recencyBonus);
        }

        // Current sub-chunk always gets weight
        weights.put(currentSubChunkId, weights.getOrDefault(currentSubChunkId, 0.5) + 0.5);

        // Get questions from all relevant sub-chunks
        List<String> subChunkIds = new ArrayList<>(weights.keySet());
        List<Question> pool = questionRepository.findBySubChunkIdIn(subChunkIds).stream()
                .filter(q -> q.getMinPath().ordinal() <= path.ordinal())
                .collect(Collectors.toList());

        // Weighted random selection of 7 questions
        List<Question> selected = weightedSample(pool, weights, 7);

        log.info("[Interleaving] Generated {} questions for user={} after subChunk={}",
                selected.size(), userId, currentSubChunkId);

        // Create session
        ReviewSession session = retrievalService.saveSession(userId, SessionType.INTERLEAVED_REVIEW,
                new GradeResult(0, 0, selected.size(), List.of()));

        return new ReviewSessionQuestions(selected, session.getId());
    }

    /**
     * Generate daily review queue: 10-20 questions from sub-chunks due for review.
     */
    public ReviewSessionQuestions generateDailyReview(String userId) {
        LearnerPath path = getPath(userId);
        List<UserChunkProgress> dueReviews = spacingService.getDueReviews(userId);

        if (dueReviews.isEmpty()) {
            log.info("[DailyReview] No reviews due for user={}", userId);
            return new ReviewSessionQuestions(List.of());
        }

        List<String> dueSubChunkIds = dueReviews.stream()
                .map(UserChunkProgress::getSubChunkId)
                .toList();

        List<Question> pool = questionRepository.findBySubChunkIdIn(dueSubChunkIds).stream()
                .filter(q -> q.getMinPath().ordinal() <= path.ordinal())
                .collect(Collectors.toList());

        // Weight by weakness
        Map<String, Double> weights = new HashMap<>();
        for (UserChunkProgress p : dueReviews) {
            double decayed = spacingService.computeDecayedStrength(p);
            weights.put(p.getSubChunkId(), 1.0 - decayed);
        }

        int targetCount = Math.min(20, Math.max(10, pool.size()));
        List<Question> selected = weightedSample(pool, weights, targetCount);

        ReviewSession session = retrievalService.saveSession(userId, SessionType.DAILY_REVIEW,
                new GradeResult(0, 0, selected.size(), List.of()));

        log.info("[DailyReview] Generated {} questions from {} due sub-chunks for user={}",
                selected.size(), dueSubChunkIds.size(), userId);

        return new ReviewSessionQuestions(selected, session.getId());
    }

    /**
     * Submit review answers, grade them, update SM-2 for all affected sub-chunks.
     */
    @Transactional
    public ReviewResult submitReview(String userId, String sessionId,
                                     List<AnswerPair> answers) {
        GradeResult graded = retrievalService.gradeAnswers(answers);

        // Group results by sub-chunk and update SM-2 for each
        Map<String, List<QuestionResult>> bySubChunk = graded.results().stream()
                .collect(Collectors.groupingBy(QuestionResult::subChunkId));

        for (var entry : bySubChunk.entrySet()) {
            long correct = entry.getValue().stream().filter(QuestionResult::correct).count();
            double subScore = (double) correct / entry.getValue().size();
            spacingService.updateSpacing(userId, entry.getKey(), subScore);
        }

        List<BadgeDto> newBadges = gamification.evaluateAndAwardBadges(userId);

        log.info("[Review] Submitted | user={} session={} score={} correct={}/{}",
                userId, sessionId, graded.score(), graded.correct(), graded.total());

        return new ReviewResult(graded.score(), graded.correct(), graded.total(),
                graded.results(), newBadges);
    }

    private List<Question> weightedSample(List<Question> pool, Map<String, Double> weights, int count) {
        if (pool.isEmpty()) return List.of();

        Random rng = new Random();
        List<Question> selected = new ArrayList<>();
        List<Question> remaining = new ArrayList<>(pool);

        while (selected.size() < count && !remaining.isEmpty()) {
            double totalWeight = remaining.stream()
                    .mapToDouble(q -> weights.getOrDefault(q.getSubChunkId(), 0.5))
                    .sum();

            double r = rng.nextDouble() * totalWeight;
            double cumulative = 0;

            for (int i = 0; i < remaining.size(); i++) {
                cumulative += weights.getOrDefault(remaining.get(i).getSubChunkId(), 0.5);
                if (cumulative >= r) {
                    selected.add(remaining.remove(i));
                    break;
                }
            }
        }

        Collections.shuffle(selected);
        return selected;
    }

    private LearnerPath getPath(String userId) {
        return profileRepository.findByUserId(userId)
                .map(UserLearnerProfile::getCurrentPath)
                .orElse(LearnerPath.FOUNDATION);
    }

}
