package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.ai.service.AiMentorService;
import com.ambravate.arcane.academy.ai.service.RetrievalService;
import com.ambravate.arcane.academy.ai.service.SpacingService;
import com.ambravate.arcane.academy.auth.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.EncodingPhase;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkPracticeType;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.events.UserEngagedEvent;
import com.ambravate.arcane.academy.common.telemetry.service.TelemetryService;
import com.ambravate.arcane.academy.content.repository.ChunkRepository;
import com.ambravate.arcane.academy.content.repository.SubChunkRepository;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import com.ambravate.arcane.academy.practice.domain.PracticeResult;
import com.ambravate.arcane.academy.practice.domain.SubChunkSession;
import com.ambravate.arcane.academy.practice.dto.CodeRunResponse;
import com.ambravate.arcane.academy.practice.repository.ReviewSessionRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.practice.runner.JavaCodeRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link EncodingService} — covers the three main learner flows:
 * <ul>
 *   <li>startSubChunk — prerequisite gates and first-start vs. resume detection</li>
 *   <li>submitGuidedPractice — compile/runtime errors, test failures, and happy-path XP award</li>
 *   <li>submitSoloPractice — pass marking without XP</li>
 *   <li>calculateRank — XP → rank tier boundaries</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EncodingService")
class EncodingServiceTest {

    @Mock private SubChunkRepository subChunkRepository;
    @Mock private ChunkRepository chunkRepository;
    @Mock private UserChunkProgressRepository progressRepository;
    @Mock private ReviewSessionRepository reviewSessionRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserLearnerProfileRepository learnerProfileRepository;
    @Mock private JavaCodeRunner codeRunner;
    @Mock private AiMentorService aiMentorService;
    @Mock private RetrievalService retrievalService;
    @Mock private SpacingService spacingService;
    @Mock private GamificationFacade gamification;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private TelemetryService telemetry;
    @Spy  private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks private EncodingService service;

    private static final String USER_ID = "u-test";
    private static final String SUB_CHUNK_ID = "sc-1";
    private static final String CHUNK_ID = "ch-1";

    // ── Test helpers ─────────────────────────────────────────────────────────────

    private SubChunk subChunk(String id, String chunkId, int sortOrder) {
        return SubChunk.builder()
                .id(id)
                .chunkId(chunkId)
                .title("Test Sub-Chunk")
                .sortOrder(sortOrder)
                .xpReward(50)
                .practiceType(SubChunkPracticeType.JAVA)
                .build();
    }

    private SubChunk subChunkWithTests(String id, String chunkId) {
        return SubChunk.builder()
                .id(id)
                .chunkId(chunkId)
                .title("Test Sub-Chunk")
                .sortOrder(1)
                .xpReward(50)
                .practiceType(SubChunkPracticeType.JAVA)
                .guidedPracticeTestsJson("[{\"label\":\"T1\",\"input\":\"5\",\"expected\":\"Hello\"}]")
                .build();
    }

    private Chunk chunk(String id, String... prereqIds) {
        List<Chunk> prereqs = java.util.Arrays.stream(prereqIds)
                .map(pId -> Chunk.builder().id(pId).build())
                .toList();
        return Chunk.builder()
                .id(id)
                .title("Parent Chunk")
                .sortOrder(1)
                .prerequisites(prereqs)
                .build();
    }

    private UserChunkProgress progressInProgress(String subChunkId) {
        return UserChunkProgress.builder()
                .userId(USER_ID)
                .subChunkId(subChunkId)
                .status(SubChunkStatus.IN_PROGRESS)
                .currentPhase(EncodingPhase.HOOK)
                .build();
    }

    private User user(int xp) {
        return User.aUser()
                .withId(USER_ID)
                .withUsername("tester")
                .withEmail("t@test")
                .withTotalXp(xp)
                .build();
    }

    // ── startSubChunk ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("startSubChunk()")
    class StartSubChunk {

