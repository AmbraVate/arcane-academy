---
id: de-jun-m2-01
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m2
moduleTitle: "Module 2: Database Programming"
moduleGlyph: "🔧"
moduleSortOrder: 2
topicSlug: stored_procedures
topicTitle: "Stored Procedures"
topicSortOrder: 1
lesson: stored_procedures
title: "Stored Procedures"
sortOrder: 1
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m1-05]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what a stored procedure is and why server-side logic matters
    - Writes basic CREATE PROCEDURE syntax with IN and OUT parameters
    - Describes the advantages and disadvantages of stored procedures
    - Explains when a stored procedure is preferable to application-side SQL
    - Identifies security benefits of stored procedures (parameterisation, least-privilege)
  keywords: [stored procedure, CREATE PROCEDURE, IN, OUT, INOUT, CALL, EXECUTE, server-side, reuse, security, injection, encapsulation, network]
  modelAnswer: |
    A stored procedure is a named, reusable block of SQL (and procedural logic) stored in the database. It executes on the database server, reducing network round trips. IN parameters pass values into the procedure; OUT parameters return values to the caller; INOUT do both. Advantages: reusability, encapsulation of business logic, reduced network traffic, security (grant EXECUTE without exposing table structure), and prevention of SQL injection through parameterised queries. Disadvantages: harder to test and version control than application code, database-specific syntax (not portable), and mixing business logic into the database layer complicates architecture.
guidedSteps:
  - id: de-jun-m2-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A stored procedure reduces network traffic compared to application-side SQL. Why?
    inputConfig:
      options:
        - "Stored procedures are automatically cached by the database"
        - "The application sends one CALL statement; multiple SQL statements execute on the server without round trips"
        - "Stored procedures compress query results before sending"
        - "The application does not need a database connection to call a stored procedure"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The application sends one CALL statement; multiple SQL statements execute on the server without round trips"]
      rejectedFeedback: "Without a stored procedure, the application might send three SQL statements to the database: SELECT to check a condition, UPDATE to apply a change, and INSERT to log the action. Each statement is a network round trip. With a stored procedure, the application sends one CALL command; all three statements execute on the server in memory without network round trips between them. For high-volume operations (batch processing, transactional workflows), this reduces latency significantly."
    hint: "How many network messages does CALL procedure() require vs three separate SQL statements?"
    reflectionPrompt: "When would network round trips between application and database be a significant performance concern?"
  - id: de-jun-m2-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To call a stored procedure named process_order with a parameter, you use the ________ keyword (in MySQL/PostgreSQL).
    inputConfig:
      placeholder: "CALL"
    markingRule:
      matchMode: CONTAINS
      accepted: [CALL, call, EXECUTE, execute, EXEC, exec]
      rejectedFeedback: "In MySQL and PostgreSQL, stored procedures are called with CALL procedure_name(parameters). In SQL Server and some others, EXECUTE (or EXEC) is used. Example: CALL process_order(1234, 'confirmed'). This sends a single command to the database server, which then executes all the logic inside the procedure. Unlike functions, procedures can have OUT parameters that return values to the caller, can contain transaction control (COMMIT/ROLLBACK), and do not return a result set through the SELECT mechanism (though they can return result sets through OUT parameters or result cursors)."
    hint: "The keyword used to invoke a stored procedure (not SELECT, not FROM)."
    reflectionPrompt: "What is the difference between CALL in MySQL and EXECUTE in SQL Server?"
  - id: de-jun-m2-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain how stored procedures can prevent SQL injection, and why granting EXECUTE on a procedure is safer than granting direct table access.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [parameter, parameterised, injection, table, access, EXECUTE, structure, least privilege, expose, dynamic SQL]
      rejectedFeedback: "Stored procedures prevent SQL injection by using parameterised inputs — user-supplied values are always treated as data, not SQL code. The procedure's internal SQL is fixed at compile time; parameters cannot alter the structure of the query. Granting EXECUTE on a procedure is safer than granting SELECT/UPDATE/DELETE on tables because: the procedure exposes only specific operations (e.g. update a customer's email), not the full table; the caller never sees the schema; and the procedure can validate inputs before executing. This implements least-privilege: the caller can do what the procedure allows, nothing more."
    hint: "A stored procedure treats all inputs as data. How does this differ from building a SQL string from user input?"
    reflectionPrompt: "What is dynamic SQL in a stored procedure, and when does it re-introduce the SQL injection risk that parameterisation prevents?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of these is a significant disadvantage of stored procedures?"
    options:
      - "They cannot perform INSERT, UPDATE, or DELETE"
      - "They are database-specific (different syntax per vendor) and harder to version control than application code"
      - "They are slower than equivalent application code"
      - "They cannot accept parameters"
    correctIndex: 1
    feedback: "Stored procedure syntax varies significantly between databases: MySQL, PostgreSQL (PL/pgSQL), SQL Server (T-SQL), and Oracle (PL/SQL) are all different. This makes stored procedures non-portable — moving to a different database requires rewriting all procedures. Version control is also harder: procedures live in the database, not in the application's git repository, so code reviews, history tracking, and rollback require additional tooling (Flyway, Liquibase, or manual scripts). Application-side SQL in a versioned codebase is easier to manage in most team environments."
  - type: MULTIPLE_CHOICE
    question: "A stored procedure with an OUT parameter is used to return:"
    options:
      - "A result set the caller can iterate over"
      - "A single value back to the calling application"
      - "An error message if the procedure fails"
      - "The execution time of the procedure"
    correctIndex: 1
    feedback: "OUT parameters allow a stored procedure to return one or more scalar values to the caller. Example: CALL get_customer_stats(IN customer_id, OUT total_orders, OUT total_spend) — the caller passes the customer_id and receives total_orders and total_spend back in the OUT variables. INOUT parameters are both passed in and returned. For returning full result sets, procedures use SELECT statements (MySQL) or REF CURSOR (Oracle/PostgreSQL) — different from OUT parameter scalars."
