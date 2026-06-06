---
id: de-app-m3-13
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
lesson: min_max
title: "MIN and MAX"
sortOrder: 13
difficulty: 1
estimatedMinutes: 20
xpReward: 25
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: low
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m3-12]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Uses MIN and MAX correctly on numeric, date, and string columns
    - Explains that MIN and MAX ignore NULL values
    - Combines MIN and MAX with GROUP BY for per-group extremes
    - Uses MIN/MAX on dates to find earliest/latest events
    - Reflects on when MIN/MAX is more useful than ORDER BY + LIMIT 1
  keywords: [MIN, MAX, extreme, earliest, latest, NULL, GROUP BY, date, string, aggregate]
  modelAnswer: |
    MIN returns the smallest non-NULL value in a column; MAX returns the largest. Both work on numeric, string (alphabetical order), and date columns. NULLs are ignored. With GROUP BY, MIN and MAX return the extreme value per group rather than the global extreme. MIN/MAX are more efficient than ORDER BY + LIMIT 1 in aggregate queries because they avoid sorting — and they combine naturally with other aggregates in a single query without requiring a subquery.
guidedSteps:
  - id: de-app-m3-13-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A `products` table has prices: 10.00, 45.00, NULL, 8.50, 22.00. What does `SELECT MIN(price) FROM products` return?
    inputConfig:
      options:
        - "NULL"
        - "8.50"
        - "0"
        - "10.00"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["8.50"]
      rejectedFeedback: "MIN ignores NULL values and returns the smallest non-NULL value. Among 10.00, 45.00, 8.50, and 22.00, the minimum is 8.50. NULL is not a value — it is unknown — so it cannot be the minimum or maximum. MIN and MAX both behave this way."
    hint: "MIN ignores NULLs and finds the smallest value among non-NULL rows."
    reflectionPrompt: "What would MIN return if ALL values in the column were NULL?"
  - id: de-app-m3-13-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To find the earliest order date for each customer, you write: `SELECT customer_id, ________(order_date) AS first_order FROM orders GROUP BY customer_id;`
    inputConfig:
      placeholder: "MIN"
    markingRule:
      matchMode: CONTAINS
      accepted: [MIN, min]
      rejectedFeedback: "MIN(order_date) finds the earliest (smallest) date within each customer group. Dates are ordered chronologically, so MIN returns the earliest date. MAX(order_date) would return the most recent date. This pattern — MIN(date) per group — is the SQL way to find 'when did each X first happen?'"
    hint: "Earlier dates are 'smaller' in chronological order, so use MIN to find the earliest."
    reflectionPrompt: "How would you find both the first and last order date for each customer in a single query?"
  - id: de-app-m3-13-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why `SELECT MIN(price) FROM products` is preferable to `SELECT price FROM products ORDER BY price ASC LIMIT 1` when you need the minimum as part of a larger aggregation query.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [aggregate, combine, single query, subquery, MIN, MAX, COUNT, SUM, AVG, together, alongside]
      rejectedFeedback: "MIN can be combined with other aggregate functions (COUNT, SUM, AVG, MAX) in a single SELECT query: SELECT MIN(price), MAX(price), AVG(price), COUNT(*) FROM products — one scan, one result row. ORDER BY + LIMIT 1 is a separate query that returns a full row and cannot be combined with other aggregates without a subquery. For aggregation purposes, MIN/MAX are simpler, clearer, and more composable."
    hint: "MIN can sit alongside COUNT, SUM, and AVG in the same SELECT. ORDER BY + LIMIT 1 cannot."
    reflectionPrompt: "Write a single query that shows the minimum, maximum, average, and count of prices for each product category."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `SELECT MAX(created_at) FROM users` return?"
    options:
      - "The user who signed up first"
      - "The most recent signup date"
      - "The oldest signup date"
      - "The number of users"
    correctIndex: 1
    feedback: "MAX on a date column returns the largest (most recent) date value. The most recently signed-up user has the largest (newest) created_at date. MIN(created_at) would return the earliest signup date."
  - type: MULTIPLE_CHOICE
    question: "Which query finds the highest-paying and lowest-paying employee in each department?"
    options:
      - "SELECT department_id, MAX(salary), MIN(salary) FROM employees;"
      - "SELECT department_id, MAX(salary), MIN(salary) FROM employees GROUP BY department_id;"
      - "SELECT MAX(salary) FROM employees GROUP BY department_id;"
      - "SELECT department_id FROM employees WHERE salary = MAX(salary);"
    correctIndex: 1
    feedback: "To get the max and min per department, you need both the aggregate functions (MAX, MIN) and GROUP BY department_id to partition the data by department. Option A is an error — mixing department_id (non-aggregate) with MAX/MIN without GROUP BY. Option C is correct syntax but missing MIN and department_id label. Option D is an error — you cannot use an aggregate in a WHERE clause."
