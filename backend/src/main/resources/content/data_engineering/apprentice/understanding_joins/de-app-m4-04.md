---
id: de-app-m4-04
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
lesson: right_join
title: "RIGHT JOIN"
sortOrder: 4
difficulty: 2
estimatedMinutes: 20
xpReward: 25
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: low
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m4-03]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains that RIGHT JOIN is the mirror image of LEFT JOIN
    - Explains that any RIGHT JOIN can be rewritten as a LEFT JOIN by swapping table order
    - Identifies when RIGHT JOIN might appear in practice
    - Describes why LEFT JOIN is strongly preferred in practice
    - Reflects on the importance of understanding table order in joins
  keywords: [RIGHT JOIN, mirror, LEFT JOIN, swap, table order, right table, preserve, rewrite, convention]
  modelAnswer: |
    RIGHT JOIN is the mirror image of LEFT JOIN: it preserves all rows from the right (second) table, filling in NULLs for the left-table columns when there is no match. Any RIGHT JOIN can be rewritten as a LEFT JOIN by swapping the table order — they are logically equivalent. In practice, most data engineers avoid RIGHT JOIN entirely and use LEFT JOIN with swapped tables instead, because mixing LEFT and RIGHT JOINs in a multi-table query is confusing. RIGHT JOIN is occasionally useful when adding a table late in a query chain where reordering would be disruptive.
guidedSteps:
  - id: de-app-m4-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which query is equivalent to: `SELECT c.name, o.order_id FROM orders AS o RIGHT JOIN customers AS c ON o.customer_id = c.customer_id`?
    inputConfig:
      options:
        - "SELECT c.name, o.order_id FROM customers AS c LEFT JOIN orders AS o ON c.customer_id = o.customer_id"
        - "SELECT c.name, o.order_id FROM customers AS c INNER JOIN orders AS o ON c.customer_id = o.customer_id"
        - "SELECT c.name, o.order_id FROM orders AS o LEFT JOIN customers AS c ON o.customer_id = c.customer_id"
        - "SELECT c.name, o.order_id FROM customers AS c RIGHT JOIN orders AS o ON c.customer_id = o.customer_id"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SELECT c.name, o.order_id FROM customers AS c LEFT JOIN orders AS o ON c.customer_id = o.customer_id"]
      rejectedFeedback: "RIGHT JOIN on the right table (customers) is equivalent to LEFT JOIN on the same table when it moves to the left (FROM) position. 'orders RIGHT JOIN customers' means customers is fully preserved — the same result as 'customers LEFT JOIN orders'. Swapping the table order and changing RIGHT to LEFT gives the identical result. This is why RIGHT JOIN is rarely needed."
    hint: "To convert RIGHT JOIN to LEFT JOIN, swap the table positions and change RIGHT to LEFT."
    reflectionPrompt: "If you have three tables chained with LEFT JOINs, would introducing a RIGHT JOIN on the third table be confusing? Why?"
  - id: de-app-m4-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      `FROM orders RIGHT JOIN customers ON orders.customer_id = customers.customer_id` preserves all rows from the ________ table.
    inputConfig:
      placeholder: "customers"
    markingRule:
      matchMode: CONTAINS
      accepted: [customers, right, right table]
      rejectedFeedback: "RIGHT JOIN preserves all rows from the RIGHT table — the one listed after the JOIN keyword. In 'orders RIGHT JOIN customers', customers is on the right and is fully preserved. All customers appear in the result, with NULLs in order columns for customers who have no orders. This is identical in result to 'customers LEFT JOIN orders' where customers is on the left."
    hint: "RIGHT JOIN preserves the table that comes after the JOIN keyword."
    reflectionPrompt: "Which table would have NULLs in its columns for unmatched rows — the right table or the left table?"
  - id: de-app-m4-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why most SQL practitioners prefer LEFT JOIN over RIGHT JOIN even when RIGHT JOIN would be natural.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [convention, readable, consistent, confusing, mixed, LEFT JOIN, swap, rewrite, equivalent, clarity]
      rejectedFeedback: "Any RIGHT JOIN can be rewritten as a LEFT JOIN by swapping the table order — they are equivalent. Using LEFT JOIN consistently means all 'preserving' joins read in the same direction, making multi-table queries easier to reason about. A query mixing LEFT JOINs and RIGHT JOINs is harder to read — you have to track which table each join type preserves. Most teams adopt a convention of LEFT JOIN only for clarity."
    hint: "Think about readability in a long query with five joined tables — mixing LEFT and RIGHT is harder to follow."
    reflectionPrompt: "If a colleague sends you a query using RIGHT JOIN, what is the first thing you would do to make it easier to understand?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does RIGHT JOIN return that INNER JOIN does not?"
    options:
      - "All rows from the left table, even without matches"
      - "All rows from the right table, even without matches"
      - "All rows from both tables, with NULLs for unmatched rows on both sides"
      - "Exactly the same as INNER JOIN"
    correctIndex: 1
    feedback: "RIGHT JOIN preserves all rows from the right (second) table. Rows from the right table with no match in the left table appear with NULL in the left-table columns. INNER JOIN would exclude these unmatched rows entirely. LEFT JOIN preserves the left table; RIGHT JOIN preserves the right table; FULL OUTER JOIN preserves both."
  - type: MULTIPLE_CHOICE
    question: "Which of these is true about RIGHT JOIN?"
    options:
      - "It cannot be replaced by LEFT JOIN"
      - "It is more efficient than LEFT JOIN"
      - "It can always be rewritten as a LEFT JOIN by swapping table order"
      - "It is only available in MySQL"
    correctIndex: 2
    feedback: "Any RIGHT JOIN is logically equivalent to a LEFT JOIN with the table order swapped. `A RIGHT JOIN B ON ...` = `B LEFT JOIN A ON ...`. Most teams adopt a LEFT JOIN-only convention for consistency and readability. RIGHT JOIN has no performance advantage — query optimisers treat them identically."
