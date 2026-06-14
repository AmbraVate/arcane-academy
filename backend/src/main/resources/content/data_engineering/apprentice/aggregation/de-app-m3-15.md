---
id: de-app-m3-15
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
lesson: having
title: "HAVING"
sortOrder: 15
difficulty: 2
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m3-14]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains the difference between WHERE and HAVING and when each is used
    - Writes a HAVING clause that filters on an aggregate result
    - Explains the logical execution order including HAVING
    - Correctly places row-level filters in WHERE and group-level filters in HAVING
    - Reflects on a common mistake of using HAVING when WHERE would be more efficient
  keywords: [HAVING, WHERE, aggregate, filter, GROUP BY, execution order, group, post-aggregation, COUNT, SUM]
  modelAnswer: |
    HAVING filters groups after GROUP BY has run and aggregates have been computed. WHERE filters individual rows before grouping. The rule is: if the filter uses an aggregate function (COUNT, SUM, AVG, MIN, MAX), it belongs in HAVING. If it tests a raw column value, it belongs in WHERE. The logical execution order is FROM → WHERE → GROUP BY → HAVING → SELECT → ORDER BY. Using HAVING where WHERE is sufficient is less efficient because it processes more rows through grouping before discarding groups.
guidedSteps:
  - id: de-app-m3-15-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which clause do you use to keep only customers who have placed more than 5 orders?
    inputConfig:
      options:
        - "WHERE COUNT(*) > 5"
        - "HAVING COUNT(*) > 5"
        - "WHERE orders > 5"
        - "FILTER COUNT(*) > 5"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["HAVING COUNT(*) > 5"]
      rejectedFeedback: "You cannot use aggregate functions in WHERE — WHERE runs before grouping and before COUNT has been computed. HAVING runs after GROUP BY and aggregation, so COUNT(*) is available for filtering. The query structure is: SELECT customer_id, COUNT(*) FROM orders GROUP BY customer_id HAVING COUNT(*) > 5. FILTER is not a standard clause for this purpose."
    hint: "Filtering on COUNT, SUM, AVG, MIN, or MAX requires HAVING — those values don't exist when WHERE runs."
    reflectionPrompt: "How would you change this to find customers who have placed between 5 and 20 orders?"
  - id: de-app-m3-15-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To find categories with average product price above 50, you write: `SELECT category, AVG(price) FROM products GROUP BY category ________ AVG(price) > 50;`
    inputConfig:
      placeholder: "HAVING"
    markingRule:
      matchMode: CONTAINS
      accepted: [HAVING, having]
      rejectedFeedback: "HAVING AVG(price) > 50 filters groups after GROUP BY has run. Only categories where the computed average exceeds 50 appear in the result. WHERE AVG(price) > 50 would be an error — WHERE cannot reference aggregate results. HAVING is the post-aggregation filter; WHERE is the pre-aggregation filter."
    hint: "This filter uses an aggregate function — it cannot be WHERE."
    reflectionPrompt: "Could you use the alias in HAVING? e.g. HAVING avg_price > 50 instead of HAVING AVG(price) > 50?"
  - id: de-app-m3-15-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why `WHERE status = 'completed'` should go before GROUP BY rather than in a HAVING clause, even though both would produce the same result.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [efficiency, performance, rows, filter, before, grouping, fewer, WHERE, HAVING, waste]
      rejectedFeedback: "WHERE filters rows before grouping, so fewer rows enter the GROUP BY phase — the database does less work. HAVING filters groups after they have already been formed and aggregated. If you filter status = 'completed' in HAVING, the database groups ALL rows (including cancelled, pending, etc.), computes aggregates for every group, then throws away the ones that don't match. WHERE eliminates the unwanted rows before that work ever starts — it is more efficient."
    hint: "Think about at what point in execution each filter runs, and how much unnecessary work HAVING does."
    reflectionPrompt: "Is there ever a case where you cannot put a filter in WHERE and must use HAVING for a non-aggregate condition?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the correct logical execution order for a query with WHERE, GROUP BY, and HAVING?"
    options:
      - "FROM → GROUP BY → WHERE → HAVING → SELECT"
      - "FROM → WHERE → GROUP BY → HAVING → SELECT"
      - "FROM → HAVING → GROUP BY → WHERE → SELECT"
      - "SELECT → FROM → WHERE → GROUP BY → HAVING"
    correctIndex: 1
    feedback: "The logical execution order is: FROM (identify table) → WHERE (filter rows) → GROUP BY (partition rows into groups) → HAVING (filter groups) → SELECT (compute output) → ORDER BY (sort output). This order is why WHERE cannot reference aggregates (they don't exist yet) and why HAVING can."
  - type: MULTIPLE_CHOICE
    question: "Which query finds departments with more than 10 employees AND an average salary over 60,000?"
    options:
      - "SELECT department_id FROM employees WHERE COUNT(*) > 10 AND AVG(salary) > 60000;"
      - "SELECT department_id, COUNT(*), AVG(salary) FROM employees GROUP BY department_id HAVING COUNT(*) > 10 AND AVG(salary) > 60000;"
      - "SELECT department_id FROM employees HAVING COUNT(*) > 10 GROUP BY department_id;"
      - "SELECT department_id FROM employees GROUP BY department_id WHERE COUNT(*) > 10;"
    correctIndex: 1
    feedback: "Both COUNT(*) and AVG(salary) are aggregate functions — both conditions must be in HAVING. The correct structure is: GROUP BY first to form groups, then HAVING to filter on the aggregate results. Multiple conditions in HAVING use AND/OR just like WHERE. Option A is invalid (WHERE with aggregates). Options C and D have incorrect clause ordering."
