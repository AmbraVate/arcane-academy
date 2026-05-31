---
id: se-sen-m6-02
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m6
moduleTitle: "Module 6: Performance Engineering"
moduleGlyph: "🚀"
moduleSortOrder: 6
topicSlug: memory_management
topicTitle: "Memory Management"
topicSortOrder: 2
lesson: memory_management
title: "Memory Management"
sortOrder: 2
difficulty: 4
estimatedMinutes: 30
xpReward: 60
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [profiling]
integrationDomains: [mathematics, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Explains JVM heap structure (young/old generation) and how objects are promoted through generational GC"
    - "Distinguishes G1, ZGC, and Shenandoah GC algorithms and the scenarios each targets"
    - "Describes what GC pauses are, why they cause latency spikes, and how low-pause collectors mitigate them"
    - "Identifies at least two common memory leak patterns in Java (static collections, listener leaks) and how to detect them"
    - "Explains how to analyse a heap dump to find leak candidates and what tools to use"
  keywords: [heap, young generation, old generation, G1GC, ZGC, Shenandoah, GC pause, memory leak, static collection, heap dump, Eclipse MAT, object allocation, off-heap]
  modelAnswer: |
    The JVM heap is divided into generations based on the generational hypothesis: most objects die young. The young generation (Eden + two Survivor spaces) holds newly allocated objects. Minor GC runs frequently, is fast, and collects only the young generation. Objects that survive multiple minor GCs are promoted to the old generation (tenured space). Major GC (or full GC) collects the old generation and is expensive. The permanent generation (PermGen, replaced by Metaspace in Java 8) holds class metadata.

    Garbage collectors differ in how they balance throughput, latency, and footprint. G1GC (default since Java 9) divides the heap into equal-sized regions and prioritises regions with the most garbage ("Garbage First"). It targets predictable pause times (configurable via -XX:MaxGCPauseMillis) and suits general-purpose applications. ZGC (Java 15+ production-ready) is a concurrent, low-latency collector designed for sub-millisecond pause times on very large heaps (multi-terabyte). It performs most work concurrently with the application. Shenandoah (OpenJDK) similarly targets ultra-low pause times through concurrent compaction, suitable for latency-sensitive services where even G1's pauses are unacceptable.

    GC pauses stop application threads ("stop-the-world") to safely collect and compact memory. For G1, typical young generation pauses are 5-50ms; old generation collections can be hundreds of milliseconds. These pauses appear as latency spikes in p99 metrics. Low-pause collectors (ZGC, Shenandoah) reduce stop-the-world phases to sub-millisecond by doing most work concurrently, at the cost of higher CPU overhead (10-20% more CPU utilisation).

    Memory leaks in Java occur when objects remain reachable through inadvertent references, preventing GC from collecting them. Common patterns: (1) Static collections that grow without bound — a static HashMap accumulating user sessions; (2) Listener/callback leaks — objects registered as listeners on an event bus or observable, never deregistered when the subscriber is destroyed, keeping the subscriber alive as long as the publisher lives; (3) ThreadLocal leaks — ThreadLocal values not removed after request completion in thread-pool-based servers, accumulating indefinitely as threads are reused; (4) Inner class holding outer class reference — anonymous Runnable capturing an Activity or Connection prevents the outer object from being collected.

    Heap dump analysis with Eclipse Memory Analyser Tool (MAT) provides the Leak Suspects report and Dominator Tree. The Dominator Tree shows which objects retain the most memory by being the dominator in the reference graph — objects at the top of the dominator tree with unexpectedly large retained heap are the leak candidates. Histogram view shows instance counts by class; an abnormally high instance count for a business object (e.g. 500,000 UserSession instances) is a strong leak signal.
guidedSteps:
  - id: mm-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A Java service runs for hours with no traffic changes. Heap usage climbs steadily from 512MB to 2GB over 4 hours until OOM occurs. GC runs frequently but heap never drops to baseline. What is the most likely cause?
    inputConfig:
      options:
        - "A. The heap is too small — increase -Xmx"
        - "B. A memory leak: objects are being retained through live references and cannot be collected"
        - "C. GC algorithm is wrong — switch from G1 to ZGC"
        - "D. High allocation rate causing frequent minor GC"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["B"]
      rejectedFeedback: "Heap climbing steadily to OOM despite frequent GC is the signature of a memory leak. GC runs but cannot collect because objects still have live references. Increasing -Xmx only delays the OOM. Switching GC algorithm does not fix a leak. Take a heap dump and use Eclipse MAT to find the dominator."
    hint: "GC running frequently but heap not recovering is the key clue."
    reflectionPrompt: "A leak is not about allocation rate — it is about retention. Objects that should be dead are staying alive."
  - id: mm-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Objects that survive multiple minor GC cycles are promoted to the ___ generation, where collection is more expensive.
    inputConfig:
      placeholder: "generation name"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["old", "old generation", "tenured", "tenured space"]
      rejectedFeedback: "Young objects start in Eden (young generation). Survivors are moved to Survivor spaces. After enough survivors, they are promoted to the old generation (tenured space), which is collected by major GC — a much more expensive operation."
    hint: "Think about JVM generational heap structure."
    reflectionPrompt: "Reducing object promotion (keeping objects short-lived) is the main lever for reducing major GC frequency."
  - id: mm-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A web service uses a static HashMap to cache computed results. After running for several days, the heap fills and the service crashes. Explain the memory leak mechanism and describe two ways to fix it.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [static, unbounded, WeakHashMap, eviction, TTL, cache, Caffeine, bounded]
      rejectedFeedback: "A static HashMap lives for the lifetime of the classloader, never GC'd. Without eviction, it grows without bound as new entries are added. Fix 1: replace with a bounded cache like Caffeine with size or TTL eviction. Fix 2: use WeakHashMap so entries are collected when keys become unreachable (only works if keys are not held elsewhere)."
    hint: "Consider the lifetime of a static field and what controls when entries are removed."
    reflectionPrompt: "Any cache without an eviction policy is a memory leak waiting for enough load to trigger it."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which GC collector is best suited for a latency-sensitive service requiring sub-millisecond pause times on a 200GB heap?"
    options:
      - "A. Serial GC — simplest and most predictable"
      - "B. G1GC — optimised for throughput on large heaps"
      - "C. ZGC — designed for sub-millisecond pauses on very large heaps through concurrent collection"
      - "D. ParallelGC — highest throughput"
    correctIndex: 2
    feedback: "ZGC performs most collection work concurrently with the application, achieving sub-millisecond stop-the-world pauses even on multi-terabyte heaps. G1GC targets predictable pauses but they are typically 10-100ms. Serial and Parallel GC are not suitable for latency-sensitive large-heap services."
  - type: MULTIPLE_CHOICE
    question: "In Eclipse MAT's Dominator Tree, an object at the top with 800MB retained heap is shown. What does this tell you?"
    options:
      - "A. The object itself is 800MB in size"
      - "B. The object is responsible for keeping 800MB of objects alive — removing it from the reference graph would free 800MB"
      - "C. The object has been in the heap for 800 seconds"
      - "D. The object was allocated in 800 separate allocations"
    correctIndex: 1
    feedback: "Retained heap in MAT means the total memory that would be freed if this object (and all objects only reachable through it) were collected. It identifies the root of a large object graph — the leak suspect."
retrieval:
  recall: "Name three common memory leak patterns in Java and explain why each prevents garbage collection."
  explain: "Explain to a junior developer why increasing the JVM heap size (-Xmx) does not fix a memory leak and what should be done instead."
  mistakeId:
    code: |
      public class EventService {
          private static final List<EventListener> listeners = new ArrayList<>();

          public void register(EventListener listener) {
              listeners.add(listener);
          }

          // No deregister method
      }
    answer: "The static listeners list holds strong references to every registered EventListener forever. There is no deregister mechanism. Every listener registered during the application's lifetime is retained, keeping all objects they reference alive too. Over time this causes the heap to fill. Fix: add a deregister method and call it in listener lifecycle hooks, or use a WeakReference list so listeners can be GC'd when they go out of scope in the caller."
---

# Hook

Your application has been running in production for 72 hours. Memory usage was 512MB at startup. It is now 3.8GB and climbing. GC is running constantly but the heap never recovers. The paging team fires at 4AM. You have a heap dump and an hour to find the leak before the on-call engineer restarts the service and the evidence disappears. Memory management is not a junior concern — it is the silent killer of long-running production services, and the engineers who understand the JVM's memory model are the ones who diagnose it fast.

# Lore Introduction

The Academy's masters speak of the "Phantom Collection" — objects that seem like they should be dead, but are not. They wander the heap, consuming space, anchored to the living world by a single reference neither programmer nor GC can see without deliberate investigation. The JVM's garbage collector is among the most sophisticated automatic memory management systems in existence — and yet it cannot collect what it cannot prove is unreachable. Memory management at the senior level means understanding when the GC's assumptions break down, and knowing exactly how to find the reference keeping the phantom alive.

# Core Learning

## Concept Introduction

The JVM manages memory through two primary regions:

**Heap** — where all Java objects live. Divided into generations:
- *Young Generation*: Eden (new allocations) + two Survivor spaces. Minor GC is frequent and fast.
- *Old Generation*: objects promoted after surviving multiple minor GCs. Major GC is expensive.
- *Metaspace*: class metadata (replaced PermGen in Java 8). Grows dynamically by default.

**Off-Heap** — memory outside the JVM heap, allocated via `ByteBuffer.allocateDirect()` or `sun.misc.Unsafe`. Not subject to GC but must be manually managed. Used by NIO, Netty, and some caches (off-heap Caffeine, Ehcache).

**GC Algorithms:**

| Collector | Pause Model | Heap Size | Best For |
|-----------|------------|-----------|----------|
| G1GC | Low (10-200ms) | Medium (4GB-128GB) | General purpose, predictable pauses |
| ZGC | Ultra-low (<1ms) | Very large (up to TB) | Latency-sensitive, large heaps |
| Shenandoah | Ultra-low (<1ms) | Medium-large | Latency-sensitive (OpenJDK) |
| ParallelGC | High throughput, long pauses | Any | Batch jobs, throughput priority |

**Object Allocation Pressure** — allocating many short-lived objects floods Eden, triggering frequent minor GCs. High allocation rates are measured in MB/s; async-profiler's allocation profiling mode captures this.

## Why It Matters

Memory leaks in Java are often more insidious than in C/C++. In C, a leak is an allocation with no free(). In Java, a leak is an object that is logically dead but reachable through a stale reference. The GC cannot know it is logically dead — it can only know it is reachable. This means Java memory leaks are logic errors, not memory errors, and require understanding the application's object lifecycle rather than just the heap structure. Senior engineers who can read heap dumps and dominator trees diagnose leaks in hours; engineers who cannot may restart the service indefinitely.

## Worked Examples

**Example 1: Diagnosing a Listener Leak**

```java
// Event bus (singleton)
public class EventBus {
    private final List<OrderListener> listeners = new ArrayList<>();
    
    public void subscribe(OrderListener l) { listeners.add(l); }
    // No unsubscribe method!
}

// Request handler — creates a new listener per request
public class OrderHandler {
    @Autowired EventBus bus;
    
    public void handleOrder(Order order) {
        bus.subscribe(new AuditListener(order)); // Registered, never removed
        // ... handle order
    }
}
```

After 10,000 requests: 10,000 `AuditListener` instances are live in `EventBus.listeners`. Each holds an `Order` reference. Heap grows by (Order size × 10,000) per 10,000 requests. Fix: add `unsubscribe()` and call it after handling, or use `WeakReference<OrderListener>`.

**Example 2: ThreadLocal Leak in Thread Pool**

```java
// Incorrect: ThreadLocal not cleared after request
public class RequestContext {
    private static final ThreadLocal<UserContext> CTX = new ThreadLocal<>();
    
    public static void set(UserContext ctx) { CTX.set(ctx); }
    // No remove() called after request completes
}
```

In a thread pool, threads are reused across requests. `UserContext` objects accumulate in the `ThreadLocal` map for each thread. Each `UserContext` may hold heavy objects (session data, connection). Fix: always call `CTX.remove()` in a `finally` block or a servlet filter.

**Example 3: Heap Dump Analysis with Eclipse MAT**

```bash
# Capture heap dump
jcmd <pid> GC.heap_dump /tmp/heap.hprof
# Or from command line
jmap -dump:format=b,file=/tmp/heap.hprof <pid>

# Analyse with Eclipse MAT
# 1. Open heap.hprof
# 2. Run "Leak Suspects" report
# 3. Open Dominator Tree — sort by retained heap descending
# 4. Find top entries — expand to see object graph
# 5. Follow references to find the GC root holding the leak
```

A typical MAT finding: `java.util.HashMap$Entry` array with 650MB retained heap, dominated by a `static final` field in `CacheService`. Expanding shows 2.3 million `ProductEntity` instances. Root cause: unbounded product cache built at startup and never evicted.

## Common Mistakes

- **Confusing live heap with retained heap in MAT**: live heap is object size; retained heap is size freed if the object is collected. Always sort the Dominator Tree by retained heap.
- **Not closing resources in try-with-resources**: JDBC `Connection`, `ResultSet`, and `Statement` objects that are not closed hold native memory and connection pool slots, creating functional leaks.
- **Using finalizers**: finalizers delay GC and can prevent collection if the finalizer thread falls behind. Use `Cleaner` (Java 9+) or explicit close patterns instead.
- **Off-heap memory not accounted for**: direct ByteBuffers and native libraries allocate outside the heap, invisible to heap profilers. Monitor native memory with `-XX:NativeMemoryTracking=detail`.
- **Over-tuning GC without root cause analysis**: adjusting `-XX:MaxGCPauseMillis` or heap sizes masks symptoms; if the problem is a memory leak, tuning GC parameters only delays the OOM.

## Mental Model

Think of the heap as a city. Objects are buildings. References are roads. GC is the demolition crew — they can only demolish a building when no roads lead to it from the city centre (GC roots: stack variables, static fields, JNI references). A memory leak is a road that should have been removed but was not. The building (object) persists not because it is needed, but because the demolition crew cannot reach it without first demolishing a building that still has roads. Eclipse MAT's Dominator Tree finds the building with the most roads pointing through it — destroy that, and everything beyond it becomes reachable for demolition.

## Mini Summary

- ✔ JVM heap is generational: young generation (Eden + Survivors) for short-lived objects, old generation for long-lived; minor GC is fast, major GC is expensive
- ✔ G1GC suits general workloads; ZGC and Shenandoah target ultra-low-latency services with large heaps
- ✔ GC pauses stop application threads; low-pause collectors mitigate this by doing most work concurrently
- ✔ Memory leaks in Java are reachability bugs: objects logically dead but held alive by stale references
- ✔ Heap dump + Eclipse MAT Dominator Tree is the standard diagnostic path for production memory leaks

# Guided Practice Quest

Work through the guided steps above. For the short-text step, go beyond the immediate fix — consider what architectural pattern or design principle would have prevented the problem being introduced at all.

# Solo Practice Quest

A long-running Spring Boot service processes insurance claim records. After two weeks, heap usage is 4x baseline and the service is OOM-restarted nightly. You have a heap dump from just before the last OOM event.

Write a complete memory investigation plan:
1. What are the first three things you examine in Eclipse MAT and what are you looking for?
2. Name four memory leak patterns common in Spring applications and how each manifests
3. How would you confirm a listener leak vs a cache leak vs a ThreadLocal leak from the heap dump alone?
4. After identifying the root cause, what JVM flags or code changes would you apply, and how would you verify the fix in staging before deploying to production?

# Integration

**Connecting to Mathematics — Graph Theory and Reachability**

The JVM's reachability analysis is a graph traversal problem. The object reference graph is a directed graph where nodes are objects and edges are references. GC roots (stack variables, static fields, JNI global references) are the source nodes. An object is live if and only if there exists a directed path from any GC root to that node. Memory leak analysis in Eclipse MAT is therefore a graph problem: find the shortest path from a GC root to the suspicious object, then identify which edge in that path is the stale reference that should have been removed. The Dominator Tree is a derived structure: node A dominates node B if every path from a GC root to B passes through A. The dominators of a large retained-heap cluster identify the objects whose removal would free the most memory — the minimum-cut in the reference graph separating the leaked objects from the GC roots. How does your understanding of graph reachability change how you would design a deregistration API for an event bus?

**Connecting to Design — Object Lifecycle and Ownership**

Memory leak patterns in Java often reflect an ownership design failure: an object is registered with a longer-lived component, but the responsibility for deregistration is unclear or absent. The Observer pattern's canonical implementation has this vulnerability — the Subject holds strong references to all Observers, and if Observers do not explicitly deregister, they live as long as the Subject. Clean architecture addresses this through explicit lifecycle management: components declare their scope (request, session, application), and the container (Spring, Guice) manages registration and deregistration. The lesson is that memory safety is not just a runtime concern — it is a design concern. Ownership and lifetime must be explicit in the API contract: who creates the reference, and who is responsible for removing it?

# Lore Conclusion

The phantom objects in the heap are not magical — they are logical errors wearing GC's clothing. Every memory leak is a reference that outlived its purpose, a road not taken down when the building it led to was no longer needed. The senior engineer who reads a heap dump is not performing debugging wizardry — they are tracing the reference graph back to its root cause with the same methodical precision that a detective traces evidence back to its source. Profile the heap. Read the dominator tree. Find the stale reference. Cut it. The ghost will finally rest.
