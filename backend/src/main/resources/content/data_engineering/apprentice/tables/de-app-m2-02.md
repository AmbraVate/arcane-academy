---
id: de-app-m2-02
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m2
moduleTitle: "Module 2: Relational Database Foundations"
moduleGlyph: "🗄️"
moduleSortOrder: 2
topicSlug: tables
topicTitle: "Tables"
topicSortOrder: 1
lesson: rows_and_records
title: "Rows and Records"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m2-01]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Accurately defines a row/record in a relational database
    - Explains the relationship between a row and an entity instance
    - Describes what happens when a row is inserted, updated, or deleted
    - Explains the importance of row uniqueness and the role of the primary key
    - Reflects on how row-level data integrity is maintained
  keywords: [row, record, tuple, instance, insert, update, delete, primary key, unique, integrity]
  modelAnswer: |
    A row (also called a record or tuple) in a relational database represents one specific instance of the entity the table models. Each row contains one value per column, forming a complete description of that instance. Rows are identified uniquely by the primary key, which ensures no two rows represent the same real-world entity. When data changes, rows are updated; when an entity no longer exists, rows are deleted. Maintaining the integrity of rows — ensuring they are complete, valid, and unique — is a fundamental database concern.
guidedSteps:
  - id: de-app-m2-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In a `products` table with 500 rows, each row represents:
    inputConfig:
      options:
        - "One column definition in the table's schema"
        - "One specific product with its own attribute values"
        - "One SQL query that has been run against the table"
        - "One database user who has accessed the table"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["One specific product with its own attribute values"]
      rejectedFeedback: "Each row (record/tuple) is one instance of the entity the table models. In a products table, each row is a specific product — Product A with its name, price, and SKU; Product B with its own values; and so on."
    hint: "A row is to a table what a specific item is to a category."
    reflectionPrompt: "If the products table has 500 rows, what does that tell you about the business?"
  - id: de-app-m2-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence: "The SQL command used to add a new row to a table is ________."
    inputConfig:
      placeholder: "INSERT"
    markingRule:
      matchMode: CONTAINS
      accepted: [INSERT, insert, INSERT INTO, insert into]
      rejectedFeedback: "INSERT (specifically INSERT INTO) is the SQL command for adding new rows to a table. It specifies the table name, the columns to populate, and the values for each column."
    hint: "Think about the word used when adding something new to a collection."
    reflectionPrompt: "What happens if you try to INSERT a row that violates a NOT NULL constraint?"
  - id: de-app-m2-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why every row in a table must be uniquely identifiable, and what mechanism ensures this uniqueness.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [primary key, unique, identifier, identify, distinguish, duplicate, update, delete]
      rejectedFeedback: "Without a unique identifier, there is no reliable way to update, delete, or reference a specific row. The primary key is the mechanism that guarantees uniqueness — the database will reject any INSERT that would produce a duplicate primary key value."
    hint: "Consider what problems arise if two rows have identical values in all columns."
    reflectionPrompt: "What happens in a system without primary keys when you try to update a specific customer's email address?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which SQL command is used to modify the values in an existing row?"
    options: ["INSERT", "CREATE", "UPDATE", "ALTER"]
    correctIndex: 2
    feedback: "UPDATE modifies the values in one or more existing rows. It is always used with a WHERE clause to specify which rows to change — without WHERE, all rows are updated."
  - type: MULTIPLE_CHOICE
    question: "A primary key on a table ensures that:"
    options:
      - "All columns have values"
      - "No two rows have the same value in the primary key column(s)"
      - "The table cannot be deleted"
      - "Rows are sorted alphabetically"
    correctIndex: 1
    feedback: "A primary key is a uniqueness constraint applied to one or more columns. The database enforces that no two rows share the same primary key value, ensuring every row can be uniquely identified."
