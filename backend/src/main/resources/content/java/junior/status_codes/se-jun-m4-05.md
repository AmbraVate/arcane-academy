---
id: se-jun-m4-05
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m4
moduleTitle: "Module 4: APIs & Networking"
moduleGlyph: "🌐"
moduleSortOrder: 4
topicSlug: status_codes
topicTitle: "Status Codes"
topicSortOrder: 5
lesson: status_codes
title: "Status Codes"
sortOrder: 5
difficulty: 2
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m4-04]
integrationDomains: [crud_apis, error_strategies]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Matches 200/201/204 to their use cases in REST CRUD operations"
    - "Distinguishes 400 (client error — bad input) from 401 (not authenticated) from 403 (authenticated but forbidden)"
    - "Explains when to use 404 vs 409 Conflict"
    - "Explains the difference between 4xx (client error) and 5xx (server error)"
    - "Returns the correct status code in a Spring ResponseEntity for at least four scenarios"
  keywords: [200 OK, 201 Created, 204 No Content, 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found, 409 Conflict, 500 Internal Server Error, HttpStatus, ResponseEntity]
  modelAnswer: |
    // 200 OK — read operations
    return ResponseEntity.ok(spell);

    // 201 Created — successful creation
    return ResponseEntity.status(HttpStatus.CREATED).body(created);

    // 204 No Content — successful delete or update with no body
    return ResponseEntity.noContent().build();

    // 400 Bad Request — invalid input
    return ResponseEntity.badRequest().body("Name cannot be blank");

    // 404 Not Found — resource doesn't exist
    return ResponseEntity.notFound().build();

    // 409 Conflict — resource already exists
    return ResponseEntity.status(HttpStatus.CONFLICT).body("Spell already exists");
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: REFLECTION
    instruction: "For each scenario, name the correct HTTP status code and explain why: (a) GET /spells/99 where spell 99 does not exist, (b) POST /spells with a valid request body, (c) DELETE /spells/5 successfully, (d) POST /spells with an empty name, (e) GET /spells/1 when the user is not logged in."
    inputConfig:
      language: java
      starterCode: |
        // Identify the correct status code for each scenario:
        // (a) GET /spells/99 - spell does not exist  → ?
        // (b) POST /spells - created successfully     → ?
        // (c) DELETE /spells/5 - deleted successfully → ?
        // (d) POST /spells - name field is blank      → ?
        // (e) GET /spells/1 - user not authenticated  → ?
    markingRule: "404 for missing resource, 201 for successful creation, 204 for successful deletion, 400 for invalid input, 401 for unauthenticated"
    hint: "4xx = client caused the problem. 5xx = server caused the problem. 2xx = success."
    reflectionPrompt: "When would a POST request return 200 instead of 201? Is that ever appropriate?"
  - id: step-2
    sortOrder: 2
    inputType: REFLECTION
    instruction: "Explain the difference between 401 and 403. Give a concrete scenario where each applies in an Academy API. Also explain the difference between 400 and 422."
    inputConfig:
      language: java
      starterCode: |
        // Explain:
        // 401 Unauthorized: ?
        // 403 Forbidden: ?
        // Scenario for 401: ?
        // Scenario for 403: ?

        // 400 Bad Request vs 422 Unprocessable Entity:
        // 400: ?
        // 422: ?
    markingRule: "401 = not authenticated (no token or invalid token), 403 = authenticated but not permitted (wrong role), concrete scenario for each, 400 = malformed request, 422 = well-formed request but semantically invalid"
    hint: "401: 'Who are you?' (no valid credentials). 403: 'I know who you are, but you cannot do this' (lacks permission)."
    reflectionPrompt: "Your API returns 403 when a user tries to access another user's data. Is that correct, or should it return 404 to avoid revealing the resource exists?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A POST /api/wizards request is received with valid JSON but the wizard's name is already taken. What status code should be returned?"
    options:
      - "400 Bad Request — the input is invalid"
      - "404 Not Found — the wizard does not exist yet"
      - "409 Conflict — the resource conflicts with existing state"
      - "500 Internal Server Error — an unexpected failure occurred"
    correctIndex: 2
    feedback: "409 Conflict is the correct code when the request cannot be completed because it conflicts with the current state of the resource — such as a duplicate name or unique constraint violation. 400 is for malformed or invalid input, not semantic conflicts with existing data."
  - type: MULTIPLE_CHOICE
    question: "What is the difference between a 4xx and a 5xx status code?"
    options:
      - "4xx means the server handled the request; 5xx means it was rejected"
      - "4xx indicates client-caused errors; 5xx indicates server-side failures"
      - "4xx is for JSON parsing errors; 5xx is for database errors"
      - "4xx is always retryable; 5xx errors are permanent"
    correctIndex: 1
    feedback: "4xx status codes indicate the client made a request the server cannot fulfil — bad input (400), missing authentication (401), forbidden access (403), or non-existent resource (404). 5xx codes indicate the server failed to process a valid request — often a bug or infrastructure issue. 4xx = client's problem; 5xx = server's problem."
