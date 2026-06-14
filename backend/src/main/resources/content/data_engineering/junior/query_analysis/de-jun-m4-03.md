---
id: de-jun-m4-03
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m4
moduleTitle: "Module 4: Indexing & Performance"
moduleGlyph: "📊"
moduleSortOrder: 4
topicSlug: query_analysis
topicTitle: "Query Analysis"
topicSortOrder: 3
lesson: query_analysis
title: "Query Analysis"
sortOrder: 3
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m4-02]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Identifies the slow query log and what it captures
    - Explains how to find the most expensive queries system-wide using pg_stat_statements or equivalent
    - Describes the N+1 query problem and its database impact
    - Identifies SELECT * as a systemic query quality issue
    - Explains how query normalisation helps group similar queries for analysis
  keywords: [slow query log, pg_stat_statements, N+1, SELECT *, query normalisation, total time, calls, mean time, sys-wide, performance monitoring, hotspot]
  modelAnswer: |
    Query analysis identifies performance problems at the system level, not just individual queries. The slow query log captures queries exceeding a time threshold. pg_stat_statements (PostgreSQL) records execution statistics for every unique query pattern: total time, call count, mean time, rows returned. The N+1 problem: an application makes 1 query to get N items, then N more queries to get details for each — instead of one JOIN. SELECT * returns unnecessary columns, wastes network bandwidth, prevents covering indexes, and breaks when schemas change. Query normalisation replaces literal values with placeholders to group identical queries with different parameters — revealing which query pattern is the system-wide hotspot.
guidedSteps:
  - id: de-jun-m4-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      An ORM generates: SELECT * FROM members WHERE id = 1, then SELECT * FROM loans WHERE member_id = 1, then SELECT * FROM loans WHERE member_id = 2, etc. — one query per member. This is called the:
    inputConfig:
      options:
        - "Lazy loading — acceptable ORM optimisation"
        - "N+1 problem — 1 query returns N members, then N more queries fetch their loans, totalling N+1 queries"
        - "Cartesian product — the ORM is joining without a condition"
        - "Over-fetching — too many columns selected"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["N+1 problem — 1 query returns N members, then N more queries fetch their loans, totalling N+1 queries"]
      rejectedFeedback: "The N+1 problem occurs when the application makes 1 query to fetch a list of N items, then executes N additional queries to fetch related data for each item. For 1,000 members: 1 (get members) + 1,000 (get each member's loans) = 1,001 queries, each with network round-trip overhead. The fix: use an eager loading JOIN (or ORM's equivalent: .include(), .eager_load(), .prefetch_related()) to fetch members and their loans in one query. N+1 is the most common ORM performance anti-pattern and can turn a 50ms page load into a 10+ second one on large datasets."
    hint: "1 initial query + N follow-up queries = N+1 queries total."
    reflectionPrompt: "Write the SQL that fixes the N+1 problem — joining members and loans in one query."
  - id: de-jun-m4-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The PostgreSQL extension that records execution statistics (total time, call count, mean time) for every distinct query pattern across the whole database is called pg_stat_______. 
    inputConfig:
      placeholder: "statements"
    markingRule:
      matchMode: CONTAINS
      accepted: [statements, "pg_stat_statements", "stat_statements"]
      rejectedFeedback: "pg_stat_statements is a PostgreSQL extension that tracks execution statistics for all SQL statements. It records: query text (normalised — literals replaced with $1, $2 placeholders), total execution time, number of calls, mean time, standard deviation, rows returned, shared/local buffer hits and misses. Querying pg_stat_statements shows which query patterns consume the most total database time — the system-wide hotspots. Sort by total_exec_time to find the highest-impact queries for optimisation. Enable with: CREATE EXTENSION pg_stat_statements; in postgresql.conf: shared_preload_libraries = 'pg_stat_statements'."
    hint: "The pg_stat_??? view in PostgreSQL for tracking query execution statistics system-wide."
    reflectionPrompt: "What is the difference between a query with high total_time vs one with high mean_time in pg_stat_statements?"
  - id: de-jun-m4-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why SELECT * is problematic from a systemic query analysis perspective, beyond just performance.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [schema, change, column, add, remove, break, application, network, maintenance, cover, unexpected, fragile]
      rejectedFeedback: "SELECT * is problematic beyond performance: it creates implicit coupling between queries and schema — adding a column to a table immediately returns that column to all SELECT * queries, which may break applications that do not expect the new column (e.g. column position dependencies in legacy code). Removing a column from a table breaks all SELECT * queries that reference the removed column by position. It prevents covering index optimisation (the query might need only 2 columns but fetches all 20). In analysis, SELECT * queries are harder to analyse because you cannot tell from the query text which columns the application actually uses."
    hint: "How does SELECT * couple the query to the table's schema? What happens when the schema changes?"
    reflectionPrompt: "How would you detect all SELECT * queries in a production database's query log?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "pg_stat_statements shows a query with: total_exec_time=3600s, calls=1,000,000, mean_exec_time=3.6ms. What does this tell you?"
    options:
      - "This query is very slow (3.6ms per call)"
      - "This query is called very frequently and contributes 3,600 seconds of total database time — a high-impact optimisation target"
      - "This query should be rewritten because it has high call count"
      - "The 3.6ms mean time is below the slow query threshold — ignore it"
    correctIndex: 1
    feedback: "3.6ms per call sounds fast and would never appear in a slow query log (typically threshold 1-5 seconds). But 1,000,000 calls × 3.6ms = 3,600 seconds (1 hour) of total database time. This query consumes a full hour of CPU every time statistics are collected. Even a 1ms improvement saves 1,000 seconds total — high-impact for a 'fast' query. This is why pg_stat_statements total_exec_time is more valuable than individual slow query logs for system-wide optimisation. Frequent, medium-speed queries often cause more total load than rare, very slow queries."
  - type: MULTIPLE_CHOICE
    question: "Query normalisation replaces literal values with placeholders to group queries for analysis. Which are the same normalised query?"
    options:
      - "SELECT * FROM orders WHERE id = 1 and SELECT name FROM orders WHERE id = 1"
      - "SELECT status FROM orders WHERE id = 1 and SELECT status FROM orders WHERE id = 9999"
      - "SELECT status FROM orders WHERE id = 1 and SELECT status FROM members WHERE id = 1"
      - "SELECT status FROM orders WHERE status = 'pending' and SELECT status FROM orders WHERE id = 1"
    correctIndex: 1
    feedback: "Normalisation replaces literal values (1, 9999) with placeholders ($1 or ?), so SELECT status FROM orders WHERE id = 1 and SELECT status FROM orders WHERE id = 9999 both normalise to SELECT status FROM orders WHERE id = $1 — the same pattern. This allows pg_stat_statements to group all executions of this pattern (regardless of the specific id value) and sum their total execution time. The other options change table names, column names, or the WHERE clause structure — different query patterns after normalisation."
