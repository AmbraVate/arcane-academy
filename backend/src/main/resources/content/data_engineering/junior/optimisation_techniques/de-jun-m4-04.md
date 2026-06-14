---
id: de-jun-m4-04
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m4
moduleTitle: "Module 4: Indexing & Performance"
moduleGlyph: "📊"
moduleSortOrder: 4
topicSlug: optimisation_techniques
topicTitle: "Optimisation Techniques"
topicSortOrder: 4
lesson: optimisation_techniques
title: "Optimisation Techniques"
sortOrder: 4
difficulty: 3
estimatedMinutes: 35
xpReward: 55
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m4-03]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Lists at least five concrete SQL query rewriting techniques
    - Explains query batching and when it replaces row-by-row processing
    - Describes connection pooling and why opening a new connection per query is expensive
    - Explains pagination and cursor-based vs offset-based approaches
    - Identifies when denormalisation is appropriate for read performance
  keywords: [rewrite, batching, connection pooling, pagination, cursor, offset, LIMIT, denormalisation, materialised view, bulk, IN clause, batch INSERT, pooler]
  modelAnswer: |
    Key optimisation techniques: (1) Sargable predicate rewrites — remove functions from WHERE column references; (2) Batching — send multiple rows in one INSERT or process multiple IDs in one IN() instead of row by row; (3) Connection pooling — reuse database connections (PgBouncer, HikariCP) to avoid per-query connection overhead; (4) Cursor/keyset pagination — use WHERE id > last_seen_id instead of OFFSET n for stable, efficient pagination; (5) Denormalisation — store computed values (summary counts, pre-joined columns) for read performance at the cost of write complexity; (6) Materialised views — pre-compute expensive aggregations for dashboard queries.
