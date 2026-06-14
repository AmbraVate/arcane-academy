---
id: de-jun-m4-02
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m4
moduleTitle: "Module 4: Indexing & Performance"
moduleGlyph: "📊"
moduleSortOrder: 4
topicSlug: execution_plans
topicTitle: "Execution Plans"
topicSortOrder: 2
lesson: execution_plans
title: "Execution Plans"
sortOrder: 2
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m4-01]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what an execution plan is and how to obtain one
    - Identifies Seq Scan, Index Scan, and Index Only Scan in a plan
    - Explains the difference between EXPLAIN (estimated) and EXPLAIN ANALYZE (actual)
    - Reads cost estimates and row count estimates from a plan node
    - Describes common join methods (Hash Join, Nested Loop, Merge Join) and when each is used
  keywords: [EXPLAIN, EXPLAIN ANALYZE, Seq Scan, Index Scan, Hash Join, Nested Loop, Merge Join, cost, rows, actual time, execution plan, node, operator, statistics]
  modelAnswer: |
    An execution plan shows how the database will (EXPLAIN) or did (EXPLAIN ANALYZE) execute a query. Key nodes: Seq Scan (full table read — potentially slow), Index Scan (B-tree lookup), Index Only Scan (covered by index, no table fetch). Cost estimates: startup..total cost in planner units. Row estimates: rows the planner expects. EXPLAIN ANALYZE adds actual execution time and actual row counts — mismatches between estimated and actual rows indicate stale statistics. Join methods: Hash Join (build hash table from smaller input, probe with larger — good for large unsorted sets), Nested Loop (for each outer row, look up inner — good when inner is indexed and outer is small), Merge Join (merge two sorted inputs — good when both sides are already sorted/indexed).
guidedSteps:
  - id: de-jun-m4-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In an execution plan, you see: "Seq Scan on orders (cost=0..45000 rows=1000000 width=32) (actual time=0.1..8200ms rows=1000000)". What should you investigate?
    inputConfig:
      options:
        - "The width=32 is too large for this query"
        - "A sequential scan on 1M rows is slow — check if the WHERE clause column is indexed"
        - "The startup cost of 0 indicates the plan is optimal"
        - "The cost estimate of 45000 is within acceptable range"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A sequential scan on 1M rows is slow — check if the WHERE clause column is indexed"]
      rejectedFeedback: "Seq Scan on 1,000,000 rows taking 8.2 seconds is the primary concern. A sequential scan reads every row, which is unavoidable if: there is no index on the filter column, the filter column is wrapped in a function (non-sargable), or the planner estimated it would be cheaper (low selectivity). Steps: check if the WHERE clause column has an index; if not, create one; if an index exists, check if the predicate is sargable (no function on the column). Run EXPLAIN ANALYZE after adding the index to verify the plan changed to Index Scan."
    hint: "Reading 1,000,000 rows taking 8.2 seconds — what type of access is Seq Scan?"
    reflectionPrompt: "What would the execution plan look like after adding an index on the filter column?"
  - id: de-jun-m4-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The difference between EXPLAIN and EXPLAIN ________ is that the latter actually runs the query and shows real execution times and row counts.
    inputConfig:
      placeholder: "ANALYZE"
    markingRule:
      matchMode: CONTAINS
      accepted: [ANALYZE, analyze, ANALYSE, analyse]
      rejectedFeedback: "EXPLAIN shows the query plan the optimiser chose with estimated costs and row counts — without executing the query. EXPLAIN ANALYZE actually runs the query and adds actual time and actual row count to each node. EXPLAIN ANALYZE is essential for diagnosing real performance issues because: (1) the planner's row estimates may be wrong (stale statistics), leading to poor plan choices; (2) estimated costs are abstract planner units — actual time in milliseconds is more actionable; (3) a node with estimated rows=100 but actual rows=100,000 indicates why the planner made a bad choice (it underestimated selectivity)."
    hint: "The EXPLAIN variant that actually executes the query and measures real timings."
    reflectionPrompt: "When would you use EXPLAIN instead of EXPLAIN ANALYZE? (Hint: consider very expensive queries.)"
  - id: de-jun-m4-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why a Hash Join is typically faster than a Nested Loop when joining two large unsorted tables.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [hash table, build, probe, O(n), O(n log n), inner, outer, linear, lookup, unsorted, large]
      rejectedFeedback: "A Hash Join builds a hash table from the smaller of the two inputs (O(n)), then probes it with each row from the larger input (O(m)) — total O(n+m). A Nested Loop without an index does O(n×m) work: for each of the n outer rows, it scans all m inner rows. For large unsorted tables (n=100,000, m=1,000,000), Nested Loop does 100 billion comparisons; Hash Join does roughly 1.1 million operations — orders of magnitude faster. Nested Loop is faster when the inner table is indexed (making each inner lookup O(log m)) and the outer table is small."
    hint: "Compare the algorithmic complexity: Hash Join O(n+m) vs Nested Loop O(n×m)."
    reflectionPrompt: "When would a Nested Loop outperform a Hash Join?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "An EXPLAIN ANALYZE plan shows estimated rows=10 but actual rows=100,000. What does this indicate?"
    options:
      - "The query is incorrect and returns too many rows"
      - "The planner's statistics are stale — ANALYZE (or VACUUM ANALYZE) should be run to update them"
      - "The Index Scan is working correctly — rows estimates are always low"
      - "The database ran out of memory during execution"
    correctIndex: 1
    feedback: "A large discrepancy between estimated rows and actual rows means the planner's statistics are stale or inaccurate. Statistics are updated by ANALYZE (PostgreSQL) or automatic statistics collection (SQL Server, MySQL). When statistics are stale, the planner underestimates or overestimates rows, choosing suboptimal plans — it might choose a Nested Loop expecting 10 rows but actually processing 100,000. After ANALYZE, statistics are updated and the planner re-evaluates the plan. For heavily written tables, increase the statistics target or schedule more frequent ANALYZE runs."
  - type: MULTIPLE_CHOICE
    question: "A Merge Join appears in the execution plan for a JOIN on two tables. What does this imply about the inputs?"
    options:
      - "Both inputs are small and fit in memory"
      - "Both inputs are sorted (or sortable with an existing index) on the join column"
      - "One input has an index and the other does not"
      - "The database ran out of hash memory and fell back to merge"
    correctIndex: 1
    feedback: "Merge Join works by merging two already-sorted inputs — like merging two sorted lists. The database reads both inputs in sorted order on the join column and emits matching pairs. This is O(n+m) if both inputs are already sorted (e.g. they were sorted by an earlier Sort node or an existing index). If sorting is required, the Sort node appears before the Merge Join, adding O(n log n + m log m) cost. Merge Join is optimal when both inputs come pre-sorted from indexes. It requires no extra memory (unlike Hash Join which builds a hash table in memory)."
