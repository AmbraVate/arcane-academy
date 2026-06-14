---
id: de-sen-m6-01
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m6
moduleTitle: "Module 6: Data Governance"
moduleGlyph: "🏛️"
moduleSortOrder: 6
topicSlug: ownership
topicTitle: "Ownership"
topicSortOrder: 1
lesson: 1
title: "Data Ownership: Who is Responsible for This Data?"
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
  - de-sen-m5-04
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines data ownership and distinguishes it from data stewardship"
    - "Explains what a data owner is accountable for"
    - "Describes the data mesh principle of domain ownership"
    - "Identifies consequences of undefined ownership"
  keywords:
    - data owner
    - data steward
    - data mesh
    - domain ownership
    - accountability
    - data product
    - RACI
  modelAnswer: |
    A data owner is a business-side accountable party responsible for the quality, access decisions, and lifecycle of a dataset. They define who can access data and for what purpose. A data steward is a technical implementer who enforces the owner's decisions — maintaining pipelines, applying access controls, documenting schemas.
    Without defined ownership, no one is accountable for data quality problems, access requests sit unanswered, and schema changes break downstream consumers without warning. Data quality degrades silently.
    Data mesh is an organisational pattern where domain teams own their data end-to-end — they produce, maintain, and publish data products, and are accountable for their quality SLAs. The central data team provides platform infrastructure; domain teams own their slice of the data landscape.
    A RACI (Responsible, Accountable, Consulted, Informed) matrix maps data governance activities to roles, ensuring no activity is unowned. For any governance decision, there must be exactly one Accountable party.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "The `user_profiles` table has incorrect email addresses for 3% of users. Nobody has fixed it for 6 months. What governance failure does this represent?"
    options:
      - "A technical failure — the ETL pipeline has a bug"
      - "An ownership failure — no one is accountable for the quality of user_profiles"
      - "A stewardship failure — the data steward failed to run quality checks"
      - "A compliance failure — GDPR requires accurate email addresses"
    correctIndex: 1
    explanation: "The root cause is an ownership failure. A data owner who is accountable for user_profiles quality would have escalated this the moment it was detected. Without an owner, the problem sits in a gap — everyone can see it, nobody is responsible for fixing it. Technical and stewardship failures may co-exist, but the enabling condition is the absence of accountability."
  - type: FILL_BLANK
    question: "In data mesh, each ___ team owns its data end-to-end — producing, maintaining, and publishing it as a data product with a defined quality SLA."
    answer: "domain"
    explanation: "Data mesh's core principle is domain ownership. The learner engagement team owns learner_events; the payments team owns transaction_events. Each team runs its own pipelines, maintains its own schemas, and publishes data products to a data platform that other teams can consume. Central data teams shift from building everything to providing platform infrastructure."
  - type: SHORT_TEXT
    question: "What is a data product in the data mesh model, and how does it differ from a database table?"
    modelAnswer: "A data product is a dataset published by a domain team with a formal contract: schema definition, quality SLAs (freshness, completeness), access policy, and documentation. It is treated like an external-facing product — versioned, monitored, and with breaking changes communicated to consumers. A database table is an internal implementation detail — no SLA, no versioning, no consumer contract. A domain team might implement a data product as a set of Parquet files, a BigQuery table, or a streaming topic — the consumer sees the contract, not the implementation."
microCheckpoint:
  question: "What is the difference between a data owner and a data steward?"
  answer: "A data owner is a business-accountable person who decides what data exists, who can access it, and what quality it must meet. A data steward is a technical implementer who enforces those decisions — maintaining pipelines, access controls, and documentation. Owner = accountability. Steward = execution."
retrieval:
  recall: "What are the four roles in a RACI matrix and what does each mean?"
  explain: "Explain how undefined data ownership leads to data quality degradation over time."
  mistakeId: "data-ownership-undefined"
---

# Nobody's Data

