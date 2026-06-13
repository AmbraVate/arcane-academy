package com.ambravate.arcane.academy.retention.service;

import com.ambravate.arcane.academy.ai.domain.FeynmanResult;
import com.ambravate.arcane.academy.ai.service.FeynmanService;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonPracticeType;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.practice.dto.CodeRunResponse;
import com.ambravate.arcane.academy.practice.runner.CodeExecutionPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * Routes Teach Back submissions to the correct scoring strategy:
 * <ul>
 *   <li>JAVA lesson + expected output set → code execution via {@link CodeExecutionPort}</li>
 *   <li>All other cases → pattern-match via {@link FeynmanService}</li>
 * </ul>
 * This is the single entry-point for {@code EncodingController.submitFeynman}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TeachBackScoringService {

    private final FeynmanService feynmanService;
    private final LessonRepository lessonRepository;
    private final CodeExecutionPort codeExecutionPort;

    public FeynmanResult evaluate(String userId, String lessonId, String submission) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException("Lesson not found: " + lessonId));

        if (lesson.getPracticeType() == LessonPracticeType.JAVA
                && lesson.getTeachBackExpectedOutput() != null
                && !lesson.getTeachBackExpectedOutput().isBlank()) {
            return scoreViaCodeExecution(userId, lessonId, submission, lesson.getTeachBackExpectedOutput());
        }

        return feynmanService.evaluateExplanation(userId, lessonId, submission);
    }

    private FeynmanResult scoreViaCodeExecution(String userId, String lessonId,
                                                 String code, String expectedOutput) {
        log.debug("[TeachBack] Code execution path lessonId={} userId={}", lessonId, userId);
        CodeRunResponse result = codeExecutionPort.run(code, null);

        String expected = expectedOutput.trim();

        if (result.getStatus() == CodeRunResponse.RunStatus.COMPILE_ERROR) {
            log.debug("[TeachBack] Compile error lessonId={}", lessonId);
            return FeynmanResult.codeExecution(0.0, 0.0, 1.0, 1.0, 0.1,
                    "Compilation failed. Check your syntax and try again. " + truncate(result.getError()), 0);
        }

        if (result.getStatus() == CodeRunResponse.RunStatus.RUNTIME_ERROR
                || result.getStatus() == CodeRunResponse.RunStatus.TIMEOUT
                || result.getStatus() == CodeRunResponse.RunStatus.ERROR) {
            log.debug("[TeachBack] Runtime error lessonId={}", lessonId);
            return FeynmanResult.codeExecution(0.0, 0.5, 1.0, 1.0, 0.2,
                    "Your code compiled but threw a runtime error. " + truncate(result.getError()), 5);
        }

        String actual = result.getOutput() == null ? "" : result.getOutput().trim();
        boolean exactMatch = actual.equals(expected);
        boolean partialMatch = !exactMatch && (actual.equalsIgnoreCase(expected)
                || actual.replaceAll("\\s+", " ").equals(expected.replaceAll("\\s+", " ")));

        if (exactMatch) {
            log.debug("[TeachBack] Exact match lessonId={}", lessonId);
            return FeynmanResult.codeExecution(1.0, 1.0, 1.0, 1.0, 1.0,
                    "Output matched. Your code demonstrates the concept correctly.", 20);
        }

        if (partialMatch) {
            log.debug("[TeachBack] Partial match lessonId={}", lessonId);
            return FeynmanResult.codeExecution(0.8, 1.0, 1.0, 1.0, 0.8,
                    "Almost there — your output is very close but differs slightly in formatting or casing.", 12);
        }

        log.debug("[TeachBack] Wrong output lessonId={} expected='{}' actual='{}'", lessonId, expected, actual);
        return FeynmanResult.codeExecution(0.0, 1.0, 1.0, 1.0, 0.4,
                "Code ran but output didn't match. Expected: \"" + expected + "\" — Got: \"" + truncate(actual) + "\"", 5);
    }

    private static String truncate(String s) {
        if (s == null) return "";
        return s.length() > 200 ? s.substring(0, 200) + "…" : s;
    }
}
