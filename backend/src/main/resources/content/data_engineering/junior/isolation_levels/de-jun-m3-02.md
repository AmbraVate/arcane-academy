---
id: de-jun-m3-02
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m3
moduleTitle: "Module 3: Transactions"
moduleGlyph: "🔒"
moduleSortOrder: 3
topicSlug: isolation_levels
topicTitle: "Isolation Levels"
topicSortOrder: 2
lesson: isolation_levels
title: "Isolation Levels"
sortOrder: 2
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m3-01]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Names the four standard SQL isolation levels in order from weakest to strongest
    - Defines dirty read, non-repeatable read, and phantom read
    - Maps which anomalies each isolation level prevents
    - Explains why SERIALIZABLE is the strongest but least concurrent
    - Identifies the default isolation level in at least two major databases
  keywords: [dirty read, non-repeatable read, phantom read, READ UNCOMMITTED, READ COMMITTED, REPEATABLE READ, SERIALIZABLE, MVCC, isolation level, concurrency, anomaly]
  modelAnswer: |
    The four isolation levels from weakest to strongest: READ UNCOMMITTED, READ COMMITTED, REPEATABLE READ, SERIALIZABLE. Dirty read: reading uncommitted data from another transaction. Non-repeatable read: same row gives different values in the same transaction (another transaction committed an update between the reads). Phantom read: same query returns different rows in the same transaction (another transaction committed inserts/deletes). READ UNCOMMITTED allows all three anomalies. READ COMMITTED prevents dirty reads. REPEATABLE READ prevents dirty and non-repeatable reads. SERIALIZABLE prevents all three. Default: PostgreSQL and SQL Server use READ COMMITTED; MySQL InnoDB uses REPEATABLE READ.
guidedSteps:
  - id: de-jun-m3-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Transaction A reads an account balance (£500). Transaction B updates the balance to £300 and commits. Transaction A reads the same balance again in the same transaction and gets £300. What anomaly occurred?
    inputConfig:
      options:
        - "Dirty read — Transaction A read uncommitted data"
        - "Non-repeatable read — the same row returned a different value within the same transaction"
        - "Phantom read — a new row appeared in the result"
        - "Lost update — Transaction A's write was overwritten"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Non-repeatable read — the same row returned a different value within the same transaction"]
      rejectedFeedback: "A non-repeatable read occurs when Transaction A reads the same row twice within the same transaction and gets different values because Transaction B committed an UPDATE between A's two reads. Transaction B committed (so it is not a dirty read — dirty reads are uncommitted data). No new rows appeared (so not a phantom). The result: Transaction A cannot rely on its own earlier read — the data changed under it. REPEATABLE READ isolation prevents this by guaranteeing a transaction sees a consistent snapshot of rows it has already read."
    hint: "Transaction B COMMITTED its update. Transaction A read the SAME row twice and got different values."
    reflectionPrompt: "Under REPEATABLE READ, if Transaction B commits a new INSERT between Transaction A's two reads of the same table with a WHERE clause, can Transaction A see the new row?"
  - id: de-jun-m3-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Under ________ isolation, a transaction can read data that another transaction has modified but not yet committed — the most dangerous isolation level.
    inputConfig:
      placeholder: "READ UNCOMMITTED"
    markingRule:
      matchMode: CONTAINS
      accepted: ["READ UNCOMMITTED", "read uncommitted", "uncommitted"]
      rejectedFeedback: "READ UNCOMMITTED is the lowest isolation level. It allows dirty reads: a transaction can read rows that another concurrent transaction has modified but not yet committed. If the modifying transaction later rolls back, the reading transaction has based decisions on data that never officially existed. This is generally never appropriate for production use — the risk of reading phantom data is too high. The only legitimate use case is approximate analytics on very large datasets where a small risk of stale/dirty data is acceptable and the performance benefit of no locking matters."
    hint: "The isolation level that allows reading uncommitted data — the weakest of the four."
    reflectionPrompt: "Can you think of a real-world scenario where READ UNCOMMITTED might be acceptable?"
  - id: de-jun-m3-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why SERIALIZABLE isolation, while the safest, reduces database concurrency and can cause more lock waits or deadlocks.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [lock, block, wait, serialise, serial, concurrent, range lock, predicate, throughput, performance, prevent]
      rejectedFeedback: "SERIALIZABLE prevents all anomalies by ensuring concurrent transactions produce the same result as some serial (one-at-a-time) ordering. To achieve this, the database uses range locks (predicate locks) that block not just the rows that were read, but the entire range of rows that could match a query. This means Transaction A and Transaction B can block each other even if they don't touch the same specific rows. More locking → more waiting → lower throughput. SERIALIZABLE transactions are more likely to deadlock if two transactions acquire locks in different orders. Lower isolation levels use less aggressive locking (or MVCC snapshots), allowing more concurrency at the cost of allowing specific anomalies."
    hint: "What kind of locks does SERIALIZABLE need to prevent phantom reads? How do those locks affect concurrent transactions?"
    reflectionPrompt: "Why do most applications use READ COMMITTED rather than SERIALIZABLE?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which anomaly does READ COMMITTED prevent that READ UNCOMMITTED does not?"
    options:
      - "Phantom read"
      - "Non-repeatable read"
      - "Dirty read — reading uncommitted data from another transaction"
      - "Lost update"
    correctIndex: 2
    feedback: "READ COMMITTED guarantees that a transaction only reads data that has been committed by other transactions — no dirty reads. It does not prevent non-repeatable reads (the same row can return different values if another transaction commits an update between reads) or phantom reads (new rows can appear if another transaction commits inserts). READ UNCOMMITTED allows all three anomalies. The progression: READ UNCOMMITTED (no protection) → READ COMMITTED (prevents dirty reads) → REPEATABLE READ (also prevents non-repeatable reads) → SERIALIZABLE (prevents all three)."
  - type: MULTIPLE_CHOICE
    question: "PostgreSQL's default isolation level is:"
    options:
      - "READ UNCOMMITTED"
      - "READ COMMITTED"
      - "REPEATABLE READ"
      - "SERIALIZABLE"
    correctIndex: 1
    feedback: "PostgreSQL defaults to READ COMMITTED. This means each statement within a transaction sees a fresh snapshot of committed data — preventing dirty reads, but allowing non-repeatable reads (different rows may appear if another transaction commits between statements within the same transaction). MySQL InnoDB defaults to REPEATABLE READ, which uses a snapshot at transaction start. SQL Server defaults to READ COMMITTED (with READ COMMITTED SNAPSHOT using MVCC as an option). Knowing the defaults matters because application code written assuming one isolation level may behave unexpectedly when deployed to a database with a different default."
