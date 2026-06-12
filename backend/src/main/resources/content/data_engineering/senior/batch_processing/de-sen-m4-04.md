---
id: de-sen-m4-04
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m4
moduleTitle: "Module 4: Data Pipelines"
moduleGlyph: "🔄"
moduleSortOrder: 4
topicSlug: batch_processing
topicTitle: "Batch Processing"
topicSortOrder: 4
lesson: 4
title: "Batch Processing: High-Throughput Data at Scale"
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
  - de-sen-m4-03
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains MapReduce and how it parallelises large-scale data processing"
    - "Describes data shuffling and why it is the bottleneck in distributed batch jobs"
    - "Identifies when batch processing is preferable to streaming"
    - "Explains partition strategies for efficient distributed joins"
  keywords:
    - MapReduce
    - shuffle
    - Spark
    - partition
    - distributed join
    - batch job
    - broadcast join
  modelAnswer: |
    Batch processing accumulates data and processes it in large chunks — maximising throughput over latency. MapReduce decomposes a job into Map (apply a function to each record, emit key-value pairs) and Reduce (aggregate all values for each key). The intermediate step between Map and Reduce — the shuffle — moves all values for the same key to the same reducer node. The shuffle involves network I/O and disk serialisation; it is typically the bottleneck in distributed batch jobs.
    Batch processing is preferable to streaming when latency requirements are relaxed (hourly, daily reports), data volumes require distributed processing, transformations are complex (multi-pass algorithms, iterative ML), or exactly-once semantics are simpler to guarantee with transactions than with stream checkpointing.
    Distributed joins require co-partitioning: both datasets must be partitioned on the join key so that matching rows end up on the same executor. A broadcast join is an optimisation for joining a large dataset with a small one — the small dataset is replicated to all executors, eliminating the shuffle entirely.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "A Spark job joins a 5TB user_events table with a 2MB reference table of country codes. Which join strategy should the planner use?"
    options:
      - "Sort-merge join — both tables need sorting on the join key"
      - "Hash join — build a hash table on user_events, probe with country codes"
      - "Broadcast join — replicate the 2MB country codes to all executors, avoiding shuffle"
      - "Nested loop join — iterate all combinations"
    correctIndex: 2
    explanation: "A broadcast join replicates the small table (2MB) to all executors. Each executor then performs a local hash join against its partition of the large table — no shuffle required. Shuffling 5TB of user_events across the network for a sort-merge join would be enormously expensive. Spark automatically chooses broadcast join when one side is below spark.sql.autoBroadcastJoinThreshold (default 10MB)."
  - type: FILL_BLANK
    question: "In MapReduce, the ___ phase moves all values with the same key to the same reducer, and is typically the primary performance bottleneck due to network I/O."
    answer: "shuffle"
    explanation: "After the Map phase emits (key, value) pairs, the shuffle sorts and redistributes data across the network so all values for key K arrive at the same reducer. For large datasets, shuffling can generate terabytes of network traffic. Minimising shuffle — by filtering before grouping, using combiners, and choosing the right partition key — is the primary optimisation lever in batch jobs."
  - type: SHORT_TEXT
    question: "A daily batch job processes 200GB of event data and takes 4 hours. The team wants it to finish in 1 hour. What are three approaches to reduce the runtime?"
    modelAnswer: "1. Increase parallelism: add more executor nodes/partitions — each node processes a smaller slice. 2. Reduce shuffle: push filters before GROUP BY to reduce the data size before the expensive shuffle phase. Use broadcast joins for small dimension tables. 3. Read only the data needed: use columnar storage (Parquet) and partition pruning on date — if the job processes yesterday's data, only read yesterday's partition rather than scanning the full 200GB. Any combination of these compounds the improvement."
microCheckpoint:
  question: "Why is the shuffle phase the bottleneck in most distributed batch jobs?"
  answer: "The shuffle moves intermediate data across the network so values with the same key land on the same reducer. It involves serialisation, network transfer, and disk I/O at every executor. For large datasets this can generate terabytes of cross-node traffic — bound by network bandwidth, not CPU."
retrieval:
  recall: "What is a broadcast join and when should it be used over a sort-merge join?"
  explain: "Explain why batch processing is still valuable despite the availability of streaming systems."
  mistakeId: "batch-no-partition-pruning"
---

# The Monthly ML Training Run

