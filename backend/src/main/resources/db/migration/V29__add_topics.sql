-- V29: Add topics table (Module → Topic → Lesson hierarchy)
-- A Topic is a concept-cluster that groups related Lessons within a Module.
-- Canonical hierarchy: School → Domain → Tier → Module → Topic → Lesson

CREATE TABLE topics (
    id           VARCHAR(255) PRIMARY KEY,
    module_id    VARCHAR(255) NOT NULL REFERENCES modules(id),
    title        VARCHAR(255) NOT NULL,
    purpose_html TEXT,
    learning_outcomes_json TEXT,
    sort_order   INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_topics_module_id ON topics (module_id);

-- Add topic_id FK to lessons (nullable so existing lessons are valid before backfill)
ALTER TABLE lessons
    ADD COLUMN topic_id VARCHAR(255) REFERENCES topics(id);

-- Backfill: create one default Topic per existing module (id = moduleId + '-default-topic')
INSERT INTO topics (id, module_id, title, sort_order)
SELECT id || '-default-topic', id, title, 0
FROM modules;

-- Wire all existing lessons to their module's default topic
UPDATE lessons
SET topic_id = module_id || '-default-topic'
WHERE topic_id IS NULL;