retrieval:
  recall: "Describe all three read anomalies (dirty read, non-repeatable read, phantom read) with a concrete example of each in a banking context."
  explain: "Explain the isolation level spectrum from READ UNCOMMITTED to SERIALIZABLE, and give a real-world use case where each level (except READ UNCOMMITTED) would be appropriate."
  mistakeId:
    code: "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE; BEGIN; SELECT COUNT(*) FROM seats WHERE flight_id = 42 AND status = 'available'; -- application logic here UPDATE seats SET status = 'reserved', passenger_id = ? WHERE seat_id = ? AND flight_id = 42; COMMIT;"
    answer: "Using SERIALIZABLE is correct here to prevent phantom reads (another transaction reserving the same seat between the SELECT and UPDATE). However, the logic has a TOCTOU (Time of Check to Time of Use) issue: the SELECT counts available seats, but the UPDATE reserves a specific seat. If two transactions are both in SERIALIZABLE, one will be aborted by the database and must retry. The application must handle the serialization failure error (SQLSTATE 40001) and retry. Many applications assume SERIALIZABLE transactions always succeed — they must be coded to handle the serialization failure exception and retry the entire transaction. This is a common oversight."
---

# Hook

ACID's Isolation property says concurrent transactions do not interfere. But full isolation comes at a performance cost — the database must do more work to prevent interference. Isolation levels are the knob that controls this trade-off: from nearly no isolation (READ UNCOMMITTED) to complete isolation (SERIALIZABLE), with two intermediate levels covering the most common real-world needs.

# Lore Introduction

"Two archivists processed renewals simultaneously," the Junior Engineer reported. "Both read the same member's loan count. Both decided the member qualified for tier promotion. Both inserted a tier change — the member was promoted twice." The Senior Archivist examined the logs. "Non-repeatable read? Or phantom?" She traced the sequence. "Both read a consistent snapshot, but neither saw the other's INSERT until both committed. Phantom read: new rows appeared in the 'pending promotions' query between the two transactions." She set the report down. "The fix is not always SERIALIZABLE. Understand the anomaly first. Then choose the isolation level that prevents exactly that anomaly, with the minimum impact on concurrency."

