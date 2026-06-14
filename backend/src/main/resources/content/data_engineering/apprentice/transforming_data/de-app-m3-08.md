---
id: de-app-m3-08
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m3
moduleTitle: "Module 3: SQL Foundations"
moduleGlyph: "🔍"
moduleSortOrder: 3
topicSlug: transforming_data
topicTitle: "Transforming Data"
topicSortOrder: 2
lesson: string_functions
title: "String Functions"
sortOrder: 8
difficulty: 2
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m3-07]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Uses UPPER, LOWER, and TRIM correctly with examples
    - Concatenates strings using || or CONCAT and explains the difference
    - Uses SUBSTRING/SUBSTR to extract parts of a string
    - Uses LENGTH and REPLACE appropriately
    - Reflects on when string transformation should happen in SQL vs in application code
  keywords: [UPPER, LOWER, TRIM, CONCAT, SUBSTRING, LENGTH, REPLACE, string, function, transform]
  modelAnswer: |
    String functions transform text column values at query time. UPPER/LOWER convert case; TRIM removes leading/trailing whitespace (common for cleaning imported data); CONCAT (or ||) joins strings; SUBSTRING extracts a portion; LENGTH returns character count; REPLACE substitutes characters. These functions are used to clean data (normalise case, remove whitespace), format output (combine first and last name), or extract substrings (isolate postcode prefix). String transformations in SQL avoid a round-trip to application code — the database processes them during the query, which is faster for large datasets. Case sensitivity varies by database collation.
guidedSteps:
  - id: de-app-m3-08-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which expression produces a full name from `first_name` and `last_name` columns, separated by a space?
    inputConfig:
      options:
        - "first_name + ' ' + last_name"
        - "CONCAT(first_name, last_name)"
        - "CONCAT(first_name, ' ', last_name)"
        - "first_name || last_name"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["CONCAT(first_name, ' ', last_name)"]
      rejectedFeedback: "CONCAT(first_name, ' ', last_name) concatenates the three arguments with a space in the middle. CONCAT(first_name, last_name) would produce 'AliceSmith' (no space). The + operator is SQL Server syntax. The || operator works in PostgreSQL and SQLite but not MySQL. CONCAT is the most portable option."
    hint: "The space ' ' needs to be the middle argument in the CONCAT call."
    reflectionPrompt: "How would the output differ if you used CONCAT(first_name, last_name) without the space argument?"
  - id: de-app-m3-08-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To extract the first 3 characters from a `postcode` column (e.g. 'SW1A 1AA' → 'SW1'), you would use: `________(postcode, 1, 3)`
    inputConfig:
      placeholder: "SUBSTRING"
    markingRule:
      matchMode: CONTAINS
      accepted: [SUBSTRING, substring, SUBSTR, substr, LEFT]
      rejectedFeedback: "SUBSTRING(postcode, 1, 3) extracts 3 characters starting at position 1 (SQL positions are 1-indexed). SUBSTR is an alias available in many databases. LEFT(postcode, 3) also works and is perhaps more readable for this use case — it returns the leftmost N characters."
    hint: "The function that extracts a portion of a string — starting at a position, for a given length."
    reflectionPrompt: "How would you extract the last 3 characters of a postcode using SUBSTRING? (Hint: use LENGTH)"
  - id: de-app-m3-08-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why TRIM is particularly useful when working with data that has been imported from CSV files or entered via web forms.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [whitespace, spaces, trailing, leading, match, compare, import, CSV, form, input, clean]
      rejectedFeedback: "Data imported from CSV files or entered in web forms often contains leading or trailing whitespace that is invisible but affects comparisons. 'London ' (with a trailing space) does not equal 'London' — a WHERE clause for city = 'London' would miss it. TRIM removes this whitespace, normalising the data so comparisons and lookups work correctly. It is one of the most common data cleaning operations."
    hint: "Think about invisible characters that users might accidentally type, or that CSV exports often include."
    reflectionPrompt: "If a city column contains ' London ' (spaces on both sides), which comparison would fail: city = 'London' or TRIM(city) = 'London'?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `UPPER('hello world')` return?"
    options: ["hello world", "Hello World", "HELLO WORLD", "HELLO_WORLD"]
    correctIndex: 2
    feedback: "UPPER converts all characters to uppercase: 'HELLO WORLD'. LOWER converts to lowercase. Title case (Hello World) is not a standard SQL function — it requires more complex expressions or database-specific functions."
  - type: MULTIPLE_CHOICE
    question: "What does `LENGTH('database')` return?"
    options: ["7", "8", "9", "It depends on the database"]
    correctIndex: 1
    feedback: "'database' has 8 characters: d-a-t-a-b-a-s-e. LENGTH returns the number of characters (not bytes, for multibyte/Unicode characters in most databases). LEN is the equivalent in SQL Server."
