---
id: de-jun-m8-01
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m8
moduleTitle: "Module 8: Database Testing"
moduleGlyph: "🧪"
moduleSortOrder: 8
topicSlug: data_validation
topicTitle: "Data Validation"
topicSortOrder: 1
lesson: data_validation
title: "Data Validation"
sortOrder: 1
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m7-04]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Distinguishes database-level validation from application-level validation
    - Explains CHECK constraints, NOT NULL, UNIQUE, and FK constraints as validation mechanisms
    - Describes data quality dimensions (completeness, validity, consistency, timeliness)
    - Explains assertion-based data quality testing with SQL
    - Identifies the difference between detecting and preventing bad data
  keywords: [validation, CHECK constraint, NOT NULL, UNIQUE, foreign key, data quality, completeness, validity, consistency, timeliness, assertion, invariant, domain constraint, referential integrity, dbt test, Great Expectations]
  modelAnswer: |
    Database-level validation prevents bad data from entering the database: NOT NULL enforces completeness, CHECK validates value domains (status IN ('active','inactive')), UNIQUE prevents duplicates, FK enforces referential integrity. Application-level validation happens before the INSERT/UPDATE and provides user-friendly error messages. Both layers are needed — database constraints are the last line of defence. Data quality dimensions: completeness (all required fields populated), validity (values within acceptable domains), consistency (relationships between fields are coherent), timeliness (data is current). Assertion testing: SQL queries that should return zero rows if data is valid — SELECT COUNT(*) FROM loans WHERE due_date < loan_date (should be 0). Tools like dbt tests and Great Expectations automate these assertions and run them as part of CI/CD pipelines. Detecting bad data (monitoring queries) is different from preventing it (constraints) — both are needed.
