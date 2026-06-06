---
id: de-sen-m7-04
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m7
moduleTitle: "Module 7: Database Reliability"
moduleGlyph: "🛡️"
moduleSortOrder: 7
topicSlug: disaster_planning
topicTitle: "Disaster Planning"
topicSortOrder: 4
lesson: 4
title: "Disaster Planning: When Everything Goes Wrong"
sortOrder: 4
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
  - de-sen-m7-03
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Distinguishes high availability (component failure) from disaster recovery (regional failure)"
    - "Explains RTO and RPO for a disaster recovery scenario"
    - "Describes the runbook as the core artefact of disaster planning"
    - "Identifies the role of DR testing (game days) in validating plans"
  keywords:
    - disaster recovery
    - RTO
    - RPO
    - runbook
    - game day
    - multi-region
    - warm standby
    - cold standby
  modelAnswer: |
    High availability handles component failure (disk, node, network interface) within a region. Disaster recovery handles regional failure — an entire datacenter or cloud region becoming unavailable. HA operates at seconds-to-minutes timescale; DR operates at minutes-to-hours.
    RTO (Recovery Time Objective) for DR is how long the business can survive without the service — typically 1–4 hours for most systems, minutes for critical financial services. RPO defines maximum data loss. These drive architectural choices: cold standby (hours RTO, high data loss risk) vs warm standby (minutes RTO, WAL streaming across regions, near-zero data loss) vs active-active multi-region (near-zero RTO/RPO, highest cost).
    The runbook is the step-by-step procedure written before the disaster, validated before the disaster, and executed during the disaster by engineers who may be stressed or unfamiliar with the system. A disaster is not the time to improvise. Runbooks must be tested and updated quarterly.
    Game days are scheduled exercises where the DR plan is executed against a production-like environment — deliberately triggering failure scenarios. They validate that runbooks work, that engineers can execute them under simulated pressure, and that RTO/RPO targets are achievable. DR plans that have never been tested are not DR plans.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "Your primary region (eu-west-1) suffers a complete outage. You have a warm standby in us-east-1 with cross-region WAL streaming (5-minute lag). RTO is 30 minutes, RPO is 15 minutes. Can you meet both targets?"
    options:
      - "Yes — warm standby failover takes ~10 minutes; WAL lag means at most 5 minutes data loss"
      - "No — cross-region failover always exceeds 30 minutes due to DNS propagation"
      - "Yes on RTO, No on RPO — 5-minute WAL lag exceeds the 15-minute RPO target"
      - "No on both — warm standby requires manual intervention which always exceeds RTO"
    correctIndex: 0
    explanation: "A warm standby in another region with WAL streaming satisfies both constraints here. Failover execution (promote standby, update DNS/load balancer, verify) takes 10–20 minutes — within the 30-minute RTO. WAL replication lag of 5 minutes means maximum data loss of 5 minutes — within the 15-minute RPO. The RTO and RPO targets are deliberately conservative enough to accommodate this architecture. For tighter requirements (5-min RTO, 1-min RPO), active-active with synchronous replication is needed."
  - type: FILL_BLANK
    question: "A ___ is a pre-written, step-by-step procedure for executing disaster recovery, written when engineers are calm, tested before it is needed, and followed precisely during the event."
    answer: "runbook"
    explanation: "A runbook documents exactly what to do, in what order, with what commands and verification steps. During a disaster, engineers are under stress and time pressure — the runbook removes the need for improvisation. Runbooks must be stored outside the system they describe (not just on the failed server) and kept up to date as infrastructure changes."
  - type: SHORT_TEXT
    question: "What is a game day in the context of disaster recovery testing and what specific outcomes must it validate?"
    modelAnswer: "A game day is a scheduled exercise where engineers deliberately trigger a disaster scenario in a controlled environment (staging clone of production) and execute the DR runbook. It must validate: (1) the runbook steps are complete and accurate — no missing commands or outdated references, (2) engineers can execute the runbook within RTO — timed from failure detection to service restoration, (3) data loss does not exceed RPO — verified by checking the restored instance's most recent transaction timestamp, (4) communication procedures work — stakeholder notification templates and escalation paths tested, (5) dependencies are correctly identified — all downstream systems reconnect correctly after DR failover."
microCheckpoint:
  question: "What is the difference between high availability and disaster recovery?"
  answer: "High availability handles component failures (disk, node, NIC) within a region, with automated failover in seconds to minutes. Disaster recovery handles regional failures — an entire cloud region or datacenter becoming unavailable — with planned failover in minutes to hours. HA is automated; DR typically requires human decision and execution."
retrieval:
  recall: "What are the three DR standby models (cold, warm, hot/active-active) and their RTO/RPO trade-offs?"
  explain: "Explain why a DR plan that has never been tested is not a DR plan."
  mistakeId: "dr-untested-runbook"
---

# The Region Goes Down

At 11:23 on a Wednesday, the cloud provider declared a major incident: eu-west-1 degraded. By 11:31, the database in eu-west-1 was unreachable. The Lead Data Engineer opened the incident channel. The Senior Engineer reached for the DR runbook. Not to improvise — to execute. Every step had been written in advance. Every step had been tested. "Start the clock," the Lead said. "We have 30 minutes."

