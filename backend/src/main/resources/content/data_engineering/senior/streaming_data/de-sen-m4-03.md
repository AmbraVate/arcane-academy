---
id: de-sen-m4-03
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m4
moduleTitle: "Module 4: Data Pipelines"
moduleGlyph: "🔄"
moduleSortOrder: 4
topicSlug: streaming_data
topicTitle: "Streaming Data"
topicSortOrder: 3
lesson: 3
title: "Streaming Data: Processing Events as They Arrive"
sortOrder: 3
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
  - de-sen-m4-02
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the difference between streaming and batch processing"
    - "Describes Kafka's topic/partition/consumer group model"
    - "Explains event time vs processing time and why late-arriving events cause problems"
    - "Identifies windowing strategies and their trade-offs"
  keywords:
    - Kafka
    - topic
    - partition
    - consumer group
    - event time
    - processing time
    - watermark
    - windowing
    - late data
  modelAnswer: |
    Streaming processes events individually or in micro-batches as they arrive, providing low-latency results. Batch processing accumulates events and processes them in large runs (hourly, daily), providing higher throughput at the cost of latency.
    Kafka organises data into topics, partitioned for parallelism. Each partition is an append-only log with durable ordered messages. Consumer groups allow multiple consumers to share the workload — each partition is assigned to one consumer in the group. Messages are retained for a configurable period (not deleted on consumption), enabling replay.
    Event time is when the event occurred (in the source system); processing time is when the stream processor receives it. Late-arriving events (network delays, mobile devices reconnecting) arrive with event time earlier than the current watermark. Processing them requires either waiting (watermark delay) or discarding/correcting results.
    Windowing aggregates events within a time range. Tumbling windows: fixed, non-overlapping (count events per 1-minute window). Sliding windows: overlapping (events in the past 5 minutes, updated every 30 seconds). Session windows: grouped by activity gap (events within 30 minutes of inactivity form one session).
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "A Kafka topic has 6 partitions. A consumer group has 4 consumers. How are partitions assigned, and what happens if a 5th consumer joins?"
    options:
      - "4 consumers get 1 partition each; 2 partitions are unassigned. Adding a 5th rebalances to 1 each and 1 still unassigned"
      - "Each consumer gets 1.5 partitions on average; adding a 5th causes an error"
      - "4 consumers get between 1 and 2 partitions each (Kafka distributes evenly). Adding a 5th causes rebalance: 1 consumer gets 2 partitions, 4 consumers get 1 partition each"
      - "All 4 consumers receive all 6 partitions; consumer groups broadcast, not partition"
    correctIndex: 2
    explanation: "Kafka assigns each partition to exactly one consumer in a consumer group. With 6 partitions and 4 consumers: 2 consumers get 2 partitions, 2 consumers get 1 partition. Adding a 5th consumer triggers a group rebalance: all partition assignments are re-computed — typically 1 consumer gets 2 partitions, 4 consumers get 1 each. If consumers exceed partitions, excess consumers are idle."
  - type: FILL_BLANK
    question: "In stream processing, a ___ defines how far behind processing time event time can lag before the system declares a window closed and emits results."
    answer: "watermark"
    explanation: "The watermark is an estimate of event-time progress. The stream processor declares 'I believe all events up to event-time T-delay have arrived.' When the watermark passes a window's end time, the window is closed and results emitted. A longer watermark delay tolerates more late data but increases output latency."
  - type: SHORT_TEXT
    question: "A streaming pipeline counts login events per user per 1-minute tumbling window. A mobile user's network drops, and 3 events from 10 minutes ago arrive late. Describe two ways to handle this."
    modelAnswer: "1. Allowed lateness: keep the window open for late data up to N minutes after the watermark passes. Emit a corrected result when late events arrive (update/retract-and-replace). Higher latency, correct results. 2. Discard late events: close the window strictly when the watermark passes. Late events are counted in the current window or dropped. Lower latency, some events missed. The right choice depends on whether correctness (option 1) or latency (option 2) is more important for the use case."
microCheckpoint:
  question: "What is the difference between event time and processing time in a stream processor?"
  answer: "Event time is when the event actually occurred in the source system. Processing time is when the stream processor receives and processes the event. Network delays, retries, and offline mobile devices cause events to arrive out of event-time order — processing time can be minutes or hours ahead of some events' event time."
retrieval:
  recall: "What are the three main windowing types in stream processing and when would you use each?"
  explain: "Explain why Kafka retains messages after consumption and what this enables."
  mistakeId: "streaming-processing-time-as-event-time"
