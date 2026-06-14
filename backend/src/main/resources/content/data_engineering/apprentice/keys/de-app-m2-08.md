---
id: de-app-m2-08
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
lesson: uniqueness
title: "Uniqueness"
sortOrder: 3
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m2-07]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Distinguishes between a UNIQUE constraint and a PRIMARY KEY constraint
    - Explains why duplicate values in certain columns cause real-world data problems
    - Describes the difference between a single-column UNIQUE constraint and a composite UNIQUE constraint
    - Gives two examples of columns that require UNIQUE constraints but should not be primary keys
    - Reflects on how uniqueness constraints shift validation responsibility from application to database
  keywords: [unique, constraint, duplicate, primary key, composite unique, business rule, email, ISBN, natural key]
  modelAnswer: |
    A UNIQUE constraint prevents duplicate values in a column (or combination of columns) independently of the primary key. Unlike a primary key, a UNIQUE constraint can allow NULL values (depending on the database) and a table can have multiple UNIQUE constraints. Columns like email, username, and product SKU require uniqueness as a business rule but are not necessarily the primary key. Composite UNIQUE constraints ensure that a combination of values is unique — for example, a student may only enrol once in any given course. Delegating uniqueness enforcement to the database removes the risk of race conditions and bypass vulnerabilities that application-level checks carry.
guidedSteps:
  - id: de-app-m2-08-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A `users` table has an auto-increment `user_id` as its primary key, but the business requires that no two users can register with the same email address. How should this be enforced?
    inputConfig:
      options:
        - "Make email the primary key instead of user_id"
        - "Add a UNIQUE constraint on the email column"
        - "Add a CHECK constraint that queries the table for existing emails"
        - "Enforce uniqueness in the application layer only"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Add a UNIQUE constraint on the email column"]
      rejectedFeedback: "A UNIQUE constraint on the email column enforces the business rule at the database level while keeping the stable surrogate primary key (user_id). Making email the primary key is risky (emails can change). CHECK constraints cannot query other rows. Application-only enforcement is fragile — any direct database access bypasses it."
    hint: "The primary key and the unique business identifier do not have to be the same column."
    reflectionPrompt: "What vulnerability exists if email uniqueness is only checked in application code before inserting a user?"
  - id: de-app-m2-08-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A UNIQUE constraint that spans two or more columns — where the *combination* of those columns must be unique — is called a ________ UNIQUE constraint.
    inputConfig:
      placeholder: "composite"
    markingRule:
      matchMode: CONTAINS
      accepted: [composite, compound, multi-column, multi column, multicolumn]
      rejectedFeedback: "A composite UNIQUE constraint covers multiple columns together. For example, a table tracking which employees are assigned to which projects might require that each employee can only appear once per project — but an employee can appear in multiple projects and a project can have multiple employees. The composite UNIQUE (employee_id, project_id) enforces this without making either column unique on its own."
    hint: "The same word used for primary keys that span multiple columns."
    reflectionPrompt: "If (employee_id, project_id) has a composite UNIQUE constraint, can employee 5 appear on three different projects?"
  - id: de-app-m2-08-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why checking for duplicate values in application code before inserting a record is not a safe substitute for a database UNIQUE constraint.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [race condition, concurrent, parallel, bypass, direct, application, database, constraint, enforce]
      rejectedFeedback: "Application-level uniqueness checks have a race condition vulnerability: two users can submit forms simultaneously, both pass the check (neither sees the other's record yet), and both inserts succeed — creating a duplicate. Additionally, any direct database access (migrations, scripts, admin tools) bypasses application code entirely. A database UNIQUE constraint is atomic — it checks and enforces in a single database operation, making it immune to race conditions."
    hint: "What happens if two requests arrive at exactly the same moment, both pass the uniqueness check, and both proceed to insert?"
    reflectionPrompt: "Think of a real scenario (e.g. two people registering at the same instant) where application-only checking would fail."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "How does a UNIQUE constraint differ from a PRIMARY KEY constraint?"
    options:
      - "They are identical — UNIQUE and PRIMARY KEY are interchangeable"
      - "UNIQUE allows NULL values and a table can have multiple UNIQUE constraints; PRIMARY KEY allows neither"
      - "UNIQUE can only be applied to text columns; PRIMARY KEY applies to any type"
      - "UNIQUE creates an index; PRIMARY KEY does not"
    correctIndex: 1
    feedback: "PRIMARY KEY implies UNIQUE + NOT NULL and there can only be one per table. UNIQUE allows NULL (in most databases, multiple NULLs are permitted since NULL ≠ NULL) and a table can have as many UNIQUE constraints as needed. Both automatically create an index."
  - type: MULTIPLE_CHOICE
    question: "Which SQL syntax adds a UNIQUE constraint to an existing `products` table on the `sku` column?"
    options:
      - "ALTER TABLE products ADD UNIQUE (sku);"
      - "ALTER TABLE products SET sku UNIQUE;"
      - "UPDATE products SET CONSTRAINT sku = UNIQUE;"
      - "CREATE UNIQUE INDEX ON products (sku);"
    correctIndex: 0
    feedback: "ALTER TABLE ... ADD UNIQUE (column) is the standard SQL syntax for adding a UNIQUE constraint to an existing table. Creating a unique index (option D) achieves the same result functionally in most databases, but the explicit constraint form is preferred for clarity."
