# Arcane Academy — CLAUDE.md

Arcane Academy is a gamified, multi-disciplinary learning platform built for **polymaths** — learners who pursue mastery across several fields in parallel. The platform combines **accelerated learning algorithms** (spaced repetition, interleaving, retrieval practice, active recall) with RPG progression (XP, ranks, bosses, badges) in a fantasy/wizard aesthetic.

The pedagogical thesis: polymaths don't suffer from breadth — they suffer from forgetting. Reviews, not new content, are the core loop.

---

## 0. Status & Drift

This document describes both **design intent** and **shipping reality**. They differ in several places — the audit (April 2026) flagged the gaps below. Treat anything not in this list as accurate.

**Active topics:** Java (38 chunks, content depth verified), Tailwind CSS (4 chunks, shallower than §1.3 plans), **React (4 chunks: `rx-a` … `rx-d`, registered in TopicSeeder, JSON content auto-loaded by `JsonContentSeeder`, practice dispatched to `ReactPracticeService` via in-iframe runner)**, **SQL (8 chunks across all three tiers: Foundation `sql-a/b/c`, Practitioner `sql-d/e/f`, Expert `sql-g/h` — 22 sub-chunks, all `practiceType: NONE`; learning happens via reading + retrieval-check questions, no live SQL execution yet)**. Tier enum extended with `CAPSTONE` for `rx-d` ("The Guild Portal"). React practice grading is **client-trusted in v1** — the iframe runs tests, posts results to the backend, which performs a structural sanity check on the JSX source before awarding XP. Document this in §14a-equivalent before any leaderboard ships.

**SQL track scope:** the 8 chunks ship with the in-iframe **sql.js (SQLite-WASM) runner** for interactive practice. Each sub-chunk has hookHtml, explanationHtml, story beats with the in-fiction mentor "Cipher the Archivist", a guided practice phase, and ~4 retrieval-check questions (mix of RECALL / APPLICATION / DISCRIMINATION). Topic outline:
- **Foundation:** `sql-a` Tables & SELECT, `sql-b` Filtering (AND/OR/NOT, IN/BETWEEN/LIKE, NULL), `sql-c` Aggregation (COUNT/SUM/AVG, GROUP BY, HAVING)
- **Practitioner:** `sql-d` JOINs (INNER + LEFT/RIGHT/FULL), `sql-e` Subqueries & CTEs (incl. EXISTS, NOT IN/NULL trap), `sql-f` Modifying data (INSERT/UPDATE/DELETE, transactions, ACID, idempotency)
- **Expert:** `sql-g` Indexes (B-trees, leftmost prefix, selectivity) & EXPLAIN (plan reading, EXPLAIN ANALYZE), `sql-h` Window functions (ROW_NUMBER/RANK/LAG/LEAD, framing) & recursive CTEs (anchor + recursive step, cycle detection)

**SQL practice runner (architecture).** `SqlEditor.tsx` mounts a sandboxed iframe that loads sql.js from `cdn.jsdelivr.net/npm/sql.js@1.10.3/dist/`. Each test spec runs against a fresh in-memory SQLite database seeded by the spec's `setup` SQL (the first spec's setup is shared as the preview seed). Match modes supported: `rowsExact` (order-sensitive), `rowsAnyOrder` (set comparison), `rowCount` (count only), `columns` (column-name check). Test specs can supply either an `expectedQuery` (run as the reference) or literal `expectedRows` / `expectedRowCount` / `expectedColumns`. For mutating practice (INSERT/UPDATE/DELETE) where the user's query has no result set, set `verifyQuery` on the spec — the harness runs it against the post-mutation DB and uses ITS rows as the comparison subject (backward-compatible: specs without `verifyQuery` are unaffected). Per-test pass/fail is posted back via `postMessage` and forwarded to `/api/sql/{subChunkId}/submit`, where `SqlPracticeService` performs a structural sanity check (must contain SELECT/INSERT/UPDATE/DELETE/WITH and ≥6 chars) before idempotently awarding XP — same client-trusted trust model as React.

**13 of 22 SQL sub-chunks are interactive** as of this iteration: the entire Foundation tier (sql-a2/a3, sql-b1/b2, sql-c1/c2/c3), the entire `sql-d` JOINs chunk (sql-d1 your-first-JOIN, sql-d2 INNER JOIN, sql-d3 LEFT JOIN orphan-finder), sql-e1 scalar subqueries, sql-f1 multi-row INSERT (uses the `verifyQuery` extension to assert post-INSERT state without trusting an empty user-result), and sql-h1 ROW_NUMBER + PARTITION BY for top-N-per-group. The remaining 9 sub-chunks stay `practiceType: NONE` because they're conceptually read-only — schema reading (sql-a1), correlated subqueries + CTEs (sql-e2/e3 — sql.js semantics differ enough to mislead), UPDATE/DELETE/transactions (sql-f2/f3 — fresh DB per test makes the consequence academic), indexes & EXPLAIN (sql-g — toy data shows nothing meaningful in EXPLAIN plans), recursive CTEs (sql-h2). These show as a "Mark as studied →" view in `EncodingPage`.

**Java package:** root is `com.ambravate.polymath.academy`, not `com.arcane.academy`. Build artefact is `polymath-academy`.

**Frontend state management:** uses React Context (`useAuth`), not Zustand. Zustand appears in **content** (rx-c5 teaching material) but is not a runtime dependency.

**Tailwind version:** frontend runs **Tailwind v4** via `@tailwindcss/vite`. Content (`tw-a`…`tw-d`) and platform are aligned. The migration from v3 swapped:
- `@tailwind base/components/utilities` → `@import "tailwindcss"` in `src/index.css`
- `tailwind.config.js` `theme.extend` → `@theme { ... }` block in `src/index.css` (naming: `--color-*`, `--font-*`, `--animate-*`)
- `tailwind.config.js` + `postcss.config.js` + `autoprefixer` deps → all deleted (the Vite plugin bakes them in)

**One v4 default change worth knowing:** the default border colour changed from `gray-200` to `currentColor`. We restore the legacy default with a `@layer base` rule in `index.css` so existing `border` utilities without a colour still produce a visible neutral border. Remove that rule once every `border` utility in the codebase specifies an explicit colour.

**Visual-QA risks to spot-check** (caused by the migration):
- Custom colour utilities (`bg-gold`, `text-purple-light`, etc.) — same hex values, should be identical
- The `@layer components` block — buttons, chips, hook-card — still applied; spot-check on `EncodingPage` and `LandingPage`
- `animate-fade-up` — the v3 setup had two different `fade-up` keyframes (24px in config, 18px in CSS); we unified on 24px, so any inline `animate-[fade-up_...]` reads will be slightly larger

**Tests:** the Java/JUnit and Vitest+RTL+Playwright setup described in §13 is the target, not the current state. Test scaffolding is in progress; see `backend/src/test` and `frontend/vitest.config.ts` for what actually exists.

**AI Mentor:** `AiMentorService` and `AiMentorController` exist in code but the LLM provider, prompt template, rate limit, and PII policy are **not yet documented**. See §14a below for the standard to meet before public launch.

