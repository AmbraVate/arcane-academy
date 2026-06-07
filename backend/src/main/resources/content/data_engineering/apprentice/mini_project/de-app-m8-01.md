---
id: de-app-m8-01
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m8
moduleTitle: "Module 8: Apprentice Project"
moduleGlyph: "🏗️"
moduleSortOrder: 8
topicSlug: mini_project
topicTitle: "Mini Project"
topicSortOrder: 1
lesson: the_village_ledger
title: "The Village Ledger"
sortOrder: 1
difficulty: 3
estimatedMinutes: 120
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: low
questTypes: [solo]
prerequisites:
  - de-app-m1-06
  - de-app-m2-01
  - de-app-m3-01
  - de-app-m4-01
  - de-app-m5-01
  - de-app-m6-01
  - de-app-m7-01
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Tables are correctly defined with appropriate data types and column names"
    - "Primary keys are present on all tables; foreign keys correctly reference parent tables"
    - "At least one relationship between tables is correctly implemented and enforced"
    - "SELECT queries use correct syntax including WHERE, ORDER BY, and GROUP BY where appropriate"
    - "At least one aggregate function (COUNT, SUM, AVG, MAX, MIN) is used correctly"
    - "A JOIN query combines data from at least two tables to answer a business question"
    - "At least one data quality constraint (NOT NULL, CHECK, UNIQUE) is applied intentionally"
    - "Written reflection explains one design decision and one thing that would be done differently"
  keywords: [table, primary key, foreign key, SELECT, WHERE, JOIN, aggregate, GROUP BY, constraint, relationship]
  modelAnswer: |
    A complete Village Ledger correctly defines tables with appropriate types, enforces
    primary and foreign key relationships, answers at least three business questions
    with SELECT queries (including at least one with a JOIN and one with an aggregate),
    and applies at least one data quality constraint with a stated reason. The reflection
    demonstrates awareness of at least one modelling tradeoff.
---

# Hook

Seven modules. Data types. Tables. Keys. Relationships. Joins. Aggregates. Data quality.

You have learned each piece separately. Now you use them together — not in a single exercise, but in a small database that does something real.

This project is your first complete data structure. It will be imperfect. That is expected. The goal is not perfection — it is integration: proving that you can bring the pieces together to model a real domain.

> Before you start: think about what questions someone would need answered. A database exists to serve questions. Design yours around the questions, not just the data.

# Lore Introduction

*"The village of Thornhaven,"* the archivist explains, setting down a stack of paper ledgers, *"has kept records for two hundred years. But the records are paper. Ink fades. Books are lost. Last winter, the ledger recording every craftsperson's goods was destroyed in a flood."*

*"We need a proper system. One that does not flood. One that can answer a question in seconds rather than hours of searching. One that — if we add a new craftsperson — does not require rewriting the entire record by hand."*

She hands you a quill and a blank tablet.

*"The village does not need something grand. It needs something correct."*

# Project Brief

Build a **Village Trade Ledger** — a small database that records the craftspeople, goods, and trades of Thornhaven. The database should be designed, populated, and queried to answer the questions the village council actually needs answered.

---

## Step 1: Design the Schema

Thornhaven's ledger tracks the following:

**Craftspeople:** Each craftsperson has a name, a trade (e.g. blacksmith, weaver, healer), and the year they joined the village guild. A craftsperson may have multiple apprentices; an apprentice belongs to exactly one master.

**Goods:** Each good has a name, a category (e.g. tools, cloth, remedies), and a standard value in silver coins. Each good is produced by exactly one craftsperson (the one who made it).

**Trades:** When goods are exchanged, a trade is recorded: the good, the buyer (another craftsperson), the date, and the actual price paid (which may differ from the standard value due to negotiation).

**Your task:**
1. Write the `CREATE TABLE` statements for this schema. Include primary keys, foreign keys, appropriate NOT NULL constraints, and at least one CHECK constraint where it makes sense.
2. Write at least 15 `INSERT` statements to populate the database with realistic sample data (at least 4 craftspeople, 8 goods, and 10 trades).
3. In a short comment above the CREATE TABLE statements, explain the most important modelling decision you made and why.

---

## Step 2: Answer the Council's Questions

Write a SQL query for each of the following questions. Each query should be clearly commented with the question it answers.

1. **Who are all the craftspeople in the village, listed alphabetically by trade and then by name?**

2. **What is the total value of all goods currently in the ledger, by category?** (Use the standard value, not trade price.)

3. **Which goods have been traded for more than their standard value?** (Show the good's name, the standard value, and the actual price paid.)

4. **Who has made the most trades as a buyer?** (Show the top 3.)

5. **What is the average trade price for each category of goods?**

6. **Which craftspeople have not made any trades as a buyer?** (They may have sold goods, but have not bought any.)

---

## Step 3: Improve the Design

After writing your queries, answer the following in written reflection (not SQL):

1. The council wants to start tracking which goods are currently in stock (not yet sold) vs sold. How would you change the schema to support this? (Describe the change — you do not need to write the SQL.)

2. What is one constraint you did NOT add but probably should have? Why did you not add it, and what is the risk of its absence?

3. If the village grew to 1,000 craftspeople and 50,000 goods, which of your queries would slow down first, and why?

---

## Submission

Your submission should include:
- `schema.sql` — all CREATE TABLE and INSERT statements
- `queries.sql` — all six queries, each commented with the question it answers
- A short written reflection (200-400 words) answering the three questions in Step 3

---

# Lore Conclusion

*"The archivist reviews your work carefully,"* turning each page of the ledger slowly.

*"It is not perfect. The craftsperson names allow duplicates — two Elara Smiths are recorded, and the system cannot tell them apart. The trade dates have no time component, so two trades on the same day are indistinguishable."*

*"But the structure is sound. The questions can be answered. The flood cannot destroy this."*

She stamps it with the guild seal.

*"Every system begins imperfect. The ones that matter are the ones that begin."*

---
