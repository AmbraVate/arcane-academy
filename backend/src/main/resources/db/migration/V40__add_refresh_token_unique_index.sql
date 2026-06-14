-- Adds a unique index on refresh_token to:
--   1. Enforce single-use integrity (duplicate tokens would collide at DB level)
--   2. Make findByRefreshToken a fast index scan instead of a full-table scan
-- Partial index (WHERE refresh_token IS NOT NULL) avoids indexing the many NULL rows.
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_refresh_token
    ON users (refresh_token)
    WHERE refresh_token IS NOT NULL;
