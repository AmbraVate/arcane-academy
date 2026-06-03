---
id: se-sen-m9-01
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m9
moduleTitle: "Module 9: Senior Project"
moduleGlyph: "🏗️"
moduleSortOrder: 9
topicSlug: mini_project
topicTitle: "Mini Project"
topicSortOrder: 1
lesson: the_resilient_service
title: "The Resilient Service"
sortOrder: 1
difficulty: 7
estimatedMinutes: 360
xpReward: 500
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: low
questTypes: [solo]
prerequisites:
  - se-sen-m1-01
  - se-sen-m2-01
  - se-sen-m3-01
  - se-sen-m4-01
  - se-sen-m5-01
  - se-sen-m6-01
  - se-sen-m7-01
  - se-sen-m8-01
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Service handles concurrent requests without data corruption or race conditions"
    - "At least one concurrency primitive (lock, semaphore, or concurrent collection) is used correctly"
    - "Rate limiting is implemented and tested under simulated load"
    - "Authentication is enforced on protected endpoints (JWT, API key, or equivalent)"
    - "At least one async operation is non-blocking (Future, CompletableFuture, or reactive)"
    - "Structured logging records key events with correlation IDs or request context"
    - "A health check or metrics endpoint exposes service state"
    - "A written design doc explains threading model, security choices, and failure modes"
  keywords: [concurrency, thread, lock, async, rate-limit, authentication, logging, metrics, resilience]
  modelAnswer: |
    A complete Resilient Service handles concurrent load without corruption, enforces
    rate limiting per client, authenticates callers, performs at least one operation
    asynchronously, emits structured logs with request context, and exposes a health
    endpoint. The design document identifies the threading model used, explains the
    security approach, and names at least two failure modes with their mitigations.
---

# Hook

Your Junior project ran correctly. It handled one request at a time, in a predictable sequence, with a database that waited patiently.

Production does not work like that.

Production means a hundred clients hitting your service simultaneously. It means requests that take 500ms while others are still processing. It means attackers probing your endpoints. It means failures you did not anticipate — and logs that need to tell you what happened after the fact.

This project builds that.

> Before you start: write down three ways this service could fail. You will reference this list in your design document.

# Lore Introduction

The Council of Senior Architects assembles.

*"You have built services that work,"* says the Chief Architect. *"Now build one that survives."*

She draws a diagram on the board: dozens of clients, a single service, a database. Arrows everywhere. Some of them red.

*"Thread safety. Authentication. Rate limits. Async operations. Observability. These are not features you add later. They are the architecture. If you do not design for them from the start, you will spend the rest of your career fixing systems that collapse under real load."*

She hands you the brief.

*"Build it right."*

# Project Brief

Build a **Resilient URL Shortener Service** — a production-grade HTTP service that handles concurrent load, enforces rate limits, authenticates callers, and observes itself.

---

## Core Functionality

| Feature | Description |
|---|---|
| **Shorten URL** | `POST /shorten` — accepts a long URL, returns a short code |
| **Redirect** | `GET /{code}` — looks up the code and redirects (or returns the target URL) |
| **Stats** | `GET /stats/{code}` — returns hit count for a short code |
| **Health** | `GET /health` — returns service status and thread pool state |

---

## Technical Requirements

### Concurrency

- [ ] The in-memory store (or cache layer) is **thread-safe** under concurrent reads and writes
- [ ] Use at least one of: `ConcurrentHashMap`, `ReentrantLock`, `AtomicLong`, `synchronized`
- [ ] Hit counting for `GET /{code}` must be race-condition-free

### Rate Limiting

- [ ] Limit each client (by IP or API key) to a configurable maximum requests per minute
- [ ] Requests exceeding the limit return `429 Too Many Requests`
- [ ] The rate limiter state is thread-safe

### Authentication

- [ ] `POST /shorten` requires an authenticated caller (JWT, API key header, or Basic Auth)
- [ ] Unauthenticated requests return `401 Unauthorized`
- [ ] `/health` and `GET /{code}` are public

### Async Processing

- [ ] One operation must be non-blocking: options include async stat recording, async URL validation, or async cleanup of expired codes
- [ ] Use `CompletableFuture`, `@Async`, or a reactive approach

### Observability

- [ ] Structured logs include: timestamp, request ID (generated per request), client IP, operation, outcome
- [ ] `/health` returns: status, uptime, active thread count, total requests served
- [ ] At least one counter metric is tracked (total requests, error count, or cache hit rate)

---

## Design Document (Required)

Before writing code, produce a short design document (300–500 words or a diagram equivalent) covering:

1. **Threading model** — how concurrent requests flow through the system; what is shared state; what protects it
2. **Rate limiter design** — algorithm chosen (token bucket, fixed window, sliding window) and why
3. **Security approach** — what is protected, how credentials are validated, what is intentionally left public
4. **Failure modes** — at least two: what breaks, how the system behaves, what a caller sees

Submit the design document alongside the code.

---

## Load Test (Required)

Write a simple load test (using `JMeter`, `k6`, `wrk`, or a custom script) that:

- Sends at least 50 concurrent requests to `GET /{code}`
- Verifies the hit counter is correct at the end (no lost updates)
- Verifies the rate limiter rejects requests above the threshold

Include the load test script and its output in your submission.

---

## Acceptance Criteria

- [ ] Service starts and all endpoints respond correctly
- [ ] Hit counter is accurate under 50 concurrent requests (verified by load test)
- [ ] Rate limiter correctly rejects clients exceeding the limit
- [ ] Unauthenticated `POST /shorten` returns `401`
- [ ] At least one async operation is demonstrably non-blocking
- [ ] Logs include request ID on every line for a given request
- [ ] `/health` reports thread pool state
- [ ] Design document submitted

---

## Reflection Prompt

After completing the project, write **5–7 sentences** addressing:

1. What concurrency primitive did you choose and why? What alternatives did you consider?
2. Which rate limiting algorithm did you implement? What are its failure modes?
3. What was the hardest part of making the service thread-safe?
4. What did the load test reveal that unit tests would not?
5. If you were deploying this to production tomorrow, what would you add first?

---

# Integration

**Connecting to Mathematics — Little's Law and Queue Theory**

Your rate limiter controls how many requests a client can send per unit time. But the *service* itself also has a throughput limit — determined by thread pool size, I/O wait times, and CPU.

Little's Law (from queueing theory) states: **L = λW**, where L is the average number of items in the system, λ is the average arrival rate, and W is the average time an item spends in the system. If your service takes 50ms per request and you receive 100 requests/second, Little's Law predicts you need at least 5 threads in flight at steady state — ignoring queue depth.

Rate limiting protects the service, but it also forces the client to match the system's capacity. What does this suggest about the relationship between rate limits and service throughput?

# Lore Conclusion

The load test completes. The counters are exact. The logs tell the full story of every request.

The Chief Architect reviews the design document, then the metrics endpoint.

*"Thread safety: verified. Auth: enforced. Rate limiting: working. Observability: present."*

She nods.

*"A service that survives load, rejects attackers, and tells you what it is doing — that is a professional piece of work. You understand now why we design before we code."*

The Senior rune is inscribed.

---
