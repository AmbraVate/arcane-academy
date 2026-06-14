---
id: de-app-m2-13
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
lesson: relationship_design
title: "Relationship Design"
sortOrder: 4
difficulty: 2
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m2-12]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Correctly identifies the relationship type (one-to-one, one-to-many, many-to-many) for given scenarios
    - Explains how to read a data model and identify where foreign keys should go
    - Describes the process of translating a conceptual model into a physical SQL schema
    - Identifies at least two design mistakes that arise from incorrectly modelling relationships
    - Reflects on how relationship design decisions affect query complexity and data integrity
  keywords: [relationship design, entity, cardinality, one-to-one, one-to-many, many-to-many, foreign key, junction table, schema]
  modelAnswer: |
    Relationship design begins with identifying entities and their cardinalities — how many instances of one entity relate to one instance of another. One-to-one is implemented with a UNIQUE FK; one-to-many with a plain FK in the child table; many-to-many with a junction table containing two FKs. Common design mistakes include placing the FK on the wrong side, using comma-separated lists for many values, and confusing a one-to-many for a many-to-many or vice versa. The goal is a schema where all relationships are enforced by constraints, queries are clean and efficient, and no data is duplicated.
guidedSteps:
  - id: de-app-m2-13-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A library allows each book to be borrowed by one member at a time, but a member can borrow many books simultaneously. What relationship type is this?
    inputConfig:
      options:
        - "One-to-one: each book is borrowed by one member"
        - "One-to-many: one member can borrow many books; each book is borrowed by one member at a time"
        - "Many-to-many: books and members are both involved in many relationships"
        - "No relationship — books and members are unrelated entities"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["One-to-many: one member can borrow many books; each book is borrowed by one member at a time"]
      rejectedFeedback: "This is one-to-many: one member → many current borrowings, but each book → one borrower at a time. The FK (member_id) goes in the borrowings table (child), not in the members or books table. Note: this models current borrowing state. A full borrowing history would be many-to-many (one book has been borrowed by many members over time; one member has borrowed many books)."
    hint: "Look at both sides: how many can one member relate to, and how many can one book relate to, at a given time?"
    reflectionPrompt: "How would the relationship change if you modelled the full borrowing history rather than just who currently holds a book?"
  - id: de-app-m2-13-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      When designing relationships, determining how many instances of one entity can be associated with one instance of another is called analysing the relationship's ________.
    inputConfig:
      placeholder: "cardinality"
    markingRule:
      matchMode: CONTAINS
      accepted: [cardinality, multiplicity, degree]
      rejectedFeedback: "Cardinality describes the numerical relationship between entities — specifically, whether the relationship is one-to-one, one-to-many, or many-to-many. It is determined by answering two questions: 'How many B can one A have?' and 'How many A can one B have?' The answers determine which relationship type applies and therefore how to implement it in SQL."
    hint: "This term describes the 'how many' aspect of a relationship — whether it is 1:1, 1:N, or M:N."
    reflectionPrompt: "How does cardinality affect where you place the foreign key in your schema?"
  - id: de-app-m2-13-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain the process you would follow to translate a real-world scenario into the correct SQL relationship design — from identifying the entities to writing the final CREATE TABLE statements.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [entity, cardinality, relationship, foreign key, junction, table, one-to-many, many-to-many, primary key, constraint]
      rejectedFeedback: "The process is: (1) identify the entities (nouns in the domain) and give each a table; (2) identify the relationships between entities and determine cardinality by asking 'how many X per Y?' in both directions; (3) choose the implementation: UNIQUE FK for 1:1, plain FK in child for 1:many, junction table for many:many; (4) write CREATE TABLE statements with appropriate primary keys, foreign keys, and constraints."
    hint: "Think about the sequence: entities first, then relationships, then cardinality, then implementation."
    reflectionPrompt: "How does getting cardinality wrong (e.g. designing one-to-many when it should be many-to-many) manifest as a bug in the running application?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A hospital system records which doctors are assigned to which patients. A doctor can treat many patients; a patient can be treated by many doctors. What is needed?"
    options:
      - "A doctor_id FK in the patients table"
      - "A patient_id FK in the doctors table"
      - "A junction table (e.g. doctor_patient_assignments) with FKs to both tables"
      - "A shared primary key between doctors and patients"
    correctIndex: 2
    feedback: "This is a classic many-to-many relationship. A junction table (doctor_patient_assignments, or similar) with doctor_id and patient_id foreign keys is required. Each row represents one doctor-patient treatment relationship. Attributes like assigned_date and role could also live on this junction table."
  - type: MULTIPLE_CHOICE
    question: "You have designed a one-to-many relationship but later realise it should be many-to-many. What change is required?"
    options:
      - "Add a UNIQUE constraint to the existing foreign key"
      - "Remove the existing foreign key and add a junction table"
      - "Move the foreign key from the child table to the parent table"
      - "Add a second primary key to the child table"
    correctIndex: 1
    feedback: "Converting a one-to-many to many-to-many requires introducing a junction table. The existing FK in the child table is no longer sufficient (it would only allow each child to link to one parent). The junction table gets two FKs — one to each entity — and each row represents one association. This is a schema migration change."
