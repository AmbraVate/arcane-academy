---
id: de-app-m3-05
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m3
moduleTitle: "Module 3: SQL Foundations"
moduleGlyph: "🔍"
moduleSortOrder: 3
topicSlug: reading_data
topicTitle: "Reading Data"
topicSortOrder: 1
lesson: limiting_results
title: "Limiting Results"
sortOrder: 5
difficulty: 1
estimatedMinutes: 15
xpReward: 25
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m3-04]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Writes correct LIMIT and OFFSET clauses
    - Explains why LIMIT without ORDER BY produces non-deterministic results
    - Describes how LIMIT and OFFSET combine to implement pagination
    - Explains the performance trade-off of large OFFSET values
    - Distinguishes between LIMIT syntax in different database systems
  keywords: [LIMIT, OFFSET, TOP, FETCH, pagination, rows, performance, non-deterministic, page]
  modelAnswer: |
    LIMIT restricts the number of rows returned by a query. OFFSET skips a specified number of rows before returning. Together they implement pagination: LIMIT 10 OFFSET 20 returns rows 21-30. LIMIT without ORDER BY produces non-deterministic results — you get some rows but not necessarily consistent ones. Large OFFSET values are slow because the database must generate and skip all preceding rows. Different databases have different syntax: LIMIT N (PostgreSQL, MySQL), TOP N (SQL Server), FETCH FIRST N ROWS ONLY (standard SQL). LIMIT is commonly used for top-N queries (find the 5 most expensive products) and for paginating large result sets in UIs.
guidedSteps:
  - id: de-app-m3-05-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which query correctly returns the 3 most expensive products?
    inputConfig:
      options:
        - "SELECT name, price FROM products LIMIT 3;"
        - "SELECT name, price FROM products ORDER BY price DESC LIMIT 3;"
        - "SELECT name, price FROM products WHERE price > 0 LIMIT 3;"
        - "SELECT TOP 3 name, price FROM products;"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SELECT name, price FROM products ORDER BY price DESC LIMIT 3;"]
      rejectedFeedback: "LIMIT alone (option A) without ORDER BY returns any 3 rows — not necessarily the most expensive. You must combine ORDER BY price DESC (most expensive first) with LIMIT 3 to reliably get the top 3. Option D uses SQL Server syntax (TOP) rather than standard LIMIT, and also lacks ORDER BY."
    hint: "LIMIT only makes sense when combined with ORDER BY — otherwise 'top 3' is meaningless."
    reflectionPrompt: "What would the result look like if you used LIMIT 3 without ORDER BY on a million-row table?"
  - id: de-app-m3-05-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To retrieve page 3 of results, where each page shows 10 rows, you would use `LIMIT 10 OFFSET ________`.
    inputConfig:
      placeholder: "20"
    markingRule:
      matchMode: CONTAINS
      accepted: ["20"]
      rejectedFeedback: "Page 3 starts after 20 rows (page 1: rows 1-10, page 2: rows 11-20, page 3: rows 21-30). OFFSET 20 skips the first 20 rows. The formula is: OFFSET = (page_number - 1) × page_size. For page 3 with page_size 10: OFFSET = (3-1) × 10 = 20."
    hint: "Page 1 skips 0 rows. Page 2 skips 10. What does page 3 skip?"
    reflectionPrompt: "How does the OFFSET formula generalise to any page number and page size?"
  - id: de-app-m3-05-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why using a large OFFSET value (e.g. OFFSET 100000) is slow in SQL.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [generate, scan, skip, rows, all, preceding, discard, performance, slow, offset, cursor]
      rejectedFeedback: "SQL databases implementing OFFSET must generate all the rows up to the offset value before discarding them. For OFFSET 100000 LIMIT 10, the database generates 100,010 rows, discards the first 100,000, and returns 10. There is no shortcut: even with an index, the database must traverse 100,000 entries. On large tables with large offsets, this becomes increasingly slow — a common performance problem in infinite-scroll UIs."
    hint: "The database cannot jump directly to row 100,001 — it must count through all rows before it."
    reflectionPrompt: "What alternative to OFFSET-based pagination avoids this performance problem for large datasets?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `LIMIT 5 OFFSET 10` return?"
    options:
      - "The first 5 rows, skipping every 10th row"
      - "Rows 11 through 15 (skip 10, then return 5)"
      - "The first 10 rows and then 5 more"
      - "5 rows from position 10 to the end"
    correctIndex: 1
    feedback: "OFFSET 10 skips the first 10 rows. LIMIT 5 then returns the next 5 rows. So you get rows 11, 12, 13, 14, 15 (1-indexed). This is the pagination pattern: OFFSET = (page - 1) × page_size, LIMIT = page_size."
  - type: MULTIPLE_CHOICE
    question: "In SQL Server, how do you retrieve the top 10 rows (the SQL Server equivalent of LIMIT 10)?"
    options:
      - "LIMIT 10"
      - "FETCH FIRST 10 ROWS"
      - "SELECT TOP 10 ..."
      - "ROWNUM <= 10"
    correctIndex: 2
    feedback: "SQL Server uses SELECT TOP N syntax. PostgreSQL and MySQL use LIMIT N. Standard SQL (supported by PostgreSQL) uses FETCH FIRST N ROWS ONLY. Oracle uses ROWNUM or FETCH FIRST. The concept is the same; the syntax differs by database."
