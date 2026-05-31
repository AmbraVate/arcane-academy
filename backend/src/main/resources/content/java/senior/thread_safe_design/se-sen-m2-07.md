---
id: se-sen-m2-07
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m2
moduleTitle: "Module 2: Concurrency & Parallelism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: thread_safe_design
topicTitle: Thread-Safe Design
topicSortOrder: 7
lesson: thread_safe_design
title: "Thread-Safe Design"
sortOrder: 7
difficulty: 4
estimatedMinutes: 35
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Identifies immutability as eliminating the need for synchronization by design rather than adding it retroactively
    - Explains thread confinement (stack confinement, ThreadLocal, ad-hoc confinement) as eliminating the shared dimension
    - Articulates stateless services as the highest-scale thread-safe design: no per-request state means no sharing
    - Compares atomic variables (AtomicInteger, AtomicReference) with synchronized blocks and explains when each is preferable
    - Describes compare-and-swap (CAS) semantics, the ABA problem, and why optimistic concurrency scales better under low contention
  keywords: [immutability, thread confinement, ThreadLocal, stateless, atomic variable, CAS, compare-and-swap, ABA problem, AtomicReference, optimistic concurrency, volatile, final field]
  modelAnswer: |
    Thread-safe design is fundamentally about reducing the surface area of shared mutable state rather than managing access to it. Synchronization is a necessary tool when shared mutable state cannot be avoided, but it is a symptom of a design problem, not a solution to one. The most scalable and correct concurrent code has the smallest possible shared mutable state.

    Immutability is the most powerful thread-safety strategy. An object whose state cannot change after construction requires no synchronization — there is nothing to race on. In Java, immutability requires: all fields final, the object reference not shared before construction completes (safe publication), no mutable objects accessible through the immutable object's fields, and no methods that mutate state. The JMM provides a special guarantee for final fields: a reference to an immutable object that was safely published is guaranteed to see all final field values initialized by the constructor, without synchronization.

    Thread confinement achieves thread safety by eliminating the "shared" dimension rather than the "mutable" dimension. Stack confinement keeps objects in local variables never passed to other threads. ThreadLocal provides a per-thread copy of an object, ensuring that each thread has its own state without sharing. Ad-hoc confinement relies on conventions maintained by the programmer rather than the type system, which is fragile. The strongest confinement is when the constraint is enforced by the language or framework.

    Stateless services — services that hold no per-request or per-user instance state — are the simplest form of thread safety at scale. A Spring bean that holds no mutable instance fields and delegates all state to method parameters or infrastructure (database, cache) can serve any number of concurrent requests without synchronization overhead. This is why the recommendation to make Spring beans stateless is not merely a style preference — it is a thread-safety design decision.

    Atomic variables (AtomicInteger, AtomicLong, AtomicReference) use hardware CAS (compare-and-swap) instructions to implement non-blocking atomic operations. CAS takes an expected current value and a new value; if the current value matches the expected, the update is applied atomically; otherwise it fails and the caller retries. This is optimistic concurrency: assume no conflict, attempt the update, retry if conflicted. Under low-to-moderate contention, CAS outperforms synchronized because no thread is ever blocked. Under high contention, repeated CAS failures create a spin-retry loop that can waste CPU. The ABA problem — where a value changes from A to B and back to A between a read and a CAS, making the CAS falsely succeed — is addressed by AtomicStampedReference, which includes a version counter alongside the value.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "Why does the JMM provide a special visibility guarantee for final fields that is not available for non-final fields without synchronization?"
    options:
      - "The JVM caches final fields in a faster memory region"
      - "The JMM guarantees that all final fields are visible to any thread that reads the object reference, provided the object was safely published, because final fields cannot change after construction"
      - "Final fields are stored in thread-local storage automatically"
      - "The JVM prevents final field reads from being reordered with any other reads"
    correctIndex: 1
  - type: SHORT_TEXT
    prompt: "Explain the ABA problem in CAS operations and describe a scenario where it could cause incorrect behavior in a lock-free stack."
  - type: MULTIPLE_CHOICE
    prompt: "A service is implemented as a stateless Spring singleton. Under what condition would this service still have thread safety issues?"
    options:
      - "If it uses Spring's @Autowired annotation"
      - "If it holds mutable instance fields that are modified per-request, such as a running total or a request-specific cache"
      - "If it is accessed by more than 100 concurrent threads"
      - "If it calls other Spring beans"
    correctIndex: 1
  - type: SHORT_TEXT
    prompt: "Compare the performance characteristics of AtomicInteger.incrementAndGet() versus synchronized counter increment under three scenarios: (1) single thread, (2) low contention (10 threads, rare updates), (3) high contention (100 threads, constant updates). Explain the mechanism behind each difference."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    prompt: "Which thread-safety strategy eliminates the need for synchronization by removing the possibility of shared state rather than managing access to it?"
    options:
      - "Synchronized methods"
      - "Volatile fields"
      - "Thread confinement"
      - "ReentrantLock"
    correctIndex: 2
  - type: MULTIPLE_CHOICE
    prompt: "CAS (compare-and-swap) implements optimistic concurrency. What is the performance tradeoff compared to pessimistic locking?"
    options:
      - "CAS always outperforms locking regardless of contention level"
      - "CAS is faster under low-to-moderate contention because no threads are blocked; under high contention, repeated CAS failures create expensive spin-retry loops"
      - "CAS is only applicable to integer operations; locking is required for reference types"
      - "CAS prevents the ABA problem; locking does not"
    correctIndex: 1
