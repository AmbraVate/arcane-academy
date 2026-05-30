package com.ambravate.arcane.academy.practice.service;

import com.ambravate.arcane.academy.ai.service.AiMentorService;
import com.ambravate.arcane.academy.ai.service.RetrievalService;
import com.ambravate.arcane.academy.ai.service.SpacingService;
import com.ambravate.arcane.academy.auth.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.common.domain.EncodingPhase;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonPracticeType;
import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.events.UserEngagedEvent;
import com.ambravate.arcane.academy.common.telemetry.service.TelemetryService;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import com.ambravate.arcane.academy.auth.repository.UserTrackProfileRepository;
import com.ambravate.arcane.academy.practice.domain.LessonSession;
import com.ambravate.arcane.academy.practice.domain.PracticeResult;
import com.ambravate.arcane.academy.practice.domain.SoloAssessmentResult;
import com.ambravate.arcane.academy.practice.dto.CodeRunResponse;
import com.ambravate.arcane.academy.practice.dto.SoloSubmitRequest;
import com.ambravate.arcane.academy.practice.repository.ReviewSessionRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.practice.runner.CodeExecutionPort;
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
 *   <li>startLesson — prerequisite gates and first-start vs. resume detection</li>
 *   <li>submitGuidedPractice — compile/runtime errors, test failures, and happy-path XP award</li>
 *   <li>submitSoloPractice — pass marking without XP</li>
 *   <li>calculateRank — XP → rank tier boundaries</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EncodingService")
class EncodingServiceTest {

    @Mock private LessonRepository lessonRepository;
    @Mock private LearningModuleRepository moduleRepository;
    @Mock private UserChunkProgressRepository progressRepository;
    @Mock private ReviewSessionRepository reviewSessionRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserLearnerProfileRepository learnerProfileRepository;
    @Mock private UserTrackProfileRepository topicProfileRepository;
    @Mock private CodeExecutionPort codeRunner;
    @Mock private AiMentorService aiMentorService;
    @Mock private RetrievalService retrievalService;
    @Mock private SpacingService spacingService;
    @Mock private GamificationFacade gamification;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private TelemetryService telemetry;
    @Mock private GuidedStepService guidedStepService;
    @Mock private KeywordScoringService keywordScoringService;
    @Spy  private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks private EncodingService service;

    private static final String USER_ID = "u-test";
    private static final String LESSON_ID = "sc-1";
    private static final String MODULE_ID = "ch-1";

    // ── Test helpers ─────────────────────────────────────────────────────────────

    private Lesson lesson(String id, String moduleId, int sortOrder) {
        return Lesson.builder()
                .id(id)
                .moduleId(moduleId)
                .title("Test Lesson")
                .sortOrder(sortOrder)
                .xpReward(50)
                .practiceType(LessonPracticeType.JAVA)
                .build();
    }

    private Lesson lessonWithTests(String id, String moduleId) {
        return Lesson.builder()
                .id(id)
                .moduleId(moduleId)
                .title("Test Lesson")
                .sortOrder(1)
                .xpReward(50)
                .practiceType(LessonPracticeType.JAVA)
                .guidedPracticeTestsJson("[{\"label\":\"T1\",\"input\":\"5\",\"expected\":\"Hello\"}]")
                .build();
    }

    private LearningModule module(String id, String... prereqIds) {
        List<LearningModule> prereqs = java.util.Arrays.stream(prereqIds)
                .map(pId -> LearningModule.builder().id(pId).build())
                .toList();
        return LearningModule.builder()
                .id(id)
                .title("Parent Module")
                .sortOrder(1)
                .prerequisites(prereqs)
                .build();
    }

    private UserChunkProgress progressInProgress(String lessonId) {
        return UserChunkProgress.builder()
                .userId(USER_ID)
                .lessonId(lessonId)
                .status(LessonStatus.IN_PROGRESS)
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

    // ── startLesson ───────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("startLesson()")
    class StartLesson {

        @Test
        @DisplayName("First start — creates progress and emits questStarted telemetry")
        void firstStart() {
            Lesson sc = lesson(LESSON_ID, MODULE_ID, 1);
            when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(sc));
            when(moduleRepository.findById(MODULE_ID)).thenReturn(Optional.of(module(MODULE_ID)));
            when(progressRepository.findByUserIdAndLessonId(USER_ID, LESSON_ID))
                    .thenReturn(Optional.empty());
            when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            LessonSession session = service.startLesson(USER_ID, LESSON_ID);

            assertThat(session).isNotNull();
            assertThat(session.lesson()).isEqualTo(sc);
            verify(progressRepository).save(any(UserChunkProgress.class));
            verify(telemetry).questStarted(USER_ID, LESSON_ID, MODULE_ID);
        }

        @Test
        @DisplayName("Resume — returns existing progress without emitting questStarted")
        void resume() {
            Lesson sc = lesson(LESSON_ID, MODULE_ID, 1);
            UserChunkProgress existing = progressInProgress(LESSON_ID);

            when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(sc));
            when(moduleRepository.findById(MODULE_ID)).thenReturn(Optional.of(module(MODULE_ID)));
            when(progressRepository.findByUserIdAndLessonId(USER_ID, LESSON_ID))
                    .thenReturn(Optional.of(existing));

            LessonSession session = service.startLesson(USER_ID, LESSON_ID);

            assertThat(session.progress()).isEqualTo(existing);
            verify(telemetry, never()).questStarted(anyString(), anyString(), anyString());
        }

