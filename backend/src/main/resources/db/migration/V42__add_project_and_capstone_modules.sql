-- V42: Add mini-project modules (one per tier) and Capstone Quest module
--
-- These are seeded here because the MarkdownContentSeeder only creates a module
-- if the lesson ID is absent. Explicit migration ensures the module titles and
-- sort orders are correct on first deploy regardless of seeder run order.

-- ── Apprentice Mini Project ───────────────────────────────────────────────────

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id)
VALUES ('se-app-m7', 'Module 7: Apprentice Project', '🏗️', 7, 'APPRENTICE', 'software-engineering')
ON CONFLICT (id) DO NOTHING;

INSERT INTO topics (id, module_id, title, sort_order)
VALUES ('se-app-m7-mini_project', 'se-app-m7', 'Mini Project', 1)
ON CONFLICT (id) DO NOTHING;

-- ── Junior Mini Project ───────────────────────────────────────────────────────

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id)
VALUES ('se-jun-m9', 'Module 9: Junior Project', '🏗️', 9, 'JUNIOR', 'software-engineering')
ON CONFLICT (id) DO NOTHING;

INSERT INTO topics (id, module_id, title, sort_order)
VALUES ('se-jun-m9-mini_project', 'se-jun-m9', 'Mini Project', 1)
ON CONFLICT (id) DO NOTHING;

-- ── Senior Mini Project ───────────────────────────────────────────────────────

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id)
VALUES ('se-sen-m9', 'Module 9: Senior Project', '🏗️', 9, 'SENIOR', 'software-engineering')
ON CONFLICT (id) DO NOTHING;

INSERT INTO topics (id, module_id, title, sort_order)
VALUES ('se-sen-m9-mini_project', 'se-sen-m9', 'Mini Project', 1)
ON CONFLICT (id) DO NOTHING;

-- ── Lead Mini Project ─────────────────────────────────────────────────────────

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id)
VALUES ('se-lea-m6', 'Module 6: Lead Project', '🏗️', 6, 'LEAD', 'software-engineering')
ON CONFLICT (id) DO NOTHING;

INSERT INTO topics (id, module_id, title, sort_order)
VALUES ('se-lea-m6-mini_project', 'se-lea-m6', 'Mini Project', 1)
ON CONFLICT (id) DO NOTHING;

-- ── Capstone Quest ────────────────────────────────────────────────────────────
-- Sort order 99 ensures it always appears after all Lead-tier modules.

INSERT INTO modules (id, title, glyph, sort_order, tier, track_id)
VALUES ('se-lea-m7', 'Capstone Quest', '⚔️', 99, 'LEAD', 'software-engineering')
ON CONFLICT (id) DO NOTHING;

INSERT INTO topics (id, module_id, title, sort_order)
VALUES ('se-lea-m7-capstone', 'se-lea-m7', 'Capstone Quest', 1)
ON CONFLICT (id) DO NOTHING;
