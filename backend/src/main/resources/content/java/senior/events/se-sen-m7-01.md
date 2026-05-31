---
id: se-sen-m7-01
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m7
moduleTitle: "Module 7: Event-Driven Architecture"
moduleGlyph: "📨"
moduleSortOrder: 7
topicSlug: events
topicTitle: "Events"
topicSortOrder: 1
lesson: events
title: "Events"
sortOrder: 1
difficulty: 3
estimatedMinutes: 25
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [design, history]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Distinguishes domain events (within a bounded context) from integration events (crossing context boundaries) and explains the schema implications"
    - "Explains why events are immutable records of facts that happened, not commands to be executed"
    - "Describes event schema design principles including versioning concerns"
    - "Explains the CloudEvents specification and why a standard envelope schema matters"
    - "Introduces event sourcing as the pattern of storing state as a sequence of events"
  keywords: [domain event, integration event, immutability, event schema, CloudEvents, event sourcing, event-driven, request-driven, versioning, fact]
  modelAnswer: |
    An event is a record of something that happened — a fact about a state change in a system. Events are immutable: once something has happened, it cannot be un-happened. "OrderPlaced", "PaymentProcessed", "UserDeactivated" are events. They describe completed facts in the past tense. This distinguishes events from commands ("PlaceOrder", "ProcessPayment") which are requests for future action. The distinction matters: an event announces a fact and any interested party may react; a command is directed at a specific handler and implies synchronous expectation of response.

    Domain events capture significant state changes within a bounded context. They are the vocabulary of the domain — the things the business cares about. Integration events cross context boundaries, enabling loose coupling between contexts. Domain events may be fine-grained and internally detailed; integration events should be coarser-grained and stable (changing their schema affects all consumers). Domain events may be published internally (within the same JVM via Spring ApplicationEvents); integration events require a durable messaging infrastructure.

    Event schema design must balance expressibility with stability. Envelope fields are standard: event ID (UUID), event type (string, versioned), source (originating service), timestamp (ISO-8601), and correlation ID. Payload fields carry the specific fact data. Schema versioning is critical: once an integration event is published and consumers depend on its schema, changes must be backward-compatible (additive only — new optional fields). Breaking changes (renaming, removing, or changing field types) require a new event version with a migration strategy for existing consumers.

    CloudEvents is a CNCF specification for a standard event envelope, enabling tool interoperability across event brokers, cloud providers, and SDKs. CloudEvents defines mandatory fields (id, source, specversion, type) and optional fields (datacontenttype, time, subject). Adopting CloudEvents means events from different services can be routed and processed by standard tooling without bespoke parsers.

    Event sourcing is the pattern of storing the application's state as an append-only log of events rather than current-state snapshots. To reconstruct current state, replay all events from the beginning (or from the last snapshot). Benefits: complete audit history, temporal queries ("what was the state at time T?"), and decoupling of write (events) from read (projections). Cost: query complexity (cannot easily query across event streams) and replay performance for long event histories.
