---
id: se-jun-m5-02
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m5
moduleTitle: "Module 5: Databases"
moduleGlyph: "🗄️"
moduleSortOrder: 5
topicSlug: joins
topicTitle: "Joins"
topicSortOrder: 2
lesson: joins
title: "Joins"
sortOrder: 2
difficulty: 3
estimatedMinutes: 30
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [sql_basics]
integrationDomains: [mathematics, data_science]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly writes an INNER JOIN query on a foreign key relationship"
    - "Explains the difference between INNER JOIN and LEFT JOIN with a concrete example"
    - "Describes what NULL means in the context of an outer join result"
    - "Uses a Venn diagram mental model to reason about join types"
    - "Identifies a real-world scenario where a LEFT JOIN is preferable to an INNER JOIN"
  keywords: [INNER JOIN, LEFT JOIN, RIGHT JOIN, NULL, foreign key, Venn, ON, multi-table]
  modelAnswer: |
    -- INNER JOIN: only rows where both sides match
    SELECT customers.name, orders.total
    FROM customers
    INNER JOIN orders ON customers.id = orders.customer_id;

    -- LEFT JOIN: all customers, even those with no orders (NULL for order columns)
    SELECT customers.name, orders.total
    FROM customers
    LEFT JOIN orders ON customers.id = orders.customer_id;
    -- customers with no orders appear with NULL in the orders.total column

    -- Useful pattern: find customers with NO orders
    SELECT customers.name
    FROM customers
    LEFT JOIN orders ON customers.id = orders.customer_id
    WHERE orders.id IS NULL;
guidedSteps:
  - id: joins-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You have two tables: `employees(id, name, dept_id)` and `departments(id, name)`.
      Which query returns ONLY employees who have a matching department?
    inputConfig:
      options:
        - "SELECT * FROM employees LEFT JOIN departments ON employees.dept_id = departments.id;"
        - "SELECT * FROM employees INNER JOIN departments ON employees.dept_id = departments.id;"
        - "SELECT * FROM employees RIGHT JOIN departments ON employees.dept_id = departments.id;"
        - "SELECT * FROM employees CROSS JOIN departments;"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SELECT * FROM employees INNER JOIN departments ON employees.dept_id = departments.id;"]
      rejectedFeedback: "INNER JOIN returns only rows where the ON condition is satisfied on BOTH sides. Employees without a matching department_id are excluded. LEFT JOIN would include all employees (with NULL for department columns if unmatched)."
    hint: "Think about which join type only returns rows that exist in both tables."
    reflectionPrompt: "INNER JOIN is the strictest join — it demands a match on both sides. Use it when you only care about complete, matched records."

  - id: joins-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A `LEFT JOIN` between `customers` (left) and `orders` (right) on `customers.id = orders.customer_id` returns a row where `orders.total` is NULL. What does this mean?
    inputConfig:
      options:
        - "The order total was not entered in the system"
        - "The query has an error in the ON condition"
        - "This customer has no matching orders in the orders table"
        - "NULL means the order total is zero"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["This customer has no matching orders in the orders table"]
      rejectedFeedback: "In a LEFT JOIN, all rows from the left table are preserved. When no matching row exists in the right table, the right-side columns are filled with NULL. A NULL in orders.total means the customer exists but has placed no orders."
    hint: "LEFT JOIN preserves all rows from the left table — what fills in when there's no match on the right?"
    reflectionPrompt: "The NULL-for-unmatched pattern is essential: it lets you find rows that have NO match — a LEFT JOIN WHERE right.id IS NULL."

  - id: joins-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe a business scenario where you would use a LEFT JOIN instead of an INNER JOIN, and explain why the choice matters for the query results.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [all, missing, NULL, include, no match]
      rejectedFeedback: "Example: A marketing team wants a list of ALL customers, showing their last order date — including customers who have never ordered (so they can target them with a campaign). INNER JOIN would exclude those customers entirely; LEFT JOIN includes them with NULL for the order columns, so the full customer list is visible."
    hint: "Think about a report that needs to include records even when related data is absent."
    reflectionPrompt: "The choice between INNER and LEFT JOIN is a business question as much as a technical one: do you want to see everything, or only what has a complete match?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In a Venn diagram model of joins, INNER JOIN represents:"
    options:
      - "The entire left circle"
      - "The entire right circle"
      - "The overlapping intersection of both circles"
      - "Both circles combined"
    correctIndex: 2
    feedback: "INNER JOIN produces only the rows that exist in both tables — the intersection of the two sets. LEFT JOIN produces the entire left circle (all left rows, with NULLs where no right match exists)."
  - type: MULTIPLE_CHOICE
    question: "What does the ON clause in a JOIN specify?"
    options:
      - "Which columns to display in the result"
      - "The condition that determines how rows from the two tables are matched"
      - "The order in which results are returned"
      - "Which table is the primary table"
    correctIndex: 1
    feedback: "The ON clause defines the join condition — usually a foreign key relationship like ON orders.customer_id = customers.id. Rows from both tables are matched wherever this condition is true."
