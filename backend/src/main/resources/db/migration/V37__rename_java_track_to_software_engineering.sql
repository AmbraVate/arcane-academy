-- V37: Rename track 'java' → 'software-engineering'.
--
-- The 'java' track represents the Software Engineering pathway, not just the Java language.
-- Its frontend name is already "Software Engineering"; this aligns the DB ID with that name
-- and makes the URL /pathway/software-engineering consistent with the domain hierarchy.
--
-- Strategy: insert new row first (avoids FK violation), migrate children, delete old row.

-- Step 1 — add new track row with the corrected ID
INSERT INTO tracks (id, domain_id, name, description, sort_order)
SELECT 'software-engineering', domain_id, name, description, sort_order
FROM tracks
WHERE id = 'java';

-- Step 2 — migrate child rows
UPDATE modules            SET track_id = 'software-engineering' WHERE track_id = 'java';
UPDATE user_track_profiles SET track_id = 'software-engineering' WHERE track_id = 'java';

-- Step 3 — remove the old track
DELETE FROM tracks WHERE id = 'java';
