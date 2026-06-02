package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.ai.domain.AnswerPair;
import com.ambravate.arcane.academy.ai.domain.GradeResult;
import com.ambravate.arcane.academy.common.telemetry.service.TelemetryService;
import com.ambravate.arcane.academy.common.dto.BadgeDto;
import com.ambravate.arcane.academy.practice.dto.CodeRunResponse;
import com.ambravate.arcane.academy.practice.dto.SoloSubmitRequest;
import com.ambravate.arcane.academy.common.domain.EncodingPhase;
import com.ambravate.arcane.academy.common.domain.ReviewSession;
import com.ambravate.arcane.academy.common.domain.SessionType;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonPracticeType;
import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.SoloAssessmentType;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.UserLearnerProfile;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.practice.repository.ReviewSessionRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.auth.repository.UserTrackProfileRepository;
import com.ambravate.arcane.academy.common.domain.UserTrackProfile;
import com.ambravate.arcane.academy.ai.service.AiMentorService;
import com.ambravate.arcane.academy.common.events.UserEngagedEvent;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import com.ambravate.arcane.academy.ai.service.RetrievalService;
import com.ambravate.arcane.academy.ai.service.SpacingService;

import com.ambravate.arcane.academy.practice.domain.PracticeResult;
import com.ambravate.arcane.academy.practice.domain.RetrievalCheckResult;
import com.ambravate.arcane.academy.practice.domain.LessonSession;
import com.ambravate.arcane.academy.practice.domain.SoloAssessmentResult;
import com.ambravate.arcane.academy.practice.domain.TestResult;
import com.ambravate.arcane.academy.practice.runner.CodeExecutionPort;
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
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncodingService {

    private final LessonRepository lessonRepository;
    private final LearningModuleRepository moduleRepository;
    private final UserChunkProgressRepository progressRepository;
    private final ReviewSessionRepository reviewSessionRepository;
    private final UserRepository userRepository;
    private final UserLearnerProfileRepository learnerProfileRepository;
    private final UserTrackProfileRepository topicProfileRepository;
    private final CodeExecutionPort codeRunner;
    private final AiMentorService aiMentorService;
    private final RetrievalService retrievalService;
    private final SpacingService spacingService;
    private final GamificationFacade gamification;
    private final ApplicationEventPublisher eventPublisher;
    private final TelemetryService telemetry;
    private final ObjectMapper objectMapper;
    // Phase 3 — guided step engine
    private final GuidedStepService guidedStepService;
    // Phase 4 — solo assessment
    private final KeywordScoringService keywordScoringService;

    private static final List<LearnerPath> TIER_PROGRESSION = List.of(
            LearnerPath.APPRENTICE, LearnerPath.JUNIOR, LearnerPath.SENIOR, LearnerPath.LEAD);

    @Transactional
    public LessonSession startLesson(String userId, String lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException("Lesson not found: " + lessonId));

        LearningModule parentModule = moduleRepository.findById(lesson.getModuleId())
                .orElseThrow(() -> new IllegalStateException("Module not found: " + lesson.getModuleId()));
        List<String> prereqIds = parentModule.getPrerequisites().stream().map(LearningModule::getId).toList();

        boolean needsProgressCheck = !prereqIds.isEmpty() || lesson.getSortOrder() > 1;
        Set<String> completedLessonIds = needsProgressCheck
                ? progressRepository.findByUserId(userId).stream()
                        .filter(p -> p.getStatus() == LessonStatus.COMPLETE
                                  || p.getStatus() == LessonStatus.SKIPPED)
                        .map(UserChunkProgress::getLessonId)
                        .collect(Collectors.toSet())
                : Collections.emptySet();

        if (!prereqIds.isEmpty()) {
            Map<String, List<Lesson>> prereqLessonsByModule = lessonRepository.findByModuleIdIn(prereqIds)
                    .stream()
                    .collect(Collectors.groupingBy(Lesson::getModuleId));
            boolean allPrereqsMet = prereqIds.stream().allMatch(prereqModuleId -> {
                List<Lesson> prereqLessons = prereqLessonsByModule.getOrDefault(prereqModuleId, List.of());
                return !prereqLessons.isEmpty()
                        && prereqLessons.stream().allMatch(l -> completedLessonIds.contains(l.getId()));
            });
            if (!allPrereqsMet) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Complete prerequisite modules before starting this one.");
            }
        }

        if (lesson.getSortOrder() > 1) {
            List<Lesson> priorLessons = lessonRepository
                    .findByModuleIdOrderBySortOrderAsc(lesson.getModuleId())
                    .stream()
                    .filter(l -> l.getSortOrder() < lesson.getSortOrder())
                    .toList();
            boolean allPriorComplete = priorLessons.stream()
                    .allMatch(l -> completedLessonIds.contains(l.getId()));
            if (!allPriorComplete) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Complete earlier lessons in this module before advancing.");
            }
        }

        Optional<UserChunkProgress> existingProgress =
                progressRepository.findByUserIdAndLessonId(userId, lessonId);
        boolean isFirstStart = existingProgress.isEmpty();

        UserChunkProgress progress = existingProgress.orElseGet(() -> {
                    UserChunkProgress p = UserChunkProgress.builder()
                            .userId(userId)
                            .lessonId(lessonId)
                            .status(LessonStatus.IN_PROGRESS)
                            .currentPhase(EncodingPhase.HOOK)
                            .build();
                    return progressRepository.save(p);
                });

        if (progress.getStatus() == LessonStatus.NOT_STARTED) {
            progress.setStatus(LessonStatus.IN_PROGRESS);
            progress.setCurrentPhase(EncodingPhase.HOOK);
            progressRepository.save(progress);
        }

        if (isFirstStart) {
            telemetry.questStarted(userId, lessonId, lesson.getModuleId());
        }

        return new LessonSession(lesson, progress);
    }

    @Transactional
    public LessonSession advancePhase(String userId, String lessonId) {
        UserChunkProgress progress = progressRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElseThrow(() -> new IllegalStateException("No progress for " + lessonId));
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();

        boolean hasPracticeContent = lesson.getGuidedPracticeHtml() != null
                && !lesson.getGuidedPracticeHtml().isBlank();
        if (progress.getCurrentPhase() == EncodingPhase.GUIDED_PRACTICE) {
            if (guidedStepService.hasSteps(lessonId)) {
                // Phase 3 step engine: require all steps completed
                if (!guidedStepService.allStepsCompleted(userId, lessonId)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "Complete all guided steps before advancing.");
                }
            } else if (hasPracticeContent && !progress.isGuidedPracticePassed()) {
                // Legacy path: traditional guided practice submission
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Submit guided practice and pass all tests before advancing.");
            }
        }
        boolean hasSoloContent = lesson.getSoloPracticeHtml() != null
                && !lesson.getSoloPracticeHtml().isBlank();
        if (progress.getCurrentPhase() == EncodingPhase.SOLO_PRACTICE
                && hasSoloContent
                && !progress.isSoloPracticePassed()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Submit solo practice and pass all tests before advancing.");
        }

        if (progress.getCurrentPhase() == EncodingPhase.RETRIEVAL_CHECK
                && progress.getStatus() != LessonStatus.COMPLETE
                && !progress.isRetrievalCheckSubmitted()) {
            boolean hasQuestions = !retrievalService.generateRetrievalCheck(userId, lessonId).isEmpty();
            if (hasQuestions) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Submit the retrieval check before advancing.");
            }
            if (progress.getNextReviewAt() == null) {
                spacingService.updateSpacing(userId, lessonId, 1.0);
            }
        }

        boolean hasIntegration = lesson.getIntegrationPrompt() != null
                && !lesson.getIntegrationPrompt().isBlank();

        EncodingPhase next = switch (progress.getCurrentPhase()) {
            case HOOK -> EncodingPhase.EXPLANATION;
            case EXPLANATION -> EncodingPhase.GUIDED_PRACTICE;
            case GUIDED_PRACTICE -> {
                String solo = lesson.getSoloPracticeHtml();
                yield (solo != null && !solo.isBlank())
                        ? EncodingPhase.SOLO_PRACTICE
                        : EncodingPhase.RETRIEVAL_CHECK;
            }
            case SOLO_PRACTICE -> EncodingPhase.RETRIEVAL_CHECK;
            case RETRIEVAL_CHECK -> hasIntegration ? EncodingPhase.INTEGRATION : EncodingPhase.COMPLETE;
            case INTEGRATION -> EncodingPhase.COMPLETE;
            case COMPLETE -> EncodingPhase.COMPLETE;
        };

        boolean transitionsToComplete = next == EncodingPhase.COMPLETE
                && progress.getStatus() != LessonStatus.COMPLETE;

        progress.setCurrentPhase(next);
        if (next == EncodingPhase.COMPLETE) {
            progress.setStatus(LessonStatus.COMPLETE);
            progress.setCompletedAt(Instant.now());
        }
        progressRepository.save(progress);

        log.info("[Encoding] Phase advanced | user={} lesson={} phase={}", userId, lessonId, next);

        if (transitionsToComplete) {
            telemetry.questCompleted(userId, lessonId, lesson.getModuleId(), lesson.getXpReward());
            advanceLearnerPathIfTierComplete(userId, lesson);
        }

        return new LessonSession(lesson, progress);
    }

    private void advanceLearnerPathIfTierComplete(String userId, Lesson completedLesson) {
        try {
            LearningModule parentModule = moduleRepository.findById(completedLesson.getModuleId()).orElse(null);
            if (parentModule == null || parentModule.getTier() == null) return;

            LearnerPath completedTier = parentModule.getTier();
            String trackId = parentModule.getTrackId();

            int completedIdx = TIER_PROGRESSION.indexOf(completedTier);
            if (completedIdx < 0) return;

            List<String> tierModuleIds = moduleRepository.findByTrackIdOrderBySortOrderAsc(trackId).stream()
                    .filter(m -> completedTier.equals(m.getTier()))
                    .map(LearningModule::getId)
                    .toList();
            List<Lesson> tierLessons = lessonRepository.findByModuleIdIn(tierModuleIds);
            if (tierLessons.isEmpty()) return;

            Set<String> doneLessonIds = progressRepository.findByUserId(userId).stream()
                    .filter(p -> p.getStatus() == LessonStatus.COMPLETE || p.getStatus() == LessonStatus.SKIPPED)
                    .map(UserChunkProgress::getLessonId)
                    .collect(Collectors.toSet());

            boolean tierComplete = tierLessons.stream().allMatch(l -> doneLessonIds.contains(l.getId()));
            if (!tierComplete) return;

            int nextIdx = completedIdx + 1;
            if (nextIdx >= TIER_PROGRESSION.size()) return;

            LearnerPath nextTier = TIER_PROGRESSION.get(nextIdx);

            if ("java".equals(trackId)) {
                UserLearnerProfile profile = learnerProfileRepository.findByUserId(userId).orElse(null);
                if (profile == null) return;
                int currentIdx = TIER_PROGRESSION.indexOf(profile.getCurrentPath());
                if (currentIdx < 0 || completedIdx >= currentIdx) {
                    profile.setCurrentPath(nextTier);
                    learnerProfileRepository.save(profile);
                    log.info("[Encoding] Java tier complete — advanced | user={} tier={} next={}",
                            userId, completedTier, nextTier);
                }
            } else {
                UserTrackProfile trackProfile = topicProfileRepository
                        .findByUserIdAndTrackId(userId, trackId).orElse(null);
                if (trackProfile == null) return;
                LearnerPath current = trackProfile.getCurrentTier() != null
                        ? trackProfile.getCurrentTier() : LearnerPath.APPRENTICE;
                int currentIdx = TIER_PROGRESSION.indexOf(current);
                if (currentIdx < 0 || completedIdx >= currentIdx) {
                    trackProfile.setCurrentTier(nextTier);
                    topicProfileRepository.save(trackProfile);
                    log.info("[Encoding] Track tier complete — advanced | user={} track={} tier={} next={}",
                            userId, trackId, completedTier, nextTier);
                }
            }
        } catch (Exception e) {
            log.warn("[Encoding] Failed to advance learner path after tier completion: {}", e.getMessage());
        }
    }

    @Transactional
    public PracticeResult submitGuidedPractice(String userId, String lessonId, String code) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException("Lesson not found: " + lessonId));
        UserChunkProgress progress = progressRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElseThrow(() -> new IllegalStateException("No progress record for " + lessonId));

        if (lesson.getPracticeType() == LessonPracticeType.NONE) {
            PracticeResult result = gradeWrittenPractice(userId, lesson, code, false);
            if (result.allPassed()) {
                progress.setGuidedPracticePassed(true);
                progressRepository.save(progress);
            }
            return result;
        }

        List<Map<String, Object>> testCases = parseTestCases(lesson.getGuidedPracticeTestsJson());
        if (testCases.isEmpty()) {
            progress.setGuidedPracticePassed(true);
            progressRepository.save(progress);
            return new PracticeResult(true, List.of(), 0, null, null, List.of());
        }

        CodeRunResponse probe = codeRunner.run(code,
                (String) testCases.getFirst().getOrDefault("input", null));

        if (probe.getStatus() == CodeRunResponse.RunStatus.COMPILE_ERROR) {
            String feedback = aiMentorService.explainCompileError(
                    lesson.getTitle(), "Java", code, probe.getError());
            return new PracticeResult(false, List.of(), 0, feedback, "COMPILE_ERROR", List.of());
        }

        if (probe.getStatus() == CodeRunResponse.RunStatus.RUNTIME_ERROR
                || probe.getStatus() == CodeRunResponse.RunStatus.TIMEOUT) {
            String feedback = aiMentorService.explainRuntimeError(
                    lesson.getTitle(), "Java", code,
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

        int xpEarned = 0;
        String mentorFeedback = null;
        List<BadgeDto> newBadges = List.of();

        if (allPassed) {
            progress.setGuidedPracticePassed(true);
            progressRepository.save(progress);
            xpEarned = awardXp(userId, lessonId, lesson.getXpReward());
            newBadges = gamification.evaluateAndAwardBadges(userId,
                    progressRepository.findByUserId(userId),
                    reviewSessionRepository.findByUserIdOrderByStartedAtDesc(userId));
        } else {
            mentorFeedback = aiMentorService.getFeedback(lesson.getTitle(), "Java",
                    "", code, String.join(", ", failedLabels));
        }

        return new PracticeResult(allPassed, results, xpEarned, mentorFeedback,
                allPassed ? null : "TEST_FAILURE", newBadges);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SOLO PRACTICE — Phase 4 dispatch
    // ─────────────────────────────────────────────────────────────────────────

    @Transactional
    public SoloAssessmentResult submitSoloPractice(
            String userId, String lessonId, SoloSubmitRequest request) {

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException("Lesson not found: " + lessonId));
        UserChunkProgress progress = progressRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElseThrow(() -> new IllegalStateException("No progress record for " + lessonId));

        SoloAssessmentType type = lesson.getSoloAssessmentType();
        if (type == null) type = SoloAssessmentType.DETERMINISTIC;

        return switch (type) {
            case DETERMINISTIC  -> soloDeterministic(userId, lessonId, lesson, progress, request);
            case RUBRIC_REFLECTION -> soloRubric(userId, lesson, progress,
                    request.getAnswer(), request.getCheckedRubricItems(), request.getConfidence());
            case PATTERN_MATCH  -> soloPatternMatch(userId, lesson, progress, request.getAnswer());
            case AI_REVIEW      -> soloAiReview(userId, lessonId, lesson, progress, request.getAnswer());
        };
    }

    /** Returns the number of AI reviews remaining for the current month. */
    public int getAiReviewsRemaining(String userId, String domainId) {
        String month = YearMonth.now().toString();
        if ("java".equals(domainId)) {
            return learnerProfileRepository.findByUserId(userId)
                    .map(p -> !month.equals(p.getAiReviewQuotaMonth())
                            ? KeywordScoringService.AI_REVIEW_MONTHLY_QUOTA
                            : Math.max(0, KeywordScoringService.AI_REVIEW_MONTHLY_QUOTA
                                            - p.getAiReviewUsesThisMonth()))
                    .orElse(KeywordScoringService.AI_REVIEW_MONTHLY_QUOTA);
        }
        return topicProfileRepository.findByUserIdAndTrackId(userId, domainId)
                .map(p -> !month.equals(p.getAiReviewQuotaMonth())
                        ? KeywordScoringService.AI_REVIEW_MONTHLY_QUOTA
                        : Math.max(0, KeywordScoringService.AI_REVIEW_MONTHLY_QUOTA
                                        - p.getAiReviewUsesThisMonth()))
                .orElse(KeywordScoringService.AI_REVIEW_MONTHLY_QUOTA);
    }

    // ── DETERMINISTIC ─────────────────────────────────────────────────────────

    private SoloAssessmentResult soloDeterministic(
            String userId, String lessonId,
            Lesson lesson, UserChunkProgress progress,
            SoloSubmitRequest request) {

        // Written-response path (practiceType == NONE)
        if (lesson.getPracticeType() == LessonPracticeType.NONE) {
            String answer = request.getAnswer() != null ? request.getAnswer()
                    : (request.getCode() != null ? request.getCode() : "");
            PracticeResult pr = gradeWrittenPractice(userId, lesson, answer, true);
            if (pr.allPassed()) {
                progress.setSoloPracticePassed(true);
                progressRepository.save(progress);
            }
            return practiceToSolo(pr);
        }

        // Code-test path
        String code = request.getCode() != null ? request.getCode() : "";
        List<Map<String, Object>> testCases = parseTestCases(lesson.getGuidedPracticeTestsJson());
        if (testCases.isEmpty()) {
            progress.setSoloPracticePassed(true);
            progressRepository.save(progress);
            return new SoloAssessmentResult(
                    true, List.of(), 0, null, null, List.of(), null, null, List.of(), 0, false);
        }

        CodeRunResponse probe = codeRunner.run(code,
                (String) testCases.getFirst().getOrDefault("input", null));

        if (probe.getStatus() == CodeRunResponse.RunStatus.COMPILE_ERROR) {
            String fb = aiMentorService.explainCompileError(lesson.getTitle(), "Java", code, probe.getError());
            return new SoloAssessmentResult(
                    false, List.of(), 0, fb, "COMPILE_ERROR", List.of(), null, null, List.of(), 0, false);
        }
        if (probe.getStatus() == CodeRunResponse.RunStatus.RUNTIME_ERROR
                || probe.getStatus() == CodeRunResponse.RunStatus.TIMEOUT) {
            String fb = aiMentorService.explainRuntimeError(lesson.getTitle(), "Java", code,
                    probe.getError() != null ? probe.getError() : "Timeout — possible infinite loop");
            return new SoloAssessmentResult(
                    false, List.of(), 0, fb, "RUNTIME_ERROR", List.of(), null, null, List.of(), 0, false);
        }

        List<TestResult> results = new ArrayList<>();
        boolean allPassed = true;
        List<String> failedLabels = new ArrayList<>();
        for (Map<String, Object> tc : testCases) {
            String label    = (String) tc.get("label");
            String input    = (String) tc.getOrDefault("input", null);
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
            mentorFeedback = aiMentorService.getFeedback(lesson.getTitle(), "Java",
                    "", code, String.join(", ", failedLabels));
        }
        return new SoloAssessmentResult(allPassed, results, 0, mentorFeedback,
                allPassed ? null : "TEST_FAILURE", List.of(), null, null, List.of(), 0, false);
    }

    /** Converts legacy PracticeResult to SoloAssessmentResult for the NONE path. */
    private SoloAssessmentResult practiceToSolo(PracticeResult pr) {
        return new SoloAssessmentResult(
                pr.allPassed(), pr.testResults(), pr.xpEarned(), pr.mentorFeedback(),
                pr.errorType(), pr.newBadges(), null, null, List.of(), 0, false);
    }

    // ── RUBRIC_REFLECTION ─────────────────────────────────────────────────────

    private SoloAssessmentResult soloRubric(
            String userId,
            Lesson lesson, UserChunkProgress progress,
            String answer, List<String> checkedItems, String confidence) {

        // Rubric-reflection always passes — it's a self-reflection tool
        if (confidence != null && !confidence.isBlank()) {
            progress.setSoloConfidence(confidence);
        }
        progress.setSoloPracticePassed(true);
        progressRepository.save(progress);

        int xp = awardXp(userId, lesson.getId() + "-solo-rubric", lesson.getXpReward());
        List<BadgeDto> badges = gamification.evaluateAndAwardBadges(userId,
                progressRepository.findByUserId(userId),
                reviewSessionRepository.findByUserIdOrderByStartedAtDesc(userId));

        String feedback = buildRubricFeedback(lesson, checkedItems);
        return new SoloAssessmentResult(
                true, List.of(), xp, feedback, null, badges,
                null, lesson.getSoloModelAnswerHtml(), List.of(), 0, false);
    }

    private String buildRubricFeedback(Lesson lesson, List<String> checkedItems) {
        if (checkedItems == null || checkedItems.isEmpty()) {
            return "Master Velan reviews your self-assessment. " +
                   "\"Honest reflection is the foundation of mastery. " +
                   "Study the model answer carefully and note what you included or missed.\"";
        }
        return "Master Velan approves of your self-assessment. " +
               "\"You have checked " + checkedItems.size() + " rubric item"
               + (checkedItems.size() == 1 ? "" : "s") + ". " +
               "Compare your work against the model answer to deepen your understanding.\"";
    }

    // ── PATTERN_MATCH ─────────────────────────────────────────────────────────

    private SoloAssessmentResult soloPatternMatch(
            String userId,
            Lesson lesson, UserChunkProgress progress,
            String answer) {

        List<String> keywords = parseStringList(lesson.getKeywordsJson());
        KeywordScoringService.KeywordScore score = keywordScoringService.score(answer, keywords);

        boolean passed = score.isPassing();
        if (passed) {
            progress.setSoloPracticePassed(true);
            progressRepository.save(progress);
        }

        int xp = 0;
        List<BadgeDto> badges = List.of();
        if (passed) {
            xp = awardXp(userId, lesson.getId() + "-solo-pattern", lesson.getXpReward());
            badges = gamification.evaluateAndAwardBadges(userId,
                    progressRepository.findByUserId(userId),
                    reviewSessionRepository.findByUserIdOrderByStartedAtDesc(userId));
        }

        String feedback = buildPatternFeedback(score);
        return new SoloAssessmentResult(
                passed, List.of(), xp, feedback, passed ? null : "TEXT_CHECK_FAILURE", badges,
                score.band().name(), lesson.getSoloModelAnswerHtml(), score.matchedKeywords(), 0, false);
    }

    private String buildPatternFeedback(KeywordScoringService.KeywordScore score) {
        return switch (score.band()) {
            case EXCELLENT -> "Master Velan is impressed. \"Excellent work, young wizard — you have "
                    + "woven " + score.matched() + " of the " + score.total() + " key concepts "
                    + "into your answer. Your understanding is clear.\"";
            case GOOD      -> "Master Velan nods thoughtfully. \"A good response — you have covered "
                    + score.matched() + " of " + score.total() + " key concepts. "
                    + "Review the model answer to see what else you could have included.\"";
            case WEAK      -> "Master Velan shakes his head gently. \"Your answer touches only "
                    + score.matched() + " of the " + score.total() + " key concepts I look for. "
                    + "Read the lesson vocabulary again and try to weave more of those terms "
                    + "naturally into your explanation.\"";
        };
    }

    // ── AI_REVIEW ─────────────────────────────────────────────────────────────

    private SoloAssessmentResult soloAiReview(
            String userId, String lessonId,
            Lesson lesson, UserChunkProgress progress,
            String answer) {

        String domainId = moduleRepository.findById(lesson.getModuleId())
                .map(LearningModule::getTrackId).orElse("java");

        int remaining = getAiReviewsRemaining(userId, domainId);

        // Downgrade to rubric-reflection when quota exhausted
        if (remaining <= 0) {
            log.info("[Encoding] AI review quota exhausted — downgrading to rubric | user={} lesson={}", userId, lessonId);
            return soloRubric(userId, lesson, progress, answer, List.of(), null);
        }

        // Consume one quota unit
        incrementAiReview(userId, domainId);
        remaining = Math.max(0, remaining - 1);

        AiMentorService.SoloAiReview review = aiMentorService.reviewSoloPractice(
                lesson.getTitle(), lesson.getSoloPracticeHtml(), answer);

        if (review.passed()) {
            progress.setSoloPracticePassed(true);
            progressRepository.save(progress);
        }

        int xp = 0;
        List<BadgeDto> badges = List.of();
        if (review.passed()) {
            xp = awardXp(userId, lessonId + "-solo-ai", lesson.getXpReward());
            badges = gamification.evaluateAndAwardBadges(userId,
                    progressRepository.findByUserId(userId),
                    reviewSessionRepository.findByUserIdOrderByStartedAtDesc(userId));
        }

        return new SoloAssessmentResult(
                review.passed(), List.of(), xp, review.feedback(),
                review.passed() ? null : "TEXT_CHECK_FAILURE", badges,
                null, lesson.getSoloModelAnswerHtml(), List.of(), remaining, true);
    }

    // ── AI quota helpers ──────────────────────────────────────────────────────

    private void incrementAiReview(String userId, String domainId) {
        String month = YearMonth.now().toString();
        if ("java".equals(domainId)) {
            learnerProfileRepository.findByUserId(userId).ifPresent(p -> {
                if (!month.equals(p.getAiReviewQuotaMonth())) {
                    p.setAiReviewQuotaMonth(month);
                    p.setAiReviewUsesThisMonth(0);
                }
                p.setAiReviewUsesThisMonth(p.getAiReviewUsesThisMonth() + 1);
                learnerProfileRepository.save(p);
            });
        } else {
            topicProfileRepository.findByUserIdAndTrackId(userId, domainId).ifPresent(p -> {
                if (!month.equals(p.getAiReviewQuotaMonth())) {
                    p.setAiReviewQuotaMonth(month);
                    p.setAiReviewUsesThisMonth(0);
                }
                p.setAiReviewUsesThisMonth(p.getAiReviewUsesThisMonth() + 1);
                topicProfileRepository.save(p);
            });
        }
    }

    private List<String> parseStringList(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return List.of();
        }
    }

    @Transactional
    public RetrievalCheckResult submitRetrievalCheck(String userId, String lessonId,
                                                      List<AnswerPair> answers) {
        GradeResult graded = retrievalService.gradeAnswers(answers);
        ReviewSession session = retrievalService.saveSession(userId, SessionType.RETRIEVAL_CHECK, graded);

        spacingService.updateSpacing(userId, lessonId, graded.score());

        UserChunkProgress progress = progressRepository.findByUserIdAndLessonId(userId, lessonId).orElseThrow();
        progress.setRetrievalCheckSubmitted(true);

        boolean passed = graded.score() >= 0.6;
        int xpEarned = 0;
        List<BadgeDto> newBadges = List.of();

        if (passed) {
            Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
            boolean hasIntegration = lesson != null
                    && lesson.getIntegrationPrompt() != null
                    && !lesson.getIntegrationPrompt().isBlank();

            boolean firstCompletion = progress.getStatus() != LessonStatus.COMPLETE;

            xpEarned = awardXp(userId, lessonId + "-retrieval", 25);
            newBadges = gamification.evaluateAndAwardBadges(userId,
                    progressRepository.findByUserId(userId),
                    reviewSessionRepository.findByUserIdOrderByStartedAtDesc(userId));

            if (hasIntegration) {
                progress.setCurrentPhase(EncodingPhase.INTEGRATION);
                progressRepository.save(progress);
            } else {
                progress.setCurrentPhase(EncodingPhase.COMPLETE);
                progress.setStatus(LessonStatus.COMPLETE);
                progress.setCompletedAt(Instant.now());
                progressRepository.save(progress);

                if (firstCompletion) {
                    telemetry.questCompleted(userId, lessonId,
                            lesson != null ? lesson.getModuleId() : null, xpEarned);
                    if (lesson != null) {
                        advanceLearnerPathIfTierComplete(userId, lesson);
                    }
                }
            }
        } else {
            progressRepository.save(progress);
        }

        return new RetrievalCheckResult(graded.score(), graded.correct(), graded.total(),
                graded.results(), passed, xpEarned, newBadges,
                passed ? null : "Score below 60%. Consider re-encoding this lesson.");
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

    private PracticeResult gradeWrittenPractice(String userId, Lesson lesson, String response, boolean solo) {
        String answer = response == null ? "" : response.trim();
        String lowerAnswer = answer.toLowerCase(Locale.ROOT);
        int wordCount = answer.isBlank() ? 0 : answer.split("\\s+").length;
        int sentenceCount = answer.isBlank() ? 0 : answer.split("[.!?]+").length;

        Set<String> expectedTerms = expectedTerms(lesson);
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
                    || lowerAnswer.contains(" — ")
                    || lowerAnswer.contains(" -- ");
            results.add(new TestResult(
                    "Explains rather than lists",
                    sentenceCount >= 3 && hasExplanatoryConnector,
                    sentenceCount >= 3 && hasExplanatoryConnector
                            ? "Includes explanation and reasoning."
                            : "Add more explanation: use words like 'because', 'therefore', 'however', or 'this means' to link your ideas.",
                    "3+ sentences with reasoning"
            ));
        }

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
            xpEarned = awardXp(userId, lesson.getId(), lesson.getXpReward());
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

    private Set<String> expectedTerms(Lesson lesson) {
        String source = String.join(" ",
                Optional.ofNullable(lesson.getTitle()).orElse(""),
                Optional.ofNullable(lesson.getExplanationHtml()).orElse(""),
                Optional.ofNullable(lesson.getGuidedPracticeHtml()).orElse(""));

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
