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

    /**
     * Admin-controlled paywall bypass. When true the user can enrol in any number of
     * topics without a subscription. Admins always bypass the paywall implicitly.
     */
    @Builder.Default
    private boolean bypassPaywall = false;

    /**
     * Set to true when the user completes (or skips) the onboarding walkthrough.
     * Controls whether the onboarding modal is shown on the home page.
     */
    @Builder.Default
    private boolean onboardingCompleted = false;

    private String refreshToken;

    /**
     * Optional free-text location (e.g. "Manchester, UK"). Used on the public leaderboard
     * to give geographic context. Never required; null means the user has not set it.
     */
    @Column(length = 100)
    private String location;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Getter(AccessLevel.NONE)   // suppress Lombok getter; we provide a null-safe one below
    private UserRole role = UserRole.USER;

    /** Never returns null — legacy rows with a NULL role column are treated as USER. */
    public UserRole getRole() {
        return role != null ? role : UserRole.USER;
    }

    // ── Stripe / Subscription ─────────────────────────────────────────────────

    /**
     * Current subscription tier. Defaults to FREE (one topic allowed).
     * Updated by the Stripe webhook controller on payment events.
     */
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SubscriptionStatus subscriptionStatus = SubscriptionStatus.FREE;

    /**
     * When the current billing period ends (epoch).
     * Null for FREE and LIFETIME subscribers.
     * Used to maintain access after cancellation until the paid period expires.
     */
    private Instant subscriptionPeriodEnd;

    /**
     * Stripe's customer ID (cus_...). Created on the user's first checkout session.
     * Required to open the Stripe Customer Portal.
     */
    private String stripeCustomerId;

    /**
     * Stripe's subscription ID (sub_...). Null for one-time LIFETIME purchases.
     * Used to correlate subscription lifecycle webhook events back to this user.
     */
    private String stripeSubscriptionId;

    /**
     * Returns true when this user has an active paid subscription that grants
     * access to all topics. Takes cancellation grace periods into account.
     */
    public boolean hasActiveSubscription() {
        if (subscriptionStatus == null) return false;
        return switch (subscriptionStatus) {
            case MONTHLY, ANNUAL -> subscriptionPeriodEnd != null && Instant.now().isBefore(subscriptionPeriodEnd);
            // CANCELLED: keep access until the period end the user already paid for
            case CANCELLED -> subscriptionPeriodEnd != null && Instant.now().isBefore(subscriptionPeriodEnd);
            case LIFETIME -> true;
            case FREE -> false;
        };
    }

}
