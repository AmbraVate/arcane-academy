---
id: de-sen-m2-01
school: engineering
domainId: data-engineering
tier: SENIOR
moduleId: de-sen-m2
moduleTitle: "Module 2: Distributed Data Systems"
moduleGlyph: "🌐"
moduleSortOrder: 2
topicSlug: replication
topicTitle: "Replication"
topicSortOrder: 1
lesson: replication
title: "Replication"
sortOrder: 1
difficulty: 4
estimatedMinutes: 35
xpReward: 75
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-sen-m1-04]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains synchronous vs asynchronous replication and their trade-offs
    - Describes streaming replication in PostgreSQL
    - Explains replication lag and its implications for read replicas
    - Describes leader election and failover in primary-replica setups
    - Identifies when to use synchronous vs asynchronous replication
  keywords: [replication, synchronous, asynchronous, streaming replication, WAL, replication lag, read replica, failover, leader election, primary, replica, hot standby, logical replication, pg_stat_replication, RPO, RTO]
  modelAnswer: |
    Replication copies data from a primary database to one or more replicas. Synchronous replication: the primary waits for the replica to confirm receipt before acknowledging the write — zero data loss (RPO=0) but higher write latency. Asynchronous replication: the primary acknowledges the write before the replica confirms — lower write latency but risk of data loss on primary failure (replication lag = potential data loss window). PostgreSQL streaming replication: replicas connect to the primary and stream the WAL continuously. Replication lag: the replica is behind the primary by N bytes/milliseconds — reads from the replica may return stale data. Read replicas: route read queries to replicas to reduce primary load. Failover: on primary failure, a replica is promoted to primary (manual or via Patroni/repmgr). RPO (Recovery Point Objective): maximum data loss tolerated. RTO (Recovery Time Objective): maximum downtime tolerated.
