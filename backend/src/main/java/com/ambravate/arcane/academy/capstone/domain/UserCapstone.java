package com.ambravate.arcane.academy.capstone.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;

@Entity
@Table(name = "user_capstones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserCapstone {

    @Id
    @UuidGenerator
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "chunk_id", nullable = false)
    private String chunkId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "code_content", columnDefinition = "TEXT")
    private String codeContent;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "admin_feedback", columnDefinition = "TEXT")
    private String adminFeedback;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
