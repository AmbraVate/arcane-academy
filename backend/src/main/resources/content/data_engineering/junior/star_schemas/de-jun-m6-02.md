---
id: de-jun-m6-02
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m6
moduleTitle: "Module 6: Data Warehousing Foundations"
moduleGlyph: "🏛️"
moduleSortOrder: 6
topicSlug: star_schemas
topicTitle: "Star Schemas"
topicSortOrder: 2
lesson: star_schemas
title: "Star Schemas"
sortOrder: 2
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m6-01]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Identifies the components of a star schema (fact table + dimension tables)
    - Explains why star schema reduces JOINs compared to normalised schema
    - Distinguishes star schema from snowflake schema
    - Identifies grain as the most important design decision for a fact table
    - Explains surrogate keys and why they are preferred over natural keys in warehouses
  keywords: [star schema, fact table, dimension table, grain, surrogate key, natural key, snowflake schema, conformed dimension, slowly changing dimension, SCD, denormalised, measure, attribute]
  modelAnswer: |
    A star schema has one central fact table containing measures (numeric, additive values like revenue, quantity) surrounded by dimension tables containing descriptive attributes (who, what, when, where). The schema looks like a star — fact in the centre, dimensions radiating out. Grain: the level of detail of each row in the fact table (e.g. "one row per loan" vs "one row per loan per item"). Setting grain correctly is the most critical design decision — mixing grains causes incorrect aggregations. Star schema vs snowflake: star has denormalised dimensions (all attributes in one dimension table), snowflake normalises dimensions further (region → country → continent as separate tables). Star is simpler to query; snowflake saves storage. Surrogate keys: warehouse-assigned integer keys (sk_member = 1001), independent of the source system's natural key — enables tracking members who change IDs in the source, supports SCD (Slowly Changing Dimensions) where history must be preserved.