retrieval:
  recall: "Write a query showing the earliest and latest order date per customer."
  explain: "Explain how MIN and MAX work on string columns — what ordering do they use?"
  mistakeId:
    code: "WHERE salary = MAX(salary) to find the highest-paid employee"
    answer: "Aggregate functions like MAX() cannot be used directly in a WHERE clause — WHERE runs before aggregation. The correct approach is a subquery: WHERE salary = (SELECT MAX(salary) FROM employees). Alternatively, ORDER BY salary DESC LIMIT 1 works for a single result. HAVING can filter after aggregation but only within a GROUP BY query."
---

# Hook

"What is our cheapest product?" "When did this customer first order?" "Which employee earns the most?" "What is the temperature range recorded this year?" These questions want the extreme value — the minimum or maximum — from a set of data.

MIN and MAX are the simplest aggregates: they scan a column and return the boundary values. They work on numbers, dates, and even strings.

# Lore Introduction

"The Archivist wants to know the oldest scroll in the collection and the most recent acquisition," the Librarian said. Master Selvaris wrote two values in a single query: `SELECT MIN(acquired_date) AS oldest, MAX(acquired_date) AS newest FROM scrolls;`. "One scan. Two answers." She then added GROUP BY collection_id. "Now we have the date range for each collection — when it started and when it was last updated." The Librarian examined the results. "How do you know these are correct and not just a sorted query?" Selvaris pointed to the execution plan. "MIN and MAX do not sort. They scan once and track the running extreme. Faster than sorting, and composable with every other aggregate."

# Core Learning

## Concept Introduction

### Basic MIN and MAX

```sql
-- Cheapest and most expensive product
SELECT MIN(price) AS cheapest, MAX(price) AS most_expensive
FROM products;

-- Earliest and latest order
SELECT MIN(order_date) AS first_order, MAX(order_date) AS latest_order
FROM orders;

-- Combined summary
SELECT
    COUNT(*)                    AS total_products,
    MIN(price)                  AS min_price,
    MAX(price)                  AS max_price,
    MAX(price) - MIN(price)     AS price_range,
    ROUND(AVG(price), 2)        AS avg_price
FROM products;
```

### MIN and MAX on Different Data Types

```sql
-- Numeric: standard numeric ordering
SELECT MIN(score), MAX(score) FROM exam_results;   -- 0 to 100

-- Date/Timestamp: chronological ordering
SELECT MIN(created_at) AS first_signup, MAX(created_at) AS latest_signup
FROM users;

-- String: alphabetical (lexicographic) ordering
SELECT MIN(last_name), MAX(last_name) FROM employees;
-- MIN = first alphabetically, MAX = last alphabetically
-- Useful for: finding the first/last item alphabetically
```

### NULL Behaviour

```sql
-- Both MIN and MAX ignore NULLs
-- Values: 10, NULL, 5, NULL, 15 → MIN = 5, MAX = 15
SELECT MIN(score), MAX(score) FROM results;

-- If ALL values are NULL, both return NULL
SELECT MIN(score) FROM results WHERE category = 'uncategorised';
-- Returns NULL if no non-NULL scores exist
```

### MIN and MAX with GROUP BY

```sql
-- Price range per category
SELECT category, MIN(price) AS cheapest, MAX(price) AS most_expensive
FROM products
GROUP BY category
ORDER BY category;

-- First and last order per customer (customer history)
SELECT
    customer_id,
    MIN(order_date) AS first_order_date,
    MAX(order_date) AS most_recent_order_date,
    COUNT(*)        AS total_orders
FROM orders
GROUP BY customer_id;

-- Temperature range per month
SELECT
    EXTRACT(YEAR FROM recorded_at)  AS year,
    EXTRACT(MONTH FROM recorded_at) AS month,
    MIN(temperature_c)              AS min_temp,
    MAX(temperature_c)              AS max_temp,
    AVG(temperature_c)              AS avg_temp
FROM weather_readings
GROUP BY EXTRACT(YEAR FROM recorded_at), EXTRACT(MONTH FROM recorded_at)
ORDER BY year, month;
```

### MIN/MAX vs ORDER BY + LIMIT

