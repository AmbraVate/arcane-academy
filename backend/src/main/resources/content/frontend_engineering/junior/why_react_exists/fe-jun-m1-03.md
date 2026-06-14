---
id: fe-jun-m1-03
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m1
moduleTitle: "Module 1: React Foundations"
moduleGlyph: "⚛️"
moduleSortOrder: 1
topicSlug: why_react_exists
topicTitle: "Why React Exists"
topicSortOrder: 1
lesson: react_vs_vanilla_js
title: "React vs Vanilla JS"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-01, fe-jun-m1-02]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies when vanilla JS is the better choice"
    - "Identifies when React is the better choice"
    - "Explains the trade-offs accurately"
    - "Avoids religious thinking (React is always better)"
  keywords: [vanilla, React, complexity, trade-off, overhead, bundle, state, interactivity]
  modelAnswer: |
    Vanilla JS is better for simple, mostly static pages where the overhead of a framework isn't justified. React is better when an application has significant interactive state that must be kept in sync across multiple UI elements. The decision should be driven by the complexity of the state management problem, not by familiarity or popularity.
guidedSteps:
  - id: fe-jun-m1-03-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A marketing landing page with one contact form. Which is the better choice?"
    inputConfig:
      options:
        - "React — always use React for web projects"
        - "Vanilla JS or a lightweight library — React adds unnecessary complexity"
        - "Neither — use a server-side framework only"
        - "React — because it's the industry standard"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Vanilla JS or a lightweight library — React adds unnecessary complexity"]
      rejectedFeedback: "React adds a build pipeline, a runtime, and conceptual overhead. For a simple static page with one form, this cost is not justified. Use the simplest tool that solves the problem."
    hint: "Does this page have complex, frequently changing state?"
    reflectionPrompt: "The best tool is the simplest one that solves the problem. Senior engineers choose tools based on problem fit, not popularity."
  - id: fe-jun-m1-03-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "A real-time collaborative document editor. Which is the better choice?"
    inputConfig:
      options:
        - "Vanilla JS — it's faster and simpler"
        - "React (or similar) — complex, frequently changing shared state benefits from a reactive framework"
        - "Neither — this requires a native app"
        - "Vanilla JS with jQuery"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["React (or similar) — complex, frequently changing shared state benefits from a reactive framework"]
      rejectedFeedback: "Real-time collaborative state — many users, frequent changes, complex UI updates — is exactly the problem React was designed for. Vanilla JS would require significant custom state management infrastructure."
    hint: "Think about how many DOM updates a real-time editor needs to coordinate."
    reflectionPrompt: "React's value is proportional to the complexity of your state management problem. Simple state = small benefit. Complex state = large benefit."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the main cost of using React over vanilla JS?"
    options:
      - "React is slower at all tasks"
      - "React requires a build pipeline, adds bundle size, and has a learning curve"
      - "React cannot be used on mobile devices"
      - "React doesn't support CSS"
    correctIndex: 1
    feedback: "React adds real costs: a build step (Vite/webpack), a runtime (~45KB gzipped), and the learning curve of its component model. These costs are worth it when the state complexity is high enough."
retrieval:
  recall: "Name two scenarios where vanilla JS is a better choice than React."
  explain: "Why does React's value increase as application complexity increases?"
  mistakeId:
    code: "Always use React for web projects"
    answer: "This is framework bias, not engineering judgement. The right tool depends on the problem. React adds overhead that's not justified for simple pages. Good engineers choose tools based on fit, not habit."
---

# Hook

A new developer joins a team. Their first week, they're asked to build a contact page with a name field, email field, and a submit button. They spin up Create React App, install 15 dependencies, configure a build pipeline, and write three components.

The same page could have been built in 40 lines of HTML, CSS, and vanilla JS in 20 minutes.

Tools should match problems. This lesson is about knowing when React is the right tool — and when it isn't.

# Lore Introduction

*"Every journeyman's temptation,"* says Aelindra, *"is to use the most powerful spell they know — for every problem, no matter how small. Power has a cost. Wisdom is knowing when to pay it."*

She places two crystals on the table — one simple, one intricate. *"Both light this candle. But one does it with ten times the complexity. For a candle, which do you reach for?"*

# Core Learning

## Concept Introduction

| Factor | Vanilla JS | React |
|---|---|---|
| **Setup** | None (just HTML + JS) | Build pipeline required |
| **Bundle size** | Minimal | ~45KB+ runtime |
| **State management** | Manual | Automatic re-rendering |
| **Best for** | Simple / mostly static pages | Complex interactive UIs |
| **Learning curve** | Low | Medium |
| **Component reuse** | Copy-paste or custom | First-class support |

## Why It Matters

Choosing the wrong tool creates real costs. Using React for a simple page means: a build pipeline to maintain, a larger bundle that users must download, and code that's harder for non-React developers to read. Using vanilla JS for a complex app means: manual DOM synchronisation bugs, growing maintenance burden, no component reuse.

Engineering judgement means matching tool to problem.

## Worked Example

**When vanilla JS wins:**
- A blog with static content and a dark mode toggle
- A landing page with an animated hero and contact form
- A simple calculator with no shared state

**When React wins:**
- A dashboard with live data updates across multiple panels
- A multi-step form wizard with conditional logic
- Any application where multiple UI elements depend on the same changing data

## Common Mistakes

- **Using React because it's popular.** Popularity doesn't mean fitness for your problem.
- **Avoiding React because of its learning curve.** For complex UIs, React's benefits outweigh the learning cost.
- **Thinking this is binary.** Islands architecture, partial hydration, and micro-frontends let you use React where needed and vanilla everywhere else.

## Mental Model

Vanilla JS and React differ like manual bookkeeping versus a spreadsheet. In a ledger you update totals by hand: change one purchase and *you* must remember every dependent figure — subtotal, tax, balance — and recalculate each, in order, without missing one. That's `document.querySelector` and friends: every state change makes you personally responsible for finding and updating each affected piece of UI. A spreadsheet inverts it: cells declare formulas ("this cell = price × quantity"), and when an input changes, every dependent cell recalculates *itself*. React components are formula cells — they declare what they look like in terms of state, and re-derive automatically when it changes. Manual ledgers work fine for three entries; the spreadsheet wins the moment the books get real.

## Mini Summary

- React solves a specific problem: complex, changing state in large UIs
- For simple, mostly static pages, vanilla JS is simpler and better
- The decision should be based on the complexity of your state management problem
- Good engineers choose tools for fit, not familiarity

# Guided Practice Quest

Work through the guided steps — two realistic scenarios — to practise matching tool to problem.

# Solo Practice Quest

Think of three web applications you use regularly. For each one, decide whether React (or a similar framework) is justified or overkill. Write 2–3 sentences per application, explaining your reasoning based on the complexity of the state management problem.

# Integration

**Mathematics — Cost-Benefit Analysis**

Every technology choice has a cost (setup, learning, maintenance, performance overhead) and a benefit (productivity, correctness, reusability). Good engineering decisions involve estimating both sides. As application complexity grows, React's marginal benefit increases while vanilla JS's marginal benefit decreases — at some crossover point, React becomes the rational choice. This is the same cost-benefit logic used in operations research and economics. Engineers who can model trade-offs quantitatively make better tool decisions.

# Lore Conclusion

*"The journeyman who reaches for their greatest spell at every turn,"* Aelindra says, *"exhausts themselves on candles. Wisdom is restraint — and the confidence to use the simple solution when it is correct."*

---
