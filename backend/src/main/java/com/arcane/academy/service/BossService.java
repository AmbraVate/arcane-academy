package com.arcane.academy.service;

import com.arcane.academy.dto.*;
import com.arcane.academy.model.*;
import com.arcane.academy.repository.*;
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
public class BossService {

    private final BossRepository bossRepository;
    private final QuestRepository questRepository;
    private final UserProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public List<BossDto> getAllWithProgress(String userId) {
        Set<String> completedIds = getCompletedIds(userId);
        List<Quest> allQuests = questRepository.findAllByOrderByChapterNumberAscOrderInChapterAsc();

        return bossRepository.findAllByOrderByChapterNumberAsc().stream().map(b -> {
            boolean completed = completedIds.contains(b.getId());
            boolean locked = isBossLocked(b, allQuests, completedIds);
            return BossDto.builder()
                    .id(b.getId()).name(b.getName()).glyph(b.getGlyph())
                    .chapterNumber(b.getChapterNumber()).xpReward(b.getXpReward())
                    .intro(b.getIntro()).questions(parseQuestions(b.getQuestionsJson()))
                    .completed(completed).locked(locked)
                    .build();
        }).collect(Collectors.toList());
    }

    /** Boss is locked until ALL quests in its chapter are completed. */
    private boolean isBossLocked(Boss boss, List<Quest> allQuests, Set<String> completedIds) {
        return allQuests.stream()
                .filter(q -> q.getChapterNumber() == boss.getChapterNumber())
                .anyMatch(q -> !completedIds.contains(q.getId()));
    }

    public BossDto getById(String bossId, String userId) {
        Boss boss = bossRepository.findById(bossId)
                .orElseThrow(() -> new NoSuchElementException("Boss not found: " + bossId));
        Set<String> completedIds = getCompletedIds(userId);
        List<Quest> allQuests = questRepository.findAllByOrderByChapterNumberAscOrderInChapterAsc();
        boolean locked = isBossLocked(boss, allQuests, completedIds);
        if (locked) throw new IllegalStateException("Boss is locked. Complete all quests in this chapter first.");

        return BossDto.builder()
                .id(boss.getId()).name(boss.getName()).glyph(boss.getGlyph())
                .chapterNumber(boss.getChapterNumber()).xpReward(boss.getXpReward())
                .intro(boss.getIntro()).questions(parseQuestions(boss.getQuestionsJson()))
                .completed(completedIds.contains(bossId)).locked(false)
                .build();
    }

    public BossAnswerResponse checkAnswer(String bossId, BossAnswerRequest request) {
        Boss boss = bossRepository.findById(bossId).orElseThrow();
        List<Map<String, Object>> questions = parseRawQuestions(boss.getQuestionsJson());
        Map<String, Object> question = questions.stream()
                .filter(q -> request.getQuestionId().equals(q.get("id")))
                .findFirst().orElseThrow();
        String correct = String.valueOf(question.get("correct")).trim();
        String given = request.getAnswer() == null ? "" : request.getAnswer().trim();
        return BossAnswerResponse.builder()
                .correct(correct.equalsIgnoreCase(given))
                .explanation(String.valueOf(question.get("explanation")))
                .correctAnswer(correct)
                .build();
    }

    @Transactional
    public ProgressResponse defeatBoss(String bossId, String userId) {
        Boss boss = bossRepository.findById(bossId).orElseThrow();
        boolean alreadyDone = progressRepository.existsByUserIdAndItemIdAndItemType(userId, bossId, UserProgress.ItemType.BOSS);
        int xpEarned = 0;
        if (!alreadyDone) {
            xpEarned = boss.getXpReward();
            progressRepository.save(UserProgress.builder().userId(userId).itemId(bossId)
                    .itemType(UserProgress.ItemType.BOSS).completed(true).xpEarned(xpEarned).build());
            int finalXp = xpEarned;
            userRepository.findById(userId).ifPresent(user -> {
                int newXp = user.getTotalXp() + finalXp;
                user.setTotalXp(newXp);
                user.setRank(calculateRank(newXp));
                userRepository.save(user);
            });
        }
        User user = userRepository.findById(userId).orElseThrow();
        return ProgressResponse.builder().xpEarned(xpEarned).totalXp(user.getTotalXp()).rank(user.getRank()).build();
    }

    private String calculateRank(int xp) {
        if (xp >= 4000) return "Archmage";
        if (xp >= 2000) return "Mage";
        if (xp >= 1000) return "Adept";
        if (xp >= 400)  return "Apprentice";
        return "Novice";
    }

    private Set<String> getCompletedIds(String userId) {
        return progressRepository.findByUserId(userId).stream()
                .filter(UserProgress::isCompleted).map(UserProgress::getItemId).collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    private List<Object> parseQuestions(String json) {
        if (json == null) return List.of();
        try {
            List<Map<String, Object>> raw = objectMapper.readValue(json, new TypeReference<>() {});
            return raw.stream().map(q -> { Map<String, Object> s = new LinkedHashMap<>(q); s.remove("correct"); return (Object) s; }).collect(Collectors.toList());
        } catch (Exception e) { return List.of(); }
    }

    private List<Map<String, Object>> parseRawQuestions(String json) {
        if (json == null) return List.of();
        try { return objectMapper.readValue(json, new TypeReference<>() {}); } catch (Exception e) { return List.of(); }
    }
}
