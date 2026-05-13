package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/**
 * Tracks a user's diagnostic completion and progress metadata for a non-Java topic
 * (e.g. "tailwind", "html"). Java uses {@link UserLearnerProfile} for historical reasons.
 */
@Entity
@Table(name = "user_topic_profiles",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "topic_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with", builderMethodName = "aUserTopicProfile")
public class UserTopicProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "topic_id", nullable = false)
    private String topicId;

    @Builder.Default
    private boolean diagnosticCompleted = false;

    private Instant diagnosticCompletedAt;

    @Builder.Default
    private double diagnosticScore = 0.0;

    @Column(columnDefinition = "TEXT")
    private String diagnosticResultsJson;

    private Instant startedAt;
}
