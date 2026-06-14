# Arcane Academy — Curriculum Audit Executive Summary

**Date:** 2026-06-14
**Scope:** 4 active pathways × 4 tiers = 16 audit reports (PRs #15–#18)
**Auditors:** Principal/Distinguished Engineer (SE), Principal Data Architect (DE), Staff Principal Frontend Engineer (FE), Research Physicist / Professor (Physics)
**Total lessons audited:** ~750 across 437 topics

---

## Score Card

| Pathway | Apprentice | Junior | Senior | Lead |
|---|---|---|---|---|
| **Software Engineering** | 4/4/4/5 | 3/3/4/3 | 4/3/4/3 | 4/5/4/4 |
| **Data Engineering** | 4/4/4/4.5 | 4/4.5/4.5/4 | 4.5/5/4.5/4.5 | 3.5/5/4/5 |
| **Frontend Engineering** | 4/4/4/5 | 4/4/4/4 | 3/5/4/4 | 4/5/4/5 |
| **Physics** | 4/5 overall | 4.5/5 overall | 5/5 overall | 5/5 overall |

*Scores: Coverage / Rigor/Depth / Sequencing / Practice quality, each out of 5.*

---

## Headline Findings

### What is genuinely excellent (preserve and protect)

- **Physics Senior and Lead** are the strongest tiers on the entire platform. The quantum physics lesson (`phy-sen-m3-02`) was named the quality benchmark: university-grade mathematical rigour, historical context, worked numerical examples, and sophisticated narrative. The Physics Lead tier was called "the most distinctive tier on any platform — genuine research-practice pedagogy."
- **DE Senior** is the strongest engineering tier: "technically precise, production-grounded, intellectually honest about trade-offs, and free of the oversimplifications that plague most online curriculum at this level."
- **SE Lead** DDD, CQRS, and mentoring lessons are exceptional. The multidisciplinary integration module (SE + economics, mathematics, philosophy, psychology, systems thinking) is a genuine differentiator absent from almost every other SE curriculum.
- **FE Lead** was called "the most intellectually ambitious and consistently excellent tier in the pathway." The technical_leadership lesson: "the best lesson on technical leadership I have seen in any curriculum."
- **DE Lead capstone** ("The Grand Data Architect") was called "the platform's best assessment — genuinely university-equivalent synthesis work."
- **Practice quality is consistently strong** across all pathways at Apprentice tier. The `mistakeId` pattern, scenario-anchored guided steps, and rubric-based solo assessments set a high bar.

---

## Critical Issues (Fix Before Any New Content)

These are bugs or structural defects that affect learners immediately.

| # | Issue | Location | Action |
|---|---|---|---|
| 1 | **Numerical error** in spring energy formula: `½ × 400 × 0.25 = 50 J` should be `½ × 400 × 0.25² = 12.5 J` | `phy-jun-m4-01` | Fix formula |
| 2 | **Stale legacy file** with incompatible schema (no `id`, no `difficulty`, different frontmatter) | `se/apprentice/foundations_of_computation/variables_and_state/variables_intro.md` | Delete file |
| 3 | **Empty lesson files** — topics listed but no content: `etl_fundamentals` (DE Junior), `data_mesh`, `modern_data_stack`, `self_service_analytics`, `streaming_and_realtime` (DE Lead), `mini_project` (FE Lead) | Multiple | Create content or remove phantom topics |
| 4 | **Duplicate lesson IDs** — `technical_leadership/` folder contains se-lea-m1-01 through se-lea-m1-05, identical IDs to standalone topic folders (`mentoring`, `architecture_governance`, etc.) with divergent rubrics | SE Lead | Resolve to single canonical location |
| 5 | **Spring Boot never taught** but SE Junior mini-project requires it (annotations, IoC, `@RestController`, `@Autowired`) | SE Junior | Add Spring Boot topic (3–4 lessons) |
| 6 | **mini_project directory is empty** — no integration exercise between design system module and capstone | FE Lead | Populate with design system governance scenario |

---

## Top Curriculum Gaps by Pathway

### Software Engineering

**Junior** — 43 of 47 topics have exactly 1 lesson (insufficient for job-readiness depth):
- Spring Boot fundamentals — *#1 gap; mini-project requires it implicitly*
- Lambdas and functional interfaces — *prerequisite for lists_advanced and maps_advanced, neither of which currently has a foundation*
- Optional / null safety
- Git conflict resolution
- DTO pattern
- Streams API as standalone concept
- SQL depth (currently 1 lesson each for sql_basics and joins)

**Senior** — every topic is 1 lesson; key absences:
- OAuth 2.0 / OIDC (JWT alone is insufficient at senior level)
- Circuit Breaker pattern / Resilience4j
- Distributed transactions / Saga pattern
- Docker / container fundamentals
- System design practice (the format used in every senior interview)
- API design principles (REST maturity, versioning, idempotency, pagination)

**Lead** — high quality but missing:
- ADR format and practice (*mini-project requires it but no lesson teaches it*)
- DORA metrics
- Team Topologies (Skelton & Pais)
- RFC / Design Doc process
- Staff engineer archetypes (Larson)
- Design review simulation (oral architecture defence)

---

### Data Engineering

**Junior** — 38 topics, only 34 lesson files:
- ETL fundamentals — *listed as topic, no lesson file; critical job-readiness gap*
- HAVING clause — *absent from advanced SQL module despite completing GROUP BY*
- Schema migrations in production (Flyway/Liquibase, expand-contract pattern)
- Data pipeline orchestration basics (Airflow DAG concepts)
- JSON/JSONB querying in PostgreSQL

**Senior** — strong but missing the modern data stack:
- Data lakehouse architecture (Delta Lake / Iceberg / Hudi) — *highest market-relevance gap*
- Apache Spark fundamentals — *required for the polyglot platform project*
- Data observability (Great Expectations, dbt tests, freshness/completeness SLOs)
- CDC deep-dive (Debezium architecture, connector failure modes)
- Data contracts (Avro/Protobuf, Schema Registry)

**Lead** — 31 topics, only 21 lesson files:
- Data mesh — *the defining paradigm for Lead data architects in 2025–2026; listed, no file*
- Modern data stack (dbt + Airflow + warehouse + BI as integrated decision)
- Data product thinking (product interfaces, SLAs, health metrics)
- Self-service analytics programme design
- Streaming / real-time architecture at the Lead (Lambda vs Kappa)
- Organisational design for data engineering (team topologies, hiring strategy)

---

### Frontend Engineering

**Apprentice** — strong but missing:
- CSS Custom Properties — *used in mini-project scaffold without being taught*
- JavaScript Functions — *prerequisite for everything in JS module; appears absorbed not explicit*
- CSS Cascade — *the C in CSS; specificity is present but cascade order is not*
- HTML Forms — *direct prerequisite for Junior form handling module*

**Junior** — biggest commercial gap:
- **TypeScript** — *expected at virtually every junior interview in 2025; absent entirely*
- `useEffect` as a dedicated topic (currently buried inside `loading_states`)
- Custom Hooks as a dedicated topic with worked examples
- `useReducer`
- TanStack Query / server state introduction
- Mid-module consolidation project (9 modules before the capstone with no checkpoint)

Also: `presentational_vs_container` (3 lessons) is obsolete post-hooks; reduce to 1 historical-context lesson.

**Senior** — deepest individual lessons on the platform but coverage suffers from 1-lesson-per-topic:
- TypeScript advanced (generics, discriminated unions, utility types)
- TanStack Query / server state as a dedicated topic
- E2E Testing (Cypress / Playwright)
- Next.js / SSR concepts (the dominant React framework; absent)
- CI/CD with performance budgets and Lighthouse CI

**Lead** — excellent content, structural gaps:
- Empty `mini_project` directory
- Engineering hiring and levelling lesson
- Technical strategy document lesson
- `versioning` and `change_management` have significant overlap — merge

---

### Physics

**Apprentice** — strong; targeted additions only:
- `v = fλ` confirmation in wave_fundamentals module
- Energy conservation as a named law (KE + GPE = constant lesson)
- Basic electricity (charge, current, voltage, resistance) — *may be missing entirely*
- Dimensional analysis as a checking technique

**Junior** — near-excellent; one error to fix:
- Fix `phy-jun-m4-01` spring energy calculation (see Critical Issues #1)
- Quantitative Faraday's law (EMF = −dΦ/dt)
- Boltzmann entropy formula (S = k ln W)
- Vector resolution in trigonometry applications

**Senior** — benchmark quality; additions for completeness:
- Relativity of simultaneity (logically prior to time dilation; currently omitted)
- Angular momentum as a vector with right-hand rule (needed for precession direction)
- SHM time-domain solutions x(t) = A cos(ωt), v(t) = −Aω sin(ωt)
- Maxwell's equations in integral form (confirm in unsampled `maxwells_theory` module)
- TISE and infinite square well (confirm in unsampled `wave_mechanics` module)
- Full relativistic energy-momentum relation E² = (pc)² + (mc²)²

**Lead** — exceptional; currency updates:
- Black-hole information paradox (current frontier topic)
- Hubble tension (the most active controversy in modern cosmology)
- EU AI Act as a science-policy case study
- Lyapunov exponent estimation formula (chaos_theory lesson)

---

## Cross-Pathway Patterns

### What works across all pathways
1. **Practice quality is the platform's strongest asset.** The `mistakeId` pattern, scenario-anchored guided steps, specific rubric items, and expert-quality `modelAnswer` blocks are consistently above industry standard.
2. **Capstone projects are well-designed.** The Task Board API (SE Junior), The Village Ledger (DE Apprentice), The Grand Data Architect (DE Lead), The Guild Dashboard (FE Junior), and The Arcane Frontend Architect (FE Lead) are all portfolio-worthy.
3. **Lore integration is non-intrusive and adds motivation** without sacrificing technical accuracy.
4. **Word-count bands enforced by CI lint** (`MarkdownLessonValidationTest`) successfully prevent thin lessons.

### Structural patterns to address
1. **1-lesson-per-topic is the dominant structural weakness** at Junior and Senior tiers across all engineering pathways. Complex topics (Big-O, design patterns, SQL, OAuth, microservices, TypeScript) cannot build durable competence from a single lesson.
2. **Topic lists inflate over lesson files** — phantom topics that appear in module declarations but have no lesson file. Each pathway has several. Run an automated audit (`topic list vs content directory listing`) to surface all gaps.
3. **Prerequisites are inconsistently declared** — many lessons omit `prerequisites` in frontmatter even when a dependency is obvious (e.g., CQRS requiring event sourcing, event sourcing requiring DDD). Add prerequisites systematically to support correct learning sequencing.
4. **Integration exercises are sparse between modules.** Most tiers have a single mini-project at the end. A mid-tier synthesis checkpoint (after approximately half the modules) would reduce the cognitive jump to the capstone.

---

## Prioritized Action List (Cross-Pathway, Top 20)

| # | Action | Pathway | Impact |
|---|---|---|---|
| 1 | Fix `phy-jun-m4-01` spring energy numerical error | Physics | 🔴 Correctness |
| 2 | Delete stale `variables_intro.md` in SE apprentice `foundations_of_computation` | SE | 🔴 Schema/seeder |
| 3 | Create content for empty topics: `etl_fundamentals`, `data_mesh`, `modern_data_stack`, `self_service_analytics`, `streaming_and_realtime` | DE | 🔴 Critical gap |
| 4 | Populate FE Lead `mini_project` directory with design system governance scenario | FE | 🔴 Missing exercise |
| 5 | Resolve SE Lead `technical_leadership` folder ID duplication | SE | 🔴 Maintenance hazard |
| 6 | Add Spring Boot fundamentals topic (3–4 lessons) to SE Junior | SE | 🟠 Job readiness |
| 7 | Add TypeScript dedicated topic (3+ lessons) to FE Junior | FE | 🟠 Employability #1 gap |
| 8 | Add Data Lakehouse lesson (Delta Lake / Iceberg) to DE Senior | DE | 🟠 Market relevance |
| 9 | Expand SE Junior to add depth to 43 single-lesson topics (lambda, Optional, SQL, Git branching, DTO) | SE | 🟠 Depth |
| 10 | Add `useEffect` as dedicated topic in FE Junior Module 2 | FE | 🟠 Curriculum structure |
| 11 | Add OAuth 2.0 / OIDC lesson to SE Senior | SE | 🟠 Security gap |
| 12 | Add Spark fundamentals lesson to DE Senior | DE | 🟠 Job readiness |
| 13 | Add ADR format lesson to SE Lead (required by mini-project rubric) | SE | 🟠 Rubric-curriculum gap |
| 14 | Add Custom Hooks dedicated topic to FE Junior | FE | 🟠 Interview gap |
| 15 | Add Energy Conservation lesson to Physics Apprentice | Physics | 🟡 Syllabus gap |
| 16 | Add CSS Custom Properties lesson to FE Apprentice | FE | 🟡 Mini-project alignment |
| 17 | Add DORA metrics to SE Lead `engineering_effectiveness` | SE | 🟡 Leadership currency |
| 18 | Add Data Observability lesson to DE Senior | DE | 🟡 Production skills |
| 19 | Reduce FE Junior `presentational_vs_container` from 3 to 1 historical lesson | FE | 🟡 Obsolete content |
| 20 | Run automated topic-vs-file audit across all 4 pathways to surface all phantom topics | All | 🟡 Structural hygiene |

---

## Detailed Reports

Individual tier reports are in the linked PRs:

- [PR #15 — Software Engineering (all tiers)](https://github.com/AmbraVate/arcane-academy/pull/15)
- [PR #16 — Physics (all tiers)](https://github.com/AmbraVate/arcane-academy/pull/16)
- [PR #17 — Frontend Engineering (all tiers)](https://github.com/AmbraVate/arcane-academy/pull/17)
- [PR #18 — Data Engineering (all tiers)](https://github.com/AmbraVate/arcane-academy/pull/18)
