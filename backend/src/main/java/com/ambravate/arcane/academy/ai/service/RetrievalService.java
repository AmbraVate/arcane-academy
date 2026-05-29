package com.ambravate.arcane.academy.ai.service;

import com.ambravate.arcane.academy.ai.domain.AnswerPair;
import com.ambravate.arcane.academy.ai.domain.GradeResult;
import com.ambravate.arcane.academy.ai.domain.QuestionResult;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.QuestionTier;
import com.ambravate.arcane.academy.common.domain.QuestionType;
import com.ambravate.arcane.academy.common.domain.ReviewSession;
import com.ambravate.arcane.academy.common.domain.SessionType;
import com.ambravate.arcane.academy.common.domain.UserLearnerProfile;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.practice.repository.ReviewSessionRepository;
import com.ambravate.arcane.academy.auth.repository.UserLearnerProfileRepository;
import com.ambravate.arcane.academy.common.telemetry.service.TelemetryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetrievalService {

    private final QuestionRepository questionRepository;
    private final UserLearnerProfileRepository profileRepository;
    private final ReviewSessionRepository sessionRepository;
    private final TelemetryService telemetry;

    /**
     * Generate a retrieval check for a sub-chunk: exactly 4 randomly selected questions.
     *
     * <p>Target distribution: 1 RECALL + 2 APPLICATION + 1 DISCRIMINATION. Each tier's
     * question pool is independently shuffled before selection, so repeated calls to this
     * method return a different set whenever the pool is larger than the quota. If a tier
     * has fewer questions than its quota, the remaining slots are filled from the other
     * tiers to always return exactly 4 questions (or as many as exist in the pool).</p>
     */
    public List<Question> generateRetrievalCheck(String userId, String lessonId) {
        LearnerPath path = getPath(userId);
        List<Question> pool = questionRepository.findByLessonId(lessonId).stream()
                .filter(q -> q.getMinPath().ordinal() <= path.ordinal())
                .toList();

        // Partition by tier and shuffle each bucket independently for true randomisation
        List<Question> recall        = new ArrayList<>(pool.stream().filter(q -> q.getTier() == QuestionTier.RECALL).toList());
        List<Question> application   = new ArrayList<>(pool.stream().filter(q -> q.getTier() == QuestionTier.APPLICATION).toList());
        List<Question> discrimination = new ArrayList<>(pool.stream().filter(q -> q.getTier() == QuestionTier.DISCRIMINATION).toList());

        Collections.shuffle(recall);
        Collections.shuffle(application);
        Collections.shuffle(discrimination);

        // Quotas: 1 RECALL, 2 APPLICATION, 1 DISCRIMINATION
        List<Question> selected = new ArrayList<>();
        selected.addAll(recall.stream().limit(1).toList());
        selected.addAll(application.stream().limit(2).toList());
        selected.addAll(discrimination.stream().limit(1).toList());

        // Fill up to 4 from any remaining unseen questions if a tier was short
        if (selected.size() < 4) {
            Set<String> usedIds = selected.stream().map(Question::getId).collect(Collectors.toSet());
            List<Question> overflow = new ArrayList<>(pool.stream()
                    .filter(q -> !usedIds.contains(q.getId()))
                    .toList());
            Collections.shuffle(overflow);
            int needed = 4 - selected.size();
            selected.addAll(overflow.stream().limit(needed).toList());
        }

        // Present questions in a random order
        Collections.shuffle(selected);
        log.info("[Retrieval] Generated {} questions for subChunk={} user={} (pool: {} recall, {} application, {} discrimination)",
                selected.size(), lessonId, userId, recall.size(), application.size(), discrimination.size());
        return selected;
    }

    /**
     * Grade a list of answers against question correct answers.
     * Returns score as 0.0-1.0.
     *
     * <p>Grading strategy by question type:
     * <ul>
     *   <li>MULTIPLE_CHOICE, TRUE_FALSE â€” exact case-insensitive match</li>
     *   <li>WHATS_THE_OUTPUT, FILL_BLANK â€” normalised line-by-line match
     *       (handles literal \n in stored answers vs real newlines typed by learners)</li>
     *   <li>DEBUGGING, SCENARIO, COMPARE_CONTRAST, CODE_COMPLETION â€” keyword overlap:
     *       at least 50% of significant tokens from the correct answer must appear
     *       in the learner's response (semantic equivalence rather than word-for-word)</li>
     * </ul>
     */
    public GradeResult gradeAnswers(List<AnswerPair> answers) {
        int correct = 0;
        List<QuestionResult> results = new ArrayList<>();

        for (AnswerPair pair : answers) {
            Question question = questionRepository.findById(pair.questionId()).orElse(null);
            if (question == null) continue;

            boolean isCorrect = isAnswerCorrect(question, pair.answer());
            if (isCorrect) correct++;

            results.add(new QuestionResult(
                    question.getId(), question.getLessonId(), isCorrect,
                    pair.answer().trim(), question.getCorrectAnswer(), question.getExplanationHtml()
            ));
        }

        double score = answers.isEmpty() ? 0.0 : (double) correct / answers.size();
        return new GradeResult(score, correct, answers.size(), results);
    }

    // â”€â”€ Grading helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private boolean isAnswerCorrect(Question question, String userAnswer) {
        String answer = userAnswer == null ? "" : userAnswer.trim();
        String correct = question.getCorrectAnswer() == null ? "" : question.getCorrectAnswer();

        return switch (question.getType()) {
            case MULTIPLE_CHOICE, MCQ, TRUE_FALSE ->
                    correct.equalsIgnoreCase(answer);

            case WHATS_THE_OUTPUT, FILL_BLANK ->
                    normalizedOutputMatch(correct, answer);

            case DEBUGGING, SCENARIO, COMPARE_CONTRAST, CODE_COMPLETION,
                 APPLICATION, DISCRIMINATION ->
                    keywordOverlapScore(correct, answer) >= 0.50;
        };
    }

    /**
     * Compares expected vs actual output line-by-line after normalising whitespace.
     * Handles both literal "\n" strings stored in JSON and real newlines typed by learners.
     */
    private boolean normalizedOutputMatch(String correct, String answer) {
        List<String> expectedLines = splitLines(correct);
        List<String> actualLines   = splitLines(answer);
        if (expectedLines.size() != actualLines.size()) return false;
        for (int i = 0; i < expectedLines.size(); i++) {
            if (!expectedLines.get(i).equalsIgnoreCase(actualLines.get(i))) return false;
        }
        return true;
    }

    private List<String> splitLines(String text) {
        // Normalise literal \n (from JSON) to real newlines, then split
        return Arrays.stream(text.replace("\\n", "\n").split("\r?\n"))
                .map(String::trim)
                .filter(l -> !l.isEmpty())
                .toList();
    }

    /**
     * Scores the fraction of significant tokens from {@code correct} that appear
     * anywhere in {@code answer} (case-insensitive substring match per token).
     * Tokens shorter than 2 characters or in the stop-word list are excluded.
     */
    private double keywordOverlapScore(String correct, String answer) {
        Set<String> keyTokens = extractKeyTokens(correct);
        if (keyTokens.isEmpty()) return 1.0; // no tokens to check â†’ give benefit of doubt
        String lowerAnswer = answer.toLowerCase();
        long matched = keyTokens.stream().filter(lowerAnswer::contains).count();
        double score = (double) matched / keyTokens.size();
        log.debug("[Grading] keyword overlap {}/{} ({}%) correct='{}' answer='{}'",
                matched, keyTokens.size(), Math.round(score * 100), correct, answer);
        return score;
    }

    private static final Set<String> STOP_WORDS = Set.of(
            "the", "an", "is", "it", "in", "on", "at", "to", "for", "of", "and",
            "or", "but", "not", "use", "uses", "should", "would", "instead", "this",
            "that", "with", "from", "by", "be", "are", "was", "were", "will", "can",
            "has", "have", "had", "do", "does", "did", "which", "when", "where",
            "how", "what", "why", "if", "then", "else", "so", "than", "as", "we",
            "you", "they", "them", "its", "our", "your", "their", "always", "never"
    );

    private Set<String> extractKeyTokens(String text) {
        // Split on anything that isn't alphanumeric, dot, underscore, equals, angle bracket,
        // exclamation or parenthesis â€” keeping code-style tokens intact (e.g. s.equals, ==)
        return Stream.of(text.toLowerCase().split("[^a-z0-9_.=<>!()]+"))
                .map(String::trim)
                .filter(t -> t.length() >= 2)
                .filter(t -> !STOP_WORDS.contains(t))
                .collect(Collectors.toSet());
    }

    /**
     * Create and save a review session with results.
     */
    @Transactional
    public ReviewSession saveSession(String userId, SessionType type, GradeResult result) {
        ReviewSession session = ReviewSession.builder()
                .userId(userId)
                .sessionType(type)
                .startedAt(Instant.now())
                .completedAt(Instant.now())
                .totalQuestions(result.total())
                .correctAnswers(result.correct())
                .score(result.score())
                .build();
        ReviewSession saved = sessionRepository.save(session);

        // Telemetry: one event per session, plus per-answer grade events for granular dashboards
        String cadence = type.name();
        telemetry.reviewSessionCompleted(userId, cadence, result.correct(), result.total());
        for (QuestionResult qr : result.results()) {
            telemetry.reviewGradeGiven(userId, cadence, qr.correct() ? 1.0 : 0.0);
        }

        return saved;
    }

    private LearnerPath getPath(String userId) {
        return profileRepository.findByUserId(userId)
                .map(UserLearnerProfile::getCurrentPath)
                .orElse(LearnerPath.FOUNDATION);
    }

}
