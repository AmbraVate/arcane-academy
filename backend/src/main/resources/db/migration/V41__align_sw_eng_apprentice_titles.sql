-- V41: Align Software Engineering Apprentice module and topic titles to curriculum document
--
-- Fixes three categories of mismatch introduced during initial seeding:
--   1. Ampersand vs "and" inconsistency (& → and)
--   2. Module 4 had two competing titles ("Data Structures Foundations" and "Collections and Data")
--      → unified to "Data Structures" as per the curriculum
--   3. Topic "Engineering Habits" gains its tier qualifier "Beginner"

-- ── Module titles ─────────────────────────────────────────────────────────────

UPDATE modules
SET title = 'Module 3: Functions and Reusability'
WHERE title = 'Module 3: Functions & Reusability';

UPDATE modules
SET title = 'Module 4: Data Structures'
WHERE title IN ('Module 4: Data Structures Foundations', 'Module 4: Collections and Data');

UPDATE modules
SET title = 'Module 6: Debugging and Engineering Habits'
WHERE title = 'Module 6: Debugging & Engineering Habits';

-- ── Topic titles ──────────────────────────────────────────────────────────────

UPDATE topics
SET title = 'Variables and State'
WHERE title = 'Variables & State';

UPDATE topics
SET title = 'Inputs and Outputs'
WHERE title = 'Inputs & Outputs';

UPDATE topics
SET title = 'Problem-Solving'
WHERE title = 'Problem Solving';

UPDATE topics
SET title = 'Beginner Engineering Habits'
WHERE title = 'Engineering Habits';
