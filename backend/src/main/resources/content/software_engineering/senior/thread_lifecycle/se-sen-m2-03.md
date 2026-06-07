---
id: se-sen-m2-03
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m2
moduleTitle: "Module 2: Concurrency & Parallelism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: thread_lifecycle
topicTitle: "Thread Lifecycle"
topicSortOrder: 3
lesson: thread_lifecycle
title: "Thread Lifecycle"
sortOrder: 3
difficulty: 3
estimatedMinutes: 35
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Accurately describes all six Java thread states and their transitions
    - Explains the difference between BLOCKED (waiting for a monitor) and WAITING (waiting for notification)
    - Articulates thread interruption and how it interacts with blocking operations
    - Describes daemon vs non-daemon threads and their JVM lifecycle implications
    - Identifies the resource implications of threads in various states
  keywords: [NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED, interrupt, daemon, monitor, notify, park, unpark, thread pool, lifecycle]
  modelAnswer: |
    Java threads progress through six states defined in the Thread.State enum. Understanding these states is essential for debugging concurrency issues, interpreting thread dumps, and designing correct concurrent systems.

    NEW: The thread object has been created but start() has not been called. The thread exists as an object in the heap but has no OS thread associated with it.

    RUNNABLE: The thread has been started. It may be actively executing on a CPU core or it may be waiting for the OS scheduler to assign it a core — the JVM does not distinguish between these sub-states. From Java's perspective, both are RUNNABLE. This is an important nuance: a RUNNABLE thread is not necessarily executing at this instant.

    BLOCKED: The thread is waiting to acquire a monitor lock (synchronized block or method) that another thread currently holds. It is consuming no CPU but is occupying a thread slot. It will transition to RUNNABLE automatically when the lock becomes available.

    WAITING: The thread is waiting indefinitely for another thread to signal it, via Object.wait() (waiting for notify/notifyAll), Thread.join() (waiting for another thread to terminate), or LockSupport.park() (waiting for an unpark). The key distinction from BLOCKED: WAITING threads release any locks they hold and wait for an explicit signal rather than a lock release.

    TIMED_WAITING: Like WAITING but with a timeout — Thread.sleep(n), Object.wait(n), Thread.join(n), or LockSupport.parkNanos(n). The thread will return to RUNNABLE after the timeout expires even if not signalled.

    TERMINATED: The thread's run() method has completed or an uncaught exception propagated out. The Thread object still exists in the heap but the OS thread has been released. Terminated threads cannot be restarted.

    Thread interruption is a cooperative mechanism: calling interrupt() on a thread sets its interrupted flag. If the thread is in WAITING or TIMED_WAITING (sleeping, waiting), an InterruptedException is thrown immediately. If the thread is RUNNABLE, the interrupted flag is set and the thread must check it explicitly. Interruption is not forcible — a thread that never checks its flag or catches and swallows InterruptedException cannot be stopped this way.

    Daemon threads are threads that do not prevent JVM shutdown. When all non-daemon threads have terminated, the JVM exits regardless of any remaining daemon threads. Background maintenance tasks (GC finaliser, timer threads) are typically daemon threads. Service threads that must complete their work before shutdown should be non-daemon.

    Thread pools (ExecutorService) recycle threads across many tasks, avoiding the creation/destruction overhead of one-thread-per-task. The thread state management becomes the pool's responsibility. A thread in a pool in the WAITING state is awaiting a new task from the work queue. This is the correct idle state for a thread pool worker.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A thread dump shows 200 threads in BLOCKED state, all waiting for a lock held by one thread. What does this tell you about the system's performance?"
    options:
      - "The system is performing well — BLOCKED threads use no CPU"
      - "There is a lock contention bottleneck: one thread is serialising the work of 200 others"
      - "The threads are deadlocked and will never proceed"
      - "The JVM needs more memory to run these threads"
    correctIndex: 1
    feedback: "BLOCKED threads consume no CPU but they are queued waiting for a single resource. The throughput of the system is now limited by how fast the lock-holding thread processes and releases. This is a contention bottleneck — 200 threads reduced to the throughput of 1. The fix is reducing lock granularity, using concurrent data structures, or redesigning to eliminate the shared lock."
  - type: SHORT_TEXT
    prompt: "A thread pool worker thread is in the WAITING state. A new task is submitted to the pool. What mechanism transitions the worker thread from WAITING back to RUNNABLE?"
    hint: "Think about how thread pool implementations use blocking queues and LockSupport."
  - type: FILL_BLANK
    prompt: "When all ___ threads in a JVM have terminated, the JVM shuts down even if ___ threads are still running."
    answer: "non-daemon; daemon"
    hint: "Daemon threads are background threads that do not prevent JVM exit."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the difference between a thread in BLOCKED state and a thread in WAITING state?"
    options: ["BLOCKED threads use more CPU than WAITING threads", "BLOCKED threads wait to acquire a monitor lock; WAITING threads wait for an explicit signal from another thread", "WAITING threads hold locks while waiting; BLOCKED threads do not", "They are equivalent — both indicate an inactive thread"]
    correctIndex: 1
    feedback: "BLOCKED = waiting to acquire a lock owned by another thread (automatic transition when lock released). WAITING = waiting for notify()/unpark()/join() — requires an explicit signal. WAITING threads release any held locks while waiting; BLOCKED threads are not holding a lock — they are trying to acquire one."
  - type: MULTIPLE_CHOICE
    question: "Calling Thread.interrupt() on a thread in TIMED_WAITING state (sleeping) will:"
    options: ["Immediately kill the thread", "Throw InterruptedException in the sleeping thread, terminating the sleep early", "Set the interrupted flag but have no immediate effect", "Cause the thread to become BLOCKED"]
    correctIndex: 1
    feedback: "Interrupting a sleeping (TIMED_WAITING) thread immediately throws InterruptedException inside that thread, allowing it to handle the interruption cleanly. This is the cooperative cancellation mechanism — the thread decides what to do when interrupted, rather than being forcibly killed."
