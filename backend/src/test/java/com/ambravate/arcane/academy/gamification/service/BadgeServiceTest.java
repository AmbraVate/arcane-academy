package com.ambravate.arcane.academy.gamification.service;

import com.ambravate.arcane.academy.auth.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.common.domain.BadgeDefinition;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserBadge;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.dto.BadgeDto;
import com.ambravate.arcane.academy.common.telemetry.service.TelemetryService;
import com.ambravate.arcane.academy.content.repository.SubChunkRepository;
import com.ambravate.arcane.academy.gamification.repository.UserBadgeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link BadgeService} — verifies badge enumeration, award conditions,
 * duplicate-award prevention, and chunk-completion detection.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BadgeService")
class BadgeServiceTest {

    @Mock private UserBadgeRepository badgeRepository;
    @Mock private UserRepository userRepository;
    @Mock private SubChunkRepository subChunkRepository;
    @Mock private UserLearnerProfileRepository profileRepository;
    @Mock private StreakService streakService;
    @Mock private TelemetryService telemetry;
    @Mock private JdbcTemplate jdbc;

    @InjectMocks private BadgeService service;

    private static final String USER_ID = "u-test";

    private User user(int totalXp, int streakDays) {
        return User.aUser()
                .withId(USER_ID)
                .withUsername("tester")
                .withEmail("t@test")
                .withTotalXp(totalXp)
                .withStreakDays(streakDays)
                .build();
    }

    private UserChunkProgress completed(String subChunkId) {
        return UserChunkProgress.builder()
                .userId(USER_ID)
                .subChunkId(subChunkId)
                .status(SubChunkStatus.COMPLETE)
                .build();
    }

    private UserBadge earned(String badgeId) {
        return UserBadge.builder()
                .userId(USER_ID)
                .badgeId(badgeId)
                .earnedAt(Instant.now())
                .build();
    }

    @BeforeEach
    void noAlreadyEarnedBadges() {
        // Default: user has no badges yet
        lenient().when(badgeRepository.findByUserId(USER_ID)).thenReturn(List.of());
        lenient().when(badgeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        lenient().when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        lenient().doNothing().when(telemetry).badgeEarned(anyString(), anyString(), anyString());
        // Default: user has no notes saved yet
        lenient().when(jdbc.queryForObject(anyString(), eq(Long.class), anyString())).thenReturn(0L);
    }

    // ── getEarnedBadges / getAllForUser ─────────────────────────────────────────

    @Nested
    @DisplayName("getEarnedBadges()")
    class GetEarnedBadges {

        @Test
        @DisplayName("Returns only earned badges when user has some")
        void returnsEarnedSubset() {
            when(badgeRepository.findByUserId(USER_ID))
                    .thenReturn(List.of(earned(BadgeDefinition.FIRST_CONCEPT.name())));

            List<BadgeDto> result = service.getEarnedBadges(USER_ID);

            assertThat(result).isNotEmpty();
            assertThat(result).allMatch(BadgeDto::isEarned);
            assertThat(result).anyMatch(b -> b.getId().equals("FIRST_CONCEPT"));
        }

        @Test
        @DisplayName("Returns empty list when user has earned no badges")
        void returnsEmptyWhenNoneEarned() {
            when(badgeRepository.findByUserId(USER_ID)).thenReturn(List.of());

            List<BadgeDto> result = service.getEarnedBadges(USER_ID);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("getAllForUser returns every badge definition")
        void allForUserContainsAllDefinitions() {
            when(badgeRepository.findByUserId(USER_ID)).thenReturn(List.of());

            List<BadgeDto> all = service.getAllForUser(USER_ID);

            assertThat(all).hasSize(BadgeDefinition.values().length);
            assertThat(all).noneMatch(BadgeDto::isEarned);
        }

        @Test
        @DisplayName("getAllForUser marks earned badges correctly")
        void marksEarnedCorrectly() {
            when(badgeRepository.findByUserId(USER_ID))
                    .thenReturn(List.of(earned("FIRST_CONCEPT"), earned("XP_100")));

            List<BadgeDto> all = service.getAllForUser(USER_ID);

            long earnedCount = all.stream().filter(BadgeDto::isEarned).count();
            assertThat(earnedCount).isEqualTo(2);
            assertThat(all).filteredOn(BadgeDto::isEarned)
                    .extracting(BadgeDto::getId)
                    .containsExactlyInAnyOrder("FIRST_CONCEPT", "XP_100");
        }
    }

    // ── evaluateAndAward — guard conditions ──────────────────────────────────────

    @Nested
    @DisplayName("evaluateAndAward() — guards")
    class Guards {

        @Test
        @DisplayName("Returns empty list when user is not found")
        void unknownUserReturnsEmpty() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

            List<BadgeDto> result = service.evaluateAndAward(USER_ID, List.of(), List.of());

            assertThat(result).isEmpty();
            verify(badgeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Does not re-award an already-earned badge")
        void skipsAlreadyEarnedBadge() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user(0, 0)));
            when(badgeRepository.findByUserId(USER_ID))
                    .thenReturn(List.of(earned("FIRST_CONCEPT")));
            // One sub-chunk completed — FIRST_CONCEPT would normally trigger
            UserChunkProgress p = completed("sc-1");

            List<BadgeDto> result = service.evaluateAndAward(USER_ID, List.of(p), List.of());

            assertThat(result).noneMatch(b -> b.getId().equals("FIRST_CONCEPT"));
            verify(badgeRepository, never()).save(argThat(b ->
                    ((UserBadge) b).getBadgeId().equals("FIRST_CONCEPT")));
        }
    }

