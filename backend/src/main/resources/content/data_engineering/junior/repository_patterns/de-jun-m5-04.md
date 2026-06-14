---
id: de-jun-m5-04
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m5
moduleTitle: "Module 5: Application Data Access"
moduleGlyph: "🔌"
moduleSortOrder: 5
topicSlug: repository_patterns
topicTitle: "Repository Patterns"
topicSortOrder: 4
lesson: repository_patterns
title: "Repository Patterns"
sortOrder: 4
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m5-03]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains the Repository pattern and why it separates data access from business logic
    - Describes the difference between a Repository and a DAO
    - Explains the Unit of Work pattern in the context of Spring @Transactional
    - Identifies when to use a custom repository implementation vs derived queries
    - Describes how to test repositories without a live database
  keywords: [Repository, DAO, "Unit of Work", "domain model", "data access", interface, abstraction, "Spring Data", "custom implementation", testability, H2, "@DataJpaTest", "query method", specification]
  modelAnswer: |
    The Repository pattern provides a collection-like interface for accessing domain objects, hiding persistence details from business logic. Business logic calls repository.findOverdueLoans() — it doesn't know if that hits a database, cache, or file. DAO (Data Access Object) is similar but more procedure-oriented; Repository is domain-model-oriented and often returns domain objects. Unit of Work: coordinates writes across multiple repositories within a single transaction — in Spring, @Transactional on the service method is the Unit of Work boundary. Custom repository implementations: when JPQL/derived queries aren't expressive enough, implement the custom interface + Impl suffix class. Test with @DataJpaTest (loads only JPA context, uses H2 in-memory) or with Testcontainers for production-faithful tests.