retrieval:
  recall: "Name three strategies for achieving thread safety that do not rely on synchronization."
  explain: "Why is a stateless service inherently thread-safe, and what makes a Spring bean non-stateless?"
  mistakeId:
    code: |
      @Service
      public class OrderService {
          private List<Order> recentOrders = new ArrayList<>();

          public void processOrder(Order order) {
              recentOrders.add(order);
              // ... process
              if (recentOrders.size() > 100) {
                  recentOrders.remove(0);
              }
          }
      }
    answer: "This Spring singleton has a mutable instance field (recentOrders) shared across all requests and threads. Concurrent calls to processOrder will race on ArrayList, which is not thread-safe. The fix depends on intent: if recentOrders is per-session or per-request state, it should be a method-local variable or stored externally. If it is genuinely shared state (last N orders globally), it should use a thread-safe structure like CopyOnWriteArrayList or be replaced with a ConcurrentLinkedDeque with explicit size management."
---

# Hook

The engineers who never have concurrency bugs are not lucky — they design code where shared mutable state barely exists. They make objects immutable. They make services stateless. They confine per-request state to the stack. Synchronization is their last resort, applied to the minimal surface that genuinely requires it. The engineers who spend weeks debugging race conditions write mutable shared state everywhere and rely on synchronized to make it safe — a strategy that works until it does not, and when it fails, fails silently at scale. The question is not "did you synchronize?" but "why do you have shared mutable state at all?"

# Lore Introduction

Master architect Soren teaches thread-safe design through a thought experiment: "Imagine every method in your service is called simultaneously by ten thousand threads. Which ones break? Now ask why those methods have state that can break." The Academy's advanced concurrency curriculum begins not with lock patterns but with design: what kind of state does a class need, who owns it, and what is the lifecycle of that ownership? Thread safety problems are almost always state ownership problems in disguise, and the best fix is almost always to re-examine whether the state needs to be shared at all.

# Core Learning

## Concept Introduction

Thread-safe design means structuring code so that correctness under concurrent execution is a natural consequence of the design rather than a constraint managed through synchronization. The distinction matters because synchronization adds overhead, restricts scalability, and requires discipline to maintain correctly. Design-level thread safety — through immutability, confinement, or statelessness — requires no ongoing discipline because the structural properties make races impossible.

Immutability is the strongest design-level guarantee. An immutable object's state is fixed at construction; no mutation is possible, so no thread can interfere with another's view of the object. Java's final keyword supports immutability: a field declared final cannot be reassigned after construction, and the JMM provides a special visibility guarantee that all final fields initialized in the constructor are visible to any thread that subsequently reads the object reference, provided the reference is safely published. "Safe publication" means the reference is made available to other threads via a mechanism that establishes a happens-before edge — through a volatile field, a synchronized block, a final field, or a concurrent collection. Publishing a reference through a plain non-volatile field does not guarantee that other threads see the fully constructed object.

Thread confinement means a thread owns an object and no other thread can access it. Stack confinement is the strongest form: a local variable is allocated on the thread's stack and cannot be accessed by any other thread unless explicitly shared. This is why method-local state is the safest state. ThreadLocal provides a per-thread copy of an object: each thread gets its own instance, visible only to that thread. This is used to confine per-request context (user session, database connection, transaction context) in frameworks like Spring, where the request-handling thread carries its own copy of these objects. Ad-hoc confinement, where the programmer maintains the invariant that an object is never shared, is fragile — it depends on discipline rather than structure and is easily broken by future refactoring.

