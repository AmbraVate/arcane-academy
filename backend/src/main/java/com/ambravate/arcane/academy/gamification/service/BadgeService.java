package com.ambravate.arcane.academy.gamification.service;

import com.ambravate.arcane.academy.common.telemetry.service.TelemetryService;
import com.ambravate.arcane.academy.common.dto.BadgeDto;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import com.ambravate.arcane.academy.common.domain.BadgeDefinition;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.ReviewSession;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserBadge;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.domain.UserLearnerProfile;
import com.ambravate.arcane.academy.common.repository.ReviewSessionRepository;
import com.ambravate.arcane.academy.common.repository.SubChunkRepository;
import com.ambravate.arcane.academy.common.repository.UserBadgeRepository;
import com.ambravate.arcane.academy.common.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.common.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private final UserChunkProgressRepository chunkProgressRepository;
  private final SubChunkRepository subChunkRepository;
  private final UserLearnerProfileRepository profileRepository;
  private final ReviewSessionRepository reviewSessionRepository;
  private final StreakService streakService;
  private final TelemetryService telemetry;

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
  public List<BadgeDto> evaluateAndAwardBadges(String userId) {
    return evaluateAndAward(userId);
  }

  @Override
  public boolean isStreakAtRisk(String userId) {
    return streakService.isStreakAtRisk(userId);
  }

  @Transactional
  public List<BadgeDto> evaluateAndAward(String userId) {
    User user = userRepository.findById(userId)
        .orElse(null);

    if (user == null) {
      return Collections.emptyList();
    }

    Set<String> alreadyEarned = badgeRepository.findByUserId(userId).stream()
        .map(UserBadge::getBadgeId)
        .collect(Collectors.toSet());

    List<UserChunkProgress> allProgress = chunkProgressRepository.findByUserId(userId);
    long completedSubChunks = allProgress.stream()
        .filter(progress -> progress.getStatus() == SubChunkStatus.COMPLETE)
        .count();

    // Build set of completed chunk IDs
    Set<String> completedChunks = getCompletedChunkIds(allProgress);

    // Review session stats
    List<ReviewSession> sessions = reviewSessionRepository.findByUserIdOrderByStartedAtDesc(userId);
    boolean hasPerfectReview = sessions.stream()
        .anyMatch(session -> session.getScore() >= 1.0 && session.getTotalQuestions() > 0);

    // Feynman stats
    long feynmanCompleted = allProgress.stream()
        .filter(UserChunkProgress::isFeynmanCompleted)
        .count();
    long feynmanHighScore = allProgress.stream()
        .filter(progress -> progress.isFeynmanCompleted() && progress.getFeynmanScore() >= 0.8)
        .count();

    // Profile
    UserLearnerProfile profile = profileRepository.findByUserId(userId).orElse(null);

    List<BadgeDto> newBadges = new ArrayList<>();

    for (BadgeDefinition def : BadgeDefinition.values()) {
      if (alreadyEarned.contains(def.name())) {
        continue;
      }
      if (!checkCondition(
          def, user, completedSubChunks, completedChunks, hasPerfectReview,
          feynmanCompleted, feynmanHighScore, profile
      )) {
        continue;
      }

      badgeRepository.save(UserBadge.builder()
          .userId(userId).badgeId(def.name()).earnedAt(Instant.now()).build());

      newBadges.add(
          BadgeDto.aBadgeDto()
              .withId(def.name())
              .withDisplayName(def.getDisplayName())
              .withDescription(def.getDescription())
              .withGlyph(def.getGlyph())
              .withCategory(def.getCategory().name())
              .withEarned(true)
              .withEarnedAt(Instant.now())
              .build()
      );

      log.info("[BadgeService] Badge awarded | userId={} badge={}", userId, def.name());
      telemetry.badgeEarned(userId, def.name(), def.getCategory().name());
    }

    return newBadges;
  }

  private boolean checkCondition(
      BadgeDefinition def, User user,
      long completedSubChunks, Set<String> completedChunks,
      boolean hasPerfectReview, long feynmanCompleted,
      long feynmanHighScore, UserLearnerProfile profile
  ) {
    return switch (def) {
      // Learning
      case FIRST_CONCEPT -> completedSubChunks >= 1;

      // Java Foundation milestones (java-fnd-1 through java-fnd-8)
      case JAVA_FND_1_COMPLETE -> completedChunks.contains("java-fnd-1");
      case JAVA_FND_2_COMPLETE -> completedChunks.contains("java-fnd-2");
      case JAVA_FND_3_COMPLETE -> completedChunks.contains("java-fnd-3");
      case JAVA_FND_4_COMPLETE -> completedChunks.contains("java-fnd-4");
      case JAVA_FND_5_COMPLETE -> completedChunks.contains("java-fnd-5");
      case JAVA_FND_6_COMPLETE -> completedChunks.contains("java-fnd-6");
      case JAVA_FND_7_COMPLETE -> completedChunks.contains("java-fnd-7");
      case JAVA_FND_8_COMPLETE -> completedChunks.contains("java-fnd-8");
      case JAVA_FOUNDATION_COMPLETE -> completedChunks.containsAll(java.util.List.of(
          "java-fnd-1", "java-fnd-2", "java-fnd-3", "java-fnd-4",
          "java-fnd-5", "java-fnd-6", "java-fnd-7", "java-fnd-8"));

      case JAVA_CAPSTONE_COMPLETE -> completedChunks.contains("java-cap");
      case TAILWIND_CAPSTONE_COMPLETE -> completedChunks.contains("tw-d");

      // SQL track — one badge per tier, plus the track-master capstone
      case SQL_QUERY_INITIATE -> completedChunks.contains("sql-a");
      case SQL_JOIN_WEAVER -> completedChunks.contains("sql-d");
      case SQL_QUERY_OPTIMISER -> completedChunks.contains("sql-g");
      case SQL_TRACK_MASTER -> completedChunks.containsAll(java.util.List.of(
          "sql-a", "sql-b", "sql-c", "sql-d", "sql-e", "sql-f", "sql-g", "sql-h"));

      // React track — Foundation/Practitioner milestones plus the capstone (mirrors Tailwind)
      case REACT_HOOK_INITIATE -> completedChunks.contains("rx-a");
      case REACT_STATE_WEAVER -> completedChunks.contains("rx-b");
      case REACT_CAPSTONE_COMPLETE -> completedChunks.contains("rx-d");

      // Non-technical track Foundation milestones — one badge per Foundation tier
      case PSY_FOUNDATION_COMPLETE -> completedChunks.containsAll(java.util.List.of("psy-a", "psy-b", "psy-c"));
      case GEN_FOUNDATION_COMPLETE -> completedChunks.containsAll(java.util.List.of("gen-a", "gen-b", "gen-c"));
      case SCI_FOUNDATION_COMPLETE -> completedChunks.containsAll(java.util.List.of("sci-a", "sci-b", "sci-c"));

      // Mastery
      case PERFECT_REVIEW -> hasPerfectReview;
      case MEMORY_MASTER -> false; // Checked separately via dashboard
      case DIAGNOSTIC_ACE -> profile != null && profile.isDiagnosticCompleted() && profile.getDiagnosticScore() >= 0.8;

      // Feynman
      case FEYNMAN_FIRST -> feynmanCompleted >= 1;
      case FEYNMAN_MASTER -> feynmanHighScore >= 10;

      // Path
      case PATH_PRACTITIONER ->
          profile != null && profile.getCurrentPath() == LearnerPath.PRACTITIONER;
      case PATH_EXPERT -> profile != null && profile.getCurrentPath() == LearnerPath.EXPERT;

      // Exploration
      case RABBIT_HOLE_FIRST -> false; // Tracked separately when rabbit hole is completed

      // XP
      case XP_100 -> user.getTotalXp() >= 100;
      case XP_500 -> user.getTotalXp() >= 500;
      case XP_1000 -> user.getTotalXp() >= 1000;
      case XP_2500 -> user.getTotalXp() >= 2500;
      case XP_5000 -> user.getTotalXp() >= 5000;

      // Streak
      case STREAK_3 -> user.getStreakDays() >= 3;
      case STREAK_7 -> user.getStreakDays() >= 7;
      case STREAK_30 -> user.getStreakDays() >= 30;
    };
  }

  private Set<String> getCompletedChunkIds(List<UserChunkProgress> allProgress) {
    if (allProgress.isEmpty()) return Collections.emptySet();

    // Load only the sub-chunks this user has touched — avoids a full-table scan
    List<String> subChunkIds = allProgress.stream()
        .map(UserChunkProgress::getSubChunkId)
        .distinct()
        .collect(Collectors.toList());
    Map<String, String> subChunkToChunk = subChunkRepository.findAllById(subChunkIds).stream()
        .collect(Collectors.toMap(SubChunk::getId, SubChunk::getChunkId));

    Map<String, List<UserChunkProgress>> byChunk = allProgress.stream()
        .collect(Collectors.groupingBy(p -> subChunkToChunk.getOrDefault(p.getSubChunkId(), "?")));

    // Load all sub-chunks for the relevant chunks in a single query
    List<String> chunkIds = new ArrayList<>(byChunk.keySet());
    chunkIds.remove("?");
    Map<String, Long> subChunkCountByChunk = subChunkRepository.findByChunkIdIn(chunkIds).stream()
        .collect(Collectors.groupingBy(SubChunk::getChunkId, Collectors.counting()));

    Set<String> completed = new HashSet<>();
    for (var entry : byChunk.entrySet()) {
      String chunkId = entry.getKey();
      long totalSubs = subChunkCountByChunk.getOrDefault(chunkId, 0L);
      if (totalSubs > 0 && entry.getValue().stream()
          .filter(p -> p.getStatus() == SubChunkStatus.COMPLETE
              || p.getStatus() == SubChunkStatus.SKIPPED)
          .count() >= totalSubs) {
        completed.add(chunkId);
      }
    }
    return completed;
  }
}
