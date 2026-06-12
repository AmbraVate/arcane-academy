---
id: de-jun-m8-03
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m8
moduleTitle: "Module 8: Database Testing"
moduleGlyph: "🧪"
moduleSortOrder: 8
topicSlug: test_data_management
topicTitle: "Test Data Management"
topicSortOrder: 3
lesson: test_data_management
title: "Test Data Management"
sortOrder: 3
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m8-02]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains why using production data in test environments is problematic
    - Describes test data builders and why they improve test maintainability
    - Explains data masking and anonymisation for test environments
    - Identifies test data strategies (minimal fixtures, builder pattern, factories)
    - Describes referential integrity challenges when seeding test data
  keywords: [test data, fixture, builder pattern, factory, data masking, anonymisation, seed data, referential integrity, synthetic data, minimal fixture, GDPR, test isolation, Faker, production data]
  modelAnswer: |
    Production data in test environments is a GDPR violation (PII in non-production environments without proper safeguarding) and creates unpredictable test state (tests depend on real data that changes). Test data must be: isolated (test-specific, not shared state), minimal (only what the test needs), deterministic (same data every run), and GDPR-safe (no real PII). Test data builders: Builder pattern or factory classes that create valid entity objects with sensible defaults, allowing tests to override only the fields they care about. Data masking: replace real PII in a copy of production data (email → fake email, name → fake name) to create realistic volume without real PII. Referential integrity ordering: when seeding test data, insert parents before children (categories → items → loans), or use DEFER/disable constraints during bulk seed operations.