retrieval:
  recall: "Describe the steps to diagnose a slow query using EXPLAIN ANALYZE — what you look for, in what order, and how you interpret the output."
  explain: "Explain the three main join algorithms (Hash Join, Nested Loop, Merge Join), their algorithmic complexity, and the conditions under which each is optimal."
  mistakeId:
    code: "EXPLAIN SELECT * FROM orders o JOIN customers c ON o.customer_id = c.customer_id WHERE o.status = 'pending';"
    answer: "Using EXPLAIN without ANALYZE only shows estimated values — helpful for planning but not for diagnosing actual performance. For diagnosis: use EXPLAIN ANALYZE to see actual execution times and row counts per node. Also: (1) check if Seq Scan appears on large tables; (2) compare estimated rows vs actual rows — a large mismatch means stale statistics (run ANALYZE); (3) check if the status filter and customer_id join columns are indexed; (4) note the join method — is Hash Join appropriate given the table sizes? The plan will show estimated but not actual timings without ANALYZE. For production diagnosis, always use EXPLAIN (ANALYZE, BUFFERS) to see memory and I/O usage as well."
---

# Hook

A slow query has a cause. The database can show you exactly what it is doing — the execution plan. Reading execution plans is the primary diagnostic skill for database performance work. Without it, optimisation is guesswork. With it, the bottleneck is visible and the fix is clear.

# Lore Introduction

"The overdue report is slow again," the Junior Engineer said. "I added three indexes last week." The Senior Archivist pulled up the query. "Run EXPLAIN ANALYZE first. Show me what the database is actually doing, not what you think it's doing." The plan appeared. Hash Join, Seq Scan on members (50,000 rows), Index Scan on loans. "The members table has no index on region — the WHERE clause is filtering it with a sequential scan. The index you added was on loans, not on the path the database takes." She pointed at the estimated rows vs actual rows. "Also: the planner estimated 500 members but found 15,000. Statistics are stale. ANALYZE first, re-run EXPLAIN ANALYZE, then decide which index to add."

# Core Learning

## Concept Introduction

### Obtaining Execution Plans

