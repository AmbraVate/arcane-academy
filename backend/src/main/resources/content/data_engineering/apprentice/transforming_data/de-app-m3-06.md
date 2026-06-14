---
id: de-app-m3-06
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
lesson: calculated_columns
title: "Calculated Columns"
sortOrder: 6
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m3-05]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Writes a SELECT expression that calculates a new column from existing columns
    - Uses arithmetic operators (+, -, *, /) correctly in SQL expressions
    - Explains the difference between a calculated column and a stored column
    - Describes NULL propagation in arithmetic expressions
    - Reflects on why calculating at query time is preferable to storing derived values
  keywords: [calculated column, expression, arithmetic, derived, formula, NULL, propagation, virtual, runtime]
  modelAnswer: |
    A calculated column is an expression in the SELECT list that derives a new value from existing columns using arithmetic operators, functions, or literals. It is computed at query time, not stored in the database. Examples include unit_price * quantity for line total, or (salary * 0.12) for pension contribution. NULL propagates through arithmetic — any expression containing NULL evaluates to NULL, so coalesce() or NULLIF() may be needed. Storing derived values creates data integrity risk (the stored value can go out of sync with its source columns); calculating at query time keeps the database normalised and always correct.
guidedSteps:
  - id: de-app-m3-06-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which SELECT expression calculates the total cost for each order line (unit_price × quantity)?
    inputConfig:
      options:
        - "SELECT unit_price, quantity, total FROM order_lines;"
        - "SELECT unit_price, quantity, unit_price * quantity FROM order_lines;"
        - "SELECT unit_price * quantity = line_total FROM order_lines;"
        - "SELECT CALCULATE(unit_price, quantity) FROM order_lines;"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SELECT unit_price, quantity, unit_price * quantity FROM order_lines;"]
      rejectedFeedback: "SQL arithmetic expressions are written directly in the SELECT list: column1 * column2. No special keyword or function is needed. Option A would fail unless a 'total' column exists in the table. Option C uses invalid SQL syntax (= in SELECT is not assignment). CALCULATE() is not a SQL function."
    hint: "Arithmetic in SQL SELECT works like arithmetic in any formula: just write the expression."
    reflectionPrompt: "What would the calculated column be named in the result set if you don't provide an alias?"
  - id: de-app-m3-06-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      If a row has `unit_price = 25.00` and `discount = NULL`, the expression `unit_price - discount` evaluates to ________.
    inputConfig:
      placeholder: "NULL"
    markingRule:
      matchMode: CONTAINS
      accepted: [NULL, null, "NULL (unknown)"]
      rejectedFeedback: "NULL represents an unknown value. Any arithmetic operation involving NULL produces NULL — because if one value is unknown, the result is also unknown. 25.00 - NULL = NULL. This is called NULL propagation. To handle this, use COALESCE(discount, 0) which substitutes 0 when discount is NULL: unit_price - COALESCE(discount, 0) = 25.00."
    hint: "NULL is not zero — it is 'unknown'. What do you get when you subtract something unknown?"
    reflectionPrompt: "How would you rewrite the expression to treat a NULL discount as 0?"
  - id: de-app-m3-06-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why it is better to calculate `total_price = unit_price * quantity` at query time rather than storing a `total_price` column in the database.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [sync, stale, update, derived, normalised, redundant, integrity, source]
      rejectedFeedback: "Storing a calculated value creates a redundant copy that can go out of sync with its source data. If unit_price is updated, the stored total_price must also be updated — and if that update fails or is forgotten, the database contains contradictory information. Calculating at query time always derives the correct result from the current source data, keeping the database normalised and consistent."
    hint: "What happens to the stored total_price if unit_price changes later?"
    reflectionPrompt: "In a table with 1 million rows, how many stored total_price values would need updating if the VAT rate changed?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `SELECT salary * 12 AS annual_salary FROM employees` return?"
    options:
      - "A new column called annual_salary containing 12 for every row"
      - "A new column called annual_salary containing each employee's monthly salary multiplied by 12"
      - "An error — SQL cannot multiply columns"
      - "A column called salary*12 with the monthly salary"
    correctIndex: 1
    feedback: "salary * 12 is an arithmetic expression that multiplies the salary column value by 12 for each row. AS annual_salary gives the result column a readable name (alias). The result is a derived column — it appears in the query result but is not stored in the database."
  - type: MULTIPLE_CHOICE
    question: "A products table has columns weight_kg and price_per_kg. Which expression calculates the total price?"
    options:
      - "price_per_kg + weight_kg"
      - "price_per_kg / weight_kg"
      - "price_per_kg * weight_kg"
      - "weight_kg - price_per_kg"
    correctIndex: 2
    feedback: "Total price = price per unit × number of units = price_per_kg × weight_kg. Multiplication (*) gives the total. Addition would be dimensionally wrong (adding price and weight). Division and subtraction are also incorrect for this calculation."
