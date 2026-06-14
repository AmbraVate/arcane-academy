---
id: se-lea-m4-02
school: engineering
domainId: software_engineering
tier: LEAD
moduleId: se-lea-m4
moduleTitle: "Module 4: Knowledge Transfer"
moduleGlyph: "📚"
moduleSortOrder: 4
topicSlug: technical_writing
topicTitle: "Technical Writing"
topicSortOrder: 2
lesson: writing_technical_explanations
title: "Writing Technical Explanations"
sortOrder: 2
difficulty: 4
estimatedMinutes: 35
xpReward: 70
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [teaching_programming]
integrationDomains: [linguistics, psychology]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - "Demonstrates audience analysis before writing"
    - "Applies progressive disclosure (simple first, detail available on demand)"
    - "Uses analogy effectively to bridge unfamiliar concepts to familiar ones"
    - "Writes an Architecture Decision Record (ADR) for a real-ish decision"
    - "Explains the difference between types of technical documentation (tutorial, how-to, reference, explanation)"
  keywords: [audience, progressive, disclosure, analogy, adr, diataxis, tutorial, reference, explanation, curse]
  modelAnswer: |
    Technical writing excellence starts with audience analysis:
    Who is reading? What do they already know? What do they need to decide/do?
    
    Curse of Knowledge: writers who know the subject cannot remember not knowing it.
    Solution: explicitly model the reader's knowledge state before writing.
    
    Progressive disclosure: lead with the essential, defer complexity.
    "A circuit breaker stops calls to a failing service, like a real circuit breaker
    trips when too much current flows." (30 words)
    Then: detailed explanation of states, thresholds, half-open behavior.
    
    Diátaxis framework (Procida):
    - Tutorial: learning-oriented (newcomer hand-held through a task)
    - How-to guide: task-oriented (step-by-step for a specific goal)
    - Reference: information-oriented (accurate, complete, consultable)
    - Explanation: understanding-oriented (context and background)
    
    ADR structure:
    - Title: ADR-0003: Use JWT for Authentication
    - Status: Accepted
    - Context: what situation led to this decision
    - Decision: what we decided
    - Consequences: trade-offs accepted
guidedSteps:
  - id: tw-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You are writing documentation for a new internal API. Your two audiences are:
      (A) Backend engineers who will integrate with it, and
      (B) Non-technical product managers who need to understand what it can do.
      What is the correct approach?
    inputConfig:
      options:
        - "Write one document covering all technical details for both audiences"
        - "Write separate documents for each audience with different levels of technical detail and different purposes"
        - "Write only for engineers — product managers shouldn't read API docs"
        - "Write for the most technical audience; others can skip sections they don't understand"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Write separate documents for each audience with different levels of technical detail and different purposes"]
      rejectedFeedback: "Audience analysis is foundational. Engineers need: endpoint reference, request/response schemas, error codes, authentication. Product managers need: what capabilities exist, what use cases are supported, what limitations apply — in plain English. One document serves neither audience well."
    hint: "Two different audiences have different needs, different knowledge backgrounds, and will use the documentation for different purposes."
    reflectionPrompt: "In the Diátaxis framework: engineers need how-to guides (integration steps) and reference (endpoint docs). Product managers need explanation (conceptual overview). Different document types for different purposes — not one size fits all."
  - id: tw-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      What is the "Curse of Knowledge" in technical writing and describe a concrete strategy to overcome it when writing documentation for a new system.
    inputConfig:
      minWords: 35
    markingRule:
      matchMode: CONTAINS
      accepted: [curse, knowledge, assume, novice, forget, reader, assume, test, review, fresh, eyes, audience]
      rejectedFeedback: "Curse of Knowledge (Chip Heath): experts cannot remember what it was like not to know something. They assume context, skip steps they find obvious, and use jargon without definition. Strategy: have someone who doesn't know the system follow your documentation literally and report every point of confusion. Or explicitly model the reader: 'This person has never seen a message queue. They don't know what a consumer group is.'"
    hint: "Think about why technical documentation is often confusing to newcomers even when written by experts. What's the expert writer missing?"
    reflectionPrompt: "The most practical cure: find a real person in your target audience, give them only the documentation, watch them try to use it. Every point of confusion is a documentation failure. This user-testing of docs is rare but extremely effective."
  - id: tw-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Write a one-paragraph explanation of database transactions suitable for a junior engineer who understands databases but has never used transactions. Apply progressive disclosure — start simple, add depth.
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [transaction, all, nothing, atomic, commit, rollback, fail, consistent, together, either]
      rejectedFeedback: "Good transaction explanation: 'A transaction groups multiple database operations so they either all succeed or all fail together — never partially. If you're transferring money: deduct from account A and add to account B. If anything goes wrong after the deduction but before the addition, the transaction rolls back — the deduction is undone. Without transactions, you could deduct the money but never add it. This all-or-nothing guarantee is called atomicity.'"
    hint: "Start with the simplest possible statement of what a transaction does. Then the why. Then an example. Then the technical term."
    reflectionPrompt: "Progressive disclosure: the simplest correct statement first ('all or nothing'), then why it matters (partial failures), then an example (money transfer), then the formal term (atomicity). Each layer adds depth for readers who need it; readers who got it in layer 1 stop there."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In the Diátaxis documentation framework, what is the difference between a 'tutorial' and a 'how-to guide'?"
    options:
      - "Tutorials are for beginners; how-to guides are for experts"
      - "A tutorial is learning-oriented (guides a newcomer through building something); a how-to guide is task-oriented (shows how to accomplish a specific goal)"
      - "Tutorials are interactive; how-to guides are written"
      - "They are the same thing with different names"
    correctIndex: 1
    feedback: "Diátaxis (Procida): Tutorial = newcomer-focused, learning by doing, hand-held, less concerned with the result. How-to = task-focused, assumes existing knowledge, step-by-step to achieve a specific outcome. Example: 'Getting started with Spring Boot' is a tutorial; 'How to configure HTTPS in Spring Boot' is a how-to."
  - type: MULTIPLE_CHOICE
    question: "What is the primary purpose of an Architecture Decision Record (ADR)?"
    options:
      - "To document all code changes in the system"
      - "To capture why a significant architectural decision was made, what alternatives were considered, and the accepted trade-offs"
      - "To replace design documents for new features"
      - "To track technical debt"
    correctIndex: 1
    feedback: "ADRs capture the *reasoning* behind architecture decisions — the context, alternatives considered, decision made, and accepted consequences. Without ADRs, future engineers see what the architecture is but not why it is that way, leading to costly undoing of intentional decisions."

