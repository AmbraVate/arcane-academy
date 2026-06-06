---
id: de-jun-m1-05
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m1
moduleTitle: "Module 1: Advanced SQL"
moduleGlyph: "⚡"
moduleSortOrder: 1
topicSlug: query_optimisation
topicTitle: "Query Optimisation"
topicSortOrder: 5
lesson: query_optimisation
title: "Query Optimisation"
sortOrder: 5
difficulty: 3
estimatedMinutes: 35
xpReward: 55
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m1-04]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what a query execution plan is and how to read it
    - Identifies sequential scan vs index scan in an execution plan
    - Describes why functions in WHERE clauses prevent index use
    - Lists three common query rewriting techniques that improve performance
    - Explains the difference between filtering early vs filtering late
  keywords: [execution plan, EXPLAIN, sequential scan, index scan, function on column, sargable, predicate pushdown, early filter, join order, cost, rows, index]
  modelAnswer: |
    A query execution plan describes how the database executes a query — which tables are scanned, whether indexes are used, how joins are performed, and the estimated cost. Sequential scans read every row; index scans jump directly to matching rows. Functions applied to indexed columns in WHERE clauses prevent index use (WHERE YEAR(date) = 2024 scans the whole table; WHERE date >= '2024-01-01' AND date < '2025-01-01' uses an index). Sargable predicates (Search ARGument ABLE) allow index use. Performance improvements: filter early with WHERE before GROUP BY; avoid SELECT *; use EXISTS instead of IN for large sets; join on indexed foreign keys; avoid correlated subqueries where a JOIN suffices.
guidedSteps:
  - id: de-jun-m1-05-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A query runs in 5 seconds on a 1,000,000-row orders table. The WHERE clause is: WHERE YEAR(order_date) = 2024. Why is this slow even though order_date is indexed?
    inputConfig:
      options:
        - "YEAR() is a built-in function that is inherently slow"
        - "The index on order_date cannot be used because a function is applied to the column — every row must be scanned"
        - "The table has too many rows for any query to be fast"
        - "ORDER BY is missing, causing a full scan"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The index on order_date cannot be used because a function is applied to the column — every row must be scanned"]
      rejectedFeedback: "When you wrap an indexed column in a function — YEAR(order_date), LOWER(name), TRIM(email) — the database cannot use the index because the index stores raw column values, not function results. The database must evaluate YEAR(order_date) for every row in the table (sequential scan). The fix: rewrite the predicate to avoid the function on the column. Instead of WHERE YEAR(order_date) = 2024, use WHERE order_date >= '2024-01-01' AND order_date < '2025-01-01'. Both return the same rows, but the rewrite is sargable — the index on order_date can be used."
    hint: "The index stores the raw value of order_date — not the result of YEAR(order_date)."
    reflectionPrompt: "Rewrite WHERE MONTH(created_at) = 6 to be index-friendly."
  - id: de-jun-m1-05-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A predicate in WHERE that can use an index is called ________ (Search ARGument ABLE).
    inputConfig:
      placeholder: "sargable"
    markingRule:
      matchMode: CONTAINS
      accepted: [sargable, "sarg", "search argument able", "searchable"]
      rejectedFeedback: "Sargable (Search ARGument ABLE) predicates allow the query optimiser to use an index for lookup rather than scanning the full table. Examples of sargable predicates: col = value, col > value, col BETWEEN a AND b, col LIKE 'prefix%' (prefix match only). Non-sargable predicates: YEAR(col) = value (function on column), col LIKE '%suffix' (leading wildcard), CONVERT(col, VARCHAR) = '123' (implicit conversion). Rewriting non-sargable predicates to sargable ones is one of the most impactful query optimisations."
    hint: "A predicate that allows the database to use an index — the database term for this property."
    reflectionPrompt: "Is col LIKE '%word%' sargable? What about col LIKE 'word%'?"
  - id: de-jun-m1-05-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why SELECT * is harmful in production queries, particularly when JOINs are involved.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [columns, all, unnecessary, network, memory, join, wide, index, cover, bandwidth, maintenance]
      rejectedFeedback: "SELECT * retrieves every column from every table in the query — including columns that the application never uses. On wide tables with large text or BLOB columns, this transfers unnecessary data over the network and into application memory. With JOINs, SELECT * returns duplicate columns (e.g. customer_id appears from both the customers and orders tables). It also prevents the use of covering indexes (an index that contains all required columns, avoiding a table lookup entirely). In production, always specify only the columns you need."
    hint: "What extra cost does SELECT * impose on the network and memory? How does it interact with indexes?"
    reflectionPrompt: "What is a covering index and how does specifying columns instead of SELECT * enable it?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In an execution plan, 'Seq Scan on orders (cost=0.00..45000.00 rows=1000000)' means:"
    options:
      - "The query is optimised and will run in 0 seconds"
      - "The database will read every row in the orders table — a full table scan"
      - "The query uses a sequential index"
      - "1,000,000 rows will be returned to the application"
    correctIndex: 1
    feedback: "Seq Scan (Sequential Scan) means the database will read every row in the table from start to finish — the slowest access method on large tables. The cost estimate (0.00..45000.00) is the query planner's estimate of the work required: start-up cost and total cost in arbitrary planner units. Rows=1000000 is the estimated row count. If you see Seq Scan on a large table and your query has a WHERE clause that should filter most rows, check whether the WHERE predicate is sargable and whether the relevant column is indexed."
  - type: MULTIPLE_CHOICE
    question: "Which of these query rewrites will most likely improve performance on a 10M-row table?"
    options:
      - "Adding ORDER BY at the end of every query"
      - "Moving a selective WHERE filter earlier in the query so it reduces the dataset before a GROUP BY or JOIN"
      - "Adding DISTINCT to all SELECT statements"
      - "Using subqueries instead of JOINs"
    correctIndex: 1
    feedback: "Filtering early reduces the number of rows that subsequent operations (GROUP BY, JOIN, window functions) need to process. If a WHERE clause reduces 10M rows to 10,000, the subsequent GROUP BY operates on 10,000 rows, not 10M. This is called predicate pushdown — the optimiser may do this automatically, but explicitly filtering with WHERE before expensive operations always helps. Adding ORDER BY adds a sort step. DISTINCT adds a deduplication step. Subqueries vs JOINs is often equivalent — the optimiser rewrites both to the same plan."
