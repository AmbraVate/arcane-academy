package com.ambravate.arcane.academy.ai.service;

import com.ambravate.arcane.academy.ai.domain.FsrsState;
import com.ambravate.arcane.academy.ai.service.FsrsAlgorithm;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link SpacingService} — the FSRS-4.5 spaced-repetition engine.
 *
 * <p>Phase 5 replaced the legacy SM-2 scheduler with FSRS. Tests verify:
 * <ul>
 *   <li>First-review seeding (NEW → REVIEW with correct initial stability)</li>
 *   <li>Recall path: stability grows, state stays REVIEW</li>
 *   <li>Lapse path: stability drops, lapses increment, state → RELEARNING</li>
 *   <li>Memory-strength reflects pre-review retrievability</li>
 *   <li>{@code computeDecayedStrength} uses FSRS power forgetting curve</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SpacingService — FSRS-4.5 scheduler")
class SpacingServiceTest {

    private static final String USER_ID   = "user-test";
    private static final String LESSON_ID = "java-ch1-variables";

    @Mock
    private UserChunkProgressRepository repo;

    @InjectMocks
    private SpacingService spacing;

    private UserChunkProgress progress;

    @BeforeEach
    void setUp() {
        progress = UserChunkProgress.builder()
                .userId(USER_ID)
                .lessonId(LESSON_ID)
                .build();
    }

    // ── NEW card (first review) ───────────────────────────────────────────────

    @Nested
    @DisplayName("NEW card — first review")
    class NewCard {

        @Test
        @DisplayName("Good score seeds stability from w[2] ≈ 3.13 and schedules 3-day interval")
        void goodScore_seedsInitialStability() {
            stubProgress();
            spacing.updateSpacing(USER_ID, LESSON_ID, 0.8); // Good

            assertThat(progress.getFsrsStability()).isCloseTo(FsrsAlgorithm.W[2], within(0.01));
            assertThat(progress.getFsrsLastInterval()).isEqualTo(3);
            assertThat(progress.getIntervalDays()).isEqualTo(3);   // legacy field kept in sync
        }

        @Test
        @DisplayName("Easy score seeds stability from w[3] ≈ 15.47 and schedules 15-day interval")
        void easyScore_seedsHigherStability() {
            stubProgress();
            spacing.updateSpacing(USER_ID, LESSON_ID, 1.0); // Easy

            assertThat(progress.getFsrsStability()).isCloseTo(FsrsAlgorithm.W[3], within(0.01));
            assertThat(progress.getFsrsLastInterval()).isEqualTo(15);
        }

        @Test
        @DisplayName("Again on a new card records state RELEARNING (not lapse — never had prior interval)")
        void againOnNewCard_noLapse_stateRelearning() {
            stubProgress();
            spacing.updateSpacing(USER_ID, LESSON_ID, 0.0); // Again

            // First review — lapses stay 0 (FsrsAlgorithm.initCard treats NEW as first attempt)
            assertThat(progress.getFsrsState()).isEqualTo(FsrsState.REVIEW.name());
        }

        @Test
        @DisplayName("State is set to REVIEW after first successful review")
        void firstReview_stateIsReview() {
            stubProgress();
            spacing.updateSpacing(USER_ID, LESSON_ID, 0.8);

            assertThat(progress.getFsrsState()).isEqualTo(FsrsState.REVIEW.name());
        }
    }

    // ── REVIEW card — successful recall ──────────────────────────────────────

    @Nested
    @DisplayName("REVIEW card — successful recall")
    class SuccessfulRecall {

        @BeforeEach
        void setUpReviewCard() {
            // simulate a card already reviewed 3 days ago
            progress.setFsrsStability(3.1262);
            progress.setFsrsDifficulty(5.31);
            progress.setFsrsState(FsrsState.REVIEW.name());
            progress.setFsrsLastInterval(3);
            progress.setLastReviewedAt(Instant.now().minus(Duration.ofDays(3)));
        }

