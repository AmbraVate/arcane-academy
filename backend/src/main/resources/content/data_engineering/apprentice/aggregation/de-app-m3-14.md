---
id: de-app-m3-14
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m3
moduleTitle: "Module 3: SQL Foundations"
moduleGlyph: "🔍"
moduleSortOrder: 3
topicSlug: aggregation
topicTitle: "Aggregation"
topicSortOrder: 3
lesson: group_by
title: "GROUP BY"
sortOrder: 14
difficulty: 2
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m3-13]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what GROUP BY does and why it is needed with aggregate functions
    - Writes a query with GROUP BY on a single column
    - Writes a query with GROUP BY on multiple columns
    - Explains the rule that non-aggregate SELECT columns must appear in GROUP BY
    - Describes the logical execution order of FROM → WHERE → GROUP BY → SELECT
  keywords: [GROUP BY, aggregate, partition, group, non-aggregate, SELECT, WHERE, execution order, per group, breakdown]
  modelAnswer: |
    GROUP BY partitions the rows of a table into groups based on one or more columns, then applies aggregate functions (COUNT, SUM, AVG, MIN, MAX) within each group independently. Without GROUP BY, aggregates produce a single value for the entire table. Every non-aggregate column in the SELECT list must appear in the GROUP BY clause (or be functionally dependent on GROUP BY columns). The logical execution order is FROM → WHERE (filters rows before grouping) → GROUP BY (partitions remaining rows) → SELECT (computes aggregates per group). Multiple GROUP BY columns create one group per unique combination of values.
guidedSteps:
  - id: de-app-m3-14-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which query is valid SQL and returns the number of orders per customer?
    inputConfig:
      options:
        - "SELECT customer_id, COUNT(*) FROM orders;"
        - "SELECT customer_id, COUNT(*) FROM orders GROUP BY customer_id;"
        - "SELECT COUNT(*) FROM orders WHERE customer_id GROUP BY customer_id;"
        - "SELECT customer_id FROM orders GROUP BY COUNT(*);"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SELECT customer_id, COUNT(*) FROM orders GROUP BY customer_id;"]
      rejectedFeedback: "Option A is invalid in standard SQL — you cannot mix a non-aggregate column (customer_id) with an aggregate (COUNT(*)) without GROUP BY. Option B is correct: GROUP BY customer_id groups rows by customer, then COUNT(*) counts within each group. Option C has WHERE customer_id which is not a valid condition. Option D has GROUP BY inside the wrong clause."
    hint: "Any non-aggregate column in SELECT must appear in GROUP BY."
    reflectionPrompt: "What would happen if you removed GROUP BY from the correct query — what error would you expect?"
  - id: de-app-m3-14-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To count orders per status per year, you use two GROUP BY columns: `SELECT status, EXTRACT(YEAR FROM order_date) AS year, COUNT(*) FROM orders GROUP BY status, ________(order_date);`
    inputConfig:
      placeholder: "EXTRACT(YEAR FROM"
    markingRule:
      matchMode: CONTAINS
      accepted: ["EXTRACT(YEAR FROM", "EXTRACT(YEAR FROM order_date)"]
      rejectedFeedback: "When using an expression like EXTRACT(YEAR FROM order_date) in SELECT, the GROUP BY clause must repeat the same expression (or the alias, in databases that support alias in GROUP BY). GROUP BY status, EXTRACT(YEAR FROM order_date) creates one group per unique (status, year) combination — so you get counts like: ('pending', 2025) = 312, ('completed', 2025) = 4,200, ('pending', 2026) = 89."
    hint: "The GROUP BY clause must contain the same expression you used in the SELECT list."
    reflectionPrompt: "How many result rows would you expect if there are 4 statuses and 3 years of data?"
  - id: de-app-m3-14-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why WHERE filters rows BEFORE GROUP BY, and give an example of when this distinction matters.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [WHERE, GROUP BY, before, after, filter, rows, groups, execution, order, aggregate]
      rejectedFeedback: "WHERE runs before GROUP BY in the logical execution order. This means WHERE can filter out individual rows before they are grouped and aggregated. Example: WHERE status = 'completed' before GROUP BY customer_id means only completed-order rows are grouped — cancelled orders are excluded before the per-customer count is computed. If you want to filter on aggregate results (e.g. only customers with more than 10 orders), you cannot use WHERE — you need HAVING, which runs after GROUP BY."
    hint: "Think about the execution order: FROM → WHERE → GROUP BY. Filtering happens before grouping."
    reflectionPrompt: "How would you count only completed orders per customer — where would the status filter go?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A query has: SELECT region, category, SUM(revenue). What must the GROUP BY clause contain?"
    options:
      - "GROUP BY SUM(revenue)"
      - "GROUP BY region"
      - "GROUP BY region, category"
      - "GROUP BY revenue"
    correctIndex: 2
    feedback: "Every non-aggregate column in the SELECT list (region and category) must appear in GROUP BY. SUM(revenue) is the aggregate — it does not go in GROUP BY. GROUP BY region alone would be invalid because category is also in SELECT without being aggregated. GROUP BY revenue is wrong — revenue is the aggregated column, not a grouping column."
  - type: MULTIPLE_CHOICE
    question: "In what order do FROM, WHERE, GROUP BY, and SELECT execute logically?"
    options:
      - "SELECT → FROM → WHERE → GROUP BY"
      - "FROM → GROUP BY → WHERE → SELECT"
      - "FROM → WHERE → GROUP BY → SELECT"
      - "WHERE → FROM → SELECT → GROUP BY"
    correctIndex: 2
    feedback: "The logical execution order is: FROM (identify the table), WHERE (filter individual rows), GROUP BY (partition filtered rows into groups), SELECT (compute aggregates and select columns per group). This order explains why WHERE cannot reference column aliases defined in SELECT, and why WHERE cannot filter on aggregate results — those don't exist yet when WHERE runs."
