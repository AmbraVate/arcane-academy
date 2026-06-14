---
id: de-lead-m3-01
school: DATA_ENGINEERING
domainId: data_engineering
tier: LEAD
moduleId: de-lead-m3
moduleTitle: "Module 3: Data Governance & Ethics"
moduleGlyph: "⚖️"
moduleSortOrder: 3
topicSlug: privacy
topicTitle: "Privacy"
topicSortOrder: 1
lesson: 1
title: "Privacy by Design: Embedding Protection into Architecture"
sortOrder: 1
difficulty: 5
estimatedMinutes: 40
xpReward: 100
practiceType: GUIDED_AND_SOLO
questType: RETRIEVAL_CHALLENGE
retrievalWeight: 0.6
questTypes:
  - guided
  - solo
  - retrieval
prerequisites:
  - de-lead-m2-04
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the seven principles of privacy by design"
    - "Describes how to embed privacy into data architecture rather than bolting it on"
    - "Identifies the privacy implications of data architecture decisions"
    - "Explains differential privacy as a technical approach to privacy-preserving analytics"
  keywords:
    - privacy by design
    - data minimisation
    - privacy-preserving analytics
    - differential privacy
    - consent management
    - PII classification
    - data flow mapping
  modelAnswer: |
    Privacy by Design (Ann Cavoukian, 1990s; now codified in GDPR Article 25) embeds privacy into systems from the start rather than adding it as a compliance afterthought. The seven principles: proactive not reactive; privacy as the default; privacy embedded into design; full functionality (not privacy vs security); end-to-end security; visibility and transparency; respect for user privacy.
    Architecture implications: data flow mapping identifies where PII enters, flows through, and exits systems — making privacy risks visible before implementation. Data minimisation is enforced architecturally: collect only required fields, default to shorter retention, prefer aggregates over individual records in analytics. PII classification (direct, indirect, special category) determines which controls apply to each field.
    Privacy-preserving analytics enables data use while protecting individuals. Aggregation (no individual-level output), k-anonymity (each combination of quasi-identifiers appears ≥k times), and differential privacy (add calibrated noise so individual contributions are indistinguishable) are the primary techniques. Differential privacy is the gold standard for statistical query outputs — used by Apple, Google, and US Census.
    A Lead's architectural responsibility: ensure that privacy controls are enforced at the data layer (not just application layer), that consent signals flow through the data pipeline, and that privacy-by-default is the starting configuration rather than an opt-in.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "A data scientist requests access to raw individual-level session recordings to train a user behaviour model. What privacy-by-design alternative should the Lead propose?"
    options:
      - "Grant access with a data sharing agreement — the scientist accepts responsibility"
      - "Deny access — individual-level session data cannot be used for ML under GDPR"
      - "Provide a privacy-preserving alternative: aggregate session features, differentially private dataset, or federated learning on-device"
      - "Pseudonymise the recordings and grant access — pseudonymisation satisfies privacy requirements"
    correctIndex: 2
    explanation: "Privacy by design asks: can the legitimate purpose (training a behaviour model) be achieved without exposing individual-level raw data? Often yes: aggregate features (average session length, completion rates by cohort) may be sufficient. If not, differential privacy can add noise to individual-level data such that individual contributions are indistinguishable. Federated learning trains the model on-device without centralising data. Pseudonymisation is not anonymisation — it doesn't address the privacy risk of raw session recordings. The Lead's job is to enable the data science while protecting privacy, not simply to approve or deny."
  - type: FILL_BLANK
    question: "Privacy ___ requires that, in any system, privacy should be the default setting — a user who takes no action should have maximum privacy protection, not minimum."
    answer: "as the default"
    explanation: "Privacy as the default is Principle 2 of Privacy by Design. It means that new data products, features, and analytics should start with the most privacy-protective configuration. Opting OUT of privacy features should require explicit action; opting IN should be the default. This inverts the common pattern of 'collect everything, restrict later' to 'collect nothing, enable by exception.'"
  - type: SHORT_TEXT
    question: "What is differential privacy and why is it stronger than pseudonymisation for protecting individuals in aggregate statistics?"
    modelAnswer: "Differential privacy (Dwork, 2006) adds mathematically calibrated noise to query outputs so that whether any individual's data is included or excluded changes the output by at most a bounded amount (ε). An attacker cannot determine if a specific person's record was in the dataset by comparing query outputs. This provides formal, quantified privacy guarantees. Pseudonymisation replaces identifiers but the underlying data patterns may still enable re-identification (e.g. a unique combination of age, postcode, and diagnosis). Differential privacy protects against re-identification attacks on aggregate statistics by design — individual contribution is mathematically indistinguishable within the ε-DP guarantee. Used by Apple for keyboard analytics, Google for Chrome usage data, and the US Census for population statistics."
