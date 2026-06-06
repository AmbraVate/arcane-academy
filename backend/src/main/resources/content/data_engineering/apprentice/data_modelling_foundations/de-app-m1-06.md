---
id: de-app-m1-06
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
lesson: entities
title: "Entities"
sortOrder: 1
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m1-01]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Clearly defines what an entity is in data modelling
    - Distinguishes between an entity type and an entity instance
    - Provides two original real-world examples of entities
    - Explains why identifying entities is the first step in data modelling
    - Reflects on how poor entity identification leads to design problems
  keywords: [entity, instance, type, object, thing, model, table, represent, real-world]
  modelAnswer: |
    An entity is any distinct, identifiable thing about which data is stored — a person, place, event, or concept. In data modelling, an entity type defines the category (e.g., Customer), while an entity instance is a specific occurrence (e.g., the customer Sarah Johnson with ID 1042). Identifying entities correctly is the foundational step in data modelling because every table in a relational database represents one entity type, and getting this wrong creates confusion and redundancy throughout the entire schema.
guidedSteps:
  - id: de-app-m1-06-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of the following best defines an "entity" in data modelling?
    inputConfig:
      options:
        - "A column in a database table"
        - "A distinct, identifiable thing about which data is stored"
        - "A relationship between two tables"
        - "A rule that prevents duplicate records"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A distinct, identifiable thing about which data is stored"]
      rejectedFeedback: "An entity is the 'thing' being modelled — a person, product, order, or event. Columns and relationships are other modelling concepts that describe and connect entities."
    hint: "Think about the 'nouns' of your data model — the things you want to keep records about."
    reflectionPrompt: "In a library system, can you identify three distinct entities that would need to be modelled?"
  - id: de-app-m1-06-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence: "An entity ________ is the category or template, while an entity ________ is one specific occurrence of that category."
    inputConfig:
      placeholder: "type ... instance"
    markingRule:
      matchMode: CONTAINS
      accepted: [type, instance]
      rejectedFeedback: "An entity type (like 'Customer') defines the category. An entity instance (like 'Customer #1042, Sarah Johnson') is one specific real-world occurrence of that type."
    hint: "Think about the difference between the concept 'dog' and the specific dog called 'Rover'."
    reflectionPrompt: "Why is it important to design for entity types rather than specific instances when building a database?"
  - id: de-app-m1-06-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why identifying entities correctly is the most important first step in data modelling.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [table, schema, structure, foundation, relationships, attributes, design]
      rejectedFeedback: "Consider that each entity becomes a table, and all relationships and attributes are built around entities. Getting the entity list wrong means the entire schema is built on a flawed foundation."
    hint: "Think about what happens downstream if you define your entities incorrectly at the start."
    reflectionPrompt: "What real-world harm could come from a hospital data system that modelled 'Doctor' and 'Patient' as the same entity?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In a school database, which of the following is an entity?"
    options: ["The number of students in a class", "A Student", "The average grade across all subjects", "A term date range"]
    correctIndex: 1
    feedback: "A Student is an entity — a distinct, identifiable thing about which data is stored. Counts and averages are derived facts, and date ranges are attributes, not entities."
  - type: MULTIPLE_CHOICE
    question: "What is the relationship between an entity type and an entity instance?"
    options:
      - "They are the same thing described differently"
      - "A type is the category; an instance is one specific occurrence of that category"
      - "An instance contains multiple types"
      - "Types are only used in object-oriented programming, not in data modelling"
    correctIndex: 1
    feedback: "An entity type defines the pattern or category (e.g., Product). An entity instance is a specific record — one particular product with its own unique identifier and attribute values."
retrieval:
  recall: "In one sentence, define what an entity is in data modelling."
  explain: "Explain the difference between an entity type and an entity instance, using a concrete example."
  mistakeId:
    code: "an entity is the same as a table"
    answer: "An entity is a conceptual thing from the real world — the idea exists before any database is designed. A table is one implementation of an entity in a relational database. The entity concept belongs to the model; the table belongs to the implementation."
---

# Hook

Before you can design a single database table, write a line of SQL, or build a data pipeline, you need to answer one fundamental question: **what things are you keeping records about?**

In data modelling, these "things" are called **entities**. An entity might be a person, a product, an order, a location, or an event — anything distinct and identifiable that your system needs to track. Getting the entity list right is the first and most important step in data modelling. Get it wrong, and every decision that follows is built on a shaky foundation.

What kinds of things would you need to track to run a successful online bookshop? Start listing them in your head before reading on.

# Lore Introduction

Master Selvaris spread a large blank parchment across the table and handed her apprentice a quill. "Before we inscribe a single record," she said, "we must ask: what things exist in the realm that we care about? The merchants exist. The goods exist. The caravans exist. The towns exist." She pointed to a corner of the parchment. "Begin there. Name the things. Do not describe them yet — just name them. A well-designed archive begins not with columns and rows, but with a clear answer to the question: what are we keeping records of?" She looked expectantly at her apprentice. "So — what things exist in the trading system we must model today?"

