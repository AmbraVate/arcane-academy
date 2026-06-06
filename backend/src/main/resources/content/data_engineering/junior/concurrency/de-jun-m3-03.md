---
id: de-jun-m3-03
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m3
moduleTitle: "Module 3: Transactions"
moduleGlyph: "🔒"
moduleSortOrder: 3
topicSlug: concurrency
topicTitle: "Concurrency"
topicSortOrder: 3
lesson: concurrency
title: "Concurrency Control"
sortOrder: 3
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m3-02]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains optimistic vs pessimistic concurrency control
    - Describes the lost update problem and how SELECT FOR UPDATE prevents it
    - Explains the check-then-act pattern and why it requires transactional protection
    - Identifies when optimistic locking (version columns) is appropriate
    - Describes how application-layer concurrency differs from database-layer concurrency
  keywords: [optimistic, pessimistic, lost update, SELECT FOR UPDATE, version, timestamp, check-then-act, race condition, retry, compare-and-swap, lock]
  modelAnswer: |
    Concurrency control prevents conflicts when multiple transactions access the same data. Pessimistic concurrency assumes conflicts are likely — it acquires locks before reading/writing (SELECT FOR UPDATE). Optimistic concurrency assumes conflicts are rare — it checks for conflicts at commit time using a version counter or timestamp. The lost update problem: Transaction A reads a value, Transaction B reads and updates it, Transaction A then updates based on its stale read — B's update is overwritten. SELECT FOR UPDATE prevents this by locking the row at read time. Optimistic locking adds a version column: updates only proceed if the version matches what was originally read. Check-then-act patterns (read, decide, act) must happen within one transaction to be safe.
guidedSteps:
  - id: de-jun-m3-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Two users simultaneously view a product's stock (10 units) and both try to purchase 8 units. Both read 10, both decide 8 ≤ 10, both subtract 8. The stock ends at -6. This is an example of:
    inputConfig:
      options:
        - "A dirty read — they read uncommitted data"
        - "A lost update — both updates were applied based on stale reads, the intermediate update was overwritten"
        - "A phantom read — new rows appeared"
        - "An isolation failure — they should have used READ UNCOMMITTED"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A lost update — both updates were applied based on stale reads, the intermediate update was overwritten"]
      rejectedFeedback: "This is a lost update (or write-write conflict): both transactions read the same value (10), both computed their update independently (10-8=2), and one overwrote the other's committed update. Even if each transaction is individually correct (8 ≤ 10 is true when each checked), the combined result is wrong. The fix: use SELECT FOR UPDATE to lock the row at read time, preventing the second transaction from reading until the first has committed. Or use an atomic UPDATE: UPDATE products SET stock = stock - 8 WHERE product_id = ? AND stock >= 8 (if rows_affected = 0, stock was insufficient — no lost update possible)."
    hint: "Both transactions checked stock before the other wrote. The first writer's update was 'lost' because the second writer overwrote it with a stale read."
    reflectionPrompt: "How does SELECT FOR UPDATE prevent the lost update in this scenario?"
  - id: de-jun-m3-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To lock a row at read time and prevent other transactions from modifying it until you commit, use SELECT ... ________.
    inputConfig:
      placeholder: "FOR UPDATE"
    markingRule:
      matchMode: CONTAINS
      accepted: ["FOR UPDATE", "for update", "LOCK IN SHARE MODE", "FOR SHARE"]
      rejectedFeedback: "SELECT ... FOR UPDATE acquires an exclusive lock on the selected rows. Other transactions that try to SELECT FOR UPDATE or UPDATE those rows will block until the first transaction commits or rolls back. This implements pessimistic locking: you assume a conflict is likely, so you lock first. FOR SHARE (or LOCK IN SHARE MODE in MySQL) acquires a shared lock — multiple transactions can hold a shared lock simultaneously, but an exclusive lock (UPDATE) blocks until all shared locks are released. Use FOR UPDATE when you intend to update the locked rows; FOR SHARE when you need to prevent updates but allow other shared reads."
    hint: "The SQL clause appended to a SELECT to acquire an exclusive lock on the returned rows."
    reflectionPrompt: "What happens to a second transaction that tries to SELECT FOR UPDATE the same row while the first transaction holds the lock?"
  - id: de-jun-m3-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain how a version column enables optimistic locking without SELECT FOR UPDATE.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [version, counter, check, WHERE, rows affected, zero, conflict, increment, mismatch, stale]
      rejectedFeedback: "Optimistic locking adds a version (integer counter) or updated_at timestamp column to each row. When an application reads a row, it reads the version. When it updates, it includes WHERE version = original_version in the UPDATE. If another transaction committed an update between the read and the UPDATE, the version will have been incremented and the WHERE clause matches 0 rows. The application detects this (rows_affected = 0), knows a conflict occurred, and retries. This avoids database locks entirely — conflicts are detected and resolved at the application layer rather than prevented by blocking."
    hint: "How does the UPDATE know if the data changed since it was read? What does zero rows affected indicate?"
    reflectionPrompt: "When would optimistic locking be more appropriate than SELECT FOR UPDATE?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The check-then-act pattern — read a value, decide based on it, then act — is unsafe when:"
    options:
      - "The value read is from a view"
      - "The check and the act are in separate transactions, allowing another transaction to change the value between them"
      - "The database is using READ COMMITTED isolation"
      - "The check involves a COUNT(*)"
    correctIndex: 1
    feedback: "If the check (SELECT) and the act (INSERT/UPDATE) are in separate transactions, another transaction can change the data between them. Classic example: check if a username is available (SELECT), username is available (result: 0), pause, another user registers with the same username, you register with that username — duplicate usernames result. Fix: put both SELECT and INSERT in one transaction with appropriate locking (SELECT FOR UPDATE for the existence check, or use a unique constraint to catch the race condition at the database level)."
  - type: MULTIPLE_CHOICE
    question: "Optimistic locking is most appropriate when:"
    options:
      - "Conflicts are frequent and data correctness is critical"
      - "Conflicts are rare and blocking other transactions would harm performance"
      - "The database does not support SELECT FOR UPDATE"
      - "You need to prevent phantom reads"
    correctIndex: 1
    feedback: "Optimistic locking is best when conflicts are rare (most reads are not followed immediately by competing writes). It avoids holding locks, which improves throughput for read-heavy workloads. If conflicts are common (high contention on the same rows), optimistic locking causes many retries, which is worse than pessimistic locking. Use pessimistic (SELECT FOR UPDATE) when conflicts are expected and the cost of retrying is high. Use optimistic (version column) when conflicts are rare and retries are cheap."
