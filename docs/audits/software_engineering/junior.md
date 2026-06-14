# Audit — Software Engineering · Junior

**Auditor lens:** Engineering Manager / Staff Engineer conducting a hiring rubric review — "does this curriculum produce someone I can hire as a junior developer?"
**Tier mandate:** Completing this tier should produce a job-ready junior developer who can build REST APIs with a database, write unit and integration tests, apply OOP design patterns, use Git professionally, understand Big-O, and work within a CI/CD pipeline.
**Scope:** 49 lessons across 47 topics (most topics are single-lesson; inheritance and interfaces have 2 lessons each; solid_principles has 2 lessons).

---

## 1. Verdict at a glance

The Junior tier delivers on its core mandate in breadth but has a severe single-lesson-per-topic structural problem. 43 of 47 topics have exactly one lesson. For complex topics like Big O, SOLID principles, design patterns, and SQL/database work, one lesson is insufficient to build the competence needed for real employment. The content that exists is high quality — the SOLID lesson (se-jun-m1-07) is genuinely excellent, the TDD lesson (se-jun-m6-05) correctly centres Red-Green-Refactor with a worked Java example, and the git lesson (se-jun-m8-01) covers the three-area model and commit hygiene properly. The mini_project (se-jun-m9-01 "The Task Board API") is a strong integrative capstone — a Spring Boot CRUD REST API with ORM, custom exceptions, and testing. However, the journey from single-topic introduction to that capstone has too many gaps, particularly in SQL/database depth, Git workflow depth (branching strategies, merge vs rebase), and functional Java features (streams, lambdas) that are expected in any modern Java codebase. The curriculum also introduces too many design patterns (Strategy, Builder, Factory, Adapter, Observer, Singleton) as single-lesson concepts without reinforcing them through practice, which will not produce recognisable competence at interview.

**Scores:** Coverage 3/5 · Rigor/Depth 3/5 · Sequencing 4/5 · Practice quality 3/5

---

## 2. KEEP — strengths to preserve

- **se-jun-m1-07, se-jun-m1-08 / solid_principles** — Two lessons on SOLID is the right investment. The OCP exercise using `instanceof` chains → interface refactor is exactly the kind of hands-on violation-to-fix pattern that builds real understanding. The ISP guided question (Printer fat interface) is canonical.
- **se-jun-m6-05 / tdd** — `Test-Driven Development`: Red-Green-Refactor cycle taught correctly with a concrete `PasswordValidator` worked example showing failing test → minimal implementation → refactor. The reflection prompt ("a test that passes before any implementation tests nothing") is a genuine insight.
- **se-jun-m8-01 / git** — `Git`: Three-area model (working tree, staging, repo) is the right mental model. Conventional commits format (`feat:`, `fix:`, `refactor:`) is taught, which is a professional habit that distinguishes a job-ready developer. `.gitignore` examples are practical.
- **se-jun-m9-01 / mini_project** — `The Task Board API`: strong capstone. The rubric enforces ORM (no raw SQL in service layer), custom exceptions with structured JSON responses, SOLID principle applied and named, and git commit hygiene. This is a portfolio-worthy project brief.
- **se-jun-m6-05 / why_testing (se-jun-m6-01)** — Positioning the testing motivation lesson before the unit test mechanics is correct pedagogy. The sequence `why_testing → unit_tests → integration_tests → mocking → tdd` is well-ordered.
- **se-jun-m3-03 / try_with_resources** — Covering AutoCloseable, resource leaks, and multi-resource declarations with ordering semantics is solid and practically important.
- **se-jun-m2-06 / big_o** — The four complexity classes (O(1), O(n), O(n²), O(log n)) are introduced with code-first examples. The guided exercise asks the learner to classify four distinct patterns.

---

## 3. CHANGE — restructure / resequence

