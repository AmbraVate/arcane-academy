# Audit — Data Engineering · Junior

**Auditor lens:** Principal Data Engineer / hiring manager evaluating whether this curriculum produces a graduate I would hire as a junior data engineer — someone who can contribute to a production data team from week one, writing advanced SQL, reasoning about transactions and indexes, building basic ETL, and implementing dimensional models.

**Tier mandate:** Produce a job-ready junior data engineer proficient in advanced SQL (window functions, CTEs, subqueries, set operations, stored procedures), dimensional modelling (star/snowflake schemas, SCD), ETL fundamentals, query performance (indexes, execution plans), ACID/transactions, ORM/JDBC, basic security (access control, encryption), and integration testing.

**Scope:** 34 lessons across 38 topics. Note: topic count (38) exceeds lesson count (34), suggesting some topics share a lesson file or the topic list includes sub-topics.

---

## 1. Verdict at a glance

The Junior tier is strong and covers the mandate comprehensively. The module structure is well-organised: Advanced SQL (m1), Database Programming (m2), Transactions (m3), Indexing and Performance (m4), ORM/JDBC (m5), Data Warehousing Foundations (m6), Security (m7), and Testing (m8). Each lesson examined is technically accurate, practically grounded, and demonstrates senior-level understanding communicated at the right level. The ACID lesson (de-jun-m3-01) and star schema lesson (de-jun-m6-02) are among the best curriculum pieces I have reviewed at this level anywhere. The dimensional modelling coverage (OLTP vs OLAP → star schema → fact tables → dimension tables → slowly changing dimensions) is a complete and accurate treatment. Window functions (de-jun-m1-03) hit the mark — RANK/DENSE_RANK, LAG/LEAD, running totals, and frame clauses are all present. Two notable weaknesses: ETL fundamentals is listed as a topic in the topic list but there is no `etl_fundamentals` lesson file — the ETL content lives at Senior tier (de-sen-m4-01). Additionally, the `connection_patterns`, `ctes`, `orm_patterns`, `query_safety`, `slowly_changing_dimensions`, `star_and_snowflake_schema`, and `dimensional_modelling` appear in the topic list but have no corresponding lesson files — these topics may be covered within adjacent lessons, but this is a structural risk. Scores: **Coverage 4/5, Rigor/Depth 4.5/5, Sequencing 4.5/5, Practice quality 4/5.**

---

## 2. KEEP — strengths to preserve

- **de-jun-m1-03 (window_functions):** Excellent. ROW_NUMBER/RANK/DENSE_RANK distinctions, NTILE, LAG/LEAD with month-over-month calculations, ROWS BETWEEN frame clause, and the "cannot use in WHERE — use CTE" rule are all present. The "top N per group" pattern is the most important real-world window function use case and is explicitly taught. The mathematics integration (discrete differences, prefix sums) is a genuine enhancement.
- **de-jun-m3-01 (acid):** The SAVEPOINT coverage, the WAL/durability connection, the AUTOCOMMIT danger, and the "pencil and ink" mental model are all excellent. The common mistake section (catching exceptions without ROLLBACK) is precisely the error junior engineers make. The finance integration example (Knight Capital, SWIFT) contextualises why this matters.
- **de-jun-m6-02 (star_schemas):** Outstanding. Sets the grain concept front-and-centre, covers surrogate vs natural keys in the warehouse context, explicitly shows SCD Type 1 and Type 2, and includes the date dimension as a first-class object. The "common mistakes" section (storing dimension attributes in fact table, using natural keys as FKs, no date dimension) are exactly the errors I see in production data warehouses. The solo practice is suitably ambitious.
- **de-jun-m6-01 (oltp_vs_olap):** Covers columnar vs row-oriented storage (I/O reduction mechanics), ELT vs ETL, and the cost of running analytics on OLTP. The compression mathematics integration (run-length encoding, dictionary encoding) is genuinely informative. Rare to see this level of technical depth at the junior level.
- **de-jun-m4-01 (indexes):** B-tree internals (O(log n) vs O(n)), cardinality/selectivity, composite index leading-column rule, covering indexes, partial indexes, write overhead, and the "don't index everything" trade-off are all present. The `pg_stat_user_indexes` monitoring query adds production realism.
- **de-jun-m3-01 through de-jun-m3-04 (ACID / isolation_levels / concurrency / locking):** A complete transaction module. Isolation levels with anomaly mapping (dirty read, non-repeatable read, phantom read) → isolation level selection is exactly what junior engineers need. The two-phase locking vs MVCC distinction at the concurrency lesson is appropriately advanced.
- **de-jun-m5-01 through de-jun-m5-04 (jdbc / orm_concepts / jpa_hibernate / repository_patterns):** Well-placed for a platform targeting Spring/Java backends. The N+1 query problem, lazy vs eager loading, and repository pattern are essential concepts for any junior working on a Java data stack.
- **de-jun-m8-01 through de-jun-m8-04 (data_validation / integration_testing / test_data_management / migration_testing):** Testing is treated as a first-class concern at the junior level. Migration testing is particularly notable — this is often omitted from data engineering curricula but causes real production incidents.

