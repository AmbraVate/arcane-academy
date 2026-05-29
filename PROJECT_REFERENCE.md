# Arcane Academy — Project Reference

> **Purpose:** Living reference for all engineers working on this codebase. Update this document at every significant decision point. Supersedes `README.md` for developer guidance.
>
> **Last updated:** 2026-05-29

---

## Table of Contents

1. [What Is Arcane Academy](#1-what-is-arcane-academy)
2. [Tech Stack & Deployment Topology](#2-tech-stack--deployment-topology)
3. [Repository Structure](#3-repository-structure)
4. [Backend Architecture](#4-backend-architecture)
5. [Frontend Architecture](#5-frontend-architecture)
6. [Content System](#6-content-system)
7. [Learning Flow (Encoding Phases)](#7-learning-flow-encoding-phases)
8. [Practice Types](#8-practice-types)
9. [Gamification & XP](#9-gamification--xp)
10. [AI Integration](#10-ai-integration)
11. [Auth & Subscriptions](#11-auth--subscriptions)
12. [Decision Log](#12-decision-log)
13. [Known Constraints & Watch-outs](#13-known-constraints--watch-outs)
14. [Pending Work](#14-pending-work)

---

## 1. What Is Arcane Academy

A gamified, multi-topic learning platform with a wizardry RPG aesthetic. Learners progress through structured content tiers, earn XP and badges, and are guided by an AI mentor. The learning model is spaced-repetition based (SM-2 algorithm) with explicit encoding phases per lesson.

**Current active topics (71 sub-chunks each):**

| Topic | ID | Practice Type | Notes |
|---|---|---|---|
| Java | `java` | JAVA (code runner) | Original topic; most complete |
| Tailwind CSS | `tailwind` | TAILWIND (HTML editor) | Live preview |
| React | `react` | REACT (JSX sandbox) | Client-side iframe execution |
| Psychology | `psychology` | NONE (written) | Prose essay responses |
| Natural Sciences | `sciences` | NONE (written) | Prose essay responses |
| Genealogy | `genealogy` | NONE (written) | Prose essay responses |

**Tier structure across all topics:** APPRENTICE → JUNIOR → SENIOR → LEAD (15 / 20 / 19 / 17 sub-chunks)

---

## 2. Tech Stack & Deployment Topology

### Stack

| Layer | Technology |
|---|---|
| Frontend | React 18, TypeScript, Vite, React Router, Tailwind CSS, Shadcn/ui |
| Backend | Spring Boot 3.3, Java 21, Spring Modulith (package-by-feature) |
| Database | PostgreSQL (Neon, serverless) |
| Auth | JWT (jjwt 0.12) + Google OAuth2 |
| AI | Anthropic Claude API (proxied through backend) |
| Payments | Stripe (live keys in production) |
| Code execution | In-process JavaCompiler + sandboxed thread (5 s timeout) |

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

## 3. Repository Structure

```
arcane-academy/
├── CLAUDE.md                        # AI assistant instructions (overrides defaults)
├── PROJECT_REFERENCE.md             # ← this file
├── docker-compose.yml               # Local dev: backend + postgres
├── netlify.toml                     # Netlify SPA routing config
├── frontend/
│   └── src/
│       ├── features/                # Feature-sliced: each domain owns its pages
│       │   ├── learning/            # EncodingPage, editors, AiMentorPanel
│       │   ├── topics/              # TopicPage, ChunkMapPage
│       │   ├── auth/                # Login, Register, OAuth callback
│       │   ├── home/                # Dashboard / HomePage
│       │   ├── admin/               # Admin CRUD pages
│       │   ├── diagnostic/          # Topic diagnostic quiz
│       │   ├── review/              # Spaced-repetition review page
│       │   ├── profile/             # User + public profile
│       │   ├── payment/             # UpgradeModal, Stripe checkout
│       │   └── exploration/         # Rabbit holes, Curiosity Queue
│       ├── shared/
│       │   ├── types/index.ts       # All TypeScript interfaces
│       │   ├── api/services.ts      # All frontend API calls
│       │   └── hooks/useAuth.tsx    # Auth context
│       └── components/              # Shared layout (Nav, BlizzardScene)
└── backend/
    └── src/main/
        ├── java/com/ambravate/arcane/academy/
        │   ├── ai/                  # AiMentorService, RetrievalService, SpacingService, FeynmanService
        │   ├── auth/                # JWT, OAuth2, UserRepository, seeder
        │   ├── capstone/            # Capstone project saves
        │   ├── common/              # Shared domain entities, enums, events, telemetry
        │   ├── content/             # ContentSeeder, SubChunkRepository, ChunkRepository
        │   ├── gamification/        # BadgeService, GamificationFacade (public API only)
        │   ├── notes/               # User notes on lessons
        │   ├── payment/             # Stripe integration
        │   ├── practice/            # EncodingService, TailwindPracticeService, ReactPracticeService, etc.
        │   └── profile/             # User profile, topic enrolment
        └── resources/
            ├── content/             # JSON lesson files (see §6)
            └── application.properties
```

---

## 4. Backend Architecture

### Module boundaries (Spring Modulith)

Each top-level package is a module. Cross-module communication rules:

- **Allowed:** depend on `common/` from anywhere
- **Allowed:** depend on `gamification.api.GamificationFacade` interface only — never `gamification.service.*`
- **Forbidden:** `practice` → `gamification` directly (would create a cycle)
- **Pattern:** callers pass pre-fetched data into gamification to avoid N+1 DB fetches

### Key services

| Service | Responsibility |
|---|---|
| `EncodingService` | Phase state machine, practice submission, written-response grading |
| `ContentSeeder` | Reads JSON files from `resources/content/` and upserts into DB on startup |
| `AiMentorService` | Proxies to Anthropic API; compile error explanations, feedback, Feynman grading |
| `RetrievalService` | Builds and grades retrieval check question sets |
| `SpacingService` | SM-2 spaced-repetition interval calculation |
| `FeynmanService` | AI-grades free-text "teach it back" responses |
| `BadgeService` | Evaluates all badge conditions; awards new badges |
| `TailwindPracticeService` | Validates Tailwind CSS class presence in HTML |
| `ReactPracticeService` | Accepts client-side DOM test results; awards XP |
| `SqlPracticeService` | Accepts client-side SQL test results; awards XP |
| `RPracticeService` | Accepts client-side R (WebR) test results; awards XP |

### Phase advance guards

`EncodingService.advancePhase()` enforces:
1. `GUIDED_PRACTICE → SOLO_PRACTICE`: requires `guidedPracticePassed = true` (if content exists)
2. `SOLO_PRACTICE → RETRIEVAL_CHECK`: requires `soloPracticePassed = true` (if content exists)
3. `RETRIEVAL_CHECK → COMPLETE`: requires `retrievalCheckSubmitted = true` (if questions exist)

### Written response grading (`gradeWrittenPractice`)

Called whenever `practiceType = NONE`. Two grading modes are automatically chosen:

| Mode | Detection | Checks |
|---|---|---|
| **Pseudocode** | ≥ 3 uppercase algorithm keywords (`ALGORITHM`, ` FOR `, ` IF `, ` SET `, `RETURN`, etc.) | Word count, lesson vocabulary, **algorithmic structure** (condition + action steps) |
| **Prose/essay** | Default (no algorithm keywords detected) | Word count, lesson vocabulary, **explanatory connectors** (because/therefore/however), solo: independent application |

> **Decision 2026-05-28:** Single `NONE` type handles both pseudocode (Java) and prose (psychology/sciences/genealogy) by detecting the response format at grading time. This avoids introducing a new `PSEUDOCODE` practice type and means content files need no changes. See §12 for rationale.

---

## 5. Frontend Architecture

### EncodingPage (`src/features/learning/pages/EncodingPage.tsx`)

The central learning experience. All phase rendering is in one file (~1500 lines). State is managed with `useReducer` (`EncodingState` + `EncodingAction`). Submit handlers are separate per practice type:

| Handler | Used for |
|---|---|
| `handleSubmitPractice` | JAVA or NONE (guided) |
| `handleSubmitSoloPractice` | JAVA or NONE (solo) |
| `handleSubmitTailwind` / `Solo` | TAILWIND |
| `handleSubmitReact` | REACT |
| `handleSubmitSql` | SQL |
| `handleSubmitR` | R |

### WrittenResponseEditor

Used when `practiceType === 'NONE'`. Shows a free-text textarea with word count. The editor is **not** a code editor — it's a plain `<textarea>` styled with `font-crimson`. No syntax highlighting.

**Important:** The placeholder text currently says "explain your reasoning" regardless of whether the task asks for pseudocode or prose. This is intentional — it's generic enough not to mislead either way. The task panel (left side) has the actual instructions.

### Model answer panels

- `guidedPracticeModelAnswer` — shown **after** guided practice passes
- `modelAnswer` — shown **after** solo practice passes (collapsible — encourages genuine attempt first)

Both are only sent to the frontend after the relevant practice phase is marked as passed. The backend withholds them until then.

---

## 6. Content System

### File naming convention

```
{topic}-{tier}-{number}.json

Examples:
  java-app-1.json         ← Java Apprentice 1
  tw-lea-17.json          ← Tailwind Lead 17
  rx-jun-5.json           ← React Junior 5
  psy-sen-12.json         ← Psychology Senior 12
```

| Tier | Abbreviation | Sub-chunks |
|---|---|---|
| APPRENTICE | `app` | 15 |
| JUNIOR | `jun` | 20 |
| SENIOR | `sen` | 19 |
| LEAD | `lea` | 17 |

### File structure

```jsonc
{
  "id": "java-app-1",              // must match filename stem
  "title": "Computational Thinking",
  "glyph": "🧠",
  "sortOrder": 1,
  "tier": "APPRENTICE",
  "topicId": "java",
  "prerequisites": [],             // array of chunk IDs that must be complete
  "subChunks": [
    {
      "id": "java-app-1a",         // subChunk ID
      "title": "...",
      "sortOrder": 1,
      "xpReward": 50,
      "practiceType": "JAVA",      // JAVA | TAILWIND | REACT | SQL | R | NONE
      "filename": "Main.java",     // file shown in editor tab
      "hookHtml": "...",
      "explanationHtml": "...",
      "storyBeats": [...],
      "learningObjectives": [...],
      "commonMistakes": [...],
      "assessmentCriteria": [...],
      "feynmanPrompt": "...",
      "guidedPracticeHtml": "...",
      "guidedPracticeModelAnswer": "...",  // NONE type only
      "guidedPracticeStarterCode": "...",  // JAVA type: starter code in editor
      "guidedPracticeTestsJson": [...],    // JAVA type: test cases
      "soloPracticeHtml": "...",
      "modelAnswer": "...",               // NONE type only
      "downloadables": [...],
      "questions": [...]           // retrieval check questions
    }
  ]
}
```

### Question format (all content files must use this)

```jsonc
{
  "id": "java-app-1-1-q1",        // {subChunkId}-q{n}
  "type": "MULTIPLE_CHOICE",      // or TRUE_FALSE
  "tier": "RECALL",               // RECALL | APPLICATION | DISCRIMINATION
  "minPath": "APPRENTICE",        // minimum tier to see this question
  "prompt": "...",
  "options": ["A", "B", "C", "D"],
  "answer": "A",
  "explanation": "..."
}
```

> **Watch-out:** Older Java content files (pre-migration) use `questionHtml`, `correctAnswer`, `explanationHtml` without `id` or `minPath`. These are being migrated progressively. The seeder handles both formats via `ContentSeeder`.

### Content seeding

`ContentSeeder` runs on startup and upserts all JSON files into the database. It is idempotent — safe to restart. Seeder reads `backend/src/main/resources/content/**/*.json`.

**Rule:** Never put old source/migration files in the content directories. All files must be final, seeder-ready JSON. Old source files (`tw-a.json`, `rx-a.json`, etc.) have been deleted.

---

## 7. Learning Flow (Encoding Phases)

```
HOOK → EXPLANATION → GUIDED_PRACTICE → SOLO_PRACTICE → RETRIEVAL_CHECK → COMPLETE
```

| Phase | Purpose | Can skip? |
|---|---|---|
| HOOK | Narrative introduction; motivational hook | Auto-skipped if `hookHtml` is blank |
| EXPLANATION | Full lesson content with story beats | Always shown |
| GUIDED_PRACTICE | Scaffolded task (starter code / worked example) | Gated: must pass before advancing |
| SOLO_PRACTICE | Same task rebuilt from memory, no hints | Gated if `soloPracticeHtml` exists |
| RETRIEVAL_CHECK | Quiz drawn from `questions` array | Gated if questions exist; 60% pass threshold |
| COMPLETE | Summary; Feynman teach-back (optional, earns XP) | Terminal state |

**Phase skip logic:** GUIDED_PRACTICE is always entered. SOLO_PRACTICE is entered only if `soloPracticeHtml` is non-blank. If a subChunk has no questions, RETRIEVAL_CHECK is auto-passed.

---

## 8. Practice Types

| Type | Editor | Submission | Grading |
|---|---|---|---|
| `JAVA` | Monaco / CodeEditor | POST to backend | JavaCodeRunner compiles + runs against test cases |
| `TAILWIND` | TailwindEditor (HTML) | POST to backend | TailwindPracticeService checks CSS class presence via DOM |
| `REACT` | ReactEditor (JSX, iframe) | Client tests → POST to backend | ReactPracticeService awards XP; tests run client-side |
| `SQL` | SqlEditor (sql.js iframe) | Client tests → POST to backend | SqlPracticeService awards XP; tests run client-side |
| `R` | REditor (WebR iframe) | Client tests → POST to backend | RPracticeService awards XP; tests run client-side |
| `NONE` | WrittenResponseEditor (textarea) | POST to backend | gradeWrittenPractice (pseudocode or prose branch) |

### TAILWIND test format

```jsonc
// guidedPracticeTests array in content JSON
{ "label": "Grid: grid", "selector": ".my-grid", "requiredClass": "grid" }
```

### JAVA test format

```jsonc
// guidedPracticeTestsJson array in content JSON
{ "label": "Outputs Hello", "input": null, "expected": "Hello" }
```

---

## 9. Gamification & XP

### XP awards

| Event | XP |
|---|---|
| Guided practice passed | `subChunk.xpReward` (typically 50–350) |
| Retrieval check passed | 25 |
| Feynman teach-back | Variable (AI-graded, typically 20–50) |
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

`GamificationFacade.evaluateAndAwardBadges()` is called after every XP-earning event. Badge conditions are defined in `BadgeService`. The facade pattern isolates the gamification module — no other module may import from `gamification.service.*`.

---

## 10. AI Integration

All AI calls are proxied through the backend. The Anthropic API key never reaches the client.

| Service | Model | Purpose |
|---|---|---|
| `AiMentorService.explainCompileError` | Claude | Socratic error explanation after compile failure |
| `AiMentorService.explainRuntimeError` | Claude | Socratic explanation after runtime error |
| `AiMentorService.getFeedback` | Claude | Hint after test failures |
| `FeynmanService.grade` | Claude | Scores free-text teach-back on 4 dimensions |
| `RetrievalService.gradeAnswers` | Claude | Grades short-answer retrieval check responses |

The AI mentor persona is **Archmage Veylan** — Socratic, does not give direct answers, uses the wizardry register.

---

## 11. Auth & Subscriptions

### Auth flow

- Email/password: register → login → JWT issued
- Google OAuth2: redirect to `/oauth2/authorization/google` → `OAuthCallbackPage` receives JWT
- JWT stored in `localStorage` via `useAuth` context hook

### Subscription model

| Status | Access |
|---|---|
| `FREE` | One topic of their choice |
| `MONTHLY` | All topics |
| `ANNUAL` | All topics |
| `LIFETIME` | All topics |
| `CANCELLED` | Reverts to FREE behaviour |

Admins and users with `bypassPaywall = true` get full access regardless.

Stripe webhooks (`StripeWebhookController`) update subscription status on `checkout.session.completed`, `customer.subscription.deleted`, etc.

---

## 12. Decision Log

Decisions are recorded here with date, context, and rationale. Reference this before making changes that touch the same areas.

---

### [2026-05-28] Written practice grading: auto-detect pseudocode vs prose

**Context:** `practiceType = NONE` is used for both pseudocode tasks (Java Apprentice 1: write an algorithm) and prose/essay tasks (Psychology: explain a concept). The original `gradeWrittenPractice` had a single rubric requiring explanatory prose connectors ("because", "therefore") which pseudocode structurally never contains.

**Decision:** Keep `NONE` as a single type. Add `looksLikePseudocode(answer)` detection in `gradeWrittenPractice` that checks for ≥ 3 uppercase algorithm keywords. Pseudocode answers get "Shows algorithmic structure" (condition + action check) instead of "Explains rather than lists" (connector check). The solo "Applies the idea independently" check is skipped for pseudocode.

**Rationale:** Adding a new `PSEUDOCODE` practice type would require updating ~32 Java content files and the `SubChunkPracticeType` enum. Detection at grading time costs nothing and is invisible to content authors.

**File changed:** `backend/.../practice/service/EncodingService.java`

---

### [2026-05-28] Content file naming standardised to tier-abbreviation scheme

**Context:** Original content was in monolithic `tw-a.json`, `rx-a.json` style files. Migrated to `{topic}-{tier-abbr}-{n}.json` to match Java's `java-app-1.json` pattern.

**Decision:** All content files now follow `{topic}-{tier}-{n}.json`. Old source files (`tw-a/b/c/d.json`, `rx-a/b/c/d.json`, build scripts `build-rx-*.js`) are deleted.

**Mapping:**

| Tier | Abbreviation |
|---|---|
| APPRENTICE | `app` |
| JUNIOR | `jun` |
| SENIOR | `sen` |
| LEAD | `lea` |

---

### [2026-05-27] React/SQL/R practice graded client-side, server awards XP only

**Context:** Running JSX, SQL, and R server-side would require heavyweight sandboxing. React already has a Babel CDN iframe approach; SQL has sql.js-wasm; R has WebR.

**Decision:** Client runs the tests in the iframe sandbox, serialises pass/fail results, posts them to the backend. The backend trusts the results and awards XP. This is intentionally un-tamper-proof for learning content — cheating here harms only the learner.

**Files:** `ReactPracticeService`, `SqlPracticeService`, `RPracticeService`

---

### [2026-05-XX] Gamification uses facade pattern to avoid module cycle

**Context:** `EncodingService` (in `practice`) needed to award badges after XP events. `BadgeService` (in `gamification`) needed `UserChunkProgress` data from `practice`. Direct cross-module dependency would create a cycle.

**Decision:** `gamification` exposes only `GamificationFacade` interface. Callers in `practice` pass pre-fetched `List<UserChunkProgress>` and `List<ReviewSession>` into the facade method. Gamification module never queries `practice` repositories directly.

---

### [2026-05-XX] Tier progression only auto-advances for Java topic

**Context:** `EncodingService.advanceLearnerPathIfTierComplete()` advances `UserLearnerProfile.currentPath` when a tier is fully completed. Non-Java topics don't have a `UserLearnerProfile` concept (they use `UserTopicProfile`).

**Decision:** Tier auto-advance is guarded by `if (!"java".equals(topicId)) return;`. Non-Java topics do not auto-advance learner path — their progress is tracked in `UserTopicProfile`.

**Implication:** When non-Java topics need tier progression in the future, this method must be extended.

---

### [2026-05-XX] Content depth target: 71 sub-chunks per topic

**Context:** Java was built first with 71 sub-chunks (15/20/19/17 across tiers). All subsequent topics target the same depth for feature parity on the topic map.

**Decision:** 71 is the canonical depth. `topics.ts` hard-codes `chunks: 71` for active topics. SQL and other coming-soon topics start with fewer until full content is built.

---

## 13. Known Constraints & Watch-outs

### Java code runner

- Runs in-process using `javax.tools.JavaCompiler` with a 5-second timeout
- Student code is wrapped in a `StudentSolution` class before compilation
- Output is captured via a `ThreadLocalPrintStream` installed at startup — concurrent runs are fully isolated (no `System.setOut` race)
- `SandboxedClassLoader` (null parent = bootstrap only) blocks: file I/O, `java.net.*`, `java.nio.file.*`, `java.lang.reflect.*`, `ProcessBuilder`, `Runtime`, and the entire Spring classpath
- Pre-compile source scan rejects `System.exit`, `Runtime.getRuntime`, `ProcessBuilder`, and `System.set{Out,Err,In}`
- **Not** a container-per-run sandbox — suitable for learning content; insufficient for competitive submissions
- Remaining risk: tight infinite loops (`while(true){}`) are not interruptible; 5s timeout is the backstop
- Remaining risk: `System.exit()` from obfuscated reflection is blocked at classloader level (reflect.* blocked), but direct call is caught by pre-compile scan only

### Neon Postgres (serverless)

- Cold-start latency on first connection (~200ms)
- Connection pool is managed by Neon's proxy — Spring's HikariCP settings need to match Neon's limits
- DB password is in `.env` / `cloud-run.env.yaml` — never commit

### Cloud Run auto-scaling to zero

- First request after idle period has cold-start latency (~1–3 seconds)
- Keep-alive pings from frontend can mitigate this if needed

### ContentSeeder idempotency

- Runs on every startup; upserts by ID
- Changing a sub-chunk's `id` creates a duplicate instead of updating
- Never rename a content file's `"id"` field after it's been deployed

### Retrieval question format inconsistency

- Newer files: `{ "id", "type", "tier", "minPath", "prompt", "options", "answer", "explanation" }`
- Older Java files: `{ "type", "tier", "questionHtml", "options", "correctAnswer", "explanationHtml" }` (no `id`, no `minPath`)
- `ContentSeeder` handles both. Migration to new format is ongoing.

### CAPSTONE_CHUNK_IDS in EncodingPage

- Hard-coded set: `java-app-15`, `java-jun-20`, `java-sen-19`, `java-lea-17`
- When new capstone chunks are added (for Tailwind, React, etc.), this set must be updated manually
- Location: `EncodingPage.tsx` line ~7

---

### [2026-05-29] Production security review completed

**Context:** Full pre-scale security audit of auth, input validation, CORS, secrets, admin authorization, rate limiting, and Stripe webhook handling.

**Findings fixed (HIGH):**
- `@Valid` added to all 4 `EncodingController` `@RequestBody` parameters — validation was declared but never enforced
- `AnswerRequest` and `FeynmanRequest` DTOs now have `@NotNull`/`@NotBlank`/`@Size` constraints (including cascading `@Valid` on nested `AnswerItem` list)
- Removed hardcoded `JWT_SECRET` and Stripe key defaults from `application.yml` — startup now fails fast if env vars are unset (`.env` already has them for local dev)

**Findings fixed (MEDIUM):**
- CORS `allowedHeaders` changed from `List.of("*")` to explicit `["Content-Type", "Authorization", "X-Requested-With"]`
- CORS origins now trimmed after split (prevents whitespace-misconfiguration bug)
- `@EnableMethodSecurity` added to `SecurityConfig`; `@PreAuthorize("hasRole('ADMIN')")` added to all 8 admin controllers (defense-in-depth on top of URL-pattern guard)
- Rate limiting extended to `/api/admin/**` (30 req/min per userId, configurable via `RATELIMIT_ADMIN_*` env vars)

**Findings fixed (LOW):**
- `@Size(max=10000)` added to `testInput` in `CodeRunRequest`
- `session.getMetadata()` null-guarded in `PaymentService.handleCheckoutCompleted` before map lookups

**Known remaining risks (documented, not fixed in this pass):**
- `JavaCodeRunner` sandbox hardened 2026-05-29 (ThreadLocalPrintStream, SandboxedClassLoader, pre-compile scan). Remaining: tight infinite loops not interruptible; full process isolation deferred.
- `refreshToken` is returned in `AuthResponse` and stored in localStorage — a full cookie-based token flow is a larger refactor deferred to a future sprint

---

### [2026-05-29] Psychology content prerequisite IDs used stale tier abbreviations

**Context:** 12 Psychology JSON content files used old tier abbreviations (`fnd`, `prt`, `adv`) in their `prerequisiteIds` fields, referencing chunk IDs that do not exist. This caused `JsonContentSeeder.wirePrerequisites` to throw `IllegalStateException: Prereq chunk not found` on every startup, preventing the backend from booting.

**Root cause:** Content was authored with an earlier tier naming scheme (foundation/practitioner/advanced) before the current scheme (app/jun/sen/lea) was standardised.

**Fix:** Replaced all stale prerequisite IDs using the mapping: `fnd`→`app`, `prt`→`jun`, `adv`→`sen`. Also corrected `psy-jun-3.json` whose `tier` field was incorrectly set to `APPRENTICE` (should be `JUNIOR`).

**Files changed:** `psy-app-3`, `psy-jun-2` through `psy-jun-10`, `psy-sen-2` through `psy-sen-4` (12 files).

**Watch-out:** When authoring new content, always use the current tier abbreviations (`app`, `jun`, `sen`, `lea`). Run the prerequisite validation script before pushing.

---

## 14. Pending Work

| Item | Priority | Notes |
|---|---|---|
| Migrate older Java question format to new format (`id`, `minPath`, `prompt`) | Medium | Affects all pre-2026 Java content files |
| Extend tier auto-advance to non-Java topics (`UserTopicProfile`) | Medium | Currently only Java advances `currentPath` |
| Add Tailwind / React capstone IDs to `CAPSTONE_CHUNK_IDS` | Low | When those capstones are confirmed |
| SQL content expansion (currently 8 files, target 71) | Low | Not yet started |
| Hard-coded Feynman persona references mentor by name | Low | Generic enough for non-Java topics |
| `looksLikePseudocode` detection for mixed-case responses | Low | Handles capitalised keywords; fully lowercase pseudocode would be misclassified as prose |

---

## 15. Improvements Backlog

| Item | Priority | Notes |
|---|---|---|
| Observability & stats metrics in Admin Panel | High | Usage dashboards, learner activity, XP distribution, engagement metrics |
| Sync topic order in Admin Panel to lesson sort order | Medium | Admin chunk list should reflect the same ordering learners see on the topic map |
| SEO | Medium | Meta tags, Open Graph, sitemap, structured data; important for organic growth |
| Additional topic content (polymathic expansion) | Medium | Extend beyond current 6 topics to broaden the polymathic offering |
| Production security review | ~~High~~ Done | Completed 2026-05-29 — see Decision Log §[2026-05-29] |
| AI optimisation (prompt caching, token efficiency) | Medium | Enable Anthropic prompt caching headers; reduce per-request token cost; review model selection per use case |
| User location for leaderboard gamification | Low | Optional location field on profile; regional / global leaderboard segments |
| Icons, images, and infographics | Medium | Replace placeholder or text-only UI elements with branded iconography and explanatory visuals to improve comprehension and polish |
