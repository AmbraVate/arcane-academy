-- V20: Add Stripe subscription fields to users table.
-- subscription_status: FREE (default) | MONTHLY | ANNUAL | LIFETIME | CANCELLED
-- subscription_period_end: null for FREE/LIFETIME; set by Stripe webhook for recurring plans.
-- stripe_customer_id: Stripe's cus_... identifier; created on first checkout.
-- stripe_subscription_id: Stripe's sub_... identifier; null for one-time LIFETIME purchases.

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS subscription_status    VARCHAR(20)  NOT NULL DEFAULT 'FREE',
    ADD COLUMN IF NOT EXISTS subscription_period_end TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS stripe_customer_id     VARCHAR(255),
    ADD COLUMN IF NOT EXISTS stripe_subscription_id VARCHAR(255);
