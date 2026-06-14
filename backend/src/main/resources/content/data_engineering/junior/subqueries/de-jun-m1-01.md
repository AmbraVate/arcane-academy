---
id: de-jun-m1-01
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m1
moduleTitle: "Module 1: Advanced SQL"
moduleGlyph: "⚡"
moduleSortOrder: 1
topicSlug: subqueries
topicTitle: "Subqueries"
topicSortOrder: 1
lesson: subqueries
title: "Subqueries"
sortOrder: 1
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m7-04]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what a subquery is and where it can appear in a SQL statement
    - Distinguishes scalar, row, table, and correlated subqueries
    - Identifies when a correlated subquery re-executes per outer row
    - Rewrites a correlated subquery as a JOIN where appropriate
    - Explains the EXISTS operator and why it is more efficient than IN for large sets
  keywords: [subquery, scalar, correlated, EXISTS, IN, derived table, nested, outer, inner, performance, rewrite, JOIN]
  modelAnswer: |
    A subquery is a SELECT statement nested inside another SQL statement. It can appear in SELECT (scalar), FROM (derived table/inline view), WHERE (filter), or HAVING. A scalar subquery returns exactly one value. A correlated subquery references columns from the outer query and re-executes once per outer row — powerful but potentially slow. EXISTS is preferred over IN when checking membership against large sets because EXISTS short-circuits on the first match. Many correlated subqueries can be rewritten as JOINs for better performance, though correlated subqueries are sometimes more readable for per-row computations.
guidedSteps:
  - id: de-jun-m1-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which type of subquery re-executes once for every row in the outer query?
    inputConfig:
      options:
        - "Scalar subquery — returns a single value used in SELECT"
        - "Derived table subquery — appears in the FROM clause"
        - "Correlated subquery — references a column from the outer query"
        - "IN subquery — returns a list of values for membership testing"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Correlated subquery — references a column from the outer query"]
      rejectedFeedback: "A correlated subquery references a column from the outer query (e.g., WHERE inner.id = outer.id). Because it depends on the current outer row, the database must re-execute it once for each row in the outer result set. On a table with 1,000,000 rows, the correlated subquery may execute 1,000,000 times. This makes correlated subqueries potentially expensive — they should be used only where a JOIN cannot achieve the same result, or where the correlated nature is required (e.g., row-by-row maximum)."
    hint: "Which subquery has a reference to the outer query inside it?"
    reflectionPrompt: "When would you choose a correlated subquery over a JOIN?"
  - id: de-jun-m1-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A subquery that appears in the FROM clause and is treated as a temporary table is called a ________ table.
    inputConfig:
      placeholder: "derived"
    markingRule:
      matchMode: CONTAINS
      accepted: [derived, "derived table", "inline view", "subquery in FROM"]
      rejectedFeedback: "A subquery in the FROM clause is called a derived table (or inline view). It is evaluated first and its result is treated as a temporary table that the outer query can SELECT from, JOIN to, or filter. Example: SELECT d.dept_name, d.avg_salary FROM (SELECT department_id, AVG(salary) AS avg_salary FROM employees GROUP BY department_id) AS d JOIN departments ON d.department_id = departments.id. The subquery alias (here 'd') is mandatory — most databases require naming derived tables."
    hint: "A subquery in FROM — it acts like a table but is computed on the fly."
    reflectionPrompt: "Why must a derived table subquery always have an alias?"
  - id: de-jun-m1-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain the difference between WHERE id IN (SELECT ...) and WHERE EXISTS (SELECT ...), and when EXISTS is preferred.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [short-circuit, first match, large, list, NULL, EXISTS, performance, entire list, materialise]
      rejectedFeedback: "IN evaluates the entire subquery result set and checks membership — this materialises all matching IDs into memory. EXISTS stops as soon as it finds the first matching row (short-circuit evaluation). For large subquery result sets, EXISTS is more efficient because it does not need to build the full list. EXISTS also handles NULLs more predictably: IN (SELECT ...) returns no rows if any NULL exists in the subquery result (because NULL IN (1, NULL, 3) is NULL, not TRUE), while EXISTS is unaffected by NULLs in non-key columns."
    hint: "Think about what happens when the subquery finds its first match — does IN or EXISTS stop early?"
    reflectionPrompt: "Write both an IN and an EXISTS version of the same query, then explain which you would use and why."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A scalar subquery in the SELECT clause returns more than one row. What happens?"
    options:
      - "The query returns the first row only"
      - "The query fails with an error — scalar subquery must return exactly one row"
      - "The query returns multiple result rows automatically"
      - "NULL is returned for extra rows"
    correctIndex: 1
    feedback: "A scalar subquery (one used in the SELECT list or WHERE comparison operators like =, >, <) must return exactly one row and one column. If it returns more than one row, the database raises a runtime error: 'subquery returns more than one row'. Use aggregate functions (MAX, MIN, AVG) or LIMIT 1 to guarantee a single row when the subquery result is not naturally scalar."
  - type: MULTIPLE_CHOICE
    question: "Which of these is a correlated subquery?"
    options:
      - "SELECT * FROM orders WHERE total > (SELECT AVG(total) FROM orders)"
      - "SELECT * FROM orders o WHERE o.total > (SELECT AVG(total) FROM orders WHERE customer_id = o.customer_id)"
      - "SELECT * FROM (SELECT customer_id, COUNT(*) FROM orders GROUP BY 1) AS t"
      - "SELECT * FROM orders WHERE customer_id IN (SELECT customer_id FROM vip_customers)"
    correctIndex: 1
    feedback: "The second example is correlated: the inner subquery references o.customer_id from the outer query alias. The inner query calculates the average order total for that specific customer — a value that changes for every outer row. The first is a non-correlated subquery (the inner query references no outer columns). The third is a derived table. The fourth is an IN subquery — non-correlated because the inner query stands alone."
