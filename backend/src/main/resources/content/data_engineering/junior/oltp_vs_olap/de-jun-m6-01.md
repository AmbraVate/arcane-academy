---
id: de-jun-m6-01
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m6
moduleTitle: "Module 6: Data Warehousing Foundations"
moduleGlyph: "🏛️"
moduleSortOrder: 6
topicSlug: oltp_vs_olap
topicTitle: "OLTP vs OLAP"
topicSortOrder: 1
lesson: oltp_vs_olap
title: "OLTP vs OLAP"
sortOrder: 1
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m5-04]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Distinguishes OLTP and OLAP by purpose, query pattern, and data model
    - Explains why normalisation is beneficial for OLTP but harmful for OLAP
    - Describes what a data warehouse is and why it exists separately from the operational database
    - Explains ETL and why it is needed to move data from OLTP to OLAP
    - Identifies characteristics of column-oriented vs row-oriented storage
  keywords: [OLTP, OLAP, normalisation, denormalisation, data warehouse, ETL, columnar storage, row storage, aggregation, reporting, operational, analytical, HTAP, latency, throughput]
  modelAnswer: |
    OLTP (Online Transaction Processing): optimised for many short read/write operations on individual rows — order entry, user login, inventory update. Normalised schema minimises write anomalies. OLAP (Online Analytical Processing): optimised for complex aggregations over large datasets — monthly sales by region, year-over-year trends. Denormalised (star/snowflake schema) reduces JOINs on read. Data warehouse: a separate analytical store loaded from OLTP via ETL (Extract, Transform, Load) — separates analytical load from operational systems. Columnar storage (Redshift, BigQuery, Parquet): stores all values for one column together — excellent for aggregation (SUM, AVG, COUNT) on a subset of columns; poor for row-level updates. Row storage (PostgreSQL, MySQL): stores all columns of a row together — excellent for retrieving full rows; poor for scanning one column across millions of rows.
