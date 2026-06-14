---
id: de-sen-m1-03
school: engineering
domainId: data-engineering
tier: SENIOR
moduleId: de-sen-m1
moduleTitle: "Module 1: Database Architecture"
moduleGlyph: "🏗️"
moduleSortOrder: 1
topicSlug: service_oriented_data
topicTitle: "Service-Oriented Data"
topicSortOrder: 3
lesson: service_oriented_data
title: "Service-Oriented Data"
sortOrder: 3
difficulty: 4
estimatedMinutes: 35
xpReward: 75
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-sen-m1-02]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains the database-per-service pattern and its justifications
    - Describes the data consistency challenges (no cross-service transactions)
    - Explains the Saga pattern for distributed transactions
    - Describes event-driven data sharing between services
    - Identifies the genuine trade-offs of service-oriented data vs shared database
  keywords: [database-per-service, service autonomy, distributed transaction, saga pattern, choreography, orchestration, eventual consistency, event sourcing, outbox pattern, two-phase commit, data ownership, bounded context]
  modelAnswer: |
    Database-per-service: each service owns its own database, no direct cross-service database access. Justifications: independent scaling, independent deployment, technology choice per service, compliance isolation. Challenges: no ACID transactions across services — a checkout operation that spans loans+inventory+notifications requires either 2PC (distributed transaction, complex and fragile) or the Saga pattern. Saga: a sequence of local transactions coordinated by events or an orchestrator — if any step fails, compensating transactions run in reverse. Choreography saga: services react to events independently. Orchestration saga: a central coordinator drives the saga steps. Outbox pattern: write business data + event to the same database transaction, then reliably publish the event — prevents the "wrote to DB but failed to publish event" split-brain. The genuine trade-offs: service autonomy vs data consistency complexity — choose only when autonomy benefits exceed the consistency overhead.
