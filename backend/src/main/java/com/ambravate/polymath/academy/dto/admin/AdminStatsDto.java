package com.ambravate.polymath.academy.dto.admin;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminStatsDto {
    private long totalUsers;
    private long activeUsers7d;
    private long totalTopics;
    private long totalChunks;
    private long totalSubChunks;
    private long totalQuestions;
    private List<AdminUserDto> recentSignups;
    private List<ContentHealthDto> contentHealth;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ContentHealthDto {
        private String subChunkId;
        private String title;
        private String chunkTitle;
        /** Top-level subject area (e.g. "java", "sql", "tailwind"). */
        private String topicId;
        /** Tier within the topic (e.g. "FOUNDATION", "PRACTITIONER"). */
        private String tier;
        private List<String> issues;
    }
}
