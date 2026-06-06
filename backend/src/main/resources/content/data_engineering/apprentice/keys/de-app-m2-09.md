---
id: de-app-m2-09
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m2
moduleTitle: "Module 2: Relational Database Foundations"
moduleGlyph: "🗄️"
moduleSortOrder: 2
topicSlug: keys
topicTitle: "Keys"
topicSortOrder: 2
lesson: data_integrity
title: "Data Integrity"
sortOrder: 4
difficulty: 2
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m2-08]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines data integrity and explains its four components
    - Correctly identifies the SQL constraint type for each kind of integrity rule
    - Explains the difference between entity integrity, referential integrity, and domain integrity
    - Gives at least two examples of CHECK constraints that enforce domain integrity
    - Reflects on why database-enforced integrity is more reliable than application-enforced integrity
  keywords: [data integrity, entity integrity, referential integrity, domain integrity, NOT NULL, CHECK, UNIQUE, PRIMARY KEY, constraint]
  modelAnswer: |
    Data integrity is the guarantee that data in a database is accurate, consistent, and reliable throughout its lifecycle. Entity integrity is enforced by primary keys (every row is uniquely identifiable and non-null). Referential integrity is enforced by foreign keys (no orphaned references). Domain integrity constrains what values are valid within a column — enforced by data types, NOT NULL, UNIQUE, and CHECK constraints. CHECK constraints express business rules like "price must be positive" or "status must be one of approved, pending, rejected". Database-enforced integrity holds regardless of how data enters; application-enforced integrity can be bypassed.
guidedSteps:
  - id: de-app-m2-09-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A `products` table has a `unit_price` column. The business rule states that prices must be greater than zero. Which constraint enforces this?
    inputConfig:
      options:
        - "PRIMARY KEY"
        - "FOREIGN KEY"
        - "UNIQUE"
        - "CHECK (unit_price > 0)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["CHECK (unit_price > 0)"]
      rejectedFeedback: "A CHECK constraint validates that column values satisfy a specific expression. CHECK (unit_price > 0) evaluates this Boolean expression for every inserted or updated row and rejects any that evaluate to false. This is domain integrity: constraining the valid values for a column to those that make sense in the business domain."
    hint: "You need a constraint that evaluates a Boolean condition on the column's value."
    reflectionPrompt: "What would happen if a data entry error inserted a product with a price of -5? How would a CHECK constraint prevent this?"
  - id: de-app-m2-09-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The type of integrity that ensures every row in a table is uniquely identifiable and has a non-null primary key is called ________ integrity.
    inputConfig:
      placeholder: "entity"
    markingRule:
      matchMode: CONTAINS
      accepted: [entity]
      rejectedFeedback: "Entity integrity is the guarantee that every row (entity instance) in a table can be uniquely identified — enforced by the PRIMARY KEY constraint, which combines UNIQUE and NOT NULL. Without entity integrity, rows are ambiguous and cannot be reliably referenced by foreign keys."
    hint: "This type of integrity relates to identifying individual 'entities' in the real world."
    reflectionPrompt: "What real-world problem arises if a table has no primary key and you try to update one specific row that has duplicate values?"
  - id: de-app-m2-09-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why the NOT NULL constraint is considered a data integrity mechanism, and give one real-world column where allowing NULL would cause a data quality problem.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [null, missing, required, unknown, mandatory, integrity, empty, absent]
      rejectedFeedback: "NOT NULL ensures that required data is always present. A NULL value means 'unknown or missing' — which is appropriate for some columns (e.g. a middle name) but dangerous for others. For example, allowing NULL in a customer's email address means some customers have no contact mechanism, making order confirmation emails impossible and causing silent failures in any system that assumes all customers have an email."
    hint: "Think of a column that absolutely must have a value for the record to be meaningful."
    reflectionPrompt: "Should a medical database allow NULL for a patient's date of birth? What problems would that create?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of the four integrity types is enforced by FOREIGN KEY constraints?"
    options: ["Entity integrity", "Domain integrity", "Referential integrity", "User-defined integrity"]
    correctIndex: 2
    feedback: "Referential integrity is the guarantee that every foreign key value points to an existing row in the parent table — enforced by FOREIGN KEY constraints. Entity integrity uses PRIMARY KEY. Domain integrity uses CHECK, NOT NULL, UNIQUE, and data types. User-defined integrity covers complex business rules."
  - type: MULTIPLE_CHOICE
    question: "A `status` column should only contain the values 'active', 'inactive', or 'suspended'. Which constraint enforces this?"
    options:
      - "FOREIGN KEY referencing a status table"
      - "CHECK (status IN ('active', 'inactive', 'suspended'))"
      - "UNIQUE (status)"
      - "NOT NULL"
    correctIndex: 1
    feedback: "CHECK (status IN ('active', 'inactive', 'suspended')) is the most direct way to constrain a column to a fixed set of values. A foreign key to a lookup table also works and has the advantage of being easily extensible, but for a small fixed set, CHECK is simpler. UNIQUE and NOT NULL alone do not restrict the range of valid values."