retrieval:
  recall: "In one sentence, define what a row (record) represents in a relational database table."
  explain: "Explain why a primary key is essential for maintaining data integrity at the row level."
  mistakeId:
    code: "running UPDATE without a WHERE clause"
    answer: "UPDATE without WHERE modifies every row in the table, not just the intended one. This is one of the most common and destructive SQL mistakes. Always include a WHERE clause that targets only the intended rows."
---

# Hook

Every database table starts empty — just a schema with no data. The moment a user registers, a product is added to the catalogue, or a transaction is processed, a **row** is born. Rows are where the actual data lives. They are the instances, the records, the real-world facts that all the careful schema design was preparing to receive.

Understanding rows — how they are created, identified, modified, and deleted — is fundamental to working with relational databases. And one question underlies all of it: how do you uniquely identify a single row out of millions? The answer is the primary key, and getting it right is more important than it might initially seem.

# Lore Introduction

"The schema defines the form," Master Selvaris said, holding up a blank ledger page with column headings printed across the top. "But the Archive only earns its keep when records are inscribed." She wrote a single entry across the first row: a merchant's name, guild, city, and licence number. "This is the instance — the real merchant, recorded. From this moment, this merchant exists in the Archive." She added two more rows. "Three merchants. Three instances. Three truths." She looked at the page carefully. "Now — how do we distinguish the first from the second if their names are the same?"

# Core Learning

## Concept Introduction

| Term | Synonym | Description |
|------|---------|-------------|
| **Row** | Record, Tuple | One complete instance of the entity represented by the table |
| **INSERT** | — | SQL command to add new rows to a table |
| **UPDATE** | — | SQL command to modify existing row values |
| **DELETE** | — | SQL command to remove rows from a table |
| **Primary Key** | PK | The column(s) that uniquely identify each row |
| **NULL** | — | The absence of a value for a column in a specific row |
| **CRUD** | — | Create, Read, Update, Delete — the four fundamental row operations |

### Row Operations in SQL

```sql
-- INSERT: add a new row
INSERT INTO customers (customer_id, first_name, last_name, email)
VALUES (1, 'Jane', 'Doe', 'jane@example.com');

-- SELECT: read rows (more detail in Module 3)
SELECT * FROM customers WHERE customer_id = 1;

-- UPDATE: modify an existing row
UPDATE customers
SET email = 'jane.doe@example.com'
WHERE customer_id = 1;

-- DELETE: remove a row
DELETE FROM customers
WHERE customer_id = 1;
```

## Why It Matters

Every business operation ultimately translates to a row operation. A new user signs up → INSERT. A customer updates their address → UPDATE. An order is fulfilled → UPDATE. An account is closed → DELETE (or status UPDATE). The rows in a database are the living record of every significant event in the system's history.

Row integrity — ensuring rows are complete, valid, and uniquely identifiable — is what separates a reliable database from an unreliable one. Schema constraints (NOT NULL, UNIQUE, CHECK, FOREIGN KEY) enforce integrity automatically, making the database self-protecting.

## Worked Examples

**Example 1: E-commerce Order Lifecycle**

```sql
-- Customer places order: INSERT a new order row
INSERT INTO orders (order_id, customer_id, order_date, status, total_amount)
VALUES (1001, 42, '2026-06-01', 'pending', 149.99);

-- Order is confirmed: UPDATE the status
UPDATE orders
SET status = 'confirmed'
WHERE order_id = 1001;

-- Order is dispatched: UPDATE again
UPDATE orders
SET status = 'dispatched', dispatched_at = '2026-06-02'
WHERE order_id = 1001;

-- Order is cancelled (rare): DELETE or mark as cancelled
UPDATE orders
SET status = 'cancelled'
WHERE order_id = 1001;
-- (Soft delete preferred: keeps the record for audit purposes)
```

**Example 2: The Danger of UPDATE Without WHERE**

