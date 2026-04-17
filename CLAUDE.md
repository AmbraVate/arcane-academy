# Arcane Academy — CLAUDE.md

Arcane Academy is a gamified, multi-disciplinary learning platform built for **polymaths** — learners who pursue mastery across several fields in parallel. The platform combines **accelerated learning algorithms** (spaced repetition, interleaving, retrieval practice, active recall) with RPG progression (XP, ranks, bosses, badges) in a fantasy/wizard aesthetic.

The pedagogical thesis: polymaths don't suffer from breadth — they suffer from forgetting. Reviews, not new content, are the core loop.

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

### Secrets
Never commit secrets. Local dev uses `.env` (git-ignored); test/prod pull from the host's secret manager (Render env vars / AWS Secrets Manager).

---

## 6. Project Structure

```
backend/src/main/java/com/arcane/academy/
  config/
    AbstractChapterSeeder.java       — DSL helpers for content authoring
    java/Ch1Seeder.java … Ch8Seeder  — Java topic quest content (per-topic folders)
    tailwind/TwASeeder.java          — Tailwind topic content
    BossSeeder.java                  — boss content per topic
    DataSeeder.java                  — orchestrates all seeders (dev only)
    TestUserSeeder.java              — dev/test accounts
    SecurityConfig.java              — JWT + OAuth2 + BCrypt
  model/
    User, Topic, Quest, Boss, UserProgress, UserBadge, BadgeDefinition
    ReviewItem, ReviewSchedule, ReviewSession, ReviewGrade   ← review engine
  repository/    — Spring Data JPA
  service/
    QuestService, BossService, BadgeService
    ReviewScheduler, ReviewSessionService                    ← review engine
  controller/    — /api/auth, /api/topics, /api/quests, /api/code, /api/boss,
                   /api/badges, /api/reviews
  runner/        — JavaCodeRunner
  dto/           — response DTOs
  security/      — JwtAuthFilter, JwtService, OAuth2LoginSuccessHandler

backend/src/main/resources/db/migration/   — Flyway SQL migrations

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
- **E2E** (Playwright): critical flows — register, complete first quest, run a daily review
- **CI gate**: all suites + Flyway migration dry-run must pass before merge

---

## 14. Observability & Operations

- `GET /actuator/health` — only `health` exposed publicly
- `GET /actuator/prometheus` — internal scrape only
- Structured JSON logs in prod; no stack traces in responses (`GlobalExceptionHandler` returns `{ "message": "…" }`)
- Key metrics: review-session completion rate, per-topic retention %, code-runner p95 latency, auth error rate

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

## 17. Active Branch

`rewrite-java-syllabus` → target `master`
