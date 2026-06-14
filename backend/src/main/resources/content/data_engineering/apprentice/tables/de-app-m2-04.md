---
id: de-app-m2-04
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
lesson: choosing_data_types
title: "Choosing Data Types"
sortOrder: 4
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m2-03]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains the three key questions to ask when choosing a data type
    - Correctly identifies appropriate types for at least four categories of real-world data
    - Explains at least two consequences of choosing the wrong data type
    - Distinguishes between exact numeric types (DECIMAL) and approximate types (FLOAT)
    - Reflects on how data type choices affect query performance and storage efficiency
  keywords: [data type, integer, decimal, varchar, boolean, timestamp, storage, precision, arithmetic, domain]
  modelAnswer: |
    Choosing a data type requires asking: what values are valid, what operations will be performed, and how much storage is needed? Integers handle whole number counts and IDs; DECIMAL handles exact monetary values; VARCHAR handles variable-length text; BOOLEAN handles true/false flags; TIMESTAMP handles date and time values. Using FLOAT for money introduces rounding errors; storing dates as strings prevents sorting; storing numbers as text prevents arithmetic. The right type choice enforces data validity automatically and enables correct, efficient querying.
guidedSteps:
  - id: de-app-m2-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You need to store the price of a product in a database. Which data type should you use?
    inputConfig:
      options:
        - "FLOAT"
        - "VARCHAR(20)"
        - "DECIMAL(10,2)"
        - "INT"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["DECIMAL(10,2)"]
      rejectedFeedback: "DECIMAL(10,2) stores exact decimal values, essential for monetary amounts. FLOAT introduces floating-point rounding errors (0.1 + 0.2 ≠ 0.3). VARCHAR prevents arithmetic. INT cannot store decimal places."
    hint: "For money, you need exact decimal representation — which type guarantees precision?"
    reflectionPrompt: "What financial error could arise from storing prices as FLOAT and then summing thousands of transactions?"
  - id: de-app-m2-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence: "When you need to store a value that can only be true or false — such as whether a user has verified their email — you should use the ________ data type."
    inputConfig:
      placeholder: "BOOLEAN"
    markingRule:
      matchMode: CONTAINS
      accepted: [BOOLEAN, boolean, BOOL, bool, BIT, bit]
      rejectedFeedback: "BOOLEAN (or BIT in some databases) is designed for two-state values: TRUE or FALSE. Using INT (0/1) works but is less semantically clear and may accept values other than 0 and 1 without a CHECK constraint."
    hint: "This type has exactly two possible values: true and false."
    reflectionPrompt: "Why might a developer use INT (0/1) instead of BOOLEAN in some database systems?"
  - id: de-app-m2-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why storing a date value as VARCHAR (text) instead of the DATE data type causes problems.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [sort, arithmetic, format, invalid, compare, range, calculate, filter, date]
      rejectedFeedback: "Storing dates as VARCHAR means they sort alphabetically rather than chronologically (e.g., '2-Jan' sorts before '31-Dec' alphabetically but not chronologically). Date arithmetic (calculating age, days between dates) is impossible. Invalid dates like '32-Jan-2026' can be inserted. The DATE type prevents all these problems."
    hint: "Think about what you lose if you cannot perform date calculations or sort records chronologically."
    reflectionPrompt: "What would a report query look like if you needed to find all orders placed in the last 30 days, but dates were stored as VARCHAR?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the key difference between DECIMAL and FLOAT?"
    options:
      - "DECIMAL is faster; FLOAT uses less storage"
      - "DECIMAL stores exact values; FLOAT stores approximate values with potential rounding errors"
      - "FLOAT supports negative numbers; DECIMAL does not"
      - "They are identical — the choice makes no practical difference"
    correctIndex: 1
    feedback: "DECIMAL stores values with exact precision — essential for financial data. FLOAT uses binary floating-point representation, which introduces rounding errors for many decimal values. Never use FLOAT for monetary amounts."
  - type: MULTIPLE_CHOICE
    question: "Which data type is best for storing a user's full biography (potentially thousands of characters)?"
    options: ["VARCHAR(255)", "CHAR(50)", "TEXT", "BLOB"]
    correctIndex: 2
    feedback: "TEXT (or similar large text types like CLOB) is designed for arbitrarily long strings. VARCHAR(255) has a fixed maximum that would truncate a long biography. CHAR pads to a fixed length, wasting storage. BLOB is for binary data, not text."
