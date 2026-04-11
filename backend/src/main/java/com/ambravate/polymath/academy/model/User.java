package com.ambravate.polymath.academy.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = true)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AuthProvider authProvider = AuthProvider.LOCAL;

    private String providerId;

    @Builder.Default
    private int totalXp = 0;

    @Builder.Default
    private int streakDays = 0;

    @Builder.Default
    private String rank = "Novice";

    @Builder.Default
    private Instant createdAt = Instant.now();

    private Instant lastLoginAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LearnerPath learnerPath = LearnerPath.FOUNDATION;

    public enum AuthProvider {
        LOCAL, GOOGLE
    }
}
