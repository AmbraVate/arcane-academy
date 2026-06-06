---
id: de-jun-m5-03
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m5
moduleTitle: "Module 5: Application Data Access"
moduleGlyph: "🔌"
moduleSortOrder: 5
topicSlug: jpa_hibernate
topicTitle: "JPA/Hibernate"
topicSortOrder: 3
lesson: jpa_hibernate
title: "JPA/Hibernate"
sortOrder: 3
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m5-02]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Distinguishes JPA (specification) from Hibernate (implementation)
    - Explains the EntityManager and persistence context lifecycle
    - Describes JPQL and its differences from SQL
    - Explains entity state transitions (transient, managed, detached, removed)
    - Identifies the LazyInitializationException and its cause
  keywords: [JPA, Hibernate, EntityManager, persistence context, JPQL, entity state, transient, managed, detached, removed, LazyInitializationException, open session in view, second-level cache, cascade]
  modelAnswer: |
    JPA (Jakarta Persistence API) is a specification — a set of interfaces and annotations. Hibernate is the dominant implementation of that specification. The EntityManager manages the persistence context: a transaction-scoped cache where loaded entities live. Entity states: Transient (new, no ID, not tracked), Managed (loaded or saved, tracked by persistence context), Detached (was managed, transaction ended), Removed (marked for DELETE). JPQL is object-oriented SQL: queries target entity class names and field names, not table/column names — the ORM translates to SQL. LazyInitializationException: accessing a lazy association outside a transaction (after the persistence context closed) — Hibernate cannot open a new session to load the proxy. Fix: load the association inside the transaction, or use JOIN FETCH.
