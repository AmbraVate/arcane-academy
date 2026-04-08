package com.ambravate.polymath.academy.service;

import com.ambravate.polymath.academy.dto.BadgeDto;
import com.ambravate.polymath.academy.model.BadgeDefinition;
import com.ambravate.polymath.academy.model.Quest;
import com.ambravate.polymath.academy.model.User;
import com.ambravate.polymath.academy.model.UserBadge;
import com.ambravate.polymath.academy.model.UserProgress;
import com.ambravate.polymath.academy.repository.BossRepository;
import com.ambravate.polymath.academy.repository.QuestRepository;
import com.ambravate.polymath.academy.repository.UserBadgeRepository;
import com.ambravate.polymath.academy.repository.UserProgressRepository;
import com.ambravate.polymath.academy.repository.UserRepository;
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
public class BadgeService {

    private final UserBadgeRepository badgeRepository;
    private final UserProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final BossRepository bossRepository;

    public List<BadgeDto> getAllForUser(String userId) {
        Map<String, UserBadge> earned = badgeRepository.findByUserId(userId).stream()
                .collect(Collectors.toMap(UserBadge::getBadgeId, b -> b));

        return Arrays.stream(BadgeDefinition.values()).map(def -> BadgeDto.builder()
                .id(def.name())
                .displayName(def.getDisplayName())
                .description(def.getDescription())
                .glyph(def.getGlyph())
                .category(def.getCategory().name())
                .earned(earned.containsKey(def.name()))
                .earnedAt(earned.containsKey(def.name()) ? earned.get(def.name()).getEarnedAt() : null)
                .build()
        ).collect(Collectors.toList());
    }

    public List<BadgeDto> getEarnedForUser(String userId) {
        return getAllForUser(userId).stream().filter(BadgeDto::isEarned).collect(Collectors.toList());
    }

    @Transactional
    public List<BadgeDto> evaluateAndAward(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return List.of();

        Set<String> alreadyEarned = badgeRepository.findByUserId(userId).stream()
                .map(UserBadge::getBadgeId).collect(Collectors.toSet());

        Set<String> completedIds = progressRepository.findByUserId(userId).stream()
                .filter(UserProgress::isCompleted)
                .map(UserProgress::getItemId)
                .collect(Collectors.toSet());

        long questsDone = completedIds.stream().filter(id -> id.matches("ch\\d+-q\\d+")).count();
        long bossesDone = completedIds.stream().filter(id -> id.endsWith("-boss")).count();
        long totalBosses = bossRepository.count();

        List<BadgeDto> newBadges = new ArrayList<>();

        for (BadgeDefinition def : BadgeDefinition.values()) {
            if (alreadyEarned.contains(def.name())) continue;
            if (!checkCondition(def, user, completedIds, questsDone, bossesDone, totalBosses)) continue;

            badgeRepository.save(UserBadge.builder()
                    .userId(userId).badgeId(def.name()).earnedAt(Instant.now()).build());

            newBadges.add(BadgeDto.builder()
                    .id(def.name())
                    .displayName(def.getDisplayName())
                    .description(def.getDescription())
                    .glyph(def.getGlyph())
                    .category(def.getCategory().name())
                    .earned(true)
                    .earnedAt(Instant.now())
                    .build());

            log.info("[BadgeService] Badge awarded | userId={} badge={}", userId, def.name());
        }

        return newBadges;
    }

    private boolean checkCondition(BadgeDefinition def, User user,
                                   Set<String> completedIds, long questsDone,
                                   long bossesDone, long totalBosses) {
        return switch (def) {
            case FIRST_SPELL -> questsDone >= 1;
            case CHAPTER_I_COMPLETE -> allMainQuestsComplete(1, completedIds);
            case CHAPTER_II_COMPLETE -> allMainQuestsComplete(2, completedIds);
            case CHAPTER_III_COMPLETE -> allMainQuestsComplete(3, completedIds);
            case CHAPTER_IV_COMPLETE -> allMainQuestsComplete(4, completedIds);
            case QUEST_MASTER -> questsDone >= 25;
            case FIRST_BOSS -> bossesDone >= 1;
            case ALL_BOSSES -> bossesDone >= totalBosses && totalBosses > 0;
            case XP_100 -> user.getTotalXp() >= 100;
            case XP_500 -> user.getTotalXp() >= 500;
            case XP_1000 -> user.getTotalXp() >= 1000;
            case XP_2500 -> user.getTotalXp() >= 2500;
            case XP_5000 -> user.getTotalXp() >= 5000;
            case STREAK_3 -> user.getStreakDays() >= 3;
            case STREAK_7 -> user.getStreakDays() >= 7;
            case STREAK_30 -> user.getStreakDays() >= 30;
        };
    }

    private boolean allMainQuestsComplete(int chapter, Set<String> completedIds) {
        List<Quest> mainQuests = questRepository
                .findAllByOrderByChapterNumberAscOrderInChapterAsc().stream()
                .filter(q -> q.getChapterNumber() == chapter && !q.isSideQuest())
                .toList();
        return !mainQuests.isEmpty() && mainQuests.stream()
                .allMatch(q -> completedIds.contains(q.getId()));
    }
}