- **`branching` (se-jun-m8-02)** — This topic covers Git branching but appears after `git` (se-jun-m8-01) with no lesson treating merge vs rebase, pull request workflow, or branch naming conventions. In a professional team, branching strategy is as important as committing. **Recommendation:** Expand to 2-3 lessons covering: (1) feature branch workflow, (2) merge vs rebase trade-offs, (3) pull request etiquette and review process.
- **`patterns_intro` (se-jun-m7-01)** — Introduction to design patterns appears after six individual pattern topics in the sort order within the module. The intro should come first. **Recommendation:** Confirm `sortOrder: 1` is assigned to patterns_intro and the individual patterns follow it.
- **`composition` (se-jun-m1-04)** — Composition is placed before `polymorphism` (se-jun-m1-03) and `interfaces` (se-jun-m1-05) in the sort order within module 1. This is backwards: polymorphism and interfaces provide the vocabulary for "prefer composition over inheritance." **Recommendation:** Resequence to: inheritance → polymorphism → interfaces → composition → SOLID.
- **`singleton_pattern` (se-jun-m7-05)** — The Singleton is correctly taught but must include a prominent warning that it is now considered an anti-pattern in dependency-injection frameworks (Spring beans are effectively singletons by default via the IoC container). A learner who learns Singleton without this context will write Spring `@Component` classes that manually implement Singleton, demonstrating a fundamental misconception. **Recommendation:** Add a section: "Singleton in the Spring era."

---

## 4. UPDATE — depth / rigor / currency

- **`sql_basics` (se-jun-m5-01)** — A single SQL basics lesson is inadequate for job readiness. A junior developer needs fluency with SELECT/WHERE/ORDER BY, GROUP BY/HAVING, basic subqueries, and INNER/LEFT JOIN at minimum. The joins are in a separate `joins` topic (se-jun-m5-02) which is better than nothing, but the current depth (1 lesson each) is insufficient. Senior interviewers routinely test SQL. **Recommendation:** Expand to 3-4 lessons.
- **`big_o` (se-jun-m2-06)** — One lesson covers O(1)/O(n)/O(n²)/O(log n) but omits O(n log n) — which is the complexity of every efficient sort algorithm (mergesort, quicksort). Since `sorting` is a sibling topic, this is a notable gap. Also absent: space complexity. **Recommendation:** Add O(n log n) and introduce space vs time complexity distinction.
- **`refactoring` (se-jun-m8-04)** — One lesson on refactoring is thin for a topic this important. The lesson should cover: extract method, rename, extract class, replace conditional with polymorphism, and the critical discipline of test-first refactoring (never refactor without tests). **Recommendation:** Expand to 2 lessons.
- **`orms` (se-jun-m5-04)** — ORM fundamentals are covered but the N+1 problem — arguably the most critical ORM pitfall a junior developer will encounter in production — is not mentioned here (it appears later in the senior `database_optimisation` lesson). At junior tier, learners need at least an introduction to N+1 and JOIN FETCH as the counter-pattern. **Recommendation:** Add an N+1 section to this lesson.
- **`cicd` (se-jun-m8-06)** — The CI/CD lesson is good conceptually but omits practical GitHub Actions YAML syntax. A junior developer joining a team needs to be able to read and modify a basic `.github/workflows/main.yml`. At minimum, show an annotated example file. **Recommendation:** Add a worked YAML example with annotations.

---

## 5. REMOVE — cut or merge

- **`lists_advanced` (se-jun-m2-01)** and **`maps_advanced` (se-jun-m2-03)** — These cover functional-style collection operations (streams, lambdas, filter/map/reduce). They are taught in Java idiom but the learner has had no introduction to lambdas or functional programming as concepts. The lesson drops into lambda syntax without foundation. **Recommendation:** Either add a prerequisite "Introduction to Lambdas and Functional Interfaces" lesson, or restructure these lessons to build up from first principles. The topic names are fine; the content needs a prerequisite.
- **`observer_pattern` (se-jun-m7-06)** — The Observer pattern at junior tier is premature. It is primarily relevant in event-driven and GUI programming contexts that a junior developer won't encounter until well into their career. The cognitive slot is better used for depth in patterns they will use immediately (Factory, Builder, Strategy). **Recommendation:** Move to Senior tier or replace with a "Command Pattern" lesson which is more immediately applicable.

---

## 6. GAPS — missing canonical topics

