/**
 * Payment module — Stripe checkout, subscription lifecycle, and customer portal.
 *
 * <p>Inbound API:
 * <ul>
 *   <li>POST /api/payments/checkout   — create a Stripe Checkout Session (authenticated)</li>
 *   <li>POST /api/payments/portal     — open the Stripe Customer Portal (authenticated)</li>
 *   <li>GET  /api/payments/status     — current subscription status for the caller (authenticated)</li>
 *   <li>POST /api/webhooks/stripe     — Stripe webhook (unauthenticated; signature-verified)</li>
 * </ul>
 *
 * <p>Outbound dependency: Stripe Java SDK ({@link com.stripe}).
 * <p>Internal dependency: {@link com.ambravate.arcane.academy.auth.repository.UserRepository}
 * (accessed via the shared {@code common} module).
 */
package com.ambravate.arcane.academy.payment;
