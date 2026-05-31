---
id: se-sen-m3-07
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m3
moduleTitle: "Module 3: Asynchronous Programming"
moduleGlyph: "⚡"
moduleSortOrder: 3
topicSlug: reactive_programming
topicTitle: "Reactive Programming"
topicSortOrder: 7
lesson: reactive_programming_foundations
title: "Reactive Programming Foundations"
sortOrder: 7
difficulty: 4
estimatedMinutes: 32
xpReward: 65
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [callbacks]
integrationDomains: [mathematics, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "States the four Reactive Manifesto traits: responsive, resilient, elastic, message-driven"
    - "Distinguishes Mono (0-1 item) from Flux (0-N items)"
    - "Explains backpressure and why it matters"
    - "Identifies appropriate use cases for reactive programming"
    - "Notes the added complexity cost of reactive code vs imperative code"
  keywords: [Reactive Manifesto, responsive, resilient, elastic, message-driven, Flux, Mono, backpressure, WebFlux, Project Reactor, streaming, non-blocking]
  modelAnswer: |
    // Reactive Manifesto: responsive (low latency), resilient (handles failures),
    // elastic (scales up/down), message-driven (async, non-blocking communication)

    // Mono: 0 or 1 result
    Mono<User> userMono = userRepository.findById(id); // non-blocking

    // Flux: 0 to N results (streaming)
    Flux<Order> orderFlux = orderRepository.findByUserId(id);

    // Compose with operators
    Flux<OrderSummary> summaries = orderFlux
        .filter(order -> order.getStatus() == Status.COMPLETED)
        .map(OrderSummary::from)
        .onErrorResume(ex -> Flux.empty()); // backpressure handled by Reactor

    // Backpressure: subscriber signals how many items it can handle
    orderFlux.subscribe(
        new BaseSubscriber<Order>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(10); // request 10 items at a time
            }
            @Override
            protected void hookOnNext(Order order) {
                process(order);
                request(1); // request next item after processing
            }
        }
    );
guidedSteps:
  - id: rp-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      According to the Reactive Manifesto, a system that scales resources up or down dynamically in response to demand is described as which property?
    inputConfig:
      options:
        - "Responsive — it replies quickly under all conditions"
        - "Resilient — it stays responsive in the face of failure"
        - "Elastic — it scales to the workload"
        - "Message-driven — it communicates asynchronously"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Elastic — it scales to the workload"]
      rejectedFeedback: "Elasticity is the Reactive Manifesto property that describes dynamic scaling. Responsiveness is about latency; resilience is about failure tolerance; message-driven is about communication style."
    hint: "Think about stretching and shrinking resources to match demand."
    reflectionPrompt: "All four Manifesto properties are interconnected — elasticity enables responsiveness under variable load."
  - id: rp-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In Project Reactor, the type that represents a stream of 0 to N asynchronous elements (like a list of database rows arriving over time) is called ___.
    inputConfig:
      placeholder: "Project Reactor type name"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Flux"]
      rejectedFeedback: "Flux<T> represents a stream of 0 to N elements. Mono<T> is for 0 or 1 element. Flux is the reactive equivalent of a collection or stream, but non-blocking and backpressure-aware."
    hint: "Mono is for one result; this is for many."
    reflectionPrompt: "Mono/Flux map roughly to Optional/Stream in the reactive world, but are asynchronous and non-blocking."
  - id: rp-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain backpressure in reactive programming and give a concrete example of what happens without it when a fast producer sends data to a slow consumer.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [backpressure, producer, consumer, overwhelm, buffer, memory, OutOfMemory, flow control, slow]
      rejectedFeedback: "Backpressure is a flow-control mechanism where the consumer signals to the producer how much data it can handle. Without backpressure, a fast producer overwhelms a slow consumer: the buffer grows unboundedly, eventually causing OutOfMemoryError or dropped data. With backpressure, the consumer controls the rate of data flow."
    hint: "Think about what happens when a fire hose is connected to a garden hose."
    reflectionPrompt: "Backpressure is not just a technical mechanism — it is an expression of the contract between producer and consumer."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which Spring module provides reactive, non-blocking HTTP server support using Project Reactor?"
    options:
      - "Spring MVC"
      - "Spring WebFlux"
      - "Spring Batch"
      - "Spring Integration"
    correctIndex: 1
    feedback: "Spring WebFlux is the reactive web stack in Spring, built on Project Reactor. It uses an event-loop model (Netty by default) instead of servlet threads."
  - type: MULTIPLE_CHOICE
    question: "Reactive programming is most appropriate when your application is primarily:"
    options:
      - "CPU-bound with complex calculations"
      - "Executing short, synchronous database transactions"
      - "I/O-bound with many concurrent connections and streaming data"
      - "Running batch jobs overnight without user interaction"
    correctIndex: 2
    feedback: "Reactive programming's non-blocking I/O and backpressure shine when handling many concurrent I/O operations or continuous data streams. For CPU-bound or simple synchronous work, the added complexity is not justified."
