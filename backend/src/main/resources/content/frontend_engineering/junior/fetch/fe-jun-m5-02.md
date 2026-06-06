---
id: fe-jun-m5-02
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m5
moduleTitle: "Module 5: API Integration"
moduleGlyph: "🌐"
moduleSortOrder: 5
topicSlug: fetch
topicTitle: "Fetch"
topicSortOrder: 1
lesson: reading_responses
title: "Reading Responses"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-01]
integrationDomains: [mathematics, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the difference between the Response object and the actual data"
    - "Describes what response.ok and status codes communicate"
    - "Explains why response.json() returns a Promise"
    - "Chains .then() calls correctly in an explanation or example"
  keywords: [response, json, status, ok, then, chain, parse, body]
  modelAnswer: |
    When fetch resolves, it gives you a Response object — not your data. The Response has metadata like response.ok (true if status is 200-299) and response.status (the HTTP status code). To get your actual data, you call response.json() which reads the response body stream and parses it as JSON — this is also asynchronous and returns another Promise. You chain a second .then() to receive the parsed data. Always check response.ok before parsing to handle HTTP errors cleanly.
guidedSteps:
  - id: fe-jun-m5-02-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "After the first `.then(response => ...)` resolves, what type is the `response` object?"
    inputConfig:
      options:
        - "A plain JavaScript object containing your data"
        - "A JSON string"
        - "A Response object with metadata and an unread body stream"
        - "An array of results from the server"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A Response object with metadata and an unread body stream"]
      rejectedFeedback: "The first .then() gives you a Response object — a wrapper with status, headers, and a body stream. You haven't read the data yet."
    hint: "Think of it as an envelope — you have the envelope, but you haven't opened it yet."
    reflectionPrompt: "Why do you think fetch separates the metadata (status, headers) from the body content?"
  - id: fe-jun-m5-02-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "What does `response.ok` return when the server responds with a 404 status?"
    inputConfig:
      options:
        - "true — the request technically completed"
        - "false — ok is only true for 200-299 status codes"
        - "It throws an error"
        - "undefined"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["false — ok is only true for 200-299 status codes"]
      rejectedFeedback: "response.ok is false for any status outside 200-299. A 404 means the resource wasn't found — that's an error, even though the network request technically succeeded."
    hint: "The HTTP status code range 200-299 means success. Anything outside that is a problem."
    reflectionPrompt: "Why is it dangerous to call response.json() without first checking response.ok?"
  - id: fe-jun-m5-02-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: "Write a fetch chain that: requests `/api/user/1`, checks if the response is ok, then parses the JSON body. If not ok, throw an error."
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: [response.ok, response.json, throw]
      rejectedFeedback: "Your chain should check response.ok and throw if false, then return response.json() to trigger a second .then() with the parsed data."
    hint: "Pattern: fetch(url).then(res => { if (!res.ok) throw new Error(...); return res.json(); }).then(data => ...)"
    reflectionPrompt: "What happens in the .catch() handler if you throw inside a .then()?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why does calling `response.json()` return a Promise rather than the data directly?"
    options:
      - "It's a bug in the fetch specification"
      - "JSON parsing is CPU-intensive and needs to run in a worker"
      - "The response body is a stream that must be read asynchronously"
      - "It always returns undefined for empty responses"
    correctIndex: 2
    feedback: "The body arrives as a readable stream. response.json() reads that stream and parses the result — both of which are async operations, so it returns a Promise."
retrieval:
  recall: "What are the three pieces of information you can get from the Response object before calling response.json()?"
  explain: "Explain the difference between a 200 response and a 404 response from the perspective of fetch's Promise — does the Promise reject in either case?"
  mistakeId:
    code: |
      fetch('/api/users')
        .then(response => {
          const data = response.json();
          console.log(data[0].name); // error
        });
    answer: "response.json() returns a Promise, not the data directly. You must chain another .then() — or use await — to get the actual parsed data."
---

# Hook

You've sent the fetch request. It resolved. You have a `response` object. You try `console.log(response.name)` — undefined. You try `response[0]` — undefined. You try `JSON.parse(response)` — error. The data is *somewhere* in there, but you can't see it. Every developer has wrestled with this. The Response object is not your data — it's a wrapper around your data, and you need to know how to open it.

# Lore Introduction

*The Academy messenger has returned from the Eastern Tower. But they haven't handed you the scroll itself — they've handed you a sealed leather satchel. The satchel has markings on the outside: a wax seal indicating success, a guild stamp, a date. This is your `response.ok` and `response.status`. To read the scroll itself, you must undo the clasp, unroll the parchment, and decipher the text — that is `response.json()`. The information exists; you simply must know how to retrieve it from the container.*

# Core Learning

## Concept Introduction

When a fetch Promise resolves, it doesn't give you your data. It gives you a **Response** object — a representation of the HTTP response that includes:

- **`response.status`** — the HTTP status code (200, 201, 404, 500, etc.)
- **`response.ok`** — a boolean: `true` if status is in the 200–299 range
- **`response.headers`** — the response headers
- **The body** — the actual data, accessed via `response.json()`, `response.text()`, or `response.blob()`

The body is a **readable stream** — it hasn't been read yet when the first `.then()` fires. Calling `response.json()` reads and parses that stream, which is itself asynchronous — so it returns another Promise.

## Why It Matters

Understanding the two-step process (Response metadata → parse body) prevents one of the most common fetch bugs: assuming `response` is your data. It also enables proper error handling, since you can check `response.ok` before attempting to parse.

HTTP status codes communicate the outcome of every request. Knowing what they mean lets you build robust, user-friendly error handling:

| Range | Meaning |
|-------|---------|
| 200–299 | Success |
| 300–399 | Redirects |
| 400–499 | Client errors (your fault: 400 bad request, 401 unauthorised, 404 not found) |
| 500–599 | Server errors (their fault: 500 internal server error) |

## Worked Example

```js
fetch('https://jsonplaceholder.typicode.com/users/1')
  .then(response => {
    // Step 1: inspect the Response object
    console.log(response.status); // 200
    console.log(response.ok);     // true

    // Guard: if not OK, throw — this triggers the .catch()
    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`);
    }

    // Step 2: parse the body — returns another Promise
    return response.json();
  })
  .then(user => {
    // Step 3: now we have our actual data
    console.log(user.name); // "Leanne Graham"
    console.log(user.email);
  })
  .catch(error => {
    console.error('Something went wrong:', error.message);
  });
