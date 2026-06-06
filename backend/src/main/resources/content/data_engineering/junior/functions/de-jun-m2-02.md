---
id: de-jun-m2-02
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m2
moduleTitle: "Module 2: Database Programming"
moduleGlyph: "🔧"
moduleSortOrder: 2
topicSlug: functions
topicTitle: "Functions"
topicSortOrder: 2
lesson: functions
title: "Database Functions"
sortOrder: 2
difficulty: 3
estimatedMinutes: 25
xpReward: 45
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m2-01]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Distinguishes scalar functions, table-valued functions, and aggregate functions
    - Writes a scalar user-defined function with a RETURNS type and RETURN statement
    - Explains the key difference between functions and stored procedures
    - Identifies when a function is usable in a SELECT, WHERE, or JOIN clause
    - Describes the impact of non-deterministic or side-effect functions on query optimisation
  keywords: [function, scalar, table-valued, UDF, RETURNS, RETURN, deterministic, side effect, SELECT, WHERE, aggregate, pure]
  modelAnswer: |
    A database function always returns a value: scalar functions return a single value and can appear in SELECT, WHERE, and JOIN clauses. Table-valued functions return a result set (a virtual table). Aggregate functions (like custom COUNT variants) accept a set of rows and return one value. Unlike procedures, functions must return a value, cannot contain COMMIT/ROLLBACK, and are usable in expressions. Deterministic functions (same inputs always produce the same output) allow optimiser caching. Non-deterministic functions (RAND(), NOW()) are re-evaluated per row. Side effects (modifying data) inside functions are generally prohibited or discouraged — keep functions pure for composability and optimisation.
guidedSteps:
  - id: de-jun-m2-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of these correctly identifies the key difference between a function and a stored procedure?
    inputConfig:
      options:
        - "Functions are slower than procedures"
        - "Functions must return a value and can be used in SQL expressions; procedures cannot be used in SELECT or WHERE"
        - "Procedures can have parameters; functions cannot"
        - "Functions run on the client; procedures run on the server"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Functions must return a value and can be used in SQL expressions; procedures cannot be used in SELECT or WHERE"]
      rejectedFeedback: "The fundamental distinction: a function returns a value (scalar or table) and can appear anywhere a value or table can appear in SQL — in SELECT, WHERE, JOIN ON, HAVING. A stored procedure is a named operation invoked with CALL; it cannot appear inside a SELECT or WHERE clause. Functions also generally cannot modify data (no INSERT/UPDATE/DELETE) or control transactions (no COMMIT/ROLLBACK) — they are designed to compute and return, not to perform actions. Procedures are designed to perform actions; functions are designed to compute values."
    hint: "Where can you use a function in SQL that you cannot use a procedure?"
    reflectionPrompt: "Can you call a stored procedure inside a WHERE clause? Why or why not?"
  - id: de-jun-m2-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A function that always returns the same output for the same input (no randomness, no dependency on time or state) is called ________.
    inputConfig:
      placeholder: "deterministic"
    markingRule:
      matchMode: CONTAINS
      accepted: [deterministic, "deterministic function", pure, idempotent]
      rejectedFeedback: "A deterministic function always returns the same result for the same inputs, regardless of when or how many times it is called. Examples: LENGTH('hello') always returns 5; LOWER('ABC') always returns 'abc'. Non-deterministic functions return different values on different calls: NOW() returns different timestamps; RAND() returns different numbers; functions that read from tables may return different results as data changes. Deterministic functions are important for query optimisation (the optimiser can evaluate them once) and for use in computed columns and indexes (non-deterministic functions cannot be indexed)."
    hint: "The mathematical property where f(x) = f(x) always — the function has no side effects or time dependency."
    reflectionPrompt: "Is UPPER('hello') deterministic? Is UUID() deterministic? How does this affect their use in indexed computed columns?"
  - id: de-jun-m2-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain what a table-valued function is and give a practical use case where it would be more useful than a scalar function.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [table, result set, rows, multiple, JOIN, parameterised, view, query, FROM]
      rejectedFeedback: "A table-valued function returns a result set (multiple rows and columns) instead of a single value. It can be used in the FROM clause or as the right side of a JOIN — essentially a parameterised view. Use case: a function get_customer_orders(customer_id, start_date, end_date) that returns all orders for that customer in the date range — the caller can JOIN to this function with different parameters without repeating the query. This is more flexible than a view (which cannot accept parameters) and more reusable than a subquery (named and callable from multiple queries)."
    hint: "What can a table-valued function do that a scalar function cannot? Think about what it returns and where it can appear."
    reflectionPrompt: "Compare a table-valued function to a view. What does a table-valued function provide that a view cannot?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You write a scalar function that runs an UPDATE inside it. What typically happens?"
    options:
      - "The function works normally — functions can modify data"
      - "Most databases prohibit data modification inside scalar functions; it causes an error"
      - "The UPDATE is queued and executed after the function returns"
      - "The function only modifies data if called from a procedure"
    correctIndex: 1
    feedback: "Most databases (PostgreSQL, SQL Server) prohibit data modification statements (INSERT, UPDATE, DELETE) inside scalar functions used in queries. The rationale: if a function were called in a SELECT that processes 1M rows, it would execute the UPDATE 1M times, which would be unexpected and dangerous. PostgreSQL raises an error if a function with VOLATILE classification attempts to modify data during a read query. Functions should be pure computations — they take inputs and return outputs without side effects. If you need to modify data, use a stored procedure."
  - type: MULTIPLE_CHOICE
    question: "Which is the correct way to use a scalar function called calculate_tax(price, rate) in a query?"
    options:
      - "CALL calculate_tax(ol.unit_price, 0.20) FROM order_lines ol"
      - "SELECT ol.product_id, ol.unit_price, calculate_tax(ol.unit_price, 0.20) AS tax FROM order_lines ol"
      - "FROM calculate_tax(ol.unit_price, 0.20) SELECT *"
      - "WHERE calculate_tax(ol.unit_price, 0.20)"
    correctIndex: 1
    feedback: "Scalar functions appear in SQL expressions exactly like built-in functions — in the SELECT list, WHERE, JOIN ON, HAVING, or ORDER BY. calculate_tax(ol.unit_price, 0.20) is an expression that evaluates to a single decimal value for each row. CALL is for stored procedures, not functions. FROM is for table-valued functions (which return rows). WHERE without a comparison operator is not valid SQL. Scalar functions are used as value expressions: they produce a value that can be compared, displayed, or used in further calculations."
