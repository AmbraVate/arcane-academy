---
id: de-app-m6-04
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
lesson: first_normal_form
title: "First Normal Form"
sortOrder: 4
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m6-03]
integrationDomains: [mathematics, software_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines First Normal Form (1NF) in own words
    - Identifies the two main violations of 1NF (repeating groups, non-atomic values)
    - Explains how to convert a table to 1NF with an example
    - Defines "atomic value" and explains why it is required
    - Reflects on why storing multiple values in one cell is tempting but harmful
  keywords: [1NF, atomic, repeating group, multi-valued, column, row, primary key, decompose]
  modelAnswer: |
    First Normal Form (1NF) requires that every column in a table contains atomic (indivisible) values, every row is unique, and there are no repeating groups of columns. An atomic value cannot be meaningfully split — for example, "Alice" is atomic but "Alice, Bob, Charlie" in one cell is not. Repeating groups occur when a table has columns like Phone1, Phone2, Phone3 instead of a separate phone_numbers table. To convert to 1NF: flatten repeating groups into separate rows and split multi-valued cells into separate tables. The result may have a composite primary key (e.g., student_id, phone_number).
guidedSteps:
  - id: de-app-m6-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A table has columns: `student_id`, `student_name`, `phone1`, `phone2`, `phone3`. Which 1NF violation does this represent?
    inputConfig:
      options:
        - "Non-atomic values — phone numbers contain spaces"
        - "Repeating groups — the same attribute (phone number) appears in multiple columns"
        - "Missing primary key — student_id is not unique"
        - "Transitive dependency — phone numbers depend on student_name"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Repeating groups — the same attribute (phone number) appears in multiple columns"]
      rejectedFeedback: "Repeating groups occur when the same type of data appears across multiple columns (phone1, phone2, phone3). The fix is to create a separate student_phones table with one row per phone number."
    hint: "What if a student has four phone numbers? The column structure cannot accommodate this without changing the schema."
    reflectionPrompt: "How would you redesign this to accommodate any number of phone numbers per student?"
  - id: de-app-m6-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "In 1NF, every value in a table must be ________ — meaning it cannot be meaningfully divided into smaller parts."
    inputConfig:
      placeholder: "atomic"
    markingRule:
      matchMode: CONTAINS
      accepted: [atomic, indivisible, single]
      rejectedFeedback: "An atomic value is one that cannot be further divided into meaningful parts in the context of the database. 'London' is atomic; 'London, Paris, Berlin' in one cell is not."
    hint: "This word, borrowed from physics, means 'cannot be split into smaller meaningful parts'."
    reflectionPrompt: "Is a full name like 'Alice Smith' atomic? Depends on whether you ever need to query first and last names separately."
  - id: de-app-m6-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A cell in an orders table contains "Widget A, Widget B, Widget C" as the list of products in an order. In 2–3 sentences, explain why this violates 1NF and how to fix it.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [atomic, separate, row, table, order_items, fix, 1NF, violates]
      rejectedFeedback: "The cell contains multiple values where one is expected — a non-atomic value. Fix: create a separate order_items table with one row per product per order, linking back to the orders table via order_id."
    hint: "Each row should represent one unit of the entity. What is the entity here — the order or the item?"
    reflectionPrompt: "What queries become possible after this fix that were impossible before?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A table with a column containing 'Mathematics, Physics, Chemistry' for a student's enrolled subjects violates 1NF because:"
    options:
      - "The column has the wrong data type"
      - "The values are not atomic — multiple subjects are stored in one cell"
      - "The column name is ambiguous"
      - "The column should be a foreign key"
    correctIndex: 1
    feedback: "Multiple values in one cell violate atomicity — a core requirement of 1NF. Each subject should be a separate row in a student_subjects table."
  - type: MULTIPLE_CHOICE
    question: "Which of the following tables is in First Normal Form?"
    options:
      - "A table with columns: id, name, phone1, phone2, phone3"
      - "A table with a column containing comma-separated tags"
      - "A table where each column contains exactly one atomic value per row"
      - "A table with duplicate rows"
    correctIndex: 2
    feedback: "1NF requires each column to contain exactly one atomic value per row, with no repeating groups and no duplicate rows."
retrieval:
  recall: "State the requirements of First Normal Form in your own words."
  explain: "Why does storing multiple values in one column (e.g., comma-separated) cause problems for queries and data integrity?"
  mistakeId:
    code: "storing a list in one column is fine — I can split it with a string function in my queries"
    answer: "String splitting in queries is fragile, slow, and impossible to index. It makes filtering, joining, and counting on individual values unreliable. It also prevents foreign key constraints, type enforcement, and any form of referential integrity. The correct approach is to store each value as its own row in a related table."
---

# Hook

A developer needs to store the phone numbers for each customer. They add three columns: `phone1`, `phone2`, `phone3`. Six months later, a customer has four phone numbers. Another developer adds `phone4`. A year later, the table has seven phone columns, most of them empty for most rows. Querying "all phone numbers for customer 42" requires `COALESCE(phone1, phone2, phone3, phone4, phone5, phone6, phone7)` — a maintenance nightmare.

Or: a developer stores all of a customer's phone numbers in one column, separated by commas. Querying whether any customer has the number "07700900123" requires parsing every row. There is no way to index it. No way to enforce a phone number format. No way to know how many phone numbers exist.

Both approaches violate the First Normal Form — and both are common mistakes. 1NF is the foundation of every well-designed relational schema.

# Lore Introduction

Master Selvaris opened a registration scroll where each entry contained a single field for "all family members" — names crammed together, sometimes with commas, sometimes with "and," sometimes just as a long string. "The scribe thought they were being efficient," he said, quietly amused. "One row per family. One field for all names." He pointed to a query annotation in the margin: "Find all families containing a member named 'Edith.'" The annotation was followed by three pages of elaborate text-matching rules. "This is what happens when we do not separate things that should be separate." He closed the scroll. "First Normal Form: one thing, one place."

# Core Learning

## Concept Introduction

| 1NF Requirement | Meaning | Violation Example |
|-----------------|---------|------------------|
| **Atomic values** | Each column cell contains exactly one indivisible value | `skills = "SQL, Python, Java"` |
| **No repeating groups** | Same attribute not repeated across multiple columns | `phone1, phone2, phone3` columns |
| **Unique rows** | Each row must be distinguishable (usually via a primary key) | Exact duplicate rows |
| **Single data type per column** | Every value in a column has the same type | Mixing dates and strings in one column |

## Why It Matters

1NF is the prerequisite for everything else in relational design:
- Without atomic values, you cannot filter on individual elements
- Without unique rows, you cannot reference a specific row reliably
- Without 1NF, no higher Normal Form can be meaningfully applied

## Worked Examples

**Example 1: Fixing a non-atomic column**
```
-- Violates 1NF:
| student_id | subjects           |
|------------|-------------------|
| 101        | Math, English, Art |

-- 1NF compliant — separate table:
student_subjects: student_id (FK), subject_name
| 101 | Math    |
| 101 | English |
| 101 | Art     |
```

**Example 2: Fixing repeating groups**
```
-- Violates 1NF:
| order_id | product1 | qty1 | product2 | qty2 |

-- 1NF compliant:
order_items: order_id (FK), product_id (FK), quantity
```

**Example 3: Primary key after flattening**
After converting to 1NF by flattening `student_subjects`, the new table may need a composite primary key:
```sql
PRIMARY KEY (student_id, subject_name)
-- Or a surrogate key:
id SERIAL PRIMARY KEY
```

## Common Mistakes

- **Storing JSON or arrays in a column as a workaround**: Modern databases support JSON/ARRAY columns, which can store multiple values. These are appropriate in specific cases but violate 1NF for relational querying purposes.
- **Confusing "atomic" with "short"**: An atomic value can be long (a full address as one field). It is atomic if the database never needs to query its internal parts. If you ever query by postcode, the full address is not atomic — it must be split.
- **Creating too many columns for variety**: Adding phone1 through phone10 seems to solve the problem — until the 11th phone number arrives. Only a separate table scales indefinitely.

## Mental Model

Think of 1NF like a proper file cabinet. Each drawer holds exactly one category of document. Each document is one coherent unit. No drawer has multiple documents jammed into one folder and labeled "miscellaneous." And no folder has a page that lists twenty documents all mixed together. One item, one place, one clear purpose. Everything else is accessible, searchable, and consistent.

## Mini Summary

- ✔ 1NF requires atomic values — one value per cell, no lists or sets
- ✔ 1NF requires no repeating groups — no phone1, phone2, phone3 patterns
- ✔ 1NF requires unique rows — every row must be distinguishable via a primary key
- ✔ Convert by creating separate tables and linking with foreign keys
- ✔ 1NF is the prerequisite for all higher Normal Forms

# Guided Practice Quest

Work through the guided steps to identify 1NF violations in a given table and describe the decomposition that would bring it into compliance.

# Solo Practice Quest

You are given this unnormalised table for a university enrolment system:
`enrolment_id, student_name, student_email, courses (comma-separated), teacher_names (comma-separated), enrolment_date`
(1) List every 1NF violation you can identify. (2) Redesign the table into a set of 1NF-compliant tables with appropriate foreign key relationships. (3) Write the CREATE TABLE statements. (4) Write a query on your new schema that returns all courses for a specific student. Reflect on how the redesign changes the query complexity.

# Integration

**Mathematics**: In formal logic, an atomic formula is one that cannot be further decomposed into simpler logical formulae. An atom is the fundamental unit of the system. Database atomicity at the 1NF level is precisely this concept — values that are the irreducible units of information for the purposes of the schema.

**Software Engineering**: In API design, a well-formed data model returns one item per field — not comma-separated lists in JSON strings, not embedded encoded data. 1NF's requirements mirror the "single value per field" principle of clean API design. Systems that violate 1NF in the database inevitably push the complexity upward — into application code, where it is harder to enforce and test.

# Lore Conclusion

Master Selvaris picked up the family registration scroll and methodically rewrote it — one row per family member, each row containing one name, one birth date, one role. The work took an hour. "This will serve the Archive for the next hundred years," he said, without irony. "The original would have served no one." He handed you the pen. "Whenever you design a table, ask this first: can every value in every column be further divided? If yes, it is not yet 1NF." He turned to the next section of the ledger. "Now try this one."

---
