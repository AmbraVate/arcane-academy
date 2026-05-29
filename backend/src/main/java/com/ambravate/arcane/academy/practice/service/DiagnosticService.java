package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.ai.domain.AnswerPair;
import com.ambravate.arcane.academy.ai.domain.GradeResult;
import com.ambravate.arcane.academy.ai.domain.QuestionResult;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.practice.domain.DiagnosticResult;
import com.ambravate.arcane.academy.practice.domain.DiagnosticSession;
import com.ambravate.arcane.academy.common.domain.EncodingPhase;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.QuestionTier;
import com.ambravate.arcane.academy.common.domain.ReviewSession;
import com.ambravate.arcane.academy.common.domain.SessionType;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.domain.UserLearnerProfile;
import com.ambravate.arcane.academy.common.domain.UserTrackProfile;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.auth.repository.UserTrackProfileRepository;
import com.ambravate.arcane.academy.common.telemetry.service.TelemetryService;
import com.ambravate.arcane.academy.ai.service.RetrievalService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiagnosticService {

    private final LearningModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final QuestionRepository questionRepository;
    private final UserChunkProgressRepository progressRepository;
    private final UserLearnerProfileRepository profileRepository;
    private final UserTrackProfileRepository trackProfileRepository;
    private final RetrievalService retrievalService;
    private final TelemetryService telemetry;
    private final ObjectMapper objectMapper;

    // ─── Start ─────────────────────────────────────────────────────────────

    /** Java diagnostic (backwards-compatible). */
    public DiagnosticSession startEntryDiagnostic(String userId) {
        return startEntryDiagnostic(userId, "java");
    }

    /**
     * Generate entry diagnostic: 2 RECALL questions per chunk for the given track.
     */
    public DiagnosticSession startEntryDiagnostic(String userId, String trackId) {
        List<LearningModule> chunks = moduleRepository.findByTrackIdOrderBySortOrderAsc(trackId);

        List<Question> diagnosticQuestions = new ArrayList<>();
        for (LearningModule chunk : chunks) {
            List<Lesson> subChunks = lessonRepository.findByModuleIdOrderBySortOrderAsc(chunk.getId());
            List<String> lessonIds = subChunks.stream().map(Lesson::getId).toList();

            List<Question> recallQuestions = questionRepository.findByLessonIdIn(lessonIds).stream()
                    .filter(q -> q.getTier() == QuestionTier.RECALL)
                    .collect(Collectors.toList());

            Collections.shuffle(recallQuestions);
            diagnosticQuestions.addAll(recallQuestions.stream().limit(2).toList());
        }

        ReviewSession session = retrievalService.saveSession(userId, SessionType.DIAGNOSTIC,
                new GradeResult(0, 0, diagnosticQuestions.size(), List.of()));

        log.info("[Diagnostic] Started for user={} track={} with {} questions", userId, trackId, diagnosticQuestions.size());
        return new DiagnosticSession(session.getId(), diagnosticQuestions);
    }

    // ─── Submit ────────────────────────────────────────────────────────────

    /** Java diagnostic submit (backwards-compatible). */
    @Transactional
    public DiagnosticResult submitDiagnostic(String userId, List<AnswerPair> answers) {
        return submitDiagnostic(userId, answers, "java");
    }

    /**
     * Grade answers and record results. For Java, assigns FOUNDATION/PRACTITIONER/EXPERT path.
     * For other tracks, just stores the score and marks diagnostic complete.
     */
    @Transactional
    public DiagnosticResult submitDiagnostic(String userId, List<AnswerPair> answers, String trackId) {
        GradeResult graded = retrievalService.gradeAnswers(answers);

        // Map sub-chunk → chunk
        Map<String, String> subChunkToChunk = new HashMap<>();
        lessonRepository.findAll().forEach(sc -> subChunkToChunk.put(sc.getId(), sc.getModuleId()));

        Map<String, List<QuestionResult>> byChunk = graded.results().stream()
                .collect(Collectors.groupingBy(r -> subChunkToChunk.getOrDefault(r.lessonId(), "unknown")));

        Map<String, String> chunkRecommendations = new HashMap<>();
        for (var entry : byChunk.entrySet()) {
            long correct = entry.getValue().stream().filter(QuestionResult::correct).count();
            chunkRecommendations.put(entry.getKey(), correct >= 2 ? "SKIP" : correct == 1 ? "COMPRESS" : "FULL");
        }

        List<LearningModule> topicChunks = moduleRepository.findByTrackIdOrderBySortOrderAsc(trackId);
        topicChunks.forEach(c -> chunkRecommendations.putIfAbsent(c.getId(), "FULL"));

        // Determine path: FOUNDATION → ADVANCED → PRACTITIONER → EXPERT
        long skipCount = chunkRecommendations.values().stream().filter("SKIP"::equals).count();
        long totalChunks = chunkRecommendations.size();
        LearnerPath recommended;
        if (totalChunks > 0 && (double) skipCount / totalChunks > 0.9) {
            recommended = LearnerPath.EXPERT;
        } else if (totalChunks > 0 && (double) skipCount / totalChunks > 0.7) {
            recommended = LearnerPath.PRACTITIONER;
        } else if (totalChunks > 0 && (double) skipCount / totalChunks > 0.5) {
            recommended = LearnerPath.ADVANCED;
        } else {
            recommended = LearnerPath.FOUNDATION;
        }

        // Pre-create progress entries for skipped chunks
        for (LearningModule chunk : topicChunks) {
            String rec = chunkRecommendations.get(chunk.getId());
            for (Lesson sc : lessonRepository.findByModuleIdOrderBySortOrderAsc(chunk.getId())) {
                if (!progressRepository.existsByUserIdAndLessonId(userId, sc.getId())) {
                    LessonStatus status = "SKIP".equals(rec) ? LessonStatus.SKIPPED : LessonStatus.NOT_STARTED;
                    UserChunkProgress progress = UserChunkProgress.builder()
                            .userId(userId).lessonId(sc.getId()).status(status)
                            .currentPhase(status == LessonStatus.SKIPPED ? EncodingPhase.COMPLETE : EncodingPhase.HOOK)
                            .memoryStrength(status == LessonStatus.SKIPPED ? 0.8 : 0.0).build();
                    if (status == LessonStatus.SKIPPED) {
                        progress.setCompletedAt(Instant.now());
                        progress.setLastReviewedAt(Instant.now());
                        progress.setNextReviewAt(Instant.now().plusSeconds(86400 * 3));
                    }
                    progressRepository.save(progress);
                }
            }
        }

        String resultsJson = null;
        try { resultsJson = objectMapper.writeValueAsString(chunkRecommendations); }
        catch (Exception e) { log.warn("Failed to serialize diagnostic results", e); }

        Instant now = Instant.now();
        if ("java".equals(trackId)) {
            UserLearnerProfile profile = profileRepository.findByUserId(userId)
                    .orElse(UserLearnerProfile.aUserLearnerProfile().withUserId(userId).build());
            profile.setDiagnosticCompleted(true);
            profile.setDiagnosticCompletedAt(now);
            profile.setDiagnosticScore(graded.score());
            profile.setCurrentPath(recommended);
            profile.setDiagnosticResultsJson(resultsJson);
            profileRepository.save(profile);
        } else {
            UserTrackProfile profile = trackProfileRepository.findByUserIdAndTrackId(userId, trackId)
                    .orElse(UserTrackProfile.aUserTrackProfile().withUserId(userId).withTrackId(trackId).build());
            profile.setDiagnosticCompleted(true);
            profile.setDiagnosticCompletedAt(now);
            profile.setDiagnosticScore(graded.score());
            profile.setDiagnosticResultsJson(resultsJson);
            trackProfileRepository.save(profile);
        }

        log.info("[Diagnostic] Completed for user={} track={} path={}", userId, trackId, recommended);
        telemetry.diagnosticCompleted(userId, trackId, recommended.name(), graded.score());
        return new DiagnosticResult(recommended, chunkRecommendations, graded.score());
    }

    // ─── Skip ──────────────────────────────────────────────────────────────

    /** Java skip (backwards-compatible). */
    @Transactional
    public void skipDiagnostic(String userId) {
        skipDiagnostic(userId, "java");
    }

    @Transactional
    public void skipDiagnostic(String userId, String trackId) {
        Instant now = Instant.now();
        if ("java".equals(trackId)) {
            UserLearnerProfile profile = profileRepository.findByUserId(userId)
                    .orElse(UserLearnerProfile.aUserLearnerProfile().withUserId(userId).build());
            profile.setDiagnosticCompleted(true);
            profile.setDiagnosticCompletedAt(now);
            profile.setCurrentPath(LearnerPath.FOUNDATION);
            profileRepository.save(profile);
        } else {
            UserTrackProfile profile = trackProfileRepository.findByUserIdAndTrackId(userId, trackId)
                    .orElse(UserTrackProfile.aUserTrackProfile().withUserId(userId).withTrackId(trackId).build());
            profile.setDiagnosticCompleted(true);
            profile.setDiagnosticCompletedAt(now);
            trackProfileRepository.save(profile);
        }
        log.info("[Diagnostic] Skipped for user={} track={}", userId, trackId);
        telemetry.diagnosticCompleted(userId, trackId, "SKIPPED", 0.0);
    }
}