retrieval:
  recall: "Write a scalar function that accepts an order total and returns the tier classification: 'Bronze' (< £100), 'Silver' (£100–£500), 'Gold' (> £500)."
  explain: "Explain the difference between scalar functions, table-valued functions, and aggregate functions, giving one practical use case for each."
  mistakeId:
    code: "CREATE FUNCTION get_latest_order_date(p_customer_id INT) RETURNS DATE BEGIN SELECT MAX(order_date) FROM orders WHERE customer_id = p_customer_id; END"
    answer: "This function is missing the RETURN statement. The SELECT computes the value but does not return it — the function would return NULL or cause an error depending on the database. The fix: DECLARE v_date DATE; SELECT MAX(order_date) INTO v_date FROM orders WHERE customer_id = p_customer_id; RETURN v_date; The SELECT INTO pattern stores the query result into a local variable, which is then returned. In PostgreSQL PL/pgSQL: RETURN (SELECT MAX(order_date) FROM orders WHERE customer_id = p_customer_id); is the cleaner form."
---

# Hook

Stored procedures perform actions. Functions compute values. The distinction matters because functions can participate in SQL expressions — in SELECT lists, WHERE clauses, JOIN conditions — in ways that procedures cannot. User-defined functions extend SQL's built-in vocabulary with domain-specific computations that live in the database.

# Lore Introduction

"Every query that calculates member tier has the same CASE WHEN logic duplicated across twelve different reports," the Junior Engineer said. "When the tier thresholds changed last month, we updated eleven of them. Missed one." The Senior Archivist shook her head. "That's the problem with logic that belongs in one place but lives in many. Write a function. One definition. Every query uses it. The threshold changes once, everywhere." She opened the editor. "A function is SQL's version of DRY — Don't Repeat Yourself. Write it once; call it from SELECT, WHERE, anywhere you need a value."

# Core Learning

## Concept Introduction

