package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.ai.domain.GradeResult;
import com.ambravate.arcane.academy.ai.domain.QuestionResult;
import com.ambravate.arcane.academy.ai.service.RetrievalService;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.ReviewSession;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.repository.ChunkRepository;
import com.ambravate.arcane.academy.common.repository.QuestionRepository;
import com.ambravate.arcane.academy.common.repository.SubChunkRepository;
import com.ambravate.arcane.academy.common.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.common.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.common.repository.UserTopicProfileRepository;
import com.ambravate.arcane.academy.common.telemetry.service.TelemetryService;
import com.ambravate.arcane.academy.practice.domain.DiagnosticResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DiagnosticService} tier-placement logic.
 * <p>
 * Boundary conditions for the four-tier system:
 * <ul>
 *   <li>&gt;90% skip → EXPERT</li>
 *   <li>&gt;70% skip → PRACTITIONER</li>
 *   <li>&gt;50% skip → ADVANCED (added in May 2026)</li>
 *   <li>≤50% skip → FOUNDATION</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("DiagnosticService — tier placement")
class DiagnosticServiceTest {

    private static final String USER_ID = "diag-user";
    private static final String TOPIC_ID = "java";

    @Mock private ChunkRepository chunkRepository;
    @Mock private SubChunkRepository subChunkRepository;
    @Mock private QuestionRepository questionRepository;
    @Mock private UserChunkProgressRepository progressRepository;
    @Mock private UserLearnerProfileRepository profileRepository;
    @Mock private UserTopicProfileRepository topicProfileRepository;
    @Mock private RetrievalService retrievalService;
    @Mock private TelemetryService telemetry;
    @Mock private ObjectMapper objectMapper;

    @InjectMocks
    private DiagnosticService service;

    private Chunk chunk(String id) {
        Chunk c = new Chunk();
        c.setId(id);
        c.setTopicId(TOPIC_ID);
        c.setSortOrder(1);
        return c;
    }

    private SubChunk subChunk(String id, String chunkId) {
        SubChunk sc = new SubChunk();
        sc.setId(id);
        sc.setChunkId(chunkId);
        return sc;
    }

    /**
     * Build a GradeResult where the first {@code correctCount} sub-chunks out
     * of the ordered list are answered correctly (2 correct per chunk → SKIP).
     * Sub-chunks alternate a/b per chunk, so correct pairs produce SKIP chunks.
     */
    private GradeResult gradeResult(
            List<String> subChunkIds, int correctCount) {
        List<QuestionResult> results = new java.util.ArrayList<>();
        for (int i = 0; i < subChunkIds.size(); i++) {
            String scId = subChunkIds.get(i);
            results.add(new QuestionResult(
                    "q-" + scId, scId, i < correctCount, "A", "A", "no feedback"));
        }
        return new GradeResult(
                (double) correctCount / subChunkIds.size(),
                correctCount, subChunkIds.size(), results);
    }

