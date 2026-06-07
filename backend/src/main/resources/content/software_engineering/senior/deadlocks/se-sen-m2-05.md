---
id: se-sen-m2-05
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m2
moduleTitle: "Module 2: Concurrency & Parallelism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: deadlocks
topicTitle: Deadlocks
topicSortOrder: 5
lesson: deadlocks
title: "Deadlocks"
sortOrder: 5
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
    - Correctly names all four Coffman conditions and explains why removing any one prevents deadlock
    - Distinguishes detection, prevention, and avoidance as three distinct strategies with different runtime costs
    - Explains lock ordering as a prevention technique and identifies when it is impractical
    - Articulates the tradeoffs of tryLock with timeout versus lock ordering as mitigation approaches
    - Demonstrates systems-level reasoning about livelock and starvation as related but distinct failure modes
  keywords: [Coffman conditions, mutual exclusion, hold-and-wait, no preemption, circular wait, lock ordering, tryLock, deadlock detection, livelock, starvation, resource allocation graph]
  modelAnswer: |
    A deadlock is a permanent stall in which each thread in a cycle holds a resource that another thread in the cycle requires, and no thread will release its held resource until it acquires the one it is waiting for. The outcome is complete progress cessation for the deadlocked threads — no error is thrown, no recovery occurs, the threads simply stop forever.

    Coffman's four necessary and sufficient conditions for deadlock are: mutual exclusion (at least one resource is non-shareable), hold-and-wait (a thread holds at least one resource while waiting for another), no preemption (resources cannot be forcibly taken from holding threads), and circular wait (a cycle of threads exists where each holds a resource needed by the next). All four must hold simultaneously for deadlock to occur. Preventing deadlock means ensuring at least one condition cannot hold.

    Lock ordering — assigning a global order to all locks and always acquiring them in that order — breaks the circular wait condition. If every thread acquires lock A before lock B, no cycle of A-waiting-for-B and B-waiting-for-A can form. Lock ordering is the preferred prevention strategy because it has no runtime overhead. Its weakness is that it requires knowing all locks that will ever be acquired in combination, which becomes impractical in systems with dynamic lock sets (e.g., database row-level locking where the locked rows depend on the query).

    For dynamic lock sets, tryLock with timeout is the standard mitigation. A thread attempts to acquire each lock with a bounded wait; if any acquisition times out, it releases all held locks and retries after a random backoff. This breaks the hold-and-wait condition — if the thread cannot acquire all resources, it releases what it holds. The tradeoff is the risk of livelock: two threads repeatedly timing out and retrying in sync, neither making progress but neither permanently stalled. Random backoff reduces but does not eliminate livelock probability.

    Detection rather than prevention is used in systems where the cost of prevention is higher than the cost of recovery. Database engines maintain a waits-for graph and periodically check for cycles; when a cycle is found, one transaction (the victim) is killed and its locks released. This is practical because database transactions can be rolled back cleanly. In application code, rolling back a thread's work is rarely possible, making detection less useful than prevention.

    Livelock and starvation are related failure modes that senior engineers must distinguish. Livelock threads are active but make no progress — they are continuously responding to each other. Starvation means a thread is perpetually denied access to a resource despite the resource being available, because other threads always acquire it first (often a fairness problem with unfair lock implementations).
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "Which Coffman condition does lock ordering specifically break?"
    options:
      - "Mutual exclusion"
      - "Hold-and-wait"
      - "No preemption"
      - "Circular wait"
    correctIndex: 3
  - type: SHORT_TEXT
    prompt: "Explain why using tryLock with timeout can introduce livelock, and what the standard mitigation for livelock in this context is."
  - type: MULTIPLE_CHOICE
    prompt: "A database detects a deadlock between two transactions and kills one. Which Coffman condition does this strategy attack?"
    options:
      - "Mutual exclusion — the resource becomes shared"
      - "No preemption — the database forcibly reclaims resources from the victim transaction"
      - "Hold-and-wait — the victim is forced to release before re-acquiring"
      - "Circular wait — the dependency cycle is broken by transaction ordering"
    correctIndex: 1
  - type: SHORT_TEXT
    prompt: "Describe a scenario where lock ordering is impractical as a deadlock prevention strategy and explain what alternative you would use."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    prompt: "Which of the four Coffman conditions is the most practical to break in application code?"
    options:
      - "Mutual exclusion — make all resources shareable"
      - "No preemption — forcibly take locks from threads"
      - "Circular wait — enforce a global lock acquisition order"
      - "Hold-and-wait — threads may never hold multiple locks"
    correctIndex: 2
  - type: MULTIPLE_CHOICE
    prompt: "What distinguishes a livelock from a deadlock?"
    options:
      - "Livelock involves only one thread; deadlock involves multiple"
      - "In a livelock, threads are active and responding to each other but making no progress; in a deadlock, threads are permanently blocked"
      - "Livelock is always caused by improper use of tryLock; deadlock is caused by synchronized"
      - "They are synonyms with different names in different JVM versions"
    correctIndex: 1
