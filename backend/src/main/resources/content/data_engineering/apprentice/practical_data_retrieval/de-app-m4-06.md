---
id: de-app-m4-06
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
lesson: customer_and_order_data
title: "Customer and Order Data"
sortOrder: 6
difficulty: 2
estimatedMinutes: 30
xpReward: 40
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m4-05]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Writes a multi-table join across customers, orders, and order_lines
    - Uses LEFT JOIN to include customers with no orders
    - Combines JOINs with GROUP BY and aggregate functions for summary reports
    - Applies WHERE and HAVING appropriately in join queries
    - Reflects on the N+1 problem and how joins solve it
  keywords: [customer, order, join, LEFT JOIN, GROUP BY, aggregate, SUM, COUNT, order_lines, multi-table, lifetime value]
  modelAnswer: |
    Customer and order data is the most common multi-table join scenario. The typical chain is customers → orders → order_lines, with foreign keys customer_id and order_id linking the tables. Combining this with GROUP BY and aggregates (COUNT, SUM) produces customer summaries: order counts, total spend, first/last order dates. LEFT JOIN is used when all customers must appear even if they have no orders. The N+1 problem — fetching a customer list then querying orders for each customer individually — is solved by a single join query with aggregation.
guidedSteps:
  - id: de-app-m4-06-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      To get total revenue per customer across all orders, which query structure is correct?
    inputConfig:
      options:
        - "SELECT customer_id, SUM(total_amount) FROM orders;"
        - "SELECT c.name, SUM(o.total_amount) FROM customers c INNER JOIN orders o ON c.customer_id = o.customer_id GROUP BY c.customer_id, c.name;"
        - "SELECT c.name, SUM(o.total_amount) FROM customers c INNER JOIN orders o ON c.customer_id = o.customer_id;"
        - "SELECT c.name, o.total_amount FROM customers c INNER JOIN orders o ON c.customer_id = o.customer_id GROUP BY c.name;"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SELECT c.name, SUM(o.total_amount) FROM customers c INNER JOIN orders o ON c.customer_id = o.customer_id GROUP BY c.customer_id, c.name;"]
      rejectedFeedback: "Option B is correct: INNER JOIN links customers to orders, SUM(o.total_amount) totals the orders, and GROUP BY c.customer_id, c.name groups the result per customer. Option A lacks the customer name (no join). Option C is invalid — SUM without GROUP BY when c.name is also in SELECT. Option D groups by c.name only, which can merge different customers with the same name."
    hint: "JOIN brings in the customer name, GROUP BY on the customer key produces one row per customer, SUM totals their orders."
    reflectionPrompt: "Why should GROUP BY include c.customer_id in addition to c.name — what problem does it prevent?"
  - id: de-app-m4-06-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To find customers who spent more than 500 total, you add to the GROUP BY query: ________ SUM(o.total_amount) > 500
    inputConfig:
      placeholder: "HAVING"
    markingRule:
      matchMode: CONTAINS
      accepted: [HAVING, having]
      rejectedFeedback: "HAVING SUM(o.total_amount) > 500 filters groups after aggregation — keeping only customer groups where the total across all their orders exceeds 500. WHERE would run before GROUP BY and before SUM is computed, so WHERE SUM(...) > 500 would be an error. The pattern is: JOIN + GROUP BY + HAVING for filtered aggregate results."
    hint: "This filter is on an aggregate result (SUM) and must run after GROUP BY."
    reflectionPrompt: "How would you also filter to only include completed orders — where does that filter go?"
  - id: de-app-m4-06-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain the N+1 query problem and how a JOIN with GROUP BY solves it for customer order summaries.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [N+1, loop, each, separate, query, queries, application, code, one query, join, aggregate]
      rejectedFeedback: "The N+1 problem: first query fetches N customers, then for each customer a separate query fetches their orders — N+1 total queries. With 10,000 customers, this is 10,001 database round trips. A single JOIN + GROUP BY query fetches all customers and their aggregated order data in one trip to the database: one round trip, regardless of how many customers. This is dramatically more efficient and is why SQL aggregation exists."
    hint: "Think about what would happen if a web application fetched customer data in a loop instead of with a single join."
    reflectionPrompt: "If each database query takes 5ms, how long does N+1 take for 10,000 customers vs one join?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which query returns all customers with their order count, showing 0 for customers with no orders?"
    options:
      - "SELECT c.name, COUNT(*) FROM customers c INNER JOIN orders o ON c.customer_id = o.customer_id GROUP BY c.name;"
      - "SELECT c.name, COUNT(o.order_id) FROM customers c LEFT JOIN orders o ON c.customer_id = o.customer_id GROUP BY c.customer_id, c.name;"
      - "SELECT c.name, COUNT(*) FROM customers c LEFT JOIN orders o ON c.customer_id = o.customer_id GROUP BY c.customer_id, c.name;"
      - "SELECT c.name, COUNT(o.order_id) FROM customers c INNER JOIN orders o ON c.customer_id = o.customer_id GROUP BY c.customer_id, c.name;"
    correctIndex: 1
    feedback: "Option B is correct: LEFT JOIN keeps all customers, COUNT(o.order_id) returns 0 for unmatched rows (NULL order_id is not counted), GROUP BY on customer_id+name groups correctly. Option A uses INNER JOIN (excludes zero-order customers) and COUNT(*) (would return 1 for unmatched rows). Option C uses COUNT(*) which counts the NULL row as 1. Option D uses INNER JOIN (excludes zero-order customers)."
  - type: MULTIPLE_CHOICE
    question: "A query joins customers → orders → order_lines. The ON clause for the second JOIN should be:"
    options:
      - "ON customers.customer_id = order_lines.customer_id"
      - "ON order_lines.order_id = customers.customer_id"
      - "ON order_lines.order_id = orders.order_id"
      - "ON customers.order_id = order_lines.order_id"
    correctIndex: 2
    feedback: "The second JOIN adds order_lines to the query that already has customers and orders. Order_lines links to orders via order_id: ON order_lines.order_id = orders.order_id. Order_lines does not have a direct customer_id — it links through orders. Each JOIN in a chain adds one table at a time using the appropriate foreign key."
