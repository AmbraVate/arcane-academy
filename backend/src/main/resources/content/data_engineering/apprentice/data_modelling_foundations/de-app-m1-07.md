---
id: de-app-m1-07
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
lesson: attributes
title: "Attributes"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m1-06]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines attribute clearly in the context of data modelling
    - Distinguishes simple from composite attributes with examples
    - Explains what a derived attribute is and why it should not always be stored
    - Identifies the role of the key attribute as a special type of attribute
    - Reflects on how attribute selection affects data quality and storage efficiency
  keywords: [attribute, property, column, simple, composite, derived, key, null, domain, constraint]
  modelAnswer: |
    An attribute is a property or characteristic that describes an entity — for example, a Customer entity has attributes like name, email address, and date of birth. Attributes can be simple (a single value), composite (made up of sub-parts like a full address), or derived (calculated from other attributes like age from date of birth). Choosing the right attributes and their constraints is critical for data quality: over-collecting leads to storage waste, while missing attributes means important information cannot be recorded.
guidedSteps:
  - id: de-app-m1-07-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A customer's "date of birth" is stored. From this, the customer's "age" is calculated by the application. What type of attribute is "age"?
    inputConfig:
      options:
        - "Simple attribute"
        - "Composite attribute"
        - "Derived attribute"
        - "Key attribute"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Derived attribute"]
      rejectedFeedback: "A derived attribute is one that can be calculated from other stored data. Since age can always be computed from date_of_birth and today's date, storing age separately creates redundancy and the risk of it becoming out of sync."
    hint: "This attribute can be computed from another attribute that is already stored."
    reflectionPrompt: "What is the risk of storing a derived attribute like 'age' in the database rather than calculating it at query time?"
  - id: de-app-m1-07-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence: "An attribute that can be divided into smaller meaningful sub-parts — such as a full name split into first name and last name — is called a ________ attribute."
    inputConfig:
      placeholder: "composite"
    markingRule:
      matchMode: CONTAINS
      accepted: [composite, compound, multi-valued]
      rejectedFeedback: "A composite attribute is one that can be broken down into smaller meaningful components. 'Full Name' can be split into first_name and last_name. Whether to store it composite or atomic depends on whether you need to query the sub-parts independently."
    hint: "This type of attribute is made up of multiple smaller parts that each have meaning on their own."
    reflectionPrompt: "When would you choose to store a full name as one attribute versus splitting it into first and last name?"
  - id: de-app-m1-07-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why it is important to define constraints on attributes (such as NOT NULL or maximum length) at the data modelling stage rather than leaving it to the application layer.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [constraint, null, invalid, quality, enforce, database, integrity, validation]
      rejectedFeedback: "Database-level constraints are enforced regardless of which application inserts data. Application-layer validation can be bypassed, updated inconsistently, or absent when data arrives via bulk imports or direct SQL."
    hint: "Think about what happens when data is inserted by multiple different applications or scripts."
    reflectionPrompt: "Can you think of a real-world example where a missing NOT NULL constraint caused a problem?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which type of attribute uniquely identifies each instance of an entity?"
    options: ["Simple attribute", "Composite attribute", "Key attribute", "Derived attribute"]
    correctIndex: 2
    feedback: "A key attribute (or identifier) uniquely identifies each entity instance — for example, customer_id or product_sku. It is the basis for the primary key in a relational table."
  - type: MULTIPLE_CHOICE
    question: "A 'full_address' attribute that contains street, city, and postcode in one field is an example of:"
    options: ["A key attribute", "A derived attribute", "A composite attribute", "A multivalued attribute"]
    correctIndex: 2
    feedback: "A composite attribute is made up of meaningful sub-parts. Storing address as one field prevents efficient querying and filtering by city or postcode — best practice is to store each sub-part separately."
retrieval:
  recall: "In one sentence, define what an attribute is in data modelling."
  explain: "Explain the difference between a simple attribute and a composite attribute, and give one example where you would deliberately choose to keep a composite attribute atomic."
  mistakeId:
    code: "store every piece of information you might ever need"
    answer: "Over-collecting attributes creates storage overhead, maintenance burden, and privacy risk. Only store attributes that serve a defined purpose aligned with the system's requirements — this is the principle of data minimisation."
---

# Hook

Once you know *what* things your data model needs to represent, the next question is: *what do you need to know about each thing?* A customer is not just an abstract concept — they have a name, an email address, a date of birth, a home address, and an account status. These properties are what make each customer unique and useful to work with.

In data modelling, these properties are called **attributes**. Choosing the right attributes — and defining them precisely — is what transforms a vague entity into a well-designed table. Get it right, and your data is clean, queryable, and efficient. Get it wrong, and you spend years cleaning up concatenated names, redundant columns, and unmaintainable derived values.

Before reading on: think of a `Product` entity. What attributes does it need? How many can you list in 30 seconds?

# Lore Introduction

"You have named the entities," Master Selvaris said, tapping the circles on the parchment. "Now describe them. What does the Archive need to know about each merchant?" She began writing alongside one of the circles: *name... guild... city... licence number...* "Each fact about a thing is what we call an attribute. But be careful — not every fact you could record is worth recording. Only the attributes that serve the Archive's purpose deserve a column in our ledger." She paused, quill in hand. "And some facts can be derived from others — age from date of birth, total from price and quantity. We do not copy those. We calculate them when needed. The ledger must be lean."

# Core Learning

## Concept Introduction

