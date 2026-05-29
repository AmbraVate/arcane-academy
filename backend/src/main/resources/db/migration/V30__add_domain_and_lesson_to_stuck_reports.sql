-- V30: Add domain_id and lesson_id columns to stuck_reports table
-- These columns satisfy the StuckReport entity mappings.

ALTER TABLE stuck_reports
    ADD COLUMN domain_id VARCHAR(255);

ALTER TABLE stuck_reports
    ADD COLUMN lesson_id VARCHAR(255);