Stateless services hold no per-request or per-user mutable instance state. A class whose fields are all final, immutable, or injected infrastructure (also stateless) has nothing to share per request and therefore has no thread-safety concern. This is why Spring's recommendation to use singleton-scoped beans assumes statelessness — the same instance is shared across all threads, so instance state would be shared mutable state.

Atomic variables use hardware CAS (compare-and-swap) instructions to provide atomic operations on single variables without blocking. AtomicInteger, AtomicLong, and AtomicReference are the primary types. CAS is optimistic: it reads the current value, computes the desired new value, then attempts to update the field atomically only if the current value still matches what was read. If another thread has changed the value since the read, the CAS fails and the caller retries. This is a spin-retry loop, which is efficient under low contention (rare failures, rare retries) but degrades under high contention (many threads retrying continuously).

## Why It Matters

Thread-safe design at the architectural level determines whether a service can scale to high concurrency. A service that uses fine-grained synchronization on every operation can be correct but contention-limited: throughput flattens as threads queue for locks. A service that uses immutability and statelessness scales linearly with thread count because there is nothing to contend over. The difference is visible at hundreds of concurrent users — well within the range of a production web service. Getting thread safety right in design, not as an afterthought, is the leverage point that determines whether a service scales gracefully or requires increasingly aggressive tuning.

## Worked Examples

**Example 1: Immutable Value Objects**
A configuration object is built at startup and read thousands of times per second by request-handling threads. If the configuration object is mutable (fields can be updated), every read must synchronize to prevent seeing a partially updated state. If it is immutable — all fields final, initialized in the constructor, reference published through a volatile or final field — reads require no synchronization at all. The immutable object can be passed freely across threads, cached, and read concurrently without any coordination overhead. This is the pattern behind Java's String and all record types.

**Example 2: ThreadLocal for Per-Request Context**
A request-handling framework needs to carry a correlation ID and user context through every layer of the call stack without passing them as parameters. ThreadLocal solves this: at the start of request handling, the framework sets `requestContext.set(context)` on a ThreadLocal; any layer of the stack can call `requestContext.get()` to retrieve it. Each thread has its own copy; there is no sharing and no synchronization. The critical operational requirement is clearing the ThreadLocal at the end of each request: in a thread pool, threads are reused, and stale ThreadLocal values from a previous request will be visible to the next request if not cleared. This is a common source of data leakage bugs in frameworks.

**Example 3: Lock-Free Counter with AtomicLong**
A metrics system needs a thread-safe counter that hundreds of threads increment simultaneously. A synchronized counter serializes all increments, creating a bottleneck. An AtomicLong counter uses CAS to increment without locking: each thread reads the current value, adds 1, and attempts a CAS. Under moderate load, most CAS operations succeed on the first attempt. Under extreme contention, the Java 8 LongAdder class improves on AtomicLong by maintaining a set of cells that are summed on read — this distributes the write contention across multiple memory locations, dramatically reducing CAS failure rates at the cost of slightly stale reads.

## Common Mistakes

