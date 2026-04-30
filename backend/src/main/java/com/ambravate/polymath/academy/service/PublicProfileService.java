package com.ambravate.polymath.academy.service;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Assembles the read-only data shown at <code>/u/{username}</code>.
 *
 * <p>Returns {@link Optional#empty()} when:
 * <ul>
 *   <li>No user has the username, OR</li>
 *   <li>The user has not opted into public profiles ({@code publicProfileEnabled=false})</li>
 * </ul>
 *
 * <p>The DTO deliberately does NOT include email, role, providerId, completedAt instants
 * for individual sub-chunks, or anything that could be used for fingerprinting.
 * It surfaces: username, member-since (date only, not time), rank, streak,
 * per-topic XP + chunk completion, and earned badges.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PublicProfileService {

    private final UserRepository userRepository;
    private final UserChunkProgressRepository progressRepository;
    private final UserBadgeRepository badgeRepository;
    private final ChunkRepository chunkRepository;
    private final SubChunkRepository subChunkRepository;
    private final TopicRepository topicRepository;

    public Optional<PublicProfile> findByUsername(String username) {
        return userRepository.findByUsername(username)
            .filter(User::isPublicProfileEnabled)
            .map(this::assemble);
    }

    private PublicProfile assemble(User user) {
        // 1. Per-topic XP earned + completion stats
        Map<String, String> chunkTopic = chunkRepository.findAll().stream()
            .collect(Collectors.toMap(Chunk::getId, Chunk::getTopicId, (a, b) -> a));
        Map<String, SubChunk> subChunkById = subChunkRepository.findAll().stream()
            .collect(Collectors.toMap(SubChunk::getId, sc -> sc, (a, b) -> a));

        // topicId → totals
        Map<String, int[]> agg = new HashMap<>();   // [xpEarned, completedSubChunks]
        for (UserChunkProgress p : progressRepository.findByUserId(user.getId())) {
            if (p.getStatus() != SubChunkStatus.COMPLETE) continue;
            SubChunk sc = subChunkById.get(p.getSubChunkId());
            if (sc == null) continue;
            String topic = chunkTopic.get(sc.getChunkId());
            if (topic == null) continue;
            int[] arr = agg.computeIfAbsent(topic, k -> new int[]{0, 0});
            arr[0] += sc.getXpReward();
            arr[1] += 1;
        }

        Map<String, Topic> topicsById = topicRepository.findAll().stream()
            .collect(Collectors.toMap(Topic::getId, t -> t, (a, b) -> a));

        List<TopicEntry> topics = agg.entrySet().stream()
            .map(e -> {
                Topic t = topicsById.get(e.getKey());
                return new TopicEntry(
                    e.getKey(),
                    t != null ? t.getName() : e.getKey(),
                    t != null ? t.getGlyph() : "✦",
                    t != null ? t.getAccentColor() : null,
                    e.getValue()[0],
                    e.getValue()[1]
                );
            })
            .sorted(Comparator.comparingInt(TopicEntry::xpEarned).reversed())
            .toList();

        // 2. Earned badges (id only — frontend resolves display name from BadgeDefinition)
        List<EarnedBadge> badges = badgeRepository.findByUserId(user.getId()).stream()
            .map(b -> {
                String display = b.getBadgeId();
                String glyph = "🏅";
                String category = "OTHER";
                try {
                    BadgeDefinition def = BadgeDefinition.valueOf(b.getBadgeId());
                    display = def.getDisplayName();
                    glyph = def.getGlyph();
                    category = def.getCategory().name();
                } catch (IllegalArgumentException ignored) {
                    // unknown badge id — fall back to raw id
                }
                return new EarnedBadge(b.getBadgeId(), display, glyph, category, b.getEarnedAt());
            })
            .sorted(Comparator.comparing(EarnedBadge::earnedAt).reversed())
            .toList();

        return new PublicProfile(
            user.getUsername(),
            user.getCreatedAt(),
            user.getRank(),
            user.getTotalXp(),
            user.getStreakDays(),
            topics,
            badges
        );
    }

    // ── Read-only DTOs ─────────────────────────────────────────────────────────

    public record PublicProfile(
        String username,
        Instant memberSince,
        String rank,
        int totalXp,
        int streakDays,
        List<TopicEntry> topics,
        List<EarnedBadge> badges
    ) {}

    public record TopicEntry(
        String topicId,
        String name,
        String glyph,
        String accentColor,
        int xpEarned,
        int subChunksCompleted
    ) {}

    public record EarnedBadge(
        String id,
        String displayName,
        String glyph,
        String category,
        Instant earnedAt
    ) {}
}
