---
id: se-jun-m4-02
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m4
moduleTitle: "Module 4: APIs & Networking"
moduleGlyph: "🌐"
moduleSortOrder: 4
topicSlug: rest
topicTitle: "REST"
topicSortOrder: 2
lesson: rest
title: "REST"
sortOrder: 2
difficulty: 2
estimatedMinutes: 25
xpReward: 70
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m4-01]
integrationDomains: [crud_apis, json]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Lists at least three REST constraints and explains each in a sentence"
    - "Designs resource-oriented URLs using nouns (not verbs)"
    - "Maps CRUD operations to the correct HTTP verb + URL combination"
    - "Explains why REST uses JSON as the default body format"
    - "Distinguishes a RESTful API from an RPC-style API by URL design"
  keywords: [REST, resource, URI, stateless, uniform interface, CRUD, GET, POST, PUT, DELETE, JSON, nouns, client-server, idempotent]
  modelAnswer: |
    // REST CRUD mapping for a /spells resource:
    // GET    /spells          → list all spells
    // GET    /spells/{id}     → get one spell
    // POST   /spells          → create a new spell
    // PUT    /spells/{id}     → replace a spell
    // PATCH  /spells/{id}     → partial update
    // DELETE /spells/{id}     → delete a spell

    // Nested resource:
    // GET    /wizards/{id}/spells     → list spells for wizard
    // POST   /wizards/{id}/spells     → add spell to wizard

    // NOT RESTful (verb in URL):
    // POST /spells/createSpell   ← wrong: createSpell is a verb
    // GET  /spells/deleteSpell/5 ← wrong: GET for deletion
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: REFLECTION
    instruction: "Convert these RPC-style URLs to RESTful resource URLs. For each, also specify the correct HTTP method: (a) GET /getWizard?id=5, (b) POST /createSpell, (c) POST /deleteWizard/3, (d) GET /getAllCourses, (e) POST /updateCourseTitle/7."
    inputConfig:
      language: java
      starterCode: |
        // Convert each to RESTful design:
        // (a) GET /getWizard?id=5        → ? METHOD  ? URL
        // (b) POST /createSpell           → ? METHOD  ? URL
        // (c) POST /deleteWizard/3        → ? METHOD  ? URL
        // (d) GET /getAllCourses           → ? METHOD  ? URL
        // (e) POST /updateCourseTitle/7   → ? METHOD  ? URL
    markingRule: "GET /wizards/5, POST /spells, DELETE /wizards/3, GET /courses, PATCH /courses/7 (or PUT) — all use resource nouns, actions encoded in HTTP method"
    hint: "REST uses nouns for URLs. The HTTP method (GET/POST/PUT/DELETE) expresses the action. Remove verbs from URLs."
    reflectionPrompt: "Why do RESTful APIs use nouns in URLs rather than verbs? What does the verb role belong to?"
  - id: step-2
    sortOrder: 2
    inputType: REFLECTION
    instruction: "Design a complete RESTful API surface for a 'Tournament' resource with nested 'Participants'. Specify method, URL, and one-line description for at least 7 endpoints (tournaments and their participants)."
    inputConfig:
      language: java
      starterCode: |
        // Design the REST API for Tournament + Participants:
        // Tournaments:
        // ? /tournaments         → ?
        // ? /tournaments/{id}    → ?
        // ...
        // Participants (nested):
        // ? /tournaments/{id}/participants → ?
        // ...
    markingRule: "GET list, GET single, POST create, PUT/PATCH update, DELETE for tournaments; at least GET list and POST for nested participants; URL structure uses nouns, no verbs"
    hint: "Nested resources use /parent/{id}/child pattern. Use the tournament's id as the path segment to scope participants."
    reflectionPrompt: "When should you use a nested URL like /tournaments/{id}/participants vs a query parameter like /participants?tournamentId={id}?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which URL design is most RESTful?"
    options:
      - "POST /api/createNewWizard"
      - "GET /api/wizard/fetchById/5"
      - "POST /api/wizards"
      - "GET /api/getWizardList"
    correctIndex: 2
    feedback: "POST /api/wizards is correct REST: the URL uses a noun (wizards as a collection resource) and the HTTP method POST indicates creation. The other options embed verbs in the URL (createNewWizard, fetchById, getWizardList) — REST encodes actions in HTTP methods, not URLs."
  - type: MULTIPLE_CHOICE
    question: "What does the REST 'stateless' constraint mean?"
    options:
      - "The server never stores any data permanently"
      - "Each request must contain all information needed to process it; no server-side session state"
      - "The API can only return static responses"
      - "The client must not maintain any state between requests"
    correctIndex: 1
    feedback: "Stateless in REST means the server processes each request independently — it does not rely on session state stored between requests. Authentication credentials, context, and any other needed information must be included in each request. This enables horizontal scaling: any server instance can handle any request."
