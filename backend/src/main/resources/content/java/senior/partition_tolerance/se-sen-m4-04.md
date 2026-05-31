---
id: se-sen-m4-04
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m4
moduleTitle: "Module 4: Distributed Systems"
moduleGlyph: "🌐"
moduleSortOrder: 4
topicSlug: partition_tolerance
topicTitle: "Partition Tolerance"
topicSortOrder: 4
lesson: partition_tolerance
title: "Partition Tolerance"
sortOrder: 4
difficulty: 4
estimatedMinutes: 28
xpReward: 60
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [replication]
integrationDomains: [mathematics, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Defines a network partition and its real-world causes"
    - "Explains the split-brain problem and its danger"
    - "Describes fencing tokens as a split-brain prevention mechanism"
    - "Explains quorum reads and writes (W+R>N)"
    - "Names at least two conflict resolution strategies"
  keywords: [network partition, split-brain, fencing token, quorum, W+R>N, conflict resolution, last-write-wins, CRDT, two-phase commit, lease]
  modelAnswer: |
    A network partition is a loss of network connectivity between subsets of nodes
    in a distributed system. Causes: hardware failure, network congestion, misconfiguration,
    data centre outage.

    Split-brain: when partitioned segments both believe they are the active primary
    and accept writes independently, creating diverging state that is hard to reconcile.

    Fencing token: a monotonically increasing token issued by a lock service (e.g., ZooKeeper).
    Each write must include the current token; storage layer rejects writes with
    lower tokens than the highest seen, preventing stale primaries from writing.

    Quorum: with N replicas, require W writes and R reads where W+R>N.
    Guarantees read/write overlap, preventing stale reads without global coordination.

    Conflict resolution strategies:
    - Last-write-wins (LWW): highest timestamp wins — risk of data loss
    - CRDT: merge is always safe (commutative, associative, idempotent)
    - Application-level merge: application logic decides how to reconcile
    - Two-phase commit: atomic across nodes but blocks on coordinator failure
guidedSteps:
  - id: pt-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Two primary database nodes lose connectivity with each other but both remain connected to some of their clients. Both continue accepting writes. This situation is called:
    inputConfig:
      options:
        - "Replication lag — writes are queued and will sync later"
        - "Split-brain — both nodes act as primary simultaneously"
        - "Quorum failure — neither node has a majority"
        - "Failover — the secondary promotes itself automatically"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Split-brain — both nodes act as primary simultaneously"]
      rejectedFeedback: "Split-brain occurs when partitioned nodes both believe themselves to be the active primary and continue accepting writes. This creates two diverging copies of the data that are difficult to reconcile without data loss."
    hint: "Two nodes, both thinking they are the leader — a brain divided."
    reflectionPrompt: "Split-brain is one of the most dangerous failure modes. Most HA systems use quorum, STONITH, or fencing tokens to prevent it."
  - id: pt-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A monotonically increasing number issued by a lock service (e.g., ZooKeeper) that storage nodes use to reject writes from outdated leaders is called a ___ ___.
    inputConfig:
      placeholder: "two words"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["fencing token", "fencing tokens"]
      rejectedFeedback: "A fencing token is a monotonically increasing number (epoch, lease number) included with every write to a storage system. The storage system rejects any write with a token lower than the highest token it has seen, preventing old/stale leaders from writing after a new leader has been elected."
    hint: "It fences out stale leaders by using an ever-increasing counter."
    reflectionPrompt: "Fencing tokens are simple but powerful. They make 'outdated leader' writes self-preventing rather than requiring the leader to realise it is outdated."
  - id: pt-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe a real-world scenario (not a lab experiment) where a network partition could occur and explain what the two partition scenarios should do to handle it safely.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [partition, datacenter, network, split, quorum, refuse, stale, majority, availability, consistency]
      rejectedFeedback: "Example: An AWS us-east-1 to us-west-2 inter-region link degrades, partitioning two data centres. The majority partition (e.g., the one with 2 of 3 nodes) should continue serving traffic using quorum. The minority partition (1 of 3 nodes) should refuse writes (to prevent split-brain) and optionally serve stale reads with an explicit staleness warning."
    hint: "Think about a multi-datacenter deployment where the network link between DCs fails."
    reflectionPrompt: "Real partitions happen during planned maintenance, infrastructure failures, and even BGP route leaks. Design for them explicitly."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A ZooKeeper lock holder goes silent for longer than its lease period. A new leader is elected with lease #42. The old leader recovers and tries to write with lease #41. What happens?"
    options:
      - "The write succeeds because the old leader had a valid lease"
      - "The write is queued until the new leader acknowledges it"
      - "The storage layer rejects the write because 41 < 42 (fencing token)"
      - "A merge conflict is created between the two writes"
    correctIndex: 2
    feedback: "Fencing tokens work by the storage layer rejecting writes with tokens lower than the highest seen. Since the new leader has already written with token 42, the storage layer will reject any write with token 41 as stale."
  - type: MULTIPLE_CHOICE
    question: "Last-write-wins (LWW) conflict resolution has a significant drawback, which is:"
    options:
      - "It requires global coordination between all nodes"
      - "It always picks the wrong value"
      - "Concurrent writes with close timestamps can silently discard one writer's changes"
      - "It is incompatible with quorum replication"
    correctIndex: 2
    feedback: "LWW uses timestamps to pick the winner, but clocks are not perfectly synchronised across distributed nodes. Concurrent writes within the clock skew window may have the 'wrong' winner chosen, silently discarding a legitimate write without any error to the client."
retrieval:
  recall: "Define network partition, split-brain, and fencing token. Explain how fencing tokens prevent split-brain."
  explain: "Explain to a junior developer why you cannot simply use a timestamp to decide which write wins during a partition conflict."
  mistakeId:
    code: |
      // Leader election: node becomes leader if it hasn't heard from current leader in 30s
      if (timeSinceLastHeartbeat() > 30_000) {
          promoteToLeader(thisNode);
          acceptAllWrites();
      }
    answer: "Without a quorum check or fencing token, both the old leader (if it recovers) and the new self-promoted leader will be writing simultaneously — classic split-brain. A correct implementation requires winning an election with a quorum of nodes (majority must agree this node is leader), not unilateral self-promotion based on a timeout."
---

# Hook

The Academy's northern and southern wings are connected by a single enchanted bridge. One night, a magical storm severs the bridge. Both wings have mages, both have records, both believe themselves to be the primary authority of knowledge. Both accept new spell submissions. When the storm clears and the bridge is restored — whose spells are correct? This is the partition tolerance problem.

# Lore Introduction

The Guild of Fault-Tolerant Enchanters has a saying: *The network will fail. The question is only when.* A system that ignores this axiom is not a distributed system — it is a distributed accident waiting to happen. Mastering partition tolerance means designing systems that continue operating correctly despite broken communication channels, and recovering cleanly when those channels are restored.

# Core Learning

## Concept Introduction

A **network partition** is a communication break between subsets of nodes in a distributed system. Neither subset can reach the other, but both can reach their local clients. This creates the fundamental dilemma: should each subset continue operating (at the risk of diverging state) or refuse to operate (sacrificing availability)?

**Key failure modes:**
- **Split-brain** — both segments act as primary simultaneously, creating diverging state
- **Stale reads** — nodes serve data that has not received recent updates from partitioned nodes
- **Write divergence** — both segments accept conflicting writes to the same data

## Why It Matters

Partitions are not hypothetical. AWS has regional outages. Data centre cables are accidentally cut. BGP route leaks partition the internet. Kubernetes nodes lose heartbeats under load. Any production distributed system WILL experience partitions; the question is whether you designed for them.

## Worked Examples

### Detecting and handling a partition in ZooKeeper

```java
// ZooKeeper uses quorum: majority of nodes must be reachable to serve requests
// Minority partition automatically refuses requests

CuratorFramework client = CuratorFrameworkFactory.builder()
    .connectString("zk1:2181,zk2:2181,zk3:2181")
    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
    .build();

// ZooKeeper node monitors connection state
client.getConnectionStateListenable().addListener((c, state) -> {
    if (state == ConnectionState.SUSPENDED || state == ConnectionState.LOST) {
        log.warn("ZooKeeper connection lost — partition may be in progress");
        // Stop accepting writes that require distributed coordination
        acceptingWrites.set(false);
    } else if (state == ConnectionState.RECONNECTED) {
        log.info("ZooKeeper reconnected — resuming writes");
        acceptingWrites.set(true);
    }
});
```

### Fencing tokens preventing split-brain

```java
// Lock service issues monotonically increasing tokens
// Storage layer enforces: reject writes with token < max seen

public class FencedStorageService {
    private final AtomicLong maxTokenSeen = new AtomicLong(0);

    public void write(String key, String value, long fencingToken) {
        // Enforce fencing: reject stale leaders
        long current = maxTokenSeen.get();
        if (fencingToken < current) {
            throw new StaleLeaderException(
                "Write rejected: token " + fencingToken + " < max seen " + current);
        }
        maxTokenSeen.updateAndGet(prev -> Math.max(prev, fencingToken));
        storage.put(key, value);
    }
}

// Old leader (token=41) tries to write after new leader (token=42) is elected
// → throws StaleLeaderException — split-brain prevented
```

### Quorum reads and writes (Cassandra)

```java
// N=5, W=3, R=3 → W+R=6 > 5 → reads always overlap writes by at least 1 node
// Minority partition (2 nodes): cannot form a quorum of 3 → refuses writes

// Majority partition (3 nodes): can form quorum → continues operating

session.execute(
    SimpleStatement.newInstance("INSERT INTO inventory (sku, qty) VALUES (?, ?)", sku, qty)
        .setConsistencyLevel(DefaultConsistencyLevel.QUORUM) // requires 3/5 nodes
);
```

The minority partition (2 nodes) cannot reach quorum of 3, so it refuses writes — preventing split-brain without needing explicit leader election.

### Conflict resolution strategies

```java
// Strategy 1: Last-Write-Wins (LWW)
// Risk: clock skew can choose the wrong winner silently
record VersionedValue(String value, long timestamp) {}

VersionedValue merge(VersionedValue a, VersionedValue b) {
    return a.timestamp() >= b.timestamp() ? a : b; // higher timestamp wins
}

// Strategy 2: CRDT (conflict-free) — G-Counter
// Safe: merge is always correct regardless of order
long mergedCount = Math.max(localCount, remoteCount); // per-node max

// Strategy 3: Multi-Version Concurrency Control (MVCC)
// Both versions preserved; application or human decides which to keep
record ConflictResult(List<VersionedValue> conflictingVersions) {}

// Strategy 4: Application-level merge
// Example: shopping cart — union of items is always safe
Set<Item> mergedCart = new HashSet<>(localCart);
mergedCart.addAll(remoteCart); // both sides' items included
```

### Real-world partition scenario — AWS multi-region

```
Normal operation:
  us-east-1 (primary)  ←→  us-west-2 (replica)
  Both receive writes (multi-leader) or east-1 leads.

During partition (inter-region link fails):
  us-east-1:  has quorum (3/3 nodes) → continues accepting writes
  us-west-2:  minority (0/3 can reach east) → refuses writes, serves stale reads

Post-partition:
  Reconciliation: replay west-2's missed writes from east-1
  Conflict resolution: any conflicting writes resolved per strategy
```

## Common Mistakes

1. **Assuming partitions are rare.** In production, partitions happen regularly: planned maintenance, cloud provider issues, bad deploys. Design for the rule, not the exception.

2. **Using wall-clock timestamps for conflict resolution without clock synchronisation.** Distributed clock skew (even with NTP) can be 10ms or more. LWW with wall clocks can choose the wrong winner.

3. **Not testing partition behaviour.** Systems that have never been tested under a simulated partition almost always have bugs. Use chaos engineering tools (Chaos Monkey, Toxiproxy) to simulate partitions.

4. **Confusing partition tolerance with fault tolerance.** Partition tolerance specifically means the system continues operating when network communication fails. Node crashes are a different failure mode with different solutions.

5. **Forgetting to handle partition recovery.** Partitions end. Data must be reconciled. Systems that handle the partition but not the reconciliation leave data in a permanently inconsistent state.

## Mental Model

Imagine two groups of people trapped in separate rooms after an earthquake blocks the corridor. Each group keeps a shared notebook of decisions. When the corridor is cleared:
- If both groups made decisions in isolation, they need to reconcile — this is conflict resolution
- A fencing token is like each group numbering their decisions; the group with lower numbers defers to the group with higher numbers
- Quorum is agreeing beforehand that decisions only count if more than half the groups agree

## Mini Summary

- A network partition is loss of communication between nodes; they remain independently operational
- Split-brain: both partitions believe they are primary and accept diverging writes
- Fencing tokens prevent split-brain by the storage layer rejecting stale-leader writes
- Quorum (W+R>N) ensures read/write overlap without explicit leader election
- Conflict resolution strategies: LWW, CRDTs, application-level merge, MVCC

# Guided Practice Quest

Work through the guided steps to practise identifying split-brain risks and applying fencing token and quorum principles.

# Solo Practice Quest

Design a partition-tolerant distributed lock service that:
1. Issues fencing tokens to lock holders
2. Detects when a lock holder's lease expires
3. Safely promotes a new lock holder with a higher fencing token
4. Rejects writes from the old holder even if it recovers

Describe the protocol as pseudocode and explain what happens when the lock service itself is partitioned.

# Integration

**Connecting to Mathematics — Graph Theory and Design — Failure Mode Analysis**

Network partitions are naturally modelled as graph disconnections. A distributed system is a graph where nodes are machines and edges are network links. A partition is a cut in this graph that divides it into two or more disconnected subgraphs. Graph theory tells us that for any connected graph, there exists at least one edge whose removal disconnects it (a bridge). Real networks are designed to have multiple paths (redundancy), but the probability of a disconnecting cut never reaches zero — especially under correlated failures.

From a system design perspective, designing for partition tolerance is fundamentally about failure mode analysis (FMA): systematically identifying what could go wrong and what the system should do about it. FMEA (Failure Mode and Effects Analysis), borrowed from aerospace and automotive engineering, is directly applicable to distributed systems: for each component, ask "what if this fails?" and trace the effect through the system. Applied to network links, this discipline produces explicit partition handling logic rather than leaving it as an afterthought. Systems with explicit partition-handling strategies are not just more reliable — they are more operable, because the runbook for "network partition detected" is written before it is needed rather than improvised at 3am during an incident. Good design encodes this understanding into the architecture as explicit partition policies, quorum configurations, and conflict resolution strategies.

# Lore Conclusion

The storm clears. The enchanted bridge between the Academy wings shimmers back into existence. The Guild of Fault-Tolerant Enchanters reviews the split logs — some spells were submitted to both wings during the partition. The fencing tokens sort the truth from the duplicates in seconds. The conflict resolution runes handle the rest. No knowledge is lost. No false spell displaces a true one. The system has survived its first real partition — because it was designed to.

---
