---
id: de-lead-m2-01
school: DATA_ENGINEERING
domainId: data_engineering
tier: LEAD
moduleId: de-lead-m2
moduleTitle: "Module 2: Data Architecture Leadership"
moduleGlyph: "🏗️"
moduleSortOrder: 2
topicSlug: enterprise_modelling
topicTitle: "Enterprise Modelling"
topicSortOrder: 1
lesson: 1
title: "Enterprise Modelling: The Map of Your Data World"
sortOrder: 1
difficulty: 5
estimatedMinutes: 40
xpReward: 100
practiceType: GUIDED_AND_SOLO
questType: RETRIEVAL_CHALLENGE
retrievalWeight: 0.6
questTypes:
  - guided
  - solo
  - retrieval
prerequisites:
  - de-lead-m1-04
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the purpose of an enterprise data model and how it differs from a physical schema"
    - "Describes canonical data models and their role in cross-system integration"
    - "Identifies the bounded context pattern from DDD and its application to data modelling"
    - "Explains master data management and why it is strategically important"
  keywords:
    - enterprise data model
    - canonical model
    - master data management
    - bounded context
    - domain-driven design
    - data dictionary
    - conceptual model
  modelAnswer: |
    An enterprise data model is a conceptual representation of the key entities in an organisation, their attributes, and their relationships — independent of any physical implementation. It provides a shared vocabulary and semantic consistency across systems and teams. It differs from a physical schema which is tied to a specific database implementation.
    A canonical data model is a neutral, agreed-upon representation for a business entity (e.g. Customer, Product, Event) used for integration between systems. When System A sends a Customer record to System B, both use the canonical model — neither needs to know the other's internal representation. This decouples systems.
    Bounded contexts (from Domain-Driven Design) recognise that large organisations cannot maintain one consistent model for every concept everywhere — "Customer" means different things to sales (prospect + account), finance (billing entity), and support (ticket owner). A bounded context defines the boundary within which a model is consistent. Context maps show how bounded contexts integrate.
    Master data management (MDM) creates a single authoritative reference for core business entities — Customer, Product, Location — that is shared across systems. Without MDM, each system has its own definition of Customer with different IDs, different attributes, and different quality. Reports that join across systems produce inconsistencies. MDM provides the golden record.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "The Consortium's sales system calls a learner 'Account', the platform calls them 'User', the support system calls them 'Customer', and the billing system calls them 'Subscriber'. All are the same person. What enterprise architecture problem does this represent?"
    options:
      - "Technical debt — the systems need refactoring to use the same programming language"
      - "Semantic fragmentation — no canonical model or shared vocabulary for a core entity"
      - "Database normalisation failure — each system should share one users table"
      - "API versioning conflict — the systems use different API protocols"
    correctIndex: 1
    explanation: "This is semantic fragmentation — four systems represent the same business entity with different names, different identifiers, and different attributes. Cross-system reporting requires complex entity resolution (is Account 'A-123' the same as User 'U-456'?). The remedy is a canonical 'Learner' entity in a conceptual enterprise model, with each system's context-specific view mapped to it. MDM provides the golden record with a universal identifier."
  - type: FILL_BLANK
    question: "A ___ model documents the key business entities, their relationships, and their definitions at a conceptual level — independent of any database or technology implementation."
    answer: "conceptual (or enterprise data)"
    explanation: "A conceptual model captures what the business needs to know — entities, relationships, cardinality — without specifying how it will be stored. It is technology-agnostic and audience-agnostic (understandable by business stakeholders, not just engineers). The physical model (table design, column types, indexes) is derived from the conceptual model but adds implementation specifics."
  - type: SHORT_TEXT
    question: "What is a context map in Domain-Driven Design and why does a Lead Data Engineer need to understand it?"
    modelAnswer: "A context map diagrams the relationships between bounded contexts — how different sub-systems integrate and where semantic translation occurs. Relationship types include: Shared Kernel (two contexts share a common model segment), Customer-Supplier (one context serves another), Conformist (downstream adopts upstream's model), Anti-corruption Layer (downstream translates upstream's model to protect its own consistency). A Lead needs context maps to: understand where data integration complexity lives, identify where semantic conflicts arise (the same field meaning different things in different systems), design integration patterns that respect bounded context boundaries, and communicate architectural boundaries to engineering teams."