retrieval:
  recall: "Write a query using a correlated subquery that returns each customer alongside their highest order total."
  explain: "Explain the four positions where a subquery can appear in a SELECT statement and what each position is typically used for."
  mistakeId:
    code: "SELECT name FROM customers WHERE id IN (SELECT customer_id FROM orders WHERE status = 'cancelled')"
    answer: "This query is correct syntactically but has a subtle risk: if the subquery ever returns a NULL (e.g. a row where customer_id is NULL), the IN operator will return no rows even for IDs that should match, because NULL IN (1, NULL, 3) evaluates to NULL not TRUE. The safer rewrite uses EXISTS: SELECT name FROM customers c WHERE EXISTS (SELECT 1 FROM orders o WHERE o.customer_id = c.id AND o.status = 'cancelled'). EXISTS is unaffected by NULLs in the subquery and typically performs better on large datasets due to short-circuit evaluation."
---

# Hook

SQL's power multiplies when queries become composable — when one query's result can feed directly into another. Subqueries allow you to embed a SELECT inside another SQL statement, enabling per-row calculations, existence checks, and multi-step transformations that a single flat query cannot express.

This lesson covers the four placements and three types of subquery that form the foundation of advanced SQL.

# Lore Introduction

"The Archive is growing," the Senior Archivist said, reviewing the expanding catalogue. "We need to find members who have borrowed items more expensive than the average borrowing cost for their tier." The new Junior Engineer pulled up the editor. "That needs a subquery — the average is per tier, and I need to compare each member against their own tier's average, not the overall average." The Senior Archivist nodded. "Exactly. The subquery references the outer query's tier. That's a correlated subquery — it runs once per member." The Junior wrote the query. "Slower than a JOIN, but sometimes the most readable way to express a row-by-row calculation." The Senior Archivist reviewed it. "Understanding when to use each form — that's what separates a query writer from a query engineer."

# Core Learning

## Concept Introduction

### What is a Subquery?

A subquery (nested query / inner query) is a SELECT statement embedded inside another SQL statement. The outer query uses the subquery's result.