guidedSteps:
  - id: de-jun-m6-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A loan fact table has the grain "one row per loan". A data analyst wants to add a column for the number of items in each loan. Is this compatible with the grain?
    inputConfig:
      options:
        - "Yes — number of items per loan is a natural measure at the loan grain"
        - "No — item count requires changing the grain to 'one row per loan per item'"
        - "Yes — any numeric column can be added to a fact table"
        - "No — counts can only be stored in dimension tables"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Yes — number of items per loan is a natural measure at the loan grain"]
      rejectedFeedback: "Number of items per loan is additive at the loan grain — it is a measure that naturally describes one loan. This is compatible with the grain 'one row per loan'. Adding it is straightforward: item_count INTEGER in the fact table, populated at ETL time. Incompatible examples: if you added 'item title' to the loan fact table, you'd need one row per item per loan — changing the grain. If you wanted to aggregate item counts by category across loans, you'd query dim_item, not the fact table. The grain test: 'does this column describe the same unit of work as every other row?' If yes → compatible. If it requires splitting rows → grain change needed."
    hint: "A measure at the grain level describes the entire unit (one loan). Would adding item count require splitting any rows?"
    reflectionPrompt: "If you changed the grain to 'one row per loan per item', how would that affect the SUM(revenue) aggregation?"
  - id: de-jun-m6-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In a star schema, the central table containing numeric measures (revenue, quantity, duration) and foreign keys to dimension tables is called the ________ table.
    inputConfig:
      placeholder: "fact"
    markingRule:
      matchMode: CONTAINS
      accepted: [fact, "fact table", facts]
      rejectedFeedback: "The fact table is the central, usually largest table in a star schema. It contains: (1) Measures — numeric, additive values that can be aggregated: revenue, quantity, loan_days, fine_amount. Measures answer 'how much/how many'. (2) Foreign keys — integer surrogate keys pointing to each dimension table (sk_member, sk_item, sk_date). (3) Sometimes degenerate dimensions — dimensional attributes that have no dimension table (e.g. loan_number as a reference identifier). The fact table is tall (millions to billions of rows) and narrow (fewer columns than the full normalised source, mostly FKs and measures). Most warehouse queries start from the fact table and join to dimensions for filtering and grouping."
    hint: "This table contains the measurements and the foreign keys to all the 'who/what/when/where' tables."
    reflectionPrompt: "Why is the fact table typically much larger than the dimension tables?"
  - id: de-jun-m6-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain what a surrogate key is and why data warehouses use them instead of natural keys from the source system.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [surrogate, integer, sequential, natural, change, source, SCD, history, independent, system, warehouse]
      rejectedFeedback: "A surrogate key is a warehouse-generated integer (typically a sequence: 1, 2, 3, ...) assigned to each dimension record as its primary key. The natural key is the identifier from the source system (member_id = 'M-20240512-A7', email, employee_number). Why use surrogate keys in the warehouse: (1) Natural keys change — members may get new IDs when systems are upgraded. The surrogate key remains stable; the new natural key is just an attribute. (2) SCD (Slowly Changing Dimensions) — when a member changes tier, a new dimension row is inserted with a new surrogate key but the same natural key. Historical fact rows point to the old surrogate (old tier); new facts point to the new surrogate (new tier). (3) Joins are faster on integers than on strings. (4) Source system independence — the warehouse controls its own key space, not dependent on source system behaviour."
    hint: "What happens when a natural key changes in the source system? What would break in the warehouse if you used it directly?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A snowflake schema differs from a star schema in that:"
    options:
      - "A snowflake schema has no fact table — it only contains dimension tables"
      - "In a snowflake schema, dimension tables are further normalised into sub-dimension tables (e.g. product → category → department)"
      - "A snowflake schema stores fact data in multiple tables partitioned by time"
      - "A snowflake schema is used for OLTP; star schema is used for OLAP"
    correctIndex: 1
    feedback: "A snowflake schema normalises the dimension tables: instead of dim_product containing category_name and department_name as attributes, it has dim_product → dim_category → dim_department. This reduces dimension table storage (no repeated category names in dim_product) and enforces referential integrity within dimensions. Trade-off: queries now need an extra JOIN per normalised level — SELECT category_name requires joining fact → dim_product → dim_category. Star schema keeps dim_product denormalised (category_name is a column in dim_product) — one fewer join per query. In practice, star schemas are preferred for query simplicity and performance; snowflake schemas are used when dimension table size and data quality enforcement are priorities."
  - type: MULTIPLE_CHOICE
    question: "The 'grain' of a fact table is:"
    options:
      - "The size of each row in bytes"
      - "The number of distinct values in the primary key column"
      - "The precise level of detail represented by each row — what one row in the fact table describes"
      - "The time interval between ETL loads"
    correctIndex: 2
    feedback: "Grain is the most fundamental decision in data warehouse design. It defines: 'what does one row in this fact table represent?' Examples: 'one row per individual loan' (loan grain), 'one row per day per item' (daily inventory grain), 'one row per transaction line item' (line item grain). The grain must be declared explicitly before any other design decisions. Consequences of mixing grains: if some rows represent individual loans and others represent loan-day combinations, SUM(revenue) produces incorrect results depending on which rows are selected. Rule: all rows in a fact table must have the same grain. Changing the grain requires a new fact table or a complete redesign."
retrieval:
  recall: "Draw (describe in text) the star schema for an Archive system loan fact table. List all dimension tables, their key attributes, and the foreign keys in the fact table."
  explain: "Explain the trade-off between star schema and snowflake schema for query performance and storage. Give an example where you would choose each."
  mistakeId:
    code: |
      CREATE TABLE loan_facts (
          loan_id        BIGINT PRIMARY KEY,     -- natural key from source
          member_id      BIGINT,                  -- natural FK to source
          item_id        BIGINT,                  -- natural FK to source
          loan_date      DATE,
          due_date       DATE,
          return_date    DATE,
          member_name    VARCHAR(100),            -- copied attribute
          item_title     VARCHAR(200),            -- copied attribute
          item_category  VARCHAR(50),             -- copied attribute
          loan_days      INT,
          is_overdue     BOOLEAN
      );
    answer: "Several design issues: (1) Using natural keys (loan_id, member_id, item_id from source system) instead of surrogate keys — breaks when source IDs change, prevents SCD history tracking. (2) member_name, item_title, item_category are dimensional attributes stored in the fact table — this is partial denormalisation without the structure. These should be in dim_member and dim_item dimension tables, with surrogate key FKs in the fact table. (3) loan_date, due_date, return_date should be FK references to a dim_date dimension table (sk_loan_date, sk_due_date, sk_return_date) — enables filtering and grouping by day, week, month, quarter, year without date calculations in every query. (4) is_overdue is a derived attribute — calculate it in the query (return_date IS NULL AND due_date < CURRENT_DATE) rather than storing a potentially stale boolean. Fixed design: loan_facts(sk_loan, sk_member, sk_item, sk_loan_date, sk_due_date, sk_return_date, loan_days, fine_amount) with proper surrogate keys to dim_member, dim_item, dim_date."
