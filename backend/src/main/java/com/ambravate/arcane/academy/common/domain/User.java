package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder (
    setterPrefix = "with",
    builderMethodName = "aUser"
)
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

    /**
     * Privacy switch — when true the user appears on leaderboards and at /u/:username.
     * Default false: opt-in. Toggled from the profile page.
     */
    @Builder.Default
    private boolean publicProfileEnabled = false;

    /**
     * Admin-controlled block flag. When true the user's JWT is rejected at the filter
     * layer and they cannot access any authenticated endpoint.
     */
    @Builder.Default
    private boolean blocked = false;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Getter(AccessLevel.NONE)   // suppress Lombok getter; we provide a null-safe one below
    private UserRole role = UserRole.USER;

    /** Never returns null — legacy rows with a NULL role column are treated as USER. */
    public UserRole getRole() {
        return role != null ? role : UserRole.USER;
    }

}