retrieval:
  recall: "Name the three main types of data integrity and the SQL mechanism that enforces each."
  explain: "Explain what domain integrity is and give two examples of CHECK constraints that enforce it."
  mistakeId:
    code: "no integrity constraints — trusting the application to send only valid data"
    answer: "Applications change, have bugs, and can be bypassed. Data also enters through migrations, admin tools, bulk imports, and API clients. Without database constraints, any of these paths can insert invalid, inconsistent, or corrupt data. Database-level integrity constraints are the only safeguard that applies unconditionally to every data pathway."
---

# Hook

You have now studied three specific types of database constraint: primary keys, foreign keys, and UNIQUE constraints. Each enforces a particular rule about the data. But there is a broader concept that unifies them all: **data integrity**.

Data integrity is the guarantee that your data is accurate, consistent, and trustworthy — not just at the moment it is inserted, but throughout its entire lifecycle: as it is updated, as related records are added and deleted, as time passes and business rules evolve.

Without data integrity, a database is unreliable. A product with a negative price. An order that references a customer who no longer exists. A user with no email address. These are not theoretical problems — they are the daily reality of systems that don't enforce their own rules.

# Lore Introduction

Master Selvaris opened the archive's quality ledger — a record of every error found in the database over the past decade. "Look at the pattern," she said. The errors fell into four clear categories: records with no identifier (cannot be found); records referencing non-existent counterparts (orphaned); records with impossible values (negative ages, future birthdates, blank names); and records that violated business rules (transactions above the authorised credit limit). "Four categories. Four types of failure. Each one has a prevention." She began writing on the whiteboard. "We call the combination of these preventions data integrity. It is not one rule but a system of rules that together guarantee the archive's trustworthiness."

# Core Learning

## Concept Introduction

### The Four Types of Integrity

| Type | Definition | Enforced By |
|------|-----------|-------------|
| **Entity integrity** | Every row is uniquely identifiable; no row is nameless | PRIMARY KEY (UNIQUE + NOT NULL) |
| **Referential integrity** | Every FK value points to a real existing row | FOREIGN KEY constraints |
| **Domain integrity** | Column values are within valid ranges and formats | Data types, NOT NULL, CHECK, UNIQUE |
| **User-defined integrity** | Complex business rules beyond standard constraints | CHECK, triggers, application logic |

### Entity Integrity

```sql
-- Primary key guarantees every row has an identity
CREATE TABLE employees (
    employee_id  INT PRIMARY KEY,   -- UNIQUE + NOT NULL: entity integrity
    name         VARCHAR(200) NOT NULL
);
```

Without entity integrity: duplicate rows, rows with no identity, inability to reference specific records via foreign keys.

### Referential Integrity

```sql
-- Foreign key guarantees references are never broken
CREATE TABLE departments (
    department_id INT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL
);

CREATE TABLE employees (
    employee_id   INT PRIMARY KEY,
    name          VARCHAR(200) NOT NULL,
    department_id INT NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
        ON DELETE RESTRICT
);
```

Without referential integrity: employees referencing deleted departments, "ghost" records, JOIN queries that silently drop rows.

### Domain Integrity

Domain integrity constrains the *values* that can appear in a column. It has three layers:

**Layer 1 — Data types**: `INT` rejects text; `DATE` rejects invalid dates; `DECIMAL(10,2)` rejects values with more than 2 decimal places.

**Layer 2 — NOT NULL**: Ensures mandatory columns are always populated.

```sql
CREATE TABLE orders (
    order_id    INT PRIMARY KEY,
    customer_id INT NOT NULL,      -- every order must have a customer
    order_date  DATE NOT NULL,     -- every order must have a date
    total       DECIMAL(10,2) NULL -- total may be calculated later
);
```

**Layer 3 — CHECK constraints**: Express arbitrary business rules about valid values.

```sql
CREATE TABLE products (
    product_id   INT PRIMARY KEY,
    name         VARCHAR(200) NOT NULL,
    unit_price   DECIMAL(10,2) NOT NULL CHECK (unit_price > 0),
    stock_qty    INT NOT NULL DEFAULT 0 CHECK (stock_qty >= 0),
    rating       DECIMAL(2,1) CHECK (rating BETWEEN 1.0 AND 5.0),
    status       VARCHAR(20) NOT NULL CHECK (status IN ('draft', 'active', 'discontinued'))
);
```

### Combining Integrity Types

