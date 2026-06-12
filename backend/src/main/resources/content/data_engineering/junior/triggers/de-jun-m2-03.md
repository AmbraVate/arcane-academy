---
id: de-jun-m2-03
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m2
moduleTitle: "Module 2: Database Programming"
moduleGlyph: "🔧"
moduleSortOrder: 2
topicSlug: triggers
topicTitle: "Triggers"
topicSortOrder: 3
lesson: triggers
title: "Triggers"
sortOrder: 3
difficulty: 3
estimatedMinutes: 25
xpReward: 45
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m2-02]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what a trigger is and when it fires (event, timing, per-row vs per-statement)
    - Writes a BEFORE or AFTER trigger with NEW and OLD row references
    - Describes appropriate use cases for triggers (audit logging, derived data, validation)
    - Identifies the dangers of triggers (hidden behaviour, cascading, performance)
    - Explains why triggers can be hard to debug and test
  keywords: [trigger, BEFORE, AFTER, INSTEAD OF, INSERT, UPDATE, DELETE, NEW, OLD, per-row, FOR EACH ROW, audit, cascade, hidden, side effect]
  modelAnswer: |
    A trigger is a procedure that fires automatically when a specific event (INSERT, UPDATE, DELETE) occurs on a table. Timing is BEFORE (before the operation — can cancel or modify it) or AFTER (after the operation — for logging and derived data). FOR EACH ROW fires once per affected row. NEW references the new row being inserted/updated; OLD references the row before update/delete. Good use cases: audit logs, maintaining denormalised summary counts, data validation. Dangers: invisible behaviour (logic hidden in the database), cascading triggers, performance impact on bulk inserts, difficult debugging. Triggers should be used sparingly and documented clearly.
guidedSteps:
  - id: de-jun-m2-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You need to automatically log every change to the orders table (old and new status) to an order_audit table. Which trigger type is correct?
    inputConfig:
      options:
        - "BEFORE UPDATE — fires before the update so you can see what will change"
        - "AFTER UPDATE FOR EACH ROW — fires after each row update, allowing access to both OLD and NEW values"
        - "BEFORE INSERT — fires when a new order is inserted"
        - "INSTEAD OF UPDATE — replaces the update entirely"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["AFTER UPDATE FOR EACH ROW — fires after each row update, allowing access to both OLD and NEW values"]
      rejectedFeedback: "For audit logging, use AFTER UPDATE FOR EACH ROW. 'AFTER' ensures the update succeeded before we log it (no point logging a failed update). 'FOR EACH ROW' fires once per updated row, allowing you to insert one audit record per change. Inside the trigger, OLD.status is the status before the update and NEW.status is the new value — you log both to show what changed. BEFORE UPDATE is used when you need to modify or validate data before it is written; INSTEAD OF is used on views to redirect operations to underlying tables."
    hint: "You want to log after a successful update — which timing fires after the operation completes?"
    reflectionPrompt: "What would happen if you used BEFORE UPDATE for audit logging but the UPDATE was subsequently rejected by a constraint?"
  - id: de-jun-m2-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Inside a trigger on an UPDATE, ________ refers to the row's values before the update, and NEW refers to the values after.
    inputConfig:
      placeholder: "OLD"
    markingRule:
      matchMode: CONTAINS
      accepted: [OLD, old, ":OLD"]
      rejectedFeedback: "In SQL triggers, OLD refers to the row as it existed before the UPDATE or DELETE — it represents the 'before' state. NEW refers to the row as it will be after the INSERT or UPDATE — the 'after' state. For INSERT triggers: only NEW exists (there is no prior row). For DELETE triggers: only OLD exists (there is no row after deletion). For UPDATE triggers: both OLD and NEW exist. In Oracle, the syntax is :OLD and :NEW (with colon prefix); in MySQL and PostgreSQL, it is OLD and NEW (no colon)."
    hint: "The two special row references available in an UPDATE trigger — one for before, one for after."
    reflectionPrompt: "In a DELETE trigger, which pseudo-row is available — OLD, NEW, or both?"
  - id: de-jun-m2-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why triggers can make a database hard to maintain, and when you should use application-layer logic instead.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [hidden, invisible, debug, test, unexpected, side effect, cascade, performance, application, transparent]
      rejectedFeedback: "Triggers execute automatically and invisibly — an INSERT triggers a log, which might trigger another action, which might trigger another. This cascading and hidden behaviour makes it difficult for developers to understand what a single SQL statement actually does. Triggers are not visible in the application code that issues the SQL. Testing is harder because triggers require the full database context to test. Application-layer logic should be preferred when: the logic needs to be tested independently, the behaviour needs to be transparent to all team members, or the logic is business-rule complexity that belongs in the domain model rather than the data layer."
    hint: "What makes triggers 'invisible' from the application's perspective? Why does invisible = hard to maintain?"
    reflectionPrompt: "If a colleague does a bulk INSERT of 100,000 rows and the INSERT trigger is unexpectedly slow, how would they know the trigger is the cause?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A BEFORE INSERT trigger on the members table sets NEW.created_at = NOW() if it is NULL. What does this achieve?"
    options:
      - "It prevents inserts by overwriting the timestamp"
      - "It ensures every new member row has a created_at timestamp, even if the application did not provide one"
      - "It fires after the insert to record when it happened"
      - "It is invalid — triggers cannot modify NEW values"
    correctIndex: 1
    feedback: "BEFORE triggers can modify the NEW row before it is written to the table. Setting NEW.created_at = NOW() in a BEFORE INSERT trigger ensures the column is always populated, acting as a server-side default that cannot be accidentally omitted by the application. This is a valid and common use of BEFORE triggers for data normalisation and default enforcement. AFTER triggers cannot modify the row (it has already been written). In PostgreSQL, this is done by returning NEW from the trigger function with the modified value."
  - type: MULTIPLE_CHOICE
    question: "A bulk INSERT of 500,000 rows runs unusually slowly. After investigation, you discover a FOR EACH ROW trigger. Why is this the likely cause?"
    options:
      - "Triggers are not compatible with bulk inserts"
      - "A FOR EACH ROW trigger executes once per inserted row — 500,000 trigger executions, each potentially doing additional work"
      - "The trigger is locking the table exclusively"
      - "FOR EACH ROW triggers only work on UPDATE, not INSERT"
    correctIndex: 1
    feedback: "FOR EACH ROW triggers execute once per affected row. A bulk INSERT of 500,000 rows triggers 500,000 individual trigger executions. If each trigger execution does a single INSERT to an audit table, that is 500,000 additional INSERT operations. For bulk loading scenarios, triggers are a significant performance concern — the overhead multiplies with the row count. Consider disabling triggers for bulk load operations (if auditing is not required during load) or using FOR EACH STATEMENT triggers where appropriate."