        @Test
        @DisplayName("First start — creates progress and emits questStarted telemetry")
        void firstStart() {
            SubChunk sc = subChunk(SUB_CHUNK_ID, CHUNK_ID, 1);
            when(subChunkRepository.findById(SUB_CHUNK_ID)).thenReturn(Optional.of(sc));
            when(chunkRepository.findById(CHUNK_ID)).thenReturn(Optional.of(chunk(CHUNK_ID)));
            when(progressRepository.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.empty());
            when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            SubChunkSession session = service.startSubChunk(USER_ID, SUB_CHUNK_ID);

            assertThat(session).isNotNull();
            assertThat(session.subChunk()).isEqualTo(sc);
            verify(progressRepository).save(any(UserChunkProgress.class));
            verify(telemetry).questStarted(USER_ID, SUB_CHUNK_ID, CHUNK_ID);
        }

        @Test
        @DisplayName("Resume — returns existing progress without emitting questStarted")
        void resume() {
            SubChunk sc = subChunk(SUB_CHUNK_ID, CHUNK_ID, 1);
            UserChunkProgress existing = progressInProgress(SUB_CHUNK_ID);

            when(subChunkRepository.findById(SUB_CHUNK_ID)).thenReturn(Optional.of(sc));
            when(chunkRepository.findById(CHUNK_ID)).thenReturn(Optional.of(chunk(CHUNK_ID)));
            when(progressRepository.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.of(existing));

            SubChunkSession session = service.startSubChunk(USER_ID, SUB_CHUNK_ID);

            assertThat(session.progress()).isEqualTo(existing);
            verify(telemetry, never()).questStarted(anyString(), anyString(), anyString());
        }

        @Test
        @DisplayName("Throws FORBIDDEN when prerequisite chunk is not complete")
        void prerequisiteNotMet() {
            SubChunk sc = subChunk(SUB_CHUNK_ID, CHUNK_ID, 1);
            Chunk parentChunk = chunk(CHUNK_ID, "prereq-chunk");
            // prereq-chunk has one sub-chunk that the user has NOT completed
            SubChunk prereqSub = SubChunk.builder().id("sc-prereq").chunkId("prereq-chunk").build();

            when(subChunkRepository.findById(SUB_CHUNK_ID)).thenReturn(Optional.of(sc));
            when(chunkRepository.findById(CHUNK_ID)).thenReturn(Optional.of(parentChunk));
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of()); // nothing done
            when(subChunkRepository.findByChunkIdIn(List.of("prereq-chunk")))
                    .thenReturn(List.of(prereqSub));

            assertThatThrownBy(() -> service.startSubChunk(USER_ID, SUB_CHUNK_ID))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("prerequisite");
        }

        @Test
        @DisplayName("Succeeds when prerequisite chunk is fully complete")
        void prerequisiteMet() {
            SubChunk sc = subChunk(SUB_CHUNK_ID, CHUNK_ID, 1);
            Chunk parentChunk = chunk(CHUNK_ID, "prereq-chunk");
            SubChunk prereqSub = SubChunk.builder().id("sc-prereq").chunkId("prereq-chunk").build();

            UserChunkProgress prereqDone = UserChunkProgress.builder()
                    .userId(USER_ID).subChunkId("sc-prereq")
                    .status(SubChunkStatus.COMPLETE).build();

            when(subChunkRepository.findById(SUB_CHUNK_ID)).thenReturn(Optional.of(sc));
            when(chunkRepository.findById(CHUNK_ID)).thenReturn(Optional.of(parentChunk));
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of(prereqDone));
            when(subChunkRepository.findByChunkIdIn(List.of("prereq-chunk")))
                    .thenReturn(List.of(prereqSub));
            when(progressRepository.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.empty());
            when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            SubChunkSession session = service.startSubChunk(USER_ID, SUB_CHUNK_ID);

