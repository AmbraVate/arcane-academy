# Arcane Academy — Project Reference

> **Purpose:** Living reference for all engineers working on this codebase. Update this document at every significant decision point.
>
> **Last updated:** 2026-05-30 (Phase 1 — Topic hierarchy)

---

## Table of Contents

1. [What Is Arcane Academy](#1-what-is-arcane-academy)
2. [Canonical Hierarchy](#2-canonical-hierarchy)
3. [Tech Stack & Deployment Topology](#3-tech-stack--deployment-topology)
4. [Repository Structure](#4-repository-structure)
5. [Backend Architecture](#5-backend-architecture)
6. [Frontend Architecture](#6-frontend-architecture)
7. [Content System](#7-content-system)
8. [Learning Flow & Phases](#8-learning-flow--phases)
9. [Practice Types](#9-practice-types)
10. [Gamification & XP](#10-gamification--xp)
11. [AI Integration](#11-ai-integration)
12. [Auth & Subscriptions](#12-auth--subscriptions)
13. [Decision Log](#13-decision-log)
14. [Known Constraints & Watch-outs](#14-known-constraints--watch-outs)
15. [Pending Work (Phases 2–7)](#15-pending-work-phases-27)

---

## 1. What Is Arcane Academy

A degree-level, self-paced learning platform for polymath development. Learners progress through structured knowledge domains organised into Schools, earn XP and badges, and are guided by an AI mentor (Archmage Veylan). The learning model is spaced-repetition based with explicit encoding phases per lesson.

---

## 2. Canonical Hierarchy

```
School
 └─ Domain               e.g. Software Engineering
     └─ Tier             e.g. Apprentice  ← enum grouping, not a DB table
         └─ Module       e.g. Module 1: Foundations of Computation
             └─ Topic    e.g. Topic 1: Computational Thinking  ← NEW (V29)
                 └─ Lesson   e.g. Lesson 1: What is a Program?
                     └─ Lesson Content (sections inside one lesson):
                        Hook · Lore Introduction · Core Learning
                        (Concept · Why It Matters · Worked Examples ·
                         Common Mistakes · Mental Model · Mini Summary) ·
                        Guided Practice Quest · Solo Practice Quest ·
                        Retrieval · Integration · Lore Conclusion
```

### Notes

- **6 structural levels** — School → Domain → Tier → Module → Topic → Lesson.
- **Tier** is an enum on `LearningModule` and `Lesson` — UI groups by it, no separate table.
- **Topic** (V29) is a concept-cluster table that groups Lessons within a Module. Every Module receives one default Topic (`moduleId + "-default-topic"`) via the V29 migration backfill and the seeder. Fine-grained Topics are authored in Phase 2 (Markdown pipeline).
- **Lesson Content** sections (Hook, Core Learning, Guided Quest, Solo Quest, Retrieval, Integration, Lore Conclusion) are fields on `Lesson`, not hierarchy levels. Phase 2 adds the full section model.

### ⚠ Naming watch-out — "Topic"

"Topic" is now the **Module → Topic → Lesson** cluster. The old "Topic = Domain" naming from before May 2026 was fully removed in Phase 0. If you see `topicId` on a legacy API response it refers to a **Domain ID** (the old field was kept as `@JsonAlias` in `ChunkContentDto` and may appear in old content JSON). New code uses `domainId` for the Domain and `topicId` for the Topic cluster.

### Active domains (71 lessons each)

| Domain | ID | Practice Type | School | Guild |
|---|---|---|---|---|
| Java | `java` | JAVA (code runner) | Engineering & Systems | Guild of Systems Architects |
| Tailwind CSS | `tailwind` | TAILWIND (HTML editor) | Engineering & Systems | Guild of Artisan Interfaces |
| React | `react` | REACT (JSX sandbox) | Engineering & Systems | Guild of Systems Architects |
| Psychology | `psychology` | NONE (written) | Human Systems | Order of Minds |
| Natural Sciences | `sciences` | NONE (written) | Mathematical & Scientific | Observatory of Nature |
| Genealogy | `genealogy` | NONE (written) | Heritage | Keepers of Lineage |

---

## 3. Tech Stack & Deployment Topology

### Stack

| Layer | Technology |
|---|---|
| Frontend | React 18, TypeScript, Vite, React Router 6, Tailwind CSS 4, Shadcn/ui |
| Backend | Spring Boot 3.3, Java 21, Spring Modulith (package-by-feature) |
| Database | PostgreSQL (Neon, serverless) |
| Auth | JWT (jjwt 0.12) + Google OAuth2 |
| AI | Anthropic Claude API (proxied through backend — key never reaches client) |
| Payments | Stripe (live keys in production) |
| Code execution | In-process `JavaCodeRunner` (Phase 5 will introduce Docker sandbox behind `CodeExecutionPort`) |
| Spacing | SM-2 algorithm in `SpacingService` (Phase 5 will replace with FSRS) |

### Deployment

| Service | Platform | Notes |
|---|---|---|
| Backend | Google Cloud Run | Auto-scales to zero; stateless |
| Frontend | Netlify | Static build from Vite |
| Database | Neon Postgres | Serverless; connection pooling via Neon |
| Secrets | Cloud Run env vars | `cloud-run.env.yaml` — **never commit** |

### Critical: secrets locations

- `.env` — local dev only, never committed
- `cloud-run.env.yaml` — Cloud Run deployment config, never committed
- Contains: Stripe live secret key, GROQ key, Anthropic key, Google OAuth secret, JWT secret, Neon DB password

---

## 4. Repository Structure

```
arcane-academy/
├── CLAUDE.md                        # AI assistant instructions
├── PROJECT_REFERENCE.md             # ← this file
├── docker-compose.yml               # Local dev: backend + postgres
├── netlify.toml                     # Netlify SPA routing config
├── frontend/
│   └── src/
│       ├── features/                # Feature-sliced: each domain owns its pages
│       │   ├── learning/            # EncodingPage, editors, AiMentorPanel
│       │   ├── domains/             # DomainsPage, DomainPage, ModuleMapPage
│       │   │   ├── pages/           # (was features/topics/ — renamed Phase 0)
│       │   │   └── data/domains.ts  # Domain metadata, DOMAINS array, School enum
│       │   ├── auth/                # Login, Register, OAuth callback, LandingPage
│       │   ├── home/                # Dashboard / HomePage
│       │   ├── admin/               # Admin CRUD pages
│       │   ├── diagnostic/          # Domain diagnostic quiz (DomainDiagnosticPage)
│       │   ├── onboarding/          # DomainOnboardingPage, PrerequisiteCheckPage
│       │   ├── review/              # Spaced-repetition review page
│       │   ├── profile/             # User + public profile
│       │   ├── payment/             # UpgradeModal, Stripe checkout
│       │   └── exploration/         # Rabbit holes, Curiosity Queue
│       ├── shared/
│       │   ├── types/index.ts       # All TypeScript interfaces (Topic, ModuleDetail, LessonEncoding…)
│       │   ├── api/services.ts      # All frontend API calls
│       │   ├── api/adminServices.ts # Admin API calls (AdminDomain, AdminTopic, AdminChunk…)
│       │   └── hooks/useAuth.tsx    # Auth context
│       └── components/              # Shared layout, DomainIcon, TierIcon, Nav
└── backend/
    └── src/main/
        ├── java/com/ambravate/arcane/academy/
        │   ├── ai/                  # AiMentorService, RetrievalService, SpacingService, FeynmanService
        │   ├── auth/                # JWT, OAuth2, UserRepository, seeder
        │   ├── capstone/            # Capstone project saves
        │   ├── common/              # Shared entities (Domain, LearningModule, Lesson, Topic, School)
        │   │                        # enums, events, security, config
        │   ├── content/             # DomainSeeder, JsonContentSeeder, TopicRepository
        │   │                        # LessonRepository, LearningModuleRepository, DomainRepository
        │   │                        # ModuleController, ModuleGraphService
        │   ├── gamification/        # BadgeService, GamificationFacade (public API only)
        │   ├── notes/               # User notes on lessons
        │   ├── payment/             # Stripe integration
        │   ├── practice/            # EncodingService, TailwindPracticeService, ReactPracticeService…
        │   └── profile/             # User profile, domain enrolment, leaderboard
        └── resources/
            ├── content/             # JSON lesson files (see §7)
            └── db/migration/        # Flyway migrations V1–V29+
```

---

## 5. Backend Architecture

### Module boundaries (Spring Modulith)

Each top-level package is a module. Cross-module communication rules:

- **Allowed:** depend on `common/` from anywhere
- **Allowed:** depend on `gamification.api.GamificationFacade` interface only — never `gamification.service.*`
- **Forbidden:** `practice` → `gamification` directly (would create a cycle)
- **Pattern:** callers pass pre-fetched data into gamification to avoid N+1 DB fetches

### Key services

| Service | Responsibility |
|---|---|
| `EncodingService` | Phase state machine, lesson submission, written-response grading |
| `JsonContentSeeder` | Reads JSON from `resources/content/` and upserts into DB on startup; also upserts default Topics |
| `DomainSeeder` | Seeds default Domain records on startup |
| `ModuleGraphService` | Computes module unlock status (LOCKED/UNLOCKED/IN_PROGRESS/COMPLETE) |
| `AiMentorService` | Proxies to Anthropic API; compile error explanations, feedback, Feynman grading |
| `RetrievalService` | Builds and grades retrieval check question sets |
| `SpacingService` | SM-2 spaced-repetition interval calculation (Phase 5 → FSRS) |
| `FeynmanService` | AI-grades free-text "teach it back" responses |
| `BadgeService` | Evaluates all badge conditions; awards new badges |
| `TailwindPracticeService` | Validates Tailwind CSS class presence in HTML |
| `ReactPracticeService` | Accepts client-side DOM test results; awards XP |
| `SqlPracticeService` | Accepts client-side SQL test results; awards XP |
| `RPracticeService` | Accepts client-side R (WebR) test results; awards XP |

### API paths

| Path | Purpose |
|---|---|
| `GET /api/modules` | List all modules with status for the authenticated user |
| `GET /api/modules/{id}` | Module detail with topic list and lesson list (topicId/topicTitle per lesson) |
| `GET /api/admin/topics` | List topics (filter by `?moduleId=`) |
| `POST/PUT/DELETE /api/admin/topics/{id}` | Topic CRUD |
| `GET /api/admin/modules` | Admin module list |
| `GET /api/admin/domains` | Admin domain list |
| `/api/encoding/{lessonId}/...` | Lesson encoding phase endpoints |

### Phase advance guards

`EncodingService.advancePhase()` enforces:
1. `GUIDED_PRACTICE → SOLO_PRACTICE`: requires `guidedPracticePassed = true`
2. `SOLO_PRACTICE → RETRIEVAL_CHECK`: requires `soloPracticePassed = true`
3. `RETRIEVAL_CHECK → COMPLETE`: requires `retrievalCheckSubmitted = true`
4. `INTEGRATION` phase is skipped automatically when `integrationPrompt` is blank

---

## 6. Frontend Architecture

### Route map

| Route | Component | Notes |
|---|---|---|
| `/domains` | `DomainsPage` | All domains grid with Schools filter |
| `/domain/:domainId` | `DomainPage` | Domain dashboard; tier → module cards |
| `/domain/:domainId/onboarding` | `DomainOnboardingPage` | First-visit onboarding |
| `/domain/:domainId/diagnostic` | `DomainDiagnosticPage` | Entry diagnostic quiz |
| `/chunk/:moduleId` | `ModuleMapPage` | Lessons grouped by Topic |
| `/learn/:lessonId` | `EncodingPage` | Full lesson encoding experience |
| `/topics` | → redirect | Legacy URL → `/domains` |
| `/topic/*` | → redirect | Legacy URLs → `/domain/*` |

### EncodingPage

The central learning experience (`features/learning/pages/EncodingPage.tsx`, ~1500 lines). State is managed with `useReducer`. Phase 2+ will add new section rendering; Phase 3 adds `GuidedStepper` for step-by-step guided practice.

### ModuleMapPage — topic grouping

Lessons are grouped under `Topic` header rows when `topics` is non-empty in the `ModuleDetail` response. Falls back to a flat list for lessons without `topicId`. Each lesson card shows status, XP, memory-strength bar, and metadata chips.

---

## 7. Content System

### Current format (Phases 0–1)

Content files are **JSON**, seeded by `JsonContentSeeder` on startup (idempotent).

**File naming:** `{domain}-{tier}-{number}.json`

| Tier | Abbreviation |
|---|---|
| APPRENTICE | `app` |
| JUNIOR | `jun` |
| SENIOR | `sen` |
| LEAD | `lea` |

**File structure (key fields):**

```jsonc
{
  "id": "java-app-1",
  "title": "Computational Thinking",
  "glyph": "🧠",
  "sortOrder": 1,
  "tier": "APPRENTICE",
  "domainId": "java",           // also accepted as "topicId" (legacy alias)
  "prerequisites": [],
  "subChunks": [                // also accepted as "lessons"
    {
      "id": "java-app-1a",
      "title": "...",
      "sortOrder": 1,
      "xpReward": 50,
      "practiceType": "JAVA",   // JAVA | TAILWIND | REACT | SQL | R | NONE
      "guidedPracticeHtml": "...",
      "guidedPracticeStarterCode": "...",
      "guidedPracticeTestsJson": [...],
      "soloPracticeHtml": "...",
      "questions": [...],
      "integrationPrompt": "...",
      "questType": "GUIDED"
    }
  ]
}
```

### Phase 2 target format (Markdown-first)

**Coming in Phase 2.** Content will migrate to `.md` + YAML frontmatter in a directory tree `content/{domain}/{tier}/{module}/{topic}/{lesson}.md`. The full lesson section model (Hook · Lore Introduction · Core Learning sections · Guided Quest · Solo Quest · Retrieval · Integration · Lore Conclusion) will be parsed by `MarkdownLessonParser`.

### Content seeding

`JsonContentSeeder` runs on startup and upserts all JSON files. It is idempotent — safe to restart. For each module, it also upserts a **default Topic** (`moduleId + "-default-topic"`) and sets `topicId` on every seeded lesson.

**Rule:** Never put old source/migration files in the content directories. All files must be final, seeder-ready JSON.

### Question format

```jsonc
{
  "id": "java-app-1-1-q1",
  "type": "MULTIPLE_CHOICE",
  "tier": "RECALL",
  "prompt": "...",
  "options": ["A", "B", "C", "D"],
  "answer": "A",
  "explanation": "..."
}
```

---

## 8. Learning Flow & Phases

```
HOOK → EXPLANATION → GUIDED_PRACTICE → SOLO_PRACTICE → RETRIEVAL_CHECK → INTEGRATION → COMPLETE
```

| Phase | Purpose | Skip condition |
|---|---|---|
| HOOK | Narrative hook | Auto-skipped if `hookHtml` blank |
| EXPLANATION | Full lesson content | Always shown |
| GUIDED_PRACTICE | Scaffolded task | Gated: must pass before advancing |
| SOLO_PRACTICE | Independent rebuild, no hints | Skipped if `soloPracticeHtml` blank |
| RETRIEVAL_CHECK | Quiz from `questions` array | Auto-passed if no questions; 60% threshold |
| INTEGRATION | Cross-domain connection prompt | Skipped if `integrationPrompt` blank |
| COMPLETE | Summary + optional Feynman teach-back | Terminal state |

**Phase 3** will refactor GUIDED_PRACTICE into a step-by-step engine (Try → Hint → Compare → Reflection, deterministic marking).

**Phase 4** will refactor SOLO_PRACTICE into 4 typed assessment modes (Deterministic / Rubric+Reflection / Pattern-match / AI-review for Lead tier only).

### Quest types (optional field on Lesson)

`questType` (enum): `KNOWLEDGE | GUIDED | PRACTICE | INVESTIGATION | SYNTHESIS | MASTERY`. Shown as a gold badge in the EncodingPage lesson header. Content JSON declares `"questType": "GUIDED"`. Optional, backward-compatible.

---

## 9. Practice Types

| Type | Editor | Grading |
|---|---|---|
| `JAVA` | Code editor (textarea, Phase 3 → Monaco) | Backend: compiles + runs against test cases |
| `TAILWIND` | TailwindEditor (HTML) | Backend: CSS class presence check |
| `REACT` | ReactEditor (JSX iframe) | Client-side → backend awards XP |
| `SQL` | SqlEditor (sql.js iframe) | Client-side → backend awards XP |
| `R` | REditor (WebR iframe) | Client-side → backend awards XP |
| `NONE` | WrittenResponseEditor (textarea) | Backend: `gradeWrittenPractice` (pseudocode or prose branch) |

### JAVA code runner constraints

- Runs in-process via `javax.tools.JavaCompiler`, 5-second timeout
- `SandboxedClassLoader` blocks: file I/O, `java.net.*`, `java.nio.file.*`, reflection, Spring classpath
- Pre-compile scan rejects `System.exit`, `Runtime.getRuntime`, `ProcessBuilder`
- Phase 5 will introduce `CodeExecutionPort` interface + `DockerSandboxRunner` (config-flip, no forced cost)

---

## 10. Gamification & XP

### XP awards

| Event | XP |
|---|---|
| Guided practice passed | `lesson.xpReward` (typically 50–350) |
| Retrieval check passed | 25 |
| Feynman teach-back | Variable (AI-graded, 20–50) |
| Solo practice | 0 (XP already earned in guided) |

### Rank thresholds

| XP | Rank |
|---|---|
| 0 | Novice |
| 800 | Apprentice |
| 2000 | Adept |
| 4000 | Mage |
| 6500 | Archmage |
| 8000 | Magus |
| 11000 | Lord Magus |

### Badges

`GamificationFacade.evaluateAndAwardBadges()` is called after every XP-earning event. The facade pattern isolates the gamification module — no other module may import from `gamification.service.*`.

---

## 11. AI Integration

All AI calls are proxied through the backend. The Anthropic API key never reaches the client. Prompt caching (`CacheControlEphemeral.Ttl.TTL_1H`) is enabled on system prompts.

| Service | Purpose |
|---|---|
| `AiMentorService.explainCompileError` | Socratic error explanation after compile failure |
| `AiMentorService.explainRuntimeError` | Socratic explanation after runtime error |
| `AiMentorService.getFeedback` | Hint after test failures |
| `FeynmanService.grade` | Scores free-text teach-back on 4 dimensions |
| `RetrievalService.gradeAnswers` | Grades short-answer retrieval check responses |

The AI mentor persona is **Archmage Veylan** — Socratic, does not give direct answers, uses the wizardry register.

**Phase 4** will add AI-review as a Solo Practice assessment mode (Lead tier only, quota-limited to 3/month). **Phase 6** will add an AI-assisted content authoring harness (offline, not runtime).

---

## 12. Auth & Subscriptions

### Auth flow

- Email/password: register → login → JWT issued
- Google OAuth2: redirect to `/oauth2/authorization/google` → `OAuthCallbackPage` receives JWT
- JWT stored in `localStorage` via `useAuth` context hook

### Subscription model

| Status | Access |
|---|---|
| `FREE` | One domain of their choice |
| `MONTHLY` | All domains |
| `ANNUAL` | All domains |
| `LIFETIME` | All domains |
| `CANCELLED` | Reverts to FREE behaviour |

Admins and users with `bypassPaywall = true` get full access regardless.

---

## 13. Decision Log

---

### [2026-05-30] Phase 1 — Topic hierarchy introduced

**Context:** The Curriculum Template defines `School → Domain → Tier → Module → Topic → Lesson`. Topic (a concept-cluster grouping related Lessons within a Module) was missing from the DB schema.

**Decision:** Added `topics` table (`id`, `moduleId`, `title`, `purposeHtml`, `learningOutcomesJson`, `sortOrder`). Added `lessons.topic_id` FK. V29 migration backfills one default Topic per existing module (`moduleId + "-default-topic"`). `JsonContentSeeder` upserts the default Topic per module and sets `topicId` on every seeded lesson. `ModuleController` populates `topicId`/`topicTitle` per lesson and returns the `topics` list in `ModuleDetailDto`. Frontend `ModuleMapPage` groups lessons under topic header rows.

**Fine-grained Topics** (multiple per module, authored in content files) will arrive in Phase 2 (Markdown pipeline). Until then, every module has exactly one default Topic.

---

### [2026-05-30] Phase 0 — Complete Domain-era rename

**Context:** "Topic" was the old name for Domain (renamed May 2026) but residuals remained (`TopicSeeder`, `TopicEntry`, `TopicEngagementItem`, serialised field names, `features/topics/` pages, `/topics` route). These would collide with the new Topic entity.

**Decision:** Full rename: `TopicSeeder→DomainSeeder`, `TopicEntry→DomainEntry`, `TopicEngagementItem→DomainEngagementItem`, serialised fields updated (`topics→domains`, `totalTopics→totalDomains`, `topicEngagement→domainEngagement`). Frontend: `features/topics/` consolidated into `features/domains/`; `TopicIcon→DomainIcon`; routes `/topics→/domains`, `/topic/:id→/domain/:id`; `LegacyTopicRedirect` component added for backward-compat bookmarks.

---

### [2026-05-29] Blueprint implementation — Integration phase, Quest types, Core 12 domains

**Decision:** Added `INTEGRATION` to `EncodingPhase` (skipped when `integrationPrompt` blank). Added `QuestType` enum. Extended `topics.ts` with 6 new coming-soon domains. V26 migration adds `integration_prompt` and `quest_type` to `lessons`.

---

### [2026-05-29] Full rename to blueprint terminology (Topic→Domain, Chunk→Module, SubChunk→Lesson)

**Decision:** `topics→domains`, `chunks→modules`, `sub_chunks→lessons`; API paths updated; frontend updated. V24 migration. Schools layer added (V25).

---

### [2026-05-28] Written practice grading: auto-detect pseudocode vs prose

**Decision:** Single `NONE` practice type handles both pseudocode and prose by detecting ≥3 uppercase algorithm keywords at grading time. No new `PSEUDOCODE` type needed.

---

### [2026-05-27] React/SQL/R practice graded client-side, server awards XP only

**Decision:** Client runs tests in iframe sandbox, serialises pass/fail, posts to backend. Backend trusts results and awards XP. Intentionally un-tamper-proof for learning content.

---

## 14. Known Constraints & Watch-outs

### Java code runner

- In-process `JavaCodeRunner`, 5-second wall-clock timeout backstop
- Not a container-per-run sandbox — suitable for learning; insufficient for competitive submissions
- **Phase 5:** `CodeExecutionPort` interface will allow a config-flip to `DockerSandboxRunner` (deployed to Oracle Cloud Always Free ARM VM or GCP free-tier e2-micro — **£0 until paying customers**)

### FSRS (Phase 5)

- Current spacing is SM-2 (`SpacingService.java`). Phase 5 replaces internals with FSRS while keeping the public method signatures unchanged.

### Neon Postgres (serverless)

- Cold-start latency on first connection (~200ms). HikariCP settings must match Neon's connection limits.

### Cloud Run auto-scaling to zero

- First request after idle: ~1–3 second cold-start. Keep-alive pings from frontend can mitigate.

### ContentSeeder idempotency

- Runs on every startup; upserts by ID. Changing a lesson's `"id"` creates a duplicate instead of updating. **Never rename a content file's `"id"` field after it's been deployed.**
- Topic default IDs follow the convention `{moduleId}-default-topic`. These are stable once created.

### Content file naming

**Current (Phases 0–1):** `{domain}-{tier}-{number}.json` (e.g. `java-app-1.json`).
**Phase 2+:** Markdown tree `content/{domain}/{tier}/{module}/{topic}/{lesson}.md`.

### Retrieval question format

Newer files use `{ "id", "type", "tier", "prompt", "options", "answer", "explanation" }`. Older Java files use `{ "type", "tier", "questionHtml", "options", "correctAnswer", "explanationHtml" }`. `ContentSeeder` handles both.

---

## 15. Pending Work (Phases 2–7)

| Phase | Summary | Status |
|---|---|---|
| **Phase 2** | Markdown-first content pipeline (`MarkdownLessonParser`, commonmark-java, full lesson section model in `Lesson` entity, `MarkdownContentSeeder`) | Pending |
| **Phase 3** | Guided-step engine (Try → Hint → Compare → Reflection, deterministic marking per step) + Monaco editor replacing the textarea `CodeEditor` | Pending |
| **Phase 4** | Solo assessment types: Deterministic / Rubric+Reflection / Pattern-match keyword NLP / AI-review (Lead only, quota-limited) | Pending |
| **Phase 5** | `CodeExecutionPort` abstraction + `DockerSandboxRunner` (£0 until customers); FSRS replacing SM-2 behind `SpacingService` interface | Pending |
| **Phase 6** | Content authoring at scale: AI-assisted generation harness + CI validation lint (sections/steps/tier reading-size bands); migrate 71 lessons/domain to Markdown then scale to 150–300/domain | Pending |
| **Phase 7** | Mermaid in-lesson diagrams + React Flow knowledge map (`CurriculumGraph.tsx`, cross-domain integration edges, `/knowledge-map` route) | Pending |