retrieval:
  recall: "Write a query that finds product categories where total revenue exceeds 100,000."
  explain: "Explain the difference between WHERE and HAVING using the logical execution order."
  mistakeId:
    code: "SELECT department_id, AVG(salary) FROM employees HAVING AVG(salary) > 50000 WHERE department_id > 3"
    answer: "Two errors: (1) HAVING before WHERE is wrong — clause order must be GROUP BY then HAVING; WHERE must come before GROUP BY. (2) Filtering on department_id > 3 is a row-level filter that belongs in WHERE, not HAVING. Correct query: SELECT department_id, AVG(salary) FROM employees WHERE department_id > 3 GROUP BY department_id HAVING AVG(salary) > 50000."
---

# Hook

You have learned to count, sum, and average per group with GROUP BY. But what if you only want groups that meet a condition — only customers with more than 10 orders, only categories with average price above 50, only regions where total revenue exceeds a million?

You cannot use WHERE for this. WHERE runs before grouping. The aggregate values do not exist when WHERE runs.

HAVING is the solution: a filter clause that runs after GROUP BY, operating on aggregate results.

# Lore Introduction

"The report shows earnings per guild rank," the Administrator said, reviewing the GROUP BY results. "But the Guild Master only wants ranks where the average annual earnings exceed 500 gold. The low-volume ranks should be hidden." Master Selvaris wrote: `HAVING AVG(annual_earnings) > 500`. "HAVING filters groups," she explained. "Not rows — groups. After the grouping is done and the averages are computed, HAVING keeps only the groups that pass the condition." She added another condition. "AND COUNT(*) > 5 — she also wants at least 6 members in the group, otherwise the average is not meaningful." The Administrator nodded. "So WHERE removes rows before grouping, and HAVING removes groups after?" Selvaris smiled. "Exactly. Different moments, different filters."

# Core Learning

## Concept Introduction

### The HAVING Clause

HAVING filters groups after GROUP BY has run and aggregates are computed:

