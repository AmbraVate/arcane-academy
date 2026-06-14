---
id: fe-jun-m5-07
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
lesson: the_three_states
title: "The Three States of Data Fetching"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-06]
integrationDomains: [design, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Names and describes all three data fetching states"
    - "Explains what happens in a component that doesn't handle loading state"
    - "Explains what happens in a component that doesn't handle error state"
    - "Shows how to model all three states with useState"
  keywords: [loading, success, error, state, useState, handle, render, conditional]
  modelAnswer: |
    The three states of data fetching are: loading (the request is in flight), success (data arrived), and error (something went wrong). Every fetch should handle all three. Without loading state, the component renders empty content before data arrives, causing layout shifts or crashes. Without error state, users see nothing when a request fails. Model them with separate useState variables: isLoading, data, and error. Render different UI for each: a spinner/skeleton during loading, the actual content on success, and an error message on failure.
guidedSteps:
  - id: fe-jun-m5-07-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A component starts fetching data. What three state variables should you initialise to model all possible outcomes?"
    inputConfig:
      options:
        - "data, fetchError, isDone"
        - "isLoading (true), data (null), error (null)"
        - "status ('idle'), response (null)"
        - "fetching (false), result (undefined), failed (false)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["isLoading (true), data (null), error (null)"]
      rejectedFeedback: "A clean, conventional pattern: isLoading starts true (request begins immediately), data starts null, error starts null. Each updates as the fetch progresses."
    hint: "Think about the three outcomes: still waiting, got data, got an error."
    reflectionPrompt: "Why should isLoading start as true rather than false if you fetch immediately on mount?"
  - id: fe-jun-m5-07-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "What happens if a component tries to render `data.items.map(...)` when data is still null (loading state not yet handled)?"
    inputConfig:
      options:
        - "It renders an empty list"
        - "It throws a TypeError: cannot read properties of null"
        - "It automatically waits until data is available"
        - "It renders undefined silently"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It throws a TypeError: cannot read properties of null"]
      rejectedFeedback: "Accessing .items on null throws a TypeError. The component crashes. This is why you must check isLoading (and that data is not null) before trying to render data."
    hint: "JavaScript doesn't wait for data — it tries to read null.items immediately."
    reflectionPrompt: "What would a user see if your component crashes during initial load?"
  - id: fe-jun-m5-07-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: "Describe the UI you should show for each of the three states: loading, error, and success. Why is it important to never leave any state unhandled?"
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [loading, error, success, spinner, message, data]
      rejectedFeedback: "Loading: show a spinner, skeleton, or 'loading...' message. Error: show a friendly error message with possibly a retry option. Success: show the actual data. Unhandled states crash or confuse users."
    hint: "Think about what the user sees in each case — never leave them with a blank or broken page."
    reflectionPrompt: "What is the UX impact of a page that silently fails vs one that shows an error message?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "After a fetch succeeds, which state variables should be updated?"
    options:
      - "Only data — leave isLoading as true"
      - "data (set to result) and isLoading (set to false)"
      - "Only isLoading — set to false"
      - "error — set to null to confirm there was no error"
    correctIndex: 1
    feedback: "On success: set data to the fetched result, and set isLoading to false. The error state stays null. On failure: set error to the error, set isLoading to false, leave data as null."
retrieval:
  recall: "What are the three states of data fetching, and what value does each state variable hold in each state?"
  explain: "Why is it important to handle all three states (loading, success, error) in a component? What goes wrong if you only handle success?"
  mistakeId:
    code: |
      function UserList() {
        const [users, setUsers] = React.useState([]);
        React.useEffect(() => {
          fetch('/api/users').then(r => r.json()).then(setUsers);
        }, []);
        return <ul>{users.map(u => <li key={u.id}>{u.name}</li>)}</ul>;
      }
    answer: "This component handles only the success state. During loading, it renders an empty list (confusing). On error, it silently shows nothing. The correct pattern uses isLoading and error state variables to handle all three cases with appropriate UI."
---

# Hook

You ship a product page. On a slow connection, it shows nothing for three seconds, then suddenly content appears. On a failed request, it stays blank forever with no explanation. Your user thinks the site is broken. They leave. Meanwhile, a competitor's site shows a loading skeleton, then a helpful "Couldn't load products — try again" message. The difference isn't a fancy library. It's three pieces of state: loading, success, and error — and the discipline to handle all three.

# Lore Introduction

*Every quest in the Academy has three possible present states: the emissary is still on their journey, the emissary has returned with answers, or the emissary encountered misfortune. A wise Master plans for all three — they do not simply wait expectantly with a blank expression, assuming success. They set a lantern in the window for the returning emissary, post a notice for waiting students, and have a contingency scroll prepared for failure. The Academy never shows its students an empty hall and calls it a loading screen.*

# Core Learning

## Concept Introduction

Every data fetch operation can be in one of three states:

1. **Loading** — the request is in flight; no data yet
2. **Success** — the request completed; data is available
3. **Error** — the request failed; data is unavailable

All three must be handled. Skipping any one creates a broken user experience:
- Skipping **loading**: component renders with null/empty data, causing crashes or empty screens
- Skipping **error**: users see nothing when requests fail — silent failures
- Skipping **success**: pointless, but also possible to "forget" to render the data

Model these states in React with three `useState` variables:
```js
const [isLoading, setIsLoading] = React.useState(true); // starts loading
const [data, setData] = React.useState(null);
const [error, setError] = React.useState(null);
```

## Why It Matters

Handling all three states is the difference between a professional application and a broken one. Users encountering loading states (loading spinners, skeleton screens) understand they should wait. Users encountering error states (with helpful messages) understand something went wrong and what to do next. Users encountering neither are confused and leave.

## Worked Example

```jsx
function ProductList() {
  const [isLoading, setIsLoading] = React.useState(true);
  const [products, setProducts] = React.useState(null);
  const [error, setError] = React.useState(null);

  React.useEffect(() => {
    async function fetchProducts() {
      try {
        const res = await fetch('/api/products');
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        setProducts(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setIsLoading(false); // always stop loading, success or failure
      }
    }
    fetchProducts();
  }, []);

  // Handle each state explicitly
  if (isLoading) {
    return (
      <div className="flex justify-center p-8">
        <div className="text-gray-500">Loading products...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-4 bg-red-50 border border-red-200 rounded text-red-700">
        Failed to load products: {error}
      </div>
    );
  }

  return (
    <ul className="grid grid-cols-3 gap-4 p-4">
      {products.map(product => (
        <li key={product.id} className="bg-white rounded shadow p-4">
          <h3 className="font-semibold">{product.name}</h3>
          <p className="text-gray-600">${product.price}</p>
        </li>
      ))}
    </ul>
  );
}
```

Note the `finally` block: `setIsLoading(false)` runs whether the fetch succeeded or failed — both outcomes should stop the loading indicator.

## Common Mistakes

**Mistake 1: Using `finally` but forgetting to set error state**
```js
// INCOMPLETE — if fetch fails, error is never set, user sees blank
try {
  const data = await fetch('/api').then(r => r.json());
  setData(data);
} finally {
  setIsLoading(false);
}
```

**Mistake 2: Setting isLoading only on success**
```js
fetch('/api/data')
  .then(r => r.json())
  .then(data => {
    setData(data);
    setIsLoading(false); // never called if fetch fails!
  });
```

**Mistake 3: Rendering data without null-checking**
```js
// If data is null during loading, this throws
return <div>{data.name}</div>;

// Safe
return <div>{data?.name}</div>;
// Or better: check isLoading first, so by the time you render, data is guaranteed
```

## Mini Summary

- Three states: **loading**, **success**, **error** — always handle all three
- Model with `isLoading`, `data`, and `error` useState variables
- Use `finally` to always stop the loading indicator regardless of outcome
- Render different UI for each state: spinner, actual content, error message
- Never render data without confirming it's not null

# Guided Practice Quest

Work through the guided steps to model all three fetch states correctly and understand what breaks when any one is missing.

# Solo Practice Quest

Explain why all three data fetching states must be handled in a component. Describe what can go wrong when loading or error states are skipped. Show a React component with state variables for all three states and explain what happens in each branch of the conditional render.

# Integration

**Design — Feedback and System Status:** Nielsen's first usability heuristic is "Visibility of System Status" — users should always know what's happening. A loading state is an implementation of this heuristic. An error state is an implementation of heuristic 9: "Help users recognise, diagnose, and recover from errors." Ignoring these states isn't just technically incorrect — it violates established UX principles that have been validated by decades of usability research.

**Psychology — Uncertainty and Anxiety:** Research by Maister (1985) on waiting psychology found that uncertain waits feel longer than known-length waits. A blank screen creates maximum uncertainty — users don't know if the page is loading, broken, or empty. A loading indicator converts uncertain waiting to defined waiting, reducing perceived duration. The same content loading behind a spinner feels faster than the same content loading behind a blank screen, because the user's anxiety is reduced.

# Lore Conclusion

*Three lanterns hang in the Academy's waiting hall. The amber lantern burns while the quest is in progress: "Your emissary travels." The green lantern signals success: "Here is what you sought." The red lantern, rarely lit, signals misfortune: "Something went wrong — here is what to do next." The hall is never dark, never silent. Students always know where they stand. That is the discipline of the three states.*

---
