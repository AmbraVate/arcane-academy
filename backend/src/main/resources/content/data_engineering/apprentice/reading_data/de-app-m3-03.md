---
id: de-app-m3-03
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
lesson: filtering_with_where
title: "Filtering with WHERE"
sortOrder: 3
difficulty: 1
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m3-02]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Writes correct WHERE clauses using comparison operators
    - Combines conditions with AND and OR correctly, including operator precedence
    - Uses BETWEEN, IN, LIKE, and IS NULL appropriately with examples
    - Explains why NULL requires IS NULL rather than = NULL
    - Reflects on how WHERE conditions correspond to business rules
  keywords: [WHERE, filter, condition, AND, OR, NOT, BETWEEN, IN, LIKE, IS NULL, comparison, predicate]
  modelAnswer: |
    The WHERE clause filters rows returned by a SELECT — only rows where the condition evaluates to TRUE are included. Comparison operators (=, !=, <, >, <=, >=) compare column values. AND requires both conditions to be true; OR requires either to be true; AND takes precedence over OR (use parentheses to control this). BETWEEN checks a range inclusively; IN checks membership in a list; LIKE matches patterns with % (any characters) and _ (one character). IS NULL checks for absent values — = NULL never works because NULL = NULL evaluates to NULL (not TRUE) in SQL. WHERE conditions translate directly to business rules: "show me active customers who spent more than £500."
guidedSteps:
  - id: de-app-m3-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which WHERE clause correctly retrieves products where the price is between £10 and £50 (inclusive)?
    inputConfig:
      options:
        - "WHERE price > 10 AND price < 50"
        - "WHERE price BETWEEN 10 AND 50"
        - "WHERE price IN (10, 50)"
        - "WHERE price >= 10 OR price <= 50"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["WHERE price BETWEEN 10 AND 50"]
      rejectedFeedback: "BETWEEN 10 AND 50 is inclusive — it includes rows where price = 10 and price = 50. Option A uses strict inequality (> 10 AND < 50) which excludes the boundary values. Option C (IN) checks for exact values 10 and 50 only — not the range. Option D (OR) would return all rows because a price >= 10 OR <= 50 is almost always true."
    hint: "BETWEEN is inclusive on both ends. Option A uses > and <, which are exclusive."
    reflectionPrompt: "What is the equivalent WHERE clause using >= and <= that produces the same result as BETWEEN 10 AND 50?"
  - id: de-app-m3-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To check whether a column value is absent (has no value), you must use ________ rather than = NULL, because NULL cannot be compared using equality operators in SQL.
    inputConfig:
      placeholder: "IS NULL"
    markingRule:
      matchMode: CONTAINS
      accepted: ["IS NULL", "is null", "IS NULL / IS NOT NULL"]
      rejectedFeedback: "In SQL, NULL represents an unknown or absent value. Because NULL is 'unknown', comparing it with = produces NULL (not TRUE or FALSE). The expression column = NULL is always NULL — never TRUE. You must use IS NULL to test for absent values and IS NOT NULL to test for present ones. This is one of SQL's most common gotchas."
    hint: "The special NULL-aware comparison operator in SQL."
    reflectionPrompt: "What does the query SELECT * FROM customers WHERE email = NULL return, and why is this wrong?"
  - id: de-app-m3-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain the difference between AND and OR in a WHERE clause, and give one example where using OR instead of AND would return a very different (and likely unintended) result.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [AND, OR, both, either, all, any, condition, rows, different, result]
      rejectedFeedback: "AND requires ALL conditions to be true for a row to be included — it narrows the result. OR requires ANY condition to be true — it widens the result. For example: WHERE country = 'UK' AND status = 'active' returns only active UK customers. WHERE country = 'UK' OR status = 'active' returns all UK customers AND all active customers worldwide — likely a much larger and unintended set."
    hint: "AND narrows; OR widens. What happens to the number of rows as you change AND to OR?"
    reflectionPrompt: "If a WHERE clause has three OR conditions, is the result larger or smaller than with AND? Why?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which query returns products in categories 'Electronics', 'Books', or 'Clothing' without using OR three times?"
    options:
      - "WHERE category = 'Electronics' OR 'Books' OR 'Clothing'"
      - "WHERE category IN ('Electronics', 'Books', 'Clothing')"
      - "WHERE category BETWEEN 'Electronics' AND 'Clothing'"
      - "WHERE category LIKE 'Electronics, Books, Clothing'"
    correctIndex: 1
    feedback: "IN (value1, value2, value3) tests whether a column value matches any value in the list — a compact alternative to multiple OR conditions. Option A is invalid SQL syntax (you cannot use OR without repeating the column name). BETWEEN on text compares alphabetically and doesn't match a discrete list. LIKE with a full string doesn't match individual values."
  - type: MULTIPLE_CHOICE
    question: "What does the pattern `LIKE 'J%'` match?"
    options:
      - "Any value containing the letter J"
      - "Any value that is exactly one character starting with J"
      - "Any value starting with J, followed by any characters"
      - "Any value starting with J and ending with any single character"
    correctIndex: 2
    feedback: "% in a LIKE pattern matches zero or more of any characters. 'J%' matches anything starting with J: 'John', 'Jane', 'J', 'JavaScript'. To match J anywhere, use '%J%'. To match a single character, use _ (underscore): 'J_' matches 'Jo' but not 'John'."
