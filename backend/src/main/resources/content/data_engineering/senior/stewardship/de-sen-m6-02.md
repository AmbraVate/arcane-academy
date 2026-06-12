---
id: de-sen-m6-02
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m6
moduleTitle: "Module 6: Data Governance"
moduleGlyph: "🏛️"
moduleSortOrder: 6
topicSlug: stewardship
topicTitle: "Stewardship"
topicSortOrder: 2
lesson: 2
title: "Data Stewardship: Executing the Governance Contract"
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
  - de-sen-m6-01
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Describes the steward's technical responsibilities in data governance"
    - "Explains data lineage and why it is essential for impact analysis"
    - "Identifies data quality dimensions and how stewards monitor them"
    - "Describes schema change management and the consumer notification process"
  keywords:
    - data steward
    - data lineage
    - data quality
    - schema change
    - impact analysis
    - data catalogue
    - quality dimensions
  modelAnswer: |
    A data steward implements the data owner's governance decisions. Responsibilities: maintaining data quality monitoring (completeness, validity, consistency, freshness), documenting schemas in the data catalogue, managing access controls, executing data retention policies, and communicating schema changes to consumers.
    Data lineage maps how data flows through pipelines — from source to transformation to target. It enables impact analysis: "if I change column X, which downstream tables and reports will break?" Without lineage, schema changes cause unexpected breakage. Modern tools (dbt, OpenLineage, Marquez) auto-generate lineage graphs from transformation code.
    Data quality dimensions (completeness, validity, consistency, uniqueness, timeliness, accuracy) each require different monitoring approaches. A steward defines quality checks, sets thresholds, and is alerted when metrics fall below SLA.
    Schema changes follow a change management process: notify consumers in advance, use expand-contract for non-breaking migration, maintain backward compatibility during a transition window, and document the change in the data catalogue.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "A data engineer renames column `user_id` to `learner_id` in `fact_lesson_completions`. Which governance process was skipped?"
    options:
      - "Access control review — renaming breaks row-level security"
      - "Impact analysis + consumer notification — downstream consumers referencing user_id will break"
      - "Data retention policy — renaming resets the retention clock"
      - "Quality check calibration — quality thresholds reference old column names"
    correctIndex: 1
    explanation: "Renaming a column is a breaking schema change. Any downstream SQL, BI dashboard, or dbt model referencing 'user_id' will fail. The steward's job is to: (1) use data lineage to identify all consumers, (2) notify them at least 30 days in advance, (3) provide migration guidance (use the new name), and (4) consider an expand-contract migration (add learner_id, keep user_id as alias, remove user_id in a later migration). Skipping this breaks production systems without warning."
  - type: FILL_BLANK
    question: "Data ___ maps how a specific piece of data moves from its source through transformations to its final destination, enabling engineers to assess the impact of changes."
    answer: "lineage"
    explanation: "Data lineage is the audit trail of data movement. Column-level lineage (e.g. dbt's built-in lineage graph) shows that mart.lesson_completion_rate.completed_count is derived from staging.stg_lesson_attempts.status, which is derived from raw.lesson_attempts.lesson_status in PostgreSQL. When PostgreSQL changes lesson_status values, the lineage graph shows exactly which downstream metrics are affected."
  - type: SHORT_TEXT
    question: "The completeness of `domain_id` in `fact_lesson_completions` is 91.2% (8.8% null). The quality SLA requires >99.5%. Describe the steward's response process."
    modelAnswer: "1. Raise a data quality incident — notify the data owner and create a ticket. 2. Use lineage to identify the upstream source of domain_id (likely the lesson_id → domain lookup in the ETL). 3. Investigate: are certain lesson IDs missing domain assignments in the reference table? Are specific date ranges affected? 4. Fix the root cause (populate missing domain mappings, fix the ETL join). 5. Backfill historical null values where the domain can be determined. 6. Update the quality monitoring threshold alert to page the steward before the SLA is violated. 7. Document the incident in the data catalogue entry."
microCheckpoint:
  question: "What is data lineage and why is it essential before making schema changes?"
  answer: "Data lineage maps the flow of data from source through transformations to destinations. Before changing a schema, lineage reveals all downstream consumers — tables, dashboards, ML models — that will be affected. Without lineage, schema changes break unknown consumers silently."
retrieval:
  recall: "Name four data quality dimensions and give a monitoring check for each."
  explain: "Explain the expand-contract pattern for non-breaking schema migrations."
  mistakeId: "schema-change-no-impact-analysis"
---

# The Silent Break

