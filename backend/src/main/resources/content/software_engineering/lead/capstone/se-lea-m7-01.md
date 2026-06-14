---
id: se-lea-m7-01
school: engineering
domainId: software_engineering
tier: LEAD
moduleId: se-lea-m7
moduleTitle: "Capstone Quest"
moduleGlyph: "⚔️"
moduleSortOrder: 99
topicSlug: capstone
topicTitle: "Capstone Quest"
topicSortOrder: 1
lesson: systems_architect
title: "Systems Architect"
sortOrder: 1
difficulty: 10
estimatedMinutes: 1440
xpReward: 2000
practiceType: NONE
questType: MASTERY
retrievalWeight: low
questTypes: [solo]
prerequisites:
  - se-app-m7-01
  - se-jun-m9-01
  - se-sen-m9-01
  - se-lea-m6-01
integrationDomains: [mathematics, psychology, data_science, economics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "A production-ready system is implemented, deployed, and accessible via a public URL or documented deployment"
    - "Architecture document covers system design, component responsibilities, and communication patterns"
    - "At least one concurrency or scalability decision is implemented and load-tested"
    - "Security is addressed: authentication, authorisation, and at least one OWASP concern mitigated"
    - "Testing covers unit, integration, and at least one end-to-end scenario"
    - "Deployment pipeline is automated (CI/CD) and documented"
    - "Observability is present: structured logging, at least one metric, health endpoint"
    - "Tradeoff analysis document identifies at least three architectural decisions with alternatives considered"
    - "Written rationale demonstrates systems-level thinking — not just what was built, but why"
    - "The submission could be handed to a new engineer who could understand and extend it"
  keywords: [architecture, deployment, concurrency, security, testing, CI/CD, observability, tradeoff, rationale, production]
  modelAnswer: |
    A Capstone submission is a complete, deployable system solving a real-world problem.
    It includes: a written architecture document, working CI/CD pipeline, security controls,
    load testing results, structured observability, and a tradeoff analysis naming at least
    three decisions with alternatives considered. The written rationale demonstrates that
    every major decision was conscious — the engineer understood the alternatives and chose
    deliberately. The system is not perfect; the rationale acknowledges its limitations
    and identifies what a production version would add.
---

# Hook

Four tiers. Hundreds of lessons. Every concept, every pattern, every principle — they all led here.

Not to a single lesson. Not to a single exercise. To a system.

A system that solves a real problem. That handles real load. That fails gracefully. That a real team could maintain. That you can defend to a technical panel and explain to a non-technical stakeholder.

This is the Systems Architect Capstone. It is the hardest thing you will build in this Academy. It is also the most honest test of what you have learned — because a system does not lie. Either it works, or it does not. Either the architecture holds, or it collapses. Either the rationale is clear, or it is not.

> What problem will you solve? Choose something you care about. You will spend a significant amount of time on this.

# Lore Introduction

The Arcane Academy's Hall of Masters falls silent as you enter.

Every Archmage who has ever been awarded the title of Systems Architect built something here — a system that proved they understood not just the craft of programming, but the discipline of engineering.

Archmage Veylan speaks from across the hall:

*"We do not assess what you built. We assess why. Why this architecture? Why this tradeoff? Why this deployment strategy? A junior engineer builds what they are told. A senior engineer builds it correctly. A lead engineer knows which questions to ask before a line is written."*

*"You have one final task: prove that you ask the right questions."*

# Project Brief

Build and deploy a **production-ready system** that solves a real-world problem of your choosing.

---

## Scope Requirements

Your system must be non-trivial. It must have:

- At least **2 services** or **2 significant modules** (if monolith: clearly separated bounded contexts)
- At least **one persistent data store**
- At least **one async or concurrent operation**
- At least **one external integration** (another API, an event stream, a file system, email — anything outside your own code)
- A **deployable artefact** (Docker container, JAR with deployment script, cloud-deployed service)

---

## Mandatory Deliverables

### 1. Architecture Document

A written document (or diagram + annotations) covering:

- **System overview**: what it does, who uses it, what problem it solves
- **Component diagram**: every major component, its responsibility, and how it communicates with others
- **Data model**: key entities and their relationships
- **Sequence diagram**: at least one non-trivial request flow from client to data store and back
- **Deployment diagram**: where each component runs and how it is accessed

---

### 2. Implementation

Working, deployable code. Assessed on:

| Criterion | Detail |
|---|---|
| **Correctness** | Core features work end-to-end |
| **Code quality** | Clean, readable, consistent — no commented-out blocks, no magic numbers |
| **Error handling** | Failures produce useful responses, not stack traces |
| **Separation of concerns** | No business logic in controllers; no I/O in domain objects |

---

### 3. Testing

| Test type | Minimum requirement |
|---|---|
| Unit tests | Cover all service layer logic; at least 20 meaningful assertions |
| Integration tests | At least 1 full-stack test (real DB, real HTTP call) |
| End-to-end test | At least 1 scenario exercising the system as a user would |
| Load test | At least one endpoint tested under concurrency; results documented |

---

### 4. Deployment Pipeline

A CI/CD pipeline that, on every push to `main`:

- Runs all tests
- Builds the deployable artefact
- Deploys to a staging or production environment (or documents what manual step remains and why)

Document the pipeline. Explain every stage.

---

### 5. Concurrency Strategy

Identify the part of your system with the highest concurrency risk. Document:

- What shared state exists (if any)
- What concurrency primitive or pattern protects it
- What happens if two requests arrive simultaneously for the same resource
- Load test results demonstrating thread safety under concurrent load

---

### 6. Security Review

For each of the following OWASP Top 10 categories, state whether it applies to your system and what mitigation is in place (or why it does not apply):

- Injection (SQL, command, etc.)
- Broken Authentication
- Sensitive Data Exposure
- Broken Access Control
- Security Misconfiguration

Document any known remaining risks.

---

### 7. Scalability Strategy

Answer the following:

1. At what load does your current architecture become the bottleneck? (Estimate, with reasoning.)
2. What is the first component you would scale? How?
3. What would you add to handle 10× current load?
4. What would require a fundamental re-architecture at 100× current load?

---

### 8. Tradeoff Analysis

Document at least **3 architectural decisions** using this format:

```
Decision: [What you decided]
Alternatives considered: [What else you evaluated]
Why you chose this: [Reasoning — not just "it was easier"]
Consequences: [What you gave up; what this makes harder in future]
```

---

### 9. Written Rationale

A **500–800 word** document answering:

1. Why did you choose this problem?
2. What is the most important architectural decision you made? Why?
3. What would a production version of this system add that yours does not have?
4. What did you learn that surprised you?
5. If a new engineer joined your team tomorrow, what would you most want them to understand about this system before touching it?

---

## Acceptance Criteria

- [ ] System is deployed and accessible (URL or documented deployment steps)
- [ ] Architecture document submitted: overview, component diagram, sequence diagram, deployment diagram
- [ ] At least 20 meaningful unit test assertions pass
- [ ] At least 1 integration test and 1 end-to-end test pass
- [ ] Load test results submitted (concurrent requests, no data corruption)
- [ ] CI/CD pipeline documented and working (or documented with known gap)
- [ ] OWASP review covers all 5 categories
- [ ] Scalability strategy submitted
- [ ] At least 3 tradeoff analysis entries submitted
- [ ] Written rationale submitted (500–800 words)
- [ ] A new engineer could read the documentation and understand the system's purpose, structure, and constraints

---

## What is Not Assessed

- **Perfect code.** Production systems are never perfect. Conscious imperfection with documented rationale is better than hidden imperfection.
- **Novel technology.** Choosing boring, proven tools and explaining why is a stronger signal than using the newest framework because it seemed interesting.
- **Scale.** A system handling 10 users with a sound architecture is better than a system claiming to handle 10,000 users with a brittle one.

---

# Integration

**Connecting to Economics — Technical Debt as Compound Interest**

Ward Cunningham coined the term "technical debt" to describe the cost of shortcuts in code — not because shortcuts are always wrong, but because they accrue interest. Like financial debt, technical debt is sometimes worth taking on deliberately (to meet a deadline, to test an idea) and sometimes accumulated accidentally (through ignorance or pressure).

The interest on technical debt is paid every time a developer touches the affected code: they move slower, they introduce more bugs, they spend time understanding rather than building. At high enough levels, technical debt becomes the primary driver of development cost — not new features, but the maintenance cost of past shortcuts.

Your Capstone is a chance to make deliberate debt decisions. Your tradeoff analysis is the ledger. Every "known limitation" you document is a debt you have acknowledged — which means it can be managed. The debts you leave undocumented are the ones that will surprise the next engineer.

What does this suggest about the relationship between documentation and technical risk?

# Lore Conclusion

The Hall of Masters is quiet.

You have laid out the architecture. You have walked through the tradeoffs. You have explained what you built, why, and what you would change.

Archmage Veylan speaks:

*"A Systems Architect does not build alone. They create systems that others can understand, extend, and fix without them. The measure of your work is not the elegance of your code — it is the clarity of your thinking, captured in a form that survives your absence."*

She places the final rune on the table.

*"You have earned this."*

The Hall of Masters welcomes a new Systems Architect.

---
