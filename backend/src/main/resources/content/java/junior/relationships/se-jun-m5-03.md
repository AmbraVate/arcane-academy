---
id: se-jun-m5-03
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m5
moduleTitle: "Module 5: Databases"
moduleGlyph: "🗄️"
moduleSortOrder: 5
topicSlug: relationships
topicTitle: "Relationships"
topicSortOrder: 3
lesson: relationships
title: "Relationships"
sortOrder: 3
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [joins]
integrationDomains: [mathematics, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly identifies a one-to-many relationship and explains the foreign key placement"
    - "Explains why a junction table is needed for many-to-many relationships"
    - "Describes first normal form (1NF) in plain terms"
    - "Sketches an ER diagram with at least two entities and a relationship"
    - "Reflects on how normalisation reduces data anomalies"
  keywords: [one-to-many, many-to-many, foreign key, junction table, normalisation, 1NF, 2NF, ER diagram]
  modelAnswer: |
    -- One-to-many: one department has many employees
    -- Foreign key 'dept_id' lives on the MANY side (employees table)
    CREATE TABLE departments (id INT PRIMARY KEY, name VARCHAR(100));
    CREATE TABLE employees (
        id INT PRIMARY KEY,
        name VARCHAR(100),
        dept_id INT REFERENCES departments(id)  -- FK on the many side
    );

    -- Many-to-many: students enrol in many courses; courses have many students
    -- Junction table holds both foreign keys
    CREATE TABLE enrolments (
        student_id INT REFERENCES students(id),
        course_id  INT REFERENCES courses(id),
        enrolled_at DATE,
        PRIMARY KEY (student_id, course_id)
    );
guidedSteps:
  - id: rel-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In a one-to-many relationship between `orders` and `order_items`, where does the foreign key column live?
    inputConfig:
      options:
        - "In the orders table, pointing to order_items"
        - "In the order_items table, pointing to orders"
        - "In a separate junction table"
        - "In both tables"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["In the order_items table, pointing to orders"]
      rejectedFeedback: "In a one-to-many relationship, the foreign key lives on the 'many' side. Each order_item belongs to one order, so order_items.order_id references orders.id. This is the canonical pattern: the child (many) holds a reference to the parent (one)."
    hint: "The foreign key always lives on the 'many' side — the side that belongs to the 'one'."
    reflectionPrompt: "Foreign key on the many side is a fundamental rule. If you see a FK on the 'one' side, something is wrong with the design."

  - id: rel-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Students can enrol in many courses, and each course can have many students. What database structure handles this relationship?
    inputConfig:
      options:
        - "A foreign key in the students table"
        - "A foreign key in the courses table"
        - "A junction (associative) table with foreign keys to both students and courses"
        - "A BLOB column storing a list of course IDs in the students table"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A junction (associative) table with foreign keys to both students and courses"]
      rejectedFeedback: "Many-to-many relationships cannot be represented with a single foreign key — neither table can hold a list of IDs in a relational column. A junction table (e.g. enrolments) is created with foreign keys to both tables, turning the many-to-many into two one-to-many relationships."
    hint: "A foreign key stores a single reference. What do you do when you need to store many?"
    reflectionPrompt: "Junction tables are so common they deserve their own name: associative entities. They often gain their own attributes too — like 'enrolment_date' or 'grade'."

  - id: rel-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain what first normal form (1NF) means and give an example of a table that violates it, along with how to fix the violation.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [atomic, single, repeating, column, separate]
      rejectedFeedback: "1NF requires that each column hold a single, atomic value — no lists, arrays, or comma-separated values in a single cell. Violation: a 'phone_numbers' column containing '555-0101, 555-0102'. Fix: create a separate phone_numbers table with a foreign key to the parent record."
    hint: "1NF is about what should NOT be inside a single cell."
    reflectionPrompt: "Storing lists in a single column is tempting but creates enormous problems for querying, updating, and maintaining data integrity."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which relationship type requires a junction table in a relational database?"
    options:
      - "One-to-one"
      - "One-to-many"
      - "Many-to-many"
      - "Self-referencing"
    correctIndex: 2
    feedback: "Many-to-many relationships require a junction (associative) table because a single foreign key column can only hold one reference. The junction table stores pairs of foreign keys, converting the many-to-many into two one-to-many relationships."
  - type: MULTIPLE_CHOICE
    question: "In an ER diagram, what does a line with a 'crow's foot' notation on one end represent?"
    options:
      - "A one-to-one relationship"
      - "The 'one' side of a relationship"
      - "The 'many' side of a relationship"
      - "A nullable foreign key"
    correctIndex: 2
    feedback: "The crow's foot symbol (three lines spreading like a bird's foot) represents the 'many' end of a relationship in ER diagrams. A single vertical line represents the 'one' end."
retrieval:
  recall: "Describe three types of table relationships and explain how each is implemented using foreign keys."
  explain: "A developer stores a user's roles as a comma-separated string ('admin,editor,viewer') in a single column. What are the problems with this design and how would you fix it?"
  mistakeId:
    code: |
      CREATE TABLE authors (
          id INT PRIMARY KEY,
          name VARCHAR(100),
          book_ids VARCHAR(500)  -- comma-separated list of book IDs
      );
    answer: "Storing book_ids as a comma-separated string violates 1NF (atomic values). Problems: cannot use JOIN to get book details; cannot enforce referential integrity; searching for a specific book requires string parsing; adding/removing a book requires string manipulation with risk of corruption. Fix: create a separate books table with author_id as a foreign key (one-to-many), or if authors can have many books and books can have many authors, create an author_books junction table."
---

# Hook

Every meaningful database stores not just data — it stores *relationships* between data. A customer is related to their orders. An order is related to its items. An item is related to a product. These relationships are the connective tissue of a relational database, and designing them correctly determines whether your database is a reliable source of truth or a fragile mess.

Poor relationship design leads to data anomalies: updating a customer's address in one place but not another; being unable to delete a category without deleting all its products; storing the same information in ten different rows so they drift out of sync. Understanding how to model one-to-many, many-to-many, and one-to-one relationships — and how to normalise your schema — is one of the most important database design skills you will build.

> Think of any system that manages related entities — a school, a library, an online shop. What are the entities, and what relationships exist between them? Are any of those relationships many-to-many?

# Lore Introduction

The Academy's Registry grew chaotic in its early years. Each student's scroll listed their name, their master's name, and their master's specialisation — in full, in every row. When a master changed their specialisation, hundreds of scrolls had to be updated. When a mistake crept in, the scrolls gave contradictory answers to the same question.

The Head Archivist introduced the **Doctrine of Referenced Records**: each piece of information lives in exactly one place, and all other records refer to it by a unique identifier. A student's scroll now records only their master's ID — the details live in the Master Registry, updated once and consulted everywhere. This is relational database design, expressed in parchment.

# Core Learning

## Concept Introduction

**Three fundamental relationship types:**

**One-to-Many** (most common)
One parent record relates to many child records. The foreign key lives on the *many* side.
```sql
-- One department has many employees
CREATE TABLE departments (id INT PRIMARY KEY, name VARCHAR(100));
CREATE TABLE employees (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    dept_id INT REFERENCES departments(id)  -- FK on the many side
);
```

**Many-to-Many**
Requires a **junction table** (also called an associative table).
```sql
-- Students enrol in many courses; courses have many students
CREATE TABLE enrolments (
    student_id INT REFERENCES students(id),
    course_id  INT REFERENCES courses(id),
    enrolled_date DATE,
    PRIMARY KEY (student_id, course_id)
);
```

**One-to-One**
One record corresponds to exactly one related record. FK can live on either side; put it on the table that is optional.
```sql
-- Each user has at most one profile
CREATE TABLE profiles (
    id INT PRIMARY KEY,
    user_id INT UNIQUE REFERENCES users(id),  -- UNIQUE enforces one-to-one
    bio TEXT
);
```

**Normalisation basics:**
- **1NF**: Every column holds a single, atomic value. No lists in cells.
- **2NF**: Every non-key column depends on the WHOLE primary key (relevant for composite keys).

## Why It Matters

Correct relationship design prevents **data anomalies**: update anomalies (changing one copy of data but not all), insertion anomalies (inability to insert without unrelated data), and deletion anomalies (losing data by deleting an unrelated record). Normalisation is the discipline of eliminating these anomalies by ensuring each fact is stored in exactly one place.

## Worked Examples

**Example 1 — One-to-many ER sketch**
```
departments          employees
-----------          ---------
id (PK)  ←───────── dept_id (FK)
name                 id (PK)
                     name
```

**Example 2 — Many-to-many with junction table**
```sql
-- Books and authors: one book can have multiple authors; one author writes multiple books
CREATE TABLE books (id INT PRIMARY KEY, title VARCHAR(200));
CREATE TABLE authors (id INT PRIMARY KEY, name VARCHAR(100));
CREATE TABLE book_authors (
    book_id   INT REFERENCES books(id),
    author_id INT REFERENCES authors(id),
    PRIMARY KEY (book_id, author_id)
);
```

**Example 3 — 1NF violation and fix**
```sql
-- VIOLATION: multi-valued column
CREATE TABLE students (
    id INT,
    name VARCHAR(100),
    subjects VARCHAR(500)  -- 'Maths,Science,English' — WRONG
);

-- FIX: separate table
CREATE TABLE student_subjects (
    student_id INT REFERENCES students(id),
    subject VARCHAR(100),
    PRIMARY KEY (student_id, subject)
);
```

## Common Mistakes

- **FK on the wrong side** — putting the foreign key on the "one" side in a one-to-many (it belongs on the "many" side).
- **Skipping the junction table** — storing comma-separated IDs in a column instead of a proper many-to-many table.
- **No referential integrity constraints** — declaring foreign keys without `REFERENCES` means the database does not enforce the relationship.
- **Over-normalising** — splitting data into too many tiny tables for simple data that rarely changes.
- **Forgetting that junction tables can carry attributes** — enrolment date, order quantity, and role assignment date are all junction-table attributes.

## Mental Model

Think of relationships as contracts. A one-to-many contract says: "Each child must point to a parent." A many-to-many contract says: "Two parties meet in a shared room — the junction table is that room." Normalisation says: "Each fact is written down once and referenced everywhere else, never copied."

## Mini Summary

✔ One-to-many: foreign key lives on the "many" side (the child table).
✔ Many-to-many: requires a junction table with foreign keys to both parent tables.
✔ One-to-one: foreign key with a UNIQUE constraint; place it on the optional/weaker side.
✔ 1NF: every column holds a single atomic value — no arrays or comma-separated lists.
✔ Normalisation stores each fact in one place, eliminating update and deletion anomalies.

# Guided Practice Quest

**The Doctrine of Referenced Records**
The Academy's Registry is being redesigned. Model the correct relationships between students, masters, and courses.
Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Design a database schema for a simple music streaming service with: artists, albums, songs, and playlists (users can add songs to playlists). (1) Identify all relationships and their types. (2) Write CREATE TABLE SQL for all tables with appropriate foreign keys. (3) Identify which relationships require junction tables and name them. (4) Verify your schema satisfies 1NF. Reflect in 3 sentences on one design decision you found non-obvious and how you resolved it.

# Integration

**Connecting to Design — Information Architecture**
Relational database design is a form of **information architecture** — the discipline of structuring and organising information so it can be found, understood, and used effectively. Information architects design the taxonomies, hierarchies, and relationships in websites, knowledge bases, and content systems. Database designers do the same thing, but with formal mathematical constraints (foreign keys, normal forms) that enforce correctness.

The principles are strikingly similar: avoid duplication, maintain a single source of truth, design relationships that reflect the real world. An IA designing a website navigation considers how users move between related content; a database designer considers how queries traverse related tables. Both are asking: "What relates to what, and how should that relationship be expressed structurally?"

> In your experience using apps or websites, can you think of a place where the information architecture felt wrong — where related things were hard to find, or where the same information appeared in contradictory ways in different places? What database design problem might that reflect?

# Lore Conclusion

The Archivist finishes drafting the new Registry schema: three clean tables, each storing one type of record, connected by precisely-placed references. Where previously a master's name was copied a hundred times, there is now a single Master record pointed to by a hundred student records. One update to the Master record cascades correctly everywhere.

The Head Archivist examines the design and pronounces it sound. "You have eliminated the three great anomalies," she says. "Now your data has integrity." In the next lesson, the Registry will be brought to life in Java code — learning how an ORM translates these table relationships into objects, and what happens when the two worlds do not map cleanly.

---
