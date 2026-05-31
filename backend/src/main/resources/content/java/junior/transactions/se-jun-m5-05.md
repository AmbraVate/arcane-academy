---
id: se-jun-m5-05
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m5
moduleTitle: "Module 5: Databases"
moduleGlyph: "🗄️"
moduleSortOrder: 5
topicSlug: transactions
topicTitle: "Transactions"
topicSortOrder: 5
lesson: transactions
title: "Transactions"
sortOrder: 5
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [orms]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly names all four ACID properties and explains each in plain terms"
    - "Explains when @Transactional should be applied in a Spring service"
    - "Describes what ROLLBACK does and when it occurs"
    - "Explains the difference between READ COMMITTED and REPEATABLE READ isolation levels"
    - "Reflects on a real-world scenario where a transaction boundary is critical"
  keywords: [ACID, atomicity, consistency, isolation, durability, rollback, commit, "@Transactional"]
  modelAnswer: |
    // Spring @Transactional ensures the entire method runs in one transaction
    @Service
    public class BankingService {

        @Transactional  // rollback on any RuntimeException
        public void transferFunds(Long fromId, Long toId, BigDecimal amount) {
            Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new AccountNotFoundException(fromId));
            Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new AccountNotFoundException(toId));

            if (from.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient balance");
            }

            from.debit(amount);  // if this succeeds but to.credit() throws,
            to.credit(amount);   // the @Transactional rolls back BOTH changes

            accountRepository.save(from);
            accountRepository.save(to);
        }
    }
guidedSteps:
  - id: txn-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which ACID property ensures that if a bank transfer fails halfway through (money debited but not credited), the debit is automatically reversed?
    inputConfig:
      options:
        - "Consistency"
        - "Isolation"
        - "Atomicity"
        - "Durability"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Atomicity"]
      rejectedFeedback: "Atomicity means a transaction is all-or-nothing: either every operation within it succeeds (and is committed), or if any operation fails, all prior operations are rolled back. A half-completed bank transfer violates atomicity — the debit and credit must succeed together or not at all."
    hint: "Think about what 'atom' means — something that cannot be divided."
    reflectionPrompt: "Atomicity is the most critical property for financial systems. Without it, money can disappear into the gap between a debit and a failed credit."

  - id: txn-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In Spring, which annotation do you place on a service method to ensure all database operations within it run inside a single transaction?
    inputConfig:
      placeholder: "@annotation"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["@Transactional", "Transactional"]
      rejectedFeedback: "@Transactional (from org.springframework.transaction.annotation) wraps the annotated method in a database transaction. Spring commits on success and rolls back on unchecked exception (RuntimeException) by default."
    hint: "It is a Spring annotation whose name describes what it does."
    reflectionPrompt: "@Transactional on service methods is the standard Spring pattern. Avoid placing it on repository or controller methods — the service layer is the correct transaction boundary."

  - id: txn-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe the difference between READ COMMITTED and REPEATABLE READ isolation levels. Give a concrete example of a problem that REPEATABLE READ prevents but READ COMMITTED does not.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [read, changed, transaction, phantom, consistent, isolation]
      rejectedFeedback: "READ COMMITTED: a transaction can read data that another transaction has just committed, so if the same row is read twice in a transaction, its value may differ. REPEATABLE READ: once a row is read in a transaction, subsequent reads of the same row return the same value, even if another transaction commits a change. Example: a balance check followed by a debit — REPEATABLE READ ensures the balance seen when deciding to debit is the same as when the debit executes."
    hint: "Think about reading the same row twice in a transaction — what could change between the two reads?"
    reflectionPrompt: "Isolation levels are trade-offs between data consistency and concurrency performance. Higher isolation reduces concurrent throughput; lower isolation risks anomalies."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does the 'D' in ACID stand for, and what does it mean?"
    options:
      - "Determinism — the same input always produces the same output"
      - "Distribution — data is spread across multiple nodes"
      - "Durability — committed transactions persist even after a system crash"
      - "Deletion — completed transactions are automatically archived"
    correctIndex: 2
    feedback: "Durability means that once a transaction is committed, its changes are permanently stored — even if the database crashes immediately after. This is why databases write to disk (not just memory) before confirming a commit."
  - type: MULTIPLE_CHOICE
    question: "By default, Spring's @Transactional rolls back the transaction when:"
    options:
      - "Any checked exception is thrown"
      - "Any unchecked exception (RuntimeException or Error) is thrown"
      - "The method takes longer than 30 seconds"
      - "More than 100 rows are affected"
    correctIndex: 1
    feedback: "By default, @Transactional only rolls back for unchecked exceptions (RuntimeException and its subclasses, and Error). Checked exceptions do NOT trigger rollback by default. To rollback on a checked exception, use @Transactional(rollbackFor = MyCheckedException.class)."
