package com.ambravate.arcane.academy.gamification.service;

import com.ambravate.arcane.academy.common.telemetry.service.TelemetryService;
import com.ambravate.arcane.academy.common.dto.BadgeDto;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import com.ambravate.arcane.academy.common.domain.BadgeDefinition;
import com.ambravate.arcane.academy.common.domain.ReviewSession;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserBadge;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.domain.UserLearnerProfile;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.gamification.repository.UserBadgeRepository;
import com.ambravate.arcane.academy.auth.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeService implements GamificationFacade {

  private final UserBadgeRepository badgeRepository;
  private final UserRepository userRepository;
  private final LessonRepository lessonRepository;
  private final UserLearnerProfileRepository profileRepository;
  private final StreakService streakService;
  private final TelemetryService telemetry;
  private final JdbcTemplate jdbc;

  // ── Module counts per domain tier ─────────────────────────────────────────
  // Update these when new modules are added to the content.
  private static final int SE_APP_MODULES  = 7;
  private static final int SE_JUN_MODULES  = 9;
  private static final int SE_SEN_MODULES  = 9;
  private static final int SE_LEA_MODULES  = 7;

  private static final int FE_APP_MODULES  = 8;
  private static final int FE_JUN_MODULES  = 10;
  private static final int FE_SEN_MODULES  = 5;
  private static final int FE_LEA_MODULES  = 6;

  private static final int DE_APP_MODULES  = 8;
  private static final int DE_JUN_MODULES  = 9;
  private static final int DE_SEN_MODULES  = 8;
  private static final int DE_LEA_MODULES  = 6;

  public List<BadgeDto> getAllForUser(String userId) {
    Map<String, UserBadge> earned = badgeRepository.findByUserId(userId)
        .stream()
        .collect(Collectors.toMap(UserBadge::getBadgeId, badge -> badge));

    return Arrays.stream(BadgeDefinition.values())
        .map(def -> BadgeDto.aBadgeDto()
            .withId(def.name())
            .withDisplayName(def.getDisplayName())
            .withDescription(def.getDescription())
            .withGlyph(def.getGlyph())
            .withCategory(def.getCategory().name())
            .withEarned(earned.containsKey(def.name()))
            .withEarnedAt(
                earned.containsKey(def.name()) ? earned.get(def.name()).getEarnedAt() : null)
            .build()
        ).collect(Collectors.toList());
  }

  public List<BadgeDto> getEarnedForUser(String userId) {
    return getAllForUser(userId)
        .stream()
        .filter(BadgeDto::isEarned)
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public List<BadgeDto> evaluateAndAwardBadges(String userId,
                                               List<UserChunkProgress> allProgress,
                                               List<ReviewSession> sessions) {
    return evaluateAndAward(userId, allProgress, sessions);
  }

  @Override
  public boolean isStreakAtRisk(String userId) {
    return streakService.isStreakAtRisk(userId);
  }

  @Override
  public int getBadgeCount(String userId) {
    return badgeRepository.findByUserId(userId).size();
  }

  @Override
  public List<BadgeDto> getEarnedBadges(String userId) {
    return getEarnedForUser(userId);
  }

  @Transactional
  public List<BadgeDto> evaluateAndAward(String userId,
                                         List<UserChunkProgress> allProgress,
                                         List<ReviewSession> sessions) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      return Collections.emptyList();
    }

    Set<String> alreadyEarned = badgeRepository.findByUserId(userId).stream()
        .map(UserBadge::getBadgeId)
        .collect(Collectors.toSet());

    long completedSubChunks = allProgress.stream()
        .filter(p -> p.getStatus() == LessonStatus.COMPLETE)
        .count();

    Set<String> completedModules = getCompletedModuleIds(allProgress);

    boolean hasPerfectReview = sessions.stream()
        .anyMatch(s -> s.getScore() >= 1.0 && s.getTotalQuestions() > 0);

    long feynmanCompleted = allProgress.stream()
        .filter(UserChunkProgress::isFeynmanCompleted)
        .count();
    long feynmanHighScore = allProgress.stream()
        .filter(p -> p.isFeynmanCompleted() && p.getFeynmanScore() >= 0.8)
        .count();

    UserLearnerProfile profile = profileRepository.findByUserId(userId).orElse(null);

    Long noteCount = jdbc.queryForObject(
        "SELECT COUNT(*) FROM user_notes WHERE user_id = ?", Long.class, userId);
    long userNoteCount = noteCount != null ? noteCount : 0L;

    List<BadgeDto> newBadges = new ArrayList<>();

    for (BadgeDefinition def : BadgeDefinition.values()) {
      if (alreadyEarned.contains(def.name())) continue;
      if (!checkCondition(def, user, completedSubChunks, completedModules, hasPerfectReview,
          feynmanCompleted, feynmanHighScore, profile, userNoteCount)) continue;

      badgeRepository.save(UserBadge.builder()
          .userId(userId).badgeId(def.name()).earnedAt(Instant.now()).build());

      newBadges.add(BadgeDto.aBadgeDto()
          .withId(def.name())
          .withDisplayName(def.getDisplayName())
          .withDescription(def.getDescription())
          .withGlyph(def.getGlyph())
          .withCategory(def.getCategory().name())
          .withEarned(true)
          .withEarnedAt(Instant.now())
          .build());

      log.info("[BadgeService] Badge awarded | userId={} badge={}", userId, def.name());
      telemetry.badgeEarned(userId, def.name(), def.getCategory().name());
    }

    return newBadges;
  }

  private boolean checkCondition(BadgeDefinition def, User user,
      long completedSubChunks, Set<String> completedModules,
      boolean hasPerfectReview, long feynmanCompleted,
      long feynmanHighScore, UserLearnerProfile profile, long noteCount) {
    return switch (def) {

      // ── Onboarding & first steps ─────────────────────────────────────────
      case ARCANE_INITIATE -> user.isOnboardingCompleted();
      case FIRST_CONCEPT   -> completedSubChunks >= 1;

      // ── Software Engineering tier badges ─────────────────────────────────
      case SE_APPRENTICE_COMPLETE -> isAllModulesComplete(completedModules, "se-app-m", SE_APP_MODULES);
      case SE_JUNIOR_COMPLETE     -> isAllModulesComplete(completedModules, "se-jun-m", SE_JUN_MODULES);
      case SE_SENIOR_COMPLETE     -> isAllModulesComplete(completedModules, "se-sen-m", SE_SEN_MODULES);
      case SE_LEAD_COMPLETE       -> isAllModulesComplete(completedModules, "se-lea-m", SE_LEA_MODULES);
      // Capstone badges — true once a capstone lesson ID is added to content
      case SE_APPRENTICE_CAPSTONE, SE_JUNIOR_CAPSTONE,
           SE_SENIOR_CAPSTONE, SE_LEAD_CAPSTONE -> false;

      // ── Frontend Engineering tier badges ─────────────────────────────────
      case FE_APPRENTICE_COMPLETE -> isAllModulesComplete(completedModules, "fe-app-m", FE_APP_MODULES);
      case FE_JUNIOR_COMPLETE     -> isAllModulesComplete(completedModules, "fe-jun-m", FE_JUN_MODULES);
      case FE_SENIOR_COMPLETE     -> isAllModulesComplete(completedModules, "fe-sen-m", FE_SEN_MODULES);
      case FE_LEAD_COMPLETE       -> isAllModulesComplete(completedModules, "fe-lea-m", FE_LEA_MODULES);
      case FE_APPRENTICE_CAPSTONE, FE_JUNIOR_CAPSTONE,
           FE_SENIOR_CAPSTONE, FE_LEAD_CAPSTONE -> false;

      // ── Data Engineering tier badges ─────────────────────────────────────
      case DE_APPRENTICE_COMPLETE -> isAllModulesComplete(completedModules, "de-app-m", DE_APP_MODULES);
      case DE_JUNIOR_COMPLETE     -> isAllModulesComplete(completedModules, "de-jun-m", DE_JUN_MODULES);
      case DE_SENIOR_COMPLETE     -> isAllModulesComplete(completedModules, "de-sen-m", DE_SEN_MODULES);
      case DE_LEAD_COMPLETE       -> isAllModulesComplete(completedModules, "de-lead-m", DE_LEA_MODULES);
      case DE_APPRENTICE_CAPSTONE, DE_JUNIOR_CAPSTONE,
           DE_SENIOR_CAPSTONE, DE_LEAD_CAPSTONE -> false;

      // ── Cross-domain tier completion ──────────────────────────────────────
      case APPRENTICE_COMPLETE ->
          isAllModulesComplete(completedModules, "se-app-m",  SE_APP_MODULES) ||
          isAllModulesComplete(completedModules, "fe-app-m",  FE_APP_MODULES) ||
          isAllModulesComplete(completedModules, "de-app-m",  DE_APP_MODULES);
      case JUNIOR_COMPLETE ->
          isAllModulesComplete(completedModules, "se-jun-m",  SE_JUN_MODULES) ||
          isAllModulesComplete(completedModules, "fe-jun-m",  FE_JUN_MODULES) ||
          isAllModulesComplete(completedModules, "de-jun-m",  DE_JUN_MODULES);
      case SENIOR_COMPLETE ->
          isAllModulesComplete(completedModules, "se-sen-m",  SE_SEN_MODULES) ||
          isAllModulesComplete(completedModules, "fe-sen-m",  FE_SEN_MODULES) ||
          isAllModulesComplete(completedModules, "de-sen-m",  DE_SEN_MODULES);
      case LEAD_COMPLETE ->
          isAllModulesComplete(completedModules, "se-lea-m",  SE_LEA_MODULES) ||
          isAllModulesComplete(completedModules, "fe-lea-m",  FE_LEA_MODULES) ||
          isAllModulesComplete(completedModules, "de-lead-m", DE_LEA_MODULES);

      // ── Note-taking ───────────────────────────────────────────────────────
      case FIRST_NOTE   -> noteCount >= 1;
      case AVID_SCHOLAR -> noteCount >= 50;

      // ── Mastery ───────────────────────────────────────────────────────────
      case PERFECT_REVIEW  -> hasPerfectReview;
      case MEMORY_MASTER   -> false;  // requires full memory-health check; not yet implemented
      case DIAGNOSTIC_ACE  -> profile != null && profile.isDiagnosticCompleted()
                                  && profile.getDiagnosticScore() >= 0.8;

      // ── Feynman ───────────────────────────────────────────────────────────
      case FEYNMAN_FIRST  -> feynmanCompleted >= 1;
      case FEYNMAN_MASTER -> feynmanHighScore >= 10;

      // ── Exploration ───────────────────────────────────────────────────────
      case RABBIT_HOLE_FIRST -> false;  // triggered directly by rabbit-hole completion event

      // ── XP thresholds ─────────────────────────────────────────────────────
      case XP_100  -> user.getTotalXp() >= 100;
      case XP_500  -> user.getTotalXp() >= 500;
      case XP_1000 -> user.getTotalXp() >= 1000;
      case XP_2500 -> user.getTotalXp() >= 2500;
      case XP_5000 -> user.getTotalXp() >= 5000;

      // ── Streak milestones ─────────────────────────────────────────────────
      case STREAK_3  -> user.getStreakDays() >= 3;
      case STREAK_7  -> user.getStreakDays() >= 7;
      case STREAK_30 -> user.getStreakDays() >= 30;
    };
  }

  /**
   * Returns true if the number of completed modules whose IDs start with {@code prefix}
   * meets or exceeds {@code required}.
   */
  private boolean isAllModulesComplete(Set<String> completedModules, String prefix, int required) {
    long count = completedModules.stream()
        .filter(id -> id.startsWith(prefix))
        .count();
    return count >= required;
  }

  /**
   * Builds the set of module IDs for which every lesson has been completed or skipped.
   */
  private Set<String> getCompletedModuleIds(List<UserChunkProgress> allProgress) {
    if (allProgress.isEmpty()) return Collections.emptySet();

    List<String> lessonIds = allProgress.stream()
        .map(UserChunkProgress::getLessonId)
        .distinct()
        .collect(Collectors.toList());
    Map<String, String> lessonToModule = lessonRepository.findAllById(lessonIds).stream()
        .collect(Collectors.toMap(Lesson::getId, Lesson::getModuleId));

    Map<String, List<UserChunkProgress>> byModule = allProgress.stream()
        .collect(Collectors.groupingBy(
            p -> lessonToModule.getOrDefault(p.getLessonId(), "?")));

    List<String> moduleIds = new ArrayList<>(byModule.keySet());
    moduleIds.remove("?");
    Map<String, Long> lessonCountByModule = lessonRepository.findByModuleIdIn(moduleIds).stream()
        .collect(Collectors.groupingBy(Lesson::getModuleId, Collectors.counting()));

    Set<String> completed = new HashSet<>();
    for (var entry : byModule.entrySet()) {
      String moduleId = entry.getKey();
      long totalLessons = lessonCountByModule.getOrDefault(moduleId, 0L);
      if (totalLessons > 0 && entry.getValue().stream()
          .filter(p -> p.getStatus() == LessonStatus.COMPLETE
              || p.getStatus() == LessonStatus.SKIPPED)
          .count() >= totalLessons) {
        completed.add(moduleId);
      }
    }
    return completed;
  }
}
