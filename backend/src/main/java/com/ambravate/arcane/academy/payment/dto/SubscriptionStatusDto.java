package com.ambravate.arcane.academy.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

/**
 * Subscription summary returned by GET /api/payments/status.
 */
@Getter
@AllArgsConstructor
@Builder
public class SubscriptionStatusDto {

    /** FREE | MONTHLY | ANNUAL | LIFETIME | CANCELLED */
    private final String status;

    /** True when the user currently has full access (active plan or within grace period). */
    private final boolean active;

    /**
     * When the current billing period ends. Null for FREE and LIFETIME.
     * After cancellation, access is maintained until this date.
     */
    private final Instant periodEnd;

    /** Whether the user has a Stripe customer ID (i.e. has ever paid). */
    private final boolean hasStripeCustomer;
}