guidedSteps:
  - id: de-jun-m5-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A service method calls loanRepository.save(loan) and inventoryRepository.decrementStock(itemId) in the same method. What ensures both operations succeed or fail together?
    inputConfig:
      options:
        - "Spring Data JPA automatically wraps multiple repository calls in one transaction"
        - "@Transactional on the service method — both repository calls participate in the same transaction"
        - "The database auto-commits after each repository call, so they are independent"
        - "Each repository manages its own transaction — they cannot be combined"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["@Transactional on the service method — both repository calls participate in the same transaction"]
      rejectedFeedback: "@Transactional on the service method is the Unit of Work: it opens one transaction, both repository operations join it, and the transaction commits or rolls back atomically at the end. Without @Transactional on the service: each Spring Data repository method opens and commits its own transaction. loanRepository.save(loan) commits. If inventoryRepository.decrementStock(itemId) then fails, loan is already saved — data is inconsistent. The @Transactional annotation on the service method is the correct place to define transaction boundaries for operations that must succeed together. Repository @Transactional (on the repository method itself) is the fallback for single-operation atomicity."
    hint: "Which annotation defines the boundary within which multiple operations are treated as one atomic unit?"
    reflectionPrompt: "What happens if the service method throws a RuntimeException partway through — does @Transactional roll back automatically?"
  - id: de-jun-m5-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In Spring Data, to add a custom query method not expressible with derived queries, you create a separate interface and a class named with the ________ suffix that implements it.
    inputConfig:
      placeholder: "Impl"
    markingRule:
      matchMode: CONTAINS
      accepted: [Impl, impl, Implementation, implementation, "Impl suffix", "repository impl"]
      rejectedFeedback: "Spring Data's custom repository fragment pattern: (1) Define a separate interface, e.g. LoanRepositoryCustom with method findComplexQuery(). (2) Create an implementation class named exactly LoanRepositoryImpl (the main repository interface name + 'Impl') — Spring Data auto-discovers this class. (3) Make LoanRepository extend both JpaRepository<Loan, Long> and LoanRepositoryCustom. Spring Data automatically weaves the Impl class into the repository proxy. The Impl class can @Autowire EntityManager directly. Alternative: use @Query with nativeQuery = true on the main repository interface for complex SQL without needing a separate class."
    hint: "Spring Data looks for a class with a specific suffix to find your custom implementation."
    reflectionPrompt: "When would you choose @Query(nativeQuery=true) over a custom repository Impl class?"
  - id: de-jun-m5-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why testing a repository with @DataJpaTest is faster and more focused than testing with @SpringBootTest, and one scenario where you should use Testcontainers instead of H2.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [H2, context, slice, fast, specific, PostgreSQL, dialect, JSON, array, production, Testcontainers, in-memory, partial]
      rejectedFeedback: "@DataJpaTest is a test slice: loads only the JPA layer (EntityManager, repositories, DataSource) — not the full Spring context (controllers, services, message queues). This makes it significantly faster to start. It auto-configures an in-memory H2 database, rolling back each test automatically. Use Testcontainers instead when: (1) Your queries use PostgreSQL-specific syntax not supported by H2 — JSON operators (@>, ?), ARRAY types, window functions with specific PostgreSQL behaviour, RETURNING clause, ILIKE. (2) You need to verify query performance with real data volumes. (3) Your schema uses PostgreSQL-specific column types (JSONB, UUID primary keys with gen_random_uuid(), custom types). Testcontainers spins up a real PostgreSQL Docker container — slower startup but production-faithful."
    hint: "Think about what @DataJpaTest loads compared to the full application, and what H2 cannot replicate."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The Repository pattern's primary benefit over direct use of EntityManager/JDBC in service classes is:"
    options:
      - "It makes queries faster by caching results"
      - "It hides persistence implementation details from business logic — services depend on interfaces, not specific database technology"
      - "It automatically handles database transactions"
      - "It generates SQL queries without requiring annotations"
    correctIndex: 1
    feedback: "The Repository pattern's core benefit is abstraction: business logic depends on a LoanRepository interface, not on Hibernate, JDBC, or any specific database. This enables: (1) Testability — inject a mock or in-memory repository in unit tests without a database. (2) Substitutability — switch from PostgreSQL to MongoDB by providing a new repository implementation; services don't change. (3) Single Responsibility — repositories own persistence logic; services own business logic. (4) Readability — service code reads as domain operations (findOverdueLoans(), markReturned()) rather than database plumbing. This is the Dependency Inversion Principle (D in SOLID): depend on abstractions, not concretions."
  - type: MULTIPLE_CHOICE
    question: "Spring Data JPA's Specification API (JpaSpecificationExecutor) is most useful when:"
    options:
      - "You need to run stored procedures from your repository"
      - "You need to build complex dynamic queries at runtime — different filter combinations depending on user input"
      - "You need to improve query performance by adding indexes programmatically"
      - "You need to execute queries across multiple databases simultaneously"
    correctIndex: 1
    feedback: "The Specification API lets you build WHERE clause predicates programmatically and combine them: Specification<Loan> spec = Specification.where(hasStatus('ACTIVE')).and(dueBefore(today)).and(hasMemberTier('PREMIUM')). Each piece is a reusable predicate object. This is useful for search/filter UIs where the user selects any combination of filters — generating safe, type-checked JPQL rather than building query strings. Alternative: QueryDSL (strongly typed query DSL, better refactoring support). Both are preferable to string concatenation for dynamic queries. For fixed queries with known parameters, plain @Query or derived methods are simpler."
retrieval:
  recall: "Describe the structure of a custom repository implementation in Spring Data JPA: what files you create, their naming conventions, and how Spring discovers and wires them."
  explain: "Explain the difference between the Repository pattern and the DAO (Data Access Object) pattern, focusing on what each returns (domain objects vs DTOs/raw data) and their relationship to the domain model."
  mistakeId:
    code: |
      @Service
      public class LoanService {
          @Autowired
          private EntityManager entityManager;
          
          public List<Loan> getOverdueLoans() {
              return entityManager.createQuery(
                  "SELECT l FROM Loan l WHERE l.dueDate < CURRENT_DATE",
                  Loan.class
              ).getResultList();
          }
          
          public void returnLoan(Long id) {
              Loan loan = entityManager.find(Loan.class, id);
              loan.setReturnDate(LocalDate.now());
          }
      }
    answer: "LoanService directly uses EntityManager — the service class is coupled to JPA internals. This violates the Repository pattern's purpose. Problems: (1) Testing LoanService requires mocking EntityManager, which is complex and brittle. (2) Switching the persistence layer (to JDBC, MongoDB, or a different ORM) requires changing LoanService. (3) JPQL strings scattered across service classes instead of centralised in repositories. (4) The query 'SELECT l FROM Loan l WHERE l.dueDate < CURRENT_DATE' is not reusable from other services. Fix: extract to a LoanRepository extending JpaRepository<Loan, Long> with a @Query method findOverdueLoans(). LoanService injects LoanRepository, not EntityManager. Tests can mock the repository interface trivially."
