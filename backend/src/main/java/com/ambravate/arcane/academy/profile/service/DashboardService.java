package com.ambravate.arcane.academy.profile.service;

import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.ModuleHealth;
import com.ambravate.arcane.academy.common.domain.DashboardData;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.domain.UserLearnerProfile;
import com.ambravate.arcane.academy.common.domain.UserTrackProfile;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.auth.repository.UserTrackProfileRepository;
import com.ambravate.arcane.academy.ai.service.SpacingService;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
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

  private final LearningModuleRepository moduleRepository;
  private final LessonRepository lessonRepository;
  private final UserChunkProgressRepository progressRepository;
  private final UserRepository userRepository;
  private final UserLearnerProfileRepository profileRepository;
  private final UserTrackProfileRepository trackProfileRepository;
  private final SpacingService spacingService;
  private final GamificationFacade gamification;

  /** Java-topic dashboard (backwards-compatible). */
  public DashboardData getDashboard(String userId) {
    return getDashboard(userId, "java");
  }

  public DashboardData getDashboard(String userId, String domainId) {
    User user = userRepository.findById(userId).orElseThrow();

    boolean diagnosticCompleted;
    Instant diagnosticCompletedAt;
    LearnerPath currentPath;
    int dailyGoalMinutes;

    if ("java".equals(domainId)) {
      UserLearnerProfile profile = profileRepository.findByUserId(userId)
          .orElse(UserLearnerProfile.aUserLearnerProfile().withUserId(userId).build());
      diagnosticCompleted = profile.isDiagnosticCompleted();
      diagnosticCompletedAt = profile.getDiagnosticCompletedAt();
      currentPath = profile.getCurrentPath();
      dailyGoalMinutes = profile.getDailyGoalMinutes();
    } else {
      // Per-topic diagnostic state; path defaults to FOUNDATION (tier is chunk-level)
      var trackProfile = trackProfileRepository.findByUserIdAndTrackId(userId, domainId);
      diagnosticCompleted = trackProfile.map(UserTrackProfile::isDiagnosticCompleted).orElse(false);
      diagnosticCompletedAt = trackProfile.map(UserTrackProfile::getDiagnosticCompletedAt).orElse(null);
      currentPath = LearnerPath.FOUNDATION;
      dailyGoalMinutes = 40;
    }

    List<ModuleHealth> chunkHealth = getMemoryHealth(userId, domainId);
    int reviewsDue = spacingService.getDueReviews(userId).size();
    boolean streakAtRisk = gamification.isStreakAtRisk(userId);

    // Overall progress scoped to this topic's sub-chunks
    List<LearningModule> topicChunks = moduleRepository.findByTrackIdOrderBySortOrderAsc(domainId);
    List<String> topicChunkIds = topicChunks.stream().map(LearningModule::getId).toList();
    List<Lesson> topicSubChunks = topicChunkIds.isEmpty()
        ? List.of()
        : lessonRepository.findByModuleIdIn(topicChunkIds);
    long totalSubChunks = topicSubChunks.size();

    List<UserChunkProgress> allProgress = progressRepository.findByUserId(userId);
    Set<String> topicSubChunkIds = topicSubChunks.stream().map(Lesson::getId).collect(Collectors.toSet());
    long completedSubChunks = allProgress.stream()
        .filter(p -> topicSubChunkIds.contains(p.getLessonId()))
        .filter(p -> p.getStatus() == LessonStatus.COMPLETE || p.getStatus() == LessonStatus.SKIPPED)
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
  public List<ModuleHealth> getMemoryHealth(String userId, String domainId) {
    List<LearningModule> chunks = moduleRepository.findByTrackIdOrderBySortOrderAsc(domainId);
    List<UserChunkProgress> allProgress = progressRepository.findByUserId(userId);
    Map<String, UserChunkProgress> progressMap = allProgress.stream()
        .collect(Collectors.toMap(UserChunkProgress::getLessonId, p -> p, (a, b) -> a));

    Set<String> completedChunks = new HashSet<>();
    Map<String, List<Lesson>> chunkSubs = new HashMap<>();
    for (LearningModule c : chunks) {
      List<Lesson> subs = lessonRepository.findByModuleIdOrderBySortOrderAsc(c.getId());
      chunkSubs.put(c.getId(), subs);
      if (!subs.isEmpty() && subs.stream().allMatch(sc -> {
        UserChunkProgress p = progressMap.get(sc.getId());
        return p != null && (p.getStatus() == LessonStatus.COMPLETE
            || p.getStatus() == LessonStatus.SKIPPED);
      })) {
        completedChunks.add(c.getId());
      }
    }

    List<ModuleHealth> result = new ArrayList<>();
    for (LearningModule chunk : chunks) {
      List<Lesson> subs = chunkSubs.get(chunk.getId());
      double avgStrength = 0.0;
      int count = 0;

      for (Lesson sc : subs) {
        UserChunkProgress p = progressMap.get(sc.getId());
        if (p != null && (p.getStatus() == LessonStatus.COMPLETE
            || p.getStatus() == LessonStatus.SKIPPED)) {
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
        return p != null && p.getStatus() == LessonStatus.IN_PROGRESS;
      })) {
        status = "IN_PROGRESS";
      } else {
        List<String> prereqs = chunk.getPrerequisites().stream().map(LearningModule::getId).toList();
        status = prereqs.isEmpty() || completedChunks.containsAll(prereqs) ? "UNLOCKED" : "LOCKED";
      }

      result.add(new ModuleHealth(
          chunk.getId(), chunk.getTitle(), chunk.getGlyph(),
          status, avgStrength, healthColor,
          subs.size(), count,
          chunk.getTier() != null ? chunk.getTier().name() : "APPRENTICE"
      ));
    }
    return result;
  }

}
