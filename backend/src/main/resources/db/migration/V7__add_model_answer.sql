-- Adds an optional model / exemplar answer to sub_chunks.
-- Revealed to the learner after solo practice is complete (written-response sub-chunks only).
ALTER TABLE sub_chunks ADD COLUMN IF NOT EXISTS model_answer TEXT;
