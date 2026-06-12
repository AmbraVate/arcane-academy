---
id: se-jun-m3-03
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m3
moduleTitle: "Module 3: Exception Handling"
moduleGlyph: "⚠️"
moduleSortOrder: 3
topicSlug: try_with_resources
topicTitle: "Try-with-Resources"
topicSortOrder: 3
lesson: try_with_resources
title: "Try-with-Resources"
sortOrder: 3
difficulty: 2
estimatedMinutes: 25
xpReward: 70
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m3-02]
integrationDomains: [custom_exceptions, error_strategies]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Opens a resource inside a try-with-resources declaration rather than a plain try block"
    - "Explains that the resource is automatically closed even if an exception is thrown"
    - "States that the resource must implement AutoCloseable (or Closeable)"
    - "Recognises that multiple resources can be declared in a single try statement, separated by semicolons"
    - "Understands that try-with-resources supersedes the need for a finally block solely for closing"
  keywords: [try-with-resources, AutoCloseable, Closeable, close, finally, resource leak, suppress, multiple resources]
  modelAnswer: |
    // Single resource — automatically closed after the block exits
    try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    } catch (IOException e) {
        System.err.println("Could not read file: " + e.getMessage());
    }
    // reader.close() is called automatically here — even if an exception is thrown

    // Multiple resources — closed in reverse declaration order
    try (
        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?")
    ) {
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        // process rs...
    } catch (SQLException e) {
        throw new DataAccessException("Failed to load user", e);
    }
    // stmt closed first, then conn — in reverse order

    // Custom AutoCloseable resource
    class ManagedCache implements AutoCloseable {
        void put(String key, Object value) { /* ... */ }
        @Override
        public void close() { System.out.println("Cache cleared"); }
    }

    try (ManagedCache cache = new ManagedCache()) {
        cache.put("user:1", fetchUser(1));
    }
    // cache.close() called automatically
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      The following code has a resource leak — the FileInputStream is not always closed. Rewrite it using try-with-resources so the stream is always closed without a finally block.

      ```java
      FileInputStream fis = null;
      try {
          fis = new FileInputStream("config.properties");
          int data = fis.read();
          process(data);
      } catch (IOException e) {
          log.error("Read failed", e);
      } finally {
          if (fis != null) {
              try { fis.close(); } catch (IOException ignored) {}
          }
      }
      ```
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [try, FileInputStream, catch, IOException, close]
      rejectedFeedback: |
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            int data = fis.read();
            process(data);
        } catch (IOException e) {
            log.error("Read failed", e);
        }
        The resource is declared inside the try(...) header. Java calls fis.close() automatically when the block exits — whether normally or via exception. The entire finally block is eliminated.
    hint: "Declare the resource inside try(...) rather than before the try block."
    reflectionPrompt: "The old pattern required a nested try/catch inside finally just to safely close the stream. Try-with-resources makes that boilerplate disappear — and makes the happy-path code clearer."

  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      You need to open a database Connection and then a PreparedStatement from it. Both must be closed when you are done — the PreparedStatement first, then the Connection. Write a try-with-resources block that declares both resources. Explain what order they are closed in.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [Connection, PreparedStatement, reverse, order, close]
      rejectedFeedback: |
        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT id FROM orders WHERE customer_id = ?")
        ) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            // process...
        } catch (SQLException e) {
            throw new DataAccessException("Query failed", e);
        }
        Resources are closed in reverse declaration order: stmt is closed first, then conn. This mirrors the order of dependency — you must close the thing that depends on the connection before the connection itself.
    hint: "Multiple resources are separated by semicolons inside the try(...). They close in reverse order — last declared, first closed."
    reflectionPrompt: "Reverse-order closing is intentional: it mirrors a stack. Things that depend on earlier-declared resources are closed before those resources themselves disappear."

  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      You are writing a utility class that manages a temporary directory. When done, the directory should be deleted. How would you implement AutoCloseable so this class can be used with try-with-resources? Write the class skeleton and show an example of using it.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [AutoCloseable, close, try, implements]
      rejectedFeedback: |
        class TempDirectory implements AutoCloseable {
            private final Path dir;

            TempDirectory(String prefix) throws IOException {
                this.dir = Files.createTempDirectory(prefix);
            }

            public Path getPath() { return dir; }

            @Override
            public void close() throws IOException {
                Files.walk(dir)
                     .sorted(Comparator.reverseOrder())
                     .forEach(p -> p.toFile().delete());
            }
        }

        // Usage
        try (TempDirectory tmp = new TempDirectory("upload-")) {
            Files.copy(uploadedFile, tmp.getPath().resolve("file.dat"));
            processUpload(tmp.getPath());
        }
        // tmp.close() deletes the directory automatically
    hint: "Implement AutoCloseable and put your cleanup logic in close(). The method signature is: public void close() throws Exception."
    reflectionPrompt: "Any object that acquires something at construction and must release it on completion is a candidate for AutoCloseable. File handles, network connections, locks, caches — all fit this pattern."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What interface must a class implement to be used in a try-with-resources statement?"
    options:
      - "Serializable"
      - "AutoCloseable"
      - "Runnable"
      - "Cloneable"
    correctIndex: 1
    tier: RECALL
    feedback: "AutoCloseable (and its subinterface Closeable) is the contract the JVM uses to know how to close a resource. If your class implements AutoCloseable and provides a close() method, it can be declared in a try(...) header and will be closed automatically when the block exits."

  - type: MULTIPLE_CHOICE
    question: "When two resources are declared in a single try-with-resources block, in what order are they closed?"
    options:
      - "The order they were declared"
      - "Reverse declaration order — last declared, first closed"
      - "They are closed simultaneously"
      - "Alphabetical order by variable name"
    correctIndex: 1
    tier: APPLICATION
    feedback: "Resources close in reverse declaration order. If you declare Connection conn first and PreparedStatement stmt second, stmt is closed before conn. This prevents closing a resource that another open resource still depends on."

