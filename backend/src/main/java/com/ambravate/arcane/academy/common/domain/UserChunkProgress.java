package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "user_chunk_progress",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "lesson_id"}),
    indexes = {
        @Index(name = "idx_ucp_user_status",   columnList = "user_id, status"),
        @Index(name = "idx_ucp_next_review",   columnList = "next_review_at"),
        @Index(name = "idx_ucp_user_lesson",   columnList = "user_id, lesson_id"),
    })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserChunkProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "lesson_id", nullable = false)
    private String lessonId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EncodingPhase currentPhase = EncodingPhase.HOOK;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LessonStatus status = LessonStatus.NOT_STARTED;

    @Builder.Default
    private double memoryStrength = 0.0;

    @Builder.Default
    private double easeFactor = 2.5;

    @Builder.Default
    private int repetitionCount = 0;

    @Builder.Default
    private int intervalDays = 0;

    private Instant lastReviewedAt;

    private Instant nextReviewAt;

    @Builder.Default
    private double lastScore = 0.0;

    @Builder.Default
    private boolean guidedPracticePassed = false;

    @Builder.Default
    private boolean soloPracticePassed = false;

    @Builder.Default
    private boolean retrievalCheckSubmitted = false;

    @Builder.Default
    private boolean feynmanCompleted = false;

    @Builder.Default
    private double feynmanScore = 0.0;

    private Instant completedAt;

    @Builder.Default
    private Instant createdAt = Instant.now();

    /**
     * Phase 3 — JSON array of guided step IDs the user has passed.
     * Example: {@code ["var_step_1","var_step_2"]}.
     * Null means no steps attempted yet.
     */
    @Column(name = "guided_steps_completed_json", columnDefinition = "TEXT")
    private String guidedStepsCompletedJson;

    /**
     * Phase 4 — Self-reported confidence after RUBRIC_REFLECTION solo practice.
     * Values: NOT_CONFIDENT | SOMEWHAT | CONFIDENT | VERY_CONFIDENT
     */
    @Column(name = "solo_confidence")
    private String soloConfidence;
}
