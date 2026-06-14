---
id: de-app-m2-07
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m2
moduleTitle: "Module 2: Relational Database Foundations"
moduleGlyph: "🗄️"
moduleSortOrder: 2
topicSlug: keys
topicTitle: "Keys"
topicSortOrder: 2
lesson: foreign_keys
title: "Foreign Keys"
sortOrder: 2
difficulty: 2
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m2-06]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines a foreign key accurately and explains its relationship to a primary key
    - Explains what referential integrity means and why it matters
    - Describes what happens when you attempt to insert a foreign key value that does not exist in the parent table
    - Explains at least two ON DELETE / ON UPDATE behaviour options with examples
    - Reflects on how foreign keys enforce real-world rules at the database level
  keywords: [foreign key, referential integrity, parent table, child table, constraint, ON DELETE, CASCADE, SET NULL, relationship]
  modelAnswer: |
    A foreign key is a column in one table that references the primary key of another table, creating a link between records. It enforces referential integrity — the guarantee that a foreign key value always points to a real, existing row in the referenced table. Attempting to insert an order with a non-existent customer_id is rejected by the database. ON DELETE CASCADE means deleting the parent row also deletes all child rows; ON DELETE SET NULL means the foreign key is set to NULL instead. Foreign keys enforce real-world constraints (an order must belong to a real customer) at the database level, independent of application code.
guidedSteps:
  - id: de-app-m2-07-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      An `orders` table has a column `customer_id` defined as a foreign key referencing `customers(customer_id)`. You try to insert a new order with `customer_id = 999`, but no customer with ID 999 exists. What happens?
    inputConfig:
      options:
        - "The order is inserted with customer_id = NULL"
        - "The database inserts the order and creates a warning"
        - "The database rejects the insert with a referential integrity violation error"
        - "The database creates a new customer record with ID 999 automatically"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The database rejects the insert with a referential integrity violation error"]
      rejectedFeedback: "A foreign key constraint enforces that every value in the foreign key column must exist as a primary key value in the referenced table. Inserting customer_id = 999 when no customer 999 exists violates this constraint — the database rejects the insert entirely. This is referential integrity in action: the database prevents orphaned records."
    hint: "The foreign key constraint is a guarantee — the database will enforce it, not just record a warning."
    reflectionPrompt: "Why is it better for the database to reject invalid foreign key values rather than allowing application code to handle the validation?"
  - id: de-app-m2-07-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The table that contains the foreign key is called the ________ table. The table that contains the primary key being referenced is called the parent table.
    inputConfig:
      placeholder: "child"
    markingRule:
      matchMode: CONTAINS
      accepted: [child, referencing, dependent]
      rejectedFeedback: "The child table (also called the referencing or dependent table) contains the foreign key. The parent table (also called the referenced table) contains the primary key that the foreign key points to. In an orders/customers example: orders is the child table, customers is the parent table."
    hint: "Think of the relationship: which table 'belongs to' the other?"
    reflectionPrompt: "In a blog system with posts and comments, which is the parent table and which is the child table?"
  - id: de-app-m2-07-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain what `ON DELETE CASCADE` means on a foreign key constraint and give one example of when it is appropriate to use.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [delete, cascade, parent, child, automatically, rows, removed, deleted]
      rejectedFeedback: "ON DELETE CASCADE means that when a parent row is deleted, all child rows referencing it are automatically deleted too. For example, if a shopping cart is deleted, all cart_items referencing that cart should also be deleted — they cannot exist without a cart. It is appropriate when child rows have no meaning without their parent. It is inappropriate when child rows should be preserved (e.g. order history should not be deleted when a customer account is closed)."
    hint: "The keyword 'cascade' means the effect flows downward through the relationship."
    reflectionPrompt: "Would you use ON DELETE CASCADE for orders when a customer deletes their account? Why or why not?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You delete a customer from the `customers` table. Their five orders in the `orders` table have `ON DELETE SET NULL` on the foreign key. What happens to the orders?"
    options:
      - "The orders are deleted automatically"
      - "The delete is rejected because orders still reference this customer"
      - "The orders remain, but their customer_id is set to NULL"
      - "Nothing — SET NULL only affects future inserts"
    correctIndex: 2
    feedback: "ON DELETE SET NULL means the foreign key column in child rows is set to NULL when the parent row is deleted. The order records are preserved but are now 'orphaned' — they no longer reference a specific customer. This is appropriate for audit trails where you want to keep the record but remove the personal link."
  - type: MULTIPLE_CHOICE
    question: "Which SQL syntax correctly defines a foreign key on the `orders` table referencing `customers`?"
    options:
      - "FOREIGN KEY (customer_id) REFERENCES customers(customer_id)"
      - "LINK customer_id TO customers.customer_id"
      - "REFERENCES customer_id FROM customers"
      - "JOIN KEY (customer_id) ON customers"
    correctIndex: 0
    feedback: "FOREIGN KEY (column_name) REFERENCES parent_table(parent_column) is the standard SQL syntax for a foreign key constraint. The referenced column must be a primary key or have a UNIQUE constraint in the parent table."
