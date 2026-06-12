---
id: de-jun-m8-02
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m8
moduleTitle: "Module 8: Database Testing"
moduleGlyph: "🧪"
moduleSortOrder: 8
topicSlug: integration_testing
topicTitle: "Integration Testing"
topicSortOrder: 2
lesson: integration_testing
title: "Integration Testing"
sortOrder: 2
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m8-01]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what database integration tests verify that unit tests cannot
    - Describes @DataJpaTest and its limitations vs Testcontainers
    - Explains test isolation patterns (transactions, truncation, test-specific data)
    - Identifies what makes a good integration test (specific, deterministic, fast)
    - Describes testing transactional behaviour and rollback scenarios
  keywords: ["integration test", "@DataJpaTest", Testcontainers, "test isolation", rollback, transaction, TestEntityManager, H2, PostgreSQL, "@BeforeEach", "@Transactional", flyway, liquibase, migration, "test database"]
  modelAnswer: |
    Database integration tests verify that application code works correctly with a real database: SQL queries return expected results, constraints fire correctly, transactions commit and roll back appropriately. Unit tests with mocks cannot test these behaviours — a mock repository always returns what you configured, never testing whether the actual SQL is correct. @DataJpaTest: Spring slice test loading only JPA context, using H2 in-memory by default. Faster than full context but H2 doesn't support PostgreSQL-specific SQL. Testcontainers: starts a real PostgreSQL Docker container — slower startup but production-faithful dialect. Test isolation: each test should start with known state — use @BeforeEach to insert fixtures, @Transactional to roll back after each test (for @DataJpaTest), or TRUNCATE at @BeforeEach (for non-transactional tests). Good integration tests are specific (test one behaviour), deterministic (same result every run), and fast (use only the data needed).