microCheckpoint:
  question: "What is the core architectural principle of privacy by design?"
  answer: "Privacy is embedded into system architecture from the beginning, not added as a compliance afterthought. Systems are designed to collect minimal data, protect it by default, and enable legitimate use through privacy-preserving techniques — rather than collecting everything and adding restrictions later."
retrieval:
  recall: "Name four of the seven principles of Privacy by Design."
  explain: "Explain data flow mapping and how it reveals privacy risks before implementation."
  mistakeId: "privacy-add-on-not-by-design"
---

# The Session Recording Request

The data science team wanted to train a user behaviour model on 6 months of session recordings — keystroke timing, scroll patterns, click paths. Raw. Individual-level. Hundreds of millions of events per learner. "It would produce a significantly better recommendation model," the data scientist said. The Lead Data Engineer was already thinking about the architecture. Not "can we?" — but "how do we achieve the goal while protecting the people in the data?"

# Privacy by Design: The Seven Principles

Ann Cavoukian's framework (now Article 25 GDPR):

```
1. PROACTIVE NOT REACTIVE
   Anticipate and prevent privacy risks before they occur.
   Don't wait for a breach to add privacy controls.

2. PRIVACY AS THE DEFAULT
   Maximum privacy is the starting configuration.
   Users who take no action have full protection.
   Opt-out of collection is easier than opt-in to protection.

3. PRIVACY EMBEDDED INTO DESIGN
   Privacy is a core component, not a bolt-on add-on.
   Architecture decisions reflect privacy from day one.

4. FULL FUNCTIONALITY (POSITIVE-SUM)
   Privacy AND security AND functionality — not privacy OR security.
   Privacy is not an excuse for worse products.

5. END-TO-END SECURITY
   Data is protected throughout its entire lifecycle —
   collection, storage, processing, sharing, disposal.

6. VISIBILITY AND TRANSPARENCY
   Systems are open about what data is collected and why.
   Verifiable by third parties.

7. RESPECT FOR USER PRIVACY
   User-centric: user consent, access rights, and erasure rights
   are easily exercisable.
```

## Data Flow Mapping

Before building, map where PII flows — makes privacy risks visible before implementation.

```
Data Flow Map: Learner Session Data

Collection point:
  Browser → [session recording SDK] → API endpoint
  PII at entry: IP address, user_id, keystroke timing, content viewed

Storage:
  Raw store: S3 (eu-west-1)
  PII classification: HIGH (direct identifier user_id + indirect IP)
  Encryption: AES-256 at rest, TLS in transit
  Access: session-recording-service role only

Processing:
  ML feature extraction → aggregate behavioural features
  PII transformation: user_id → anonymous cohort_id (hash + salt)
  Output: NO individual-level records in training dataset

Analytics access:
  Aggregated metrics only (no individual session visible)
  Data retention: 90 days raw, aggregated features indefinitely

Disposal:
  Raw sessions deleted after 90 days (automated retention job)
  Audit log of deletion retained (no PII, just metadata)

Privacy risks identified:
  ⚠️ IP address retained in raw store — enables re-identification
  ⚠️ Keystroke timing with user_id — highly sensitive combination
  ✓ Training dataset: cohort-level only — acceptable
```

## PII Classification Schema

```sql
-- Annotate PII classification in data catalogue
CREATE TABLE column_classifications (
    schema_name  TEXT,
    table_name   TEXT,
    column_name  TEXT,
    pii_class    TEXT CHECK (pii_class IN (
                   'DIRECT',     -- name, email, phone, NI number
                   'INDIRECT',   -- IP, device ID, cookie, postcode
                   'SPECIAL',    -- health, biometric, political, race
                   'NONE'
                 )),
    sensitivity  TEXT CHECK (sensitivity IN ('HIGH', 'MEDIUM', 'LOW', 'NONE')),
    retention_days INT,
    notes        TEXT
);

-- Example entries
INSERT INTO column_classifications VALUES
  ('public', 'users', 'email',        'DIRECT',   'HIGH', 365, 'Primary contact'),
  ('public', 'users', 'ip_address',   'INDIRECT', 'HIGH', 30,  'Delete after 30 days'),
  ('public', 'users', 'tier',         'NONE',      'LOW', NULL, 'Not personal data'),
  ('raw', 'sessions', 'keystroke_ms', 'INDIRECT', 'HIGH', 90,  'Aggregate before exposing');
```

## Privacy-Preserving Analytics

