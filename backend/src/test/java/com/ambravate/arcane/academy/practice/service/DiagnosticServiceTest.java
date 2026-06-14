package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.ai.domain.GradeResult;
import com.ambravate.arcane.academy.ai.domain.QuestionResult;
import com.ambravate.arcane.academy.ai.service.RetrievalService;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.ReviewSession;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.auth.repository.UserTrackProfileRepository;
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
 * Placement boundaries (based on fraction of modules skipped):
 * <ul>
 *   <li>&gt;90% skip → LEAD</li>
 *   <li>&gt;70% skip → SENIOR</li>
 *   <li>&gt;50% skip → JUNIOR</li>
 *   <li>≤50% skip  → APPRENTICE</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("DiagnosticService — tier placement")
class DiagnosticServiceTest {

    private static final String USER_ID = "diag-user";
    private static final String TOPIC_ID = "java";

    @Mock private LearningModuleRepository moduleRepository;
    @Mock private LessonRepository lessonRepository;
    @Mock private QuestionRepository questionRepository;
    @Mock private UserChunkProgressRepository progressRepository;
    @Mock private UserLearnerProfileRepository profileRepository;
    @Mock private UserTrackProfileRepository topicProfileRepository;
    @Mock private RetrievalService retrievalService;
    @Mock private TelemetryService telemetry;
    @Mock private ObjectMapper objectMapper;

    @InjectMocks
    private DiagnosticService service;

    private LearningModule chunk(String id) {
        return LearningModule.builder()
                .id(id)
                .trackId(TOPIC_ID)
                .sortOrder(1)
                .build();
    }

    private Lesson subChunk(String id, String moduleId) {
        return Lesson.builder()
                .id(id)
                .moduleId(moduleId)
                .build();
    }

    /**
     * Build a GradeResult where the first {@code correctCount} lessons out
     * of the ordered list are answered correctly (2 correct per module → SKIP).
     */
    private GradeResult gradeResult(List<String> lessonIds, int correctCount) {
        List<QuestionResult> results = new java.util.ArrayList<>();
        for (int i = 0; i < lessonIds.size(); i++) {
            String scId = lessonIds.get(i);
            results.add(new QuestionResult(
                    "q-" + scId, scId, i < correctCount, "A", "A", "no feedback"));
        }
        return new GradeResult(
                (double) correctCount / lessonIds.size(),
                correctCount, lessonIds.size(), results);
    }

