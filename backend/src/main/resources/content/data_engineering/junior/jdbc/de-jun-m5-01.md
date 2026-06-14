---
id: de-jun-m5-01
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m5
moduleTitle: "Module 5: Application Data Access"
moduleGlyph: "🔌"
moduleSortOrder: 5
topicSlug: jdbc
topicTitle: "JDBC"
topicSortOrder: 1
lesson: jdbc
title: "JDBC"
sortOrder: 1
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m4-04]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what JDBC is and its role in Java database access
    - Distinguishes Statement from PreparedStatement and explains why PreparedStatement is preferred
    - Explains how to iterate over a ResultSet
    - Describes proper resource management with try-with-resources
    - Identifies the SQL injection vulnerability in string-concatenated queries
  keywords: [JDBC, Connection, PreparedStatement, Statement, ResultSet, try-with-resources, SQL injection, getConnection, executeQuery, executeUpdate, close, DataSource]
  modelAnswer: |
    JDBC (Java Database Connectivity) is the standard Java API for relational database access. The core classes: Connection (session with the database), PreparedStatement (parameterised query — prevents SQL injection), ResultSet (query results as a cursor). Statement (string SQL) must never be used with user input — SQL injection risk. PreparedStatement uses ? placeholders for parameters set with setString/setInt/etc. Resources (Connection, PreparedStatement, ResultSet) must be closed — use try-with-resources to guarantee closing. DataSource (from connection pool) is preferred over DriverManager for production code.