guidedSteps:
  - id: de-jun-m6-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A business analyst runs: SELECT region, SUM(revenue) FROM sales GROUP BY region. The sales table has 500 million rows and 40 columns. The query only needs the region and revenue columns. Which storage format completes this query fastest?
    inputConfig:
      options:
        - "Row-oriented storage — reads complete rows and extracts the needed columns"
        - "Columnar storage — reads only the region and revenue columns, skipping the other 38"
        - "Both formats perform identically for this type of query"
        - "Row-oriented storage — it has better compression so less I/O overall"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Columnar storage — reads only the region and revenue columns, skipping the other 38"]
      rejectedFeedback: "Columnar (column-oriented) storage stores each column's values contiguously on disk. For this query, only region and revenue are needed. A column store reads only those two columns — 2/40 = 5% of the data. A row store must read entire rows (all 40 columns) even though 38 columns are discarded — 20× more I/O for the same result. Columnar storage also compresses better for analytical data (a column of revenue values has high repetition of patterns, enabling run-length encoding or dictionary encoding). This is why data warehouses (Redshift, BigQuery, Snowflake, DuckDB) use columnar storage by default, while OLTP databases (PostgreSQL, MySQL) use row storage."
    hint: "How much of each row does this query actually need, and how does that relate to I/O?"
    reflectionPrompt: "For a query that retrieves a single complete customer record (SELECT * FROM customers WHERE id = 42), would columnar storage help or hurt? Why?"
  - id: de-jun-m6-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The process of moving data from operational OLTP systems into the data warehouse — extracting, cleaning, transforming, and loading — is called ________.
    inputConfig:
      placeholder: "ETL"
    markingRule:
      matchMode: CONTAINS
      accepted: [ETL, "Extract Transform Load", "extract transform load", ELT, "Extract Load Transform"]
      rejectedFeedback: "ETL (Extract, Transform, Load): (1) Extract — pull data from source systems (OLTP databases, APIs, files). (2) Transform — clean, deduplicate, standardise formats, apply business rules, join across sources. (3) Load — write transformed data into the data warehouse. ELT (Extract, Load, Transform) is a modern variant used with cloud warehouses: load raw data into the warehouse first, then transform using the warehouse's own compute power (dbt is the dominant tool for the T in ELT). ETL is needed because: OLTP schemas are normalised (many tables with FKs) while warehouse schemas are denormalised (flat fact tables). Data from multiple source systems must be standardised. Historical data must be preserved even when source records change."
    hint: "Three letters: Extract, something, Load."
    reflectionPrompt: "Why might a company run ETL overnight rather than continuously in real time?"
  - id: de-jun-m6-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why running complex analytical queries (like total sales by region for the last 5 years) directly on the production OLTP database is a bad idea, even if the query is correct.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [lock, contend, slow, performance, operational, users, concurrent, full scan, impact, degrade, resource, compete]
      rejectedFeedback: "Running analytical queries on the OLTP database causes: (1) Resource contention — a 5-year sales aggregation performs full table scans consuming significant CPU, memory, and I/O. While the scan runs, ordinary transactional operations (insert order, update stock, process payment) compete for the same resources and slow down. (2) Lock contention — depending on isolation level, long-running reads may block writes or be blocked by writes, causing timeouts for end users. (3) Schema impedance — OLTP schemas are normalised: 5-year sales requires JOINs across orders, order_lines, products, customers, dates — not what the schema is optimised for. (4) SLA violation — analytical queries are not latency-sensitive but OLTP operations are. Mixing them causes unpredictable latency spikes in the operational system."
    hint: "What are the operational users of the system trying to do while the analyst's query runs?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A highly normalised OLTP schema is good for operational use but poor for analytical reporting because:"
    options:
      - "Normalised schemas store duplicate data that confuses analytical tools"
      - "Analytical queries require many JOINs across normalised tables, which is expensive at scale and harder to express as simple aggregations"
      - "OLTP schemas use row locking which prevents analytical queries from running"
      - "Normalised schemas cannot store NULL values needed in analytical contexts"
    correctIndex: 1
    feedback: "Normalisation (3NF, BCNF) eliminates redundancy and prevents update anomalies — ideal for OLTP where data changes frequently. For analytics: 'total revenue by product category by month' requires JOINs across orders, order_lines, products, categories, dates — potentially 5+ tables. At scale (hundreds of millions of rows), those JOINs are expensive. The warehouse solution: denormalise into a star schema — a central fact table (sales_facts) with foreign keys to dimension tables (dim_product, dim_customer, dim_date). Common queries become single table scans or simple joins to small dimension tables. Denormalisation trades storage efficiency (some redundancy) for query performance — the right trade-off when data is read 10,000× more than it's written."
  - type: MULTIPLE_CHOICE
    question: "Which workload characteristic best describes OLAP?"
    options:
      - "Many short transactions, each touching a few rows, high concurrency"
      - "Few long-running queries, each scanning millions of rows, returning aggregated results"
      - "Frequent INSERT/UPDATE/DELETE operations requiring strict ACID guarantees"
      - "Low latency lookups by primary key, sub-millisecond response time required"
    correctIndex: 1
    feedback: "OLAP characteristics: few concurrent users (analysts, BI tools), queries that scan large ranges of data (months, years), aggregate operations (SUM, COUNT, AVG, GROUP BY), complex multi-table joins, result sets that are small (a pivot table, a chart's data points) even when the scan was large. OLTP characteristics: thousands of concurrent users (customers, applications), queries that touch specific rows by PK or narrow indexed filters, frequent INSERT/UPDATE/DELETE, sub-second latency required. These opposing requirements justify separate systems: OLTP optimised for write throughput and low latency; OLAP optimised for scan throughput and aggregation. HTAP (Hybrid Transaction/Analytical Processing — e.g., CockroachDB, TiDB) attempts to serve both workloads but with trade-offs."