---

# Hook

The Repository pattern answers a simple question: where does the code that talks to the database live? The answer: behind an interface in its own layer. Business logic never knows if data comes from PostgreSQL, a cache, or a test double. This separation is what makes systems testable, maintainable, and replaceable.

# Lore Introduction

"The service class imports `EntityManager` directly," the Junior Engineer noted, reading the code. "The query string is inside the business logic." The Senior Archivist read it with a measured expression. "The database query is tangled with the business rule. To test the business rule, you must boot a database. To change the query, you modify business logic. To swap persistence technology, you rewrite the service." She highlighted the method. "The Repository pattern: a named, collection-like interface that the service calls. The service knows *what* it needs — overdue loans, active memberships — not *how* to retrieve it. The how is the repository's responsibility." The Junior nodded. "Separation of concerns." The Senior Archivist confirmed. "And the seam that makes the system testable."

# Core Learning

## Concept Introduction

### Repository Pattern Structure

```
Business Logic Layer
─────────────────────────────────────────────────────────
  LoanService (business rules, transaction boundaries)
      │
      │ depends on interface, not implementation
      ▼
  LoanRepository (interface)
      ├── findOverdueLoans(): List<Loan>
      ├── findByMemberId(id): List<Loan>
      ├── save(loan): Loan
      └── markReturned(id, date): void
      
Data Access Layer
─────────────────────────────────────────────────────────
  LoanRepositoryImpl (JpaRepository-based implementation)
      └── actual SQL/JPQL/Hibernate calls live here

Test Layer
─────────────────────────────────────────────────────────
  MockLoanRepository (implements LoanRepository)
      └── in-memory list, no database
```

### Spring Data Repository Interfaces

```java
// Standard repository — inherits CRUD + pagination
public interface LoanRepository extends JpaRepository<Loan, Long> {

    // Derived query (Spring Data generates JPQL from method name):
    List<Loan> findByMemberId(Long memberId);
    List<Loan> findByStatus(String status);
    List<Loan> findByDueDateBeforeAndReturnDateIsNull(LocalDate date);
    Optional<Loan> findByMemberIdAndItemId(Long memberId, Long itemId);
    long countByStatus(String status);
    boolean existsByMemberIdAndStatus(Long memberId, String status);

    // Custom JPQL:
    @Query("SELECT l FROM Loan l JOIN FETCH l.member JOIN FETCH l.item " +
           "WHERE l.dueDate < :today AND l.returnDate IS NULL " +
           "ORDER BY l.dueDate ASC")
    List<Loan> findOverdueWithDetails(@Param("today") LocalDate today);

    // Bulk modify (always add @Modifying):
    @Modifying
    @Query("UPDATE Loan l SET l.returnDate = :date, l.status = 'RETURNED' WHERE l.id = :id")
    int returnLoan(@Param("id") Long id, @Param("date") LocalDate date);

    // Native SQL (PostgreSQL-specific, or complex analytics):
    @Query(value = """
        SELECT DATE_TRUNC('month', loan_date) AS month,
               COUNT(*) AS total,
               AVG(EXTRACT(DAY FROM (COALESCE(return_date, CURRENT_DATE) - loan_date))) AS avg_days
        FROM loans GROUP BY month ORDER BY month
        """, nativeQuery = true)
    List<Object[]> getLoanStatsByMonth();
}
```

### Custom Repository Fragment Pattern

