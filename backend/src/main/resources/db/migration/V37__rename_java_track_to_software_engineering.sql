-- V37: Redirect modules and profiles from track 'java' → 'software-engineering'.
--
-- V35 already created a 'software-engineering' track (hyphen) by backfilling
-- every domain that had no matching track row.  So the target row exists;
-- we only need to move child rows and drop the old 'java' track.

UPDATE modules             SET track_id = 'software-engineering' WHERE track_id = 'java';
UPDATE user_track_profiles SET track_id = 'software-engineering' WHERE track_id = 'java';

DELETE FROM tracks WHERE id = 'java';
