---
id: de-app-m4-03
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m4
moduleTitle: "Module 4: Joining Information"
moduleGlyph: "🔗"
moduleSortOrder: 4
topicSlug: understanding_joins
topicTitle: "Understanding Joins"
topicSortOrder: 1
lesson: left_join
title: "LEFT JOIN"
sortOrder: 3
difficulty: 2
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m4-02]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains that LEFT JOIN returns all rows from the left table regardless of matches
    - Identifies that unmatched right-table columns appear as NULL
    - Uses LEFT JOIN to find rows with no match ("find all X with no Y" pattern)
    - Distinguishes when to use LEFT JOIN vs INNER JOIN
    - Reflects on the IS NULL filter pattern to find unmatched rows
  keywords: [LEFT JOIN, all rows, NULL, unmatched, left table, right table, IS NULL, find, missing, outer]
  modelAnswer: |
    LEFT JOIN returns all rows from the left (first) table regardless of whether there is a matching row in the right (second) table. Where a match exists, right-table columns appear normally. Where no match exists, right-table columns are NULL. This makes LEFT JOIN essential for two patterns: showing all records with optional related data (all customers with their orders, if any), and finding records with no related data (customers who have never ordered, identified by WHERE right_table.id IS NULL). Choosing LEFT vs INNER JOIN depends on whether unmatched left-table rows are relevant to the question.
guidedSteps:
  - id: de-app-m4-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A `customers` table has 1,000 rows. An `orders` table has 5,000 rows. 200 customers have never placed an order. A LEFT JOIN on customer_id (customers on the left) returns how many rows?
    inputConfig:
      options:
        - "800 rows (only matched customers)"
        - "5,000 rows (all orders)"
        - "5,200 rows (all 5,000 orders plus 200 unmatched customers)"
        - "1,000 rows (all customers)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["5,200 rows (all 5,000 orders plus 200 unmatched customers)"]
      rejectedFeedback: "LEFT JOIN keeps ALL rows from the left table (customers). The 800 customers with orders produce 5,000 rows (one per order). The 200 customers with no orders each produce 1 row with NULL for all order columns. Total: 5,000 + 200 = 5,200 rows. The left table is always fully preserved; the right table contributes NULLs for non-matching rows."
    hint: "LEFT JOIN preserves every row from the left table — matched rows join normally, unmatched rows get NULLs for right-table columns."
    reflectionPrompt: "How would the result differ if orders were on the LEFT and customers on the RIGHT?"
  - id: de-app-m4-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To find customers who have NEVER placed an order using LEFT JOIN, you filter for rows where the order columns are: `WHERE o.order_id IS ________`
    inputConfig:
      placeholder: "NULL"
    markingRule:
      matchMode: CONTAINS
      accepted: [NULL, null]
      rejectedFeedback: "After a LEFT JOIN, customers with no orders will have NULL in all order columns (including order_id). WHERE o.order_id IS NULL filters to only these unmatched rows — giving you exactly the customers who have never ordered. This 'LEFT JOIN + IS NULL' pattern is the standard way to find rows in one table with no corresponding row in another."
    hint: "When there is no matching order, what value appears in the order columns for that customer row?"
    reflectionPrompt: "Could you use WHERE o.order_id = NULL instead? Why or why not?"
  - id: de-app-m4-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain when you would choose LEFT JOIN over INNER JOIN, using a concrete business example.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [all, include, no orders, no match, NULL, unmatched, missing, every, report, complete]
      rejectedFeedback: "Use LEFT JOIN when you need all rows from the left table even if they have no match. Example: a customer report should show ALL customers, even those with no purchases — an INNER JOIN would silently drop them. A product listing should show ALL products even those with no reviews. A staff report should show ALL employees even those assigned to no project. Whenever the question is 'show me everything, plus related data if it exists', LEFT JOIN is the right choice."
    hint: "Think of a report where you need to show all records in one table, and related data from another table is optional."
    reflectionPrompt: "What would be wrong with an INNER JOIN in a report that checks which customers haven't reordered in 90 days?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "After a LEFT JOIN of customers (left) and orders (right), what appears in the order columns for a customer with no orders?"
    options:
      - "0"
      - "An empty string"
      - "NULL"
      - "The previous row's values"
    correctIndex: 2
    feedback: "NULL is placed in all right-table columns for rows from the left table that have no match in the right table. This is how LEFT JOIN signals 'there is no corresponding record on the right side'. You can test for this with IS NULL in a WHERE clause to find unmatched rows."
  - type: MULTIPLE_CHOICE
    question: "Which query finds all products that have never been ordered?"
    options:
      - "SELECT p.name FROM products p INNER JOIN order_lines ol ON p.product_id = ol.product_id WHERE ol.product_id IS NULL;"
      - "SELECT p.name FROM products p LEFT JOIN order_lines ol ON p.product_id = ol.product_id WHERE ol.product_id IS NULL;"
      - "SELECT p.name FROM products p LEFT JOIN order_lines ol ON p.product_id = ol.product_id;"
      - "SELECT p.name FROM products p WHERE product_id NOT IN (SELECT product_id FROM order_lines);"
    correctIndex: 1
    feedback: "Option B is the standard LEFT JOIN + IS NULL pattern for 'no match'. Option A uses INNER JOIN — it excludes unmatched products entirely, so the WHERE IS NULL would never trigger. Option C returns all products but doesn't filter to unmatched ones. Option D (NOT IN subquery) also works but can be slow and behaves unexpectedly if order_lines contains NULL product_ids."
