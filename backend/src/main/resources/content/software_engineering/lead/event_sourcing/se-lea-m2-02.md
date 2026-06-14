---
id: se-lea-m2-02
school: engineering
domainId: software_engineering
tier: LEAD
moduleId: se-lea-m2
moduleTitle: "Module 2: Advanced Architecture"
moduleGlyph: "🏛️"
moduleSortOrder: 2
topicSlug: event_sourcing
topicTitle: "Event Sourcing"
topicSortOrder: 2
lesson: event-sourcing
title: "Event Sourcing"
sortOrder: 2
difficulty: 4
estimatedMinutes: 42
xpReward: 180
practiceType: NONE
questType: MASTERY
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, philosophy, economics]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - Explains the append-only event log as the primary source of truth with precision, distinguishing it from event-driven architectures that do not use event sourcing
    - Describes projections and read models as derived views over the event log, not as the source of truth
    - Explains snapshots correctly as an optimisation to reduce event replay cost, including when they are and are not needed
    - Identifies temporal queries as a unique capability of event sourcing — reconstructing system state at any point in time
    - Articulates specific operational complexities of event sourcing and the contexts in which it is not appropriate
  keywords:
    - append-only log
    - event store
    - projection
    - read model
    - snapshot
    - temporal query
    - event replay
    - event schema evolution
    - eventual consistency
    - command handler
    - aggregate reconstruction
    - upcasting
    - idempotency
    - compensating event
    - event versioning
  modelAnswer: |
    Event sourcing is an architectural pattern in which the state of an application is derived from an append-only log of events, rather than stored as current state in a database. The event log is the authoritative source of truth; the application's current state is a derived view, reconstructed by replaying the event sequence. This inverts the typical persistence model and produces a system with unusual capabilities — and unusual operational challenges.

    The fundamental unit in event sourcing is the domain event: an immutable record of something that happened in the domain. Events are named in the past tense using domain vocabulary: OrderPlaced, PaymentAuthorised, ItemShipped. The event log is append-only — events are never modified or deleted. This immutability is not just a technical constraint; it reflects the business reality that history cannot be changed, only extended. In financial systems, insurance, and regulatory contexts, this property is enormously valuable.

    Projections are functions that fold over the event stream to produce a specific view of the current state. Multiple projections can exist over the same event log, each optimised for a different read concern. A user-facing Order Detail projection might include all fields needed to render an order page; an analytics projection might track aggregate statistics; a shipping projection might track only shipment-relevant state. Projections are eventually consistent — they lag behind the event log by whatever the processing latency is. This is an architectural property of event sourcing that must be understood and managed.

    Snapshots are an optimisation that reduces aggregate reconstruction cost. Instead of replaying all events from the beginning to reconstruct an aggregate, a snapshot captures the aggregate state at a specific event sequence number. Reconstruction then only requires replaying events after the snapshot. Snapshots are necessary when aggregates accumulate long event histories (thousands of events), but they add operational complexity: they must be kept consistent with the event log, they must be versioned alongside event schema changes, and they are an optimisation that should not be introduced prematurely.

    Temporal queries — the ability to reconstruct system state at any arbitrary past point — are the unique capability that event sourcing provides and other persistence models cannot replicate without significant additional engineering. In a financial system, the ability to ask "what was the customer's account balance on the 15th of March?" and receive an accurate answer is a business requirement, not a nice-to-have. Event sourcing provides this natively.

    Event sourcing should not be adopted universally. It adds significant operational complexity: event schema evolution requires versioning and upcasting strategies, projection rebuilds can be slow and resource-intensive, debugging requires understanding state as a function of history rather than inspecting current state, and onboarding engineers requires understanding the event-first mental model. For systems where temporal queries are not required, where audit trails are not a regulatory requirement, and where current-state persistence is sufficient, event sourcing is overengineering.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      A financial services company is considering event sourcing for their transaction processing system. A sceptical architect argues that "we can achieve the same audit trail by logging all database changes to a separate audit table." Evaluate this argument — what are the genuine similarities and differences between change data capture / audit tables and event sourcing?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [audit, event, source, truth, reconstruct, intent, technical, temporal, query, projection, business]
      rejectedFeedback: "The audit table approach records technical changes (column-level deltas) but not business intent. Event sourcing records business intent ('PaymentAuthorised') not technical change. The differences are: events are business-meaningful and queryable; projections allow multiple read models; temporal queries over business events are possible; the model can be replayed to test new projections. However, the audit table approach is simpler and may be sufficient for pure regulatory compliance without the need for temporal business queries."
    hint: "What does an audit table record that an event log does not? What does an event log record that an audit table cannot?"
    reflectionPrompt: "Event sourcing is not just about audit trails — it is about making business intent the primary persistence concern rather than technical state. That distinction only matters if you need temporal queries over business concepts."

  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Your event-sourced system has been running for two years. A critical event, OrderShipped, needs a new field: the estimated delivery date. Events already stored in the log do not have this field. How do you handle schema evolution for this event, and what is the upcasting strategy?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [upcasting, version, schema, evolve, transform, old, new, default, migration, backward]
      rejectedFeedback: "Event schema evolution requires a versioning strategy. Upcasting transforms old events to the new schema at read time: when the event log is replayed and a v1 OrderShipped event is encountered, an upcaster transforms it to a v2 event with a null or default estimated delivery date. The source log is never modified — the transformation happens in the projection pipeline. This preserves the immutability of the log while allowing the system to evolve."
    hint: "The event log is immutable — you cannot change existing events. So the transformation must happen when the events are read. What transforms them?"
    reflectionPrompt: "Upcasting is event sourcing's equivalent of database migration — it transforms old data to match new expectations. The discipline of never modifying the event log means transformations always happen at the projection layer."

  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A startup is building a simple task management application and a senior engineer proposes using event sourcing because "it gives us a complete audit trail and is more scalable." Evaluate this proposal. Is event sourcing appropriate for this context? What are the tradeoffs?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [complexity, appropriate, tradeoff, simple, overhead, not needed, operational, scale, audit, context]
      rejectedFeedback: "For a simple task management application, event sourcing is likely overengineering. The operational complexity — event schema versioning, projection rebuilds, debugging state-as-history, onboarding overhead — is not justified by the benefits unless temporal queries or complex audit requirements are genuine business needs. The 'more scalable' claim is also questionable; event sourcing adds complexity that can impede performance at small scale. A simple CRUD application is better served by a simple persistence model, with event sourcing adopted when specific requirements make its benefits concrete."
    hint: "What specific business requirements would make event sourcing's benefits concrete for a task management application? Are those requirements present?"
    reflectionPrompt: "The question is not 'does event sourcing have benefits?' but 'do those specific benefits justify this specific operational complexity in this specific context?' Architecture decisions require contextual evaluation, not category endorsement."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In an event-sourced system, the current state of an aggregate is obtained by:"
    options:
      - "Querying the latest record in the database for that aggregate"
      - "Calling a dedicated API that maintains a cached current-state representation"
      - "Replaying all events for that aggregate from the event log (or from the latest snapshot) and applying them to an initial empty state"
      - "Reading the state from the read model projection that is kept current by event handlers"
    correctIndex: 2
    feedback: "In event sourcing, the event log is the source of truth. The current state of an aggregate is derived by replaying events — loading all events for the aggregate (or events since the last snapshot) and applying each event to reconstruct the aggregate's state. This is distinct from read models, which are projections derived from events for query purposes. The aggregate state is reconstructed in the write model, not read from a projection."

  - type: MULTIPLE_CHOICE
    question: "A snapshot in event sourcing is best described as:"
    options:
      - "A backup copy of the database at a specific point in time"
      - "A periodic summary of aggregate state captured to reduce event replay cost during aggregate reconstruction"
      - "A projection that represents the current state of all aggregates for query purposes"
      - "A read-only copy of the event log used for analytics"
    correctIndex: 1
    feedback: "A snapshot captures the state of a specific aggregate at a specific event sequence number, allowing reconstruction to start from the snapshot point rather than from the first event. Snapshots are an optimisation for aggregates with long event histories. They do not replace the event log as the source of truth — the log must still be the authority, with snapshots as a performance optimisation on top of it."
