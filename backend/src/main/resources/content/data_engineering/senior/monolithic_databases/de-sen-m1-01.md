---
id: de-sen-m1-01
school: engineering
domainId: data-engineering
tier: SENIOR
moduleId: de-sen-m1
moduleTitle: "Module 1: Database Architecture"
moduleGlyph: "🏗️"
moduleSortOrder: 1
topicSlug: monolithic_databases
topicTitle: "Monolithic Databases"
topicSortOrder: 1
lesson: monolithic_databases
title: "Monolithic Databases"
sortOrder: 1
difficulty: 4
estimatedMinutes: 35
xpReward: 75
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m8-04]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines monolithic database architecture and its characteristics
    - Explains the advantages of centralised data in a single database
    - Identifies the scaling limitations of monolithic databases (vertical scaling ceiling)
    - Describes the operational concerns of large monolithic databases
    - Explains when a monolithic database is the correct architectural choice
  keywords: [monolithic, single database, vertical scaling, scale-up, shared schema, operational complexity, ACID, joins, foreign keys, centralised, bottleneck, schema coupling, connection pool, write bottleneck]
  modelAnswer: |
    A monolithic database is a single database instance serving all application components — one connection pool, one schema, all tables accessible to all services. Advantages: ACID transactions across all entities, full JOIN capabilities across tables, simple operational model (one database to backup, monitor, and tune). Limitations: vertical scaling ceiling — single node cannot grow beyond the largest available hardware; write bottleneck (all writes go to one master); schema coupling — all teams modify the same schema, causing coordination overhead and deployment risk. Correct choice: monolithic databases are appropriate and often optimal for most applications — the vast majority of systems do not need distributed databases. Choose monolithic until scale or isolation requirements genuinely demand otherwise. The mistake is prematurely distributing — not staying monolithic.
