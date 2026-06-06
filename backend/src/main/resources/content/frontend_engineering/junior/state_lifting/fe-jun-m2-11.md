---
id: fe-jun-m2-11
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m2
moduleTitle: "Module 2: State Management"
moduleGlyph: "🔄"
moduleSortOrder: 2
topicSlug: state_lifting
topicTitle: "State Lifting"
topicSortOrder: 4
lesson: shared_state_patterns
title: "Shared State Patterns"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies which component should own shared state"
    - "Passes state and setter down via props correctly"
    - "Explains the single source of truth principle"
    - "Recognises when sibling components need to share state"
  keywords: [shared-state, lifting, owner, single-source-of-truth, sibling, parent, props, controlled]
  modelAnswer: |
    When two sibling components need the same state, lift it to their nearest common
    ancestor. The parent owns the state and passes both the value and the setter as
    props. This creates a single source of truth — one place controls the value,
    preventing siblings from diverging. The pattern: identify the lowest common ancestor,
    move state there, pass down as props.
guidedSteps:
  - id: fe-jun-m2-11-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A `SearchInput` component and a `ResultsList` component both need the current search query. Where should the query state live?
    inputConfig:
      options:
        - "In SearchInput — it owns the input"
        - "In ResultsList — it owns the display"
        - "In their shared parent component"
        - "In both components independently"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["In their shared parent component"]
      rejectedFeedback: "State that two siblings share must live in their nearest common ancestor. The parent passes query to ResultsList and setQuery to SearchInput. One source of truth — when the query changes in SearchInput, ResultsList re-renders with the new value automatically."
    hint: "Who can see both siblings?"
    reflectionPrompt: "Single source of truth: one place owns the value. When two components each maintain their own copy of the same state, they drift apart. Lifting to the parent ensures they always show the same value."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the 'single source of truth' principle in React state management?"
    options:
      - "All state must live in the root App component"
      - "Each piece of state has exactly one authoritative location"
      - "State should be duplicated for performance"
      - "State should always be in a global store"
    correctIndex: 1
    feedback: "Single source of truth: one component owns each piece of state. Other components receive it via props. This prevents inconsistency — you can't have two components showing different values for the same data."

retrieval:
  recall: "Describe the pattern for sharing state between two sibling components."
  explain: "Explain why having state in two sibling components independently leads to bugs."
  mistakeId:
    code: "Both SearchInput and ResultsList each have their own query state"
    answer: "They will diverge. Lift query to the parent. Pass query as prop to ResultsList; pass setQuery as prop to SearchInput. One state, two consumers."
---

# Hook

Two sibling components need the same value. Where does the state live? Whoever owns the state controls the truth — and there can only be one truth.

# Lore Introduction

*"Two towers,"* says Master Aelindra, *"cannot each keep their own copy of the Academy's roll call. One scroll, kept in the central hall, consulted by both. That is state lifting."*

# Core Learning

## Concept Introduction

**Lifting state up — the pattern:**

```jsx
function SearchPage() {
  const [query, setQuery] = useState('');  // ← lifted here

  return (
    <>
      <SearchInput query={query} onQueryChange={setQuery} />
      <ResultsList query={query} />
    </>
  );
}

function SearchInput({ query, onQueryChange }) {
  return <input value={query} onChange={e => onQueryChange(e.target.value)} />;
}

function ResultsList({ query }) {
  return <ul>{/* filter by query */}</ul>;
}
```

**Decision rule:** Move state to the lowest common ancestor of all components that need it.

## Common Mistakes
- State in the wrong component — causes one-way sync issues
- Lifting too high — prop drilling results (covered in the next lesson)
- Duplicating state instead of lifting

## Mini Summary
- ✔ Shared state lives in the nearest common ancestor
- ✔ Single source of truth: one owner, multiple consumers via props
- ✔ Parent passes value to one child, setter to another

# Solo Practice Quest

Build a filter + list pair: a FilterBar with a category select and a ProductList that shows only matching products. Lift the selected category state to their shared parent.

# Integration

**Mathematics — Referential Integrity:** Shared state is a foreign key relationship. Multiple components reference one value; when it changes, all references update automatically — the same guarantee relational databases provide with foreign keys.

# Lore Conclusion

*"The central scroll is consulted, never copied. State is lifted, never duplicated."*

---