retrieval:
  recall: "Describe INNER JOIN, LEFT JOIN, and RIGHT JOIN in one sentence each, focusing on which rows they include."
  explain: "A report must show every product in the catalogue alongside its total sales — including products that have never been sold (showing 0 or NULL). Which join type do you use, and why?"
  mistakeId:
    code: |
      SELECT orders.id, customers.name
      FROM orders
      INNER JOIN customers ON orders.id = customers.id;
    answer: "The ON condition is wrong: orders.id is the primary key of orders, and customers.id is the primary key of customers — joining on these means only orders whose ID happens to equal a customer's ID would match, which is coincidental. The correct join should be on the foreign key: ON orders.customer_id = customers.id."
---

# Hook

Imagine a company storing customer information in one spreadsheet and order history in another. To produce an invoice that shows both the customer name and their purchase details, someone has to manually look up each customer in the first spreadsheet and find their orders in the second. Now imagine doing this for a million customers. This manual matching is exactly what a SQL JOIN automates — and it is one of the most powerful operations in relational databases.

Understanding joins is a critical skill for any backend engineer. Every meaningful database query in a real application — user profiles with their associated data, products with their categories, orders with their items — requires combining data from multiple tables. The join is the SQL engine that makes this possible.

> Think of any two related datasets you interact with — a playlist and its songs, a school and its students, a department and its employees. What field links them together, and what would you want to see when you combine them?

# Lore Introduction

The Academy's Archivists maintain separate ledgers: one for registered mages, one for spells each mage has learned, one for guild affiliations. When Archmage Veylan requests a report on senior mages and their most powerful known spells, the junior Archivist must cross-reference all three ledgers by hand — a laborious process prone to transcription errors.

The Senior Archivists have mastered the **Convergence Rune** — an enchantment that draws matching records from multiple ledgers into a single unified view in seconds. In SQL, this is the JOIN. It is the art of merging related records from separate tables into coherent, multi-dimensional results.

# Core Learning

## Concept Introduction

SQL **JOINs** combine rows from two or more tables based on a related column (usually a foreign key).

**INNER JOIN** — returns only rows that have a match in BOTH tables:
```sql
SELECT customers.name, orders.total
FROM customers
INNER JOIN orders ON customers.id = orders.customer_id;
-- Customers with no orders are excluded
```

**LEFT JOIN** — returns ALL rows from the left table, with NULLs for unmatched right rows:
```sql
SELECT customers.name, orders.total
FROM customers
LEFT JOIN orders ON customers.id = orders.customer_id;
-- All customers included; NULL in orders columns for those with no orders
```

**RIGHT JOIN** — returns ALL rows from the right table (less common; LEFT JOIN is preferred by convention):
```sql
SELECT customers.name, orders.total
FROM customers
RIGHT JOIN orders ON customers.id = orders.customer_id;
-- All orders included; NULL in customer columns if order has no matching customer
```

### Venn Diagram Mental Model
- INNER JOIN = the overlapping centre of two circles
- LEFT JOIN = the entire left circle (plus overlap)
- RIGHT JOIN = the entire right circle (plus overlap)
- FULL OUTER JOIN = both circles entirely

## Why It Matters

Real-world databases are normalised — data is split across multiple tables to avoid duplication. A single user record never contains all their related data inline; that data lives in separate tables joined by foreign keys. Without JOIN, every application query would require multiple round trips to the database, assembling results in application code — slow, complex, and fragile.

## Worked Examples

