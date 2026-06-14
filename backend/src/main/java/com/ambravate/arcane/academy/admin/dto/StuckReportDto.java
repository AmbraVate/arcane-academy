package com.ambravate.arcane.academy.admin.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record StuckReportDto(
        String id,
        String userId,
        String username,
        String email,
        String domainId,
        String lessonId,
        String currentPhase,
        String currentUrl,
        String userMessage,
        String userAgent,
        String status,
        String adminNotes,
        String screenshotData,
        Instant createdAt,
        Instant updatedAt
) {}