```java
// Step 1: define the custom interface
public interface LoanRepositoryCustom {
    List<Loan> findByDynamicFilters(LoanSearchCriteria criteria);
}

// Step 2: main repository extends both
public interface LoanRepository
    extends JpaRepository<Loan, Long>, LoanRepositoryCustom {
    // derived + @Query methods here
}

// Step 3: implementation class — must end in "Impl"
@Repository
public class LoanRepositoryImpl implements LoanRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Loan> findByDynamicFilters(LoanSearchCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Loan> query = cb.createQuery(Loan.class);
        Root<Loan> root = query.from(Loan.class);

        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getMemberId() != null)
            predicates.add(cb.equal(root.get("member").get("id"), criteria.getMemberId()));
        if (criteria.getStatus() != null)
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
        if (criteria.getDueBefore() != null)
            predicates.add(cb.lessThan(root.get("dueDate"), criteria.getDueBefore()));

        query.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(query).getResultList();
    }
}
```

### Unit of Work with @Transactional

```java
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final InventoryRepository inventoryRepository;
    private final NotificationRepository notificationRepository;

    // Unit of Work: all three repositories participate in ONE transaction
    @Transactional
    public void checkOutItem(Long memberId, Long itemId) {
        // 1. Validate item is available
        Item item = inventoryRepository.findAvailableItem(itemId)
            .orElseThrow(() -> new ItemNotAvailableException(itemId));

        // 2. Create loan
        Loan loan = new Loan(memberId, itemId, LocalDate.now(),
                             LocalDate.now().plusDays(item.getDefaultLoanDays()));
        loanRepository.save(loan);

        // 3. Decrement stock
        inventoryRepository.decrementStock(itemId);

        // 4. Queue notification
        notificationRepository.save(new Notification(memberId, "Item checked out", loan.getId()));

        // If ANY step throws RuntimeException → all 4 operations roll back
        // All 4 succeed → single commit
    }

    @Transactional(readOnly = true)
    public List<LoanSummaryDto> getOverdueLoans() {
        return loanRepository.findOverdueWithDetails(LocalDate.now())
            .stream()
            .map(LoanSummaryDto::from)
            .toList();
    }
}
```

### Testing Repositories

```java
// Slice test — fast, loads only JPA layer, uses H2 in-memory
@DataJpaTest
class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void findOverdueWithDetails_returnsOnlyUnreturnedPastDueDate() {
        Member member = em.persistAndFlush(new Member("Alice"));
        Item item = em.persistAndFlush(new Item("SQL Mastery"));
        em.persistAndFlush(new Loan(member, item, LocalDate.now().minusDays(5),
                                    LocalDate.now().minusDays(1), null));  // overdue
        em.persistAndFlush(new Loan(member, item, LocalDate.now().minusDays(3),
                                    LocalDate.now().plusDays(7), null));   // not overdue

        List<Loan> overdue = loanRepository.findOverdueWithDetails(LocalDate.now());

        assertThat(overdue).hasSize(1);
        assertThat(overdue.get(0).getMember().getName()).isEqualTo("Alice");
    }
}

// For PostgreSQL-specific queries — use Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)  // don't replace with H2
@Testcontainers
class LoanRepositoryPostgresTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    // ... same tests but against real PostgreSQL
}
```

## Why It Matters

The repository pattern is how applications keep persistence from leaking into business logic — a boundary you'll meet in nearly every well-structured codebase:

- Swapping or upgrading the data layer (or mocking it in tests) is only possible when access goes through a defined interface
- Spring Data's derived queries make simple repositories nearly free, while custom methods keep complex SQL in one auditable place
- Without the boundary, queries scatter through services and controllers until no one can say what touches a table

Repositories are SOLID principles applied to data access: one place, one responsibility, easily substituted. That's why interviewers and architects keep asking about them.

## Common Mistakes

- **Service directly using `EntityManager`**: Bypasses the repository pattern; service is now coupled to JPA. Extract persistence calls to a repository interface — even if it's just a thin wrapper today, it gives you the seam for testing and future changes.
- **Business logic inside repositories**: Repositories should only retrieve and persist data. Validations, calculations, and workflow decisions belong in the service layer. A repository method named `checkOutAndNotifyAndDeductInventory` is doing too much.
- **One repository per database table**: The repository pattern is domain-oriented, not table-oriented. An `OrderRepository` might query `orders`, `order_lines`, and `products` in one query — that's appropriate. Avoid splitting by table rather than by domain aggregate.
- **No transaction on multi-repository service methods**: Operations spanning multiple repositories need `@Transactional` at the service level. Without it, partial failures leave data inconsistent.

## Mental Model