retrieval:
  recall: "Draw the Java thread state machine: list all six states and the transitions between them with the method calls or events that cause each transition."
  explain: "Why is thread interruption described as 'cooperative' rather than 'preemptive'? What are the implications for writing interruptible blocking operations?"
  mistakeId:
    code: |
      try {
          Thread.sleep(10_000);
      } catch (InterruptedException e) {
          // ignore and continue
          log.debug("Sleep interrupted, continuing");
      }
    answer: "This code swallows the InterruptedException without restoring the interrupted status. The InterruptedException is thrown precisely because some external agent (another thread, a shutdown hook) has requested that this thread stop what it is doing. Catching and ignoring it breaks the cooperative cancellation protocol. The thread that called interrupt() — perhaps the thread pool's shutdown mechanism — now believes this thread was notified of interruption, but the thread continues running as if nothing happened. The correct pattern is either: (1) let the InterruptedException propagate by declaring 'throws InterruptedException' on the enclosing method, or (2) if you cannot propagate it, re-assert the interrupted status: Thread.currentThread().interrupt(). The interrupted flag must be preserved so that callers further up the stack can detect and respond to the interruption."
---

# Hook

A thread dump lands in your inbox during a production incident. There are 800 threads. Some are RUNNABLE, dozens are BLOCKED, hundreds are WAITING. Can you read this dump and identify the bottleneck in 60 seconds? That skill — reading thread state as diagnostic information — is what separates engineers who debug production incidents quickly from those who spend hours guessing. This lesson gives you the state machine.

# Lore Introduction

The Academy's concurrent systems specialists read thread dumps the way doctors read X-rays: a single snapshot that reveals everything about the system's health. Hundreds of BLOCKED threads signal a lock contention crisis. A single thread in RUNNABLE holding a lock that others need is a bottleneck made visible. Understanding the thread lifecycle is not academic — it is the clinical vocabulary that makes concurrent bugs diagnosable in minutes.

# Core Learning

## Concept Introduction

Java threads have six states defined in `Thread.State`:

**NEW → RUNNABLE → [BLOCKED | WAITING | TIMED_WAITING] → TERMINATED**

The state machine is the mental model for understanding any concurrent execution problem. Let us examine each state precisely.

**NEW**
A `Thread` object exists but `start()` has not been called. No OS thread is allocated yet. The thread has no lifecycle impact on the JVM.

**RUNNABLE**
`start()` has been called. The OS thread exists and is either actively executing or queued in the OS scheduler's run queue awaiting CPU time. The JVM does not distinguish between these sub-states — from Java's perspective, both are RUNNABLE. This means a thread can be "RUNNABLE" while waiting for its turn on the CPU.

