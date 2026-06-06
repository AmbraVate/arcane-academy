---
id: de-jun-m1-04
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m1
moduleTitle: "Module 1: Advanced SQL"
moduleGlyph: "⚡"
moduleSortOrder: 1
topicSlug: set_operations
topicTitle: "Set Operations"
topicSortOrder: 4
lesson: set_operations
title: "Set Operations"
sortOrder: 4
difficulty: 2
estimatedMinutes: 25
xpReward: 40
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m1-03]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Distinguishes UNION (deduplicates) from UNION ALL (keeps duplicates)
    - Explains the column compatibility requirement for set operations
    - Describes what INTERSECT returns and gives a practical use case
    - Describes what EXCEPT (or MINUS) returns and gives a practical use case
    - Identifies when to use UNION ALL over UNION for performance reasons
  keywords: [UNION, UNION ALL, INTERSECT, EXCEPT, MINUS, column compatibility, deduplication, combine, set theory, stacking rows]
  modelAnswer: |
    Set operations combine result sets from two or more SELECT statements. UNION combines and removes duplicates. UNION ALL combines and keeps all rows including duplicates — faster because no deduplication step is needed. INTERSECT returns only rows present in both result sets. EXCEPT (MINUS in Oracle) returns rows in the first result set not present in the second. All set operations require both queries to have the same number of columns with compatible data types. Column names in the output come from the first SELECT. Use UNION ALL instead of UNION when duplicates are acceptable (performance) or impossible (naturally distinct sets).
guidedSteps:
  - id: de-jun-m1-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You need to combine all customers from two regional databases (North and South) into a single list for a mailing. Some customers exist in both. Which set operation gives you all customers with no duplicates?
    inputConfig:
      options:
        - "UNION ALL — combines all rows from both queries including duplicates"
        - "UNION — combines all rows and removes duplicates"
        - "INTERSECT — returns only customers present in both databases"
        - "EXCEPT — returns customers in North but not South"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["UNION — combines all rows and removes duplicates"]
      rejectedFeedback: "UNION combines the results of two queries and removes duplicate rows — perfect for merging lists where you want each customer to appear once. UNION ALL would give you duplicates for customers in both databases. INTERSECT gives you only the customers who exist in both (not all customers). EXCEPT gives you only the customers in the first database who are not in the second. For a deduplicated combined mailing list, UNION is the correct choice — though UNION ALL is faster if you can guarantee no duplicates beforehand."
    hint: "Which operation combines everything and removes duplicates?"
    reflectionPrompt: "If you know the two customer lists cannot have any overlap (the databases cover completely different regions), which operation would you use instead of UNION and why?"
  - id: de-jun-m1-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      When combining two queries with UNION, both SELECT statements must have the same number of ________ with compatible data types.
    inputConfig:
      placeholder: "columns"
    markingRule:
      matchMode: CONTAINS
      accepted: [columns, "number of columns", "column count", "column types", "column compatibility"]
      rejectedFeedback: "Set operations require both SELECT statements to have the same number of columns, and corresponding columns must have compatible data types (a VARCHAR column in query 1 must align with a VARCHAR or compatible type in query 2). The column names in the output are taken from the first SELECT — column names in the second SELECT are ignored. If queries have different numbers of columns, add NULL placeholders: SELECT id, name, NULL AS phone FROM customers UNION SELECT id, name, phone FROM prospects."
    hint: "What must match between the two SELECT statements in a UNION?"
    reflectionPrompt: "You need to UNION a query with 3 columns to one with 4 columns. How do you make them compatible?"
  - id: de-jun-m1-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain a real business use case for EXCEPT, and why EXCEPT is more reliable than a NOT IN subquery when the source data may contain NULLs.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [NULL, NOT IN, reliable, unsubscribed, exclude, difference, safe, missing, subset]
      rejectedFeedback: "A practical EXCEPT use case: finding customers who placed an order last month but have not placed an order this month — a churn detection pattern. EXCEPT is more reliable than NOT IN because NOT IN with NULLs in the second result set returns no rows (NULL IN (1, NULL, 3) is NULL, not FALSE). EXCEPT correctly handles NULLs by treating NULLs as equal for comparison purposes — two NULL values are considered duplicates in set operations. This makes EXCEPT safer than NOT IN for set-difference queries on real-world data that may contain NULLs."
    hint: "What happens when NOT IN encounters a NULL in the list? Why does EXCEPT not have this problem?"
    reflectionPrompt: "Rewrite a NOT IN subquery using EXCEPT. Which version is easier to understand?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the key performance difference between UNION and UNION ALL?"
    options:
      - "UNION ALL is slower because it processes more rows"
      - "UNION requires a deduplication step (sort or hash), making it slower than UNION ALL"
      - "They perform identically — the difference is only semantic"
      - "UNION ALL is only supported in PostgreSQL"
    correctIndex: 1
    feedback: "UNION requires the database to identify and remove duplicates after combining the two result sets. This typically involves sorting or hashing all rows — an O(n log n) operation on the combined result. UNION ALL skips this step entirely, making it faster. Use UNION ALL whenever you know duplicates are impossible (naturally distinct datasets) or acceptable (you will deduplicate later, or the application does not care). UNION is only needed when row-level deduplication is required."
  - type: MULTIPLE_CHOICE
    question: "INTERSECT is most useful for:"
    options:
      - "Combining two tables side by side (like JOIN)"
      - "Finding rows that exist in both result sets — overlap detection"
      - "Removing duplicate rows within a single table"
      - "Stacking two queries with different column counts"
    correctIndex: 1
    feedback: "INTERSECT returns only rows that appear in both result sets — it is the SQL implementation of set intersection (A ∩ B). Use cases: customers who bought both Product A and Product B (two separate queries selecting customer_ids, then INTERSECT); employees who appear in both the 'completed training' table and the 'eligible for promotion' table; subscribers who are on both an email list and an SMS list. It is much more readable than the JOIN-based alternative for these membership overlap queries."
