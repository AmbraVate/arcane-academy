---
id: de-sen-m2-02
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m2
moduleTitle: "Module 2: Distributed Data Systems"
moduleGlyph: "🌐"
moduleSortOrder: 2
topicSlug: sharding
topicTitle: "Sharding"
topicSortOrder: 2
lesson: 2
title: "Sharding: Splitting Data Across Nodes"
sortOrder: 2
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
  - de-sen-m2-01
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains shard key selection criteria and the hot spot risk of poor choices"
    - "Describes consistent hashing and why it minimises resharding data movement"
    - "Identifies scenarios where sharding is genuinely necessary vs premature"
    - "Explains cross-shard query limitations and how to design around them"
  keywords:
    - shard key
    - consistent hashing
    - hot spot
    - resharding
    - cross-shard query
    - cardinality
  modelAnswer: |
    Sharding horizontally partitions data across multiple independent database nodes, each owning a subset of rows. Shard key selection is the most critical design decision: a poor key (e.g. a boolean flag, a low-cardinality field, a monotonically increasing timestamp) routes most traffic to one shard, creating a hot spot that defeats the purpose. Good shard keys have high cardinality, are present in most queries, and distribute write load evenly.
    Consistent hashing maps both keys and nodes onto a ring, so adding or removing a node only redistributes ~1/N of data rather than nearly everything. Without consistent hashing, naive modulo sharding (shard = hash(key) % N) forces a full reshard when N changes.
    Cross-shard queries — JOINs across shards, aggregates without a shard key in the WHERE clause — require scatter-gather: fan out to all shards, collect results, merge. This is slow and operationally complex; the schema should be designed so the most common queries are shard-local.
    Sharding is genuinely needed only when a single node cannot handle the write throughput even after vertical scaling and read replicas are saturated. Most systems never reach this threshold; premature sharding adds enormous operational complexity for no gain.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "A social platform shards its `posts` table by `user_id`. A celebrity with 10 million followers makes one post per minute. What problem does this create?"
    options:
      - "Data loss due to replication lag"
      - "A hot shard receiving disproportionate write and read traffic"
      - "Cross-shard JOIN failures on the followers table"
      - "Consistent hashing ring imbalance"
    correctIndex: 1
    explanation: "The celebrity's shard receives all their writes and the bulk of reads. High-cardinality user_id is usually a good shard key, but celebrity accounts are a classic hot-spot exception — sometimes mitigated by sharding celebrities to their own dedicated node or using write amplification to fan out their posts."
  - type: FILL_BLANK
    question: "In consistent hashing, adding one new node to a ring of N nodes means approximately ___ of the existing data must be redistributed."
    answer: "1/(N+1)"
    explanation: "Each node owns roughly 1/(N+1) of the keyspace after rebalancing. Only the data that now falls on the new node's arc needs to move, compared to ~100% redistribution in naive modulo sharding."
  - type: SHORT_TEXT
    question: "You have a 10-shard cluster. A reporting query needs COUNT(*) across all users. Describe the execution plan."
    modelAnswer: "The query coordinator fans out the COUNT(*) query to all 10 shards in parallel (scatter). Each shard returns its local count. The coordinator sums the 10 partial counts (gather) and returns the total. This scatter-gather pattern adds latency proportional to the slowest shard."
microCheckpoint:
  question: "What is a hot spot in sharding and what shard key property helps prevent it?"
  answer: "A hot spot is one shard receiving a disproportionate share of traffic, overloading that node. High cardinality in the shard key helps distribute writes and reads evenly across all shards."
retrieval:
  recall: "What does consistent hashing achieve that naive modulo sharding cannot?"
  explain: "Explain why cross-shard JOINs are expensive and how schema design can reduce them."
  mistakeId: "sharding-premature-complexity"
---

# The Weight of Scale

The Senior Engineer pulled up the metrics dashboard. Write throughput had hit the ceiling three days in a row. Every vertical scaling option was exhausted. "We've reached the limit of one box," the Lead Data Engineer said. "It's time to talk about sharding."

# What Sharding Actually Is

Sharding is **horizontal partitioning across physically separate database nodes**. Each node — called a shard — owns a distinct, non-overlapping slice of the data. Combined, all shards hold the complete dataset.

```
            ┌─────────────┐
            │   Client    │
            └──────┬──────┘
                   │
            ┌──────▼──────┐
            │   Router /  │
            │  Coordinator│
            └──┬───┬───┬──┘
               │   │   │
         ┌─────▼┐ ┌▼────┐ ┌▼─────┐
         │Shard │ │Shard│ │Shard │
         │  A   │ │  B  │ │  C   │
         │users │ │users│ │users │
         │0–33% │ │33–66│ │66–100│
         └──────┘ └─────┘ └──────┘
```

Each shard is a full PostgreSQL instance with its own CPU, RAM, and disk. The router (or application logic) determines which shard to query based on the **shard key** — a field present in the data.

## Shard Key Selection

The shard key is the most consequential decision in the entire sharding design. A bad shard key creates a **hot spot** — one overloaded shard.

| Shard Key Choice | Problem |
|---|---|
| `is_premium` (boolean) | 2 shards max; 95% traffic on non-premium |
| `created_at` (timestamp) | All new writes go to the latest shard |
| `country_code` | Uneven; US shard gets 40% of all traffic |
| `user_id` (UUID/hash) | High cardinality, even distribution ✓ |
| `tenant_id` (SaaS) | Good if tenants are similarly sized ✓ |

**Criteria for a good shard key:**
1. **High cardinality** — thousands of distinct values minimum
2. **Present in most queries** — avoids scatter-gather
3. **Even write distribution** — no celebrity/super-user skew
4. **Stable** — changing a row's shard key requires moving data

