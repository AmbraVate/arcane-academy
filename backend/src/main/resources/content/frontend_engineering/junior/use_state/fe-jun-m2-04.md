---
id: fe-jun-m2-04
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
lesson: the_usestate_hook
title: "The useState Hook"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m2-01]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly uses useState destructuring syntax"
    - "Explains what useState returns"
    - "Names state variables and setters appropriately"
    - "Understands the initial value parameter"
  keywords: [useState, hook, destructure, setter, initial value, array, pair, convention]
  modelAnswer: |
    useState is called with an initial value and returns a pair: the current state value and a setter function. The pair is typically destructured using array destructuring. The naming convention is [value, setValue]. The initial value is only used on the first render — after that, React remembers the current state between renders.
guidedSteps:
  - id: fe-jun-m2-04-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What does `const [name, setName] = useState('Alice')` do?"
    inputConfig:
      options:
        - "Creates a global variable named 'name' with value 'Alice'"
        - "Creates state with initial value 'Alice'; name holds the current value, setName updates it"
        - "Imports name and setName from React"
        - "Creates a constant that cannot be updated"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Creates state with initial value 'Alice'; name holds the current value, setName updates it"]
      rejectedFeedback: "useState(initialValue) returns [currentValue, setter]. Destructuring assigns them to name and setName. 'Alice' is only used on the first render."
    hint: "useState returns an array of two things: current value and updater function."
    reflectionPrompt: "The array destructuring syntax lets you name the pair whatever you want. React doesn't care about the names — but the convention [value, setValue] is universal."
  - id: fe-jun-m2-04-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write a useState declaration for tracking whether a sidebar is open (default: false)."
    inputConfig:
      minWords: 3
    markingRule:
      matchMode: CONTAINS
      accepted: [useState, false, sidebarOpen, isOpen, open, setSidebar, setIsOpen, setOpen]
      rejectedFeedback: "Example: `const [isSidebarOpen, setIsSidebarOpen] = useState(false);` — boolean initial value, clear naming convention."
    hint: "Boolean state: start with false, use is/has/should prefix."
    reflectionPrompt: "Naming convention: `is` prefix for booleans (isOpen, isLoading, isValid), no prefix for values (count, name, items). This communicates type at a glance."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "When is the initial value passed to useState used?"
    options:
      - "On every render"
      - "Only on the first render — React remembers state between renders"
      - "Only when the component unmounts"
      - "Whenever the component's parent re-renders"
    correctIndex: 1
    feedback: "Initial value is used only once — the first time the component mounts. After that, React tracks the current state value internally. This is why useState is called a 'hook' — it hooks into React's memory system."
retrieval:
  recall: "What two things does useState return?"
  explain: "Why is the naming convention [value, setValue] important?"
  mistakeId:
    code: "const count = useState(0)"
    answer: "useState returns an array [value, setter]. Without destructuring, count holds the whole array — [0, function]. The correct form: `const [count, setCount] = useState(0)`. This is one of the most common beginner mistakes."
---

# Hook

Every interactive React component begins with the same hook: `useState`. It's the most fundamental tool in React's API — the hook that gives components memory. Once you understand exactly what it returns and how to use it, everything else follows.

# Lore Introduction

*"The binding rune,"* Aelindra says, *"tethers a memory to a weaver's work. Call it with an initial memory; it returns two things: the memory itself, and the power to change it."*

# Core Learning

## Concept Introduction

```jsx
import { useState } from 'react';

function Counter() {
  const [count, setCount] = useState(0);
  //     ^value  ^setter    ^initial

  return (
    <div>
      <p>{count}</p>
      <button onClick={() => setCount(count + 1)}>+</button>
    </div>
  );
}
```

| Part | Description |
|---|---|
| `useState(0)` | Hook call with initial value 0 |
| `[count, setCount]` | Array destructuring of the returned pair |
| `count` | Current state value |
| `setCount` | Function to update state (triggers re-render) |
| `0` | Initial value — used only on first render |

## Why It Matters

useState is used in nearly every real React component. Its syntax is fundamental to reading and writing React code.

## Worked Example

```jsx
// String state
const [username, setUsername] = useState('');

// Boolean state
const [isLoading, setIsLoading] = useState(false);

// Array state
const [items, setItems] = useState([]);

// Object state
const [user, setUser] = useState({ name: '', email: '' });

// Number state
const [page, setPage] = useState(1);
```

## Common Mistakes

- **Forgetting to destructure.** `const count = useState(0)` gives you the array, not the value.
- **Calling useState conditionally.** Hooks must be called at the top level — never inside if/else.
- **Mutating object/array state directly.** Always return a new object/array.

## Mental Model

`useState` is a coat-check, and the destructured pair is your ticket stub. A component function re-runs top to bottom on every render — its local variables are pockets that get *emptied* each time. Anything that must survive between runs has to be checked into the cloakroom (React's internal storage), and `useState` is the counter: you hand over an initial value once (`useState(0)` — the first deposit), and on every subsequent render you present your ticket and receive two things: the *current* contents (`count`) and a request form for swapping them (`setCount`). Two cloakroom rules explain the API's apparent strictness. You can't rummage the racks yourself — `count = 5` changes your local copy and the cloakroom never hears about it; only the official form (`setCount(5)`) updates the stored coat *and* notifies the front desk to re-run your component with the new contents. And tickets are issued by position — first `useState` call gets rack one, second gets rack two — which is exactly why hooks can't go inside `if` blocks or loops: shuffle the order of visits and everyone walks out wearing someone else's coat. Initial value as the deposit, value-plus-setter as the stub, setter as the only door to the racks: that's the entire mechanism.

## Mini Summary

- `useState(initial)` returns `[currentValue, setter]`
- Calling the setter triggers a re-render with the new value
- Initial value is only used on the first render
- Hooks must be called at the top level, unconditionally

# Guided Practice Quest

Work through the guided steps on useState syntax and destructuring.

# Solo Practice Quest

Write a `ToggleSwitch` component using useState. It should have an internal isOn state (default false), display different text based on state ("ON" or "OFF"), and toggle on click. Then extend it: add a label prop, an onChange callback prop, and a disabled prop. Show how the component changes when disabled is true.

# Integration

**Mathematics — Tuples**

useState returns a 2-element array — essentially a tuple. Tuples (fixed-length, ordered sequences) are a common data structure in mathematics and type theory. TypeScript's type for useState's return is `[T, Dispatch<SetStateAction<T>>]` — a heterogeneous tuple. Understanding tuples makes array destructuring of hooks intuitive: you're unpacking the two elements of a typed pair. Haskell and other functional languages use tuples extensively for returning multiple values from functions.

# Lore Conclusion

*"Two things return from the binding rune,"* Aelindra says. *"The memory, and the power to rewrite it. Hold both. Use both. They are inseparable."*

---
