---
id: de-jun-m1-03
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m1
moduleTitle: "Module 1: Advanced SQL"
moduleGlyph: "⚡"
moduleSortOrder: 1
topicSlug: window_functions
topicTitle: "Window Functions"
topicSortOrder: 3
lesson: window_functions
title: "Window Functions"
sortOrder: 3
difficulty: 3
estimatedMinutes: 35
xpReward: 55
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m1-02]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what a window function is and how OVER() differs from GROUP BY
    - Writes PARTITION BY to apply a window function within groups
    - Distinguishes ROW_NUMBER, RANK, and DENSE_RANK with an example showing their difference
    - Uses LAG and LEAD to access adjacent rows
    - Explains the ROWS/RANGE BETWEEN frame clause for running calculations
  keywords: [window function, OVER, PARTITION BY, ORDER BY, ROW_NUMBER, RANK, DENSE_RANK, LAG, LEAD, running total, frame, SUM OVER, NTILE]
  modelAnswer: |
    A window function performs a calculation across a set of rows related to the current row without collapsing them into a single output row (unlike GROUP BY). OVER() defines the window: PARTITION BY divides rows into groups; ORDER BY determines row order within the partition. ROW_NUMBER assigns a unique sequential integer. RANK assigns the same number to ties but leaves gaps. DENSE_RANK assigns the same number to ties with no gaps. LAG/LEAD access values from previous/next rows without a self-join. The ROWS BETWEEN frame clause defines the window boundaries for running totals: ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW accumulates from the start.
guidedSteps:
  - id: de-jun-m1-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Three products in the same category have revenues of £500, £500, and £300. Using RANK(), what rank values do they receive?
    inputConfig:
      options:
        - "1, 2, 3 — every product gets a unique rank"
        - "1, 1, 3 — ties share rank 1; rank 2 is skipped"
        - "1, 1, 2 — ties share rank 1; next rank is 2 (no gap)"
        - "0, 0, 1 — ranks start at 0"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["1, 1, 3 — ties share rank 1; rank 2 is skipped"]
      rejectedFeedback: "RANK() assigns the same rank to ties but leaves a gap equal to the number of tied rows. Two rows tied at first place both receive rank 1, and the next distinct value receives rank 3 (skipping rank 2). DENSE_RANK() also assigns the same rank to ties, but without the gap — the next distinct value gets rank 2. ROW_NUMBER() always assigns unique sequential integers regardless of ties. The choice matters for pagination, leaderboards, and nth-row selection: DENSE_RANK is usually preferred for 'top N per group' queries."
    hint: "RANK leaves gaps; DENSE_RANK does not."
    reflectionPrompt: "When would you use RANK vs DENSE_RANK in a real business report?"
  - id: de-jun-m1-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To apply a window function separately to each department without combining them, you use ________ BY department_id inside the OVER() clause.
    inputConfig:
      placeholder: "PARTITION"
    markingRule:
      matchMode: CONTAINS
      accepted: [PARTITION, "PARTITION BY", partition]
      rejectedFeedback: "PARTITION BY divides the rows into groups (partitions) and applies the window function independently within each partition — analogous to GROUP BY but without collapsing rows. Example: ROW_NUMBER() OVER (PARTITION BY department_id ORDER BY salary DESC) assigns rank 1 to the highest-paid employee in each department. Without PARTITION BY, ROW_NUMBER() OVER (ORDER BY salary DESC) ranks across the entire table."
    hint: "The word that divides a window into sub-windows — similar to GROUP BY but doesn't collapse rows."
    reflectionPrompt: "What does OVER() with no PARTITION BY and no ORDER BY do?"
  - id: de-jun-m1-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain how LAG() is used to calculate month-over-month revenue change without a self-join.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [previous, row, offset, prior, before, LAG, current, minus, subtract, compare]
      rejectedFeedback: "LAG(column, offset, default) accesses a value from a previous row in the window's ordering. For month-over-month change: LAG(revenue, 1) OVER (ORDER BY month) returns the previous month's revenue. You can then compute the difference directly in the SELECT: revenue - LAG(revenue, 1) OVER (ORDER BY month) AS change. Without LAG, you would need a self-join on the previous month — more complex and harder to read. LAG with an offset of 12 gives year-over-year comparison: LAG(revenue, 12) OVER (ORDER BY month)."
    hint: "LAG returns the value from a row N rows before the current row in the ordering."
    reflectionPrompt: "How would you calculate year-over-year revenue change using LAG?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does SUM(amount) OVER (PARTITION BY customer_id ORDER BY order_date ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) calculate?"
    options:
      - "The total amount across all orders for all customers"
      - "The running total of order amounts for each customer, sorted by order date"
      - "The average amount per customer"
      - "The maximum amount for each customer"
    correctIndex: 1
    feedback: "This window function computes a running total (cumulative sum) of order amounts for each customer, ordered by date. PARTITION BY customer_id resets the running total for each customer. ORDER BY order_date determines the accumulation order. ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW includes all rows from the start of the partition up to and including the current row. After the first order, the running total equals that order's amount. After the second, it equals the first two combined. And so on."
  - type: MULTIPLE_CHOICE
    question: "Why can't you use a window function in a WHERE clause?"
    options:
      - "Window functions are not supported in SQL"
      - "Window functions are calculated after WHERE filtering — you cannot filter on their output in the same SELECT"
      - "Window functions only work in SELECT"
      - "WHERE only supports column references, not functions"
    correctIndex: 1
    feedback: "SQL execution order: FROM → JOIN → WHERE → GROUP BY → HAVING → SELECT → ORDER BY. Window functions are computed during the SELECT phase. Since WHERE runs before SELECT, the window function result does not exist yet when WHERE is evaluated. To filter on a window function result, wrap the query in a CTE or subquery: WITH ranked AS (SELECT *, ROW_NUMBER() OVER (...) AS rn FROM ...) SELECT * FROM ranked WHERE rn = 1."