guidedSteps:
  - id: de-jun-m4-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      An application inserts 10,000 log entries one at a time in a loop. Each INSERT takes 2ms (including network round trip). Total: 20 seconds. What is the most impactful fix?
    inputConfig:
      options:
        - "Add an index to the log table"
        - "Use a batch INSERT — send all 10,000 rows in one statement: INSERT INTO logs VALUES (...), (...), ..."
        - "Use a faster server"
        - "Increase the database connection timeout"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Use a batch INSERT — send all 10,000 rows in one statement: INSERT INTO logs VALUES (...), (...), ..."]
      rejectedFeedback: "The bottleneck is not the INSERT itself (2ms is reasonable) but the 10,000 network round trips × 2ms = 20 seconds. Batch INSERT sends all rows in one SQL statement: INSERT INTO logs (col1, col2) VALUES (v1a, v1b), (v2a, v2b), ..., (v10000a, v10000b). This is one network round trip, one statement parse, one transaction — typically 10-100x faster for bulk inserts. Most databases support batch INSERT (PostgreSQL, MySQL, SQL Server). For very large batches (>10,000 rows), use COPY (PostgreSQL) or LOAD DATA INFILE (MySQL) for maximum throughput."
    hint: "The time cost is 10,000 network round trips. What reduces round trips from 10,000 to 1?"
    reflectionPrompt: "What is the maximum practical batch size for INSERT? What happens if the batch is too large?"
  - id: de-jun-m4-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Instead of SELECT ... LIMIT 20 OFFSET 10000 (which must skip 10,000 rows), efficient pagination uses WHERE id > ________ (the last seen ID) — called keyset or cursor pagination.
    inputConfig:
      placeholder: "last_seen_id"
    markingRule:
      matchMode: CONTAINS
      accepted: ["last_seen_id", "last seen id", "previous id", "last id", "cursor", "keyset"]
      rejectedFeedback: "OFFSET n pagination requires the database to generate and then skip n rows on every page request — page 500 (OFFSET 10,000) is 500x slower than page 1. Keyset (cursor) pagination uses WHERE id > last_seen_id LIMIT 20 — the database uses the index on id to jump directly to the starting point, regardless of which page you're on. Page 500 is as fast as page 1. Limitation: you cannot jump to an arbitrary page number with keyset pagination — you must navigate sequentially. For most user-facing pagination (infinite scroll, next/prev navigation), this is acceptable."
    hint: "What value do you store from the last fetched page to know where to start the next page?"
    reflectionPrompt: "When would you be forced to use OFFSET pagination instead of keyset pagination?"
  - id: de-jun-m4-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain what connection pooling is and why opening a new database connection for every query is expensive.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [connection, pool, reuse, overhead, establish, TCP, authentication, handshake, cost, per-request, latency, pooler]
      rejectedFeedback: "Opening a database connection involves: TCP handshake, SSL negotiation (if encrypted), authentication, session initialisation — typically 50-200ms. A connection pool maintains a set of already-open connections that queries can borrow and return. Instead of opening a new connection per query (expensive), the query borrows an existing one from the pool (microseconds). PgBouncer (PostgreSQL), HikariCP (Java/Spring), and similar poolers are essential in production systems where the application serves many concurrent requests. Without pooling, 1000 concurrent users each opening their own connection would overwhelm the database (connection limit, memory, CPU for connection management)."
    hint: "A new connection requires TCP, SSL, auth, and session setup — how does a pool avoid this per request?"
    reflectionPrompt: "What is the difference between connection pool size and database max_connections? Why should pool size typically be much smaller?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "When should denormalisation (storing computed or redundant data) be considered?"
    options:
      - "Always — it always improves performance"
      - "When read performance is critical and the data is read far more often than written, and the normalised join is too slow"
      - "When storage is limited and duplicating data is necessary"
      - "When the database does not support JOINs"
    correctIndex: 1
    feedback: "Denormalisation trades write complexity and data consistency risk for read performance. It is appropriate when: (1) a JOIN is genuinely too slow and indexing cannot fix it; (2) the data is read orders of magnitude more often than written; (3) the computed value is stable or can be updated reliably via triggers or application logic. Examples: storing order_count on the customers table (updated by trigger); storing category_name in order_lines (avoids JOIN to categories for reporting). Always maintain normalised data as the source of truth — denormalised values should be derivable from it for integrity checks."
  - type: MULTIPLE_CHOICE
    question: "An API endpoint queries SELECT * FROM products WHERE category_id = ? and is called 50,000 times per hour. The products table changes rarely. What optimisation technique would most reduce database load?"
    options:
      - "Add an index on category_id"
      - "Rewrite to use a stored procedure"
      - "Cache the query result at the application layer — serve from memory, refresh periodically"
      - "Switch to OFFSET pagination"
    correctIndex: 2
    feedback: "50,000 identical (or near-identical) queries per hour on rarely-changing data is a caching opportunity. An application-layer cache (Redis, Memcached, in-process LRU cache) returns the result from memory without hitting the database for the remaining 49,999 requests after the first cache population. Cache invalidation strategy: time-based (TTL — e.g. refresh every 60 seconds) or event-based (invalidate when a product in that category is updated). An index on category_id (option A) would help but still hits the database 50,000 times. Caching eliminates most of those database hits entirely."
retrieval:
  recall: "List seven optimisation techniques with a one-line description and a concrete example of each."
  explain: "Explain keyset pagination vs OFFSET pagination — algorithmic complexity, implementation, and the scenarios where OFFSET is unavoidable."
  mistakeId:
    code: "-- Processing 10,000 members individually\nFOR member_id IN (SELECT member_id FROM members WHERE renewal_due = TRUE) LOOP\n    UPDATE members SET status = 'renewed' WHERE member_id = member_id;\n    INSERT INTO renewal_log (member_id, renewed_at) VALUES (member_id, NOW());\nEND LOOP;"
    answer: "This cursor loop executes 20,000 SQL statements (1 UPDATE + 1 INSERT per member × 10,000 members). Each statement is a separate round trip and transaction. Replace with set-based operations: UPDATE members SET status = 'renewed' WHERE renewal_due = TRUE; INSERT INTO renewal_log (member_id, renewed_at) SELECT member_id, NOW() FROM members WHERE renewal_due = TRUE; This is 2 statements instead of 20,000 — the database processes all 10,000 rows internally without application-layer looping. Set-based operations are the SQL philosophy: express what to do, not how to do it row by row. Cursor loops in application code are almost always replaceable with set operations."
---

# Hook

You can identify slow queries with execution plans and analyse system-wide hotspots with pg_stat_statements. The final skill is the toolkit of concrete fixes: rewriting predicates, batching operations, caching results, paginating efficiently, pooling connections, and knowing when denormalisation is the right call. This lesson consolidates the optimisation techniques that turn diagnosis into improvement.

