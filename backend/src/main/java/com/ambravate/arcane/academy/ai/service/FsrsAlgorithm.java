package com.ambravate.arcane.academy.ai.service;

import com.ambravate.arcane.academy.ai.domain.FsrsState;

/**
 * Pure FSRS-4.5 scheduling algorithm — no Spring dependencies, fully testable.
 *
 * <p><strong>Key concepts:</strong>
 * <ul>
 *   <li><em>Stability (S)</em> — number of days until retrievability drops to the
 *       target retention (90%). Next scheduled interval = round(S) days.</li>
 *   <li><em>Difficulty (D)</em> — 1–10 scale; higher means harder to remember.
 *       Drives how much stability grows after each successful recall.</li>
 *   <li><em>Retrievability R(t, S)</em> — probability of recall at {@code t} days
 *       after last review:
 *       {@code R = (1 + 19·t / (81·S))^(-1/2)}</li>
 *   <li><em>Rating</em> — 1 Again / 2 Hard / 3 Good / 4 Easy</li>
 * </ul>
 *
 * <p>Default weights are the FSRS-4.5 paper defaults (optimal across all Anki
 * users). Domain-specific fine-tuning can be added later without changing this API.
 *
 * @see <a href="https://github.com/open-spaced-repetition/fsrs4anki">FSRS-4.5 reference</a>
 */
public final class FsrsAlgorithm {

    private FsrsAlgorithm() {}

    // ── FSRS-4.5 default weights ──────────────────────────────────────────────

    /** FSRS-4.5 default weight vector (19 values). */
    static final double[] W = {
        0.4072, 1.1829, 3.1262, 15.4722,   // w[0..3]  initial stability by rating 1–4
        7.2102, 0.5316, 1.0651, 0.0589,    // w[4..7]  difficulty parameters
        1.5330, 0.1544, 1.0070,             // w[8..10] recall-stability multipliers
        1.9395, 0.1100, 0.2900, 2.2700,    // w[11..14] forget-stability parameters
        0.2500, 2.9898,                     // w[15..16] hard penalty, easy bonus
        0.5100, 0.3400                      // w[17..18] (FSRS-5 additions, unused here)
    };

    // ── Power-forgetting-curve constants ─────────────────────────────────────
    //
    //   R(t, S) = (1 + FACTOR × t/S)^DECAY
    //   where FACTOR = 0.9^(1/DECAY) − 1 = 0.9^(−2) − 1 = 19/81 ≈ 0.2346
    //         DECAY  = −0.5

    static final double DECAY  = -0.5;
    static final double FACTOR = Math.pow(0.9, 1.0 / DECAY) - 1.0; // ≈ 19/81

    private static final double TARGET_RETENTION = 0.9;
    private static final int    MAX_INTERVAL      = 36_500; // 100 years

    // ── Public record ─────────────────────────────────────────────────────────

    /**
     * Immutable snapshot of a card's FSRS parameters at a point in time.
     *
     * @param stability    days until retrievability reaches the target retention
     * @param difficulty   card hardness 1.0 (easy) – 10.0 (hard)
     * @param lapses       total number of forgotten reviews
     * @param state        current lifecycle state
     * @param lastInterval last scheduled interval in days
     */
    public record FsrsCard(
        double    stability,
        double    difficulty,
        int       lapses,
        FsrsState state,
        int       lastInterval
    ) {}

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Maps a normalised 0.0–1.0 score to an FSRS rating (1–4).
     *
     * <pre>
     *   [0.00, 0.40) → 1  Again
     *   [0.40, 0.60) → 2  Hard
     *   [0.60, 0.85) → 3  Good
     *   [0.85, 1.00] → 4  Easy
     * </pre>
     */
    public static int toRating(double score) {
        if (score < 0.40) return 1;
        if (score < 0.60) return 2;
        if (score < 0.85) return 3;
        return 4;
    }

