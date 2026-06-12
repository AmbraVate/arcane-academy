---
id: de-jun-m6-04
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m6
moduleTitle: "Module 6: Data Warehousing Foundations"
moduleGlyph: "🏛️"
moduleSortOrder: 6
topicSlug: dimension_tables
topicTitle: "Dimension Tables"
topicSortOrder: 4
lesson: dimension_tables
title: "Dimension Tables"
sortOrder: 4
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m6-03]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what a dimension table contains and its role in a star schema
    - Describes SCD Type 1, Type 2, and Type 3 and when to use each
    - Explains the date dimension and why it is pre-populated
    - Describes conformed dimensions and why they enable cross-fact analysis
    - Identifies junk dimensions and when they are appropriate
  keywords: [dimension, SCD, slowly changing dimension, Type 1, Type 2, Type 3, date dimension, conformed dimension, junk dimension, role-playing dimension, hierarchy, attribute, denormalised, is_current, valid_from, valid_to]
  modelAnswer: |
    Dimension tables contain descriptive attributes for filtering, grouping, and labelling facts. They are wide (many columns), relatively small (compared to fact tables), and denormalised. SCD Type 1: overwrite — no history, simplest, use when history is irrelevant (typo correction). SCD Type 2: insert new row, close old row (valid_to, is_current) — full history, use when changes affect historical analysis (member tier changes, product category changes). SCD Type 3: add a new column for the previous value — limited history, use for "before/after" comparisons with only one prior state. Date dimension: pre-populated with every date in a range — pre-calculates day_of_week, month_name, quarter, is_holiday so queries filter by d.quarter = 2 instead of EXTRACT(QUARTER FROM date). Conformed dimensions: shared across multiple fact tables — dim_member used in loan_facts AND reservation_facts means you can join both fact tables through dim_member to answer cross-domain questions.
