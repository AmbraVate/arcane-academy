---
id: fe-app-m1-05
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m1
moduleTitle: "Module 1: Understanding the Web"
moduleGlyph: "🌐"
moduleSortOrder: 1
topicSlug: the_internet
topicTitle: "The Internet"
topicSortOrder: 1
lesson: http_requests
title: "HTTP Requests"
sortOrder: 5
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m1-04]
integrationDomains: [sciences, history]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Names the four main HTTP methods and their purposes"
    - "Describes the structure of an HTTP request"
    - "Explains what HTTP headers are used for"
    - "Distinguishes between GET and POST correctly"
    - "Explains HTTPS and why it matters"
  keywords: [http, get, post, put, delete, header, body, method, https, request, response, tls]
  modelAnswer: |
    HTTP (HyperText Transfer Protocol) is the protocol used to transfer data on the Web.
    The four main methods are GET (retrieve), POST (create), PUT/PATCH (update), DELETE (remove).
    A request consists of a method, URL, headers (metadata), and optional body (data payload).
    HTTPS adds TLS encryption to HTTP, protecting data in transit from eavesdropping and tampering.
guidedSteps:
  - id: fe-app-m1-05-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A user submits a registration form to create a new account. Which HTTP method is most appropriate?
    inputConfig:
      options:
        - "GET — to retrieve the user's information"
        - "POST — to submit new data to be created on the server"
        - "DELETE — to remove any existing account with the same email"
        - "PUT — to update an existing user record"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["POST — to submit new data to be created on the server"]
      rejectedFeedback: "POST is used to create new resources. GET retrieves data (and should never change server state). PUT/PATCH updates existing resources. DELETE removes them. Creating a new account = POST."
    hint: "Which method is used to send new data to a server for creation?"
    reflectionPrompt: "Choosing the correct HTTP method is not just convention — it communicates intent to the server and to anyone reading your code. Misusing GET for state changes (e.g., GET /delete-user/42) creates security and caching problems."

  - id: fe-app-m1-05-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "HTTPS adds ___ encryption to HTTP, protecting data in transit between client and server."
    inputConfig:
      placeholder: "TLS"
    markingRule:
      matchMode: CONTAINS
      accepted: [TLS, tls, "TLS/SSL", ssl, encryption]
      rejectedFeedback: "**TLS** (Transport Layer Security) encrypts the connection. Without it, any device on the network path could read your data — passwords, credit card numbers, personal information. HTTPS = HTTP over TLS."
    hint: "It's an acronym for the encryption protocol. Three letters."
    reflectionPrompt: "HTTPS is non-negotiable for any production website. Modern browsers show warnings on HTTP sites. Search engines penalise them. And fundamentally: users trust you with their data — you must protect it."

  - id: fe-app-m1-05-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why a GET request should never be used to delete or modify data on a server. Give a specific example of what could go wrong if it were.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [cache, bookmark, link, safe, idempotent, get, delete, modify]
      rejectedFeedback: "GET requests are cached, bookmarked, prefetched by browsers, and followed by link crawlers. If GET /delete-account/42 worked, a search engine crawling it could delete accounts. GET must be 'safe' — it must not change server state."
    hint: "Think about what browsers, search engines, and caches do with GET requests automatically."
    reflectionPrompt: "The HTTP method contract exists for a reason. GET = safe (no side effects). POST = creates. PUT = replaces. DELETE = removes. Breaking these contracts causes subtle, hard-to-debug bugs and security vulnerabilities."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the purpose of HTTP headers in a request?"
    options:
      - "They contain the main data payload being sent"
      - "They identify the HTTP method to use"
      - "They carry metadata about the request, such as content type and authentication tokens"
      - "They replace the URL"
    correctIndex: 2
    feedback: "Headers carry metadata — not the primary data. Common request headers: `Content-Type` (format of the body), `Authorization` (auth token), `Accept` (what formats the client accepts), `Cookie` (session data)."
  - type: MULTIPLE_CHOICE
    question: "Which HTTP method is designed to be 'idempotent' — meaning calling it multiple times produces the same result as calling it once?"
    options:
      - "POST"
      - "GET"
      - "DELETE"
      - "Both GET and DELETE"
    correctIndex: 3
    feedback: "GET and DELETE are idempotent. Deleting the same resource twice has the same outcome as deleting it once (it's gone). POST is not idempotent — submitting a form twice creates two records. Understanding idempotency helps you decide when retrying a failed request is safe."

retrieval:
  recall: "Name the four main HTTP methods and state in one word what each one does."
  explain: "Why does HTTPS matter for a login form specifically?"
  mistakeId:
    code: "You can use GET requests to update or delete data because they're simpler to implement"
    answer: "GET requests must be 'safe' — they must not change server state. Browsers cache them, search engines crawl them, prefetch tools follow them. Using GET for mutations creates security holes (CSRF attacks) and data corruption. Always use POST/PUT/PATCH/DELETE for state changes."
---

# Hook

Every time your browser communicates with a server, it speaks in a precise, structured language — HTTP.

This language has a vocabulary of methods (`GET`, `POST`, `PUT`, `DELETE`), a grammar of headers and bodies, and a set of conventions that every web developer must understand.

When you fetch data in JavaScript, when you submit a form, when you hit an API — you are speaking HTTP. Fluency in this language makes you a better debugger, a better API consumer, and a more secure developer.

> Before reading on: what do you think `GET` and `POST` mean? Write your current understanding.

# Lore Introduction

The Academy's messengers are trained in a precise formal language. Every message has the same structure: a declaration of intent (method), a precise address (URL), accompanying notes (headers), and sometimes a sealed package (body).

*"Without this structure,"* says Master Aelindra, *"the recipient wouldn't know if you were asking for something or delivering it, updating a record or deleting it."*