retrieval:
  recall: "Write the pg_stat_statements query to find the top 10 most time-consuming query patterns in a PostgreSQL database."
  explain: "Explain the N+1 problem, why ORMs commonly produce it, and how to detect it from a database query log or APM tool."
  mistakeId:
    code: "-- ORM code (pseudocode):\nfor member in db.query('SELECT * FROM members WHERE status = 'active'):\n    member.loans = db.query('SELECT * FROM loans WHERE member_id = ?', member.id)\n    print(f'{member.name}: {len(member.loans)} loans')"
    answer: "This is the N+1 problem: 1 query gets all active members, then 1 query per member gets their loans. For 10,000 active members: 10,001 queries. Each has a network round trip — on a 1ms latency connection, minimum 10 seconds just in network overhead. Fix: use a JOIN to fetch both in one query: SELECT m.name, COUNT(l.loan_id) AS loan_count FROM members m LEFT JOIN loans l ON m.member_id = l.member_id WHERE m.status = 'active' GROUP BY m.member_id, m.name. This is a single query regardless of how many members exist. Most ORMs support eager loading: members.where(status: 'active').includes(:loans) produces a 2-query version (all members, then all their loans in one batch query) — much better than N+1."
---

# Hook

Individual query optimisation fixes one slow query. Query analysis identifies patterns — which queries are the most expensive system-wide, where are the architectural problems (N+1, SELECT *), and what are the trends over time. Production database performance requires both: fixing individual bottlenecks and monitoring patterns across all queries.

# Lore Introduction

"The Archive system slows down every morning at 9am," the Junior Engineer reported. "But no individual query takes more than 300ms." The Senior Archivist looked at the pg_stat_statements summary. "Not one slow query. A thousand medium-speed queries." She sorted by total_exec_time. "This pattern — SELECT * FROM loans WHERE member_id = ? — executed 45,000 times in one hour. Three hundred milliseconds each × 45,000 = 3.7 hours of database time." The Junior stared. "From a query that's technically fast." The Senior Archivist nodded. "That's N+1. The application fetches members, then for each member it fetches their loans. Individually fast. Collectively catastrophic. One JOIN fixes it — 45,000 queries → one."

# Core Learning

## Concept Introduction

### System-Wide Query Analysis

