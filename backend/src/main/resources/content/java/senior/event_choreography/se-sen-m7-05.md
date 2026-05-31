---
id: se-sen-m7-05
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m7
moduleTitle: "Module 7: Event-Driven Architecture"
moduleGlyph: "📨"
moduleSortOrder: 7
topicSlug: event_choreography
topicTitle: "Event Choreography"
topicSortOrder: 5
lesson: event_choreography
title: "Event Choreography"
sortOrder: 5
difficulty: 4
estimatedMinutes: 30
xpReward: 60
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [kafka_fundamentals]
integrationDomains: [design, philosophy]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Explains choreography vs orchestration clearly"
    - "Describes the Saga pattern with choreography for distributed transactions"
    - "Names at least two benefits of choreography (autonomy, loose coupling)"
    - "Names at least two drawbacks (debugging, cyclic events, no single view of state)"
    - "Explains compensating transactions and when they are needed"
  keywords: [choreography, saga, compensate, autonomous, decouple, cyclic, trace, state, event, react]
  modelAnswer: |
    Choreography: each service reacts to events and publishes new events.
    No central coordinator — services self-organise through shared events.

    Order Saga (choreography):
    1. OrderService publishes OrderCreated
    2. PaymentService receives OrderCreated → charges card → publishes PaymentProcessed
    3. InventoryService receives PaymentProcessed → reserves stock → publishes StockReserved
    4. ShipmentService receives StockReserved → creates shipment → publishes ShipmentCreated

    Compensation: if step 3 fails (stock unavailable):
    - InventoryService publishes StockUnavailable
    - PaymentService receives StockUnavailable → refunds card → publishes PaymentRefunded
    - OrderService receives PaymentRefunded → marks order as failed

    Benefits: services are autonomous, independently deployable, no single point of failure.
    Drawbacks: no single place to see saga state; debugging cross-service chains is hard;
    cyclic event patterns must be explicitly prevented.
guidedSteps:
  - id: choreo-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In a choreography-based saga, OrderService places an order and publishes `OrderCreated`.
      PaymentService reacts and publishes `PaymentProcessed`. Who decides what happens next?
    inputConfig:
      options:
        - "A central orchestrator service coordinates the next step"
        - "PaymentService tells OrderService what to do next"
        - "InventoryService independently listens for `PaymentProcessed` and reacts"
        - "Kafka decides the routing"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["InventoryService independently listens for `PaymentProcessed` and reacts"]
      rejectedFeedback: "In choreography, each service knows only its own role and the events it listens for. InventoryService independently subscribes to `PaymentProcessed` and decides what to do (reserve stock). No service tells another what to do — they react autonomously."
    hint: "In choreography, there is no conductor. Each dancer knows their steps."
    reflectionPrompt: "Choreography achieves loose coupling by removing direct knowledge between services. InventoryService doesn't know OrderService exists — it only knows about the `PaymentProcessed` event. Adding a new service that also reacts to this event requires zero changes to existing services."
  - id: choreo-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      When a step in a choreography saga fails and previously completed steps must be undone,
      the mechanism used is called a ___ transaction.
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["compensating", "compensation"]
      rejectedFeedback: "A **compensating transaction** reverses the effect of a previously completed step. Example: if `InventoryService` cannot reserve stock after payment was already taken, a compensating transaction refunds the payment. Compensation is not rollback (which is database-level); it's a new business action that undoes the effect."
    hint: "It's a business action that reverses a previous step. Not a database rollback — a new event."
    reflectionPrompt: "Distributed systems cannot do ACID transactions across services. Sagas compensate instead: if step N fails, trigger compensating actions for steps N-1, N-2, ... back to the start. Each compensation is itself a business event that other services may react to."
  - id: choreo-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A developer says "choreography is better than orchestration because it has no single point of failure." Name one significant drawback of choreography that this developer is ignoring.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [debug, trace, state, observe, visibility, hard, distributed, log, see, track, where]
      rejectedFeedback: "The major drawback: no single view of saga state. In a 6-step order saga, if something goes wrong, you must query 6 different service logs to understand what happened and where the saga is. Orchestration gives you a central place to see: 'Step 3 failed for order 12345.' Choreography gives you: 6 disconnected event streams you must correlate manually."
    hint: "If the entire saga state is distributed across 6 services, what becomes difficult when something goes wrong?"
    reflectionPrompt: "Debugging choreography requires distributed tracing with correlation IDs across all services. Tools like Jaeger or Zipkin can visualise the trace — but only if every service propagates the correlation ID correctly. This is non-trivial operational discipline."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the key difference between choreography and orchestration in event-driven sagas?"
    options:
      - "Choreography is faster; orchestration is more reliable"
      - "Choreography uses events for coordination with no central controller; orchestration has a central service directing each step"
      - "Choreography is only for simple sagas; orchestration handles complex ones"
      - "Choreography uses Kafka; orchestration uses RabbitMQ"
    correctIndex: 1
    feedback: "Choreography = decentralised, services react to events autonomously. Orchestration = centralised, one service explicitly directs each step and knows the full workflow. Each has trade-offs in coupling, visibility, and operational complexity."
  - type: MULTIPLE_CHOICE
    question: "Which of these is a risk specific to choreography that orchestration avoids?"
    options:
      - "Network partitions"
      - "Service restarts"
      - "Accidental event cycles where Service A's event triggers Service B, which triggers Service A again"
      - "Database transactions failing"
    correctIndex: 2
    feedback: "Event cycles are a choreography-specific risk. If OrderService listens for PaymentRefunded and publishes OrderCancelled, and PaymentService listens for OrderCancelled... you can create an infinite loop. Orchestration's central controller explicitly manages the flow, making cycles impossible."

