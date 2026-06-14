---
id: de-sen-m6-03
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m6
moduleTitle: "Module 6: Data Governance"
moduleGlyph: "🏛️"
moduleSortOrder: 6
topicSlug: compliance
topicTitle: "Compliance"
topicSortOrder: 3
lesson: 3
title: "Compliance: Engineering for Regulatory Requirements"
sortOrder: 3
difficulty: 4
estimatedMinutes: 35
xpReward: 75
practiceType: GUIDED_AND_SOLO
questType: RETRIEVAL_CHALLENGE
retrievalWeight: 0.6
questTypes:
  - guided
  - solo
  - retrieval
prerequisites:
  - de-sen-m6-02
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies core GDPR principles and their engineering implications"
    - "Explains the right to erasure and how it is implemented technically without breaking referential integrity"
    - "Describes the role of a Data Protection Impact Assessment (DPIA)"
    - "Identifies the difference between anonymisation and pseudonymisation"
  keywords:
    - GDPR
    - right to erasure
    - pseudonymisation
    - anonymisation
    - DPIA
    - data minimisation
    - purpose limitation
    - consent
  modelAnswer: |
    GDPR's core principles with engineering implications: lawfulness/purpose limitation (only collect data for stated purpose — implement purpose tags on columns), data minimisation (collect only what's needed — periodic schema audits), storage limitation (retain only as long as necessary — retention policies + automated deletion), accuracy (keep data correct — quality monitoring), integrity/confidentiality (secure storage and access).
    Right to erasure: you cannot DELETE a user's rows from all tables without breaking referential integrity and losing analytical history. Correct implementation: pseudonymise (replace PII identifiers with a random token, store the mapping separately). On erasure request, delete the mapping — the pseudonymous data remains but can never be re-identified. Anonymised aggregate data (COUNT(*) per tier) is not personal data and need not be erased.
    DPIA is a pre-implementation risk assessment required for high-risk processing (large-scale PII, sensitive categories, automated decision-making). It identifies risks, documents mitigations, and may require DPA consultation.
    Pseudonymisation replaces a direct identifier with a surrogate (reversible with the key). Anonymisation irreversibly removes the ability to identify the individual. Truly anonymised data is outside GDPR scope; pseudonymised data is still personal data (the key exists).
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "A user submits a GDPR right to erasure request. Your database has their user_id in 8 tables including financial transaction records required by UK tax law (7-year retention). What is the correct response?"
    options:
      - "Delete all records from all 8 tables immediately"
      - "Deny the erasure request because records are needed for tax compliance"
      - "Pseudonymise PII in all tables except financial records; delete PII fields in financial records that are not required for tax compliance; retain required financial records for the statutory period"
      - "Mark records as 'erased' in a flag column but keep all data in place"
    correctIndex: 2
    explanation: "Right to erasure is not absolute — Article 17(3) GDPR lists exceptions including legal obligations. UK tax law requires financial records for 7 years. Correct approach: (1) pseudonymise or anonymise PII in all tables where legally permissible — replace name/email with 'ERASED' or a random token; (2) financial transaction records required by law are retained for the statutory period with PII fields not legally required removed; (3) document the legal basis for retention in the erasure response to the user. Option A violates tax law. Option B is incorrect — partial erasure is required. Option D is GDPR non-compliance."
  - type: FILL_BLANK
    question: "A dataset where a user's name is replaced with a random UUID, with the mapping stored in a separate secure key table, is ___ — still personal data under GDPR because re-identification is possible."
    answer: "pseudonymised"
    explanation: "Pseudonymisation (Article 4(5) GDPR) replaces identifying information with a surrogate. It is a privacy-enhancing measure but NOT anonymisation — the mapping key allows re-identification. Pseudonymised data is still personal data subject to GDPR. Anonymisation requires that re-identification is not reasonably possible — if achieved, the data falls outside GDPR scope."
  - type: SHORT_TEXT
    question: "Your team is building a feature that analyses learner behaviour using session recordings to automatically suggest improvements. What is a DPIA and when must you conduct one?"
    modelAnswer: "A Data Protection Impact Assessment (DPIA) is a systematic analysis of the privacy risks of a high-risk processing activity, required under GDPR Article 35. It documents: what data is processed, for what purpose, what risks to data subjects exist, and what technical and organisational mitigations are applied. Session recordings involving keystroke or gaze data likely qualify as high-risk processing (systematic monitoring of individuals). A DPIA must be completed before processing begins — not after. If residual risks remain high after mitigation, the Data Protection Authority must be consulted before proceeding."
microCheckpoint:
  question: "What is the difference between pseudonymisation and anonymisation under GDPR?"
  answer: "Pseudonymisation replaces identifying information with a surrogate key — re-identification is possible if the key is available. Pseudonymised data is still personal data under GDPR. Anonymisation irreversibly removes all ability to identify individuals. Truly anonymised data is outside GDPR's scope. The difference is reversibility."