retrieval:
  recall: "Write a query that returns the 5 cheapest in-stock products from a products table."
  explain: "Explain why LIMIT without ORDER BY produces non-deterministic results."
  mistakeId:
    code: "SELECT * FROM orders LIMIT 10"
    answer: "Without ORDER BY, LIMIT returns an arbitrary 10 rows — not the 10 most recent, largest, or any meaningful 'top 10'. The result can change between runs as the query execution plan varies. Always pair LIMIT with ORDER BY to make the selection deterministic and meaningful."
---

# Hook

Not every query should return every matching row. A homepage showing "latest 5 blog posts" needs exactly 5 rows. A paginated product list shows 20 items per page. A leaderboard shows only the top 10. An admin alert shows the first 100 unprocessed items.

LIMIT (and its equivalents in other databases) is how you control the number of rows returned. It is simple, widely used, and almost always needs to be paired with ORDER BY to be meaningful.

# Lore Introduction

"The Archivist wants the five most recently acquired scrolls," Master Selvaris said. "Not all scrolls. Not all recent scrolls. The top five." She added `ORDER BY acquired_at DESC LIMIT 5` to the query. "Without the LIMIT, the archive returns every scroll ever acquired — ordered by date, but all of them. With LIMIT 5, it stops after returning the first five." She ran the query. "Five rows. The five most recent acquisitions." She looked at her apprentice. "LIMIT is how you communicate to the archive: 'I only need this many.' It saves time, memory, and bandwidth."

# Core Learning

## Concept Introduction

### LIMIT Syntax

```sql
-- PostgreSQL, MySQL, SQLite
SELECT column1, column2
FROM table_name
ORDER BY sort_column DESC
LIMIT n;

-- SQL Server
SELECT TOP n column1, column2
FROM table_name
ORDER BY sort_column DESC;

-- Standard SQL (also supported in PostgreSQL)
SELECT column1, column2
FROM table_name
ORDER BY sort_column DESC
FETCH FIRST n ROWS ONLY;
```

### LIMIT + ORDER BY — Always Together

```sql
-- Top 5 most expensive products
SELECT name, unit_price
FROM products
ORDER BY unit_price DESC
LIMIT 5;

-- 10 most recent orders
SELECT order_id, customer_id, order_date
FROM orders
ORDER BY order_date DESC
LIMIT 10;

-- Lowest-rated product to prioritise for review
SELECT name, rating
FROM products
WHERE rating IS NOT NULL
ORDER BY rating ASC
LIMIT 1;
```

### Pagination with LIMIT and OFFSET

```sql
-- Page 1: rows 1–10
SELECT product_id, name FROM products ORDER BY name ASC LIMIT 10 OFFSET 0;

-- Page 2: rows 11–20
SELECT product_id, name FROM products ORDER BY name ASC LIMIT 10 OFFSET 10;

-- Page 3: rows 21–30
SELECT product_id, name FROM products ORDER BY name ASC LIMIT 10 OFFSET 20;

-- Formula: OFFSET = (page_number - 1) * page_size
```

### The OFFSET Performance Problem

OFFSET works by generating all rows up to the offset, then discarding them:

```
LIMIT 10 OFFSET 0:    generate 10 rows, return 10   (fast)
LIMIT 10 OFFSET 100:  generate 110 rows, discard 100, return 10
LIMIT 10 OFFSET 10000: generate 10010 rows, discard 10000, return 10 (slow)
LIMIT 10 OFFSET 100000: generate 100010 rows, discard 100000, return 10 (very slow)
```

For large datasets or deep pagination, consider **keyset/cursor pagination** instead:

