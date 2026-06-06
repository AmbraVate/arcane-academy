---
id: de-app-m5-06
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m5
moduleTitle: "Module 5: Data Quality"
moduleGlyph: "✅"
moduleSortOrder: 5
topicSlug: data_integrity
topicTitle: "Data Integrity"
topicSortOrder: 2
lesson: referential_integrity
title: "Referential Integrity"
sortOrder: 2
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m5-05]
integrationDomains: [mathematics, software_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines referential integrity and explains what it protects against
    - Explains what a foreign key relationship is with an example
    - Describes ON DELETE and ON UPDATE actions and when to use each
    - Explains what an orphaned record is and why it is harmful
    - Reflects on when cascading deletes might be dangerous
  keywords: [referential integrity, foreign key, orphaned, CASCADE, RESTRICT, parent, child, relationship]
  modelAnswer: |
    Referential integrity ensures that every foreign key value in a child table corresponds to an existing primary key in the parent table. Without it, orphaned records accumulate — rows in the child table that reference non-existent parents, making joins return incomplete results. The ON DELETE and ON UPDATE clauses specify how child rows behave when the parent is deleted or updated: CASCADE propagates the change, RESTRICT prevents the change if children exist, SET NULL nullifies the foreign key, and SET DEFAULT sets a default value. CASCADE deletes are powerful but dangerous — deleting a parent record can silently remove hundreds of related child records.
guidedSteps:
  - id: de-app-m5-06-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You have an orders table with `customer_id INT REFERENCES customers(id) ON DELETE RESTRICT`. You try to delete a customer who has existing orders. What happens?
    inputConfig:
      options:
        - "The customer is deleted and all their orders are deleted too"
        - "The customer is deleted and their orders have customer_id set to NULL"
        - "The delete is rejected because child rows (orders) still reference the customer"
        - "The customer is deleted and their orders are flagged as orphaned"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The delete is rejected because child rows (orders) still reference the customer"]
      rejectedFeedback: "ON DELETE RESTRICT prevents deletion of a parent row when child rows still reference it. This protects orphaned records from being created."
    hint: "RESTRICT means 'deny this operation if it would break referential integrity'."
    reflectionPrompt: "When would ON DELETE CASCADE be appropriate, and when might it be dangerous?"
  - id: de-app-m5-06-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "A row in a child table that references a primary key that no longer exists in the parent table is called an ________ record."
    inputConfig:
      placeholder: "orphaned"
    markingRule:
      matchMode: CONTAINS
      accepted: [orphaned, orphan, dangling]
      rejectedFeedback: "An orphaned (or dangling) record is a child row whose parent no longer exists — it is a symptom of referential integrity being violated."
    hint: "Think of a child that has lost its parent — what would you call such a record?"
    reflectionPrompt: "What kind of query result would an orphaned record produce in a JOIN?"
  - id: de-app-m5-06-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain what CASCADE means in the context of a foreign key ON DELETE action, and give one scenario where it would be appropriate.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [CASCADE, delete, child, parent, propagate, appropriate]
      rejectedFeedback: "ON DELETE CASCADE automatically deletes all child rows when the parent is deleted. It is appropriate when children have no independent meaning — e.g., order line items have no value without the parent order."
    hint: "Think about what should happen to order items if the parent order is deleted."
    reflectionPrompt: "What might go wrong if you use CASCADE on a table that has many levels of child relationships?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the purpose of a FOREIGN KEY constraint?"
    options:
      - "To ensure each row has a unique identifier"
      - "To prevent NULL values in a column"
      - "To ensure a value in one table corresponds to an existing value in another table"
      - "To speed up JOIN queries"
    correctIndex: 2
    feedback: "A FOREIGN KEY constraint enforces referential integrity — the value must exist as a primary key in the referenced parent table."
  - type: MULTIPLE_CHOICE
    question: "Which ON DELETE action sets the foreign key column to NULL when the parent row is deleted?"
    options: ["CASCADE", "RESTRICT", "SET NULL", "NO ACTION"]
    correctIndex: 2
    feedback: "ON DELETE SET NULL nullifies the foreign key in all child rows when the parent is deleted. This preserves the child row but breaks the link to the parent."
retrieval:
  recall: "What is referential integrity and what database feature enforces it?"
  explain: "Explain the difference between ON DELETE CASCADE and ON DELETE RESTRICT, and give a use case for each."
  mistakeId:
    code: "I can delete parent rows freely; orphaned records don't matter"
    answer: "Orphaned records cause JOIN queries to silently return incomplete results — child rows with no matching parent are excluded from INNER JOINs. They can also corrupt statistics, cause application errors when code expects a parent to exist, and make data cleanup increasingly complex over time."
---

# Hook

A support agent deletes a customer account after they request data erasure. The customer's data is gone — but their 12 purchase orders remain in the orders table, each pointing to a customer ID that no longer exists. When the finance team runs their monthly revenue report, the JOIN between orders and customers silently drops those 12 orders. Revenue appears to be £4,300 lower than it really was. Nobody notices. The numbers are wrong. The database "works."

Referential integrity is the guarantee that relationships between tables remain valid. When it is violated, your carefully normalised schema becomes a maze of broken links. Understanding how foreign keys work — and how to configure their behaviour on delete and update — is essential for building databases that stay coherent over time.

# Lore Introduction

Master Selvaris opened the cross-reference index — a thick volume that mapped every scroll in the Archive to the province it came from. "This index only has value," he said, "if every reference in it points to a scroll that actually exists." He turned to a torn page. "Here, three references to scrolls from the Eastern Province — but the Eastern Province vault was flooded a decade ago. Those scrolls are gone, but the references remain. We call these orphaned references." He closed the index carefully. "Every orphaned reference is a lie the Archive tells itself. We do not tolerate them."

# Core Learning

## Concept Introduction

| Concept | Definition | Example |
|---------|-----------|---------|
| **Referential integrity** | Guarantee that every FK value corresponds to an existing PK | `order.customer_id` must exist in `customers.id` |
| **Parent table** | The table containing the primary key being referenced | `customers` table |
| **Child table** | The table containing the foreign key | `orders` table |
| **Orphaned record** | A child row whose parent no longer exists | An order pointing to a deleted customer |
| **ON DELETE CASCADE** | When parent is deleted, all child rows are deleted too | Deleting an order deletes all its order_items |
| **ON DELETE RESTRICT** | Prevent deletion of parent if children still exist | Cannot delete a customer who has orders |
| **ON DELETE SET NULL** | When parent is deleted, child FK is set to NULL | Deleted author's books have author_id = NULL |

## Why It Matters

Without referential integrity:
- **JOIN queries silently drop orphaned rows**: INNER JOINs exclude child rows with no matching parent, producing incomplete result sets without any error
- **Counts and aggregates are wrong**: Revenue, order counts, and other metrics are understated
- **Application errors occur**: Code that expects a parent to always exist raises NullPointerExceptions or crashes

## Worked Examples

**Example 1: Declaring a FOREIGN KEY with ON DELETE action**
```sql
CREATE TABLE orders (
    id          SERIAL PRIMARY KEY,
    customer_id INT NOT NULL REFERENCES customers(id) ON DELETE RESTRICT,
    total       DECIMAL(10,2) NOT NULL
);
-- Prevents customer deletion if they have orders
```

**Example 2: CASCADE for dependent child records**
```sql
CREATE TABLE order_items (
    id       SERIAL PRIMARY KEY,
    order_id INT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id INT NOT NULL,
    quantity INT NOT NULL
);
-- When an order is deleted, its line items are automatically deleted too
```

**Example 3: Detecting existing orphaned records**
```sql
SELECT o.*
FROM orders o
LEFT JOIN customers c ON o.customer_id = c.id
WHERE c.id IS NULL;
-- Finds orders that have no corresponding customer
```

## Common Mistakes

- **Confusing CASCADE with RESTRICT**: CASCADE is permissive (allows deletes, propagates them); RESTRICT is protective (prevents deletes that would create orphans).
- **Using CASCADE carelessly**: A single customer delete cascading through orders, order_items, invoices, and returns can wipe out hundreds of rows silently. Always test CASCADE behaviour in a staging environment first.
- **Not declaring foreign keys in "soft delete" systems**: Systems that mark rows as deleted (e.g., `is_deleted = true`) instead of physically deleting them may skip FK constraints, allowing logical orphans to accumulate.

## Mental Model

Think of referential integrity like a library book loan system. A loan record references both a member and a book. If the book is removed from the catalogue, the loan record becomes an orphan — it points to a book that doesn't exist. The system can either prevent the book from being removed while loans exist (RESTRICT), remove the loan when the book is removed (CASCADE), or mark the loan as having a null book (SET NULL). Each choice has consequences. The key is to make the choice deliberately.

## Mini Summary

- ✔ Referential integrity ensures every foreign key value references an existing parent
- ✔ Orphaned records occur when parents are deleted without handling child rows
- ✔ ON DELETE RESTRICT prevents parent deletion if children exist
- ✔ ON DELETE CASCADE deletes children when the parent is deleted — use with care
- ✔ ON DELETE SET NULL nullifies the FK, preserving the child row but breaking the link

# Guided Practice Quest

Work through the guided steps to apply the correct ON DELETE action to each scenario and explain the consequences of orphaned records in JOIN queries.

# Solo Practice Quest

Design a `blog` database with four tables: `authors`, `posts`, `comments`, and `tags`. Define foreign key relationships between them and specify appropriate ON DELETE actions for each. Write the CREATE TABLE statements. Then answer: (1) What should happen to posts when an author is deleted? (2) What should happen to comments when a post is deleted? (3) What should happen to post_tag links when a tag is deleted? Justify each decision. Then write a SQL query that finds all orphaned comments (comments whose parent post no longer exists).

# Integration

**Mathematics**: A relational database models real-world relationships as mathematical relations. Referential integrity is the database enforcement of a foreign key constraint, which is the physical representation of a functional dependency — a mathematical relationship where one attribute's value determines another's. Violating referential integrity is equivalent to allowing undefined values in a function's codomain.

**Software Engineering**: In object-oriented design, a parent object owns its children — deleting a parent destroys its children (composition). In association relationships, the parent and child can exist independently. These OO patterns map directly to ON DELETE CASCADE (composition) and ON DELETE RESTRICT or SET NULL (association). Understanding which relationship type you are modelling guides the correct FK action.

# Lore Conclusion

Master Selvaris closed the cross-reference index. "Every reference in the Archive is a promise," he said. "A promise that the scroll it points to exists, is real, and can be found." He walked to the shelves and straightened a row of neatly numbered spines. "When you define a foreign key, you make a promise to everyone who queries your data: follow this link and you will find something real." He paused. "Design your delete actions as carefully as you design your tables. A careless delete can break a hundred promises at once."

---
