package com.ambravate.arcane.academy.profile.service;

import com.ambravate.arcane.academy.common.domain.BadgeDefinition;
import com.ambravate.arcane.academy.common.domain.Domain;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.dto.BadgeDto;
import com.ambravate.arcane.academy.content.repository.DomainRepository;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
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
 *   <li>Per-domain XP and completion counts are aggregated correctly across domains</li>
 *   <li>Earned badges are decorated with display names from {@link BadgeDefinition} and sorted recency-first</li>
 *   <li>Unknown badge IDs degrade gracefully without throwing</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PublicProfileService")
class PublicProfileServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserChunkProgressRepository progressRepository;
    @Mock private GamificationFacade gamificationFacade;
    @Mock private LearningModuleRepository moduleRepository;
    @Mock private LessonRepository lessonRepository;
    @Mock private DomainRepository domainRepository;

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
    @DisplayName("Aggregates per-domain XP + completion counts when opted in")
    void aggregatesPerDomain() {
        User alice = user("alice", true);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(alice));

        when(moduleRepository.findAll()).thenReturn(List.of(
            LearningModule.builder().id("se-app-m1").title("SE Apprentice M1").trackId("software-engineering").sortOrder(1).build(),
            LearningModule.builder().id("fe-app-m1").title("FE Apprentice M1").trackId("frontend-engineering").sortOrder(1).build()
        ));
        when(lessonRepository.findAll()).thenReturn(List.of(
            Lesson.builder().id("se-app-m1-01").moduleId("se-app-m1").title("SE1").sortOrder(1).xpReward(50).build(),
            Lesson.builder().id("se-app-m1-02").moduleId("se-app-m1").title("SE2").sortOrder(2).xpReward(75).build(),
            Lesson.builder().id("fe-app-m1-01").moduleId("fe-app-m1").title("FE1").sortOrder(1).xpReward(40).build()
        ));
        when(progressRepository.findByUserId("u-alice")).thenReturn(List.of(
            UserChunkProgress.builder().userId("u-alice").lessonId("se-app-m1-01")
                .status(LessonStatus.COMPLETE).build(),
            UserChunkProgress.builder().userId("u-alice").lessonId("se-app-m1-02")
                .status(LessonStatus.COMPLETE).build(),
            UserChunkProgress.builder().userId("u-alice").lessonId("fe-app-m1-01")
                .status(LessonStatus.COMPLETE).build(),
            // IN_PROGRESS row is ignored
            UserChunkProgress.builder().userId("u-alice").lessonId("fe-app-m1-01")
                .status(LessonStatus.IN_PROGRESS).build()
        ));
        when(domainRepository.findAll()).thenReturn(List.of(
            Domain.builder().id("software-engineering").name("Software Engineering").glyph("⚙️").accentColor("#2dd4bf").sortOrder(1).build(),
            Domain.builder().id("frontend-engineering").name("Frontend Engineering").glyph("🖥️").accentColor("#f59e0b").sortOrder(2).build()
        ));
        when(gamificationFacade.getEarnedBadges("u-alice")).thenReturn(List.of());

        Optional<PublicProfile> result = service.findByUsername("alice");
        assertThat(result).isPresent();

        PublicProfile profile = result.get();
        assertThat(profile.username()).isEqualTo("alice");
        assertThat(profile.totalXp()).isEqualTo(1234);
        assertThat(profile.streakDays()).isEqualTo(7);
        assertThat(profile.rank()).isEqualTo("Adept");

        // Topics sorted by xpEarned desc — software-engineering (50+75=125) before frontend-engineering (40)
        assertThat(profile.domains()).hasSize(2);
        assertThat(profile.domains().get(0).domainId()).isEqualTo("software-engineering");
        assertThat(profile.domains().get(0).xpEarned()).isEqualTo(125);
        assertThat(profile.domains().get(0).subChunksCompleted()).isEqualTo(2);
        assertThat(profile.domains().get(0).name()).isEqualTo("Software Engineering");
        assertThat(profile.domains().get(1).domainId()).isEqualTo("frontend-engineering");
        assertThat(profile.domains().get(1).xpEarned()).isEqualTo(40);
        assertThat(profile.domains().get(1).subChunksCompleted()).isEqualTo(1);
    }

    // ── Badge decoration ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Decorates badges with display names + sorts recency-first")
    void decoratesBadges() {
        User alice = user("alice", true);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(alice));
        when(progressRepository.findByUserId("u-alice")).thenReturn(List.of());
        lenient().when(moduleRepository.findAll()).thenReturn(List.of());
        lenient().when(lessonRepository.findAll()).thenReturn(List.of());
        lenient().when(domainRepository.findAll()).thenReturn(List.of());

        Instant older = Instant.parse("2025-01-01T00:00:00Z");
        Instant newer = Instant.parse("2025-04-01T00:00:00Z");
        when(gamificationFacade.getEarnedBadges("u-alice")).thenReturn(List.of(
            BadgeDto.aBadgeDto().withId("FIRST_CONCEPT")
                .withDisplayName(BadgeDefinition.FIRST_CONCEPT.getDisplayName())
                .withGlyph(BadgeDefinition.FIRST_CONCEPT.getGlyph())
                .withCategory(BadgeDefinition.FIRST_CONCEPT.getCategory().name())
                .withEarned(true).withEarnedAt(older).build(),
            BadgeDto.aBadgeDto().withId("STREAK_7")
                .withDisplayName(BadgeDefinition.STREAK_7.getDisplayName())
                .withGlyph(BadgeDefinition.STREAK_7.getGlyph())
                .withCategory(BadgeDefinition.STREAK_7.getCategory().name())
                .withEarned(true).withEarnedAt(newer).build()
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
        lenient().when(moduleRepository.findAll()).thenReturn(List.of());
        lenient().when(lessonRepository.findAll()).thenReturn(List.of());
        lenient().when(domainRepository.findAll()).thenReturn(List.of());

        when(gamificationFacade.getEarnedBadges("u-alice")).thenReturn(List.of(
            BadgeDto.aBadgeDto().withId("LEGACY_BADGE_NO_LONGER_DEFINED")
                .withDisplayName("LEGACY_BADGE_NO_LONGER_DEFINED")
                .withGlyph("🏅")
                .withCategory("OTHER")
                .withEarned(true).withEarnedAt(Instant.now()).build()
        ));

        PublicProfile profile = service.findByUsername("alice").orElseThrow();
        assertThat(profile.badges()).hasSize(1);
        assertThat(profile.badges().get(0).id()).isEqualTo("LEGACY_BADGE_NO_LONGER_DEFINED");
        assertThat(profile.badges().get(0).displayName()).isEqualTo("LEGACY_BADGE_NO_LONGER_DEFINED");
        assertThat(profile.badges().get(0).category()).isEqualTo("OTHER");
    }
}
