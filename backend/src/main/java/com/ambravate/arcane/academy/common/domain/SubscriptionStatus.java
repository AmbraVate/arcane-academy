package com.ambravate.arcane.academy.common.domain;

/**
 * Represents a user's current subscription state.
 * <p>
 * FREE     — default; one topic allowed.
 * MONTHLY  — active recurring monthly plan.
 * ANNUAL   — active recurring annual plan.
 * LIFETIME — one-time purchase; never expires.
 * CANCELLED — subscription was cancelled; access valid until {@code subscriptionPeriodEnd}.
 */
public enum SubscriptionStatus {
    FREE,
    MONTHLY,
    ANNUAL,
    LIFETIME,
    CANCELLED
}
