---
id: de-app-m2-11
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m2
moduleTitle: "Module 2: Relational Database Foundations"
moduleGlyph: "🗄️"
moduleSortOrder: 2
topicSlug: relationships
topicTitle: "Relationships"
topicSortOrder: 3
lesson: one_to_many_relationships
title: "One-to-Many Relationships"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m2-10]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Correctly defines a one-to-many relationship with an example
    - Explains how a one-to-many relationship is implemented using a foreign key
    - Describes the parent and child sides of the relationship correctly
    - Explains what happens when you query the parent table and want to include child data
    - Reflects on why one-to-many is the most common relationship type in relational databases
  keywords: [one-to-many, parent, child, foreign key, relationship, join, orders, customers, posts, comments]
  modelAnswer: |
    A one-to-many relationship exists when one row in the parent table can be associated with many rows in the child table, but each child row belongs to exactly one parent. It is implemented by placing a foreign key in the child table referencing the parent's primary key — no UNIQUE constraint, so multiple child rows can reference the same parent. The customer-orders relationship is the classic example: one customer can have many orders, but each order belongs to one customer. One-to-many is the most common relationship type because real-world domains are full of parent-child hierarchies: departments and employees, posts and comments, products and reviews.
guidedSteps:
  - id: de-app-m2-11-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In a blog system, one post can have many comments, but each comment belongs to exactly one post. Where does the foreign key go?
    inputConfig:
      options:
        - "In the posts table, referencing the comments table"
        - "In the comments table, referencing the posts table"
        - "In a separate junction table linking posts and comments"
        - "In both tables — each referencing the other"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["In the comments table, referencing the posts table"]
      rejectedFeedback: "The foreign key goes in the child table (the 'many' side) — which is comments. Each comment has a post_id foreign key pointing to the post it belongs to. A post does not store a list of comment IDs — that would be storing multiple values in one column, violating atomicity. The posts table is the parent; comments is the child."
    hint: "The foreign key always goes in the table on the 'many' side of the relationship."
    reflectionPrompt: "Why would putting the foreign key in the posts table (pointing to comments) cause a problem?"
  - id: de-app-m2-11-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In a one-to-many relationship, the table on the "one" side (e.g. customers) is called the ________ table. The table on the "many" side (e.g. orders) is called the child table.
    inputConfig:
      placeholder: "parent"
    markingRule:
      matchMode: CONTAINS
      accepted: [parent, referenced]
      rejectedFeedback: "The parent table holds the primary key that the foreign key references. In a customer-orders relationship, customers is the parent — it contains customer_id as its primary key. Orders is the child — it contains customer_id as a foreign key. One parent row can have many child rows."
    hint: "The 'one' side of one-to-many — it is referenced by the other table."
    reflectionPrompt: "Why is it called 'parent'? What does that tell you about the dependency direction?"
  - id: de-app-m2-11-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why one-to-many is the most common relationship type in relational databases.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [hierarchical, parent, child, belongs, many, common, real, world, entities, relationships]
      rejectedFeedback: "One-to-many is common because most real-world domains naturally form hierarchies: one department contains many employees, one order contains many line items, one author writes many books, one product receives many reviews. The 'belongs to' structure — where many instances of one thing all reference a single instance of another — is the dominant pattern in business data. Many-to-many relationships can always be decomposed into two one-to-many relationships via a junction table."
    hint: "Think about how many real-world relationships you can describe as 'one X has many Ys'."
    reflectionPrompt: "Can you think of five one-to-many relationships from everyday life that you would find in a typical business application?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A customer has placed 50 orders. How many times does this customer's customer_id appear in the orders table?"
    options: ["Once", "Twice", "50 times", "Zero — orders only store order data"]
    correctIndex: 2
    feedback: "Each of the 50 order rows in the orders table stores the customer_id of the customer who placed it. The same customer_id value (e.g. 42) appears in all 50 rows. This is how one-to-many is stored: the parent's identifier is repeated in each child row — there is no 'list' stored anywhere."
  - type: MULTIPLE_CHOICE
    question: "Which statement is true about the foreign key in a one-to-many relationship?"
    options:
      - "It must have a UNIQUE constraint to ensure one-to-many"
      - "It must not have a UNIQUE constraint — that would make it one-to-one"
      - "It must be the same column as the primary key"
      - "It must be NULL to allow the parent to have zero children"
    correctIndex: 1
    feedback: "In a one-to-many relationship, the foreign key in the child table must NOT have a UNIQUE constraint. Multiple child rows need to be able to reference the same parent row. If you added UNIQUE to the FK, only one child per parent would be allowed — that would make it one-to-one."