A repository is a domain-facing data store — from the service's perspective, it's like an in-memory collection with query capabilities. The service says "give me all overdue loans" without specifying SQL, indexes, or joins. The repository decides how to retrieve that data efficiently. This is the same mental model as a library catalogue: you ask the librarian for "all books on SQL published after 2020"; you don't specify which filing cabinet, which index card system, or which floor. The librarian (repository) knows the storage implementation; you know what you need.

## Mini Summary

- ✔ Repository pattern: domain-facing interface hides persistence details from services
- ✔ Spring Data JPA: extend JpaRepository for free CRUD + derived queries + @Query
- ✔ Custom fragments: separate interface + `RepositoryImpl` class for complex queries
- ✔ Unit of Work: `@Transactional` on service methods coordinates multiple repositories
- ✔ `@DataJpaTest`: fast slice test for repository queries — uses H2 in-memory by default
- ✔ Testcontainers: production-faithful PostgreSQL test environment for dialect-specific queries

# Guided Practice Quest

Work through the guided steps to refactor a service that uses `EntityManager` directly into one that uses a proper `LoanRepository` interface, write a `@DataJpaTest` for the overdue loans query, and identify the correct `@Transactional` boundary for a multi-step checkout operation.

# Solo Practice Quest

Design the full data access layer for the Archive system. Tasks: (1) Define a `LoanRepository` interface with at least six methods covering the most common access patterns; (2) Write a `LoanRepositoryImpl` fragment for a dynamic search (filter by member name, item category, status, and date range — any combination); (3) Write a `LoanService` with proper `@Transactional` boundaries for: checkout, return, bulk overdue marking, and monthly statistics report; (4) Write three `@DataJpaTest` tests for your most complex repository queries; (5) Identify two queries in your system that require Testcontainers (not H2) and explain why; (6) Draw the dependency diagram showing how controllers, services, and repositories relate — and explain why the dependency arrows always point toward the repository interfaces, not implementations.

# Integration

**Mathematics**: Repository testing is an application of the test oracle problem in software testing theory. A test oracle is a mechanism that determines whether a test passed or failed — i.e., what the correct output is for a given input. For repository tests, the oracle is: given these rows inserted via `TestEntityManager`, does the query return exactly these results? The challenge is combinatorial: for N filter parameters each with K possible values, the test space is K^N. Equivalence partitioning (grouping inputs that should produce the same output) reduces this: null/non-null, valid/invalid, boundary values. A well-designed test suite tests boundary conditions — the query for `dueDate < today` is tested with loans due yesterday (included), today (excluded), and tomorrow (excluded). This aligns with formal verification techniques: the query's correctness is proven by boundary cases, not exhaustive enumeration.

**Sciences (Library Science — Cataloguing Systems)**: The Repository pattern mirrors the physical organisation of a reference library. A reference desk (the service) accepts patron queries — "I need all papers on distributed consensus published between 2015 and 2020." The reference desk does not know whether the answer is in the stacks, on microfilm, or in a digital archive — it delegates to the catalogue system (the repository). The catalogue system knows the storage topology and retrieval strategy. When the library migrates from card catalogues to a digital system, the reference desk's procedures don't change — only the catalogue system does. This is precisely the benefit of the Repository pattern: the business logic (reference desk) is insulated from infrastructure changes (catalogue technology). The Dewey Decimal System itself is an abstraction over physical shelf location — a query interface hiding storage details.

# Lore Conclusion

"The service no longer imports EntityManager," the Junior Engineer reported. "All persistence calls are through `LoanRepository`. The checkout method is `@Transactional` — I tested a failure midway through and all four operations rolled back." The Senior Archivist reviewed the test suite. "Three repository tests, all using `@DataJpaTest`, running in 800 milliseconds. The Testcontainers test for the monthly statistics query with `DATE_TRUNC` is separate — it's slow but tests the production dialect." She closed the file. "You've completed Module 5. Application data access: JDBC for the foundation, ORM concepts for the mapping model, JPA and Hibernate for the implementation, repositories for the architecture. The Archive's data layer is clean." She looked ahead. "Next: the data shifts from operational — one record at a time — to analytical — millions of records, aggregated. Data Warehousing Foundations."

---
