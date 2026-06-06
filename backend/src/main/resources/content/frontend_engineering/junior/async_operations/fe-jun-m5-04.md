---
id: fe-jun-m5-04
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
lesson: promises
title: "Promises"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-03]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Names and describes all three states of a Promise"
    - "Explains what .then() receives and when it runs"
    - "Explains what .catch() handles and when it runs"
    - "Explains why Promises are preferable to nested callbacks"
  keywords: [Promise, pending, fulfilled, rejected, then, catch, resolve, reject, asynchronous]
  modelAnswer: |
    A Promise is an object that represents an eventual result. It has three states: pending (operation in flight), fulfilled (succeeded with a value), and rejected (failed with a reason). .then(onFulfilled) runs when the Promise fulfils and receives the resolved value. .catch(onRejected) runs when the Promise rejects and receives the error. Promises solve callback hell — deeply nested callbacks that are hard to read and error-prone — by allowing you to chain operations linearly.
guidedSteps:
  - id: fe-jun-m5-04-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A fetch request is sent but the server hasn't responded yet. What state is the Promise in?"
    inputConfig:
      options:
        - "Fulfilled — it was dispatched successfully"
        - "Rejected — it hasn't returned yet"
        - "Pending — the operation is still in flight"
        - "Settled — all Promises start settled"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Pending — the operation is still in flight"]
      rejectedFeedback: "A Promise is pending from the moment it's created until it either fulfils or rejects. Mid-flight means pending."
    hint: "Think of 'pending' like an order placed but not yet delivered."
    reflectionPrompt: "Can a Promise go from fulfilled back to pending? Why or why not?"
  - id: fe-jun-m5-04-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "Where in a Promise chain does the `.catch()` handler run?"
    inputConfig:
      options:
        - "After every .then(), regardless of success or failure"
        - "Only when the Promise rejects or when a .then() throws an error"
        - "Only when the HTTP status code is 500"
        - "Only on network failures, not on application errors"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Only when the Promise rejects or when a .then() throws an error"]
      rejectedFeedback: ".catch() handles rejections and errors thrown in preceding .then() handlers. It won't run if everything succeeds."
    hint: "Think of .catch() as a try/catch block at the end of your chain."
    reflectionPrompt: "If you throw an error inside a .then(), where does it end up?"
  - id: fe-jun-m5-04-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: "Explain why Promise chaining (using .then().then()) is better than nesting callbacks inside callbacks. What problem does it solve?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [read, nest, indent, flat, chain, hell, callback]
      rejectedFeedback: "Focus on readability and maintainability: deeply nested callbacks create 'callback hell' — code that's hard to follow and error-prone. Chaining keeps it linear."
    hint: "What does deeply nested code look like? What does a flat chain look like?"
    reflectionPrompt: "Have you ever seen code with 4 or 5 levels of indentation from nested callbacks? How does that affect readability?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `.then(value => ...)` receive as its argument?"
    options:
      - "The error if the Promise rejected"
      - "The resolved value when the Promise fulfils"
      - "The Promise object itself"
      - "The HTTP response status code"
    correctIndex: 1
    feedback: "Correct — .then() receives the fulfilled value. For fetch, the first .then() gets a Response object; after .json(), the second .then() gets your parsed data."
retrieval:
  recall: "List the three states of a Promise and describe what each one means."
  explain: "Explain what happens to an error thrown inside a .then() handler. Where does it go? How do you catch it?"
  mistakeId:
    code: |
      fetch('/api/data')
        .then(response => response.json())
        .then(data => {
          if (!data) throw new Error('No data');
          renderData(data);
        });
      // No .catch()
    answer: "Without a .catch(), any rejection or thrown error in the chain is silently swallowed (or triggers an unhandled rejection warning). Always add .catch() at the end of a chain to handle errors."
---

# Hook

Your app makes three API calls in sequence. The first one fails silently. The second one crashes because it expected data from the first. The third never runs. The error message is cryptic. You have no idea where it went wrong because you never added error handling. Promises have a built-in mechanism for exactly this — three states, a .then() for success, a .catch() for failure — and once you understand the model, chasing async bugs becomes dramatically simpler.

# Lore Introduction

*Every quest dispatched from the Academy has one of three outcomes: the emissary is still travelling (pending), they returned victorious with the prize (fulfilled), or they encountered disaster and returned empty-handed (rejected). A wise Master plans for all three. They do not assume success. They do not ignore failure. They prepare a course of action for each outcome — and they chain those plans in the order they must occur. This is the Promise: not just a commitment, but a complete contract for all possible futures.*

# Core Learning

## Concept Introduction

A **Promise** is an object that represents an asynchronous operation that will eventually produce a value. When you call `fetch()`, you get back a Promise. You can then attach handlers to that Promise using `.then()` and `.catch()`.

