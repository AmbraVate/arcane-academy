---
id: de-app-m5-04
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m5
moduleTitle: "Module 5: Data Quality"
moduleGlyph: "✅"
moduleSortOrder: 5
topicSlug: data_accuracy
topicTitle: "Data Accuracy"
topicSortOrder: 1
lesson: duplicate_data
title: "Duplicate Data"
sortOrder: 4
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m5-03]
integrationDomains: [mathematics, software_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines a duplicate record and explains why duplicates occur
    - Distinguishes between exact duplicates and near-duplicates
    - Describes at least two strategies for detecting duplicates
    - Explains the consequences of duplicate data in aggregate queries
    - Reflects on how to prevent duplicates at the design stage
  keywords: [duplicate, deduplication, DISTINCT, PRIMARY KEY, unique, aggregation, bloat, near-duplicate]
  modelAnswer: |
    A duplicate record is a row that represents the same real-world entity as one or more other rows in a dataset. Duplicates arise from multiple data imports, concurrent insertions, or merged datasets. Exact duplicates have identical values across all columns; near-duplicates differ in minor fields (whitespace, formatting, abbreviated names). Detection strategies include using DISTINCT, GROUP BY with HAVING COUNT(*) > 1, or fuzzy matching algorithms. Duplicates corrupt aggregations — a customer counted twice inflates revenue and skews averages. Prevention relies on PRIMARY KEY constraints, UNIQUE constraints, and idempotent ingestion pipelines.
guidedSteps:
  - id: de-app-m5-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A query counts distinct customers who placed orders last month and returns 1,200 — but the marketing team says there were only 800. Which problem is most likely?
    inputConfig:
      options:
        - "Some customer records have NULL IDs"
        - "Duplicate order records are inflating the customer count"
        - "The wrong date range was used in the WHERE clause"
        - "The customers table has no PRIMARY KEY"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Duplicate order records are inflating the customer count"]
      rejectedFeedback: "If the same customer appears multiple times in the orders table, a COUNT(DISTINCT customer_id) should handle it — but a COUNT without DISTINCT would inflate the number. The most likely cause here is duplicate rows."
    hint: "If the reported count is 50% higher than expected, what would cause the same entity to be counted multiple times?"
    reflectionPrompt: "How would you use SQL to verify whether duplicate rows are the cause?"
  - id: de-app-m5-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "To find duplicate rows in a table grouped by customer email, you would use GROUP BY email HAVING COUNT(*) ________ 1."
    inputConfig:
      placeholder: "> 1"
    markingRule:
      matchMode: CONTAINS
      accepted: ["> 1", ">1", "greater than 1", "more than 1"]
      rejectedFeedback: "HAVING COUNT(*) > 1 filters for groups where more than one row shares the same email — these are your duplicates."
    hint: "You want groups where there is more than one row per email address."
    reflectionPrompt: "What would you do with the duplicate rows once you've found them?"
  - id: de-app-m5-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain two database design choices that prevent duplicate records from being inserted in the first place.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [PRIMARY KEY, UNIQUE, constraint, design, prevent, insert]
      rejectedFeedback: "PRIMARY KEY ensures each row has a unique identifier. UNIQUE constraints on business keys (like email) prevent logical duplicates. Together they prevent duplicates at the database level."
    hint: "What SQL constraints ensure a row can only appear once in a table?"
    reflectionPrompt: "Why might these constraints not be enough if data arrives from external systems?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What SQL keyword removes duplicate rows from a SELECT result?"
    options: ["UNIQUE", "DISTINCT", "DEDUPLICATE", "FILTER"]
    correctIndex: 1
    feedback: "SELECT DISTINCT removes duplicate rows from the result set. It does not alter the underlying table — it filters the output."
  - type: MULTIPLE_CHOICE
    question: "A table has no PRIMARY KEY. A nightly import job runs twice due to a bug. What is the most likely result?"
    options:
      - "The database raises an error and refuses the second import"
      - "All rows from the first import are overwritten by the second"
      - "All rows from the import are inserted a second time, creating duplicates"
      - "The import succeeds but the duplicate rows are flagged automatically"
    correctIndex: 2
    feedback: "Without a PRIMARY KEY or UNIQUE constraint, the database has no mechanism to detect that identical rows already exist. Both runs insert successfully, doubling the data."
retrieval:
  recall: "Define a duplicate record and give one example of how duplicates arise in a real system."
  explain: "How do PRIMARY KEY and UNIQUE constraints prevent duplicate data, and what are their limitations?"
  mistakeId:
    code: "using DISTINCT in all queries solves the duplicate problem"
    answer: "DISTINCT filters duplicates from query output but does not remove them from the database. The underlying data remains duplicated, which corrupts aggregations in queries that don't use DISTINCT, wastes storage, and misleads anyone who queries the raw table directly. The real solution is prevention through constraints and idempotent pipeline design."
---

# Hook

An e-commerce business sends promotional emails to its customer list. One Monday, 50,000 customers receive the same email twice within minutes of each other. Complaints flood the support inbox. The cause: a mailing list import ran twice over the weekend, and the customer database had no UNIQUE constraint on email addresses. Every customer was now stored twice.

Duplicate data is everywhere in real systems — and it is one of the hardest quality problems to spot because duplicates look like legitimate data. They do not trigger errors. They do not cause immediate crashes. They silently inflate counts, distort averages, and erode trust in your system's numbers.

How do duplicates arise — and how do you prevent and detect them?

# Lore Introduction

Master Selvaris opened the Registry of Citizens to a seemingly normal page. "Count the villagers of Thornhaven," he said. You counted: 47. "Now count again, looking for any name that appears twice." After careful reading: five names appeared twice. "Two registration ceremonies were held in the same month, and no one checked for overlaps," he said. "The census now shows 47 citizens. The true count is 42. Five families have been paying double taxes for three years." He closed the book. "Duplicates are not loud. They are quiet, and patient, and very expensive."

# Core Learning

## Concept Introduction

| Concept | Definition | Example |
|---------|-----------|---------|
| **Exact duplicate** | Row with identical values in every column as another row | Same customer inserted twice from a double-click |
| **Near-duplicate** | Row representing the same entity with minor differences | "J. Smith, 12 Oak St" and "John Smith, 12 Oak Street" |
| **DISTINCT** | SQL keyword filtering duplicate rows from a query result | `SELECT DISTINCT email FROM users` |
| **GROUP BY + HAVING** | Pattern for finding duplicate values | `HAVING COUNT(*) > 1` |
| **Idempotent pipeline** | A pipeline that produces the same result even if run multiple times | Using UPSERT (INSERT OR UPDATE) instead of blind INSERT |
| **Deduplication** | Process of identifying and removing duplicate records | ETL step that merges near-duplicates before loading |

## Why It Matters

Duplicates corrupt analytics:
- **COUNT** returns inflated entity counts
- **SUM** double-counts revenue or quantities
- **AVG** is skewed toward duplicated entities
- **Reports** lose credibility when duplicates are discovered by stakeholders

## Worked Examples

**Example 1: Finding exact duplicates**
```sql
SELECT email, COUNT(*) as occurrences
FROM customers
GROUP BY email
HAVING COUNT(*) > 1;
-- Returns emails that appear more than once
```

**Example 2: Preventing duplicates with constraints**
```sql
CREATE TABLE customers (
    id    SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name  VARCHAR(100) NOT NULL
);
-- Attempting to insert a duplicate email raises a constraint violation
```

**Example 3: Idempotent insert with UPSERT**
```sql
INSERT INTO customers (email, name)
VALUES ('alice@example.com', 'Alice')
ON CONFLICT (email) DO UPDATE SET name = EXCLUDED.name;
-- Safe to run multiple times — updates if exists, inserts if not
```

## Common Mistakes

- **Using DISTINCT as a workaround**: Adding DISTINCT to every query hides duplicates rather than removing them. The underlying data is still corrupt.
- **Ignoring near-duplicates**: Exact duplicate detection is easy; near-duplicate detection (slightly different names, addresses, or IDs) requires more sophisticated matching and is where most real deduplication effort goes.
- **Deduplicating at query time only**: If duplicates exist in the database, every developer querying it must know to handle them. Clean the source data instead.

## Mental Model

Imagine a library catalogue where every book can be added by any librarian without checking whether it already exists. Over time, the same book appears under five slightly different titles — some with subtitles, some with abbreviated author names. The library "has" each book once by count of entries, but visitors find multiple copies on the shelf and cannot tell which is authoritative. Deduplication is the process of auditing the catalogue, identifying duplicates, choosing the canonical record, and removing the rest.

## Mini Summary

- ✔ Duplicates are records that represent the same real-world entity more than once
- ✔ They inflate counts, distort sums and averages, and mislead decision-makers
- ✔ Detection: GROUP BY email HAVING COUNT(*) > 1, or compare on business keys
- ✔ Prevention: PRIMARY KEY, UNIQUE constraints, idempotent pipelines with UPSERT
- ✔ DISTINCT hides duplicates in output; it does not clean the underlying data

# Guided Practice Quest

Work through the guided steps to identify how duplicates arise, how SQL constraints prevent them, and how UPSERT patterns make pipelines safe to re-run.

# Solo Practice Quest

You have been given a CSV of 10,000 customer records that was imported twice by mistake. Write a step-by-step deduplication plan: (1) how you would detect duplicates using SQL; (2) how you would decide which of two duplicate rows to keep (what makes a row "canonical"?); (3) how you would delete the duplicates safely without losing data; (4) what constraint you would add to prevent this happening again. Reflect on how you would handle near-duplicates where the email is the same but the name is slightly different.

# Integration

**Mathematics**: In set theory, a set by definition contains no duplicates — each element appears exactly once. A well-designed database table is meant to behave like a mathematical set: a collection of unique facts about the world. When duplicates exist, the table has become a multiset (or bag), which does not obey the same mathematical properties and breaks the relational model's guarantees.

**Software Engineering**: The principle of idempotency — an operation that produces the same result regardless of how many times it is applied — is fundamental to safe pipeline design. UPSERT (INSERT OR UPDATE) is an idempotent database operation. Designing pipelines around idempotent operations prevents the most common class of duplicate-creating bugs.

# Lore Conclusion

Master Selvaris handed you a small brass seal. "This is the Seal of Provenance. Every scroll entered into the Archive must bear it — dated, signed, and checked against the Registry of Entries." He gestured to the stacks. "Duplicates do not enter a well-kept Archive because we check before we shelve. Build your pipelines the same way: check first, insert second, and make every operation safe to repeat." He smiled. "The Archive does not forgive careless additions. Neither will your stakeholders."

---