retrieval:
  recall: "List five common causes of slow SQL queries and the fix for each."
  explain: "Explain what an execution plan shows and how to identify whether a query is using an index or doing a full table scan."
  mistakeId:
    code: "SELECT c.name, COUNT(o.order_id) FROM customers c LEFT JOIN orders o ON c.customer_id = o.customer_id WHERE LOWER(c.email) = LOWER('User@Example.com') GROUP BY c.name"
    answer: "Two performance problems: (1) LOWER(c.email) applies a function to the indexed email column, preventing index use — every row is scanned. The fix: store email in lowercase at insert time (application-level normalisation) and use WHERE c.email = 'user@example.com'. Or create a functional index: CREATE INDEX idx_customers_email_lower ON customers (LOWER(email)). (2) If the table is large, applying LOWER() to every row is expensive. Additionally, the query joins all customers before filtering — the WHERE on email should be applied first to reduce the join set. Best practice: normalise case at write time so comparisons are always direct."
---

# Hook

A query that returns the correct result but takes 30 seconds is not production-ready. Query optimisation is the discipline of understanding how the database executes SQL — and writing queries that the database can execute efficiently. This lesson covers execution plans, the most common performance pitfalls, and the rewrites that fix them.

# Lore Introduction

"The Archive member report takes 40 seconds," the Senior Archivist said, handing the Junior Engineer the query. "Leadership wants it in under two seconds." The Junior reviewed the query — it worked, but it had five joins and a WHERE clause wrapping every column in a function. "The query is correct but every filter prevents the indexes from working," the Junior said. "The database reads every row in every table." The Senior Archivist nodded. "Read the execution plan first. It tells you what the database actually does. Then fix the most expensive step." The Junior ran EXPLAIN. "Seq Scan on members, 2.3 million rows. That's the problem." One rewrite later: "1.1 seconds." The Senior Archivist smiled. "Correct queries and fast queries are both required. This is why data engineers understand execution plans."

# Core Learning

## Concept Introduction

### Reading Execution Plans

```sql
-- PostgreSQL: EXPLAIN ANALYZE shows estimated AND actual costs
EXPLAIN ANALYZE
SELECT c.name, COUNT(o.order_id)
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.name;

-- Sample output (simplified):
-- HashAggregate (cost=12500..13000 rows=50000 actual time=280ms)
--   Hash Left Join (cost=5000..11000 actual time=150ms)
--     Seq Scan on customers c (cost=0..3000 rows=100000 actual time=20ms)
--     Hash (cost=2000..2000 rows=500000 actual time=90ms)
--       Seq Scan on orders o (cost=0..2000 rows=500000 actual time=80ms)
```