---

## 3. CHANGE — restructure / resequence

- **de-jun-m2-01 (stored_procedures) → position:** Stored procedures appear in module 2 (Database Programming) before transactions (module 3). Since stored procedures frequently contain transaction control (BEGIN/COMMIT/ROLLBACK), learners who haven't yet studied ACID will not fully understand the transaction examples inside the stored procedure lesson. **Resequence: move stored_procedures to after the transactions module (m3).**
- **de-jun-m6 (Data Warehousing Foundations):** The star schema / fact table / dimension table / SCD sequence within this module is correct, but the OLAP lesson (oltp_vs_olap) would benefit from being a bridge lesson between the Apprentice tier and the Junior module 6 — learners arriving from Apprentice have no warehouse context. Consider adding the OLTP vs OLAP lesson as the explicit entry point to the warehousing module with a prerequisite check that confirms Apprentice graduation.
- **Topic list / lesson file mismatch:** Topics `etl_fundamentals`, `connection_patterns`, `ctes`, `orm_patterns`, `query_safety`, `slowly_changing_dimensions`, `star_and_snowflake_schema`, `dimensional_modelling` appear in the module declaration but have no standalone lesson files. **Change:** Either create dedicated lesson files or explicitly map these topics to the lessons that cover them, to prevent content gaps from being invisible.

---

## 4. UPDATE — depth / rigor / currency

- **de-jun-m1-01 (subqueries):** Subqueries lesson exists but correlated subqueries vs lateral joins (LATERAL keyword) is not visible from available files. Correlated subqueries that run per-row are a common performance trap that junior engineers must recognise. **Update: add correlated subquery identification and the anti-pattern of using them where a JOIN suffices.**
- **de-jun-m4-02 (execution_plans):** The EXPLAIN / EXPLAIN ANALYZE pattern is covered but the lesson should explicitly include identifying Seq Scan vs Index Scan vs Index Only Scan vs Bitmap Heap Scan in output, and the cost model (rows × width × cost). Junior engineers reading their first EXPLAIN output are confused by the node tree structure. **Update: add annotated EXPLAIN output with node-by-node explanation.**
- **de-jun-m7-02 (encryption):** Encryption at rest (AES-256, TDE, column-level) and in transit (TLS) are both relevant but the lesson likely focuses on one. Encryption key management (rotation, HSM, KMS) is missing from most junior curricula and is a real gap. **Update: add key management lifecycle (rotation, external KMS services) as a dedicated topic within the encryption lesson.**
- **de-jun-m6-03 (fact_tables):** Additive, semi-additive, and non-additive facts — a fundamental distinction for anyone building a warehouse — should be explicitly named and worked through. SUM(balance) across time periods is the classic semi-additive trap. **Update: ensure the fact tables lesson names and illustrates all three measure additivity types.**
- **de-jun-m6-04 (dimension_tables):** Role-playing dimensions (one date dimension used as loan_date, due_date, and return_date with different aliases) are a canonical star schema technique that likely belongs here. **Update: add role-playing dimension pattern if not already present.**
- **de-jun-m2-03 (triggers):** Triggers are covered but the lesson should explicitly warn about the "invisible logic" problem — triggers that fire on DML are a common source of unexpected behaviour and debugging nightmares. When NOT to use triggers is as important as how to write them. **Update: strengthen the "when to avoid triggers" guidance.**

---

## 5. REMOVE — cut or merge

- **de-jun-m1-02 (common_table_expressions) and standalone `ctes` topic:** CTEs appear twice in the topic list (as both `common_table_expressions` and `ctes`). If there are two lesson files, one should be merged into the other — recursive CTEs can share a lesson with standard CTEs. **Merge: combine into one CTE lesson covering non-recursive and recursive CTEs.**
- **No full removals recommended.** The scope is appropriate for the junior mandate. The ORM/JDBC block (module 5) is platform-specific (Java/Spring) but justified given the platform's backend technology choice. A note in the module description should acknowledge that Python/SQLAlchemy equivalents exist for learners on different stacks.