retrieval:
  recall: "Write a query showing total revenue and order count per region per year, ordered by year then revenue."
  explain: "Explain the rule about which columns can appear in SELECT when using GROUP BY."
  mistakeId:
    code: "SELECT department, name, MAX(salary) FROM employees GROUP BY department"
    answer: "The name column is in SELECT but not in GROUP BY and is not an aggregate. This is invalid in standard SQL — you cannot select a column that is neither aggregated nor part of the GROUP BY clause. The database does not know which employee's name to show for each department group. Fix: either add name to GROUP BY (which changes the meaning), remove name from SELECT, or use a subquery to find the employee with the max salary."
---

# Hook

Every aggregate function you have learned — COUNT, SUM, AVG, MIN, MAX — has been producing a single number for the entire table. But most real-world questions are not "grand total" questions. They are "per group" questions: revenue per region, orders per customer, average score per category.

GROUP BY is what turns grand totals into breakdowns. It is the most important clause in analytical SQL.

# Lore Introduction

"The Guild Master doesn't want the total earnings for all members," the Administrator said. "She wants earnings per rank — Apprentice total, Journeyman total, Master total." Master Selvaris added three words: `GROUP BY rank`. "The aggregate now runs independently within each rank group," she said. "Three groups, three totals." She then added department as a second GROUP BY column. "Now twelve groups — three ranks across four departments. Twelve totals, each correct for its combination." The Administrator stared at the 12-row result. "One query. Twelve answers." Selvaris nodded. "GROUP BY is the clause that turns one number into many."

# Core Learning

## Concept Introduction

### Why GROUP BY Exists

Without GROUP BY, aggregates produce one row for the whole table:

```sql
SELECT COUNT(*) AS total_orders, SUM(total_amount) AS total_revenue
FROM orders;
-- Returns ONE row: total across all orders
```

With GROUP BY, aggregates produce one row per group:

```sql
SELECT status, COUNT(*) AS order_count, SUM(total_amount) AS revenue
FROM orders
GROUP BY status;
-- Returns ONE row PER STATUS VALUE
```

Result:
```
status      | order_count | revenue
completed   | 31,200      | 2,841,000
pending     | 6,800       | 589,000
cancelled   | 2,000       | 0
```

### The GROUP BY Rule

**Every column in SELECT must either be in GROUP BY or be inside an aggregate function.**

```sql
-- VALID: status is in GROUP BY, COUNT(*) is an aggregate
SELECT status, COUNT(*) FROM orders GROUP BY status;

-- INVALID: customer_name is neither aggregated nor in GROUP BY
SELECT status, customer_name, COUNT(*) FROM orders GROUP BY status;
-- Error: column "customer_name" must appear in GROUP BY or aggregate

-- VALID: both non-aggregate columns are in GROUP BY
SELECT region, category, SUM(revenue) FROM sales GROUP BY region, category;
```

### Multiple GROUP BY Columns

```sql
-- One group per (region, year) combination
SELECT
    region,
    EXTRACT(YEAR FROM order_date) AS year,
    COUNT(*) AS order_count,
    SUM(total_amount) AS revenue
FROM orders
GROUP BY region, EXTRACT(YEAR FROM order_date)
ORDER BY year, revenue DESC;
```

Result (excerpt):
```
region  | year | order_count | revenue
North   | 2025 | 4,200       | 381,000
South   | 2025 | 3,800       | 346,000
North   | 2026 | 1,100       | 103,000
South   | 2026 | 900         | 82,000
```

### WHERE and GROUP BY — Execution Order

```sql
-- WHERE filters BEFORE grouping
SELECT customer_id, COUNT(*) AS completed_orders, SUM(total_amount) AS spent
FROM orders
WHERE status = 'completed'          -- Step 1: keep only completed rows
GROUP BY customer_id                -- Step 2: group the remaining rows
ORDER BY spent DESC;                -- Step 3: sort the result
```

This is different from filtering after grouping (which requires HAVING — covered in the next lesson).

### GROUP BY Expressions

You can group by expressions, not just column names:

