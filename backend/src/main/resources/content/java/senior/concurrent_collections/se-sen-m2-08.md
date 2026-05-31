---
id: se-sen-m2-08
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m2
moduleTitle: "Module 2: Concurrency & Parallelism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: concurrent_collections
topicTitle: Concurrent Collections
topicSortOrder: 8
lesson: concurrent_collections
title: "Concurrent Collections"
sortOrder: 8
difficulty: 4
estimatedMinutes: 30
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
    - Correctly explains ConcurrentHashMap's segment/striped locking and why it outperforms synchronizedMap under concurrency
    - Articulates when CopyOnWriteArrayList is appropriate (read-heavy, infrequent writes) and its memory/GC overhead
    - Explains BlockingQueue's role in producer-consumer coordination without explicit synchronization
    - Distinguishes weakly consistent iterators from fail-fast iterators and explains the implications for traversal logic
    - Identifies the compound operation gap: even concurrent collections do not make compound check-then-act operations atomic
  keywords: [ConcurrentHashMap, segment locking, striped locking, CopyOnWriteArrayList, BlockingQueue, weakly consistent iterator, fail-fast iterator, synchronizedMap, compound operation, putIfAbsent, producer-consumer, LinkedBlockingQueue, ArrayBlockingQueue]
  modelAnswer: |
    Java's concurrent collections are purpose-built for high-concurrency scenarios where Collections.synchronizedXxx wrappers are insufficient. The fundamental difference is granularity: synchronized wrappers hold a single lock for every operation, serializing all access; concurrent collections use internal concurrency mechanisms that allow multiple threads to proceed simultaneously.

    ConcurrentHashMap achieves concurrency through striped locking (Java 7) or a node-level CAS and synchronized approach (Java 8+). In Java 8, the map is segmented into bins; reads typically proceed without locking (using volatile reads of bin heads), and writes lock only the bin being modified. This allows O(N/bins) effective concurrency for writes and fully concurrent reads, compared to the single-lock serialization of synchronizedMap. The tradeoff is that size() and other aggregate operations are approximate: they reflect a weakly consistent snapshot rather than a precise moment-in-time count.

    CopyOnWriteArrayList creates a fresh copy of the underlying array on every write. Reads never need synchronization because the array reference they observe is immutable for the lifetime of their traversal. Writers pay O(N) copy cost. This makes it appropriate for scenarios with many reads and very infrequent writes: event listener lists, service registries, small configuration sets. Using it for write-heavy workloads creates continuous GC pressure from discarded copies.

    BlockingQueue (LinkedBlockingQueue, ArrayBlockingQueue) implements the producer-consumer pattern with internal locking and condition variables. Producers call put() which blocks when the queue is full; consumers call take() which blocks when empty. This eliminates the explicit wait/notify coordination that a hand-rolled implementation requires, and the blocking is implemented efficiently through the OS rather than spin-waiting.

    The critical limitation all concurrent collections share: they make individual operations atomic, not compound operations. Checking whether a key exists then inserting if absent is still a race — use putIfAbsent() or computeIfAbsent() to make the compound operation atomic. This is the check-then-act problem, which no collection can solve for the caller — the caller must use the atomic compound methods provided by the collection.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "Why does ConcurrentHashMap outperform Collections.synchronizedMap(new HashMap<>()) under high read concurrency?"
    options:
      - "ConcurrentHashMap uses faster hashing algorithms"
      - "synchronizedMap holds a single lock for every read and write; ConcurrentHashMap allows concurrent reads without locking and limits write locks to individual bins"
      - "ConcurrentHashMap pre-allocates more memory to avoid resizing"
      - "synchronizedMap is deprecated and uses slower legacy code paths"
    correctIndex: 1
  - type: SHORT_TEXT
    prompt: "A team uses CopyOnWriteArrayList for a list that is updated frequently (hundreds of times per second) and read occasionally. Describe the performance problem this creates and what data structure would be more appropriate."
  - type: MULTIPLE_CHOICE
    prompt: "A ConcurrentHashMap is used to implement a cache with lazy population: if absent, compute and store. The team uses: if (!map.containsKey(k)) map.put(k, compute(k)). What is wrong with this?"
    options:
      - "ConcurrentHashMap does not support null values"
      - "containsKey and put are individually atomic but not atomically composed — another thread can insert between the check and the put. Use computeIfAbsent() instead."
      - "compute() may throw an exception that the map cannot handle"
      - "Nothing — ConcurrentHashMap makes this pattern safe"
    correctIndex: 1
  - type: SHORT_TEXT
    prompt: "Explain what a weakly consistent iterator means in the context of ConcurrentHashMap, and describe a scenario where this behavior could cause a correctness issue in application logic."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    prompt: "When is CopyOnWriteArrayList the appropriate choice over ConcurrentLinkedQueue or synchronizedList?"
    options:
      - "When the list is very large and memory is not a concern"
      - "When reads vastly outnumber writes and snapshot-consistent iteration (seeing exactly the state at iterator creation) is required"
      - "When the list must support fast removal from the middle"
      - "When writes happen more frequently than reads"
    correctIndex: 1
  - type: MULTIPLE_CHOICE
    prompt: "What property of BlockingQueue makes it particularly well-suited for producer-consumer pipelines?"
    options:
      - "It is faster than all other queue implementations"
      - "It provides blocking put() and take() operations that automatically handle the coordination between producers and consumers without explicit synchronization code"
      - "It supports distributed coordination across multiple JVMs"
      - "It uses lock-free algorithms for all operations"
    correctIndex: 1