**Example 1 — INNER JOIN on a foreign key**
```sql
-- Tables: products(id, name, category_id), categories(id, name)
SELECT products.name AS product, categories.name AS category
FROM products
INNER JOIN categories ON products.category_id = categories.id;
-- Only products with a valid category appear in results
```

**Example 2 — LEFT JOIN to find missing relationships**
```sql
-- Find all authors who have NOT published any books
SELECT authors.name
FROM authors
LEFT JOIN books ON authors.id = books.author_id
WHERE books.id IS NULL;
-- The IS NULL check identifies the "no match" rows from the LEFT JOIN
```

**Example 3 — Multi-table JOIN**
```sql
SELECT customers.name, orders.date, products.name AS item
FROM customers
INNER JOIN orders ON customers.id = orders.customer_id
INNER JOIN order_items ON orders.id = order_items.order_id
INNER JOIN products ON order_items.product_id = products.id;
-- Chains three joins to assemble the full picture
```

## Common Mistakes

- **Wrong join column** — joining on primary keys instead of foreign keys (e.g., `ON orders.id = customers.id` instead of `ON orders.customer_id = customers.id`).
- **Forgetting the ON clause** — a JOIN without ON produces a CROSS JOIN (every row matched with every other row) — often catastrophically large.
- **Using WHERE for outer join filtering on the right side** — `WHERE orders.total > 100` on a LEFT JOIN eliminates the NULL rows (turning it into an INNER JOIN); put such filters in the ON clause or a subquery.
- **Ambiguous column names** — if both tables have a `name` column, qualify it: `customers.name`, `products.name`.
- **Overusing RIGHT JOIN** — just swap the table order and use LEFT JOIN for consistency.

## Mental Model

Imagine two tables as two sets of translucent cards laid flat. INNER JOIN only shows you cards that overlap perfectly when you slide the two sets together. LEFT JOIN shows you every card in the left deck — with blank right-side information wherever there is no matching card beneath it.

## Mini Summary

✔ INNER JOIN returns only rows with matching records on both sides of the join condition.
✔ LEFT JOIN returns all left-table rows; unmatched right rows appear as NULL.
✔ The ON clause defines the matching condition — always use foreign keys.
✔ `LEFT JOIN ... WHERE right.id IS NULL` finds rows with NO matching related record.
✔ Multi-table queries chain multiple JOINs, each adding another dimension of data.

# Guided Practice Quest

**The Convergence Rune**
The Academy's Registry needs cross-referenced reports. Write JOIN queries to combine data from the mage and spell ledgers.
Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Given these three tables:
- `students(id, name, enrolment_date)`
- `courses(id, title, credits)`
- `enrolments(student_id, course_id, grade)`

Write SQL queries for: (1) all students and their enrolled course titles (INNER JOIN); (2) all students including those not enrolled in any course (LEFT JOIN); (3) students who have no enrolments at all. For each query, explain in one sentence why you chose that join type. Reflect in 3 sentences on how NULL behaviour in LEFT JOINs can lead to surprising results if not handled carefully.

# Integration

**Connecting to Mathematics — Set Theory**
SQL joins are direct implementations of **set theory operations**. The relational model of databases, invented by Edgar Codd in 1970, was explicitly built on mathematical set theory. INNER JOIN corresponds to the **intersection** of two sets; FULL OUTER JOIN corresponds to the **union**. LEFT JOIN is a left-biased union where elements from the left set are always preserved.

This mathematical foundation is not merely theoretical — it is why relational databases are so reliable and predictable. Every JOIN operation has well-defined, mathematically proven behaviour. When you understand that INNER JOIN = intersection and LEFT JOIN = left-biased union, you can reason about complex multi-table queries using the same tools you use to reason about sets.

> Can you express the "find all customers with no orders" query in pure set notation? What does "A minus the intersection of A and B" mean for a JOIN query?

# Lore Conclusion

The junior Archivist invokes the Convergence Rune for the first time and watches three separate ledgers draw together into a single coherent view: mage names, their learned spells, and their guild affiliations, all aligned in one authoritative table. Archmage Veylan reviews the report in seconds.

The Archivist has unlocked one of the most powerful tools in the data scholar's repertoire. In the next lesson, the deeper structure beneath these tables will be revealed: the relationships between data — how one record points to another, how many records can share a relationship, and how to design these connections with precision and integrity.

---
