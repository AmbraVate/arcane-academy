-- V36: Migrate all remaining legacy LearnerPath enum values to the current four-tier structure.
--
-- Mapping:
--   FOUNDATION   → APPRENTICE   (first tier, direct equivalent)
--   PRACTITIONER → JUNIOR       (second tier, direct equivalent)
--   EXPERT       → SENIOR       (third tier, direct equivalent)
--   ADVANCED     → JUNIOR       (intermediate tier — merged into JUNIOR)
--   CAPSTONE     → LEAD         (final tier — replaced by per-tier capstone topics in LEAD)
--
-- Applies to every table that stores a LearnerPath enum value as a VARCHAR.

-- ── users.learner_path ────────────────────────────────────────────────────────
UPDATE users SET learner_path = 'APPRENTICE' WHERE learner_path = 'FOUNDATION';
UPDATE users SET learner_path = 'JUNIOR'     WHERE learner_path = 'PRACTITIONER';
UPDATE users SET learner_path = 'SENIOR'     WHERE learner_path = 'EXPERT';
UPDATE users SET learner_path = 'JUNIOR'     WHERE learner_path = 'ADVANCED';
UPDATE users SET learner_path = 'LEAD'       WHERE learner_path = 'CAPSTONE';

ALTER TABLE users ALTER COLUMN learner_path SET DEFAULT 'APPRENTICE';

-- ── user_learner_profiles.current_path ────────────────────────────────────────
UPDATE user_learner_profiles SET current_path = 'APPRENTICE' WHERE current_path = 'FOUNDATION';
UPDATE user_learner_profiles SET current_path = 'JUNIOR'     WHERE current_path = 'PRACTITIONER';
UPDATE user_learner_profiles SET current_path = 'SENIOR'     WHERE current_path = 'EXPERT';
UPDATE user_learner_profiles SET current_path = 'JUNIOR'     WHERE current_path = 'ADVANCED';
UPDATE user_learner_profiles SET current_path = 'LEAD'       WHERE current_path = 'CAPSTONE';

ALTER TABLE user_learner_profiles ALTER COLUMN current_path SET DEFAULT 'APPRENTICE';

-- ── user_track_profiles.current_tier (renamed from user_topic_profiles in V27) ─
UPDATE user_track_profiles SET current_tier = 'APPRENTICE' WHERE current_tier = 'FOUNDATION';
UPDATE user_track_profiles SET current_tier = 'JUNIOR'     WHERE current_tier = 'PRACTITIONER';
UPDATE user_track_profiles SET current_tier = 'SENIOR'     WHERE current_tier = 'EXPERT';
UPDATE user_track_profiles SET current_tier = 'JUNIOR'     WHERE current_tier = 'ADVANCED';
UPDATE user_track_profiles SET current_tier = 'LEAD'       WHERE current_tier = 'CAPSTONE';

-- ── questions.min_path ────────────────────────────────────────────────────────
UPDATE questions SET min_path = 'APPRENTICE' WHERE min_path = 'FOUNDATION';
UPDATE questions SET min_path = 'JUNIOR'     WHERE min_path = 'PRACTITIONER';
UPDATE questions SET min_path = 'SENIOR'     WHERE min_path = 'EXPERT';
UPDATE questions SET min_path = 'JUNIOR'     WHERE min_path = 'ADVANCED';
UPDATE questions SET min_path = 'LEAD'       WHERE min_path = 'CAPSTONE';

ALTER TABLE questions ALTER COLUMN min_path SET DEFAULT 'APPRENTICE';