retrieval:
  recall: "State all four ACID properties and give a one-sentence plain-English description of each."
  explain: "Why should @Transactional be placed on the service layer method rather than the repository or controller? What would go wrong if you placed it on individual repository calls?"
  mistakeId:
    code: |
      @Service
      public class OrderService {
          public void placeOrder(Order order) {
              inventory.decrease(order.getItemId(), order.getQuantity());
              payment.charge(order.getCustomerId(), order.getTotal());
              orderRepository.save(order);
          }
      }
    answer: "The placeOrder() method lacks @Transactional. If payment.charge() succeeds but orderRepository.save() throws an exception, the inventory has been decreased and the payment charged, but no order record exists. The system is now in an inconsistent state. Adding @Transactional ensures all three operations succeed together or are all rolled back."
---

# Hook

Imagine a bank transfer: £500 moves from your account to a friend's. The bank's system deducts £500 from your account — and then crashes before crediting your friend. The money has vanished. When the system restarts, it must know whether to retry the credit, restore your balance, or do something else entirely. Without transactions, this scenario is a catastrophe. With them, it is just a temporary inconvenience.

Transactions are the mechanism by which databases guarantee that sequences of operations are treated as a single, indivisible unit. They are one of the most important reliability guarantees in all of computing — and understanding them is essential for building any system that handles real money, inventory, or other state that must always be consistent.

> Think of a multi-step process in daily life where every step must succeed or the entire process must be undone. What are the consequences if it is interrupted halfway through?

# Lore Introduction

The Academy's Treasury maintains strict protocols for the transfer of magical reagents between vaults. Before any transfer begins, the Senior Treasurer invokes the **Binding Rune** — a magical contract that encompasses every action in the transfer. If the reagents leave the source vault but are lost before reaching the destination, the Binding Rune reverses the departure as if it never happened. Only when every step of the transfer completes successfully is the Binding Rune sealed.

In database terms, the Binding Rune is a transaction. COMMIT seals it; ROLLBACK reverses it. Spring's `@Transactional` is your way of invoking the Binding Rune automatically.

# Core Learning

## Concept Introduction

A **transaction** is a sequence of database operations treated as a single unit. Either all operations succeed (COMMIT) or all are reversed (ROLLBACK).

**ACID properties** — the four guarantees transactions provide:

| Property | Meaning |
|---|---|
| **Atomicity** | All operations succeed together, or none do |
| **Consistency** | The database moves from one valid state to another |
| **Isolation** | Concurrent transactions do not interfere with each other |
| **Durability** | Committed changes persist even after crashes |

**SQL transaction syntax:**
```sql
BEGIN;
UPDATE accounts SET balance = balance - 500 WHERE id = 1;
UPDATE accounts SET balance = balance + 500 WHERE id = 2;
COMMIT;  -- or ROLLBACK; if something went wrong
```

**Spring's @Transactional:**
```java
@Transactional
public void transferFunds(Long fromId, Long toId, BigDecimal amount) {
    // All DB operations here run in ONE transaction
    // Any RuntimeException triggers automatic ROLLBACK
}
```

**Isolation levels (most common):**
- `READ_COMMITTED` — a transaction sees only committed data; same row may read differently on second read
- `REPEATABLE_READ` — once a row is read, subsequent reads in the same transaction return the same value

## Why It Matters

Without transactions, partial failures leave data in inconsistent states. In a web application handling concurrent users, transactions also protect against race conditions — two users trying to book the last seat simultaneously should not both succeed. `@Transactional` in Spring is the primary mechanism for ensuring your service operations are atomic and isolated.

## Worked Examples

**Example 1 — Fund transfer with @Transactional**
```java
@Transactional
public void transfer(Long fromId, Long toId, BigDecimal amount) {
    Account from = accountRepo.findById(fromId).orElseThrow();
    Account to   = accountRepo.findById(toId).orElseThrow();

    from.setBalance(from.getBalance().subtract(amount));
    to.setBalance(to.getBalance().add(amount));
    // If save() throws, BOTH balance changes are rolled back
    accountRepo.save(from);
    accountRepo.save(to);
}
```

