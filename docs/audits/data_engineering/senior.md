# Audit — Data Engineering · Senior

**Auditor lens:** Principal Data Architect / Staff Data Engineer evaluating whether this tier produces a candidate I would promote to Senior DE or hire at a senior level — someone who can design polyglot data platforms, reason authoritatively about distributed system trade-offs, architect data pipelines at scale, own governance and stewardship, and lead technical discussions on NoSQL technology selection, HA/DR, streaming, and data ethics.

**Tier mandate:** Produce a senior-interview-ready data engineer with deep knowledge of NoSQL (document, column-store, key-value, graph), streaming data (Kafka, windowing, event-time), batch vs ELT architectures, polyglot persistence, partitioning and sharding, replication and HA/DR, business intelligence and reporting, data ethics and stewardship.

**Scope:** 29 lessons across 27 topics.

---

## 1. Verdict at a glance

The Senior tier is excellent — the strongest of the four tiers in terms of technical depth and production-authentic examples. The lessons examined (streaming_data, document_databases, etl, partitioning, replication, data_ethics) all reach the level of quality I would expect in a Staff Engineer's internal tech talk: technically precise, production-grounded, intellectually honest about trade-offs, and free of the oversimplifications that plague most online curriculum at this level. The streaming lesson covers Kafka partition/consumer group semantics, event time vs processing time, watermarks, and all three windowing types — this is exactly what senior data engineers need. The partitioning lesson correctly distinguishes partitioning from sharding, covers partition pruning defeats, and includes O(1) DETACH as a maintenance benefit. The replication lesson covers synchronous vs asynchronous RPO/RTO trade-offs, pg_stat_replication monitoring, and Patroni failover automation. The data ethics lesson is among the best on the entire platform — it names the impossibility theorem, proxy discrimination, and feedback loops. The Senior project (de-sen-m8-01, The Polyglot Data Platform) is outstanding — a genuine integration challenge across all modules. Weaknesses: the ETL topic (de-sen-m4-01) is strong but Change Data Capture (CDC) is mentioned only briefly; the streaming lesson does not cover Kafka Streams or ksqlDB as first-class options alongside Flink; the column-store lesson should discuss Parquet/ORC internals more; and the governance module (ownership, stewardship, compliance) is solid but lacks explicit data lineage tooling coverage (OpenLineage, DataHub). Scores: **Coverage 4.5/5, Rigor/Depth 5/5, Sequencing 4.5/5, Practice quality 4.5/5.**

---

## 2. KEEP — strengths to preserve

- **de-sen-m4-03 (streaming_data):** The Kafka message structure, consumer group partition assignment, event time vs processing time divergence (with offline mobile device example), watermark formula, and all three windowing types are present and accurate. The architecture diagram (PostgreSQL → Debezium CDC → Kafka → Flink → ClickHouse) is exactly right for a modern streaming platform. Common mistakes (processing time as event time, no backpressure, exactly-once semantics) are production-authentic.
- **de-sen-m3-01 (document_databases):** The "47 nullable columns" hook is perfect. Embedding vs referencing decision criteria, single-document atomicity, multi-document transaction overhead, and MongoDB's aggregation pipeline as relational algebra are all present. The "when relational wins" table is honest and balanced.
- **de-sen-m4-01 (etl):** Idempotency via staging+MERGE, watermark advancement only after successful load, silent record rejection anti-pattern, and the Airflow DAG with dependency graph are all present. The "three bugs in the code snippet" solo practice is an excellent assessment format — forces active debugging rather than passive recall.
- **de-sen-m2-01 (replication):** Synchronous vs asynchronous trade-offs, RPO/RTO framework, pg_stat_replication monitoring, read-your-own-writes problem, logical vs physical replication distinction, and Patroni/repmgr failover automation are all covered. The Spring Boot read routing example (`@Transactional(readOnly=true)`) is practically grounded.
- **de-sen-m2-03 (partitioning):** Correctly separates partitioning (within-node) from sharding (cross-node). Covers range/list/hash strategies, partition pruning rules (function wrapping defeats pruning), O(1) DETACH, and local vs global indexes. The default partition anti-pattern omission is noted.
- **de-sen-m6-04 (data_ethics):** The impossibility theorem (Chouldechova, Kleinberg), proxy discrimination under UK Equality Act, feedback loops, Fairlearn/Aequitas audit tooling, and the engineer's professional responsibility framing are all correct and above what most programs teach at any level.
- **de-sen-m8-01 (mini_project / The Polyglot Data Platform):** The 7-part structure requiring technology selection with CAP theorem reasoning, batch pipeline design with idempotency strategy, streaming topology with delivery guarantees, HA/DR with RTO/RPO targets, data governance with ownership and retention, a non-technical design document, and a written reflection is a genuinely rigorous integration exercise. The Chief Data Officer character who challenges the RTO choice is excellent pedagogical design.
- **de-sen-m4-02 (elt):** ELT's separation from ETL, the modern cloud warehouse model (dbt as the T in ELT), and the architectural implication (preserve raw data, transform in warehouse) are all present and correct.
- **de-sen-m7-01 through de-sen-m7-04 (backups / recovery / high_availability / disaster_planning):** HA/DR coverage at this level is rare. RTO vs RPO as business decisions (not engineering decisions) is exactly the right framing, as demonstrated by the Polyglot project's capstone scene.

