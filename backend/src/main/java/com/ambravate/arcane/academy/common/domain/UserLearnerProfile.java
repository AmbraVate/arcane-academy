package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "user_learner_profiles",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(
    setterPrefix = "with",
    builderMethodName = "aUserLearnerProfile"
)
public class UserLearnerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LearnerPath currentPath = LearnerPath.FOUNDATION;

    @Builder.Default
    private boolean diagnosticCompleted = false;

    private Instant diagnosticCompletedAt;

    @Builder.Default
    private double diagnosticScore = 0.0;

    @Column(columnDefinition = "TEXT")
    private String diagnosticResultsJson;

    @Builder.Default
    private int dailyGoalMinutes = 40;

    private Instant lastSessionAt;
}
