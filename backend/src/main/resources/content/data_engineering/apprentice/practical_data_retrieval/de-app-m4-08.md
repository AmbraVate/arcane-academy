---
id: de-app-m4-08
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
lesson: reporting_scenarios
title: "Reporting Scenarios"
sortOrder: 8
difficulty: 3
estimatedMinutes: 30
xpReward: 45
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m4-07]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Writes a multi-table join query that produces a business report
    - Combines JOINs with GROUP BY, HAVING, and ORDER BY correctly
    - Selects appropriate join types for each table relationship in a report
    - Applies date filtering with WHERE in a join query
    - Reflects on how a multi-table join + aggregation query maps to a dashboard widget
  keywords: [report, dashboard, multi-table, join, GROUP BY, aggregate, date filter, WHERE, HAVING, business, metrics]
  modelAnswer: |
    Reporting queries combine multi-table joins with aggregation to produce the kind of data shown in dashboards and reports. The pattern is: FROM the central table, JOIN outward to lookup tables (categories, regions), JOIN to fact tables (order_lines), apply WHERE for date/status filters, GROUP BY the report dimensions, apply HAVING for thresholds, and ORDER BY for ranking. Each join type is chosen deliberately: INNER JOIN for required dimensions, LEFT JOIN to preserve all records in the report. Most dashboard metrics can be expressed as a single SQL query with this structure.
guidedSteps:
  - id: de-app-m4-08-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A monthly revenue report needs: region name, month, total orders, total revenue. Data is in: `orders` (region_id, order_date, total_amount), `regions` (region_id, name). Which is the correct query skeleton?
    inputConfig:
      options:
        - "SELECT r.name, SUM(o.total_amount) FROM orders o JOIN regions r ON o.region_id = r.region_id GROUP BY r.name;"
        - "SELECT r.name, EXTRACT(MONTH FROM o.order_date), COUNT(*), SUM(o.total_amount) FROM orders o JOIN regions r ON o.region_id = r.region_id GROUP BY r.name, EXTRACT(MONTH FROM o.order_date);"
        - "SELECT r.name, o.order_date, COUNT(*), SUM(o.total_amount) FROM orders o JOIN regions r ON o.region_id = r.region_id GROUP BY r.name;"
        - "SELECT r.name, SUM(o.total_amount) FROM regions r LEFT JOIN orders o ON r.region_id = o.region_id GROUP BY r.region_id, r.name;"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SELECT r.name, EXTRACT(MONTH FROM o.order_date), COUNT(*), SUM(o.total_amount) FROM orders o JOIN regions r ON o.region_id = r.region_id GROUP BY r.name, EXTRACT(MONTH FROM o.order_date);"]
      rejectedFeedback: "Option B correctly groups by both region name and month (EXTRACT(MONTH FROM order_date)), producing one row per (region, month) combination with order count and revenue. Option A only groups by region, collapsing all months. Option C groups by order_date (too granular — one row per day). Option D uses LEFT JOIN (useful if you want regions with no orders) but lacks the month dimension."
    hint: "The report needs one row per (region, month) — GROUP BY must include both dimensions."
    reflectionPrompt: "How would you extend this query to also group by year, so a multi-year report doesn't mix months across years?"
  - id: de-app-m4-08-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In a report query, date filtering belongs in the ________ clause (before grouping), while filtering on aggregate results (e.g. only regions with revenue > 10,000) belongs in HAVING.
    inputConfig:
      placeholder: "WHERE"
    markingRule:
      matchMode: CONTAINS
      accepted: [WHERE, where]
      rejectedFeedback: "WHERE filters individual rows before GROUP BY runs — the correct place for date filters (WHERE order_date >= '2026-01-01'), status filters (WHERE status = 'completed'), and other row-level conditions. HAVING filters groups after aggregation — the correct place for filters on aggregate values (HAVING SUM(revenue) > 10000). Date filtering in WHERE is also more efficient: it reduces the rows entering GROUP BY."
    hint: "Row-level filters run before grouping, using this clause."
    reflectionPrompt: "If you put the date filter in HAVING instead of WHERE, what would change about when rows are excluded?"
  - id: de-app-m4-08-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, describe how a single SQL query with JOINs and GROUP BY maps to a specific widget you might see on a business dashboard.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [dashboard, widget, bar chart, table, metric, GROUP BY, report, breakdown, row, dimension]
      rejectedFeedback: "A bar chart showing 'revenue per region' is a GROUP BY region query with SUM(revenue) — one SQL result row per bar. A table showing 'top 10 customers by spend this month' is a JOIN + WHERE date + GROUP BY customer + ORDER BY + LIMIT 10 query. A KPI card showing 'total orders this week' is a COUNT(*) + WHERE date query. Every dashboard widget is, at its core, a SQL query — usually a join + aggregation."
    hint: "Think of a specific chart type (bar chart, ranking table, KPI card) and describe the SQL behind it."
    reflectionPrompt: "What SQL would power a line chart showing monthly revenue for the past 12 months, broken down by region?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A sales report groups by salesperson and month, showing total revenue. Where does the filter 'only show completed sales' belong?"
    options:
      - "HAVING status = 'completed'"
      - "ON o.status = 'completed' (in the JOIN condition)"
      - "WHERE status = 'completed'"
      - "SELECT ... WHERE status = 'completed'"
    correctIndex: 2
    feedback: "WHERE status = 'completed' filters rows before GROUP BY — only completed sales are included in the aggregation. HAVING is for filters on aggregate results (e.g. HAVING SUM(revenue) > 1000). Putting it in ON is possible if it's on a LEFT JOIN and you want to preserve all salespeople even without completed sales, but for this scenario WHERE is correct and more efficient."
  - type: MULTIPLE_CHOICE
    question: "A report needs: department name, headcount, average salary, highest salary — grouped by department and ordered by headcount descending. How many aggregate functions are used?"
    options:
      - "1 (just AVG)"
      - "2 (AVG and MAX)"
      - "3 (COUNT, AVG, MAX)"
      - "4 (COUNT, SUM, AVG, MAX)"
    correctIndex: 2
    feedback: "Headcount = COUNT(*) or COUNT(employee_id). Average salary = AVG(salary). Highest salary = MAX(salary). That's three aggregate functions: COUNT, AVG, MAX. This is a common reporting pattern — multiple aggregates in a single GROUP BY query. They all operate independently within each department group."
