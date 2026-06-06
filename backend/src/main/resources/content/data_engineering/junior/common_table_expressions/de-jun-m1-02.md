---
id: de-jun-m1-02
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m1
moduleTitle: "Module 1: Advanced SQL"
moduleGlyph: "⚡"
moduleSortOrder: 1
topicSlug: common_table_expressions
topicTitle: "Common Table Expressions"
topicSortOrder: 2
lesson: common_table_expressions
title: "Common Table Expressions"
sortOrder: 2
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m1-01]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Writes correct WITH ... AS (...) syntax for a single CTE
    - Chains multiple CTEs in a single WITH clause
    - Explains why CTEs improve readability over nested subqueries
    - Describes the structure and termination condition of a recursive CTE
    - Identifies use cases for recursive CTEs (hierarchies, sequences)
  keywords: [CTE, WITH, recursive, anchor, recursive member, UNION ALL, hierarchy, readability, chain, termination]
  modelAnswer: |
    A Common Table Expression (CTE) is defined with WITH name AS (SELECT ...) before the main query. Multiple CTEs chain with commas inside one WITH clause. CTEs improve readability by naming intermediate results instead of nesting subqueries. A recursive CTE has an anchor member (base case) UNION ALL'd with a recursive member (which references the CTE itself). A termination condition — typically a WHERE clause that eventually becomes false — prevents infinite recursion. Recursive CTEs are used for hierarchies (org charts, category trees), sequences, and graph traversal.
guidedSteps:
  - id: de-jun-m1-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which SQL clause introduces a Common Table Expression?
    inputConfig:
      options:
        - "DECLARE name AS (...)"
        - "WITH name AS (...)"
        - "SUBQUERY name AS (...)"
        - "TEMP TABLE name AS (...)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["WITH name AS (...)"]
      rejectedFeedback: "CTEs are introduced with the WITH keyword followed by the CTE name, AS, and the SELECT statement in parentheses. The WITH clause comes before the main SELECT: WITH cte_name AS (SELECT ...) SELECT * FROM cte_name. This is the standard SQL syntax supported in PostgreSQL, SQL Server, MySQL 8+, and SQLite 3.35+. DECLARE is used for variables in stored procedures. TEMP TABLE creates a physical temporary table (different from a CTE — CTEs exist only for the duration of the query)."
    hint: "The keyword that starts a CTE is a common English preposition meaning 'together with'."
    reflectionPrompt: "What is the difference between a CTE and a temporary table? When would you use each?"
  - id: de-jun-m1-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In a recursive CTE, the base case SELECT that does not reference the CTE itself is called the ________ member.
    inputConfig:
      placeholder: "anchor"
    markingRule:
      matchMode: CONTAINS
      accepted: [anchor, "anchor member", "base case", base, initial]
      rejectedFeedback: "The anchor member (base case) is the first SELECT in a recursive CTE — it does not reference the CTE name and returns the initial rows. Example: WITH RECURSIVE hierarchy AS (SELECT id, name, manager_id, 0 AS depth FROM employees WHERE manager_id IS NULL  -- anchor: top-level managers UNION ALL SELECT e.id, e.name, e.manager_id, h.depth + 1 FROM employees e JOIN hierarchy h ON e.manager_id = h.id  -- recursive member). The anchor provides the starting rows; the recursive member extends them until no more rows match."
    hint: "It provides the starting rows — the non-recursive half of a UNION ALL."
    reflectionPrompt: "What happens if a recursive CTE has no termination condition?"
  - id: de-jun-m1-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why using multiple chained CTEs is typically more readable than an equivalent query written with nested subqueries.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [name, readable, named, step, intermediate, sequential, understand, follow, layer, nesting, level]
      rejectedFeedback: "Chained CTEs give each intermediate result set a meaningful name, making the intent of each step explicit. A reader can follow the query top-to-bottom: first we calculate X, then Y, then the final result uses both. Deeply nested subqueries require reading inside-out — the innermost query executes first, but appears last visually. Named CTEs also make debugging easier: you can run each CTE independently to verify its result before combining them."
    hint: "How does naming each step affect a reader's ability to understand the query?"
    reflectionPrompt: "Take a complex nested subquery you have written before and rewrite it using CTEs. Which is easier to read?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You have three CTEs in a WITH clause. Which of the following is the correct syntax?"
    options:
      - "WITH cte1 AS (...) WITH cte2 AS (...) WITH cte3 AS (...) SELECT ..."
      - "WITH cte1 AS (...), cte2 AS (...), cte3 AS (...) SELECT ..."
      - "WITH cte1 AS (...); WITH cte2 AS (...); WITH cte3 AS (...); SELECT ..."
      - "DECLARE cte1 AS (...), cte2 AS (...), cte3 AS (...) SELECT ..."
    correctIndex: 1
    feedback: "Multiple CTEs are separated by commas within a single WITH clause. Only one WITH keyword is used, and the final CTE definition is followed immediately by the main SELECT (no comma). Later CTEs can reference earlier CTEs in the same WITH clause: WITH cte1 AS (...), cte2 AS (SELECT * FROM cte1 WHERE ...) SELECT * FROM cte2."
  - type: MULTIPLE_CHOICE
    question: "A recursive CTE traversing an org chart runs for 10 seconds on a 10,000-employee table. The most likely cause is:"
    options:
      - "CTEs are always slow on large tables"
      - "The recursive CTE has no effective termination condition and is producing too many rows or cycling"
      - "The anchor member should not use WHERE"
      - "SQL does not support recursion"
    correctIndex: 1
    feedback: "Without an effective termination condition, the recursive member keeps joining until it runs out of new rows — but if there is a cycle in the data (an employee who is their own manager's manager), the recursion never terminates naturally. Most databases have a default maximum recursion depth (SQL Server: 100, PostgreSQL: configurable). On very large hierarchies, the problem is often a missing termination condition or an unexpectedly deep hierarchy. Always add a depth counter and a WHERE depth < N to bound recursive CTEs."
