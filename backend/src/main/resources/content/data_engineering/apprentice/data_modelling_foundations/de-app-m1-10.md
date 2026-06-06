---
id: de-app-m1-10
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
lesson: common_modelling_mistakes
title: "Common Modelling Mistakes"
sortOrder: 5
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m1-06, de-app-m1-07, de-app-m1-08, de-app-m1-09]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Names and explains at least three common data modelling mistakes
    - Explains the real-world consequence of each mistake
    - Proposes a correct approach that avoids each mistake
    - Reflects on how these mistakes arise from cognitive shortcuts or time pressure
    - Connects at least one mistake to a normalisation concept
  keywords: [redundancy, normalisation, null, many-to-many, naming, overloading, god table, refactor, anomaly]
  modelAnswer: |
    Common data modelling mistakes include conflating entities (creating one large "god table"), storing multi-valued data in a single column, using non-descriptive or inconsistent naming, and failing to resolve many-to-many relationships with junction tables. These mistakes typically arise from time pressure or insufficient requirements analysis. Each creates data anomalies — inconsistencies that occur when inserting, updating, or deleting records — which become increasingly expensive to fix as the database grows. Learning to recognise these patterns early prevents weeks of refactoring later.
guidedSteps:
  - id: de-app-m1-10-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A developer stores customer addresses as "123 High Street|London|SW1A 1AA" in a single text column. What is the primary problem with this approach?
    inputConfig:
      options:
        - "The column name is too short"
        - "The data cannot be queried or filtered by individual components like city or postcode"
        - "Text columns are slower than integer columns"
        - "Addresses should be stored in a separate database"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The data cannot be queried or filtered by individual components like city or postcode"]
      rejectedFeedback: "Storing composite data in a single column violates the principle of atomicity. The combined address cannot be filtered by city, cannot be validated by postcode format, and cannot be sorted alphabetically by surname — all common query requirements."
    hint: "What happens when a business analyst wants to find all customers in London? Could they do that with this design?"
    reflectionPrompt: "How would you redesign the address storage to fix this problem?"
  - id: de-app-m1-10-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence: "A table that tries to represent too many different entity types or concepts in one place is sometimes called a ________ table."
    inputConfig:
      placeholder: "god"
    markingRule:
      matchMode: CONTAINS
      accepted: [god, blob, mega, fat, catch-all, universal]
      rejectedFeedback: "A 'god table' (or 'blob table') is an anti-pattern where one table attempts to store many different types of entities by using generic column names and many nullable fields. It is extremely difficult to query correctly and maintain over time."
    hint: "Think of a table that 'does everything' — what mythological title might you give it?"
    reflectionPrompt: "Why might a developer create a god table in the first place? What pressures or misunderstandings lead to this pattern?"
  - id: de-app-m1-10-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain what a data anomaly is and describe one type of anomaly that results from poor modelling.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [anomaly, insert, update, delete, redundancy, inconsistent, duplicate, normalisation]
      rejectedFeedback: "A data anomaly is an inconsistency that occurs when data is inserted, updated, or deleted in a poorly designed schema. An update anomaly occurs when the same fact is stored in multiple rows — changing it in one place without changing others creates contradictory data."
    hint: "Think about what happens when the same piece of information is stored in multiple places and one copy is updated but others are not."
    reflectionPrompt: "How does normalisation (organising data to reduce redundancy) prevent data anomalies?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the consequence of storing a list of values in a single column (e.g., tag_ids = '1,3,7')?"
    options:
      - "The column becomes faster to query"
      - "The data violates first normal form and cannot be reliably joined or filtered"
      - "The table requires fewer foreign keys"
      - "It is acceptable as long as the values are separated by commas"
    correctIndex: 1
    feedback: "Storing multiple values in one column violates First Normal Form (1NF). The data cannot be joined to the referenced table, cannot be filtered by individual values, and creates fragile parsing logic in application code."
  - type: MULTIPLE_CHOICE
    question: "Which of the following is an example of an update anomaly?"
    options:
      - "Deleting a customer accidentally deletes their orders"
      - "The same customer address is stored in multiple rows and one copy is updated incorrectly"
      - "A new order cannot be inserted because the customer does not yet exist"
      - "A NULL value appears in a mandatory column"
    correctIndex: 1
    feedback: "An update anomaly occurs when the same fact exists in multiple places and an update to one instance is not propagated to others, creating contradictory data in the same database."