retrieval:
  recall: "Write a stored procedure that accepts a customer_id and a new_status, updates that customer's status in the customers table, and logs the change to a customer_audit table with a timestamp."
  explain: "Compare stored procedures to application-side SQL on four dimensions: testability, portability, performance, and security."
  mistakeId:
    code: "CREATE PROCEDURE search_customers(IN search_term VARCHAR(100)) BEGIN SET @sql = CONCAT('SELECT * FROM customers WHERE name LIKE \"%', search_term, '%\"'); PREPARE stmt FROM @sql; EXECUTE stmt; END"
    answer: "This stored procedure uses dynamic SQL built by concatenating user input directly — reintroducing SQL injection despite being inside a stored procedure. A malicious search_term like 'x%\" OR 1=1 -- can alter the query. The fix: use parameterised LIKE directly without string concatenation: SELECT * FROM customers WHERE name LIKE CONCAT('%', search_term, '%'). This passes search_term as a parameter value, not as SQL text. Dynamic SQL should only be used when the query structure itself (table names, column names) must vary — never for filter values."
---

# Hook

SQL queries live in application code — strings sent to the database on each request. Stored procedures invert this: the logic lives in the database itself, named and reusable. The application calls the procedure by name; the database executes it. Understanding stored procedures means understanding when server-side logic is the right architectural choice.

# Lore Introduction

"The order processing workflow runs five separate SQL statements," the Junior Engineer said. "Every time an order is confirmed, the application sends five round trips to the database." The Senior Archivist looked up. "And if the network drops between statements three and four?" The Junior paused. "Partial update — inventory decremented but order status not updated." The Senior Archivist nodded. "A stored procedure wraps all five statements. One CALL. Runs on the server. Transactional. The network drop never happens mid-procedure." She opened the editor. "Let me show you. Understanding stored procedures is understanding when the database should own the logic."

# Core Learning

## Concept Introduction

### What is a Stored Procedure?

A stored procedure is a named block of SQL (and procedural logic) stored inside the database. It can accept parameters, execute multiple SQL statements, include conditional logic, and optionally return values.

