---
id: de-app-m6-03
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
lesson: introduction_to_normalisation
title: "Introduction to Normalisation"
sortOrder: 3
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m6-02]
integrationDomains: [mathematics, software_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines normalisation and states its primary goal
    - Explains what a Normal Form is and why there are multiple levels
    - Distinguishes between unnormalised, 1NF, 2NF, and 3NF at a conceptual level
    - Explains what a functional dependency is
    - Reflects on why normalisation is applied progressively rather than all at once
  keywords: [normalisation, normal form, 1NF, 2NF, 3NF, functional dependency, redundancy, anomaly]
  modelAnswer: |
    Normalisation is a systematic process of structuring a relational database to reduce redundancy and prevent data anomalies. It is applied in stages called Normal Forms (1NF, 2NF, 3NF, and beyond), each addressing a specific category of redundancy. A functional dependency exists when one attribute's value determines another's (e.g., student_id determines student_name). 1NF eliminates repeating groups and multi-valued columns. 2NF removes partial dependencies (non-key attributes depending on only part of a composite key). 3NF removes transitive dependencies (non-key attributes depending on other non-key attributes). The process is progressive because each form builds on the guarantees of the previous one.
guidedSteps:
  - id: de-app-m6-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the primary purpose of database normalisation?
    inputConfig:
      options:
        - "To make queries run faster by reducing the number of tables"
        - "To reduce redundancy and prevent data anomalies by structuring data correctly"
        - "To increase the number of columns per table for easier reading"
        - "To compress data and reduce storage space"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["To reduce redundancy and prevent data anomalies by structuring data correctly"]
      rejectedFeedback: "Normalisation's primary goal is reducing redundancy and preventing anomalies through systematic structural design — not performance, compression, or ease of reading."
    hint: "Think about the problems identified in the previous lesson — what is normalisation designed to solve?"
    reflectionPrompt: "If normalisation prevents anomalies but requires more JOIN operations, is it always the right approach?"
  - id: de-app-m6-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "When the value of one attribute uniquely determines the value of another attribute, we say there is a ________ dependency between them."
    inputConfig:
      placeholder: "functional"
    markingRule:
      matchMode: CONTAINS
      accepted: [functional, function]
      rejectedFeedback: "A functional dependency (FD) exists when attribute A determines attribute B — knowing A's value tells you B's value. For example, student_id → student_name."
    hint: "This term describes a relationship where one attribute's value determines another's, like how a student ID determines a student's name."
    reflectionPrompt: "Can you identify two functional dependencies in a simple library database?"
  - id: de-app-m6-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why normalisation is applied in progressive stages (1NF, then 2NF, then 3NF) rather than all at once.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [progressive, stage, form, build, prerequisite, incremental]
      rejectedFeedback: "Each normal form addresses a different category of redundancy and builds on the guarantees of the previous form. A table must satisfy 1NF before 2NF can be applied, because 2NF assumes atomic columns. This incremental approach makes the process tractable and verifiable at each step."
    hint: "What must be true before you can apply 2NF? What must 2NF satisfy before 3NF makes sense?"
    reflectionPrompt: "What would happen if you tried to apply 3NF rules directly to an unnormalised table?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which statement correctly describes a functional dependency?"
    options:
      - "Two columns have the same data type"
      - "One attribute's value uniquely determines another attribute's value"
      - "A column references a row in another table"
      - "Two tables are joined on a shared column"
    correctIndex: 1
    feedback: "A functional dependency A → B means that knowing the value of A is sufficient to determine the value of B. This is the core concept behind normalisation."
  - type: MULTIPLE_CHOICE
    question: "What does it mean for a table to be in a higher Normal Form?"
    options:
      - "It has more columns than a lower normal form table"
      - "It has been indexed for faster queries"
      - "It satisfies all the constraints of all lower normal forms plus additional ones"
      - "It has been backed up and restored successfully"
    correctIndex: 2
    feedback: "Normal forms are cumulative — 3NF implies 2NF which implies 1NF. Each higher form adds constraints that eliminate additional categories of redundancy."
retrieval:
  recall: "Define normalisation in one sentence and name the first three Normal Forms."
  explain: "What is a functional dependency and why is it central to the normalisation process?"
  mistakeId:
    code: "normalisation always means splitting tables until everything is in separate tables"
    answer: "Normalisation eliminates specific categories of redundancy defined by functional dependencies — not all possible redundancy. The goal is to reach 3NF (or appropriate form for the use case), not to split tables as finely as possible. Over-normalisation can make queries unnecessarily complex."
---

# Hook

In the previous lesson, you saw how redundancy creates anomalies. But how do you systematically eliminate redundancy from a real database design? "Remove redundancy" is good advice, but vague. You need a method — a series of checkpoints you can apply to any schema to verify it is well-structured.

Normalisation is that method. Developed by Edgar Codd alongside the relational model in the 1970s, it provides a progressive sequence of rules — called Normal Forms — that each eliminate a specific category of structural problem. By applying them in order, you arrive at a schema that is clean, consistent, and free from the anomalies that plague poorly designed databases.

In the next three lessons, you will learn the first three Normal Forms in detail. First, you need to understand the overall framework and the core concept that drives all of normalisation: functional dependencies.

# Lore Introduction

Master Selvaris opened a thick academic volume. "The founders of the Archive did not stumble upon good design through trial and error," he said. "They codified it. They asked: what are the precise conditions a schema must meet to be free of update anomalies? Free of insert anomalies? Free of delete anomalies?" He set the book down. "Their answers became the Normal Forms — a sequence of tests, each stricter than the last. Pass all three and your schema is, by mathematical proof, free of the most damaging redundancy." He picked up a piece of parchment. "Let us begin with the concept that underpins all of them: functional dependency."

# Core Learning

## Concept Introduction

| Concept | Definition | Example |
|---------|-----------|---------|
| **Normalisation** | Systematic process of structuring tables to reduce redundancy | Applying 1NF, 2NF, 3NF in sequence |
| **Normal Form (NF)** | A set of rules a table must satisfy to eliminate a specific type of redundancy | 1NF, 2NF, 3NF, BCNF, 4NF... |
| **Functional Dependency** | Attribute A determines attribute B: knowing A gives you B | `student_id → student_name` |
| **Partial Dependency** | A non-key attribute depends on only part of a composite key | In a composite key (student_id, course_id), if student_name depends only on student_id |
| **Transitive Dependency** | A non-key attribute depends on another non-key attribute | `zip_code → city` when neither is the primary key |
| **Unnormalised Form** | A table with repeating groups or multi-valued fields | A column containing "Widget, Gadget, Thingamajig" |

## Why It Matters

Without a systematic approach:
- Engineers make inconsistent decisions about where to put each fact
- Some tables are over-normalised, others are not at all
- The schema grows inconsistently as new engineers make different assumptions
- Anomalies accumulate because no one can systematically verify the design

Normalisation provides a shared, verifiable standard that any engineer can apply and check.

## Worked Examples

**Example 1: Functional dependencies in a student database**
```
student_id → student_name    (knowing the ID gives you the name)
student_id → student_email   (knowing the ID gives you the email)
course_id  → course_name     (knowing the course ID gives you the name)
(student_id, course_id) → grade  (knowing both gives you the grade)
```

**Example 2: Unnormalised table**
```
| student_id | student_name | courses_enrolled     |
|------------|-------------|----------------------|
| 101        | Alice        | Math, English, Art   |
| 102        | Bob          | Science, Math        |
```
`courses_enrolled` is multi-valued — this violates 1NF.

**Example 3: The three Normal Forms in brief**
- **1NF**: Eliminate repeating groups and multi-valued columns — all values must be atomic
- **2NF**: Eliminate partial dependencies — every non-key attribute must depend on the whole primary key
- **3NF**: Eliminate transitive dependencies — non-key attributes must depend directly on the primary key, not on other non-key attributes

## Common Mistakes

- **Skipping directly to 3NF**: Each NF assumes the previous one is satisfied. Applying 3NF to a table that is not yet in 1NF may appear to work but leaves structural problems.
- **Confusing normalisation with physical optimisation**: Normalisation is about logical structure and data integrity, not query performance.
- **Treating NFs as bureaucratic rules**: Each form solves a real problem (anomaly category). Understanding which anomaly each form prevents makes the rules intuitive rather than arbitrary.

## Mental Model

Think of Normal Forms as a series of health checks for a database schema. 1NF is the basic health check: is everything properly formed? 2NF is the secondary check: are all the right things in the right places? 3NF is the specialist check: are there hidden dependencies causing indirect redundancy? Each check is only meaningful after the previous one passes.

## Mini Summary

- ✔ Normalisation systematically eliminates redundancy through progressive Normal Forms
- ✔ Each Normal Form addresses a specific type of dependency-based redundancy
- ✔ Functional dependency A → B means knowing A determines B
- ✔ 1NF: atomic values; 2NF: no partial dependencies; 3NF: no transitive dependencies
- ✔ Forms are cumulative — each NF implies all lower NFs

# Guided Practice Quest

Work through the guided steps to identify functional dependencies in a sample schema and determine which Normal Form a given table satisfies.

# Solo Practice Quest

Create an unnormalised table for a school that stores: `student_id`, `student_name`, `teacher_name`, `teacher_email`, `courses` (a comma-separated list), `room_number`, `room_capacity`. List all the functional dependencies you can identify. Then describe what changes would bring the table to 1NF. What additional change would bring it to 2NF? And then 3NF? For each step, write or describe the resulting tables.

# Integration

**Mathematics**: Functional dependency is directly borrowed from mathematics, where a function maps each input to exactly one output. In database theory, A → B means the mapping from A's values to B's values is functional — each value of A maps to exactly one value of B. The entire theory of normalisation rests on this mathematical concept.

**Software Engineering**: The Single Responsibility Principle (SRP) states that each module should have one reason to change. Applied to tables: each table should model one entity or relationship. When a table has multiple responsibilities (stores both student data and course data), it violates SRP — a change to courses requires touching the same table as a change to students. Normalisation enforces SRP at the data layer.

# Lore Conclusion

Master Selvaris placed a series of stamps on the desk — three different seals, each more intricate than the last. "Every scroll entering the Archive must earn these three seals," he said. "The first seal: each field contains exactly one piece of information. The second: each piece belongs fully to the scroll's subject. The third: no piece depends on another piece that is not itself the subject." He lined them up precisely. "These are the three normal forms. They are not bureaucracy. Each one solves a problem the predecessors discovered by painful experience." He offered you the first seal. "Begin with the first. Everything else follows from it."

---
