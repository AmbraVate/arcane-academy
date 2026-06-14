---
id: de-sen-m8-01
school: engineering
domainId: data-engineering
tier: SENIOR
moduleId: de-sen-m8
moduleTitle: "Module 8: Senior Project"
moduleGlyph: "🏗️"
moduleSortOrder: 8
topicSlug: mini_project
topicTitle: "Mini Project"
topicSortOrder: 1
lesson: the_polyglot_data_platform
title: "The Polyglot Data Platform"
sortOrder: 1
difficulty: 7
estimatedMinutes: 360
xpReward: 500
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: low
questTypes: [solo]
prerequisites:
  - de-sen-m1-01
  - de-sen-m2-01
  - de-sen-m3-01
  - de-sen-m4-01
  - de-sen-m5-01
  - de-sen-m6-01
  - de-sen-m7-01
integrationDomains: [mathematics, psychology, ethics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly identifies which storage technology is appropriate for each use case and explains the reasoning"
    - "Database selection demonstrates understanding of the CAP theorem trade-offs (consistency vs availability vs partition tolerance)"
    - "At least one batch processing pipeline is designed with correct extract, transform, load stages and error handling"
    - "At least one streaming or near-real-time data flow is designed with a stated latency target and delivery guarantee"
    - "High availability and disaster recovery requirements are addressed with specific mechanisms (replication, backups, RTO/RPO targets)"
    - "Data governance controls are applied: ownership is assigned, retention periods are defined, and at least one compliance requirement is addressed"
    - "The design document is written for both a technical audience (architecture decisions) and a non-technical audience (business justification)"
    - "Written reflection honestly identifies at least two significant tradeoffs and the conditions under which the alternative would have been better"
  keywords: [polyglot, NoSQL, CAP theorem, batch processing, streaming, replication, backup, RTO, RPO, governance, retention, compliance, ETL, ELT]
  modelAnswer: |
    A complete Polyglot Data Platform correctly matches storage technologies to use cases
    with explicit CAP theorem reasoning, designs both a batch and a streaming data flow
    with stated delivery guarantees and error handling, addresses HA/DR requirements with
    specific mechanisms and quantified targets, applies data governance controls including
    ownership and retention, and produces a design document readable by both technical
    and non-technical audiences. The reflection demonstrates honest engagement with the
    tradeoffs rather than defending every choice as optimal.
---

# Hook

Your Junior project was a single database. Your Senior project is a platform.

A platform that combines relational storage for transactions, a document store for flexible schemas, a time-series database for metrics, a graph database for relationships, and a message queue for event streaming — because no single technology serves all data needs well.

A platform that handles batch processing of historical data and real-time streaming of events. That survives the failure of a node. That complies with data protection requirements. That is owned, documented, and governable.

Production is not a single database. Production is this.

# Lore Introduction

*"The Academy has grown,"* the Chief Data Officer announces. *"When we had one hundred scholars, one database sufficed. Now we have ten thousand. And ten thousand scholars generate ten different kinds of data."*

*"Assessment results: structured, transactional, integrity-critical. Scholar social connections: graph-shaped, traversal-heavy. Spellcasting metrics: time-series, high-volume, append-only. Library catalogue annotations: document-shaped, flexible-schema, full-text-searchable. Real-time lesson events: streaming, sub-second latency required."*

*"One database cannot be all of these things well. We need a polyglot platform — the right storage for each kind of data, integrated into a coherent whole."*

She draws a blank architecture diagram on the board.

*"Design it. Then tell me what breaks if the document store goes down. What breaks if the message queue loses a message. What breaks if the DBA leaves."*

# Project Brief

Design a **Senior Data Platform Architecture** for the Academy at scale. This is a design and architecture project — you will produce a design document, architecture diagrams (described in text if you cannot draw), and SQL/pseudo-code where specific implementation is required.

---

## Part 1: Technology Selection

The Academy requires storage for five distinct data domains. For each, recommend a storage technology from this list: **PostgreSQL** (relational), **MongoDB** (document), **Neo4j** (graph), **InfluxDB / TimescaleDB** (time-series), **Redis** (key-value / cache), **Apache Kafka** (message streaming), or **Amazon S3 / object storage** (cold archive).

| Data Domain | Description | Volume | Access Pattern |
|---|---|---|---|
| Assessment results | Scholar scores, attempts, completion dates | 50M rows, growing 5M/year | Structured queries, JOINs, reporting |
| Scholar social graph | Follows, mentoring relationships, study groups | 2M nodes, 15M edges | Traversal (friends of friends, shortest path) |
| Live spellcasting metrics | Every spell cast: timestamp, scholar, spell type, energy consumed | 500K events/day | Time-range queries, aggregations, retention 90 days |
| Library annotations | Scholar notes on scrolls: flexible schema, tags, full-text | 10M documents | Full-text search, tag filtering, flexible schema |
| Real-time lesson events | Started, completed, abandoned — sub-second | 200K events/day | Streaming, event-driven downstream consumers |

**Your task:**
1. Select a storage technology for each domain and justify the choice with explicit reference to: the access pattern, the CAP theorem trade-offs you are accepting, and one specific risk of your choice.
2. Identify which two data domains have the most complex integration requirements (i.e., data needs to flow between them) and describe the integration approach.

---

## Part 2: Batch Processing Pipeline

Design an overnight batch pipeline that produces a **Daily Scholar Performance Report** — a summary table containing, for each scholar: total XP earned today, lessons completed, average score, and rank within their faculty.

1. Describe the pipeline stages: what is extracted, from which source, what transformation is applied, and what the load target looks like.
2. Write pseudo-SQL (or real SQL if you have a target database) for the transformation logic, including the window function used for faculty ranking.
3. Define the error handling: if 5% of records fail transformation, does the pipeline abort or proceed with partial results? What is logged, and who is alerted?
4. The pipeline runs at 02:00 daily. What happens if it runs at 02:00 and the source database is locked due to an overnight maintenance window? Describe the retry and idempotency strategy.

---

## Part 3: Streaming Data Flow

Design a real-time event processing flow for lesson events. When a scholar starts a lesson, a `lesson_started` event is emitted. When they complete it, a `lesson_completed` event is emitted.

1. Describe the event schema for both event types (what fields are in the payload).
2. Describe the message queue topology: how many topics, what is the partitioning strategy, and what delivery guarantee (at-most-once, at-least-once, exactly-once) do you require for each consumer?
3. Identify two downstream consumers of these events and describe what each does with the data (e.g. real-time leaderboard update; fraud/anomaly detection for unusual completion patterns).
4. What happens if the message queue is down for 30 minutes? Which downstream systems are affected, and how do they recover when the queue comes back?

---

## Part 4: High Availability and Disaster Recovery

The Academy's operational systems must not go down. Define the HA/DR requirements for the two most critical data stores (your choice).

1. For each, specify:
   - **Replication strategy:** synchronous or asynchronous? How many replicas?
   - **Failover:** automatic or manual? How long is the failover process?
   - **RTO** (Recovery Time Objective): how long can this system be down?
   - **RPO** (Recovery Point Objective): how much data loss is acceptable?
2. Design the backup strategy: frequency, retention period, and how you would test that backups are restorable. (Untested backups are not backups.)
3. Define the runbook for a complete database failure scenario: who is paged, in what order, and what are the first three actions taken?

---

## Part 5: Data Governance

A data audit has flagged three issues:
- Scholar email addresses are stored unencrypted in the assessment results database
- Library annotations are retained indefinitely with no deletion policy
- No one knows who "owns" the spellcasting metrics data — who is responsible for its quality and access decisions

1. Address each issue with a specific technical or process control. For email addresses: what encryption approach would you apply at the storage layer? For annotations: what retention policy would you implement, and what happens to annotations when a scholar account is deleted? For metrics: what does a "data owner" role mean in practice, and what decisions does the owner make?
2. The Academy must comply with a fictional data protection regulation that requires: (a) scholars can request deletion of all their personal data, (b) data older than 7 years must be archived or deleted, (c) all access to personal data must be logged. For each requirement, describe the technical implementation across the platform.

---

## Part 6: Design Document

Write a **2-page design document** (approximately 600-800 words) addressed to the Academy's non-technical Council. It should:
- Explain in plain language what the platform does and why it is needed
- Describe what happens when something goes wrong (in terms a non-engineer can understand)
- State the three most important decisions the Council needs to make (e.g. budget for redundancy, acceptable downtime, data retention periods) and the consequences of each choice
- Not use any technical jargon without explaining it in plain English on first use

---

## Part 7: Written Reflection

Answer the following in 400-600 words:

1. Your technology selection made trade-offs. For your two hardest decisions, describe the alternative you rejected and the conditions under which the rejected choice would have been better.
2. The streaming pipeline uses at-least-once delivery. Describe a scenario where a duplicate event causes a real problem in a downstream consumer, and how you would design the consumer to be idempotent.
3. Six months from now, the Academy acquires a second campus with its own data platform. What is the hardest integration challenge, and what approach would you take?

---

# Lore Conclusion

*"The Chief Data Officer reads through the design document twice."*

*"'The time-series choice for spellcasting metrics is defensible. The graph database for the social layer is correct. The streaming topology is sound.'"*

*"She pauses at the HA/DR section."*

*"'You've set an RTO of four hours for the assessment database. That means if it fails during the Final Examinations, four hundred scholars cannot submit their results for four hours. Is that acceptable?'"*

*"You hadn't considered the examination schedule. The RTO is probably wrong."*

*"'That's the answer I wanted,'* she says. *'Not the correct RTO — that depends on budget I haven't told you yet. I wanted you to know that the RTO is a business decision, not a technical one. You design the mechanism. The Council decides the target.'"*

*"The platform is not finished. It is never finished. But it is designed — and a designed thing can be improved. An undesigned thing can only be survived."*

---