retrieval:
  recall: "Write an atomic stock decrement that prevents overselling without SELECT FOR UPDATE — using UPDATE with a conditional WHERE and rows-affected checking."
  explain: "Explain the lost update problem in a ticket booking scenario, and show two different solutions: one using SELECT FOR UPDATE (pessimistic) and one using a version column (optimistic)."
  mistakeId:
    code: "-- Check if username is available\nSELECT COUNT(*) FROM users WHERE username = 'newuser'; -- returns 0\n-- (application decides username is available)\nINSERT INTO users (username) VALUES ('newuser');"
    answer: "This check-then-act pattern has a race condition: two users simultaneously find 'newuser' available (both get COUNT = 0), and both INSERT, creating duplicate usernames. The fix has two layers: (1) Put the SELECT and INSERT in one transaction with FOR UPDATE or wrap in an application lock — though this is awkward here since the row doesn't exist yet to lock. (2) Better: use a UNIQUE constraint on username and handle the duplicate key error in the application. The database's constraint is the final arbiter — the first INSERT succeeds, the second raises a duplicate key error, which the application catches and handles (offer a different username). Unique constraints are the correct tool for uniqueness — not application-layer check-then-act."
---

# Hook

Database isolation levels define what anomalies can occur. Concurrency control is the set of techniques used to prevent or detect conflicts when multiple transactions access the same data. Two philosophies: pessimistic (lock first, prevent conflicts) and optimistic (allow conflicts, detect and resolve). Understanding both is essential for building correct high-concurrency applications.

# Lore Introduction

"The Archive sold three copies of a rare manuscript that only had one," the Junior Engineer said, checking the transaction logs. "All three orders were processed simultaneously. Each read stock = 1, each checked 1 >= 1, each decremented by 1. Final stock: -2." The Senior Archivist shook her head. "Lost update. Three transactions, same stale read, three independent decrements. The check and the decrement are not atomic." She opened the editor. "Two fixes. Pessimistic: SELECT FOR UPDATE locks the row before reading. Optimistic: atomic UPDATE with stock >= 1 in the WHERE clause — zero rows means the stock was gone. Which you choose depends on how often this happens. High contention: pessimistic. Rare conflict: optimistic."

# Core Learning

## Concept Introduction

### The Lost Update Problem