---

# The Real-Time Requirement

The Consortium's fraud detection team needed alerts within 30 seconds of a suspicious transaction pattern. The current batch ETL pipeline ran every four hours. "Batch is not an option here," the Lead Data Engineer said. "Thirty seconds means streaming. Let's build it."

# Batch vs Streaming

```
Batch Processing:
  ──────────[collect events 00:00–01:00]──────[process 1hr batch]──→ result at 01:05

Streaming Processing:
  event1 → [process immediately] → result in <100ms
  event2 → [process immediately] → result in <100ms
```

| Dimension | Batch | Streaming |
|---|---|---|
| Latency | Minutes to hours | Milliseconds to seconds |
| Throughput | Very high | High (per partition) |
| Complexity | Low | High |
| Use cases | Reporting, ETL, ML training | Fraud detection, monitoring, real-time dashboards |

## Apache Kafka: The Streaming Backbone

Kafka is a distributed event log. Producers write events; consumers read them. Unlike a message queue, **Kafka retains messages** — consumers can replay from any offset.

```
Topic: xp_events
  Partition 0: [msg_0, msg_1, msg_4, msg_7, ...]
  Partition 1: [msg_2, msg_5, msg_8, ...]
  Partition 2: [msg_3, msg_6, msg_9, ...]

  └─ Messages ordered within a partition (by offset)
  └─ Globally ordered only within a partition
  └─ Partitioned by key hash (e.g. user_id) → same user's events in same partition
```

### Consumer Groups
Multiple consumers sharing the work of consuming a topic.

```
Consumer Group: fraud-detection-service
  Consumer A → Partition 0, 1
  Consumer B → Partition 2

Consumer Group: analytics-service
  Consumer C → Partition 0
  Consumer D → Partition 1
  Consumer E → Partition 2
```

Different consumer groups read independently — fraud detection and analytics both consume all events without interfering. Within a group, each partition is assigned to exactly one consumer.

### Kafka Message Structure
```json
{
  "key": "user_4891",
  "value": {
    "eventId": "evt_001",
    "userId": "user_4891",
    "lessonId": "de-sen-m4-03",
    "xpEarned": 75,
    "occurredAt": "2024-03-15T14:32:00Z"
  },
  "timestamp": 1710512320000,
  "offset": 42847,
  "partition": 1
}
```

## Event Time vs Processing Time

```
Event occurs at 14:32:00 (event time)
  ↓ network delay: 8 seconds
Kafka receives at 14:32:08
  ↓ consumer lag: 2 seconds
Stream processor processes at 14:32:10 (processing time)
```

For most events, the gap is milliseconds. But:
- A mobile user goes offline for 2 hours, then reconnects
- A batch microservice publishes 10 minutes of buffered events at once
- A network partition is resolved, releasing held messages

These **late-arriving events** have event time in the past but processing time in the present.

## Watermarks

A watermark is the stream processor's estimate of event-time progress.

```
Watermark = MAX(seen event times) - allowed_lateness
```

When the watermark passes a window's end, the window is closed and results emitted.

```
Tumbling 1-minute window: [14:00, 14:01)
Events arriving (by processing time):
  14:00:05  → event_time=14:00:03  → falls in [14:00, 14:01) window
  14:00:50  → event_time=14:00:45  → falls in [14:00, 14:01) window
  14:01:10  → event_time=14:01:05  → window [14:00, 14:01) is CLOSED → emit result
  14:01:30  → event_time=14:00:55  → LATE for window [14:00, 14:01)
```

## Windowing Strategies

### Tumbling Windows
Fixed, non-overlapping intervals. Each event belongs to exactly one window.

```python
# Apache Flink (Java API pseudocode)
stream
    .keyBy(event -> event.userId)
    .window(TumblingEventTimeWindows.of(Time.minutes(1)))
    .aggregate(new XpSumAggregator())
# Emits: (userId, window_start, total_xp) once per minute per user
```

### Sliding Windows
Overlapping intervals. Each event belongs to multiple windows.

```python
# Events in the past 5 minutes, updated every 1 minute
stream
    .keyBy(event -> event.userId)
    .window(SlidingEventTimeWindows.of(Time.minutes(5), Time.minutes(1)))
    .aggregate(new XpSumAggregator())
# Emits: (userId, xp_in_last_5min) every 1 minute per active user
```

### Session Windows
Group events separated by a gap smaller than a defined inactivity timeout.