retrieval:
  recall: "List five key differences between OLTP and OLAP systems across these dimensions: primary use case, query pattern, schema design, typical users, and storage format."
  explain: "Explain why columnar storage is faster than row storage for analytical aggregation queries. Include the I/O reduction mechanism and how compression further improves performance."
  mistakeId:
    code: |
      -- Run on the production OLTP database every morning at 8am
      SELECT 
          c.country,
          p.category,
          DATE_TRUNC('month', o.order_date) AS month,
          SUM(ol.quantity * ol.unit_price) AS revenue,
          COUNT(DISTINCT o.customer_id) AS unique_customers
      FROM orders o
      JOIN order_lines ol ON o.id = ol.order_id
      JOIN products p ON ol.product_id = p.id
      JOIN customers c ON o.customer_id = c.id
      WHERE o.order_date >= '2020-01-01'
      GROUP BY c.country, p.category, month
      ORDER BY month, revenue DESC;
    answer: "This query performs a full scan across orders, order_lines, products, and customers for 4+ years of data on the production OLTP database. At 8am when operational users are most active, this will: (1) Consume significant I/O and CPU, degrading order processing and customer-facing operations. (2) Lock pages during the scan under certain isolation levels, potentially blocking writes. (3) Run on a normalised schema not optimised for this access pattern — the JOINs across four tables are expensive at scale. Fix: load this data into a data warehouse (daily ETL). In the warehouse, a sales_facts table with pre-joined region, category, and date dimensions makes this query a single fact table scan with simple dimension lookups. The analyst's query runs overnight on a separate system with no impact on the operational database."
---

# Hook

The same data serves two completely different masters: the application that records it (one row at a time, sub-second latency, thousands of concurrent users) and the analyst who aggregates it (billions of rows, complex joins, minutes to run). Trying to serve both from one system destroys both. The data warehouse is the solution — but understanding *why* it exists requires understanding the fundamental difference between transactional and analytical workloads.

# Lore Introduction

"The board reports are making the Archive application slow," the Junior Engineer reported. "The monthly loan statistics query takes two minutes and the checkout system times out while it runs." The Senior Archivist pulled up the query plan. "Full sequential scan. Four tables joined. Three years of data." She closed it. "This query is correct for analysis. It is wrong for a production database." The Junior looked puzzled. "It's the same data." The Senior Archivist nodded. "Same data, different purpose. The application needs fast row lookups for individual loans. The board needs aggregated trends across all loans. Those two patterns require fundamentally different designs. One database cannot serve both well." She pulled up a diagram. "This is why data warehouses exist."

# Core Learning

## Concept Introduction

### OLTP vs OLAP Comparison

```
                    OLTP                         OLAP
────────────────────────────────────────────────────────────────────
Purpose         Operational recording         Analytical reporting
Primary users   Applications, end users       Analysts, BI tools, reports
Query pattern   Single row by PK/index        Full scans, GROUP BY, aggregates
Row volume/q    1 – 100 rows                  Thousands to billions of rows
Concurrency     Thousands of transactions/s   Tens to hundreds of queries
Response time   < 100ms required              Minutes acceptable
Schema          Normalised (3NF)              Denormalised (star schema)
Operations      INSERT/UPDATE/DELETE heavy    Mostly SELECT (read-heavy)
Data freshness  Real-time                     Minutes to 24 hours lag OK
Storage         Row-oriented                  Column-oriented
Examples        PostgreSQL, MySQL, SQL Server  Redshift, BigQuery, Snowflake

Key insight: optimising for OLTP (indexes on individual columns,
  normalised schema, row-level locks) HURTS OLAP performance,
  and vice versa. Separate systems serve both correctly.
```

### Why Normalisation Hurts Analytics

```sql
-- OLTP (normalised): calculating total revenue by category requires 4 JOINs
SELECT p.category, SUM(ol.quantity * ol.unit_price)
FROM orders o
JOIN order_lines ol ON o.id = ol.order_id
JOIN products p ON ol.product_id = p.id
JOIN customers c ON o.customer_id = c.id
GROUP BY p.category;
-- At 500M order_lines rows: expensive joins, full scans, minutes to run

-- OLAP (star schema, denormalised fact table):
SELECT category, SUM(revenue)
FROM sales_facts
GROUP BY category;
-- Same result: one table scan, seconds at 500M rows in columnar store
-- (category pre-joined into the fact table at load time)
```

### Columnar vs Row Storage

