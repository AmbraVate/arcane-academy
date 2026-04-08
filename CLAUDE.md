# Polymath Academy — CLAUDE.md

Gamified learning platform of various Topics. Students progress through chapters of programming quests, earn XP, and fight chapter bosses. Fantasy/wizard aesthetic throughout.

# Initial

Java is the first Topic being created taking someone who has not programmed or touched code, through to being job ready for their first Apprenticeship/Junior Role.
This will also be the track used as a Proof of Concept.
## Tech Stack

- **Backend**: Java 21, Spring Boot 3, Spring Security (JWT + OAuth2 Client), Spring Data JPA, PostgreSQL
- **Frontend**: React 18, TypeScript, Vite, React Router, CSS Modules
- **Database**: PostgreSQL — `ddl-auto: update` means Hibernate manages schema automatically; no migration scripts are needed

## Project Structure

```
backend/src/main/java/com/arcane/academy/
  config/
    AbstractChapterSeeder.java  — DSL helpers for content authoring
    Ch1Seeder.java … Ch8Seeder.java  — quest + side quest content
    BossSeeder.java             — boss content
    DataSeeder.java             — orchestrates all seeders on startup
    TestUserSeeder.java         — creates test accounts on startup
    SecurityConfig.java         — JWT auth + OAuth2 + BCrypt PasswordEncoder bean
  model/        — JPA entities: User, Quest, Boss, UserProgress, UserBadge, BadgeDefinition (enum)
  repository/   — Spring Data JPA repositories
  service/      — QuestService, BossService, BadgeService: XP, locking, evaluation, badges
  controller/   — REST: /api/auth, /api/quests, /api/code, /api/boss, /api/badges
  runner/       — JavaCodeRunner: compiles + sandboxes student Java code
  dto/          — response DTOs (QuestSummaryDto, SubmitResponse, etc.)
  security/     — JwtAuthFilter, JwtService, OAuth2LoginSuccessHandler

frontend/src/
  pages/
    HomePage.tsx      — chapter list with quest rows, side quest divider, boss row
    QuestPage.tsx     — story → coding flow, lesson toggle, practice mode
    BossPage.tsx      — sequential boss questions, defeat breakdown panel
    ProfilePage.tsx   — user profile with earned/locked badge grid
    OAuthCallbackPage — handles OAuth2 redirect with token params
  components/
    quest/StoryPanel  — renders story beats (narration / dialogue / example)
    quest/CodeEditor  — code editor
    quest/AiMentorPanel — AI mentor feedback on compile/runtime errors
    layout/LevelUpModal — rank-up celebration modal
    layout/BadgeToast   — animated badge-earned notification
  api/services.ts     — typed API client
  types/index.ts      — shared TypeScript interfaces
  hooks/useAuth.ts    — auth context: user, updateXp, login, logout
```

## Content Authoring DSL

All quest content is written in chapter seeders using helpers from `AbstractChapterSeeder`.

### Quest registration

```java
// Main quest
q(id, title, eyebrow, topic, chapter, order, xp, filename,
  story(...), problemHtml, hint, starterCode, winStory, tests(...))

// Side quest (identical but sets sideQuest = true)
sq(id, title, eyebrow, topic, chapter, order, xp, filename, ...)
```

### Story beats

```java
story(
  n("Narration text — HTML allowed"),
  d("emoji", "type", "Speaker Name", "css-class", "Dialogue — HTML allowed"),
  e("Label", "Example code text")
)
```

### Example code blocks — critical rule

**Always use `\n` (Java newline escape) in `e()` calls, never `\\n`.**

- `\n` in a Java string literal = actual newline character → stored correctly → renders as a line break in `<pre>`
- `\\n` in a Java string literal = literal backslash + n → renders as the text `\n` in the browser

### Test cases

```java
tests(
  test("label", "injectedVars", "expectedOutput")
)
// injectedVars: "null" for no injection; otherwise e.g. "int coins = 35;"
```