```sql
-- Groups (customers) with more than 5 orders
SELECT customer_id, COUNT(*) AS order_count
FROM orders
GROUP BY customer_id
HAVING COUNT(*) > 5;

-- Categories with average price above 50
SELECT category, ROUND(AVG(price), 2) AS avg_price
FROM products
GROUP BY category
HAVING AVG(price) > 50
ORDER BY avg_price DESC;
```

### WHERE vs HAVING — The Core Distinction

```sql
-- WHERE: filter rows before grouping (on column values)
-- HAVING: filter groups after grouping (on aggregate results)

-- Find total revenue per completed-order customer (WHERE for row filter, HAVING for group filter)
SELECT
    customer_id,
    COUNT(*)            AS order_count,
    SUM(total_amount)   AS total_spent
FROM orders
WHERE status = 'completed'          -- row filter: exclude non-completed rows BEFORE grouping
GROUP BY customer_id
HAVING SUM(total_amount) > 500      -- group filter: keep only high-value customers AFTER aggregation
ORDER BY total_spent DESC;
```

### Full Execution Order

```
FROM orders               → identify the source table
WHERE status = 'completed'  → filter to completed rows only (row-level)
GROUP BY customer_id       → partition into per-customer groups
HAVING SUM(...) > 500       → keep only groups meeting the condition (group-level)
SELECT customer_id, ...    → compute output columns
ORDER BY total_spent DESC  → sort the result
```

### Multiple HAVING Conditions

```sql
-- Departments with more than 10 employees AND average salary over 60,000
SELECT
    department_id,
    COUNT(*)        AS headcount,
    AVG(salary)     AS avg_salary
FROM employees
GROUP BY department_id
HAVING COUNT(*) > 10
   AND AVG(salary) > 60000;

-- Categories with revenue between 50,000 and 200,000
SELECT category, SUM(revenue) AS total_revenue
FROM sales
GROUP BY category
HAVING SUM(revenue) BETWEEN 50000 AND 200000;
```

### HAVING Without Aggregate (Unusual but Valid)

```sql
-- HAVING can reference GROUP BY columns, though WHERE is preferred for efficiency
SELECT region, COUNT(*) FROM sales GROUP BY region HAVING region = 'North';
-- Equivalent to WHERE region = 'North' but less efficient
-- Use WHERE here instead — HAVING is for aggregate conditions only
```

### WHERE + GROUP BY + HAVING Together

```sql
-- Most active customers in 2026 (at least 3 orders, excluding cancelled)
SELECT
    customer_id,
    COUNT(*)            AS orders_placed,
    SUM(total_amount)   AS total_spent
FROM orders
WHERE order_date >= '2026-01-01'        -- row filter: 2026 orders only
  AND status != 'cancelled'             -- row filter: exclude cancelled
GROUP BY customer_id
HAVING COUNT(*) >= 3                    -- group filter: at least 3 orders
ORDER BY total_spent DESC
LIMIT 20;
```

## Why It Matters

HAVING completes the aggregation toolkit by letting you filter on the *results* of aggregation — questions like "which customers placed more than ten orders?" simply cannot be answered with WHERE alone.

- Real analyses are usually about exceptional groups: busiest stores, slowest endpoints, top-spending customers — all HAVING territory
- Confusing WHERE (filters rows before grouping) with HAVING (filters groups after) produces queries that run fine but answer the wrong question
- Interviewers and code reviewers use this distinction as a litmus test for whether someone truly understands aggregation order

Knowing exactly when each filter applies makes your queries both correct and efficient.

## Common Mistakes

- **Using WHERE instead of HAVING for aggregate conditions**: `WHERE COUNT(*) > 5` is an error — COUNT does not exist at WHERE time. Use HAVING.
- **Using HAVING instead of WHERE for row-level conditions**: `HAVING status = 'completed'` works but is inefficient — it groups all rows first, then discards groups. Use WHERE to filter rows before grouping.
- **Wrong clause order**: The required order is WHERE → GROUP BY → HAVING. Reversing them is a syntax error.
- **Not realising HAVING can use column aliases from SELECT in some databases**: PostgreSQL allows `HAVING total_revenue > 100` (using the SELECT alias); SQL Server and MySQL may not. Using the expression directly (`HAVING SUM(revenue) > 100`) is always safe.

