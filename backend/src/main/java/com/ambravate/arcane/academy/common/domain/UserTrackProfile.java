package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/**
 * Tracks a user's diagnostic completion and progress metadata for a track
 * (e.g. "tailwind", "psychology"). Java uses {@link UserLearnerProfile} for historical reasons.
 */
@Entity
@Table(name = "user_track_profiles",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "track_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with", builderMethodName = "aUserTrackProfile")
public class UserTrackProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "track_id", nullable = false)
    private String trackId;

    @Builder.Default
    private boolean diagnosticCompleted = false;

    private Instant diagnosticCompletedAt;

    @Builder.Default
    private double diagnosticScore = 0.0;

    @Column(columnDefinition = "TEXT")
    private String diagnosticResultsJson;

    private Instant startedAt;

    @Enumerated(EnumType.STRING)
    private LearnerPath currentTier;
}