```sql
-- PostgreSQL: pg_stat_statements — find hotspots
-- Enable first: CREATE EXTENSION pg_stat_statements;
-- Add to postgresql.conf: shared_preload_libraries = 'pg_stat_statements'

-- Top 10 queries by total execution time
SELECT
    LEFT(query, 100)                     AS query_prefix,
    calls,
    ROUND(total_exec_time::NUMERIC, 2)   AS total_ms,
    ROUND(mean_exec_time::NUMERIC, 2)    AS mean_ms,
    ROUND(stddev_exec_time::NUMERIC, 2)  AS stddev_ms,
    rows
FROM pg_stat_statements
ORDER BY total_exec_time DESC
LIMIT 10;

-- High call count queries (frequent, possibly N+1)
SELECT query, calls, ROUND(mean_exec_time::NUMERIC, 3) AS mean_ms
FROM pg_stat_statements
WHERE calls > 10000
ORDER BY calls DESC;

-- Reset statistics (before load test, or periodically)
SELECT pg_stat_statements_reset();
```

### The Slow Query Log

```sql
-- PostgreSQL: log queries exceeding threshold
-- In postgresql.conf:
-- log_min_duration_statement = 1000   -- log queries > 1 second
-- log_min_duration_statement = 0      -- log ALL queries (use with caution)

-- MySQL: slow query log
-- SET GLOBAL slow_query_log = ON;
-- SET GLOBAL long_query_time = 1;  -- threshold in seconds

-- MySQL: analyse slow query log
-- mysqldumpslow -s t -t 10 /var/log/mysql/slow.log
-- → top 10 slowest queries with counts and times
```

### The N+1 Query Problem

```sql
-- BAD: application code generates N+1 queries
-- 1 query for members + 1 per member for their loans
-- For 10,000 members: 10,001 queries

-- Detected in pg_stat_statements:
-- SELECT * FROM loans WHERE member_id = $1    calls=10,000   mean=1.5ms   total=15s
-- SELECT * FROM members WHERE status = $1     calls=1        mean=50ms    total=50ms

-- FIX: single JOIN query
SELECT
    m.member_id,
    m.name,
    m.status,
    COUNT(l.loan_id)                                          AS total_loans,
    SUM(CASE WHEN l.return_date IS NULL THEN 1 ELSE 0 END)   AS active_loans
FROM members m
LEFT JOIN loans l ON m.member_id = l.member_id
WHERE m.status = 'active'
GROUP BY m.member_id, m.name, m.status;
-- 1 query regardless of member count
```

### SELECT * as a Systemic Issue

```sql
-- Problems with SELECT *:
-- 1. Fetches all columns — wastes bandwidth for columns never used
-- 2. Prevents covering index optimisation
-- 3. Couples query to schema — adding/removing columns changes output
-- 4. Hides which columns the application actually depends on
-- 5. Large columns (TEXT, BLOB) transferred even when not needed

-- Detection: find SELECT * in pg_stat_statements
SELECT query, calls, total_exec_time
FROM pg_stat_statements
WHERE query LIKE 'SELECT *%' OR query ILIKE 'select *%'
ORDER BY total_exec_time DESC;

-- Fix: always list specific columns
-- Before: SELECT * FROM members WHERE status = 'active'
-- After:  SELECT member_id, name, email, tier FROM members WHERE status = 'active'
```

### Query Normalisation

```sql
-- pg_stat_statements automatically normalises literals to $1, $2...
-- SELECT * FROM orders WHERE id = 1   → SELECT * FROM orders WHERE id = $1
-- SELECT * FROM orders WHERE id = 999 → SELECT * FROM orders WHERE id = $1
-- Both executions are counted under the same normalised query

-- Manual normalisation (for tools that don't do it automatically):
-- Replace all integer literals with ?
-- Replace all string literals with ?
-- Replace all float literals with ?
-- Group and aggregate by normalised query

-- This reveals: "this query pattern was called 50,000 times today"
-- even though each individual call had a different parameter value
```

### Monitoring Dashboard Metrics

```sql
-- Connections by state (too many idle-in-transaction is a concern)
SELECT state, count(*)
FROM pg_stat_activity
GROUP BY state
ORDER BY count(*) DESC;

-- Table read patterns (sequential scan rate)
SELECT relname AS table,
       seq_scan,
       idx_scan,
       ROUND(seq_scan * 100.0 / NULLIF(seq_scan + idx_scan, 0), 1) AS seq_pct
FROM pg_stat_user_tables
WHERE seq_scan + idx_scan > 0
ORDER BY seq_pct DESC;
-- Tables with high seq_pct and high seq_scan are candidates for new indexes

-- Index usage
SELECT indexrelname, idx_scan, idx_tup_read
FROM pg_stat_user_indexes
WHERE idx_scan = 0   -- indexes never used — candidates for removal
  AND schemaname = 'public';
```

## Why It Matters

Finding the slow query is half of every performance investigation — and production systems generate too much SQL to guess:

- Slow-query logs and views like `pg_stat_statements` rank queries by total cost, pointing you at the 3 queries causing 90% of load
- The worst offender is often not the slowest query but a fast one executed thousands of times per minute
- Systematic analysis prevents the classic anti-pattern: optimising the query you suspect instead of the one that's actually burning the CPU