```
Row-oriented storage (PostgreSQL):
  Row 1: [id=1, date='2024-01', region='North', product='SQL Book', qty=2, revenue=49.98, ...]
  Row 2: [id=2, date='2024-01', region='South', product='Java Guide', qty=1, revenue=34.99, ...]
  → Reading revenue column: must read entire row, discard 38 other fields per row

Columnar storage (Parquet, Redshift, BigQuery):
  id column:       [1, 2, 3, 4, 5, ...]
  date column:     ['2024-01', '2024-01', '2024-02', ...]  ← compressed (run-length)
  region column:   ['North', 'South', 'North', ...]        ← dictionary encoded
  revenue column:  [49.98, 34.99, 24.99, ...]              ← only this read for SUM

  For SUM(revenue): read only the revenue column
  I/O: 1 column / 40 columns = 2.5% of the data a row store reads
  Compression: column values are homogeneous → 5-10× compression ratio typical
```

### The Data Warehouse Architecture

```
                    Source Systems (OLTP)
              ┌─────────────┬─────────────┐
              │  Loans DB   │ Members DB  │  ← operational, normalised
              └──────┬──────┴──────┬──────┘
                     │             │
              ┌──────▼─────────────▼──────┐
              │         ETL / ELT          │
              │  Extract → Transform → Load│
              │  (runs nightly or hourly)  │
              └──────────────┬────────────┘
                             │
              ┌──────────────▼────────────┐
              │       Data Warehouse       │
              │  (Redshift, Snowflake,     │
              │   BigQuery, DuckDB)        │
              │  Denormalised star schema  │
              │  Columnar storage          │
              └──────────────┬────────────┘
                             │
              ┌──────────────▼────────────┐
              │   BI Tools / Reports       │
              │  (Tableau, Metabase, dbt)  │
              └───────────────────────────┘
```

### ETL vs ELT

```
ETL (traditional):
  Extract → Transform in pipeline → Load clean data to warehouse
  Tools: Apache Spark, Informatica, Talend, custom Python
  When: complex transformations, data quality enforcement before loading

ELT (modern cloud warehouses):
  Extract → Load raw data to warehouse → Transform in warehouse using SQL
  Tools: dbt (data build tool), Airbyte, Fivetran
  When: warehouse has enough compute, want to preserve raw data, SQL-first teams

Example ELT for Archive:
  1. Load: loans, members, items tables copied nightly to warehouse as-is
  2. Transform (dbt): CREATE TABLE sales_facts AS
     SELECT l.id, m.id as member_id, i.id as item_id,
            d.date_key, l.loan_date, l.return_date,
            EXTRACT(DAY FROM return_date - loan_date) AS loan_days
     FROM loans l JOIN members m ON l.member_id = m.id
     JOIN items i ON l.item_id = i.id
     JOIN dim_date d ON l.loan_date = d.calendar_date
```

## Why It Matters

OLTP vs OLAP is the most consequential split in data architecture — and running analytics on a transactional database is the classic way to learn it painfully:

- A heavy report scanning millions of rows competes with checkout transactions for the same resources; analytics literally slows revenue
- The workloads want opposite designs: OLTP wants normalised, indexed, row-oriented stores; OLAP wants wide, denormalised, column-oriented scans
- Recognising the boundary tells you when a read replica, warehouse, or lakehouse is the right next architectural step

Nearly every "database is on fire" incident in growing companies traces to these two workloads sharing one engine. Know the split and you'll see it coming.

## Common Mistakes

- **Running reports on the operational database**: Analytical queries compete with transactional operations for I/O and CPU. Even read-only queries can degrade write performance and introduce lock contention. Run reports on a read replica at minimum, or a proper data warehouse.
- **Building a warehouse with a normalised schema**: A warehouse with fully normalised tables (3NF) requires the same expensive JOINs that hurt OLTP analytics. Warehouse schemas should be denormalised — star or snowflake schema — to reduce JOINs at query time.
- **Confusing "a warehouse is just a big database"**: Warehouses differ in storage format (columnar), optimisation focus (scan throughput vs lookup latency), schema philosophy (denormalised), and data freshness (periodic loads). Drop-in substitution misses the design differences.
- **Ignoring data latency requirements**: ETL running nightly means warehouse data is 24 hours behind. If the business needs hourly or real-time analytics, different architectures (streaming ETL, HTAP, materialised views on the operational DB) are needed.