A Promise is always in one of three states:

| State | Meaning |
|-------|---------|
| **Pending** | The operation is in flight — no result yet |
| **Fulfilled** | The operation succeeded — has a resolved value |
| **Rejected** | The operation failed — has an error/reason |

Once a Promise settles (fulfils or rejects), it cannot change state. It is immutable.

## Why It Matters

Before Promises, asynchronous JavaScript used callbacks — functions passed as arguments to be called later. This led to "callback hell": deeply nested, hard-to-read code with poor error handling. Promises flatten async code into readable chains and give you a single `.catch()` for the whole chain.

Understanding Promises is foundational to understanding async/await, React Query, and virtually every other async pattern in the JavaScript ecosystem.

## Worked Example

```js
// Creating and consuming a Promise
const myPromise = new Promise((resolve, reject) => {
  // Simulate async work
  setTimeout(() => {
    const success = Math.random() > 0.5;
    if (success) {
      resolve({ message: 'Data loaded!' });
    } else {
      reject(new Error('Something went wrong'));
    }
  }, 1000);
});

myPromise
  .then(value => {
    console.log('Success:', value.message); // "Data loaded!"
  })
  .catch(error => {
    console.error('Failed:', error.message); // "Something went wrong"
  });
```

Chaining multiple `.then()` calls:
```js
fetch('/api/users/1')
  .then(response => {          // receives: Response object
    if (!response.ok) throw new Error(`${response.status}`);
    return response.json();    // returns: Promise<User>
  })
  .then(user => {              // receives: User object
    return fetch(`/api/teams/${user.teamId}`); // returns: Promise<Response>
  })
  .then(response => response.json())           // returns: Promise<Team>
  .then(team => {              // receives: Team object
    console.log('Team:', team.name);
  })
  .catch(error => {            // handles any rejection in the whole chain
    console.error('Chain failed:', error.message);
  });
```

## Common Mistakes

**Mistake 1: Not returning from inside .then()**
```js
// WRONG — next .then() receives undefined because nothing is returned
.then(response => {
  response.json(); // not returned!
})
.then(data => console.log(data)) // data is undefined
```

**Mistake 2: No .catch() handler**
```js
// RISKY — silent failures, hard to debug
fetch('/api/data')
  .then(res => res.json())
  .then(data => render(data));
// Where does an error go? Nowhere useful.
```

**Mistake 3: Nesting .then() instead of chaining**
```js
// WRONG — callback hell pattern with Promises
fetch('/api/a')
  .then(res => res.json()
    .then(data => fetch('/api/b')
      .then(res2 => res2.json()
        .then(data2 => console.log(data2)))));

// CORRECT — flat chain
fetch('/api/a')
  .then(res => res.json())
  .then(data => fetch('/api/b'))
  .then(res => res.json())
  .then(data => console.log(data));
```

## Mini Summary

- A Promise has three states: **pending**, **fulfilled**, **rejected**
- `.then(handler)` runs when the Promise fulfils; the handler receives the resolved value
- `.catch(handler)` runs when any Promise in the chain rejects or when a `.then()` throws
- Always **return** values from `.then()` to pass them down the chain
- Always add a `.catch()` at the end of any Promise chain

# Guided Practice Quest

Work through the guided steps to reinforce the three states and the role of .then() and .catch() in a chain.

# Solo Practice Quest

Explain Promises to someone who has only used synchronous code. Cover: what a Promise is, its three states, how .then() and .catch() work, and why this is better than nested callbacks. Use a concrete analogy alongside any code example.

# Integration

**Psychology — Uncertainty Tolerance:** Promises model a cognitive challenge humans face constantly: operating under uncertainty. Pending is the most uncomfortable state — we don't know what will happen. Research in psychology shows that uncertainty is often more stressful than a known negative outcome. Developers who don't handle the pending state well (missing loading UI) pass that cognitive discomfort to their users. Designing for all three states — pending, success, failure — is both good engineering and good psychology.

**Mathematics — State Machines:** A Promise is a simple state machine: a formal model with a defined set of states and valid transitions. In a Promise, the only valid transitions are pending → fulfilled and pending → rejected. There is no fulfilled → pending or rejected → fulfilled. State machines are a cornerstone of computer science and formal verification — and every time you reason about a Promise's state, you're applying state machine theory.

# Lore Conclusion

*The contract is written. Three possible futures, all accounted for. When the emissary returns victorious, you have a plan. When they return empty-handed, you have a different plan. And while they travel — you work. No more frozen halls awaiting news that may never come. The Academy is not shaken by uncertainty; it has planned for it. Neither, now, will your code be.*

---
