---
id: de-jun-m7-04
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m7
moduleTitle: "Module 7: Data Security"
moduleGlyph: "🔐"
moduleSortOrder: 7
topicSlug: compliance_basics
topicTitle: "Compliance Basics"
topicSortOrder: 4
lesson: compliance_basics
title: "Compliance Basics"
sortOrder: 4
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m7-03]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what PII is and why it requires special handling
    - Describes the key data engineering requirements of GDPR
    - Identifies the right to erasure and how it conflicts with audit trail immutability
    - Explains data minimisation and purpose limitation principles
    - Describes the data engineer's role in compliance (technical implementation of legal requirements)
  keywords: [GDPR, PII, personally identifiable information, right to erasure, data minimisation, purpose limitation, data retention, lawful basis, data subject, consent, pseudonymisation, anonymisation, privacy by design, DPA, ICO]
  modelAnswer: |
    PII (Personally Identifiable Information): any data that can identify an individual — name, email, address, IP address, device ID. GDPR (General Data Protection Regulation): EU law requiring lawful basis for processing personal data, limiting data collection to what is necessary (data minimisation), retaining data only as long as needed (retention policy), and responding to data subject requests (access, rectification, erasure, portability). Right to erasure: individuals can request deletion of their personal data — data engineers must implement this in schemas and ETL pipelines. Conflict with audit trails: audit logs may contain PII — solution is pseudonymisation (replace real identity with a pseudonym in logs) or anonymisation (remove all identifying data) after retention period. Privacy by design: security controls (access control, encryption, audit) must be designed in from the start, not added as an afterthought.