guidedSteps:
  - id: de-sen-m2-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      The Archive uses asynchronous replication to a read replica. The primary fails. The replica had 5 seconds of replication lag at the time of failure. What is the worst-case data loss?
    inputConfig:
      options:
        - "No data loss — asynchronous replication captures all writes before acknowledging them"
        - "5 seconds of committed transactions — writes in the last 5 seconds may not have reached the replica"
        - "All data since the last backup — replication lag doesn't cause data loss"
        - "No data loss — the replica automatically catches up when the primary recovers"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["5 seconds of committed transactions — writes in the last 5 seconds may not have reached the replica"]
      rejectedFeedback: "Asynchronous replication: the primary acknowledges writes immediately, then sends WAL to the replica. If the primary fails before the replica receives the last 5 seconds of WAL, those transactions are committed on the primary but never applied to the replica. When the replica is promoted to primary, those writes are permanently lost. This is the RPO (Recovery Point Objective) for asynchronous replication — the replication lag at failover time is the maximum data loss. For zero data loss (RPO=0): use synchronous replication (SYNCHRONOUS_COMMIT = on). Trade-off: synchronous replication increases write latency because the primary must wait for the replica to confirm WAL receipt before acknowledging the write to the client. For applications that can tolerate a few seconds of potential data loss (non-financial), asynchronous replication is appropriate. For financial systems, synchronous replication or multi-region synchronous writes are required."
    hint: "In asynchronous replication, what has the primary committed that the replica hasn't received yet when the primary fails?"
    reflectionPrompt: "For which types of data would you always require synchronous replication? For which is asynchronous acceptable?"
  - id: de-sen-m2-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In PostgreSQL, the view that shows the current replication lag between the primary and each replica (measured in bytes of WAL not yet sent) is called ________.
    inputConfig:
      placeholder: "pg_stat_replication"
    markingRule:
      matchMode: CONTAINS
      accepted: [pg_stat_replication, "pg_stat_replication view", "pg_stat_replication table"]
      rejectedFeedback: "pg_stat_replication (query on the primary): SELECT client_addr, state, sent_lsn, write_lsn, flush_lsn, replay_lsn, write_lag, flush_lag, replay_lag FROM pg_stat_replication; Key columns: sent_lsn: last WAL position sent to replica. flush_lsn: last position written and flushed to disk by replica. replay_lag: time between WAL flush on primary and replay on replica — the actual lag a read query would experience. A replica with replay_lag = '5 seconds' means reads from that replica see data 5 seconds behind the primary. Monitoring: alert when replay_lag > threshold (e.g., alert at 30 seconds, page at 5 minutes). On the replica: pg_stat_wal_receiver shows similar stats from the replica's perspective. pg_replication_slots shows logical replication slot lag — important because unconsumed slots prevent WAL cleanup and can cause disk exhaustion."
    hint: "This PostgreSQL system view, queried on the primary, shows the connection details and lag metrics for each replica."
    reflectionPrompt: "What happens to disk usage on the primary if a replication slot is created but the replica falls behind and doesn't reconnect?"
  - id: de-sen-m2-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why a user who just wrote data to the primary and then immediately reads from a read replica may not see their own write, and describe two strategies to handle this.
    inputConfig:
      minWords: 35
    markingRule:
      matchMode: CONTAINS
      accepted: [lag, delay, async, replica, primary, read-your-own-writes, sticky, route, session, read after write, eventual, consistency]
      rejectedFeedback: "Replication lag causes read-after-write inconsistency: (1) User writes a new loan to the primary (committed at t=0). (2) User immediately reads their loan list from the read replica (at t=0ms). (3) The replica has not yet applied that WAL segment — the new loan doesn't appear. The user thinks their checkout failed. Two strategies: (1) Read-your-own-writes (sticky session for writes): after a write, route the same user's reads to the primary for a window (e.g., 30 seconds). After the window, reads go back to replicas. Implemented via session state or a routing header. (2) Monotonic read consistency: track the LSN (Log Sequence Number) of the latest write by this user/session. Only send reads to a replica whose replay_lsn >= the user's write LSN. If no replica is caught up enough, fall back to the primary. Both approaches add application complexity — weigh against the value of read scaling."
    hint: "The write goes to primary; the read goes to a replica; there is a delay between when primary commits and when the replica has replayed that commit."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Logical replication in PostgreSQL differs from streaming replication in that:"
    options:
      - "Logical replication is faster and more reliable than streaming replication"
      - "Logical replication replicates specific tables or schemas and can replicate to a different PostgreSQL major version or to non-PostgreSQL databases"
      - "Logical replication uses a different physical storage format"
      - "Streaming replication cannot be used for read replicas"
    correctIndex: 1
    feedback: "Streaming replication (physical): copies the entire WAL byte-for-byte — the replica is an exact physical copy of the primary. Requires same PostgreSQL major version (same WAL format). Cannot replicate specific tables. Logical replication: replicates at the logical row-change level (INSERT, UPDATE, DELETE of specific tables). Can replicate specific tables or schemas. Can replicate to a different PostgreSQL major version (useful for zero-downtime major version upgrades). Can replicate to Kafka, Debezium, or other consumers via logical replication slots. Can have different indexes, schemas on the replica. Use streaming for: HA replicas (exact copies, fast failover). Use logical for: migration to a new major version, CDC pipelines, selective table replication to a different database."
  - type: MULTIPLE_CHOICE
    question: "Patroni is used in PostgreSQL high availability to:"
    options:
      - "Monitor slow queries and automatically create indexes"
      - "Manage automatic leader election and failover — promoting a replica to primary when the primary fails"
      - "Encrypt replication traffic between primary and replicas"
      - "Balance reads across multiple replicas based on lag"
    correctIndex: 1
    feedback: "Patroni is a PostgreSQL HA (High Availability) solution that: (1) Manages the primary/replica cluster state in a distributed configuration store (etcd, Consul, or ZooKeeper). (2) On primary failure, conducts leader election — the replica with the most current WAL is promoted to primary. (3) Reconfigures remaining replicas to follow the new primary. (4) Manages switchover (planned primary change) and failover (unplanned primary failure). Without Patroni: primary failure requires manual intervention — SSH to the replica, stop recovery mode, update connection strings. RTO (time to recover) = minutes to hours of manual work. With Patroni: automated failover in 15-30 seconds. Alternatives: Repmgr (simpler, less feature-rich), AWS RDS Multi-AZ (managed service equivalent), Citus, CockroachDB (multi-primary, no single point of failure)."