# HA vs Disaster Recovery

```
High Availability:
  Scope:     One component within one region fails
  Examples:  Disk failure, node crash, network interface outage
  Response:  Automated failover (Patroni) in 10–30 seconds
  RTO:       Seconds to minutes
  
Disaster Recovery:
  Scope:     Entire datacenter or cloud region becomes unavailable
  Examples:  Regional power grid failure, cloud provider outage, flood
  Response:  Planned, human-executed failover to another region
  RTO:       Minutes to hours (depending on architecture)
```

Both are needed. HA handles daily operational failures. DR handles rare but catastrophic events.

## Standby Models

### Cold Standby
No running instance in the DR region. Backups (S3 cross-region) are available.

```
RTO: 2–8 hours (spin up instance, restore backup, verify)
RPO: Up to 24 hours (depends on backup frequency)
Cost: Lowest (only storage)
Risk: Untested infrastructure; slowest recovery
```

**Use when**: cost is the primary constraint and the business can tolerate multi-hour outages.

### Warm Standby
Running but small instance in DR region with continuous WAL replication.

```
Primary (eu-west-1) ──── WAL streaming ────→ Standby (us-east-1)
  Full-size instance                          Smaller instance
  Accepts writes                              Read-only; hot and ready
  
RTO: 10–30 minutes (promote, update DNS, verify)
RPO: WAL lag (typically 1–10 minutes depending on replication lag)
Cost: Medium (DR instance running continuously, smaller than primary)
```

**Use when**: RTO of 30 minutes is acceptable and cost must be managed.

### Active-Active (Hot Standby)
Both regions actively accept writes; data is synchronously replicated.

```
Users (EU) → Primary (eu-west-1) ──sync replication──→ Primary (us-east-1) ← Users (US)

RTO: Seconds (DNS failover; other region already active)
RPO: Near-zero (synchronous replication; may lose in-flight transactions)
Cost: Highest (full-size instances in both regions, complex routing)
```

**Use when**: business cannot tolerate any downtime and cost is secondary.

## The DR Runbook

The runbook is the core artefact. Written before the disaster, tested before the disaster, executed precisely during it.

```markdown
# Consortium Database DR Runbook — eu-west-1 Regional Failure

## Pre-conditions
- This runbook is for complete eu-west-1 unavailability (>15 minutes)
- Decision to invoke DR requires approval from: [CTO or Head of Engineering]
- Executor: Senior Data Engineer on-call
- Communications lead: [named person]

## Step 1: Confirm the Disaster (5 minutes)
- [ ] Check AWS Service Health Dashboard
- [ ] Confirm PagerDuty: eu-west-1 database unreachable for >10 minutes
- [ ] Check Slack #status-aws for provider status
- [ ] Obtain approval to invoke DR from [CTO or Head of Engineering]
- [ ] Log decision: timestamp, approver, executor

## Step 2: Assess DR Standby State (3 minutes)
- [ ] SSH to us-east-1 standby: ssh dr-standby.us-east-1.internal
- [ ] Check replication lag: patronictl -c /etc/patroni.yml list
- [ ] Record last WAL LSN received: SELECT pg_last_wal_receive_lsn();
- [ ] Record lag: this is the data loss (RPO impact). Log to incident.

## Step 3: Promote DR Standby (5 minutes)
- [ ] patronictl -c /etc/patroni.yml promote pg-dr-node-1
- [ ] Verify: patronictl list — confirm dr node shows as Leader
- [ ] Verify: SELECT pg_is_in_recovery(); → must return 'f' (false)

## Step 4: Update DNS and Load Balancer (5 minutes)
- [ ] Update Route 53: db.consortium.internal → us-east-1 IP
  Command: aws route53 change-resource-record-sets --hosted-zone-id ... --change-batch file://dr-dns.json
- [ ] TTL is 60s — wait 60s for propagation
- [ ] Update PgBouncer config: /etc/pgbouncer/pgbouncer.ini
  host=dr-primary.us-east-1.internal
- [ ] Reload PgBouncer: pg_ctlcluster reload pgbouncer

## Step 5: Verify Service (5 minutes)
- [ ] Run smoke test: psql -h db.consortium.internal -c "SELECT COUNT(*) FROM users;"
- [ ] Check application health endpoint: curl https://api.consortium.io/health
- [ ] Verify dashboards recovering
- [ ] Confirm PagerDuty alerts resolving

## Step 6: Communications
- [ ] Post to #incident: "DR failover to us-east-1 complete. [X minutes] data loss.
       Service restored. Monitoring for stability."
- [ ] Update status page: Operational
- [ ] Notify stakeholders: [distribution list]

## Total expected RTO: 23 minutes (within 30-minute SLA)
## Expected data loss: WAL lag at time of failure (typically 2–8 minutes)
```

## DR Testing: Game Days