retrieval:
  recall: "Write a CTE-based query that finds the top 5 customers by total spend, then for each of those customers shows their most recent order date."
  explain: "Explain the structure of a recursive CTE using an employee org chart example. Name the two parts of the UNION ALL and explain the termination condition."
  mistakeId:
    code: "WITH monthly_revenue AS (SELECT EXTRACT(MONTH FROM order_date) AS month, SUM(total_amount) AS revenue FROM orders GROUP BY 1) SELECT * FROM monthly_revenue ORDER BY revenue DESC"
    answer: "This query groups by month number only (1–12) but includes data from multiple years — January 2023 and January 2024 are both month 1 and will be aggregated together. To correctly track monthly revenue, include the year: WITH monthly_revenue AS (SELECT EXTRACT(YEAR FROM order_date) AS year, EXTRACT(MONTH FROM order_date) AS month, SUM(total_amount) AS revenue FROM orders GROUP BY 1, 2) SELECT * FROM monthly_revenue ORDER BY year, month. This is a common mistake when extracting date parts — always ask whether the grouping should span multiple years."
---

# Hook

Subqueries solve the problem of composing queries, but deeply nested subqueries become unreadable: the logic is inverted (innermost executes first, appears last) and there is no way to name intermediate steps. Common Table Expressions solve the readability problem by naming each intermediate result before the main query executes.

# Lore Introduction

"The query works," the Junior Engineer reported, "but even I can barely read it two days later." The Senior Archivist reviewed the five-level nested subquery. "Classic staircase of doom," she said. "The logic is correct but buried. Let me show you Common Table Expressions." She rewrote the first subquery above the main query. "Name each step. The reader can follow the logic from top to bottom — step one does this, step two does that, the final query combines them." The Junior read the rewritten version. "It's three times longer but ten times clearer." The Senior Archivist smiled. "Code is written once, read many times. Optimise for the reader."

# Core Learning

## Concept Introduction

### Basic CTE Syntax