guidedSteps:
  - id: de-jun-m6-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A member changes their city from 'Manchester' to 'London'. Historical loan facts should still show Manchester for loans made before the move. Which SCD type handles this correctly?
    inputConfig:
      options:
        - "SCD Type 1 — update the city column to London; history is not needed"
        - "SCD Type 2 — insert a new dimension row for London, close the Manchester row; historical fact FKs still point to Manchester row"
        - "SCD Type 3 — add a previous_city column set to Manchester, update city to London"
        - "SCD Type 0 — never update dimension attributes once set"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SCD Type 2 — insert a new dimension row for London, close the Manchester row; historical fact FKs still point to Manchester row"]
      rejectedFeedback: "SCD Type 2 is the correct answer for preserving history. Mechanism: (1) Find the current member row (WHERE member_key = 'M-001' AND is_current = TRUE). (2) UPDATE: set valid_to = CURRENT_DATE - 1, is_current = FALSE. (3) INSERT: new row with city = 'London', valid_from = CURRENT_DATE, valid_to = NULL, is_current = TRUE, new sk_member value. Historical fact rows point to the old sk_member (Manchester row) — those loans will always show Manchester. New fact rows point to the new sk_member (London row) — future loans show London. SCD Type 1 (overwrite) would retroactively change all historical loans to London — losing the historical geography. SCD Type 3 would preserve only one prior value — fine for 'before/after' but doesn't support arbitrary point-in-time queries."
    hint: "Which SCD type creates a new row with a new surrogate key rather than modifying the existing row?"
    reflectionPrompt: "What would a query look like to find all loans made while a member lived in Manchester, using SCD Type 2?"
  - id: de-jun-m6-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A dimension table shared across multiple fact tables — enabling queries that join across those facts — is called a ________ dimension.
    inputConfig:
      placeholder: "conformed"
    markingRule:
      matchMode: CONTAINS
      accepted: [conformed, "conformed dimension", "shared dimension", "enterprise dimension"]
      rejectedFeedback: "A conformed dimension is a dimension table used in exactly the same way across multiple fact tables and even multiple data marts. Example: dim_member is used in loan_facts (to describe who borrowed items) and also in reservation_facts (to describe who reserved items) and fine_payment_facts (to describe who paid fines). Because the same dim_member is used, you can write cross-fact queries: 'For each member tier, what is the average number of loans per month AND the average outstanding fine?' — joining loan_facts and fine_payment_facts through the conformed dim_member. Without conformed dimensions, each fact table has its own member data that may differ — making cross-fact analysis incorrect or impossible. The date dimension (dim_date) is almost always conformed — the same table across all fact tables in the warehouse."
    hint: "This type of dimension can be reused across different fact tables to enable cross-domain analysis."
    reflectionPrompt: "What problems arise if two fact tables define 'member' differently — one includes inactive members, the other doesn't?"
  - id: de-jun-m6-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      What is a junk dimension, and when is it a better design choice than adding low-cardinality flags directly to the fact table?
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [junk, flag, boolean, low cardinality, combine, single, dimension, reduce, columns, fact table, narrow]
      rejectedFeedback: "A junk dimension combines several low-cardinality flags and indicators into a single dimension table rather than storing them as individual columns in the fact table. Example: a loan fact might have flags: is_renewal (Y/N), is_premium_member (Y/N), is_online_request (Y/N), is_first_loan (Y/N). 4 boolean columns = 2^4 = 16 possible combinations. Create dim_loan_type with 16 rows (one per combination) and a surrogate key sk_loan_type. The fact table stores only one FK (sk_loan_type) instead of 4 boolean columns. Benefits: (1) Narrows the fact table (important when you have billions of rows — 4 bytes FK vs 4 bytes × 4 columns). (2) Groups semantically related flags. (3) Makes flag combinations queryable as a dimension (GROUP BY loan_type). When to use: when you have 3+ low-cardinality flag columns in the fact table and the total combinations are manageable (< 100 distinct values)."
    hint: "Think about several boolean flags (yes/no columns) that belong together. What happens if you put them all in one small table?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The date dimension table is pre-populated with all dates in a range because:"
    options:
      - "The database requires it for referential integrity with date FKs in fact tables"
      - "Pre-calculating attributes (month_name, quarter, is_holiday, day_of_week) means queries filter and group without expensive date functions"
      - "It prevents NULL dates in fact tables by providing a 'unknown date' surrogate"
      - "Date calculations are not supported in SQL, so they must be pre-computed"
    correctIndex: 1
    feedback: "The date dimension pre-materialises all date attributes: month_name = 'March', quarter = 1, year = 2024, is_weekend = FALSE, week_number = 12. Without it: every query that groups by quarter runs EXTRACT(QUARTER FROM date) on billions of fact rows — a function that cannot use an index and executes per-row. With the date dimension: WHERE d.quarter = 1 AND d.year = 2024 joins to a tiny dimension table via an indexed integer key. All date attributes are pre-indexed columns in dim_date. This is why data engineers pre-populate dim_date from 2000-01-01 to 2035-12-31 (about 12,000 rows) before loading any facts. Additional benefits: is_holiday and is_weekend are custom attributes that EXTRACT cannot provide — they require external data (public holiday calendars) incorporated at load time."
  - type: MULTIPLE_CHOICE
    question: "A dimension that is used twice in the same fact table — for example, dim_date referenced as both loan_date and return_date — is called a:"
    options:
      - "Conformed dimension"
      - "Junk dimension"
      - "Role-playing dimension"
      - "Slowly changing dimension"
    correctIndex: 2
    feedback: "A role-playing dimension is a single physical dimension table used multiple times in the same fact table, each time playing a different role. dim_date is the most common example: loan_facts has sk_loan_date (the date the loan started) and sk_due_date (the target return date) and sk_return_date (actual return date) — all three are foreign keys to dim_date, but they represent different events. In SQL, you alias the table per role: FROM loan_facts f JOIN dim_date dl ON f.sk_loan_date = dl.sk_date JOIN dim_date dd ON f.sk_due_date = dd.sk_date JOIN dim_date dr ON f.sk_return_date = dr.sk_date WHERE dl.year = 2024 AND dd.month_number = 3. Some BI tools require views (CREATE VIEW dim_loan_date AS SELECT * FROM dim_date) to handle role-playing dimensions without confusion."
