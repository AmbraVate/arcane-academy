-- Stores story terms that users have bookmarked for later research.
-- Upsert-safe: unique constraint on (user_id, term).
CREATE TABLE IF NOT EXISTS user_rabbit_hole_terms (
    id         VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    user_id    VARCHAR(255) NOT NULL,
    term       VARCHAR(255) NOT NULL,
    description TEXT,
    sub_chunk_id VARCHAR(255),
    topic_id   VARCHAR(255),
    saved_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_user_term UNIQUE (user_id, term)
);
