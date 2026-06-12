---
id: fe-app-m7-04
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m7
moduleTitle: "Module 7: Frontend Engineering Habits"
moduleGlyph: "🛠️"
moduleSortOrder: 7
topicSlug: code_quality
topicTitle: "Code Quality"
topicSortOrder: 2
lesson: naming_conventions
title: "Naming Conventions"
sortOrder: 1
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Uses kebab-case for CSS class names consistently"
    - "Uses camelCase for JavaScript variable and function names"
    - "Uses PascalCase for HTML component names (React) and constructor functions"
    - "Explains why descriptive names are better than short abbreviations"
    - "Applies BEM (Block__Element--Modifier) or similar methodology to CSS classes"
  keywords: [naming, kebab-case, camelCase, PascalCase, BEM, descriptive, convention, class, variable, readable]
  modelAnswer: |
    Naming conventions create consistency across a codebase. CSS classes use kebab-case
    (.nav-link, .card-title). JavaScript variables and functions use camelCase
    (getUserData, isLoading). React components and constructor functions use PascalCase
    (UserCard, LoginForm). Names should be descriptive: .card-title > .ct; getUserData()
    > getData(). BEM (.block__element--modifier) provides a systematic CSS class naming
    structure that makes relationships explicit.
guidedSteps:
  - id: fe-app-m7-04-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which naming style is conventional for CSS class names?
    inputConfig:
      options:
        - "camelCase: .navLink, .cardTitle"
        - "snake_case: .nav_link, .card_title"
        - "kebab-case: .nav-link, .card-title"
        - "PascalCase: .NavLink, .CardTitle"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["kebab-case: .nav-link, .card-title"]
      rejectedFeedback: "CSS class names conventionally use kebab-case (words separated by hyphens). This is partially because CSS properties use hyphens (background-color), making kebab-case feel consistent. JavaScript uses camelCase; Python uses snake_case. Each language has its own idiomatic convention."
    hint: "CSS uses hyphens as separators — what does that suggest for class names?"
    reflectionPrompt: "Following the convention for each language reduces cognitive friction when reading code: you immediately know you're reading CSS class names (kebab-case), not JS variables (camelCase). This is not about correctness — it's about communication. Code is for humans first."

  - id: fe-app-m7-04-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In BEM, a disabled submit button inside a form would have class: `form___submit-button--___`
    inputConfig:
      placeholder: "__  disabled"
    markingRule:
      matchMode: CONTAINS
      accepted: ["__", "disabled", "form__submit", "submit-button--disabled"]
      rejectedFeedback: "BEM: .block__element--modifier. Block: form. Element: submit-button (double underscore). Modifier: disabled (double hyphen). Full class: .form__submit-button--disabled. This makes relationships explicit: 'a submit-button element that is part of a form, in a disabled state.'"
    hint: "BEM uses __ (double underscore) for elements and -- (double hyphen) for modifiers."
    reflectionPrompt: "BEM is verbose but explicit. Reading .form__submit-button--disabled tells you exactly: which component (form), which part (submit-button), and which state (disabled). Reading .btn-disabled tells you only the state — you don't know which component or which element. Explicitness beats brevity for maintainability."

  - id: fe-app-m7-04-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 2-3 sentences why `function getUserData()` is a better name than `function getData()` or `function g()`.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [descriptive, understand, clear, what, purpose, guess, abbreviat, readable]
      rejectedFeedback: "getUserData() tells you: what it does (get), what it gets (user data). getData() is ambiguous — what data? g() requires reading the function body to understand what it does. Descriptive names make code self-documenting: the reader understands without reading the implementation."
    hint: "Think about what a new developer reading the code needs to understand without opening the function."
    reflectionPrompt: "The test for a good function name: can a new developer who has never seen this code understand what the function does, returns, and requires — without opening it? If yes, the name is good. If they have to open the function to understand it, the name is insufficient."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A React component that renders a user's profile card should be named:"
    options:
      - "userProfileCard"
      - "user-profile-card"
      - "UserProfileCard"
      - "USER_PROFILE_CARD"
    correctIndex: 2
    feedback: "React components (and class names / constructor functions in JS) use PascalCase. This convention distinguishes components from regular functions and variables. JSX requires PascalCase for custom components — lowercase is interpreted as an HTML element (div, span, p)."
  - type: MULTIPLE_CHOICE
    question: "A CSS class `.btn` vs `.primary-action-button` — which is better for maintainability?"
    options:
      - ".btn — shorter is always better"
      - ".primary-action-button — more descriptive, intent is immediately clear"
      - "They are equivalent — only consistency matters"
      - ".btn — abbreviations are professional conventions"
    correctIndex: 1
    feedback: "Descriptive names trade brevity for clarity. .primary-action-button tells you: what it is (button), and its role (primary action). .btn tells you only that it's a button. In a large codebase, clarity is worth the extra characters. That said, widely understood abbreviations (nav, btn in a design system with documentation) are acceptable if consistent."

