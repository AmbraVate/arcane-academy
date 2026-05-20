-- V12: Add optional exemplar answer for guided practice phase (NONE practice type sub-chunks).
ALTER TABLE sub_chunks ADD COLUMN IF NOT EXISTS guided_practice_model_answer TEXT;