---
id: de-app-m6-05
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
lesson: second_normal_form
title: "Second Normal Form"
sortOrder: 5
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m6-04]
integrationDomains: [mathematics, software_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines Second Normal Form (2NF) in own words
    - Explains what a partial dependency is with a concrete example
    - Explains why 2NF only applies to tables with composite primary keys
    - Demonstrates how to eliminate a partial dependency by decomposition
    - Reflects on the relationship between 2NF and data redundancy
  keywords: [2NF, partial dependency, composite key, non-key attribute, decompose, redundancy, functional dependency]
  modelAnswer: |
    Second Normal Form (2NF) requires that a table is in 1NF and that every non-key attribute is fully functionally dependent on the entire primary key — not just part of it. Partial dependencies only occur in tables with composite primary keys. For example, in an enrolments table with a composite key of (student_id, course_id), if student_name depends only on student_id (not the whole key), that is a partial dependency. The fix is to move student_name to a separate students table and reference it via student_id as a foreign key. This removes the redundancy of repeating student names across every enrolment row.
guidedSteps:
  - id: de-app-m6-05-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A table has a composite primary key of (student_id, course_id). The column `student_name` depends only on student_id. What kind of dependency is this?
    inputConfig:
      options:
        - "Full functional dependency — student_name depends on the full key"
        - "Partial dependency — student_name depends on only part of the composite key"
        - "Transitive dependency — student_name depends on another non-key attribute"
        - "No dependency — student_name is independent of the key"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Partial dependency — student_name depends on only part of the composite key"]
      rejectedFeedback: "A partial dependency exists when a non-key attribute depends on only some columns of a composite primary key — here, student_name only needs student_id, not the full (student_id, course_id) pair."
    hint: "Does knowing just the student_id tell you the student_name, without needing the course_id?"
    reflectionPrompt: "What redundancy does this partial dependency create across the enrolments table?"
  - id: de-app-m6-05-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "A table with a single-column primary key (not composite) automatically satisfies ________ Normal Form because partial dependencies cannot exist."
    inputConfig:
      placeholder: "2NF"
    markingRule:
      matchMode: CONTAINS
      accepted: [2NF, Second Normal Form, second normal form]
      rejectedFeedback: "Partial dependencies require a composite key — if a table has a single-column primary key, there is no 'part of the key' for a non-key attribute to depend on. Such a table automatically satisfies 2NF (assuming it already satisfies 1NF)."
    hint: "What is required for a partial dependency to exist? What must the primary key be?"
    reflectionPrompt: "Why might a designer use a surrogate key (an auto-generated ID) to sidestep 2NF issues?"
  - id: de-app-m6-05-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, describe how you would eliminate the partial dependency of `student_name` on `student_id` in a composite-key enrolments table.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [separate, students, table, foreign key, decompose, move]
      rejectedFeedback: "Move student_name to a separate students table where student_id is the primary key. In the enrolments table, keep student_id as a foreign key that references students. This way, student_name is stored once and fetched via JOIN."
    hint: "The goal is to store student_name in exactly one place, not once per enrolment row."
    reflectionPrompt: "How many enrolment rows could be updated incorrectly if student_name is stored per-row and a student changes their name?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A table with composite key (order_id, product_id) has a column `product_price` that depends only on product_id. This violates:"
    options: ["1NF", "2NF", "3NF", "No normal form — this is acceptable"]
    correctIndex: 1
    feedback: "product_price depending on only product_id (part of the composite key) is a partial dependency — a 2NF violation. Fix: move product_price to the products table."
  - type: MULTIPLE_CHOICE
    question: "Which of the following tables is guaranteed to be in 2NF without any changes?"
    options:
      - "A table with a composite primary key and multiple non-key columns"
      - "A table with a single-column primary key that is already in 1NF"
      - "A table with no primary key at all"
      - "A table where all columns are part of the primary key"
    correctIndex: 1
    feedback: "A table with a single-column primary key cannot have partial dependencies (there is no 'part of the key' to depend on), so it automatically satisfies 2NF if it is already in 1NF."
retrieval:
  recall: "Define Second Normal Form and explain what type of dependency it eliminates."
  explain: "Why can partial dependencies only occur in tables with composite primary keys?"
  mistakeId:
    code: "2NF is about removing duplicate rows"
    answer: "Duplicate rows are a 1NF violation. 2NF is specifically about eliminating partial dependencies — where a non-key attribute depends on only part of a composite primary key. A table with no duplicate rows can still violate 2NF if it has composite keys and partial dependencies."
---

# Hook

An enrolments table records which students are taking which courses. Its primary key is the combination of `student_id` and `course_id`. It also stores `student_name`, `student_email`, and `course_name` alongside the key. Alice Smith is enrolled in five courses. Her name and email appear in five rows — five identical copies. When Alice changes her name after getting married, five rows need updating. If one is missed, the database has two names for the same person.

This is a partial dependency — and it is what Second Normal Form is designed to eliminate. 1NF made sure each cell has one value. 2NF ensures that each value is in the right table: one that is defined by the whole primary key, not just part of it.

# Lore Introduction

Master Selvaris opened an enrolment register. "The composite key is (student_id, course_id)," he explained. "The combination uniquely identifies each enrolment." He pointed to several columns. "But student_name, student_email — these depend only on student_id. They have nothing to do with which course the student is taking." He pointed to row after row of repeated names. "Alice Thorn. Alice Thorn. Alice Thorn. Twelve times — once for each course she attends." He tapped the desk. "When Alice marries and becomes Alice Vane, twelve rows must be changed. Twelve chances for error. One cause: a partial dependency. Second Normal Form eliminates it."

# Core Learning

## Concept Introduction

| Concept | Definition | Example |
|---------|-----------|---------|
| **Full functional dependency** | A non-key attribute depends on the entire composite primary key | `grade` depends on both `student_id` AND `course_id` |
| **Partial dependency** | A non-key attribute depends on only part of a composite key | `student_name` depends on `student_id` alone |
| **2NF requirement** | Table is in 1NF AND has no partial dependencies | All non-key attributes depend on the whole primary key |
| **Decomposition** | Moving partially-dependent attributes to a separate table | `student_name` moves to a `students` table |

## Why It Matters

Partial dependencies create the same update anomaly seen in redundant tables:
- `student_name` stored in every enrolment row must be updated everywhere when the name changes
- Each mismatch creates an inconsistency — the student has multiple names
- Deleting all of a student's enrolments also deletes the only record of their name (delete anomaly)

## Worked Examples

**Example 1: Identifying the partial dependency**
```
Table: enrolments
Primary key: (student_id, course_id)

Functional dependencies:
student_id           → student_name   ← PARTIAL (depends on half the key)
student_id           → student_email  ← PARTIAL
course_id            → course_name    ← PARTIAL
(student_id, course_id) → grade       ← FULL (needs both)
```

**Example 2: Decomposition to 2NF**
```sql
-- Before 2NF (violating table):
-- enrolments(student_id, course_id, student_name, course_name, grade)

-- After 2NF decomposition:
CREATE TABLE students (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE courses (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE enrolments (
    student_id INT REFERENCES students(id),
    course_id  INT REFERENCES courses(id),
    grade      DECIMAL(4,2),
    PRIMARY KEY (student_id, course_id)
);
```

**Example 3: Querying after decomposition**
```sql
SELECT s.name, c.name, e.grade
FROM enrolments e
JOIN students s ON e.student_id = s.id
JOIN courses c  ON e.course_id  = c.id;
```

## Common Mistakes

- **Thinking 2NF requires normalising all dependencies**: 2NF only eliminates *partial* dependencies — those where a non-key attribute depends on part of a composite key. Transitive dependencies (3NF issues) are not addressed.
- **Applying 2NF to tables with no composite key**: If the primary key is a single column (or a surrogate key), 2NF is automatically satisfied. No action needed.
- **Over-decomposing**: Creating a separate table for every column is not normalisation — it is over-engineering. Move only the partially dependent attributes.

## Mental Model

Imagine an enrolments register in a school. Each row records: "Student Alice is in Course Mathematics, taught by Mr Brown, and earned 85%." The grade belongs to the enrolment — it depends on both student and course. But Alice's address belongs to Alice — it only depends on the student. Storing Alice's address in the enrolments register means it changes every time Alice updates her address. The fix: Alice's address lives in the student file, not the enrolment register. Each fact lives where it belongs.

## Mini Summary

- ✔ 2NF requires 1NF PLUS no partial dependencies on a composite key
- ✔ A partial dependency is when a non-key attribute depends on only part of the composite key
- ✔ Tables with single-column primary keys automatically satisfy 2NF
- ✔ Fix partial dependencies by moving the attribute to a table where the partial key is the full key
- ✔ 2NF eliminates update anomalies caused by partially-dependent attributes being repeated

# Guided Practice Quest

Work through the guided steps to identify partial dependencies in a composite-key table and perform the decomposition required to achieve 2NF.

# Solo Practice Quest

You have a table with composite key `(supplier_id, product_id)` and the following columns: `supplier_name`, `supplier_city`, `product_name`, `product_category`, `unit_price`, `quantity_stocked`. (1) Identify every functional dependency. (2) Identify which are partial and which are full. (3) Decompose the table into 2NF by creating separate tables. (4) Write CREATE TABLE statements for each. (5) Write a JOIN query that retrieves all products from suppliers in a specific city with their stocked quantities. Reflect: how does 2NF change the behaviour of update and delete operations?

# Integration

**Mathematics**: Second Normal Form formalises the concept of a minimal set of attributes determining another attribute. A full functional dependency is one where no proper subset of the determinant suffices. Partial dependencies exist when a proper subset (part of the composite key) is already sufficient to determine the dependent attribute. This is the mathematical basis for 2NF.

**Software Engineering**: The principle of cohesion — each module should have one clear responsibility — applies at the table level. A table with partial dependencies has mixed cohesion: it describes two different entities (students and enrolments) in one structure. 2NF separates these concerns, giving each table a single, coherent responsibility.

# Lore Conclusion

Master Selvaris reorganised the enrolment register into three separate books: one for students, one for courses, one for enrolments (which now contained only student references, course references, and grades). "This took three hours," he said. "And will save three thousand hours over the next decade." He pointed to the student book. "Alice Vane's name, once changed here, is correct in every enrolment automatically." He closed the books. "2NF: every attribute belongs to the table it depends on. Not the table that happens to also store related data. The table it actually depends on."

---
