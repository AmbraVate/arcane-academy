-- V21: Track whether a user has completed (or skipped) the onboarding walkthrough.
-- Defaults to false so existing users see the modal on their next login.
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS onboarding_completed BOOLEAN NOT NULL DEFAULT FALSE;
