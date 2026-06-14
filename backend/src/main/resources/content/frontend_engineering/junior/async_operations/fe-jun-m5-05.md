---
id: fe-jun-m5-05
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m5
moduleTitle: "Module 5: API Integration"
moduleGlyph: "🌐"
moduleSortOrder: 5
topicSlug: async_operations
topicTitle: "Async Operations"
topicSortOrder: 2
lesson: async_await
title: "Async/Await"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-04]
integrationDomains: [design, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains that async/await is syntax sugar over Promises"
    - "Describes what the async keyword does to a function"
    - "Explains how await pauses execution within the function"
    - "Shows correct try/catch usage with async/await"
  keywords: [async, await, Promise, try, catch, syntax, function, pause]
  modelAnswer: |
    async/await is syntax sugar that makes Promise-based code look and read like synchronous code. Marking a function with async makes it return a Promise automatically. Inside an async function, you can use await before any expression that returns a Promise — this pauses execution of that function (but not the rest of the program) until the Promise resolves, then returns the resolved value. Errors are caught with try/catch instead of .catch(), which most developers find more readable and familiar.
guidedSteps:
  - id: fe-jun-m5-05-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What does the `async` keyword do when placed before a function declaration?"
    inputConfig:
      options:
        - "It makes the function run faster"
        - "It makes the function return a Promise automatically"
        - "It allows the function to use console.log"
        - "It prevents the function from throwing errors"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It makes the function return a Promise automatically"]
      rejectedFeedback: "async makes any function return a Promise. Even if you return a plain value like 42, it gets wrapped: the function returns Promise.resolve(42)."
    hint: "Remember: async/await is built on top of Promises — async functions always return Promises."
    reflectionPrompt: "If async functions always return Promises, how would you await the result of an async function call?"
  - id: fe-jun-m5-05-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "When you `await` a Promise inside an async function, what happens to the rest of the application?"
    inputConfig:
      options:
        - "The entire JavaScript runtime pauses until the Promise resolves"
        - "Only the current function pauses; other code continues running"
        - "The browser tab freezes temporarily"
        - "All other Promises are cancelled"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Only the current function pauses; other code continues running"]
      rejectedFeedback: "await pauses only the current async function — not the whole program. The event loop keeps running; other callbacks and events continue."
    hint: "JavaScript is single-threaded but non-blocking. await yields the thread back to the event loop."
    reflectionPrompt: "How is this similar to how the .then() chain works — where does control go while waiting?"
  - id: fe-jun-m5-05-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: "Rewrite this Promise chain using async/await with proper try/catch: `fetch('/api/user').then(r => r.json()).then(u => console.log(u.name)).catch(err => console.error(err))`"
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: [async, await, try, catch]
      rejectedFeedback: "Use: async function getUser() { try { const r = await fetch('/api/user'); const u = await r.json(); console.log(u.name); } catch(err) { console.error(err); } }"
    hint: "Replace .then() with await, and .catch() with a try/catch block around the whole thing."
    reflectionPrompt: "Both approaches do the same thing. When might you prefer .then() chaining over async/await?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of these correctly handles errors in an async/await fetch function?"
    options:
      - "async function load() { const data = await fetch('/api').json(); }"
      - "async function load() { try { const res = await fetch('/api'); const data = await res.json(); } catch(e) { console.error(e); } }"
      - "async function load() { const data = fetch('/api').catch(console.error); }"
      - "function load() { await fetch('/api'); }"
    correctIndex: 1
    feedback: "The second option is correct: async function, two awaits (one for fetch, one for .json()), wrapped in try/catch for error handling."
retrieval:
  recall: "What two keywords define the async/await pattern, and where does each one go?"
  explain: "Explain why you need two await statements when using async/await with fetch — one for fetch itself and one for .json()."
  mistakeId:
    code: |
      async function getUser() {
        const res = await fetch('/api/user');
        const data = res.json(); // missing await
        console.log(data.name);
      }
    answer: "res.json() returns a Promise. Without await, data is a Promise object, not the parsed JSON. It should be: const data = await res.json();"
---

# Hook

You've written a Promise chain with five `.then()` calls. You need to add a condition halfway through. The indentation is off, the error handling is unclear, and your code reviewer has left three comments asking "what does this do?". Then a colleague shows you the same logic written with async/await in half the lines. It reads like a recipe: step 1, step 2, step 3. Async/await doesn't change what happens — it changes how clearly you can express it.

# Lore Introduction

*The senior enchanters of the Academy discovered something remarkable: the same incantations could be written in the ancient form — long, winding, nested — or in the modern tongue, which reads like plain instructions. "Dispatch the messenger. Wait for their return. Read the scroll. Record the findings." The outcome is identical. But the modern form is readable to any apprentice, not just masters of arcane syntax. Async/await is that modern tongue: clarity without sacrifice.*

# Core Learning

## Concept Introduction

`async/await` is **syntactic sugar** over Promises — it doesn't introduce new capabilities, it makes Promise-based code look synchronous. Under the hood, it compiles to the same Promise chains you've already learned.

Two keywords:

- **`async`** — placed before a function declaration; makes the function return a Promise automatically
- **`await`** — placed before any expression that returns a Promise; pauses the current function until the Promise resolves, then returns the resolved value

The critical nuance: `await` pauses only the **current async function** — not the entire JavaScript runtime. The event loop keeps running, other code keeps executing.

## Why It Matters

Async/await is the dominant pattern in modern React codebases. Event handlers, custom hooks, and server actions all use it. It makes code easier to read, easier to debug (stack traces are cleaner), and easier to write conditionals and loops with async operations.

## Worked Example

```js
// Using .then() chains — harder to follow
function fetchUserThen(id) {
  return fetch(`/api/users/${id}`)
    .then(response => {
      if (!response.ok) throw new Error(response.status);
      return response.json();
    })
    .then(user => {
      return fetch(`/api/teams/${user.teamId}`);
    })
    .then(response => response.json())
    .then(team => {
      console.log(`${user.name} is in ${team.name}`); // user is out of scope!
    })
    .catch(err => console.error(err));
}

// Using async/await — reads linearly, variables stay in scope
async function fetchUser(id) {
  try {
    const response = await fetch(`/api/users/${id}`);
    if (!response.ok) throw new Error(`HTTP ${response.status}`);

    const user = await response.json();
    const teamResponse = await fetch(`/api/teams/${user.teamId}`);
    const team = await teamResponse.json();

    console.log(`${user.name} is in ${team.name}`); // both in scope!
  } catch (error) {
    console.error('Failed to load user:', error.message);
  }
}
```

In a React component:
```jsx
function UserProfile({ userId }) {
  const [user, setUser] = React.useState(null);

  async function loadUser() {
    try {
      const res = await fetch(`/api/users/${userId}`);
      if (!res.ok) throw new Error('Failed to load');
      const data = await res.json();
      setUser(data);
    } catch (err) {
      console.error(err);
    }
  }

  return (
    <div className="p-4">
      <button
        onClick={loadUser}
        className="bg-indigo-600 text-white px-4 py-2 rounded"
      >
        Load User
      </button>
      {user && <p className="mt-2 text-gray-700">{user.name}</p>}
    </div>
  );
}
```

## Common Mistakes

**Mistake 1: Using await outside an async function**
```js
// WRONG — SyntaxError
function load() {
  const data = await fetch('/api'); // can't use await here!
}

// CORRECT
async function load() {
  const data = await fetch('/api');
}
```

**Mistake 2: Forgetting the second await for .json()**
```js
// WRONG — data is a Promise
const data = (await fetch('/api')).json(); // calling .json() without awaiting it

// CORRECT
const response = await fetch('/api');
const data = await response.json();
```

**Mistake 3: No try/catch — swallowing errors**
```js
// RISKY — async errors are silently ignored
async function load() {
  const res = await fetch('/api');
  const data = await res.json();
  setData(data);
  // If any of these fail, you'll never know
}
```

## Mini Summary

- `async` before a function makes it return a Promise
- `await` pauses the current function until a Promise resolves; only that function pauses
- Use `try/catch` instead of `.catch()` for error handling in async/await code
- Two `await` calls needed: one for `fetch()`, one for `response.json()`
- It's syntactic sugar — the underlying Promise mechanism is the same

# Guided Practice Quest

Work through the steps above to practise converting .then() chains to async/await and understanding what each keyword does.

# Solo Practice Quest

Explain the async/await pattern to a developer who understands Promises but hasn't used it yet. Cover: what async does to a function, what await does to execution, how try/catch replaces .catch(), and at least one scenario where async/await is clearly more readable than a .then() chain.

# Integration

**Design — Readability as Interface:** Code is an interface — it is read by humans as well as machines. Async/await is a design decision that prioritises human readability. The same principle governs UI design: progressive disclosure, clear hierarchy, familiar patterns reduce cognitive load. When code reads top-to-bottom like a procedural recipe, the reader's mental model matches the execution flow. This alignment between mental model and reality is the core goal of both good code style and good UX design.

**Psychology — Cognitive Load Theory:** John Sweller's Cognitive Load Theory argues that working memory has a limited capacity. Complex syntax (nested .then() chains with closures) consumes more working memory than linear code. Async/await reduces **extraneous cognitive load** — the load imposed by the presentation of information rather than the information itself — leaving more capacity for understanding the actual logic. Good language design and good teaching both aim for this same reduction.

# Lore Conclusion

*The modern tongue is not simpler magic — it is the same magic, rendered legible. The steps are the same: dispatch, wait, read, act. But now they are written clearly, one after another, for all to follow. Your fellow apprentices can read your incantations without a cipher. Your future self will thank you. The Academy's archives grow richer when they can be understood, not just executed.*

---
