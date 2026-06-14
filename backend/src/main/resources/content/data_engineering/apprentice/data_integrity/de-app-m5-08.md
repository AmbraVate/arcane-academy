---
id: de-app-m5-08
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m5
moduleTitle: "Module 5: Data Quality"
moduleGlyph: "✅"
moduleSortOrder: 5
topicSlug: data_integrity
topicTitle: "Data Integrity"
topicSortOrder: 2
lesson: reliability
title: "Reliability"
sortOrder: 4
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m5-07]
integrationDomains: [software_engineering, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines data reliability in the context of a data system
    - Distinguishes reliability from accuracy and consistency
    - Explains how data pipelines can introduce reliability failures
    - Describes at least two practices that improve data reliability
    - Reflects on how to detect reliability problems before users do
  keywords: [reliability, pipeline, monitoring, alerting, audit log, data lineage, availability, trustworthy]
  modelAnswer: |
    Data reliability means that data is available, correct, and consistent when needed — that the system can be trusted to deliver the right data at the right time. Unlike accuracy (values reflect reality) or consistency (the same fact agrees everywhere), reliability is about the ongoing trustworthiness of the system over time. Pipelines introduce reliability risks when they fail silently, process data out of order, or produce partial loads. Practices that improve reliability include monitoring pipeline runs, alerting on failures, maintaining audit logs, and tracking data lineage so the source of every value is known and verifiable.
guidedSteps:
  - id: de-app-m5-08-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A nightly pipeline that loads sales data into a warehouse fails silently. The dashboard shows yesterday's data but users assume it is current. Which data quality dimension has been violated?
    inputConfig:
      options:
        - "Accuracy — the data values are wrong"
        - "Consistency — the same data is stored differently in two places"
        - "Reliability — the system failed to deliver current data without alerting anyone"
        - "Validity — the data violates format constraints"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Reliability — the system failed to deliver current data without alerting anyone"]
      rejectedFeedback: "Reliability is about the system consistently doing what it promises. A silent pipeline failure that leaves users looking at stale data is a reliability failure — not a data accuracy or consistency problem per se."
    hint: "The failure here is not in the data values themselves — it is in the system's ability to deliver them on time and alert when it cannot."
    reflectionPrompt: "How would you design the pipeline differently to prevent this silent failure?"
  - id: de-app-m5-08-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "A record of when data was created, modified, or moved through a pipeline — including by which process and from which source — is called data ________."
    inputConfig:
      placeholder: "lineage"
    markingRule:
      matchMode: CONTAINS
      accepted: [lineage, provenance, audit trail, origin]
      rejectedFeedback: "Data lineage (or provenance) tracks the origin and history of data through a system — where it came from, what transformed it, and when. This is essential for diagnosing reliability failures."
    hint: "Think of this as a chain of custody for data — documenting where it has been."
    reflectionPrompt: "How would data lineage help you diagnose a report that suddenly shows wrong numbers?"
  - id: de-app-m5-08-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, describe two monitoring practices that improve the reliability of a data pipeline.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [monitor, alert, log, check, validate, freshness, count, threshold]
      rejectedFeedback: "Strong answers might include: row count checks (alert if a pipeline loads significantly fewer rows than expected), freshness checks (alert if data is older than a threshold), error logging, and pipeline status dashboards."
    hint: "Think about what you would want to know about a pipeline run — did it succeed? Did it load the expected volume of data?"
    reflectionPrompt: "What is the difference between a monitoring system that records failures and one that alerts on them proactively?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which practice would most directly improve the reliability of a data pipeline?"
    options:
      - "Adding more columns to the source tables"
      - "Implementing row count checks and alerts on pipeline failures"
      - "Switching from SQL to NoSQL storage"
      - "Increasing the frequency of backups"
    correctIndex: 1
    feedback: "Row count checks verify that the expected volume of data arrived. Combined with alerts on failures, they ensure the pipeline team knows immediately when something goes wrong — rather than users discovering stale data themselves."
  - type: MULTIPLE_CHOICE
    question: "What is data lineage?"
    options:
      - "The historical growth rate of a database over time"
      - "The chain of transformations and sources that produced a given data value"
      - "The backup schedule for a production database"
      - "The number of rows processed by a pipeline per second"
    correctIndex: 1
    feedback: "Data lineage tracks where data came from, what transformed it, and when — providing the chain of custody needed to diagnose quality issues and verify data provenance."
retrieval:
  recall: "Define data reliability and give one example of a reliability failure that is not caused by inaccurate data."
  explain: "Why is monitoring a data pipeline essential for reliability, even if the underlying data values are correct?"
  mistakeId:
    code: "if the data values are correct, the system is reliable"
    answer: "Reliability encompasses more than data correctness. A system can contain accurate data but fail to deliver it on time, fail silently, produce partial loads, or become unavailable during peak hours. Reliability requires monitoring, alerting, recovery processes, and SLA management — not just correct values in storage."
---

# Hook

An analyst opens the sales dashboard every morning at 9am to check the previous day's performance. For three weeks, everything looks normal. Then one Monday, the dashboard shows the same numbers as Friday. The pipeline failed on Saturday night and again on Sunday — silently. No alert. No error visible to the analyst. Two days of sales data are missing, and nobody knew until the analyst noticed that total figures seemed too low for a weekend.

Data reliability is the promise that your system will deliver the right data when it is needed — and that when it cannot, it will say so loudly. Building reliable data systems means going beyond correct values in storage to the ongoing health of the entire pipeline: its monitoring, alerting, logging, and recovery behaviour. A system that stores accurate data but delivers it unreliably cannot be trusted.

# Lore Introduction

Master Selvaris pointed to a brass bell mounted beside the Archive's main gate. "Every morning at dawn, the Night Keeper rings this bell if all scroll deliveries arrived safely," he said. "If the bell does not ring, we know something failed and we investigate immediately." He paused. "We do not wait for the scholars to discover that yesterday's shipment is missing. We tell them before they even ask." He nodded at the bell. "Reliability is not just about having the right scrolls. It is about knowing — and communicating — when you do not."

# Core Learning

## Concept Introduction

| Concept | Definition | Example |
|---------|-----------|---------|
| **Data reliability** | Trustworthy, timely, consistent delivery of data | Dashboard always shows data from the last hour |
| **Silent failure** | A pipeline fails without any alert or visible error | Nightly job crashes; dashboard shows stale data |
| **Data freshness** | How current the data is relative to the real world | "Updated 5 minutes ago" vs "Updated 3 days ago" |
| **Data lineage** | Chain of sources and transformations that produced a value | "This revenue figure came from OrdersDB, transformed by pipeline v2.3 at 14:00" |
| **Audit log** | Record of all changes to data, who made them, and when | `INSERT INTO audit_log (user_id, action, table, timestamp)` |
| **Row count check** | Validation that a pipeline loaded the expected volume of rows | Alert if fewer than 90% of expected rows arrived |
| **SLA (Service Level Agreement)** | Formal commitment on data availability and freshness | "Dashboard data is no more than 15 minutes old" |

## Why It Matters

Reliable data systems:
- Allow stakeholders to act on data with confidence
- Detect and report failures before users experience them
- Provide audit trails for regulatory compliance
- Enable root-cause analysis when problems occur

Unreliable systems erode trust — once stakeholders doubt the data, they stop using it, build their own spreadsheets, or make decisions without any data at all.

## Worked Examples

**Example 1: A simple freshness check**
```sql
-- Alert if the most recent sales record is more than 2 hours old
SELECT CASE
    WHEN MAX(created_at) < NOW() - INTERVAL '2 hours'
    THEN 'STALE DATA — pipeline may have failed'
    ELSE 'OK'
END AS freshness_status
FROM sales;
```

**Example 2: Row count monitoring**
```python
 # After pipeline runs, compare row counts
expected_rows = get_previous_day_count()
actual_rows = get_todays_row_count()
if actual_rows < expected_rows * 0.9:
    send_alert("Pipeline loaded fewer rows than expected")
```

**Example 3: An audit log table**
```sql
CREATE TABLE data_audit (
    id         SERIAL PRIMARY KEY,
    table_name VARCHAR(100) NOT NULL,
    record_id  INT NOT NULL,
    action     VARCHAR(10) NOT NULL,  -- INSERT, UPDATE, DELETE
    changed_by VARCHAR(100),
    changed_at TIMESTAMP DEFAULT NOW()
);
```

## Common Mistakes

- **Treating reliability as purely an infrastructure concern**: Data reliability also requires data-level checks — row counts, freshness thresholds, and business rule validations. Infrastructure reliability (uptime) and data reliability are different things.
- **Only monitoring for errors, not for anomalies**: A pipeline that runs successfully but loads zero rows is technically "successful" — it did not crash. Only a row count check reveals that it loaded nothing.
- **No recovery plan**: Monitoring identifies failures; a recovery plan specifies what to do when failure occurs — how to reprocess data, how to backfill missing records, and who is responsible.

## Mental Model

Think of a reliable data system like a reliable postal service. Reliability is not just about each letter being correct (accuracy) — it is about whether letters are delivered on time, every day, with confirmation of delivery, and with a process for redelivery when something goes wrong. A postal service that loses 5% of packages silently — no notification, no tracking — is not reliable, even if the 95% that arrive are perfectly addressed.

## Mini Summary

- ✔ Reliability means data is delivered correctly and on time, with visible failure when it is not
- ✔ Silent failures are the enemy of reliable systems — monitoring and alerting are mandatory
- ✔ Data freshness, row count checks, and audit logs are core reliability tools
- ✔ Data lineage enables root-cause analysis when reliability failures occur
- ✔ Reliable systems maintain stakeholder trust in data as a decision-making tool

# Guided Practice Quest

Work through the guided steps to identify reliability failures in pipeline scenarios and explain how monitoring and alerting improve trustworthiness.

# Solo Practice Quest

Design a monitoring plan for a nightly ETL pipeline that loads sales data from a transactional database into a reporting warehouse. Your plan should include: (1) a freshness check with a specific threshold and alert action; (2) a row count check with logic for what counts as an acceptable deviation; (3) an audit log schema for tracking all pipeline runs; (4) a recovery procedure for what happens when the pipeline fails. Write each component as a brief specification (3–5 sentences each). Then reflect: how would you communicate a data reliability SLA to a non-technical stakeholder?

# Integration

**Software Engineering**: The concept of observability in software engineering — making a system's internal state visible through logs, metrics, and traces — applies equally to data pipelines. A data system is observable when engineers can determine its health from its outputs. Row counts, freshness metrics, and lineage graphs are the data engineering equivalents of service metrics and distributed traces.

**Psychology**: Trust is built slowly through consistent behaviour and lost quickly through unexpected failures. Research shows that users who experience a single significant data failure (e.g., a dashboard showing wrong figures during a board meeting) reduce their trust in that system for months, even if it subsequently performs perfectly. Designing reliable data systems is as much about maintaining human trust as it is about technical correctness.

# Lore Conclusion

Master Selvaris rang the bell himself, the clear tone echoing through the Archive. "Every morning this bell tells the scholars: the Archive is ready. Your work can begin." He straightened the rope. "On the three mornings in four hundred years when the bell did not ring, every scholar in the Kingdom knew within minutes that something had gone wrong. We did not wait for them to discover it themselves." He handed the rope to you. "Reliability is not just the absence of failure. It is the presence of trust. Build systems worthy of it."

---
