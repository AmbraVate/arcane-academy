-- Tracks the highest tier a user has fully completed for a non-Java topic.
-- NULL means the user is still in APPRENTICE (no tier has been fully unlocked yet).
ALTER TABLE user_topic_profiles
    ADD COLUMN IF NOT EXISTS current_tier VARCHAR(50);