retrieval:
  recall: "What naming case does CSS use for classes, JavaScript for variables, and React for components?"
  explain: "Explain the BEM naming methodology and write one example class using Block, Element, and Modifier."
  mistakeId:
    code: "CSS: .n { }, .ct { }, .c2 { } — meaningless abbreviations"
    answer: "These names tell the reader nothing. .n could be anything. Use: .nav { }, .card-title { }, .card--featured { }. The few keystrokes saved by abbreviations cost minutes (or hours) of future confusion. In a team setting, unclear names are a form of miscommunication."
---

# Hook

You write code once. You read it dozens of times.

Good names cost nothing. They take five seconds to write. They save minutes of reading comprehension every time someone (including you in six months) encounters them.

Bad names are a tax on every future reader.

# Lore Introduction

*"The Academy's cataloguing system,"* says Master Aelindra, *"uses full names for every entry: not 'MS/A/4' but 'Manuscript, Arcane Division, Entry 4: The Properties of Silver Ink.' The abbreviation saves three seconds to write and costs three minutes every time someone reads it. Over a hundred years of use, clarity wins by thousands of hours."*

# Core Learning

## Concept Introduction

**Naming conventions by context:**

| Context | Convention | Example |
|---|---|---|
| CSS classes | kebab-case | `.nav-link`, `.card-title` |
| JavaScript variables | camelCase | `userName`, `isLoading` |
| JavaScript functions | camelCase | `getUserData()`, `formatDate()` |
| React components | PascalCase | `UserCard`, `LoginForm` |
| Constants | UPPER_SNAKE_CASE | `MAX_RETRIES`, `API_BASE_URL` |
| HTML IDs | kebab-case | `id="main-content"` |

**BEM (Block__Element--Modifier):**
```css
/* Block */
.card { }

/* Element (part of the block) */
.card__title { }
.card__body { }
.card__footer { }

/* Modifier (variation of block or element) */
.card--featured { }
.card__title--truncated { }
```

**Good vs bad names:**
```css
/* Bad — meaningless */
.x { } .c1 { } .n { }

/* Good — self-documenting */
.product-card { }
.nav-link--active { }
.form__submit-button { }
```

```javascript
// Bad — requires reading the body to understand
function g(u) { }
const d = new Date();

// Good — intent is clear
function getUserById(userId) { }
const createdAt = new Date();
```

## Why It Matters

Naming is the cheapest documentation you will ever write — and the most-read code in any project is the names:

- A team scanning `userCardList` knows instantly what it holds; `data2` forces them to read every usage
- Consistent conventions (camelCase variables, PascalCase components, kebab-case files) let you predict a name before searching for it
- Renaming later is risky and noisy in version control; choosing well now is free

Most code is read tens of times for every time it's written. Names are the interface other people — and future you — actually use.

## Common Mistakes

- Abbreviations that only make sense to the original author
- Inconsistent case (some classes camelCase, some kebab-case)
- Names that describe implementation rather than intent (`div3` vs `hero-section`)
- Too generic (`data`, `item`, `content`)

## Mental Model

Think of names as street signs in a city. Good signs let a stranger navigate without a map: "Station Road" probably leads to the station, and consistent sign design means you always know where to look. Bad signs — or streets named "Road 2" — force everyone to stop and ask directions at every corner. Your codebase is a city your teammates visit daily. Every variable, function, and file name is a sign; conventions are the city-wide signage standard that makes the whole place navigable.

## Mini Summary

- ✔ CSS: kebab-case; JS variables/functions: camelCase; React components: PascalCase
- ✔ Names describe *what*, not *how*: `getUserData()` not `queryDatabaseAndReturnUser()`
- ✔ BEM: `.block__element--modifier` — makes HTML/CSS relationships explicit
- ✔ Descriptive > brief for all names longer than single-use loop counters

# Guided Practice Quest

**The Lexicon of the Craft** — three naming convention questions. Steps in `guidedSteps`.

# Solo Practice Quest

Rename all the following to follow conventions and add descriptive meaning:

CSS: `.x { }`, `.tbl-hdr { }`, `.c2 { }`
JS: `function getData(x) { }`, `const a = [];`, `let f = false;`
React: `function card() { }`, `function userprofilepage() { }`

Write the renamed versions and explain each choice.

# Integration

**Connecting to Psychology — Cognitive Load and Chunk Theory**

George Miller's research on working memory shows humans process information in "chunks" — meaningful units. A well-named function (`getUserData`) is processed as a single chunk: "something that gets user data." A poorly named function (`g`) has no chunk — the reader must open the function to create one. Good names offload cognitive work from the reader to the author. Every descriptive name reduces the reader's working memory load by providing pre-formed chunks. Code with good names reads like natural language; code with poor names reads like encrypted text.

# Lore Conclusion

*"Name things for the reader, not the writer,"* says Master Aelindra. *"The writer knows what `.ct` means — they just wrote it. The reader knows nothing. Every name is a message from the past to the future. Write messages worth receiving."*

---
