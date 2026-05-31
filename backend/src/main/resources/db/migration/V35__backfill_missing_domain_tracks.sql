-- V35: Backfill missing tracks for non-engineering domains
--
-- V27 created tracks from existing domains at migration time, but domains
-- inserted by DomainSeeder *after* V27 already ran (e.g. genealogy) never got
-- a matching track, breaking the fk_modules_track constraint at seed time.
-- This migration re-applies school assignments for late-created domains and
-- creates a track for every domain that still lacks one.

-- Re-apply school assignments for any domain created after V25 ran
UPDATE domains SET school_id = 'heritage'
    WHERE id = 'genealogy' AND school_id IS NULL;
UPDATE domains SET school_id = 'mathematical-scientific'
    WHERE id IN ('sciences', 'mathematics', 'botany') AND school_id IS NULL;
UPDATE domains SET school_id = 'human-systems'
    WHERE id IN ('psychology', 'philosophy', 'history', 'economics') AND school_id IS NULL;
UPDATE domains SET school_id = 'creative-cultural'
    WHERE id IN ('music', 'communication') AND school_id IS NULL;

-- Create a track for every domain that doesn't have one yet
INSERT INTO tracks (id, domain_id, name, description, sort_order)
SELECT d.id, d.id, d.name, 'Core curriculum for ' || d.name, 1
FROM   domains d
WHERE  NOT EXISTS (SELECT 1 FROM tracks t WHERE t.id = d.id)
ON CONFLICT (id) DO NOTHING;
