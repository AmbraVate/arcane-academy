---
id: de-app-m4-09
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m4
moduleTitle: "Module 4: Joining Information"
moduleGlyph: "🔗"
moduleSortOrder: 4
topicSlug: practical_data_retrieval
topicTitle: "Practical Data Retrieval"
topicSortOrder: 2
lesson: join_troubleshooting
title: "Join Troubleshooting"
sortOrder: 9
difficulty: 3
estimatedMinutes: 30
xpReward: 40
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m4-08]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Identifies the cause of duplicate rows in a join result
    - Diagnoses missing rows and determines whether INNER or LEFT JOIN is the cause
    - Recognises the Cartesian product symptom (row count explosion)
    - Applies diagnostic COUNT queries to verify join correctness
    - Reflects on how to systematically debug a join query that produces unexpected results
  keywords: [duplicate, missing rows, Cartesian, row explosion, one-to-many, diagnose, COUNT, verify, debug, unexpected]
  modelAnswer: |
    The three most common join problems are: (1) duplicate rows — caused by a one-to-many relationship where the "many" side multiplies the result; (2) missing rows — caused by INNER JOIN excluding unmatched rows, or a wrong ON condition; (3) row explosion — caused by a missing or wrong ON clause producing a Cartesian product. Diagnosis uses COUNT queries: compare the expected row count with the actual. Duplicate rows require deduplication (DISTINCT, GROUP BY, or subquery). Missing rows require checking the join type (INNER vs LEFT) and verifying the ON condition. Cartesian products require adding or correcting the ON clause.
guidedSteps:
  - id: de-app-m4-09-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A join of `orders` (5,000 rows) to `order_lines` (25,000 rows) on order_id returns 25,000 rows. You expected 5,000. What caused this?
    inputConfig:
      options:
        - "A Cartesian product — the ON clause is missing"
        - "Normal behaviour — each order appears once per order line (one-to-many)"
        - "A data error — the orders table has duplicates"
        - "LEFT JOIN included NULL rows"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Normal behaviour — each order appears once per order line (one-to-many)"]
      rejectedFeedback: "This is correct behaviour. Orders to order_lines is a one-to-many relationship: one order can have many lines. The join result has one row per (order, line) combination. An order with 5 line items appears 5 times in the result. If you wanted one row per order with aggregated line data, you need GROUP BY order_id with SUM/COUNT. The 25,000 rows are correct — one per line item, not one per order."
    hint: "In a one-to-many join, each row on the 'one' side appears multiple times — once for each matching row on the 'many' side."
    reflectionPrompt: "How would you transform this result to get one row per order showing the total order value from all its lines?"
  - id: de-app-m4-09-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A join returns far fewer rows than expected — many customers are missing. The first diagnostic step is to check whether the join type should be changed from INNER to ________.
    inputConfig:
      placeholder: "LEFT"
    markingRule:
      matchMode: CONTAINS
      accepted: [LEFT, left, "LEFT JOIN", "left join"]
      rejectedFeedback: "Missing rows after INNER JOIN usually means those rows have no match in the other table. Switching to LEFT JOIN shows all rows from the left table, with NULLs for unmatched right-table columns. If the missing rows then appear with NULL values, the problem was that INNER JOIN silently excluded them. You then need to decide: should they be excluded (use INNER), or should they appear with NULLs or defaults (use LEFT)?"
    hint: "The join type that preserves all rows from the left table regardless of matches."
    reflectionPrompt: "After switching to LEFT JOIN, what would you look for in the result to confirm the original query was wrong?"
  - id: de-app-m4-09-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A join query returns 50,000,000 rows when you expected about 5,000. In 2–3 sentences, explain what likely went wrong and how to fix it.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [Cartesian, ON, missing, condition, cross join, every, combination, fix, add]
      rejectedFeedback: "A row count of M × N (1,000 customers × 50,000 orders = 50,000,000) is a Cartesian product — produced when the ON clause is missing or incorrect. Without ON, the database matches every row from table A with every row from table B. The fix is to add the correct ON clause: ON orders.customer_id = customers.customer_id. Check that the join condition references the correct columns in the correct tables."
    hint: "If the result rows = (rows in table A) × (rows in table B), the ON clause is missing or wrong."
    reflectionPrompt: "How would you use COUNT(*) before and after adding the ON clause to confirm you fixed the Cartesian product?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A query joining employees to their manager (self-join) on manager_id = employee_id returns fewer employees than expected. The most likely cause is:"
    options:
      - "A Cartesian product from a missing ON clause"
      - "INNER JOIN excluding employees with no manager (NULL manager_id)"
      - "GROUP BY incorrectly grouping employees"
      - "SUM computing an incorrect total"
    correctIndex: 1
    feedback: "Employees with no manager have NULL in the manager_id column. INNER JOIN's ON condition (manager_id = employee_id) never evaluates to TRUE for NULL values — so employees without a manager are silently excluded. The fix is to use LEFT JOIN: LEFT JOIN employees AS m ON e.manager_id = m.employee_id. This preserves all employees, with NULL in the manager columns for those at the top of the hierarchy."
  - type: MULTIPLE_CHOICE
    question: "You run a query and get duplicate customer rows in the result. The most likely cause is:"
    options:
      - "The customers table has duplicate rows"
      - "A missing WHERE clause"
      - "The customer is joined to a table where they have multiple matching rows (one-to-many)"
      - "The SELECT includes too many columns"
    correctIndex: 2
    feedback: "Duplicate customer rows in a join result almost always come from a one-to-many relationship: each customer-to-order join produces one result row per order. A customer with 10 orders appears 10 times. This is correct join behaviour, not a bug. If you want one row per customer, use GROUP BY customer_id with appropriate aggregates (COUNT, SUM) to collapse the multiple rows into one summary row."