retrieval:
  recall: "What is the difference between an event-driven architecture and an event-sourced architecture? A system can be event-driven without being event-sourced — explain how, and why the distinction matters."
  explain: "Design the event sourcing model for a bank account domain. Identify the events, describe how current balance is projected, explain how a temporal query for 'what was the balance on January 1st?' would work, and identify one regulatory benefit and one operational challenge of this approach."
  mistakeId:
    code: |
      An architect implements event sourcing by saving domain events to the database alongside a 'current_state' table that is kept up-to-date by event handlers. They argue that the current_state table is the source of truth and the events are the audit log.
    answer: "This inverts the fundamental contract of event sourcing. If the current_state table is the source of truth, the system is not event-sourced — it is a conventional system with an audit log. The properties of event sourcing (temporal queries, projection rebuilding, single source of truth) require the event log to be the source of truth. If the two diverge (a bug in an event handler, a direct database update), the events become an inaccurate audit log and the current_state table becomes the de facto reality. True event sourcing means current state is always derived from the event log, which may be cached but is never authoritative. The architect's design has the operational cost of event sourcing without its architectural benefits."
---

# Hook

Your production database has just been corrupted by a bug that was introduced three months ago. The bug has been silently writing incorrect state for 90 days. In a conventional system, you have a problem with no clean solution: the corrupted state is your only truth, the bug affected millions of records, and the original correct state is gone. In an event-sourced system, you have a solution: revert the application code to the pre-bug version, replay the event log, and rebuild the read models from the corrected history. The events are immutable. The history cannot be corrupted. This is not a thought experiment — it is the reason organisations with complex financial, legal, or regulatory requirements adopt event sourcing. The question is whether the complexity it introduces is justified by the requirements you actually have.

