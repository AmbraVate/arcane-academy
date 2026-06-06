---
id: de-sen-m3-04
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m3
moduleTitle: "Module 3: NoSQL Systems"
moduleGlyph: "🗂️"
moduleSortOrder: 3
topicSlug: column_stores
topicTitle: "Column Stores"
topicSortOrder: 4
lesson: 4
title: "Column Stores: Analytical Speed Through Storage Orientation"
sortOrder: 4
difficulty: 4
estimatedMinutes: 35
xpReward: 75
practiceType: GUIDED_AND_SOLO
questType: RETRIEVAL_CHALLENGE
retrievalWeight: 0.6
questTypes:
  - guided
  - solo
  - retrieval
prerequisites:
  - de-sen-m3-03
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains how columnar storage reduces I/O for analytical queries"
    - "Describes run-length encoding and dictionary encoding as columnar compression techniques"
    - "Identifies when a column store outperforms a row store and vice versa"
    - "Names real columnar technologies and their primary use contexts"
  keywords:
    - columnar storage
    - row store
    - run-length encoding
    - dictionary encoding
    - vectorised execution
    - analytical query
    - OLAP
  modelAnswer: |
    Column stores persist each column as a separate file or segment on disk, rather than packing all columns of a row together. An analytical query that reads 3 of 50 columns only reads 6% of the data; a row store reads all 50 columns even though 47 are irrelevant.
    Columnar storage also enables high compression ratios. Run-length encoding (RLE) collapses consecutive repeated values into (value, count) pairs — effective for sorted or low-cardinality columns like date ranges or status codes. Dictionary encoding replaces repeated string values with integer codes — effective for high-repetition string columns like country names or product categories.
    Column stores excel at OLAP queries: SELECT few columns, aggregate many rows, GROUP BY, time-range filters. They are poor for OLTP: a single-row INSERT must write to every column file; fetching one complete row requires reading from all column files.
    Examples: Amazon Redshift, Google BigQuery, Apache Parquet (file format), ClickHouse, DuckDB (embedded), PostgreSQL with cstore_fdw extension. Apache Cassandra is often called a "column-family" store — this is different from a true column store; Cassandra is optimised for write throughput, not analytical reads.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "A table has 200 columns and 500 million rows. An analytical query reads 4 columns. Approximately what fraction of data does a column store read vs a row store?"
    options:
      - "Column store: 4/200 = 2% of data; Row store: 100% of all columns"
      - "Column store: 100%; Row store: 4/200 = 2%"
      - "Both read 100% — storage orientation only affects write speed"
      - "Column store: 4/200 = 2%; Row store: 4/200 = 2% due to index scan"
    correctIndex: 0
    explanation: "A column store reads only the 4 requested columns — 2% of total data (plus minor overhead). A row store must read entire rows to project the 4 columns; even with a covering index, it typically reads far more. The I/O reduction is the primary source of columnar speed for analytical queries."
  - type: FILL_BLANK
    question: "Run-length encoding compresses the sequence [UK, UK, UK, UK, DE, DE, DE, US] into ___ (value, count) pairs."
    answer: "(UK,4), (DE,3), (US,1)"
    explanation: "RLE collapses runs of identical consecutive values. The original 8 values become 3 pairs — a 2.7x compression. RLE is highly effective for sorted columns (a sorted country column will have long runs of the same value) and for columns with few distinct values regardless of sort order."
  - type: SHORT_TEXT
    question: "Why is Apache Parquet a columnar file format rather than a database, and when would you choose it over BigQuery?"
    modelAnswer: "Parquet is a storage format — it defines how data is physically organised on disk (column-oriented, with metadata, compression, row groups). It has no query engine of its own. You pair Parquet with a query engine (DuckDB, Spark, Athena). Choose Parquet when you need portable, open-standard storage that multiple tools can read — e.g. a data lake where both Spark and Athena process the same files. Choose BigQuery when you want a fully managed serverless query engine and don't need to own the storage layer."
microCheckpoint:
  question: "Why are column stores faster than row stores for analytical queries but slower for OLTP?"
  answer: "Column stores read only the columns a query needs, dramatically reducing I/O for analytics. But for OLTP (INSERT/UPDATE, single-row fetch), column stores must write to or read from every column file separately — more I/O than a row store that packs the entire row in one sequential write or read."
