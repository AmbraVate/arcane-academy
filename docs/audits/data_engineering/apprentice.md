# Audit — Data Engineering · Apprentice

**Auditor lens:** Principal Data Engineer reviewing foundational curriculum for accuracy, completeness, and conceptual soundness — evaluating whether a graduate could confidently describe what data is, read and write basic SQL, and reason about relational database design.

**Tier mandate:** Produce a learner who understands what data is, can write correct SQL for common single- and multi-table queries (SELECT/WHERE/GROUP BY/aggregation/JOINs), grasps relational table design (keys, relationships, normalisation basics), and can articulate basic data quality concepts.

**Scope:** 69 lessons across 16 topics.

---

## 1. Verdict at a glance

The Apprentice tier is genuinely strong. The pedagogical structure — Hook, Lore, Core Learning, Worked Examples, Common Mistakes, Mental Model, Mini Summary, Guided Practice, Solo Practice, Integration — is consistently executed at a high level. Every lesson examined had well-calibrated guided steps, realistic worked SQL examples, and solo assessment rubrics that demand synthesis rather than regurgitation. The progression from `what_is_data` through tables, keys, relationships, SQL reading, transforming, joining, and data quality is coherent and covers the mandate adequately. The mini project (de-app-m8-01) synthesises all topics effectively. Where the tier falls short is in normalisation depth (1NF/2NF/3NF is not explicitly covered as a named concept), the absence of any DDL practice (learners never write CREATE TABLE themselves until the mini project), and a thin coverage of data types (VARCHAR/INT/DATE mentioned but no systematic lesson on type selection trade-offs). The `asking_better_questions` topic is misplaced here and belongs above this tier. Scores: **Coverage 4/5, Rigor/Depth 4/5, Sequencing 4/5, Practice quality 4.5/5.**

---

## 2. KEEP — strengths to preserve

- **de-app-m1-01 (what_is_data / data_vs_information):** Exceptional opening lesson. The data/information/context/interpretation framework is conceptually precise. The "37" example is memorable and pedagogically sound. The integration with mathematics (relational algebra origins) and psychology (sensation vs perception) elevates the lesson beyond typical intro content. Keep as the canonical model for lesson writing across the platform.
- **de-app-m2-01 (tables / what_is_a_table):** Correctly grounds tables in Codd's relational model, distinguishes schema from instance, and avoids the spreadsheet conflation error explicitly. Schema-first, data-second framing is exactly right.
- **de-app-m2-06 (keys / primary_keys):** Rigorous. Covers surrogate vs natural key trade-offs, composite PKs, and the mutability risk with real-world examples. The reflection prompts ("what happens if a customer with 1,000 orders changes their email?") build the right intuitions.
- **de-app-m3-01 through de-app-m3-09 (reading_data + transforming_data):** The SELECT → WHERE → ORDER BY → GROUP BY progression is well paced. Each lesson isolates one concept. The NULL handling in transforming_data topics is appropriate at this level.
- **de-app-m3-10 through de-app-m3-15 (aggregation):** COUNT variants (COUNT(*) vs COUNT(col) vs COUNT(DISTINCT)) are explained with production-relevant precision. The "census counter" mental model is excellent. SUM/AVG/MIN/MAX covered cleanly.
- **de-app-m4-01 through de-app-m4-05 (understanding_joins):** The N+1 problem framing for "why joins exist" is well chosen and production-relevant. The progression from concept to INNER JOIN to LEFT/RIGHT JOIN is correct.
- **de-app-m1-06 through de-app-m1-10 (data_modelling_foundations):** Entities, attributes, relationships, and the distinction between entity type and entity instance are taught rigorously. Weak entities are introduced correctly.
- **de-app-m8-01 (mini_project / the_village_ledger):** Outstanding capstone. Requires schema design, data population, multiple query types, and a written reflection on design trade-offs. The "Step 3: Improve the Design" section that asks about schema changes, missing constraints, and performance anticipates real-world thinking.
- **de-app-m5-01 through de-app-m5-08 (data_accuracy + data_integrity):** Treating data quality as a first-class topic at the apprentice level is exactly right. The constraint-based approach (NOT NULL, UNIQUE, CHECK, FK) maps correctly to what relational databases actually enforce.

---

## 3. CHANGE — restructure / resequence

- **de-app-m7-01 through de-app-m7-04 (asking_better_questions):** These lessons belong in the Junior tier (or as a bridge module). "Asking better questions of data" implies already having basic SQL; this topic should follow a learner who has completed data retrieval and joins, not precede or accompany it at the apprentice level. At the Apprentice level this risks feeling abstract without enough query experience to draw on. **Move to Junior tier as a Junior introduction module.**
- **de-app-m6-01 through de-app-m6-09 (introduction_to_design + design_practice):** These appear after SQL modules (m3, m4) but introduce data modelling concepts that should logically precede SQL. Designing tables should come before querying them. **Resequence: move design topics (m6) before SQL reading (m3).**
- **de-app-m5 (data_accuracy + data_integrity):** Quality concepts appear mid-curriculum. Introductory constraint material (NOT NULL, UNIQUE, CHECK) would be better placed immediately after keys and relationships (after m2), so learners apply constraints as they learn to create tables rather than as a separate later block. **Resequence to follow m2 (keys/relationships).**

---

## 4. UPDATE — depth / rigor / currency