guidedSteps:
  - id: de-jun-m7-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A member requests deletion of their Archive account under GDPR's right to erasure. The Archive's audit_log table contains rows referencing that member's actions. Which approach correctly handles this conflict?
    inputConfig:
      options:
        - "Delete all audit log rows referencing the member's ID — compliance with erasure trumps audit requirements"
        - "Pseudonymise the member's PII in audit records (replace name/email with an anonymous token) while retaining the operational record for legal/fraud prevention purposes"
        - "Refuse the erasure request — audit logs are exempt from GDPR entirely"
        - "Delete the member's application account but keep all audit records unchanged — erasure only applies to the primary database"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Pseudonymise the member's PII in audit records (replace name/email with an anonymous token) while retaining the operational record for legal/fraud prevention purposes"]
      rejectedFeedback: "GDPR's right to erasure (Article 17) requires deletion of personal data — but it has exceptions. Article 17(3)(b) exempts data needed to comply with a legal obligation, including fraud prevention, tax records, and other legal compliance. Audit records may be required for legal, financial, or regulatory obligations. The correct approach: pseudonymise or anonymise the PII in audit records while retaining the operational record. Pseudonymisation: replace member name, email, address with a non-identifying token ('DELETED_USER_42a8f'). The audit record still shows 'someone changed loan #123 on date X' without revealing WHO. Anonymisation (stronger): replace the member ID with a random UUID and remove all attributes that could re-identify. The GDPR does not apply to truly anonymised data. Practical implementation: a 'member erasure' process that updates the member record to anonymised values, then pseudonymises references in related tables."
    hint: "There is a GDPR exception for data retained for legal or compliance purposes. Can you retain the record without the identifying details?"
    reflectionPrompt: "What is the difference between pseudonymisation and anonymisation, and which is stronger for GDPR purposes?"
  - id: de-jun-m7-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The GDPR principle that you should collect only the minimum data necessary for a specific, declared purpose is called data ________.
    inputConfig:
      placeholder: "minimisation"
    markingRule:
      matchMode: CONTAINS
      accepted: [minimisation, minimization, "data minimisation", "data minimization", "minimum necessary", "minimal data"]
      rejectedFeedback: "Data minimisation (GDPR Article 5(1)(c)): personal data must be adequate, relevant, and limited to what is necessary in relation to the purposes for which it is processed. For data engineers: before adding a column to collect personal data, ask: do we actually need this? Can the business purpose be achieved without it? Example: for a library system, collecting birth decade (1990s) rather than full date of birth achieves age verification without storing a precise PII field. Collecting device fingerprints, location history, or browsing behaviour requires a strong justification. Related principles: (1) Purpose limitation (Article 5(1)(b)): data collected for one purpose cannot be used for another without consent. Loan history collected for managing the library cannot be sold to advertisers. (2) Storage limitation (Article 5(1)(e)): personal data must not be retained longer than necessary — define retention periods per data category."
    hint: "Three words starting with 'data' — the principle about not collecting more than you need."
    reflectionPrompt: "If the Archive already collects date of birth for age verification, does data minimisation require deleting it after the initial check?"
  - id: de-jun-m7-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why 'privacy by design' means a data engineer must consider compliance requirements at schema design time — not as a post-hoc fix.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [schema, design, later, migrate, difficult, retroactive, encrypt, delete, access, column, type, structure, change, hard, expensive, foundation]
      rejectedFeedback: "Privacy by design (GDPR Article 25): data protection must be built into systems from the start. For data engineers: (1) Column types — storing full datetime of birth vs birth_decade is a schema decision. Changing it later requires a migration across potentially millions of rows. (2) Encryption — adding column encryption retroactively requires: adding new columns, migrating existing plaintext to ciphertext, updating all application code that reads/writes those columns, verifying the migration, dropping the old columns. At scale this is a multi-week project. (3) Data segregation — separating PII into a separate table with different retention policies is a schema decision. Retrofitting it onto an existing schema requires restructuring relations and updating all queries. (4) Access control — adding RLS to an existing table works, but designing the schema with separation (separate PII table vs mixed table) is much cleaner. Each decision made correctly at design time costs an hour; made incorrectly and fixed later costs weeks."
    hint: "Think about how hard it is to add encryption or change column types after a schema has millions of rows of data."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Under GDPR, which of these is NOT a valid lawful basis for processing personal data?"
    options:
      - "Consent — the data subject has given explicit consent"
      - "Legitimate interests — processing is necessary for the organisation's legitimate interests"
      - "Contractual necessity — processing is necessary to perform a contract with the data subject"
      - "Commercial benefit — the organisation derives financial benefit from the data"
    correctIndex: 3
    feedback: "GDPR Article 6 lists six lawful bases for processing: (1) Consent, (2) Contract performance, (3) Legal obligation, (4) Vital interests, (5) Public task, (6) Legitimate interests. 'Commercial benefit' is not a lawful basis. An organisation cannot process personal data simply because it is profitable. Legitimate interests (basis 6) requires a balancing test: the organisation's interest must be weighed against the individual's rights and freedoms. Selling member data to third-party advertisers typically fails this test for a library — members would not reasonably expect this use. Practical implication for data engineers: when asked to build a new data pipeline using member data, ask 'what is the lawful basis?'. If the answer is 'we want to make money', that is not a valid basis and the pipeline should not be built without legal review."
  - type: MULTIPLE_CHOICE
    question: "A data retention policy requires deleting member records after 7 years of inactivity. As a data engineer, your implementation should:"
    options:
      - "Write a one-off script to delete the records when requested by the legal team"
      - "Create an automated scheduled job that identifies and deletes (or anonymises) records older than 7 years, with logging of what was deleted"
      - "Add a note in the README that records should be deleted every 7 years"
      - "Retention policies are a legal matter, not a data engineering concern"
    correctIndex: 1
    feedback: "Data retention is a legal requirement (GDPR Article 5(1)(e)) but its implementation is a data engineering responsibility. An automated, scheduled process is the correct approach because: (1) Manual processes fail — legal asks annually, someone forgets, the schedule slips. (2) The process must be consistent — same rules applied every run. (3) It must be auditable — log what was deleted, when, and by what process. (4) It must handle exceptions correctly — records subject to legal hold, active investigations, or ongoing contractual obligations should not be deleted. Implementation: a scheduled job (cron, Spring Batch, Airflow) that: queries for records past retention date, checks exception flags, anonymises PII (rather than hard-deletes — for referential integrity), logs the operation to the audit trail. The data engineer designs and maintains this system; legal defines the retention rules."
retrieval:
  recall: "List the five GDPR principles for processing personal data (Article 5) and give one data engineering implementation for each principle."
  explain: "Explain the GDPR right to data portability (Article 20). What does it require technically, and how would you implement it for the Archive's member loan history?"
  mistakeId:
    code: |
      -- Archive keeps all member data forever for "historical analysis"
      -- No retention policy implemented
      -- PII stored across 15 tables without documentation of which fields are PII
      -- Analytics pipeline sends member emails + loan history to a marketing partner
      -- No consent was obtained for marketing use
      -- No process for responding to subject access requests
    answer: "Multiple GDPR violations: (1) No retention policy: GDPR requires data not be kept 'longer than necessary'. Keeping data forever for 'historical analysis' requires a lawful basis for each data subject. If the lawful basis was 'contract' (membership), the basis expires when membership ends. Fix: define retention periods per data category (e.g. 7 years post-membership for financial records per tax law, then anonymise). (2) PII not documented: GDPR requires a Record of Processing Activities (RoPA) — a register of all PII, where it lives, lawful basis, retention period. Data engineers maintain the technical portions of the RoPA. (3) Sending to marketing partner without consent: a separate lawful basis (consent or legitimate interests with balancing test) is required for marketing. Sharing with a third party requires a Data Processing Agreement. This is likely a reportable breach. Fix: stop the pipeline; obtain consent or DPA before resuming. (4) No subject access request process: GDPR requires responding to SARs within 30 days. Fix: implement a SAR response process that can generate a member's complete data export across all 15 tables."
