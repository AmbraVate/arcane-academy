-- V27: Add tracks layer to structural hierarchy
--
-- Hierarchy: School -> Domain -> Track -> Tier -> Module -> Lesson
-- 
-- 1. Create tracks table.
-- 2. Add broad engineering domains.
-- 3. Convert tool-specific domains (java, react) into tracks.
-- 4. Re-link modules and user_profiles to tracks.

CREATE TABLE tracks (
    id VARCHAR(255) PRIMARY KEY,
    domain_id VARCHAR(255) NOT NULL REFERENCES domains(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    sort_order INTEGER NOT NULL DEFAULT 0
);

-- Add broad blueprint domains for Engineering & Systems
INSERT INTO domains (id, school_id, name, description, sort_order) VALUES
('software-engineering', 'engineering-systems', 'Software Engineering', 'Architecture, logic, and backend systems.', 1),
('frontend-engineering', 'engineering-systems', 'Frontend Engineering', 'User interfaces and client-side logic.', 2),
('data-databases', 'engineering-systems', 'Data & Databases', 'Data modeling, pipelines, and queries.', 3);

-- Convert existing tool domains into tracks
INSERT INTO tracks (id, domain_id, name, description, sort_order) VALUES
('java', 'software-engineering', 'Java', 'Object-oriented backend development.', 1),
('python', 'software-engineering', 'Python', 'Versatile scripting language.', 2),
('tailwind', 'frontend-engineering', 'Tailwind CSS', 'Utility-first CSS framework.', 1),
('react', 'frontend-engineering', 'React', 'Component-based UI library.', 2),
('html', 'frontend-engineering', 'HTML', 'Structural markup.', 3),
('css', 'frontend-engineering', 'CSS', 'Styling logic.', 4),
('javascript', 'frontend-engineering', 'JavaScript', 'Web behavior.', 5),
('typescript', 'frontend-engineering', 'TypeScript', 'Typed JavaScript.', 6),
('sql', 'data-databases', 'SQL', 'Relational database querying.', 1);

-- Create default tracks for non-engineering domains (psychology -> psychology)
-- We will use the exact same ID for the track as the old domain ID, so modules don't need to change their IDs.
INSERT INTO tracks (id, domain_id, name, description, sort_order)
SELECT id, id, name, 'Core curriculum for ' || name, 1
FROM domains
WHERE school_id != 'engineering-systems';

-- Update modules to reference track_id
ALTER TABLE modules RENAME COLUMN domain_id TO track_id;

-- Add foreign key constraint to modules
ALTER TABLE modules ADD CONSTRAINT fk_modules_track
    FOREIGN KEY (track_id) REFERENCES tracks(id);

-- Wait, the `modules.track_id` currently holds 'java', 'tailwind', etc. which perfectly match `tracks.id`!
-- For non-engineering domains, the `modules.track_id` holds 'psychology', which matches the newly created track 'psychology'.
-- This means the data mapping is already perfectly aligned just by renaming the column!

-- Rename user_topic_profiles to user_track_profiles
ALTER TABLE user_topic_profiles RENAME TO user_track_profiles;
ALTER TABLE user_track_profiles RENAME COLUMN domain_id TO track_id;
ALTER TABLE user_track_profiles ADD CONSTRAINT fk_utp_track
    FOREIGN KEY (track_id) REFERENCES tracks(id);

-- We need to drop the old foreign keys if they existed, but Postgres auto-updates constraint columns on RENAME.
-- We do need to clean up the `domains` table to remove the old tool domains (java, tailwind, etc.)
-- However, `tracks.id` doesn't reference `domains.id` except for its `domain_id` column.
-- `tracks.id` being 'java' is fine.
-- Let's delete the old tool domains from the domains table.
DELETE FROM domains WHERE id IN (
    'java', 'python', 'tailwind', 'react', 'html', 'css', 'javascript', 'typescript', 'sql'
);

-- Done!