retrieval:
  recall: "Name four concurrent collection types from java.util.concurrent and their primary use cases."
  explain: "Why does using a ConcurrentHashMap not make all operations on it race-free?"
  mistakeId:
    code: |
      ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();

      // Thread 1 and Thread 2 both execute:
      Integer current = counts.get(key);
      if (current == null) {
          counts.put(key, 1);
      } else {
          counts.put(key, current + 1);
      }
    answer: "This has a check-then-act race and a lost-update race. Two threads can both read null and both insert 1, losing one increment. Two threads can both read value N, both compute N+1, and both write N+1, losing one increment. The correct fix uses counts.merge(key, 1, Integer::sum) or counts.compute(key, (k, v) -> v == null ? 1 : v + 1), which execute atomically on the key."
---

# Hook

You wrapped your HashMap in Collections.synchronizedMap. Your tests pass. You deploy. Under load, your service becomes five times slower than the sequential version — every read and write serializes through a single lock. You switch to ConcurrentHashMap. Performance recovers. But two weeks later, you discover that your "check if absent, then insert" logic still has a race — because ConcurrentHashMap makes individual operations atomic, not your compound logic. Concurrent collections are not magic thread-safety wrappers. They are precisely scoped tools with specific atomicity guarantees and specific gaps.

# Lore Introduction

The java.util.concurrent package was the result of years of academic and industry research, crystallized by Doug Lea's work and incorporated into Java 5. The concurrent collections it provides are not simply synchronized versions of the standard collections — they are purpose-built data structures with internal concurrency mechanisms tuned for specific access patterns. Senior engineers who treat them as drop-in replacements for synchronized collections misunderstand both their capabilities and their limitations. Understanding when to use each, and what each guarantees at its boundaries, is a prerequisite for building correct high-throughput systems.

# Core Learning

## Concept Introduction

Java's standard synchronized wrappers (Collections.synchronizedList, synchronizedMap) acquire a single object-level lock for every operation. This serializes all reads and all writes through a single bottleneck, which is correct but limits concurrency to one thread at a time. For low-concurrency scenarios this is acceptable; for high-concurrency production services, it creates a lock contention bottleneck that limits throughput as thread count increases.

ConcurrentHashMap addresses this through fine-grained internal concurrency. In Java 8 and later, the map organizes its entries into bins (linked lists or tree nodes at each hash bucket). Reads typically proceed without acquiring any lock — they use volatile reads of the bin head to observe the current state. Writes lock only the specific bin being modified, not the entire map. Multiple writers operating on different bins proceed simultaneously. The result is substantially higher write throughput than a synchronized map when writes are distributed across many keys. The tradeoff: aggregate operations like size() and isEmpty() are approximate. ConcurrentHashMap maintains a distributed counter using techniques similar to LongAdder; the size is computed by summing internal cells, which may reflect a state slightly in the past relative to the actual map contents. For applications where precise size matters for correctness (not just monitoring), this must be accounted for.

CopyOnWriteArrayList maintains thread safety through copy-on-write semantics: every mutating operation (add, remove, set) creates a new copy of the underlying array with the modification applied, then atomically replaces the reference. Readers always read through a reference to an array snapshot; they never observe partial writes. Iterator creation captures the array reference at that moment; the iterator always traverses that exact snapshot, even if subsequent writes produce new copies. This makes iterators immune to ConcurrentModificationException and provides snapshot isolation for traversals. The cost is O(N) time and memory for every write. For collections that are traversed thousands of times per write (event listener lists, small configuration registries), this is an excellent tradeoff. For collections written frequently, it is a GC-pressure disaster.

