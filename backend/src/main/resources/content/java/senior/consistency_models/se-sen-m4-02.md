---
id: se-sen-m4-02
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m4
moduleTitle: "Module 4: Distributed Systems"
moduleGlyph: "🌐"
moduleSortOrder: 4
topicSlug: consistency_models
topicTitle: "Consistency Models"
topicSortOrder: 2
lesson: consistency_models
title: "Consistency Models"
sortOrder: 2
difficulty: 4
estimatedMinutes: 30
xpReward: 60
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [cap_theorem]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Defines strong consistency and gives an example system"
    - "Defines eventual consistency and its typical use case"
    - "Explains read-your-writes and why it matters to users"
    - "Explains causal consistency and its relationship to Lamport clocks"
    - "Describes what a CRDT is and gives a practical example"
  keywords: [strong consistency, eventual consistency, read-your-writes, monotonic reads, causal consistency, CRDT, linearisable, vector clock, replica]
  modelAnswer: |
    Strong (linearisable) consistency: every read reflects all completed writes.
    The system appears as a single node. Example: Google Spanner, single-leader Postgres.

    Eventual consistency: replicas will converge to the same value if writes stop.
    Fast but can return stale data. Example: Cassandra with CL=ONE, DynamoDB.

    Read-your-writes: a client always sees its own writes, even if other clients may see
    stale data. Critical for user-facing operations (post a comment, see it immediately).

    Monotonic reads: successive reads never return older data than a previous read.
    Without it, a user could see time go backwards.

    Causal consistency: operations that are causally related are seen in the same order
    by all clients. Replies appear after the post they respond to.
    Implemented via vector clocks or Lamport timestamps.

    CRDT (Conflict-free Replicated Data Type): a data structure that can be merged
    from any two replicas without conflicts. Example: G-Counter (grow-only counter)
    used in Cassandra counters, collaborative editors (e.g., operational transform).
guidedSteps:
  - id: cm-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A user posts a comment, then immediately refreshes the page. The comment does not appear. Which consistency guarantee has been violated?
    inputConfig:
      options:
        - "Strong consistency — the comment should be globally visible immediately"
        - "Read-your-writes — the user should always see their own recent writes"
        - "Monotonic reads — the data should never go backwards"
        - "Causal consistency — the comment should appear before any replies"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Read-your-writes — the user should always see their own recent writes"]
      rejectedFeedback: "Read-your-writes (or read-your-own-writes) guarantees that a client always sees its own writes in subsequent reads. This is a weaker guarantee than strong consistency but is the minimum users expect for their own actions."
    hint: "Strong consistency is global; this guarantee is per-client, per-session."
    reflectionPrompt: "Many social media platforms implement read-your-writes by routing a user's reads to the same replica they wrote to."
  - id: cm-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A data structure that can be automatically merged across replicas without coordination, because its merge operation is commutative, associative, and idempotent, is called a ___ (abbreviation).
    inputConfig:
      placeholder: "4-letter abbreviation"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["CRDT", "crdt"]
      rejectedFeedback: "CRDT stands for Conflict-free Replicated Data Type. Because merge is commutative (order doesn't matter), associative (grouping doesn't matter), and idempotent (applying twice = applying once), replicas can merge without coordination or conflict resolution."
    hint: "The C stands for Conflict-free."
    reflectionPrompt: "CRDTs appear in collaborative tools (Google Docs), counters in distributed systems, and eventually-consistent shopping carts."
  - id: cm-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain causal consistency using a real-world example from a social media platform, and explain how it differs from strong consistency.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [causal, reply, post, order, happen-before, strong consistency, global, all clients]
      rejectedFeedback: "Causal consistency ensures that causally related operations are seen in causal order by all clients. Example: if Alice posts 'Question?' and Bob replies 'Yes!', all clients must see Alice's post before Bob's reply. However, causally unrelated operations can appear in any order. Strong consistency is stricter — it requires ALL operations to be seen in the same order by ALL clients, not just causally related ones."
    hint: "Causal: 'happens-before' relationships are respected. Strong: a global total order is respected."
    reflectionPrompt: "Causal consistency is often the 'sweet spot' — strong enough for most user-facing applications, weaker (and thus more available) than linearisability."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which consistency model ensures that once a client reads a value v, all subsequent reads by that client return v or a value newer than v?"
    options:
      - "Read-your-writes"
      - "Monotonic reads"
      - "Strong consistency"
      - "Eventual consistency"
    correctIndex: 1
    feedback: "Monotonic reads guarantees that time does not appear to go backwards for a client — once you see a value, you will not see an older value on subsequent reads. Read-your-writes is specifically about seeing your own writes."
  - type: MULTIPLE_CHOICE
    question: "Google Spanner achieves strong (external) consistency across globally distributed nodes using:"
    options:
      - "Eventual consistency with conflict resolution"
      - "Optimistic locking and version vectors"
      - "TrueTime — atomic clocks and GPS receivers to bound clock uncertainty"
      - "Master-slave replication with synchronous commits"
    correctIndex: 2
    feedback: "Google Spanner uses TrueTime (atomic clocks + GPS) to bound clock skew to a few milliseconds, enabling globally consistent external consistency with commit-wait to ensure timestamps are in the past before committing."
