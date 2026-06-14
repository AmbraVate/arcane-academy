---
id: de-app-m3-09
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
lesson: date_functions
title: "Date Functions"
sortOrder: 9
difficulty: 2
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m3-08]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Uses CURRENT_DATE and CURRENT_TIMESTAMP correctly
    - Extracts date parts (year, month, day) using EXTRACT or equivalent
    - Performs date arithmetic (add/subtract intervals)
    - Formats dates for display using TO_CHAR or DATE_FORMAT
    - Reflects on why storing dates as proper DATE/TIMESTAMP types rather than text is essential for these functions to work
  keywords: [date, CURRENT_DATE, EXTRACT, INTERVAL, DATE_TRUNC, TO_CHAR, DATE_FORMAT, arithmetic, timestamp, age]
  modelAnswer: |
    Date functions allow querying, calculating, and formatting date and time values. CURRENT_DATE and CURRENT_TIMESTAMP return the current date/time. EXTRACT retrieves components (year, month, day). Date arithmetic using INTERVAL adds or subtracts time periods. DATE_TRUNC (PostgreSQL) or equivalent truncates dates to a precision level, useful for grouping by month or year. TO_CHAR/DATE_FORMAT converts dates to display strings. All of these functions only work when dates are stored as DATE or TIMESTAMP types — not as VARCHAR. Common use cases include finding records within a date range, calculating age or time elapsed, and grouping reports by month.
guidedSteps:
  - id: de-app-m3-09-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which WHERE clause finds all orders placed in the last 30 days?
    inputConfig:
      options:
        - "WHERE order_date > '30 days ago'"
        - "WHERE order_date >= CURRENT_DATE - INTERVAL '30 days'"
        - "WHERE order_date BETWEEN 30 AND 0"
        - "WHERE DAYS(order_date) < 30"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["WHERE order_date >= CURRENT_DATE - INTERVAL '30 days'"]
      rejectedFeedback: "CURRENT_DATE returns today's date; subtracting INTERVAL '30 days' gives the date 30 days ago. Using >= means 'today or after that date' — inclusive of exactly 30 days ago. '30 days ago' is not valid SQL syntax. BETWEEN 30 AND 0 is nonsense for dates. DAYS() is not a standard SQL function."
    hint: "You need today's date (a function) minus a duration (an INTERVAL)."
    reflectionPrompt: "How would you modify this clause to find orders placed in the last 7 days? The last calendar year?"
  - id: de-app-m3-09-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To extract just the year from a `order_date` column (e.g. to get 2026 from '2026-06-05'), you use: `________(YEAR FROM order_date)`
    inputConfig:
      placeholder: "EXTRACT"
    markingRule:
      matchMode: CONTAINS
      accepted: [EXTRACT, extract]
      rejectedFeedback: "EXTRACT(YEAR FROM date_column) extracts the year component from a date or timestamp. EXTRACT(MONTH FROM date_column) extracts the month (1-12). EXTRACT(DAY FROM date_column) extracts the day. In MySQL you can also use YEAR(date_column), MONTH(date_column). In SQL Server: YEAR(date_column) or DATEPART(year, date_column)."
    hint: "The standard SQL function for pulling a component out of a date."
    reflectionPrompt: "How would you group orders by month and year using EXTRACT?"
  - id: de-app-m3-09-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why storing a date as VARCHAR (e.g. '05/06/2026') makes date functions and date arithmetic impossible to use.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [text, string, VARCHAR, type, arithmetic, compare, sort, format, parse, inconsistent, invalid]
      rejectedFeedback: "Date functions require the database to know the value is a date. When stored as VARCHAR, the database treats it as arbitrary text — EXTRACT, INTERVAL arithmetic, and comparisons all fail or produce wrong results. Additionally, VARCHAR dates cannot be sorted chronologically, can accept invalid dates like '32/13/2026', and may use inconsistent formats ('06/05/2026' is June 5 in US format but May 6 in UK format). Only DATE and TIMESTAMP types enable correct date operations."
    hint: "The database can only do date arithmetic if it knows the column contains a date — not just any text."
    reflectionPrompt: "How would you write a WHERE clause to find orders from 'last month' if order_date is stored as VARCHAR?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `EXTRACT(MONTH FROM '2026-08-15')` return?"
    options: ["2026", "8", "15", "August"]
    correctIndex: 1
    feedback: "EXTRACT(MONTH FROM date) returns the month as a number: 1 for January through 12 for December. For '2026-08-15', the month is August, which is month number 8."
  - type: MULTIPLE_CHOICE
    question: "Which expression calculates how many days ago an order was placed?"
    options:
      - "order_date - 30"
      - "CURRENT_DATE - order_date"
      - "INTERVAL(order_date)"
      - "DATEDIFF(order_date)"
    correctIndex: 1
    feedback: "CURRENT_DATE - order_date returns the number of days between the order date and today (as an integer or interval depending on the database). In PostgreSQL: CURRENT_DATE - order_date returns an INTEGER number of days. In MySQL: DATEDIFF(CURRENT_DATE, order_date) is explicit. SQL Server uses DATEDIFF(day, order_date, GETDATE())."
