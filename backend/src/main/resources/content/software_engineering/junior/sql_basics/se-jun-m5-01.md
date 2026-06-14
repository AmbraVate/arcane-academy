---
id: se-jun-m5-01
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m5
moduleTitle: "Module 5: Databases"
moduleGlyph: "🗄️"
moduleSortOrder: 5
topicSlug: sql_basics
topicTitle: "SQL Basics"
topicSortOrder: 1
lesson: sql_basics
title: "SQL Basics"
sortOrder: 1
difficulty: 2
estimatedMinutes: 30
xpReward: 70
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m4-05]
integrationDomains: [joins, orms]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes a SELECT query with WHERE, ORDER BY, and LIMIT clauses"
    - "Writes an INSERT INTO statement with correct column and value lists"
    - "Writes an UPDATE with a WHERE clause (never updates all rows unintentionally)"
    - "Writes a DELETE with a WHERE clause"
    - "Identifies the purpose of PRIMARY KEY and common data types (VARCHAR, INT, BOOLEAN, TIMESTAMP)"
  keywords: [SELECT, FROM, WHERE, ORDER BY, LIMIT, INSERT INTO, UPDATE, SET, DELETE, PRIMARY KEY, VARCHAR, INT, BOOLEAN, TIMESTAMP, column, table, row]
  modelAnswer: |
    -- Create table
    CREATE TABLE spells (
        id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
        name VARCHAR(100) NOT NULL,
        power INT NOT NULL,
        school VARCHAR(50),
        active BOOLEAN DEFAULT true,
        created_at TIMESTAMP DEFAULT now()
    );

    -- SELECT with filters
    SELECT id, name, power FROM spells
    WHERE school = 'fire' AND power > 50
    ORDER BY power DESC
    LIMIT 10;

    -- INSERT
    INSERT INTO spells (name, power, school) VALUES ('Fireball', 80, 'fire');

    -- UPDATE (always include WHERE!)
    UPDATE spells SET power = 90 WHERE id = 1;

    -- DELETE (always include WHERE!)
    DELETE FROM spells WHERE id = 1;
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: REFLECTION
    instruction: "Write SQL queries for the wizards table (columns: id, name, school, level, active): (a) List all wizards in school 'fire' with level > 5, ordered by level descending, limit 20. (b) Insert a new wizard named 'Aldric' in school 'frost' at level 3. (c) Update Aldric's level to 4 (by id). (d) Deactivate all wizards in school 'shadow' by setting active = false."
    inputConfig:
      language: java
      starterCode: |
        -- (a) SELECT with WHERE, ORDER BY, LIMIT
        SELECT ...

        -- (b) INSERT a new wizard
        INSERT INTO ...

        -- (c) UPDATE level by id
        UPDATE wizards SET ...

        -- (d) Deactivate all shadow wizards
        UPDATE wizards SET ...
    markingRule: "SELECT uses WHERE with AND condition, ORDER BY level DESC, LIMIT 20; INSERT includes column names and matching values; UPDATE uses WHERE id = value; mass UPDATE includes WHERE clause to scope to shadow school"
    hint: "Always include a WHERE clause on UPDATE and DELETE. Without it, every row is affected."
    reflectionPrompt: "What happens if you run UPDATE wizards SET level = 10 with no WHERE clause? How do you protect against this in a production database?"
  - id: step-2
    sortOrder: 2
    inputType: REFLECTION
    instruction: "Design a CREATE TABLE statement for a 'courses' table in an academy system. Include: an auto-generated id, a name (required, max 200 chars), a description (optional, long text), a max_capacity (required, default 30), a start_date, and an is_active boolean (default true). Choose appropriate data types."
    inputConfig:
      language: java
      starterCode: |
        -- Create the courses table:
        CREATE TABLE courses (
            -- id: auto-generated primary key
            -- name: required string max 200
            -- description: optional long text
            -- max_capacity: int default 30
            -- start_date: date
            -- is_active: boolean default true
        );
    markingRule: "id uses BIGINT or SERIAL PRIMARY KEY with auto-generation, name VARCHAR(200) NOT NULL, description TEXT (nullable), max_capacity INT NOT NULL DEFAULT 30, start_date DATE, is_active BOOLEAN DEFAULT true"
    hint: "GENERATED ALWAYS AS IDENTITY or SERIAL for auto-increment. TEXT for long optional strings. NOT NULL for required fields. DEFAULT for default values."
    reflectionPrompt: "Why do we use VARCHAR(200) for name but TEXT for description? When would TEXT be inappropriate?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which SQL clause filters rows returned by a SELECT query?"
    options:
      - "ORDER BY — sorts and limits results"
      - "GROUP BY — groups rows for aggregation"
      - "WHERE — filters rows based on a condition"
      - "HAVING — applies conditions after grouping"
    correctIndex: 2
    feedback: "WHERE filters rows before any grouping or aggregation. It applies to individual rows: `WHERE school = 'fire'` returns only rows where the school column equals 'fire'. ORDER BY sorts the result. GROUP BY groups rows for aggregate functions. HAVING filters groups after GROUP BY."
  - type: MULTIPLE_CHOICE
    question: "What is the purpose of PRIMARY KEY on a column?"
    options:
      - "It stores the most important data in the row"
      - "It uniquely identifies each row and cannot be null; used for joins and lookups"
      - "It sorts the table by that column automatically"
      - "It creates an index that speeds up ORDER BY on that column"
    correctIndex: 1
    feedback: "A PRIMARY KEY constraint ensures every row has a unique, non-null identifier. It automatically creates an index on the column, making lookups by id (SELECT WHERE id = ?) extremely fast. Primary keys are the foundation of joins — foreign keys in other tables reference the primary key of this table."
