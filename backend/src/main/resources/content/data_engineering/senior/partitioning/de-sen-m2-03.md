---
id: de-sen-m2-03
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m2
moduleTitle: "Module 2: Distributed Data Systems"
moduleGlyph: "🌐"
moduleSortOrder: 2
topicSlug: partitioning
topicTitle: "Partitioning"
topicSortOrder: 3
lesson: 3
title: "Partitioning: Dividing Tables Within a Node"
sortOrder: 3
difficulty: 4
estimatedMinutes: 35
xpReward: 75
practiceType: GUIDED_AND_SOLO
questType: RETRIEVAL_CHALLENGE
retrievalWeight: 0.6
questTypes:
  - guided
  - solo
  - retrieval
prerequisites:
  - de-sen-m2-02
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Distinguishes partitioning (within one node) from sharding (across nodes)"
    - "Explains range, list, and hash partitioning with appropriate use cases"
    - "Describes partition pruning and why it requires the partition key in the query"
    - "Identifies the partition key as a prerequisite for global indexes vs local indexes"
  keywords:
    - partition pruning
    - range partitioning
    - list partitioning
    - hash partitioning
    - partition key
    - local index
    - global index
  modelAnswer: |
    Partitioning divides one logical table into multiple physical storage segments (child tables) within the same database instance. It differs from sharding: both nodes and data stay on one machine; the gain is query performance and maintenance, not write throughput.
    Range partitioning (e.g. by month) enables pruning — the query planner skips partitions whose range cannot match the WHERE clause. This is the primary performance benefit. List partitioning maps discrete values to partitions (e.g. country codes), useful for data locality or regulatory isolation. Hash partitioning distributes rows evenly when no natural range or list exists.
    Partition pruning only fires when the query filter includes the partition key in a form the planner can evaluate at plan time. Wrapping the key in a function (DATE_TRUNC) or comparing to a non-constant defeats pruning.
    Indexes on partitioned tables are by default local (per-partition). A global (cross-partition) index is possible but makes partition detach/drop expensive because the index must be updated.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "A table is partitioned by range on `created_at` with monthly partitions. A query runs: `SELECT * FROM events WHERE DATE(created_at) = '2024-03-15'`. Will partition pruning fire?"
    options:
      - "Yes — the query targets a specific date, so only the March partition is scanned"
      - "No — wrapping created_at in DATE() prevents the planner from pruning by partition boundary"
      - "Yes — the planner can infer the month from the date"
      - "No — range partitioning does not support date-based pruning"
    correctIndex: 1
    explanation: "DATE(created_at) transforms the partition key. The planner cannot determine at plan time which partitions the transformed value might fall in, so it scans all partitions. Write the filter as: WHERE created_at >= '2024-03-15' AND created_at < '2024-03-16' to enable pruning."
  - type: FILL_BLANK
    question: "When you DROP or DETACH a monthly partition, the data is removed or detached in ___ (time complexity) regardless of how many rows the partition contains."
    answer: "O(1)"
    explanation: "Partition detach/drop is a metadata operation — it unlinks the child table from the parent. The actual storage can then be dropped or archived separately. This is orders of magnitude faster than DELETE FROM table WHERE created_at BETWEEN ... which must process every row."
  - type: SHORT_TEXT
    question: "You need to query events across all partitions filtered only by user_id (not the partition key). What happens and what is the mitigation?"
    modelAnswer: "The planner cannot prune any partition, so it scans all of them — a full table scan across every partition. Mitigation: add a local index on user_id in each partition (PostgreSQL creates these automatically with CREATE INDEX on the parent). Queries still touch all partitions but use the index rather than sequential scan within each."
microCheckpoint:
  question: "What is the key difference between partitioning and sharding?"
  answer: "Partitioning divides a table into segments within a single database node — improving performance and maintenance without distributing across machines. Sharding distributes data across multiple independent nodes — primarily for write throughput scale."
retrieval:
  recall: "What three strategies exist for partitioning a table, and when would you choose each?"
  explain: "Explain partition pruning — what enables it and what defeats it."
  mistakeId: "partitioning-function-on-key-defeats-pruning"
---

# Archive Archaeology