retrieval:
  recall: "Describe the complete PostgreSQL streaming replication setup: what configuration is needed on the primary, what configuration on the replica, and how to verify the replication is working with correct lag metrics."
  explain: "Explain RPO and RTO in the context of database replication. For each of three scenarios (financial transactions, library loans, analytics reports), state appropriate RPO and RTO values and the replication configuration that achieves them."
  mistakeId:
    code: |
      -- Primary PostgreSQL: postgresql.conf
      synchronous_commit = on
      synchronous_standby_names = 'replica1,replica2'
      
      -- Application: all reads go to primary, replicas are unused
      -- Replicas are configured but never queried
      
      -- Justification: "We need synchronous replication for data safety"
    answer: "Two problems: (1) Synchronous commit with two replicas and no reads from replicas wastes the investment. Synchronous replication makes every write wait for both replicas to acknowledge — this doubles (or more) write latency. If replicas are not serving reads, the latency cost provides no benefit. Synchronous commit to two replicas is appropriate for highest-durability financial systems where write latency is acceptable. For most systems, asynchronous replication with monitoring achieves adequate durability at lower latency. (2) Replicas configured but never queried: a replica consuming replication bandwidth and storage without serving reads provides only HA benefit (which synchronous replication already covers). Fix: route read traffic to replicas to reduce primary load. Use a connection pool like PgBouncer with read/write splitting, or application-level routing based on query type (JPQL @Transactional(readOnly=true) → replica, write operations → primary). Re-evaluate: do you actually need synchronous replication? What is the RPO requirement? If RPO > 0, switch to asynchronous and use the replicas for reads."
---

# Hook

Replication is the foundation of database resilience and read scalability. A single database node is a single point of failure — when it fails, the system fails. A replicated system can survive primary failure, distribute read load, and provide disaster recovery. The design choices — synchronous vs asynchronous, read replica routing, failover automation — determine the system's RPO, RTO, and the complexity of operating it under pressure.

# Lore Introduction

"The primary database was down for forty minutes last week," the Lead Data Engineer said. "The archive system was inaccessible the entire time." The Senior Engineer reviewed the configuration. "No read replicas. No failover automation. When the primary went down, we waited for a DBA to manually restore it." The Lead Data Engineer looked at the impact report. "Six thousand members couldn't access the system. Loan returns were delayed. Late fees accrued incorrectly." The Senior Engineer began the design review. "We need replication — a hot standby that can take over automatically. And we need read replicas to reduce primary load so the primary is less likely to fail under query pressure. Today: how replication works, the trade-offs between synchronous and asynchronous, and how to automate failover."

# Core Learning

## Concept Introduction

### Streaming Replication Architecture

```
Streaming Replication (Physical):

  Primary (Read/Write)          Replica (Read-only hot standby)
  ┌─────────────────────┐       ┌────────────────────────┐
  │  Write queries      │       │  Read queries (optional)│
  │  WAL writer         │──────►│  WAL receiver          │
  │  postgresql.conf:   │  WAL  │  In recovery mode      │
  │    wal_level=replica│stream │  Applies WAL to disk   │
  │    max_wal_senders=5│       └────────────────────────┘
  └─────────────────────┘
       │
       └──► pg_stat_replication (monitor lag)

Setup on primary (postgresql.conf):
  wal_level = replica
  max_wal_senders = 5
  synchronous_commit = off  # asynchronous (or 'on' for synchronous)

Setup on replica (recovery.conf or postgresql.conf):
  primary_conninfo = 'host=primary port=5432 user=replication_user'
  hot_standby = on  # allows read queries on the replica
```

### Synchronous vs Asynchronous

