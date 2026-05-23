-- Rename tier values to the new universal four-tier structure.
-- Applies to all pathways (future-proof: APPRENTICE/JUNIOR/SENIOR/LEAD are pathway-agnostic).
UPDATE chunks SET tier = 'APPRENTICE' WHERE tier = 'FOUNDATION';
UPDATE chunks SET tier = 'JUNIOR'     WHERE tier = 'PRACTITIONER';
UPDATE chunks SET tier = 'SENIOR'     WHERE tier = 'EXPERT';

-- Remove ADVANCED and CAPSTONE content (clean-slate migration, no learner progress to preserve)
DELETE FROM sub_chunks WHERE chunk_id IN (SELECT id FROM chunks WHERE tier IN ('ADVANCED', 'CAPSTONE'));
DELETE FROM chunks WHERE tier IN ('ADVANCED', 'CAPSTONE');
