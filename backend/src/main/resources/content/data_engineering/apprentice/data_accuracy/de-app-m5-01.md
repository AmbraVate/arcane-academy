---
id: de-app-m5-01
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
lesson: what_makes_data_useful
title: "What Makes Data Useful?"
sortOrder: 1
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines data quality in own words and identifies at least three dimensions
    - Explains why poor data quality has real-world consequences
    - Provides a concrete example of low-quality data causing a bad decision
    - Distinguishes between accuracy, completeness, and consistency
    - Reflects on how data quality affects trust in a system
  keywords: [quality, accuracy, completeness, consistency, trustworthy, reliable, dimensions]
  modelAnswer: |
    Data quality describes how well data fulfils its intended purpose. Key dimensions include accuracy (values reflect reality), completeness (no important values are missing), and consistency (the same fact is represented the same way everywhere). Poor data quality erodes trust — a decision based on wrong or missing data can cause financial loss, safety failures, or customer dissatisfaction. Engineers must design systems that validate, clean, and monitor data quality continuously.
guidedSteps:
  - id: de-app-m5-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of the following best describes "data quality"?
    inputConfig:
      options:
        - "The volume of data stored in a database"
        - "How well data fulfils its intended purpose and reflects reality"
        - "The speed at which data is retrieved from storage"
        - "Whether data is stored in a relational or non-relational format"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["How well data fulfils its intended purpose and reflects reality"]
      rejectedFeedback: "Data quality is about fitness for purpose — accuracy, completeness, and consistency — not volume or speed."
    hint: "Think about what makes data trustworthy and useful for decision-making."
    reflectionPrompt: "Why might two people disagree on whether a dataset is 'high quality'?"
  - id: de-app-m5-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "Data is only as useful as its ________ — if the values do not reflect reality, decisions based on them will be wrong."
    inputConfig:
      placeholder: "accuracy"
    markingRule:
      matchMode: CONTAINS
      accepted: [accuracy, correctness, truthfulness, reliability]
      rejectedFeedback: "Accuracy is the core dimension — data values must correctly reflect the real-world facts they represent."
    hint: "This word describes whether a value matches the real-world fact it claims to represent."
    reflectionPrompt: "Can you think of a time when inaccurate data led to a wrong outcome?"
  - id: de-app-m5-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why data quality matters for decision-making. Give one example of a consequence of poor data quality.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [quality, decision, wrong, accurate, example]
      rejectedFeedback: "A strong answer names the quality dimensions, links them to decisions, and gives a concrete negative consequence."
    hint: "Think about what happens when a hospital, bank, or shop uses inaccurate records."
    reflectionPrompt: "Who in an organisation is responsible for data quality — engineers, analysts, or everyone?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A customer database lists the same person at two different addresses with conflicting spellings of their name. Which data quality dimension is violated?"
    options: ["Completeness", "Accuracy", "Consistency", "Timeliness"]
    correctIndex: 2
    feedback: "Correct — having the same fact represented differently in the same system violates consistency."
  - type: MULTIPLE_CHOICE
    question: "Which statement best describes why data quality matters?"
    options:
      - "High-quality data uses less storage space"
      - "Decisions based on poor-quality data are likely to be wrong or harmful"
      - "Data quality only matters for large organisations"
      - "High-quality data always arrives faster"
    correctIndex: 1
    feedback: "Data quality directly affects the reliability of every decision made from that data — poor quality leads to poor outcomes."
retrieval:
  recall: "Name three dimensions of data quality and define each in one sentence."
  explain: "Explain why data quality is the responsibility of engineers, not just data analysts."
  mistakeId:
    code: "data quality only means no missing values"
    answer: "Data quality encompasses multiple dimensions: accuracy, completeness, consistency, timeliness, and validity. Missing values is just one issue — incorrect, inconsistent, or outdated values are equally damaging."
---

# Hook

Imagine a hospital that stores patient blood types incorrectly — not because of a system crash, but simply because a nurse typed the wrong value into a form. The data is present and the database is running perfectly. Yet when an emergency surgeon looks up the blood type before a transfusion, the record is wrong. The system worked. The data failed.

This is the central challenge of data quality. Data can exist, be accessible, even look correct — and still be wrong. For every system you build as a data engineer, the question is not just "does the data arrive?" but "is the data trustworthy?" Data quality separates systems that inform good decisions from systems that create dangerous ones.

Before you can validate, clean, or monitor data, you need to understand what quality actually means. What dimensions make data truly useful?

# Lore Introduction

