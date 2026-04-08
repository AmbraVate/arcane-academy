package com.ambravate.polymath.academy.dto;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProgressResponse {
    private int xpEarned;
    private int totalXp;
    private String rank;
    private int streakDays;
    private List<BadgeDto> newBadges;
}