retrieval:
  recall: "State the four Coffman conditions for deadlock."
  explain: "Why does lock ordering prevent deadlock, and under what circumstances does it become impractical?"
  mistakeId:
    code: |
      // Two bank accounts, transfer method
      synchronized(fromAccount) {
          synchronized(toAccount) {
              fromAccount.debit(amount);
              toAccount.credit(amount);
          }
      }
      // Thread 1: transfer(A, B, 100)
      // Thread 2: transfer(B, A, 200)
    answer: "This is a classic deadlock. Thread 1 holds the lock on A and waits for B; Thread 2 holds the lock on B and waits for A — circular wait with hold-and-wait. The fix is lock ordering: always acquire the lock on the account with the lower ID (or hash) first, regardless of which is 'from' and which is 'to'. This breaks circular wait."
---

# Hook

Your service stops responding at 2 AM. No exceptions. No errors. No CPU spike. Just silence. The threads are alive — you can see them in jstack — but they are waiting. Each one is waiting for a lock held by another, which is waiting for a lock held by another, forming a perfect ring of mutual dependency. No thread will ever proceed. No timeout will fire. The JVM will not detect this. Your users will not get a 500 error — they will get infinite loading spinners until they give up. This is a deadlock, and unlike most failures, it requires no bug in the code's logic — only a structural property of how locks are acquired.

# Lore Introduction

Senior architect Brennan has a single slide he shows to every engineer joining the concurrency team: a directed graph with four nodes, each pointing to the next in a circle, labeled with thread names. "This," he says, "is the simplest way to destroy a production system with perfectly written code." The four conditions for deadlock have been known since 1971, when E.G. Coffman formally characterized them. Fifty years later, engineers still create them — because the conditions are local (each lock acquisition looks correct in isolation) and the failure is global (the cycle is only visible across all threads simultaneously).

# Core Learning

## Concept Introduction

A deadlock is a state in which a set of threads are each waiting for a resource held by another thread in the set, and no thread will release its resource until it acquires the one it is waiting for. The system reaches a fixed point of zero progress. No exception is thrown, no watchdog fires by default, and the threads are technically alive — they are just permanently blocked.

Coffman's four necessary and sufficient conditions describe exactly what must hold for a deadlock to be possible:

**Mutual exclusion:** At least one resource must be non-shareable — only one thread may hold it at a time. This is inherent in the concept of a mutex and cannot generally be eliminated without changing the nature of the protected resource.

**Hold-and-wait:** A thread holds at least one resource while waiting to acquire another. If threads could only acquire all required resources at once (or had to release everything before requesting more), this condition would not hold.

**No preemption:** Resources cannot be forcibly taken from the threads holding them. Locks in Java are not preemptible; a thread holds its lock until it explicitly releases it.

**Circular wait:** A cycle exists in the resource allocation graph: Thread A waits for a resource held by Thread B, which waits for a resource held by Thread C, which waits for a resource held by Thread A (or any such cycle). This is the structural condition that creates the permanent stall.

All four must hold simultaneously. Preventing deadlock means ensuring at least one cannot hold.

## Why It Matters

