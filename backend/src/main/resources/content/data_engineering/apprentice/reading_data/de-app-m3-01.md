---
id: de-app-m3-01
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m3
moduleTitle: "Module 3: SQL Foundations"
moduleGlyph: "🔍"
moduleSortOrder: 3
topicSlug: reading_data
topicTitle: "Reading Data"
topicSortOrder: 1
lesson: introduction_to_sql
title: "Introduction to SQL"
sortOrder: 1
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m2-13]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what SQL is and what problem it solves
    - Distinguishes between DDL, DML, and DQL with examples of each
    - Explains why SQL is declarative rather than procedural
    - Names at least three major database systems that use SQL
    - Reflects on why a single standard language for data querying is valuable
  keywords: [SQL, declarative, DDL, DML, DQL, query, relational, database, standard, ANSI]
  modelAnswer: |
    SQL (Structured Query Language) is the standard language for communicating with relational databases. It is declarative — you describe what data you want, not how to retrieve it, and the database engine decides the execution plan. SQL has three main sub-languages: DDL (Data Definition Language) for creating and modifying structure (CREATE, ALTER, DROP), DML (Data Manipulation Language) for modifying data (INSERT, UPDATE, DELETE), and DQL (Data Query Language) for reading data (SELECT). Major database systems including PostgreSQL, MySQL, Oracle, and SQL Server all implement SQL with minor dialect variations. SQL's value is that one language works across all relational databases, making data skills highly transferable.
guidedSteps:
  - id: de-app-m3-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      SQL is described as a "declarative" language. What does this mean?
    inputConfig:
      options:
        - "You must declare all variables before using them"
        - "You describe what data you want, not the steps to retrieve it"
        - "You must write SQL in uppercase letters"
        - "SQL only runs on declared database schemas"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["You describe what data you want, not the steps to retrieve it"]
      rejectedFeedback: "Declarative means you specify the desired result, not the procedure to get there. In SQL you write 'give me all customers from London' — the database engine decides how to find them (which indexes to use, what order to scan). This is in contrast to procedural languages where you write the exact steps: loop through all rows, check city, add matching rows to result."
    hint: "Compare to a recipe (procedural) vs ordering food at a restaurant (declarative)."
    reflectionPrompt: "What advantage does a declarative approach give you as a developer — especially as data volumes grow?"
  - id: de-app-m3-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The sub-language of SQL used to read data — containing the SELECT statement — is called ________ (Data Query Language).
    inputConfig:
      placeholder: "DQL"
    markingRule:
      matchMode: CONTAINS
      accepted: [DQL, "Data Query Language", "data query language"]
      rejectedFeedback: "DQL (Data Query Language) covers statements that retrieve data without modifying it — primarily SELECT. DDL (Data Definition Language) covers CREATE, ALTER, DROP — structure changes. DML (Data Manipulation Language) covers INSERT, UPDATE, DELETE — data changes. Some sources fold DQL into DML, but the distinction is useful when learning."
    hint: "The sub-language that contains SELECT — for querying (reading) data."
    reflectionPrompt: "Why is it useful to distinguish between DDL, DML, and DQL when thinking about database permissions?"
  - id: de-app-m3-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why the existence of a standard SQL language across multiple database systems is valuable for data engineers and developers.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [standard, transferable, portable, multiple, systems, different, databases, skills, dialect]
      rejectedFeedback: "SQL is an ANSI/ISO standard, meaning skills learned on PostgreSQL are largely transferable to MySQL, Oracle, SQL Server, and others. Teams can switch database systems without retraining the entire engineering organisation. SQL knowledge is one of the most durable technical skills in the industry precisely because the standard has remained stable for decades while proprietary APIs have changed."
    hint: "Think about what it would mean if every database had a completely different query language."
    reflectionPrompt: "Even though different databases have dialect differences, why is the core SQL standard still a huge advantage?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which SQL sub-language contains CREATE TABLE, ALTER TABLE, and DROP TABLE?"
    options: ["DQL", "DML", "DDL", "DCL"]
    correctIndex: 2
    feedback: "DDL (Data Definition Language) covers commands that define or modify database structure: CREATE, ALTER, DROP, TRUNCATE. DQL covers SELECT (reading data). DML covers INSERT, UPDATE, DELETE (changing data). DCL (Data Control Language) covers GRANT and REVOKE (permissions)."
  - type: MULTIPLE_CHOICE
    question: "Which of the following is NOT a relational database that uses SQL?"
    options: ["PostgreSQL", "MySQL", "MongoDB", "Oracle"]
    correctIndex: 2
    feedback: "MongoDB is a NoSQL document database — it uses a JavaScript-based query API, not SQL. PostgreSQL, MySQL, and Oracle are all relational databases that implement the SQL standard (with their own dialect extensions). SQL Server (Microsoft) and SQLite are two more common SQL databases."
