// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.arcane.academy.service;

import com.arcane.academy.dto.*;
import com.arcane.academy.model.*;
import com.arcane.academy.repository.*;
import com.arcane.academy.runner.JavaCodeRunner;
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
    private final ObjectMapper objectMapper;

    public List<QuestSummaryDto> getAllWithProgress(String userId) {
        Set<String> completedIds = getCompletedIds(userId);
        List<Quest> allQuests = questRepository.findAllByOrderByChapterNumberAscOrderInChapterAsc();
        List<Boss> allBosses = bossRepository.findAllByOrderByChapterNumberAsc();

        return allQuests.stream().map(q -> {
            boolean completed = completedIds.contains(q.getId());
            boolean locked = isQuestLocked(q, allQuests, allBosses, completedIds);
            return QuestSummaryDto.builder()
                    .id(q.getId()).title(q.getTitle()).eyebrow(q.getEyebrow())
                    .topic(q.getTopic()).chapterNumber(q.getChapterNumber())
                    .orderInChapter(q.getOrderInChapter()).xpReward(q.getXpReward())
                    .completed(completed).locked(locked)
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * A quest is locked if:
     * 1. Any quest before it in the same chapter is not completed, OR
     * 2. The previous chapter's boss is not completed (for chapter > 1)
     */
    private boolean isQuestLocked(Quest quest, List<Quest> allQuests,
                                   List<Boss> allBosses, Set<String> completedIds) {
        int chapter = quest.getChapterNumber();
        int order = quest.getOrderInChapter();

        // Chapter 1, Quest 1 is always unlocked
        if (chapter == 1 && order == 1) return false;

        // First quest of a chapter (order == 1, chapter > 1): requires previous chapter boss complete
        if (order == 1 && chapter > 1) {
            String prevBossId = "ch" + (chapter - 1) + "-boss";
            return !completedIds.contains(prevBossId);
        }

        // Any other quest: requires all previous quests in same chapter to be complete
        boolean prevDone = allQuests.stream()
                .filter(q -> q.getChapterNumber() == chapter && q.getOrderInChapter() == order - 1)
                .allMatch(q -> completedIds.contains(q.getId()));
        return !prevDone;
    }

    public QuestDetailDto getQuestDetail(String questId, String userId) {
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new NoSuchElementException("Quest not found: " + questId));
        Set<String> completedIds = getCompletedIds(userId);
        List<Quest> allQuests = questRepository.findAllByOrderByChapterNumberAscOrderInChapterAsc();
        List<Boss> allBosses = bossRepository.findAllByOrderByChapterNumberAsc();

        boolean locked = isQuestLocked(quest, allQuests, allBosses, completedIds);
        if (locked) throw new IllegalStateException("Quest is locked. Complete previous quests first.");

        boolean completed = completedIds.contains(questId);
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
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new NoSuchElementException("Quest not found: " + questId));

        List<Map<String, Object>> testCases = parseTestCases(quest.getTestCasesJson());
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
            results.add(SubmitResponse.TestResult.builder()
                    .label(label).passed(passed).actualOutput(actual).expectedOutput(expected).build());
            if (!passed) { allPassed = false; failedLabels.add(label); }
        }

        int xpEarned = 0;
        String mentorFeedback = null;

        if (allPassed) {
            xpEarned = quest.getXpReward();
            awardXp(userId, questId, xpEarned, UserProgress.ItemType.QUEST);
        } else {
            String plainProblem = quest.getProblemHtml().replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").trim();
            mentorFeedback = aiMentorService.getFeedback(quest.getTitle(), quest.getTopic(),
                    plainProblem, code, String.join(", ", failedLabels));
        }

        return SubmitResponse.builder().allPassed(allPassed).testResults(results)
                .xpEarned(xpEarned).mentorFeedback(mentorFeedback).build();
    }

    @Transactional
    public ProgressResponse markComplete(String questId, String userId) {
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new NoSuchElementException("Quest not found: " + questId));
        int xp = awardXp(userId, questId, quest.getXpReward(), UserProgress.ItemType.QUEST);
        User user = userRepository.findById(userId).orElseThrow();
        return ProgressResponse.builder().xpEarned(xp).totalXp(user.getTotalXp()).rank(user.getRank()).build();
    }

    private int awardXp(String userId, String itemId, int xp, UserProgress.ItemType type) {
        if (progressRepository.existsByUserIdAndItemIdAndItemType(userId, itemId, type)) return 0;
        progressRepository.save(UserProgress.builder().userId(userId).itemId(itemId)
                .itemType(type).completed(true).xpEarned(xp).build());
        userRepository.findById(userId).ifPresent(user -> {
            int newXp = user.getTotalXp() + xp;
            user.setTotalXp(newXp);
            user.setRank(calculateRank(newXp));
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
