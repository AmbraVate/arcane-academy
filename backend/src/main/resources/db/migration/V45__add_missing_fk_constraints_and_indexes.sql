-- V45: Add missing FK constraints and indexes
--
-- The lessons table was created without FK constraints on module_id and topic_id.
-- These constraints enforce referential integrity at the DB level (the admin UI
-- already guards against orphaned lessons in application code, but belt-and-braces
-- is safer).
--
-- Also adds indexes on high-cardinality FK columns used in common list queries.

-- ── FK constraints ────────────────────────────────────────────────────────────

ALTER TABLE lessons
    ADD CONSTRAINT fk_lessons_module
    FOREIGN KEY (module_id) REFERENCES modules(id);

ALTER TABLE lessons
    ADD CONSTRAINT fk_lessons_topic
    FOREIGN KEY (topic_id) REFERENCES topics(id);

-- ── Indexes ───────────────────────────────────────────────────────────────────

CREATE INDEX IF NOT EXISTS idx_lessons_module_id ON lessons (module_id);
CREATE INDEX IF NOT EXISTS idx_modules_track_id  ON modules (track_id);
