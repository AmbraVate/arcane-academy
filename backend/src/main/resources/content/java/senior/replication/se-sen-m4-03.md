---
id: se-sen-m4-03
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m4
moduleTitle: "Module 4: Distributed Systems"
moduleGlyph: "🌐"
moduleSortOrder: 4
topicSlug: replication
topicTitle: "Replication"
topicSortOrder: 3
lesson: replication
title: "Replication"
sortOrder: 3
difficulty: 4
estimatedMinutes: 30
xpReward: 60
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [consistency_models]
integrationDomains: [design, mathematics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Describes single-leader replication and its failover challenge"
    - "Describes multi-leader replication and the write conflict problem"
    - "Describes leaderless replication and quorum reads/writes"
    - "Explains synchronous vs asynchronous replication trade-offs"
    - "Explains how read replicas are used for read scaling"
  keywords: [single-leader, multi-leader, leaderless, synchronous, asynchronous, replication lag, read replica, failover, quorum, conflict]
  modelAnswer: |
    Single-leader: one node accepts all writes; followers replicate.
    Simple, avoids write conflicts. Failover risk: if leader dies before followers sync,
    writes can be lost. Used by PostgreSQL, MySQL.

    Multi-leader: multiple nodes accept writes; each propagates to others.
    Higher write availability but write conflicts must be resolved.
    Used by multi-datacenter setups, CouchDB.

    Leaderless (Dynamo-style): any node accepts writes/reads; quorum determines validity.
    W + R > N for consistency (W=write quorum, R=read quorum, N=replicas).
    Used by Cassandra, DynamoDB, Riak.

    Synchronous replication: leader waits for follower acknowledgement before confirming
    to client. Durable but high latency. Asynchronous: leader confirms immediately,
    followers catch up. Low latency but risks data loss on leader failure.

    Read replicas: followers handle read queries; leader handles writes only.
    Scales read throughput but introduces replication lag — reads may be stale.
guidedSteps:
  - id: rep-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In a single-leader replication setup, the application write throughput is limited to the capacity of which node?
    inputConfig:
      options:
        - "The slowest follower replica"
        - "The leader node alone, since all writes go through it"
        - "The load balancer distributing writes"
        - "All replicas collectively, since they all process the write log"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The leader node alone, since all writes go through it"]
      rejectedFeedback: "In single-leader replication, only the leader accepts writes. Followers only replicate — they do not process client writes. This makes the leader a write bottleneck. Multi-leader or leaderless architectures are used when write throughput exceeds a single node's capacity."
    hint: "Only one node in this model accepts write requests."
    reflectionPrompt: "Single-leader is simple but the leader is the write throughput ceiling. This is a common reason systems migrate to multi-leader or sharding."
  - id: rep-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In Dynamo-style leaderless replication with N=3 replicas, to guarantee that reads always see the latest write you need W + R > N. If W=2, what is the minimum value of R that satisfies this quorum?
    inputConfig:
      placeholder: "number"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["2"]
      rejectedFeedback: "W + R > N means 2 + R > 3, so R > 1, therefore R must be at least 2. With W=2 and R=2, any read quorum must overlap with the write quorum, ensuring at least one node that participated in the write also participates in the read."
    hint: "W + R must be strictly greater than N=3. Solve for R given W=2."
    reflectionPrompt: "Quorum mathematics ensures read/write overlap. Reducing R or W increases availability and reduces latency, at the cost of consistency."
  - id: rep-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A PostgreSQL primary database has 3 read replicas. A developer adds code that writes a user's profile update to the primary and immediately queries a read replica for the updated profile. What problem might occur and how would you address it?
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [replication lag, stale, read-your-writes, primary, replica, routing, sticky]
      rejectedFeedback: "Replication lag means the read replica may not have applied the write yet, so the query returns the old profile data. Solutions include: (1) route reads-after-writes to the primary (read-your-writes), (2) track the write's WAL LSN and wait until the replica has caught up, (3) use a short delay before querying the replica, or (4) use synchronous replication for this operation."
    hint: "Replication from primary to replica is not instantaneous."
    reflectionPrompt: "Replication lag is the gap between what the leader has written and what a follower has applied. For user-facing operations, this is often unacceptable without mitigation."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which replication mode guarantees zero data loss on leader failure but at the cost of higher write latency?"
    options:
      - "Asynchronous replication"
      - "Synchronous replication"
      - "Semi-synchronous replication"
      - "Leaderless replication"
    correctIndex: 1
    feedback: "Synchronous replication waits for at least one follower to confirm the write before acknowledging to the client. This guarantees the write is on at least two nodes, so leader failure cannot lose that write — but adds network round-trip latency to every write."
  - type: MULTIPLE_CHOICE
    question: "In multi-leader replication, two users in different data centres simultaneously update the same record. This creates a:"
    options:
      - "Replication lag"
      - "Split-brain"
      - "Write conflict"
      - "Quorum violation"
    correctIndex: 2
    feedback: "A write conflict occurs when two leaders accept concurrent writes to the same record. The system must reconcile these conflicting versions using a strategy like last-write-wins, merge, or application-level conflict resolution."
retrieval:
  recall: "Name three replication topologies, and for each: who accepts writes, and what is the main failure mode or trade-off."
  explain: "Explain to a junior developer why adding more read replicas does not help write throughput."
  mistakeId:
    code: |
      // Failover script triggered on leader failure
      // Promotes follower with highest replica ID to leader
      Follower newLeader = followers.stream()
          .max(Comparator.comparing(Follower::getId))
          .orElseThrow();
      promoteToLeader(newLeader);
    answer: "Promoting the follower with the highest ID is arbitrary and dangerous. Failover should promote the follower that is most up to date (has applied the most replication log entries / has the highest LSN). Promoting a lagging follower as leader loses all writes that were on the old leader but not yet replicated to the new leader."
---

# Hook

A single scroll of spells exists in the Academy library. Excellent — until the library burns down. So the Academy creates copies across three towers. Now the question is not if the knowledge survives, but how all three copies stay in agreement as new spells are added every day.

# Lore Introduction

The Academy's Master Scribes understand that replication is both gift and curse. Multiple copies mean resilience; multiple copies mean divergence. How you choose to keep those copies synchronised determines everything about your system's durability, performance, and failure behaviour. There are three great schools of replication philosophy — and each senior mage must know when to apply each.

# Core Learning

## Concept Introduction

**Replication** is the process of maintaining copies of the same data on multiple nodes. It enables fault tolerance (if one node fails, others serve traffic), read scaling (distribute read queries across replicas), and geographic distribution (replicas in different regions serve local users with lower latency).

**The three replication topologies:**
1. **Single-leader (primary/replica)** — one leader accepts all writes; followers replicate
2. **Multi-leader (active-active)** — multiple leaders accept writes; propagate to each other
3. **Leaderless (Dynamo-style)** — any node accepts writes/reads; quorum determines validity

## Why It Matters

Replication is the foundation of high-availability databases. Almost every production database you use (PostgreSQL, MySQL, Cassandra, MongoDB) uses replication. Understanding the models helps you select databases, configure consistency, handle failover, and debug replication lag in production.

## Worked Examples

### Single-leader replication

```
Client → [Leader (primary)] → replicates → [Follower 1]
                                         → replicates → [Follower 2]
                                         → replicates → [Follower 3]

Reads:  can go to any follower (read replicas)
Writes: MUST go to leader only
```

```java
// Spring: configure primary datasource for writes, replica for reads
@Configuration
public class DataSourceConfig {
    @Bean
    @Primary
    DataSource primaryDataSource() {
        return DataSourceBuilder.create()
            .url("jdbc:postgresql://primary-host:5432/db")
            .build();
    }

    @Bean("replicaDataSource")
    DataSource replicaDataSource() {
        return DataSourceBuilder.create()
            .url("jdbc:postgresql://replica-host:5432/db")
            .build();
    }
}

// Repository using replica for reads
@Repository
public class UserRepository {
    @Autowired @Qualifier("replicaDataSource") DataSource replica;

    @Transactional(readOnly = true)
    public Optional<User> findById(String id) {
        // routes to read replica automatically in Spring
        return jpaRepo.findById(id);
    }
}
```

### Synchronous vs asynchronous replication

```
Synchronous:
  Client → Leader → [writes local] → waits for Follower ACK → confirms to client
  Latency: higher   Durability: guaranteed (data on 2+ nodes)

Asynchronous:
  Client → Leader → [writes local] → confirms to client immediately
                  → (background) replication to followers
  Latency: lower    Durability: leader failure before follower sync = data loss
```

PostgreSQL supports `synchronous_standby_names` to require synchronous replication to named standbys.

### Leaderless replication — Cassandra quorum

```java
// N=3, W=2, R=2 → W+R=4 > N=3 → reads always overlap with writes
// Write to 2 of 3 nodes; read from 2 of 3 nodes → at least 1 overlap

CqlSession session = CqlSession.builder()
    .addContactPoint(new InetSocketAddress("cassandra-host", 9042))
    .build();

// Write with quorum
session.execute(SimpleStatement.newInstance(
    "INSERT INTO users (id, name) VALUES (?, ?)", userId, name)
    .setConsistencyLevel(DefaultConsistencyLevel.QUORUM));

// Read with quorum — guaranteed to see latest write if W+R>N
ResultSet rs = session.execute(SimpleStatement.newInstance(
    "SELECT * FROM users WHERE id = ?", userId)
    .setConsistencyLevel(DefaultConsistencyLevel.QUORUM));
```

### Replication lag measurement

```sql
-- PostgreSQL: check replication lag on standbys
SELECT client_addr,
       state,
       sent_lsn,
       write_lsn,
       flush_lsn,
       replay_lsn,
       (sent_lsn - replay_lsn) AS lag_bytes
FROM pg_stat_replication;
```

High `lag_bytes` means replicas are behind — reads from replicas may be stale.

### Failover automation (Patroni / Spring)

```yaml
# Patroni configuration (PostgreSQL HA)
bootstrap:
  dcs:
    synchronous_mode: true          # require sync replication
    synchronous_mode_strict: false  # tolerate if no sync standby available
postgresql:
  parameters:
    synchronous_standby_names: "ANY 1 (replica1, replica2)"
```

## Common Mistakes

1. **Ignoring replication lag on read replicas.** Assuming read replicas are always up-to-date leads to stale data bugs. Always consider lag for user-facing reads.

2. **Promoting the wrong follower during failover.** Promoting a lagging follower discards writes that were on the old leader. Always promote the most-advanced follower (highest WAL LSN / replication offset).

3. **Multi-leader without conflict resolution.** Deploying multi-leader replication and assuming writes won't conflict. Any system that accepts concurrent writes to the same record needs an explicit conflict resolution strategy.

4. **Over-relying on synchronous replication for all writes.** Synchronous replication to a geographically distant replica adds hundreds of milliseconds of latency to every write. Use it selectively.

5. **Treating all reads as equal.** Some reads can tolerate stale data (analytics dashboards); others cannot (payment confirmations). Route them accordingly.

## Mental Model

Think of replication as a library with branch offices. The main library (leader) holds the master copy. Branches (followers) hold copies for local reading. When a new book arrives at the main library, it eventually arrives at the branches too — but there is a processing delay (replication lag). If the main library burns down (leader failure), which branch takes over? The one with the most complete copy (most up-to-date follower).

## Mini Summary

- Single-leader: one node accepts writes; followers replicate; simple but write-bottlenecked
- Multi-leader: multiple leaders; higher write availability but requires conflict resolution
- Leaderless: quorum-based (W+R>N); no single point of failure; tunable consistency
- Synchronous replication: durable but high-latency; asynchronous: fast but risks data loss on failure
- Read replicas scale read throughput; replication lag means reads may see stale data

# Guided Practice Quest

Work through the guided steps to practise quorum mathematics and identifying replication lag scenarios.

# Solo Practice Quest

Design a replication strategy for a global e-commerce platform with:
- A product catalogue (read-heavy, tolerates 5-second stale reads)
- An order service (writes must be durable; no lost orders)
- A user session store (latency-sensitive, loss of a session token is acceptable)

For each, recommend a replication topology and synchrony mode. Justify your choices in terms of the trade-offs between latency, durability, and consistency.

# Integration

**Connecting to Design — System Reliability and Mathematics — Probability Theory**

Replication is fundamentally a reliability engineering technique, and reliability is modelled mathematically using probability. If a single node has a probability p of failure in a given period, then N independent replicas reduce the probability of total data loss to p^N (assuming independent failures). This is the theoretical foundation for why replication improves durability. However, the independence assumption breaks down in practice: nodes in the same rack share a power supply; nodes in the same data centre share network infrastructure; nodes in the same cloud region share the same blast radius for regional failures. The mathematical model demands truly independent failure domains — different racks, different data centres, different cloud regions — to deliver the claimed reliability improvements.

From a system design perspective, replication is one of the first levers engineers reach for when designing for high availability. The topology choice — single-leader, multi-leader, leaderless — is a design decision that propagates throughout the system: it determines the failure modes you need to handle, the consistency guarantees you can offer, and the operational complexity of your runbooks. Good designs make replication topology an explicit architectural decision (documented in an ADR), rather than an implicit consequence of database choice. The relationship between the designer's intent (we need 99.99% availability) and the implementation mechanism (synchronous replication to 2 geographically distributed standbys) should be traceable and testable through chaos engineering.

# Lore Conclusion

The Master Scribe rolls up the parchment: "Three towers now hold the knowledge. The lead tower receives all new additions. The others follow. When the lead tower falls, we promote the most complete tower." The Academy's knowledge survives not because any single scroll is perfect, but because the system of scrolls is designed to endure. You now understand how to architect such a system.

---