**Onboarding prerequisite check:** `/topic/:topicId/prereq-check` and `/topic/:topicId/css-primer` exist for Tailwind and React. Triggered when a CSS-dependent topic is selected and the user has no localStorage record of having passed/skipped the check.

**Leaderboards & public profiles:** `/leaderboard` (three boards — per-topic weekly, per-topic all-time, polymath breadth) and `/u/:username` (read-only profile aggregating per-topic XP + badges). Both gated by an opt-in `User.publicProfileEnabled` flag (default `false`); users toggle it from `/profile`. See §17a for the full surface area, the privacy contract, and the v1 performance trade-offs.

**Non-technical tracks (Foundation tier shipped):** three new topics ship a Foundation tier each — **Psychology** (`topicId: psychology`, mentor "Aetherius the Mind-Walker", purple accent — 3 chunks `psy-a/b/c`, 15 sub-chunks), **Genealogy** (`topicId: genealogy`, mentor "Theodora of the Thousand Names", gold accent — 3 chunks `gen-a/b/c`, 15 sub-chunks), and **Natural Sciences** (`topicId: sciences`, mentor "Master Vesper", teal accent — 3 chunks `sci-a/b/c`, 15 sub-chunks). All 45 sub-chunks are `practiceType: NONE` — learning is read explanation + retrieval-check questions, no live runner (same model as the read-only SQL sub-chunks; `EncodingPage` shows the "Mark as studied →" path). Content lives in `backend/src/main/resources/content/{psychology,genealogy,sciences}/` and is auto-loaded by `JsonContentSeeder`. Chunks are registered in `TopicSeeder`. Practitioner / Expert / Capstone tiers are sketched in §1.4 / §1.5 / §1.6 below but **deferred to follow-up batches** — only Foundation is committed in this batch. See §11 for the three new Foundation milestone badges.

**SQL sql-g deepened:** the Expert-tier "Indexes & EXPLAIN" chunk grew from 2 sub-chunks (~9k chars total explanation) to 3 sub-chunks (~21k chars) and now covers `sql-g1` How Indexes Work / B-Trees, `sql-g2` Selectivity & Composite Indexes, and `sql-g3` Reading EXPLAIN / EXPLAIN ANALYZE. `practiceType` stays `NONE` (toy data shows nothing meaningful in EXPLAIN plans, and the cost-model intuition is the actual learning goal). Total SQL sub-chunk count is now 23, not 22; the "13 of 22 interactive" line above has not been updated because no new interactive specs landed — `sql-g3` joins the read-only set.

**In-flight (NOT in this batch — defer to follow-up PRs if they land):** Tailwind `tw-b5` Transitions and `tw-b6` Component Extraction are being authored in a parallel agent. Tailwind `tw-c` is unchanged from the original 1196-line file (a regression rewrite was discarded; the §1.3 plan target remains intact). React `rx-e` is being authored in a parallel agent. Treat any of these as not-yet-landed until their own PR updates §1.3 / §0.

---

## 1. Product Vision

### Target Learner
A self-directed adult who wants to build durable, job-ready competence across multiple topics simultaneously (e.g. Java + Tailwind + Spanish + Music Theory) without one track cannibalising another.

### The Two Loops

1. **Forward loop (Topics)** — per-topic quest chapters that teach new material. Topics are **fully isolated**: content, progression, XP ledgers, bosses, and badges never cross topic boundaries.
2. **Review loop (Cross-topic)** — Daily / Weekly / Monthly reviews scheduled by the accelerated-learning engine. Reviews are the **only** place where items from multiple topics appear together. A review session interleaves items the algorithm has identified as due.

### Learning Paths (Topics)

Every topic is structured into **three tiers**. A learner enters a tier based on the diagnostic (see §1.1); they can skip ahead, start at Foundation, or re-enter Practitioner/Expert if returning. Each tier has its own chapters, boss(es), and Grand Boss certifying progression to the next tier.

| Tier | Purpose | Outcome |
|---|---|---|
| **Foundation** | Mental model, vocabulary, first wins | Can read the topic's output and produce simple, correct artefacts unaided |
| **Practitioner** | Composition, patterns, real-world tasks | Can solve realistic task-sized problems idiomatically |
| **Expert** | Architecture, optimisation, edge cases, synthesis | Job-ready; can reason about trade-offs and teach the topic |

---

## 1.1 Onboarding Flow

The onboarding sequence is explicit and ordered. Skipping steps is not allowed for first-time users.

1. **Auth** — Register or sign in (local or Google).
2. **Topic selection** — `/onboarding/topics`: learner picks one or more topics to start. Polymath-first: picking ≥2 is encouraged and awards the "Dual Path" badge immediately.
3. **Diagnostic (per chosen topic)** — `/onboarding/diagnostic/{topicSlug}`: a short adaptive quiz (8–15 questions) that places the learner into **Foundation / Practitioner / Expert**. Diagnostics are **per topic**, run back-to-back if multiple topics were selected.
4. **Placement summary** — shows tier placement per topic, suggested first quest, and initial review cadence.
5. **Home dashboard** — first session begins.

### Diagnostic Mechanics
- Adaptive: wrong answer → easier follow-up; right answer → harder follow-up
- Scored per tier; learner is placed in the highest tier where they passed the floor threshold
- Results seed the `ReviewScheduler` — confidently-answered items are scheduled at longer initial intervals, shaky items at shorter ones
- Learner may opt to **start at Foundation** regardless of placement (for thoroughness)
- Diagnostic can be **retaken** after 30 days, or manually reset from the profile page

### Data Model
- `Diagnostic` — per-topic question bank
- `DiagnosticAttempt` — user run, stores placement and per-question grades
- `TopicEnrollment` — `(user, topic, tier, startedAt, placementSource)` — created on diagnostic completion

---

## 1.2 Syllabus — Java

**Goal:** zero programming experience → job-ready for a Junior / Apprenticeship role.

### Foundation (Ch1–Ch4)
Mental model of "code is instructions a machine follows."

- **Ch1 — Runes of Syntax**: variables, primitive types, arithmetic, `System.out.println`
- **Ch2 — Tomes of Control**: booleans, `if/else`, comparison, string basics
- **Ch3 — Loops & Arrays**: `for`, `while`, array indexing, off-by-one pitfalls
- **Ch4 — Methods & Scope**: method definition, parameters, return, local vs instance scope
- **Foundation Grand Boss** — "The Gatekeeper": mixed-syntax challenge gating Practitioner

### Practitioner (Ch5–Ch9)
Composing programs that solve realistic small tasks.

- **Ch5 — Objects & Classes**: fields, constructors, `this`, encapsulation
- **Ch6 — Collections**: `List`, `Map`, `Set`, iteration, common algorithms
- **Ch7 — Inheritance & Interfaces**: polymorphism, abstract classes, `instanceof`
- **Ch8 — Exceptions & I/O**: checked vs unchecked, `try/catch/finally`, reading files
- **Ch9 — Generics & Streams**: type parameters, `Stream` pipelines, `Optional`
- **Practitioner Grand Boss** — "The Architect's Trial": multi-class refactor

