package com.ambravate.arcane.academy.ai.service;

import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.repository.UserChunkProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpacingService {

    private final UserChunkProgressRepository progressRepository;

    /**
     * Core SM-2 algorithm update.
     * @param score 0.0 to 1.0 — the learner's score on retrieval/review
     */
    @Transactional
    public void updateSpacing(String userId, String subChunkId, double score) {
        UserChunkProgress progress = progressRepository
                .findByUserIdAndSubChunkId(userId, subChunkId)
                .orElseThrow(() -> new IllegalStateException("No progress found for " + subChunkId));

        double q5 = score * 5.0; // Map 0-1 to SM-2's 0-5 scale

        if (score < 0.6) {
            // Failed review — reset interval
            progress.setRepetitionCount(0);
            progress.setIntervalDays(1);
            log.info("[SM-2] Reset | user={} subChunk={} score={}", userId, subChunkId, score);
        } else {
            int rep = progress.getRepetitionCount() + 1;
            progress.setRepetitionCount(rep);

            if (rep == 1) {
                progress.setIntervalDays(1);
            } else if (rep == 2) {
                progress.setIntervalDays(3);
            } else {
                progress.setIntervalDays((int) Math.round(progress.getIntervalDays() * progress.getEaseFactor()));
            }

            // Update ease factor (SM-2 formula)
            double ef = progress.getEaseFactor() + (0.1 - (5.0 - q5) * (0.08 + (5.0 - q5) * 0.02));
            progress.setEaseFactor(Math.max(1.3, ef));

            log.info("[SM-2] Updated | user={} subChunk={} score={} interval={}d ef={}",
                    userId, subChunkId, score, progress.getIntervalDays(), progress.getEaseFactor());
        }

        // Update memory strength: blend of new score and previous strength
        double newStrength = Math.min(1.0, score * 0.7 + progress.getMemoryStrength() * 0.3);
        progress.setMemoryStrength(newStrength);
        progress.setLastScore(score);
        progress.setLastReviewedAt(Instant.now());
        progress.setNextReviewAt(Instant.now().plus(Duration.ofDays(progress.getIntervalDays())));

        progressRepository.save(progress);
    }

    /**
     * Get sub-chunks due for review (nextReviewAt <= now).
     */
    public List<UserChunkProgress> getDueReviews(String userId) {
        return progressRepository.findByUserIdAndNextReviewAtBefore(userId, Instant.now());
    }

    /**
     * Compute the current memory strength with exponential decay applied.
     * The stored memoryStrength is the post-review value; this applies time-based decay.
     */
    public double computeDecayedStrength(UserChunkProgress progress) {
        if (progress.getLastReviewedAt() == null) return 0.0;

        double hoursSince = Duration.between(progress.getLastReviewedAt(), Instant.now()).toHours();
        double decayRate = 0.05 / progress.getEaseFactor();
        double decayed = progress.getMemoryStrength() * Math.exp(-decayRate * hoursSince);
        return Math.max(0.0, Math.min(1.0, decayed));
    }
}