retrieval:
  recall: "Describe three symptoms of a broken join query and how to diagnose each one."
  explain: "Explain why a one-to-many join produces multiple rows for each 'one' side record, and how GROUP BY fixes it."
  mistakeId:
    code: "SELECT c.name, o.order_date FROM customers c JOIN orders o ON c.name = o.customer_name"
    answer: "Joining on a name column (a mutable, non-unique text value) instead of an ID (a stable, unique integer) is fragile: two customers with the same name produce incorrect cross-matches, and any name change breaks the relationship. Always join on primary and foreign keys — integers that are immutable, unique, and indexed. The correct ON clause is ON c.customer_id = o.customer_id."
---

# Hook

Join queries fail in predictable ways. Rows appear that shouldn't be there. Rows disappear that should be there. Millions of rows appear when you expected thousands. The good news: all three problems have clear diagnoses and fixes.

This lesson is a systematic troubleshooting guide for the join problems every data engineer encounters.

# Lore Introduction

"The report is wrong," the Guild Secretary said, dropping a printout on the desk. "It shows 847 members, but there should be 1,000. And three members appear five times each." Master Selvaris examined the query. "Two separate problems," she said. "The missing members are excluded by INNER JOIN — they have no transactions yet. The duplicates are from joining members to a table where each member has multiple rows — normal join behaviour, but you expected one row per member." She fixed the first by switching to LEFT JOIN. She fixed the second by adding GROUP BY. "Duplicate rows and missing rows are the two most common join problems. Different causes. Different fixes. Always diagnose before changing anything."

# Core Learning

## Concept Introduction

### Problem 1: Duplicate Rows

**Cause**: One-to-many join — each row from the "one" side is repeated for each matching row on the "many" side.

```sql
-- customers (1,000 rows) JOIN orders (5,000 rows)
-- Result: 5,000 rows — one per (customer, order) pair
-- Customer Alice appears 8 times if she has 8 orders
SELECT c.name, o.order_date FROM customers c JOIN orders o ON c.customer_id = o.customer_id;
-- 5,000 rows: NOT a bug, this is correct join behaviour
```

**Fix**: If you want one row per customer, use GROUP BY + aggregates:

```sql
-- One row per customer with order summary
SELECT c.name, COUNT(o.order_id) AS orders, SUM(o.total_amount) AS revenue
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.name;
-- 1,000 rows: one per customer
```