retrieval:
  recall: "Write a query that returns the top 3 products by revenue within each category, using window functions."
  explain: "Explain ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW vs ROWS BETWEEN 2 PRECEDING AND CURRENT ROW, with examples of when each would be used."
  mistakeId:
    code: "SELECT customer_id, SUM(total) OVER () AS total_revenue FROM orders WHERE SUM(total) OVER () > 10000"
    answer: "Window functions cannot be used in WHERE clauses because they are evaluated after WHERE in the SQL execution order. The fix is to wrap the query in a CTE or subquery: WITH customer_totals AS (SELECT customer_id, SUM(total) OVER (PARTITION BY customer_id) AS total_revenue FROM orders) SELECT customer_id, total_revenue FROM customer_totals WHERE total_revenue > 10000. Alternatively, use a subquery: SELECT * FROM (SELECT customer_id, SUM(total) OVER (PARTITION BY customer_id) AS total_revenue FROM orders) t WHERE t.total_revenue > 10000."
---

# Hook

GROUP BY collapses rows to produce one output row per group. But sometimes you need to calculate a value that depends on other rows — a running total, a rank within a partition, yesterday's value — while keeping all individual rows visible. Window functions perform calculations across a defined set of rows (the window) without collapsing them.

# Lore Introduction

"I need the rank of each member within their tier by total borrowings this year," the Senior Archivist said. "And I need to keep the individual member rows — not one row per tier." The Junior Engineer frowned. "GROUP BY collapses the rows. I'd need a subquery or a self-join to rank within groups and keep all rows." The Senior Archivist opened the query editor. "Not anymore. Window functions changed SQL fundamentally. One clause: RANK() OVER (PARTITION BY tier ORDER BY borrowings DESC). Every member gets a rank. No collapsing. No self-join." The Junior read the result. "Rank 1 per tier — and all the member data is still there." The Senior Archivist nodded. "Window functions are the most powerful single feature added to standard SQL. Most analytical queries that required complex subqueries or application-layer logic before window functions now have a clean, single-query solution."

# Core Learning

## Concept Introduction

### The OVER() Clause

Every window function uses an `OVER()` clause that defines the window:

```sql
function_name() OVER (
    PARTITION BY column   -- divide into groups (optional)
    ORDER BY column       -- order within the group
    ROWS BETWEEN ...      -- frame boundary (optional)
)
```

```sql
-- Without window functions: GROUP BY collapses rows
SELECT customer_id, SUM(total_amount) AS total
FROM orders
GROUP BY customer_id;
-- Result: one row per customer

-- With window function: all rows preserved
SELECT customer_id, order_id, total_amount,
       SUM(total_amount) OVER (PARTITION BY customer_id) AS customer_total
FROM orders;
-- Result: one row per order, each showing the customer's total
```

### Ranking Functions

