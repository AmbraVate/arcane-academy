-- V51: Add teach_back_expected_output to lessons.
-- When set on a JAVA-type lesson, the Teach Back step runs the learner's code
-- and compares stdout against this expected output (trimmed, case-sensitive).
-- NULL means fall back to pattern-match Feynman scoring.

ALTER TABLE lessons ADD COLUMN IF NOT EXISTS teach_back_expected_output TEXT DEFAULT NULL;