guidedSteps:
  - id: de-jun-m5-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A Loan entity is loaded inside a @Transactional service method, returned to the controller, and the controller accesses loan.getMember().getName(). What happens?
    inputConfig:
      options:
        - "The member name is returned — Hibernate loads it transparently"
        - "LazyInitializationException — the persistence context closed when the transaction ended"
        - "A NullPointerException — detached entities lose their associations"
        - "The ORM opens a new session automatically to load the member"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["LazyInitializationException — the persistence context closed when the transaction ended"]
      rejectedFeedback: "When the @Transactional method returns, the transaction commits and the persistence context closes. The loan entity is now detached. The lazy member proxy cannot initialise — there is no open session to execute the SELECT. Hibernate throws LazyInitializationException: 'could not initialise proxy — no Session'. Fixes: (1) Load the member inside the transaction using JOIN FETCH. (2) Call Hibernate.initialise(loan.getMember()) before the transaction closes. (3) Use @Transactional on the controller (antipattern). (4) Open Session In View filter (broadly discouraged — causes one session per HTTP request, leaks connections). The correct fix is loading what you need inside the transaction boundary."
    hint: "When does the transaction end, and what happens to the persistence context at that point?"
    reflectionPrompt: "What is wrong with the Open Session In View pattern as a general solution to LazyInitializationException?"
  - id: de-jun-m5-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In JPQL, you query entity class names and field names — not table names. The JPQL query to find all loans for a member would be: SELECT l FROM ________ l WHERE l.member.id = :memberId
    inputConfig:
      placeholder: "Loan"
    markingRule:
      matchMode: CONTAINS
      accepted: [Loan, loan]
      rejectedFeedback: "JPQL (Jakarta Persistence Query Language) uses entity class names, not table names. If your entity class is named 'Loan' (with @Entity), the JPQL FROM clause is 'FROM Loan' — not 'FROM loans' (which is the table name). Field names in WHERE clauses are Java field names (l.member.id) not column names (l.member_id). The ORM translates JPQL to SQL automatically: FROM Loan → FROM loans, l.member.id → members.id (with a JOIN). This makes JPQL portable across different databases — change the dialect, the generated SQL changes, your JPQL stays the same. For database-specific features not supported in JPQL, use nativeQuery = true."
    hint: "JPQL targets the Java class name, not the SQL table name."
    reflectionPrompt: "What is one advantage of JPQL over native SQL for cross-database portability?"
  - id: de-jun-m5-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain what happens to an entity when you call entityManager.detach(loan) or when the transaction ends. What is the entity's state, and what can/cannot you do with it?
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [detach, detached, persistence context, tracked, session, lazy, exception, reattach, merge, save]
      rejectedFeedback: "A detached entity: was previously managed (tracked by the persistence context), but is no longer associated with an active session. After detachment, the entity: (1) Has its scalar fields populated — you can read id, name, status etc. (2) Has uninitialized lazy proxies for any associations not yet loaded — accessing them throws LazyInitializationException. (3) Is no longer tracked — changes to its fields are NOT persisted automatically. To reattach: call entityManager.merge(detachedEntity) or repository.save(detachedEntity) — this creates a new managed entity with the detached entity's state, and dirty checking resumes. Understanding the entity lifecycle (transient → managed → detached → removed) is essential to avoiding both missing-update bugs and unintended-update bugs."
    hint: "After the transaction ends, is the entity still tracked? What happens if you access a lazy association?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the difference between JPA and Hibernate?"
    options:
      - "JPA is the implementation, Hibernate is the specification it follows"
      - "JPA is a specification (set of interfaces/annotations), Hibernate is the most common implementation of that specification"
      - "JPA is used for SQL databases, Hibernate is used for NoSQL databases"
      - "JPA and Hibernate are identical — different names for the same framework"
    correctIndex: 1
    feedback: "JPA (Jakarta Persistence API, formerly Java Persistence API) is a specification defined by the Jakarta EE committee. It defines annotations (@Entity, @Id, @ManyToOne), interfaces (EntityManager, EntityManagerFactory), and JPQL. Hibernate is an independent ORM framework that predates JPA (2001) and became JPA's reference implementation. Other JPA implementations exist: EclipseLink (the RI), OpenJPA. Writing code against JPA interfaces (EntityManager, @Query with JPQL) makes your code portable across implementations. Hibernate-specific features (Hibernate criteria, @Formula, @Filter, @Cache) are not portable but offer additional power. In Spring Boot: the default JPA implementation is Hibernate, configured via spring.jpa.* properties."
  - type: MULTIPLE_CHOICE
    question: "The CascadeType.REMOVE annotation on a @OneToMany means:"
    options:
      - "The database uses ON DELETE CASCADE on the foreign key"
      - "When a parent entity is deleted via the EntityManager, the ORM also deletes the child entities"
      - "Child entities are automatically removed when they are dereferenced from the parent collection"
      - "The removal is cascaded to all related entities across the entire object graph"
    correctIndex: 1
    feedback: "CascadeType.REMOVE (or CascadeType.ALL which includes it) means: when you call entityManager.remove(parent) or repository.delete(parent), the EntityManager also calls remove() on each child entity in the annotated collection. This is ORM-level cascading — separate from database-level ON DELETE CASCADE. The ORM issues individual DELETE statements per child (not a set-based DELETE). For large collections, this is slower than database CASCADE. Key risk: CascadeType.ALL on a @ManyToMany causes deletion of the shared entity when you remove from one side's collection. Use cascade carefully — typically only on OneToMany relationships where the parent owns the child's lifecycle."
