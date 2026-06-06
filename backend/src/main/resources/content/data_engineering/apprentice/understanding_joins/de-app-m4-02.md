---
id: de-app-m4-02
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
lesson: inner_join
title: "INNER JOIN"
sortOrder: 2
difficulty: 2
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m4-01]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Writes a correct INNER JOIN query with ON condition
    - Explains that INNER JOIN only returns rows with a match in both tables
    - Identifies which rows are excluded (rows with no match in either table)
    - Uses table aliases to qualify column names from each table
    - Reflects on when INNER JOIN is appropriate vs when it might silently exclude data
  keywords: [INNER JOIN, match, both tables, exclude, ON, alias, qualify, rows, intersection]
  modelAnswer: |
    INNER JOIN returns only rows where the ON condition is true in both tables — rows with no match in either table are excluded. If a customer has no orders, they do not appear in the result. If an order has a customer_id that does not exist in customers, it does not appear either. INNER JOIN is the most common join type, appropriate when you only care about records that exist on both sides. The risk is silent exclusion: rows without matches disappear without error, which can mislead if you expect all records to appear.
guidedSteps:
  - id: de-app-m4-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A `customers` table has 1,000 rows. An `orders` table has 5,000 rows. 200 customers have never placed an order. An INNER JOIN on customer_id returns how many customer rows?
    inputConfig:
      options:
        - "1,000 (all customers)"
        - "800 (only customers who have placed orders)"
        - "5,000 (all orders)"
        - "5,200 (all customers plus all orders)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["800 (only customers who have placed orders)"]
      rejectedFeedback: "INNER JOIN returns only rows with matches on both sides. The 200 customers with no orders have no matching row in orders — so they are excluded. Only the 800 customers who have at least one order appear. The result has one row per (customer, order) match — so it could have up to 5,000 rows, but each row represents one customer-order pair."
    hint: "INNER JOIN requires a match on BOTH sides. Customers with no orders have no match in the orders table."
    reflectionPrompt: "If you need to see all customers — even those with no orders — which join type would you use?"
  - id: de-app-m4-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the ON clause: `SELECT c.name, o.order_date FROM customers AS c INNER JOIN orders AS o ON c.________ = o.customer_id;`
    inputConfig:
      placeholder: "customer_id"
    markingRule:
      matchMode: CONTAINS
      accepted: [customer_id]
      rejectedFeedback: "ON c.customer_id = o.customer_id matches rows where the primary key in customers (c.customer_id) equals the foreign key in orders (o.customer_id). This is the standard join pattern: PK in the parent table = FK in the child table. The aliases c and o are used to qualify column names, which is essential when both tables have a column with the same name."
    hint: "The primary key in the customers table that the orders table references as a foreign key."
    reflectionPrompt: "Why must you write c.customer_id rather than just customer_id in this query?"
  - id: de-app-m4-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain a scenario where INNER JOIN would silently give you incomplete results because of missing matches.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [missing, NULL, orphan, no match, exclude, incomplete, silent, wrong, absent]
      rejectedFeedback: "Example: A report counting 'orders per customer' using INNER JOIN will silently omit customers who have never ordered — they simply don't appear in the result. If the report is used to identify all customers, the 200 with no orders are invisible. Another example: if some orders have a customer_id pointing to a deleted customer (orphaned foreign key), those orders also disappear from an INNER JOIN result, making totals look lower than they actually are."
    hint: "Think about rows that exist on one side but have no match on the other — where does INNER JOIN send them?"
    reflectionPrompt: "How would you detect that INNER JOIN is silently excluding rows in a real-world report?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which best describes what INNER JOIN returns?"
    options:
      - "All rows from both tables, with NULLs where there is no match"
      - "All rows from the left table, with NULLs for unmatched right table columns"
      - "Only rows where the ON condition is true in both tables"
      - "Only rows from the right table"
    correctIndex: 2
    feedback: "INNER JOIN returns the intersection — only rows that have a matching row in both tables according to the ON condition. Rows with no match on either side are excluded. This is the most common join type and appropriate when you only care about records that exist in both tables."
  - type: MULTIPLE_CHOICE
    question: "Which query correctly retrieves each order with the customer's name?"
    options:
      - "SELECT name, order_date FROM orders, customers;"
      - "SELECT c.name, o.order_date FROM customers c INNER JOIN orders o ON c.customer_id = o.customer_id;"
      - "SELECT c.name, o.order_date FROM customers c INNER JOIN orders o WHERE c.customer_id = o.customer_id;"
      - "SELECT name, order_date FROM customers INNER JOIN orders;"
    correctIndex: 1
    feedback: "Option B is correct: aliases c and o, INNER JOIN with ON specifying the linking condition. Option A is a Cartesian join (no ON) — produces every customer matched with every order. Option C uses WHERE instead of ON — this works in practice for inner joins but is non-standard style; ON is the correct clause for join conditions. Option D has no ON clause."