```sql
-- EXPLAIN: show estimated plan (does not execute)
EXPLAIN SELECT * FROM orders WHERE status = 'pending';

-- EXPLAIN ANALYZE: execute and show actual timings (executes the query!)
EXPLAIN ANALYZE SELECT * FROM orders WHERE status = 'pending';

-- EXPLAIN with all details (PostgreSQL)
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT o.order_id, c.name, o.total_amount
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id
WHERE o.status = 'completed'
  AND o.order_date >= '2024-01-01';

-- SQL Server equivalent
SET STATISTICS IO ON;
SET STATISTICS TIME ON;
SELECT * FROM orders WHERE status = 'pending';

-- MySQL equivalent
EXPLAIN FORMAT=JSON SELECT ...;
```

### Reading Key Plan Nodes

```
Seq Scan on orders (cost=0.00..45000.00 rows=1000000 width=50)
                         ↑startup  ↑total   ↑estimated ↑row size
                    cost in planner units (not milliseconds)

(actual time=0.100..8200.000 rows=998723 loops=1)
               ↑first row  ↑last row  ↑actual  ↑times this node ran
```

| Node | Meaning |
|---|---|
| Seq Scan | Full table read — all rows |
| Index Scan | B-tree lookup, then fetch row from table |
| Index Only Scan | B-tree lookup — row not needed (covered) |
| Bitmap Index Scan | Multiple index scans combined |
| Hash Join | Build hash table from one side, probe with other |
| Nested Loop | For each outer row, look up inner |
| Merge Join | Merge two sorted inputs |
| Sort | Explicit sort step (pre-sorted inputs avoid this) |
| Hash Aggregate | GROUP BY using hash table |
| Limit | Stop after N rows |

### Interpreting a Multi-Node Plan

```
EXPLAIN ANALYZE output (read bottom-up — inner nodes execute first):

Hash Join (cost=5000..15000 rows=45000) (actual time=180ms..2400ms rows=43271)
  Hash Cond: (o.customer_id = c.customer_id)
  ->  Seq Scan on orders o (cost=0..8000 rows=200000) (actual time=0..1200ms rows=199831)
        Filter: (status = 'completed')
        Rows Removed by Filter: 850000      ← most rows filtered out here
  ->  Hash (cost=2000..2000 rows=100000) (actual time=100ms..100ms rows=99800)
        ->  Index Scan on customers c (cost=0..2000 rows=100000)
              Index Cond: (region = 'North')

Problems visible:
1. Seq Scan on orders: 1,000,000 rows scanned, 850,000 filtered out
   → Add index on orders.status (high selectivity filter)
2. Estimated rows for orders: 200,000 → actual: 199,831 (good estimate)
3. customers uses Index Scan on region — good
```

### Diagnosing Row Estimate Mismatch

```sql
-- Large mismatch: planner chose suboptimal plan
-- Estimated: 500 rows | Actual: 50,000 rows

-- Fix: update statistics
ANALYZE orders;          -- PostgreSQL
-- or
ANALYZE TABLE orders;    -- MySQL
-- or
UPDATE STATISTICS orders; -- SQL Server

-- Re-run EXPLAIN ANALYZE to verify estimates improved
-- If still mismatched: increase statistics target (PostgreSQL)
ALTER TABLE orders ALTER COLUMN status SET STATISTICS 500;
ANALYZE orders;
```

### Join Method Selection

```sql
-- Hash Join: optimal for large unsorted tables
-- → builds hash table from smaller table in memory
-- → probes hash table with each row from larger table
-- → O(n + m) time, O(n) memory (n = smaller table)

-- Nested Loop: optimal when inner table is indexed and outer is small
-- → for each outer row, perform one index lookup on inner
-- → O(n × log m) with index, O(n × m) without
-- → best when n is very small (< 1000 rows)

-- Merge Join: optimal when both inputs are already sorted
-- → merge two sorted streams — no memory buffer needed beyond one row each
-- → O(n + m) if inputs are pre-sorted, O(n log n + m log m) if Sort needed

-- Force a specific join type (PostgreSQL — for testing):
SET enable_hashjoin = off;
SET enable_nestloop = off;
-- → forces Merge Join for comparison
```

## Why It Matters

The execution plan is the database telling you exactly how it will run your query — reading one is the difference between guessing and knowing:

- A query that's slow in production but fine in dev usually has different plans; only the plan reveals why
- Spotting a sequential scan where you expected an index seek diagnoses the problem in seconds
- Estimated vs actual row counts expose stale statistics — a silent killer of query performance

Engineers who can read plans fix performance issues; engineers who can't add indexes at random and hope. EXPLAIN is the single highest-value debugging skill in this module.

## Common Mistakes

