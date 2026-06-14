---
id: se-sen-m7-06
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m7
moduleTitle: "Module 7: Event-Driven Architecture"
moduleGlyph: "📨"
moduleSortOrder: 7
topicSlug: event_orchestration
topicTitle: "Event Orchestration"
topicSortOrder: 6
lesson: event_orchestration
title: "Event Orchestration"
sortOrder: 6
difficulty: 4
estimatedMinutes: 30
xpReward: 60
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [event_choreography]
integrationDomains: [design, psychology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Explains orchestration vs choreography with the orchestrator's role clearly defined"
    - "Describes the central orchestrator as a state machine for the saga"
    - "Names at least two benefits of orchestration over choreography"
    - "Names at least one drawback (coupling to orchestrator, orchestrator becomes a bottleneck)"
    - "Knows at least one workflow engine tool (Temporal, Conductor, Spring State Machine)"
  keywords: [orchestrator, state, machine, central, coordinator, workflow, visibility, coupling, temporal, saga]
  modelAnswer: |
    Orchestration: a central OrderSagaOrchestrator service explicitly directs each step.

    OrderSagaOrchestrator state machine:
    CREATED → (call PaymentService) → PAYMENT_PENDING
    PAYMENT_PENDING → (PaymentService confirms) → PAYMENT_DONE
    PAYMENT_DONE → (call InventoryService) → INVENTORY_PENDING
    INVENTORY_PENDING → (InventoryService confirms) → RESERVED
    RESERVED → (call ShipmentService) → COMPLETE

    On failure at any step:
    Orchestrator explicitly calls compensating actions in reverse order.

    Benefits vs choreography:
    - Single place to see saga state (query orchestrator)
    - Explicit control flow — easy to reason about
    - Compensation logic centralised and testable

    Drawbacks:
    - Services coupled to orchestrator interface
    - Orchestrator becomes a potential bottleneck
    - More code to write and maintain
guidedSteps:
  - id: orch-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In an orchestration-based saga, who decides that the `PaymentService` should run next
      after `InventoryService` completes?
    inputConfig:
      options:
        - "InventoryService publishes an event and PaymentService reacts"
        - "A central orchestrator service that tracks saga state and explicitly calls each step"
        - "Kafka routes the message to PaymentService"
        - "The services negotiate via a shared database"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A central orchestrator service that tracks saga state and explicitly calls each step"]
      rejectedFeedback: "In orchestration, the central orchestrator explicitly directs each step: 'InventoryService done → now call PaymentService.' The orchestrator holds all saga state and knows the full workflow. This is the opposite of choreography where services react autonomously."
    hint: "In orchestration, someone is in charge. Who?"
    reflectionPrompt: "The orchestrator is like a project manager: it assigns tasks, waits for completion, handles failures, and knows the overall status at all times. This centralisation is both its strength (visibility) and weakness (coupling)."
  - id: orch-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      An orchestration saga coordinator can be modelled as a ___ machine, where the
      saga moves through defined states based on the outcomes of each step.
    inputConfig:
      placeholder: "two words"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["state machine", "finite state machine", "state"]
      rejectedFeedback: "A **state machine** explicitly models all saga states (CREATED, PAYMENT_PENDING, PAYMENT_DONE, RESERVED, SHIPPED, FAILED) and the transitions between them. Each step's success or failure triggers a state transition. This makes saga logic explicit, testable, and auditable."
    hint: "A machine that moves between defined states based on inputs. What's it called?"
    reflectionPrompt: "State machines make complex workflows explicit. You can visualise them, test every state transition, and debug by inspecting current state. Libraries: Spring State Machine, Temporal (workflow engine), or a simple `status` field with explicit transitions."
  - id: orch-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      When would you choose orchestration over choreography? Describe a specific scenario where orchestration's properties are more valuable.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [visibility, complex, state, compliance, audit, many, steps, critical, business, track]
      rejectedFeedback: "Choose orchestration when: (1) you need visibility into saga state at all times (compliance, customer support queries); (2) the workflow is complex with conditional branching and many steps; (3) compensation logic is complex and needs to be centralised; (4) business stakeholders need to see the current state of any in-flight workflow."
    hint: "Think about who needs to see the state of a long-running workflow and when."
    reflectionPrompt: "Orchestration is often the right choice for business-critical workflows (order fulfilment, loan approval, insurance claims) where stakeholders need visibility and accountability. Choreography is better for loosely-coupled, independently-owned service events."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the main advantage of orchestration over choreography for debugging failures?"
    options:
      - "Orchestration is faster so failures are less frequent"
      - "The orchestrator has a central record of saga state, making it easy to see exactly which step failed"
      - "Orchestration prevents failures entirely"
      - "Services log more information in orchestration"
    correctIndex: 1
    feedback: "The orchestrator tracks saga state explicitly. When an order is stuck, you query the orchestrator: 'Order 12345 is in state PAYMENT_FAILED — PaymentService returned card_declined.' With choreography, you'd reconstruct this from 6 distributed service logs."
  - type: MULTIPLE_CHOICE
    question: "What is a risk of using an orchestrator service for all sagas?"
    options:
      - "Events become non-replayable"
      - "The orchestrator becomes a bottleneck and a single point of failure for all workflows"
      - "Services can no longer be independently deployed"
      - "Kafka cannot be used"
    correctIndex: 1
    feedback: "If all workflows route through one orchestrator, it becomes a bottleneck at high volume and a single point of failure. Mitigations: make the orchestrator stateless (state in DB), scale horizontally, or use distributed workflow engines like Temporal which handle orchestrator resilience."

retrieval:
  recall: "What is the difference between saga orchestration and saga choreography? Describe how compensation works in orchestration."
  explain: "Explain to a colleague who prefers choreography why you might still choose orchestration for a loan approval workflow."
  mistakeId:
    code: |
      // OrderSagaOrchestrator
      public void processOrder(Long orderId) {
          paymentService.charge(orderId);       // direct synchronous call
          inventoryService.reserve(orderId);   // direct synchronous call
          shipmentService.create(orderId);     // direct synchronous call
      }
    answer: "This is not orchestration — it's synchronous coupling with no saga semantics. If `inventoryService.reserve` fails, payment has already been charged with no compensation logic. An orchestrator should: call services asynchronously, await confirmations, handle failures explicitly, trigger compensating actions, and persist saga state so it can resume after a crash."
---

# Hook

A customer calls support: "My order has been stuck for three days. What's happening?"

With choreography, the answer requires querying logs across five services. With orchestration, you query the `OrderSagaOrchestrator` and learn: "Order 98765 is in state PAYMENT_FAILED — the card was declined at 14:32 on Tuesday."

Orchestration trades autonomy for visibility. The question is: which trade-off does your system need?

> What's the most complex multi-step workflow in a system you've worked on? How would you answer "what is the current state of workflow X?"

# Lore Introduction

The Academy's most complex enchantments — the weather-control wards, the dimensional portals — have a Conductor artificer who coordinates each component explicitly. The Conductor knows the complete state of the enchantment at all times. If a component fails, the Conductor knows immediately and directs the reversal.

*"Choreography is elegant,"* Archmage Veylan says. *"Orchestration is accountable. For enchantments where failure has consequences — real, visible consequences — you want to know the state at all times. The Conductor provides that."*

# Core Learning

## Concept Introduction

**Orchestration** uses a central coordinator (orchestrator) that explicitly directs each step of a saga and tracks the complete workflow state.

```
OrderSagaOrchestrator
├── State: CREATED
├── → calls PaymentService  → State: PAYMENT_PENDING
├── ← PaymentService confirms → State: PAYMENT_DONE
├── → calls InventoryService → State: INVENTORY_PENDING
├── ← InventoryService confirms → State: RESERVED
├── → calls ShipmentService → State: COMPLETE
└── on any failure: triggers compensation in reverse order
```

The orchestrator is a **state machine**: each state transition is explicit, auditable, and recoverable.

## Why It Matters

Orchestration provides:
- **Visibility** — query the orchestrator to see the current state of any saga instance
- **Centralised error handling** — compensation logic in one place, not scattered across services
- **Explicit control flow** — the workflow is readable code, not emergent event chains
- **Recovery** — orchestrator state persisted; can resume after crash
- **Auditability** — full history of state transitions per saga instance

## Worked Examples

**Spring State Machine approach:**
```java
@Configuration
@EnableStateMachine
public class OrderSagaConfig extends StateMachineConfigurerAdapter<OrderState, OrderEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
        states.withStates()
            .initial(OrderState.CREATED)
            .state(OrderState.PAYMENT_PENDING)
            .state(OrderState.PAYMENT_DONE)
            .state(OrderState.RESERVED)
            .end(OrderState.COMPLETE)
            .end(OrderState.FAILED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception {
        transitions
            .withExternal().source(CREATED).target(PAYMENT_PENDING).event(START_PAYMENT)
            .and()
            .withExternal().source(PAYMENT_PENDING).target(PAYMENT_DONE).event(PAYMENT_SUCCESS)
            .and()
            .withExternal().source(PAYMENT_PENDING).target(FAILED).event(PAYMENT_FAILED);
    }
}
```

**Simple orchestrator (without a framework):**
```java
@Service
@Transactional
public class OrderSagaOrchestrator {

    public void processOrder(Order order) {
        order.setStatus(PAYMENT_PENDING);
        orderRepository.save(order);  // persisted before async call

        kafkaTemplate.send("payment.requests",
            new ChargeCardCommand(order.getId(), order.getTotal()));
    }

    @KafkaListener(topics = "payment.results")
    public void onPaymentResult(PaymentResultEvent event) {
        Order order = orderRepository.findById(event.getOrderId()).orElseThrow();
        if (event.isSuccess()) {
            order.setStatus(INVENTORY_PENDING);
            orderRepository.save(order);
            kafkaTemplate.send("inventory.requests", new ReserveStockCommand(order.getId()));
        } else {
            order.setStatus(FAILED);
            orderRepository.save(order);
            // no compensation needed — payment wasn't taken
        }
    }
}
```

## Common Mistakes

- **Synchronous direct calls inside the orchestrator** — creates tight coupling and no fault tolerance.
- **Orchestrator not persisting state** — if it crashes mid-saga, all in-progress workflows are lost.
- **One orchestrator for all workflows** — creates a god service; model each saga type separately.
- **Not persisting state before sending commands** — state must be saved before the async call, not after.
- **Forgetting idempotency in steps** — commands may be retried; steps must handle duplicate execution.

## Mental Model

Orchestration is a **flight operations centre**. Every flight (saga) is tracked on a board. Controllers know exactly where each flight is, what happened last, and what comes next. If a flight has a problem, the controller has the full picture and can direct a response. Choreography is a flight without a control centre — pilots are skilled and autonomous, but when something goes wrong, reconstruction is required.

## Mini Summary

- ✔ Orchestration: central coordinator explicitly manages each saga step and tracks full state
- ✔ Modelled as a state machine: states, transitions, and actions on each transition
- ✔ Benefits: visibility, centralised compensation, explicit control flow, recoverability
- ✔ Drawbacks: services coupled to orchestrator interface, potential bottleneck
- ✔ Use orchestration for complex, business-critical workflows requiring full visibility

# Guided Practice Quest

**The Conductor's Podium**

Design an orchestrator state machine for a 4-step booking saga. Define states, transitions, and compensation paths.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

A mortgage application system has a complex approval workflow:
1. Credit check (CreditBureauService)
2. Property valuation (ValuationService) — can run in parallel with credit check
3. Underwriter review (UnderwritingService) — requires both 1 and 2 to complete
4. Legal document generation (LegalService)
5. Funds disbursement (FundsService)

Design the orchestrator:
1. Define all saga states (including failed states for each step)
2. Design the state transition diagram
3. Handle the parallel execution of steps 1 and 2 — what state does the saga enter while waiting for both?
4. Define compensating actions for: credit check fails, valuation fails, underwriting rejects
5. Why is orchestration more appropriate than choreography for a mortgage approval workflow?

# Integration

**Connecting to Psychology — Situational Awareness**

Psychologist Mica Endsley's theory of situational awareness (1988) describes three levels: perception (seeing what's happening), comprehension (understanding what it means), and projection (predicting what will happen next). Elite performance in complex domains (air traffic control, surgery, military command) requires all three.

Orchestration gives software operators situational awareness that choreography cannot. An orchestrator makes the current state of every workflow perceivable (perception), relates it to the overall saga goal (comprehension), and enables prediction of what comes next (projection). A distributed choreography system provides only raw events — perception is difficult, comprehension requires reconstruction, projection is nearly impossible.

This matters most in high-stakes domains: financial services, healthcare, logistics. When a payment saga is stuck and a customer is on the phone, situational awareness translates directly to time-to-resolution. Systems that can immediately answer "what state is workflow X in and why?" are more operable than those that cannot.

The design principle: build for operability, not just functionality. A system that works in production but cannot be understood when it fails is incomplete.

How would you design a workflow system that maximises situational awareness for the operations team?

# Lore Conclusion

The Conductor's board shows every enchantment in progress. One is in state WARD_PENDING; its crystal confirmed activation thirty minutes ago. The Conductor alerts the next component artificer.

*"Choreography is what the system does,"* Archmage Veylan says. *"Orchestration is what you can see the system doing. For high-stakes work, the ability to see and intervene is worth the coupling cost."*

Choose your coordination pattern deliberately. Neither is universally better. Both have a place.
---
