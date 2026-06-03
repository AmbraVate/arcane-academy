---
id: se-lea-m6-01
school: engineering
domainId: java
tier: LEAD
moduleId: se-lea-m6
moduleTitle: "Module 6: Lead Project"
moduleGlyph: "🏗️"
moduleSortOrder: 6
topicSlug: mini_project
topicTitle: "Mini Project"
topicSortOrder: 1
lesson: the_engineering_transformation
title: "The Engineering Transformation"
sortOrder: 1
difficulty: 8
estimatedMinutes: 480
xpReward: 750
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: low
questTypes: [solo]
prerequisites:
  - se-lea-m1-01
  - se-lea-m2-01
  - se-lea-m3-01
  - se-lea-m4-01
  - se-lea-m5-01
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Architecture proposal identifies and justifies at least one bounded context using DDD"
    - "At least one Architectural Decision Record (ADR) is written in standard format"
    - "A migration strategy from the legacy system is sequenced and risk-assessed"
    - "A mentoring plan is written for one junior engineer joining the project"
    - "CQRS or hexagonal architecture is applied to at least one module (with justification)"
    - "Stakeholder communication plan addresses at least two non-technical stakeholders"
    - "Engineering effectiveness metrics are proposed with a measurement approach"
    - "Written retrospective identifies what the lead would do differently with hindsight"
  keywords: [DDD, ADR, bounded-context, CQRS, hexagonal, migration, mentoring, stakeholder, metrics]
  modelAnswer: |
    A complete Engineering Transformation submission includes: a DDD analysis identifying
    at least one bounded context, at least one ADR in standard format, a risk-assessed
    migration roadmap, a concrete mentoring plan for a named junior engineer, an
    architectural pattern applied with justification, and a stakeholder communication
    plan. The retrospective is candid about tradeoffs and demonstrates systems-level
    thinking rather than implementation-level detail.
---

# Hook

Senior engineers write code that works under load. Lead engineers change how an entire team works.

This project is not about a single service. It is about a system: the technical system, the team system, and the organisational system that surrounds both. You will design an architecture, write the documents that guide a team, plan the migration of a legacy system, and build a mentoring programme for the engineers who will implement your vision.

You will write less code than you have in any previous project. You will make more consequential decisions than you have in any previous project.

> Before you start: who are the stakeholders? What do they care about? How do those concerns conflict?

# Lore Introduction

The Arcane Academy's Grand Council convenes.

*"The Registry System,"* says the Council Chair, *"was built twelve years ago. It works. It is also unmaintainable, untested, and understood by one person who is leaving next month."*

She gestures to a diagram of the old system — a single monolith, no tests, a database that is directly queried from the frontend.

*"We are not asking you to rewrite it. Rewrites fail. We are asking you to lead the transformation: bounded contexts, clear interfaces, a migration strategy that does not bring the Academy offline, and a team that understands what they are building and why."*

*"You have six months. You have four engineers, two of whom are junior. What is your plan?"*

# Project Brief

You are the Lead Engineer on the **Registry System Transformation** — a plan to modernise the Arcane Academy's core administrative system without a big-bang rewrite.

---

## Context: The Legacy System

```
Legacy Registry (12 years old)
├── Monolithic Java application
├── Direct database queries from UI layer (no service layer)
├── No tests
├── No CI/CD
├── One database: 40+ tables, no documented schema
├── Known issues: slow enrolment queries, data integrity bugs in reporting
└── Team: 1 senior (leaving), 2 juniors (6 months experience)
```

---

## Deliverables

### 1. DDD Analysis (Required)

Identify the bounded contexts within the Registry System. For each context:

- Name and describe it
- Identify the key domain entities and aggregates
- Define the ubiquitous language (key terms that must be used consistently)
- Describe how it communicates with other contexts (shared kernel, anti-corruption layer, etc.)

Minimum: **3 bounded contexts** documented.

---

### 2. Architectural Decision Records (Required)

Write **at least 2 ADRs** in the following format:

```
# ADR-[number]: [Short Title]

## Status
[Proposed | Accepted | Deprecated]

## Context
[What situation or problem prompted this decision?]

## Decision
[What was decided?]

## Consequences
[What are the positive and negative consequences of this decision?]
```