retrieval:
  recall: "Describe the four entity states in JPA (transient, managed, detached, removed). For each: when does an entity enter this state, and what happens to changes you make to the entity?"
  explain: "Explain why LazyInitializationException occurs, what conditions trigger it, and describe the correct fix for a Spring MVC controller that returns entity data to a REST endpoint."
  mistakeId:
    code: |
      @Service
      public class LoanService {
          public LoanDto getLoanDetails(Long id) {
              Loan loan = loanRepository.findById(id).orElseThrow();
              // No @Transactional — transaction auto-committed
              return new LoanDto(
                  loan.getId(),
                  loan.getMember().getName(),    // LazyInitializationException
                  loan.getItem().getTitle()      // LazyInitializationException
              );
          }
      }
    answer: "The getLoanDetails method has no @Transactional annotation. Spring Data repositories use their own transaction per method — findById opens a transaction, loads the Loan, and commits immediately. When getLoanDetails then accesses loan.getMember() and loan.getItem(), those are lazy proxies but there is no open session. Hibernate throws LazyInitializationException. Fixes: (1) Add @Transactional to getLoanDetails — keeps the persistence context open for the entire method. (2) Use JOIN FETCH in the repository query: @Query(\"SELECT l FROM Loan l JOIN FETCH l.member JOIN FETCH l.item WHERE l.id = :id\") — loads all needed data in one query. (3) Use a DTO projection in JPQL: @Query(\"SELECT new com.example.LoanDto(l.id, m.name, i.title) FROM Loan l JOIN l.member m JOIN l.item i WHERE l.id = :id\") — avoids loading entities at all."
---

# Hook

JPA is the contract; Hibernate is the contractor. Every Java data engineer works with this stack — understanding the specification versus the implementation, the entity lifecycle, and the session boundary is the difference between code that works and code that randomly throws `LazyInitializationException` in production.

# Lore Introduction

"The controller is throwing `LazyInitializationException`," the Junior Engineer reported, pasting the stack trace. The Senior Archivist read it. "Accessing a lazy proxy after the session closed. Where is your `@Transactional`?" The Junior pointed to the repository. "The repository has it." The Senior Archivist shook her head. "Each repository method opens and commits its own transaction. By the time you access `loan.getMember()` in the service, the transaction is gone — the proxy cannot initialise." The Junior nodded slowly. "So the persistence context lifecycle is tied to the transaction?" The Senior Archivist stood. "Exactly. JPA defines the rules. Hibernate enforces them. Learn the entity lifecycle — transient, managed, detached, removed — and you will understand why the framework does what it does."

# Core Learning

## Concept Introduction

### JPA vs Hibernate

```
JPA (Jakarta Persistence API)          Hibernate
────────────────────────────────────────────────────────────
Specification (interfaces, annots)     Implementation
Defines: @Entity, @Id, @ManyToOne     Implements: SessionFactory,
         EntityManager, JPQL                      Session, Criteria API
Portable across implementations        Hibernate-specific extensions
Standard annotations always usable     @Formula, @Filter, @Cache

Spring Boot dependency:
  spring-boot-starter-data-jpa
  → pulls in Hibernate as the JPA provider
  → configures EntityManagerFactory, TransactionManager
```

### The Persistence Context and Entity States

```
                  new MyEntity()
                       │
                  TRANSIENT ← no ID, not tracked
                       │
          entityManager.persist(e)
          or repository.save(new e)
                       │
                   MANAGED ← tracked, dirty-checking active
                  /       \
    tx commits /           \ entityManager.detach(e) or tx ends
              /             \
         REMOVED          DETACHED ← has ID, not tracked
            │
  entityManager.remove(e)
  → DELETE on flush

State transitions:
  Transient  → Managed:   persist(e) or save(e)
  Managed    → Detached:  detach(e), clear(), transaction end
  Managed    → Removed:   remove(e) or delete(e)
  Detached   → Managed:   merge(e) or save(detached) → new managed copy
  Removed    → Managed:   persist(removed entity) — re-inserts
```

### JPQL — Object-Oriented Queries

```java
// JPQL uses entity class names and field names (not table/column names)

// SQL equivalent: SELECT * FROM loans WHERE due_date < CURRENT_DATE
@Query("SELECT l FROM Loan l WHERE l.dueDate < CURRENT_DATE AND l.returnDate IS NULL")
List<Loan> findOverdueLoans();

// Joining — uses relationship field names, not FK column names
@Query("SELECT l FROM Loan l JOIN FETCH l.member m WHERE m.membershipTier = :tier")
List<Loan> findByMemberTier(@Param("tier") String tier);

// DTO projection — avoids loading full entities
@Query("SELECT new com.archive.dto.LoanSummary(l.id, m.name, i.title, l.dueDate) " +
       "FROM Loan l JOIN l.member m JOIN l.item i " +
       "WHERE l.dueDate < :today AND l.returnDate IS NULL")
List<LoanSummary> findOverdueSummaries(@Param("today") LocalDate today);

// Named parameters: :paramName with @Param
// Positional parameters: ?1, ?2 (positional, order-sensitive, avoid in complex queries)
```