guidedSteps:
  - id: de-jun-m8-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A loan is inserted with due_date = '2024-01-01' and loan_date = '2024-03-15' (due date before loan date). Which mechanism prevents this at the database level?
    inputConfig:
      options:
        - "NOT NULL constraint on loan_date and due_date"
        - "UNIQUE constraint on (loan_date, due_date)"
        - "CHECK constraint: CHECK (due_date > loan_date)"
        - "Foreign key constraint on loan_date referencing dim_date"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["CHECK constraint: CHECK (due_date > loan_date)"]
      rejectedFeedback: "A CHECK constraint validates that a boolean expression is true for every row. CHECK (due_date > loan_date) rejects any INSERT or UPDATE where due_date is not strictly after loan_date. NOT NULL only verifies that values are present — it doesn't validate the relationship between values. UNIQUE prevents two rows from having the same combination — irrelevant here. FK constraints validate that a value exists in another table — they don't validate business rules about relationships between columns in the same row. CHECK constraints are the database's mechanism for business rule enforcement — encoding knowledge like 'a due date must be after the loan date' directly in the schema. If the business rule changes, you ALTER the constraint — the enforcement follows automatically."
    hint: "Which constraint type validates a condition involving multiple columns in the same row?"
    reflectionPrompt: "What are the limits of CHECK constraints? What business rules cannot be expressed as a CHECK constraint on a single row?"
  - id: de-jun-m8-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A data quality test that asserts a condition should always be true is called a data ________. If the query returns any rows, the test fails.
    inputConfig:
      placeholder: "assertion"
    markingRule:
      matchMode: CONTAINS
      accepted: [assertion, assertions, "data assertion", invariant, "quality assertion", "data test"]
      rejectedFeedback: "Data assertions are SQL queries that should return zero rows if data quality is maintained. They express invariants — conditions that must always be true. Examples: SELECT * FROM loans WHERE due_date <= loan_date — must return 0 rows. SELECT l.id FROM loans l LEFT JOIN members m ON l.member_id = m.id WHERE m.id IS NULL — orphaned loans, must return 0. SELECT member_id, COUNT(*) FROM loans WHERE status = 'ACTIVE' GROUP BY member_id HAVING COUNT(*) > max_concurrent_loans — must return 0. Tools like dbt tests (schema tests + custom data tests), Great Expectations, and Soda Core run these assertions automatically in CI/CD pipelines and data pipeline runs. A failing assertion triggers an alert — either blocking a pipeline run (preventing bad data from propagating) or notifying the data team (for monitoring)."
    hint: "Like a code assertion that should pass — if it returns rows, something is wrong."
    reflectionPrompt: "Should data assertions run before or after a data pipeline loads new data? Why?"
  - id: de-jun-m8-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why database constraints alone are insufficient for data quality, and what role assertion-based testing adds.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [cross-row, cross-table, aggregate, historical, pattern, exist, prevent, detect, monitor, pipeline, runtime, assertion, CHECK, cannot]
      rejectedFeedback: "Database constraints prevent violations at write time on a single row or foreign key relationship. They cannot: (1) Enforce aggregate invariants — 'no member can have more than 3 active loans' requires counting rows, not evaluating a single row. (2) Detect gradual degradation — if the email column's non-null rate drops from 99% to 70% over a week (ETL bug), no constraint fires. (3) Validate cross-table consistency not expressed as FK — 'all loans with status ACTIVE should have a member with status ACTIVE'. (4) Enforce business rules that span time — 'a renewal cannot be issued after the return date'. Assertion testing fills these gaps: run queries that check invariants, monitor metrics (null rates, row counts, statistical distributions), and alert when data quality degrades. Constraints prevent individual bad rows; assertions monitor the health of the dataset as a whole."
    hint: "What can a constraint on a single row NOT verify? Think about counts across rows or conditions spanning multiple tables."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Data quality dimension 'completeness' refers to:"
    options:
      - "All historical data is present — no records are missing from any time period"
      - "All required fields are populated — no unexpected NULLs in mandatory columns"
      - "Data values are within acceptable ranges and domains"
      - "Data reflects the current real-world state"
    correctIndex: 1
    feedback: "Completeness: all required data is present. Measured as: non-null rate for mandatory columns (email NULL rate should be 0%), row count relative to expected (a daily ETL should load at least N rows per day), foreign key coverage (all loan member_ids should have matching member records). Tools like Great Expectations use 'expect_column_values_to_not_be_null' and 'expect_table_row_count_to_be_between' to monitor completeness. Other dimensions: validity (values in acceptable domains — status must be one of 'ACTIVE', 'RETURNED', 'OVERDUE'), consistency (relationships are coherent — return_date >= loan_date), accuracy (data matches the real-world source — harder to test without ground truth), timeliness (data is current — last ETL run was within expected window), uniqueness (no duplicate records for the same entity)."
  - type: MULTIPLE_CHOICE
    question: "The FOREIGN KEY constraint with ON DELETE RESTRICT means:"
    options:
      - "Deleting the parent row automatically deletes all related child rows"
      - "Deleting the parent row sets the FK in child rows to NULL"
      - "Deleting the parent row is rejected if any child rows reference it"
      - "Foreign key checks are deferred until the end of the transaction"
    correctIndex: 2
    feedback: "ON DELETE RESTRICT (default in PostgreSQL if omitted): prevents deletion of a parent row if child rows reference it. DELETE FROM members WHERE id = 42 fails with: ERROR: update or delete on table 'members' violates foreign key constraint 'loans_member_id_fkey' on table 'loans'. This is referential integrity enforcement — you cannot remove a member who has outstanding loans. Options: ON DELETE CASCADE (delete child rows automatically — use with caution, destructive), ON DELETE SET NULL (set FK to NULL in child rows — requires FK to be nullable), ON DELETE SET DEFAULT (set FK to a default value), DEFERRABLE INITIALLY DEFERRED (defer constraint check to transaction end — needed for bulk imports). For the Archive: loans should use ON DELETE RESTRICT on member_id — you must handle loan cleanup before deleting a member."
retrieval:
  recall: "List five SQL assertion queries for the Archive database that should return zero rows if data quality is maintained. For each, explain what data quality rule it tests."
  explain: "Explain the four data quality dimensions most relevant to a library system (completeness, validity, consistency, timeliness). Give a specific metric for each dimension that you would monitor."
  mistakeId:
    code: |
      CREATE TABLE loans (
          id          BIGSERIAL PRIMARY KEY,
          member_id   BIGINT,        -- no NOT NULL, no FK
          item_id     BIGINT,        -- no NOT NULL, no FK
          loan_date   DATE,          -- no NOT NULL
          due_date    DATE,          -- no NOT NULL
          status      VARCHAR(50)    -- no constraint on valid values
      );
    answer: "No constraints: (1) member_id and item_id have no NOT NULL — NULL member loans can be inserted (anonymous loans? orphaned records?). No FK constraint — member_id = 9999999 can be inserted even if no such member exists. Fix: BIGINT NOT NULL REFERENCES members(id) ON DELETE RESTRICT. (2) loan_date and due_date have no NOT NULL — a loan with no date is meaningless. Fix: DATE NOT NULL. (3) status has no domain constraint — any string can be stored ('ACTIV', 'active', 'Active', typos). Fix: add CHECK (status IN ('ACTIVE', 'RETURNED', 'OVERDUE', 'LOST')). (4) Missing business rule: due_date should be after loan_date. Fix: CHECK (due_date > loan_date). (5) Missing: no DEFAULT for status — new loans should default to 'ACTIVE'. The fully constrained table prevents an entire class of data quality problems at the source, eliminating the need for validation assertions on these specific invariants."
