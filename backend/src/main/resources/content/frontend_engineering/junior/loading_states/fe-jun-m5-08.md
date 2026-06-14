---
id: fe-jun-m5-08
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m5
moduleTitle: "Module 5: API Integration"
moduleGlyph: "🌐"
moduleSortOrder: 5
topicSlug: loading_states
topicTitle: "Loading States"
topicSortOrder: 3
lesson: useeffect_for_fetching
title: "useEffect for Data Fetching"
sortOrder: 2
difficulty: 4
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-07]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains why you cannot call async functions directly inside useEffect"
    - "Describes the purpose of the dependency array in useEffect"
    - "Explains why cleanup matters for fetch operations"
    - "Demonstrates the correct pattern: define async function inside, call it"
  keywords: [useEffect, async, dependency, cleanup, mount, fetch, array, effect]
  modelAnswer: |
    useEffect runs side effects after render. You cannot make useEffect's callback itself async because React expects it to return either nothing or a cleanup function, not a Promise. Instead, define an async function inside useEffect and call it immediately. The dependency array controls when the effect re-runs: an empty array [] means only on mount, an array with values like [userId] means when those values change. Cleanup prevents setting state on unmounted components — use an 'active' flag or AbortController to cancel in-flight requests when the component unmounts.
guidedSteps:
  - id: fe-jun-m5-08-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Why can't you write `useEffect(async () => { ... }, [])`?"
    inputConfig:
      options:
        - "useEffect doesn't support arrow functions"
        - "React expects useEffect's callback to return a cleanup function or undefined, not a Promise"
        - "async functions can't access component state"
        - "The empty dependency array prevents async execution"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["React expects useEffect's callback to return a cleanup function or undefined, not a Promise"]
      rejectedFeedback: "An async function always returns a Promise. useEffect interprets the return value as a cleanup function — receiving a Promise instead causes React to misbehave or warn."
    hint: "What does async always return? What does useEffect expect to receive as the return value?"
    reflectionPrompt: "If the callback returns a Promise, what does React try to do with it? What should it return instead?"
  - id: fe-jun-m5-08-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "Your component fetches user data when `userId` changes. What should the dependency array be?"
    inputConfig:
      options:
        - "[] — only fetch once on mount"
        - "[userId] — re-fetch whenever userId changes"
        - "[data] — re-fetch whenever data changes"
        - "No array — re-fetch on every render"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["[userId] — re-fetch whenever userId changes"]
      rejectedFeedback: "When the data you need depends on a prop or state value, include it in the dependency array. [userId] means: run this effect on mount AND whenever userId changes."
    hint: "The effect should re-run whenever the thing it depends on changes."
    reflectionPrompt: "What would happen if you used [] instead of [userId] — would the displayed user ever update if userId changed?"
  - id: fe-jun-m5-08-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: "Explain what problem the cleanup function in useEffect solves for fetch requests, and describe one way to implement it."
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [unmount, cancel, abort, stale, state, cleanup]
      rejectedFeedback: "Without cleanup, a slow fetch can complete after the component unmounts and try to call setState — causing a React warning (or bug). Use AbortController or an 'active' flag to cancel or ignore stale responses."
    hint: "What if the component unmounts before the fetch completes? What happens when the fetch then tries to update state?"
    reflectionPrompt: "In strict mode, React intentionally unmounts and remounts components — why does that make cleanup even more important?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does a useEffect with `[]` as the dependency array do?"
    options:
      - "Runs the effect every time the component renders"
      - "Runs the effect only on the first render (mount)"
      - "Disables the effect entirely"
      - "Runs the effect every second"
    correctIndex: 1
    feedback: "An empty dependency array [] means the effect runs once after the initial render — equivalent to componentDidMount. It won't re-run unless the component unmounts and remounts."
retrieval:
  recall: "What are the three things useEffect's dependency array controls: what happens with [], [value], and no array?"
  explain: "Explain the correct pattern for using async/await inside useEffect — why you can't make the callback async and what you do instead."
  mistakeId:
    code: |
      useEffect(async () => {
        const res = await fetch(`/api/users/${userId}`);
        const data = await res.json();
        setUser(data);
      }, [userId]);
    answer: "useEffect's callback must not be async — it returns a Promise instead of a cleanup function, causing React warnings. Fix: define an async function inside the callback and call it. useEffect(() => { async function load() { ... } load(); }, [userId]);"
---

# Hook

You've got your three states set up. But when do you actually run the fetch? You try calling it at the top of the component — it runs on every render, causing an infinite loop. You try calling it in an event handler — it only runs when the user clicks something. You need the data to load when the component mounts. That's what `useEffect` is for: running side effects — like data fetching — at the right moment in the component lifecycle.

# Lore Introduction

*A spell that fires the moment you think of it is dangerous — the Academy learned this lesson early, when eager apprentices cast incantations mid-sentence and interrupted half the lecture hall. Certain actions must be bound to the right moment: when you enter the chamber, not before. Not repeatedly on every breath. And when you leave, the spell must be undone, lest it linger in the empty room and cause mischief. `useEffect` is the Academy's timing rune: it binds your action to the right moment, and ensures proper cleanup when you depart.*