```
Technique comparison:

  AGGREGATION (simplest):
    Output: COUNT(*) GROUP BY cohort — no individual
    Privacy: Strong for group ≥ 100; weak for small groups
    Limitation: Small-cell risk (groups of 1 = identified)

  K-ANONYMITY:
    Each quasi-identifier combination appears ≥k times (k=5 minimum)
    Age+postcode+gender combination ≥5 rows
    Limitation: Homogeneity attacks if all k rows have same sensitive value

  DIFFERENTIAL PRIVACY (strongest):
    Add noise N(0, σ²) calibrated to privacy budget ε
    Individual contribution indistinguishable with probability e^ε
    Used by: Apple (keyboard analytics), US Census, Google Chrome
    Cost: reduced accuracy (noise degrades precision for small counts)

Python example (using IBM diffprivlib):
  from diffprivlib import mechanisms
  
  true_count = 1523  # actual users completing a lesson
  private_count = mechanisms.Laplace(epsilon=1.0, sensitivity=1).randomise(true_count)
  # Returns: e.g. 1521 (close enough for strategic decisions; individual contribution hidden)
```

## Consent Architecture

```
Consent signals must flow through the data pipeline:

  User grants: analytics consent ✓, personalisation consent ✓, marketing consent ✗
       │
       ▼
  Consent store (PostgreSQL): {user_id, purpose, granted, granted_at, version}
       │
       ▼ (event-driven sync to data warehouse)
  Warehouse: dim_consent (user_key, purpose, consent_at, revoked_at)
       │
       ▼ (filter applied at mart layer)
  Analytics queries: WHERE consent = true AND purpose = 'analytics'
  
  Revoking consent:
    User revokes marketing consent → consent store updated → 
    marketing mart refreshes → user excluded from next marketing export
    (Near-real-time propagation required for GDPR compliance)
```

## Common Mistakes

> **Privacy as the Last Step**
> Adding anonymisation to a system after it was built to collect everything rarely works well. Fields collected "just in case" are hard to remove once downstream systems depend on them. Design for minimum collection from the start.

> **Conflating Pseudonymisation with Anonymisation**
> Pseudonymised data is still personal data under GDPR. Using it in a data science workflow without appropriate controls creates regulatory risk. Apply differential privacy or genuine aggregation for analytics that should be "anonymous."

## Mental Model

Think of privacy by design as **structural engineering for data systems**. A privacy requirement is not a decorative add-on applied at the end — it is a load-bearing requirement that shapes the structure from the foundation. A building where fire safety was added after construction (sprinklers retrofitted into walls, fire exits cut through load-bearing walls) is more dangerous and more expensive than one designed with fire safety from the start. Privacy controls designed into the architecture are more robust, cheaper, and better for users than those bolted on after.

**Mini Summary**: Privacy by design embeds privacy into architecture from the start — proactive, default-protective, functional, and user-centric. Data flow mapping reveals PII risks before implementation. PII classification determines which controls apply to each field. Privacy-preserving techniques (aggregation, k-anonymity, differential privacy) enable analytics without individual-level exposure. Consent signals must flow through data pipelines and be queryable in analytics.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium wants to publish an anonymised dataset for researchers studying learning behaviour. The dataset would contain: completion events, XP earned, lesson difficulty, time spent per lesson, and inferred session patterns — but no name, email, or explicit user ID.

Design the privacy architecture:
1. Draw the data flow map for creating and publishing this research dataset.
2. Apply k-anonymity: what quasi-identifiers are present and what k value do you recommend?
3. Apply differential privacy to the publication mechanism: what queries would be permitted and how would you calibrate ε?
4. What consent basis is required and how would you verify it?

---

# Integration

**Mathematics**: Differential privacy's formal guarantee is: for any two neighbouring databases D and D' (differing in exactly one individual's record), and any output S: P(M(D) ∈ S) ≤ e^ε × P(M(D') ∈ S). The privacy budget ε quantifies the trade-off: ε = 0 is perfect privacy (output is independent of data — useless); ε = ∞ is no privacy (output reveals everything). Practical ε values: ε = 0.1 (strong privacy, significant noise), ε = 1 (balanced), ε = 10 (weak privacy, minimal noise). The **composition theorem** states that k sequential ε-DP mechanisms give k×ε-DP total — privacy budget is consumed with each query. This is why privacy-preserving analytics systems track cumulative privacy budget consumption and reject queries that would exceed the budget.

**Sciences**: Privacy by design mirrors **sterile technique** in surgery and microbiology. Sterile technique doesn't add sterility after the procedure — it maintains sterility throughout by design: sterile fields, sterile instruments, gowned practitioners. Contamination introduced at any point propagates. Adding disinfectant at the end doesn't undo mid-procedure contamination. Similarly, privacy added after PII collection doesn't undo the data that was already collected, stored, and potentially accessed. The Lead's architectural mandate is surgical sterility for data: design for minimum PII from the first data collection event.

---

# The Synthetic Dataset

The data science team got their training dataset — not raw sessions, but a differentially private feature set aggregated at the cohort level with ε=1.0 added noise. The model trained on it achieved 94% of the accuracy of a hypothetical model trained on raw data. "We gave up 6% accuracy," the data scientist said. "And we protected every learner in the dataset." The CDO reviewed the privacy impact assessment. The research dataset was published with a data use agreement. The Lead Data Engineer closed the data flow map. Privacy by design was not a constraint. It was engineering.