# Lore Introduction

The scribes of the ancient academies did not write history by updating a single stone tablet to show the current state of the realm — they maintained a scroll, appending a new line for every significant event in the kingdom's life. The current state of the realm was always derivable from the scroll, but the scroll itself was the truth: immutable, complete, and auditable. Modern software systems discovered this same insight independently: the append-only log of events, not a snapshot of current state, is the most powerful representation of a system's history. Event sourcing is the formalisation of that insight, applied to the design of complex, auditable software systems.

# Core Learning

## Concept Introduction

Event sourcing replaces the conventional persistence model (persist current state, overwrite on change) with an append-only log of events (persist what happened, derive current state from the log). The consequences of this architectural choice pervade the system design: every state change produces an event, events are immutable once written, current state is always a derived view, and the event log is the authoritative record of everything that has ever happened.

A projection is a function from event stream to some useful view of state. Projections are not the source of truth — they are derived and can be rebuilt. A system can have many projections over the same event stream, each optimised for different query patterns. In practice, most event-sourced systems maintain a small number of persistent projections as materialised views, updated asynchronously as events are written. The lag between event writing and projection update is the eventual consistency window.

Temporal queries exploit the event log's completeness to answer questions about past states. Given a complete event log, the system can reconstruct the exact state of any aggregate at any past timestamp by replaying all events up to that timestamp. This is a fundamentally different capability from conventional systems, which typically require custom audit tables or CDC infrastructure to answer such questions — and those approaches answer questions about data changes, not business events.

Event schema evolution is one of the most practically challenging aspects of operating an event-sourced system at scale. Events written to the log are immutable, but the schema of those events must evolve as the system's requirements change. The standard solution is event versioning with upcasting: old events are transformed to the current schema at read time by an upcasting pipeline. This keeps the write path clean (always write the current schema) while preserving backward compatibility with old events.

## Why It Matters

Event sourcing is appropriate when one or more of the following conditions apply: the business has genuine temporal query requirements, there is a regulatory requirement for an immutable audit trail at the business event level (not just technical change data), the system needs to support complex projections that may need to be rebuilt, or the domain model is so rich in events that events are the most natural representation of system behaviour. It is inappropriate for CRUD-heavy applications where none of these conditions apply, because it adds significant operational complexity without corresponding benefit.

## Worked Examples

**The Financial Audit.** A trading platform uses event sourcing for all trade execution events. A regulator requests the complete history of a specific account's trading activity on a specific date, including the order book state at the time of each trade. The system replays the account's event stream up to the requested date and produces a complete reconstruction. In a conventional system, this would require a complex audit table design and would likely be impossible to rebuild retroactively.

**The Bug Recovery.** A pricing engine introduces a bug that applies incorrect discounts for six weeks. In an event-sourced system, the fix involves: correcting the projection logic, rebuilding the affected projections from the unchanged event log, and restoring the correct prices. The events (orders placed, discounts applied) are not affected by the projection bug because they record business intent, not computed results.

**The Premature Adoption.** A team adopts event sourcing for a simple CRM application because a conference talk made it sound compelling. After 18 months, the team has accumulated eight versions of CustomerUpdated events, a complex upcasting pipeline, projection rebuild times of 40 minutes, and onboarding documentation that takes new engineers a week to absorb. None of the temporal query capabilities have ever been used. The team is maintaining a complex architecture for benefits they never needed.