retrieval:
  recall: "Define a foreign key and explain its relationship to a primary key."
  explain: "Explain what referential integrity means and give one example of how a foreign key constraint enforces it."
  mistakeId:
    code: "relying on application code to enforce referential integrity instead of using foreign keys"
    answer: "Application-level enforcement is fragile — bugs, direct database access, batch imports, and legacy scripts can all bypass application code. A foreign key constraint is enforced by the database itself, regardless of how data enters. It is the only reliable guarantee of referential integrity. Application code should validate first, but the database constraint is the final safeguard."
---

# Hook

A database of isolated tables is just a collection of spreadsheets. The power of the relational model comes from the relationships *between* tables. An order belongs to a customer. A comment belongs to a post. A product belongs to a category. These relationships must be enforced — not just assumed.

The foreign key is how relational databases enforce these connections. It is not just a column that happens to contain the same values as another table's primary key. It is a *constraint* — a rule the database actively monitors and enforces on every insert, update, and delete.

# Lore Introduction

Master Selvaris pointed to two ledgers lying open on the desk. "This ledger records transactions. This one records merchants. Each transaction names a merchant by their archive number." She placed a finger on a transaction entry. "But what if a clerk writes a merchant number that does not exist? We have a transaction for a ghost — a merchant with no record, no address, no name." She closed one ledger firmly. "Without a rule enforced by the archive itself, clerks will make mistakes. The rule is simple: a transaction may only name a merchant number that exists in the merchant register. Any entry that names a non-existent merchant is rejected before it enters the archive." She tapped the ledger. "This rule is the foreign key."

# Core Learning

## Concept Introduction

### Definition

A **foreign key** is a column in one table (the *child* table) whose values must match a primary key value in another table (the *parent* table). It creates a verified link between rows in two tables.

```sql
CREATE TABLE customers (
    customer_id  INT PRIMARY KEY,
    name         VARCHAR(200) NOT NULL,
    email        VARCHAR(255) NOT NULL
);

CREATE TABLE orders (
    order_id     INT PRIMARY KEY,
    customer_id  INT NOT NULL,
    order_date   DATE NOT NULL,
    CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);
```

The `FOREIGN KEY` constraint ensures every `customer_id` in `orders` exists as a `customer_id` in `customers`. The database enforces this on every INSERT and UPDATE.

### Referential Integrity

**Referential integrity** is the guarantee that every foreign key value points to a real, existing row in the parent table. There are no "orphaned" rows — no order that points to a non-existent customer.

```sql
-- This succeeds: customer 1 exists
INSERT INTO orders (order_id, customer_id, order_date)
VALUES (101, 1, '2026-06-01');

-- This fails: customer 999 does not exist
INSERT INTO orders (order_id, customer_id, order_date)
VALUES (102, 999, '2026-06-01');
-- ERROR: insert or update on table "orders" violates foreign key constraint
-- Key (customer_id)=(999) is not present in table "customers"
```

### ON DELETE and ON UPDATE Behaviour

When a parent row is deleted or its primary key updated, the database needs to know what to do with the child rows. The options:

| Behaviour | ON DELETE effect | When to use |
|-----------|-----------------|-------------|
| `RESTRICT` (default) | Reject the delete if child rows exist | When child rows must not be orphaned |
| `CASCADE` | Delete all child rows automatically | When children cannot exist without the parent |
| `SET NULL` | Set foreign key in child rows to NULL | When children should be preserved but de-linked |
| `SET DEFAULT` | Set foreign key to its default value | Rare; when a fallback parent exists |
| `NO ACTION` | Like RESTRICT, checked at end of statement | Used in deferred constraint checks |

```sql
-- Example: deleting a post deletes all its comments (comments have no meaning alone)
CREATE TABLE comments (
    comment_id  INT PRIMARY KEY,
    post_id     INT NOT NULL,
    content     TEXT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts(post_id)
        ON DELETE CASCADE
);

-- Example: deleting a category sets product's category_id to NULL (keep the product)
CREATE TABLE products (
    product_id  INT PRIMARY KEY,
    category_id INT,
    name        VARCHAR(200) NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
        ON DELETE SET NULL
);
```