## Mental Model

OLTP and OLAP are like a hospital's emergency ward and its research department. The emergency ward (OLTP) needs instant access to individual patient records — "retrieve patient #4721 right now." The research department (OLAP) needs to analyse patterns across all patients over ten years — "what is the 5-year survival rate for treatment X by age group?" Both use patient data, but different formats, different access patterns, different infrastructure. Running the research analysis on the emergency ward's systems would slow down emergency care. Separate systems, same underlying reality.

## Mini Summary

- ✔ OLTP: operational, row-level operations, normalised, millisecond response, many users
- ✔ OLAP: analytical, full scans, aggregations, denormalised, minutes to run, few users
- ✔ Running analytical queries on OLTP degrades both workloads — separate systems needed
- ✔ Columnar storage: reads only needed columns → 10-20× less I/O for analytical aggregations
- ✔ Data warehouse: separate analytical store, loaded via ETL/ELT from OLTP sources
- ✔ ETL: extract from sources, transform to analytical model, load to warehouse

# Guided Practice Quest

Work through the guided steps to classify five queries as OLTP or OLAP, explain why a specific analytical query degrades the operational database, and design the basic ETL process to move Archive loan data into a warehouse.

# Solo Practice Quest

Evaluate the Archive system's reporting requirements and design an analytics solution. Tasks: (1) Identify five analytical questions the Archive management wants to answer (e.g. most popular items, member engagement by tier, seasonal borrowing patterns); (2) For each question, write the SQL you'd run against the data warehouse — contrast with what it would look like against the normalised OLTP schema; (3) Design the ETL process: what tables to extract, what transformations to apply, how often to run, and in what order; (4) Explain why columnar storage is beneficial for your analytical queries — estimate the I/O reduction for the largest query; (5) Identify one reporting requirement where 24-hour data latency is unacceptable and describe an alternative architecture for that specific case.

# Integration

**Mathematics**: Columnar compression exploits statistical properties of data. Run-length encoding (RLE) represents a sequence of repeated values as (value, count) pairs: [North, North, North, South, South] → [(North, 3), (South, 2)]. For a column with cardinality k in N rows, average run length = N/k. A status column (k=3 values, N=1,000,000 rows) has average runs of 333,333 — compresses to near-zero. Dictionary encoding maps distinct values to integers: {North→1, South→2, East→3, West→4}. A 6-character string becomes a 1-byte integer, achieving 6× compression on that column alone. These compression ratios directly reduce I/O: a 100GB uncompressed fact table might compress to 10-15GB in a columnar store, meaning queries read 7-10× less data from disk. Combined with reading only relevant columns, OLAP queries achieve 20-100× I/O reduction compared to row-based OLTP storage for typical analytical workloads.

**Sciences (Cognitive Science — Dual Process Theory)**: The OLTP/OLAP distinction maps elegantly onto Kahneman's dual process theory of cognition (Thinking, Fast and Slow). System 1 thinking: fast, automatic, requires immediate response — analogous to OLTP. A cashier cannot wait two minutes for the system to process a transaction. System 2 thinking: slow, deliberate, requires extended processing — analogous to OLAP. An analyst can wait minutes for a complex report to run. Cognitive research shows that the same person uses both systems for different decisions. The failure mode — running analytical queries on OLTP systems — is analogous to trying to perform slow, deliberate reasoning under time pressure: both suffer. Humans evolved to keep fast and slow thinking separate for efficiency; database architects discovered the same principle through decades of performance engineering.

# Lore Conclusion

"I understand now," the Junior Engineer said. "The board reports should never have been on the operational database." The Senior Archivist nodded. "The Archive will have two data stores: the operational PostgreSQL database for loan management, and an analytical store for reporting. The ETL runs overnight, loading the previous day's data." She pulled up the schema. "The analytical schema is different — not the normalised tables we use for operations. Flat tables, optimised for aggregation." The Junior Engineer studied it. "The loans table joined with member and item data into one wide row." The Senior Archivist confirmed. "That is the fact table. Dimensioned by member, item, date, category. This is called a star schema. It is the foundation of data warehousing — and the next thing you will learn."

---