## Common Mistakes

**Event sourcing as audit logging.** Treating the event log as an audit trail while treating a separate state table as the source of truth. Produces the cost of event sourcing without its benefits.

**Events as database change records.** Recording technical changes (field X changed from A to B) rather than business events (CustomerAddressUpdated). Loses the domain intent that gives event sourcing its expressive power.

**Overly large aggregates in event-sourced systems.** Aggregates with thousands of events and no snapshotting strategy. Reconstruction becomes prohibitively slow.

**Schema evolution without strategy.** Adding fields to events without a versioning and upcasting strategy. Old events become incompatible with new projections.

**Universal adoption.** Using event sourcing for every part of a system regardless of requirements. User session data, configuration, and content management are typically better served by conventional persistence.

**Synchronous projection updates.** Blocking the write path until all projections are updated, sacrificing the performance benefits of asynchronous projection processing.

## Mental Model

An event-sourced system is like a financial ledger rather than a balance sheet. A balance sheet shows the current state: total assets, total liabilities. A ledger shows the complete history of every transaction, from which the current balance is computable but which also preserves every intermediate state. The ledger is the truth; the balance is derived. Event sourcing applies this model to software: the event log is the ledger, and all current state views are the derived balances. When the balance seems wrong, you audit the ledger, not update the balance.

## Mini Summary

- The event log is the source of truth; current state and read models are derived views that can always be rebuilt from the log.
- Events are immutable, named in the past tense using domain vocabulary, and represent business intent rather than technical state changes.
- Projections are eventually consistent derived views optimised for specific query patterns; multiple projections can coexist over the same event stream.
- Snapshots are an optimisation for aggregate reconstruction performance, not the source of truth.
- Temporal queries — reconstructing state at any past point — are a unique native capability of event sourcing.
- Event sourcing is appropriate when temporal queries, complex audit requirements, or projection rebuilding are genuine requirements; it is overengineering when they are not.

# Guided Practice Quest

Work through the three guided steps above with detailed analytical responses. Demonstrate understanding of the architectural tradeoffs, not just the pattern vocabulary.

# Solo Practice Quest

Design an event-sourced architecture for a securities trading platform. The platform must: maintain a real-time view of each trader's current portfolio, support regulatory queries for "what was this account's position at 10:30 AM on any given trading day," handle corrections to erroneous trades without mutating existing events, and integrate with downstream risk management systems that need near-real-time position updates. For each requirement, explain how event sourcing addresses it, what the projection design would be, and what the operational challenges are. Then identify one aspect of the trading domain where you would NOT use event sourcing and explain why.

# Integration

Event sourcing connects to several deep ideas in mathematics and philosophy. The concept of a pure function — deterministic, without side effects — maps directly to the aggregate: given the same sequence of events, the same state is always reconstructed. This property enables testing, debugging, and reasoning about system behaviour in ways that mutable state systems cannot achieve. The event stream is an algebraic structure: a sequence, with well-defined composition and replay semantics.

From philosophy of time and causality, event sourcing reflects a particular metaphysics: events are the fundamental reality, and states are derived appearances. This is not unlike Heraclitus's observation that the river is not a thing but a process — the "current state" of a system is not the fundamental reality, but a temporary cross-section of a stream of events. This philosophical position has practical consequences: it makes the system more honest about the temporal nature of data, and more honest about the fact that "current state" is always an approximation of a moving reality.

From economics, the concept of information as a non-rival, non-excludable good applies to the event log: the same event can serve multiple projections simultaneously without being consumed. This is unlike conventional database rows, which can only be efficiently read for one schema at a time. The event log's information can be exploited repeatedly in different ways — analytics, operational views, machine learning training data — creating value from the same stored information that conventional persistence architectures cannot match.

# Lore Conclusion

Event sourcing is one of the most powerful tools in the lead architect's repertoire — and one of the most frequently misapplied. Its power comes from the completeness of the event log: the ability to reconstruct any past state, rebuild any projection, and audit any decision. Its cost comes from the operational complexity of schema evolution, projection management, and the mental model shift required to reason about state as a function of history. The architect who recommends event sourcing should be able to answer concretely: what temporal queries will this system need to support, what audit requirements make an immutable log a genuine requirement, and what specific projection capabilities justify the added complexity? If the answers are compelling, event sourcing is a profound architectural investment. If they are not, a simpler persistence model will serve better.
---