| Topic | Why essential at this tier | Suggested placement |
|---|---|---|
| **Lambdas and functional interfaces** | Java 8+ lambdas are ubiquitous in modern Java. Every Spring Boot project uses them extensively (`@Service`, `Comparator.comparing()`, `Optional.map()`). Without this foundation, `lists_advanced` and `maps_advanced` are inaccessible. | New topic in Module 2, before lists_advanced |
| **Optional and null safety** | `NullPointerException` is the #1 cause of production bugs in Java. `Optional` is the idiomatic Java solution. Every senior engineer will ask about Optional in an interview. | New lesson in Module 3 (Exception Handling) |
| **Spring Boot basics** | The mini_project assumes Spring Boot knowledge (annotations, dependency injection via `@Autowired`, `@RestController`, `@Service`, `@Repository`) but none of the curriculum explicitly teaches Spring Boot. Learners are expected to use a framework they were never taught. | New topic in Module 4 (APIs), 3-4 lessons |
| **DTO pattern / request-response mapping** | The Task Board API requires structured JSON responses, but the curriculum has no explicit lesson on the DTO (Data Transfer Object) pattern — the standard Spring Boot idiom for separating domain model from API contract. | New lesson in Module 4 |
| **Streams API** | Java Streams are the modern Java idiom for collection transformation. Used in virtually every Spring Boot codebase. Currently implied by `lists_advanced` but not taught as a standalone concept. | New lesson, prerequisite for lists_advanced |
| **Git conflict resolution** | Merge conflicts are a daily reality for junior developers. The git topic covers the happy path but not conflict resolution, which is anxiety-inducing without guidance. | Expand git topic or add to branching |
| **Environment variables and configuration** | Junior developers cannot operate in a team without understanding `application.properties`, `application.yml`, and environment variable injection via `@Value`. Absent entirely. | New lesson in Module 4 or 8 |

---

## 7. PRACTICE & ASSESSMENT

The single-lesson-per-topic model reduces practice quality significantly. Many lessons have only 2-3 guided steps when the topic warrants 5-6 to build genuine fluency. The SOLID lesson (se-jun-m1-07) is the exception — 3 steps covering SRP, OCP, and ISP with actual code refactoring — and it is noticeably more rigorous than the average.

Specific concerns:
- **Design pattern lessons** (adapter, builder, factory, observer, singleton, strategy) all appear to be single-lesson with guided steps that test recognition more than application. A junior should be able to implement a pattern from a prompt, not just identify a violation. The practice level needs to move from "identify this pattern" to "implement this pattern for this scenario."
- **SQL and ORM topics** need SQL query writing exercises, not just conceptual questions. The ability to write a correct JOIN or GROUP BY under time pressure is tested at nearly every junior interview.
- **The mini_project rubric** is strong but there is no intermediate "checkpoint project" between the single-topic exercises and the full Task Board API. The cognitive jump is large. A mid-tier mini exercise (e.g., a service class with unit tests before the full API) would reduce attrition.
- **Code review exercises** (se-jun-m8-03) should include a sample PR diff for the learner to review in writing — currently the lesson teaches code review principles conceptually but does not simulate the actual activity.

---

## 8. Prioritized action list

1. **ADD** Spring Boot fundamentals topic (3-4 lessons: project structure, annotations, IoC container, REST controllers) — this is the biggest gap; the mini_project requires it implicitly.
2. **ADD** Lambdas and functional interfaces lesson as prerequisite to `lists_advanced` and `maps_advanced`.
3. **EXPAND** `sql_basics` and `joins` to 3-4 lessons total with query-writing practice exercises.
4. **ADD** `Optional` lesson to the exception handling module.
5. **EXPAND** `branching` to cover merge vs rebase, PR workflow, and conflict resolution.
6. **UPDATE** `big_o` to include O(n log n) and basic space complexity.
7. **UPDATE** `orms` to include N+1 query problem and JOIN FETCH mitigation.
8. **RESEQUENCE** Module 1 OOP topics to: inheritance → polymorphism → interfaces → composition → SOLID.
9. **ADD** DTO pattern lesson to Module 4.
10. **UPDATE** `singleton_pattern` to address the Spring IoC container context and why manual Singleton is an anti-pattern in framework-based applications.