## Java Code Runner

`JavaCodeRunner` compiles and runs student code in a sandboxed thread (5-second timeout).

**How wrapping works:**

- If the student code contains a class declaration (`class Foo {`), the runner renames it to `StudentSolution` and runs it as-is
- If no class declaration is found, the code is wrapped inside `public static void main(String[] args) { ... }`

### Import statements — critical rule

`import` statements inside a method body cause `illegal start of expression`. Quests that need imports (`ArrayList`, `HashSet`, `Collections`, etc.) **must** provide a full class template as their `starterCode`.

✅ Correct — full class template:
```java
"import java.util.ArrayList;\n\npublic class MyQuest {\n    public static void main(String[] args) {\n        // student code here\n    }\n}\n"
```

❌ Wrong — bare import gets wrapped inside main:
```java
"import java.util.ArrayList;\n\n// student code here\n"
```

## Quest Flow (Frontend)

1. **Story stage** — full-page story with scroll-reveal quest panel → "Accept Quest" (or "Review Quest" if already completed) enters coding stage
2. **Coding stage** — split layout:
   - Left: eyebrow / title / XP chip / "📖 View lesson" toggle (shows StoryPanel inline) / quest brief + hints
   - Right: code editor / run / submit / AI mentor feedback
3. **Complete stage** — win panel shows win story + "Return to Academy" + "🔄 Practice Again" button
   - Practice Again resets code to starter and re-enables submission
   - Backend deduplicates XP via `UserProgress` unique constraint — re-submissions return `xpEarned: 0`

## Boss Fight Flow

- Questions are shuffled on each attempt
- One wrong answer = defeat; all correct = victory
- **Defeat**: breakdown auto-opens showing ✓/✗ per answered question; wrong question shows code block, user's answer vs correct answer, and explanation
- **Victory**: calls `POST /api/boss/{id}/defeat` to award XP and unlock next chapter

## XP & Ranking

| Rank       | XP threshold |
|------------|--------------|
| Novice     | 0            |
| Apprentice | 800          |
| Adept      | 2 000        |
| Mage       | 4 000        |
| Archmage   | 6 500        |
| Magus      | 8 000        |
| Lord Magus | 11 000       |

XP is awarded exactly once per quest/boss — enforced by a unique constraint on `(user_id, item_id, item_type)` in `user_progress`.

## Locking Rules

- Ch1 quests unlock sequentially (each requires the previous)
- Ch1 side quests are always unlocked
- Ch2+ first quest requires Ch(N-1) boss completion
- Ch2+ side quests require the previous chapter's boss
- Side quests don't count toward chapter completion — `getChapterStatus()` filters to main quests only

## Test Accounts

Created by `TestUserSeeder` on every startup (skipped if already present). **Password for all: `Test1234!`**

| Email | Username | Stage |
|---|---|---|
| `novice@polymath.test` | `test_novice` | Fresh start, 0 XP |
| `apprentice@polymath.test` | `test_apprentice` | Chapter I complete |
| `adept@polymath.test` | `test_adept` | Chapters I–II complete |
| `mage@polymath.test` | `test_mage` | Chapters I–IV complete |

## Badges / Accolades

Badge definitions are in the `BadgeDefinition` enum — code-driven constants, not database rows. The `user_badges` table stores which user earned which badge and when. `BadgeService.evaluateAndAward(userId)` checks all conditions and awards new badges.

### Badge Categories

| Category | Badges |
|---|---|
| Quest | First Spell Cast, Rune Initiate (Ch1), Tome Scholar (Ch2), Structure Weaver (Ch3), Grimoire Keeper (Ch4), Quest Master (25 quests) |
| Boss | Dragon Slayer (first boss), Conqueror of Shadows (all bosses) |
| XP | Spark of Magic (100), Rising Flame (500), Arcane Adept (1k), Master of the Arts (2.5k), Legendary Wizard (5k) |
| Streak | Consistent Apprentice (3d), Week of Dedication (7d), Unyielding Will (30d) |

