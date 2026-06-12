---
id: de-app-m3-10
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
lesson: count
title: "COUNT"
sortOrder: 10
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m3-09]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Distinguishes between COUNT(*), COUNT(column), and COUNT(DISTINCT column)
    - Explains why COUNT(*) counts all rows including NULLs, while COUNT(column) skips NULLs
    - Uses COUNT with WHERE to count filtered subsets
    - Combines COUNT with GROUP BY to count per group
    - Reflects on the difference between counting rows and counting distinct values
  keywords: [COUNT, aggregate, NULL, DISTINCT, rows, group, total, filter, WHERE]
  modelAnswer: |
    COUNT is an aggregate function that counts rows. COUNT(*) counts all rows including those with NULL values. COUNT(column) counts only rows where that column is not NULL — skipping NULLs. COUNT(DISTINCT column) counts unique non-NULL values. When used with GROUP BY, COUNT produces a count per group rather than a single total. COUNT is commonly combined with WHERE for conditional counts and with HAVING to filter groups by count. Understanding the NULL behaviour is critical: COUNT(*) and COUNT(column) can return very different values on the same table.
guidedSteps:
  - id: de-app-m3-10-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A `customers` table has 1000 rows. 200 rows have NULL in the `phone` column. What does `COUNT(phone)` return?
    inputConfig:
      options:
        - "1000"
        - "800"
        - "200"
        - "0"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["800"]
      rejectedFeedback: "COUNT(column) counts only non-NULL values. With 200 NULL phone values, COUNT(phone) = 1000 - 200 = 800. COUNT(*) would return 1000 (counts all rows regardless of NULLs). COUNT(DISTINCT phone) would return the number of unique non-NULL phone values."
    hint: "COUNT(column) skips NULLs. COUNT(*) counts every row."
    reflectionPrompt: "When would it matter to use COUNT(phone) instead of COUNT(*) — what business question would that answer?"
  - id: de-app-m3-10-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To count the number of unique countries in a `customers` table, you use: `SELECT COUNT(________ country) FROM customers;`
    inputConfig:
      placeholder: "DISTINCT"
    markingRule:
      matchMode: CONTAINS
      accepted: [DISTINCT, distinct]
      rejectedFeedback: "COUNT(DISTINCT country) counts the number of unique, non-NULL country values in the table. If 5,000 customers come from 47 different countries, COUNT(DISTINCT country) = 47. Without DISTINCT, COUNT(country) = 5,000 (counting every row with a non-NULL country)."
    hint: "The keyword that removes duplicates before counting."
    reflectionPrompt: "A table has 10,000 rows but only 50 unique product categories. What does COUNT(DISTINCT category) return?"
  - id: de-app-m3-10-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain what the following query does and what it returns: `SELECT COUNT(*) FROM orders WHERE status = 'pending';`
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [pending, filter, WHERE, count, rows, number, orders, status]
      rejectedFeedback: "This query counts the total number of rows in the orders table where status = 'pending'. The WHERE clause filters rows before COUNT aggregates them — so only pending orders are counted. It returns a single row with a single number: the count of pending orders. This is the pattern for 'how many X satisfy condition Y'."
    hint: "Read the query clause by clause: FROM orders, WHERE status = 'pending', SELECT COUNT(*)."
    reflectionPrompt: "How would you modify this query to count pending orders placed in the last 30 days?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `SELECT COUNT(*) FROM products` return if the products table has 500 rows, 20 of which have a NULL name?"
    options: ["480", "500", "20", "It returns an error"]
    correctIndex: 1
    feedback: "COUNT(*) counts all rows regardless of NULL values in any column — it counts the row itself, not a specific column. 500 rows total means COUNT(*) = 500. COUNT(name) would return 480 (skipping the 20 NULLs)."
  - type: MULTIPLE_CHOICE
    question: "Which query returns the number of orders for each customer?"
    options:
      - "SELECT COUNT(*) FROM orders;"
      - "SELECT customer_id, COUNT(*) FROM orders;"
      - "SELECT customer_id, COUNT(*) FROM orders GROUP BY customer_id;"
      - "SELECT COUNT(customer_id) FROM orders GROUP BY customer_id;"
    correctIndex: 2
    feedback: "To get a count per group, you need GROUP BY. Without it, COUNT(*) returns a single grand total. SELECT customer_id, COUNT(*) without GROUP BY is an error in standard SQL (non-aggregate column mixed with aggregate). GROUP BY customer_id groups the rows by customer first, then COUNT(*) counts within each group."
