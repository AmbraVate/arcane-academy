---
id: de-app-m2-03
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m2
moduleTitle: "Module 2: Relational Database Foundations"
moduleGlyph: "🗄️"
moduleSortOrder: 2
topicSlug: tables
topicTitle: "Tables"
topicSortOrder: 1
lesson: columns_and_attributes
title: "Columns and Attributes"
sortOrder: 3
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m2-01, de-app-m2-02]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines a column in a relational database context
    - Explains the relationship between a column and a data type
    - Describes at least three column constraints and their purpose
    - Distinguishes between nullable and non-nullable columns
    - Reflects on how column design decisions affect long-term data quality
  keywords: [column, attribute, data type, constraint, NOT NULL, DEFAULT, UNIQUE, CHECK, nullable, domain]
  modelAnswer: |
    A column in a relational database represents one attribute shared by all rows in the table. Each column has a name, a data type (defining what values are allowed), and optional constraints (rules that further restrict acceptable values). NOT NULL prevents missing values, UNIQUE prevents duplicates, DEFAULT provides a fallback value, and CHECK enforces custom rules. Getting column design right at the start is critical — changing column types or adding constraints to large tables later is expensive and disruptive.
guidedSteps:
  - id: de-app-m2-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which column constraint ensures that every row must have a value for that column?
    inputConfig:
      options:
        - "UNIQUE"
        - "DEFAULT"
        - "NOT NULL"
        - "CHECK"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["NOT NULL"]
      rejectedFeedback: "NOT NULL prevents a column from being left empty when a row is inserted or updated. Without NOT NULL, a column can hold NULL (the absence of a value), which can cause unexpected results in queries."
    hint: "This constraint makes a column mandatory — every row must supply a value."
    reflectionPrompt: "What are the consequences of allowing NULL values in a column that should always have a value, like a customer's email address?"
  - id: de-app-m2-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence: "A ________ constraint allows you to specify a custom rule that all values in a column must satisfy — for example, ensuring a price column only accepts positive numbers."
    inputConfig:
      placeholder: "CHECK"
    markingRule:
      matchMode: CONTAINS
      accepted: [CHECK, check]
      rejectedFeedback: "A CHECK constraint defines a Boolean condition that every row's value in that column must satisfy. For example: CHECK (price > 0) prevents negative prices from being stored."
    hint: "This constraint verifies that values meet a specific condition you define."
    reflectionPrompt: "What CHECK constraint would you add to an age column to prevent obviously invalid values?"
  - id: de-app-m2-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why choosing the correct data type for a column matters, using a concrete example of what goes wrong when the wrong type is chosen.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [type, integer, text, date, sort, calculate, validate, storage, error]
      rejectedFeedback: "Consider: storing prices as TEXT prevents mathematical operations (SUM, AVG). Storing dates as VARCHAR prevents date arithmetic and correct sorting. Choosing the right type enforces correctness automatically and enables the right operations."
    hint: "Think about what mathematical or sorting operations you cannot perform on text that you can on numbers or dates."
    reflectionPrompt: "Have you ever seen a system where phone numbers were stored as integers? What problems does that cause?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does a DEFAULT constraint do?"
    options:
      - "Prevents any value from being inserted"
      - "Provides a value automatically when no value is specified during INSERT"
      - "Ensures the column value is unique across all rows"
      - "Validates that the value matches a pattern"
    correctIndex: 1
    feedback: "A DEFAULT constraint specifies a value that the database uses automatically when an INSERT statement does not provide a value for that column — for example, DEFAULT 'active' for a status column."
  - type: MULTIPLE_CHOICE
    question: "Which data type is most appropriate for storing a product's price?"
    options: ["VARCHAR(20)", "BOOLEAN", "DECIMAL(10,2)", "INT"]
    correctIndex: 2
    feedback: "DECIMAL(10,2) is designed for exact monetary values — it stores up to 10 digits total with exactly 2 decimal places. INT loses the decimal portion; VARCHAR prevents arithmetic; BOOLEAN is for true/false values."