        @Test
        @DisplayName("Throws FORBIDDEN when prerequisite module is not complete")
        void prerequisiteNotMet() {
            Lesson sc = lesson(LESSON_ID, MODULE_ID, 1);
            LearningModule parentModule = module(MODULE_ID, "prereq-module");
            // prereq-module has one lesson that the user has NOT completed
            Lesson prereqLesson = Lesson.builder().id("sc-prereq").moduleId("prereq-module").build();

            when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(sc));
            when(moduleRepository.findById(MODULE_ID)).thenReturn(Optional.of(parentModule));
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of()); // nothing done
            when(lessonRepository.findByModuleIdIn(List.of("prereq-module")))
                    .thenReturn(List.of(prereqLesson));

            assertThatThrownBy(() -> service.startLesson(USER_ID, LESSON_ID))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("prerequisite");
        }

        @Test
        @DisplayName("Succeeds when prerequisite module is fully complete")
        void prerequisiteMet() {
            Lesson sc = lesson(LESSON_ID, MODULE_ID, 1);
            LearningModule parentModule = module(MODULE_ID, "prereq-module");
            Lesson prereqLesson = Lesson.builder().id("sc-prereq").moduleId("prereq-module").build();

            UserChunkProgress prereqDone = UserChunkProgress.builder()
                    .userId(USER_ID).lessonId("sc-prereq")
                    .status(LessonStatus.COMPLETE).build();

            when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(sc));
            when(moduleRepository.findById(MODULE_ID)).thenReturn(Optional.of(parentModule));
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of(prereqDone));
            when(lessonRepository.findByModuleIdIn(List.of("prereq-module")))
                    .thenReturn(List.of(prereqLesson));
            when(progressRepository.findByUserIdAndLessonId(USER_ID, LESSON_ID))
                    .thenReturn(Optional.empty());
            when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            LessonSession session = service.startLesson(USER_ID, LESSON_ID);

            assertThat(session).isNotNull();
        }

        @Test
        @DisplayName("Throws FORBIDDEN when prior sibling in same module is not complete")
        void sequentialOrderNotMet() {
            Lesson sc2 = lesson(LESSON_ID, MODULE_ID, 2); // sortOrder=2
            Lesson sc1 = lesson("sc-first", MODULE_ID, 1);    // prior sibling

            when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(sc2));
            when(moduleRepository.findById(MODULE_ID)).thenReturn(Optional.of(module(MODULE_ID)));
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of()); // sc1 not done
            when(lessonRepository.findByModuleIdOrderBySortOrderAsc(MODULE_ID))
                    .thenReturn(List.of(sc1, sc2));

            assertThatThrownBy(() -> service.startLesson(USER_ID, LESSON_ID))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("earlier lessons");
        }

        @Test
        @DisplayName("Throws NoSuchElementException when lesson does not exist")
        void lessonNotFound() {
            when(lessonRepository.findById("ghost")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.startLesson(USER_ID, "ghost"))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    // ── submitGuidedPractice ──────────────────────────────────────────────────────

    @Nested
    @DisplayName("submitGuidedPractice()")
    class SubmitGuidedPractice {

        private void stubProgressAndUser(UserChunkProgress progress) {
            when(progressRepository.findByUserIdAndLessonId(USER_ID, LESSON_ID))
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
            Lesson sc = lessonWithTests(LESSON_ID, MODULE_ID);
            UserChunkProgress progress = progressInProgress(LESSON_ID);
            when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(sc));
            stubProgressAndUser(progress);
            // Both probe and actual run return "Hello" → matches expected
            when(codeRunner.run(eq("good code"), any())).thenReturn(CodeRunResponse.success("Hello"));

            PracticeResult result = service.submitGuidedPractice(USER_ID, LESSON_ID, "good code");

            assertThat(result.allPassed()).isTrue();
            assertThat(result.xpEarned()).isEqualTo(50);
            assertThat(progress.isGuidedPracticePassed()).isTrue();
        }

        @Test
        @DisplayName("Compile error — returns COMPILE_ERROR result with AI feedback")
        void compileError() {
            Lesson sc = lessonWithTests(LESSON_ID, MODULE_ID);
            UserChunkProgress progress = progressInProgress(LESSON_ID);
            when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(sc));
            when(progressRepository.findByUserIdAndLessonId(USER_ID, LESSON_ID))
                    .thenReturn(Optional.of(progress));
            when(codeRunner.run(anyString(), any())).thenReturn(CodeRunResponse.compilationError("missing ;"));
            when(aiMentorService.explainCompileError(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn("Fix: add a semicolon");

            PracticeResult result = service.submitGuidedPractice(USER_ID, LESSON_ID, "bad code");

            assertThat(result.allPassed()).isFalse();
            assertThat(result.errorType()).isEqualTo("COMPILE_ERROR");
            assertThat(result.mentorFeedback()).contains("semicolon");
            assertThat(progress.isGuidedPracticePassed()).isFalse();
        }

        @Test
        @DisplayName("Test failure — returns TEST_FAILURE result with mentor feedback")
        void testFailure() {
            Lesson sc = lessonWithTests(LESSON_ID, MODULE_ID);
            UserChunkProgress progress = progressInProgress(LESSON_ID);
            when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(sc));
            when(progressRepository.findByUserIdAndLessonId(USER_ID, LESSON_ID))
                    .thenReturn(Optional.of(progress));
            // Probe succeeds but output doesn't match expected "Hello"
            when(codeRunner.run(anyString(), any())).thenReturn(CodeRunResponse.success("Wrong output"));
            when(aiMentorService.getFeedback(anyString(), anyString(), anyString(), anyString(), anyString()))
                    .thenReturn("Try checking your output format");

            PracticeResult result = service.submitGuidedPractice(USER_ID, LESSON_ID, "code");

            assertThat(result.allPassed()).isFalse();
            assertThat(result.errorType()).isEqualTo("TEST_FAILURE");
            assertThat(result.mentorFeedback()).contains("output format");
            assertThat(progress.isGuidedPracticePassed()).isFalse();
        }

        @Test
        @DisplayName("No test cases defined — auto-passes and marks guidedPracticePassed")
        void noTestCasesAutoPasses() {
            Lesson sc = Lesson.builder()
                    .id(LESSON_ID).moduleId(MODULE_ID).title("SC").sortOrder(1)
                    .practiceType(LessonPracticeType.JAVA)
                    .guidedPracticeTestsJson(null) // no tests
                    .build();
            UserChunkProgress progress = progressInProgress(LESSON_ID);
            when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(sc));
            when(progressRepository.findByUserIdAndLessonId(USER_ID, LESSON_ID))
                    .thenReturn(Optional.of(progress));
            when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            PracticeResult result = service.submitGuidedPractice(USER_ID, LESSON_ID, "any code");

            assertThat(result.allPassed()).isTrue();
            assertThat(result.xpEarned()).isEqualTo(0);
            assertThat(progress.isGuidedPracticePassed()).isTrue();
        }

        @Test
        @DisplayName("Throws IllegalStateException when no progress record exists")
        void noProgressRecord() {
            Lesson sc = lessonWithTests(LESSON_ID, MODULE_ID);
            when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(sc));
            when(progressRepository.findByUserIdAndLessonId(USER_ID, LESSON_ID))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.submitGuidedPractice(USER_ID, LESSON_ID, "code"))
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
            Lesson sc = lessonWithTests(LESSON_ID, MODULE_ID);
            UserChunkProgress progress = progressInProgress(LESSON_ID);
            when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(sc));
            when(progressRepository.findByUserIdAndLessonId(USER_ID, LESSON_ID))
                    .thenReturn(Optional.of(progress));
            when(progressRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(codeRunner.run(anyString(), any())).thenReturn(CodeRunResponse.success("Hello"));

            SoloAssessmentResult result = service.submitSoloPractice(
                    USER_ID, LESSON_ID, SoloSubmitRequest.builder().code("code").build());

            assertThat(result.passed()).isTrue();
            assertThat(result.xpEarned()).isEqualTo(0); // no XP for solo DETERMINISTIC path
            assertThat(progress.isSoloPracticePassed()).isTrue();
        }

        @Test
        @DisplayName("Test failure → does not mark soloPracticePassed")
        void testFailure() {
            Lesson sc = lessonWithTests(LESSON_ID, MODULE_ID);
            UserChunkProgress progress = progressInProgress(LESSON_ID);
            when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(sc));
            when(progressRepository.findByUserIdAndLessonId(USER_ID, LESSON_ID))
                    .thenReturn(Optional.of(progress));
            when(codeRunner.run(anyString(), any())).thenReturn(CodeRunResponse.success("Wrong"));
            when(aiMentorService.getFeedback(anyString(), anyString(), anyString(), anyString(), anyString()))
                    .thenReturn("Check output");

            SoloAssessmentResult result = service.submitSoloPractice(
                    USER_ID, LESSON_ID, SoloSubmitRequest.builder().code("code").build());

            assertThat(result.passed()).isFalse();
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
