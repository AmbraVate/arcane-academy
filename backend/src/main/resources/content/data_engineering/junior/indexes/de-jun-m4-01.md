---
id: de-jun-m4-01
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m4
moduleTitle: "Module 4: Indexing & Performance"
moduleGlyph: "📊"
moduleSortOrder: 4
topicSlug: indexes
topicTitle: "Indexes"
topicSortOrder: 1
lesson: indexes
title: "Indexes"
sortOrder: 1
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m3-04]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what a B-tree index is and how it speeds up lookups
    - Identifies which columns are good candidates for indexing
    - Explains the write overhead of indexes
    - Describes composite indexes and the leading column rule
    - Distinguishes unique indexes, partial indexes, and covering indexes
  keywords: [index, B-tree, composite, covering index, partial index, unique index, leading column, cardinality, write overhead, CREATE INDEX, foreign key, selectivity]
  modelAnswer: |
    A B-tree index is a balanced tree structure that allows O(log n) lookups instead of O(n) full scans. Good candidates: columns used in WHERE, JOIN ON, ORDER BY, with high cardinality (many distinct values). Indexes slow down writes: every INSERT/UPDATE/DELETE must update all relevant indexes. Composite indexes: (col1, col2) — usable for queries on col1 alone, or col1 AND col2 together, but not col2 alone (leading column rule). Covering index: includes all columns needed by the query — avoids fetching the table row (index-only scan). Partial index: covers only a subset of rows (WHERE status = 'active') — smaller and faster for selective queries.
guidedSteps:
  - id: de-jun-m4-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You have an index on (last_name, first_name). Which query can use this index effectively?
    inputConfig:
      options:
        - "WHERE first_name = 'Alice' (first_name only)"
        - "WHERE last_name = 'Smith' (last_name only — the leading column)"
        - "WHERE first_name = 'Alice' AND last_name = 'Smith' (both columns, first_name first)"
        - "ORDER BY first_name (ordering by non-leading column)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["WHERE last_name = 'Smith' (last_name only — the leading column)"]
      rejectedFeedback: "A composite index (last_name, first_name) is sorted first by last_name, then by first_name within each last_name. The index can be used for: queries on last_name alone, queries on last_name AND first_name together. It cannot efficiently serve queries on first_name alone — the first_name values are not sorted globally in the index, only within each last_name group. This is the leading column rule (also called the leftmost prefix rule). The index is most selective when the leading column has high cardinality (many distinct values)."
    hint: "Which column is 'first' in the index? That column's prefix must be present in the WHERE clause."
    reflectionPrompt: "If you also need to query by first_name alone frequently, what would you do?"
  - id: de-jun-m4-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      An index that includes all columns needed by a query, allowing the database to satisfy the query entirely from the index without accessing the table rows, is called a ________ index.
    inputConfig:
      placeholder: "covering"
    markingRule:
      matchMode: CONTAINS
      accepted: [covering, "covering index", "index-only", "index only"]
      rejectedFeedback: "A covering index includes all columns needed by the query (SELECT columns, WHERE columns, ORDER BY columns). The database can satisfy the query entirely from the index without accessing the table rows — an Index Only Scan in the execution plan. This avoids random I/O to the table heap. Example: for SELECT customer_id, name FROM customers WHERE region = 'North', a covering index on (region, customer_id, name) allows an Index Only Scan. The index is wider (more storage, slower writes) but enables the fastest possible reads for the specific query pattern."
    hint: "The index contains all the data needed — the table row never needs to be read."
    reflectionPrompt: "What is the trade-off of making every index a covering index?"
  - id: de-jun-m4-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why adding indexes on every column is a bad idea, focusing on the write-side cost.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [write, INSERT, UPDATE, DELETE, maintain, slow, overhead, storage, B-tree, update, each]
      rejectedFeedback: "Every index must be kept up to date when data changes. An INSERT adds a new entry to every index on the table. An UPDATE to an indexed column removes the old entry and inserts the new one in each relevant index. A DELETE removes entries from every index. A table with 10 indexes incurs 10× the index maintenance overhead on every write. For write-heavy tables (transaction logs, event streams, IoT data), excessive indexes can make writes the bottleneck. Index selectively: only columns with high selectivity that appear in frequent WHERE, JOIN, or ORDER BY clauses."
    hint: "How many B-tree updates does an INSERT into a table with 10 indexes require?"
    reflectionPrompt: "How would you decide which indexes to keep on a table that is both heavily read and heavily written?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A table has 10 million rows and a column status with only 3 distinct values ('active', 'inactive', 'suspended'). Is this a good indexing candidate?"
    options:
      - "Yes — any column used in WHERE benefits from an index"
      - "No — low cardinality (3 distinct values) means the index returns millions of rows and is often not faster than a full scan"
      - "Yes — the small number of distinct values makes the index small and fast"
      - "No — indexes only work with numeric columns"
    correctIndex: 1
    feedback: "Cardinality (number of distinct values) is key to index effectiveness. A status column with 3 values means each value represents ~33% of the table. An index on status for WHERE status = 'active' returns ~3.3 million rows — the database may decide a sequential scan is faster than randomly jumping to 3.3M scattered rows via the index. Indexes are most effective on high-cardinality columns (email, order_id, created_at) where the filter returns a small fraction of rows. Exception: a partial index on a rare value (WHERE status = 'suspended' covers only 0.1% of rows) can be effective even on a low-cardinality column."
  - type: MULTIPLE_CHOICE
    question: "Foreign key columns (e.g. order_lines.order_id) should almost always be indexed because:"
    options:
      - "Foreign key constraints require an index automatically"
      - "JOIN operations on foreign keys require a full scan without an index, causing severe performance degradation on large tables"
      - "The database uses the foreign key index for constraint checking"
      - "Both B and C"
    correctIndex: 3
    feedback: "Both B and C are correct. JOINs on foreign keys (JOIN orders o ON ol.order_id = o.order_id) need to look up rows from both sides of the join. Without an index on order_lines.order_id, every order requires a full scan of order_lines — O(n) per order. Additionally, some databases (MySQL InnoDB) check foreign key constraints on INSERT/UPDATE/DELETE by looking up the referenced table — an index on the FK column makes this O(log n) instead of O(n). MySQL automatically creates an index on FK columns; PostgreSQL does not — explicit CREATE INDEX is required."