## Mental Model

Extend the sorting hat metaphor from GROUP BY: WHERE stands at the door before the hat — it turns away rows that don't meet the entry criteria before they are sorted into buckets. HAVING stands at the exit — after all buckets are filled and counted, it inspects each bucket and removes those that do not meet the criteria. The two filters operate at different points in time on different things: WHERE on individual rows, HAVING on completed groups.

## Mini Summary

- ✔ `HAVING` filters groups after aggregation — runs after GROUP BY
- ✔ Use HAVING for aggregate conditions: `HAVING COUNT(*) > 5`, `HAVING SUM(...) > 1000`
- ✔ Use WHERE for row-level conditions — it runs before grouping (more efficient)
- ✔ Both can appear in the same query: WHERE first, GROUP BY second, HAVING third
- ✔ Execution order: FROM → WHERE → GROUP BY → HAVING → SELECT → ORDER BY

# Guided Practice Quest

Work through the guided steps to filter groups with HAVING, combine WHERE and HAVING in a single query, and identify which conditions belong in WHERE versus HAVING.

# Solo Practice Quest

A `sales` table has: `sale_id`, `salesperson_id`, `region`, `product_category`, `amount`, `sale_date`, `is_returned` (boolean). Write six queries: (1) regions with total revenue over 500,000, (2) salesperson IDs with more than 50 sales in the current year, (3) product categories with average sale amount between 100 and 500, (4) salespeople with a return rate (returned sales / total sales) above 10%, (5) regions where both total revenue exceeds 100,000 AND average sale amount is above 200, (6) a comprehensive leaderboard query: top 10 salespeople by revenue this year, excluding returns, with at least 20 sales. For each, identify which conditions are in WHERE and which are in HAVING, and explain why.

# Integration

**Mathematics**: HAVING implements a selection predicate on the quotient relation produced by GROUP BY. In relational algebra, if GROUP BY creates the quotient relation R/~_G, then HAVING applies a predicate P on this quotient: σ_P(R/~_G). The predicate P can reference aggregate functions over the equivalence classes — it is a second-order selection operating on the aggregated structure rather than the base tuples. This two-phase filtering (σ on R to produce R', then GROUP BY on R' to produce quotient, then σ_P on the quotient) is why WHERE and HAVING are formally distinct operations even though they both express "keep rows/groups that satisfy a condition."

**Sciences (Epidemiology)**: Disease surveillance uses the HAVING pattern constantly. A researcher studying outbreak clusters computes: `SELECT region, COUNT(case_id) AS cases, COUNT(case_id) * 100.0 / population AS rate FROM disease_reports JOIN regions USING (region_id) WHERE report_date >= '2026-01-01' GROUP BY region HAVING COUNT(case_id) * 100.0 / population > 0.01` — finding regions where the incidence rate exceeds 1 per 10,000 population. The HAVING clause filters on the computed rate (an aggregate expression), exactly because the threshold is a per-group metric, not a per-row filter. Public health alerts are triggered by precisely this pattern.

# Lore Conclusion

The final report showed four guild ranks meeting both criteria: more than five members and average earnings over 500 gold. The other twelve ranks were filtered out. "HAVING removed the groups that did not qualify," Master Selvaris said. "WHERE had already removed the individuals from the wrong years. Together, they produced exactly the subset the Guild Master needed." She closed the session. "You now have the complete aggregate toolkit: COUNT, SUM, AVG, MIN, MAX for computing values; GROUP BY for computing per group; WHERE for filtering rows before grouping; HAVING for filtering groups after. These five concepts underpin almost every analytical query you will ever write." She handed the results to the Administrator. "Every report, every dashboard, every KPI — it all runs on these foundations."

---
