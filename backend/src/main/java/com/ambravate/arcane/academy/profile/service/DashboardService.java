package com.ambravate.arcane.academy.profile.service;

import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.ChunkHealth;
import com.ambravate.arcane.academy.common.domain.DashboardData;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.domain.UserLearnerProfile;
import com.ambravate.arcane.academy.common.domain.UserTopicProfile;
import com.ambravate.arcane.academy.common.repository.ChunkRepository;
import com.ambravate.arcane.academy.common.repository.SubChunkRepository;
import com.ambravate.arcane.academy.common.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.common.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.common.repository.UserRepository;
import com.ambravate.arcane.academy.common.repository.UserTopicProfileRepository;
import com.ambravate.arcane.academy.ai.service.SpacingService;
import com.ambravate.arcane.academy.gamification.service.StreakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

  private final ChunkRepository chunkRepository;
  private final SubChunkRepository subChunkRepository;
  private final UserChunkProgressRepository progressRepository;
  private final UserRepository userRepository;
  private final UserLearnerProfileRepository profileRepository;
  private final UserTopicProfileRepository topicProfileRepository;
  private final SpacingService spacingService;
  private final StreakService streakService;

  /** Java-topic dashboard (backwards-compatible). */
  public DashboardData getDashboard(String userId) {
    return getDashboard(userId, "java");
  }

  public DashboardData getDashboard(String userId, String topicId) {
    User user = userRepository.findById(userId).orElseThrow();

    boolean diagnosticCompleted;
    Instant diagnosticCompletedAt;
    LearnerPath currentPath;
    int dailyGoalMinutes;

    if ("java".equals(topicId)) {
      UserLearnerProfile profile = profileRepository.findByUserId(userId)
          .orElse(UserLearnerProfile.aUserLearnerProfile().withUserId(userId).build());
      diagnosticCompleted = profile.isDiagnosticCompleted();
      diagnosticCompletedAt = profile.getDiagnosticCompletedAt();
      currentPath = profile.getCurrentPath();
      dailyGoalMinutes = profile.getDailyGoalMinutes();
    } else {
      // Per-topic diagnostic state; path defaults to FOUNDATION (tier is chunk-level)
      var topicProfile = topicProfileRepository.findByUserIdAndTopicId(userId, topicId);
      diagnosticCompleted = topicProfile.map(UserTopicProfile::isDiagnosticCompleted).orElse(false);
      diagnosticCompletedAt = topicProfile.map(UserTopicProfile::getDiagnosticCompletedAt).orElse(null);
      currentPath = LearnerPath.FOUNDATION;
      dailyGoalMinutes = 40;
    }

    List<ChunkHealth> chunkHealth = getMemoryHealth(userId, topicId);
    int reviewsDue = spacingService.getDueReviews(userId).size();
    boolean streakAtRisk = streakService.isStreakAtRisk(userId);

    // Overall progress scoped to this topic's sub-chunks
    List<Chunk> topicChunks = chunkRepository.findByTopicIdOrderBySortOrderAsc(topicId);
    List<String> topicChunkIds = topicChunks.stream().map(Chunk::getId).toList();
    List<SubChunk> topicSubChunks = topicChunkIds.isEmpty()
        ? List.of()
        : subChunkRepository.findByChunkIdIn(topicChunkIds);
    long totalSubChunks = topicSubChunks.size();

    List<UserChunkProgress> allProgress = progressRepository.findByUserId(userId);
    Set<String> topicSubChunkIds = topicSubChunks.stream().map(SubChunk::getId).collect(Collectors.toSet());
    long completedSubChunks = allProgress.stream()
        .filter(p -> topicSubChunkIds.contains(p.getSubChunkId()))
        .filter(p -> p.getStatus() == SubChunkStatus.COMPLETE || p.getStatus() == SubChunkStatus.SKIPPED)
        .count();
    double overallProgress = totalSubChunks > 0 ? (double) completedSubChunks / totalSubChunks : 0.0;

    return new DashboardData(
        user.getTotalXp(),
        user.getRank(),
        user.getStreakDays(),
        streakAtRisk,
        currentPath,
        diagnosticCompleted,
        diagnosticCompletedAt,
        reviewsDue,
        dailyGoalMinutes,
        overallProgress,
        chunkHealth
    );
  }

  /**
   * Memory health per chunk for the given topic. GREEN > 0.7, YELLOW 0.4-0.7, RED < 0.4.
   */
  public List<ChunkHealth> getMemoryHealth(String userId, String topicId) {
    List<Chunk> chunks = chunkRepository.findByTopicIdOrderBySortOrderAsc(topicId);
    List<UserChunkProgress> allProgress = progressRepository.findByUserId(userId);
    Map<String, UserChunkProgress> progressMap = allProgress.stream()
        .collect(Collectors.toMap(UserChunkProgress::getSubChunkId, p -> p, (a, b) -> a));

    Set<String> completedChunks = new HashSet<>();
    Map<String, List<SubChunk>> chunkSubs = new HashMap<>();
    for (Chunk c : chunks) {
      List<SubChunk> subs = subChunkRepository.findByChunkIdOrderBySortOrderAsc(c.getId());
      chunkSubs.put(c.getId(), subs);
      if (!subs.isEmpty() && subs.stream().allMatch(sc -> {
        UserChunkProgress p = progressMap.get(sc.getId());
        return p != null && (p.getStatus() == SubChunkStatus.COMPLETE
            || p.getStatus() == SubChunkStatus.SKIPPED);
      })) {
        completedChunks.add(c.getId());
      }
    }

    List<ChunkHealth> result = new ArrayList<>();
    for (Chunk chunk : chunks) {
      List<SubChunk> subs = chunkSubs.get(chunk.getId());
      double avgStrength = 0.0;
      int count = 0;

      for (SubChunk sc : subs) {
        UserChunkProgress p = progressMap.get(sc.getId());
        if (p != null && (p.getStatus() == SubChunkStatus.COMPLETE
            || p.getStatus() == SubChunkStatus.SKIPPED)) {
          avgStrength += spacingService.computeDecayedStrength(p);
          count++;
        }
      }
      if (count > 0) avgStrength /= count;

      String healthColor = avgStrength > 0.7 ? "GREEN" : avgStrength >= 0.4 ? "YELLOW" : "RED";

      String status;
      if (completedChunks.contains(chunk.getId())) {
        status = "COMPLETE";
      } else if (subs.stream().anyMatch(sc -> {
        UserChunkProgress p = progressMap.get(sc.getId());
        return p != null && p.getStatus() == SubChunkStatus.IN_PROGRESS;
      })) {
        status = "IN_PROGRESS";
      } else {
        List<String> prereqs = parsePrereqs(chunk.getPrerequisiteIds());
        status = prereqs.isEmpty() || completedChunks.containsAll(prereqs) ? "UNLOCKED" : "LOCKED";
      }

      result.add(new ChunkHealth(
          chunk.getId(), chunk.getTitle(), chunk.getGlyph(),
          status, avgStrength, healthColor,
          subs.size(), count,
          chunk.getTier() != null ? chunk.getTier().name() : "FOUNDATION"
      ));
    }
    return result;
  }

  private List<String> parsePrereqs(String json) {
    if (json == null || json.isBlank()) return List.of();
    try {
      return new com.fasterxml.jackson.databind.ObjectMapper().readValue(
          json, new com.fasterxml.jackson.core.type.TypeReference<>() {});
    } catch (Exception e) {
      return List.of();
    }
  }
}