---

## 3. CHANGE — restructure / resequence

- **de-sen-m6 (Data Governance — ownership, stewardship, compliance, data_ethics):** The governance module appears after the pipeline and NoSQL modules. Governance thinking should permeate earlier modules — data quality, ownership, and compliance are relevant when designing pipelines and choosing NoSQL databases. Consider adding governance considerations as explicit sections within m3 and m4 lessons. The module is correct as a dedicated deep-dive but a forward reference in m1-m4 would reinforce the principle that governance is not a late-stage concern.
- **de-sen-m1 (monolithic_databases → shared_databases → service_oriented_data → polyglot_persistence):** This module frames the journey from monolith to polyglot — correct positioning as the entry module for Senior. However, the CAP theorem and eventual consistency (de-sen-m2-04) logically precede the NoSQL selection decisions in m3 (document, key-value, column-store, graph). **Resequence: move eventual_consistency (de-sen-m2-04) to Module 1 as the theoretical foundation before NoSQL technology selection.**
- **de-sen-m5 (metrics_design → business_intelligence → data_modelling_for_analytics → reporting_systems):** The analytics/BI module is placed after pipeline (m4) but before governance (m6). This is reasonable but analytics modelling (de-sen-m5-03) builds on dimensional modelling taught at Junior — the prerequisite link should be explicit. **Add explicit prerequisite annotation referencing the Junior star schema and SCD lessons.**

---

## 4. UPDATE — depth / rigor / currency

- **de-sen-m3-04 (column_stores):** Should explicitly cover Parquet and ORC file formats as the dominant open-format column stores (not just database-native column stores like Redshift). The Delta Lake / Apache Iceberg table format revolution (ACID semantics on a data lake) is the current state of the industry and belongs here. **Update: add Parquet/ORC, Delta Lake, and Iceberg as a modern data lakehouse section.**
- **de-sen-m4-03 (streaming_data):** Kafka Streams and ksqlDB are not covered alongside Flink. While Flink is the correct choice for complex stateful streaming, Kafka Streams is more accessible and widely used in organisations already on Kafka. **Update: add a Kafka Streams section comparing it to Flink for use-case selection.**
- **de-sen-m4-01 (etl):** Change Data Capture (Debezium) is mentioned in the streaming lesson but ETL's own CDC section is thin — the architectural difference between polling-based extraction (watermark) and log-based extraction (CDC) deserves a dedicated subsection in the ETL lesson. **Update: expand CDC (Debezium, AWS DMS, log-based capture) as a full section in the ETL lesson.**
- **de-sen-m3-02 (key_value_stores):** Redis is the dominant key-value store in DE contexts but the lesson likely lacks coverage of Redis data structures (sorted sets, streams, pub/sub), Redis clustering, and Redis vs Memcached trade-offs. Redis Streams as a lightweight Kafka alternative is also relevant. **Update: add Redis data structure variety and Redis Cluster vs single-node trade-offs.**
- **de-sen-m3-03 (graph_databases):** Neo4j and Cypher query language should be covered with at least two to three realistic query examples. Property graphs vs RDF triplestores is a distinction worth drawing at this level. **Update: verify Cypher query examples are present and add property graph vs RDF distinction.**
- **de-sen-m6-02 (stewardship):** Data lineage tooling (OpenLineage, DataHub, Marquez, Apache Atlas) is likely absent or thin. For a Senior DE, being able to evaluate and deploy lineage tooling is a real expectation. **Update: add data lineage tooling evaluation section.**
- **de-sen-m5-01 (metrics_design):** The distinction between vanity metrics and actionable metrics, and the DORA/OKR frameworks for data engineering team metrics, would strengthen this lesson. **Update: add DORA metrics and the vanity vs actionable metric distinction.**

---

## 5. REMOVE — cut or merge

- **de-sen-m1-01 (monolithic_databases) and de-sen-m1-02 (shared_databases):** If these are primarily historical context (RDBMS monolith → service-oriented data), they risk being more philosophy than practical skill. They are appropriate as a two-lesson sequence establishing why polyglot persistence exists, but should not exceed their scope. **Merge into a single "Evolution from Monolith to Polyglot" lesson if content overlaps.**
- No other removals recommended. The 29 lessons are all appropriately scoped for Senior. The risk is not excess but insufficient depth in a few technical areas.

---

