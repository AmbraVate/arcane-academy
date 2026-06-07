---
id: se-jun-m4-01
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m4
moduleTitle: "Module 4: APIs & Networking"
moduleGlyph: "🌐"
moduleSortOrder: 4
topicSlug: http
topicTitle: "HTTP"
topicSortOrder: 1
lesson: http
title: "HTTP"
sortOrder: 1
difficulty: 2
estimatedMinutes: 25
xpReward: 60
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m3-04]
integrationDomains: [rest, crud_apis]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Describes the HTTP request/response cycle including client, server, and the four main parts of a request"
    - "Names the five main HTTP methods and the action each represents"
    - "Explains what HTTP headers are and gives two examples"
    - "Explains why HTTP is stateless and what that means for application design"
    - "Matches HTTP status code ranges (2xx/4xx/5xx) to their meaning"
  keywords: [HTTP, request, response, GET, POST, PUT, DELETE, PATCH, headers, body, stateless, status code, URL, method, client, server]
  modelAnswer: |
    // HTTP Request structure:
    // GET /api/spells/fireball HTTP/1.1
    // Host: academy.example.com
    // Accept: application/json
    // Authorization: Bearer <token>
    //
    // HTTP methods:
    // GET    - retrieve resource (safe, idempotent)
    // POST   - create new resource
    // PUT    - replace entire resource (idempotent)
    // PATCH  - partial update
    // DELETE - remove resource (idempotent)
    //
    // Response:
    // HTTP/1.1 200 OK
    // Content-Type: application/json
    // { "name": "Fireball", "power": 80 }
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: REFLECTION
    instruction: "For each scenario, identify the correct HTTP method and explain why: (a) fetching a user's profile, (b) creating a new order, (c) updating just the email field of a user, (d) deleting a post, (e) replacing an entire document."
    inputConfig:
      language: java
      starterCode: |
        // Match each scenario to its HTTP method and explain:
        // (a) Fetch a user's profile → ?
        // (b) Create a new order → ?
        // (c) Update only the email field of a user → ?
        // (d) Delete a blog post → ?
        // (e) Replace an entire product document → ?
    markingRule: "GET for fetch (safe, no side effects), POST for create (new resource, not idempotent), PATCH for partial update, DELETE for removal, PUT for full replacement (idempotent)"
    hint: "PUT replaces the entire resource. PATCH updates specific fields. POST creates a new resource each time it is called."
    reflectionPrompt: "What does 'idempotent' mean? Which HTTP methods are idempotent and why does it matter?"
  - id: step-2
    sortOrder: 2
    inputType: REFLECTION
    instruction: "HTTP is stateless. Explain what this means and describe two mechanisms that web applications use to maintain session state across stateless HTTP requests."
    inputConfig:
      language: java
      starterCode: |
        // Stateless means: each request is independent and carries all information needed.
        // The server does NOT remember previous requests.
        //
        // How do applications maintain state across requests?
        // Mechanism 1: ?
        // Mechanism 2: ?
    markingRule: "Correctly explains stateless as each request being self-contained, identifies cookies and JWT tokens (or session IDs in headers) as common state mechanisms"
    hint: "If the server forgets you after each request, how does it know you are still logged in on your next request? The answer is in the request headers."
    reflectionPrompt: "What are the scalability benefits of HTTP being stateless? Why is it easier to run multiple server instances when requests are stateless?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which HTTP method is both safe AND idempotent?"
    options:
      - "POST — creates a new resource each time"
      - "DELETE — each call may return different status codes"
      - "GET — retrieves data without modifying state; repeated calls return the same result"
      - "PATCH — modifies a resource partially"
    correctIndex: 2
    feedback: "GET is safe (no side effects on the server) and idempotent (calling it multiple times has the same effect as calling it once). POST is neither — each call may create a new resource. DELETE is idempotent but not safe. PATCH is neither safe nor necessarily idempotent."
  - type: MULTIPLE_CHOICE
    question: "What does the HTTP Content-Type header tell the receiver?"
    options:
      - "The HTTP method being used"
      - "The format of the message body (e.g., application/json, text/html)"
      - "The authentication token for the request"
      - "The size limit of the response"
    correctIndex: 1
    feedback: "Content-Type describes the media type of the request or response body. Common values: application/json (JSON data), text/html (HTML pages), multipart/form-data (file uploads). The receiver uses Content-Type to know how to parse the body."