retrieval:
  recall: "Explain the difference between COUNT(*), COUNT(column), and COUNT(DISTINCT column)."
  explain: "Explain how COUNT works differently with and without GROUP BY."
  mistakeId:
    code: "SELECT COUNT(*) FROM orders to check how many customers have ordered"
    answer: "COUNT(*) FROM orders counts order rows, not unique customers. A customer who placed 10 orders is counted 10 times. To count unique customers who have placed orders, use COUNT(DISTINCT customer_id) FROM orders — this counts each customer once regardless of how many orders they have."
---

# Hook

"How many customers do we have?" "How many orders are pending?" "How many unique countries are our customers from?" These questions all have something in common: the answer is a single number derived from many rows of data. That is aggregation — and COUNT is where it starts.

COUNT is the most frequently used aggregate function in SQL. It answers "how many" — of everything, of a specific column, of unique values, per group.

# Lore Introduction

"The Guild Council wants a summary," the Archivist said. "Total members. Members by rank. Members with no listed specialisation." Master Selvaris wrote three queries in sequence. `SELECT COUNT(*) FROM members;` — total members. `SELECT rank, COUNT(*) FROM members GROUP BY rank;` — members by rank. `SELECT COUNT(*) - COUNT(specialisation) FROM members;` — members with no specialisation (the difference between total rows and non-NULL specialisation count). "COUNT is the foundation of every summary report," she said. "It turns millions of rows into the numbers that matter."

# Core Learning

## Concept Introduction

### COUNT Variants

```sql
COUNT(*)             -- counts ALL rows, including rows with NULL values
COUNT(column_name)   -- counts rows where column_name is NOT NULL
COUNT(DISTINCT col)  -- counts unique, non-NULL values in column_name
```

```sql
-- Total rows in the table
SELECT COUNT(*) FROM orders;               -- e.g. 50,000

-- Rows where email is not NULL
SELECT COUNT(email) FROM customers;        -- e.g. 48,320 (some emails missing)

-- How many unique email domains are there?
SELECT COUNT(DISTINCT domain) FROM customers;   -- e.g. 1,204
```

### COUNT with WHERE

```sql
-- Count pending orders only
SELECT COUNT(*) AS pending_count
FROM orders
WHERE status = 'pending';

-- Count customers from the UK
SELECT COUNT(*) AS uk_customers
FROM customers
WHERE country = 'UK';

-- Count products below minimum stock
SELECT COUNT(*) AS low_stock_count
FROM products
WHERE stock_qty < 10;
```

### COUNT with GROUP BY

Without GROUP BY: one number for the whole table.
With GROUP BY: one number per group.

```sql
-- Total orders (one number)
SELECT COUNT(*) AS total FROM orders;

-- Orders per status (one number per status value)
SELECT status, COUNT(*) AS order_count
FROM orders
GROUP BY status
ORDER BY order_count DESC;
```

Result:
```
status      | order_count
delivered   | 31,200
shipped     | 9,400
pending     | 6,800
cancelled   | 2,600
```

```sql
-- Orders per customer (one number per customer)
SELECT customer_id, COUNT(*) AS orders_placed
FROM orders
GROUP BY customer_id
ORDER BY orders_placed DESC
LIMIT 10;
-- Returns the 10 customers who have placed the most orders
```

### Counting NULL Values

```sql
-- How many rows have a NULL phone number?
SELECT COUNT(*) - COUNT(phone) AS missing_phone_count
FROM customers;

-- What percentage have a phone number?
SELECT
    COUNT(phone) AS with_phone,
    COUNT(*) AS total,
    ROUND(COUNT(phone) * 100.0 / COUNT(*), 1) AS pct_with_phone
FROM customers;
```

