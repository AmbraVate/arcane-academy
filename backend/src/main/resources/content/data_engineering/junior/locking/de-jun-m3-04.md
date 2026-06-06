---
id: de-jun-m3-04
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m3
moduleTitle: "Module 3: Transactions"
moduleGlyph: "🔒"
moduleSortOrder: 3
topicSlug: locking
topicTitle: "Locking"
topicSortOrder: 4
lesson: locking
title: "Locking"
sortOrder: 4
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m3-03]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Distinguishes shared (read) locks from exclusive (write) locks
    - Explains what a deadlock is and how it occurs
    - Describes the standard deadlock prevention technique (consistent lock ordering)
    - Explains database deadlock detection and automatic victim selection
    - Identifies table locks vs row locks and when each is used
  keywords: [shared lock, exclusive lock, deadlock, lock ordering, victim, row lock, table lock, wait, block, cycle, detection, prevention, timeout]
  modelAnswer: |
    Shared (read) locks allow multiple transactions to read simultaneously but block exclusive locks. Exclusive (write) locks block all other locks — only one writer at a time. A deadlock occurs when two transactions each hold a lock and wait for the other's lock, creating a cycle. Prevention: acquire locks in a consistent global order across all transactions. Detection: the database monitors the wait-for graph and kills one transaction (the victim) to break the cycle. Row-level locks provide finer granularity (higher concurrency) than table locks. Table locks are used for DDL operations (ALTER TABLE) or explicitly when bulk operations are more efficient with a table-level lock.
guidedSteps:
  - id: de-jun-m3-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Transaction A holds an exclusive lock on Row 1 and waits for Row 2. Transaction B holds an exclusive lock on Row 2 and waits for Row 1. What is this situation called?
    inputConfig:
      options:
        - "Race condition — both transactions will eventually proceed"
        - "Deadlock — circular wait; neither transaction can proceed without the other releasing its lock"
        - "Starvation — one transaction is permanently blocked"
        - "Lock escalation — row locks promoted to table locks"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Deadlock — circular wait; neither transaction can proceed without the other releasing its lock"]
      rejectedFeedback: "A deadlock is a circular wait: Transaction A waits for B's lock, B waits for A's lock — neither can proceed. The database detects this by periodically checking the wait-for graph for cycles. When a cycle is found, the database selects one transaction as the 'victim' and rolls it back (releasing its locks), allowing the other to proceed. The victim is typically chosen based on minimum undo cost or transaction age. The rolled-back transaction receives an error that the application must handle — usually by retrying."
    hint: "Both transactions are waiting for each other. Neither can proceed. What is this called?"
    reflectionPrompt: "How would you redesign the transaction order to prevent this deadlock?"
  - id: de-jun-m3-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To prevent deadlocks, all transactions should acquire locks on multiple rows in the same ________ order.
    inputConfig:
      placeholder: "consistent"
    markingRule:
      matchMode: CONTAINS
      accepted: [consistent, "consistent order", "same order", global, predetermined, fixed]
      rejectedFeedback: "Consistent lock ordering is the primary deadlock prevention technique: if all transactions always acquire locks in the same global order (e.g. always lock account with lower ID first), circular waits are impossible. Example: transfer from account 1 to account 3 → lock account 1 first, then account 3. Transfer from account 3 to account 1 → still lock account 1 first (the lower ID), then account 3. Both transactions acquire locks in the same order — no cycle. This technique transforms the potential deadlock into a linear wait: one transaction blocks while the other completes."
    hint: "If all transactions acquire locks in the same order, circular waits cannot form."
    reflectionPrompt: "How would you define the lock ordering for transactions that lock different combinations of tables?"
  - id: de-jun-m3-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain the difference between row-level locks and table-level locks, and describe a scenario where a table lock is more appropriate than row locks.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [row, table, granularity, DDL, bulk, ALTER, entire, overhead, all rows, schema, lock escalation, metadata]
      rejectedFeedback: "Row-level locks lock individual rows, allowing concurrent access to other rows in the same table — higher concurrency, more memory overhead per lock. Table-level locks lock the entire table, blocking all concurrent access — lower concurrency, minimal overhead. Table locks are appropriate for: DDL operations (ALTER TABLE) that modify the schema; bulk INSERT/UPDATE/DELETE operations where row-by-row locking overhead exceeds the benefit; TRUNCATE TABLE. Many databases acquire a table lock automatically for DDL. Explicit table locking (LOCK TABLES in MySQL) is occasionally used for batch operations where the reduced concurrency is acceptable in exchange for lower lock overhead."
    hint: "When does locking every row individually have more overhead than locking the whole table?"
    reflectionPrompt: "Why does ALTER TABLE typically require a table lock? What does this mean for production schema changes on large tables?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Multiple transactions hold shared locks on the same row. Transaction A tries to acquire an exclusive lock on the same row. What happens?"
    options:
      - "Transaction A acquires the exclusive lock immediately — exclusive locks can coexist with shared locks"
      - "Transaction A blocks until all shared locks are released"
      - "All shared lock holders are notified to release their locks"
      - "The database upgrades one shared lock to exclusive automatically"
    correctIndex: 1
    feedback: "An exclusive (write) lock is incompatible with shared (read) locks. Transaction A must wait until all current shared lock holders commit or rollback, releasing their shared locks. Only then can A acquire the exclusive lock. This is the fundamental reader-writer lock pattern: multiple readers can proceed simultaneously, but a writer must wait for all readers to finish. Conversely, when an exclusive lock is held, new shared lock requests also block — readers must wait for the writer to finish."
  - type: MULTIPLE_CHOICE
    question: "The database rolls back Transaction B with a 'deadlock victim' error. What should the application do?"
    options:
      - "Exit the application — deadlocks indicate a critical bug"
      - "Retry Transaction B from the beginning — deadlocks are expected and retryable"
      - "Ignore the error — the data is unchanged"
      - "Switch to READ UNCOMMITTED isolation to avoid future deadlocks"
    correctIndex: 1
    feedback: "Deadlock victim errors are expected in high-concurrency systems and must be handled with retry logic. The database rolled back the victim cleanly — all its changes are undone, the row is in a consistent state. The application should catch the deadlock error (e.g. SQLSTATE 40P01 in PostgreSQL, error 1213 in MySQL) and retry the entire transaction after a brief pause. Most applications implement a retry loop with exponential backoff for deadlock errors. This is a normal part of database application development — not a bug in the application or the database."