```sql
-- Instead of OFFSET, use the last seen ID to continue from where you left off
SELECT product_id, name
FROM products
WHERE product_id > :last_seen_id   -- continue from cursor
ORDER BY product_id ASC
LIMIT 10;
```

This scans only the needed rows regardless of how deep into the result set you are.

### Database Syntax Reference

| Database | Syntax |
|---|---|
| PostgreSQL | `LIMIT n OFFSET m` |
| MySQL | `LIMIT n OFFSET m` or `LIMIT m, n` |
| SQLite | `LIMIT n OFFSET m` |
| SQL Server | `SELECT TOP n ...` or `FETCH FIRST n ROWS ONLY` |
| Oracle | `FETCH FIRST n ROWS ONLY` or `WHERE ROWNUM <= n` |

## Common Mistakes

- **LIMIT without ORDER BY**: Returns an arbitrary set of rows — not a meaningful "top N".
- **Large offsets in pagination**: Slow on large tables. Use keyset pagination for large datasets.
- **Forgetting OFFSET starts at 0**: Page 1 is OFFSET 0, not OFFSET 1.
- **Using database-specific syntax in portable code**: `TOP` only works in SQL Server. `LIMIT` doesn't work in SQL Server. Use `FETCH FIRST` for portability.

## Mental Model

Think of LIMIT as telling the librarian: "I only need the first N books from your sorted pile — you can stop there." Without this instruction, the librarian sorts and retrieves every matching book in the entire library before handing them over. With LIMIT 5, they stop after the fifth book. OFFSET is like saying: "Skip the first 20 and give me the next 5." The librarian must still count through the first 20 before skipping — they can't teleport to position 21.

## Mini Summary

- ✔ LIMIT n — return at most n rows
- ✔ OFFSET m — skip m rows before returning
- ✔ Always pair LIMIT with ORDER BY for meaningful results
- ✔ Pagination formula: OFFSET = (page - 1) × page_size
- ✔ Large offsets are slow — consider keyset pagination for deep pages

# Guided Practice Quest

Work through the guided steps to write LIMIT queries with appropriate ORDER BY clauses and calculate OFFSET values for pagination scenarios.

# Solo Practice Quest

You are building a data API for a product catalogue with 50,000 products. Design three versions of a pagination query for the products endpoint: (1) offset-based pagination returning page N of results (20 per page) — write the SQL for pages 1, 5, and 2500, and estimate the relative query time for each, (2) a top-N query returning the 10 most recently added products in a specific category, (3) a keyset pagination version of (2) that avoids large offset performance issues — explain how the client would call it repeatedly to page through results. For each version, explain its trade-offs in terms of performance, consistency, and ease of implementation.

# Integration

**Mathematics**: LIMIT and OFFSET implement the mathematical operations of head and tail on an ordered sequence. Given an ordered sequence S = (s₁, s₂, ..., sₙ), LIMIT k returns the prefix (s₁, ..., sₖ), and OFFSET m LIMIT k returns the subsequence (sₘ₊₁, ..., sₘ₊ₖ). The performance characteristic of OFFSET follows from the linear nature of sequence access — accessing position m+1 in a linear scan requires traversing positions 1 through m first. This is why keyset pagination (which uses a predicate to jump directly to the next window) is O(log n) per page using an index, while offset pagination degrades to O(n) for large offsets.

**Sciences (Information Theory)**: Limiting result sets is a direct application of the information-theoretic principle of relevance filtering. Shannon's information theory quantifies the "information content" of a message as inversely proportional to its probability. In database terms, returning all 50,000 products when only 20 are needed transmits 49,980 rows of zero marginal relevance — wasting channel capacity. LIMIT is the database equivalent of bandwidth management: transmit only the minimum information needed to answer the query. This principle underlies the design of modern streaming protocols, recommendation systems, and search engine result pages — all of which show a bounded number of results per query.

# Lore Conclusion

"The Archivist needs the five most recently acquired scrolls each morning," Master Selvaris said. "Not a list of all acquisitions. Not the last hundred. Five." She ran the final version of the query: `ORDER BY acquired_at DESC LIMIT 5`. "The archive retrieves precisely five rows, discards nothing, and returns them in seconds." She closed the terminal. "This is the final piece of basic data retrieval: SELECT tells the archive what columns to return, FROM tells it where to look, WHERE tells it which rows to include, ORDER BY tells it in what order, and LIMIT tells it how many." She stepped back from the whiteboard where all five clauses were written in sequence. "Together, these five clauses cover the majority of queries any data engineer writes in their first year. Master them, and the rest builds naturally."

---
