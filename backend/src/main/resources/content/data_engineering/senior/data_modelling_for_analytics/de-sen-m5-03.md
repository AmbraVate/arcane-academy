---
id: de-sen-m5-03
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m5
moduleTitle: "Module 5: Analytics Engineering"
moduleGlyph: "📊"
moduleSortOrder: 5
topicSlug: data_modelling_for_analytics
topicTitle: "Data Modelling for Analytics"
topicSortOrder: 3
lesson: 3
title: "Data Modelling for Analytics: Designing for Questions"
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
  - de-sen-m5-02
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains why analytical models are deliberately denormalised"
    - "Describes the grain of a fact table and why it must be explicitly declared"
    - "Explains Kimball vs Inmon methodology differences"
    - "Identifies the one-big-table (OBT) pattern and when it is appropriate"
  keywords:
    - fact table
    - dimension table
    - grain
    - denormalisation
    - Kimball
    - Inmon
    - OBT
    - slowly changing dimension
  modelAnswer: |
    Analytical models are denormalised deliberately — joins are expensive at query time on billions of rows, and analysts need wide, flat tables they can filter and aggregate without SQL expertise. A star schema pre-joins dimension attributes into wide dimension tables so a BI tool can apply a filter on `tier` without the analyst knowing about foreign keys.
    The grain of a fact table is the precise level of detail each row represents — "one row per lesson attempt by one user." The grain must be declared and enforced: if two different grains mix in one table, aggregations produce incorrect results. The most common error is aggregating across a joined dimension that doubles rows.
    Kimball methodology (dimensional modelling): star schemas, fact + dimension tables, designed bottom-up from reporting requirements. Fast for BI tools, easier for analysts. Inmon methodology: normalised enterprise data warehouse (3NF), data marts derived from it. Single source of truth, flexible, but slower query performance without pre-aggregated marts.
    The One-Big-Table (OBT) pattern denormalises everything into a single wide table — all dimension attributes joined directly onto the fact. Maximally simple for analysts; appropriate for smaller datasets, embedded analytics, or when BI tools lack good join support. Poor for large tables (redundant data, high storage cost) and when dimension attributes change frequently.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "A fact table has grain 'one row per lesson attempt'. A query JOINs it to dim_lesson which has one row per lesson. For lesson L with 3 modules (which is a separate join), the analyst accidentally uses a different JOIN that makes each lesson attempt row appear once per module. What happens to COUNT(*) of attempts?"
    options:
      - "Count stays the same — JOINs don't affect row counts when there's a primary key"
      - "Count is tripled — each attempt row fans out to 3 rows (one per module), inflating the count"
      - "Count is halved — duplicate detection removes inflated rows"
      - "The query fails with a key violation error"
    correctIndex: 1
    explanation: "This is 'fan-out' — a JOIN to a parent table with multiple children inflates the row count of the child table. Each attempt appears 3 times (once per module). COUNT(*) is tripled; SUM(xp_earned) is tripled. This is why grain must be defined and respected: every join must preserve the grain or be an explicit many-to-one join. The fix is to separate the module join into a separate subquery or use dim_module only for filtering, not fan-out."
  - type: FILL_BLANK
    question: "In a star schema, the central fact table stores ___ and foreign keys to surrounding dimension tables."
    answer: "measures (numeric additive values)"
    explanation: "The fact table stores measurable events at a specific grain — xp_earned, duration_seconds, amount_paid. These should be additive so SUM() across any dimension is meaningful. Dimension tables store descriptive attributes (name, category, tier) and are joined to the fact for filtering and grouping."
  - type: SHORT_TEXT
    question: "A lesson can change its difficulty level over time. An analyst wants to see completion rates by the difficulty the lesson had AT THE TIME OF the attempt. Which SCD type handles this, and how does it affect the fact table?"
    modelAnswer: "SCD Type 2 (slowly changing dimension). dim_lesson has a new row for each historical version: lesson_surrogate_key, lesson_id, difficulty, valid_from, valid_to. The fact table stores lesson_surrogate_key (pointing to the specific version at attempt time), not lesson_id. When the fact row is inserted, the ETL looks up the current surrogate key for the lesson. Future changes to difficulty create a new surrogate key; historical fact rows still point to the old version — preserving the historical relationship."
microCheckpoint:
  question: "What is the grain of a fact table and why must it be explicitly declared?"
  answer: "The grain is the precise level of detail each row represents. It must be declared because JOINs that violate the grain (fan-out) cause silent aggregation errors — SUM and COUNT return inflated numbers. The grain determines which JOINs are safe (many-to-one) and which will inflate rows (one-to-many)."