retrieval:
  recall: "Name four SQL string functions and give one use case for each."
  explain: "Explain how TRIM helps with data quality issues in user-input or imported data."
  mistakeId:
    code: "WHERE city = 'London' when some values are stored as ' London' or 'London '"
    answer: "Leading or trailing whitespace makes equality comparisons fail silently. The fix is WHERE TRIM(city) = 'London', or better: clean the data at input time using TRIM so it is never stored with whitespace. Using TRIM in a WHERE clause prevents index usage on large tables — cleaning at input is more efficient."
---

# Hook

Data is messy. Names are stored in inconsistent casing. Addresses have trailing spaces. Phone numbers include dashes in some rows and not others. First and last names are in separate columns but reports need them combined.

String functions let you clean, transform, and format text data inside SQL queries. They are the first line of defence against dirty data, and they make output readable without requiring a trip to application code.

# Lore Introduction

"The member registry has names in all different cases," the Archivist said. "'SMITH, JOHN'. 'smith, jane'. 'Smith, Robert'. They are the same format but different capitalisation — they won't match in searches." Master Selvaris wrote: `WHERE LOWER(last_name) = 'smith'`. "The LOWER function normalises case at query time. We don't change the stored data — we transform it for the comparison." She then showed CONCAT for building full name strings, TRIM for removing the invisible spaces many records had accumulated, and SUBSTRING for extracting the district code from postcodes. "String functions are the archive's cleaning tools," she said. "Use them to work with data as it is, and produce output as it should be."

# Core Learning

## Concept Introduction

### Case Functions

```sql
UPPER(string)   -- converts to uppercase
LOWER(string)   -- converts to lowercase

-- Case-insensitive search
WHERE LOWER(email) = LOWER('Alice@Example.COM')

-- Normalise for display
SELECT UPPER(country_code) AS country FROM addresses;
```

### TRIM — Remove Whitespace

```sql
TRIM(string)          -- removes leading and trailing spaces
LTRIM(string)         -- removes leading spaces only
RTRIM(string)         -- removes trailing spaces only
TRIM(char FROM string) -- removes specific character (PostgreSQL/Oracle)

-- Clean data from CSV imports
WHERE TRIM(city) = 'London'
SELECT TRIM(name) AS clean_name FROM customers;
```

### Concatenation

```sql
-- Standard SQL (PostgreSQL, SQLite)
SELECT first_name || ' ' || last_name AS full_name FROM employees;

-- CONCAT (MySQL, PostgreSQL, SQL Server 2012+)
SELECT CONCAT(first_name, ' ', last_name) AS full_name FROM employees;

-- CONCAT_WS (with separator) — handles NULL gracefully
SELECT CONCAT_WS(' ', first_name, middle_name, last_name) AS full_name FROM employees;
-- If middle_name is NULL, CONCAT_WS omits it (unlike CONCAT which returns NULL)
```

### SUBSTRING — Extract Part of a String

```sql
-- SUBSTRING(string, start_position, length)
-- Positions are 1-indexed in SQL

SUBSTRING('SW1A 1AA', 1, 4)   -- 'SW1A' (first 4 characters)
SUBSTRING('SW1A 1AA', 6, 3)   -- '1AA' (3 chars starting at position 6)

-- Extract year from a date stored as text (poor design, but happens)
SELECT SUBSTRING(date_string, 1, 4) AS year FROM legacy_table;

-- LEFT and RIGHT equivalents
LEFT('ABCDE', 3)    -- 'ABC'
RIGHT('ABCDE', 2)   -- 'DE'
```

### LENGTH — String Length

```sql
LENGTH(string)   -- PostgreSQL, MySQL
LEN(string)      -- SQL Server

LENGTH('hello')  -- 5
LENGTH('')       -- 0
LENGTH(NULL)     -- NULL

-- Find suspiciously short names
WHERE LENGTH(TRIM(name)) < 2
```

### REPLACE — Substitute Characters

```sql
-- REPLACE(string, search, replacement)
REPLACE('hello world', 'world', 'SQL')   -- 'hello SQL'
REPLACE(phone, '-', '')                  -- remove dashes from phone numbers
REPLACE(UPPER(sku), ' ', '_')            -- normalise product codes
```

### Function Composition

String functions can be nested:

```sql
-- Normalise: trim whitespace, then convert to uppercase
SELECT UPPER(TRIM(country_code)) AS normalised_country FROM addresses;

-- Build a username from first name and last name
SELECT LOWER(first_name) || '.' || LOWER(last_name) AS username FROM employees;
```

