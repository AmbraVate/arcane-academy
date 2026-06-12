---
id: de-app-m2-10
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
lesson: one_to_one_relationships
title: "One-to-One Relationships"
sortOrder: 1
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m2-09]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Correctly defines a one-to-one relationship with an example
    - Explains how a one-to-one relationship is implemented using a UNIQUE foreign key
    - Identifies at least two valid reasons for splitting a one-to-one relationship across two tables
    - Distinguishes between a mandatory and optional one-to-one relationship
    - Reflects on when merging vs splitting is the better design choice
  keywords: [one-to-one, relationship, UNIQUE, foreign key, split, optional, mandatory, entity, extension]
  modelAnswer: |
    A one-to-one relationship exists when each row in table A corresponds to at most one row in table B, and vice versa. It is implemented by placing a foreign key in one table that references the primary key of the other, with a UNIQUE constraint on that foreign key. Valid reasons to split into two tables include: separating optional extension data (not all entities have it), separating sensitive or rarely-accessed data for security or performance, and representing genuinely separate real-world concepts that happen to be linked. A mandatory one-to-one requires both rows to always exist; an optional one-to-one allows one side to be absent. When the relationship is always present and the data volumes are modest, merging into one table is often simpler.
guidedSteps:
  - id: de-app-m2-10-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      How is a one-to-one relationship between `employees` and `employee_profiles` implemented in SQL?
    inputConfig:
      options:
        - "A foreign key in employee_profiles referencing employees, with no additional constraint"
        - "A foreign key in employee_profiles referencing employees, with a UNIQUE constraint on the foreign key"
        - "A foreign key in both tables, each referencing the other"
        - "A shared primary key value stored in a separate junction table"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A foreign key in employee_profiles referencing employees, with a UNIQUE constraint on the foreign key"]
      rejectedFeedback: "A foreign key alone creates a one-to-many relationship (many profiles could reference the same employee). Adding UNIQUE on the foreign key makes it one-to-one — each employee can have at most one profile. The UNIQUE constraint is what distinguishes one-to-one from one-to-many."
    hint: "What constraint prevents multiple profile rows from referencing the same employee?"
    reflectionPrompt: "Without the UNIQUE constraint on the foreign key, how many profile rows could a single employee have?"
  - id: de-app-m2-10-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In a one-to-one relationship where the second table only exists for *some* rows in the first table (e.g. only some users have a premium subscription), the relationship is described as ________.
    inputConfig:
      placeholder: "optional"
    markingRule:
      matchMode: CONTAINS
      accepted: [optional, partial, nullable]
      rejectedFeedback: "An optional one-to-one relationship means that the related row in the second table may or may not exist. Only some users have premium subscriptions — most do not. This is modelled by allowing the foreign key to be NULL (or simply by the absence of a row in the second table when the relationship doesn't apply)."
    hint: "The opposite of mandatory — the related row may or may not exist."
    reflectionPrompt: "If you merged the premium subscription data into the users table instead, what would happen to columns like subscription_start_date for non-premium users?"
  - id: de-app-m2-10-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain one good reason to split what could be a single table into a one-to-one relationship across two tables.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [optional, security, performance, sensitive, rarely, separate, access, extension, concern]
      rejectedFeedback: "Good reasons to split include: (1) optional data — if only 20% of users have premium profiles, keeping that data in a separate table avoids 80% of rows having many NULL columns; (2) security — separating sensitive data (payment details, medical records) into a table with stricter access controls; (3) performance — separating large rarely-read columns (e.g. a biography TEXT field) from frequently-read core data; (4) separation of concerns — the data represents genuinely separate concepts that happen to have a one-to-one mapping."
    hint: "Think about what happens when some rows have lots of NULLs because the additional data doesn't apply to them."
    reflectionPrompt: "If 10 million users exist and only 50,000 have premium subscriptions, how many NULL values would you have if you kept subscription data in the main users table?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Table A has 5 rows. Table B has a one-to-one relationship with Table A (some rows in A have no corresponding row in B). How many rows can Table B have at most?"
    options: ["5", "10", "Unlimited", "1"]
    correctIndex: 0
    feedback: "In a one-to-one relationship, each row in A corresponds to at most one row in B. With 5 rows in A, B can have at most 5 rows (one per A row). B may have fewer — some A rows may have no corresponding B row in an optional one-to-one relationship."
  - type: MULTIPLE_CHOICE
    question: "Why is a UNIQUE constraint on the foreign key column essential for a one-to-one relationship?"
    options:
      - "It speeds up the foreign key lookup"
      - "Without it, the relationship would be one-to-many — multiple rows in B could reference the same row in A"
      - "It prevents NULL values in the foreign key"
      - "It creates the foreign key constraint automatically"
    correctIndex: 1
    feedback: "A foreign key constraint alone says 'each row in B must reference a valid row in A' — it does not prevent multiple B rows referencing the same A row. That would be a one-to-many relationship. The UNIQUE constraint on the FK column adds the restriction that each A row can be referenced by at most one B row, making the relationship one-to-one."
