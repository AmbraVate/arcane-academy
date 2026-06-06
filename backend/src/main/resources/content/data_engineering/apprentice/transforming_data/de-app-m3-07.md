---
id: de-app-m3-07
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m3
moduleTitle: "Module 3: SQL Foundations"
moduleGlyph: "🔍"
moduleSortOrder: 3
topicSlug: transforming_data
topicTitle: "Transforming Data"
topicSortOrder: 2
lesson: aliases
title: "Aliases"
sortOrder: 7
difficulty: 1
estimatedMinutes: 15
xpReward: 25
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: low
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m3-06]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Uses AS to alias both columns and tables correctly
    - Explains why aliases are necessary for calculated columns and expressions
    - Explains when table aliases are used and how they shorten query syntax
    - Distinguishes between column aliases and table aliases
    - Reflects on how good alias names improve query readability
  keywords: [alias, AS, column alias, table alias, readability, calculated column, rename, expression]
  modelAnswer: |
    An alias renames a column or table in the context of a query, without modifying the database. Column aliases rename result columns — essential for calculated columns that have no built-in name, and useful for making generic names more descriptive. Table aliases (used extensively with JOINs) shorten long table names into short prefixes. AS is the keyword, though many databases allow the alias directly without AS. Good aliases are descriptive (annual_salary not s), use snake_case, and match the context of the query. Column aliases defined in SELECT cannot be referenced in WHERE (because WHERE runs before SELECT), but can be used in ORDER BY in most databases.
guidedSteps:
  - id: de-app-m3-07-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Without an alias, what column name appears in the result set for the expression `unit_price * quantity`?
    inputConfig:
      options:
        - "total"
        - "unit_price * quantity (the expression itself)"
        - "column1"
        - "An error — calculated columns must have aliases"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["unit_price * quantity (the expression itself)"]
      rejectedFeedback: "Without an alias, most databases use the expression text as the column name: '?column?' or 'unit_price * quantity'. This is unreadable and inconsistent across databases. Always alias calculated columns: unit_price * quantity AS line_total."
    hint: "The database has to call the column something — and without an alias, it uses what you gave it."
    reflectionPrompt: "If application code reads result columns by name, what breaks when a calculated column has no alias?"
  - id: de-app-m3-07-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To give the `employees` table a short alias `e` in a query, you write: `FROM employees ________ e`
    inputConfig:
      placeholder: "AS"
    markingRule:
      matchMode: CONTAINS
      accepted: [AS, as]
      rejectedFeedback: "FROM employees AS e assigns the alias 'e' to the employees table for the duration of the query. You can then write e.name instead of employees.name. The AS keyword is optional in most databases — FROM employees e works too — but including AS makes the intent explicit."
    hint: "The same keyword used for column aliases also works for table aliases."
    reflectionPrompt: "After aliasing employees as e, can you still write employees.name in the same query?"
  - id: de-app-m3-07-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why you cannot use a column alias in the WHERE clause, but you can use it in ORDER BY.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [order, execution, WHERE, SELECT, before, after, alias, defined, run]
      rejectedFeedback: "SQL clauses execute in a specific logical order: FROM → WHERE → SELECT → ORDER BY. The SELECT clause (where aliases are defined) runs AFTER the WHERE clause — so when WHERE executes, the aliases do not yet exist. ORDER BY runs AFTER SELECT, so aliases are available there. This is why WHERE total > 100 fails (total is not yet defined), but ORDER BY total works."
    hint: "Think about the logical execution order: FROM, WHERE, SELECT, ORDER BY."
    reflectionPrompt: "How do you filter on a calculated value in WHERE if you can't use the alias? (Hint: repeat the expression)"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which is the correct way to alias the result of `COUNT(*)` as 'total_orders'?"
    options:
      - "COUNT(*) = total_orders"
      - "COUNT(*) total_orders"
      - "COUNT(*) AS total_orders"
      - "ALIAS COUNT(*) total_orders"
    correctIndex: 2
    feedback: "COUNT(*) AS total_orders is the standard syntax. Most databases also accept COUNT(*) total_orders (without AS), but including AS is clearer. The = syntax is not valid SQL for aliasing. ALIAS is not a SQL keyword."
  - type: MULTIPLE_CHOICE
    question: "When should you use a table alias?"
    options:
      - "Only when querying multiple tables with a JOIN"
      - "Always — table aliases are required in SQL"
      - "When table names are long, or in JOINs where you need to distinguish columns from different tables"
      - "Only when the table has more than 10 columns"
    correctIndex: 2
    feedback: "Table aliases are optional for single-table queries but very useful when: (1) joining multiple tables where you need to qualify column names (e.g. c.name vs o.name), (2) the table name is long (customer_shipping_addresses AS csa), or (3) the table is self-joined (employees AS manager JOIN employees AS report). They make queries shorter and more readable."
