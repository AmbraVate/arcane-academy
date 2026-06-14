package com.ambravate.arcane.academy.ai.service;

import com.ambravate.arcane.academy.ai.domain.FsrsState;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.ambravate.arcane.academy.ai.service.FsrsAlgorithm.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Unit tests for {@link FsrsAlgorithm} — FSRS-4.5 scheduling algorithm.
 *
 * <p>Tests pin the mathematical behaviour so future weight changes are deliberate.
 * Expected values are computed from the FSRS-4.5 reference formulas with the
 * default weight vector.
 */
class FsrsAlgorithmTest {

    // ── rating mapping ────────────────────────────────────────────────────────

    @Nested
    class RatingMapping {

        @Test
        void zeroBelowThreshold_isAgain() {
            assertThat(toRating(0.0)).isEqualTo(1);
            assertThat(toRating(0.39)).isEqualTo(1);
        }

        @Test
        void hardBand() {
            assertThat(toRating(0.40)).isEqualTo(2);
            assertThat(toRating(0.59)).isEqualTo(2);
        }

        @Test
        void goodBand() {
            assertThat(toRating(0.60)).isEqualTo(3);
            assertThat(toRating(0.84)).isEqualTo(3);
        }

        @Test
        void easyBand() {
            assertThat(toRating(0.85)).isEqualTo(4);
            assertThat(toRating(1.0)).isEqualTo(4);
        }
    }

    // ── nextInterval (algebraic simplification: interval ≈ stability) ────────

    @Nested
    class NextInterval {

        @Test
        void intervalEqualsStabilityForGoodRating() {
            // S = w[2] = 3.1262 → interval = round(3.1262) = 3
            assertThat(nextInterval(W[2])).isEqualTo(3);
        }

        @Test
        void intervalForEasyRating() {
            // S = w[3] = 15.4722 → interval = 15
            assertThat(nextInterval(W[3])).isEqualTo(15);
        }

        @Test
        void intervalIsMinimumOne() {
            assertThat(nextInterval(0.3)).isEqualTo(1);
            assertThat(nextInterval(0.0)).isEqualTo(1);
        }

        @Test
        void retrievabilityAtScheduledInterval_isNinetyPercent() {
            // By definition: R(nextInterval(S), S) ≈ 0.9
            double s = W[2]; // 3.1262
            int i = nextInterval(s);
            double r = retrievability(i, s);
            assertThat(r).isCloseTo(0.9, within(0.02));
        }
    }

    // ── retrievability ────────────────────────────────────────────────────────

    @Nested
    class Retrievability {

        @Test
        void isOneAtElapsedZero() {
            assertThat(retrievability(0, 5.0)).isCloseTo(1.0, within(0.001));
        }

        @Test
        void decaysOverTime() {
            double r7  = retrievability(7, 5.0);
            double r14 = retrievability(14, 5.0);
            assertThat(r7).isGreaterThan(r14);
            assertThat(r7).isBetween(0.0, 1.0);
        }

        @Test
        void higherStability_decaysSlower() {
            double rLow  = retrievability(7, 3.0);
            double rHigh = retrievability(7, 10.0);
            assertThat(rHigh).isGreaterThan(rLow);
        }

        @Test
        void zeroStability_returnsZero() {
            assertThat(retrievability(5, 0.0)).isEqualTo(0.0);
        }
    }

    // ── newCard (state = NEW, first rating) ───────────────────────────────────

    @Nested
    class NewCard {

        @Test
        void goodRating_setsInitialStability() {
            // w[2] = 3.1262
            FsrsCard card = review(new FsrsCard(0, 0, 0, FsrsState.NEW, 0), 3, 0);
            assertThat(card.stability()).isCloseTo(W[2], within(0.001));
        }

        @Test
        void easyRating_setsHigherInitialStability() {
            FsrsCard card = review(new FsrsCard(0, 0, 0, FsrsState.NEW, 0), 4, 0);
            assertThat(card.stability()).isCloseTo(W[3], within(0.001));  // 15.4722
        }