retrieval:
  recall: "Name four of the six GDPR data protection principles (Article 5)."
  explain: "Explain why deleting a user's rows from a fact table is usually wrong for GDPR erasure compliance."
  mistakeId: "gdpr-delete-vs-pseudonymise"
---

# The Erasure Request

"We've received a GDPR right-to-erasure request," the Senior Engineer said. "The user has records in 11 tables. I started writing the DELETE statements." The Lead Data Engineer stopped them. "Before you run those: some of those records may be legally required to retain. And deleting from fact tables will break our analytics history. We need to pseudonymise, not delete." Compliance, they were learning, was never as simple as it looked.

# GDPR Principles and Their Engineering Implications

The UK GDPR / EU GDPR Article 5 principles each have concrete engineering implementations:

| Principle | Engineering Implication |
|---|---|
| **Lawfulness, fairness, transparency** | Purpose tags on columns; consent records; privacy notice |
| **Purpose limitation** | Only use data for declared purpose; access controls enforce this |
| **Data minimisation** | Collect only necessary fields; periodic column audits; don't log what you don't need |
| **Accuracy** | Data quality monitoring; user self-service correction; steward-maintained quality SLAs |
| **Storage limitation** | Retention policies; automated deletion jobs; periodic review |
| **Integrity and confidentiality** | Encryption at rest and in transit; access controls; audit logging |

## Personal Data Categories

```
Direct identifiers: name, email, phone, NHS number, passport
Indirect identifiers: IP address, device ID, cookie, username, employee ID
Special category: health data, biometrics, political opinions, racial origin
  → Higher protection requirements; explicit consent or specific legal basis
```

Pseudonymised data (UUID replacing name/email) is still personal data. Aggregate statistics (`COUNT(users) BY tier`) with no re-identification path are not personal data.

## Right to Erasure — Technical Implementation

The right to erasure (Article 17) requires removing personal data, but not all data:
- **Not** applicable when legally required to retain (tax, financial regulation)
- **Not** applicable to anonymised data (aggregates, analytics)
- **Applicable** to direct identifiers and indirect identifiers not legally required

### Correct Implementation: Pseudonymisation on Erasure

```sql
-- 1. Create an erasure mapping (UUID to ERASED marker)
CREATE TABLE erasure_requests (
    user_id         UUID PRIMARY KEY,
    requested_at    TIMESTAMPTZ DEFAULT NOW(),
    completed_at    TIMESTAMPTZ,
    legal_basis     TEXT  -- "user request", "account deletion", etc.
);

-- 2. Pseudonymise direct identifiers in all applicable tables
UPDATE users
SET
    email           = 'ERASED_' || id,  -- irreversible placeholder
    full_name       = 'ERASED',
    phone_number    = NULL,
    date_of_birth   = NULL
WHERE id = :user_id;

-- 3. Audit log entries retain action records without PII
-- audit_log.user_id → anonymise to 'ERASED' in user-facing fields
-- but retain for compliance: the fact that a login occurred is not PII

-- 4. Financial records: retain, but remove non-required PII fields
UPDATE payment_events
SET
    billing_name    = NULL,
    billing_address = NULL
    -- retain: amount, date, transaction_id (legally required)
WHERE user_id = :user_id;

-- 5. Mark erasure complete
UPDATE erasure_requests
SET completed_at = NOW()
WHERE user_id = :user_id;
```

Analytical fact tables retain the pseudonymised UUID. Analytics results (lesson completion rates, XP trends) are unaffected. The user cannot be re-identified because the PII fields are erased.

## Data Minimisation in Practice

```sql
-- Audit: which columns are never queried?
-- (using BigQuery information_schema or pg_stats)
SELECT column_name, last_accessed
FROM column_usage_stats
WHERE table_name = 'learner_events'
  AND last_accessed < NOW() - INTERVAL '90 days'
ORDER BY last_accessed;

-- Columns never queried: candidates for deletion
-- "We're collecting it in case we need it" is not a legal basis
```

Schedule quarterly data minimisation reviews: review all columns, confirm they have a declared purpose, and delete those that don't.

## Data Protection Impact Assessment (DPIA)

Required before high-risk processing (GDPR Article 35). Triggers:
- Systematic monitoring of individuals (session recording, tracking)
- Large-scale processing of special-category data
- Automated decision-making with significant effect on individuals
- New technologies with unknown risk

```
DPIA Template:
1. Description of processing
   Purpose: [what you're doing]
   Data types: [which personal data]
   Recipients: [who sees it]
   
2. Necessity and proportionality
   Legal basis: [consent / legitimate interest / legal obligation]
   Data minimisation: [why each field is necessary]
   
3. Risk assessment
   Risk: [e.g. session recordings could expose sensitive keystrokes]
   Likelihood: [high/medium/low]
   Impact: [high/medium/low]
   Mitigation: [e.g. mask input fields in recordings]
   Residual risk: [post-mitigation assessment]
   
4. DPA consultation required if residual risk remains high
```