```sql
-- ROW_NUMBER: unique rank, no ties allowed
SELECT
    name,
    revenue,
    ROW_NUMBER() OVER (ORDER BY revenue DESC) AS rn
FROM products;
-- 1, 2, 3, 4, 5 ... (always unique)

-- RANK: ties get same rank; next rank skips
-- Two products tied at rank 2 → next is rank 4
SELECT
    name,
    revenue,
    RANK() OVER (ORDER BY revenue DESC) AS rnk
FROM products;
-- 1, 2, 2, 4, 5 ...

-- DENSE_RANK: ties get same rank; no gap
SELECT
    name,
    revenue,
    DENSE_RANK() OVER (ORDER BY revenue DESC) AS drnk
FROM products;
-- 1, 2, 2, 3, 4 ...

-- NTILE: divide into N roughly equal buckets
SELECT name, revenue,
       NTILE(4) OVER (ORDER BY revenue DESC) AS quartile
FROM products;
-- Quartile 1 = top 25%, quartile 4 = bottom 25%
```

### Top N Per Group (Common Pattern)

```sql
-- Top 3 products by revenue within each category
WITH ranked_products AS (
    SELECT
        p.name,
        c.name AS category,
        SUM(ol.quantity * ol.unit_price) AS revenue,
        RANK() OVER (
            PARTITION BY p.category_id
            ORDER BY SUM(ol.quantity * ol.unit_price) DESC
        ) AS rnk
    FROM products p
    JOIN order_lines ol ON p.product_id = ol.product_id
    JOIN categories c ON p.category_id = c.category_id
    GROUP BY p.product_id, p.name, p.category_id, c.name
)
SELECT category, name, revenue
FROM ranked_products
WHERE rnk <= 3
ORDER BY category, rnk;
```

### LAG and LEAD

```sql
-- Month-over-month revenue change
WITH monthly_revenue AS (
    SELECT
        DATE_TRUNC('month', order_date) AS month,
        SUM(total_amount) AS revenue
    FROM orders
    GROUP BY 1
)
SELECT
    month,
    revenue,
    LAG(revenue, 1) OVER (ORDER BY month)  AS prev_month_revenue,
    revenue - LAG(revenue, 1) OVER (ORDER BY month) AS change,
    ROUND(
        (revenue - LAG(revenue, 1) OVER (ORDER BY month))
        / NULLIF(LAG(revenue, 1) OVER (ORDER BY month), 0) * 100,
        1
    ) AS pct_change
FROM monthly_revenue
ORDER BY month;

-- Access next row: LEAD
SELECT
    order_date,
    total_amount,
    LEAD(total_amount, 1) OVER (ORDER BY order_date) AS next_order_amount
FROM orders;
```

### Running Totals and Moving Averages (ROWS BETWEEN)

```sql
-- Running total of revenue
SELECT
    order_date,
    total_amount,
    SUM(total_amount) OVER (
        ORDER BY order_date
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS running_total
FROM orders;

-- 3-month moving average
SELECT
    month,
    revenue,
    AVG(revenue) OVER (
        ORDER BY month
        ROWS BETWEEN 2 PRECEDING AND CURRENT ROW
    ) AS moving_avg_3m
FROM monthly_revenue;
-- At each row: average of current month and 2 prior months

-- RANGE BETWEEN (uses values, not row counts — different for duplicates)
-- ROWS BETWEEN: counts rows regardless of value equality
-- RANGE BETWEEN: includes all rows with the same ORDER BY value
```

### FIRST_VALUE and LAST_VALUE

```sql
-- Each employee's salary relative to the highest-paid in their department
SELECT
    name,
    department_id,
    salary,
    FIRST_VALUE(salary) OVER (
        PARTITION BY department_id
        ORDER BY salary DESC
    ) AS dept_max_salary,
    salary * 100.0 / FIRST_VALUE(salary) OVER (
        PARTITION BY department_id
        ORDER BY salary DESC
    ) AS pct_of_dept_max
FROM employees;
```

## Why It Matters

Window functions are the biggest expressiveness jump in modern SQL — calculations across related rows without collapsing them:

- Rankings ("top 3 per category"), running totals, and month-over-month deltas are one clean clause instead of tortured self-joins
- Unlike GROUP BY, the detail rows survive — you see each order *and* its share of the customer's total
- Analysts and interviewers both treat window fluency as the marker separating intermediate SQL from advanced

Once you think in windows, a whole class of "export it to Python/Excel" problems becomes a single query. That's a permanent productivity upgrade.

## Common Mistakes

