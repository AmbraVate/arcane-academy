---
id: de-sen-m7-03
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m7
moduleTitle: "Module 7: Database Reliability"
moduleGlyph: "🛡️"
moduleSortOrder: 7
topicSlug: high_availability
topicTitle: "High Availability"
topicSortOrder: 3
lesson: 3
title: "High Availability: Eliminating Single Points of Failure"
sortOrder: 3
difficulty: 4
estimatedMinutes: 35
xpReward: 75
practiceType: GUIDED_AND_SOLO
questType: RETRIEVAL_CHALLENGE
retrievalWeight: 0.6
questTypes:
  - guided
  - solo
  - retrieval
prerequisites:
  - de-sen-m7-02
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines availability in terms of nines and maps them to downtime"
    - "Explains primary-standby failover and the split-brain problem"
    - "Describes connection pooling and why it is essential for high-availability systems"
    - "Identifies the role of health checks and load balancers in HA architectures"
  keywords:
    - availability nines
    - failover
    - split-brain
    - Patroni
    - connection pooling
    - PgBouncer
    - health check
    - load balancer
  modelAnswer: |
    Availability is measured in nines: 99.9% (three nines) = 8.7 hours downtime/year; 99.99% (four nines) = 52 minutes/year; 99.999% (five nines) = 5 minutes/year. Each additional nine is roughly 10x harder to achieve. SLAs define the target; architecture must achieve it.
    Primary-standby failover: when the primary fails, a standby is promoted to primary. The split-brain problem occurs when a primary and standby both believe they are primary (e.g. network partition makes each think the other is dead). Both accepting writes causes data divergence that is hard to reconcile. Patroni prevents split-brain via distributed consensus (etcd/Consul): a node can only become primary if a quorum of consensus nodes agree. Fencing (STONITH) kills the old primary to prevent it writing after demotion.
    Connection pooling (PgBouncer): applications open connections to PgBouncer; PgBouncer maintains a fixed pool of connections to PostgreSQL. During failover, the application's PgBouncer config updates; existing application connections reconnect transparently. Without pooling, a failover causes thousands of application connections to simultaneously reconnect to PostgreSQL, overwhelming the new primary.
    Health checks: load balancers and failover tools continuously probe database availability. HAProxy can route read traffic to replicas and write traffic to primary based on health check results.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "A network partition isolates the primary from the replica and from the consensus cluster (etcd). Patroni detects this. What should happen and why?"
    options:
      - "The replica promotes itself to primary — it is still reachable and the primary is isolated"
      - "The primary continues accepting writes — it was primary first and should remain so"
      - "Both nodes step down and refuse writes until quorum is restored — preventing split-brain"
      - "The primary and replica operate independently; writes are merged when the partition heals"
    correctIndex: 2
    explanation: "Without quorum, neither node should accept writes. Patroni uses etcd/Consul consensus: a node can only be primary if a majority of consensus nodes agree. If the primary loses contact with etcd, Patroni demotes it — it enters read-only mode. The replica cannot promote without etcd quorum either. Both nodes wait until the partition heals and quorum is restored. This is the correct split-brain prevention: brief unavailability is preferable to data divergence."
  - type: FILL_BLANK
    question: "PgBouncer operates in ___ mode for most OLTP workloads, returning a database connection to the pool after each transaction rather than holding it for the session's lifetime."
    answer: "transaction pooling"
    explanation: "PgBouncer's transaction pooling mode assigns a backend connection for the duration of a transaction, then returns it to the pool. A 1000-connection application can be served by 20–50 actual PostgreSQL connections. This dramatically reduces PostgreSQL connection overhead (each PostgreSQL connection is a forked process consuming ~5MB RAM and CPU). Session pooling assigns a connection per session — simpler but fewer connections saved."
  - type: SHORT_TEXT
    question: "Your PostgreSQL primary has 200 application connections directly. A failover occurs and the replica is promoted. What happens to those 200 connections and how does PgBouncer improve this situation?"
    modelAnswer: "Without PgBouncer: all 200 connections drop simultaneously. Applications receive connection errors and retry, generating a thundering herd of reconnect attempts to the newly promoted primary — which is already under load from promotion. Many connections may fail or timeout. With PgBouncer: the 200 application connections are to PgBouncer, not PostgreSQL. PgBouncer's config is updated (via Patroni callback or patroni-controlled HAProxy) to point to the new primary. Application connections to PgBouncer are queued briefly during switchover; PgBouncer reconnects its backend pool to the new primary. Applications experience a brief pause (seconds) rather than errors and reconnect storms."
microCheckpoint:
  question: "What is split-brain in a database HA cluster and how does Patroni prevent it?"
  answer: "Split-brain is when two nodes both believe they are the primary and accept writes simultaneously, causing data divergence. Patroni prevents it using distributed consensus (etcd/Consul): a node can only be promoted if a quorum of consensus nodes agree. A primary that loses quorum contact is demoted to read-only. No quorum = no writes."