---

# Hook

A database that accepts any data is not a database — it's a bin. Constraints define what valid data looks like; validation tests verify that reality matches expectation. Together, they prevent the downstream corruption that turns "slightly wrong data" into "completely wrong reports."

# Lore Introduction

"The overdue report shows negative overdue days," the Junior Engineer reported. "Some loans have a due date before the loan date." The Senior Archivist checked the loans table. "No CHECK constraint. The migration script had a bug — it inserted some rows with transposed dates. The database accepted them." The Junior Engineer looked at the schema. "Should the database have rejected them?" The Senior Archivist nodded. "The rule 'due date must be after loan date' is a business invariant. It belongs in the database as a constraint, not only in the application as a validation." She pulled up the test suite. "It also belongs in a data assertion — a test that we run every night to verify invariants are still holding. The database prevents new violations. The assertion detects old ones and monitors for degradation."

# Core Learning

## Concept Introduction

### Database Constraints as Validation

```sql
-- Complete constraint coverage for the loans table:
CREATE TABLE loans (
    id          BIGSERIAL PRIMARY KEY,

    -- Referential integrity: cannot create loan without valid member/item
    member_id   BIGINT NOT NULL REFERENCES members(id) ON DELETE RESTRICT,
    item_id     BIGINT NOT NULL REFERENCES items(id) ON DELETE RESTRICT,

    -- Domain constraints: required fields
    loan_date   DATE    NOT NULL,
    due_date    DATE    NOT NULL,

    -- Business rule constraint: due date must be after loan date
    CONSTRAINT chk_due_after_loan CHECK (due_date > loan_date),

    -- Domain constraint: status must be one of the valid values
    status      VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT chk_status CHECK (status IN ('ACTIVE', 'RETURNED', 'OVERDUE', 'LOST')),

    -- Optional fields with value constraints
    return_date DATE,
    CONSTRAINT chk_return_after_loan CHECK (return_date IS NULL OR return_date >= loan_date),
    CONSTRAINT chk_return_date_requires_status
        CHECK (return_date IS NULL OR status IN ('RETURNED', 'LOST')),

    fine_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT chk_fine_non_negative CHECK (fine_amount >= 0),

    -- Uniqueness: prevent duplicate active loans for same member+item
    CONSTRAINT uq_active_loan UNIQUE NULLS NOT DISTINCT (member_id, item_id)
    -- (partial unique index preferred for conditional uniqueness)
);

-- Partial unique index: one active loan per member per item
CREATE UNIQUE INDEX idx_one_active_loan
    ON loans (member_id, item_id)
    WHERE status = 'ACTIVE';
```

### Data Quality Dimensions

```sql
-- COMPLETENESS: are all required fields populated?
SELECT
    COUNT(*) AS total_loans,
    COUNT(member_id) AS has_member,
    COUNT(item_id) AS has_item,
    COUNT(loan_date) AS has_loan_date,
    100.0 * COUNT(member_id) / COUNT(*) AS member_completeness_pct
FROM loans
WHERE loan_date >= CURRENT_DATE - INTERVAL '30 days';

-- VALIDITY: are values within acceptable domains?
SELECT COUNT(*) AS invalid_status_count
FROM loans
WHERE status NOT IN ('ACTIVE', 'RETURNED', 'OVERDUE', 'LOST');  -- should be 0

-- CONSISTENCY: are related values coherent?
SELECT COUNT(*) AS date_inversions
FROM loans
WHERE due_date <= loan_date;  -- should be 0

SELECT COUNT(*) AS returned_without_date
FROM loans
WHERE status = 'RETURNED' AND return_date IS NULL;  -- should be 0

-- TIMELINESS: is data current?
SELECT EXTRACT(MINUTES FROM (NOW() - MAX(created_at))) AS minutes_since_last_loan
FROM loans;  -- should be within expected operational window
```