### Problem 2: Missing Rows

**Cause**: INNER JOIN silently excludes rows with no match.

```sql
-- Diagnosis: compare row counts
SELECT COUNT(*) FROM customers;                      -- 1,000
SELECT COUNT(DISTINCT c.customer_id)
FROM customers c JOIN orders o ON c.customer_id = o.customer_id; -- 800
-- 200 customers missing: they have no orders, excluded by INNER JOIN
```

**Fix**: Switch to LEFT JOIN to preserve unmatched rows:

```sql
-- LEFT JOIN keeps all 1,000 customers
SELECT c.name, COUNT(o.order_id) AS order_count
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.name;
```

### Problem 3: Cartesian Product (Row Explosion)

**Cause**: Missing or wrong ON clause.

```sql
-- WRONG: no ON clause
SELECT c.name, o.order_date FROM customers c JOIN orders o;
-- Result: 1,000 × 5,000 = 5,000,000 rows — every customer with every order

-- WRONG: ON uses wrong columns
SELECT c.name, o.order_date FROM customers c JOIN orders o ON c.customer_id = o.order_id;
-- Joins customer ID to order ID — semantically wrong, produces incorrect matches
```

**Fix**: Ensure ON links the correct FK to the correct PK:

```sql
-- CORRECT: FK in orders → PK in customers
FROM customers c JOIN orders o ON c.customer_id = o.customer_id
```

**Diagnostic**: If `result_rows = rows_in_A × rows_in_B`, you have a Cartesian product.

### Problem 4: Wrong ON Condition

```sql
-- Joining on a mutable text value instead of a stable ID
FROM customers c JOIN orders o ON c.name = o.customer_name
-- Problem: if two customers are named "John Smith", orders are cross-matched
-- Problem: name changes break the join silently
-- Fix: always join on PK/FK integer keys
```

### Diagnostic Toolkit

```sql
-- Step 1: count the source tables
SELECT COUNT(*) FROM customers;    -- expected: 1,000
SELECT COUNT(*) FROM orders;       -- expected: 5,000

-- Step 2: count the join result
SELECT COUNT(*) FROM customers c JOIN orders o ON c.customer_id = o.customer_id;
-- If result >> 5,000: Cartesian product (check ON clause)
-- If result << 5,000: INNER JOIN excluding rows (check join type)
-- If result = 5,000: correct (one row per order)

-- Step 3: check for unmatched rows
SELECT COUNT(*) FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
WHERE o.order_id IS NULL;
-- Shows how many customers have no orders (excluded by INNER JOIN)

-- Step 4: check for duplicate key values in the joined table
SELECT customer_id, COUNT(*) FROM orders GROUP BY customer_id ORDER BY COUNT(*) DESC LIMIT 5;
-- Shows customers with the most orders — explains why they appear multiple times
```

### Common NULL Issues in Join Results

```sql
-- Symptom: filter on right-table column eliminates too many rows
-- Cause: WHERE filter on NULL values after LEFT JOIN
-- Problem:
FROM customers c LEFT JOIN orders o ON c.customer_id = o.customer_id
WHERE o.status = 'completed';  -- excludes customers with no orders (NULL status ≠ 'completed')

-- Fix: move filter to ON clause
FROM customers c LEFT JOIN orders o ON c.customer_id = o.customer_id AND o.status = 'completed';
```

## Why It Matters

Joins fail quietly — wrong results, not error messages — so debugging them is a distinct skill you will use constantly:

- Too many rows? You've hit a one-to-many fan-out or a missing join condition (the accidental cross join)
- Too few rows? An INNER JOIN is dropping unmatched records, or a WHERE clause on the outer table's NULLs is undoing your LEFT JOIN
- Counting rows before and after each join is the systematic technique that finds these in minutes instead of hours

Every data engineer spends real time diagnosing someone else's join. Learning the failure patterns now means you'll recognise them on sight later.

## Common Mistakes