**Key nodes to recognise**:

| Node | Meaning |
|---|---|
| Seq Scan | Full table scan — reads every row |
| Index Scan | Uses an index to find matching rows |
| Index Only Scan | All needed data is in the index — no table lookup |
| Hash Join | Builds a hash table from one input, probes with the other |
| Nested Loop | For each row in outer, look up in inner (good for small sets) |
| Sort | Explicit sort step (check if you can avoid it with an index) |
| Hash Aggregate | GROUP BY using a hash table |

### The Cost of Functions on Indexed Columns

```sql
-- Non-sargable: function on column — index cannot be used
WHERE YEAR(order_date) = 2024           -- function on column
WHERE LOWER(email) = 'user@example.com' -- function on column
WHERE SUBSTRING(phone, 1, 3) = '020'    -- function on column
WHERE total_amount / 100 > 50           -- arithmetic on column

-- Sargable rewrites that allow index use
WHERE order_date >= '2024-01-01' AND order_date < '2025-01-01'
WHERE email = 'user@example.com'        -- normalise case at insert time
WHERE phone LIKE '020%'                 -- prefix LIKE is sargable
WHERE total_amount > 5000               -- move the constant, not the column
```

### SELECT * vs Specific Columns

```sql
-- SELECT * retrieves all columns — problematic for:
-- 1. Network: transfers data the app doesn't need
-- 2. Memory: loads unnecessary columns into result buffers
-- 3. Covering indexes: can't be used if * requires columns not in the index
-- 4. JOINs: ambiguous duplicate column names

-- Always specify needed columns
SELECT c.customer_id, c.name, o.order_id, o.total_amount
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id;

-- Covering index example:
-- If you have INDEX(customer_id, name) and query:
-- SELECT customer_id, name FROM customers WHERE customer_id = ?
-- → Index Only Scan (never touches the table row)
-- SELECT * would require reading the full table row (Index Scan + fetch)
```

### Filter Early

```sql
-- Slow: join all rows, then filter
SELECT c.name, o.total_amount
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id
WHERE c.region = 'North' AND o.status = 'completed';
-- Optimiser may push predicates automatically, but be explicit

-- Better: filter in subquery/CTE before the join
WITH north_customers AS (
    SELECT customer_id, name FROM customers WHERE region = 'North'
),
completed_orders AS (
    SELECT customer_id, total_amount FROM orders WHERE status = 'completed'
)
SELECT nc.name, co.total_amount
FROM north_customers nc
JOIN completed_orders co ON nc.customer_id = co.customer_id;
-- Each CTE filters first, then join operates on smaller sets
```

### Avoid Correlated Subqueries on Large Tables

```sql
-- Slow: correlated subquery runs once per customer (100,000 executions)
SELECT c.name,
    (SELECT MAX(o.total_amount) FROM orders o WHERE o.customer_id = c.customer_id)
FROM customers c;

-- Fast: single aggregation, then join
SELECT c.name, om.max_order
FROM customers c
JOIN (
    SELECT customer_id, MAX(total_amount) AS max_order
    FROM orders
    GROUP BY customer_id
) AS om ON c.customer_id = om.customer_id;
```

### LIMIT Pushdown and Early Termination

```sql
-- LIMIT helps when the optimiser can stop early
-- Index-ordered query with LIMIT — very fast
SELECT customer_id, name
FROM customers
ORDER BY created_at DESC
LIMIT 10;
-- If created_at is indexed, the database reads only the 10 newest rows

-- Without an index on created_at:
-- Full scan, full sort, then LIMIT — does not help until the end
```

### Common Performance Fixes Summary

```
Problem                          → Fix
──────────────────────────────────────────────────────────────────
Function on indexed column       → Rewrite predicate (no function on col)
SELECT * with wide tables        → Specify only needed columns
Correlated subquery on large table → Rewrite as JOIN + aggregation
Missing WHERE filter (no index)  → Add index on filter column
Seq Scan on large join table     → Add FK index
OR in WHERE (splits plans)       → UNION ALL or index on each condition
NOT IN with NULLs                → Use NOT EXISTS or EXCEPT
```

