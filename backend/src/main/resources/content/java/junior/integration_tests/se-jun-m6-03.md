---
id: se-jun-m6-03
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m6
moduleTitle: "Module 6: Testing"
moduleGlyph: "🧪"
moduleSortOrder: 6
topicSlug: integration_tests
topicTitle: "Integration Tests"
topicSortOrder: 3
lesson: integration_tests
title: "Integration Tests"
sortOrder: 3
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [unit_tests]
integrationDomains: [design, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Distinguishes what integration tests verify vs unit tests"
    - "Names at least one Spring Boot integration test annotation"
    - "Explains why an in-memory database is used for tests"
    - "Describes a scenario where an integration test catches a bug a unit test would miss"
    - "Acknowledges the cost trade-off (integration tests are slower)"
  keywords: [integration, spring, database, h2, context, repository, service, end-to-end, slow, real]
  modelAnswer: |
    // Integration test with Spring Boot
    @SpringBootTest
    @Transactional
    class UserServiceIntegrationTest {

        @Autowired
        private UserService userService;

        @Autowired
        private UserRepository userRepository;

        @Test
        void createUser_persistsToDatabase() {
            CreateUserRequest request = new CreateUserRequest("alice@example.com", "Alice");
            User saved = userService.createUser(request);
            Optional<User> found = userRepository.findById(saved.getId());
            assertTrue(found.isPresent());
            assertEquals("Alice", found.get().getName());
        }
    }
guidedSteps:
  - id: it-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does `@SpringBootTest` do when added to a test class?
    inputConfig:
      options:
        - "Runs only the service layer without the database"
        - "Loads the full Spring application context for the test"
        - "Creates a mock of every Spring bean"
        - "Replaces the database with an in-memory version"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Loads the full Spring application context for the test"]
      rejectedFeedback: "`@SpringBootTest` bootstraps the full Spring application context — all beans, configuration, and wiring — just as if the app started normally. This lets tests exercise real component interactions."
    hint: "Think about what 'integration' means — components working together through their real wiring."
    reflectionPrompt: "Because `@SpringBootTest` loads the real context, it's slower than a unit test. That's the trade-off: you get real interaction fidelity at the cost of startup time."
  - id: it-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To prevent integration tests from permanently writing data to the database,
      annotate the test class with `@___` so all changes are rolled back after each test.
    inputConfig:
      placeholder: "Spring annotation"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Transactional", "@Transactional", "transactional"]
      rejectedFeedback: "`@Transactional` on a test class wraps each test in a transaction that is automatically rolled back after the test completes. The database is left in a clean state for the next test."
    hint: "It's the same annotation used on service methods to manage database transactions."
    reflectionPrompt: "`@Transactional` on tests is a clean, cheap way to keep tests independent. Each test starts from the same baseline database state without needing explicit cleanup code."
  - id: it-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe one bug that an integration test would catch but a unit test would miss. Be specific about what the unit test would say (pass) and what the integration test would reveal.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [database, query, mapping, column, join, sql, persist, repository, constraint, transaction]
      rejectedFeedback: "Example: A unit test of `UserService.createUser()` with a mocked repository would pass even if the JPA `@Column(name='email_address')` mapping was wrong. The integration test — which uses a real H2 database — would fail because the SQL insert would reference a non-existent column."
    hint: "Think about the gap between what your code *thinks* will happen at the database level, and what actually happens."
    reflectionPrompt: "ORM misconfigurations, wrong SQL queries, missing foreign key constraints, and transaction boundary bugs all fall into this category. Unit tests mock the data layer away; integration tests exercise it."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why use an H2 in-memory database in integration tests rather than the production database?"
    options:
      - "H2 is faster and more accurate than real databases"
      - "It avoids polluting production data and makes tests fast and self-contained"
      - "H2 supports more SQL features than PostgreSQL"
      - "Integration tests don't actually need a database"
    correctIndex: 1
    feedback: "H2 starts fresh for each test run, requires no external setup, runs entirely in memory (fast), and doesn't touch production data. The trade-off: some database-specific behaviour may differ from the real DB."
  - type: MULTIPLE_CHOICE
    question: "Which of these bugs would a unit test (with mocked dependencies) NOT catch but an integration test would?"
    options:
      - "A method returning the wrong value"
      - "An incorrect JPA column mapping causing a database error"
      - "A missing null check in business logic"
      - "An off-by-one error in a calculation"
    correctIndex: 1
    feedback: "JPA mapping errors only surface when actual SQL is executed against a real database. Unit tests mock the repository and never touch SQL, so the mapping error is invisible until integration test time."

retrieval:
  recall: "What is the difference between a unit test and an integration test? Give an example of each."
  explain: "Explain to a junior developer why you would choose an integration test over a unit test for a specific scenario."
  mistakeId:
    code: |
      @SpringBootTest
      class ProductServiceTest {
          @Autowired
          ProductRepository realRepo;

          @Test
          void saveProduct_checkExists() {
              productService.save(new Product("Widget", 9.99));
              assertEquals(1, realRepo.count());
          }
      }
    answer: "This test doesn't clean up after itself. After it runs, the product remains in the database and the count may be wrong for the next test run. Add `@Transactional` to the class (auto-rollback) or use `@AfterEach` to explicitly delete the test data."
---

# Hook

Your unit tests all pass. Every service method is tested in isolation with mocked dependencies. You're confident.

Then you deploy. The app starts. A user tries to save their profile. Database error. Column `user_name` doesn't exist — someone renamed it to `username` in a migration and the JPA mapping was never updated.

Your unit tests never saw the database. Your mocks returned whatever you told them to return. The integration between your code and the actual database was never tested.

Integration tests close this gap.

> What's an example of a component in your codebase that your unit tests never actually exercise against real infrastructure?

# Lore Introduction

Unit tests verify the individual runes. But a spell is more than its component runes — it's the *binding* between them. A protection ward might have perfect individual components yet fail catastrophically because the binding rune was inscribed on the wrong surface.

The Academy's Integration Chamber tests complete spell assemblies against real materials. Not simulations. Not stand-ins. Real stone, real mana, real consequences.

*"A rune that works in isolation but fails in assembly is not a working rune,"* Archmage Veylan says. *"The integration test is honest in a way the unit test cannot be."*

# Core Learning

## Concept Introduction

An **integration test** verifies that multiple components work correctly *together* — typically including real infrastructure like a database.

```java
@SpringBootTest           // loads full Spring context
@Transactional            // rolls back changes after each test
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_validUser_persistsAndRetrievesCorrectly() {
        User user = new User("alice@example.com", "Alice");
        User saved = userRepository.save(user);

        Optional<User> found = userRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("alice@example.com", found.get().getEmail());
    }
}
```

For the database, add H2 to `test` scope in `pom.xml`:
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

Spring Boot auto-configures H2 for tests when it's on the classpath.

## Why It Matters

Integration tests catch a class of bugs that unit tests cannot:
- **ORM mapping errors** — wrong column names, missing relationships
- **Query correctness** — custom `@Query` methods that return wrong results
- **Transaction behaviour** — rollback not happening where expected
- **Spring wiring errors** — beans not autowired correctly, circular dependencies
- **Schema migration issues** — Flyway migrations failing or producing wrong structure

The testing pyramid says: many unit tests, fewer integration tests, fewest end-to-end tests. Integration tests are slower and more complex — use them where they add unique value.

## Worked Examples

**Testing a custom query:**
```java
@SpringBootTest
@Transactional
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findByStatus_returnsOnlyMatchingOrders() {
        orderRepository.save(new Order("PENDING"));
        orderRepository.save(new Order("PENDING"));
        orderRepository.save(new Order("COMPLETE"));

        List<Order> pending = orderRepository.findByStatus("PENDING");

        assertEquals(2, pending.size());
        assertTrue(pending.stream().allMatch(o -> "PENDING".equals(o.getStatus())));
    }
}
```

**Testing a service that calls the database:**
```java
@SpringBootTest
@Transactional
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    void createProduct_lowStock_setsLowStockFlag() {
        Product product = productService.create("Widget", 9.99, 3);
        assertTrue(product.isLowStock()); // triggered when quantity < 5
    }
}
```

## Common Mistakes

- **Not using `@Transactional`** — tests leave data in the database, causing test order dependencies.
- **Testing too much in one test** — integration tests are slower, so keep each test focused.
- **Coupling to test data order** — tests should pass regardless of execution order.
- **Using the production database** — always use an in-memory or dedicated test database.
- **Treating integration tests as a substitute for unit tests** — they are complementary, not replacements.

## Mental Model

Think of testing as **layers of a net**. Unit tests are a fine-mesh net that catches small bugs in individual methods. Integration tests are a coarser net underneath — they miss the small bugs (already caught above) but catch the larger bugs that fall through the gaps: component mismatches, infrastructure errors, wiring problems.

You need both layers.

## Mini Summary

- ✔ `@SpringBootTest` loads the full Spring context, enabling real component interaction
- ✔ `@Transactional` on a test class auto-rolls back changes after each test
- ✔ H2 in-memory database provides a fast, isolated database for tests
- ✔ Integration tests catch ORM mismatches, query bugs, and wiring errors that unit tests miss
- ✔ Use integration tests selectively — they're slower than unit tests

# Guided Practice Quest

**The Integration Chamber**

Three components — `ProductService`, `ProductRepository`, and H2 — need to be tested together. Verify that the integration between them is correct.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You have a `BookmarkService` that saves URLs for users. It has a method:
```java
Bookmark save(String url, Long userId)
```
And a repository with a custom query:
```java
List<Bookmark> findByUserIdOrderByCreatedAtDesc(Long userId)
```

Design **three integration tests** for this service:
1. Saving a bookmark persists it and returns an entity with a non-null ID
2. The custom query returns bookmarks in the correct order (most recent first)
3. Saving a duplicate URL for the same user throws a constraint violation (or is handled by your choice)

For each test, write the full `@Test` method outline including: setup, call, assertion, and annotations. Explain in your answer what each test verifies that a unit test would miss.

# Integration

**Connecting to Philosophy — The Coherence Theory of Truth**

The correspondence theory of truth says a statement is true if it *corresponds* to reality. The coherence theory says a statement is true if it *coheres* with other accepted beliefs — it fits into a consistent whole without contradiction.

Unit tests are coherentist: they verify that a piece of code is internally consistent — given this input, this output follows, logically. Integration tests are correspondence tests: they verify that your code corresponds to reality — the database, the ORM, the schema — as they actually exist.

A system can be internally coherent (all unit tests pass) and yet fail to correspond to reality (integration tests fail). The JPA mapping error passes coherence but fails correspondence.

This maps to a broader principle in software: local correctness does not imply global correctness. A perfectly coded service that makes wrong assumptions about its dependencies is broken in the only way that ultimately matters — at runtime.

How does this suggest you should divide your testing effort across unit and integration tests?

# Lore Conclusion

The Integration Chamber confirms it: components work together. The Repository speaks to H2. The Service speaks to the Repository. The data flows correctly and rolls back cleanly.

*"Integration tests are slower,"* Archmage Veylan acknowledges. *"But they catch what unit tests cannot. The question is never 'should I write integration tests?' — the question is 'which interactions are important enough to test at the integration level?'"*

The net has two layers now. But there is still something untested: everything a mock replaces.
---
