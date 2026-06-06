---
id: de-sen-m7-02
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m7
moduleTitle: "Module 7: Database Reliability"
moduleGlyph: "🛡️"
moduleSortOrder: 7
topicSlug: recovery
topicTitle: "Recovery"
topicSortOrder: 2
lesson: 2
title: "Recovery: Executing Under Pressure"
sortOrder: 2
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
  - de-sen-m7-01
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Describes the recovery decision framework: assess before acting"
    - "Explains point-in-time recovery execution steps"
    - "Identifies the role of communication during a data recovery incident"
    - "Describes the post-recovery review and how it prevents recurrence"
  keywords:
    - PITR
    - recovery runbook
    - incident communication
    - RTO
    - blast radius
    - post-mortem
    - data verification
  modelAnswer: |
    Recovery execution follows a structured decision framework: assess the blast radius (what is lost, how much, can it be reconstructed), choose the recovery path (PITR, selective restore, reconstruct from source), execute in isolation (never on production until verified), verify before cutover, then communicate status throughout.
    PITR execution: (1) identify the target timestamp (one second before the damaging event), (2) restore the base backup to a separate instance, (3) replay WAL to the target timestamp, (4) verify data integrity on the restored instance, (5) cutover — either promote the restored instance or restore selected rows back to production.
    Communication during incidents follows a structured cadence: initial acknowledgment within minutes, status updates every 30 minutes, resolution notification, and a post-incident report. Silence during an incident is more alarming than slow progress.
    The post-recovery review (blameless post-mortem) identifies root cause, contributing factors, and preventive actions. It distinguishes between the immediate fix (what stopped the bleeding) and systemic fixes (what prevents recurrence). Follow-through on post-mortem action items is the measure of a mature engineering culture.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "During recovery, you restore the base backup and begin replaying WAL. The WAL replay fails at timestamp T+45min with 'WAL file missing'. What happened and what do you do?"
    options:
      - "The recovery is complete — WAL replay stops when there's no more data to replay"
      - "A WAL file was not archived (archive_command failed silently). The recovery can only proceed to T+44min; data after that is lost. Verify with archive monitoring logs."
      - "The restore is corrupt — start over from a different backup"
      - "Increase wal_keep_size and re-run; the file will be found"
    correctIndex: 1
    explanation: "A missing WAL file means archive_command failed for that segment — a monitoring gap. The recovery can proceed to the last complete WAL file (T+44min) but cannot recover data after that point. This is a partial recovery — better than nothing but with some data loss. This scenario is why archive_command failures must alert immediately: a WAL gap is invisible until recovery day. Fix: monitor archive success rate; use pgBackRest or Barman which verify each archived segment."
  - type: FILL_BLANK
    question: "In PITR recovery, you replay WAL up to but not including the target timestamp, typically setting recovery_target_time to ___ the damaging event occurred."
    answer: "one second before (immediately before)"
    explanation: "Setting recovery_target_time = '2024-03-15 14:36:59' (one second before the 14:37:00 DROP TABLE) replays all committed transactions up to that point but excludes the destructive command. This recovers all data except the final second of transactions — typically acceptable. Some DBA tools allow specifying an exact WAL LSN position for sub-second precision."
  - type: SHORT_TEXT
    question: "A recovery is taking longer than expected. It's been 3 hours and the RTO was 4 hours. You have 1 hour left. What communication actions do you take?"
    modelAnswer: "1. Send an immediate status update to stakeholders: current progress, honest estimate of completion time, whether RTO will be met. 2. If completion is uncertain within RTO: escalate to manager/on-call lead now — don't wait until the deadline passes. 3. Consider whether any partial recovery can reduce user impact while full recovery completes (e.g. restore read-only access to yesterday's snapshot while current data is recovered). 4. Continue providing 30-minute updates. 5. After recovery: document the timeline, what caused the extended duration, and how to improve future recovery speed."
microCheckpoint:
  question: "What is the first action before beginning database recovery, before touching any data?"
  answer: "Assess the blast radius: what exactly is lost or corrupted, how much data, from when, and whether any of it can be reconstructed from application logs or caches. This determines the recovery path and target timestamp, and prevents starting recovery down the wrong path."
retrieval:
  recall: "What are the five steps of PITR execution in PostgreSQL?"
  explain: "Explain why running recovery directly on the production instance is dangerous."
  mistakeId: "recovery-no-verify-before-cutover"
---

# The Incident

Alert: `users` table — zero rows. Time: 14:37. The Senior Engineer's hands were steady. The Lead Data Engineer was already at a second terminal. "Don't touch production. Assess first." This was not the time for instinct — it was the time for the runbook.