retrieval:
  recall: "In one sentence, describe what a 'god table' anti-pattern is."
  explain: "Explain what a data anomaly is and give one example of an insert, update, or delete anomaly."
  mistakeId:
    code: "using generic column names like 'value1', 'value2', 'value3'"
    answer: "Generic column names make the schema impossible to understand without documentation, create confusion about what each column means, and often signal that an entity has been incorrectly modelled as a flat list rather than a proper structure."
---

# Hook

Even experienced engineers make data modelling mistakes. Some mistakes are classic — so common that they have names and entire chapters dedicated to them in database design textbooks. Others are subtle, hiding in schemas that look reasonable until the data grows to millions of rows and the cracks appear.

Learning to recognise **common modelling mistakes** early is one of the highest-value skills in data engineering. A mistake caught in a diagram costs minutes to fix. The same mistake discovered in a production database with five years of data costs weeks — or months — of painful migration work.

Have you ever inherited a messy spreadsheet where someone had jammed too much information into too few columns? You are already familiar with the pain these mistakes cause.

# Lore Introduction

Master Selvaris led her apprentice to a section of the Archive she rarely opened — a row of old ledgers bound in faded red. "These are the mistakes," she said, pulling one out. The pages were a chaos of crossed-out entries, inserted notes, and columns crammed with multiple values separated by slashes. "This was the work of an archivist who was in a hurry. He merged records that should have been separate. He stored three values in one column. He used the same table to record both merchants and caravans." She closed the ledger. "For fifty years, no one could query these records reliably. We still cannot correct them fully." She replaced the ledger firmly. "The time you save by modelling carelessly, you repay tenfold in confusion and lost trust."

# Core Learning

## Concept Introduction

| Mistake | Description | Consequence | Fix |
|---------|-------------|-------------|-----|
| **God Table** | One table represents many different entity types | Most columns are NULL for any given row; schema is incomprehensible | Split into proper entity tables |
| **Multi-valued Column** | Storing multiple values in one field ("1,3,7") | Violates 1NF; cannot be joined or filtered | Create a junction table |
| **Composite Column** | Storing compound data in one field ("John Smith") | Cannot query sub-parts independently | Split into atomic columns |
| **No Surrogate Key** | Using natural keys (e.g., email) as primary key | Natural keys change; references break | Add a surrogate integer/UUID key |
| **Inconsistent Naming** | Mixing conventions (customerId, customer_id, CUSTOMERID) | Code that reads the schema is error-prone | Agree and enforce a naming convention |
| **Missing Relationships** | Entities connected only in application code, not by foreign keys | Orphaned records; referential integrity violations | Add foreign key constraints |
| **Premature Denormalisation** | Duplicating data for "performance" before any measurement | Update anomalies; data drift | Normalise first; denormalise only with evidence |
| **Over-Nullable Schema** | Making every column nullable to avoid thinking | Data quality collapses; wrong NULLs everywhere | Decide mandatory vs optional at design time |

## Why It Matters

### The Three Data Anomalies

Data anomalies are inconsistencies that arise from poor normalisation:

- **Insert Anomaly**: Cannot insert a new record without also inserting unrelated data. Example: cannot add a new department to a poorly-designed employee table unless you have at least one employee in that department.
- **Update Anomaly**: The same fact stored in multiple rows — updating one without the others creates contradictions. Example: a customer's city stored on every order row; one order is updated, the others are not.
- **Delete Anomaly**: Deleting one record accidentally destroys unrelated information. Example: deleting the last employee in a department also deletes all knowledge of that department's existence.

Normalisation — organising data to reduce redundancy — prevents all three anomalies.

## Worked Examples

**Example 1: Multi-valued Column (Bad vs Good)**

Bad:
```
orders table:
order_id | product_ids
1        | "42,17,99"
```
Good:
```
order_lines table:
order_line_id | order_id | product_id | quantity
1             | 1        | 42         | 2
2             | 1        | 17         | 1
3             | 1        | 99         | 3
```

