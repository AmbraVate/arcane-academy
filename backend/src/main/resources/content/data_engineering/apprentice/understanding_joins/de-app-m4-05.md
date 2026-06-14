---
id: de-app-m4-05
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m4
moduleTitle: "Module 4: Joining Information"
moduleGlyph: "🔗"
moduleSortOrder: 4
topicSlug: understanding_joins
topicTitle: "Understanding Joins"
topicSortOrder: 1
lesson: full_outer_join
title: "FULL OUTER JOIN"
sortOrder: 5
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m4-04]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains that FULL OUTER JOIN preserves all rows from both tables
    - Describes that NULLs appear on whichever side has no match
    - Identifies the use case for FULL OUTER JOIN (data reconciliation, finding gaps on either side)
    - Notes that FULL OUTER JOIN is not supported in MySQL and describes the workaround
    - Reflects on when FULL OUTER JOIN is more appropriate than LEFT JOIN or INNER JOIN
  keywords: [FULL OUTER JOIN, both tables, NULL, reconciliation, gaps, union, LEFT JOIN, MySQL, symmetrical]
  modelAnswer: |
    FULL OUTER JOIN returns all rows from both tables. Where a match exists (ON condition is true), the row is merged as in INNER JOIN. Where a row from the left table has no match in the right, it appears with NULLs in the right-table columns. Where a row from the right table has no match in the left, it appears with NULLs in the left-table columns. FULL OUTER JOIN is the combination of LEFT JOIN and RIGHT JOIN. It is used for data reconciliation — finding rows that exist in one table but not the other, or comparing two datasets for completeness. MySQL does not support FULL OUTER JOIN natively; the workaround is LEFT JOIN UNION ALL RIGHT JOIN with deduplication.