**BLOCKED**
The thread is waiting to acquire a monitor lock (entering a `synchronized` block or method) that another thread currently holds. The thread consumes no CPU. It will atomically transition to RUNNABLE when the lock becomes available. Importantly: the thread is not waiting for any explicit signal — the lock release is the only trigger.

**WAITING**
The thread is waiting indefinitely for a signal from another thread. Causes:
- `Object.wait()` — waiting for `notify()` or `notifyAll()` on the same object
- `Thread.join()` — waiting for a target thread to terminate
- `LockSupport.park()` — waiting for `LockSupport.unpark(thread)`

WAITING threads release any monitors they hold (if using `wait()`). This avoids a common deadlock scenario where a waiting thread blocks others from acquiring the lock it is holding.

**TIMED_WAITING**
Like WAITING but with a maximum duration. Causes:
- `Thread.sleep(long)` — sleeping for a specified duration
- `Object.wait(long)` — waiting with timeout
- `Thread.join(long)` — joining with timeout
- `LockSupport.parkNanos(long)` / `parkUntil(long)` — parking with timeout

Transitions to RUNNABLE either when the timeout expires or when the appropriate signal is received.

**TERMINATED**
`run()` has completed (normally or via uncaught exception). The OS thread has been released. The `Thread` object remains in the heap but cannot be restarted. This state is visible in thread dumps only for threads that terminated very recently before the dump was taken.

## Why It Matters

Every concurrency bug — deadlock, livelock, starvation, contention — manifests as abnormal thread state distributions. A thread dump is the primary diagnostic tool for production concurrency issues. Engineers who can interpret thread states can identify contention bottlenecks (mass BLOCKED), deadlocks (circular BLOCKED chains), thread pool saturation (all RUNNABLE with queue backed up), and resource leaks (growing thread counts over time).

## Worked Examples

**Example 1: Reading a Thread Dump**

A partial thread dump:
```
Thread-47 [BLOCKED] on java.lang.Object@1a2b3c4d owned by Thread-1
Thread-48 [BLOCKED] on java.lang.Object@1a2b3c4d owned by Thread-1
Thread-49 [BLOCKED] on java.lang.Object@1a2b3c4d owned by Thread-1
...
Thread-1 [RUNNABLE] synchronized void processOrder(...)
```

Analysis: 200+ threads are blocked waiting for the same monitor `1a2b3c4d`. Thread-1 currently holds this monitor executing `processOrder()`. If `processOrder()` is slow (e.g., making a database call while holding the lock), the entire thread pool is serialised through this single lock. The solution is to narrow the lock scope (acquire only when actually modifying shared state, not during I/O), or use concurrent data structures that do not require coarse-grained locks.

**Example 2: Thread Interruption Protocol**

The correct pattern for interruptible blocking code:

```
public void runWithInterruptSupport() {
    while (!Thread.currentThread().isInterrupted()) {
        try {
            WorkItem item = queue.take(); // WAITING — interruptible
            process(item);
        } catch (InterruptedException e) {
            // Restore interrupted status — do NOT swallow
            Thread.currentThread().interrupt();
            break; // or re-throw
        }
    }
}
```

The `queue.take()` operation parks the thread in WAITING until an item is available. If `interrupt()` is called while waiting, `InterruptedException` is thrown. The correct response is to restore the interrupted flag (so callers can detect it) and exit or re-throw — not to log and continue.

**Example 3: Daemon vs Non-Daemon Threads**

```
// Non-daemon (default) — JVM waits for this to complete
Thread orderProcessor = new Thread(this::processOrders);
orderProcessor.start();

// Daemon — JVM exits without waiting
Thread cacheRefresher = new Thread(this::refreshCache);
cacheRefresher.setDaemon(true);
cacheRefresher.start();
```

When the main thread completes and all non-daemon threads finish, the JVM exits. The daemon `cacheRefresher` is abruptly terminated without executing any cleanup. This is correct for a cache refresh task (data is not lost, it is just not refreshed). It would be catastrophically wrong for an order processor (in-progress orders would be lost without completion).

## Common Mistakes

