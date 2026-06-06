---
id: de-app-m7-02
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m7
moduleTitle: "Module 7: Data Thinking"
moduleGlyph: "💡"
moduleSortOrder: 7
topicSlug: asking_better_questions
topicTitle: "Asking Better Questions"
topicSortOrder: 1
lesson: patterns_in_data
title: "Patterns in Data"
sortOrder: 2
difficulty: 2
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m7-01]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Identifies the four common data pattern types (trend, seasonality, outlier, distribution)
    - Describes how SQL queries can surface each type of pattern
    - Explains what an outlier is and why outliers deserve investigation before removal
    - Distinguishes between a real pattern and random noise
    - Reflects on how visual representations (even described) help identify patterns that numbers alone may miss
  keywords: [trend, seasonality, outlier, distribution, pattern, noise, anomaly, GROUP BY, aggregate, time series]
  modelAnswer: |
    Common data patterns include: trends (consistent direction over time), seasonality (repeating cycles tied to calendar periods), outliers (values far from the norm), and distributions (how values spread across a range). SQL surfaces trends with time-grouped aggregates (GROUP BY month), seasonality by grouping across multiple years, outliers by filtering for extreme values, and distributions with GROUP BY on value ranges. Outliers must be investigated before removal — they may be data errors, but they may also be the most important signal in the data. The difference between a real pattern and noise requires comparing the pattern's magnitude to the natural variability of the data.
guidedSteps:
  - id: de-app-m7-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Monthly revenue data shows a spike every December and a dip every February, repeated every year for 5 years. What type of data pattern is this?
    inputConfig:
      options:
        - "Trend — a consistent directional change over time"
        - "Outlier — unexpected values that deviate from the norm"
        - "Seasonality — a repeating pattern tied to the calendar"
        - "Distribution — the spread of values across a range"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Seasonality — a repeating pattern tied to the calendar"]
      rejectedFeedback: "A pattern that repeats at regular calendar intervals (weekly, monthly, yearly) is seasonality. December sales peaks (Christmas shopping) and February dips (post-holiday slow period) are classic retail seasonality. Seasonality is important to understand because it affects how you compare periods: December this year vs December last year is a fair comparison; December vs February is not."
    hint: "The pattern repeats at the same calendar time each year — it is tied to the calendar."
    reflectionPrompt: "Why is it misleading to compare December revenue to November revenue without accounting for seasonality?"
  - id: de-app-m7-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      An order with a total_amount of £45,000 in a dataset where the average order is £120 is likely an ________. The first step is to investigate it, not remove it.
    inputConfig:
      placeholder: "outlier"
    markingRule:
      matchMode: CONTAINS
      accepted: [outlier, "outlier (anomaly)", anomaly]
      rejectedFeedback: "An outlier is a value that falls far outside the typical range. A £45,000 order when the average is £120 could be: a data entry error (wrong), a bulk order from a corporate client (correct and important), a test transaction (wrong). The correct response is always to investigate first. Removing outliers without investigation risks deleting real, important signals — the best customers, fraud cases, or system errors all appear as outliers."
    hint: "A value far outside the normal range of the data — investigate before deciding what to do."
    reflectionPrompt: "Write a SQL query to identify the top 10 orders by amount. What would you look for to determine if they are errors or genuine?"
  - id: de-app-m7-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain the difference between a real trend and random noise in sales data, and why looking at only two data points is insufficient to identify a trend.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [consistent, direction, random, variation, months, points, noise, fluctuation, multiple, sustained]
      rejectedFeedback: "A trend is a consistent directional change over many periods — revenue consistently increasing or decreasing over 6–12 months. Noise is random variation that goes up and down without a sustained direction. Two data points (this month vs last month) cannot distinguish trend from noise — a single up or down month may be random fluctuation. A real trend requires consistent direction across multiple consecutive periods, with the magnitude of change exceeding the normal month-to-month variability."
    hint: "One month's change could be random. What would you need to see to be confident the direction is real?"
    reflectionPrompt: "How many months of data would you want before concluding that revenue is on an upward trend?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A data engineer notices that sales data shows no orders between 2am and 4am every day. What type of pattern is this most likely?"
    options:
      - "Outlier — these zero-order periods are anomalous"
      - "Trend — sales are declining over time"
      - "Seasonality (daily cycle) — low activity during overnight hours"
      - "Distribution — orders are unevenly distributed"
    correctIndex: 2
    feedback: "Daily patterns (low overnight, peak at lunchtime) are a form of seasonality — repeating cycles at a sub-daily level. Weekly patterns (lower weekends for B2B) and yearly patterns (Christmas peaks) are also seasonality. Recognising the cycle type (daily, weekly, monthly, yearly) is the first step in understanding it."
  - type: MULTIPLE_CHOICE
    question: "Which SQL pattern best identifies the distribution of order amounts across value ranges?"
    options:
      - "SELECT MAX(total_amount) FROM orders;"
      - "SELECT total_amount FROM orders ORDER BY total_amount;"
      - "SELECT CASE WHEN total_amount < 50 THEN 'small' WHEN total_amount < 200 THEN 'medium' ELSE 'large' END AS bucket, COUNT(*) FROM orders GROUP BY 1;"
      - "SELECT AVG(total_amount) FROM orders;"
    correctIndex: 2
    feedback: "Grouping values into buckets (CASE WHEN) and counting rows per bucket reveals the distribution — how many orders fall in each range. This shows whether most orders are small with a few large ones, or uniformly distributed, or bimodal. MAX shows only the ceiling; ORDER BY shows all values but no summary; AVG collapses the distribution to a single number that may hide the true shape."
