---
id: de-app-m1-04
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m1
moduleTitle: "Module 1: Understanding Data"
moduleGlyph: "📊"
moduleSortOrder: 1
topicSlug: what_is_data
topicTitle: "What is Data?"
topicSortOrder: 1
lesson: the_data_lifecycle
title: "The Data Lifecycle"
sortOrder: 4
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m1-01, de-app-m1-02, de-app-m1-03]
integrationDomains: [sciences, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Names and briefly describes all major stages of the data lifecycle
    - Explains what can go wrong at each stage if it is poorly managed
    - Identifies the role of a data engineer in at least two lifecycle stages
    - Reflects on why data must eventually be archived or deleted
    - Connects lifecycle thinking to system design decisions
  keywords: [collection, storage, processing, analysis, sharing, archiving, deletion, governance, lifecycle]
  modelAnswer: |
    The data lifecycle begins with collection, moves through storage, processing, and analysis, then into sharing and consumption, and finally into archiving or deletion. At each stage, quality and governance decisions shape the value of the data. Data engineers are most active in the collection, storage, and processing stages — building pipelines that reliably move clean data from source to destination. Understanding the full lifecycle ensures engineers design systems that are sustainable, compliant, and purpose-driven from the start.
guidedSteps:
  - id: de-app-m1-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In which lifecycle stage does a data engineer typically build ETL pipelines?
    inputConfig:
      options:
        - "Collection only"
        - "Processing and transformation"
        - "Archiving and deletion"
        - "Sharing with end users"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Processing and transformation"]
      rejectedFeedback: "ETL (Extract, Transform, Load) pipelines are the core of the processing stage — they clean, reshape, and move data from raw sources into analytical stores."
    hint: "ETL stands for Extract, Transform, Load — which lifecycle stage does that describe?"
    reflectionPrompt: "What would happen to the downstream analysis if the transformation step introduced errors?"
  - id: de-app-m1-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence: "The final stage of the data lifecycle, where data is either preserved for long-term compliance or permanently removed, is called ________ or deletion."
    inputConfig:
      placeholder: "archiving"
    markingRule:
      matchMode: CONTAINS
      accepted: [archiving, archive, archival, retention]
      rejectedFeedback: "Archiving is the process of moving data that is no longer actively needed to long-term, lower-cost storage for compliance or historical reference — before eventual deletion."
    hint: "Think about what libraries do with old records they need to keep but not actively use."
    reflectionPrompt: "Under GDPR, organisations must delete personal data when it is no longer needed. How does this create engineering requirements?"
  - id: de-app-m1-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why understanding the full data lifecycle matters when designing a data pipeline, rather than focusing only on the collection stage.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [downstream, quality, storage, archiving, compliance, governance, design, future]
      rejectedFeedback: "A strong answer notes that decisions made early (like collection format or storage schema) affect every downstream stage, and that compliance and archiving requirements must be considered at design time."
    hint: "Think about how early design decisions create constraints for later stages."
    reflectionPrompt: "Have you seen a system where a short-term engineering decision caused long-term problems?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which data lifecycle stage involves cleaning, reshaping, and enriching raw data?"
    options: ["Collection", "Archiving", "Processing", "Sharing"]
    correctIndex: 2
    feedback: "Processing includes all transformations applied to raw data — cleaning nulls, standardising formats, joining sources — before the data is ready for analysis or consumption."
  - type: MULTIPLE_CHOICE
    question: "Why must organisations eventually archive or delete data rather than keeping all data indefinitely?"
    options:
      - "Old data is always inaccurate"
      - "Storage is free so there is no reason"
      - "Legal requirements, storage costs, and data relevance all demand lifecycle management"
      - "Data automatically corrupts after three years"
    correctIndex: 2
    feedback: "GDPR and other regulations mandate deletion of personal data when no longer needed; storage costs grow with volume; and old data may mislead analysis if not properly managed."
retrieval:
  recall: "Name the six main stages of the data lifecycle in order."
  explain: "Explain why decisions made in the collection stage affect every subsequent stage of the data lifecycle."
  mistakeId:
    code: "once data is collected, the hard work is done"
    answer: "Collection is only the first stage. Processing, quality assurance, governance, analysis, and eventual archiving or deletion each require engineering effort. Poor collection decisions create cascading problems through every downstream stage."
---

# Hook

Think of data as a living thing. It is born — collected from a sensor, a form, a transaction. It grows — processed, enriched, joined with other data. It is used — by analysts, dashboards, machine learning models. And eventually, it must be retired — archived for compliance, or permanently deleted when its purpose is served.

This journey from creation to deletion is called the **data lifecycle**. Every data engineer who designs a system without considering the full lifecycle will eventually face a crisis: a compliance audit for data that should have been deleted, a storage bill for petabytes of useless raw files, or a downstream pipeline that breaks because a source changed without warning.

What happens to data after it has been used? Have you ever thought about where your personal data goes when you close an account?

# Lore Introduction

"Every record has a life," Master Selvaris said, guiding her apprentice past a row of deep wooden drawers labelled by decade. "It is born when a clerk makes an inscription. It serves its purpose — trade, census, law. Then it must be stored properly, or it rots. Used carefully, or it misleads. And when its time has passed, it must be sealed in the deep vaults or burned, as the law requires." She paused at a drawer labelled *Destruction Register*. "I once knew an archivist who kept everything, forever, out of sentiment. When the inspectors came, they found records that should have been destroyed a century prior. He lost his post." She closed the drawer firmly. "We serve the lifecycle. We do not fight it."

# Core Learning

## Concept Introduction

| Stage | Description | Data Engineer's Role | Risk if Mismanaged |
|-------|-------------|---------------------|-------------------|
| **1. Collection** | Data is captured from sources — sensors, forms, APIs, transactions | Design ingestion pipelines; validate at point of entry | Incomplete, duplicate, or malformed data enters the system |
| **2. Storage** | Data is persisted in appropriate systems | Choose storage format, schema, and partitioning strategy | Wrong format causes query failures; poor partitioning creates performance problems |
| **3. Processing** | Raw data is cleaned, transformed, and enriched | Build ETL/ELT pipelines; apply business rules | Dirty data propagates downstream; incorrect transformations corrupt analysis |
| **4. Analysis** | Processed data is queried, visualised, or modelled | Build analytical models and data marts; document for consumers | Misinterpretation; analysts draw wrong conclusions from poorly structured data |
| **5. Sharing** | Information is distributed to stakeholders and systems | Build APIs, reports, dashboards; manage access controls | Data exposed to wrong audiences; privacy violations; stale data misrepresented as current |
| **6. Archiving / Deletion** | Data is moved to long-term storage or permanently removed | Implement retention policies and automated deletion jobs | Regulatory non-compliance; unnecessary storage costs; inability to respond to data subject access requests |

## Why It Matters

Thinking in lifecycle terms prevents the "pipeline tunnel vision" that affects many early-career engineers — the tendency to focus only on getting data from source to destination without considering what happens before and after. A system designed with the full lifecycle in mind:

- Captures data in the right format from the start
- Stores it efficiently for its expected query patterns
- Processes it reliably and traceably
- Makes it accessible to the right people at the right time
- Retains only what must be retained and deletes what must be deleted

## Worked Examples

**Example 1: Ride-Hailing App**
1. *Collection*: GPS location data from driver phones, trip requests from passenger app
2. *Storage*: Time-series database for location; relational DB for trips
3. *Processing*: Match drivers to passengers; calculate fares; detect anomalies
4. *Analysis*: Demand forecasting, surge pricing models
5. *Sharing*: Driver app, passenger app, finance reporting
6. *Archiving*: Trip records retained for 7 years for tax compliance; GPS tracks deleted after 90 days per privacy policy

**Example 2: University Admissions**
1. *Collection*: Online application forms
2. *Storage*: Application database
3. *Processing*: Scoring, deduplication, eligibility checking
4. *Analysis*: Admissions committee review dashboards
5. *Sharing*: Offer letters, UCAS reporting
6. *Archiving*: Accepted students' records transferred to student record system; rejected applications deleted after 12 months

## Common Mistakes

- **Skipping lifecycle planning**: Engineers who do not plan for archiving and deletion build systems that accumulate data indefinitely, creating cost and compliance problems.
- **Over-collecting data**: Gathering data "just in case" without a defined purpose violates GDPR and creates unnecessary storage and security obligations.
- **No data lineage tracking**: Without records of where data came from and what transformations were applied, diagnosing errors in later stages becomes extremely difficult.

## Mental Model

Think of the data lifecycle as a river system. Water (data) falls as rain (collection), flows into streams and rivers (storage and processing), reaches populated areas (analysis and sharing), and eventually returns to the sea (archiving) or evaporates (deletion). Engineers are the hydrologists — they design the channels, the dams, and the treatment plants that keep the flow clean, directed, and sustainable.

## Mini Summary

- ✔ The data lifecycle has six stages: collection, storage, processing, analysis, sharing, and archiving/deletion
- ✔ Decisions at each stage affect every subsequent stage
- ✔ Data engineers are most active in collection, storage, and processing
- ✔ Compliance requirements (GDPR) make archiving and deletion non-optional
- ✔ Lifecycle thinking prevents short-term engineering decisions from creating long-term problems

# Guided Practice Quest

Work through the guided steps to identify which lifecycle stage different engineering activities belong to and explain why full lifecycle awareness matters in system design.

# Solo Practice Quest

Design the data lifecycle for a fictional mobile fitness tracking app. For each of the six stages, describe: (1) what specific data is involved, (2) what happens to it at this stage, (3) what a data engineer would need to build or configure, and (4) what compliance or quality risks exist. Think about personal health data, GPS location, user-generated content, and payment records separately. Write approximately 400 words.

# Integration

**Sciences (Ecology)**: The concept of a lifecycle in data engineering mirrors the biological concept of a nutrient cycle. Nutrients (data) are collected from the environment (sources), processed by organisms (pipelines), consumed (analysis), and returned to the ecosystem (archiving). Just as disrupting the nitrogen cycle has consequences for the entire ecosystem, disrupting any stage of the data lifecycle creates downstream failures throughout the system.

**Mathematics**: The idea of data retention policy has a direct mathematical expression: a retention function `R(t)` that defines whether a record at age `t` should be kept or deleted. This function varies by data type (financial records: `t < 7 years`; session logs: `t < 90 days`) and is essentially a formal specification that a data engineer implements as automated deletion jobs. Thinking mathematically about retention makes policies precise and auditable.

# Lore Conclusion

Master Selvaris led her apprentice out of the vaults and back into the bright reading room. "You have seen the Archive's beginning and its end today," she said. "Most apprentices only think about filling the shelves. The wisest archivists think about the whole journey — from the moment the ink meets the parchment to the moment the record is sealed or burned." She set a fresh ledger on the table. "Design every system from creation to destruction. That is the mark of a master keeper."

---
