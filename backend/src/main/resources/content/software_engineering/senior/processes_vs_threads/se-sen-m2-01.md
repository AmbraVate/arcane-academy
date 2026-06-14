---
id: se-sen-m2-01
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m2
moduleTitle: "Module 2: Concurrency & Parallelism"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: processes_vs_threads
topicTitle: "Processes vs Threads"
topicSortOrder: 1
lesson: processes_vs_threads
title: "Processes vs Threads"
sortOrder: 1
difficulty: 3
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
    - Accurately describes process isolation including separate memory spaces and OS-level protection
    - Explains thread characteristics including shared heap, independent stacks, and lighter context switch cost
    - Articulates when process isolation is preferred over thread-sharing
    - Discusses the JVM model and how Java threads map to OS threads
    - Synthesises the tradeoff between isolation (processes) and efficiency (threads)
  keywords: [process, thread, memory space, heap, stack, context switch, isolation, JVM, virtual thread, OS thread, fork, PCB, TCB]
  modelAnswer: |
    A process is an instance of a running program with its own isolated memory space: private heap, private stack, private file descriptor table, and its own page table managed by the OS. The kernel treats each process as an independent unit — a process crash does not corrupt other processes because address space isolation prevents one process's memory from being readable or writable by another. This isolation has a cost: creating a process requires allocating a full memory space, loading the program image, setting up file descriptors, and initialising the runtime — typically milliseconds. Inter-process communication (IPC) — pipes, sockets, shared memory segments — requires explicit coordination and serialisation, adding latency and complexity.

    A thread is a unit of execution within a process. All threads in a process share the same heap, the same file descriptors, and the same code segment. Each thread has its own stack (typically 512KB–1MB) and its own program counter and register set. Because threads share the heap, creation is fast (microseconds), and data sharing requires no serialisation. Context switching between threads in the same process is faster than switching between processes because the kernel does not need to swap page tables or flush the TLB. The cost of this efficiency is the absence of isolation: a thread that corrupts the heap corrupts the entire process, and a thread that holds a lock incorrectly blocks all other threads waiting for that lock.

    In the JVM, each Java thread historically corresponded to one OS thread (the 1:1 threading model). Creating and managing OS threads is expensive in terms of memory (each OS thread requires a stack) and scheduling overhead. A JVM with 10,000 OS threads will spend significant time in context switching. Java 21's Virtual Threads (Project Loom) introduce a M:N threading model: many virtual threads multiplexed onto few OS carrier threads. Virtual threads are cheap to create (millions can exist simultaneously), have small initial stacks, and are parked (unmounted from the OS thread) when they block on I/O. This allows I/O-bound workloads to achieve high concurrency without the memory and scheduling overhead of OS threads.

    The design choice between processes and threads is a tradeoff between isolation and efficiency. Use processes when: crash isolation is required (a bug in the child process must not kill the parent), security isolation is required (running untrusted code in a sandbox), or the components have genuinely independent lifecycles. Use threads when: components share data heavily, the task is CPU-bound and parallelism without IPC overhead is required, or the performance cost of process creation and IPC is prohibitive. Many production systems use both: a multi-process architecture for isolation between major subsystems, with multi-threading within each process for concurrency.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A web browser runs each tab in a separate process. A web server handles each request in a separate thread. What does this reveal about the primary design priority of each?"
    options:
      - "The browser prioritises performance; the server prioritises isolation"
      - "The browser prioritises isolation (tab crash doesn't kill others); the server prioritises efficiency (shared state, low overhead per request)"
      - "Both prioritise performance above all else"
      - "The browser uses processes because JavaScript requires it; the server uses threads for historical reasons"
    correctIndex: 1
    feedback: "Browser tab isolation prevents a crashed or malicious tab from affecting others — a security and reliability choice. The web server uses threads because request handling is short-lived, shares resources (database connections, caches), and benefits from the low overhead of thread creation vs process creation."
  - type: SHORT_TEXT
    prompt: "A Java application needs to run 50,000 concurrent tasks, most of which spend 90% of their time waiting for I/O responses. Why would Java 21 Virtual Threads be dramatically more efficient than traditional OS threads for this workload?"
    hint: "Consider what happens to an OS thread while it waits for I/O versus what Virtual Threads do."
  - type: FILL_BLANK
    prompt: "All threads within a Java process share the same ___, but each thread has its own ___."
    answer: "heap; stack"
    hint: "Objects are allocated on the shared heap; method calls and local variables use the per-thread stack."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the primary safety advantage of process isolation over thread-based concurrency?"
    options: ["Processes are faster to create than threads", "A crashed process cannot corrupt another process's memory", "Processes share data more efficiently", "Processes use less memory than threads"]
    correctIndex: 1
    feedback: "OS-level memory isolation means a process crash, memory corruption, or runaway allocation cannot affect sibling processes. This is why browsers, JVMs, and microservices use processes as isolation units. Threads share the heap, so a corrupted thread can corrupt the entire process."
  - type: MULTIPLE_CHOICE
    question: "Java 21 Virtual Threads differ from OS threads primarily because they:"
    options: ["Run on the GPU instead of the CPU", "Are multiplexed onto a small number of OS carrier threads and unmounted when blocking", "Use a separate JVM heap", "Cannot share data with other threads"]
    correctIndex: 1
    feedback: "Virtual threads implement M:N multiplexing: many virtual threads share few OS threads. When a virtual thread blocks (e.g., waiting for I/O), it is unmounted from the OS carrier thread, freeing that carrier for another virtual thread. This enables millions of concurrent tasks without millions of OS threads."
