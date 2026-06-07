---
id: se-jun-m4-04
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m4
moduleTitle: "Module 4: APIs & Networking"
moduleGlyph: "🌐"
moduleSortOrder: 4
topicSlug: crud_apis
topicTitle: "CRUD APIs"
topicSortOrder: 4
lesson: crud_apis
title: "CRUD APIs"
sortOrder: 4
difficulty: 3
estimatedMinutes: 35
xpReward: 80
practiceType: JAVA
questType: PRACTICE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m4-03]
integrationDomains: [status_codes, orms]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Creates a @RestController with @RequestMapping for a resource"
    - "Implements GET list and GET by id endpoints using @GetMapping"
    - "Implements POST create using @PostMapping with @RequestBody"
    - "Implements DELETE using @DeleteMapping with @PathVariable"
    - "Returns appropriate ResponseEntity with correct status codes (200/201/204/404)"
  keywords: [RestController, RequestMapping, GetMapping, PostMapping, PutMapping, DeleteMapping, RequestBody, PathVariable, ResponseEntity, RequestParam, status code]
  modelAnswer: |
    @RestController
    @RequestMapping("/api/spells")
    public class SpellController {

        private final SpellService spellService;

        public SpellController(SpellService spellService) {
            this.spellService = spellService;
        }

        @GetMapping
        public ResponseEntity<List<SpellDto>> list() {
            return ResponseEntity.ok(spellService.findAll());
        }

        @GetMapping("/{id}")
        public ResponseEntity<SpellDto> getById(@PathVariable Long id) {
            return spellService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        }

        @PostMapping
        public ResponseEntity<SpellDto> create(@RequestBody CreateSpellRequest req) {
            SpellDto created = spellService.create(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
            spellService.delete(id);
            return ResponseEntity.noContent().build();
        }
    }
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Create a SpellController @RestController with @RequestMapping(\"/api/spells\"). Inject a SpellService via constructor. Implement GET /api/spells returning List<SpellDto> with 200 OK, and GET /api/spells/{id} returning SpellDto or 404."
    inputConfig:
      language: java
      starterCode: |
        import org.springframework.web.bind.annotation.*;
        import org.springframework.http.*;
        import java.util.*;

        @RestController
        @RequestMapping("/api/spells")
        public class SpellController {

            private final SpellService spellService;

            // constructor injection
            public SpellController(SpellService spellService) {
                this.spellService = spellService;
            }

            // GET /api/spells → 200 + list
            @GetMapping
            public ResponseEntity<List<SpellDto>> list() { return null; }

            // GET /api/spells/{id} → 200 + spell, or 404
            @GetMapping("/{id}")
            public ResponseEntity<SpellDto> getById(@PathVariable Long id) { return null; }
        }
    markingRule: "GET /api/spells returns ResponseEntity.ok(spellService.findAll()), GET /api/spells/{id} uses @PathVariable, calls service, returns 200 with body or 404 not found"
    hint: "ResponseEntity.ok(body) for 200, ResponseEntity.notFound().build() for 404. If service returns Optional, use .map(ResponseEntity::ok).orElse(notFound)."
    reflectionPrompt: "Why use ResponseEntity instead of just returning the object directly? What additional control does it give you?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Add POST /api/spells with @RequestBody to create a spell (return 201 Created with the created spell), and DELETE /api/spells/{id} (return 204 No Content)."
    inputConfig:
      language: java
      starterCode: |
        // Add to SpellController:

        // POST /api/spells → 201 + created spell
        @PostMapping
        public ResponseEntity<SpellDto> create(@RequestBody CreateSpellRequest request) {
            // create, return 201
            return null;
        }

        // DELETE /api/spells/{id} → 204 No Content
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
            // delete, return 204
            return null;
        }
    markingRule: "POST uses @RequestBody CreateSpellRequest, calls service.create(), returns ResponseEntity.status(HttpStatus.CREATED).body(created), DELETE calls service.delete(id) and returns ResponseEntity.noContent().build()"
    hint: "ResponseEntity.status(HttpStatus.CREATED).body(dto) for 201. ResponseEntity.noContent().build() for 204."
    reflectionPrompt: "Why does POST return 201 Created rather than 200 OK? Why does DELETE return 204 No Content rather than 200?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does @PathVariable do in a Spring controller method?"
    options:
      - "Reads a value from the HTTP request body"
      - "Reads a query parameter from the URL (?id=5)"
      - "Binds a URL path segment (e.g., /spells/{id}) to a method parameter"
      - "Specifies the HTTP method for the endpoint"
    correctIndex: 2
    feedback: "@PathVariable extracts a value from the URL path template. For @GetMapping(\"/{id}\"), the {id} segment in the URL becomes the method parameter value. Use @RequestParam for query parameters (?id=5) and @RequestBody for the request body."
  - type: MULTIPLE_CHOICE
    question: "What does @RestController do in Spring Boot?"
    options:
      - "It is equivalent to @Controller + @ResponseBody — methods return data serialised to JSON, not view names"
      - "It creates a REST-only HTTP server separate from the main application"
      - "It adds automatic CRUD methods without any implementation"
      - "It enables HTTPS for the controller"
    correctIndex: 0
    feedback: "@RestController combines @Controller (marks the class as a Spring MVC controller) and @ResponseBody (method return values are serialised to HTTP response body via Jackson, not resolved as view names). This is the standard annotation for JSON REST API controllers."
retrieval:
  recall: "List the four Spring Boot annotations for the four HTTP methods covered in this lesson and the URL pattern each uses."
  explain: "Explain the difference between @PathVariable and @RequestParam. Give a URL example for each."
  mistakeId:
    code: |
      @PostMapping("/spells")
      public SpellDto create(SpellDto spell) {  // missing annotation!
          return spellService.create(spell);
      }
    answer: "The 'spell' parameter is missing @RequestBody. Without it, Spring tries to bind the spell from URL query parameters, not the request body. Fix: `public SpellDto create(@RequestBody SpellDto spell)`. The request body will then be deserialised from JSON automatically by Jackson."
---

# Hook

Theory becomes real here. `@RestController`, `@GetMapping`, `@PostMapping`, `@PathVariable`, `@RequestBody` — these five annotations are the foundation of almost every Spring Boot API ever written. In this lesson you will wire together everything from HTTP, REST, and JSON into working code: a CRUD controller that handles real HTTP requests and returns real JSON responses. This is where backend development starts to feel like building something that actually works.

# Lore Introduction

The Academy's spell management used to require a senior wizard to manually update the spell registry in the archive — a process that took days and was prone to transcription errors. An apprentice engineer proposed a REST API: any authorised system could read, create, update, or delete spells via HTTP. After implementing five methods with five annotations, the registry was live. Portals could fetch spell data in milliseconds. Administration consoles could add new spells remotely. The senior wizard stopped transcribing and started reviewing. Five annotations. Enormous impact.

# Core Learning

## Concept Introduction

**@RestController:**
Marks a class as a Spring MVC controller where every method's return value is serialised to JSON and written to the HTTP response body. Combines `@Controller` + `@ResponseBody`.

**@RequestMapping("/api/spells"):**
Sets the base URL path for all methods in the controller.

**HTTP method annotations:**
| Annotation | HTTP Method | Typical URL |
|---|---|---|
| `@GetMapping` | GET | `/api/spells` or `/api/spells/{id}` |
| `@PostMapping` | POST | `/api/spells` |
| `@PutMapping` | PUT | `/api/spells/{id}` |
| `@PatchMapping` | PATCH | `/api/spells/{id}` |
| `@DeleteMapping` | DELETE | `/api/spells/{id}` |

**Parameter annotations:**
- `@PathVariable` — extracts a value from the URL path: `GET /spells/{id}` → `@PathVariable Long id`
- `@RequestBody` — deserialises the request body JSON to a Java object: `@RequestBody CreateSpellRequest req`
- `@RequestParam` — reads a query parameter: `GET /spells?name=fireball` → `@RequestParam String name`

**ResponseEntity\<T\>:**
Wraps the response body with control over the HTTP status code and headers:
```java
ResponseEntity.ok(body)              // 200
ResponseEntity.created(URI).body(b)  // 201
ResponseEntity.noContent().build()   // 204
ResponseEntity.notFound().build()    // 404
ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err) // 422
```

## Why It Matters

CRUD APIs are the building block of every backend service. Approximately 80% of backend API endpoints are variations of list, get-by-id, create, update, and delete. Understanding how Spring Boot maps HTTP requests to Java methods — and how to control the response status code — is the core skill for backend development. The annotations are minimal, but their interactions (path variables, request bodies, response entities) form the complete vocabulary of REST controller development.

## Worked Examples

**Example 1 — Complete CRUD controller**

```java
@RestController
@RequestMapping("/api/spells")
public class SpellController {

    private final SpellService spellService;

    public SpellController(SpellService spellService) {
        this.spellService = spellService;
    }

    // GET /api/spells → 200 + List<SpellDto>
    @GetMapping
    public ResponseEntity<List<SpellDto>> list() {
        return ResponseEntity.ok(spellService.findAll());
    }

    // GET /api/spells/5 → 200 + SpellDto, or 404
    @GetMapping("/{id}")
    public ResponseEntity<SpellDto> getById(@PathVariable Long id) {
        return spellService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/spells → 201 + created SpellDto
    @PostMapping
    public ResponseEntity<SpellDto> create(@RequestBody CreateSpellRequest request) {
        SpellDto created = spellService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/spells/5 → 200 + updated SpellDto, or 404
    @PutMapping("/{id}")
    public ResponseEntity<SpellDto> update(
            @PathVariable Long id,
            @RequestBody UpdateSpellRequest request) {
        return spellService.update(id, request)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/spells/5 → 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        spellService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

**Example 2 — Request and path parameter**

```java
// GET /api/spells?school=fire&minPower=50
@GetMapping
public ResponseEntity<List<SpellDto>> search(
        @RequestParam(required = false) String school,
        @RequestParam(defaultValue = "0") int minPower) {
    return ResponseEntity.ok(spellService.search(school, minPower));
}
```

**Example 3 — Request and response DTOs**

```java
// Separate DTOs for request and response:
public record CreateSpellRequest(String name, int power, int manaCost) {}
public record SpellDto(Long id, String name, int power, int manaCost) {}

// Controller uses request type for input, response type for output
@PostMapping
public ResponseEntity<SpellDto> create(@RequestBody CreateSpellRequest request) {
    SpellDto created = spellService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}
```

## Common Mistakes

- **Missing `@RequestBody` on POST/PUT parameters.** Without it, Spring tries to bind from URL parameters, leaving all fields null.
- **Returning `null` instead of 404.** If a resource is not found, return `ResponseEntity.notFound().build()` — returning null causes Jackson to serialise null to an empty response with 200.
- **Putting business logic in the controller.** Controllers should only handle HTTP concerns (parsing, routing, status codes). Business logic belongs in a service class.
- **Using `@GetMapping("/{id}")` and `@GetMapping("/search")` — ambiguous mapping.** If `{id}` could match "search", Spring may route `/api/spells/search` to the wrong method. Order specific paths before variable paths or use constraints.
- **Not using constructor injection.** Use constructor injection for the service, not `@Autowired` on a field. Constructor injection is testable and makes dependencies explicit.

## Mental Model

A `@RestController` is a routing table. When a request arrives, Spring looks at the URL and HTTP method, finds the matching method in the controller, extracts path variables and body, calls the method, serialises the return value to JSON, and sends the response. The annotations are declarations: "this method handles this HTTP method at this URL". Your job is to express the mapping and write the business logic (in a service) — Spring handles all the HTTP plumbing.

## Mini Summary

- `@RestController` + `@RequestMapping` declares a JSON REST controller.
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` map HTTP methods to methods.
- `@PathVariable` extracts path segments from the URL.
- `@RequestBody` deserialises the request body JSON to a Java object.
- `@RequestParam` reads query parameters from the URL.
- `ResponseEntity` controls the response status code — use 200/201/204/404 correctly.

# Guided Practice Quest

Complete the two steps: implement GET list and GET by id with correct `ResponseEntity` status codes using path variables, then add POST create (201 Created) and DELETE (204 No Content) with request body and path variable handling.

# Solo Practice Quest

Build a complete `WizardController` for a wizard management API. The controller should: inject a `WizardService` via constructor; implement GET `/api/wizards` (list), GET `/api/wizards/{id}` (single or 404), GET `/api/wizards?school=fire` (filter by school using `@RequestParam`), POST `/api/wizards` (create, 201), PATCH `/api/wizards/{id}` (partial update), and DELETE `/api/wizards/{id}` (204). Use `WizardDto` as the response type and separate request types for create and update. Return appropriate `ResponseEntity` status codes for all cases including not-found. Write the service interface (`WizardService`) stub with the method signatures the controller requires.

# Integration

The CRUD controller is the top layer of the three-layer architecture you will build in this course. In **Databases** and **ORMs**, you will implement the `SpellService` and `SpellRepository` that the controller delegates to. In **Status Codes**, you will formalise which codes to return in every scenario. In **Testing**, `@SpringBootTest` and `TestRestTemplate` will call these endpoints end-to-end. In **Exception Handling**, Spring's `@ExceptionHandler` (inside `@ControllerAdvice`) will catch exceptions from the service layer and convert them to structured JSON error responses automatically.

**Integration question:** Your `SpellController.getById()` delegates to `SpellService.findById()`. The service throws `SpellNotFoundException` if not found. How would you handle this: catch in the controller and return 404, or use a `@ControllerAdvice` to map `SpellNotFoundException` to 404 globally? What are the trade-offs?

# Lore Conclusion

Five annotations. One controller class. A complete HTTP interface to the spell registry. Any system with network access can now create, read, update, and delete spells via standard HTTP requests. The senior wizard reviews proposals instead of transcribing them. The administration console updates the registry remotely. Portals cache spell data and refresh it in real time. The five annotations are not magic — they are the result of Spring Boot implementing decades of web framework experience. Your job is to use them correctly, which you now can.