retrieval:
  recall: "Describe how a choreography-based order saga works from OrderCreated to ShipmentCreated, including what happens if payment fails."
  explain: "Explain to your team the trade-off between choreography and orchestration when deciding how to implement a 5-step business workflow."
  mistakeId:
    code: |
      // InventoryService
      @KafkaListener(topics = "orders.payment-processed")
      void onPaymentProcessed(PaymentProcessedEvent event) {
          inventory.reserve(event.getOrderId());
          // If reservation fails, do nothing — the order will just be stuck
      }
    answer: "The saga has no compensation logic. If `inventory.reserve` fails, no event is published, and the order permanently enters an unknown state with money already charged. The service must publish a `StockUnavailable` event on failure, triggering the compensating transaction (refund payment, cancel order)."
---

# Hook

An order is placed. Payment is charged. Stock is reserved. Shipment is scheduled. Each step happens in a different service. There is no central coordinator — each service simply reacts to the previous one's event.

This is choreography: emergent coordination through events. Like a jazz ensemble with no conductor — each musician knows the piece and listens to the others.

The beauty: perfect decoupling. The challenge: when something goes wrong, the failure is distributed across five service logs and there's no single place that knows the current state of the order.

> Have you worked on a distributed workflow where it was unclear which service "owned" the state?

# Lore Introduction

The Academy's enchantment assembly process has no foreman. The binding rune is inscribed; the storage crystal reacts automatically; the security ward activates in response to the crystal; the authentication charm reacts to the ward. Each component knows only its role and the signal it waits for.

*"When it works,"* Archmage Veylan says, *"it is beautiful. Every component is autonomous, self-sufficient, replaceable. When it fails — and it does fail — you will read four different incident logs looking for the break in the chain."*

# Core Learning

## Concept Introduction

**Choreography** is a coordination style where services react independently to events — there is no central controller.

**Choreography Saga for order processing:**
```
OrderService      → publishes: OrderCreated
PaymentService    → listens: OrderCreated    → publishes: PaymentProcessed
InventoryService  → listens: PaymentProcessed → publishes: StockReserved
ShipmentService   → listens: StockReserved   → publishes: ShipmentCreated
```

Each service is autonomous — it knows only its own events, not the full workflow.

**Compensating transactions (on failure):**
```
InventoryService  → publishes: StockUnavailable
PaymentService    → listens: StockUnavailable → refunds → publishes: PaymentRefunded
OrderService      → listens: PaymentRefunded  → marks order failed
```

## Why It Matters

Choreography provides:
- **Loose coupling** — services don't know each other, only the events
- **Autonomy** — each service can be deployed, scaled, and changed independently
- **No single point of failure** — no orchestrator to crash
- **Easy extension** — new service? Just subscribe to the relevant event

## Worked Examples