# Core Learning

## Concept Introduction

### The Three Read Anomalies

**Dirty Read**: reading uncommitted data from another transaction

```
Transaction A: UPDATE balance = 300 (uncommitted)
Transaction B: SELECT balance → returns 300  ← dirty read
Transaction A: ROLLBACK
Transaction B: made a decision based on data that never existed
```

**Non-Repeatable Read**: same row returns different values within one transaction

```
Transaction A: SELECT balance → 500
Transaction B: UPDATE balance = 300; COMMIT
Transaction A: SELECT balance (same row) → 300  ← different value!
Problem: Transaction A's two reads are inconsistent within itself
```

**Phantom Read**: same query returns different rows within one transaction

```
Transaction A: SELECT COUNT(*) FROM bookings WHERE seat = 5 → 0 (no bookings)
Transaction B: INSERT INTO bookings (seat=5); COMMIT
Transaction A: SELECT COUNT(*) → 1  ← phantom row appeared!
Problem: not a changed value, but new rows matching the same query
```

### The Four Isolation Levels

| Level | Dirty Read | Non-Repeatable Read | Phantom Read |
|---|---|---|---|
| READ UNCOMMITTED | ✗ Possible | ✗ Possible | ✗ Possible |
| READ COMMITTED | ✓ Prevented | ✗ Possible | ✗ Possible |
| REPEATABLE READ | ✓ Prevented | ✓ Prevented | ✗ Possible* |
| SERIALIZABLE | ✓ Prevented | ✓ Prevented | ✓ Prevented |

*PostgreSQL's MVCC-based REPEATABLE READ also prevents phantom reads in practice

### Setting Isolation Levels

```sql
-- For the current transaction
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
BEGIN;
-- ... statements ...
COMMIT;

-- For the current session (PostgreSQL)
SET SESSION CHARACTERISTICS AS TRANSACTION ISOLATION LEVEL REPEATABLE READ;

-- For the current session (MySQL)
SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE;

-- Check current level (PostgreSQL)
SHOW TRANSACTION ISOLATION LEVEL;
```

### When to Use Each Level

```
READ UNCOMMITTED:
  Use: Almost never. Approximate analytics on massive datasets where
       stale/dirty data is explicitly acceptable.

READ COMMITTED (default in PostgreSQL, SQL Server):
  Use: Most OLTP applications. Prevents dirty reads. Each statement
       sees the latest committed data. Good default.

REPEATABLE READ (default in MySQL InnoDB):
  Use: Reports that read the same data multiple times within a transaction
       and require consistency (e.g. financial reports that sum several
       related tables — should see a consistent snapshot throughout).

SERIALIZABLE:
  Use: Critical correctness scenarios where phantom reads matter:
       seat reservations, ticket sales, inventory allocation.
       Applications must handle serialization failures (error 40001) and retry.
```

### MVCC — How Modern Databases Implement Isolation

```
Multi-Version Concurrency Control (MVCC):
  Instead of blocking reads with locks, the database maintains multiple
  versions of each row (with transaction timestamps).

  Under READ COMMITTED:
    Each statement gets a snapshot of all committed data at statement start.
    
  Under REPEATABLE READ / SERIALIZABLE (PostgreSQL):
    Each transaction gets a snapshot of all committed data at transaction start.
    The snapshot does not change during the transaction.
    Other transactions' commits are invisible to the snapshot.

  MVCC allows:
    Reads don't block writes  (reader and writer have separate versions)
    Writes don't block reads
    → Higher concurrency than pure locking
    → Trade-off: dead row versions accumulate (VACUUM in PostgreSQL)
```

### Handling Serialization Failures

```python
# Application code must retry on SQLSTATE 40001 (serialization failure)
import psycopg2

def transfer_funds(conn, from_id, to_id, amount, max_retries=3):
    for attempt in range(max_retries):
        try:
            with conn.cursor() as cur:
                conn.set_isolation_level(
                    psycopg2.extensions.ISOLATION_LEVEL_SERIALIZABLE
                )
                cur.execute("BEGIN")
                cur.execute("UPDATE accounts SET balance = balance - %s WHERE id = %s",
                            (amount, from_id))
                cur.execute("UPDATE accounts SET balance = balance + %s WHERE id = %s",
                            (amount, to_id))
                conn.commit()
                return True
        except psycopg2.errors.SerializationFailure:
            conn.rollback()
            if attempt == max_retries - 1:
                raise
    return False
```

