---
id: fe-jun-m6-08
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m6
moduleTitle: "Module 6: Component Design"
moduleGlyph: "🧩"
moduleSortOrder: 6
topicSlug: presentational_vs_container
topicTitle: "Presentational vs Container"
topicSortOrder: 3
lesson: pure_components
title: "Pure Components"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m6-07]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what makes a component 'pure' (same props = same output)"
    - "Describes what referential equality means in the context of object/array props"
    - "Explains at least one benefit of pure components for testing or reasoning"
  keywords: [pure, same, props, output, deterministic, referential, equality]
  modelAnswer: |
    A pure component always produces the same output for the same props — it has no side effects and does not depend on anything outside its props. This makes it deterministic and easy to reason about in isolation. For object and array props, referential equality matters: two objects with identical values are not the same reference, so a component should not rely on object identity. Pure components are easy to test (just pass props and assert output) and easy to reason about (no hidden state, no effects).
guidedSteps:
  - id: fe-jun-m6-08-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Which of the following is a pure component?"
    inputConfig:
      options:
        - "A component that calls fetch() during render"
        - "A component that reads from localStorage inside the render function"
        - "A component that renders a list from a prop and has no side effects"
        - "A component that uses Math.random() to generate a key"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A component that renders a list from a prop and has no side effects"]
      rejectedFeedback: "A pure component depends only on its props. Network calls, localStorage reads, and Math.random() are side effects that break purity."
    hint: "Purity means: given these exact props, I always return this exact output. Nothing else influences the result."
    reflectionPrompt: "Why does a component that calls Math.random() fail the same-props-same-output test?"
  - id: fe-jun-m6-08-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Explain why passing a new object literal as a prop on every render can cause unnecessary re-renders, even if the object's values haven't changed."
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [reference, new, same, equal, object, identity, memo]
      rejectedFeedback: "Each new object literal creates a new reference in memory. Even if the values are the same, {a: 1} !== {a: 1} by reference equality — so a memoised component sees a 'new' prop and re-renders."
    hint: "JavaScript compares objects by reference, not by value. Two objects with the same keys and values are still different objects."
    reflectionPrompt: "How would you prevent this problem when passing an object as a prop?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A component receives the same props as last render but still re-renders. What is the most likely cause?"
    options:
      - "The component uses useState internally"
      - "A parent is creating a new object or function reference on every render and passing it as a prop"
      - "The component is inside a React.StrictMode boundary"
      - "The component's CSS changed"
    correctIndex: 1
    feedback: "New object or function references cause re-renders even when the logical values are unchanged. useMemo and useCallback on the parent side preserve referential stability."
retrieval:
  recall: "What does it mean for a component to be 'pure'?"
  explain: "Why does referential equality matter when passing objects or arrays as props?"
  mistakeId:
    code: |
      const Dashboard = ({ userId }) => {
        return (
          <UserCard
            user={{ id: userId, role: 'admin' }}
            style={{ padding: 16 }}
          />
        );
      };
    answer: "Both user and style are new object literals created on every render. UserCard will re-render on every Dashboard render even if userId hasn't changed. Fix: memoize the objects with useMemo or lift them outside the component if they are static."
---

# Hook

You add `React.memo` to a slow list component to prevent unnecessary re-renders. Performance profiling shows it is still re-rendering on every parent update. The problem: the parent creates a new `config` object literal on every render and passes it as a prop. The component sees a "new" prop even though the data is identical. The memo achieves nothing.

Understanding purity — and referential equality — is what separates a React developer from a React expert.

# Lore Introduction

The Academy's oracle stones are the most trusted divination tools in the realm. Their power comes from one property: given the same question inscribed in the same runes, they always return the same answer. No external influence, no hidden state, no randomness. Artificers who understand this trust them absolutely. Artificers who add hidden inputs to oracle stones ruin their reliability forever.

Your pure components are oracle stones.

# Core Learning

## Concept Introduction

A **pure component** is one that behaves like a pure function: **given the same props, it always returns the same rendered output.** It has no side effects (no fetches, no localStorage writes, no global mutations) and depends on nothing except its props.

