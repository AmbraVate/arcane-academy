---
id: fe-jun-m2-10
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
lesson: lifting_state_up
title: "Lifting State Up"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-06, fe-jun-m2-01]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains when and why to lift state"
    - "Moves state to the lowest common ancestor correctly"
    - "Passes callback props for children to update parent state"
    - "Understands the cost of lifting (re-renders, prop passing)"
  keywords: [lift, common ancestor, sibling, callback, share, parent, props, coordination]
  modelAnswer: |
    State lifting moves state from a child component to its parent (the lowest common ancestor of all components that need it). This allows siblings to share the same state. The parent passes the state value down as a prop and passes a callback function so children can request state updates. The cost is that the parent re-renders when state changes.
guidedSteps:
  - id: fe-jun-m2-10-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Two sibling components both need to know whether a user is logged in. Where should the auth state live?"
    inputConfig:
      options:
        - "In both sibling components separately"
        - "In the lowest common ancestor of both siblings"
        - "In a global CSS variable"
        - "In one sibling, with the other polling it"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["In the lowest common ancestor of both siblings"]
      rejectedFeedback: "Siblings cannot share state directly. The state must live in their shared ancestor, which passes it down to both. This is 'lifting state up' — moving state to the level where it's accessible to all who need it."
    hint: "React data flows downward. To share between siblings, go up to the parent."
    reflectionPrompt: "The lowest common ancestor minimises re-render scope. Don't lift higher than needed — state in App re-renders the whole tree."
  - id: fe-jun-m2-10-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "How does a child component update state that lives in its parent?"
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [callback, function, prop, handler, onUpdate, onChange, onClick]
      rejectedFeedback: "The parent passes a callback function as a prop: `<Child onChange={handleChange} />`. The child calls `props.onChange(newValue)` when it needs to update the state. The parent's handler calls `setState`."
    hint: "The parent provides the setter; the child calls it."
    reflectionPrompt: "Callbacks as props are how children communicate upward. This maintains unidirectional data flow: state lives in the parent, changes flow through callbacks, updated state flows back down as props."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the risk of lifting state too high (e.g., to App)??"
    options:
      - "It's impossible to access the state"
      - "Every state change re-renders the entire component tree unnecessarily"
      - "React throws an error for state in App"
      - "The state becomes immutable"
    correctIndex: 1
    feedback: "State in App re-renders App and ALL descendants on every change. Keep state in the lowest common ancestor — not higher. This minimises the re-render scope."
retrieval:
  recall: "What is the 'lowest common ancestor' rule for state placement?"
  explain: "Write the pattern for a parent passing a state update callback to a child."
  mistakeId:
    code: "Storing auth state in two separate sibling components"
    answer: "Duplicate state in siblings will inevitably drift out of sync. When one updates, the other doesn't know. Lift the state to their shared parent — one source of truth, passed to both as props."
---

# Hook

Two components on the same page both need to know whether the user has selected something. They're siblings — neither is the other's parent. How do they share information? By lifting the state to their closest common ancestor, which manages the shared truth and distributes it downward.

# Lore Introduction

*"When two rune-circles must respond to the same event,"* Aelindra says, *"they cannot speak directly — they are equals in the hierarchy. Instead, they each bind to the same registry node above them. The node receives the event and broadcasts to both."*

# Core Learning

## Concept Introduction

```jsx
// State lifted to the parent
function FilterableList() {
  const [filter, setFilter] = useState(''); // ← lifted here

  return (
    <div>
      <SearchInput value={filter} onChange={setFilter} />
      <ItemList filter={filter} />
    </div>
  );
}

// Child 1: reads and updates state
function SearchInput({ value, onChange }) {
  return <input value={value} onChange={e => onChange(e.target.value)} />;
}

// Child 2: reads state
function ItemList({ filter }) {
  const items = getItems().filter(i => i.name.includes(filter));
  return <ul>{items.map(i => <li key={i.id}>{i.name}</li>)}</ul>;
}
```

## Why It Matters

Lifting state is the primary coordination mechanism in React. Before reaching for Context or Redux, ask: can I lift state to the common ancestor? Often, this is sufficient and simpler.

## Common Mistakes

- **Lifting too high.** Puts state near root, causing widespread re-renders.
- **Not lifting far enough.** State lives in one sibling, the other can't access it.
- **Direct sibling communication.** Siblings cannot talk directly — always through the parent.

## Mini Summary

- Lift state to the lowest common ancestor of all components that need it
- Parent passes state down as prop, and callback down as prop
- Children call the callback to request state updates
- Keep state as low as possible — lift only as far as needed

# Guided Practice Quest

Work through the guided steps on identifying when and where to lift state.

# Solo Practice Quest

You have a Tabs component and a TabPanel component. Both need to know which tab is selected. Currently each has its own `activeTab` state — they're out of sync. Refactor: lift the state to a parent `TabbedSection` component. Write the three component signatures showing props and callbacks. Explain in 2 sentences what changed and why it eliminates the sync problem.

# Integration

**Design — Centralised vs Distributed Control**

Lifting state is a form of centralised control. Systems design faces the same choice: centralised coordination (one source of truth) vs distributed (each node self-manages). Centralised is simpler to reason about but creates a single point that must be maintained. Distributed is more resilient but harder to keep in sync. React's recommendation (lift to lowest common ancestor) is a middle path: centralise only what must be shared, distribute everything else. This mirrors good system design.

# Lore Conclusion

*"The registry node,"* Aelindra says, *"carries the burden of coordination — but it also carries the guarantee of consistency. Two rune-circles bound to the same node always agree. This is worth the hierarchy."*

---
