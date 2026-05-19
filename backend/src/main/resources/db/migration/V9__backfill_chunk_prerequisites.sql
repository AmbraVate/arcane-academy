-- V9: Backfill chunk_prerequisites junction table from the deprecated prerequisite_ids TEXT column.
-- Rows that already exist in the junction table are skipped (ON CONFLICT DO NOTHING).
INSERT INTO chunk_prerequisites (chunk_id, prerequisite_id)
SELECT c.id, p.value
FROM   chunks c,
       json_array_elements_text(c.prerequisite_ids::json) p(value)
WHERE  c.prerequisite_ids IS NOT NULL
  AND  c.prerequisite_ids <> ''
  AND  c.prerequisite_ids <> '[]'
ON CONFLICT DO NOTHING;