## 6. GAPS — missing canonical topics

| Topic | Why essential at this tier | Suggested placement |
|---|---|---|
| **Data lakehouse architecture (Delta Lake, Iceberg, Apache Hudi)** | The data lakehouse pattern (ACID semantics on open-format object storage) has replaced traditional data warehouses at scale in most modern data platforms. A Senior DE cannot be ignorant of this architecture. Delta Lake time travel, schema evolution, and MERGE operations are interview-standard topics. | New lesson in m4 (Data Pipelines) or extend column_stores |
| **CDC with Debezium (log-based change data capture)** | Only briefly mentioned in streaming. A dedicated lesson on Debezium architecture (connector, change event format, transaction log integration), connector configuration, and failure modes is a Senior-level expectation. | New lesson in m4 or extend etl |
| **Data observability and pipeline monitoring** | Great Expectations, dbt tests, and Montecarlo-style data observability are the industry standard for detecting data quality issues in production. A Senior DE must be able to implement and operate these. Missing entirely. | New lesson in m4 or m6 |
| **Data quality SLAs and SLOs** | Defining, measuring, and alerting on data quality SLAs (freshness, completeness, accuracy targets) is a Senior responsibility. Absent from governance module. | New lesson in m6 (governance) |
| **Apache Spark fundamentals** | Spark is the dominant batch processing engine and appears in the mini project implicitly but is not a named lesson topic. Understanding RDDs vs DataFrames, partitioning in Spark, shuffle operations, and Spark execution model is expected at Senior level. | New lesson in m4 (Data Pipelines) |
| **Data contracts** | Formalised schema contracts between producers and consumers (using tools like Protobuf, Avro, OpenAPI, or Serde Schema Registry) prevent the producer-breaks-consumer problem at scale. This is an emerging but rapidly adopted Senior-level concept. | New lesson in m1 or m6 |
| **Cost optimisation for cloud data platforms** | Cloud warehouse cost management (Snowflake credits, BigQuery slot utilisation, Redshift WLM) is a practical Senior-level responsibility rarely taught in curricula. | New lesson in m4 or m5 |

---

## 7. PRACTICE & ASSESSMENT

Practice quality at the Senior tier is high. The format shift — from structured YAML guided steps to narrative solo practice questions — is appropriate for the level. The "three bugs in the code snippet" ETL solo and the "40-ticket platform team" scenario in the mini project are both excellent authentic problem formats.

Gaps:
1. **The mini project (de-sen-m8-01) is genuinely ambitious** but is a single 360-minute solo exercise. An intermediate integration challenge between modules 3 and 4 (e.g., "choose the right database for three different access patterns and justify with CAP theorem reasoning") would prevent all synthesis pressure accumulating at the capstone.
2. **Architecture review practice is absent.** Senior DEs are expected to present architecture decisions, respond to technical challenges, and write Architecture Decision Records. A practice task that asks learners to write an ADR for a technology choice and defend it would be valuable. (This exists in Lead tier — consider mirroring the pattern at Senior.)
3. **No explicit observability/monitoring practice.** Learners write pipelines but are never asked to design alerts, dashboards, or runbooks. The stewardship lessons mention monitoring but solo practice does not require a monitoring design.

---

## 8. Prioritized action list

1. **Add:** Data lakehouse architecture lesson (Delta Lake, Iceberg, Hudi — ACID on object storage, time travel, schema evolution) in module 4 or extend column_stores. Highest market relevance gap.
2. **Add:** Apache Spark fundamentals lesson (DataFrame API, partitioning, shuffle, execution model) in module 4 — required for the polyglot platform project and most senior interviews.
3. **Add:** Data observability lesson (Great Expectations, dbt tests, freshness/completeness/accuracy SLOs, Montecarlo-style anomaly detection) in module 4 or 6.
4. **Add:** CDC deep-dive (Debezium architecture, change event format, connector failure modes) as a standalone lesson in module 4, expanding on the ETL lesson's brief mention.
5. **Update:** Expand column_stores (de-sen-m3-04) to include Parquet/ORC file formats, dictionary encoding internals, and the data lakehouse table format comparison.
6. **Update:** Add Kafka Streams comparison to streaming_data (de-sen-m4-03) for organisations choosing between Flink and Kafka Streams.
7. **Add:** Data contracts lesson (Avro/Protobuf schemas, Schema Registry, contract testing between producers and consumers) in module 1 or 6.
8. **Change:** Move eventual_consistency (de-sen-m2-04) to module 1 as theoretical grounding before NoSQL selection in module 3.
9. **Update:** Add data lineage tooling section (OpenLineage, DataHub, Marquez) to stewardship lesson (de-sen-m6-02).
10. **Add:** Intermediate synthesis exercise between modules 3 and 4 requiring CAP theorem reasoning for multi-database technology selection — to distribute integration practice rather than concentrating it all at the capstone.
