---
id: de-app-m3-12
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m3
moduleTitle: "Module 3: SQL Foundations"
moduleGlyph: "🔍"
moduleSortOrder: 3
topicSlug: aggregation
topicTitle: "Aggregation"
topicSortOrder: 3
lesson: avg
title: "AVG"
sortOrder: 12
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m3-11]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Uses AVG to calculate the mean of a numeric column
    - Explains that AVG ignores NULL values (does not treat them as zero)
    - Describes the difference between AVG and SUM/COUNT
    - Uses AVG with GROUP BY to compute per-group averages
    - Reflects on the risk of NULL exclusion distorting AVG results
  keywords: [AVG, average, mean, NULL, GROUP BY, SUM, COUNT, distort, exclude]
  modelAnswer: |
    AVG computes the arithmetic mean of a numeric column. It is equivalent to SUM(column) / COUNT(column) — it sums non-NULL values and divides by the count of non-NULL rows. Because AVG ignores NULLs, the result can be misleading if NULLs represent "zero" rather than "unknown" — the average is computed over a smaller, potentially unrepresentative sample. AVG with GROUP BY computes a mean per group. For meaningful averages, understanding the NULL semantics of the column is essential.
guidedSteps:
  - id: de-app-m3-12-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A `reviews` table has 5 rows with scores: 4, 5, NULL, 3, NULL. What does `SELECT AVG(score) FROM reviews` return?
    inputConfig:
      options:
        - "2.4 (sum of all including NULLs treated as 0, divided by 5)"
        - "4.0 (average of 4, 5, 3)"
        - "NULL (because NULLs are present)"
        - "3.0 (average of 4, 5, NULL, 3, NULL)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["4.0 (average of 4, 5, 3)"]
      rejectedFeedback: "AVG ignores NULL values. It computes SUM(non-NULL values) / COUNT(non-NULL values) = (4 + 5 + 3) / 3 = 12 / 3 = 4.0. The two NULL rows are excluded from both the sum and the count. If NULLs represented 'no review given' (i.e. a score of 0), the correct average should be (4+5+0+3+0)/5 = 2.4 — but AVG cannot know this. Understanding the business meaning of NULL is critical."
    hint: "AVG skips NULLs from both the numerator (sum) and denominator (count)."
    reflectionPrompt: "How would you calculate the average score if NULL means 'did not rate' and should count as 0?"
  - id: de-app-m3-12-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To calculate the average order value per customer, you write: `SELECT customer_id, AVG(total_amount) FROM orders GROUP BY ________;`
    inputConfig:
      placeholder: "customer_id"
    markingRule:
      matchMode: CONTAINS
      accepted: [customer_id]
      rejectedFeedback: "GROUP BY customer_id groups rows by customer, then AVG(total_amount) computes the mean order value within each customer's group. Without GROUP BY, AVG returns a single grand average across all orders. Adding GROUP BY turns the global average into a per-customer average."
    hint: "GROUP BY the column you want to compute a separate average for."
    reflectionPrompt: "How would you find only customers whose average order value exceeds 100?"
  - id: de-app-m3-12-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why AVG(score) and SUM(score) / COUNT(*) can return different results on the same table.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [NULL, count, rows, exclude, denominator, different, all rows, non-null]
      rejectedFeedback: "AVG is equivalent to SUM(score) / COUNT(score) — both ignoring NULLs. But COUNT(*) counts ALL rows, including those with NULL scores. So SUM(score) / COUNT(*) divides by a larger denominator (all rows), producing a lower result than AVG when NULLs are present. If NULL means 'zero score', then SUM(score) / COUNT(*) is actually the more honest calculation."
    hint: "The difference is in the denominator. AVG uses COUNT(column); manual division might use COUNT(*)."
    reflectionPrompt: "Which version — AVG or SUM/COUNT(*) — better represents the 'average score per product' when some products have no ratings?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is AVG mathematically equivalent to?"
    options:
      - "SUM(col) / COUNT(*)"
      - "SUM(col) / COUNT(col)"
      - "MAX(col) + MIN(col) / 2"
      - "COUNT(col) / SUM(col)"
    correctIndex: 1
    feedback: "AVG(col) = SUM(col) / COUNT(col). Both the sum and the count exclude NULLs. COUNT(*) counts all rows including those with NULLs — using it as the denominator would give a lower result than AVG when NULLs are present. (MAX+MIN)/2 is the midpoint, not the mean."
  - type: MULTIPLE_CHOICE
    question: "A products table has 3 categories. Which query shows the average price per category?"
    options:
      - "SELECT AVG(price) FROM products;"
      - "SELECT category, AVG(price) FROM products;"
      - "SELECT category, AVG(price) FROM products GROUP BY category;"
      - "SELECT AVG(price) FROM products GROUP BY category ORDER BY category;"
    correctIndex: 2
    feedback: "To get a separate average per category, you need both category in the SELECT list (to label each row) and GROUP BY category (to divide the data into groups). Option B is an error in standard SQL — you cannot mix a non-aggregate column (category) with an aggregate (AVG) without GROUP BY. Option D is missing 'category' in the SELECT list, making the result unidentifiable."