    // ── evaluateAndAward — individual conditions ─────────────────────────────────

    @Nested
    @DisplayName("evaluateAndAward() — conditions")
    class Conditions {

        @Test
        @DisplayName("Awards FIRST_CONCEPT when one sub-chunk is complete")
        void firstConcept() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user(0, 0)));
            UserChunkProgress p = completed("sc-any");

            List<BadgeDto> awarded = service.evaluateAndAward(USER_ID, List.of(p), List.of());

            assertThat(awarded).anyMatch(b -> b.getId().equals("FIRST_CONCEPT"));
            verify(telemetry).badgeEarned(eq(USER_ID), eq("FIRST_CONCEPT"), anyString());
        }

        @Test
        @DisplayName("Awards XP_100 when user totalXp >= 100")
        void xp100() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user(150, 0)));

            List<BadgeDto> awarded = service.evaluateAndAward(USER_ID, List.of(), List.of());

            assertThat(awarded).anyMatch(b -> b.getId().equals("XP_100"));
        }

        @Test
        @DisplayName("Does not award XP_100 when user totalXp < 100")
        void xp100NotMet() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user(50, 0)));

            List<BadgeDto> awarded = service.evaluateAndAward(USER_ID, List.of(), List.of());

            assertThat(awarded).noneMatch(b -> b.getId().equals("XP_100"));
        }

        @Test
        @DisplayName("Awards STREAK_7 when user streakDays >= 7")
        void streak7() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user(0, 7)));

            List<BadgeDto> awarded = service.evaluateAndAward(USER_ID, List.of(), List.of());

            assertThat(awarded).anyMatch(b -> b.getId().equals("STREAK_7"));
        }

        @Test
        @DisplayName("Awards PERFECT_REVIEW when a session has score >= 1.0")
        void perfectReview() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user(0, 0)));

            var session = com.ambravate.arcane.academy.common.domain.ReviewSession.builder()
                    .userId(USER_ID)
                    .score(1.0)
                    .totalQuestions(5)
                    .build();

            List<BadgeDto> awarded = service.evaluateAndAward(USER_ID, List.of(), List.of(session));

            assertThat(awarded).anyMatch(b -> b.getId().equals("PERFECT_REVIEW"));
        }

        @Test
        @DisplayName("Awards FEYNMAN_FIRST when at least one feynman is completed")
        void feynmanFirst() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user(0, 0)));

            UserChunkProgress feynmanDone = UserChunkProgress.builder()
                    .userId(USER_ID)
                    .subChunkId("sc-feynman")
                    .status(SubChunkStatus.COMPLETE)
                    .feynmanCompleted(true)
                    .feynmanScore(0.9)
                    .build();

            List<BadgeDto> awarded = service.evaluateAndAward(USER_ID, List.of(feynmanDone), List.of());

            assertThat(awarded).anyMatch(b -> b.getId().equals("FEYNMAN_FIRST"));
        }

        @Test
        @DisplayName("Awards multiple badges in one evaluation pass")
        void multipleBadgesInOnePass() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user(500, 7)));
            UserChunkProgress p = completed("sc-1");

            List<BadgeDto> awarded = service.evaluateAndAward(USER_ID, List.of(p), List.of());

            // At 500 XP: XP_100, XP_500; streak 7: STREAK_3, STREAK_7; one complete: FIRST_CONCEPT
            assertThat(awarded).extracting(BadgeDto::getId)
                    .contains("FIRST_CONCEPT", "XP_100", "XP_500", "STREAK_3", "STREAK_7");
        }
    }

    // ── getCompletedChunkIds / chunk-completion badge ────────────────────────────

    @Nested
    @DisplayName("Chunk-completion badges")
    class ChunkCompletion {

        @Test
        @DisplayName("Awards JAVA_FND_1_COMPLETE when all sub-chunks of java-fnd-1 are complete")
        void javaFnd1Complete() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user(0, 0)));

            // Two sub-chunks in java-fnd-1
            SubChunk sc1 = SubChunk.builder().id("sc-jf1-a").chunkId("java-fnd-1").sortOrder(1).build();
            SubChunk sc2 = SubChunk.builder().id("sc-jf1-b").chunkId("java-fnd-1").sortOrder(2).build();

            UserChunkProgress p1 = completed("sc-jf1-a");
            UserChunkProgress p2 = completed("sc-jf1-b");

            // findAllById resolves subChunkId → SubChunk (for chunkId mapping)
            when(subChunkRepository.findAllById(anyList())).thenReturn(List.of(sc1, sc2));
            // findByChunkIdIn checks if ALL sub-chunks in the chunk are done
            when(subChunkRepository.findByChunkIdIn(anyList())).thenReturn(List.of(sc1, sc2));

            List<BadgeDto> awarded = service.evaluateAndAward(USER_ID, List.of(p1, p2), List.of());

            assertThat(awarded).anyMatch(b -> b.getId().equals("JAVA_FND_1_COMPLETE"));
        }

        @Test
        @DisplayName("Does NOT award chunk badge when only one of two sub-chunks is complete")
        void partialChunkNotAwarded() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user(0, 0)));

            SubChunk sc1 = SubChunk.builder().id("sc-jf1-a").chunkId("java-fnd-1").sortOrder(1).build();
            SubChunk sc2 = SubChunk.builder().id("sc-jf1-b").chunkId("java-fnd-1").sortOrder(2).build();

            // Only sc1 is complete
            UserChunkProgress p1 = completed("sc-jf1-a");

            when(subChunkRepository.findAllById(anyList())).thenReturn(List.of(sc1));
            // The chunk has 2 sub-chunks total
            when(subChunkRepository.findByChunkIdIn(anyList())).thenReturn(List.of(sc1, sc2));

            List<BadgeDto> awarded = service.evaluateAndAward(USER_ID, List.of(p1), List.of());

            assertThat(awarded).noneMatch(b -> b.getId().equals("JAVA_FND_1_COMPLETE"));
        }
    }

    // ── isStreakAtRisk ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("isStreakAtRisk delegates to StreakService")
    void streakAtRiskDelegates() {
        when(streakService.isStreakAtRisk(USER_ID)).thenReturn(true);
        assertThat(service.isStreakAtRisk(USER_ID)).isTrue();

        when(streakService.isStreakAtRisk(USER_ID)).thenReturn(false);
        assertThat(service.isStreakAtRisk(USER_ID)).isFalse();
    }

    // ── getBadgeCount ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getBadgeCount returns the size of the user's earned badge list")
    void badgeCount() {
        when(badgeRepository.findByUserId(USER_ID))
                .thenReturn(List.of(earned("FIRST_CONCEPT"), earned("XP_100")));
        assertThat(service.getBadgeCount(USER_ID)).isEqualTo(2);
    }
}
