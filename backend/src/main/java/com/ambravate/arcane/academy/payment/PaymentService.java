package com.ambravate.arcane.academy.payment;

import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.common.domain.SubscriptionStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.exception.UserNotFoundException;
import com.ambravate.arcane.academy.payment.dto.CheckoutResponse;
import com.ambravate.arcane.academy.payment.dto.SubscriptionStatusDto;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final UserRepository userRepository;
    private final StripeProperties stripeProperties;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    // ── Checkout ─────────────────────────────────────────────────────────────

    /**
     * Creates a Stripe Checkout Session for the given plan and returns the
     * redirect URL. MONTHLY and ANNUAL use subscription mode; LIFETIME uses
     * one-time payment mode.
     */
    public CheckoutResponse createCheckoutSession(String userId, PlanType planType) {
        User user = requireUser(userId);

        String priceId = switch (planType) {
            case MONTHLY  -> stripeProperties.getPriceIdMonthly();
            case ANNUAL   -> stripeProperties.getPriceIdAnnual();
            case LIFETIME -> stripeProperties.getPriceIdLifetime();
        };

        SessionCreateParams.Mode mode = planType == PlanType.LIFETIME
                ? SessionCreateParams.Mode.PAYMENT
                : SessionCreateParams.Mode.SUBSCRIPTION;

        try {
            SessionCreateParams.Builder builder = SessionCreateParams.builder()
                    .setMode(mode)
                    .setCustomerEmail(user.getEmail())
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setPrice(priceId)
                            .setQuantity(1L)
                            .build())
                    .setSuccessUrl(frontendUrl + "/?payment=success")
                    .setCancelUrl(frontendUrl + "/?payment=cancelled")
                    .putMetadata("userId", userId)
                    .putMetadata("planType", planType.name());

            // Re-use existing Stripe customer if we already have one
            if (user.getStripeCustomerId() != null) {
                builder.setCustomer(user.getStripeCustomerId());
                builder.setCustomerEmail(null); // mutually exclusive with customer
            }

            Session session = Session.create(builder.build());
            log.info("[Payment] Checkout session created | userId={} plan={} sessionId={}",
                    userId, planType, session.getId());
            return new CheckoutResponse(session.getUrl());

        } catch (StripeException e) {
            log.error("[Payment] Failed to create checkout session | userId={} plan={} error={}",
                    userId, planType, e.getMessage());
            throw new RuntimeException("Could not create checkout session. Please try again.", e);
        }
    }

    // ── Customer Portal ───────────────────────────────────────────────────────

    /**
     * Creates a Stripe Customer Portal session so the user can manage or
     * cancel their subscription. Requires an existing Stripe customer ID.
     */
    public CheckoutResponse createPortalSession(String userId) {
        User user = requireUser(userId);

        if (user.getStripeCustomerId() == null) {
            throw new IllegalStateException("No billing account found. Please subscribe first.");
        }

        try {
            com.stripe.param.billingportal.SessionCreateParams params =
                    com.stripe.param.billingportal.SessionCreateParams.builder()
                            .setCustomer(user.getStripeCustomerId())
                            .setReturnUrl(frontendUrl + "/profile?tab=subscription")
                            .build();

            com.stripe.model.billingportal.Session portalSession =
                    com.stripe.model.billingportal.Session.create(params);

            log.info("[Payment] Portal session created | userId={}", userId);
            return new CheckoutResponse(portalSession.getUrl());

        } catch (StripeException e) {
            log.error("[Payment] Failed to create portal session | userId={} error={}", userId, e.getMessage());
            throw new RuntimeException("Could not open billing portal. Please try again.", e);
        }
    }

    // ── Status ────────────────────────────────────────────────────────────────

    public SubscriptionStatusDto getStatus(String userId) {
        User user = requireUser(userId);
        return SubscriptionStatusDto.builder()
                .status(user.getSubscriptionStatus() != null
                        ? user.getSubscriptionStatus().name()
                        : "FREE")
                .active(user.hasActiveSubscription())
                .periodEnd(user.getSubscriptionPeriodEnd())
                .hasStripeCustomer(user.getStripeCustomerId() != null)
                .build();
    }

    // ── Webhook ───────────────────────────────────────────────────────────────

    /**
     * Verifies the Stripe webhook signature and processes the event.
     * Idempotent — safe to receive the same event multiple times.
     *
     * @throws SignatureVerificationException if the signature is invalid (return 400 to Stripe)
     */
    @Transactional
    public void processWebhook(String payload, String sigHeader) throws SignatureVerificationException {
        Event event = Webhook.constructEvent(payload, sigHeader, stripeProperties.getWebhookSecret());
        log.info("[Webhook] Received event | type={} id={}", event.getType(), event.getId());

        switch (event.getType()) {
            case "checkout.session.completed"      -> handleCheckoutCompleted(event);
            case "customer.subscription.updated"   -> handleSubscriptionUpdated(event);
            case "customer.subscription.deleted"   -> handleSubscriptionDeleted(event);
            case "invoice.payment_failed"          -> handlePaymentFailed(event);
            default -> log.debug("[Webhook] Unhandled event type: {}", event.getType());
        }
    }

    // ── Webhook event handlers ────────────────────────────────────────────────

    private void handleCheckoutCompleted(Event event) {
        EventDataObjectDeserializer des = event.getDataObjectDeserializer();
        if (des.getObject().isEmpty()) {
            log.warn("[Webhook] checkout.session.completed — could not deserialise session");
            return;
        }

        Session session = (Session) des.getObject().get();
        String userId   = session.getMetadata().get("userId");
        String planType = session.getMetadata().get("planType");

        if (userId == null || planType == null) {
            log.warn("[Webhook] checkout.session.completed — missing metadata | sessionId={}", session.getId());
            return;
        }

        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            log.warn("[Webhook] checkout.session.completed — user not found | userId={}", userId);
            return;
        }

        User user = optUser.get();

        // Persist the Stripe customer ID if this is the first payment
        if (user.getStripeCustomerId() == null && session.getCustomer() != null) {
            user.setStripeCustomerId(session.getCustomer());
        }

        if ("LIFETIME".equals(planType)) {
            user.setSubscriptionStatus(SubscriptionStatus.LIFETIME);
            user.setSubscriptionPeriodEnd(null);
            user.setStripeSubscriptionId(null);
            log.info("[Webhook] LIFETIME activated | userId={}", userId);
        } else {
            // For subscription plans, the sub_id lives on the session
            String subscriptionId = session.getSubscription();
            user.setStripeSubscriptionId(subscriptionId);

            SubscriptionStatus newStatus = "ANNUAL".equals(planType)
                    ? SubscriptionStatus.ANNUAL
                    : SubscriptionStatus.MONTHLY;
            user.setSubscriptionStatus(newStatus);

            // Retrieve the subscription to get the current period end
            if (subscriptionId != null) {
                try {
                    Subscription sub = Subscription.retrieve(subscriptionId);
                    user.setSubscriptionPeriodEnd(Instant.ofEpochSecond(sub.getCurrentPeriodEnd()));
                } catch (StripeException e) {
                    log.warn("[Webhook] Could not retrieve subscription period end | subId={}", subscriptionId);
                }
            }
            log.info("[Webhook] {} activated | userId={} subId={}", newStatus, userId, subscriptionId);
        }

        userRepository.save(user);
    }

    private void handleSubscriptionUpdated(Event event) {
        EventDataObjectDeserializer des = event.getDataObjectDeserializer();
        if (des.getObject().isEmpty()) return;

        Subscription sub = (Subscription) des.getObject().get();
        String stripeSubId = sub.getId();

        userRepository.findByStripeSubscriptionId(stripeSubId).ifPresentOrElse(user -> {
            Instant periodEnd = Instant.ofEpochSecond(sub.getCurrentPeriodEnd());
            user.setSubscriptionPeriodEnd(periodEnd);

            // Map Stripe's subscription status to our internal enum
            String stripeStatus = sub.getStatus();
            SubscriptionStatus newStatus = switch (stripeStatus) {
                case "active"   -> detectPlanType(sub);
                case "past_due" -> user.getSubscriptionStatus(); // keep current; Stripe will retry
                case "canceled" -> SubscriptionStatus.CANCELLED;
                default         -> user.getSubscriptionStatus();
            };
            user.setSubscriptionStatus(newStatus);
            userRepository.save(user);
            log.info("[Webhook] Subscription updated | subId={} stripeStatus={} newStatus={}",
                    stripeSubId, stripeStatus, newStatus);
        }, () -> log.warn("[Webhook] subscription.updated — no user found for subId={}", stripeSubId));
    }

    private void handleSubscriptionDeleted(Event event) {
        EventDataObjectDeserializer des = event.getDataObjectDeserializer();
        if (des.getObject().isEmpty()) return;

        Subscription sub = (Subscription) des.getObject().get();
        String stripeSubId = sub.getId();

        userRepository.findByStripeSubscriptionId(stripeSubId).ifPresentOrElse(user -> {
            user.setSubscriptionStatus(SubscriptionStatus.CANCELLED);
            // Preserve period end so user keeps access until the paid period expires
            if (sub.getCurrentPeriodEnd() != null) {
                user.setSubscriptionPeriodEnd(Instant.ofEpochSecond(sub.getCurrentPeriodEnd()));
            }
            userRepository.save(user);
            log.info("[Webhook] Subscription cancelled | userId={} accessUntil={}",
                    user.getId(), user.getSubscriptionPeriodEnd());
        }, () -> log.warn("[Webhook] subscription.deleted — no user found for subId={}", stripeSubId));
    }

    private void handlePaymentFailed(Event event) {
        // Stripe handles retries automatically. We log but do not immediately revoke access —
        // the subscription.updated event will fire if the subscription ultimately lapses.
        log.warn("[Webhook] invoice.payment_failed received — Stripe will retry automatically");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private User requireUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }

    /**
     * Infers MONTHLY vs ANNUAL from the subscription's interval metadata.
     * Falls back to MONTHLY if the interval cannot be determined.
     */
    private SubscriptionStatus detectPlanType(Subscription sub) {
        try {
            String interval = sub.getItems().getData().get(0)
                    .getPrice().getRecurring().getInterval();
            return "year".equals(interval) ? SubscriptionStatus.ANNUAL : SubscriptionStatus.MONTHLY;
        } catch (Exception e) {
            return SubscriptionStatus.MONTHLY;
        }
    }
}