retrieval:
  recall: "List the five main HTTP methods. For each, describe the action it represents and whether it is idempotent."
  explain: "Explain what 'stateless' means in the context of HTTP. How does a server know a user is authenticated if it does not remember previous requests?"
  mistakeId:
    code: |
      // Developer uses GET to delete a user:
      // GET /api/users/123/delete
    answer: "Using GET for deletion violates HTTP semantics. GET is a safe method — it must not modify server state. Browsers and proxies cache GET requests and may re-execute them. Use DELETE /api/users/123 instead. Semantic correctness matters: caches, proxies, load balancers, and clients all rely on HTTP method semantics."
---

# Hook

Every web application you have ever used runs on HTTP. When you click a link, your browser sends an HTTP request. When you submit a form, that is another HTTP request. When a mobile app loads your profile, it makes an HTTP request to a server. HTTP is the protocol that powers the web — the rules for how clients ask for things and how servers respond. Understanding it is not optional for a backend developer; it is foundational to everything you will build.

# Lore Introduction

The Academy's long-distance communication system originally had no standards. Some towers sent "GET SCROLL" messages; others sent "FETCH SCROLL PLEASE"; others said nothing and just sent a scroll identifier. Nobody knew what to do with a message that arrived without a verb. The Academy adopted a standard protocol: every message specifies a verb (what to do), a resource path (what to do it to), and headers (metadata about the message). Responses always include a status code. Confusion dropped to zero. That protocol was HTTP, and the Academy never looked back.

# Core Learning

## Concept Introduction

**The HTTP Request/Response Model:**
Every HTTP interaction is a request-response pair. A client (browser, mobile app, another server) sends a request; the server sends a response. The connection is then closed (or reused for subsequent requests in HTTP/1.1+).

**Anatomy of an HTTP Request:**
```
METHOD /path?query=string HTTP/1.1
Header-Name: header-value
Another-Header: value
                          ← blank line separates headers from body
{ "request": "body" }    ← body (only for POST, PUT, PATCH)
```

**HTTP Methods:**
| Method | Action | Idempotent | Safe |
|---|---|---|---|
| GET | Retrieve a resource | Yes | Yes |
| POST | Create a new resource | No | No |
| PUT | Replace an entire resource | Yes | No |
| PATCH | Partial update | No | No |
| DELETE | Remove a resource | Yes | No |

- **Safe**: no side effects on the server (GET, HEAD, OPTIONS)
- **Idempotent**: calling multiple times has the same effect as once (GET, PUT, DELETE)

**HTTP Headers:**
Key-value pairs providing metadata about the request or response.
- `Content-Type: application/json` — format of the body
- `Accept: application/json` — formats the client accepts
- `Authorization: Bearer <token>` — authentication credential
- `Content-Length: 128` — size of the body in bytes
- `Cache-Control: no-cache` — caching instructions

**Stateless:**
HTTP does not maintain state between requests. Each request must carry all the information the server needs to process it. The server has no memory of previous requests from the same client.
State is maintained application-side, not protocol-side:
- Cookies (sent automatically by the browser)
- JWT tokens in `Authorization` header
- Session IDs

**Response structure:**
```
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 48

{ "id": 1, "name": "Fireball", "power": 80 }
```

## Why It Matters

HTTP is the contract between your backend API and every client that uses it. Using the right method communicates intent: GET signals "safe to cache and re-request"; DELETE signals "remove this resource". Incorrect method usage (e.g., GET for deletion) breaks caching, proxies, and client assumptions. Headers carry authentication, content negotiation, and caching directives that affect every layer of the stack. Statelessness is a scalability feature — any server in a cluster can handle any request because no server-side session state exists.

## Worked Examples

**Example 1 — HTTP request structure**

```
GET /api/spells/fireball HTTP/1.1
Host: academy.example.com
Accept: application/json
Authorization: Bearer eyJhbGciOiJSUzI1NiJ9...
```

```
HTTP/1.1 200 OK
Content-Type: application/json
Cache-Control: max-age=3600

{
  "name": "Fireball",
  "power": 80,
  "manaCost": 35,
  "school": "fire"
}
```

