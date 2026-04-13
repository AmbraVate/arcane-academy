package com.ambravate.polymath.academy.model;

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

    @Column(columnDefinition = "TEXT")
    private String diagnosticResultsJson;

    @Builder.Default
    private int dailyGoalMinutes = 40;

    private Instant lastSessionAt;
}