```

Using `response.text()` instead for plain text responses:
```js
fetch('/api/health')
  .then(res => res.text())
  .then(text => console.log(text)); // "OK"
```

## Common Mistakes

**Mistake 1: Forgetting response.json() returns a Promise**
```js
// WRONG
.then(response => {
  const data = response.json(); // data is a Promise, not the object!
  console.log(data.name);       // undefined
})

// CORRECT
.then(response => response.json()) // return the Promise
.then(data => console.log(data.name)) // handle it in the next .then()
```

**Mistake 2: Calling response.json() on error responses**
```js
// RISKY — a 500 error might not return valid JSON
fetch('/api/data')
  .then(response => response.json()) // crashes if body isn't JSON
```

**Mistake 3: Checking status manually when response.ok exists**
```js
// VERBOSE — prefer response.ok
if (response.status >= 200 && response.status < 300) { ... }

// CLEAN
if (response.ok) { ... }
```

## Mini Summary

- `fetch()` resolves with a **Response object**, not your data
- **`response.ok`** is true for 200–299 status codes; false for 4xx/5xx
- **`response.status`** gives you the exact HTTP status code
- **`response.json()`** reads and parses the body — it returns a **Promise**, so you need a second `.then()`
- Always check `response.ok` before parsing to avoid processing error responses as data

# Guided Practice Quest

Work through the steps above to reinforce the two-step fetch pattern and the meaning of response.ok.

# Solo Practice Quest

Explain the full journey of a fetch request from `fetch(url)` to having usable data in a variable. Why are there two `.then()` calls? What does each one receive? Include the check for `response.ok` and explain why it matters. You may use a code example to support your explanation.

# Integration

**Mathematics — Status Codes as a Taxonomy:** HTTP status codes form a structured numerical taxonomy — a classification system. The hundreds digit defines the category (2xx = success, 4xx = client error, 5xx = server error), and the tens/units digits specify the subcategory. This mirrors how taxonomic systems in biology or library classification work: broad category first, then increasingly specific subdivisions. Understanding this hierarchical structure lets you reason about any status code you haven't seen before — a 429 is clearly a client error (4xx), and by convention it means "too many requests."

**Design — Feedback Loops:** `response.ok` maps to a fundamental design principle: every action needs feedback. In interface design, Norman's "feedback" principle states that users must know what happened as a result of their action. The same applies to code: when your application makes a request, it must know whether it succeeded. Building on `response.ok` is how you create the feedback loop between your app and the server that lets you give meaningful feedback to the user.

# Lore Conclusion

*You've unsealed the satchel. The wax mark confirmed it came from the right source, the guild stamp told you it arrived intact, and now the scroll is in your hands. You've learned to read not just the message, but the container. A true apprentice knows that the envelope is as important as the letter — it tells you whether to trust what's inside.*

---
