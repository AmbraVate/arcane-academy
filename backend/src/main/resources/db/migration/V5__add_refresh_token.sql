-- V5: Add refresh token support
-- Allows the frontend to refresh expired access JWTs without re-authentication.
ALTER TABLE users ADD COLUMN IF NOT EXISTS refresh_token VARCHAR(255);