retrieval:
  recall: "What do the letters SQL stand for, and what type of language is it (declarative or procedural)?"
  explain: "Describe the three main SQL sub-languages (DDL, DML, DQL) with one example statement for each."
  mistakeId:
    code: "writing SQL procedurally — manually looping through rows in application code instead of using WHERE"
    answer: "SQL's declarative nature means the database engine optimises retrieval for you. Fetching all rows and filtering in application code bypasses this optimisation, transfers large amounts of unnecessary data over the network, and makes queries far slower. Writing WHERE conditions and letting the database do the work is almost always faster and simpler."
---

# Hook

Every time you log into a web application, book a flight, check your bank balance, or search a product catalogue, a database query runs behind the scenes. In the vast majority of cases, that query is written in SQL.

SQL — Structured Query Language — is the universal language for relational databases. It has been the standard since 1974. It runs on PostgreSQL, MySQL, Oracle, SQL Server, SQLite, and dozens of other systems. Learning SQL is one of the most durable technical investments you can make: the skills transfer across companies, industries, and decades.

This module is where you start speaking that language.

# Lore Introduction

Master Selvaris produced a thick scroll labelled "The Archive Query Protocol." "The Archive of the Eternal Library holds records spanning seven centuries," she said. "Ten million rows across three thousand tables. Without a language for retrieving specific records, one would spend lifetimes searching manually." She unrolled the scroll to reveal the first line: `SELECT * FROM scrolls WHERE era = 'Third Age';`. "This one instruction retrieves every Third Age scroll in the archive — tens of thousands of records — instantly." She set down the scroll. "The language is called SQL. It is the most important tool in a data engineer's arsenal. Not because it is complex — it is remarkably simple. But because it is universal, powerful, and everywhere."

# Core Learning

## Concept Introduction

### What SQL Is

**SQL** (Structured Query Language) is a declarative language for creating, managing, and querying relational databases. "Declarative" means you describe *what* you want, not *how* to find it — the database engine determines the execution plan.

```sql
-- Declarative: "give me all UK customers with more than 5 orders"
-- You don't write the loop — the database decides how to find these records
SELECT c.name, COUNT(o.order_id) AS order_count
FROM customers c
JOIN orders o ON o.customer_id = c.customer_id
WHERE c.country = 'UK'
GROUP BY c.customer_id, c.name
HAVING COUNT(o.order_id) > 5;
```

### The Three SQL Sub-Languages

| Sub-language | Full Name | Purpose | Key Statements |
|---|---|---|---|
| **DDL** | Data Definition Language | Define and modify structure | `CREATE`, `ALTER`, `DROP`, `TRUNCATE` |
| **DML** | Data Manipulation Language | Modify data | `INSERT`, `UPDATE`, `DELETE` |
| **DQL** | Data Query Language | Read data | `SELECT` |

(Some sources also list **DCL** — Data Control Language — for `GRANT`/`REVOKE`, and **TCL** — Transaction Control Language — for `COMMIT`/`ROLLBACK`.)

### SQL in Practice

```sql
-- DDL: create a table (structure)
CREATE TABLE products (
    product_id   INT PRIMARY KEY,
    name         VARCHAR(200) NOT NULL,
    price        DECIMAL(10,2) NOT NULL
);

-- DML: insert data
INSERT INTO products (product_id, name, price) VALUES (1, 'Arcane Tome', 29.99);

-- DQL: read data
SELECT name, price FROM products WHERE price < 50;

-- DML: update data
UPDATE products SET price = 24.99 WHERE product_id = 1;

-- DML: delete data
DELETE FROM products WHERE product_id = 1;

-- DDL: remove the table
DROP TABLE products;
```

### SQL Standards and Dialects