guidedSteps:
  - id: de-jun-m8-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A team copies the production database to their test environment for "realistic testing". What problems does this create?
    inputConfig:
      options:
        - "Only performance: production data is too large for a test database"
        - "GDPR violation (real PII in test environment without safeguards), test non-determinism (tests depend on real data that changes), and risk of accidental production data modification"
        - "Schema incompatibility: production and test schemas are always different"
        - "Test performance: queries on production data volumes are too slow"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["GDPR violation (real PII in test environment without safeguards), test non-determinism (tests depend on real data that changes), and risk of accidental production data modification"]
      rejectedFeedback: "Using production data in test environments creates three categories of problems: (1) Privacy violation: real member names, emails, addresses, and loan history in the test database. Test environments typically have weaker access controls — more developers can access them. Under GDPR, personal data must be protected appropriately in all processing environments. (2) Test non-determinism: tests that assert 'there are 3 overdue loans' fail when new loans are created in production, or pass incorrectly when loans are returned. Tests must control their own data. (3) Accidental data modification risk: a developer running integration tests against a misconfigured environment that points to production can destroy or corrupt data. Test environments must use synthetic or masked data, with no path to production data."
    hint: "Think about legal compliance (GDPR), test reliability, and the risk of accidental production impact."
    reflectionPrompt: "What is the minimum viable approach if your team genuinely needs production-volume data for performance testing?"
  - id: de-jun-m8-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A test data pattern where a class provides a default valid entity with sensible values, allowing tests to override only the fields relevant to their specific scenario, is called the ________ pattern.
    inputConfig:
      placeholder: "builder"
    markingRule:
      matchMode: CONTAINS
      accepted: [builder, "builder pattern", "test builder", factory, "object mother", "test factory", "factory method"]
      rejectedFeedback: "The Builder pattern (also called Test Data Builder or Object Mother in testing contexts) creates valid entity instances with default values, allowing callers to override specific fields. Benefits: (1) Readability — test code focuses on what matters: LoanBuilder.defaults().withMemberId(42).overdue(7).build() clearly shows a 7-day overdue loan for member 42. (2) Maintainability — when the Loan constructor adds a new required field, you update one builder class, not 50 test methods. (3) Validity — the builder always produces a valid entity (passes all constraints) by default. Tests that don't care about a field (like member name) don't need to specify it. Framework equivalents: Lombok @Builder for Java entities (not for test builders — test builders use different defaults), Instancio (auto-generates values for all fields), Datafaker (generates realistic fake data)."
    hint: "This pattern creates objects with sensible defaults and allows you to customise only the fields that matter for your test."
    reflectionPrompt: "What makes a test data builder different from just using the entity's constructor directly?"
  - id: de-jun-m8-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain data masking as an approach for creating test data. What does it do to production data, and what compliance problem does it solve?
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [production, PII, mask, replace, fake, synthetic, email, name, GDPR, anonymise, realistic, volume, copy]
      rejectedFeedback: "Data masking creates a test dataset from production data by replacing sensitive PII with realistic but fake values. Process: (1) Take a copy of the production database schema and data. (2) Replace real names with generated names (John Smith → Alice Selvaris), emails with fake emails (real@company.com → alice.selvaris@example.com), phone numbers with random valid-format phones, addresses with synthetic addresses. (3) Preserve the data structure, relationships, and volume — the masked dataset has the same number of rows, the same distribution of loan durations, the same membership tier distribution. Compliance benefit: the masked dataset contains no real PII — GDPR does not apply to anonymised data. The test environment can be used without privacy controls for development and testing. Tools: Faker (Java, Python), Mimesis, Presidio, custom SQL scripts. Key requirement: masking must be irreversible — you must not be able to recover the original value from the masked value."
    hint: "Masking preserves the structure and volume of production data while replacing the identifying details with fake equivalents."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Minimal fixture principle in test data management means:"
    options:
      - "Use the smallest possible database for testing"
      - "Each test inserts only the data necessary for that specific test — no shared global fixtures that all tests depend on"
      - "Minimise the number of test methods in each test class"
      - "Use only one test database for all environments"
    correctIndex: 1
    feedback: "Minimal fixtures: each test creates exactly the data it needs — no more. Avoid large shared seed scripts that all tests depend on. Problems with large shared fixtures: (1) Tests become coupled to each other — adding data for test A breaks test B that assumes specific counts. (2) Hard to understand what each test depends on — the test reads 'findOverdueLoans returns 3' but you must read the entire seed script to understand why. (3) Fragile — any change to the seed script can break many tests. Minimal fixture approach: each test creates 2-4 rows that directly test the behaviour. The test is self-documenting: insert 1 overdue loan, 1 non-overdue loan, assert result contains only the overdue one. This is more verbose but dramatically more maintainable."
  - type: MULTIPLE_CHOICE
    question: "When inserting test data with referential integrity constraints (members → loans → payments), the insertion order must be:"
    options:
      - "Alphabetical by table name"
      - "Parents before children: members first, then loans referencing members, then payments referencing loans"
      - "Largest table first for performance"
      - "The order doesn't matter — FK constraints are deferred automatically in test environments"
    correctIndex: 1
    feedback: "Referential integrity requires parent records to exist before child records reference them. Insertion order: (1) Reference tables first: membership_tiers, categories, item_formats. (2) Independent entities: members, items. (3) Dependent entities: loans (depends on members AND items). (4) Further dependencies: payments, reservations (depend on loans). Violations: INSERT INTO loans (member_id=42, ...) before INSERT INTO members (id=42, ...) → FK violation. Solutions: (1) Follow the correct insertion order in @BeforeEach or seed scripts. (2) Use DEFER constraints (SET CONSTRAINTS ALL DEFERRED within transaction) for complex circular dependencies. (3) Use TestEntityManager.persistAndFlush() which immediately makes records visible. For TRUNCATE during cleanup: reverse the order (children first) or use TRUNCATE ... CASCADE to let the database handle FK cascades."
retrieval:
  recall: "Design a LoanTestBuilder class in Java: what default values it provides, what fluent methods it exposes (withMemberId, overdueDays, returned), and how a test would use it to create three different loan scenarios."
  explain: "Explain why synthetic data (generated by builders/factories) is preferable to anonymised production data for most integration tests, despite anonymised data having more realistic distribution."
  mistakeId:
    code: |
      @DataJpaTest
      class MemberRepositoryTest {
          
          @BeforeAll  // runs once before all tests in this class
          static void insertSharedFixtures(@Autowired TestEntityManager em) {
              em.persist(new Member(1L, "Alice", "alice@test.com", "Standard"));
              em.persist(new Member(2L, "Bob", "bob@test.com", "Premium"));
              em.persist(new Member(3L, "Charlie", "charlie@test.com", "Standard"));
              em.flush();
          }
          
          @Test
          void findStandardMembers_returnsTwo() {
              List<Member> standard = memberRepository.findByMembershipTier("Standard");
              assertThat(standard).hasSize(2);   // expects exactly 2
          }
          
          @Test
          void findPremiumMembers_returnsOne() {
              List<Member> premium = memberRepository.findByMembershipTier("Premium");
              assertThat(premium).hasSize(1);    // expects exactly 1
          }
      }
    answer: "Two problems: (1) @BeforeAll with @DataJpaTest: each test in @DataJpaTest runs in its own transaction that is rolled back. @BeforeAll runs outside any test transaction — the data may not be visible to tests or may be committed to the shared in-memory H2 database and persist across test classes unexpectedly. Use @BeforeEach instead to ensure data is inserted in the test's own transaction. (2) Asserting by count: assertThat(standard).hasSize(2) is fragile. If any other test in the class or suite inserts a Standard member and it leaks, this test fails. Better: give the members distinct IDs or emails, insert them in @BeforeEach of each test method, and assert by member ID. Or use @BeforeEach to insert only the data this test needs, and assert on the specific IDs returned rather than the count. The minimal fixture principle: each test creates its own data and cleans up (via transaction rollback) independently."
