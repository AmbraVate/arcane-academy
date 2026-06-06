---
id: de-sen-m4-01
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m4
moduleTitle: "Module 4: Data Pipelines"
moduleGlyph: "🔄"
moduleSortOrder: 4
topicSlug: etl
topicTitle: "ETL"
topicSortOrder: 1
lesson: 1
title: "ETL: Extract, Transform, Load"
sortOrder: 1
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
  - de-sen-m3-04
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Describes each ETL phase and what happens in each"
    - "Explains idempotency and why it is essential in ETL pipelines"
    - "Identifies the watermark pattern for incremental extraction"
    - "Describes at least two failure modes and their mitigations"
  keywords:
    - extract
    - transform
    - load
    - idempotency
    - watermark
    - incremental extraction
    - staging table
    - pipeline orchestration
  modelAnswer: |
    ETL (Extract, Transform, Load) moves data from source systems to a target (typically a data warehouse). Extract pulls data from sources; Transform applies business rules, cleaning, and reshaping; Load writes the result to the target.
    Idempotency means running the same ETL job multiple times produces the same result — no duplicate records, no data loss. It is essential because pipelines fail and must be re-run. Achieving idempotency requires: using MERGE (upsert) rather than blind INSERT, tracking a high-water mark to extract only new/changed records, and loading into a staging table before applying to the final table.
    The watermark pattern tracks the latest extracted record (by timestamp or sequence ID). On re-run, extraction starts from the watermark — avoiding full table scans and duplicate processing.
    Common failures: source schema changes breaking transformations (fix: contract tests on source); late-arriving data missing the extraction window (fix: reprocessing window — always extract records updated in the past N hours, not just since last run); network interruption mid-load (fix: transactional load — commit only when complete).
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "An ETL job runs at 02:00 daily, extracting records WHERE updated_at > last_run_timestamp. The job fails at 02:30. At 02:00 the next day it re-runs. Which approach ensures no data is lost?"
    options:
      - "Use WHERE updated_at > last_successful_run_timestamp (not last_run_timestamp)"
      - "Use WHERE updated_at > NOW() - INTERVAL '24 hours' (rolling window)"
      - "Restart the failed run at the 02:30 failure point"
      - "Only update last_run_timestamp when the job succeeds"
    correctIndex: 3
    explanation: "Only advance the watermark on success. If the job failed at 02:30, last_run_timestamp should remain at yesterday's value. On the next run, extract from yesterday's watermark — this may re-process some records already loaded, but with an idempotent MERGE/upsert this is safe. Option A is equivalent to D. Option B risks losing records updated between the rolling window and the actual failure point."
  - type: FILL_BLANK
    question: "Loading into a ___ table first, then applying a MERGE to the final table, makes the load phase atomic and idempotent."
    answer: "staging"
    explanation: "A staging table holds the new/changed data from the current ETL run. Once fully populated, a MERGE (or INSERT...ON CONFLICT) applies changes atomically to the final table. If the pipeline fails mid-extraction, the staging table is simply repopulated on re-run — no partial data in the final table."
  - type: SHORT_TEXT
    question: "An ETL job transforms a source `amount` column from USD to GBP using today's exchange rate. If the job re-runs tomorrow, the result will be different. Why is this a problem and how do you fix it?"
    modelAnswer: "The transformation is not idempotent — re-running on different days produces different results, meaning historical data would be rewritten with a different exchange rate. Fix: extract and store the exchange rate at the time of the transaction (from a rates table or API snapshot), not at the time the ETL runs. Alternatively, store the source amount in USD and apply currency conversion only at query time using a rates dimension table."
microCheckpoint:
  question: "Why must ETL jobs be idempotent?"
  answer: "Pipelines fail and must be re-run. If re-running produces duplicates or different results, data integrity is compromised. Idempotency ensures a re-run produces exactly the same result — safe to retry without side effects."
retrieval:
  recall: "What is a watermark in ETL and what problem does it solve?"
  explain: "Explain why loading into a staging table before merging to the final table improves reliability."
  mistakeId: "etl-non-idempotent-insert"
---

# The Broken Dashboard