retrieval:
  recall: "What does 99.99% availability mean in hours of downtime per year?"
  explain: "Explain why connection pooling is important specifically during a failover event."
  mistakeId: "ha-split-brain"
---

# The Unreachable Primary

At 02:17 on a Tuesday, the primary database node's network interface failed. Patroni detected the failure within 10 seconds. The etcd quorum confirmed the primary was unreachable. The standby was promoted. PgBouncer updated its connection target. By 02:17:28, writes were flowing to the new primary. Eleven seconds of downtime, zero data loss. "That's three nines in a single event," the Lead Data Engineer said. The Senior Engineer closed the alert. This was what high availability looked like working.

# Measuring Availability

Availability is expressed as the percentage of time a system is operational.

| Availability | Downtime/Year | Downtime/Month | Architecture Required |
|---|---|---|---|
| 99% (two nines) | 87.6 hours | 7.3 hours | Single instance |
| 99.9% (three nines) | 8.76 hours | 43.8 minutes | Primary + standby |
| 99.99% (four nines) | 52.6 minutes | 4.4 minutes | HA cluster + automation |
| 99.999% (five nines) | 5.26 minutes | 26 seconds | Active-active multi-region |

Each additional nine is roughly 10× harder and more expensive to achieve. Align the target with the business impact of downtime: a 1-hour outage costing €50k justifies more investment than one costing €500.

## Primary-Standby Architecture

```
┌──────────────────────────────────────────────────────┐
│              Applications                            │
└──────┬───────────────────────────────────────────────┘
       │ connect to
┌──────▼──────────────────────────────────────────────┐
│              PgBouncer (connection pool)              │
│         writes → primary  reads → replicas           │
└──────┬──────────────────────────┬────────────────────┘
       │                          │
┌──────▼──────┐            ┌──────▼──────┐
│  Primary    │──streaming─▶  Standby 1  │
│  (read/write)│  replication│  (read-only)│
└──────┬──────┘            └─────────────┘
       │                          
       │  Patroni manages failover
┌──────▼──────────────────────────────────────────────┐
│  etcd / Consul (distributed consensus)               │
│  3 nodes — quorum = 2 agree                          │
└─────────────────────────────────────────────────────┘
```

## Patroni: Automated Failover

Patroni manages the primary-standby lifecycle: leader election, failover, and topology changes.

```yaml
 # patroni.yml (simplified)
scope: consortium-pg
namespace: /db/
name: pg-node-1

restapi:
  listen: 0.0.0.0:8008

etcd:
  hosts: etcd1:2379,etcd2:2379,etcd3:2379

postgresql:
  listen: 0.0.0.0:5432
  data_dir: /var/lib/postgresql/data
  pg_hba:
    - host replication replicator 0.0.0.0/0 md5
  parameters:
    wal_level: replica
    hot_standby: "on"
    max_wal_senders: 10
    synchronous_commit: "on"

bootstrap:
  dcs:
    ttl: 30                    # leader lease: 30 seconds
    loop_wait: 10              # health check interval
    retry_timeout: 10
    maximum_lag_on_failover: 1048576  # don't promote if > 1MB lag
```

### Failover Timeline
```
T+00s: Primary network interface fails
T+10s: Patroni health check timeout on primary; standby detects
T+12s: Patroni acquires leader lock in etcd (quorum)
T+14s: Old primary's lease expires; it steps down (or is fenced)
T+15s: Standby runs pg_ctl promote — becomes read/write
T+16s: Patroni callback fires: update HAProxy/PgBouncer config
T+18s: PgBouncer reconnects to new primary
T+18s: Application writes succeed on new primary
Total downtime: 18 seconds
```

## Split-Brain Prevention

The greatest risk in HA: two nodes simultaneously believe they are primary and accept writes.

```
Scenario (without Patroni):
  Network partition: primary and standby cannot see each other
  Primary: "I'm still primary, I'll keep accepting writes"
  Standby: "Primary is dead, I'll promote myself"
  Result: TWO primaries accepting divergent writes → unrecoverable split-brain

Patroni prevention:
  Leader lease held in etcd (distributed consensus)
  Primary must renew lease every TTL seconds (30s default)
  If primary cannot reach etcd quorum → lease expires → primary steps down
  Standby can only promote if it can acquire the lease from etcd quorum
  Quorum = majority of etcd nodes (2 of 3)
  → Only ONE node can hold the lease at any time
```

**STONITH (Shoot The Other Node In The Head)**: Fencing mechanism that physically powers off or network-isolates the old primary before promoting the standby — preventing a "zombie primary" that can't reach etcd but can still write to disk.

## Connection Pooling: PgBouncer

```
Without PgBouncer:
  1000 app threads × 1 PostgreSQL connection each
  = 1000 PostgreSQL processes
  = ~5GB RAM for connections alone
  = Slow connection establishment (~50ms each)
  
With PgBouncer (transaction mode):
  1000 app threads → PgBouncer → 50 PostgreSQL connections
  = 250MB RAM
  = App connects to PgBouncer in <1ms (persistent connection)
  = PostgreSQL connections reused efficiently
```

