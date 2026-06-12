---
id: de-sen-m7-01
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m7
moduleTitle: "Module 7: Database Reliability"
moduleGlyph: "🛡️"
moduleSortOrder: 7
topicSlug: backups
topicTitle: "Backups"
topicSortOrder: 1
lesson: 1
title: "Backups: Your Last Line of Defence"
sortOrder: 1
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
  - de-sen-m6-04
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Distinguishes full, incremental, and WAL-based backups"
    - "Explains RPO and how it drives backup frequency"
    - "Identifies the 3-2-1 backup rule and why offsite storage is essential"
    - "Describes why untested backups are not backups"
  keywords:
    - full backup
    - incremental backup
    - WAL archiving
    - RPO
    - 3-2-1 rule
    - point-in-time recovery
    - pg_basebackup
    - restore test
  modelAnswer: |
    Full backups copy the entire database. Incremental backups copy only changes since the last backup. WAL archiving continuously streams write-ahead log files to object storage, enabling point-in-time recovery (PITR) to any second within the retention window.
    RPO (Recovery Point Objective) defines the maximum acceptable data loss in time. RPO of 1 hour means you can tolerate losing up to 1 hour of data. RPO drives backup frequency: a 1-hour RPO requires at minimum hourly backups (or continuous WAL archiving). A 24-hour RPO permits daily full backups.
    The 3-2-1 rule: 3 copies of data, 2 different media, 1 offsite. If the production database and its backup are both on the same server, a disk failure destroys both. Offsite (different cloud region or provider) protects against datacenter failure, ransomware, and catastrophic events.
    An untested backup is not a backup — it is a file you hope will work. Backup files can be corrupted, incomplete, or incompatible with the restoration process. Regular restore tests (at minimum monthly, in a staging environment) prove that recovery is actually possible within the declared RTO.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "Your PostgreSQL database has daily full backups. At 14:37 on Thursday, a developer runs DROP TABLE users. You restore from the Thursday 02:00 backup. What is the data loss?"
    options:
      - "No data loss — the backup was taken the same day"
      - "12 hours and 37 minutes of transactions (02:00 to 14:37)"
      - "24 hours — you have to use Wednesday's backup because Thursday's is corrupt"
      - "37 minutes of data — the daily backup covers most of the day"
    correctIndex: 1
    explanation: "Without WAL archiving, you can only restore to the point of the last backup — 02:00 Thursday. All transactions between 02:00 and 14:37 (12h37m) are lost. With WAL archiving (pg_basebackup + continuous WAL), you could restore to 14:36:59 — one second before the DROP TABLE — recovering all but one second of data."
  - type: FILL_BLANK
    question: "Point-in-time recovery (PITR) in PostgreSQL works by restoring a base backup and replaying ___ files up to the desired recovery timestamp."
    answer: "WAL (Write-Ahead Log)"
    explanation: "PostgreSQL's WAL records every change before it is applied to the data files. Replaying WAL from a base backup forward reconstructs the database state at any point in time. pg_basebackup takes the base backup; WAL archival (wal_level = replica, archive_command) continuously ships WAL files to object storage. Recovery: restore base backup + replay WAL to target timestamp."
  - type: SHORT_TEXT
    question: "You take a backup of your production database every night at 02:00. You test the backup by checking the file size is non-zero. Why is this an inadequate test?"
    modelAnswer: "A non-zero file size only confirms the backup process ran and produced output. It doesn't verify: (1) the file is not corrupted internally (pg_restore might fail mid-way), (2) the backup is complete (process may have been interrupted without error), (3) the restoration process actually works (dependencies, credentials, schema compatibility), (4) recovery completes within RTO. Adequate backup testing requires actually restoring the backup to a test database and verifying the data is queryable and correct."
microCheckpoint:
  question: "What is the 3-2-1 backup rule?"
  answer: "Keep 3 copies of data, on 2 different storage media, with 1 copy offsite. This protects against single-storage failure (disk crash), same-location failure (fire, ransomware affecting one server), and datacenter-level failure (flood, power outage affecting all local storage)."
retrieval:
  recall: "What is RPO and how does it determine backup frequency?"
  explain: "Explain why WAL archiving enables point-in-time recovery when daily full backups alone cannot."
  mistakeId: "backup-untested"
---

# The Missing Table

