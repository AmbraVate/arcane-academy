---
id: de-jun-m3-01
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m3
moduleTitle: "Module 3: Transactions"
moduleGlyph: "🔒"
moduleSortOrder: 3
topicSlug: acid
topicTitle: "ACID"
topicSortOrder: 1
lesson: acid
title: "ACID Properties"
sortOrder: 1
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m2-04]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines Atomicity, Consistency, Isolation, and Durability in own words
    - Explains what happens to a transaction when a failure occurs mid-way (atomicity)
    - Describes a scenario where violating each ACID property causes a real problem
    - Explains the role of COMMIT and ROLLBACK in controlling transactions
    - Distinguishes implicit vs explicit transactions
  keywords: [ACID, atomicity, consistency, isolation, durability, COMMIT, ROLLBACK, transaction, BEGIN, START TRANSACTION, partial failure, rollback, write-ahead log]
  modelAnswer: |
    ACID ensures database correctness under concurrent access and failures. Atomicity: all operations in a transaction succeed or none do — partial completion is impossible. Consistency: a transaction moves the database from one valid state to another; constraints are not violated. Isolation: concurrent transactions do not interfere with each other — intermediate states are not visible to other transactions. Durability: once committed, data survives crashes (written to persistent storage, typically a write-ahead log). COMMIT makes changes permanent; ROLLBACK undoes them. Explicit transactions use BEGIN/START TRANSACTION; implicit transactions auto-commit each statement unless AUTOCOMMIT is off.
guidedSteps:
  - id: de-jun-m3-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A bank transfer moves £100 from Account A to Account B. The debit of Account A succeeds, but the system crashes before the credit to Account B completes. Which ACID property was violated?
    inputConfig:
      options:
        - "Consistency — the constraint that total money in the system is conserved"
        - "Atomicity — the debit and credit are part of one transaction that must both succeed or both fail"
        - "Isolation — another user saw the intermediate state"
        - "Durability — the debit was not persisted to disk"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Atomicity — the debit and credit are part of one transaction that must both succeed or both fail"]
      rejectedFeedback: "This is the classic atomicity violation scenario. The debit and credit are two operations that must be treated as a single atomic unit — either both complete or neither does. If the system crashes mid-transaction, atomicity guarantees the partial work is rolled back: Account A is restored to its original balance. Without atomicity, the bank loses £100 — debited but never credited. The database achieves this through the write-ahead log (WAL): incomplete transactions are identified on restart and rolled back automatically."
    hint: "Which ACID property guarantees 'all or nothing'?"
    reflectionPrompt: "How does a database know which transactions to roll back after a crash?"
  - id: de-jun-m3-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To make changes in an explicit transaction permanent, you use the ________ statement.
    inputConfig:
      placeholder: "COMMIT"
    markingRule:
      matchMode: CONTAINS
      accepted: [COMMIT, commit]
      rejectedFeedback: "COMMIT makes all changes in the current transaction permanent and visible to other transactions. Before COMMIT, changes exist in the transaction's private working area — they are not visible to other sessions (isolation) and can be undone. After COMMIT, changes are written to durable storage and cannot be undone by ROLLBACK (only by a new transaction that reverses them). If you want to undo all changes in the current transaction before committing, use ROLLBACK — this restores the database to the state it was in at the start of the transaction."
    hint: "The SQL statement that makes transaction changes permanent."
    reflectionPrompt: "Once you COMMIT a transaction, can you ROLLBACK? What does that imply about the order of COMMIT and ROLLBACK?"
  - id: de-jun-m3-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain the difference between explicit and implicit transactions, and why implicit transactions (AUTOCOMMIT) can be dangerous for multi-step operations.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [autocommit, implicit, explicit, BEGIN, multi-step, automatic, immediate, partial, each statement, separate]
      rejectedFeedback: "In AUTOCOMMIT mode (the default in most databases), every SQL statement is its own implicit transaction — it commits immediately on completion. This is safe for single statements. For multi-step operations (transfer funds: debit one account then credit another), AUTOCOMMIT is dangerous: if the first statement succeeds and the application crashes before the second, the first change is already committed and cannot be rolled back. Explicit transactions (BEGIN; ...; COMMIT) wrap multiple statements into one atomic unit — either all commit or all roll back."
    hint: "What happens to each statement's changes in AUTOCOMMIT mode if the application crashes before the next statement runs?"
    reflectionPrompt: "How do you check whether AUTOCOMMIT is on in MySQL? How do you turn it off for a session?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which ACID property ensures that data is not lost even if the server crashes immediately after a COMMIT?"
    options:
      - "Atomicity — the transaction completed fully"
      - "Consistency — the data satisfies all constraints"
      - "Isolation — no other transaction interfered"
      - "Durability — committed data is written to persistent storage (disk)"
    correctIndex: 3
    feedback: "Durability guarantees that once a transaction commits, its changes survive system failures. Databases implement durability through the write-ahead log (WAL): before writing data to the main database files, the change is recorded in the log. On crash recovery, the database replays the log to restore committed transactions and roll back uncommitted ones. Without durability, a server crash immediately after COMMIT could lose the committed data — the data was only in memory, not yet on disk."
  - type: MULTIPLE_CHOICE
    question: "Two transactions run simultaneously. Transaction 1 reads Account A's balance while Transaction 2 has debited Account A but not yet committed. What does Transaction 1 see in a properly isolated database?"
    options:
      - "The new (debited) balance — transactions share state"
      - "The original (pre-debit) balance — Transaction 2's uncommitted change is not visible"
      - "NULL — the row is locked"
      - "An error — you cannot read while another transaction is modifying"
    correctIndex: 1
    feedback: "Isolation ensures that Transaction 1 sees the original balance — Transaction 2's uncommitted debit is not visible to other transactions. This prevents a dirty read: reading data that may be rolled back. The database achieves this through multi-version concurrency control (MVCC) or locking — Transaction 1 reads the committed version of the row, not the in-progress modification. The exact isolation behaviour depends on the isolation level (covered in the next lesson)."
