---
id: de-app-m2-05
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
lesson: table_design_basics
title: "Table Design Basics"
sortOrder: 5
difficulty: 2
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m2-04]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Applies the one-entity-per-table rule correctly to a real-world scenario
    - Explains at least three principles of good table design with examples
    - Identifies the most common table design mistakes and explains their consequences
    - Explains why column names should be clear, consistent, and unambiguous
    - Reflects on how early design decisions affect long-term maintainability
  keywords: [table design, entity, column, naming, atomic, one-entity, normalisation, redundancy, single responsibility]
  modelAnswer: |
    Good table design centres on one entity per table, atomic columns (each column stores one indivisible value), clear and consistent naming conventions, and appropriate use of constraints. Common mistakes include storing multiple values in one column (e.g. comma-separated lists), creating catch-all tables, using inconsistent naming (mixing cases and abbreviations), and duplicating data that belongs in a separate table. Early design decisions are difficult to change once a system has data, so getting the fundamentals right upfront prevents expensive migrations later.
guidedSteps:
  - id: de-app-m2-05-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A developer creates a `users` table with a column called `phone_numbers` that stores values like `"07700 900123, 07700 900456"`. What principle does this violate?
    inputConfig:
      options:
        - "Single responsibility — the table should only store one user"
        - "Atomicity — each column should store one indivisible value"
        - "Data typing — phone numbers should use a numeric type"
        - "Normalisation — the column name is not descriptive enough"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Atomicity — each column should store one indivisible value"]
      rejectedFeedback: "Storing multiple phone numbers in a single column violates atomicity — the principle that each column stores exactly one indivisible value. This makes it impossible to query, index, or validate individual phone numbers. The correct approach is a separate `user_phone_numbers` table with one row per number."
    hint: "Think about what happens when you try to query for a specific phone number in that column."
    reflectionPrompt: "How would you write a WHERE clause to find a user with a specific phone number if numbers are stored as a comma-separated string?"
  - id: de-app-m2-05-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the rule: "Each table should represent exactly one ________ type. A table that represents more than one entity mixes responsibilities and introduces redundancy."
    inputConfig:
      placeholder: "entity"
    markingRule:
      matchMode: CONTAINS
      accepted: [entity, thing, concept, subject]
      rejectedFeedback: "The one-entity-per-table rule is fundamental: a `customers` table represents customers, an `orders` table represents orders. Mixing them into a single table (e.g. repeating customer details on every order row) creates redundancy, update anomalies, and deletion anomalies."
    hint: "This is the core concept from your data modelling lessons — tables correspond to these in the real world."
    reflectionPrompt: "What problems would arise from storing customer name and address directly on every order row, rather than in a separate customers table?"
  - id: de-app-m2-05-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain what an "update anomaly" is and why it occurs when the same data is stored redundantly in multiple rows.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [update, redundant, inconsistent, multiple, rows, change, duplicate, anomaly]
      rejectedFeedback: "An update anomaly occurs when the same fact is stored in multiple rows, so updating it requires changing every row — and if even one is missed, the data becomes inconsistent. For example, if a customer's address is stored on every order row and the customer moves, you must update hundreds of rows. Miss one and the database now contains contradictory information about reality."
    hint: "Consider what happens when you need to change a value that appears in many rows."
    reflectionPrompt: "How does moving the customer's address to a separate customers table solve the update anomaly problem?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of the following is the best name for a column storing a product's selling price?"
    options: ["p", "productprice", "unit_price_gbp", "priceOfProductInGBP"]
    correctIndex: 2
    feedback: "unit_price_gbp is clear (unit price, not total), uses snake_case (a common SQL convention), and includes the currency. Short abbreviations like 'p' are cryptic; mixed-case names like 'productprice' are hard to read; overly verbose names reduce query readability."
  - type: MULTIPLE_CHOICE
    question: "A `contacts` table has a column `address` that stores the full address as one string (e.g. '12 High Street, London, SW1A 1AA'). What problem does this create?"
    options:
      - "It uses too much storage"
      - "It makes queries like 'find all contacts in London' impossible to write reliably"
      - "It violates the one-entity-per-table rule"
      - "It prevents the table from having a primary key"
    correctIndex: 1
    feedback: "Storing the full address as one string makes it impossible to reliably query by city, postcode, or street, because you cannot parse free-form text consistently. The correct approach is separate columns: street, city, postcode, country — each queryable independently."