retrieval:
  recall: "What are the four document types in the Diátaxis framework? Give an example of each for a database system."
  explain: "Explain to a senior engineer why writing a brief ADR for every significant architectural decision is worth the time investment."
  mistakeId:
    code: |
      // README for a new microservice:
      "This service uses hexagonal architecture with a domain model implementing
      DDD aggregate patterns. The primary ports are driven adapters using the 
      Command Query Responsibility Segregation pattern. The infrastructure layer
      implements the repository pattern using JPA with Spring Data."
      
      // Intended audience: new engineers joining the team
    answer: "Every sentence assumes expert knowledge: hexagonal architecture, DDD, CQRS, Command/driven adapters, repository pattern, JPA. A new engineer learns nothing actionable. Rewrite for the audience: 'This service handles order processing. It's structured so business logic (in src/main/domain) is separate from infrastructure (database, APIs). To get started: [3 concrete steps]. To understand the design: [link to ADR and design doc].'"
---

# Hook

You've designed an elegant architecture. You've written a thorough RFC. You've sent it to the team.

Three people respond. Seven don't. In the code review three months later, it's clear that only two people actually understood the proposal. The other five built around assumptions the document never addressed.

The design was good. The communication failed. Technical writing is engineering — it has quality levels, it can be done well or badly, and doing it well produces better outcomes.

> Think of a technical document you've written that you later realised was unclear. What would you change?

# Lore Introduction

The Academy's greatest artificers were not always its greatest writers. The most powerful ward enchantments in Academy history were created by a mage whose documentation was so opaque that the enchantments died with her — no one could understand the scrolls well enough to reproduce or maintain them.

*"An enchantment that cannot be taught,"* Archmage Veylan says, *"dies with its creator. A written explanation is an act of generosity to the future. Write it with the same care you give the enchantment itself."*

# Core Learning

## Concept Introduction

Technical writing is not a soft skill adjacent to engineering — it is a core engineering discipline. Clear documentation enables:
- Independent onboarding of new team members
- Preserved reasoning behind architectural decisions
- Async decision-making across time zones
- Compounded knowledge (you teach once; the document teaches forever)

**The Diátaxis framework (Procida, 2021):**

| Type | Orientation | Purpose | Example |
|------|-------------|---------|---------|
| Tutorial | Learning | Guide newcomers through building | "Get started with our API" |
| How-to | Task | Accomplish a specific goal | "Configure authentication" |
| Reference | Information | Accurate, consultable facts | "API endpoint reference" |
| Explanation | Understanding | Context, background, why | "Why we use event sourcing" |

**Architecture Decision Records (ADRs):**
Short documents capturing: context → decision → consequences. Preserved in version control alongside code.

## Why It Matters