guidedSteps:
  - id: de-sen-m1-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A checkout operation must: (1) Create a loan record in loans-db, (2) Decrement stock in inventory-db, (3) Charge the member in payments-db. The loans step succeeds, then inventory fails. What is the problem with using a database-per-service architecture here?
    inputConfig:
      options:
        - "The problem does not exist — each service can retry independently"
        - "There is no single ACID transaction spanning all three databases — the loan is created and committed but inventory was never decremented, leaving data inconsistent"
        - "Database-per-service prevents this type of multi-step operation entirely"
        - "The inventory failure automatically rolls back the loan creation"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["There is no single ACID transaction spanning all three databases — the loan is created and committed but inventory was never decremented, leaving data inconsistent"]
      rejectedFeedback: "Without a distributed transaction (2PC) or Saga pattern, partial failure leaves the system in an inconsistent state: a loan exists in loans-db for a book that still shows as available in inventory-db. This is the core challenge of service-oriented data. The loan commit cannot be rolled back automatically when inventory fails — they are separate databases. Solutions: (1) Two-Phase Commit (2PC): all three databases agree to commit or all roll back. Complex, slow, fragile — all three databases must be available. (2) Saga pattern: instead of one ACID transaction, a sequence of local transactions with compensating transactions for failure. Checkout saga: create loan → if inventory fails → compensating transaction cancels the loan. Eventual consistency — there may be a window where the loan exists but the cancellation hasn't run yet."
    hint: "The loan is committed to loans-db before inventory is attempted. When inventory fails, can the committed loan be automatically rolled back?"
    reflectionPrompt: "What is the difference in complexity between implementing the checkout flow in a monolith vs implementing a Saga across three services?"
  - id: de-sen-m1-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The pattern that ensures an event is reliably published to a message broker after a database write — by first writing the event to an outbox table in the same transaction, then publishing it asynchronously — is called the ________ pattern.
    inputConfig:
      placeholder: "outbox"
    markingRule:
      matchMode: CONTAINS
      accepted: [outbox, "outbox pattern", "transactional outbox", "transactional outbox pattern"]
      rejectedFeedback: "The Transactional Outbox Pattern: (1) Service writes business data (create loan) AND an event record (LoanCreated) to the same database in the same ACID transaction. If the transaction commits, both the loan and the event record are written atomically. (2) A background process (or CDC — Change Data Capture) reads the outbox table and publishes events to the message broker (Kafka, RabbitMQ). (3) Events are only published after successful database commit. Without the outbox pattern: if the service writes to the DB and then tries to publish the event directly, but crashes between the two operations, the event is lost — the DB has the loan but no one is notified. The outbox pattern prevents this split-brain by making event publishing an atomic side effect of the database commit."
    hint: "This pattern uses a database table as a reliable staging area for events before they are published to a message broker."
    reflectionPrompt: "What happens if the background outbox reader crashes before publishing an event? How does the pattern handle this?"
  - id: de-sen-m1-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Compare choreography and orchestration Saga patterns. Which should be used for a complex checkout flow with five steps, and why?
    inputConfig:
      minWords: 35
    markingRule:
      matchMode: CONTAINS
      accepted: [choreography, orchestration, coordinator, event, react, central, complex, visibility, debug, compensate, step, manage]
      rejectedFeedback: "Choreography saga: each service reacts to events independently — LoanCreated → InventoryService decrements stock → InventoryDecremented → PaymentService charges member. No central coordinator. Simple for 2-3 steps; becomes hard to understand for 5+ steps (difficult to see the full saga flow or debug failures). Orchestration saga: a central orchestrator (Saga Orchestrator) knows all steps and drives them explicitly — step 1: call loans-service, step 2: call inventory-service, step 3: call payments-service. On failure at step N, orchestrator drives compensating calls in reverse order (cancel payment, restore inventory, cancel loan). For a 5-step checkout: orchestration is better — the flow is explicit and visible in one place, compensating transactions are clearly defined, debugging is easier (one component owns the saga state). Use choreography for simple, well-bounded flows; orchestration for complex multi-step business processes where visibility and explicit failure handling are critical."
    hint: "Think about where the knowledge of 'what to do next' lives in each pattern, and which is easier to debug for a complex flow."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The bounded context concept in Domain-Driven Design relates to database-per-service because:"
    options:
      - "Each database should be bounded to exactly one table"
      - "A bounded context defines a domain boundary where a model is valid — a service database stores only the data for its bounded context, not other contexts' data"
      - "Bounded contexts require using NoSQL databases instead of relational ones"
      - "Each bounded context must have exactly the same database schema"
    correctIndex: 1
    feedback: "Bounded context (Eric Evans, Domain-Driven Design): a logical boundary within which a domain model has consistent meaning and terms. 'Order' means different things in a shipping context vs a billing context — they may have different attributes, validations, and lifecycle. A database-per-service aligned with bounded contexts: the loans-service database models Loan as it means in the loan management context — with loan-specific attributes and rules. The payments-service models the same conceptual entity differently. This alignment prevents the 'one model for everything' problem of shared schemas where the Loan entity must satisfy the needs of loans, billing, analytics, and notifications simultaneously — creating a bloated, compromised model. Database-per-service bounded context allows each service to have the right model for its context."
  - type: MULTIPLE_CHOICE
    question: "Event-driven data sharing between services (consuming events instead of querying databases) provides eventual consistency, meaning:"
    options:
      - "All services will agree on data at some point in the future, but not necessarily right now"
      - "Events are eventually lost and data becomes inconsistent"
      - "Services share data in real-time without any delay"
      - "Only the most recent event is consistent — older events may be wrong"
    correctIndex: 0
    feedback: "Eventual consistency: given that no new updates occur, all nodes will eventually reach the same state. In event-driven architecture: when loans-service publishes LoanCreated, inventory-service processes the event and decrements stock. There is a window (milliseconds to seconds) where loans-service has created the loan but inventory-service hasn't processed the event yet — the two services are temporarily inconsistent. Eventually (once the event is processed), both agree. Applications must be designed to tolerate this window: read-your-own-writes consistency (the service that wrote can read its own data immediately), monotonic reads (newer events don't appear before older ones), and idempotent event processing (processing the same event twice produces the same result — required for at-least-once delivery)."