She holds up a scroll.

*"This is an HTTP request. Every word in it is deliberate. Every part has a defined role. Learn the language — and you will be able to communicate with any server in the realm."*

# Core Learning

## Concept Introduction

**HTTP** (HyperText Transfer Protocol) is the protocol that powers the Web. Every web interaction — loading a page, fetching data, submitting a form — is an HTTP transaction.

### HTTP Methods

| Method | Purpose | Body? | Safe? | Idempotent? |
|---|---|---|---|---|
| `GET` | Retrieve a resource | No | Yes | Yes |
| `POST` | Create a new resource | Yes | No | No |
| `PUT` | Replace a resource entirely | Yes | No | Yes |
| `PATCH` | Partially update a resource | Yes | No | No |
| `DELETE` | Remove a resource | Optional | No | Yes |

**Safe** = does not change server state. **Idempotent** = calling it multiple times has the same effect as calling it once.

### Request Structure

```
GET /api/products/42 HTTP/1.1
Host: shop.example.com
Accept: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

Components:
1. **Request line** — method, path, HTTP version
2. **Headers** — key-value metadata
3. **Blank line** — separates headers from body
4. **Body** — optional data payload (typically for POST/PUT)

### Response Structure

```
HTTP/1.1 200 OK
Content-Type: application/json
Cache-Control: max-age=3600

{"id": 42, "name": "Widget", "price": 9.99}
```

### HTTPS

HTTP sends data as plaintext — anyone on the network path can read it. **HTTPS** wraps HTTP in **TLS** (Transport Layer Security), encrypting the entire exchange. Every production website must use HTTPS — without it, passwords, tokens, and personal data are exposed.

### Common Request Headers

| Header | Purpose |
|---|---|
| `Content-Type` | Format of the request body (e.g., `application/json`) |
| `Accept` | Formats the client can handle |
| `Authorization` | Authentication token |
| `Cookie` | Session cookies |
| `Cache-Control` | Caching instructions |

## Why It Matters

Frontend engineers write HTTP requests constantly — via `fetch()`, `XMLHttpRequest`, or HTTP client libraries. Understanding the method semantics, header conventions, and HTTPS implications helps you:
- Choose the correct method for each operation
- Debug failed requests by reading them accurately
- Handle authentication correctly
- Understand caching behaviour
- Build secure applications

## Worked Examples

**Example 1 — Fetching a list of products:**
```javascript
fetch('https://api.shop.com/products', {
  method: 'GET',
  headers: {
    'Accept': 'application/json'
  }
})
```

**Example 2 — Creating a new order:**
```javascript
fetch('https://api.shop.com/orders', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + token
  },
  body: JSON.stringify({ productId: 42, quantity: 2 })
})
```

**Example 3 — Deleting a resource:**
```javascript
fetch('https://api.shop.com/orders/99', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer ' + token
  }
})
```

## Common Mistakes

- **Using GET for state-changing operations.** GET must be safe. Use POST to create, PUT/PATCH to update, DELETE to remove.
- **Forgetting `Content-Type` on POST requests.** Without it, the server may not know how to parse the body.
- **Not handling non-200 responses.** `fetch()` in JavaScript does not throw on 4xx/5xx — you must check `response.ok` or `response.status`.
- **Building on HTTP in production.** HTTPS is mandatory. HTTP is for local development only.

## Mental Model

Think of HTTP methods like **post office services**:
- `GET` = request a document from the archive (reading only — no changes)
- `POST` = deliver a new parcel (creates something new)
- `PUT` = replace the entire contents of a filing slot
- `PATCH` = add or change one sheet in the filing slot
- `DELETE` = remove the filing slot entirely

The post office (server) knows what to do with each type of delivery because the method tells it.

## Mini Summary

- HTTP is the protocol for web communication; HTTPS adds TLS encryption
- The four main methods: GET (read), POST (create), PUT/PATCH (update), DELETE (remove)
- Requests have a method, URL, headers, and optional body
- Responses have a status code, headers, and optional body
- GET must be safe (no side effects); POST is not idempotent

# Guided Practice Quest

**The Protocol Envoy**

The Academy dispatches messages using the HTTP protocol. As a newly trained envoy, you must identify the correct method and interpret each message's structure.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You are building a task management API. For each of the following operations, write:
1. The HTTP method you would use
2. A sample URL
3. Whether a request body is needed, and if so, what it might contain
4. The likely success status code

Operations:
- Get a list of all tasks for a user
- Create a new task
- Mark a task as complete
- Delete a task
- Get the details of one specific task

# Integration

**Connecting to Sciences — Protocol as Shared Language**

HTTP is a protocol — a set of agreed rules for communication between parties that don't know each other in advance. This concept appears throughout nature and engineering.

In molecular biology, the "lock and key" model describes how enzymes and substrates must precisely match for a reaction to occur. The shape of the active site is a protocol — it defines what can bind and what cannot.

In human communication, language itself is a protocol — shared conventions that allow strangers to convey meaning. Without the convention, the same sounds carry no information.

HTTP's power comes from the same source: any client and any server that implements the protocol can communicate, regardless of the programming language, operating system, or physical hardware involved. The protocol is the universal translator.

What does the concept of protocol as "shared convention" suggest about the value of standards in engineering systems?

# Lore Conclusion

The envoy returns with the scroll filled — every message precisely structured, every method correctly chosen, every response properly interpreted.

*"You speak the language,"* says Master Aelindra. *"HTTP is not just a technicality — it is the grammar of the Web. Master it, and you can communicate with any server in the world."*

The fifth rune of the Frontend path ignites. The first topic is complete.

*"Now we turn to the browser — the tool your users will use to experience everything you build. What does it do, and how does it do it?"*