retrieval:
  recall: "Write a transaction that transfers stock from one warehouse to another: decrement qty in warehouse A, increment qty in warehouse B, log the transfer. Include proper error handling with ROLLBACK."
  explain: "Explain each of the four ACID properties using the example of an e-commerce checkout: place order, reserve stock, charge payment card."
  mistakeId:
    code: "UPDATE accounts SET balance = balance - 100 WHERE account_id = 1; UPDATE accounts SET balance = balance + 100 WHERE account_id = 2;"
    answer: "These two statements run as separate implicit transactions (in AUTOCOMMIT mode). If the connection drops or the second statement fails, Account 1 is debited but Account 2 is never credited — £100 disappears. The fix: wrap both statements in an explicit transaction: BEGIN; UPDATE accounts SET balance = balance - 100 WHERE account_id = 1; UPDATE accounts SET balance = balance + 100 WHERE account_id = 2; COMMIT; With a transaction, if either statement fails, ROLLBACK undoes both — atomicity is preserved. Additionally, add a CHECK constraint on balance >= 0 to enforce consistency (no negative balances)."
---

# Hook

Multi-step database operations must succeed or fail as a whole. If a bank transfer credits one account but crashes before debiting the other, money is created from nothing. ACID is the set of properties that databases guarantee to prevent exactly these failures — ensuring data remains correct under any combination of concurrent access and system failures.

# Lore Introduction

"The Archive processes membership renewals: charge the fee, extend the expiry date, update the tier. Three operations." The Senior Archivist set down the incident report. "Last Thursday, the database server crashed mid-renewal. The fee was charged. The expiry was not extended. Thirty-seven members complained." The Junior Engineer looked at the code. "No transaction. Three separate statements in AUTOCOMMIT mode. The crash committed the first, the rest never ran." The Senior Archivist nodded. "Transactions exist to prevent exactly this. One unit, all or nothing. The crash happened — but with a transaction, the partial work rolls back automatically. The member is billed only if everything succeeds."

# Core Learning

## Concept Introduction

### The Four ACID Properties

```
A — Atomicity:   All operations in a transaction succeed, or none do
C — Consistency: A transaction moves the database from one valid state to another
I — Isolation:   Concurrent transactions do not interfere with each other
D — Durability:  Once committed, changes survive failures
```