retrieval:
  recall: "Describe how a one-to-one relationship is implemented in SQL."
  explain: "Explain two situations where splitting data across two tables in a one-to-one relationship is better than a single merged table."
  mistakeId:
    code: "adding a foreign key without UNIQUE to implement a one-to-one relationship"
    answer: "A foreign key without UNIQUE creates a one-to-many relationship — multiple child rows can reference the same parent row. For a one-to-one relationship, you must add UNIQUE on the foreign key column to ensure each parent row is referenced by at most one child row."
---

# Hook

Most database relationships are one-to-many: one customer has many orders, one author writes many books, one department has many employees. But sometimes the relationship is simpler: one entity corresponds to exactly one other entity.

A person has one passport. A user account has one profile page. A company has one registered address. These are one-to-one relationships — and they arise in database design more often than you might expect, particularly when you're deciding whether to split information across two tables or keep it in one.

# Lore Introduction

The archive had two registries for guild members: the public membership register and the private financial ledger. "Each member appears in exactly one row of each register," Master Selvaris explained. "One-to-one. The public register holds their name, specialisation, and contact details. The financial ledger holds their dues, debts, and payment history." She pointed to the shelf separating them. "They could be one combined ledger. But the financial information is confidential. The public register can be consulted by any clerk; the financial ledger requires a separate key." She turned to her apprentice. "One entity. Two representations. The split serves a purpose — security. Had there been no reason to separate them, a single register would be simpler."

# Core Learning

## Concept Introduction

### Definition

A **one-to-one relationship** exists when each row in table A corresponds to at most one row in table B, and each row in table B corresponds to at most one row in table A.

### Implementation

Place a **foreign key** in one table referencing the primary key of the other, and add a **UNIQUE constraint** on that foreign key. The UNIQUE constraint is what makes it one-to-one rather than one-to-many.

```sql
CREATE TABLE users (
    user_id   INT PRIMARY KEY,
    email     VARCHAR(255) NOT NULL UNIQUE,
    joined_at TIMESTAMP NOT NULL
);

-- user_profiles is optional — not every user has filled in their profile
CREATE TABLE user_profiles (
    profile_id  INT PRIMARY KEY,
    user_id     INT NOT NULL UNIQUE,          -- UNIQUE makes this one-to-one
    display_name VARCHAR(100),
    bio          TEXT,
    avatar_url   VARCHAR(500),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
```

With this schema:
- A user can have zero or one profile
- A profile belongs to exactly one user
- Multiple profiles cannot reference the same user (enforced by UNIQUE)

### Alternative: Shared Primary Key

A stricter form of one-to-one uses the same value as both the primary key and foreign key, enforcing that the relationship is always mandatory on the child side:

```sql
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    hire_date   DATE NOT NULL
);

-- Every employee contract row uses the employee_id as its own PK and FK
CREATE TABLE employee_contracts (
    employee_id   INT PRIMARY KEY,    -- same value is both PK and FK
    contract_type VARCHAR(50) NOT NULL,
    salary        DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);
```

Here, a contract can only exist if an employee exists, and a contract's PK is the same as the employee's PK — making the one-to-one relationship enforced without a separate UNIQUE constraint.

### When to Split vs Merge

| Split into one-to-one | Keep as one table |
|----------------------|------------------|
| Data is optional (many NULLs if merged) | Data is always present |
| Data is sensitive / different access control | Access level is the same |
| Data is large and rarely accessed | Data is small and frequently accessed |
| Genuinely separate business concepts | Same concept, just more columns |

