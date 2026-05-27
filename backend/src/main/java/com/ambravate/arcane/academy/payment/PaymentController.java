package com.ambravate.arcane.academy.payment;

import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.payment.dto.CheckoutRequest;
import com.ambravate.arcane.academy.payment.dto.CheckoutResponse;
import com.ambravate.arcane.academy.payment.dto.SubscriptionStatusDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Authenticated payment endpoints for the currently logged-in user.
 *
 * <p>All paths require a valid JWT — Spring Security's {@code anyRequest().authenticated()}
 * rule already covers them. The Stripe webhook has its own controller
 * ({@link StripeWebhookController}) at {@code /api/webhooks/stripe} and is
 * explicitly {@code permitAll()} in {@code SecurityConfig}.
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Creates a Stripe Checkout Session for the requested plan and returns
     * the URL the client should redirect to.
     */
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> createCheckout(
            @Valid @RequestBody CheckoutRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        log.info("[Payment] Checkout requested | userId={} plan={}", principal.getId(), request.getPlanType());
        CheckoutResponse response = paymentService.createCheckoutSession(principal.getId(), request.getPlanType());
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a Stripe Customer Portal session. The user is redirected to
     * Stripe to manage or cancel their subscription.
     * Requires an existing Stripe customer (i.e. the user has previously subscribed).
     */
    @PostMapping("/portal")
    public ResponseEntity<CheckoutResponse> createPortal(
            @AuthenticationPrincipal UserPrincipal principal) {

        log.info("[Payment] Portal session requested | userId={}", principal.getId());
        CheckoutResponse response = paymentService.createPortalSession(principal.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * Returns the caller's current subscription status, active flag, and
     * period-end date. Used by the frontend to render the subscription tab
     * on the profile page.
     */
    @GetMapping("/status")
    public ResponseEntity<SubscriptionStatusDto> getStatus(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(paymentService.getStatus(principal.getId()));
    }
}