retrieval:
  recall: "Write a customer lifetime value report: customer name, first order date, most recent order date, total orders, total spend — for customers who have placed at least 2 orders."
  explain: "Explain the three-table join chain for customers → orders → order_lines, including the ON clause for each JOIN."
  mistakeId:
    code: "looping through 10,000 customers in application code and running a separate COUNT query for each to get their order count"
    answer: "This is the N+1 problem: 10,001 database queries instead of 1. The correct approach is a single SQL query with JOIN and GROUP BY: SELECT c.customer_id, COUNT(o.order_id) FROM customers c LEFT JOIN orders o ON c.customer_id = o.customer_id GROUP BY c.customer_id. One round trip, all results returned at once. For 10,000 customers at 5ms per query, N+1 takes ~50 seconds; the join query takes milliseconds."
---

# Hook

Customer and order data is the classic relational join scenario — almost every business application has customers, orders, and line items. Understanding how to query across this three-table structure efficiently is a fundamental data engineering skill.

This lesson applies the join types you have learned to real retrieval patterns: customer summaries, order histories, high-value customers, and the N+1 problem.

# Lore Introduction

"The merchant guild's reporting wizard is broken," the Guild Secretary said. "It runs one query per member to get their transaction count, then another to get their total. For 3,000 members it takes four minutes." Master Selvaris examined the code. "N+1 queries," she said. "3,001 database trips when one would do." She replaced the loop with a single JOIN and GROUP BY. "One query. All 3,000 members. Order counts, totals, first and last transaction dates. All in one result set." The Secretary ran it. "Under a second." Selvaris closed the old code. "When you find yourself writing a loop that queries the database for each row, the answer is almost always a join."

# Core Learning

## Concept Introduction

### The Standard Customer-Order Schema

