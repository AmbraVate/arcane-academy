---
id: fe-jun-m6-06
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m6
moduleTitle: "Module 6: Component Design"
moduleGlyph: "🧩"
moduleSortOrder: 6
topicSlug: separation_of_concerns
topicTitle: "Separation of Concerns"
topicSortOrder: 2
lesson: data_flow_clarity
title: "Data Flow Clarity"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m6-04, fe-jun-m6-05]
integrationDomains: [design, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the 'props down, events up' pattern accurately"
    - "Describes the problem with components that modify shared state without explicit events"
    - "Identifies how clear data flow makes a component easier to understand in isolation"
  keywords: [props, events, down, up, unidirectional, callback, parent]
  modelAnswer: |
    Props down, events up means parent components pass data down to children via props, and children communicate back to parents by calling callback functions (event handlers) passed as props. This creates a unidirectional data flow where the source of truth is always clear. When components modify shared state directly (e.g. by mutating objects or calling context setters deep in the tree), the data flow becomes implicit and hard to trace.
guidedSteps:
  - id: fe-jun-m6-06-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A child component needs to tell its parent that a button was clicked. What is the correct approach?"
    inputConfig:
      options:
        - "The child should directly update the parent's state using a ref"
        - "The child should call a callback function passed down as a prop"
        - "The child should emit a global event using a custom EventEmitter"
        - "The child should update a global variable and the parent re-renders"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The child should call a callback function passed down as a prop"]
      rejectedFeedback: "Events travel up via callback props. The parent passes onSomething={handleSomething} to the child, and the child calls props.onSomething() when appropriate."
    hint: "In React's unidirectional model, events flow upward through the props tree, not through side channels."
    reflectionPrompt: "Why does passing a callback as a prop keep the parent in control of what happens when the button is clicked?"
  - id: fe-jun-m6-06-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "What is 'implicit state coupling' and why is it a problem?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [implicit, coupling, shared, hidden, depend, context, global]
      rejectedFeedback: "Implicit coupling is when a component depends on or modifies state without that dependency being visible in its props signature — making the component's behaviour unpredictable in isolation."
    hint: "Think about what happens when a deeply nested component calls a context setter that five other components depend on."
    reflectionPrompt: "How does making dependencies explicit in props make a component easier to use and test?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does 'unidirectional data flow' mean in React?"
    options:
      - "Data can only be strings or numbers"
      - "Data flows down from parent to child via props; communication goes up via callbacks"
      - "All data must come from a single Redux store"
      - "Components can only render in one direction"
    correctIndex: 1
    feedback: "Unidirectional means data travels one way: down through props. Children communicate back upward by calling callback props, keeping the data flow explicit and traceable."
retrieval:
  recall: "Describe the 'props down, events up' pattern in one or two sentences."
  explain: "Why does a component with no hidden state dependencies become easier to test?"
  mistakeId:
    code: |
      // Child component reaching into parent context without explicit props
      const ChildForm = () => {
        const { setFormData, formData } = useFormContext(); // pulls from context
        return (
          <input
            value={formData.name}
            onChange={e => setFormData({ ...formData, name: e.target.value })}
          />
        );
      };
    answer: "ChildForm is implicitly coupled to FormContext — it can only be used inside a FormContext provider and its behaviour depends on hidden shared state. If the component instead accepted value and onChange as props, it would be a pure, portable input with no hidden dependencies."
---

# Hook

A bug appears in production: a form is submitting before validation completes. You trace the call. A child component deep in the tree is calling a context setter that triggers submission. The parent had no idea this was possible. The implicit coupling hid the dependency entirely.

Clear data flow would have made this impossible to write accidentally.

# Lore Introduction

Water in the Academy's alchemical labs flows through visible channels: input flasks at the top, reaction chambers in the middle, output reservoirs at the bottom. An apprentice who drills a secret pipe from an output reservoir back to an input flask to "save time" causes chaos — the master artificer cannot reason about what is flowing where. Visible channels are not just convention; they are safety.

Your component tree is an alchemical flow system. Keep the channels visible.

# Core Learning

## Concept Introduction

**Unidirectional data flow** is React's core data model. It means:
- **Props flow down:** Parent components pass data to children via props.
- **Events flow up:** Children communicate with parents by calling callback functions passed as props.

This creates a **single source of truth**: state lives in one place (usually a parent or global store), and any component that needs it receives it explicitly as a prop.

```tsx
// Clear data flow — source of truth is in the parent
const ParentForm = () => {
  const [name, setName] = useState('');

  return (
    <NameInput
      value={name}
      onChange={(newName) => setName(newName)}
    />
  );
};

const NameInput = ({ value, onChange }: { value: string; onChange: (v: string) => void }) => (
  <input
    value={value}
    onChange={e => onChange(e.target.value)}
    className="border rounded px-3 py-2"
  />
);
```

`NameInput` has no hidden dependencies. Its entire contract is visible in its props.

## Why It Matters

When data flows unidirectionally and explicitly:
- **Components are predictable:** Given the same props, a component always behaves the same way.
- **Bugs are traceable:** You can follow data from its source to the component and back. No surprises.
- **Components are portable:** A component with no hidden dependencies can be dropped anywhere in the tree.

When data flows implicitly (through mutable shared objects, uncontrolled context writes, or global variables):
- Components become unpredictable — their behaviour depends on invisible shared state.
- Bugs are hard to trace — the mutation could have originated anywhere.
- Components cannot be tested or reused in isolation.

## Worked Example

A common pattern: lifting state up to enable clear data flow.

```tsx
// Before: each input manages its own state — parent cannot access the values
const SearchBar = () => {
  const [query, setQuery] = useState('');
  const [filter, setFilter] = useState('all');
  // Parent has no way to know what these are without reaching in
  return ( /* ... */ );
};

// After: state lifted to parent, flow is explicit
interface SearchBarProps {
  query: string;
  filter: string;
  onQueryChange: (q: string) => void;
  onFilterChange: (f: string) => void;
}

const SearchBar = ({ query, filter, onQueryChange, onFilterChange }: SearchBarProps) => (
  <div className="flex gap-2">
    <input value={query} onChange={e => onQueryChange(e.target.value)} />
    <select value={filter} onChange={e => onFilterChange(e.target.value)}>
      <option value="all">All</option>
      <option value="active">Active</option>
    </select>
  </div>
);

// Parent now owns the state and can use the values for searching
const SearchPage = () => {
  const [query, setQuery] = useState('');
  const [filter, setFilter] = useState('all');

  return (
    <>
      <SearchBar
        query={query}
        filter={filter}
        onQueryChange={setQuery}
        onFilterChange={setFilter}
      />
      <ResultsList query={query} filter={filter} />
    </>
  );
};
```

## Common Mistakes

**Prop drilling avoidance via context overuse.** Context solves prop drilling for *genuinely global* state (current user, theme). Using it to avoid passing 2–3 props through one level creates implicit coupling.

**Mutating prop objects.** `props.user.name = 'Alice'` is an implicit write that bypasses the props-down model. Always treat props as read-only.

**Children that call parent methods directly via refs.** `parentRef.current.submitForm()` is an anti-pattern. The child should emit an `onSubmit` event and let the parent decide what to do.

## Mini Summary

Clear data flow means props travel down through the component tree and events travel up via callback props. Dependencies are explicit in the component's props signature. Components with no hidden dependencies are predictable, portable, and easy to test. Implicit state coupling — through global state, context misuse, or ref mutation — creates hidden dependencies that make components fragile.

# Guided Practice Quest

Work through the steps to trace data flow and identify implicit coupling in component trees.

# Solo Practice Quest

Describe a scenario where a component has hidden, implicit coupling — either from your own experience or invented. Then describe how you would refactor it to make the coupling explicit via props. Cover:

1. What the hidden dependency was
2. How the refactored props interface looks
3. What the component gains from the change

Write 4–6 sentences.

# Integration

**Mathematics — Graph Theory:** A component tree is a directed acyclic graph (DAG). Unidirectional data flow means all edges in the graph point in one direction (parent to child for data, child to parent for events). Implicit state coupling adds back-edges and bidirectional edges, turning the clean DAG into a tangle of cycles — exactly the structure that makes dependency analysis and change impact analysis hard.

**Design — Information Architecture:** Information architects design systems where each piece of information has a clear home and a clear path to where it is needed. Lifting state up, naming callback props clearly (`onSubmit`, `onCancel`, `onSearch`), and passing data explicitly is the IA equivalent of a well-organised filing system: every piece of data has a known location and a documented route.

# Lore Conclusion

The Academy's alchemical channels are visible by law. Every apprentice can stand at the input flask and trace exactly where the compound travels and what reactions it undergoes. Build your component trees with the same clarity: visible inputs, explicit outputs, no secret pipes. A codebase with clear data flow is one where every artificer — no matter how new — can trace the magic.

---
