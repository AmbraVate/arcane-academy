---
id: se-sen-m2-06
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m2
moduleTitle: "Module 2: Concurrency & Parallelism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: synchronisation
topicTitle: Synchronisation
topicSortOrder: 6
lesson: synchronisation
title: "Synchronisation"
sortOrder: 6
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
    - Correctly explains happens-before as the JMM's mechanism for visibility guarantees, not just ordering
    - Distinguishes volatile (visibility + ordering, not atomicity) from synchronized (visibility + atomicity + mutual exclusion)
    - Explains monitor ownership in Java's intrinsic lock model and why reentrance is necessary
    - Articulates the specific advantages of ReentrantLock over synchronized (tryLock, timeout, fairness, condition variables)
    - Demonstrates understanding of when synchronization is insufficient (e.g., 64-bit long/double reads on 32-bit JVMs without volatile)
  keywords: [happens-before, Java Memory Model, volatile, synchronized, monitor, intrinsic lock, ReentrantLock, memory visibility, reentrance, condition variable, memory barrier, store buffer]
  modelAnswer: |
    Synchronization in Java serves two distinct purposes that are frequently conflated: mutual exclusion (ensuring only one thread executes a critical section at a time) and memory visibility (ensuring that writes made by one thread become visible to other threads). Both are required for correct concurrent programs, and addressing only one while ignoring the other produces subtly broken code.

    The Java Memory Model formalizes visibility through the happens-before relationship. If action A happens-before action B, the JMM guarantees that A's effects are visible to B. The key happens-before edges in Java are: a monitor unlock happens-before every subsequent lock of that same monitor; a volatile write happens-before every subsequent volatile read of that variable; thread start happens-before every action in the started thread; and every action in a thread happens-before the thread's join returns to another thread. Without a happens-before relationship, the JMM explicitly permits any read to return a stale value.

    The synchronized keyword establishes both mutual exclusion and a happens-before edge: a lock release happens-before the subsequent lock acquisition, flushing any pending writes to main memory and invalidating the acquiring thread's caches. This is why synchronized alone can guarantee visibility when used consistently. volatile is a lighter mechanism: it establishes a happens-before edge for each write/read pair of that specific variable without mutual exclusion. It is the correct choice for simple flags and single-variable state but insufficient for compound operations.

    ReentrantLock offers capabilities that synchronized cannot provide: tryLock with timeout (preventing indefinite blocking), fair lock ordering (preventing starvation), multiple Condition objects per lock (finer-grained wait/notify semantics than the single implicit condition of Object.wait/notify), and the ability to acquire and release in different code blocks. The cost is verbosity and the risk of failing to release the lock in a finally block. For most application code, synchronized is sufficient and less error-prone; ReentrantLock is warranted when its specific capabilities are needed.

    A subtle correctness issue: Java does not guarantee atomic reads and writes for 64-bit values (long, double) on 32-bit JVMs unless the field is declared volatile. A 64-bit read can observe the high 32 bits from one write and the low 32 bits from another — a "word tearing" issue. Modern 64-bit JVMs treat long/double as atomic in practice, but the specification only guarantees it with volatile, making volatile the correct choice for shared long/double fields regardless of the target platform.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A field is written by Thread A under synchronized(lock) and read by Thread B without any synchronization. Is the read guaranteed to see Thread A's write?"
    options:
      - "Yes, synchronized on the writer side is sufficient"
      - "No, both the writer and the reader must synchronize on the same lock to establish a happens-before edge"
      - "Yes, if the field is a reference type"
      - "It depends on the JVM implementation"
    correctIndex: 1
  - type: SHORT_TEXT
    prompt: "Explain why volatile is insufficient for implementing a thread-safe counter (i.e., one that supports concurrent increments), but sufficient for a simple shutdown flag that one thread writes and others read."
  - type: MULTIPLE_CHOICE
    prompt: "What specific capability of ReentrantLock makes it preferable to synchronized when you need to avoid indefinite blocking on lock acquisition?"
    options:
      - "ReentrantLock is faster than synchronized in all cases"
      - "tryLock(timeout) — allows the thread to attempt acquisition with a time limit and proceed with alternative logic if not acquired"
      - "ReentrantLock is reentrant, which synchronized is not"
      - "ReentrantLock automatically prevents deadlocks"
    correctIndex: 1
  - type: SHORT_TEXT
    prompt: "Describe a scenario where using Object.wait()/notify() is less appropriate than using ReentrantLock with multiple Condition objects, and explain the structural advantage."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    prompt: "What does the volatile keyword guarantee in Java?"
    options:
      - "Atomicity of compound operations on the field"
      - "Mutual exclusion when reading or writing the field"
      - "Visibility: a write to a volatile field happens-before every subsequent read of that field; no CPU/compiler reordering across the volatile access"
      - "That the field is stored in CPU registers for performance"
    correctIndex: 2
  - type: MULTIPLE_CHOICE
    prompt: "Java's intrinsic locks (synchronized) are reentrant. Why is reentrance necessary?"
    options:
      - "To improve performance by reducing lock acquisition overhead"
      - "To allow a thread that already holds a lock to call synchronized methods on the same object without deadlocking itself"
      - "To support multiple threads sharing the same lock simultaneously"
      - "To enable tryLock semantics in synchronized blocks"
    correctIndex: 1
