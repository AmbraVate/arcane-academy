package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.ai.domain.AnswerPair;
import com.ambravate.arcane.academy.ai.domain.GradeResult;
import com.ambravate.arcane.academy.common.telemetry.service.TelemetryService;
import com.ambravate.arcane.academy.common.dto.BadgeDto;
import com.ambravate.arcane.academy.practice.dto.CodeRunResponse;
import com.ambravate.arcane.academy.common.domain.EncodingPhase;
import com.ambravate.arcane.academy.common.domain.ReviewSession;
import com.ambravate.arcane.academy.common.domain.SessionType;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkPracticeType;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.UserLearnerProfile;
import com.ambravate.arcane.academy.content.repository.ChunkRepository;
import com.ambravate.arcane.academy.content.repository.SubChunkRepository;
import com.ambravate.arcane.academy.practice.repository.ReviewSessionRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.auth.repository.UserTopicProfileRepository;
import com.ambravate.arcane.academy.common.domain.UserTopicProfile;
import com.ambravate.arcane.academy.ai.service.AiMentorService;
import com.ambravate.arcane.academy.common.events.UserEngagedEvent;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import com.ambravate.arcane.academy.ai.service.RetrievalService;
import com.ambravate.arcane.academy.ai.service.SpacingService;

import com.ambravate.arcane.academy.practice.domain.PracticeResult;
import com.ambravate.arcane.academy.practice.domain.RetrievalCheckResult;
import com.ambravate.arcane.academy.practice.domain.SubChunkSession;
import com.ambravate.arcane.academy.practice.domain.TestResult;
import com.ambravate.arcane.academy.practice.runner.JavaCodeRunner;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncodingService {

    private final SubChunkRepository subChunkRepository;
    private final ChunkRepository chunkRepository;
    private final UserChunkProgressRepository progressRepository;
    private final ReviewSessionRepository reviewSessionRepository;
    private final UserRepository userRepository;
    private final UserLearnerProfileRepository learnerProfileRepository;
    private final UserTopicProfileRepository topicProfileRepository;
    private final JavaCodeRunner codeRunner;
    private final AiMentorService aiMentorService;
    private final RetrievalService retrievalService;
    private final SpacingService spacingService;
    private final GamificationFacade gamification;
    private final ApplicationEventPublisher eventPublisher;
    private final TelemetryService telemetry;
    private final ObjectMapper objectMapper;

    /** Ordered tier progression — new canonical four-tier scheme. */
    private static final List<LearnerPath> TIER_PROGRESSION = List.of(
            LearnerPath.APPRENTICE, LearnerPath.JUNIOR, LearnerPath.SENIOR, LearnerPath.LEAD);

    /**
     * Start or resume a sub-chunk. Creates UserChunkProgress if not exists.
     */
    @Transactional
    public SubChunkSession startSubChunk(String userId, String subChunkId) {
        SubChunk subChunk = subChunkRepository.findById(subChunkId)
                .orElseThrow(() -> new NoSuchElementException("SubChunk not found: " + subChunkId));

        // Check 1 — Parent chunk prerequisites met (chunk must not be LOCKED)
        Chunk parentChunk = chunkRepository.findById(subChunk.getChunkId())
                .orElseThrow(() -> new IllegalStateException("Chunk not found: " + subChunk.getChunkId()));
        List<String> prereqIds = parentChunk.getPrerequisites().stream().map(Chunk::getId).toList();

        // Fetch completed sub-chunk IDs once — reused by both the prereq check and the
        // sequential-ordering check, avoiding a duplicate DB round-trip.
        boolean needsProgressCheck = !prereqIds.isEmpty() || subChunk.getSortOrder() > 1;
        Set<String> completedSubChunkIds = needsProgressCheck
                ? progressRepository.findByUserId(userId).stream()
                        .filter(p -> p.getStatus() == SubChunkStatus.COMPLETE
                                  || p.getStatus() == SubChunkStatus.SKIPPED)
                        .map(UserChunkProgress::getSubChunkId)
                        .collect(Collectors.toSet())
                : Collections.emptySet();

        if (!prereqIds.isEmpty()) {
            // Batch-load all prerequisite sub-chunks in a single query (avoids N+1 per prereq).
            Map<String, List<SubChunk>> prereqSubsByChunk = subChunkRepository.findByChunkIdIn(prereqIds)
                    .stream()
                    .collect(Collectors.groupingBy(SubChunk::getChunkId));
            // A prerequisite chunk is "fully completed" when every one of its sub-chunks is done.
            boolean allPrereqsMet = prereqIds.stream().allMatch(prereqChunkId -> {
                List<SubChunk> prereqSubs = prereqSubsByChunk.getOrDefault(prereqChunkId, List.of());
                return !prereqSubs.isEmpty()
                        && prereqSubs.stream().allMatch(sc -> completedSubChunkIds.contains(sc.getId()));
            });
            if (!allPrereqsMet) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Complete prerequisite chunks before starting this one.");
            }
        }

        // Check 2 — Sequential sub-chunk ordering (prior siblings in this chunk must be complete)
        if (subChunk.getSortOrder() > 1) {
            List<SubChunk> priorSubChunks = subChunkRepository
                    .findByChunkIdOrderBySortOrderAsc(subChunk.getChunkId())
                    .stream()
                    .filter(sc -> sc.getSortOrder() < subChunk.getSortOrder())
                    .toList();
            boolean allPriorComplete = priorSubChunks.stream()
                    .allMatch(sc -> completedSubChunkIds.contains(sc.getId()));
            if (!allPriorComplete) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Complete earlier lessons in this module before advancing.");
            }
        }

        // Single fetch for both first-start detection and progress initialisation.
        Optional<UserChunkProgress> existingProgress =
                progressRepository.findByUserIdAndSubChunkId(userId, subChunkId);
        boolean isFirstStart = existingProgress.isEmpty();

        UserChunkProgress progress = existingProgress.orElseGet(() -> {
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

        // Guard: prevent advancing past coding phases without a passing submission.
        // Only enforced when the sub-chunk actually has practice content — old seeder-based
        // chunks with null guidedPracticeHtml are not gated so they remain completable.
        boolean hasPracticeContent = subChunk.getGuidedPracticeHtml() != null
                && !subChunk.getGuidedPracticeHtml().isBlank();
        if (progress.getCurrentPhase() == EncodingPhase.GUIDED_PRACTICE
                && hasPracticeContent
                && !progress.isGuidedPracticePassed()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Submit guided practice and pass all tests before advancing.");
        }
        boolean hasSoloContent = subChunk.getSoloPracticeHtml() != null
                && !subChunk.getSoloPracticeHtml().isBlank();
        if (progress.getCurrentPhase() == EncodingPhase.SOLO_PRACTICE
                && hasSoloContent
                && !progress.isSoloPracticePassed()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Submit solo practice and pass all tests before advancing.");
        }

        // Guard: block advancing past RETRIEVAL_CHECK if the check hasn't been submitted yet
        if (progress.getCurrentPhase() == EncodingPhase.RETRIEVAL_CHECK
                && progress.getStatus() != SubChunkStatus.COMPLETE
                && !progress.isRetrievalCheckSubmitted()) {
            boolean hasQuestions = !retrievalService.generateRetrievalCheck(userId, subChunkId).isEmpty();
            if (hasQuestions) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Submit the retrieval check before advancing.");
            }
            // No questions — seed spacing with a perfect score so reviews get scheduled
            if (progress.getNextReviewAt() == null) {
                spacingService.updateSpacing(userId, subChunkId, 1.0);
            }
        }

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
            advanceLearnerPathIfTierComplete(userId, subChunk);
        }

        return new SubChunkSession(subChunk, progress);
    }

    /**
     * After a sub-chunk completes, check whether the user has now finished every sub-chunk in
     * the parent chunk's tier for this topic. If so, advance their currentPath to the next tier
     * so the TopicPage correctly shows their progression (BUG-12 fix).
     */
    private void advanceLearnerPathIfTierComplete(String userId, SubChunk completedSubChunk) {
        try {
            Chunk parentChunk = chunkRepository.findById(completedSubChunk.getChunkId()).orElse(null);
            if (parentChunk == null || parentChunk.getTier() == null) return;

            LearnerPath completedTier = parentChunk.getTier();
            String topicId = parentChunk.getTopicId();

            int completedIdx = TIER_PROGRESSION.indexOf(completedTier);
            if (completedIdx < 0) return; // Legacy or unknown tier — skip

            // Check every sub-chunk in this tier is done
            List<String> tierChunkIds = chunkRepository.findByTopicIdOrderBySortOrderAsc(topicId).stream()
                    .filter(c -> completedTier.equals(c.getTier()))
                    .map(Chunk::getId)
                    .toList();
            List<SubChunk> tierSubChunks = subChunkRepository.findByChunkIdIn(tierChunkIds);
            if (tierSubChunks.isEmpty()) return;

            Set<String> doneSubChunkIds = progressRepository.findByUserId(userId).stream()
                    .filter(p -> p.getStatus() == SubChunkStatus.COMPLETE || p.getStatus() == SubChunkStatus.SKIPPED)
                    .map(UserChunkProgress::getSubChunkId)
                    .collect(Collectors.toSet());

            boolean tierComplete = tierSubChunks.stream().allMatch(sc -> doneSubChunkIds.contains(sc.getId()));
            if (!tierComplete) return;

            int nextIdx = completedIdx + 1;
            if (nextIdx >= TIER_PROGRESSION.size()) return; // Already at LEAD — no higher tier

            LearnerPath nextTier = TIER_PROGRESSION.get(nextIdx);

            if ("java".equals(topicId)) {
                UserLearnerProfile profile = learnerProfileRepository.findByUserId(userId).orElse(null);
                if (profile == null) return;
                // Normalise any legacy tier value before comparing position
                int currentIdx = TIER_PROGRESSION.indexOf(normaliseTier(profile.getCurrentPath()));
                if (currentIdx < 0 || completedIdx >= currentIdx) {
                    profile.setCurrentPath(nextTier);
                    learnerProfileRepository.save(profile);
                    log.info("[Encoding] Java tier complete — advanced | user={} tier={} next={}",
                            userId, completedTier, nextTier);
                }
            } else {
                UserTopicProfile topicProfile = topicProfileRepository
                        .findByUserIdAndTopicId(userId, topicId).orElse(null);
                if (topicProfile == null) return;
                LearnerPath current = topicProfile.getCurrentTier() != null
                        ? topicProfile.getCurrentTier() : LearnerPath.APPRENTICE;
                int currentIdx = TIER_PROGRESSION.indexOf(current);
                if (currentIdx < 0 || completedIdx >= currentIdx) {
                    topicProfile.setCurrentTier(nextTier);
                    topicProfileRepository.save(topicProfile);
                    log.info("[Encoding] Topic tier complete — advanced | user={} topic={} tier={} next={}",
                            userId, topicId, completedTier, nextTier);
                }
            }
        } catch (Exception e) {
            log.warn("[Encoding] Failed to advance learner path after tier completion: {}", e.getMessage());
        }
    }

    /** Maps deprecated LearnerPath values to their current equivalents. */
    private static LearnerPath normaliseTier(LearnerPath tier) {
        if (tier == null) return LearnerPath.APPRENTICE;
        return switch (tier) {
            case FOUNDATION   -> LearnerPath.APPRENTICE;
            case PRACTITIONER -> LearnerPath.JUNIOR;
            case EXPERT       -> LearnerPath.SENIOR;
            default           -> tier;
        };
    }

    /**
     * Submit guided practice code — compile and run against tests.
     * Same pattern as the old QuestService.evaluateSubmission.
     */
    @Transactional
    public PracticeResult submitGuidedPractice(String userId, String subChunkId, String code) {
        SubChunk subChunk = subChunkRepository.findById(subChunkId)
                .orElseThrow(() -> new NoSuchElementException("SubChunk not found: " + subChunkId));
        // Load progress once; reused for all pass-marking calls below.
        UserChunkProgress progress = progressRepository.findByUserIdAndSubChunkId(userId, subChunkId)
                .orElseThrow(() -> new IllegalStateException("No progress record for " + subChunkId));

        if (subChunk.getPracticeType() == SubChunkPracticeType.NONE) {
            PracticeResult result = gradeWrittenPractice(userId, subChunk, code, false);
            if (result.allPassed()) {
                progress.setGuidedPracticePassed(true);
                progressRepository.save(progress);
            }
            return result;
        }

        List<Map<String, Object>> testCases = parseTestCases(subChunk.getGuidedPracticeTestsJson());
        if (testCases.isEmpty()) {
            // No tests defined — auto-pass and mark as passed.
            progress.setGuidedPracticePassed(true);
            progressRepository.save(progress);
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
            // Mark as passed before badge evaluation so the fresh findByUserId reflects it.
            progress.setGuidedPracticePassed(true);
            progressRepository.save(progress);
            xpEarned = awardXp(userId, subChunkId, subChunk.getXpReward());
            newBadges = gamification.evaluateAndAwardBadges(userId,
                    progressRepository.findByUserId(userId),
                    reviewSessionRepository.findByUserIdOrderByStartedAtDesc(userId));
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
        // Load progress once; reused for all pass-marking calls below.
        UserChunkProgress progress = progressRepository.findByUserIdAndSubChunkId(userId, subChunkId)
                .orElseThrow(() -> new IllegalStateException("No progress record for " + subChunkId));

        if (subChunk.getPracticeType() == SubChunkPracticeType.NONE) {
            PracticeResult result = gradeWrittenPractice(userId, subChunk, code, true);
            if (result.allPassed()) {
                progress.setSoloPracticePassed(true);
                progressRepository.save(progress);
            }
            return result;
        }

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
        if (allPassed) {
            progress.setSoloPracticePassed(true);
            progressRepository.save(progress);
        } else {
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
                                                      List<AnswerPair> answers) {
        GradeResult graded = retrievalService.gradeAnswers(answers);
        ReviewSession session = retrievalService.saveSession(userId, SessionType.RETRIEVAL_CHECK, graded);

        // Update SM-2 spacing
        spacingService.updateSpacing(userId, subChunkId, graded.score());

        // Mark retrieval as submitted regardless of score so advancePhase() allows progression
        UserChunkProgress progress = progressRepository.findByUserIdAndSubChunkId(userId, subChunkId).orElseThrow();
        progress.setRetrievalCheckSubmitted(true);

        boolean passed = graded.score() >= 0.6;
        int xpEarned = 0;
        List<BadgeDto> newBadges = List.of();

        if (passed) {
            boolean firstCompletion = progress.getStatus() != SubChunkStatus.COMPLETE;
            progress.setCurrentPhase(EncodingPhase.COMPLETE);
            progress.setStatus(SubChunkStatus.COMPLETE);
            progress.setCompletedAt(Instant.now());
            progressRepository.save(progress);

            xpEarned = awardXp(userId, subChunkId + "-retrieval", 25);
            newBadges = gamification.evaluateAndAwardBadges(userId,
                    progressRepository.findByUserId(userId),
                    reviewSessionRepository.findByUserIdOrderByStartedAtDesc(userId));

            if (firstCompletion) {
                SubChunk sc = subChunkRepository.findById(subChunkId).orElse(null);
                telemetry.questCompleted(userId, subChunkId,
                        sc != null ? sc.getChunkId() : null, xpEarned);
                if (sc != null) {
                    advanceLearnerPathIfTierComplete(userId, sc);
                }
            }
        } else {
            progressRepository.save(progress);
        }

        return new RetrievalCheckResult(graded.score(), graded.correct(), graded.total(),
                graded.results(), passed, xpEarned, newBadges,
                passed ? null : "Score below 60%. Consider re-encoding this sub-chunk.");
    }

    private int awardXp(String userId, String itemId, int xp) {
        eventPublisher.publishEvent(new UserEngagedEvent(userId));
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

    /**
     * Returns true when the response looks like pseudocode / structured algorithm notation
     * rather than a prose essay.
     *
     * <p>Detection: 3 or more algorithm control-flow keywords present, matched
     * case-insensitively (uppercased before comparison) so that students who write
     * entirely lowercase pseudocode ("for each", "if x = 1", "return x") are classified
     * correctly. Space-padding ensures whole-word matching — " IF " won't trigger on "DIFFICULT".
     */
    private boolean looksLikePseudocode(String answer) {
        String upper = answer.toUpperCase(Locale.ROOT);
        String[] algorithmKeywords = {
            "ALGORITHM", " FOR ", " IF ", " SET ", " RETURN ", " PRINT ",
            " WHILE ", " FOREACH ", " ELSE ", " OUTPUT ", " INPUT ",
            " ASSIGN ", " BREAK ", " GOTO ", " GO TO ", "FOR EACH",
            " BEGIN ", " END ", " DO ", " THEN ", " STEP "
        };
        int matches = 0;
        for (String kw : algorithmKeywords) {
            if (upper.contains(kw)) {
                matches++;
                if (matches >= 3) return true;
            }
        }
        return false;
    }

    private PracticeResult gradeWrittenPractice(String userId, SubChunk subChunk, String response, boolean solo) {
        String answer = response == null ? "" : response.trim();
        String lowerAnswer = answer.toLowerCase(Locale.ROOT);
        int wordCount = answer.isBlank() ? 0 : answer.split("\\s+").length;
        int sentenceCount = answer.isBlank() ? 0 : answer.split("[.!?]+").length;

        Set<String> expectedTerms = expectedTerms(subChunk);
        long matchedTerms = expectedTerms.stream()
                .filter(term -> lowerAnswer.contains(term.toLowerCase(Locale.ROOT)))
                .count();

        int minWords = solo ? 70 : 45;
        int minTerms = Math.min(solo ? 6 : 4, Math.max(3, expectedTerms.size() / 4));

        List<TestResult> results = new ArrayList<>();
        results.add(new TestResult(
                solo ? "Independent written response" : "Guided written response",
                wordCount >= minWords,
                wordCount >= minWords
                        ? "Enough detail included (" + wordCount + " words)."
                        : "Add more detail: " + wordCount + "/" + minWords + " words.",
                minWords + "+ words"
        ));
        results.add(new TestResult(
                "Uses lesson vocabulary",
                matchedTerms >= minTerms,
                matchedTerms >= minTerms
                        ? "Used " + matchedTerms + " key terms from the lesson."
                        : "Use more lesson vocabulary: " + matchedTerms + "/" + minTerms + " key terms found.",
                minTerms + "+ key terms"
        ));

        // Check #3 — branch on response type:
        //   • Pseudocode / algorithm: check for structural elements (conditions + actions).
        //   • Prose / essay: check for explanatory connectors and sentence count.
        if (looksLikePseudocode(answer)) {
            boolean hasCondition = lowerAnswer.contains(" if ") || answer.startsWith("IF ")
                    || lowerAnswer.contains(" when ") || lowerAnswer.contains(" while ")
                    || lowerAnswer.contains(" for ") || lowerAnswer.contains(" foreach ")
                    || lowerAnswer.contains("for each");
            boolean hasAction = lowerAnswer.contains(" set ") || lowerAnswer.contains(" print ")
                    || lowerAnswer.contains(" return ") || lowerAnswer.contains(" output ")
                    || lowerAnswer.contains(" assign ") || lowerAnswer.contains(" purchase ")
                    || lowerAnswer.contains(" go to ") || lowerAnswer.contains(" goto ");
            boolean hasStructure = hasCondition && hasAction;
            results.add(new TestResult(
                    "Shows algorithmic structure",
                    hasStructure,
                    hasStructure
                            ? "Pseudocode includes control flow and action steps."
                            : "Include both a condition step (IF/FOR/WHILE) and an action step (SET/PRINT/RETURN) in your pseudocode.",
                    "Control flow + action steps"
            ));
        } else {
            boolean hasExplanatoryConnector = lowerAnswer.contains("because")
                    || lowerAnswer.contains(" since ")
                    || lowerAnswer.contains("therefore")
                    || lowerAnswer.contains("however")
                    || lowerAnswer.contains("which means")
                    || lowerAnswer.contains("this means")
                    || lowerAnswer.contains("as a result")
                    || lowerAnswer.contains("this suggests")
                    || lowerAnswer.contains("this explains")
                    || lowerAnswer.contains("in contrast")
                    || lowerAnswer.contains("whereas")
                    || lowerAnswer.contains("while")
                    || lowerAnswer.contains("although")
                    || lowerAnswer.contains(" — ")   // em-dash used as an explanatory connector
                    || lowerAnswer.contains(" -- ");  // double-hyphen equivalent
            results.add(new TestResult(
                    "Explains rather than lists",
                    sentenceCount >= 3 && hasExplanatoryConnector,
                    sentenceCount >= 3 && hasExplanatoryConnector
                            ? "Includes explanation and reasoning."
                            : "Add more explanation: use words like 'because', 'therefore', 'however', or 'this means' to link your ideas.",
                    "3+ sentences with reasoning"
            ));
        }

        // Solo prose check: pseudocode solo tasks demonstrate independence through a novel
        // algorithm, not through essay-style "example/case/evidence" language — skip it.
        if (solo && !looksLikePseudocode(answer)) {
            boolean hasIndependentApplication = lowerAnswer.contains("example")
                    || lowerAnswer.contains("case")
                    || lowerAnswer.contains("evidence")
                    || lowerAnswer.contains("would");
            results.add(new TestResult(
                    "Applies the idea independently",
                    hasIndependentApplication,
                    hasIndependentApplication
                            ? "Includes independent application language."
                            : "Add your own example, case, evidence, or what you would do next.",
                    "Own example or application"
            ));
        }

        boolean allPassed = results.stream().allMatch(TestResult::passed);
        int xpEarned = 0;
        List<BadgeDto> newBadges = List.of();
        String mentorFeedback = null;

        if (allPassed && !solo) {
            xpEarned = awardXp(userId, subChunk.getId(), subChunk.getXpReward());
            newBadges = gamification.evaluateAndAwardBadges(userId,
                    progressRepository.findByUserId(userId),
                    reviewSessionRepository.findByUserIdOrderByStartedAtDesc(userId));
        } else if (!allPassed) {
            if (looksLikePseudocode(answer)) {
                mentorFeedback = "Use the task checklist: include all required steps, use lesson vocabulary "
                        + "(ingredient names, threshold values, actions), and make sure your algorithm covers "
                        + "both the condition check (IF) and the action to take (SET/PRINT/RETURN). "
                        + (solo ? "For the solo challenge, write a fresh algorithm without copying the guided example." : "");
            } else {
                mentorFeedback = "Use the prompt as a checklist: write in full sentences, include the lesson vocabulary, "
                        + "and explain why the ideas matter. "
                        + (solo ? "For solo practice, add your own case or example rather than following the guided wording." : "");
            }
        }

        return new PracticeResult(allPassed, results, xpEarned, mentorFeedback,
                allPassed ? null : "TEXT_CHECK_FAILURE", newBadges);
    }

    private Set<String> expectedTerms(SubChunk subChunk) {
        String source = String.join(" ",
                Optional.ofNullable(subChunk.getTitle()).orElse(""),
                Optional.ofNullable(subChunk.getExplanationHtml()).orElse(""),
                Optional.ofNullable(subChunk.getGuidedPracticeHtml()).orElse(""));

        String text = source
                .replaceAll("<[^>]+>", " ")
                .replaceAll("&[a-zA-Z]+;", " ")
                .toLowerCase(Locale.ROOT);

        Set<String> stopWords = Set.of(
                "this", "that", "with", "from", "your", "they", "them", "then", "than",
                "into", "what", "when", "where", "which", "write", "lesson", "module",
                "practice", "guided", "solo", "because", "about", "after", "before",
                "should", "could", "would", "their", "there", "these", "those"
        );

        Set<String> terms = new LinkedHashSet<>();
        for (String token : text.split("[^a-z0-9-]+")) {
            if (token.length() >= 5 && !stopWords.contains(token)) {
                terms.add(token);
            }
            if (terms.size() >= 24) break;
        }

        if (terms.isEmpty()) {
            terms.add("psychology");
            terms.add("example");
            terms.add("evidence");
        }
        return terms;
    }

}
