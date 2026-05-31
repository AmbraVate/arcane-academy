---
id: se-sen-m6-04
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m6
moduleTitle: "Module 6: Performance Engineering"
moduleGlyph: "🚀"
moduleSortOrder: 6
topicSlug: database_optimisation
topicTitle: "Database Optimisation"
topicSortOrder: 4
lesson: database_optimisation
title: "Database Optimisation"
sortOrder: 4
difficulty: 4
estimatedMinutes: 32
xpReward: 65
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [cpu_bottlenecks]
integrationDomains: [mathematics, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Explains how to read a query execution plan (EXPLAIN) and identifies Seq Scan vs Index Scan as a key signal"
    - "Distinguishes B-tree, hash, and covering indexes and explains when each is appropriate"
    - "Defines the N+1 query problem, explains why ORMs commonly produce it, and describes two solutions"
    - "Explains HikariCP connection pooling parameters (maximumPoolSize, connectionTimeout) and why pool sizing matters"
    - "Describes the trade-offs of denormalisation and read replicas as read-scaling strategies"
  keywords: [EXPLAIN, Seq Scan, Index Scan, B-tree, covering index, N+1, eager loading, JOIN, HikariCP, connection pool, denormalisation, read replica, slow query log]
  modelAnswer: |
    Query execution plan analysis begins with EXPLAIN (PostgreSQL, MySQL). The output shows each step the database engine takes to satisfy the query: Seq Scan (full table scan — reads every row), Index Scan (uses an index to locate rows), Index Only Scan (satisfies query entirely from the index, avoids table access), Hash Join, Nested Loop, etc. EXPLAIN ANALYZE actually executes the query and shows real row counts and timings. The primary concern is any Seq Scan on a large table — it signals a missing or unused index. EXPLAIN's cost estimates are in arbitrary cost units; ANALYZE's timings are in milliseconds. Look for "rows=" discrepancies between estimated and actual row counts — large discrepancies indicate stale table statistics (run ANALYZE to refresh).

    Index types serve different access patterns. B-tree indexes are the default: sorted data structure supporting range queries, equality, and ORDER BY optimisation. Hash indexes support only equality lookups and are marginally faster for exact match but cannot support range queries. Covering indexes include all columns referenced by a query in the index itself, enabling Index Only Scan — the query is answered without accessing the table at all. For a query SELECT a, b WHERE c = ?, a covering index on (c, a, b) satisfies the query entirely from the index. Composite indexes must match the query's column order; the leftmost prefix rule applies.

    The N+1 problem occurs when code loads a list of N entities, then executes one query per entity to load a related association — resulting in N+1 database round trips. JPA/Hibernate produces N+1 by default with LAZY-loaded associations: fetching 100 Orders, then accessing each Order's OrderItems, triggers 100 additional queries. Solutions: (1) JOIN FETCH in JPQL explicitly eagerly loads the association in the same query; (2) @EntityGraph or @BatchSize controls loading strategy declaratively; (3) Native SQL with JOIN returns flat result set that is mapped in application code.

    HikariCP is the standard JDBC connection pool. Key parameters: maximumPoolSize (total connections to the database — should not exceed database's max_connections / number of application instances); minimumIdle (kept-alive idle connections); connectionTimeout (how long to wait for a connection before throwing — default 30s, usually too long for a web service, set to 5s); idleTimeout (when idle connections are returned). Pool sizing follows Little's Law: pool_size = (concurrency × query_time). Oversizing the pool wastes database connections; undersizing causes queueing and connection timeout errors.

    Denormalisation duplicates data to pre-join associations, improving read performance at the cost of write complexity and consistency. Suitable when reads vastly outnumber writes and joins are expensive (large tables, complex joins). Read replicas direct read traffic to replica databases, spreading read load. Replicas introduce replication lag — reads may see stale data. Applications must route writes to primary and accept eventual consistency on reads, or route consistency-sensitive reads to primary.
guidedSteps:
  - id: db-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      EXPLAIN ANALYZE shows a Seq Scan on a `payments` table (5 million rows) for the query:
      `SELECT * FROM payments WHERE user_id = 42 AND status = 'PENDING'`
      The query takes 3.2 seconds. What is the most likely fix?
    inputConfig:
      options:
        - "A. Increase the database's shared_buffers so the table fits in memory"
        - "B. Add a composite index on (user_id, status) to enable Index Scan"
        - "C. Denormalise by copying user payments into a separate table"
        - "D. Add a read replica to distribute the load"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["B"]
      rejectedFeedback: "A Seq Scan on a large table for a filtered query is a missing index problem. A composite index on (user_id, status) allows the database to locate matching rows via Index Scan instead of scanning all 5 million rows. Increasing shared_buffers helps cache but does not eliminate the scan. Read replicas spread load but do not fix the slow query on each replica."
    hint: "The query filters on two columns — what index structure matches this access pattern?"
    reflectionPrompt: "EXPLAIN is the first tool to reach for before any query optimisation. Read the plan before guessing."
  - id: db-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Loading 100 orders, then accessing each order's line items with a separate query per order, results in ___ total database queries.
    inputConfig:
      placeholder: "number"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["101", "100+1", "N+1"]
      rejectedFeedback: "1 query to load 100 orders + 100 queries to load each order's line items = 101 total queries. This is the N+1 problem. The fix is JOIN FETCH or @EntityGraph to load orders and line items in a single query with a JOIN."
    hint: "One initial query, then one per loaded entity."
    reflectionPrompt: "The N+1 problem is invisible in unit tests with 1 record but catastrophic in production with 10,000 records."
  - id: db-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A Spring Boot application uses Spring Data JPA. Loading a list of `Order` entities triggers 1 additional query per order to load `OrderItems`. Explain the root cause and show two specific approaches to fix it using JPA/Hibernate features.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [LAZY, JOIN FETCH, EntityGraph, BatchSize, eager, N+1, JPQL]
      rejectedFeedback: "Root cause: OrderItems association is LAZY-loaded by default. When application code accesses order.getOrderItems(), Hibernate fires a SELECT per order. Fix 1: JPQL JOIN FETCH: 'SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.userId = :userId'. Fix 2: @EntityGraph on the repository method declaring orderItems as an attributePath. Fix 3: @BatchSize(size=20) on the OrderItems collection so Hibernate batches the N queries into groups of 20."
    hint: "The root cause is JPA's default lazy-loading strategy — what alternatives does JPA provide for association loading?"
    reflectionPrompt: "ORM lazy loading is a convenience that becomes a performance trap at scale. Always verify association loading strategy in slow query logs."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is a covering index and what performance benefit does it provide?"
    options:
      - "A. An index that covers all rows in a table, replacing a full table scan"
      - "B. An index that includes all columns referenced by a query, allowing the database to answer the query from the index without accessing the table"
      - "C. An index that covers multiple tables in a JOIN operation"
      - "D. An index that automatically updates when data is inserted"
    correctIndex: 1
    feedback: "A covering index includes all columns needed by the query (SELECT columns, WHERE columns, ORDER BY columns). The database performs an Index Only Scan — it never accesses the main table. For high-frequency read queries, covering indexes eliminate the table access cost entirely."
  - type: MULTIPLE_CHOICE
    question: "HikariCP's maximumPoolSize is set to 200 connections to a PostgreSQL database. The database has max_connections = 100. What happens?"
    options:
      - "A. HikariCP silently reduces to 100 connections"
      - "B. PostgreSQL rejects connections beyond max_connections, causing connection errors in the application"
      - "C. The application queues excess requests until connections are available"
      - "D. PostgreSQL automatically increases max_connections to match"
    correctIndex: 1
    feedback: "PostgreSQL enforces max_connections strictly. If the pool tries to open more connections than the database allows, connection establishment fails. With multiple application instances each trying to hold 200 connections, the total can exceed max_connections, causing intermittent connection failures. Pool size must account for: (max_connections - reserved) / number_of_app_instances."
retrieval:
  recall: "Explain what EXPLAIN ANALYZE shows in a PostgreSQL query plan and what Seq Scan on a large table tells you."
  explain: "Explain the N+1 problem to a junior developer using a concrete Spring Data JPA example."
  mistakeId:
    code: |
      // Repository
      List<Order> orders = orderRepository.findByUserId(userId);

      // Service — processes each order's items
      for (Order order : orders) {
          List<OrderItem> items = order.getOrderItems(); // LAZY loaded
          processItems(items);
      }
      // With 500 orders: 501 database queries
    answer: "Classic N+1: 1 query to load orders, 500 queries to lazy-load each order's items. At 500 orders, this creates 501 database round trips. In production with 10,000 orders/user this becomes 10,001 queries, causing severe latency. Fix: use JOIN FETCH in the repository query: @Query('SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.userId = :userId') to load all orders and their items in a single SQL query."
---

# Hook

The feature works perfectly in development with 10 records. In production with 2 million records, it takes 45 seconds. The developer adds an index; it drops to 3 seconds. The tech lead notices the service loads 200 orders and then fires 200 additional queries to load their items — the N+1 problem. After fixing that, 1.8 seconds. Then HikariCP starts throwing connection timeout exceptions at peak load because the pool is undersized. Database optimisation is not a single technique — it is a systematic discipline of understanding how the database engine actually executes queries, what the connection layer costs, and how associations load.

# Lore Introduction

The Academy's database masters are called "Plan Readers" — not because they read data, but because they read the engine's execution plan before it runs. A Plan Reader looks at EXPLAIN output the way a chess player looks at a board: every step is intentional, every Seq Scan is a threat, every Index Only Scan is an opportunity exploited. The N+1 problem is their particular nemesis — the "thousand cuts" of database performance, each cut invisible until counted together. The senior engineer who masters the query plan, the index, and the connection pool holds a set of keys that unlock an order of magnitude of performance without changing a line of business logic.

# Core Learning

## Concept Introduction

**Query Execution Plans**

```sql
EXPLAIN ANALYZE SELECT * FROM orders WHERE user_id = 42 AND status = 'PENDING';
```

Key plan nodes:
- `Seq Scan` — reads every row. Acceptable only on small tables.
- `Index Scan` — uses B-tree index to locate rows, then fetches from table.
- `Index Only Scan` — answers query entirely from index (covering index).
- `Bitmap Heap Scan` — uses index to build bitmap of row locations, then batch-fetches.
- `Hash Join`, `Nested Loop`, `Merge Join` — join strategies with different cost profiles.

The `cost=X..Y` values are in arbitrary planner units. The `rows=` estimate vs actual rows from ANALYZE reveals statistics accuracy. `Buffers: shared hit=X read=Y` shows cache hit ratio.

**Index Types**

| Type | Use Case | Range Queries |
|------|----------|---------------|
| B-tree | General: equality, range, ORDER BY | Yes |
| Hash | Equality only, slightly faster for exact match | No |
| GIN | Full-text search, arrays, JSONB | Special |
| Covering | All columns in query included in index | N/A (Index Only Scan) |

**Connection Pooling with HikariCP**

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20        # Total connections to DB
      minimum-idle: 5              # Always-warm connections
      connection-timeout: 5000     # ms before throw on pool exhaustion
      idle-timeout: 600000         # ms before idle connection returned
      max-lifetime: 1800000        # ms max connection lifetime
```

Pool sizing formula (Little's Law): `pool_size = avg_concurrency × avg_query_time_seconds`. If handling 100 concurrent requests with 50ms average query time: `pool_size = 100 × 0.05 = 5`. Add headroom: 10-15 connections.

## Why It Matters

Databases are typically the primary performance bottleneck in web services. A single missing index can turn a 1ms query into a 3-second query. The N+1 problem can turn a 200ms request into a 20-second request. Connection pool misconfiguration can cause cascading timeouts that take down an otherwise-healthy service. Database optimisation skills have the highest ROI of any performance engineering discipline: a 30-minute index addition can outperform weeks of application code optimisation.

## Worked Examples

**Example 1: Identifying and Fixing a Missing Index**

```sql
-- Slow query (3.2s on 5M rows)
EXPLAIN ANALYZE 
SELECT id, amount FROM payments 
WHERE user_id = 42 AND status = 'PENDING' 
ORDER BY created_at DESC LIMIT 20;

-- Plan shows: Seq Scan on payments (cost=0.00..145233.00 rows=23 actual time=3142.233ms)

-- Add composite covering index
CREATE INDEX CONCURRENTLY idx_payments_user_status_date 
ON payments (user_id, status, created_at DESC) INCLUDE (id, amount);

-- Re-run EXPLAIN ANALYZE
-- Plan now shows: Index Only Scan using idx_payments_user_status_date (actual time=0.082ms)
-- 40,000x speedup
```

**Example 2: Fixing N+1 with JOIN FETCH**

```java
// PROBLEM: N+1 — 1 + N queries
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId); // LAZY loads orderItems
}