"The learner completion data has 8% null values in `domain_id`," the Senior Engineer reported. "It's been like this for 4 months." The Lead Data Engineer pulled up the data catalogue. No owner listed. "That's the problem. When data has no owner, it has no guardian. Problems accumulate and nobody is accountable to fix them." They opened the governance framework document. It was time to assign owners.

# What Data Ownership Means

**Data ownership** is not about who created the data or who physically stores it. It is about **accountability**: who is responsible for the data's quality, access decisions, and lifecycle.

```
Data Owner responsibilities:
  ✓ Defining data quality standards for this dataset
  ✓ Approving or denying access requests
  ✓ Deciding how long data is retained
  ✓ Communicating schema changes to consumers
  ✓ Escalating quality problems to engineering
  ✓ Signing off on data used in key business decisions
```

The data owner is typically a **business stakeholder**, not an engineer. The product manager who understands what lesson completion means owns `fact_lesson_completions`. They define what "complete" means, which edge cases are excluded, and who can access learner progress data.

## Data Owner vs Data Steward

| Role | Who | Accountability |
|---|---|---|
| **Data Owner** | Business stakeholder (product manager, domain lead) | Quality standards, access policy, lifecycle decisions |
| **Data Steward** | Data engineer or analyst | Technical implementation: pipelines, access controls, documentation, monitoring |
| **Data Consumer** | Analyst, data scientist, BI tool | Using data within agreed access boundaries |

The owner sets the policy; the steward enforces it. If data quality degrades, the owner is escalated to — even if the steward detects the problem first.

## RACI Matrix for Data Governance

For any governance activity, assign each role using RACI:

| Activity | Owner (Business) | Steward (Engineering) | Consumer | Governance Team |
|---|---|---|---|---|
| Define quality standards | **A** (Accountable) | R (Responsible) | C (Consulted) | I (Informed) |
| Grant data access | **A** | R | I | C |
| Schema change approval | **A** | R | C | I |
| Detect quality issue | I | **R** | R | I |
| Retain/delete data | **A** | R | I | C |

**Exactly one Accountable role per activity.** If two parties are both Accountable, neither is.

## The Cost of Undefined Ownership

Without owners, governance breaks down predictably:

```
Timeline of an unowned dataset:
  Month 1:  Created for a project. No owner assigned.
  Month 3:  Producer adds a new column without telling consumers.
  Month 4:  3% of rows have null domain_id. Noticed, but who fixes it?
  Month 6:  8% null values. Report silently wrong.
  Month 9:  ETL changed. Old column removed. 2 downstream dashboards break.
  Month 12: "Can I have access to this table?" — No one to ask.
  Month 18: Table is 60GB. Nobody knows if it's still used. Nobody deletes it.
```

Each failure mode has a clear governance solution: owner assigns steward, monitors quality, approves access, reviews lifecycle.

## Data Mesh: Domain Ownership at Scale

In large organisations, centralised data ownership becomes a bottleneck. Data mesh distributes ownership to **domain teams**.

```
Traditional (centralised):
  All data → Central Data Team → BI tools
  Central team: owns all pipelines, all schemas, all quality
  Problem: bottleneck, deep domain knowledge gaps

Data Mesh (distributed):
  Learner Domain Team → learner_events data product
  Payments Team      → transaction_events data product
  Content Team       → lesson_engagement data product
  
  Central Platform:   provides data lake, compute, governance tooling
  Domain Teams:       own their data products, SLAs, schemas
```

### Data Product Contract
```yaml
data_product: learner_engagement_events
owner: learner-platform-team@consortium.io
domain: learner
version: "2.1"
sla:
  freshness: "< 1 hour"
  completeness: "> 99.5%"
  availability: "> 99.9%"
schema_file: schemas/learner_engagement_events_v2.json
access_policy: "Internal use only. PII fields require DPO approval."
breaking_change_notice: "30 days minimum"
consumers:
  - analytics-team
  - ml-team
```

Consumers know what to expect. The domain team is accountable for meeting the SLA. Breaking changes require advance notice.

## Data Catalogue as Ownership Directory

