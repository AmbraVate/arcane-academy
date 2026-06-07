---
id: se-sen-m7-03
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m7
moduleTitle: "Module 7: Event-Driven Architecture"
moduleGlyph: "📨"
moduleSortOrder: 7
topicSlug: publish_subscribe
topicTitle: "Publish-Subscribe"
topicSortOrder: 3
lesson: publish_subscribe
title: "Publish-Subscribe"
sortOrder: 3
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [message_queues]
integrationDomains: [design, psychology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Explains the difference between pub/sub and point-to-point messaging"
    - "Describes topic-based vs content-based subscription routing"
    - "Explains fan-out and why it enables decoupling"
    - "Identifies when a durable subscription is needed vs non-durable"
    - "Names a real-world consequence of pub/sub debugging complexity"
  keywords: [publisher, subscriber, topic, fan-out, durable, decouple, broadcast, filter, event, subscription]
  modelAnswer: |
    Pub/Sub vs point-to-point:
    - Point-to-point (queue): one message → one consumer
    - Pub/Sub (topic): one message → many subscribers

    Fan-out: OrderPlaced event published once.
    Subscribers: InventoryService, EmailService, AnalyticsService all receive it.
    Publisher doesn't know who subscribes — true decoupling.

    Durable subscription: subscriber keeps receiving messages even while offline.
    Required for: critical event processing (payment confirmations).
    Non-durable: subscriber misses messages while disconnected.
    Suitable for: live dashboards, optional notifications.

    Debugging challenge: when OrderPlaced triggers a chain of events,
    tracing failures across 5 independent subscribers requires
    distributed tracing (correlation IDs, OpenTelemetry).
guidedSteps:
  - id: ps-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In a pub/sub system, when `OrderService` publishes an `OrderPlaced` event to a topic,
      what happens if no subscribers are currently listening?
    inputConfig:
      options:
        - "The publisher blocks until a subscriber is available"
        - "The event is discarded (non-durable) or retained for durable subscribers"
        - "The publisher receives an error"
        - "The event is returned to the publisher"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The event is discarded (non-durable) or retained for durable subscribers"]
      rejectedFeedback: "Pub/sub systems decouple publishers from subscribers completely. Without a durable subscription, events published while no subscriber is connected are lost. With a durable subscription (or with Kafka's persistent log), the subscriber will receive the events when it reconnects."
    hint: "The answer depends on whether the subscription is durable. What happens without a durable subscription?"
    reflectionPrompt: "This is a key design decision. Non-durable pub/sub suits live feeds (a missing notification is acceptable). Durable subscriptions suit critical business events (missing an order event is not acceptable). Choose deliberately."
  - id: ps-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In pub/sub, when one published event reaches multiple independent subscribers simultaneously,
      this is called ___.
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["fan-out", "fanout", "fan out", "broadcasting"]
      rejectedFeedback: "**Fan-out** is when one published message reaches multiple subscribers simultaneously. It enables decoupling: the publisher sends once, and any number of downstream services receive the event without the publisher knowing about them."
    hint: "Think of the shape — one input, many outputs. Like a fan spreading outward."
    reflectionPrompt: "Fan-out enables truly independent services. Adding a new subscriber (e.g. a new fraud detection service) requires no change to the publisher. The architecture scales horizontally without coordination."
  - id: ps-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe a specific debugging challenge introduced by pub/sub that doesn't exist in direct synchronous service calls. How would you address it?
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [trace, correlation, id, async, chain, failure, subscriber, log, distributed, invisible]
      rejectedFeedback: "In direct calls, a stack trace shows you the call chain. In pub/sub, an event triggers multiple async subscribers across services. If one fails, there's no automatic stack trace linking it to the original event. Solution: propagate a correlation ID with every event; use distributed tracing (OpenTelemetry) to track the full event chain across services."
    hint: "When an event causes a downstream failure in a different service, how do you connect them in your logs?"
    reflectionPrompt: "Correlation IDs are the minimum: every event carries a trace ID that propagates through all derived events and service calls. With OpenTelemetry, this becomes a complete distributed trace visualisable in Jaeger or Zipkin."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the key difference between topic-based and content-based subscription routing?"
    options:
      - "Topic-based is faster; content-based is more reliable"
      - "Topic-based routes by a named channel; content-based filters messages by their content/attributes"
      - "Topic-based is for events; content-based is for commands"
      - "They are the same thing with different broker support"
    correctIndex: 1
    feedback: "Topic-based: subscribers register for named topics ('orders.placed'). Content-based: subscribers specify filters on message attributes ('deliver only if amount > 1000'). Content-based is more powerful but harder to reason about and often harder to scale."
  - type: MULTIPLE_CHOICE
    question: "Which of these use cases is BEST suited to a non-durable pub/sub subscription?"
    options:
      - "Processing payment confirmations"
      - "Updating inventory after an order"
      - "A live dashboard showing currently active users"
      - "Sending order confirmation emails"
    correctIndex: 2
    feedback: "A live dashboard only cares about current state — it's acceptable to miss events while offline (the next event will update it). Payment processing, inventory updates, and order emails are all critical — missing a message causes real business harm, so durable subscriptions are required."

retrieval:
  recall: "What is the difference between a message queue (point-to-point) and pub/sub (topic)? When would you choose each?"
  explain: "Explain to a junior developer why pub/sub makes services more independent than direct service-to-service calls."
  mistakeId:
    code: |
      // UserService publishes events
      eventBus.publish("user.registered", new UserRegisteredEvent(userId));

      // EmailService subscribes
      @Subscribe("user.registered")
      void sendWelcomeEmail(UserRegisteredEvent event) { ... }

      // 6 months later, a developer renames the event field:
      // UserRegisteredEvent.userId → UserRegisteredEvent.id
      // The change looks safe — compiler shows no errors elsewhere
    answer: "Pub/sub decouples publishers and subscribers, which means schema changes are invisible to the compiler. Renaming a field breaks all subscribers deserialising the event — but the publisher compiles and runs fine. Mitigations: event schema registry (enforces compatibility), schema versioning (never remove/rename fields — add new ones), contract testing between publishers and subscribers."
---

# Hook

`OrderService` places an order. `InventoryService` needs to reserve stock. `EmailService` needs to send a confirmation. `AnalyticsService` needs to record the event. `FraudService` needs to run a check.

Option A: `OrderService` calls all five services directly. It now knows about all five, is coupled to all five, and breaks if any of them are slow or unavailable.

Option B: `OrderService` publishes an `OrderPlaced` event to a topic. Any interested service subscribes. `OrderService` knows about none of them.

Option B is pub/sub. And it scales to fifty services as easily as five.

> Can you think of a system where one "thing happens" and many independent processes need to react — without the originator knowing who they are?

# Lore Introduction

The Academy's announcement bell rings once per hour. Every apprentice, every artificer, every guard hears it. Each acts according to their own role: apprentices go to class, artificers begin their crafting windows, guards rotate positions. The bell does not know who hears it or how they respond.

*"The bell has no relationships,"* Archmage Veylan says. *"That is its power. I can add a hundred listeners without touching the bell. And the bell can be replaced without touching a single listener."*

# Core Learning

## Concept Introduction

**Pub/Sub (Publish-Subscribe)** decouples message producers (publishers) from message consumers (subscribers) via a shared **topic**.

```
Publisher                   Topic                Subscribers
OrderService  ─── OrderPlaced ──→  orders.placed  ─── InventoryService
                                                  ─── EmailService
                                                  ─── AnalyticsService
                                                  ─── FraudService
```

The publisher sends once; all subscribers receive independently.

**Spring ApplicationEvents (in-process pub/sub):**
```java
// Publisher
@Service
public class OrderService {
    private final ApplicationEventPublisher eventPublisher;

    public Order placeOrder(OrderRequest request) {
        Order order = createOrder(request);
        eventPublisher.publishEvent(new OrderPlacedEvent(order.getId()));
        return order;
    }
}

// Subscriber
@Component
public class InventoryEventHandler {
    @EventListener
    public void onOrderPlaced(OrderPlacedEvent event) {
        inventoryService.reserveStock(event.getOrderId());
    }
}
```

**External pub/sub (RabbitMQ topics, SNS, Kafka topics)** works the same way but across process boundaries.

## Why It Matters

Pub/sub enables:
- **Decoupling** — publishers and subscribers don't know each other
- **Extensibility** — new subscribers added without changing the publisher
- **Independent scaling** — each subscriber scales separately
- **Resilience** — a slow subscriber doesn't block the publisher
- **Audit trail** — events are a natural record of what happened

## Worked Examples

**Fan-out to multiple subscribers:**
```java
// One event, three independent handlers
@EventListener
public void sendConfirmationEmail(OrderPlacedEvent e) { ... }

@EventListener
public void reserveInventory(OrderPlacedEvent e) { ... }

@EventListener
public void recordForAnalytics(OrderPlacedEvent e) { ... }
```

**Async event handling (non-blocking):**
```java
@EventListener
@Async  // runs in separate thread pool — publisher is not blocked
public void processHeavyTask(OrderPlacedEvent e) {
    analyticsService.processExpensiveReport(e.getOrderId());
}
```

**External pub/sub with correlation ID:**
```java
public class OrderPlacedEvent {
    private final UUID orderId;
    private final String correlationId;  // for distributed tracing
    private final Instant occurredAt;
    // immutable — events are facts, not commands
}
```

## Common Mistakes

- **Missing correlation IDs** — impossible to trace event chains across services without them.
- **Mutable events** — events represent things that happened; they should be immutable facts.
- **Ignoring subscriber failures** — if a subscriber fails, are you OK with losing the event? Use dead letter queues.
- **Over-using pub/sub for synchronous operations** — if you need an immediate response, use direct calls.
- **Schema changes breaking subscribers silently** — the compiler won't catch field renames across topics.

## Mental Model

Pub/sub is a **bulletin board**. The poster pins a notice and walks away. Anyone who passes and is interested reads it. New notice-readers appear; old ones leave. The poster never knows. The bulletin board is the only shared point — and it's stateless. This is maximum decoupling.

## Mini Summary

- ✔ Pub/sub: one publisher → one topic → many independent subscribers (fan-out)
- ✔ Publisher and subscribers are completely decoupled — neither knows the other
- ✔ Durable subscriptions: messages retained while subscriber is offline (critical for business events)
- ✔ Correlation IDs are essential for distributed tracing across event chains
- ✔ Events should be immutable facts; schema changes require backward-compatible evolution

# Guided Practice Quest

**The Academy Announcement Bell**

Model a pub/sub system for Academy-wide events. Wire publishers and subscribers and evaluate the decoupling properties.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

A marketplace application needs to handle a `PaymentReceived` event. The following services need to react:
- `OrderService`: mark the order as paid
- `InventoryService`: confirm stock reservation
- `ShipmentService`: trigger fulfilment workflow
- `EmailService`: send receipt to buyer
- `SellerNotificationService`: notify seller
- `FraudMonitoringService`: run post-payment fraud check

Design the pub/sub architecture for this:
1. What topic(s) would you create? (Consider whether all six services should subscribe to the same topic or different topics)
2. Which subscribers need durable subscriptions and why?
3. Which subscribers could be non-durable and why?
4. How would you handle `FraudMonitoringService` detecting fraud after payment is already confirmed?
5. What information would you include in the `PaymentReceived` event schema?

# Integration

**Connecting to Psychology — The Observer Effect in Social Systems**

Psychologist Robert Cialdini's research on social proof shows that people's behaviour is influenced by observing others (the observer effect in social psychology). Systems that broadcast signals to many participants create emergent coordination: market price discovery, viral social media, cascading failures in interconnected systems.

Pub/sub architectures exhibit similar emergent properties. One event triggers N subscribers, each triggering further events, potentially cascading in unexpected ways. A payment event triggers inventory, which triggers shipping, which triggers notification — a four-step chain from one message. In complex systems, these chains can create feedback loops: an event triggers a subscriber that triggers a related event that re-triggers the first subscriber.

Designing event-driven systems requires awareness of these emergent dynamics. Event loops must be explicitly prevented (check if the event being published would re-trigger your own listener). Cascades should be monitored for unexpected depth. The simplicity of pub/sub (publish and forget) masks the complexity of what it sets in motion.

How does understanding emergent behaviour in event-driven systems change how you design event schemas and subscriber logic?

# Lore Conclusion

The announcement bell rings. Five different Academywide services react, each independently, each correctly.

*"Notice,"* Archmage Veylan says, *"that I added the analytics service last month and changed nothing about the bell. That is the value of decoupling — the system grows without coordination cost."*

But also notice: when the analytics service failed silently, it took two days to discover that it had missed three thousand events. Decoupling reduces coupling costs. It does not eliminate operational complexity.
---