retrieval:
  recall: "Design the index strategy for an orders table with columns: order_id (PK), customer_id (FK), status, order_date, total_amount — considering queries that filter by customer_id, status, and order_date ranges."
  explain: "Explain the B-tree index structure: what it stores, how a range scan uses it, and why a leading-wildcard LIKE ('%text') cannot use a B-tree index."
  mistakeId:
    code: "CREATE INDEX idx_orders_all ON orders (order_id, customer_id, status, order_date, total_amount);"
    answer: "This composite index on all five columns is ineffective for most queries. A composite index is most useful when queries frequently filter on all leading columns together. With five columns, the index is rarely useful beyond queries that filter on order_id alone (the leading column) — which already has a primary key index. The wide index consumes significant storage and adds write overhead. Better: create targeted indexes for actual query patterns: CREATE INDEX idx_orders_customer ON orders (customer_id, order_date) for customer history queries; CREATE INDEX idx_orders_status_date ON orders (status, order_date) for status reports with date filters. Covering indexes should include only the SELECT columns the query needs."
---

# Hook

A well-written query against a poorly indexed table will be slow. A mediocre query against a well-indexed table will be fast. Indexes are the most impactful performance tool available to a data engineer — but they have costs. Understanding what indexes do, when to create them, and when not to is fundamental to production database engineering.

# Lore Introduction

"The member search is taking four seconds," the Junior Engineer reported. "The query is correct but slow." The Senior Archivist looked at the query — `WHERE last_name = 'Selvaris'` on a 500,000-row members table. "Sequential scan. Every row checked. 500,000 comparisons." She ran CREATE INDEX. "Re-run it." The Junior ran the query. "12 milliseconds." The Senior Archivist nodded. "That's the impact of an index: O(n) → O(log n). Five hundred thousand operations to twenty." She paused. "But indexes have a cost. Every INSERT pays to maintain that index. Add too many and your writes slow down. The craft is knowing which indexes to create."

# Core Learning

## Concept Introduction

### What is a B-tree Index?