Deadlocks are among the most operationally severe failure modes because they are silent, permanent, and invisible to the application's own error handling. Unlike exceptions, which propagate and can be caught, or timeouts, which surface to callers, deadlocked threads simply stop. In a thread pool, deadlocked threads consume slots without doing work, eventually exhausting the pool and starving new requests. In services without health-check liveness probes, a deadlock can take down a service completely with no alerting. The cost of a deadlock in a payment processor or booking system during peak load is severe and immediate. The diagnostic cost is also high: reproducing a deadlock requires recreating the exact timing that caused the cycle, which is often specific to load characteristics that do not appear in staging environments.

## Worked Examples

**Example 1: The Classic Transfer Deadlock**
A banking service has a transfer method that acquires a lock on the source account, then a lock on the destination account. Thread 1 is transferring from account A to account B; Thread 2 is simultaneously transferring from B to A. Thread 1 acquires A's lock; Thread 2 acquires B's lock. Now Thread 1 waits for B's lock, held by Thread 2. Thread 2 waits for A's lock, held by Thread 1. Both wait forever. The fix is lock ordering: always acquire the lock on the account with the lower canonical identifier (ID, hash code, or any consistent ordering) first. If every thread acquires A before B when both are needed, no cycle can form. This breaks circular wait.

**Example 2: Dynamic Lock Sets in ORM Frameworks**
An application uses Hibernate with row-level locking. Two transactions each lock a row in table Orders and a row in table Items, but different rows in different orders. Transaction 1 locks Orders row 17 and waits for Items row 42. Transaction 2 locks Items row 42 and waits for Orders row 17. Lock ordering by table (always Orders before Items) would help, but applying a global ordering across all possible row combinations is impractical. Databases solve this with deadlock detection: they maintain a waits-for graph, detect cycles, choose a victim transaction, and roll it back. The application must be prepared to retry the rolled-back transaction. This is detection and recovery rather than prevention.

**Example 3: Lock Inversion via Callbacks**
A framework acquires lock L1 and invokes a registered callback. The callback, written by application code, acquires lock L2. Separately, application code acquires L2 and calls a framework method that acquires L1. The acquisition order is L1→L2 via the callback path and L2→L1 via the direct path — a classic lock inversion. The deadlock is difficult to spot because the lock acquisition happens across framework and application boundaries. This is why frameworks that invoke callbacks while holding locks must document their locking behavior precisely, and why acquiring application-level locks inside framework callbacks is a known anti-pattern.

## Common Mistakes

**Mistake 1: Adding more locks to fix contention.** Engineers sometimes add fine-grained locks to reduce contention, inadvertently increasing the number of locks that must be acquired together, which increases deadlock potential. More locks with more combinations require stricter ordering discipline.

**Mistake 2: Relying on code review alone to detect deadlock potential.** Deadlock cycles are global properties of a program's lock acquisition graph. No single method, viewed in isolation, looks incorrect. Detection requires reasoning about all possible sequences of lock acquisitions across all code paths — a task that requires tooling (thread dump analysis, lock dependency analysis) rather than manual inspection.

**Mistake 3: Using tryLock without backoff.** Replacing synchronized with `tryLock(timeout)` prevents permanent deadlock but can introduce livelock if two threads consistently time out and retry in synchrony. Random exponential backoff is required to break the symmetry.

**Mistake 4: Assuming ReentrantLock is deadlock-free.** ReentrantLock is reentrant (the same thread can acquire it multiple times without blocking), but it is not deadlock-free. Two threads with two different ReentrantLocks can deadlock just as easily as with synchronized blocks.

**Mistake 5: Conflating deadlock with starvation.** A starved thread is runnable but perpetually denied CPU time or lock access because other threads always win. A deadlocked thread is waiting and will never be unblocked. Starvation is a fairness problem; deadlock is a structural cycle. The fixes are different: starvation requires fair lock implementations (ReentrantLock with `fair=true`); deadlock requires cycle prevention or detection.

## Mental Model

Think of deadlock as a circular dependency graph for dinner guests: each guest will not start eating until they have both forks, but each holds one fork and is waiting for the one held by their neighbor. No guest is being unreasonable — each is following the protocol. The collective behavior creates an impossible state. Lock ordering is the protocol change that says "always pick up the left fork first." It seems arbitrary, but the global convention breaks the symmetry that enables the cycle. Resource allocation graphs make this visual: nodes are threads and resources; edges point from a thread to the resource it wants and from a resource to the thread holding it. A cycle in this graph is a deadlock.