### Assertion-Based Testing

```sql
-- Data assertions: queries that should return 0 rows
-- Run these in CI/CD, after ETL loads, or on a schedule

-- Assertion 1: No loans with due_date before loan_date
WITH violation AS (
    SELECT id, member_id, loan_date, due_date
    FROM loans WHERE due_date <= loan_date
)
SELECT COUNT(*) AS violations FROM violation;  -- must be 0

-- Assertion 2: No orphaned loans (member deleted but loans remain)
SELECT COUNT(*) AS orphaned_loans
FROM loans l
LEFT JOIN members m ON l.member_id = m.id
WHERE m.id IS NULL;  -- must be 0

-- Assertion 3: No member with more than 5 active loans
SELECT member_id, COUNT(*) AS active_count
FROM loans WHERE status = 'ACTIVE'
GROUP BY member_id
HAVING COUNT(*) > 5;  -- must return no rows

-- Assertion 4: All returned loans have a return_date
SELECT COUNT(*) AS returned_no_date
FROM loans WHERE status = 'RETURNED' AND return_date IS NULL;  -- must be 0

-- Assertion 5: Fine amount non-negative
SELECT COUNT(*) AS negative_fines
FROM loans WHERE fine_amount < 0;  -- must be 0
```

### dbt Schema Tests

```yaml
 # dbt schema.yml — automated data quality testing
models:
  - name: loans
    columns:
      - name: id
        tests:
          - unique
          - not_null
      - name: member_id
        tests:
          - not_null
          - relationships:
              to: ref('members')
              field: id
      - name: status
        tests:
          - not_null
          - accepted_values:
              values: ['ACTIVE', 'RETURNED', 'OVERDUE', 'LOST']
      - name: due_date
        tests:
          - not_null

 # Custom dbt test — due_date after loan_date:
 # tests/assert_due_date_after_loan_date.sql
SELECT *
FROM {{ ref('loans') }}
WHERE due_date <= loan_date
```

### Monitoring Data Quality Over Time

```sql
-- Track data quality metrics in a monitoring table
CREATE TABLE data_quality_log (
    run_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    table_name      VARCHAR(100),
    metric_name     VARCHAR(100),
    metric_value    NUMERIC,
    status          VARCHAR(10)  -- 'PASS' or 'FAIL'
);

-- Populate after each ETL run or on a schedule:
INSERT INTO data_quality_log (table_name, metric_name, metric_value, status)
SELECT 'loans', 'completeness_member_id',
       100.0 * COUNT(member_id) / NULLIF(COUNT(*), 0),
       CASE WHEN 100.0 * COUNT(member_id) / NULLIF(COUNT(*), 0) >= 99 THEN 'PASS' ELSE 'FAIL' END
FROM loans;

-- Alert if FAIL: ship data_quality_log to monitoring system
-- Plot metric trends: a drop in completeness % indicates an ETL issue
```

## Why It Matters

Bad data is cheaper to reject than to clean — validation is the gate that keeps a database trustworthy:

- Database constraints (NOT NULL, CHECK, foreign keys) are the last line of defence that no buggy application path can bypass
- Application-level validation gives friendly errors; database-level validation guarantees integrity — production systems need both layers
- A single unvalidated load can poison months of reports, and finding the bad rows afterwards costs far more than checking on entry

The teams with clean data aren't lucky; they validate at the boundary. The teams doing data cleanup every quarter skipped this lesson.

## Common Mistakes

- **Validating only in the application**: application validation provides user-friendly errors, but is bypassed by direct SQL, migration scripts, and other services. Database constraints are the authoritative last line of defence — both layers are needed.
- **No CHECK constraints on status/type columns**: without a CHECK constraint, typos ('ACTIV', 'active') or new unhandled values ('CANCELLED') silently enter the database and break downstream queries. Always constrain categorical columns.
- **Data assertions run after data has propagated**: if assertions run only at the end of the pipeline, bad data may already be in the warehouse, sent to external systems, or used in reports. Run assertions as early gating checks — fail fast before propagation.
- **No baseline for "normal"**: a completeness rate of 85% may be fine or catastrophic depending on the column. Establish baselines during initial load, then alert on deviations greater than a threshold (e.g. alert if completeness drops more than 2% from baseline).