retrieval:
  recall: "Write a SELECT query that uses column aliases for two calculated columns and a table alias."
  explain: "Explain why a column alias defined in SELECT cannot be used in WHERE."
  mistakeId:
    code: "WHERE annual_salary > 50000 (where annual_salary is a SELECT alias for salary * 12)"
    answer: "The WHERE clause executes before SELECT in SQL's logical order, so the alias annual_salary has not yet been defined when WHERE runs. The query will fail with 'column not found'. The fix is to repeat the expression: WHERE salary * 12 > 50000, or use a subquery or CTE."
---

# Hook

A column named `unit_price * quantity` is technically correct but practically useless. An expression like `COUNT(*)` defaults to `?column?` or worse. A table named `customer_shipping_addresses` repeated five times makes a query difficult to read.

Aliases solve all of these. They rename columns and tables in the query context — making results readable, expressions named, and long table names manageable.

# Lore Introduction

"The query output has a column named 'unit_price * quantity'," the merchant said, squinting at the result. "What am I looking at?" Master Selvaris added `AS line_total` to the expression. "An alias renames the column in the result," she said. "The archive does not change. The column is still computed from unit_price * quantity. But the result set now shows it as 'line_total' — a name that means something." She also added `FROM order_lines AS ol` and showed how the table name could be shortened. "Aliases are courtesy to the reader. They make queries self-documenting."

# Core Learning

## Concept Introduction

### Column Aliases

```sql
-- Without alias: column name is the expression text
SELECT unit_price * quantity FROM order_lines;
-- Column name in result: "unit_price * quantity" (unreadable)

-- With alias: column name is meaningful
SELECT unit_price * quantity AS line_total FROM order_lines;
-- Column name in result: "line_total"
```

Common uses:

```sql
-- Naming calculated columns
SELECT
    salary * 12 AS annual_salary,
    salary * 12 * 0.12 AS annual_pension_contribution
FROM employees;

-- Renaming generic column names for reports
SELECT
    u.email AS contact_email,
    u.created_at AS registration_date
FROM users AS u;

-- Naming aggregate results
SELECT COUNT(*) AS total_orders, SUM(total_amount) AS revenue
FROM orders;
```

**AS is optional** in most databases — `salary * 12 annual_salary` works — but explicit AS is clearer.

**Quoted aliases** allow spaces or reserved words:

```sql
SELECT name AS "Product Name", price AS "Unit Price (£)" FROM products;
```

### Table Aliases

```sql
-- Long table name → short alias
SELECT csa.street, csa.city, csa.postcode
FROM customer_shipping_addresses AS csa
WHERE csa.country = 'UK';

-- Essential in JOINs (covered in Module 4)
SELECT c.name, o.order_date, o.total_amount
FROM customers AS c
JOIN orders AS o ON o.customer_id = c.customer_id;
```

Once you alias a table, the alias replaces the original name in the rest of the query:

```sql
-- CORRECT: use the alias
FROM customers AS c WHERE c.country = 'UK'

-- WRONG: mix alias and original name
FROM customers AS c WHERE customers.country = 'UK'  -- error in most databases
```

### Aliases and Execution Order