retrieval:
  recall: "Write a trigger that automatically updates a member's last_activity_date on the members table whenever they insert a new loan record in the loans table."
  explain: "Explain FOR EACH ROW vs FOR EACH STATEMENT triggers, and give a use case where FOR EACH STATEMENT is more appropriate."
  mistakeId:
    code: "CREATE TRIGGER prevent_overdue_loan BEFORE INSERT ON loans FOR EACH ROW BEGIN IF (SELECT COUNT(*) FROM loans WHERE member_id = NEW.member_id AND return_date IS NULL) > 5 THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot loan: member has 5+ active loans'; END IF; END"
    answer: "This trigger has a performance issue: every new loan insert runs a correlated subquery (SELECT COUNT(*) FROM loans WHERE member_id = NEW.member_id) — effective but potentially slow on large loans tables without an index on member_id. More importantly, it creates a logic that is invisible to the application — the application's INSERT fails with an error that must be handled, but a developer reading the application code would not know why without inspecting the database schema. Best practice: validate active loan count in the application layer (where it is visible and testable), or at minimum document the trigger clearly and handle the SQLSTATE error in the application's data access layer."
---

# Hook

Procedures and functions are explicitly called. Triggers fire automatically — the database calls them in response to data changes. This makes triggers powerful for cross-cutting concerns like audit logging and derived data, but also dangerous: hidden behaviour that fires invisibly makes systems hard to understand and debug.

# Lore Introduction

"Every time a member's status changes, we need an audit record," the Junior Engineer said. "The application has seven places where status updates happen. I'd have to add the audit log to each one." The Senior Archivist leaned back. "Or one trigger. Status changes — one audit INSERT. No matter which application path caused the change." She paused. "But understand what you're building. Triggers are invisible to the application developer. A colleague inserts a record, and something happens they didn't expect, logged in a table they don't know about, because a trigger fired. That invisibility is the power — and the danger."

# Core Learning

## Concept Introduction

### Trigger Anatomy

```
Event:   INSERT | UPDATE | DELETE (or multiple)
Timing:  BEFORE | AFTER | INSTEAD OF (views only)
Scope:   FOR EACH ROW | FOR EACH STATEMENT
Action:  Trigger body (SQL + procedural logic)
```