retrieval:
  recall: "Write a monthly sales report for 2026: month, region, number of orders, total revenue, average order value — ordered by month then revenue."
  explain: "Explain how the structure of a JOIN + GROUP BY query maps to a bar chart or ranking table in a dashboard."
  mistakeId:
    code: "GROUP BY order_date when the report should show monthly totals"
    answer: "GROUP BY order_date groups by the exact date/timestamp — producing one row per day (or per second if a timestamp). Monthly totals require grouping by the month, not the raw date. The fix is: GROUP BY EXTRACT(YEAR FROM order_date), EXTRACT(MONTH FROM order_date), or DATE_TRUNC('month', order_date). This collapses all orders in the same month into one group, which is what a monthly report requires."
---

# Hook

Joins and aggregation come together in reporting queries — the kind that power dashboards, management summaries, and KPI widgets. A monthly revenue breakdown, a regional sales ranking, an employee performance summary — all built from the same pattern: JOINs to bring in dimensions, GROUP BY to create the report axes, aggregates to compute the metrics, WHERE and HAVING to filter.

This lesson builds several complete reporting queries from scratch.

# Lore Introduction

"The Guild Council meets monthly," the Treasurer said. "They want the same five reports: revenue by region, top-performing members, inventory turnover, new member acquisition trend, and overdue accounts." Master Selvaris opened a blank query file. "Each report is one SQL query," she said. "JOIN to bring in the labels. GROUP BY to create the dimensions. SUM and COUNT for the metrics. WHERE for the time filter. ORDER BY to rank." She built each query methodically. "Five queries. Run once per month. Council gets their data in under a minute." The Treasurer paused. "Previously it took three clerks three days." Selvaris closed the file. "Three clerks, three days, versus five SQL queries, forty seconds. The difference is knowing the pattern."