## Common Mistakes

- **Assuming all databases use the same default**: PostgreSQL and SQL Server default to READ COMMITTED; MySQL InnoDB defaults to REPEATABLE READ. Code tested on one may behave differently on another.
- **Using SERIALIZABLE without retry logic**: SERIALIZABLE transactions can fail with a serialization error if they conflict with another transaction. Applications must handle this error and retry — many do not.
- **Setting isolation level inside an existing transaction**: In most databases, the isolation level must be set before the transaction begins, not inside it.
- **Thinking higher isolation always means safer application logic**: Higher isolation prevents certain anomalies but does not prevent application-level race conditions (e.g., check-then-act patterns where the check and act are in separate transactions).

## Mental Model

Isolation levels are like different levels of frosted glass between workstations. READ UNCOMMITTED: completely transparent — you see everything your neighbours are doing, including work in progress (risky). READ COMMITTED: you see only their completed, finished work — nothing half-done. REPEATABLE READ: once you look at something, it appears frozen for the rest of your shift — even if your neighbour changes it, you see your original view. SERIALIZABLE: you and your neighbours work in completely separate rooms — total isolation, but you must wait for a room to become available.

## Mini Summary

- ✔ Dirty read: reading uncommitted data; non-repeatable read: same row changes within a transaction; phantom read: new rows appear within a transaction
- ✔ READ UNCOMMITTED → READ COMMITTED → REPEATABLE READ → SERIALIZABLE (increasing protection)
- ✔ Default: PostgreSQL/SQL Server = READ COMMITTED; MySQL InnoDB = REPEATABLE READ
- ✔ MVCC enables concurrent reads and writes without locking at lower isolation levels
- ✔ SERIALIZABLE prevents all anomalies but requires retry logic for serialization failures

# Guided Practice Quest

Work through the guided steps to demonstrate a non-repeatable read by running two concurrent transactions (simulated), identify which isolation level prevents it, and write application code that retries on a serialization failure.

# Solo Practice Quest

Analyse the following scenarios and recommend an isolation level for each, justifying your choice: (1) a daily financial reconciliation report that sums multiple tables and must see a fully consistent snapshot; (2) a reservation system where two users might simultaneously book the last available item; (3) a product catalogue refresh that reads all products and updates their stock status based on inventory — occasional stale reads are acceptable; (4) a real-time analytics dashboard showing approximate order counts (stale by a few seconds is acceptable). Then write a demonstration query sequence for scenario 1 showing how REPEATABLE READ prevents the non-repeatable read anomaly that would cause the reconciliation to be wrong.

# Integration

**Mathematics**: Isolation levels are formalised in database theory through the concept of history serializability. A history H is a sequence of operations from concurrent transactions. A serial history executes all of T₁ before T₂, or all of T₂ before T₁. A history H is serializable if it produces the same result as some serial history. The isolation levels correspond to weakened forms of serializability: READ COMMITTED allows non-serial histories as long as each read sees committed data; REPEATABLE READ adds snapshot consistency for already-read data. The formalism, developed by Berenson et al. (1995) and expanded by Adya (1999), uses dependency graphs to characterise which anomalies each isolation level permits — the same anomalies described informally in this lesson.

**Sciences (Medicine — Clinical Trials)**: Isolation levels have a direct analogue in clinical trial design. In a randomised controlled trial, participants are blinded to prevent their behaviour from being influenced by knowledge of which treatment they received — analogous to READ COMMITTED preventing dirty reads (seeing incomplete treatment assignments). Cross-contamination between treatment groups — where a participant's behaviour changes because they learn about another group — corresponds to phantom reads: new "rows" (observations) in the result that should not have appeared. The CONSORT reporting standards for clinical trials enforce separation between data collection and analysis phases to prevent these anomalies — the same motivation as higher isolation levels in database transactions.

# Lore Conclusion

"The double-promotion bug is fixed," the Junior Engineer reported. "REPEATABLE READ on the tier evaluation transaction. The snapshot is taken at transaction start — the second transaction sees the pre-first-transaction state and correctly finds no pending promotion." The Senior Archivist verified the fix. "And it doesn't require SERIALIZABLE — we're preventing a non-repeatable read, not a phantom. REPEATABLE READ is sufficient." She closed the console. "The right isolation level for the right problem. Not always the strongest — the right one." She paused. "Next: concurrency — the broader patterns of how multiple transactions interact, beyond the four isolation levels."

---