retrieval:
  recall: "What is a resource leak? How does try-with-resources prevent one?"
  explain: "A colleague says: 'I always use finally to close my resources — try-with-resources is just syntactic sugar.' Is this accurate? What does try-with-resources handle that a naive finally block does not?"
  mistakeId:
    code: |
      try (Scanner scanner = new Scanner(System.in)) {
          String input = scanner.nextLine();
          process(input);
      }
      scanner.close(); // explicit close after the block
    answer: "The explicit scanner.close() call after the try block is redundant and will cause a compile error — scanner is out of scope outside the try block. Try-with-resources already closes the scanner automatically when the block exits. The correct pattern is simply the try-with-resources block alone, with no further close() call."
---

# Hook

You open a database connection. You run a query. Then an exception is thrown — and the connection is never closed.

The connection sits there, leaking. The connection pool fills up. Other requests start failing.

Resource leaks are silent until they are catastrophic. Try-with-resources is how Java guarantees cleanup — without boilerplate, without forgetting.

# Lore Introduction

*"Every spell in the Academy opens a channel,"* the instructor explains, gesturing at the glowing conduits of energy threading through the walls. *"If you open a channel and do not close it, it stays open — drawing power until the Academy's reserve is drained."*

*"Some apprentices use a finally clause — a closing ritual performed after the spell. But apprentices forget. Exceptions interrupt. The finally block itself can fail."*

*"The third year curriculum introduces the Bound Channel — a channel that seals itself. Open it, use it, and it closes the moment you step out of its scope. Whether you step out normally or flee an exception."*

That is try-with-resources.

# Core Learning

## Concept Introduction

**Try-with-resources** (Java 7+) declares one or more resources in the `try(...)` header. The JVM calls `close()` on them automatically when the block exits — regardless of whether it exits normally or via exception.

A resource is any class that implements **`AutoCloseable`** (or its subinterface `Closeable`).

```java
// Basic form
try (ResourceType name = new ResourceType()) {
    // use name
} catch (SomeException e) {
    // handle
}
// name.close() is called here automatically
```

### Before vs After

**Before (manual finally):**
```java
FileInputStream fis = null;
try {
    fis = new FileInputStream("data.txt");
    return fis.read();
} finally {
    if (fis != null) {
        try { fis.close(); } catch (IOException ignored) {} // nested try!
    }
}
```

**After (try-with-resources):**
```java
try (FileInputStream fis = new FileInputStream("data.txt")) {
    return fis.read();
}
// fis.close() called automatically — no finally, no null check, no nested try
```

### Multiple Resources

Declare multiple resources separated by semicolons. They close in **reverse declaration order**:

```java
try (
    Connection conn = ds.getConnection();          // opened first
    PreparedStatement stmt = conn.prepareStatement(sql) // opened second
) {
    // use conn and stmt
}
// stmt.close() called first, then conn.close()
```

### Custom AutoCloseable

Any class can participate:

```java
class DatabaseTransaction implements AutoCloseable {
    private final Connection conn;
    private boolean committed = false;

    DatabaseTransaction(DataSource ds) throws SQLException {
        this.conn = ds.getConnection();
        this.conn.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        conn.commit();
        committed = true;
    }

    @Override
    public void close() throws SQLException {
        if (!committed) conn.rollback(); // auto-rollback on exception
        conn.close();
    }
}

// Usage: if anything throws before commit(), the transaction rolls back automatically
try (DatabaseTransaction tx = new DatabaseTransaction(ds)) {
    userRepo.save(user);
    auditLog.record(user.getId());
    tx.commit();
}
```

