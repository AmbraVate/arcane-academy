package com.ambravate.arcane.academy.capstone.dto;

import com.ambravate.arcane.academy.capstone.domain.UserCapstone;

import java.time.Instant;

public record CapstoneDto(
        String id,
        String chunkId,
        String title,
        String description,
        String codeContent,
        String githubUrl,
        String adminFeedback,
        Instant reviewedAt,
        Instant createdAt,
        Instant updatedAt
) {
    public static CapstoneDto from(UserCapstone c) {
        return new CapstoneDto(
                c.getId(), c.getModuleId(), c.getTitle(), c.getDescription(),
                c.getCodeContent(), c.getGithubUrl(), c.getAdminFeedback(),
                c.getReviewedAt(), c.getCreatedAt(), c.getUpdatedAt()
        );
    }
}