retrieval:
  recall: "Describe the steps to implement SCD Type 2 in an ETL process: how to detect that a member's tier has changed, what SQL runs to close the old row, and what SQL runs to insert the new row."
  explain: "Explain why a pre-populated date dimension is superior to storing raw DATE values in fact tables for analytical queries. Give two specific analytical queries that are simpler and faster with a date dimension."
  mistakeId:
    code: |
      -- SCD Type 2 implementation
      INSERT INTO dim_member (member_key, full_name, membership_tier, valid_from, is_current)
      SELECT m.id, m.name, m.tier, CURRENT_DATE, TRUE
      FROM source_members m
      WHERE m.tier != (
          SELECT dm.membership_tier FROM dim_member dm
          WHERE dm.member_key = m.id AND dm.is_current = TRUE
      );
      -- Missing: close the old row
    answer: "The INSERT correctly creates new rows for members whose tier has changed. But the old rows are never closed — they remain with is_current = TRUE. Result: multiple is_current = TRUE rows per member, breaking the 'one current row per member' invariant. Queries using WHERE is_current = TRUE will return multiple rows for changed members, causing fan-out in joins and double-counted measures. Fix: add an UPDATE step BEFORE the INSERT: UPDATE dim_member SET valid_to = CURRENT_DATE - 1, is_current = FALSE WHERE member_key IN (SELECT id FROM source_members WHERE tier != (subquery)) AND is_current = TRUE. Order matters: UPDATE (close old) must happen before INSERT (open new). Also missing: the valid_to = NULL on the new row (should be explicitly set or defaulted) and the valid_from = CURRENT_DATE should use a consistent ETL run timestamp, not CURRENT_DATE (which can change mid-ETL if run at midnight)."
---

# Hook

Dimension tables are the context that makes facts meaningful. A fact row says "36, 5.00, 20240115" — meaningless without knowing who, what, and when. Dimension tables answer those questions. But dimensions are not static — members change addresses, items change categories, and the warehouse must track those changes without corrupting historical analysis. This is the craft of dimension management.

# Lore Introduction

"The geographic report changed," the Junior Engineer reported. "Alice Selvaris now shows all her loans under London — but she was in Manchester for the first two years. The historical data is wrong." The Senior Archivist looked at the ETL log. "You used SCD Type 1. You overwrote the city." The Junior nodded. "I didn't realise it mattered." The Senior Archivist pulled up the dim_member table. "For geography analysis, the location at the time of the loan matters — not where the member lives today. This is why Type 2 exists." She outlined the fix. "New row. Old row closed. Historical fact FKs preserved." The Junior looked concerned. "How do I re-process the historical data?" The Senior Archivist paused. "You cannot, completely. The overwrite lost the original city. This is why you choose SCD type before you run ETL, not after."

# Core Learning

## Concept Introduction

### Dimension Table Characteristics

```sql
-- Dimension tables are WIDE and SHORT (compared to fact tables)
-- Few rows: dim_member might have 50,000 rows
-- Many columns: descriptive attributes for every way you'd want to slice data

CREATE TABLE dim_member (
    sk_member           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    -- Natural key (source system reference):
    member_key          VARCHAR(50)   NOT NULL,
    -- Descriptive attributes (denormalised — no sub-dimension tables in star):
    full_name           VARCHAR(200)  NOT NULL,
    first_name          VARCHAR(100),
    last_name           VARCHAR(100),
    email               VARCHAR(200),
    phone               VARCHAR(30),
    -- Geography (denormalised — not normalised to dim_city, dim_country):
    city                VARCHAR(100),
    region              VARCHAR(100),
    country             VARCHAR(100),
    postal_code         VARCHAR(20),
    -- Classification:
    membership_tier     VARCHAR(50),
    membership_status   VARCHAR(20),
    -- Dates:
    join_date           DATE,
    birth_decade        VARCHAR(10),  -- '1990s' — coarsened for privacy
    -- SCD Type 2 tracking:
    valid_from          DATE          NOT NULL,
    valid_to            DATE,                    -- NULL = current record
    is_current          BOOLEAN       DEFAULT TRUE,
    -- ETL audit:
    etl_loaded_at       TIMESTAMP     DEFAULT NOW()
);
```

