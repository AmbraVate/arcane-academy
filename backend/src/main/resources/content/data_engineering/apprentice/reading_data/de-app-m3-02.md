---
id: de-app-m3-02
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
lesson: select_statements
title: "SELECT Statements"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m3-01]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Writes a correct SELECT statement retrieving specific columns from a table
    - Explains the difference between SELECT * and selecting named columns
    - Explains what SELECT DISTINCT does and when to use it
    - Describes the logical order of execution of a basic SELECT statement
    - Reflects on why selecting only needed columns is better practice than SELECT *
  keywords: [SELECT, FROM, column, table, DISTINCT, projection, result set, asterisk, query]
  modelAnswer: |
    A SELECT statement retrieves data from one or more tables. The FROM clause specifies the table; the column list after SELECT specifies which columns to return (projection). SELECT * retrieves all columns — convenient but expensive in production (transfers unused data, breaks when schema changes). SELECT DISTINCT removes duplicate rows from the result. The logical execution order of a basic SELECT is: FROM (identify the source), WHERE (filter rows), SELECT (project columns), ORDER BY (sort). Writing SELECT with specific named columns is best practice: it is explicit, performant, and resilient to schema changes.
guidedSteps:
  - id: de-app-m3-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which SQL statement retrieves only the `name` and `email` columns from a `customers` table?
    inputConfig:
      options:
        - "GET name, email FROM customers;"
        - "SELECT name, email FROM customers;"
        - "FETCH name, email FROM customers;"
        - "READ customers COLUMNS name, email;"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SELECT name, email FROM customers;"]
      rejectedFeedback: "SELECT column1, column2 FROM table_name is the fundamental SQL query structure. SELECT specifies which columns to return; FROM specifies which table to read from. GET, FETCH, and READ are not SQL keywords for data retrieval."
    hint: "The two core keywords in a basic SQL read query are SELECT and FROM."
    reflectionPrompt: "What is the difference between specifying column names and using SELECT * in terms of the data returned?"
  - id: de-app-m3-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To remove duplicate rows from a query result, you add the keyword ________ immediately after SELECT.
    inputConfig:
      placeholder: "DISTINCT"
    markingRule:
      matchMode: CONTAINS
      accepted: [DISTINCT, distinct]
      rejectedFeedback: "SELECT DISTINCT removes duplicate rows from the result set. For example, SELECT DISTINCT country FROM customers returns each country name only once, even if thousands of customers share the same country. Without DISTINCT, every row is returned, including duplicates."
    hint: "This keyword means 'unique' — return each unique combination only once."
    reflectionPrompt: "If a customers table has 10,000 rows but only 50 unique countries, how many rows does SELECT DISTINCT country FROM customers return?"
  - id: de-app-m3-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why using SELECT * in production queries is considered bad practice.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [columns, schema, change, performance, unnecessary, all, bandwidth, transfer, explicit, break]
      rejectedFeedback: "SELECT * returns all columns, including ones the application doesn't need — wasting network bandwidth and memory. When the schema changes (columns added or reordered), SELECT * silently returns different data, potentially breaking application code that assumes a specific column order. Explicitly naming columns makes queries self-documenting and resilient to schema changes."
    hint: "Think about what happens when a new column is added to the table, and about data you don't actually need."
    reflectionPrompt: "If an application uses column index positions (column[0], column[1]) instead of names, what breaks when a column is added to the table?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does the FROM clause in a SELECT statement specify?"
    options:
      - "Which columns to return"
      - "Which table (or tables) to read data from"
      - "A condition that filters rows"
      - "The sort order of results"
    correctIndex: 1
    feedback: "FROM specifies the data source — the table (or tables, in a JOIN) from which rows will be read. SELECT specifies which columns to return (projection). WHERE filters rows. ORDER BY sorts results. These four clauses cover most basic queries."
  - type: MULTIPLE_CHOICE
    question: "A `products` table has 200 rows. How many rows does `SELECT DISTINCT category FROM products` return if there are 8 unique categories?"
    options: ["200", "8", "1", "It depends on the database"]
    correctIndex: 1
    feedback: "DISTINCT reduces the result to unique values only. If there are 8 unique category values across 200 rows, SELECT DISTINCT category returns exactly 8 rows — one per unique category. All duplicate category values are collapsed."
retrieval:
  recall: "Write a SELECT statement that retrieves the name and price columns from a products table."
  explain: "Explain the difference between SELECT * and SELECT name, price in terms of what data is returned and why the named version is preferred."
  mistakeId:
    code: "SELECT * FROM orders WHERE ..."
    answer: "SELECT * returns all columns in the table, including columns the query doesn't need. This wastes network bandwidth, memory, and processing. It also breaks if application code relies on column positions and the schema changes. Always select only the columns you need: SELECT order_id, customer_id, order_date FROM orders."
---

# Hook

The most common word in any SQL database log is SELECT. It is the instruction that reads data — the foundation of every report, dashboard, API response, and data pipeline.

A SELECT statement is deceptively simple to start with: two keywords, a column list, and a table name. But understanding it properly — what it really does, what SELECT * costs, and how to read the execution order — sets up everything that follows.

# Lore Introduction

"The archive holds ten million scrolls," Master Selvaris said, "but you rarely need all ten million. You need the title and author of scrolls from the Third Age. Or the title, era, and location of scrolls about elemental magic." She wrote on the whiteboard: `SELECT title, author FROM scrolls;`. "This statement retrieves two columns from the scrolls table — every row, but only those two pieces of information. The archive does not return the acquisition date, the preservation status, the shelf coordinates. Only what you asked for." She underlined SELECT and FROM. "These two keywords are the foundation of every query you will write."