# The Recovery Decision Framework

Before executing any recovery, **stop and assess**.

```
Recovery Assessment Checklist:
│
├── What exactly happened?
│   └── DROP TABLE? Bad UPDATE? Corruption? Ransomware?
│
├── What is the blast radius?
│   ├── Which tables/rows are affected?
│   ├── How much data? From what time window?
│   └── Is the data partially reconstructable from application logs/queues?
│
├── What recovery paths are available?
│   ├── PITR (WAL archiving available?)
│   ├── Selective restore (pg_dump of specific tables?)
│   ├── Replica (did replication replicate the damage?)
│   └── Application-layer reconstruction (event replay from Kafka?)
│
├── What is the target timestamp?
│   └── One second before the damaging event
│
└── Where will we restore to?
    └── Separate instance — NEVER restore directly to production until verified
```

## PITR Execution: Step by Step

### 1. Identify the Target Timestamp

```bash
# Check PostgreSQL audit log for the destructive command
grep "DROP TABLE" /var/log/postgresql/postgresql-2024-03-15.log
# 2024-03-15 14:37:02 UTC [12345]: DROP TABLE users;
# Target: '2024-03-15 14:37:01' — one second before
```

### 2. Restore Base Backup to Isolated Instance

```bash
# Spin up a separate recovery instance (Docker or new VM)
# NEVER restore to the production instance

# Restore physical backup
pg_restore --clean --if-exists -d recovery_db /backups/base_2024-03-15_02-00.dump
# Or for pgBackRest:
pgbackrest --stanza=consortium --target-time="2024-03-15 02:00:00" restore
```

### 3. Configure WAL Replay to Target Timestamp

```bash
# postgresql.conf on recovery instance
restore_command = 'aws s3 cp s3://consortium-backups/wal/%f %p'

# recovery.conf (PostgreSQL 11 and earlier) or postgresql.conf (12+)
recovery_target_time = '2024-03-15 14:37:01'
recovery_target_action = 'promote'  # stop replay and promote to read-write
```

### 4. Start and Monitor Recovery

```bash
pg_ctl start -D /recovery/data
# Monitor: tail -f /recovery/data/log/postgresql.log
# Watch for: "recovery stopping before commit of transaction..."
# Then: "database system is ready to accept read only connections"
# Then promotion: "database system is ready to accept connections"
```

### 5. Verify Data Integrity

```sql
-- On recovery instance — verify before any cutover
SELECT COUNT(*) FROM users;                    -- should match pre-incident count
SELECT MAX(created_at) FROM users;             -- should be close to 14:37:00
SELECT * FROM users WHERE id = :known_test_id; -- spot check known record
-- Run application smoke tests against recovery instance
```

### 6. Cutover Options

**Option A: Row-level restore (surgical)**
```sql
-- If only a subset of rows are lost, copy from recovery instance to production
INSERT INTO production.users
SELECT * FROM recovery_instance.users
WHERE id NOT IN (SELECT id FROM production.users)
ON CONFLICT DO NOTHING;
```

**Option B: Full promotion (table or database)**
```bash
# If damage is widespread — promote recovery instance as new production
# Or: dump the table from recovery and restore to production
pg_dump -t users recovery_db | psql production_db
```

## Communication During Incidents

```
Incident: users table dropped at 14:37
│
├── 14:38 — Initial acknowledgment
│   "Data incident confirmed. Engineers actively responding. Update in 30min."
│
├── 15:08 — Status update 1
│   "Recovery via PITR in progress. Base backup restored. WAL replay ~60% complete.
│    Estimated resolution: 15:45. No data will be permanently lost."
│
├── 15:40 — Status update 2
│   "Recovery complete on test instance. Data verified. Cutover initiating. ETA: 15:55."
│
├── 15:55 — Resolution
│   "Service restored. All user data recovered to 14:36:59. Data loss: <1 second.
│    Root cause investigation ongoing. Post-mortem scheduled for tomorrow."
│
└── Next day — Post-incident report
    Full timeline, root cause, action items with owners and dates
```

**Communication principles:**
- Acknowledge within minutes — silence is more alarming than bad news
- Give honest estimates; revise them promptly if they change
- Never say "we lost no data" until it's verified
- Post-mortem report within 48 hours

## The Blameless Post-Mortem