retrieval:
  recall: "Name four WHERE clause operators and give one example of each."
  explain: "Explain why WHERE column = NULL never works in SQL and what you must use instead."
  mistakeId:
    code: "WHERE status = NULL"
    answer: "NULL represents an unknown value. In SQL, NULL = NULL evaluates to NULL (not TRUE), so the condition never matches any row — even rows where status IS NULL. You must use WHERE status IS NULL to find rows with absent values. This is one of the most common SQL mistakes."
---

# Hook

Selecting all rows from a table is rarely what you want. A customer service query needs *this* customer's orders, not all orders. A monthly report needs orders from *this* month. A stock alert needs products where stock is *below* ten units.

The WHERE clause is what turns a full-table read into a targeted retrieval. It is the single most important SQL clause for writing useful, efficient queries — and it is where most beginners make their first mistakes (especially around NULL).

# Lore Introduction

"Find all scrolls from the Third Age about elemental magic," the Archivist requested. Without SQL, this meant searching ten million scrolls by hand. Master Selvaris wrote: `SELECT * FROM scrolls WHERE era = 'Third Age' AND subject = 'Elemental Magic';`. "The WHERE clause is the archive's filter," she explained. "Only rows where both conditions are true pass through. The archive does not even look at the other ten million scrolls — it goes directly to the matching ones." She pointed to the result: thirty-seven scrolls. "From ten million to thirty-seven, in milliseconds. That is the power of a well-written WHERE clause."

# Core Learning

## Concept Introduction

### Basic WHERE Syntax

```sql
SELECT column1, column2
FROM table_name
WHERE condition;
```

Only rows where `condition` evaluates to TRUE are returned.

### Comparison Operators

| Operator | Meaning | Example |
|---|---|---|
| `=` | Equal to | `WHERE status = 'active'` |
| `!=` or `<>` | Not equal to | `WHERE country != 'UK'` |
| `<` | Less than | `WHERE price < 50` |
| `>` | Greater than | `WHERE stock_qty > 0` |
| `<=` | Less than or equal | `WHERE age <= 18` |
| `>=` | Greater than or equal | `WHERE rating >= 4.0` |

### Combining Conditions: AND, OR, NOT

```sql
-- AND: both conditions must be true
SELECT * FROM orders
WHERE status = 'shipped' AND total_amount > 100;

-- OR: either condition is true
SELECT * FROM products
WHERE category = 'Books' OR category = 'Electronics';

-- NOT: negates a condition
SELECT * FROM users
WHERE NOT is_deleted;
```

**Operator precedence**: AND evaluates before OR. Use parentheses to control this:

```sql
-- WITHOUT parentheses: "active UK customers" OR "all VIP customers"
WHERE country = 'UK' AND status = 'active' OR tier = 'VIP'

-- WITH parentheses: "UK or US customers" who are active
WHERE (country = 'UK' OR country = 'US') AND status = 'active'
```

### BETWEEN — Range Check (Inclusive)

```sql
-- Inclusive: includes rows where price = 10.00 and price = 49.99
WHERE unit_price BETWEEN 10.00 AND 49.99

-- Equivalent to:
WHERE unit_price >= 10.00 AND unit_price <= 49.99

-- Works on dates too
WHERE order_date BETWEEN '2026-01-01' AND '2026-03-31'
```

### IN — Membership in a List

```sql
-- More readable than multiple ORs
WHERE country IN ('UK', 'US', 'Canada', 'Australia')

-- NOT IN — exclude a list
WHERE status NOT IN ('cancelled', 'refunded')
```

### LIKE — Pattern Matching

| Pattern | Matches |
|---|---|
| `'J%'` | Starts with J |
| `'%smith'` | Ends with smith |
| `'%coffee%'` | Contains coffee |
| `'J__n'` | J + any 2 chars + n (e.g. John, Joan) |
| `'_'` | Exactly one character |

```sql
-- Find products with 'wireless' in the name (case-insensitive in most databases)
WHERE name LIKE '%wireless%'

-- Find customers whose postcode starts with 'SW'
WHERE postcode LIKE 'SW%'
```