guidedSteps:
  - id: de-jun-m8-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A repository test uses @DataJpaTest with H2 in-memory. The production database is PostgreSQL and uses the query: WHERE lower(email) LIKE lower(:pattern). The test passes. Production fails with a syntax error. Why?
    inputConfig:
      options:
        - "H2 has a bug in the LIKE implementation"
        - "H2 and PostgreSQL have different SQL dialects — H2 may not support the exact syntax used, or behave differently. Tests must use the same database as production."
        - "@DataJpaTest automatically translates queries to H2 syntax"
        - "Spring Data JPQL handles dialect differences transparently"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["H2 and PostgreSQL have different SQL dialects — H2 may not support the exact syntax used, or behave differently. Tests must use the same database as production."]
      rejectedFeedback: "H2 is a Java in-memory database that supports a subset of standard SQL. It does not support many PostgreSQL-specific features: JSONB operators (@>, ?->), ILIKE, LATERAL JOIN, ON CONFLICT DO NOTHING, RETURNING clause, array types, custom functions, window function syntax differences, full-text search syntax. When you use nativeQuery = true with PostgreSQL-specific SQL, @DataJpaTest with H2 will either fail the test (catching the problem) or silently accept different syntax and pass (missing the problem). The safe approaches: (1) Use only JPQL for queries — JPQL is database-agnostic and @DataJpaTest can test it correctly. (2) For nativeQuery = true with PostgreSQL-specific SQL, use Testcontainers with a real PostgreSQL container. A test that passes on H2 but fails on PostgreSQL provides false confidence."
    hint: "H2 is not PostgreSQL. What happens when the SQL uses database-specific syntax that H2 doesn't support?"
    reflectionPrompt: "For which types of tests would you always choose Testcontainers over H2?"
  - id: de-jun-m8-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To ensure each integration test starts with a clean, known state, the @BeforeEach method typically ________ the tables and inserts fresh test fixtures.
    inputConfig:
      placeholder: "truncates"
    markingRule:
      matchMode: CONTAINS
      accepted: [truncate, truncates, truncating, TRUNCATE, "truncate the", "truncates the", "clears", "deletes", "resets", "rolls back", "rollback"]
      rejectedFeedback: "Test isolation ensures that test A's data does not affect test B. Common patterns: (1) TRUNCATE + reinsert: @BeforeEach truncates all test tables (TRUNCATE TABLE loans, members, items RESTART IDENTITY CASCADE) and inserts only the fixtures needed for this test. Slower but ensures a completely clean state. (2) @Transactional on the test class: @DataJpaTest wraps each test in a transaction that is rolled back after the test — no data persists between tests. Fast but requires awareness that @Transactional semantics affect what you can test (cannot test commit-dependent behaviour). (3) TestEntityManager.persistAndFlush(): inserts test data that is visible to the current persistence context. (4) Use test-specific IDs (e.g., use member ID 99000+ for test data) to avoid clashing with existing data. The right choice depends on the test framework and whether you need to test commit-dependent behaviour."
    hint: "To get a clean slate before each test, you remove all existing rows and start fresh."
    reflectionPrompt: "Why is the @Transactional rollback approach potentially misleading for testing commit-dependent behaviour?"
  - id: de-jun-m8-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain what a database integration test can verify that a unit test with a mocked repository cannot.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [SQL, constraint, query, actual, real, database, transaction, rollback, dialect, index, FK, trigger, correct, behaviour]
      rejectedFeedback: "Unit tests with mock repositories verify application logic — that the service calls the right method with the right parameters. They cannot verify: (1) SQL correctness — a mock returns whatever you configured; a real database executes the SQL and either returns the correct result or throws a syntax/logic error. (2) Constraint violations — a mock doesn't enforce NOT NULL, CHECK, or FK constraints. Integration tests verify that your INSERT actually fails when it should. (3) Query results — a derived query like findByDueDateBeforeAndReturnDateIsNull might have an off-by-one error in the date comparison; a mock returns what you set up, not what the real query returns. (4) Transaction behaviour — that a @Transactional method actually commits on success and rolls back on exception. (5) Trigger behaviour — audit triggers fire correctly. (6) Index effectiveness — an index test can verify that a query uses an index via EXPLAIN. Only a real database can verify these."
    hint: "What does a mock repository return regardless of the SQL you wrote? What about a real database?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "When using Testcontainers in a JUnit 5 test, @Container annotation ensures:"
    options:
      - "The test runs inside the Docker container"
      - "The PostgreSQL container is started before tests run and stopped after, with the connection URL injected via @DynamicPropertySource"
      - "Testcontainers automatically generates test data"
      - "The container is created fresh for each individual test method"
    correctIndex: 1
    feedback: "Testcontainers @Container lifecycle (with @Testcontainers on the class): the container starts once before all tests in the class (when declared as a static field) and stops after all tests complete — or starts/stops per test (when declared as an instance field, slower). @DynamicPropertySource injects the container's connection URL, username, and password into Spring's property system, overriding the normal datasource config. The test class then uses a real PostgreSQL database for all tests. Starting a container takes 5-15 seconds (Docker image pull: once; container start: ~3 seconds). To optimise: declare as static (shared across all tests in the class), use a singleton pattern (one container shared across all test classes in the JVM using a shared PostgreSQL container with a unique schema per test class)."
  - type: MULTIPLE_CHOICE
    question: "Testing that a unique constraint correctly rejects duplicate insertions requires:"
    options:
      - "A unit test that mocks the repository and asserts an exception is thrown"
      - "An integration test that actually attempts the duplicate INSERT and asserts a DataIntegrityViolationException is thrown"
      - "Checking the database schema to verify the UNIQUE constraint exists"
      - "A static analysis tool that detects duplicate INSERTs at compile time"
    correctIndex: 1
    feedback: "Constraint behaviour testing requires a real database. A mocked repository will not throw DataIntegrityViolationException for duplicate INSERTs — it just executes whatever you configured. The integration test: (1) Insert a member with email = 'alice@example.com'. (2) Attempt to insert another member with the same email. (3) Assert that DataIntegrityViolationException (Spring's translation of SQL constraint violations) is thrown. (4) Assert the database still contains exactly one member with that email (the first). This tests three things: the UNIQUE constraint exists in the schema, Spring correctly translates the database exception, and the application rolls back correctly on the violation. Schema verification (option C) only tests structure, not runtime behaviour — a constraint can exist in the schema but be incorrect or disabled."
