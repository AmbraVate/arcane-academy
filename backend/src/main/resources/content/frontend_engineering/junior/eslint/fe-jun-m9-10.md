---
id: fe-jun-m9-10
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m9
moduleTitle: "Module 9: Modern Frontend Tooling"
moduleGlyph: "🔧"
moduleSortOrder: 9
topicSlug: eslint
topicTitle: "ESLint"
topicSortOrder: 4
lesson: what_is_linting
title: "What is Linting?"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m9-09]
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what linting is and what problems it catches"
    - "Distinguishes linting from type checking"
    - "Names three common ESLint rules and what each prevents"
    - "Explains the difference between a lint warning and a lint error"
  keywords: [ESLint, lint, static analysis, rule, error, warning, code quality, bug, runtime, syntax]
  modelAnswer: |
    Linting is static analysis — examining code without executing it to find potential bugs, anti-patterns, and style issues. ESLint applies configurable rules to your JavaScript/TypeScript. Unlike TypeScript, which checks types, ESLint checks patterns: unused variables (could indicate a bug), console.log left in production, == instead of ===, no-var (use const/let), react-hooks/rules-of-hooks (hooks must be called unconditionally). Errors block the build (or PR in CI); warnings are shown but don't block. Well-configured ESLint catches bugs before they reach the browser — static analysis is faster than runtime debugging.
guidedSteps:
  - id: fe-jun-m9-10-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "ESLint reports `no-unused-vars: 'data' is assigned but never used`. Why is this rule useful?"
    inputConfig:
      options:
        - "Unused variables waste memory at runtime"
        - "An unused variable often indicates a bug — you assigned data but forgot to use it"
        - "ESLint requires all variables to be used for performance reasons"
        - "TypeScript cannot detect unused variables, so ESLint must"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["An unused variable often indicates a bug — you assigned data but forgot to use it"]
      rejectedFeedback: "no-unused-vars catches a common class of bugs: you fetched data, parsed a response, or computed a value — and then forgot to use it. The variable exists; the intent is missing. Maybe you renamed the variable but missed one usage. Maybe you refactored and forgot to remove dead code. The lint rule surfaces this before runtime."
    hint: "If you declared a variable but never read it, what might that tell you about the code?"
    reflectionPrompt: "Static analysis is cheap (milliseconds) compared to debugging a production bug. Rules like no-unused-vars catch bugs before they manifest as runtime errors."
  - id: fe-jun-m9-10-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "What is the difference between a lint error and a lint warning? Which should be used for rules that could indicate real bugs?"
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [error, warning, block, fail, CI, build, serious, critical, real, bug]
      rejectedFeedback: "Errors fail the lint check (npm run lint exits with code 1, blocking CI). Warnings are shown but don't fail. Rules that could indicate real bugs (no-unused-vars, no-undef, react-hooks/exhaustive-deps) should be errors — you want them to block the PR. Style preferences (prefer-const, no-console) could be warnings. If everything is a warning, developers ignore everything. Be selective: error for bugs, warn for style."
    hint: "How does CI know whether to block a PR based on lint results?"
    reflectionPrompt: "Warning fatigue is real. If your lint output has 200 warnings, developers learn to ignore them. Keep errors for things that matter; warnings are lower priority. Ideally, fix warnings too — or don't add the rule."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "ESLint and TypeScript both analyse code without running it. What is the key difference?"
    options:
      - "ESLint is faster; TypeScript is more accurate"
      - "TypeScript checks type correctness; ESLint checks code patterns, style, and potential bugs"
      - "ESLint checks syntax; TypeScript checks logic"
      - "They check identical things but ESLint is older"
    correctIndex: 1
    feedback: "TypeScript's compiler checks that types are correct — that you're calling functions with the right argument types. ESLint checks patterns — that you're using === not ==, not leaving console.log in code, not calling hooks conditionally. Both are static analysis; they check different dimensions of code quality. Use both."
retrieval:
  recall: "Name three ESLint rules and what each prevents."
  explain: "Why is linting considered 'static analysis' and what makes it different from runtime testing?"
  mistakeId:
    code: |
      function calculateTotal(items) {
        const total = items.reduce((sum, item) => sum + item.price, 0);
        const tax = total * 0.2;
        return total; // Bug: forgot to add tax
      }
    answer: "The no-unused-vars rule would flag `tax` as assigned but never used — a direct clue that something is wrong. ESLint would report: 'tax is assigned a value but never used'. Without this lint rule, this bug only appears when the tax is missing from the total in production. With the rule, it's caught the moment you save the file. This is static analysis at its most useful: surfacing intent-implementation mismatches."