retrieval:
  recall: "List the four Reactive Manifesto properties and explain backpressure in one sentence each."
  explain: "A junior developer asks: 'Why would I use Flux instead of just returning a List from my service?' Explain the difference with reference to non-blocking I/O and backpressure."
  mistakeId:
    code: |
      @GetMapping("/orders")
      public Flux<Order> getOrders() {
          return orderRepository.findAll()
                               .collectList()
                               .block()
                               .stream()
                               .collect(Flux::fromIterable);
      }
    answer: "The .block() call blocks the reactive pipeline on the calling thread, defeating the entire purpose of reactive programming and potentially deadlocking the event loop. collectList().block() forces a blocking wait for all results. The correct approach is to return the Flux directly: return orderRepository.findAll();"
---

# Hook

The ancient aqueduct system of the Academy doesn't push water to every dormitory simultaneously — it responds. When demand rises in the east wing, more water flows there. When a pipe breaks, alternate routes activate. When the reservoir is low, the flow rate is reduced. The entire system is responsive, resilient, elastic, and message-driven. You are about to learn to build software the same way.

# Lore Introduction

The most advanced enchanters at the Arcane Academy do not write spells that produce single results and halt. They weave *streams of enchantment* — continuous flows of magical energy that adapt to demand, recover from disruptions, and never overwhelm their recipients. This is reactive magic, and it requires a new way of thinking about how power flows through a system.

Project Reactor is your grimoire for reactive Java.

# Core Learning

## Concept Introduction

**Reactive programming** is a paradigm for building asynchronous, non-blocking systems that can handle streams of data with built-in flow control (backpressure). It is formalised in the **Reactive Manifesto** (2014), which defines four properties of reactive systems.

### The Reactive Manifesto

| Property | Meaning |
|---|---|
| **Responsive** | The system replies in a timely manner under all conditions |
| **Resilient** | The system stays responsive in the face of failure |
| **Elastic** | The system scales to the workload (up and down) |
| **Message-driven** | Components communicate via async, non-blocking messages |

### Project Reactor types

- `Mono<T>` — 0 or 1 asynchronous result (like `Optional` but non-blocking)
- `Flux<T>` — 0 to N asynchronous results (like `Stream` but non-blocking, with backpressure)

## Why It Matters

Traditional imperative code blocks threads. Under high I/O load, thread pools exhaust. Reactive programming uses a tiny thread pool and non-blocking I/O, allowing millions of concurrent operations. It also solves the producer/consumer imbalance problem via backpressure.

Use reactive when: many concurrent I/O operations, streaming data, or event-driven architectures.
Avoid reactive when: simple CRUD, CPU-bound computation, or when team familiarity is low.

## Worked Examples

### Mono and Flux basics

```java
// Mono: single async result
Mono<User> userMono = userRepository.findById(userId); // R2DBC non-blocking

// Flux: stream of results
Flux<Order> orderFlux = orderRepository.findByUserId(userId);

// Nothing happens until subscription
userMono.subscribe(
    user  -> System.out.println("User: " + user),
    error -> System.err.println("Error: " + error),
    ()    -> System.out.println("Complete")
);
```

Reactive is *lazy*: the pipeline executes only when someone subscribes.

### Composing a reactive pipeline

```java
Flux<OrderSummary> summaries = orderRepository.findByUserId(userId)
    .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
    .map(OrderSummary::from)
    .take(10)                          // limit results
    .timeout(Duration.ofSeconds(5))    // fail if too slow
    .onErrorResume(ex -> {
        log.error("Order stream failed", ex);
        return Flux.empty();            // graceful fallback
    });
```

### Spring WebFlux controller

```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    @GetMapping("/{userId}")
    public Flux<OrderSummary> getOrders(@PathVariable String userId) {
        // Returned directly — Spring WebFlux subscribes when request arrives
        return orderRepository.findByUserId(userId)
            .map(OrderSummary::from);
    }

    @GetMapping("/user/{userId}")
    public Mono<ResponseEntity<User>> getUser(@PathVariable String userId) {
        return userRepository.findById(userId)
            .map(user -> ResponseEntity.ok(user))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
```

No blocking, no thread-per-request. Netty's event loop handles all connections.

### Backpressure in action

```java
// Without backpressure: fast producer overwhelms slow consumer
Flux.range(1, 1_000_000)
    .subscribe(i -> {
        Thread.sleep(100); // simulate slow consumer — would eventually OOM
    });

// With explicit backpressure: consumer controls rate
Flux.range(1, 1_000_000)
    .subscribe(new BaseSubscriber<Integer>() {
        @Override
        protected void hookOnSubscribe(Subscription subscription) {
            request(1); // request one at a time
        }
        @Override
        protected void hookOnNext(Integer value) {
            process(value);
            request(1); // request next only after processing
        }
    });
```

