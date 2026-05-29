package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.common.dto.BadgeDto;
import com.ambravate.arcane.academy.practice.dto.ReactSubmitRequest;
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
 * Awards XP for React practice submissions.
 *
 * <p>The frontend runs the tests inside a sandboxed iframe (React + Babel via CDN)
 * against the rendered DOM, and reports results back here. The backend performs a lightweight
 * structural sanity check on the source â€” non-empty, contains JSX markers â€” to guard against
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

  private final LessonRepository lessonRepository;
  private final UserRepository userRepository;
  private final UserChunkProgressRepository progressRepository;
  private final ReviewSessionRepository reviewSessionRepository;
  private final GamificationFacade gamification;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public SubmitResponse submit(String userId, String lessonId, ReactSubmitRequest request) {
    Lesson subChunk = lessonRepository.findById(lessonId)
        .orElseThrow(() -> new IllegalArgumentException("SubChunk not found: " + lessonId));

    // â”€â”€ 1. Structural sanity check on source â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    String code = request.getCode() == null ? "" : request.getCode();
    if (code.length() < MIN_SOURCE_LENGTH || !looksLikeJsx(code)) {
      log.info(
          "[React] Rejected â€” source too short or not JSX-like | user={} subChunk={}",
          userId, lessonId
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

    // â”€â”€ 2. Trust the client-reported test results â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
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

    // â”€â”€ 3. Award XP if all passed â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    int xpEarned = 0;
    List<BadgeDto> newBadges = List.of();
    if (allPassed) {
      xpEarned = awardXp(userId, lessonId, subChunk.getXpReward());
      newBadges = gamification.evaluateAndAwardBadges(userId,
              progressRepository.findByUserId(userId),
              reviewSessionRepository.findByUserIdOrderByStartedAtDesc(userId));
      log.info(
          "[React] All tests passed | user={} subChunk={} xp={}",
          userId,
          lessonId,
          xpEarned
      );
    } else {
      log.info(
          "[React] Tests failed | user={} subChunk={} failures={}",
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
   * Lightweight JSX detection. We're not parsing â€” we just want to reject obviously non-React
   * submissions like "abc" or "<html><body>".
   */
  private boolean looksLikeJsx(String code) {
    // At least one of these must be present:
    // - "return (" or "return <" â€” function components return JSX
    // - "createElement(" â€” explicit React.createElement (rare but valid)
    // - "useState" / "useEffect" / "function " + capital letter â€” component idioms
    return code.contains("return (")
        || code.contains("return <")
        || code.contains("React.createElement")
        || code.contains("useState")
        || code.contains("useEffect")
        || code.matches("(?s).*function\\s+[A-Z]\\w+.*")
        || code.matches("(?s).*const\\s+[A-Z]\\w+\\s*=.*=>.*");
  }

  /**
   * Awards XP exactly once per sub-chunk per user â€” same idempotency pattern as
   * {@link TailwindPracticeService}.
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