retrieval:
  recall: "List the status codes for: successful read, successful create, successful delete, resource not found, bad input, and server error."
  explain: "Explain the difference between 401 and 403. In what order should an API check authentication and authorisation?"
  mistakeId:
    code: |
      @GetMapping("/{id}")
      public ResponseEntity<SpellDto> getById(@PathVariable Long id) {
          Spell spell = spellService.findById(id);
          if (spell == null) {
              return ResponseEntity.ok(null); // wrong!
          }
          return ResponseEntity.ok(new SpellDto(spell));
      }
    answer: "Returning 200 OK with a null body when the resource is not found is incorrect. Clients receive a 200 status suggesting success, then get null data — a silent failure. Fix: return `ResponseEntity.notFound().build()` (404) when the resource is absent. Clients can then detect the absence cleanly."
---

# Hook

Status codes are the traffic signals of HTTP. They tell clients immediately whether their request succeeded, failed, or needs to be handled differently — without parsing the response body. A client that receives 201 knows a resource was created. A client that receives 404 knows the resource does not exist. A client that receives 409 knows the operation conflicts with current state. Using the correct status code is not optional style — it is the contract your API makes with every client that calls it.

# Lore Introduction

The Academy's early API returned 200 OK for everything — including failures. "User not found? 200 OK with null body." "Invalid spell name? 200 OK with an error message in the body." Client developers spent days writing defensive null-checks and text-parsing code to detect failures. When a senior engineer refactored the API to return proper status codes, clients became simpler overnight: check the status code first, parse the body only on 2xx, display the error message on 4xx. The API contract was now communicated by the protocol itself, not buried in the response body.

# Core Learning

## Concept Introduction

**Status Code Classes:**
- **1xx — Informational:** Rarely used in REST APIs
- **2xx — Success:** Request was received, understood, and processed
- **3xx — Redirection:** Client must take additional action
- **4xx — Client Error:** The client made a request the server cannot fulfil
- **5xx — Server Error:** The server failed to fulfil a valid request

**The Essential REST Status Codes:**

**Success (2xx):**
| Code | Name | When to Use |
|---|---|---|
| 200 | OK | Successful GET, PUT, PATCH |
| 201 | Created | Successful POST that creates a resource |
| 204 | No Content | Successful DELETE or PUT with no response body |

**Client Errors (4xx):**
| Code | Name | When to Use |
|---|---|---|
| 400 | Bad Request | Malformed JSON, missing required fields, invalid format |
| 401 | Unauthorized | Missing or invalid authentication token |
| 403 | Forbidden | Authenticated but lacks permission for this action |
| 404 | Not Found | Resource does not exist |
| 409 | Conflict | Request conflicts with existing state (duplicate name, version conflict) |
| 422 | Unprocessable Entity | Valid format but semantically invalid (business rule violation) |

**Server Errors (5xx):**
| Code | Name | When to Use |
|---|---|---|
| 500 | Internal Server Error | Unhandled exception, database failure, unexpected error |
| 503 | Service Unavailable | Server is temporarily unavailable (overloaded, maintenance) |

**Spring Boot status codes:**
```java
HttpStatus.OK              // 200
HttpStatus.CREATED         // 201
HttpStatus.NO_CONTENT      // 204
HttpStatus.BAD_REQUEST     // 400
HttpStatus.UNAUTHORIZED    // 401
HttpStatus.FORBIDDEN       // 403
HttpStatus.NOT_FOUND       // 404
HttpStatus.CONFLICT        // 409
HttpStatus.INTERNAL_SERVER_ERROR // 500
```

## Why It Matters

Status codes are part of your API's contract. Clients use them to decide what to do next: retry on 503, prompt login on 401, display "not found" on 404, show validation errors on 400 or 422. If your API returns 200 for everything, clients must parse response bodies to detect failures — error-prone and fragile. Correct status codes make your API self-documenting, enable standard client error-handling patterns, and work correctly with HTTP infrastructure (caches, gateways, monitoring).

## Worked Examples

**Example 1 — CRUD endpoints with correct status codes**

```java
@GetMapping("/{id}")                              // 200 or 404
public ResponseEntity<SpellDto> getById(@PathVariable Long id) {
    return spellService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}

@PostMapping                                       // 201
public ResponseEntity<SpellDto> create(@RequestBody CreateSpellRequest req) {
    SpellDto created = spellService.create(req);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}

@DeleteMapping("/{id}")                           // 204
public ResponseEntity<Void> delete(@PathVariable Long id) {
    spellService.delete(id);
    return ResponseEntity.noContent().build();
}
```

