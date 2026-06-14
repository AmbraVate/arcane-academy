package com.ambravate.arcane.academy.retention.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "concept_retention")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConceptRetention {

    @Id
    @UuidGenerator
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "lesson_id", nullable = false)
    private String lessonId;

    @Column(name = "retention_score", nullable = false, precision = 4, scale = 3)
    private BigDecimal retentionScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "stability_state", nullable = false)
    private StabilityState stabilityState;

    /** JSON array of last 4 attempt scores (0 or 1), e.g. "[1,0,1,1]" */
    @Column(name = "attempt_history", nullable = false, columnDefinition = "TEXT")
    private String attemptHistory;

    @Column(name = "consecutive_correct", nullable = false)
    private short consecutiveCorrect;

    @Column(name = "next_review_date")
    private LocalDate nextReviewDate;

    @Column(name = "teach_back_score", precision = 4, scale = 3)
    private BigDecimal teachBackScore;

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