# Core Learning

## Concept Introduction

### The Basic SELECT Statement

```sql
SELECT column1, column2
FROM table_name;
```

- `SELECT` — specifies which columns to return (the "projection")
- `FROM` — specifies which table to read from

**Examples:**

```sql
-- Retrieve two columns from customers
SELECT first_name, email
FROM customers;

-- Retrieve three columns from products
SELECT name, unit_price, stock_qty
FROM products;
```

### SELECT * — All Columns

```sql
-- Returns every column in the table
SELECT *
FROM customers;
```

Useful for:
- Quick exploration during development
- Understanding what columns exist in a table

Not appropriate for:
- Production queries (returns unnecessary data)
- Application code (breaks when schema changes)

### SELECT DISTINCT — Unique Values

```sql
-- Returns each unique country value once
SELECT DISTINCT country
FROM customers;

-- Returns each unique (category, brand) combination once
SELECT DISTINCT category, brand
FROM products;
```

### Column Ordering and All Rows

```sql
-- Columns are returned in the order you list them — not table order
SELECT email, first_name, last_name
FROM customers;

-- Without a WHERE clause, all rows are returned
-- (a table with 1,000,000 rows returns 1,000,000 rows)
SELECT product_id, name
FROM products;
```

### Logical Execution Order

When you write a SELECT query, SQL executes clauses in this logical order (not the written order):

1. `FROM` — identify the source table
2. `WHERE` — filter rows (added in the next lesson)
3. `SELECT` — project the columns
4. `ORDER BY` — sort the result (lesson 4)
5. `LIMIT` — restrict the row count (lesson 5)

Understanding this order explains why you cannot use a SELECT alias in a WHERE clause — the WHERE runs before the SELECT names are assigned.

### Selecting Expressions

SELECT is not limited to column names — you can include literal values and expressions:

```sql
SELECT
    name,
    unit_price,
    'GBP' AS currency,          -- literal value
    unit_price * 1.2 AS price_with_vat  -- expression (covered in Module 3 Topic 2)
FROM products;
```

## Common Mistakes

- **`SELECT *` in production code**: Returns all columns, including unused ones. Wastes bandwidth, memory, and breaks on schema changes.
- **Forgetting the semicolon**: SQL statements end with `;`. Most clients tolerate its absence, but it is required in scripts and stored procedures.
- **Assuming column order matters**: `SELECT a, b` and `SELECT b, a` return the same rows — just different column orders. The table's physical column order is irrelevant.
- **DISTINCT on the wrong columns**: `SELECT DISTINCT first_name, last_name` returns unique name pairs, not unique first names alone.

## Mental Model

Think of SELECT and FROM as "give me these columns from this drawer." The FROM clause opens the correct filing drawer (the table). The SELECT clause specifies which pieces of information to pull from each card in that drawer. SELECT * says "give me the entire card." SELECT name, email says "give me only the name and email fields from each card." DISTINCT says "if two cards have identical information, give me only one of them."

## Mini Summary

- ✔ `SELECT col1, col2 FROM table` — the fundamental read statement
- ✔ `SELECT *` returns all columns — fine for exploration, bad for production
- ✔ `SELECT DISTINCT` removes duplicate rows from the result
- ✔ Logical execution order: FROM → WHERE → SELECT → ORDER BY → LIMIT
- ✔ Always name the columns you need — explicit is safer than *

# Guided Practice Quest

Work through the guided steps to write correct SELECT statements, explain the difference between SELECT * and named columns, and apply SELECT DISTINCT to a scenario.

# Solo Practice Quest

You have access to a `library_catalogue` table with columns: `book_id`, `title`, `author`, `genre`, `publication_year`, `isbn`, `shelf_location`, `acquisition_date`, `condition`. Write five SELECT queries: (1) retrieve only the title and author, (2) retrieve all columns (explain when this is appropriate), (3) retrieve the distinct genres available in the catalogue, (4) retrieve the title, author, and genre — but listed in that specific column order, (5) retrieve the distinct combinations of genre and condition. For each query explain what it returns and how many rows you would expect from a table with 5,000 books in 12 genres and 3 condition categories.

# Integration

**Mathematics**: The SELECT column list is a direct implementation of the projection operator (π) in relational algebra. Given a relation R with attributes {A, B, C, D}, πA,C(R) returns a new relation containing only attributes A and C — discarding B and D. SELECT name, email FROM customers is πname,email(customers). SELECT DISTINCT adds duplicate elimination (equivalent to set semantics rather than bag semantics). SELECT * is the identity projection — πA,B,C,D,…(R) where all attributes are included, returning R unchanged in content.

**Sciences (Optics)**: A projection lens focuses only selected wavelengths of light onto a surface — analogous to SQL projection selecting only specified columns. A full-spectrum lens (SELECT *) transmits all wavelengths but may overwhelm a sensor designed for specific frequencies. A filtered lens (SELECT col1, col2) transmits only what the sensor needs, improving signal quality and efficiency. The optics analogy extends to DISTINCT: a diffraction grating separates light into distinct spectral lines — one per unique wavelength — just as DISTINCT returns one row per unique value combination.

# Lore Conclusion

Master Selvaris erased the whiteboard and started fresh. "You now know the two most important words in SQL," she said. "SELECT tells the archive what to return. FROM tells it where to look." She wrote a query: `SELECT title, author, era FROM scrolls;`. "Ten million scrolls in this archive. This query returns three pieces of information from every one of them. Remove the era from the list and the archive omits it. Add DISTINCT and it collapses duplicates." She stepped back. "Every SQL query you will ever write begins here. Everything else — filtering, sorting, aggregating — is built on top of this foundation."

---