### Expert (Ch10–Ch14)
Production-grade thinking.

- **Ch10 — Concurrency**: threads, `ExecutorService`, immutability, race-condition diagnosis
- **Ch11 — Testing & TDD**: JUnit 5, AAA, mocking, coverage intuition
- **Ch12 — Spring Boot Essentials**: DI, REST controllers, JPA basics
- **Ch13 — Databases & SQL for Java devs**: JDBC, transactions, N+1, indexing basics
- **Ch14 — Interview Forge**: data-structure fluency, system-design warm-ups, code-review drills
- **Expert Grand Boss** — "The Apprenticeship Gauntlet": timed, multi-stage interview simulation

---

## 1.3 Syllabus — Tailwind CSS

**Goal:** zero CSS/Tailwind to shipping polished, responsive, accessible, production-grade UIs.

### Foundation — "Utility Apprentice" (TW-A1 … TW-A5)
Internalise the utility-first model and read a Tailwind class string fluently.

- **TW-A1 — The Utility Mindset**: why utilities over bespoke CSS; the box model via `p-*`, `m-*`, `w-*`, `h-*`
- **TW-A2 — Colour & Typography**: palette scales, `text-*`, `font-*`, `leading-*`, `tracking-*`
- **TW-A3 — Flex Basics**: `flex`, `items-*`, `justify-*`, `gap-*` — build a nav bar
- **TW-A4 — Borders, Radius, Shadows**: `border`, `rounded-*`, `shadow-*`, ring utilities
- **TW-A5 — State Variants**: `hover:`, `focus:`, `disabled:`, `group`/`peer` intro
- **Foundation Grand Boss** — "The Component Trial": rebuild a reference card component from a screenshot

### Practitioner — "Layout Weaver" (TW-B1 … TW-B6)
Build real, responsive pages idiomatically.

- **TW-B1 — Grid Mastery**: `grid-cols-*`, `col-span-*`, responsive grids, dense packing
- **TW-B2 — Responsive Design**: `sm:` / `md:` / `lg:` / `xl:` breakpoints, mobile-first discipline
- **TW-B3 — Dark Mode**: `dark:` variant strategies (class vs media), token-driven theming
- **TW-B4 — Forms**: inputs, validation states, the `@tailwindcss/forms` plugin
- **TW-B5 — Transitions & Animation**: `transition`, `duration-*`, `ease-*`, `animate-*`, `motion-reduce:`
- **TW-B6 — Component Extraction**: `@apply` (and when not to), component classes vs utility repetition
- **Practitioner Grand Boss** — "The Landing Page Forge": build a responsive marketing page from a brief

### Expert — "Design-System Archmage" (TW-C1 … TW-C6)
Ship production UI at team scale.

- **TW-C1 — Tailwind Config Deep-Dive**: `theme.extend`, semantic tokens, plugin authoring
- **TW-C2 — Accessibility**: focus rings, contrast, `sr-only`, `aria-*` patterns, keyboard nav
- **TW-C3 — Performance**: JIT content globs, purging, critical CSS, preventing utility bloat
- **TW-C4 — Design Systems**: tokenising colour/spacing/type, dark+light theming, multi-brand
- **TW-C5 — Framework Integration**: React + Tailwind patterns, CVA / `clsx` / `tailwind-merge`, Headless UI / Radix pairing
- **TW-C6 — Production Hardening**: RTL, print styles, email-safe subsets, CI visual regression
- **Expert Grand Boss** — "The Design-System Summit": deliver a mini design system (tokens + 6 components + docs page) against a spec

---

## 1.4 Syllabus — Psychology

**Goal:** zero formal psychology background → competence equivalent to BPS / APA undergraduate-major core competencies (perception, learning, social, developmental, clinical, neuro, methods).

**Mentor:** Aetherius the Mind-Walker — an in-fiction archivist of the inner world who frames cognition as a system to be mapped rather than a mystery. Purple accent.

**Mode:** all sub-chunks are `practiceType: NONE` — read explanation + retrieval-check questions. No code editor, no live runner. Same model as the read-only SQL sub-chunks.

### Foundation — "Cartographer of Mind" (psy-a … psy-c) — SHIPPED
Build the vocabulary and mental scaffolding of cognition, learning, and the social mind.

- **psy-a — The Cognition Compass**: Perception & Attention, Working & Long-Term Memory, Decision-Making & Cognitive Biases, Language & Thought, Problem-Solving & Reasoning
- **psy-b — Behavior & Learning**: Classical Conditioning, Operant Conditioning, Habit Formation, Motivation & Self-Determination Theory, Cognitive-Behavioral Foundations
- **psy-c — Social Psychology**: Social Cognition & Schemas, Conformity & Obedience, Persuasion & Elaboration Likelihood Model, Group Dynamics, Attribution Theory & Self-Concept
- **Foundation Grand Boss** *(planned)* — "The Mind Map": mixed retrieval-check on perception/learning/social fundamentals

### Practitioner — "Healer of Patterns" *(planned, not shipped)*
Apply Foundation models to lifespan, individual differences, and the clinical lens.

- **Developmental Psychology**: stages (Piaget, Erikson, Vygotsky), attachment, adolescence, ageing
- **Clinical Foundations**: DSM/ICD frameworks, mood/anxiety/trauma disorders at a literate-generalist level, the diathesis-stress model
- **Personality Theory**: Big Five, trait vs state, temperament, identity formation
- **Practitioner Grand Boss** *(planned)* — case-formulation drill: read a vignette, identify mechanisms, propose framework-aligned interventions

### Expert — "Polymath of the Psyche" *(planned, not shipped)*
Architecture-level thinking: brain ↔ behaviour, evidence-based therapy, research literacy.

- **Neuropsychology**: brain regions ↔ functions, lesion logic, fMRI/EEG basics, neuroplasticity
- **Therapy Modalities**: CBT, ACT, psychodynamic, systems/family, evidence base for each
- **Research Methods**: study design, effect sizes, replication crisis, p-hacking, pre-registration
- **Expert Grand Boss** *(planned)* — "The Replication Audit": read a published study, identify methodological strengths/weaknesses, recommend a replication design

---

## 1.5 Syllabus — Genealogy

**Goal:** zero formal training → ready to begin BCG (Board for Certification of Genealogists) certification preparation. Methodology-first, not surname-collecting.

**Mentor:** Theodora of the Thousand Names — an in-fiction archivist who treats every name as a citation and every claim as a hypothesis. Gold accent.

**Mode:** all sub-chunks are `practiceType: NONE` — read explanation + retrieval-check questions. No code editor, no live runner.

### Foundation — "Apprentice of the Archive" (gen-a … gen-c) — SHIPPED
Learn to find sources, evaluate them, and extract evidence from human DNA.

- **gen-a — Records & Sources**: Vital Records, Census Records, Church/Parish Records, Immigration & Naturalization, Source Types: Primary/Derivative/Authored
- **gen-b — Research Methodology**: Genealogical Proof Standard, Source Citation per Evidence Explained, Reasonably Exhaustive Search, Conflict Analysis & Resolution, Research Logs & Documentation
- **gen-c — DNA & Genetic Genealogy**: Autosomal DNA Basics, mtDNA & Haplogroups, Y-DNA Inheritance, Centimorgans & Match Interpretation, Triangulation & Chromosome Mapping
- **Foundation Grand Boss** *(planned)* — "The First Proof": construct a fully-cited proof statement from supplied source extracts

