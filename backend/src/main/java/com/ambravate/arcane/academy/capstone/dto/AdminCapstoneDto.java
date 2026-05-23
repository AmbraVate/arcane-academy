package com.ambravate.arcane.academy.capstone.dto;

import com.ambravate.arcane.academy.capstone.domain.UserCapstone;

import java.time.Instant;

/** Extended view for admin panel — includes userId for display. */
public record AdminCapstoneDto(
        String id,
        String userId,
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
    public static AdminCapstoneDto from(UserCapstone c) {
        return new AdminCapstoneDto(
                c.getId(), c.getUserId(), c.getChunkId(), c.getTitle(),
                c.getDescription(), c.getCodeContent(), c.getGithubUrl(),
                c.getAdminFeedback(), c.getReviewedAt(), c.getCreatedAt(), c.getUpdatedAt()
        );
    }
}