### Entity Manager in Practice

```java
// Spring Data JPA abstracts the EntityManager — use JpaRepository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    // findById, save, delete, findAll are inherited
    
    // Derived queries from method names:
    List<Loan> findByMemberId(Long memberId);
    List<Loan> findByDueDateBeforeAndReturnDateIsNull(LocalDate date);
    Optional<Loan> findByMemberIdAndItemId(Long memberId, Long itemId);
    
    // Custom JPQL:
    @Query("SELECT l FROM Loan l JOIN FETCH l.member JOIN FETCH l.item WHERE l.id = :id")
    Optional<Loan> findByIdWithDetails(@Param("id") Long id);
    
    // Bulk modify:
    @Modifying
    @Transactional
    @Query("UPDATE Loan l SET l.status = 'OVERDUE' WHERE l.dueDate < :today AND l.returnDate IS NULL")
    int markOverdue(@Param("today") LocalDate today);
}

// Direct EntityManager access (when needed):
@Repository
public class CustomLoanRepositoryImpl {
    @PersistenceContext
    private EntityManager em;
    
    public List<Loan> findComplexQuery(/* params */) {
        return em.createQuery("SELECT l FROM Loan l ...", Loan.class)
                 .setParameter(...)
                 .getResultList();
    }
}
```

### Transaction and Session Boundaries

```java
// Correct: @Transactional keeps persistence context open for full method
@Service
public class LoanService {
    
    @Transactional(readOnly = true)
    public LoanDetailDto getLoanDetails(Long id) {
        // Persistence context open for entire method
        Loan loan = loanRepository.findByIdWithDetails(id).orElseThrow();
        // loan.getMember() and loan.getItem() already loaded via JOIN FETCH
        return LoanDetailDto.from(loan);
    }
    
    @Transactional
    public void returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        // Managed entity — changes tracked
        loan.setReturnDate(LocalDate.now());
        loan.setStatus("RETURNED");
        // No save() needed — dirty check generates UPDATE on commit
    }
}

// readOnly = true: tells Hibernate to skip dirty checking on flush
//   → performance optimization for read-only operations
//   → also signals to some databases to use read replica
```

### Second-Level Cache

```java
// First-level cache (persistence context): automatic, transaction-scoped
// Second-level cache: optional, session-factory-scoped (shared across transactions)

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  // Hibernate annotation
public class MembershipTier {
    @Id private Long id;
    private String name;
    private int maxLoans;
    // Rarely changes — good cache candidate
}

// application.properties:
// spring.jpa.properties.hibernate.cache.use_second_level_cache=true
// spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheCacheRegionFactory

// Good cache candidates: reference data (categories, tiers, settings)
// Poor cache candidates: frequently-updated transactional data (loans, balances)
```

## Common Mistakes

- **No `@Transactional` on the service method**: Spring Data repositories auto-commit per method. Accessing lazy associations after `findById` returns throws `LazyInitializationException`. Add `@Transactional` to the service method, or use JOIN FETCH to load everything needed.
- **`CascadeType.ALL` on `@ManyToMany`**: Deleting one side of a many-to-many deletes the shared entity, not just the relationship. Use `CascadeType.PERSIST` and `CascadeType.MERGE` for many-to-many; handle removal explicitly.
- **`@Transactional` on private methods**: Spring's AOP proxy cannot intercept private method calls — `@Transactional` on a private method does nothing. Always put `@Transactional` on public methods in Spring beans.
- **`@Modifying` without `@Transactional`**: Bulk `UPDATE`/`DELETE` JPQL queries require `@Modifying`. They also need a transaction — either on the repository method itself or the calling service.

## Mental Model