Without good technical writing:
- Knowledge is locked in individuals (bus factor risk)
- Architectural decisions are re-debated when the original reasoning is lost
- Onboarding takes weeks instead of days
- Async work is impossible (every question requires synchronous conversation)

## Worked Examples

**ADR template:**
```markdown
 # ADR-0004: Use Kafka for Event Streaming

 ## Status
Accepted

 ## Context
We need to propagate domain events between services. 
Currently using synchronous HTTP calls which creates tight coupling.
Teams need to subscribe independently without coordinating with event producers.

 ## Decision
Use Apache Kafka for all cross-service event streaming.

 ## Consequences
+ Services are decoupled from event producers
+ Events are durable and replayable
+ New services can backfill from historical events
- Adds operational complexity (Kafka cluster to manage)
- Team needs Kafka expertise (training investment required)
- Debugging distributed event chains requires distributed tracing
```

**Progressive disclosure in README:**
```markdown
 ## What This Service Does
Processes payment for completed orders. Receives events from OrderService,
charges the customer, and emits PaymentResult events.

 ## Quick Start (for local development)
1. `docker compose up -d` — starts dependencies
2. `./gradlew bootRun` — starts the service  
3. Navigate to http://localhost:8082/actuator/health

 ## Architecture
(link to ADR and C4 model diagram)

 ## Detailed Integration Guide
(link to separate document)
```

## Common Mistakes

- **Writing for yourself** — assuming reader knowledge that isn't there.
- **All detail, no summary** — no progressive disclosure; reader drowns in complexity immediately.
- **Missing the why** — documenting what without explaining why (especially in ADRs).
- **Stale documentation** — great initial docs that become misleading as the system evolves.
- **Jargon without definition** — DDD, CQRS, hexagonal — always define or link for the target audience.

## Mental Model

Good technical writing is **building a bridge**. The writer stands on one bank (expert knowledge); the reader stands on the other (current knowledge). The document is the bridge. A bridge that only makes sense from the expert bank isn't a bridge — it's a cliff. Build from the reader's bank toward the expert knowledge.

## Mini Summary

- ✔ Diátaxis: four document types (tutorial, how-to, reference, explanation) for four needs
- ✔ ADRs capture architectural decision context and reasoning — invest in writing them
- ✔ Audience analysis first: who reads this? what do they know? what do they need?
- ✔ Curse of Knowledge: explicitly model the reader's knowledge state before writing
- ✔ Progressive disclosure: lead with the simplest correct statement; depth on demand

# Guided Practice Quest

**The Living Scrolls**

The Academy's documentation vault needs restructuring. Classify five documents by Diátaxis type, write an ADR for a recent decision, and rewrite a jargon-heavy paragraph for a junior audience.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Your team has just decided to migrate from MySQL to PostgreSQL for the main application database. Write the following:

1. **An ADR** for this decision (full template: status, context, alternatives considered, decision, consequences — good and bad)

2. **A one-page explanation** for a new engineer joining the team 6 months from now, who will see the codebase already on PostgreSQL and wonder why (Diátaxis: Explanation type)

3. **A how-to guide** for an existing engineer performing the local development setup migration (Diátaxis: How-to type, assume they have MySQL knowledge)

For each, identify: who is the intended reader, what knowledge do they bring, and what do they need to leave with?

# Integration

**Connecting to Linguistics — Speech Act Theory and Performative Documents**

Philosopher J.L. Austin's Speech Act Theory (1962) distinguishes between statements that describe (locutionary acts) and statements that perform actions (performative acts). "I promise" doesn't describe a promise — it creates one. The utterance is the act.

Technical documentation contains both. An ADR saying "We decided to use Kafka" is both descriptive (records the decision) and performative (by documenting it, the team commits to it and future teams are informed by it). The RFC saying "We propose this architecture" is a speech act inviting response and deliberation.

This framing elevates documentation: a well-written ADR doesn't just record history — it performs coordination across time. The engineer three years later who reads "we chose eventual consistency because X, Y, Z" and says "those constraints no longer apply — we can reconsider" is engaging with the original document as an act, not just as information.

Engineering leaders who understand this write documents that are designed to perform coordination, not just store information. An ADR written to justify a decision (defensive) is different from one written to share context for future decisions (collaborative).

How would you change the way you write technical documents if you thought of them as coordination acts rather than records?

# Lore Conclusion

The documentation vault is reorganised. Every enchantment has a tutorial (how to use it), a reference (what it does), an explanation (why it was designed that way), and how-to guides for the most common maintenance tasks.

*"The best enchantments in this vault,"* Archmage Veylan says, *"are not the most powerful. They are the ones that have been maintained, improved, and extended for centuries — because their creators wrote well enough that others could carry the work forward."*

Write well. It compounds.
---