## Common Mistakes

- **Ignoring EXPLAIN output**: Running slow queries without reading the execution plan is debugging without evidence. Always EXPLAIN before optimising.
- **Premature optimisation**: Optimise queries that are actually slow on real data volumes, not queries on small test datasets. A sequential scan on a 100-row table is fine.
- **Rewriting without verifying results match**: Optimised query must return identical results. Run both versions on the same data and compare counts.
- **Adding indexes on every column**: Indexes speed up reads but slow down writes (each INSERT/UPDATE/DELETE must maintain all indexes). Index selectively — high-cardinality columns used in WHERE, JOIN ON, or ORDER BY.

## Mental Model

A query execution plan is a recipe the database chooses to follow. Your job is to write queries that let the database choose a good recipe. The most important choice: can the database use an index (jump directly to matching rows) or must it use a full scan (read every row)? Indexes are like an alphabetical index at the back of a textbook — you jump to the right page instead of reading every page. Functions on indexed columns destroy this: the index stores "2024-06-15" but you asked for YEAR() — the index cannot answer that without reading each entry. Write your predicates to match what the index stores.

## Mini Summary

- ✔ EXPLAIN ANALYZE shows the execution plan — Seq Scan (slow) vs Index Scan (fast)
- ✔ Functions on indexed columns prevent index use — rewrite to sargable predicates
- ✔ SELECT specific columns; SELECT * prevents covering index optimisation
- ✔ Filter early (WHERE before JOIN on large tables)
- ✔ Rewrite correlated subqueries on large tables as JOINs with pre-aggregation
- ✔ LIMIT + ORDER BY on indexed column enables early termination

# Guided Practice Quest

Work through the guided steps to read a provided execution plan and identify the most expensive operation, rewrite a non-sargable predicate to be index-friendly, and rewrite a correlated subquery as a pre-aggregation join.

# Solo Practice Quest

You are given four slow queries and their EXPLAIN output. For each: (1) identify the primary cause of slowness from the execution plan; (2) rewrite the query to address the root cause; (3) explain which index would help most if one does not already exist. The four queries are: (a) a report joining orders to customers filtered by LOWER(email), (b) a correlated subquery that finds each product's average rating, (c) a dashboard query using SELECT * across five joined tables, (d) a monthly revenue query using EXTRACT(MONTH FROM order_date) in WHERE. After rewriting all four, write a brief "execution plan reading checklist" — the five things you look for first when diagnosing a slow query.

# Integration

**Mathematics**: Query optimisation is applied combinatorial optimisation. The query planner's job is to find the lowest-cost execution plan from an exponentially large space of alternatives: n tables can be joined in n! orders; each join can use nested loop, hash join, or merge join; each table access can use a sequential scan or one of many available indexes. Database optimisers use dynamic programming (System R's approach) or genetic algorithms to explore this space. Cost estimates are based on statistics (row counts, value distributions via histograms) and estimated I/O. The optimiser minimises estimated total cost — the same mathematical objective as the Travelling Salesman Problem for small numbers of tables (and similarly NP-hard for large n, which is why optimisers use heuristics for queries with many tables).

**Sciences (Computer Science — Algorithm Complexity)**: Index scans and sequential scans correspond directly to O(log n) and O(n) search algorithms. A B-tree index lookup (Index Scan) is O(log n) — the database descends the B-tree to find matching rows. A sequential scan is O(n) — every row must be examined. For a 1,000,000-row table, log₂(1,000,000) ≈ 20 — an index scan examines approximately 20 nodes to find a single row, compared to 1,000,000 for a sequential scan. This is the computational complexity rationale behind why indexes matter and why function application on indexed columns is so costly — it converts an O(log n) lookup into an O(n) scan, defeating the purpose of the index entirely.

# Lore Conclusion

"All four slow reports now run under two seconds," the Junior Engineer reported. "The correlated subquery rewrite alone saved 35 seconds — it ran 2.3 million times." The Senior Archivist reviewed the execution plans. "Seq Scan gone. Index Scan everywhere." She closed the results. "Query optimisation is the difference between a system that works in testing and one that survives production. Data volumes grow; slow queries become outages." She handed the Junior a new task. "You have completed Module 1: Advanced SQL — subqueries, CTEs, window functions, set operations, and optimisation. The next module introduces database programming: stored procedures, functions, triggers, and views — server-side logic that lives in the database itself."

---
