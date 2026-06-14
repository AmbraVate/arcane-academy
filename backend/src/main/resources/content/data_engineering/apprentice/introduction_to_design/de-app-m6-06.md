---
id: de-app-m6-06
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
lesson: third_normal_form
title: "Third Normal Form"
sortOrder: 6
difficulty: 3
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m6-05]
integrationDomains: [mathematics, software_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines Third Normal Form (3NF) in own words
    - Explains what a transitive dependency is with a concrete example
    - Distinguishes transitive dependencies from partial dependencies (2NF violations)
    - Demonstrates how to eliminate a transitive dependency by decomposition
    - Reflects on why 3NF is considered "good enough" for most practical database designs
  keywords: [3NF, transitive dependency, non-key attribute, decompose, functional dependency, intermediate, chain, normalise]
  modelAnswer: |
    Third Normal Form (3NF) requires that a table is in 2NF and that no non-key attribute is transitively dependent on the primary key through another non-key attribute. A transitive dependency is a chain: primary key → non-key attribute A → non-key attribute B, where B can be determined from A without using the primary key. Example: employees table with employee_id (PK) → department_id → department_name. Department_name transitively depends on employee_id via department_id. The fix is to move department_name to a departments table, referenced by department_id as a foreign key. Most practical designs aim for 3NF — it eliminates the most harmful redundancy while keeping the schema manageable.
guidedSteps:
  - id: de-app-m6-06-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      An `employees` table has: employee_id (PK), name, department_id, department_name, department_location. What type of dependency violation is this?
    inputConfig:
      options:
        - "Partial dependency (2NF violation) — department_name depends on part of a composite key"
        - "Transitive dependency (3NF violation) — department_name depends on department_id, not directly on employee_id"
        - "No violation — every column is necessary in the employees table"
        - "1NF violation — department_name is a repeating group"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Transitive dependency (3NF violation) — department_name depends on department_id, not directly on employee_id"]
      rejectedFeedback: "The chain is: employee_id → department_id → department_name. Department_name is determined by department_id, not by employee_id itself — it is transitively dependent. This is a 3NF violation. It is NOT a partial dependency (the key is not composite). The fix: create a departments table (department_id PK, name, location) and store only department_id in employees."
    hint: "The key is: employee_id → department_id → department_name. The name depends on department_id, not directly on the employee."
    reflectionPrompt: "How many times would 'Engineering' be stored if there are 50 engineers in the department? What happens if the department is renamed?"
  - id: de-app-m6-06-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In a 3NF violation, a non-key attribute depends on another ________ attribute rather than directly on the primary key.
    inputConfig:
      placeholder: "non-key"
    markingRule:
      matchMode: CONTAINS
      accepted: [non-key, "non-key attribute", nonkey]
      rejectedFeedback: "A transitive dependency means: PK → non-key attribute A → non-key attribute B. B depends on A (a non-key attribute), not on the PK directly. If B depended on the PK directly, it would be fine. If B depended on part of a composite PK, it would be a partial dependency (2NF violation). Transitive dependence specifically involves a chain through a non-key intermediate."
    hint: "The dependency chain goes through an intermediate attribute that is not part of the primary key."
    reflectionPrompt: "Can a table violate 3NF without violating 2NF? Give an example."
  - id: de-app-m6-06-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain what update anomaly occurs when department_name is stored in the employees table, and how 3NF normalisation eliminates it.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [update, rename, all rows, inconsistent, multiple, one place, departments, separate, anomaly]
      rejectedFeedback: "Update anomaly: if the Engineering department is renamed to Product Engineering, every employee row where department_name = 'Engineering' must be updated. If any row is missed (e.g., during a partial update), the database contains two different names for the same department — inconsistency. After 3NF normalisation, the department name is stored in exactly one row in the departments table. Renaming it requires updating one row, and all employees that reference that department_id automatically see the new name."
    hint: "Think about what happens when a department is renamed and department_name is stored in 50 employee rows."
    reflectionPrompt: "What is the insertion anomaly? Can you add a new department if no employees belong to it yet, in the non-normalised design?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of these is a transitive dependency in an orders table?"
    options:
      - "order_id → customer_id (customer_id depends on the order's PK)"
      - "order_id → customer_id → customer_city (customer_city depends on customer_id, not on order_id)"
      - "order_id → order_date (order_date depends on the PK)"
      - "(order_id, product_id) → product_name (product_name depends on part of a composite key — a 2NF violation)"
    correctIndex: 1
    feedback: "Option B is a transitive dependency: order_id → customer_id → customer_city. Customer_city is determined by customer_id (a non-key attribute), not by order_id directly. Storing customer_city in orders means every order for a customer repeats the city, and changing the city requires updating every order row. Fix: store customer_city in the customers table. Option D is a partial dependency (2NF violation), not transitive."
  - type: MULTIPLE_CHOICE
    question: "How do you fix a transitive dependency?"
    options:
      - "Add the dependent column to the primary key"
      - "Remove the intermediate column entirely"
      - "Move the transitively dependent attributes to a separate table keyed by the intermediate attribute"
      - "Convert the primary key to a composite key"
    correctIndex: 2
    feedback: "The fix is decomposition: move the transitively dependent attributes to a new table where the intermediate column becomes the primary key. Example: move department_name and department_location to a departments table keyed by department_id. The employees table retains department_id as a foreign key. Now department_name is stored once, and the transitive chain is resolved."
retrieval:
  recall: "Identify and fix the 3NF violation in this table: orders(order_id, customer_id, customer_name, customer_city, order_date, total_amount)."
  explain: "Explain the difference between a partial dependency (2NF violation) and a transitive dependency (3NF violation) using different examples."
  mistakeId:
    code: "storing zip_code and city in an addresses table where city is determined by zip_code"
    answer: "zip_code → city is a transitive dependency: city is determined by zip_code (a non-key column), not directly by address_id. Storing city redundantly means: (1) every address with the same zip_code stores the same city — redundancy; (2) if a zip_code moves from one city to another, many rows need updating. The 3NF fix is to create a zip_codes table (zip_code PK, city, state) and reference it from addresses by zip_code. City is then stored once per zip code."
---

# Hook

You have learned that 1NF eliminates repeating groups, and 2NF eliminates partial dependencies (attributes that depend on only part of a composite key). Third Normal Form goes one step further: it eliminates dependencies that chain through non-key attributes.

3NF is the practical target for most database designs. A table in 3NF is free from the three most damaging forms of data redundancy.

# Lore Introduction

"The employees ledger stores department names in every employee row," the Archivist said. "The Engineering department has 47 employees — the name 'Engineering' appears 47 times." Master Selvaris examined it. "Transitive dependency," she said. "Employee ID points to department ID, and department ID points to department name. The name depends on the department, not on the employee." She drew the chain: `employee_id → department_id → department_name`. "When we rename 'Engineering' to 'Product Engineering', we must update 47 rows. Miss one, and we have two names for the same department." She decomposed it. "Now department_name lives in a departments table, one row per department. Rename once, every employee sees the new name automatically."

# Core Learning

## Concept Introduction

### What is a Transitive Dependency?

A transitive dependency is a chain: **PK → non-key column A → non-key column B**

Column B can be determined from column A, without needing the PK directly.

```
employees table:
employee_id (PK) | name | department_id | department_name | department_location

Chain: employee_id → department_id → department_name
                                   → department_location

department_name and department_location depend on department_id (a non-key attribute)
not directly on employee_id. This is a 3NF violation.
```

### The Problem: Update Anomaly

```
Engineering department has 47 employees → "Engineering" appears 47 times

Rename to "Product Engineering":
→ Must update 47 rows
→ If one is missed: two different names for the same department
→ Inconsistency
```

### The Fix: Decomposition

Move the transitively dependent attributes to a new table keyed by the intermediate column:

```sql
-- BEFORE (3NF violation):
employees: employee_id | name | department_id | department_name | department_location

-- AFTER (3NF compliant):
employees:   employee_id | name | department_id (FK)
departments: department_id (PK) | department_name | department_location
```

Now:
- `department_name` is stored exactly once per department
- Renaming requires updating one row in `departments`
- All employees referencing that `department_id` see the updated name automatically

### Transitive vs Partial Dependency

```
Partial dependency (2NF violation):
  - Only occurs with composite primary keys
  - A non-key attribute depends on PART of the composite key
  - Example: (student_id, course_id) → student_name
    student_name depends on student_id only, not the full (student_id, course_id) key

Transitive dependency (3NF violation):
  - Occurs with any key (simple or composite)
  - A non-key attribute depends on ANOTHER non-key attribute
  - Example: employee_id → department_id → department_name
    department_name depends on department_id (non-key), not on employee_id (PK)
```

### Another Common Example: Zip Codes

```sql
-- BEFORE (3NF violation):
orders: order_id | customer_id | zip_code | city | state

Chain: order_id → zip_code → city → state
       city and state depend on zip_code, not on order_id

-- AFTER (3NF compliant):
orders:    order_id | customer_id | zip_code (FK)
zip_codes: zip_code (PK) | city | state
```

### The Three Normal Forms Together

```
1NF: No repeating groups. Atomic values. Each row is unique.
     "No arrays or multi-valued columns."

2NF: In 1NF + No partial dependencies.
     "Every non-key attribute depends on the WHOLE primary key."
     (Only relevant when composite key exists)

3NF: In 2NF + No transitive dependencies.
     "Every non-key attribute depends DIRECTLY on the primary key,
     not through another non-key attribute."
```

### Three Anomalies Solved by Normalisation

| Anomaly | Problem | 3NF Solution |
|---|---|---|
| Update | Change department name in 47 rows | Change in 1 row in departments |
| Insertion | Can't add a new department without an employee | departments table can hold any department |
| Deletion | Deleting the last employee deletes the department | departments table retains the record |

## Why It Matters

Third Normal Form is the standard the industry actually checks designs against — "is this 3NF?" is shorthand for "is this schema trustworthy?"

- Transitive dependencies are sneaky: a city column that depends on a postcode column *through* the customer creates silent duplication
- Every duplicated fact is a future inconsistency — update one copy, forget the other, and the database now contains two truths
- 3NF gives you a mechanical test to find these problems instead of relying on intuition

It is also the pragmatic stopping point: most production schemas aim for 3NF, so this is the level of rigour working engineers are expected to apply.

## Common Mistakes

- **Confusing 2NF and 3NF violations**: Partial dependencies (2NF) require a composite key. Transitive dependencies (3NF) can occur with any key. If the key is simple (single column), only 3NF violations are possible — 2NF violations cannot occur.
- **Over-normalising**: Not every derived or related attribute must be extracted. If city and state are only ever used in context of the order and never change independently, keeping them in orders may be appropriate. Normalisation is a tool, not a law.
- **Forgetting to add the FK after decomposition**: After moving department_name to departments, add `department_id` as a foreign key in employees. Without it, the link between the tables is lost.

## Mental Model

Think of 3NF as the "single source of truth" rule for non-key attributes. Every piece of information about a department should live in the departments table. Every piece of information about a customer should live in the customers table. If you find yourself typing the same city name, department name, or country name in multiple rows of a table, ask: "does this belong in a separate table?" If the answer is yes, that is a transitive dependency waiting to be fixed.

## Mini Summary

- ✔ 3NF: every non-key attribute depends directly on the PK, not through another non-key attribute
- ✔ Transitive chain: PK → non-key A → non-key B violates 3NF
- ✔ Fix: move the transitively dependent attributes to a new table keyed by the intermediate column
- ✔ Solves update, insertion, and deletion anomalies
- ✔ 1NF → 2NF → 3NF is the standard normalisation progression for practical database design

# Guided Practice Quest

Work through the guided steps to identify transitive dependencies, distinguish them from partial dependencies, and decompose a 3NF-violating table into two correctly normalised tables.

# Solo Practice Quest

Normalise the following denormalised table to 3NF: `student_courses(enrolment_id, student_id, student_name, student_email, course_id, course_name, department_id, department_name, instructor_id, instructor_name, instructor_office, grade, enrolment_date)`. Work through each normal form step: (1) confirm it is in 1NF, (2) identify and fix any 2NF violations, (3) identify and fix all 3NF violations. For each fix, write the resulting table schemas (column names and primary/foreign keys), explain the dependency that was violated, and describe what update anomaly the fix prevents. Show the complete 3NF schema at the end with all tables and their key structures.

# Integration

**Mathematics**: 3NF is defined in terms of functional dependencies. A relation schema R with set of functional dependencies F is in 3NF if, for every non-trivial functional dependency X → A in F⁺ (the closure of F), either X is a superkey of R, or A is a prime attribute (part of some candidate key). Armstrong's axioms (reflexivity, augmentation, transitivity) define the closure F⁺. A transitive dependency violates the condition that X must be a superkey: if A → B and A is not a superkey, then the schema is not in 3NF. This formal definition, due to Codd (1971), precisely captures the intuition that every non-key attribute must be "about" the key and nothing else.

**Software Engineering**: Database normalisation directly parallels the Single Responsibility Principle in software design: each class (or table) should have one responsibility. A table that stores both employee data and department data has two responsibilities — employee records and department directory. Just as a class violates SRP when it changes for two different reasons, a table violates 3NF when it needs to be updated for two different reasons (employee name change vs department rename). Both fixes — extracting a class, extracting a table — involve decomposition and reference (dependency injection / foreign key). The structural analogy between OOP and relational design is intentional: both draw on the same mathematical foundations.

# Lore Conclusion

"Three normal forms," the Apprentice said, reviewing the completed schema. "1NF for atomic values and unique rows. 2NF for full key dependence. 3NF for direct dependence — no chains through non-key attributes." Master Selvaris nodded. "And 3NF solves the three anomalies: update, insertion, deletion. Rename a department once. Add a new department without any employees. Delete an employee without deleting their department." She closed the design document. "Most real databases target 3NF. Beyond it — BCNF, 4NF, 5NF — exists for special cases. For the work you will do at this stage of your career, a clean 3NF schema is the right target." She handed back the completed design. "Learn to spot transitive dependencies. They are the most common normalisation problem you will encounter."

---
