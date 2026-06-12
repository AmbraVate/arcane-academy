---
id: fe-jun-m2-06
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m2
moduleTitle: "Module 2: State Management"
moduleGlyph: "🔄"
moduleSortOrder: 2
topicSlug: use_state
topicTitle: "useState"
topicSortOrder: 2
lesson: state_and_rerenders
title: "State and Re-renders"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m2-05]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what triggers a re-render"
    - "Understands that re-renders include all children"
    - "Explains why unnecessary re-renders matter for performance"
    - "Names basic strategies to reduce re-renders"
  keywords: [re-render, reconciliation, virtual DOM, children, memo, key, trigger, performance]
  modelAnswer: |
    A React component re-renders when its state changes, its parent re-renders, or its props change. When a component re-renders, all its children re-render too (unless memoised). React's reconciler diffs the new virtual DOM against the old and applies minimal DOM changes. Unnecessary re-renders are a performance concern — React.memo can prevent them for pure components.
guidedSteps:
  - id: fe-jun-m2-06-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Component A has a child B. A's state changes. What happens to B?"
    inputConfig:
      options:
        - "Nothing — B only re-renders when its own state changes"
        - "B re-renders automatically (unless wrapped in React.memo)"
        - "B's props are updated but it doesn't re-render"
        - "B unmounts and remounts"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["B re-renders automatically (unless wrapped in React.memo)"]
      rejectedFeedback: "React re-renders a component and all its descendants when state changes. This is safe because React's reconciler only applies actual DOM changes — but the function is still called, which can be expensive for complex children."
    hint: "The component tree re-renders top-down when state changes."
    reflectionPrompt: "React.memo wraps a component and prevents re-renders if props haven't changed. It's the basic tool for avoiding unnecessary child re-renders."
  - id: fe-jun-m2-06-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "Why does React use a Virtual DOM rather than directly updating the real DOM on every state change?"
    inputConfig:
      options:
        - "The Virtual DOM is faster for all operations"
        - "React diffs the virtual DOM to find minimal changes, then applies only those to the real DOM"
        - "The Virtual DOM works without JavaScript"
        - "Direct DOM access is blocked by browsers"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["React diffs the virtual DOM to find minimal changes, then applies only those to the real DOM"]
      rejectedFeedback: "Virtual DOM diffing finds the minimum set of real DOM changes. DOM operations are expensive; computing diffs in memory is cheap. This optimisation makes React's frequent re-renders practical."
    hint: "DOM operations are expensive. In-memory diffing is cheap."
    reflectionPrompt: "The virtual DOM is not a universal performance win — it adds overhead for simple cases. Its value is in making complex, frequent updates manageable. React 19's compiler reduces this overhead further."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Setting state to the same value as the current state — does React re-render?"
    options:
      - "Yes — always re-renders on setState"
      - "No — React bails out if the new value is identical (Object.is comparison)"
      - "It depends on the state type"
      - "Only if the component has children"
    correctIndex: 1
    feedback: "React uses Object.is() to compare new vs old state. If they're the same, React bails out without re-rendering (the 'bailout optimisation'). For objects/arrays, remember: same reference = same value to React."
retrieval:
  recall: "Name three things that trigger a React component to re-render."
  explain: "What is the Virtual DOM and why does React use it?"
  mistakeId:
    code: "Using array index as key when items can be reordered"
    answer: "Keys help React's reconciler match list items between renders. If you use index as key and an item is removed from the middle, all items after it get new keys — React re-renders them all unnecessarily and may produce incorrect results. Use stable unique IDs."
---

# Hook

React re-renders constantly — every state change, every prop update. Understanding what triggers re-renders, what gets re-rendered, and how React minimises DOM work is essential to building performant UIs and debugging mysterious behaviour.

# Lore Introduction

*"When a rune changes,"* Aelindra says, *"the Academy's scribe does not rewrite the whole scroll. She compares old to new, marks the differences, and rewrites only those lines. This is the reconciliation — and its efficiency is what makes frequent transformation possible."*

# Core Learning

## Concept Introduction

**What triggers a re-render:**
1. The component's own state changes (`setState`)
2. The component's parent re-renders (by default)
3. The component's props change
4. A context it consumes changes

**What gets re-rendered:**
- The component and all its descendants (unless memoised)

**What React does during re-render:**
1. Calls the component function (producing new virtual DOM)
2. Diffs new virtual DOM against previous virtual DOM
3. Applies only the changed parts to the real DOM

## Why It Matters

Understanding re-renders helps you diagnose performance problems, avoid unnecessary work, and place state strategically.

## Worked Example

```
App (state: theme)
├── Header (re-renders on theme change)
│   ├── Logo (re-renders unnecessarily)
│   └── Nav (re-renders unnecessarily)
└── Content (re-renders on theme change)
    └── Article (re-renders unnecessarily)

Better: move theme lower or use React.memo on components that don't use it
```

## Common Mistakes

- **State too high in the tree.** Every setState there re-renders the whole tree.
- **Not memoising expensive pure components.** Use React.memo to prevent unnecessary re-renders.
- **Objects as dependencies.** `{ count }` as a prop creates a new object each render, breaking reference equality.

## Mental Model

A state update triggering a re-render works like editing a document with track-changes and a fastidious printer. When you call a setter, React doesn't grab a brush and repaint your screen — it re-runs your component function to produce a fresh *manuscript* (the new JSX), lays it beside the previous one, and lets the printer reprint only the lines that differ (the diff against the virtual DOM). Holding this two-phase picture — *describe everything, change little* — explains the behaviours that otherwise look wasteful or weird. Your whole function re-running on every keystroke is fine: producing the manuscript is cheap; it's the printing (DOM changes) that costs, and React minimises exactly that. Re-renders cascade downward — when a parent reprints, its children's sections get re-described too — which is why state should live *low*: a keystroke in a search box shouldn't re-manuscript the whole page, and moving that state into the SearchBar confines the reprinting to one paragraph. And React batching several setter calls into one reprint stops surprising you: the printer reasonably waits for you to finish marking edits before running the presses. Re-renders aren't the enemy — *wide* re-renders from state perched too high are; place the scoreboard near its only viewer.

## Mini Summary

- State change = component + all children re-render
- React diffs virtual DOM and applies minimal real DOM changes
- Unnecessary re-renders waste CPU — use React.memo for expensive pure components
- Keep state low to minimise re-render scope

# Guided Practice Quest

Work through the guided steps on understanding re-render triggers and the Virtual DOM.

# Solo Practice Quest

Draw a component tree for a simple app (App → Sidebar + Main → Header + Feed → PostCard). For each scenario, describe what re-renders: (1) a post in the Feed gets liked, (2) the Sidebar filters change, (3) the App theme changes. Then suggest where React.memo would reduce unnecessary re-renders.

# Integration

**Mathematics — Tree Diffing Algorithms**

React's reconciliation is a tree diffing problem: given two trees (old and new virtual DOM), find the minimum number of operations to transform one into the other. The general solution is O(n³). React uses heuristics to achieve O(n): same type = update in place, different type = destroy and rebuild, key prop = match items across renders. This approximation works because UI trees rarely change type at the same position. Understanding algorithm complexity (Big O notation) makes these trade-offs legible.

# Lore Conclusion

*"The scribe is fast,"* Aelindra says, *"but not infinitely so. Help her: keep changes small, keep trees focused, and avoid asking her to compare what hasn't changed. Work with the reconciler, not against it."*

---
