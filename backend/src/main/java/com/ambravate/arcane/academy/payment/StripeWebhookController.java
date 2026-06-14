package com.ambravate.arcane.academy.payment;

import com.stripe.exception.SignatureVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Receives Stripe webhook events. This endpoint is public (no JWT required)
 * because Stripe servers have no way to obtain a user JWT — the request
 * signature verified by the Stripe SDK is the sole security mechanism.
 *
 * <p>IMPORTANT: add {@code /api/webhooks/**} to the {@code permitAll()} list
 * in {@code SecurityConfig} and ensure HTTPS is enforced in production
 * ({@code https.required=true}).
 *
 * <p>The endpoint intentionally reads the request body as a raw {@code String}
 * so the Stripe SDK receives the exact bytes used to compute the HMAC signature.
 * Parsing the body as JSON before signature verification would break it.
 */
@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

    private final PaymentService paymentService;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String stripeSignature) {

        try {
            paymentService.processWebhook(payload, stripeSignature);
            return ResponseEntity.ok("Received");
        } catch (SignatureVerificationException e) {
            log.warn("[Webhook] Invalid Stripe signature — rejecting request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception e) {
            // Return 500 so Stripe retries the event
            log.error("[Webhook] Unexpected error processing webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Processing error");
        }
    }
}
