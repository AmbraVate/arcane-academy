package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.common.dto.BadgeDto;
import com.ambravate.arcane.academy.practice.dto.ReactSubmitRequest;
import com.ambravate.arcane.academy.practice.dto.SubmitResponse;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.repository.SubChunkRepository;
import com.ambravate.arcane.academy.common.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.common.repository.UserRepository;
import com.ambravate.arcane.academy.gamification.service.BadgeService;
import com.ambravate.arcane.academy.gamification.service.StreakService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Awards XP for React practice submissions.
 *
 * <p>The frontend runs the tests inside a sandboxed iframe (React + Babel via CDN)
 * against the rendered DOM, and reports results back here. The backend performs a lightweight
 * structural sanity check on the source — non-empty, contains JSX markers — to guard against
 * trivial empty submissions. See {@link ReactSubmitRequest} for the rationale.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReactPracticeService {

  /**
   * Minimum source length before we consider the submission a real attempt.
   */
  private static final int MIN_SOURCE_LENGTH = 30;

  private final SubChunkRepository subChunkRepository;
  private final UserRepository userRepository;
  private final UserChunkProgressRepository progressRepository;
  private final BadgeService badgeService;
  private final StreakService streakService;

  @Transactional
  public SubmitResponse submit(String userId, String subChunkId, ReactSubmitRequest request) {
    SubChunk subChunk = subChunkRepository.findById(subChunkId)
        .orElseThrow(() -> new IllegalArgumentException("SubChunk not found: " + subChunkId));

    // ── 1. Structural sanity check on source ────────────────────────────────
    String code = request.getCode() == null ? "" : request.getCode();
    if (code.length() < MIN_SOURCE_LENGTH || !looksLikeJsx(code)) {
      log.info(
          "[React] Rejected — source too short or not JSX-like | user={} subChunk={}",
          userId, subChunkId
      );
      return buildResponse(
          false,
          List.of(SubmitResponse.TestResult.builder()
              .label("Source check")
              .passed(false)
              .actualOutput("Submission is empty or doesn't look like JSX")
              .expectedOutput("A complete component definition")
              .build()),
          0, List.of()
      );
    }

    // ── 2. Trust the client-reported test results ───────────────────────────
    List<ReactSubmitRequest.ClientTestResult> clientResults =
        request.getClientTestResults() == null ? List.of() : request.getClientTestResults();

    List<SubmitResponse.TestResult> results = new ArrayList<>();
    boolean allPassed = !clientResults.isEmpty();

    for (ReactSubmitRequest.ClientTestResult ctr : clientResults) {
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
      newBadges = badgeService.evaluateAndAward(userId);
      log.info(
          "[React] All tests passed | user={} subChunk={} xp={}",
          userId,
          subChunkId,
          xpEarned
      );
    } else {
      log.info(
          "[React] Tests failed | user={} subChunk={} failures={}",
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
   * Lightweight JSX detection. We're not parsing — we just want to reject obviously non-React
   * submissions like "abc" or "<html><body>".
   */
  private boolean looksLikeJsx(String code) {
    // At least one of these must be present:
    // - "return (" or "return <" — function components return JSX
    // - "createElement(" — explicit React.createElement (rare but valid)
    // - "useState" / "useEffect" / "function " + capital letter — component idioms
    return code.contains("return (")
        || code.contains("return <")
        || code.contains("React.createElement")
        || code.contains("useState")
        || code.contains("useEffect")
        || code.matches("(?s).*function\\s+[A-Z]\\w+.*")
        || code.matches("(?s).*const\\s+[A-Z]\\w+\\s*=.*=>.*");
  }

  /**
   * Awards XP exactly once per sub-chunk per user — same idempotency pattern as
   * {@link TailwindPracticeService}.
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

    streakService.updateStreak(userId);
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
