# Audit — Software Engineering · Lead

**Auditor lens:** VP Engineering / Distinguished Engineer reviewing a principal/staff-track curriculum for mastery, strategic thinking, and leadership readiness
**Tier mandate:** Completing this tier should produce an engineer capable of DDD-informed architecture, technical leadership, engineering organisational design, economic reasoning about software, and capstone-level system delivery — equivalent to a staff or principal engineer at a top-tier company.
**Scope:** 32 lessons across 28 topic folders (the `technical_leadership` folder contains 5 lessons that are near-duplicates of content in the standalone `mentoring`, `architecture_governance`, `technical_decisions`, `engineering_culture`, and `stakeholder_communication` folders — this structural duplication is the tier's primary defect).

---

## 1. Verdict at a glance

The Lead tier contains some of the most intellectually sophisticated content in the entire Arcane Academy curriculum. The DDD lesson (se-lea-m2-01) is exceptional — bounded contexts as boundaries of meaning (not module boundaries), aggregates as consistency boundaries (not groups of related objects), anti-corruption layer explained with precision. The CQRS lesson (se-lea-m2-03) correctly identifies when CQRS is overengineering vs genuine architectural value. The mentoring lesson (se-lea-m1-01) applies Vygotsky's ZPD, Socratic questioning, expert blind spot, and psychological safety with genuine depth. The multidisciplinary integration module (se_economics, se_mathematics, se_philosophy, se_psychology, se_systems_thinking) is a genuine differentiator — this content is absent from almost every other software engineering curriculum and is exactly what separates staff engineers from senior engineers. The capstone (se-lea-m7-01 "Systems Architect") is appropriately ambitious: a production-deployable system with architecture document, CI/CD, load testing, security, observability, and tradeoff analysis. The structural problem is a duplication of lesson content between the `technical_leadership` folder (5 files: se-lea-m1-01 through se-lea-m1-05) and the standalone topic folders (`mentoring`, `architecture_governance`, `technical_decisions`, `engineering_culture`, `stakeholder_communication`) — the same IDs (se-lea-m1-01 through se-lea-m1-05) appear in both locations with different `topicSlug` values and slightly different rubric/keyword sets. This creates navigation ambiguity and maintenance risk. The lead tier also has notable gaps in team-level engineering practice: no ADR (Architectural Decision Record) template lesson, no RFC (Request for Comments) process, no on-call / runbook culture, and no explicit treatment of the staff engineer's "glue work" that differs fundamentally from the senior engineer's scope.

**Scores:** Coverage 4/5 · Rigor/Depth 5/5 · Sequencing 4/5 · Practice quality 4/5

---

## 2. KEEP — strengths to preserve

- **se-lea-m2-01 / ddd** — `Domain-Driven Design`: exceptional. Ubiquitous language defined precisely (anti-pattern: `Manager`, `Handler`, `Processor` — concepts no domain expert recognises). Bounded contexts as boundaries of meaning is the correct framing that most DDD curricula miss. Aggregates as consistency boundaries with the Customer-in-Order aggregate exercise is the canonical teaching scenario.
- **se-lea-m2-03 / cqrs** — `CQRS`: accurately describes the write/read model separation, eventual consistency implications, the synergy with event sourcing, and — critically — when CQRS is overengineering. The Axon Framework reference is appropriate for the Java stack this platform targets.
- **se-lea-m1-01 / technical_leadership** (also `mentoring`) — `Mentoring Engineers`: the Socratic questioning vs leading questions distinction is precise ("Have you considered a cache here?" is leading — ego-driven, not mentee-driven). Expert blind spot and curse of knowledge named and applied to engineering mentoring. Psychological safety as a load-bearing prerequisite, not a nice-to-have. This is the quality of pedagogy the tier mandate requires.
- **se-lea-m5-02 / se_economics** — `SE + Economics`: Cost of Delay / CD3 metric (Reinertsen), real options theory applied to architectural deferral, incentive misalignment (dev teams vs ops teams → shared error budget as solution), build vs buy total cost of ownership. This content distinguishes a staff engineer who can influence executive decisions from one who cannot.
- **se-lea-m5-04 / se_mathematics** — `SE + Mathematics`: Little's Law applied to service capacity, Amdahl's Law for parallelisation limits, availability as probability calculation (series vs parallel system availability). Correct and practical. The type-system-as-formal-specification insight is sophisticated and accurate.
- **se-lea-m3-05 / sociotechnical** — Conway's Law stated and the Inverse Conway Manoeuvre as a deliberate tool for org design is the right framing. Team coupling → architecture coupling is the practical implication most engineers miss.
- **se-lea-m7-01 / capstone** — `Systems Architect`: the rubric requiring a tradeoff analysis document naming at least three architectural decisions with alternatives considered is exactly right. "The system is not perfect; the rationale acknowledges its limitations" is the mature framing that distinguishes architect-level thinking.
- **se-lea-m6-01 / mini_project** — `The Engineering Transformation`: requiring a DDD analysis, at least one ADR, a migration roadmap, a mentoring plan, and a stakeholder communication plan is the correct lead-tier integration. "You will write less code than any previous project. You will make more consequential decisions" is the right framing.

---

## 3. CHANGE — restructure / resequence

- **`technical_leadership` folder duplication** — The `technical_leadership` folder contains files se-lea-m1-01 through se-lea-m1-05 with `topicSlug: technical_leadership`. Standalone topic folders (`mentoring`, `architecture_governance`, `technical_decisions`, `engineering_culture`, `stakeholder_communication`) contain files with the same IDs but different `topicSlug` values and slightly different rubric/keyword content. This creates two conflicting canonical versions of the same lessons. **Recommendation:** Decide on one canonical location. If `technical_leadership` is a module grouping, the individual topic slugs should be the canonical versions and the `technical_leadership` versions should be removed. Alternatively, remove the standalone folders and treat `technical_leadership` as the single authoritative module for this content. The current state is a maintenance hazard.
- **`cqrs` (se-lea-m2-03)** has `prerequisites: [event_sourcing]` which is correct — CQRS and event sourcing are synergistic. However, `event_sourcing` (se-lea-m2-02) and `ddd` (se-lea-m2-01) have no stated prerequisites, which means a learner could reach CQRS without having covered DDD's aggregate concept. **Recommendation:** Add `prerequisites: [se-lea-m2-01]` to `event_sourcing` and `prerequisites: [se-lea-m2-02]` to `cqrs`.
- **`se_psychology` (se-lea-m5-01)** is positioned first in the multidisciplinary integration module despite being named as a topic rather than a framework. The module ordering (psychology → economics → philosophy → mathematics → systems_thinking) would be improved by starting with `se_systems_thinking` as the meta-framework for the module, then applying it through each lens. **Recommendation:** Resequence to: systems_thinking → mathematics → economics → psychology → philosophy.

---

## 4. UPDATE — depth / rigor / currency

- **`architecture_governance` (se-lea-m1-02)** — Architecture governance at lead tier should explicitly cover Architectural Decision Records (ADRs) — the MADR format, what belongs in context/decision/consequences, how to manage a living ADR library, and how to use ADRs as onboarding artefacts for new engineers. The mini_project rubric requires "at least one ADR in standard format" but there is no lesson that teaches the ADR format. **Recommendation:** Add an explicit ADR format and template lesson.
- **`technical_decisions` (se-lea-m1-03)** — Two-way-door vs one-way-door (reversible vs irreversible decisions) and DACI are covered. The lesson should also address the "disagree and commit" principle explicitly, the use of pre-mortems (assume the decision failed — why?) as a decision quality tool, and the specific challenge of decisions under uncertainty where data is unavailable or misleading. **Recommendation:** Add pre-mortem and disagree-and-commit to the lesson content.
- **`hexagonal_architecture` (se-lea-m2-04)** — Ports and Adapters (Hexagonal Architecture) should explicitly connect to the `dependency_injection` lesson from the Junior tier and Spring Boot's `@Configuration` / `@Bean` / `@Primary` patterns as the Java implementation idiom. A staff engineer at a Java shop should be able to draw the dependency graph of a hexagonally structured Spring Boot application. **Recommendation:** Add a worked hexagonal Spring Boot project structure as a concrete example.
- **`clean_architecture` (se-lea-m2-05)** — Clean Architecture (Uncle Bob) and Hexagonal Architecture are taught as separate lessons but should explicitly cross-reference and compare their differences and similarities. The current structure risks treating them as alternatives when they are complementary (Clean Architecture provides the layer model; Hexagonal provides the port/adapter boundary mechanism). **Recommendation:** Add a comparison section and state when to apply each.
- **`devops_maturity` (se-lea-m3-03)** — The DevOps maturity model should include DORA metrics (Deployment Frequency, Lead Time for Changes, Change Failure Rate, Time to Restore Service) as the canonical measurement framework. If these are absent, add them. These are now the industry standard metrics for engineering effectiveness and every senior leader is expected to track them.
- **`platform_engineering` (se-lea-m3-02)** — Platform engineering (internal developer platform, golden paths, self-service infrastructure) is a relatively new but rapidly mainstream concept. The lesson should reference Gartner's adoption curve for IDP, CNCF's platform engineering white paper, and Team Topologies (Skelton & Pais) as the foundational framework. If these are absent, add them.

---

## 5. REMOVE — cut or merge

- **One of the two versions of each "technical_leadership" lesson** — As described in section 3, the duplication between `technical_leadership/se-lea-m1-01` and `mentoring/se-lea-m1-01` (and similarly for se-lea-m1-02 through se-lea-m1-05) must be resolved. The content variants are close but not identical; the rubrics and keywords differ slightly. Until resolved, any content update must be made in two places, creating a divergence risk.
- **`learning_systems` (se-lea-m4-03)** — This topic covers how engineers build personal learning systems (spaced repetition, deliberate practice, etc.). While interesting, it partially duplicates the platform-level content in `teaching_programming` (se-lea-m4-01) and the meta-skills content in `se_psychology`. At Lead tier, this content is less prioritised than the missing ADR, RFC, and team-level knowledge management topics. **Recommendation:** Merge the most relevant content into `teaching_programming` and `se_psychology`; retire the standalone topic.

---

## 6. GAPS — missing canonical topics

| Topic | Why essential at this tier | Suggested placement |
|---|---|---|
| **Architectural Decision Records (ADRs) — format and practice** | The mini_project requires an ADR but no lesson teaches the format. The MADR format is a professional standard. | Expand `architecture_governance` or new standalone lesson |
| **RFC (Request for Comments) process** | Staff/lead engineers drive technical direction through written proposals. The RFC process (Google Design Doc, Amazon 6-pager, RFC format) is how tech decisions are socialised and challenged at scale. | New lesson in Module 1 or 3 |
| **Team Topologies** (stream-aligned, platform, enabling, complicated-subsystem teams) | Team Topologies (Skelton & Pais, 2019) is the dominant framework for org design in engineering-led companies. Referenced implicitly in sociotechnical but needs explicit treatment. | Expand `sociotechnical` or new lesson in Module 3 |
| **DORA metrics and engineering effectiveness measurement** | Deployment Frequency, Lead Time for Changes, Change Failure Rate, Time to Restore — these four metrics are the industry standard for measuring engineering performance. A lead engineer who cannot speak to DORA metrics cannot make the business case for engineering investment. | Expand `engineering_effectiveness` or `devops_maturity` |
| **Staff engineer archetypes** (Tech Lead, Architect, Solver, Right Hand) | Will Larson's staff engineer archetypes describe the different shapes of staff-level impact. A curriculum preparing staff engineers should name and examine these. | New lesson in Module 1 or a new "Staff Engineering" topic |
| **Risk management in technical projects** | Lead engineers own technical risk. Risk identification matrices, dependency mapping, mitigation strategies, and technical due diligence processes are absent. | New lesson in Module 3 (Engineering Strategy) |
| **Incident command system and on-call culture** | Lead engineers design and evolve on-call rotations, runbook standards, and incident command protocols for their organisations. The Senior tier covers incident response as a practitioner; the Lead tier should cover it as a designer. | New lesson in Module 3 |
| **Technical strategy writing (Roadmaps, OKRs, technical north star)** | Lead engineers author multi-quarter technical roadmaps and align them with business OKRs. Absent entirely. `sdlc_strategy` covers some of this but needs expansion. | Expand `sdlc_strategy` or new lesson |

---

## 7. PRACTICE & ASSESSMENT

The Lead tier correctly uses AI_REVIEW assessment type for its most complex open-ended questions, which is appropriate — these questions cannot be marked by keyword matching. The rubric for the DDD lesson (se-lea-m2-01) asking learners to bound an e-commerce domain and argue against Customer-in-Order aggregate inclusion is an excellent example of practical application.

Specific concerns:
- **The duplication issue** (section 3/5) means assessment rubrics exist in two slightly different versions for the same conceptual content. Until this is resolved, there is no single source of truth for what constitutes passing competence on mentoring, architecture governance, etc.
- **The mini_project (se-lea-m6-01)** asks for an ADR and a migration roadmap but there is no ADR template lesson and no "migration planning" lesson — learners are assessed on skills they were not taught. This gap between rubric and curriculum must be closed.
- **Systems design interview practice** is absent. A staff engineer candidate is expected to walk through a complex system design verbally. The curriculum should include at least 2-3 structured system design exercises at this tier.
- **The capstone (se-lea-m7-01)** at 1,440 estimated minutes (24 hours / ~3 days) is ambitious and the rubric is strong. The tradeoff analysis requirement (three architectural decisions with alternatives considered) is excellent. However, the capstone lacks a "design review" simulation — a structured oral examination where the learner defends their architectural choices. This is the most important skill assessment for staff-level engineers and it is entirely absent.
- **Cross-tier synthesis** is expected by the capstone (it requires the apprentice, junior, senior, and lead mini_projects as prerequisites) but there is no intermediate exercise that explicitly synthesises DDD + CQRS + hexagonal architecture into a single worked scenario. Learners encounter these in isolation and must synthesise at the capstone, which is a large cognitive jump.

---

## 8. Prioritized action list

1. **RESOLVE** the `technical_leadership` folder duplication — pick one canonical location for these 5 lessons and remove the other. This is a structural maintenance risk that affects all 5 lessons simultaneously.
2. **ADD** ADR format and practice lesson to `architecture_governance` — required by the mini_project rubric but not taught.
3. **ADD** DORA metrics lesson or expand `engineering_effectiveness` — the most important engineering effectiveness measurement framework, absent.
4. **ADD** Team Topologies lesson or expand `sociotechnical` — the dominant framework for org design, referenced implicitly but not taught.
5. **ADD** RFC / Design Doc process lesson — how staff engineers drive technical direction through written proposals.
6. **ADD** `prerequisites` to `event_sourcing` and `cqrs` to enforce the DDD → event sourcing → CQRS learning sequence.
7. **UPDATE** `devops_maturity` to explicitly cover DORA metrics.
8. **UPDATE** `technical_decisions` to include pre-mortem technique and disagree-and-commit principle.
9. **UPDATE** `hexagonal_architecture` to include a concrete Spring Boot hexagonal project structure example.
10. **ADD** a design review simulation exercise at the capstone level — a structured oral defence prompt that mimics a staff engineer architecture review.