guidedSteps:
  - id: de-jun-m5-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A developer writes: String query = "SELECT * FROM users WHERE name = '" + userInput + "'"; Statement stmt = conn.createStatement(); stmt.execute(query); What is the critical problem?
    inputConfig:
      options:
        - "Statement is slower than PreparedStatement"
        - "SQL injection: if userInput contains SQL syntax (e.g. ' OR 1=1 --), the query structure is altered"
        - "The SELECT * retrieves too many columns"
        - "conn.createStatement() is deprecated"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SQL injection: if userInput contains SQL syntax (e.g. ' OR 1=1 --), the query structure is altered"]
      rejectedFeedback: "SQL injection is a critical security vulnerability. If userInput is: ' OR '1'='1, the full query becomes: SELECT * FROM users WHERE name = '' OR '1'='1' — which returns all users, bypassing authentication. Worse inputs can DROP tables or exfiltrate data. The fix: always use PreparedStatement with ? placeholders. PreparedStatement treats parameter values as pure data, never as SQL syntax — the query structure is fixed at prepare time, and user input cannot alter it. This is the single most important security rule in database programming."
    hint: "What happens if the user inputs: '; DROP TABLE users; --"
    reflectionPrompt: "Can PreparedStatement ever be vulnerable to SQL injection? When?"
  - id: de-jun-m5-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In JDBC, database resources (Connection, Statement, ResultSet) should be closed using ________ to guarantee they are closed even if an exception is thrown.
    inputConfig:
      placeholder: "try-with-resources"
    markingRule:
      matchMode: CONTAINS
      accepted: ["try-with-resources", "try with resources", "AutoCloseable", "finally", "try-finally"]
      rejectedFeedback: "JDBC resources implement AutoCloseable. Using try-with-resources syntax: try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) { ... } guarantees that both ps.close() and conn.close() are called even if an exception is thrown inside the block. Without this guarantee, unclosed connections eventually exhaust the connection pool. Pre-Java 7, this required a try-catch-finally block with explicit null checks before closing — error-prone. try-with-resources is the idiomatic Java solution for all AutoCloseable resources."
    hint: "The Java language feature that ensures close() is called automatically, even on exception."
    reflectionPrompt: "What happens if you forget to close a Connection from a connection pool? What does that cause over time?"
  - id: de-jun-m5-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why DataSource is preferred over DriverManager.getConnection() for production applications.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [pool, reuse, overhead, DriverManager, connection pooling, HikariCP, new connection, cost, DataSource, manage]
      rejectedFeedback: "DriverManager.getConnection() opens a new physical database connection on every call — expensive (TCP handshake, auth, session setup = 50-200ms per connection). DataSource is an abstraction that typically wraps a connection pool (HikariCP, c3p0, DBCP): getConnection() borrows an already-open connection from the pool (microseconds). DataSource also allows connection configuration (max pool size, timeout, health check queries) to be centralised and managed. In production, always inject a DataSource; never call DriverManager directly from application code."
    hint: "DataSource wraps a connection pool — what does that mean for per-request connection cost?"
    reflectionPrompt: "How does Spring Boot configure and inject a DataSource automatically?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "When iterating over a ResultSet, rs.next() returns false when:"
    options:
      - "The current row has null values"
      - "There are no more rows — the cursor has passed the last row"
      - "The query returned zero rows"
      - "The connection was closed"
    correctIndex: 1
    feedback: "rs.next() moves the ResultSet cursor to the next row and returns true if there is a row, false if no more rows exist. The standard pattern: while (rs.next()) { // process current row }. This handles both zero-row results (the while loop never enters) and multiple rows (iterates until rs.next() returns false). Column values are accessed with rs.getString(columnName), rs.getInt(columnIndex), etc. — after calling next() at least once. Note: rs.next() should be called before reading any column — the cursor starts before the first row."
  - type: MULTIPLE_CHOICE
    question: "Which JDBC method executes a query that returns rows (SELECT), and which executes a statement that modifies data (INSERT/UPDATE/DELETE)?"
    options:
      - "Both use executeQuery()"
      - "executeQuery() for SELECT; executeUpdate() for INSERT/UPDATE/DELETE"
      - "executeUpdate() for SELECT; executeQuery() for modifications"
      - "execute() for everything"
    correctIndex: 1
    feedback: "executeQuery() executes a SELECT and returns a ResultSet. executeUpdate() executes INSERT, UPDATE, or DELETE and returns the number of affected rows (int). Using the wrong method causes a runtime exception. There is also execute() which can execute any SQL and returns a boolean indicating whether the result was a ResultSet — used when the query type is unknown at compile time (rare in application code). Always use the specific method: executeQuery() for reads, executeUpdate() for writes."
retrieval:
  recall: "Write a complete JDBC method that accepts a member_id and returns the member's name and email using PreparedStatement and try-with-resources."
  explain: "Explain the JDBC connection lifecycle: from DataSource.getConnection() through PreparedStatement preparation, execution, ResultSet iteration, and proper resource closure."
  mistakeId:
    code: "public List<Member> findByStatus(String status) throws SQLException {\n    Connection conn = DriverManager.getConnection(URL, USER, PASS);\n    Statement stmt = conn.createStatement();\n    ResultSet rs = stmt.executeQuery(\"SELECT * FROM members WHERE status = '\" + status + \"'\");\n    List<Member> members = new ArrayList<>();\n    while (rs.next()) {\n        members.add(new Member(rs.getInt(\"id\"), rs.getString(\"name\")));\n    }\n    return members;\n}"
    answer: "Three critical problems: (1) SQL injection: status parameter concatenated directly into SQL — an attacker can pass status = \"' OR 1=1 --\" to return all members. Fix: use PreparedStatement with setString(). (2) Resource leak: Connection, Statement, ResultSet are never closed. If an exception is thrown mid-iteration, all three leak. Fix: use try-with-resources. (3) DriverManager.getConnection() opens a new physical connection every call — expensive. Fix: inject DataSource and call dataSource.getConnection(). Corrected: try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(\"SELECT id, name FROM members WHERE status = ?\")) { ps.setString(1, status); try (ResultSet rs = ps.executeQuery()) { while (rs.next()) { ... } } }"
---

# Hook

All the SQL in this curriculum has been written as standalone queries. In production, SQL is executed from application code — typically Java. JDBC (Java Database Connectivity) is the standard API for this bridge: it defines how Java code connects to a database, sends SQL, and processes results. Before ORMs abstract this away, understanding JDBC means understanding what all database access ultimately does.

# Lore Introduction

"The Archive's Java application queries the database with string concatenation," the Senior Archivist said, showing the code. "Every search query builds a SQL string from the user's input directly." The Junior Engineer scanned the code. "SQL injection vulnerability. An attacker inputs a search term with SQL syntax and they can read — or delete — any data in the Archive." The Senior Archivist nodded. "Every query. PreparedStatement prevents this by treating user input as data, never as SQL. But there are three more problems: DriverManager opens a new connection every time, resources are never closed, and SELECT * fetches columns the application doesn't use." She opened a new file. "Let me show you what correct JDBC looks like."

# Core Learning

## Concept Introduction

### The JDBC Core Classes

```
DriverManager / DataSource  → manages physical connections to the database
Connection                  → a session with the database; can commit/rollback
PreparedStatement           → compiled, parameterised SQL statement
Statement                   → plain SQL (never use with user input)
ResultSet                   → cursor over query results
```

### Vulnerable Code vs Correct Code

```java
// VULNERABLE: SQL injection via string concatenation
// Never do this with user input
String sql = "SELECT * FROM members WHERE email = '" + userEmail + "'";
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(sql);
// If userEmail = "' OR 1=1 --" → returns ALL members

// CORRECT: PreparedStatement with parameterised query
String sql = "SELECT member_id, name, email FROM members WHERE email = ?";
try (PreparedStatement ps = conn.prepareStatement(sql)) {
    ps.setString(1, userEmail);   // parameter 1 = first ?
    try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            int id = rs.getInt("member_id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            // process row
        }
    }
}
// User input is ALWAYS treated as data, never as SQL
```

### Complete JDBC Example with try-with-resources

```java
public Optional<Member> findMemberById(DataSource dataSource, int memberId)
        throws SQLException {

    String sql = "SELECT member_id, name, email, tier FROM members WHERE member_id = ?";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, memberId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return Optional.of(new Member(
                    rs.getInt("member_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("tier")
                ));
            }
            return Optional.empty();
        }
    }
    // conn, ps, rs all auto-closed here — even if exception thrown
}
```

### Transactions in JDBC

```java
public void transferLoan(DataSource dataSource, int loanId, int fromMember, int toMember)
        throws SQLException {

    try (Connection conn = dataSource.getConnection()) {
        conn.setAutoCommit(false);   // begin explicit transaction

        try {
            try (PreparedStatement ps1 = conn.prepareStatement(
                    "UPDATE loans SET member_id = ? WHERE loan_id = ?")) {
                ps1.setInt(1, toMember);
                ps1.setInt(2, loanId);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(
                    "INSERT INTO loan_transfers (loan_id, from_member, to_member, transferred_at) " +
                    "VALUES (?, ?, ?, NOW())")) {
                ps2.setInt(1, loanId);
                ps2.setInt(2, fromMember);
                ps2.setInt(3, toMember);
                ps2.executeUpdate();
            }

            conn.commit();     // both succeeded — commit

        } catch (SQLException e) {
            conn.rollback();   // either failed — rollback both
            throw e;
        } finally {
            conn.setAutoCommit(true);  // restore for pool reuse
        }
    }
}
```

### Batch Updates in JDBC

```java
public void insertLogs(DataSource dataSource, List<LogEntry> entries)
        throws SQLException {

    String sql = "INSERT INTO logs (type, message, created_at) VALUES (?, ?, NOW())";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        conn.setAutoCommit(false);

        for (LogEntry entry : entries) {
            ps.setString(1, entry.type());
            ps.setString(2, entry.message());
            ps.addBatch();      // add to batch, don't execute yet
        }

        int[] rowsAffected = ps.executeBatch();  // execute all at once
        conn.commit();

        System.out.println("Inserted " + rowsAffected.length + " rows");
    }
}
```

### DataSource vs DriverManager

```java
// DriverManager: NOT for production — new connection every time
Connection conn = DriverManager.getConnection(
    "jdbc:postgresql://localhost:5432/archive",
    "archive_user",
    "password"
);

// DataSource with HikariCP connection pool: production standard
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:postgresql://localhost:5432/archive");
config.setUsername("archive_user");
config.setPassword("password");
config.setMaximumPoolSize(10);
config.setConnectionTimeout(30000);

DataSource dataSource = new HikariDataSource(config);
// In Spring Boot: auto-configured from application.properties
// spring.datasource.url=jdbc:postgresql://localhost:5432/archive
// spring.datasource.hikari.maximum-pool-size=10
```

## Why It Matters

JDBC is the foundation every Java data technology stands on — JPA, Hibernate, Spring Data all compile down to these calls:

- When the abstraction leaks (connection exhaustion, transaction weirdness, batch performance), the debugging happens at the JDBC layer
- PreparedStatement is the canonical defence against SQL injection — understanding *why* placeholders are safe is non-negotiable security knowledge
- Resource leaks from unclosed connections are a classic production outage; try-with-resources exists for exactly this

You may write little raw JDBC in your career, but you will read stack traces through it forever. Knowing the layer beneath your ORM is what makes you the debugger of last resort.

## Common Mistakes

- **String concatenation in queries**: The single most common and dangerous JDBC mistake. Always use PreparedStatement with ? parameters for all user-controlled input.
- **Not closing resources**: Connection, Statement, and ResultSet each hold database resources. A leaked connection exhausts the pool; a leaked ResultSet holds a server-side cursor. Always use try-with-resources.
- **Not resetting AutoCommit**: When returning a connection to the pool with autoCommit = false, subsequent borrowers inherit the transaction state. Always set autoCommit back to true in a finally block.
- **Using Statement when column names are dynamic**: If column names or table names are dynamic (user-selectable reports), PreparedStatement cannot parameterise them. Use an allow-list of valid names in application code and select with string formatting — never with raw user input.

## Mental Model

JDBC is the plumbing — the pipes and fittings that connect Java to a database. DriverManager is like drilling a new hole in the wall for every plumbing connection: functional but wasteful. DataSource with a connection pool is like having a set of pre-drilled, permanently open pipes — each request borrows a pipe for a moment and returns it. PreparedStatement is like a stencil: the shape of the query is fixed; only the colours (parameter values) change. Statement is like free-hand drawing on the wall — flexible but dangerous if user input controls the brush.

## Mini Summary

- ✔ PreparedStatement with ? parameters: mandatory for all user-controlled input — prevents SQL injection
- ✔ try-with-resources: guarantees Connection/PreparedStatement/ResultSet are closed
- ✔ DataSource (connection pool) preferred over DriverManager for production code
- ✔ conn.setAutoCommit(false) for multi-statement transactions; reset in finally
- ✔ ps.addBatch() + ps.executeBatch() for batch INSERT/UPDATE operations

# Guided Practice Quest

Work through the guided steps to identify the SQL injection vulnerability in a provided JDBC method, rewrite it using PreparedStatement, wrap it in try-with-resources, and implement a transaction that inserts a loan and updates the item status atomically.

# Solo Practice Quest

Implement the following JDBC methods for the Archive system (no ORM — pure JDBC): (1) findLoansByMember(DataSource ds, int memberId) — returns all active loans for a member; (2) createLoan(DataSource ds, int memberId, int itemId) — inserts a new loan and updates the item's status to 'on_loan' atomically; (3) bulkUpdateOverdueFees(DataSource ds, List<OverdueFee> fees) — batch INSERT for daily fee generation; (4) searchMembers(DataSource ds, String nameFragment, String tier, int limit) — searches by partial name and optionally filters by tier (both parameters may be null meaning "no filter"). For searchMembers, explain why you cannot use PreparedStatement ? parameters for the conditional WHERE clause structure and what safe alternative you use.

# Integration

**Mathematics**: PreparedStatement parameterisation is a formal application of the separation between syntax and semantics in formal language theory. A SQL query is a string in the SQL formal language; parameter values are data, not language tokens. SQL injection exploits the inability to distinguish a string boundary (closing quote) that is part of the syntax from one that appears in data. PreparedStatement solves this by compiling the query structure once (parsing it as a formal language expression) and binding data values separately — the data values are in the domain of the semantic evaluation, not the syntactic parsing. This is equivalent to the formal distinction between a program (syntax) and its input (data) in the halting problem: Turing showed they are theoretically distinct; SQL injection exploits their conflation in practice.

**Sciences (Chemistry — Laboratory Safety)**: JDBC resource management (try-with-resources) mirrors laboratory decontamination protocols. A laboratory procedure that opens a biosafety cabinet, handles a pathogen, and exits must guarantee decontamination regardless of what happens during the procedure (spillage, error, equipment failure). Similarly, JDBC's try-with-resources guarantees that database connections (hazardous resources that consume server memory and connection slots) are returned to the pool regardless of any exception in the data processing code. The formal equivalence: AutoCloseable.close() = decontamination protocol; try-with-resources = mandatory-decontamination-on-exit requirement; ResourceLeak = laboratory contamination incident. Both disciplines treat resource acquisition as a contract that requires guaranteed release.

# Lore Conclusion

"The Archive JDBC layer is fixed," the Junior Engineer reported. "All queries use PreparedStatement. Resources are closed with try-with-resources. DataSource connection pool replaces DriverManager. The injection vulnerability is gone." The Senior Archivist reviewed the code. "And batch INSERT for log entries — 500 individual inserts → 1 batch. Clean, correct, safe." She set the code aside. "JDBC is the foundation layer. Most production Java applications don't write raw JDBC — they use an ORM that generates JDBC calls. But understanding JDBC means you understand what the ORM is doing, when it's doing it wrong, and how to fix it."

---