---

# Hook

The star schema is the blueprint of data warehousing — a design so widely adopted that understanding it is a baseline expectation for any data engineer. It solves the analytical query problem elegantly: one central table of measurements, surrounded by descriptive tables that answer who, what, when, and where. Simple to query, fast to scan, and designed from the ground up for aggregation at scale.

# Lore Introduction

"The analytical database is up," the Junior Engineer reported. "But I've just copied the operational schema over. The reports are still slow — five joins per query." The Senior Archivist set down her notes. "The operational schema is designed for one thing: correct storage of individual transactions. You've brought the problem with you." She pulled up a blank diagram. "The analytical schema has a different goal: fast aggregation. We design it from the queries out." She drew a centre circle. "The loan fact. What did we measure?" The Junior thought. "Duration. Whether it was returned. Whether it was overdue." The Senior Archivist added lines outward. "And who borrowed it. What they borrowed. When. These are the dimensions. The star schema."

# Core Learning

## Concept Introduction

### The Star Schema Structure

```
                        dim_date
                        ┌─────────────────┐
                        │ sk_date (PK)     │
                        │ calendar_date    │
                        │ day_of_week      │
                        │ week_number      │
                        │ month_name       │
                        │ quarter          │
                        │ year             │
                        │ is_weekend       │
                        └────────┬────────┘
                                 │ sk_loan_date FK
    dim_member                   │
    ┌─────────────────┐  loan_facts              dim_item
    │ sk_member (PK)  │  ┌────────────────────┐  ┌─────────────────┐
    │ member_key      │  │ sk_loan (PK)        │  │ sk_item (PK)    │
    │ full_name       ├──┤ sk_member (FK)      ├──┤ item_key        │
    │ email           │  │ sk_item (FK)        │  │ title           │
    │ membership_tier │  │ sk_loan_date (FK)   │  │ author          │
    │ join_date       │  │ sk_due_date (FK)    │  │ isbn            │
    │ city            │  │ sk_return_date (FK) │  │ category        │
    │ country         │  │ loan_days           │  │ format          │
    └─────────────────┘  │ fine_amount         │  │ publish_year    │
                         │ was_overdue         │  └─────────────────┘
                         └────────────────────┘
                              │
                         sk_return_date FK ───→ dim_date (same table,
                                               different role alias)
```

### Setting the Grain

```sql
-- GRAIN DECLARATION: "One row in loan_facts represents one completed or active loan"
-- This means:
--   loan_days = total days of this individual loan (not per item, not per day)
--   fine_amount = total fine for this loan
--   ONE row per loan_id

-- Grain test: can every column describe one loan?
--   loan_days → YES (duration of this specific loan)
--   fine_amount → YES (total fine accrued on this loan)
--   item_title → NO (belongs in dim_item — describes the item, not the loan)
--   member_name → NO (belongs in dim_member — describes the member, not the loan)

-- WRONG grain: mixing "one row per loan" with "one row per loan per renewal"
-- Result: loan_id appears multiple times → SUM(fine_amount) double-counts fines
```

### Creating the Dimension Tables

```sql
-- Surrogate key as PK (warehouse-assigned sequence, not source system ID)
CREATE TABLE dim_member (
    sk_member       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    member_key      VARCHAR(50) NOT NULL,     -- natural key from source (stable reference)
    full_name       VARCHAR(200) NOT NULL,
    email           VARCHAR(200),
    membership_tier VARCHAR(50),
    join_date       DATE,
    city            VARCHAR(100),
    country         VARCHAR(100),
    -- SCD Type 2 columns:
    valid_from      DATE NOT NULL,
    valid_to        DATE,                     -- NULL = current record
    is_current      BOOLEAN DEFAULT TRUE
);

-- Date dimension: pre-populated for all dates in range
CREATE TABLE dim_date (
    sk_date         INT PRIMARY KEY,          -- e.g. 20240115 = Jan 15 2024
    calendar_date   DATE NOT NULL UNIQUE,
    day_name        VARCHAR(10),              -- 'Monday'
    day_of_month    INT,
    day_of_week     INT,                      -- 1=Mon, 7=Sun
    week_number     INT,
    month_number    INT,
    month_name      VARCHAR(10),              -- 'January'
    quarter         INT,
    year            INT,
    is_weekend      BOOLEAN,
    is_public_holiday BOOLEAN
);
-- Populated once: INSERT for every date from 2010-01-01 to 2035-12-31
```