microCheckpoint:
  question: "What is master data management and what problem does it solve?"
  answer: "Master data management (MDM) creates a single authoritative record (golden record) for core business entities — Customer, Product, Location — shared across all systems. Without MDM, each system has its own definition and ID for the same entity, making cross-system reporting inconsistent. MDM solves the fragmentation problem by providing a universal identifier and agreed canonical attributes for each entity."
retrieval:
  recall: "What are the three levels of data modelling (conceptual, logical, physical) and what does each represent?"
  explain: "Explain the bounded context concept and why a single enterprise-wide canonical model is often impractical in large organisations."
  mistakeId: "enterprise-model-physical-first"
---

# Four Names, One Person

The quarterly retention report failed to reconcile. Finance said 12,000 paying learners. Product said 14,500 active users. Support said 9,800 open customer accounts. "They're all describing the same people," the Lead Data Engineer said, "with four different definitions, four different IDs, and four different data quality levels." The CDO looked at the report. "We need an enterprise data model. We need to agree what a learner is before we can count them."

# Three Levels of Data Modelling

```
CONCEPTUAL MODEL (business view)
  What entities exist? What are their relationships?
  Audience: business stakeholders, architects
  Example: Learner ─enrolls in─ Course ─contains─ Lesson
  Tech-independent; no column names, no types

LOGICAL MODEL (data view)
  How are entities and attributes structured?
  Audience: data architects, senior engineers
  Example: Learner(learner_id PK, email, tier, enrolled_at)
           Enrollment(learner_id FK, course_id FK, enrolled_at)
  Tech-independent; column names and types, no indexes

PHYSICAL MODEL (implementation view)
  How is data stored in a specific technology?
  Audience: data engineers, DBAs
  Example: CREATE TABLE learners (id UUID PRIMARY KEY, ...)
           WITH TABLESPACE fast_nvme PARTITION BY RANGE (enrolled_at)
  Tech-specific; includes indexes, partitioning, storage params
```

The conceptual model should drive the logical, which drives the physical. Starting with the physical model (creating tables ad hoc) produces fragmentation and inconsistency.

## The Enterprise Data Model

An enterprise data model (EDM) documents the key entities across the entire organisation and their relationships — the semantic backbone of the business.

```
Consortium EDM (simplified):

  SCHOOL ──contains──▶ DOMAIN ──contains──▶ MODULE ──contains──▶ LESSON
                                                                      │
                                                                    ┌─▼────────┐
  LEARNER ──attempts──▶ LESSON_ATTEMPT ──earns──▶ XP_EVENT         │          │
    │                                                                │ LESSON   │
    ├──has──▶ SUBSCRIPTION (billing entity)                         │          │
    │                                                                └──────────┘
    ├──receives──▶ BADGE
    │
    └──belongs to──▶ COHORT

Each entity has:
  - Canonical name (the agreed business term)
  - Definition (in plain English, reviewed by business)
  - Key attributes (business-relevant, not all database columns)
  - Relationships (cardinality: one-to-many, many-to-many)
  - Owner (who is the business authority on this entity's definition)
```

## Canonical Data Models

A canonical model is the agreed-upon, neutral representation of a business entity for integration purposes.

```
Problem without canonical model:
  Sales API sends:   {"accountId": "A-123", "emailAddress": "aria@..."} 
  Platform expects:  {"userId": "U-456", "email": "aria@..."}
  Integration layer: ??? (brittle, bespoke mapping for every pair)

Solution with canonical model:
  Canonical Learner: {"learnerId": "L-789", "email": "aria@...", "tier": "senior"}
  Sales translates:  A-123 → canonical → platform stores as U-456
  Billing translates: S-999 → canonical → analytics stores as L-789
  
  All systems can integrate via the canonical form.
  Changing one system's internal model doesn't break others.
```

## Bounded Contexts (Domain-Driven Design)

In large organisations, one canonical model for every entity everywhere is impractical. "Customer" means different things in different contexts.

```
Sales context:   Customer = Prospect (may not yet be paying)
                 Attributes: lead_score, sales_stage, account_manager
                 
Finance context: Customer = Billing entity
                 Attributes: invoice_address, payment_method, balance
                 
Support context: Customer = Ticket owner
                 Attributes: sla_tier, open_tickets, satisfaction_score
```

Each context has a locally consistent model. **Context maps** show how contexts relate:

```
Context Map:
  Sales [Upstream] ──Customer-Supplier──▶ CRM [Downstream]
  CRM ──Shared Kernel──▶ Platform (shared User definition)
  Platform ──Anti-corruption Layer──▶ Legacy Billing
    (Platform translates; doesn't let Billing's model contaminate Platform's)
```

