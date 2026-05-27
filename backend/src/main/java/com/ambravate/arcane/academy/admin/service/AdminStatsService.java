package com.ambravate.arcane.academy.admin.service;

import com.ambravate.arcane.academy.admin.dto.AdminStatsDto;
import com.ambravate.arcane.academy.admin.dto.AdminUserDto;
import com.ambravate.arcane.academy.admin.dto.ContentHealthDto;
import com.ambravate.arcane.academy.admin.dto.UserStatsDto;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkPracticeType;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.content.repository.ChunkRepository;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.practice.repository.ReviewSessionRepository;
import com.ambravate.arcane.academy.content.repository.SubChunkRepository;
import com.ambravate.arcane.academy.content.repository.TopicRepository;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStatsService {

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final ChunkRepository chunkRepository;
    private final SubChunkRepository subChunkRepository;
    private final QuestionRepository questionRepository;
    private final UserChunkProgressRepository progressRepository;
    private final GamificationFacade gamificationFacade;
    private final ReviewSessionRepository reviewSessionRepository;
    private final JdbcTemplate jdbc;

    public AdminStatsDto getStats() {
        Instant sevenDaysAgo = Instant.now().minusSeconds(7L * 86400);

        List<User> recent = userRepository.findTop10ByOrderByCreatedAtDesc();
        List<AdminUserDto> recentDtos = recent.stream().map(u -> toUserDto(u, 0)).toList();

        // Content health — subchunks missing key fields or zero questions
        List<SubChunk> allSubs = subChunkRepository.findAll();
        Map<String, Long> questionCounts = questionRepository.findAll().stream()
                .collect(Collectors.groupingBy(Question::getSubChunkId, Collectors.counting()));
        Map<String, Chunk> chunkById = chunkRepository.findAll().stream()
                .collect(Collectors.toMap(Chunk::getId, c -> c));

        List<ContentHealthDto> health = new ArrayList<>();
        for (SubChunk sc : allSubs) {
            List<String> issues = new ArrayList<>();
            if (sc.getHookHtml() == null || sc.getHookHtml().isBlank())
                issues.add("Missing hook");
            if (sc.getExplanationHtml() == null || sc.getExplanationHtml().isBlank())
                issues.add("Missing explanation");
            // "Missing guided practice" is only meaningful for practice-type sub-chunks.
            // NONE-type sub-chunks (SQL read-only, written-response) use a different
            // practice model and may intentionally have no guided-practice HTML.
            boolean isNonePractice = sc.getPracticeType() == SubChunkPracticeType.NONE;
            if (!isNonePractice && (sc.getGuidedPracticeHtml() == null || sc.getGuidedPracticeHtml().isBlank()))
                issues.add("Missing guided practice");
            if (questionCounts.getOrDefault(sc.getId(), 0L) == 0)
                issues.add("No retrieval questions");

            if (!issues.isEmpty()) {
                Chunk parent = chunkById.get(sc.getChunkId());
                health.add(ContentHealthDto.builder()
                        .subChunkId(sc.getId())
                        .title(sc.getTitle())
                        .chunkTitle(parent != null ? parent.getTitle() : sc.getChunkId())
                        .topicId(parent != null ? parent.getTopicId() : null)
                        .tier(parent != null && parent.getTier() != null ? parent.getTier().name() : null)
                        .issues(issues)
                        .build());
            }
        }

        return AdminStatsDto.builder()
                .totalUsers(userRepository.count())
                .activeUsers7d(userRepository.countByLastLoginAtAfter(sevenDaysAgo))
                .totalTopics(topicRepository.count())
                .totalChunks(chunkRepository.count())
                .totalSubChunks(subChunkRepository.count())
                .totalQuestions(questionRepository.count())
                .totalNotes(jdbc.queryForObject("SELECT COUNT(*) FROM user_notes", Long.class))
                .totalCapstones(jdbc.queryForObject("SELECT COUNT(*) FROM user_capstones", Long.class))
                .recentSignups(recentDtos)
                .contentHealth(health)
                .build();
    }

    private static final DateTimeFormatter ISO_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);

    private static String formatInstant(Instant instant) {
        return instant != null ? ISO_DATE_FORMATTER.format(instant) : null;
    }

    public UserStatsDto toUserStatsDto(User u) {
        long subChunksCompleted = progressRepository.countByUserIdAndStatus(u.getId(), SubChunkStatus.COMPLETE);

        // Count chunks where every sub-chunk is completed by this user
        List<UserChunkProgress> userProgress = progressRepository.findByUserId(u.getId());
        java.util.Set<String> completedSubChunkIds = userProgress.stream()
                .filter(p -> p.getStatus() == SubChunkStatus.COMPLETE)
                .map(UserChunkProgress::getSubChunkId)
                .collect(java.util.stream.Collectors.toSet());
        Map<String, List<SubChunk>> subChunksByChunk = subChunkRepository.findAll().stream()
                .collect(Collectors.groupingBy(SubChunk::getChunkId));
        long chunksCompleted = subChunksByChunk.values().stream()
                .filter(subs -> !subs.isEmpty()
                        && completedSubChunkIds.containsAll(
                                subs.stream().map(SubChunk::getId).toList()))
                .count();

        long badgesEarned = gamificationFacade.getBadgeCount(u.getId());
        long reviewSessionsCompleted = reviewSessionRepository.countByUserIdAndCompletedAtIsNotNull(u.getId());

        return new UserStatsDto(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getTotalXp(),
                u.getRank(),
                u.getStreakDays(),
                subChunksCompleted,
                chunksCompleted,
                badgesEarned,
                reviewSessionsCompleted,
                formatInstant(u.getCreatedAt()),
                formatInstant(u.getLastLoginAt()),
                u.isBlocked(),
                u.getRole().name()
        );
    }

    public AdminUserDto toUserDto(User u, long completedCount) {
        return AdminUserDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .rank(u.getRank())
                .totalXp(u.getTotalXp())
                .streakDays(u.getStreakDays())
                .authProvider(u.getAuthProvider().name())
                .role(u.getRole().name())
                .blocked(u.isBlocked())
                .bypassPaywall(u.isBypassPaywall())
                .subscriptionStatus(u.getSubscriptionStatus() != null ? u.getSubscriptionStatus().name() : "FREE")
                .createdAt(u.getCreatedAt())
                .lastLoginAt(u.getLastLoginAt())
                .completedSubChunks(completedCount)
                .build();
    }
}