```sql
-- Example: orders above the average order value
SELECT order_id, total_amount
FROM orders
WHERE total_amount > (SELECT AVG(total_amount) FROM orders);
--                    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
--                    This is the subquery (inner query)
```

### The Four Positions

#### 1. Subquery in WHERE (Filter Subquery)

```sql
-- Find customers who have placed at least one cancelled order
SELECT customer_id, name
FROM customers
WHERE customer_id IN (
    SELECT DISTINCT customer_id
    FROM orders
    WHERE status = 'cancelled'
);
```

#### 2. Subquery in SELECT (Scalar Subquery)

```sql
-- Show each customer's name alongside their total number of orders
SELECT
    c.name,
    (SELECT COUNT(*) FROM orders o WHERE o.customer_id = c.customer_id) AS order_count
FROM customers c;
-- This is a correlated scalar subquery — runs once per customer row
```

#### 3. Subquery in FROM (Derived Table / Inline View)

```sql
-- Aggregate first, then filter on the aggregated result
SELECT dept_name, avg_salary
FROM (
    SELECT department_id, AVG(salary) AS avg_salary
    FROM employees
    GROUP BY department_id
) AS dept_averages    -- alias is required
JOIN departments d ON dept_averages.department_id = d.department_id
WHERE avg_salary > 50000;
```

#### 4. Subquery in HAVING

```sql
-- Departments where average salary exceeds the company-wide average
SELECT department_id, AVG(salary) AS avg_salary
FROM employees
GROUP BY department_id
HAVING AVG(salary) > (SELECT AVG(salary) FROM employees);
```

### Non-Correlated vs Correlated Subqueries

**Non-correlated**: the inner query can run independently of the outer query. It runs once and its result is reused.

```sql
-- Non-correlated: inner query runs once
SELECT * FROM orders
WHERE total_amount > (SELECT AVG(total_amount) FROM orders);
--                    inner query has no reference to outer table
```

**Correlated**: the inner query references a column from the outer query. It runs once per outer row.

```sql
-- Correlated: inner query runs for each customer
SELECT c.name
FROM customers c
WHERE EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.customer_id   -- reference to outer alias c
      AND o.total_amount > 1000
);
```

### EXISTS vs IN

```sql
-- IN version: materialises the full list of customer_ids
SELECT name FROM customers
WHERE customer_id IN (SELECT customer_id FROM orders WHERE status = 'completed');

-- EXISTS version: short-circuits on first match
SELECT name FROM customers c
WHERE EXISTS (
    SELECT 1 FROM orders o
    WHERE o.customer_id = c.customer_id
      AND o.status = 'completed'
);
```

| | IN | EXISTS |
|---|---|---|
| Evaluates | All matching rows | Stops at first match |
| NULL handling | Problematic (IN (1,NULL,3) → NULL) | Safe |
| Best for | Small subquery result sets | Large or correlated checks |
| Correlated | Can be | Always is |

### Rewriting Correlated Subqueries as JOINs

Many correlated subqueries in WHERE can be rewritten as JOINs — typically faster:

```sql
-- Correlated subquery version
SELECT c.name
FROM customers c
WHERE (
    SELECT COUNT(*) FROM orders o
    WHERE o.customer_id = c.customer_id
) > 3;

-- JOIN version (usually faster — single pass)
SELECT c.name
FROM customers c
JOIN (
    SELECT customer_id, COUNT(*) AS order_count
    FROM orders
    GROUP BY customer_id
) AS order_counts ON order_counts.customer_id = c.customer_id
WHERE order_counts.order_count > 3;
```

The second version pre-aggregates orders once, then joins — far more efficient on large tables.

## Why It Matters

Subqueries are SQL's way of asking layered questions — "above average", "the latest per group", "those who never" — in a single statement:

- Filtering against a computed value (everyone earning above the mean) is impossible in one flat query
- The correlated/uncorrelated distinction is also a performance lesson: one runs once, the other runs per row
- EXISTS and IN subqueries express membership tests that reports and data-quality checks use daily

