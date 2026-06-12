-- V48: Add Physics pathway (School of Natural Sciences)
--
-- Seeds the full Apprentice → Junior → Senior → Lead structure for the
-- Physics domain: domain + track + 20 modules + topics for every tier.
-- Lesson content is seeded at runtime by MarkdownContentSeeder from
-- classpath:content/physics/**.
--
-- All INSERTs use ON CONFLICT (id) DO NOTHING so re-running is safe.

-- Ensure mathematical-scientific school exists (created by V25; guard for fresh DBs)
INSERT INTO schools (id, name, description, sort_order)
VALUES ('mathematical-scientific', 'Mathematical & Scientific Foundations',
        'Mathematics, Natural Sciences, Botany', 2)
ON CONFLICT (id) DO NOTHING;

-- Ensure domain exists (DomainSeeder also adds it at runtime)
INSERT INTO domains (id, school_id, name, sort_order)
VALUES ('physics', 'mathematical-scientific', 'Physics', 4)
ON CONFLICT (id) DO NOTHING;

-- Ensure track exists (one track per pathway, track id = domain id)
INSERT INTO tracks (id, domain_id, name, description, sort_order)
VALUES ('physics', 'physics', 'Physics',
        'From classical mechanics to quantum theory and relativity — the laws of the universe.', 1)
ON CONFLICT (id) DO NOTHING;

-- ═══════════════════════════════════════════════════════════════════════════
-- PHYSICS — Apprentice
-- ═══════════════════════════════════════════════════════════════════════════

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id) VALUES
  ('phy-app-m1', 'Module 1: Foundations of Physics',  '🔭', 1, 'APPRENTICE', 'physics'),
  ('phy-app-m2', 'Module 2: Mechanics Fundamentals',  '🍎', 2, 'APPRENTICE', 'physics'),
  ('phy-app-m3', 'Module 3: Waves and Light',         '🌊', 3, 'APPRENTICE', 'physics'),
  ('phy-app-m4', 'Module 4: Matter and Heat',         '🔥', 4, 'APPRENTICE', 'physics'),
  ('phy-app-m5', 'Module 5: Apprentice Project',      '🏗️', 5, 'APPRENTICE', 'physics')
ON CONFLICT (id) DO NOTHING;

INSERT INTO topics (id, module_id, title, sort_order) VALUES
  ('phy-app-m1-scientific_measurement',    'phy-app-m1', 'Scientific Measurement',    1),
  ('phy-app-m1-scientific_investigation',  'phy-app-m1', 'Scientific Investigation',  2),
  ('phy-app-m1-mathematical_tools',        'phy-app-m1', 'Mathematical Tools',        3),
  ('phy-app-m1-physical_quantities',       'phy-app-m1', 'Physical Quantities',       4),
  ('phy-app-m2-forces',                    'phy-app-m2', 'Forces',                    1),
  ('phy-app-m2-motion',                    'phy-app-m2', 'Motion',                    2),
  ('phy-app-m2-newtonian_mechanics',       'phy-app-m2', 'Newtonian Mechanics',       3),
  ('phy-app-m2-work_and_energy',           'phy-app-m2', 'Work and Energy',           4),
  ('phy-app-m3-wave_fundamentals',         'phy-app-m3', 'Wave Fundamentals',         1),
  ('phy-app-m3-sound',                     'phy-app-m3', 'Sound',                     2),
  ('phy-app-m3-light',                     'phy-app-m3', 'Light',                     3),
  ('phy-app-m3-electromagnetic_spectrum',  'phy-app-m3', 'Electromagnetic Spectrum',  4),
  ('phy-app-m4-states_of_matter',          'phy-app-m4', 'States of Matter',          1),
  ('phy-app-m4-thermal_energy',            'phy-app-m4', 'Thermal Energy',            2),
  ('phy-app-m4-particle_theory',           'phy-app-m4', 'Particle Theory',           3),
  ('phy-app-m4-everyday_physics',          'phy-app-m4', 'Everyday Physics',          4),
  ('phy-app-m5-mini_project',              'phy-app-m5', 'Mini Project',              1)
ON CONFLICT (id) DO NOTHING;

-- ═══════════════════════════════════════════════════════════════════════════
-- PHYSICS — Junior
-- ═══════════════════════════════════════════════════════════════════════════

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id) VALUES
  ('phy-jun-m1', 'Module 1: Advanced Mechanics',         '🎯', 1, 'JUNIOR', 'physics'),
  ('phy-jun-m2', 'Module 2: Electricity and Magnetism',  '⚡', 2, 'JUNIOR', 'physics'),
  ('phy-jun-m3', 'Module 3: Thermodynamics',             '♨️', 3, 'JUNIOR', 'physics'),
  ('phy-jun-m4', 'Module 4: Applied Physics',            '🔧', 4, 'JUNIOR', 'physics'),
  ('phy-jun-m5', 'Module 5: Junior Project',             '⚙️', 5, 'JUNIOR', 'physics')
ON CONFLICT (id) DO NOTHING;

INSERT INTO topics (id, module_id, title, sort_order) VALUES
  ('phy-jun-m1-momentum',                   'phy-jun-m1', 'Momentum',                   1),
  ('phy-jun-m1-circular_motion',            'phy-jun-m1', 'Circular Motion',            2),
  ('phy-jun-m1-gravitation',                'phy-jun-m1', 'Gravitation',                3),
  ('phy-jun-m1-mechanical_systems',         'phy-jun-m1', 'Mechanical Systems',         4),
  ('phy-jun-m2-electric_charge',            'phy-jun-m2', 'Electric Charge',            1),
  ('phy-jun-m2-circuits',                   'phy-jun-m2', 'Circuits',                   2),
  ('phy-jun-m2-magnetism',                  'phy-jun-m2', 'Magnetism',                  3),
  ('phy-jun-m2-electromagnetic_induction',  'phy-jun-m2', 'Electromagnetic Induction',  4),
  ('phy-jun-m3-gas_laws',                   'phy-jun-m3', 'Gas Laws',                   1),
  ('phy-jun-m3-energy_systems',             'phy-jun-m3', 'Energy Systems',             2),
  ('phy-jun-m3-entropy',                    'phy-jun-m3', 'Entropy',                    3),
  ('phy-jun-m3-thermal_applications',       'phy-jun-m3', 'Thermal Applications',       4),
  ('phy-jun-m4-materials',                  'phy-jun-m4', 'Materials',                  1),
  ('phy-jun-m4-fluid_physics',              'phy-jun-m4', 'Fluid Physics',              2),
  ('phy-jun-m4-engineering_physics',        'phy-jun-m4', 'Engineering Physics',        3),
  ('phy-jun-m4-measurement_systems',        'phy-jun-m4', 'Measurement Systems',        4),
  ('phy-jun-m5-mini_project',               'phy-jun-m5', 'Mini Project',               1)
ON CONFLICT (id) DO NOTHING;

-- ═══════════════════════════════════════════════════════════════════════════
-- PHYSICS — Senior
-- ═══════════════════════════════════════════════════════════════════════════

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id) VALUES
  ('phy-sen-m1', 'Module 1: Advanced Dynamics',       '🌀', 1, 'SENIOR', 'physics'),
  ('phy-sen-m2', 'Module 2: Electromagnetic Theory',  '🧲', 2, 'SENIOR', 'physics'),
  ('phy-sen-m3', 'Module 3: Modern Physics',          '⚛️', 3, 'SENIOR', 'physics'),
  ('phy-sen-m4', 'Module 4: Computational Physics',   '💻', 4, 'SENIOR', 'physics'),
  ('phy-sen-m5', 'Module 5: Senior Project',          '🔬', 5, 'SENIOR', 'physics')
ON CONFLICT (id) DO NOTHING;

INSERT INTO topics (id, module_id, title, sort_order) VALUES
  ('phy-sen-m1-rotational_mechanics',     'phy-sen-m1', 'Rotational Mechanics',     1),
  ('phy-sen-m1-oscillations',             'phy-sen-m1', 'Oscillations',             2),
  ('phy-sen-m1-wave_mechanics',           'phy-sen-m1', 'Wave Mechanics',           3),
  ('phy-sen-m1-complex_motion',           'phy-sen-m1', 'Complex Motion',           4),
  ('phy-sen-m2-electric_fields',          'phy-sen-m2', 'Electric Fields',          1),
  ('phy-sen-m2-magnetic_fields',          'phy-sen-m2', 'Magnetic Fields',          2),
  ('phy-sen-m2-electromagnetic_waves',    'phy-sen-m2', 'Electromagnetic Waves',    3),
  ('phy-sen-m2-maxwells_theory',          'phy-sen-m2', 'Maxwell''s Theory',        4),
  ('phy-sen-m3-relativity',               'phy-sen-m3', 'Relativity',               1),
  ('phy-sen-m3-quantum_physics',          'phy-sen-m3', 'Quantum Physics',          2),
  ('phy-sen-m3-atomic_physics',           'phy-sen-m3', 'Atomic Physics',           3),
  ('phy-sen-m3-nuclear_physics',          'phy-sen-m3', 'Nuclear Physics',          4),
  ('phy-sen-m4-modelling',                'phy-sen-m4', 'Modelling',                1),
  ('phy-sen-m4-numerical_methods',        'phy-sen-m4', 'Numerical Methods',        2),
  ('phy-sen-m4-data_analysis',            'phy-sen-m4', 'Data Analysis',            3),
  ('phy-sen-m4-scientific_computing',     'phy-sen-m4', 'Scientific Computing',     4),
  ('phy-sen-m5-mini_project',             'phy-sen-m5', 'Mini Project',             1)
ON CONFLICT (id) DO NOTHING;

-- ═══════════════════════════════════════════════════════════════════════════
-- PHYSICS — Lead
-- ═══════════════════════════════════════════════════════════════════════════

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id) VALUES
  ('phy-lea-m1', 'Module 1: Complex Systems Physics',  '🌪️', 1, 'LEAD', 'physics'),
  ('phy-lea-m2', 'Module 2: Research Physics',         '🧪', 2, 'LEAD', 'physics'),
  ('phy-lea-m3', 'Module 3: Physics Innovation',       '🚀', 3, 'LEAD', 'physics'),
  ('phy-lea-m4', 'Module 4: Scientific Leadership',    '👑', 4, 'LEAD', 'physics'),
  ('phy-lea-m5', 'Module 5: Capstone Project',         '🏆', 5, 'LEAD', 'physics')
ON CONFLICT (id) DO NOTHING;

INSERT INTO topics (id, module_id, title, sort_order) VALUES
  ('phy-lea-m1-chaos_theory',               'phy-lea-m1', 'Chaos Theory',               1),
  ('phy-lea-m1-emergence',                  'phy-lea-m1', 'Emergence',                  2),
  ('phy-lea-m1-systems_modelling',          'phy-lea-m1', 'Systems Modelling',          3),
  ('phy-lea-m1-interdisciplinary_physics',  'phy-lea-m1', 'Interdisciplinary Physics',  4),
  ('phy-lea-m2-research_design',            'phy-lea-m2', 'Research Design',            1),
  ('phy-lea-m2-scientific_communication',   'phy-lea-m2', 'Scientific Communication',   2),
  ('phy-lea-m2-experimental_methods',       'phy-lea-m2', 'Experimental Methods',       3),
  ('phy-lea-m2-research_ethics',            'phy-lea-m2', 'Research Ethics',            4),
  ('phy-lea-m3-energy_technologies',        'phy-lea-m3', 'Energy Technologies',        1),
  ('phy-lea-m3-materials_science',          'phy-lea-m3', 'Materials Science',          2),
  ('phy-lea-m3-quantum_technologies',       'phy-lea-m3', 'Quantum Technologies',       3),
  ('phy-lea-m3-frontier_physics',           'phy-lea-m3', 'Frontier Physics',           4),
  ('phy-lea-m4-leadership_skills',          'phy-lea-m4', 'Leadership Skills',          1),
  ('phy-lea-m4-project_management',         'phy-lea-m4', 'Project Management',         2),
  ('phy-lea-m4-policy_and_society',         'phy-lea-m4', 'Policy and Society',         3),
  ('phy-lea-m4-innovation_strategy',        'phy-lea-m4', 'Innovation Strategy',        4),
  ('phy-lea-m5-capstone',                   'phy-lea-m5', 'Capstone',                   1)
ON CONFLICT (id) DO NOTHING;