### Audit Log Trigger (AFTER UPDATE)

```sql
-- MySQL: create audit table
CREATE TABLE member_audit (
    audit_id    SERIAL PRIMARY KEY,
    member_id   INT,
    changed_at  TIMESTAMP DEFAULT NOW(),
    changed_by  VARCHAR(100),
    old_status  VARCHAR(20),
    new_status  VARCHAR(20)
);

-- Create trigger
DELIMITER $$

CREATE TRIGGER after_member_status_change
AFTER UPDATE ON members
FOR EACH ROW
BEGIN
    IF OLD.status != NEW.status THEN
        INSERT INTO member_audit (member_id, old_status, new_status)
        VALUES (NEW.member_id, OLD.status, NEW.status);
    END IF;
END$$

DELIMITER ;
```

```sql
-- PostgreSQL: triggers use a separate trigger function
CREATE OR REPLACE FUNCTION log_member_status_change()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF OLD.status IS DISTINCT FROM NEW.status THEN
        INSERT INTO member_audit (member_id, old_status, new_status)
        VALUES (NEW.member_id, OLD.status, NEW.status);
    END IF;
    RETURN NEW;   -- must return NEW for AFTER triggers
END;
$$;

CREATE TRIGGER after_member_status_change
AFTER UPDATE ON members
FOR EACH ROW
EXECUTE FUNCTION log_member_status_change();
```

### BEFORE Trigger — Data Normalisation

```sql
-- Normalise email to lowercase on every insert/update
CREATE OR REPLACE FUNCTION normalise_member_email()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    NEW.email = LOWER(TRIM(NEW.email));
    RETURN NEW;   -- BEFORE trigger must return NEW (or NULL to cancel)
END;
$$;

CREATE TRIGGER before_member_insert_or_update
BEFORE INSERT OR UPDATE ON members
FOR EACH ROW
EXECUTE FUNCTION normalise_member_email();
```

### Maintaining Denormalised Counts

```sql
-- Keep a loan_count column on members up to date automatically
CREATE OR REPLACE FUNCTION update_member_loan_count()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE members SET loan_count = loan_count + 1
        WHERE member_id = NEW.member_id;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE members SET loan_count = loan_count - 1
        WHERE member_id = OLD.member_id;
    END IF;
    RETURN NULL;   -- AFTER trigger return value is ignored for non-view
END;
$$;

CREATE TRIGGER maintain_member_loan_count
AFTER INSERT OR DELETE ON loans
FOR EACH ROW
EXECUTE FUNCTION update_member_loan_count();
```

### FOR EACH STATEMENT vs FOR EACH ROW

```sql
-- FOR EACH ROW: fires once per affected row (most common)
-- FOR EACH STATEMENT: fires once per SQL statement regardless of rows affected

-- Use FOR EACH STATEMENT for:
-- 1. Logging that a table was modified (not each row)
-- 2. Operations where row-level detail is not needed
-- 3. Performance — avoid N trigger executions on bulk operations

CREATE TRIGGER log_bulk_import
AFTER INSERT ON imported_records
FOR EACH STATEMENT   -- fires once even if 100,000 rows inserted
EXECUTE FUNCTION record_import_event();
```

### INSTEAD OF Triggers (Views)

```sql
-- INSERT on a view that combines two tables
CREATE VIEW member_with_address AS
SELECT m.member_id, m.name, a.street, a.city
FROM members m JOIN addresses a ON m.member_id = a.member_id;

-- INSTEAD OF allows INSERT on the view
CREATE RULE member_view_insert AS ON INSERT TO member_with_address
DO INSTEAD (
    INSERT INTO members (name) VALUES (NEW.name);
    INSERT INTO addresses (member_id, street, city)
    VALUES (currval('members_member_id_seq'), NEW.street, NEW.city);
);
```

## Why It Matters

Triggers are automation with a dark side — they guarantee something always happens, and they hide that it's happening:

- Audit trails are the classic legitimate use: no application path can forget to log a change, because the database itself writes the log
- But invisible logic confuses debugging — an UPDATE that mysteriously changes other rows sends engineers hunting through application code for hours before anyone checks triggers
- Trigger chains (a trigger firing a trigger) can amplify one statement into a cascade with real performance cost

The professional judgement: use triggers where the guarantee must be absolute, document them loudly, and reach for application code everywhere else.