Subqueries are also the gateway to reading other people's SQL — production queries nest, and you need to unpick them from the inside out.

## Common Mistakes

- **Scalar subquery returning multiple rows**: A subquery used with `=` must return exactly one row. Use `MAX()`, `MIN()`, or `LIMIT 1` to guarantee this.
- **Forgetting the alias on derived tables**: Every subquery in FROM must have an alias — most databases enforce this.
- **Using IN with NULLs in the subquery**: If the subquery result contains any NULL, IN returns NULL (not TRUE or FALSE) for non-matching values, silently filtering out rows.
- **Correlated subquery for large datasets**: A correlated subquery on a 1M-row outer table executes the inner query 1M times. Rewrite as a JOIN or CTE when performance matters.

## Mental Model

Think of a subquery as a question you need to answer before you can answer the main question. "Which products were ordered by high-value customers?" requires first knowing which customers are high-value. The subquery answers the preliminary question; the outer query uses that answer. Correlated subqueries answer the preliminary question differently for every row: "Is this customer a high-value customer? What about this one?" Each outer row gets its own inner answer.

## Mini Summary

- ✔ Subqueries can appear in WHERE, SELECT, FROM, and HAVING
- ✔ Non-correlated: runs once; correlated: runs once per outer row
- ✔ Derived tables (subquery in FROM) require an alias
- ✔ EXISTS is safer and faster than IN for large or correlated checks
- ✔ Most correlated subqueries can be rewritten as JOINs for better performance

# Guided Practice Quest

Work through the guided steps to identify correlated vs non-correlated subqueries, write an EXISTS check, and rewrite a correlated subquery as a JOIN.

# Solo Practice Quest

Using a database with customers, orders, order_lines, and products tables: (1) write a query using a scalar subquery in SELECT that shows each product name alongside the total quantity ever ordered; (2) write a query using a derived table (subquery in FROM) to find the top 3 customers by revenue in each region; (3) rewrite the same query as a CTE (you will cover CTEs in the next lesson — attempt this now for discussion); (4) write an EXISTS query to find all customers who have never placed an order; (5) identify which of your queries uses a correlated subquery and explain how many times the inner query executes. Annotate each query with a comment explaining the subquery type and why you chose that approach.

# Integration

**Mathematics**: Subqueries correspond to function composition in mathematics: f(g(x)) where g is evaluated first and its result is passed to f. A scalar subquery in SELECT is analogous to a function applied to each element of a set: for each customer c, compute order_count(c). The correlated subquery is a parameterised function evaluated for each element. Non-correlated subqueries in WHERE correspond to set operations: {x ∈ customers | customer_id(x) ∈ result(inner_query)} — a set-builder notation where the inner query defines the comparison set. EXISTS corresponds to the existential quantifier ∃: SELECT c FROM customers c WHERE EXISTS (subquery) is formalised as {c | ∃o ∈ orders : o.customer_id = c.id ∧ condition(o)}.

**Sciences (Bioinformatics)**: Correlated subqueries appear frequently in bioinformatics databases. A common pattern is finding sequences that exceed the average quality score for their sample batch — exactly a correlated subquery (average computed per batch, compared per sequence). The NCBI database uses nested queries extensively: finding genes expressed above the average expression level in a given tissue, finding patients with a biomarker level exceeding the reference range for their age group. The SQL patterns (correlated subquery, EXISTS for membership checks) are standard in genomics and clinical data analysis — the same constructs learned here apply directly to biological data at scale.

# Lore Conclusion

The query ran. "Fourteen members have borrowed items more expensive than the average for their tier," the Junior Engineer reported. "The correlated subquery calculates each tier's average separately — exactly what was needed." The Senior Archivist reviewed the execution plan. "Two hundred milliseconds on this dataset. If the Archive grows to a million members, this will be slow. Remember the rewrite as a JOIN." She marked the query for future optimisation. "You've mastered the first form of query composition. In the next lesson, we will see a cleaner way to express these multi-step queries: Common Table Expressions. Same power, far more readable."

---