# Lore Introduction

"I've identified seven performance problems," the Junior Engineer reported. "Missing index, N+1, SELECT *, stale statistics, two sargable rewrites, and 10,000 individual inserts in a loop." The Senior Archivist nodded. "Now fix them. But use the right tool for each." She went through the list. "Indexes for the access pattern. Single JOIN for the N+1. Specific columns for SELECT *. ANALYZE for statistics. Rewrite predicates for sargability. Batch INSERT for the loop — one statement for 10,000 rows." She paused. "And the catalogue search — queried 50,000 times per hour, changes twice a day. Cache it. Don't query the database 50,000 times for data that doesn't change." The Junior Engineer nodded. "Different problem, different tool."

# Core Learning

## Concept Introduction

### 1. Sargable Predicate Rewrites (Review and Expand)

```sql
-- Remove functions from WHERE column references
WHERE YEAR(order_date) = 2024          → WHERE order_date >= '2024-01-01' AND order_date < '2025-01-01'
WHERE LOWER(email) = 'user@example.com' → WHERE email = 'user@example.com' (normalise at write)
WHERE total_amount / 100 > 50           → WHERE total_amount > 5000
WHERE TRIM(name) = 'Smith'              → WHERE name = 'Smith' (normalise at write)
WHERE status != 'cancelled'             → WHERE status IN ('pending','confirmed','completed') (if low cardinality)
```

### 2. Batch Operations

```sql
-- Bad: row-by-row INSERT (N round trips)
-- for each row: INSERT INTO logs (type, msg) VALUES (?, ?);

-- Good: batch INSERT (1 round trip)
INSERT INTO logs (type, message, created_at)
VALUES
    ('info', 'System started', NOW()),
    ('info', 'User logged in', NOW()),
    ('warn', 'Slow query detected', NOW());
-- Add as many rows as practical (test with 100, 1000, 10000 row batches)

-- PostgreSQL COPY: fastest bulk loading
-- From application: use COPY protocol (JDBC, psycopg2 support this)
-- From file:
COPY products (name, category_id, price, stock_qty)
FROM '/tmp/products.csv'
WITH (FORMAT csv, HEADER true);

-- Batch DELETE (avoid full table scan loops)
DELETE FROM logs WHERE created_at < NOW() - INTERVAL '90 days';
-- One statement, one scan, one transaction — not 1M individual DELETEs

-- Batch UPDATE
UPDATE members
SET last_renewal_notice = NOW()
WHERE status = 'active' AND last_renewal_notice < NOW() - INTERVAL '30 days';
```

### 3. Connection Pooling

```
Database connection lifecycle (without pooling):
  1. Application opens TCP connection to database (5ms)
  2. SSL handshake (5ms)
  3. Database authentication (10ms)
  4. Session initialisation (5ms)
  5. Execute query (2ms)
  6. Close connection (1ms)
  Total: ~28ms per query, 2ms actual work

With connection pooling (PgBouncer / HikariCP):
  1. Borrow idle connection from pool (< 1ms)
  2. Execute query (2ms)
  3. Return connection to pool (< 1ms)
  Total: ~3ms per query
  
  A pool of 10 connections handles 1000 concurrent requests
  (each waits briefly for a pool slot — far cheaper than 1000 connections)
```

```
Connection pool sizing:
  Rule of thumb: pool_size = (cpu_cores × 2) + effective_spindle_count
  Typical: 10-20 connections for a web application
  NOT: one connection per application thread (can be thousands)
```

### 4. Pagination

```sql
-- Offset pagination (simple but slow on large pages)
SELECT id, name, created_at FROM members
ORDER BY created_at DESC
LIMIT 20 OFFSET 10000;
-- Problem: database generates 10,020 rows and discards 10,000 — waste grows with page number

-- Keyset (cursor) pagination (fast at any page depth)
SELECT id, name, created_at FROM members
WHERE created_at < :last_seen_created_at   -- from previous page's last row
   OR (created_at = :last_seen_created_at AND id < :last_seen_id)  -- tie-break
ORDER BY created_at DESC, id DESC
LIMIT 20;
-- Requires: index on (created_at DESC, id DESC)
-- Fastest page: same speed regardless of page depth
-- Works with: next/prev, infinite scroll, API cursors
-- Does not work with: jump to page 500

-- UUID/ID-based cursor (simpler when ordering by ID)
SELECT * FROM orders WHERE id > :cursor ORDER BY id ASC LIMIT 20;
-- cursor = last_id from previous response
```