```sql
-- Group by year (expression)
SELECT EXTRACT(YEAR FROM order_date) AS year, COUNT(*) AS orders
FROM orders
GROUP BY EXTRACT(YEAR FROM order_date);

-- Group by first letter of name
SELECT SUBSTRING(last_name, 1, 1) AS letter, COUNT(*) AS count
FROM customers
GROUP BY SUBSTRING(last_name, 1, 1)
ORDER BY letter;
```

### Combining All Aggregates

```sql
-- Full sales breakdown by category
SELECT
    category,
    COUNT(*)                    AS product_count,
    SUM(units_sold)             AS total_units,
    ROUND(AVG(unit_price), 2)   AS avg_price,
    MIN(unit_price)             AS cheapest,
    MAX(unit_price)             AS most_expensive,
    SUM(units_sold * unit_price) AS total_revenue
FROM products
GROUP BY category
ORDER BY total_revenue DESC;
```

## Common Mistakes

- **Selecting a non-aggregated column not in GROUP BY**: Standard SQL requires all SELECT columns to be either aggregated or in GROUP BY. MySQL historically allowed this (returning arbitrary values) — do not rely on this behaviour.
- **Grouping by the aggregate**: `GROUP BY COUNT(*)` is an error — you cannot group by an aggregate result.
- **Trying to use column aliases in GROUP BY**: `SELECT EXTRACT(YEAR FROM d) AS yr ... GROUP BY yr` works in some databases (PostgreSQL, MySQL) but not all (SQL Server). Use the expression to be safe.
- **Confusing WHERE and HAVING**: WHERE filters before grouping (on row values); HAVING filters after grouping (on aggregate results). The next lesson covers HAVING.

## Mental Model

Think of GROUP BY as a sorting hat that reads a column value and places each row into a labelled bucket. Every row with the same value goes into the same bucket. Once all rows are bucketed, aggregate functions run independently inside each bucket — COUNT counts the contents of that bucket, SUM adds them up, AVG averages them. The result is one output row per bucket. WHERE removes rows before the sorting hat sees them; HAVING removes entire buckets after they are processed.

## Mini Summary

- ✔ `GROUP BY col` — runs aggregates independently per group
- ✔ Every non-aggregate SELECT column must appear in GROUP BY
- ✔ Multiple GROUP BY columns create one group per unique combination
- ✔ WHERE filters rows before grouping; HAVING (next lesson) filters groups after
- ✔ Logical order: FROM → WHERE → GROUP BY → SELECT → ORDER BY

# Guided Practice Quest

Work through the guided steps to write GROUP BY queries on a single column, multiple columns, and combined with WHERE, applying the rule about non-aggregate SELECT columns.

# Solo Practice Quest

An `employee_performance` table has: `employee_id`, `department_id`, `review_year`, `performance_score` (1-5), `bonus_paid` (may be NULL), `projects_completed`. Write six queries: (1) average performance score per department, (2) total bonus paid per department per year, (3) number of employees per department who received a bonus (bonus_paid IS NOT NULL), (4) average projects completed per year, (5) departments where total bonus paid in 2025 exceeded 50,000, (6) a comprehensive 2025 department report: headcount, avg score, total bonus, max/min score in a single query. Explain which columns must be in GROUP BY for each query and why.

# Integration

**Mathematics**: GROUP BY implements the mathematical concept of a partition of a set. Given a relation R and a set of grouping attributes G, GROUP BY creates equivalence classes [t]_G = {s ∈ R : s[G] = t[G]} — all tuples with the same G-values form one class. The aggregate function then maps each equivalence class to a scalar value: f([t]_G) → scalar. This is formally a quotient operation: R / ~_G, where ~_G is the equivalence relation defined by equality on G. The result relation has one tuple per equivalence class — the "quotient set" of R with respect to the partition defined by G.

**Sciences (Ecology — Biodiversity Surveys)**: Biodiversity research groups species observations by taxonomy (genus, family, order) and by location (habitat, region, grid square) to compute species richness (COUNT(DISTINCT species_id)) and abundance (SUM(individual_count)) per group. A survey database query is: `SELECT habitat_type, family, COUNT(DISTINCT species_id) AS species_richness, SUM(individuals) AS total_abundance FROM observations GROUP BY habitat_type, family`. This is directly the two-column GROUP BY pattern — one group per (habitat, family) combination. The same partition-then-aggregate structure used by ecologists is the formal GROUP BY operation.

# Lore Conclusion

The Guild Master's earnings breakdown arrived: four ranks, three departments, twelve groups — each with its own total earnings, average tenure, and head count. "Every number is for exactly one combination of rank and department," she said. "No mixing, no double-counting." Master Selvaris closed the query. "GROUP BY is the clause that makes aggregate functions useful for real analysis. Without it, you have grand totals. With it, you have breakdowns — and breakdowns are what drive decisions." She paused. "Every dashboard you will ever build has GROUP BY underneath it. Learn it deeply. You will write it hundreds of times."

---