guidedSteps:
  - id: de-sen-m1-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A growing e-commerce platform has one PostgreSQL database serving all functions: orders, inventory, payments, user accounts, analytics. The team wants to "modernise" to microservices with separate databases. What should drive this decision?
    inputConfig:
      options:
        - "Monolithic databases are inherently bad architecture — the move to separate databases is always correct"
        - "The decision should be driven by specific scaling, isolation, or team autonomy problems — if the monolith is working well, there is no reason to distribute"
        - "The platform should immediately split into separate databases to be ready for future scale"
        - "Microservices always require separate databases — they are architecturally inseparable"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The decision should be driven by specific scaling, isolation, or team autonomy problems — if the monolith is working well, there is no reason to distribute"]
      rejectedFeedback: "Distributing a database adds enormous complexity: no cross-database transactions (losing ACID across services), network latency between services, eventual consistency management, distributed failure scenarios, more complex monitoring and operations. These costs are justified only when: (1) The database has a genuine scale bottleneck that vertical scaling cannot solve. (2) Different parts of the system have genuinely different availability, consistency, or compliance requirements. (3) Independent team deployments are blocked by schema coupling. (4) A specific component needs a different data model (graph, document). Without these problems, a monolithic database is simpler, more reliable, and easier to operate than a distributed system. The principle: distributed systems are a solution to specific problems, not a default architecture."
    hint: "What specific problem does splitting the database solve? If there is no problem, is there a benefit?"
    reflectionPrompt: "What is one metric you would monitor to determine if the monolithic database is actually becoming a bottleneck?"
  - id: de-sen-m1-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Scaling a monolithic database by upgrading to a larger server (more CPU, RAM, faster storage) is called ________ scaling. This has an upper limit determined by available hardware.
    inputConfig:
      placeholder: "vertical"
    markingRule:
      matchMode: CONTAINS
      accepted: [vertical, "vertical scaling", "scale up", "scale-up", "scaling up"]
      rejectedFeedback: "Vertical scaling (scale-up): add more resources to a single node — faster CPUs, more RAM, NVMe SSDs, more cores. PostgreSQL and most relational databases scale surprisingly well vertically: a 64-core server with 2TB RAM and NVMe storage can handle enormous workloads. Practical limits: the largest cloud VM (e.g. AWS r6i.metal: 128 vCPU, 1024 GB RAM) handles most real-world workloads. Cost: diminishing returns — 2× hardware cost often does not deliver 2× performance. Horizontal scaling (scale-out): add more servers. For reads: read replicas (common). For writes: sharding (complex) or distributed databases (very complex). Rule: exhaust vertical scaling and read replica strategies before resorting to write sharding or distributed databases. Most systems never genuinely need horizontal write scaling."
    hint: "Making one machine bigger vs adding more machines — what is the term for making one machine bigger?"
    reflectionPrompt: "At what specific point does vertical scaling become insufficient, and what would the monitoring data look like at that point?"
  - id: de-sen-m1-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe two operational advantages of a monolithic database compared to a distributed multi-database architecture.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [backup, single, ACID, transaction, JOIN, cross, monitor, simpler, operations, one, consistency, referential, integrity]
      rejectedFeedback: "Operational advantages of monolithic databases: (1) Simple operations: one database to back up, restore, monitor, tune, and secure. One set of credentials, one connection pool to manage, one query log to analyse. Incident response has one system to check. (2) Full ACID guarantees across all entities: a transaction that updates orders, inventory, and payments simultaneously is atomic in a monolith. In a distributed system, this becomes a distributed transaction (2PC) or requires eventual consistency with saga patterns — both dramatically more complex. (3) Full JOIN capability: analytics queries that span all entities run as single SQL statements. In a distributed system, cross-service queries require API calls or data replication to a data warehouse. (4) Referential integrity across all tables: FK constraints work across the entire domain in a monolith; distributed systems lose this guarantee."
    hint: "Think about what becomes more complex when data is split across multiple databases."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A social media platform stores posts, comments, likes, user profiles, and messages in a single PostgreSQL database. The database handles 10,000 writes/second and 100,000 reads/second. The CPU is at 20% and storage is at 40%. This is:"
    options:
      - "A dangerous architecture that must be immediately split into microservices databases"
      - "A well-functioning monolith — there is no scaling problem to solve"
      - "Approaching its limit — vertical scaling cannot handle this load"
      - "Inefficient — the low CPU usage indicates the database is underutilised"
    correctIndex: 1
    feedback: "10,000 writes/second and 100,000 reads/second on a single PostgreSQL instance is substantial but entirely manageable for a well-configured server. CPU at 20% and storage at 40% indicates the system has significant headroom — there is no imminent scaling problem. This is the correct state: a database that is sized appropriately with growth headroom. The answer 'must be immediately split into microservices' reflects the fallacy that monolithic databases are inherently bad. They are not. The correct response is to continue monitoring, ensure the architecture is ready to scale (read replicas available, connection pooling configured), and address real bottlenecks when they arise. Premature distribution would introduce complexity, operational overhead, and reduced data consistency — solving a problem that doesn't exist."
  - type: MULTIPLE_CHOICE
    question: "Schema coupling in a monolithic database refers to:"
    options:
      - "The performance cost of JOINs between large tables"
      - "Multiple teams or services sharing the same schema — changes by one team can break other teams"
      - "The risk of running out of storage space on a single database server"
      - "The latency introduced by cross-table transactions"
    correctIndex: 1
    feedback: "Schema coupling: in a monolith, all teams work in the same schema. If the payments team renames a column, the orders team's queries may break. A migration requires coordinating across all teams. A new index added by one team may affect the query plans of another team's queries. Schema coupling is a real organizational cost of monolithic databases — but it is managed through: (1) Clear schema ownership (the orders schema is managed by the orders team). (2) Backward-compatible migrations (expand-contract). (3) Schema conventions and review processes. In contrast, separate databases per service (microservices) give each team complete schema autonomy — but at the cost of losing cross-service transactions and joins. The choice between schema coupling and distributed data problems is an architectural trade-off, not a clear winner."
