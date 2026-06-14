-- V50: Add tier_labels JSONB column to domains.
-- Stores domain-specific display names for the four tiers (APPRENTICE, JUNIOR, SENIOR, LEAD).
-- NULL means "use application defaults" (Apprentice / Junior / Senior / Lead).
-- JSON structure: {"APPRENTICE": "Intern", "JUNIOR": "Associate", "SENIOR": "Senior Engineer", "LEAD": "Tech Lead"}

ALTER TABLE domains ADD COLUMN IF NOT EXISTS tier_labels JSONB DEFAULT NULL;

-- Seed real-world practice titles for engineering domains
UPDATE domains SET tier_labels = '{"APPRENTICE":"Intern","JUNIOR":"Associate","SENIOR":"Senior Engineer","LEAD":"Tech Lead"}'::jsonb
WHERE id IN ('software-engineering', 'frontend-engineering', 'data-engineering');

-- Physics pathway uses academic progression titles
UPDATE domains SET tier_labels = '{"APPRENTICE":"Foundation","JUNIOR":"Experimental","SENIOR":"Advanced","LEAD":"Research"}'::jsonb
WHERE id = 'physics';