retrieval:
  recall: "State two differences between a UNIQUE constraint and a PRIMARY KEY constraint."
  explain: "Explain why a race condition makes application-level uniqueness checking unreliable, and how a UNIQUE constraint solves it."
  mistakeId:
    code: "relying on application code to prevent duplicate emails at registration"
    answer: "Application-level duplicate checking is vulnerable to race conditions (two simultaneous registrations both pass the check and both insert) and can be bypassed by direct database access. A UNIQUE constraint on the email column enforces uniqueness atomically at the database level, making it immune to both failure modes."
---

# Hook

Some facts about the world are unique by nature: two users cannot share the same email address, two products cannot have the same SKU, two library books cannot have the same ISBN. These are business rules — constraints on what data is valid.

The question is: where do you enforce them?

Application code can check for duplicates before inserting. But application code can be bypassed. Two simultaneous requests can both pass the check and both insert. Scripts and admin tools often go straight to the database. The only enforcement that cannot be bypassed is enforcement built into the database itself.

# Lore Introduction

Master Selvaris spread two identical merchant registration forms on the desk. "Both arrived on the same morning courier," she said. "Different clerks processed them simultaneously. Both checked the registry — and at the moment each checked, the other's entry had not yet been written. Both saw no conflict. Both completed the registration." She held up the forms. "We now have two merchant accounts with the same trading licence number." She shook her head. "The registry's rule against duplicate licence numbers existed. But it was a rule for clerks to follow, not a rule enforced by the registry itself." She picked up a stamp. "Today we are adding a mechanical lock. When a licence number is entered, the registry physically prevents a second entry with the same number — no matter how many clerks work simultaneously."

# Core Learning

## Concept Introduction

### The UNIQUE Constraint

A `UNIQUE` constraint prevents duplicate values in a column (or combination of columns). Any attempt to insert or update a row that would create a duplicate is rejected.

```sql
CREATE TABLE users (
    user_id   INT PRIMARY KEY,
    email     VARCHAR(255) NOT NULL UNIQUE,
    username  VARCHAR(50)  NOT NULL UNIQUE,
    joined_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

Now:
```sql
INSERT INTO users (user_id, email, username) VALUES (1, 'alice@example.com', 'alice');
INSERT INTO users (user_id, email, username) VALUES (2, 'alice@example.com', 'alice2');
-- ERROR: duplicate key value violates unique constraint "users_email_key"
-- Detail: Key (email)=(alice@example.com) already exists.
```

### UNIQUE vs PRIMARY KEY

| Property | PRIMARY KEY | UNIQUE |
|----------|-------------|--------|
| Enforces uniqueness | ✅ | ✅ |
| Allows NULL | ❌ Never | ✅ Usually (one or more NULLs permitted) |
| Count per table | 1 only | Many |
| Auto-creates index | ✅ | ✅ |
| Used for FK references | ✅ Typically | ✅ Possible |

### Composite UNIQUE Constraints

When the uniqueness rule applies to a *combination* of columns, you define a composite UNIQUE constraint:

```sql
-- A student may only enrol once in a given course
CREATE TABLE enrolments (
    enrolment_id  INT PRIMARY KEY,
    student_id    INT NOT NULL,
    course_id     INT NOT NULL,
    enrolled_at   DATE NOT NULL,
    UNIQUE (student_id, course_id)   -- the combination must be unique
);

