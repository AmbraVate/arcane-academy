---
id: de-app-m5-03
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m5
moduleTitle: "Module 5: Data Quality"
moduleGlyph: "✅"
moduleSortOrder: 5
topicSlug: data_accuracy
topicTitle: "Data Accuracy"
topicSortOrder: 1
lesson: missing_data
title: "Missing Data"
sortOrder: 3
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m5-02]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what NULL means in a database context
    - Distinguishes between missing data that is acceptable and missing data that is a problem
    - Describes at least two strategies for handling missing data
    - Explains the risks of ignoring NULL values in queries
    - Reflects on how the meaning of NULL varies by context
  keywords: [NULL, missing, unknown, default, imputation, handling, query, aggregate]
  modelAnswer: |
    NULL in SQL represents an unknown or absent value — it is not zero, not an empty string, and not false. Missing data is sometimes acceptable (an optional phone number) and sometimes a critical failure (a missing patient dosage). Strategies include: rejecting NULL for required fields (NOT NULL constraint), substituting a default value, imputing an estimated value (e.g., the column mean), or flagging the record for manual review. In queries, NULL propagates through arithmetic and comparisons unpredictably — NULL + 5 is NULL, and NULL = NULL is not true — so engineers must use IS NULL and COALESCE deliberately.
guidedSteps:
  - id: de-app-m5-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does NULL mean in a database column?
    inputConfig:
      options:
        - "The value is zero"
        - "The value is an empty string"
        - "The value is unknown or absent"
        - "The value is false"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The value is unknown or absent"]
      rejectedFeedback: "NULL means the value is unknown or not provided — it is distinct from zero, empty string, or false. This distinction matters enormously in queries."
    hint: "NULL is not any specific value — it represents the absence of a value."
    reflectionPrompt: "Can you think of a column where NULL would be acceptable and one where it would never be acceptable?"
  - id: de-app-m5-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "In SQL, the result of NULL + 5 is ________."
    inputConfig:
      placeholder: "NULL"
    markingRule:
      matchMode: CONTAINS
      accepted: [NULL, null, unknown]
      rejectedFeedback: "Any arithmetic or comparison involving NULL produces NULL — the unknown value 'infects' the result. This is why ignoring NULLs in queries leads to silently wrong answers."
    hint: "What happens when an unknown value is combined with a known one?"
    reflectionPrompt: "How does this behaviour affect SUM, AVG, or COUNT queries if a column has NULL values?"
  - id: de-app-m5-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe two strategies for handling missing data in a pipeline, and explain when you would choose each.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [reject, default, impute, flag, review, strategy]
      rejectedFeedback: "Strong answers might include: NOT NULL constraints to prevent missing required data at ingestion; COALESCE to substitute defaults in queries; imputation for optional analytics columns; or flagging records for manual review."
    hint: "Think about both prevention (stop NULLs from entering) and treatment (handle NULLs that already exist)."
    reflectionPrompt: "What are the risks of substituting a default value (e.g., 0) for a missing value you do not understand?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A query calculates AVG(salary) on a table where 20% of rows have NULL in the salary column. What happens?"
    options:
      - "The query returns an error"
      - "NULL values are treated as 0 and included in the average"
      - "NULL values are excluded — the average is calculated over non-NULL rows only"
      - "The query returns NULL"
    correctIndex: 2
    feedback: "SQL aggregate functions (SUM, AVG, COUNT) ignore NULL values by default. This can silently skew results if NULLs are prevalent."
  - type: MULTIPLE_CHOICE
    question: "Which SQL function returns a fallback value when an expression is NULL?"
    options: ["ISNULL()", "NULLIF()", "COALESCE()", "NVL2()"]
    correctIndex: 2
    feedback: "COALESCE(expression, fallback) returns the first non-NULL value. It is the standard SQL way to substitute a default when a value is missing."
retrieval:
  recall: "In one sentence, explain what NULL represents in SQL and why it is different from zero or empty string."
  explain: "Why can NULL values cause silent errors in aggregate queries, and how do you prevent this?"
  mistakeId:
    code: "NULL is the same as zero or empty string"
    answer: "NULL means unknown or absent — it is not zero, not empty, and not false. NULL in arithmetic produces NULL. NULL = NULL is not true in SQL. Treating NULL as zero can produce wildly incorrect aggregations and comparisons."
---

# Hook

You are building a sales dashboard. Your `AVG(revenue)` query returns a result that seems low. You double-check the data — the revenue column has values, the table looks populated. What you do not notice is that 30% of rows have NULL in the revenue column. The average is calculated over only 70% of the data, silently. No error. No warning. Just a wrong number on a dashboard someone is about to present to the board.

Missing data is one of the most insidious quality problems in data engineering. Unlike an obviously wrong value, a missing value can be invisible — it passes through queries, it escapes notices, and it warps results quietly. Understanding how NULL works and how to handle missing data is essential to building systems you can trust.

What does it really mean for data to be "missing" — and what should you do about it?

# Lore Introduction