### Database Differences

| Function | PostgreSQL | MySQL | SQL Server |
|---|---|---|---|
| Uppercase | `UPPER` | `UPPER` | `UPPER` |
| Lowercase | `LOWER` | `LOWER` | `LOWER` |
| Length | `LENGTH` | `LENGTH` | `LEN` |
| Substring | `SUBSTRING` | `SUBSTRING` | `SUBSTRING` |
| Concatenate | `\|\|` or `CONCAT` | `CONCAT` | `+` or `CONCAT` |
| Trim | `TRIM` | `TRIM` | `TRIM` or `LTRIM`/`RTRIM` |

## Why It Matters

Real-world text data is messy — inconsistent case, stray spaces, names and codes mashed into one field — and string functions are the cleanup crew:

- Matching "Smith", "smith ", and "SMITH" as the same customer requires TRIM and case functions before comparison
- Extracting an area code, a product prefix, or a domain from an email is daily-driver substring work
- Building display values (full name from first and last) with concatenation appears in nearly every report

Data engineers famously spend more time cleaning data than analysing it; string functions are the first and most-used tools in that kit.

## Common Mistakes

- **Using string functions in WHERE without indexes**: `WHERE LOWER(email) = 'alice@example.com'` cannot use a standard index on `email`. Better: store emails already lowercase, or use a functional index.
- **CONCAT with NULL**: `CONCAT('Hello', NULL, 'World')` returns NULL in standard SQL. Use CONCAT_WS or COALESCE.
- **Confusing LENGTH (characters) with OCTET_LENGTH (bytes)**: For multibyte character sets (UTF-8), these differ.

## Mental Model

Think of string functions as the archive's calligraphy tools. They don't change what's written in the ledger — they reformat it as it is read. UPPER is a magnification lens that shows all letters in capitals. TRIM is a dust brush that removes invisible margin marks. CONCAT is a paste operation that combines entries from two columns into one line. The original entries are untouched; the output is transformed for the reader's purpose.

## Mini Summary

- ✔ `UPPER` / `LOWER` — normalise case for comparisons and display
- ✔ `TRIM` — remove invisible leading/trailing whitespace
- ✔ `CONCAT(a, sep, b)` or `a || sep || b` — join strings
- ✔ `SUBSTRING(str, start, len)` — extract part of a string
- ✔ `LENGTH` / `REPLACE` — measure and substitute string content

# Guided Practice Quest

Work through the guided steps to apply string functions for case normalisation, concatenation, whitespace cleaning, and substring extraction.

# Solo Practice Quest

A `contacts` table has been imported from a legacy system with messy data. Columns: `id`, `first_name`, `last_name`, `email`, `phone`, `postcode`, `city`. Known data quality issues: names are inconsistent case, emails have trailing spaces, phone numbers have inconsistent dash formatting, postcodes need the outward code extracted (first 3-4 characters before the space). Write five queries: (1) display full name in consistent capitalisation, (2) case-insensitive email search for 'alice@example.com', (3) clean phone numbers by removing dashes, (4) extract the outward postcode code, (5) a comprehensive cleaning query that combines multiple functions to produce a clean display record. For each, explain what problem it solves.

# Integration

**Mathematics**: String functions in SQL implement formal operations on the free monoid over the alphabet — the mathematical structure underlying string theory. Concatenation (||) is the monoid operation; the empty string '' is the identity element (x || '' = x). LENGTH measures the monoid element's length. SUBSTRING implements the subsequence operation. REPLACE implements string transduction — a function from strings to strings defined by substitution rules. The mathematical study of these operations (formal language theory, automata theory) underpins both SQL string functions and programming language compilers.

**Sciences (Linguistics — Corpus Analysis)**: Linguists analysing large text corpora use exactly these operations: LOWER for case normalisation (so 'The' and 'the' are treated as the same word), TRIM for removing artefacts from text extraction, REPLACE for normalising punctuation, SUBSTRING for extracting morphological roots. A corpus database of ten million sentences relies on the same SQL string functions for preprocessing that a web application uses for user data cleaning — the operations are universal because text manipulation requirements are universal across domains.

# Lore Conclusion

The member registry searches now worked correctly. 'SMITH', 'smith', and 'Smith' all matched when searching for 'smith' via LOWER. Trailing spaces no longer broke comparisons after TRIM. Full names appeared cleanly formatted with CONCAT. "String functions are not glamorous," Master Selvaris said, "but they solve the real problems in real data. Data is never perfectly clean. Every import from the outside world brings inconsistencies. String functions let you work with the data you have while producing the output you need." She closed the query editor. "Clean data at input if you can. When you can't, clean it at query time."

---
