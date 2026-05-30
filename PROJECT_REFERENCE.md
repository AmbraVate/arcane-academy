# Arcane Academy — Project Reference

> **Purpose:** Living reference for all engineers working on this codebase. Update this document at every significant decision point.
>
> **Last updated:** 2026-05-30 (Phase 7 — Knowledge maps · all 8 phases complete)

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
15. [Completed Restructure Summary](#15-completed-restructure-summary)

---

## 1. What Is Arcane Academy

A degree-level, self-paced learning platform for polymath development. Learners progress through structured knowledge domains organised into Schools, earn XP and badges, and are guided by an AI mentor (Archmage Veylan). The learning model uses FSRS spaced-repetition with explicit encoding phases per lesson, interactive guided-step quests, and a cross-domain knowledge map.

---

## 2. Canonical Hierarchy

```
School
 └─ Domain               e.g. Software Engineering
     └─ Tier             e.g. Apprentice  ← enum grouping, not a DB table
         └─ Module       e.g. Module 1: Foundations of Computation
             └─ Topic    e.g. Topic 1: Computational Thinking
                 └─ Lesson   e.g. Lesson 1: What is a Program?
                     └─ Lesson Content (sections inside one lesson):
                        Hook · Lore Introduction · Core Learning
                        (Concept Introduction · Why It Matters · Worked Examples ·
                         Common Mistakes · Mental Model · Mini Summary) ·
                        Guided Practice Quest · Solo Practice Quest ·
                        Retrieval · Integration · Lore Conclusion
```

### Notes

- **6 structural levels** — School → Domain → Tier → Module → Topic → Lesson.
- **Tier** is an enum on `LearningModule` and `Lesson` — UI groups by it, no separate table.
- **Topic** (V29) is a concept-cluster table grouping Lessons within a Module. Every Module has at least one default Topic (`moduleId + "-default-topic"`) from the V29 backfill.
- **Lesson Content** sections are fields on `Lesson`, not hierarchy levels.

### ⚠ Naming watch-out — "Topic"

"Topic" is now the **Module → Topic → Lesson** cluster. The old "Topic = Domain" naming from before May 2026 was fully removed in Phase 0. The `@JsonAlias("topicId")` on legacy content DTOs refers to **Domain ID**. New code uses `domainId` for the Domain and `topicId` for the Topic cluster.

### Active domains

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
| State | TanStack Query (React Query v5) |
| Rich editors | Monaco (`@monaco-editor/react`) for code; Mermaid for diagrams |
| Knowledge map | `@xyflow/react` (React Flow v12) |
| Backend | Spring Boot 3.3, Java 21, Spring Modulith (package-by-feature) |
| Database | PostgreSQL (Neon, serverless) |
| Migrations | Flyway (V1–V33+) |
| Auth | JWT (jjwt 0.12) + Google OAuth2 |
| AI | Anthropic Claude API (proxied through backend — key never reaches client) |
| Payments | Stripe (live keys in production) |
| Code execution | `JavaCodeRunner` (in-process, default) behind `CodeExecutionPort` interface; `DockerSandboxRunner` available via `SANDBOX_MODE=docker` |
| Spacing | FSRS-4.5 algorithm in `FsrsAlgorithm` + `SpacingService` |

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
│       ├── features/
│       │   ├── learning/            # EncodingPage, GuidedStepper, SoloAssessmentPanel
│       │   │   └── components/
│       │   │       ├── guided/      # FillBlankInput, McqInput, ShortTextInput, etc.
│       │   │       ├── MonacoCodeEditor.tsx
│       │   │       ├── MermaidBlock.tsx   # Mermaid renderer + useMermaidHydration hook
│       │   │       └── SoloAssessmentPanel.tsx
│       │   ├── knowledge-map/       # /knowledge-map route (Phase 7)
│       │   │   ├── components/CurriculumGraph.tsx
│       │   │   └── pages/KnowledgeMapPage.tsx
│       │   ├── domains/             # DomainsPage, DomainPage, ModuleMapPage
│       │   ├── auth/                # Login, Register, OAuth callback, LandingPage
│       │   ├── home/                # HomePage
│       │   ├── admin/               # Admin CRUD pages
│       │   ├── diagnostic/          # Domain diagnostic quiz
│       │   ├── onboarding/          # DomainOnboardingPage, PrerequisiteCheckPage
│       │   ├── review/              # Spaced-repetition review page
│       │   ├── profile/             # User + public profile
│       │   ├── payment/             # UpgradeModal, Stripe checkout
│       │   └── exploration/         # Rabbit holes, Curiosity Queue
│       ├── shared/
│       │   ├── types/index.ts       # All TypeScript interfaces
│       │   ├── api/services.ts      # All frontend API calls (incl. graphApi)
│       │   ├── api/adminServices.ts # Admin API calls
│       │   └── hooks/useAuth.tsx    # Auth context
│       ├── hooks/
│       │   └── queries.ts           # TanStack Query hooks (useDashboard, useGraph, …)
│       └── components/              # Shared layout, DomainIcon, TierIcon, Nav
└── backend/
    └── src/
        ├── main/
        │   ├── java/com/ambravate/arcane/academy/
        │   │   ├── ai/              # FsrsAlgorithm, SpacingService, AiMentorService, FeynmanService
        │   │   ├── auth/            # JWT, OAuth2, UserRepository
        │   │   ├── capstone/        # Capstone project saves
        │   │   ├── common/          # Shared entities (Domain, LearningModule, Lesson, Topic, GuidedStep…)
        │   │   │                    # enums, events, security, config
        │   │   ├── content/         # Seeders, repositories, ModuleController, GraphController
        │   │   │   └── seeder/      # MarkdownLessonParser, MarkdownContentSeeder, MarkdownConverter
        │   │   ├── gamification/    # BadgeService, GamificationFacade
        │   │   ├── notes/           # User lesson notes
        │   │   ├── payment/         # Stripe integration
        │   │   ├── practice/        # EncodingService, GuidedStepService, SoloAssessmentService
        │   │   │   └── runner/      # CodeExecutionPort, JavaCodeRunner, DockerSandboxRunner
        │   │   ├── profile/         # User profile, domain enrolment, leaderboard
        │   │   └── tools/           # LessonAuthoringHarness (standalone, not Spring-scanned)
        │   └── resources/
        │       ├── content/         # Markdown lesson files (see §7)
        │       │   └── java/apprentice/{topic}/{id}.md
        │       └── db/migration/    # Flyway migrations V1–V33
        └── test/
            └── java/com/ambravate/arcane/academy/
                ├── ai/service/      # FsrsAlgorithmTest, SpacingServiceTest
                ├── content/         # MarkdownLessonValidationTest, GraphControllerTest, seeder/
                └── practice/        # GuidedStepServiceTest, SoloAssessmentServiceTest, DockerSandboxRunnerIntegrationTest
```

---

## 5. Backend Architecture

### Module boundaries (Spring Modulith)

Each top-level package is a module. Cross-module communication rules:

- **Allowed:** depend on `common/` from anywhere
- **Allowed:** depend on `gamification.api.GamificationFacade` interface only — never `gamification.service.*`
- **Forbidden:** `practice` → `gamification` directly
- **Pattern:** callers pass pre-fetched data into gamification to avoid N+1 DB fetches

### Key services

| Service | Responsibility |
|---|---|
| `EncodingService` | Phase state machine, lesson start/advance, guided→solo→retrieval guards |
| `GuidedStepService` | Per-step deterministic marking (EXACT / NORMALIZED / REGEX / CONTAINS / OUTPUT_MATCH) |
| `SoloAssessmentService` | Dispatches on `soloAssessmentType`: DETERMINISTIC / RUBRIC_REFLECTION / PATTERN_MATCH / AI_REVIEW |
| `KeywordScoringService` | Keyword-match scoring for PATTERN_MATCH solo assessment |
| `MarkdownLessonParser` | Parses YAML frontmatter + Markdown body into `MarkdownLessonDto`; reads guidedSteps + soloAssessment blocks |
| `MarkdownContentSeeder` | Scans `content/**/*.md`, calls parser, upserts Lessons + GuidedSteps + Topics into DB |
| `JsonContentSeeder` | Legacy JSON seeder — still runs for unmigrated JSON content |
| `DomainSeeder` | Seeds default Domain records on startup |
| `ModuleGraphService` | Computes module unlock status (LOCKED / UNLOCKED / IN_PROGRESS / COMPLETE) |
| `GraphController` | `GET /api/graph` — DOMAIN + MODULE + LESSON nodes + HIERARCHY / PREREQUISITE / INTEGRATION edges |
| `AiMentorService` | Proxies to Anthropic API; compile errors, feedback, Feynman grading |
| `RetrievalService` | Builds and grades retrieval check question sets |
| `FsrsAlgorithm` | Pure FSRS-4.5 scheduling: `review()`, `retrievability()`, `nextInterval()` |
| `SpacingService` | Wraps `FsrsAlgorithm`; public methods: `updateSpacing`, `computeDecayedStrength`, `getDueReviews` |
| `BadgeService` | Evaluates all badge conditions; awards via `GamificationFacade` |
| `JavaCodeRunner` | In-process Java compilation + execution; implements `CodeExecutionPort`; active when `sandbox.mode=inprocess` (default) |
| `DockerSandboxRunner` | Docker-per-run execution; activated via `SANDBOX_MODE=docker`; `--network=none`, `--memory=256m`, `--cpus=0.5`, `--pids-limit=64` |
| `TailwindPracticeService` | Validates Tailwind CSS class presence in submitted HTML |
| `ReactPracticeService` | Accepts client-side DOM test results; awards XP |
| `SqlPracticeService` | Accepts client-side SQL test results; awards XP |
| `RPracticeService` | Accepts client-side R (WebR) test results; awards XP |
| `LessonAuthoringHarness` | Standalone `main()` class in `tools/`; calls `claude-opus-4-5` to draft `.md` lessons; not Spring-scanned |

### API paths

| Path | Purpose |
|---|---|
| `GET /api/modules` | List all modules with status for the authenticated user |
| `GET /api/modules/{id}` | Module detail — topic list + lesson list with `topicId`/`topicTitle` |
| `GET /api/graph` | Full curriculum graph: nodes (DOMAIN/MODULE/LESSON) + edges (HIERARCHY/PREREQUISITE/INTEGRATION) |
| `GET /api/encoding/{id}/guided/steps` | Guided steps for a lesson |
| `POST /api/encoding/{id}/guided/steps/{stepId}/check` | Check a single guided step answer |
| `POST /api/encoding/{id}/solo-practice/submit` | Submit solo assessment (all types) |
| `GET /api/admin/topics` | List topics (filter by `?moduleId=`) |
| `POST/PUT/DELETE /api/admin/topics/{id}` | Topic CRUD |
| `/api/encoding/{lessonId}/...` | All lesson encoding phase endpoints |

### Flyway migration history (significant)

| Migration | Change |
|---|---|
| V24 | Topic → Domain rename (topics table → domains) |
| V25 | Schools layer added |
| V26 | `integration_prompt`, `quest_type` on lessons |
| V27 | Domain-era rename residuals cleanup |
| V28 | `topics` table (concept-cluster), `lessons.topic_id` FK, V29 backfill |
| V29 | Lesson section fields (lore_intro_html, why_it_matters_html, worked_examples_html, mental_model_html, mini_summary_html, lore_conclusion_html, integration_domains_json) |
| V30 | `guided_steps` table |
| V31 | `lessons.solo_assessment_type`, `rubric_items_json`, `keywords_json`, `solo_model_answer_html`; `user_chunk_progress.solo_confidence` |
| V32 | (reserved) |
| V33 | FSRS columns on `user_chunk_progress`: `fsrs_stability`, `fsrs_difficulty`, `fsrs_state`, `fsrs_lapses`, `fsrs_last_interval` |

### Phase advance guards

`EncodingService.advancePhase()` enforces:
1. `GUIDED_PRACTICE → SOLO_PRACTICE`: requires all guided steps passed (when steps exist)
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
| `/chunk/:moduleId` | `ModuleMapPage` | Lessons grouped by Topic header rows |
| `/learn/:lessonId` | `EncodingPage` | Full lesson encoding experience |
| `/knowledge-map` | `KnowledgeMapPage` | Interactive React Flow curriculum graph |
| `/review` | `ReviewPage` | FSRS-due spaced-repetition review |
| `/topics` | → redirect | Legacy URL → `/domains` |
| `/topic/*` | → redirect | Legacy URLs → `/domain/*` |

### EncodingPage

The central learning experience (`features/learning/pages/EncodingPage.tsx`). State managed with `useReducer`. Renders:

- **HOOK / LORE_INTRODUCTION / CORE_LEARNING** — section HTML from lesson fields
- **GUIDED_PRACTICE** — `GuidedStepper` (step workbook: Try → Hint → Compare → Reflection) when `hasGuidedSteps=true`; falls back to legacy guided-practice HTML for JSON-seeded lessons
- **SOLO_PRACTICE** — `SoloAssessmentPanel` dispatches on `soloAssessmentType`
- **RETRIEVAL_CHECK** — retrieval quiz
- **INTEGRATION** — cross-domain integration prompt HTML
- **COMPLETE** — summary + optional Feynman teach-back

### GuidedStepper

`features/learning/components/GuidedStepper.tsx` — one step at a time:
1. Render `instructionHtml` + appropriate input (`FillBlankInput` / `McqInput` / `ShortTextInput` / `CodeStepInput` / `DragDropInput` / `SequenceInput`)
2. POST to `/api/encoding/{id}/guided/steps/{stepId}/check`
3. Show inline pass/fail feedback
4. Progressive **Hint** reveal
5. **Compare answer** (reflectionPrompt after pass)
6. **Next** advance

### SoloAssessmentPanel

`features/learning/components/SoloAssessmentPanel.tsx` — switches on `soloAssessmentType`:

| Type | UI |
|---|---|
| `DETERMINISTIC` | Code editor + run → submit |
| `RUBRIC_REFLECTION` | Checklist of rubric items + confidence selector (Not/Partial/Mostly/Very) → model-answer reveal |
| `PATTERN_MATCH` | Text box → keyword-band feedback (WEAK / GOOD / EXCELLENT) + matched keyword highlight |
| `AI_REVIEW` | Text box → AI mentor feedback; quota badge shows remaining uses (3/month, Lead only) |

### CurriculumGraph

`features/knowledge-map/components/CurriculumGraph.tsx` — React Flow knowledge map:

- **3-row DAG layout**: Domains (y=0) → Modules (y=220) → Lessons (y=440)
- **Node colours**: Domain (gold border), Module (status-coloured), Lesson (FSRS-strength coloured: green/amber/red/slate)
- **Edge types**: HIERARCHY (solid gray), PREREQUISITE (dashed amber), INTEGRATION (dotted teal, animated)
- `GraphLegend` inline SVG legend component

### MermaidBlock

`features/learning/components/MermaidBlock.tsx`:

- `<MermaidBlock chart="…" />` — standalone component; dark theme; animated loading, graceful error fallback
- `useMermaidHydration(containerRef)` — post-processing hook: finds `<code class="language-mermaid">` in rendered HTML and replaces with inline SVG

### Monaco

`MonacoCodeEditor.tsx` — lazy-loaded Monaco editor; used by `CodeStepInput` (guided code steps) and JAVA/NONE `EncodingPage` editors.

---

## 7. Content System

### Dual-track (current)

Content is authored in one of two formats:

| Format | Source | Seeder |
|---|---|---|
| **Markdown** (new) | `resources/content/{domain}/{tier}/{topic}/{id}.md` | `MarkdownContentSeeder` |
| **JSON** (legacy) | `resources/content/{domain}-{tier}-{number}.json` | `JsonContentSeeder` |

Both seeders run on startup; they are idempotent (upsert by ID). The JSON seeder will be retired once all domains are migrated to Markdown.

### Markdown lesson format

```yaml
---
moduleId:       java-app-2
moduleTitle:    "Module 2: Variables & Data Types"
moduleGlyph:    "📝"
moduleSortOrder: 2
domainId:       java
tier:           APPRENTICE
topicSlug:      variables_and_data_types
topicTitle:     "Variables & Data Types"
topicSortOrder: 2
id:             java-app-2a
title:          "Primitives, Wrapper Classes & Type Safety"
sortOrder:      1
xpReward:       60
practiceType:   JAVA           # JAVA | TAILWIND | REACT | SQL | R | NONE
questType:      KNOWLEDGE
feynmanPrompt:  "..."
learningObjectives:
  - "..."
integrationDomains:
  - mathematics
  - psychology
soloAssessment:
  type: RUBRIC_REFLECTION       # RUBRIC_REFLECTION | PATTERN_MATCH | AI_REVIEW
  rubricItems:
    - "Declares at least one int variable"
  keywords:
    - int
    - double
  modelAnswer: |
    ```java
    int level = 5;
    ```
guidedSteps:
  - id: prim-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE  # FILL_BLANK | MULTIPLE_CHOICE | SHORT_TEXT | CODE | DRAG_DROP | SEQUENCE
    instruction: "..."
    inputConfig:
      options: ["int", "String", "double", "boolean"]
    markingRule:
      matchMode: NORMALIZED     # EXACT | NORMALIZED | REGEX | CONTAINS | OUTPUT_MATCH
      accepted: ["String"]
      rejectedFeedback: "..."
    hint: "..."
    reflectionPrompt: "..."
---

# Hook
...

# Lore Introduction
...

# Core Learning

## Concept Introduction
...

## Why It Matters
...

## Worked Examples
...

## Common Mistakes
- First mistake
- Second mistake

## Mental Model
...

## Mini Summary
...

# Guided Practice Quest
...

# Solo Practice Quest
...

# Integration
...

# Lore Conclusion
...
```

### Markdown content tree (current state)

```
resources/content/java/apprentice/
├── variables_and_data_types/
│   └── java_app_2.md    ✓ migrated (Primitives, Wrapper Classes & Type Safety)
├── control_flow/
│   └── java_app_4.md    ✓ migrated (Conditionals: if/else and switch-expressions)
├── methods_and_functions/
│   └── java_app_5.md    ✓ migrated (Parameters, Return Types & Scope)
└── arrays/
    └── java_app_6.md    ✓ migrated (1D Arrays: Declaration, Access & Iteration)
```

JSON files (java-app-1, java-app-3, java-app-7 through java-app-15 and all other domains) remain as JSON pending migration via `LessonAuthoringHarness`.

### CI validation (`MarkdownLessonValidationTest`)

Parameterised JUnit test scans every `.md` in `classpath:content/**/*.md`. Checks per file:

| Check | Rule |
|---|---|
| Frontmatter | YAML block present |
| Required fields | `id`, `domainId`, `tier`, `moduleId`, `title` |
| Valid tier | `APPRENTICE \| JUNIOR \| SENIOR \| LEAD` |
| Required H1 sections | Hook · Lore Introduction · Core Learning · Integration · Lore Conclusion |
| Required H2 in Core | Concept Introduction · Why It Matters · Mental Model · Mini Summary |
| Word count bands | APPRENTICE 400–1800 / JUNIOR 600–2500 / SENIOR 800–3500 / LEAD 1000+ |
| Solo assessment | Type in valid set; RUBRIC_REFLECTION → rubricItems + modelAnswer present |
| Guided steps | Every step has `id` + `markingRule` |

### `LessonAuthoringHarness`

`tools/LessonAuthoringHarness.java` — standalone `main()` class (not Spring-scanned). Calls `claude-opus-4-5` with the full lesson template as system prompt. **Human review required before commit.**

```bash
java -cp ... LessonAuthoringHarness \
    --id java_app_3 \
    --title "Operators and Expressions" \
    --domain java \
    --tier APPRENTICE \
    --module-id java-app-3 \
    --topic operators_and_expressions \
    --sort 3 \
    [--out path/to/output.md] \
    [--dry-run]
# Requires: ANTHROPIC_API_KEY env var
```

### Legacy JSON format

```jsonc
{
  "id": "java-app-1",
  "title": "Computational Thinking",
  "domainId": "java",           // also accepted as "topicId" (legacy alias)
  "tier": "APPRENTICE",
  "sortOrder": 1,
  "subChunks": [                // also accepted as "lessons"
    {
      "id": "java-app-1a",
      "practiceType": "JAVA",
      "guidedPracticeHtml": "...",
      "soloPracticeHtml": "...",
      "questions": [...],
      "integrationPrompt": "..."
    }
  ]
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
| EXPLANATION | Full lesson content (Concept · Why It Matters · Worked Examples · Mental Model · Mini Summary) | Always shown |
| GUIDED_PRACTICE | Step workbook (GuidedStepper) when `hasGuidedSteps=true`; legacy HTML fallback | Gated: all steps must pass before advancing |
| SOLO_PRACTICE | Independent rebuild — RUBRIC_REFLECTION / PATTERN_MATCH / DETERMINISTIC / AI_REVIEW | Skipped if `soloPracticeHtml` blank |
| RETRIEVAL_CHECK | Quiz from `questions` array | Auto-passed if no questions; 60% threshold |
| INTEGRATION | Cross-domain connection prompt | Skipped if `integrationPrompt` blank |
| COMPLETE | Summary + optional Feynman teach-back | Terminal state |

### Quest types (optional field on Lesson)

`questType` (enum): `KNOWLEDGE | GUIDED | PRACTICE | INVESTIGATION | SYNTHESIS | MASTERY`. Shown as a gold badge in the EncodingPage lesson header.

---

## 9. Practice Types

| Type | Editor | Grading |
|---|---|---|
| `JAVA` | Monaco (`MonacoCodeEditor`) | Backend: compiles + runs via `CodeExecutionPort` |
| `TAILWIND` | `TailwindEditor` (HTML) | Backend: CSS class presence check |
| `REACT` | `ReactEditor` (JSX iframe) | Client-side → backend awards XP |
| `SQL` | `SqlEditor` (sql.js iframe) | Client-side → backend awards XP |
| `R` | `REditor` (WebR iframe) | Client-side → backend awards XP |
| `NONE` | `WrittenResponseEditor` (textarea) | Backend: `gradeWrittenPractice` (pseudocode or prose auto-detected) |

### Code execution (`CodeExecutionPort`)

Two implementations behind the `CodeExecutionPort` interface:

| Implementation | Activation | Behaviour |
|---|---|---|
| `JavaCodeRunner` | `SANDBOX_MODE=inprocess` (default / `matchIfMissing=true`) | In-process `javax.tools.JavaCompiler`; 5s timeout; `SandboxedClassLoader` blocks I/O, reflection, network |
| `DockerSandboxRunner` | `SANDBOX_MODE=docker` | `docker run --rm --network=none --memory=256m --cpus=0.5 --pids-limit=64 --read-only --tmpfs /tmp`; 10s wall-clock timeout; image `eclipse-temurin:21-jre-alpine` |

**£0 deployment strategy:** Production uses `JavaCodeRunner` inside the existing Cloud Run service (scales to zero). Switching to Docker is a single env-var flip, deployed only to an Always-Free ARM VM or GCP free-tier e2-micro when affordable.

### `DockerSandboxRunnerIntegrationTest`

Gated on `@EnabledIfEnvironmentVariable(named = "DOCKER_SANDBOX_TESTS", matches = "true")`. Tests: hello-world output, timeout, network blocked, forbidden pattern, compilation error. Excluded from the standard test run.

---

## 10. Gamification & XP

### XP awards

| Event | XP |
|---|---|
| Guided practice passed | `lesson.xpReward` (typically 50–350) |
| Retrieval check passed | 25 |
| Feynman teach-back | Variable (AI-graded, 20–50) |
| Solo practice (all types) | 0 (XP already earned in guided) |

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

`GamificationFacade.evaluateAndAwardBadges()` is called after every XP-earning event. The facade pattern isolates the gamification module — no other module imports from `gamification.service.*`.

---

## 11. AI Integration

All AI calls are proxied through the backend. The Anthropic API key never reaches the client. Prompt caching (`CacheControlEphemeral.Ttl.TTL_1H`) is enabled on system prompts.

| Service / Tool | Purpose | Tier |
|---|---|---|
| `AiMentorService.explainCompileError` | Socratic error explanation after compile failure | All |
| `AiMentorService.explainRuntimeError` | Socratic explanation after runtime error | All |
| `AiMentorService.getFeedback` | Hint after test failures | All |
| `FeynmanService.grade` | Scores free-text teach-back on 4 dimensions | All |
| `RetrievalService.gradeAnswers` | Grades short-answer retrieval check responses | All |
| `SoloAssessmentService` (AI_REVIEW mode) | Full solo-practice review with AI feedback | Lead only; 3/month quota |
| `LessonAuthoringHarness` (offline) | Generates `.md` lesson drafts via `claude-opus-4-5`; **not runtime** | Authoring tool |

The AI mentor persona is **Archmage Veylan** — Socratic, does not give direct answers, uses the wizardry register.

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

### [2026-05-30] Phase 7 — Knowledge maps (React Flow + Mermaid)

**Decision:** `GET /api/graph` endpoint returns DOMAIN / MODULE / LESSON nodes and three edge types (HIERARCHY, PREREQUISITE, INTEGRATION). Integration edges derived from `integrationDomainsJson` on each Lesson — edges to own domain and non-existent domains are silently filtered.

Frontend: `CurriculumGraph.tsx` uses `@xyflow/react` with a simple three-row DAG layout (no auto-layout lib dependency). Nodes colour-coded by progress status / FSRS memory strength. `MermaidBlock` + `useMermaidHydration` hook added for in-lesson diagrams; initialised with the Academy dark-theme palette.

---

### [2026-05-30] Phase 6 — Content authoring at scale

**Decision:** CI lint (`MarkdownLessonValidationTest`) parameterised test validates every `.md` in the content tree. Word-count bands: APPRENTICE 400–1800 / JUNIOR 600–2500 / SENIOR 800–3500 / LEAD 1000+. `LessonAuthoringHarness` (offline `main()` class using `java.net.http.HttpClient`) calls `claude-opus-4-5` to draft lessons; writes to the content tree; not part of the runtime application. 4 Java Apprentice lessons migrated to Markdown (java-app-2, 4, 5, 6).

---

### [2026-05-30] Phase 5 — CodeExecutionPort abstraction + FSRS

**`CodeExecutionPort`:** `@ConditionalOnProperty` chosen over `@ConditionalOnMissingBean` because `@ConditionalOnMissingBean` is unreliable in regular component scanning (evaluation order not guaranteed). `JavaCodeRunner` activates on `sandbox.mode=inprocess` (`matchIfMissing=true` — no config needed in prod). `DockerSandboxRunner` activates on `sandbox.mode=docker`. Zero forced cost on Cloud Run.

**FSRS:** `FsrsAlgorithm` is a pure-function utility class (no Spring bean). `SpacingService` wraps it while keeping public method signatures unchanged so all callers (`EncodingService`, `GuidedStepService`, `RabbitHoleService`, `CodeController`) need no changes. Key insight: `nextInterval(stability) ≈ round(stability)` — stability in days is numerically the interval because the FACTOR in the power-forgetting curve cancels out. FSRS fields on `UserChunkProgress` are additive (V33); legacy SM-2 rows with `fsrsStability=0` are treated as NEW cards.

---

### [2026-05-30] Phase 4 — Solo assessment types

**Decision:** `SoloAssessmentType` enum (`DETERMINISTIC | RUBRIC_REFLECTION | PATTERN_MATCH | AI_REVIEW`). `SoloAssessmentService` dispatches in `EncodingService.submitSoloPractice`. RUBRIC_REFLECTION requires no AI — learner self-checks rubric items and selects confidence; model answer revealed after first submission. PATTERN_MATCH uses `KeywordScoringService` keyword counting → WEAK / GOOD / EXCELLENT bands. AI_REVIEW is Lead-only with a 3/month per-domain quota tracked on `UserDomainProfile.aiReviewsUsedThisMonth`; downgrades to rubric outside quota.

---

### [2026-05-30] Phase 3 — Guided-step engine + Monaco

**Decision:** `GuidedStep` entity in `guided_steps` table; `markingRuleJson` stores `{ matchMode, accepted[], rejectedFeedback }`. EXACT / NORMALIZED / REGEX / CONTAINS are all deterministic — no AI in the happy path. AI hint is an optional fallback. `advancePhase` guard requires all steps passed before GUIDED→SOLO transition. Monaco replaces the `<textarea>` `CodeEditor.tsx` for JAVA and CODE-step inputs (lazy-loaded; saves ~500KB from initial bundle).

---

### [2026-05-30] Phase 2 — Markdown-first content pipeline

**Decision:** YAML frontmatter (snakeyaml, ships with Spring Boot) + body sections parsed by `MarkdownLessonParser`. CommonMark (commonmark-java) renders prose to HTML. Mermaid fenced code blocks (`language-mermaid`) are preserved through the parser — hydration is handled client-side in Phase 7. `MarkdownContentSeeder` replaces `JsonContentSeeder` for `.md` files; both run on startup. Topic slug from frontmatter determines Topic entity (fine-grained Topics per module replace single default Topics). `integrationDomainsJson` stored on Lesson for Phase 7 graph edges.

---

### [2026-05-30] Phase 1 — Topic hierarchy introduced

**Decision:** `topics` table (`id`, `moduleId`, `title`, `purposeHtml`, `learningOutcomesJson`, `sortOrder`). `lessons.topic_id` FK. V29 migration backfills one default Topic per existing module. `ModuleController` returns `topicId`/`topicTitle` per lesson and the `topics` list in `ModuleDetailDto`. `ModuleMapPage` groups lessons under Topic header rows.

---

### [2026-05-30] Phase 0 — Complete Domain-era rename

**Decision:** Full rename: `TopicSeeder→DomainSeeder`, serialised fields `topics→domains`, `features/topics/→features/domains/`, `/topics→/domains`, `/topic/:id→/domain/:id`. `LegacyTopicRedirect` component added for backward-compat bookmarks. `@JsonAlias("topicId")` kept on `ChunkContentDto` for legacy JSON content only.

---

### [2026-05-29] Blueprint implementation — Integration phase, Quest types, Schools layer

**Decision:** `INTEGRATION` added to `EncodingPhase` (auto-skipped when `integrationPrompt` blank). `QuestType` enum added. 6 new coming-soon domains added to `domains.ts`. V26 migration. Schools layer (V25): `School` entity, `Domain.schoolId`.

---

### [2026-05-29] Full rename to blueprint terminology

**Decision:** `topics→domains`, `chunks→modules`, `sub_chunks→lessons`; all API paths updated. V24 migration.

---

### [2026-05-28] Written practice grading: auto-detect pseudocode vs prose

**Decision:** Single `NONE` practice type handles both by detecting ≥3 uppercase algorithm keywords at grading time. No `PSEUDOCODE` type needed.

---

### [2026-05-27] React/SQL/R practice graded client-side, server awards XP only

**Decision:** Client runs tests in iframe sandbox, serialises pass/fail, posts to backend. Intentionally un-tamper-proof for a learning context.

---

## 14. Known Constraints & Watch-outs

### Code execution

- **Default:** `JavaCodeRunner` (in-process); 5-second wall-clock timeout; suitable for learning
- **Available but not deployed:** `DockerSandboxRunner` — activate with `SANDBOX_MODE=docker`. Requires Docker daemon on the host. Not deployed to Cloud Run (no Docker-in-Docker support); would need a separate Always-Free VM.
- **Integration tests** gated on `DOCKER_SANDBOX_TESTS=true` — excluded from standard CI

### FSRS

- FSRS-4.5 implemented with default weight vector from the paper. `fsrsStability=0` rows are treated as `NEW` cards for backward compatibility with pre-V33 SM-2 data.
- `nextInterval(stability) ≈ round(stability)` in days — verified by 35 unit tests

### Neon Postgres (serverless)

- Cold-start latency on first connection (~200ms). HikariCP settings must match Neon's connection limits.

### Cloud Run auto-scaling to zero

- First request after idle: ~1–3 second cold-start.

### ContentSeeder idempotency

- Both seeders upsert by ID. Changing a lesson's `"id"` or `id:` frontmatter field after deployment creates a duplicate instead of updating.
- **Never rename a content file's ID after it's been deployed.**
- Markdown file renaming is safe — ID comes from frontmatter, not filename.

### Content migration (in progress)

- 4 of ~15 Java Apprentice lessons migrated to Markdown (java-app-2, 4, 5, 6)
- java-app-1, 3, 7–15 remain as JSON; all other domains are still JSON
- Use `LessonAuthoringHarness` to draft new `.md` files; always human-review before commit
- JSON seeder will be retired once all domains are fully migrated

### Knowledge graph scaling

- `GET /api/graph` loads all active modules and all lessons in two queries (`findAllByOrderBySortOrderAsc` + `findByModuleIdIn`). Works fine at current scale (71 lessons/domain × 6 domains ≈ 426 lessons). Add pagination or eager-load optimisation if content grows to thousands of lessons.

### `@xyflow/react` attribution

- React Flow v12 (community edition) shows a "React Flow" attribution watermark. Suppressed via `proOptions={{ hideAttribution: true }}`. This is permitted in non-commercial / student projects; revisit before commercial launch.

---

## 15. Completed Restructure Summary

All 8 phases of the 2026 restructure are complete as of 2026-05-30.

| Phase | Delivered | Commit |
|---|---|---|
| **Phase 0** | Domain-era rename (free up "Topic"); `LegacyTopicRedirect`; V27 migration | — |
| **Phase 1** | `Topic` entity + navigation; `ModuleMapPage` topic grouping; fresh `PROJECT_REFERENCE.md` (V29) | — |
| **Phase 2** | `MarkdownLessonParser`, `MarkdownContentSeeder`, `MarkdownConverter`, full section model on `Lesson` (V29) | — |
| **Phase 3** | `GuidedStep` entity + `GuidedStepService` + deterministic marking engine; `GuidedStepper.tsx` + 6 input components; Monaco replaces textarea (V30) | — |
| **Phase 4** | `SoloAssessmentService` dispatching 4 types; `SoloAssessmentPanel.tsx`; AI quota tracking (V31) | — |
| **Phase 5** | `CodeExecutionPort` + `DockerSandboxRunner` (£0 config-flip); FSRS-4.5 in `FsrsAlgorithm` + `SpacingService` (V33) | `860d055` |
| **Phase 6** | `MarkdownLessonValidationTest` CI lint; `LessonAuthoringHarness`; 4 Java Apprentice `.md` lessons | `beea763` |
| **Phase 7** | `GraphController` + DTOs; `CurriculumGraph.tsx` React Flow map; `MermaidBlock` + hydration hook; `/knowledge-map` route; Nav "Map" link | `fd27267` |

### Remaining content work (not blocking)

- Migrate remaining ~11 Java Apprentice JSON lessons to Markdown (java-app-1, 3, 7–15) using `LessonAuthoringHarness`
- Migrate all other domains (Tailwind, React, Psychology, Sciences, Genealogy) to Markdown
- Author toward 150–300 lessons per domain per tier (current: ~71/domain)
- Retire `JsonContentSeeder` once all domains are fully migrated