Measurement before optimisation isn't bureaucracy — it's how you avoid spending a week speeding up a query that runs once a day.

## Common Mistakes

- **Fixing only slow query log entries**: Queries under the slow query threshold collectively cause more load than the handful of truly slow queries. Analyse total_exec_time × calls.
- **Not looking for N+1**: ORM applications almost always have some N+1 queries. Look for a query pattern with very high call count and suspiciously low mean time — that's N+1.
- **Analysing queries in isolation**: Context matters. A query that runs fine with 10,000 rows may be a problem with 10,000,000. Analyse under realistic data volumes.
- **Never resetting statistics**: pg_stat_statements accumulates forever — statistics from 3 months ago distort current analysis. Reset before load tests; schedule periodic resets.

## Mental Model

Individual query optimisation is like finding the one slow worker on an assembly line and helping them go faster. System-wide query analysis is like studying the entire production floor — which stations are bottlenecks, which workers repeat the same task 10,000 times (N+1), which movements are unnecessary (SELECT *). You need both views: zoom in on the individual (EXPLAIN ANALYZE) and zoom out on the system (pg_stat_statements). The most impactful database performance work often comes from the zoom-out view — finding architectural problems that affect thousands of queries simultaneously.

## Mini Summary

- ✔ pg_stat_statements finds system-wide hotspots by total execution time, not just slow queries
- ✔ Slow query log catches individual slow queries; pg_stat_statements finds frequent medium-speed queries
- ✔ N+1: 1 query + N per-row queries — fix with JOIN or batch loading
- ✔ SELECT *: wastes bandwidth, prevents covering indexes, creates schema coupling
- ✔ Query normalisation groups query patterns — essential for understanding true query frequency

# Guided Practice Quest

Work through the guided steps to interpret a pg_stat_statements output and identify the N+1 pattern, rewrite the N+1 as a single JOIN query, and write a monitoring query that identifies tables with high sequential scan rates.

# Solo Practice Quest

You are given a pg_stat_statements dump from the Archive system during a 1-hour peak period. The data shows: query A (member lookup by id) called 95,000 times, mean 2ms, total 190s; query B (loan insert) called 8,000 times, mean 5ms, total 40s; query C (overdue report) called 3 times, mean 45s, total 135s; query D (catalogue search) called 12,000 times, mean 1ms, total 12s; query E (SELECT * FROM members WHERE ...) called 45,000 times, mean 3ms, total 135s. (1) Rank these queries by total time; (2) identify which is likely an N+1 pattern and explain why; (3) identify the SELECT * concern and its fix; (4) identify which single query fix would have the highest system impact and implement it; (5) write a monitoring query you would run daily to track the health of this system.

# Integration

**Mathematics**: Query analysis is an application of Pareto analysis (the 80/20 rule) in performance engineering. In most database systems, 80% of database load is caused by 20% of query patterns — or more extremely, 5 queries cause 95% of load. This is Zipf's law distribution of query frequencies: a few patterns are called exponentially more often than the rest. Total impact = calls × mean_time — this is the area under a frequency-time product distribution. Sorting by total_time implements a first-order Pareto prioritisation. The N+1 problem is a specific instance of an O(n) algorithm where O(1) suffices: n queries each O(1) = O(n) total; 1 JOIN query O(1) = O(1) total. Reducing O(n) to O(1) is always the highest-priority optimisation (better than constant-factor improvements).

**Sciences (Systems Biology — Metabolic Flux Analysis)**: System-wide query analysis mirrors metabolic flux analysis in systems biology. Individual enzymatic reactions (queries) may be fast; the bottleneck is the pathway (query pattern) that processes the highest total flux (calls × time). Metabolic control analysis identifies rate-limiting steps in biochemical pathways by measuring flux distribution — exactly the pg_stat_statements approach applied to metabolic networks. In both systems, optimising a low-flux pathway has negligible impact; optimising the high-flux bottleneck has maximum impact. The N+1 problem corresponds to futile metabolic cycles — energy-consuming cycles that accomplish no net work, identifiable by unusually high flux through a pair of reactions that cancel each other out.

# Lore Conclusion

"The N+1 pattern is fixed," the Junior Engineer reported. "One JOIN replacing 45,000 queries. The Archive's morning load dropped by 40%." The Senior Archivist reviewed the new pg_stat_statements output. "Query A still calls 95,000 times — that's the member login check, legitimate frequency. Query C — the overdue report — is the next target: 45 seconds, runs three times daily." She set the monitor down. "You now think in systems, not just in single queries. The slow query log is for finding the occasional disaster. pg_stat_statements is for finding the architectural inefficiency that costs the database an hour of work every hour." She handed the next file. "Last lesson in this module: optimisation techniques — the toolkit for fixing the problems you've learned to find."

---