retrieval:
  recall: "Describe how a one-to-many relationship is implemented in SQL."
  explain: "Explain why the foreign key goes in the child table (the 'many' side), not in the parent table."
  mistakeId:
    code: "storing a list of child IDs in the parent table column"
    answer: "Storing a comma-separated list of order IDs in a customer column violates atomicity — a column should hold one value, not a list. It also makes queries, updates, and joins extremely difficult. The correct approach is to store the foreign key in the child table (orders.customer_id), allowing any number of orders to reference the same customer without modifying the parent row."
---

# Hook

The one-to-many relationship is the workhorse of database design. It shows up everywhere: one customer, many orders. One author, many books. One department, many employees. One blog post, many comments. One product, many reviews.

If you understand one relationship type in depth, make it this one. It is the most common, the most useful, and the foundation on which everything else is built.

# Lore Introduction

Master Selvaris opened a merchant register to a page for the Aldric Trading Company. The company had placed forty-seven transactions in the last year. "Each transaction belongs to one merchant," she said. "But one merchant may have any number of transactions." She pointed to the transaction ledger — forty-seven rows, all with the same merchant reference number. "The merchant's identity is recorded once — in the merchant register. The transaction ledger copies that reference number into every transaction that belongs to this merchant." She traced a line between the ledger and the register. "One merchant. Many transactions. The reference number in each transaction is the link. This is a one-to-many relationship — the most natural structure in any archive."

# Core Learning

## Concept Introduction

### Definition

A **one-to-many relationship** exists when:
- One row in the parent table can correspond to **many** rows in the child table
- Each row in the child table corresponds to **exactly one** row in the parent table

### Implementation

Place a **foreign key** in the child table (the "many" side). No UNIQUE constraint — multiple child rows must be able to reference the same parent row.

```sql
CREATE TABLE customers (
    customer_id  INT PRIMARY KEY,
    name         VARCHAR(200) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE orders (
    order_id    INT PRIMARY KEY,
    customer_id INT NOT NULL,           -- FK, NO UNIQUE — one customer, many orders
    order_date  DATE NOT NULL,
    status      VARCHAR(20) NOT NULL DEFAULT 'pending',
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
        ON DELETE RESTRICT
);
```

Stored data:
```
customers:
 customer_id | name          | email
 1           | Alice Green   | alice@example.com
 2           | Bob Patel     | bob@example.com

orders:
 order_id | customer_id | order_date | status
 101      | 1           | 2026-01-10 | shipped
 102      | 1           | 2026-02-14 | delivered   -- Alice has two orders
 103      | 2           | 2026-03-01 | pending
 104      | 1           | 2026-04-22 | shipped     -- Alice has three orders
```

Alice (customer_id = 1) appears three times in the orders table. Her customer data is stored once; only the reference (customer_id) is repeated.

### More Examples

```sql
-- Department → Employees
CREATE TABLE departments (
    dept_id  INT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL
);

CREATE TABLE employees (
    emp_id   INT PRIMARY KEY,
    name     VARCHAR(200) NOT NULL,
    dept_id  INT NOT NULL,
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
);

-- Post → Comments
CREATE TABLE posts (
    post_id     INT PRIMARY KEY,
    title       VARCHAR(300) NOT NULL,
    published_at TIMESTAMP
);

CREATE TABLE comments (
    comment_id  INT PRIMARY KEY,
    post_id     INT NOT NULL,          -- the FK is in comments, not posts
    content     TEXT NOT NULL,
    posted_at   TIMESTAMP NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE
);
```

### Where the FK Always Goes

**Rule**: The foreign key goes in the child table — the table on the "many" side.

Why? Because the parent needs to be referenced by potentially unlimited child rows. Storing a reference in the child is simple (one column, one value per row). Storing a list of child references in the parent violates atomicity.