## Why It Matters

Counting rows sounds trivial until a wrong count costs real money. COUNT is the most-used aggregate in production systems, and the subtle differences between its variants cause real incidents:

- Dashboards that report `COUNT(*)` when the business asked for *distinct customers* silently inflate metrics
- `COUNT(column)` skips NULLs — a quiet rule that makes "customers with phone numbers" and "all customers" different numbers
- Billing, capacity planning, and alerting all start with an accurate count; get it wrong and every downstream decision inherits the error

Mastering COUNT's variants is the difference between answering "how many?" and answering the question that was actually asked.

## Common Mistakes

- **`COUNT(*)` vs `COUNT(column)`**: They differ when NULLs are present. Know which one answers your business question.
- **`COUNT(DISTINCT *)` is not valid**: You cannot use DISTINCT with *. Use COUNT(DISTINCT column).
- **Using COUNT without GROUP BY for per-group counts**: Mixing a non-aggregated column with COUNT(*) without GROUP BY is an error in standard SQL.

## Mental Model

Think of COUNT as a census counter. COUNT(*) counts every person in the building, whether or not they have a name tag. COUNT(name) counts only people with name tags — those without are invisible to it. COUNT(DISTINCT name) counts unique names — three people named "Alice" count as one. GROUP BY divides the building into rooms and runs a separate census in each room.

## Mini Summary

- ✔ `COUNT(*)` — all rows (NULLs included)
- ✔ `COUNT(col)` — non-NULL rows only
- ✔ `COUNT(DISTINCT col)` — unique non-NULL values
- ✔ Add WHERE to filter before counting
- ✔ Add GROUP BY to count per group

# Guided Practice Quest

Work through the guided steps to write COUNT queries for totals, filtered counts, and per-group counts, explaining the NULL behaviour in each case.

# Solo Practice Quest

A `support_tickets` table has: `ticket_id`, `customer_id`, `category`, `status` (open/closed/escalated), `assigned_to` (may be NULL), `created_at`, `resolved_at` (may be NULL). Write six COUNT queries: (1) total tickets, (2) open tickets, (3) tickets with no assigned agent, (4) tickets per category, (5) tickets per status per category (two GROUP BY columns), (6) the percentage of tickets that have been resolved. For each query, explain the business question it answers and note any NULL handling considerations.

# Integration

**Mathematics**: COUNT is a cardinality function in the mathematical sense. COUNT(*) computes |R| — the cardinality of the relation R (the number of tuples). COUNT(column) computes |{t ∈ R : t[column] ≠ NULL}| — the cardinality of the subset of tuples where the attribute is defined. COUNT(DISTINCT column) computes |{t[column] : t ∈ R, t[column] ≠ NULL}| — the cardinality of the set of distinct non-NULL attribute values. GROUP BY partitions the relation into equivalence classes and applies COUNT to each class independently — a set-partition followed by per-partition cardinality measurement.

**Sciences (Ecology)**: Population counts are fundamental to ecology — estimating the size of animal populations, measuring biodiversity, tracking species distribution. COUNT(*) corresponds to total census counting; COUNT(DISTINCT species_id) corresponds to species richness (the number of distinct species in an area); GROUP BY region, COUNT(DISTINCT species_id) computes species richness per region. Mark-recapture studies use more sophisticated cardinality estimation when full enumeration is impossible — exactly analogous to COUNT(DISTINCT) estimation techniques used in large-scale database analytics.

# Lore Conclusion

The Guild Council received their summary: 1,247 total members, distributed across seven ranks, 89 with no listed specialisation. "These three numbers came from three COUNT queries," Master Selvaris said. "Count everything. Count what's non-null. Count the difference." She turned to her apprentice. "COUNT is simple — but it is the foundation of every summary report ever produced. Every dashboard number, every KPI, every 'how many' question is answered with COUNT. Learn it well. You will use it every day."

---