### IS NULL / IS NOT NULL

```sql
-- Find orders with no assigned delivery date
WHERE delivered_at IS NULL

-- Find customers who have provided a phone number
WHERE phone IS NOT NULL
```

**Critical**: `WHERE phone = NULL` never returns any rows. NULL is not equal to anything — not even to another NULL. Always use `IS NULL`.

### Complete Example

```sql
SELECT order_id, customer_id, total_amount, status
FROM orders
WHERE
    total_amount >= 100
    AND status IN ('processing', 'shipped')
    AND order_date BETWEEN '2026-01-01' AND '2026-06-30'
    AND customer_id IS NOT NULL;
```

## Common Mistakes

- **`= NULL` instead of `IS NULL`**: Returns zero rows. Always use `IS NULL`.
- **Forgetting AND/OR precedence**: `WHERE a = 1 AND b = 2 OR c = 3` is not the same as `WHERE a = 1 AND (b = 2 OR c = 3)`. Use parentheses.
- **LIKE '%value'** on large tables without an index: Leading wildcards prevent index use — very slow on large tables.
- **NOT IN with a NULL in the list**: `WHERE id NOT IN (1, 2, NULL)` returns zero rows because `id != NULL` is NULL (unknown) for every row.

## Mental Model

Think of WHERE as a sieve you hold over the table. Each condition is a hole size — only rows that fit through all the holes (AND) or fit through any hole (OR) make it into the result. BETWEEN is a range-shaped hole. IN is a set of discrete holes. LIKE is a pattern-shaped hole. IS NULL is a hole that only fits empty containers. The database pours all rows through your sieve and collects only what passes.

## Mini Summary

- ✔ WHERE filters rows — only TRUE conditions pass through
- ✔ `=`, `!=`, `<`, `>`, `<=`, `>=` — comparison operators
- ✔ AND (both true), OR (either true), NOT (negate) — logical operators
- ✔ BETWEEN (range, inclusive), IN (list), LIKE (pattern), IS NULL (absent value)
- ✔ `= NULL` never works — always use `IS NULL`

# Guided Practice Quest

Work through the guided steps to write WHERE conditions for realistic business scenarios, correctly apply BETWEEN, IN, and IS NULL, and understand AND/OR precedence.

# Solo Practice Quest

You are querying an `employees` table with columns: `employee_id`, `name`, `department`, `hire_date`, `salary`, `manager_id`, `is_remote`. Write seven WHERE clause queries: (1) employees in the Engineering or Product department earning over £60,000, (2) employees hired between 2020 and 2023, (3) remote employees whose manager is not yet assigned (IS NULL), (4) employees with names starting with 'A', (5) employees NOT in the Sales or Marketing department, (6) employees earning between £40,000 and £80,000 in the Engineering department, (7) employees whose salary is known (IS NOT NULL) and is above the company average (assume average is £52,000). For each query, write the full SELECT statement with appropriate column selection and explain what business question it answers.

# Integration

**Mathematics**: WHERE conditions are logical predicates — Boolean-valued functions over the domain of table rows. A WHERE clause with multiple conditions is a compound predicate formed by the logical connectives ∧ (AND), ∨ (OR), and ¬ (NOT) — directly corresponding to Boolean algebra operations. SQL's three-valued logic (TRUE, FALSE, NULL) is an extension of Boolean logic to handle absent values, where NULL propagates through logical operations: NULL AND TRUE = NULL, NULL OR TRUE = TRUE. Understanding this three-valued logic explains the counterintuitive behaviour of NULL in WHERE conditions.

**Sciences (Biology — Classification)**: WHERE clauses map directly to taxonomic classification criteria in biology. A biologist querying a species database might write WHERE kingdom = 'Animalia' AND phylum = 'Chordata' AND class = 'Mammalia' — selecting all mammals. The BETWEEN operator corresponds to range-based criteria like body mass between 10g and 500kg. LIKE corresponds to partial name matching for species with naming variations. IS NULL identifies records where classification is incomplete. Biological databases use exactly these SQL constructs to filter the millions of species in modern taxonomic databases.

# Lore Conclusion

"Thirty-seven scrolls from ten million," the Archivist said, reviewing the results. "All Third Age. All elemental magic. Exactly what I needed." Master Selvaris nodded. "The WHERE clause is precision. Without it, a query is a blunt instrument — it returns everything and leaves the filtering to the reader. With it, the archive does the filtering itself, at the speed of its index structures." She turned to her apprentice. "Every business question has conditions: active customers, recent orders, unassigned staff. WHERE translates those conditions into the language the archive understands. Learn to write it precisely, and you can answer any question the data contains."

---
