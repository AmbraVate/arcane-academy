-- V43: Add Frontend Engineering and Data Engineering pathways
--
-- Ensures domains and tracks exist before inserting modules.
-- DomainSeeder adds `data-engineering` at runtime, so on a fresh install it may
-- not exist when this migration runs.  We insert it here (idempotently) so the
-- FK constraint modules.track_id → tracks.id is satisfied.
-- All INSERTs use ON CONFLICT (id) DO NOTHING so re-running is safe.

-- Ensure engineering-systems school exists (created by V25; guard for fresh DBs)
INSERT INTO schools (id, name, description, sort_order)
VALUES ('engineering-systems', 'Engineering & Systems',
        'Software Engineering, Frontend Engineering, Data & Databases', 1)
ON CONFLICT (id) DO NOTHING;

-- Ensure domains exist
INSERT INTO domains (id, school_id, name, sort_order)
VALUES ('frontend-engineering', 'engineering-systems', 'Frontend Engineering', 2)
ON CONFLICT (id) DO NOTHING;

INSERT INTO domains (id, school_id, name, sort_order)
VALUES ('data-engineering', 'engineering-systems', 'Data Engineering', 4)
ON CONFLICT (id) DO NOTHING;

-- Ensure tracks exist (one track per pathway, track id = domain id)
INSERT INTO tracks (id, domain_id, name, description, sort_order)
VALUES ('frontend-engineering', 'frontend-engineering', 'Frontend Engineering',
        'HTML, CSS, JavaScript, React — build polished, accessible, production-quality UIs.', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO tracks (id, domain_id, name, description, sort_order)
VALUES ('data-engineering', 'data-engineering', 'Data Engineering',
        'SQL, databases, pipelines, and data architecture — from a single table to enterprise scale.', 1)
ON CONFLICT (id) DO NOTHING;

-- ═══════════════════════════════════════════════════════════════════════════
-- FRONTEND ENGINEERING — Apprentice
-- ═══════════════════════════════════════════════════════════════════════════

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id) VALUES
  ('fe-app-m1', 'Module 1: Understanding the Web',         '🌐', 1, 'APPRENTICE', 'frontend-engineering'),
  ('fe-app-m2', 'Module 2: HTML Foundations',              '📄', 2, 'APPRENTICE', 'frontend-engineering'),
  ('fe-app-m3', 'Module 3: CSS Foundations',               '🎨', 3, 'APPRENTICE', 'frontend-engineering'),
  ('fe-app-m4', 'Module 4: Responsive Design',             '📱', 4, 'APPRENTICE', 'frontend-engineering'),
  ('fe-app-m5', 'Module 5: JavaScript Foundations',        '⚡', 5, 'APPRENTICE', 'frontend-engineering'),
  ('fe-app-m6', 'Module 6: Accessibility Foundations',     '♿', 6, 'APPRENTICE', 'frontend-engineering'),
  ('fe-app-m7', 'Module 7: Frontend Engineering Habits',   '🛠️', 7, 'APPRENTICE', 'frontend-engineering'),
  ('fe-app-m8', 'Module 8: Apprentice Project',            '🏗️', 8, 'APPRENTICE', 'frontend-engineering')
ON CONFLICT (id) DO NOTHING;

