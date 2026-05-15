package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.ai.domain.AnswerPair;
import com.ambravate.arcane.academy.ai.domain.GradeResult;
import com.ambravate.arcane.academy.ai.domain.QuestionResult;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.practice.domain.DiagnosticResult;
import com.ambravate.arcane.academy.practice.domain.DiagnosticSession;
import com.ambravate.arcane.academy.common.domain.EncodingPhase;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.QuestionTier;
import com.ambravate.arcane.academy.common.domain.ReviewSession;
import com.ambravate.arcane.academy.common.domain.SessionType;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.domain.UserLearnerProfile;
import com.ambravate.arcane.academy.common.domain.UserTopicProfile;
import com.ambravate.arcane.academy.common.repository.ChunkRepository;
import com.ambravate.arcane.academy.common.repository.QuestionRepository;
import com.ambravate.arcane.academy.common.repository.SubChunkRepository;
import com.ambravate.arcane.academy.common.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.common.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.common.repository.UserTopicProfileRepository;
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

    private final ChunkRepository chunkRepository;
    private final SubChunkRepository subChunkRepository;
    private final QuestionRepository questionRepository;
    private final UserChunkProgressRepository progressRepository;
    private final UserLearnerProfileRepository profileRepository;
    private final UserTopicProfileRepository topicProfileRepository;
    private final RetrievalService retrievalService;
    private final TelemetryService telemetry;
    private final ObjectMapper objectMapper;

    // ── Start ────────────────────────────────────────────────────────────────

    /** Java diagnostic (backwards-compatible). */
    public DiagnosticSession startEntryDiagnostic(String userId) {
        return startEntryDiagnostic(userId, "java");
    }

    /**
     * Generate entry diagnostic: 2 RECALL questions per chunk for the given topic.
     */
    public DiagnosticSession startEntryDiagnostic(String userId, String topicId) {
        List<Chunk> chunks = "java".equals(topicId)
            ? chunkRepository.findAllByOrderBySortOrderAsc()
            : chunkRepository.findByTopicIdOrderBySortOrderAsc(topicId);

        List<Question> diagnosticQuestions = new ArrayList<>();
        for (Chunk chunk : chunks) {
            List<SubChunk> subChunks = subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunk.getId());
            List<String> subChunkIds = subChunks.stream().map(SubChunk::getId).toList();

            List<Question> recallQuestions = questionRepository.findBySubChunkIdIn(subChunkIds).stream()
                    .filter(q -> q.getTier() == QuestionTier.RECALL)
                    .collect(Collectors.toList());

            Collections.shuffle(recallQuestions);
            diagnosticQuestions.addAll(recallQuestions.stream().limit(2).toList());
        }

        ReviewSession session = retrievalService.saveSession(userId, SessionType.DIAGNOSTIC,
                new GradeResult(0, 0, diagnosticQuestions.size(), List.of()));

        log.info("[Diagnostic] Started for user={} topic={} with {} questions", userId, topicId, diagnosticQuestions.size());
        return new DiagnosticSession(session.getId(), diagnosticQuestions);
    }

    // ── Submit ───────────────────────────────────────────────────────────────

    /** Java diagnostic submit (backwards-compatible). */
    @Transactional
    public DiagnosticResult submitDiagnostic(String userId, List<AnswerPair> answers) {
        return submitDiagnostic(userId, answers, "java");
    }

    /**
     * Grade answers and record results. For Java, assigns FOUNDATION/PRACTITIONER/EXPERT path.
     * For other topics, just stores the score and marks diagnostic complete.
     */
    @Transactional
    public DiagnosticResult submitDiagnostic(String userId, List<AnswerPair> answers, String topicId) {
        GradeResult graded = retrievalService.gradeAnswers(answers);

        // Map sub-chunk → chunk
        Map<String, String> subChunkToChunk = new HashMap<>();
        subChunkRepository.findAll().forEach(sc -> subChunkToChunk.put(sc.getId(), sc.getChunkId()));

        Map<String, List<QuestionResult>> byChunk = graded.results().stream()
                .collect(Collectors.groupingBy(r -> subChunkToChunk.getOrDefault(r.subChunkId(), "unknown")));

        Map<String, String> chunkRecommendations = new HashMap<>();
        for (var entry : byChunk.entrySet()) {
            long correct = entry.getValue().stream().filter(QuestionResult::correct).count();
            chunkRecommendations.put(entry.getKey(), correct >= 2 ? "SKIP" : correct == 1 ? "COMPRESS" : "FULL");
        }

        List<Chunk> topicChunks = "java".equals(topicId)
            ? chunkRepository.findAllByOrderBySortOrderAsc()
            : chunkRepository.findByTopicIdOrderBySortOrderAsc(topicId);
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
        for (Chunk chunk : topicChunks) {
            String rec = chunkRecommendations.get(chunk.getId());
            for (SubChunk sc : subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunk.getId())) {
                if (!progressRepository.existsByUserIdAndSubChunkId(userId, sc.getId())) {
                    SubChunkStatus status = "SKIP".equals(rec) ? SubChunkStatus.SKIPPED : SubChunkStatus.NOT_STARTED;
                    UserChunkProgress progress = UserChunkProgress.builder()
                            .userId(userId).subChunkId(sc.getId()).status(status)
                            .currentPhase(status == SubChunkStatus.SKIPPED ? EncodingPhase.COMPLETE : EncodingPhase.HOOK)
                            .memoryStrength(status == SubChunkStatus.SKIPPED ? 0.8 : 0.0).build();
                    if (status == SubChunkStatus.SKIPPED) {
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
        if ("java".equals(topicId)) {
            UserLearnerProfile profile = profileRepository.findByUserId(userId)
                    .orElse(UserLearnerProfile.aUserLearnerProfile().withUserId(userId).build());
            profile.setDiagnosticCompleted(true);
            profile.setDiagnosticCompletedAt(now);
            profile.setDiagnosticScore(graded.score());
            profile.setCurrentPath(recommended);
            profile.setDiagnosticResultsJson(resultsJson);
            profileRepository.save(profile);
        } else {
            UserTopicProfile profile = topicProfileRepository.findByUserIdAndTopicId(userId, topicId)
                    .orElse(UserTopicProfile.aUserTopicProfile().withUserId(userId).withTopicId(topicId).build());
            profile.setDiagnosticCompleted(true);
            profile.setDiagnosticCompletedAt(now);
            profile.setDiagnosticScore(graded.score());
            profile.setDiagnosticResultsJson(resultsJson);
            topicProfileRepository.save(profile);
        }

        log.info("[Diagnostic] Completed for user={} topic={} path={}", userId, topicId, recommended);
        telemetry.diagnosticCompleted(userId, topicId, recommended.name(), graded.score());
        return new DiagnosticResult(recommended, chunkRecommendations, graded.score());
    }

    // ── Skip ─────────────────────────────────────────────────────────────────

    /** Java skip (backwards-compatible). */
    @Transactional
    public void skipDiagnostic(String userId) {
        skipDiagnostic(userId, "java");
    }

    @Transactional
    public void skipDiagnostic(String userId, String topicId) {
        Instant now = Instant.now();
        if ("java".equals(topicId)) {
            UserLearnerProfile profile = profileRepository.findByUserId(userId)
                    .orElse(UserLearnerProfile.aUserLearnerProfile().withUserId(userId).build());
            profile.setDiagnosticCompleted(true);
            profile.setDiagnosticCompletedAt(now);
            profile.setCurrentPath(LearnerPath.FOUNDATION);
            profileRepository.save(profile);
        } else {
            UserTopicProfile profile = topicProfileRepository.findByUserIdAndTopicId(userId, topicId)
                    .orElse(UserTopicProfile.aUserTopicProfile().withUserId(userId).withTopicId(topicId).build());
            profile.setDiagnosticCompleted(true);
            profile.setDiagnosticCompletedAt(now);
            topicProfileRepository.save(profile);
        }
        log.info("[Diagnostic] Skipped for user={} topic={}", userId, topicId);
        telemetry.diagnosticCompleted(userId, topicId, "SKIPPED", 0.0);
    }
}
