---
id: de-app-m3-11
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
lesson: sum
title: "SUM"
sortOrder: 11
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m3-10]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Uses SUM to total a numeric column
    - Explains that SUM ignores NULL values automatically
    - Combines SUM with WHERE to total a filtered subset
    - Combines SUM with GROUP BY to produce per-group totals
    - Reflects on the difference between SUM and COUNT and when each is appropriate
  keywords: [SUM, aggregate, NULL, total, GROUP BY, WHERE, numeric, revenue, amount]
  modelAnswer: |
    SUM is an aggregate function that adds up all numeric values in a column. SUM ignores NULL values — if a row has NULL in the summed column, that row is excluded from the total. When used with WHERE, SUM totals only the rows that match the filter. When used with GROUP BY, SUM produces a total per group rather than a grand total. SUM only works on numeric columns — it cannot sum text or date values. The difference between SUM and COUNT: COUNT measures how many rows exist; SUM measures the total value across those rows.
guidedSteps:
  - id: de-app-m3-11-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      An `orders` table has a `total_amount` column. Three rows have amounts 100, NULL, and 200. What does `SELECT SUM(total_amount) FROM orders` return?
    inputConfig:
      options:
        - "300"
        - "NULL"
        - "400"
        - "0"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["300"]
      rejectedFeedback: "SUM ignores NULL values — it does not treat NULL as 0, but it also does not propagate NULL the way arithmetic expressions do. NULL rows are simply excluded from the sum. So SUM(100, NULL, 200) = 300. If you want NULL treated as 0, use SUM(COALESCE(total_amount, 0)) — though with SUM this is usually unnecessary."
    hint: "SUM skips NULL rows rather than treating them as zero or returning NULL."
    reflectionPrompt: "When would you want NULL to count as 0 in a SUM? When would you want it excluded?"
  - id: de-app-m3-11-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To calculate total revenue per product category, you write: `SELECT category, SUM(revenue) FROM sales GROUP BY ________;`
    inputConfig:
      placeholder: "category"
    markingRule:
      matchMode: CONTAINS
      accepted: [category]
      rejectedFeedback: "GROUP BY category groups all rows with the same category value together, then SUM(revenue) totals the revenue within each group. Without GROUP BY, SUM(revenue) would return a single grand total for all rows. GROUP BY is the mechanism that turns a grand total into per-group totals."
    hint: "GROUP BY the same column you have in the SELECT list alongside the aggregate."
    reflectionPrompt: "What happens if you add a second column to GROUP BY, like GROUP BY category, region?"
  - id: de-app-m3-11-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain the difference between using COUNT(*) and SUM(quantity) on an orders table, and what business question each answers.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [rows, orders, items, quantity, total, how many, count, sum, different]
      rejectedFeedback: "COUNT(*) counts the number of rows — the number of orders placed. SUM(quantity) adds up the quantity values across all rows — the total number of items ordered. A business might ask 'how many orders did we receive?' (COUNT) versus 'how many products did we ship?' (SUM). One order can contain many items, so these two numbers will usually be very different."
    hint: "COUNT measures the number of rows (orders). SUM measures the total value in a column (total items)."
    reflectionPrompt: "How would you get both the number of orders and the total items in a single query?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which query returns the total revenue for completed orders only?"
    options:
      - "SELECT SUM(amount) FROM orders;"
      - "SELECT SUM(amount) FROM orders WHERE status = 'completed';"
      - "SELECT SUM(amount) WHERE status = 'completed' FROM orders;"
      - "SELECT TOTAL(amount) FROM orders WHERE status = 'completed';"
    correctIndex: 1
    feedback: "WHERE filters the rows before SUM aggregates them. SELECT SUM(amount) FROM orders WHERE status = 'completed' first keeps only completed-order rows, then sums their amounts. Clause order matters: SELECT → FROM → WHERE → aggregate. TOTAL() is not a SQL function."
  - type: MULTIPLE_CHOICE
    question: "A sales table has 5 rows with amounts: 10, 20, NULL, 30, NULL. What does SUM(amount) return?"
    options:
      - "NULL"
      - "60"
      - "0"
      - "62 (with NULLs treated as 1)"
    correctIndex: 1
    feedback: "SUM ignores NULLs — 10 + 20 + 30 = 60. The two NULL rows are excluded entirely. SUM only returns NULL if ALL rows are NULL (or the table is empty). This behaviour is different from arithmetic expressions where NULL propagates."
