# Audit — Software Engineering · Senior

**Auditor lens:** Distinguished Engineer / Technical Director reviewing a senior-track curriculum for interview and on-the-job readiness at FAANG-adjacent companies
**Tier mandate:** Completing this tier should prepare a developer to work independently on distributed systems, concurrency, security, performance, and observability — and to pass a senior software engineer interview at a top-tier company.
**Scope:** 52 lessons across 52 topics (every topic is a single lesson).

---

## 1. Verdict at a glance

The Senior tier makes an ambitious topical sweep — distributed systems, concurrency, event-driven architecture, security, performance, and observability — and the breadth is genuinely impressive for a self-study curriculum. Lesson quality is high: the CAP theorem lesson (se-sen-m4-01) is accurate and appropriately nuanced (PA vs CA is not optional), the database optimisation lesson (se-sen-m6-04) covers EXPLAIN plans, N+1, and HikariCP with genuine depth, and the incident response lesson (se-sen-m8-07) delivers a blameless post-mortem framework with professional rigour. The mini_project (se-sen-m9-01 "The Resilient Service") is an excellent capstone — concurrent load, rate limiting, authentication, async operations, structured logging, and metrics together in one build. The structural problem throughout this tier is identical to Junior: virtually every topic has exactly one lesson, which is inadequate for the depth the mandate requires. Senior engineers interviewing at top companies are expected to not just know what a deadlock is but to identify one in code, reason about the four Coffman conditions, and propose a locking discipline to prevent it. One lesson does not get a learner there. Additionally, several topics listed in the original brief (backpressure, async_patterns, choreography, orchestration, distributed_tracing) are absent from the actual file system — they are covered within other lessons but not as standalone topics. The mission brief's topic list is aspirational, not actual.

**Scores:** Coverage 4/5 · Rigor/Depth 3/5 · Sequencing 4/5 · Practice quality 3/5

---

## 2. KEEP — strengths to preserve

- **se-sen-m4-01 / cap_theorem** — Correct and nuanced. States the Brewer conjecture origin, proves that CA is only achievable on a single node (making the "CA" column in typical CAP diagrams misleading), and correctly classifies Cassandra (AP) and ZooKeeper (CP) with justification. The guided exercise asking learners to classify a database by its behaviour during a partition is the right pedagogical move.
- **se-sen-m6-04 / database_optimisation** — Deep and practical. EXPLAIN/EXPLAIN ANALYZE, B-tree vs hash vs covering indexes, N+1 with JPQL JOIN FETCH solution, HikariCP pool sizing via Little's Law. This is the quality bar the whole tier should hit.
- **se-sen-m8-07 / incident_response** — Blameless post-mortem, incident commander role, severity tiers, communication cadence ("never speculate publicly") — all present and correct. The distinction between "what happened" and "who to blame" is handled with appropriate care.
- **se-sen-m7-04 / kafka** — Topic/partition/offset/consumer-group model is accurate. Retention vs queue deletion distinction is a key Kafka concept handled well. Idempotent consumer pattern for at-least-once delivery is correctly named.
- **se-sen-m5-05 / owasp** — 2021 Top 10 covered with examples and mitigations. Correctly positions Broken Access Control at #1. The insight that Insecure Design is an architectural concern (not an implementation bug) is important and often absent in security curricula.
- **se-sen-m9-01 / mini_project** — `The Resilient Service`: Excellent integration of concurrency, rate limiting, authentication, async, and observability. The design doc requirement (threading model, security choices, failure modes) forces architectural thinking, not just implementation.
- **se-sen-m1-01 / requirements_analysis** — The back-of-envelope calculation model for capacity estimation is professionally essential and rarely taught explicitly. SLA vs SLO vs SLI distinction is precise.
- **se-sen-m3-07 / reactive_programming** — Reactive Manifesto four traits, Mono vs Flux, backpressure explained with Project Reactor code. Correct scoping: notes complexity cost of reactive. This is the appropriate level of detail for a foundational lesson.

---

## 3. CHANGE — restructure / resequence