```
Quarterly Game Day Schedule:
│
├── Pre-game (1 week before)
│   - Announce to team (stress test: give short notice)
│   - Prepare staging environment identical to production
│   - Review and update runbook with recent changes
│
├── Game Day execution
│   - Deliberately trigger the failure scenario on staging
│   - Execute runbook exactly as written (no improvisation)
│   - Time each step; record total RTO
│   - Verify data loss (compare last transaction timestamps)
│   - Note any runbook gaps or failures
│
├── Post-game review
│   - Document deviations between expected and actual
│   - Update runbook with fixes
│   - Update RTO/RPO measurements
│   - File action items for infrastructure gaps
│
└── Sign-off: runbook validated for this quarter
```

## Communication During Disaster

```
T+00: Incident opens. "Investigating eu-west-1 database outage."
T+10: "DR invoked. Failover to us-east-1 in progress."
T+20: "DR failover complete. Service restored in us-east-1.
       Estimated data loss: 6 minutes. Monitoring stability."
T+60: "Service stable. Full post-mortem tomorrow 10:00."
Next day: Post-incident report distributed.
```

The status page and stakeholder communications must be maintained separately from the technical recovery — assign a communications lead who is NOT the engineer executing the runbook.

## Common Mistakes

> **DR Region Has No Regular Traffic**
> A DR instance that has never served real traffic may have configuration gaps only visible under load (connection limits, SSL certificates, firewall rules). Route 1–5% of production read traffic to the DR instance continuously — warm it up and verify it works.

> **Runbook Stored Only on Production Servers**
> If your runbook is a document on the eu-west-1 instance that just went down, it doesn't exist. Store runbooks in: Git (accessible from anywhere), shared document (Notion/Confluence), and printed copy in the office. At least two locations outside the failed region.

> **RTO Never Timed**
> "We can recover in 30 minutes" without ever having measured it is fiction. Game days that measure actual step durations reveal that DNS propagation takes 3 minutes, not 1; that a manual approval adds 7 minutes; that the smoke test takes 5 minutes. Measure to know.

## Mental Model

Think of DR planning as **fire drills for data**. Fire drills are practised before fires, not during them. The drill reveals that the fire exit is blocked by a storage cabinet, that the assembly point is in the staff car park, and that the alarm is inaudible in the basement. DR game days reveal the equivalent: the DR runbook references a server that was decommissioned three months ago, the DNS update command uses the wrong hosted zone ID, and the smoke test database URL is hardcoded to eu-west-1. Discover these in a drill, not a disaster.

**Mini Summary**: DR handles regional failure — beyond what HA covers. Three standby models: cold (hours RTO, lowest cost), warm (minutes RTO, WAL streaming), active-active (seconds RTO, highest cost). The runbook is the core DR artefact — pre-written, tested, executed precisely. Game days quarterly validate runbook accuracy and RTO achievability. Communicate every 10 minutes during a DR event. Store runbooks outside the failed region.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium's business requirements: RTO = 1 hour, RPO = 30 minutes, database size = 1TB, budget = moderate (cannot afford active-active multi-region).

Design the DR architecture and plan:
1. Which standby model do you choose and why?
2. How do you achieve the 30-minute RPO with your chosen model?
3. Write the headers (steps, not detail) of the DR runbook.
4. Design a game day for this DR plan — what failure scenario do you trigger, what do you measure, what constitutes a pass?

---

# Integration

**Mathematics**: DR planning involves **expected value optimisation** under risk. The cost of a disaster C_disaster (revenue loss, reputational damage, regulatory fine) multiplied by the probability of occurrence P_disaster gives the expected annual loss: E[loss] = C × P. Investing in DR infrastructure I_DR reduces C (via lower RTO/RPO) and sometimes P (via redundancy). The optimal investment maximises E[value] = -C × P + benefits, subject to I_DR ≤ budget. This is why cold standby is rational for low-value, low-probability disasters and active-active is rational for high-value, higher-probability scenarios. Insurance mathematics, portfolio theory, and DR planning all solve the same expected-value optimisation under uncertainty.

**Sciences**: DR planning mirrors **species survival strategies under catastrophic events**. The K-T extinction (asteroid impact, regional climate collapse) wiped out species relying on a single habitat type; those with distributed populations across multiple habitats survived. Geographically distributed DR (multi-region) is the data engineering equivalent of **refugia** — isolated populations that persist through regional catastrophe and recolonise afterward. The game day is analogous to **adaptive pressure in a protected environment**: controlled stress that reveals fitness gaps (vulnerabilities) before uncontrolled catastrophe does.

---

# Twenty-Three Minutes

The DR runbook was closed at 11:54. Twenty-three minutes from start to restored service. Seven minutes data loss — within the RPO of 15 minutes. The Lead Data Engineer wrote the final line in the incident log: "DR failover complete. eu-west-1 regional failure. us-east-1 active. Full post-mortem scheduled." The Senior Engineer looked at the game day report from last quarter: "Predicted RTO 23 minutes. Actual: 23 minutes." They had drilled this. Every step was where it was supposed to be. "That's why we test," the Lead said.