A complete table definition should address all integrity types:

```sql
CREATE TABLE order_lines (
    line_id      INT PRIMARY KEY,                           -- entity integrity
    order_id     INT NOT NULL,
    product_id   INT NOT NULL,
    quantity     INT NOT NULL CHECK (quantity > 0),         -- domain integrity
    unit_price   DECIMAL(10,2) NOT NULL CHECK (unit_price >= 0),  -- domain integrity
    UNIQUE (order_id, product_id),                          -- domain integrity (business rule)
    FOREIGN KEY (order_id) REFERENCES orders(order_id)      -- referential integrity
        ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) -- referential integrity
        ON DELETE RESTRICT
);
```

## Why It Matters

Data quality compounds over time. A system with no integrity constraints starts clean and degrades. Six months later you find:
- 3,000 orders for non-existent customers
- 400 products with zero or negative prices
- 150 users with blank email addresses
- Dozens of duplicate records

Cleaning this data retroactively is expensive, error-prone, and often incomplete. Preventing it is free — the constraints are defined once and enforce themselves forever.

## Common Mistakes

- **Skipping NOT NULL on obviously mandatory columns**: "The application will always send a value" is not a guarantee. Put NOT NULL on anything that must always be present.
- **No CHECK constraints**: The most common gap. Business rules that live only in application code will eventually be violated.
- **Overly permissive CHECK**: `CHECK (price >= 0)` allows a price of 0 for items that should be paid for. Match the constraint to the actual business rule.
- **Not testing constraint violations**: Constraints should be tested explicitly — insert invalid data and verify it is rejected.

## Mental Model

Think of database integrity as the building code for a data structure. A building code doesn't just say "buildings should be safe" — it specifies precise rules: minimum wall thickness, maximum load per square metre, required emergency exits. These rules are enforced by inspectors and built into approved architectural plans. Data integrity constraints are the database's building code: specific, measurable rules that every row must satisfy. Just as a building that passes inspection is certified as safe, a database row that passes all constraints is certified as valid. No inspector needed after that — the structure itself is sound.

## Mini Summary

- ✔ Data integrity is the system of rules that keeps data accurate and trustworthy
- ✔ Entity integrity: every row is uniquely identifiable (PRIMARY KEY)
- ✔ Referential integrity: no broken references (FOREIGN KEY)
- ✔ Domain integrity: values are valid for their column (data types, NOT NULL, CHECK)
- ✔ Constraints enforced by the database hold unconditionally for all data pathways

# Guided Practice Quest

Work through the guided steps to identify the correct constraint type for a set of business rules and distinguish between the four types of data integrity.

# Solo Practice Quest

You are auditing the database schema for a hospital patient management system. The current schema has tables for `patients`, `wards`, `admissions`, `medications`, and `prescriptions`, but no constraints beyond basic data types. Identify at least eight specific integrity problems this creates (across all four integrity types) and write the SQL constraint additions that would fix each one. For each constraint: (1) name the integrity type it addresses, (2) write the SQL syntax, (3) explain the real-world error it prevents, and (4) describe the consequence if this constraint is absent and a bad value enters the system.

# Integration

**Mathematics**: Data integrity constraints form a formal constraint system over the database state space. The state space is all possible assignments of values to rows across all tables — an astronomically large set. Integrity constraints define a subset of this space: the *valid* states. Every INSERT and UPDATE is an attempt to move from one state to another; the database accepts the operation if and only if the destination state is within the valid subset. This framing reveals why constraints are powerful: they reduce the space of reachable states to only those the business considers correct, eliminating entire classes of bugs from possibility.

**Sciences (Quality Control)**: In manufacturing, Statistical Process Control (SPC) monitors production processes to ensure outputs stay within specification limits. Control charts flag when a measurement drifts outside acceptable bounds — analogous to a CHECK constraint. Acceptance sampling validates incoming materials against quality standards — analogous to NOT NULL and domain type checks. The core philosophy is identical: define what "good" looks like in advance, measure everything against that definition, and reject anything that fails. Both SPC and database integrity constraints embody the principle that quality cannot be inspected in retrospect — it must be built into the process.

# Lore Conclusion

Master Selvaris closed the quality ledger. The four categories of error — unidentifiable records, orphaned references, impossible values, violated business rules — were now addressed by four layers of constraint written into the archive's structure. "The errors did not stop appearing because clerks became more careful," she said. "They stopped because the archive itself refuses to accept them." She ran her hand along the constraint definitions written in the design document. "An archive that enforces its own rules is an archive that can be trusted. A database that cannot enforce its own rules is simply a place where data is stored until someone examines it and finds it wanting." She handed the design document to her apprentice. "Trust must be built into structure. Not assumed from behaviour."

---