retrieval:
  recall: "Write the SQL template for SELECT, INSERT, UPDATE, and DELETE (with placeholder column names and a WHERE clause on each write operation)."
  explain: "Explain what happens if you run DELETE FROM spells with no WHERE clause. How do transactions protect against this mistake?"
  mistakeId:
    code: |
      -- Trying to update Aldric's level:
      UPDATE wizards SET level = 10;
      -- Missing WHERE clause!
    answer: "Without WHERE, this updates the level of EVERY row in the wizards table to 10 — a catastrophic accidental mass update. Always include WHERE on UPDATE and DELETE: `UPDATE wizards SET level = 10 WHERE name = 'Aldric'` or better `WHERE id = 42`. In production, use transactions so you can roll back before committing."
---

# Hook

Every web application that stores data uses a database. Every Spring Boot service you write will need to read and write records. SQL is the language of relational databases — the standard that works across PostgreSQL, MySQL, Oracle, and SQLite. The five core operations — SELECT, INSERT, UPDATE, DELETE, and CREATE TABLE — are what you need to store and retrieve the data your application works with. Master these and you can communicate with any relational database ever built.

# Lore Introduction

The Academy's spell registry was originally stored in handwritten scrolls. Retrieving a specific spell required unrolling hundreds of scrolls. Adding a new spell meant rewriting the entire index. The head archivist introduced a relational database: structured tables with named columns, rows for each record, and a query language that could find any spell in milliseconds. The first `SELECT name FROM spells WHERE school = 'fire' ORDER BY power DESC` returned in under a millisecond. The archivist who had spent days on the same query never went back to scrolls.

# Core Learning

## Concept Introduction

**What is a relational database?**
Data is stored in tables. Each table has named columns with defined types. Each row is a record. Tables can reference each other via foreign keys (covered in Joins and Relationships).

**Common SQL data types:**
| SQL Type | Java Type | Notes |
|---|---|---|
| `BIGINT` | `Long` | 64-bit integer; use for ids |
| `INT` / `INTEGER` | `int` | 32-bit integer |
| `VARCHAR(n)` | `String` | String with max length n |
| `TEXT` | `String` | Unlimited-length string |
| `BOOLEAN` | `boolean` | true/false |
| `DECIMAL(p,s)` | `BigDecimal` | Precise decimal; use for money |
| `TIMESTAMP` | `LocalDateTime` | Date and time |
| `DATE` | `LocalDate` | Date only |

**SELECT — retrieve rows:**
```sql
SELECT column1, column2
FROM table_name
WHERE condition
ORDER BY column ASC|DESC
LIMIT n;

-- Select all columns:
SELECT * FROM spells WHERE school = 'fire';
```

**INSERT — add a new row:**
```sql
INSERT INTO table_name (col1, col2, col3)
VALUES (val1, val2, val3);
```

**UPDATE — modify existing rows:**
```sql
UPDATE table_name
SET col1 = val1, col2 = val2
WHERE condition; -- ALWAYS include WHERE!
```

**DELETE — remove rows:**
```sql
DELETE FROM table_name
WHERE condition; -- ALWAYS include WHERE!
```

**CREATE TABLE — define a new table:**
```sql
CREATE TABLE spells (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(100) NOT NULL,
    power INT NOT NULL DEFAULT 0,
    school VARCHAR(50),
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT now()
);
```

## Why It Matters

SQL is the interface between your Java application and persistent storage. When you call `spellRepository.findBySchool("fire")` in Spring Data JPA, it translates to a SELECT query. When you call `repository.save(spell)`, it becomes an INSERT or UPDATE. Understanding SQL means you can read the queries your ORM generates, diagnose performance issues, write custom queries when the ORM falls short, and reason about what your code is doing at the data layer. Every backend developer needs SQL.

## Worked Examples