```sql
-- Single CTE
WITH high_value_customers AS (
    SELECT customer_id, SUM(total_amount) AS lifetime_value
    FROM orders
    WHERE status = 'completed'
    GROUP BY customer_id
    HAVING SUM(total_amount) > 5000
)
SELECT c.name, hvc.lifetime_value
FROM customers c
JOIN high_value_customers hvc ON c.customer_id = hvc.customer_id
ORDER BY hvc.lifetime_value DESC;
```

The CTE `high_value_customers` is defined once and referenced in the main query like a table.

### Chaining Multiple CTEs

```sql
WITH 
-- Step 1: monthly revenue by product category
monthly_category_revenue AS (
    SELECT
        DATE_TRUNC('month', o.order_date)  AS month,
        p.category_id,
        SUM(ol.quantity * ol.unit_price)   AS revenue
    FROM orders o
    JOIN order_lines ol ON o.order_id = ol.order_id
    JOIN products p ON ol.product_id = p.product_id
    WHERE o.status != 'cancelled'
    GROUP BY 1, 2
),
-- Step 2: rank categories within each month
ranked_categories AS (
    SELECT
        month,
        category_id,
        revenue,
        RANK() OVER (PARTITION BY month ORDER BY revenue DESC) AS rnk
    FROM monthly_category_revenue
),
-- Step 3: top category per month only
top_category_per_month AS (
    SELECT month, category_id, revenue
    FROM ranked_categories
    WHERE rnk = 1
)
-- Final result
SELECT
    t.month,
    c.name AS top_category,
    t.revenue
FROM top_category_per_month t
JOIN categories c ON t.category_id = c.category_id
ORDER BY t.month;
```

### CTEs vs Subqueries

| | CTE | Subquery |
|---|---|---|
| Naming | Named intermediate result | Anonymous (unless aliased in FROM) |
| Reuse | Can reference the same CTE multiple times | Must repeat the subquery |
| Reading order | Top-to-bottom (logical order) | Inside-out |
| Debugging | Run each CTE independently | Must extract to debug |
| Recursion | Yes (RECURSIVE keyword) | No |

### Recursive CTEs

Recursive CTEs have two parts joined by `UNION ALL`:
1. **Anchor member**: base case — does not reference the CTE
2. **Recursive member**: references the CTE, extending the result

```sql
-- Traverse an employee hierarchy (org chart)
WITH RECURSIVE employee_hierarchy AS (
    -- Anchor: start with top-level managers (no manager above them)
    SELECT
        employee_id,
        name,
        manager_id,
        0 AS depth,
        name AS path
    FROM employees
    WHERE manager_id IS NULL

    UNION ALL

    -- Recursive: add direct reports of already-included employees
    SELECT
        e.employee_id,
        e.name,
        e.manager_id,
        h.depth + 1,
        h.path || ' > ' || e.name
    FROM employees e
    JOIN employee_hierarchy h ON e.manager_id = h.employee_id
    WHERE h.depth < 10   -- termination guard: maximum 10 levels deep
)
SELECT depth, name, path
FROM employee_hierarchy
ORDER BY path;
```

```sql
-- Generate a sequence of numbers (useful for date series generation)
WITH RECURSIVE number_series AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM number_series WHERE n < 100   -- termination: stop at 100
)
SELECT n FROM number_series;
```

### Date Series with Recursive CTE

```sql
-- Generate all dates in a range (useful for filling gaps in time-series data)
WITH RECURSIVE date_series AS (
    SELECT '2024-01-01'::DATE AS d
    UNION ALL
    SELECT d + INTERVAL '1 day' FROM date_series WHERE d < '2024-12-31'
)
SELECT
    ds.d AS date,
    COALESCE(SUM(o.total_amount), 0) AS daily_revenue
FROM date_series ds
LEFT JOIN orders o ON o.order_date::DATE = ds.d
GROUP BY ds.d
ORDER BY ds.d;
-- Produces a row for every day, including days with zero revenue
```

## Common Mistakes

