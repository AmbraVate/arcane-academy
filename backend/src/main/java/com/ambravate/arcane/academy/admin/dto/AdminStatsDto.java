package com.ambravate.arcane.academy.admin.dto;

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

}