**Example 2: God Table (Bad vs Good)**

Bad:
```
entities table:
id | type       | name      | email            | address   | product_code | price
1  | customer   | Jane Doe  | jane@example.com | London    | NULL         | NULL
2  | product    | NULL      | NULL             | NULL      | PROD-42      | 29.99
```
Good: Separate `customers` and `products` tables with appropriate schemas.

**Example 3: Composite Column (Bad vs Good)**

Bad:
```
customers.full_address = "45 Park Lane, London, W1K 1QA"
```
Good:
```
customers.street = "45 Park Lane"
customers.city   = "London"
customers.postcode = "W1K 1QA"
```

## Common Mistakes

- **Optimising before measuring**: Engineers sometimes denormalise (introduce redundancy) for "performance" before the database is even built. This is premature optimisation — normalise first, then measure, then denormalise only where evidence demands it.
- **Using email as a primary key**: Emails change. Business rules change. Natural keys that seem stable often are not. Surrogate keys (auto-increment integers or UUIDs) are safer primary keys.
- **Naming columns generically**: `value1`, `field_a`, `extra_data` — columns that have no descriptive name create unmaintainable schemas. Every column should have a name that clearly communicates its content and purpose.

## Mental Model

Think of data modelling mistakes as architectural shortcuts: cutting a doorway through a load-bearing wall because it is faster than going around. The shortcut saves ten minutes today and causes the building to gradually collapse over the next decade. Every multi-valued column, every god table, every nullable key is a load-bearing wall that has been cut through — and the structural failure accumulates invisibly until something breaks catastrophically.

## Mini Summary

- ✔ God tables, multi-valued columns, and composite columns are the most common structural mistakes
- ✔ Data anomalies (insert, update, delete) arise from redundancy caused by poor normalisation
- ✔ Natural keys that seem stable often change — use surrogate keys for primary keys
- ✔ Naming conventions must be consistent across the entire schema
- ✔ Normalise first; denormalise only with measured evidence of performance need

# Guided Practice Quest

Work through the guided steps to identify common data modelling mistakes in given scenarios, explain the anomalies they cause, and propose correct alternatives.

# Solo Practice Quest

You have inherited the following poorly designed table from a previous engineer. Analyse every modelling mistake present, name each mistake, explain the consequence, and design a corrected schema to replace it. Write your analysis as a structured critique followed by a corrected ER description.

```
sales_data table:
id | salesperson_name | customer_names       | product_list   | total_sales | region | manager
1  | Alice Brown      | "Bob, Carol, Dave"   | "P1,P2,P3"     | 15000       | North  | James Smith
2  | Alice Brown      | "Eve"                | "P1"           | 3000        | North  | James Smith
3  | Carlos Diaz      | "Frank"              | "P4,P5"        | 7500        | South  | Maria Jones
```

Identify at least five distinct mistakes and explain the anomaly each would cause.

# Integration

**Psychology**: Cognitive psychologists identify a phenomenon called **premature closure** — the tendency to stop analysing a problem once a plausible-looking solution is found. Data modelling mistakes often arise from premature closure: the schema looks reasonable to the person who designed it, so they stop questioning it. Peer review and structured checklists (like the mistake catalogue in this lesson) are psychological tools for overcoming premature closure by forcing systematic re-examination.

**Mathematics**: The formal study of data normalisation is based on **functional dependencies** — a mathematical concept describing when one attribute's value determines another. First, Second, and Third Normal Form (1NF, 2NF, 3NF) are each defined as rules about functional dependencies. Understanding these forms mathematically helps engineers see *why* a given design creates anomalies, not just that it does — providing the reasoning needed to fix problems systematically rather than by intuition.

# Lore Conclusion

Master Selvaris replaced the red ledger in its place and dusted her hands. "Now you understand why we are slow and careful at the beginning," she said. "Speed is the enemy of good modelling. Every shortcut a young archivist takes is paid for, in time, by every archivist who comes after." She walked back toward the main reading room. "You will be tempted. Requirements will press. Deadlines will threaten. Remember these red ledgers." She paused at the door. "The greatest service you can do for those who follow you is to leave a clean, well-modelled archive behind."

---