retrieval:
  recall: "State three principles of good table design."
  explain: "Explain what an update anomaly is and which table design mistake causes it."
  mistakeId:
    code: "storing multiple values in one column"
    answer: "Storing multiple values in a single column (e.g. comma-separated tags, multiple phone numbers) violates atomicity and makes the values impossible to query, index, or validate individually. The correct design is a separate table with one value per row, linked back to the parent table with a foreign key."
---

# Hook

The first four lessons covered the vocabulary of tables: what they are, how rows and columns work, and how to choose data types. Now we turn to craft: how do you design a table well?

The difference between a well-designed table and a poorly designed one is invisible when the system is small and new. It becomes painfully visible at scale — when a query that should take milliseconds takes minutes, when a simple data change requires updating thousands of rows, or when adding a new feature requires rewriting the entire database schema.

Most table design mistakes are simple in nature. They come down to a handful of violations of basic principles. Learn to spot them, and you can avoid years of maintenance pain.

# Lore Introduction

Master Selvaris unrolled a large parchment covered in dozens of column headings — some abbreviated to single letters, others with duplicate information, several storing what appeared to be multiple values crammed into one cell. "This is the archive's first attempt at a records table," she said. "The apprentice who designed it was eager but untrained. It seemed to work at first." She pointed to a row where the same address appeared seventeen times — once per transaction for the same merchant. "Until the merchant moved. We had to correct every single entry manually. Three contained the old address for another six months before anyone noticed." She rolled the parchment back up. "Good design costs nothing extra at the start. Poor design is very expensive later."

# Core Learning

## Concept Introduction

### Principle 1: One Entity Per Table

Each table represents exactly one entity type. A `customers` table represents customers. An `orders` table represents orders. Never mix them.

**Wrong — customer data duplicated on every order:**
```sql
CREATE TABLE orders (
    order_id      INT,
    order_date    DATE,
    customer_name VARCHAR(200),   -- duplicated on every order
    customer_email VARCHAR(255),  -- duplicated on every order
    customer_address TEXT         -- duplicated on every order
);
```

**Correct — customer data lives once in its own table:**
```sql
CREATE TABLE customers (
    customer_id   INT PRIMARY KEY,
    name          VARCHAR(200),
    email         VARCHAR(255),
    address       TEXT
);

CREATE TABLE orders (
    order_id      INT PRIMARY KEY,
    order_date    DATE,
    customer_id   INT NOT NULL    -- reference to customers table
);
```

### Principle 2: Atomic Columns

Each column stores exactly one indivisible value. Never store multiple values in a single column.

```sql
-- WRONG: comma-separated phone numbers
CREATE TABLE contacts (
    contact_id   INT,
    phone_numbers VARCHAR(500)  -- e.g. "07700900001, 07700900002"
);

-- CORRECT: separate table, one row per number
CREATE TABLE contact_phone_numbers (
    id          INT PRIMARY KEY,
    contact_id  INT NOT NULL,
    phone_number VARCHAR(20) NOT NULL
);
```

### Principle 3: Clear, Consistent Naming

- Use **snake_case** for column names: `order_date`, not `OrderDate` or `orderdate`
- Names should be **self-explanatory**: `unit_price_gbp` not `p` or `price_thing`
- Be **consistent** across tables: if one table uses `created_at`, all tables should use `created_at`
- Avoid **reserved words** as column names: `date`, `name`, `order` can cause parsing errors

### Principle 4: Don't Store What You Can Calculate

Storing derived data introduces consistency risk.

