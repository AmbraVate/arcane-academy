---
id: de-sen-m5-02
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m5
moduleTitle: "Module 5: Analytics Engineering"
moduleGlyph: "📊"
moduleSortOrder: 5
topicSlug: business_intelligence
topicTitle: "Business Intelligence"
topicSortOrder: 2
lesson: 2
title: "Business Intelligence: From Data to Decisions"
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
  - de-sen-m5-01
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the BI stack: data source → warehouse → BI tool"
    - "Describes the role of the semantic layer in consistent reporting"
    - "Identifies the difference between exploratory and operational BI use cases"
    - "Names specific risks of self-serve BI without governance"
  keywords:
    - business intelligence
    - semantic layer
    - dashboard
    - self-serve BI
    - drill-down
    - OLAP cube
    - data catalogue
  modelAnswer: |
    A BI stack connects raw data through a warehouse to a reporting layer. Data sources (OLTP, SaaS, files) are loaded into a data warehouse (BigQuery, Snowflake, Redshift). A semantic layer (dbt metrics, Looker LookML, Cube) defines business concepts (metrics, dimensions) in a single place. BI tools (Tableau, Looker, Metabase, PowerBI) query the semantic layer to produce dashboards and reports.
    The semantic layer ensures consistent numbers: "revenue" means the same thing in every dashboard because all dashboards query the same definition. Without it, each analyst builds their own SQL, and definitions diverge.
    Operational BI: fixed dashboards refreshed automatically, consumed by non-technical stakeholders (executives, ops). Exploratory BI: ad-hoc analysis by data analysts, investigates anomalies, generates hypotheses.
    Self-serve BI risks: analysts build their own metrics inconsistently; sensitive data is exposed to users without appropriate access controls; too many dashboards with no ownership leads to metrics drift. Governance requires: column-level access control, metric definitions in a semantic layer, dashboard ownership and review cadences.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "The finance team and product team both have 'revenue' metrics in their Tableau dashboards. Finance reports €2.1M for Q1; product reports €1.8M. Both teams are querying the same underlying data. What is the most likely cause?"
    options:
      - "Tableau has a rendering bug that shifts decimal points"
      - "The two teams applied different filters (e.g. finance includes refunds, product excludes them) in their separate SQL queries"
      - "The data warehouse has duplicate records in one team's schema"
      - "Q1 is defined differently in each country's fiscal calendar"
    correctIndex: 1
    explanation: "Metric definition divergence is the most common cause of conflicting numbers. Finance might define revenue as gross (before refunds), while product defines it as net. Without a shared semantic layer enforcing a single definition of 'revenue', each team writes their own SQL and inevitably applies different filters, date ranges, or treatment of edge cases."
  - type: FILL_BLANK
    question: "A ___ layer sits between the data warehouse and BI tools, defining business concepts (metrics, dimensions, filters) once so all consuming tools report consistent numbers."
    answer: "semantic"
    explanation: "The semantic layer (implemented by tools like Looker's LookML, dbt Metrics, or Cube.js) is the single source of truth for metric definitions. BI tools query the semantic layer rather than writing raw SQL against warehouse tables. This ensures 'monthly active users' means the same thing in every dashboard, every team, every tool."
  - type: SHORT_TEXT
    question: "A stakeholder wants a dashboard showing lesson completion rates by tier, with the ability to click on 'Senior' and see a breakdown by domain, then click on 'Data Engineering' and see individual lessons. What BI concept is this?"
    modelAnswer: "Drill-down (or OLAP drill-down). The dashboard starts at the aggregate level (completion rate by tier), allows navigation to progressively finer granularity (by domain, then by individual lesson). The data must be modelled to support this hierarchy: tier → domain → lesson, with all three dimensions on the fact table. OLAP cubes pre-aggregate at each level for instant navigation; modern columnar warehouses often compute drill-downs on-the-fly fast enough to skip pre-aggregation."
microCheckpoint:
  question: "Why do large organisations implement a semantic layer rather than letting each BI tool or analyst write their own SQL?"
  answer: "Without a semantic layer, each team defines metrics independently in their own SQL. Definitions diverge silently. Finance's 'revenue' and product's 'revenue' produce different numbers from the same data. A semantic layer defines each metric once — all downstream tools query the same definition, guaranteeing consistent numbers across all reports."
retrieval:
  recall: "What are the three layers in a typical BI stack?"
  explain: "Explain the difference between operational and exploratory BI use cases."
  mistakeId: "bi-no-semantic-layer"
---

# The Two Revenue Numbers