retrieval:
  recall: "Name four SQL data types and give one real-world use case for each."
  explain: "Explain why DECIMAL should be used instead of FLOAT for storing monetary values."
  mistakeId:
    code: "storing a date as VARCHAR because 'text is flexible'"
    answer: "Flexibility is a liability for dates. VARCHAR dates cannot be sorted chronologically, cannot be used in date arithmetic, cannot be validated for calendar correctness, and cannot be filtered by date range efficiently. DATE type provides all these capabilities automatically."
---

# Hook

One of the most consequential decisions in database design happens before a single row of data is inserted: choosing the right data type for each column. Data types are not just labels — they determine what values can be stored, what operations can be performed, how the data is indexed, and how much storage is consumed.

A wrong type choice is a trap that springs later. Prices stored as text cannot be summed. Dates stored as strings cannot be sorted. Phone numbers stored as integers lose their leading zeros. These mistakes are silent at insertion time but cause cascading failures at query time.

The good news: once you understand the landscape of data types and the questions to ask, type selection becomes intuitive. Let's build that intuition.

# Lore Introduction

"The ledger headings are set," Master Selvaris said, "but we have not yet decided what kind of inscription each column accepts." She pointed to the price column. "Should the price be written as words — 'twenty-three silvers'? As a symbol — '23s'? Or as a precise numeral — '23.50'?" She paused. "The choice matters enormously. Written as words, we cannot add prices together. Written as symbols, we cannot sort them. Written as a precise numeral with a fixed format — we can do both, and we can prevent nonsense like 'twenty-three silvers and a promise.'" She smiled. "Data types are the archive's guarantee of precision."

# Core Learning

## Concept Introduction

### The Three Questions for Type Selection

1. **What values are valid?** (integers only? decimals? any text? just true/false?)
2. **What operations will be performed?** (arithmetic? sorting? date arithmetic? pattern matching?)
3. **What is the expected size?** (a few characters? thousands? exactly N characters?)

### Data Type Reference

| Category | Types | When to Use | When NOT to Use |
|----------|-------|-------------|----------------|
| **Integer** | `INT`, `BIGINT`, `SMALLINT` | IDs, counts, quantities, foreign keys | Monetary values, phone numbers, codes with leading zeros |
| **Exact Decimal** | `DECIMAL(p,s)`, `NUMERIC(p,s)` | Money, measurements needing precision | Scientific values requiring large range |
| **Approximate Float** | `FLOAT`, `REAL`, `DOUBLE` | Scientific/engineering values, coordinates | Monetary values (rounding errors) |
| **Fixed-length Text** | `CHAR(n)` | Values always exactly n chars (country codes, gender codes) | Variable-length content |
| **Variable-length Text** | `VARCHAR(n)` | Names, emails, descriptions with a known maximum | Very large text blocks |
| **Large Text** | `TEXT`, `CLOB` | Biographies, articles, free-text fields | Values needing indexing or short fixed-length use |
| **Boolean** | `BOOLEAN`, `BOOL` | Yes/no flags (is_active, is_verified) | Multi-state values (use an enum or VARCHAR) |
| **Date** | `DATE` | Calendar dates (birth date, order date) | When time-of-day is also needed |
| **Time** | `TIME` | Time of day without date | Rare; usually use TIMESTAMP |
| **Datetime** | `TIMESTAMP`, `DATETIME` | Full date + time (events, logs) | Date-only values (wastes precision) |
| **UUID** | `UUID` | Globally unique distributed IDs | Simple auto-increment use cases |
| **Binary** | `BYTEA`, `BLOB` | File content, encrypted data, binary protocols | Text, numbers, dates |
| **JSON** | `JSON`, `JSONB` | Semi-structured nested data | Fully structured data (use proper columns) |

## Why It Matters

**Storage efficiency**: Storing a boolean as VARCHAR("true"/"false") uses 4–5 bytes; BOOLEAN uses 1 bit. Across millions of rows this accumulates.

**Query correctness**: Sorting `VARCHAR` dates: `'10-Jun'`, `'2-Mar'`, `'9-Nov'` → `'10-Jun'`, `'2-Mar'`, `'9-Nov'` (alphabetical). Sorting `DATE` dates correctly produces chronological order.

**Operational integrity**: The database enforces type rules automatically. `DECIMAL(10,2)` rejects 'hello'. `DATE` rejects '32-Jan-2026'. Type enforcement is your first line of defence against bad data.

## Worked Examples

**Example 1: E-commerce Product — Type Choices Explained**
```sql
CREATE TABLE products (
    product_id    BIGINT           -- Large integer: auto-increment ID, may need > 2 billion
    product_name  VARCHAR(200)     -- Variable text up to 200 chars; names vary in length
    sku           CHAR(12)         -- Fixed: all SKUs are exactly 12 characters
    unit_price    DECIMAL(10,2)    -- Exact decimal: money requires precision
    weight_kg     DECIMAL(8,3)     -- Exact decimal: physical measurement
    is_active     BOOLEAN          -- Two states only: active or not
    created_at    TIMESTAMP        -- Full datetime: need to know exact moment
    description   TEXT             -- Unlimited text: product descriptions vary widely
);
```