```sql
-- WRONG: order_total can go out of sync with line items
CREATE TABLE orders (
    order_id    INT,
    order_total DECIMAL(10,2)  -- recalculated and stored manually
);

-- CORRECT: calculate at query time
SELECT order_id, SUM(quantity * unit_price) AS order_total
FROM order_lines
GROUP BY order_id;
```

## Why It Matters

These principles collectively prevent three classes of anomalies:

| Anomaly | Cause | Effect |
|---------|-------|--------|
| **Update anomaly** | Same fact stored in multiple rows | Updating one copy leaves others stale |
| **Insertion anomaly** | Can't add data without unrelated data | Can't add a product without creating an order |
| **Deletion anomaly** | Deleting one entity deletes another | Deleting the last order for a customer loses the customer record |

All three arise from mixing entities in the same table or duplicating data. Following the principles above eliminates them.

## Common Mistakes

- **The god table**: One huge table with fifty columns containing half-null rows for different "types" of record. Split by entity.
- **Abbreviated column names**: `fn`, `ln`, `dob` — unreadable without a legend.
- **Mixing concerns**: Storing order information and shipping information in the same table. Each is its own entity.
- **Calculated columns**: Storing a total that should be derived from related rows. Calculation belongs at query time.
- **Inconsistent conventions**: `CustomerID` in one table, `customer_id` in another, `cust_id` in a third. Errors accumulate.

## Mental Model

Think of each table as a filing cabinet drawer, labelled with exactly one subject. The drawer labelled "Customers" contains only customer cards — one card per customer. The drawer labelled "Orders" contains only order forms. If you find yourself wanting to attach customer information directly onto an order form (rather than just writing the customer number), you have identified an entity boundary that deserves its own drawer.

## Mini Summary

- ✔ One entity per table — mix entities and you create redundancy and anomalies
- ✔ Atomic columns — one value per column, always
- ✔ Clear, consistent naming using snake_case and full words
- ✔ Don't store derived data — calculate it at query time
- ✔ Poor design is cheap at the start and expensive forever after

# Guided Practice Quest

Work through the guided steps to identify violations of table design principles in example schemas and explain the consequences of each mistake.

# Solo Practice Quest

You are reviewing a `sales_records` table that a colleague has designed for a small retail business. The table has the following columns: `id`, `dt`, `custname`, `custemail`, `custaddr`, `prodname`, `prodcat`, `qty`, `unitprice`, `totalprice`, `tags` (stored as comma-separated values like "new,urgent,vip"). Write a critique of this design: identify every principle it violates, explain the specific problems each violation causes, and propose a corrected multi-table schema with proper column names, data types, and at least four constraints. Justify every change you make.

# Integration

**Mathematics**: The anomalies described in this lesson (update, insertion, deletion) have a precise mathematical treatment in relational theory. Edgar Codd formalised these issues in his work on normalisation, which uses functional dependency theory to define when a schema is free of redundancy. A functional dependency X → Y means that knowing the value of X determines the value of Y. Anomalies arise when a table contains functional dependencies that span multiple entities — precisely the one-entity-per-table violation. Normalisation theory provides a rigorous procedure for decomposing tables to eliminate these dependencies.

**Sciences (Systems Biology)**: The cell biology concept of separation of concerns maps directly onto table design. A cell membrane separates the cell's interior from its environment; specialised organelles perform distinct functions without overlap. Poor table design is the biological equivalent of combining the nucleus and the mitochondrion into one indistinct blob — the functions interfere and the system becomes fragile. Well-designed tables, like well-differentiated cells, have clear boundaries and single responsibilities. This is why complex living systems and complex databases both benefit from the same architectural principle.

# Lore Conclusion

Master Selvaris produced a fresh blank parchment. "We will redesign this archive section from first principles," she said. "One register per entity. Every column holds one thing. Names that speak for themselves." She began drawing table boundaries on the parchment. "You may wonder whether all this care is necessary for a small archive. It is. Small archives become large ones. The Archive of the Eternal Library began with twelve scrolls and now holds twelve million records." She passed the pen to her apprentice. "Every design decision you make today, you will live with for twenty years. Design accordingly."

---