retrieval:
  recall: "Write a query listing all employees and their manager's name, where employees with no manager show NULL."
  explain: "Explain the LEFT JOIN + IS NULL pattern and what it is used for."
  mistakeId:
    code: "LEFT JOIN orders o ON c.customer_id = o.customer_id WHERE o.status = 'completed'"
    answer: "Adding a WHERE filter on the right table after a LEFT JOIN converts it back into an INNER JOIN. The WHERE o.status = 'completed' excludes customers with no orders (their o.status is NULL, which does not equal 'completed'). To filter the right table while preserving unmatched left rows, move the condition into the ON clause: LEFT JOIN orders o ON c.customer_id = o.customer_id AND o.status = 'completed'. This keeps unmatched customers with NULL order columns, but only joins orders with status = 'completed'."
---

# Hook

INNER JOIN only returns rows with a match on both sides. But many real questions require all rows from one table, whether or not they have a match: all customers (including those who never ordered), all products (including those never reviewed), all employees (including those assigned to no project).

LEFT JOIN is the solution. It preserves every row from the left table and fills in NULL where there is no match on the right.

# Lore Introduction

"The Membership Committee wants a list of all members," the Administrator said, "with their most recent transaction date — but including members who have never transacted, so we know who to follow up with." Master Selvaris changed INNER JOIN to LEFT JOIN. "Every member now appears," she said. "Members with transactions show their transaction data. Members without transactions show NULL in the transaction columns." The Administrator noticed the NULL rows. "So NULL means no activity?" Selvaris nodded. "NULL means no match. We can then filter WHERE transaction_id IS NULL to extract just the inactive members. Two queries from one join: the full list, and the follow-up list."

# Core Learning

## Concept Introduction

### LEFT JOIN Syntax

```sql
SELECT c.name, c.email, o.order_date, o.total_amount
FROM customers AS c
LEFT JOIN orders AS o ON c.customer_id = o.customer_id;
```

The `LEFT` table (customers) drives the result: every customer row appears. The `RIGHT` table (orders) contributes its columns when a match exists, or NULL when there is no match.

### LEFT JOIN vs INNER JOIN — Side by Side

```
customers table:              orders table:
customer_id | name            order_id | customer_id | amount
1           | Alice           101      | 1           | 50
2           | Bob             102      | 1           | 120
3           | Carol           103      | 2           | 75
4           | Dave

INNER JOIN result:            LEFT JOIN result:
name  | order_id | amount     name  | order_id | amount
Alice | 101      | 50         Alice | 101      | 50
Alice | 102      | 120        Alice | 102      | 120
Bob   | 103      | 75         Bob   | 103      | 75
                              Carol | NULL     | NULL   ← preserved with NULLs
                              Dave  | NULL     | NULL   ← preserved with NULLs
```

### Finding Rows with No Match ("Find all X with no Y")

The classic LEFT JOIN + IS NULL pattern:

```sql
-- Customers who have never placed an order
SELECT c.customer_id, c.name, c.email
FROM customers AS c
LEFT JOIN orders AS o ON c.customer_id = o.customer_id
WHERE o.order_id IS NULL;

-- Products that have never been ordered
SELECT p.product_id, p.name
FROM products AS p
LEFT JOIN order_lines AS ol ON p.product_id = ol.product_id
WHERE ol.line_id IS NULL;

-- Employees not assigned to any project
SELECT e.employee_id, e.name
FROM employees AS e
LEFT JOIN project_assignments AS pa ON e.employee_id = pa.employee_id
WHERE pa.assignment_id IS NULL;
```

### LEFT JOIN with Aggregation

```sql
-- All customers with their order count (0 for customers with no orders)
SELECT
    c.customer_id,
    c.name,
    COUNT(o.order_id) AS order_count,   -- COUNT(col) skips NULLs → 0 for no orders
    COALESCE(SUM(o.total_amount), 0) AS total_spent
FROM customers AS c
LEFT JOIN orders AS o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.name
ORDER BY total_spent DESC;
```

Note: `COUNT(o.order_id)` is used instead of `COUNT(*)` — `COUNT(*)` would count the NULL row as 1 for unmatched customers, but `COUNT(column)` skips NULLs, correctly returning 0.

### The WHERE Trap — Converting LEFT JOIN to INNER JOIN