Master Selvaris opened a ledger of merchant trade records. "This column tracks quantities shipped," he said, pointing to a long list of numbers punctuated by gaps. "See these blanks? In the original scrolls, the merchant simply left those lines empty. Were the quantities unknown? Not recorded? Zero? Lost in transit?" He shook his head. "The blank means something — but we do not know what. That ambiguity is its own kind of corruption. In the Archive, we name the unknown. We do not pretend it does not exist."

# Core Learning

## Concept Introduction

| Concept | Meaning | Example |
|---------|---------|---------|
| **NULL** | A value that is unknown, absent, or not applicable | A customer with no phone number: `phone = NULL` |
| **Required field** | A field where NULL is not acceptable | `customer_id` — must always be present |
| **Optional field** | A field where NULL is acceptable | `middle_name` — not every person has one |
| **NULL propagation** | NULL in any calculation produces NULL | `NULL + 100 = NULL`, `NULL * 0 = NULL` |
| **Imputation** | Replacing NULL with an estimated value | Replace missing salary with the column mean |
| **COALESCE** | SQL function returning the first non-NULL value | `COALESCE(phone, 'Not provided')` |

## Why It Matters

Missing data causes three categories of problem:

1. **Silent query errors**: Aggregations (AVG, SUM) silently exclude NULLs, skewing results without any warning.
2. **Logic failures**: Comparisons with NULL behave unexpectedly — `WHERE age > 18` excludes NULLs silently.
3. **Downstream corruption**: A NULL passed to a system expecting a real value triggers exceptions, default-value substitutions, or crashes.

## Worked Examples

**Example 1: NULL propagation in arithmetic**
```sql
SELECT revenue + 100 AS adjusted_revenue FROM orders;
-- Rows where revenue IS NULL return NULL for adjusted_revenue
-- No error is raised — the NULL silently propagates
```

**Example 2: COALESCE for safe defaults**
```sql
SELECT product_name, COALESCE(discount, 0) AS discount
FROM products;
-- If discount is NULL, 0 is substituted — safe for arithmetic
```

**Example 3: Filtering NULLs correctly**
```sql
-- Wrong: this excludes NULLs silently
SELECT * FROM customers WHERE phone <> '0000000000';

-- Correct: explicitly handle NULL
SELECT * FROM customers WHERE phone IS NULL OR phone <> '0000000000';
```

## Common Mistakes

- **Treating NULL as zero**: `SUM(COALESCE(amount, 0))` substitutes zero for unknown values, which may distort your totals if the absence means "not applicable" rather than "zero."
- **Forgetting that `NULL = NULL` is false in SQL**: Use `IS NULL` and `IS NOT NULL`, never `= NULL`.
- **Ignoring NULLs in joins**: An `INNER JOIN` silently drops rows where a join key is NULL — rows disappear from results without errors.

## Mental Model

Think of NULL as a locked box. You know a box exists, but you do not know what is inside it. Adding a locked box to a pile of items does not change the total — you cannot count what you cannot see. Comparing a locked box to another box tells you nothing — you cannot verify equality without opening both. In data, NULL is the locked box: it participates in structure but cannot participate meaningfully in calculation or comparison until it is handled explicitly.

## Mini Summary

- ✔ NULL means unknown or absent — not zero, not empty, not false
- ✔ NULL propagates: any arithmetic or comparison involving NULL returns NULL
- ✔ SQL aggregate functions silently exclude NULLs — this can skew results
- ✔ Use `IS NULL` / `IS NOT NULL` for comparisons, never `= NULL`
- ✔ Strategies: NOT NULL constraints, COALESCE for defaults, imputation, or manual review

# Guided Practice Quest

Work through the guided steps to test your understanding of NULL behaviour in SQL and the strategies available for handling missing data safely.

# Solo Practice Quest

Write five SQL queries (or pseudocode if you don't have a database) that demonstrate different NULL handling strategies: (1) a SELECT that uses COALESCE to substitute a default; (2) a WHERE clause that correctly filters both NULL and non-NULL rows; (3) a NOT NULL constraint on a CREATE TABLE; (4) an aggregate query where you describe the effect of NULLs; (5) a query using NULLIF to convert a sentinel value (like 0) to NULL. For each query, write one sentence explaining why the approach matters.

# Integration

**Mathematics**: In statistics, missing data is classified as Missing Completely At Random (MCAR), Missing At Random (MAR), or Missing Not At Random (MNAR). The type of missingness determines which imputation strategy is statistically valid. Engineers who understand this can design better data collection systems that minimise biased missingness.

**Psychology**: People tend to assume absence of information means the default — a blank field "must be zero" or "must mean no." This cognitive bias (the default effect) leads engineers to substitute zeros for NULLs without questioning whether zero is correct. Explicit NULL handling forces conscious, documented decisions about what absence actually means.

# Lore Conclusion

Master Selvaris closed the ledger. "In the old days, blank spaces in the Archive were filled in by whoever found them — with guesses, with defaults, with whatever seemed reasonable at the time. Decades later, those guesses were indistinguishable from facts." He looked at you. "The Archive's greatest law: never pretend to know something you do not. Mark the unknown as unknown. Decide explicitly what to do with it. And document every decision." He shelved the ledger carefully. "The blank space, honestly acknowledged, is more valuable than a confidently wrong number."

---