Three reports went wrong on Monday morning. All three referenced `user_id` in `fact_lesson_completions`. A data engineer had renamed it to `learner_id` Friday afternoon. No impact analysis. No notification. "This is a stewardship failure," the Lead Data Engineer said. "Good governance doesn't just define policies. It implements them technically so changes can't be made without following the process."

# The Steward's Role

Where the data owner sets policy, the **data steward** implements it technically. The steward is the hands-on engineer who ensures governance decisions become real technical controls.

```
Owner decides:    "learner_events quality must be >99.5% complete"
Steward implements: dbt test + alert + incident process

Owner decides:    "only the ML team can access raw PII fields"
Steward implements: PostgreSQL column-level grants + audit logging

Owner decides:    "learner data retained for 3 years then deleted"
Steward implements: retention_policy table + nightly deletion job + audit trail
```

## Data Lineage

Lineage is the dependency graph of data flowing through systems.

```
PostgreSQL: raw.xp_events
    │
    ▼ (dbt: stg_xp_events)
Warehouse: staging.stg_xp_events
    │
    ├─▶ (dbt: fact_xp_events) → warehouse.fact_xp_events
    │       │
    │       ├─▶ (dbt: mart_weekly_xp) → executive dashboard
    │       └─▶ (dbt: mart_learner_xp) → learner profile widget
    │
    └─▶ (dbt: mart_domain_engagement) → content team dashboard
```

### Impact Analysis Workflow
Before any schema change:
1. Query the lineage graph for all downstream dependents
2. Assess the impact on each consumer (breaking vs non-breaking)
3. Plan migration (expand-contract if needed)
4. Notify consumers with timeline and migration guide
5. Execute change with agreed transition window
6. Monitor downstream consumers post-change

```bash
 # dbt lineage query
dbt ls --select stg_xp_events+  # all models downstream of stg_xp_events
 # Returns: fact_xp_events, mart_weekly_xp, mart_learner_xp, mart_domain_engagement
```

Modern lineage tools (OpenLineage, Marquez, dbt docs) auto-build these graphs from transformation code.

## Data Quality Monitoring

A steward monitors quality across six dimensions:

```python
 # dbt schema tests (run after every pipeline)
models:
  - name: fact_lesson_completions
    columns:
      # Uniqueness
      - name: completion_id
        tests: [unique, not_null]

      # Completeness
      - name: domain_id
        tests:
          - not_null
          - dbt_utils.expression_is_true:
              expression: "domain_id IS NOT NULL"  # explicit SLA
      
      # Validity
      - name: xp_earned
        tests:
          - dbt_utils.accepted_range:
              min_value: 0
              max_value: 500
      
      # Referential integrity
      - name: user_key
        tests:
          - relationships:
              to: ref('dim_user')
              field: user_key

    # Freshness (timeliness)
    freshness:
      warn_after: {count: 1, period: hour}
      error_after: {count: 3, period: hour}
```

| Dimension | Check Example |
|---|---|
| Completeness | % non-null for required fields |
| Validity | Values within expected range/set |
| Uniqueness | No duplicate primary keys |
| Consistency | Same fact_value across redundant sources |
| Timeliness | Last updated within freshness SLA |
| Accuracy | Sample validation against source system |

## Schema Change Management

### Expand-Contract Pattern
Never remove a column before consumers have migrated.

```sql
-- Phase 1 (Expand): Add new column alongside old
ALTER TABLE fact_lesson_completions ADD COLUMN learner_id UUID;
UPDATE fact_lesson_completions SET learner_id = user_id;
-- Notify consumers: "learner_id is now available. Migrate in 30 days."

-- Phase 2 (Transition): Both columns exist and are valid
-- Consumers migrate their SQL/dbt models from user_id to learner_id

-- Phase 3 (Contract): Remove old column after all consumers migrated
ALTER TABLE fact_lesson_completions DROP COLUMN user_id;
-- Verified: zero downstream references to user_id
```

### Consumer Communication Template
```
Subject: [Breaking Change 30d Notice] fact_lesson_completions — user_id renamed to learner_id

Effective date: 2024-04-15
Change: Column user_id will be removed. Use learner_id (identical values).
Migration: UPDATE your SQL/dbt models to reference learner_id.
Both columns available until 2024-04-15.
Questions: data-engineering@consortium.io / #data-governance Slack
```

## Access Control Enforcement

The steward implements the owner's access decisions as technical controls:

