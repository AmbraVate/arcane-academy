---
id: de-sen-m4-02
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m4
moduleTitle: "Module 4: Data Pipelines"
moduleGlyph: "🔄"
moduleSortOrder: 4
topicSlug: elt
topicTitle: "ELT"
topicSortOrder: 2
lesson: 2
title: "ELT: Transform After Loading"
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
  - de-sen-m4-01
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the key difference between ETL and ELT and when ELT is preferred"
    - "Describes the role of dbt in the ELT pattern"
    - "Identifies the data lake landing zone pattern and its purpose"
    - "Explains why ELT is natural for cloud data warehouses but risky for sensitive data"
  keywords:
    - ELT
    - dbt
    - data lake
    - raw layer
    - transformation layer
    - cloud data warehouse
    - schema-on-read
  modelAnswer: |
    ELT inverts the transform step: raw data is loaded into the warehouse first, then transformed in-place using the warehouse's own compute. ETL transforms before loading — the warehouse only sees clean data.
    ELT is preferred when the target is a cloud data warehouse with scalable compute (BigQuery, Snowflake, Redshift): transformations run as SQL directly on the warehouse engine at scale, with no separate transformation infrastructure. The raw data is preserved in a landing zone (raw layer), enabling re-transformation when business logic changes.
    dbt (data build tool) is the dominant ELT transformation framework. It writes SQL SELECT statements as models; dbt compiles them into CREATE TABLE AS SELECT or CREATE VIEW statements and manages dependencies between models. dbt adds testing, documentation, and lineage tracking on top of the SQL transformation layer.
    Risk: loading raw data (before transformation) means sensitive PII is in the warehouse in raw form. Security controls (column masking, RLS, encryption) must be applied at the raw layer before transformation models expose it downstream. In ETL, PII can be masked during transformation before it ever enters the warehouse.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "Your team discovers the revenue calculation in your ETL transformation was wrong for the past 6 months. In ETL, the raw data was discarded after transformation. In ELT, raw data was preserved in the landing zone. What can you do in each case?"
    options:
      - "ETL: re-extract from source; ELT: re-transform from raw layer — ELT is easier"
      - "ETL: re-transform using the corrected logic; ELT: must re-extract from source"
      - "Both require a full re-extract from source — no difference"
      - "ETL: reverse the incorrect transformation; ELT: raw data is immutable so no fix is possible"
    correctIndex: 0
    explanation: "In ELT, the raw data is preserved in the landing zone (raw layer). You can apply the corrected transformation logic and re-run the dbt models against the unchanged raw data — no need to re-extract from the source. In ETL, if raw data was discarded or is inaccessible, you must re-extract. This is a major operational advantage of ELT: the raw layer is the single source of truth, and transformations are reproducible."
  - type: FILL_BLANK
    question: "In dbt, a ___ is a SQL SELECT statement that dbt compiles into a table or view in the data warehouse."
    answer: "model"
    explanation: "A dbt model is a .sql file containing a single SELECT statement. dbt wraps it in CREATE TABLE AS SELECT or CREATE VIEW depending on the materialization setting. Models declare dependencies using the ref() function — dbt builds the dependency graph and runs models in topological order."
  - type: SHORT_TEXT
    question: "Describe the three-layer architecture commonly used in ELT data warehouses."
    modelAnswer: "Raw (landing zone): exact copy of source data, never modified, append-only. Staging: light transformations — rename columns, cast types, basic filtering. No business logic. Mart: business-logic transformations — metrics, aggregations, denormalised reporting tables. Each layer builds on the previous using dbt models. Raw preserves history; staging normalises format; mart answers business questions."
microCheckpoint:
  question: "What is the key operational advantage of preserving raw data in an ELT pipeline?"
  answer: "Raw data can be re-transformed when business logic changes or errors are discovered, without re-extracting from the source system. The raw layer is immutable and serves as a reproducible source of truth for all downstream transformations."
retrieval:
  recall: "What does dbt stand for and what is its primary purpose in an ELT pipeline?"
  explain: "Explain why ELT is natural for cloud data warehouses but ETL may be preferable when handling PII."
  mistakeId: "elt-pii-in-raw-layer"