### Combining Mono and Flux

```java
// Zip two Monos together
Mono<Dashboard> dashboard = Mono.zip(
    userRepository.findById(userId),
    settingsRepository.findByUserId(userId),
    (user, settings) -> new Dashboard(user, settings)
);

// flatMap: Mono to Flux (one user to many orders)
Flux<Order> userOrders = userRepository.findById(userId)
    .flatMapMany(user -> orderRepository.findByUserId(user.getId()));
```

## Common Mistakes

1. **Calling .block() in a reactive context.** Block stalls the event loop thread and can cause deadlocks. Only call block() at the very top of your stack (e.g., in a test or main method), never in a reactive pipeline.

2. **Forgetting to subscribe.** Reactive pipelines are lazy — if nothing subscribes, nothing executes. In Spring WebFlux, the framework subscribes for you on HTTP calls, but in other contexts you must subscribe explicitly.

3. **Mixing reactive and blocking I/O.** Using a blocking JDBC driver inside a Flux pipeline defeats the purpose. Use R2DBC for reactive database access.

4. **Ignoring the complexity cost.** Reactive code is harder to debug (stack traces are fragmented), harder to understand (new mental model), and harder to test. Use it where the concurrency benefit justifies the cost.

5. **Using reactive for CPU-bound work.** Reactive is designed for I/O-bound operations. Heavy computation belongs in a dedicated `Schedulers.boundedElastic()` worker pool: `Flux.fromIterable(data).publishOn(Schedulers.boundedElastic()).map(this::heavyComputation)`.

## Mental Model

Reactive programming is a river system. The source (producer) generates water; tributaries (operators) filter, transform, and route it; the mouth (subscriber) receives it. Backpressure is the dam that controls flow rate. Without the dam, downstream floods — with it, the entire system runs at the pace the consumer can handle.

## Mini Summary

- The Reactive Manifesto defines: responsive, resilient, elastic, message-driven systems
- `Mono<T>` is 0/1 async result; `Flux<T>` is 0/N async stream
- Backpressure allows consumers to signal their capacity to producers, preventing overflow
- Spring WebFlux and Project Reactor provide the Java reactive stack
- Reactive adds significant complexity — only use it when the I/O concurrency gains justify it

# Guided Practice Quest

Work through the guided steps to practise identifying Reactive Manifesto properties and writing basic Flux/Mono pipelines.

# Solo Practice Quest

Design a `StreamingDashboardService` using Spring WebFlux and Project Reactor that:
1. Exposes a `Flux<LiveMetric>` endpoint that streams real-time system metrics every second
2. Filters out metrics below a significance threshold
3. Handles errors by logging and resuming with an empty element
4. Limits the stream to a configurable maximum duration using `take(Duration)`

Write the controller and service. Explain where backpressure applies and what would happen if the consumer is slower than the 1-second production rate.

# Integration

**Connecting to Mathematics — Signal Theory and Design — System Responsiveness**

Reactive programming has deep roots in mathematical signal processing. A `Flux<T>` is essentially a discrete-time signal: a sequence of values indexed by time (or event occurrence), processed through a pipeline of transformations (analogous to signal filters). Operators like `debounce`, `throttle`, `window`, and `buffer` are direct translations of signal-processing concepts into the reactive API. `backpressure` corresponds to the Nyquist-Shannon sampling theorem principle: you can only meaningfully process a signal at the rate your system can sample it.

From a system design perspective, reactive architectures change the fundamental contract between services. In an imperative system, a slow consumer creates latency that propagates upstream as thread exhaustion. In a reactive system, slow consumers signal via backpressure, allowing the producer to reduce its rate rather than crash. This shifts the design question from "how many threads can we provision?" to "what is the right flow-control contract between each pair of services?" Systems designed reactively from the start have explicit, negotiated throughput contracts at every boundary, making capacity planning and SLA enforcement far more tractable. The tradeoff — operator complexity, fragmented stack traces, reactive library lock-in — is real and should inform architectural decisions rather than being dismissed. Senior engineers choose reactive where the concurrency and streaming requirements demand it, and resist the temptation to use it everywhere simply because it is powerful.

# Lore Conclusion

The Academy's aqueduct hums with new efficiency. Water flows exactly where it is needed, at exactly the rate each chamber can receive it. No overflow, no drought. When the eastern wing's rune-forge fails, flow reroutes automatically to the backup channel. You have inscribed the four properties of reactive magic into your staff, and the rivers of data will bend to your will.

---