retrieval:
  recall: "List four REST constraints. For each, write one sentence explaining what it requires."
  explain: "Explain the difference between a RESTful URL design and an RPC-style URL design. Give one example of each for a 'create user' operation."
  mistakeId:
    code: |
      // REST API design:
      GET  /api/spell/get/5
      POST /api/spell/create
      GET  /api/spell/delete/5
      POST /api/spell/update/5
    answer: "These URLs violate REST: (1) verbs in URLs (get, create, delete, update) — REST encodes actions in HTTP methods; (2) GET for deletion violates HTTP safety semantics. Correct design: GET /api/spells/5, POST /api/spells, DELETE /api/spells/5, PUT or PATCH /api/spells/5"
---

# Hook

REST is the architectural style that most modern web APIs follow. It is not a protocol or a standard — it is a set of constraints that, when followed, produce APIs that are predictable, scalable, and easy for any client to consume. When you see a URL like `/api/users/42/orders`, you already know what it represents before reading any documentation. That predictability is what REST delivers, and it is why the entire industry converged on it.

# Lore Introduction

The Academy's inter-school API network was chaos. Each school designed endpoints their own way: `/getWizardInfo`, `/school/fetchAll`, `/tournament/3/doRegister`. Integrating a new school took weeks of reading documentation. A senior architect proposed a standard: all schools must use resource-oriented URLs with consistent HTTP method semantics. The first school to migrate found that new clients integrated in hours instead of weeks. Within a year every school had adopted the standard. REST did not make the APIs more powerful — it made them universally understandable.

# Core Learning

## Concept Introduction

**What is REST?**
REST (Representational State Transfer) is an architectural style for designing networked APIs, defined by Roy Fielding in 2000. It is defined by constraints, not a specific technology.

**Core REST Constraints:**

1. **Client-Server:** Clients and servers are separate concerns. The client handles UI/UX; the server handles data and business logic. They communicate only through a well-defined interface.

2. **Stateless:** Each request contains all information needed. No server-side session state between requests.

3. **Cacheable:** Responses must indicate whether they can be cached. Caching improves performance and scalability.

4. **Uniform Interface:** The most important constraint. All resources are accessed via a consistent interface using: resource identifiers (URIs), standard methods (HTTP verbs), self-descriptive messages (headers + content type).

5. **Layered System:** Clients do not know if they are talking to the origin server or an intermediate (load balancer, cache, gateway).

6. **Code on Demand (optional):** Servers can send executable code to clients (e.g., JavaScript). Rarely used in APIs.

**Resources and URIs:**
REST models everything as a *resource* — a noun. URIs identify resources:
```
/wizards              ← collection of all wizards
/wizards/42           ← specific wizard with id 42
/wizards/42/spells    ← spells belonging to wizard 42
```

**CRUD to HTTP verb mapping:**
| Operation | HTTP Method | URL | Description |
|---|---|---|---|
| Read all | GET | /spells | List all spells |
| Read one | GET | /spells/{id} | Get specific spell |
| Create | POST | /spells | Create new spell |
| Replace | PUT | /spells/{id} | Replace spell |
| Update | PATCH | /spells/{id} | Partial update |
| Delete | DELETE | /spells/{id} | Delete spell |

**JSON as default:**
REST does not mandate JSON, but JSON has become the default because: it is human-readable, language-agnostic, lightweight, and natively parsed by browsers. Most REST APIs use `Content-Type: application/json`.

## Why It Matters

When every API uses REST conventions, clients can predict how new endpoints will behave without reading documentation. A `/wizards/{id}/courses` endpoint — even if you have never seen it before — clearly means "courses for a specific wizard". Consistent HTTP method semantics mean caches know what to cache (GET responses), clients know what to retry safely (idempotent methods), and load balancers route requests correctly. REST is the vocabulary that makes APIs self-describing.

## Worked Examples

**Example 1 — RESTful vs RPC-style URL comparison**

```
// RPC-style (verbs in URLs — NOT REST):
GET  /getSpell?id=5
POST /createSpell
POST /deleteSpell?id=5
POST /updateSpellPower?id=5&power=80

// RESTful (nouns in URLs, actions in HTTP methods):
GET    /spells/5
POST   /spells
DELETE /spells/5
PATCH  /spells/5         body: { "power": 80 }
```

