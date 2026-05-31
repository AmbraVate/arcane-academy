---
id: se-sen-m6-03
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m6
moduleTitle: "Module 6: Performance Engineering"
moduleGlyph: "🚀"
moduleSortOrder: 6
topicSlug: cpu_bottlenecks
topicTitle: "CPU Bottlenecks"
topicSortOrder: 3
lesson: cpu_bottlenecks
title: "CPU Bottlenecks"
sortOrder: 3
difficulty: 4
estimatedMinutes: 28
xpReward: 60
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [memory_management]
integrationDomains: [mathematics, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Distinguishes CPU-bound workloads (high CPU utilisation, compute-intensive) from I/O-bound workloads (threads blocked waiting, CPU underutilised)"
    - "Explains context switching cost and why excessive thread counts degrade performance on CPU-bound work"
    - "Describes JIT compilation phases (interpreted, C1, C2) and how warmup affects JVM performance"
    - "Identifies reducing object creation as a lever for GC pressure reduction and CPU reclamation"
    - "Explains when StringBuilder outperforms String concatenation and the JVM's own optimisations"
  keywords: [CPU-bound, I/O-bound, context switch, JIT, C2 compiler, StringBuilder, String concatenation, object allocation, cache coherence, synchronisation, warmup]
  modelAnswer: |
    A CPU-bound task keeps the processor busy computing: cryptography, image processing, sorting large arrays, complex mathematical operations. CPU utilisation approaches 100% and adding more CPUs (or parallel threads up to core count) improves throughput. An I/O-bound task spends most of its time waiting for network, disk, or database responses. CPU utilisation is low; threads are mostly blocked. Adding more threads (not cores) can improve throughput by overlapping waiting periods.

    Context switching occurs when the OS scheduler preempts one thread and runs another. Each switch saves and restores CPU registers, instruction pointer, and cache lines — typically 1-10 microseconds of overhead. For CPU-bound work with more threads than cores, context switching dominates: every switch is wasted time that could be computation. The optimal thread count for CPU-bound work is approximately the number of CPU cores (N or N+1 to account for occasional I/O). Excessive synchronisation causes threads to block on locks, forcing context switches even with optimal thread counts.

    JIT compilation occurs in stages. The JVM initially interprets bytecode (slowest). After a method reaches a call count threshold (~1,500), C1 (client compiler) compiles it to native code with basic optimisations. After a higher threshold (~10,000-15,000 executions), C2 (server compiler) performs aggressive optimisations: inlining, loop unrolling, escape analysis, and speculative deoptimisation. JVM warmup (the period before methods reach C2 compilation) affects benchmarks and post-deployment performance. JMH (Java Microbenchmark Harness) forces JVM warmup before measuring to avoid benchmarking interpreted code.

    String concatenation in a loop allocates a new String object per iteration before Java 9. `"a" + "b" + "c"` in Java 8 compiles to `new StringBuilder().append("a").append("b").append("c").toString()` — but in a loop, a new StringBuilder is created per iteration. Explicit StringBuilder reuse (pre-allocating capacity, reusing across iterations) eliminates per-iteration object creation. In Java 9+ with invokedynamic-based string concatenation, the JVM may optimise some cases automatically, but explicit StringBuilder is still preferable in tight loops.

    Reducing object allocation reduces GC pressure, which frees CPU from garbage collection. Key techniques: object pooling for expensive objects (byte buffers, connections), reusing arrays instead of allocating new ones, using primitive types instead of boxed types (int vs Integer), and designing value objects to be immutable but short-lived to stay in the young generation (cheap to collect).
guidedSteps:
  - id: cpu-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A service performs PDF generation (CPU-intensive) with 200 threads on a 4-core machine. CPU is at 100% but throughput is half what was expected from 4 cores. What is the most likely cause?
    inputConfig:
      options:
        - "A. The JVM is not warmed up yet"
        - "B. Context switching overhead — 200 threads on 4 cores means 50 threads per core compete for CPU time"
        - "C. I/O latency — PDF generation is disk-bound"
        - "D. Memory pressure — heap is full"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["B"]
      rejectedFeedback: "CPU-bound work with more threads than cores is dominated by context switching. Each of the 200 threads competes for 4 cores, switching 50x more than necessary. For CPU-bound PDF generation, the optimal thread count is 4-8 (core count). Reducing threads to core count will eliminate most context switch overhead."
    hint: "Consider the ratio of threads to available CPU cores for compute-intensive work."
    reflectionPrompt: "Thread count and core count must be matched to the workload type. More threads only helps for I/O-bound work."
  - id: cpu-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A JVM method is initially interpreted, then compiled by the C1 compiler, and finally by the ___ compiler for maximum optimisation after reaching ~10,000-15,000 invocations.
    inputConfig:
      placeholder: "compiler name"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["C2", "C2 compiler", "server compiler"]
      rejectedFeedback: "The C2 (server) compiler performs aggressive optimisations: method inlining, loop unrolling, escape analysis, and speculative deoptimisation. It is triggered after the JVM's tier 4 threshold (~10,000-15,000 invocations). This is why JVM performance improves after a warmup period."
    hint: "There are two JIT compilers — C1 for quick compilation, and another for maximum optimisation."
    reflectionPrompt: "JVM warmup is real: pre-production load testing should include a warmup phase to reach C2 compilation before measuring representative performance."
  - id: cpu-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A developer writes the following code in a hot loop processing 1 million records:
      ```java
      String result = "";
      for (Record r : records) {
          result = result + r.getValue() + ",";
      }
      ```
      Explain the CPU and memory impact of this code and rewrite it correctly.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [StringBuilder, allocation, GC, immutable, concatenation, loop, capacity]
      rejectedFeedback: "Each `result + r.getValue() + ','` creates a new String object and discards the previous one. 1 million iterations = ~1 million String allocations, flooding Eden and triggering frequent minor GC. Fix: use StringBuilder pre-allocated with estimated capacity: `StringBuilder sb = new StringBuilder(records.size() * 20); for (Record r : records) { sb.append(r.getValue()).append(','); } String result = sb.toString();`"
    hint: "Strings are immutable in Java — what happens to the old String on each concatenation?"
    reflectionPrompt: "String immutability is a correctness guarantee; its performance implication in loops is why StringBuilder exists."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "For a CPU-bound task, what is the optimal number of threads relative to available CPU cores?"
    options:
      - "A. 10x core count — more threads keep CPUs busier"
      - "B. Approximately equal to core count — additional threads add context switching overhead without more parallelism"
      - "C. 1 thread — to avoid any synchronisation"
      - "D. 100 threads — to ensure all cores are always busy"
    correctIndex: 1
    feedback: "CPU-bound tasks use the CPU fully. Adding more threads than cores means threads compete for CPU time, incurring context switch overhead without additional parallelism. N or N+1 threads for N cores is optimal for CPU-bound work. I/O-bound tasks benefit from more threads because threads spend most time waiting."
  - type: MULTIPLE_CHOICE
    question: "Why does excessive synchronisation hurt CPU-bound performance even when there is no contention?"
    options:
      - "A. Synchronized methods are always slower than unsynchronised ones due to bytecode overhead"
      - "B. Even uncontested locks require memory barriers that flush CPU caches and prevent instruction reordering optimisations"
      - "C. The JVM cannot JIT-compile synchronised methods"
      - "D. Synchronisation always triggers a context switch"
    correctIndex: 1
    feedback: "Memory barriers (required by the Java Memory Model for synchronised blocks) prevent certain CPU and compiler optimisations. Uncontested locks do not cause context switches, but they do prevent aggressive reordering and cache use. Excessive synchronisation on hot code paths reduces the JIT's optimisation headroom."
retrieval:
  recall: "Explain the difference between CPU-bound and I/O-bound workloads and how the optimal thread pool size differs between them."
  explain: "Why does a JVM application often perform better after 60 seconds of load compared to immediately after startup? Explain JIT compilation phases to a junior developer."
  mistakeId:
    code: |
      public List<String> processNames(List<String> names) {
          List<String> result = new ArrayList<>();
          for (String name : names) {
              result.add(name.toLowerCase() + "_processed");
          }
          return result;
      }
      // Called 50,000 times per second on a list of 100 names each time
    answer: "5 million String concatenation operations per second, each creating a temporary String. At 100 names per call × 50,000 calls/second = 5 million allocations/second from concatenation alone, plus the 5 million 'name.toLowerCase()' allocations. This floods Eden and triggers high GC frequency. Fix: use StringBuilder per element, or use String.format only when needed, and consider whether toLowerCase is needed for all names or only specific ones (e.g., cache the result if names repeat)."
---

# Hook

The performance test is running. The service is handling 500 requests/second — half the target of 1,000. CPU is pegged at 100%. Adding more threads makes it worse. The engineer doubles the machine size: 8 cores instead of 4. Performance improves linearly — the code is CPU-bound. But the team notices that performance at startup is 30% lower than after 5 minutes of load. The JVM is warming up. Understanding CPU bottlenecks means understanding not just what your code does, but what the CPU, OS scheduler, and JIT compiler do to it.

# Lore Introduction

The Academy's performance masters speak of "spinning the gears without turning the wheel" — wasted CPU cycles that generate heat but no output. Context switches that interrupt computation in its prime. String concatenations that manufacture and discard objects faster than the garbage collector can clean them. JIT compilation that performs brilliantly after warmup but interprets code during the critical first minutes of deployment. CPU bottleneck mastery is the art of ensuring that every cycle the processor spends is a cycle that advances the computation — not a cycle wasted on overhead, speculation, or redundant allocation.

# Core Learning

## Concept Introduction

**CPU-Bound vs I/O-Bound**

A CPU-bound task is limited by processor speed: cryptography, JSON parsing at scale, matrix multiplication, complex business rule evaluation. CPU is at 100%; adding more cores or more parallelism (up to core count) improves throughput.

An I/O-bound task is limited by waiting: database queries, HTTP calls, disk reads. CPU is underutilised; threads spend most of their time blocked. Adding more threads (beyond core count) allows overlap of waiting periods, improving throughput without adding CPU.

Optimal thread pool sizing differs:
- CPU-bound: `threads ≈ core count` (N or N+1)
- I/O-bound: `threads ≈ core count × (1 + wait_time / compute_time)` — often 10-100x core count

**Context Switching**

The OS scheduler is preemptive. Every 1-10ms (scheduler tick), it may switch from one thread to another. Each switch saves registers, instruction pointer, and cache state; loads the next thread's state. Cost: 1-10 microseconds of overhead plus cache invalidation. For CPU-bound work, every context switch is a computation interruption. 200 threads on 4 cores means up to 50x more context switches than necessary.

**JIT Compilation Phases**

| Phase | Trigger | Speed |
|-------|---------|-------|
| Interpreted | Method first called | Slowest |
| C1 (client compiler) | ~1,500 invocations | Moderate |
| C2 (server compiler) | ~10,000-15,000 invocations | Fastest — aggressive optimisations |

C2 performs: method inlining (eliminates call overhead), loop unrolling, escape analysis (avoids heap allocation for objects that don't escape the stack), and speculative deoptimisation (assumes common case, backs off if violated). JVM warmup takes 30-120 seconds under load before C2 kicks in for all hot methods.

**Cache Coherence**

Modern CPUs have multiple levels of cache (L1, L2, L3). When multiple threads write to variables on the same cache line ("false sharing"), hardware cache coherence protocols force expensive cache invalidation even when threads are accessing logically independent data. Use `@Contended` (Java 8+) or padding to prevent false sharing in concurrent counters.

## Why It Matters

CPU bottlenecks explain why well-written code can be slow in production. String concatenation in loops, excessive object allocation triggering GC, too many threads causing context switching, and insufficient JVM warmup in canary deployments are all CPU performance problems that do not show up in unit tests. Senior engineers understand these dynamics and design code — and deployment strategies — that account for them.

## Worked Examples

**Example 1: Measuring Context Switch Overhead**

```java
// Thread pool for CPU-bound image processing
// WRONG: 200 threads on an 8-core machine
ExecutorService pool = Executors.newFixedThreadPool(200);

// RIGHT: match thread count to core count
int cores = Runtime.getRuntime().availableProcessors();
ExecutorService pool = Executors.newFixedThreadPool(cores);
```

Benchmark on 8-core machine:
- 200-thread pool: 12,000 images/second (87% context switch overhead in profiler)
- 8-thread pool: 47,000 images/second
- Improvement: 3.9x — almost linear with core count, as expected

**Example 2: StringBuilder vs String Concatenation**

```java
// SLOW: O(n²) allocations in loop
String csv = "";
for (String value : values) {
    csv += value + ",";  // New String allocation every iteration
}

// FAST: O(n) allocations
StringBuilder sb = new StringBuilder(values.size() * 15); // pre-size estimate
for (String value : values) {
    sb.append(value).append(',');
}
String csv = sb.toString();  // Single allocation at end
```

For 10,000 values: String concatenation allocates ~10,000 String objects (50MB in Eden). StringBuilder allocates 1 StringBuilder + 1 final String. GC pressure reduction: 99.98%.

**Example 3: Escape Analysis — JVM Avoids Heap Allocation**

```java
// JVM C2 may stack-allocate Point if it doesn't escape the method
public double distance(int x1, int y1, int x2, int y2) {
    Point p1 = new Point(x1, y1);  // May be stack-allocated by C2
    Point p2 = new Point(x2, y2);  // May be stack-allocated by C2
    return p1.distanceTo(p2);
}
```

After JVM warmup, C2's escape analysis detects that `p1` and `p2` do not escape the method. It may scalar-replace them (store fields in CPU registers) or stack-allocate them (no heap allocation, no GC pressure). This is a zero-allocation hot path — but only after C2 kicks in at ~10,000 invocations.

## Common Mistakes

- **Over-threading CPU-bound work**: adding threads beyond core count increases context switching, reducing throughput. Profile thread states — if threads are RUNNABLE (not BLOCKED/WAITING), you are CPU-bound.
- **Benchmarking without JVM warmup**: benchmarks run for less than 30 seconds on rarely-called methods measure C1 or interpreted performance, not production C2 performance. Always use JMH with warmup iterations.
- **Unnecessary boxing**: `Integer` requires heap allocation; `int` is a primitive in a register. Frequent `int`→`Integer` boxing in collections causes allocation pressure. Prefer primitive collections (Eclipse Collections, Trove) for hot code.
- **Volatile overuse**: `volatile` prevents certain JIT optimisations and adds memory barrier cost. Use `AtomicInteger` / `LongAdder` for counters, and only `volatile` for flags.
- **Synchronising on hot paths**: even uncontested locks carry memory barrier cost. Use lock-free structures (`ConcurrentHashMap`, `LongAdder`) for hot shared state.

## Mental Model

Think of CPU time as water flowing through a pipe. The pipe's diameter is the number of cores. Adding more threads beyond core count does not widen the pipe — it just adds turbulence (context switching) that reduces flow. String concatenation in loops is like drilling holes in the pipe — most of the water (CPU cycles) is diverted to creating and collecting garbage rather than doing work. JIT warmup is like the pipe gradually straightening and smoothing its interior — flow increases over time until the C2 "smooth pipe" is fully established. Optimise the pipe, not the pump.

## Mini Summary

- ✔ CPU-bound work benefits from threads ≈ core count; I/O-bound work benefits from higher thread counts that overlap waiting periods
- ✔ Context switching costs 1-10µs per switch plus cache invalidation; excessive threads on CPU-bound work create more overhead than parallelism
- ✔ JIT C2 compiler kicks in after ~10,000 invocations and performs aggressive optimisations; JVM warmup period is real and affects deployment strategies
- ✔ String concatenation in loops creates O(n) allocations; StringBuilder with pre-allocated capacity is O(1) allocations
- ✔ Reduce object allocation to reduce GC pressure and free CPU cycles currently consumed by garbage collection

# Guided Practice Quest

Work through the guided steps above. For the code-rewrite step, think about not just correctness but capacity — pre-allocating StringBuilder to the right size prevents internal array resizing.

# Solo Practice Quest

A payment processing service handles 2,000 transactions/second on a 16-core machine. Flame graph shows: 35% of CPU in `TransactionValidator.validate()`, which performs 8 regex matches per transaction. GC logs show 800 minor GCs per minute. Thread count is 512.

Design an optimisation plan:
1. Is this CPU-bound, I/O-bound, or GC-bound? What evidence supports your diagnosis?
2. What is the first optimisation you would make to the thread count and why?
3. How would you address the regex bottleneck (assume regex patterns are static and reused)?
4. What code-level changes would reduce the 800 minor GCs per minute?
5. How would you measure each optimisation's effect in isolation?

# Integration

**Connecting to Mathematics — Amdahl's Law and Parallelism Limits**

Amdahl's Law states that the maximum speedup from parallelism is limited by the sequential fraction of the work: `Speedup = 1 / (S + (1 - S) / N)` where S is the fraction of work that must run sequentially and N is the number of processors. If 30% of a task is sequential, the maximum speedup from infinite cores is only 3.33x. This law directly constrains CPU-bound optimisation: profiling reveals the sequential bottleneck (the Amdahl serial fraction), and throwing more cores at it past that limit yields diminishing returns. Context switching overhead is an additional tax that reduces the effective N in Amdahl's formula. Engineers who understand Amdahl's Law set realistic parallelism targets rather than assuming linear scaling. How does understanding Amdahl's Law change how you would design the parallelism strategy for a data processing pipeline?

**Connecting to Design — Immutability and Allocation Trade-offs**

String's immutability is a design choice that makes strings safe to share across threads without synchronisation. The cost is allocation: every transformation creates a new object. StringBuilder is the mutable counterpart — safe only when confined to a single thread or operation. The tension between immutability (correctness, thread safety) and mutability (performance, allocation efficiency) is a recurring design trade-off. Value types (Java 16+ Records, Valhalla value classes) aim to provide immutable objects with stack or inline allocation, eliminating the trade-off. Until Valhalla is ubiquitous, the design principle is: design for immutability by default, introduce controlled mutability only where profiling proves allocation is a bottleneck.

# Lore Conclusion

Every CPU cycle is a unit of potential. Wasted on context switching, on building and discarding objects, on interpreting bytecode that could be compiled — it is potential unrealised. The master performance engineer is a conservationist of cycles: they eliminate wasteful patterns, match thread counts to workload types, and trust the JIT to optimise what they write clearly. They do not guess — they profile. They do not over-thread — they measure. They write StringBuilder because they understand what concatenation costs. Small disciplines, compounded across millions of requests per second, become the difference between a system that scales and a system that collapses.
