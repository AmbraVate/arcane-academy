---
id: fe-jun-m9-13
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m9
moduleTitle: "Module 9: Modern Frontend Tooling"
moduleGlyph: "🔧"
moduleSortOrder: 9
topicSlug: prettier
topicTitle: "Prettier"
topicSortOrder: 5
lesson: what_is_prettier
title: "What is Prettier?"
sortOrder: 1
difficulty: 4
estimatedMinutes: 20
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m9-12]
integrationDomains: [psychology, sociology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what Prettier is and what it formats"
    - "Explains the difference between Prettier and ESLint"
    - "Explains what 'opinionated' means in this context"
    - "Describes why Prettier ends style debates"
  keywords: [Prettier, format, opinionated, style, debate, ESLint, automatic, consistent, whitespace, semicolon]
  modelAnswer: |
    Prettier is an opinionated code formatter — it automatically reformats code to a consistent style. Unlike ESLint, which checks for bugs and patterns, Prettier only cares about visual formatting: indentation, line length, semicolons, quote style, trailing commas. 'Opinionated' means it makes specific choices and doesn't offer much flexibility — you accept its style or you don't use it. This is its strength: it ends style debates. Instead of arguing whether to use single or double quotes, you run Prettier and the debate is over. Code reviews focus on logic, not whitespace.
guidedSteps:
  - id: fe-jun-m9-13-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Prettier reformats your file. The result compiles and runs identically. What has changed?"
    inputConfig:
      options:
        - "The runtime behaviour of the code"
        - "Only the visual appearance — indentation, line breaks, quote style"
        - "The TypeScript types in the file"
        - "The ESLint errors in the file"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Only the visual appearance — indentation, line breaks, quote style"]
      rejectedFeedback: "Prettier is a pure formatter — it changes only how code looks, not what it does. The same program, formatted differently. This is why it's safe to run automatically: it cannot introduce bugs. It adds/removes whitespace, adjusts line lengths, normalises quote styles — all semantically neutral changes."
    hint: "Formatting changes never affect what the code does — only how it looks."
    reflectionPrompt: "Because formatting changes are semantically neutral, Prettier can run automatically without risk. This is the key property that makes automation safe here."
  - id: fe-jun-m9-13-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Your team has been debating for two weeks whether to use single quotes or double quotes. Describe how adopting Prettier would resolve this debate."
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [decide, config, automatic, format, consistent, debate, enforce, choice, one, standard]
      rejectedFeedback: "Prettier makes the choice (via its config — singleQuote: true or false) and enforces it automatically. Once Prettier is configured, the choice is made and applied everywhere — any code committed gets reformatted to the chosen style. The debate becomes irrelevant: it doesn't matter what you type, Prettier will reformat it to the agreed style. Code review comments about quotes become impossible."
    hint: "If a tool automatically enforces the style, does it matter which style was chosen?"
    reflectionPrompt: "The value of Prettier is not which style it chooses — it's that it chooses and enforces automatically. Any consistent style is better than inconsistent styles. Prettier makes consistency free."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the primary difference between Prettier and ESLint?"
    options:
      - "Prettier is faster; ESLint is more accurate"
      - "Prettier formats visual appearance; ESLint checks for bugs and code quality patterns"
      - "Prettier works with TypeScript; ESLint only works with JavaScript"
      - "Prettier is built into VS Code; ESLint requires an extension"
    correctIndex: 1
    feedback: "Prettier: formatting (indentation, line length, quotes) — purely visual, never affects logic. ESLint: code quality (bugs, anti-patterns, style rules that affect meaning). They solve different problems. Use both: ESLint for quality, Prettier for formatting."
retrieval:
  recall: "What does 'opinionated formatter' mean and what is the advantage of this approach?"
  explain: "Why can Prettier safely run automatically on every file save, while ESLint cannot always auto-fix?"
  mistakeId:
    code: |
      // Code review comment on a PR:
      // "This should use double quotes, not single quotes"
      // "Indent this block with 4 spaces, not 2"
      // "Move this closing brace to a new line"
    answer: "These comments should not exist in code review. They waste reviewer time, create friction for the author, and are subjective. The solution: adopt Prettier. It auto-formats all these decisions. Code reviewers can then focus on logic, architecture, naming, and correctness — not whitespace. Style debates in code review are a symptom of missing automation."
---

# Hook

Your team's code review comments include:
- "Use double quotes here"
- "This function should be on one line"
- "The closing brace should be on its own line"

These debates waste time and create friction. No reviewer should spend mental energy on whitespace. Prettier makes these debates obsolete.

# Lore Introduction

*"The scriptorium once spent three days debating whether chapter headings should be centred or left-aligned,"* the Chief Scribe recalls. *"Three days! The content was irrelevant to the debate."*

He holds up a formatting template. *"Now every scribe uses the template. The headings are always centred. There is no debate. There is only work."*

Prettier is the template.

# Core Learning

## Concept Introduction

**Prettier** is an opinionated code formatter. It:
- Parses your code into an AST
- Reprints it from scratch using its own formatting rules
- Ignores how you wrote it — outputs consistent style

**What Prettier formats:**
- Indentation (2 spaces vs 4 spaces)
- Line length (wraps at configured max)
- Semicolons (always, or never)
- Quote style (single vs double)
- Trailing commas (always, sometimes, never)
- Bracket spacing (`{ a: 1 }` vs `{a: 1}`)
- Arrow function parens (`x => x` vs `(x) => x`)

**What Prettier does NOT do:**
- Fix logic errors
- Check types
- Enforce naming conventions
- Apply ESLint rules

**Prettier vs ESLint:**

| | Prettier | ESLint |
|---|---|---|
| Focus | Visual formatting | Code quality & bugs |
| Auto-fixable | Everything it touches | Some rules |
| Opinion level | Very opinionated | Configurable |
| Purpose | Consistency | Correctness |

## Why It Matters

Consistent formatting:
- Makes code reviews focus on logic, not whitespace
- Makes diffs smaller (no style-only changes hidden in real changes)
- Means any developer can work in any file without jarring style shifts
- Ends style debates permanently — the tool decides

## Worked Example

**Before Prettier:**
```js
const user = {name: 'Aria',   age: 24,
  role: 'admin'};
function greet(name){return "Hello, "+name+"!"}
```

**After Prettier:**
```js
const user = { name: 'Aria', age: 24, role: 'admin' };
function greet(name) {
  return 'Hello, ' + name + '!';
}
```

Same program. Completely consistent style. Run it on 200 files — consistent everywhere.

## Common Mistakes

- **Spending time configuring Prettier extensively.** Its value is being opinionated. Accept the defaults or make minimal changes; don't replicate ESLint's configuration complexity.
- **Using Prettier for logic decisions.** It's a formatter, not a code quality tool. Don't expect it to catch bugs.
- **Using ESLint formatting rules alongside Prettier.** They conflict. Install `eslint-config-prettier` to disable ESLint's formatting rules and let Prettier own them.

## Mini Summary

- Prettier is an opinionated code formatter — it reformats code to consistent style automatically
- It handles visual formatting only — quotes, indentation, line length, semicolons
- ESLint handles code quality; Prettier handles style — use both together
- It ends style debates by automating the decision

# Guided Practice Quest

Work through the two guided steps to understand what Prettier changes and how it resolves team style debates.

# Solo Practice Quest

Your team is split: half want 2-space indentation, half want 4-space. Half want semicolons, half don't. Explain why, once Prettier is adopted, this debate is both resolved and irrelevant. What happens to code that uses the "wrong" style after Prettier runs?

# Integration

**Sociology — Schelling Points and Coordination**

Thomas Schelling studied how people coordinate without communication. A "Schelling point" is a natural focal point where coordination converges. In team style debates, there is no natural Schelling point — any consistent choice is equally valid, but choosing requires coordination cost. Prettier acts as an artificial Schelling point: it makes a specific, arbitrary choice and enforces it. The choice doesn't need to be optimal — it needs to be made and enforced consistently. Research on team coordination shows that arbitrary but enforced standards reduce coordination overhead substantially compared to negotiated standards. Teams that debate style spend cognitive resources on a zero-sum argument; teams that adopt Prettier spend those resources on the actual product. The sociological insight: the content of the standard matters far less than its consistency.

# Lore Conclusion

*"The template is adopted,"* the Chief Scribe says. *"Three hundred scrolls in the archive — all consistent. A reader moving from scroll to scroll never stumbles on an unexpected style. The reader's mind is free to engage with the content, not the format."*

---