The Consortium's weekly report had duplicate rows. The ETL job had crashed mid-run on Tuesday and been re-run on Wednesday. The fix had been to re-run the INSERT — but the records from Monday's partial run were already in the target table. "ETL pipelines fail," the Lead Data Engineer said. "You design them to fail safely. That starts with idempotency."

# ETL: Extract, Transform, Load

ETL is the process of moving data from one or more source systems into a target (typically a data warehouse or data mart).

```
Source Systems              ETL Pipeline              Target
┌──────────────┐           ┌───────────┐           ┌──────────────┐
│ PostgreSQL   │──Extract──│ Transform │──Load────▶│ Data         │
│ Salesforce   │           │  & Clean  │           │ Warehouse    │
│ CSV files    │           └───────────┘           └──────────────┘
└──────────────┘
```

## Phase 1: Extract

Pull data from source systems. The challenge: **extracting only changed data** (incremental extraction), not the full table every run.

### Full Extract
```sql
-- Pull everything — simple but expensive
SELECT * FROM source.events;
```

Acceptable for small tables. For 100M-row tables: re-extracting everything daily is prohibitively slow.

### Incremental Extraction with Watermark
```sql
-- Watermark table tracks last extraction per source
CREATE TABLE etl_watermarks (
    source_table TEXT PRIMARY KEY,
    last_extracted_at TIMESTAMPTZ NOT NULL DEFAULT '1970-01-01'
);

-- Extract only new/updated records
SELECT *
FROM source.events
WHERE updated_at > (
    SELECT last_extracted_at
    FROM etl_watermarks
    WHERE source_table = 'events'
)
ORDER BY updated_at;

-- Advance watermark ONLY after successful load
UPDATE etl_watermarks
SET last_extracted_at = :max_updated_at_from_this_run
WHERE source_table = 'events';
```

**Requirement for watermark extraction**: source table must have a reliable `updated_at` column updated on every INSERT and UPDATE. A `created_at`-only column misses updates.

### Change Data Capture (CDC)
For sources that can't add `updated_at`, use database log-based CDC (Debezium, AWS DMS). CDC reads the transaction log (WAL in PostgreSQL) and emits every INSERT/UPDATE/DELETE as an event. No polling delay, no missed updates.

## Phase 2: Transform

Apply business rules, clean data, reshape for the target schema.

```python
# Example transformation pipeline (pseudocode)
def transform_events(raw_events: DataFrame) -> DataFrame:
    return (raw_events
        # Clean
        .dropna(subset=['user_id', 'event_type'])
        .filter(col('event_type').isin(VALID_EVENT_TYPES))

        # Enrich
        .withColumn('month', date_trunc('month', col('occurred_at')))
        .withColumn('amount_gbp', col('amount_usd') * col('exchange_rate'))

        # Rename to target schema
        .withColumnRenamed('user_id', 'dim_user_key')
        .select(['dim_user_key', 'event_type', 'month', 'amount_gbp', 'occurred_at'])
    )
```