---

# Hook

Bad test data is the silent killer of test suites: tests that depend on shared state fail randomly when another test changes the data, PII in test environments creates legal liability, and tests that work only because the database happened to be in a specific state give false confidence. Test data management is the discipline of making test data reliable, isolated, compliant, and maintainable.

# Lore Introduction

"The CI build failed on Tuesday," the Junior Engineer reported. "Test 'findStandardMembers returns 2' started returning 3. No one changed that test." The Senior Archivist reviewed the test run. "Another test in the suite is inserting a Standard member and not cleaning it up. The shared fixture approach creates invisible dependencies between tests." The Junior looked at the test database. "And there's a bigger problem — someone copied the production database to the test environment three months ago for 'realistic data'. Six thousand real member records with real emails." The Senior Archivist set down her notes. "That is a GDPR violation. We will address the legal risk today and the test isolation problem today. Test data management: not glamorous, but the foundation of a trustworthy test suite."

# Core Learning

## Concept Introduction

### Test Data Builder Pattern

```java
// Test data builder: sensible defaults, fluent override API
public class LoanBuilder {

    private Long memberId = 1L;
    private Long itemId = 1L;
    private LocalDate loanDate = LocalDate.now().minusDays(14);
    private LocalDate dueDate = LocalDate.now().plusDays(7);  // not overdue
    private LocalDate returnDate = null;
    private String status = "ACTIVE";
    private BigDecimal fineAmount = BigDecimal.ZERO;

    public static LoanBuilder defaults() {
        return new LoanBuilder();
    }

    public LoanBuilder forMember(Long memberId) {
        this.memberId = memberId;
        return this;
    }

    public LoanBuilder forItem(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    public LoanBuilder overdueDays(int days) {
        this.dueDate = LocalDate.now().minusDays(days);
        this.loanDate = dueDate.minusDays(14);
        this.status = "OVERDUE";
        return this;
    }

    public LoanBuilder returned() {
        this.returnDate = LocalDate.now().minusDays(1);
        this.status = "RETURNED";
        return this;
    }

    public LoanBuilder withFine(BigDecimal amount) {
        this.fineAmount = amount;
        return this;
    }

    public Loan build() {
        return new Loan(memberId, itemId, loanDate, dueDate,
                        returnDate, status, fineAmount);
    }
}

// Test usage:
@Test
void findOverdue_excludesReturnedLoans() {
    Long memberId = persistMember();
    Long itemId = persistItem();

    persistLoan(LoanBuilder.defaults().forMember(memberId).forItem(itemId)
        .overdueDays(5).build());                            // overdue
    persistLoan(LoanBuilder.defaults().forMember(memberId).forItem(itemId)
        .overdueDays(5).returned().build());                 // overdue but returned
    persistLoan(LoanBuilder.defaults().forMember(memberId).forItem(itemId)
        .build());                                            // active, not overdue

    List<Loan> result = loanRepository.findOverdueWithDetails(LocalDate.now());

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getStatus()).isEqualTo("OVERDUE");
}
```

### Synthetic Data Generation

