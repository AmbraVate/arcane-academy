---
id: de-lead-m6-01
school: DATA_ENGINEERING
domainId: data_engineering
tier: LEAD
moduleId: de-lead-m6
moduleTitle: "Capstone Quest"
moduleGlyph: "👑"
moduleSortOrder: 6
topicSlug: capstone
topicTitle: "Capstone"
topicSortOrder: 1
lesson: capstone
title: "The Grand Data Architect"
sortOrder: 1
difficulty: 10
estimatedMinutes: 1440
xpReward: 2000
practiceType: NONE
questType: MASTERY
retrievalWeight: high
questTypes: [solo]
prerequisites: [de-lead-m5-04]
integrationDomains: [software_engineering, economics, philosophy, ethics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Produces a coherent conceptual data model that accurately represents domain entities, relationships, and cardinality
    - Translates the conceptual model into a sound relational database design with appropriate normalisation, primary keys, and foreign keys
    - Implements correct SQL for schema creation, complex queries (JOINs, CTEs, window functions), and data quality constraints
    - Defines a complete data quality programme with validation rules, monitoring, and remediation procedures
    - Designs reporting and analytical queries appropriate for business intelligence consumers
    - Addresses security through role-based access control, data classification, and audit logging
    - Produces a governance strategy covering data ownership, lineage, cataloguing, and compliance
    - Proposes a scalability roadmap that anticipates growth and technological evolution
  keywords:
    - conceptual model
    - entity relationship
    - normalisation
    - SQL
    - CTE
    - window function
    - data quality
    - constraints
    - RBAC
    - audit
    - data governance
    - lineage
    - data catalogue
    - compliance
    - scalability
    - partitioning
    - indexing
    - reporting
    - GDPR
    - data strategy
  modelAnswer: |
    The Grand Data Architect capstone requires synthesis across the full data engineering discipline: from strategic thinking (data as an asset, governance, ethics) through technical implementation (modelling, SQL, quality controls) to leadership (change management, communication to non-technical stakeholders).

    Conceptual modelling: the Kingdom's knowledge archive contains Scholars (who produce), Works (what they produce), Topics (how knowledge is organised), Collections (curated groupings), and Loans/Access Records (how knowledge is accessed). The key design decisions are: how to handle the many-to-many relationship between Works and Topics (junction table); whether Scholars are people or roles (important for the privacy and audit requirements); how to model the hierarchical nature of Topics (adjacency list, nested sets, or materialised path — each with different query trade-offs).

    Relational design: third normal form eliminates redundancy and maintains integrity. Natural keys (scholar names, topic names) are fragile — surrogate keys (sequential integers or UUIDs) are more stable. Foreign key constraints enforce referential integrity. CHECK constraints enforce domain validity at the database level.

    SQL: complex reporting requires window functions (ranking scholars by productivity within topic; running totals of scroll production over time), CTEs (for readable multi-step analytical queries), and appropriate JOINs. Correlated subqueries are a common mistake that becomes a performance problem at scale — set-based operations with JOINs are preferable.

    Data quality: quality is not just about constraints — it is about processes. An ingestion pipeline that validates before inserting is more robust than catching violations at the database layer. But database constraints are the last line of defence. Monitoring requires measuring: null rates, constraint violation rates, referential integrity failures, duplicate detection.

    Governance: the Knowledge Archive is a cultural and historical asset. Data ownership (who is responsible for which data), lineage (where did each record come from), retention (how long is each type of record kept, and who can authorise deletion), and access (who can see what, and is access logged) are governance fundamentals that cannot be retrofitted — they must be designed in.
---

# The Grand Data Architect

## The Final Trial

*"You have mastered the data arts,"* the Grand Archivist announces. *"You have learned to model, to query, to govern, to protect, and to lead. You have studied how knowledge is created, stored, transformed, and lost."*

*"The final trial asks you to do all of it — for real, under constraint, for an organisation that depends on you to get it right."*

*"The Academy's Knowledge Archive has existed for four centuries. It has never had a data strategy. Your task is to give it one."*

---

## The Scenario

The **Arcane Academy Knowledge Archive** holds four centuries of scholarly output: scrolls, treatises, experimental records, and correspondence. It has grown from a few hundred documents to an estimated 2.4 million records — but nobody knows the exact count. Records are held in paper catalogues maintained by individual archivists. The head archivist is retiring in 8 months.

The Academy's Grand Council has mandated a full digital transformation of the Archive. You have been appointed **Chief Data Architect**. Your deliverable: a complete data platform that will outlast its creators and serve the Academy for the next century.

**What exists:**
- Paper catalogue entries (scholar name, work title, topic, date, location reference)
- Archivist personal notes (handwritten, inconsistent)
- Loan records (handwritten ledgers: borrower, date, return date, condition)
- A partial digital index created 10 years ago (covering approximately 15% of records, inconsistently formatted)

**Requirements from the Grand Council:**
1. All archive records searchable by any Scholar within 2 years
2. Loan and access records maintained for audit purposes
3. Historical records preserved with attribution and provenance
4. Sensitive correspondence protected from general access
5. Reporting on knowledge production trends across centuries
6. The system must be maintainable by archivists, not data engineers, after Year 3

---

## Part 1: Conceptual Data Model

Before any database design or SQL, you must understand the domain.

**Your task:**
1. Identify the core entities of the Knowledge Archive domain. For each entity, list its most important attributes and explain why you have defined it as a distinct entity rather than an attribute of another.
2. Define the relationships between entities. For each relationship, specify the cardinality (one-to-one, one-to-many, many-to-many) and the business rule that determines it. Where cardinality is debatable, state the assumption you are making and why.
3. Identify at least two design decisions in your conceptual model where you had to choose between multiple valid approaches. For each, describe the alternatives and justify your choice.

**Minimum:** 350 words with a clear entity list, relationship definitions, and explicit design decision discussion.

---

## Part 2: Relational Database Design

Translate your conceptual model into a relational schema.

**Your task:**
1. Define the table structure for your core entities. For each table, specify: the column name, data type, whether null is allowed, and any constraints (primary key, foreign key, unique, check). Use standard SQL data types (VARCHAR, INTEGER, DATE, TEXT, BOOLEAN, TIMESTAMP).
2. Explain the normalisation decisions you made. Which tables are in third normal form, and where did you deliberately denormalise for performance? Justify any deliberate denormalisation.
3. The Academy's topic taxonomy is hierarchical (e.g., Natural Philosophy → Alchemy → Transmutation → Metal Transmutation). How do you represent this in a relational database? Describe at least two approaches and select the one appropriate for the query patterns the Archive will require.

**Minimum:** Present your table definitions in SQL CREATE TABLE format (you may use comments to explain decisions), plus 250 words of design justification.

---

## Part 3: SQL Implementation

Write SQL that demonstrates mastery of the techniques required to operate this Archive.

**Your task — write the following SQL:**

1. **Data retrieval:** A query that returns the top 10 most prolific scholars of the 15th century (1400–1499), ranked by number of works, with their most common topic area. Use a CTE to make the query readable.

2. **Window functions:** A query that shows, for each decade from 1600 to 1900, the total number of works produced, the running total of works produced from 1600 to that decade, and the decade's percentage contribution to the total 300-year output.

3. **Data quality check:** A query that identifies potential duplicate works — cases where the same title appears more than once with the same scholar name, but different dates or locations. Include the suspected duplicate records in the output.

4. **Access control view:** A view definition that exposes works for general scholar access but excludes records tagged as `restricted` or `sensitive_correspondence`. The view should include only the columns appropriate for general consumption.

For each query, explain in 1-2 sentences what it does and why you wrote it this way.

**Minimum:** Four working SQL statements with explanations.

---

## Part 4: Data Quality Programme

The partial digital index was created 10 years ago and is notoriously unreliable. The paper records have inconsistent formatting. The migration will produce dirty data.

**Your task:**
1. Define the data quality dimensions that matter most for the Knowledge Archive. For each dimension (completeness, accuracy, consistency, timeliness, uniqueness — or others you identify as relevant), describe what "good quality" looks like for this domain and how you would measure it.
2. Design the validation rules that would run during data ingestion. For each rule, specify: what it checks, how it responds to a violation (reject, flag for review, accept with warning), and who is responsible for resolving flagged records.
3. Define the ongoing data quality monitoring process. Once the Archive is operational, how do you detect quality degradation before it becomes a crisis? What are your thresholds, who receives alerts, and what is the remediation process?

**Minimum:** 350 words covering all three parts with specific rules, metrics, and process ownership.

---

## Part 5: Reporting and Analytics

The Grand Council needs to understand knowledge production trends across four centuries to inform resource allocation and strategic priorities.

**Your task:**
1. Define the three most strategically important reports for the Grand Council. For each, describe: the business question it answers, the data it requires, and a sketch of how you would construct the query.
2. The Council wants a "Century Review" — a summary of knowledge production across each of the last four centuries: works produced, active scholars, topic diversity, and top 3 contributing scholars. Write the SQL for this report using window functions and CTEs.
3. The archivists are not SQL-literate. How do you make the reporting capability available to them? Describe the technical approach (views, reporting tools, dashboards) and the governance model (who can create new reports, how are report definitions maintained).

**Minimum:** 300 words covering the strategic reports, the Century Review SQL, and the non-technical access approach.

---

## Part 6: Security and Compliance

The Archive contains sensitive correspondence, personal records of scholars (some of whom have living descendants), and donor records that are subject to confidentiality agreements.

**Your task:**
1. Classify the Archive's data into sensitivity tiers. Define at least three tiers, explain what belongs in each tier, and specify who has read/write access by role.
2. Design the role-based access control model. Define the roles that exist, what each role can do, and how role assignment is governed (who grants access, and what approval is required).
3. Define the audit logging requirements. Which actions must be logged, what information must each log entry contain, who can view audit logs, and how long are they retained? Explain how audit logs would be used in a hypothetical security investigation.
4. The Archive contains records about scholars who have living descendants. Under modern data protection principles (similar to GDPR), what obligations does the Archive have, and how does your technical design support compliance?

**Minimum:** 350 words covering data classification, RBAC, audit logging, and data protection compliance.

---

## Part 7: Governance Strategy

A data platform without governance decays. After 5 years, unmanaged data platforms look like the paper catalogue you were hired to replace.

**Your task:**
1. Define the data ownership model. For each major data domain in the Archive (Works, Scholars, Access Records, Sensitive Correspondence), who is the data owner, what are their responsibilities, and how are disputes resolved?
2. Design the data lineage tracking approach. How will you record where each record came from (paper catalogue, digital index, archivist manual entry, external donation) and what transformations it has undergone? How will you use lineage to investigate data quality incidents?
3. Define the data catalogue structure. What information will each entry in the catalogue contain, who maintains it, and how is it kept current as the Archive evolves?
4. The head archivist retires in 8 months. How do you ensure the governance model survives the departure of the person who built it? What documentation, training, and succession planning is required?

**Minimum:** 350 words covering data ownership, lineage, cataloguing, and knowledge continuity.

---

## Part 8: Scalability Roadmap

The Archive currently holds an estimated 2.4 million records. Digital submissions are growing at 15% per year. The Academy intends to acquire three regional archive collections in the next decade.

**Your task:**
1. Identify the three scaling challenges that are most likely to emerge in the next 10 years. For each, describe the symptom that would alert you to the problem, and the technical approach you would apply.
2. The full-text search requirement ("searchable by any Scholar within 2 years") will eventually exceed what a relational database can serve efficiently. Describe the approach you would take to full-text search: at what scale would you introduce a dedicated search capability, what technology would you choose, and how would you keep it synchronised with the relational database?
3. Write a one-page non-technical roadmap for the Grand Council that describes: the current state, the 2-year target, the 5-year target, and the three most important decisions the Council needs to make in Year 1. Write it for a reader who does not know what a database is.

**Minimum:** 350 words covering the scaling challenges, the search strategy, and the Council roadmap in non-technical language.

---

## Evaluation

Your response is evaluated on eight dimensions:

| Dimension | What is assessed |
|---|---|
| **Conceptual accuracy** | Entities, relationships, and cardinality correctly modelled |
| **Relational design** | Normalisation, constraints, and schema correctness |
| **SQL quality** | Correctness, efficiency, and appropriate use of advanced features |
| **Data quality rigour** | Specific rules, measurable thresholds, and process ownership |
| **Security completeness** | Classification, RBAC, audit logging, and compliance reasoning |
| **Governance sustainability** | Ownership, lineage, cataloguing designed to outlast individuals |
| **Scalability reasoning** | Anticipating growth with specific technical approaches |
| **Stakeholder communication** | Technical decisions translated for non-technical audiences |

---

*"The Knowledge Archive has stood for four centuries,"* the Grand Archivist says. *"Scholars have come and gone. Archivists have retired and been replaced. The knowledge itself has survived — because enough people, at enough moments, made the choice to preserve it."*

*"Your task is not to build a database. Your task is to build a system that makes preservation the path of least resistance — for every scholar, archivist, and administrator who will interact with it for the next century."*

*"The Academy awaits your design."*

---
