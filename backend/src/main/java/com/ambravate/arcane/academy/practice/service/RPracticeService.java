package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.common.dto.BadgeDto;
import com.ambravate.arcane.academy.practice.dto.RSubmitRequest;
import com.ambravate.arcane.academy.practice.dto.SubmitResponse;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.content.repository.SubChunkRepository;
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
 * Awards XP for R practice submissions.
 *
 * <p>The frontend runs the tests inside a sandboxed iframe via WebR (R compiled
 * to WebAssembly via CDN) against a fresh R environment seeded with the
 * sub-chunk's setup data. Per-test results are posted back here. The backend
 * performs a lightweight structural sanity check on the R source before
 * awarding XP.
 *
 * <p>Same trust domain as {@link SqlPracticeService} — documented in CLAUDE.md §0.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RPracticeService {

  /**
   * Minimum source length before we consider the submission a real attempt.
   * R is more terse than SQL so we set a lower threshold.
   */
  private static final int MIN_SOURCE_LENGTH = 3;

  private final SubChunkRepository subChunkRepository;
  private final UserRepository userRepository;
  private final UserChunkProgressRepository progressRepository;
  private final GamificationFacade gamification;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public SubmitResponse submit(String userId, String subChunkId, RSubmitRequest request) {
    SubChunk subChunk = subChunkRepository.findById(subChunkId)
        .orElseThrow(() -> new IllegalArgumentException("SubChunk not found: " + subChunkId));

    // ── 1. Structural sanity check on source ────────────────────────────────
    String code = request.getCode() == null ? "" : request.getCode().trim();
    if (code.length() < MIN_SOURCE_LENGTH || !looksLikeR(code)) {
      log.info(
          "[R] Rejected — source too short or not R-like | user={} subChunk={}",
          userId, subChunkId
      );
      return buildResponse(
          false,
          List.of(SubmitResponse.TestResult.builder()
              .label("Source check")
              .passed(false)
              .actualOutput("Submission is empty or doesn't look like R")
              .expectedOutput("An R expression, assignment, or function call")
              .build()),
          0, List.of()
      );
    }

    // ── 2. Trust the client-reported test results ───────────────────────────
    List<RSubmitRequest.ClientTestResult> clientResults =
        request.getClientTestResults() == null ? List.of() : request.getClientTestResults();

    List<SubmitResponse.TestResult> results = new ArrayList<>();
    boolean allPassed = !clientResults.isEmpty();

    for (RSubmitRequest.ClientTestResult ctr : clientResults) {
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

    // ── 3. Award XP if all passed ────────────────────────────────────────────
    int xpEarned = 0;
    List<BadgeDto> newBadges = List.of();
    if (allPassed) {
      xpEarned = awardXp(userId, subChunkId, subChunk.getXpReward());
      newBadges = gamification.evaluateAndAwardBadges(userId);
      log.info("[R] All tests passed | user={} subChunk={} xp={}", userId, subChunkId, xpEarned);
    } else {
      log.info(
          "[R] Tests failed | user={} subChunk={} failures={}",
          userId, subChunkId,
          results.stream()
              .filter(r -> !r.isPassed())
              .map(SubmitResponse.TestResult::getLabel)
              .toList()
      );
    }

    return buildResponse(allPassed, results, xpEarned, newBadges);
  }

  /**
   * Lightweight R detection. We're not parsing — we just want to reject obviously
   * non-R submissions like "abc" or "<html>". The code must contain at least one
   * R-flavoured token: assignment operator, common function call, pipe, or
   * statistical keyword.
   */
  private boolean looksLikeR(String code) {
    String lower = code.toLowerCase();
    return code.contains("<-")
        || code.contains("=")
        || code.contains("%>%")
        || code.contains("|>")
        || code.contains("(")
        || lower.contains("mean")
        || lower.contains("sum")
        || lower.contains("library")
        || lower.contains("function")
        || lower.contains("data.frame")
        || lower.contains("c(");
  }

  /**
   * Awards XP exactly once per sub-chunk per user — same idempotency pattern as
   * {@link SqlPracticeService}.
   */
  private int awardXp(String userId, String subChunkId, int xp) {
    Optional<UserChunkProgress> progressOpt =
        progressRepository.findByUserIdAndSubChunkId(userId, subChunkId);

    boolean alreadyAwarded = progressOpt
        .map(p -> p.getLastScore() >= 1.0 || p.getStatus() == SubChunkStatus.COMPLETE)
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
        UserChunkProgress.builder().userId(userId).subChunkId(subChunkId).build());
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