### Practitioner — "Locality Specialist" *(planned, not shipped)*
Apply the GPS to real research workflows and progressively harder problems.

- **Locality-Based Research**: jurisdictional history, repository finding aids, FAN-club (Friends/Associates/Neighbours) methodology
- **Brick-Wall Methodology**: cluster research, negative searches, alternative-record substitution
- **Indirect-Evidence Proof Arguments**: building a conclusion from accumulated indirect evidence per the GPS, written proof argument structure
- **Practitioner Grand Boss** *(planned)* — "The Brick-Wall Trial": resolve a deliberately-ambiguous identity question with indirect evidence

### Expert — "Master Researcher" *(planned, not shipped)*
Pre-1850 records, complex evidence, BCG portfolio readiness.

- **Pre-1850 Research**: pre-civil-registration records, parish reconstitution, paleography, Latin/older script literacy
- **Land Records & Probate**: deed analysis, metes-and-bounds, intestate succession, wills as evidence of family structure
- **Forensic Genealogy**: heir searches, military repatriation, unknown-parentage cases, ethical handling of living-relative DNA matches
- **Expert Grand Boss** *(planned)* — "The Portfolio Submission": deliver a BCG-spec kinship-determination project against a reference rubric

---

## 1.6 Syllabus — Natural Sciences

**Goal:** zero formal science background → science-literate generalist equivalent to AP / A-level combined-sciences mastery, with statistical reasoning baked in throughout.

**Mentor:** Master Vesper — an in-fiction natural philosopher who treats experiment as ritual and replication as the only proof. Teal accent.

**Mode:** all sub-chunks are `practiceType: NONE` — read explanation + retrieval-check questions. No live wet-lab, no simulation runner (yet).

### Foundation — "Apprentice Naturalist" (sci-a … sci-c) — SHIPPED
Acquire the method itself, then the vocabulary of physics and biology.

- **sci-a — The Scientific Method**: Hypothesis Formation, Controlled Experiments & Variables, Statistical Reasoning, Peer Review & Replication, Pseudoscience Detection
- **sci-b — Physics Foundations**: Newton's Laws & Forces, Energy & Conservation, Waves & Sound, Electromagnetism Intro, Modern Physics Glimpse
- **sci-c — Biology Foundations**: Cells & Cellular Processes, Genetics & DNA, Evolution by Natural Selection, Ecosystems & Energy Flow, Human Body Systems Overview
- **Foundation Grand Boss** *(planned)* — "The First Investigation": read a popular-science claim, identify the underlying hypothesis, evaluate evidence quality

### Practitioner — "Field Investigator" *(planned, not shipped)*
Round out the major branches the Foundation skipped, and build statistical fluency.

- **Chemistry Foundations**: atoms, periodic logic, bonding, stoichiometry, reaction types, acids/bases
- **Earth & Climate Science**: plate tectonics, the rock cycle, atmospheric systems, the carbon cycle, climate-vs-weather, anthropogenic forcing
- **Ecology**: populations, communities, biogeochemical cycles, biodiversity, conservation thresholds
- **Practitioner Grand Boss** *(planned)* — "The System Diagram": given a real-world phenomenon (e.g. ocean acidification), trace it across chemistry / ecology / climate

### Expert — "Synthesist of the Sciences" *(planned, not shipped)*
Modern physics, molecular biology, and proper statistical methods.

- **Modern Physics Deep-Dive**: relativity (special then general at intuition level), quantum mechanics fundamentals, particle physics overview, cosmology
- **Genetics & Molecular Biology**: DNA replication/transcription/translation in detail, gene regulation, CRISPR, epigenetics, GWAS literacy
- **Statistical Methods for Science**: hypothesis testing, p-values vs effect sizes, confidence intervals, power analysis, multiple-comparisons correction, Bayesian basics
- **Expert Grand Boss** *(planned)* — "The Synthesis": review a contemporary research paper across these domains, write a literate-generalist explanation that survives peer scrutiny

---

## 2. Accelerated Learning Engine

Every review-eligible item (quest objective, boss question, concept card) produces one or more `ReviewItem`s. Each review item has an independent memory trace per user, scheduled by a spaced-repetition algorithm (SM-2 derived, with interleaving and difficulty weighting).

### Review Cadences

| Cadence | Scope | Purpose | Default size |
|---|---|---|---|
| **Daily** | Items due today across all active topics | Fight the forgetting curve at 24h | 10–15 items, ~5 min |
| **Weekly** | Broader recall, interleaved across topics | Strengthen medium-term retention, surface weak topics | 25–40 items, ~15 min |
| **Monthly** | Deep retention sweep + boss-tier synthesis questions | Consolidate long-term memory, certify mastery | 50–80 items, ~30 min |