**Event-driven saga (Spring Kafka):**
```java
// PaymentService
@KafkaListener(topics = "orders.created")
public void onOrderCreated(OrderCreatedEvent event) {
    try {
        paymentGateway.charge(event.getOrderId(), event.getAmount());
        kafkaTemplate.send("orders.payment-processed",
            new PaymentProcessedEvent(event.getOrderId(), event.getCorrelationId()));
    } catch (PaymentException e) {
        kafkaTemplate.send("orders.payment-failed",
            new PaymentFailedEvent(event.getOrderId(), e.getReason()));
    }
}
```

**Preventing event cycles:**
```java
// Always check: am I already processing a derived event?
@KafkaListener(topics = "orders.payment-refunded")
public void onPaymentRefunded(PaymentRefundedEvent event) {
    // Guard: only cancel if not already cancelled
    if (orderRepository.findById(event.getOrderId())
            .filter(o -> o.getStatus() != CANCELLED).isPresent()) {
        cancelOrder(event.getOrderId());
        // Note: do NOT publish OrderCancelled if PaymentService listens for it
        //       — this would create a cycle
    }
}
```

## Common Mistakes

- **No compensation logic** — every step that can fail needs a compensating event path.
- **Cyclic event chains** — service A events trigger service B which re-triggers service A.
- **Missing correlation IDs** — impossible to reconstruct a saga's history without them.
- **Assuming event order** — events may arrive out of order; services must be idempotent.
- **No monitoring of saga completion** — "stuck" sagas (partially complete) can silently leave data inconsistent.

## Mental Model

Choreography is a **relay race without a coach**. Each runner knows their leg, waits for the baton, runs their section, passes to the next. No one tells them when to start or stop — they react to receiving the baton. Beautiful in execution; the failure mode is: the baton drops and the coach has no view of where it is.

## Mini Summary

- ✔ Choreography: services react to events autonomously — no central controller
- ✔ Saga pattern: multi-step distributed workflow using events for coordination
- ✔ Compensating transactions: business actions that undo previous completed steps on failure
- ✔ Benefits: loose coupling, service autonomy, no single point of failure
- ✔ Drawbacks: hard to observe saga state, cyclic event risk, complex debugging

# Guided Practice Quest

**The Autonomous Assembly**

Model a three-step order saga using choreography. Design the event chain, the failure path, and the compensating transactions.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

A hotel booking system has a 4-step saga:
1. Reserve room (`RoomReservationService`)
2. Charge payment (`PaymentService`)
3. Notify hotel (`HotelNotificationService`)
4. Send confirmation email (`EmailService`)

Design the choreography-based saga:
1. Draw (or describe) the full happy-path event chain
2. Design compensation flows for each of the three failure scenarios:
   a. Payment fails after room is reserved
   b. Hotel notification fails after payment is charged
   c. Email fails after hotel is notified (is compensation needed? why/why not?)
3. What events would you use correlation IDs on and why?
4. How would you monitor whether a saga has completed or is stuck?

# Integration

**Connecting to Philosophy — Emergence and Self-Organisation**

Philosopher Philip Anderson coined "More is Different" (1972): at sufficient complexity, new properties emerge that cannot be predicted from the individual components. Ant colonies exhibit sophisticated collective behaviour with no central planning — each ant follows simple local rules and the colony "decides" collectively.

Choreography embodies this emergent design philosophy. No component knows the full workflow; complex multi-service transactions emerge from simple local rules (receive this event → do this → publish that event). The system's intelligence is distributed, not centralised.

This has both strengths and limitations. Emergent systems are robust — removing one component degrades performance but rarely causes catastrophic failure (no single point of failure). But emergent behaviour is also hard to predict at design time. Complex event chains produce outcomes that weren't explicitly designed — including failure modes no one anticipated.

The philosophical implication: designed complexity (orchestration) is more legible and controllable; emergent complexity (choreography) is more resilient and adaptive. Most real systems benefit from understanding both and choosing deliberately.

When is emergent coordination better than designed coordination? What properties of the problem drive the choice?

# Lore Conclusion

The enchantment chain completes. Four components, four reactions, no foreman. The ward is active.

*"You notice,"* Archmage Veylan says, *"that it worked. You also notice that when the security ward failed last Tuesday, it took us four hours to find which component first received the wrong signal. Autonomy has its price."*

Choose choreography where decoupling matters most. Choose it with eyes open to the debugging cost.
---