retrieval:
  recall: "Write a query that shows total revenue per customer, ordered from highest to lowest."
  explain: "Explain how SUM handles NULL values and why this differs from arithmetic NULL propagation."
  mistakeId:
    code: "SELECT SUM(order_count) to get the number of orders"
    answer: "SUM(order_count) adds up the values in the order_count column — it does not count the number of rows. To count the number of orders (rows), use COUNT(*). SUM is for totalling numeric values in a column; COUNT is for counting how many rows exist. Using the wrong one gives a plausible-looking but incorrect answer."
---

# Hook

"What is our total revenue this month?" "How much stock have we sold?" "Which region generates the most sales?" These questions need a running total — not a count of rows, but the sum of values across rows. That is SUM.

SUM is the aggregate function for financial and quantity reporting. It turns a column of individual values into a single meaningful total.

# Lore Introduction

"The Guild Treasury wants the total value of all outstanding invoices," the Accountant said. "Not how many invoices — the total gold owed." Master Selvaris wrote: `SELECT SUM(invoice_amount) FROM invoices WHERE status = 'outstanding';`. "SUM adds the value column," she explained. "COUNT would tell you how many invoices. SUM tells you what they are worth." She added GROUP BY guild_id to produce per-guild totals. "The Treasury now has a breakdown by guild — which guilds owe the most, and what the grand total is." She paused. "Counting rows and summing values are two different questions. Know which one you are asking."

# Core Learning

## Concept Introduction

### Basic SUM

```sql
-- Grand total of all order amounts
SELECT SUM(total_amount) AS total_revenue
FROM orders;

-- Total quantity sold across all products
SELECT SUM(quantity_sold) AS units_sold
FROM sales;

-- Total salary cost
SELECT SUM(monthly_salary) * 12 AS annual_salary_bill
FROM employees;
```

### SUM with WHERE

```sql
-- Revenue from completed orders only
SELECT SUM(total_amount) AS completed_revenue
FROM orders
WHERE status = 'completed';

-- Revenue from a specific date range
SELECT SUM(total_amount) AS monthly_revenue
FROM orders
WHERE order_date >= '2026-06-01' AND order_date < '2026-07-01';

-- Total stock value for items below reorder level
SELECT SUM(stock_qty * unit_cost) AS at_risk_inventory_value
FROM products
WHERE stock_qty < reorder_level;
```

### SUM with GROUP BY

```sql
-- Revenue per product category
SELECT category, SUM(total_amount) AS category_revenue
FROM order_lines
GROUP BY category
ORDER BY category_revenue DESC;

-- Total orders per customer
SELECT customer_id, SUM(total_amount) AS customer_lifetime_value
FROM orders
GROUP BY customer_id
ORDER BY customer_lifetime_value DESC
LIMIT 10;
-- Top 10 customers by lifetime value

-- Monthly revenue trend
SELECT
    EXTRACT(YEAR FROM order_date) AS year,
    EXTRACT(MONTH FROM order_date) AS month,
    SUM(total_amount) AS monthly_revenue
FROM orders
GROUP BY EXTRACT(YEAR FROM order_date), EXTRACT(MONTH FROM order_date)
ORDER BY year, month;
```

### SUM and NULL Behaviour

```sql
-- SUM ignores NULLs (unlike arithmetic expressions)
-- amounts: 100, NULL, 200 → SUM = 300 (NULL row excluded)
SELECT SUM(amount) FROM transactions;

-- If ALL values are NULL (or table is empty), SUM returns NULL
-- To return 0 instead of NULL for an empty result:
SELECT COALESCE(SUM(amount), 0) AS total FROM transactions
WHERE status = 'pending';

-- NULL vs 0 can matter: a NULL amount means "unknown"; 0 means "zero value"
-- Treat them differently depending on the business context
```