retrieval:
  recall: "What is the key difference between Kimball and Inmon data warehouse methodologies?"
  explain: "Explain why dimensional models are deliberately denormalised when OLTP models are deliberately normalised."
  mistakeId: "analytics-fan-out-join"
---

# The Wrong Aggregation

"The report says we had 14,000 lesson attempts this week," the Senior Engineer said. "But the raw events table has 4,700." The Lead Data Engineer pulled up the SQL. There it was — a JOIN to dim_lesson_module that had 3 modules per lesson. Every attempt was counted three times. "The grain of the fact table was violated. This is the most common analytics bug. Let's talk about how to prevent it."

# Why Analytics Models Are Different

Operational databases (OLTP) are normalised to avoid redundancy and enforce consistency. Analytical databases (OLAP) are deliberately denormalised to make queries fast and simple.

```
OLTP (3NF — normalised):
  lesson_attempts → lesson → domain → school
  (3 JOINs to answer "attempts by school")

OLAP (star schema — denormalised):
  fact_lesson_attempts: attempt_id, user_id, lesson_key, xp_earned, occurred_at
  dim_lesson: lesson_key, lesson_id, title, difficulty, domain_name, school_name
  (0 JOINs to filter by school — already on fact via dim_lesson)
```

Denormalisation pre-joins the hierarchy. Analysts filter on `school_name` without knowing the schema.

## Fact Tables

The fact table is the centre of the star. It records **events or measurements** at a specific **grain**.

```sql
-- fact_lesson_attempts
-- Grain: one row per attempt of a lesson by one user

CREATE TABLE fact_lesson_attempts (
    -- Surrogate keys (foreign keys to dimensions)
    attempt_id          BIGINT PRIMARY KEY,
    user_key            INT NOT NULL REFERENCES dim_user(user_key),
    lesson_key          INT NOT NULL REFERENCES dim_lesson(lesson_key),
    date_key            INT NOT NULL REFERENCES dim_date(date_key),

    -- Measures (additive facts)
    xp_earned           INT,
    duration_seconds    INT,
    score_pct           DECIMAL(5,2),

    -- Degenerate dimension (no corresponding dim table)
    session_id          UUID
);
```

**Rules for fact tables:**
- One row per grain event
- Measures should be additive (SUM across any dimension = valid result)
- Foreign keys to all relevant dimensions
- No descriptive attributes (those live in dimensions)

## Dimension Tables

Dimension tables carry descriptive attributes used for filtering and grouping.

```sql
-- dim_lesson (wide, denormalised)
CREATE TABLE dim_lesson (
    lesson_key      INT PRIMARY KEY,          -- surrogate key
    lesson_id       TEXT NOT NULL,            -- natural key (source system ID)
    title           TEXT,
    difficulty      INT,
    module_id       TEXT,
    module_title    TEXT,
    domain_id       TEXT,
    domain_name     TEXT,
    school_name     TEXT,
    valid_from      DATE,                     -- SCD Type 2
    valid_to        DATE,
    is_current      BOOLEAN
);
```

Wide and flat — all attributes of the lesson, its module, domain, and school are on one row. No JOIN required to get school_name from a lesson_key.

## Declaring the Grain

The grain is the most important design decision. Write it in plain English before writing a single column.

```
fact_lesson_attempts grain:
  "One row per (user, lesson, attempt_date) — each distinct attempt
   of a lesson by a user on a given day."

fact_lesson_completions grain:
  "One row per (user, lesson) — the first successful completion only."

fact_xp_events grain:
  "One row per individual XP award event — each badge, completion, 
   streak bonus is a separate row."
```

A grain violation (fan-out) occurs when a JOIN multiplies rows. It produces silently incorrect results.

```sql
-- WRONG: joining dim_lesson_module fans out each attempt × modules
SELECT COUNT(*) FROM fact_lesson_attempts
JOIN dim_lesson USING (lesson_key)
JOIN dim_lesson_module USING (module_id)  -- 3 modules per lesson → ×3 rows
-- COUNT(*) = 3× correct value

-- RIGHT: never join a one-to-many relationship off the fact table
-- If module is needed, put module_key directly on the fact table at load time
-- OR aggregate the module join separately
```

## Kimball vs Inmon

| | Kimball | Inmon |
|---|---|---|
| **Approach** | Dimensional (star schema) | Normalised (3NF EDW) |
| **Design starts from** | Business process / reporting needs | Enterprise data model |
| **Data marts** | First-class; each process has a mart | Derived from centralised EDW |
| **Query performance** | Fast (denormalised, analyst-friendly) | Slower without mart layer |
| **Flexibility** | Less flexible (schema-per-process) | More flexible (normalised) |
| **Best for** | BI dashboards, self-serve analytics | Enterprise data governance |

