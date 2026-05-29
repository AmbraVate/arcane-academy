package com.ambravate.arcane.academy.admin.service;

import com.ambravate.arcane.academy.admin.dto.AdminStatsDto;
import com.ambravate.arcane.academy.admin.dto.AdminUserDto;
import com.ambravate.arcane.academy.admin.dto.ContentHealthDto;
import com.ambravate.arcane.academy.admin.dto.DailyCount;
import com.ambravate.arcane.academy.admin.dto.TopicEngagementItem;
import com.ambravate.arcane.academy.admin.dto.UserStatsDto;
import com.ambravate.arcane.academy.admin.dto.XpBucket;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonPracticeType;
import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.practice.repository.ReviewSessionRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.content.repository.DomainRepository;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStatsService {

    private final UserRepository userRepository;
    private final DomainRepository domainRepository;
    private final LearningModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final QuestionRepository questionRepository;
    private final UserChunkProgressRepository progressRepository;
    private final GamificationFacade gamificationFacade;
    private final ReviewSessionRepository reviewSessionRepository;
    private final JdbcTemplate jdbc;

    public AdminStatsDto getStats() {
        Instant sevenDaysAgo = Instant.now().minusSeconds(7L * 86400);

        List<User> recent = userRepository.findTop10ByOrderByCreatedAtDesc();
        List<AdminUserDto> recentDtos = recent.stream().map(u -> toUserDto(u, 0)).toList();

        // Content health â€” subchunks missing key fields or zero questions
        List<Lesson> allSubs = lessonRepository.findAll();
        Map<String, Long> questionCounts = questionRepository.findAll().stream()
                .collect(Collectors.groupingBy(Question::getLessonId, Collectors.counting()));
        Map<String, LearningModule> chunkById = moduleRepository.findAll().stream()
                .collect(Collectors.toMap(LearningModule::getId, c -> c));

        List<ContentHealthDto> health = new ArrayList<>();
        for (Lesson sc : allSubs) {
            List<String> issues = new ArrayList<>();
            if (sc.getHookHtml() == null || sc.getHookHtml().isBlank())
                issues.add("Missing hook");
            if (sc.getExplanationHtml() == null || sc.getExplanationHtml().isBlank())
                issues.add("Missing explanation");
            // "Missing guided practice" is only meaningful for practice-type sub-chunks.
            // NONE-type sub-chunks (SQL read-only, written-response) use a different
            // practice model and may intentionally have no guided-practice HTML.
            boolean isNonePractice = sc.getPracticeType() == LessonPracticeType.NONE;
            if (!isNonePractice && (sc.getGuidedPracticeHtml() == null || sc.getGuidedPracticeHtml().isBlank()))
                issues.add("Missing guided practice");
            if (questionCounts.getOrDefault(sc.getId(), 0L) == 0)
                issues.add("No retrieval questions");

            if (!issues.isEmpty()) {
                LearningModule parent = chunkById.get(sc.getModuleId());
                health.add(ContentHealthDto.builder()
                        .lessonId(sc.getId())
                        .title(sc.getTitle())
                        .chunkTitle(parent != null ? parent.getTitle() : sc.getModuleId())
                        .domainId(parent != null ? parent.getTrackId() : null)
                        .tier(parent != null && parent.getTier() != null ? parent.getTier().name() : null)
                        .issues(issues)
                        .build());
            }
        }

        // Subscription breakdown
        Map<String, Long> subscriptionBreakdown = new LinkedHashMap<>();
        jdbc.query(
            "SELECT COALESCE(subscription_status, 'FREE') AS sub, COUNT(*) AS cnt " +
            "FROM users GROUP BY COALESCE(subscription_status, 'FREE') ORDER BY cnt DESC",
            rs -> {
                while (rs.next()) subscriptionBreakdown.put(rs.getString("sub"), rs.getLong("cnt"));
            });

        // Open stuck reports (not yet resolved)
        Long openStuckReports = jdbc.queryForObject(
            "SELECT COUNT(*) FROM stuck_reports WHERE status IN ('NEW', 'REVIEWED')", Long.class);

        // Capstones without admin feedback
        Long pendingCapstones = jdbc.queryForObject(
            "SELECT COUNT(*) FROM user_capstones WHERE admin_feedback IS NULL OR trim(admin_feedback) = ''",
            Long.class);

        // XP distribution by rank
        List<XpBucket> xpDistribution = new ArrayList<>();
        jdbc.query(
            "SELECT rank, COUNT(*) AS cnt FROM users GROUP BY rank ORDER BY MIN(total_xp)",
            rs -> {
                while (rs.next())
                    xpDistribution.add(new XpBucket(rs.getString("rank"), rs.getLong("cnt")));
            });

        // Domain engagement
        List<TopicEngagementItem> topicEngagement = new ArrayList<>();
        jdbc.query("""
            SELECT t.id AS topic_id, t.name AS topic_name, t.glyph,
                COUNT(DISTINCT sc.id)                                                    AS total_sub_chunks,
                COUNT(DISTINCT CASE WHEN ucp.status = 'COMPLETE' THEN ucp.id END)       AS total_completions,
                COUNT(DISTINCT CASE WHEN ucp.status = 'COMPLETE' THEN ucp.user_id END)  AS unique_learners
            FROM domains t
            JOIN modules c ON c.domain_id = t.id
            JOIN lessons sc ON sc.module_id = c.id
            LEFT JOIN user_chunk_progress ucp ON ucp.lesson_id = sc.id
            WHERE t.active = true
            GROUP BY t.id, t.name, t.glyph
            ORDER BY t.sort_order
            """,
            rs -> {
                while (rs.next())
                    topicEngagement.add(new TopicEngagementItem(
                        rs.getString("topic_id"),
                        rs.getString("topic_name"),
                        rs.getString("glyph"),
                        rs.getLong("total_sub_chunks"),
                        rs.getLong("total_completions"),
                        rs.getLong("unique_learners")));
            });

        return AdminStatsDto.builder()
                .totalUsers(userRepository.count())
                .activeUsers7d(userRepository.countByLastLoginAtAfter(sevenDaysAgo))
                .totalTopics(domainRepository.count())
                .totalChunks(moduleRepository.count())
                .totalSubChunks(lessonRepository.count())
                .totalQuestions(questionRepository.count())
                .totalNotes(jdbc.queryForObject("SELECT COUNT(*) FROM user_notes", Long.class))
                .totalCapstones(jdbc.queryForObject("SELECT COUNT(*) FROM user_capstones", Long.class))
                .openStuckReports(openStuckReports != null ? openStuckReports : 0L)
                .pendingCapstones(pendingCapstones != null ? pendingCapstones : 0L)
                .recentSignups(recentDtos)
                .contentHealth(health)
                .subscriptionBreakdown(subscriptionBreakdown)
                .topicEngagement(topicEngagement)
                .signupTrend(buildSignupTrend())
                .xpDistribution(xpDistribution)
                .build();
    }

    private List<DailyCount> buildSignupTrend() {
        Map<String, Long> raw = new LinkedHashMap<>();
        jdbc.query(
            "SELECT CAST(created_at AT TIME ZONE 'UTC' AS DATE)::TEXT AS day, COUNT(*) AS cnt " +
            "FROM users " +
            "WHERE created_at >= CURRENT_DATE - INTERVAL '13 days' " +
            "GROUP BY day ORDER BY day",
            rs -> {
                while (rs.next()) raw.put(rs.getString("day"), rs.getLong("cnt"));
            });
        List<DailyCount> trend = new ArrayList<>(14);
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        for (int i = 13; i >= 0; i--) {
            String date = today.minusDays(i).toString();
            trend.add(new DailyCount(date, raw.getOrDefault(date, 0L)));
        }
        return trend;
    }

    private static final DateTimeFormatter ISO_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);

    private static String formatInstant(Instant instant) {
        return instant != null ? ISO_DATE_FORMATTER.format(instant) : null;
    }

    public UserStatsDto toUserStatsDto(User u) {
        long subChunksCompleted = progressRepository.countByUserIdAndStatus(u.getId(), LessonStatus.COMPLETE);

        // Count chunks where every sub-chunk is completed by this user
        List<UserChunkProgress> userProgress = progressRepository.findByUserId(u.getId());
        java.util.Set<String> completedSubChunkIds = userProgress.stream()
                .filter(p -> p.getStatus() == LessonStatus.COMPLETE)
                .map(UserChunkProgress::getLessonId)
                .collect(java.util.stream.Collectors.toSet());
        Map<String, List<Lesson>> subChunksByChunk = lessonRepository.findAll().stream()
                .collect(Collectors.groupingBy(Lesson::getModuleId));
        long chunksCompleted = subChunksByChunk.values().stream()
                .filter(subs -> !subs.isEmpty()
                        && completedSubChunkIds.containsAll(
                                subs.stream().map(Lesson::getId).toList()))
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