retrieval:
  recall: "Write a query that calculates the average salary per department."
  explain: "Explain why AVG and SUM/COUNT(*) can produce different results, and which is more accurate when NULLs mean 'zero'."
  mistakeId:
    code: "SELECT AVG(rating) FROM products to find the average rating including unrated products as 0"
    answer: "AVG(rating) excludes NULL rows from both the sum and count, so unrated products (NULL rating) are ignored entirely. This inflates the average. To include unrated products as 0, use AVG(COALESCE(rating, 0)) — this substitutes 0 for NULLs before computing the average, including those rows in both the sum and denominator."
---

# Hook

"What is the average order value?" "What is the average rating of our products?" "Which department has the highest average salary?" These questions don't want a total or a count — they want a representative typical value. That is AVG.

AVG is the most commonly misunderstood aggregate function, because its handling of NULL values can produce subtly wrong results if you are not careful about what NULL means in your data.

# Lore Introduction

"The Guild Master wants to know the average fee collected per contract," the Treasurer said. "Not the total — the average, to benchmark against competitors." Master Selvaris wrote: `SELECT AVG(fee_amount) FROM contracts WHERE status = 'completed';`. "AVG divides the sum by the count," she said. "But only non-NULL rows count in both." She paused. "Some contracts have NULL fees — contracts where payment was waived. If NULL means 'zero fee collected', AVG is giving you the wrong number." She rewrote it: `SELECT AVG(COALESCE(fee_amount, 0)) FROM contracts WHERE status = 'completed';`. "Now waived contracts contribute a fee of zero to the average. The difference matters."

# Core Learning

## Concept Introduction

### Basic AVG

```sql
-- Average order value
SELECT AVG(total_amount) AS avg_order_value
FROM orders;

-- Average product rating
SELECT AVG(rating) AS avg_rating
FROM product_reviews;

-- Average delivery time in days
SELECT AVG(CURRENT_DATE - order_date) AS avg_days_to_today
FROM orders
WHERE status = 'pending';
```

### AVG and NULL Behaviour

```sql
-- scores: 4, 5, NULL, 3, NULL
SELECT AVG(score) FROM reviews;
-- Returns 4.0 = (4 + 5 + 3) / 3   — NULLs excluded from both sum and count

-- If NULL means "0 score", substitute before averaging:
SELECT AVG(COALESCE(score, 0)) FROM reviews;
-- Returns 2.4 = (4 + 5 + 0 + 3 + 0) / 5

-- AVG vs SUM/COUNT(*) — they can differ when NULLs are present
SELECT
    AVG(score)              AS avg_ignoring_nulls,   -- 4.0
    SUM(score) / COUNT(*)   AS avg_including_nulls    -- 2.4
FROM reviews;
```

### AVG with GROUP BY

```sql
-- Average salary per department
SELECT department_id, AVG(monthly_salary) AS avg_salary
FROM employees
GROUP BY department_id
ORDER BY avg_salary DESC;

-- Average order value per customer
SELECT customer_id, AVG(total_amount) AS avg_order_value
FROM orders
GROUP BY customer_id;

-- Average rating per product category
SELECT category, ROUND(AVG(rating), 2) AS avg_rating, COUNT(*) AS review_count
FROM product_reviews
JOIN products USING (product_id)
GROUP BY category
ORDER BY avg_rating DESC;
```

### Rounding AVG Results

```sql
-- AVG often produces many decimal places
SELECT AVG(price);            -- 24.666666...

-- ROUND to a sensible precision
SELECT ROUND(AVG(price), 2) AS avg_price FROM products;   -- 24.67
SELECT ROUND(AVG(rating), 1) AS avg_rating FROM reviews;  -- 4.2
```

### Combining AVG, SUM, COUNT