INSERT INTO topics (id, module_id, title, sort_order) VALUES
  ('fe-app-m1-the_internet',            'fe-app-m1', 'The Internet',                1),
  ('fe-app-m1-browsers',                'fe-app-m1', 'Browsers',                    2),
  ('fe-app-m2-html_basics',             'fe-app-m2', 'HTML Basics',                 1),
  ('fe-app-m2-content_elements',        'fe-app-m2', 'Content Elements',            2),
  ('fe-app-m2-semantic_html',           'fe-app-m2', 'Semantic HTML',               3),
  ('fe-app-m3-styling_basics',          'fe-app-m3', 'Styling Basics',              1),
  ('fe-app-m3-layout',                  'fe-app-m3', 'Layout',                      2),
  ('fe-app-m3-visual_design',           'fe-app-m3', 'Visual Design',               3),
  ('fe-app-m4-device_diversity',        'fe-app-m4', 'Device Diversity',            1),
  ('fe-app-m4-responsive_techniques',   'fe-app-m4', 'Responsive Techniques',       2),
  ('fe-app-m5-javascript_basics',       'fe-app-m5', 'JavaScript Basics',           1),
  ('fe-app-m5-decisions_and_loops',     'fe-app-m5', 'Decisions and Loops',         2),
  ('fe-app-m5-dom_manipulation',        'fe-app-m5', 'DOM Manipulation',            3),
  ('fe-app-m6-accessibility_principles','fe-app-m6', 'Accessibility Principles',    1),
  ('fe-app-m6-practical_accessibility', 'fe-app-m6', 'Practical Accessibility',     2),
  ('fe-app-m7-debugging',               'fe-app-m7', 'Debugging',                   1),
  ('fe-app-m7-code_quality',            'fe-app-m7', 'Code Quality',                2),
  ('fe-app-m8-mini_project',            'fe-app-m8', 'Mini Project',                1)
ON CONFLICT (id) DO NOTHING;

-- ── Frontend Engineering — Junior ─────────────────────────────────────────

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id) VALUES
  ('fe-jun-m1',  'Module 1: React Foundations',          '⚛️',  1, 'JUNIOR', 'frontend-engineering'),
  ('fe-jun-m2',  'Module 2: State Management',           '🔄',  2, 'JUNIOR', 'frontend-engineering'),
  ('fe-jun-m3',  'Module 3: Events and Forms',           '📝',  3, 'JUNIOR', 'frontend-engineering'),
  ('fe-jun-m4',  'Module 4: Routing and Navigation',     '🗺️',  4, 'JUNIOR', 'frontend-engineering'),
  ('fe-jun-m5',  'Module 5: API Integration',            '🔌',  5, 'JUNIOR', 'frontend-engineering'),
  ('fe-jun-m6',  'Module 6: Component Design',           '🧩',  6, 'JUNIOR', 'frontend-engineering'),
  ('fe-jun-m7',  'Module 7: Frontend Testing',           '🧪',  7, 'JUNIOR', 'frontend-engineering'),
  ('fe-jun-m8',  'Module 8: Tailwind CSS',               '💨',  8, 'JUNIOR', 'frontend-engineering'),
  ('fe-jun-m9',  'Module 9: Modern Frontend Tooling',    '🔧',  9, 'JUNIOR', 'frontend-engineering'),
  ('fe-jun-m10', 'Module 10: Junior Project',            '🏗️', 10, 'JUNIOR', 'frontend-engineering')
ON CONFLICT (id) DO NOTHING;

-- ── Frontend Engineering — Senior ─────────────────────────────────────────

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id) VALUES
  ('fe-sen-m1', 'Module 1: Frontend Architecture',        '🏗️', 1, 'SENIOR', 'frontend-engineering'),
  ('fe-sen-m2', 'Module 2: Advanced State Management',    '🔄', 2, 'SENIOR', 'frontend-engineering'),
  ('fe-sen-m3', 'Module 3: Performance Engineering',      '⚡', 3, 'SENIOR', 'frontend-engineering'),
  ('fe-sen-m4', 'Module 4: Security',                     '🔒', 4, 'SENIOR', 'frontend-engineering'),
  ('fe-sen-m5', 'Module 5: Design Systems',               '🎨', 5, 'SENIOR', 'frontend-engineering'),
  ('fe-sen-m6', 'Module 6: Advanced Accessibility',       '♿', 6, 'SENIOR', 'frontend-engineering'),
  ('fe-sen-m7', 'Module 7: Frontend Observability',       '🔭', 7, 'SENIOR', 'frontend-engineering'),
  ('fe-sen-m8', 'Module 8: Senior Project',               '🏗️', 8, 'SENIOR', 'frontend-engineering')
