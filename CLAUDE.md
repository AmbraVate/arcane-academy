# Arcane Academy — CLAUDE.md

Gamified Java learning platform. Students progress through chapters of programming quests, earn XP, and fight chapter bosses. Fantasy/wizard aesthetic throughout.

## Tech Stack

- **Backend**: Java 21, Spring Boot 3, Spring Security (JWT), Spring Data JPA, PostgreSQL
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
    SecurityConfig.java         — JWT auth + BCrypt PasswordEncoder bean
  model/        — JPA entities: User, Quest, Boss, UserProgress
  repository/   — Spring Data JPA repositories
  service/      — QuestService, BossService: XP, locking, evaluation
  controller/   — REST: /api/auth, /api/quests, /api/code, /api/boss
  runner/       — JavaCodeRunner: compiles + sandboxes student Java code
  dto/          — response DTOs (QuestSummaryDto, SubmitResponse, etc.)
  security/     — JwtAuthFilter, JwtUtils

frontend/src/
  pages/
    HomePage.tsx      — chapter list with quest rows, side quest divider, boss row
    QuestPage.tsx     — story → coding flow, lesson toggle, practice mode
    BossPage.tsx      — sequential boss questions, defeat breakdown panel
  components/
    quest/StoryPanel  — renders story beats (narration / dialogue / example)
    quest/CodeEditor  — code editor
    quest/AiMentorPanel — AI mentor feedback on compile/runtime errors
    layout/LevelUpModal — rank-up celebration modal
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

| Rank | XP threshold |
|---|---|
| Novice | 0 |
| Apprentice | 400 |
| Adept | 1 000 |
| Mage | 2 000 |
| Archmage | 4 000 |

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
| `novice@arcane.test` | `test_novice` | Fresh start, 0 XP |
| `apprentice@arcane.test` | `test_apprentice` | Chapter I complete |
| `adept@arcane.test` | `test_adept` | Chapters I–II complete |
| `mage@arcane.test` | `test_mage` | Chapters I–IV complete |

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

## Active Branch

`claude/wonderful-neumann` → target `master`
PR: https://github.com/AmbraVate/arcane-academy/pull/new/claude/wonderful-neumann