The board meeting ground to a halt. Finance said revenue was up 12% year-over-year. Product said 8%. "Same company, same data, different numbers," the Lead Data Engineer said later. "This happens without a semantic layer. Every team builds their own SQL, makes their own assumptions. We need a single definition of every metric." The Senior Engineer opened the dbt metrics file. This was the work that kept organisations honest with themselves.

# The BI Stack

Business intelligence converts raw data into decisions through a layered architecture.

```
┌─────────────────────────────────────────────────────────┐
│ DATA SOURCES                                            │
│  PostgreSQL (OLTP) │ Salesforce │ CSV files │ APIs      │
└────────────────────┬────────────────────────────────────┘
                     │ ETL / ELT (dbt, Fivetran, Airbyte)
┌────────────────────▼────────────────────────────────────┐
│ DATA WAREHOUSE                                          │
│  BigQuery / Snowflake / Redshift / ClickHouse           │
│  raw → staging → marts                                  │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│ SEMANTIC LAYER                                          │
│  dbt Metrics / Looker LookML / Cube.js                  │
│  Defines: metrics, dimensions, filters, joins           │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│ BI TOOLS                                                │
│  Tableau │ Looker │ Metabase │ PowerBI │ Redash          │
│  Dashboards │ Reports │ Ad-hoc queries                  │
└─────────────────────────────────────────────────────────┘
```

## The Semantic Layer

The semantic layer defines business concepts — metrics, dimensions, joins — in a single authoritative location. Every BI tool queries the semantic layer rather than writing raw warehouse SQL.

```yaml
 # dbt MetricFlow definition
metrics:
  - name: weekly_active_learners
    label: Weekly Active Learners
    type: count_distinct
    type_params:
      measure: user_id
    filter: |
      {{ Dimension('fct_lesson_completions__completed_at') }}
        >= dateadd(week, -1, current_date)
    dimensions:
      - name: tier
        type: categorical
      - name: domain
        type: categorical

  - name: lesson_completion_rate
    label: Lesson Completion Rate
    type: ratio
    type_params:
      numerator: completed_lessons
      denominator: started_lessons
    dimensions:
      - tier
      - domain
      - difficulty
```

Now every tool querying `lesson_completion_rate` uses this definition. The finance-vs-product discrepancy cannot happen.

## Operational vs Exploratory BI

### Operational BI
Fixed dashboards, refreshed automatically, consumed by non-technical stakeholders.

```
Executive Dashboard:
  ┌─────────────────┬──────────────────┬──────────────────┐
  │ Weekly Active   │ Completion Rate  │ Revenue This     │
  │ Learners: 4,821 │ 62.3%  ↑+3.1%   │ Month: €89,400   │
  │ ↑+8% WoW        │                 │ ↑+12% MoM        │
  └─────────────────┴──────────────────┴──────────────────┘
```

Characteristics: pre-defined questions, refreshed daily/hourly, no SQL knowledge required from consumer.

### Exploratory BI
Ad-hoc analysis by data analysts or data scientists. Used to investigate anomalies, validate hypotheses, prepare reports.

```sql
-- Analyst investigating a completion rate drop in senior tier
SELECT
    week,
    domain,
    COUNT(*) FILTER (WHERE status = 'COMPLETED') AS completions,
    COUNT(*) FILTER (WHERE status IN ('STARTED','IN_PROGRESS','COMPLETED')) AS starts,
    ROUND(100.0 * COUNT(*) FILTER (WHERE status = 'COMPLETED')
          / NULLIF(COUNT(*), 0), 1) AS completion_pct
FROM lesson_attempts
JOIN lessons ON lessons.id = lesson_attempts.lesson_id
WHERE tier = 'SENIOR'
  AND week >= CURRENT_DATE - INTERVAL '12 weeks'
GROUP BY 1, 2
ORDER BY 1, 3 DESC;
```

Characteristics: questions emerge from data, SQL proficiency required, results feed back into operational dashboard design.

## OLAP Operations

BI tools apply OLAP (Online Analytical Processing) operations to navigate data:

| Operation | Description | Example |
|---|---|---|
| **Drill-down** | From aggregate to detail | Tier → Domain → Lesson |
| **Roll-up** | From detail to aggregate | Day → Week → Month |
| **Slice** | Filter on one dimension | Only Senior tier |
| **Dice** | Filter on multiple dimensions | Senior + Data Engineering + last 3 months |
| **Pivot** | Rotate dimensions | Tiers as rows, Months as columns |

Modern BI tools expose these operations through UI; the warehouse executes them as SQL GROUP BY, WHERE, and CASE statements.

## Self-Serve BI: Benefits and Risks