guidedSteps:
  - id: de-app-m4-05-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Two tables: `table_a` (ids: 1, 2, 3) and `table_b` (ids: 2, 3, 4). A FULL OUTER JOIN ON a.id = b.id returns how many rows?
    inputConfig:
      options:
        - "2 rows (only matched rows: ids 2 and 3)"
        - "3 rows (all from table_a: ids 1, 2, 3)"
        - "3 rows (all from table_b: ids 2, 3, 4)"
        - "4 rows (all unique ids: 1, 2, 3, 4)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["4 rows (all unique ids: 1, 2, 3, 4)"]
      rejectedFeedback: "FULL OUTER JOIN returns all rows from both tables. IDs 2 and 3 exist in both — they produce matched rows. ID 1 exists only in table_a — it appears with NULL for table_b columns. ID 4 exists only in table_b — it appears with NULL for table_a columns. Total: 4 rows. INNER JOIN would return 2 (only matched). LEFT JOIN would return 3 (all of table_a). RIGHT JOIN would return 3 (all of table_b)."
    hint: "FULL OUTER JOIN includes ALL rows from BOTH tables — matched pairs, plus unmatched lefts, plus unmatched rights."
    reflectionPrompt: "How would the result change if you added WHERE a.id IS NULL OR b.id IS NULL?"
  - id: de-app-m4-05-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In MySQL, FULL OUTER JOIN is not supported. The workaround uses two outer joins combined with: `SELECT * FROM a LEFT JOIN b ON a.id = b.id UNION ALL SELECT * FROM a RIGHT JOIN b ON a.id = b.id WHERE ________ IS NULL`
    inputConfig:
      placeholder: "a.id"
    markingRule:
      matchMode: CONTAINS
      accepted: [a.id, "a.id"]
      rejectedFeedback: "The UNION ALL workaround in MySQL: take all rows from LEFT JOIN (all of table_a), then add all rows from RIGHT JOIN WHERE a.id IS NULL (rows from table_b with no match in table_a — avoiding duplicates of the matched rows). Together they simulate FULL OUTER JOIN. PostgreSQL, SQL Server, and Oracle support FULL OUTER JOIN natively."
    hint: "The RIGHT JOIN part of the UNION should only include the rows from table_b that had no match in table_a."
    reflectionPrompt: "Why must you use UNION ALL rather than UNION in this workaround? What is the difference?"
  - id: de-app-m4-05-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, describe a real-world scenario where FULL OUTER JOIN is the right tool, and what the NULLs on each side would represent.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [reconcile, compare, both, missing, gap, discrepancy, audit, NULL, left, right, neither]
      rejectedFeedback: "Example: Reconciling two financial systems that should contain the same transactions. Table A is the payment processor's records; table B is the internal accounting system's records. FULL OUTER JOIN on transaction_id shows: matched rows (in both systems), left-only rows with NULLs in B columns (in payment processor but missing from accounting), right-only rows with NULLs in A columns (in accounting but missing from payment processor). The NULLs on each side expose the discrepancies."
    hint: "Think of two datasets that should contain the same records but might have gaps on either side — reconciliation, audit, data migration."
    reflectionPrompt: "After a FULL OUTER JOIN, how would you use WHERE to find ONLY the unmatched rows from both sides?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does FULL OUTER JOIN return that neither LEFT JOIN nor RIGHT JOIN returns alone?"
    options:
      - "Only matched rows"
      - "Unmatched rows from the left table only"
      - "Unmatched rows from both tables simultaneously"
      - "The Cartesian product of both tables"
    correctIndex: 2
    feedback: "FULL OUTER JOIN includes unmatched rows from both tables — left-only rows (with NULLs for right columns) AND right-only rows (with NULLs for left columns). LEFT JOIN only preserves unmatched left-table rows. RIGHT JOIN only preserves unmatched right-table rows. FULL OUTER JOIN combines both, plus the matched rows."
  - type: MULTIPLE_CHOICE
    question: "Which database does NOT natively support FULL OUTER JOIN?"
    options:
      - "PostgreSQL"
      - "SQL Server"
      - "MySQL"
      - "Oracle"
    correctIndex: 2
    feedback: "MySQL does not support FULL OUTER JOIN. The workaround is LEFT JOIN UNION ALL RIGHT JOIN WHERE left.id IS NULL. PostgreSQL, SQL Server, Oracle, and SQLite all support FULL OUTER JOIN natively."
retrieval:
  recall: "Write a FULL OUTER JOIN query between two employee tables from different systems to find discrepancies."
  explain: "Explain the difference between all four join types (INNER, LEFT, RIGHT, FULL OUTER) in one concise paragraph."
  mistakeId:
    code: "using LEFT JOIN when comparing two datasets to find all discrepancies on both sides"
    answer: "LEFT JOIN only shows rows missing from the right table (unmatched in the right). It misses rows that exist only in the right table but not in the left. For complete reconciliation — finding gaps on EITHER side — use FULL OUTER JOIN. Then filter WHERE left.id IS NULL OR right.id IS NULL to extract all discrepancies. LEFT JOIN gives you a half-picture; FULL OUTER JOIN gives you the complete comparison."
---

# Hook

LEFT JOIN preserves the left table. RIGHT JOIN preserves the right table. FULL OUTER JOIN preserves both tables simultaneously — every row from both sides appears, whether matched or not. NULLs fill in wherever there is no corresponding row.

FULL OUTER JOIN is the tool for data reconciliation: "show me everything in both datasets, and highlight where they don't align."

# Lore Introduction

"Two membership ledgers," the Auditor said. "The Main Registry and the Branch Registry. They should contain the same members, but we suspect there are discrepancies." Master Selvaris wrote a FULL OUTER JOIN between the two tables on member_id. "Every member in either ledger appears," she said. "Members in both appear as full rows. Members in the Main Registry with no Branch record show NULL on the Branch side. Members in the Branch Registry with no Main record show NULL on the Main side." The Auditor examined the result. "Twelve members in Main but not Branch. Seven in Branch but not Main." Selvaris filtered for the NULLs on each side. "Your discrepancy report."

# Core Learning