- **Swallowing InterruptedException.** Catching `InterruptedException` and doing nothing breaks the cooperative cancellation protocol. Always either propagate it or restore the flag with `Thread.currentThread().interrupt()`.
- **Holding locks during I/O.** Acquiring a `synchronized` lock before making a database call or network request forces every other thread that needs the lock to wait during the I/O latency. This is the most common cause of mass BLOCKED states in thread dumps.
- **Confusing BLOCKED and WAITING.** BLOCKED = waiting for a lock (automatic resolution). WAITING = waiting for an explicit signal (requires another thread to call notify/unpark). A thread that is WAITING but nobody will ever signal it is effectively leaked.
- **Using non-daemon threads for background tasks.** A background thread that is non-daemon will prevent JVM shutdown even if every other part of the application has finished, causing the JVM to hang.
- **Not monitoring thread counts over time.** A slowly increasing thread count (threads are created but never terminated) is a thread leak. Without monitoring, this is invisible until the JVM runs out of resources.

## Mental Model

Think of the thread state machine as a metro system. NEW threads are passengers who have not yet boarded. RUNNABLE threads are on the train (or waiting on the platform for the next train — the OS scheduler is the train). BLOCKED threads are queued at the turnstile — they cannot board until someone in front of them clears. WAITING threads are sitting in a waiting room until they are called. TIMED_WAITING threads are waiting with an alarm clock — they will leave on their own if nobody calls them. TERMINATED threads have reached their destination and left the system.

## Mini Summary

- ✔ Java threads cycle through six states: NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED
- ✔ BLOCKED = waiting for a lock; WAITING = waiting for a signal; TIMED_WAITING = waiting with a timeout
- ✔ Thread interruption is cooperative — the interrupted thread must check the flag or respond to InterruptedException
- ✔ Never swallow InterruptedException without restoring the interrupted flag
- ✔ Daemon threads do not prevent JVM shutdown; non-daemon threads do — choose deliberately
- ✔ Thread dumps map directly to the state machine: mass BLOCKED signals lock contention; growing thread counts signal thread leaks

# Guided Practice Quest

Work through the guided steps. For the thread dump interpretation question, reason about the causal chain: what does this state distribution tell you about the root cause?

# Solo Practice Quest

You receive a thread dump from a production Java web application that is experiencing severe performance degradation. The dump shows:

- 450 threads in BLOCKED state, all waiting for `java.lang.Object@deadbeef`
- 1 thread in RUNNABLE holding `java.lang.Object@deadbeef`, stack trace showing it is inside `UserSessionService.validateSession()`
- 10 threads in TIMED_WAITING (sleeping 100ms)
- 5 threads in WAITING (parked)

Write a diagnostic analysis covering:
1. What is the root cause of the performance degradation?
2. What is `validateSession()` most likely doing that causes it to hold the lock so long?
3. What three architectural changes would fix the root cause?
4. How would you verify the fix is working without taking another production incident?

# Integration

**Mathematics connection:** Thread state transitions form a finite-state machine (FSM) — a mathematical model used widely in theoretical computer science. An FSM is formally defined as a set of states, a set of inputs (transition triggers), and a transition function mapping (state, input) → state. Java's Thread.State is a direct implementation of an FSM with defined transitions. Modelling concurrent systems as FSMs allows formal verification of properties like "can this thread reach a terminal state from every reachable state?" and "is there a sequence of events that leads to a state where no thread can make progress (deadlock)?" Finite-state analysis is how Java's concurrency specification was formally verified.

**Philosophy connection:** The cooperative nature of thread interruption reflects a deeper philosophical point about autonomy and coercion. The JVM provides a cooperative cancellation mechanism precisely because forcibly killing a thread risks leaving shared state in an inconsistent state — corrupting the heap that all threads share. This mirrors Kant's categorical imperative at the system level: treating threads as autonomous agents that must consent to termination produces a safer system than treating them as objects to be destroyed at will. The philosophical principle (respect for autonomy) produces better systems than the alternative (force). Where else in system design does respecting autonomy produce better outcomes than coercion?

# Lore Conclusion

Thread dumps are the stethoscope of the concurrent systems engineer. When a production system is drowning in lock contention or silently leaking threads, the thread dump shows you the exact state of every thread at the moment of crisis. The engineer who can read this snapshot and identify the root cause in minutes — not hours — is the one who resolves incidents quickly. The thread state machine is not abstract theory; it is the lens through which you diagnose the hardest concurrency problems you will ever face.