### Atomicity — All or Nothing

```sql
-- Atomic transaction: fee charge + expiry extension + tier update
BEGIN;

UPDATE members SET balance = balance - renewal_fee WHERE member_id = ?;
UPDATE members SET expiry_date = expiry_date + INTERVAL '1 year' WHERE member_id = ?;
UPDATE members SET tier = calculate_new_tier(member_id) WHERE member_id = ?;

COMMIT;
-- If any statement fails (or the server crashes), the entire transaction rolls back
-- Member is left in their original state — no partial billing
```

```sql
-- With error handling (MySQL stored procedure style)
DELIMITER $$

CREATE PROCEDURE renew_membership(IN p_member_id INT, IN p_fee DECIMAL(10,2))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;  -- undo all changes if anything fails
        RESIGNAL;  -- re-raise the error to the caller
    END;

    START TRANSACTION;
    UPDATE members SET balance = balance - p_fee WHERE member_id = p_member_id;
    UPDATE members SET expiry_date = expiry_date + INTERVAL '1 year' WHERE member_id = p_member_id;
    COMMIT;
END$$

DELIMITER ;
```

### Consistency — Valid States Only

```sql
-- Constraints enforce consistency
CREATE TABLE members (
    member_id   SERIAL PRIMARY KEY,
    balance     DECIMAL(10,2) NOT NULL CHECK (balance >= 0),  -- can't go negative
    expiry_date DATE NOT NULL,
    tier        VARCHAR(20) NOT NULL CHECK (tier IN ('Initiate','Reader','Scholar','Archivist'))
);

-- A transaction violating a constraint is rolled back automatically
BEGIN;
UPDATE members SET balance = balance - 1000 WHERE member_id = 42;
-- If this makes balance < 0, the CHECK constraint fires → ROLLBACK
-- Database returns to previous valid state
COMMIT;
```

### Isolation — Invisible Intermediate States

```sql
-- Transaction A is updating; Transaction B reads
-- Without isolation: B could read A's partial update (dirty read)
-- With isolation: B sees the committed state before A's transaction

-- Session A:
BEGIN;
UPDATE accounts SET balance = 900 WHERE account_id = 1;  -- not yet committed

-- Session B (runs concurrently):
SELECT balance FROM accounts WHERE account_id = 1;
-- Returns 1000 (original committed value), not 900 (A's uncommitted change)
-- This is READ COMMITTED isolation

-- Session A:
COMMIT;

-- Session B (now):
SELECT balance FROM accounts WHERE account_id = 1;
-- Returns 900 (now committed)
```

### Durability — Persistent Commits

```sql
-- After COMMIT, data is safe even if the server crashes
BEGIN;
INSERT INTO orders (customer_id, total_amount, order_date)
VALUES (42, 150.00, NOW());
COMMIT;
-- Even if the server crashes 1 millisecond after COMMIT,
-- the row is in the write-ahead log (WAL) and will survive

-- The WAL is the durability mechanism:
-- 1. Write change to WAL (sequential disk write — fast)
-- 2. Acknowledge COMMIT to the client
-- 3. Eventually write to the main data file (background process)
-- On crash: replay WAL to recover committed transactions
```

### SAVEPOINTS — Partial Rollback

```sql
BEGIN;

INSERT INTO orders (...) VALUES (...);
SAVEPOINT order_created;

INSERT INTO order_lines (...) VALUES (...);  -- may fail for each line
SAVEPOINT lines_added;

-- If payment processing fails, roll back to after order was created
-- (keep the order, discard the lines — will retry)
ROLLBACK TO SAVEPOINT order_created;

-- Continue with a different approach
COMMIT;
```

## Common Mistakes

- **AUTOCOMMIT with multi-step operations**: Each statement auto-commits in AUTOCOMMIT mode. A partial failure leaves data inconsistent. Always use explicit transactions for operations with multiple dependent statements.
- **Long-running transactions**: Transactions hold locks and consume resources. Long-running transactions can block other users and increase memory usage. Commit as soon as the operation is complete.
- **Catching exceptions and not rolling back**: If you catch an error and continue without ROLLBACK, the transaction may contain partial changes that commit at the end — dangerous. Always ROLLBACK on exception.
- **Treating SAVEPOINT as a substitute for COMMIT**: SAVEPOINTs allow partial rollback within a transaction but do not make changes durable — you must still COMMIT the outer transaction.