```ini
 # pgbouncer.ini
[databases]
consortium_db = host=patroni-primary port=5432 dbname=consortium_db

[pgbouncer]
pool_mode = transaction
max_client_conn = 1000
default_pool_size = 50
max_db_connections = 100
server_idle_timeout = 600
```

During failover: Patroni triggers a callback that updates PgBouncer's target from old primary to new primary. Application connections reconnect transparently.

## Health Checks and Load Balancing

HAProxy routes read traffic to replicas and write traffic to the primary using Patroni's REST API health checks.

```
HAProxy configuration (simplified):
  
  backend postgres-primary
    option httpchk GET /primary          ← Patroni returns 200 if this node is primary
    server pg1 10.0.0.1:5432 check port 8008
    server pg2 10.0.0.2:5432 check port 8008 backup
  
  backend postgres-replicas
    balance leastconn
    option httpchk GET /replica          ← Patroni returns 200 if replica
    server pg1 10.0.0.1:5432 check port 8008
    server pg2 10.0.0.2:5432 check port 8008
```

Patroni's REST API:
- `GET /primary` → 200 if this node is primary, 503 otherwise
- `GET /replica` → 200 if this node is a healthy replica, 503 otherwise
- `GET /health` → 200 always (use only for liveness, not routing)

## Common Mistakes

> **No Fencing**
> A primary that loses etcd but still has disk access can continue writing. Without STONITH fencing, the old primary may write after the standby has promoted — causing split-brain. Always configure fencing.

> **Synchronous Commit Off During Failover**
> `synchronous_commit = off` risks losing committed transactions on failover — the primary acknowledges commits before WAL is sent to the standby. Use `synchronous_commit = remote_write` or `on` if zero data loss is required.

> **Too Few etcd Nodes**
> Two etcd nodes: one failure = quorum loss (2 nodes need 2 to agree). Always run 3, 5, or 7 etcd nodes. Three nodes tolerate one failure; five tolerate two.

> **No Connection Pool**
> Direct connections to PostgreSQL from 500+ application threads overwhelm the connection limit and crash under failover reconnect storms. PgBouncer is not optional at scale.

## Mental Model

Think of HA as a **flight crew handover**. If the pilot becomes incapacitated, the co-pilot takes control — but only after the tower (etcd) confirms the transfer and revokes the pilot's authority. The old pilot can't just keep flying because they think they're fine (fencing). Passengers (application connections) don't notice the handover because the plane (PgBouncer) maintains steady flight path throughout. The co-pilot can only take over if the majority of air traffic control (quorum) agrees the handover is legitimate.

**Mini Summary**: Availability is measured in nines; each additional nine requires architectural investment. Primary-standby with Patroni provides automated failover in 10–30 seconds. Split-brain is prevented via distributed consensus (etcd) with leader leasing. PgBouncer absorbs connection storms during failover. HAProxy routes reads to replicas and writes to primary based on Patroni health checks. Always run odd-numbered etcd clusters.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium's database SLA is 99.99% availability (~52 minutes downtime/year). The current setup: single primary PostgreSQL, no standby, no connection pool.

Design the HA architecture to meet the SLA:
1. What components are needed and why?
2. How many etcd nodes and why?
3. If the business requires zero data loss on failover, what PostgreSQL parameter must be set and what is the trade-off?
4. How would you test that the failover actually works within the 52-minute annual budget?

---

# Integration

**Mathematics**: The availability of a series system (single point of failure) is A_series = ∏A_i — each component's unavailability multiplies. A parallel system (redundant components) has A_parallel = 1 - ∏(1 - A_i). A single database node with 99.9% availability in a series architecture: A = 0.999. Two nodes in parallel with automatic failover: A = 1 - (1-0.999)² = 1 - 0.000001 = 99.9999% — six nines from two three-nine components. The mathematics of redundancy is why HA architectures add components: parallel failures must be correlated to defeat redundancy. The failure independence assumption (network, disk, and power fail independently) is why multi-AZ and multi-region add more value than local redundancy.

**Sciences**: HA database architecture mirrors **redundant biological systems**. The human cardiovascular system has two atria, two ventricles, a pacemaker (SA node) with backup (AV node), and distributed capillary networks — multiple redundant systems for a vital function. Patroni's leader election mirrors the SA node's authority: it fires first; only if it fails does the backup AV node take over. STONITH is analogous to **apoptosis** — programmed cell death that prevents a malfunctioning cell from disrupting the organism. The consensus requirement prevents a "zombie primary" (equivalent of a zombie cell) from continuing harmful activity after its legitimate authority has been revoked.

---

# Eleven Seconds

The Senior Engineer showed the alert timeline to the Lead Data Engineer: 02:17:17 (primary failure detected) to 02:17:28 (writes restored). Eleven seconds. "For 99.99% SLA we have 52 minutes downtime budget for the whole year," the Lead said. "We used eleven seconds of it." They looked at the architecture diagram: Patroni, three etcd nodes, PgBouncer, HAProxy. Six months of engineering work. "Worth it," the Senior Engineer said.