retrieval:
  recall: "Write a SELECT expression that calculates a 20% discount on a unit_price column."
  explain: "Explain what NULL propagation means in arithmetic expressions and how COALESCE can fix it."
  mistakeId:
    code: "storing total = quantity * unit_price as a column in the order_lines table"
    answer: "Storing a calculated value creates a derived column that can become inconsistent if the source values are updated without updating the derived column. The correct approach is to calculate unit_price * quantity at query time — it is always correct, costs nothing extra, and keeps the schema normalised."
---

# Hook

Data is rarely stored in exactly the form you need. A price is stored without VAT — but you need the VAT-inclusive price. A salary is monthly — but a report needs annual. Coordinates are in degrees — but a map needs them in radians. Order quantities and unit prices are stored separately — but a report needs the line total.

Calculated columns let you derive new values from existing data inside your SELECT query, without modifying the database. They are computed at query time, appear in the result, and then disappear — the database remains unchanged.

# Lore Introduction

"The inventory shows unit prices and quantities," the merchant said. "But I need the total value of each product line — price times quantity." Master Selvaris wrote: `SELECT name, unit_price, stock_qty, unit_price * stock_qty AS inventory_value FROM products;`. "The archive does not store inventory_value," she explained. "We calculate it on the fly. The arithmetic happens in the archive, in the query, and the result appears as a column named inventory_value." The merchant reviewed the output. "The values are correct — and I didn't have to write them into the archive manually." Selvaris nodded. "Calculated columns keep the archive lean and always accurate. If a price changes, the inventory value is automatically correct the next time you query."

# Core Learning

## Concept Introduction

### Arithmetic in SELECT

SQL supports standard arithmetic operators directly in the SELECT list:

| Operator | Meaning | Example |
|---|---|---|
| `+` | Addition | `salary + bonus` |
| `-` | Subtraction | `price - discount` |
| `*` | Multiplication | `unit_price * quantity` |
| `/` | Division | `total / count` |
| `%` | Modulo (remainder) | `id % 2` |

```sql
-- Line total from order_lines
SELECT
    product_id,
    unit_price,
    quantity,
    unit_price * quantity AS line_total
FROM order_lines;

-- Annual salary from monthly
SELECT
    name,
    monthly_salary,
    monthly_salary * 12 AS annual_salary
FROM employees;

-- Price with VAT
SELECT
    name,
    unit_price,
    unit_price * 1.20 AS price_inc_vat
FROM products;

-- Discount calculation
SELECT
    name,
    list_price,
    list_price * 0.85 AS sale_price,
    list_price * 0.15 AS discount_amount
FROM products;
```

### Calculated vs Stored Columns

```
Calculated: SELECT unit_price * quantity AS total FROM order_lines
  → computed at runtime, not stored, always accurate

Stored: total DECIMAL(10,2) column in order_lines
  → must be updated manually when unit_price or quantity changes
  → can become inconsistent → data integrity risk
```

Rule of thumb: if a value can be derived from other columns in the same row, calculate it — don't store it.

### NULL Propagation in Arithmetic

```sql
-- If discount is NULL, the result is NULL
SELECT unit_price - discount AS discounted_price FROM products;
-- row: unit_price=25.00, discount=NULL → discounted_price = NULL

-- Fix with COALESCE: treat NULL as 0
SELECT unit_price - COALESCE(discount, 0) AS discounted_price FROM products;
-- row: unit_price=25.00, discount=NULL → discounted_price = 25.00
```

**COALESCE(value, fallback)** returns the first non-NULL value — a safe way to substitute defaults.

### Integer Division

In most databases, dividing two integers performs integer division (truncates the decimal):