retrieval:
  recall: "List four happens-before edges defined by the Java Memory Model."
  explain: "Why is it insufficient to synchronize only on the writing side when sharing a field between threads?"
  mistakeId:
    code: |
      private volatile boolean done = false;
      private volatile int result = 0;

      // Thread 1:
      result = compute();
      done = true;

      // Thread 2:
      if (done) {
          System.out.println(result); // Is result visible here?
      }
    answer: "This is actually correct in Java due to volatile's happens-before semantics. The volatile write to 'done' happens-before Thread 2's volatile read of 'done', and by the program order rule, 'result = compute()' happens-before the volatile write to 'done'. Therefore 'result' is visible when Thread 2 reads it. A common mistake is assuming this pattern is broken — it is valid because the volatile write/read creates the necessary happens-before chain across both variables."
---

# Hook

You added synchronized. The test passes. You ship it. Three months later, a user reports seeing stale data — a value that was clearly updated but appears as if the update never happened. You add more synchronization. The stale reads continue, intermittently. The problem is not that you forgot to synchronize — it is that you synchronized on one object and your reader uses a different object, or synchronized writes but not reads, or used volatile where you needed atomicity. Synchronization that is almost correct is not synchronization at all.

# Lore Introduction

The Academy's memory model instructor, Professor Veth, begins every advanced concurrency lecture with the same provocation: "The JVM you are writing for is not the JVM you think you are writing for." Modern JVMs — and the CPUs underneath them — reorder instructions, cache values in registers, defer writes to store buffers, and execute speculative computations that may be discarded. The synchronized keyword and the volatile modifier are not programmer conveniences — they are explicit contracts with the JVM and the CPU, telling them where they may not reorder, where they must flush, and where they must invalidate. Without these contracts, the hardware is free to make your program appear to violate causality.

# Core Learning

## Concept Introduction

Synchronization in Java addresses two distinct problems: mutual exclusion and memory visibility. Engineers who understand only mutual exclusion write code that is thread-safe in execution ordering but incorrect in memory visibility — a class of bug that is especially insidious because the code appears logically correct.

The Java Memory Model (JMM) is the specification that defines what guarantees Java makes about memory visibility in concurrent programs. The central concept is happens-before: if action A happens-before action B, the JMM guarantees that any effect of A is visible to B. Without a happens-before relationship between a write and a read of the same variable, the read is permitted to return any value the variable has ever held — including values from long before the write, or values that were never written to that variable on this execution path.

The happens-before edges defined by the JMM include: a synchronized block's exit (lock release) happens-before any subsequent entry to a synchronized block on the same lock; a volatile write happens-before every subsequent volatile read of that same variable; Thread.start() happens-before every action in the started thread; all actions in a thread happen-before Thread.join() returns to any thread joining it; and the default initialization of fields happens-before any thread accesses them.

The synchronized keyword uses the object's intrinsic lock (or monitor). Every Java object has one. When a thread enters a synchronized block or method, it acquires the monitor; when it exits, it releases it. The JMM guarantees that on lock release, all writes performed in the critical section are flushed and visible; on lock acquisition, all previously cached values are invalidated and re-read. This provides both mutual exclusion (no two threads hold the same monitor simultaneously) and a happens-before edge (release before acquisition). Critically, both the writer and the reader must synchronize on the same monitor — synchronizing only the writer does not create a happens-before edge visible to unsynchronized readers.

