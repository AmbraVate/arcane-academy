-- V25: Add schools — the top-level grouping layer above domains.
--
-- Blueprint schools (§4):
--   Engineering & Systems            (java, tailwind, react, sql, …)
--   Mathematical & Scientific Foundations (sciences, …)
--   Human Systems                    (psychology, philosophy, history, economics)
--   Creative & Cultural Systems      (music, …)
--   Heritage Systems                 (genealogy)

CREATE TABLE schools (
    id          VARCHAR(255) PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    sort_order  INTEGER NOT NULL DEFAULT 0
);

ALTER TABLE domains
    ADD COLUMN school_id VARCHAR(255)
    REFERENCES schools (id);

-- ─── Seed the five blueprint schools ─────────────────────────────────────────

INSERT INTO schools (id, name, description, sort_order) VALUES
    ('engineering-systems',
     'Engineering & Systems',
     'Software Engineering, Frontend Engineering, Data & Databases',
     1),
    ('mathematical-scientific',
     'Mathematical & Scientific Foundations',
     'Mathematics, Natural Sciences, Botany',
     2),
    ('human-systems',
     'Human Systems',
     'Psychology, Philosophy, History, Economics',
     3),
    ('creative-cultural',
     'Creative & Cultural Systems',
     'Music, Communication & Writing',
     4),
    ('heritage',
     'Heritage Systems',
     'Genealogy and family history',
     5);

-- ─── Assign existing domains to their schools ─────────────────────────────────

UPDATE domains SET school_id = 'engineering-systems'
    WHERE id IN ('java', 'tailwind', 'react', 'sql', 'html', 'css', 'javascript', 'typescript', 'python');

UPDATE domains SET school_id = 'mathematical-scientific'
    WHERE id IN ('sciences', 'mathematics', 'botany');

UPDATE domains SET school_id = 'human-systems'
    WHERE id IN ('psychology', 'philosophy', 'history', 'economics');

UPDATE domains SET school_id = 'creative-cultural'
    WHERE id IN ('music', 'communication');

UPDATE domains SET school_id = 'heritage'
    WHERE id IN ('genealogy');