```sql
-- Dashboard summary for orders
SELECT
    COUNT(*)                    AS total_orders,
    SUM(total_amount)           AS total_revenue,
    ROUND(AVG(total_amount), 2) AS avg_order_value,
    MIN(total_amount)           AS smallest_order,
    MAX(total_amount)           AS largest_order
FROM orders
WHERE status = 'completed';
```

## Why It Matters

Averages drive decisions everywhere — average order value, average response time, average rating — yet AVG is the most commonly *misread* aggregate:

- AVG ignores NULLs, so "average rating" only reflects people who rated; that's often not what stakeholders assume
- A few extreme values can drag an average far from the typical case, hiding problems or inventing them
- Averaging averages (e.g. per-region averages) without weighting gives a wrong overall figure

Understanding what AVG actually computes lets you spot misleading numbers before they reach a report — and explain why a median sometimes tells the truer story.

## Common Mistakes

- **NULL treated as zero by AVG**: AVG excludes NULLs. If NULL represents zero, use COALESCE before averaging.
- **AVG of integers returns integer in some databases**: In PostgreSQL, AVG of integers returns a decimal. In some others it truncates. Cast to ensure decimal output: `AVG(CAST(col AS DECIMAL))`.
- **Averaging an average (double aggregation)**: `AVG(AVG(score))` is not valid and does not compute a weighted average. Use SUM(total_score) / SUM(review_count) for weighted averages.
- **Not rounding**: Raw AVG output is often 10+ decimal places. Always ROUND for presentation.

## Mental Model

Think of AVG as a fair distribution problem. If you poured all the values into a bucket and distributed them equally across all non-NULL rows, what would each row get? That is the average. NULLs are absent from the room — they neither contribute to the total nor claim a share of the distribution. If they should claim a share (because NULL means "zero"), you must explicitly fill their bucket with COALESCE before AVG.

## Mini Summary

- ✔ `AVG(column)` — arithmetic mean of non-NULL values
- ✔ Equivalent to `SUM(col) / COUNT(col)` — both ignore NULLs
- ✔ If NULL means zero, use `AVG(COALESCE(col, 0))` instead
- ✔ Add `GROUP BY` to compute a mean per group
- ✔ Use `ROUND(AVG(col), 2)` to control decimal places in output

# Guided Practice Quest

Work through the guided steps to calculate averages with NULL considerations, use GROUP BY for per-group averages, and understand the difference between AVG and SUM/COUNT(*).

# Solo Practice Quest

A `product_reviews` table has: `review_id`, `product_id`, `customer_id`, `rating` (1–5, may be NULL for unrated), `review_date`, `verified_purchase` (boolean). Write five queries: (1) average rating per product, (2) average rating per product treating unrated reviews as 0, (3) the difference between the two averages for each product (shows the NULL impact), (4) products with an average rating above 4.0 and at least 10 reviews, (5) monthly average rating trend for one specific product. For each, explain what it measures and how NULL is handled.

# Integration

**Mathematics**: AVG computes the arithmetic mean: x̄ = (Σᵢ xᵢ) / n, where n is the count of non-NULL values. This is one of three classical measures of central tendency, alongside median (middle value) and mode (most frequent value). SQL provides AVG for the arithmetic mean natively; median and mode require more complex expressions (PERCENTILE_CONT and window functions). The arithmetic mean is sensitive to outliers — a few extremely large orders inflate AVG(order_value) above what is "typical". This is why statisticians often prefer the median for skewed distributions — a limitation to be aware of when interpreting SQL AVG results.

**Sciences (Biology — Population Studies)**: Ecologists use mean measurements extensively: average body mass per species, average litter size per population, average lifespan by habitat. These correspond exactly to AVG with GROUP BY in SQL. The NULL issue appears directly: an animal that was not weighed has NULL weight, not zero weight — including it as zero would distort the population mean downward. Excluding it (AVG's default behaviour) is biologically correct — the sample mean is computed over measured individuals only. This is the same decision SQL's AVG makes automatically.

# Lore Conclusion

The Guild Master's benchmark report showed average fees per contract type. Standard contracts: 450 gold. Expedited contracts: 720 gold. Waived contracts: 0 gold (after COALESCE). "The first version — excluding waived contracts entirely — made us look more profitable than we are," the Treasurer said. "The corrected version shows our true average." Master Selvaris nodded. "AVG is mathematically simple. The complexity is in deciding what NULL means." She closed the report. "Every time you run AVG, ask yourself: should those NULL rows count as zero? If yes, use COALESCE. If no, AVG's default is correct. One question. It changes everything."

---
