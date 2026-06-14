---
id: fe-jun-m6-05
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
lesson: custom_hooks
title: "Custom Hooks"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m6-04]
integrationDomains: [design, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what a custom hook is and the naming convention"
    - "Describes what it returns (state and/or handlers)"
    - "Gives a real example of logic that belongs in a custom hook"
  keywords: [hook, useState, useEffect, return, stateful, logic]
  modelAnswer: |
    A custom hook is a function whose name starts with 'use' and that can call other React hooks. It extracts stateful logic — state declarations, effects, and derived values — out of a component so the component only receives the values it needs to render. Examples include useFetch for data fetching, useForm for form state, or useLocalStorage for persisted state. The hook returns state values and any event handlers the component needs.
guidedSteps:
  - id: fe-jun-m6-05-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What is the naming rule for custom hooks and why does it exist?"
    inputConfig:
      options:
        - "Names must start with 'use' so React can enforce the rules of hooks"
        - "Names must start with 'use' so TypeScript can infer return types"
        - "Names can be anything as long as the file is in a hooks/ folder"
        - "Names must end in 'Hook' to distinguish them from components"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Names must start with 'use' so React can enforce the rules of hooks"]
      rejectedFeedback: "The 'use' prefix is a contract with React's linter and runtime. It signals that this function can call hooks and must follow the rules of hooks — enabling React to lint violations correctly."
    hint: "React's eslint-plugin-react-hooks checks the 'use' prefix to know where to apply rules-of-hooks enforcement."
    reflectionPrompt: "What would break if you named a hook getFormState instead of useFormState?"
  - id: fe-jun-m6-05-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Describe what a useFetch hook should return and why."
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [data, loading, error, return, state]
      rejectedFeedback: "A useFetch hook should return the data, a loading state, and an error state — the three things the component needs to render the right UI for each fetch outcome."
    hint: "Think about the three possible states of a network request from the component's perspective."
    reflectionPrompt: "If useFetch returns data, loading, and error — what does the component need to do with each?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of the following is the best candidate for extraction into a custom hook?"
    options:
      - "A function that formats a date string"
      - "A set of useState and useEffect calls that manage a modal's open/close state"
      - "A JSX fragment for a button"
      - "A TypeScript interface for a User type"
    correctIndex: 1
    feedback: "Stateful logic (useState + useEffect combinations) is the primary candidate for custom hooks. Pure functions go to utility files; JSX goes to components."
retrieval:
  recall: "What is the naming convention for custom hooks and what does it unlock?"
  explain: "What is the difference between extracting logic to a utility function versus a custom hook?"
  mistakeId:
    code: |
      const UserProfile = ({ userId }: { userId: string }) => {
        const [user, setUser] = useState(null);
        const [loading, setLoading] = useState(true);
        const [error, setError] = useState(null);

        useEffect(() => {
          fetch(`/api/users/${userId}`)
            .then(r => r.json())
            .then(setUser)
            .catch(setError)
            .finally(() => setLoading(false));
        }, [userId]);

        if (loading) return <Spinner />;
        if (error) return <ErrorMessage error={error} />;
        return <div>{user?.name}</div>;
      };
    answer: "The fetch logic (useState + useEffect) should be extracted to a useUser(userId) custom hook that returns { user, loading, error }. The component then calls const { user, loading, error } = useUser(userId) and focuses only on rendering."
---

# Hook

Your team has five components that all fetch data from an API. Each one has the same three useState declarations and the same useEffect pattern — copy-pasted five times. When a new caching requirement arrives, you have to find and update all five. One is missed. A bug ships.

Custom hooks exist to solve exactly this problem.

# Lore Introduction

The Academy's healers each carried their own bag of herbs, prepared the same infusions each morning, and kept individual remedies for the same ailments. An elder healer noticed the duplication and created the *Apothecary's Compact* — a shared store of prepared reagents that any healer could draw from. Now a new remedy improved once is improved for all.

Custom hooks are your Apothecary's Compact.

# Core Learning

## Concept Introduction

A **custom hook** is a JavaScript function whose name starts with `use` and which can call other React hooks (like `useState`, `useEffect`, `useCallback`). Custom hooks extract **stateful logic** — state declarations, side effects, and derived values — out of components so that the logic can be shared and tested in isolation.

The key distinction from utility functions:
- **Utility functions** are pure functions. They take inputs, return outputs, and have no state.
- **Custom hooks** can use React hooks internally, managing state and effects.

```tsx
// A custom hook — stateful, uses React hooks
function useCounter(initialValue = 0) {
  const [count, setCount] = useState(initialValue);
  const increment = () => setCount(c => c + 1);
  const decrement = () => setCount(c => c - 1);
  const reset = () => setCount(initialValue);
  return { count, increment, decrement, reset };
}

// Usage
const Counter = () => {
  const { count, increment, decrement, reset } = useCounter(10);
  return (
    <div>
      <p>{count}</p>
      <button onClick={decrement}>-</button>
      <button onClick={increment}>+</button>
      <button onClick={reset}>Reset</button>
    </div>
  );
};
```

## Why It Matters

Custom hooks let you:
1. **Share stateful logic** between components without changing the component hierarchy.
2. **Test logic independently** — a hook can be tested with `renderHook` from React Testing Library.
3. **Simplify components** — the component body becomes a clear declaration of what it renders, not how it manages state.

## Worked Example

A `useFetch` hook encapsulates the full data-fetching lifecycle:

```tsx
// hooks/useFetch.ts
interface FetchState<T> {
  data: T | null;
  loading: boolean;
  error: Error | null;
}

function useFetch<T>(url: string): FetchState<T> {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);

    fetch(url)
      .then(r => {
        if (!r.ok) throw new Error(`HTTP ${r.status}`);
        return r.json() as Promise<T>;
      })
      .then(json => { if (!cancelled) setData(json); })
      .catch(err => { if (!cancelled) setError(err); })
      .finally(() => { if (!cancelled) setLoading(false); });

    return () => { cancelled = true; };
  }, [url]);

  return { data, loading, error };
}
```

```tsx
// Any component can now use it in one line
const UserCard = ({ userId }: { userId: string }) => {
  const { data: user, loading, error } = useFetch<User>(`/api/users/${userId}`);

  if (loading) return <Spinner />;
  if (error) return <ErrorMessage message={error.message} />;
  return <div className="p-4">{user?.name}</div>;
};
```

Another example — `useForm`:

```tsx
function useForm<T extends Record<string, string>>(initialValues: T) {
  const [values, setValues] = useState(initialValues);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setValues(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const reset = () => setValues(initialValues);

  return { values, handleChange, reset };
}
```

## Common Mistakes

**Naming a hook without the `use` prefix.** `getFormState()` is just a function — React's rules-of-hooks linter will not protect it, and calling hooks inside it violates the rules of hooks without warning.

**Returning too much from a hook.** If a hook returns 10 values, it is doing too much. Split it into smaller hooks with focused responsibilities.

**Using a custom hook for pure logic.** If your "hook" does not call any React hooks, it should be a plain utility function in a `utils/` file, not a hook.

**Not handling cleanup in useEffect.** Async hooks that do not cancel in-flight requests on unmount cause memory leaks and state updates on unmounted components.

## Mini Summary

Custom hooks extract stateful logic (useState, useEffect, and related state) out of components using the `use` naming convention. They can be shared across components, tested independently, and keep component bodies focused on rendering. Common patterns include useFetch, useForm, useLocalStorage, and useDebounce.

# Guided Practice Quest

Work through the steps to identify when to use a custom hook versus a utility function and practise reading a hook's return signature.

# Solo Practice Quest

Design a `useLocalStorage` custom hook. Describe:

1. What parameters it accepts
2. What state it manages internally
3. What it returns
4. One edge case you would need to handle (e.g., JSON parsing errors)

Write in 4–6 sentences, including a function signature.

# Integration

**Psychology — Working Memory:** A component full of useState declarations and useEffect calls taxes a developer's working memory — they must hold the full state management model in mind while also understanding the rendering logic. Custom hooks compress that cognitive load into a named abstraction (`useUserProfile`), freeing working memory for the rendering logic.

**Design — Progressive Disclosure:** Custom hooks embody progressive disclosure: the component exposes a simple, clean interface (`const { user, loading, error } = useUser(id)`), while the complexity of state management is disclosed only to those who open the hook file. This principle — simple surface, accessible depth — is a cornerstone of good interface design.

# Lore Conclusion

The Apothecary's Compact transformed the healers' guild. Instead of each healer reinventing remedies, they contributed to and drew from a shared store of tested, reliable preparations. Your custom hooks are that shared store. Build them with care, name them clearly, and your entire team benefits every time they use them.

---