"This events table is 2.4 billion rows," the Senior Engineer said. Query response times had degraded to 30 seconds. The Lead Data Engineer glanced at the table DDL. "There's no partitioning. We need to fix that before we even consider distribution."

# Partitioning vs Sharding

Before going further: **partitioning and sharding are different things**.

| | Partitioning | Sharding |
|---|---|---|
| **Node count** | One database instance | Multiple independent instances |
| **Primary goal** | Query performance, maintenance | Write throughput, capacity |
| **Data location** | Same machine, different files | Different machines |
| **Complexity** | Low–Medium | High |
| **When to use** | Large tables with range/category patterns | Exhausted single-node options |

Partitioning is a **within-node optimisation**. It divides one logical table into multiple physical child tables called partitions. SQL queries still address the parent table — the planner routes them internally.

## PostgreSQL Declarative Partitioning

```sql
-- Parent table — no data stored here
CREATE TABLE events (
    id          BIGINT GENERATED ALWAYS AS IDENTITY,
    user_id     UUID        NOT NULL,
    event_type  TEXT        NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL,
    payload     JSONB
) PARTITION BY RANGE (created_at);

-- Child partitions — data lives here
CREATE TABLE events_2024_01
    PARTITION OF events
    FOR VALUES FROM ('2024-01-01') TO ('2024-02-01');

CREATE TABLE events_2024_02
    PARTITION OF events
    FOR VALUES FROM ('2024-02-01') TO ('2024-03-01');

-- Automate with pg_partman extension
```

INSERT, UPDATE, DELETE, SELECT on `events` transparently route to the correct child partition.

## Partition Strategies

### Range Partitioning
Divides rows by a continuous range of values. Best for **time-series data**, logs, transaction history.

```sql
PARTITION BY RANGE (created_at)
-- Each partition: one month, one quarter, or one year
```

**Benefits**: natural for archival (DROP old partitions), even growth if data is temporally distributed.

### List Partitioning
Maps discrete values to specific partitions. Best for **categorical data** — country codes, regions, product lines.

```sql
CREATE TABLE orders (
    id         BIGINT,
    region     TEXT,
    ...
) PARTITION BY LIST (region);

CREATE TABLE orders_eu PARTITION OF orders FOR VALUES IN ('UK', 'DE', 'FR', 'NL');
CREATE TABLE orders_us PARTITION OF orders FOR VALUES IN ('US', 'CA');
CREATE TABLE orders_apac PARTITION OF orders FOR VALUES IN ('AU', 'JP', 'SG');
```

Useful for data residency requirements — EU data lives on EU-located storage.

### Hash Partitioning
Distributes rows by a hash of the partition key. Use when there is no natural range or list, but you want even distribution for performance.

```sql
CREATE TABLE sessions (
    id      UUID,
    user_id UUID,
    ...
) PARTITION BY HASH (user_id);

CREATE TABLE sessions_0 PARTITION OF sessions FOR VALUES WITH (MODULUS 4, REMAINDER 0);
CREATE TABLE sessions_1 PARTITION OF sessions FOR VALUES WITH (MODULUS 4, REMAINDER 1);
CREATE TABLE sessions_2 PARTITION OF sessions FOR VALUES WITH (MODULUS 4, REMAINDER 2);
CREATE TABLE sessions_3 PARTITION OF sessions FOR VALUES WITH (MODULUS 4, REMAINDER 3);
```

## Partition Pruning

The primary performance gain from range and list partitioning is **partition pruning** — the query planner skips partitions that cannot contain matching rows.

```sql
-- Pruning fires: planner eliminates all partitions except 2024-03
SELECT * FROM events
WHERE created_at >= '2024-03-01'
  AND created_at <  '2024-04-01';
-- EXPLAIN shows: "Partitions: events_2024_03"

-- Pruning FAILS: DATE() wraps the partition key
SELECT * FROM events
WHERE DATE(created_at) = '2024-03-15';
-- EXPLAIN shows: "Partitions: ALL" — full scan
```

**Rules for pruning to fire:**
- Filter must be on the partition key directly (no function wrappers)
- Filter must use a constant or parameter the planner can evaluate at plan time
- For range partitions: `>=`, `<=`, `<`, `>`, `BETWEEN` with boundary values