retrieval:
  recall: "List three characteristics of a monolithic database architecture, two genuine advantages, and two genuine limitations. Avoid the framing that monolithic is 'bad' — treat it as one valid choice among several."
  explain: "Explain the scaling progression for a growing application: at what point does each step become necessary (vertical scaling, read replicas, caching, sharding/distribution), and what is the cost of each step?"
  mistakeId:
    code: |
      -- Architecture decision: Split 'archive' monolith into 6 microservices
      -- each with their own database, because "that's best practice"
      -- 
      -- Current system: 5,000 daily active users, 50 req/s peak, 
      -- PostgreSQL CPU at 15%, 200GB data, 2 developers
      -- 
      -- Proposed new architecture:
      -- loans-service → loans-db (PostgreSQL)
      -- members-service → members-db (PostgreSQL)  
      -- items-service → items-db (PostgreSQL)
      -- fines-service → fines-db (PostgreSQL)
      -- reservations-service → reservations-db (PostgreSQL)
      -- analytics-service → analytics-db (PostgreSQL)
    answer: "Premature distribution. At 5,000 daily active users, 50 req/s, and 15% CPU utilization with 2 developers, there is no scaling problem to solve. Distributing into 6 separate databases creates: (1) No cross-database transactions — checking out an item (create loan + update inventory + notify member) now requires either a distributed saga or distributed 2PC — complex to implement and debug. (2) No cross-database JOINs — the analytics service cannot query across members + loans + items with SQL; it needs ETL or API calls. (3) 6× the operational complexity: 6 databases to backup, monitor, tune, and secure. (4) Network latency between services on every cross-service call — previously one SQL JOIN, now multiple HTTP calls. (5) With 2 developers, this architecture is unmanageable. The correct decision: keep the monolith. Consider splitting only when a specific, measurable problem demands it. The cost of distributing must be less than the cost of the problem being solved."
---

# Hook

Every experienced data engineer has seen the same mistake: a team distributes their data before they need to, in pursuit of architectural purity, and spends the next two years fighting distributed systems problems instead of building features. The monolithic database is not a legacy antipattern — it is the correct default. The question is not "when do we move away from monolithic?" but "what specific problem requires us to incur the cost of distribution?"

# Lore Introduction

"The Consortium's new architect wants to split the Archive into six microservices databases," the Lead Data Engineer said, reading the proposal. "One database per service. 'Industry best practice'." The Senior Engineer read it. "What problem does this solve?" The Lead Data Engineer looked at the metrics. "PostgreSQL at 12% CPU, 18% storage, 3,000 daily users. No performance problems." The Senior Engineer set the document down. "Then it solves no problem, and creates many. Today's lesson: understand what a monolithic database actually is, when it excels, and what specifically has to be true before the cost of distributing it is justified." She looked at the proposal again. "The architect will present tomorrow. We need to be ready with the right questions."

# Core Learning

## Concept Introduction

### Monolithic Database Characteristics

```
Monolithic Database Architecture:

  Application Server(s)
  ┌──────────────────────────────────────┐
  │  Orders Service   │  Members Service │
  │  Items Service    │  Analytics       │
  └──────────┬───────────────┬──────────┘
             │               │
      Connection Pool (shared)
             │
  ┌──────────▼────────────────────────────┐
  │        PostgreSQL (single instance)   │
  │  Schema: orders, members, items,      │
  │          loans, fines, reservations   │
  │  All tables, all data, one server     │
  └───────────────────────────────────────┘

Characteristics:
  ✓ Single connection pool, single schema
  ✓ Full ACID transactions across all tables
  ✓ Full JOIN capability across entire domain
  ✓ FK constraints span the entire domain
  ✓ One system to backup, monitor, tune
  ✗ All teams work in the same schema (coupling)
  ✗ Single write master (vertical scaling ceiling)
  ✗ All services share the same failure domain
```