### Integration Points

- `QuestService.evaluateSubmission()` → on all tests passed, evaluates badges and returns `newBadges` in `SubmitResponse`
- `BossService.defeatBoss()` → evaluates badges and returns `newBadges` in `ProgressResponse`
- `AuthService.login()` → evaluates badges (for streak-based awards)
- Frontend: `BadgeToast` component shows animated gold-bordered notification for each newly earned badge
- Profile page (`/profile`) shows all badges in a grid by category — earned badges glow, locked badges are greyed out

## Authentication

### Email/Password (Local)
Standard registration and login via `POST /api/auth/register` and `POST /api/auth/login`. Passwords are BCrypt-hashed. JWT token returned in response.

### Google OAuth2
Spring Security OAuth2 Client handles the flow. Frontend redirects to `/oauth2/authorization/google`, backend handles Google callback, creates/finds user, generates JWT, redirects to `/oauth2/callback?token=...&userId=...&username=...&totalXp=...&rank=...&streakDays=...`.

**Account linking**: If a Google user's email matches an existing local user, the accounts are linked (existing user gets Google provider added). OAuth users have no `passwordHash`.

**Environment variables**: `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `OAUTH2_FRONTEND_REDIRECT`

### User Entity Auth Fields
- `authProvider` (enum: LOCAL, GOOGLE) — defaults to LOCAL
- `providerId` — Google's `sub` claim (nullable)
- `passwordHash` — nullable (null for OAuth-only users)

## CSS Conventions

Global design tokens are in `frontend/src/index.css`. Key variables:

```css
--bg, --surface, --card, --border
--gold, --gold-dim
--purple, --purple-dim, --purple-light
--teal, --teal-dim
--text, --muted
--red, --green, --orange
```

Chip classes: `chip chip-purple`, `chip chip-green`, `chip chip-teal`, `chip chip-red`, `chip chip-gold`, `chip chip-gray`

Button classes: `btn btn-primary`, `btn btn-ghost`, `btn btn-success`

## Production Deployment

### Profiles

- **Default** (`application.yml`): Development — `ddl-auto: update`, DEBUG logging
- **Production** (`application-prod.yml`): `ddl-auto: validate`, INFO logging — activated via `spring.profiles.active=prod` (set automatically in Dockerfile)

### Render Deployment (recommended free hosting)

`render.yaml` blueprint defines all services:
- **polymath-academy-api**: Docker web service (backend)
- **polymath-academy**: Static site (frontend with API rewrites)
- **polymath-db**: Free PostgreSQL database

Deploy: Dashboard > New > Blueprint Instance > connect GitHub repo. Set `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET` manually in the dashboard.

### Docker Compose (local production test)

```bash
JWT_SECRET=$(openssl rand -base64 48) docker compose up --build
```

### Required Environment Variables (production)

| Variable | Description |
|---|---|
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | DB password |
| `JWT_SECRET` | Min 32 chars, cryptographically random |
| `ALLOWED_ORIGINS` | Frontend URL (e.g., `https://polymath-academy.onrender.com`) |
| `GOOGLE_CLIENT_ID` | Google OAuth client ID (optional) |
| `GOOGLE_CLIENT_SECRET` | Google OAuth client secret (optional) |
| `OAUTH2_FRONTEND_REDIRECT` | OAuth callback URL (e.g., `https://your-domain.com/oauth2/callback`) |

### Error Handling

`GlobalExceptionHandler` (`@RestControllerAdvice`) catches all exceptions and returns clean JSON `{ "message": "..." }` responses — no stack traces leak to the client.

### Health Check

`GET /actuator/health` — Spring Boot Actuator, only `health` endpoint exposed, no details shown.

## Active Branch

`claude/wonderful-neumann` → target `master`
PR: https://github.com/AmbraVate/arcane-academy/pull/new/claude/wonderful-neumann
