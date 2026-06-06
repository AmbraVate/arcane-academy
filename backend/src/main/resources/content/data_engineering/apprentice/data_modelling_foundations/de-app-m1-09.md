---
id: de-app-m1-09
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m1
moduleTitle: "Module 1: Understanding Data"
moduleGlyph: "📊"
moduleSortOrder: 1
topicSlug: data_modelling_foundations
topicTitle: "Data Modelling Foundations"
topicSortOrder: 2
lesson: modelling_real_world_systems
title: "Modelling Real-World Systems"
sortOrder: 4
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m1-06, de-app-m1-07, de-app-m1-08]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Describes the process of moving from requirements to an entity-relationship model
    - Identifies entities, attributes, and relationships in a given scenario
    - Explains how abstraction simplifies real-world complexity into a data model
    - Acknowledges that models are approximations and reflects on what is left out
    - Reflects on a situation where the model might need to evolve as requirements change
  keywords: [model, requirements, abstraction, entity, relationship, diagram, ER, schema, simplify, evolve]
  modelAnswer: |
    Modelling a real-world system begins with eliciting requirements — understanding what the system must track and why. From requirements, engineers identify entities (the things), their attributes (their properties), and the relationships between them. The resulting entity-relationship (ER) diagram is an abstraction: it captures what matters for the system's purpose while intentionally leaving out irrelevant detail. All models are approximations, and good engineers design them to be extensible as requirements evolve.
