-- Optional location field on users for leaderboard geographic context.
-- Free-text (e.g. "Manchester, UK"). Null means not set.
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS location VARCHAR(100);
