-- V17: Add downloadables_json column to sub_chunks
-- Stores an array of { title, type, url } objects as JSON text.
-- Rendered as download chips at the start of the EXPLANATION phase.
ALTER TABLE sub_chunks ADD COLUMN downloadables_json TEXT;