retrieval:
  recall: "Rewrite this query using LEFT JOIN instead: SELECT d.name, e.name FROM employees e RIGHT JOIN departments d ON e.department_id = d.department_id"
  explain: "Explain why RIGHT JOIN is almost never necessary in practice."
  mistakeId:
    code: "mixing LEFT JOIN and RIGHT JOIN in the same multi-table query"
    answer: "Mixing LEFT and RIGHT JOINs in a single query is valid SQL but difficult to reason about — you must track which table each join type preserves, and the result can be non-intuitive. The fix is to rewrite all RIGHT JOINs as LEFT JOINs by swapping table order. This makes the query read consistently: all 'preserving' joins flow in the same direction, and the FROM table is always the anchor. Most teams enforce this as a convention."
---

# Hook

RIGHT JOIN is the mirror image of LEFT JOIN: it preserves all rows from the right table instead of the left. Every row from the right table appears in the result; unmatched left-table columns are NULL.

In practice, RIGHT JOIN is almost never used — because any RIGHT JOIN can be rewritten as a LEFT JOIN by swapping table order. Understanding RIGHT JOIN helps you read others' code, but you will almost always write LEFT JOIN instead.

# Lore Introduction

"This query uses RIGHT JOIN," the Apprentice said, handing Master Selvaris a query from a legacy archive system. "I have never seen it before." Selvaris examined it. "RIGHT JOIN preserves the right table — the same way LEFT JOIN preserves the left. Watch." She rewrote it with the tables swapped and LEFT instead of RIGHT. "Identical result. Same rows, same NULLs, same meaning." She handed it back. "Most data engineers write LEFT JOIN always, because consistency is easier to read than a mix of directions. When you see RIGHT JOIN, rewrite it. The query gets clearer immediately."

# Core Learning

## Concept Introduction

### RIGHT JOIN Syntax and Behaviour

```sql
-- RIGHT JOIN preserves the RIGHT table (customers)
SELECT c.name, o.order_id, o.total_amount
FROM orders AS o
RIGHT JOIN customers AS c ON o.customer_id = c.customer_id;
```

```
orders RIGHT JOIN customers result:
name  | order_id | amount        (same result as customers LEFT JOIN orders)
Alice | 101      | 50
Alice | 102      | 120
Bob   | 103      | 75
Carol | NULL     | NULL    ← preserved from right table (customers), no order match
Dave  | NULL     | NULL    ← preserved from right table (customers), no order match
```

### RIGHT JOIN = LEFT JOIN with Tables Swapped

These two queries produce identical results:

```sql
-- RIGHT JOIN: orders on left, customers on right (customers preserved)
SELECT c.name, o.order_id
FROM orders AS o
RIGHT JOIN customers AS c ON o.customer_id = c.customer_id;

-- LEFT JOIN equivalent: swap the tables
SELECT c.name, o.order_id
FROM customers AS c
LEFT JOIN orders AS o ON c.customer_id = o.customer_id;
```

Always prefer the LEFT JOIN version — it is more readable and consistent.

### When RIGHT JOIN Appears in Practice

RIGHT JOIN occasionally appears when adding a table to an existing query chain where reordering would require restructuring:

```sql
-- Three-table chain where the third table needs to be preserved
SELECT a.col, b.col, c.col
FROM table_a a
JOIN table_b b ON a.id = b.a_id
RIGHT JOIN table_c c ON b.id = c.b_id;
-- Here RIGHT JOIN on table_c preserves all rows from table_c

-- Cleaner rewrite using LEFT JOIN only:
SELECT a.col, b.col, c.col
FROM table_c c
LEFT JOIN table_b b ON c.b_id = b.id
LEFT JOIN table_a a ON b.a_id = a.id;
```

### The Convention: LEFT JOIN Only

Most SQL style guides and teams adopt a convention of never using RIGHT JOIN:

