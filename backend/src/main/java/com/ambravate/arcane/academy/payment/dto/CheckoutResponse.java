package com.ambravate.arcane.academy.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Returned by both checkout and portal session creation endpoints.
 * The {@code url} is the Stripe-hosted page the client should redirect to.
 */
@Getter
@AllArgsConstructor
public class CheckoutResponse {
    private final String url;
}
