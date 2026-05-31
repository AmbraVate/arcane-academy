---
id: se-sen-m7-04
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m7
moduleTitle: "Module 7: Event-Driven Architecture"
moduleGlyph: "📨"
moduleSortOrder: 7
topicSlug: kafka
topicTitle: "Kafka"
topicSortOrder: 4
lesson: kafka_fundamentals
title: "Kafka Fundamentals"
sortOrder: 4
difficulty: 4
estimatedMinutes: 32
xpReward: 65
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [publish_subscribe]
integrationDomains: [design, mathematics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Explains topics, partitions, and offsets correctly"
    - "Describes how consumer groups achieve parallel consumption"
    - "Explains the retention model and how it differs from traditional queues"
    - "Names at least two scenarios where Kafka is appropriate vs overkill"
    - "Describes at-least-once delivery and how to handle duplicate events"
  keywords: [topic, partition, offset, consumer, group, retention, log, replay, throughput, idempotent]
  modelAnswer: |
    Kafka fundamentals:

    Topic: named stream of events. e.g. "orders"
    Partition: ordered, immutable log within a topic.
      - Messages within a partition maintain order.
      - Messages across partitions have no guaranteed order.
    Offset: position of a message within a partition.
      - Consumers track their offset — allowing replay from any point.

    Consumer group: N consumers sharing a topic's partitions.
      - Each partition assigned to one consumer in the group.
      - Parallelism = number of partitions.

    Retention: events kept for configured duration (e.g. 7 days).
      - Unlike queues, messages aren't deleted on consumption.
      - Enables replay, auditing, backfill of new services.

    At-least-once delivery: consumers may receive duplicates on restart.
    Idempotent consumers handle this: check if event already processed before acting.
guidedSteps:
  - id: kafka-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A Kafka topic `orders` has 6 partitions and a consumer group has 4 consumers.
      How are the partitions distributed?
    inputConfig:
      options:
        - "Each consumer reads all 6 partitions"
        - "The 6 partitions are distributed across 4 consumers — some consumers handle more than one partition"
        - "Only 4 partitions are used; 2 are idle"
        - "Kafka randomly assigns messages to consumers regardless of partitions"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The 6 partitions are distributed across 4 consumers — some consumers handle more than one partition"]
      rejectedFeedback: "Kafka assigns each partition to exactly one consumer in a group. With 6 partitions and 4 consumers: 2 consumers get 2 partitions; 2 consumers get 1 partition. If you had more consumers than partitions, some consumers would be idle."
    hint: "Each partition goes to exactly one consumer. With more partitions than consumers, some consumers get more than one."
    reflectionPrompt: "The maximum parallelism equals the number of partitions. To increase throughput, increase partitions (and consumers). This is Kafka's horizontal scaling model."
  - id: kafka-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In Kafka, the position of a message within a partition is identified by its ___.
      Consumers store this position so they know which messages they've processed.
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["offset"]
      rejectedFeedback: "The **offset** is the sequential position of a message in a partition (0, 1, 2, ...). Consumers commit their offset to Kafka, which enables them to resume from where they left off after a restart — or replay from any point by resetting the offset."
    hint: "It's a position number within the partition. What do you call a pointer that tracks 'I've read up to here'?"
    reflectionPrompt: "Committed offsets are what make Kafka fault-tolerant. If a consumer crashes, it restarts and reads from its last committed offset. No message is lost. Messages may be reprocessed (at-least-once delivery) — your consumers should handle this."
  - id: kafka-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Kafka retains events for a configurable period rather than deleting them when consumed. Name two architectural capabilities this retention model enables that a traditional queue cannot provide.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [replay, reprocess, backfill, audit, new, service, history, past, rebuild, recover]
      rejectedFeedback: "Retention enables: (1) **Event replay** — reprocess all historical events to rebuild state or recover from errors. (2) **New service backfill** — when a new service is added, it can consume all historical events from the beginning to build its initial state. Traditional queues delete messages on consumption, so backfilling is impossible."
    hint: "Think about what's impossible if messages are deleted as soon as they're read."
    reflectionPrompt: "Kafka's log model (persistent, replayable) is fundamentally different from a queue (ephemeral, consumed-and-gone). This makes Kafka suitable for event sourcing, audit trails, and microservice decoupling at scale."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is Kafka's delivery guarantee by default?"
    options:
      - "Exactly-once — each message is processed exactly once"
      - "At-least-once — consumers may receive duplicates but no message is lost"
      - "At-most-once — messages may be lost but never duplicated"
      - "Best-effort — no guarantees"
    correctIndex: 1
    feedback: "Kafka's default is at-least-once. If a consumer processes a message but crashes before committing its offset, it will reprocess the message on restart. To handle this, consumers should be idempotent (processing the same event twice has the same effect as once)."
  - type: MULTIPLE_CHOICE
    question: "When would you choose RabbitMQ over Kafka?"
    options:
      - "When you need extremely high throughput (millions of events/sec)"
      - "When you need simple point-to-point messaging with routing flexibility and don't need event replay"
      - "When you need to retain events for months"
      - "When you need to replay events for new services"
    correctIndex: 1
    feedback: "RabbitMQ excels at: flexible routing (exchanges, bindings), lower operational complexity, simpler semantics for task queues. Choose Kafka when: you need event replay, high throughput at scale, multiple independent consumers, or event sourcing patterns."

retrieval:
  recall: "Explain Kafka's topic-partition-offset model. How does it enable consumer parallelism?"
  explain: "Explain to a colleague why Kafka's event retention makes it different from a message queue, and name a use case that benefits from this."
  mistakeId:
    code: |
      // Consumer processes OrderPlaced event
      @KafkaListener(topics = "orders")
      public void handleOrder(OrderPlacedEvent event) {
          // Create shipment
          shipmentService.createShipment(event.getOrderId());
          // Note: offset is auto-committed before processing completes
      }
    answer: "Auto-committing the offset before processing completes means if `createShipment` fails after the offset is committed, the event is lost — at-most-once delivery. Configure `enable.auto.commit=false` and manually commit after successful processing: `acknowledgment.acknowledge()`. Also, make `createShipment` idempotent (check if shipment already exists) to handle at-least-once redeliveries safely."
---

# Hook

A message queue processes millions of events per day. Traditional queues delete messages when consumed. A new analytics service is launched — and it needs three months of historical order data that no longer exists.

Kafka was built to solve exactly this. It's a distributed event log — not a queue. Events are retained, replayable, and consumable by many independent groups simultaneously.

Understanding Kafka means understanding the shift from "messages are tasks to be consumed" to "events are facts to be observed, now and in the future."

> If every event in your system were retained for a year, what new capabilities would that enable?

# Lore Introduction

The Academy's Grand Archive doesn't discard the enchantment ledger when an artificer reads it. Every enchantment ever cast remains inscribed, in order, indefinitely. New archivists joining the Academy can read the entire history. A rune-scholar can replay the last century's enchantments to understand patterns the original artificers never noticed.

*"A queue is a temporary message,"* Archmage Veylan says. *"A log is a permanent record. These are not the same thing. Most architectures need the log."*

# Core Learning

## Concept Introduction

**Apache Kafka** is a distributed event streaming platform built around a persistent, partitioned log.

**Core concepts:**

```
Topic: "orders"
├── Partition 0: [event@0, event@1, event@2, ...]
├── Partition 1: [event@0, event@1, ...]
└── Partition 2: [event@0, ...]

Consumer Group "inventory-service":
├── Consumer A → Partition 0
├── Consumer B → Partition 1
└── Consumer C → Partition 2
```

- **Topic** — named category of events
- **Partition** — ordered, immutable sequence within a topic; enables parallelism
- **Offset** — position within a partition; consumers track their position
- **Consumer Group** — consumers sharing partitions for parallel processing
- **Retention** — events kept for configured time (days/weeks/forever), regardless of consumption

## Why It Matters

Kafka enables:
- **High throughput** — millions of events per second, horizontal scaling
- **Multiple independent consumers** — ten services can all read the same topic without interfering
- **Event replay** — reprocess historical events for debugging, rebuilding state, new services
- **Decoupling** — producers and consumers are completely independent
- **Event sourcing** — Kafka's log is a natural event store

## Worked Examples

**Spring Kafka producer:**
```java
@Service
public class OrderEventPublisher {
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void publishOrderPlaced(Order order) {
        kafkaTemplate.send("orders.placed",
            order.getId().toString(),        // partition key
            new OrderPlacedEvent(order.getId(), order.getTotal()));
    }
}
```

**Spring Kafka consumer:**
```java
@Component
public class InventoryEventConsumer {
    @KafkaListener(topics = "orders.placed",
                   groupId = "inventory-service")
    public void handleOrderPlaced(OrderPlacedEvent event,
                                  Acknowledgment ack) {
        inventoryService.reserveStock(event.getOrderId());
        ack.acknowledge();  // manual offset commit after successful processing
    }
}
```

**application.properties:**
```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=inventory-service
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.enable-auto-commit=false
```

## Common Mistakes

- **Auto-committing offsets before processing** — creates at-most-once delivery (messages lost on failure).
- **Non-idempotent consumers** — at-least-once delivery means duplicates are possible; consumers must handle them.
- **Too few partitions** — partition count limits parallelism; plan ahead (partitions can be added but not removed).
- **Using Kafka for simple task queues** — Kafka's operational complexity is only justified at scale; use RabbitMQ for simple routing.
- **Key-less messages** — without a partition key, related events may land in different partitions and lose ordering guarantees.

## Mental Model

Kafka is a **magnetic tape archive** for digital events. Traditional queues are like radio broadcasts — received once, then gone. Kafka is like recording everything to tape: the original broadcast is preserved, and any number of listeners can play it back from any point. New listeners can hear the whole history. And the tape never rewinds unless you tell it to.

## Mini Summary

- ✔ Kafka = persistent distributed log; not a queue — events are retained after consumption
- ✔ Topics → Partitions → Offsets: the fundamental data model
- ✔ Consumer groups: partitions shared across consumers for parallel processing
- ✔ Event retention enables replay, backfilling new services, audit trails
- ✔ Use manual offset commit + idempotent consumers to achieve safe at-least-once processing

# Guided Practice Quest

**The Grand Archive**

Design the partition and consumer group model for an order processing system using Kafka. Evaluate throughput, ordering guarantees, and replay capabilities.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You are designing Kafka infrastructure for a fintech platform with three event streams:
1. `transactions` — millions of payment events per day; ordering per-user required
2. `alerts` — fraud alerts generated by a ML model; low volume, multiple consumers
3. `audit-log` — regulatory requirement; events must be retained indefinitely

For each topic:
1. How many partitions would you recommend and why?
2. What would you use as the partition key and why?
3. What retention policy would you configure?
4. How many consumer groups would you expect?
5. What delivery guarantee approach (idempotency vs exactly-once transaction API) would you use?

# Integration

**Connecting to Mathematics — The Distributed Log as a Mathematical Structure**

Jay Kreps (Kafka's creator) wrote an influential post, "The Log: What every software engineer should know about real-time data's unifying abstraction." He argues that the append-only sequential log is the most fundamental data structure in distributed systems — more fundamental than databases, message queues, or caches.

Mathematically, a log is a **monoid** under concatenation: associative (order of combination is irrelevant for the end result) and with an identity element (empty log). This mathematical property makes logs composable and fault-tolerant. Two partial logs can be merged; a log can be replicated exactly by copying its sequence.

The log's mathematical properties explain why it's so powerful: any state can be derived from a log by replaying it (the "event sourcing" pattern). Databases are just materialised views of an underlying log. Replication is just log shipping. Consensus algorithms (Raft, Paxos) are just distributed agreement on log entries.

Understanding Kafka as an implementation of the mathematical log concept — rather than "a better message queue" — reveals why it's architecturally significant.

How does thinking of Kafka as a mathematical log rather than a messaging system change how you might design data pipelines?

# Lore Conclusion

The Grand Archive grows. Every enchantment is logged, partitioned by type, retained indefinitely. New scholars join the Academy and read the full history. Old patterns are discovered in historical data that were invisible at the time.

*"The log is the truth,"* Archmage Veylan says. *"Everything else — every summary, every cache, every derived view — is just a convenient materialisation of the log. If you have the log, you can derive everything else. If you lose the log, you lose the past."*

Guard the log. It is the foundation.
---