---

# Hook

Compliance is where data engineering meets law. GDPR, HIPAA, PCI-DSS, SOX — these are not abstract regulations. They translate directly into technical requirements: which columns to encrypt, how long to retain data, who can access it, what happens when a user requests deletion. A data engineer who cannot implement compliance requirements is a liability risk.

# Lore Introduction

"We received a Subject Access Request from a member," the Senior Archivist said, placing a letter on the table. "Under GDPR, we have 30 days to provide them with every piece of personal data we hold about them, in a portable format." The Junior Engineer looked at the database. "That's across seven tables — loans, renewals, fines, reservations, notifications, audit logs, preferences." The Senior Archivist nodded. "We also have a deletion request from another member who closed their account six months ago. We never deleted their data." She looked at the Junior directly. "Privacy law is not the legal team's problem alone. Every schema we design, every pipeline we build, every column we add — these are compliance decisions. A data engineer who does not understand GDPR is building systems that will create legal exposure for the organisation."

# Core Learning

## Concept Introduction

### What is PII and Why It Matters

```
PII (Personally Identifiable Information):
Any data that can identify, or be used to identify, an individual.

Direct PII:
  Name, email, phone, address, national ID, passport number,
  date of birth, biometric data, financial account numbers

Indirect PII (PII in context):
  IP address, device ID, cookie ID, location data,
  employment details + job title + company (combined = identifying),
  age range + postcode + gender (combined = identifying)

Special categories (extra protection required by GDPR Article 9):
  Health data, genetic data, biometric data, racial/ethnic origin,
  political opinions, religious beliefs, sexual orientation

Data Engineer responsibilities:
  - Know which columns in every table contain PII
  - Apply appropriate access controls to PII columns
  - Implement encryption for sensitive PII
  - Enforce data minimisation at schema design time
  - Implement and automate retention policies
```

### GDPR Key Requirements for Data Engineers

```
GDPR Article 5 — Principles for processing personal data:
  1. Lawfulness, fairness, transparency
     → Don't build pipelines that process data without a lawful basis
     → Ask legal: what is the lawful basis for this pipeline?

  2. Purpose limitation
     → Data collected for loan management cannot be used for marketing
     → Don't reuse member data for undeclared purposes

  3. Data minimisation
     → Collect only what is necessary
     → birth_decade instead of full date_of_birth if only age tier is needed
     → Don't add PII columns "in case we need them"

  4. Accuracy
     → Provide members a way to correct their data
     → Log changes to PII via audit trail

  5. Storage limitation (Retention)
     → Define retention periods: LOANS: 7 years (financial records)
     →                           MEMBERS: 2 years post-cancellation
     →                           AUDIT_LOG_PII: anonymise after 90 days
     → Implement automated deletion/anonymisation processes

  6. Integrity and confidentiality (Security)
     → Access control (Module 7.1), Encryption (Module 7.2), Auditing (Module 7.3)
```

### Implementing Data Subject Rights

```sql
-- RIGHT TO ACCESS (Article 15): provide all data held about a member
-- Data engineer implements: a query that collects data from all tables

SELECT json_build_object(
    'member', row_to_json(m),
    'loans', (SELECT json_agg(row_to_json(l)) FROM loans l WHERE l.member_id = m.id),
    'fines', (SELECT json_agg(row_to_json(f)) FROM fines f WHERE f.member_id = m.id),
    'reservations', (SELECT json_agg(row_to_json(r)) FROM reservations r WHERE r.member_id = m.id)
) AS subject_access_data
FROM members m
WHERE m.id = :memberId;
-- Return as JSON download — GDPR requires machine-readable format (right to portability)

-- RIGHT TO ERASURE (Article 17):
-- Step 1: anonymise PII in primary record (retain the record shell for referential integrity)
UPDATE members
SET full_name     = 'DELETED',
    email         = NULL,
    phone         = NULL,
    address       = NULL,
    is_deleted    = TRUE,
    deleted_at    = NOW()
WHERE id = :memberId;

-- Step 2: pseudonymise references in audit log (retain operational record, remove PII)
UPDATE audit_log
SET old_values = jsonb_set(old_values, '{full_name}', '"DELETED"') - 'email' - 'phone'
WHERE table_name = 'members'
  AND record_id = :memberId
  AND old_values ? 'email';  -- only rows containing PII fields

-- Step 3: Remove from marketing systems, analytics exports, email lists
-- (document each external system in the RoPA — Record of Processing Activities)
```