-- student 5 can appear in course 10, 11, and 12 — that's fine
-- student 5 cannot appear in course 10 twice — that violates the constraint
```

### The Race Condition Problem

Application code uniqueness check:
1. Query: "Does email 'alice@example.com' exist?"
2. Result: "No"
3. Insert user with that email

Two simultaneous requests both reach step 2 before either completes step 3. Both see "No". Both insert. You now have a duplicate.

A UNIQUE constraint is checked atomically inside the database transaction — no concurrent request can observe an intermediate state. The second insert is rejected regardless of timing.

### Common Columns That Need UNIQUE Constraints

| Column | Table | Business Rule |
|--------|-------|---------------|
| `email` | `users` | One account per email address |
| `username` | `users` | No two users share a username |
| `sku` | `products` | Each product has a unique stock-keeping unit |
| `isbn` | `books` | Each book has a unique international identifier |
| `national_id` | `persons` | Government-issued IDs are unique |
| `slug` | `articles` | URL slugs must be unique for routing |

## Why It Matters

Duplicate values in key columns corrupt data in subtle, cascading ways:

- Two users with the same email: password reset goes to the wrong account
- Two products with the same SKU: inventory counts are wrong for both
- Two articles with the same URL slug: one is unreachable

These are not edge cases — they are predictable failures that occur in any system where uniqueness is assumed but not enforced.

## Common Mistakes

- **UNIQUE on a nullable column without thinking**: Multiple NULL values are typically all permitted (since NULL ≠ NULL in SQL). If you expect "one null allowed", verify your database's behaviour.
- **Uniqueness only in the application**: Race conditions make this unreliable. Always add the database constraint too.
- **Confusing UNIQUE with PRIMARY KEY**: A table can have many UNIQUE constraints. Use them freely for any column that carries a real-world uniqueness requirement.
- **Forgetting composite uniqueness**: If the rule is "one enrolment per student per course", a single-column UNIQUE on student_id or course_id is wrong — you need a composite UNIQUE.

## Mental Model

Imagine a hotel room booking system. The rule "no two bookings can occupy the same room on the same night" is a composite uniqueness rule over (room_id, night_date). A UNIQUE(room_id, night_date) constraint makes the database physically incapable of double-booking — even if two reservation agents press "confirm" at the identical millisecond. The database's atomic check-and-insert operation means only one can win. The other gets an error. No human review needed. This is the value of database-level uniqueness.

## Mini Summary

- ✔ UNIQUE constraints prevent duplicate values and enforce real-world business rules
- ✔ A table can have many UNIQUE constraints; it can have only one PRIMARY KEY
- ✔ Composite UNIQUE constraints enforce uniqueness over combinations of columns
- ✔ Race conditions make application-only uniqueness checks unreliable
- ✔ UNIQUE and PRIMARY KEY both create automatic indexes

# Guided Practice Quest

Work through the guided steps to identify which columns need UNIQUE constraints, distinguish between single-column and composite UNIQUE constraints, and explain why database-level enforcement is safer than application-level checks.

# Solo Practice Quest

You are designing a multi-tenant SaaS application. The database has these tables: `organisations`, `users`, `teams`, `team_memberships`, and `invitations`. For each table, identify all columns and column combinations that require UNIQUE constraints. For each constraint: (1) write the SQL syntax, (2) state whether it is single-column or composite, (3) explain the real-world business rule it enforces, and (4) describe the failure mode if the constraint were missing. Present your final CREATE TABLE statements with all UNIQUE constraints included.

# Integration

**Mathematics**: Uniqueness constraints are a direct implementation of the mathematical concept of an injective function. A UNIQUE constraint on column C defines a partial function from the set of rows to the domain of C where no two rows map to the same value. The constraint formalises the statement: ∀ rows r₁, r₂ in T: r₁ ≠ r₂ → r₁[C] ≠ r₂[C]. In set theory, a UNIQUE constraint on multiple columns defines a function from rows to a product domain (C₁ × C₂) where the same injectivity property holds. This mathematical grounding explains why uniqueness constraints are so fundamental — they preserve the distinctness of real-world entities in their digital representation.

**Sciences (Chemistry)**: In chemistry, every compound has a unique CAS (Chemical Abstracts Service) registry number — an identifier assigned by a central authority to guarantee that each substance has exactly one canonical identity. This is a UNIQUE constraint enforced at a societal level. Chemical databases layer additional uniqueness constraints: IUPAC names, InChI strings, SMILES representations. A chemical database without uniqueness constraints would contain duplicate entries for the same substance under different names — exactly the confusion that surrogate primary keys and UNIQUE constraints on natural identifiers prevent in SQL databases.

# Lore Conclusion

Master Selvaris watched the mechanical lock click into place on the trading licence registry. The next clerk to attempt a duplicate entry found their pen physically stopped. "The rule is now structural," she said. "Not a guideline. Not a reminder. A physical constraint." She watched a confused clerk try the entry a second time before accepting the rejection. "Notice: the clerk need not know the rule exists. The archive enforces it regardless of what the clerk knows or intends." She turned to her apprentice. "That is the goal of every constraint: to make violations structurally impossible, not merely discouraged."

---