guidedSteps:
  - id: ev-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of the following is correctly modelled as an event (not a command)?
    inputConfig:
      options:
        - "A. SendWelcomeEmail"
        - "B. CreateUserAccount"
        - "C. UserRegistered"
        - "D. ValidatePaymentDetails"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["C"]
      rejectedFeedback: "Events describe things that have already happened — past tense facts. 'UserRegistered' is a fact: the user has been registered. 'SendWelcomeEmail' and 'CreateUserAccount' are commands — they direct future action. 'ValidatePaymentDetails' is a query/command. Events are named in past tense and describe completed state changes."
    hint: "Events are named in past tense and describe completed facts."
    reflectionPrompt: "The naming convention (past tense) is not stylistic — it encodes the semantic difference between events (facts) and commands (requests)."
  - id: ev-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Events that cross bounded context boundaries and require durable messaging infrastructure are called ___ events.
    inputConfig:
      placeholder: "event type"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["integration", "integration events", "integration event"]
      rejectedFeedback: "Integration events cross context boundaries. They require careful schema design and versioning because multiple external consumers depend on them. Domain events are internal to a bounded context and can be less stable. Both are events, but their audience and infrastructure implications differ."
    hint: "Think about the scope: within one bounded context vs crossing between contexts."
    reflectionPrompt: "Integration events are API contracts — their schema stability affects all downstream consumers."
  - id: ev-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A team is publishing an `OrderPlaced` integration event. A new business requirement adds a `discountCode` field to orders. Explain the correct schema evolution strategy to avoid breaking existing event consumers.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [backward-compatible, optional, additive, versioning, consumers, breaking, schema, new field]
      rejectedFeedback: "Add discountCode as an optional field with a null/absent default. Existing consumers that do not read the field are unaffected — this is a backward-compatible additive change. Never remove or rename fields in a published integration event schema. If a breaking change is unavoidable, create a new event type (e.g. OrderPlacedV2) and run both versions in parallel during consumer migration."
    hint: "What kind of schema change can existing consumers handle without modification?"
    reflectionPrompt: "Integration event schemas are public contracts. Every consumer breaking change is a coordination cost. Design schemas to be additive-only."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why are events immutable once published?"
    options:
      - "A. Message brokers do not support event updates"
      - "B. Events record facts that have already occurred — you cannot un-happen the past; mutability would corrupt the historical record"
      - "C. Immutability makes events faster to process"
      - "D. The CloudEvents spec mandates immutability for compliance"
    correctIndex: 1
    feedback: "Events represent historical facts. 'OrderPlaced at 14:32:05 for £49.99' is a fact that occurred. Mutating it would falsify history. Immutability enables reliable audit trails, event replay, and temporal queries. If the fact was wrong, a new compensating event ('OrderCancelled') records the correction — it does not rewrite history."
  - type: MULTIPLE_CHOICE
    question: "What does the CloudEvents specification standardise?"
    options:
      - "A. The internal format of event payloads (JSON schema)"
      - "B. The event envelope (id, source, type, time) enabling cross-tool interoperability"
      - "C. The message broker protocol (AMQP vs Kafka)"
      - "D. The programming language SDK for event handling"
    correctIndex: 1
    feedback: "CloudEvents standardises the event envelope — the metadata wrapper around any event payload. Standard envelope fields enable routers, loggers, and processing tools to understand events without knowing the specific payload schema. This interoperability is the value of the spec."
retrieval:
  recall: "Distinguish domain events from integration events — what is different about their scope, schema stability requirements, and infrastructure needs?"
  explain: "Explain to a junior developer why 'OrderPlaced' is a better event name than 'PlaceOrder', using the command vs event distinction."
  mistakeId:
    code: |
      // Integration event published by Order Service
      {
        "type": "order.placed",
        "orderId": "abc123",
        "totalAmount": 49.99,
        "currency": "GBP",
        "customerId": "cust456"
      }

      // New version: rename "totalAmount" to "amount" and remove "currency"
      // (assuming all amounts are now GBP)
      {
        "type": "order.placed",
        "orderId": "abc123",
        "amount": 49.99,
        "customerId": "cust456"
      }
    answer: "Renaming 'totalAmount' to 'amount' and removing 'currency' are breaking changes to a published integration event schema. All consumers reading 'totalAmount' will receive null/absent and fail. Consumers that relied on 'currency' lose that information. The correct approach: keep 'totalAmount' (and optionally add 'amount' as alias), keep 'currency' (or default it). Breaking changes require a new event type 'order.placed.v2' with a migration plan. Never modify the schema of an already-published event type."
---

# Hook

An order is placed on your e-commerce platform. Twenty things need to happen: the inventory must be updated, the customer confirmation email must be sent, the analytics pipeline must record the conversion, the loyalty points must be credited, the warehouse must be notified, the payment must be captured, and fourteen more things. In a request-driven architecture, the order service must call all twenty systems synchronously, knowing all their APIs, and failing if any one is unavailable. In an event-driven architecture, the order service publishes one event: `OrderPlaced`. All twenty systems subscribe to it. The order service does not know they exist. It does not need to. That is the power of events.

# Lore Introduction