retrieval:
  recall: "Describe the complete setup for a @DataJpaTest that tests the findOverdueLoans() repository method: what annotations, what test data setup, what assertion, and how the test isolates its data from other tests."
  explain: "Compare @DataJpaTest with H2 vs Testcontainers with PostgreSQL for testing a repository. When would you choose each, and what are the specific trade-offs?"
  mistakeId:
    code: |
      @DataJpaTest
      class LoanRepositoryTest {
          @Autowired LoanRepository loanRepository;
          
          @Test
          void findOverdue_returnsOverdueLoans() {
              // No setup — relies on data in the test database
              List<Loan> loans = loanRepository.findOverdueWithDetails(LocalDate.now());
              assertThat(loans).hasSize(3);  // expects 3 rows to exist
          }
      }
    answer: "Three problems: (1) No test data setup: the test relies on pre-existing data in the test database (or data inserted by other tests). If the database is empty, the test fails spuriously. If other tests insert data, the count of 3 may be wrong. Tests must be self-contained — insert their own test data. (2) assertThat(loans).hasSize(3): this is a fragile assertion. It assumes exactly 3 overdue loans will exist after setup. A better assertion is: insert exactly N known overdue loans and N non-overdue loans, then assert the result contains exactly the overdue ones by their known IDs — not by count. (3) No @BeforeEach or transaction rollback strategy documented: if multiple tests share the test context, data from one test bleeds into another. @DataJpaTest wraps each test in a rolled-back transaction by default — but if the test inserts its own data, it should do so inside the test method or @BeforeEach. Fix: add @BeforeEach that inserts 2 overdue and 1 non-overdue loan; assert by loan ID not by count."
---

# Hook

Unit tests verify that your code calls the right methods with the right arguments. Only integration tests verify that the SQL is correct, the constraints fire as expected, and transactions behave properly. A test suite that mocks the database is testing your assumptions about the database — not the database itself.

# Lore Introduction

"The overdue loans report was showing the wrong count in production," the Junior Engineer said. "The unit tests all passed." The Senior Archivist looked at the test. "Show me the repository test." The Junior opened it. "We mock the repository. The mock returns three loans. The test passes." The Senior Archivist shook her head. "The mock returns what you told it to return. The actual SQL query had an off-by-one error in the date comparison — due_date <= CURRENT_DATE instead of due_date < CURRENT_DATE. The mock has no idea. Only a test against a real database would catch that." The Junior looked at the passing tests. "So unit tests give false confidence for database behaviour." The Senior Archivist confirmed. "For business logic: unit tests are correct. For database interactions: integration tests against a real database are required."

# Core Learning

## Concept Introduction

### @DataJpaTest — Fast JPA Slice Tests

```java
@DataJpaTest   // loads only JPA context (repositories, EntityManager, DataSource)
class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TestEntityManager em;  // helper for inserting test data

    // @DataJpaTest wraps each test in a transaction, rolled back after test
    // → data doesn't persist between tests

    @Test
    void findOverdueWithDetails_onlyReturnsUnreturnedPastDueDate() {
        // ARRANGE: insert known test data
        Member member = em.persistAndFlush(
            new Member("Alice", "alice@test.com", "Standard"));
        Item item = em.persistAndFlush(
            new Item("SQL Mastery", "isbn-001", "Technology"));

        // Overdue loan: due yesterday, not returned
        Loan overdueLoam = em.persistAndFlush(new Loan(
            member, item,
            LocalDate.now().minusDays(10),  // loan_date
            LocalDate.now().minusDays(1),   // due_date (yesterday)
            null));                          // return_date (not returned)

        // Active loan: due next week
        em.persistAndFlush(new Loan(
            member, item,
            LocalDate.now().minusDays(3),
            LocalDate.now().plusDays(7),
            null));

        // Returned loan: was overdue but now returned
        em.persistAndFlush(new Loan(
            member, item,
            LocalDate.now().minusDays(20),
            LocalDate.now().minusDays(10),
            LocalDate.now().minusDays(5)));  // has return_date

        // ACT
        List<Loan> overdue = loanRepository.findOverdueWithDetails(LocalDate.now());

        // ASSERT: only the overdue, unreturned loan
        assertThat(overdue).hasSize(1);
        assertThat(overdue.get(0).getId()).isEqualTo(overdueLoan.getId());
        assertThat(overdue.get(0).getMember()).isNotNull();  // JOIN FETCH loaded
        assertThat(overdue.get(0).getItem()).isNotNull();
    }
}
```

