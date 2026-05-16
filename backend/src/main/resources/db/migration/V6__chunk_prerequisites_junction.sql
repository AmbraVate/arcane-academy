-- V6: Add chunk_prerequisites junction table
-- Replaces the denormalized prerequisiteIds TEXT column with a proper relation.
-- The old `prerequisite_ids` column is left in place until application code is
-- updated to use this table; a follow-up migration can drop it.
CREATE TABLE IF NOT EXISTS chunk_prerequisites (
    chunk_id          VARCHAR(255) NOT NULL,
    prerequisite_id   VARCHAR(255) NOT NULL,
    PRIMARY KEY (chunk_id, prerequisite_id),
    FOREIGN KEY (chunk_id)        REFERENCES chunks(id) ON DELETE CASCADE,
    FOREIGN KEY (prerequisite_id) REFERENCES chunks(id) ON DELETE CASCADE
);
