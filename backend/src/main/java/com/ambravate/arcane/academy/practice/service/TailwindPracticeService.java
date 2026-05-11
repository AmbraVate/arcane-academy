package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.common.dto.BadgeDto;
import com.ambravate.arcane.academy.common.dto.SubmitResponse;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.repository.SubChunkRepository;
import com.ambravate.arcane.academy.common.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.common.repository.UserRepository;
import com.ambravate.arcane.academy.gamification.service.BadgeService;
import com.ambravate.arcane.academy.gamification.service.StreakService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Validates student Tailwind HTML submissions by parsing the markup with Jsoup and checking that
 * required CSS classes are present on the correct elements.
 * <p>
 * Test spec JSON format (stored in sub_chunks.guided_practice_tests_json):
 * <pre>
 * [
 *   {"label": "Card has white background", "selector": ".card", "requiredClass": "bg-white"},
 *   {"label": "Heading is bold",           "selector": "h1",    "requiredClass": "font-bold"},
 *   {"label": "Button is blue",            "selector": "button","requiredClass": "bg-blue-500"}
 * ]
 * </pre>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TailwindPracticeService {

  private final SubChunkRepository subChunkRepository;
  private final UserRepository userRepository;
  private final UserChunkProgressRepository progressRepository;
  private final BadgeService badgeService;
  private final StreakService streakService;
  private final ObjectMapper objectMapper;

  @Transactional
  public SubmitResponse submit(String userId, String subChunkId, String html) {
    SubChunk subChunk = subChunkRepository.findById(subChunkId)
        .orElseThrow(() -> new IllegalArgumentException("SubChunk not found: " + subChunkId));

    List<Map<String, Object>> specs = parseSpecs(subChunk.getGuidedPracticeTestsJson());
    if (specs.isEmpty()) {
      // No test specs — award XP immediately
      int xp = awardXp(userId, subChunkId, subChunk.getXpReward());
      return buildResponse(true, List.of(), xp, List.of());
    }

    Document doc = Jsoup.parseBodyFragment(html == null ? "" : html);
    List<SubmitResponse.TestResult> results = new ArrayList<>();
    boolean allPassed = true;

    for (Map<String, Object> spec : specs) {
      String label = (String) spec.getOrDefault("label", "Test");
      String selector = (String) spec.getOrDefault("selector", "*");
      String requiredClass = (String) spec.getOrDefault("requiredClass", "");

      Elements matched = doc.select(selector);
      boolean hasElement = !matched.isEmpty();
      boolean hasClass = hasElement && matched.stream()
          .anyMatch(el -> el.classNames().contains(requiredClass));

      String actual;
      if (!hasElement) {
        actual = "Element '" + selector + "' not found";
      } else {
        actual = "classes: " + Objects.requireNonNull(matched.first()).className();
      }

      results.add(SubmitResponse.TestResult.builder()
          .label(label)
          .passed(hasClass)
          .actualOutput(actual)
          .expectedOutput("'" + selector + "' has class '" + requiredClass + "'")
          .build());

      if (!hasClass) {
        allPassed = false;
      }
    }

    int xpEarned = 0;
    List<BadgeDto> newBadges = List.of();
    if (allPassed) {
      xpEarned = awardXp(userId, subChunkId, subChunk.getXpReward());
      newBadges = badgeService.evaluateAndAward(userId);
      log.info(
          "[Tailwind] All tests passed | user={} subChunk={} xp={}",
          userId,
          subChunkId,
          xpEarned
      );
    } else {
      log.info(
          "[Tailwind] Tests failed | user={} subChunk={} failures={}",
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
   * Awards XP exactly once per sub-chunk per user.
   * <p>
   * Idempotency: when XP is first awarded we set lastScore = 1.0 on the progress record as a
   * "guided-practice-xp-awarded" marker. Subsequent calls see this flag and return 0.
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

    // Mark XP as awarded so re-submissions in the same session don't double-count
    UserChunkProgress progress = progressOpt.orElseGet(() ->
        UserChunkProgress.builder().userId(userId).subChunkId(subChunkId).build());
    progress.setLastScore(1.0);
    progressRepository.save(progress);

    return xp;
  }

  private List<Map<String, Object>> parseSpecs(String json) {
    if (json == null || json.isBlank()) {
      return List.of();
    }
    try {
      return objectMapper.readValue(
          json, new TypeReference<>() {
          }
      );
    } catch (Exception e) {
      log.warn("[Tailwind] Failed to parse test specs: {}", e.getMessage());
      return List.of();
    }
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
