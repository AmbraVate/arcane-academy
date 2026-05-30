package com.ambravate.arcane.academy.ai.service;

import com.ambravate.arcane.academy.ai.domain.FsrsState;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Spaced-repetition scheduling service — Phase 5 upgrade to FSRS-4.5.
 *
 * <p>Public signatures ({@link #updateSpacing}, {@link #getDueReviews},
 * {@link #computeDecayedStrength}) are unchanged from the SM-2 era so that all
 * callers ({@code RetrievalService}, review controllers) require zero modification.
 *
 * <p><strong>FSRS crash-course:</strong>
 * <ul>
 *   <li><em>Stability (S)</em> — the scheduled interval is {@code round(S)} days.
 *       Grows after each successful recall; collapses on a lapse.</li>
 *   <li><em>Difficulty (D)</em> — converges toward the card's natural hardness over
 *       multiple reviews.</li>
 *   <li><em>Retrievability R(t,S)</em> — probability of recall at time {@code t}:
 *       {@code R = (1 + 19t/(81S))^{-1/2}}</li>
 * </ul>
 *
 * <p>Rows with {@code fsrsStability = 0} are treated as NEW cards; their first
 * rating seeds initial stability and difficulty from the FSRS defaults.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SpacingService {

    private final UserChunkProgressRepository progressRepository;

    // ── updateSpacing ─────────────────────────────────────────────────────────

    /**
     * Run FSRS scheduling after a retrieval review.
     *
     * @param score 0.0–1.0 performance score
     *              (mapped to FSRS ratings: &lt;0.4 Again, 0.4 Hard, 0.6 Good, 0.85 Easy)
     */
    @Transactional
    public void updateSpacing(String userId, String lessonId, double score) {
        UserChunkProgress progress = progressRepository
                .findByUserIdAndLessonId(userId, lessonId)
                .orElseThrow(() -> new IllegalStateException("No progress found for " + lessonId));

        // ── Build current FSRS card from persisted state ──────────────────────
        int elapsedDays = 0;
        if (progress.getLastReviewedAt() != null) {
            elapsedDays = (int) Duration.between(progress.getLastReviewedAt(), Instant.now()).toDays();
        }

        FsrsAlgorithm.FsrsCard currentCard = loadCard(progress);
        int rating = FsrsAlgorithm.toRating(score);

        // Retrieve current R (before this review) for memory-strength display
        double retrievabilityBeforeReview = (currentCard.state() != FsrsState.NEW && currentCard.stability() > 0)
                ? FsrsAlgorithm.retrievability(elapsedDays, currentCard.stability())
                : score; // first review — use raw score as proxy

        // ── Schedule via FSRS ─────────────────────────────────────────────────
        FsrsAlgorithm.FsrsCard newCard = FsrsAlgorithm.review(currentCard, rating, elapsedDays);

        log.info("[FSRS] Scheduled | user={} lesson={} rating={} stability={}->{}  difficulty={} interval={}d lapses={}",
                userId, lessonId, rating,
                String.format("%.2f", currentCard.stability()),
                String.format("%.2f", newCard.stability()),
                String.format("%.2f", newCard.difficulty()),
                newCard.lastInterval(), newCard.lapses());

        // ── Persist FSRS fields ───────────────────────────────────────────────
        progress.setFsrsStability(newCard.stability());
        progress.setFsrsDifficulty(newCard.difficulty());
        progress.setFsrsLapses(newCard.lapses());
        progress.setFsrsState(newCard.state().name());
        progress.setFsrsLastInterval(newCard.lastInterval());

        // Keep legacy SM-2 field in sync (used by frontend "review in N days" display)
        progress.setIntervalDays(newCard.lastInterval());
        progress.setRepetitionCount(progress.getRepetitionCount() + 1);

        // Memory strength = retrievability immediately before this review
        progress.setMemoryStrength(Math.min(1.0, Math.max(0.0, retrievabilityBeforeReview)));
        progress.setLastScore(score);
        progress.setLastReviewedAt(Instant.now());
        progress.setNextReviewAt(Instant.now().plus(Duration.ofDays(newCard.lastInterval())));

        progressRepository.save(progress);
    }

    // ── getDueReviews ─────────────────────────────────────────────────────────

    /**
     * Cards where {@code nextReviewAt <= now}.
     */
    public List<UserChunkProgress> getDueReviews(String userId) {
        return progressRepository.findByUserIdAndNextReviewAtBefore(userId, Instant.now());
    }

    // ── computeDecayedStrength ────────────────────────────────────────────────

    /**
     * Current retrievability — probability the learner still remembers the
     * material right now, accounting for elapsed time since the last review.
     *
     * <p>Uses the FSRS power-forgetting curve rather than the old SM-2
     * exponential proxy.  Returns 0.0 when the card has never been reviewed
     * or lacks FSRS stability data.
     */
    public double computeDecayedStrength(UserChunkProgress progress) {
        if (progress.getLastReviewedAt() == null || progress.getFsrsStability() <= 0) {
            return 0.0;
        }
        double daysSince = Duration.between(progress.getLastReviewedAt(), Instant.now()).toHours() / 24.0;
        return FsrsAlgorithm.retrievability(daysSince, progress.getFsrsStability());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Reconstruct a {@link FsrsAlgorithm.FsrsCard} from persisted entity fields. */
    private static FsrsAlgorithm.FsrsCard loadCard(UserChunkProgress p) {
        if (p.getFsrsStability() <= 0.0) {
            // NEW card — no FSRS state established yet
            return new FsrsAlgorithm.FsrsCard(0.0, 0.0, 0, FsrsState.NEW, 0);
        }
        FsrsState state = FsrsState.REVIEW; // safe default
        if (p.getFsrsState() != null) {
            try { state = FsrsState.valueOf(p.getFsrsState()); } catch (IllegalArgumentException ignored) {}
        }
        return new FsrsAlgorithm.FsrsCard(
                p.getFsrsStability(),
                p.getFsrsDifficulty(),
                p.getFsrsLapses(),
                state,
                p.getFsrsLastInterval()
        );
    }
}