```python
# All events within 30-minute inactivity gaps form one session
stream
    .keyBy(event -> event.userId)
    .window(EventTimeSessionWindows.withGap(Time.minutes(30)))
    .aggregate(new SessionAggregator())
# Each session's length and event count = learning session metrics
```

## Streaming Architecture Pattern

```
PostgreSQL OLTP
       ↓ (Debezium CDC)
Kafka Topic: db.events
       ↓
Flink / Kafka Streams
  - Filter fraud patterns
  - Aggregate per window
  - Enrich with reference data
       ↓               ↓
Alert Service    ClickHouse (streaming OLAP)
(webhook)        (real-time dashboard)
```

## Common Mistakes

> **Using Processing Time Instead of Event Time**
> A windowed count using processing time will miscategorise late-arriving events. Use event-time windowing with an appropriate watermark delay for any aggregation that needs to be historically accurate.

> **No Backpressure Handling**
> If the consumer processes slower than Kafka produces, consumer lag grows unboundedly. Implement backpressure: limit processing rate, scale consumers horizontally, or degrade gracefully under load.

> **Small Messages, Large Overhead**
> Publishing one Kafka message per database row for 1M rows/sec generates massive broker overhead. Batch messages before publishing (Kafka producer batching) or use larger value payloads. Kafka is optimised for throughput, not ultra-low-latency single-message delivery.

> **Ignoring Exactly-Once Semantics**
> Kafka supports exactly-once delivery end-to-end (Kafka Streams, Flink with checkpointing). Without it, consumer restarts produce duplicates (at-least-once) or lost messages (at-most-once). Use transactional producers and consumers for financial or audit data.

## Mental Model

Think of a streaming pipeline as a **river with measuring stations**. Events are water molecules flowing downstream. Watermarks are markers on the bank: "I'm confident all water upstream of this point has passed." Windowing is measuring the total water flow past a station in one-minute intervals. Late-arriving water (upstream tributaries with slow flow) arrives after the measuring interval has been recorded — you either extend the measurement window or acknowledge the slight inaccuracy.

**Mini Summary**: Streaming processes events in near-real-time (<1 second latency). Kafka topics are partitioned append-only logs; consumer groups share partition assignments. Event time (when it happened) vs processing time (when we processed it) diverges for late-arriving events. Watermarks determine when to close windows. Windowing types: tumbling (fixed non-overlapping), sliding (overlapping), session (inactivity-gap). Use streaming for fraud detection, monitoring, and real-time metrics.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium wants real-time learner engagement metrics: "How many lessons have been completed in the past 5 minutes?" and "Alert if a user completes more than 20 lessons in 10 minutes (possible cheating)."

Design the streaming pipeline:
1. What Kafka topic structure (topic name, partition key, message schema) would you use for lesson completion events?
2. Which windowing type would you use for each of the two queries, and why?
3. How would you handle the case where a student's mobile device reconnects after 20 minutes offline and publishes 15 buffered completion events at once?

---

# Integration

**Mathematics**: Windowing in stream processing is the **discretisation** of a continuous event-time domain into countable intervals. Tumbling windows partition the time axis into non-overlapping half-open intervals [t, t+w). Sliding windows create overlapping partitions every step s. A sliding window of size w and step s assigns each event to ceil(w/s) windows. The trade-off between latency and accuracy mirrors the **sampling theorem**: a window emitting results every s seconds can only capture frequency components up to 1/(2s) Hz — events with recurrence periods shorter than 2s will be aliased (appear in the wrong window). Stream processing watermarks are an application of **causal ordering** from distributed systems theory.

**Sciences**: Streaming event processing mirrors **action potential propagation in neurons**. Individual signals (events) travel along the axon (Kafka partition). The neuron integrates signals within a temporal window (sliding window): if enough signals arrive within the integration period, the neuron fires (threshold alert). The refractory period (cooldown after firing) is equivalent to session windows — the neuron won't fire again until activity drops below threshold for a defined gap. The brain's real-time decision-making architecture is a massively parallel streaming system with exactly these components.

---

# The Alert

Thirty-seven seconds after the suspicious transaction pattern began, the fraud alert fired. The fraud analyst was already reviewing it. "The old system would have flagged this at the 4-hour batch run," the Lead Data Engineer said. "The money would have been long gone." The Senior Engineer watched the Flink job dashboard. Watermarks advancing, windows closing, results flowing. Streaming had changed what was possible.
