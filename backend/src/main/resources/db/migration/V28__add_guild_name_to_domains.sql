-- V28: Add guild_name column to domains table
-- This column supports the Domain entity's guildName field for narrative overlay.

ALTER TABLE domains
    ADD COLUMN guild_name VARCHAR(255);