### Slowly Changing Dimensions

```sql
-- SCD TYPE 1: OVERWRITE — no history (simplest)
-- Use for: typo corrections, data quality fixes where history is irrelevant
UPDATE dim_member
SET email = 'corrected@example.com'
WHERE member_key = 'M-001' AND is_current = TRUE;
-- All historical fact rows now effectively show corrected email ← intentional for typos

-- SCD TYPE 2: INSERT NEW ROW — full history (most common)
-- Use for: attributes that affect historical analysis (tier, location, category)
-- Step 1: Close the old row
UPDATE dim_member
SET valid_to = CURRENT_DATE - 1,
    is_current = FALSE
WHERE member_key = 'M-001' AND is_current = TRUE;

-- Step 2: Insert the new row (new sk_member generated)
INSERT INTO dim_member (member_key, full_name, email, membership_tier,
                         city, country, valid_from, valid_to, is_current)
VALUES ('M-001', 'Alice Selvaris', 'alice@example.com', 'Premium',
        'London', 'UK', CURRENT_DATE, NULL, TRUE);

-- Result: 2 rows for M-001
-- Old: sk=42, tier='Standard', city='Manchester', valid_to=yesterday, is_current=FALSE
-- New: sk=99, tier='Premium', city='London', valid_from=today, is_current=TRUE
-- Historical loans → sk=42 (Standard, Manchester preserved)
-- New loans → sk=99 (Premium, London)

-- SCD TYPE 3: ADD COLUMN — one prior value only
-- Use for: "before/after" comparisons, single version of history
ALTER TABLE dim_member ADD COLUMN previous_membership_tier VARCHAR(50);
UPDATE dim_member
SET previous_membership_tier = membership_tier,
    membership_tier = 'Premium'
WHERE member_key = 'M-001' AND is_current = TRUE;
-- Query: "show loans made when member was at their previous tier"
-- WHERE dm.previous_membership_tier = 'Standard'
-- Limitation: only one prior value; no point-in-time history
```

### The Date Dimension

```sql
-- Pre-populated: generate all dates from 2010 to 2035
INSERT INTO dim_date (sk_date, calendar_date, day_name, day_of_month,
                       day_of_week, week_number, month_number, month_name,
                       quarter, year, is_weekend, is_public_holiday)
SELECT
    TO_CHAR(d, 'YYYYMMDD')::INT AS sk_date,
    d AS calendar_date,
    TO_CHAR(d, 'Day') AS day_name,
    EXTRACT(DAY FROM d)::INT AS day_of_month,
    EXTRACT(ISODOW FROM d)::INT AS day_of_week,
    EXTRACT(WEEK FROM d)::INT AS week_number,
    EXTRACT(MONTH FROM d)::INT AS month_number,
    TO_CHAR(d, 'Month') AS month_name,
    EXTRACT(QUARTER FROM d)::INT AS quarter,
    EXTRACT(YEAR FROM d)::INT AS year,
    EXTRACT(ISODOW FROM d) IN (6, 7) AS is_weekend,
    FALSE AS is_public_holiday   -- update separately from holiday calendar
FROM generate_series('2010-01-01'::DATE, '2035-12-31'::DATE, '1 day'::INTERVAL) d;

-- Using the date dimension:
-- GROUP BY quarter WITHOUT date functions:
SELECT d.year, d.quarter, m.membership_tier, COUNT(*) AS loans
FROM loan_facts f
JOIN dim_date d ON f.sk_loan_date = d.sk_date
JOIN dim_member m ON f.sk_member = m.sk_member AND m.is_current = TRUE
WHERE d.year BETWEEN 2022 AND 2024
GROUP BY d.year, d.quarter, m.membership_tier
ORDER BY d.year, d.quarter;
```