### 5. Denormalisation for Read Performance

```sql
-- Normalised: expensive JOIN on every read
SELECT c.customer_id, c.name, COUNT(o.order_id) AS order_count
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.name;
-- On 1M orders: expensive

-- Denormalised: pre-computed column
ALTER TABLE customers ADD COLUMN order_count INT NOT NULL DEFAULT 0;

-- Maintained by trigger (or application code):
CREATE TRIGGER after_order_insert
AFTER INSERT ON orders FOR EACH ROW
UPDATE customers SET order_count = order_count + 1 WHERE customer_id = NEW.customer_id;

CREATE TRIGGER after_order_delete
AFTER DELETE ON orders FOR EACH ROW
UPDATE customers SET order_count = order_count - 1 WHERE customer_id = OLD.customer_id;

-- Read is now instant (no JOIN)
SELECT customer_id, name, order_count FROM customers WHERE order_count > 10;
```

### 6. Caching Strategy

```
Query result caching levels:
  1. Database query cache (MySQL had this — deprecated, too coarse)
  2. Materialised views (database-level, refresh on demand)
  3. Application-layer cache (Redis, Memcached — most flexible)
  4. CDN / HTTP cache (for read-only API endpoints)

When to cache:
  ✓ Data changes infrequently (product catalogue, reference data)
  ✓ Same query called many times with same parameters
  ✓ Result is expensive to compute
  ✓ Stale data is acceptable within a time window (TTL)

When NOT to cache:
  ✗ Data must always be real-time (bank balance, seat availability)
  ✗ Parameters vary too much (every user sees different data)
  ✗ The computation is cheap (don't add cache complexity for trivial queries)
```

### 7. Set-Based vs Row-Based Processing

```sql
-- Replace application loops with SQL set operations
-- Bad: application loops calling SQL per row
-- for each active_member: UPDATE ... WHERE member_id = member.id

-- Good: set operation processes all rows in one statement
UPDATE members
SET renewal_reminder_sent = TRUE, last_contact = NOW()
WHERE status = 'active'
  AND renewal_due_date BETWEEN NOW() AND NOW() + INTERVAL '30 days'
  AND renewal_reminder_sent = FALSE;

-- Generate derived results without loops
INSERT INTO overdue_fees (member_id, fee_amount, generated_at)
SELECT
    l.member_id,
    DATEDIFF(CURRENT_DATE, l.due_date) * 0.50 AS fee_amount,  -- £0.50/day
    NOW()
FROM loans l
WHERE l.return_date IS NULL
  AND l.due_date < CURRENT_DATE
  AND NOT EXISTS (
      SELECT 1 FROM overdue_fees of
      WHERE of.member_id = l.member_id
        AND of.generated_at::DATE = CURRENT_DATE
  );
-- Generates all overdue fee records in one INSERT ... SELECT
```

## Why It Matters

Query optimisation is the highest-leverage performance work in most systems — the database is usually the bottleneck, and the fixes are often small:

- Rewriting a correlated subquery as a join, or making a predicate sargable, can deliver speedups no amount of application caching matches
- `SELECT *` and functions wrapped around indexed columns are tiny habits with large production costs
- Optimisation follows a method — measure, read the plan, change one thing, re-measure — not folklore

Hardware upgrades buy 2×; a corrected query plan routinely buys 100×. This skill pays for itself on its first production incident.

## Common Mistakes

- **Batching without transaction control**: A batch INSERT of 10,000 rows in one transaction — if the database crashes mid-insert, all 10,000 may be lost or partially inserted. Wrap large batches in explicit transactions and consider committing in chunks (1,000 at a time) for large operations.
- **Connection pool too large**: More connections = more database memory and CPU for connection management. The sweet spot is usually 10-20 connections for most web applications, not 100+.
- **Cache invalidation problems**: Cached data becomes stale. A product price changes but the cache still serves the old price. Define TTL and invalidation events explicitly — don't assume caches are always fresh.
- **Denormalisation without integrity maintenance**: A denormalised column that falls out of sync with the source data is worse than no denormalisation — it introduces subtle data bugs that are hard to diagnose.

