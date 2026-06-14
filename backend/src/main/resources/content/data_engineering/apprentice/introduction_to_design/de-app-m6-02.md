---
id: de-app-m6-02
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m6
moduleTitle: "Module 6: Database Design Foundations"
moduleGlyph: "📐"
moduleSortOrder: 6
topicSlug: introduction_to_design
topicTitle: "Introduction to Design"
topicSortOrder: 1
lesson: avoiding_redundancy
title: "Avoiding Redundancy"
sortOrder: 2
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m6-01]
integrationDomains: [mathematics, software_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines data redundancy and explains why it is a design problem
    - Identifies at least two anomalies caused by redundancy (insert, update, delete)
    - Explains how splitting a table reduces redundancy
    - Describes the concept of a foreign key as an alternative to copying data
    - Reflects on when some redundancy might be acceptable (e.g., for performance)
  keywords: [redundancy, duplication, anomaly, update anomaly, insert anomaly, delete anomaly, foreign key, normalisation]
  modelAnswer: |
    Data redundancy means storing the same fact in more than one place in a database. This leads to three categories of anomaly: update anomalies (changing one copy of a fact without changing others creates inconsistency), insert anomalies (inability to add a fact without also adding unrelated data), and delete anomalies (deleting one record unintentionally destroys unrelated facts). The solution is to store each fact in exactly one place and reference it with foreign keys. Some redundancy is intentionally introduced for performance (denormalisation), but this must be managed carefully to avoid anomalies.
guidedSteps:
  - id: de-app-m6-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A table stores `customer_name` and `customer_email` alongside every order row. The customer changes their email. What problem does this cause?
    inputConfig:
      options:
        - "The table runs out of storage space"
        - "Every order row for that customer must be updated separately — and some may be missed"
        - "The customer's new email is automatically rejected as a duplicate"
        - "The ORDER BY clause will no longer work correctly"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Every order row for that customer must be updated separately — and some may be missed"]
      rejectedFeedback: "This is an update anomaly — when the same fact is stored in multiple rows, updating one without updating all creates inconsistency. The solution is to store the email in the customers table only and reference it from orders via a foreign key."
    hint: "If a customer has 200 orders and you store their email in every row, what happens when they change their email?"
    reflectionPrompt: "How would you redesign this table to prevent this update anomaly?"
  - id: de-app-m6-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "The problem where deleting one record accidentally destroys other facts (e.g., deleting the last order of a supplier removes the only record of that supplier's contact details) is called a ________ anomaly."
    inputConfig:
      placeholder: "delete"
    markingRule:
      matchMode: CONTAINS
      accepted: [delete, deletion]
      rejectedFeedback: "A delete anomaly occurs when removing one row unintentionally destroys other data that should be preserved. This happens when unrelated facts are stored together in a single table."
    hint: "What type of database operation causes this unintended data loss?"
    reflectionPrompt: "How would you redesign the table to separate supplier contact details from order data?"
  - id: de-app-m6-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain how using a foreign key relationship instead of copying data prevents update anomalies.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [foreign key, reference, once, single, update, copy]
      rejectedFeedback: "By storing a fact (like email) in exactly one place (customers table) and referencing it from other tables (orders) via a foreign key, a change to the fact only needs to happen once. All queries that JOIN on the foreign key automatically see the updated value."
    hint: "If the email only exists in one place, how many places need to be updated when it changes?"
    reflectionPrompt: "Are there situations where copying data (redundancy) is intentional and acceptable?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which type of anomaly occurs when you cannot add a new supplier to a database without also creating a dummy order for them?"
    options: ["Delete anomaly", "Update anomaly", "Insert anomaly", "Constraint anomaly"]
    correctIndex: 2
    feedback: "An insert anomaly occurs when the table structure forces you to add unrelated data (a fake order) just to record a new fact (a new supplier). Separating suppliers into their own table solves this."
  - type: MULTIPLE_CHOICE
    question: "Which approach best eliminates data redundancy?"
    options:
      - "Copying data into every table that needs it"
      - "Storing each fact once and referencing it with foreign keys"
      - "Adding UNIQUE constraints to all columns"
      - "Using text columns instead of structured fields"
    correctIndex: 1
    feedback: "Storing each fact once and using foreign keys to reference it ensures that updates propagate automatically and no anomalies can occur."
retrieval:
  recall: "Name the three anomalies caused by data redundancy and define each in one sentence."
  explain: "How does a foreign key relationship eliminate the need to copy data between tables?"
  mistakeId:
    code: "redundancy is fine because storage is cheap"
    answer: "Storage cost is not the problem — consistency is. Redundant data requires every fact to be updated in every place it exists. Miss one location and the database becomes inconsistent. The cost of managing inconsistency — in engineer time, user confusion, and potential regulatory liability — vastly exceeds the cost of any amount of storage."
---

# Hook

A small business stores all its orders in one spreadsheet: customer name, customer email, customer phone, order date, product name, product price, and quantity — all in one row. When a customer changes their email address, someone must manually update every row that contains their old email. When a product is discontinued, deleting its rows destroys the only record of that product's price history. When a new supplier is added who hasn't placed orders yet, there is nowhere to put them.

These are not edge cases. They are systematic consequences of a fundamental design flaw: storing multiple facts that belong to separate entities in a single table. Redundancy does not just waste space — it creates update anomalies, insert anomalies, and delete anomalies. Understanding redundancy and how to eliminate it is the foundation of relational database design.

# Lore Introduction

Master Selvaris opened a poorly designed ledger — one of the Archive's earliest records from before proper design principles were established. "Every merchant transaction is recorded here: merchant name, merchant district, goods, quantity, price, date." He pointed to row after row of the same merchant name. "Here, the merchant Aldric Voss appears 87 times. When Voss moved from the Eastern District to the Northern District, our predecessors had to update 87 rows. They missed six." He shook his head. "Six rows in the Archive still say Voss is in the Eastern District. Four hundred years later, those six rows are still wrong. That is the cost of redundancy."

# Core Learning

## Concept Introduction

| Anomaly | Cause | Example |
|---------|-------|---------|
| **Update anomaly** | Same fact stored in multiple rows; updating one misses others | Changing a customer's email requires updating every order row |
| **Insert anomaly** | Cannot add a new entity without adding unrelated data | Cannot add a new supplier without creating a fake order |
| **Delete anomaly** | Deleting one entity accidentally destroys another's data | Deleting the last order from a supplier removes all supplier info |
| **Redundancy** | The same fact exists in more than one place | `customer_email` stored in both `customers` and `orders` |
| **Decomposition** | Splitting one table into two or more to remove redundancy | Separate `customers` and `orders` tables |

## Why It Matters

Redundancy violates the relational principle that each fact should appear exactly once. Every copy of a fact is an opportunity for divergence. As systems scale, the cost of managing redundant copies grows linearly — more rows, more updates required, more opportunities for anomalies.

## Worked Examples

**Example 1: Redundant design**
```
| orders                                                                    |
| cust_name | cust_email         | product  | price | qty | date       |
|-----------|-------------------|----------|-------|-----|------------|
| Alice     | alice@example.com | Widget A | 9.99  |  2  | 2026-01-01 |
| Alice     | alice@example.com | Widget B | 4.99  |  1  | 2026-01-15 |
-- Alice's email stored twice — update anomaly risk
```

**Example 2: Decomposed design**
```sql
customers: id, name, email
products: id, name, price
orders: id, customer_id (FK), order_date
order_items: order_id (FK), product_id (FK), quantity
```
Alice's email exists once. Any query JOINing orders to customers always sees the current email.

**Example 3: The insert anomaly**
```
-- Redundant design: cannot record a new product until someone orders it
-- Decomposed design: products table exists independently of orders
INSERT INTO products (name, price) VALUES ('Widget C', 7.50);
-- Can now be added before any orders exist
```

## Common Mistakes

- **Conflating redundancy with denormalisation**: Intentional denormalisation (adding calculated or copied columns for performance) is a deliberate trade-off. Accidental redundancy is a structural flaw.
- **Normalising too far**: A table with one column and millions of foreign keys is over-normalised — too many joins reduce readability. Normalise until anomalies are eliminated, then stop.
- **Assuming "it works" means it is well-designed**: A redundant schema functions correctly as long as all copies are kept in sync. It fails the moment any update is missed.

## Mental Model

Think of redundancy like photocopying an important document and filing the copies in twelve different cabinets. When the document changes (a phone number, an address), every copy must be updated. If you miss one cabinet, that cabinet now holds an outdated document. The better approach: keep one original, filed in one place, and reference its location from everywhere else.

## Mini Summary

- ✔ Redundancy means storing the same fact in more than one place
- ✔ It causes update, insert, and delete anomalies
- ✔ The solution is decomposition: split tables so each fact lives exactly once
- ✔ Foreign keys allow tables to reference facts without copying them
- ✔ Some intentional redundancy (denormalisation) is acceptable for performance, but must be managed carefully

# Guided Practice Quest

Work through the guided steps to identify which anomaly applies to each scenario and explain the decomposition that would eliminate it.

# Solo Practice Quest

You are given a single table containing: `teacher_name`, `teacher_email`, `teacher_phone`, `course_name`, `course_code`, `student_name`, `student_email`, `enrolment_date`, `grade`. Identify all the redundancy in this table and describe every update, insert, and delete anomaly it can produce. Then decompose it into a normalised set of tables with appropriate foreign key relationships. Write the CREATE TABLE statements for your redesign and explain each decision.

# Integration

**Mathematics**: In set theory, the principle that each element of a set appears exactly once is fundamental. A multiset allows duplicates; a set does not. A normalised database table should be a set of facts — each fact appearing exactly once. Redundancy transforms a set into a multiset, introducing all the consistency problems that mathematical sets are designed to avoid.

**Software Engineering**: The DRY (Don't Repeat Yourself) principle in software engineering states that every piece of knowledge should have a single, authoritative representation in a system. Database normalisation is the application of DRY to data: every fact has exactly one authoritative location, and all other references point to that location rather than copying it.

# Lore Conclusion

Master Selvaris closed the old ledger. "The predecessors who designed this were not foolish," he said carefully. "They were in a hurry. They thought copying the merchant's name into every transaction row would be faster than building a separate merchant registry." He set the ledger aside permanently. "It was faster in the beginning. And then they spent four hundred years managing the consequences." He gestured to the Archive's clean, well-organised shelves. "Every fact, exactly once. Every reference, a link to that fact. This is not merely a preference. It is the foundation of everything the Archive stands for."

---