**Example 2 — Validation errors and conflicts**

```java
@PostMapping
public ResponseEntity<?> create(@RequestBody CreateSpellRequest req) {
    if (req.name() == null || req.name().isBlank()) {
        return ResponseEntity.badRequest().body("Spell name is required"); // 400
    }
    if (spellService.existsByName(req.name())) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body("Spell '" + req.name() + "' already exists"); // 409
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(spellService.create(req));
}
```

**Example 3 — Security codes**

```java
// 401: Not authenticated (handled by security framework, but shown for clarity)
// Request missing Authorization header → 401 Unauthorized

// 403: Authenticated but insufficient permission
@GetMapping("/admin/all-wizards")
public ResponseEntity<List<WizardDto>> adminList(Authentication auth) {
    if (!auth.getAuthorities().contains("ROLE_ADMIN")) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
    }
    return ResponseEntity.ok(wizardService.findAll());
}
```

## Common Mistakes

- **Returning 200 OK with null body for missing resources.** Clients receive a success signal then null data. Return 404 instead.
- **Confusing 401 and 403.** 401 means "not authenticated — provide credentials". 403 means "authenticated but not authorised". They require different client behaviour.
- **Returning 500 for client input errors.** A 500 says "the server crashed". Bad client input should be 400 or 422. Reserve 5xx for actual server failures.
- **Never returning 204 for DELETE.** Returning 200 with the deleted object's data is common but not standard. 204 No Content is the conventional response for successful DELETE with no response body.
- **Returning 404 when 403 is correct.** Some APIs return 404 instead of 403 to hide that a resource exists (security through obscurity). This is a valid security decision, but it should be intentional.

## Mental Model

Status codes are a brief, standardised summary of the outcome — before the client reads the body. Think of it like a restaurant receipt: the green checkmark means "paid and complete", the red X means "payment failed", the yellow alert means "item unavailable — see note". The body provides details; the status code tells the client which branch to take in its decision tree. Correct status codes mean clients can be written against the contract, not against parsing heuristics.

## Mini Summary

- 200 OK: successful GET, PUT, PATCH with body.
- 201 Created: successful POST that creates a resource.
- 204 No Content: successful DELETE or update with no response body.
- 400 Bad Request: client sent invalid input.
- 401 Unauthorized: missing or invalid authentication.
- 403 Forbidden: authenticated but not permitted.
- 404 Not Found: resource does not exist.
- 409 Conflict: request conflicts with existing state.
- 500 Internal Server Error: unhandled server-side failure.

# Guided Practice Quest

Complete the two reflection steps: match five scenarios to correct status codes with reasoning, then distinguish 401 vs 403 and 400 vs 422 with concrete scenarios.

# Solo Practice Quest

Design a complete status code mapping for a "Tournament Registration" API. For each of these scenarios, specify the HTTP method, URL, status code, and a one-line description of when it applies: tournament not found, registration successful, already registered (duplicate), not logged in, not permitted to register (spectator role), invalid tournament date in request body, server database unavailable, successful cancellation of registration. Present as a table. For each 4xx, specify whether it is 400, 401, 403, 404, 409, or 422 and justify the choice.

# Integration

Status codes integrate directly with the error handling you studied in Module 3. In Spring Boot, a `@ControllerAdvice` class maps exception types to status codes: `@ExceptionHandler(SpellNotFoundException.class)` returns 404, `@ExceptionHandler(ConflictException.class)` returns 409. This means the status code your API returns is determined by the exception your service throws — the layers are connected. In **Testing**, your integration tests will assert specific status codes: `assertEquals(201, response.getStatusCode().value())`. In the frontend, React code handles 401 by redirecting to login and 400 by displaying validation messages — the UI behaviour is driven by your status code choices.

**Integration question:** Spring Boot returns 500 when an unhandled exception propagates from a controller. How would you convert a `SpellNotFoundException` (a custom RuntimeException) to a 404 response automatically for all controllers, without adding a try/catch to each controller method?

# Lore Conclusion

The Academy API now speaks in status codes. Clients no longer parse response bodies looking for error indicators. A 404 means the spell does not exist; 409 means it conflicts with an existing one; 201 means it was created successfully. Monitoring systems alert on elevated 5xx rates. Security infrastructure intercepts 401s. The API contract is communicated by the protocol, not buried in custom fields. Status codes are not bureaucracy — they are the vocabulary that makes HTTP APIs machine-readable, client-friendly, and operationally observable.