retrieval:
  recall: "Write a query using UNION ALL that combines active and archived orders into a single result set, adding a column to indicate which table each row came from."
  explain: "Explain when you would use EXCEPT instead of NOT IN or LEFT JOIN ... WHERE IS NULL. What are the advantages and limitations of each approach?"
  mistakeId:
    code: "SELECT customer_id, name FROM customers UNION SELECT order_id, total_amount FROM orders"
    answer: "This UNION is semantically wrong — it combines customer IDs with order IDs and names with amounts. The query runs (both have 2 columns) but produces meaningless results: order IDs and total amounts appear in the 'customer_id' and 'name' columns. Set operations combine rows, not related data — use JOIN to combine data from related tables, not UNION. UNION is for stacking rows of the same entity from different sources (e.g. active_customers UNION archived_customers). The column count and data types should match because they represent the same thing, not just because they happen to fit."
---

# Hook

JOINs combine columns from multiple tables into wider rows. Set operations combine rows from multiple queries into a longer result set. UNION, INTERSECT, and EXCEPT give you the SQL implementation of mathematical set theory — tools for merging, finding overlaps, and finding differences between query results.

# Lore Introduction

"We need to find Archive members who borrowed from the east wing last quarter but have not borrowed from the west wing at all," the Senior Archivist said. "Two separate populations. One minus the other." The Junior Engineer drafted a NOT IN subquery. The Senior Archivist reviewed it. "Correct, but EXCEPT reads more naturally — it says exactly what we want: borrowers from east EXCEPT borrowers from west. And unlike NOT IN, it handles NULLs correctly." She ran both queries. "Same result, but EXCEPT is safer and clearer for set-difference questions. Use the operation that names what you're doing."

# Core Learning

## Concept Introduction

### The Four Set Operations

```
A UNION B        All rows from A plus all rows from B (duplicates removed)
A UNION ALL B    All rows from A plus all rows from B (duplicates kept)
A INTERSECT B    Only rows present in both A and B
A EXCEPT B       Rows in A that are not in B (called MINUS in Oracle)
```

### UNION and UNION ALL