retrieval:
  recall: "Name two compression techniques commonly applied to columnar data and the column characteristics that make each effective."
  explain: "Explain vectorised execution in columnar databases and why it is faster than row-by-row processing."
  mistakeId: "column-store-for-oltp"
---

# The Slow Dashboard

The Consortium's analytics dashboard ran a report: total XP earned by tier by month for the past two years. The query scanned a 400-million-row events table. It took 8 minutes. "The query touches three columns out of 40," the Senior Engineer said. "We're reading 37 columns of data we don't need." The Lead Data Engineer pulled up the architecture diagram. "We need a columnar engine. Let's talk about why storage orientation matters."

# Row Stores vs Column Stores

How data is physically stored on disk determines what queries are fast.

### Row Store (PostgreSQL, MySQL)
All columns of a row are stored together on the same disk page.

```
Page contents (row store):
  [id=1, user_id='abc', event='login',  ts=2024-01-01, xp=10, tier='junior', ...]
  [id=2, user_id='def', event='lesson', ts=2024-01-02, xp=50, tier='senior', ...]
  [id=3, user_id='abc', event='badge',  ts=2024-01-03, xp=25, tier='junior', ...]
```

Fetching row 1 is one disk read. Fetching the `xp` column for all rows requires reading every page (because `xp` is scattered across all rows on all pages).

### Column Store (BigQuery, Redshift, ClickHouse, DuckDB)
Each column is stored contiguously.

```
Column file: xp
  [10, 50, 25, 10, 75, 50, 10, 25, ...]

Column file: tier
  [junior, senior, junior, junior, senior, senior, junior, senior, ...]

Column file: ts
  [2024-01-01, 2024-01-02, 2024-01-03, ...]
```

A query reading `xp` and `tier` reads only those two column files — skipping `id`, `user_id`, `event`, and 35 other columns entirely.

## I/O Reduction

```
Table: 400M rows × 40 columns × 8 bytes avg = 128 GB total data

Analytical query: SELECT tier, SUM(xp) FROM events
                  WHERE ts BETWEEN '2022-01-01' AND '2024-01-01'
                  GROUP BY tier

Row store I/O:  128 GB (read all rows, project 3 columns)
Column store I/O: 128 GB × (3/40) = 9.6 GB (read only ts, tier, xp)
Speedup: ~13x from I/O reduction alone, before compression or SIMD
```

## Compression

Columnar data compresses extremely well because a column contains homogeneous data of the same type.

### Run-Length Encoding (RLE)
```
Sorted tier column:
  [junior×12M, senior×8M, lead×2M]
  Stored as: (junior, 12_000_000), (senior, 8_000_000), (lead, 2_000_000)
  Compression ratio: 22M × 8 bytes → 3 × 16 bytes = 7,300x
```

Effective when data is sorted or has long runs of identical values.

### Dictionary Encoding
```
Country column (250M rows, ~200 distinct values):
  Original: 250M × 20 bytes avg = 5 GB
  Dictionary: {"UK":0, "DE":1, "US":2, "FR":3, ...}  (small)
  Encoded column: 250M × 1 byte (uint8 code) = 250 MB
  Compression ratio: 20x
```

Effective for string columns with high repetition and low cardinality.

## Vectorised Execution

Column stores process data in **batches of column values** rather than one row at a time.

```
Row-by-row (OLTP style):
  for row in table:
    if row.tier == 'senior':
      total_xp += row.xp    -- branch + deref every row

Vectorised (columnar style):
  tier_batch = tier_column[0:1024]   -- load 1024 tiers (cache line)
  xp_batch   = xp_column[0:1024]    -- load 1024 xp values
  mask = (tier_batch == 'senior')    -- SIMD comparison
  total_xp += SUM(xp_batch[mask])   -- SIMD masked sum
```

Modern CPUs execute SIMD (Single Instruction, Multiple Data) operations that process 16–32 values simultaneously. Vectorised execution saturates these CPU features; row-by-row processing cannot.

## Technologies

| Technology | Type | Use Case |
|---|---|---|
| Apache Parquet | File format | Data lake storage, portable columnar format |
| Apache ORC | File format | Hive/Spark ecosystem |
| DuckDB | Embedded database | Local analytics, embedded in applications |
| ClickHouse | OLAP database | Real-time analytics, high ingestion rate |
| Amazon Redshift | Cloud data warehouse | AWS-native OLAP |
| Google BigQuery | Serverless OLAP | Serverless, petabyte-scale analytics |
| Apache Cassandra | Column-family store | High-write throughput (NOT analytical) |

