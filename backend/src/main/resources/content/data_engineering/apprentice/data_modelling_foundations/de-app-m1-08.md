---
id: de-app-m1-08
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
lesson: relationships
title: "Relationships"
sortOrder: 3
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m1-06, de-app-m1-07]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines a relationship in data modelling and explains its purpose
    - Correctly distinguishes one-to-one, one-to-many, and many-to-many cardinalities
    - Explains how many-to-many relationships are resolved using a junction table
    - Identifies at least one participation constraint (mandatory vs optional)
    - Reflects on how misidentifying cardinality leads to schema design problems
  keywords: [relationship, cardinality, one-to-many, many-to-many, junction, foreign key, participation, optional, mandatory]
  modelAnswer: |
    A relationship in data modelling describes how two or more entities are associated. Cardinality defines how many instances of one entity can be related to instances of another — one-to-one, one-to-many, or many-to-many. Many-to-many relationships require a junction table to resolve them into two one-to-many relationships, because relational databases cannot directly model multiple links between the same two entity types. Participation constraints specify whether a relationship is mandatory or optional for each entity involved.
guidedSteps:
  - id: de-app-m1-08-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In a university system, one Department can have many Lecturers, but each Lecturer belongs to exactly one Department. What is the cardinality of this relationship?
    inputConfig:
      options:
        - "One-to-one"
        - "One-to-many"
        - "Many-to-many"
        - "Zero-to-many"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["One-to-many"]
      rejectedFeedback: "One Department to many Lecturers is a classic one-to-many relationship. The 'many' side (Lecturer) holds the foreign key pointing back to the 'one' side (Department)."
    hint: "Count the maximum on each side: how many Lecturers can a Department have? How many Departments can a Lecturer belong to?"
    reflectionPrompt: "Where would you store the foreign key in this relationship — on the Department table or the Lecturer table? Why?"
  - id: de-app-m1-08-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence: "A many-to-many relationship is resolved in a relational database by creating a ________ table that holds foreign keys to both related entities."
    inputConfig:
      placeholder: "junction"
    markingRule:
      matchMode: CONTAINS
      accepted: [junction, associative, bridge, link, pivot, join, intersection]
      rejectedFeedback: "A junction (also called associative or bridge) table resolves a many-to-many relationship by containing foreign keys to both entities, creating two one-to-many relationships."
    hint: "This special table sits between the two entities and links them."
    reflectionPrompt: "What additional attributes might a junction table carry beyond the two foreign keys?"
  - id: de-app-m1-08-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain what a participation constraint is and give one example of a mandatory and one example of an optional participation.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [mandatory, optional, must, required, participate, relationship, constraint]
      rejectedFeedback: "Participation constraints define whether an entity instance MUST participate in a relationship (mandatory/total) or MAY participate (optional/partial). For example, an Order MUST have a Customer (mandatory), but a Customer does not have to have an Order (optional)."
    hint: "Think about whether an entity can exist independently of the relationship or always requires the other entity."
    reflectionPrompt: "How does a mandatory participation constraint translate into a NOT NULL constraint in SQL?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A student can enrol in many courses, and each course can have many students. What type of relationship is this?"
    options: ["One-to-one", "One-to-many", "Many-to-many", "Recursive"]
    correctIndex: 2
    feedback: "Student–Course is a classic many-to-many relationship. It requires a junction table (e.g., Enrolment) with foreign keys to both Student and Course to be implemented in a relational database."
  - type: MULTIPLE_CHOICE
    question: "In a one-to-many relationship between Department and Employee, where does the foreign key live?"
    options:
      - "On the Department table"
      - "On the Employee table"
      - "In a separate junction table"
      - "In both tables"
    correctIndex: 1
    feedback: "The foreign key always lives on the 'many' side. Each Employee record holds a department_id foreign key pointing to the Department they belong to."
retrieval:
  recall: "In one sentence, define what cardinality means in data modelling."
  explain: "Explain why a many-to-many relationship cannot be directly implemented in a relational database and how it is resolved."
  mistakeId:
    code: "adding multiple foreign keys to one column to model many-to-many"
    answer: "Storing multiple IDs in one column (e.g., '1,2,5') violates first normal form and makes the data unqueryable with standard SQL. The correct approach is a junction table with one row per link."
---

# Hook

Entities do not exist in isolation. A customer places an order. An order contains products. A product belongs to a category. A student enrols in a course. A doctor sees patients. The real world is a web of connections — and a data model that captures entities but ignores their relationships is only half-complete.

**Relationships** are the connections between entities, and the rules governing those connections — how many instances of one entity can link to how many of another — are called **cardinality**. Getting cardinality right determines whether your schema accurately represents reality, whether your joins work correctly, and whether your data remains consistent over time.

Think about a library. A book can be borrowed by a reader. Can one book be borrowed by many readers simultaneously? Can one reader borrow many books at once? Already you have a relationship with interesting cardinality — and a design decision to make.

# Lore Introduction

"The entities are named and described," Master Selvaris said, surveying the parchment. "But look — they are islands. The merchant exists. The caravan exists. The town exists. But how are they connected?" She drew a line between two circles. "A merchant owns caravans. One merchant, many caravans. But a caravan belongs to only one merchant." She drew another line. "A caravan trades with many towns. And a town trades with many caravans." She paused. "That second one is more complex — you cannot draw it simply. That is our challenge today: to model the connections between things, not just the things themselves."

# Core Learning

## Concept Introduction