## Common Mistakes

- **Cascading triggers**: A trigger fires, which modifies another table, which fires another trigger. Deep cascades are extremely hard to debug and can cause unexpected recursion or infinite loops.
- **Trigger order ambiguity**: Multiple triggers on the same event may fire in unexpected order. Most databases execute them in creation order, but this is not always guaranteed.
- **Hidden performance costs**: FOR EACH ROW triggers on high-write tables add per-row overhead to every INSERT/UPDATE/DELETE — significant on bulk loads.
- **Testing gaps**: Triggers are not tested by unit tests that mock the database — they require integration tests against a real database schema. Teams using mocked repositories may never test trigger behaviour.

## Mental Model

A trigger is like a motion sensor attached to a table. When data moves (INSERT/UPDATE/DELETE), the sensor fires a response automatically. The application that caused the motion does not need to know the sensor exists — which is both the feature (consistent cross-cutting logic) and the risk (unexpected behaviour when you forget the sensor is there). Every trigger attached to a table is additional work that runs invisibly on every qualifying operation. The more triggers, the harder it is to understand what a single INSERT actually does.

## Mini Summary

- ✔ Triggers fire automatically on INSERT, UPDATE, or DELETE events
- ✔ BEFORE triggers can modify/cancel the operation; AFTER triggers react to it
- ✔ NEW = row after operation; OLD = row before; not all are available for all events
- ✔ FOR EACH ROW fires per row; FOR EACH STATEMENT fires once per SQL statement
- ✔ Use sparingly: audit logs, data normalisation, denormalised count maintenance
- ✔ Document clearly — invisible behaviour is the main maintenance risk

# Guided Practice Quest

Work through the guided steps to write an AFTER INSERT trigger on loans that updates member last_activity_date, write a BEFORE UPDATE trigger that prevents status from moving backwards (e.g. 'cancelled' → 'active'), and identify the performance concern in a FOR EACH ROW trigger on a high-write table.

# Solo Practice Quest

Build the following triggers for the Archive system: (1) an audit trigger on loans that logs every INSERT (new loan) and UPDATE (status change, return date set) to a loans_audit table with old/new values; (2) a BEFORE INSERT trigger on members that validates the email format contains '@' and raises an error if it does not; (3) a AFTER INSERT/DELETE trigger on loans that maintains a denormalised active_loan_count column on members (test: does it handle concurrent inserts correctly? — hint: research row-level locking); (4) a trigger that prevents deletion of members who have active loans. For each trigger: describe the trade-off between trigger-based enforcement and application-layer enforcement, and when you would choose each.

# Integration

**Mathematics**: Triggers correspond to reactive functions in event-driven mathematical models — specifically, they implement the observer pattern from event algebra. A trigger T on table R for event e is formalised as: ∀ row r : event e occurs on R(r) → T(r) fires. BEFORE triggers implement precondition checks: a BEFORE INSERT trigger that raises an error corresponds to a guard condition g(r): if ¬g(r) then reject insertion. This is related to the concept of integrity constraints in relational algebra — triggers enforce invariants that cannot be expressed as static CHECK constraints. Cascading triggers implement event propagation in reactive systems: modifying relation R₁ triggers event e₁, which propagates to modify R₂, triggering e₂, etc. — a Petri net-like model of reactive database behaviour.

**Sciences (Epidemiology — Surveillance Systems)**: Public health surveillance systems use database triggers to implement real-time disease reporting. When a laboratory inserts a positive test result for a notifiable disease, a AFTER INSERT trigger fires to: create a case report, notify the regional health authority, check for outbreak patterns, and update surveillance statistics. This automatic propagation ensures no case is missed regardless of which application path created the lab result. The same pattern — insert data, trigger automatic public health response — is used in food safety (positive pathogen detection triggers supplier notification), drug safety (adverse event reports trigger pharmacovigilance alerts), and environmental monitoring (sensor reading exceeds threshold triggers regulatory notification).

# Lore Conclusion

"The audit trigger is in place," the Junior Engineer reported. "Every status change on members is logged — which application path made the change is irrelevant." The Senior Archivist reviewed the audit table. "And the BEFORE INSERT trigger ensures no member can be inserted with a malformed email, regardless of which form submitted it." She paused. "But we have four triggers now. Before we add more: document them. Every trigger on every table, what it does, why it exists. Future engineers — including your future self — need to understand what happens when they INSERT a loan record." She closed the editor. "Next: views — a different kind of database object that provides a reusable, named query."

---