### Algorithm Responsibilities (`ReviewScheduler` service)
- Compute next due-date per `ReviewItem` using ease factor, interval, and last-grade
- Select items for a session (due-first, then overdue, then upcoming soft-due)
- **Interleave** across topics — never cluster all items from one topic consecutively
- Penalise forgotten items by increasing future review frequency
- Respect per-topic opt-out (user can pause a topic's reviews without losing progress)

### Grading
Each review answer is graded on a 0–5 scale (Again / Hard / Good / Easy). Grade feeds back into the item's ease factor and interval.

---

## 3. Topic Isolation Rules (Strict)

- **No shared XP pool.** Each topic has its own XP ledger, rank progression, and chapter tree.
- **No shared bosses or quests.** A Java boss cannot reference Tailwind content.
- **Badges are scoped.** Topic-specific badges live under the topic; cross-topic badges (e.g. "Polymath: 3 topics active") are explicitly in a global namespace.
- **Reviews are the single exception.** The `reviews` module reads from every topic's item pool and is the only place cross-topic content may appear in one session.
- **Routing.** `/topics/{topicSlug}/…` for topic content; `/reviews/{daily|weekly|monthly}` for the review loop.

---

## 4. Tech Stack

### Backend
- **Java 21**, **Spring Boot 3**
- **Spring Security** (JWT + OAuth2 Client — Google)
- **Spring Data JPA** on **PostgreSQL 16**
- **Flyway** for schema migrations (production/test); `ddl-auto: update` only in local dev
- **JavaCodeRunner** — sandboxed compile + run of student code (5s timeout, thread isolation)
- **Spring Boot Actuator** — health, metrics (Prometheus-scrapable)

### Frontend
- **React 18 + TypeScript**, **Vite**
- **React Router** for routing
- **CSS Modules** + global design tokens (`index.css`)
- **Zustand** (or React Context where state is local) for auth/session state
- Typed API client in `src/api/services.ts`, shared types in `src/types/index.ts`

### Infrastructure
- **Docker** multi-stage builds (backend + frontend)
- **docker-compose** for local prod-parity runs
- **Render** as the default hosting target (`render.yaml` blueprint) — swappable for Fly.io / AWS
- **GitHub Actions** for CI (lint, test, build, migration dry-run)

---

## 5. Environments

Three tiers, enforced by Spring profiles and separate infra:

| Environment | Spring profile | DB | ddl-auto | Seed data | Purpose |
|---|---|---|---|---|---|
| **dev** | `dev` (default) | Local Postgres (docker-compose) | `update` | Full `DataSeeder` + `TestUserSeeder` | Local iteration |
| **test** | `test` | Ephemeral Postgres (Testcontainers in CI; shared staging DB for QA) | `validate` + Flyway | Minimal fixtures only | CI test runs + staging QA |
| **prod** | `prod` | Managed Postgres | `validate` + Flyway | None | Live traffic |

### Environment Variables (all envs)

| Variable | Notes |
|---|---|
| `SPRING_DATASOURCE_URL` / `USERNAME` / `PASSWORD` | DB connection |
| `JWT_SECRET` | ≥32 chars, random; **never** reuse across envs |
| `ALLOWED_ORIGINS` | CSV of permitted frontend origins |
| `GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_SECRET` | OAuth2 (optional in dev) |
| `OAUTH2_FRONTEND_REDIRECT` | OAuth callback URL |
| `REVIEW_SCHEDULER_ENABLED` | Feature flag for the review engine |
| `SENTRY_DSN` | Backend Sentry DSN. Empty (default) → starter auto-disables. Required in prod. |
| `SENTRY_ENVIRONMENT` | Tag attached to every event. Default `dev` (overridden to `prod` in `application-prod.yml`). |
| `SENTRY_TRACES_SAMPLE_RATE` | 0.0–1.0. Dev default 0.0; prod default 0.1. |
| `VITE_SENTRY_DSN` | Frontend Sentry DSN. Read by `main.tsx` at build time; empty → `Sentry.init` is skipped. |
| `VITE_SENTRY_ENVIRONMENT` | Frontend env tag. Default `dev`. |
| `VITE_SENTRY_TRACES_SAMPLE_RATE` | Frontend trace sample rate. Default `0`. |
| `RATELIMIT_ENABLED` | `true` (default) / `false`. Master toggle for the rate-limit filter. |
| `RATELIMIT_AUTH_*` / `RATELIMIT_AI_*` / `RATELIMIT_CODE_*` | Per-bucket capacity, refill, and period overrides. See `application.yml` defaults (10/10/60s for auth + AI mentor; 30/30/60s for code runner). |

### Secrets
Never commit secrets. Local dev uses `.env` (git-ignored); test/prod pull from the host's secret manager (Render env vars / AWS Secrets Manager).

### Behind a proxy / CDN (prod checklist)

The `RateLimitFilter` keys auth-endpoint buckets on `HttpServletRequest.getRemoteAddr()`. When the app sits behind Render's load balancer, Cloudflare, or any other proxy, the direct peer is the proxy — not the user — and every request would share one bucket key.

Fix: set `server.forward-headers-strategy=framework` in the prod profile (and trust the upstream X-Forwarded-For). This is **not** wired by default; turn it on once you know which proxy headers the host injects and how many hops the trust chain has.

---

## 6. Project Structure

```
backend/src/main/java/com/ambravate/polymath/academy/
  config/
    AbstractChunkSeeder.java         — DSL helpers for content authoring
    JsonContentSeeder.java           — loads content/*/*.json into the DB
    ChunkASeeder … ChunkMtoNSeeder   — Java Foundation/Practitioner chunk wiring
    PractitionerSeeder1/2.java       — Java Practitioner-tier seeders
    ExpertSeeder1/2.java             — Java Expert-tier seeders
    TailwindSeeder.java + TailwindPractitionerSeeder + TailwindExpertSeeder
    TopicSeeder.java                 — registers topics (java, tailwind, …)
    DataSeeder.java                  — orchestrates all seeders (dev only)
    TestUserSeeder.java              — dev/test accounts
    SecurityConfig.java              — JWT + OAuth2 + BCrypt
    AdminUserPromoter.java           — promotes seeded admin in dev
  model/
    User, Topic, Chunk, SubChunk, Question, UserChunkProgress,
    UserBadge, BadgeDefinition,
    ReviewSession, ReviewGrade, CuriosityQueueItem
  repository/    — Spring Data JPA
  service/
    EncodingService                  — quest/learning flow
    SpacingService                   — SM-2 + decayed memory strength
    InterleavingService              — cross-topic review mixing
    DiagnosticService, RetrievalService, FeynmanService
    BadgeService, StreakService, DashboardService
    AiMentorService                  — LLM-backed hints (provider TBD, see §14a)
    AdminStatsService, RabbitHoleService, CuriosityQueueService
    TailwindPracticeService          — Jsoup-based class validation
  controller/    — /api/auth, /api/chunks, /api/encoding, /api/code,
                   /api/reviews, /api/diagnostic, /api/dashboard,
                   /api/badges, /api/rabbit-holes, /api/curiosity-queue,
                   /api/tailwind, /api/ai-mentor, /api/admin/*
  runner/        — JavaCodeRunner (sandboxed)
  dto/           — response DTOs (incl. dto/admin/*)
  security/      — JwtAuthFilter, JwtService, OAuth2LoginSuccessHandler

backend/src/main/resources/
  content/java/        — 38 chunk JSON files (chunk-a … chunk-xj, chunk-cap)
  content/tailwind/    — tw-a, tw-b, tw-c, tw-d
  content/react/       — rx-a, rx-b, rx-c, rx-d (active; see §0)
  content/sql/         — sql-a..sql-h (Foundation/Practitioner/Expert; practiceType=NONE; 22 sub-chunks)
  db/migration/        — Flyway SQL migrations

frontend/src/
  pages/
    HomePage.tsx         — dashboard: due reviews, active topics, streak
    TopicsPage.tsx       — list of available topics
    TopicPage.tsx        — chapters + quests for a single topic
    QuestPage.tsx        — story → coding flow
    BossPage.tsx         — sequential boss questions
    ReviewPage.tsx       — daily/weekly/monthly review session runner
    ProfilePage.tsx      — per-topic XP, badges, retention stats
    OAuthCallbackPage.tsx
  components/
    quest/      — StoryPanel, CodeEditor, AiMentorPanel
    review/     — ReviewCard, ReviewGrader, InterleaveIndicator
    layout/     — LevelUpModal, BadgeToast, Navbar, TopicSwitcher
  api/services.ts
  types/index.ts
  hooks/        — useAuth, useReviews, useTopic
```

---

## 7. Frontend Design Principles

1. **Clarity over cleverness.** A learner under cognitive load should never have to decode the UI. No hidden gestures, no ambiguous icons without labels.
2. **Single primary action per screen.** "Accept Quest", "Submit", "Grade Recall" — one obvious next step.
3. **Progress must be visible.** XP bars, streak counters, due-review badge on the nav — the learner always knows where they stand.
4. **Interleaving is visible in reviews.** The UI shows which topic each review card comes from, reinforcing the cross-topic nature of the session.
5. **Calm by default, celebratory on milestones.** Quiet base palette; gold/purple bursts reserved for level-ups, boss victories, badge earns.
6. **Mobile-first practice, desktop-first authoring.** Review sessions must work on phone; coding quests assume a keyboard.
7. **Accessibility is a requirement, not a nice-to-have.** WCAG AA contrast, keyboard navigable, `prefers-reduced-motion` respected.
8. **Fantasy aesthetic is flavour, not friction.** Ranks and bosses are theming; they never obscure what the user is doing or learning.

### Design Tokens (`frontend/src/index.css`)

```css
--bg, --surface, --card, --border
--gold, --gold-dim
--purple, --purple-dim, --purple-light
--teal, --teal-dim
--text, --muted
--red, --green, --orange
```

Chips: `chip chip-purple|green|teal|red|gold|gray`
Buttons: `btn btn-primary|ghost|success`

---

## 8. Content Authoring DSL (Per-Topic)

Content lives in chapter seeders under `config/<topic>/`. Helpers come from `AbstractChapterSeeder`.

```java
// Main quest
q(id, title, eyebrow, topic, chapter, order, xp, filename,
  story(...), problemHtml, hint, starterCode, winStory, tests(...))

// Side quest — sideQuest = true, does not gate chapter completion
sq(id, title, eyebrow, topic, chapter, order, xp, filename, ...)
```

### Story Beats

```java
story(
  n("Narration — HTML allowed"),
  d("emoji", "type", "Speaker", "css-class", "Dialogue — HTML allowed"),
  e("Label", "Example code")
)
```

### Critical Rule: `\n` vs `\\n` in `e()`
- `\n` in a Java literal = real newline → renders correctly in `<pre>`
- `\\n` = literal backslash-n → renders as the text `\n`

### Test Cases

```java
tests(
  test("label", "injectedVars", "expectedOutput")
)
// injectedVars: "null" for no injection; or e.g. "int coins = 35;"
```

### Java Runner — Import Rule
`import` inside a method body fails to compile. Quests needing imports (`ArrayList`, `HashSet`, …) **must** supply a full class template as `starterCode`. Never rely on the wrapper to inject imports.

---

## 9. Gameplay Flows

### Quest Flow
1. **Story stage** — full-page story, scroll-reveal brief → "Accept Quest" (or "Review Quest" if completed)
2. **Coding stage** — split: brief/hints left, editor/run/submit/AI mentor right; inline "📖 View lesson" toggle
3. **Complete stage** — win story + "Return to Academy" + "🔄 Practice Again" (practice re-runs don't re-award XP; deduplicated by `UserProgress` unique constraint)

### Boss Flow
- Questions shuffled per attempt
- One wrong answer = defeat; breakdown panel auto-opens with ✓/✗ per question, correct answer, explanation
- Victory → `POST /api/boss/{id}/defeat` awards XP and unlocks the next chapter **within the same topic**

### Review Flow
1. **Session start** — `GET /api/reviews/{cadence}/next` returns the scheduled item set
2. **Item prompt** — ReviewCard shows the item (code MCQ, concept recall, short-answer)
3. **Self-grade** — user rates recall (Again/Hard/Good/Easy); server re-schedules
4. **Session end** — summary: retention %, topics touched, streak maintained

---

## 10. XP, Ranks & Locking

### Ranks (per topic)

| Rank       | XP |
|------------|----|
| Novice     | 0 |
| Apprentice | 800 |
| Adept      | 2 000 |
| Mage       | 4 000 |
| Archmage   | 6 500 |
| Magus      | 8 000 |
| Lord Magus | 11 000 |

XP is awarded once per quest/boss — unique `(user_id, item_id, item_type)` constraint in `user_progress`. Review grading awards small, diminishing XP separately, tracked on `review_sessions`.

### Locking Rules
- Entry tier is set by the onboarding diagnostic (§1.1); chapters inside the entry tier unlock sequentially from that tier's first chapter
- If placed above Foundation, lower-tier content is **unlocked but optional** (available for review / completionist play)
- Within a tier: each main quest requires the previous; first quest of a chapter requires the previous chapter's boss
- **Grand Boss** at the end of each tier (Foundation / Practitioner / Expert) gates progression to the next tier
- Side quests in the current tier are always unlocked; they do not count toward chapter completion
- Tier skipping without the diagnostic is disallowed — Grand Bosses cannot be challenged out of order

---

## 11. Badges

Badge definitions live in the `BadgeDefinition` enum (code, not DB rows). `user_badges` records who earned what and when. `BadgeService.evaluateAndAward(userId)` runs after every quest submission, boss defeat, login, and review session.

### Categories
| Category | Examples |
|---|---|
| **Topic-scoped Quest** | First Spell Cast, Rune Initiate (Java Ch1), Structure Weaver (Java Ch3), Tailwind Initiate |
| **Topic-scoped Boss** | Dragon Slayer (first boss of a topic), Conqueror of Shadows (all bosses in a topic) |
| **Topic-scoped XP** | Spark of Magic (100), Legendary Wizard (5k) — per topic |
| **Review / Retention** | Daily Ritualist (7 daily reviews), Mnemonic Master (30d review streak), Deep Recall (90%+ retention over a month) |
| **Polymath (global)** | Dual Path (2 topics active), Triad (3 topics), Renaissance (5 topics with rank ≥ Adept) |
| **Streak** | Consistent Apprentice (3d), Week of Dedication (7d), Unyielding Will (30d) |

### SQL track badges (LEARNING category)

Four milestone badges across the SQL track, mirroring the Tailwind/React minimalism (one badge per tier + a track-master capstone) rather than Java's per-chunk approach:

| Badge | Trigger | Glyph |
|---|---|---|
| **Query Initiate** (`SQL_QUERY_INITIATE`) | All sub-chunks in `sql-a` complete (Tables & SELECT) | 🪄 |
| **Join Weaver** (`SQL_JOIN_WEAVER`) | All sub-chunks in `sql-d` complete (JOINs) | 🔗 |
| **Query Optimiser** (`SQL_QUERY_OPTIMISER`) | All sub-chunks in `sql-g` complete (Indexes & EXPLAIN) | ⚙️ |
| **Cipher's Heir** (`SQL_TRACK_MASTER`) | All 8 SQL chunks complete (`sql-a` … `sql-h`) | 🗃️ |

Conditions live in `BadgeService.checkCondition` and key off the `completedChunks` set (chunk IDs match the JSON content's `id` field — `sql-a`, `sql-b`, etc.). Awarded automatically by `BadgeService.evaluateAndAward(userId)`, which already runs after every quest submission, retrieval-check pass, and review session.

### React track badges (LEARNING category)

Three milestone badges across the React track, mirroring the Tailwind capstone pattern (Foundation + Practitioner milestones plus the capstone) rather than Java's per-chunk approach. The Expert tier (`rx-c`) is intentionally unbadged — completing it on the way to the capstone is the reward, and `REACT_CAPSTONE_COMPLETE` doubles as the track-master since `rx-d` is the terminal chunk:

| Badge | Trigger | Glyph |
|---|---|---|
| **Hook Initiate** (`REACT_HOOK_INITIATE`) | All sub-chunks in `rx-a` complete (Components, props, useState) | ⚛️ |
| **State Weaver** (`REACT_STATE_WEAVER`) | All sub-chunks in `rx-b` complete (useEffect, custom hooks, lifting state) | 🧶 |
| **Guild Architect** (`REACT_CAPSTONE_COMPLETE`) | All sub-chunks in `rx-d` complete (The Guild Portal capstone) | 🏛️ |

Conditions live in `BadgeService.checkCondition` and key off the same `completedChunks` set as the SQL/Tailwind/Java badges. Awarded automatically by `BadgeService.evaluateAndAward(userId)`, which `ReactPracticeService.recordPracticeCompletion` already invokes after every successful sub-chunk submission; sub-chunks marked as studied via the read-only path through `EncodingService` also trigger the same evaluation.

### Non-technical track Foundation badges (LEARNING category)

One milestone badge per non-technical track, awarded when all three Foundation chunks complete. Practitioner / Expert badges will land alongside those tiers in follow-up batches:

| Badge | Trigger | Glyph |
|---|---|---|
| **Mind-Walker** (`PSY_FOUNDATION_COMPLETE`) | `psy-a`, `psy-b`, `psy-c` all complete | 🧠 |
| **Lineage Scholar** (`GEN_FOUNDATION_COMPLETE`) | `gen-a`, `gen-b`, `gen-c` all complete | 🌳 |
| **Natural Philosopher** (`SCI_FOUNDATION_COMPLETE`) | `sci-a`, `sci-b`, `sci-c` all complete | 🔬 |

Enum values are added in `BadgeDefinition.java`; conditions live in `BadgeService.checkCondition` and key off the `completedChunks` set (same pattern as the SQL / React / Tailwind / Java badges). Awarding is automatic via `BadgeService.evaluateAndAward(userId)`, which already runs after every quest submission, retrieval-check pass, login, and review session — and these tracks complete via the retrieval-check path because all sub-chunks are `practiceType: NONE`.

---

## 12. Authentication

### Local (Email/Password)
`POST /api/auth/register`, `POST /api/auth/login`. BCrypt-hashed passwords, JWT returned.

### Google OAuth2
`/oauth2/authorization/google` → Google → backend callback → JWT → redirect to `/oauth2/callback?token=…&userId=…&username=…`.

**Linking:** If a Google user's email matches a local user, accounts link (existing user gains the Google provider). OAuth-only users have `passwordHash = null`.

### `User` Auth Fields
- `authProvider` (enum: `LOCAL`, `GOOGLE`)
- `providerId` (Google `sub`, nullable)
- `passwordHash` (nullable)

---

## 13. Testing Strategy

- **Unit tests** (JUnit 5): services, scheduler math, badge evaluator
- **Integration tests** (Spring Boot + Testcontainers Postgres): repositories, controllers, security
- **Code-runner tests**: sandbox timeout, malicious code, common student mistakes
- **Frontend unit** (Vitest + React Testing Library): components, hooks
- **Accessibility tests** (Vitest + jest-axe in jsdom): UI primitives — Button, Card. Catches semantic WCAG AA failures (alt text, ARIA, headings, labels) at <2s. Visual rules (contrast, focus indicators, layout overlap) need a real browser; pair with `@axe-core/playwright` once Playwright is wired.
- **E2E** (Playwright): critical flows — register, complete first quest, run a daily review. Not yet wired.
- **CI gate**: GitHub Actions (`.github/workflows/ci.yml`). Two parallel jobs:
  - **Backend** — `mvn -B verify` on Java 21. Tests currently `-DskipTests` because the suite is bootstrapped only; remove the flag once the JUnit harness exists.
  - **Frontend** — `npm ci → lint → tsc --noEmit → test → test:a11y → build`. All steps green on `master` today.
  - The pipeline cancels in-progress runs on the same branch (`concurrency`) so a force-push doesn't queue a backlog.

---

## 14a. AI Mentor — Integration Policy (REQUIRED before public launch)

The platform exposes an AI mentor for hints during coding quests via `AiMentorService` and `/api/ai-mentor`. Before public launch, the following must be documented and enforced — **not yet implemented**:

| Requirement | Standard |
|---|---|
| **Provider** | Document which LLM provider/model is used (e.g. Anthropic Claude Sonnet 4.7, OpenAI GPT-4.x). Pin model version. |
| **Prompt template** | Versioned and stored in code, not constructed at runtime from user input alone. Must include the system instruction "Do not give the answer outright — guide the learner toward it." |
| **Cost cap** | Per-user daily token budget (default suggested: 50k input + 10k output). Hard-fail with a friendly message when exceeded. Stored in `user_ai_usage` table or equivalent. |
| **Rate limit** | ✅ Shipped via `RateLimitFilter` (Bucket4j, in-memory). Default 10 req/min per user on `/api/ai-mentor/**`, configurable via `RATELIMIT_AI_*` env vars. Returns 429 + `Retry-After`. Filter runs BEFORE `JwtAuthFilter` so a flood doesn't burn JWT-parse CPU. |
| **PII / data policy** | Document explicitly what is sent to the LLM provider: at minimum the learner's code submission, the quest brief, and any error message. **Must not** send: email, real name, JWT, or any other identifier. The user must consent to this in onboarding. |
| **Provider env vars** | `AI_PROVIDER` (`anthropic` / `openai` / `none`), `AI_API_KEY`, `AI_MODEL`, `AI_DAILY_TOKEN_CAP`. Absence of `AI_API_KEY` must disable the mentor cleanly (UI hides the panel) — not crash. |
| **Failure mode** | Network/timeout/rate-limit → mentor returns a generic "I can't help right now, try the hint" rather than a stack trace. |
| **Observability** | Log per-call: user hash, quest id, prompt token count, response token count, latency, status. No prompt/response content in logs (privacy). |

Until the above is in place, the mentor should be feature-flagged off in production via `AI_MENTOR_ENABLED=false`.

---

## 14. Observability & Operations

- `GET /actuator/health` — only `health` exposed publicly
- `GET /actuator/prometheus` — internal scrape only
- Structured JSON logs in prod; no stack traces in responses (`GlobalExceptionHandler` returns `{ "message": "…" }`)
- Key metrics: review-session completion rate, per-topic retention %, code-runner p95 latency, auth error rate

### Sentry — error & performance monitoring

Backend wires `sentry-spring-boot-starter-jakarta`; frontend wires `@sentry/react`. Both **auto-disable when their DSN env var is empty**, which is the default in dev — so the codebase boots cleanly with zero Sentry network traffic until you opt in by setting `SENTRY_DSN` (backend) and `VITE_SENTRY_DSN` (frontend).

**Privacy contract:**
- `sentry.send-default-pii: false` — Sentry's auto-attached IPs/cookies/headers are off.
- We never call `Sentry.setUser(...)` (backend or frontend). Errors include exception messages and stack traces only.
- The Logback integration captures `ERROR`-level log lines as Sentry events. **Audit existing `log.error(...)` calls** before turning Sentry on in prod — anything that interpolates user input (emails, JWT contents, query strings) becomes a Sentry payload. Telemetry log lines already pseudonymise via HMAC; everything else is on the author.
- Frontend trace sample rate defaults to `0`. Browser tracing is opt-in via `VITE_SENTRY_TRACES_SAMPLE_RATE`.

**Failure mode:** missing or malformed DSN → starter logs a single warning and stays silent. The app does not crash. Sentry init failures on the frontend likewise no-op.

### Engagement Telemetry — Event Catalog

`TelemetryService` emits two channels per event: a Micrometer counter (low-cardinality labels only — Prometheus-friendly) and a structured log line (named logger `TELEMETRY`, with HMAC-SHA256-hashed user IDs for privacy).

| Event | Counter | Labels | Triggered by |
|---|---|---|---|
| **quest_started** | `arcane_quest_started_total` | `topic` | First time a learner enters a sub-chunk's encoding session |
| **quest_completed** | `arcane_quest_completed_total` | `topic` | Sub-chunk transitions to `COMPLETE` (first time only) |
| **review_grade_given** | `arcane_review_grade_given_total` | `cadence`, `passed` | Each individual question answered in a review/retrieval session |
| **review_session_completed** | `arcane_review_session_completed_total` | `cadence` | A whole review session is submitted |
| **streak_extended** | `arcane_streak_extended_total` | (none) | A learner's streak grows by 1 day |
| **streak_broken** | `arcane_streak_broken_total` | (none) | A learner returns after >1 day idle (resets to 1) |
| **badge_earned** | `arcane_badge_earned_total` | `category` | `BadgeService.evaluateAndAward` saves a new `UserBadge` |
| **diagnostic_completed** | `arcane_diagnostic_completed_total` | `topic`, `tier` | A learner completes (or skips) the placement diagnostic |

**`topic` label values:** `java | tailwind | react | sql | unknown` (derived from `chunkId` prefix — `chunk-*` → java, `tw-*` → tailwind, `rx-*` → react, `sql-*` → sql). Anything else falls to `unknown` to defend against cardinality blow-ups.

**`cadence` label values:** `DAILY | WEEKLY | MONTHLY | RETRIEVAL | DIAGNOSTIC | unknown`.

**Privacy / PII:** user IDs are NEVER raw in logs. They go through HMAC-SHA256 with the `TELEMETRY_HASH_SALT` env var (default in dev: `dev-salt-rotate-in-production`), truncated to 16 hex chars. Same user → same pseudonym, but the hash cannot be reversed to a UUID without the salt. **Set `TELEMETRY_HASH_SALT` to a strong random value (≥32 chars) in production**.

**Why no `user_id` label on counters?** Prometheus creates a separate time series per unique label combination. Putting user IDs there would create one series per user → tens of thousands of series, killing query performance. Per-user behavioural analysis belongs in the log channel.

### Recommended Grafana Dashboards (3 to build first)

1. **Engagement health** — quest_started/completed rates by topic; review_session_completed rate; streak_broken vs extended ratio (the leading indicator of churn)
2. **Content quality** — `review_grade_given{passed="false"}` by cadence (subjects that consistently fail signal weak content); funnel drop at `diagnostic_completed{tier=...}` distribution
3. **Reliability** — Spring Boot's built-in counters: HTTP 5xx rate by endpoint, code-runner p95 latency, auth failure rate

---

## 15. Deployment

### Render (default)
`render.yaml` blueprint defines:
- **polymath-academy-api** — Docker web service (backend, `prod` profile)
- **polymath-academy** — static site (frontend, with API rewrites)
- **polymath-db** — managed Postgres

Set `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `JWT_SECRET` manually.

### Local Prod-Parity
```bash
JWT_SECRET=$(openssl rand -base64 48) docker compose up --build
```

---

## 16. Test Accounts (dev/test only — seeded by `TestUserSeeder`)

Password for all: **`Test1234!`**

| Email | Username | Stage |
|---|---|---|
| `novice@polymath.test` | `test_novice` | 0 XP, no topics started |
| `apprentice@polymath.test` | `test_apprentice` | Java Ch1 complete |
| `adept@polymath.test` | `test_adept` | Java Ch1–2 + Tailwind TW-A |
| `mage@polymath.test` | `test_mage` | Java Ch1–4, mature review schedule |

---

## 17. Leaderboards & Public Profiles

### Surface area

| Method | Path | Notes |
|---|---|---|
| `GET` | `/api/leaderboard/topic/{topicId}/weekly` | Top N users by XP earned in `topicId` since current ISO-week start (Mon 00:00 UTC) |
| `GET` | `/api/leaderboard/topic/{topicId}/all-time` | Top N users by XP earned in `topicId` since signup |
| `GET` | `/api/leaderboard/polymath` | Top N users by distinct topics they've earned XP in; tie-break = total XP |
| `GET` | `/api/profile/public/{username}` | Read-only profile; **404 if user is opted out** (intentional — same response for "doesn't exist" and "private", to avoid enumeration leakage) |
| `GET` | `/api/profile/visibility` | `{ enabled: boolean }` — caller's current opt-in state |
| `POST` | `/api/profile/visibility` | Body `{ enabled: boolean }` — caller toggles their own visibility |

Frontend routes: `/leaderboard` (`?board=weekly|all-time|polymath` + `?topic=java|tailwind|react`) and `/u/:username`. Toggle lives at the top of `/profile`.

### Privacy contract

- **Default is opt-in `false`.** No data appears anywhere until the user explicitly toggles it on. The intent is "leaderboards are a feature you choose to use," not a default exposure.
- The public profile DTO **never** includes: email, role, providerId, password hash, or per-sub-chunk completion timestamps. It does include: username, member-since (month/year), rank title, totalXp, streakDays, per-topic xp+completion counts, earned badge IDs.
- Leaderboard rows expose: username, rank title, streakDays, badgeCount, xpEarned (windowed or total), globalXp, topicCount (polymath board only).
- Username clicks on the leaderboard go to `/u/{username}` — already gated behind authentication. We do **not** expose any of this without a logged-in caller.

### XP attribution

Per-topic XP is computed by joining `UserChunkProgress.completedAt` (with status COMPLETE) → `SubChunk.xpReward` → `Chunk.topicId`. We deliberately do NOT use `User.totalXp` for topic-windowed numbers because that ledger is global and includes review XP we don't want to attribute to a single topic.

### v1 performance

`LeaderboardService` aggregates in memory: one `progressRepository.findAll()` + one `subChunkRepository.findAll()` per request. Acceptable up to ~10k completed sub-chunks across all users — switch to a native SQL aggregation (or a materialised `weekly_xp_by_user` table refreshed on a cron) when the table grows past that. `topicSubChunkIds()` is bounded per topic.

### Tests
`LeaderboardServiceTest` (11 tests) covers the privacy filter, the COMPLETE-only filter, the ISO-week boundary, topic isolation, ordering, limits, and the breadth-then-depth polymath sort. `PublicProfileServiceTest` (5 tests) covers the opt-in gate, the per-topic aggregation, the badge decoration + ordering, and the "unknown badge id" fallback.

---

## 18. Active Branch

`rewrite-java-syllabus` → target `master`
