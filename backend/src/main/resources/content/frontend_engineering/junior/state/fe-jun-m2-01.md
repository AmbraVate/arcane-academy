---
id: fe-jun-m2-01
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m2
moduleTitle: "Module 2: State Management"
moduleGlyph: "🔄"
moduleSortOrder: 2
topicSlug: state
topicTitle: "State"
topicSortOrder: 1
lesson: what_is_state
title: "What is State?"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-04]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines state accurately in the context of React"
    - "Distinguishes state from props"
    - "Identifies what belongs in state vs what doesn't"
    - "Explains how state drives re-renders"
  keywords: [state, props, re-render, mutable, internal, dynamic, useState, data]
  modelAnswer: |
    State is data that belongs to a component and can change over time. Unlike props (which are passed from outside and are read-only), state is owned and managed internally. When state changes, React re-renders the component to reflect the new data. State should only hold data that changes over time and affects what is rendered.
guidedSteps:
  - id: fe-jun-m2-01-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Which of these belongs in component state?"
    inputConfig:
      options:
        - "A user's name passed as a prop"
        - "Whether a dropdown menu is open or closed"
        - "A constant list of country names"
        - "The application's colour theme (set once at startup)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Whether a dropdown menu is open or closed"]
      rejectedFeedback: "State is for data that changes over time and affects rendering. A dropdown's open/closed status changes (user can toggle it) and affects what renders. The others are either static or controlled from outside."
    hint: "Does it change? Does it affect what is rendered?"
    reflectionPrompt: "The state minimisation principle: put only what changes and affects rendering into state. Derived values, constants, and data passed from parents don't belong in state."
  - id: fe-jun-m2-01-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Name three things that belong in state for a shopping cart component."
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: [items, quantity, total, selected, open, count, cart]
      rejectedFeedback: "State in a cart: items array, quantities per item, whether a coupon is applied. NOT state: product names from the catalogue (that's data from an API/prop), the tax rate (a constant), the user's name (passed as prop)."
    hint: "Think about what the user can change in a shopping cart."
    reflectionPrompt: "State is what the user's actions change. If a user can modify it via UI interaction, it's probably state."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What happens in React when state changes?"
    options:
      - "The entire page reloads"
      - "The component (and its children) re-renders with the new state"
      - "Nothing — state changes are batched until the page is closed"
      - "Only the specific DOM node that shows the state value updates"
    correctIndex: 1
    feedback: "When state changes, React re-renders the component and all of its children. React's reconciler then diffs the new virtual DOM with the old, and applies only the minimal necessary DOM changes. This is efficient but requires understanding to avoid unnecessary re-renders."
retrieval:
  recall: "What is the key difference between state and props?"
  explain: "What is the 'minimum state principle' — what should and shouldn't be in state?"
  mistakeId:
    code: "Storing derived values in state: const [fullName, setFullName] = useState(firstName + ' ' + lastName)"
    answer: "Derived values should not be in state. fullName can be computed from firstName and lastName — storing it separately creates a risk of them going out of sync. Compute derived values during render: `const fullName = firstName + ' ' + lastName`."
---

# Hook

A counter that increments. A modal that opens and closes. A form that tracks input values. These all require something to *remember* — something that persists between renders and drives what appears on screen. That something is **state**.

# Lore Introduction

*"A scroll is not just words,"* Aelindra says. *"It is words plus memory — the knowledge that this page has been read, that this ward has been cast. Memory is state. Without it, every moment is the first."*

# Core Learning

## Concept Introduction

**State** is data owned by a component that can change over time and affects what the component renders.

| Property | Props | State |
|---|---|---|
| Owned by | Parent component | The component itself |
| Mutable by | Cannot be changed | The component via setState |
| Source | Passed from outside | Created internally |
| Triggers re-render | When parent re-renders | When state changes |

## Why It Matters

Without state, React components are purely static — they always render the same output for the same props. State is what makes UIs interactive: it's the memory that tracks what the user has done.

## Worked Example

```jsx
// Counter — state tracks the count
function Counter() {
  const [count, setCount] = useState(0);
  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={() => setCount(count + 1)}>+</button>
    </div>
  );
}
```

`count` is state: it changes (user clicks +), it's owned by the component, and it drives rendering.

## Common Mistakes

- **Storing everything in state.** Constants, computed values, and received props don't belong in state.
- **Mutating state directly.** `state.count = 5` doesn't trigger a re-render. Always use the setter.
- **Overusing state.** Ask: can this be derived from existing state or props? If yes, don't store it in state.

## Mini Summary

- State is internal, mutable data that affects rendering
- Unlike props, state is owned and changed by the component itself
- Changing state triggers a re-render
- Only store in state what changes over time and cannot be derived

# Guided Practice Quest

Work through the guided steps on identifying what belongs in state.

# Solo Practice Quest

Design a `TodoItem` component. Identify every piece of data it would need: which should be props (passed from the parent), which should be local state (managed internally), and which should be derived (computed from other values). Write the full list with justifications. Aim for at least six data points.

# Integration

**Mathematics — Finite State Machines**

Every interactive UI component is, at its core, a finite state machine: a system with a fixed set of possible states, transitions between them, and outputs for each state. A toggle has two states: on and off. A form has states: idle, loading, success, error. Finite state machine (FSM) theory gives us tools to reason about, document, and verify these transitions. When UIs have bugs, they often result from missing transitions — the machine reaches a state that wasn't designed for. FSMs are a mathematical model that make these gaps visible.

# Lore Conclusion

*"Memory,"* Aelindra says, *"is the difference between a photograph and a story. State gives your component memory. Without it, it forgets everything every render."*

---
