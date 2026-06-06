---
id: de-app-m5-07
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m5
moduleTitle: "Module 5: Data Quality"
moduleGlyph: "✅"
moduleSortOrder: 5
topicSlug: data_integrity
topicTitle: "Data Integrity"
topicSortOrder: 2
lesson: consistency
title: "Consistency"
sortOrder: 3
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m5-06]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines data consistency and distinguishes it from accuracy
    - Explains the difference between internal consistency and cross-system consistency
    - Describes at least two causes of inconsistent data
    - Explains how transactions help maintain consistency
    - Reflects on how inconsistency erodes user trust in a system
  keywords: [consistency, inconsistent, transaction, conflict, cross-system, single source of truth, synchronisation]
  modelAnswer: |
    Data consistency means the same real-world fact is represented identically wherever it appears in a system. Accuracy concerns whether a value reflects reality; consistency concerns whether all representations of that value agree with each other. Internal inconsistency occurs within a single database (the same customer has two conflicting addresses in two tables). Cross-system inconsistency occurs between separate systems (a CRM shows a customer as active while the billing system shows them as cancelled). Causes include concurrent updates, failed synchronisation, and missing transactions. Transactions help by ensuring multi-step updates either all succeed or all fail — preventing partial updates that leave data in an inconsistent state.
guidedSteps:
  - id: de-app-m5-07-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A customer's billing address is "42 Oak Street" in the accounts table but "42 Oak Str." in the shipping table. Which data quality dimension is violated?
    inputConfig:
      options:
        - "Accuracy — the address may be wrong"
        - "Completeness — part of the address is missing"
        - "Consistency — the same fact is stored differently in two places"
        - "Timeliness — the address is out of date"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Consistency — the same fact is stored differently in two places"]
      rejectedFeedback: "Consistency means the same real-world fact is represented identically across all its occurrences. Two slightly different representations of the same address violate consistency."
    hint: "The problem is not that either value is wrong — it is that they disagree with each other."
    reflectionPrompt: "What system design approach would prevent this kind of inconsistency at the source?"
  - id: de-app-m5-07-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "When every system or table that stores the same fact stores it in exactly the same way, the data has a single ________ of truth."
    inputConfig:
      placeholder: "source"
    markingRule:
      matchMode: CONTAINS
      accepted: [source, version]
      rejectedFeedback: "A single source of truth means one authoritative location for each piece of data, from which all other representations are derived. This eliminates inconsistency by design."
    hint: "This phrase describes having one authoritative location for each piece of data."
    reflectionPrompt: "What are the risks of having the same data stored in multiple systems that can each be updated independently?"
  - id: de-app-m5-07-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain how using a database transaction helps maintain data consistency during a multi-step update.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [transaction, commit, rollback, partial, consistent, atomic]
      rejectedFeedback: "A transaction groups multiple statements into an all-or-nothing unit. If any step fails, the entire transaction is rolled back, preventing partial updates that leave data in an inconsistent intermediate state."
    hint: "What happens to a transaction if one of its SQL statements fails halfway through?"
    reflectionPrompt: "Can you think of a real-world operation that always requires multiple steps to complete — and what inconsistency would result if only some steps succeeded?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A bank transfer deducts £100 from Account A and adds it to Account B. If the deduction succeeds but the addition fails (due to a server crash), the data is:"
    options:
      - "Accurate — the deduction reflects a real transaction"
      - "Inconsistent — money has left one account but not arrived in another"
      - "Complete — both steps were attempted"
      - "Valid — the values are within acceptable ranges"
    correctIndex: 1
    feedback: "Correct — a partial update that leaves the system in a state where £100 has disappeared from one account without appearing in another is a consistency violation. A transaction wrapping both steps prevents this."
  - type: MULTIPLE_CHOICE
    question: "Which design approach best prevents inconsistency across multiple tables that store overlapping data?"
    options:
      - "Storing the same value in every table that might need it"
      - "Creating a single source of truth and referencing it with foreign keys"
      - "Allowing each system to maintain its own version of the data"
      - "Running a nightly reconciliation script"
    correctIndex: 1
    feedback: "A single source of truth — one authoritative location for each fact, referenced via foreign keys — prevents the divergence that causes inconsistency."
retrieval:
  recall: "Define data consistency and explain how it differs from data accuracy."
  explain: "Why does a bank transfer require a database transaction to maintain consistency?"
  mistakeId:
    code: "consistency is about accuracy — if each value is correct, the data is consistent"
    answer: "Consistency and accuracy are distinct dimensions. Accuracy means a value reflects reality. Consistency means all representations of the same fact agree with each other. Two tables can each have an accurate address for a customer, but if those addresses differ by even a comma, consistency is violated — because a system cannot know which to trust."
---

# Hook

A customer calls your support line to update their address. The support agent updates it in the CRM. The billing system, however, pulls addresses from a separate database that is synchronised only once a week. For the next six days, the customer receives bills at their old address. The support agent did everything right. The data in the CRM is accurate. But the billing system is inconsistent with the CRM — the same fact is stored differently in two places.

Data consistency is about agreement. Every time the same real-world fact appears in your system, it should look the same. When it does not, you have consistency violations — and the consequences range from frustrated customers to dangerous medical errors. Designing consistent systems requires both good database design and careful transaction management.

