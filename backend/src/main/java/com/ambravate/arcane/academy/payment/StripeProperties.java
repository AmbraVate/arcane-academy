package com.ambravate.arcane.academy.payment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds the {@code stripe.*} block in application.yml.
 *
 * <p>All values must be supplied via environment variables in production:
 * STRIPE_SECRET_KEY, STRIPE_WEBHOOK_SECRET, STRIPE_PRICE_ID_MONTHLY,
 * STRIPE_PRICE_ID_ANNUAL, STRIPE_PRICE_ID_LIFETIME.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "stripe")
public class StripeProperties {

    /** Stripe secret key (sk_test_... / sk_live_...). Never log or expose this. */
    private String secretKey;

    /**
     * Stripe webhook signing secret (whsec_...).
     * Found under Developers → Webhooks → your endpoint in the Stripe dashboard.
     */
    private String webhookSecret;

    /** Stripe Price ID for the monthly recurring plan. */
    private String priceIdMonthly;

    /** Stripe Price ID for the annual recurring plan. */
    private String priceIdAnnual;

    /** Stripe Price ID for the lifetime one-time payment. */
    private String priceIdLifetime;
}