retrieval:
  recall: "List the memory regions that are shared between threads in a process versus the regions that are private to each thread."
  explain: "Explain why context switching between threads in the same process is cheaper than context switching between processes, and what cost this efficiency creates."
  mistakeId:
    code: |
      // Architecture decision: use separate OS threads for 100,000 
      // concurrent WebSocket connections in a Java application
      // Each connection gets one dedicated OS thread
      // Thread stack size: 1MB per thread (JVM default)
    answer: "This design will exhaust available memory and produce catastrophic performance degradation. 100,000 OS threads × 1MB stack = 100GB of stack memory alone — more than most servers have. Even at 256KB per thread, that is 25GB just for stacks. Additionally, the OS scheduler will spend enormous time context-switching between 100,000 threads, degrading throughput. The correct approach for 100,000 concurrent I/O-bound connections is either: (a) Java 21 Virtual Threads, which are lightweight and unmounted during blocking I/O, or (b) non-blocking I/O with an event loop (Netty, Vert.x, WebFlux) where a small number of OS threads handle all connections by never blocking. The one-thread-per-connection model works at hundreds of connections; it fails catastrophically at hundreds of thousands."
---

# Hook

Every Java application you have ever run exists within a process. Every request your web server handles runs in a thread. Yet most engineers who work with these abstractions daily have never precisely understood what separates them — and that gap in understanding is directly responsible for memory exhaustion incidents, cascade failures, and the "it works fine until we hit 10,000 users" production surprises. This lesson gives you the precise mental model.

# Lore Introduction

The Academy teaches that concurrency is not a feature you add — it is a property of the execution environment you must understand from the ground up. The engineers who debug the hardest production incidents are those who can reason about what the OS scheduler is doing, where memory is being allocated, and why 50,000 threads bring a server to its knees when 500 do not. Master processes and threads, and you master the foundation on which all concurrency knowledge rests.

# Core Learning

## Concept Introduction