**Transform principles:**
- Make transformations **deterministic** — same input always produces same output
- Avoid time-dependent calculations (current rate, today's date) without anchoring to the source record's timestamp
- Log rejected/invalid records to a `rejected_records` table rather than silently dropping them

## Phase 3: Load

Write transformed data to the target. Load strategy determines whether re-runs are safe.

### Naive INSERT (not idempotent)
```sql
-- BAD: Re-run produces duplicates
INSERT INTO warehouse.fact_events
SELECT * FROM staging.events_transformed;
```

### Idempotent MERGE via Staging Table
```sql
-- Step 1: Load to staging (truncate + insert = idempotent)
TRUNCATE TABLE staging.events_transformed;
INSERT INTO staging.events_transformed
SELECT * FROM :transformed_data;

-- Step 2: Merge into final table
INSERT INTO warehouse.fact_events (event_id, user_key, event_type, month, amount_gbp)
SELECT event_id, user_key, event_type, month, amount_gbp
FROM staging.events_transformed
ON CONFLICT (event_id) DO UPDATE SET
    amount_gbp = EXCLUDED.amount_gbp,
    event_type = EXCLUDED.event_type;
-- Re-run is safe: duplicate event_ids are updated, not doubled
```

## Pipeline Orchestration

Production ETL pipelines run on a scheduler with dependency management.

```python
# Apache Airflow DAG (simplified)
with DAG('consortium_etl', schedule_interval='0 2 * * *') as dag:
    extract = PythonOperator(task_id='extract', python_callable=extract_events)
    transform = PythonOperator(task_id='transform', python_callable=transform_events)
    load = PythonOperator(task_id='load', python_callable=load_to_warehouse)
    update_watermark = PythonOperator(task_id='watermark', python_callable=advance_watermark)

    extract >> transform >> load >> update_watermark
```

Key orchestration features: dependency graph (task B runs after task A succeeds), retry with backoff, alerting on failure, backfill (re-run historical date ranges).

## Common Mistakes

> **Advancing the Watermark Before Load Succeeds**
> If you update `last_extracted_at` before the load commits, a subsequent failure means that window of data is permanently skipped. Always update the watermark as the final step, after confirming the load committed.

> **Using INSERT Instead of MERGE**
> Blind INSERT produces duplicates on re-run. Always use MERGE/upsert with a natural key (the source system's primary key) as the conflict target.

> **Silent Record Rejection**
> Dropping records that fail transformation without logging them hides data quality problems. Write rejected records to a separate table with the rejection reason for investigation.

> **Full Extract on Large Tables Daily**
> Extracting a 100M-row table every night consumes hours and source database I/O. Always design incremental extraction from the start.

## Mental Model

Think of ETL as a **factory assembly line**. Raw materials (source data) arrive at the loading dock (Extract). Workers on the production line reshape and clean them (Transform). Finished goods go to the warehouse (Load). If the assembly line stops, you don't discard all in-progress materials — you mark where you stopped and resume from that point (watermark). Each batch of goods has a serial number (natural key) so receiving duplicates doesn't create double inventory (idempotent MERGE).

**Mini Summary**: ETL extracts data from sources incrementally using watermarks, transforms it deterministically, and loads via idempotent MERGE into a staging table first. Pipelines fail — idempotency ensures re-runs are safe. Never advance the watermark before the load commits. Log rejected records; never drop them silently.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium runs an ETL pipeline that moves `xp_events` from PostgreSQL into a ClickHouse data warehouse every hour. The pipeline has been failing intermittently — sometimes records are duplicated, sometimes records are missing.

Given the following code snippet:
```python
def run_etl():
    last_ts = get_watermark('xp_events')
    events = extract(f"WHERE occurred_at > '{last_ts}'")
    transformed = transform(events)
    load_insert(transformed)         # blind INSERT
    update_watermark('xp_events', datetime.now())  # updated before commit
```

Identify all three bugs and explain how you would fix each.

---

# Integration

**Mathematics**: ETL pipeline orchestration with dependencies forms a **Directed Acyclic Graph (DAG)**. Task A must complete before task B starts; B before C. Topological sort determines execution order. The critical path (longest path through the DAG by duration) determines total pipeline completion time — no amount of parallelism can reduce it below the critical path length. This is why large ETL pipelines are decomposed into parallel branches: tasks on non-critical paths run concurrently, keeping the wall-clock time close to the critical path minimum.

**Sciences**: Incremental ETL with watermarks mirrors **carbon-14 dating** in archaeology. The watermark (the decay state of C-14) tells you where in time you are relative to a known baseline. Each extraction window is an isotope decay interval — measurable, reproducible, anchored to a physical reference point. Just as C-14 dating fails when the baseline is contaminated, watermark-based ETL fails when `updated_at` is unreliable (not updated on every change, set to a future time by application bugs, or absent entirely).

---

# The Fixed Pipeline

The Senior Engineer rewrote the ETL job: MERGE instead of INSERT, watermark updated only after commit, and incremental extraction with a reprocessing overlap window (always look back 2 hours to catch late-arriving updates). The next re-run after failure produced zero duplicates. "ETL isn't magic," the Lead Data Engineer said. "It's disciplined plumbing."