---

## 6. GAPS — missing canonical topics

| Topic | Why essential at this tier | Suggested placement |
|---|---|---|
| **ETL fundamentals (watermark pattern, idempotency, staging tables)** | Listed as a topic but no lesson file found at Junior tier — the content exists only at Senior (de-sen-m4-01). A junior DE must understand basic pipeline construction: extract, transform, load, idempotency, failure handling. This is a job-day-one skill. | New lesson in a new Module 8 or within the mini project module |
| **Data pipeline orchestration basics (Airflow DAG concepts)** | Junior DEs are expected to understand and run scheduled pipelines. Even a conceptual introduction to DAGs, task dependencies, and schedule intervals is absent from Junior. | After or alongside ETL fundamentals |
| **Schema migrations in production (Flyway/Liquibase)** | Migration testing (de-jun-m8-04) exists but there is no lesson on designing backward-compatible migrations, the expand-contract pattern, and tooling (Flyway/Liquibase). Zero-downtime schema changes are a practical skill every junior DE needs on day 1. | New lesson in testing module or database programming module |
| **Correlated subqueries and their performance implications** | A canonical SQL pitfall that distinguishes competent from struggling junior engineers. | Extend de-jun-m1-01 (subqueries) |
| **Data cataloguing and metadata basics** | Even at the junior level, understanding what a data catalogue is (business glossary, technical metadata, lineage) prevents the common mistake of building pipelines with no discoverability. | New lesson in module 6 or module 7 |
| **HAVING clause** | Filtering on aggregate results is a foundational SQL skill that completes the GROUP BY picture. Should be in the Advanced SQL module but does not appear in any visible topic. | de-jun-m1 Advanced SQL block |
| **JSON/JSONB in SQL (semi-structured data in relational databases)** | PostgreSQL JSONB is ubiquitous in modern data engineering. Querying, indexing, and updating JSONB is a practical skill a junior DE needs. | New lesson in Advanced SQL or a dedicated module |

---

## 7. PRACTICE & ASSESSMENT

Practice quality is high. The guided steps consistently use realistic business scenarios (Archive loans, orders, member management). Solo practice tasks are ambitious — the window functions solo (de-jun-m1-03) asking for five complex queries including chained CTEs and NTILE-based revenue analysis is appropriate for the tier. The mini project (de-jun-m9-01) exists but was not directly sampled; the prerequisites list suggests it synthesises modules 1–8 correctly.

Two gaps:
1. **No cross-module integration practice until the mini project.** An intermediate synthesis task (combining window functions + CTEs + joins in a single realistic analytical problem) between module 3 and the mini project would help cement the skill combination.
2. **Testing practice is present (m8) but lacks data contract testing** — the practice of asserting that upstream data sources conform to expected schemas and value distributions before processing. This is a real junior-level skill gap in the industry.

---

## 8. Prioritized action list

1. **Add:** ETL fundamentals lesson (watermark pattern, idempotency, staging+MERGE load, Airflow DAG basics) — the most critical gap at this tier for job readiness.
2. **Add:** Schema migration lesson (Flyway/Liquibase, expand-contract pattern, zero-downtime strategies) — paired with the existing migration_testing lesson.
3. **Update:** Add correlated subquery anti-pattern explicitly to de-jun-m1-01 (subqueries).
4. **Update:** Add HAVING clause coverage to the Advanced SQL module (it is absent from visible topic list but fundamental to GROUP BY mastery).
5. **Add:** JSONB/JSON querying in PostgreSQL as a new Advanced SQL topic — modern junior DEs encounter semi-structured data in relational databases daily.
6. **Change:** Move stored_procedures (de-jun-m2-01) to after the transactions module (m3) — learners need ACID context before transaction control in procedures.
7. **Update:** Expand execution_plans lesson (de-jun-m4-02) with annotated EXPLAIN output showing all scan types and cost model interpretation.
8. **Add:** Data pipeline orchestration basics (DAG concepts, task dependencies, retry/alerting) as a bridge to the Senior streaming and batch lessons.
9. **Update:** Add role-playing dimension pattern and all three measure additivity types to dimension_tables and fact_tables lessons.
10. **Change:** Resolve topic/lesson file mismatch — audit all 38 topics against lesson files and create stubs or map to existing lessons.
