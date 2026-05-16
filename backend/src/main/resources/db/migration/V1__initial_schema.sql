-- V1: Initial schema
-- Baseline migration for existing Hibernate-managed schema.
-- Flyway is disabled in dev (FLYWAY_ENABLED=false); this runs in prod/staging only.
-- New schema changes must be V2__, V3__, etc. — never edit this file after baseline.

-- ─── Content ─────────────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS topics (
    id           VARCHAR(255) PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    glyph        VARCHAR(255),
    tagline      TEXT,
    accent_color VARCHAR(255),
    sort_order   INTEGER      NOT NULL DEFAULT 0,
    active       BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS chunks (
    id               VARCHAR(255) PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    glyph            VARCHAR(255),
    sort_order       INTEGER      NOT NULL DEFAULT 0,
    prerequisite_ids TEXT,
    tier             VARCHAR(50)  NOT NULL DEFAULT 'FOUNDATION',
    topic_id         VARCHAR(255) NOT NULL DEFAULT 'java'
);

CREATE TABLE IF NOT EXISTS sub_chunks (
    id                              VARCHAR(255) PRIMARY KEY,
    chunk_id                        VARCHAR(255) NOT NULL,
    title                           VARCHAR(255) NOT NULL,
    sort_order                      INTEGER      NOT NULL DEFAULT 0,
    hook_html                       TEXT,
    explanation_html                TEXT,
    story_json                      TEXT,
    guided_practice_html            TEXT,
    guided_practice_starter_code    TEXT,
    guided_practice_tests_json      TEXT,
    filename                        VARCHAR(255),
    solo_practice_html              TEXT,
    feynman_prompt                  TEXT,
    xp_reward                       INTEGER      NOT NULL DEFAULT 50,
    practice_type                   VARCHAR(50)  NOT NULL DEFAULT 'JAVA',
    rabbit_hole_terms_json          TEXT
);

CREATE TABLE IF NOT EXISTS questions (
    id               VARCHAR(255) PRIMARY KEY,
    sub_chunk_id     VARCHAR(255) NOT NULL,
    tier             VARCHAR(50)  NOT NULL,
    type             VARCHAR(50)  NOT NULL,
    question_html    TEXT         NOT NULL,
    code_snippet     TEXT,
    options_json     TEXT,
    correct_answer   TEXT         NOT NULL,
    explanation_html TEXT,
    cross_chunk_ids  TEXT,
    difficulty_level INTEGER      NOT NULL DEFAULT 1,
    min_path         VARCHAR(50)  NOT NULL DEFAULT 'FOUNDATION'
);

CREATE INDEX IF NOT EXISTS idx_question_subchunk_tier ON questions (sub_chunk_id, tier);

CREATE TABLE IF NOT EXISTS rabbit_hole_modules (
    id           VARCHAR(255) PRIMARY KEY,
    chunk_id     VARCHAR(255) NOT NULL,
    title        VARCHAR(255) NOT NULL,
    content_html TEXT,
    story_json   TEXT,
    starter_code TEXT,
    test_cases_json TEXT,
    filename     VARCHAR(255),
    sort_order   INTEGER      NOT NULL DEFAULT 0
);

-- ─── Users & Auth ────────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS users (
    id                     VARCHAR(255) PRIMARY KEY,
    username               VARCHAR(255) NOT NULL UNIQUE,
    email                  VARCHAR(255) NOT NULL UNIQUE,
    password_hash          VARCHAR(255),
    auth_provider          VARCHAR(50)  NOT NULL DEFAULT 'LOCAL',
    provider_id            VARCHAR(255),
    total_xp               INTEGER      NOT NULL DEFAULT 0,
    streak_days            INTEGER      NOT NULL DEFAULT 0,
    rank                   VARCHAR(255) NOT NULL DEFAULT 'Novice',
    created_at             TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    last_login_at          TIMESTAMPTZ,
    learner_path           VARCHAR(50)  NOT NULL DEFAULT 'FOUNDATION',
    public_profile_enabled BOOLEAN      NOT NULL DEFAULT FALSE,
    blocked                BOOLEAN      NOT NULL DEFAULT FALSE,
    role                   VARCHAR(50)  NOT NULL DEFAULT 'USER'
);

-- ─── Learner Profiles ────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS user_learner_profiles (
    id                       VARCHAR(255) PRIMARY KEY,
    user_id                  VARCHAR(255) NOT NULL,
    current_path             VARCHAR(50)  NOT NULL DEFAULT 'FOUNDATION',
    diagnostic_completed     BOOLEAN      NOT NULL DEFAULT FALSE,
    diagnostic_completed_at  TIMESTAMPTZ,
    diagnostic_score         DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    diagnostic_results_json  TEXT,
    daily_goal_minutes       INTEGER      NOT NULL DEFAULT 40,
    last_session_at          TIMESTAMPTZ,
    CONSTRAINT uq_ulp_user_id UNIQUE (user_id)
);

CREATE TABLE IF NOT EXISTS user_topic_profiles (
    id                       VARCHAR(255) PRIMARY KEY,
    user_id                  VARCHAR(255) NOT NULL,
    topic_id                 VARCHAR(255) NOT NULL,
    diagnostic_completed     BOOLEAN      NOT NULL DEFAULT FALSE,
    diagnostic_completed_at  TIMESTAMPTZ,
    diagnostic_score         DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    diagnostic_results_json  TEXT,
    started_at               TIMESTAMPTZ,
    CONSTRAINT uq_utp_user_topic UNIQUE (user_id, topic_id)
);

-- ─── Progress & Spaced Repetition ────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS user_chunk_progress (
    id                        VARCHAR(255) PRIMARY KEY,
    user_id                   VARCHAR(255) NOT NULL,
    sub_chunk_id              VARCHAR(255) NOT NULL,
    current_phase             VARCHAR(50)  NOT NULL DEFAULT 'HOOK',
    status                    VARCHAR(50)  NOT NULL DEFAULT 'NOT_STARTED',
    memory_strength           DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    ease_factor               DOUBLE PRECISION NOT NULL DEFAULT 2.5,
    repetition_count          INTEGER      NOT NULL DEFAULT 0,
    interval_days             INTEGER      NOT NULL DEFAULT 0,
    last_reviewed_at          TIMESTAMPTZ,
    next_review_at            TIMESTAMPTZ,
    last_score                DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    guided_practice_passed    BOOLEAN      NOT NULL DEFAULT FALSE,
    solo_practice_passed      BOOLEAN      NOT NULL DEFAULT FALSE,
    retrieval_check_submitted BOOLEAN      NOT NULL DEFAULT FALSE,
    feynman_completed         BOOLEAN      NOT NULL DEFAULT FALSE,
    feynman_score             DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    completed_at              TIMESTAMPTZ,
    created_at                TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_ucp_user_subchunk UNIQUE (user_id, sub_chunk_id)
);

CREATE INDEX IF NOT EXISTS idx_ucp_user_status   ON user_chunk_progress (user_id, status);
CREATE INDEX IF NOT EXISTS idx_ucp_next_review   ON user_chunk_progress (next_review_at);
CREATE INDEX IF NOT EXISTS idx_ucp_user_subchunk ON user_chunk_progress (user_id, sub_chunk_id);

-- ─── Review Sessions ─────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS review_sessions (
    id                    VARCHAR(255) PRIMARY KEY,
    user_id               VARCHAR(255) NOT NULL,
    session_type          VARCHAR(50)  NOT NULL,
    started_at            TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    completed_at          TIMESTAMPTZ,
    total_questions       INTEGER      NOT NULL DEFAULT 0,
    correct_answers       INTEGER      NOT NULL DEFAULT 0,
    score                 DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    question_results_json TEXT
);

CREATE INDEX IF NOT EXISTS idx_review_user_type ON review_sessions (user_id, session_type);

-- ─── Gamification ────────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS user_badges (
    id        VARCHAR(255) PRIMARY KEY,
    user_id   VARCHAR(255) NOT NULL,
    badge_id  VARCHAR(255) NOT NULL,
    earned_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_ub_user_badge UNIQUE (user_id, badge_id)
);

-- ─── Rabbit Hole / Curiosity ──────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS user_rabbit_hole_terms (
    id           VARCHAR(255) PRIMARY KEY,
    user_id      VARCHAR(255) NOT NULL,
    term         VARCHAR(255) NOT NULL,
    description  TEXT,
    sub_chunk_id VARCHAR(255),
    topic_id     VARCHAR(255),
    saved_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_urht_user_term UNIQUE (user_id, term)
);

CREATE TABLE IF NOT EXISTS curiosity_queue (
    id           VARCHAR(255) PRIMARY KEY,
    user_id      VARCHAR(255) NOT NULL,
    sub_chunk_id VARCHAR(255) NOT NULL,
    saved_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_cq_user_subchunk UNIQUE (user_id, sub_chunk_id)
);
