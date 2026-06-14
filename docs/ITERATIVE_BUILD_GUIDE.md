# Arcane Academy Iterative Build Guide

This guide teaches the project in the order you should build it when learning the codebase from scratch.

The important idea is to make every stage produce something visible before moving on:

1. Generate or write lesson content and print it to the console.
2. Parse that content into structured Java objects.
3. Save it into the backend database.
4. Expose it with simple public backend endpoints.
5. Connect the React frontend to those endpoints.
6. Add authentication, progress, payments, admin, and AI only after the content loop works.

The existing project is bigger than this guide because it already contains authentication, payments, AI, admin tools, gamification, and a full learning flow. When learning or rebuilding it, ignore those advanced layers at first. Build the smallest content-to-screen path, then add depth.

## Project Shape

Current stack:

- Backend: Java 21, Spring Boot 3.3, Maven, Spring Web, Spring Data JPA, PostgreSQL, Flyway, Spring Security, Spring Modulith.
- Frontend: React 18, TypeScript, Vite, React Router, Tailwind, Axios, React Query patterns.
- Content: Markdown lesson files stored under `backend/src/main/resources/content`.
- Runtime path: Markdown files are parsed, seeded into the database, served by `/api/...`, then rendered in React.

Important existing files:

- `backend/src/main/java/com/ambravate/arcane/academy/tools/LessonAuthoringHarness.java`
- `backend/src/main/java/com/ambravate/arcane/academy/content/seeder/MarkdownLessonParser.java`
- `backend/src/main/java/com/ambravate/arcane/academy/content/seeder/MarkdownContentSeeder.java`
- `backend/src/main/java/com/ambravate/arcane/academy/content/controller/ModuleController.java`
- `backend/src/main/java/com/ambravate/arcane/academy/common/config/SecurityConfig.java`
- `frontend/src/shared/api/client.ts`
- `frontend/src/shared/api/services.ts`
- `frontend/src/features/domains/pages/ModuleMapPage.tsx`
- `frontend/src/features/learning/pages/EncodingPage.tsx`

## Iteration 0: Run One Thing In The Console

Goal: prove you can run Java code without Spring, a database, auth, or the frontend.

Start with a plain console class:

```java
public final class ContentConsoleDemo {
    public static void main(String[] args) {
        String lesson = """
                ---
                id: demo-lesson-1
                moduleId: demo-module-1
                moduleTitle: "Demo Module"
                domainId: software_engineering
                tier: APPRENTICE
                title: "What is a Variable?"
                sortOrder: 1
                xpReward: 30
                practiceType: NONE
                ---

                # Hook
                A variable is a named place to keep a value.

                # Core Learning

                ## Concept Introduction
                A variable lets a program remember data.
                """;

        System.out.println(lesson);
    }
}
```

Checkpoint:

- You can run a Java `main`.
- You can see raw lesson content in the console.
- You are not yet thinking about controllers, databases, JWT, or React.

In the existing project, the closest real tool is `LessonAuthoringHarness`. It can draft a full Markdown lesson and either print it with `--dry-run` or write it to the content tree.

Example conceptually:

```powershell
cd backend
mvn -DskipTests package
java -cp target/classes com.ambravate.arcane.academy.tools.LessonAuthoringHarness `
  --id se-app-demo-01 `
  --title "Variables as Named Values" `
  --domain software_engineering `
  --tier APPRENTICE `
  --module-id se-app-demo `
  --topic variables_and_state `
  --sort 1 `
  --dry-run
```

That command requires `ANTHROPIC_API_KEY`. If you do not want AI yet, manually write the Markdown. The learning goal is the same: produce one valid lesson as text.

## Iteration 1: Define The Lesson Format

Goal: decide what a lesson file must contain.

The current project expects:

- YAML frontmatter between `---` lines.
- Required fields such as `id`, `moduleId`, `moduleTitle`, `domainId`, `tier`, `title`, `sortOrder`, `xpReward`, and `practiceType`.
- Body sections using `#` headings.
- Core learning sections using `##` headings.
- Optional `guidedSteps`, `soloAssessment`, and `microCheckpoint` blocks.

Minimal useful lesson:

````markdown
---
id: se-app-demo-01
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-demo
moduleTitle: "Demo Module"
moduleGlyph: "*"
moduleSortOrder: 99
topicSlug: variables
topicTitle: "Variables"
topicSortOrder: 1
title: "Variables as Named Values"
sortOrder: 1
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
---

# Hook

Variables let a program remember a value so it can use it later.

# Lore Introduction

An apprentice labels a small crystal before placing a number inside it.

# Core Learning

## Concept Introduction

A variable is a named storage location for a value.

## Why It Matters

Without variables, a program cannot keep track of changing information.

## Worked Examples

```java
int age = 21;
System.out.println(age);
```