Self-serve BI lets non-engineers build their own reports without waiting for a data team.

**Benefits**: faster time to insight, reduces bottleneck on data team, empowers domain experts.

**Risks and mitigations:**

| Risk | Mitigation |
|---|---|
| Metric inconsistency | Semantic layer defines canonical metrics |
| PII exposure | Column-level access control per role |
| Dashboard sprawl (1000 stale dashboards) | Dashboard ownership, review cadence, archival policy |
| Wrong SQL producing wrong results silently | dbt tests on source tables; BI tool query limits |

## Data Catalogue

A data catalogue documents what data exists, what it means, who owns it, and who can access it.

```yaml
 # Example data catalogue entry (Datahub / Alation / dbt docs)
dataset: marts.learner_engagement
description: "Daily engagement metrics per learner. Source: xp_events + lesson_attempts."
owner: data-engineering@consortium.io
last_updated: 2024-03-15
sensitivity: INTERNAL (no PII)
columns:
  - name: user_id
    description: "Consortium user UUID. Foreign key to users.id"
  - name: completion_rate_7d
    description: "Lessons completed / lessons started in past 7 days"
    metric_definition: ref(lesson_completion_rate)
```

Without a catalogue, analysts can't find data, don't know what it means, and build their own copies.

## Common Mistakes

> **Skipping the Semantic Layer**
> Each dashboard author writes their own SQL. Revenue means six different things across six dashboards. Fix: invest in a semantic layer before proliferating dashboards.

> **Too Many Dashboards**
> 800 dashboards and nobody knows which ones are used, which are stale, or which are authoritative. Every dashboard should have an owner and a last-verified date. Establish a review cycle; archive unused dashboards.

> **Refreshing Dashboards Too Frequently**
> A daily metric refreshed every 5 minutes incurs constant warehouse query cost with no informational benefit. Match refresh frequency to metric update frequency. Daily metrics → daily refresh.

## Mental Model

Think of the BI stack as a **newspaper organisation**. Raw events are the wire service feeds (data sources). The editorial archive is the data warehouse (organised, searchable). The style guide and factbook is the semantic layer (how to write revenue, what an active subscriber means). Reporters (exploratory BI) investigate and write original pieces. The newspaper (operational BI) presents finished stories to readers. Without the style guide, two reporters will define "subscriber" differently and publish contradicting stories.

**Mini Summary**: The BI stack connects sources → warehouse → semantic layer → BI tools. The semantic layer defines metrics once, ensuring consistent numbers across all consumers. Operational BI serves fixed dashboards to stakeholders; exploratory BI enables ad-hoc investigation. Self-serve BI requires governance: access control, semantic layer, dashboard ownership. Data catalogues document what data exists and what it means.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium's stakeholders want three dashboards:
1. Executive: weekly active learners, completion rate, revenue — refreshed daily
2. Product: completion rate by module with drill-down to individual lessons — refreshed daily
3. Learner: personal progress — XP earned, lessons completed, badges — refreshed hourly

For each dashboard:
1. What BI tool capabilities are needed (operational vs exploratory, drill-down, etc.)?
2. What mart tables from the warehouse would it query?
3. What access controls should be applied (which users see which data)?

---

# Integration

**Mathematics**: OLAP drill-down implements hierarchical **data cube** navigation. A data cube is a multi-dimensional array where each dimension is a hierarchy (e.g. time: day < week < month < year). Aggregating from a finer to a coarser level is the **group-by reduction** — a fold operation over the dimension's values. Pre-aggregating all combinations of dimensions produces a **materialised OLAP cube** with O(2^d) cells for d dimensions — exponential in dimensionality. Modern columnar warehouses avoid pre-aggregation by computing GROUP BY on demand at sub-second speed, replacing materialised cubes with on-demand vectorised aggregation.

**Sciences**: The BI stack mirrors the **information hierarchy in scientific publishing**: raw observations (data sources) → processed datasets (warehouse) → peer-reviewed analysis (semantic layer + marts) → published papers (operational dashboards) → science journalism (executive summaries). Each step adds interpretation and loses granularity but gains accessibility. The semantic layer plays the role of peer review: ensuring definitions are rigorous and reproducible. Just as replication crises in science trace back to inconsistent operationalisation of concepts, BI discrepancies trace back to inconsistent metric definitions.

---

# The Single Definition

The semantic layer was deployed. All dashboards were migrated to query through it. Finance and product both reported the same revenue number in the next board meeting: €2.2M, up 11%. "Which of you is right?" the chair asked. "Both," the Lead Data Engineer said. "For the first time."