```sql
-- UNION: combine active and archived customers, deduplicated
SELECT customer_id, name, email FROM active_customers
UNION
SELECT customer_id, name, email FROM archived_customers;
-- Removes duplicates — if a customer_id appears in both, appears once

-- UNION ALL: faster — keeps duplicates
SELECT customer_id, name, email FROM active_customers
UNION ALL
SELECT customer_id, name, email FROM archived_customers;
-- Both rows kept if customer exists in both tables

-- Add a source column to track origin
SELECT customer_id, name, 'active' AS source FROM active_customers
UNION ALL
SELECT customer_id, name, 'archived' AS source FROM archived_customers;
```

### Column Compatibility

```sql
-- Both queries must have the same number of columns with compatible types
-- Column names come from the FIRST query

SELECT id, name, NULL AS phone FROM customers    -- 3 columns (phone is NULL)
UNION ALL
SELECT id, name, phone FROM prospects;           -- 3 columns

-- Wrong — different number of columns:
-- SELECT id, name FROM customers UNION SELECT id, name, email FROM prospects;
-- Error: each SELECT must have the same number of expressions
```

### INTERSECT — Overlap Detection

```sql
-- Customers who placed orders in both January and February
SELECT customer_id FROM orders
WHERE order_date >= '2024-01-01' AND order_date < '2024-02-01'
INTERSECT
SELECT customer_id FROM orders
WHERE order_date >= '2024-02-01' AND order_date < '2024-03-01';

-- Products that appear in both 'Electronics' and 'Accessories' categories
-- (products tagged with multiple categories)
SELECT product_id FROM product_categories WHERE category = 'Electronics'
INTERSECT
SELECT product_id FROM product_categories WHERE category = 'Accessories';
```

### EXCEPT — Set Difference

```sql
-- Customers who ordered last month but not this month (potential churn)
SELECT customer_id FROM orders
WHERE order_date >= '2024-02-01' AND order_date < '2024-03-01'
EXCEPT
SELECT customer_id FROM orders
WHERE order_date >= '2024-03-01' AND order_date < '2024-04-01';

-- Products never ordered (exist in catalogue but have zero orders)
SELECT product_id FROM products
EXCEPT
SELECT DISTINCT product_id FROM order_lines;
-- Equivalent to: SELECT p.product_id FROM products p
--               LEFT JOIN order_lines ol ON p.product_id = ol.product_id
--               WHERE ol.product_id IS NULL
```

### EXCEPT vs NOT IN vs LEFT JOIN IS NULL

```sql
-- Three ways to find customers with no orders — equivalent results,
-- but EXCEPT handles NULLs most safely

-- EXCEPT approach
SELECT customer_id FROM customers
EXCEPT
SELECT customer_id FROM orders;

-- NOT IN approach (breaks if orders.customer_id contains any NULL)
SELECT customer_id FROM customers
WHERE customer_id NOT IN (SELECT customer_id FROM orders);

-- LEFT JOIN approach (explicit and performant on large datasets)
SELECT c.customer_id
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
WHERE o.customer_id IS NULL;
```

### ORDER BY and LIMIT with Set Operations

```sql
-- ORDER BY applies to the entire combined result, not individual queries
SELECT customer_id, name FROM active_customers
UNION ALL
SELECT customer_id, name FROM archived_customers
ORDER BY name;   -- sorts the combined result

-- LIMIT applies to the combined result
SELECT product_id, revenue FROM popular_products
UNION ALL
SELECT product_id, revenue FROM niche_products
ORDER BY revenue DESC
LIMIT 10;   -- top 10 across both sets
```

### Multiple UNION ALL — Building a Single Report

```sql
-- Combine multiple metrics into a single summary report
SELECT 'Total Revenue' AS metric, SUM(total_amount) AS value FROM orders
UNION ALL
SELECT 'Total Orders', COUNT(*) FROM orders
UNION ALL
SELECT 'Unique Customers', COUNT(DISTINCT customer_id) FROM orders
UNION ALL
SELECT 'Average Order Value', AVG(total_amount) FROM orders;
-- Produces a key-value summary table — common pattern for dashboards
```

## Common Mistakes