# Core Learning

## Concept Introduction

### The Reporting Query Pattern

```
FROM        central table
JOIN        dimension tables (region, category, department...)
LEFT JOIN   optional fact tables (order_lines, reviews...)
WHERE       row filters (date range, status, region...)
GROUP BY    report dimensions (region, month, department...)
HAVING      aggregate thresholds (only groups with revenue > X)
ORDER BY    ranking (highest first, alphabetical...)
LIMIT       top-N if needed
```

### Monthly Revenue by Region

```sql
SELECT
    r.name                              AS region,
    EXTRACT(YEAR FROM o.order_date)     AS year,
    EXTRACT(MONTH FROM o.order_date)    AS month,
    COUNT(o.order_id)                   AS order_count,
    SUM(o.total_amount)                 AS total_revenue,
    ROUND(AVG(o.total_amount), 2)       AS avg_order_value
FROM orders AS o
INNER JOIN regions AS r ON o.region_id = r.region_id
WHERE o.status = 'completed'
  AND o.order_date >= '2026-01-01'
GROUP BY r.name, EXTRACT(YEAR FROM o.order_date), EXTRACT(MONTH FROM o.order_date)
ORDER BY year, month, total_revenue DESC;
```

### Top Salesperson Report

```sql
SELECT
    e.employee_id,
    e.name                              AS salesperson,
    d.name                              AS department,
    COUNT(s.sale_id)                    AS sales_count,
    SUM(s.amount)                       AS total_sales,
    ROUND(AVG(s.amount), 2)             AS avg_sale
FROM employees AS e
INNER JOIN departments AS d ON e.department_id = d.department_id
LEFT JOIN sales AS s ON e.employee_id = s.employee_id
    AND s.sale_date >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY e.employee_id, e.name, d.name
HAVING COUNT(s.sale_id) > 0                  -- only employees with at least one sale
ORDER BY total_sales DESC
LIMIT 10;
```

### Inventory Report with Sales Velocity

```sql
SELECT
    p.product_id,
    p.name                              AS product,
    c.name                              AS category,
    p.stock_qty                         AS current_stock,
    COALESCE(SUM(ol.quantity), 0)       AS sold_last_30_days,
    CASE
        WHEN COALESCE(SUM(ol.quantity), 0) = 0 THEN 'No recent sales'
        WHEN p.stock_qty < SUM(ol.quantity) THEN 'Reorder urgent'
        ELSE 'OK'
    END                                 AS stock_status
FROM products AS p
INNER JOIN categories AS c ON p.category_id = c.category_id
LEFT JOIN order_lines AS ol ON p.product_id = ol.product_id
LEFT JOIN orders AS o ON ol.order_id = o.order_id
    AND o.order_date >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY p.product_id, p.name, c.name, p.stock_qty
ORDER BY sold_last_30_days DESC;
```

### New Member Acquisition Trend

```sql
SELECT
    EXTRACT(YEAR FROM created_at)   AS year,
    EXTRACT(MONTH FROM created_at)  AS month,
    COUNT(*)                        AS new_members
FROM members
GROUP BY EXTRACT(YEAR FROM created_at), EXTRACT(MONTH FROM created_at)
ORDER BY year, month;
```

Note: This query has no join — GROUP BY and aggregation alone produce a trend report when the data is in one table.

### CASE in Reports — Conditional Columns

```sql
-- Classify customers by lifetime value
SELECT
    c.customer_id,
    c.name,
    SUM(o.total_amount)         AS lifetime_value,
    CASE
        WHEN SUM(o.total_amount) > 5000 THEN 'VIP'
        WHEN SUM(o.total_amount) > 1000 THEN 'Regular'
        ELSE 'Occasional'
    END                         AS customer_tier
FROM customers AS c
LEFT JOIN orders AS o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.name
ORDER BY lifetime_value DESC NULLS LAST;
```

## Common Mistakes