## Concept Introduction

### FULL OUTER JOIN Syntax

```sql
SELECT
    COALESCE(a.id, b.id) AS id,
    a.name AS name_in_table_a,
    b.name AS name_in_table_b
FROM table_a AS a
FULL OUTER JOIN table_b AS b ON a.id = b.id;
```

### What FULL OUTER JOIN Returns

```
table_a:        table_b:
id | name       id | name
1  | Alice      2  | Bob
2  | Bob        3  | Carol
3  | Carol      4  | Dave

FULL OUTER JOIN ON a.id = b.id:
a.id | a.name | b.id | b.name
1    | Alice  | NULL | NULL    ← in table_a only
2    | Bob    | 2    | Bob     ← in both (matched)
3    | Carol  | 3    | Carol   ← in both (matched)
NULL | NULL   | 4    | Dave    ← in table_b only
```

### Join Type Comparison

```
INNER JOIN:       only matched rows (2, 3)
LEFT JOIN:        all of table_a + matched (1, 2, 3) — row 4 excluded
RIGHT JOIN:       matched + all of table_b (2, 3, 4) — row 1 excluded
FULL OUTER JOIN:  everything (1, 2, 3, 4)
```

### Finding All Discrepancies with FULL OUTER JOIN

```sql
-- Reconcile two systems: find all rows not in both
SELECT
    COALESCE(sys_a.transaction_id, sys_b.transaction_id) AS transaction_id,
    sys_a.amount AS amount_in_system_a,
    sys_b.amount AS amount_in_system_b
FROM system_a_transactions AS sys_a
FULL OUTER JOIN system_b_transactions AS sys_b
    ON sys_a.transaction_id = sys_b.transaction_id
WHERE sys_a.transaction_id IS NULL    -- in system B only
   OR sys_b.transaction_id IS NULL;   -- in system A only
```

### COALESCE for Combined ID Column

When both tables have the same column (like id), use COALESCE to show the non-NULL value:

```sql
-- Either a.id or b.id is NULL for unmatched rows
-- COALESCE returns the first non-NULL value
SELECT
    COALESCE(a.employee_id, b.employee_id) AS employee_id,
    a.name AS hr_name,
    b.name AS payroll_name
FROM hr_employees AS a
FULL OUTER JOIN payroll_employees AS b ON a.employee_id = b.employee_id;
```

### MySQL Workaround (FULL OUTER JOIN not supported)

```sql
-- MySQL: LEFT JOIN UNION ALL RIGHT JOIN
SELECT a.id, a.name AS a_name, b.name AS b_name
FROM table_a a LEFT JOIN table_b b ON a.id = b.id

UNION ALL

SELECT b.id, a.name AS a_name, b.name AS b_name
FROM table_a a RIGHT JOIN table_b b ON a.id = b.id
WHERE a.id IS NULL;
-- The RIGHT JOIN WHERE a.id IS NULL adds only the right-only rows
-- avoiding duplicating the matched rows already in the LEFT JOIN
```

## Why It Matters

FULL OUTER JOIN keeps unmatched rows from *both* sides, which makes it the natural tool for comparison and reconciliation:

- Comparing two systems' records — billing vs CRM, yesterday's snapshot vs today's — needs rows that exist in either one, matched where possible
- The NULL patterns in the result tell you exactly where the two sides disagree: missing here, missing there, or matched
- Data migration checks and audit jobs lean on this join to prove nothing was lost or invented

It's the rarest join in daily queries but the star of integration work — and a reliable test of whether someone truly understands outer joins.

## Common Mistakes

- **MySQL: FULL OUTER JOIN not supported**: Use the LEFT JOIN UNION ALL RIGHT JOIN workaround. Running `FULL OUTER JOIN` in MySQL is a syntax error.
- **Not using COALESCE on the join key**: For unmatched rows, one side's key is NULL. Use COALESCE(a.id, b.id) to get a non-NULL identifier in the result.
- **Using FULL OUTER JOIN when LEFT JOIN is sufficient**: If you only need unmatched rows from one side, LEFT JOIN + IS NULL is simpler and clearer. FULL OUTER JOIN is for genuine two-sided reconciliation.