```sql
-- WRONG: WHERE on right-table column converts LEFT JOIN to INNER JOIN
SELECT c.name, o.total_amount
FROM customers AS c
LEFT JOIN orders AS o ON c.customer_id = o.customer_id
WHERE o.status = 'completed';    ← excludes unmatched customers (their status is NULL)

-- CORRECT: filter in ON clause to preserve unmatched rows
SELECT c.name, o.total_amount
FROM customers AS c
LEFT JOIN orders AS o ON c.customer_id = o.customer_id
    AND o.status = 'completed';  ← unmatched customers still appear, order cols = NULL
```

## Why It Matters

LEFT JOIN exists for the questions INNER JOIN cannot ask — the ones about absence:

- "Customers with *no* orders", "products *never* sold", "students *missing* a grade" — finding what's absent drives retention, stock, and quality work
- Keeping every left-side row and showing NULL where nothing matches lets a single query show both the matched and the unmatched
- The classic trap: a WHERE condition on a right-table column silently turns your LEFT JOIN back into an INNER JOIN

Some of the most valuable business questions are about what *didn't* happen. LEFT JOIN is how SQL answers them.

## Common Mistakes

- **WHERE on right table column accidentally removing unmatched rows**: See above. Move right-table filters into the ON clause if you want to keep unmatched left rows.
- **Using COUNT(*) instead of COUNT(right_col) for zero-counts**: `COUNT(*)` returns 1 for unmatched rows (it counts the row itself); `COUNT(o.order_id)` returns 0 (NULLs are excluded from COUNT).
- **Confusing which table is "left"**: The left table is the one after FROM. The right table is the one after JOIN. The left table is always fully preserved.

## Mental Model

Imagine the left table as a complete class register. Every student must appear on the final report. The right table is assignment submissions. For students who submitted, their submission details are merged onto the report. For students who did not submit, their row still appears — with empty (NULL) cells where submission data would be. This is LEFT JOIN: the register drives the output, the submissions fill in optional details.

## Mini Summary

- ✔ `LEFT JOIN` preserves all rows from the left (FROM) table
- ✔ Right-table columns are NULL for rows with no match
- ✔ `LEFT JOIN ... WHERE right.col IS NULL` finds unmatched left rows
- ✔ Use `COUNT(right.col)` not `COUNT(*)` to get 0 for unmatched rows
- ✔ Beware: WHERE on right-table columns converts LEFT JOIN to INNER JOIN

# Guided Practice Quest

Work through the guided steps to write a LEFT JOIN query, identify NULL rows in the result, use the IS NULL pattern to find unmatched rows, and combine LEFT JOIN with COUNT using the right column.

# Solo Practice Quest

Using `students` (student_id, name, enrollment_date), `submissions` (submission_id, student_id, assignment_id, submitted_at, grade), and `assignments` (assignment_id, title, due_date): write five queries: (1) all students with the number of submissions each has made (0 for students with none), (2) students who have submitted nothing at all, (3) all assignments with the number of students who submitted, including assignments with no submissions, (4) students who missed a specific assignment (student submitted nothing for assignment_id = 5), (5) a complete grade report: all students × all assignments, with grade or NULL if not submitted. For each, explain which table should be on the left and why.

# Integration

**Mathematics**: LEFT JOIN computes the left outer join in relational algebra: R ⟕ S. For each tuple r ∈ R, if there exists s ∈ S such that r[A] = s[A], then (r, s) appears in the result. If no such s exists, then (r, null) appears — where null extends the tuple with null values for all attributes in S's schema that are not in R. This is a total function from R to result tuples: every element of R maps to at least one result tuple. Compare to INNER JOIN, which is a partial function — elements of R with no match produce no result tuple. The left outer join guarantees surjectivity on R.

**Sciences (Environmental Science)**: Environmental monitoring stations report temperature, air quality, and rainfall data. A station database joins monitoring_stations (left) to daily_readings (right): stations with no readings appear as NULL in the readings table. `SELECT s.station_id, s.location, r.temperature, r.reading_date FROM monitoring_stations s LEFT JOIN daily_readings r ON s.station_id = r.station_id WHERE r.reading_id IS NULL` — finding offline or failed stations. This exact pattern — "find all X with no corresponding Y" — is used in environmental science to detect data gaps, equipment failures, and unreported events. The LEFT JOIN IS NULL idiom is fundamental to data quality monitoring.

# Lore Conclusion

The Membership Committee received their list: 847 active members with transaction history, 153 members with no transactions at all. "LEFT JOIN showed all 1,000 members," Master Selvaris said. "INNER JOIN would have shown only 847." She handed over the filtered list. "The 153 with NULL transaction columns are your follow-up list. One join, two uses: the full picture and the targeted action list." She paused. "This is the most important practical difference between INNER and LEFT JOIN. INNER JOIN is for 'show me matched data'. LEFT JOIN is for 'show me everything, whether matched or not'. Choose deliberately. The wrong choice can make real data invisible."

---
