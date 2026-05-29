package com.ambravate.arcane.academy.profile.service;

import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.profile.domain.LeaderboardEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Computes per-topic + polymath leaderboards on demand.
 *
 * <p>Moved from the gamification module to profile so that leaderboard's
 * read access to practice repositories does not create a module-level cycle.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LeaderboardService {

    private static final int DEFAULT_LIMIT = 20;

    private final UserRepository userRepository;
    private final UserChunkProgressRepository progressRepository;
    private final LessonRepository lessonRepository;
    private final LearningModuleRepository moduleRepository;
    private final GamificationFacade gamificationFacade;

    public List<LeaderboardEntry> topicWeekly(String domainId, int limit) {
        return topicLeaderboard(domainId, currentWeekStart(), limit);
    }

    public List<LeaderboardEntry> topicAllTime(String domainId, int limit) {
        return topicLeaderboard(domainId, Instant.EPOCH, limit);
    }

    public List<LeaderboardEntry> polymath(int limit) {
        Map<String, Integer> xpBySubChunk = xpBySubChunk();
        Map<String, String> topicBySubChunk = topicBySubChunk();
        Map<String, User> users = optedInUsers();

        Map<String, Set<String>> topicsByUser = new HashMap<>();
        Map<String, Integer> totalXpByUser = new HashMap<>();

        for (UserChunkProgress p : progressRepository.findAll()) {
            if (!users.containsKey(p.getUserId())) continue;
            if (p.getStatus() != LessonStatus.COMPLETE) continue;
            String topic = topicBySubChunk.get(p.getLessonId());
            if (topic == null) continue;
            topicsByUser.computeIfAbsent(p.getUserId(), k -> new HashSet<>()).add(topic);
            totalXpByUser.merge(p.getUserId(), xpBySubChunk.getOrDefault(p.getLessonId(), 0), Integer::sum);
        }

        List<Map.Entry<String, Set<String>>> sorted = new ArrayList<>(topicsByUser.entrySet());
        sorted.sort((a, b) -> {
            int byTopics = Integer.compare(b.getValue().size(), a.getValue().size());
            if (byTopics != 0) return byTopics;
            return Integer.compare(
                totalXpByUser.getOrDefault(b.getKey(), 0),
                totalXpByUser.getOrDefault(a.getKey(), 0));
        });

        List<LeaderboardEntry> out = new ArrayList<>();
        int rank = 1;
        for (var entry : sorted) {
            if (out.size() >= limit) break;
            User u = users.get(entry.getKey());
            out.add(new LeaderboardEntry(
                rank++, u.getUsername(),
                totalXpByUser.getOrDefault(u.getId(), 0),
                u.getTotalXp(), u.getStreakDays(), u.getRank(),
                entry.getValue().size(),
                gamificationFacade.getBadgeCount(u.getId()),
                u.getLocation()));
        }
        return out;
    }

    private List<LeaderboardEntry> topicLeaderboard(String domainId, Instant since, int limit) {
        Set<String> topicSubChunkIds = topicSubChunkIds(domainId);
        if (topicSubChunkIds.isEmpty()) return List.of();

        Map<String, Integer> xpBySubChunk = xpBySubChunk();
        Map<String, User> users = optedInUsers();

        Map<String, Integer> xpByUser = new HashMap<>();
        for (UserChunkProgress p : progressRepository.findAll()) {
            if (!users.containsKey(p.getUserId())) continue;
            if (p.getStatus() != LessonStatus.COMPLETE) continue;
            if (p.getCompletedAt() == null || p.getCompletedAt().isBefore(since)) continue;
            if (!topicSubChunkIds.contains(p.getLessonId())) continue;
            xpByUser.merge(p.getUserId(), xpBySubChunk.getOrDefault(p.getLessonId(), 0), Integer::sum);
        }

        List<Map.Entry<String, Integer>> sorted = xpByUser.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit).toList();

        List<LeaderboardEntry> out = new ArrayList<>(sorted.size());
        int rank = 1;
        for (var e : sorted) {
            User u = users.get(e.getKey());
            out.add(new LeaderboardEntry(
                rank++, u.getUsername(), e.getValue(),
                u.getTotalXp(), u.getStreakDays(), u.getRank(),
                -1, gamificationFacade.getBadgeCount(u.getId()),
                u.getLocation()));
        }
        return out;
    }

    private Map<String, User> optedInUsers() {
        return userRepository.findByPublicProfileEnabledTrue().stream()
            .collect(Collectors.toMap(User::getId, u -> u));
    }

    private Map<String, Integer> xpBySubChunk() {
        return lessonRepository.findAll().stream()
            .collect(Collectors.toMap(Lesson::getId, Lesson::getXpReward, (a, b) -> a));
    }

    private Map<String, String> topicBySubChunk() {
        Map<String, String> chunkTopic = moduleRepository.findAll().stream()
            .collect(Collectors.toMap(LearningModule::getId, LearningModule::getDomainId, (a, b) -> a));
        Map<String, String> out = new HashMap<>();
        for (Lesson sc : lessonRepository.findAll()) {
            String topic = chunkTopic.get(sc.getModuleId());
            if (topic != null) out.put(sc.getId(), topic);
        }
        return out;
    }

    private Set<String> topicSubChunkIds(String domainId) {
        List<String> chunkIds = moduleRepository.findByDomainIdOrderBySortOrderAsc(domainId)
            .stream().map(LearningModule::getId).toList();
        if (chunkIds.isEmpty()) return Set.of();
        return lessonRepository.findByModuleIdIn(chunkIds).stream()
            .map(Lesson::getId).collect(Collectors.toSet());
    }

    static Instant currentWeekStart() {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        LocalDate monday = today.minusDays(today.getDayOfWeek().getValue() - 1);
        return monday.atStartOfDay(ZoneOffset.UTC).toInstant();
    }

    public static Instant weekStartFor(LocalDate date) {
        LocalDate monday = date.minusDays(date.getDayOfWeek().getValue() - 1);
        return monday.atStartOfDay(ZoneOffset.UTC).toInstant();
    }

    public int defaultLimit() { return DEFAULT_LIMIT; }
}
