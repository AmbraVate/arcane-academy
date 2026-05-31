---
id: se-sen-m1-04
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m1
moduleTitle: "Module 1: System Design"
moduleGlyph: "🏗️"
moduleSortOrder: 1
topicSlug: microservices
topicTitle: "Microservices"
topicSortOrder: 4
lesson: microservices
title: "Microservices"
sortOrder: 4
difficulty: 4
estimatedMinutes: 40
xpReward: 100
practiceType: NONE
questType: INVESTIGATION
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [economics, philosophy]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Explains service decomposition and how bounded contexts map to service boundaries
    - Describes inter-service communication patterns including synchronous and asynchronous options
    - Articulates the genuine deployment complexity costs of microservices
    - Discusses organisational considerations including Conway's Law and team topology
    - Identifies the distributed monolith anti-pattern and how it arises
  keywords: [microservices, bounded context, service decomposition, inter-service communication, gRPC, REST, event-driven, Conway's Law, distributed monolith, service mesh, deployment complexity]
  modelAnswer: |
    Microservices decompose a system into independently deployable services, each owning its own data and communicating over the network. The correct trigger for this decomposition is not scale, not team size, and not a technology trend — it is validated domain boundaries combined with concrete evidence that independent deployability provides more value than it costs.

    Service decomposition should follow bounded contexts identified through Domain-Driven Design. Each service owns one bounded context: one coherent domain model, one team, one deployment pipeline, one database. The "one team per service" is not aspirational — it is the minimum required for microservices to deliver their promised benefits. Conway's Law (the structure of a software system mirrors the communication structure of the organisation that built it) is not a suggestion; it is a law. If your team structure does not match your service boundaries, your services will develop coupling that mirrors the team dependencies.

    Inter-service communication is the source of most microservices complexity. Synchronous communication (REST, gRPC) is intuitive but creates temporal coupling: if Service B is down, Service A's request fails. Asynchronous communication (event queues, Kafka) decouples availability but introduces eventual consistency, idempotency requirements, and the need for compensating transactions. Neither is universally correct — the choice depends on whether the calling service can tolerate the inconsistency window of async communication.

    The deployment complexity of microservices is not optional overhead — it is inherent to the model. Each service requires its own: CI/CD pipeline, container image, health checks, deployment strategy, rollback procedure, API version management, and observability instrumentation. A 20-service system requires 20 independent versions of all of this infrastructure. Service meshes (Istio, Linkerd) manage inter-service communication, TLS, retry logic, and observability at the infrastructure level — but they are themselves complex systems requiring expertise to operate.

    The distributed monolith is the worst microservices failure mode: services that are deployed separately but are so tightly coupled that they must be deployed together. It arises when services share databases, when service A calls service B synchronously in the critical path of every operation, or when a single business transaction spans multiple services that cannot proceed independently. The test for a healthy microservices architecture is: can you deploy Service A without deploying Service B? If not, you have not achieved independent deployability — you have added distribution costs without distribution benefits.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "You are designing a microservices system. Service A needs data from Service B to complete a user-facing request. Which approach best avoids tight coupling?"
    options:
      - "Service A directly queries Service B's database"
      - "Service A calls Service B synchronously via REST on every request"
      - "Service A subscribes to events published by Service B and maintains its own local copy"
      - "Both services share a common data model library"
    correctIndex: 2
    feedback: "The event subscription pattern (query-side replication) gives Service A local data ownership without runtime coupling to Service B. If B goes down, A continues serving requests from its local store. This is the event-driven approach to avoiding synchronous coupling."
  - type: SHORT_TEXT
    prompt: "Explain Conway's Law and describe a real scenario where ignoring it when designing microservices would produce a distributed monolith."
    hint: "Think about what happens when the team structure does not match the service structure."
  - type: FILL_BLANK
    prompt: "A microservices architecture where services cannot be deployed independently because they are too tightly coupled is called a ___."
    answer: "distributed monolith"
    hint: "It is the worst of both worlds — distribution costs with monolith coupling."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the primary operational justification for microservices that does NOT apply to a well-structured modular monolith?"
    options: ["Better code organisation", "Independent deployability of services by separate teams", "Ability to use different programming languages", "Better database performance"]
    correctIndex: 1
    feedback: "Independent deployability — the ability for Team A to release their service without coordinating with Team B — is the core operational benefit. A modular monolith requires coordinated releases. This benefit only materialises if team structure matches service boundaries."
  - type: MULTIPLE_CHOICE
    question: "Service A must call Service B to complete a financial transaction. Service B is sometimes slow (500ms). What is the correct resilience pattern?"
    options: ["Increase Service A's timeout to 5 seconds", "Implement a circuit breaker that fails fast when Service B is degraded", "Deploy more instances of Service B", "Cache Service B's responses indefinitely"]
    correctIndex: 1
    feedback: "A circuit breaker monitors failure rates and opens (fails fast) when Service B is degraded. This prevents Service A from accumulating blocked threads waiting for slow B responses, which would cascade into Service A also becoming slow and eventually unavailable — the cascade failure pattern."
retrieval:
  recall: "List five concrete pieces of infrastructure you need for every microservice that you do not need for a monolith."
  explain: "Why does Conway's Law mean that organisational design must precede or accompany service boundary design? Give a concrete example of a Conway's Law violation in a microservices architecture."
  mistakeId:
    code: |
      Microservices Design:
      - OrderService: processes orders, calls InventoryService + PaymentService synchronously
      - InventoryService: manages stock, shares inventory table with OrderService
      - PaymentService: processes payments, calls OrderService to update order status
      - All three services must be deployed together due to shared database schema
    answer: "This is a textbook distributed monolith. Three violations are present. First, OrderService calls InventoryService and PaymentService synchronously — if either is down or slow, OrderService fails. This is temporal coupling, defeating the availability benefits of distribution. Second, InventoryService shares a database table with OrderService — this is the most severe violation. It means schema changes require coordinated deployments of both services, and either service can corrupt the other's data. Third, PaymentService calls OrderService back (a circular dependency) — this creates a cycle in the call graph that prevents independent deployment. These services cannot be deployed, scaled, or failed independently. The fix is to break circular dependencies, establish event-driven async communication for non-critical paths, give each service its own data store, and verify that each service can start and process requests without the others being available."
---

# Hook

Microservices have toppled engineering organisations that adopted them without counting the cost. Services that sounded like they promised independence delivered instead a distributed monolith: all the operational complexity of distribution with none of the deployment benefits. In this lesson you will learn to see microservices clearly — when they solve real problems, when they create new ones, and how to distinguish architectural clarity from architectural fashion.

# Lore Introduction

The Academy's most humbling case studies are the systems that began as clean monoliths, were decomposed into microservices by ambitious teams, and arrived at production as distributed balls of mud — each service dependent on three others, no service deployable without a coordinated release, incidents spanning twelve dashboards. Microservices are a powerful pattern, but they are powerful in the same way that a chainsaw is powerful: in the right hands, on the right problem, at the right time.

# Core Learning

## Concept Introduction

Microservices is an architectural style where a system is composed of small, independently deployable services, each:
- Owning a single bounded context and its data
- Communicating over network protocols (HTTP/REST, gRPC, message queues)
- Deployable, scalable, and rollback-able independently of other services
- Maintained by a single team

The last point is the most important and the most frequently ignored. Independent deployability only delivers its promised value when each service is owned by a team that can release it without coordination with other teams. If deploying Service A requires a Slack message to the Service B team asking them to also deploy, you have not achieved independent deployability — you have achieved deployment theatre with extra infrastructure.

**Service Decomposition**

The canonical method for finding service boundaries is mapping to bounded contexts (DDD). Each bounded context has its own ubiquitous language, its own model of the domain, and its own team. When two parts of the system use the same word to mean different things, you have found a potential boundary.

Decomposition signals that justify microservices:
- Different scaling requirements (the image pipeline needs GPU; the auth service needs minimal CPU)
- Different deployment frequencies (the recommendation engine deploys 10 times a day; the billing service deploys once a month)
- Different reliability requirements (the search service can tolerate 0.1% error rate; the payment service cannot)
- Different team ownership with genuine independent release cadences

**Inter-Service Communication**

Services communicate in two fundamental modes:

*Synchronous (request-response):*
- REST over HTTP — universal, human-readable, but text-heavy and loosely typed
- gRPC — binary, strongly typed, lower latency, requires generated clients
- Both create temporal coupling: the caller blocks until the callee responds

*Asynchronous (event-driven):*
- Message queues (RabbitMQ, SQS) — point-to-point, durable delivery
- Event streaming (Kafka) — log-based, durable, replayable, fan-out
- Decouples services temporally but introduces eventual consistency

The strategic choice is not "synchronous OR asynchronous" but "which operations truly require immediate consistency versus which can tolerate a bounded delay."

## Why It Matters

Microservices are often adopted for the wrong reasons: because they are popular, because an engineering leader saw a Netflix talk, or because the monolith has become painful. The right reason is precise: you have validated domain boundaries, you have measured the cost of coupled deployments, and you have the organisational structure to own independent services. Without those foundations, microservices add infrastructure cost and operational complexity in exchange for distribution benefits that are never actually realised.

## Worked Examples

**Example 1: Conway's Law in Practice**

Conway's Law states that organisations design systems that mirror their communication structures. A company with three product teams (User Experience, Transactions, Analytics) will naturally produce three services — even if the optimal domain decomposition is different.

The implication: you cannot design a microservices architecture without also designing the team topology. If you draw service boundaries that do not match team boundaries, Conway's Law will gradually pull the services back into the shape that matches the org chart — through ad-hoc database joins, synchronous call chains, and shared libraries that couple services together.

The Team Topologies framework recommends designing team structures around stream-aligned teams (one team, one service, one value stream) with platform teams providing shared infrastructure. This organisational design must precede the technical design.

**Example 2: The Cascade Failure Pattern**

Service A calls Service B synchronously. Service B calls Service C. Service C becomes slow due to a database query.

Timeline:
- Service C's threads are blocked on DB → C's response time increases to 2 seconds
- Service B's threads are all waiting for C → B becomes slow
- Service A's threads are all waiting for B → A becomes unavailable
- User-facing service calls A → user sees timeouts

This is a cascade failure (or "cascading failure"), and it is endemic to synchronous microservices architectures without resilience patterns. The solution is a circuit breaker on each inter-service call: when downstream latency exceeds a threshold, the circuit opens, and the caller fails fast (or returns a cached/default response) instead of blocking.

**Example 3: The Distributed Monolith**

A team decomposes their monolith into 8 microservices. Six months later:
- Deploying Service A always breaks if Service B is not also deployed (shared database schema)
- A user-facing request calls A → B → C → D in a synchronous chain (4 network hops, 4 points of failure)
- All 8 services share a "common" library that must be updated in sync

This is a distributed monolith. The team has incurred all the costs of microservices (8 CI/CD pipelines, 8 monitoring dashboards, distributed tracing complexity) while retaining the coupling costs of a monolith. It is worse than either alternative.

The root cause was skipping the domain boundary analysis. The services were split by technical layer or by team convenience, not by domain cohesion.

## Common Mistakes

- **Starting with microservices.** Unless you have a very clear understanding of your domain boundaries (proven through existing use), starting with microservices guarantees either a distributed monolith or premature service boundaries that must be refactored.
- **Sharing databases between services.** This is the most common and most damaging violation. A shared database means a shared schema, and a shared schema means coupled deployments.
- **Synchronous call chains longer than two hops.** A→B→C→D synchronous chains produce cascade failure risks and latency multiplication. Flatten them with async communication or aggregate patterns.
- **Underestimating operational overhead.** Each service requires independent monitoring, alerting, deployment pipelines, and runbooks. For a 20-service system, this is 20x the operational surface area. Without a platform team or solid DevOps automation, this overhead becomes unsustainable.
- **Ignoring team topology.** Drawing service boundaries without aligning them to team ownership is the primary cause of distributed monoliths.

## Mental Model

Think of microservices as city-states in a federation. Each city-state is sovereign (independent deployment), has its own laws (domain model), its own treasury (database), and its own army (scaling capacity). Trade between city-states happens through formal diplomatic channels (APIs and events), not through one city sending soldiers to loot another's treasury (shared database access). The federation works when each city-state is genuinely independent. When city-states become economically dependent on each other for every decision, the federation collapses into a bureaucracy more expensive than a unified empire (monolith).

## Mini Summary

- ✔ Microservices provide independent deployability, scaling flexibility, and organisational alignment — at significant operational cost
- ✔ Service boundaries must follow validated domain bounded contexts, not technical layers or convenience
- ✔ Conway's Law is a law: service boundaries that do not match team boundaries will drift toward the team structure
- ✔ Synchronous inter-service communication creates temporal coupling; async communication introduces eventual consistency
- ✔ A distributed monolith — services that cannot be deployed independently — is the most common microservices failure mode
- ✔ Start with a modular monolith; extract services only when you have validated boundaries and genuine need for independent deployability

# Guided Practice Quest

Work through the guided steps. For the communication scenario, reason about what happens in failure conditions, not just in the happy path.

# Solo Practice Quest

An online learning platform (50 engineers, 5 product teams, 2M daily users) currently runs as a modular monolith with 8 modules: User, Course, Enrollment, Progress, Payment, Notification, Search, and Analytics.

The CTO wants to move to microservices. Write an architectural analysis covering:
1. Which 2-3 modules are strong candidates for extraction and why
2. Which 2-3 modules should stay in the monolith and why
3. The organisational changes required alongside the technical decomposition
4. Three specific risks in the migration and how you would mitigate each
5. How you would verify that the extracted services are genuinely independent

# Integration

**Economics connection:** The microservices decision is an investment with a long payback period. The upfront cost is high (infrastructure, tooling, team retraining, migration), and the benefits (independent deployability, isolated scaling) take months to materialise. Many teams underestimate the fixed cost and overestimate the benefits, leading to a negative-ROI decomposition. The economic test is: does the cost of coupled deployments today (delay, coordination overhead, risk) exceed the cost of decomposition? For most systems below a certain scale and team size, the answer is no. What would your break-even analysis look like?

**Philosophy connection:** The microservices debate is a manifestation of the philosophical tension between reductionism (breaking systems into smaller, independent parts) and holism (the whole is more than the sum of its parts). Reductionism says that independent services are easier to understand individually. Holism says that the system-level behaviour — cascade failures, eventual consistency windows, distributed transactions — cannot be understood by studying services in isolation. Senior architects practice systems thinking: they hold both views simultaneously, understanding that the right level of decomposition depends on the specific properties you are trying to optimise.

# Lore Conclusion

Microservices, used wisely, give large organisations the independence they need to move fast at scale. Used prematurely or carelessly, they create systems that are expensive to operate, difficult to debug, and no more deployable independently than the monolith they replaced. The architect who can articulate precisely what problem microservices solves for their specific organisation — and what it costs — is worth ten architects who simply reach for the fashionable pattern. Know the tool. Know the tradeoff. Choose deliberately.