## Mental Model

Think of a transaction as writing entries in a logbook with a pencil. You make all your changes in pencil. Only when you are certain everything is correct do you trace over the entries in permanent ink (COMMIT). If you discover a mistake partway through, you erase all the pencil marks (ROLLBACK) and start again. Other readers of the logbook only ever see the permanent ink — they cannot see your pencil marks while you are still writing. Durability means the ink is waterproof — even if the book gets wet (server crash), the permanent entries survive.

## Mini Summary

- ✔ Atomicity: all-or-nothing — partial transactions are automatically rolled back
- ✔ Consistency: transactions move the database between valid states; constraints enforced
- ✔ Isolation: intermediate states are hidden from concurrent transactions
- ✔ Durability: committed data survives failures via write-ahead logging
- ✔ BEGIN/START TRANSACTION starts a transaction; COMMIT persists; ROLLBACK undoes
- ✔ AUTOCOMMIT makes each statement a separate transaction — dangerous for multi-step ops

# Guided Practice Quest

Work through the guided steps to convert a two-statement operation from AUTOCOMMIT to an explicit transaction, write a transaction with a ROLLBACK on exception handler, and explain what the write-ahead log does using a provided crash scenario.

# Solo Practice Quest

Design transaction logic for the following Archive operations: (1) borrow_item(member_id, item_id) — check item is available, check member has no overdue items and is in good standing, create the loan record, update item status; (2) return_item(loan_id) — mark loan as returned, calculate and record any overdue fee, update member's fee balance; (3) process_payment(member_id, amount) — deduct from balance, record in payment history, check balance is not negative. For each: write the transaction SQL, identify which ACID property each statement in the transaction protects, and describe what inconsistent state would result if the transaction were not atomic. Then write a SAVEPOINT-based version of borrow_item that allows partial rollback to the point before item status was updated (for error recovery scenarios).

# Integration

**Mathematics**: ACID properties correspond to formal properties in database theory derived from Codd's relational model. Consistency is formalised as integrity constraint satisfaction: the database state s must satisfy a set of constraints C at the start and end of every transaction. Atomicity is formalised as all-or-nothing: for a transaction T = {o₁, o₂, ..., oₙ}, either all oᵢ are applied to the database state, or the final state is identical to the initial state. Isolation at the SERIALIZABLE level is formalised as serializability: the execution of concurrent transactions produces the same result as some serial ordering of those transactions. These formalisms come from the theory of concurrency control in databases, rooted in the work of Gray and Reuter (Transaction Processing: Concepts and Techniques, 1992).

**Sciences (Finance — Settlement Systems)**: The global financial system relies on ACID transactions. The SWIFT network processes millions of inter-bank transfers daily, each requiring atomic execution: debit sender's correspondent bank, credit receiver's correspondent bank, record in both ledgers. The 2012 Knight Capital Group incident illustrated the cost of non-atomic operations: a software bug caused 45 minutes of unintended trading, costing $440 million, because trade operations were not properly atomically paired with their hedges. Modern settlement systems (TARGET2 in Europe, Fedwire in the US) use database transactions with exactly the ACID properties described here — atomicity ensures no money creation or destruction, durability ensures settlement is permanent once confirmed.

# Lore Conclusion

"The renewal procedure is now transactional," the Junior Engineer reported. "If any step fails, the entire renewal rolls back. No partial charges." The Senior Archivist ran a test with a deliberate failure injected mid-transaction. "Rolled back cleanly. Member's original balance, original expiry. As if the renewal never started." She closed the incident report. "Thirty-seven complaints, zero data corrections — the crash caused temporary unavailability but no data inconsistency. Now it would be the same." She filed the report. "Next: isolation levels — the settings that control precisely how much concurrent transactions see of each other's work, and the trade-offs between consistency and performance."

---