Reentrance is a property of Java's intrinsic locks: a thread that already holds a monitor can re-acquire it without blocking. This is essential because a synchronized method frequently calls another synchronized method on the same object; without reentrance, this would deadlock the thread against itself.

## Why It Matters

Incorrect synchronization is one of the most common causes of production data corruption in Java applications. Unlike race conditions that manifest as obvious logical errors, visibility failures can be subtle: a correctly written value that is simply not visible to the reading thread, causing it to operate on stale state. Cache invalidation, CPU store buffers, and JIT compiler optimizations all contribute to visibility failures in ways that are hardware and load-dependent. A program that appears correct on a single-core machine or under low load may exhibit visibility bugs on multi-core hardware under production load — the same pattern as race conditions, but for a different underlying reason.

## Worked Examples

**Example 1: The Visibility-Only Bug**
A service has a running flag set by one thread and checked by another: `private boolean running = true; ... while (running) { ... }`. The writer sets `running = false` to stop the service. The reader thread never stops. This is a visibility failure: the writer's update may sit in a CPU store buffer indefinitely, never propagating to the reader's cache. Making the field volatile is the minimal correct fix: a volatile write happens-before a volatile read, establishing the required visibility guarantee. Synchronization would also work but is heavier for a simple flag.

**Example 2: The Partial Synchronization Bug**
A class has a `setName(String)` method that is synchronized and a `getName()` method that is not. A thread that calls `getName()` on an unsynchronized path has no happens-before relationship with the `setName()` call, so it may observe a stale value regardless of the writer's synchronization. Both methods must synchronize on the same lock to guarantee that the writer's update is visible to the reader. This is the "consistent locking protocol" rule: a field accessed by multiple threads must be protected by the same lock on every access, not just writes.

**Example 3: ReentrantLock with Multiple Conditions**
A bounded buffer requires two conditions: not-full (for producers waiting to insert) and not-empty (for consumers waiting to take). With Object.wait()/notifyAll(), both conditions share a single wait set: notifyAll() wakes both producers and consumers, requiring them to re-check their condition and go back to wait. This is inefficient. With ReentrantLock, two Condition objects can be created: `notFull = lock.newCondition()` and `notEmpty = lock.newCondition()`. A producer signals only `notEmpty` when inserting; a consumer signals only `notFull` when removing. Only relevant threads are awakened, reducing contention and spurious wake-up handling.

## Common Mistakes

**Mistake 1: Synchronizing on different objects.** `synchronized(this)` in one method and `synchronized(lock)` (a different object) in another do not protect the same critical section. Both threads can execute simultaneously because they hold different monitors. All accesses to a shared field must use the same lock.

**Mistake 2: Using volatile for compound operations.** Volatile ensures that the value is read fresh from main memory and written directly to main memory. It does not prevent another thread from interleaving between a read and a write. `counter++` is three operations (read, increment, write) — volatile makes each one visible but does not make the compound sequence atomic.

**Mistake 3: Double-checked locking without volatile.** The classic double-checked locking pattern for lazy initialization is broken without volatile because object construction involves multiple writes (allocating memory, initializing fields, assigning the reference), which can be reordered by the JVM. Another thread can observe a non-null reference to a partially initialized object. Declaring the reference volatile prevents this reordering.

**Mistake 4: Calling wait() outside a loop.** Spurious wake-ups are explicitly permitted by the JVM specification: a thread waiting on a condition may be woken without being notified. Any use of wait() must reccheck the condition in a loop: `while (!condition) { wait(); }`.

**Mistake 5: Forgetting to release ReentrantLock in a finally block.** Unlike synchronized, which releases the lock when the block exits regardless of exceptions, ReentrantLock requires explicit `lock.unlock()`. If an exception is thrown and unlock is not in a finally block, the lock is never released, and all subsequent threads attempting to acquire it will block forever.

## Mental Model

The JMM is like a postal system with no guaranteed delivery time. When a thread writes a value, it deposits a letter in an outbox. The postal system (CPU/JVM) eventually delivers it, but there is no default guarantee about when. Synchronization is a registered mail service: when you release a lock, all your letters are immediately delivered and stamped; when you acquire a lock, you collect all delivered letters before proceeding. Volatile is a direct delivery service for a single envelope: each write is hand-delivered immediately, and each read checks the mailbox fresh. The key insight: sending registered mail on the writer's side is useless if the reader does not also use registered mail — the postal metaphor makes clear why both sides must participate in the same delivery protocol.