# Core Learning

## Concept Introduction

`useEffect` is React's hook for running **side effects** — operations that interact with the outside world: fetching data, setting up subscriptions, reading from DOM, etc.

It takes two arguments:
1. A **callback function** — the side effect to run
2. A **dependency array** — controls when the effect re-runs

```js
useEffect(() => {
  // This runs after render
}, [/* dependencies */]);
```

Dependency array behaviour:
- `[]` — runs once after the first render (on mount)
- `[value]` — runs on mount AND whenever `value` changes
- No array — runs after every render (usually wrong for fetching)

**Critical rule**: The callback cannot be `async`. React expects it to return either `undefined` or a cleanup function. An async function returns a Promise, which React misinterprets.

## Why It Matters

Most data fetching in React components is triggered by `useEffect` on mount or when a key dependency (like a user ID from props) changes. Understanding this hook — and its dependency array and cleanup mechanism — prevents infinite re-render loops, stale data, and memory leak warnings.

## Worked Example

```jsx
function UserProfile({ userId }) {
  const [isLoading, setIsLoading] = React.useState(true);
  const [user, setUser] = React.useState(null);
  const [error, setError] = React.useState(null);

  useEffect(() => {
    // Define async function INSIDE, don't make the callback async
    async function fetchUser() {
      setIsLoading(true);
      setError(null);

      try {
        const res = await fetch(`/api/users/${userId}`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        setUser(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setIsLoading(false);
      }
    }

    fetchUser(); // call it immediately

    // Cleanup with AbortController
    // This runs if userId changes before the fetch completes,
    // or if the component unmounts
  }, [userId]); // re-fetch when userId changes

  if (isLoading) return <div className="p-4 animate-pulse">Loading...</div>;
  if (error) return <div className="p-4 text-red-600">{error}</div>;

  return (
    <div className="p-4 bg-white rounded shadow">
      <h2 className="text-lg font-semibold">{user.name}</h2>
      <p className="text-gray-500">{user.email}</p>
    </div>
  );
}
```

With proper AbortController cleanup:
```jsx
useEffect(() => {
  const controller = new AbortController();

  async function fetchUser() {
    try {
      const res = await fetch(`/api/users/${userId}`, {
        signal: controller.signal
      });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const data = await res.json();
      setUser(data);
    } catch (err) {
      if (err.name === 'AbortError') return; // intentional cancel — ignore
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  }

  fetchUser();

  return () => controller.abort(); // cleanup: cancel if userId changes or unmounts
}, [userId]);
```

## Common Mistakes

**Mistake 1: Making the effect callback async**
```js
// WRONG
useEffect(async () => { await fetch(...); }, []);

// CORRECT
useEffect(() => {
  async function load() { await fetch(...); }
  load();
}, []);
```

**Mistake 2: Missing the dependency array (infinite loop)**
```js
useEffect(() => {
  fetch('/api/data').then(r => r.json()).then(setData);
}); // no array — runs after EVERY render, including renders caused by setData!
```

**Mistake 3: Setting state after unmount**
```js
// Without cleanup, if the component unmounts, the setState still fires
// React 18 handles this more gracefully, but it's still a code smell
```

## Mini Summary

- Use `useEffect` to trigger fetches at the right time: on mount (`[]`) or when values change (`[dep]`)
- Never make the `useEffect` callback `async` — define the async function inside and call it
- Use `finally` to always clear the loading state
- Use `AbortController` for cleanup — abort the request if the component unmounts or deps change
- Include all values in the dependency array that the effect uses from the component scope

# Guided Practice Quest

Work through the steps to confirm your understanding of the dependency array, the async-inside-useEffect pattern, and the importance of cleanup.

# Solo Practice Quest

Explain the correct pattern for fetching data inside a React component using useEffect. Cover: why the callback can't be async, what the dependency array does, and why cleanup matters. Include a code example showing the complete pattern with isLoading, data, and error state.

# Integration

**Mathematics — Trigger Conditions as Predicates:** The dependency array is a set of predicates that determine when the effect executes. In formal logic, a predicate is a function that maps to true/false. The effect re-runs when any dependency value changes — this is equivalent to: `run_effect if any(new_value != old_value for value in deps)`. Understanding the dependency array as a mathematical trigger condition helps reason about edge cases: what if deps never change? What if they change every render?

**Philosophy — Timing and Causation:** Aristotle's four causes include the *efficient cause* — the trigger that brings something into being. useEffect's dependency array specifies the efficient cause of the side effect: *what* causes it to run. This is a profound design pattern: separating the *what* (the effect body) from the *when* (the dependency array). Good software engineering, like good philosophical analysis, often comes down to clarity about cause and effect.

# Lore Conclusion

*The timing rune is mastered. Your spell fires when you enter the chamber — not before, not after every breath, not repeatedly. And when you leave, the room is as you found it: the fetch cancelled, the state untouched. The Academy's halls are quiet and ordered. No lingering incantations, no ghost responses haunting components long since departed. This is the discipline of effects: know when to cast, and know how to undo.*

---
