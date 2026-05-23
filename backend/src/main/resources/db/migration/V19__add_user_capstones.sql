CREATE TABLE user_capstones (
    id             VARCHAR(36)  PRIMARY KEY,
    user_id        VARCHAR(36)  NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    chunk_id       VARCHAR(100) NOT NULL,
    title          VARCHAR(255) NOT NULL,
    description    TEXT,
    code_content   TEXT,
    github_url     VARCHAR(500),
    admin_feedback TEXT,
    reviewed_at    TIMESTAMP,
    created_at     TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_user_capstones_user_id ON user_capstones(user_id);
CREATE INDEX idx_user_capstones_chunk   ON user_capstones(chunk_id);