retrieval:
  recall: "Describe the Orchestration Saga pattern for a 3-step checkout operation (create loan, decrement inventory, send notification). Include the normal flow and the compensating transactions for failure at each step."
  explain: "Explain the Outbox Pattern: what problem it solves, how it works technically, and what happens during the happy path vs the failure path (application crash between DB write and event publish)."
  mistakeId:
    code: |
      @Service
      public class CheckoutService {
          @Transactional
          public Loan checkOut(Long memberId, Long itemId) {
              Loan loan = loanRepository.save(new Loan(memberId, itemId));
              inventoryClient.decrementStock(itemId);   // HTTP call to inventory service
              notificationClient.sendConfirmation(memberId, loan.getId()); // HTTP call
              return loan;
          }
      }
    answer: "Three problems with this distributed transaction approach: (1) @Transactional only covers loanRepository.save() — the local database. If inventoryClient.decrementStock() succeeds and then notificationClient fails, the loan exists, inventory is decremented, but no notification was sent. The @Transactional rollback will rollback the loan, but the inventory decrement in the remote service cannot be rolled back — it's already committed. (2) inventoryClient HTTP call is made inside the transaction — this holds the database connection and transaction open for the duration of the HTTP call (potentially hundreds of milliseconds), increasing connection pool pressure and deadlock risk. (3) No compensation logic: when inventoryClient fails, there is no code to undo the already-committed loan. Fix: use the Saga pattern with compensation. Or: use the Outbox pattern — commit the loan, write LoanCreated event to outbox table, return. The event-driven architecture handles inventory and notification asynchronously with retry and compensation."
---

# Hook

Database-per-service gives each team complete autonomy — they can deploy, scale, and evolve their storage independently. The cost is that ACID transactions across service boundaries no longer exist, and maintaining consistency requires explicit design: Sagas, outbox patterns, and eventual consistency. This is not a bug of the architecture — it is the fundamental trade-off, and it must be understood before choosing it.

# Lore Introduction

"The loans service and inventory service are now separate databases," the Senior Engineer said. "The checkout operation worked perfectly in the monolith — one ACID transaction. How does it work now?" The Lead Data Engineer pulled up the code. "Direct HTTP calls inside a @Transactional annotation. The developer assumed the transaction would cover everything." The Senior Engineer ran the failure test. "Loan created, inventory decremented, notification service down. The transaction rolls back the loan — but the inventory decrement already committed in a separate database. The book shows as borrowed but no loan record exists." The Lead Data Engineer set down the code review. "We have consistency problems in production. This is why the architecture decision must come with the distributed transaction solution. Database-per-service is not a feature to add — it is a design that requires deliberate patterns for everything that spans service boundaries."

# Core Learning

## Concept Introduction

### Database-Per-Service Architecture

```
Service-Oriented Data:

  Loans Service         Inventory Service      Payments Service
  ┌────────────┐        ┌─────────────────┐    ┌────────────────┐
  │ Loan logic │        │ Inventory logic  │    │ Payment logic  │
  └─────┬──────┘        └────────┬────────┘    └───────┬────────┘
        │                         │                      │
  ┌─────▼──────┐        ┌────────▼────────┐    ┌───────▼────────┐
  │  loans-db  │        │  inventory-db   │    │  payments-db   │
  └────────────┘        └─────────────────┘    └────────────────┘

Each service:
  ✓ Owns its database completely
  ✓ Can choose its own database technology
  ✓ Can scale independently
  ✓ Can evolve schema without coordinating with other teams
  ✗ Cannot share ACID transactions with other services
  ✗ Cross-service queries require API calls or data duplication
  ✗ Distributed consistency requires Saga or 2PC
```

### Saga Pattern (Orchestration)