- **Using UNION when UNION ALL is sufficient**: UNION adds a deduplication step. If the two datasets cannot possibly overlap (e.g. data from different date ranges), use UNION ALL — it's always faster.
- **Column mismatch**: Different column counts between the two queries cause an error. Add NULL columns as placeholders to align column counts.
- **Relying on NOT IN with nullable columns**: NOT IN returns no rows if any NULL exists in the subquery result. EXCEPT and EXISTS are safer alternatives.
- **Forgetting that ORDER BY applies to the whole result**: You cannot add ORDER BY to individual queries in a UNION — only to the final combined result.

## Mental Model

Set operations work on whole rows, not on related data. Think of each SELECT as a set of cards — UNION stacks all cards (removing identical cards), UNION ALL stacks all cards including duplicates, INTERSECT keeps only the cards in both piles, EXCEPT keeps only the cards in the first pile that are not in the second. The requirement that both piles have the same "shape" (same column count and types) corresponds to the requirement that the cards being combined are comparable — you can only compare apples to apples.

## Mini Summary

- ✔ UNION: combine + deduplicate; UNION ALL: combine + keep duplicates (faster)
- ✔ Both queries must have the same number of columns with compatible types
- ✔ Column names in output come from the first SELECT
- ✔ INTERSECT: rows in both sets; EXCEPT: rows in first set not in second
- ✔ ORDER BY and LIMIT apply to the full combined result
- ✔ EXCEPT handles NULLs more safely than NOT IN for set-difference queries

# Guided Practice Quest

Work through the guided steps to write a UNION ALL that combines two customer tables with a source column, use INTERSECT to find customers who ordered in two consecutive months, and use EXCEPT to find products never ordered, then verify the result with a LEFT JOIN IS NULL approach.

# Solo Practice Quest

Using orders, customers, products, and a historical_orders (archived) table: (1) combine current and archived orders into a unified order history, preserving all columns and adding an is_archived boolean column; (2) find customers who placed orders in all four quarters of last year using INTERSECT (hint: intersect four separate quarter-filtered queries); (3) find products that appeared in the top 10 by revenue in Q1 but not in Q2 using EXCEPT; (4) produce a summary dashboard row using UNION ALL that shows total revenue, order count, unique customers, and average order value as a four-row key-value report; (5) write the same "customers with no orders" query three ways — EXCEPT, NOT IN, and LEFT JOIN IS NULL — and compare: what do they return differently if orders.customer_id can be NULL?

# Integration

**Mathematics**: SQL set operations are the direct computational implementation of Cantor's set algebra. UNION corresponds to union A ∪ B = {x | x ∈ A ∨ x ∈ B}. INTERSECT corresponds to intersection A ∩ B = {x | x ∈ A ∧ x ∈ B}. EXCEPT corresponds to set difference A \ B = {x | x ∈ A ∧ x ∉ B}. UNION ALL corresponds to the multiset union, which preserves multiplicities (multisets extend sets to allow repeated elements). These are core concepts of naive set theory, formalised by Cantor in the 1870s. SQL's three-valued logic (TRUE, FALSE, NULL) complicates the mathematical model — NULL values require the semantics of EXCEPT to differ from NOT IN, because the set algebra assumes all elements are comparable, while SQL's NULL is "incomparable" under equals.

**Sciences (Genomics — Variant Analysis)**: Set operations are fundamental to genomic data analysis. INTERSECT is used to find variants (genetic mutations) that appear in two different patient cohorts — shared risk variants. EXCEPT identifies variants in cancer patients not present in healthy controls — candidate disease-associated mutations. UNION ALL combines variant calls from multiple sequencing runs of the same sample to increase coverage. These are among the most common operations in population genomics databases (gnomAD, UK Biobank) — the same SQL set operations applied to tables with hundreds of millions of rows representing individual genetic variants.

# Lore Conclusion

"The east-wing-only borrowers list is ready," the Junior Engineer reported. "Ninety-three members borrowed from east wing last quarter but never from west wing. EXCEPT gave us that in three lines." The Senior Archivist nodded. "And it handled the two members with NULL registration data correctly — NOT IN would have excluded everyone." She scanned the list. "Set operations are elegant for these comparison questions. JOINs combine related data across columns. Set operations combine same-shape data across rows. Two different tools for two different problems." She set the list down. "One lesson left in Module 1: query optimisation — understanding why some queries are ten times slower than others on the same data."

---