- **de-app-m2-01 through de-app-m2-05 (tables):** The CREATE TABLE syntax is shown but never written by the learner until the mini project. Solo practice should include writing a CREATE TABLE statement with deliberate constraints from lesson 2 onwards, not just at the capstone. **Update: add DDL writing to solo practice in tables and keys topics.**
- **de-app-m2-10 through de-app-m2-13 (relationships):** One-to-one, one-to-many, and many-to-many relationships are covered but normalisation (1NF, 2NF, 3NF) is never named. A graduate cannot discuss "is this table normalised?" without this vocabulary. **Update: add a dedicated lesson on 1NF/2NF/3NF with the language needed to discuss normalisation.**
- **de-app-m3 (reading_data through aggregation):** No lesson addresses LIMIT/OFFSET or pagination — a fundamental practical skill. **Update: add LIMIT/OFFSET to the reading_data or practical_data_retrieval block.**
- **de-app-m4-01 through de-app-m4-05 (understanding_joins):** CROSS JOIN and SELF JOIN are absent. CROSS JOIN is the anti-pattern ("what happens without ON?") and SELF JOIN is needed for hierarchical data. **Update: add a lesson on CROSS JOIN risks and SELF JOIN patterns.**
- **de-app-m2 (tables):** Data type trade-offs (VARCHAR vs TEXT, INT vs BIGINT, DECIMAL vs FLOAT, DATE vs TIMESTAMP) are mentioned in passing but never treated as a dedicated lesson. A graduate who picks FLOAT for a price column will cause real production incidents. **Update: add a dedicated data types lesson with precision/scale for DECIMAL.**

---

## 5. REMOVE — cut or merge

- **de-app-m7 (asking_better_questions — 4 lessons):** As described in section 3, this topic is conceptually above the Apprentice mandate. Rather than remove entirely, **merge/move to Junior** — the question decomposition and business-context framing belongs after a learner can write multi-table queries.
- **No full removals recommended.** All remaining topics are appropriately scoped. The tier is well-constructed; the issue is sequencing and a few gaps, not excess.

---

## 6. GAPS — missing canonical topics

| Topic | Why essential at this tier | Suggested placement |
|---|---|---|
| **Normalisation (1NF, 2NF, 3NF)** | Without the vocabulary of normal forms, a graduate cannot reason about why tables are structured the way they are, or diagnose redundancy. Every professional interview for a junior DE role asks about normalisation. Essential pre-join concept. | After relationships (de-app-m2-13), before SQL modules |
| **Data types and precision trade-offs** | Choosing FLOAT for monetary values is a production incident waiting to happen. VARCHAR length, DECIMAL(p,s), DATE vs TIMESTAMP, TEXT vs VARCHAR — all need explicit treatment. | As a new lesson in tables module (de-app-m2) |
| **NULL semantics** | NULL handling in SQL is notoriously counterintuitive (NULL != NULL, IS NULL vs = NULL). A dedicated lesson on NULL behaviour in WHERE, JOIN, and aggregation is essential for any SQL practitioner. The topic is touched but deserves a standalone lesson. | After reading_data, before aggregation |
| **CREATE TABLE / DDL writing** | Learners read and interpret CREATE TABLE but never write it until the mini project. Hands-on DDL should begin from lesson 2. ALTER TABLE (add column, add constraint) is absent entirely. | Integrated into tables and keys topics |
| **LIMIT / OFFSET and pagination** | Fundamental practical SQL skill missing from the module. | practical_data_retrieval block |
| **CASE expressions** | CASE WHEN...THEN...ELSE...END is one of the most commonly used SQL constructs. It belongs at the Apprentice level as part of transforming_data. | transforming_data block |

---

## 7. PRACTICE & ASSESSMENT

The practice quality is the strongest aspect of this tier. Guided steps are well-calibrated with appropriate fill-blank/MCQ/short-text variety. Solo assessments with RUBRIC_REFLECTION and explicit rubric items are the correct mechanism. The mini project is exemplary.

Two weaknesses:
1. **No live SQL execution environment is referenced.** Learners are instructed to write queries but there is no sandboxed SQL environment. Solo practice requires self-verification against a real database. The platform should provide or clearly link to a runnable SQL environment (SQLite in browser, a hosted PostgreSQL instance, etc.).
2. **Retrieval practice (recall/explain/mistakeId)** is consistently well-designed. The `mistakeId` pattern — identifying a common incorrect code pattern and explaining the correct reasoning — is an excellent pedagogical technique that should be expanded. Consider adding a second `mistakeId` per lesson for topics where multiple common mistakes exist (e.g., JOIN without ON, SELECT * performance implications).

---

## 8. Prioritized action list

1. **Add:** Dedicated 3NF normalisation lesson (1NF/2NF/3NF vocabulary, worked examples, update anomalies) — placed after de-app-m2-13. Highest gap risk for junior interviews.
2. **Update:** Add DDL writing (CREATE TABLE, ALTER TABLE) to solo practice from de-app-m2-01 onwards, not only at the capstone.
3. **Add:** Dedicated NULL semantics lesson covering NULL in WHERE conditions, aggregation, and JOIN behaviour — placed between reading_data and aggregation.
4. **Add:** Data types lesson (DECIMAL(p,s) for money, INT vs BIGINT, DATE vs TIMESTAMP, TEXT vs VARCHAR) in the tables module.
5. **Change:** Resequence introduction_to_design / design_practice (m6) to precede SQL modules (m3/m4) — table design before querying.
6. **Add:** CASE WHEN expression lesson in the transforming_data block.
7. **Add:** LIMIT/OFFSET and pagination lesson in practical_data_retrieval.
8. **Move:** asking_better_questions (de-app-m7) to Junior tier as a bridge introduction module.
9. **Update:** Add CROSS JOIN anti-pattern and SELF JOIN pattern as a lesson in understanding_joins.
10. **Update:** Resequence data_accuracy/data_integrity (m5) to follow keys/relationships (m2), so constraints are taught at the point of schema design, not as a separate later block.