```sql
-- BAD: merging optional subscription data — 80% of rows have NULLs
CREATE TABLE users (
    user_id            INT PRIMARY KEY,
    email              VARCHAR(255) NOT NULL,
    -- these are NULL for 80% of users
    subscription_plan  VARCHAR(50),
    subscription_start DATE,
    subscription_end   DATE,
    billing_email      VARCHAR(255)
);

-- BETTER: separate table, only exists for subscribers
CREATE TABLE user_subscriptions (
    user_id           INT PRIMARY KEY,
    subscription_plan VARCHAR(50) NOT NULL,
    subscription_start DATE NOT NULL,
    subscription_end   DATE,
    billing_email      VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
```

## Why It Matters

One-to-one relationships look redundant — why not one table? — until you meet the real reasons systems split data:

- Sensitive fields (salary, medical details) live in a separate table so access can be locked down independently
- Wide, rarely used columns are split out so common queries on the main table stay fast
- Optional sub-records (a user's seller profile) avoid forests of NULL columns for everyone else

Recognising when a one-to-one split is justified — and when it's needless complexity — is a recurring design judgement in real schemas, and a favourite interview question.

## Common Mistakes

- **Forgetting the UNIQUE constraint**: Without it, you get one-to-many — multiple child rows can reference the same parent.
- **Splitting without a reason**: If data is always present and doesn't require different access controls, a single table is simpler.
- **Both tables having foreign keys pointing to each other**: Circular foreign keys create insertion ordering problems. Pick one direction.

## Mental Model

Think of a one-to-one relationship like two filing drawers that are always kept in perfect sync — or not. A person's driver's licence and their medical record are both associated with that person. Each document exists once per person. But they live in different drawers with different locks, because the people who need access to driver's licences are not the same people who need access to medical records. The filing structure (one-to-one split) serves the access control requirement, not the data model itself.

## Mini Summary

- ✔ One-to-one: each row in A corresponds to at most one row in B
- ✔ Implemented by: foreign key + UNIQUE on the FK column
- ✔ An optional one-to-one means the related row may not exist
- ✔ Split into two tables when: optional data, security, performance, or separate concerns
- ✔ Merge into one table when: data is always present and access is the same

# Guided Practice Quest

Work through the guided steps to implement a one-to-one relationship correctly in SQL and identify when splitting or merging is the right design choice.

# Solo Practice Quest

A healthcare platform stores information about doctors. The core doctor record contains: name, licence number, specialty, and contact email. Optionally, a doctor may complete an extended professional profile: biography, photo URL, languages spoken, consultation fee, and whether they accept new patients. Design this as either a one-to-one relationship or a single merged table — justify your choice. Then write the complete SQL schema including all keys and constraints, and explain what happens when a doctor with a profile is deleted.

# Integration

**Mathematics**: A one-to-one relationship implements a mathematical partial injection between two finite sets. Formally: given tables A and B with a one-to-one relationship, the mapping f: A → B (where f is defined only for rows that have a corresponding row in B) is injective — no two rows of A map to the same row of B. If every row of A has a corresponding B row, f is a bijection (one-to-one and onto). The UNIQUE constraint on the foreign key enforces injectivity; the optional nature of the relationship determines whether f is partial or total.

**Sciences (Biology — Symbiosis)**: One-to-one relationships appear throughout biology in obligate mutualistic symbiosis, where two species exist in a relationship where each individual of species A is paired with exactly one individual of species B. The bobtail squid and the bioluminescent bacterium Aliivibrio fischeri form such a relationship — each squid hosts exactly one colony of bacteria in its light organ; each established colony belongs to exactly one squid. This biological one-to-one mirrors the database pattern: distinct entities, separate existence possible in principle, but linked by a precise constraint.

# Lore Conclusion

"The public register and the financial ledger are two tables," Master Selvaris concluded. "Each member has at most one row in each. The foreign key in the financial ledger points to the member's primary key in the public register. The UNIQUE constraint ensures no member has two financial records." She closed both books. "We could have combined them. But confidentiality required the split." She turned to her apprentice. "This is the lesson of one-to-one relationships: the data model does not always dictate the physical structure. Access control, performance, and separation of concerns may require you to split what could theoretically be one table into two. The relationship type tells you how to do it correctly."

---