```sql
-- Owner decision: "ML team can access anonymised learner data; PII fields restricted"
-- Steward implements:
REVOKE SELECT ON TABLE fact_lesson_completions FROM ml_analyst_role;
GRANT SELECT (
    completion_id, lesson_key, date_key,
    xp_earned, duration_seconds, score_pct
    -- user_key omitted (links to PII in dim_user)
) ON fact_lesson_completions TO ml_analyst_role;

-- Owner decision: "audit_log must be immutable"
REVOKE UPDATE, DELETE, TRUNCATE ON audit_log FROM everyone;
```

## Data Retention Execution

```sql
-- Steward implements owner's 3-year retention policy
CREATE TABLE data_retention_log (
    table_name   TEXT,
    deleted_at   TIMESTAMPTZ DEFAULT NOW(),
    rows_deleted BIGINT,
    criteria     TEXT
);

-- Nightly deletion job (steward-maintained)
WITH deleted AS (
    DELETE FROM learner_events
    WHERE occurred_at < NOW() - INTERVAL '3 years'
    RETURNING *
)
INSERT INTO data_retention_log VALUES (
    'learner_events',
    NOW(),
    (SELECT COUNT(*) FROM deleted),
    'retention_policy: 3 years from occurred_at'
);
```

## Common Mistakes

> **Schema Change Without Impact Analysis**
> Renaming or removing a column without first querying the lineage graph. Always use lineage before touching any column that consumers reference.

> **Quality Monitoring Without Alerting**
> dbt tests that run but whose failures are silently logged to a report nobody reads. Quality failures should page the steward — not just appear in a dashboard checked once a week.

> **Documenting Only What Was, Not What Changed**
> Data catalogue entries that describe current state but omit schema version history and migration notes. Consumers troubleshooting failures need to know what changed and when.

## Mental Model

Think of a data steward as a **building manager for a shared office**. The owner (landlord) sets the rules: no smoking, 9pm quiet hours, shared kitchen cleaned weekly. The building manager implements them: locks doors, posts signs, cleans the kitchen, fixes the broken tap. When a tenant (consumer) asks "can I modify the layout of my office?", the manager does the impact assessment (will this affect the fire exit?) and communicates to other tenants. The manager doesn't set policy — they make policy operational.

**Mini Summary**: Data stewards technically implement governance decisions. Data lineage enables impact analysis before schema changes. Quality monitoring covers completeness, validity, uniqueness, consistency, timeliness, and accuracy. Schema changes follow expand-contract with consumer notification. Access controls are the technical expression of ownership decisions. Quality failures should trigger alerts, not silent logs.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

You are the data steward for `fact_lesson_completions`. The data owner has asked you to:
1. Rename `xp_earned` to `xp_awarded` — plan the expand-contract migration
2. Add a quality SLA: `domain_id` must be >99.5% non-null, `xp_awarded` must be between 0–500
3. The ML team requests read access to `fact_lesson_completions` but should NOT see `user_key`

For each task:
- What technical steps do you take?
- What communications must you send to consumers or stakeholders?
- How do you verify the task is complete?

---

# Integration

**Mathematics**: Data lineage graphs are **directed acyclic graphs (DAGs)** with nodes as datasets/transformations and edges as data flows. Impact analysis is a **graph reachability** problem: given a changed node u, which nodes v are reachable from u following directed edges? This is computed with BFS or DFS in O(V + E). Topological sort of the lineage DAG determines the order in which quality checks propagate — a quality failure in a source dataset propagates to all downstream descendants. The steward's job of managing cascading quality impacts is computationally equivalent to failure propagation analysis in dependency graphs — formally equivalent to the reliability engineering problem of identifying all affected components when a component fails.

**Sciences**: Data stewardship mirrors **ecological management** in conservation biology. The data owner is the policy-maker (government setting conservation law). The steward is the ranger: monitoring population health (quality checks), maintaining habitat boundaries (access controls), managing invasive species (bad data), and reporting changes to the authority. The lineage graph is the **food web** — changing one species (dataset) propagates through the ecosystem via predator-prey relationships (consumer-producer dependencies). Impact analysis before schema changes is the ecological equivalent of an Environmental Impact Assessment before land use change.

---

# The Governance Infrastructure

The impact analysis tool was deployed: any PR touching a warehouse schema now automatically queried the lineage graph and listed affected downstream consumers in the PR description. The Friday rename that had broken three reports could no longer happen silently — the PR would have shown seven consumers, triggering the 30-day notification process. "Governance isn't just process," the Lead Data Engineer said. "It's infrastructure. Process without infrastructure is just good intentions."