    @BeforeEach
    void commonStubs() {
        when(progressRepository.existsByUserIdAndLessonId(any(), any())).thenReturn(false);
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
     * Set up {@code chunkCount} modules each with 2 lessons.
     * Returns the flat list of lesson IDs in order.
     * Mocks both {@code findByTrackIdOrderBySortOrderAsc} and the new
     * {@code findByModuleIdIn} call used by the service to build the lesson→module map.
     */
    private List<String> setupChunks(int chunkCount) {
        List<LearningModule> chunks = new java.util.ArrayList<>();
        for (int i = 0; i < chunkCount; i++) {
            chunks.add(chunk("chunk-" + i));
        }
        when(moduleRepository.findByTrackIdOrderBySortOrderAsc(anyString())).thenReturn(chunks);

        List<Lesson> allLessons = new java.util.ArrayList<>();
        List<String> allLessonIds = new java.util.ArrayList<>();

        for (LearningModule c : chunks) {
            Lesson sc1 = subChunk(c.getId() + "-a", c.getId());
            Lesson sc2 = subChunk(c.getId() + "-b", c.getId());
            when(lessonRepository.findByModuleIdOrderBySortOrderAsc(c.getId()))
                    .thenReturn(List.of(sc1, sc2));
            allLessons.add(sc1);
            allLessons.add(sc2);
            allLessonIds.add(sc1.getId());
            allLessonIds.add(sc2.getId());
        }

        // Service now uses findByModuleIdIn for the lesson→module map
        when(lessonRepository.findByModuleIdIn(anyList())).thenReturn(allLessons);
        return allLessonIds;
    }

    // ── APPRENTICE boundary ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("APPRENTICE tier")
    class ApprenticeTier {

        @Test
        @DisplayName("places learner at APPRENTICE when 0% of modules are skipped")
        void zeroSkipIsApprentice() {
            List<String> scIds = setupChunks(4);
            when(retrievalService.gradeAnswers(any()))
                    .thenReturn(gradeResult(scIds, 0));

            DiagnosticResult result = service.submitDiagnostic(USER_ID, List.of(), TOPIC_ID);

            assertThat(result.recommendedPath()).isEqualTo(LearnerPath.APPRENTICE);
        }

        @Test
        @DisplayName("places learner at APPRENTICE when exactly 50% of modules are skipped (boundary is strictly > 50%)")
        void exactlyFiftyPercentIsApprentice() {
            List<String> scIds = setupChunks(10);
            when(retrievalService.gradeAnswers(any()))
                    .thenReturn(gradeResult(scIds, 10));

            DiagnosticResult result = service.submitDiagnostic(USER_ID, List.of(), TOPIC_ID);

            assertThat(result.recommendedPath()).isEqualTo(LearnerPath.APPRENTICE);
        }
    }

    // ── JUNIOR boundary ─────────────────────────────────────────────────────────

    @Nested
    @DisplayName("JUNIOR tier")
    class JuniorTier {

        @Test
        @DisplayName("places learner at JUNIOR when just over 50% of modules are skipped")
        void justOverFiftyPercentIsJunior() {
            List<String> scIds = setupChunks(10);
            when(retrievalService.gradeAnswers(any()))
                    .thenReturn(gradeResult(scIds, 12));

            DiagnosticResult result = service.submitDiagnostic(USER_ID, List.of(), TOPIC_ID);

            assertThat(result.recommendedPath()).isEqualTo(LearnerPath.JUNIOR);
        }

        @Test
        @DisplayName("places learner at JUNIOR when 65% of modules are skipped")
        void sixtyFivePercentIsJunior() {
            List<String> scIds = setupChunks(20);
            when(retrievalService.gradeAnswers(any()))
                    .thenReturn(gradeResult(scIds, 26));

            DiagnosticResult result = service.submitDiagnostic(USER_ID, List.of(), TOPIC_ID);

            assertThat(result.recommendedPath()).isEqualTo(LearnerPath.JUNIOR);
        }

        @Test
        @DisplayName("places learner at JUNIOR when exactly 70% of modules are skipped (boundary is strictly > 70%)")
        void exactlySeventyPercentIsJunior() {
            List<String> scIds = setupChunks(10);
            when(retrievalService.gradeAnswers(any()))
                    .thenReturn(gradeResult(scIds, 14));

            DiagnosticResult result = service.submitDiagnostic(USER_ID, List.of(), TOPIC_ID);

            assertThat(result.recommendedPath()).isEqualTo(LearnerPath.JUNIOR);
        }
    }

    // ── SENIOR boundary ─────────────────────────────────────────────────────────

    @Nested
    @DisplayName("SENIOR tier")
    class SeniorTier {

        @Test
        @DisplayName("places learner at SENIOR when 80% of modules are skipped")
        void eightyPercentIsSenior() {
            List<String> scIds = setupChunks(10);
            when(retrievalService.gradeAnswers(any()))
                    .thenReturn(gradeResult(scIds, 16));

            DiagnosticResult result = service.submitDiagnostic(USER_ID, List.of(), TOPIC_ID);

            assertThat(result.recommendedPath()).isEqualTo(LearnerPath.SENIOR);
        }
    }

    // ── LEAD boundary ───────────────────────────────────────────────────────────

    @Nested
    @DisplayName("LEAD tier")
    class LeadTier {

        @Test
        @DisplayName("places learner at LEAD when more than 90% of modules are skipped")
        void ninetyOnePercentIsLead() {
            List<String> scIds = setupChunks(10);
            when(retrievalService.gradeAnswers(any()))
                    .thenReturn(gradeResult(scIds, 20));

            DiagnosticResult result = service.submitDiagnostic(USER_ID, List.of(), TOPIC_ID);

            assertThat(result.recommendedPath()).isEqualTo(LearnerPath.LEAD);
        }
    }
}
