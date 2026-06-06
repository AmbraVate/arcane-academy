---
id: de-jun-m6-03
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m6
moduleTitle: "Module 6: Data Warehousing Foundations"
moduleGlyph: "🏛️"
moduleSortOrder: 6
topicSlug: fact_tables
topicTitle: "Fact Tables"
topicSortOrder: 3
lesson: fact_tables
title: "Fact Tables"
sortOrder: 3
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m6-02]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Distinguishes the three fact table types (transaction, periodic snapshot, accumulating snapshot)
    - Explains additive, semi-additive, and non-additive measures
    - Identifies degenerate dimensions and when they are used
    - Explains factless fact tables and gives an example use case
    - Describes how ETL populates a fact table from normalised source data
  keywords: [transaction fact, periodic snapshot, accumulating snapshot, additive measure, semi-additive, non-additive, degenerate dimension, factless fact, grain, ETL, surrogate key lookup, measure, milestone]
  modelAnswer: |
    Three fact table types: (1) Transaction fact — one row per discrete event (loan created, payment made); most common type; grain is the event. (2) Periodic snapshot — one row per entity per time period (account balance at end of month); captures state at regular intervals. (3) Accumulating snapshot — one row per lifecycle instance (one row per loan, with columns for each milestone: created_date, approved_date, returned_date); columns update as milestones complete. Additive measures (SUM across all dimensions): revenue, quantity, loan_count. Semi-additive (SUM across some dimensions only): bank balance (SUM by account is valid, SUM by time period is not). Non-additive (cannot SUM): ratios, percentages, rankings. Degenerate dimension: dimensional attribute with no dimension table (loan_reference number — used for drill-through but doesn't justify a full dimension table). Factless fact: fact table with no measures, only FK columns — records that an event occurred (student enrolled in course, member attended event).
guidedSteps:
  - id: de-jun-m6-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      The Archive tracks bank account balances for its members' deposit accounts. A periodic snapshot fact table stores the balance at the end of each month. Which aggregation is INCORRECT for this measure?
    inputConfig:
      options:
        - "SELECT MAX(balance) FROM account_snapshot_facts WHERE year = 2024 — the highest balance across all months"
        - "SELECT SUM(balance) FROM account_snapshot_facts WHERE month = 'December' AND year = 2024 — total deposits held"
        - "SELECT SUM(balance) FROM account_snapshot_facts WHERE account_id = 42 — total of all monthly balances for one account"
        - "SELECT AVG(balance) FROM account_snapshot_facts WHERE account_id = 42 AND year = 2024 — average monthly balance for an account"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SELECT SUM(balance) FROM account_snapshot_facts WHERE account_id = 42 — total of all monthly balances for one account"]
      rejectedFeedback: "Balance is semi-additive: it can be summed across accounts (total deposits held across all members at a point in time) but NOT summed across time periods for the same account. SUM(balance) WHERE account_id = 42 adds January balance + February balance + … + December balance = a meaningless number. The balance in December already includes all the January-through-November deposits — summing across months double-counts. Correct operations across time: MAX (highest balance), MIN (lowest balance), AVG (average monthly balance), LAST (balance at period end — requires windowing). This semi-additive property is why snapshot fact tables require careful attention to which aggregations are valid across which dimensions."
    hint: "A balance at month-end already includes all previous months. What happens if you add twelve monthly balances together?"
    reflectionPrompt: "How would you get the 'total deposits held across all members as of December 2024' correctly?"
  - id: de-jun-m6-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A fact table that contains only foreign keys to dimensions and no measures — used to record that an event occurred without any numeric quantity — is called a ________ fact table.
    inputConfig:
      placeholder: "factless"
    markingRule:
      matchMode: CONTAINS
      accepted: [factless, "factless fact", "factless fact table", "coverage table", "event fact"]
      rejectedFeedback: "A factless fact table records the occurrence of an event without any numeric measures. Example: member_attendance_facts(sk_member, sk_event, sk_date) — records that a member attended an event. There is no quantity or amount to record — the fact is simply that it happened. Factless facts answer questions like: 'How many members attended each event?' (COUNT(*) on the fact rows), 'Which members attended no events in Q4?' (outer join from member dimension to factless fact). Another factless use: coverage facts — 'which promotions are eligible for which products?' (sk_promotion, sk_product, sk_date) — not a transaction, just a valid combination. Count the rows to get coverage counts."
    hint: "The 'fact' in this table is simply that an event happened — no number needed."
    reflectionPrompt: "How would you count total unique members who borrowed any item in 2024 using a factless fact table?"
  - id: de-jun-m6-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the difference between a transaction fact table and an accumulating snapshot fact table for a library loan. When would you use each?
    inputConfig:
      minWords: 35
    markingRule:
      matchMode: CONTAINS
      accepted: [transaction, accumulating, milestone, lifecycle, event, update, one row, state, progress, pipeline, funnel, workflow]
      rejectedFeedback: "Transaction fact table: one row per discrete event, never updated after insert. For a loan: one row when loan is created, another row when loan is renewed, another when returned — each event is a separate row. Good for: audit trail, event counting, time-between-events analysis. Accumulating snapshot fact table: one row per loan lifecycle instance, updated as milestones complete. Columns: sk_created_date, sk_due_date, sk_returned_date (NULLable), sk_overdue_notified_date. The row is INSERT'd when the loan starts; the return_date and other columns are UPDATE'd as milestones complete. Good for: pipeline analysis (what % of loans were returned on time?), lifecycle duration (average days from loan to return), funnel analysis (how many loans reach each milestone?). Use transaction when you need every event; use accumulating snapshot when you need the complete lifecycle picture per instance."
    hint: "One type records each event as a new row; the other has one row per item that gets updated over time."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In ETL loading a fact table, 'surrogate key lookup' refers to:"
    options:
      - "Looking up which surrogate key sequence to use next for new rows"
      - "Translating natural keys from the source system to the warehouse surrogate keys for dimension foreign keys"
      - "Verifying that surrogate keys in dimension tables match those in fact tables"
      - "Generating surrogate keys for degenerate dimensions"
    correctIndex: 1
    feedback: "During ETL: the source system provides natural keys (member_id = 'M-20240512-A7', item_id = 'ISBN-978-0134685991'). The fact table needs surrogate keys (sk_member = 1042, sk_item = 2381) matching the current (is_current = TRUE) dimension rows. The surrogate key lookup step: JOIN the source natural key against the dimension table to find its surrogate key. If the dimension row doesn't exist yet (new member), the ETL first inserts it into the dimension table (Type 1: insert new row; Type 2: also check if it's a new version of an existing entity). This lookup step is what separates ETL from a simple database copy — it resolves source identifiers to warehouse identifiers, handling SCD lookups for historical fact rows."
  - type: MULTIPLE_CHOICE
    question: "Which of these is an example of a degenerate dimension in a loan fact table?"
    options:
      - "sk_member — the foreign key to the member dimension"
      - "loan_days — the number of days the item was borrowed"
      - "loan_reference_number — a human-readable loan ID used for drill-through, with no attributes worth a separate dimension table"
      - "sk_date — the foreign key to the date dimension"
    correctIndex: 2
    feedback: "A degenerate dimension is a dimensional attribute stored directly in the fact table because it has no other attributes — it doesn't justify a full dimension table. loan_reference_number (e.g. 'LN-2024-00042') is an identifier from the operational system used to link back to source records (drill-through from analytics to the original transaction). It's dimensional in nature (it identifies/classifies) but has no further attributes — no name, no category, nothing else to store. A separate dim_loan_reference table would have only one column (loan_reference_number) — wasteful. So it lives directly in the fact table. Other examples: order number, invoice number, transaction ID, receipt number."
retrieval:
  recall: "Describe the three types of fact tables. For each type, give the Archive-system equivalent (what would a periodic snapshot, transaction, and accumulating snapshot look like for the Archive's loans and members)."
  explain: "Explain the ETL fact table loading process: what the source provides (natural keys, raw measures), what transformations are needed (surrogate key lookup, business rule application), and how SCD Type 2 affects the SK lookup step."
  mistakeId:
    code: |
      CREATE TABLE loan_facts (
          sk_loan      BIGINT PRIMARY KEY,
          sk_member    BIGINT,
          sk_item      BIGINT,
          sk_date      INT,
          loan_days    INT,
          fine_amount  DECIMAL(10,2),
          return_rate  DECIMAL(5,4),   -- fine / max_possible_fine
          avg_fine     DECIMAL(10,2),  -- average fine across all loans this month
          loan_count   INT             -- total loans this member has ever made
      );
    answer: "Three measure design problems: (1) return_rate (fine / max_possible_fine) is a non-additive ratio — SUM(return_rate) across loans is meaningless. Ratios must be computed at query time from additive components: SUM(fine_amount) / SUM(max_possible_fine). Store fine_amount and max_possible_fine separately. (2) avg_fine is semi-additive — it's an average of a summary period, not a per-row measure. Storing averages in fact tables destroys their additive composability. Store the components (fine_amount, loan_count) and compute AVG at query time: SUM(fine_amount) / SUM(loan_count). (3) loan_count as a cumulative lifetime total violates the grain. Each row's loan_count would change over time as the member makes more loans — this is a periodic snapshot measure, not a transaction fact measure. The fact table row should be immutable after ETL. Use COUNT(*) at query time or use a periodic snapshot fact for cumulative totals."
---

# Hook

The fact table is the core of the warehouse — the table you query billions of times in production. Getting its design right — choosing the correct fact type, defining measures correctly, handling degenerate dimensions and factless facts — determines whether your analytics are fast, correct, and flexible. Most data quality bugs in warehouses originate in poorly designed fact tables.

# Lore Introduction

"The loan counts are wrong," the Junior Engineer reported, reviewing the board report. "The monthly total is double the actual number." The Senior Archivist pulled up the fact table. "You have renewal events in the same fact table as new loan events. The grain is mixed — two different types of events." The Junior examined the rows. "So when I count loans, I'm counting renewals as loans." The Senior Archivist nodded. "And when you sum fine amounts, you're double-counting fines that appear on both the loan row and the renewal row." She closed the query. "You need to understand the types of fact tables — transaction, snapshot, accumulating — and which measures can be safely summed. The fact table is not just any table. It is the source of truth for every number in every report. Design it incorrectly and every report is wrong."

# Core Learning

## Concept Introduction

### Three Types of Fact Tables

```
1. TRANSACTION FACT TABLE
   ─────────────────────────────────────────────────────────────────
   One row per discrete event. Rows are NEVER updated after insert.
   
   loan_transaction_facts:
   sk_loan | sk_member | sk_item | sk_date | event_type | loan_days | fine
   --------|-----------|---------|---------|------------|-----------|-----
   1       | 42        | 77      | 20240115| CHECKED_OUT| NULL      | 0
   2       | 42        | 77      | 20240220| RETURNED   | 36        | 5.00
   3       | 42        | 77      | 20240302| RENEWED    | NULL      | 0
   
   Good for: event counts, time-between-events, audit trail
   Bad for: "current state of a loan" (need to aggregate events)

2. PERIODIC SNAPSHOT FACT TABLE
   ─────────────────────────────────────────────────────────────────
   One row per entity per time period. Captures state at fixed intervals.
   
   member_monthly_snapshot_facts:
   sk_snapshot | sk_member | sk_date(month) | active_loans | total_fines_ytd
   ------------|-----------|----------------|--------------|----------------
   1           | 42        | 20240131       | 2            | 0.00
   2           | 42        | 20240229       | 3            | 2.50
   3           | 42        | 20240331       | 1            | 7.50
   
   Good for: trend analysis, current state per period, comparing periods
   Caution: measures may be semi-additive (balance: OK to SUM by member, NOT by time)

3. ACCUMULATING SNAPSHOT FACT TABLE
   ─────────────────────────────────────────────────────────────────
   One row per lifecycle instance. Rows ARE updated as milestones complete.
   
   loan_lifecycle_facts:
   sk_loan | sk_member | sk_item | sk_created | sk_due | sk_returned | loan_days | was_overdue
   --------|-----------|---------|------------|--------|-------------|-----------|------------
   1       | 42        | 77      | 20240115   | 20240215| 20240220   | 36        | TRUE
   2       | 55        | 92      | 20240301   | 20240401| NULL       | NULL      | FALSE (ongoing)
   
   Good for: pipeline analysis, lifecycle duration, funnel completion rates
   INSERT on creation, UPDATE when milestones complete
```

### Measure Types

```sql
-- ADDITIVE: can SUM across ALL dimensions (most measures)
SUM(loan_days)         -- total lending days by region, by item, by any dimension
SUM(fine_amount)       -- total fines collected — add across any combination
COUNT(*)               -- loan count — add across any subset

-- SEMI-ADDITIVE: can SUM across SOME dimensions only
-- Example: account balance in a periodic snapshot
SUM(balance) WHERE month = 'December'   -- VALID: total deposits across members (spatial sum)
SUM(balance) WHERE account_id = 42      -- INVALID: sums the same money 12 times (temporal sum)

-- Correct aggregation across time for semi-additive:
SELECT MAX(balance), MIN(balance), AVG(balance)
FROM member_monthly_snapshot_facts
WHERE sk_member = 42 AND year = 2024;
-- Or use LAST_VALUE(balance) with window function for period-end snapshot

-- NON-ADDITIVE: cannot SUM — must recompute from components
-- WRONG: SUM(overdue_rate)  -- meaningless
-- RIGHT: SUM(overdue_loans) / SUM(total_loans) as overdue_rate  -- compute at query time

-- Store components, compute ratios at query time:
CREATE TABLE loan_facts (
    ...
    fine_amount       DECIMAL(10,2),    -- additive component
    max_fine_amount   DECIMAL(10,2),    -- additive component
    -- fine_rate NOT stored: compute as SUM(fine_amount)/SUM(max_fine_amount)
);
```

### Degenerate Dimensions and Factless Facts

```sql
-- DEGENERATE DIMENSION: dimensional attribute with no dimension table
-- loan_reference is an identifier, not a measure, but has no other attributes
CREATE TABLE loan_facts (
    sk_loan           BIGINT PRIMARY KEY,
    sk_member         BIGINT NOT NULL,
    sk_item           BIGINT NOT NULL,
    sk_loan_date      INT NOT NULL,
    -- Degenerate dimension: reference back to operational system
    loan_reference    VARCHAR(50),    -- 'LN-2024-001234' — for drill-through
    -- Measures:
    loan_days         INT,
    fine_amount       DECIMAL(10,2) DEFAULT 0.00,
    overdue_days      INT DEFAULT 0
);

-- FACTLESS FACT TABLE: records event occurrence, no measures
CREATE TABLE member_event_facts (
    sk_member   BIGINT NOT NULL,
    sk_event    BIGINT NOT NULL,
    sk_date     INT NOT NULL,
    PRIMARY KEY (sk_member, sk_event, sk_date)
);
-- "Did member 42 attend the library event on 2024-03-15?" → COUNT(*) = 1
-- "Which members attended NO events in Q1?" → LEFT JOIN, WHERE sk_member IS NULL
```

### ETL Fact Table Loading

```sql
-- Source (OLTP normalized) → Warehouse (fact + surrogate key lookup)
INSERT INTO loan_facts (sk_member, sk_item, sk_loan_date, sk_due_date,
                         sk_return_date, loan_days, fine_amount, loan_reference)
SELECT
    dm.sk_member,                          -- surrogate key lookup: member
    di.sk_item,                            -- surrogate key lookup: item
    dl.sk_date AS sk_loan_date,           -- surrogate key lookup: loan date
    dd.sk_date AS sk_due_date,            -- surrogate key lookup: due date
    dr.sk_date AS sk_return_date,         -- NULL if not returned
    EXTRACT(DAY FROM (l.return_date - l.loan_date))::INT AS loan_days,
    COALESCE(l.fine_amount, 0.00),
    l.loan_reference
FROM loans l                               -- source table
JOIN dim_member dm ON l.member_id = dm.member_key AND dm.is_current = TRUE
JOIN dim_item di ON l.item_id = di.item_key AND di.is_current = TRUE
JOIN dim_date dl ON l.loan_date = dl.calendar_date
JOIN dim_date dd ON l.due_date = dd.calendar_date
LEFT JOIN dim_date dr ON l.return_date = dr.calendar_date   -- NULL if ongoing
WHERE l.loan_date >= :last_etl_run          -- incremental: only new loans
  AND NOT EXISTS (
    SELECT 1 FROM loan_facts lf WHERE lf.loan_reference = l.loan_reference
  );                                         -- idempotency: skip already loaded
```

## Common Mistakes

- **Storing non-additive measures**: never store rates, ratios, or percentages in a fact table. `SUM(return_rate)` is meaningless. Store the numerator and denominator as additive measures; compute ratios at query time.
- **Mixing event types with different grains in one fact table**: loan creation events and loan renewal events have different grain (one is a loan event, one is a renewal event). Mix them and `COUNT(*)` returns renewals as loans. Use separate fact tables or an `event_type` dimension key.
- **Updating transaction fact rows**: transaction facts should be immutable. Corrections should be handled with reversal rows (insert a row with negative measures to cancel the original) not by updating existing rows — updates break audit trail and incremental ETL logic.
- **Missing idempotency in ETL**: if ETL reruns due to a failure, it must not insert duplicate rows. Always include a uniqueness check (NOT EXISTS or INSERT ... ON CONFLICT DO NOTHING) in the fact table load.

## Mental Model

The three fact table types map to three ways of recording reality. A transaction fact is like a receipt — a permanent record of each event. A periodic snapshot is like a bank statement — a summary of state at the end of each period. An accumulating snapshot is like a workflow tracker — one row per case that updates as the case progresses through stages. Which one you choose depends on the questions you need to answer: "what happened?" (transaction), "what is the state right now vs last month?" (periodic), "how long did each case take, and where did it get stuck?" (accumulating).

## Mini Summary

- ✔ Transaction fact: one row per event, immutable — use for event counting, audit
- ✔ Periodic snapshot: one row per entity per time period — use for state-over-time tracking
- ✔ Accumulating snapshot: one row per lifecycle, updated at milestones — use for pipeline/funnel
- ✔ Additive measures: SUM across all dimensions (fine_amount, loan_days)
- ✔ Semi-additive: SUM across some dimensions (balance — OK by entity, NOT by time)
- ✔ Non-additive: never SUM (ratios/rates) — store components and compute at query time
- ✔ ETL: natural key → surrogate key lookup; idempotent loads; immutable transaction rows

# Guided Practice Quest

Work through the guided steps to classify three Archive measures (loan count, overdue rate, outstanding fine balance) as additive/semi-additive/non-additive, design the accumulating snapshot fact table for a loan lifecycle, and write the ETL INSERT to populate loan_facts from the normalised source with surrogate key lookups.

# Solo Practice Quest

Design and validate the complete fact table layer for the Archive data warehouse. Tasks: (1) Design three fact tables: a transaction fact for loan events, a periodic snapshot for monthly member activity, and an accumulating snapshot for loan lifecycles — specify grain, all columns, measure types; (2) For each of five candidate measures (loan count, average fine rate, outstanding balance, items borrowed per member, overdue duration), classify as additive/semi-additive/non-additive and explain what you would actually store in the fact table; (3) Write the ETL for your accumulating snapshot — including the UPDATE step for when loans are returned; (4) Design a factless fact table for a new Archive feature: recording which members participated in which reading groups on which dates; (5) Write a query that correctly computes average fine rate across members and months using only additive components stored in the fact table.

# Integration

**Mathematics**: The additive/semi-additive/non-additive classification formalises the mathematical properties of aggregate functions. An additive measure f satisfies: f(A ∪ B) = f(A) + f(B) for disjoint sets A and B. COUNT and SUM have this property — SUM(north_revenue) + SUM(south_revenue) = SUM(all_revenue). This is the defining property of a measure on a set (measure theory: σ-additivity). Semi-additive measures are additive along some dimensions (member axis) but not others (time axis). This is because balance at time T includes all events up to T — it is a cumulative function, not an incremental one. Non-additive measures (ratios) are quotients of additive measures — their numerators and denominators are additive, but the ratio itself is not: (a₁ + a₂)/(b₁ + b₂) ≠ a₁/b₁ + a₂/b₂. This is Jensen's inequality in disguise: the average of ratios ≠ the ratio of averages.

**Sciences (History — Archival Record Types)**: The three fact table types have direct parallels in archival science. Transaction facts correspond to event registers — ledgers recording each transaction as it occurs (loan register, fine ledger). Periodic snapshots correspond to censuses or inventory counts — a point-in-time enumeration of current state (annual stock count, monthly member roster). Accumulating snapshots correspond to case files — a single record per instance that accumulates information as the case progresses (member file, loan dossier). Archivists have managed these three record types for centuries because they answer fundamentally different questions: "what happened?" (event register), "what was the state at a given time?" (census), "how did this instance progress?" (case file). Data warehousing re-discovered these patterns as first principles.

# Lore Conclusion

"The loan counts are correct now," the Junior Engineer reported. "Transaction fact table for events, accumulating snapshot for lifecycle analysis. The renewal events are in their own event_type group — they don't inflate the loan count." The Senior Archivist reviewed the measures. "Fine amount: additive. Overdue rate: computed at query time from overdue_days and loan_days. Outstanding balance: semi-additive — we only sum by member, not by time." The Junior nodded. "And the ETL is idempotent — I tested it by running it twice. No duplicates." The Senior Archivist set the report down. "The fact table is correct. The measures are reliable. The board reports will be accurate." She looked at the remaining schema. "One layer left: dimension tables. Not just their structure, but their management over time — how they change, and how you preserve history when they do."

---