Suggested decisions to document:
- Strangler Fig vs. Big-Bang Rewrite
- Event-driven vs. synchronous inter-context communication
- Choice of database migration strategy (in-place vs. side-by-side)
- CQRS adoption scope

---

### 3. Architecture Proposal (Required)

Produce an architecture diagram (any tool: draw.io, Mermaid, hand-drawn and photographed) and a written description covering:

- Target architecture (after transformation)
- How at least one bounded context implements **hexagonal architecture** or **CQRS**
- How contexts communicate (events, APIs, shared database — and why)
- What the system looks like at Month 3 (mid-migration), not just the end state

---

### 4. Migration Roadmap (Required)

Produce a sequenced plan for migrating from the legacy system. The plan must:

- Use a **Strangler Fig** or **Branch by Abstraction** pattern (name it and justify it)
- Identify at least **3 migration risks** with likelihood and impact ratings
- Define what "done" looks like for each phase (not just "migrate the module")
- Specify how the team will know if a phase has gone wrong (failure signals)

---

### 5. Mentoring Plan (Required)

Write a mentoring plan for **one junior engineer** joining the project. Include:

- Their starting point (assume 6 months experience, strong on syntax, weak on design)
- 3-month learning goals (specific and measurable)
- Weekly 1:1 structure (topics, format, how you give feedback)
- How you will involve them in architectural decisions without overwhelming them
- How you will know they are progressing

---

### 6. Stakeholder Communication Plan (Required)

Identify at least **2 non-technical stakeholders** (e.g., Academy Director, Finance Officer, Head of Admissions). For each:

- What they care about (not in technical terms)
- What risks of the transformation concern them most
- How you will communicate progress (frequency, format, level of detail)
- What you will never say to them (technical jargon that obscures rather than informs)

---

### 7. Engineering Effectiveness Metrics (Required)

Define **3–5 metrics** you will use to measure whether the transformation is improving engineering effectiveness. For each metric:

- Name and definition
- How it is measured (tooling, manual process, frequency)
- Target value (or direction of improvement)
- Why it matters for this transformation specifically

Suggested metrics: DORA metrics (deployment frequency, lead time, MTTR, change failure rate), test coverage, onboarding time for new engineers.

---

## Acceptance Criteria

- [ ] At least 3 bounded contexts are documented with ubiquitous language
- [ ] At least 2 ADRs are written in standard format
- [ ] Architecture diagram shows target state and Month 3 state
- [ ] At least one module uses hexagonal architecture or CQRS (with justification)
- [ ] Migration roadmap names a Strangler Fig or Branch by Abstraction pattern
- [ ] At least 3 migration risks are identified with likelihood + impact
- [ ] Mentoring plan includes specific, measurable 3-month goals
- [ ] At least 2 non-technical stakeholders are addressed in the communication plan
- [ ] At least 3 effectiveness metrics are defined with measurement approach

---

## Retrospective Prompt

After completing all deliverables, write a **retrospective** (400–600 words) addressing:

1. Which decision was hardest to make? What made it hard?
2. Where did you feel most out of your depth? How did you work through it?
3. What would you do differently if you started this engagement again?
4. What does "lead engineering" mean to you now, compared to before this project?

---

# Integration

**Connecting to Psychology — Change Management and the Satir Change Model**

Every technical transformation is also a human transformation. The Satir Change Model describes how teams respond to change: from a stable old status quo, through a period of chaos when the old way no longer works but the new way is not yet established, to a new status quo that (if the change succeeds) is better than the original.

Lead engineers who ignore this model create transformations that succeed technically and fail organisationally — the new architecture is deployed, but the team does not understand it, does not trust it, and cannot maintain it. The technical deliverables in this project are not the end goal. They are instruments of change. Their value depends entirely on whether the humans around them can use them well.

What does this suggest about the most important skill a Lead engineer needs that cannot be learned from a textbook?

# Lore Conclusion

The Grand Council reconvenes six months later.

You present the architecture. You walk through the ADRs. You show the metrics — deployment frequency up, lead time down, two junior engineers who can now explain the domain model without prompting.

The Council Chair nods slowly.

*"The system is not done. It will never be done. But it is moving in the right direction, and the team knows why. That is what leadership produces — not a finished product, but a team with direction and momentum."*

The Lead rune is inscribed.

---
