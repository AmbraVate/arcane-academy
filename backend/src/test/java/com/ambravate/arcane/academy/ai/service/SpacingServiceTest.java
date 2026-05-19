package com.ambravate.arcane.academy.ai.service;

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
 * Unit tests for {@link SpacingService} — the SM-2 derived spaced-repetition engine.
 * <p>
 * The scheduler is core to the platform's pedagogical thesis ("reviews are the
 * core loop"). These tests pin its mathematical behaviour so future refactors
 * cannot silently change the schedule learners depend on.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SpacingService — SM-2 scheduler")
class SpacingServiceTest {

    private static final String USER_ID = "user-test";
    private static final String SUB_CHUNK_ID = "java-ch1-variables";

    @Mock
    private UserChunkProgressRepository repo;

    @InjectMocks
    private SpacingService spacing;

    private UserChunkProgress progress;

    @BeforeEach
    void setUp() {
        progress = UserChunkProgress.builder()
                .userId(USER_ID)
                .subChunkId(SUB_CHUNK_ID)
                .easeFactor(2.5)
                .repetitionCount(0)
                .intervalDays(0)
                .memoryStrength(0.0)
                .build();
    }

    // ── updateSpacing — failure path ────────────────────────────────────────────

    @Nested
    @DisplayName("when score is below the 0.6 threshold")
    class FailedReview {

        @Test
        @DisplayName("resets repetitionCount to 0 and interval to 1 day")
        void resetsScheduleOnFailure() {
            progress.setRepetitionCount(5);
            progress.setIntervalDays(30);
            when(repo.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.of(progress));

            spacing.updateSpacing(USER_ID, SUB_CHUNK_ID, 0.4);

            assertThat(progress.getRepetitionCount()).isZero();
            assertThat(progress.getIntervalDays()).isEqualTo(1);
        }

        @Test
        @DisplayName("schedules next review for tomorrow")
        void nextReviewIsTomorrow() {
            when(repo.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.of(progress));

            Instant before = Instant.now();
            spacing.updateSpacing(USER_ID, SUB_CHUNK_ID, 0.0);

            assertThat(progress.getNextReviewAt())
                    .isCloseTo(before.plus(Duration.ofDays(1)), within(5, java.time.temporal.ChronoUnit.SECONDS));
        }
    }

    // ── updateSpacing — success path ────────────────────────────────────────────

    @Nested
    @DisplayName("when score meets or exceeds the 0.6 threshold")
    class SuccessfulReview {

        @Test
        @DisplayName("first successful repetition schedules at 1 day")
        void firstRepetitionInterval() {
            when(repo.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.of(progress));

            spacing.updateSpacing(USER_ID, SUB_CHUNK_ID, 0.8);

            assertThat(progress.getRepetitionCount()).isEqualTo(1);
            assertThat(progress.getIntervalDays()).isEqualTo(1);
        }

        @Test
        @DisplayName("second successful repetition schedules at 3 days")
        void secondRepetitionInterval() {
            progress.setRepetitionCount(1);
            progress.setIntervalDays(1);
            when(repo.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.of(progress));

            spacing.updateSpacing(USER_ID, SUB_CHUNK_ID, 0.8);

            assertThat(progress.getRepetitionCount()).isEqualTo(2);
            assertThat(progress.getIntervalDays()).isEqualTo(3);
        }

        @Test
        @DisplayName("third+ repetition multiplies interval by ease factor")
        void thirdRepetitionUsesEaseFactor() {
            progress.setRepetitionCount(2);
            progress.setIntervalDays(3);
            progress.setEaseFactor(2.5);
            when(repo.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.of(progress));

            spacing.updateSpacing(USER_ID, SUB_CHUNK_ID, 0.8);

            // 3 * 2.5 = 7.5, rounded → 8 (ease factor will also be updated, but
            // the interval calculation uses the pre-update ease factor)
            assertThat(progress.getIntervalDays()).isBetween(7, 8);
        }

        @Test
        @DisplayName("ease factor is floored at 1.3 (SM-2 minimum)")
        void easeFactorMinimum() {
            progress.setEaseFactor(1.4); // already near the floor
            when(repo.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.of(progress));

            // Score of 0.6 maps to q5=3 → EF delta = 0.1 - 2*(0.08+2*0.02) = 0.1 - 0.24 = -0.14
            spacing.updateSpacing(USER_ID, SUB_CHUNK_ID, 0.6);

            assertThat(progress.getEaseFactor()).isGreaterThanOrEqualTo(1.3);
        }

