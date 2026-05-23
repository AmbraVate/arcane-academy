CREATE TABLE user_notes (
    id            VARCHAR(36)  PRIMARY KEY,
    user_id       VARCHAR(36)  NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    sub_chunk_id  VARCHAR(100) NOT NULL,
    chunk_id      VARCHAR(100) NOT NULL,
    title         VARCHAR(500) NOT NULL,
    content       TEXT         NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_user_notes_user_id ON user_notes(user_id);
CREATE INDEX idx_user_notes_sub_chunk ON user_notes(user_id, sub_chunk_id);