Master Selvaris unrolled a long census scroll across the table. "Twelve villages," he said. "Twelve scribes. Twelve slightly different ways to record a family name." He tapped a village entry: 'Caldwell', then another: 'Coldwell', then a third: 'Cauldwell.' "Every scribe believed they were writing the truth. And yet when the Kingdom tried to levy taxes based on this Archive, three families were billed twice and two were missed entirely." He rolled the scroll back up. "The Archive is only as powerful as the quality of what we place inside it. Today you learn what quality truly means."

# Core Learning

## Concept Introduction

| Quality Dimension | Definition | Example Failure |
|-------------------|-----------|----------------|
| **Accuracy** | Values correctly reflect the real-world fact | Blood type recorded as O+ when patient is A+ |
| **Completeness** | No important values are missing | Order record has no delivery address |
| **Consistency** | Same fact is represented the same way everywhere | Customer listed as "Active" in one table, "1" in another |
| **Timeliness** | Data is current enough for its intended use | An address that was updated 3 years ago but not since |
| **Validity** | Values conform to expected format and range | Age field contains -5 or "twenty" |
| **Uniqueness** | No unintended duplicate records exist | Same customer appears twice with different IDs |

## Why It Matters

Every business decision, machine learning model, or report is only as trustworthy as the data underneath it. Poor data quality causes:

- Incorrect billing, wrong diagnoses, misdirected shipments
- Loss of customer trust when systems "know" wrong things about people
- Failed audits and regulatory penalties when records are inconsistent
- Engineers spending 80% of their time cleaning data instead of building

## Worked Examples

**Example 1: E-commerce Shipping**
An online store stores customer postcodes as free text. Some users type `SW1A 1AA`, others type `sw1a1aa`, others type `SW1A`. When the shipping system tries to calculate delivery costs, it fails on non-standard formats. Validity was never enforced.

**Example 2: Healthcare Database**
A patient's medication dosage is stored in milligrams in one system and micrograms in another. Both are technically "present and non-null," but the inconsistency between systems could lead to a 1000x overdose if the records are merged naively.

**Example 3: Retail Analytics**
A sales dashboard shows revenue is up 12% this quarter. But two stores reported their figures in the wrong currency. Accuracy was compromised at the point of entry, making the aggregate metric meaningless.

## Common Mistakes

- **Treating completeness as the only dimension**: Having no NULL values does not mean data is accurate or consistent. A field can be filled with the wrong value.
- **Assuming data quality is someone else's problem**: Engineers build the systems that capture, store, and transmit data. Every design decision affects quality.
- **Fixing data quality only after problems appear**: Quality must be enforced at ingestion, not discovered through failures in production.

## Mental Model

Think of data quality like the quality of ingredients in a recipe. Accuracy means the ingredient is what the label claims. Completeness means you have all the ingredients. Consistency means measurements use the same units throughout. Timeliness means the ingredients are fresh. Validity means none of them are wildly the wrong thing (salt instead of sugar). A beautiful dish requires all five properties — one failure ruins the whole result.

## Mini Summary

- ✔ Data quality means data is fit for its intended purpose
- ✔ Key dimensions: accuracy, completeness, consistency, timeliness, validity, uniqueness
- ✔ Poor quality causes bad decisions, lost trust, and wasted engineering effort
- ✔ Quality must be enforced at system design time, not fixed after failures
- ✔ Engineers share responsibility for data quality with analysts and data owners

# Guided Practice Quest

Work through the guided steps to sharpen your understanding of the dimensions of data quality and why each matters in real engineering contexts.

# Solo Practice Quest

Think of a real-world system you use — a bank app, a health tracker, a streaming service, or an online shop. Identify one scenario in each of these quality dimensions where the system might fail: accuracy, completeness, consistency, and timeliness. For each scenario, write two to three sentences describing the failure and its consequence. Then reflect: which quality dimension do you think is hardest to maintain at scale, and why?

# Integration

**Mathematics**: Statistical analysis assumes data is a true sample of the underlying population. Inaccurate or incomplete data introduces systematic bias — the statistics become measurements of the noise rather than the signal. Quality control in data engineering mirrors quality control in scientific measurement.

**Psychology**: The concept of "garbage in, garbage out" maps to cognitive biases — if the information feeding a decision is flawed, even sound reasoning will produce flawed conclusions. Humans tend to trust data presented in a system without questioning its quality, which is why engineers must build quality in rather than relying on end users to notice problems.

# Lore Conclusion

Master Selvaris placed a hand on your shoulder. "Every apprentice wants to learn how to query the Archive. But the greatest skill — the one that separates the architects from the scribes — is knowing how to keep it honest." He turned back to the shelves. "Learn to ask: is this data accurate? Is it complete? Is it consistent? Ask these questions before you ask anything else, and the Archive will never betray you."

---
