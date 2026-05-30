package com.ambravate.arcane.academy.admin.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminStatsDto {
    private long totalUsers;
    private long activeUsers7d;
    private long totalDomains;
    private long totalChunks;
    private long totalSubChunks;
    private long totalQuestions;
    private long totalNotes;
    private long totalCapstones;
    private long openStuckReports;
    private long pendingCapstones;
    private List<AdminUserDto> recentSignups;
    private List<ContentHealthDto> contentHealth;
    private Map<String, Long> subscriptionBreakdown;
    private List<DomainEngagementItem> domainEngagement;
    private List<DailyCount> signupTrend;
    private List<XpBucket> xpDistribution;
}