```sql
-- For a single extreme value, both work:
SELECT MIN(price) FROM products;
SELECT price FROM products ORDER BY price ASC LIMIT 1;   -- equivalent result

-- But MIN/MAX composes with other aggregates in one query:
SELECT MIN(price), MAX(price), AVG(price), COUNT(*) FROM products;

-- ORDER BY + LIMIT requires a subquery to combine with other aggregates:
SELECT
    (SELECT MIN(price) FROM products) AS min_price,
    (SELECT MAX(price) FROM products) AS max_price,
    AVG(price) AS avg_price,
    COUNT(*) AS total
FROM products;
-- Four scans vs one scan — less efficient
```

## Common Mistakes

- **Using MIN/MAX in WHERE**: `WHERE salary = MAX(salary)` is an error. Use a subquery: `WHERE salary = (SELECT MAX(salary) FROM employees)`.
- **Expecting MIN on a string to mean "shortest"**: MIN returns the first alphabetically, not the shortest string. `MIN('z', 'aa')` returns 'aa' (alphabetically first), even though 'z' is shorter.
- **Forgetting GROUP BY for per-group extremes**: `SELECT department, MAX(salary) FROM employees` is an error without GROUP BY.
- **Assuming MAX(date) is the most recently created row**: It is the row with the latest date value — but if data was inserted out of chronological order, the row with the latest date may not have the highest id or most recent insert timestamp.

## Mental Model

Think of MIN and MAX as scouts running to the ends of a sorted line. MIN runs to the front (smallest value); MAX runs to the back (largest). They don't sort the entire line — they just find the boundary. GROUP BY is like splitting everyone into separate rooms and sending a scout to each room's boundaries. NULLs are people who stepped out of line — the scouts don't count them.

## Mini Summary

- ✔ `MIN(col)` — smallest non-NULL value; `MAX(col)` — largest non-NULL value
- ✔ Works on numbers, dates, and strings (alphabetical for strings)
- ✔ Both ignore NULLs; return NULL only if all values are NULL
- ✔ Add `GROUP BY` to find the extreme per group
- ✔ Composes with COUNT, SUM, AVG in a single query — no subquery needed

# Guided Practice Quest

Work through the guided steps to use MIN and MAX on numeric and date columns, combine them with other aggregates, and apply GROUP BY for per-group extremes.

# Solo Practice Quest

An `employee_salaries` table has: `employee_id`, `department_id`, `hire_date`, `salary`, `last_review_date` (may be NULL). Write five queries: (1) the highest and lowest salary across the whole company, (2) the highest and lowest salary per department, (3) the longest-serving employee per department (earliest hire_date), (4) employees in departments where the salary range (MAX - MIN) exceeds 20,000, (5) a comprehensive department summary: department_id, headcount, min/max/avg salary, and date of most recent review. For each, explain the business question it answers.

# Integration

**Mathematics**: MIN and MAX compute the infimum and supremum of a finite set — the greatest lower bound and least upper bound. For a finite set of real numbers {x₁, x₂, ..., xₙ}, MIN = inf{xᵢ} = min{xᵢ} (the smallest element) and MAX = sup{xᵢ} = max{xᵢ} (the largest). The range MAX - MIN measures the spread of the distribution — a simple but useful statistic alongside variance and standard deviation. In order statistics, MIN is the 0th percentile and MAX is the 100th percentile; AVG is the mean; MEDIAN (not a standard SQL aggregate but available via PERCENTILE_CONT) is the 50th percentile.

**Sciences (Meteorology)**: Weather reporting is built on MIN and MAX. Daily maximum and minimum temperatures, highest recorded wind speed, lowest barometric pressure during a storm — all are computed with MAX and MIN over time-partitioned readings. A meteorological database runs: `SELECT date, MIN(temperature), MAX(temperature), AVG(temperature) FROM readings GROUP BY date` to generate the daily weather summary report seen in every newspaper. The GROUP BY date produces one row per day; MIN and MAX find the day's boundaries. This is identical to the GROUP BY pattern in SQL, applied at scale to millions of sensor readings.

# Lore Conclusion

The Archivist's report showed the complete date range for each collection: the oldest scroll in the Ancient Languages collection dated to the Third Era; the most recently acquired scroll in the Alchemy section arrived last month. "One query," Master Selvaris said. "MIN and MAX, grouped by collection. No sorting required." She closed the results. "MIN and MAX tell you where the boundaries of your data lie. They answer the edge-case questions: the best, the worst, the first, the last. And because they compose naturally with COUNT, SUM, and AVG, a single query can answer all five questions about a dataset at once." She paused. "Scan once. Know everything."

---