## Mini Summary

- ✔ All four Coffman conditions — mutual exclusion, hold-and-wait, no preemption, circular wait — must hold for deadlock to occur; eliminating one prevents it.
- ✔ Lock ordering (global acquisition order) breaks circular wait and is the preferred prevention strategy when the lock set is static.
- ✔ tryLock with timeout breaks hold-and-wait but risks livelock without random exponential backoff.
- ✔ Deadlock detection (waits-for graph cycle detection) is used in databases where transaction rollback is a clean recovery path.
- ✔ Livelock (active but no progress) and starvation (fairness failure) are related but distinct failure modes requiring different fixes.
- ✔ Lock inversion across framework/callback boundaries is a frequent source of production deadlocks that is invisible in per-method code review.

# Guided Practice Quest

1. A service has three locks: UserLock, AccountLock, and TransactionLock. Engineers acquire them in different orders depending on the operation. Describe the specific conditions that would produce a deadlock among three threads, and propose a lock ordering protocol that prevents it.

2. An architect proposes eliminating deadlock risk by using a single coarse-grained lock across the entire service. Evaluate this proposal: what does it achieve, what does it sacrifice, and under what conditions would you accept or reject it?

3. Explain why deadlock detection (rather than prevention) is the standard strategy in relational databases, and why the same strategy is difficult to apply in Java application code.

4. Describe the symptoms you would observe in a production monitoring system (thread dumps, CPU metrics, request latency, error rates) when a deadlock has occurred, distinguishing deadlock from other concurrent failure modes.

# Solo Practice Quest

You are designing a hotel room booking service. The core operation acquires locks on the room (to check availability) and on the user account (to verify credit). Multiple concurrent booking requests can involve the same room or the same user.

Design a locking strategy that is deadlock-free for this system. Your analysis should: (1) identify the deadlock risk in a naïve implementation, (2) propose and justify a prevention strategy, (3) explain how your strategy behaves under high contention for a popular room, and (4) describe what failure mode your strategy introduces (if any) and how you would handle it at the application layer.

# Integration

**Mathematics — Graph Theory:** Deadlock detection is fundamentally a cycle detection problem in a directed graph. The resource allocation graph has threads and resources as nodes; a "request" edge points from a thread to the resource it wants, and an "assignment" edge points from a resource to the thread holding it. A deadlock exists if and only if a cycle is present in this graph. For single-instance resources, cycle detection is O(V+E) using depth-first search — computationally tractable even for large systems. For multi-instance resources (where a resource can be held by multiple threads), the more complex Banker's Algorithm is required. The mathematical structure of deadlock is therefore well-understood; the engineering challenge is maintaining the graph accurately in dynamic systems.

**Philosophy — Rationality and Collective Failure:** Deadlock is a canonical example of individually rational behavior producing collectively irrational outcomes — a theme central to game theory and social philosophy. Each thread follows a sensible local protocol: acquire the resource you need, keep what you have while waiting for more. No thread makes an error. Yet the system as a whole reaches an absurd state of total paralysis. This mirrors the Prisoner's Dilemma and the Tragedy of the Commons: rational local optimization produces global catastrophe. The philosophical lesson for system design is that correct local behavior does not guarantee correct global behavior; global protocols (lock ordering conventions, deadlock detection) must be designed at the system level, not inferred from local correctness.

**Question for reflection:** In large distributed systems where multiple services each have their own lock management, how would you extend the lock ordering principle, and what organizational discipline is required to maintain a global lock ordering across team boundaries?

# Lore Conclusion

The Academy teaches that a deadlock is not a bug in any thread — it is a bug in the relationship between threads. This is why fixing a deadlock requires reasoning about the system, not the code. The four Coffman conditions are a diagnostic lens: any time you see multiple locks being acquired together, ask which condition you are relying on not to hold. If the answer is "none in particular," you have a structural problem waiting for the right timing to manifest. The architects who never experience production deadlocks are not lucky — they are disciplined about lock ordering, minimal lock scope, and the principle that the more locks a single operation acquires, the more carefully its acquisition order must be documented and enforced across the entire codebase.