```java
// Datafaker: generate realistic-looking fake data (not real PII)
import net.datafaker.Faker;

@Component
public class MemberTestDataFactory {

    private final Faker faker = new Faker();
    private final MemberRepository memberRepository;

    public Member createMember(String tier) {
        Member member = new Member();
        // Faker generates realistic fake data — not real PII
        member.setFullName(faker.name().fullName());
        member.setEmail(faker.internet().emailAddress());
        member.setPhone(faker.phoneNumber().phoneNumber());
        member.setCity(faker.address().city());
        member.setMembershipTier(tier);
        member.setJoinDate(LocalDate.now().minusDays(faker.number().numberBetween(1, 1000)));
        return memberRepository.save(member);
    }

    public List<Member> createMembers(int count, String tier) {
        return IntStream.range(0, count)
            .mapToObj(i -> createMember(tier))
            .toList();
    }
}
```

### Data Masking for Staging Environments

```sql
-- Create a masked copy of production data for staging/performance testing
-- Run this against a production backup in an isolated environment:

-- Step 1: Copy schema + data to staging
-- Step 2: Mask PII columns:

UPDATE members_staging
SET
    full_name  = 'Masked_' || id::TEXT,
    email      = 'user_' || id::TEXT || '@masked.example.com',
    phone      = '+1-555-' || LPAD((id % 9000 + 1000)::TEXT, 4, '0'),
    address    = FLOOR(RANDOM() * 999 + 1)::TEXT || ' Test Street',
    city       = CASE (id % 5)
        WHEN 0 THEN 'London' WHEN 1 THEN 'Manchester'
        WHEN 2 THEN 'Bristol' WHEN 3 THEN 'Edinburgh' ELSE 'Cardiff'
    END
-- Keep: membership_tier, join_date, loan_count — non-identifying
-- Mask: all direct PII fields

-- Verify masking completeness:
SELECT COUNT(*) FROM members_staging
WHERE email LIKE '%@masked.example.com';  -- should equal total row count
```

### Referential Integrity in Test Data Setup

```java
@BeforeEach
void setUpTestData() {
    // Insert in dependency order: parents before children

    // 1. Reference data (no dependencies)
    MembershipTier standard = em.persist(new MembershipTier("Standard", 3));
    MembershipTier premium = em.persist(new MembershipTier("Premium", 10));
    Category tech = em.persist(new Category("Technology"));

    // 2. Independent entities (depend on reference data)
    Member alice = em.persist(new Member("Alice Test", "alice@test.invalid", standard));
    Item sqlBook = em.persist(new Item("Test SQL Book", "TEST-ISBN", tech));

    // 3. Dependent entities
    this.overdueLoan = em.persist(
        LoanBuilder.defaults().forMember(alice).forItem(sqlBook).overdueDays(5).build());

    em.flush();  // ensure all FK relationships are written to DB
}

@AfterEach
void tearDown() {
    // With @DataJpaTest, the transaction rolls back automatically — no manual cleanup
    // For @SpringBootTest without @Transactional:
    // loanRepository.deleteAll();
    // memberRepository.deleteAll();
    // itemRepository.deleteAll();
}
```

## Why It Matters

Test data is where quality and compliance collide — bad test data hides bugs, and copied production data leaks personal information:

- Tests pass on ten tidy rows and the feature dies on ten million messy ones; volume and mess are part of correctness
- Using real customer data in dev/test environments is a GDPR violation waiting for an incident — anonymisation or synthesis isn't optional
- Deterministic, repeatable seed data is what makes test failures mean something; "it depends which rows were there" means flaky

Every data team eventually builds test-data tooling. The good ones build it before the breach or the heisenbug, not after.

## Common Mistakes

- **Production data in test environments**: both a GDPR violation (PII without safeguards) and a test reliability issue (tests depend on real data that changes). Use synthetic data or masked copies.
- **Large shared fixtures in @BeforeAll**: shared state creates invisible test dependencies. Tests that rely on "there are exactly 3 Standard members" break when any other test inserts a Standard member. Use @BeforeEach minimal fixtures.
- **Builder defaults that violate constraints**: if the builder creates an entity with NULL in a NOT NULL column by default, every test that uses the builder will fail with a constraint violation. Builder defaults must produce a valid entity that passes all constraints.
- **Not seeding reference data**: tests that insert loans fail if membership_tier or category records don't exist. Always seed reference/lookup data first, or provide a shared reference data seed that all tests depend on.

## Mental Model

