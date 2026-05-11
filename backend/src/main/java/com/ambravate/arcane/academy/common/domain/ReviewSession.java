package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "review_sessions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionType sessionType;

    @Builder.Default
    private Instant startedAt = Instant.now();

    private Instant completedAt;

    @Builder.Default
    private int totalQuestions = 0;

    @Builder.Default
    private int correctAnswers = 0;

    @Builder.Default
    private double score = 0.0;

    @Column(columnDefinition = "TEXT")
    private String questionResultsJson;
}
