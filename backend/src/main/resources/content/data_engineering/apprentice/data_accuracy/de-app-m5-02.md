---
id: de-app-m5-02
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m5
moduleTitle: "Module 5: Data Quality"
moduleGlyph: "✅"
moduleSortOrder: 5
topicSlug: data_accuracy
topicTitle: "Data Accuracy"
topicSortOrder: 1
lesson: validation_rules
title: "Validation Rules"
sortOrder: 2
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m5-01]
integrationDomains: [mathematics, software_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines a validation rule and explains its purpose
    - Identifies at least three types of validation rule with examples
    - Explains where validation should be applied in a data system
    - Describes what happens when validation fails and how to handle it
    - Reflects on the trade-off between strict and lenient validation
  keywords: [validation, constraint, rule, format, range, type, reject, error]
  modelAnswer: |
    A validation rule is a condition that data must satisfy before it is accepted into a system. Types include type checks (a birthdate must be a date), range checks (age must be between 0 and 150), format checks (email must match a pattern), and referential checks (a foreign key must exist in the parent table). Validation should be applied at ingestion, in application logic, and at the database level. When validation fails, the system should reject the record, log the failure, and alert the appropriate team rather than silently storing bad data.
guidedSteps:
  - id: de-app-m5-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A database column for "age" accepts the value -5. Which type of validation rule was missing?
    inputConfig:
      options:
        - "Type check — the column should only accept integers"
        - "Range check — the value must fall within an acceptable range"
        - "Format check — the value must match a specific pattern"
        - "Uniqueness check — the value must not already exist"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Range check — the value must fall within an acceptable range"]
      rejectedFeedback: "-5 is technically an integer, so a type check would pass. A range check (e.g., 0–150) would catch this impossible value."
    hint: "The value is of the right type but outside any realistic range. What kind of rule enforces boundaries?"
    reflectionPrompt: "What other fields in a typical database would need range validation?"
  - id: de-app-m5-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "A validation rule that checks whether an email address contains '@' and '.' is called a ________ check."
    inputConfig:
      placeholder: "format"
    markingRule:
      matchMode: CONTAINS
      accepted: [format, pattern, regex, structure]
      rejectedFeedback: "Format or pattern checks verify that a value conforms to an expected structure, often using regular expressions."
    hint: "This type of check looks at the shape or structure of the value, not its numeric range."
    reflectionPrompt: "Can a value pass a format check but still be incorrect? Give an example."
  - id: de-app-m5-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why validation should be applied at multiple layers of a data system — not just the database.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [layer, application, database, early, reject, invalid]
      rejectedFeedback: "A strong answer explains defence-in-depth: catching errors early (at the API or form level) before they reach the database reduces wasted storage and downstream failures."
    hint: "Think about an API, an application form, and a database — where does data enter the system?"
    reflectionPrompt: "What is the cost of catching a validation error at the database versus catching it at the user interface?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "An order form requires a postcode in the format 'AA1 1AA'. A user submits '12345'. Which validation rule catches this?"
    options: ["Range check", "Uniqueness check", "Format check", "Referential check"]
    correctIndex: 2
    feedback: "Correct — a format check (often implemented as a regular expression) verifies the structural pattern of a value."
  - type: MULTIPLE_CHOICE
    question: "When a record fails validation, what is the correct response from a well-designed system?"
    options:
      - "Silently discard the record and continue"
      - "Store the record anyway and fix it later"
      - "Reject the record, log the failure, and alert the appropriate team"
      - "Convert the invalid value to NULL"
    correctIndex: 2
    feedback: "Silent discards hide problems. Storing bad data spreads the problem downstream. Rejection with logging and alerting is the correct pattern."
retrieval:
  recall: "Name four types of validation rule and give one example of each."
  explain: "Why is it important to validate data at multiple layers (UI, application, database) rather than relying on database constraints alone?"
  mistakeId:
    code: "database constraints are enough for validation"
    answer: "Database constraints are the last line of defence, but validation should also occur at the application layer and ideally at the point of entry (UI or API). Catching errors early reduces unnecessary database calls, improves user experience, and prevents corrupted data from ever entering the pipeline."
---

# Hook

A web form asks a user for their date of birth. They type `32/13/1990`. The month doesn't exist, the day doesn't exist, and yet — if no validation rule is in place — the database happily stores it. Later, when the marketing team tries to calculate which customers are over 18, their query fails, or worse, silently returns wrong results.

Validation rules are the first line of defence for data quality. They define what "acceptable" looks like before data enters your system, and they ensure that garbage never gets a chance to accumulate. As a data engineer, writing effective validation rules is one of the most practical and impactful skills you will develop.

What makes a good validation rule — and where should those rules live?

# Lore Introduction

Master Selvaris spread open the intake manifest for the Great Archive. "Before any scroll may be shelved," he explained, "it must pass the Registrar's checks. The date must be a real date. The scribe's name must be on the approved list. The region code must match a known province." He pointed to a pile of rejected scrolls in the corner. "These failed. Some had impossible dates. Some had unrecognised authors. We do not shelve them — we record the failure and send them back." He looked at you. "A rule that catches one corrupt scroll today saves a thousand corrupted references tomorrow."

# Core Learning

## Concept Introduction

| Validation Type | What It Checks | Example Rule |
|-----------------|---------------|--------------|
| **Type check** | Value is the correct data type | `age` must be an integer, not a string |
| **Range check** | Value falls within an acceptable numeric or date range | `age` must be between 0 and 150 |
| **Format check** | Value matches an expected pattern or structure | `email` must match `^[^@]+@[^@]+\.[^@]+$` |
| **Not-null check** | Required field is present | `customer_id` cannot be NULL |
| **Uniqueness check** | Value is not a duplicate | `email` must be unique across the users table |
| **Referential check** | Value references a valid record in another table | `order.customer_id` must exist in the customers table |
| **Business rule check** | Value satisfies domain-specific logic | `end_date` must be after `start_date` |

## Why It Matters

Validation rules serve as gatekeepers. Every rule you define prevents a category of bad data from entering your system. Without them:

- Storage fills with invalid, unusable records
- Queries return wrong answers because corrupt values skew aggregations
- Downstream systems receive bad inputs and produce worse outputs
- Engineers spend weeks debugging data issues that should never have occurred

## Worked Examples

**Example 1: User Registration Form**
A registration API validates that `email` contains '@', `password` is at least 8 characters, `date_of_birth` is a valid date, and `age` derived from that date is at least 13. Any failure returns a structured error — it does not proceed to the database.

**Example 2: CSV Data Import Pipeline**
A script ingests daily sales files. Before loading, it validates: all required columns are present, `sale_amount` is a positive decimal, `product_id` exists in the product catalogue, and `sale_date` is within the current quarter. Rows failing any check are written to a quarantine file for review.

**Example 3: Database Column Constraints**
SQL:
```sql
CREATE TABLE employees (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    age        INT CHECK (age >= 16 AND age <= 80),
    department_id INT REFERENCES departments(id)
);
```
Each constraint is a validation rule enforced by the database as a last line of defence.

## Common Mistakes

- **Only validating at one layer**: Relying solely on database constraints means invalid data reaches the database before being rejected — expensive and slow. Validate early and often.
- **Writing rules that are too strict**: A postcode validator that rejects valid international formats will block legitimate users. Rules must reflect real-world variation.
- **Silent failure**: Storing NULL or a default value when validation fails masks the problem instead of surfacing it. Always log and alert on validation failures.

## Mental Model

Think of validation rules as the security checks at an airport. Passengers (data) must pass document checks (type and format), must be on the approved list (referential checks), and must not carry prohibited items (business rule checks). Failing any check means the passenger does not board. The checks happen at multiple points — online, at check-in, and at the gate — because defence-in-depth is the only reliable approach.

## Mini Summary

- ✔ Validation rules define what acceptable data looks like before it enters a system
- ✔ Types: type, range, format, not-null, uniqueness, referential, and business rule checks
- ✔ Validation should be applied at every layer: UI, API, application logic, and database
- ✔ Failed validation should be logged, alerted, and rejected — never silently stored
- ✔ Good validation rules are precise but not so strict they reject legitimate data

# Guided Practice Quest

Work through the guided steps to identify the correct validation type for each scenario and explain how validation layers work together to protect data quality.

# Solo Practice Quest

Design a validation rule set for a simple `products` table in an e-commerce system. The table should have at least five columns (e.g., product_id, name, price, stock_quantity, category_id). For each column, identify at least one validation rule and categorise it by type (type check, range check, format check, etc.). Write the SQL constraints you would apply to the CREATE TABLE statement. Then reflect: are there any business rules that cannot be expressed as simple SQL constraints — and how would you enforce those?

# Integration

**Mathematics**: Range checks directly apply the concept of domain in mathematics — a function maps inputs from a valid domain to outputs. If the input falls outside the domain, the function is undefined. Validation rules define the domain of acceptable inputs for every field in a data system.

**Software Engineering**: The principle of "fail fast" — detecting and reporting errors at the earliest possible point — is central to both application and data engineering. Validating data at ingestion is the data engineering equivalent of input validation in secure coding. Both prevent downstream corruption.

# Lore Conclusion

Master Selvaris held up the rejected scrolls. "The scribes who wrote these were not careless. They simply did not know the rules of the Archive." He placed them carefully in a labelled box. "We do not throw them away — we record why they failed, and we send word back to the provinces so the scribes can learn. Rules protect the Archive. Feedback improves it." He looked at you steadily. "Now: what rules would you write for the Vault of Births?"

---