**Example 2 — Nested resource design**

```
// Spells belonging to a specific wizard:
GET    /wizards/42/spells         → list wizard 42's spells
POST   /wizards/42/spells         → add a spell to wizard 42
DELETE /wizards/42/spells/7       → remove spell 7 from wizard 42

// Enrollments (many-to-many: wizard ↔ course):
GET  /courses/10/enrollments      → all enrollments for course 10
POST /courses/10/enrollments      body: { "wizardId": 42 }
```

**Example 3 — REST API in Spring Boot (preview)**

```java
@RestController
@RequestMapping("/api/spells")
public class SpellController {

    @GetMapping               // GET /api/spells
    public List<Spell> list() { ... }

    @GetMapping("/{id}")      // GET /api/spells/{id}
    public Spell getById(@PathVariable Long id) { ... }

    @PostMapping              // POST /api/spells
    public Spell create(@RequestBody CreateSpellRequest req) { ... }

    @DeleteMapping("/{id}")   // DELETE /api/spells/{id}
    public void delete(@PathVariable Long id) { ... }
}
```

## Common Mistakes

- **Verbs in URLs.** `/api/createSpell` violates REST. The HTTP method is the verb. URLs should contain only nouns.
- **Using GET for operations with side effects.** `/api/deleteWizard/5` via GET will be cached and replayed by browsers.
- **Nesting too deeply.** `/api/schools/1/departments/2/courses/3/students/4` becomes unmanageable. Maximum two levels of nesting is a common guideline.
- **Using POST for everything.** POST for reads misses caching, idempotency, and HTTP semantics. Use GET for retrieval.
- **Plural inconsistency.** Use either always plural (`/wizards`) or always singular (`/wizard`) — mixing causes confusion. Plural is the convention for collections.

## Mental Model

REST sees your entire API as a map of named places (resources) that you interact with using a small set of verbs (HTTP methods). The place names are nouns: `/wizards`, `/spells`, `/tournaments`. The actions are always the same five verbs: GET, POST, PUT, PATCH, DELETE. This constraint — consistent nouns + fixed verbs — is what makes REST APIs predictable. A new developer can read the URL and know what resource is involved; they read the method and know what action is happening. No other documentation needed for the basic structure.

## Mini Summary

- REST is an architectural style defined by constraints: stateless, uniform interface, client-server, cacheable.
- Resources are nouns in URLs: `/wizards`, `/wizards/5`, `/wizards/5/courses`.
- HTTP methods encode the action: GET=read, POST=create, PUT=replace, PATCH=update, DELETE=remove.
- Nested resources: `/parent/{id}/child` scopes child to a specific parent.
- JSON is the de facto standard body format for REST APIs.
- No verbs in URLs — the HTTP method is the verb.

# Guided Practice Quest

Complete the two reflection steps: convert five RPC-style URLs to RESTful equivalents with correct HTTP methods, then design a complete REST API surface for a Tournament resource with nested Participants.

# Solo Practice Quest

Design a RESTful API for a "Guild" system. A Guild has Members. Members can have Roles within a Guild. Write a complete endpoint specification (method, URL, request/response format) for: CRUD operations on Guilds, listing and adding Members to a Guild, assigning a Role to a Member within a Guild, and removing a Member from a Guild. Include the JSON body structure for at least three endpoints. Identify any endpoints where the nested URL depth exceeds two levels and propose a flatter alternative.

# Integration

REST is the bridge between HTTP (the protocol) and your Spring Boot API. In **CRUD APIs**, you will implement these REST endpoints using Spring annotations. In **JSON**, you will structure the request and response bodies. In **Status Codes**, you will return the correct HTTP status for each REST operation. In **Testing**, you will write integration tests that hit your REST endpoints with `TestRestTemplate`. When you learn **Databases**, your REST API's CRUD operations will map directly to SQL INSERT/SELECT/UPDATE/DELETE operations.

**Integration question:** A REST API has `GET /wizards` and `POST /wizards`. A developer proposes adding `GET /wizards/search?name=Aldric`. Is this RESTful? Is it a good design? What is the alternative?

# Lore Conclusion

The Academy's inter-school network is now predictable. Every school uses the same pattern: nouns in URLs, HTTP methods as verbs, JSON as the exchange format. A developer at any school can look at a URL they have never seen — `/tournaments/12/participants` — and immediately understand it as "participants in tournament 12". Integration takes hours, not weeks. The API surface is self-describing. REST did not make the underlying systems more powerful; it made the interface between them universally comprehensible. That is what a good standard does.