## Common Mistakes

- Using a variable before giving it a value.
- Choosing names that do not explain the data.

## Mental Model

Think of a variable as a labelled box.

## Mini Summary

- Variables have names.
- Variables hold values.
- Programs read and update those values.

# Guided Practice Quest

Describe a variable in your own words.

# Solo Practice Quest

Write three examples of values a program might store.

# Integration

Variables connect to mathematics because algebra also uses named symbols.

# Lore Conclusion

The apprentice learns that naming a thing is the first step to controlling it.
````

Checkpoint:

- You can write one lesson by hand.
- You know which fields drive backend records.
- You know which sections become rendered lesson HTML.

## Iteration 2: Parse Markdown Into A Java Object

Goal: convert raw text into structured data.

In the existing backend, `MarkdownLessonParser` does this:

- Reads frontmatter with SnakeYAML.
- Splits the body by H1 headings.
- Splits `# Core Learning` by H2 headings.
- Converts Markdown into HTML.
- Extracts common mistakes into JSON.
- Parses guided steps, solo assessment, and checkpoints.

When rebuilding this from scratch, start smaller:

1. Create a `LessonDraft` record with `id`, `title`, `moduleId`, and `body`.
2. Parse those fields from one string.
3. Print the parsed object.
4. Add more fields only when the console output is correct.

Example target output:

```text
LessonDraft[
  id=se-app-demo-01,
  title=Variables as Named Values,
  moduleId=se-app-demo
]
```

Checkpoint:

- The console no longer prints raw Markdown only.
- It prints a structured Java object.
- Invalid Markdown should fail with a useful error.

Existing test to study:

- `backend/src/test/java/com/ambravate/arcane/academy/content/seeder/MarkdownLessonParserTest.java`

## Iteration 3: Add The Backend Skeleton Without Auth

Goal: run Spring Boot and return a hard-coded lesson from HTTP.

Build only:

- Spring Boot application class.
- One DTO.
- One controller.
- One endpoint.

Example endpoint:

```java
@RestController
@RequestMapping("/api/public-lessons")
class PublicLessonController {
    @GetMapping("/{id}")
    LessonDto getLesson(@PathVariable String id) {
        return new LessonDto(id, "Variables as Named Values", "<p>A variable stores a value.</p>");
    }
}
```

Checkpoint:

```powershell
curl http://localhost:8080/api/public-lessons/se-app-demo-01
```

Expected result:

```json
{
  "id": "se-app-demo-01",
  "title": "Variables as Named Values",
  "html": "<p>A variable stores a value.</p>"
}
```

Do not add JWT yet. Do not add users yet. Do not add progress yet.

In this project, public read access already exists for:

- `GET /api/modules/**`
- `GET /api/dashboard/public`

Full lesson learning currently goes through protected `/api/encoding/...` endpoints. When learning from scratch, first create a small public lesson endpoint, then later merge the idea into the richer encoding flow.

## Iteration 4: Add Persistence

Goal: save parsed lessons into PostgreSQL.

Add the minimum domain model:

- `LearningModule`
- `Topic`
- `Lesson`

Add repositories:

- `LearningModuleRepository`
- `TopicRepository`
- `LessonRepository`

Then create a seeder:

1. Scan `classpath:content/**/*.md`.
2. Skip files beginning with `_`.
3. Parse each file.
4. Save missing modules.
5. Save missing topics.
6. Save missing lessons.

The existing class that does this is `MarkdownContentSeeder`.

Important behavior in the current project:

- The seeder only seeds lessons that do not already exist.
- Existing lessons are left as-is on restart.
- This avoids overwriting production or admin-edited content.

Checkpoint:

```powershell
cd backend
mvn spring-boot:run
```

Then check:

```powershell
curl http://localhost:8080/api/modules/se-app-m1
```

You should see a module with topics and lesson summaries.

## Iteration 5: Shape Public Content APIs

Goal: expose enough read-only content for the frontend to browse.

Start with public endpoints:

- `GET /api/modules/{moduleId}` returns module title, topics, and lesson summaries.
- `GET /api/public-lessons/{lessonId}` returns lesson content sections.

Keep the response stable and boring. Frontend work becomes easier when the API shape is predictable.

Suggested DTOs:

```text
ModuleDetailDto
  id
  domainId
  title
  glyph
  topics[]
  lessons[]

LessonDetailDto
  id
  moduleId
  domainId
  title
  hookHtml
  explanationHtml
  workedExamplesHtml
  guidedPracticeHtml
  soloPracticeHtml
```

Checkpoint:

- You can browse modules without logging in.
- You can fetch one lesson without logging in.
- The frontend can be built against stable JSON before auth exists.

Current project note:

- `ModuleController` already supports unauthenticated module detail.
- `SecurityConfig` permits public `GET /api/modules/**`.
- Lesson start is still protected through `EncodingController`, which is fine for the full app but later than the learning path in this guide.