- **`requirements_analysis` (se-sen-m1-01)** — This lesson belongs more naturally as a lead-tier topic. Requirements analysis at the level taught here (SLAs, RACI, stakeholder conflicts) is primarily the responsibility of architects and tech leads, not individual senior contributors. Its presence in the Senior tier is not wrong, but it feels misplaced as the very first lesson in the tier. A senior developer needs system design fundamentals first (capacity estimation, choosing databases, tradeoffs) before they need stakeholder negotiation skills. **Recommendation:** Move to lesson 3-4 in Module 1, after architectural trade-off content.
- **`monoliths` (se-sen-m1-02) → `modular_architectures` (se-sen-m1-03) → `microservices` (se-sen-m1-04) → `architectural_tradeoffs` (se-sen-m1-05)** — This sequence is correct in principle but `architectural_tradeoffs` should precede the specific architectural styles so learners have a framework for evaluating them. **Recommendation:** Move `architectural_tradeoffs` to position 2, before `monoliths`.
- **`sync_vs_async` (se-sen-m3-01) and `blocking_nonblocking` (se-sen-m3-02)** — These two topics cover overlapping ground (synchronous vs blocking are not the same concept, but they are closely related and the modules risk redundancy). **Recommendation:** Read both lessons carefully; if the content is sufficiently differentiated, add explicit cross-references. If not, merge into a single lesson with a comparison table.
- **`event_choreography` (se-sen-m7-05) and `event_orchestration` (se-sen-m7-06)** — These are correctly differentiated (choreography = implicit coordination via events; orchestration = explicit coordinator). However, they appear after `kafka` and `publish_subscribe`, which introduces the pattern before the architectural distinction. **Recommendation:** The current sequence is acceptable, but add explicit back-references from these lessons to Kafka/pub-sub as implementations of each pattern.

---

## 4. UPDATE — depth / rigor / currency

- **`deadlocks` (se-sen-m2-05)** — A single lesson on deadlocks should include the four Coffman conditions (mutual exclusion, hold-and-wait, no preemption, circular wait) as the canonical analytical framework — not just a description of what deadlocks are. Senior interview questions routinely ask: "What are the necessary conditions for a deadlock, and how would you design a locking discipline to prevent it?" The lesson likely covers the concept but needs this framework explicitly named and drillable.
- **`authentication` (se-sen-m5-01)** — JWT-based authentication is covered but OAuth 2.0 and OIDC (OpenID Connect) are the industry standard for modern web applications. A senior developer who cannot explain the OAuth authorization code flow or distinguish authentication (who you are) from authorization (what you can do via scopes/claims) is missing essential knowledge. **Recommendation:** Add an OAuth 2.0 / OIDC section covering: grant types, token introspection, PKCE for SPAs.
- **`secure_coding` (se-sen-m5-04)** — Secure coding practices need explicit coverage of: input sanitisation (not just validation), SQL injection prevention (parameterised queries — this may be in the SQL lessons but should be reinforced here), XSS prevention (output encoding), CSRF mitigation, and secret management (never hardcode secrets; use environment variables and secret managers). **Recommendation:** Expand with a worked "insecure → secure" code transformation for each vulnerability class.
- **`caching_strategies` (se-sen-m6-05)** — Cache invalidation (cache-aside, write-through, write-behind, read-through) should be explicitly named with their consistency trade-offs. Cache stampede (thundering herd on cache expiry) and probabilistic early expiry as a mitigation are senior-level topics that are likely absent. **Recommendation:** Add cache invalidation taxonomy and cache stampede mitigation.
- **`microservices` (se-sen-m1-04)** — A single lesson on microservices cannot cover the full scope of what a senior engineer needs. Missing: service discovery, circuit breaker pattern (Resilience4j), API gateway vs service mesh, health checks, and distributed transaction problems. These are separate topics in other lessons but the microservices lesson should at minimum reference them. **Recommendation:** Add a "navigation guide" section pointing to related lessons.
- **`consistency_models` (se-sen-m4-02)** — Eventual consistency, strong consistency, and causal consistency are the minimum. MVCC (multiversion concurrency control) as used in Postgres/MySQL should be introduced here as it bridges the distributed systems theory with the database optimisation practice.

---

## 5. REMOVE — cut or merge

- **`backpressure` (listed in mission brief but does not exist as a standalone topic folder)** — The concept is covered within `reactive_programming` (se-sen-m3-07). If a standalone topic is desired, extract from that lesson. Otherwise, the topic brief is aspirational and the existing coverage is adequate.
- **`async_patterns`, `choreography`, `orchestration`, `distributed_tracing` (listed in mission brief, absent as standalone folders)** — These are covered within `async_workflows`, `event_choreography`, `event_orchestration`, and `tracing` respectively. The brief's naming inconsistency is a documentation issue, not a content issue. No action needed on the content; update the brief to match actual folder names.

---

## 6. GAPS — missing canonical topics