```sql
customers:   customer_id (PK), name, email, country, created_at
orders:      order_id (PK), customer_id (FK), order_date, status, total_amount
order_lines: line_id (PK), order_id (FK), product_id (FK), quantity, unit_price
```

Foreign key chain: order_lines.order_id → orders.order_id → customers.customer_id

### Basic Customer Order Join

```sql
-- All orders with customer name
SELECT
    c.customer_id,
    c.name          AS customer_name,
    o.order_id,
    o.order_date,
    o.status,
    o.total_amount
FROM customers AS c
INNER JOIN orders AS o ON c.customer_id = o.customer_id
ORDER BY c.name, o.order_date DESC;
```

### Customer Order Summary (LEFT JOIN + Aggregation)

```sql
-- All customers with order stats (0 for customers with no orders)
SELECT
    c.customer_id,
    c.name,
    c.email,
    COUNT(o.order_id)                       AS order_count,
    COALESCE(SUM(o.total_amount), 0)        AS lifetime_value,
    MIN(o.order_date)                       AS first_order,
    MAX(o.order_date)                       AS last_order
FROM customers AS c
LEFT JOIN orders AS o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.name, c.email
ORDER BY lifetime_value DESC;
```

### Three-Table Join: Customer → Order → Lines

```sql
-- Full order detail: customer name + order info + line items
SELECT
    c.name          AS customer_name,
    o.order_id,
    o.order_date,
    p.product_id,
    ol.quantity,
    ol.unit_price,
    ol.quantity * ol.unit_price AS line_total
FROM order_lines AS ol
INNER JOIN orders AS o      ON ol.order_id    = o.order_id
INNER JOIN customers AS c   ON o.customer_id  = c.customer_id
INNER JOIN products AS p    ON ol.product_id  = p.product_id
ORDER BY o.order_id, ol.line_id;
```

### High-Value Customer Identification

```sql
-- Customers who have spent more than 1,000 total on completed orders
SELECT
    c.customer_id,
    c.name,
    c.email,
    COUNT(o.order_id)           AS orders_placed,
    SUM(o.total_amount)         AS total_spent
FROM customers AS c
INNER JOIN orders AS o ON c.customer_id = o.customer_id
WHERE o.status = 'completed'
GROUP BY c.customer_id, c.name, c.email
HAVING SUM(o.total_amount) > 1000
ORDER BY total_spent DESC;
```

### Customers Who Haven't Ordered Recently

```sql
-- Customers with no order in the last 90 days
SELECT c.customer_id, c.name, c.email, MAX(o.order_date) AS last_order_date
FROM customers AS c
LEFT JOIN orders AS o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.name, c.email
HAVING MAX(o.order_date) < CURRENT_DATE - INTERVAL '90 days'
    OR MAX(o.order_date) IS NULL;   -- also include customers who have never ordered
```

### The N+1 Anti-Pattern

```sql
-- WRONG (N+1 queries in application code):
-- 1. SELECT customer_id, name FROM customers   → 10,000 rows
-- 2. For each customer: SELECT COUNT(*) FROM orders WHERE customer_id = ?
-- → 10,001 database queries

-- CORRECT (1 query):
SELECT c.customer_id, c.name, COUNT(o.order_id) AS order_count
FROM customers AS c
LEFT JOIN orders AS o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.name;
-- → 1 database query, all results in one go
```

## Why It Matters

Customers and orders form the most queried relationship in business databases, and joining them correctly underpins nearly every commercial question:

- "Which customers haven't ordered this year?" requires a LEFT JOIN with a NULL check — an INNER JOIN silently hides exactly the customers you're looking for
- Revenue-per-customer, repeat-purchase rates, and churn all start with this join done at the right grain
- One customer with many orders means join results multiply rows; aggregating without realising that inflates every number

This pairing is where join theory meets the queries you will actually write in week one of a data job.

## Common Mistakes

