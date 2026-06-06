---
id: fe-sen-m3-02
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m3
moduleTitle: "Module 3: Performance Engineering"
moduleGlyph: "🚀"
moduleSortOrder: 3
topicSlug: memoisation
topicTitle: "Memoisation"
topicSortOrder: 2
lesson: memoisation
title: "Memoisation"
sortOrder: 1
difficulty: 4
estimatedMinutes: 35
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Correctly distinguishes React.memo (component), useMemo (value), and useCallback (function)
    - Explains the cost of memoisation and when it creates more overhead than it saves
    - Identifies when referential equality matters for props and dependencies
    - Describes the correct use case for each memoisation tool
    - Synthesises a decision framework for when to memoise
  keywords: [React.memo, useMemo, useCallback, referential, equality, shallow, comparison, dependency, expensive, overhead, premature, optimisation]
  modelAnswer: |
    React.memo wraps a component and prevents re-renders when props are shallowly equal. useMemo memoises a computed value, recalculating only when specified dependencies change. useCallback memoises a function reference, returning the same function object across renders when dependencies are unchanged.

    The cost of memoisation is real: each call adds a cache lookup, a dependency comparison, and memory for the cached value. For cheap operations (string concatenation, simple arithmetic), the overhead of memoisation exceeds the cost of recomputation. Memoisation pays when: (a) the computation is genuinely expensive (filtering thousands of items, complex transforms), or (b) referential stability of a value/function is required to prevent unnecessary child re-renders.

    Referential equality matters when a value or function is passed as a prop to a React.memo component or used as a dependency in another hook. A new function reference on every render defeats React.memo (the child sees 'new' props on every render). useCallback stabilises function references; useMemo stabilises object/array references.

    Decision framework: start with no memoisation. Profile. If a component is genuinely expensive and re-renders unnecessarily because of unstable prop references, apply React.memo + useCallback/useMemo to stabilise the references. Never memoise as a default — it creates cognitive overhead and hidden state.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A parent passes `onClick={() => handleDelete(id)}` as a prop to a React.memo child. The child re-renders on every parent render. Why?"
    options:
      - "React.memo doesn't work with function props"
      - "Arrow functions in JSX create a new function reference on every render; the child sees different props each time"
      - "handleDelete causes the child to re-render"
      - "The id variable triggers re-renders"
    correctIndex: 1
    feedback: "Every render creates a new arrow function — a new object reference. React.memo's shallow comparison: `prevProp !== nextProp` → `() => {...} !== () => {...}` → true → re-render. Fix: `const handleDeleteMemo = useCallback(() => handleDelete(id), [id])`. Now the same function reference is passed unless `id` changes."
  - type: SHORT_TEXT
    prompt: "A developer wraps every useMemo and useCallback they write with the comment 'performance optimisation'. Explain what's wrong with this approach, and what the correct philosophy should be."
    hint: "What does memoisation cost? When does the cost exceed the benefit?"
  - type: FILL_BLANK
    prompt: "React.memo prevents component re-renders. useMemo prevents expensive ___ recalculation. useCallback prevents ___ reference changes."
    answer: "value/computation; function"
    hint: "Each tool memoises a different kind of thing."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "When is useMemo genuinely useful?"
    options:
      - "Whenever you compute anything inside a component"
      - "When a computation is expensive AND its result is used in a way that affects rendering or is passed to a memo component"
      - "Always — useMemo never adds overhead"
      - "Only for primitive values (strings, numbers)"
    correctIndex: 1
    feedback: "useMemo is useful when: (a) the computation is expensive (takes >1ms, used frequently), or (b) the result is passed to a React.memo child or used as a useEffect dependency where referential stability matters. For cheap computations, the memoisation overhead exceeds the savings."
  - type: MULTIPLE_CHOICE
    question: "A developer sees a component rendering slowly. They add `useMemo` to every computation in it. Performance is now slightly worse. Why?"
    options:
      - "useMemo is incompatible with the component's props"
      - "Each useMemo call adds a cache lookup and dependency comparison overhead that exceeded the savings"
      - "The React version is too old to optimise useMemo"
      - "The dependencies array is causing infinite loops"
    correctIndex: 1
    feedback: "useMemo isn't free. Each call: allocates memory for the cached value, compares the dependency array on every render (even cache hits). For simple computations (string formatting, basic arithmetic), these overheads exceed the recomputation cost. Premature memoisation makes code slower, not faster."
retrieval:
  recall: "What are the three React memoisation APIs and what does each memoize?"
  explain: "Explain why adding useCallback to a function that's passed to a React.memo child is necessary but not sufficient."
  mistakeId:
    code: |
      function Dashboard({ data }) {
        const stats = useMemo(() => ({
          total: data.length,
          label: `${data.length} items`,
        }), [data]);

        // stats.label is displayed in a <p> tag — no memo child
        // stats is not passed to any memoised component
        return <p>{stats.label}</p>;
      }
    answer: "useMemo here is unnecessary. The computation (data.length, string template) is trivial — microseconds. There's no memo child receiving stats, and stats isn't used as a hook dependency elsewhere. The useMemo adds overhead (cache lookup, dependency comparison) that exceeds the savings. The simpler and correct version: const total = data.length; return <p>{total} items</p>. Reserve useMemo for genuinely expensive computations or referential stability requirements."