// FIX: JOIN FETCH loads associations in one query
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.userId = :userId")
    List<Order> findByUserIdWithItems(@Param("userId") Long userId);
}

// FIX 2: EntityGraph
@EntityGraph(attributePaths = {"orderItems"})
List<Order> findByUserId(Long userId);
```

**Example 3: Slow Query Logging**

```yaml
# PostgreSQL: log queries exceeding 500ms
log_min_duration_statement = 500
log_statement = 'none'

# HikariCP leak detection
spring.datasource.hikari.leak-detection-threshold=10000  # Log connections held >10s
```

Slow query log identifies the queries to prioritise. Sort by total time (frequency × duration) not just by maximum duration — a 200ms query running 50,000 times is more impactful than a 5-second query running twice.

## Common Mistakes

- **Adding indexes reactively without EXPLAIN**: indexes have a write cost (every INSERT/UPDATE/DELETE updates indexes). Add only indexes that EXPLAIN proves are needed.
- **Wrong index column order**: composite index (a, b, c) supports queries filtering on (a), (a, b), or (a, b, c), but not (b) or (b, c) alone. The leftmost prefix rule is critical.
- **Forgetting CONCURRENTLY**: `CREATE INDEX` without `CONCURRENTLY` locks the table. Always use `CREATE INDEX CONCURRENTLY` in production.
- **Pool size too large**: 500 connections to a PostgreSQL instance with max_connections=100 causes connection errors. More pool connections does not mean more throughput if the database is the bottleneck.
- **Denormalising prematurely**: denormalisation introduces write complexity and consistency risk. Only denormalise after profiling proves the join cost is the bottleneck.

## Mental Model

The database is a machine with moving parts: the query planner finds the optimal execution path through indexes and join strategies; the storage engine executes the plan fetching data from disk or buffer cache; the connection pool manages a limited resource (database connections) shared by all application threads. Optimising the database is tuning each of these parts in turn: first ensure the planner finds the best path (indexes), then reduce round trips (N+1 fixes), then ensure the connection resource is not exhausted (pool sizing). These improvements compound — fixing the index reduces query time, which reduces connection hold time, which reduces pool pressure.

## Mini Summary

- ✔ EXPLAIN ANALYZE reveals the query plan; Seq Scan on large tables is the primary signal that an index is missing
- ✔ Composite covering indexes allow Index Only Scans, answering queries without accessing the main table
- ✔ N+1 is an ORM default behaviour problem; JOIN FETCH, @EntityGraph, or @BatchSize fix it in JPA
- ✔ HikariCP pool size should be calculated from concurrency × query duration, not set to an arbitrary large number
- ✔ Slow query logging sorted by total time (frequency × duration) prioritises the highest-impact queries

# Guided Practice Quest

Work through the guided steps above. For the JPA fix step, consider which solution fits a read-heavy list API vs a single-entity load endpoint — the optimal strategy may differ.

# Solo Practice Quest

A Spring Boot e-commerce service has these symptoms: checkout API averages 8 seconds, slow query log shows ORDER_ITEMS table Seq Scans, and HikariCP throws connection timeouts during flash sales. The service runs 4 instances against a PostgreSQL database with max_connections=200.

Design an optimisation plan:
1. Write the EXPLAIN queries you would run to diagnose the ORDER_ITEMS Seq Scan
2. Propose an index strategy including column order justification
3. Calculate the correct maximumPoolSize for each instance given 4 instances, max_connections=200, and a 20ms average query duration with 50 concurrent requests per instance
4. Identify what in the ORM layer might be causing excessive queries and propose the fix
5. Describe the deployment strategy for adding the index without downtime

# Integration

**Connecting to Mathematics — B-tree Complexity and Index Trade-offs**

B-tree indexes provide O(log N) lookup time for a table with N rows, compared to O(N) for a sequential scan. For a 10-million-row table, log₂(10,000,000) ≈ 23 — an index lookup examines ~23 B-tree nodes versus 10 million rows. This logarithmic advantage means indexes scale: doubling the table size adds only one B-tree level, not doubling the scan cost. However, each index adds O(log N) cost to every write (INSERT, UPDATE, DELETE must update the index). The trade-off is explicit: N indexes on a table make reads N × O(log N) better but writes N × O(log N) more expensive. High-write, low-read tables (event logs, audit tables) may have fewer indexes than high-read, low-write tables (product catalogues). How does the read/write ratio of your entity influence your indexing strategy?

**Connecting to Design — Data Access Patterns and Domain Boundaries**

The N+1 problem is fundamentally a mismatch between the object graph (how the application thinks about data) and the relational model (how the database stores data). Object-oriented code traverses associations naturally; relational databases require explicit JOINs. ORM bridges this gap, but the bridge has a toll: lazy loading fires queries on property access, far from where the original query was issued. Clean Architecture addresses this by treating the data access layer as an explicit boundary: repositories declare their loading contract (what associations are loaded), not leaving it implicit in lazy-loading behaviour. A repository that returns `Order` with `OrderItems` loaded is a richer contract than one that returns `Order` and silently defers `OrderItems` loading. Making loading strategy explicit at the repository boundary eliminates the N+1 surprise.

# Lore Conclusion

The database does not lie. Its execution plan is an honest account of exactly how it will answer your question. Every Seq Scan on a large table is a statement: "I do not have a path to this data — I will read everything." Every Index Only Scan is a statement: "I have exactly what you need in the index — I will not disturb the table." The senior engineer reads these statements the way a doctor reads test results: not with fear, but with diagnostic precision. EXPLAIN first. Index intelligently. Pool correctly. And never, ever count the N+1 queries until you have already fixed them.