## Master Data Management

MDM creates a **golden record** — the single authoritative source for core entities.

```
MDM architecture:
  Sales system    ──contributes──▶ ┌──────────────┐
  Platform        ──contributes──▶ │  MDM Hub     │──▶ Universal ID: L-789
  Billing         ──contributes──▶ │  (Golden     │──▶ Canonical attributes
  Support         ──contributes──▶ │  Record)     │──▶ Match/merge rules
                                   └──────────────┘──▶ Survivorship rules
                                         │
                                         ▼
                              Data warehouse (joins on L-789)
```

**Survivorship rules** determine which system's attribute wins when systems disagree:
- Email: Platform wins (most recently validated)
- Name: CRM wins (most formally verified)
- Subscription tier: Billing wins (contractually authoritative)

## Common Mistakes

> **Building the Physical Model First**
> Creating tables ad hoc without a conceptual model produces the "four names for one person" problem. Physical models created in isolation are consistent only within one system.

> **EDM as a One-Time Artefact**
> An enterprise data model that is never updated becomes a historical document rather than a living architecture. Review and update quarterly as new systems are added.

> **Forcing One Canonical Model Everywhere**
> Imposing one universal Customer definition on all contexts destroys the local consistency that bounded contexts provide. Context maps are not a failure — they are the acknowledgement that complexity is inherent in large organisations.

## Mental Model

Think of an enterprise data model as a **national cartographic standard**. Individual maps (system schemas) use their own notation, scale, and detail. The national standard (enterprise model) defines the canonical coordinate system so maps from different producers can be aligned. Without it, a map from one survey overlaid on a map from another shows rivers in the wrong place. With it, every map-maker uses the same datum and all maps align. The canonical entity is the coordinate system; each system's schema is a local map.

**Mini Summary**: Enterprise data modelling has three levels: conceptual (what exists), logical (how structured), physical (how stored). Start conceptual. The enterprise data model documents the canonical entities and relationships across the organisation — the shared semantic vocabulary. Bounded contexts acknowledge that large organisations need local consistency within defined boundaries. Master data management creates golden records for core entities with survivorship rules. Canonical models decouple systems from each other's internal representations.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium is integrating a new corporate training platform (acquisition). The acquired system has: `Employee` (corporate learner), `Programme` (course bundle), `Completion` (finished programme), `Certification` (award). The Consortium's existing model has: `Learner`, `Course`, `Module`, `Lesson`, `Badge`.

1. Draw the conceptual model for each system (entities + relationships, no columns).
2. Identify the semantic overlaps and conflicts between the two models.
3. Design the canonical model for the merged enterprise — what are the unified entities?
4. How would you handle the integration using a bounded context approach? What is the context map?

---

# Integration

**Mathematics**: The enterprise data model is a formal **ontology** — a description of entities, their properties, and their relationships in a domain. Formally, an ontology is a tuple O = (C, R, I, A) where C is a set of classes (entities), R is a set of relations (relationships), I is a set of individuals (instances), and A is a set of axioms (constraints, cardinalities). Description logic (DL) provides the formal semantics: TBox (terminological knowledge — the schema) and ABox (assertional knowledge — the data). OWL (Web Ontology Language) implements this formally. MDM's golden record is an instantiation of the ABox under the TBox constraints — each golden record must satisfy the ontological axioms (e.g. a Learner must have exactly one canonical email).

**Sciences**: Enterprise modelling mirrors **taxonomic classification in biology** — the Linnaean hierarchy (Kingdom → Phylum → Class → Order → Family → Genus → Species). The enterprise data model is the taxonomy: canonical definitions, hierarchical relationships, unambiguous naming. Bounded contexts are analogous to **paraphyletic groups** — locally useful classifications that don't reflect monophyletic ancestry. A 'fish' (paraphyletic) is a useful bounded context for a fish market (customer perspective) but not for evolutionary biology (phylogenetic context). Context maps document where the biological and market taxonomies diverge and how to translate between them.

---

# The Golden Learner

The MDM project ran for four months. At the end, every learner had one canonical ID — L-xxxxx — that worked across the sales system, the platform, the billing system, and the support system. The quarterly retention report ran again. Finance: 13,100 paying learners. Product: 13,100 active users. Support: 13,100 customer accounts. "One number," the CDO said. "Finally, one number." The Lead Data Engineer saved the context map. This was the work that made every other piece of data work better.
