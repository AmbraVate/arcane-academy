package com.ambravate.polymath.academy.service;

import com.ambravate.polymath.academy.dto.BadgeDto;
import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
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
public class BadgeService {

  private final UserBadgeRepository badgeRepository;
  private final UserRepository userRepository;
  private final UserChunkProgressRepository chunkProgressRepository;
  private final SubChunkRepository subChunkRepository;
  private final UserLearnerProfileRepository profileRepository;
  private final ReviewSessionRepository reviewSessionRepository;

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
      case CHUNK_A_MASTERED -> completedChunks.contains("A");
      case CHUNK_B_MASTERED -> completedChunks.contains("B");
      case CHUNK_C_MASTERED -> completedChunks.contains("C");
      case CHUNK_D_MASTERED -> completedChunks.contains("D");
      case CHUNK_E_MASTERED -> completedChunks.contains("E");
      case CHUNK_F_MASTERED -> completedChunks.contains("F");
      case CHUNK_G_MASTERED -> completedChunks.contains("G");
      case CHUNK_H_MASTERED -> completedChunks.contains("H");
      case CHUNK_I_MASTERED -> completedChunks.contains("I");
      case CHUNK_J_MASTERED -> completedChunks.contains("J");
      case CHUNK_K_MASTERED -> completedChunks.contains("K");
      case ALL_CHUNKS_COMPLETE -> completedChunks.size() >= 11;

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
    // Group progress by chunk via sub-chunk → chunk mapping
    Map<String, String> subChunkToChunk = new HashMap<>();
    for (SubChunk sc : subChunkRepository.findAll()) {
      subChunkToChunk.put(sc.getId(), sc.getChunkId());
    }

    Map<String, List<UserChunkProgress>> byChunk = allProgress.stream()
        .collect(Collectors.groupingBy(p -> subChunkToChunk.getOrDefault(p.getSubChunkId(), "?")));

    Set<String> completed = new HashSet<>();
    for (var entry : byChunk.entrySet()) {
      String chunkId = entry.getKey();
      List<SubChunk> allSubs = subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunkId);
      if (!allSubs.isEmpty() && entry.getValue().stream()
          .filter(p -> p.getStatus() == SubChunkStatus.COMPLETE
              || p.getStatus() == SubChunkStatus.SKIPPED)
          .count() >= allSubs.size()) {
        completed.add(chunkId);
      }
    }
    return completed;
  }
}
