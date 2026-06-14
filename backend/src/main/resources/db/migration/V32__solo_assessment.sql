-- Phase 4: Solo assessment types
-- Adds soloAssessmentType dispatch + rubric/keyword/quota columns.

-- ── Lesson solo assessment config ──────────────────────────────────────────

ALTER TABLE lessons
    ADD COLUMN solo_assessment_type VARCHAR(50),
    ADD COLUMN rubric_items_json    TEXT,
    ADD COLUMN keywords_json        TEXT,
    ADD COLUMN solo_model_answer_html TEXT;

-- ── Per-user confidence on solo practice ───────────────────────────────────

ALTER TABLE user_chunk_progress
    ADD COLUMN solo_confidence VARCHAR(20);

-- ── Monthly AI-review quota ────────────────────────────────────────────────
-- Tracked separately on learner profiles (Java) and track profiles (others).

ALTER TABLE user_learner_profiles
    ADD COLUMN ai_review_uses_this_month INT DEFAULT 0,
    ADD COLUMN ai_review_quota_month     VARCHAR(7);

ALTER TABLE user_track_profiles
    ADD COLUMN ai_review_uses_this_month INT DEFAULT 0,
    ADD COLUMN ai_review_quota_month     VARCHAR(7);