"The `users` table is gone," the Senior Engineer said. The on-call alert had fired at 14:37. A developer had run a DROP TABLE in production, intending to target a staging database. "Do we have a backup?" The Lead Data Engineer pulled up the backup system. Daily backups. Last backup: 02:00 this morning. Twelve hours and thirty-seven minutes of data at risk. "If we had WAL archiving, we could recover to 14:36. Without it..." they paused. "We lose twelve and a half hours." This was a lesson that would not be forgotten.

# Why Backups Exist

Backups protect against failure modes that replication and high availability cannot:

| Failure | Replication/HA handles? | Backup handles? |
|---|---|---|
| Hardware failure | Yes — failover to replica | Yes — if HA unavailable |
| Application bug (DROP TABLE) | No — replicates to all nodes | Yes — restore pre-deletion |
| Silent data corruption | No — replicates corruption | Yes — restore clean state |
| Ransomware | No — encrypts replicas too | Yes — if offsite backup |
| Human error (bad UPDATE) | No | Yes — point-in-time recovery |

Replication is for availability. Backups are for recoverability. Both are necessary.

## RPO and RTO

**RPO (Recovery Point Objective)**: Maximum acceptable data loss (time).
- RPO = 1 hour: tolerate losing up to 1 hour of data
- RPO drives backup frequency

**RTO (Recovery Time Objective)**: Maximum acceptable recovery time (duration).
- RTO = 4 hours: must be operational within 4 hours of incident
- RTO drives infrastructure choices (warm standby vs cold restore)

```
Daily full backup:
  RPO = up to 24 hours (worst case: incident 1 second after backup)
  
Hourly full backup:
  RPO = up to 1 hour
  
Continuous WAL archiving + daily base backup:
  RPO = near-zero (seconds of loss at most)
```

## Backup Types

### Full Backup
Complete snapshot of the entire database.

```bash
 # PostgreSQL: pg_dump (logical backup)
pg_dump -Fc -Z 9 -h localhost -U postgres consortium_db \
  > /backups/consortium_$(date +%Y%m%d_%H%M%S).dump

 # PostgreSQL: pg_basebackup (physical backup — binary copy of data files)
pg_basebackup -h localhost -U replication_user -D /backups/base \
  --checkpoint=fast --wal-method=stream -P
```

**pg_dump (logical)**: database-agnostic, slower, supports selective restore, portable.
**pg_basebackup (physical)**: faster, database-version dependent, required for PITR.

### WAL Archiving (Continuous)
Continuously ship WAL files to object storage — enables point-in-time recovery.

```bash
 # postgresql.conf
wal_level = replica
archive_mode = on
archive_command = 'aws s3 cp %p s3://consortium-backups/wal/%f'
archive_timeout = 300  # force WAL switch every 5 minutes even if not full
```

With WAL archiving, the recovery window is: base backup + continuous WAL = any second in the retention window.

### Incremental Backup
Copy only pages changed since the last backup (PostgreSQL 17+ natively; previously via pgBackRest, Barman).

```bash
 # pgBackRest (popular backup tool with incremental + compression)
pgbackrest --stanza=consortium --type=incr backup
 # Only modified 8kB pages since last backup are copied
```

## The 3-2-1 Rule

| Copies | Rule | Rationale |
|---|---|---|
| 3 | Three copies of data | Production + local backup + offsite |
| 2 | Two different storage media | Disk + object storage (S3/GCS) |
| 1 | One offsite copy | Different datacenter/region/provider |

```
Insufficient:
  Production DB (same server as backup) → disk failure = total loss
  Production DB + backup on same datacenter → datacenter outage = total loss
  
Correct:
  Production DB (us-east-1 PostgreSQL)
  Standby replica (us-east-1 secondary AZ)
  Nightly pg_basebackup + WAL to S3 us-east-1
  Cross-region replication to S3 eu-west-1 (offsite)
```

Cloud databases (Neon, RDS, Cloud SQL) typically handle this automatically — verify in the documentation.

## Testing Restores

**An untested backup is not a backup.**

```yaml
 # Monthly restore test procedure
restore_test:
  schedule: "0 2 1 * *"  # first day of each month
  steps:
    - Spin up isolated test environment (no production network access)
    - Restore latest backup to test environment
    - Verify: SELECT COUNT(*) FROM users; -- matches last known count
    - Verify: Run smoke tests against restored data
    - Test PITR: restore to specific timestamp, verify expected state
    - Measure: record time taken (RTO validation)
    - Document: restore test log with timestamp and engineer sign-off
    - Alert: page on-call if restore test fails
```