**The Process** is the OS-level unit of isolation. When the OS creates a process, it allocates:
- A private virtual address space (the process cannot read or write another process's memory)
- A heap for dynamic memory allocation
- A stack for the initial thread
- A file descriptor table
- Page tables mapping virtual to physical memory

Process creation (fork/exec on Unix, CreateProcess on Windows) is heavyweight: the OS must allocate all these structures, copy or lazy-initialise the address space, and load the program image. This typically costs milliseconds.

**The Thread** is the OS-level unit of execution within a process. When a new thread is created:
- A new stack is allocated (typically 512KB–1MB for OS threads)
- A new Thread Control Block (TCB) is created with the thread's register state
- The thread is registered with the OS scheduler

All threads in a process share the heap, file descriptors, and code. Thread creation costs microseconds — significantly cheaper than process creation. Context switching between threads is also cheaper because the kernel does not need to reload page tables.

**The Shared Heap Problem**

The shared heap is threads' greatest strength and greatest danger. Any thread can read or write any heap-allocated object. No serialisation is needed. But this means any thread can corrupt any object. A thread that writes an incorrect value, creates a circular reference, or over-allocates the heap affects every other thread in the same process. This is why thread-safe design (discussed in later lessons) is critical — but first you must understand why the danger exists.

## Why It Matters

Modern backend systems routinely handle thousands of concurrent requests. The model you choose for concurrency — one OS thread per request, one virtual thread per request, an event loop with non-blocking I/O — determines your server's capacity limits. An engineer who does not understand the memory and scheduling cost of OS threads cannot diagnose why their server degrades at 1,000 concurrent requests. An engineer who understands process isolation can correctly evaluate when to use subprocess execution (for security or crash containment) versus thread pools.

## Worked Examples

**Example 1: The Cost of Threads at Scale**

A naïve web server assigns one OS thread per connection:
- 1,000 concurrent connections = 1,000 OS threads
- 1,000 × 1MB stack = ~1GB memory just for stacks
- OS scheduler must manage 1,000 threads → context switch overhead

At 10,000 connections: 10GB stack memory. This exceeds typical available memory. The server either runs out of memory or thrashes — constantly swapping stack pages in and out. This is the "C10k problem": how to handle 10,000+ concurrent connections without one-thread-per-connection.

Solutions:
- **Virtual Threads (Java 21):** 10,000 virtual threads share a small number (e.g., 8–16) of OS carrier threads. Each virtual thread has a small initial stack that grows on demand. When a virtual thread blocks on I/O, it is unmounted from the carrier thread, which then picks up another virtual thread. Memory and scheduling overhead is dramatically reduced.
- **Non-blocking I/O (event loop):** A single thread handles all I/O events using `select`/`epoll`. No thread is ever blocked — I/O completion is signalled via callbacks. This is the Node.js and Netty model.

**Example 2: When Processes Are Correct**

A security-sensitive platform needs to evaluate user-submitted code (like a competitive programming judge). Options:
- **Thread:** user code runs in the JVM heap of the main process. A malicious script could attempt to read the heap, exhaust memory, or crash the JVM, taking down the entire platform.
- **Process (subprocess):** user code runs in a separate JVM process with resource limits (CPU time, memory cap, file system restrictions). A crash or exploit affects only that subprocess. The main platform remains unaffected.

The cost is process creation overhead (milliseconds) and IPC for result communication. For this security-isolation requirement, that cost is not just acceptable — it is mandatory.

**Example 3: JVM Threading Model Evolution**

| Model | Java Version | OS Threads | Virtual Threads | Use Case |
|---|---|---|---|---|
| 1:1 OS threads | Java 1–20 | One per Java thread | None | CPU-bound work, low concurrency |
| Virtual threads | Java 21+ | Small carrier pool | Millions possible | I/O-bound, high concurrency |
| Parallel streams | Java 8+ | ForkJoinPool | N/A | CPU-parallel data processing |

The JVM's evolution toward Virtual Threads is a direct response to the C10k problem: enabling high concurrency for I/O-bound workloads without the memory cost of millions of OS threads.

## Common Mistakes

- **Assuming more threads always means more throughput.** For CPU-bound work, the optimal thread count is close to the number of CPU cores. Beyond that, context-switching overhead reduces throughput. For I/O-bound work, more concurrency is beneficial — but through Virtual Threads or non-blocking I/O, not more OS threads.
- **Treating thread creation as free.** Each OS thread costs ~1MB of stack memory. In containers with 512MB memory limits, you have room for roughly 500 threads before the container OOMs. Architects must count threads as a resource.
- **Assuming shared memory (threads) is always faster than IPC (processes).** For heavy computation where the work dwarf the communication overhead, IPC is perfectly acceptable. Do not sacrifice crash isolation for marginal throughput gains.
- **Forgetting that Virtual Threads are still threads.** Virtual threads share the heap and have the same thread-safety requirements as OS threads. They solve the concurrency-at-scale problem; they do not solve race conditions or deadlocks.

## Mental Model

A process is a country: it has its own territory, its own laws, and its own currency. No other country can directly enter its territory. Trade between countries (IPC) requires formal channels.

A thread is a citizen within that country: free to move anywhere within the country's borders, access the same shared resources (the heap), but subject to coordination requirements when accessing shared property. Citizens are cheap to create and coordinate efficiently — but a rogue citizen can damage shared resources that affect everyone.

Virtual threads are like delivery drones: many drones can exist simultaneously, but they only occupy a landing pad (OS thread) when actively delivering — they hover in a queue (parked) when waiting.

## Mini Summary

- ✔ Processes have isolated memory spaces; a process crash cannot corrupt other processes
- ✔ Threads share the heap and are cheaper to create and context-switch than processes
- ✔ OS threads cost ~1MB of stack each; 10,000 OS threads require significant memory and scheduling overhead
- ✔ Java 21 Virtual Threads multiplex millions of logical threads onto a small OS carrier pool, enabling high-concurrency I/O-bound applications
- ✔ Use processes when crash isolation or security isolation is required; use threads when data sharing and low overhead are priorities
- ✔ Virtual threads have the same thread-safety requirements as OS threads — they solve the scale problem, not the concurrency correctness problem

# Guided Practice Quest

Work through the guided steps. Reason about the memory arithmetic and the scheduling implications of each design choice — numbers matter in concurrency engineering.

# Solo Practice Quest

Design the concurrency model for a real-time collaborative document editor (think Google Docs). The system must support 100,000 concurrent users, each making document edits. Edits must be broadcast to all other collaborators within 500ms.

Analyse:
1. Should each connection be handled by an OS thread, a Virtual Thread, or a non-blocking event loop? Justify with memory arithmetic.
2. How many processes should the server run? Should each user's document be in its own process?
3. When a user's session crashes, what should be isolated from the impact?
4. How does your concurrency model change if each document edit requires a database write?

# Integration

**Mathematics connection:** The scheduling of threads is modelled as a queuing theory problem. Each CPU core is a server in the queue; OS threads are customers. Little's Law (L = λW) tells you that the average number of threads waiting (L) equals the arrival rate (λ) times the average wait time (W). When thread count exceeds CPU count, average wait time W increases — each additional thread above the core count adds to the queue. For I/O-bound threads that spend most of their time blocked, this analysis changes: blocked threads are not in the CPU queue, making higher thread-to-core ratios tolerable. Virtual Threads exploit this insight by only consuming a carrier thread when actually executing.

**Philosophy connection:** The tension between process isolation (independence, autonomy) and thread sharing (interdependence, efficiency) mirrors a deep philosophical tension in political philosophy: individualism versus communitarianism. Processes are individualist units — self-contained, self-governing, isolated. Threads are communitarian — sharing resources, coordinating on shared state, accepting mutual vulnerability. Neither model is universally correct; the right model depends on what properties — independence or efficiency — matter most in the specific context. How does this philosophical tension map to the design decisions you make in distributed system architecture?

# Lore Conclusion

The JVM gives you a powerful abstraction over the OS's raw concurrency primitives. Understanding what lies beneath that abstraction — processes, OS threads, context switches, memory maps — gives you the ability to reason about performance limits, debug pathological cases, and make architectural decisions that hold under production load. Every engineer knows threads and processes exist. The senior engineer knows precisely what each one costs.
