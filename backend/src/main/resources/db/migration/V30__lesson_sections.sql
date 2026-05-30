-- Phase 2: Add per-section content fields to lessons
-- These columns store the individual lesson content sections introduced in the
-- Markdown-first content template.  All are nullable — existing JSON-seeded
-- lessons keep null here; only Markdown-authored lessons populate them.

ALTER TABLE lessons ADD COLUMN lore_intro_html         TEXT;
ALTER TABLE lessons ADD COLUMN why_it_matters_html     TEXT;
ALTER TABLE lessons ADD COLUMN worked_examples_html    TEXT;
ALTER TABLE lessons ADD COLUMN mental_model_html       TEXT;
ALTER TABLE lessons ADD COLUMN mini_summary_html       TEXT;
ALTER TABLE lessons ADD COLUMN lore_conclusion_html    TEXT;
ALTER TABLE lessons ADD COLUMN integration_domains_json TEXT;
