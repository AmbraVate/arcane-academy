---
id: fe-jun-m5-10
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m5
moduleTitle: "Module 5: API Integration"
moduleGlyph: "🌐"
moduleSortOrder: 5
topicSlug: error_handling
topicTitle: "Error Handling"
topicSortOrder: 4
lesson: network_errors
title: "Network Errors"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-09]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains that fetch only rejects on network failure, not HTTP errors"
    - "Distinguishes between network errors, HTTP errors, and application errors"
    - "Shows how to detect HTTP errors using response.ok"
    - "Explains why this fetch behaviour is a common source of bugs"
  keywords: [network, HTTP, reject, ok, status, error, 404, 500, fetch, throw]
  modelAnswer: |
    fetch's Promise only rejects on genuine network failures — the server is unreachable, DNS fails, or the request is aborted. A 404 or 500 response does NOT cause the Promise to reject — the Promise fulfils with a Response that has response.ok === false. This means if you only use .catch() for error handling, HTTP errors will silently pass through as "successful" responses. You must explicitly check response.ok and throw an error if false. Three categories: network errors (Promise rejects), HTTP errors (ok is false, need manual check), application errors (200 response with error in body).
guidedSteps:
  - id: fe-jun-m5-10-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "The server responds with a 404 Not Found. What does fetch do with its Promise?"
    inputConfig:
      options:
        - "Rejects the Promise with a 404 error"
        - "Fulfils the Promise with a Response where response.ok is false"
        - "Throws a TypeError immediately"
        - "Retries the request automatically"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Fulfils the Promise with a Response where response.ok is false"]
      rejectedFeedback: "fetch fulfils its Promise for any completed HTTP response — including 4xx and 5xx. The Promise only rejects on true network failures. A 404 is a fulfilled Promise with response.ok === false."
    hint: "The server responded — a response arrived. Does that mean success or failure from fetch's perspective?"
    reflectionPrompt: "Why do you think the fetch spec was designed this way? What are the implications for error handling?"
  - id: fe-jun-m5-10-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "Which of the following scenarios causes fetch's Promise to actually REJECT?"
    inputConfig:
      options:
        - "The server responds with 500 Internal Server Error"
        - "The server responds with 401 Unauthorized"
        - "The device loses network connectivity before the response arrives"
        - "The response body is empty"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The device loses network connectivity before the response arrives"]
      rejectedFeedback: "A 500 and a 401 are HTTP responses — they fulfil the fetch Promise (with response.ok === false). Only true network failures — no connection, DNS failure, request aborted — cause a reject."
    hint: "Think about whether a response was received at all. If there's a response, even an error response, the Promise fulfils."
    reflectionPrompt: "What code handles network failures (Promise rejections) vs HTTP errors (response.ok === false)?"
  - id: fe-jun-m5-10-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: "Write the pattern that correctly handles both network errors AND HTTP errors in a fetch call. Use async/await."
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [response.ok, throw, try, catch]
      rejectedFeedback: "The pattern: async function load() { try { const res = await fetch(url); if (!res.ok) throw new Error('HTTP ' + res.status); const data = await res.json(); } catch(err) { /* handles both */ } }"
    hint: "You need a check for response.ok inside the try block, AND a catch block for network failures."
    reflectionPrompt: "Could you put both checks in the catch block? Or does the HTTP error check need to go elsewhere?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is an 'application error' in the context of API responses?"
    options:
      - "An error in your React component's render method"
      - "A 200 response whose JSON body contains an error field (e.g. { error: 'User not found' })"
      - "A TypeScript type mismatch"
      - "A network timeout"
    correctIndex: 1
    feedback: "Application errors are when the server responds 200 OK but the body signals a problem. Some APIs use this pattern: { success: false, error: 'User not found' }. You need to check the body content, not just response.ok."
retrieval:
  recall: "What are the three types of errors you can encounter when using fetch, and which mechanism handles each?"
  explain: "Explain why a developer who only uses .catch() for fetch error handling might miss all their HTTP errors. What should they do instead?"
  mistakeId:
    code: |
      fetch('/api/user/999')
        .then(response => response.json())
        .then(user => displayUser(user))
        .catch(err => showError(err.message));
    answer: "If the server returns 404, the Promise fulfils (not rejects), so .catch() never runs. The code calls response.json() on a 404 response and passes whatever the error body contains to displayUser(). Fix: check response.ok before calling response.json(), and throw if not ok."
---

# Hook

You add error handling to your fetch. You write a `.catch()`. You test it — works fine. You ship it. Users start reporting that they see empty pages, not errors. You dig in and discover: the server has been returning 404s for three weeks, but your `.catch()` never fired. Because fetch doesn't reject on 404. Or 500. Or any HTTP error. It only rejects when the network itself fails. This is the single most common fetch misunderstanding, and it's responsible for more silent failures than almost anything else in frontend code.

# Lore Introduction

*The Academy's complaint system has a flaw that the Council discovered too late: emissaries only report failure if they never returned — if they were lost in a storm or ambushed. If they returned and said "the repository was empty, the ward was sealed, the archivist refused," those were not counted as failures. They were logged as completed missions. The Council had assumed: "returned" equals "succeeded." They were wrong. A mission can return with bad news. Your code must be taught to distinguish completion from success.*

# Core Learning