### Data Retention Automation

```sql
-- Define retention rules (in a configuration table or application config)
CREATE TABLE retention_policy (
    table_name        VARCHAR(100) PRIMARY KEY,
    retention_months  INT NOT NULL,
    action            VARCHAR(20) DEFAULT 'ANONYMISE',  -- ANONYMISE or DELETE
    pii_columns       TEXT[],                           -- columns to anonymise
    exception_column  VARCHAR(100)                      -- skip rows where this is set
);

INSERT INTO retention_policy VALUES
    ('members', 24, 'ANONYMISE', ARRAY['full_name','email','phone','address'], 'legal_hold'),
    ('loans', 84, 'ANONYMISE', ARRAY['member_email_snapshot'], NULL),
    ('audit_log', 12, 'ANONYMISE', ARRAY['old_values','new_values'], NULL);

-- Retention job (run nightly via pg_cron or scheduled Spring Batch job):
UPDATE members
SET full_name = 'ANONYMISED',
    email     = NULL,
    phone     = NULL,
    address   = NULL
WHERE status = 'INACTIVE'
  AND updated_at < NOW() - INTERVAL '24 months'
  AND (legal_hold IS NULL OR legal_hold = FALSE)
  AND NOT is_deleted;

-- Log the anonymisation run to audit trail for compliance reporting
INSERT INTO audit_log (table_name, operation, record_id, db_user, changed_at)
SELECT 'members', 'ANONYMISE', id, CURRENT_USER, NOW()
FROM members WHERE ... -- same WHERE clause above
```

### Privacy by Design Checklist

```
Schema design — before adding PII columns:
  □ Is this column actually necessary? (data minimisation)
  □ What is the lawful basis for collecting it? (document in RoPA)
  □ Which roles need access? (access control)
  □ Does it need column-level encryption? (sensitivity classification)
  □ What is the retention period? (add to retention_policy table)
  □ What happens on erasure request? (update the erasure query)
  □ Is it included in the SAR (Subject Access Request) export? (update SAR query)
  □ Does any ETL pipeline send this to an external system? (document in RoPA)

Pipeline design — before building a new data pipeline:
  □ What PII does this pipeline process?
  □ What is the lawful basis?
  □ Does the destination have a Data Processing Agreement?
  □ Is the destination in an adequate country (GDPR transfer rules)?
  □ Is the pipeline included in the DPIA (Data Protection Impact Assessment)?
```

## Why It Matters

Compliance turns data handling from engineering preference into legal obligation — and engineers are the ones who implement it:

- GDPR's "right to erasure" is a schema design problem: can you actually delete one person's data from every table, backup, and downstream copy?
- Retention rules cut both ways — keeping data too long is a violation, just like losing it too early
- Fines are real (up to 4% of global turnover under GDPR), and "the developer didn't know" is not a defence the company can use

Understanding the basics lets you raise the right questions at design time, when compliance is cheap, instead of at audit time, when it isn't.

## Common Mistakes

- **"We'll add compliance later"**: GDPR Article 25 requires privacy by design and by default. Adding compliance retroactively means schema migrations across millions of rows, rewriting pipelines, and retrospective risk assessments. Design it in from the start.
- **Not knowing where all PII lives**: you cannot comply with erasure or SAR requests if you don't have a complete map of every table, column, and external system that holds PII. Maintain a Record of Processing Activities.
- **Treating anonymisation and pseudonymisation as equivalent**: pseudonymised data (identifiable with a key) is still personal data under GDPR. Only truly anonymised data (irreversibly stripped of all identifying information) is outside GDPR's scope. Data keyed by member_id is pseudonymised, not anonymous — the key exists.
- **No process for data subject requests**: GDPR requires a 30-day response window for SARs and erasure requests. "We'll handle it manually when it comes in" fails at scale. Build automated pipelines for SAR export and erasure before they are needed.

## Mental Model