### The Parent-Child Relationship

- **Parent table**: holds the primary key being referenced (e.g. `customers`)
- **Child table**: holds the foreign key that references the parent (e.g. `orders`)
- A parent can have many children; a child has one parent per foreign key
- A table can be both a parent (referenced by others) and a child (references others)

## Why It Matters

Without foreign keys, data integrity depends entirely on application code — which can be buggy, bypassed, or absent for direct database access. Foreign key constraints enforce integrity at the database level, unconditionally:

- Direct SQL queries from the database CLI respect the constraints
- Bulk imports via CSV tools respect the constraints
- Every ORM, API, and script that touches the database respects the constraints

This is the principle of defence in depth: the database itself is the final guarantee.

## Common Mistakes

- **Skipping foreign keys for performance**: Foreign keys have a tiny overhead on inserts/deletes. The data integrity they provide is worth far more than the marginal cost.
- **CASCADE everywhere**: Cascading deletes can cause accidental mass deletions. Prefer RESTRICT as the default; use CASCADE only when you have thought through the consequences.
- **Forgetting to index the foreign key column**: The foreign key column in the child table is not automatically indexed (unlike the primary key). Without an index, joining and filtering by the foreign key is slow on large tables.
- **Circular references**: Table A references Table B which references Table A. This creates insertion ordering problems. Usually a sign of a design flaw.

## Mental Model

Think of a foreign key as a library borrowing system. A borrowing record must reference a real library member number. The library does not allow you to create a borrowing record for member number 99999 if that member does not exist. And if a member record is deleted, the library must decide: delete all their borrowing records (CASCADE), keep the records but mark them as anonymous (SET NULL), or refuse to delete the member while they have outstanding borrowings (RESTRICT). The foreign key is the rule; the ON DELETE behaviour is the policy for when the rule is tested.

## Mini Summary

- ✔ A foreign key links a column in one table to the primary key of another
- ✔ Referential integrity means every FK value points to an existing parent row
- ✔ The database enforces this on every insert and update — no application code needed
- ✔ ON DELETE CASCADE, SET NULL, and RESTRICT control what happens when a parent is deleted
- ✔ Index your foreign key columns for query performance

# Guided Practice Quest

Work through the guided steps to identify valid and invalid foreign key insertions, choose appropriate ON DELETE behaviours for real-world scenarios, and explain referential integrity violations.

# Solo Practice Quest

Design the foreign key relationships for a simplified e-commerce system with these tables: `customers`, `addresses`, `products`, `categories`, `orders`, `order_lines`. For each foreign key: (1) write the CONSTRAINT definition in SQL, (2) choose an ON DELETE behaviour (CASCADE, SET NULL, or RESTRICT) and justify it in one sentence, and (3) explain what real-world business rule the foreign key enforces. Then describe one scenario where your ON DELETE behaviour would be tested (e.g. "a customer deletes their account") and explain the outcome.

# Integration

**Mathematics**: Foreign keys implement the mathematical concept of a referential constraint — a constraint that the values in one relation (the child) must be a subset of the values in another relation (the parent). Formally: for every tuple t in the child relation where t[FK] is not null, there must exist a tuple s in the parent relation where t[FK] = s[PK]. This is a statement about set membership: the set of FK values in the child must be a subset of the set of PK values in the parent. Violations of this set membership relationship are what referential integrity constraints prevent.

**Sciences (Ecology)**: Ecosystems exhibit referential dependencies analogous to foreign keys. An organism cannot belong to a species that does not exist in the taxonomy; a species cannot belong to a genus that does not exist. Ecological databases use referential integrity exactly as SQL databases do — records at higher taxonomic levels must exist before records at lower levels can be inserted. The ON DELETE CASCADE analogy is also visible: if a genus is reclassified and removed from the taxonomy, all species under that genus must be re-associated or the database becomes inconsistent — precisely the problem that ON DELETE behaviour policies solve.

# Lore Conclusion

Master Selvaris watched as the database rejected an attempt to record a transaction for merchant number 7,441 — a number not found in the merchant registry. "The archive has enforced the rule," she said with quiet satisfaction. "No human review was needed. No clerk had to check the registry manually. The constraint did its work." She turned to her apprentice. "This is the nature of good database design: the rules are written into the structure itself. Once defined, they hold forever, regardless of who writes the data or how they access the archive." She made a note in the design document. "Foreign keys are how databases remember that data does not exist in isolation. Every record belongs to a context, and that context must be real."

---