### Creating the Fact Table

```sql
CREATE TABLE loan_facts (
    sk_loan         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    -- Dimension foreign keys (surrogate keys):
    sk_member       BIGINT NOT NULL REFERENCES dim_member(sk_member),
    sk_item         BIGINT NOT NULL REFERENCES dim_item(sk_item),
    sk_loan_date    INT NOT NULL REFERENCES dim_date(sk_date),
    sk_due_date     INT NOT NULL REFERENCES dim_date(sk_date),
    sk_return_date  INT REFERENCES dim_date(sk_date),    -- NULL if not yet returned
    -- Measures (numeric, additive):
    loan_days       INT,                  -- actual days borrowed (NULL if ongoing)
    overdue_days    INT DEFAULT 0,        -- days past due date
    fine_amount     DECIMAL(10,2) DEFAULT 0.00,
    -- Degenerate dimension (reference, no dimension table):
    loan_reference  VARCHAR(50)           -- human-readable loan ID for lookup
);
```

### Querying the Star Schema

```sql
-- Monthly loans by membership tier:
SELECT d.month_name, d.year,
       m.membership_tier,
       COUNT(*) AS loan_count,
       AVG(f.loan_days) AS avg_loan_days,
       SUM(f.fine_amount) AS total_fines
FROM loan_facts f
JOIN dim_member m ON f.sk_member = m.sk_member
JOIN dim_date d ON f.sk_loan_date = d.sk_date
WHERE d.year = 2024 AND m.is_current = TRUE
GROUP BY d.year, d.month_name, d.month_number, m.membership_tier
ORDER BY d.month_number, m.membership_tier;

-- No JOIN to items needed — items are irrelevant for this query
-- Only joins the dimensions needed for filtering and grouping
-- Simple, readable, fast on columnar storage
```

### Slowly Changing Dimensions (SCD)

```sql
-- SCD Type 1: overwrite (no history preserved)
UPDATE dim_member SET email = 'new@email.com' WHERE member_key = 'M-001' AND is_current = TRUE;

-- SCD Type 2: insert new row, close old row (history preserved)
-- Member upgrades from 'Standard' to 'Premium':
UPDATE dim_member SET valid_to = CURRENT_DATE, is_current = FALSE
WHERE member_key = 'M-001' AND is_current = TRUE;

INSERT INTO dim_member (member_key, full_name, email, membership_tier,
                         join_date, city, country, valid_from, valid_to, is_current)
VALUES ('M-001', 'Alice Selvaris', 'alice@example.com', 'Premium',
        '2020-03-15', 'London', 'UK', CURRENT_DATE, NULL, TRUE);

-- Historical fact rows still point to the old sk_member (Standard tier)
-- New fact rows point to the new sk_member (Premium tier)
-- Query: "how many loans did Alice make as a Standard member?" → filter old sk
```

## Common Mistakes

- **Storing dimension attributes in the fact table**: putting `member_name` or `category` in the fact table instead of dimension tables creates an "all-in-one" table that loses the structural benefits of star schema — harder to maintain, harder to query by dimension attribute, no reuse across fact tables.
- **Using natural keys as foreign keys**: when source system IDs change, all fact rows pointing to that ID break. Surrogate keys decouple the warehouse from source system changes.
- **No date dimension**: storing `loan_date DATE` in the fact table instead of `sk_loan_date INT` makes every date-based grouping (by month, quarter, year, day-of-week) require a `DATE_TRUNC` or `EXTRACT` function — not indexable, not reusable. A pre-populated date dimension makes these attributes instant lookups.
- **Mixing grain in one fact table**: adding a summary row (annual loan count for the member) alongside individual loan rows in the same fact table corrupts all aggregations.

## Mental Model