```
❌ Wrong thinking: "customers stores a list of order_ids"
✅ Correct: "each order stores the customer_id of its owner"
```

### Querying One-to-Many

To retrieve a parent row with all its children, use a JOIN:

```sql
-- All orders for customer 1
SELECT o.order_id, o.order_date, o.status
FROM orders o
WHERE o.customer_id = 1;

-- All customers with their order counts
SELECT c.name, COUNT(o.order_id) AS order_count
FROM customers c
LEFT JOIN orders o ON o.customer_id = c.customer_id
GROUP BY c.customer_id, c.name;
```

## Why It Matters

One-to-many is the dominant pattern because it reflects the natural structure of business hierarchies. Almost every entity in a real system "belongs to" some other entity: orders belong to customers, line items belong to orders, employees belong to departments, comments belong to posts. The relational model expresses these hierarchies cleanly, without duplication, using a single foreign key column.

## Common Mistakes

- **Putting the FK in the wrong table**: The FK goes in the child (many) side, always. Not in the parent.
- **Storing a comma-separated list of child IDs in the parent**: Violates atomicity, makes queries impossible.
- **No index on the FK column**: Without an index on the FK, queries like "give me all orders for this customer" do a full table scan. Always index FK columns.

## Mental Model

Think of a library checkout system. A book is checked out by one member at a time, but one member can have many books checked out. The checkout record (child) stores the member's library card number (FK). The member record (parent) does not store a list of books — that would be impractical for members with many checkouts. Each checkout record simply says "this book is held by card number 4471." Many checkouts can share the same card number. One member, many checkouts. The FK is always in the checkout record — never in the member record.

## Mini Summary

- ✔ One-to-many: one parent row corresponds to many child rows
- ✔ Implemented by: FK in the child table, no UNIQUE constraint
- ✔ The FK value is the parent's primary key, repeated in each child row
- ✔ The parent table holds the data once; the child table references it by ID
- ✔ Always index FK columns for query performance

# Guided Practice Quest

Work through the guided steps to correctly identify the parent and child tables, place foreign keys on the correct side, and retrieve parent-with-children data using a simple WHERE clause.

# Solo Practice Quest

Design the schema for a simple content management system with these entities: `authors`, `articles`, `tags`, `comments`, and `comment_replies`. For each one-to-many relationship: (1) identify the parent and child tables, (2) write the SQL CREATE TABLE statements with correct FK placement, appropriate ON DELETE behaviour, and at least two additional constraints per table, and (3) write one SQL query that uses the relationship to answer a useful business question (e.g. "find all articles by a specific author with their comment counts"). Present the full schema and all queries.

# Integration

**Mathematics**: A one-to-many relationship is the database implementation of a function from the child set to the parent set — specifically, a total surjective function if every parent has at least one child (though not always required). The FK column in the child table defines the function: for every child row c, f(c) = the parent row referenced by c.FK. The fact that multiple child rows can map to the same parent is the mathematical definition of a non-injective function (many-to-one in the function direction, one-to-many in the human-readable direction). Constraints on this function — mandatory vs optional, minimum child count — translate to NOT NULL on the FK and CHECK constraints.

**Sciences (Ecology — Food Webs)**: One-to-many hierarchies are pervasive in ecological systems. A habitat (parent) hosts many species (children). A predator species (parent) has many individual predators. An individual predator (parent) has many prey interactions (children). Ecological databases model these hierarchies using exactly the same one-to-many pattern as business databases: species tables referencing habitat tables, individual organism records referencing species records. The foreign key structure captures the belonging relationship that defines the ecological hierarchy.

# Lore Conclusion

Master Selvaris pointed to the forty-seven transactions for the Aldric Trading Company. "Not one piece of the merchant's information is repeated in those rows," she said. "Name, address, registration date — all stored once, in one row of the merchant register. Each transaction simply says: 'I belong to merchant A-1044.'" She turned to her apprentice. "When the merchant moves, we update one row. When we want all their transactions, we follow the reference. When we want their transaction count, we count the references." She tapped the ledger. "This is the efficiency of one-to-many. Store facts once. Reference them many times. The relationship does the rest."

---