## Consistent Hashing

Naive sharding uses `shard = hash(key) % N`. This breaks when `N` changes: every row's shard assignment shifts, forcing a full data migration.

**Consistent hashing** places both keys and nodes on a ring of hash values (typically 0–2³²). Each node owns the arc from its position to the previous node's position. Adding one node to an N-node ring only moves ~1/(N+1) of the data.

```
         0
    ┌────┴────┐
   330        90
    │  NodeC  NodeA  │
   270   ──ring──   120
    │  NodeB       │
   210        150
    └────┬────┘
        180

Adding NodeD between 270 and 330:
Only data in that arc (270–330) migrates to NodeD.
```

Most sharding middleware (Citus, Vitess, application-layer routers) uses consistent hashing with **virtual nodes** (vnodes) — each physical node owns multiple arcs — to further smooth distribution.

## Cross-Shard Queries

Queries that include the shard key in their `WHERE` clause are **shard-local** — routed to one shard, no fan-out.

```sql
-- Shard-local (user_id is the shard key): fast ✓
SELECT * FROM orders WHERE user_id = 'abc-123';

-- Cross-shard (no shard key): scatter-gather to all shards ✗
SELECT COUNT(*) FROM orders WHERE status = 'PENDING';
```

The scatter-gather pattern:
1. Coordinator broadcasts query to all N shards in parallel
2. Each shard executes locally and returns partial results
3. Coordinator merges results (SUM, UNION, sort-merge JOIN)
4. Latency = slowest shard response + merge time

Cross-shard JOINs are particularly painful — data from two shards must be pulled to the coordinator for in-memory joining. Design your schema so the entities that JOIN together share the same shard key.

```sql
-- Collocating: shard both orders and order_items by user_id
-- JOIN is always local to one shard
SELECT o.id, oi.product_id
FROM orders o
JOIN order_items oi ON oi.order_id = o.id
WHERE o.user_id = 'abc-123';  -- shard-local ✓
```

## Resharding

As data grows, shards themselves may become too large and need to be split. **Resharding** is operationally complex:

1. Choose a new shard count (often 2× current)
2. Compute new shard assignments for all rows
3. Migrate rows to their new shards (while serving live traffic)
4. Update router configuration atomically
5. Remove old shards

Zero-downtime resharding requires careful orchestration. Some systems (Citus, CockroachDB) make this automated; home-rolled sharding does not. This complexity is a strong argument against sharding prematurely.

## Common Mistakes

> **Hot Shard from Low-Cardinality Key**
> A `region` column with 5 values creates 5 shards at most. Traffic skews heavily to the US shard. Solution: composite shard key or prefix the key with a random salt.

> **Forgetting Scatter-Gather Cost**
> Dashboard queries without the shard key in WHERE now fan out to all shards. A 10-shard cluster turns a 100ms query into 100ms × 10 + merge overhead. Profile cross-shard query frequency before committing to a shard key.

> **Sharding Too Early**
> Most databases never need sharding. Replication handles read scale; vertical scaling handles write scale to ~10k TPS on modern hardware. The operational burden of sharding — resharding, cross-shard queries, distributed transactions — is enormous. Exhaust all single-node options first.

## Mental Model

Think of sharding like distributing books across multiple warehouses. The shard key is the first letter of the title. Warehouse A has A–H, B has I–P, C has Q–Z. Finding one book is fast — go to the right warehouse. Finding all books published in 2023 means sending a query to all three warehouses and combining the results. The filing system (consistent hashing) means you can add a fourth warehouse without re-cataloguing everything — just the books on the boundary arc move.

**Mini Summary**: Sharding splits data across independent nodes for write scale. Choose a high-cardinality, evenly distributed shard key. Consistent hashing minimises migration cost when adding nodes. Cross-shard queries use scatter-gather and are expensive. Only shard when single-node options are genuinely exhausted.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium is evaluating sharding for its transaction ledger. Currently 8 million rows, growing at 500k/month, running on a beefy single node with 98% CPU utilisation during peak reconciliation. The schema has: `transactions(id UUID, account_id UUID, merchant_id UUID, amount DECIMAL, created_at TIMESTAMPTZ, status TEXT)`.

Reflect on:
1. Is this workload a legitimate sharding candidate? Justify using the "exhaust single-node options first" principle.
2. Compare `account_id`, `merchant_id`, and `created_at` as shard key candidates. Which do you recommend and why?
3. Describe one cross-shard query that will be unavoidable and explain how you would mitigate its cost.

---

# Integration

**Mathematics**: Consistent hashing is a form of **modular arithmetic on a circular group**. The ring is typically 2³² (≈4.3 billion) positions. If you have N nodes each with V virtual nodes, the expected load per node is 1/N ± variance that shrinks as V increases (law of large numbers). The variance of load imbalance follows 1/√(N×V), so 10 nodes × 150 vnodes gives ≈ 2.6% standard deviation — practically uniform.

**Sciences**: Sharding mirrors **taxonomy in biology** — classifying organisms into phyla, classes, orders. Each level partitions the space cleanly so a lookup traverses a narrow path from kingdom to species. A bad taxonomic key (e.g. "all organisms that are the colour brown") creates messy overlapping categories, just as a bad shard key creates overlapping hot spots. The key insight from both: the partition criterion must be *intrinsic to the data*, not incidental.

---

# The Allocation

The Lead Data Engineer nodded at the architectural diagram the Senior Engineer had drawn: account_id as shard key, eight shards, virtual nodes for balance. "The reconciliation queries will scatter-gather — we'll add a pre-aggregated summary table to cache those results nightly."

"What about resharding when we grow past eight?" the Senior Engineer asked. "We adopt Citus," the Lead replied, "and that problem becomes someone else's carefully engineered solution."
