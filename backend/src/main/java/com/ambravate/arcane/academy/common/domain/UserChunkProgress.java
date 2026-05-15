package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "user_chunk_progress",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "sub_chunk_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserChunkProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "sub_chunk_id", nullable = false)
    private String subChunkId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EncodingPhase currentPhase = EncodingPhase.HOOK;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SubChunkStatus status = SubChunkStatus.NOT_STARTED;

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

    /** Set to true once guided practice has been submitted with a passing result. */
    @Builder.Default
    private boolean guidedPracticePassed = false;

    /** Set to true once solo practice has been submitted with a passing result. */
    @Builder.Default
    private boolean soloPracticePassed = false;

    @Builder.Default
    private boolean feynmanCompleted = false;

    @Builder.Default
    private double feynmanScore = 0.0;

    private Instant completedAt;

    @Builder.Default
    private Instant createdAt = Instant.now();
}