### Conformed and Junk Dimensions

```sql
-- CONFORMED DIMENSION: same dim_member used across multiple fact tables
SELECT m.membership_tier,
       COUNT(DISTINCT lf.sk_loan) AS total_loans,
       COUNT(DISTINCT rf.sk_reservation) AS total_reservations,
       AVG(fp.fine_amount) AS avg_fine
FROM dim_member m
LEFT JOIN loan_facts lf ON m.sk_member = lf.sk_member
LEFT JOIN reservation_facts rf ON m.sk_member = rf.sk_member
LEFT JOIN fine_payment_facts fp ON m.sk_member = fp.sk_member
WHERE m.is_current = TRUE
GROUP BY m.membership_tier;
-- dim_member conformed → same grain, same definition, usable across all facts

-- JUNK DIMENSION: combine low-cardinality flags
-- Instead of 4 boolean columns in fact table:
CREATE TABLE dim_loan_type (
    sk_loan_type    INT PRIMARY KEY,
    is_renewal      BOOLEAN,
    is_online       BOOLEAN,
    is_first_loan   BOOLEAN,
    is_extension    BOOLEAN
);
-- 16 rows (2^4 combinations) — pre-populate all:
INSERT INTO dim_loan_type VALUES
    (1, FALSE, FALSE, FALSE, FALSE),
    (2, TRUE,  FALSE, FALSE, FALSE),
    (3, FALSE, TRUE,  FALSE, FALSE),
    -- ... all 16 combinations
    (16, TRUE, TRUE,  TRUE,  TRUE);
-- Fact table: sk_loan_type INT FK → dim_loan_type (1 column instead of 4)
```

## Why It Matters

Dimensions are how analytics speak human — they hold the *who, what, where, when* that turn fact rows into answers:

- "Revenue by region by quarter" is only possible because region and date live in well-built dimension tables
- Slowly changing dimensions answer the awkward question every warehouse eventually faces: when a customer moves city, do old sales stay with the old city or move to the new one?
- Bad dimension keys (reusing natural keys, skipping surrogate keys) cause silent fan-out joins that quietly corrupt every report

Facts are voluminous but dumb; dimensions are small but smart. Getting them right is most of warehouse design.

## Common Mistakes

- **SCD Type 1 when Type 2 is needed**: overwriting tier or location retroactively changes historical reports. Decide SCD strategy per attribute before ETL runs — it cannot be easily fixed after the fact without re-sourcing original data.
- **Normalising dimension tables into snowflake**: separating dim_member_city, dim_member_country as sub-tables adds JOINs for no practical benefit in a star schema. Keep dimensions denormalised.
- **Missing `is_current = TRUE` filter**: queries joining to an SCD Type 2 dimension without filtering `is_current = TRUE` return multiple rows per member (one per historical version), causing fan-out and double-counted measures. Always filter current rows unless doing historical point-in-time analysis.
- **Not pre-populating the date dimension**: storing raw DATE values in fact tables forces every aggregation query to run `DATE_TRUNC`, `EXTRACT`, or `TO_CHAR` per row. Pre-populating dim_date is a one-time five-minute task that saves every analyst every day.

## Mental Model

Dimension tables are the reference books of the warehouse. The fact table is the transaction register — just numbers and codes. The dimension tables translate those codes into meaning: member SK 42 → Alice Selvaris, Premium tier, London. SCD Type 2 is like a reference book that keeps all past editions — you can look up what the entry said on any given date, not just what it says today. A conformed dimension is like a dictionary used consistently across all departments — everyone agrees on the definition of "member" and "date", enabling cross-department comparisons.

## Mini Summary