```java
// Orchestration Saga: central coordinator drives all steps
@Component
public class CheckoutSagaOrchestrator {

    public CheckoutSagaResult executeCheckout(Long memberId, Long itemId) {
        String sagaId = UUID.randomUUID().toString();

        try {
            // Step 1: Create loan
            Loan loan = loansClient.createLoan(memberId, itemId, sagaId);

            try {
                // Step 2: Decrement inventory
                inventoryClient.decrementStock(itemId, sagaId);

                try {
                    // Step 3: Charge member
                    paymentsClient.chargeMember(memberId, loan.getId(), sagaId);

                    return CheckoutSagaResult.success(loan);

                } catch (PaymentException e) {
                    // Step 3 failed — compensate steps 2 and 1
                    inventoryClient.restoreStock(itemId, sagaId);    // compensate step 2
                    loansClient.cancelLoan(loan.getId(), sagaId);    // compensate step 1
                    return CheckoutSagaResult.failure("Payment failed");
                }

            } catch (InventoryException e) {
                // Step 2 failed — compensate step 1
                loansClient.cancelLoan(loan.getId(), sagaId);        // compensate step 1
                return CheckoutSagaResult.failure("Item not available");
            }

        } catch (LoanException e) {
            return CheckoutSagaResult.failure("Cannot create loan");
        }
    }
}
```

### Transactional Outbox Pattern

```java
// Service writes business data + outbox event in ONE transaction
@Transactional
public Loan createLoan(Long memberId, Long itemId, String sagaId) {
    // 1. Write business data
    Loan loan = loanRepository.save(
        new Loan(memberId, itemId, LocalDate.now(), LocalDate.now().plusDays(14)));

    // 2. Write outbox event IN THE SAME TRANSACTION
    OutboxEvent event = new OutboxEvent(
        "LoanCreated",
        objectMapper.writeValueAsString(new LoanCreatedPayload(
            loan.getId(), memberId, itemId, sagaId)),
        "NOT_PUBLISHED",
        Instant.now()
    );
    outboxRepository.save(event);

    // Both committed atomically — no split-brain possible
    return loan;
}

// Background process reads outbox and publishes to Kafka:
@Scheduled(fixedDelay = 100)
public void publishOutboxEvents() {
    List<OutboxEvent> pending = outboxRepository
        .findByStatusOrderByCreatedAt("NOT_PUBLISHED");
    for (OutboxEvent event : pending) {
        kafkaTemplate.send("loan-events", event.getPayload());
        // Update status after successful publish
        event.setStatus("PUBLISHED");
        outboxRepository.save(event);
    }
}
// Idempotent consumers handle at-least-once delivery
```

### Event-Driven Data Sharing

```java
// Inventory service consumes LoanCreated event
@KafkaListener(topics = "loan-events")
public void handleLoanCreated(LoanCreatedPayload payload) {
    if (payload.getEventType().equals("LoanCreated")) {
        // Idempotent: check if already processed
        if (processedEventRepository.exists(payload.getEventId())) return;

        inventoryService.decrementStock(payload.getItemId());
        processedEventRepository.save(payload.getEventId()); // mark processed
    }
}

// Eventual consistency window:
// t=0: Loan created in loans-db
// t=50ms: LoanCreated event published to Kafka
// t=100ms: Inventory service processes event, stock decremented
// Window [t=0, t=100ms]: loans says item is borrowed; inventory still shows available
// Applications must tolerate this window
```

## Common Mistakes

- **HTTP calls inside @Transactional**: makes the transaction span the duration of the HTTP call, holding database connections and increasing deadlock risk. Write to the database, close the transaction, then make external calls (or use the outbox pattern).
- **Sagas without idempotent steps**: if a saga step is retried (due to network failure), it may execute twice. Each step must be idempotent — executing twice produces the same result. Use saga IDs and check-before-act.
- **Not planning compensating transactions before building**: compensating transactions are complex and must be designed alongside the forward path. "We'll add compensation later" results in irrecoverable inconsistency incidents.
- **Premature adoption of database-per-service**: this architecture is justified by specific team autonomy, compliance, or scale requirements. Without those drivers, it adds immense complexity for no benefit.

## Mental Model

Service-oriented data is like international banking. A bank transfer between accounts in the same bank is a simple ledger entry — ACID. An international wire transfer between banks in different countries is a Saga: step 1 debit your account, step 2 SWIFT message to the receiving bank, step 3 credit the recipient. If step 3 fails, the money doesn't arrive but your account is debited — the compensation is a reversal credit. There is a window where the money is "in flight" — your account shows less money but the recipient hasn't received it. This is eventual consistency. Banks accept this complexity because the benefits (independent bank operation, different currencies, regulatory separation) justify it. Service-oriented data requires the same justification.

