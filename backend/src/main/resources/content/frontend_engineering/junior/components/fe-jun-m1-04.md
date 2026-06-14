---
id: fe-jun-m1-04
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m1
moduleTitle: "Module 1: React Foundations"
moduleGlyph: "⚛️"
moduleSortOrder: 1
topicSlug: components
topicTitle: "Components"
topicSortOrder: 2
lesson: what_is_a_component
title: "What is a Component?"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-02]
integrationDomains: [design, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines a React component accurately"
    - "Explains the input/output model of a component"
    - "Distinguishes a component from a plain function"
    - "Uses correct terminology (props, JSX, render)"
  keywords: [component, function, props, JSX, render, return, tree, reuse]
  modelAnswer: |
    A React component is a JavaScript function that accepts props as input and returns JSX describing what should appear on screen. Components are the building blocks of React UIs — they can be composed together into a tree to build complex interfaces from simple pieces.
guidedSteps:
  - id: fe-jun-m1-04-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What does a React component return?"
    inputConfig:
      options:
        - "A DOM node directly"
        - "JSX — a description of what should appear on screen"
        - "A CSS class name"
        - "A promise"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["JSX — a description of what should appear on screen"]
      rejectedFeedback: "Components return JSX, which React converts into DOM nodes. The component describes *what* to show; React handles *how* to show it."
    hint: "React components are descriptive, not imperative."
    reflectionPrompt: "Returning a description (JSX) rather than modifying the DOM directly is the key to React's declarative model."
  - id: fe-jun-m1-04-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write the simplest possible React component — a Greeting that displays 'Hello, World!'."
    inputConfig:
      minWords: 3
    markingRule:
      matchMode: CONTAINS
      accepted: [function, return, Hello]
      rejectedFeedback: "The simplest component: `function Greeting() { return <h1>Hello, World!</h1>; }` — a function that returns JSX."
    hint: "A component is just a function that returns JSX."
    reflectionPrompt: "Component names must start with a capital letter. This is how React distinguishes components from plain HTML elements."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why must React component names start with a capital letter?"
    options:
      - "It's just a convention with no technical meaning"
      - "React uses the capital letter to distinguish custom components from HTML elements"
      - "It makes the code easier to read"
      - "JavaScript requires it for functions"
    correctIndex: 1
    feedback: "In JSX, lowercase tags (like `<div>`) are treated as HTML elements. Capitalised tags (like `<MyComponent>`) are treated as React components. This distinction is enforced by the JSX transformer."
retrieval:
  recall: "What are the two things every React component must do?"
  explain: "Why can't React component names start with a lowercase letter?"
  mistakeId:
    code: "function myComponent() { return <div>Hello</div>; }"
    answer: "Component names must start with a capital letter. `myComponent` would be treated as an unknown HTML element by JSX. The correct name is `MyComponent`."
---

# Hook

You've seen that React is about components. But what exactly *is* a component? The word gets used loosely. Let's be precise: a React component is a JavaScript function that accepts data and returns a description of the UI.

That's it. A function in, JSX out.

# Lore Introduction

*"A rune,"* says Aelindra, *"is not drawn once and left. It is a formula: given these inputs, produce this glyph. Feed it different inputs, and it produces different glyphs — but always according to the same law."*

She traces a symbol in the air. *"Your components are runes. Define the law once. Apply it as many times as needed."*

# Core Learning

## Concept Introduction

A React component is a **JavaScript function** that:
1. Accepts **props** (input data) as its argument
2. Returns **JSX** (a description of what to render)

```jsx
function Greeting({ name }) {
  return <h1>Hello, {name}!</h1>;
}
```

| Part | Description |
|---|---|
| `function Greeting` | The component — must start with capital letter |
| `{ name }` | Props — data passed in from outside |
| `return <h1>...</h1>` | JSX — what to render |
| `{name}` | JS expression embedded in JSX |

## Why It Matters

Understanding components as functions is foundational. It means:
- Components are **predictable**: same props → same output
- Components are **testable**: pass props in, check JSX out
- Components are **composable**: nest components like function calls

## Worked Example

```jsx
function UserCard({ username, avatarUrl, bio }) {
  return (
    <div className="card">
      <img src={avatarUrl} alt={username} />
      <h2>{username}</h2>
      <p>{bio}</p>
    </div>
  );
}

// Usage:
<UserCard
  username="aelindra"
  avatarUrl="/images/aelindra.png"
  bio="Frontend Architect, Arcane Academy"
/>
```

The component defines the structure. Props fill it with data. The same component can render any user.

## Common Mistakes

- **lowercase component names.** React treats `<myComponent>` as an unknown HTML element.
- **Forgetting to return.** A component with no return renders nothing.
- **Returning multiple elements without a wrapper.** JSX must return a single root element — use `<>...</>` (Fragment) if needed.

## Mental Model

A component is a rubber stamp, not a drawing. When you draw a card on a page, you've made one card — want another, draw it all again, and good luck keeping seventeen hand-drawn cards identical. A stamp captures the *design* once; pressing it produces as many identical cards as you like, each one positioned independently. The component function is the stamp's engraving (the JSX it returns is the design), and every `<Card />` in your app is one press of it. The two properties that make stamps powerful carry over exactly: re-carving the engraving updates the look of *every* future press in the whole app at once, and stamps can include space for handwriting — the customisable parts (props, next lessons) that make each press unique while the structure stays uniform. When you spot the same UI pattern twice in a design, your reflex should be: that's one stamp, pressed twice.

## Mini Summary

- A component is a JS function that accepts props and returns JSX
- Component names must start with a capital letter
- JSX is a description of the UI — not direct DOM manipulation
- Same props always produce the same JSX (pure function)

# Guided Practice Quest

Work through the guided steps to confirm you understand the anatomy of a component.

# Solo Practice Quest

Write (or describe) three components for a music player app: one for a track title and artist, one for a play/pause button, and one for a volume slider. For each, describe what props it needs and what JSX it returns. Write 2–3 sentences per component.

# Integration

**Mathematics — Pure Functions**

A React component behaves like a **pure function**: given the same inputs (props), it always produces the same output (JSX). No side effects, no mutations. This mathematical property makes components predictable and testable. In functional programming, pure functions are the foundation of composable, reliable systems. React's component model borrows heavily from functional programming theory — understanding pure functions helps you understand why React components should be written without modifying their inputs or creating side effects.

# Lore Conclusion

*"The rune is defined,"* Aelindra says. *"Function in, glyph out. You understand the atom of React. Now we will learn how to build atoms from inside."*

---