            assertThat(session).isNotNull();
        }

        @Test
        @DisplayName("Throws FORBIDDEN when prior sibling in same chunk is not complete")
        void sequentialOrderNotMet() {
            SubChunk sc2 = subChunk(SUB_CHUNK_ID, CHUNK_ID, 2); // sortOrder=2
            SubChunk sc1 = subChunk("sc-first", CHUNK_ID, 1);    // prior sibling

            when(subChunkRepository.findById(SUB_CHUNK_ID)).thenReturn(Optional.of(sc2));
            when(chunkRepository.findById(CHUNK_ID)).thenReturn(Optional.of(chunk(CHUNK_ID)));
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of()); // sc1 not done
            when(subChunkRepository.findByChunkIdOrderBySortOrderAsc(CHUNK_ID))
                    .thenReturn(List.of(sc1, sc2));

            assertThatThrownBy(() -> service.startSubChunk(USER_ID, SUB_CHUNK_ID))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("earlier lessons");
        }

        @Test
        @DisplayName("Throws NoSuchElementException when sub-chunk does not exist")
        void subChunkNotFound() {
            when(subChunkRepository.findById("ghost")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.startSubChunk(USER_ID, "ghost"))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    // ── submitGuidedPractice ──────────────────────────────────────────────────────

    @Nested
    @DisplayName("submitGuidedPractice()")
    class SubmitGuidedPractice {

        private void stubProgressAndUser(UserChunkProgress progress) {
            when(progressRepository.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.of(progress));
            lenient().when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            lenient().when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user(0)));
            lenient().when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of(progress));
            lenient().when(reviewSessionRepository.findByUserIdOrderByStartedAtDesc(USER_ID)).thenReturn(List.of());
            lenient().when(gamification.evaluateAndAwardBadges(anyString(), anyList(), anyList())).thenReturn(List.of());
            lenient().doNothing().when(eventPublisher).publishEvent(any(UserEngagedEvent.class));
        }

        @Test
        @DisplayName("Happy path — all tests pass → marks passed, awards XP, evaluates badges")
        void allTestsPass() {
            SubChunk sc = subChunkWithTests(SUB_CHUNK_ID, CHUNK_ID);
            UserChunkProgress progress = progressInProgress(SUB_CHUNK_ID);
            when(subChunkRepository.findById(SUB_CHUNK_ID)).thenReturn(Optional.of(sc));
            stubProgressAndUser(progress);
            // Both probe and actual run return "Hello" → matches expected
            when(codeRunner.run(eq("good code"), any())).thenReturn(CodeRunResponse.success("Hello"));

            PracticeResult result = service.submitGuidedPractice(USER_ID, SUB_CHUNK_ID, "good code");

            assertThat(result.allPassed()).isTrue();
            assertThat(result.xpEarned()).isEqualTo(50);
            assertThat(progress.isGuidedPracticePassed()).isTrue();
        }

        @Test
        @DisplayName("Compile error — returns COMPILE_ERROR result with AI feedback")
        void compileError() {
            SubChunk sc = subChunkWithTests(SUB_CHUNK_ID, CHUNK_ID);
            UserChunkProgress progress = progressInProgress(SUB_CHUNK_ID);
            when(subChunkRepository.findById(SUB_CHUNK_ID)).thenReturn(Optional.of(sc));
            when(progressRepository.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.of(progress));
            when(codeRunner.run(anyString(), any())).thenReturn(CodeRunResponse.compilationError("missing ;"));
            when(aiMentorService.explainCompileError(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn("Fix: add a semicolon");

            PracticeResult result = service.submitGuidedPractice(USER_ID, SUB_CHUNK_ID, "bad code");

            assertThat(result.allPassed()).isFalse();
            assertThat(result.errorType()).isEqualTo("COMPILE_ERROR");
            assertThat(result.mentorFeedback()).contains("semicolon");
            assertThat(progress.isGuidedPracticePassed()).isFalse();
        }

        @Test
        @DisplayName("Test failure — returns TEST_FAILURE result with mentor feedback")
        void testFailure() {
            SubChunk sc = subChunkWithTests(SUB_CHUNK_ID, CHUNK_ID);
            UserChunkProgress progress = progressInProgress(SUB_CHUNK_ID);
            when(subChunkRepository.findById(SUB_CHUNK_ID)).thenReturn(Optional.of(sc));
            when(progressRepository.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.of(progress));
            // Probe succeeds but output doesn't match expected "Hello"
            when(codeRunner.run(anyString(), any())).thenReturn(CodeRunResponse.success("Wrong output"));
            when(aiMentorService.getFeedback(anyString(), anyString(), anyString(), anyString(), anyString()))
                    .thenReturn("Try checking your output format");

            PracticeResult result = service.submitGuidedPractice(USER_ID, SUB_CHUNK_ID, "code");

            assertThat(result.allPassed()).isFalse();
            assertThat(result.errorType()).isEqualTo("TEST_FAILURE");
            assertThat(result.mentorFeedback()).contains("output format");
            assertThat(progress.isGuidedPracticePassed()).isFalse();
        }

        @Test
        @DisplayName("No test cases defined — auto-passes and marks guidedPracticePassed")
        void noTestCasesAutoPasses() {
            SubChunk sc = SubChunk.builder()
                    .id(SUB_CHUNK_ID).chunkId(CHUNK_ID).title("SC").sortOrder(1)
                    .practiceType(SubChunkPracticeType.JAVA)
                    .guidedPracticeTestsJson(null) // no tests
                    .build();
            UserChunkProgress progress = progressInProgress(SUB_CHUNK_ID);
            when(subChunkRepository.findById(SUB_CHUNK_ID)).thenReturn(Optional.of(sc));
            when(progressRepository.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.of(progress));
            when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            PracticeResult result = service.submitGuidedPractice(USER_ID, SUB_CHUNK_ID, "any code");

            assertThat(result.allPassed()).isTrue();
            assertThat(result.xpEarned()).isEqualTo(0);
            assertThat(progress.isGuidedPracticePassed()).isTrue();
        }

        @Test
        @DisplayName("Throws IllegalStateException when no progress record exists")
        void noProgressRecord() {
            SubChunk sc = subChunkWithTests(SUB_CHUNK_ID, CHUNK_ID);
            when(subChunkRepository.findById(SUB_CHUNK_ID)).thenReturn(Optional.of(sc));
            when(progressRepository.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.submitGuidedPractice(USER_ID, SUB_CHUNK_ID, "code"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("No progress record");
        }
    }

    // ── submitSoloPractice ────────────────────────────────────────────────────────

    @Nested
    @DisplayName("submitSoloPractice()")
    class SubmitSoloPractice {

        @Test
        @DisplayName("All tests pass → marks soloPracticePassed, no XP awarded")
        void allTestsPass() {
            SubChunk sc = subChunkWithTests(SUB_CHUNK_ID, CHUNK_ID);
            UserChunkProgress progress = progressInProgress(SUB_CHUNK_ID);
            when(subChunkRepository.findById(SUB_CHUNK_ID)).thenReturn(Optional.of(sc));
            when(progressRepository.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.of(progress));
            when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(codeRunner.run(anyString(), any())).thenReturn(CodeRunResponse.success("Hello"));

            PracticeResult result = service.submitSoloPractice(USER_ID, SUB_CHUNK_ID, "code");

            assertThat(result.allPassed()).isTrue();
            assertThat(result.xpEarned()).isEqualTo(0); // no XP for solo
            assertThat(progress.isSoloPracticePassed()).isTrue();
        }

        @Test
        @DisplayName("Test failure → does not mark soloPracticePassed")
        void testFailure() {
            SubChunk sc = subChunkWithTests(SUB_CHUNK_ID, CHUNK_ID);
            UserChunkProgress progress = progressInProgress(SUB_CHUNK_ID);
            when(subChunkRepository.findById(SUB_CHUNK_ID)).thenReturn(Optional.of(sc));
            when(progressRepository.findByUserIdAndSubChunkId(USER_ID, SUB_CHUNK_ID))
                    .thenReturn(Optional.of(progress));
            when(codeRunner.run(anyString(), any())).thenReturn(CodeRunResponse.success("Wrong"));
            when(aiMentorService.getFeedback(anyString(), anyString(), anyString(), anyString(), anyString()))
                    .thenReturn("Check output");

            PracticeResult result = service.submitSoloPractice(USER_ID, SUB_CHUNK_ID, "code");

            assertThat(result.allPassed()).isFalse();
            assertThat(result.errorType()).isEqualTo("TEST_FAILURE");
            assertThat(progress.isSoloPracticePassed()).isFalse();
        }
    }

    // ── calculateRank ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("calculateRank()")
    class CalculateRank {

        @ParameterizedTest(name = "{0} XP → {1}")
        @CsvSource({
            "0,       Novice",
            "799,     Novice",
            "800,     Apprentice",
            "1999,    Apprentice",
            "2000,    Adept",
            "3999,    Adept",
            "4000,    Mage",
            "6499,    Mage",
            "6500,    Archmage",
            "7999,    Archmage",
            "8000,    Magus",
            "10999,   Magus",
            "11000,   Lord Magus",
            "99999,   Lord Magus"
        })
        void rankBoundaries(int xp, String expectedRank) {
            assertThat(EncodingService.calculateRank(xp)).isEqualTo(expectedRank);
        }
    }
}