## Mini Summary

- ✔ Database-per-service: each service owns its own database — full autonomy, no shared schema
- ✔ No cross-service ACID transactions — consistency requires Saga or 2PC
- ✔ Saga pattern: sequence of local transactions + compensating transactions for failure
- ✔ Orchestration saga: central coordinator — better for complex, multi-step flows
- ✔ Choreography saga: services react to events — better for simple, decoupled flows
- ✔ Outbox pattern: write data + event in one transaction, publish event asynchronously
- ✔ Eventual consistency: services converge on consistent state after event processing

# Guided Practice Quest

Work through the guided steps to implement a 3-step checkout saga with compensation, design the outbox table schema and the background publisher, and trace through both the success path and the failure path (inventory fails) to verify correct compensation.

# Solo Practice Quest

Design the distributed data consistency layer for the Archive's service-oriented architecture. Tasks: (1) Design an Orchestration Saga for checkout: define all 4 steps (create loan, decrement stock, schedule notification, update member history), all 4 compensating transactions, and the saga state machine; (2) Implement the Outbox pattern for the loans service: the OutboxEvent entity, the @Transactional service that writes both loan and event, and the background publisher; (3) Design the idempotency mechanism: how each downstream service determines if it has already processed an event; (4) Trace through a failure scenario: inventory service is down for 5 minutes, 100 checkout attempts are made — what is the system state during the outage, after inventory recovers, and after the saga completes? (5) Identify three business operations that can remain eventually consistent and two that require synchronous consistency — justify each.

# Integration

**Mathematics**: Distributed consistency can be modelled with the CAP theorem (Brewer, 2000): in the presence of a network Partition, a distributed system can guarantee at most one of Consistency (all nodes see the same data) or Availability (every request gets a response). Service-oriented data chooses AP (Available + Partition-tolerant) — the system continues operating during network partitions, accepting temporary inconsistency. The BASE properties (Basically Available, Soft state, Eventually consistent) are the operational consequence of this choice. Saga compensating transactions implement a form of eventual consistency: the system is guaranteed to reach a consistent state in finite time, assuming all services eventually process their events. The mathematical correctness criterion for a Saga: if all forward and compensating transactions are idempotent and serialisable within each service, the saga's final state is equivalent to either the complete success or complete failure of the corresponding ACID transaction.

**Sciences (Systems Engineering — Fault Tolerance)**: The Saga pattern is an application of graceful degradation principles from systems engineering. NASA's fault-tolerant system design principle (Redundancy + Diagnostics + Recovery) maps directly: Redundancy (multiple retry attempts for each saga step), Diagnostics (saga state machine records which steps succeeded or failed), Recovery (compensating transactions restore the pre-saga state). The key systems engineering insight is that partial failures are normal, not exceptional — the system must be designed to reach a consistent state from any partial failure state. This is the same principle used in aircraft hydraulic systems: if one hydraulic circuit fails, the backup circuit automatically assumes control; if both fail, mechanical backup engages. Each failure mode has a designed recovery path. The Saga orchestrator is the system's flight management computer: it tracks the saga state and drives recovery when any step fails.

# Lore Conclusion

"Checkout saga deployed," the Senior Engineer reported. "Three steps with orchestration. Compensation logic for each failure mode. Outbox pattern for event publishing — no more split-brain between loan creation and event emission." The Lead Data Engineer reviewed the saga state table. "The inventory outage yesterday?" The Senior Engineer pulled up the saga log. "Forty-three checkouts attempted. Saga paused at step 2 for each. When inventory recovered, saga resumed — stock decremented, forty-three successful completions." The Lead Data Engineer looked at the monitoring dashboard. "Consistent state across all services." The Senior Engineer closed the saga state log. "The cost is visible: the outbox publisher, the saga orchestrator, the idempotency checks, the compensation logic. Five times more code than the monolith checkout. But the services deploy independently, scale independently, and fault isolation is clean." The Lead nodded. "Justified cost for this scale. Next: polyglot persistence — when different components need fundamentally different database technologies."

---