```
ASYNCHRONOUS (default):
  Client ──write──► Primary: commit, ACK to client ──► later sends WAL ──► Replica
  Latency: normal commit time (1-5ms typical)
  RPO: up to replication_lag at time of failure
  Good for: most applications, best performance

SYNCHRONOUS:
  Client ──write──► Primary: sends WAL ──► Replica confirms ──► commits, ACK to client
  Latency: commit_time + network_RTT + replica_write_time (10-50ms typical)
  RPO: 0 (zero data loss — replica always has all committed data)
  Good for: financial transactions, legal records, audit trails

  postgresql.conf:
  synchronous_commit = on
  synchronous_standby_names = 'FIRST 1 (replica1, replica2)'
  # 'FIRST 1': wait for the first available replica to confirm
  # 'ANY 1 (r1, r2, r3)': wait for ANY 1 of the 3 to confirm

  synchronous_commit = remote_write  # faster than on — waits for OS cache, not fdatasync
```

### Read Replica Routing

```java
// Application-level routing: writes → primary, reads → replica
// Spring Boot with multiple DataSources:
@Configuration
public class DataSourceRoutingConfig {

    @Bean("primaryDataSource")
    public DataSource primaryDataSource() {
        // Primary connection pool
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://primary:5432/archive");
        return new HikariDataSource(config);
    }

    @Bean("replicaDataSource")
    public DataSource replicaDataSource() {
        // Read replica connection pool
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://replica:5432/archive");
        return new HikariDataSource(config);
    }
}

// Repository layer: @Transactional(readOnly=true) → replica
// The routing logic checks the transaction's readOnly flag
// and routes to the appropriate DataSource

// PgBouncer alternative: connection-pooling proxy with read/write splitting
// Read queries (SELECT) → replica pool
// Write queries (INSERT/UPDATE/DELETE) → primary pool
```

### Monitoring Replication

```sql
-- On primary: check replica status and lag
SELECT
    client_addr,
    state,            -- 'streaming', 'catchup', 'backup', 'offline'
    write_lag,        -- time to write WAL to replica OS cache
    flush_lag,        -- time to flush WAL to replica disk
    replay_lag        -- time until replica applies (user-visible lag)
FROM pg_stat_replication;

-- Replication lag in bytes:
SELECT
    pg_wal_lsn_diff(pg_current_wal_lsn(), replay_lsn) AS lag_bytes,
    replay_lag
FROM pg_stat_replication;

-- Alert thresholds:
-- Warning: replay_lag > 30 seconds
-- Critical: replay_lag > 5 minutes (reads from replica are significantly stale)
-- Emergency: replica disconnected (state != 'streaming')

-- On replica: check recovery status
SELECT
    pg_is_in_recovery(),           -- TRUE if replica
    pg_last_wal_receive_lsn(),     -- last WAL received
    pg_last_wal_replay_lsn(),      -- last WAL applied
    now() - pg_last_xact_replay_timestamp() AS replication_delay;
```

## Common Mistakes

- **No replication at all**: a single-node database is a single point of failure. Any outage = full downtime. At minimum, deploy one hot standby replica.
- **Read replicas with no replication lag monitoring**: without lag alerts, a replica can fall hours behind and serve significantly stale data without anyone knowing. Always monitor and alert on `replay_lag`.
- **Replication slots that never consume**: an unconsumed logical replication slot prevents WAL cleanup — the WAL directory grows unbounded, eventually exhausting disk space. Monitor slot lag and set `max_slot_wal_keep_size` to limit WAL retention.
- **Manual failover without rehearsal**: failover procedures that have never been tested will fail under pressure. Rehearse failover quarterly in a non-production environment.

## Mental Model

Database replication is like maintaining a shadow copy of a critical document. The primary document (primary database) is the authoritative source. Shadow copies (replicas) are kept updated in near-real-time. If the original is lost (primary failure), you promote the most current shadow copy to the authoritative version. The lag between the original and the shadow determines how current the shadow is — and how much work you lose if the original is destroyed before the shadow catches up. Synchronous replication: the shadow is updated at the same time as the original — guaranteed current, but takes longer because both copies must confirm the update.