BlockingQueue implementations (LinkedBlockingQueue, ArrayBlockingQueue, PriorityBlockingQueue, SynchronousQueue) provide a thread-safe queue with blocking semantics for producer-consumer coordination. LinkedBlockingQueue uses two separate locks — one for the head (consumers) and one for the tail (producers) — allowing producers and consumers to proceed simultaneously when the queue is neither full nor empty. put() blocks when the queue is full; take() blocks when empty. This replaces the explicit wait/notify loop that a hand-rolled bounded buffer would require. ArrayBlockingQueue uses a single lock but a bounded array, making it preferable when maximum memory use must be capped. SynchronousQueue has no internal capacity — a put() blocks until a take() is ready, and vice versa. It is used for direct handoff between threads with no buffering.

## Why It Matters

Concurrent collections are infrastructure that underpins the throughput of virtually every high-concurrency Java service. Incorrect collection choice — using a synchronized wrapper where a concurrent collection is needed, or using CopyOnWriteArrayList for a write-heavy workload — can be the sole cause of a service's throughput plateau. Beyond performance, the compound-operation gap is a correctness concern: engineers who treat ConcurrentHashMap as a fully thread-safe map for all purposes write check-then-act races that are no different from races on an unsynchronized HashMap. The atomic compound methods (putIfAbsent, computeIfAbsent, compute, merge) exist precisely to close this gap.

## Worked Examples

**Example 1: Replacing synchronizedMap in a High-Read Cache**
A service caches computed results in a `Collections.synchronizedMap(new HashMap<>())`. Profiling shows that the map lock accounts for 40% of request latency during peak load, despite the fact that 95% of operations are reads. Replacing with ConcurrentHashMap immediately allows concurrent reads, reducing lock contention. The remaining 5% of write operations lock only the affected bin. The only code change required is using computeIfAbsent() instead of the previous get-check-put pattern to ensure atomic population.

**Example 2: Event Listener List with CopyOnWriteArrayList**
A UI framework maintains a list of registered change listeners. New listeners are registered at startup and rarely removed thereafter; listener notification occurs on every state change, potentially thousands of times per second. CopyOnWriteArrayList is ideal: the expensive copy operation happens only at registration/removal (rare), and the hot-path notification loop traverses a stable array snapshot with no synchronization overhead. If the framework used a synchronizedList, every notification would acquire a lock and contend with registrations, and the iterator would require holding the lock for the entire traversal.

**Example 3: Work Queue for Thread Pool**
A batch processing service dispatches work items from a producer thread to multiple consumer threads. An ArrayBlockingQueue with a bounded capacity serves as the work queue. The bounded capacity provides backpressure: if consumers cannot keep up, the producer blocks on put() rather than accumulating an unbounded queue that eventually causes an OutOfMemoryError. The BlockingQueue's internal locking ensures that each work item is dispatched to exactly one consumer, with no explicit synchronization required in the producer or consumer code.

## Common Mistakes

**Mistake 1: Using compound get-check-put instead of atomic compound operations.** ConcurrentHashMap provides putIfAbsent(), computeIfAbsent(), compute(), and merge() precisely because get-check-put is a race condition. Always use these atomic methods for any compound read-modify-write operation.

**Mistake 2: Relying on size() for flow control.** ConcurrentHashMap.size() is an estimate. Using it to limit insertions ("if map.size() < limit, insert") has a race: size may return a stale value, multiple threads may all see "below limit" and all insert, exceeding the limit. Use a separate AtomicInteger counter with compareAndSet for precise capacity enforcement.

**Mistake 3: Using CopyOnWriteArrayList for write-heavy workloads.** Each write copies the entire array. In a list with 10,000 elements that is updated 100 times per second, this creates 1,000,000 element allocations per second. GC pause frequency increases sharply. ConcurrentLinkedQueue or a lock-striped list is more appropriate.

**Mistake 4: Iterating a synchronizedList without external synchronization.** A synchronizedList's individual methods are synchronized, but iteration (which involves multiple method calls) is not. The Javadoc explicitly requires external synchronization during iteration: `synchronized(list) { for (E e : list) { ... } }`.

**Mistake 5: Treating weakly consistent iteration as equivalent to snapshot isolation.** ConcurrentHashMap iterators are weakly consistent: they may or may not reflect updates made after the iterator was created. They will not throw ConcurrentModificationException, but they may see some updates and miss others. If the application requires a consistent snapshot of all entries at a moment in time (e.g., for a consistent backup), a proper snapshot must be obtained explicitly.

## Mental Model

Think of concurrent collections as specialized tools in a hardware store, each optimized for a specific job. ConcurrentHashMap is a multi-drawer filing cabinet: multiple people can open different drawers simultaneously; only one person at a time can access a single drawer. CopyOnWriteArrayList is a bulletin board: anyone can read it at any time by making a personal photocopy; adding a new notice requires printing a fresh board and swapping it out. BlockingQueue is a factory conveyor belt: producers add items at one end, consumers take from the other; the belt stops when full (blocking producers) or when empty (blocking consumers). Using a hammer (synchronized wrapper) for every job is correct but inefficient; choosing the right specialized tool for each job is what senior engineers do.

