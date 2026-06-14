-- Phase 5 — FSRS spaced-repetition columns on user_chunk_progress
--
-- Adds the five FSRS-4.5 parameters that replace SM-2's ease_factor /
-- repetition_count / interval_days.  Legacy SM-2 columns are kept in place
-- (existing rows keep their values; FSRS treats stability = 0 as a NEW card).
--
-- Fields:
--   fsrs_stability     — days until retrievability falls to the 90 % target
--   fsrs_difficulty    — card hardness [1.0 – 10.0]; higher = harder to learn
--   fsrs_state         — NEW | REVIEW | RELEARNING
--   fsrs_lapses        — cumulative count of "Again" (forgotten) reviews
--   fsrs_last_interval — most recently scheduled interval in days

ALTER TABLE user_chunk_progress
    ADD COLUMN IF NOT EXISTS fsrs_stability     DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    ADD COLUMN IF NOT EXISTS fsrs_difficulty    DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    ADD COLUMN IF NOT EXISTS fsrs_state         VARCHAR(20),
    ADD COLUMN IF NOT EXISTS fsrs_lapses        INT              NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS fsrs_last_interval INT              NOT NULL DEFAULT 0;
