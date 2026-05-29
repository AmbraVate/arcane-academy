package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "user_rabbit_hole_terms",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "term"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserRabbitHoleTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String term;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "lesson_id")
    private String lessonId;

    @Column(name = "domain_id")
    private String domainId;

    @Builder.Default
    @Column(name = "saved_at", nullable = false)
    private Instant savedAt = Instant.now();
}