```
B-tree index structure (simplified):
                    [Smith, Taylor]
                   /      |      \
         [Jones, King]  [Smith]  [Taylor, White]
         /     |    \     |        /       \
      [J]   [K]   [Ko]  [S]     [T]        [W]
      rows  rows  rows  rows    rows        rows

Benefits:
  - Equality: WHERE last_name = 'Smith' → O(log n) lookups
  - Range: WHERE last_name BETWEEN 'S' AND 'T' → follow branch + scan
  - Ordering: ORDER BY last_name → in-order traversal (pre-sorted)
```

### Creating Indexes

```sql
-- Basic index on a single column
CREATE INDEX idx_members_last_name ON members (last_name);

-- Unique index (enforces uniqueness + fast lookup)
CREATE UNIQUE INDEX idx_members_email ON members (email);
-- equivalent to UNIQUE constraint with faster lookup

-- Composite index: filters or sorts by multiple columns
CREATE INDEX idx_orders_customer_date ON orders (customer_id, order_date);
-- Supports: WHERE customer_id = ?
--           WHERE customer_id = ? AND order_date > ?
-- Does NOT support: WHERE order_date > ? (alone, without customer_id)

-- Covering index: includes extra columns to avoid table row lookup
CREATE INDEX idx_orders_status_covering
ON orders (status, order_date)
INCLUDE (total_amount, customer_id);  -- PostgreSQL INCLUDE syntax
-- Query: SELECT order_id, total_amount FROM orders WHERE status = 'completed'
-- → Index Only Scan (no table row fetch needed)

-- Partial index: only indexes rows matching a condition
CREATE INDEX idx_orders_pending ON orders (created_at)
WHERE status = 'pending';
-- Small index: only pending orders (~5% of table)
-- Fast for: WHERE status = 'pending' AND created_at > ...
```

### The Leading Column Rule

```sql
-- Index: (customer_id, order_date, status)

-- Uses the index (full, most selective)
WHERE customer_id = 42 AND order_date > '2024-01-01' AND status = 'completed'

-- Uses the index (partial — customer_id is the leading column)
WHERE customer_id = 42

-- Uses the index (partial — customer_id then order_date)
WHERE customer_id = 42 AND order_date > '2024-01-01'

-- Cannot use index efficiently (skips the leading column)
WHERE order_date > '2024-01-01'   -- order_date is not the leading column
WHERE status = 'completed'         -- status is not the leading column
```

### Index Selectivity and Cardinality

```sql
-- High cardinality → high selectivity → good index candidate
-- email: 100,000 unique values in 100,000 rows (cardinality = 100%)
-- order_date: many distinct dates (high cardinality)
-- customer_id: 50,000 distinct values (good FK index candidate)

-- Low cardinality → low selectivity → poor index candidate alone
-- status: 3 distinct values in 100,000 rows → each returns 33,000 rows
-- is_deleted: 2 values → each returns 50% of table

-- Check cardinality:
SELECT COUNT(DISTINCT status) AS distinct_count,
       COUNT(*) AS total_rows,
       COUNT(DISTINCT status) * 100.0 / COUNT(*) AS selectivity_pct
FROM orders;
```

### Index Maintenance Cost

```sql
-- Each INSERT updates all indexes on the table
INSERT INTO orders (customer_id, status, order_date, total_amount)
VALUES (42, 'pending', NOW(), 150.00);
-- Updates: primary key index, idx_orders_customer_date,
--          idx_orders_status_covering, idx_orders_pending (if status='pending')

-- Monitoring index usage (PostgreSQL)
SELECT schemaname, tablename, indexname,
       idx_scan    AS times_used,
       idx_tup_read AS rows_returned
FROM pg_stat_user_indexes
WHERE tablename = 'orders'
ORDER BY idx_scan DESC;
-- Indexes with 0 or near-0 idx_scan are candidates for removal
```

## Why It Matters

Indexes are the single most powerful performance tool in databases — the difference between scanning ten million rows and jumping to ten:

- Most "the app is slow" tickets trace back to a missing index on a filtered or joined column
- But indexes aren't free: every INSERT and UPDATE must maintain them, so over-indexing slows writes — the trade-off is the engineering judgement
- Composite index column order decides whether queries can use the index at all; it's the detail interviews love and production punishes

An engineer who understands indexes can often deliver a 100× speedup with one line of DDL. Few skills have a better effort-to-impact ratio.