```sql
-- Two transactions simultaneously read stock = 1 and both decrement

-- Transaction A:          -- Transaction B:
SELECT stock FROM products  SELECT stock FROM products
WHERE product_id = 42;     WHERE product_id = 42;
-- A reads: 1              -- B reads: 1
-- A decides: 1 >= 1 ✓    -- B decides: 1 >= 1 ✓

UPDATE products            UPDATE products
SET stock = stock - 1      SET stock = stock - 1
WHERE product_id = 42;     WHERE product_id = 42;
-- A sets stock = 0         -- B sets stock = 0 (overwriting A's committed 0!)
COMMIT;                     COMMIT;

-- Final stock: -1 (or 0 if concurrent, but the second UPDATE is invalid)
-- If stock was truly 1, only one should have succeeded
```

### Pessimistic Locking — SELECT FOR UPDATE

```sql
-- Transaction A locks the row before reading
BEGIN;

SELECT stock FROM products
WHERE product_id = 42
FOR UPDATE;   -- acquires exclusive lock on this row
-- Returns: 1

-- Transaction B tries to SELECT FOR UPDATE same row → BLOCKS until A commits

UPDATE products SET stock = stock - 1
WHERE product_id = 42 AND stock >= 1;
-- Check stock still >= 1 (defensive, even though we locked)

COMMIT;  -- Transaction B unblocks, reads updated stock = 0

-- Transaction B:
-- Re-reads stock = 0, check fails → does not decrement → correct!
```

```sql
-- FOR SHARE: shared lock (multiple readers, no writers)
SELECT * FROM products WHERE product_id = 42 FOR SHARE;
-- Other transactions can SELECT FOR SHARE simultaneously
-- But UPDATE or SELECT FOR UPDATE will block
```

### Optimistic Locking — Version Column

```sql
-- Add version column to the table
ALTER TABLE products ADD COLUMN version INT NOT NULL DEFAULT 1;

-- Application reads the row with its version
SELECT product_id, name, stock, version FROM products WHERE product_id = 42;
-- Returns: (42, 'Rare Manuscript', 1, version=5)

-- Application updates, checking version is unchanged
UPDATE products
SET stock = stock - 1,
    version = version + 1
WHERE product_id = 42
  AND version = 5       -- must match what we read
  AND stock >= 1;

-- Check rows affected
-- rows_affected = 1: success, version was still 5
-- rows_affected = 0: conflict — another transaction changed the row, retry
```

### Atomic Operations — Avoiding Application-Layer Races

```sql
-- Best for simple cases: atomic UPDATE without SELECT
-- The entire check-and-update is one operation — no race condition possible
UPDATE products
SET stock = stock - 1
WHERE product_id = 42
  AND stock >= 1;       -- atomic check + decrement

-- Check rows affected:
-- 1 row updated → success
-- 0 rows updated → stock was 0 (or product not found) → out of stock

-- For increment (e.g., adding to stock):
UPDATE products SET stock = stock + ? WHERE product_id = ?;
-- Safe: arithmetic UPDATE is always atomic — no stale read issue
```

### Check-Then-Act Anti-Pattern

```sql
-- UNSAFE: check and act are separate statements (or transactions)
-- Window for concurrent modification between them

-- Safe version: unique constraint as the final arbiter
CREATE UNIQUE INDEX idx_users_username ON users (username);

BEGIN;
INSERT INTO users (username, email) VALUES ('newuser', 'user@example.com');
-- If username already exists: UNIQUE CONSTRAINT VIOLATION → catch in application
COMMIT;

-- This is correct: the database enforces uniqueness atomically.
-- The unique constraint is always the correct tool for uniqueness,
-- not a check-then-act SELECT/INSERT sequence.
```

### Application vs Database Concurrency

```
Database-layer concurrency control:
  SELECT FOR UPDATE, SERIALIZABLE isolation, constraints
  → Database enforces correctness regardless of application behaviour
  → Correct even if the application is buggy or concurrent

Application-layer concurrency control:
  Optimistic locking (version columns), retry logic, queuing
  → Application must correctly implement the protocol
  → Can be more efficient but requires careful coding and testing

Best practice:
  Use database-layer constraints for invariants that MUST hold (uniqueness, FK)
  Use optimistic/pessimistic locking for complex multi-step operations
  Never rely on application-layer checks alone for data integrity
```

## Common Mistakes