- **GROUP BY order_date for monthly reports**: Groups by exact date, not month. Use `EXTRACT(MONTH FROM date)` or `DATE_TRUNC('month', date)`.
- **Forgetting to include the year in GROUP BY**: `GROUP BY EXTRACT(MONTH FROM date)` merges the same month across different years. Always include year: `GROUP BY year, month`.
- **Date filters in HAVING instead of WHERE**: Inefficient — all rows enter GROUP BY before being filtered. Date filters belong in WHERE.
- **Not using table aliases for all column references in multi-table reports**: `SELECT name` is ambiguous if multiple joined tables have a `name` column.

## Mental Model

A reporting query is a factory pipeline. Raw materials enter at FROM. JOIN stages add enriching information (labels, dimensions). WHERE is a quality filter that removes raw material that doesn't meet spec before processing. GROUP BY is the moulding press that shapes individual items into batches. Aggregate functions measure each batch. HAVING is the final quality gate that removes batches not meeting the output specification. ORDER BY is the conveyor belt that arranges the finished product for presentation.

## Mini Summary

- ✔ Pattern: FROM → JOIN dimensions → LEFT JOIN facts → WHERE filters → GROUP BY → HAVING → ORDER BY
- ✔ Date filters belong in WHERE (not HAVING) for efficiency
- ✔ GROUP BY on `EXTRACT(YEAR FROM date), EXTRACT(MONTH FROM date)` for monthly reports
- ✔ CASE expressions in SELECT add classification columns to reports
- ✔ Every dashboard widget is a SQL query — usually JOIN + GROUP BY + aggregate

# Guided Practice Quest

Work through the guided steps to build a monthly revenue report, add a regional dimension, apply a date filter, and extend to a ranking query with HAVING.

# Solo Practice Quest

You are building a management dashboard for a fictional e-commerce platform. Write six SQL queries that would power six dashboard widgets: (1) bar chart: monthly revenue for the last 6 months, (2) ranking table: top 5 product categories by revenue this month, (3) KPI card: total new customers this week vs last week (two separate queries or a UNION), (4) data table: all customers who have not ordered in 60+ days, with days since last order, (5) stacked bar: order count per status per region for the current quarter, (6) alert list: products with less than 10 units in stock that have been ordered in the last 7 days. For each, explain what the widget looks like and identify the key clauses that produce it.

# Integration

**Mathematics**: Reporting queries implement multidimensional aggregation — the mathematical foundation of OLAP (Online Analytical Processing). A report with dimensions {region, month} and measures {revenue, order_count} computes the GROUP BY on the Cartesian product of dimension values, applying measure functions to each cell. This is equivalent to computing a 2D matrix where rows are regions and columns are months, with each cell containing [revenue, order_count]. In the general case with k dimensions, the GROUP BY produces a k-dimensional cube (the "data cube" concept). The SQL GROUP BY on multiple columns is the relational implementation of this multidimensional aggregation operation.

**Sciences (Public Health — Disease Surveillance)**: Epidemiological reporting queries follow the same pattern: `SELECT region.name, EXTRACT(MONTH FROM report_date), disease.name, COUNT(case_id) AS case_count FROM disease_cases JOIN regions ON disease_cases.region_id = regions.region_id JOIN diseases ON disease_cases.disease_id = diseases.disease_id WHERE report_date >= CURRENT_DATE - INTERVAL '90 days' GROUP BY region.name, EXTRACT(MONTH FROM report_date), disease.name ORDER BY case_count DESC`. Public health dashboards — like those used during the COVID-19 pandemic — are powered by exactly this JOIN + GROUP BY pattern applied to surveillance databases with millions of case records.

# Lore Conclusion

Five reports. Forty seconds. The Guild Council received their monthly briefing: revenue trends, top performers, inventory status, acquisition trends, and overdue accounts — all from SQL queries built on the same JOIN + GROUP BY foundation. "Every number in these reports came from a structured query," Master Selvaris said. "No manual compilation. No risk of arithmetic error. Run next month, the same queries will produce updated results automatically." She closed the session file. "This is the power of SQL reporting. The query is the report logic — reusable, auditable, repeatable. Learn to write this pattern well, and you will be able to produce any business report from any database."

---