**Note on Cassandra**: Despite being called a "wide-column store" or "column-family database", Cassandra is optimised for high-write throughput and key-based lookups — not analytical aggregations. It is closer to a distributed key-value store than a true column store. Do not confuse the terminology.

## When to Use Each

| Workload | Row Store | Column Store |
|---|---|---|
| Single-row INSERT | Fast (one sequential write) | Slow (write to each column file) |
| Single-row SELECT | Fast (one page read) | Slow (read from each column file) |
| SELECT few columns, many rows | Slow | Fast |
| Aggregate over large range | Slow | Fast |
| UPDATE/DELETE by PK | Fast | Slow |

## Common Mistakes

> **Using Column Store for OLTP**
> An application that does high-frequency single-row INSERTs and point-queries (by primary key) will be slower in a column store. Column stores are purpose-built for read-heavy analytics. Use PostgreSQL for OLTP; use a column store for the analytical replica.

> **Confusing Cassandra with a Column Store**
> Cassandra's "column-family" terminology is historical and misleading. It is not a columnar analytical database. Using Cassandra expecting BigQuery-like analytical performance will disappoint.

> **Neglecting Sort Order**
> Column stores (especially Redshift, ClickHouse) benefit enormously from declaring the right sort key. A time-series table sorted by `ts` enables RLE compression and allows the engine to skip entire row groups whose time range doesn't match the query.

## Mental Model

Imagine a spreadsheet. A **row store** files the spreadsheet by rows — each sheet of paper contains one row. To tally a column, you pull every sheet and read one cell per page — you touch 100% of the paper. A **column store** files by columns — one sheet contains all values for `xp`, another for `tier`. To tally `xp`, you pull one sheet. To write a new row, you must update every sheet — expensive. For reading columns: unbeatable.

**Mini Summary**: Column stores persist each column contiguously on disk. An analytical query reading 3 of 50 columns reads only 6% of data. High compression ratios from RLE and dictionary encoding reduce I/O further. Vectorised SIMD execution multiplies CPU throughput. Fast for OLAP (aggregate few columns across many rows); slow for OLTP (INSERT and point queries). Technologies: Parquet (format), DuckDB, ClickHouse, BigQuery, Redshift.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium wants to build a data warehouse to answer business questions: "How many students completed each module per month?", "What is the average XP per tier?", "Which lessons have the highest drop-off rate?"

The source data is in PostgreSQL (OLTP). The schema has: `lesson_completions(id, user_id, lesson_id, completed_at, xp_earned)`, `users(id, tier, joined_at, country)`, `lessons(id, module_id, title, difficulty)`.

Reflect on:
1. Should the warehouse be built on PostgreSQL or a columnar database? What query pattern justifies your choice?
2. Describe the ETL pipeline you would build to move data from PostgreSQL to the warehouse.
3. What sort key would you choose for the `lesson_completions` fact table in a columnar warehouse, and why?

---

# Integration

**Mathematics**: Columnar compression ratios can be predicted using **information theory**. Shannon entropy H(X) = -Σ p(x) log₂ p(x) measures the minimum bits required per symbol. A column with 4 equally likely values (uniform distribution) has H = 2 bits — it needs 2 bits per value. The same column with one value appearing 99.9% of the time has H ≈ 0.011 bits — RLE or dictionary encoding approaches this limit. The compression ratio is approximately log₂(256) / H(X) for a byte-encoded column. Low-entropy columns (repetitive, low-cardinality) compress best — exactly the columns in analytical fact tables (tier, status, country, month).

**Sciences**: Vectorised execution mirrors **muscle fibre recruitment** in physiology. A single muscle fibre fires or rests (scalar, row-by-row). A whole muscle contracts via coordinated firing of thousands of parallel fibres simultaneously (vectorised, SIMD). The same mechanical work done by one fibre 1000 times sequentially takes far longer than 1000 fibres firing in one coordinated burst. CPU SIMD units are the silicon equivalent of motor unit recruitment — parallelism within a single clock cycle rather than across clock cycles.

---

# The Eight-Minute Query

The Senior Engineer migrated the analytics events table to ClickHouse. The eight-minute report ran in 340ms. "Same query, same data," the Senior Engineer said. "Different storage." The Lead Data Engineer nodded. "The query engine didn't get smarter. The storage stopped wasting I/O. That's what storage orientation means in practice."