A star schema is like an invoice. The invoice body (fact table) records the transaction: item purchased, date, quantity, price. The edges reference master data: customer file (dim_member), product catalogue (dim_item), calendar (dim_date). The invoice itself only records what changed (the fact); the stable descriptive information lives in the reference files (dimensions). The "star" shape is literal — draw the fact table in the centre, draw lines to each dimension table, and the diagram looks like a star.

## Mini Summary

- ✔ Star schema: central fact table + surrounding dimension tables (who/what/when/where)
- ✔ Grain: what one row in the fact table represents — must be declared and consistent
- ✔ Measures: numeric, additive values in the fact table (revenue, quantity, duration)
- ✔ Dimensions: descriptive attributes for filtering and grouping (name, category, date parts)
- ✔ Surrogate keys: warehouse-generated integers, independent of source system natural keys
- ✔ SCD Type 2: new dimension row per change — preserves history for point-in-time analysis
- ✔ Snowflake schema: further normalises dimensions — fewer dimension table duplicates, more joins

# Guided Practice Quest

Work through the guided steps to identify the grain of a provided Archive loan dataset, design the dimension tables (member, item, date), and write the star schema CREATE TABLE statements with correct surrogate key relationships.

# Solo Practice Quest

Design a complete star schema for the Archive reporting system. Tasks: (1) Declare the grain for your primary fact table — write it as a statement ("one row in loan_facts represents..."); (2) Design dim_member, dim_item, dim_date, and dim_category tables — list all columns and mark which are surrogate keys, natural keys, measures, and attributes; (3) Design the loan_facts table — all dimension FKs, all measures, any degenerate dimensions; (4) Write a query on your star schema for the most complex report: "top 10 most borrowed item categories per quarter for the last two years, with average loan duration and total fines collected"; (5) Apply SCD Type 2 to dim_member for the scenario where a member changes their membership tier — write the UPDATE and INSERT statements; (6) Explain why you chose your grain and what analytical questions it can and cannot answer.

# Integration

**Mathematics**: Star schema design has direct connections to relational algebra and set theory. The grain declaration defines the primary key — each row is a unique element in the fact table's set. Measures are additive functions over the set: SUM(revenue) is a fold over the revenue values of a filtered subset. The associativity of addition enables GROUP BY to produce correct subtotals: SUM for (year=2024, region='North') = SUM for (year=2024, region='North', tier='Premium') + SUM for (year=2024, region='North', tier='Standard'). This additive property is why measures must be numeric and why averages (non-additive) require special handling: AVG(loan_days) across regions ≠ average of regional averages (Simpson's paradox). Dimensional modelling recognises three measure types: fully additive (SUM works across all dimensions), semi-additive (SUM works across some dimensions — e.g. account balance can be summed across accounts but not across time), and non-additive (must use MIN/MAX/AVG — e.g. ratios, percentages).

**Sciences (Cartography — Map Projections)**: The star schema concept parallels cartographic projection theory. A geographic map is a 2D representation of a 3D spherical world — a simplification that enables navigation, measurement, and reasoning. Different projections optimise for different properties: Mercator preserves angles (for navigation), equal-area projections preserve size relationships (for statistics). No projection is perfect — all involve trade-offs. The star schema is a projection of operational reality onto an analytical space: normalised transactional data projected into denormalised fact+dimension form. The projection preserves what matters for analysis (measures, dimensional slicing) while sacrificing what OLTP requires (update efficiency, constraint enforcement). Choosing the wrong projection for your use case — using a transactional schema for analytics, or a star schema for transaction processing — produces distorted results just as using Mercator for equal-area analysis produces misleading maps.

# Lore Conclusion

"The star schema is deployed," the Junior Engineer reported. "dim_member, dim_item, dim_date, and loan_facts. ETL runs overnight." The Senior Archivist reviewed the query output. "Monthly report: 0.3 seconds. The same query was two minutes on the operational database." The Junior nodded. "Because now it's one fact table scan and three small dimension lookups. No normalised joins." The Senior Archivist pointed to dim_date. "The date dimension was the key. Month, quarter, year — all pre-calculated columns. No `DATE_TRUNC`, no `EXTRACT`. Just `WHERE d.year = 2024`." She looked at the schema. "You've built the structure. Next: the fact table itself — what goes in it, how measures are defined, and the different types of facts a warehouse needs."

---