## Mini Summary

- ✔ ConcurrentHashMap uses bin-level locking for writes and volatile reads for reads, providing high concurrency without single-lock serialization.
- ✔ CopyOnWriteArrayList is optimal for read-heavy, write-rare collections; O(N) write cost makes it wrong for frequent writes.
- ✔ BlockingQueue enables producer-consumer coordination with bounded backpressure without explicit wait/notify logic.
- ✔ Weakly consistent iterators do not throw ConcurrentModificationException but may observe partial updates; synchronizedList still requires external lock for safe iteration.
- ✔ Atomic compound methods (computeIfAbsent, merge, putIfAbsent) are required to close the gap between individual operation atomicity and compound operation atomicity.
- ✔ synchronizedMap is a bottleneck under concurrency and is almost never the right choice in modern Java; ConcurrentHashMap should be the default for shared maps.

# Guided Practice Quest

1. A service uses a ConcurrentHashMap as a rate limiter, tracking request counts per user ID. The logic reads the current count, increments, and compares against the limit. Describe the race condition in this approach and propose the specific ConcurrentHashMap method that eliminates it.

2. An event bus uses a `List<EventListener>` to dispatch events. Events are dispatched frequently; listeners are registered at startup and occasionally at runtime. Compare CopyOnWriteArrayList, synchronizedList, and ConcurrentLinkedQueue as backing implementations, stating which you would choose and why.

3. A team replaces a BlockingQueue with a plain ConcurrentLinkedQueue in a producer-consumer system and uses a spin loop for the consumer: `while (queue.isEmpty()) { /* spin */ }`. Identify the problem with this approach and explain why BlockingQueue is superior for this use case.

4. What is the difference between LinkedBlockingQueue and ArrayBlockingQueue in terms of memory behavior, throughput characteristics, and when each is preferable in a production system?

# Solo Practice Quest

You are designing the session store for a web application that handles 50,000 concurrent users. Sessions are looked up on every request (very high read volume). Sessions are created at login and deleted at logout (moderate write volume). Sessions expire after 30 minutes of inactivity.

Design the concurrent data structure strategy for this store. Address: which concurrent collection(s) you would use, how you implement atomic session creation (preventing duplicate sessions for the same user), how you handle expiry without a background thread holding a lock during scan, and what the consistency tradeoffs are of your chosen approach. Acknowledge any limitations.

# Integration

**Mathematics — Concurrency and Throughput Modeling:** The throughput of a lock-contended system follows Amdahl's Law: if a fraction S of the work is serialized (held under a lock), the maximum speedup from adding more threads is 1/(S + (1-S)/N), which approaches 1/S as N grows. A synchronizedMap that serializes all operations means S approaches 1 for map-heavy workloads, capping throughput regardless of thread count. ConcurrentHashMap reduces S by a factor proportional to the number of bins — effectively O(1/bins) of operations contend at any time. This mathematical model explains why fine-grained locking is not just a performance preference but a requirement for systems where the map is on the critical path.

**Philosophy — Trust and Contracts:** Concurrent collections present an interesting question about the nature of contracts in software. The contract of ConcurrentHashMap states: individual operations are atomic; aggregate state is approximate; compound operations are not atomic unless explicitly atomic methods are used. This is a precisely scoped promise, not a blanket guarantee of safety. Engineers who use these classes without reading their contracts make assumptions that are not warranted. The philosophical lesson is that trust in a tool must be commensurate with your understanding of what the tool actually promises — not what you imagine it promises. "Thread-safe" in the Javadoc means "individual operations will not corrupt internal state," not "all programs using this class are correct."

**Question for reflection:** Concurrent collections provide safety at the data structure level but cannot prevent application-level races in compound operations. What principle of software design does this suggest about where thread safety guarantees should be expressed, and how does this relate to the concept of abstraction levels?

# Lore Conclusion

The Academy teaches that the java.util.concurrent collections are not a silver bullet — they are a set of carefully designed contracts. Using them correctly requires understanding not just what they do, but what they do not guarantee. A senior engineer who reads the Javadoc and uses computeIfAbsent instead of get-check-put has absorbed the lesson. The engineer who wraps everything in synchronizedMap "to be safe" and then switches to ConcurrentHashMap when it is slow, without understanding the compound operation gap, has only solved half the problem. Thread safety is not a property you add to a data structure — it is a property you reason about at every call site, for every compound operation, with explicit awareness of what the collection contracts do and do not cover.
