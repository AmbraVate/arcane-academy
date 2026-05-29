package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.common.dto.BadgeDto;
import com.ambravate.arcane.academy.practice.dto.SqlSubmitRequest;
import com.ambravate.arcane.academy.practice.dto.SubmitResponse;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.practice.repository.ReviewSessionRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.common.events.UserEngagedEvent;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Awards XP for SQL practice submissions.
 *
 * <p>The frontend runs the tests inside a sandboxed iframe via sql.js (SQLite-WASM
 * via CDN) against an in-memory database seeded with the sub-chunk's setup SQL. Per-test results
 * are posted back here. The backend performs a lightweight structural sanity check on the SQL
 * source before awarding XP.
 *
 * <p>Same trust domain as {@link ReactPracticeService} â€” documented in CLAUDE.md Â§0.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SqlPracticeService {

  /**
   * Minimum source length before we consider the submission a real attempt.
   */
  private static final int MIN_SOURCE_LENGTH = 6;

  private final LessonRepository lessonRepository;
  private final UserRepository userRepository;
  private final UserChunkProgressRepository progressRepository;
  private final ReviewSessionRepository reviewSessionRepository;
  private final GamificationFacade gamification;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public SubmitResponse submit(String userId, String lessonId, SqlSubmitRequest request) {
    Lesson subChunk = lessonRepository.findById(lessonId)
        .orElseThrow(() -> new IllegalArgumentException("SubChunk not found: " + lessonId));

    // â”€â”€ 1. Structural sanity check on source â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    String code = request.getCode() == null ? "" : request.getCode().trim();
    if (code.length() < MIN_SOURCE_LENGTH || !looksLikeSql(code)) {
      log.info(
          "[SQL] Rejected â€” source too short or not SQL-like | user={} subChunk={}",
          userId, lessonId
      );
      return buildResponse(
          false,
          List.of(SubmitResponse.TestResult.builder()
              .label("Source check")
              .passed(false)
              .actualOutput("Submission is empty or doesn't look like SQL")
              .expectedOutput("A SELECT (or INSERT/UPDATE/DELETE) statement")
              .build()),
          0, List.of()
      );
    }

    // â”€â”€ 2. Trust the client-reported test results â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    List<SqlSubmitRequest.ClientTestResult> clientResults =
        request.getClientTestResults() == null ? List.of() : request.getClientTestResults();

    List<SubmitResponse.TestResult> results = new ArrayList<>();
    boolean allPassed = !clientResults.isEmpty();

    for (SqlSubmitRequest.ClientTestResult ctr : clientResults) {
      results.add(SubmitResponse.TestResult.builder()
          .label(ctr.getLabel() == null ? "Test" : ctr.getLabel())
          .passed(ctr.isPassed())
          .actualOutput(ctr.getActual() == null ? "" : ctr.getActual())
          .expectedOutput("Pass")
          .build());
      if (!ctr.isPassed()) {
        allPassed = false;
      }
    }

    // â”€â”€ 3. Award XP if all passed â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    int xpEarned = 0;
    List<BadgeDto> newBadges = List.of();
    if (allPassed) {
      xpEarned = awardXp(userId, lessonId, subChunk.getXpReward());
      newBadges = gamification.evaluateAndAwardBadges(userId,
              progressRepository.findByUserId(userId),
              reviewSessionRepository.findByUserIdOrderByStartedAtDesc(userId));
      log.info("[SQL] All tests passed | user={} subChunk={} xp={}", userId, lessonId, xpEarned);
    } else {
      log.info(
          "[SQL] Tests failed | user={} subChunk={} failures={}",
          userId, lessonId,
          results.stream()
              .filter(r -> !r.isPassed())
              .map(SubmitResponse.TestResult::getLabel)
              .toList()
      );
    }

    return buildResponse(allPassed, results, xpEarned, newBadges);
  }

  /**
   * Lightweight SQL detection. We're not parsing â€” we just want to reject obviously non-SQL
   * submissions like "abc" or "<html>". The query must contain a recognisable SQL verb keyword.
   */
  private boolean looksLikeSql(String code) {
    String upper = code.toUpperCase();
    return upper.contains("SELECT")
        || upper.contains("INSERT")
        || upper.contains("UPDATE")
        || upper.contains("DELETE")
        || upper.contains("WITH ");  // for CTEs
  }

  /**
   * Awards XP exactly once per sub-chunk per user â€” same idempotency pattern as
   * {@link ReactPracticeService} and {@link TailwindPracticeService}.
   */
  private int awardXp(String userId, String lessonId, int xp) {
    Optional<UserChunkProgress> progressOpt =
        progressRepository.findByUserIdAndLessonId(userId, lessonId);

    boolean alreadyAwarded = progressOpt
        .map(p -> p.getLastScore() >= 1.0 || p.getStatus() == LessonStatus.COMPLETE)
        .orElse(false);
    if (alreadyAwarded) {
      return 0;
    }

    eventPublisher.publishEvent(new UserEngagedEvent(userId));
    User user = userRepository.findById(userId).orElseThrow();
    user.setTotalXp(user.getTotalXp() + xp);
    user.setRank(EncodingService.calculateRank(user.getTotalXp()));
    userRepository.save(user);

    UserChunkProgress progress = progressOpt.orElseGet(() ->
        UserChunkProgress.builder().userId(userId).lessonId(lessonId).build());
    progress.setLastScore(1.0);
    progressRepository.save(progress);

    return xp;
  }

  private SubmitResponse buildResponse(
      boolean allPassed,
      List<SubmitResponse.TestResult> results,
      int xpEarned,
      List<BadgeDto> newBadges
  ) {
    return SubmitResponse.builder()
        .allPassed(allPassed)
        .testResults(results)
        .xpEarned(xpEarned)
        .newBadges(newBadges)
        .build();
  }
}