---

# Hook

You shipped a production bug: `if (user.role = 'admin')` — a single `=` instead of `==`. The assignment always returns `'admin'` (truthy), so every user became an admin. Your tests didn't catch it. Your code review didn't catch it.

ESLint's `no-assign-in-condition` rule would have caught it the moment you typed it.

# Lore Introduction

*"The proofreader does not wait for the book to be printed before finding errors,"* the Academy Librarian explains. *"They read the manuscript, marking mistakes — misspellings, repeated words, logical contradictions — before a single copy is made."*

She taps the ESLint output. *"This is your proofreader. It reads your code before it runs. It finds mistakes before they become bugs."*

# Core Learning

## Concept Introduction

**ESLint** is a static analysis tool for JavaScript and TypeScript. It applies configurable **rules** to your code — rules that catch patterns likely to be bugs or bad practices.

**Categories of rules:**

| Category | Example rule | What it prevents |
|---|---|---|
| **Possible errors** | `no-unused-vars` | Assigned but unused variables (likely bugs) |
| **Best practices** | `eqeqeq` | `==` instead of `===` (type coercion bugs) |
| **ES6+** | `no-var` | `var` instead of `const`/`let` |
| **React** | `react-hooks/rules-of-hooks` | Calling hooks conditionally |
| **Style** | `no-console` | console.log in production code |

**How it differs from TypeScript:**
- TypeScript: checks **types** — "you passed a string where a number was expected"
- ESLint: checks **patterns** — "you used == instead of ===", "you called a hook inside an if statement"

Both run without executing the code — both are static analysis. Use both.

## Why It Matters

Linting catches an entire class of bugs before they reach the browser:
- Typos in variable names that JavaScript silently accepts
- React hook rules violations that cause unpredictable behaviour
- Accidental `=` instead of `===` in conditions
- Missing return statements in functions

The cost: seconds. The alternative: hours of debugging a production bug.

## Worked Example

```js
// ESLint catches this without running the code:
function getUser(id) {
  const user = fetchUser(id);  // no-unused-vars: 'user' is assigned but never used
  return fetchUserProfile(id); // did you mean to return 'user'?
}

// And this:
if (process.env = 'production') { // no-assign-in-condition: assignment in condition
  // This always runs because assignment returns the assigned value
}

// And this React bug:
function Component({ isLoading }) {
  if (isLoading) return null;
  const [count, setCount] = useState(0); // react-hooks/rules-of-hooks: conditional hook call
}
```

## Common Mistakes

- **Setting everything to 'warn'.** Warnings are ignored. Use 'error' for rules that catch bugs.
- **Disabling ESLint for large sections.** If you need to disable a rule often, it may be misconfigured or wrong for your project.
- **Not adding React and TypeScript plugins.** The base ESLint config doesn't know React or TypeScript rules. Add `eslint-plugin-react-hooks` and `@typescript-eslint/eslint-plugin`.

## Mini Summary

- ESLint analyses code patterns without running it
- It catches bugs (unused vars, conditional hooks, == vs ===) and enforces best practices
- Errors block CI; warnings don't — use error for bug-catching rules
- ESLint complements TypeScript: different dimensions of code quality

# Guided Practice Quest

Work through the two guided steps to understand what lint rules protect against and how severity levels work.

# Solo Practice Quest

List five ESLint rules you would enable on a React TypeScript project. For each, explain: what code pattern it catches, what bug or problem that pattern causes, and whether you'd set it to error or warn.

# Integration

**Philosophy — Pre-mortem vs Post-mortem**

Gary Klein's research on pre-mortems describes a practice where teams imagine a project has failed and ask "what went wrong?" before starting — identifying failure modes proactively. Static analysis is the code equivalent of a pre-mortem: it analyses the code before execution to identify patterns that typically lead to failure. The philosophical distinction is between reactive (post-mortem: "the bug happened, now let's investigate") and proactive (pre-mortem: "what patterns typically lead to bugs?") quality assurance. ESLint encodes decades of collective experience about which code patterns lead to bugs, and applies that knowledge pre-mortem — before the code runs, before the bug manifests, before the user is affected. This shift from reactive to proactive bug detection is one of the most impactful quality improvements a team can make.

# Lore Conclusion

*"The proofreader has found three errors,"* the Librarian says, reviewing the lint output. *"A contradiction on page four. A repeated binding on page nine. A logic flaw on page twelve. All before printing. All before the apprentices received their copies."*

---