    /**
     * Schedules a card after a review session.
     *
     * <p>If {@code card.state() == FsrsState.NEW} the {@code elapsedDays} parameter
     * is ignored and initial stability/difficulty are set from the rating.
     *
     * @param card        current card parameters
     * @param rating      1–4 (Again / Hard / Good / Easy)
     * @param elapsedDays days since the last review (0 for same-day)
     * @return updated card with new stability, difficulty, and scheduled interval
     */
    public static FsrsCard review(FsrsCard card, int rating, int elapsedDays) {
        if (card.state() == FsrsState.NEW) {
            return initCard(rating);
        }

        double r       = retrievability(Math.max(0, elapsedDays), card.stability());
        double newS;
        int    newLapses = card.lapses();
        FsrsState newState;

        if (rating == 1) {
            // Again — stability collapses; lapse recorded
            newS      = Math.max(0.1, stabilityAfterForget(card.difficulty(), card.stability(), r));
            newLapses = card.lapses() + 1;
            newState  = FsrsState.RELEARNING;
        } else {
            // Hard / Good / Easy — stability grows
            newS     = stabilityAfterRecall(card.difficulty(), card.stability(), r, rating);
            newState = FsrsState.REVIEW;
        }

        double newD    = nextDifficulty(card.difficulty(), rating);
        int    newInt  = nextInterval(newS);

        return new FsrsCard(newS, newD, newLapses, newState, newInt);
    }

    /**
     * Current retrievability: probability that the learner still remembers the
     * material {@code elapsedDays} days after last review with stability {@code s}.
     *
     * @return value in [0.0, 1.0]; equals 1.0 at elapsedDays=0 and TARGET_RETENTION at
     *         elapsedDays=round(stability)
     */
    public static double retrievability(double elapsedDays, double stability) {
        if (stability <= 0) return 0.0;
        return Math.pow(1.0 + FACTOR * elapsedDays / stability, DECAY);
    }

    /**
     * Next review interval in days that achieves {@link #TARGET_RETENTION}.
     *
     * <p>Algebraically this simplifies to {@code round(stability)}, since
     * {@code FACTOR = targetRetention^(1/DECAY) − 1}.
     */
    public static int nextInterval(double stability) {
        // t such that R(t, S) = 0.9: t = S × (0.9^(1/DECAY) − 1) / FACTOR = S × 1.0 = S
        double t = stability * (Math.pow(TARGET_RETENTION, 1.0 / DECAY) - 1.0) / FACTOR;
        return (int) Math.min(MAX_INTERVAL, Math.max(1, Math.round(t)));
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /** Build initial card parameters from first-ever rating. */
    private static FsrsCard initCard(int rating) {
        double s = initStability(rating);
        double d = initDifficulty(rating);
        return new FsrsCard(s, d, 0, FsrsState.REVIEW, nextInterval(s));
    }

    /** Initial stability indexed by rating 1–4 → w[0–3]. */
    static double initStability(int rating) {
        return W[rating - 1];
    }

    /**
     * Initial difficulty.
     * {@code D₀(G) = w[4] − e^(w[5]·(G−1)) + 1}, clamped to [1, 10].
     */
    static double initDifficulty(int rating) {
        double d = W[4] - Math.exp(W[5] * (rating - 1)) + 1.0;
        return clampDifficulty(d);
    }

    /**
     * Stability after a successful recall.
     * {@code S' = S · (e^w[8] · (11−D) · S^(−w[9]) · (e^(w[10]·(1−R)) − 1) + 1)
     *              × hardPenalty × easyBonus}
     */
    static double stabilityAfterRecall(double d, double s, double r, int rating) {
        double hardPenalty = (rating == 2) ? W[15] : 1.0;
        double easyBonus   = (rating == 4) ? W[16] : 1.0;
        double base = s * (Math.exp(W[8]) * (11.0 - d) * Math.pow(s, -W[9])
                         * (Math.exp(W[10] * (1.0 - r)) - 1.0) + 1.0);
        return base * hardPenalty * easyBonus;
    }

    /**
     * Stability after forgetting (rating = 1 / Again).
     * {@code S' = w[11] · D^(−w[12]) · ((S+1)^w[13] − 1) · e^(w[14]·(1−R))}
     */
    static double stabilityAfterForget(double d, double s, double r) {
        return W[11] * Math.pow(d, -W[12])
                     * (Math.pow(s + 1.0, W[13]) - 1.0)
                     * Math.exp(W[14] * (1.0 - r));
    }

    /**
     * Difficulty after a review.
     * Delta {@code = −w[6]·(G−3)}, then mean-reversion toward {@code D₀(4)}.
     */
    static double nextDifficulty(double d, int rating) {
        double delta    = -W[6] * (rating - 3.0);
        double reverted = W[7] * initDifficulty(4) + (1.0 - W[7]) * (d + delta);
        return clampDifficulty(reverted);
    }

    private static double clampDifficulty(double d) {
        return Math.min(10.0, Math.max(1.0, d));
    }
}
