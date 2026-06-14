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
    private LearnerPath currentPath = LearnerPath.APPRENTICE;

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

    // ── Phase 4 — AI review quota (Java track) ────────────────────────────────

    /** Number of AI-review uses in the current quota month. Resets each calendar month. */
    @Builder.Default
    @Column(name = "ai_review_uses_this_month")
    private int aiReviewUsesThisMonth = 0;

    /**
     * The month during which {@link #aiReviewUsesThisMonth} was last incremented,
     * in {@code YYYY-MM} format (e.g. {@code "2026-05"}).
     */
    @Column(name = "ai_review_quota_month")
    private String aiReviewQuotaMonth;
}