    @BeforeEach
    void commonStubs() {
        when(progressRepository.existsByUserIdAndSubChunkId(any(), any())).thenReturn(false);
        when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        ReviewSession session = new ReviewSession();
        session.setId("session-1");
        when(retrievalService.saveSession(any(), any(), any())).thenReturn(session);
        when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(profileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        try { when(objectMapper.writeValueAsString(any())).thenReturn("{}"); }
        catch (Exception ignored) {}
    }

    /**
     * Set up {@code chunkCount} chunks each with 2 sub-chunks.
     * Returns the flat list of sub-chunk IDs in order.
     */
    private List<String> setupChunks(int chunkCount) {
        List<Chunk> chunks = new java.util.ArrayList<>();
        for (int i = 0; i < chunkCount; i++) {
            chunks.add(chunk("chunk-" + i));
        }
        when(chunkRepository.findAllByOrderBySortOrderAsc()).thenReturn(chunks);

        List<SubChunk> allSubChunks = new java.util.ArrayList<>();
        List<String> allSubChunkIds = new java.util.ArrayList<>();

        for (Chunk c : chunks) {
            SubChunk sc1 = subChunk(c.getId() + "-a", c.getId());
            SubChunk sc2 = subChunk(c.getId() + "-b", c.getId());
            when(subChunkRepository.findByChunkIdOrderBySortOrderAsc(c.getId()))
                    .thenReturn(List.of(sc1, sc2));
            allSubChunks.add(sc1);
            allSubChunks.add(sc2);
            allSubChunkIds.add(sc1.getId());
            allSubChunkIds.add(sc2.getId());
        }

        when(subChunkRepository.findAll()).thenReturn(allSubChunks);
        return allSubChunkIds;
    }

    // ── FOUNDATION boundary ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("FOUNDATION tier")
    class FoundationTier {

        @Test
        @DisplayName("places learner at FOUNDATION when 0% of chunks are skipped")
        void zeroSkipIsFoundation() {
            List<String> scIds = setupChunks(4);
            when(retrievalService.gradeAnswers(any()))
                    .thenReturn(gradeResult(scIds, 0));

            DiagnosticResult result = service.submitDiagnostic(USER_ID, List.of(), TOPIC_ID);

            assertThat(result.recommendedPath()).isEqualTo(LearnerPath.FOUNDATION);
        }

        @Test
        @DisplayName("places learner at FOUNDATION when exactly 50% of chunks are skipped")
        void exactlyFiftyPercentIsFoundation() {
            // 10 chunks. 2 sub-chunks each → 20 sub-chunk entries.
            // 10 sub-chunks correct → 5 chunks SKIP (50% = ≤50% → FOUNDATION).
            List<String> scIds = setupChunks(10);
            when(retrievalService.gradeAnswers(any()))
                    .thenReturn(gradeResult(scIds, 10));

            DiagnosticResult result = service.submitDiagnostic(USER_ID, List.of(), TOPIC_ID);

            assertThat(result.recommendedPath()).isEqualTo(LearnerPath.FOUNDATION);
        }
    }

    // ── ADVANCED tier ───────────────────────────────────────────────────────────

    @Nested
    @DisplayName("ADVANCED tier — added May 2026")
    class AdvancedTier {

        @Test
        @DisplayName("places learner at ADVANCED when just over 50% of chunks are skipped")
        void justOverFiftyPercentIsAdvanced() {
            // 10 chunks, 2 sub-chunks each → 20 sub-chunk slots.
            // 12 correct → 6 chunks SKIP (60% > 50% but < 70% → ADVANCED).
            List<String> scIds = setupChunks(10);
            when(retrievalService.gradeAnswers(any()))
                    .thenReturn(gradeResult(scIds, 12));

            DiagnosticResult result = service.submitDiagnostic(USER_ID, List.of(), TOPIC_ID);

            assertThat(result.recommendedPath()).isEqualTo(LearnerPath.ADVANCED);
        }

        @Test
        @DisplayName("places learner at ADVANCED when 65% of chunks are skipped")
        void sixtyFivePercentIsAdvanced() {
            // 20 chunks → 40 sub-chunks. 26 correct → 13 SKIP (65% > 50%, < 70%).
            List<String> scIds = setupChunks(20);
            when(retrievalService.gradeAnswers(any()))
                    .thenReturn(gradeResult(scIds, 26));

            DiagnosticResult result = service.submitDiagnostic(USER_ID, List.of(), TOPIC_ID);

            assertThat(result.recommendedPath()).isEqualTo(LearnerPath.ADVANCED);
        }

        @Test
        @DisplayName("places learner at ADVANCED when exactly 70% of chunks are skipped (boundary is strictly > 70%)")
        void exactlySeventyPercentIsAdvanced() {
            // 10 chunks → 20 sub-chunks. 14 correct → 7 SKIP = 70%.
            // 70% is NOT > 70%, so it falls into ADVANCED, not PRACTITIONER.
            List<String> scIds = setupChunks(10);
            when(retrievalService.gradeAnswers(any()))
                    .thenReturn(gradeResult(scIds, 14));

            DiagnosticResult result = service.submitDiagnostic(USER_ID, List.of(), TOPIC_ID);

            assertThat(result.recommendedPath()).isEqualTo(LearnerPath.ADVANCED);
        }
    }

    // ── PRACTITIONER boundary ───────────────────────────────────────────────────

    @Nested
    @DisplayName("PRACTITIONER tier")
    class PractitionerTier {

        @Test
        @DisplayName("places learner at PRACTITIONER when 80% of chunks are skipped")
        void eightyPercentIsPractitioner() {
            // 10 chunks → 20 sub-chunks. 16 correct → 8 SKIP (80% > 70%, < 90%).
            List<String> scIds = setupChunks(10);
            when(retrievalService.gradeAnswers(any()))
                    .thenReturn(gradeResult(scIds, 16));

            DiagnosticResult result = service.submitDiagnostic(USER_ID, List.of(), TOPIC_ID);

            assertThat(result.recommendedPath()).isEqualTo(LearnerPath.PRACTITIONER);
        }
    }

    // ── EXPERT boundary ─────────────────────────────────────────────────────────

    @Nested
    @DisplayName("EXPERT tier")
    class ExpertTier {

        @Test
        @DisplayName("places learner at EXPERT when more than 90% of chunks are skipped")
        void ninetyOnePercentIsExpert() {
            // 10 chunks → 20 sub-chunks. 20 correct → 10 SKIP = 100% > 90% → EXPERT.
            List<String> scIds = setupChunks(10);
            when(retrievalService.gradeAnswers(any()))
                    .thenReturn(gradeResult(scIds, 20));

            DiagnosticResult result = service.submitDiagnostic(USER_ID, List.of(), TOPIC_ID);

            assertThat(result.recommendedPath()).isEqualTo(LearnerPath.EXPERT);
        }
    }
}
