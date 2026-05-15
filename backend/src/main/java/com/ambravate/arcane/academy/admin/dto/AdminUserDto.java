package com.ambravate.arcane.academy.admin.dto;

import lombok.*;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminUserDto {
    private String id;
    private String username;
    private String email;
    private String rank;
    private int totalXp;
    private int streakDays;
    private String authProvider;
    private String role;
    private boolean blocked;
    private Instant createdAt;
    private Instant lastLoginAt;
    private long completedSubChunks;
}
