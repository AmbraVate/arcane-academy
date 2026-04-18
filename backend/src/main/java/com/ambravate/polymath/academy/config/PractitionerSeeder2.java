package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Deprecated
@Profile("never")
@Component
public class PractitionerSeeder2 extends AbstractChunkSeeder {

    public PractitionerSeeder2(ChunkRepository chunkRepository, SubChunkRepository subChunkRepository,
                               QuestionRepository questionRepository, RabbitHoleModuleRepository rabbitHoleRepository) {
        super(chunkRepository, subChunkRepository, questionRepository, rabbitHoleRepository);
    }

    @Override
    public void seed() {
        seedChunkPH();
        seedChunkPI();
        seedChunkPJ();
        seedChunkPK();
        seedChunkPL();
        seedChunkPM();
        seedChunkPN();
    }

    // =========================================================================
    // CHUNK PH — Concurrency Utilities
    // =========================================================================
    private void seedChunkPH() {
        chunk("PH", "Concurrency Utilities", "\uD83D\uDD00", 22, LearnerPath.PRACTITIONER, "PG");

        subChunk("PH1", "PH", "ExecutorService & CompletableFuture", 1, 80, "ExecutorService.java",

                "<p>Managing raw threads — creating, starting, joining — is tedious and error-prone. The Executor framework is the professional way.</p>",

                "<h3>ExecutorService: Thread Pools</h3>"
                + "<p>A thread pool reuses a fixed set of threads rather than creating new ones for every task:</p>"
                + "<pre><code>import java.util.concurrent.*;\n\nExecutorService pool = Executors.newFixedThreadPool(4);\n\n// Submit Runnable (no result)\npool.execute(() -> System.out.println(\"Task 1\"));\n\n// Submit Callable (returns result)\nFuture&lt;Integer&gt; future = pool.submit(() -> {\n    Thread.sleep(100);\n    return 42;\n});\n\nSystem.out.println(future.get()); // blocks until result ready\npool.shutdown();\npool.awaitTermination(5, TimeUnit.SECONDS);</code></pre>"
                + "<h3>CompletableFuture: Non-blocking Composition</h3>"
                + "<pre><code>CompletableFuture&lt;String&gt; cf =\n    CompletableFuture.supplyAsync(() -> \"Hello\")\n        .thenApply(s -> s + \", Apprentice!\")\n        .thenApply(String::toUpperCase);\n\nSystem.out.println(cf.join()); // HELLO, APPRENTICE!</code></pre>"
                + "<p>Key <code>CompletableFuture</code> methods:</p>"
                + "<ul>"
                + "<li><code>supplyAsync(Supplier)</code> — start async computation</li>"
                + "<li><code>thenApply(Function)</code> — transform result</li>"
                + "<li><code>thenAccept(Consumer)</code> — consume result, return void</li>"
                + "<li><code>thenCombine(cf2, BiFunction)</code> — combine two futures</li>"
                + "<li><code>exceptionally(Function)</code> — handle errors</li>"
                + "<li><code>join()</code> / <code>get()</code> — wait for result</li>"
                + "</ul>",

                story(
                    n("Eldrin replaces the golem-summoning ritual with a golem pool — four golems wait in the antechamber. Tasks are queued; idle golems grab work immediately."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Creating a new thread for every tiny task is like hiring a new wizard every time you need a single spell cast. A thread pool keeps workers ready and reuses them. The <code>ExecutorService</code> manages the pool; you simply submit tasks."),
                    e("ExecutorService basics", "import java.util.concurrent.*;\nExecutorService pool = Executors.newFixedThreadPool(3);\nFuture<Integer> f = pool.submit(() -> 6 * 7);\nSystem.out.println(f.get()); // 42\npool.shutdown();"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "<code>CompletableFuture</code> takes this further — you chain asynchronous steps like a pipeline, each triggered when the previous completes. The main thread is never blocked. It is the foundation of reactive and async Java code.")
                ),

                "<p>Use <code>CompletableFuture.supplyAsync()</code> to supply the string <code>\"Hello\"</code>, "
                + "chain <code>thenApply</code> to append <code>\" Apprentice\"</code>, then print with <code>join()</code>.</p>",

                "import java.util.concurrent.*;\n\npublic class ExecutorService {\n    public static void main(String[] args) {\n        String result = CompletableFuture\n            .supplyAsync(() -> \"Hello\")\n            .thenApply(s -> s + \" Apprentice\")\n            .join();\n        System.out.println(result);\n    }\n}\n",

                tests(
                    test("CompletableFuture chain", "null", "Hello Apprentice")
                ),
                "What is the difference between ExecutorService and CompletableFuture? When would you use each?"
        );

        mcQuestion("PH1", QuestionTier.RECALL,
                "<p>What happens when you call <code>future.get()</code>?</p>",
                new String[]{"Blocks the calling thread until the result is available", "Returns null immediately", "Cancels the task", "Throws an exception immediately"},
                "Blocks the calling thread until the result is available",
                "<p><code>get()</code> is a blocking call — it waits until the async computation finishes and returns the result (or throws if it failed).</p>");

        codeQuestion("PH1", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "import java.util.concurrent.*;\nCompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> 10)\n    .thenApply(n -> n * 2)\n    .thenApply(n -> n + 5);\nSystem.out.println(cf.join());",
                "25",
                "<p>10 × 2 = 20, then 20 + 5 = 25.</p>");

        subChunk("PH2", "PH", "Concurrent Collections & Atomic Variables", 2, 70, "ConcurrentCollections.java",

                "<p>Standard collections are not thread-safe. Wrapping them in synchronized blocks works but is slow. Java provides purpose-built concurrent alternatives.</p>",

                "<h3>Concurrent Collections</h3>"
                + "<pre><code>import java.util.concurrent.*;\n\n// Thread-safe Map (better than synchronized HashMap)\nConcurrentHashMap&lt;String, Integer&gt; map = new ConcurrentHashMap&lt;&gt;();\nmap.put(\"a\", 1);\nmap.computeIfAbsent(\"b\", k -> 2);\nmap.merge(\"a\", 1, Integer::sum); // atomic increment\n\n// Thread-safe list/queue\nCopyOnWriteArrayList&lt;String&gt; list = new CopyOnWriteArrayList&lt;&gt;();\nBlockingQueue&lt;String&gt; queue = new LinkedBlockingQueue&lt;&gt;();\nqueue.put(\"task\");   // blocks if full\nqueue.take();        // blocks if empty</code></pre>"
                + "<h3>Atomic Variables</h3>"
                + "<pre><code>AtomicInteger counter = new AtomicInteger(0);\ncounter.incrementAndGet();        // atomic ++\ncounter.compareAndSet(1, 99);    // only updates if current == 1\nint prev = counter.getAndAdd(5); // return old, add 5\n\nAtomicReference&lt;String&gt; ref = new AtomicReference&lt;&gt;(\"initial\");\nref.compareAndSet(\"initial\", \"updated\"); // CAS operation</code></pre>",

                story(
                    n("Eldrin points to the academy's chaotic message board — apprentices simultaneously posting and reading, messages appearing corrupted."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Standard collections were designed for single-threaded use. Put multiple threads in them without coordination and you get data corruption, lost updates, and ConcurrentModificationException. The concurrent package has purpose-built thread-safe versions."),
                    e("ConcurrentHashMap", "import java.util.concurrent.*;\nConcurrentHashMap<String,Integer> scores = new ConcurrentHashMap<>();\nscores.put(\"Alice\", 100);\nscores.merge(\"Alice\", 10, Integer::sum); // 110 — atomic\nSystem.out.println(scores.get(\"Alice\")); // 110"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "<code>ConcurrentHashMap</code> achieves thread safety without locking the entire map — it uses segment-level locking. Reads are almost always non-blocking. For most concurrent map needs, it is the right choice.")
                ),

                "<p>Use <code>ConcurrentHashMap</code> to count word frequencies from a list of words processed by multiple threads. "
                + "Start 3 threads, each adding <code>\"spell\"</code> five times using <code>merge()</code>. "
                + "Print the final count for <code>\"spell\"</code>.</p>",

                "import java.util.concurrent.*;\n\npublic class ConcurrentCollections {\n    public static void main(String[] args) throws InterruptedException {\n        ConcurrentHashMap<String, Integer> freq = new ConcurrentHashMap<>();\n        Thread[] threads = new Thread[3];\n        for (int i = 0; i < 3; i++) {\n            threads[i] = new Thread(() -> {\n                for (int j = 0; j < 5; j++)\n                    freq.merge(\"spell\", 1, Integer::sum);\n            });\n            threads[i].start();\n        }\n        for (Thread t : threads) t.join();\n        System.out.println(freq.get(\"spell\"));\n    }\n}\n",

                tests(
                    test("Final count", "null", "15")
                ),
                "Why is ConcurrentHashMap preferable to a synchronized HashMap? When would you use a BlockingQueue?"
        );

        mcQuestion("PH2", QuestionTier.DISCRIMINATION,
                "<p>You need a thread-safe counter that many threads increment and you occasionally check the value. The best option is:</p>",
                new String[]{"AtomicInteger", "volatile int", "synchronized int", "ThreadLocal<Integer>"},
                "AtomicInteger",
                "<p><code>AtomicInteger</code> provides atomic increment without locks — faster than <code>synchronized</code> for a single counter. <code>volatile</code> only ensures visibility, not atomicity of <code>++</code>.</p>");
    }

    // =========================================================================
    // CHUNK PI — JDBC & Database Access
    // =========================================================================
    private void seedChunkPI() {
        chunk("PI", "JDBC & Database Access", "\uD83D\uDDC4\uFE0F", 23, LearnerPath.PRACTITIONER, "M");

        subChunk("PI1", "PI", "JDBC: Connecting & Querying", 1, 70, "JDBCBasics.java",

                "<p>Your application needs to store and retrieve data that survives beyond a single run. JDBC is Java's standard API for talking to any relational database.</p>",

                "<h3>The JDBC Pattern</h3>"
                + "<p>Four steps: get a Connection, create a Statement, execute, process ResultSet.</p>"
                + "<pre><code>import java.sql.*;\n\n// H2 in-memory database (excellent for testing)\nString url = \"jdbc:h2:mem:test;DB_CLOSE_DELAY=-1\";\ntry (Connection conn = DriverManager.getConnection(url, \"sa\", \"\")) {\n\n    // Create table\n    conn.createStatement().execute(\n        \"CREATE TABLE wizard (id INT PRIMARY KEY, name VARCHAR(50), level INT)\"\n    );\n\n    // Insert\n    PreparedStatement insert = conn.prepareStatement(\n        \"INSERT INTO wizard VALUES (?, ?, ?)\"\n    );\n    insert.setInt(1, 1);\n    insert.setString(2, \"Merlin\");\n    insert.setInt(3, 99);\n    insert.executeUpdate();\n\n    // Query\n    ResultSet rs = conn.createStatement().executeQuery(\"SELECT * FROM wizard\");\n    while (rs.next()) {\n        System.out.println(rs.getString(\"name\") + \" Lv.\" + rs.getInt(\"level\"));\n    }\n}</code></pre>"
                + "<p><strong>Always use PreparedStatement</strong> — never concatenate user input into SQL strings (SQL injection vulnerability).</p>",

                story(
                    n("Eldrin opens a glowing portal to the Academy's great stone Archive — every wizard's record inscribed on tablets stretching to infinity."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "JDBC is the universal translation layer between Java and any relational database. Oracle, PostgreSQL, MySQL, H2 — the JDBC API is the same; only the driver and connection URL change. The four steps never vary: connect, prepare, execute, process."),
                    e("JDBC in action", "// H2 in-memory database\nString url = \"jdbc:h2:mem:test;DB_CLOSE_DELAY=-1\";\ntry (Connection c = DriverManager.getConnection(url,\"sa\",\"\")) {\n    c.createStatement().execute(\"CREATE TABLE t(v VARCHAR(20))\");\n    c.createStatement().execute(\"INSERT INTO t VALUES('Hello JDBC')\");\n    ResultSet rs = c.createStatement().executeQuery(\"SELECT v FROM t\");\n    if (rs.next()) System.out.println(rs.getString(1));\n}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Never build SQL strings with user input via concatenation. 'SELECT * FROM users WHERE name = \\'' + userInput + '\\'' — if userInput is <em>' OR '1'='1</em>, you have just opened every record to the world. PreparedStatements seal that vulnerability.")
                ),

                "import java.sql.*;\n\npublic class JDBCBasics {\n    public static void main(String[] args) throws Exception {\n        // Note: H2 JDBC driver must be on classpath\n        // This demonstrates the JDBC pattern\n        System.out.println(\"JDBC pattern demonstrated\");\n        System.out.println(\"Step 1: DriverManager.getConnection(url)\");\n        System.out.println(\"Step 2: conn.prepareStatement(sql)\");\n        System.out.println(\"Step 3: stmt.executeUpdate() / executeQuery()\");\n        System.out.println(\"Step 4: rs.next() / rs.getString()\");\n    }\n}\n",

                "<p>This exercise explains the JDBC pattern. Run the code to see the four-step summary printed.</p>",

                tests(
                    test("Pattern step 1", "null", "Step 1: DriverManager.getConnection(url)"),
                    test("Pattern step 4", "null", "Step 4: rs.next() / rs.getString()")
                ),
                "Describe the four steps of the JDBC pattern. Why must you always use PreparedStatement instead of Statement for user-supplied data?"
        );

        mcQuestion("PI1", QuestionTier.RECALL,
                "<p>What is SQL injection and how does PreparedStatement prevent it?</p>",
                new String[]{"User input is treated as data, not executable SQL — parameters are escaped automatically", "PreparedStatement runs SQL faster", "PreparedStatement validates the database schema", "SQL injection only affects INSERT statements"},
                "User input is treated as data, not executable SQL — parameters are escaped automatically",
                "<p>PreparedStatement separates SQL structure from data. The <code>?</code> placeholders are bound as values, never interpreted as SQL syntax — malicious input cannot alter the query structure.</p>");

        subChunk("PI2", "PI", "Transactions & Connection Pooling", 2, 80, "Transactions.java",

                "<p>Transferring money between accounts requires both the debit and credit to succeed — or neither. That's a transaction.</p>",

                "<h3>JDBC Transactions</h3>"
                + "<pre><code>try (Connection conn = DriverManager.getConnection(url, user, pass)) {\n    conn.setAutoCommit(false); // begin transaction\n    try {\n        PreparedStatement debit = conn.prepareStatement(\n            \"UPDATE account SET balance = balance - ? WHERE id = ?\");\n        debit.setDouble(1, 100.0);\n        debit.setInt(2, fromId);\n        debit.executeUpdate();\n\n        PreparedStatement credit = conn.prepareStatement(\n            \"UPDATE account SET balance = balance + ? WHERE id = ?\");\n        credit.setDouble(1, 100.0);\n        credit.setInt(2, toId);\n        credit.executeUpdate();\n\n        conn.commit(); // all or nothing\n    } catch (SQLException e) {\n        conn.rollback(); // undo everything\n        throw e;\n    }\n}</code></pre>"
                + "<h3>Connection Pooling (HikariCP)</h3>"
                + "<p>Opening a database connection is expensive (~50-100ms). A connection pool maintains open connections and lends them to threads on demand:</p>"
                + "<pre><code>// HikariCP (fastest connection pool — used by Spring Boot)\nHikariConfig config = new HikariConfig();\nconfig.setJdbcUrl(\"jdbc:postgresql://localhost/mydb\");\nconfig.setMaximumPoolSize(10);\nHikariDataSource ds = new HikariDataSource(config);\n\ntry (Connection conn = ds.getConnection()) {\n    // use connection — returned to pool when closed\n}</code></pre>",

                story(
                    n("The Academy treasurer transfers 100 gold from the scholarship fund to the apprentice fund — but the spell fails halfway. Without a transaction, the gold vanishes."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A transaction is an all-or-nothing ward. Either every operation within it succeeds and commits, or an error triggers a rollback — every change undone, the data returned to its state before the transaction began. ACID: Atomic, Consistent, Isolated, Durable."),
                    e("Transaction pattern", "conn.setAutoCommit(false);\ntry {\n    // ... multiple SQL operations ...\n    conn.commit();\n    System.out.println(\"Transaction committed\");\n} catch (SQLException e) {\n    conn.rollback();\n    System.out.println(\"Rolled back\");\n}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "HikariCP, the connection pool bundled with Spring Boot, is the fastest available. Without pooling, every request to your web application would spend 50ms just opening a database connection before doing any useful work. Pooling drops that to near-zero.")
                ),

                "<p>Demonstrate the transaction pattern: set autoCommit to false, perform two 'operations' (println statements representing SQL), commit, and print 'Committed'. "
                + "Then show the rollback path by simulating a failure.</p>",

                "import java.sql.*;\n\npublic class Transactions {\n    static void transferDemo(boolean fail) {\n        System.out.println(\"BEGIN TRANSACTION\");\n        try {\n            System.out.println(\"  Debit account A\");\n            if (fail) throw new RuntimeException(\"Simulated failure\");\n            System.out.println(\"  Credit account B\");\n            System.out.println(\"COMMITTED\");\n        } catch (Exception e) {\n            System.out.println(\"ROLLED BACK: \" + e.getMessage());\n        }\n    }\n    public static void main(String[] args) {\n        transferDemo(false);\n        transferDemo(true);\n    }\n}\n",

                tests(
                    test("Successful commit", "null", "COMMITTED"),
                    test("Rollback on failure", "null", "ROLLED BACK: Simulated failure")
                ),
                "What are ACID properties of a database transaction? Why is connection pooling important for application performance?"
        );

        mcQuestion("PI2", QuestionTier.RECALL,
                "<p>What does <code>conn.setAutoCommit(false)</code> do?</p>",
                new String[]{"Begins a manual transaction — changes are not committed until you call commit()", "Disables all SQL operations", "Makes the connection read-only", "Enables auto-rollback on all operations"},
                "Begins a manual transaction — changes are not committed until you call commit()",
                "<p>By default, each SQL statement auto-commits. Setting <code>autoCommit(false)</code> groups subsequent statements into a single transaction that you explicitly commit or rollback.</p>");

        tfQuestion("PI2", QuestionTier.DISCRIMINATION,
                "<p>A connection pool creates a new database connection for every request to improve reliability.</p>",
                "False",
                "<p>A connection pool does the opposite — it maintains a set of pre-opened connections and reuses them. Creating new connections is expensive; pooling amortises that cost across many requests.</p>");
    }

    // =========================================================================
    // CHUNK PJ — Testing
    // =========================================================================
    private void seedChunkPJ() {
        chunk("PJ", "Testing with JUnit & Mockito", "\u2705", 24, LearnerPath.PRACTITIONER, "L");

        subChunk("PJ1", "PJ", "JUnit 5: Writing & Organising Tests", 1, 70, "JUnit5Basics.java",

                "<p>How do you know your code works? How do you know it <em>still</em> works after a change? Automated tests are the answer.</p>",

                "<h3>JUnit 5 Basics</h3>"
                + "<p>JUnit 5 is the standard Java testing framework. Tests are methods annotated with <code>@Test</code>:</p>"
                + "<pre><code>import org.junit.jupiter.api.*;\nimport static org.junit.jupiter.api.Assertions.*;\n\nclass CalculatorTest {\n\n    Calculator calc;\n\n    @BeforeEach\n    void setup() {\n        calc = new Calculator(); // fresh instance before each test\n    }\n\n    @Test\n    @DisplayName(\"Adding two positive numbers\")\n    void testAdd() {\n        assertEquals(5, calc.add(2, 3));\n    }\n\n    @Test\n    void testDivideByZeroThrows() {\n        assertThrows(ArithmeticException.class,\n            () -> calc.divide(10, 0));\n    }\n\n    @Test\n    void testMultipleAssertions() {\n        assertAll(\n            () -> assertTrue(calc.add(1,1) == 2),\n            () -> assertFalse(calc.add(1,1) == 3),\n            () -> assertEquals(\"2\", String.valueOf(calc.add(1,1)))\n        );\n    }\n}</code></pre>"
                + "<p>Key assertions: <code>assertEquals</code>, <code>assertTrue</code>, <code>assertFalse</code>, <code>assertNull</code>, <code>assertThrows</code>, <code>assertAll</code>.</p>"
                + "<p>Lifecycle: <code>@BeforeAll</code> (once), <code>@BeforeEach</code> (before each test), <code>@AfterEach</code>, <code>@AfterAll</code>.</p>",

                story(
                    n("Eldrin ceremonially adds a new spell to the Academy Grimoire. But first, he runs the verification ritual — every test case must pass or the spell is rejected."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A test is a spell contract. It declares: given this input, I expect this output. Run the tests; if they pass, the contract holds. Change the code; run the tests again. Any broken contract is found immediately, not three months later in production."),
                    e("Test pattern", "// Production class\nclass Calculator {\n    int add(int a, int b) { return a + b; }\n    int divide(int a, int b) { return a / b; } // throws if b==0\n}\n\n// Test class (requires JUnit 5 on classpath)\n// @Test void testAdd() { assertEquals(5, new Calculator().add(2,3)); }"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Good test names read like specifications: <em>adding two positive numbers returns their sum</em>. <code>@DisplayName</code> lets you write natural language test names. The test suite becomes living documentation of your code's behaviour.")
                ),

                "<p>Implement a <code>StringUtils</code> class with method <code>isPalindrome(String s)</code> that returns true if the string reads the same forwards and backwards. "
                + "Demonstrate the logic with assertions (manual, not JUnit).</p>",

                "public class JUnit5Basics {\n    static class StringUtils {\n        static boolean isPalindrome(String s) {\n            String reversed = new StringBuilder(s).reverse().toString();\n            return s.equals(reversed);\n        }\n    }\n    public static void main(String[] args) {\n        // Demonstrate — in real code these would be JUnit @Test methods\n        System.out.println(StringUtils.isPalindrome(\"racecar\")); // true\n        System.out.println(StringUtils.isPalindrome(\"hello\"));   // false\n        System.out.println(StringUtils.isPalindrome(\"level\"));   // true\n    }\n}\n",

                tests(
                    test("Palindrome true", "null", "true"),
                    test("Palindrome false", "null", "false"),
                    test("Palindrome level", "null", "true")
                ),
                "Explain the role of @BeforeEach in JUnit 5. Why is it important to create a fresh instance before each test?"
        );

        mcQuestion("PJ1", QuestionTier.RECALL,
                "<p>What does <code>assertThrows(Exception.class, () -> ...)</code> verify?</p>",
                new String[]{"That the lambda throws the specified exception type", "That the lambda does not throw any exception", "That the lambda runs in under 1 second", "That the exception message equals \"Exception\""},
                "That the lambda throws the specified exception type",
                "<p><code>assertThrows</code> passes if and only if the lambda throws the specified exception type. It fails if no exception is thrown or a different exception type is thrown.</p>");

        subChunk("PJ2", "PJ", "Mockito & TDD", 2, 80, "MockitoAndTDD.java",

                "<p>Your service depends on a database — but you don't want a real database in your unit tests. Mockito creates fake objects that behave as you instruct.</p>",

                "<h3>Mockito: Mocking Dependencies</h3>"
                + "<pre><code>import static org.mockito.Mockito.*;\n\n// Create a mock\nUserRepository mockRepo = mock(UserRepository.class);\n\n// Stub behaviour\nwhen(mockRepo.findById(1)).thenReturn(new User(1, \"Eldrin\"));\nwhen(mockRepo.findById(99)).thenReturn(null);\n\n// Inject mock into service\nUserService service = new UserService(mockRepo);\nString name = service.getUserName(1);\nassertEquals(\"Eldrin\", name);\n\n// Verify interaction\nverify(mockRepo, times(1)).findById(1);\nverify(mockRepo, never()).save(any());</code></pre>"
                + "<h3>Test-Driven Development (TDD)</h3>"
                + "<p>The red-green-refactor cycle:</p>"
                + "<ol>"
                + "<li><span style='color:red'><strong>Red</strong></span> — write a failing test that specifies desired behaviour</li>"
                + "<li><span style='color:green'><strong>Green</strong></span> — write the minimum code to make it pass</li>"
                + "<li><strong>Refactor</strong> — clean up the code while keeping tests green</li>"
                + "</ol>"
                + "<p>TDD ensures your code is always testable, your tests always relevant, and your design emerges naturally from requirements.</p>",

                story(
                    n("Eldrin needs to test the Academy's enrollment service — but the actual database is across the country. He conjures a mock registry that behaves exactly as instructed."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A mock is a puppet — it responds exactly as you script it, without having any actual behaviour of its own. You test how your code <em>interacts</em> with the mock, not how the real dependency works. Your unit tests become fast, isolated, and reliable."),
                    e("Mock pattern", "// Manual mock-like pattern (without Mockito library)\ninterface SpellRepo { Spell find(String name); }\nclass MockSpellRepo implements SpellRepo {\n    public Spell find(String name) {\n        return new Spell(name, 80); // always returns a spell\n    }\n}\n// Inject mock into service under test\nSpellService svc = new SpellService(new MockSpellRepo());"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "TDD inverts the usual sequence. You write the <em>test</em> first — describing what the code should do. The test fails immediately (red). Then you write the code to make it pass (green). Then you clean up (refactor). The tests guard against regressions forever.")
                ),

                "<p>Implement a manual TDD cycle: write an interface <code>SpellRepo</code> with <code>findSpell(String name)</code>, "
                + "a mock implementation, a <code>SpellService</code> that uses it, "
                + "and demonstrate it returns the correct spell name.</p>",

                "public class MockitoAndTDD {\n    interface SpellRepo {\n        String findSpell(String name);\n    }\n    static class MockSpellRepo implements SpellRepo {\n        public String findSpell(String name) {\n            return name.equals(\"Fireball\") ? \"Fireball (80 dmg)\" : \"Unknown spell\";\n        }\n    }\n    static class SpellService {\n        private SpellRepo repo;\n        SpellService(SpellRepo repo) { this.repo = repo; }\n        String getSpellInfo(String name) { return repo.findSpell(name); }\n    }\n    public static void main(String[] args) {\n        SpellService svc = new SpellService(new MockSpellRepo());\n        System.out.println(svc.getSpellInfo(\"Fireball\"));\n        System.out.println(svc.getSpellInfo(\"IceBolt\"));\n    }\n}\n",

                tests(
                    test("Known spell", "null", "Fireball (80 dmg)"),
                    test("Unknown spell", "null", "Unknown spell")
                ),
                "What is Mockito used for in testing? Explain the red-green-refactor TDD cycle."
        );

        mcQuestion("PJ2", QuestionTier.RECALL,
                "<p>In TDD, what is the correct order of the cycle?</p>",
                new String[]{"Write failing test → make it pass → refactor", "Write code → write test → refactor", "Refactor → write test → run", "Write test → refactor → make it pass"},
                "Write failing test → make it pass → refactor",
                "<p>Red (failing test) → Green (minimal code to pass) → Refactor (clean up). The failing test first ensures you have a real requirement and that your test actually tests something.</p>");

        subChunk("PJ3", "PJ", "BDD & Integration Testing", 3, 80, "BDDTesting.java",

                "<p>Unit tests verify individual classes. But does the whole system work together? And can non-technical stakeholders understand the tests?</p>",

                "<h3>BDD: Given-When-Then</h3>"
                + "<p>BDD (Behaviour-Driven Development) structures tests in a Given-When-Then narrative that anyone can read:</p>"
                + "<pre><code>// JUnit 5 with BDD naming\n@Nested\n@DisplayName(\"When a wizard casts a spell\")\nclass WizardSpellTests {\n\n    @Test\n    @DisplayName(\"Given sufficient mana, the spell is cast successfully\")\n    void castWithSufficientMana() {\n        // Given\n        Wizard wizard = new Wizard(\"Merlin\", 100);\n        Spell spell = new Spell(\"Fireball\", 40);\n\n        // When\n        boolean result = wizard.cast(spell);\n\n        // Then\n        assertTrue(result);\n        assertEquals(60, wizard.getMana());\n    }\n}</code></pre>"
                + "<h3>Integration vs Unit Testing</h3>"
                + "<ul>"
                + "<li><strong>Unit test</strong>: tests a single class in isolation; mocks all dependencies; fast</li>"
                + "<li><strong>Integration test</strong>: tests multiple layers together (e.g., service + real database); slower, catches wiring bugs</li>"
                + "<li><strong>E2E test</strong>: tests the entire system from UI to database</li>"
                + "</ul>"
                + "<p>The test pyramid: many unit tests, fewer integration tests, very few E2E tests.</p>",

                story(
                    n("Eldrin presents the test pyramid etched on the academy wall — a wide base of unit tests, a narrower middle of integration tests, a tiny tip of end-to-end tests."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "BDD bridges the gap between the business and the developer. When a test reads like 'Given an enrolled apprentice, When they complete a chapter, Then their XP increases by 100' — even the Academy Headmaster can verify the system behaves correctly."),
                    e("BDD structure", "// Given\nWizard w = new Wizard(100); // 100 mana\n// When\nboolean cast = w.castSpell(40); // costs 40 mana\n// Then\nSystem.out.println(cast);          // true\nSystem.out.println(w.getMana());   // 60"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The test pyramid is economics. Unit tests are cheap — fast to write, fast to run, easy to debug. Integration tests are expensive. End-to-end tests are very expensive. Maximise unit tests; use integration tests for critical paths; use E2E tests sparingly.")
                ),

                "<p>Implement a <code>Wizard</code> class with mana management. "
                + "Using Given-When-Then style (comments), test: casting a spell that costs 40 mana from 100 (succeeds, mana = 60) "
                + "and casting when mana is insufficient (fails, mana unchanged).</p>",

                "public class BDDTesting {\n    static class Wizard {\n        private int mana;\n        Wizard(int mana) { this.mana = mana; }\n        int getMana() { return mana; }\n        boolean castSpell(int cost) {\n            if (cost > mana) return false;\n            mana -= cost;\n            return true;\n        }\n    }\n    public static void main(String[] args) {\n        // Given: wizard with 100 mana, When: cast 40-cost spell, Then: success + 60 mana\n        Wizard w1 = new Wizard(100);\n        boolean r1 = w1.castSpell(40);\n        System.out.println(r1);         // true\n        System.out.println(w1.getMana()); // 60\n\n        // Given: wizard with 20 mana, When: cast 40-cost spell, Then: fail + mana unchanged\n        Wizard w2 = new Wizard(20);\n        boolean r2 = w2.castSpell(40);\n        System.out.println(r2);         // false\n        System.out.println(w2.getMana()); // 20\n    }\n}\n",

                tests(
                    test("Cast success", "null", "true"),
                    test("Mana after success", "null", "60"),
                    test("Cast failure", "null", "false"),
                    test("Mana after failure", "null", "20")
                ),
                "Explain the test pyramid. How do unit, integration, and end-to-end tests differ, and why should unit tests outnumber the others?"
        );

        mcQuestion("PJ3", QuestionTier.RECALL,
                "<p>What is the 'Given-When-Then' structure in BDD tests?</p>",
                new String[]{"Setup preconditions / trigger action / verify outcome", "Input / Process / Output", "Create / Read / Update / Delete", "Before / During / After"},
                "Setup preconditions / trigger action / verify outcome",
                "<p>Given = arrange the test preconditions; When = act (trigger the behaviour under test); Then = assert (verify the outcome). This structure produces readable, requirement-like tests.</p>");
    }

    // =========================================================================
    // CHUNK PK — Design Patterns: Creational
    // =========================================================================
    private void seedChunkPK() {
        chunk("PK", "Design Patterns: Creational", "\uD83C\uDFD7\uFE0F", 25, LearnerPath.PRACTITIONER, "L");

        subChunk("PK1", "PK", "Singleton & Factory", 1, 70, "CreationalPatterns1.java",

                "<p>Design patterns are battle-tested solutions to recurring design problems — not invented from scratch each time, but recognised and applied.</p>",

                "<h3>Singleton Pattern</h3>"
                + "<p>Guarantees exactly one instance of a class exists. The safest implementation uses an enum:</p>"
                + "<pre><code>// Enum singleton — thread-safe, serialisation-safe\npublic enum Registry {\n    INSTANCE;\n    private final Map&lt;String,String&gt; data = new HashMap&lt;&gt;();\n    public void register(String k, String v) { data.put(k,v); }\n    public String lookup(String k) { return data.get(k); }\n}\n// Usage\nRegistry.INSTANCE.register(\"Merlin\", \"Archmage\");\nSystem.out.println(Registry.INSTANCE.lookup(\"Merlin\")); // Archmage</code></pre>"
                + "<h3>Factory Method Pattern</h3>"
                + "<p>Defines an interface for creating objects — subclasses decide which concrete class to instantiate:</p>"
                + "<pre><code>interface Spell { void cast(); }\nclass FireSpell implements Spell { public void cast() { System.out.println(\"FIRE!\"); } }\nclass IceSpell  implements Spell { public void cast() { System.out.println(\"ICE!\");  } }\n\nclass SpellFactory {\n    static Spell create(String type) {\n        return switch (type) {\n            case \"fire\" -> new FireSpell();\n            case \"ice\"  -> new IceSpell();\n            default     -> throw new IllegalArgumentException(\"Unknown: \" + type);\n        };\n    }\n}\nSpellFactory.create(\"fire\").cast(); // FIRE!</code></pre>",

                story(
                    n("Eldrin explains the Academy Registry — only one exists, consulted by every department. To have two would cause conflicting records and chaos."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Singleton is the simplest pattern — and the most abused. Use it only when you genuinely need exactly one instance: configuration, connection pools, logging systems. Used carelessly, it becomes a global variable and a testing nightmare."),
                    e("Factory pattern", "interface Spell { void cast(); }\nclass FireSpell implements Spell { public void cast() { System.out.println(\"FIRE!\"); } }\nclass IceSpell  implements Spell { public void cast() { System.out.println(\"ICE!\"); } }\nclass SpellFactory {\n    static Spell create(String type) {\n        return type.equals(\"fire\") ? new FireSpell() : new IceSpell();\n    }\n}\nSpellFactory.create(\"fire\").cast();\nSpellFactory.create(\"ice\").cast();"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Factory decouples creation from use. The caller says 'give me a Spell' and doesn't care whether it's fire or ice — the factory decides. This makes adding new spell types trivial: add a new case to the factory, no changes elsewhere.")
                ),

                "<p>Implement a <code>SpellFactory</code> that creates spells by type. "
                + "Support <code>\"fire\"</code> (prints <code>\"Fireball cast!\"</code>) and <code>\"ice\"</code> (prints <code>\"Ice Lance cast!\"</code>). "
                + "Create one of each and call <code>cast()</code>.</p>",

                "public class CreationalPatterns1 {\n    interface Spell { void cast(); }\n    static class FireSpell implements Spell {\n        public void cast() { System.out.println(\"Fireball cast!\"); }\n    }\n    static class IceSpell implements Spell {\n        public void cast() { System.out.println(\"Ice Lance cast!\"); }\n    }\n    static class SpellFactory {\n        static Spell create(String type) {\n            return switch (type) {\n                case \"fire\" -> new FireSpell();\n                case \"ice\"  -> new IceSpell();\n                default -> throw new IllegalArgumentException(type);\n            };\n        }\n    }\n    public static void main(String[] args) {\n        SpellFactory.create(\"fire\").cast();\n        SpellFactory.create(\"ice\").cast();\n    }\n}\n",

                tests(
                    test("Fire spell", "null", "Fireball cast!"),
                    test("Ice spell", "null", "Ice Lance cast!")
                ),
                "What problem does the Factory Method pattern solve? How does it enable the Open/Closed principle?"
        );

        mcQuestion("PK1", QuestionTier.RECALL,
                "<p>What is the safest way to implement a Singleton in Java?</p>",
                new String[]{"As an enum with INSTANCE constant", "With a private constructor and static field", "With a synchronized getInstance() method", "With a static nested class"},
                "As an enum with INSTANCE constant",
                "<p>Enum singletons are inherently thread-safe, serialisation-safe, and immune to reflection-based attacks. They are the preferred Singleton implementation in modern Java (Josh Bloch, Effective Java).</p>");

        subChunk("PK2", "PK", "Builder & Prototype Patterns", 2, 70, "CreationalPatterns2.java",

                "<p>A constructor with 8 parameters — all optional — is a maintenance disaster. The Builder pattern solves this elegantly.</p>",

                "<h3>Builder Pattern</h3>"
                + "<pre><code>public class Potion {\n    private final String name;\n    private final int strength;\n    private final String colour;\n    private final boolean enchanted;\n\n    private Potion(Builder b) {\n        this.name = b.name; this.strength = b.strength;\n        this.colour = b.colour; this.enchanted = b.enchanted;\n    }\n\n    public static class Builder {\n        private String name; private int strength = 10;\n        private String colour = \"clear\"; private boolean enchanted = false;\n\n        public Builder name(String n) { this.name = n; return this; }\n        public Builder strength(int s) { this.strength = s; return this; }\n        public Builder colour(String c) { this.colour = c; return this; }\n        public Builder enchanted(boolean e) { this.enchanted = e; return this; }\n        public Potion build() { return new Potion(this); }\n    }\n}\n\nPotion p = new Potion.Builder()\n    .name(\"Health Elixir\")\n    .strength(80)\n    .colour(\"red\")\n    .enchanted(true)\n    .build();</code></pre>"
                + "<h3>Prototype Pattern</h3>"
                + "<p>Clone an existing object instead of building from scratch — useful when construction is expensive:</p>"
                + "<pre><code>class SpellTemplate implements Cloneable {\n    String name; int power;\n    @Override public SpellTemplate clone() {\n        try { return (SpellTemplate) super.clone(); }\n        catch (CloneNotSupportedException e) { throw new RuntimeException(e); }\n    }\n}</code></pre>",

                story(
                    n("Eldrin assembles a complex potion — but the constructor has 8 arguments and he keeps forgetting which is which. He redesigns it as a builder."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The Builder pattern replaces a telescope of constructors with a fluent, self-documenting API. Each method sets one field and returns the builder — enabling method chaining. The final <code>build()</code> constructs the immutable result."),
                    e("Builder in action", "Potion p = new Potion.Builder()\n    .name(\"Mana Potion\")\n    .strength(50)\n    .colour(\"blue\")\n    .build();\nSystem.out.println(p.getName() + \": \" + p.getStrength());"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Prototype is builder's cousin — instead of constructing from scratch, you clone an existing object. Expensive initialisation (loading configs, establishing connections) happens once; clones copy the result. The key risk: deep vs shallow copy — ensure mutable fields are properly cloned.")
                ),

                "<p>Implement a <code>Potion.Builder</code> with fields <code>name</code> (String) and <code>power</code> (int). "
                + "Build a potion with name <code>\"Health Elixir\"</code> and power <code>75</code> and print both fields.</p>",

                "public class CreationalPatterns2 {\n    static class Potion {\n        final String name; final int power;\n        private Potion(Builder b) { name = b.name; power = b.power; }\n        static class Builder {\n            String name; int power;\n            Builder name(String n) { this.name = n; return this; }\n            Builder power(int p) { this.power = p; return this; }\n            Potion build() { return new Potion(this); }\n        }\n    }\n    public static void main(String[] args) {\n        Potion p = new Potion.Builder().name(\"Health Elixir\").power(75).build();\n        System.out.println(p.name);\n        System.out.println(p.power);\n    }\n}\n",

                tests(
                    test("Potion name", "null", "Health Elixir"),
                    test("Potion power", "null", "75")
                ),
                "What problem does the Builder pattern solve? How does method chaining make it more readable than a constructor with many parameters?"
        );

        codeQuestion("PK2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "class B {\n    String x = \"\";\n    B a(String s) { x += s; return this; }\n    B b(String s) { x += s; return this; }\n}\nSystem.out.println(new B().a(\"Hello\").b(\" World\").x);",
                "Hello World",
                "<p>Each method appends to <code>x</code> and returns <code>this</code> (the builder), enabling chaining. Final value of <code>x</code> is \"Hello World\".</p>");
    }

    // =========================================================================
    // CHUNK PL — Design Patterns: Structural
    // =========================================================================
    private void seedChunkPL() {
        chunk("PL", "Design Patterns: Structural", "\uD83D\uDD27", 26, LearnerPath.PRACTITIONER, "PK");

        subChunk("PL1", "PL", "Adapter & Decorator", 1, 70, "StructuralPatterns1.java",

                "<p>You have a legacy class that works, but its interface doesn't match what new code expects. Adapter bridges the gap.</p>",

                "<h3>Adapter Pattern</h3>"
                + "<pre><code>// Old interface you can't change\nclass LegacySpellBook {\n    void invokeAncientRitual(String incantation) {\n        System.out.println(\"Ancient: \" + incantation);\n    }\n}\n\n// New interface that clients expect\ninterface ModernSpellCaster {\n    void cast(String spell);\n}\n\n// Adapter bridges the two\nclass SpellBookAdapter implements ModernSpellCaster {\n    private LegacySpellBook legacy;\n    SpellBookAdapter(LegacySpellBook b) { this.legacy = b; }\n    @Override public void cast(String spell) {\n        legacy.invokeAncientRitual(spell); // delegate\n    }\n}\nModernSpellCaster caster = new SpellBookAdapter(new LegacySpellBook());\ncaster.cast(\"Fireball\"); // Ancient: Fireball</code></pre>"
                + "<h3>Decorator Pattern</h3>"
                + "<pre><code>interface Coffee { String description(); double cost(); }\nclass SimpleCoffee implements Coffee {\n    public String description() { return \"Coffee\"; }\n    public double cost() { return 1.0; }\n}\nclass MilkDecorator implements Coffee {\n    private Coffee coffee;\n    MilkDecorator(Coffee c) { this.coffee = c; }\n    public String description() { return coffee.description() + \", Milk\"; }\n    public double cost() { return coffee.cost() + 0.5; }\n}\nCoffee c = new MilkDecorator(new SimpleCoffee());\nSystem.out.println(c.description()); // Coffee, Milk\nSystem.out.println(c.cost());        // 1.5</code></pre>",

                story(
                    n("Eldrin adapts an ancient dusty spell book to work with the modern Academy's casting interface — without rewriting the book."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The Adapter pattern is the universal translator. When the interface of an existing class doesn't match what you need, you wrap it in an Adapter that speaks both languages. The old code is untouched; the new code sees only the interface it expects."),
                    e("Decorator chain", "Coffee c = new SugarDecorator(new MilkDecorator(new SimpleCoffee()));\nSystem.out.println(c.description()); // Coffee, Milk, Sugar\nSystem.out.println(c.cost());        // 1.8"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Decorator adds behaviour without subclassing. Stack as many decorators as you need — each wraps the previous. Java I/O streams are a classic example: <code>new BufferedReader(new FileReader(file))</code> — each layer adds capability.")
                ),

                "<p>Implement an Adapter: <code>OldPrinter</code> has <code>printOld(String)</code>. "
                + "New code expects <code>Printer</code> interface with <code>print(String)</code>. "
                + "Create <code>PrinterAdapter</code> and verify it works.</p>",

                "public class StructuralPatterns1 {\n    interface Printer { void print(String msg); }\n    static class OldPrinter { void printOld(String msg) { System.out.println(\"[OLD] \" + msg); } }\n    static class PrinterAdapter implements Printer {\n        private OldPrinter old;\n        PrinterAdapter(OldPrinter o) { this.old = o; }\n        public void print(String msg) { old.printOld(msg); }\n    }\n    public static void main(String[] args) {\n        Printer p = new PrinterAdapter(new OldPrinter());\n        p.print(\"Hello Adapter\");\n    }\n}\n",

                tests(
                    test("Adapter delegates", "null", "[OLD] Hello Adapter")
                ),
                "Explain the Adapter pattern. Give a real-world example of when you would use it."
        );

        mcQuestion("PL1", QuestionTier.RECALL,
                "<p>What problem does the Decorator pattern solve that inheritance cannot?</p>",
                new String[]{"Adding behaviour dynamically at runtime without subclassing", "Making classes immutable", "Hiding the implementation from the caller", "Controlling object creation"},
                "Adding behaviour dynamically at runtime without subclassing",
                "<p>Decorator wraps objects at runtime — you can combine multiple decorators in any order. Inheritance is static (compile-time), and deep inheritance hierarchies become rigid and brittle.</p>");

        subChunk("PL2", "PL", "Facade & Proxy", 2, 70, "StructuralPatterns2.java",

                "<p>A complex subsystem of 7 interacting classes is hard for clients to use correctly. Facade provides a simple interface to that complexity.</p>",

                "<h3>Facade Pattern</h3>"
                + "<pre><code>// Complex subsystem\nclass ManaSystem   { void check()   { System.out.println(\"Mana OK\"); } }\nclass ArcaneChannel { void open()   { System.out.println(\"Channel open\"); } }\nclass SpellWeaver  { void weave()   { System.out.println(\"Spell woven\"); } }\n\n// Facade: simple interface to the subsystem\nclass SpellcastingFacade {\n    private ManaSystem mana = new ManaSystem();\n    private ArcaneChannel channel = new ArcaneChannel();\n    private SpellWeaver weaver = new SpellWeaver();\n\n    void castSpell() {\n        mana.check();\n        channel.open();\n        weaver.weave();\n        System.out.println(\"Spell cast!\");\n    }\n}\nnew SpellcastingFacade().castSpell(); // one call hides the complexity</code></pre>"
                + "<h3>Proxy Pattern</h3>"
                + "<pre><code>interface DataService { String fetch(String query); }\n\n// Caching proxy\nclass CachingProxy implements DataService {\n    private DataService real;\n    private Map&lt;String,String&gt; cache = new HashMap&lt;&gt;();\n    CachingProxy(DataService real) { this.real = real; }\n    public String fetch(String q) {\n        return cache.computeIfAbsent(q, real::fetch);\n    }\n}</code></pre>",

                story(
                    n("Eldrin creates a single 'Start Academy Day' lever. Behind it: locking the vault, raising the bridge, lighting the torches, summoning the staff — seven subsystems. Clients pull one lever."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The Facade is a simple door into a complex room. Clients interact only with the facade; the complexity of the subsystem is hidden. The subsystem classes still exist and can be used directly when needed — the facade just provides a convenient entry point."),
                    e("Proxy for caching", "interface Service { String get(String k); }\nclass SlowService implements Service {\n    public String get(String k) { return \"result:\" + k; }\n}\nclass CacheProxy implements Service {\n    private Service real; private Map<String,String> cache = new java.util.HashMap<>();\n    CacheProxy(Service s) { real = s; }\n    public String get(String k) { return cache.computeIfAbsent(k, real::get); }\n}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Proxy is a gatekeeper. It stands in front of a real object, intercepting calls to add behaviour: caching, logging, access control, lazy loading. Spring AOP creates proxies automatically for <code>@Transactional</code> and <code>@Cacheable</code> methods.")
                ),

                "<p>Implement a <code>SpellcastingFacade</code> with a <code>castSpell()</code> method that calls "
                + "<code>ManaSystem.check()</code>, <code>ArcaneChannel.open()</code>, and prints <code>\"Spell cast!\"</code>.</p>",

                "public class StructuralPatterns2 {\n    static class ManaSystem { void check() { System.out.println(\"Mana OK\"); } }\n    static class ArcaneChannel { void open() { System.out.println(\"Channel open\"); } }\n    static class SpellcastingFacade {\n        private ManaSystem mana = new ManaSystem();\n        private ArcaneChannel channel = new ArcaneChannel();\n        void castSpell() {\n            mana.check();\n            channel.open();\n            System.out.println(\"Spell cast!\");\n        }\n    }\n    public static void main(String[] args) {\n        new SpellcastingFacade().castSpell();\n    }\n}\n",

                tests(
                    test("Mana checked", "null", "Mana OK"),
                    test("Channel opened", "null", "Channel open"),
                    test("Spell cast", "null", "Spell cast!")
                ),
                "What is the difference between Facade and Adapter patterns? When would you use a Proxy pattern?"
        );

        mcQuestion("PL2", QuestionTier.DISCRIMINATION,
                "<p>Spring's @Transactional annotation works by wrapping your bean in a ________?</p>",
                new String[]{"Proxy", "Adapter", "Decorator", "Facade"},
                "Proxy",
                "<p>Spring creates a proxy around <code>@Transactional</code> beans. The proxy intercepts method calls, starts/commits/rolls back transactions, then delegates to the real bean. This is the Proxy pattern in action.</p>");
    }

    // =========================================================================
    // CHUNK PM — Design Patterns: Behavioural
    // =========================================================================
    private void seedChunkPM() {
        chunk("PM", "Design Patterns: Behavioural", "\uD83C\uDFAD", 27, LearnerPath.PRACTITIONER, "PL");

        subChunk("PM1", "PM", "Strategy & Observer", 1, 80, "BehaviouralPatterns1.java",

                "<p>Your sort algorithm is hardcoded — changing it requires modifying the class. What if you could swap algorithms at runtime?</p>",

                "<h3>Strategy Pattern</h3>"
                + "<pre><code>interface SortStrategy {\n    void sort(int[] arr);\n}\nclass BubbleSort implements SortStrategy {\n    public void sort(int[] arr) { /* bubble sort */ System.out.println(\"Bubble sorted\"); }\n}\nclass QuickSort implements SortStrategy {\n    public void sort(int[] arr) { /* quick sort */ System.out.println(\"Quick sorted\"); }\n}\nclass Sorter {\n    private SortStrategy strategy;\n    Sorter(SortStrategy s) { this.strategy = s; }\n    void setStrategy(SortStrategy s) { this.strategy = s; }\n    void sort(int[] arr) { strategy.sort(arr); }\n}\n// Switch strategy at runtime\nSorter sorter = new Sorter(new BubbleSort());\nsorter.sort(data);              // Bubble sorted\nsorter.setStrategy(new QuickSort());\nsorter.sort(data);              // Quick sorted</code></pre>"
                + "<h3>Observer Pattern</h3>"
                + "<pre><code>interface Observer { void update(String event); }\nclass EventSource {\n    private List&lt;Observer&gt; listeners = new ArrayList&lt;&gt;();\n    void subscribe(Observer o) { listeners.add(o); }\n    void emit(String event) { listeners.forEach(o -> o.update(event)); }\n}\nEventSource src = new EventSource();\nsrc.subscribe(e -> System.out.println(\"Log: \" + e));\nsrc.subscribe(e -> System.out.println(\"Alert: \" + e));\nsrc.emit(\"SPELL_FAILED\");</code></pre>",

                story(
                    n("Eldrin's combat system used to hardcode the attack algorithm. To change tactics in battle required stopping, rewriting, and redeploying. Strategy patterns changed everything."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Strategy encapsulates an algorithm and makes it interchangeable. The context class holds a reference to the strategy interface — not a concrete implementation. Swap strategies freely at runtime, test each in isolation, add new ones without touching the context."),
                    e("Observer example", "interface Observer { void update(String evt); }\nList<Observer> obs = new ArrayList<>();\nobs.add(e -> System.out.println(\"Logger: \" + e));\nobs.add(e -> System.out.println(\"Alerter: \" + e));\n// Emit event — all observers notified\nobs.forEach(o -> o.update(\"BREACH\"));"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Observer is the publish-subscribe model — a source emits events; all registered listeners receive them. The source knows nothing about its listeners; listeners know nothing about each other. This loose coupling is the core of event-driven architectures.")
                ),

                "<p>Implement Strategy: create a <code>Greeter</code> interface with <code>greet(String name)</code>. "
                + "Implement <code>FormalGreeter</code> (prints <code>\"Good day, [name]\"</code>) and <code>CasualGreeter</code> (prints <code>\"Hey, [name]!\"</code>). "
                + "Switch strategies and call both.</p>",

                "public class BehaviouralPatterns1 {\n    interface Greeter { void greet(String name); }\n    public static void main(String[] args) {\n        Greeter formal = name -> System.out.println(\"Good day, \" + name);\n        Greeter casual = name -> System.out.println(\"Hey, \" + name + \"!\");\n\n        formal.greet(\"Eldrin\");\n        casual.greet(\"Eldrin\");\n    }\n}\n",

                tests(
                    test("Formal greeting", "null", "Good day, Eldrin"),
                    test("Casual greeting", "null", "Hey, Eldrin!")
                ),
                "Explain the Strategy pattern. How does it relate to the Open/Closed principle?"
        );

        mcQuestion("PM1", QuestionTier.RECALL,
                "<p>In the Observer pattern, what is the relationship between the subject and its observers?</p>",
                new String[]{"Subject has a list of observers and notifies all when state changes", "Observers control the subject's behaviour", "Subject extends all observer classes", "There is only one observer per subject"},
                "Subject has a list of observers and notifies all when state changes",
                "<p>The subject (event source) maintains a list of observers and broadcasts state changes to all of them. This creates one-to-many loose coupling — the subject doesn't know the observers' concrete types.</p>");

        subChunk("PM2", "PM", "Command & Template Method", 2, 80, "BehaviouralPatterns2.java",

                "<p>If every action in your application could be an object — it could be queued, logged, undone. That's the Command pattern.</p>",

                "<h3>Command Pattern</h3>"
                + "<pre><code>interface Command { void execute(); void undo(); }\n\nclass Light {\n    boolean on = false;\n    void turnOn()  { on=true;  System.out.println(\"Light on\");  }\n    void turnOff() { on=false; System.out.println(\"Light off\"); }\n}\n\nclass TurnOnCommand implements Command {\n    private Light light;\n    TurnOnCommand(Light l) { this.light = l; }\n    public void execute() { light.turnOn(); }\n    public void undo()    { light.turnOff(); }\n}\n\nDeque&lt;Command&gt; history = new ArrayDeque&lt;&gt;();\nCommand cmd = new TurnOnCommand(new Light());\ncmd.execute();\nhistory.push(cmd);\nhistory.pop().undo(); // undo the last command</code></pre>"
                + "<h3>Template Method Pattern</h3>"
                + "<pre><code>abstract class DataReport {\n    // Template method — defines the algorithm skeleton\n    final void generate() {\n        fetchData();\n        processData();\n        renderOutput();\n    }\n    abstract void fetchData();\n    abstract void processData();\n    void renderOutput() { System.out.println(\"Rendering output\"); } // hook\n}\nclass CSVReport extends DataReport {\n    void fetchData()    { System.out.println(\"Fetching CSV data\"); }\n    void processData()  { System.out.println(\"Parsing CSV\"); }\n}</code></pre>",

                story(
                    n("Eldrin creates a command crystal for every Academy action — each one can be cast, stored, and most importantly, reversed."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The Command pattern turns actions into first-class objects. An object that can be stored, passed around, queued, logged, and undone. Every action in an undo-able editor, every transaction in a payment system — these are Commands."),
                    e("Template method", "abstract class Ritual {\n    final void perform() { // template\n        prepare(); execute(); cleanup();\n    }\n    abstract void prepare();\n    abstract void execute();\n    void cleanup() { System.out.println(\"Cleanup complete\"); } // default\n}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Template Method defines the skeleton of an algorithm and lets subclasses fill in specific steps. The skeleton is <code>final</code> — cannot be overridden. The variable steps are abstract. This is the Hollywood Principle: don't call us, we'll call you.")
                ),

                "<p>Implement a <code>DataProcessor</code> abstract class with a <code>final process()</code> template method "
                + "that calls <code>loadData()</code>, <code>transform()</code>, and <code>save()</code>. "
                + "Implement <code>JSONProcessor</code> with concrete versions of each.</p>",

                "public class BehaviouralPatterns2 {\n    abstract static class DataProcessor {\n        final void process() { loadData(); transform(); save(); }\n        abstract void loadData();\n        abstract void transform();\n        void save() { System.out.println(\"Saved\"); } // default hook\n    }\n    static class JSONProcessor extends DataProcessor {\n        void loadData()   { System.out.println(\"Loading JSON\"); }\n        void transform()  { System.out.println(\"Transforming JSON\"); }\n    }\n    public static void main(String[] args) {\n        new JSONProcessor().process();\n    }\n}\n",

                tests(
                    test("Load step", "null", "Loading JSON"),
                    test("Transform step", "null", "Transforming JSON"),
                    test("Save step", "null", "Saved")
                ),
                "What is the Command pattern used for? How does undo/redo functionality relate to this pattern?"
        );

        mcQuestion("PM2", QuestionTier.RECALL,
                "<p>In the Template Method pattern, why is the template method typically declared <code>final</code>?</p>",
                new String[]{"To prevent subclasses from changing the algorithm structure", "To make it run faster", "To prevent it from being called", "To allow it to call abstract methods"},
                "To prevent subclasses from changing the algorithm structure",
                "<p>The template method defines the invariant algorithm skeleton. Making it <code>final</code> ensures subclasses can only override the variable steps, not the overall structure.</p>");
    }

    // =========================================================================
    // CHUNK PN — MVC & Repository Pattern
    // =========================================================================
    private void seedChunkPN() {
        chunk("PN", "MVC & Layered Architecture", "\uD83D\uDDFA\uFE0F", 28, LearnerPath.PRACTITIONER, "PM", "PI");

        subChunk("PN1", "PN", "MVC Architecture", 1, 80, "MVCPattern.java",

                "<p>Your single class handles user input, database queries, and formatting output — when one thing changes, everything breaks.</p>",

                "<h3>Model-View-Controller</h3>"
                + "<p>MVC separates an application into three layers:</p>"
                + "<ul>"
                + "<li><strong>Model</strong> — data and business rules; knows nothing about UI</li>"
                + "<li><strong>View</strong> — presentation; knows only how to display model data</li>"
                + "<li><strong>Controller</strong> — mediates between view and model; handles user input</li>"
                + "</ul>"
                + "<pre><code>// Model\nclass Student { String name; int grade; /* ... */ }\n\n// View (presentation)\nclass StudentView {\n    void displayStudent(String name, int grade) {\n        System.out.printf(\"%s: %d%n\", name, grade);\n    }\n}\n\n// Controller\nclass StudentController {\n    private Student model;\n    private StudentView view;\n\n    StudentController(Student m, StudentView v) { model=m; view=v; }\n\n    void setStudentName(String name) { model.name = name; }\n    void updateView() { view.displayStudent(model.name, model.grade); }\n}</code></pre>",

                story(
                    n("Eldrin redesigns the Academy's student management system. Before: one enormous class doing everything. After: three clean layers, each ignorant of the others' internals."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "MVC is separation of concerns made concrete. The Model owns the truth about your data. The View knows only how to display it. The Controller sits between them, directing traffic. Change the UI without touching the Model; change the database without touching the View."),
                    e("MVC in action", "Student s = new Student();\ns.name = \"Alice\"; s.grade = 95;\nStudentView v = new StudentView();\nStudentController ctrl = new StudentController(s, v);\nctrl.updateView(); // Alice: 95\nctrl.setStudentName(\"Alicia\");\nctrl.updateView(); // Alicia: 95"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "In web frameworks like Spring MVC, the Controller is a class annotated <code>@Controller</code>, the Model is passed via <code>Model</code> parameter or DTOs, and the View is a template (Thymeleaf, etc.) or JSON response. The pattern scales to enterprise.")
                ),

                "<p>Implement a simple console MVC: <code>Spell</code> model (name, power), <code>SpellView</code> with "
                + "<code>display(String, int)</code>, and <code>SpellController</code> that updates and displays. "
                + "Create a controller and call <code>updateView()</code>.</p>",

                "public class MVCPattern {\n    static class Spell { String name; int power; }\n    static class SpellView {\n        void display(String name, int power) {\n            System.out.println(name + \" [\" + power + \" dmg]\");\n        }\n    }\n    static class SpellController {\n        private Spell model; private SpellView view;\n        SpellController(Spell m, SpellView v) { model=m; view=v; }\n        void setName(String n) { model.name = n; }\n        void setPower(int p) { model.power = p; }\n        void updateView() { view.display(model.name, model.power); }\n    }\n    public static void main(String[] args) {\n        Spell spell = new Spell();\n        spell.name = \"Fireball\"; spell.power = 80;\n        SpellController ctrl = new SpellController(spell, new SpellView());\n        ctrl.updateView();\n        ctrl.setPower(100);\n        ctrl.updateView();\n    }\n}\n",

                tests(
                    test("Initial display", "null", "Fireball [80 dmg]"),
                    test("After update", "null", "Fireball [100 dmg]")
                ),
                "Explain the three layers of MVC. What are the advantages of this separation?"
        );

        mcQuestion("PN1", QuestionTier.RECALL,
                "<p>In MVC, which component is responsible for handling user input?</p>",
                new String[]{"Controller", "Model", "View", "Service"},
                "Controller",
                "<p>The Controller receives user input, updates the Model, and tells the View to refresh. It mediates between input and output.</p>");

        subChunk("PN2", "PN", "Repository Pattern & Service Layer", 2, 80, "RepositoryPattern.java",

                "<p>Your business logic is tangled with SQL queries. Testing requires a real database. The Repository pattern untangles this.</p>",

                "<h3>Repository Pattern</h3>"
                + "<pre><code>// Repository interface — defines the data contract\ninterface SpellRepository {\n    void save(Spell spell);\n    Optional&lt;Spell&gt; findByName(String name);\n    List&lt;Spell&gt; findAll();\n}\n\n// In-memory implementation (for tests)\nclass InMemorySpellRepository implements SpellRepository {\n    private Map&lt;String,Spell&gt; store = new HashMap&lt;&gt;();\n    public void save(Spell s) { store.put(s.name, s); }\n    public Optional&lt;Spell&gt; findByName(String n) { return Optional.ofNullable(store.get(n)); }\n    public List&lt;Spell&gt; findAll() { return new ArrayList&lt;&gt;(store.values()); }\n}\n\n// Service uses only the interface — swappable\nclass SpellService {\n    private final SpellRepository repo;\n    SpellService(SpellRepository repo) { this.repo = repo; }\n    void registerSpell(String name, int power) {\n        repo.save(new Spell(name, power));\n    }\n    String getSpellInfo(String name) {\n        return repo.findByName(name)\n            .map(s -> s.name + \": \" + s.power + \" dmg\")\n            .orElse(\"Unknown spell\");\n    }\n}</code></pre>",

                story(
                    n("Eldrin separates the Academy library into a public interface and a private filing system. The staff know the interface; only the librarian knows which physical room holds what."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The Repository pattern separates your business logic from data access. Your service talks to an interface — it doesn't know whether data lives in PostgreSQL, MongoDB, or a HashMap in memory. For tests, swap in the InMemory implementation. For production, plug in the real one."),
                    e("Service + Repository", "SpellRepository repo = new InMemorySpellRepository();\nSpellService svc = new SpellService(repo);\nsvc.registerSpell(\"Fireball\", 80);\nSystem.out.println(svc.getSpellInfo(\"Fireball\")); // Fireball: 80 dmg\nSystem.out.println(svc.getSpellInfo(\"Unknown\"));  // Unknown spell"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "This is the power of programming to interfaces. Your SpellService could have been written and tested before the database implementation existed. The interface is the contract; implementations are interchangeable. Spring Data JPA generates the implementation automatically from the interface.")
                ),

                "<p>Implement: <code>WizardRepository</code> interface (save, findByName returning Optional<String>), "
                + "<code>InMemoryWizardRepository</code>, and <code>WizardService</code> that uses it. "
                + "Register 'Merlin' and retrieve their info.</p>",

                "import java.util.*;\n\npublic class RepositoryPattern {\n    interface WizardRepository {\n        void save(String name, int level);\n        Optional<Integer> findLevel(String name);\n    }\n    static class InMemoryWizardRepository implements WizardRepository {\n        private Map<String, Integer> store = new HashMap<>();\n        public void save(String name, int level) { store.put(name, level); }\n        public Optional<Integer> findLevel(String name) { return Optional.ofNullable(store.get(name)); }\n    }\n    static class WizardService {\n        private WizardRepository repo;\n        WizardService(WizardRepository r) { repo = r; }\n        void register(String name, int level) { repo.save(name, level); }\n        String info(String name) {\n            return repo.findLevel(name)\n                .map(l -> name + \" (Lv.\" + l + \")\")\n                .orElse(name + \" not found\");\n        }\n    }\n    public static void main(String[] args) {\n        WizardService svc = new WizardService(new InMemoryWizardRepository());\n        svc.register(\"Merlin\", 99);\n        System.out.println(svc.info(\"Merlin\"));\n        System.out.println(svc.info(\"Gandalf\"));\n    }\n}\n",

                tests(
                    test("Found wizard", "null", "Merlin (Lv.99)"),
                    test("Missing wizard", "null", "Gandalf not found")
                ),
                "What are the benefits of the Repository pattern? How does it improve testability?"
        );

        mcQuestion("PN2", QuestionTier.DISCRIMINATION,
                "<p>Your service class directly uses <code>new JdbcWizardRepository()</code> inside its methods. What problem does this create?</p>",
                new String[]{"Tight coupling — you cannot swap the repository for testing or changing databases", "It is slower than using an interface", "The repository methods won't compile", "The service will throw NullPointerException"},
                "Tight coupling — you cannot swap the repository for testing or changing databases",
                "<p>Using <code>new</code> inside a service creates a hard dependency on the specific implementation. You cannot swap an in-memory repository for tests without modifying the service. Inject the interface via constructor instead.</p>");

        subChunk("PN3", "PN", "SOLID Principles", 3, 90, "SOLIDPrinciples.java",

                "<p>Five principles that, applied together, produce flexible, maintainable object-oriented designs.</p>",

                "<h3>SOLID</h3>"
                + "<ul>"
                + "<li><strong>S</strong> — Single Responsibility: a class should have one reason to change</li>"
                + "<li><strong>O</strong> — Open/Closed: open for extension, closed for modification</li>"
                + "<li><strong>L</strong> — Liskov Substitution: subclasses must be substitutable for their base class</li>"
                + "<li><strong>I</strong> — Interface Segregation: clients shouldn't depend on methods they don't use</li>"
                + "<li><strong>D</strong> — Dependency Inversion: depend on abstractions, not concretions</li>"
                + "</ul>"
                + "<pre><code>// D — Dependency Inversion example\n// Bad:\nclass WizardService {\n    private MySQLWizardRepository repo = new MySQLWizardRepository(); // concrete!\n}\n\n// Good:\nclass WizardService {\n    private final WizardRepository repo; // abstraction!\n    WizardService(WizardRepository repo) { this.repo = repo; } // DI\n}</code></pre>"
                + "<p>Every design pattern you've learned embodies one or more SOLID principles. Strategy = O. Factory = D. Template Method = O & L.</p>",

                story(
                    n("Eldrin stands before a wall etched with five runes — the SOLID principles. 'These are not rules to memorise,' he says, 'they are questions to ask of every class you write.'"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Does this class have one reason to change? Can I extend it without modifying it? If I replace it with a subclass, will things still work? Am I forcing clients to depend on methods they don't need? Am I depending on abstractions or concrete details?"),
                    e("SRP and OCP", "// SRP: separate concerns\nclass SpellParser { Spell parse(String raw) { /* parsing only */ return null; } }\nclass SpellPersistence { void save(Spell s) { /* database only */ } }\n// OCP: extend without modifying\ninterface Formatter { String format(Spell s); }\nclass JSONFormatter implements Formatter { /* ... */ public String format(Spell s) { return \"{}\"; } }\nclass CSVFormatter  implements Formatter { /* ... */ public String format(Spell s) { return \"\"; } }"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "These principles do not guarantee good code — but violating them consistently guarantees fragile, tightly-coupled code that is painful to test and terrifying to change. Apply them as lenses, not as dogma.")
                ),

                "<p>Demonstrate Dependency Inversion: create an interface <code>Logger</code> with <code>log(String msg)</code>. "
                + "Implement <code>ConsoleLogger</code> and <code>SilentLogger</code>. "
                + "Create <code>SpellCaster</code> that accepts a <code>Logger</code> via constructor and logs before casting. "
                + "Demonstrate both loggers.</p>",

                "public class SOLIDPrinciples {\n    interface Logger { void log(String msg); }\n    static class ConsoleLogger implements Logger {\n        public void log(String msg) { System.out.println(\"[LOG] \" + msg); }\n    }\n    static class SilentLogger implements Logger {\n        public void log(String msg) { /* silent */ }\n    }\n    static class SpellCaster {\n        private Logger logger;\n        SpellCaster(Logger l) { logger = l; }\n        void cast(String spell) {\n            logger.log(\"Casting: \" + spell);\n            System.out.println(spell + \" cast!\");\n        }\n    }\n    public static void main(String[] args) {\n        new SpellCaster(new ConsoleLogger()).cast(\"Fireball\");\n        new SpellCaster(new SilentLogger()).cast(\"IceBolt\");\n    }\n}\n",

                tests(
                    test("Console logged", "null", "[LOG] Casting: Fireball"),
                    test("Fireball cast", "null", "Fireball cast!"),
                    test("Silent no log", "null", "IceBolt cast!")
                ),
                "Explain the Dependency Inversion Principle. How does constructor injection relate to it?"
        );

        mcQuestion("PN3", QuestionTier.RECALL,
                "<p>Which SOLID principle says a class should have only one reason to change?</p>",
                new String[]{"Single Responsibility Principle", "Open/Closed Principle", "Liskov Substitution Principle", "Dependency Inversion Principle"},
                "Single Responsibility Principle",
                "<p>SRP: a class should have exactly one reason to change — meaning it should encapsulate one cohesive responsibility. If two concerns change for different reasons, split them into separate classes.</p>");

        scenarioQuestion("PN3",
                "<p>You have a <code>ReportService</code> class that: fetches data from a database, formats it as HTML, and sends it via email. "
                + "A new requirement arrives: support PDF format and also send via Slack. "
                + "Which SOLID principle is being violated and what should you do?</p>",
                "Single Responsibility (and Open/Closed) — split into DataFetcher, ReportFormatter (interface + HTMLFormatter/PDFFormatter), and NotificationSender (interface + EmailSender/SlackSender)",
                "<p>The class has three responsibilities, each with its own reason to change. Applying SRP produces three separate concerns; applying OCP with interfaces for formatting and sending makes the system extensible without modifying existing classes.</p>",
                "PN1", "PN2");
    }
}
