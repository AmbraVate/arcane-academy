---
id: fe-jun-m1-05
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
lesson: functional_components
title: "Functional Components"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-04]
integrationDomains: [mathematics, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes a syntactically correct functional component"
    - "Uses props correctly inside the component"
    - "Explains why functional components replaced class components"
    - "Understands the role of hooks in functional components"
  keywords: [function, component, arrow, hooks, useState, props, render, class]
  modelAnswer: |
    Functional components are plain JavaScript functions that return JSX. They replaced class components as the standard because they are simpler, shorter, and work seamlessly with hooks. Hooks let functional components access state and lifecycle features that previously required class syntax.
guidedSteps:
  - id: fe-jun-m1-05-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Why did functional components replace class components as the React standard?"
    inputConfig:
      options:
        - "Classes are slower than functions"
        - "Functional components are simpler and work with hooks, which enable state and lifecycle in plain functions"
        - "The React team prefers functional programming aesthetics"
        - "Classes were removed from JavaScript"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Functional components are simpler and work with hooks, which enable state and lifecycle in plain functions"]
      rejectedFeedback: "Hooks (introduced in React 16.8) gave functional components everything class components had — state, lifecycle, context — without the verbose class syntax."
    hint: "Think about what hooks like useState enable."
    reflectionPrompt: "The shift from class to functional components was enabled by hooks. Understanding this history helps you read older codebases confidently."
  - id: fe-jun-m1-05-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write a functional component called `WelcomeBanner` that accepts a `username` prop and renders a heading saying 'Welcome, [username]!'."
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: [WelcomeBanner, username, Welcome, return]
      rejectedFeedback: "Example: `function WelcomeBanner({ username }) { return <h1>Welcome, {username}!</h1>; }` — accept props via destructuring, embed in JSX with {}."
    hint: "Destructure the username prop in the function parameter."
    reflectionPrompt: "Destructuring props in the parameter list is idiomatic React. It makes the component's API explicit at a glance."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which syntax is preferred for React components in modern codebases?"
    options:
      - "Class components with lifecycle methods"
      - "Functional components with hooks"
      - "Function components without hooks"
      - "HOC-wrapped class components"
    correctIndex: 1
    feedback: "Since React 16.8 (2019), functional components with hooks are the standard. New codebases should use them exclusively. Class components still work but are no longer recommended for new code."
retrieval:
  recall: "What did hooks give functional components that they previously lacked?"
  explain: "Write the function signature of a component that accepts `title` and `count` as props."
  mistakeId:
    code: "Functional components cannot have state"
    answer: "Before hooks (pre-React 16.8) this was true. The useState hook gives functional components full state management. Modern functional components have every capability class components had."
---

# Hook

React has two ways to write components: class syntax and function syntax. For years, class components were required for anything with state. Then, in 2019, React introduced hooks — and functional components became capable of everything. Today, functional components are the standard. Class components still work, but you won't write new ones.

# Lore Introduction

*"Two schools of thought once divided the Academy,"* Aelindra says. *"The Old School of Class Mages, and the New School of Function Weavers. The Function Weavers prevailed — not through politics, but through elegance. Their runes were shorter, cleaner, and equally powerful once hooks were discovered."*

# Core Learning

## Concept Introduction

A **functional component** is a plain JS function (or arrow function) that returns JSX:

```jsx
// Function declaration style
function Button({ label, onClick }) {
  return <button onClick={onClick}>{label}</button>;
}

// Arrow function style (also valid)
const Button = ({ label, onClick }) => (
  <button onClick={onClick}>{label}</button>
);
```

| Feature | Functional (modern) | Class (legacy) |
|---|---|---|
| Syntax | Simple function | `class X extends Component` |
| State | `useState` hook | `this.state` |
| Lifecycle | `useEffect` hook | `componentDidMount` etc |
| Readability | High | Lower (more boilerplate) |

## Why It Matters

Reading existing codebases often means encountering class components. Knowing why functional components replaced them helps you work confidently in both styles, and make the right choice when refactoring.

## Worked Example

```jsx
// Class component (legacy)
class Counter extends React.Component {
  constructor(props) {
    super(props);
    this.state = { count: 0 };
  }
  render() {
    return <button onClick={() => this.setState({ count: this.state.count + 1 })}>
      {this.state.count}
    </button>;
  }
}

// Functional component (modern)
function Counter() {
  const [count, setCount] = React.useState(0);
  return <button onClick={() => setCount(count + 1)}>{count}</button>;
}
```

Same behaviour. Dramatically less code.

## Common Mistakes

- **Using `this` in a functional component.** Functional components don't have `this`.
- **Mixing arrow function and function declaration styles inconsistently.** Pick one and be consistent in a codebase.
- **Forgetting that hooks can only be called at the top level.** No hooks inside conditionals or loops.

## Mini Summary

- Functional components are the modern React standard since hooks arrived in 2019
- They're plain JS functions returning JSX
- `useState` and `useEffect` give them state and lifecycle capabilities
- Class components still work but aren't written for new code

# Guided Practice Quest

Work through the guided steps to demonstrate you can write and reason about functional components.

# Solo Practice Quest

Rewrite the following class component as a functional component with hooks. Describe each step of the conversion in 2–3 sentences: what changes, what stays the same, and what becomes simpler.

```jsx
class Greeter extends React.Component {
  render() {
    return <h2>Hello, {this.props.name}!</h2>;
  }
}
```

# Integration

**Mathematics — Functional Programming**

Functional components embody principles from functional programming: functions as first-class values, pure functions (same input → same output), and composition. Lambda calculus — the mathematical foundation of functional programming — treats computation as function application. React's component model is lambda calculus applied to UI: components are functions, composition is function application, and the UI is a pure function of application state. This is not just metaphor — the React team has cited functional programming explicitly as an influence.

# Lore Conclusion

*"The Function Weavers write less and achieve more,"* Aelindra says. *"This is not laziness — it is clarity. Every unnecessary symbol in a rune is a place for error to hide."*

---
