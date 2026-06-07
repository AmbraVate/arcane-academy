---
id: se-sen-m3-04
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m3
moduleTitle: "Module 3: Asynchronous Programming"
moduleGlyph: "⚡"
moduleSortOrder: 3
topicSlug: async_workflows
topicTitle: "Async Workflows"
topicSortOrder: 4
lesson: async_workflows
title: "Async Workflows"
sortOrder: 4
difficulty: 4
estimatedMinutes: 30
xpReward: 60
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [futures_and_promises]
integrationDomains: [design, psychology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Explains the difference between thenApply and thenCompose"
    - "Demonstrates parallel fan-out using thenCombine or allOf"
    - "Handles errors with exceptionally or handle"
    - "Implements a timeout pattern using orTimeout or completeOnTimeout"
    - "Contrasts async CompletableFuture approach with reactive streams"
  keywords: [thenApply, thenCompose, thenCombine, exceptionally, handle, allOf, orTimeout, fan-out, backpressure, reactive]
  modelAnswer: |
    // Fan-out: fetch user and orders in parallel, combine results
    CompletableFuture<User> userFuture = userService.findByIdAsync(userId);
    CompletableFuture<List<Order>> ordersFuture = orderService.findByUserAsync(userId);

    CompletableFuture<UserProfile> profile = userFuture
        .thenCombine(ordersFuture, (user, orders) -> new UserProfile(user, orders))
        .exceptionally(ex -> {
            log.error("Failed to build profile", ex);
            return UserProfile.empty();
        })
        .orTimeout(5, TimeUnit.SECONDS);

    // thenApply transforms the result (sync lambda), thenCompose chains another async operation
    CompletableFuture<String> name = userFuture.thenApply(User::getName);
    CompletableFuture<Address> address = userFuture.thenCompose(u -> addressService.findAsync(u.getAddressId()));

    // Reactive (Project Reactor) alternative for streaming scenarios:
    Flux<Order> orderStream = orderRepository.findByUserId(userId); // non-blocking, backpressure-aware
guidedSteps:
  - id: awf-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You need to transform the result of a CompletableFuture<User> into a String (user's display name) without starting another async operation. Which method should you use?
    inputConfig:
      options:
        - "thenCompose — it is designed to chain transformations"
        - "thenApply — it applies a synchronous function to the result"
        - "thenCombine — it merges two futures together"
        - "exceptionally — it handles errors but also transforms results"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["thenApply — it applies a synchronous function to the result"]
      rejectedFeedback: "thenApply is the correct choice for a synchronous transformation of a single future's result. thenCompose is for when your lambda itself returns another CompletableFuture."
    hint: "Think about whether your lambda returns a plain value or another CompletableFuture."
    reflectionPrompt: "thenApply maps; thenCompose flatMaps. Knowing this distinction prevents nested CompletableFuture<CompletableFuture<T>> types."
  - id: awf-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the fan-out pattern. You want to fetch a user and their settings in parallel, then combine them:
      ```java
      CompletableFuture<User> uf = userService.findAsync(id);
      CompletableFuture<Settings> sf = settingsService.findAsync(id);
      CompletableFuture<Dashboard> dash = uf.___(sf, (u, s) -> new Dashboard(u, s));
      ```
      What method goes in the blank?
    inputConfig:
      placeholder: "method name"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["thenCombine"]
      rejectedFeedback: "thenCombine accepts a second CompletableFuture and a BiFunction to combine both results when both complete."
    hint: "This method takes two futures and merges their results with a BiFunction."
    reflectionPrompt: "thenCombine is your go-to for merging exactly two independent async results."
  - id: awf-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A colleague proposes using CompletableFuture chaining for a system that streams 10,000 real-time sensor events per second to subscribers. Explain why this might be the wrong tool and what you would recommend instead.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [reactive, backpressure, Flux, WebFlux, Reactor, streaming, overwhelm]
      rejectedFeedback: "CompletableFuture models a single async result. For streaming data, reactive frameworks like Project Reactor (Flux/Mono) provide backpressure — meaning consumers can signal how fast they can process, preventing memory overload from producers that outpace consumers."
    hint: "Consider what happens when the producer generates data faster than the consumer can process it."
    reflectionPrompt: "CompletableFuture: one result. Reactive streams: continuous data flow with flow control."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which CompletableFuture method adds error recovery by providing a fallback value when the future completes exceptionally?"
    options:
      - "thenApply"
      - "thenCompose"
      - "exceptionally"
      - "thenAccept"
    correctIndex: 2
    feedback: "exceptionally(fn) is invoked only when the future completes with an exception, allowing you to return a default/fallback value and resume normal execution."
  - type: MULTIPLE_CHOICE
    question: "You fire 5 independent async calls and want to wait for ALL of them before proceeding. Which method is appropriate?"
    options:
      - "thenCombine on each pair"
      - "CompletableFuture.anyOf"
      - "CompletableFuture.allOf"
      - "CompletableFuture.runAsync"
    correctIndex: 2
    feedback: "CompletableFuture.allOf(f1, f2, ...) returns a new future that completes when all supplied futures complete, making it the right tool for fan-in across many parallel operations."
retrieval:
  recall: "Name four CompletableFuture chaining methods and the key difference between thenApply and thenCompose."
  explain: "A junior dev asks: 'Why do we chain .exceptionally() on a CompletableFuture instead of wrapping it in try-catch?' Explain it to them."
  mistakeId:
    code: |
      CompletableFuture<User> future = userService.findAsync(id);
      CompletableFuture<Address> result = future.thenApply(u -> addressService.findAsync(u.getAddressId()));
    answer: "thenApply is used but addressService.findAsync returns a CompletableFuture<Address>, so the actual return type is CompletableFuture<CompletableFuture<Address>>. thenCompose should be used instead to flatten the nested future."
---

# Hook

The spell-circuit diagram spans an entire wall of the arcane laboratory. Twenty runes must fire in a precise sequence — some in parallel, some chained — and if any one fails, the whole weave must fall back gracefully rather than detonate. This is exactly the challenge of async workflows: composing asynchronous operations into reliable, readable pipelines.

# Lore Introduction

In the higher halls of the Arcane Academy, senior mages do not cast spells in simple sequences. They orchestrate *weaves* — complex tapestries of concurrent enchantments that fan out to gather ingredients, combine their results, and recover elegantly from component failures. The Grimoire of Concurrent Weaving teaches that mastery lies not in any single spell, but in how those spells are composed.

Java's `CompletableFuture` is your weaving loom.

# Core Learning

## Concept Introduction

A `CompletableFuture<T>` represents a result that will be available in the future. The true power emerges when you *compose* futures: chaining, combining, and recovering from failure across multiple asynchronous steps.

**Core chaining methods:**

| Method | Input | Output | Use case |
|---|---|---|---|
| `thenApply(fn)` | `T` | `U` | Sync transformation of result |
| `thenCompose(fn)` | `T` | `CompletableFuture<U>` | Chain another async operation |
| `thenCombine(other, fn)` | `T`, `U` | `V` | Merge two independent futures |
| `thenAccept(fn)` | `T` | `void` | Consume result (no transform) |
| `exceptionally(fn)` | `Throwable` | `T` | Recover from failure |
| `handle(fn)` | `T`, `Throwable` | `U` | Transform result or error |

## Why It Matters

Without composition, async code collapses into deeply nested callbacks. With it, you can build readable pipelines, run independent operations in parallel (fan-out), collect their results (fan-in), and handle failures at any point — all with a fluent API that resembles synchronous code in its readability.

## Worked Examples

### Pattern 1 — Linear chain

```java
// thenApply: sync transform
// thenCompose: start a new async operation from the result
CompletableFuture<String> enrichedName = userService.findByIdAsync(userId)
    .thenApply(User::getEmail)                              // sync: User -> String
    .thenCompose(email -> auditService.logAccessAsync(email)) // async: String -> CF<String>
    .exceptionally(ex -> {
        log.warn("Failed during enrichment: {}", ex.getMessage());
        return "unknown";
    });
```

### Pattern 2 — Fan-out / fan-in with thenCombine

```java
// Two independent calls, combined when both complete
CompletableFuture<User> userFuture    = userService.findByIdAsync(userId);
CompletableFuture<Inventory> invFuture = inventoryService.findForUserAsync(userId);

CompletableFuture<Dashboard> dashboard = userFuture
    .thenCombine(invFuture, (user, inventory) -> new Dashboard(user, inventory))
    .orTimeout(5, TimeUnit.SECONDS)
    .exceptionally(ex -> Dashboard.empty());
```

Both service calls are fired simultaneously. The BiFunction runs only when *both* complete.

### Pattern 3 — Fan-out to many with allOf

```java
List<CompletableFuture<Report>> futures = reportIds.stream()
    .map(reportService::generateAsync)
    .collect(Collectors.toList());

CompletableFuture<Void> allDone = CompletableFuture.allOf(
    futures.toArray(new CompletableFuture[0])
);

// allOf returns Void; collect results manually after
CompletableFuture<List<Report>> allReports = allDone.thenApply(v ->
    futures.stream()
           .map(CompletableFuture::join) // safe — all are complete
           .collect(Collectors.toList())
);
```

### Pattern 4 — handle vs exceptionally

```java
// exceptionally: only fires on error, must return same type T
future.exceptionally(ex -> defaultValue);

// handle: always fires, receives (result, exception) — either may be null
future.handle((result, ex) -> {
    if (ex != null) return fallback;
    return transform(result);
});
```

`handle` is more flexible because it sees both the happy and error paths, allowing logging, metrics, or conditional fallback.

### Pattern 5 — Timeout

```java
// Java 9+: orTimeout throws TimeoutException if not done in time
CompletableFuture<Data> withTimeout = slowService.fetchAsync()
    .orTimeout(3, TimeUnit.SECONDS)
    .exceptionally(ex -> Data.empty());

// completeOnTimeout: provide a value instead of throwing
CompletableFuture<Data> withDefault = slowService.fetchAsync()
    .completeOnTimeout(Data.empty(), 3, TimeUnit.SECONDS);
```

## Common Mistakes

1. **Using thenApply when thenCompose is needed.** When your lambda returns `CompletableFuture<T>`, use `thenCompose` — otherwise you get `CompletableFuture<CompletableFuture<T>>`.

2. **Calling .join() or .get() inside a lambda.** This blocks the calling thread. Async pipelines are ruined the moment you synchronously wait inside them.

3. **Not handling exceptions.** Unhandled exceptions in CompletableFuture chains are silently swallowed unless you attach `.exceptionally()` or `.handle()`.

4. **Ignoring thread pool choice.** By default, async stages run on the ForkJoinPool. For I/O-bound work, supply a custom executor: `thenApplyAsync(fn, myExecutor)`.

5. **Using CompletableFuture for streaming data.** It models a *single* result. For continuous event streams (sensor data, message queues), use reactive streams (Project Reactor's `Flux`) which support backpressure.

## Mental Model

Think of `CompletableFuture` chaining as a conveyor belt:
- `thenApply` → reshape the item on the belt (stays on same belt)
- `thenCompose` → send item to a new belt that runs its own operation
- `thenCombine` → merge two parallel belts into one
- `allOf` → wait for all parallel belts to finish, then collect
- `exceptionally` → a rescue ramp that catches fallen items

## Mini Summary

- `thenApply` transforms a result synchronously; `thenCompose` chains another async operation
- `thenCombine` merges two independent futures when both complete
- `allOf` fans in multiple parallel futures; collect results manually after completion
- `exceptionally` recovers from failure; `handle` processes both result and error paths
- Use `orTimeout`/`completeOnTimeout` to enforce SLAs; use reactive streams for high-volume data flows

# Guided Practice Quest

Work through the steps above to practise identifying the correct chaining method and constructing fan-out patterns.

# Solo Practice Quest

Design a `DashboardService.buildAsync(userId)` that:
1. Fetches user data and recent notifications in parallel
2. Fetches order history only after the user is confirmed to exist (chained, not parallel)
3. Combines all three results into a `DashboardDTO`
4. Falls back to a cached `DashboardDTO` if anything fails within 4 seconds

Write the method with full CompletableFuture composition. Explain your choice of `thenApply` vs `thenCompose` vs `thenCombine` at each step.

# Integration

**Connecting to Design — Workflow Composition and Psychology — Cognitive Load**

Async workflows are not just a technical concern — they are a design concern. When architecting a system, how you compose operations determines system latency, error propagation, and failure blast radius. A well-designed async workflow mirrors good UI design: operations that are independent should run in parallel (just as visual elements that are unrelated should not block each other from loading), and the user (or calling system) should receive a coherent response even when parts fail.

The psychology connection is equally important. Deeply nested callback code imposes a high cognitive load on the developer reading it — the brain must track multiple asynchronous threads of execution simultaneously. The fluent CompletableFuture API reduces cognitive load by presenting async logic as a linear, readable chain. This is the same principle as progressive disclosure in UX: show complexity only when needed, hide it behind clear abstractions otherwise.

When designing async workflows, the questions to ask are: Which steps are truly independent? Where is the critical path? What does the system look like to a downstream caller when one service is slow? Thinking about async pipelines as design artefacts — not just implementation details — leads to cleaner, more observable, more testable systems. Teams that visualise their async graphs (as dependency diagrams or sequence diagrams) consistently produce better APIs because the design intent is explicit before a single line of code is written.

# Lore Conclusion

The senior mage steps back and surveys the completed weave. Each thread of the spell runs to its natural conclusion, errors caught by failsafe runes, parallel branches rejoining cleanly at the focal point. The enchantment fires — not in a chaotic cascade, but with the elegant precision of a composed pipeline. You have earned the right to call yourself a Weave Architect.

---
