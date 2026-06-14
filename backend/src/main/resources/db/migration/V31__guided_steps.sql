-- Phase 3: Guided-step engine
-- Stores the ordered workbook steps for a lesson's Guided Practice Quest.
-- Each step carries its own deterministic marking rule (no AI required).

CREATE TABLE guided_steps (
    id                     VARCHAR(255) PRIMARY KEY,
    lesson_id              VARCHAR(255) NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
    sort_order             INT          NOT NULL DEFAULT 0,
    instruction_html       TEXT,
    input_type             VARCHAR(50)  NOT NULL DEFAULT 'SHORT_TEXT',
    input_config_json      TEXT,
    marking_rule_json      TEXT,
    hint_html              TEXT,
    reflection_prompt_html TEXT
);

CREATE INDEX idx_guided_steps_lesson_id ON guided_steps (lesson_id);

-- Track per-user step completion so sessions resume mid-quest.
ALTER TABLE user_chunk_progress
    ADD COLUMN guided_steps_completed_json TEXT;