```sql
-- MySQL syntax
DELIMITER $$

CREATE PROCEDURE confirm_order(
    IN p_order_id   INT,
    IN p_confirmed_by VARCHAR(100)
)
BEGIN
    -- Step 1: update order status
    UPDATE orders
    SET status = 'confirmed', confirmed_at = NOW()
    WHERE order_id = p_order_id;

    -- Step 2: decrement stock for each line item
    UPDATE products p
    JOIN order_lines ol ON p.product_id = ol.product_id
    SET p.stock_qty = p.stock_qty - ol.quantity
    WHERE ol.order_id = p_order_id;

    -- Step 3: log the action
    INSERT INTO order_audit (order_id, action, action_by, action_at)
    VALUES (p_order_id, 'confirmed', p_confirmed_by, NOW());
END$$

DELIMITER ;

-- Call the procedure
CALL confirm_order(1234, 'admin_user');
```

### Parameter Types

```sql
-- IN: value passed in, not returned
-- OUT: value returned to caller (must be a variable)
-- INOUT: passed in and returned (modified in place)

CREATE PROCEDURE get_customer_stats(
    IN  p_customer_id  INT,
    OUT p_order_count  INT,
    OUT p_total_spend  DECIMAL(10,2)
)
BEGIN
    SELECT COUNT(*), SUM(total_amount)
    INTO p_order_count, p_total_spend
    FROM orders
    WHERE customer_id = p_customer_id AND status = 'completed';
END;

-- Usage:
CALL get_customer_stats(42, @orders, @spend);
SELECT @orders, @spend;
```

### PostgreSQL Syntax (PL/pgSQL)

```sql
-- PostgreSQL uses CREATE OR REPLACE PROCEDURE (or FUNCTION for return values)
CREATE OR REPLACE PROCEDURE confirm_order(
    p_order_id     INT,
    p_confirmed_by VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE orders
    SET status = 'confirmed', confirmed_at = NOW()
    WHERE order_id = p_order_id;

    UPDATE products p
    SET stock_qty = p.stock_qty - ol.quantity
    FROM order_lines ol
    WHERE ol.order_id = p_order_id AND p.product_id = ol.product_id;

    INSERT INTO order_audit (order_id, action, action_by, action_at)
    VALUES (p_order_id, 'confirmed', p_confirmed_by, NOW());
END;
$$;

-- Call: CALL confirm_order(1234, 'admin_user');
```

### Conditional Logic in Procedures

```sql
DELIMITER $$

CREATE PROCEDURE apply_discount(
    IN  p_customer_id  INT,
    IN  p_order_id     INT,
    OUT p_discount_pct DECIMAL(5,2)
)
BEGIN
    DECLARE v_lifetime_spend DECIMAL(10,2);

    SELECT SUM(total_amount) INTO v_lifetime_spend
    FROM orders
    WHERE customer_id = p_customer_id AND status = 'completed';

    IF v_lifetime_spend > 10000 THEN
        SET p_discount_pct = 15.0;
    ELSEIF v_lifetime_spend > 5000 THEN
        SET p_discount_pct = 10.0;
    ELSEIF v_lifetime_spend > 1000 THEN
        SET p_discount_pct = 5.0;
    ELSE
        SET p_discount_pct = 0.0;
    END IF;

    UPDATE orders SET discount_pct = p_discount_pct
    WHERE order_id = p_order_id;
END$$

DELIMITER ;
```

### Stored Procedures vs Application SQL

| | Stored Procedure | Application SQL |
|---|---|---|
| Location | Database server | Application codebase |
| Network trips | One CALL | One per statement |
| Portability | Database-specific | Portable (mostly) |
| Version control | Harder (lives in DB) | Easy (in git) |
| Testing | Limited tooling | Full test framework |
| Security | Grant EXECUTE only | Must grant table access |
| Transaction control | Can COMMIT/ROLLBACK | Application manages |

## Common Mistakes