### When Monolithic is the Right Choice

```
SCALE THRESHOLDS (rough guidelines):
  < 10,000 req/s write: well-tuned PostgreSQL on vertical hardware handles this
  < 1TB data: single server with NVMe SSDs is fast and operationally simple
  < 50 developers: schema coupling is manageable with process and conventions
  < 5 nines availability on any single component: single-region monolith is fine

Signs you do NOT need to distribute yet:
  - Database CPU < 60% at peak
  - No write bottleneck (replication lag < 100ms)
  - No team is blocked waiting for another team's schema changes
  - No component requires a fundamentally different data model (graph, document)
  - You can vertically scale the server to address current growth

Signs you might genuinely need to distribute:
  - Write throughput exceeds what one node handles (proven by load testing)
  - Team size makes schema coordination genuinely painful
  - One component has dramatically different availability/consistency requirements
  - Compliance requires data isolation at the storage level
```

### Vertical Scaling Progression

```
Stage 1: Default setup
  2-4 vCPU, 8-16 GB RAM, SSD
  → Handles: most small/medium applications

Stage 2: Right-size the instance
  8-16 vCPU, 64-128 GB RAM, NVMe
  → Handles: medium-large applications, millions of users

Stage 3: Optimise before scaling hardware
  - Index correctly (Module 4)
  - Query optimisation (Module 4)
  - Connection pooling (PgBouncer)
  - Caching layer (Redis) for hot data
  → Often solves apparent "scaling" problems

Stage 4: Read replicas
  One write master + N read replicas
  → Handles: read-heavy workloads at high scale

Stage 5: Vertical scaling to large instance
  64+ vCPU, 512+ GB RAM, high-IOPS NVMe
  → Handles: very high write throughput on one node

Stage 6: Distributed (sharding, multi-master)
  → Only if Stage 5 is genuinely insufficient
  → Significant operational complexity
  → Most systems never need this
```

### Operational Simplicity Advantage

```
Monolith operations:
  Backup:    pg_dump archive > backup.sql  (one command)
  Restore:   psql archive < backup.sql     (one command)
  Monitor:   one Grafana dashboard, one set of metrics
  Tune:      one postgresql.conf, one query log
  Security:  one set of roles and permissions
  Migrate:   one Flyway migration set
  Incident:  check one system

Distributed (6 databases) operations:
  Backup:    6 separate backup jobs, coordinate timing
  Restore:   restore all 6, verify cross-service consistency
  Monitor:   6 dashboards, cross-correlate incidents
  Tune:      6 configs, 6 query logs
  Security:  6 separate access control configurations
  Migrate:   6 separate migration sets, coordinate ordering
  Incident:  which of the 6 failed? what cascaded?
```

## Common Mistakes

- **Premature distribution**: splitting a monolith without a specific scaling or isolation problem creates operational complexity that slows development and reduces reliability. Monolithic databases are not a stepping stone — they can be the final architecture.
- **Confusing operational complexity with scale**: teams that struggle with a monolith often have process problems (bad indexes, unmaintained queries, schema ownership issues) that distribution does not fix — it just moves the problems.
- **"Big Ball of Mud" schema**: a monolith becomes problematic when schema ownership is unclear and all teams write to all tables without conventions. Fix: define clear logical ownership zones within the schema, even if they remain physically in one database.
- **Assuming distribution is the next step**: for most applications, the next step after a well-tuned monolith is a read replica + caching layer — not a distributed multi-database architecture.

## Mental Model

A monolithic database is a single well-organised library — all books under one roof, every librarian can access any shelf, the catalogue is consistent, and you always know where to find a book. Splitting into distributed databases is building branch libraries: each neighbourhood has its own collection, but the libraries don't share a catalogue — finding a book means calling multiple branches. Branch libraries solve specific access problems (local availability), but they cost more to operate and make cross-library research harder. The correct choice depends on whether the access problem you're solving is real and significant enough to justify the coordination overhead.