## Common Mistakes

- **Indexing every column**: More indexes = more write overhead. Index columns actually used in WHERE, JOIN, ORDER BY on large tables with high selectivity.
- **Composite index in the wrong order**: Put the most selective and most frequently filtered column first. (customer_id, status) is better than (status, customer_id) if queries always include customer_id.
- **Not indexing foreign keys**: JOINs and FK constraint checks both need index support on the FK column. MySQL creates them automatically; PostgreSQL does not.
- **Adding indexes under load**: CREATE INDEX on a large live table acquires a lock and blocks writes. Use `CREATE INDEX CONCURRENTLY` (PostgreSQL) or online DDL tools for production tables.

## Mental Model

A database index is like a book's index — instead of reading every page to find "photosynthesis," you look it up in the back and jump directly to pages 45, 182, and 317. The index is a separate, sorted structure that maps values to row locations. Creating many indexes is like creating many back-of-book indexes for different word types — useful for readers but doubles the effort to maintain when the book is updated.

## Mini Summary

- ✔ B-tree indexes allow O(log n) lookups vs O(n) sequential scan
- ✔ Good candidates: high-cardinality columns in WHERE, JOIN ON, ORDER BY
- ✔ Composite index: leading column rule — leftmost prefix must be in the query
- ✔ Covering index: includes all query columns — enables Index Only Scan
- ✔ Partial index: indexes a subset of rows — smaller and faster for selective queries
- ✔ Indexes slow writes — add only indexes that are actually used

# Guided Practice Quest

Work through the guided steps to identify missing indexes on a provided slow query using EXPLAIN, create a composite index for a customer+date filter, and design a covering index for a specific SELECT pattern.

# Solo Practice Quest

Analyse the query patterns for the Archive system and design an index strategy: the system runs the following queries frequently: (1) find all active loans for a member; (2) find all overdue loans (due_date < today, return_date IS NULL); (3) find items available for borrowing by category; (4) search members by last name; (5) get monthly statistics (count loans by month). For each query: write the EXPLAIN output (simulated), identify the sequential scan if present, create the optimal index, and explain your index design choices (column order, partial/covering/composite). Then list three indexes you would NOT create and explain why.

# Integration

**Mathematics**: B-tree indexes implement a balanced tree data structure invented by Bayer and McCreight (1972). A B-tree of order m has nodes that contain between ⌈m/2⌉ and m children. For n records and a B-tree of order m ≈ 100 (chosen to match disk page sizes), the tree has at most log₁₀₀(n) levels. For n = 1,000,000: log₁₀₀(1,000,000) = 3 levels — three I/O operations to find any record. Without the index: O(n) = 1,000,000 row comparisons. The cardinality-selectivity relationship formalises when the index is beneficial: an index is faster than a full scan when the fraction of rows returned f < 1/(cost_ratio), where cost_ratio is the ratio of sequential to random I/O cost (typically 10-100x for spinning disks, smaller for SSDs).

**Sciences (Library Science — Catalogue Indexing)**: The analogy between database indexes and library catalogues is direct and historical. Before computer databases, libraries used physical card catalogues — an author card index, a title card index, a subject index. Each is a sorted structure mapping a key (author name, title, subject) to the location of the book. The author index is a composite index on (last_name, first_name): highly selective, sorted correctly, supports prefix searches. Subject headings are low-cardinality indexes: few distinct values, each pointing to many books. The Library of Congress Subject Headings (LCSH) system is essentially a controlled vocabulary for the subject index — database engineers make the same decisions about which fields to index, in what order, and with what cardinality considerations.

# Lore Conclusion

"Seven new indexes deployed," the Junior Engineer reported. "Member search: 4 seconds → 12 milliseconds. Overdue report: 8 seconds → 150 milliseconds. Monthly statistics: partial covering index, 2 seconds → 80 milliseconds." The Senior Archivist reviewed the index usage statistics. "All seven are being used. Write overhead is acceptable — the Archive writes less than it reads." She closed the monitor. "You've indexed correctly: targeted, high-selectivity columns, composite indexes in the right order, partial index for the overdue filter." She set the report aside. "Next: execution plans — reading the database's own description of how it will run your query, and using that to diagnose performance."

---