- **Using dynamic SQL for filter values**: Building SQL strings by concatenating user input inside a procedure reintroduces SQL injection. Use parameterised queries inside procedures.
- **Putting all business logic in stored procedures**: This makes the application harder to test, version, and migrate. Stored procedures are best for data-intensive operations (batch updates, multi-step transactions), not for all business logic.
- **Not handling errors**: Procedures that fail silently or mid-way leave data in an inconsistent state. Use transaction control (START TRANSACTION / COMMIT / ROLLBACK) and error handlers.
- **Vendor lock-in**: Writing complex stored procedures in T-SQL or PL/pgSQL makes database migration significantly harder.

## Mental Model

Think of a stored procedure as a "service endpoint" in the database — a named operation with defined inputs and outputs. Instead of the application knowing the internal SQL (table names, join conditions), it only knows the procedure name and parameters. This encapsulation is the same principle as a function in application code: callers know what the function does, not how. The tradeoff is that the function lives in a different system (the database) with different tooling, versioning, and testing conventions.

## Mini Summary

- ✔ Stored procedures are named, reusable SQL blocks stored in the database
- ✔ Parameters: IN (input), OUT (return value), INOUT (both)
- ✔ Benefits: reduced network round trips, encapsulation, security (grant EXECUTE not table access)
- ✔ Risks: vendor lock-in, harder to test and version control, dynamic SQL danger
- ✔ Best use: multi-step transactional operations, batch processing, controlled data access

# Guided Practice Quest

Work through the guided steps to write a stored procedure that processes an order (update status + decrement stock + log), add error handling with ROLLBACK, and identify the injection vulnerability in a provided dynamic SQL procedure and fix it.

# Solo Practice Quest

Design and implement stored procedures for the following operations: (1) register_member(name, email, tier) — inserts a new member and returns the new member_id as OUT parameter; (2) transfer_item(item_id, from_member_id, to_member_id) — transfers a borrowed item, validating that the from_member currently has the item; (3) monthly_summary(year INT, month INT) — produces a summary report of borrowings, returns, and overdue items for the given month; (4) archive_member(member_id) — marks a member as archived and moves their borrowing history to an archive table. For each procedure: write the CREATE PROCEDURE statement, describe what parameterisation approach you used and why, and identify any scenarios where the procedure might leave data in an inconsistent state and how you would handle them.

# Integration

**Mathematics**: Stored procedures are the database implementation of functions as first-class objects in a mathematical sense. A procedure with IN and OUT parameters is a mapping f: (domain of IN types) → (domain of OUT types). The body implements this mapping using the procedural SQL language. Error handling (ROLLBACK on failure) corresponds to partial functions — the mapping is undefined (raises an exception) for inputs that violate constraints. Transaction atomicity — all statements in the procedure commit or none do — corresponds to the mathematical concept of atomicity in algebra: an atomic operation either completes fully or has no effect, like an element in a group that either has an inverse or does not exist.

**Sciences (Healthcare — Clinical Decision Support)**: Stored procedures power clinical decision support systems (CDSS) in hospital information systems. A "prescribe medication" procedure checks drug interactions, validates dosage against patient weight and kidney function, logs the prescription, and updates inventory — multiple steps that must execute atomically. Stored procedures in CDSS are privileged operations: nurses and physicians execute the procedure, but the underlying medication and patient tables are protected. This is the least-privilege principle in a safety-critical context — practitioners can order medications through defined procedures without direct access to modify clinical records. The same architectural pattern applies to any domain where data integrity and access control are paramount.

# Lore Conclusion

"The order confirmation procedure runs in 12 milliseconds, all five operations atomic," the Junior Engineer reported. "If anything fails, it rolls back completely — no partial state." The Senior Archivist nodded. "And the application only needs EXECUTE permission on that procedure. It cannot touch the tables directly." She ran the procedure twice. "Consistent. Reusable. Secure." She closed the editor. "Stored procedures are a tool — use them for multi-step transactional operations and controlled data access. Keep business logic that needs testing and versioning in your application code. In the next lesson: functions — the return-value counterpart to procedures."

---
