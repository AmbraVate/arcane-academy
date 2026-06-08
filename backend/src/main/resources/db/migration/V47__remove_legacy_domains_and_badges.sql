-- V47: Remove legacy domain data and orphaned badge records.
--
-- The platform now supports only three active domains:
--   software-engineering, frontend-engineering, data-engineering
--
-- All other domains (java, tailwind, react, sql, psychology, genealogy,
-- sciences) were removed from DomainSeeder in the previous cleanup.
-- This migration removes any rows that may exist in the database from
-- earlier deployments, keeping the schema consistent with the current
-- application code.
--
-- Order matters: delete child rows before parent rows to satisfy FK constraints.

-- 1. Remove lessons for legacy domains (via their modules/tracks)
DELETE FROM guided_steps
WHERE lesson_id IN (
    SELECT l.id FROM lessons l
    JOIN learning_modules m ON l.module_id = m.id
    WHERE m.track_id NOT IN ('software-engineering', 'frontend-engineering', 'data-engineering')
);

DELETE FROM questions
WHERE lesson_id IN (
    SELECT l.id FROM lessons l
    JOIN learning_modules m ON l.module_id = m.id
    WHERE m.track_id NOT IN ('software-engineering', 'frontend-engineering', 'data-engineering')
);

DELETE FROM user_chunk_progress
WHERE lesson_id IN (
    SELECT l.id FROM lessons l
    JOIN learning_modules m ON l.module_id = m.id
    WHERE m.track_id NOT IN ('software-engineering', 'frontend-engineering', 'data-engineering')
);

DELETE FROM lessons
WHERE module_id IN (
    SELECT id FROM learning_modules
    WHERE track_id NOT IN ('software-engineering', 'frontend-engineering', 'data-engineering')
);

-- 2. Remove topic rows for legacy modules
DELETE FROM topics
WHERE module_id IN (
    SELECT id FROM learning_modules
    WHERE track_id NOT IN ('software-engineering', 'frontend-engineering', 'data-engineering')
);

-- 3. Remove legacy modules
DELETE FROM learning_modules
WHERE track_id NOT IN ('software-engineering', 'frontend-engineering', 'data-engineering');

-- 4. Remove legacy tracks
DELETE FROM tracks
WHERE id NOT IN ('software-engineering', 'frontend-engineering', 'data-engineering');

-- 5. Remove legacy domains
DELETE FROM domains
WHERE id NOT IN ('software-engineering', 'frontend-engineering', 'data-engineering');

-- 6. Remove user_track_profiles for legacy tracks (if table exists)
DELETE FROM user_track_profiles
WHERE track_id NOT IN ('software-engineering', 'frontend-engineering', 'data-engineering');

-- 7. Remove orphaned user_badges for badge definitions that no longer exist.
--    The current BadgeDefinition enum values are listed explicitly here;
--    any badge ID not in this set is a relic of a removed domain.
DELETE FROM user_badges
WHERE badge_id NOT IN (
    'ARCANE_INITIATE',
    'FIRST_CONCEPT',
    'SE_APPRENTICE_COMPLETE', 'SE_JUNIOR_COMPLETE', 'SE_SENIOR_COMPLETE', 'SE_LEAD_COMPLETE',
    'SE_APPRENTICE_CAPSTONE',  'SE_JUNIOR_CAPSTONE',  'SE_SENIOR_CAPSTONE',  'SE_LEAD_CAPSTONE',
    'FE_APPRENTICE_COMPLETE', 'FE_JUNIOR_COMPLETE', 'FE_SENIOR_COMPLETE', 'FE_LEAD_COMPLETE',
    'FE_APPRENTICE_CAPSTONE',  'FE_JUNIOR_CAPSTONE',  'FE_SENIOR_CAPSTONE',  'FE_LEAD_CAPSTONE',
    'DE_APPRENTICE_COMPLETE', 'DE_JUNIOR_COMPLETE', 'DE_SENIOR_COMPLETE', 'DE_LEAD_COMPLETE',
    'DE_APPRENTICE_CAPSTONE',  'DE_JUNIOR_CAPSTONE',  'DE_SENIOR_CAPSTONE',  'DE_LEAD_CAPSTONE',
    'APPRENTICE_COMPLETE',     'JUNIOR_COMPLETE',     'SENIOR_COMPLETE',     'LEAD_COMPLETE',
    'FIRST_NOTE', 'AVID_SCHOLAR',
    'PERFECT_REVIEW', 'MEMORY_MASTER', 'DIAGNOSTIC_ACE',
    'FEYNMAN_FIRST', 'FEYNMAN_MASTER',
    'RABBIT_HOLE_FIRST',
    'XP_100', 'XP_500', 'XP_1000', 'XP_2500', 'XP_5000',
    'STREAK_3', 'STREAK_7', 'STREAK_30'
);