retrieval:
  recall: "In one sentence, explain what a column represents in a relational database table."
  explain: "Explain three column constraints (NOT NULL, UNIQUE, CHECK) and give a real example of when you would use each."
  mistakeId:
    code: "storing phone numbers as INT"
    answer: "Phone numbers should be stored as VARCHAR, not INT. Phone numbers may start with 0 (which INT drops), may contain + symbols or spaces, and are never used in arithmetic. Using INT also limits international numbers that exceed INT range."
---

# Hook

Every table column is a promise: every row will contain a value of this type, within these constraints, meeting these rules. Column design is where abstract attributes from the data model become concrete, typed, and constrained fields in a real database.

Getting column design right has a disproportionate impact on data quality. A column defined without a NOT NULL constraint silently accepts missing data. A price stored as text cannot be summed. A date stored as a string cannot be sorted correctly. These mistakes are subtle, accumulate quietly, and become expensive to fix once data has been loaded.

What types of data have you seen stored in the wrong format? Think about any spreadsheet you have worked with where data was inconsistently typed.

# Lore Introduction

"Every column in a ledger is a contract," Master Selvaris said, pointing to the headings across the top of a fresh page. "Name. Guild. City. Licence Number. Each heading says: every record on this page will supply this piece of information, in this format." She dipped her quill. "If I write the licence number as words in one entry and numerals in another, then try to sort the ledger by licence number — chaos. If I leave the guild column blank for half the merchants — incompleteness. The contract of the column must be upheld by every row." She set the quill down. "That is what constraints are for."

# Core Learning

## Concept Introduction

### Common SQL Data Types

| Category | Type | Use Case | Example |
|----------|------|----------|---------|
| **Integer** | INT, BIGINT, SMALLINT | Whole numbers, IDs, counts | `customer_id INT`, `stock_qty INT` |
| **Decimal** | DECIMAL(p,s), NUMERIC | Exact monetary values | `price DECIMAL(10,2)` |
| **Float** | FLOAT, REAL, DOUBLE | Approximate scientific values | `temperature FLOAT` |
| **Text** | VARCHAR(n), CHAR(n), TEXT | Variable-length strings | `email VARCHAR(255)` |
| **Boolean** | BOOLEAN | True/false flags | `is_active BOOLEAN` |
| **Date/Time** | DATE, TIME, TIMESTAMP | Calendar and time values | `created_at TIMESTAMP` |
| **UUID** | UUID | Globally unique identifiers | `session_id UUID` |

### Column Constraints

| Constraint | Purpose | Example |
|-----------|---------|---------|
| `NOT NULL` | Column must always have a value | `email VARCHAR(255) NOT NULL` |
| `NULL` (default) | Column may be left empty | `middle_name VARCHAR(50)` |
| `UNIQUE` | No two rows may have the same value | `email VARCHAR(255) UNIQUE` |
| `DEFAULT value` | Auto-fill value when INSERT omits column | `status VARCHAR(20) DEFAULT 'active'` |
| `CHECK (condition)` | Value must satisfy a Boolean expression | `price DECIMAL(10,2) CHECK (price > 0)` |
| `PRIMARY KEY` | Unique + NOT NULL; uniquely identifies the row | `customer_id INT PRIMARY KEY` |
| `FOREIGN KEY` | References a primary key in another table | `category_id INT REFERENCES categories(id)` |

## Why It Matters

Column design decisions are among the hardest to reverse after a database is in production. Consider:

- Changing a VARCHAR(50) to VARCHAR(200) requires an ALTER TABLE that may lock the entire table on large datasets
- Adding a NOT NULL constraint to a column that already contains NULLs requires cleaning the data first
- Changing a column from INT to DECIMAL requires a data migration

This is why time invested in column design upfront is time saved across the life of the system.

## Worked Examples

**Example 1: Well-Designed Products Table**
```sql
CREATE TABLE products (
    product_id    BIGINT          PRIMARY KEY,
    product_name  VARCHAR(200)    NOT NULL,
    sku           VARCHAR(50)     NOT NULL UNIQUE,
    unit_price    DECIMAL(10,2)   NOT NULL CHECK (unit_price >= 0),
    stock_qty     INT             NOT NULL DEFAULT 0 CHECK (stock_qty >= 0),
    weight_kg     DECIMAL(8,3),   -- nullable: not all products have a weight
    is_active     BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    category_id   INT             NOT NULL REFERENCES categories(category_id)
);
```