retrieval:
  recall: "Describe a deadlock scenario involving fund transfers between three accounts and show the lock ordering fix."
  explain: "Explain the wait-for graph used for deadlock detection, how the database uses it, and how the victim is selected."
  mistakeId:
    code: "-- Transfer funds between two accounts\nBEGIN;\nUPDATE accounts SET balance = balance - 100 WHERE account_id = 3; -- locks row 3\nUPDATE accounts SET balance = balance + 100 WHERE account_id = 1; -- locks row 1\nCOMMIT;\n\n-- Concurrent transaction:\nBEGIN;\nUPDATE accounts SET balance = balance - 50 WHERE account_id = 1; -- locks row 1\nUPDATE accounts SET balance = balance + 50 WHERE account_id = 3; -- waits for row 3\nCOMMIT;"
    answer: "This code creates a deadlock: Transaction 1 locks row 3 then waits for row 1; Transaction 2 locks row 1 then waits for row 3. Circular wait = deadlock. The fix: always acquire locks in the same order. Since both transactions involve accounts 1 and 3, always lock the lower account_id first: BEGIN; UPDATE accounts SET balance = balance - 100 WHERE account_id = MIN(1,3) (lock account 1 first); UPDATE accounts SET balance = ... WHERE account_id = MAX(1,3) (lock account 3 second); COMMIT. If a transfer is always from lower ID to higher ID (or vice versa), enforce this ordering in the application. Consistent ordering makes circular waits impossible."
---

# Hook

Locks are the mechanism that makes transaction isolation work at the database level. Shared locks allow concurrent readers; exclusive locks enforce single-writer access. When transactions compete for the same locks, they either wait (correct but possibly slow) or deadlock (wait forever, requiring the database to intervene). Understanding locks means understanding how the database achieves isolation and what can go wrong.

# Lore Introduction

"Two transactions just deadlocked," the Junior Engineer reported. "The database rolled one back." The Senior Archivist examined the lock trace. "Archive Member Update acquires a write lock on loans, then waits for members. Renewal Process acquires a write lock on members, then waits for loans. Circular wait — classic deadlock." She traced the sequence. "The database detected the cycle and killed the smaller transaction as the victim. The application caught the error and retried." She set the trace down. "This specific deadlock is preventable. If every transaction always locks members before loans — consistent order — the cycle is impossible. Let me show you."

# Core Learning

## Concept Introduction

### Lock Types

```
Shared Lock (S-lock / read lock):
  → Held during SELECT (with certain isolation levels or FOR SHARE)
  → Multiple transactions can hold shared locks on the same row simultaneously
  → An exclusive lock request blocks until all shared locks are released

Exclusive Lock (X-lock / write lock):
  → Held during INSERT, UPDATE, DELETE, or SELECT FOR UPDATE
  → Only one transaction can hold an exclusive lock at a time
  → Blocks all other shared and exclusive lock requests

Lock compatibility:
  S + S = compatible    (two readers allowed)
  S + X = incompatible  (reader blocks writer)
  X + S = incompatible  (writer blocks reader)
  X + X = incompatible  (writer blocks writer)
```