```
Post-mortem structure:
│
├── TIMELINE
│   Precise chronology from alert to resolution
│
├── ROOT CAUSE
│   "Developer ran DROP TABLE on production, believing it was staging.
│    No confirmation prompt exists for production DDL."
│
├── CONTRIBUTING FACTORS
│   - No WAL monitoring — WAL gap undetected
│   - Production and staging share similar hostname patterns (easy confusion)
│   - No DDL confirmation requirement in production
│
├── IMPACT
│   Users affected: 0 (recovered before user impact)
│   Data loss: 0 seconds (PITR to 14:37:01)
│   Downtime: 78 minutes
│
├── ACTION ITEMS (with owner and due date)
│   1. Implement DDL confirmation on production (DBA, 2024-03-22)
│   2. Add archive_command failure alerting (SRE, 2024-03-22)
│   3. Rename production hostname to clearly distinguish from staging (DevOps, 2024-03-29)
│   4. Monthly restore test automation (SRE, 2024-04-15)
│
└── BLAMELESS CONCLUSION
    No engineer is named in the root cause. The system permitted the error;
    the system is fixed. Blame is not an action item.
```

## Common Mistakes

> **Restoring to Production Without Verification**
> Panic leads to "just fix it fast." Restoring directly to production without verifying in isolation can compound the damage (e.g. if the backup itself is corrupt). Always restore to a separate instance, verify, then cut over.

> **Skipping Communication While Working**
> Stakeholders will escalate if they hear nothing. A 30-minute update saying "still working, ETA in 2 hours" prevents management escalation and allows impacted teams to activate contingency plans.

> **Post-Mortem Without Action Item Follow-Through**
> A post-mortem is a commitment, not a document. If action items aren't tracked and completed, the same incident recurs. Assign owners, set due dates, review in the next retrospective.

## Mental Model

Think of recovery as **surgery on an unconscious patient**. You don't start cutting before you've assessed the injury. You don't operate on the wrong patient (production vs isolated instance). You have your instruments (runbook) laid out before you start. You call time on key steps ("verify before cutover"). And you debrief afterward to improve next time. Panic and improvisation in surgery kill patients; panic and improvisation in recovery destroy data. The runbook exists so that trained hands can operate calmly under pressure.

**Mini Summary**: Assess before acting — understand the blast radius and choose the recovery path. Execute PITR in five steps: identify target timestamp, restore base backup to isolated instance, replay WAL, verify, cutover. Communicate every 30 minutes during incidents. Post-mortem with action items and follow-through. Blameless culture: fix the system, not the person.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

At 09:15, a bad database migration script accidentally nullified the `tier` field for all 48,000 users in the `users` table. The field was NOT NULL in the source, but the migration SET tier = NULL before checking the constraint (which allowed temporary nulls). You have: WAL archiving, a base backup from 02:00, and Kafka audit logs of user tier changes since 02:00.

1. Write the recovery assessment: what are the options?
2. Choose between PITR and selective data reconstruction from Kafka logs. What factors determine the better choice?
3. How do you verify the recovery is complete before cutover?
4. Write the communication message for 09:20 (5 minutes after the incident).

---

# Integration

**Mathematics**: Recovery time as a function of data volume follows an approximately **linear relationship**: T_recovery ≈ (V_backup/bandwidth) + (V_WAL × WAL_speed). For a 500GB base backup at 400MB/s network: T_restore ≈ 21 minutes. WAL replay rate depends on transaction density; typically 10–50x faster than original processing speed. The critical path to RTO is: max(restore_time, WAL_replay_time) + verification_time. This is an **LP (linear programming) optimisation** problem: minimise RTO by maximising parallelism where possible (restore and verify different tables in parallel) and identifying the critical path constraints (largest table, slowest replay segment).

**Sciences**: The recovery process mirrors **triage in emergency medicine**. The primary survey (ABC: Airway, Breathing, Circulation) maps to: assess what's lost, determine recovery path, begin restoration. You don't perform surgery before establishing airway. The golden hour principle — outcomes degrade rapidly with time before intervention — applies to both: data recovery following an incident has diminishing returns as dependent systems diverge further from the correct state. The principle of "stabilise before you treat" is the recovery equivalent of "verify on isolated instance before cutover."

---

# 78 Minutes

At 15:55, the recovery completed. All user data recovered to 14:36:59. Zero seconds of data permanently lost. The Senior Engineer closed the terminal. Seventy-eight minutes from incident to resolution. "That was good," the Lead Data Engineer said. Not happy — good. Nobody would be happy about a drop table in production. "The runbook worked. The WAL archive worked. The verify-before-cutover worked." They opened a new document: Post-Mortem. The work was not done until the system was fixed so this couldn't happen again.