- ✔ Dimension tables: wide, relatively small, descriptive attributes for filtering/grouping
- ✔ SCD Type 1: overwrite — no history (use for corrections, irrelevant changes)
- ✔ SCD Type 2: new row + close old row — full history (use for analytically significant changes)
- ✔ SCD Type 3: add prior value column — single prior state (use for before/after)
- ✔ Date dimension: pre-populated, avoids per-row date functions, enables custom attributes (holidays)
- ✔ Conformed dimension: shared across fact tables — enables cross-domain analysis
- ✔ Junk dimension: combines low-cardinality flags into one small table
- ✔ Role-playing dimension: one physical table used multiple times (date as loan_date AND due_date)

# Guided Practice Quest

Work through the guided steps to implement SCD Type 2 for a member tier change in dim_member, write the query that correctly counts loans by the tier the member held at the time of the loan (not their current tier), and build the date dimension population script for the Archive.

# Solo Practice Quest

Design and implement the complete dimension layer for the Archive data warehouse. Tasks: (1) Design dim_member with full SCD Type 2 columns and a minimum of 12 descriptive attributes; (2) Design dim_item including category hierarchy (item → category → genre) — decide: star schema denormalisation or snowflake? Justify; (3) Write the full ETL for dim_member SCD Type 2, handling three cases: new members, unchanged members (no action), and changed-tier members (close + insert); (4) Write the dim_date population script generating all dates from 2015 to 2030 with all required attributes; (5) Design a junk dimension for loan flags (is_renewal, is_online, is_extension, is_overdue_at_return) — write the CREATE TABLE and the pre-population INSERT; (6) Write a query that answers "by quarter and membership tier (at time of loan), what was the average overdue rate — only for members who are currently still active?" — this tests conformed dimension use with SCD Type 2 filtering.

# Integration

**Mathematics**: SCD Type 2 implements a temporal database — formally, a bitemporal model with valid time (when the real-world fact was true) and transaction time (when the record was stored). The is_current flag is a convenience for the common query "give me the current state", but the full power of SCD Type 2 is point-in-time queries: "what was member M-001's tier on 2022-06-15?" → WHERE member_key = 'M-001' AND valid_from <= '2022-06-15' AND (valid_to IS NULL OR valid_to >= '2022-06-15'). This is an interval intersection test: the query date must fall within [valid_from, valid_to]. Formally, the dimension row is valid for the interval [valid_from, valid_to] and the query is "does the interval contain the target date?" — a membership test in a set of intervals. The total history for one member is a partition of time into non-overlapping intervals, each with a different state. Well-maintained SCD Type 2 data satisfies: no two active rows per natural key (enforced by is_current), no gaps in the timeline, no overlaps.

**Sciences (Biology — Taxonomy and Classification)**: Dimension table design mirrors biological taxonomy: the Linnaean system of hierarchical classification (Kingdom → Phylum → Class → Order → Family → Genus → Species). A dim_item hierarchy (item → category → genre → format) is a taxonomic tree. The snowflake schema normalises this hierarchy into separate tables (dim_category, dim_genre), just as taxonomic databases normalise each level. The star schema denormalises it (all levels in dim_item), trading normalisation for query simplicity — analogous to a field guide that lists both genus and family on each species page for convenience, rather than requiring the reader to look up a separate family table. Biologists choose the reference format based on use case: taxonomists want the normalised database; field naturalists want the denormalised field guide. Data engineers make the same choice based on query patterns.

# Lore Conclusion

"The geographic report is correct," the Junior Engineer reported. "I re-sourced Alice's original city from the operational database — Manchester for the first two years, London after the move. SCD Type 2 rows created. Historical loan facts point to the Manchester row, new loans to London." The Senior Archivist reviewed the report. "Board report: membership tier at time of loan, grouped by quarter. The historical analysis is clean." She closed the dashboard. "Module 6 complete. You understand the data warehouse: OLTP versus OLAP, the star schema, fact tables and their three types, dimensions and their change management. This is the foundation every senior data engineer builds on." The Junior looked at the completed schema. "What's next?" The Senior Archivist set a folder on the table. "Before you can build systems with this data, you must secure it. Module 7: Data Security. Access control, encryption, auditing, and compliance."

---
