-- Add structured lesson metadata fields to sub_chunks
ALTER TABLE sub_chunks ADD COLUMN learning_objectives_json  TEXT;
ALTER TABLE sub_chunks ADD COLUMN challenge_html            TEXT;
ALTER TABLE sub_chunks ADD COLUMN challenge_starter_code    TEXT;
ALTER TABLE sub_chunks ADD COLUMN challenge_tests_json      TEXT;
ALTER TABLE sub_chunks ADD COLUMN mini_project_html         TEXT;
ALTER TABLE sub_chunks ADD COLUMN common_mistakes_json      TEXT;
ALTER TABLE sub_chunks ADD COLUMN assessment_criteria_json  TEXT;