**Example 1 — SELECT with multiple conditions**

```sql
-- Find active fire spells above power 50, most powerful first, top 10
SELECT id, name, power, mana_cost
FROM spells
WHERE school = 'fire'
  AND power > 50
  AND active = true
ORDER BY power DESC
LIMIT 10;
```

**Example 2 — INSERT, UPDATE, DELETE**

```sql
-- Add a new spell
INSERT INTO spells (name, power, school, mana_cost)
VALUES ('Frost Nova', 65, 'frost', 40);

-- Update the spell's power by id
UPDATE spells
SET power = 70, mana_cost = 45
WHERE id = 42;

-- Soft delete: mark inactive instead of removing
UPDATE spells SET active = false WHERE id = 42;

-- Hard delete: remove the row
DELETE FROM spells WHERE id = 42;
```

**Example 3 — CREATE TABLE with constraints**

```sql
CREATE TABLE wizards (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    school VARCHAR(50) NOT NULL,
    level INT NOT NULL DEFAULT 1,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
```

## Common Mistakes

- **UPDATE or DELETE without WHERE.** Without a WHERE clause, every row in the table is affected. Always double-check your WHERE clause before running write operations in production.
- **Using `SELECT *` in production queries.** Selecting all columns retrieves data you do not need, wastes network bandwidth, and breaks when columns are added or removed. Always name the columns you need.
- **Forgetting NOT NULL for required fields.** Without NOT NULL, a column allows null values even when the application logic requires a value. Enforce constraints at the database level.
- **Storing money as FLOAT.** Floating-point types are imprecise. Always use `DECIMAL(15, 2)` or `BIGINT` (in minor units) for monetary values.
- **No default value on boolean flags.** `active BOOLEAN` without a default means new rows have null for active — which is neither true nor false. Always add `DEFAULT true` or `DEFAULT false`.

## Mental Model

A SQL database is like a spreadsheet with enforceable rules. The table is the spreadsheet tab. Columns are the headers. Rows are the data. SELECT is filtering and reading rows. INSERT is adding a new row at the bottom. UPDATE is editing cells in existing rows. DELETE is removing rows. CREATE TABLE is defining the headers and their rules (what type, required or optional, default values). The database engine enforces the rules so your application data stays consistent.

## Mini Summary

- Tables hold rows (records) and columns (fields) with defined types.
- SELECT retrieves rows; WHERE filters; ORDER BY sorts; LIMIT caps results.
- INSERT adds a new row; always list column names explicitly.
- UPDATE modifies rows; always include WHERE or every row changes.
- DELETE removes rows; always include WHERE.
- PRIMARY KEY uniquely identifies each row; auto-generated ids are standard.

# Guided Practice Quest

Complete the two steps: write SELECT, INSERT, UPDATE, and DELETE queries for a wizards table with multiple conditions, then design a CREATE TABLE for a courses table with appropriate data types and constraints.

# Solo Practice Quest

Design and write SQL for a "Quest" system. Create a `quests` table (id, title VARCHAR 200, description TEXT, reward_gold INT, difficulty_level INT 1-5, min_level INT, is_active BOOLEAN, created_at TIMESTAMP). Write queries for: (1) list all active quests for wizards at level 3 ordered by reward descending, (2) insert a new quest, (3) deactivate a quest by id, (4) update the reward for all quests with difficulty_level 5, (5) delete all inactive quests created more than 30 days ago (hint: `WHERE created_at < now() - INTERVAL '30 days'`). For each query, add a comment explaining why you wrote it the way you did.

# Integration

SQL is the foundation that every other Database module lesson builds on. In **Joins**, you will extend SELECT with JOIN clauses to query across related tables. In **ORMs**, you will see how JPA annotations map Java classes to the tables you design here — the `@Column` annotation maps to a column definition, `@Id` maps to PRIMARY KEY. In **Transactions**, you will wrap SQL operations in ACID-compliant units. When you use Spring Data JPA's `findAll()`, it runs `SELECT * FROM table`; `findById(id)` runs `SELECT * FROM table WHERE id = ?`. Understanding the SQL your ORM generates makes you a better developer.

**Integration question:** Your application runs `SELECT * FROM spells` in a loop for 1,000 different wizard profiles. This is called the N+1 problem. What SQL would you write instead to fetch all spells for all relevant wizards in one query?

# Lore Conclusion

The Academy's spell registry is now a structured, queryable database. Any authorised system can retrieve the ten most powerful fire spells in a single millisecond query. New spells are inserted with constraints that prevent malformed data. Updates always target specific records by id. The archivist who spent days unrolling scrolls now monitors query performance dashboards instead. SQL did not eliminate the work — it eliminated the inefficiency. The data is still there. It is just organised in a way that machines can serve it at the speed of thought.