## Mini Summary

- ✔ Streaming replication: replicas consume WAL from the primary in near-real-time
- ✔ Synchronous: zero data loss (RPO=0), higher write latency — use for financial/legal data
- ✔ Asynchronous: lower latency, small RPO window — appropriate for most applications
- ✔ Replication lag: monitor `replay_lag` in `pg_stat_replication`; alert on high lag
- ✔ Read replicas: route `SELECT` queries to replicas to reduce primary load
- ✔ Read-your-own-writes: route post-write reads to primary briefly to avoid stale read anomalies
- ✔ Patroni/repmgr: automate failover — avoid manual promotion under pressure

# Guided Practice Quest

Work through the guided steps to configure asynchronous streaming replication between primary and one replica, verify the replication lag using `pg_stat_replication`, and implement read replica routing in a Spring Boot application using the `@Transactional(readOnly=true)` pattern.

# Solo Practice Quest

Design and implement the replication architecture for the Archive's high-availability setup. Tasks: (1) Design a three-node cluster (one primary, two replicas): specify synchronous_commit configuration, `synchronous_standby_names`, and which replica is synchronous vs asynchronous; (2) Write the complete postgresql.conf changes needed on the primary and replica configuration; (3) Implement read replica routing in the Archive's Spring Boot application — which repository methods should route to replicas, which must go to primary, and how; (4) Write a replication lag monitoring query and define alert thresholds (warning/critical/emergency); (5) Document the failover procedure with Patroni: what triggers failover, how long it takes, what the application must do during failover, and how you verify the new primary is accepting writes; (6) The archive has a weekly full-text search report that queries 2M rows and takes 3 minutes. Design how to run this on a replica without impacting the primary.

# Integration

**Mathematics**: Replication consistency can be formalised through the lens of distributed systems theory. The replica's state at time t is a function of the primary's state and the WAL segments received: R(t) = f(P(t - lag)). Replication lag is the temporal offset between the primary state and the replica state. In the worst case (primary failure at time t_fail), the replica has state P(t_fail - lag_max) where lag_max is the maximum lag at failure. The data loss = |P(t_fail) - P(t_fail - lag_max)| = the set of transactions committed in the window [t_fail - lag_max, t_fail]. For a workload of W writes/second and lag L seconds, expected data loss = W × L transactions. This formalises the RPO calculation: RPO = lag_max. For synchronous replication, lag_max → 0 (bounded by network latency), giving RPO → 0.

**Sciences (Telecommunications — Redundancy Engineering)**: PostgreSQL streaming replication implements a redundancy engineering principle from telecommunications: N+1 redundancy. In telecom, N+1 means N working components plus one spare — the spare can absorb the failure of any single working component. A primary + one replica is N=1, +1 = one spare — the system survives any single node failure. For higher availability (N+2): primary + two replicas — can survive two simultaneous failures. The telecommunications concept of hot standby vs cold standby applies directly: a hot standby replica is actively receiving WAL and can take over in seconds (low RTO). A cold standby (backup on disk) must be restored from backup before it can serve traffic (high RTO). The trade-off is operational cost: hot standby consumes resources continuously; cold standby is cheaper but slower to recover.

# Lore Conclusion

"Patroni cluster deployed," the Senior Engineer reported. "One primary, two asynchronous replicas, one synchronous. The synchronous replica guarantees RPO=0 for financial transactions." The Lead Data Engineer reviewed the failover test results. "Failover time?" The Senior Engineer showed the test log. "18 seconds from primary failure to new primary accepting writes. Patroni promoted the synchronous replica." The Lead nodded. "And the read routing?" The Senior Engineer opened the Spring Boot config. "Repository methods annotated with `@Transactional(readOnly=true)` route to the replica pool. Primary handles writes only. Primary CPU at 25% — down from 65%." The Lead Data Engineer reviewed the lag monitoring dashboard. "Replication lag alerts configured. Last week's incident — primary under query load — can no longer happen." She closed the monitoring view. "Next: when replication reaches the limits of vertical scaling — sharding."

---