The restore test validates:
1. Backup file is not corrupted
2. Restoration process completes successfully
3. Data is queryable and correct
4. RTO is achievable within SLA

## Backup Retention

```sql
-- Retention schedule
retention_policy:
  daily_backups: 7 days   -- past week recoverable to daily granularity
  weekly_backups: 4 weeks -- past month to weekly granularity
  monthly_backups: 12 months -- past year to monthly granularity
  WAL_archiving: 7 days   -- PITR window

-- Grandfather-Father-Son (GFS) rotation — classic pattern
-- Matches increasing RPO for older data (worth losing more of yesterday's
-- data than today's, more of last month's than last week's)
```

## Cloud Backup Solutions

| Platform | Backup Tool | Features |
|---|---|---|
| Neon (PostgreSQL) | Built-in branching | PITR to any second, branch from any point |
| AWS RDS | Automated backups | 35-day retention, PITR, cross-region copy |
| Google Cloud SQL | Automated backups | PITR, on-demand backups |
| Self-managed | pgBackRest / Barman | Full, incremental, delta, WAL archiving |

## Common Mistakes

> **No Restore Test**
> The most common backup failure: assuming the backup works because the process completed without error. Test monthly; a backup that can't restore is worthless.

> **Backup on Same Machine as Production**
> A disk failure destroys both. Always store backups on separate storage, separate from the production instance.

> **No WAL Archiving**
> Daily full backups give a 24-hour RPO worst case. WAL archiving brings this to near-zero. The storage cost is low (WAL files compress well); the RPO improvement is enormous.

> **Forgetting Offsite**
> A backup in the same datacenter as production is vulnerable to the same catastrophic events. Always have one copy in a geographically separate location.

## Mental Model

Think of backups as **insurance with a tested payout process**. Insurance you've never claimed is only as good as the first time you need it. If you've never tested whether the insurer actually pays out, you don't know you have valid coverage. Monthly restore tests are the equivalent of calling your insurer to confirm the policy is active and the payout process works — before you need it.

**Mini Summary**: Backups protect against application errors, data corruption, and ransomware that replication cannot handle. RPO defines maximum data loss and drives backup frequency. WAL archiving enables near-zero RPO via point-in-time recovery. The 3-2-1 rule: 3 copies, 2 media, 1 offsite. Test restores monthly — an untested backup is not a backup. Measure and validate RTO during restore tests.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium's database is 500GB. The business requires: RPO of 1 hour, RTO of 4 hours. The database runs on a single server with 2TB local disk. Currently: daily pg_dump at 02:00, stored locally.

Identify all the gaps in the current backup strategy and design a compliant solution:
1. What must change to achieve a 1-hour RPO?
2. What infrastructure changes are needed to store backups safely?
3. Design a restore test procedure that validates the 4-hour RTO.

---

# Integration

**Mathematics**: Backup systems apply **information theory** to data durability. Shannon's redundancy theory states that adding redundant copies of information protects against erasure noise. The 3-2-1 rule is an informal engineering application of **erasure coding** — distributing data across independent failure domains so that no single failure mode can destroy all copies. Formal erasure codes (Reed-Solomon, used in RAID and cloud storage) mathematically guarantee data recovery from k-of-n surviving fragments. The probability of data loss P(loss) = P(all copies fail simultaneously) = ∏P(copy_i fails) ≈ 10^-n for independent copies — each additional copy reduces loss probability by a factor of 10^4 to 10^6.

**Sciences**: Backup strategy mirrors **biological redundancy** in critical systems. The human body has duplicate kidneys, bilateral lungs, paired adrenal glands — single-organ loss is survivable. Critical proteins are encoded on multiple chromosomes. The cellular DNA repair mechanism is the biological equivalent of WAL-based recovery: every base-pair change is logged (via mismatch repair proteins) and can be corrected from the complementary strand. 3-2-1 is the engineering formalisation of the same evolutionary principle: redundancy across independent failure modes is the prerequisite for reliability in high-stakes systems.

---

# The Recovery

With WAL archiving now in place, the next incident — a bad UPDATE that corrupted 40,000 rows — was recovered in 22 minutes. Point-in-time restore to one second before the UPDATE. "Twelve minutes to restore, ten minutes to verify," the Lead Data Engineer reported. "Within RTO." The Senior Engineer updated the restore test log. The backup they'd never tested before had failed on first use. The backup they tested monthly had recovered in 22 minutes. The lesson was simple. Test everything.
