package com.ambravate.arcane.academy.service;

import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.repository.ChunkRepository;
import com.ambravate.arcane.academy.common.repository.SubChunkRepository;
import com.ambravate.arcane.academy.common.repository.UserBadgeRepository;
import com.ambravate.arcane.academy.common.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.common.repository.UserRepository;
import com.ambravate.arcane.academy.gamification.service.LeaderboardService;
import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link LeaderboardService} — verifies that:
 * <ul>
 *   <li>Only opted-in users appear on any board</li>
 *   <li>Only COMPLETE progress counts toward XP</li>
 *   <li>Weekly windows respect the ISO-week boundary</li>
 *   <li>Topic boards never bleed XP from other topics</li>
 *   <li>Ranks are 1-indexed and contiguous</li>
 *   <li>Polymath board sorts by topic count, ties broken by total XP</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LeaderboardService")
class LeaderboardServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserChunkProgressRepository progressRepository;
    @Mock private SubChunkRepository subChunkRepository;
    @Mock private ChunkRepository chunkRepository;
    @Mock private UserBadgeRepository badgeRepository;

    @InjectMocks private LeaderboardService leaderboardService;

    // ── Fixture builders ────────────────────────────────────────────────────────

    private User user(String id, String username, int totalXp, int streak, boolean publicProfile) {
        User u = User.aUser()
            .withId(id).withUsername(username).withEmail(username + "@test")
            .withTotalXp(totalXp).withStreakDays(streak).withPublicProfileEnabled(publicProfile)
            .withRank("Apprentice")
            .build();
        return u;
    }

    private Chunk chunk(String id, String topicId) {
        return Chunk.builder().id(id).title("c").topicId(topicId).sortOrder(1).build();
    }

    private SubChunk sub(String id, String chunkId, int xpReward) {
        return SubChunk.builder().id(id).chunkId(chunkId).title("s").sortOrder(1).xpReward(xpReward).build();
    }

    private UserChunkProgress progress(String userId, String subChunkId, SubChunkStatus status, Instant completedAt) {
        return UserChunkProgress.builder()
            .userId(userId).subChunkId(subChunkId).status(status).completedAt(completedAt).build();
    }

    @BeforeEach
    void wireDefaults() {
        // Default to no badges so badgeCount() returns 0 unless overridden by a test
        lenient().when(badgeRepository.findByUserId(anyString())).thenReturn(List.of());
    }

    // ── Topic weekly ────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("topicWeekly")
    class TopicWeekly {

        @Test
        @DisplayName("Returns empty list when nobody is opted in")
        void emptyWhenNoneOptedIn() {
            when(userRepository.findByPublicProfileEnabledTrue()).thenReturn(List.of());
            when(chunkRepository.findByTopicIdOrderBySortOrderAsc("java")).thenReturn(List.of(chunk("c1", "java")));
            when(subChunkRepository.findByChunkIdIn(any())).thenReturn(List.of(sub("s1", "c1", 50)));

            assertThat(leaderboardService.topicWeekly("java", 20)).isEmpty();
        }

        @Test
        @DisplayName("Excludes opted-out users even with completed XP")
        void excludesOptedOut() {
            User alice = user("u-a", "alice", 100, 0, true);
            User bob   = user("u-b", "bob",   500, 0, false);  // not opted in

            when(userRepository.findByPublicProfileEnabledTrue()).thenReturn(List.of(alice));
            stubTopicJava(50);
            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s1", SubChunkStatus.COMPLETE, Instant.now()),
                progress("u-b", "s1", SubChunkStatus.COMPLETE, Instant.now())
            ));

            var board = leaderboardService.topicWeekly("java", 20);
            assertThat(board).hasSize(1);
            assertThat(board.get(0).username()).isEqualTo("alice");
        }

        @Test
        @DisplayName("Ignores IN_PROGRESS / NOT_STARTED rows")
        void onlyCompleteCounts() {
            User alice = user("u-a", "alice", 0, 0, true);
            when(userRepository.findByPublicProfileEnabledTrue()).thenReturn(List.of(alice));
            stubTopicJava(50);
            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s1", SubChunkStatus.IN_PROGRESS, Instant.now()),
                progress("u-a", "s1", SubChunkStatus.SKIPPED,     Instant.now())  // skip ≠ earn XP
            ));

            assertThat(leaderboardService.topicWeekly("java", 20)).isEmpty();
        }

        @Test
        @DisplayName("Ignores completions older than the current ISO-week start")
        void ignoresOldCompletions() {
            User alice = user("u-a", "alice", 0, 0, true);
            when(userRepository.findByPublicProfileEnabledTrue()).thenReturn(List.of(alice));
            stubTopicJava(50);

            Instant weekStart = LeaderboardService.currentWeekStart();
            Instant lastWeek = weekStart.minus(Duration.ofDays(2));
            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s1", SubChunkStatus.COMPLETE, lastWeek)
            ));

            assertThat(leaderboardService.topicWeekly("java", 20)).isEmpty();
        }

        @Test
        @DisplayName("Excludes XP from other topics' sub-chunks")
        void otherTopicsExcluded() {
            User alice = user("u-a", "alice", 0, 0, true);
            when(userRepository.findByPublicProfileEnabledTrue()).thenReturn(List.of(alice));

            // Topic = java has only s-java; tailwind has s-tailwind
            when(chunkRepository.findByTopicIdOrderBySortOrderAsc("java"))
                .thenReturn(List.of(chunk("c-java", "java")));
            when(subChunkRepository.findByChunkIdIn(any())).thenReturn(List.of(sub("s-java", "c-java", 100)));
            when(subChunkRepository.findAll()).thenReturn(List.of(
                sub("s-java", "c-java", 100),
                sub("s-tailwind", "c-tailwind", 999)
            ));

            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s-tailwind", SubChunkStatus.COMPLETE, Instant.now())
            ));

            assertThat(leaderboardService.topicWeekly("java", 20)).isEmpty();
        }

        @Test
        @DisplayName("Sums XP across multiple completions and orders descending")
        void sumsAndOrders() {
            User alice = user("u-a", "alice", 0, 0, true);
            User bob   = user("u-b", "bob",   0, 0, true);
            when(userRepository.findByPublicProfileEnabledTrue()).thenReturn(List.of(alice, bob));
            stubTopicJava(50);
            when(subChunkRepository.findAll()).thenReturn(List.of(
                sub("s1", "c1", 50),
                sub("s2", "c1", 75)
            ));
            when(subChunkRepository.findByChunkIdIn(any())).thenReturn(List.of(
                sub("s1", "c1", 50),
                sub("s2", "c1", 75)
            ));

            Instant now = Instant.now();
            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s1", SubChunkStatus.COMPLETE, now),
                progress("u-a", "s2", SubChunkStatus.COMPLETE, now),  // alice = 125
                progress("u-b", "s1", SubChunkStatus.COMPLETE, now)   // bob   =  50
            ));

            var board = leaderboardService.topicWeekly("java", 20);
            assertThat(board).hasSize(2);
            assertThat(board.get(0).username()).isEqualTo("alice");
            assertThat(board.get(0).xpEarned()).isEqualTo(125);
            assertThat(board.get(0).rank()).isEqualTo(1);
            assertThat(board.get(1).username()).isEqualTo("bob");
            assertThat(board.get(1).rank()).isEqualTo(2);
        }

        @Test
        @DisplayName("Limit caps the number of returned rows")
        void limitWorks() {
            User alice = user("u-a", "alice", 0, 0, true);
            User bob   = user("u-b", "bob",   0, 0, true);
            User cara  = user("u-c", "cara",  0, 0, true);
            when(userRepository.findByPublicProfileEnabledTrue()).thenReturn(List.of(alice, bob, cara));
            stubTopicJava(50);

            Instant now = Instant.now();
            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s1", SubChunkStatus.COMPLETE, now),
                progress("u-b", "s1", SubChunkStatus.COMPLETE, now),
                progress("u-c", "s1", SubChunkStatus.COMPLETE, now)
            ));

            assertThat(leaderboardService.topicWeekly("java", 1)).hasSize(1);
        }
    }

    // ── Topic all-time ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("topicAllTime")
    class TopicAllTime {

        @Test
        @DisplayName("Counts completions older than this week (no week filter)")
        void includesAncientCompletions() {
            User alice = user("u-a", "alice", 0, 0, true);
            when(userRepository.findByPublicProfileEnabledTrue()).thenReturn(List.of(alice));
            stubTopicJava(50);

            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s1", SubChunkStatus.COMPLETE,
                    Instant.now().minus(Duration.ofDays(120)))
            ));

            var board = leaderboardService.topicAllTime("java", 20);
            assertThat(board).hasSize(1);
            assertThat(board.get(0).xpEarned()).isEqualTo(50);
        }
    }

    // ── Polymath board ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("polymath")
    class Polymath {

        @Test
        @DisplayName("Sorts by distinct topic count, breaks ties on total XP")
        void breadthThenDepth() {
            User alice = user("u-a", "alice", 0, 0, true);  // 2 topics, 100 xp
            User bob   = user("u-b", "bob",   0, 0, true);  // 2 topics, 200 xp (tie-break wins)
            User cara  = user("u-c", "cara",  0, 0, true);  // 1 topic, 999 xp (loses on breadth)

            when(userRepository.findByPublicProfileEnabledTrue()).thenReturn(List.of(alice, bob, cara));

            when(chunkRepository.findAll()).thenReturn(List.of(
                chunk("c-java",     "java"),
                chunk("c-tailwind", "tailwind"),
                chunk("c-react",    "react")
            ));
            when(subChunkRepository.findAll()).thenReturn(List.of(
                sub("s-java",     "c-java",     50),
                sub("s-tailwind", "c-tailwind", 50),
                sub("s-react",    "c-react",    999)
            ));

            Instant now = Instant.now();
            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s-java",     SubChunkStatus.COMPLETE, now),
                progress("u-a", "s-tailwind", SubChunkStatus.COMPLETE, now),  // alice: 2 topics, 100xp
                progress("u-b", "s-java",     SubChunkStatus.COMPLETE, now),
                progress("u-b", "s-tailwind", SubChunkStatus.COMPLETE, now),
                progress("u-b", "s-tailwind", SubChunkStatus.COMPLETE, now),  // duplicate row → bob: 2 topics, 150xp
                progress("u-c", "s-react",    SubChunkStatus.COMPLETE, now)   // cara: 1 topic, 999xp
            ));

            var board = leaderboardService.polymath(20);
            assertThat(board).hasSize(3);
            // Breadth wins over depth
            assertThat(board.get(0).topicCount()).isEqualTo(2);
            assertThat(board.get(1).topicCount()).isEqualTo(2);
            assertThat(board.get(2).topicCount()).isEqualTo(1);
            // Within the 2-topic tier, bob (more XP) outranks alice
            assertThat(board.get(0).username()).isEqualTo("bob");
            assertThat(board.get(1).username()).isEqualTo("alice");
            assertThat(board.get(2).username()).isEqualTo("cara");
        }
    }

    // ── Helpers ─────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("week-start arithmetic")
    class WeekMath {
        @Test
        @DisplayName("Monday → returns the same day at 00:00 UTC")
        void mondayUnchanged() {
            // 2025-04-28 is a Monday (verifiable: dayOfWeek().getValue() == 1)
            LocalDate monday = LocalDate.of(2025, 4, 28);
            assertThat(monday.getDayOfWeek().getValue()).isEqualTo(1);

            Instant ws = LeaderboardService.weekStartFor(monday);
            assertThat(ws).isEqualTo(monday.atStartOfDay(ZoneOffset.UTC).toInstant());
        }

        @Test
        @DisplayName("Sunday → returns the preceding Monday")
        void sundayBacktracks() {
            // 2025-05-04 is a Sunday
            LocalDate sunday = LocalDate.of(2025, 5, 4);
            assertThat(sunday.getDayOfWeek().getValue()).isEqualTo(7);

            Instant ws = LeaderboardService.weekStartFor(sunday);
            LocalDate prevMon = LocalDate.of(2025, 4, 28);
            assertThat(ws).isEqualTo(prevMon.atStartOfDay(ZoneOffset.UTC).toInstant());
        }
    }

    // ── Common stubs ────────────────────────────────────────────────────────────

    /** Java topic with one chunk c1 holding one sub-chunk s1 worth {@code xp}. */
    private void stubTopicJava(int xp) {
        when(chunkRepository.findByTopicIdOrderBySortOrderAsc("java"))
            .thenReturn(List.of(chunk("c1", "java")));
        when(subChunkRepository.findByChunkIdIn(any()))
            .thenReturn(List.of(sub("s1", "c1", xp)));
        lenient().when(subChunkRepository.findAll())
            .thenReturn(List.of(sub("s1", "c1", xp)));
        lenient().when(chunkRepository.findAll())
            .thenReturn(List.of(chunk("c1", "java")));
    }
}