retrieval:
  recall: "List the three relationship types and the SQL mechanism used to implement each."
  explain: "Describe the step-by-step process for determining the correct relationship type between two entities."
  mistakeId:
    code: "designing a one-to-many when the domain actually requires many-to-many"
    answer: "If you design a plain FK (one-to-many) when the real-world relationship is many-to-many, you will discover the constraint when data is entered — the FK can only point to one parent, so each child can only be associated with one parent. Users will hit errors or work around them by duplicating data. The fix is a schema migration to add a junction table — which is costly in a live system. Getting cardinality right upfront avoids this."
---

# Hook

You now know the three relationship types and how each is implemented. This final lesson brings them together into a design process — the thinking you do before you write a single line of SQL.

Good relationship design is about asking the right questions. How many of X can relate to one Y? How many of Y can relate to one X? These two questions — asked for every pair of entities — determine your entire schema structure.

# Lore Introduction

The guild's new apprentice was assigned to design the archive's member-skills registry. He came back with a single table containing columns for `member_id`, `skill_1`, `skill_2`, and `skill_3`. Master Selvaris examined it for a long moment. "How many skills can a master arcanist have?" she asked. "More than three," he admitted. She handed the design back. "Start over. Ask two questions: how many members can have one skill? And how many skills can one member have?" He thought. "Many members can share a skill. One member can have many skills." She nodded. "What relationship is that?" He reached for the correct design. "Many-to-many. I need a junction table." She smiled. "Now you are designing. Before, you were guessing."

# Core Learning

## Concept Introduction

### The Two-Question Method

For any two entities, ask:

> **"How many [B] can one [A] have?"**
> **"How many [A] can one [B] have?"**

The answers determine the relationship type:

| Answer | Answer | Relationship | Implementation |
|--------|--------|--------------|----------------|
| One | One | One-to-one | FK + UNIQUE in one table |
| One | Many | One-to-many | FK in the "many" table |
| Many | Many | Many-to-many | Junction table |

### Worked Examples

**Example 1: User ↔ Passport**
- How many passports can one user have? **One** (at a time)
- How many users can one passport belong to? **One**
- → **One-to-one**: FK (user_id) in passports + UNIQUE

**Example 2: Department ↔ Employee**
- How many employees can one department have? **Many**
- How many departments can one employee belong to? **One** (primary department)
- → **One-to-many**: FK (dept_id) in employees

**Example 3: Author ↔ Book**
- How many books can one author write? **Many**
- How many authors can one book have? **Many** (co-authors)
- → **Many-to-many**: Junction table `book_authors`

### From Concept to SQL

Step 1: List the entities
```
Users, Articles, Tags, Comments
```

Step 2: Identify relationships and cardinalities
```
User → Articles:   one user writes many articles (1:N)
Article → Tags:    one article has many tags; one tag applies to many articles (M:N)
Article → Comments: one article has many comments (1:N)
User → Comments:   one user writes many comments (1:N)
```

Step 3: Implement

```sql
CREATE TABLE users (
    user_id   INT PRIMARY KEY,
    email     VARCHAR(255) NOT NULL UNIQUE,
    name      VARCHAR(200) NOT NULL
);

CREATE TABLE articles (
    article_id   INT PRIMARY KEY,
    author_id    INT NOT NULL,
    title        VARCHAR(300) NOT NULL,
    published_at TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(user_id) ON DELETE RESTRICT
);

CREATE TABLE tags (
    tag_id  INT PRIMARY KEY,
    name    VARCHAR(50) NOT NULL UNIQUE
);

-- Many-to-many: articles ↔ tags
CREATE TABLE article_tags (
    article_id  INT NOT NULL,
    tag_id      INT NOT NULL,
    PRIMARY KEY (article_id, tag_id),
    FOREIGN KEY (article_id) REFERENCES articles(article_id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE
);

CREATE TABLE comments (
    comment_id  INT PRIMARY KEY,
    article_id  INT NOT NULL,
    author_id   INT NOT NULL,
    content     TEXT NOT NULL,
    posted_at   TIMESTAMP NOT NULL,
    FOREIGN KEY (article_id) REFERENCES articles(article_id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users(user_id) ON DELETE SET NULL
);
```

### Common Design Pitfalls

| Pitfall | Symptom | Fix |
|---------|---------|-----|
| FK on wrong side | Can't associate multiple children to one parent | Move FK to child table |
| 1:N when domain is M:N | Users hit FK violations adding second association | Add junction table |
| Comma-separated values | Queries require string parsing | Junction table |
| Missing FK constraint | Orphaned records appear over time | Add FOREIGN KEY constraint |
| No index on FK column | Slow joins and lookups | Add index on FK |