ON CONFLICT (id) DO NOTHING;

-- ── Frontend Engineering — Lead ───────────────────────────────────────────

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id) VALUES
  ('fe-lea-m1', 'Module 1: Frontend Leadership',              '👑',  1, 'LEAD', 'frontend-engineering'),
  ('fe-lea-m2', 'Module 2: Design System Governance',         '🎨',  2, 'LEAD', 'frontend-engineering'),
  ('fe-lea-m3', 'Module 3: UX Psychology',                    '🧠',  3, 'LEAD', 'frontend-engineering'),
  ('fe-lea-m4', 'Module 4: Product Thinking',                 '📊',  4, 'LEAD', 'frontend-engineering'),
  ('fe-lea-m5', 'Module 5: Strategic Frontend Architecture',  '🌐',  5, 'LEAD', 'frontend-engineering'),
  ('fe-lea-m6', 'Module 6: Lead Project',                     '🏗️',  6, 'LEAD', 'frontend-engineering'),
  ('fe-lea-m7', 'Capstone Quest',                             '⚔️', 99, 'LEAD', 'frontend-engineering')
ON CONFLICT (id) DO NOTHING;

-- ═══════════════════════════════════════════════════════════════════════════
-- DATA ENGINEERING — Apprentice
-- ═══════════════════════════════════════════════════════════════════════════

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id) VALUES
  ('de-app-m1', 'Module 1: Understanding Data',             '📊', 1, 'APPRENTICE', 'data-engineering'),
  ('de-app-m2', 'Module 2: Relational Database Foundations','🗄️', 2, 'APPRENTICE', 'data-engineering'),
  ('de-app-m3', 'Module 3: SQL Foundations',                '💾', 3, 'APPRENTICE', 'data-engineering'),
  ('de-app-m4', 'Module 4: Joining Information',            '🔗', 4, 'APPRENTICE', 'data-engineering'),
  ('de-app-m5', 'Module 5: Data Quality',                   '✅', 5, 'APPRENTICE', 'data-engineering'),
  ('de-app-m6', 'Module 6: Database Design Foundations',    '📐', 6, 'APPRENTICE', 'data-engineering'),
  ('de-app-m7', 'Module 7: Data Thinking',                  '🔍', 7, 'APPRENTICE', 'data-engineering'),
  ('de-app-m8', 'Module 8: Apprentice Project',             '🏗️', 8, 'APPRENTICE', 'data-engineering')
ON CONFLICT (id) DO NOTHING;

INSERT INTO topics (id, module_id, title, sort_order) VALUES
  ('de-app-m1-what_is_data',               'de-app-m1', 'What is Data?',                    1),
  ('de-app-m1-data_modelling_foundations', 'de-app-m1', 'Data Modelling Foundations',        2),
  ('de-app-m2-tables',                     'de-app-m2', 'Tables',                            1),
  ('de-app-m2-keys',                       'de-app-m2', 'Keys',                              2),
  ('de-app-m2-relationships',              'de-app-m2', 'Relationships',                     3),
  ('de-app-m3-reading_data',               'de-app-m3', 'Reading Data',                      1),
  ('de-app-m3-transforming_data',          'de-app-m3', 'Transforming Data',                 2),
  ('de-app-m3-aggregation',                'de-app-m3', 'Aggregation',                       3),
  ('de-app-m4-understanding_joins',        'de-app-m4', 'Understanding Joins',               1),
  ('de-app-m4-practical_data_retrieval',   'de-app-m4', 'Practical Data Retrieval',          2),
  ('de-app-m5-data_accuracy',              'de-app-m5', 'Data Accuracy',                     1),
  ('de-app-m5-data_integrity',             'de-app-m5', 'Data Integrity',                    2),
  ('de-app-m6-introduction_to_design',     'de-app-m6', 'Introduction to Design',            1),
  ('de-app-m6-design_practice',            'de-app-m6', 'Design Practice',                   2),
  ('de-app-m7-asking_better_questions',    'de-app-m7', 'Asking Better Questions',           1),
  ('de-app-m8-mini_project',               'de-app-m8', 'Mini Project',                      1)
