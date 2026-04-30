package com.ambravate.polymath.academy.service;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Computes per-topic + polymath leaderboards on demand.
 *
 * <p><b>Privacy.</b> Only users with {@code publicProfileEnabled=true} appear here.
 * Anyone can opt in/out from the profile page.
 *
 * <p><b>XP source.</b> XP "earned in window" is the sum of {@link SubChunk#getXpReward()}
 * for sub-chunks the user marked COMPLETE within the window. We do NOT use
 * {@link User#getTotalXp()} for windowed numbers because that ledger is global
 * and includes review XP that we don't want to attribute to a topic.
 *
 * <p><b>Performance.</b> v1 does in-memory aggregation over the full progress
 * table once per request. Acceptable up to ~10k completed sub-chunks; switch
 * to a native SQL aggregation if the table grows past that.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LeaderboardService {

    private static final int DEFAULT_LIMIT = 20;

    private final UserRepository userRepository;
    private final UserChunkProgressRepository progressRepository;
    private final SubChunkRepository subChunkRepository;
    private final ChunkRepository chunkRepository;
    private final UserBadgeRepository badgeRepository;

    /** Top users by XP earned in {@code topicId} since the start of the current ISO-week (Mon 00:00 UTC). */
    public List<LeaderboardEntry> topicWeekly(String topicId, int limit) {
        return topicLeaderboard(topicId, currentWeekStart(), limit);
    }

    /** Top users by total XP earned in {@code topicId} since account creation. */
    public List<LeaderboardEntry> topicAllTime(String topicId, int limit) {
        return topicLeaderboard(topicId, Instant.EPOCH, limit);
    }

    /**
     * Polymath ranking — number of distinct topics where the user has earned ≥1 sub-chunk XP,
     * tie-broken by total XP across all topics.
     */
    public List<LeaderboardEntry> polymath(int limit) {
        Map<String, Integer> xpBySubChunk = xpBySubChunk();
        Map<String, String> topicBySubChunk = topicBySubChunk();
        Map<String, User> users = optedInUsers();

        // user → set of topics they've earned in, plus total XP
        Map<String, Set<String>> topicsByUser = new HashMap<>();
        Map<String, Integer> totalXpByUser = new HashMap<>();

        for (UserChunkProgress p : progressRepository.findAll()) {
            if (!users.containsKey(p.getUserId())) continue;
            if (p.getStatus() != SubChunkStatus.COMPLETE) continue;
            String topic = topicBySubChunk.get(p.getSubChunkId());
            if (topic == null) continue;
            topicsByUser.computeIfAbsent(p.getUserId(), k -> new HashSet<>()).add(topic);
            totalXpByUser.merge(p.getUserId(), xpBySubChunk.getOrDefault(p.getSubChunkId(), 0), Integer::sum);
        }

        List<Map.Entry<String, Set<String>>> sorted = new ArrayList<>(topicsByUser.entrySet());
        sorted.sort((a, b) -> {
            int byTopics = Integer.compare(b.getValue().size(), a.getValue().size());
            if (byTopics != 0) return byTopics;
            return Integer.compare(
                totalXpByUser.getOrDefault(b.getKey(), 0),
                totalXpByUser.getOrDefault(a.getKey(), 0)
            );
        });

        List<LeaderboardEntry> out = new ArrayList<>();
        int rank = 1;
        for (var entry : sorted) {
            if (out.size() >= limit) break;
            User u = users.get(entry.getKey());
            out.add(new LeaderboardEntry(
                rank++,
                u.getUsername(),
                totalXpByUser.getOrDefault(u.getId(), 0),
                u.getTotalXp(),
                u.getStreakDays(),
                u.getRank(),
                entry.getValue().size(),
                badgeCount(u.getId())
            ));
        }
        return out;
    }

    // ── Internals ──────────────────────────────────────────────────────────────

    private List<LeaderboardEntry> topicLeaderboard(String topicId, Instant since, int limit) {
        Set<String> topicSubChunkIds = topicSubChunkIds(topicId);
        if (topicSubChunkIds.isEmpty()) return List.of();

        Map<String, Integer> xpBySubChunk = xpBySubChunk();
        Map<String, User> users = optedInUsers();

        // Aggregate XP per user with one pass over progress table.
        Map<String, Integer> xpByUser = new HashMap<>();
        for (UserChunkProgress p : progressRepository.findAll()) {
            if (!users.containsKey(p.getUserId())) continue;
            if (p.getStatus() != SubChunkStatus.COMPLETE) continue;
            if (p.getCompletedAt() == null || p.getCompletedAt().isBefore(since)) continue;
            if (!topicSubChunkIds.contains(p.getSubChunkId())) continue;
            xpByUser.merge(p.getUserId(), xpBySubChunk.getOrDefault(p.getSubChunkId(), 0), Integer::sum);
        }

        List<Map.Entry<String, Integer>> sorted = xpByUser.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .toList();

        List<LeaderboardEntry> out = new ArrayList<>(sorted.size());
        int rank = 1;
        for (var e : sorted) {
            User u = users.get(e.getKey());
            out.add(new LeaderboardEntry(
                rank++,
                u.getUsername(),
                e.getValue(),
                u.getTotalXp(),
                u.getStreakDays(),
                u.getRank(),
                -1,                         // topicCount only meaningful for polymath
                badgeCount(u.getId())
            ));
        }
        return out;
    }

    private Map<String, User> optedInUsers() {
        return userRepository.findByPublicProfileEnabledTrue().stream()
            .collect(Collectors.toMap(User::getId, u -> u));
    }

    private Map<String, Integer> xpBySubChunk() {
        return subChunkRepository.findAll().stream()
            .collect(Collectors.toMap(SubChunk::getId, SubChunk::getXpReward, (a, b) -> a));
    }

    /** Map of subChunkId → topicId (e.g. "java", "tailwind"). */
    private Map<String, String> topicBySubChunk() {
        Map<String, String> chunkTopic = chunkRepository.findAll().stream()
            .collect(Collectors.toMap(Chunk::getId, Chunk::getTopicId, (a, b) -> a));
        Map<String, String> out = new HashMap<>();
        for (SubChunk sc : subChunkRepository.findAll()) {
            String topic = chunkTopic.get(sc.getChunkId());
            if (topic != null) out.put(sc.getId(), topic);
        }
        return out;
    }

    private Set<String> topicSubChunkIds(String topicId) {
        List<String> chunkIds = chunkRepository.findByTopicIdOrderBySortOrderAsc(topicId)
            .stream().map(Chunk::getId).toList();
        if (chunkIds.isEmpty()) return Set.of();
        return subChunkRepository.findByChunkIdIn(chunkIds).stream()
            .map(SubChunk::getId)
            .collect(Collectors.toSet());
    }

    private int badgeCount(String userId) {
        return badgeRepository.findByUserId(userId).size();
    }

    /** Monday 00:00 UTC of the current ISO week. */
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

    public record LeaderboardEntry(
        int rank,
        String username,
        int xpEarned,         // XP earned in the leaderboard window (or topic for all-time)
        int globalXp,         // user.totalXp across all topics
        int streakDays,
        String rankTitle,
        int topicCount,       // -1 if not applicable (only set for polymath board)
        int badgeCount
    ) {}
}
