package com.ambravate.arcane.academy.payment;

/**
 * The three billing options presented to users at the paywall.
 * <p>
 * MONTHLY and ANNUAL create Stripe Subscription-mode checkout sessions.
 * LIFETIME creates a one-time Payment-mode session.
 */
public enum PlanType {
    MONTHLY,
    ANNUAL,
    LIFETIME
}