**Example 2 — Rollback on checked exception**
```java
@Transactional(rollbackFor = InsufficientFundsException.class)
public void withdraw(Long accountId, BigDecimal amount) throws InsufficientFundsException {
    Account account = accountRepo.findById(accountId).orElseThrow();
    if (account.getBalance().compareTo(amount) < 0) {
        throw new InsufficientFundsException("Balance too low"); // triggers rollback
    }
    account.setBalance(account.getBalance().subtract(amount));
    accountRepo.save(account);
}
```

**Example 3 — Read-only transaction for performance**
```java
@Transactional(readOnly = true)  // hint to JPA: no dirty-checking needed
public List<Order> getOrdersForCustomer(Long customerId) {
    return orderRepo.findByCustomerId(customerId);
}
```

## Common Mistakes

- **Missing @Transactional on multi-step operations** — if your service method updates two tables, it must be transactional or a partial failure leaves inconsistent data.
- **@Transactional on private methods** — Spring's proxy-based AOP cannot intercept private methods; `@Transactional` on a private method is silently ignored.
- **Expecting checked exceptions to rollback** — by default, only `RuntimeException` triggers rollback; add `rollbackFor` for checked exceptions.
- **Long transactions holding locks** — a transaction that holds a row lock while waiting for a slow external API call blocks other users; keep transactions short.
- **Calling @Transactional method from within the same class** — Spring's proxy is bypassed; the inner call does not start a new transaction.

## Mental Model

A transaction is like a dry-erase board in a meeting. You write changes on it as you go, but nothing is permanent until someone takes a photo and erases the board (COMMIT). If the meeting is interrupted, you simply erase the board back to blank (ROLLBACK). The board holds your tentative changes; the committed photo holds the permanent ones.

## Mini Summary

✔ ACID: Atomicity (all-or-nothing), Consistency (valid states), Isolation (no interference), Durability (persists crashes).
✔ `@Transactional` in Spring wraps a service method in one database transaction, with automatic ROLLBACK on RuntimeException.
✔ COMMIT makes changes permanent; ROLLBACK reverses them entirely.
✔ `READ_COMMITTED` allows non-repeatable reads; `REPEATABLE_READ` prevents them.
✔ Keep transactions short — long transactions hold locks and reduce concurrency.

# Guided Practice Quest

**The Binding Rune**
The Academy's Treasury is implementing a magical reagent transfer system. Identify the correct transaction boundaries and ACID properties for each scenario.
Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Design a Spring service method `processPurchase(Long customerId, Long productId, int quantity)` that: (1) checks and decrements inventory; (2) creates an order record; (3) processes payment. Write the complete service method with `@Transactional`, including meaningful exception handling. Identify where in the method a failure should trigger a rollback and explain why. Then write a second version using `@Transactional(readOnly = true)` for a read-only query method, explaining why this annotation matters for performance.

# Integration

**Connecting to Mathematics — Formal Consistency in Systems**
The concept of ACID transactions is deeply connected to the mathematical concept of **invariants** — properties of a system that must always be true. In mathematics, a loop invariant is a condition that is true at the start of every iteration. In a database, an integrity constraint (like "account balance must never be negative") is an invariant.

Transactions enforce invariants by ensuring that the database only moves between states where all constraints hold. A partial bank transfer would violate the invariant "the sum of all account balances equals the initial total". The COMMIT point is the only moment when the outside world observes the system, and at that moment, all invariants must hold. This is the mathematical formalisation of consistency — the 'C' in ACID.

> Can you think of a real-world system that has strict invariants — rules that must always be true regardless of what operations are performed? What would it mean for that system to have a "transaction" that temporarily violates an invariant while changes are in progress?

# Lore Conclusion

The Binding Rune seals with a soft golden light. The reagents have transferred cleanly: every step completed, every ledger balanced, every constraint satisfied. The Treasury has not lost a single unit of magical matter in three hundred years of operation — thanks entirely to the integrity of the transaction protocol.

With databases now conquered — from raw SQL to relationships, ORM, and transactions — the junior engineer is ready for a new frontier. In Module 6, the focus shifts from building features to building confidence: the discipline of testing. Because a system no one can verify is a system no one can safely change.

---
