---
id: se-jun-m9-01
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m9
moduleTitle: "Module 9: Junior Project"
moduleGlyph: "🏗️"
moduleSortOrder: 9
topicSlug: mini_project
topicTitle: "Mini Project"
topicSortOrder: 1
lesson: the_task_board_api
title: "The Task Board API"
sortOrder: 1
difficulty: 5
estimatedMinutes: 180
xpReward: 300
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: low
questTypes: [solo]
prerequisites:
  - se-jun-m1-01
  - se-jun-m2-01
  - se-jun-m3-01
  - se-jun-m4-01
  - se-jun-m5-01
  - se-jun-m6-01
  - se-jun-m7-01
  - se-jun-m8-01
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "All CRUD endpoints respond correctly with appropriate HTTP status codes"
    - "Domain model is correctly mapped to the database via an ORM (no raw SQL in service layer)"
    - "Custom exception classes are used; errors return structured JSON responses"
    - "At least one SOLID principle is clearly applied and named in the reflection"
    - "Unit tests cover the service layer; integration tests cover at least one endpoint"
    - "At least one design pattern is used and identified (e.g., Builder, Repository, Strategy)"
    - "Code is committed in small, meaningful commits on a feature branch"
    - "Written reflection explains architectural decisions and tradeoffs"
  keywords: [REST, CRUD, ORM, exception, test, SOLID, pattern, endpoint, repository, service]
  modelAnswer: |
    A complete Task Board API exposes correct CRUD endpoints, maps a domain model
    to the database via JPA/Hibernate, uses custom exceptions with structured error
    responses, is covered by meaningful unit and integration tests, and demonstrates
    at least one design pattern. The reflection shows understanding of why each
    architectural choice was made, not just what was done.
---

# Hook

You know how to write a class. You know how to query a database. You know how to test a method. You know REST, exceptions, design patterns, and git.

But you have never assembled them into something that actually runs as a service — something that accepts HTTP requests, talks to a database, and returns meaningful responses.

That changes now.

> Before you start: draw the layers of your application on paper. What talks to what?

# Lore Introduction

The Guild of Engineers presents a new commission.

*"We need a Task Board,"* says the Guild Master. *"A system that manages tasks: create them, read them, update them, delete them. Simple enough to understand. Complex enough to do properly."*

She slides a scroll across the table detailing the requirements.

*"We do not want clever code. We want correct code. Code that handles errors gracefully, that is covered by tests, that follows the principles you have studied. Do that, and you will have earned your Junior badge."*

# Project Brief

Build a **REST API** called the **Task Board** — a backend service for managing tasks with priorities, categories, and statuses.

---

## Domain Model

```
Task
├── id          (Long, auto-generated)
├── title       (String, required, max 200 chars)
├── description (String, optional)
├── status      (Enum: TODO, IN_PROGRESS, DONE)
├── priority    (Enum: LOW, MEDIUM, HIGH)
├── category    (String, optional — e.g. "Work", "Personal")
└── createdAt   (LocalDateTime, auto-set)
```

---

## API Endpoints

| Method | Path | Description | Success |
|---|---|---|---|
| `POST` | `/api/tasks` | Create a task | `201 Created` |
| `GET` | `/api/tasks` | List all tasks | `200 OK` |
| `GET` | `/api/tasks/{id}` | Get one task | `200 OK` |
| `PUT` | `/api/tasks/{id}` | Update a task | `200 OK` |
| `DELETE` | `/api/tasks/{id}` | Delete a task | `204 No Content` |
| `GET` | `/api/tasks?status=TODO` | Filter by status | `200 OK` |

---

## Technical Requirements

| Requirement | Details |
|---|---|
| **Framework** | Spring Boot (or equivalent MVC framework) |
| **Database** | Any relational DB; access via JPA/ORM (no raw SQL in service layer) |
| **Exception handling** | Custom exceptions; structured JSON error body with `message` and `status` fields |
| **Validation** | Reject requests with missing title; return `400 Bad Request` with a clear message |
| **Testing** | Unit tests on service layer (mocked repository); integration test for at least one endpoint |
| **Design patterns** | Apply and name at least one from Module 7 (Builder, Repository, Strategy, etc.) |
| **Git** | Feature branch; at least 5 meaningful commits; descriptive commit messages |

---

## Acceptance Criteria

- [ ] All 6 endpoints work and return the correct status codes
- [ ] Requesting a task that does not exist returns `404 Not Found` with a JSON body
- [ ] Creating a task with an empty title returns `400 Bad Request`
- [ ] At least 5 unit tests pass (covering service logic, not just happy paths)
- [ ] At least 1 integration test exercises the full request-response cycle
- [ ] A `BUILDER` or similar pattern is used to construct `Task` objects in tests
- [ ] No business logic lives in the controller layer
- [ ] You can name one SOLID principle you applied and explain where

---

## Scaffolding

**Suggested layered architecture:**

```
Controller  →  Service  →  Repository  →  Database
(HTTP in/out)  (business)  (data access)  (persistence)
```

**Custom exception example:**

```java
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Task not found: " + id);
    }
}
```

**Structured error response:**

```java
public record ErrorResponse(String message, int status, Instant timestamp) {}
```

---

## Reflection Prompt

After completing the API, write **4–6 sentences** addressing:

1. Which SOLID principle did you apply and where? What would the code look like without it?
2. Which design pattern did you use? What problem did it solve?
3. What was the hardest part of wiring the layers together?
4. If you were adding this to a production system, what would you add next?

---

# Integration

**Connecting to Mathematics — Big O and Database Queries**

Your filter endpoint (`GET /api/tasks?status=TODO`) retrieves a subset of tasks. If you implement this by loading all tasks into memory and filtering in Java, your solution is O(n) in both time and memory. If you push the filter into a SQL `WHERE` clause, the database can use an index — potentially O(log n) for lookup.

This tradeoff — where filtering happens — is one of the most common performance decisions in backend development. The correct answer depends on dataset size, index availability, and how often the filter is used.

What does this tell you about the relationship between algorithm complexity and database design?

# Lore Conclusion

The Guild Master reviews the repository.

*"Tests: present. Exceptions: handled. Layers: separated. Commits: clear."*

She affixes the Junior seal to your work.

*"A service is not just code that runs. It is code that fails gracefully, that is understood by the next engineer who touches it, and that does exactly what it says it does. You have built that. The next tier is harder."*

The Task Board is live.

---
