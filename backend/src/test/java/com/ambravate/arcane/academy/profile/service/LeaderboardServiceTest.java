package com.ambravate.arcane.academy.profile.service;

import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.profile.domain.LeaderboardEntry;
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
    @Mock private LessonRepository lessonRepository;
    @Mock private LearningModuleRepository moduleRepository;
    @Mock private GamificationFacade gamificationFacade;

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

    private LearningModule chunk(String id, String domainId) {
        return LearningModule.builder().id(id).title("c").trackId(domainId).sortOrder(1).build();
    }

    private Lesson sub(String id, String moduleId, int xpReward) {
        return Lesson.builder().id(id).moduleId(moduleId).title("s").sortOrder(1).xpReward(xpReward).build();
    }

    private UserChunkProgress progress(String userId, String lessonId, LessonStatus status, Instant completedAt) {
        return UserChunkProgress.builder()
            .userId(userId).lessonId(lessonId).status(status).completedAt(completedAt).build();
    }

    @BeforeEach
    void wireDefaults() {
        // Default to 0 badges so badgeCount() returns 0 unless overridden by a test
        lenient().when(gamificationFacade.getBadgeCount(anyString())).thenReturn(0);
    }

    // ── Topic weekly ────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("topicWeekly")
    class TopicWeekly {

        @Test
        @DisplayName("Returns empty list when nobody is opted in")
        void emptyWhenNoneOptedIn() {
            when(userRepository.findByPublicProfileEnabledTrue()).thenReturn(List.of());
            when(moduleRepository.findByTrackIdOrderBySortOrderAsc("java")).thenReturn(List.of(chunk("c1", "java")));
            when(lessonRepository.findByModuleIdIn(any())).thenReturn(List.of(sub("s1", "c1", 50)));

            assertThat(leaderboardService.topicWeekly("java", 20)).isEmpty();
        }

        @Test
        @DisplayName("Excludes opted-out users even with completed XP")
        void excludesOptedOut() {
            User alice = user("u-a", "alice", 100, 0, true);
            User bob   = user("u-b", "bob",   500, 0, false);  // not opted in

            when(userRepository.findByPublicProfileEnabledTrue()).thenReturn(List.of(alice));
            stubDomainJava(50);
            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s1", LessonStatus.COMPLETE, Instant.now()),
                progress("u-b", "s1", LessonStatus.COMPLETE, Instant.now())
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
            stubDomainJava(50);
            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s1", LessonStatus.IN_PROGRESS, Instant.now()),
                progress("u-a", "s1", LessonStatus.SKIPPED,     Instant.now())
            ));

            assertThat(leaderboardService.topicWeekly("java", 20)).isEmpty();
        }

        @Test
        @DisplayName("Ignores completions older than the current ISO-week start")
        void ignoresOldCompletions() {
            User alice = user("u-a", "alice", 0, 0, true);
            when(userRepository.findByPublicProfileEnabledTrue()).thenReturn(List.of(alice));
            stubDomainJava(50);

            Instant weekStart = LeaderboardService.currentWeekStart();
            Instant lastWeek = weekStart.minus(Duration.ofDays(2));
            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s1", LessonStatus.COMPLETE, lastWeek)
            ));

            assertThat(leaderboardService.topicWeekly("java", 20)).isEmpty();
        }

        @Test
        @DisplayName("Excludes XP from other topics' lessons")
        void otherTopicsExcluded() {
            User alice = user("u-a", "alice", 0, 0, true);
            when(userRepository.findByPublicProfileEnabledTrue()).thenReturn(List.of(alice));

            when(moduleRepository.findByTrackIdOrderBySortOrderAsc("java"))
                .thenReturn(List.of(chunk("c-java", "java")));
            when(lessonRepository.findByModuleIdIn(any())).thenReturn(List.of(sub("s-java", "c-java", 100)));
            when(lessonRepository.findAll()).thenReturn(List.of(
                sub("s-java", "c-java", 100),
                sub("s-tailwind", "c-tailwind", 999)
            ));

            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s-tailwind", LessonStatus.COMPLETE, Instant.now())
            ));

            assertThat(leaderboardService.topicWeekly("java", 20)).isEmpty();
        }

        @Test
        @DisplayName("Sums XP across multiple completions and orders descending")
        void sumsAndOrders() {
            User alice = user("u-a", "alice", 0, 0, true);
            User bob   = user("u-b", "bob",   0, 0, true);
            when(userRepository.findByPublicProfileEnabledTrue()).thenReturn(List.of(alice, bob));
            stubDomainJava(50);
            when(lessonRepository.findAll()).thenReturn(List.of(
                sub("s1", "c1", 50),
                sub("s2", "c1", 75)
            ));
            when(lessonRepository.findByModuleIdIn(any())).thenReturn(List.of(
                sub("s1", "c1", 50),
                sub("s2", "c1", 75)
            ));

            Instant now = Instant.now();
            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s1", LessonStatus.COMPLETE, now),
                progress("u-a", "s2", LessonStatus.COMPLETE, now),  // alice = 125
                progress("u-b", "s1", LessonStatus.COMPLETE, now)   // bob   =  50
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
            stubDomainJava(50);

            Instant now = Instant.now();
            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s1", LessonStatus.COMPLETE, now),
                progress("u-b", "s1", LessonStatus.COMPLETE, now),
                progress("u-c", "s1", LessonStatus.COMPLETE, now)
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
            stubDomainJava(50);

            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s1", LessonStatus.COMPLETE,
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

            when(moduleRepository.findAll()).thenReturn(List.of(
                chunk("c-java",     "java"),
                chunk("c-tailwind", "tailwind"),
                chunk("c-react",    "react")
            ));
            when(lessonRepository.findAll()).thenReturn(List.of(
                sub("s-java",     "c-java",     50),
                sub("s-tailwind", "c-tailwind", 50),
                sub("s-react",    "c-react",    999)
            ));

            Instant now = Instant.now();
            when(progressRepository.findAll()).thenReturn(List.of(
                progress("u-a", "s-java",     LessonStatus.COMPLETE, now),
                progress("u-a", "s-tailwind", LessonStatus.COMPLETE, now),  // alice: 2 topics, 100xp
                progress("u-b", "s-java",     LessonStatus.COMPLETE, now),
                progress("u-b", "s-tailwind", LessonStatus.COMPLETE, now),
                progress("u-b", "s-tailwind", LessonStatus.COMPLETE, now),  // duplicate row → bob: 2 topics, 150xp
                progress("u-c", "s-react",    LessonStatus.COMPLETE, now)   // cara: 1 topic, 999xp
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
            LocalDate monday = LocalDate.of(2025, 4, 28);
            assertThat(monday.getDayOfWeek().getValue()).isEqualTo(1);

            Instant ws = LeaderboardService.weekStartFor(monday);
            assertThat(ws).isEqualTo(monday.atStartOfDay(ZoneOffset.UTC).toInstant());
        }

        @Test
        @DisplayName("Sunday → returns the preceding Monday")
        void sundayBacktracks() {
            LocalDate sunday = LocalDate.of(2025, 5, 4);
            assertThat(sunday.getDayOfWeek().getValue()).isEqualTo(7);

            Instant ws = LeaderboardService.weekStartFor(sunday);
            LocalDate prevMon = LocalDate.of(2025, 4, 28);
            assertThat(ws).isEqualTo(prevMon.atStartOfDay(ZoneOffset.UTC).toInstant());
        }
    }

    // ── Common stubs ────────────────────────────────────────────────────────────

    /** Java domain with one module c1 holding one lesson s1 worth {@code xp}. */
    private void stubDomainJava(int xp) {
        when(moduleRepository.findByTrackIdOrderBySortOrderAsc("java"))
            .thenReturn(List.of(chunk("c1", "java")));
        when(lessonRepository.findByModuleIdIn(any()))
            .thenReturn(List.of(sub("s1", "c1", xp)));
        lenient().when(lessonRepository.findAll())
            .thenReturn(List.of(sub("s1", "c1", xp)));
        lenient().when(moduleRepository.findAll())
            .thenReturn(List.of(chunk("c1", "java")));
    }
}