- All "preserving" joins flow left-to-right
- Easier to reason about which rows are preserved
- No mental overhead tracking the direction of each join
- The anchor table (FROM) is always the one whose rows are guaranteed to appear

## Why It Matters

RIGHT JOIN completes the symmetry of outer joins, and understanding it sharpens your grasp of joins in general:

- It keeps every row from the *right* table — exactly a LEFT JOIN with the tables swapped
- You will rarely write one (convention favours LEFT JOIN), but you *will* read them in inherited code and need to know precisely what they do
- Reasoning through "which side is preserved?" cements the mental model that makes all outer joins predictable

Knowing RIGHT JOIN also means knowing *why* teams avoid it — a style judgement you'll be expected to articulate in code review.

## Common Mistakes

- **Using RIGHT JOIN unnecessarily**: Any RIGHT JOIN can be a LEFT JOIN. Swap the tables and change the keyword — the result is identical.
- **Mixing LEFT and RIGHT JOIN in a long query**: This creates confusion about which rows are preserved at each step. Rewrite to use LEFT JOIN exclusively.
- **Confusing which table is "right"**: The right table is the one after the JOIN keyword. In `FROM A JOIN B`, A is left and B is right.

## Mental Model

RIGHT JOIN is a mirror of LEFT JOIN. If LEFT JOIN says "keep everything from the table I listed first", RIGHT JOIN says "keep everything from the table I listed second". Since you choose which table to list first, you can always choose an order that makes LEFT JOIN the natural choice. RIGHT JOIN exists because SQL lets you write queries in multiple ways — but the readable convention is to always use LEFT JOIN with appropriate table ordering.

## Mini Summary

- ✔ `RIGHT JOIN` preserves all rows from the right (second) table
- ✔ Any `RIGHT JOIN` can be rewritten as `LEFT JOIN` by swapping table order
- ✔ In practice, most engineers use `LEFT JOIN` exclusively for consistency
- ✔ Right-to-left table order in `FROM ... RIGHT JOIN ...` means the second table is preserved
- ✔ When reading code with `RIGHT JOIN`, mentally rewrite it as `LEFT JOIN` to understand it

# Guided Practice Quest

Work through the guided steps to identify which table RIGHT JOIN preserves, rewrite a RIGHT JOIN as a LEFT JOIN, and understand why LEFT JOIN is preferred.

# Solo Practice Quest

You are given three queries that use RIGHT JOIN: (1) `SELECT p.name, r.rating FROM reviews r RIGHT JOIN products p ON r.product_id = p.product_id`, (2) `SELECT e.name, d.name FROM departments d RIGHT JOIN employees e ON e.department_id = d.department_id`, (3) `SELECT ol.quantity, p.name FROM products p RIGHT JOIN order_lines ol ON ol.product_id = p.product_id`. For each: (a) identify which table is preserved, (b) rewrite using LEFT JOIN instead, (c) describe what each query returns (including what the NULLs represent), and (d) explain whether there are rows with NULLs in this particular join and what they mean.

# Integration

**Mathematics**: RIGHT JOIN computes the right outer join in relational algebra: R ⟖ S. For each tuple s ∈ S, if there exists r ∈ R such that r[A] = s[A], the tuple (r, s) appears. If no such r exists, then (null, s) appears. This is the symmetric counterpart of the left outer join: R ⟕ S ≠ R ⟖ S in general, but R ⟕ S = S ⟖ R (swapping the relation order converts left to right outer join). The full outer join R ⟗ S = (R ⟕ S) ∪ (R ⟖ S) — both outer joins combined, which is the topic of the next lesson. This symmetry property explains why RIGHT JOIN is redundant: any right outer join is a left outer join with the argument order reversed.

**Sciences (Genetics — Cross-referencing)**: Genetic databases cross-reference gene identifiers between naming systems (HGNC symbols, Entrez IDs, Ensembl IDs). A query finding all Ensembl gene IDs that may not have an HGNC symbol: `SELECT hgnc.symbol, ens.ensembl_id FROM hgnc_genes hgnc RIGHT JOIN ensembl_genes ens ON hgnc.gene_id = ens.gene_id WHERE hgnc.symbol IS NULL` — finding Ensembl genes with no HGNC mapping. This is the RIGHT JOIN IS NULL pattern — equivalent to a LEFT JOIN with tables swapped. Bioinformaticians frequently rewrite such queries using LEFT JOINs for consistency, exactly as SQL practitioners do.

# Lore Conclusion

"The RIGHT JOIN query now reads as a LEFT JOIN," the Apprentice said, reviewing the rewrite. "Same result, but I can understand it immediately." Master Selvaris nodded. "In the Archive, we have a convention: LEFT JOIN, always. The anchor table — the one you care most about preserving — goes in the FROM clause. Everything joins to it. No ambiguity about direction." She closed the legacy query. "You will encounter RIGHT JOIN in old code, in other teams' work, in Stack Overflow examples. Know what it means, rewrite it, move on. It is not wrong — it is just inconsistent with the convention that makes queries readable."

---