### Testcontainers — Real PostgreSQL Tests

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class LoanRepositoryPostgresTest {

    // Shared container — starts once for all tests in this class
    @Container
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("archive_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired private LoanRepository loanRepository;
    @Autowired private TestEntityManager em;

    // Schema applied by Flyway/Liquibase migration — real production schema

    @Test
    void nativeQueryWithPostgresSpecificSyntax() {
        // Tests that use JSONB, ILIKE, arrays, window functions, RETURNING:
        // These ONLY work against real PostgreSQL — H2 will fail or behave differently
        List<Object[]> stats = loanRepository.getLoanStatsByMonth();
        assertThat(stats).isNotEmpty();
    }

    @Test
    void uniqueConstraint_rejectsSecondActiveLoad_forSameMemberItem() {
        Member m = em.persistAndFlush(new Member("Bob", "bob@test.com", "Standard"));
        Item i = em.persistAndFlush(new Item("Java Guide", "isbn-002", "Technology"));
        em.persistAndFlush(new Loan(m, i, LocalDate.now(), LocalDate.now().plusDays(14), null));

        // Second active loan for same member+item — should fail
        assertThatThrownBy(() -> {
            em.persistAndFlush(new Loan(m, i, LocalDate.now(), LocalDate.now().plusDays(14), null));
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}
```

### Test Isolation Strategies

```java
// Strategy 1: @Transactional rollback (default in @DataJpaTest)
// Each test runs in a transaction that is rolled back at the end
// ✓ Fast, no cleanup needed
// ✗ Cannot test commit-dependent behaviour (e.g., testing that a trigger fires on commit)

@DataJpaTest  // @Transactional is applied by default — each test rolls back
class TransactionalIsolationTest { ... }

// Strategy 2: @BeforeEach TRUNCATE (for non-transactional isolation)
// Use when you need to test actual commits
@BeforeEach
void cleanDatabase(
    @Autowired JdbcTemplate jdbcTemplate
) {
    jdbcTemplate.execute("TRUNCATE TABLE loans, members, items RESTART IDENTITY CASCADE");
}

// Strategy 3: Test data builders (readable, maintainable fixtures)
static Loan buildTestLoan(Member m, Item i, int daysOverdue) {
    return new Loan(m, i,
        LocalDate.now().minusDays(daysOverdue + 14),
        LocalDate.now().minusDays(daysOverdue),  // already past due
        null);
}
```

### Testing Transactional Behaviour

```java
// Test that a service-level transaction rolls back on exception
@SpringBootTest  // full context — tests service + repository together
@Transactional
class LoanServiceIntegrationTest {

    @Autowired private LoanService loanService;
    @Autowired private LoanRepository loanRepository;
    @Autowired private InventoryRepository inventoryRepository;

    @Test
    void checkOut_rollsBackAllChanges_whenInventoryFails() {
        // ARRANGE: item with 0 stock
        Long itemId = insertItemWithZeroStock();
        Long memberId = insertActiveMember();
        long loanCountBefore = loanRepository.count();

        // ACT + ASSERT: should fail because item is out of stock
        assertThatThrownBy(() -> loanService.checkOutItem(memberId, itemId))
            .isInstanceOf(ItemNotAvailableException.class);

        // VERIFY: no loan was created (full rollback)
        assertThat(loanRepository.count()).isEqualTo(loanCountBefore);
        // VERIFY: inventory unchanged
        assertThat(inventoryRepository.getStock(itemId)).isEqualTo(0);
    }
}
```

## Why It Matters

Unit tests with mocked repositories prove nothing about the SQL itself — integration tests against a real database are where data bugs actually get caught:

- Constraint violations, type mismatches, and dialect quirks only surface when real SQL hits a real engine
- Tools like Testcontainers make a throwaway Postgres per test run practical, killing the "works on H2, fails on prod" class of bug
- The expensive failures — broken migrations, ORM mappings that lazy-load in a loop — are exactly the ones only integration tests see

If your data layer has no integration tests, your first real test environment is production.

## Common Mistakes

- **Mocking repositories for all tests**: integration tests fill the gap that unit tests cannot. Always have at least the repository layer tested against a real database (H2 for simple queries, Testcontainers for PostgreSQL-specific queries).
- **Tests that depend on database state from other tests**: test execution order is not guaranteed. Each test must set up its own data and must not assume what other tests left behind. Use @BeforeEach or @Transactional rollback for isolation.
- **One Testcontainer per test class**: starting a new container for every test class adds minutes to the test suite. Use a shared singleton container pattern or Spring's container sharing via @ServiceConnection (Spring Boot 3.1+).
- **Asserting by count, not by identity**: `assertThat(loans).hasSize(2)` fails when other tests leave data. Assert by the specific IDs or properties of the data you inserted in that test.

## Mental Model

Integration tests are like end-to-end functional tests for the data layer. Unit tests verify that a component behaves according to its specification in isolation. Integration tests verify that the components work together correctly — the SQL actually runs, the constraints actually fire, the transactions actually commit or roll back. A test pyramid for data engineering: unit tests (fast, many, mock the database), integration tests (slower, fewer, real database), and end-to-end tests (slowest, fewest, full application stack).

## Mini Summary

- ✔ Integration tests verify SQL correctness, constraint behaviour, and transaction semantics
- ✔ @DataJpaTest: fast JPA slice test, H2 in-memory default — use for JPQL and ORM behaviour
- ✔ Testcontainers: real PostgreSQL — use for nativeQuery, PostgreSQL-specific SQL, constraints
- ✔ Test isolation: each test must set up its own data — use @Transactional rollback or @BeforeEach TRUNCATE
- ✔ Test by ID, not by count — make assertions specific to the data you inserted
- ✔ Test constraint violations: assert DataIntegrityViolationException for duplicate/null/FK violations
- ✔ Test transactional rollback: verify that failed transactions leave no partial state

# Guided Practice Quest

Work through the guided steps to write a Testcontainers-based test for the `findOverdueWithDetails` query, assert that the unique loan constraint fires correctly on a duplicate insert, and write a test verifying that a failed checkout (out-of-stock) rolls back the loan creation.

# Solo Practice Quest

Build a comprehensive integration test suite for the Archive repository layer. Tasks: (1) Set up a shared Testcontainers PostgreSQL container used across all repository test classes — implement the singleton container pattern; (2) Write @DataJpaTest tests for five repository methods: the overdue query, member-by-email lookup, bulk status update, monthly statistics native query, and the dynamic filter search; (3) Write Testcontainers tests for the three tests that use PostgreSQL-specific SQL; (4) Write a @SpringBootTest integration test that verifies the checkout service rolls back when stock reaches zero; (5) Write an integration test for audit trigger behaviour: insert a member, update their email, then query the audit_log table and assert the audit row was created with the correct before/after values; (6) Implement @BeforeEach test isolation for a test class that cannot use @Transactional rollback (because it tests commit-dependent trigger behaviour).

# Integration

**Mathematics**: Test coverage for database integration has formal parallels in combinatorial testing theory. A repository with N query parameters each having K possible values has K^N input combinations. Equivalence class partitioning (dividing inputs into classes that should behave identically) reduces this to manageable test count. For a date filter query: equivalence classes for today's date vs. the threshold include {today = threshold (boundary), today > threshold (normal overdue), today < threshold (not yet due)}. Boundary value analysis: test at the boundary value (exactly due_date = today) and just inside/outside (due_date = today - 1, due_date = today + 1). This maps to the 3-test pattern seen in the guided example above. Formal verification of SQL queries uses predicate logic: the query SELECT * FROM loans WHERE due_date < :today AND return_date IS NULL is correct iff for all rows r, r appears in the result ↔ r.due_date < today AND r.return_date IS NULL. The integration tests provide empirical evidence for this ↔ relationship by exercising representative cases.

**Sciences (Engineering — Testing and Inspection Standards)**: Integration testing methodology is formalised in engineering testing standards such as IEC 61508 (Functional Safety) and ISO 26262 (Automotive). These standards distinguish unit testing (testing components in isolation with stubs), integration testing (testing assembled components with real interfaces), and system testing (testing the complete system). For safety-critical software, these standards require integration tests to verify that: interfaces between components work correctly, the combined behaviour matches the system specification, and error handling and recovery work across component boundaries. Database integration tests satisfy the IEC 61508 requirement for interface testing: the interface between the application code and the database is tested with real database calls, real SQL, and real constraint enforcement — not with simulated interfaces (mocks). The distinction matters because the interface specification (SQL dialect, constraint behaviour, transaction semantics) is defined by the database, not by the test engineer's mock.

# Lore Conclusion

"The off-by-one date error is caught by the integration test," the Junior Engineer reported. "The test inserts a loan due yesterday and asserts it appears in overdue results. A loan due today does not appear — boundary case confirmed." The Senior Archivist reviewed the test suite. "Testcontainers?" The Junior showed the configuration. "Shared PostgreSQL container across all repository tests — starts once, runs all tests, stops. The suite takes 45 seconds total." The Senior Archivist nodded. "And the constraint violation tests?" The Junior pulled up the results. "Duplicate active loan for the same member and item throws DataIntegrityViolationException correctly. The unique partial index is enforced." She reviewed the checkout rollback test. "Failed checkout — zero stock — leaves no loan row. Full rollback verified." The Senior Archivist set the results down. "Integration tests complete. Two more topics: test data management and migration testing."

---