        @Test
        void againRating_setsLowestInitialStability() {
            FsrsCard card = review(new FsrsCard(0, 0, 0, FsrsState.NEW, 0), 1, 0);
            assertThat(card.stability()).isCloseTo(W[0], within(0.001));  // 0.4072
        }

        @Test
        void newCardTransitionsToReview() {
            FsrsCard card = review(new FsrsCard(0, 0, 0, FsrsState.NEW, 0), 3, 0);
            assertThat(card.state()).isEqualTo(FsrsState.REVIEW);
        }

        @Test
        void newCardHasNoLapses() {
            FsrsCard card = review(new FsrsCard(0, 0, 0, FsrsState.NEW, 0), 1, 0);
            assertThat(card.lapses()).isZero();
        }

        @Test
        void goodDifficulty_isInRange() {
            // D₀(3) = w[4] - exp(w[5]*2) + 1 ≈ 5.31
            FsrsCard card = review(new FsrsCard(0, 0, 0, FsrsState.NEW, 0), 3, 0);
            assertThat(card.difficulty()).isBetween(1.0, 10.0);
            assertThat(card.difficulty()).isCloseTo(5.31, within(0.1));
        }
    }

    // ── review card (state = REVIEW) ─────────────────────────────────────────

    @Nested
    class ReviewCard {

        private static final FsrsCard BASE = new FsrsCard(3.1262, 5.31, 0, FsrsState.REVIEW, 3);

        @Test
        void successfulRecall_increasesStability() {
            // reviewed at scheduled interval (elapsed = lastInterval)
            FsrsCard after = review(BASE, 3, BASE.lastInterval());
            assertThat(after.stability()).isGreaterThan(BASE.stability());
        }

        @Test
        void easyRating_increasesStabilityMoreThanGood() {
            FsrsCard good = review(BASE, 3, BASE.lastInterval());
            FsrsCard easy = review(BASE, 4, BASE.lastInterval());
            assertThat(easy.stability()).isGreaterThan(good.stability());
        }

        @Test
        void hardRating_increasesStabilityLessThanGood() {
            FsrsCard hard = review(BASE, 2, BASE.lastInterval());
            FsrsCard good = review(BASE, 3, BASE.lastInterval());
            assertThat(hard.stability()).isLessThan(good.stability());
        }

        @Test
        void forgottenCard_reducesStabilityAndCountsLapse() {
            FsrsCard after = review(BASE, 1, BASE.lastInterval());
            assertThat(after.stability()).isLessThan(BASE.stability());
            assertThat(after.lapses()).isEqualTo(BASE.lapses() + 1);
            assertThat(after.state()).isEqualTo(FsrsState.RELEARNING);
        }

        @Test
        void successfulReview_staysInReviewState() {
            FsrsCard after = review(BASE, 3, 3);
            assertThat(after.state()).isEqualTo(FsrsState.REVIEW);
        }

        @Test
        void relearningAfterLapse_transitionsBackToReview() {
            FsrsCard lapsed = review(BASE, 1, 3);
            FsrsCard recovered = review(lapsed, 3, 1);
            assertThat(recovered.state()).isEqualTo(FsrsState.REVIEW);
        }
    }

    // ── difficulty evolution ─────────────────────────────────────────────────

    @Nested
    class DifficultyEvolution {

        @Test
        void easyRating_reducesDifficulty() {
            double d0 = initDifficulty(3);
            double d1 = nextDifficulty(d0, 4); // Easy
            assertThat(d1).isLessThan(d0);
        }

        @Test
        void againRating_increasesDifficulty() {
            double d0 = initDifficulty(3);
            double d1 = nextDifficulty(d0, 1); // Again
            assertThat(d1).isGreaterThan(d0);
        }

        @Test
        void difficultyAlwaysInBounds() {
            // extreme repeated Again ratings should not push above 10
            double d = 9.5;
            for (int i = 0; i < 20; i++) d = nextDifficulty(d, 1);
            assertThat(d).isLessThanOrEqualTo(10.0);
            assertThat(d).isGreaterThanOrEqualTo(1.0);
        }
    }
}