The Consortium needed to retrain its recommendation model monthly: 18 months of lesson completion history, 500GB of enriched feature data. "This isn't a streaming problem," the Lead Data Engineer said. "No human needs this result in 30 seconds. We need maximum throughput over a multi-hour window. This is batch."

# Why Batch Processing Still Matters

Despite the rise of streaming, batch processing remains the backbone of large-scale data engineering:

| Scenario | Why Batch Wins |
|---|---|
| ML training on historical data | Requires full dataset; streaming can't accumulate 18 months |
| Complex multi-pass algorithms | Iterative graph algorithms, multi-join transformations |
| Daily/weekly reporting | Latency requirement is hours, not seconds |
| Data migration | Moving TB+ datasets between systems |
| Full reprocessing (schema fix) | Re-transform historical raw data |

## MapReduce: The Foundation

MapReduce decomposes a distributed computation into two phases:

### Map Phase
Apply a function to every record independently. Each record emits zero or more (key, value) pairs.

```python
 # Map: for each lesson completion, emit (lesson_id, 1)
def map_function(record):
    yield (record['lesson_id'], 1)

 # Applied in parallel across all partitions — no coordination needed
```

### Shuffle Phase
The framework redistributes data across nodes so all values for the same key end up on the same reducer node. **This is the bottleneck** — network I/O, serialisation, disk writes.

```
Node 1 maps: (lesson_A, 1), (lesson_B, 1), (lesson_A, 1)
Node 2 maps: (lesson_A, 1), (lesson_C, 1), (lesson_B, 1)

After shuffle:
  Reducer 1 receives: lesson_A → [1, 1, 1]
  Reducer 2 receives: lesson_B → [1, 1]
  Reducer 3 receives: lesson_C → [1]
```

### Reduce Phase
Aggregate all values for each key.

```python
 # Reduce: sum all counts for each lesson
def reduce_function(key, values):
    yield (key, sum(values))
 # Result: (lesson_A, 3), (lesson_B, 2), (lesson_C, 1)
```

## Apache Spark: Modern Batch Processing

Spark extends MapReduce with in-memory processing (avoids disk write between stages), a rich API (DataFrame, SQL, ML), and lazy evaluation.

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import col, date_trunc, sum as _sum

spark = SparkSession.builder.appName("consortium-batch").getOrCreate()

 # Read Parquet from data lake
events = spark.read.parquet("s3://consortium-datalake/events/")
users  = spark.read.parquet("s3://consortium-datalake/users/")

 # Partition pruning: only read last 18 months
events_filtered = events.filter(
    col("occurred_at") >= "2022-09-01"
)

 # Broadcast join: users table is small (2M rows, ~200MB)
from pyspark.sql.functions import broadcast
enriched = events_filtered.join(
    broadcast(users),
    on="user_id",
    how="left"
)

 # Aggregation (triggers shuffle)
monthly_xp = (enriched
    .withColumn("month", date_trunc("month", col("occurred_at")))
    .groupBy("user_id", "tier", "month")
    .agg(_sum("xp_earned").alias("total_xp"))
)

 # Write partitioned output (parallelises downstream reads)
monthly_xp.write \
    .partitionBy("month") \
    .mode("overwrite") \
    .parquet("s3://consortium-datalake/marts/monthly_xp/")
```

## Distributed Joins

Joining two large datasets in a distributed system requires **co-partitioning** — both sides partitioned on the join key so matching rows land on the same executor.

### Sort-Merge Join (default for large tables)
```
Both sides sorted and repartitioned on join key → network shuffle
Then local merge on each executor
Cost: O(N log N) sort + shuffle network traffic
```

### Broadcast Join (small + large table)
```
Small table (< threshold) → replicated to all executors
Large table → stays in place, no shuffle
Each executor does a local hash join against its partition
Cost: O(small_table_size × executor_count) network traffic
```

### Hash Join (medium tables)
```
Smaller side → hashed into memory hash table on each executor
Larger side → streamed through, probing the hash table
No sort; requires one side fits in memory
```

```python
 # Force broadcast join when Spark planner doesn't auto-detect
from pyspark.sql.functions import broadcast
result = large_table.join(broadcast(small_table), on="id")
```

## Optimising Batch Jobs

### Reduce Before Shuffle
```python
 # BAD: shuffle all records, then filter
events.groupBy("lesson_id").count().filter(col("count") > 100)

 # GOOD: filter first (reduces shuffle volume)