## Mental Model

FULL OUTER JOIN is the union of LEFT JOIN and RIGHT JOIN. Imagine two overlapping circles (a Venn diagram). INNER JOIN is the overlap only. LEFT JOIN is the left circle entirely. RIGHT JOIN is the right circle entirely. FULL OUTER JOIN is both circles — all of them. NULLs appear in the parts of each circle that have no overlap — the "left only" section has NULLs for right-table columns; the "right only" section has NULLs for left-table columns.

## Mini Summary

- ✔ `FULL OUTER JOIN` returns all rows from both tables
- ✔ Matched rows merge normally; unmatched rows have NULLs for the non-contributing side
- ✔ Used for data reconciliation — finding gaps on either side
- ✔ MySQL does not support it natively — use `LEFT JOIN UNION ALL RIGHT JOIN WHERE left IS NULL`
- ✔ Use `COALESCE(a.col, b.col)` to get a non-NULL identifier for unmatched rows

# Guided Practice Quest

Work through the guided steps to write a FULL OUTER JOIN, identify matched and unmatched rows in the result, filter for discrepancies, and use COALESCE to handle the NULL join key.

# Solo Practice Quest

Two tables represent products from two different supplier catalogues: `catalogue_a` (product_id, name, price_a) and `catalogue_b` (product_id, name, price_b). Some products are in both, some only in one. Write four queries: (1) FULL OUTER JOIN showing all products from both catalogues with prices from each (NULL where not in that catalogue), (2) products only in catalogue A (not in B), (3) products only in catalogue B (not in A), (4) products in both catalogues but with different prices. For each, explain what the result represents and which rows will have NULLs. Then write the MySQL workaround for query (1).

# Integration

**Mathematics**: FULL OUTER JOIN computes the full outer join in relational algebra: R ⟗ S = (R ⟕ S) ∪ (R ⟖ S). This is equivalent to the symmetric union of the left and right outer joins. In set-theoretic terms, FULL OUTER JOIN corresponds to computing the symmetric difference and union of two sets extended with the join semantics — every element from both sets appears, with null padding for missing complement elements. Venn diagram: INNER = A ∩ B; LEFT = A; RIGHT = B; FULL OUTER = A ∪ B. The result set cardinality: |R ⟗ S| = |R ⟕ S| + |S| - |R ⋈ S| = |R| + |S| - |R ⋈ S|, where the matched rows are not doubled.

**Sciences (Genomics — Database Reconciliation)**: Genomic research compares gene databases across organisms — for example, finding which human genes have orthologues in mice and which do not. `SELECT COALESCE(h.gene_id, m.gene_id) AS gene_id, h.symbol AS human_symbol, m.symbol AS mouse_symbol FROM human_genes h FULL OUTER JOIN mouse_orthologues m ON h.gene_id = m.human_gene_id` reveals: genes conserved in both species (matched rows), human-specific genes (NULLs on the mouse side), and mouse-specific genes (NULLs on the human side). This FULL OUTER JOIN reconciliation is standard in comparative genomics — the same technique used to compare financial systems, product catalogues, and membership registries.

# Lore Conclusion

The Auditor's reconciliation report was complete: nineteen discrepancies across two registries. "Twelve members in the Main Registry with no Branch record," Master Selvaris said. "Seven in the Branch Registry with no Main record. One query, both gaps revealed simultaneously." The Auditor signed off on the report. "I see now why you used FULL OUTER JOIN. LEFT JOIN would have shown me the twelve missing from Branch. But I would never have seen the seven missing from Main." Selvaris nodded. "That is the distinction. LEFT JOIN asks: what is missing on the right? FULL OUTER JOIN asks: what is missing on either side? For reconciliation, you always need the answer to the second question."

---
