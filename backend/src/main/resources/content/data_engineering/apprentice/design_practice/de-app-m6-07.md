---
id: de-app-m6-07
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m6
moduleTitle: "Module 6: Database Design Foundations"
moduleGlyph: "📐"
moduleSortOrder: 6
topicSlug: design_practice
topicTitle: "Design Practice"
topicSortOrder: 2
lesson: designing_library_database
title: "Designing a Library Database"
sortOrder: 7
difficulty: 3
estimatedMinutes: 35
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m6-06]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Identifies the main entities in a library system
    - Designs correct relationships (one-to-many, many-to-many) between entities
    - Applies appropriate primary and foreign keys to each table
    - Ensures the schema is in Third Normal Form (no redundancy)
    - Reflects on design decisions made and tradeoffs considered
  keywords: [entity, relationship, primary key, foreign key, normalise, 3NF, books, members, loans, many-to-many, junction table]
  modelAnswer: |
    A library database requires tables for: books (book_id, title, isbn, publication_year), authors (author_id, name), book_authors (book_id FK, author_id FK — junction for many-to-many), genres (genre_id, name), books also referencing genre_id, members (member_id, name, email, membership_expiry), copies (copy_id, book_id FK, condition, location), and loans (loan_id, copy_id FK, member_id FK, loan_date, due_date, return_date). Books and authors are many-to-many (requiring a junction table). Books to copies is one-to-many. Members to loans is one-to-many. The schema is in 3NF — no attribute depends on a non-key column.
guidedSteps:
  - id: de-app-m6-07-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A book can have multiple authors, and an author can write multiple books. What is the correct relationship type and schema approach?
    inputConfig:
      options:
        - "One-to-many: add author_id to the books table"
        - "One-to-many: add book_id to the authors table"
        - "Many-to-many: create a book_authors junction table with (book_id, author_id)"
        - "Many-to-many: store a comma-separated list of author IDs in the books table"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Many-to-many: create a book_authors junction table with (book_id, author_id)"]
      rejectedFeedback: "Books to authors is a many-to-many relationship — one book can have multiple authors, one author can write multiple books. Many-to-many requires a junction table: book_authors(book_id FK, author_id FK). Options A and B implement a one-to-many, which would only allow one author per book or one book per author. Option D violates 1NF (comma-separated values are not atomic)."
    hint: "When both sides of a relationship can have 'many', a junction table is required."
    reflectionPrompt: "What composite primary key would you use for the book_authors junction table, and why?"
  - id: de-app-m6-07-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A library has 3 copies of "Dune". Each copy can be loaned out independently. The copies table should have a ________ column linking each copy to the book it belongs to.
    inputConfig:
      placeholder: "book_id"
    markingRule:
      matchMode: CONTAINS
      accepted: [book_id, "book_id (FK)", "book_id FK"]
      rejectedFeedback: "The copies table has a book_id foreign key that links each physical copy to its book record. This is a one-to-many relationship: one book (in the books table) to many physical copies (in the copies table). Loans then reference a specific copy_id — not book_id — so the system can track which physical copy is checked out."
    hint: "Each copy belongs to one book — a foreign key in copies pointing to the books table."
    reflectionPrompt: "Why should loans reference copy_id rather than book_id? What real-world scenario requires this?"
  - id: de-app-m6-07-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why a return_date column in the loans table should allow NULL values.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [NULL, not returned, current, outstanding, active, loan, returned, yet]
      rejectedFeedback: "A NULL return_date means the book has not been returned yet — the loan is still active. Setting return_date to NULL for outstanding loans (rather than a placeholder date) is semantically correct: NULL means 'unknown' or 'not yet applicable'. This allows a simple WHERE return_date IS NULL query to find all currently outstanding loans. A placeholder value like '9999-12-31' would work but is an anti-pattern."
    hint: "What does it mean for return_date to be NULL — what is the real-world status of that loan?"
    reflectionPrompt: "How would you write a query to find all members with overdue loans (loan is outstanding and due_date < today)?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why does a library database need a separate copies table rather than just a books table?"
    options:
      - "Books are too complex to store in one table"
      - "A library may have multiple physical copies of the same book, and each copy can be loaned independently"
      - "Copies need a different data type than books"
      - "The copies table is required by SQL standards"
    correctIndex: 1
    feedback: "A single books table can't distinguish between three physical copies of the same title — which copy is on the shelf, which is checked out, which is damaged. The copies table gives each physical copy a unique copy_id, with a book_id FK linking it to the book record. Loans then reference copy_id to track which exact copy is checked out to which member."
  - type: MULTIPLE_CHOICE
    question: "Which tables would you need to answer: 'which members have borrowed this specific book title in the last year?'"
    options:
      - "Only the loans table"
      - "books and loans"
      - "books, copies, loans, and members"
      - "books and members"
    correctIndex: 2
    feedback: "The query chain is: books (find by title) → copies (physical copies of that book) → loans (which copy was loaned, when) → members (who borrowed it). You need all four tables joined together. You cannot go directly from books to loans — loans reference copies, not books. This is why the copies table exists in the schema."