```sql
-- WRONG: This updates EVERY row in the table
UPDATE customers SET email = 'error@example.com';

-- CORRECT: Target only the intended row
UPDATE customers SET email = 'error@example.com' WHERE customer_id = 99;
```

**Example 3: Soft Delete vs Hard Delete**

Rather than deleting rows (which destroys history), many systems use a soft delete:
```sql
-- Add a deleted_at column to mark rows as deleted without removing them
UPDATE customers
SET deleted_at = CURRENT_TIMESTAMP
WHERE customer_id = 42;

-- Query only active customers
SELECT * FROM customers WHERE deleted_at IS NULL;
```

## Common Mistakes

- **UPDATE or DELETE without WHERE**: The most dangerous SQL mistakes. Always verify your WHERE clause before executing.
- **Hard deleting data that should be audited**: Orders, financial transactions, and medical records should almost never be hard-deleted. Use soft deletes (a `deleted_at` timestamp or `is_active` flag) to preserve history.
- **Inserting without specifying columns**: `INSERT INTO customers VALUES (...)` depends on column order in the schema. If the schema changes, this breaks. Always specify column names explicitly.
- **Ignoring NULL semantics**: NULL means "unknown" or "not applicable" — it is not zero, not empty string, and not false. NULL values affect filtering, sorting, and aggregation in non-obvious ways.

## Mental Model

Think of a row as a filled-in form. The schema is the blank form — every form of the same type has the same fields. Each completed form is a row — one person's data filling in those fields. The form reference number (primary key) uniquely identifies each completed form. Without that reference number, there is no reliable way to find, update, or reference a specific form among thousands.

## Mini Summary

- ✔ A row (record/tuple) represents one instance of the entity the table models
- ✔ INSERT adds rows, UPDATE modifies them, DELETE removes them, SELECT reads them
- ✔ The primary key uniquely identifies each row — no two rows share the same PK value
- ✔ UPDATE and DELETE without WHERE affect all rows — always use WHERE
- ✔ Soft deletes preserve history; hard deletes destroy it permanently

# Guided Practice Quest

Work through the guided steps to practise identifying what rows represent, writing and analysing SQL INSERT and UPDATE statements, and explaining the importance of primary key uniqueness.

# Solo Practice Quest

Design a simple `employees` table for a company with 8 columns of your choice. Then write the SQL for the following operations: (1) insert three employee rows, (2) update one employee's department, (3) "soft delete" one employee by setting a `deleted_at` timestamp, (4) select only active employees. For each statement, add a SQL comment explaining what it does and why you wrote it that way. Finally, write a paragraph explaining what would go wrong if the table had no primary key.

# Integration

**Mathematics**: In set theory, a relation (table) is a set of tuples (rows). Set theory requires that elements of a set are distinct — there are no duplicate elements. The primary key constraint in a relational database enforces this mathematical property: no two rows in a relation are identical (or more precisely, no two rows share the same identifier). This is why the relational model is built on sets, not bags — sets require uniqueness, which is why primary keys are mandatory in a properly normalised schema.

**Psychology**: The concept of **change blindness** — a cognitive phenomenon where people fail to notice changes in a visual scene — has an analogy in database work. Engineers who run UPDATE or DELETE statements without carefully verifying the WHERE clause are exhibiting a form of change blindness: they know conceptually what they intend to change, but they do not always see the full scope of what their statement will actually affect. Database reviews and transactional testing (running in a transaction with a ROLLBACK check) are the engineering antidotes to this cognitive failure mode.

# Lore Conclusion

Master Selvaris surveyed the ledger page now filled with merchant entries. "Every row is a commitment," she said. "A commitment to accuracy, to completeness, and to permanence. The moment you inscribe a record, you take responsibility for it." She pointed to the licence number column — the primary key. "And every row must be identifiable. Without that number, two merchants with the same name become indistinguishable, and the Archive becomes unreliable." She closed the ledger. "Guard the rows. They are the truth of the Archive."

---
