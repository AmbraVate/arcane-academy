---
id: de-app-m5-05
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
lesson: constraints
title: "Constraints"
sortOrder: 1
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m5-04]
integrationDomains: [mathematics, software_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines a database constraint and explains its purpose
    - Lists and explains at least four types of SQL constraint
    - Explains when constraints are checked and what happens when they fail
    - Describes the difference between column-level and table-level constraints
    - Reflects on the trade-off between strict constraints and system flexibility
  keywords: [constraint, PRIMARY KEY, UNIQUE, NOT NULL, CHECK, FOREIGN KEY, violation, integrity]
  modelAnswer: |
    A database constraint is a rule enforced by the database engine that limits what values can be stored in a table. Types include: NOT NULL (column must have a value), UNIQUE (values must be distinct), PRIMARY KEY (unique, non-null identifier for each row), CHECK (value must satisfy a condition), and FOREIGN KEY (value must reference an existing row in another table). Constraints are checked on every INSERT and UPDATE — a violation raises an error and rolls back the operation. Column-level constraints apply to one column; table-level constraints can reference multiple columns (e.g., a composite PRIMARY KEY). Constraints ensure integrity but can be restrictive for systems that need to accept incomplete data temporarily.
guidedSteps:
  - id: de-app-m5-05-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      An employee table has `department_id INT REFERENCES departments(id)`. You try to insert an employee with a department_id that does not exist in the departments table. What happens?
    inputConfig:
      options:
        - "The employee is inserted with a NULL department_id instead"
        - "The database raises a foreign key constraint violation and rejects the insert"
        - "The insert succeeds, and the missing department is created automatically"
        - "The insert succeeds, but a warning is logged"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The database raises a foreign key constraint violation and rejects the insert"]
      rejectedFeedback: "A FOREIGN KEY constraint ensures referential integrity — inserting a value that has no corresponding parent row is rejected immediately with an error."
    hint: "The constraint exists to prevent orphaned rows — rows that reference a parent that doesn't exist."
    reflectionPrompt: "What would happen to data quality over time if foreign key constraints didn't exist?"
  - id: de-app-m5-05-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "A ________ KEY constraint ensures that each row in a table can be uniquely identified and the identifying column(s) cannot be NULL."
    inputConfig:
      placeholder: "PRIMARY"
    markingRule:
      matchMode: CONTAINS
      accepted: [PRIMARY, primary]
      rejectedFeedback: "A PRIMARY KEY constraint combines NOT NULL and UNIQUE — every row must have a unique, non-null identifier."
    hint: "This type of key is the main identifier for rows in a table."
    reflectionPrompt: "Can a table have more than one PRIMARY KEY? What about more than one UNIQUE constraint?"
  - id: de-app-m5-05-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain the difference between a UNIQUE constraint and a PRIMARY KEY constraint.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [UNIQUE, PRIMARY KEY, NULL, identifier, difference]
      rejectedFeedback: "Both ensure uniqueness, but a PRIMARY KEY also forbids NULL and can only be declared once per table. A table can have multiple UNIQUE constraints, and UNIQUE columns may contain NULLs (depending on the database)."
    hint: "Think about NULL values and how many of each can exist per table."
    reflectionPrompt: "Why would you choose a UNIQUE constraint over a PRIMARY KEY for a business email column?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which SQL constraint ensures a column value falls within a specified range or satisfies a condition?"
    options: ["UNIQUE", "NOT NULL", "CHECK", "FOREIGN KEY"]
    correctIndex: 2
    feedback: "The CHECK constraint enforces a condition — for example, CHECK (age >= 0 AND age <= 150) prevents impossible age values."
  - type: MULTIPLE_CHOICE
    question: "A table has `email VARCHAR(255) UNIQUE NOT NULL`. What happens if you try to insert two rows with the same email?"
    options:
      - "The second insert succeeds and the first is overwritten"
      - "Both inserts succeed but a warning is logged"
      - "The second insert is rejected with a constraint violation error"
      - "NULL is substituted for the duplicate email"
    correctIndex: 2
    feedback: "The UNIQUE constraint rejects any insert that would create a duplicate value. The operation is rolled back and an error is returned."
retrieval:
  recall: "List the five main SQL constraint types and state what each enforces in one sentence."
  explain: "Why do constraints improve data integrity even though they can cause insert or update failures?"
  mistakeId:
    code: "constraints slow down the database so should be avoided"
    answer: "Constraints do add a small overhead to write operations, but this cost is vastly outweighed by the benefit of guaranteed data integrity. Without constraints, bad data accumulates silently and becomes exponentially more expensive to fix. Well-designed constraints are an investment in data trustworthiness."
---

# Hook

Imagine a database where any order can reference any customer ID — even one that does not exist. Orders pile up pointing to ghost customers. Reports count revenue from orders that can never be linked to real people. Support staff look up customers by their order ID and find nothing. The data is present. The structure is broken.

Database constraints are the rules that prevent this kind of structural corruption. They are the database engine's built-in enforcement mechanism — guardrails that ensure the data stored in your tables actually makes sense and can be trusted. Every professional data engineer understands constraints deeply.

What types of constraints exist, and how do they work together to protect your data?

# Lore Introduction

Master Selvaris held up a contract scroll. "Every scroll in the Archive must abide by the Compact of Entry," he said. "A scroll must have a unique registration number. Its author must be on record. Its date must be valid. Its region code must match a known province." He pointed to each clause. "These are not suggestions — they are enforced at the gate. A scroll that violates any clause is turned away. Not stored somewhere for later review. Turned away." He met your eyes. "Constraints are the Compact. They are what makes the Archive reliable."

# Core Learning

## Concept Introduction

| Constraint | Purpose | Example |
|-----------|---------|---------|
| **NOT NULL** | Column must always have a value | `name VARCHAR(100) NOT NULL` |
| **UNIQUE** | Values in the column must be distinct across rows | `email VARCHAR(255) UNIQUE` |
| **PRIMARY KEY** | Unique, non-null identifier for each row (combines NOT NULL + UNIQUE) | `id SERIAL PRIMARY KEY` |
| **CHECK** | Value must satisfy a specified condition | `CHECK (age >= 0 AND age <= 150)` |
| **FOREIGN KEY** | Value must reference an existing row in another table | `REFERENCES departments(id)` |
| **DEFAULT** | Provides a default value when none is supplied | `created_at TIMESTAMP DEFAULT NOW()` |

## Why It Matters

Constraints are the database's last line of defence for data integrity. They enforce rules that application code might miss — due to bugs, concurrent writes, or direct database access that bypasses the application. When constraints are missing:

- Invalid records accumulate silently
- Orphaned rows break join queries
- Duplicate data corrupts aggregations
- Engineers spend days cleaning up preventable problems

## Worked Examples

**Example 1: Full constraint declaration**
```sql
CREATE TABLE orders (
    id          SERIAL PRIMARY KEY,
    customer_id INT NOT NULL REFERENCES customers(id),
    total       DECIMAL(10,2) CHECK (total >= 0),
    status      VARCHAR(20) NOT NULL DEFAULT 'pending',
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);
```

**Example 2: Composite UNIQUE constraint**
```sql
-- A student can only enrol in each course once
CREATE TABLE enrolments (
    student_id INT NOT NULL REFERENCES students(id),
    course_id  INT NOT NULL REFERENCES courses(id),
    CONSTRAINT uq_enrolment UNIQUE (student_id, course_id)
);
```

**Example 3: CHECK with business logic**
```sql
ALTER TABLE events
ADD CONSTRAINT chk_dates CHECK (end_date >= start_date);
-- Prevents events where the end is before the start
```

## Common Mistakes

- **Omitting NOT NULL on required columns**: Without NOT NULL, columns that should always have values can be silently NULL, causing joins and calculations to fail.
- **Forgetting composite keys**: A student enrolled in a course is uniquely identified by the combination of student_id and course_id — not by either alone.
- **Adding constraints after data is already loaded**: Adding a NOT NULL constraint to a column that contains NULLs fails. You must clean existing data before adding retroactive constraints.

## Mental Model

Think of constraints as the building code for a database. Just as building regulations prevent a house from being constructed without fire exits or structural supports, database constraints prevent data from being stored in structurally unsafe configurations. You can build without following the code — but the building will eventually fail, and the failure will be catastrophic.

## Mini Summary

- ✔ Constraints are database-level rules enforced on every insert and update
- ✔ Key types: NOT NULL, UNIQUE, PRIMARY KEY, CHECK, FOREIGN KEY, DEFAULT
- ✔ A constraint violation immediately rejects the operation with an error
- ✔ Constraints catch bugs that application code misses — treat them as essential, not optional
- ✔ Composite constraints (spanning multiple columns) are needed for multi-column uniqueness

# Guided Practice Quest

Work through the guided steps to identify which constraint type applies to each scenario and explain how constraints protect data integrity at the database level.

# Solo Practice Quest

Design a `library` database with three tables: `books`, `members`, and `loans`. For each table, define at least five columns and apply appropriate constraints. Write the full CREATE TABLE statements with all constraints declared explicitly. Then answer: (1) What happens if you try to insert a loan for a book_id that does not exist? (2) What happens if the same member tries to borrow the same book twice? (3) How would you add a constraint that prevents a loan end_date from being before its start_date?

# Integration

**Mathematics**: A relational database is based on relational algebra, where each relation (table) is a set of tuples satisfying a predicate. Constraints are the formal predicates that define which tuples are valid members of the relation. This is mathematically equivalent to domain restrictions in set theory — the constraint defines the valid domain of each column.

**Software Engineering**: The principle of "make illegal states unrepresentable" — from type-driven design — applies directly to database constraints. Rather than trusting application code to always enforce business rules, you encode those rules into the database schema itself. This is defensive design: the system physically cannot store states you have declared impossible.

# Lore Conclusion

Master Selvaris placed the Compact of Entry back in its glass case. "The Archive has stood for four hundred years," he said. "In that time, no forged scroll, no impossible date, no orphaned record has passed through these gates — because every entry is checked against the Compact." He walked to the shelves. "Application code changes. Engineers come and go. Direct imports bypass logic layers. But the Compact is always here, at the gate, checking every entry." He turned to you. "Design your constraints carefully. They will outlast every other layer of your system."

---
