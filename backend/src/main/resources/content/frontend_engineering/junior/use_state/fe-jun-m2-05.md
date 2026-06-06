---
id: fe-jun-m2-05
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
lesson: updating_state
title: "Updating State"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m2-04]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Updates primitive state correctly"
    - "Updates object state with spread operator"
    - "Updates array state without mutation"
    - "Explains why immutability matters for React"
  keywords: [immutability, spread, mutation, setState, object, array, map, filter, concat]
  modelAnswer: |
    React state must be updated immutably — you never modify the existing state object, but return a new one. For objects, use the spread operator to copy existing properties before overriding the changed ones. For arrays, use map/filter/concat instead of push/splice/pop. React detects changes by reference equality, so mutated objects (same reference) are not detected as changed.
guidedSteps:
  - id: fe-jun-m2-05-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You have `const [user, setUser] = useState({ name: 'Alice', age: 30 })`. How do you update just the name?"
    inputConfig:
      options:
        - "`user.name = 'Bob'; setUser(user)`"
        - "`setUser({ ...user, name: 'Bob' })`"
        - "`setUser({ name: 'Bob' })`"
        - "`user = { ...user, name: 'Bob' }`"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["`setUser({ ...user, name: 'Bob' })`"]
      rejectedFeedback: "Spread the existing state and override only the changed property: `setUser({ ...user, name: 'Bob' })`. This creates a new object with all old properties plus the update. Never mutate the existing object directly."
    hint: "Create a new object — don't modify the existing one."
    reflectionPrompt: "The spread operator `{ ...user, name: 'Bob' }` copies all properties from `user`, then name is overridden. Properties that appear later override earlier ones. This is immutable update."
  - id: fe-jun-m2-05-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "You have `const [items, setItems] = useState(['a', 'b', 'c'])`. How do you add 'd' to the end?"
    inputConfig:
      options:
        - "`items.push('d'); setItems(items)`"
        - "`setItems([...items, 'd'])`"
        - "`setItems(items.push('d'))`"
        - "`items[3] = 'd'; setItems(items)`"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["`setItems([...items, 'd'])`"]
      rejectedFeedback: "Spread the existing array into a new array and add the new item: `setItems([...items, 'd'])`. push() mutates the original — React won't detect the change because the reference is the same."
    hint: "Never mutate an array in React state. Create a new one."
    reflectionPrompt: "Array immutability: add = spread + new item, remove = filter, update = map. These three operations cover 90% of array state updates."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why does `items.push('d'); setItems(items)` fail to trigger a re-render correctly?"
    options:
      - "push() is not a valid JavaScript method"
      - "React compares state by reference; mutating items gives the same reference, so React thinks nothing changed"
      - "setItems only works with primitive values"
      - "push() is asynchronous"
    correctIndex: 1
    feedback: "React detects state changes by reference equality (`===`). Mutating the array changes its contents but not its reference — it's still the same array object. React sees the same reference and skips the re-render. Create a new array to create a new reference."
retrieval:
  recall: "Write the immutable update pattern for: removing an item from an array by id."
  explain: "Why does React compare state by reference rather than by deep equality?"
  mistakeId:
    code: "setUser(prev => { prev.name = 'Bob'; return prev; })"
    answer: "Even inside a functional update, you must not mutate. This returns the same object reference — React may not detect the change. Correct: `setUser(prev => ({ ...prev, name: 'Bob' }))` — creates and returns a new object."
---

# Hook

React state must be treated as immutable. Not because JavaScript enforces it — it doesn't. But because React detects changes by comparing references, and mutating an object doesn't change its reference. Mutation = invisible update = no re-render = confused UI. The discipline of immutability is the key to correct React state.

# Lore Introduction

*"The memory crystal,"* Aelindra says, *"is never erased. When you update it, you create a new crystal — a perfect copy with one change — and swap the old for the new. The old crystal remains, unchanged, in the archive. This is the way of the immutable."*

# Core Learning

## Concept Introduction

**Immutable update patterns:**

```jsx
// Primitives — just replace
setCount(count + 1);
setName('Alice');
setIsOpen(true);

// Objects — spread + override
setUser({ ...user, name: 'Bob' });
setUser(prev => ({ ...prev, age: prev.age + 1 }));

// Arrays — non-mutating methods
// Add
setItems([...items, newItem]);
// Remove
setItems(items.filter(item => item.id !== idToRemove));
// Update
setItems(items.map(item => item.id === id ? { ...item, done: true } : item));
```

## Why It Matters

React's change detection is reference-based. A new reference = change detected = re-render. Same reference = no change = no re-render (even if values inside changed). Immutable patterns ensure new references.

## Common Mistakes

- **`push()` on array state.** Mutates — same reference — no re-render.
- **`array[index] = value`.** Direct index assignment — same reference.
- **`obj.prop = value`.** Direct property mutation — same reference.
- **Nested object mutation.** Even nested: `setUser(prev => { prev.address.city = 'X'; return prev; })` — must spread at each level.

## Mini Summary

- Never mutate state — always create new objects/arrays
- Objects: `{ ...existing, changedProp: newValue }`
- Arrays add: `[...existing, newItem]`
- Arrays remove: `existing.filter(item => ...)`
- Arrays update: `existing.map(item => ...)`

# Guided Practice Quest

Work through the guided steps on immutable state update patterns.

# Solo Practice Quest

Write state update handlers for a todo list:
1. Add a new todo (object with id, text, done:false)
2. Toggle a todo's done status by id
3. Delete a todo by id
4. Edit a todo's text by id

Use only immutable patterns. Then write a sentence explaining why `push`, `splice`, and direct assignment must be avoided.

# Integration

**Mathematics — Persistent Data Structures**

Functional programming uses persistent data structures — when you "modify" a value, you create a new version while the old persists. This is exactly React's state model. Persistent data structures power efficient undo/redo, time-travel debugging, and concurrent rendering. React DevTools' time-travel feature works because each state update creates a new immutable snapshot. Persistent data structures also underlie version control systems — each commit is an immutable snapshot; the history is a tree of snapshots.

# Lore Conclusion

*"Never erase the old crystal,"* Aelindra says. *"Create the new. React will compare them, apply the difference, and archive the old. This is what makes time-travel possible. This is what makes correctness possible."*

---