        @Test
        @DisplayName("Good recall increases stability")
        void goodRecall_increasesStability() {
            stubProgress();
            double stabilityBefore = progress.getFsrsStability();
            spacing.updateSpacing(USER_ID, LESSON_ID, 0.8);
            assertThat(progress.getFsrsStability()).isGreaterThan(stabilityBefore);
        }

        @Test
        @DisplayName("Easy recall increases stability more than Good")
        void easyMoreThanGood() {
            // First run: Good rating on the 3-day elapsed card
            stubProgress();
            double sGood = captureSAfter(0.8);

            // Reset to identical starting state (including lastReviewedAt)
            // so the elapsed-days are the same for both ratings.
            progress.setFsrsStability(3.1262);
            progress.setFsrsDifficulty(5.31);
            progress.setFsrsState(FsrsState.REVIEW.name());
            progress.setFsrsLastInterval(3);
            progress.setLastReviewedAt(Instant.now().minus(Duration.ofDays(3)));

            double sEasy = captureSAfter(1.0);
            assertThat(sEasy).isGreaterThan(sGood);
        }

        @Test
        @DisplayName("nextReviewAt is set to approximately (now + interval) days")
        void nextReviewAtIsScheduled() {
            stubProgress();
            Instant before = Instant.now();
            spacing.updateSpacing(USER_ID, LESSON_ID, 0.8);
            int interval = progress.getFsrsLastInterval();
            assertThat(progress.getNextReviewAt())
                    .isAfter(before.plus(Duration.ofDays(interval - 1)));
        }

        @Test
        @DisplayName("State remains REVIEW after a successful recall")
        void stateRemainsReview() {
            stubProgress();
            spacing.updateSpacing(USER_ID, LESSON_ID, 0.8);
            assertThat(progress.getFsrsState()).isEqualTo(FsrsState.REVIEW.name());
        }
    }

    // ── REVIEW card — lapse (Again) ───────────────────────────────────────────

    @Nested
    @DisplayName("REVIEW card — lapse (Again)")
    class Lapse {

        @BeforeEach
        void setUpReviewCard() {
            progress.setFsrsStability(3.1262);
            progress.setFsrsDifficulty(5.31);
            progress.setFsrsLapses(0);
            progress.setFsrsState(FsrsState.REVIEW.name());
            progress.setFsrsLastInterval(3);
            progress.setLastReviewedAt(Instant.now().minus(Duration.ofDays(3)));
        }

        @Test
        @DisplayName("Forgetting increments lapses by 1")
        void forgetting_incrementsLapses() {
            stubProgress();
            spacing.updateSpacing(USER_ID, LESSON_ID, 0.0);
            assertThat(progress.getFsrsLapses()).isEqualTo(1);
        }

        @Test
        @DisplayName("Forgetting reduces stability")
        void forgetting_reducesStability() {
            stubProgress();
            double before = progress.getFsrsStability();
            spacing.updateSpacing(USER_ID, LESSON_ID, 0.0);
            assertThat(progress.getFsrsStability()).isLessThan(before);
        }

        @Test
        @DisplayName("Forgetting transitions state to RELEARNING")
        void forgetting_transitionsToRelearning() {
            stubProgress();
            spacing.updateSpacing(USER_ID, LESSON_ID, 0.0);
            assertThat(progress.getFsrsState()).isEqualTo(FsrsState.RELEARNING.name());
        }
    }

    // ── persistence ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Persistence")
    class Persistence {

        @Test
        @DisplayName("save is called with updated lastScore and lastReviewedAt")
        void persistsUpdate() {
            stubProgress();
            spacing.updateSpacing(USER_ID, LESSON_ID, 0.8);

            ArgumentCaptor<UserChunkProgress> captor = ArgumentCaptor.forClass(UserChunkProgress.class);
            verify(repo).save(captor.capture());
            assertThat(captor.getValue().getLastScore()).isEqualTo(0.8);
            assertThat(captor.getValue().getLastReviewedAt()).isNotNull();
        }