retrieval:
  recall: "Write a query that returns all orders with the customer's name and email address."
  explain: "Explain why INNER JOIN can produce fewer rows than you expect, and how to detect this."
  mistakeId:
    code: "SELECT name, order_date FROM customers, orders"
    answer: "Listing two tables in FROM without a JOIN and ON condition produces a Cartesian product — every customer matched with every order. With 1,000 customers and 5,000 orders, this returns 5,000,000 rows. Always use explicit JOIN ... ON syntax to specify how tables are linked. The comma syntax (implicit cross join) is a legacy pattern and should not be used."
---

# Hook

INNER JOIN is the most common join in SQL. It answers the most common join question: "give me rows that exist in both tables." Orders with their customers. Products with their categories. Employees with their departments.

If a row has no match on the other side, it is excluded. This is often exactly what you want — but understanding when it might silently exclude data you care about is essential.

# Lore Introduction

"Show me all transactions with member names," the Archivist said. "Only transactions that have a matching member record." Master Selvaris wrote the INNER JOIN. "The word INNER means intersection," she said. "Only rows with a match on both sides. The forty transactions with no matching member ID — bad data from the migration — are excluded." The Archivist frowned. "Silently?" Selvaris nodded. "Silently. INNER JOIN does not warn you about excluded rows. That is its power and its risk. You get clean, matched data — but you must know to look for the missing pieces separately." She added a COUNT. "5,000 transactions in the table. 4,960 returned by the join. 40 orphaned rows. Now you know."

# Core Learning

## Concept Introduction

### INNER JOIN Syntax

```sql
SELECT c.name, c.email, o.order_date, o.total_amount
FROM orders AS o
INNER JOIN customers AS c ON o.customer_id = c.customer_id;
```

- `INNER JOIN` — the join type (INNER is optional; `JOIN` alone means INNER JOIN)
- `customers AS c` — bring in the customers table, aliased as c
- `ON o.customer_id = c.customer_id` — the matching condition

### What INNER JOIN Includes and Excludes

```
customers table:          orders table:
customer_id | name        order_id | customer_id | amount
1           | Alice       101      | 1           | 50
2           | Bob         102      | 1           | 120
3           | Carol       103      | 2           | 75
4           | Dave        104      | 9           | 30  ← customer_id 9 doesn't exist

INNER JOIN result (ON customers.customer_id = orders.customer_id):
name  | order_id | amount
Alice | 101      | 50
Alice | 102      | 120
Bob   | 103      | 75
← Carol excluded: no orders
← Dave excluded: no orders
← order 104 excluded: customer_id 9 has no matching customer
```

### Selecting Columns from Both Tables

```sql
-- Select columns from both tables — qualify with alias
SELECT
    c.customer_id,
    c.name          AS customer_name,
    c.email,
    o.order_id,
    o.order_date,
    o.total_amount
FROM orders AS o
INNER JOIN customers AS c ON o.customer_id = c.customer_id
ORDER BY c.name, o.order_date;
```

### INNER JOIN with WHERE and Aggregation

```sql
-- All orders for UK customers
SELECT c.name, o.order_id, o.total_amount
FROM orders AS o
INNER JOIN customers AS c ON o.customer_id = c.customer_id
WHERE c.country = 'UK'
ORDER BY o.total_amount DESC;

-- Total revenue per customer (only customers with orders)
SELECT c.customer_id, c.name, COUNT(o.order_id) AS orders, SUM(o.total_amount) AS revenue
FROM customers AS c
INNER JOIN orders AS o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.name
ORDER BY revenue DESC;
```

### Joining Three Tables

