---
id: de-jun-m9-01
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m9
moduleTitle: "Module 9: Junior Project"
moduleGlyph: "🏗️"
moduleSortOrder: 9
topicSlug: mini_project
topicTitle: "Mini Project"
topicSortOrder: 1
lesson: the_academy_data_warehouse
title: "The Academy Data Warehouse"
sortOrder: 1
difficulty: 5
estimatedMinutes: 240
xpReward: 300
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: low
questTypes: [solo]
prerequisites:
  - de-jun-m1-01
  - de-jun-m2-01
  - de-jun-m3-01
  - de-jun-m4-01
  - de-jun-m5-01
  - de-jun-m6-01
  - de-jun-m7-01
  - de-jun-m8-01
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Dimensional model correctly separates fact tables from dimension tables with appropriate grain"
    - "Star schema has correct surrogate keys, foreign key relationships, and slowly changing dimension handling (at minimum, Type 1 SCD)"
    - "ETL logic is correctly structured: extract, transform (including at least one data quality check), load"
    - "At least one CTE is used to make a complex analytical query readable"
    - "At least one window function is used correctly (ranking, running total, or lag/lead)"
    - "A stored procedure or view is used to encapsulate at least one repeatable query"
    - "At least one security control is implemented: a role with restricted access, or a view that filters sensitive data"
    - "Written reflection explains the dimensional modelling decisions and at least one data quality tradeoff"
  keywords: [fact table, dimension table, star schema, ETL, CTE, window function, stored procedure, view, role, slowly changing dimension]
  modelAnswer: |
    A complete Academy Data Warehouse correctly models the academic domain as a star
    schema (fact table with at least three dimension tables), implements an ETL pipeline
    that includes a data quality check, uses CTEs and window functions in at least one
    analytical query, encapsulates a repeatable report as a view or stored procedure,
    and applies at least one access control. The reflection demonstrates understanding
    of why the fact grain was chosen and how it affects what questions can be answered.
---

# Hook

You know how to write queries. You know how to model data. You know ETL, window functions, CTEs, and access control.

But you have never assembled them into something that handles analytical workloads — a data warehouse with a dimensional model, an ETL pipeline, and reporting queries that answer real business questions.

That changes now.

# Lore Introduction

*"The Grand Academy has a problem,"* the Data Steward announces to the assembled scholars. *"We have records of every lesson completed, every assessment passed, every scholar who has ever walked these halls. But the records are scattered across seven different systems — the Registrar's Office, the Library, the Examination Hall, the Treasury, the Dormitory Records, the Faculty Roster, and the Infirmary."*

*"A Council member asks: 'How many scholars completed the Arcane Arts curriculum last decade?' No one knows. The answer is somewhere in seven systems, none of which talk to each other."*

*"We need a data warehouse. One place where all the analytical questions can be answered. Not the operational systems — those stay where they are. A reporting layer. Clean, modelled, queryable."*

She hands you the schema of the source systems.

*"You have four days. The Council meets on Thursday."*

# Project Brief

Build a **Junior Scholar Analytics Warehouse** — a dimensional model over the Academy's academic data, with an ETL pipeline to populate it and analytical queries to answer the Council's questions.

---

## Step 1: Design the Dimensional Model

The Academy wants to analyse scholar performance across three dimensions of interest: **who** (the scholar), **what** (the subject/curriculum), and **when** (time). A scholar completes lessons; each completion is a measurable event.

**Source data (simulate as INSERT statements):**

```sql
-- Operational table: scholars
scholars(id, name, tier, enrolled_date, faculty, sponsor_name)

-- Operational table: lessons
lessons(id, subject, curriculum, module_num, difficulty, xp_value)

-- Operational table: completions
completions(id, scholar_id, lesson_id, completed_at, score, attempts)
```

**Your task:**
1. Design a star schema with:
   - One **fact table** (`fact_lesson_completions`) at the grain of one row per completion event
   - At least three **dimension tables**: `dim_scholar`, `dim_lesson`, `dim_date`
2. Write `CREATE TABLE` statements for all tables including surrogate keys, foreign keys, and appropriate NOT NULL constraints.
3. Decide how you will handle a **slowly changing dimension** for `dim_scholar.tier` (scholars advance through tiers over time). Implement at minimum a Type 1 SCD (overwrite) and explain in comments why you chose this approach or why Type 2 would be better.

---

## Step 2: Build the ETL Pipeline

Write SQL that simulates a complete ETL load from the operational tables into the warehouse.

1. **Extract and validate:** Write a query that identifies completions in the source data that would fail your data quality rules (e.g. score outside 0-100, completion date in the future, lesson ID that does not exist). These records should be rejected and logged, not silently ignored.

2. **Transform:** Write the INSERT statements that load clean records from the operational tables into your star schema, performing any transformations needed (e.g. extracting year/month/day into `dim_date`, generating surrogate keys, joining source keys to dimension surrogate keys).

3. **Load:** Ensure the load is idempotent — if run twice on the same data, the second run should not create duplicate rows in the fact table.

Seed the operational tables with at least: 6 scholars, 20 lessons (spread across 3 curricula), and 50 completions.

---

## Step 3: Answer the Council's Questions

Write a SQL query for each of the following. Use CTEs where a query has more than two logical steps.

1. **Monthly completion trend:** For each month in the last year, how many lessons were completed and what was the average score? (Use window functions to add a column showing the 3-month rolling average score.)

2. **Scholar leaderboard:** Rank all scholars by total XP earned (sum of xp_value for all completed lessons), partitioned by tier, within the last 90 days.

3. **Hardest lessons:** Which 5 lessons have the highest average number of attempts before completion?

4. **Faculty performance:** Compare the average score per faculty across all curricula. Which faculty has the widest spread between their best and worst performing curriculum?

5. **Completion rate by curriculum:** For each curriculum, what percentage of enrolled scholars have completed at least 80% of the lessons?

---

## Step 4: Encapsulate and Secure

1. Create a **view** called `vw_scholar_public_profile` that exposes scholar name, tier, and total XP — but excludes `sponsor_name` (which is sensitive).

2. Create a **stored procedure** `sp_weekly_completion_report(p_week_start DATE)` that returns the total completions and average score for the given week. The procedure should validate that the input date is a Monday and raise an error if not.

3. Create a **role** `REPORTING_ANALYST` and grant it SELECT on the view and execute on the procedure, but no direct access to the fact or dimension tables.

---

## Step 5: Written Reflection

Answer the following in 300-500 words:

1. What grain did you choose for the fact table, and what questions does this grain make impossible to answer?
2. You chose Type 1 or Type 2 SCD for scholar tier. What would a stakeholder lose if they wanted historical analysis that your choice does not support?
3. If the warehouse grew to 10 million completion events, which of your analytical queries would perform worst, and what index or partitioning strategy would you apply?

---

# Lore Conclusion

*"The Data Steward reviews the warehouse on Wednesday evening."*

*"She runs the faculty performance query. The answer appears in 0.3 seconds."*

*"She runs it again, this time for the last century of Academy records. It runs in 1.2 seconds."*

*"The Council meets Thursday morning. For the first time in four centuries, every question is answered before the meeting ends."*

*"You built this,"* she says. *"Not perfectly — the slowly changing dimension implementation means we cannot see what tier a scholar was when they completed a lesson three years ago. That matters. We will fix it in the next sprint."*

*"But you built something real. And real, improvable things are how the craft advances."*

---