retrieval:
  recall: "Write the complete schema (table names, columns, PKs, FKs) for a library database supporting books, authors, copies, members, and loans."
  explain: "Explain the design decision to separate copies from books — what problem does it solve and what queries does it enable?"
  mistakeId:
    code: "storing author_name directly in the books table to avoid a join"
    answer: "Storing author_name in books means a book with multiple authors can't be represented (1NF violation for multiple authors, or data loss for single-author assumption). It also creates redundancy: if an author's name changes or is corrected, every book row must be updated. The correct design is authors(author_id, name) and book_authors(book_id, author_id) — a many-to-many junction table. This normalises author data and supports any number of authors per book."
---

# Hook

Understanding normalisation rules is one thing. Applying them to a real-world design from scratch is another. A library is a familiar domain with clear entities, relationships, and business questions — it is an ideal practice ground for applying everything you have learned about tables, keys, relationships, and normal forms.

This lesson walks through the complete design of a library database, decision by decision.

# Lore Introduction

"The Grand Archive is to be catalogued," the Chief Librarian said. "Thousands of scrolls, hundreds of authors, multiple copies of many titles, and a loan system for guild members." Master Selvaris opened her design notebook. "We start with entities," she said. "What things does this system need to track?" She listed: scrolls (books), authors, physical copies, members, and loans. "Then relationships. Then keys. Then we check for normalisation problems." She drew the first table. "Design is systematic. Start with what exists, then ask: how do things relate? What can be many? What must be unique? One question at a time."

# Core Learning

## Concept Introduction

### Step 1: Identify Entities

A library system needs to track:
- **Books** — the titles (not physical copies)
- **Authors** — who wrote each book
- **Copies** — physical instances of a book
- **Members** — library card holders
- **Loans** — a member borrowing a specific copy
- **Genres** — optional categorisation

### Step 2: Identify Relationships

```
Books ↔ Authors:   Many-to-many (book can have many authors; author writes many books)
Books → Genres:    Many-to-one (book has one genre; genre has many books)
Books → Copies:    One-to-many (one title, many physical copies)
Members → Loans:   One-to-many (one member, many loans)
Copies → Loans:    One-to-many (one copy, many loans over time)
```

### Step 3: Design the Tables

```sql
-- Genres (simple lookup table)
CREATE TABLE genres (
    genre_id   SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL UNIQUE
);

-- Books (one record per title, not per physical copy)
CREATE TABLE books (
    book_id          SERIAL PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    isbn             VARCHAR(13) UNIQUE,
    publication_year INTEGER,
    genre_id         INTEGER REFERENCES genres(genre_id)
);

-- Authors
CREATE TABLE authors (
    author_id  SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL
);

-- Book-Author junction (many-to-many)
CREATE TABLE book_authors (
    book_id    INTEGER REFERENCES books(book_id),
    author_id  INTEGER REFERENCES authors(author_id),
    PRIMARY KEY (book_id, author_id)
);

-- Physical copies (each copy of a title is a separate row)
CREATE TABLE copies (
    copy_id    SERIAL PRIMARY KEY,
    book_id    INTEGER NOT NULL REFERENCES books(book_id),
    condition  VARCHAR(20) DEFAULT 'good',  -- good, worn, damaged
    location   VARCHAR(50)                  -- shelf reference
);

-- Members
CREATE TABLE members (
    member_id         SERIAL PRIMARY KEY,
    first_name        VARCHAR(100) NOT NULL,
    last_name         VARCHAR(100) NOT NULL,
    email             VARCHAR(255) UNIQUE NOT NULL,
    membership_expiry DATE
);

-- Loans (links a member to a specific physical copy)
CREATE TABLE loans (
    loan_id     SERIAL PRIMARY KEY,
    copy_id     INTEGER NOT NULL REFERENCES copies(copy_id),
    member_id   INTEGER NOT NULL REFERENCES members(member_id),
    loan_date   DATE NOT NULL DEFAULT CURRENT_DATE,
    due_date    DATE NOT NULL,
    return_date DATE    -- NULL means not yet returned
);
```

### Step 4: Verify Normalisation

```
1NF: All values are atomic. No repeating groups (authors in a separate table, not comma-separated). ✓
2NF: All non-key attributes depend on the full PK. (Simple PKs, so 2NF trivially satisfied.) ✓
3NF: No transitive dependencies. genre_name is in genres (not books). author_name is in authors (not books). ✓
```

### Sample Queries the Schema Supports