retrieval:
  recall: "List five consistency models from strongest to weakest and give one sentence definition of each."
  explain: "Explain to a junior developer why eventual consistency is not the same as 'data might be wrong forever' — and what guarantees it does provide."
  mistakeId:
    code: |
      // Shopping cart service — uses DynamoDB (eventual consistency by default)
      public void addToCart(String userId, Item item) {
          cartTable.putItem(new PutItemRequest()
              .withTableName("carts")
              .withItem(buildItem(userId, item)));
      }

      public List<Item> getCart(String userId) {
          // Immediately after addToCart, this might return a cart without the new item
          return cartTable.getItem(new GetItemRequest()
              .withTableName("carts")
              .withKey(Map.of("userId", userId)));
      }
    answer: "DynamoDB defaults to eventually consistent reads, so getCart() called immediately after addToCart() may not return the just-added item. For user-facing operations, strong consistent reads should be used (ConsistentRead=true), or the cart should be routed to the same replica for read-your-writes semantics."
---

# Hook

Two scholars at different ends of the Academy's library network update the same magical catalogue entry simultaneously. When students across the realm read the catalogue, some see version A, some see version B, and some briefly see version A then version B then back to A. How much confusion is acceptable? How much consistency does your use case truly need? These questions define the spectrum of consistency models.

# Lore Introduction

The High Archivist of the Academy teaches that perfect knowledge is a luxury affordable only to those who guard a single scroll. In a world of replicated archives, the only question is: how much uncertainty can your readers tolerate, and what invariants must you absolutely protect? The answer varies by domain — and a senior mage chooses the right consistency level with surgical precision.

# Core Learning

## Concept Introduction

A **consistency model** defines the guarantees a distributed data store makes about the order and visibility of reads and writes. Models form a spectrum from strong (all clients see the same state) to weak (replicas converge eventually).

**Consistency model hierarchy (strongest to weakest):**
1. **Linearisable (strong)** — operations appear instantaneous; total global order
2. **Sequential** — all clients see the same operation order (not necessarily real-time)
3. **Causal** — causally related operations appear in order; unrelated operations may differ
4. **Read-your-writes** — a client always sees its own writes
5. **Monotonic reads** — a client never sees older data than it has already seen
6. **Eventual consistency** — replicas will converge if writes stop; no timing guarantee

## Why It Matters

Choosing a consistency model is a direct business decision. Strong consistency is easier to reason about but costs latency and availability. Eventual consistency is fast but requires careful application-level handling of stale reads. The wrong choice leads to either a poor user experience (stale data) or unnecessary slowness (over-consistent).

## Worked Examples

### Strong (linearisable) consistency in practice

```java
// Google Spanner / CockroachDB: globally consistent reads
// Application code is simple — reads always return latest data
User user = spannerClient.read("users", Key.of(userId));
// If another node just wrote a new email, this WILL see it.
// Cost: higher latency due to quorum coordination across global nodes
```

### Eventual consistency — Cassandra example

```java
// Cassandra CL=ONE: fastest read, potentially stale
Row row = session.execute(
    QueryBuilder.select().from("products")
        .where(eq("id", productId))
        .setConsistencyLevel(ConsistencyLevel.ONE) // only 1 replica responds
).one();

// CL=QUORUM: majority must agree — stronger but slower
Row consistentRow = session.execute(
    QueryBuilder.select().from("products")
        .where(eq("id", productId))
        .setConsistencyLevel(ConsistencyLevel.QUORUM)
).one();
```

### Read-your-writes implementation strategy

```java
// Strategy 1: sticky sessions (route user's reads to same replica they wrote to)
// Strategy 2: version token
public WriteResult addItem(String userId, Item item) {
    WriteResult result = repository.write(userId, item);
    return result; // result includes a version token (e.g., LSN from Postgres)
}

public List<Item> getItems(String userId, String minVersion) {
    // Wait until replica has applied writes up to minVersion
    return repository.readAfterVersion(userId, minVersion);
}
```

### Causal consistency — vector clocks

```java
// Simplified: each operation carries a version vector
// "Reply to post X" is causally dependent on "Post X"
// System ensures post X is visible before reply is

Message reply = new Message()
    .setCausedBy(originalPost.getVersion()) // causal dependency declared
    .setContent("Great question!");
// The store ensures no client sees the reply before the original post
```

