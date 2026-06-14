-- V24: Rename all tables and columns to match Arcane Academy blueprint terminology.
--
-- Concept mapping:
--   topics      → domains        (a learnable discipline, e.g. "Software Engineering")
--   chunks      → modules        (a thematic unit within a domain)
--   sub_chunks  → lessons        (a single lesson within a module)
--
-- FK columns follow their parent rename:
--   topic_id    → domain_id
--   chunk_id    → module_id
--   sub_chunk_id → lesson_id

-- ─── Core content tables ──────────────────────────────────────────────────────

ALTER TABLE topics     RENAME TO domains;
ALTER TABLE chunks     RENAME TO modules;
ALTER TABLE sub_chunks RENAME TO lessons;

-- ─── FK column renames ────────────────────────────────────────────────────────

ALTER TABLE modules RENAME COLUMN topic_id    TO domain_id;
ALTER TABLE lessons RENAME COLUMN chunk_id    TO module_id;

-- ─── Junction table ───────────────────────────────────────────────────────────

ALTER TABLE chunk_prerequisites RENAME TO module_prerequisites;
ALTER TABLE module_prerequisites RENAME COLUMN chunk_id TO module_id;
-- prerequisite_id is an FK to modules.id — no rename needed

-- ─── Progress and activity tables ────────────────────────────────────────────

ALTER TABLE questions                RENAME COLUMN sub_chunk_id TO lesson_id;
ALTER TABLE user_chunk_progress      RENAME COLUMN sub_chunk_id TO lesson_id;
ALTER TABLE user_rabbit_hole_terms   RENAME COLUMN topic_id     TO domain_id;
ALTER TABLE user_rabbit_hole_terms   RENAME COLUMN sub_chunk_id TO lesson_id;
ALTER TABLE user_topic_profiles      RENAME COLUMN topic_id     TO domain_id;
ALTER TABLE curiosity_queue          RENAME COLUMN sub_chunk_id TO lesson_id;
ALTER TABLE rabbit_hole_modules      RENAME COLUMN chunk_id     TO module_id;

-- ─── Indexes — drop and recreate to reflect new column names ─────────────────

DROP INDEX IF EXISTS idx_question_subchunk_tier;
DROP INDEX IF EXISTS idx_ucp_user_subchunk;

CREATE INDEX IF NOT EXISTS idx_question_lesson_tier ON questions (lesson_id, tier);
CREATE INDEX IF NOT EXISTS idx_ucp_user_lesson       ON user_chunk_progress (user_id, lesson_id);

-- Unchanged indexes are left as-is; Postgres renames the column in-place so
-- idx_ucp_user_status and idx_ucp_next_review still work without recreation.