- **Multiple WITH keywords**: Multiple CTEs use one WITH with comma-separated definitions. Two WITH keywords cause a syntax error.
- **No termination condition in recursive CTE**: Without WHERE depth < N or a natural stopping condition, recursion continues until the database hits a maximum recursion limit or runs out of memory.
- **Referencing a CTE before it is defined**: Later CTEs can reference earlier ones, but not vice versa — CTEs within a WITH clause execute in order.
- **Thinking CTEs are always materialised**: In most databases, CTEs are inlined (treated like derived tables) by the query optimiser, not cached. They may re-execute if referenced multiple times. If you need guaranteed single execution, use a temporary table.

## Mental Model

Think of CTEs as named paragraphs in a technical document. Without names, a reader has to read every paragraph before understanding the section — the context is embedded, not explained. With named paragraphs (CTEs), the document says: "First, here is the list of high-value customers. Second, here are their recent orders. Finally, the table below combines both." The reader can follow the logic step by step. Recursive CTEs are like a procedure that says "repeat this step until done" — like instructions for traversing a tree by taking one branch at a time until all leaves are reached.

## Mini Summary

- ✔ CTEs use WITH name AS (...) before the main query
- ✔ Multiple CTEs: one WITH, comma-separated definitions
- ✔ Later CTEs can reference earlier ones in the same WITH clause
- ✔ Recursive CTEs: anchor UNION ALL recursive member, with a termination condition
- ✔ Recursive CTEs solve hierarchies, sequences, and date series generation

# Guided Practice Quest

Work through the guided steps to write a two-CTE chain that calculates monthly revenue and then ranks months by performance, write a recursive CTE that traverses a two-level product category hierarchy, and identify the anchor and recursive members in a provided CTE.

# Solo Practice Quest

Using a database with employees, departments, orders, and products: (1) rewrite a provided nested subquery (3 levels deep) as a CTE chain with meaningful step names; (2) write a recursive CTE that returns all subordinates of a given manager, including subordinates' subordinates, showing depth level and reporting path; (3) use a date-series CTE to produce a monthly revenue report for the last 12 months that includes months with zero revenue (gap-filling); (4) write a CTE that identifies the "second-best" performing product in each category — the product ranked second by revenue within its category; (5) explain why the date-series CTE might be slow for a full year of daily data, and how you would optimise it.

# Integration

**Mathematics**: Recursive CTEs correspond directly to recursive function definitions in mathematics. A recursive CTE with an anchor and recursive member is equivalent to a recurrence relation: f(0) = base_case, f(n) = operation(f(n-1)). The org-chart traversal CTE is equivalent to breadth-first search (BFS) over a directed acyclic graph — the anchor provides the root set (depth 0), and each recursive step adds the next layer of descendants. The date-series CTE generates an arithmetic sequence: a_n = start_date + n days, a_0 = start_date. These are direct applications of recursive sequence definitions from discrete mathematics, implemented in SQL.

**Sciences (Ecology — Food Web Analysis)**: Recursive CTEs are used in ecological network analysis to traverse food webs. Starting from apex predators (anchor: species with no predators), a recursive CTE can traverse predator-prey relationships to build the full food chain: "This species is eaten by these, which are eaten by those, down to decomposers." Conservation databases store these relationships as species_id → predator_id links — exactly the parent-child structure of an org chart. Ecologists use recursive SQL queries to identify which species would cascade toward extinction if a keystone species were removed — path tracing through a biological hierarchy using the same SQL patterns learned here.

# Lore Conclusion

"The Archive query is now twelve lines of named steps instead of six levels of nested subqueries," the Junior Engineer said. "Anyone can read it." The Senior Archivist ran it against the expanded dataset. "And it works correctly on the recursive category hierarchy — categories that contain subcategories that contain sub-subcategories, all traversed automatically." She closed the query file. "CTEs are perhaps the most important SQL skill for working in teams. Correct queries that no one can maintain are a liability. Readable, correct queries are an asset." She paused. "Next: window functions — the most powerful single feature in modern SQL."

---