guidedSteps:
  - id: de-app-m1-09-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You are designing a hotel booking system. Which of the following correctly identifies the core entities?
    inputConfig:
      options:
        - "Booking, Price, Availability, Payment"
        - "Guest, Room, Booking, Hotel"
        - "Check-in date, Check-out date, Room number, Guest name"
        - "Room cleaning, Staff rota, Kitchen menu, Front desk"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Guest, Room, Booking, Hotel"]
      rejectedFeedback: "Entities are the distinct things the system tracks. Guest, Room, Booking, and Hotel are all nouns representing real-world objects. Dates and names are attributes. 'Availability' is a concept derived from bookings, not its own entity."
    hint: "Look for the nouns that represent identifiable things the system must track — not properties or derived concepts."
    reflectionPrompt: "What other entities might a full hotel booking system need beyond the four core ones?"
  - id: de-app-m1-09-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence: "An entity-relationship diagram uses ________ notation to visually represent entities, their attributes, and the relationships between them."
    inputConfig:
      placeholder: "ER"
    markingRule:
      matchMode: CONTAINS
      accepted: [ER, ERD, entity-relationship, entity relationship, crow's foot, UML]
      rejectedFeedback: "ER (Entity-Relationship) diagrams — developed by Peter Chen in 1976 — use standardised notation to visually represent the structure of a data model before it is implemented in a database."
    hint: "Think about the initials of the diagram type named after the two main concepts it represents."
    reflectionPrompt: "What are the benefits of drawing an ER diagram before writing any SQL?"
  - id: de-app-m1-09-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain what abstraction means in the context of data modelling and why it is a feature rather than a limitation.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [abstraction, simplify, relevant, purpose, omit, detail, model, representation]
      rejectedFeedback: "Abstraction means deliberately leaving out irrelevant details to focus on what matters for the system's purpose. A hotel booking system does not need to model the colour of curtains in each room — that detail is irrelevant to the booking process. Abstraction makes models manageable and focused."
    hint: "Consider that a map of London is not London — it leaves out most details and is more useful for that reason."
    reflectionPrompt: "What real-world detail might you leave out of a library book-lending model, and why is that a good decision?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the primary purpose of an ER diagram in the data modelling process?"
    options:
      - "To write SQL CREATE TABLE statements"
      - "To visually represent entities, attributes, and relationships before implementation"
      - "To document the number of rows in each table"
      - "To show which users have access to each table"
    correctIndex: 1
    feedback: "An ER diagram is a conceptual design tool — it communicates the structure of the data model visually, enabling review and discussion before any database is built."
  - type: MULTIPLE_CHOICE
    question: "A data model that accurately represents a business domain but omits irrelevant details is best described as:"
    options: ["Incomplete", "An abstraction", "A prototype", "A normalised schema"]
    correctIndex: 1
    feedback: "All good models are abstractions — they are purposeful simplifications that include what matters and exclude what does not. George Box's principle applies: 'All models are wrong, but some are useful.'"
retrieval:
  recall: "In one sentence, describe what an ER diagram represents."
  explain: "Explain the process of moving from a business requirement to an entity-relationship model in three steps."
  mistakeId:
    code: "the data model should capture everything about the real world"
    answer: "A data model should capture only what is relevant to the system's purpose. Over-modelling creates unnecessary complexity. Abstraction — deliberately omitting irrelevant detail — is a feature of good design, not a limitation."
---

# Hook

You have a new project: build the data layer for a gym membership management system. Where do you begin? Not with tables. Not with SQL. You begin by understanding the real-world system you are trying to represent — the people, the equipment, the classes, the memberships, the payments — and then systematically abstracting that world into a model.

This is the craft of **data modelling**: translating the messy complexity of real-world systems into clean, structured schemas that computers can store and query. It requires both analytical precision (getting the entities, attributes, and relationships right) and artistic judgment (deciding what to include and what to leave out).

Every data model is a simplification of reality. The skill is in knowing *which* simplification serves the system's purpose.

# Lore Introduction

"A map is not the territory," Master Selvaris said, unrolling a detailed chart of the trading routes across the realm. "This map does not show every tree and stone along each road. It shows the roads, the towns, the distances, and the waypoints. That is all we need to plan a caravan route." She placed her finger on a town. "A data model is the same. It shows the things we care about, described precisely enough to do our work. The task of a good archivist — and a good data engineer — is to know the difference between what matters and what does not."

# Core Learning

## Concept Introduction

### The Modelling Process

| Step | Activity | Output |
|------|----------|--------|
| **1. Requirements Gathering** | Interview stakeholders, read business requirements, understand what the system must track | List of business questions the data must answer |
| **2. Entity Identification** | Identify the distinct things the system must track | Entity list |
| **3. Attribute Definition** | For each entity, define its properties, types, and constraints | Attribute tables |
| **4. Relationship Mapping** | Identify how entities connect, with cardinality and participation | Relationship list |
| **5. ER Diagram** | Draw the visual model using ER notation | Conceptual ER diagram |
| **6. Review and Refine** | Validate against requirements; check for completeness and correctness | Revised ER diagram |
| **7. Logical Schema** | Translate the ER model into a logical schema (tables, columns, foreign keys) | SQL DDL or schema documentation |

### ER Diagram Notation (Chen / Crow's Foot)

| Symbol | Meaning |
|--------|---------|
| Rectangle | Entity |
| Ellipse | Attribute |
| Diamond | Relationship |
| Line | Connection |
| Double line | Mandatory participation |
| Single line with circle | Optional participation |
| Crow's foot | "Many" side |

## Why It Matters

Modelling before building prevents costly rework. A schema flaw discovered after millions of rows have been loaded is expensive to fix. A flaw discovered in a diagram before any code is written costs nothing but a few minutes of conversation. This is why data modelling is considered a high-leverage engineering activity — the investment in upfront design pays dividends for the lifetime of the system.

## Worked Examples

**Example: Online Bookshop**

*Requirements*: The system must track books for sale, customer orders, and customer accounts. Books belong to categories and have authors. Customers can place multiple orders. Each order contains one or more books with quantities.

*Step 1 — Entities*: `Customer`, `Order`, `OrderLine`, `Book`, `Author`, `Category`

*Step 2 — Relationships*:
- Customer → Order: one-to-many (one customer, many orders)
- Order → OrderLine: one-to-many (one order, many lines)
- OrderLine → Book: many-to-one (many lines reference one book)
- Book → Author: many-to-many (books have multiple authors; authors write multiple books) → junction: `book_authors`
- Book → Category: many-to-one (each book in one category; one category has many books)

*Step 3 — Key Attributes*: Each entity gets a surrogate key (`customer_id`, `order_id`, etc.)

*Step 4 — Participation*:
- An Order MUST belong to a Customer (mandatory)
- A Customer MAY have zero Orders (optional)
- An OrderLine MUST reference a Book and an Order (both mandatory)

## Common Mistakes

- **Skipping the diagram**: Going straight to SQL tables without drawing an ER diagram produces schemas that seem logical locally but have inconsistencies globally.
- **Modelling the current state only**: Systems evolve. A model designed only for today's requirements breaks when new requirements arrive. Building in extensibility (e.g., not hard-coding category as a text field when a Category table will be needed later) saves rework.
- **Conflating conceptual and physical modelling**: The ER diagram is conceptual. Physical decisions (index types, partitioning, storage engines) come later. Mixing the two levels creates confusion.

## Mental Model

Think of modelling as drawing architectural blueprints before constructing a building. The blueprints show rooms, walls, doors, and corridors — not the exact paint colour, furniture placement, or electrical routing. They answer the structural question: is this building the right shape to house its occupants? A data model answers the same structural question for a data system: is this schema the right shape to store and query the information we need?

## Mini Summary

- ✔ Data modelling translates real-world systems into structured schemas
- ✔ The process moves from requirements → entities → attributes → relationships → ER diagram → logical schema
- ✔ ER diagrams are visual tools for communicating and reviewing data models before implementation
- ✔ Abstraction — omitting irrelevant detail — is a feature of a good model
- ✔ Modelling before building prevents expensive schema rework

# Guided Practice Quest

Work through the guided steps to identify entities in a real-world scenario, classify an ER diagram correctly, and articulate why abstraction is a design virtue rather than a limitation.

# Solo Practice Quest

You have been asked to design the data model for a community sports league management system. The league needs to track: teams, players, matches, venues, and results. Write the full modelling process: (1) list all entities with justifications, (2) define five attributes for each entity, (3) map all relationships with cardinality and participation constraints, (4) identify any junction tables needed, and (5) write a one-paragraph reflection on what you deliberately left out of the model and why. Aim for a model that is complete for the core use case but not over-engineered.

# Integration

**Psychology**: The process of data modelling is a form of **mental model construction** — building an internal representation of an external system. Cognitive psychologists note that people form mental models constantly, but these models are often imprecise and idiosyncratic. The discipline of ER diagramming externalises and formalises mental models, allowing multiple people to share, review, and correct the same model. This is a direct application of distributed cognition theory to engineering practice.

**Mathematics**: An ER model is formally equivalent to a directed graph where entities are nodes and relationships are edges. Graph theory provides tools for analysing ER models: checking connectivity (are all entities reachable?), identifying cycles (might there be circular dependencies?), and analysing degree distribution (which entities are highly connected?). This mathematical perspective helps engineers reason about schema complexity and spot potential performance bottlenecks before building.

# Lore Conclusion

Master Selvaris stepped back from the parchment and tilted her head. "It is not perfect," she said. "It never is. But it is good enough to build from." She ran her finger along the relationships. "You have captured the essence of the trading system — the merchants, the goods, the routes, the transactions. Everything the Council needs to plan and govern the markets." She smiled. "Remember: the goal is not to model everything. The goal is to model the right things, described precisely, connected correctly." She set a fresh ink pot on the table. "Now we can begin to build."

---