## Mental Model

Performance optimisation techniques are like different categories of tool in a workshop. Some tools reduce repetition (batching — do N things at once instead of one at a time). Some reduce setup time (connection pooling — don't rebuild the scaffolding for each job). Some pre-compute results (caching, materialised views — prepare in advance so delivery is instant). Some change the route (keyset pagination — take the direct road instead of driving past all previous exits). The skill is knowing which tool category the problem calls for — misidentifying the bottleneck and applying the wrong technique wastes time and may introduce new problems.

## Mini Summary

- ✔ Batch INSERT: N round trips → 1; critical for high-volume ingestion
- ✔ Set-based SQL: replace row-by-row loops with one SQL statement
- ✔ Connection pooling: reuse connections; avoid per-request connection overhead
- ✔ Keyset pagination: WHERE id > last_id; O(1) per page; use instead of OFFSET
- ✔ Denormalisation: pre-computed columns for read-heavy, write-seldom data (maintain carefully)
- ✔ Application caching: eliminate repeated identical database queries for stable data

# Guided Practice Quest

Work through the guided steps to convert a row-by-row INSERT loop to a batch INSERT, implement keyset pagination for an orders list API, and design a cache invalidation strategy for a product catalogue query called 50,000 times per hour.

# Solo Practice Quest

Apply the full optimisation toolkit to the Archive system: (1) the daily renewal processing updates 2,000 member records one at a time — rewrite as a set-based UPDATE; (2) the overdue notification system inserts 500 notification records individually — rewrite as batch INSERT; (3) the member search API returns the first 20 results with pagination — implement keyset pagination with a cursor token in the API response; (4) the category browse page queries all items in a category — this data changes rarely; design the caching strategy (what to cache, TTL, invalidation); (5) the Archive connection opens a new database connection for every API request — design the connection pooling configuration for a server with 4 CPU cores and an expected 200 concurrent users. Document trade-offs for each decision.

# Integration

**Mathematics**: Batching and set operations reduce algorithmic complexity by constant or polynomial factors. N individual round trips of cost c each = O(N × c). One batch operation with N rows = O(c + kN) where k is the per-row processing overhead (much smaller than c, the round-trip cost). For large N with c >> k (network latency dominates per-row cost), the improvement factor approaches c/k — potentially 100-1000×. Keyset pagination changes the algorithmic complexity of page retrieval: OFFSET n pagination is O(n) per page (must skip n rows); keyset pagination is O(log n) per page (B-tree lookup to the cursor position). For page 1000 with 20 items: OFFSET requires processing 20,000 rows; keyset requires traversing ~17 B-tree nodes (log₂(20,000) ≈ 14.3).

**Sciences (Logistics — Supply Chain Batching)**: The economic order quantity (EOQ) model in supply chain management is the physical counterpart of database batching. Setting up a manufacturing run has a fixed cost S (ordering cost); holding inventory has a per-unit cost h. The optimal batch size Q* = √(2DS/h), balancing setup cost against holding cost. Database batching has the same structure: a fixed cost per round trip (network latency, connection overhead = S) and a per-row cost (processing, storage = h). The optimal batch size minimises total cost — the same economic trade-off. Just-in-time manufacturing (small, frequent batches) corresponds to low-latency, row-by-row database operations. Batch manufacturing corresponds to high-throughput, large-batch database operations. The choice depends on the relative costs of latency vs throughput, exactly as in the EOQ model.

# Lore Conclusion

"Seven problems fixed," the Junior Engineer reported. "Database load reduced by 67%. Response times under 200ms across all Archive operations." The Senior Archivist reviewed the metrics. "Batch insert: 20 seconds → 120 milliseconds. N+1 removed: 45,000 queries → 1. Keyset pagination: consistent response time regardless of page depth. Connection pool: 200 concurrent users on 10 connections." She closed the monitoring dashboard. "You have completed Module 4: Indexing and Performance. You can now find slow queries with execution plans and pg_stat_statements, design optimal indexes, and apply the full toolkit of fixes." She set the files aside. "Module 5: Application Data Access — how Java applications connect to and interact with databases through JDBC, ORMs, JPA, and the Repository pattern."

---