### SUM vs COUNT — Know Which to Use

```sql
-- How many orders were placed? → COUNT
SELECT COUNT(*) AS order_count FROM orders;

-- What is the total value of those orders? → SUM
SELECT SUM(total_amount) AS total_revenue FROM orders;

-- Both in one query
SELECT
    COUNT(*) AS order_count,
    SUM(total_amount) AS total_revenue,
    SUM(total_amount) / COUNT(*) AS average_order_value
FROM orders;
```

## Common Mistakes

- **Using SUM to count rows**: `SUM(1)` works (adds 1 per row = COUNT), but `COUNT(*)` is clearer. Use SUM for values, COUNT for row counts.
- **Expecting SUM to return 0 for an empty table**: SUM returns NULL, not 0, when there are no rows. Wrap in COALESCE if 0 is required.
- **Applying SUM to non-numeric columns**: SUM on a text or date column is an error. SUM is only for numeric types.
- **Forgetting GROUP BY when showing per-group totals**: `SELECT category, SUM(amount) FROM sales` is an error without GROUP BY — mixing a non-aggregate column with an aggregate requires GROUP BY.

## Mental Model

Think of SUM as a running calculator. As the database reads each row matching your WHERE clause, it feeds the column value into the calculator. NULL rows are skipped — they have no value to add. GROUP BY is like having a separate calculator per group: when the group changes, the running total is recorded and the calculator resets for the next group. At the end, each group's final total is output as one row.

## Mini Summary

- ✔ `SUM(column)` — totals all non-NULL values in a numeric column
- ✔ NULL rows are excluded from the sum (not treated as 0)
- ✔ Add WHERE to sum a filtered subset
- ✔ Add GROUP BY to produce per-group totals
- ✔ Use COALESCE(SUM(col), 0) if you need 0 instead of NULL for empty results
- ✔ SUM totals values; COUNT counts rows — different questions

# Guided Practice Quest

Work through the guided steps to calculate grand totals with SUM, filtered sums with WHERE, and per-group totals with GROUP BY, explaining the NULL behaviour at each step.

# Solo Practice Quest

A `sales` table has: `sale_id`, `customer_id`, `product_id`, `category`, `quantity`, `unit_price`, `discount_pct` (may be NULL), `sale_date`, `region`. Write five queries: (1) total revenue (quantity × unit_price) across all sales, (2) total revenue by category ordered highest to lowest, (3) total discounted revenue — treating NULL discount as 0% — per region, (4) monthly revenue for the current year grouped by month, (5) total revenue and total quantity sold per product (two SUM columns in one query). For each, explain what it measures and note any NULL handling required.

# Integration

**Mathematics**: SUM implements the mathematical summation operator Σ over a multiset. Given a relation R with numeric attribute A, SUM(A) computes Σ_{t ∈ R} t[A] (ignoring NULL, which represents an undefined value). With GROUP BY on attribute G, SUM partitions R into equivalence classes by G and computes Σ within each class — the indexed sum Σ_{t ∈ R : t[G] = g} t[A] for each distinct value g. This is the formal basis for all financial aggregation: revenue, cost, profit, and tax calculations reduce to Σ operations over partitioned relations.

**Sciences (Economics)**: National GDP accounting uses exactly the SUM pattern. GDP measured by the expenditure method is the sum of consumption (C), investment (I), government spending (G), and net exports (NX): GDP = C + I + G + (X - M). In a database of economic transactions, SELECT SUM(value) FROM transactions WHERE type = 'consumption' computes C; changing the WHERE clause computes each component. Macroeconomists and data engineers are doing the same operation — summing numeric values over filtered subsets — at different scales.

# Lore Conclusion

The Guild Treasury report showed three numbers: 1,247 outstanding invoices, 89,432 gold total owed, and a per-guild breakdown with the Merchants' Guild topping the list at 31,200 gold. "COUNT told us how many," the Accountant said. "SUM told us what they were worth." Master Selvaris closed the query. "Always know which question you are asking: how many, or how much. COUNT answers how many. SUM answers how much." She handed back the ledger. "They look similar. They measure completely different things. In finance, confusing them is expensive."

---