- **Using window functions in WHERE**: Wrap in a CTE or subquery — WHERE runs before SELECT where window functions are computed.
- **Confusing ROWS BETWEEN and RANGE BETWEEN**: ROWS counts physical rows; RANGE includes all rows with the same ORDER BY value. They produce different results when there are duplicate values in the ORDER column.
- **RANK vs DENSE_RANK for top-N**: If you want the top 3 products and two products tie at rank 1, RANK gives you ranks 1, 1, 3 (only 2 distinct products with rank ≤ 3 after the tie), while DENSE_RANK gives 1, 1, 2, 2, 3 (5 rows with rank ≤ 3 if more ties exist). Choose deliberately.
- **Missing ORDER BY in OVER()**: SUM without ORDER BY computes the partition total (same value on every row). SUM with ORDER BY computes a running total. Both are valid but mean different things.

## Mental Model

Imagine a spreadsheet where each row can "look" at other rows to compute its value — without those rows disappearing. A window function is exactly this: for each row, calculate a value using a defined set of surrounding rows (the window). PARTITION BY defines which rows are "in scope" (the window group). ORDER BY defines the sequence within that scope. The frame clause (ROWS BETWEEN) defines how many surrounding rows to include. Unlike GROUP BY, the row itself survives in the output — only its computed column reflects the window calculation.

## Mini Summary

- ✔ Window functions use OVER() and preserve all individual rows (unlike GROUP BY)
- ✔ PARTITION BY divides into groups; ORDER BY sequences rows within partitions
- ✔ ROW_NUMBER (unique), RANK (gaps on ties), DENSE_RANK (no gaps)
- ✔ LAG/LEAD access previous/next rows — no self-join required
- ✔ ROWS BETWEEN frame clause enables running totals and moving averages
- ✔ Filter on window function results via a CTE or subquery (not WHERE)

# Guided Practice Quest

Work through the guided steps to apply RANK() with PARTITION BY to produce a per-category product ranking, write a running total query using SUM() OVER with ROWS BETWEEN, and use LAG() to calculate month-over-month percentage change.

# Solo Practice Quest

Using an orders, order_lines, customers, and products schema: (1) write a query that returns each customer's order history with a running total of their spend (cumulative), and highlight the order that pushed them past the £1,000 total; (2) rank customers by total spend and use NTILE(10) to divide them into deciles — what percentage of revenue comes from the top decile?; (3) use LAG with an offset of 12 to calculate year-over-year monthly revenue change; (4) write a "top 2 per category" query using DENSE_RANK; (5) compare the results of RANK, DENSE_RANK, and ROW_NUMBER on a dataset with three-way ties, showing all three columns side by side and explaining the difference. For question 2, write the NTILE query and the revenue-per-decile aggregation as two chained CTEs.

# Integration

**Mathematics**: Window functions are direct implementations of concepts from discrete mathematics and statistics. RANK corresponds to the ordinal ranking function ρ: S → ℕ over a totally ordered set. Running totals implement prefix sums: S_n = Σ_{i=1}^{n} a_i, where each row n returns the sum of all elements up to and including position n. Moving averages implement sliding window averages: MA_k(n) = (1/k) Σ_{i=n-k+1}^{n} a_i. NTILE implements histogram binning into equal-frequency buckets. LAG/LEAD implement discrete differences: Δa_n = a_n - a_{n-1}, the discrete analogue of derivatives. These are foundational operations in time-series analysis, signal processing, and statistical computation.

**Sciences (Finance — Technical Analysis)**: Window functions are the SQL implementation of technical analysis indicators used in financial markets. Moving averages (SUM OVER with ROWS BETWEEN) compute the 50-day or 200-day moving average price — a standard trend signal. RANK within a window is used to identify relative strength: which stocks in a sector have performed best in the last 30 days. LAG computes daily returns: (price - LAG(price, 1)) / LAG(price, 1). LEAD is used for forward-looking performance measurement. Financial databases with billions of price ticks use exactly these window function patterns — the same SQL constructs, applied at scale to market data instead of order data.

# Lore Conclusion

"Every member now has a rank within their tier," the Junior Engineer reported. "And I can see the running total of borrowings across the year. With LAG, month-over-month change is one column." The Senior Archivist reviewed the output. "And no self-joins, no correlated subqueries for the ranking, no application-layer aggregation." She closed the results. "Window functions are the boundary between analysts who write SQL and data engineers who think in SQL. With ranking, running totals, LAG/LEAD, and moving averages, most analytical questions become single-query problems." She set the query aside. "Next: set operations — how to combine result sets from multiple queries."

---