- **Check-then-act across separate transactions**: Always the wrong approach for uniqueness or availability checks. Use database constraints and handle conflict errors.
- **Optimistic locking without retry**: If rows_affected = 0, the application must retry the entire operation, re-reading fresh data. Simply returning an error to the user is insufficient for systems where retries should be transparent.
- **SELECT FOR UPDATE without indexing the locked row**: The lock is acquired on the row(s) matching the WHERE clause. If the WHERE clause does a full table scan (missing index), it may lock many unintended rows.
- **Holding SELECT FOR UPDATE locks in application-level logic loops**: If you SELECT FOR UPDATE and then pause for user input or external API calls while holding the lock, other transactions block for the duration. Keep locked transactions short.

## Mental Model

Pessimistic locking is like reserving a seat at a restaurant before you arrive — you claim it, and no one else can take it while you hold the reservation. Optimistic locking is like arriving without a reservation and checking on arrival — if the seat is taken, you find another or return later. Pessimistic is right when demand is high (popular restaurant — you need to book). Optimistic is right when demand is low (quiet restaurant — you rarely need to book, and checking is cheap). The wrong choice in high-contention scenarios: pessimistic is too slow if you hold the reservation for 20 minutes; optimistic causes too many failed attempts and retries.

## Mini Summary

- ✔ Lost update: stale read + concurrent write = wrong result — classic concurrency bug
- ✔ SELECT FOR UPDATE: pessimistic — lock the row at read time; best for high contention
- ✔ Version column: optimistic — detect conflicts at write time; best for low contention
- ✔ Atomic UPDATE with WHERE check: simplest pattern for conditional increments/decrements
- ✔ Unique constraints are the correct tool for uniqueness — not check-then-act
- ✔ Keep locked transactions short; never hold locks during external I/O

# Guided Practice Quest

Work through the guided steps to reproduce a lost update on a stock table, fix it with SELECT FOR UPDATE, then fix the same problem with an atomic conditional UPDATE, and compare the two approaches for a scenario with 1000 concurrent transactions.

# Solo Practice Quest

Design concurrency solutions for the following Archive scenarios: (1) multiple archivists simultaneously approving loan requests — only one should succeed if the last copy is available; (2) two administrators simultaneously updating a member's tier — one update should not silently overwrite the other; (3) bulk end-of-day processing that updates 10,000 loan status records — optimistic or pessimistic locking? Justify; (4) a member requesting a reservation and a staff member processing a different reservation simultaneously for the same item. For each scenario: identify the concurrency problem (lost update, check-then-act race, etc.), write the SQL solution, and explain why you chose pessimistic or optimistic locking.

# Integration

**Mathematics**: Concurrency control is formalised as the theory of conflict serializability in transaction processing. Two operations conflict if they access the same data item and at least one is a write. The conflict graph of a history has transactions as nodes and directed edges from Tᵢ to Tⱼ if Tᵢ has a conflicting operation before Tⱼ. A history is conflict-serializable if and only if its conflict graph is acyclic. Optimistic locking corresponds to validation-based protocols: transactions execute freely, and at commit time, the validator checks for conflicts (version mismatch = cycle in the conflict graph for that execution). Pessimistic locking prevents cycles by blocking operations that would create them. The compare-and-swap (CAS) operation — the basis of optimistic locking — has a direct counterpart in computer architecture: the CAS instruction that atomically checks and updates a memory location.

**Sciences (Economics — Auction Theory)**: Concurrency control in databases has exact parallels in auction and market design. A double auction (buyers and sellers simultaneously bidding for the same good) is a concurrency problem: two buyers might simultaneously acquire the last unit unless the market mechanism prevents it. Stock exchanges use pessimistic locking (order matching engines with sequence numbers) to ensure the last share is sold exactly once. Flash crashes and erroneous double-execution in high-frequency trading often arise from check-then-act patterns at the application layer — a firm's trading algorithm checks price, decides to buy, but acts after the price has changed (the classic non-repeatable read / lost update at financial scale). The database engineering patterns in this lesson directly apply to the technical infrastructure of financial markets.

# Lore Conclusion

"The rare manuscript scenario is fixed," the Junior Engineer reported. "Atomic UPDATE with stock >= 1 in WHERE. Zero rows affected means out of stock — no lost update possible." The Senior Archivist verified the fix. "Simple, correct, and no extra locking overhead for the common case." She paused. "And for the administrator tier-promotion conflict — we added a version column. If two administrators try to update the same member simultaneously, the second one gets a conflict signal and must confirm they want to overwrite." The Junior nodded. "Pessimistic where contention is high. Optimistic where it is rare." The Senior Archivist smiled. "You understand concurrency. One more lesson: locking — the mechanism that makes concurrency control work at the database level."

---