## Mini Summary

- ✔ Monolithic databases: single instance, all tables, full ACID, full JOIN capability
- ✔ Advantages: operational simplicity, cross-domain transactions, referential integrity
- ✔ Limitations: vertical scaling ceiling, schema coupling, shared failure domain
- ✔ Scale progressively: tune → read replicas → caching → vertical scaling → distribute (if genuinely needed)
- ✔ Most systems never need horizontal write distribution
- ✔ Distribute only when a specific, measurable problem requires it — not for architectural purity

# Guided Practice Quest

Work through the guided steps to evaluate a given system's metrics (CPU, storage, team size, query patterns) and determine whether distribution is justified, design the read replica architecture as an intermediate scaling step, and identify the specific threshold at which distribution would become necessary.

# Solo Practice Quest

Evaluate and defend an architectural choice for the Consortium's Archive system. Tasks: (1) Given current metrics (8% CPU, 30% storage, 25 engineers, 15,000 DAU, 300 req/s peak), determine whether the monolith should be distributed — justify with specific reasoning; (2) Design the scaling roadmap from current state to 10× scale (3,000 req/s, 150,000 DAU) using only monolith + read replicas + caching, without distributing; (3) Identify the three specific metrics that, if exceeded, would genuinely justify splitting the database — state the threshold values; (4) For each of the proposed 6 services in the architect's plan, identify one cross-service operation that would become a distributed transaction and describe the complexity added; (5) Write a one-page architectural review response to the architect's proposal, using specific metrics and complexity trade-offs rather than opinion.

# Integration

**Mathematics**: Amdahl's Law applies to database scaling: the speedup from parallelism is limited by the sequential fraction of the workload. For a database where 20% of operations are non-parallelisable (write-ahead log, sequential constraint checking, primary key generation), the maximum speedup from N parallel nodes is 1/(0.2 + 0.8/N). At N=10 nodes: speedup ≤ 1/(0.2 + 0.08) = 3.57× — not 10×. This formalises why horizontal scaling of databases has diminishing returns: the serialisability requirements of ACID, the WAL bottleneck, and the coordination overhead of distributed consensus all impose serial fractions. For a typical OLTP database with 30% serial-fraction work, the theoretical maximum speedup from infinite nodes is 1/0.3 = 3.33× — meaning a 3-node cluster can approach the theoretical optimum, and adding more nodes provides negligible additional benefit while adding coordination overhead.

**Sciences (Organisational Theory — Conway's Law)**: Melvin Conway's observation (1968) — "organisations which design systems are constrained to produce designs which are copies of the communication structures of those organisations" — directly applies to database architecture decisions. A company with 3 teams will tend to produce a 3-component system. A company with 6 teams will tend to produce a 6-service architecture. The monolith vs microservices debate is therefore partly a reflection of organisational structure. A team of 5 engineers operates most efficiently with a monolith (one communication structure). A company of 500 engineers organised into 20 independent product teams may genuinely benefit from service separation — not primarily for technical reasons, but because schema coupling between 20 teams creates coordination costs that exceed the costs of distribution. This is the correct reason to distribute: organisational scale, not technology bias.

# Lore Conclusion

"The architect's proposal is withdrawn," the Lead Data Engineer reported, returning from the presentation. "The review committee asked three questions: what specific problem does this solve? what is the cost of distribution? and what happens to the monthly checkout query that currently JOINs four tables?" The Senior Engineer looked up. "The cross-join problem." The Lead nodded. "The architect had no answer for the cross-service query. It would require either duplicating data across databases or building an ETL pipeline for something that currently works as one SQL statement." The Senior Engineer set down her notes. "The monolith stays, for now. But we document the thresholds: if write throughput exceeds 5,000/second, if the team exceeds 40 engineers, if a component genuinely needs a graph database — those are the trigger points." The Lead wrote them down. "Architecture decisions should be driven by problems, not trends."

---