The Academy's historians record that the greatest architectural evolution of the modern era was the shift from "ask and wait" to "announce and move on." Request-driven systems are like couriers: every message has a specific recipient and the sender waits for a reply. Event-driven systems are like town criers: the crier announces the fact, and whoever needs to act on it does so at their own pace. The event is the fact. The announcement is the publishing. The reaction is the consumer's business. What separates novice architects from masters is understanding exactly what to announce, when to announce it, and how to design the announcement so that it remains true and useful long after the first listener has gone.

# Core Learning

## Concept Introduction

**Event vs Command**

An event announces that something has happened — a completed fact:
- `OrderPlaced`, `PaymentFailed`, `UserDeactivated`, `StockDepleted`

A command requests that something should happen — a directive:
- `PlaceOrder`, `ProcessPayment`, `DeactivateUser`, `ReplenishStock`

Events are named in past tense. They carry no expectation of response. Any subscriber may or may not act. Commands are directed at specific handlers and imply a response expectation.

**Domain Event vs Integration Event**

| | Domain Event | Integration Event |
|--|-------------|------------------|
| Scope | Within bounded context | Crosses context boundaries |
| Schema stability | Internal — can change | External contract — must be stable |
| Infrastructure | In-process (Spring ApplicationEvents) or internal bus | Durable message broker (Kafka, RabbitMQ) |
| Example | `OrderLineItemAdded` (internal) | `OrderPlaced` (published to other services) |

**Event Schema Design**

```json
{
  "specversion": "1.0",
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "type": "com.example.order.placed",
  "source": "/orders-service",
  "time": "2026-05-31T14:32:05Z",
  "correlationid": "req-789",
  "datacontenttype": "application/json",
  "data": {
    "orderId": "abc123",
    "customerId": "cust456",
    "totalAmount": 49.99,
    "currency": "GBP",
    "items": [...]
  }
}
```

**Event Sourcing**

Store state as events, not snapshots:
```
Event 1: AccountOpened(accountId, ownerId, 2024-01-01)
Event 2: DepositMade(accountId, 100.00, 2024-01-15)
Event 3: WithdrawalMade(accountId, 30.00, 2024-01-20)
Event 4: DepositMade(accountId, 50.00, 2024-02-01)
→ Current balance: 0 + 100 - 30 + 50 = 120.00
```

The complete event history is the source of truth. Snapshots are performance optimisations, not the truth itself.

## Why It Matters

Events decouple producers from consumers. The Order Service does not know about the Email Service, Analytics Pipeline, or Loyalty Service. It publishes facts; others subscribe. Adding a new consumer requires no change to the producer — it subscribes to the existing event. This is the Open/Closed Principle at the architectural level: open for extension (new consumers), closed for modification (producer unchanged). Events also provide a natural audit log and enable event sourcing, temporal queries, and replay-based reprocessing.

## Worked Examples

**Example 1: Spring ApplicationEvent (Domain Event)**

```java
// Domain event
public record OrderPlacedEvent(String orderId, String customerId, BigDecimal total) {}

// Publisher (Order Service)
@Service
public class OrderService {
    private final ApplicationEventPublisher publisher;
    
    public Order placeOrder(OrderRequest request) {
        Order order = createOrder(request);
        orderRepository.save(order);
        publisher.publishEvent(new OrderPlacedEvent(
            order.getId(), order.getCustomerId(), order.getTotal()));
        return order;
    }
}

// Subscriber (Email Service, same JVM)
@Component
public class OrderEmailHandler {
    @EventListener
    public void onOrderPlaced(OrderPlacedEvent event) {
        emailService.sendConfirmation(event.customerId(), event.orderId());
    }
}
```

**Example 2: CloudEvents JSON Structure**

```java
// CloudEvents-compliant event builder
CloudEvent event = CloudEventBuilder.v1()
    .withId(UUID.randomUUID().toString())
    .withType("com.example.order.placed")
    .withSource(URI.create("/orders-service"))
    .withDataContentType("application/json")
    .withExtension("correlationid", correlationId)
    .withData(objectMapper.writeValueAsBytes(orderPlacedData))
    .build();
```

## Common Mistakes