retrieval:
  recall: "Write a SQL query that shows monthly revenue for the last 24 months to identify a trend or seasonality pattern."
  explain: "Explain the four common data pattern types and give a SQL approach for surfacing each."
  mistakeId:
    code: "removing all orders above £5,000 from a dataset because they are 'outliers' before calculating average order value"
    answer: "Removing outliers before calculating an average changes the metric you are measuring. The original average (with outliers) tells you the true average including your highest-value orders. The filtered average hides your most valuable customers. Outliers should be investigated first: are they data errors, bulk orders, or fraud? Remove only confirmed errors. If high-value orders are real, they should either stay in the analysis or be analysed separately as a distinct segment."
---

# Hook

Raw data is rarely interpretable as a table of numbers. Patterns in data — trends, cycles, anomalies, distributions — are how raw data becomes insight. Recognising the type of pattern in your data determines both what questions to ask and what SQL to write.

This lesson introduces the four fundamental pattern types and the SQL techniques that surface them.

# Lore Introduction

"Something strange in the transaction logs," the Head Analyst said. "Revenue spikes every first week of the month, then drops. And three transactions are ten times larger than any other." Master Selvaris pulled up the data. "Two patterns," she said. "The weekly spike is seasonality — a calendar-driven cycle. Likely guild members paying dues on payday." She ran a GROUP BY day-of-month query. "Confirmed. First to fifth of each month, revenue is triple the rest." She turned to the three large transactions. "Outliers. Could be errors. Could be bulk purchases from affiliated organisations." She queried the customer records. "One is the Merchant's Guild placing a quarterly order. One is a data entry error — extra zero. One is suspicious — flagged for investigation." She closed the report. "Same pattern type, three different meanings. Investigate before concluding."

# Core Learning

## Concept Introduction

### The Four Pattern Types

#### 1. Trends — Consistent Direction Over Time

A trend is a sustained increase or decrease across multiple periods.

```sql
-- Monthly revenue to look for an upward or downward trend
SELECT
    EXTRACT(YEAR FROM order_date)  AS year,
    EXTRACT(MONTH FROM order_date) AS month,
    SUM(total_amount)              AS revenue
FROM orders
WHERE status != 'cancelled'
GROUP BY EXTRACT(YEAR FROM order_date), EXTRACT(MONTH FROM order_date)
ORDER BY year, month;
-- Look for consistent direction across 6+ months
```

#### 2. Seasonality — Repeating Calendar Cycles

Seasonality is a pattern that repeats at regular calendar intervals.

```sql
-- Same month across multiple years to identify annual seasonality
SELECT
    EXTRACT(MONTH FROM order_date) AS month,
    EXTRACT(YEAR FROM order_date)  AS year,
    SUM(total_amount)              AS revenue
FROM orders
GROUP BY EXTRACT(MONTH FROM order_date), EXTRACT(YEAR FROM order_date)
ORDER BY month, year;

-- Day-of-week pattern (weekly seasonality)
SELECT
    EXTRACT(DOW FROM order_date) AS day_of_week,  -- 0=Sunday, 6=Saturday
    COUNT(*)                     AS order_count,
    AVG(total_amount)            AS avg_order_value
FROM orders
GROUP BY EXTRACT(DOW FROM order_date)
ORDER BY day_of_week;
```

#### 3. Outliers — Values Far from the Norm

An outlier is a value that falls far outside the expected range.

```sql
-- Find orders far above average (potential outliers)
SELECT order_id, customer_id, total_amount, order_date
FROM orders
WHERE total_amount > (SELECT AVG(total_amount) + 3 * STDDEV(total_amount) FROM orders)
ORDER BY total_amount DESC;

-- Simpler approach: top and bottom extremes
SELECT order_id, customer_id, total_amount
FROM orders
ORDER BY total_amount DESC
LIMIT 20;  -- investigate the top 20

-- Count and percentage of outliers above threshold
SELECT
    COUNT(*) AS outlier_count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM orders), 2) AS pct_of_total
FROM orders
WHERE total_amount > 5000;
```

**Before removing an outlier, always ask:**
- Is it a data entry error? (extra zero, wrong currency)
- Is it a real but exceptional transaction? (bulk order, corporate client)
- Is it fraud or a system error?
- Is it the most important signal in the data?

#### 4. Distributions — The Shape of Values

A distribution shows how values spread across a range.