**Example 2: Common Typing Mistakes and Fixes**

| Mistake | Problem | Fix |
|---------|---------|-----|
| `phone VARCHAR(20)` stored as `INT` | Drops leading zeros, excludes + prefix | `VARCHAR(20)` |
| `price VARCHAR(10)` | Cannot SUM or AVG; sorts alphabetically | `DECIMAL(10,2)` |
| `created_at VARCHAR(20)` | Cannot sort by date; no date arithmetic | `TIMESTAMP` or `DATE` |
| `is_deleted INT` (0/1) | Semantics unclear; accepts values other than 0/1 | `BOOLEAN` |

## Common Mistakes

- **VARCHAR without length**: Some databases require a length for VARCHAR; leaving it open may cause unexpected truncation or errors depending on the database engine.
- **Using FLOAT for money**: Floating-point arithmetic introduces rounding errors. `0.1 + 0.2 != 0.3` in floating-point math. Always use DECIMAL for monetary values.
- **Not using DEFAULT**: A column without a default and without NOT NULL silently accepts NULLs when a default would be more appropriate (e.g., `created_at` should default to `CURRENT_TIMESTAMP`).
- **Over-constraining too early**: Adding overly strict CHECK constraints before requirements are clear can prevent legitimate data from being inserted. Validate requirements thoroughly before adding CHECK constraints.

## Mental Model

Think of each column as a typed envelope. The envelope has a label (column name), a size limit (data type and length), and a set of rules about what it may contain (constraints). Before you file an envelope, you must ensure its contents match all these rules. The database is the filing clerk — it checks every envelope before accepting it. If the content does not fit the envelope, the filing clerk rejects it.

## Mini Summary

- ✔ Columns represent typed, constrained attributes shared by all rows in a table
- ✔ Data types enforce correct storage format (INT, DECIMAL, VARCHAR, DATE, etc.)
- ✔ NOT NULL, UNIQUE, DEFAULT, and CHECK are key column-level constraints
- ✔ Wrong data types prevent correct querying, sorting, and calculation
- ✔ Column design decisions are expensive to reverse — invest time upfront

# Guided Practice Quest

Work through the guided steps to select appropriate data types and constraints for given scenarios, and to explain the consequences of common column design mistakes.

# Solo Practice Quest

You are designing a `flight_bookings` table for an airline. Define at least 10 columns for this table. For each column: specify the name, data type, constraints, and a one-sentence justification for each choice. Include at least one example each of: a column that uses CHECK, a column with a DEFAULT, a column that must be UNIQUE, a nullable column (and explain why it is nullable), and a column where the wrong data type choice would cause a specific problem. Present your answer as a SQL CREATE TABLE statement with comments.

# Integration

**Mathematics**: Data types in databases are formally defined as domains — the set of all valid values for a column. An INT column has domain ℤ (integers); a BOOLEAN column has domain {TRUE, FALSE}; a DATE column has domain {all valid calendar dates}. A CHECK constraint further restricts the domain: `CHECK (age >= 18)` restricts the INT domain to {18, 19, 20, ...}. This is equivalent to defining a subset of a mathematical set, making column constraints a direct application of set theory to data engineering.

**Sciences (Physics)**: The concept of measurement units in physics directly maps onto data type design. A physicist recording temperature must specify whether the value is in Celsius, Fahrenheit, or Kelvin — the number 100 means very different things depending on the unit. In databases, column names and types should always convey the unit of measurement: `weight_kg DECIMAL(8,3)` is unambiguous; `weight DECIMAL(8,3)` is not. Adding the unit to column names is a form of dimensional analysis applied to database design.

# Lore Conclusion

Master Selvaris reviewed the completed column definitions in the new ledger design. "Each column is now a commitment," she said, satisfied. "The city must always be named. The price must always be a number greater than zero. The licence number must be unique across all merchants." She traced her finger across the headings. "We have defined not just what we will record, but the quality of every recording we will ever make." She closed the ledger design and handed it back. "A well-designed column carries a thousand well-formed records effortlessly. A poorly-designed one will fight you forever."

---