**Example 2: Type Mistake and Its Consequence**
```sql
-- WRONG: Order date stored as VARCHAR
order_date VARCHAR(20)  -- values: '01/06/2026', '1 Jun 2026', '2026-06-01' (inconsistent!)

-- Query: "Find orders in the last 30 days" — impossible to write correctly
-- Sort by order_date — alphabetical, not chronological

-- CORRECT:
order_date DATE          -- enforces ISO format, enables date arithmetic
-- Query: WHERE order_date >= CURRENT_DATE - INTERVAL '30 days'  -- works perfectly
```

## Common Mistakes

- **FLOAT for currency**: Financial rounding errors accumulate across millions of transactions. Use DECIMAL.
- **VARCHAR for dates**: Prevents date arithmetic, allows invalid dates, sorts incorrectly.
- **INT for phone numbers**: Drops leading zeros, rejects `+44` prefix, insufficient range for international numbers.
- **Over-sized VARCHAR**: `VARCHAR(1000)` for a postcode wastes index space and allows invalid data. Be specific.
- **TEXT for everything**: TEXT columns cannot be indexed efficiently in most databases. Use VARCHAR with appropriate length for indexed columns.

## Mental Model

Think of data types as the physical format of a storage container. An egg carton holds exactly 12 eggs of a specific size — putting something else in it is physically wrong. A petrol station pump dispenses only petrol — connecting a diesel car causes immediate, obvious failure. Data types are the database's equivalent: they define the container format, and the database enforces that only the right kind of content goes in. Unlike physical containers, a database will often silently coerce wrong-typed data rather than failing — which is why you must choose carefully upfront.

## Mini Summary

- ✔ Ask three questions before choosing a type: valid values, operations needed, expected size
- ✔ Use DECIMAL (not FLOAT) for monetary values — floating-point causes rounding errors
- ✔ Use DATE or TIMESTAMP (not VARCHAR) for dates — enables sorting and arithmetic
- ✔ Use VARCHAR for variable text; CHAR for fixed-length codes; TEXT for large content
- ✔ Wrong type choices cause silent data corruption and broken queries

# Guided Practice Quest

Work through the guided steps to select appropriate data types for realistic column scenarios, explain the consequences of wrong type choices, and distinguish between exact and approximate numeric types.

# Solo Practice Quest

You are designing a database for a medical clinic. Define appropriate data types for the following data points, and for each write a justification and identify one specific mistake that would occur if a different type were used: (1) patient date of birth, (2) consultation fee, (3) whether the patient has given GDPR consent, (4) the patient's medical history notes, (5) the patient's NHS number (always 10 digits), (6) the date and time of an appointment, (7) blood pressure reading (e.g., 120.5), (8) patient's postcode, (9) a globally unique patient identifier, and (10) number of previous visits. Present your answer as a SQL CREATE TABLE statement with inline comments.

# Integration

**Mathematics**: The concept of a data type is a direct implementation of a mathematical domain — the set of all valid values for a variable. Integer types implement ℤ (or a bounded subset). DECIMAL types implement ℚ (rational numbers) to a specified precision. DATE implements the set of all valid calendar dates. Type constraints are set restrictions. This mathematical perspective reveals why choosing the right type matters: you are defining the domain of a variable, and operations are only valid within that domain — just as arithmetic operations are only valid within the appropriate number system.

**Sciences (Chemistry)**: In analytical chemistry, significant figures and units are paramount — reporting 100.0 g and 100 g convey different levels of precision. DECIMAL(8,3) in SQL is the database equivalent of specifying three significant decimal places — it commits to a precision that all values in that column will respect. FLOAT, by contrast, is like scientific notation without guaranteed precision — appropriate for approximate measurements, dangerous for exact ones. The database designer, like the chemist, must choose the right level of precision for the data being captured.

# Lore Conclusion

Master Selvaris examined the completed column type specifications. "Now every column knows its purpose precisely," she said. "The price column will only accept exact decimals — no approximate scrawls. The date column will only accept valid calendar entries — no ambiguous shorthand. The licence number will always be exactly twelve characters." She closed the design document. "Data types are the silent guardian of every record we will ever write. They enforce the rules so we do not have to inspect every entry manually." She handed the document to her apprentice. "Choose types carefully at the beginning. They are very difficult to change later."

---
