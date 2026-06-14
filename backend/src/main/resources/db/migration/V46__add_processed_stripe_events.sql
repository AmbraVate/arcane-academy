-- V46: Stripe webhook idempotency table
--
-- Stripe may deliver the same event more than once (their at-least-once guarantee).
-- Before processing any webhook event we insert its ID here. A duplicate ID
-- (UNIQUE constraint violation) means the event was already handled and is skipped.
-- Rows older than 30 days can be pruned by a scheduled job — they exist only to
-- guard the processing window, not for audit purposes.

CREATE TABLE processed_stripe_events (
    event_id   VARCHAR(255) PRIMARY KEY,
    processed_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_processed_stripe_events_processed_at
    ON processed_stripe_events (processed_at);
