-- V44: Fix Data Engineering Lead module IDs
--
-- V43 inserted modules as de-lea-m* but all content files use de-lead-m* in their
-- moduleId frontmatter.  At runtime the MarkdownContentSeeder was creating a second
-- set of modules (de-lead-m*) with the actual lessons, leaving the de-lea-m* rows
-- as empty orphans.  This migration removes the orphans and inserts the correct IDs
-- so the seeder finds them on the next startup rather than re-creating them.
--
-- The de-lea-m* rows have no lessons or topics attached (seeder never wrote to them),
-- so the DELETEs are safe.

DELETE FROM modules WHERE id IN (
    'de-lea-m1', 'de-lea-m2', 'de-lea-m3', 'de-lea-m4',
    'de-lea-m5', 'de-lea-m6', 'de-lea-m7'
);

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id) VALUES
    ('de-lead-m1', 'Module 1: Enterprise Data Strategy',     '🎯',  1, 'LEAD', 'data-engineering'),
    ('de-lead-m2', 'Module 2: Data Architecture Leadership', '🏗️',  2, 'LEAD', 'data-engineering'),
    ('de-lead-m3', 'Module 3: Data Governance and Ethics',   '⚖️',  3, 'LEAD', 'data-engineering'),
    ('de-lead-m4', 'Module 4: Data-Driven Organisations',    '📈',  4, 'LEAD', 'data-engineering'),
    ('de-lead-m5', 'Module 5: Emerging Data Technologies',   '🚀',  5, 'LEAD', 'data-engineering'),
    ('de-lead-m6', 'Module 6: Lead Project',                 '🏗️',  6, 'LEAD', 'data-engineering'),
    ('de-lead-m7', 'Capstone Quest',                         '⚔️', 99, 'LEAD', 'data-engineering')
ON CONFLICT (id) DO NOTHING;