**Example 2 — POST with a request body**

```
POST /api/spells HTTP/1.1
Host: academy.example.com
Content-Type: application/json
Authorization: Bearer <token>

{
  "name": "Ice Shard",
  "power": 45,
  "manaCost": 20
}
```

```
HTTP/1.1 201 Created
Content-Type: application/json
Location: /api/spells/ice-shard

{ "id": 42, "name": "Ice Shard", "power": 45 }
```

**Example 3 — HTTP method semantics in Spring Boot (preview)**

```java
@GetMapping("/spells/{name}")       // GET — safe, idempotent
@PostMapping("/spells")             // POST — creates, not idempotent
@PutMapping("/spells/{name}")       // PUT — replaces, idempotent
@PatchMapping("/spells/{name}")     // PATCH — partial update
@DeleteMapping("/spells/{name}")    // DELETE — removes, idempotent
```

## Common Mistakes

- **Using GET for operations with side effects.** GET requests can be cached and re-sent by browsers and proxies. A GET that deletes data may be executed multiple times silently.
- **Using POST when PUT is correct.** POST for "update" means calling it twice creates two updated states; PUT for full replacement is idempotent — calling it twice is the same as once.
- **Sending sensitive data in URL query parameters.** URLs are logged by servers, proxies, and browsers. Passwords or tokens in query strings (`?password=secret`) are visible in logs. Use headers or body.
- **Ignoring `Content-Type`.** Sending JSON without `Content-Type: application/json` causes the server to not parse the body correctly.
- **Treating HTTP as stateful.** Building applications that rely on server-side request ordering will break under load balancers and multiple server instances.

## Mental Model

Think of HTTP as a postal system between two parties. Each letter (request) must be fully self-contained — the postal system does not remember your previous letters. Your letter specifies: what action (method), which resource (URL), metadata about the letter (headers), and optional contents (body). The reply (response) includes a status code telling you if the action succeeded, failed, or requires further action. The postal system is stateless — it delivers letters, it does not maintain relationships.

## Mini Summary

- HTTP is a stateless request/response protocol between clients and servers.
- GET: retrieve (safe + idempotent); POST: create; PUT: replace; PATCH: partial update; DELETE: remove.
- Headers carry metadata: Content-Type, Authorization, Accept, Cache-Control.
- Stateless means every request must be self-contained — state is managed by clients (cookies, tokens).
- Status codes: 2xx success, 3xx redirect, 4xx client error, 5xx server error.
- Use the correct method — caches, proxies, and clients rely on HTTP semantics.

# Guided Practice Quest

Complete the two reflection steps: match five scenarios to correct HTTP methods with reasoning, then explain HTTP statelessness and name two mechanisms for maintaining session state across stateless requests.

# Solo Practice Quest

Design the HTTP API for a "Wizard Registry" as a written specification. For five endpoints — list all wizards, get wizard by ID, register a new wizard, update a wizard's specialisation field, and deactivate a wizard — specify: the HTTP method, URL path, required request headers, request body structure (if any), success response status code, and at least one error response. Present this as a table or structured list. No code required — focus on correct HTTP semantics for each operation.

# Integration

HTTP is the foundation of everything in Module 4. In **REST**, you will see how HTTP semantics map directly to RESTful resource design. In **CRUD APIs**, you will implement Spring Boot handlers for each HTTP method using `@GetMapping`, `@PostMapping`, etc. In **Status Codes**, you will learn exactly which code to return for each outcome. In **JSON**, you will format the request and response bodies. Every REST API you build in this course is an HTTP API — understanding the protocol makes every other concept in this module click.

**Integration question:** A client sends `DELETE /api/wizards/99` but wizard 99 does not exist. Should the server return 200, 404, or 204? What does the HTTP specification suggest, and why does it matter?

# Lore Conclusion

The Academy's communication protocol is now standardised across every tower. Every message specifies method, path, and headers. Every response includes a status code. Towers cache safe requests, retry idempotent ones, and never re-execute deletions by accident. Most importantly, the communication is stateless — any tower can handle any message from any sender, making the network robust and scalable. The apprentices who first called this "bureaucratic overhead" now design systems that run reliably across a hundred towers. Protocol is not overhead. Protocol is infrastructure.