# Core Learning

## Concept Introduction

| Term | Definition | Example |
|------|-----------|---------|
| **Entity** | A distinct, identifiable thing about which data is stored | Customer, Product, Order, Employee, Location |
| **Entity Type** | The category or template that defines the entity — becomes a table in a relational DB | `Customer` (the concept) |
| **Entity Instance** | One specific occurrence of an entity type — becomes a row in a table | Customer #1042: Sarah Johnson, sarah@example.com |
| **Entity Identifier** | The attribute that uniquely identifies each instance | `customer_id = 1042` |
| **Weak Entity** | An entity that cannot be uniquely identified without a relationship to another entity | `OrderLine` — cannot exist without an `Order` |

## Why It Matters

Every table in a relational database corresponds to exactly one entity type. If you:
- Combine two entities into one table, you create redundancy and update anomalies
- Split one entity into multiple tables unnecessarily, you create needless complexity
- Miss an entity entirely, you have no place to store that type of record

Identifying entities correctly is the foundation upon which all other modelling decisions rest. The discipline of entity identification — asking "is this a distinct thing?" — is a transferable skill across every database system you will ever design.

## Worked Examples

**Example 1: E-commerce System**
Entities: `Customer`, `Product`, `Order`, `OrderLine`, `Address`, `Category`, `Review`

Notice that `Order` and `OrderLine` are separate entities. An order has a date, a customer, and a total. An order line has a product, a quantity, and a price. Conflating them into one entity would mean repeating customer and order-level data for every product in the basket.

**Example 2: University System**
Entities: `Student`, `Course`, `Lecturer`, `Module`, `Enrolment`, `Grade`, `Department`

`Enrolment` is an entity in its own right — it represents the relationship between a student and a course and carries its own data (enrolment date, status). This is a classic "associative entity" that emerges from a many-to-many relationship.

**Example 3: Hospital System**
Entities: `Patient`, `Doctor`, `Appointment`, `Ward`, `Prescription`, `Diagnosis`, `Test`

`Appointment` is an entity, not just a date on a calendar — it connects a patient to a doctor and carries its own data (type, outcome, notes). Recognising when a relationship needs to become an entity is a key modelling skill.

## Common Mistakes

- **Confusing entities with attributes**: "Customer Address" is not an entity — it is an attribute of Customer. Unless addresses are shared across customers (e.g., a workplace shared by multiple contacts), addresses belong as fields on the Customer entity.
- **Modelling processes as entities**: "Sales" is usually a process, not an entity. The entity is "Sale" or "Transaction" — the record of an individual event. This distinction matters when naming tables.
- **Creating entity-per-status**: Beginners sometimes create separate entities for "ActiveCustomer" and "InactiveCustomer." These should be one `Customer` entity with a `status` attribute.

## Mental Model

Think of entities as the **nouns** of your data model. Just as a sentence needs a subject (noun) before you can say anything about it, a data model needs entities before you can store any attributes or draw any relationships. When you read a business requirements document, underline every noun — those candidates are your entities.

## Mini Summary

- ✔ An entity is any distinct, identifiable thing about which data is stored
- ✔ Entity types become tables; entity instances become rows
- ✔ Every entity needs a unique identifier
- ✔ Weak entities depend on parent entities for their identity
- ✔ Correct entity identification is the foundational step of all data modelling

# Guided Practice Quest

Work through the guided steps to practise identifying and distinguishing entity types from entity instances, and to explain why the entity concept is fundamental to relational database design.

# Solo Practice Quest

You have been asked to design the data model for a local library. Write a list of all the entities you think the system needs to track, with a one-sentence justification for each. Then for three of your entities, describe: (1) two or three example instances of that entity, and (2) one attribute that belongs to that entity and one that does not (explain why the second one does not belong). Finally, identify one weak entity in your list and explain why it is weak.

# Integration

**Mathematics**: In set theory, an entity type is analogous to a set, and entity instances are the elements of that set. The entity identifier is the element's unique label. Modelling with entities applies set-theoretic thinking — each set (entity type) has distinct, non-repeated elements (instances) and can be related to other sets through functions (relationships). This mathematical foundation is why relational databases, rooted in Codd's relational algebra, are so powerful.

**Psychology**: Cognitive psychology distinguishes between **categories** (concepts that group similar things) and **exemplars** (specific remembered instances of those categories). Entity types are categories; entity instances are exemplars. When people design databases from intuition, they often mix levels — storing some things at the category level and others at the exemplar level, producing inconsistent schemas. Recognising this cognitive tendency helps you design more rigorously.

# Lore Conclusion

Master Selvaris surveyed the list of entities her apprentice had named across the parchment. "Good," she said. "You have found the nouns. Now we know what exists." She dipped her quill and drew a circle around each one. "Everything else — the descriptions, the connections, the histories — will hang from these. Change one of these circles carelessly later, and the whole structure trembles." She set down her quill. "Name the things well. All else follows."

---