        @Test
        @DisplayName("ease factor increases on perfect score")
        void easeFactorIncreasesOnPerfect() {
            double initialEf = progress.getEaseFactor();
            when(repo.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.of(progress));

            spacing.updateSpacing(USER_ID, SUB_CHUNK_ID, 1.0);

            // Score 1.0 → q5=5 → delta = 0.1 - 0*(...) = +0.1
            assertThat(progress.getEaseFactor()).isGreaterThan(initialEf);
        }
    }

    // ── memory strength update ──────────────────────────────────────────────────

    @Test
    @DisplayName("memory strength is a 70/30 blend of new score and prior strength")
    void memoryStrengthBlend() {
        progress.setMemoryStrength(0.5);
        when(repo.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                .thenReturn(Optional.of(progress));

        spacing.updateSpacing(USER_ID, SUB_CHUNK_ID, 1.0);

        // Expected: 1.0 * 0.7 + 0.5 * 0.3 = 0.85
        assertThat(progress.getMemoryStrength()).isCloseTo(0.85, within(0.001));
    }

    @Test
    @DisplayName("memory strength is clamped to [0, 1]")
    void memoryStrengthClamped() {
        progress.setMemoryStrength(0.99);
        when(repo.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                .thenReturn(Optional.of(progress));

        spacing.updateSpacing(USER_ID, SUB_CHUNK_ID, 1.0);

        assertThat(progress.getMemoryStrength()).isBetween(0.0, 1.0);
    }

    // ── persistence ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("save is called on the repository with the updated progress")
    void persistsUpdate() {
        when(repo.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                .thenReturn(Optional.of(progress));

        spacing.updateSpacing(USER_ID, SUB_CHUNK_ID, 0.8);

        ArgumentCaptor<UserChunkProgress> captor = ArgumentCaptor.forClass(UserChunkProgress.class);
        verify(repo).save(captor.capture());
        assertThat(captor.getValue().getLastScore()).isEqualTo(0.8);
        assertThat(captor.getValue().getLastReviewedAt()).isNotNull();
    }

    @Test
    @DisplayName("throws IllegalStateException when no progress row exists")
    void throwsWhenMissing() {
        when(repo.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> spacing.updateSpacing(USER_ID, SUB_CHUNK_ID, 0.8))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(SUB_CHUNK_ID);
    }

    // ── computeDecayedStrength ──────────────────────────────────────────────────

    @Nested
    @DisplayName("computeDecayedStrength")
    class DecayedStrength {

        @Test
        @DisplayName("returns 0 when never reviewed (lastReviewedAt is null)")
        void zeroWhenNeverReviewed() {
            progress.setLastReviewedAt(null);
            progress.setMemoryStrength(0.9);

            assertThat(spacing.computeDecayedStrength(progress)).isZero();
        }

        @Test
        @DisplayName("returns near-original strength when reviewed seconds ago")
        void nearOriginalWhenJustReviewed() {
            progress.setLastReviewedAt(Instant.now());
            progress.setMemoryStrength(0.8);
            progress.setEaseFactor(2.5);

            assertThat(spacing.computeDecayedStrength(progress)).isCloseTo(0.8, within(0.01));
        }

        @Test
        @DisplayName("decays exponentially over time")
        void decaysOverTime() {
            // 7 days ago, EF 2.5 → decayRate=0.02/hour → over 168h: exp(-0.02*168) ≈ 0.0347
            progress.setLastReviewedAt(Instant.now().minus(Duration.ofDays(7)));
            progress.setMemoryStrength(0.9);
            progress.setEaseFactor(2.5);

            double decayed = spacing.computeDecayedStrength(progress);

            assertThat(decayed).isLessThan(0.9);
            assertThat(decayed).isBetween(0.0, 1.0);
        }

        @Test
        @DisplayName("higher ease factor produces slower decay")
        void higherEfDecaysSlower() {
            Instant weekAgo = Instant.now().minus(Duration.ofDays(7));
            progress.setLastReviewedAt(weekAgo);
            progress.setMemoryStrength(0.9);

            progress.setEaseFactor(1.3);
            double weakDecay = spacing.computeDecayedStrength(progress);

            progress.setEaseFactor(3.0);
            double strongDecay = spacing.computeDecayedStrength(progress);

            assertThat(strongDecay).isGreaterThan(weakDecay);
        }
    }

    // ── due reviews query ───────────────────────────────────────────────────────

    @Test
    @DisplayName("getDueReviews delegates to repository with current time")
    void getDueReviewsDelegates() {
        when(repo.findByUserIdAndNextReviewAtBefore(any(String.class), any(Instant.class)))
                .thenReturn(java.util.List.of(progress));

        var result = spacing.getDueReviews(USER_ID);

        assertThat(result).hasSize(1).containsExactly(progress);
    }
}