Compliance law is a contract between your organisation and the individuals whose data you hold. The law defines the terms of the contract; data engineers implement the technical controls that honour it. Think of PII as borrowed property: you're allowed to hold it for specific purposes, for a defined period, with appropriate safeguards. When the purpose ends, you return it (delete/anonymise). When the owner asks for a copy (SAR), you provide it. When they ask you to return everything (erasure), you do so. Building systems that make honouring this contract automated and reliable is the data engineer's compliance role.

## Mini Summary

- ✔ PII: any data that can identify an individual — direct (name, email) or indirect (IP, device ID)
- ✔ GDPR principles: lawfulness, purpose limitation, data minimisation, accuracy, storage limitation, security
- ✔ Right to erasure: anonymise PII in records; retain operational records for legal obligations
- ✔ Retention policies: define periods per data category; automate deletion/anonymisation
- ✔ Subject Access Requests: automated query across all tables — 30-day response window
- ✔ Privacy by design: compliance built in at schema/pipeline design time — not added later
- ✔ Record of Processing Activities (RoPA): maps every PII field to purpose, basis, retention

# Guided Practice Quest

Work through the guided steps to identify all PII columns in the Archive schema, implement the right-to-erasure process for a single member, and write the automated retention job SQL that anonymises member records inactive for more than 24 months.

# Solo Practice Quest

Implement full GDPR compliance for the Archive system. Tasks: (1) Create a complete PII data map: every table, every PII column, its classification (direct/indirect/special category), retention period, and which roles can access it; (2) Implement the Subject Access Request export as a single SQL query or stored procedure that returns all data about a given member_id in JSON format; (3) Implement the right to erasure: SQL that anonymises a member record, pseudonymises their references in audit_log, and produces a log of what was anonymised; (4) Create a retention_policy table and write the nightly retention job for three data categories (members, loans, audit_log); (5) Write a Privacy Impact Assessment checklist as comments/documentation for a hypothetical new feature: the Archive wants to track which pages of borrowed books members are reading; (6) A new analytics pipeline wants to send member loan patterns to a recommendation engine vendor. List the legal and technical steps required before this pipeline can be built.

# Integration

**Mathematics**: GDPR compliance has a combinatorial complexity that benefits from systematic enumeration. A system with N tables, each with M columns, has N×M potential PII fields. For each field, there are K choices of handling (public, role-restricted, encrypted, pseudonymised, not collected). The compliance space has K^(N×M) possible configurations — only a small subset of which are compliant. Automating compliance (retention jobs, SAR export, erasure queries) reduces this to a finite set of well-defined operations. The 30-day SAR response window creates a performance constraint: if the SAR export query runs against N tables with total data volume D, the query must complete in time ≤ 30 days × (fraction allocated to technical processing). At D = 10^9 rows, this requires indexing, pagination, and potentially pre-computation. GDPR compliance is not just a legal question but a data engineering performance problem: ensuring that privacy rights can be honoured within legally mandated timeframes at production data volumes.

**Sciences (Political Science / Legal Theory — Rule of Law)**: GDPR represents the implementation of a legal principle — the right to privacy as a fundamental right (EU Charter of Fundamental Rights, Article 7 and 8) — through technical regulations enforceable by national Data Protection Authorities. The GDPR enforcement model parallels administrative law: the DPA (e.g., ICO in the UK) acts as a regulator with investigative powers, the ability to issue fines (up to 4% of global annual turnover or €20M), and binding orders to change processing activities. Data engineers occupy a role analogous to building safety inspectors: they implement and verify the technical controls that satisfy the legal requirement, producing evidence (audit logs, retention records, access logs) that can demonstrate compliance to the regulator. The adversarial context is regulatory inspection and enforcement, not just security breach prevention — the ICO can audit a compliant organisation with no breach and still issue enforcement notices if technical controls are insufficient.

# Lore Conclusion

"The SAR response is ready," the Junior Engineer reported. "One query returns everything we hold about a member across all seven tables as JSON. The erasure process anonymises PII in the primary record and pseudonymises references in the audit log." The Senior Archivist reviewed the implementation. "And retention?" The Junior pulled up the scheduler. "Nightly job. Members inactive for 24 months are anonymised. Loans older than 7 years — financial record retention — anonymise the member email snapshot. The job logs to the audit trail." The Senior Archivist set the documentation down. "Module 7 complete. You've implemented the full security triad: access control to restrict who sees data, encryption to protect data in transit and at rest, auditing to record what happens, and compliance to define what we are legally required to do." She stood. "One module remains: Database Testing — because a system that cannot be verified cannot be trusted, regardless of how well it is built."

---