| Topic | Why essential at this tier | Suggested placement |
|---|---|---|
| **OAuth 2.0 / OIDC** | Industry standard for authentication delegation. Every senior engineer needs to understand authorization code flow, tokens, scopes, PKCE. JWT alone is insufficient. | Expand `authentication` or new lesson in Module 5 |
| **Circuit Breaker pattern (Resilience4j)** | The circuit breaker is the canonical pattern for failure isolation in microservices — prevents cascading failures. Resilience4j is the Java standard. Absent from the curriculum despite microservices being covered. | New lesson in Module 4 (Distributed Systems) or Module 7 |
| **Distributed transactions / Saga pattern** | Two-phase commit vs Saga is a critical trade-off question in distributed systems design. A senior engineer who cannot explain why 2PC is usually wrong in microservices is missing foundational knowledge. | New lesson in Module 4, after `service_communication` |
| **API design principles (REST maturity, versioning, pagination, idempotency)** | Senior engineers own API design decisions. REST maturity levels, idempotency keys, cursor vs offset pagination, API versioning strategies are absent. | New topic in Module 1 or 4 |
| **Feature flags / feature toggles** | Feature flags are used by virtually every senior development team. They decouple deployment from release and enable trunk-based development. Not covered anywhere. | New lesson in Module 1 (System Design) or Module 8 |
| **Container fundamentals (Docker / Kubernetes concepts)** | Senior engineers in 2024+ are expected to understand Docker images, containers, orchestration concepts, health checks, and basic Kubernetes resources. Completely absent. | New topic in Module 1 (System Design) |
| **gRPC and protobuf** | Modern service-to-service communication increasingly uses gRPC. `service_communication` covers the concept but the specific technology is absent. | Expand `service_communication` |
| **MVCC and transaction isolation levels** | READ COMMITTED, REPEATABLE READ, SERIALIZABLE — isolation levels and MVCC determine how databases handle concurrent transactions. Essential for the `transactions` and `database_optimisation` content to be complete. | New lesson in Module 4 or expand `database_optimisation` |

---

## 7. PRACTICE & ASSESSMENT

The practice model at Senior tier appropriately shifts toward open-ended reflection and pattern-match assessments (rather than multiple-choice), which is correct — senior-level competence is demonstrated through reasoning, not recognition. The `AI_REVIEW` assessment type used in some lead lessons should be considered for the Senior tier's most complex topics (e.g., distributed system design scenarios).

Specific concerns:
- **Concurrency topics** (deadlocks, race conditions, thread safety, synchronisation) need code-based exercises where the learner identifies the bug in a multithreaded program. Textual description of these concepts is insufficient — the skill only forms through reading and reasoning about concurrent code. None of the concurrency lessons appear to include actual broken concurrent Java code for diagnosis.
- **Security topics** need "here is the vulnerable code, find and fix it" exercises. OWASP and secure_coding lessons that explain vulnerabilities without requiring the learner to exploit or fix them are conceptual rather than applied.
- **The mini_project** (se-sen-m9-01) is ambitious and well-designed. The design document requirement (threading model, security choices, failure modes) is the right capstone ask. However, at 360 estimated minutes, learners need intermediate checkpoints — perhaps a "Phase 1: concurrency only" milestone before adding security and observability.
- **System design practice** is absent. Senior engineers are evaluated through system design interviews (design Twitter, design a URL shortener, design a payment system). There are no practice prompts in this format anywhere in the tier.

---

## 8. Prioritized action list

1. **ADD** OAuth 2.0 / OIDC lesson to Module 5 — the most obvious gap given that JWT-only authentication is insufficient for senior engineers.
2. **ADD** Circuit Breaker pattern (Resilience4j) lesson — the most important microservices resilience pattern, absent despite microservices being taught.
3. **ADD** Distributed transactions / Saga pattern lesson — a critical distributed systems design topic.
4. **EXPAND** `deadlocks` to include the four Coffman conditions as a named, drillable framework.
5. **ADD** Docker/container fundamentals topic (2-3 lessons) — expected knowledge at every senior interview.
6. **EXPAND** `caching_strategies` to include cache invalidation taxonomy and cache stampede mitigation.
7. **ADD** System design practice prompts to the mini_project or as a separate "system design challenge" track.
8. **EXPAND** `authentication` with OAuth 2.0 authorization code flow and PKCE.
9. **ADD** API design principles lesson (REST maturity, versioning, idempotency, pagination).
10. **RESEQUENCE** Module 1 to move `architectural_tradeoffs` before `monoliths` so learners have an evaluation framework before encountering specific architectural styles.