| Attribute Type | Definition | Example | Stored? |
|---------------|-----------|---------|---------|
| **Simple** | Single, indivisible value | `email`, `price`, `quantity` | Yes |
| **Composite** | Made up of meaningful sub-parts | Full address → `street`, `city`, `postcode` | Store as sub-parts |
| **Derived** | Calculated from other stored attributes | `age` from `date_of_birth`; `order_total` from lines | Usually not stored |
| **Key (Identifier)** | Uniquely identifies each entity instance | `customer_id`, `product_sku` | Yes (required) |
| **Multivalued** | Can hold multiple values for one entity | A person's multiple phone numbers | Store in a separate table |
| **Optional (Nullable)** | May or may not have a value for a given instance | `middle_name`, `company_name` | Yes, with NULL allowed |
| **Mandatory (NOT NULL)** | Must always have a value | `date_of_birth`, `email` | Yes, with NOT NULL constraint |

## Why It Matters

Attribute design decisions have direct consequences:

- **Composite attributes stored as one field** (e.g., `"123 Main Street, London, SW1A 1AA"`) cannot be queried by city or postcode without expensive string parsing
- **Derived attributes stored redundantly** (e.g., storing both `date_of_birth` and `age`) become inconsistent as time passes and updates are missed
- **Missing key attributes** make it impossible to uniquely identify records, leading to duplicates
- **Missing constraints** (NOT NULL, CHECK, UNIQUE) allow invalid data to enter the system silently

## Worked Examples

**Example 1: Customer Entity Attributes**

| Attribute | Type | Constraint | Notes |
|-----------|------|-----------|-------|
| `customer_id` | Key | NOT NULL, UNIQUE | Auto-generated integer |
| `first_name` | Simple | NOT NULL | Max 100 chars |
| `last_name` | Simple | NOT NULL | Max 100 chars |
| `email` | Simple | NOT NULL, UNIQUE | Validated format |
| `date_of_birth` | Simple | NOT NULL | Date type |
| `age` | Derived | — | Calculate: `CURRENT_DATE - date_of_birth`; do not store |
| `phone_number` | Multivalued | Nullable | Stored in separate `customer_phones` table |
| `street`, `city`, `postcode` | Composite (split) | Nullable | Address split into queryable columns |

**Example 2: Product Entity Attributes**

| Attribute | Type | Constraint |
|-----------|------|-----------|
| `product_id` | Key | NOT NULL, UNIQUE |
| `product_name` | Simple | NOT NULL |
| `sku` | Simple | NOT NULL, UNIQUE |
| `unit_price` | Simple | NOT NULL, CHECK > 0 |
| `stock_quantity` | Simple | NOT NULL, DEFAULT 0 |
| `weight_kg` | Simple | Nullable |
| `category_id` | Foreign Key | NOT NULL |

## Common Mistakes

- **Storing full name as one field**: `"John Smith"` in a single column prevents searching by surname, generates sorting problems, and makes it impossible to personalise salutations properly. Always split name fields.
- **Storing calculated values**: `age`, `full_name` (concatenated), `order_total` — if these can be calculated at query time, do not store them. They will drift out of sync.
- **No key attribute**: An entity without a unique identifier cannot be reliably referenced, updated, or deleted. Every entity needs one.
- **Everything nullable**: Making all attributes nullable feels flexible but produces messy data. Decide at design time what is truly optional versus mandatory.

## Mental Model

Think of an entity as a paper form. Each field on the form is an attribute. The form has a serial number at the top (the key attribute). Some fields are required — you cannot submit the form without filling them in (NOT NULL). Some are optional (nullable). Some fields you might consider putting on the form, but you can calculate them from other fields (derived) — those are redundant. A good form designer is ruthless about which fields are truly necessary.

## Mini Summary

- ✔ Attributes are properties or characteristics that describe an entity
- ✔ Simple attributes hold a single value; composite attributes should be split into sub-parts
- ✔ Derived attributes can be calculated from stored data and usually should not be stored
- ✔ Every entity needs a key attribute to uniquely identify instances
- ✔ Constraints (NOT NULL, UNIQUE, CHECK) enforce data quality at the database level

# Guided Practice Quest

Work through the guided steps to classify different attribute types and explain why key constraints and the avoidance of derived storage are important data engineering principles.

# Solo Practice Quest

Design the full attribute set for three entities from a cinema booking system: `Film`, `Customer`, and `Booking`. For each entity: list all attributes, classify each as simple, composite, derived, key, or multivalued, specify whether each is mandatory or optional, and explain your reasoning for each decision. Include at least one derived attribute per entity and explain why you would not store it. Write your answer as a structured table with a commentary section.

# Integration

**Mathematics**: In mathematics, a function maps each element of a domain to exactly one value in a codomain. An attribute is precisely this: it maps each entity instance (domain) to a value in its defined domain (codomain). The uniqueness constraint on a key attribute mirrors the concept of an injective function — every input maps to a unique output. Understanding attributes as functions helps clarify when NOT NULL constraints, UNIQUE constraints, and foreign keys are mathematically necessary.

**Psychology**: In cognitive psychology, the theory of "prototypes" holds that people recognise category members by comparing them to a prototype — a bundle of typical attributes. When data modellers struggle to list attributes, they are often struggling to define the prototype for an entity. Asking "what does a typical X look like?" is a useful elicitation technique that maps directly onto defining the attribute set for an entity type.

# Lore Conclusion

Master Selvaris surveyed the completed attribute list for the merchant entity. "Well chosen," she said. "You have included what is needed and left out what can be computed. You have split what was compound and constrained what must not be empty." She marked several cells with a small check. "An attribute is not a storage unit — it is a promise. A promise that every record of this type will carry this piece of truth, reliably and consistently." She looked up. "Now you understand why we are so precise about what we record — and what we do not."

---
