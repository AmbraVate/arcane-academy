package com.ambravate.arcane.academy.service;

import com.ambravate.arcane.academy.common.domain.BadgeDefinition;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.Topic;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserBadge;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.repository.ChunkRepository;
import com.ambravate.arcane.academy.common.repository.SubChunkRepository;
import com.ambravate.arcane.academy.common.repository.TopicRepository;
import com.ambravate.arcane.academy.common.repository.UserBadgeRepository;
import com.ambravate.arcane.academy.common.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.common.repository.UserRepository;
import com.ambravate.arcane.academy.profile.service.PublicProfileService;
import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import com.ambravate.arcane.academy.profile.domain.PublicProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link PublicProfileService} — verifies that:
 * <ul>
 *   <li>Profiles are gated by the {@code publicProfileEnabled} opt-in flag</li>
 *   <li>Per-topic XP and completion counts are aggregated correctly across topics</li>
 *   <li>Earned badges are decorated with display names from {@link BadgeDefinition} and sorted recency-first</li>
 *   <li>Unknown badge IDs degrade gracefully without throwing</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PublicProfileService")
class PublicProfileServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserChunkProgressRepository progressRepository;
    @Mock private UserBadgeRepository badgeRepository;
    @Mock private ChunkRepository chunkRepository;
    @Mock private SubChunkRepository subChunkRepository;
    @Mock private TopicRepository topicRepository;

    @InjectMocks private PublicProfileService service;

    private User user(String username, boolean publicEnabled) {
        return User.aUser()
            .withId("u-" + username)
            .withUsername(username)
            .withEmail(username + "@test")
            .withPublicProfileEnabled(publicEnabled)
            .withTotalXp(1234)
            .withStreakDays(7)
            .withRank("Adept")
            .withCreatedAt(Instant.parse("2025-01-01T00:00:00Z"))
            .build();
    }

    // ── Privacy gate ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Returns empty when username does not exist")
    void unknownUserNotFound() {
        when(userRepository.findByUsername("nobody")).thenReturn(Optional.empty());
        assertThat(service.findByUsername("nobody")).isEmpty();
    }

    @Test
    @DisplayName("Returns empty when user opted out (publicProfileEnabled=false)")
    void privateProfileHidden() {
        when(userRepository.findByUsername("hermit")).thenReturn(Optional.of(user("hermit", false)));
        assertThat(service.findByUsername("hermit")).isEmpty();
    }

    // ── Aggregation ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Aggregates per-topic XP + completion counts when opted in")
    void aggregatesPerTopic() {
        User alice = user("alice", true);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(alice));

        when(chunkRepository.findAll()).thenReturn(List.of(
            Chunk.builder().id("c-java").title("Java A").topicId("java").sortOrder(1).build(),
            Chunk.builder().id("c-tw").title("TW A").topicId("tailwind").sortOrder(1).build()
        ));
        when(subChunkRepository.findAll()).thenReturn(List.of(
            SubChunk.builder().id("s-java-1").chunkId("c-java").title("J1").sortOrder(1).xpReward(50).build(),
            SubChunk.builder().id("s-java-2").chunkId("c-java").title("J2").sortOrder(2).xpReward(75).build(),
            SubChunk.builder().id("s-tw-1").chunkId("c-tw").title("T1").sortOrder(1).xpReward(40).build()
        ));
        when(progressRepository.findByUserId("u-alice")).thenReturn(List.of(
            UserChunkProgress.builder().userId("u-alice").subChunkId("s-java-1")
                .status(SubChunkStatus.COMPLETE).build(),
            UserChunkProgress.builder().userId("u-alice").subChunkId("s-java-2")
                .status(SubChunkStatus.COMPLETE).build(),
            UserChunkProgress.builder().userId("u-alice").subChunkId("s-tw-1")
                .status(SubChunkStatus.COMPLETE).build(),
            // IN_PROGRESS row is ignored
            UserChunkProgress.builder().userId("u-alice").subChunkId("s-tw-1")
                .status(SubChunkStatus.IN_PROGRESS).build()
        ));
        when(topicRepository.findAll()).thenReturn(List.of(
            Topic.builder().id("java").name("Java").glyph("☕").accentColor("#f00").sortOrder(1).build(),
            Topic.builder().id("tailwind").name("Tailwind").glyph("🎨").accentColor("#0f0").sortOrder(2).build()
        ));
        when(badgeRepository.findByUserId("u-alice")).thenReturn(List.of());

        Optional<PublicProfile> result = service.findByUsername("alice");
        assertThat(result).isPresent();

        PublicProfile profile = result.get();
        assertThat(profile.username()).isEqualTo("alice");
        assertThat(profile.totalXp()).isEqualTo(1234);
        assertThat(profile.streakDays()).isEqualTo(7);
        assertThat(profile.rank()).isEqualTo("Adept");

        // Topics sorted by xpEarned desc — java (50+75=125) before tailwind (40)
        assertThat(profile.topics()).hasSize(2);
        assertThat(profile.topics().get(0).topicId()).isEqualTo("java");
        assertThat(profile.topics().get(0).xpEarned()).isEqualTo(125);
        assertThat(profile.topics().get(0).subChunksCompleted()).isEqualTo(2);
        assertThat(profile.topics().get(0).name()).isEqualTo("Java");
        assertThat(profile.topics().get(1).topicId()).isEqualTo("tailwind");
        assertThat(profile.topics().get(1).xpEarned()).isEqualTo(40);
        assertThat(profile.topics().get(1).subChunksCompleted()).isEqualTo(1);
    }

    // ── Badge decoration ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Decorates badges with display names + sorts recency-first")
    void decoratesBadges() {
        User alice = user("alice", true);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(alice));
        when(progressRepository.findByUserId("u-alice")).thenReturn(List.of());
        lenient().when(chunkRepository.findAll()).thenReturn(List.of());
        lenient().when(subChunkRepository.findAll()).thenReturn(List.of());
        lenient().when(topicRepository.findAll()).thenReturn(List.of());

        Instant older = Instant.parse("2025-01-01T00:00:00Z");
        Instant newer = Instant.parse("2025-04-01T00:00:00Z");
        when(badgeRepository.findByUserId("u-alice")).thenReturn(List.of(
            UserBadge.builder().userId("u-alice").badgeId("FIRST_CONCEPT").earnedAt(older).build(),
            UserBadge.builder().userId("u-alice").badgeId("STREAK_7").earnedAt(newer).build()
        ));

        PublicProfile profile = service.findByUsername("alice").orElseThrow();
        assertThat(profile.badges()).hasSize(2);
        // Newer first
        assertThat(profile.badges().get(0).id()).isEqualTo("STREAK_7");
        assertThat(profile.badges().get(0).displayName()).isEqualTo(BadgeDefinition.STREAK_7.getDisplayName());
        assertThat(profile.badges().get(0).category()).isEqualTo("STREAK");
        assertThat(profile.badges().get(1).id()).isEqualTo("FIRST_CONCEPT");
    }

    @Test
    @DisplayName("Falls back gracefully when a saved badge id is unknown to the enum")
    void unknownBadgeIdSurvives() {
        User alice = user("alice", true);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(alice));
        when(progressRepository.findByUserId("u-alice")).thenReturn(List.of());
        lenient().when(chunkRepository.findAll()).thenReturn(List.of());
        lenient().when(subChunkRepository.findAll()).thenReturn(List.of());
        lenient().when(topicRepository.findAll()).thenReturn(List.of());

        when(badgeRepository.findByUserId("u-alice")).thenReturn(List.of(
            UserBadge.builder().userId("u-alice").badgeId("LEGACY_BADGE_NO_LONGER_DEFINED")
                .earnedAt(Instant.now()).build()
        ));

        PublicProfile profile = service.findByUsername("alice").orElseThrow();
        assertThat(profile.badges()).hasSize(1);
        assertThat(profile.badges().get(0).id()).isEqualTo("LEGACY_BADGE_NO_LONGER_DEFINED");
        assertThat(profile.badges().get(0).displayName()).isEqualTo("LEGACY_BADGE_NO_LONGER_DEFINED");
        assertThat(profile.badges().get(0).category()).isEqualTo("OTHER");
    }
}
