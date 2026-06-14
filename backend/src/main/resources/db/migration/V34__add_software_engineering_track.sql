-- V34: Add software_engineering track
--
-- The markdown content files (Software Engineering Pathway, 205 lessons) reference
-- domainId: software_engineering, which MarkdownContentSeeder maps directly to
-- modules.track_id.  V27 created the domain `software-engineering` (hyphen) but
-- did not add a corresponding track.  This migration adds the missing track so
-- the FK constraint on modules.track_id → tracks.id is satisfied.

INSERT INTO tracks (id, domain_id, name, description, sort_order)
VALUES (
    'software_engineering',
    'software-engineering',
    'Software Engineering',
    'Core software engineering curriculum: from computation fundamentals through professional patterns.',
    0
)
ON CONFLICT (id) DO NOTHING;