## Why It Matters

Try-with-resources fixed one of Java's most reliable sources of production failure — the leaked resource — by making correct cleanup the path of least resistance:

- Every unclosed connection, stream, or file handle stays claimed until something gives; under load that's the classic slow-death outage where the pool empties and the app hangs at 3 a.m., hours after the actual bug ran
- The pre-Java-7 alternative — nested try/finally with null checks and a close() that can itself throw — was so error-prone that even careful engineers got it wrong; suppressed exceptions then *hid the original error*, sending you debugging the wrong failure
- The compiler becomes your reviewer: any `AutoCloseable` declared in the try header is closed in exactly the right order, exception or not — and modern IDEs and static analysis flag resources that should use it
- It's interview-and-review canon: reaching for manual close() in 2026 code marks the author as either unaware of fifteen years of language history or unconvinced that failure paths happen — both worth catching

Resource leaks are invisible in development (one user, short runs) and inevitable in production (thousands of users, weeks of uptime). This syntax is the cheapest insurance the language offers.

## Common Mistakes

- **Closing the resource again after the block.** It is already closed — and the variable is out of scope anyway.
- **Declaring the resource before the try block.** Then it is not managed by try-with-resources and must be closed manually.
- **Ignoring close() exceptions.** If `close()` itself throws, Java suppresses it (attaches to the primary exception via `getSuppressed()`). You can retrieve and log suppressed exceptions in your catch block.

## Mental Model

Try-with-resources is a hotel key card instead of a metal key on trust. With metal keys (manual close()), checkout depends on every guest remembering to return the key at the desk — and guests who leave through the fire exit (exceptions), get called away mid-stay (early returns), or simply forget (developer error) leave rooms locked and unusable until a manual audit finds them (the connection-pool exhaustion incident). Key cards change the contract: the *system* expires the card at checkout, automatically, no matter how the guest leaves — gracefully, abruptly, or mid-emergency. Declaring a resource in the try header is issuing the card: scope is the stay, and the expiry (close()) is guaranteed by the building, not the guest's memory. The fine print completes the analogy: multiple cards expire in reverse order of issue (last opened, first closed — dependencies respected), and if the door mechanism itself jams during checkout (close() throws) while the guest is already fleeing a fire (original exception in flight), the fire remains the headline and the jam is noted in the margin (suppressed exceptions) — the original emergency is never masked by cleanup trouble.

## Mini Summary

- Try-with-resources guarantees `close()` is called on every declared resource when the block exits
- Resources must implement `AutoCloseable` (or `Closeable`)
- Multiple resources close in reverse declaration order
- Eliminates the need for finally blocks used solely for cleanup
- Any class can implement AutoCloseable — transactions, caches, temp files, locks

# Guided Practice Quest

Work through the three steps: rewriting a leaking stream to use try-with-resources, managing two dependent resources, and implementing a custom AutoCloseable.

# Solo Practice Quest

You are implementing a CSV export service. It opens a database connection, runs a query, and writes results line-by-line to a FileWriter. Both the database connection and the file writer must be closed even if the query fails or the file write throws. Implement the method using try-with-resources. Include a custom wrapper that treats the entire export as an AutoCloseable operation, rolling back or cleaning up on failure.

# Integration

Try-with-resources is a specific application of the **Resource Acquisition Is Initialisation (RAII)** pattern — a design principle from C++ where resource lifetime is tied to object scope. Java's AutoCloseable makes RAII explicit and enforceable at the language level. The pattern also connects to the **Dependency Inversion** principle: the try-with-resources block depends on the AutoCloseable abstraction, not the concrete resource type. Any resource that correctly implements AutoCloseable participates in the same lifetime guarantee — database connections, HTTP clients, thread pool executors, file handles, cryptographic contexts. In distributed systems, resource leaks compound: a connection leak in a service that handles 1,000 requests/second may only become visible under load, when the pool exhausts and upstream services begin timing out — a failure that looks like a network issue but is actually a lifecycle management failure.

# Lore Conclusion

*"The Bound Channel closes the moment you step out of its scope,"* the instructor says, watching a student successfully cast without leaving any conduit open. *"Whether you exit with a spell complete, or flee an unexpected counter-spell — the channel seals. No drain. No leak."*

*"The old masters wrote closing rituals into every spell. Some were forgotten. Some failed mid-ritual. Some forgot to write them at all."*

*"The Bound Channel does not forget. It cannot. Forgetting is not a failure mode it permits."*

---