events.filter(col("lesson_id").isNotNull()) \
      .groupBy("lesson_id").count() \
      .filter(col("count") > 100)
```

### Use Columnar Storage with Partition Pruning
```python
 # With Parquet partitioned by date, Spark reads only matching partitions
events = spark.read.parquet("s3://data/events/")
events.filter(col("date") == "2024-03-15")
 # EXPLAIN shows: PartitionFilters: [date=2024-03-15]
 # Only reads 1/365 of data instead of full scan
```

### Tune Parallelism
```python
 # Default shuffle partitions (200) often wrong for large jobs
spark.conf.set("spark.sql.shuffle.partitions", "2000")
 # Rule of thumb: ~128MB per partition for stable performance
```

## Common Mistakes

> **Small Files Problem**
> Writing 50,000 small Parquet files (one per user) instead of a few large files causes massive overhead for downstream readers — each file requires a separate S3 request. Coalesce or repartition before writing to keep file sizes between 128MB–1GB.

> **No Partition Pruning on Read**
> Reading an entire Parquet lake (500GB) when you need only one day's data is a full scan. Always filter on the partition column before any other operations so the planner can prune at read time.

> **Cartesian Product Join**
> Forgetting `on="join_key"` in a join produces a Cartesian product — every row joined with every other row. N×M rows instead of N. This will OOM or run for days on any non-trivial dataset. Always specify the join key explicitly.

## Mental Model

Think of batch processing as a **factory overnight shift**. During the day (streaming), workers handle individual items as they arrive. At night (batch), a large machine processes the entire day's inventory: sorting by category (shuffle), counting each category (reduce), and producing a summary report for the next morning. The machine is slow to start but far more efficient per unit than individual processing. It can also do complex operations (sort the entire inventory, cross-reference suppliers) that are impractical to do in real-time.

**Mini Summary**: Batch processing maximises throughput over latency using distributed MapReduce. The shuffle — redistributing data by key across nodes — is the primary bottleneck. Spark extends MapReduce with in-memory processing and a SQL API. Key optimisations: broadcast joins for small tables, filter before shuffle, columnar storage with partition pruning. Still essential for ML training, full reprocessing, and complex multi-pass transformations.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium needs to run a monthly ML feature engineering pipeline:
- Source: 18 months of `xp_events` (500GB Parquet, partitioned by month), `users` (1GB), `lessons` (50MB)
- Output: one row per user per month with 15 computed features (e.g. total_xp, lessons_completed, avg_difficulty, distinct_domains)

Reflect on:
1. Describe the join strategy: which table should be broadcast? Which joins require a shuffle?
2. The pipeline currently processes all 18 months every run. How would you optimise it to only recompute the current month while keeping historical results intact?
3. What output file structure would you use in S3, and why?

---

# Integration

**Mathematics**: MapReduce implements the functional programming operations **map** and **fold (reduce)** — both foundational to functional composition. The correctness of MapReduce depends on the reduction function being **associative** (a ∘ (b ∘ c) = (a ∘ b) ∘ c) and optionally **commutative** (order-independent). Associativity allows the combiner optimisation: partial reduction on each mapper before the shuffle, dramatically reducing shuffle volume. SUM and COUNT are associative+commutative (always safe). MEDIAN is neither (cannot be partially combined), which is why distributed median calculations require a different algorithm (e.g. approximate quantile sketch).

**Sciences**: Distributed batch processing mirrors **tidal wave energy harvesting** — collecting and concentrating diffuse energy (events scattered across many sources) into dense, processable batches. The shuffle phase is analogous to a **tidal barrage** that concentrates the water from many tidal streams into a single reservoir before releasing it through turbines (reducers). The throughput is maximised at the cost of latency — exactly the batch processing trade-off. The key engineering challenge in both systems: minimising the energy lost in the concentration step (shuffle network overhead in data; hydraulic friction in tidal systems).

---

# The Model Ships

The batch job ran for 3 hours and 40 minutes across 200 Spark executors. One hundred and twelve gigabytes of feature data, computed from 500GB of raw events, ready for the ML training pipeline. "It would have taken eleven hours without the broadcast join optimisation," the Senior Engineer said. The Lead Data Engineer reviewed the output partitions. "And next month we only recompute the new month's features. The historical partitions are done." Batch was not glamorous. But nothing else could do this at this scale.