---

# Hook

A senior engineer reviews your PR. You've added `useMemo` to every computation in the file. They ask you to remove most of them. "These make the code slower and harder to read," they say.

You added memoisation to help performance. It did the opposite. Understanding when — and when not — to memoise is the difference between an optimisation and a pessimisation.

# Lore Introduction

*"The sage who memorises every fact they encounter is not wiser than the sage who knows when to look things up,"* the Academy philosopher observes. *"Memory has cost. Recall time, storage space, the effort of recording. Memorise what is expensive to derive. Leave what is cheap to compute fresh."*

# Core Learning

## Concept Introduction

React has three memoisation APIs:

| API | What it memoises | Use when |
|---|---|---|
| `React.memo(Component)` | Component renders | Child renders expensively and receives stable props |
| `useMemo(fn, deps)` | Computed value | Computation is expensive OR result needs referential stability |
| `useCallback(fn, deps)` | Function reference | Function is passed to a memo component or used as a dep |

**The cost of each:**
- Dependency array comparison on every render (even cache hits)
- Memory for cached value
- Cognitive overhead — hidden state in the component

**When memoisation wins:** computation cost > (lookup overhead × render frequency)

For a 50ms filter operation called 60x/sec: saves ~2950ms/sec. For a 0.01ms string format: the cache lookup costs more than recomputation.

## Why It Matters

Premature memoisation is a performance anti-pattern. It adds overhead, obscures data flow, and makes components harder to refactor. Profiler-driven memoisation is an engineering discipline — measure, identify the bottleneck, apply the minimum fix.

## Worked Example

```tsx
// When React.memo + useCallback is correct:
const ExpensiveRow = React.memo(function Row({ data, onDelete }) {
  // expensive render — complex calculations inside
  return <div onClick={() => onDelete(data.id)}>{/* ... */}</div>;
});

function List({ items }) {
  // Without useCallback: new function reference every render → memo defeated
  const handleDelete = useCallback((id) => {
    setItems(prev => prev.filter(i => i.id !== id));
  }, []); // stable reference

  return items.map(item => (
    <ExpensiveRow key={item.id} data={item} onDelete={handleDelete} />
  ));
}
```

```tsx
// When useMemo is correct:
function FilteredList({ items, filter }) {
  // Filtering 10,000 items is expensive — worth memoising
  const filtered = useMemo(
    () => items.filter(i => i.status === filter),
    [items, filter]
  );
  return <List items={filtered} />;
}
```

```tsx
// When useMemo is wrong:
function Label({ count }) {
  // DON'T DO THIS — trivial computation
  const text = useMemo(() => `${count} items`, [count]);
  return <p>{text}</p>;
}
// DO THIS instead:
function Label({ count }) {
  return <p>{count} items</p>;
}
```

## Common Mistakes

- **Wrapping everything in useMemo/useCallback by default.** Adds overhead without benefit for cheap operations.
- **Forgetting that memo requires stable props.** React.memo + unstable props = memo never activates.
- **Missing dependencies.** Missing a dep in useMemo or useCallback causes stale closures — subtle bugs.
- **Memoising before measuring.** Profile first, optimise the confirmed bottleneck.

## Mental Model

Memoisation is a trade: you pay a small cost (cache lookup + dependency comparison) on every render, in exchange for skipping an expensive operation on cache hits. The trade is only profitable when the expensive operation genuinely exceeds the lookup cost, and when cache hits are frequent. Apply it like a business decision: calculate the ROI before investing.

## Mini Summary

- ✔ React.memo: skip component re-renders when props are shallowly equal
- ✔ useMemo: cache an expensive computed value
- ✔ useCallback: stabilise a function reference for use as a prop or dep
- ✔ Each has overhead — measure before applying
- ✔ Never memoise as a default — profile first, apply to confirmed bottlenecks

# Guided Practice Quest

Work through the guided steps to develop your judgment for when memoisation helps vs hinders.

# Solo Practice Quest

Given a `ProductSearch` component that: (1) filters 5,000 products by a search string, (2) passes an `onAddToCart` callback to each `ProductCard`, (3) `ProductCard` is wrapped with React.memo, design the memoisation strategy. What do you memoise, what do you leave unmemoised, and why? What would you check in the Profiler before and after?

# Integration

**Mathematics — Memoisation in Dynamic Programming**

Memoisation is one of the two core techniques in dynamic programming (alongside tabulation). In algorithms, it trades space for time: store the result of expensive function calls and return the cached result when the same inputs occur again. React's memoisation APIs apply this to components and hook computations. The tradeoff analysis is mathematically identical: time saved = (computation cost - lookup cost) × cache hit rate. A cache hit rate of 50% with a computation cost twice the lookup cost breaks even. Below break-even, memoisation is a pessimisation. This mathematical frame makes it clear why "memoise everything" is wrong — it's a bet that cache hit rates will be high enough to justify the overhead. Often they aren't.

# Lore Conclusion

*"The sage memoises the seven-step derivation, not the single addition,"* the philosopher concludes. *"Selective memory is wisdom. Indiscriminate memory is burden."*

---