### Deadlock — Circular Wait

```
Timeline of a deadlock:

t=1: Transaction A executes: UPDATE loans SET ... WHERE loan_id = 100
     → A acquires exclusive lock on loans row 100

t=2: Transaction B executes: UPDATE members SET ... WHERE member_id = 42
     → B acquires exclusive lock on members row 42

t=3: Transaction A executes: UPDATE members SET ... WHERE member_id = 42
     → A waits for B to release the lock on members row 42

t=4: Transaction B executes: UPDATE loans SET ... WHERE loan_id = 100
     → B waits for A to release the lock on loans row 100

→ Circular wait: A waits for B, B waits for A
→ Neither can proceed → DEADLOCK
→ Database detects the cycle and rolls back one transaction (victim)
```

### Deadlock Prevention — Consistent Lock Ordering

```sql
-- Rule: always lock members before loans (alphabetical order of table name,
--       or by some other global convention)

-- Both transactions follow this rule:

-- Transaction A (previously locked loans first — wrong):
BEGIN;
-- FIXED: lock members row first
UPDATE members SET last_activity = NOW() WHERE member_id = 42;  -- lock members first
UPDATE loans SET status = 'returned' WHERE loan_id = 100;       -- lock loans second
COMMIT;

-- Transaction B:
BEGIN;
UPDATE members SET tier = 'Scholar' WHERE member_id = 42;       -- lock members first (waits for A)
UPDATE loans SET ... WHERE loan_id = 100;                       -- lock loans second
COMMIT;

-- Now B waits for A to release members lock — linear wait, not circular.
-- Deadlock impossible when acquisition order is consistent.
```

### Deadlock Detection and Victim Selection

```sql
-- PostgreSQL: automatic deadlock detection
-- Checks wait-for graph periodically (default: 1 second)
-- Selects victim with minimum undo cost (or oldest/youngest transaction)
-- Victim receives: ERROR 40P01 "deadlock detected"

-- MySQL: immediate detection
-- Error 1213: Deadlock found when trying to get lock; try restarting transaction

-- Application retry pattern (pseudocode)
MAX_RETRIES = 3
for attempt in 1..MAX_RETRIES:
    try:
        BEGIN
        -- transaction body
        COMMIT
        break  -- success
    catch DeadlockError:
        ROLLBACK
        if attempt == MAX_RETRIES:
            raise  -- give up after N retries
        sleep(random(0, 0.1) * attempt)  -- exponential backoff with jitter
```

### Row-Level vs Table-Level Locks

```sql
-- Row-level locking (default in InnoDB, PostgreSQL):
SELECT * FROM orders WHERE customer_id = 42 FOR UPDATE;
-- Locks only the rows matching customer_id = 42

-- Table-level lock (explicit — use sparingly):
-- MySQL:
LOCK TABLES orders WRITE;
-- ... batch operation ...
UNLOCK TABLES;

-- PostgreSQL:
LOCK TABLE orders IN EXCLUSIVE MODE;
-- ... DDL or bulk operation ...
-- Released automatically at transaction end

-- When table locks are appropriate:
-- 1. DDL operations (ALTER TABLE acquires table lock automatically)
-- 2. Bulk INSERT/UPDATE/DELETE where row lock overhead is excessive
-- 3. Snapshot backup of a table (requires exclusive table lock)
```

### Lock Escalation

```sql
-- Some databases (SQL Server) escalate many row locks to a table lock
-- to reduce memory overhead (row lock metadata has memory cost)

-- SQL Server: if a transaction acquires > 5000 row locks on a single table,
-- it may escalate to a table lock — preventing all concurrent access
-- Prevention: use batching to keep lock counts below escalation threshold

-- PostgreSQL: no automatic lock escalation (uses per-row version tuples instead)
-- MySQL InnoDB: no escalation; gap locks used for phantom prevention instead
```

### Advisory Locks — Application-Level Coordination

```sql
-- PostgreSQL advisory locks: application-defined locks identified by integer key
-- Useful for distributed coordination beyond row/table locks

-- Acquire an advisory lock (blocks if another session holds it)
SELECT pg_advisory_lock(42);  -- lock key = 42

-- ... perform operation ...

-- Release
SELECT pg_advisory_unlock(42);

-- Try-lock version (returns false if already held, does not block)
SELECT pg_try_advisory_lock(42);

-- Use case: ensure only one instance of a background job runs at a time
```

## Common Mistakes