### Scalar Functions

```sql
-- MySQL: scalar function returning member tier based on borrowing count
DELIMITER $$

CREATE FUNCTION member_tier(p_borrow_count INT)
RETURNS VARCHAR(20)
DETERMINISTIC
BEGIN
    DECLARE v_tier VARCHAR(20);
    IF p_borrow_count >= 100 THEN
        SET v_tier = 'Archivist';
    ELSEIF p_borrow_count >= 50 THEN
        SET v_tier = 'Scholar';
    ELSEIF p_borrow_count >= 10 THEN
        SET v_tier = 'Reader';
    ELSE
        SET v_tier = 'Initiate';
    END IF;
    RETURN v_tier;
END$$

DELIMITER ;

-- Use in SELECT
SELECT name, total_borrowings,
       member_tier(total_borrowings) AS tier
FROM members;

-- Use in WHERE
SELECT name FROM members
WHERE member_tier(total_borrowings) = 'Archivist';
```

```sql
-- PostgreSQL: same function in PL/pgSQL
CREATE OR REPLACE FUNCTION member_tier(p_borrow_count INT)
RETURNS VARCHAR
LANGUAGE plpgsql
IMMUTABLE    -- equivalent to DETERMINISTIC
AS $$
BEGIN
    RETURN CASE
        WHEN p_borrow_count >= 100 THEN 'Archivist'
        WHEN p_borrow_count >= 50  THEN 'Scholar'
        WHEN p_borrow_count >= 10  THEN 'Reader'
        ELSE 'Initiate'
    END;
END;
$$;
```

### Table-Valued Functions

```sql
-- PostgreSQL: returns a result set
CREATE OR REPLACE FUNCTION get_customer_orders(
    p_customer_id INT,
    p_start_date  DATE,
    p_end_date    DATE
)
RETURNS TABLE (
    order_id     INT,
    order_date   DATE,
    total_amount DECIMAL(10,2),
    status       VARCHAR(20)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT o.order_id, o.order_date::DATE, o.total_amount, o.status
    FROM orders o
    WHERE o.customer_id = p_customer_id
      AND o.order_date BETWEEN p_start_date AND p_end_date;
END;
$$;

-- Use in FROM (like a table)
SELECT * FROM get_customer_orders(42, '2024-01-01', '2024-03-31');

-- Use in JOIN
SELECT c.name, co.order_id, co.total_amount
FROM customers c
JOIN get_customer_orders(c.customer_id, '2024-01-01', '2024-03-31') co
     ON TRUE;   -- table-valued function called with a correlated parameter
```

### SQL Server Inline Table-Valued Function

```sql
-- SQL Server: inline TVF (returns a query result directly)
CREATE FUNCTION dbo.GetCustomerOrders (
    @customer_id INT,
    @start_date  DATE,
    @end_date    DATE
)
RETURNS TABLE
AS
RETURN (
    SELECT order_id, order_date, total_amount, status
    FROM orders
    WHERE customer_id = @customer_id
      AND order_date BETWEEN @start_date AND @end_date
);
```

### Determinism and Optimisation

```sql
-- IMMUTABLE / DETERMINISTIC: same inputs → same output, always
-- Can be used in index expressions and materialised views
CREATE INDEX idx_member_tier ON members (member_tier(total_borrowings));

-- STABLE: same output within a single query/transaction, but may vary across
-- READ ONLY but can read from the database (results may vary as data changes)

-- VOLATILE: may return different results each call (NOW(), RAND(), etc.)
-- Cannot be used in index expressions; recalculated for every row

-- Function volatility classification matters for:
-- 1. Index creation on computed columns
-- 2. Materialised view refresh
-- 3. Query plan caching and optimisation
```

### Built-in vs User-Defined Functions

```sql
-- Built-in scalar functions (reference)
UPPER(str), LOWER(str), LENGTH(str), TRIM(str)
DATE_TRUNC('month', date), EXTRACT(YEAR FROM date)
COALESCE(val1, val2), NULLIF(val, 0)
ROUND(n, decimals), ABS(n), MOD(n, m)

-- User-defined functions extend this vocabulary for domain logic
-- business_day_diff(start_date, end_date) -- excludes weekends
-- formatted_address(street, city, postcode) -- standardised address string
-- age_band(date_of_birth) -- '18-24', '25-34', etc.
```