retrieval:
  recall: "Write a WHERE clause that finds records created within the last 90 days."
  explain: "Explain why EXTRACT(YEAR FROM order_date) is useful for generating yearly summary reports."
  mistakeId:
    code: "storing birth_date as VARCHAR('15/06/1990')"
    answer: "Storing dates as text prevents all date operations: you cannot calculate age (CURRENT_DATE - birth_date), extract year/month/day, sort chronologically, or validate for impossible dates like '30/02/1990'. DATE type makes all of these operations automatic, correct, and fast. Always use proper date types."
---

# Hook

"Show me all orders from this month." "How many days since the customer last purchased?" "Group revenue by quarter." These are extremely common data questions — and they all require date functions.

Date and time are some of the most complex data types in computing (time zones, leap years, calendar edge cases), but SQL's date functions handle the hard parts for you. This lesson covers the essential toolkit.

# Lore Introduction

"The Archivist wants all scrolls acquired in the last year, grouped by acquisition month," Master Selvaris read from the requirements. "Three operations: filter by date range, extract the month, and group." She wrote the query in three parts. "CURRENT_DATE gives us today's date. Subtracting INTERVAL '1 year' gives us the cutoff. EXTRACT gets the month. DATE_TRUNC groups by month." She ran it. "Twelve rows — one per month — showing the number of acquisitions each month for the last year." She turned to her apprentice. "Date functions transform time-series data into the patterns that management needs to see."

# Core Learning

## Concept Introduction

### Getting the Current Date and Time

```sql
CURRENT_DATE        -- today's date (no time component): '2026-06-05'
CURRENT_TIMESTAMP   -- current date and time: '2026-06-05 14:32:00'
NOW()               -- alias for CURRENT_TIMESTAMP in PostgreSQL/MySQL
GETDATE()           -- SQL Server equivalent
```

### Extracting Date Components

```sql
-- Standard SQL
EXTRACT(YEAR  FROM date_column)   -- returns 2026
EXTRACT(MONTH FROM date_column)   -- returns 1-12
EXTRACT(DAY   FROM date_column)   -- returns 1-31
EXTRACT(HOUR  FROM timestamp_col) -- returns 0-23
EXTRACT(DOW   FROM date_column)   -- day of week (0=Sunday in PostgreSQL)

-- MySQL alternatives
YEAR(date_column)
MONTH(date_column)
DAY(date_column)

-- Usage example
SELECT order_id, EXTRACT(YEAR FROM order_date) AS order_year
FROM orders;
```

### Date Arithmetic with INTERVAL

```sql
-- PostgreSQL / standard SQL
CURRENT_DATE - INTERVAL '30 days'      -- 30 days ago
CURRENT_DATE + INTERVAL '1 year'       -- 1 year from now
CURRENT_DATE - INTERVAL '3 months'     -- 3 months ago

-- Days between two dates (PostgreSQL)
CURRENT_DATE - hire_date               -- returns integer days

-- MySQL
DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY)
DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY)
DATEDIFF(CURRENT_DATE, hire_date)      -- returns days between

-- SQL Server
DATEADD(day, 30, GETDATE())
DATEDIFF(day, hire_date, GETDATE())
```

### Common Date Filtering Patterns

```sql
-- Orders in the last 30 days
WHERE order_date >= CURRENT_DATE - INTERVAL '30 days'

-- Orders this calendar month
WHERE EXTRACT(YEAR FROM order_date) = EXTRACT(YEAR FROM CURRENT_DATE)
  AND EXTRACT(MONTH FROM order_date) = EXTRACT(MONTH FROM CURRENT_DATE)

-- Orders in a specific year
WHERE EXTRACT(YEAR FROM order_date) = 2025

-- Date range
WHERE order_date BETWEEN '2026-01-01' AND '2026-03-31'
```

### Truncating Dates (Grouping by Period)

```sql
-- PostgreSQL: truncate to start of month
DATE_TRUNC('month', order_date)    -- '2026-06-05' → '2026-06-01'
DATE_TRUNC('year', order_date)     -- '2026-06-05' → '2026-01-01'
DATE_TRUNC('week', order_date)     -- truncates to Monday of the week

-- MySQL
DATE_FORMAT(order_date, '%Y-%m-01')  -- first day of the month

-- Useful for grouping monthly totals
SELECT DATE_TRUNC('month', order_date) AS month, SUM(total_amount) AS revenue
FROM orders
GROUP BY DATE_TRUNC('month', order_date)
ORDER BY month;
```

### Formatting Dates for Display

```sql
-- PostgreSQL: TO_CHAR
TO_CHAR(order_date, 'DD Mon YYYY')    -- '05 Jun 2026'
TO_CHAR(order_date, 'YYYY-MM-DD')     -- '2026-06-05'
TO_CHAR(order_date, 'Month DD, YYYY') -- 'June 05, 2026'

-- MySQL: DATE_FORMAT
DATE_FORMAT(order_date, '%d %M %Y')   -- '05 June 2026'
DATE_FORMAT(order_date, '%Y-%m-%d')   -- '2026-06-05'
```