- **Deadlock-prone code with no retry**: Applications that do not catch and retry on deadlock errors fail silently or crash on a normal, recoverable database condition.
- **Acquiring locks across multiple requests**: If a transaction is begun in one HTTP request, held open while the user interacts, and committed in a later request, the lock is held for seconds — blocking all other transactions on those rows.
- **Not understanding that DDL acquires table locks**: ALTER TABLE on a busy production table can block all reads and writes for the duration of the DDL. Use online DDL tools (gh-ost, pt-online-schema-change) for large tables.
- **SELECT FOR UPDATE without WHERE index**: If the WHERE clause requires a full table scan, the FOR UPDATE locks all rows scanned — much broader than intended.

## Mental Model

Locks are like bathroom keys on a key hook at a hostel. Shared (read) locks are like multiple people being handed the key to the same bathroom simultaneously — they can coexist (each person can shower, knowing others may also be in the building). Exclusive locks are like taking the key off the hook and keeping it — only one person has the key. Deadlocks happen when person A has key 1 and waits for key 2, while person B has key 2 and waits for key 1 — both are stuck. The consistent ordering rule says: everyone agrees to always pick up key 1 before key 2 — then you can never have this situation.

## Mini Summary

- ✔ Shared locks: multiple readers; Exclusive locks: one writer blocks all others
- ✔ Deadlock: circular wait — two transactions each hold and wait for the other's lock
- ✔ Prevention: always acquire multiple locks in the same consistent global order
- ✔ Detection: database monitors wait-for graph, rolls back one victim with a retryable error
- ✔ Row locks: high concurrency; table locks: DDL and bulk operations
- ✔ Applications must catch deadlock errors and retry

# Guided Practice Quest

Work through the guided steps to trace a deadlock scenario in two transactions, apply consistent lock ordering to eliminate the deadlock, and write application retry logic that handles SQLSTATE 40P01 (PostgreSQL deadlock).

# Solo Practice Quest

Analyse the locking behaviour of the following Archive operations and fix any deadlock-prone patterns: (1) process_return(loan_id) — updates loans, then members; end_of_day_processing — updates members, then loans; (2) add advisory lock-based mutual exclusion to ensure only one end_of_day_processing job runs at a time; (3) design a batch loan renewal process for 50,000 loans — row locks or table lock? Explain the trade-off; (4) write a deadlock detection test: two transactions that will reliably deadlock, run them concurrently, catch the error, and implement retry with exponential backoff. Document the consistent lock ordering convention you would adopt for the Archive system across all its tables.

# Integration

**Mathematics**: Deadlocks correspond to directed cycles in the wait-for graph W = (V, E) where V is the set of transactions and E = {(Tᵢ, Tⱼ) | Tᵢ waits for a lock held by Tⱼ}. A deadlock exists if and only if W contains a directed cycle. Detection algorithms for deadlocks are implementations of cycle detection in directed graphs — standard algorithms include DFS-based cycle detection (O(V+E)) or Floyd-Warshall for transitive closure. Deadlock prevention through consistent ordering is equivalent to topologically sorting the lock acquisition sequence: if all transactions acquire locks in topological order of a global dependency relation, cycles are impossible (a topological sort by definition contains no cycles). This connects database engineering directly to graph theory and topological sorting.

**Sciences (Transportation — Traffic Deadlocks)**: Vehicular traffic gridlock is a physical deadlock: car A blocks the box junction waiting for car B to move; car B waits for car C; car C waits for car A — circular wait. Traffic engineers solve this with the same techniques as database engineers: priority rules (consistent ordering — ambulances always have priority), traffic signals that prevent circular entry (pessimistic locking — preventing any car from entering an intersection until it can exit), and timeouts (tow away vehicles blocking junctions — the victim mechanism). The roundabout is an elegant priority-ordering solution: all traffic gives way to traffic already in the roundabout — a consistent entry ordering that prevents circular waits. Database deadlock prevention is the same principle applied to byte-sized resources rather than physical intersections.

# Lore Conclusion

"All Archive transactions now acquire locks in alphabetical table order," the Junior Engineer reported. "Members before loans before items. The circular wait pattern is impossible by construction." The Senior Archivist tested with ten simultaneous operations. "No deadlocks. And the end-of-day batch uses an advisory lock — only one instance runs at a time, everywhere." She reviewed the retry logic. "Application catches deadlock errors and retries with backoff." She closed the monitor. "You have completed Module 3: Transactions. ACID properties, isolation levels, concurrency control, and locking — the complete picture of how databases maintain correctness under concurrent load." She handed the Junior a new assignment. "Module 4: Indexing and Performance. Now you know how to keep data correct; next you learn how to keep queries fast."

---