### CRDT — G-Counter (grow-only counter)

```java
// Each node maintains its own count; merge takes the max per node
// No coordination needed — merge is always safe
class GCounter {
    private final Map<String, Long> counts = new HashMap<>();
    private final String nodeId;

    public void increment() {
        counts.merge(nodeId, 1L, Long::sum);
    }

    public long value() {
        return counts.values().stream().mapToLong(Long::longValue).sum();
    }

    public GCounter merge(GCounter other) {
        GCounter merged = new GCounter(nodeId);
        Set<String> allNodes = new HashSet<>();
        allNodes.addAll(this.counts.keySet());
        allNodes.addAll(other.counts.keySet());
        for (String node : allNodes) {
            merged.counts.put(node,
                Math.max(this.counts.getOrDefault(node, 0L),
                         other.counts.getOrDefault(node, 0L)));
        }
        return merged;
    }
}
```

This enables distributed counters (page views, likes) without coordination.

## Common Mistakes

1. **Assuming eventual consistency means data is eventually correct.** It means replicas will *converge* — but if conflicting writes occur, the system needs a conflict resolution strategy (last-write-wins, CRDTs, or application-level merge).

2. **Using strong consistency everywhere.** Strong consistency adds coordination overhead. For analytics, caches, and non-critical reads, eventual consistency is often correct and faster.

3. **Confusing read-your-writes with consistency for other users.** Read-your-writes only guarantees the *writing* client sees their own data. Other clients may still see stale data.

4. **Ignoring monotonic reads.** Without monotonic reads, a client that reads from different replicas may observe time going backwards — seeing a deletion, then the deleted item re-appearing on the next read.

5. **Treating consistency as a database property only.** Application caches (Redis, CDN) also have consistency models. A strongly consistent database with an eventually consistent cache in front of it gives you eventual consistency overall.

## Mental Model

Think of consistency models as messaging guarantees in a team:
- **Strong**: Every team member hears every decision immediately in perfect order
- **Causal**: You always hear about a decision before you hear about its consequences
- **Read-your-writes**: You always remember your own contributions to the discussion
- **Monotonic reads**: You never forget something you've already learned
- **Eventual**: The whole team will eventually know everything, but not necessarily at the same time

## Mini Summary

- Strong (linearisable) consistency provides a global total order; slowest but easiest to reason about
- Eventual consistency offers high availability and low latency; replicas converge when writes stop
- Read-your-writes and monotonic reads are session-level guarantees essential for good UX
- Causal consistency preserves happen-before relationships across replicas
- CRDTs enable conflict-free merging of replica state without coordination

# Guided Practice Quest

Work through the guided steps to practise identifying consistency model violations and selecting appropriate models for given use cases.

# Solo Practice Quest

You are designing a social feed system where users post updates and follow each other. Identify which consistency model is minimally sufficient for each of these operations and justify your choice:

1. A user posts an update and immediately views their profile
2. A user's follower sees the update in their feed
3. A user edits their display name, which appears on all their posts
4. Likes on a post are counted across all replicas

For each, explain the cost of choosing a stronger model and whether it is worth it.

# Integration

**Connecting to Mathematics — Partial Orders and Philosophy — Epistemic Uncertainty**

Causal consistency is directly modelled by partial order theory in mathematics. A partial order is a set with a relation that is reflexive, antisymmetric, and transitive — but not every pair of elements needs to be comparable. Operations in a distributed system form exactly this structure: some are causally ordered (A happened-before B), others are concurrent (neither caused the other). Lamport clocks and vector clocks are mathematical tools for computing this partial order without global synchronisation. CRDTs are defined mathematically as join-semilattices — structures where any two elements have a unique least upper bound (the merge result), guaranteeing convergence.

Philosophically, the consistency spectrum maps onto the philosophy of epistemology — the study of knowledge and belief. Strong consistency asserts that all agents share the same knowledge at all times (an impossibly demanding epistemic standard in a physically distributed world). Eventual consistency acknowledges that knowledge propagates through space at finite speeds, and that agents will have different beliefs at different times but will converge. The choice of consistency model is therefore a statement about how much epistemic divergence between agents your system's users can tolerate. High-stakes domains (financial transactions, medical records) demand strong epistemic alignment; social media feeds, analytics dashboards, and recommendation engines function correctly with significant divergence. Senior engineers make this choice deliberately, guided by the actual needs of users rather than a reflexive preference for stronger guarantees.

# Lore Conclusion

The High Archivist closes the great tome. "The strongest wards are the heaviest," she says. "A scholar guarding a single candle needs no fortress. Choose your consistency with the same precision you choose your spells — appropriate to the danger, proportionate to the cost." You leave the archive knowing that the right consistency model is not the strongest one, but the right one for your problem.

---