```sql
-- All copies of "Dune" currently on loan
SELECT m.first_name, m.last_name, l.due_date
FROM books b
JOIN copies c ON b.book_id = c.book_id
JOIN loans l ON c.copy_id = l.copy_id
JOIN members m ON l.member_id = m.member_id
WHERE b.title = 'Dune'
  AND l.return_date IS NULL;

-- Members with overdue loans
SELECT m.email, b.title, l.due_date
FROM loans l
JOIN members m ON l.member_id = m.member_id
JOIN copies c ON l.copy_id = c.copy_id
JOIN books b ON c.book_id = b.book_id
WHERE l.return_date IS NULL
  AND l.due_date < CURRENT_DATE;

-- Most borrowed books (all time)
SELECT b.title, COUNT(l.loan_id) AS times_borrowed
FROM books b
JOIN copies c ON b.book_id = c.book_id
LEFT JOIN loans l ON c.copy_id = l.copy_id
GROUP BY b.book_id, b.title
ORDER BY times_borrowed DESC
LIMIT 10;
```

### Design Decisions Worth Noting

| Decision | Reasoning |
|---|---|
| Separate `copies` from `books` | One title can have many physical copies; loans track specific copies |
| `return_date` nullable | NULL = currently on loan; avoids placeholder magic dates |
| `book_authors` junction | Many-to-many between books and authors |
| `isbn` unique but nullable | ISBNs are globally unique identifiers, but older books may not have them |
| `membership_expiry` in members | Simple date field — the application checks if it's in the future |

## Common Mistakes

- **Storing authors as a comma-separated string in books**: Violates 1NF. Use a junction table.
- **Loans referencing book_id instead of copy_id**: You cannot track which physical copy is checked out or have multiple copies loaned simultaneously.
- **Using a default future date for "not returned"**: Use NULL for "not yet returned". NULL semantics correctly express "not applicable yet".
- **Forgetting indexes on FK columns**: In production, add indexes on foreign keys (loans.copy_id, loans.member_id) for fast join performance.

## Mental Model

Library database design illustrates the whole design process: start with real-world entities (the nouns — books, members, copies), identify relationships (the verbs — authors write books, members borrow copies), choose keys (what uniquely identifies each entity), add foreign keys to represent relationships, then verify normalisation (one fact in one place). The order matters: entities first, relationships second, keys third, normalisation last.

## Mini Summary

- ✔ Entities: books, authors, copies, members, loans, genres
- ✔ Many-to-many (books ↔ authors) requires a junction table: book_authors
- ✔ Copies table separates physical instances from title records
- ✔ Loans reference copy_id (not book_id) to track specific physical copies
- ✔ NULL return_date means the loan is outstanding — semantically correct

# Guided Practice Quest

Work through the guided steps to identify the entities and relationships in a library system, design the copies and loans tables with correct keys, and write the SQL query for finding overdue loans.

# Solo Practice Quest

Extend the library database design to support the following requirements: (1) a book can belong to multiple genres (change to many-to-many), (2) library has multiple branches — members and copies belong to a branch, (3) members can place reservations for books that are currently all on loan, (4) fines are charged for overdue returns at a rate of £0.10 per day. For each new requirement: describe the schema change, add the necessary tables or columns, update the primary and foreign keys, and confirm the updated schema remains in 3NF. Then write one SQL query that demonstrates each new feature.

# Integration

**Mathematics**: The library schema illustrates the correspondence between an entity-relationship (ER) model and a set of normalised relations. The ER model — a directed graph of entities and relationships — maps to tables via a standard algorithm: each entity becomes a table (with PK), each many-to-many relationship becomes a junction table, each one-to-many relationship becomes a FK in the "many" side. The result is provably in BCNF (Boyce-Codd Normal Form) if each ER relationship is a genuine functional dependency. This formal correspondence between graph models and relational schemas is the foundation of database design theory — the ER-to-relational mapping is a graph homomorphism from the conceptual model to the implementation model.

**Sciences (Information Science — Library Systems)**: The schema designed in this lesson closely mirrors the MARC (Machine-Readable Cataloging) standard used by libraries worldwide since 1966 — the data model underlying every library management system from small public libraries to the British Library. MARC separates bibliographic records (titles, authors, subjects — equivalent to books and book_authors) from item records (physical copies — equivalent to copies) and patron records (members). The loan record (linking patron to item) is the same transactional entity as the loans table. Real library systems like Koha (open source) and Ex Libris implement exactly this design.

# Lore Conclusion

"The Grand Archive schema is complete," the Chief Librarian said, reviewing the seven tables. "Books separate from copies. Authors in their own table, linked by a junction. Loans tracking specific copies, not titles." Master Selvaris ran three queries: all available copies of a specific title, all overdue loans for a specific member, the ten most borrowed texts of the year. All returned results instantly. "Every query we need, one schema can answer," the Librarian said. Selvaris nodded. "That is the goal of good design. Not just to store data — but to store it in a way that makes every reasonable question answerable with a clean query. Design is the foundation. The queries are only as good as the schema they run against."

---