## Mini Summary

- ✔ Synchronization addresses two independent concerns: mutual exclusion and memory visibility; ignoring visibility produces subtly incorrect programs.
- ✔ The JMM happens-before relationship is the formal guarantee of visibility; without it, any read may return a stale value.
- ✔ Both the writer and the reader must synchronize on the same lock to establish a happens-before chain.
- ✔ Volatile provides visibility and ordering guarantees for a single variable without mutual exclusion — correct for flags, wrong for compound operations.
- ✔ ReentrantLock extends synchronized with tryLock, timeout, fairness, and multiple Condition objects, at the cost of more complex usage.
- ✔ Double-checked locking, wait() loops, and ReentrantLock unlock in finally are three synchronization patterns that require explicit correctness attention.

# Guided Practice Quest

1. A configuration object is initialized once at startup and read by many threads. The writer calls `synchronized(config) { ... }` when writing; the readers call `config.getValue()` without synchronization. Explain whether this is correct, and what minimal change makes it correct.

2. An engineer replaces all `synchronized` blocks with `volatile` fields to reduce locking overhead, claiming that volatile is faster. Evaluate this claim: when is the replacement valid, and when does it break correctness?

3. Describe the double-checked locking pattern for lazy singleton initialization. Explain specifically what goes wrong without volatile on the instance field, and why volatile fixes it by addressing JVM instruction reordering.

4. A class uses `Object.wait()` and `notifyAll()` to coordinate between producers and consumers. Identify the performance problem with this approach under high concurrency, and describe how ReentrantLock with Condition objects would improve it.

# Solo Practice Quest

You are reviewing the concurrency model for a metrics aggregation service. The service collects counts from multiple writer threads and reads aggregate totals from one reader thread on a periodic schedule. The current implementation uses a single `synchronized` method for both reads and writes, but the team complains about write contention at scale.

Propose an alternative synchronization strategy that reduces write contention while maintaining visibility correctness. Justify your choice in terms of the JMM happens-before requirements, the specific operations that must be atomic, and the acceptable consistency model for periodic reads (do they need to see every individual increment, or just a consistent snapshot at each read interval?).

# Integration

**Mathematics — Memory Consistency Models:** The Java Memory Model is a formal specification in the tradition of memory consistency models studied in computer architecture. Sequential consistency (the intuitive model most engineers assume) guarantees that operations appear to execute in some global sequential order consistent with each thread's program order. The JMM provides a weaker model — causal consistency — where only operations related by happens-before are guaranteed to be ordered. This is weaker than sequential consistency but stronger than release consistency or eventual consistency. Understanding where the JMM sits in the hierarchy of consistency models explains why some apparently obvious guarantees ("a write followed by a read of the same variable must see the write") require explicit synchronization.

**Philosophy — Epistemology under Uncertainty:** Synchronization embodies a tension in epistemology: how much do you know, and when do you know it? A thread without synchronization has a local view of memory that may diverge arbitrarily from the global view. It is operating on a subjective model of reality. Synchronization is the act of aligning the subjective and objective views — but only at specific synchronization points. Between those points, the thread is again in its own epistemic bubble. This maps onto the broader philosophical question of how shared knowledge is constructed: communication (synchronization events) is the only mechanism by which isolated agents can achieve shared understanding, and the frequency and correctness of that communication determines the accuracy of the shared model.

**Question for reflection:** The JMM permits compiler and CPU reorderings that most engineers are unaware of. Given that most Java application engineers do not reason about the JMM when writing code, what architectural and tooling strategies can a senior engineer use to ensure that the codebase is JMM-correct without requiring every contributor to be a JMM expert?

# Lore Conclusion

The Academy's deepest lesson on synchronization is this: the code is not the program. The program is what the CPU executes, which includes reorderings, cache effects, and speculative execution that are invisible in the source. Synchronized and volatile are not performance features or best practices — they are the vocabulary for communicating with the JMM, the only way to assert ordering intentions that the hardware must respect. Engineers who treat them as optional or redundant are writing code that appears to work but has no specification-backed correctness guarantee. At the senior tier, the standard is not "it works in testing" — it is "I can identify every happens-before edge that makes this correct."
