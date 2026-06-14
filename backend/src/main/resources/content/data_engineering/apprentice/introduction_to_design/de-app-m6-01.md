---
id: de-app-m6-01
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m6
moduleTitle: "Module 6: Database Design Foundations"
moduleGlyph: "📐"
moduleSortOrder: 6
topicSlug: introduction_to_design
topicTitle: "Introduction to Design"
topicSortOrder: 1
lesson: why_design_matters
title: "Why Design Matters"
sortOrder: 1
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [software_engineering, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains why good database design matters before writing any SQL
    - Identifies at least three problems caused by poor schema design
    - Defines the concept of a schema and its role in a database
    - Explains what an Entity-Relationship diagram communicates
    - Reflects on the cost of redesigning a poorly designed database after data has been entered
  keywords: [schema, design, ER diagram, table, relationship, entity, planning, structure]
  modelAnswer: |
    Database design defines the structure of how data is stored before any data is entered. A well-designed schema makes queries simple, ensures data integrity, prevents redundancy, and scales gracefully. Poor design leads to data duplication, complex queries that are hard to maintain, integrity violations, and costly redesigns after data has accumulated. A schema defines the tables, columns, data types, and relationships in a database. An Entity-Relationship (ER) diagram visually represents the entities (things) in a domain and their relationships, serving as the blueprint from which a schema is implemented.
guidedSteps:
  - id: de-app-m6-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A developer stores all customer order information in a single text column: "John Smith, 42 Oak St, 3 widgets, £45.99, 2026-06-01". What problem does this cause?
    inputConfig:
      options:
        - "The data takes up too much storage space"
        - "The data cannot be indexed, queried, or validated at the column level"
        - "The database cannot store text longer than 50 characters"
        - "Foreign keys cannot be applied to text columns"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The data cannot be indexed, queried, or validated at the column level"]
      rejectedFeedback: "Packing multiple facts into one column prevents filtering, sorting, joining, and validating individual values. You cannot write WHERE price > 50 on a blob of text containing the price."
    hint: "Think about what you would need to do to find all orders over £50 from this column."
    reflectionPrompt: "How would you redesign this to allow querying individual fields like price, customer name, or date?"
  - id: de-app-m6-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "A visual diagram that shows the entities in a domain, their attributes, and the relationships between them is called an Entity-________ Diagram."
    inputConfig:
      placeholder: "Relationship"
    markingRule:
      matchMode: CONTAINS
      accepted: [Relationship, relationship, relation]
      rejectedFeedback: "An Entity-Relationship (ER) Diagram is the standard tool for planning a database schema before implementation. It maps entities, attributes, and relationships visually."
    hint: "This diagram names the connections between tables."
    reflectionPrompt: "Why is it better to draw an ER diagram before writing SQL rather than designing as you go?"
  - id: de-app-m6-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain what a database schema is and why defining it carefully before writing code matters.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [schema, structure, design, table, column, before, plan]
      rejectedFeedback: "A schema defines the structure of a database — tables, columns, data types, and constraints. Defining it carefully before implementation prevents costly refactoring after data is loaded and ensures the database can answer the questions you will need to ask."
    hint: "Think of a schema as the blueprint of a building — what is the cost of changing the blueprint after the walls are up?"
    reflectionPrompt: "What questions should you ask before designing a database schema?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of the following is a consequence of poor database design?"
    options:
      - "Faster query performance"
      - "Reduced storage requirements"
      - "Data redundancy and difficult-to-maintain queries"
      - "Automatically enforced data integrity"
    correctIndex: 2
    feedback: "Poor design typically causes data redundancy (the same fact stored multiple times), which leads to inconsistency and complex queries to work around structural problems."
  - type: MULTIPLE_CHOICE
    question: "What does an Entity-Relationship (ER) diagram represent?"
    options:
      - "The SQL statements needed to create a database"
      - "The performance characteristics of a running database"
      - "The entities in a domain, their attributes, and relationships"
      - "The backup schedule for a production database"
    correctIndex: 2
    feedback: "An ER diagram is a visual planning tool that maps the things (entities), their properties (attributes), and how they relate to each other — serving as the blueprint for a database schema."
retrieval:
  recall: "Define a database schema in one sentence and explain what it contains."
  explain: "Why is it expensive to fix poor database design after data has been entered?"
  mistakeId:
    code: "I can design the database as I code — I don't need to plan it first"
    answer: "Schema changes after data is loaded are extremely costly. Renaming columns, splitting tables, adding NOT NULL constraints on populated columns, and restructuring relationships all require careful migration scripts and may require downtime. Designing upfront prevents these costs and produces a more coherent, queryable structure."
---

# Hook

Imagine building a house without architectural plans. You pour the foundation, then decide where the walls go, then realise the kitchen should be where the bathroom is. Now the pipes are in the wrong place. Moving them means tearing up the floor. The house technically stands — but everything about it is harder than it needed to be.

Database design works the same way. A schema built without forethought leads to redundant data, impossible queries, and schema migrations that require taking systems offline for days. Good database design, done before the first INSERT, makes everything easier: queries are simpler, data stays consistent, and the system grows gracefully.

Before you write a single line of SQL, you need to understand what good design looks like — and why it matters more than any individual query you will ever write.

# Lore Introduction

Master Selvaris unrolled a large architectural drawing across his desk — not of a building, but of the Archive itself. Shelves, rooms, corridors, cross-reference vaults. "Before the first stone of the Archive was laid," he said, "the founders spent three months drawing this." He tapped the parchment. "They asked: what kinds of knowledge will we store? How will scholars find it? What connects scrolls to provinces to authors to dates?" He rolled it back up carefully. "The Archive has served the Kingdom for four hundred years without structural failure. Because we designed it before we built it. Your databases must be the same."

# Core Learning

## Concept Introduction

| Concept | Definition | Example |
|---------|-----------|---------|
| **Schema** | The structure of a database — tables, columns, types, and constraints | A schema for a library: `books`, `members`, `loans` tables |
| **Entity** | A distinct thing or concept in the domain being modelled | Customer, Order, Product |
| **Attribute** | A property of an entity | Customer: name, email, date_of_birth |
| **Relationship** | How entities are connected | A Customer places many Orders |
| **ER Diagram** | Visual blueprint showing entities, attributes, and relationships | Box for Customer connected to box for Order |
| **Normalisation** | Process of organising a schema to reduce redundancy | Splitting one bloated table into several focused tables |
| **Schema migration** | A change to an existing schema after data has been loaded | ALTER TABLE — expensive and risky |

## Why It Matters

The cost of schema decisions is asymmetric:
- **Before data**: Free to change. A diagram change takes minutes.
- **After data is loaded**: Schema changes require migration scripts, may need downtime, can lose data, and can break application code.

Good design also determines query complexity. A well-designed schema makes a JOIN query straightforward; a poorly designed one requires string parsing, multiple subqueries, or data cleaning before the real question can be asked.

## Worked Examples

**Example 1: A poorly designed order table**
```
| order_info                                              |
|---------------------------------------------------------|
| John Smith, 42 Oak St, widget x3, £45.99, 2026-06-01  |
| Jane Brown, 7 Elm Rd, gadget x1, £12.50, 2026-06-02  |
```
How would you query total revenue? Or all orders for a specific customer? Or orders over £30? You cannot — the data is locked in unstructured text.

**Example 2: Well-designed alternative**
```
customers: id, name, address
products: id, name, price
orders: id, customer_id, order_date
order_items: order_id, product_id, quantity
```
Now: `SELECT SUM(p.price * oi.quantity) FROM orders o JOIN order_items oi ... WHERE o.customer_id = 5`

**Example 3: ER diagram notation**
```
[Customer] ——< [Order] >—— [Product]
  name           date        name
  email          status      price
  address
```
The crow's foot notation shows "one customer places many orders" and "many orders contain many products."

## Common Mistakes

- **Designing tables for how you currently query, not for the domain**: If requirements change (and they always do), a schema designed around today's single use case will not accommodate tomorrow's new question.
- **Skipping the ER diagram**: Without a visual model, it is easy to miss relationships, create redundant tables, or forget important entities.
- **Premature optimisation**: Adding indexes, partitions, or denormalisation before the schema is correct inverts priorities — first get the structure right.

## Mental Model

A database schema is the blueprint of a building. The ER diagram is the architect's sketch before the blueprint. Before you lay foundations (write SQL), before you build walls (add data), you must agree on what the building is for, what rooms it needs, and how they connect. Every shortcut in planning multiplies the cost of construction.

## Mini Summary

- ✔ A schema defines the structure of a database before any data is entered
- ✔ An ER diagram maps entities, attributes, and relationships visually
- ✔ Good design prevents redundancy, enables clean queries, and supports growth
- ✔ Poor design is exponentially expensive to fix after data accumulates
- ✔ Design for the domain first; optimise only after the structure is correct

# Guided Practice Quest

Work through the guided steps to identify the problems with a poorly designed schema and sketch the elements of a well-designed ER diagram for a simple domain.

# Solo Practice Quest

Choose a real-world system you interact with regularly — a gym, a coffee shop, a streaming service, or a school. Identify at least four entities in that domain. For each entity, list three to five attributes. Then draw (or describe) the relationships between the entities, noting whether each relationship is one-to-one, one-to-many, or many-to-many. Reflect on: which relationship is most complex, and what tables or strategies would you need to represent it in a relational database?

# Integration

**Mathematics**: Graph theory provides a formal framework for understanding relationships between entities. An ER diagram is a bipartite graph where entity nodes are connected by relationship edges. Understanding graph structure helps engineers reason about join complexity — how many edges must be traversed to answer a query — and identify opportunities for simplification.

**Software Engineering**: In object-oriented design, the process of identifying classes, their attributes, and their relationships before writing code is called domain modelling. ER diagram design is the database engineering equivalent. Both practices apply Domain-Driven Design principles: understand the real world first, then encode it in structure.

# Lore Conclusion

Master Selvaris rolled up the architectural drawing and placed it back in its case. "The founders of the Archive were not architects by trade," he said. "They were knowledge-keepers who understood that structure enables discovery. That a well-placed corridor makes every scholar's work faster. That a naming convention followed consistently becomes a language everyone can read." He handed you a blank piece of parchment and a quill. "Your first task: before we write a single inscription, draw me what the vault should look like. What entities will it hold? What connects them?" He stepped back. "Begin with the drawing. Everything else follows."

---