Modern analytics engineering often uses a hybrid: dbt implements a Kimball-style mart layer on top of a normalised staging layer.

## One-Big-Table (OBT)

For simple use cases or small datasets, denormalise everything into a single wide table.

```sql
-- OBT: lesson_attempts enriched with all dimension attributes
CREATE TABLE obt_lesson_attempts AS
SELECT
    la.attempt_id,
    la.occurred_at,
    la.xp_earned,
    la.duration_seconds,
    u.tier            AS user_tier,
    u.country         AS user_country,
    l.title           AS lesson_title,
    l.difficulty,
    l.domain_name,
    l.school_name
FROM fact_lesson_attempts la
JOIN dim_user    u ON u.user_key   = la.user_key
JOIN dim_lesson  l ON l.lesson_key = la.lesson_key;
```

**OBT advantages**: zero JOINs for analysts; works well in tools without good join support (some no-code BI tools, pandas).

**OBT disadvantages**: massive storage cost if fact table is large and dimensions are wide; dimension updates require rewriting the OBT; no history for slowly changing dimensions.

## Common Mistakes

> **Fan-Out Join**
> Joining a one-to-many relationship from a fact table multiplies rows silently. Always verify JOIN cardinality before aggregating. Use `SELECT COUNT(*) BEFORE AND AFTER JOIN` to detect fan-out.

> **Using Natural Keys Instead of Surrogate Keys**
> Natural keys from source systems can be reused or changed. SCD Type 2 requires surrogate keys to distinguish versions. Use surrogate keys (integers) in all fact-to-dimension JOINs.

> **Ignoring the Grain**
> Mixing two grains in one fact table (e.g. one row per attempt AND one row per session) makes all aggregations ambiguous. Split into two fact tables with different grains.

## Mental Model

Think of a star schema as a **telescope with lens adapters**. The fact table is the eyepiece — it captures every light photon (event) at full resolution. The dimension tables are the lens adapters (filters) — they let you view by wavelength (domain), by telescope (school), by time period (date). The grain is the resolution of the eyepiece: if you declare "one photon per wavelength", mixing in multi-wavelength composites corrupts the measurement. Keep the grain pure and the dimensions clean.

**Mini Summary**: Analytical models denormalise deliberately for query speed and analyst accessibility. Fact tables store measures at a declared grain; dimension tables store descriptive attributes. Violating the grain via fan-out JOINs produces silently incorrect aggregations. Kimball (star schema) is bottom-up from business processes; Inmon (3NF EDW) is top-down from enterprise model. OBT is the extreme denormalisation for simple cases. Always declare the grain in writing before modelling.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

Design a star schema for the following analytical question: "How many unique learners completed at least one lesson in each domain this month, broken down by tier and country?"

1. Define the grain of the fact table.
2. List the fact table columns (measures and foreign keys).
3. Sketch the dim_user and dim_lesson tables with at least 5 columns each.
4. Write the SQL query against your star schema that answers the business question.

---

# Integration

**Mathematics**: The star schema is a direct application of **relational algebra**. The fact table is a relation F; each dimension D_i is a relation. The star JOIN is F ⋈ D_1 ⋈ D_2 ... ⋈ D_k — a series of equi-joins on surrogate keys. The denormalisation that produces wide dimension tables applies the **natural join** to collapse multi-level hierarchies (lesson → domain → school) into one flat relation. This trade-off — redundancy for join avoidance — mirrors the fundamental tension in relational theory between **3NF** (update anomaly prevention) and **BCNF/denormalisation** (query performance). Analytics chooses update simplicity (dimensions change rarely) over storage efficiency.

**Sciences**: Star schema design mirrors **phylogenetic classification** in biology. The fact table is the specimen record (event observed). Dimension tables are taxonomic hierarchies (species → genus → family → order → class → phylum → kingdom). A query "how many specimens of class Mammalia were collected in South America?" navigates from the specimen fact record through the taxonomy dimension to the class level — exactly a drill-down through a slowly changing dimension hierarchy. Taxonomic revisions (reclassifying a species) are the biological equivalent of SCD Type 2 — the historical specimens remain classified under the old taxonomy; new ones under the revised one.

---

# The Correct Count

The query was rewritten: the module JOIN removed from the fact query, and module_key loaded directly onto the fact table at ETL time. The count returned 4,700 — matching the raw events exactly. "4,700 is a smaller number than 14,000," the Senior Engineer said. "But it's the right number." The Lead Data Engineer updated the data quality test. Grain violations were now caught automatically on every dbt run.