This is the formal definition of purity in functional programming applied to React components.

```tsx
// Pure — same props always produces same output
const Greeting = ({ name, role }: { name: string; role: string }) => (
  <div className="p-4 border rounded">
    <h2 className="font-bold">{name}</h2>
    <p className="text-sm text-gray-500">{role}</p>
  </div>
);

// Not pure — output depends on something external
const Greeting = ({ name }: { name: string }) => (
  <div>
    <h2>{name}</h2>
    <p>Logged in at: {new Date().toTimeString()}</p> {/* different on every render */}
  </div>
);
```

## Why It Matters

Pure components are valuable for three reasons:

**Predictability:** You can reason about the component in complete isolation. No surprising behaviour based on when it renders or what else is happening in the app.

**Testability:** Testing a pure component requires only rendering it with props and asserting the output. No mocks, no async, no setup.

**Optimisability:** React's `React.memo` wraps a component and skips re-rendering if the props haven't changed. This only works correctly if the component is pure — if it has side effects, skipping renders would cause bugs.

## Referential Equality

Pure components interact subtly with JavaScript's object equality. When React checks whether props changed, it uses **referential equality** (`===`). For primitive values (strings, numbers, booleans), this works intuitively: `'hello' === 'hello'` is `true`.

For objects and arrays, it compares references, not values:

```ts
const a = { name: 'Alice' };
const b = { name: 'Alice' };

a === b // false — different references, even though same values
a === a // true — same reference
```

This means a parent that creates a new object literal on every render will cause a child (even a `React.memo` child) to re-render every time:

```tsx
// Problem: new object every render
const Parent = () => (
  <PureChild config={{ theme: 'dark', size: 'lg' }} /> // new reference each render
);

// Solution: stable reference via useMemo or defined outside component
const CONFIG = { theme: 'dark', size: 'lg' };
const Parent = () => (
  <PureChild config={CONFIG} /> // same reference every render
);

// Or, for dynamic values:
const Parent = ({ theme, size }) => {
  const config = useMemo(() => ({ theme, size }), [theme, size]);
  return <PureChild config={config} />;
};
```

## Common Mistakes

**Using `React.memo` without ensuring the component is pure.** If a component has side effects, memoisation can suppress re-renders that are needed, leading to stale output.

**Not memoising callback props.** Functions are objects — `onClick={() => doSomething()}` creates a new function reference on every render. Use `useCallback` to stabilise callback props passed to memoised children.

**Over-memoising.** Not every component needs `React.memo`. The cost of the comparison can exceed the cost of a re-render for simple components. Profile first.

## Mini Summary

A pure component produces the same output for the same props and has no side effects. Purity enables predictability, easy testing, and safe memoisation. Object and array props require attention to referential equality — new object literals on every render break `React.memo` optimisations even when the data is unchanged.

# Guided Practice Quest

Work through the steps to identify pure vs impure components and understand how referential equality affects re-rendering.

# Solo Practice Quest

Consider a `ThemeProvider` that passes a `theme` object down through context, where the theme object is constructed inline:

```tsx
<ThemeContext.Provider value={{ primary: '#3B82F6', secondary: '#6366F1' }}>
```

Describe:
1. Why this causes unnecessary re-renders in all consumers
2. How you would fix it
3. What the fix achieves in terms of purity and performance

Write 3–5 sentences.

# Integration

**Mathematics — Pure Functions:** In mathematics, a function is pure (or "total") if it maps each input to exactly one output with no side effects. `f(x) = x²` is pure; `f(x) = x + random()` is not. React components are functions — applying mathematical purity to them makes their behaviour provably consistent.

**Psychology — Mental Models:** When developers build a mental model of an application, pure components are the easiest to model: they are black boxes with a defined input-output contract. Impure components require tracking external state, timing, and side effects in addition to the component logic — dramatically increasing the cognitive complexity of understanding the system.

# Lore Conclusion

An oracle stone that always answers the same question the same way is a stone that can be trusted. Build your components to be oracle stones: defined by their inputs, deterministic in their outputs, free of hidden influences. A codebase full of pure, predictable components is one that every artificer in the Academy can reason about with confidence.

---