| Relationship Type | Description | Example | Implementation |
|-----------------|-------------|---------|---------------|
| **One-to-One (1:1)** | One instance of A relates to exactly one instance of B | A User has one Profile | Foreign key on either side (usually the dependent entity) |
| **One-to-Many (1:N)** | One instance of A relates to many instances of B; each B relates to one A | A Department has many Employees | Foreign key on the 'many' side (Employee.department_id) |
| **Many-to-Many (M:N)** | One instance of A relates to many instances of B and vice versa | Students enrol in many Courses; Courses have many Students | Junction table with FK to both entities |

### Participation Constraints

| Type | Description | SQL Implementation |
|------|-------------|-------------------|
| **Mandatory (Total)** | Every instance of the entity MUST participate in the relationship | NOT NULL on the foreign key |
| **Optional (Partial)** | An instance MAY or MAY NOT participate | NULL allowed on the foreign key |

## Why It Matters

Relationships define the integrity of your data. Without correctly modelled relationships:
- Records become orphaned (an OrderLine with no Order)
- Many-to-many data gets duplicated or impossible to query
- Business rules cannot be enforced at the database level

The foreign key mechanism — which implements relationships in relational databases — is one of the most powerful data integrity tools available. Understanding relationship types is the conceptual foundation for understanding foreign keys.

## Worked Examples

**Example 1: One-to-Many — Blog Platform**
One `Author` writes many `Posts`. Each `Post` has exactly one `Author`.
- `posts.author_id` → foreign key referencing `authors.author_id`
- If an Author must have Posts, participation is optional on the Author side (you can create an author before they publish)
- A Post must have an Author: mandatory participation → `author_id NOT NULL`

**Example 2: Many-to-Many — E-commerce Tags**
A `Product` can have many `Tags`. A `Tag` can be applied to many `Products`.
- Junction table: `product_tags(product_id, tag_id)`
- This junction table might carry an additional attribute: `added_at` (when the tag was applied)
- The junction table enables `SELECT * FROM products JOIN product_tags ... JOIN tags ...`

**Example 3: One-to-One — User and Security Profile**
A `User` has exactly one `SecurityProfile` (storing password hash, MFA settings, login history).
- `security_profiles.user_id` → foreign key referencing `users.user_id`, UNIQUE constraint
- The security profile is separated for security isolation — restricting which applications can read the security table

## Common Mistakes

- **Ignoring many-to-many**: Storing `category_ids = "1,3,5"` in a single column instead of creating a junction table. This breaks SQL queries, violates normal form, and prevents foreign key constraints.
- **Misidentifying cardinality**: Assuming one-to-many when the requirement is actually many-to-many leads to a schema that cannot represent real-world data accurately.
- **Forgetting junction table attributes**: Junction tables often need their own attributes (enrolment date, role in a project, quantity ordered). Treat them as proper entities.
- **Missing mandatory/optional classification**: Not deciding participation constraints at design time leads to nullable foreign keys that cause incorrect query results through unexpected NULLs in joins.

## Mental Model

Think of relationships as rules in a club. "One membership card per person" is one-to-one. "One coach, many players" is one-to-many. "Players in many leagues; leagues with many players" is many-to-many — and the fixture schedule is the junction table: it records which player plays in which league, and might also record the date they joined, their position, and their ranking.

## Mini Summary

- ✔ Relationships connect entities and carry cardinality rules (1:1, 1:N, M:N)
- ✔ The foreign key lives on the "many" side of a one-to-many relationship
- ✔ Many-to-many relationships are resolved with a junction (associative) table
- ✔ Participation constraints (mandatory/optional) translate to NOT NULL / nullable foreign keys
- ✔ Junction tables can carry their own attributes and should be treated as entities

# Guided Practice Quest

Work through the guided steps to correctly classify relationship cardinalities, explain where foreign keys are placed, and describe how many-to-many relationships are resolved in practice.

# Solo Practice Quest

Design the relationships for a music streaming platform. The platform has these entities: `Artist`, `Album`, `Track`, `Playlist`, `User`, and `Genre`. For each relationship: name the two entities involved, state the cardinality, identify any junction tables needed, list any attributes on the junction table, and specify participation constraints (mandatory or optional on each side). Present your answer as a table, then write a paragraph explaining which relationship was the most complex to model and why.

# Integration

**Mathematics**: Relationships in data modelling are directly analogous to mathematical relations — subsets of the Cartesian product of two sets. A one-to-many relationship is a function (each element of the 'many' set maps to exactly one element of the 'one' set). A many-to-many relationship is a general binary relation — not a function, because one input can map to multiple outputs. This is precisely why relational databases, built on relational algebra, use junction tables to represent M:N — they decompose a non-functional relation into two functions.

**Sciences (Biology)**: Ecological food webs are a natural example of many-to-many relationships: one predator eats many prey species; each prey species is eaten by many predators. Bioinformatics databases model these using exactly the same junction table approach used in relational databases — a `predation` table with `predator_id` and `prey_id` foreign keys. This cross-domain parallel shows how the relational model captures universal patterns of connection.

# Lore Conclusion

Master Selvaris stood back and looked at the completed diagram — entities connected by lines annotated with cardinality markers. "Now the map has meaning," she said quietly. "We know not just what exists, but how everything is connected — and how many of each connection is permitted." She traced a path from merchant to caravan to town. "Every query we will ever write against this archive follows one of these lines. Design the lines well, and the knowledge flows freely. Design them carelessly, and every question becomes a struggle." She handed the apprentice a fresh piece of parchment. "Now — draw it again from memory."

---
