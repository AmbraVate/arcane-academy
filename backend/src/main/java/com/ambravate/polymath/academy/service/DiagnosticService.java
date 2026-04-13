package com.ambravate.polymath.academy.service;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
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
    private final RetrievalService retrievalService;
    private final ObjectMapper objectMapper;

    /**
     * Generate entry diagnostic: 2 Tier-1 (RECALL) questions per chunk.
     * Returns questions grouped by chunk.
     */
    public DiagnosticSession startEntryDiagnostic(String userId) {
        List<Chunk> chunks = chunkRepository.findAllByOrderBySortOrderAsc();
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
                new RetrievalService.GradeResult(0, 0, diagnosticQuestions.size(), List.of()));

        log.info("[Diagnostic] Started for user={} with {} questions", userId, diagnosticQuestions.size());
        return new DiagnosticSession(session.getId(), diagnosticQuestions);
    }

    /**
     * Submit diagnostic answers and determine per-chunk recommendations + learner path.
     * Per chunk: 2/2 correct = SKIP, 1/2 = COMPRESS, 0/2 = FULL
     */
    @Transactional
    public DiagnosticResult submitDiagnostic(String userId, List<RetrievalService.AnswerPair> answers) {
        RetrievalService.GradeResult graded = retrievalService.gradeAnswers(answers);

        // Group results by chunk (via sub-chunk → chunk mapping)
        Map<String, String> subChunkToChunk = new HashMap<>();
        List<SubChunk> allSubChunks = subChunkRepository.findAll();
        for (SubChunk sc : allSubChunks) {
            subChunkToChunk.put(sc.getId(), sc.getChunkId());
        }

        Map<String, List<RetrievalService.QuestionResult>> byChunk = graded.results().stream()
                .collect(Collectors.groupingBy(r -> subChunkToChunk.getOrDefault(r.subChunkId(), "unknown")));

        Map<String, String> chunkRecommendations = new HashMap<>();
        for (var entry : byChunk.entrySet()) {
            long correct = entry.getValue().stream().filter(RetrievalService.QuestionResult::correct).count();
            String rec;
            if (correct >= 2) rec = "SKIP";
            else if (correct == 1) rec = "COMPRESS";
            else rec = "FULL";
            chunkRecommendations.put(entry.getKey(), rec);
        }

        // Also mark chunks with no questions as FULL
        List<Chunk> allChunks = chunkRepository.findAllByOrderBySortOrderAsc();
        for (Chunk c : allChunks) {
            chunkRecommendations.putIfAbsent(c.getId(), "FULL");
        }

        // Determine path
        long skipCount = chunkRecommendations.values().stream().filter(v -> v.equals("SKIP")).count();
        long totalChunks = chunkRecommendations.size();
        LearnerPath recommended;
        if (totalChunks > 0 && (double) skipCount / totalChunks > 0.9) {
            recommended = LearnerPath.EXPERT;
        } else if (totalChunks > 0 && (double) skipCount / totalChunks > 0.7) {
            recommended = LearnerPath.PRACTITIONER;
        } else {
            recommended = LearnerPath.FOUNDATION;
        }

        // Pre-create UserChunkProgress entries based on recommendations
        for (Chunk chunk : allChunks) {
            String rec = chunkRecommendations.get(chunk.getId());
            List<SubChunk> subs = subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunk.getId());
            for (SubChunk sc : subs) {
                if (!progressRepository.existsByUserIdAndSubChunkId(userId, sc.getId())) {
                    SubChunkStatus status = "SKIP".equals(rec) ? SubChunkStatus.SKIPPED : SubChunkStatus.NOT_STARTED;
                    UserChunkProgress progress = UserChunkProgress.builder()
                            .userId(userId)
                            .subChunkId(sc.getId())
                            .status(status)
                            .currentPhase(status == SubChunkStatus.SKIPPED ? EncodingPhase.COMPLETE : EncodingPhase.HOOK)
                            .memoryStrength(status == SubChunkStatus.SKIPPED ? 0.8 : 0.0)
                            .build();
                    if (status == SubChunkStatus.SKIPPED) {
                        progress.setCompletedAt(Instant.now());
                        progress.setLastReviewedAt(Instant.now());
                        progress.setNextReviewAt(Instant.now().plusSeconds(86400 * 3)); // Review in 3 days
                    }
                    progressRepository.save(progress);
                }
            }
        }

        // Save profile
        UserLearnerProfile profile = profileRepository.findByUserId(userId)
                .orElse(UserLearnerProfile.aUserLearnerProfile().withUserId(userId).build());
        profile.setDiagnosticCompleted(true);
        profile.setCurrentPath(recommended);
        try {
            profile.setDiagnosticResultsJson(objectMapper.writeValueAsString(chunkRecommendations));
        } catch (Exception e) {
            log.warn("Failed to serialize diagnostic results", e);
        }
        profileRepository.save(profile);

        // Update user's learner path
        log.info("[Diagnostic] Completed for user={} path={} recommendations={}", userId, recommended, chunkRecommendations);
        return new DiagnosticResult(recommended, chunkRecommendations, graded.score());
    }

    /**
     * Skip the diagnostic — user has no prior Java experience.
     * Sets diagnosticCompleted = true and path = FOUNDATION so they start fresh from Chunk A.
     */
    @Transactional
    public void skipDiagnostic(String userId) {
        UserLearnerProfile profile = profileRepository.findByUserId(userId)
                .orElse(UserLearnerProfile.aUserLearnerProfile().withUserId(userId).build());
        profile.setDiagnosticCompleted(true);
        profile.setCurrentPath(LearnerPath.FOUNDATION);
        profileRepository.save(profile);
        log.info("[Diagnostic] Skipped for user={} — starting fresh from FOUNDATION", userId);
    }

}
