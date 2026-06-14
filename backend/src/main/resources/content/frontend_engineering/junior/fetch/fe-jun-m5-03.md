---
id: fe-jun-m5-03
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
lesson: sending_data
title: "Sending Data"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-01, fe-jun-m5-02]
integrationDomains: [sociology, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the difference between GET and POST in terms of intent"
    - "Describes the role of JSON.stringify when sending data"
    - "Explains why Content-Type must be set for JSON POST requests"
    - "Writes correct POST fetch syntax with method, headers, and body"
  keywords: [POST, method, headers, body, JSON.stringify, Content-Type, application/json]
  modelAnswer: |
    GET requests retrieve data; POST requests send data to create or process something. When sending JSON with fetch, you pass an options object as the second argument with method: 'POST', a headers object containing 'Content-Type': 'application/json', and a body set to JSON.stringify(yourData). JSON.stringify converts a JavaScript object into a JSON string — the format the server expects. Without Content-Type, the server may not know how to parse the body. The same pattern applies to PUT and PATCH requests.
guidedSteps:
  - id: fe-jun-m5-03-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You want to create a new user by sending their name and email to `/api/users`. Which HTTP method should you use?"
    inputConfig:
      options:
        - "GET — to request the user creation page"
        - "POST — to send data and create a new resource"
        - "DELETE — to clear space for the new user"
        - "CONNECT — for a persistent connection"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["POST — to send data and create a new resource"]
      rejectedFeedback: "POST is the conventional method for creating resources. GET is for retrieving data, DELETE for removing it."
    hint: "Think about what you're doing: you're creating something new on the server."
    reflectionPrompt: "Can you think of cases where PUT would be more appropriate than POST for sending data?"
  - id: fe-jun-m5-03-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "Why must you call `JSON.stringify(data)` when setting the fetch body?"
    inputConfig:
      options:
        - "fetch requires all bodies to be base64-encoded"
        - "The body must be a string; JSON.stringify converts your JS object to a JSON string"
        - "JSON.stringify compresses the data to reduce bandwidth"
        - "It's optional — fetch can accept plain objects in the body"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The body must be a string; JSON.stringify converts your JS object to a JSON string"]
      rejectedFeedback: "HTTP request bodies are text. You must serialise your JavaScript object into a JSON-formatted string using JSON.stringify before sending."
    hint: "HTTP is a text protocol. What format does the server expect?"
    reflectionPrompt: "What would happen if you sent a JavaScript object directly in the body without stringifying it?"
  - id: fe-jun-m5-03-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: "Write a complete fetch call to POST `{ username: 'alice', role: 'admin' }` to `/api/users`. Include the method, Content-Type header, and stringified body."
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: [POST, Content-Type, JSON.stringify, application/json]
      rejectedFeedback: "Your fetch call needs: method: 'POST', headers: { 'Content-Type': 'application/json' }, and body: JSON.stringify({ username: 'alice', role: 'admin' })."
    hint: "fetch(url, { method, headers, body }) — all three properties go inside the options object."
    reflectionPrompt: "Why is it good practice to always check response.ok after a POST request?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What happens if you send a POST request with a JSON body but forget to set the `Content-Type: application/json` header?"
    options:
      - "fetch throws an error before sending the request"
      - "The server receives the body but may not parse it correctly as JSON"
      - "The body is automatically converted to a form submission"
      - "Nothing — Content-Type is always inferred automatically"
    correctIndex: 1
    feedback: "Without Content-Type, many servers default to treating the body as plain text or form data and won't parse it as JSON, often resulting in unexpected errors."
retrieval:
  recall: "List the three properties you must add to the fetch options object when making a POST request with a JSON body."
  explain: "Explain why you need both JSON.stringify in the body AND the Content-Type header when sending JSON — what is each one doing?"
  mistakeId:
    code: |
      fetch('/api/items', {
        method: 'POST',
        body: { name: 'Widget', price: 9.99 }
      });
    answer: "Two problems: the body needs JSON.stringify({ name: 'Widget', price: 9.99 }) because HTTP bodies must be strings, and a 'Content-Type': 'application/json' header is missing so the server won't know to parse it as JSON."
---

# Hook

You've built a form — name, email, submit button. The user clicks submit. Nothing happens. You write `fetch('/api/register')` and the server responds... with the wrong user, or an error, or nothing at all. GET requests read data. But forms, logins, new records — those need to *send* data. That requires understanding the second argument of fetch: the options object that configures the method, headers, and body of your request.

# Lore Introduction

*Until now, your messengers have only retrieved scrolls. But the Academy also needs you to send things: new student registrations to the Admissions Hall, spell requests to the Enchantment Ward, reports to the Council. A message that only receives can never shape the world. Today you learn to send, not just receive — to write the scroll, seal it correctly, and dispatch it with the right instructions so the recipient understands exactly what they've been given.*

# Core Learning

## Concept Introduction

`fetch()` accepts a second argument: an **options object** that configures the request. For sending data, the key properties are:

- **`method`** — the HTTP verb: `'POST'`, `'PUT'`, `'PATCH'`, or `'DELETE'`
- **`headers`** — a plain object of HTTP headers, typically including `Content-Type`
- **`body`** — the request body (must be a string for JSON)

The `Content-Type: application/json` header tells the server "the body of this request is JSON-formatted text." Without it, many servers won't know how to parse your data.

`JSON.stringify()` converts a JavaScript object into a JSON string — the format HTTP bodies require. This is the mirror of `response.json()`, which parses JSON back into a JavaScript object.

## Why It Matters

Almost every real application writes data: user registrations, form submissions, settings updates, messages sent. Every one of these is a POST, PUT, or PATCH. Understanding how to correctly configure these requests — method, Content-Type header, and stringified body — is essential for building any interactive application.

## Worked Example

```js
// Creating a new resource with POST
const newUser = {
  username: 'alice',
  email: 'alice@example.com',
  role: 'learner'
};

fetch('/api/users', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(newUser)
})
  .then(response => {
    if (!response.ok) {
      throw new Error(`Failed to create user: ${response.status}`);
    }
    return response.json();
  })
  .then(createdUser => {
    console.log('Created:', createdUser.id, createdUser.username);
  })
  .catch(error => {
    console.error('Error:', error.message);
  });
```

Updating an existing resource with PUT:
```js
fetch(`/api/users/${userId}`, {
  method: 'PUT',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ role: 'admin' })
})
  .then(res => res.json())
  .then(updated => console.log(updated));
```

In a React component with a form:
```jsx
function RegisterForm() {
  const [name, setName] = React.useState('');
  const [email, setEmail] = React.useState('');

  async function handleSubmit(e) {
    e.preventDefault();
    const response = await fetch('/api/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email })
    });
    if (response.ok) {
      console.log('Registered successfully');
    }
  }

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-4 p-6">
      <input
        className="border rounded px-3 py-2"
        value={name}
        onChange={e => setName(e.target.value)}
        placeholder="Name"
      />
      <input
        className="border rounded px-3 py-2"
        value={email}
        onChange={e => setEmail(e.target.value)}
        placeholder="Email"
      />
      <button
        type="submit"
        className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
      >
        Register
      </button>
    </form>
  );
}
```

## Common Mistakes

**Mistake 1: Forgetting JSON.stringify**
```js
// WRONG — sends "[object Object]" as the body
body: { username: 'alice' }

// CORRECT
body: JSON.stringify({ username: 'alice' })
```

**Mistake 2: Missing the Content-Type header**
```js
// RISKY — server may not parse body as JSON
fetch('/api/users', {
  method: 'POST',
  body: JSON.stringify(data) // no headers!
})
```

**Mistake 3: Using GET for state-changing operations**
```js
// WRONG — GET should never modify server state
fetch(`/api/users/delete?id=${userId}`) // please don't do this
```

## Mini Summary

- To send data with fetch, pass a second argument: `{ method, headers, body }`
- Use `'POST'` to create, `'PUT'`/`'PATCH'` to update, `'DELETE'` to remove
- Always set `'Content-Type': 'application/json'` when sending JSON
- Always use `JSON.stringify()` to serialise your object into a string body
- The server's response can still be read with `response.json()` as normal

# Guided Practice Quest

Work through the steps above to practise constructing a POST request with the correct method, headers, and body.

# Solo Practice Quest

Explain the full process of sending a new item to a REST API using fetch. Describe what the `method`, `headers`, and `body` fields do, why `JSON.stringify` is needed, and what `Content-Type: application/json` tells the server. Write an example POST request as part of your answer.

# Integration

**Sociology — Communication Protocols:** Sending a POST request requires both parties to agree on a protocol: you send JSON, you declare that you're sending JSON, and the server agrees to parse it as JSON. This mirrors how human communication depends on shared protocols — language, register, format. A business email and a text message both transmit words, but they carry different implied contracts about formality and urgency. Content-Type is like the subject line and signature block of a letter: it frames how the content should be read.

**Philosophy — Speech Act Theory:** The philosopher J.L. Austin distinguished between *locutionary* acts (saying something), *illocutionary* acts (doing something by saying it), and *perlocutionary* acts (causing an effect in the listener). A POST request is all three: it transmits data (locutionary), declares intent to create (illocutionary), and causes the server to modify its state (perlocutionary). HTTP methods aren't just technical labels — they're commitments about the action's intent, which is why using GET to delete data violates the protocol contract.

# Lore Conclusion

*Your scroll is written, sealed with the proper wax mark, and labelled for the correct ward. The messenger knows: this is not a request for information, it is a delivery — a creation. The Admissions Hall will receive it, recognise its format, and record the new student in the great ledger. You have learned not only to ask, but to give. The Academy grows richer for your contribution.*

---
