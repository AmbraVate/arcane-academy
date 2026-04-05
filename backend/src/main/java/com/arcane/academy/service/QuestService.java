package com.arcane.academy.service;

import com.arcane.academy.dto.*;
import com.arcane.academy.model.*;
import com.arcane.academy.repository.*;
import com.arcane.academy.runner.JavaCodeRunner;
import com.arcane.academy.service.StreakService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestService {

    private final QuestRepository questRepository;
    private final BossRepository bossRepository;
    private final UserProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final JavaCodeRunner codeRunner;
    private final AiMentorService aiMentorService;
    private final StreakService streakService;
    private final ObjectMapper objectMapper;

    public List<QuestSummaryDto> getAllWithProgress(String userId) {
        log.debug("[QuestService] getAllWithProgress | userId={}", userId);
        Set<String> completedIds = getCompletedIds(userId);
        List<Quest> allQuests = questRepository.findAllByOrderByChapterNumberAscOrderInChapterAsc();
        List<Boss> allBosses = bossRepository.findAllByOrderByChapterNumberAsc();
        log.debug("[QuestService] Loaded {} quests, {} bosses, {} completedIds for user {}",
                allQuests.size(), allBosses.size(), completedIds.size(), userId);

        return allQuests.stream().map(q -> {
            boolean completed = completedIds.contains(q.getId());
            boolean locked = isQuestLocked(q, allQuests, allBosses, completedIds);
            return QuestSummaryDto.builder()
                    .id(q.getId()).title(q.getTitle()).eyebrow(q.getEyebrow())
                    .topic(q.getTopic()).chapterNumber(q.getChapterNumber())
                    .orderInChapter(q.getOrderInChapter()).xpReward(q.getXpReward())
                    .completed(completed).locked(locked).sideQuest(q.isSideQuest())
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * A quest is locked if:
     * - Regular quests: requires all previous quests in chapter complete, and prev boss for chapter > 1
     * - Side quests: unlocked whenever the chapter itself is accessible (same condition as chapter's first quest)
     */
    private boolean isQuestLocked(Quest quest, List<Quest> allQuests,
                                   List<Boss> allBosses, Set<String> completedIds) {
        int chapter = quest.getChapterNumber();
        int order = quest.getOrderInChapter();

        // Chapter 1 is always accessible
        if (chapter == 1) {
            if (quest.isSideQuest()) return false;
            if (order == 1) return false;
        }

        // Side quests: unlock when the chapter is accessible (previous chapter boss beaten)
        if (quest.isSideQuest()) {
            String prevBossId = "ch" + (chapter - 1) + "-boss";
            return !completedIds.contains(prevBossId);
        }

        // First quest of a chapter (order == 1, chapter > 1): requires previous chapter boss complete
        if (order == 1 && chapter > 1) {
            String prevBossId = "ch" + (chapter - 1) + "-boss";
            return !completedIds.contains(prevBossId);
        }

        // Any other quest: requires previous quest in same chapter to be complete
        boolean prevDone = allQuests.stream()
                .filter(q -> q.getChapterNumber() == chapter && q.getOrderInChapter() == order - 1)
                .allMatch(q -> completedIds.contains(q.getId()));
        return !prevDone;
    }

    public QuestDetailDto getQuestDetail(String questId, String userId) {
        log.info("[QuestService] getQuestDetail | questId={} userId={}", questId, userId);
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new NoSuchElementException("Quest not found: " + questId));
        Set<String> completedIds = getCompletedIds(userId);
        List<Quest> allQuests = questRepository.findAllByOrderByChapterNumberAscOrderInChapterAsc();
        List<Boss> allBosses = bossRepository.findAllByOrderByChapterNumberAsc();

        boolean locked = isQuestLocked(quest, allQuests, allBosses, completedIds);
        log.debug("[QuestService] Quest '{}' locked={} for userId={}", questId, locked, userId);
        if (locked) {
            log.warn("[QuestService] Locked quest access attempt | questId={} userId={}", questId, userId);
            throw new IllegalStateException("Quest is locked. Complete previous quests first.");
        }

        boolean completed = completedIds.contains(questId);
        log.debug("[QuestService] Quest '{}' completed={}", questId, completed);
        return QuestDetailDto.builder()
                .id(quest.getId()).title(quest.getTitle()).eyebrow(quest.getEyebrow())
                .topic(quest.getTopic()).xpReward(quest.getXpReward()).filename(quest.getFilename())
                .problemHtml(quest.getProblemHtml()).hint(quest.getHint())
                .starterCode(quest.getStarterCode()).winStory(quest.getWinStory())
                .story(parseJson(quest.getStoryJson()))
                .testCaseLabels(extractTestLabels(quest.getTestCasesJson()))
                .completed(completed)
                .build();
    }

    @Transactional
    public SubmitResponse evaluateSubmission(String questId, String code, String userId) {
        log.info("[QuestService] evaluateSubmission | questId={} userId={} codeLength={}",
                questId, userId, code != null ? code.length() : 0);

        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new NoSuchElementException("Quest not found: " + questId));

        String plainProblem = quest.getProblemHtml()
                .replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").trim();

        // Run the first test case to check for compile/runtime errors before running all tests
        List<Map<String, Object>> testCases = parseTestCases(quest.getTestCasesJson());
        log.debug("[QuestService] Quest '{}' has {} test cases", questId, testCases.size());
        if (testCases.isEmpty()) {
            log.warn("[QuestService] No test cases found for questId={}", questId);
            return SubmitResponse.builder().build();
        }

        CodeRunResponse probe = codeRunner.run(code,
                (String) testCases.get(0).getOrDefault("input", null));
        log.debug("[QuestService] Probe run status={} error='{}'",
                probe.getStatus(), probe.getError());

        // Handle compile errors — translate for the student before running any more tests
        if (probe.getStatus() == CodeRunResponse.RunStatus.COMPILE_ERROR) {
            log.info("[QuestService] COMPILE_ERROR | questId={} userId={} error='{}'",
                    questId, userId, probe.getError());
            String feedback = aiMentorService.explainCompileError(
                    quest.getTitle(), quest.getTopic(), code, probe.getError());
            return SubmitResponse.builder()
                    .allPassed(false).testResults(List.of())
                    .mentorFeedback(feedback).errorType("COMPILE_ERROR").build();
        }

        // Handle runtime errors
        if (probe.getStatus() == CodeRunResponse.RunStatus.RUNTIME_ERROR
                || probe.getStatus() == CodeRunResponse.RunStatus.TIMEOUT) {
            log.info("[QuestService] RUNTIME_ERROR | questId={} userId={} status={} error='{}'",
                    questId, userId, probe.getStatus(), probe.getError());
            String feedback = aiMentorService.explainRuntimeError(
                    quest.getTitle(), quest.getTopic(), code,
                    probe.getError() != null ? probe.getError() : "Timeout — possible infinite loop");
            return SubmitResponse.builder()
                    .allPassed(false).testResults(List.of())
                    .mentorFeedback(feedback).errorType("RUNTIME_ERROR").build();
        }

        // Run all test cases
        List<SubmitResponse.TestResult> results = new ArrayList<>();
        boolean allPassed = true;
        List<String> failedLabels = new ArrayList<>();

        for (Map<String, Object> tc : testCases) {
            String label = (String) tc.get("label");
            String input = (String) tc.getOrDefault("input", null);
            String expected = (String) tc.get("expected");
            CodeRunResponse run = codeRunner.run(code, input);
            String actual = run.getOutput() != null ? run.getOutput().trim() : "";
            boolean passed = actual.contains(expected.trim());
            log.debug("[QuestService] Test '{}' passed={} | expected='{}' actual='{}'",
                    label, passed, expected, actual.length() > 80 ? actual.substring(0, 80) + "..." : actual);
            results.add(SubmitResponse.TestResult.builder()
                    .label(label).passed(passed).actualOutput(actual).expectedOutput(expected).build());
            if (!passed) { allPassed = false; failedLabels.add(label); }
        }

        int xpEarned = 0;
        String mentorFeedback = null;

        if (allPassed) {
            log.info("[QuestService] ALL TESTS PASSED | questId={} userId={} xpReward={}",
                    questId, userId, quest.getXpReward());
            xpEarned = quest.getXpReward();
            awardXp(userId, questId, xpEarned, UserProgress.ItemType.QUEST);
        } else {
            log.info("[QuestService] TESTS FAILED | questId={} userId={} failedTests='{}'",
                    questId, userId, String.join(", ", failedLabels));
            mentorFeedback = aiMentorService.getFeedback(quest.getTitle(), quest.getTopic(),
                    plainProblem, code, String.join(", ", failedLabels));
        }

        return SubmitResponse.builder().allPassed(allPassed).testResults(results)
                .xpEarned(xpEarned).mentorFeedback(mentorFeedback).errorType("TEST_FAILURE").build();
    }

    @Transactional
    public ProgressResponse markComplete(String questId, String userId) {
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new NoSuchElementException("Quest not found: " + questId));
        int xp = awardXp(userId, questId, quest.getXpReward(), UserProgress.ItemType.QUEST);
        User user = userRepository.findById(userId).orElseThrow();
        return ProgressResponse.builder().xpEarned(xp).totalXp(user.getTotalXp())
                .rank(user.getRank()).streakDays(user.getStreakDays()).build();
    }

    private int awardXp(String userId, String itemId, int xp, UserProgress.ItemType type) {
        if (progressRepository.existsByUserIdAndItemIdAndItemType(userId, itemId, type)) {
            log.debug("[QuestService] XP already awarded for userId={} itemId={} — skipping", userId, itemId);
            return 0;
        }
        log.info("[QuestService] Awarding {} XP | userId={} itemId={} type={}", xp, userId, itemId, type);
        progressRepository.save(UserProgress.builder().userId(userId).itemId(itemId)
                .itemType(type).completed(true).xpEarned(xp).build());
        int newStreak = streakService.updateStreak(userId);
        log.debug("[QuestService] Streak updated to {} for userId={}", newStreak, userId);
        userRepository.findById(userId).ifPresent(user -> {
            int newXp = user.getTotalXp() + xp;
            String newRank = calculateRank(newXp);
            log.info("[QuestService] User progress | userId={} totalXp={} rank={}", userId, newXp, newRank);
            user.setTotalXp(newXp);
            user.setRank(newRank);
            userRepository.save(user);
        });
        return xp;
    }

    public String calculateRank(int xp) {
        if (xp >= 4000) return "Archmage";
        if (xp >= 2000) return "Mage";
        if (xp >= 1000) return "Adept";
        if (xp >= 400)  return "Apprentice";
        return "Novice";
    }

    private Set<String> getCompletedIds(String userId) {
        return progressRepository.findByUserId(userId).stream()
                .filter(UserProgress::isCompleted)
                .map(UserProgress::getItemId)
                .collect(Collectors.toSet());
    }

    private Object parseJson(String json) {
        if (json == null) return List.of();
        try { return objectMapper.readValue(json, Object.class); } catch (Exception e) { return List.of(); }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> parseTestCases(String json) {
        if (json == null) return List.of();
        try { return objectMapper.readValue(json, new TypeReference<>() {}); } catch (Exception e) { return List.of(); }
    }

    private Object extractTestLabels(String json) {
        return parseTestCases(json).stream()
                .map(tc -> Map.of("label", tc.getOrDefault("label", "")))
                .collect(Collectors.toList());
    }
}