## Concept Introduction

`fetch()` has a counterintuitive but important behaviour: **it only rejects its Promise on network-level failures**. HTTP error responses (404, 500, 401, 403...) cause the Promise to **fulfil**, not reject.

This means:

```js
// 404 response — does NOT trigger .catch()
fetch('/api/missing')
  .then(res => {
    console.log(res.status); // 404
    console.log(res.ok);     // false
    // Promise fulfilled!
  })
  .catch(err => {
    // This NEVER runs for HTTP errors
    console.log('Network error:', err);
  });
```

Three distinct error categories:

| Error Type | Cause | Detected By |
|------------|-------|-------------|
| **Network error** | No connectivity, DNS failure, request aborted | Promise rejects → `.catch()` |
| **HTTP error** | 4xx or 5xx status code | `response.ok === false` → manual check |
| **Application error** | 200 response with error in body | Parse body, check content |

## Why It Matters

If you only handle Promise rejections, you miss the most common category of errors: 401 (unauthorised), 404 (not found), 422 (validation error), 500 (server crash). These all silently pass through as "successful" responses, often causing bizarre secondary errors when your code tries to use the malformed or empty response body.

## Worked Example

```js
// The complete, robust error handling pattern
async function fetchUser(id) {
  try {
    const res = await fetch(`/api/users/${id}`);

    // Manual check for HTTP errors
    if (!res.ok) {
      // Throw so it's caught by the catch block below
      throw new Error(`HTTP ${res.status}: ${res.statusText}`);
    }

    const user = await res.json();
    return user;

  } catch (err) {
    // Catches BOTH: network errors (fetch rejects) AND HTTP errors (we threw)
    console.error('Failed to fetch user:', err.message);
    throw err; // re-throw so callers can handle it
  }
}

// More detailed HTTP error handling
async function apiRequest(url) {
  const res = await fetch(url);

  if (!res.ok) {
    switch (res.status) {
      case 401:
        throw new Error('Unauthorised — please log in');
      case 403:
        throw new Error('Forbidden — you lack permission');
      case 404:
        throw new Error('Resource not found');
      case 429:
        throw new Error('Too many requests — please wait');
      case 500:
        throw new Error('Server error — try again later');
      default:
        throw new Error(`Unexpected error: ${res.status}`);
    }
  }

  return res.json();
}
```

In a React component:
```jsx
useEffect(() => {
  async function load() {
    setIsLoading(true);
    try {
      const res = await fetch(`/api/users/${userId}`);
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const data = await res.json();
      setUser(data);
    } catch (err) {
      setError(err.message); // handles both error types
    } finally {
      setIsLoading(false);
    }
  }
  load();
}, [userId]);
```

## Common Mistakes

**Mistake 1: Relying solely on .catch() for all errors**
```js
// WRONG — HTTP errors silently succeed
fetch('/api/users/999')
  .then(res => res.json()) // might be a 404 error body
  .then(data => setUser(data)) // sets user to error object!
  .catch(err => setError(err)); // only catches network failures
```

**Mistake 2: Calling response.json() on error responses without checking**
```js
// If server returns 500 with HTML (not JSON), this throws
if (!res.ok) {
  const error = await res.json(); // 💥 if body isn't valid JSON
}

// Safer pattern
if (!res.ok) {
  const errorText = await res.text(); // always works
  throw new Error(errorText || `HTTP ${res.status}`);
}
```

## Mini Summary

- fetch only **rejects** its Promise on network failures — not on 4xx/5xx responses
- Always check **`response.ok`** and throw if false — to route HTTP errors through your catch
- Three error types: network errors (Promise rejects), HTTP errors (ok is false), application errors (200 with error body)
- One `try/catch` block handles both network and HTTP errors when you throw on !response.ok

# Guided Practice Quest

Work through the guided steps to confirm your understanding of when fetch rejects vs fulfils, and how to correctly detect all error categories.

# Solo Practice Quest

Explain why relying only on `.catch()` is insufficient error handling for fetch requests. Describe all three categories of error that can occur, and write a function that correctly handles all three. Include the check for response.ok.

# Integration

**Mathematics — Boolean Algebra and Completeness:** A complete error handling system must cover the full domain of possible outcomes. Mathematically, the set of fetch outcomes is: {network_fail} ∪ {HTTP_error} ∪ {app_error} ∪ {success}. A handler that only covers {network_fail} misses three quarters of the domain. Complete error handling is a coverage problem — analogous to ensuring test cases cover all branches of a boolean expression.

**Philosophy — Epistemic Humility:** A system that assumes "returned response = success" exhibits overconfidence — it ignores evidence to the contrary. The philosopher Karl Popper argued that scientific progress requires falsifiability: a theory must be capable of being disproven. Similarly, good code must be capable of registering failure. A fetch handler that cannot represent failure isn't robust — it's simply unaware of reality. Checking response.ok is a form of epistemic humility: acknowledging that the server might have said "no."

# Lore Conclusion

*The Council reformed the complaint system. Emissaries now report not just whether they returned, but what they found when they arrived. "The repository was empty" — a 404. "The ward was sealed" — a 403. "The archivist refused" — a 401. The Academy's leadership now understands the true state of its operations. Completion and success are no longer confused. The Academy is wiser for it — and so is your code.*

---