## Iteration 6: Add The Frontend Shell

Goal: create a React app that can call one endpoint and render JSON.

Start tiny:

1. Create a Vite React TypeScript app.
2. Add Axios.
3. Add React Router.
4. Add one API client.
5. Add one page.

Frontend API client:

```ts
import axios from 'axios'

export const api = axios.create({
  baseURL: '',
  headers: { 'Content-Type': 'application/json' },
})
```

Public service:

```ts
export async function getModule(moduleId: string) {
  const { data } = await api.get(`/api/modules/${moduleId}`)
  return data
}
```

Module page:

```tsx
export function ModulePage() {
  const [module, setModule] = useState<any>(null)

  useEffect(() => {
    getModule('se-app-m1').then(setModule)
  }, [])

  if (!module) return <p>Loading...</p>

  return (
    <main>
      <h1>{module.title}</h1>
      {module.lessons.map((lesson: any) => (
        <article key={lesson.id}>{lesson.title}</article>
      ))}
    </main>
  )
}
```

Checkpoint:

```powershell
cd frontend
npm install
npm run dev
```

Open:

```text
http://localhost:5173
```

You should see backend content in the browser.

Current project equivalents:

- `frontend/src/shared/api/client.ts`
- `frontend/src/shared/api/services.ts`
- `frontend/src/features/domains/pages/ModuleMapPage.tsx`
- `frontend/src/features/domains/pages/TopicLessonsPage.tsx`

## Iteration 7: Render One Lesson Page

Goal: display actual lesson sections, still without login.

Backend:

- Add `GET /api/public-lessons/{lessonId}`.
- Return title and HTML sections.

Frontend:

- Add `publicLessonApi.get(lessonId)`.
- Add route `/lesson/:lessonId`.
- Render lesson HTML with sanitization.

The existing project already uses `safe` from:

- `frontend/src/lib/sanitize.ts`

Use that pattern before rendering HTML from the backend.

Example:

```tsx
<section dangerouslySetInnerHTML={safe(lesson.explanationHtml)} />
```

Checkpoint:

- Click a lesson from a module.
- See the lesson content.
- No login is required yet.

## Iteration 8: Add The Learning Flow

Goal: turn static reading into a sequence.

Add phases one at a time:

1. `HOOK`
2. `EXPLANATION`
3. `GUIDED_PRACTICE`
4. `SOLO_PRACTICE`
5. `RETRIEVAL_CHECK`
6. `INTEGRATION`
7. `COMPLETE`

At first, keep state in the frontend:

```ts
const phases = ['HOOK', 'EXPLANATION', 'GUIDED_PRACTICE', 'SOLO_PRACTICE']
const [phaseIndex, setPhaseIndex] = useState(0)
```

Then move phase state to the backend after the UI works.

Current project equivalent:

- `frontend/src/features/learning/pages/EncodingPage.tsx`
- `backend/src/main/java/com/ambravate/arcane/academy/practice/controller/EncodingController.java`
- `backend/src/main/java/com/ambravate/arcane/academy/practice/service/EncodingService.java`

Checkpoint:

- A user can move through lesson phases.
- No XP or user progress is required yet.
- The lesson still works as a public learning prototype.

## Iteration 9: Add Guided Steps

Goal: make lessons interactive.

Content:

- Add `guidedSteps` to Markdown frontmatter.

Backend:

- Parse `guidedSteps`.
- Save them to `GuidedStep`.
- Return them for a lesson.
- Add endpoint to check an answer.

Frontend:

- Fetch steps.
- Render each step.
- Submit answer.
- Show hint or feedback.

Current project equivalents:

- `MarkdownLessonParser.parseGuidedSteps`
- `MarkdownContentSeeder` guided step save logic.
- `GuidedStepController`
- `GuidedStepper`

Checkpoint:

- One guided step accepts a correct answer.
- One incorrect answer returns useful feedback.

## Iteration 10: Add Solo Assessment

Goal: let learners produce an independent response.

Start with `RUBRIC_REFLECTION`, not code execution.

Content frontmatter:

```yaml
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines the concept accurately"
    - "Gives a concrete example"
  keywords: [variable, value, name]
  modelAnswer: |
    A variable is a named place where a program stores a value.
```

Backend:

- Parse the block.
- Return rubric items and model answer.
- Accept a written response.

Frontend:

- Render textarea.
- Render checklist.
- Submit and reveal feedback.

Checkpoint:

- A learner can complete a non-code lesson end to end.
- This is the best point to pause and clean up before auth.

## Iteration 11: Add Code Running Later

Goal: support programming exercises only after written lessons work.

Add code execution as a separate capability:

- Request DTO: `code`, optional `testInput`.
- Response DTO: `status`, `output`, `error`.
- Runner service.
- Endpoint: `POST /api/code/run`.

Current project equivalent:

- `CodeController`
- `DockerSandboxRunner`
- `sandbox.mode` in `application.yml`

Checkpoint:

- Submit code.
- See output in the frontend console panel.
- Keep test validation separate from simple "run code".

## Iteration 12: Add Authentication After The Learning Loop

Goal: only add users when there is something worth saving.

Add auth after these work:

- Public module browsing.
- Public lesson rendering.
- Guided steps.
- Solo assessment.
- Optional code running.

Then add:

- User entity.
- Register and login endpoints.
- Password hashing.
- JWT access token.
- Refresh token.
- Frontend auth context.
- Protected routes.

Current project equivalents:

- `AuthController`
- `AuthService`
- `JwtAuthFilter`
- `SecurityConfig`
- `frontend/src/shared/hooks/useAuth.tsx`
- `frontend/src/features/auth/pages/LoginPage.tsx`

Checkpoint:

- User can register.
- User can log in.
- Protected endpoints reject anonymous requests.
- Public module browsing still works.

## Iteration 13: Add Progress, XP, And Badges

Goal: save what the authenticated learner does.

Add:

- `UserChunkProgress`
- lesson status
- current phase
- XP earned
- badge checks
- spaced review metadata

Then change the learning flow:

- Start lesson creates or resumes progress.
- Advance phase saves progress.
- Submissions can award XP.
- Completed lessons update module status.

Current project equivalents:

- `EncodingService`
- `DashboardService`
- `BadgeService`
- `LeaderboardService`

Checkpoint:

- Refreshing the page resumes the same lesson phase.
- Completed lessons stay completed.
- XP changes persist.

## Iteration 14: Add Admin Tools

Goal: manage content after the core learner flow works.

Add admin only after:

- Content seeding works.
- Public browse works.
- Auth roles work.
- Progress works.

Admin should handle:

- Domain/module/topic overview.
- Lesson editing.
- Question editing.
- Import/export.
- User management.

Current project equivalents:

- `backend/src/main/java/com/ambravate/arcane/academy/admin`
- `frontend/src/features/admin`

Checkpoint:

- Admin routes require `ADMIN`.
- Normal users cannot access admin endpoints.
- Content edits do not break seeded content.

## Iteration 15: Add AI, Payments, And Production Hardening Last

Goal: add expensive or high-risk integrations only after the product loop is real.

AI:

- Start with static feedback.
- Add AI mentor only when lesson data and submissions are stable.
- Keep provider keys only in environment variables.

Payments:

- Add Stripe only after auth and access control work.
- Keep webhook processing idempotent.

Production:

- Enable Flyway migrations.
- Set `SPRING_JPA_DDL_AUTO=validate`.
- Configure CORS.
- Configure logging, rate limits, Sentry, and health checks.

Current project equivalents:

- `AiMentorService`
- `PaymentController`
- `StripeWebhookController`
- `application-prod.yml`
- `docker-compose.yml`
- `netlify.toml`

## Recommended Build Order Summary

Follow this order when recreating or deeply learning Arcane Academy:

1. Console lesson string.
2. Hand-authored Markdown lesson.
3. Markdown parser.
4. Parser tests.
5. Hard-coded Spring endpoint.
6. JPA entities and repositories.
7. Markdown content seeder.
8. Public module endpoint.
9. Public lesson endpoint.
10. Vite React shell.
11. Frontend API client.
12. Module page.
13. Lesson page.
14. Phase-based learning UI.
15. Guided steps.
16. Solo assessment.
17. Optional code runner.
18. Authentication.
19. Progress and XP.
20. Admin tools.
21. AI.
22. Payments.
23. Production deployment.

## Daily Working Loop

For each feature, use this loop:

1. Make the smallest change.
2. Run it in the console or with `curl`.
3. Add or update a focused test.
4. Connect the frontend only after the backend response is stable.
5. Keep auth disabled or bypassed until the feature itself works.
6. Commit when the checkpoint is green.

Useful commands:

```powershell
# Backend tests
cd backend
mvn test

# Backend dev server
cd backend
mvn spring-boot:run

# Frontend dev server
cd frontend
npm run dev

# Frontend build
cd frontend
npm run build
```

## What To Ignore At First

When learning the project, deliberately ignore:

- JWT and refresh tokens.
- Google OAuth.
- Stripe.
- Admin pages.
- AI mentor.
- Sentry.
- Docker sandbox mode.
- Leaderboards.
- Profile pages.
- Public sharing.

Those are valuable, but they are not the heart of the app.

The heart is:

```text
Markdown lesson -> parser -> database -> public API -> React page -> interactive learning loop
```

Once that path feels obvious, the rest of the backend and frontend becomes much easier to reason about.