# Lore Introduction

Master Selvaris set two open ledgers side by side on the table. "The Registry of Births and the Registry of Census," he said, pointing at each. "Both should record the same population." He ran his finger down a list of names. "Here: Oswald Trent, listed as alive in the census but deceased in the births registry. And here: the village of Graymoor, counted as having 83 households in one book and 91 in the other." He closed both books. "Two records. Two different truths. A kingdom cannot govern itself on contradictions. Neither can a database."

# Core Learning

## Concept Introduction

| Concept | Definition | Example |
|---------|-----------|---------|
| **Data consistency** | Same real-world fact is identical across all representations | Customer address matches in CRM, billing, and shipping systems |
| **Internal consistency** | Consistency within a single database | Status column says "active"; deleted_at is not NULL — contradiction |
| **Cross-system consistency** | Consistency across separate systems | CRM and ERP agree on customer contact details |
| **Single source of truth** | One authoritative location for each fact | Address stored in customers table only; other tables reference it |
| **Partial update** | A multi-step change where only some steps complete | Balance deducted but not credited — incomplete transfer |
| **Transaction** | A unit of work that either fully completes or fully rolls back | BEGIN → deduct → credit → COMMIT (or ROLLBACK) |

## Why It Matters

Inconsistent data:
- Forces users to choose which version of a fact to trust
- Causes reports from different systems to disagree, undermining confidence
- Creates legal risk when regulatory records conflict
- Requires expensive reconciliation work to identify and resolve discrepancies

## Worked Examples

**Example 1: Internal inconsistency**
```sql
-- A customer is marked active but also has a deleted_at timestamp
SELECT id, status, deleted_at
FROM customers
WHERE status = 'active' AND deleted_at IS NOT NULL;
-- These rows are internally inconsistent
```

**Example 2: Using a transaction to prevent inconsistency**
```sql
BEGIN;
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  UPDATE accounts SET balance = balance + 100 WHERE id = 2;
COMMIT;
-- Either both updates succeed or neither does — no partial state
```

**Example 3: Single source of truth via foreign key**
```sql
-- Instead of storing customer_name in every order row:
CREATE TABLE orders (
    id          SERIAL PRIMARY KEY,
    customer_id INT NOT NULL REFERENCES customers(id)  -- reference, not copy
);
-- If the customer name changes, it changes everywhere at once
```

## Common Mistakes

- **Copying data instead of referencing it**: Storing `customer_name` in every order row means updates to the customer's name must be propagated to every order — a consistency nightmare waiting to happen.
- **Updating only one table in a multi-table operation**: Changing a user's email in the `users` table but not in the `email_preferences` table creates immediate internal inconsistency.
- **Assuming eventual consistency is always acceptable**: For financial, medical, or legal data, eventual consistency (where systems "catch up" over time) may not meet accuracy and regulatory requirements.

## Mental Model

Think of consistency like the time on all the clocks in a building. If every clock shows the same time, the building is internally consistent. If the clocks on different floors show different times, no one knows when meetings actually start. A single authoritative clock — a centralised time server — keeps everything synchronised. In databases, a single source of truth plays the same role as that master clock.

## Mini Summary

- ✔ Consistency means the same fact is identical wherever it appears
- ✔ It differs from accuracy: both representations can be accurate yet inconsistent
- ✔ A single source of truth (referenced, not copied) is the best preventative design
- ✔ Transactions prevent partial updates from creating inconsistent states
- ✔ Cross-system consistency requires synchronisation strategies and agreed authoritative sources

# Guided Practice Quest

Work through the guided steps to identify consistency violations and explain how transactions and single-source-of-truth design prevent them.

# Solo Practice Quest

You are told that your company's customer data is stored in three separate systems: a CRM, a billing platform, and a shipping service. Each stores the customer's name, email, and address independently. Describe: (1) three specific consistency violations that are likely to occur over time; (2) how you would redesign the system to establish a single source of truth; (3) what challenges arise when three existing systems must be reconciled. Reflect on whether a nightly synchronisation job solves the problem or just reduces the window of inconsistency.

# Integration

**Mathematics**: In formal logic, a consistent system is one in which no statement can be both true and false. A database with consistency violations is analogous to a logical system with contradictions — from contradictions, any conclusion can be derived, which means no conclusion can be trusted. This is the mathematical basis for why database consistency is foundational.

**Psychology**: Cognitive dissonance describes the mental discomfort caused by holding two contradictory beliefs simultaneously. Users experience a data equivalent when a system presents conflicting information — a customer portal shows one balance while a bank statement shows another. The response is the same: loss of trust, anxiety, and disengagement. Consistent data is not just a technical requirement — it is a foundation for user confidence.

# Lore Conclusion

Master Selvaris gestured to a large bronze plaque on the Archive wall. Engraved on it: "One Truth. Clearly Recorded. Always Accessible." "This is the Archive's founding principle," he said. "Every scribe, every record-keeper, every keeper of the archive is responsible for this promise." He turned to you. "When you design a data system, ask yourself: if this fact changes, how many places must be updated to keep everything consistent? If the answer is more than one, you have a design problem to solve."

---