## Indexes on Partitioned Tables

```sql
-- Creates a LOCAL index on each partition automatically
CREATE INDEX ON events (user_id);

-- PostgreSQL 11+: create only if not already exists
CREATE INDEX IF NOT EXISTS events_user_id_idx ON events (user_id);
```

**Local indexes** (per-partition) are the default. They make DROP PARTITION and DETACH PARTITION O(1) metadata operations — no index maintenance needed when removing a partition.

**Global indexes** (cross-partition) are possible but make partition management expensive. Prefer local indexes and accept that non-partition-key queries must scan all partition indexes.

## Partition Maintenance Benefits

```sql
-- Archive last year's data: milliseconds, not hours
ALTER TABLE events DETACH PARTITION events_2023_01;
-- events_2023_01 is now a standalone table
-- Optionally: pg_dump it to cold storage, then DROP TABLE events_2023_01;

-- Add future partition
CREATE TABLE events_2025_01
    PARTITION OF events
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');
```

Compare this to `DELETE FROM events WHERE created_at < '2024-01-01'` — a row-by-row operation generating enormous WAL, locking the table, and taking hours on 100M+ rows.

## Common Mistakes

> **Using a Function on the Partition Key**
> `WHERE EXTRACT(YEAR FROM created_at) = 2024` defeats pruning. Write `WHERE created_at >= '2024-01-01' AND created_at < '2025-01-01'` instead.

> **Partitioning Small Tables**
> Partitioning adds planning overhead. Tables under 10M rows rarely benefit. Profile before partitioning — the problem is usually missing indexes, not table size.

> **Not Creating a Default Partition**
> Without a DEFAULT partition, an INSERT with a value outside all partition ranges fails. Always create: `CREATE TABLE events_default PARTITION OF events DEFAULT;`

> **Assuming Partitioning Replaces Sharding**
> Partitioning is purely a performance and maintenance tool on a single node. Write throughput is not increased — all writes still go through one PostgreSQL instance.

## Mental Model

Think of a partitioned table as a **filing cabinet with labelled drawers** (months, regions, categories). When you need files from March, you open only the March drawer. If you ask for files by author name (not the drawer label), you still check every drawer. Dropping an old drawer (DETACH PARTITION) takes one second. Destroying individual files one by one (DELETE) takes hours.

**Mini Summary**: Partitioning splits a table into physical segments within one node. Range is best for time-series, list for categorical, hash for even distribution. Partition pruning skips irrelevant segments — only fires when the partition key appears unmodified in the WHERE clause. Partition detach/drop is O(1), making archival trivially fast.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium's `audit_log` table has 1.8 billion rows. It is partitioned by hash on `user_id` into 8 partitions. A compliance query needs all audit events for a specific user over the past 90 days: `WHERE user_id = $1 AND created_at > NOW() - INTERVAL '90 days'`.

Reflect on:
1. Will partition pruning fire? If so, on which dimension(s)?
2. What index would you create to serve this query efficiently?
3. Should hash partitioning have been the choice here? What alternative might be better, and why?

---

# Integration

**Mathematics**: Partition pruning is an application of **interval arithmetic**. The planner maintains partition boundaries as half-open intervals [start, end). A query predicate `x >= a AND x < b` represents another interval [a, b). If the intersection of the two intervals is empty, the partition is pruned. The number of non-pruned partitions is bounded by how many intervals overlap with the query interval — for a time-series query on one month, this is exactly 1 out of N partitions: an N-fold reduction in I/O.

**Sciences**: Partitioning mirrors **stratigraphic layers in geology**. Rock layers (strata) accumulate over time; a geologist dating a sample from a specific era examines only the relevant stratum rather than the entire column. Detaching a partition for archival is analogous to lifting a core sample from the column — the layer exists as its own unit. The analogy breaks at non-temporal dimensions: list and hash partitioning are more like classifying minerals by chemical composition rather than age.

---

# The Restructured Archive

The Senior Engineer ran the migration: twelve monthly partitions for the past year, a DEFAULT partition for older data, and a background job to gradually migrate historical rows. The next morning, the 30-second query ran in 400ms. "It wasn't distribution we needed," the Lead Data Engineer said. "It was organisation."