```sql
-- Integer division in PostgreSQL/MySQL
SELECT 7 / 2;       -- returns 3 (not 3.5)
SELECT 7.0 / 2;     -- returns 3.5 (cast to decimal)
SELECT 7 / 2.0;     -- returns 3.5
SELECT CAST(7 AS DECIMAL) / 2;  -- returns 3.5
```

Be aware of this when dividing column values that may be integers.

## Why It Matters

Calculated columns are where SQL stops just retrieving data and starts answering questions — turning what's stored into what's needed:

- Stored data is deliberately raw: price and quantity exist, line total is *derived*; computing at query time means it's never stale
- Unit conversions, age from a birthdate, margin from cost and price — every report is full of these
- Doing arithmetic in the database rather than exporting to a spreadsheet keeps the logic close to the data, repeatable, and shareable

This is also where NULL arithmetic first bites — anything plus NULL is NULL — a rule that explains many mysteriously empty report columns.

## Common Mistakes

- **Not aliasing the calculated column**: `unit_price * quantity` appears as the column name. Add `AS line_total` for readability.
- **Forgetting NULL propagation**: `price - discount` is NULL when discount is NULL. Use COALESCE.
- **Integer division truncation**: `total / count` truncates if both are integers. Cast one to DECIMAL.
- **Storing derived values**: Creates data integrity risk. Calculate at query time instead.

## Mental Model

Think of calculated columns as a calculator sitting alongside each row as the database reads it. For every row, the database evaluates your expression with that row's values, writes the result into the output column, and moves to the next row. Nothing is stored — it's pure computation at read time. The database is doing arithmetic that you would otherwise have to do in application code, row by row. Letting the database do it is faster, simpler, and more accurate.

## Mini Summary

- ✔ Arithmetic expressions in SELECT: `+`, `-`, `*`, `/`
- ✔ Calculated columns are computed at query time — not stored
- ✔ NULL propagates: any arithmetic with NULL produces NULL
- ✔ Use COALESCE(value, default) to handle NULL values safely
- ✔ Integer / integer = integer (truncated) — cast to DECIMAL when needed

# Guided Practice Quest

Work through the guided steps to write calculated column expressions, handle NULL propagation with COALESCE, and avoid integer division truncation.

# Solo Practice Quest

You are writing queries for a retail analytics dashboard. The `order_lines` table has: `line_id`, `product_id`, `product_name`, `unit_price`, `quantity`, `discount_pct` (may be NULL), `vat_rate` (always 0.20). Write four SELECT queries: (1) calculate line total before discount, (2) calculate discounted price using COALESCE to treat NULL discount as 0%, (3) calculate VAT-inclusive line total after discount, (4) calculate the profit margin if `cost_price` is also provided, using integer-safe division. For each query, explain what it calculates and note any NULL or type-safety considerations.

# Integration

**Mathematics**: Calculated columns in SQL are function applications in the mathematical sense. Given a function f: ℝ × ℝ → ℝ defined by f(p, q) = p × q (price times quantity), a SELECT expression applies this function to each row of the table, producing a new relation with an additional column. This is the relational algebra operation of extension (ε), which adds a new computed attribute to a relation. SQL's NULL propagation corresponds to the mathematical treatment of partial functions — when one argument is undefined (NULL), the function result is also undefined. COALESCE is a completion of the partial function — defining a total function by providing a default for the undefined cases.

**Sciences (Physics)**: Calculated columns correspond directly to derived quantities in physics. Fundamental measurements (length, time, mass) are analogous to stored columns. Derived quantities (velocity = distance/time, kinetic energy = ½mv²) are analogous to calculated columns — computed from fundamental measurements rather than measured directly. Just as it would be redundant and error-prone to store kinetic energy separately from mass and velocity (requiring an update whenever mass changes), database normalisation discourages storing values derivable from other columns.

# Lore Conclusion

"The inventory report is complete," the merchant said, reviewing the query output. Columns for unit price, quantity, and inventory value — all accurate, all derived from the two source columns. "If I update a price tomorrow," Master Selvaris told her, "run the query again. The inventory value column updates automatically." The merchant smiled. "I don't have to recalculate anything." Selvaris nodded. "That is the advantage of calculated columns. They are not data — they are formulas. Formulas are always correct. Stored calculations can become wrong. The archive stores facts; the query derives everything else."

---