Column aliases are defined in SELECT, which runs **after** WHERE but **before** ORDER BY:

```sql
-- WRONG: alias not available in WHERE
SELECT salary * 12 AS annual_salary FROM employees
WHERE annual_salary > 50000;   -- Error: column "annual_salary" does not exist

-- CORRECT: repeat the expression in WHERE
SELECT salary * 12 AS annual_salary FROM employees
WHERE salary * 12 > 50000;

-- CORRECT: alias available in ORDER BY (SELECT runs before ORDER BY)
SELECT salary * 12 AS annual_salary FROM employees
ORDER BY annual_salary DESC;
```

## Common Mistakes

- **Trying to use a column alias in WHERE**: Fails because WHERE runs before SELECT. Repeat the expression instead.
- **Not aliasing calculated columns**: Results in unreadable column names in application responses and reports.
- **Using aliases that shadow column names**: If a table has a column called `total` and you alias an expression as `total`, it can cause confusion.
- **Spaces in aliases without quotes**: `AS annual salary` is a syntax error — use `AS annual_salary` or `AS "annual salary"`.

## Mental Model

Think of an alias as a name tag. The employee whose badge says "Alex" is still the same person — you haven't renamed them permanently. You've just given them a context-specific name for this meeting. Column aliases are name tags for the duration of the query. Table aliases are name tags for the duration of the FROM clause. When the query ends, the original names are unchanged.

## Mini Summary

- ✔ `expression AS alias` — renames a column in the result
- ✔ `FROM table AS alias` — gives the table a short name for the query
- ✔ Column aliases cannot be used in WHERE (WHERE runs before SELECT)
- ✔ Column aliases can be used in ORDER BY (ORDER BY runs after SELECT)
- ✔ Always alias calculated columns and expressions for readable results

# Guided Practice Quest

Work through the guided steps to alias calculated columns, create table aliases, and understand why column aliases are unavailable in WHERE clauses.

# Solo Practice Quest

Rewrite the following unaliased query to be fully readable using appropriate column and table aliases. Then write one additional query of your own using the same tables that demonstrates three different alias use cases (calculated column alias, table alias, aggregate alias). Original query: `SELECT e.id, e.fn, e.ln, e.sal * 12, e.sal * 12 * 0.12, d.name FROM emp AS e JOIN dept AS d ON e.dept_id = d.id WHERE e.sal * 12 > 60000 ORDER BY e.sal * 12 DESC;`

# Integration

**Mathematics**: Aliases in SQL correspond to the mathematical concept of variable substitution and renaming in formal systems. In lambda calculus and type theory, alpha conversion allows renaming bound variables without changing the meaning of an expression. A table alias is an alpha conversion — renaming the table variable in a query expression. In relational algebra, the rename operator (ρ) renames attributes: ρ(annual_salary ← salary×12)(employees) is the formal expression for what `SELECT salary * 12 AS annual_salary FROM employees` achieves. This mapping shows that aliasing is not just syntactic convenience — it is a formal operation in the relational algebra.

**Sciences (Chemistry — Nomenclature)**: Chemical nomenclature distinguishes between systematic names (2-hydroxypropanoic acid), common names (lactic acid), and abbreviations (HLA). All three refer to the same compound; the choice depends on context and audience. SQL aliases serve the same purpose: `customer_tax_identification_number` is the systematic name, `tax_id` is the common name for the same column. In both chemistry and SQL, clear naming aids communication — and the choice of alias, like the choice of chemical name, reflects the intended audience and purpose of the query.

# Lore Conclusion

"The report now reads clearly," the merchant said, scanning column headers: `line_total`, `annual_salary`, `contact_email`. Master Selvaris nodded. "Aliases cost nothing. They add everything. A query written for other people to read — or for yourself to re-read next month — earns its clarity from good names." She closed the query editor. "There is one more rule: name things for what they mean, not for how they are computed. 'line_total' says what the column is. 'unit_price * quantity' says how it was computed. The reader needs the what; they can find the how in the query."

---