## Common Mistakes

- **Missing RETURN statement**: The function body must explicitly return the value with RETURN — a SELECT alone does not return from the function.
- **Side effects in scalar functions**: Modifying data inside a scalar function used in a SELECT processes the modification once per row — usually an error or an unintended mass update.
- **Non-deterministic functions on indexed columns**: Applying VOLATILE functions in index expressions or WHERE clauses on indexed computed columns prevents index use.
- **Complex functions in WHERE on large tables**: A user-defined function in WHERE is evaluated per row. If the function calls other tables or is expensive, it runs millions of times on large tables — a significant performance risk.

## Mental Model

A function is vocabulary for the SQL language — a custom word that the database understands. Built-in functions like UPPER() and AVG() are part of the standard vocabulary. User-defined functions add domain words: member_tier(), business_days_between(), format_phone_number(). Once defined, these words appear anywhere in SQL where a value is expected. The rule is the same as for pure mathematical functions: the same input must always produce the same output (deterministic). Functions that "do things" (side effects) break this contract and should be procedures instead.

## Mini Summary

- ✔ Scalar functions return one value; usable in SELECT, WHERE, JOIN, HAVING
- ✔ Table-valued functions return a result set; usable in FROM and JOIN
- ✔ Functions must have a RETURN statement; CALL is for procedures
- ✔ Deterministic/IMMUTABLE: same input → same output; enables index optimisation
- ✔ No data modification inside scalar functions — keep them pure computations

# Guided Practice Quest

Work through the guided steps to write a scalar function that classifies order size (Small/Medium/Large), use it in a SELECT and WHERE clause, then write a table-valued function that returns all items in a given category with borrowing counts.

# Solo Practice Quest

Build a library of user-defined functions for the Archive system: (1) days_overdue(due_date DATE) — returns the number of days an item is overdue (0 if not yet due); (2) loan_fee(days_overdue INT, item_category VARCHAR) — returns the fee charged, with different rates per category and a maximum cap; (3) member_standing(member_id INT) — returns 'Good', 'Warning', or 'Suspended' based on overdue items and unpaid fees (this will need to read from tables — what volatility classification should it have and why?); (4) active_loans(member_id INT) — a table-valued function returning all active loans for a member. For each function: write the CREATE FUNCTION statement, state the volatility classification and justify it, and write a sample query using the function.

# Integration

**Mathematics**: Database functions are computational implementations of mathematical functions f: X → Y mapping from a domain to a codomain. Scalar functions correspond to point-valued functions; table-valued functions to set-valued functions (functions that map to sets/relations). The determinism requirement (same input → same output) corresponds exactly to the definition of a well-defined function in mathematics — a relation f where for every x in the domain, there is exactly one y in the codomain such that (x, y) ∈ f. Non-deterministic "functions" like RAND() are technically random variables (mappings to probability distributions), not mathematical functions — hence their exclusion from index expressions, which require the determinism guarantee that mathematical functions provide.

**Sciences (Chemistry — Molecular Databases)**: Chemical databases store molecular structures and properties, and user-defined functions are essential for chemical computation. A function molecular_weight(smiles VARCHAR) converts a SMILES string to molecular mass. A function functional_group_count(smiles, group_type) counts occurrences of a chemical group. A table-valued function similar_molecules(smiles, threshold FLOAT) returns all database molecules within a Tanimoto similarity threshold. These functions encapsulate complex cheminformatics algorithms within the database — the same architectural pattern as the Archive's loan_fee and member_standing functions, but applied to molecular data in drug discovery and materials science databases.

# Lore Conclusion

"The tier classification function is deployed," the Junior Engineer reported. "Twelve reports now call member_tier() instead of duplicating the logic. I updated the thresholds once." The Senior Archivist reviewed the function. "DETERMINISTIC — correctly classified. The query optimiser can cache the result for repeated calls with the same argument." She ran the updated reports. "All consistent. Three lines of function definition replaced forty-eight lines of duplicated CASE WHEN logic." She paused. "Functions are reuse at the data layer. They speak the language of the domain. Next: triggers — functions that the database calls automatically when data changes."

---