SQL is standardised by ANSI/ISO (current standard: SQL:2023). All major databases implement the core standard but add their own extensions:

| Database | Notes |
|---|---|
| **PostgreSQL** | Most standards-compliant; excellent for learning |
| **MySQL / MariaDB** | Widely used in web applications |
| **Oracle** | Enterprise standard; powerful analytics extensions |
| **SQL Server** | Microsoft ecosystem; T-SQL dialect |
| **SQLite** | Embedded; used in browsers, mobile apps, testing |

Core SQL (SELECT, FROM, WHERE, JOIN, GROUP BY) works identically across all of them. This module uses standard SQL that runs on any system.

## Why It Matters

SQL is not just a database tool — it is a thinking framework. Learning to express data questions as SQL queries develops the ability to decompose complex information needs into precise, executable specifications. This skill transfers to data analysis, backend development, reporting, data science, and system design.

## Common Mistakes

- **Thinking SQL is hard**: The core of SQL (SELECT, FROM, WHERE) takes an hour to learn. The rest builds on that foundation incrementally.
- **Memorising syntax without understanding the model**: SQL makes sense when you understand the relational model (tables, rows, columns, keys). Without that mental model, SQL feels arbitrary.
- **Not using a real database to practice**: SQL is a practical skill. Reading about it is not enough — you must write queries against real data.

## Mental Model

Think of SQL as the language you speak to a very organised librarian. You don't need to know *how* the library is organised internally — which shelf, which floor. You just say: "I need all books published after 1990 by authors from France, sorted by title." The librarian (database engine) handles the search. SQL is that request language, and the database is the librarian who executes it efficiently.

## Mini Summary

- ✔ SQL is the standard language for relational databases — declarative, not procedural
- ✔ DDL defines structure; DML modifies data; DQL reads data
- ✔ SQL runs on PostgreSQL, MySQL, Oracle, SQL Server, SQLite — core syntax is portable
- ✔ You describe what you want; the database engine decides how to retrieve it
- ✔ Learning SQL is a durable, transferable skill across the entire data industry

# Guided Practice Quest

Work through the guided steps to identify SQL sub-languages, explain the declarative nature of SQL, and understand why a standard query language is valuable across different database systems.

# Solo Practice Quest

Research two different database systems (e.g. PostgreSQL and MySQL, or SQLite and Oracle). Write a comparison covering: (1) what they are primarily used for, (2) one SQL feature where their syntax differs from the standard (e.g. auto-increment, date functions, string concatenation), and (3) one scenario where you would choose each over the other. Then write three SQL statements — one DDL, one DML, and one DQL — for a fictional `books` table, and label which sub-language each belongs to. Explain in one sentence what the database engine must do to execute your DQL statement.

# Integration

**Mathematics**: SQL's declarative nature is grounded in relational algebra — a formal mathematical system for manipulating relations (tables) using operations like selection (σ), projection (π), and join (⋈). A SQL SELECT statement is a syntactic sugar over a relational algebra expression: WHERE corresponds to selection (σ), the column list corresponds to projection (π), and JOIN corresponds to join (⋈). The database engine's query optimiser translates the declarative SQL into an efficient relational algebra execution plan, choosing join orders, index paths, and access strategies. Understanding this mathematical foundation helps explain why SQL is both concise and powerful.

**Sciences (Linguistics)**: SQL's design reflects principles from formal linguistics. Like natural language, SQL has a grammar — a precise syntax that must be followed for a statement to be valid. Like natural language, SQL is compositional — complex queries are built from simpler clauses combined according to grammatical rules. Unlike natural language, SQL has no ambiguity — every valid query has exactly one interpretation. This formal precision is what allows database engines to execute queries mechanically without needing to resolve ambiguity. The study of SQL syntax is essentially the study of a formal grammar, a concept central to both linguistics and theoretical computer science.

# Lore Conclusion

Master Selvaris rolled the scroll back up. "SQL is not magic," she said. "It is a language — precise, consistent, and learnable. Every question you can ask about data has a SQL formulation. Every report, every filter, every summary." She set it on the shelf beside hundreds of similar scrolls. "This module will teach you to read data: to select, filter, sort, and summarise. The foundation of every query you will ever write." She turned to the first exercise. "We begin with SELECT. It is the first word of almost every SQL query ever written. Learn it well."

---