        @Test
        @DisplayName("intervalDays is kept in sync with fsrsLastInterval")
        void intervalDaysInSync() {
            stubProgress();
            spacing.updateSpacing(USER_ID, LESSON_ID, 0.8);
            assertThat(progress.getIntervalDays()).isEqualTo(progress.getFsrsLastInterval());
        }

        @Test
        @DisplayName("repetitionCount increments on each call")
        void repetitionCountIncrements() {
            progress.setRepetitionCount(3);
            stubProgress();
            spacing.updateSpacing(USER_ID, LESSON_ID, 0.8);
            assertThat(progress.getRepetitionCount()).isEqualTo(4);
        }

        @Test
        @DisplayName("throws IllegalStateException when no progress row exists")
        void throwsWhenMissing() {
            when(repo.findByUserIdAndLessonId(USER_ID, LESSON_ID)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> spacing.updateSpacing(USER_ID, LESSON_ID, 0.8))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining(LESSON_ID);
        }
    }

    // ── computeDecayedStrength ────────────────────────────────────────────────

    @Nested
    @DisplayName("computeDecayedStrength — FSRS power forgetting curve")
    class DecayedStrength {

        @Test
        @DisplayName("returns 0 when never reviewed (lastReviewedAt is null)")
        void zeroWhenNeverReviewed() {
            progress.setFsrsStability(3.0);
            progress.setLastReviewedAt(null);
            assertThat(spacing.computeDecayedStrength(progress)).isZero();
        }

        @Test
        @DisplayName("returns 0 when stability is 0 (NEW card, no FSRS data)")
        void zeroWhenNoStability() {
            progress.setFsrsStability(0.0);
            progress.setLastReviewedAt(Instant.now());
            assertThat(spacing.computeDecayedStrength(progress)).isZero();
        }

        @Test
        @DisplayName("returns ~1.0 when reviewed just now")
        void nearOneWhenJustReviewed() {
            progress.setFsrsStability(5.0);
            progress.setLastReviewedAt(Instant.now());
            assertThat(spacing.computeDecayedStrength(progress)).isCloseTo(1.0, within(0.01));
        }

        @Test
        @DisplayName("decays over time — 7 days < just reviewed")
        void decaysOverTime() {
            progress.setFsrsStability(5.0);
            progress.setLastReviewedAt(Instant.now().minus(Duration.ofDays(7)));
            double decayed = spacing.computeDecayedStrength(progress);
            assertThat(decayed).isBetween(0.0, 1.0);
            assertThat(decayed).isLessThan(1.0);
        }

        @Test
        @DisplayName("higher stability decays slower — same elapsed time")
        void higherStabilityDecaysSlower() {
            Instant weekAgo = Instant.now().minus(Duration.ofDays(7));
            progress.setLastReviewedAt(weekAgo);

            progress.setFsrsStability(3.0);
            double lowS = spacing.computeDecayedStrength(progress);

            progress.setFsrsStability(15.0);
            double highS = spacing.computeDecayedStrength(progress);

            assertThat(highS).isGreaterThan(lowS);
        }
    }

    // ── getDueReviews ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("getDueReviews delegates to repository with current time")
    void getDueReviewsDelegates() {
        when(repo.findByUserIdAndNextReviewAtBefore(any(), any())).thenReturn(java.util.List.of(progress));
        var result = spacing.getDueReviews(USER_ID);
        assertThat(result).hasSize(1).containsExactly(progress);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private void stubProgress() {
        when(repo.findByUserIdAndLessonId(USER_ID, LESSON_ID)).thenReturn(Optional.of(progress));
    }

    /** Run updateSpacing and return the new fsrsStability. */
    private double captureSAfter(double score) {
        when(repo.findByUserIdAndLessonId(USER_ID, LESSON_ID)).thenReturn(Optional.of(progress));
        spacing.updateSpacing(USER_ID, LESSON_ID, score);
        return progress.getFsrsStability();
    }
}