## Data Retention Policy

```sql
-- Retention policy registry
CREATE TABLE data_retention_policies (
    table_name          TEXT PRIMARY KEY,
    retention_period    INTERVAL NOT NULL,
    legal_basis         TEXT,
    review_date         DATE,
    owner               TEXT
);

INSERT INTO data_retention_policies VALUES
    ('learner_events',       '3 years', 'Legitimate interest', '2025-01-01', 'product@consortium.io'),
    ('payment_events',       '7 years', 'UK HMRC requirement', '2025-01-01', 'finance@consortium.io'),
    ('audit_log',            '5 years', 'Legal obligation',    '2025-01-01', 'security@consortium.io'),
    ('session_recordings',   '30 days', 'Service improvement', '2025-01-01', 'product@consortium.io');

-- Automated deletion job (runs nightly)
DO $$
DECLARE policy RECORD;
BEGIN
    FOR policy IN SELECT * FROM data_retention_policies LOOP
        EXECUTE format(
            'DELETE FROM %I WHERE created_at < NOW() - %L::INTERVAL',
            policy.table_name, policy.retention_period
        );
    END LOOP;
END $$;
```

## Common Mistakes

> **Deleting Fact Table Rows for Erasure**
> Deleting `fact_lesson_completions` rows where `user_key = :user_key` breaks foreign key integrity and corrupts analytics history. Pseudonymise the user's PII in `dim_user`; the fact table rows become unidentifiable but analytics remain intact.

> **Treating Pseudonymised Data as Anonymised**
> If you hold the pseudonymisation key, the data is personal data. Apply GDPR controls to pseudonymised datasets. Only truly irreversible anonymisation (k-anonymity, differential privacy) removes GDPR obligations.

> **No Legal Basis Documentation**
> "We collect it because it's useful" is not a legal basis. Every personal data field must have a documented lawful basis: consent, contract, legal obligation, vital interest, public task, or legitimate interest. Missing documentation is an audit finding.

## Mental Model

Think of GDPR compliance as **building and fire codes for data**. Just as you can't demolish a listed building (legally required financial records) even if the owner requests it, you can't always erase all data on request. But you can remove the personally identifying features (pseudonymise) while preserving the load-bearing structure (transaction records). The code doesn't just restrict — it also enables: data that complies with minimisation and purpose limitation is simpler, cheaper, and safer than a data swamp with unconstrained collection.

**Mini Summary**: GDPR's six principles translate to: purpose-tagging, minimisation audits, retention automation, quality monitoring, and access controls. Right to erasure means pseudonymise PII, not DELETE fact table rows. Pseudonymised data is still personal data; anonymised data is not. DPIAs are required before high-risk processing. Retention policies must be automated and legally justified per table.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium plans to introduce a new feature: personalised learning recommendations using an ML model trained on full session behaviour data, including keystroke timing and scroll patterns. 500,000 learner records will be processed.

1. Does this processing require a DPIA? Justify using the GDPR Article 35 criteria.
2. What legal basis would you use for processing keystroke timing data?
3. Design the pseudonymisation scheme for training the ML model — what data is pseudonymised, how, and what is the key management strategy?
4. How would you implement the right to erasure for a user whose keystroke data was included in the training dataset?

---

# Integration

**Mathematics**: k-anonymity is a formal mathematical definition of anonymisation. A dataset satisfies k-anonymity if every combination of quasi-identifying attributes (age range, postcode, gender) appears in at least k records — making individual re-identification probability at most 1/k. l-diversity extends k-anonymity by requiring at least l distinct sensitive values in each equivalence class (to resist homogeneity attacks). Differential privacy adds calibrated noise ε such that the probability of any output changes by at most e^ε when one individual's data is added or removed — providing a rigorous, quantified privacy guarantee. These are the mathematical foundations for claiming data is "truly anonymised" beyond GDPR scope.

**Sciences**: The GDPR's proportionality principle mirrors **the precautionary principle** in environmental law and medicine. When there is scientific uncertainty about potential harm, the burden is on the party proposing the action to demonstrate safety — not on regulators to prove harm. Data protection applies the same logic: if you cannot demonstrate a specific purpose and proportionate benefit for collecting personal data, the default is not to collect it. DPIA is the equivalent of an Environmental Impact Assessment — systematic risk identification before action, proportionate to the scale and sensitivity of the processing.

---

# The Compliant Architecture

The session recording feature was redesigned before launch. Keystroke data was masked at capture for sensitive fields. The DPIA identified three residual risks; two were mitigated before launch; one required DPA consultation. The erasure mechanism pseudonymised learner IDs in the training dataset, with the key stored in a separate vault accessible only to two named engineers. "It took three weeks longer to build this way," the Senior Engineer said. "But it's defensible." The Lead Data Engineer closed the DPIA document. "Defensible is the point."