### Calculating Age / Time Elapsed

```sql
-- Age in PostgreSQL
SELECT name, AGE(date_of_birth) AS age FROM customers;
-- returns an interval: '35 years 4 months 12 days'

SELECT name, EXTRACT(YEAR FROM AGE(date_of_birth)) AS age_years FROM customers;
-- returns: 35

-- Days since last order
SELECT customer_id, CURRENT_DATE - MAX(order_date) AS days_since_last_order
FROM orders
GROUP BY customer_id;
```

## Why It Matters

Almost every meaningful business question has a time dimension — this month, last quarter, year over year — and date functions are how SQL answers it:

- Grouping sales by month or signups by week requires extracting parts from timestamps
- "Orders in the last 30 days" needs date arithmetic that handles month lengths and year boundaries correctly
- Ages, durations, and deadlines are all differences between dates — easy to get subtly wrong by a day

Dates are also a notorious source of bugs (time zones, month boundaries, leap years), so practising the core functions now prevents painful surprises later.

## Common Mistakes

- **Comparing dates with string comparisons**: `WHERE order_date = '05/06/2026'` fails or gives wrong results unless the format matches exactly and the date type supports it.
- **EXTRACT(MONTH) = 6 for this month**: Fails if the year changes. Always also check the year.
- **Time zone blindness**: CURRENT_DATE returns the server's local date. In global systems, use UTC timestamps and convert for display.
- **Storing dates as VARCHAR**: Prevents all date functions. Always use DATE or TIMESTAMP.

## Mental Model

Think of a DATE column as a number on a timeline. CURRENT_DATE is your current position on the timeline. Subtracting INTERVAL '30 days' steps back 30 positions. EXTRACT pulls out one dimension of the position (year, month, day). DATE_TRUNC rounds your position to the nearest period boundary (start of month, start of year). Formatting is like reading the position aloud in a specific language (DD/MM/YYYY vs Month DD, YYYY). All of this only works because the database knows the column contains a timeline position — not arbitrary text.

## Mini Summary

- ✔ `CURRENT_DATE` / `CURRENT_TIMESTAMP` — today's date/time
- ✔ `EXTRACT(YEAR|MONTH|DAY FROM col)` — pull date component
- ✔ `date_col - INTERVAL '30 days'` — date arithmetic
- ✔ `DATE_TRUNC('month', col)` — truncate for grouping
- ✔ `TO_CHAR` / `DATE_FORMAT` — format for display
- ✔ None of this works if dates are stored as VARCHAR

# Guided Practice Quest

Work through the guided steps to filter by date range, extract date components, and calculate time elapsed using date arithmetic.

# Solo Practice Quest

You are building a customer analytics report for an e-commerce platform. The `orders` table has `order_id`, `customer_id`, `order_date`, `delivered_at`, and `total_amount`. Write five queries: (1) orders placed in the current calendar month, (2) monthly order count and revenue for the last 12 months (grouped by month), (3) customers who have not ordered in the last 90 days (using a subquery or aggregate), (4) average delivery time in days per month for the last 6 months, (5) orders placed on weekends (use EXTRACT(DOW...)). For each query, write the SQL and explain what business question it answers and why proper DATE typing is essential.

# Integration

**Mathematics**: Date arithmetic in SQL implements modular arithmetic on the Gregorian calendar — a complex algebraic system. Adding 30 days is simple integer addition on the underlying day count (Julian Day Number). Adding 1 month is more complex: it requires carrying across month/year boundaries and handling months of different lengths (February has 28 or 29 days). SQL databases implement this correctly: CURRENT_DATE + INTERVAL '1 month' correctly handles all edge cases. EXTRACT implements projection functions on the calendar coordinate space: π_year(date), π_month(date), π_day(date). DATE_TRUNC implements floor functions: floor_month(date) = the first day of the date's month.

**Sciences (Astronomy)**: The Julian Day Number system, used in astronomy, represents dates as a continuous count of days since January 1, 4713 BC. This is the mathematical model underlying all computer date storage: internally, dates are stored as numbers (days since epoch). SQL date arithmetic is literally integer arithmetic on these underlying numbers. Astronomers use the same approach when calculating the time between celestial events: subtract two Julian Day Numbers to get the exact number of days between them, with no calendar complexity. SQL's DATE subtraction is this operation made accessible to data engineers.

# Lore Conclusion

"Twelve months of acquisition data, grouped by month, showing acquisition rates and comparing to last year," the Archivist confirmed. "This would have taken a clerk three days manually." Master Selvaris closed the query. "Date functions are the engine of time-series analysis," she said. "Every business question about 'this period' vs 'last period', about 'how long ago', about 'customers who have not returned' — all answered by date arithmetic." She turned to her apprentice. "Dates stored correctly as DATE and TIMESTAMP types make these queries trivial. Dates stored as text make them nearly impossible." She underlined this in the design document. "Type your dates correctly. The rest follows."

---