JPA is a legal contract specifying what an ORM must do. Hibernate is the law firm that enforces it. The persistence context is a transaction-scoped notepad — everything you touch is recorded, changes are replayed to the database at flush time, and when the transaction ends the notepad is discarded. Entities that leave the notepad (detached) carry their data but lose their "live connection" — reading their lazy fields is like trying to call a phone number that's been disconnected.

## Mini Summary

- ✔ JPA = specification; Hibernate = dominant implementation
- ✔ Persistence context: transaction-scoped, tracks all managed entities
- ✔ Entity states: transient → managed → detached / removed
- ✔ JPQL: targets class/field names, portable, translated to SQL by Hibernate
- ✔ `LazyInitializationException`: lazy proxy accessed after session closed — fix with JOIN FETCH or proper `@Transactional` boundary
- ✔ `@Transactional(readOnly = true)`: skips dirty checking — use for all read operations
- ✔ `@Modifying` + `@Transactional`: required for bulk UPDATE/DELETE JPQL

# Guided Practice Quest

Work through the guided steps to trace the entity lifecycle for a loan return operation, identify the missing `@Transactional` annotation causing `LazyInitializationException`, and rewrite a JPQL query to use a DTO projection instead of loading full entities.

# Solo Practice Quest

Design and implement the data access layer for the Archive's loan management feature. Tasks: (1) Write a `LoanRepository` with five Spring Data derived queries (by member, by item, by status, by date range, by member and status); (2) Write two JPQL queries: one with JOIN FETCH loading member and item, one as a DTO projection for the overdue loans report; (3) Write a `LoanService` with correct `@Transactional` boundaries — identify which methods need readOnly = true and which need full transactional write semantics; (4) Explain what happens step-by-step when `returnLoan(id)` is called: how the entity moves through states from findById to the commit; (5) Identify one scenario where you would use `entityManager` directly instead of the `JpaRepository`, and write the implementation.

# Integration

**Mathematics**: JPA's first-level cache (persistence context) is an implementation of the Identity Map pattern (Fowler, PoEAA). For a given persistence context and primary key, exactly one object instance exists — `em.find(Loan.class, 1L)` called twice returns the same Java object. This is a mathematical bijection: a one-to-one mapping between (entity class, primary key) pairs and object instances within a context. The cache lookup is O(1) — a HashMap from (type, id) → object. The second-level cache extends this to session-factory scope using a distributed cache (Ehcache, Caffeine, Redis). Cache hit rates follow the power law distribution — a small set of frequently-accessed entities (reference data: categories, tiers) accounts for most cache traffic, while the long tail of transactional entities (individual loans) is rarely reaccessed. This makes selective caching of reference data disproportionately effective.

**Sciences (Library Science — Catalogue Management)**: The Dewey Decimal System provides an analogy for JPA's entity mapping strategy. A physical book in a library has a single catalogue card (its persistent record — the database row) and a physical copy on the shelf (the Java object — the managed entity). The catalogue card is the authority: changes made to the card are permanent. A copy you've checked out (detached entity) reflects the card's state at checkout time — subsequent card updates are not visible. The JPA persistence context mirrors the library's reading room: books you take from the shelf to read are tracked; when you return them (transaction commit), the librarian notes any annotations you added (dirty checking). Books outside the reading room (detached entities) cannot be updated by the librarian — they must be returned (merged) to re-enter tracking.

# Lore Conclusion

"The `LazyInitializationException` is gone," the Junior Engineer reported. "JOIN FETCH in the repository query loads member and item together. The service method is `@Transactional(readOnly = true)` for reads, full `@Transactional` for writes." The Senior Archivist reviewed the code. "Correct. The persistence context now spans the full service method — not just the repository call. The entity transitions are clean: managed during the transaction, detached as the DTO leaves the boundary." She closed the session trace. "You understand the JPA contract. Hibernate enforces it predictably once you know the rules." She pulled up the next issue. "The final piece of application data access: repository patterns — structuring your data layer so business logic never knows whether data comes from a database, a cache, or a file."

---