**Mistake 1: Publishing mutable objects through non-volatile fields.** Creating an immutable-looking object but sharing its reference through a plain field means the reader may see a partially constructed object (before the constructor's writes become visible). Safe publication requires a happens-before edge.

**Mistake 2: Forgetting to clear ThreadLocal in a thread pool.** Thread pools reuse threads. A ThreadLocal value set for request N is visible to request N+1 if not cleared. This is a data leakage and correctness bug that manifests as "wrong user context" in multi-tenant systems.

**Mistake 3: Making a class thread-safe when it should be confined.** Not every class needs to be thread-safe. A domain object that is created, modified, and discarded within a single request never needs synchronization. Adding synchronization to every class "just in case" adds overhead and complexity without benefit.

**Mistake 4: Relying on the fact that primitive reads are atomic.** Reading an int is atomic in Java (the JVM will not return a torn value for int). However, atomic reads do not imply visibility. Without a happens-before relationship, a thread may read the previously cached value of an int indefinitely, even if another thread has written a new value.

**Mistake 5: Using AtomicReference for compound state updates.** Updating two related fields atomically (e.g., a balance and a transaction count) cannot be done with two separate AtomicReferences — each CAS is independent, and the pair can be observed in a half-updated state. Compound state that must be updated atomically requires either synchronization or replacing both fields with a single immutable value object that is updated atomically through a single AtomicReference.

## Mental Model

Thread-safe design is like designing a kitchen for multiple chefs. The worst approach: a single large prep station that everyone must take turns at (coarse-grained synchronization) — correct but slow. A better approach: each chef has their own prep station (thread confinement) — no coordination needed. The best approach: as many tasks as possible are done with pre-packaged ingredients that cannot be modified (immutability) — the shared kitchen space is used only for tasks that genuinely require coordination. CAS is the kitchen's central spice rack: each chef grabs a spice, uses it, and puts it back — most of the time it is available immediately; occasionally they wait and retry. The goal is to design the kitchen so the shared rack is consulted as rarely as possible.

## Mini Summary

- ✔ Thread-safe design minimizes shared mutable state by design; synchronization manages what remains.
- ✔ Immutability (all-final, safely published) eliminates the mutation dimension; reads require no synchronization.
- ✔ Thread confinement (stack-local, ThreadLocal, ad-hoc) eliminates the sharing dimension; only one thread accesses the state.
- ✔ Stateless services (no per-request mutable instance fields) are inherently thread-safe and scale linearly.
- ✔ CAS/AtomicXxx provides non-blocking atomic operations; efficient under low contention, degrades under high contention.
- ✔ The ABA problem requires AtomicStampedReference when value identity (not just equality) matters for correctness.

# Guided Practice Quest

1. A team is designing a shared counter for tracking active sessions across a high-throughput web service. Compare three implementations: a synchronized long field, an AtomicLong, and a LongAdder. For each, describe the consistency model for reads, the write performance under high contention, and the appropriate use case.

2. An engineer proposes using ThreadLocal to cache a non-thread-safe SimpleDateFormat instance per thread, rather than creating a new instance per call. Evaluate this design: what problem does it solve, what operational risk does it introduce, and what modern alternative eliminates both concerns?

3. A domain event object is created, passed to an event bus, and potentially read by multiple event handler threads. What properties must the event object have to be safely shared without synchronization, and what specific publication mechanism ensures it is safely published?

4. Describe a scenario where using an immutable value object causes more overhead than using a mutable object with synchronization, and explain under what conditions you would prefer each approach.

# Solo Practice Quest

You are designing a caching layer for an expensive user profile lookup. The cache is a shared resource in a stateless web service with high concurrency. The cache must: never return corrupted data, handle concurrent population of the same key without performing the expensive lookup more than once (or accepting one duplicate lookup as an acceptable tradeoff), and support expiry.

Design the thread-safety model for this cache. Address: the data structure choice, the atomic operations required, whether you accept optimistic or pessimistic concurrency for cache population, how you handle the cache population race (two threads both finding a miss and both triggering a lookup), and what you would do differently for a distributed multi-node deployment.

# Integration

**Mathematics — Linearizability and Formal Correctness:** Atomic operations are formally characterized by linearizability: a concurrent execution is linearizable if it is equivalent to some sequential execution where each operation takes effect at a single point in time between its invocation and its return. CAS is linearizable because the comparison and swap occur atomically — the operation appears instantaneous to all observers. This formal property is the mathematical foundation that allows engineers to reason about lock-free algorithms with the same confidence as sequential code. The ABA problem violates linearizability assumptions when the algorithm incorrectly concludes that a value is unchanged based on equality — showing that formal correctness requires thinking about identity (which version of A), not just equality (the value A).

**Philosophy — Ownership and Responsibility:** Thread confinement is a form of ownership: one thread "owns" an object and is exclusively responsible for it. Ownership models appear throughout philosophy and law as mechanisms for resolving conflicts over shared resources. The Tragedy of the Commons argues that unowned shared resources are overused and degraded; thread confinement is the programmer's equivalent of private ownership — removing the object from the commons entirely. Immutability, by contrast, is the equivalent of a public good that cannot be depleted: a read-only resource can be shared without conflict because no consumption diminishes it. The parallel suggests a design philosophy: make things immutable where possible, confine them where not, and synchronize only where confinement and immutability are genuinely impossible.

**Question for reflection:** Given that statelessness is the most scalable thread-safety strategy, what architectural patterns (outside of code structure) support stateless service design, and where does state necessarily live in a stateless service architecture?

# Lore Conclusion

The Academy's capstone lesson on thread-safe design is a question that distinguishes senior from junior engineers: "What state does this class need, and who should own it?" Junior engineers ask "is this synchronized correctly?" Senior engineers ask "should this state exist here at all?" The deepest thread safety is designed in, not bolted on. Immutability, confinement, and statelessness are not performance optimizations — they are correctness decisions that happen to scale well. The engineer who designs shared mutable state as a last resort, after exhausting structural alternatives, writes concurrent code that is both correct and fast for reasons that are visible in the design, not hidden in the locking protocol.