Test data management is like mise en place in cooking — the professional kitchen technique of preparing and organising all ingredients before cooking begins. Each dish (test) uses exactly the ingredients it needs, fresh and prepared, not leftovers from the last meal. The builder pattern is the mise en place toolkit: standard preparations (defaultMember, defaultLoan) that can be customised per dish (overdueMember, returnedLoan). Production data in the test kitchen is like using last night's food service customers as ingredients — inappropriate and unsafe.

## Mini Summary

- ✔ Never use production data in test environments — GDPR violation + non-deterministic tests
- ✔ Test data builder pattern: default valid entities with fluent override API
- ✔ Minimal fixtures: each test creates only the data it needs — no shared global state
- ✔ Synthetic data: Faker/Datafaker generates realistic-looking but fake PII
- ✔ Data masking: replace production PII with fake values for volume/performance testing
- ✔ Insertion order: parents before children to satisfy FK constraints
- ✔ @BeforeEach > @BeforeAll for test isolation; @Transactional rollback cleans up automatically

# Guided Practice Quest

Work through the guided steps to create a `LoanTestBuilder` with the five most common loan scenarios (active, overdue, returned, overdue with fine, first-time borrower), write a @BeforeEach fixture that correctly sequences the insertion order for the Archive test data, and replace a large shared fixture with minimal per-test fixtures.

# Solo Practice Quest

Design and implement a test data management strategy for the Archive system. Tasks: (1) Create builder classes for all four main entities: MemberBuilder, ItemBuilder, LoanBuilder, FineBuilder — each with at least five named scenario methods; (2) Create a TestDataFactory component that provides test-ready sets of data (e.g. createActiveLibraryScenario() returning a mix of members, items, and loans); (3) Write a data masking script for the Archive's members table — all five PII columns, verifiable completeness; (4) Design the reference data seed: what tables must always exist in the test database, and write the SQL or @Sql script to populate them; (5) Identify three tests that would be brittle if written with a shared @BeforeAll fixture and rewrite them using the minimal per-test fixture approach; (6) One performance test requires 100,000 loan records — design how to generate this volume quickly in the test environment without using production data.

# Integration

**Mathematics**: Test data generation can exploit combinatorial design theory to maximise coverage with minimal data. Pairwise testing (also called all-pairs or 2-way testing): instead of testing all combinations of N parameters (exponential), test all pairs of values for any two parameters (polynomial). For a loan scenario with parameters: status (4 values), member_tier (3 values), item_category (5 values), the full combination requires 4×3×5 = 60 test cases. Pairwise testing covers all pairs in approximately √(4×3×5) ≈ 8 test cases. Tools like PICT (Pairwise Independent Combinatorial Tool) generate minimal covering sets. In practice, this means a test data factory can generate a small set of carefully chosen scenarios that, together, exercise every pair of attribute combinations — covering the interaction bugs that most commonly cause failures while keeping the test suite small.

**Sciences (Cognitive Science — Cognitive Load in Testing)**: The principle of minimal fixtures directly addresses extrinsic cognitive load in test comprehension. Cognitive load theory (Sweller, 1988) distinguishes intrinsic load (inherent complexity of the subject), extraneous load (complexity added by poor presentation), and germane load (complexity that builds understanding). A test with a large shared fixture imposes extraneous load: to understand the test, the reader must also understand all 50 rows in the shared fixture, identify which ones affect this test, and hold all of that in working memory simultaneously. A minimal fixture test: 2-4 rows inserted in @BeforeEach, each serving a clear purpose for this specific test. The reader's working memory is freed to focus on the test logic (germane load) rather than fixture archaeology (extraneous load). This is why experienced test engineers consistently advocate for self-contained tests — it is empirically supported by cognitive research on comprehension.

# Lore Conclusion

"Production data removed from the test environment," the Junior Engineer reported. "Replaced with masked data from a recent backup — all PII fields substituted. The compliance risk is resolved." The Senior Archivist reviewed the test data strategy. "And the shared fixture problem?" The Junior showed the refactored tests. "LoanBuilder and MemberBuilder with five scenario methods each. Every test uses @BeforeEach with minimal fixtures — one to three rows inserted, tested, rolled back. The fragile assertHasSize(3) tests are gone — they assert on specific IDs now." The Senior Archivist reviewed the CI results. "No more random failures on Tuesday." The Junior confirmed. "The tests are now independent. Any test can run in any order." The Senior Archivist nodded. "One more topic: migration testing. Ensuring that schema changes do not break the database or the application when deployed."

---