## Common Mistakes

- **Placing the foreign key on the wrong side of a one-to-many**: The FK always lives in the child (many) table, pointing to the parent (one). Placing the FK in the parent creates a fixed limit on how many children can be associated.
- **Storing many-to-many relationships as comma-separated values**: A column like `tag_ids = "1,3,7"` cannot be indexed, cannot enforce referential integrity, and requires string parsing for every query. Use a junction table.
- **Omitting a junction table when domain logic is many-to-many**: Modelling a many-to-many as a one-to-many produces FK violations the first time a record has more than one association. Apply the two-question method before writing any SQL.
- **Not adding an index on foreign key columns**: Joins on unindexed FK columns perform full table scans. Every FK column should have an index — many databases (PostgreSQL, SQL Server) do not create FK indexes automatically.
- **Missing foreign key constraints entirely**: Without FK constraints, orphaned records accumulate silently over time — rows in a child table that reference deleted parent rows. Always declare `FOREIGN KEY` constraints to enforce referential integrity at the database level.

## Why It Matters

Incorrect relationship design causes two categories of failure:
1. **Structural failures**: The schema physically prevents valid data from being stored (FK violations)
2. **Query failures**: The data is stored but cannot be retrieved cleanly (string parsing, full table scans)

Both are expensive to fix in a live system with real data. Getting relationships right at design time is the single highest-leverage activity in database design.

## Mental Model

Think of relationship design like planning the corridors of a building before construction. You need to know how many people will move between each pair of rooms to decide whether a door, a corridor, or a junction hall is needed. A one-to-one relationship is a private connecting door. A one-to-many is a corridor from one room to many offices. A many-to-many is a central hub where two wings meet. Building the wrong type of connection — or forgetting one — means expensive structural changes after the building is inhabited. The two-question method is your architectural survey before the first brick is laid.

## Mini Summary

- ✔ Determine cardinality by asking "how many?" in both directions
- ✔ One-to-one → FK + UNIQUE; one-to-many → FK in child; many-to-many → junction table
- ✔ Translate: list entities → identify relationships → determine cardinality → write SQL
- ✔ Wrong cardinality causes structural and query failures that are expensive to fix
- ✔ Index every FK column for query performance

# Guided Practice Quest

Work through the guided steps to apply the two-question method to real-world scenarios, correctly identify relationship types, and translate a conceptual model into SQL CREATE TABLE statements.

# Solo Practice Quest

You are the lead database designer for a new property rental platform. The domain includes: Landlords, Properties, Rooms (within properties), Tenants, Leases, Maintenance Requests, and Payments. (1) For every pair of entities that are related, apply the two-question method and state the relationship type. (2) Draw a simple entity map (text-based is fine) showing all relationships. (3) Write the complete CREATE TABLE SQL for all seven tables, including all primary keys, foreign keys, UNIQUE constraints, CHECK constraints, and appropriate ON DELETE behaviours. (4) Write three SQL queries that use JOINs across at least two of your tables to answer realistic business questions. Justify every design decision you make.

# Integration

**Mathematics**: Relationship design applies the mathematical theory of binary relations to a concrete engineering problem. In set theory, a binary relation R on sets A and B is a subset of A × B. The cardinality of R constrains what kind of function (if any) the relation represents: one-to-one corresponds to a bijection (if total) or partial bijection; one-to-many to a non-injective function; many-to-many to a general relation that is not a function. Entity-Relationship (ER) modelling provides a formal notation for these constraints before they are translated to SQL. Understanding the mathematical underpinnings helps diagnose when a designed schema fails to faithfully represent the intended relational structure.

**Sciences (Systems Engineering)**: In complex engineered systems, interfaces between components are governed by explicit contracts specifying how many instances of one component connect to how many instances of another. In circuit design, a bus can connect one master to many slaves (one-to-many). In network topology, a switch connects many devices to many other devices (many-to-many). In mechanical engineering, a bolt fits exactly one nut (one-to-one, with UNIQUE constraint semantics). Database relationship design is the information-system equivalent of defining these interface contracts before building — ensuring the components are compatible before the system is assembled.

# Lore Conclusion

The apprentice presented the completed schema: seven tables, all relationships correctly typed and implemented. Master Selvaris walked through each one, verifying the cardinalities. "User to articles: one to many. Correct — the FK is in articles." She moved on. "Articles to tags: many to many. Correct — the junction table is present, both FKs declared, composite primary key prevents duplicate tagging." She reached the end. "Every relationship is implemented correctly. Every FK has a constraint. Every junction table carries its relationship attributes." She signed off on the design. "This is what good design looks like: every rule built into structure, every relationship enforced by the database itself. When someone asks 'which tags does this article have?' or 'which articles use this tag?' — the schema answers them cleanly, quickly, and correctly." She handed the signed document to her apprentice. "Now it can be built."

---
