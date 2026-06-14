package com.ambravate.arcane.academy.retention.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;

@Entity
@Table(name = "question_attempts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuestionAttempt {

    @Id
    @UuidGenerator
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "lesson_id", nullable = false)
    private String lessonId;

    @Column(name = "question_id", nullable = false)
    private String questionId;

    @Column(nullable = false)
    private short score;

    @Column(name = "is_retrieval", nullable = false)
    private boolean retrieval;

    @Column(name = "interval_day")
    private Short intervalDay;

    @Column(name = "attempted_at", nullable = false, updatable = false)
    private Instant attemptedAt;

    @PrePersist
    void prePersist() {
        attemptedAt = Instant.now();
    }
}