- **Assuming duplicate rows are a data error**: They are usually correct join behaviour. Diagnose before changing the query.
- **Fixing duplicates with DISTINCT instead of GROUP BY**: `SELECT DISTINCT` removes exact duplicate rows but does not aggregate. It does not give you one row per customer with a count — it removes rows that are identical in all selected columns.
- **Not verifying row counts after fixing a join**: After changing INNER to LEFT JOIN or adding a GROUP BY, run a COUNT to confirm the result is as expected.

## Mental Model

Debug a join like a detective: compare what you expected with what you got. More rows than expected → Cartesian product or one-to-many inflation. Fewer rows than expected → INNER JOIN exclusion or wrong ON condition. Rows multiplied exactly by N → likely a one-to-many join where you need GROUP BY to collapse. The COUNT diagnostic queries are your forensic tools — they show you exactly where the problem is before you change anything.

## Mini Summary

- ✔ Duplicate rows: one-to-many join — fix with GROUP BY + aggregates
- ✔ Missing rows: INNER JOIN excluding unmatched rows — fix with LEFT JOIN
- ✔ Row explosion: Cartesian product (missing ON) — fix by adding correct ON clause
- ✔ Use COUNT(*) diagnostics to compare expected vs actual row counts
- ✔ Always join on PK/FK columns, not mutable text values

# Guided Practice Quest

Work through the guided steps to identify a row explosion from a missing ON clause, diagnose missing rows from an INNER JOIN, and understand why one-to-many joins produce multiple rows per "one" side record.

# Solo Practice Quest

You are given four broken join queries. For each: (a) identify the problem, (b) explain why it produces wrong results, (c) write the corrected query. The four broken queries: (1) `SELECT c.name, p.name FROM customers c JOIN products p` — produces millions of rows; (2) `SELECT c.name, COUNT(*) FROM customers c JOIN orders o ON c.customer_id = o.customer_id GROUP BY c.customer_id, c.name` — missing 300 customers; (3) `SELECT e.name, m.name AS manager FROM employees e JOIN employees m ON e.name = m.name` — self-join on name produces wrong matches; (4) `SELECT c.name, SUM(o.total_amount) FROM customers c LEFT JOIN orders o ON c.customer_id = o.customer_id WHERE o.status = 'completed' GROUP BY c.customer_id, c.name` — unintentionally excludes customers with no completed orders. For each fix, explain your reasoning.

# Integration

**Mathematics**: Join troubleshooting is fundamentally about understanding the cardinality of the result relation. The expected cardinality of a join R ⋈ S depends on the key relationship: if A is a primary key in R and a foreign key in S, then |R ⋈ S| = |S| (each tuple in S matches exactly one in R). If neither side has a unique key, the result can have up to |R| × |S| tuples (Cartesian product). Duplicate rows in a join are not data errors — they reflect the true cardinality of the relationship. The GROUP BY operation then applies the summation to each equivalence class, reducing the result back from the join cardinality to the group cardinality. Diagnosing join problems requires understanding these cardinality invariants.

**Sciences (Bioinformatics — Database Integration)**: Bioinformatics databases frequently integrate datasets with mismatched identifiers — gene names that changed, protein accession numbers that were deprecated, organism taxonomy that was reclassified. A gene expression join on gene_name instead of gene_id produces the classic "wrong ON condition" problem: genes with ambiguous names (same symbol in different species) produce false cross-matches. Bioinformatics pipelines have been invalidated by exactly this error — joining on text identifiers instead of stable numeric IDs. The fix — always join on stable primary keys — is identical to the SQL troubleshooting advice: never join on mutable text values.

# Lore Conclusion

"Duplicate rows: one-to-many join, fixed with GROUP BY. Missing rows: INNER JOIN, fixed with LEFT JOIN. Row explosion: Cartesian product from a missing ON clause, fixed by adding the correct condition." Master Selvaris reviewed the corrected report. "Three problems. Three fixes. All predictable." She handed back the query to the Secretary. "Now you can diagnose join problems systematically. When a join query produces wrong results, you do not guess — you use COUNT diagnostics to identify the pattern, then apply the correct fix." She closed the session. "Join problems are common. But they are never mysterious. Every join failure has one of these three causes — find the cause, apply the fix."

---