The data catalogue is where ownership is declared and discoverable:

```yaml
 # Data catalogue entry
dataset: fact_lesson_completions
owner: product-learner-team@consortium.io
steward: data-engineering@consortium.io
description: "One row per lesson completion event. Grain: user × lesson × attempt."
sensitivity: INTERNAL (pseudonymised user_id only)
quality_sla:
  freshness: "< 2 hours"
  completeness: "> 99.8% non-null domain_id"
last_quality_check: 2024-03-15
consumers:
  - learner-dashboard
  - executive-reporting
  - ml-recommendation-model
schema_version: "3.2"
```

## Common Mistakes

> **Assigning Ownership to a Team Instead of a Person**
> "The data engineering team owns user_profiles" means nobody owns it. A team cannot be paged, escalated, or held accountable. Ownership belongs to a named individual.

> **Engineering-Only Ownership**
> Assigning data ownership to an engineer without a business counterpart means quality standards are technical (not-null, unique) but lack business context (what does "active user" mean?). Business owners understand the domain semantics.

> **No Ownership Review**
> People change roles. An owner who left the organisation 6 months ago cannot respond to access requests. Governance frameworks require ownership reviews (quarterly or on role change).

## Mental Model

Think of data ownership as **property ownership for a city**. Every building (dataset) has an owner on the land registry (data catalogue). The owner is responsible for maintenance (quality), security (access control), and use compliance (purpose limitation). Unowned buildings (no registered owner) fall into disrepair, attract squatters (unauthorised access), and eventually become liabilities. The land registry doesn't maintain buildings — it records who is responsible. The owner decides; the steward (building manager) executes.

**Mini Summary**: Data owners are accountable for quality, access, and lifecycle. Data stewards implement technical enforcement. Undefined ownership leads to quality degradation, access confusion, and schema-breaking changes. Data mesh distributes ownership to domain teams who publish data products with formal contracts and SLAs. Document ownership in a data catalogue; assign named individuals, not teams.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium has 15 key datasets. Currently no ownership is defined for any of them. The datasets include: `learner_events`, `lesson_completions`, `xp_transactions`, `user_profiles`, `subscription_payments`, `content_metadata`, `audit_log`, and 8 others.

Design an ownership assignment process:
1. How would you identify appropriate business owners for each dataset?
2. What minimum information should each data catalogue entry contain for a governed dataset?
3. `subscription_payments` contains financial PII. Who should be the owner — the product team, finance, or a legal/compliance officer? Justify your answer.

---

# Integration

**Mathematics**: Data mesh governance can be modelled as a **graph problem**. Datasets are nodes; dependencies (a dataset consuming another as input) are directed edges. A dataset with no incoming ownership edge is ungoverned — the in-degree of the "ownership" meta-node for that dataset is 0. Finding all ungoverned datasets is a graph reachability problem. In a well-governed data mesh, every dataset node has exactly one ownership edge (out-degree = 1 in the ownership graph, in-degree ≥ 0 from consumers). The governance framework's task is to ensure this invariant holds — formally, that the ownership graph has no sink nodes (datasets with out-degree 0 in the ownership assignment).

**Sciences**: Data ownership mirrors **ecological responsibility in conservation biology**. Each species (dataset) must have a designated steward (conservation program) accountable for its health. The tragedy of the commons — shared resources degraded because nobody bears individual responsibility — is exactly the failure mode of unowned data. Garrett Hardin's 1968 paper described how commons without ownership governance collapse. The data mesh's domain ownership principle is the data engineering equivalent of assigning clear territorial stewardship — preventing the data tragedy of the commons.

---

# The Assigned Archive

Three weeks after the governance workshop, every dataset had a named owner. The `fact_lesson_completions` owner — the product lead — immediately noticed the 8% null values and escalated to the data engineer. The null rate dropped to 0.1% within a week. "The quality didn't improve because we added monitoring," the Lead Data Engineer said. "It improved because someone was now accountable for it." Accountability, it turned out, was more powerful than any monitoring alert.