## Mental Model

Data validation is like quality control in manufacturing. The machine that shapes the part (database constraint) rejects defective parts immediately — they never enter the system. The inspector who checks every finished part (data assertion) catches systematic defects: if 1% of parts are failing a measurement that should be zero, there is a systematic problem in the process. The monitoring dashboard (data quality log) tracks quality over time — a downward trend in the measurement rate signals a problem before it becomes a crisis.

## Mini Summary

- ✔ Database constraints (NOT NULL, CHECK, UNIQUE, FK): prevent individual invalid rows
- ✔ CHECK constraints: encode business rules as invariants at the schema level
- ✔ Data quality dimensions: completeness, validity, consistency, timeliness, uniqueness
- ✔ Data assertions: SQL queries that should return 0 rows — test invariants across the full dataset
- ✔ dbt tests: schema tests + custom data tests run in CI/CD pipelines
- ✔ Quality monitoring: track metrics over time, alert on degradation from baseline
- ✔ Application validation + database constraints: both needed — different failure modes

# Guided Practice Quest

Work through the guided steps to add CHECK constraints to the loans table for the five most critical business rules, write three data assertions as SQL queries that should return zero rows, and design a data quality monitoring query for the most important completeness metric.

# Solo Practice Quest

Design and implement a data validation layer for the Archive database. Tasks: (1) Audit the current loans, members, and items tables — list every constraint that is missing and write the ALTER TABLE statements to add them; (2) Write ten data assertion queries covering completeness, validity, consistency, and referential integrity; (3) Create a data_quality_log table and write the SQL to populate it with five metrics after each ETL run; (4) Define threshold rules for when each metric should alert (e.g., completeness must be ≥ 99%, duplicate rate must be 0%); (5) Implement the dbt schema.yml for the loans table with all built-in tests and one custom test file; (6) Explain how you would set up a data quality regression test — if a deploy introduces a schema change that accidentally breaks an invariant, how does the test suite catch it before production?

# Integration

**Mathematics**: Data quality testing is formally related to the field of constraint satisfaction problems (CSP). A database schema with constraints defines a constraint satisfaction problem: the set of all valid states S is the set of all row combinations that satisfy all constraints. A data quality assertion is a query that verifies a subset of constraints against the current database state. If all assertions pass, the database state ∈ S (the valid state space). A constraint can be classified by its scope: single-row constraints (CHECK on one row), tuple constraints (uniqueness across the table), and referential constraints (FK across tables). The Boolean satisfiability problem (SAT) is NP-complete for arbitrary constraint combinations — but SQL database constraints are typically restricted to polynomial-time checkable constraints. A failed assertion identifies a specific violated constraint and the offending rows — a witness to the violation.

**Sciences (Quality Engineering — Six Sigma)**: Data quality methodology borrows directly from manufacturing quality control, particularly Six Sigma's DMAIC framework (Define, Measure, Analyse, Improve, Control). Define: specify the data quality requirements (what values are valid, what relationships must hold). Measure: query the current state to establish the quality baseline (null rates, constraint violations, outlier distributions). Analyse: identify root causes of quality failures (ETL bugs, application validation gaps, data entry errors). Improve: fix the root cause and add constraints/assertions to prevent recurrence. Control: automated monitoring dashboards and alerting to maintain quality over time. The Six Sigma metric (defects per million opportunities — DPMO) translates to database quality as: violations per million rows. A 1% null rate in a mandatory field = 10,000 DPMO — unacceptably high for a system where loans are financial transactions with legal implications.

# Lore Conclusion

"The CHECK constraints are in place," the Junior Engineer reported. "Due date must be after loan date. Status must be one of four values. Fine amount non-negative. Five data assertions run nightly — all returning zero rows." The Senior Archivist reviewed the constraint list. "And the ten rows with inverted dates from the migration bug?" The Junior pulled up the correction script. "Corrected. The assertions verified the fix." The Senior Archivist nodded. "Prevention and detection. Constraints prevent future violations. Assertions detected the historical ones and confirm the correction." She looked at the data quality log table. "Track completeness and validity over time. A downward trend will warn you before a real incident." She set the schema aside. "You've validated the data. Next: integration testing — verifying that the application and database work correctly together."

---