---

# The Recalculation

"We've been calculating monthly revenue wrong for eight months," the Senior Engineer said. "The currency conversion was applied before timezone normalisation." In the old ETL pipeline, the raw data was gone. But this was an ELT system — the raw events were still in the landing zone, untouched. "Re-run the dbt models with the fix," the Lead Data Engineer said. Eight months of corrected data in forty minutes.

# ETL vs ELT: The Key Inversion

```
ETL:
  Source → [Extract] → [Transform in pipeline] → [Load clean data] → Warehouse
  Raw data: discarded after transform
  
ELT:
  Source → [Extract] → [Load raw data] → Warehouse → [Transform in-warehouse]
  Raw data: preserved in landing zone
```

The distinction matters in three ways:
1. **Where transform compute runs**: ETL runs on a separate engine (Spark, Python); ELT runs on the warehouse itself
2. **What the warehouse stores**: ETL stores only clean data; ELT stores raw + transformed
3. **Re-transformation**: ETL requires re-extraction; ELT re-runs transformations on preserved raw data

## Why ELT Emerged

Modern cloud data warehouses (BigQuery, Snowflake, Redshift) have elastic compute that scales to petabytes. Running transformations on the warehouse is:
- Cheaper than provisioning a separate transformation cluster
- Faster (data doesn't leave the warehouse network)
- Simpler (SQL is the transformation language — no Spark or Python cluster required)

ELT is the natural pattern when the warehouse can handle the transformation workload.

## The Three-Layer Architecture

```
┌─────────────────────────────────────────────────┐
│ RAW / LANDING ZONE                              │
│  - Exact copy of source data                    │
│  - Never modified, append-only                  │
│  - raw.events, raw.users, raw.products          │
└────────────────────┬────────────────────────────┘
                     │ dbt models
┌────────────────────▼────────────────────────────┐
│ STAGING                                         │
│  - Rename columns, cast types, light filtering  │
│  - stg_events, stg_users, stg_products          │
│  - One model per source table                   │
└────────────────────┬────────────────────────────┘
                     │ dbt models
┌────────────────────▼────────────────────────────┐
│ MARTS / PRESENTATION                            │
│  - Business logic, metrics, aggregations        │
│  - mart_revenue, mart_learner_progress          │
│  - Denormalised for reporting tools             │
└─────────────────────────────────────────────────┘
```

## dbt: The ELT Transformation Framework

dbt (data build tool) manages SQL transformations with:
- **Models**: SQL SELECT statements that dbt materialises as tables or views
- **Tests**: assertions that run after transformation (not null, unique, accepted values)
- **Documentation**: auto-generated data catalogue from model descriptions
- **Lineage**: automatically computes dependency graph from `ref()` calls

```sql
-- models/staging/stg_xp_events.sql
SELECT
    id                                    AS event_id,
    user_id,
    lesson_id,
    xp_earned,
    occurred_at::TIMESTAMPTZ             AS occurred_at,
    LOWER(TRIM(event_type))              AS event_type
FROM {{ source('raw', 'xp_events') }}
WHERE occurred_at IS NOT NULL
```

```sql
-- models/marts/mart_learner_xp.sql
SELECT
    u.tier,
    DATE_TRUNC('month', e.occurred_at)   AS month,
    COUNT(DISTINCT u.id)                 AS learner_count,
    SUM(e.xp_earned)                     AS total_xp
FROM {{ ref('stg_xp_events') }}        AS e
JOIN {{ ref('stg_users') }}            AS u ON u.id = e.user_id
GROUP BY 1, 2
```

The `ref()` function declares a dependency — dbt builds the DAG and runs `stg_xp_events` before `mart_learner_xp`.

```yaml
# models/staging/schema.yml — tests
models:
  - name: stg_xp_events
    columns:
      - name: event_id
        tests:
          - not_null
          - unique
      - name: xp_earned
        tests:
          - not_null
          - dbt_utils.accepted_range:
              min_value: 0
              max_value: 1000
```

## Running dbt

```bash
dbt run                    # compile and run all models
dbt run --select stg_+    # run staging models and all downstream
dbt test                   # run all tests
dbt docs generate          # build data catalogue
dbt docs serve             # open catalogue in browser
```

## Data Lake Landing Zone

For sources that can't be queried directly by the warehouse (flat files, APIs, third-party SaaS), a data lake (S3, GCS) serves as the landing zone.

```
API / CSV → [Fivetran / Airbyte] → S3 raw bucket → BigQuery external table → dbt models
```

The raw files in S3 are immutable. The BigQuery external table reads them on query. dbt transforms the external data just like any other source.

## Common Mistakes

> **Exposing Raw PII to All Warehouse Users**
> In ELT, raw data lands in the warehouse before masking or anonymisation. If the raw layer is readable by analysts, PII is exposed. Apply column masking or dynamic data masking policies on the raw layer, or use a separate schema with restricted access. Alternatively, use ETL when PII must never enter the warehouse raw.

> **Skipping the Raw Layer**
> Loading data into staging directly without a raw layer removes the ability to re-transform. Always preserve the immutable raw copy.

> **Over-materialising dbt Models**
> dbt models default to views. Only materialise as tables (CREATE TABLE AS SELECT) when query performance requires it. Unnecessary table materialisation increases storage costs and extends dbt run time.

> **No dbt Tests**
> Transformations that run without tests are invisible failures. A broken JOIN produces silently wrong metrics. Always add not_null and unique tests at minimum on primary keys; add accepted_values tests for categorical columns.

## Mental Model

Think of ELT as a **library archive system**. New acquisitions arrive as raw manuscripts (raw layer) and are immediately catalogued and stored without modification. Librarians then create summaries, indices, and research guides (staging + marts) from the originals. If a summary is wrong, you return to the original manuscript and produce a corrected version — the original was never changed. ETL, by contrast, processes manuscripts before shelving and discards the originals. Faster to shelve, but impossible to re-process.

**Mini Summary**: ELT loads raw data first, then transforms in-warehouse using SQL (dbt). The three-layer architecture (raw → staging → marts) preserves raw data for re-transformation. dbt manages model dependencies, tests, and documentation. ELT is natural for cloud data warehouses. Preserve the raw layer; protect PII with column masking before exposing to analysts.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium's data team wants to build an ELT pipeline for lesson engagement metrics. Sources: `lesson_views` (PostgreSQL), `lesson_completions` (PostgreSQL), `user_feedback` (CSV files in S3).

Design the full pipeline:
1. Describe how each source would be loaded into the raw layer.
2. Write the dbt model for `stg_lesson_completions` — what transformations belong at the staging layer?
3. Write the dbt model for `mart_lesson_engagement` — what fields would it contain and what business logic would it apply?
4. What dbt tests would you add to `stg_lesson_completions`?

---

# Integration

**Mathematics**: dbt's dependency graph is a **DAG (Directed Acyclic Graph)**. The `ref()` function adds a directed edge from the downstream model to the upstream model. dbt performs a **topological sort** to determine execution order. Topological sort is O(V + E) where V is the number of models and E is the number of ref() dependencies. When models are independent (no shared dependencies), dbt parallelises them, and the wall-clock time approaches the critical path length. For a pipeline with 100 models in a linear chain, parallelism does not help — the critical path is sequential. Redesigning into a wider DAG reduces the critical path.

**Sciences**: The ELT raw layer embodies the **immutability principle** from physics conservation laws. Just as mass-energy is conserved in a closed system, the raw layer conserves the original data — it can never be destroyed, only transformed. dbt models are the transformation functions: deterministic, reproducible, and referentially transparent. The ability to re-run a transformation from the conserved raw state is the data engineering equivalent of a **reversible process** in thermodynamics — you can always return to the original state and apply a different transformation.

---

# The Clean History

The corrected revenue figures went to the finance team. "Eight months reprocessed in forty minutes," the Senior Engineer said. "If this had been ETL we'd have been re-extracting from six different source systems, negotiating database access, and hoping nothing had changed." The Lead Data Engineer was already updating the runbook. "Keep the raw data. Always keep the raw data."
