package com.ambravate.arcane.academy.payment;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Initialises the Stripe SDK with the configured secret key at application startup.
 * The key is set as a static field on {@link Stripe} — the SDK's standard pattern.
 */
@Configuration
@EnableConfigurationProperties(StripeProperties.class)
@RequiredArgsConstructor
@Slf4j
public class StripeConfig {

    private final StripeProperties stripeProperties;

    @PostConstruct
    public void init() {
        if (stripeProperties.getSecretKey() == null
                || stripeProperties.getSecretKey().startsWith("sk_test_replace")) {
            log.warn("[Stripe] Secret key is not configured — payment features will fail. "
                    + "Set STRIPE_SECRET_KEY in your environment.");
            return;
        }
        Stripe.apiKey = stripeProperties.getSecretKey();
        log.info("[Stripe] SDK initialised.");
    }
}
