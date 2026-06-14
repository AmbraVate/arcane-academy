-- Phase 2: spaced-repetition retention tracking
-- question_attempts: one row per question per attempt (lesson or retrieval)
-- concept_retention:  one row per user×lesson, tracks stability and scheduling

CREATE TABLE question_attempts (
    id           VARCHAR(255) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    user_id      VARCHAR(255) NOT NULL,
    lesson_id    VARCHAR(255) NOT NULL,
    question_id  VARCHAR(255) NOT NULL,
    score        SMALLINT     NOT NULL CHECK (score IN (0, 1)),
    is_retrieval BOOLEAN      NOT NULL DEFAULT FALSE,
    interval_day SMALLINT,
    attempted_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_qa_user_lesson    ON question_attempts (user_id, lesson_id);
CREATE INDEX idx_qa_user_attempted ON question_attempts (user_id, attempted_at DESC);

CREATE TABLE concept_retention (
    id                  VARCHAR(255) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    user_id             VARCHAR(255) NOT NULL,
    lesson_id           VARCHAR(255) NOT NULL,
    retention_score     DECIMAL(4,3) NOT NULL DEFAULT 0.000,
    stability_state     VARCHAR(20)  NOT NULL DEFAULT 'UNVERIFIED',
    attempt_history     TEXT         NOT NULL DEFAULT '[]',
    consecutive_correct SMALLINT     NOT NULL DEFAULT 0,
    next_review_date    DATE,
    teach_back_score    DECIMAL(4,3),
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, lesson_id)
);

CREATE INDEX idx_cr_user_review ON concept_retention (user_id, next_review_date);