- **Commands disguised as events**: naming events as commands (`ProcessPayment`) instead of facts (`PaymentRequested`) creates semantic confusion and makes it ambiguous whether a response is expected.
- **Omitting correlation IDs**: distributed tracing requires correlation IDs propagated through all events in a flow. Missing IDs make it impossible to reconstruct request flows across services.
- **Payload too large**: events should carry enough context for consumers to act without additional database queries, but not so much that they become unwieldy. Include business keys, not entire entity graphs.
- **Mixing domain and integration event schemas**: using the same internal event class for both internal publishing and external messaging couples internal implementation to external contract.
- **No event versioning strategy**: publishing without a versioning plan means the first breaking schema change will silently break consumers.

## Mental Model

An event is a stone dropped in water. The stone (the state change) has already sunk. The ripples (event consumers) travel outward from the point of impact. The stone does not know which ripples form. It does not wait for them to reach the shore. Its job is simply to record its impact: "I happened, here is the proof." The consumers' job is to react. The decoupling is complete. The only coupling that exists is the shared understanding of what the event means — its schema.

## Mini Summary

- ✔ Events are immutable past-tense facts; commands are future-directed requests; the naming convention encodes this semantic
- ✔ Domain events are internal; integration events cross context boundaries and require stable schemas and durable infrastructure
- ✔ Event schemas must support additive-only evolution; breaking changes require new event type versions
- ✔ CloudEvents standardises the event envelope for cross-tool interoperability
- ✔ Event sourcing stores state as an ordered log of events; current state is derived by replaying the log

# Guided Practice Quest

Work through the guided steps above. For the schema evolution question, think about the lifecycle: the event is already published and consumed by systems you do not control. What is the contract you owe them?

# Solo Practice Quest

You are designing the event model for a hotel reservation system. Significant business facts include: rooms being searched, rooms being reserved, reservations being confirmed, payments being taken, check-ins, check-outs, and cancellations.

Design the integration event model:
1. List the integration events with past-tense names
2. Design the CloudEvents-compliant JSON schema for `ReservationConfirmed`
3. Identify which events are likely to require schema evolution and propose a versioning strategy
4. Explain which events are domain events (internal) vs integration events (external)
5. Describe two benefits event sourcing would provide for a reservation system

# Integration

**Connecting to Design — Events and the Open/Closed Principle**

The Open/Closed Principle (OCP) states that software entities should be open for extension but closed for modification. Events achieve OCP at the architectural level: the producing service is closed for modification (it publishes the same `OrderPlaced` event), while the system is open for extension (any new consumer can subscribe without touching the producer). This is architectural OCP: the system's behaviour can be extended by adding new event consumers without modifying any existing component. In contrast, a request-driven API requires the producer to add a new outbound call for each new downstream action — each extension modifies the producer. The event model inverts this: producers announce facts to the world, and the world arranges itself around those facts. How does adopting an event-driven model change the governance process for adding a new feature that requires notifying multiple downstream services?

**Connecting to History — The Observer Pattern and its Industrial-Scale Descendant**

The Observer pattern (GoF 1994) is the single-service ancestor of modern event-driven architecture. An Observable notifies registered Observers of state changes — the same producer/consumer relationship, within a single process. Decades of distributed systems engineering scaled this idea: from in-process Observer to in-service ApplicationEvents, to inter-service message queues, to global event streaming with Kafka. Each evolution preserved the core insight — announce facts, let observers react — while adding durability, ordering guarantees, and scalability. The CloudEvents specification is the latest step in this evolution: standardising the event envelope so that tools from different vendors can interoperate in the same way that HTTP standardised request/response. History shows that the abstraction was always right; the infrastructure around it became increasingly sophisticated.

# Lore Conclusion

The event is the atom of event-driven architecture. Everything else — the queues, the brokers, the consumers, the sagas — is infrastructure in service of this one idea: announce what happened, let the world decide what to do about it. The senior architect who designs events correctly does the hardest intellectual work upfront: naming them precisely, designing their schemas for longevity, and separating domain facts from integration contracts. Get the events right and the architecture flows naturally from them. Get them wrong — commands disguised as events, unstable schemas, missing correlation — and every downstream system inherits the confusion.