```sql
-- Bucket order amounts to see the distribution shape
SELECT
    CASE
        WHEN total_amount < 25    THEN '< £25'
        WHEN total_amount < 50    THEN '£25–50'
        WHEN total_amount < 100   THEN '£50–100'
        WHEN total_amount < 250   THEN '£100–250'
        WHEN total_amount < 1000  THEN '£250–1000'
        ELSE '£1000+'
    END                 AS bucket,
    COUNT(*)            AS order_count,
    ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER (), 1) AS pct
FROM orders
GROUP BY 1
ORDER BY MIN(total_amount);
```

### Trend vs Noise

```
Noise: random up-down variation month to month with no sustained direction
Trend: consistent direction across 6+ periods, change greater than typical variability

To distinguish:
1. Plot the time series (or use SQL to look at 12+ months of data)
2. Calculate the average change per period
3. Check whether consecutive periods move in the same direction
4. Look for structural breaks — a sudden change after a sustained flat period
```

### Combining Patterns

Real data often has multiple overlapping patterns:

```
Revenue data might show:
  - Upward trend (business growing)
  - Annual seasonality (December peak, February dip)
  - Weekly seasonality (weekday vs weekend patterns)
  - Occasional outliers (bulk orders, data errors)

Good analysis separates these layers.
Comparing December 2026 to November 2026 mixes trend and seasonality.
Comparing December 2026 to December 2025 isolates the trend (same seasonal position).
```

## Common Mistakes

- **Calling any unusual value an "outlier" to remove**: Outliers are not errors by definition. They require investigation.
- **Seeing a trend in two data points**: Two data points can show direction but not trend. Trend requires consistency across many points.
- **Ignoring seasonality in period-over-period comparisons**: Month-over-month comparisons during seasonal periods (comparing December to November) mislead. Use year-over-year for seasonal businesses.
- **Reporting averages without distributions**: "Average order value is £120" hides whether most orders are near £120 or whether it's a mix of £20 and £500 orders.

## Mental Model

Think of data as weather. A trend is a season changing — summer getting hotter over decades. Seasonality is the predictable cycle — summer is always warmer than winter. Outliers are unusual weather events — a snowstorm in July. Distribution is the typical range — most summer days between 20°C and 30°C. Good meteorologists distinguish these layers. Good data analysts do the same: they separate the long-term signal from the cycle from the unusual event.

## Mini Summary

- ✔ Four patterns: trend (direction), seasonality (calendar cycles), outliers (extremes), distribution (shape)
- ✔ Trends require multiple periods of consistent direction — not two data points
- ✔ Seasonality: compare same-period year-over-year, not consecutive periods
- ✔ Outliers: investigate before removing — they may be the most important signal
- ✔ Distributions: use CASE WHEN buckets to see the shape of your data

# Guided Practice Quest

Work through the guided steps to identify a seasonal pattern in monthly data, write a distribution query using CASE WHEN buckets, and decide whether to investigate or remove a specific outlier based on context.

# Solo Practice Quest

You are analysing three months of sales data from a retail store database with orders and order_lines. Write queries and interpret the results for: (1) a time-series query showing daily order counts — describe what patterns you might find and how you would recognise trend vs noise, (2) a day-of-week aggregation — what patterns would you expect and why?, (3) a distribution query for order amounts using 5–6 buckets — describe what different distribution shapes would mean about the customer base, (4) an outlier detection query using a threshold of 3× the average order value — for each outlier found, list three questions you would ask before deciding whether to keep or investigate it, (5) a year-over-year comparison for the same month to control for seasonality. For each, explain what the pattern type is and what business decision it might inform.

# Integration

**Mathematics**: The four pattern types correspond to components of a classical time series decomposition: Y(t) = T(t) + S(t) + E(t), where T is the trend component, S is the seasonal component, and E is the error/noise component. Outliers are extreme realisations of E. The decomposition separates these components to analyse each independently — the same operation performed manually in the SQL queries above. Formally, trend is estimated by moving averages (centred averaging over seasonal period length), seasonality by computing period-specific averages after detrending, and the residual E is the remainder. This decomposition is the foundation of time series analysis, exponential smoothing, and ARIMA models.

**Sciences (Ecology — Population Dynamics)**: Ecologists encounter all four pattern types in population data. Trends: long-term species population decline or recovery. Seasonality: annual breeding cycles, migration patterns, winter hibernation. Outliers: disease outbreaks causing population crashes, exceptional breeding years. Distributions: size distributions of individuals (typically lognormal for many species). The SQL patterns — GROUP BY month for seasonality, distribution buckets for size classes, outlier detection for unusual counts — are directly analogous to the tools used in population ecology. Wildlife population monitoring databases are analysed with these exact techniques, informing conservation policy.

# Lore Conclusion

The Head Analyst's report was complete: three pattern types identified in one dataset. "The monthly seasonality explains the dues cycle," they confirmed. "The outliers had three different explanations — one error corrected, one bulk order kept, one fraud case referred to the Guild Council." Master Selvaris reviewed the distribution query. "Most transactions are between 10 and 50 gold. The tail above 500 gold is only 2% of transactions but 31% of revenue — your highest-value customers." She closed the report. "Patterns are not in the numbers themselves — they emerge when you organise the numbers correctly. Time series for trends. Same-period comparison for seasonality. Extremes for outliers. Buckets for distributions. Four lenses. Same data. Completely different insights."

---
