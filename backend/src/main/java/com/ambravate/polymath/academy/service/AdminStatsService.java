package com.ambravate.polymath.academy.service;

import com.ambravate.polymath.academy.dto.admin.AdminStatsDto;
import com.ambravate.polymath.academy.dto.admin.AdminUserDto;
import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    public AdminStatsDto getStats() {
        Instant sevenDaysAgo = Instant.now().minusSeconds(7L * 86400);

        List<User> recent = userRepository.findTop10ByOrderByCreatedAtDesc();
        List<AdminUserDto> recentDtos = recent.stream().map(u -> toUserDto(u, 0)).toList();

        // Content health — subchunks missing key fields or zero questions
        List<SubChunk> allSubs = subChunkRepository.findAll();
        Map<String, Long> questionCounts = questionRepository.findAll().stream()
                .collect(Collectors.groupingBy(Question::getSubChunkId, Collectors.counting()));
        Map<String, String> chunkTitles = chunkRepository.findAll().stream()
                .collect(Collectors.toMap(Chunk::getId, Chunk::getTitle));

        List<AdminStatsDto.ContentHealthDto> health = new ArrayList<>();
        for (SubChunk sc : allSubs) {
            List<String> issues = new ArrayList<>();
            if (sc.getHookHtml() == null || sc.getHookHtml().isBlank())         issues.add("Missing hook");
            if (sc.getExplanationHtml() == null || sc.getExplanationHtml().isBlank()) issues.add("Missing explanation");
            if (sc.getGuidedPracticeHtml() == null || sc.getGuidedPracticeHtml().isBlank()) issues.add("Missing guided practice");
            if (questionCounts.getOrDefault(sc.getId(), 0L) == 0)               issues.add("No questions");
            if (!issues.isEmpty()) {
                health.add(AdminStatsDto.ContentHealthDto.builder()
                        .subChunkId(sc.getId())
                        .title(sc.getTitle())
                        .chunkTitle(chunkTitles.getOrDefault(sc.getChunkId(), sc.getChunkId()))
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
                .recentSignups(recentDtos)
                .contentHealth(health)
                .build();
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
                .createdAt(u.getCreatedAt())
                .lastLoginAt(u.getLastLoginAt())
                .completedSubChunks(completedCount)
                .build();
    }
}