ON CONFLICT (id) DO NOTHING;

-- ── Data Engineering — Junior ─────────────────────────────────────────────

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id) VALUES
  ('de-jun-m1', 'Module 1: Advanced SQL',                  '💾', 1, 'JUNIOR', 'data-engineering'),
  ('de-jun-m2', 'Module 2: Database Programming',          '🔧', 2, 'JUNIOR', 'data-engineering'),
  ('de-jun-m3', 'Module 3: Transactions',                  '🔄', 3, 'JUNIOR', 'data-engineering'),
  ('de-jun-m4', 'Module 4: Indexing and Performance',      '⚡', 4, 'JUNIOR', 'data-engineering'),
  ('de-jun-m5', 'Module 5: Application Data Access',       '🔌', 5, 'JUNIOR', 'data-engineering'),
  ('de-jun-m6', 'Module 6: Data Warehousing Foundations',  '🏛️', 6, 'JUNIOR', 'data-engineering'),
  ('de-jun-m7', 'Module 7: Data Security',                 '🔒', 7, 'JUNIOR', 'data-engineering'),
  ('de-jun-m8', 'Module 8: Database Testing',              '🧪', 8, 'JUNIOR', 'data-engineering'),
  ('de-jun-m9', 'Module 9: Junior Project',                '🏗️', 9, 'JUNIOR', 'data-engineering')
ON CONFLICT (id) DO NOTHING;

-- ── Data Engineering — Senior ─────────────────────────────────────────────

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id) VALUES
  ('de-sen-m1', 'Module 1: Database Architecture',       '🏗️', 1, 'SENIOR', 'data-engineering'),
  ('de-sen-m2', 'Module 2: Distributed Data Systems',    '🌐', 2, 'SENIOR', 'data-engineering'),
  ('de-sen-m3', 'Module 3: NoSQL Systems',               '🗃️', 3, 'SENIOR', 'data-engineering'),
  ('de-sen-m4', 'Module 4: Data Pipelines',              '🔄', 4, 'SENIOR', 'data-engineering'),
  ('de-sen-m5', 'Module 5: Analytics Engineering',       '📊', 5, 'SENIOR', 'data-engineering'),
  ('de-sen-m6', 'Module 6: Data Governance',             '🏛️', 6, 'SENIOR', 'data-engineering'),
  ('de-sen-m7', 'Module 7: Database Reliability',        '🛡️', 7, 'SENIOR', 'data-engineering'),
  ('de-sen-m8', 'Module 8: Senior Project',              '🏗️', 8, 'SENIOR', 'data-engineering')
ON CONFLICT (id) DO NOTHING;

-- ── Data Engineering — Lead ───────────────────────────────────────────────

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id) VALUES
  ('de-lea-m1', 'Module 1: Enterprise Data Strategy',        '🎯',  1, 'LEAD', 'data-engineering'),
  ('de-lea-m2', 'Module 2: Data Architecture Leadership',    '🏗️',  2, 'LEAD', 'data-engineering'),
  ('de-lea-m3', 'Module 3: Data Governance and Ethics',      '⚖️',  3, 'LEAD', 'data-engineering'),
  ('de-lea-m4', 'Module 4: Data-Driven Organisations',       '📈',  4, 'LEAD', 'data-engineering'),
  ('de-lea-m5', 'Module 5: Emerging Data Technologies',      '🚀',  5, 'LEAD', 'data-engineering'),
  ('de-lea-m6', 'Module 6: Lead Project',                    '🏗️',  6, 'LEAD', 'data-engineering'),
  ('de-lea-m7', 'Capstone Quest',                            '⚔️', 99, 'LEAD', 'data-engineering')
ON CONFLICT (id) DO NOTHING;
