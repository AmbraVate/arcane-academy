package com.ambravate.polymath.academy.service;

import com.ambravate.polymath.academy.dto.BadgeDto;
import com.ambravate.polymath.academy.dto.CodeRunResponse;
import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import com.ambravate.polymath.academy.runner.JavaCodeRunner;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncodingService {

    private final SubChunkRepository subChunkRepository;
    private final UserChunkProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final JavaCodeRunner codeRunner;
    private final AiMentorService aiMentorService;
    private final RetrievalService retrievalService;
    private final SpacingService spacingService;
    private final StreakService streakService;
    private final BadgeService badgeService;
    private final TelemetryService telemetry;
    private final ObjectMapper objectMapper;

    /**
     * Start or resume a sub-chunk. Creates UserChunkProgress if not exists.
     */
    @Transactional
    public SubChunkSession startSubChunk(String userId, String subChunkId) {
        SubChunk subChunk = subChunkRepository.findById(subChunkId)
                .orElseThrow(() -> new NoSuchElementException("SubChunk not found: " + subChunkId));

        boolean isFirstStart = progressRepository.findByUserIdAndSubChunkId(userId, subChunkId).isEmpty();

        UserChunkProgress progress = progressRepository.findByUserIdAndSubChunkId(userId, subChunkId)
                .orElseGet(() -> {
                    UserChunkProgress p = UserChunkProgress.builder()
                            .userId(userId)
                            .subChunkId(subChunkId)
                            .status(SubChunkStatus.IN_PROGRESS)
                            .currentPhase(EncodingPhase.HOOK)
                            .build();
                    return progressRepository.save(p);
                });

        if (progress.getStatus() == SubChunkStatus.NOT_STARTED) {
            progress.setStatus(SubChunkStatus.IN_PROGRESS);
            progress.setCurrentPhase(EncodingPhase.HOOK);
            progressRepository.save(progress);
        }

        // Telemetry: emit quest_started only on the first start (resumes shouldn't double-count)
        if (isFirstStart) {
            telemetry.questStarted(userId, subChunkId, subChunk.getChunkId());
        }

        return new SubChunkSession(subChunk, progress);
    }

    /**
     * Advance to the next encoding phase.
     */
    @Transactional
    public SubChunkSession advancePhase(String userId, String subChunkId) {
        UserChunkProgress progress = progressRepository.findByUserIdAndSubChunkId(userId, subChunkId)
                .orElseThrow(() -> new IllegalStateException("No progress for " + subChunkId));
        SubChunk subChunk = subChunkRepository.findById(subChunkId).orElseThrow();

        EncodingPhase next = switch (progress.getCurrentPhase()) {
            case HOOK -> EncodingPhase.EXPLANATION;
            case EXPLANATION -> EncodingPhase.GUIDED_PRACTICE;
            case GUIDED_PRACTICE -> {
                // Only enter SOLO_PRACTICE if content is defined for this sub-chunk
                String solo = subChunk.getSoloPracticeHtml();
                yield (solo != null && !solo.isBlank())
                        ? EncodingPhase.SOLO_PRACTICE
                        : EncodingPhase.RETRIEVAL_CHECK;
            }
            case SOLO_PRACTICE -> EncodingPhase.RETRIEVAL_CHECK;
            case RETRIEVAL_CHECK -> EncodingPhase.COMPLETE;
            case COMPLETE -> EncodingPhase.COMPLETE;
        };

        boolean transitionsToComplete = next == EncodingPhase.COMPLETE
                && progress.getStatus() != SubChunkStatus.COMPLETE; // emit telemetry only on first completion

        progress.setCurrentPhase(next);
        if (next == EncodingPhase.COMPLETE) {
            progress.setStatus(SubChunkStatus.COMPLETE);
            progress.setCompletedAt(Instant.now());
        }
        progressRepository.save(progress);

        log.info("[Encoding] Phase advanced | user={} subChunk={} phase={}", userId, subChunkId, next);

        if (transitionsToComplete) {
            telemetry.questCompleted(userId, subChunkId, subChunk.getChunkId(), subChunk.getXpReward());
        }

        return new SubChunkSession(subChunk, progress);
    }

    /**
     * Submit guided practice code — compile and run against tests.
     * Same pattern as the old QuestService.evaluateSubmission.
     */
    @Transactional
    public PracticeResult submitGuidedPractice(String userId, String subChunkId, String code) {
        SubChunk subChunk = subChunkRepository.findById(subChunkId)
                .orElseThrow(() -> new NoSuchElementException("SubChunk not found: " + subChunkId));

        List<Map<String, Object>> testCases = parseTestCases(subChunk.getGuidedPracticeTestsJson());
        if (testCases.isEmpty()) {
            return new PracticeResult(true, List.of(), 0, null, null, List.of());
        }

        // Probe first test for compile/runtime errors
        CodeRunResponse probe = codeRunner.run(code,
                (String) testCases.getFirst().getOrDefault("input", null));

        if (probe.getStatus() == CodeRunResponse.RunStatus.COMPILE_ERROR) {
            String feedback = aiMentorService.explainCompileError(
                    subChunk.getTitle(), "Java", code, probe.getError());
            return new PracticeResult(false, List.of(), 0, feedback, "COMPILE_ERROR", List.of());
        }

        if (probe.getStatus() == CodeRunResponse.RunStatus.RUNTIME_ERROR
                || probe.getStatus() == CodeRunResponse.RunStatus.TIMEOUT) {
            String feedback = aiMentorService.explainRuntimeError(
                    subChunk.getTitle(), "Java", code,
                    probe.getError() != null ? probe.getError() : "Timeout — possible infinite loop");
            return new PracticeResult(false, List.of(), 0, feedback, "RUNTIME_ERROR", List.of());
        }

        // Run all tests
        List<TestResult> results = new ArrayList<>();
        boolean allPassed = true;
        List<String> failedLabels = new ArrayList<>();

        for (Map<String, Object> tc : testCases) {
            String label = (String) tc.get("label");
            String input = (String) tc.getOrDefault("input", null);
            String expected = (String) tc.get("expected");
            CodeRunResponse run = codeRunner.run(code, input);
            String actual = run.getOutput() != null ? run.getOutput().trim() : "";
            boolean passed = actual.contains(expected.trim());
            results.add(new TestResult(label, passed, actual, expected));
            if (!passed) { allPassed = false; failedLabels.add(label); }
        }

        int xpEarned = 0;
        String mentorFeedback = null;
        List<BadgeDto> newBadges = List.of();

        if (allPassed) {
            xpEarned = awardXp(userId, subChunkId, subChunk.getXpReward());
            newBadges = badgeService.evaluateAndAward(userId);
        } else {
            mentorFeedback = aiMentorService.getFeedback(subChunk.getTitle(), "Java",
                    "", code, String.join(", ", failedLabels));
        }

        return new PracticeResult(allPassed, results, xpEarned, mentorFeedback,
                allPassed ? null : "TEST_FAILURE", newBadges);
    }

    /**
     * Submit solo practice code — same tests as guided practice, but no XP award
     * (XP was already earned during GUIDED_PRACTICE).
     */
    @Transactional
    public PracticeResult submitSoloPractice(String userId, String subChunkId, String code) {
        SubChunk subChunk = subChunkRepository.findById(subChunkId)
                .orElseThrow(() -> new NoSuchElementException("SubChunk not found: " + subChunkId));

        List<Map<String, Object>> testCases = parseTestCases(subChunk.getGuidedPracticeTestsJson());
        if (testCases.isEmpty()) {
            return new PracticeResult(true, List.of(), 0, null, null, List.of());
        }

        CodeRunResponse probe = codeRunner.run(code,
                (String) testCases.getFirst().getOrDefault("input", null));

        if (probe.getStatus() == CodeRunResponse.RunStatus.COMPILE_ERROR) {
            String feedback = aiMentorService.explainCompileError(
                    subChunk.getTitle(), "Java", code, probe.getError());
            return new PracticeResult(false, List.of(), 0, feedback, "COMPILE_ERROR", List.of());
        }
        if (probe.getStatus() == CodeRunResponse.RunStatus.RUNTIME_ERROR
                || probe.getStatus() == CodeRunResponse.RunStatus.TIMEOUT) {
            String feedback = aiMentorService.explainRuntimeError(
                    subChunk.getTitle(), "Java", code,
                    probe.getError() != null ? probe.getError() : "Timeout — possible infinite loop");
            return new PracticeResult(false, List.of(), 0, feedback, "RUNTIME_ERROR", List.of());
        }

        List<TestResult> results = new ArrayList<>();
        boolean allPassed = true;
        List<String> failedLabels = new ArrayList<>();

        for (Map<String, Object> tc : testCases) {
            String label = (String) tc.get("label");
            String input = (String) tc.getOrDefault("input", null);
            String expected = (String) tc.get("expected");
            CodeRunResponse run = codeRunner.run(code, input);
            String actual = run.getOutput() != null ? run.getOutput().trim() : "";
            boolean passed = actual.contains(expected.trim());
            results.add(new TestResult(label, passed, actual, expected));
            if (!passed) { allPassed = false; failedLabels.add(label); }
        }

        String mentorFeedback = null;
        if (!allPassed) {
            mentorFeedback = aiMentorService.getFeedback(subChunk.getTitle(), "Java",
                    "", code, String.join(", ", failedLabels));
        }

        // No XP — already earned during GUIDED_PRACTICE
        return new PracticeResult(allPassed, results, 0, mentorFeedback,
                allPassed ? null : "TEST_FAILURE", List.of());
    }

    /**
     * Submit retrieval check answers, grade them, update SM-2 spacing.
     */
    @Transactional
    public RetrievalCheckResult submitRetrievalCheck(String userId, String subChunkId,
                                                      List<RetrievalService.AnswerPair> answers) {
        RetrievalService.GradeResult graded = retrievalService.gradeAnswers(answers);
        ReviewSession session = retrievalService.saveSession(userId, SessionType.RETRIEVAL_CHECK, graded);

        // Update SM-2 spacing
        spacingService.updateSpacing(userId, subChunkId, graded.score());

        // If score >= 60%, mark retrieval as passed and advance to COMPLETE
        boolean passed = graded.score() >= 0.6;
        int xpEarned = 0;
        List<BadgeDto> newBadges = List.of();

        if (passed) {
            UserChunkProgress progress = progressRepository.findByUserIdAndSubChunkId(userId, subChunkId).orElseThrow();
            boolean firstCompletion = progress.getStatus() != SubChunkStatus.COMPLETE;
            progress.setCurrentPhase(EncodingPhase.COMPLETE);
            progress.setStatus(SubChunkStatus.COMPLETE);
            progress.setCompletedAt(Instant.now());
            progressRepository.save(progress);

            xpEarned = awardXp(userId, subChunkId + "-retrieval", 25);
            newBadges = badgeService.evaluateAndAward(userId);

            if (firstCompletion) {
                SubChunk sc = subChunkRepository.findById(subChunkId).orElse(null);
                telemetry.questCompleted(userId, subChunkId,
                        sc != null ? sc.getChunkId() : null, xpEarned);
            }
        }

        return new RetrievalCheckResult(graded.score(), graded.correct(), graded.total(),
                graded.results(), passed, xpEarned, newBadges,
                passed ? null : "Score below 60%. Consider re-encoding this sub-chunk.");
    }

    private int awardXp(String userId, String itemId, int xp) {
        streakService.updateStreak(userId);
        User user = userRepository.findById(userId).orElseThrow();
        int newXp = user.getTotalXp() + xp;
        user.setTotalXp(newXp);
        user.setRank(calculateRank(newXp));
        userRepository.save(user);
        return xp;
    }

    public static String calculateRank(int xp) {
        if (xp >= 11000) return "Lord Magus";
        if (xp >= 8000)  return "Magus";
        if (xp >= 6500)  return "Archmage";
        if (xp >= 4000)  return "Mage";
        if (xp >= 2000)  return "Adept";
        if (xp >= 800)   return "Apprentice";
        return "Novice";
    }

    private List<Map<String, Object>> parseTestCases(String json) {
        if (json == null) return List.of();
        try { return objectMapper.readValue(json, new TypeReference<>() {}); } catch (Exception e) { return List.of(); }
    }

    // Inner records for return types
    public record SubChunkSession(SubChunk subChunk, UserChunkProgress progress) {}
    public record TestResult(String label, boolean passed, String actualOutput, String expectedOutput) {}
    public record PracticeResult(boolean allPassed, List<TestResult> testResults, int xpEarned,
                                  String mentorFeedback, String errorType, List<BadgeDto> newBadges) {}
    public record RetrievalCheckResult(double score, int correct, int total,
                                        List<RetrievalService.QuestionResult> results,
                                        boolean passed, int xpEarned, List<BadgeDto> newBadges,
                                        String recommendation) {}
}
