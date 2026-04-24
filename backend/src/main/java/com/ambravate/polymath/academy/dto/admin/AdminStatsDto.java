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
        private List<String> issues;
    }
}