- **Using EXPLAIN on a destructive query**: EXPLAIN ANALYZE actually executes the query. Running EXPLAIN ANALYZE on a DELETE or UPDATE will delete or update real data (in the same transaction — roll it back if needed, or use BEGIN/ROLLBACK around it).
- **Ignoring the row estimate vs actual mismatch**: A plan chosen based on wrong row estimates is the most common source of unexpected slow queries. Always check for large discrepancies.
- **Reading plans top-down**: Execution plans execute bottom-up (innermost/lowest nodes first). Read the plan from the bottom to understand the execution order.
- **Fixing the wrong node**: The most time-consuming node (highest actual time in EXPLAIN ANALYZE) is the one to fix. Don't optimise a 5ms node when there's a 5-second node elsewhere in the plan.

## Mental Model

An execution plan is a recipe card showing how the chef will prepare a dish. EXPLAIN shows the recipe before cooking; EXPLAIN ANALYZE shows the recipe plus notes on how long each step actually took. If the recipe says "peel 5 potatoes" but you actually needed 50 potatoes, the recipe underestimated the work (stale statistics). The chef chose the wrong cooking method because of that wrong estimate. Reading the plan tells you which step in the recipe is taking the most time — that's where to focus optimisation.

## Mini Summary

- ✔ EXPLAIN: estimated plan; EXPLAIN ANALYZE: actual timings (executes the query)
- ✔ Seq Scan: slow on large tables; Index Scan: fast; Index Only Scan: fastest
- ✔ Compare estimated rows vs actual rows — large mismatch means stale statistics (run ANALYZE)
- ✔ Hash Join: large unsorted tables; Nested Loop: small outer + indexed inner; Merge Join: pre-sorted inputs
- ✔ Fix the highest-cost node first — don't optimise the wrong part of the plan

# Guided Practice Quest

Work through the guided steps to read a provided EXPLAIN ANALYZE output and identify: the most expensive node, the join method used and whether it is appropriate, and the row estimate mismatch that is causing a suboptimal plan.

# Solo Practice Quest

For the Archive system, investigate the following slow queries using simulated EXPLAIN ANALYZE output provided in the exercise: (1) the monthly overdue report (multi-table join, date range filter); (2) the member search by partial name (LIKE '%name%' pattern); (3) the inventory availability check (subquery, multiple joins); (4) the top-borrowed items report (GROUP BY with ORDER BY and LIMIT). For each query: identify the bottleneck node in the plan, explain why it is slow (wrong join method, missing index, stale statistics, non-sargable predicate), and write the fix. Then write a "plan reading checklist" — five things you check in every EXPLAIN ANALYZE output, in priority order.

# Integration

**Mathematics**: Execution plans are the output of a query optimiser that solves a combinatorial optimisation problem. The optimiser estimates the cost of each possible plan using statistics about data distributions (histograms, most-common-values, n-distinct). The cost model is based on expected I/O: cost = pages_read × random_io_cost + tuples_processed × cpu_tuple_cost. The optimiser searches the plan space using dynamic programming (System R algorithm): for n relations, it considers all subsets and builds up optimal plans from optimal sub-plans. For n ≥ 8 tables, the search space becomes intractable (n! orderings), so heuristics (greedy join ordering, genetic algorithms) are used. EXPLAIN exposes the plan chosen; EXPLAIN ANALYZE exposes the runtime data that would allow the optimiser to choose better if it had been available as statistics.

**Sciences (Operations Research — Critical Path Method)**: Execution plans are structurally equivalent to project network diagrams in critical path analysis. Each plan node is a task; edges are dependencies (output of one node feeds the next). The EXPLAIN ANALYZE actual times reveal the critical path: the sequence of nodes that determines total query time. Like CPM in construction management, optimising the critical path (the longest-duration sequence) is the only way to reduce total time — optimising non-critical nodes has no effect on overall query runtime. The Hash Join feeding into a Sort feeding into an Aggregate is a critical path if that sequence accounts for most total time. Reading execution plans through the CPM lens immediately identifies which nodes to prioritise for optimisation.

# Lore Conclusion

"EXPLAIN ANALYZE revealed two issues," the Junior Engineer reported. "Missing index on members.region, and stale statistics — the planner estimated 500 members in the North region, actual was 15,000. After ANALYZE and adding the index: 150 milliseconds down from 8 seconds." The Senior Archivist reviewed the new plan. "Index Scan replacing Seq Scan. Estimated rows now match actual. Hash Join optimal for these table sizes." She closed the plan viewer. "You can now diagnose any slow query: read the plan, find the expensive node, identify the cause — wrong access method, stale statistics, non-sargable predicate, wrong join algorithm. Fix the cause, not the symptom." She handed the next file. "Query analysis: identifying patterns in slow queries across the whole system."

---