- **GROUP BY only the name column**: Two customers named "Alice Smith" get merged. Always GROUP BY the primary key: `GROUP BY c.customer_id, c.name`.
- **COUNT(*) for zero-order customers after LEFT JOIN**: Returns 1 instead of 0 for unmatched rows. Use `COUNT(o.order_id)`.
- **WHERE on orders status converting LEFT JOIN to INNER JOIN**: Move right-table WHERE filters into the ON clause to preserve all customers.
- **Forgetting GROUP BY when mixing customer columns and aggregates**: `SELECT c.name, SUM(o.total_amount)` without GROUP BY is an error.

## Mental Model

Picture the join as assembling a report card. Customers is the class register — every student must appear. Orders is the grade book — one entry per exam. The LEFT JOIN matches each student to their grades. GROUP BY folds all the grades for one student into one row. SUM adds up the marks, COUNT counts the exams, MAX/MIN find the best/worst. The result is one report card row per student, summarising all their work. HAVING then filters to only the students meeting a threshold — honour roll, failing list, at-risk students.

## Mini Summary

- ✔ `customers LEFT JOIN orders` preserves all customers, NULLs for those with no orders
- ✔ `GROUP BY c.customer_id, c.name` produces one summary row per customer
- ✔ Use `COUNT(o.order_id)` not `COUNT(*)` for zero-order customers
- ✔ Three-table chain: `order_lines JOIN orders JOIN customers` follows the FK chain
- ✔ Replace N+1 loops with a single JOIN + GROUP BY query

# Guided Practice Quest

Work through the guided steps to build a customer order summary using LEFT JOIN, count orders correctly with COUNT(column), filter by status in the ON clause, and identify high-value customers with HAVING.

# Solo Practice Quest

Using `customers` (customer_id, name, email, country, created_at), `orders` (order_id, customer_id, order_date, status, total_amount), and `order_lines` (line_id, order_id, product_id, quantity, unit_price): write six queries: (1) all customers with their total order count and total spend (0 for no orders), (2) top 10 customers by lifetime value who have placed at least 3 orders, (3) customers in the UK who have not ordered in the last 180 days, (4) the three most recent completed orders for each customer (using a subquery or window function hint), (5) total revenue per customer per year, (6) a complete three-table query showing each order line with customer name, order date, product_id, quantity, and line total. For each query, identify which join type you used and why.

# Integration

**Mathematics**: The customer-order join implements a chain of relational compositions. If we model customers as C, orders as O, and order_lines as L, the three-table join is the composition: L ⋈ O ⋈ C — joining L to O on order_id, then the result to C on customer_id. This is associative (the order of joins doesn't affect the result, though it affects performance). The aggregation on top — GROUP BY customer_id, SUM(amount) — applies a partition map followed by a summation homomorphism. In linear algebra terms, this is projecting the joined relation onto the customer dimension and summing the value dimension — equivalent to a matrix row-sum after filtering.

**Sciences (Economics — Customer Behaviour Analysis)**: The customer-order join query pattern is the foundation of RFM analysis (Recency, Frequency, Monetary value) — the standard technique for customer segmentation in e-commerce. Recency = MAX(order_date), Frequency = COUNT(order_id), Monetary = SUM(total_amount) per customer. `SELECT c.customer_id, MAX(o.order_date) AS recency, COUNT(o.order_id) AS frequency, SUM(o.total_amount) AS monetary FROM customers c LEFT JOIN orders o ON c.customer_id = o.customer_id GROUP BY c.customer_id` is the direct SQL implementation of RFM. Every e-commerce system — from small retailers to Amazon — computes this exact JOIN + GROUP BY query for marketing segmentation.

# Lore Conclusion

The Guild Secretary's reporting system now ran one query in under a second. Customer summaries, order counts, total transactions, first and last activity dates — all from a single LEFT JOIN with GROUP BY. "The N+1 loop was querying the database 3,001 times," Master Selvaris said. "The join query does it once." She ran the high-value customer filter. "These 47 members have spent over 500 gold and have not transacted in 90 days. This is your re-engagement list." She closed the session. "Customer and order data is the most common join scenario you will encounter. Learn to write it fluently. JOIN + GROUP BY + HAVING is the pattern that drives almost every business dashboard."

---