```sql
-- Order lines with order info and product name
SELECT
    o.order_id,
    o.order_date,
    c.name          AS customer_name,
    p.name          AS product_name,
    ol.quantity,
    ol.unit_price
FROM order_lines AS ol
INNER JOIN orders AS o ON ol.order_id = o.order_id
INNER JOIN customers AS c ON o.customer_id = c.customer_id
INNER JOIN products AS p ON ol.product_id = p.product_id;
```

Each `INNER JOIN` adds a table and its linking condition.

### USING Shorthand (when column names match)

```sql
-- When both tables have the same FK column name, USING is a shorthand
SELECT c.name, o.order_date
FROM orders AS o
INNER JOIN customers AS c USING (customer_id);
-- Equivalent to ON o.customer_id = c.customer_id
```

## Common Mistakes

- **Missing ON clause**: `JOIN customers` without ON produces a Cartesian product. Always include ON.
- **Ambiguous column names**: `SELECT customer_id` is ambiguous if both tables have it. Use `c.customer_id` or `o.customer_id`.
- **Silently missing rows**: INNER JOIN excludes unmatched rows without error. Always verify result counts if completeness matters.
- **Joining on the wrong column**: `ON o.order_id = c.customer_id` will produce 0 or wrong matches. The ON condition must link semantically related columns.

## Mental Model

Imagine two printed lists: one of customers, one of orders. INNER JOIN is a manual matching task: for each order, find the customer with the matching ID and draw a line between them. Write out the combined details for each matched pair. Any customer with no order lines gets no entry. Any order with an ID not on the customer list gets no entry. Only matched pairs produce output rows.

## Mini Summary

- ✔ `INNER JOIN` returns only rows with a match in both tables
- ✔ Rows without a match on either side are silently excluded
- ✔ `JOIN` alone (without INNER) means INNER JOIN
- ✔ Always include the `ON` clause — without it, you get a Cartesian product
- ✔ Use table aliases to qualify column names from each table

# Guided Practice Quest

Work through the guided steps to write INNER JOIN queries, verify which rows are included and excluded, and combine INNER JOIN with WHERE and GROUP BY.

# Solo Practice Quest

Using three tables — `employees` (employee_id, name, department_id, salary), `departments` (department_id, name, location), `projects` (project_id, employee_id, project_name, hours_worked) — write five queries: (1) all employees with their department name, (2) all employees who have been assigned to a project (with project name and hours), (3) total hours worked per department, (4) employees working on more than one project, (5) the top 3 projects by hours worked with the employee's name and department name. For each, explain which rows would be excluded by INNER JOIN and whether that exclusion is appropriate.

# Integration

**Mathematics**: INNER JOIN computes the equi-join, a restricted form of the relational algebra join R ⋈_θ S where predicate θ is equality on a common attribute. This is the intersection of the Cartesian product with the predicate: INNER JOIN = σ_{R.A = S.A}(R × S). The result relation has schema that is the union of R's and S's schemas (with the join attribute appearing once). In set theory, this corresponds to finding pairs (r, s) ∈ R × S that satisfy a specific predicate — a relation-valued intersection operation. The cardinality of the result is bounded: |R ⋈ S| ≤ min(|R|, |S|) × max_matches, where max_matches is the maximum number of matching rows for any single key value.

**Sciences (Chemistry — Reaction Matching)**: Chemical databases store reactants and products in separate tables linked by reaction ID, analogous to the customer-order relationship. A reaction database join: `SELECT r.name AS reactant, p.name AS product, rx.conditions FROM reactions rx INNER JOIN reactants r ON rx.reactant_id = r.compound_id INNER JOIN products p ON rx.product_id = p.compound_id` retrieves complete reaction information. Reactions with unknown products (NULL product_id) are excluded by INNER JOIN — exactly the desired behaviour for complete reaction records. This three-table INNER JOIN pattern is fundamental in cheminformatics, genomics, and any domain with normalised relational data.

# Lore Conclusion

"4,960 matched transactions," the Archivist confirmed. "And 40 orphaned records — transactions referencing member IDs that no longer exist." Master Selvaris had already written the second query to find them. "INNER JOIN showed you the clean data. A separate query will show you the orphans. Together they give you a complete picture." She closed the results. "INNER JOIN is the foundation. It is the right choice whenever you only care about complete, matched records. But always ask yourself: what am I not seeing? The rows INNER JOIN excluded might be the ones with the problem."

---
