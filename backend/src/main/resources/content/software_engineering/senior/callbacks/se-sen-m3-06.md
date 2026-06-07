---
id: se-sen-m3-06
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m3
moduleTitle: "Module 3: Asynchronous Programming"
moduleGlyph: "⚡"
moduleSortOrder: 3
topicSlug: callbacks
topicTitle: "Callbacks"
topicSortOrder: 6
lesson: callbacks
title: "Callbacks"
sortOrder: 6
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [event_loops]
integrationDomains: [mathematics, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Demonstrates a callback using a Java functional interface"
    - "Explains inversion of control in the callback pattern"
    - "Identifies callback hell and explains why it reduces readability"
    - "Shows how CompletableFuture or Promises resolve callback hell"
    - "Names at least two Java functional interfaces used as callbacks"
  keywords: [callback, functional interface, inversion of control, callback hell, CompletableFuture, Consumer, BiConsumer, lambda, promise, composition]
  modelAnswer: |
    // Callback via functional interface
    public void fetchUser(String id, Consumer<User> onSuccess, Consumer<Throwable> onError) {
        executor.submit(() -> {
            try {
                User user = repository.findById(id);
                onSuccess.accept(user);
            } catch (Exception e) {
                onError.accept(e);
            }
        });
    }

    // Callback hell: deeply nested, hard to read
    fetchUser(id, user ->
        fetchOrders(user, orders ->
            fetchPayments(orders, payments ->
                sendReport(payments, result ->
                    log.info("Done: {}", result),
                    err -> log.error("payment error", err)),
                err -> log.error("orders error", err)),
            err -> log.error("user error", err)));

    // Solution: CompletableFuture flattens the chain
    fetchUserAsync(id)
        .thenCompose(user -> fetchOrdersAsync(user))
        .thenCompose(orders -> fetchPaymentsAsync(orders))
        .thenCompose(payments -> sendReportAsync(payments))
        .thenAccept(result -> log.info("Done: {}", result))
        .exceptionally(err -> { log.error("Error", err); return null; });
guidedSteps:
  - id: cb-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which principle does the callback pattern exemplify by allowing the caller to define behaviour that the callee executes?
    inputConfig:
      options:
        - "Encapsulation — the callback hides internal state"
        - "Inversion of Control — the caller passes behaviour to the callee to invoke"
        - "Open/Closed Principle — callbacks extend behaviour without modification"
        - "Dependency Inversion — the callback depends on abstractions not concretions"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Inversion of Control — the caller passes behaviour to the callee to invoke"]
      rejectedFeedback: "Callbacks are a classic demonstration of Inversion of Control (IoC): instead of the caller calling the callee directly for a result, the callee calls back into the caller's code when the result is ready. Control of execution is inverted."
    hint: "Who controls when the logic runs — the caller or the callee?"
    reflectionPrompt: "IoC appears in many forms: callbacks, dependency injection, event listeners. Recognising the pattern helps you reason about control flow."
  - id: cb-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In Java, a callback that accepts one argument and returns void is most naturally expressed using the `___` functional interface from java.util.function.
    inputConfig:
      placeholder: "functional interface name"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Consumer", "Consumer<T>"]
      rejectedFeedback: "Consumer<T> is the standard functional interface for accepting a single argument with no return value — exactly the shape of a typical callback."
    hint: "Think: you receive a value and consume it without returning anything."
    reflectionPrompt: "Java's functional interface library (Consumer, BiConsumer, Function, Supplier) maps directly onto callback shapes."
  - id: cb-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain what "callback hell" is, why it is considered problematic, and how CompletableFuture addresses the issue.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [nested, readability, thenCompose, CompletableFuture, chain, flat, composition]
      rejectedFeedback: "Callback hell is deeply nested callback code where each async step's callback contains the next step's callback. It makes code hard to read, debug, and reason about error handling. CompletableFuture addresses this by providing a fluent, linear chain of operations (thenApply, thenCompose, exceptionally) that represents the same async flow without nesting."
    hint: "Draw the shape of callback hell in your mind — what does deeply nested code look like structurally?"
    reflectionPrompt: "The evolution from callbacks to Promises/Futures to reactive streams tracks the industry's progress in managing async complexity."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which Java functional interface would you use as a callback that receives both a result AND an error (either may be null)?"
    options:
      - "Consumer<T>"
      - "Function<T, R>"
      - "BiConsumer<T, Throwable>"
      - "Supplier<T>"
    correctIndex: 2
    feedback: "BiConsumer<T, Throwable> accepts two parameters — the result (possibly null on error) and the error (possibly null on success) — matching the typical success/error callback pattern."
  - type: MULTIPLE_CHOICE
    question: "What is the main readability problem with deeply nested callbacks compared to CompletableFuture chains?"
    options:
      - "Callbacks are slower to execute than CompletableFuture"
      - "The indentation pyramid makes control flow and error handling hard to follow"
      - "Callbacks cannot handle exceptions"
      - "CompletableFuture automatically parallelises the operations"
    correctIndex: 1
    feedback: "Callback hell creates a pyramid of indented code where the logical sequence of operations and error paths are interleaved, making it very hard to trace what happens at each step."
retrieval:
  recall: "Name three Java functional interfaces commonly used as callbacks and explain the IoC principle in the callback pattern."
  explain: "Explain callback hell to a junior developer who has only seen synchronous code, using a simple analogy."
  mistakeId:
    code: |
      fetchUser(id, user -> {
          fetchOrders(user.getId(), orders -> {
              fetchPayments(orders.get(0).getId(), payment -> {
                  processPayment(payment);
              }, err -> handleError(err));
          }, err -> handleError(err));
      }, err -> handleError(err));
    answer: "This is callback hell — three levels of nesting with error handling duplicated at each level. This is hard to read, difficult to maintain, and error handling is not centralised. The fix is to convert each async operation to return CompletableFuture and chain with thenCompose, with a single .exceptionally() at the end."
---

# Hook

You hire a blacksmith to forge a blade. Rather than waiting by the forge, you hand them a note: "When it is done, deliver it to the armoury." You have inverted control — the blacksmith will call back into your instructions when ready. This is the callback pattern, and it is both elegant and, when abused, infernal.

# Lore Introduction

Junior enchanters learn to call upon spirits directly, waiting for each response before continuing. Senior mages understand a more powerful approach: inscribe your instructions into a scroll and hand it to the spirit. When its work is done, the spirit executes your scroll — a callback. The mage is free to pursue other work while waiting.

But the most powerful magic can become the most tangled. Scrolls that contain more scrolls, containing more scrolls — the Pyramid of Doom awaits the careless practitioner.

# Core Learning

## Concept Introduction

A **callback** is a function (or functional interface in Java) passed as an argument to another function, to be invoked when a specific event or condition occurs. The callback pattern is one of the foundational building blocks of asynchronous programming.

**Key characteristics:**
- Inverts control: the caller hands code to the callee to execute later
- Decouples when logic is defined from when it is executed
- Foundational to event listeners, async I/O, and timer-based operations

## Why It Matters

Callbacks enable non-blocking execution. Rather than blocking a thread waiting for a result, you hand a callback to the async operation and move on. The runtime invokes your callback when the result is ready. This is the core mechanism behind Java's `AsynchronousFileChannel`, NIO's `CompletionHandler`, and many event-driven frameworks.

Understanding callbacks is essential because they are the precursor to Promises/Futures and reactive streams — knowing why callbacks were insufficient explains why those abstractions were invented.

## Worked Examples

### Simple callback via Consumer

```java
// Service method accepting callbacks
public void fetchUser(String userId,
                      Consumer<User> onSuccess,
                      Consumer<Throwable> onError) {
    CompletableFuture.supplyAsync(() -> userRepository.findById(userId))
        .thenAccept(onSuccess)
        .exceptionally(ex -> { onError.accept(ex); return null; });
}

// Caller passes lambdas as callbacks
fetchUser("u-42",
    user -> System.out.println("Found: " + user.getName()),
    err  -> System.err.println("Error: " + err.getMessage())
);
```

### Java NIO CompletionHandler — the formal callback interface

```java
AsynchronousFileChannel channel = AsynchronousFileChannel.open(path);
ByteBuffer buffer = ByteBuffer.allocate(1024);

channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
    @Override
    public void completed(Integer bytesRead, ByteBuffer attachment) {
        attachment.flip();
        // process data — this is the callback
    }
    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        log.error("Read failed", exc);
    }
});
// Execution continues here immediately; callback fires later
```

`CompletionHandler<V, A>` is Java's built-in callback interface for async I/O, with `completed` and `failed` methods.

### Callback hell illustrated

```java
// Three nested async operations — readability degrades rapidly
fetchUser(userId,
    user -> fetchOrders(user.getId(),
        orders -> fetchInvoice(orders.get(0).getId(),
            invoice -> {
                // Business logic buried 3 lambdas deep
                sendEmail(invoice);
            },
            err -> handleError("invoice", err)),
        err -> handleError("orders", err)),
    err -> handleError("user", err));
```

Error handling is duplicated at each level. Adding a fourth step requires another nesting level.

### Evolution to CompletableFuture (resolving callback hell)

```java
fetchUserAsync(userId)
    .thenCompose(user    -> fetchOrdersAsync(user.getId()))
    .thenCompose(orders  -> fetchInvoiceAsync(orders.get(0).getId()))
    .thenAccept(invoice  -> sendEmail(invoice))
    .exceptionally(err   -> { handleError(err); return null; });
```

The same sequence is now linear, readable left-to-right, with a single error handler at the end.

### Inversion of Control in callbacks

```java
// Without callbacks (caller controls timing):
User user = userRepository.findById(id); // blocks caller
process(user);

// With callbacks (callee controls timing):
userRepository.findByIdAsync(id, user -> process(user)); // callee calls back when ready
// caller continues here immediately
```

The caller no longer controls when `process(user)` runs — the callee does. This is IoC.

## Common Mistakes

1. **Forgetting to handle errors.** Single-argument callbacks that only handle success silently discard exceptions. Always use BiConsumer or separate error callbacks.

2. **Callback hell through incremental addition.** Nesting starts with one callback, seems fine, then grows. Refactor to CompletableFuture chains before the third nesting level.

3. **Shared mutable state in callbacks.** Callbacks may execute on different threads. Accessing unsynchronised shared state causes race conditions.

4. **Fire-and-forget callbacks.** Passing a callback but never invoking it on all paths (especially error paths) creates silent failures.

5. **Not understanding thread context.** The thread that invokes the callback may not be the thread that registered it. Thread-local variables and UI frameworks (like JavaFX Application Thread) require explicit context switching.

## Mental Model

Callbacks are like leaving your phone number at a restaurant when there is a wait. You go about your day (non-blocking), and the restaurant calls you back (callback) when your table is ready. Callback hell is leaving your number with a restaurant that calls a second restaurant on your behalf, which calls a third, and by the time you hear anything, three different people are involved and nobody is sure who is responsible for what.

## Mini Summary

- A callback is a function passed to another function to be invoked when a result is ready
- Inversion of Control: the callee controls when the callback runs, not the caller
- Java callbacks use functional interfaces: `Consumer`, `BiConsumer`, `CompletionHandler`
- Callback hell arises from deeply nested callbacks, degrading readability and error handling
- CompletableFuture chains solve callback hell with a flat, composable pipeline

# Guided Practice Quest

Work through the guided steps to practise identifying IoC in callbacks and converting nested callbacks to CompletableFuture chains.

# Solo Practice Quest

Convert the following callback-based `NotificationService` into a `CompletableFuture`-based equivalent:

```java
notificationService.findRecipients(campaignId,
    recipients -> emailGateway.send(recipients, template,
        sentCount -> auditLog.record(campaignId, sentCount,
            () -> log.info("Campaign {} complete", campaignId),
            err -> log.error("Audit failed", err)),
        err -> log.error("Email failed", err)),
    err -> log.error("Recipients failed", err));
```

Explain the specific problems with the original code and how your refactored version addresses them.

# Integration

**Connecting to Mathematics — Function Composition and Design — API Design**

The callback pattern is mathematically equivalent to higher-order functions — functions that accept other functions as arguments. This is a core concept in lambda calculus, the theoretical foundation of functional programming. When you pass a `Consumer<User>` as a callback, you are applying function composition: `g(f(x))` where `f` produces a `User` and `g` consumes it. The composition is deferred — `g` is not applied until `f` completes.

From an API design perspective, the choice between callback-based and CompletableFuture-based APIs is a statement about the expected usage pattern. Callback APIs are low-level and flexible but impose a higher cognitive burden on callers. Promise/Future APIs are higher-level and composable, making them better suited for application-layer code. Good API design chooses the abstraction level appropriate to the audience: a framework internals might expose callbacks for maximum control; an application service should expose CompletableFuture or reactive types for composability. The progression from raw callbacks to Promises to reactive streams tracks the industry's progressive understanding that async programming needs algebraic abstractions — not just mechanisms — to remain maintainable at scale. Recognising this history allows you to make deliberate, informed choices when designing your own async APIs rather than defaulting to whatever the framework hands you.

# Lore Conclusion

The blacksmith delivers the blade exactly as instructed. The callback scroll worked perfectly — once. But as the kingdom grew more complex, scrolls began to contain scrolls containing scrolls, and no one could follow the chain of instructions anymore. The senior mages commissioned the Futures Guild, who invented a better way: linear chains of intent, with a single recovery rune at the end. You now hold both the old scroll and the new chain. Use them wisely.

---
