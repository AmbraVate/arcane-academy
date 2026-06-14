---
id: de-app-m1-05
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m1
moduleTitle: "Module 1: Understanding Data"
moduleGlyph: "📊"
moduleSortOrder: 1
topicSlug: what_is_data
topicTitle: "What is Data?"
topicSortOrder: 1
lesson: data_in_everyday_life
title: "Data in Everyday Life"
sortOrder: 5
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m1-01, de-app-m1-02, de-app-m1-03, de-app-m1-04]
integrationDomains: [psychology, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Identifies at least three personal data sources from daily life
    - Explains how that data is collected (actively vs passively)
    - Reflects on who holds the data and what they do with it
    - Considers a privacy or ethical implication of that data collection
    - Connects personal experience to professional data engineering work
  keywords: [personal, passive, active, collection, privacy, consent, tracking, digital, footprint]
  modelAnswer: |
    In everyday life we generate data constantly — through smartphone GPS, purchase transactions, fitness tracker steps, social media interactions, and website browsing. Much of this data is collected passively without active user input. Organisations use this data to personalise experiences and make operational decisions. As data engineers we build the systems that collect and process this data, which creates an ethical responsibility to handle it with care, transparency, and respect for privacy.
guidedSteps:
  - id: de-app-m1-05-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of the following is an example of data collected passively without your explicit input?
    inputConfig:
      options:
        - "Filling in a registration form on a website"
        - "Submitting a customer satisfaction survey"
        - "Your smartphone recording your location as you travel"
        - "Typing a product review on a retailer's website"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Your smartphone recording your location as you travel"]
      rejectedFeedback: "Passive data collection happens automatically without direct user action — GPS location tracking is a classic example. Forms, surveys, and reviews all require explicit user input, making them active collection."
    hint: "Passive means you did not actively do anything to produce the data — it was generated as a by-product of another activity."
    reflectionPrompt: "How does the difference between active and passive data collection affect users' awareness of their digital footprint?"
  - id: de-app-m1-05-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence: "The trail of data you leave behind through your online and digital activity is called your digital ________."
    inputConfig:
      placeholder: "footprint"
    markingRule:
      matchMode: CONTAINS
      accepted: [footprint, trail, shadow]
      rejectedFeedback: "A 'digital footprint' is the record of all digital activity a person generates — from browsing history and purchases to location data and social media interactions."
    hint: "Think of the mark left behind after you walk through soft ground."
    reflectionPrompt: "If you were building a system that collected data contributing to a user's digital footprint, what responsibilities would you have as the engineer?"
  - id: de-app-m1-05-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, describe one ethical concern that a data engineer should be aware of when building systems that collect personal data from everyday activities.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [consent, privacy, GDPR, personal, transparency, security, purpose, delete]
      rejectedFeedback: "Consider issues like informed consent, data minimisation, purpose limitation, or the security of sensitive personal data. GDPR establishes rights like the right to erasure that engineers must build into systems."
    hint: "Think about what users have a right to know and control about their own data."
    reflectionPrompt: "How might your approach to system design change if you imagined your own grandmother as one of the data subjects?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of the following best describes 'active data collection'?"
    options:
      - "A website recording which pages you visit automatically"
      - "A fitness tracker counting your steps as you walk"
      - "A user completing an online form to register an account"
      - "An app logging battery usage in the background"
    correctIndex: 2
    feedback: "Active collection requires explicit user input — filling in a form, submitting a rating, or uploading a file. Passive collection happens automatically without direct user action."
  - type: MULTIPLE_CHOICE
    question: "Under GDPR, what right allows a user to request that their personal data is permanently removed?"
    options: ["Right to Portability", "Right to Rectification", "Right to Erasure", "Right to Access"]
    correctIndex: 2
    feedback: "The Right to Erasure (also called the 'right to be forgotten') allows data subjects to request deletion of their personal data when it is no longer needed for its original purpose."
retrieval:
  recall: "In one sentence, define what a digital footprint is."
  explain: "Explain the difference between active and passive data collection and give one example of each."
  mistakeId:
    code: "users always know what data is being collected about them"
    answer: "Much data collection is passive and automatic — GPS tracking, browsing history, app usage analytics. Users are often unaware of the full extent of data collected, which is why transparency requirements like GDPR cookie consent exist."
---

# Hook

You woke up this morning and your phone recorded what time your alarm went off. You checked the news and a server logged which articles you read. You bought a coffee and the transaction was recorded with a timestamp, your card details, and the merchant's location. You walked to work and your phone's accelerometer noted every step.

By the time you sit down to start your day, dozens of organisations have already collected hundreds of data points about you — and you have not actively done anything to provide them. This is the data reality of modern life, and it is the raw material that data engineers spend their careers building systems to handle.

Understanding data in everyday life is not just interesting background knowledge — it is the foundation of your professional empathy as an engineer. The data you build systems to process is always someone's data.

# Lore Introduction

"Before you design a single pipeline," Master Selvaris said, pulling out a small leather journal, "I want you to think about your own morning." She set the journal on the table. "Every time a traveller passes through the city gates, the gatekeepers record it. Every market transaction is written in the merchant ledgers. Every letter sent through the postal relay is logged by the courier office." She tapped the journal. "Ordinary people live their lives — and every moment leaves a mark in someone's records. We are the keepers of those marks. Never forget that they belong to real people."

# Core Learning

## Concept Introduction

| Data Source | What Is Collected | Collection Type | Who Uses It |
|------------|------------------|-----------------|-------------|
| Smartphone | Location, app usage, call logs, contacts | Passive | Advertisers, network operators, app developers |
| Payment card | Transaction amount, merchant, timestamp, location | Passive | Banks, retailers, credit agencies |
| Fitness tracker | Steps, heart rate, sleep patterns, GPS route | Passive | Health app providers, insurers (with consent) |
| Social media | Posts, likes, follows, time spent, device info | Mixed (active + passive) | Advertisers, platform algorithm |
| Web browsing | URLs visited, search queries, time on page, clicks | Passive | Advertisers, analytics platforms |
| Smart home | Temperature settings, voice commands, energy usage | Passive | Device manufacturers, energy suppliers |
| Medical records | Diagnoses, prescriptions, test results, appointments | Active (mostly) | NHS, insurers, researchers (anonymised) |

## Why It Matters

Data engineers build the systems that collect, store, and process this everyday data. Understanding the human origin of that data creates:

- **Design empathy**: You will make better schema decisions when you consider what the data represents about a person
- **Privacy awareness**: GDPR and similar regulations exist because this data is sensitive and personal — engineers must implement technical controls (encryption, access controls, deletion jobs) to protect it
- **Purpose limitation**: Data collected for one purpose (processing a payment) must not silently be used for another (building a behavioural profile) without consent
- **Security responsibility**: A breach of personal data is not just a technical failure — it is a harm to real people

## Worked Examples

**Example 1: A Single Shopping Trip**
When you buy groceries with a loyalty card, the retailer captures: items purchased (basket analysis), time of visit (footfall patterns), store location (branch performance), card number (customer identity), and price paid (revenue tracking). This single trip generates structured transactional data used for operational, strategic, and marketing purposes simultaneously.

**Example 2: Streaming Service**
Every play, pause, rewind, and abandon event is logged with timestamps. This passive data stream powers recommendation engines, content investment decisions, and licensing negotiations. Engineers who build these event logging systems are directly shaping what gets recommended to millions of people.

**Example 3: NHS Patient Journey**
A patient's GP visit, prescription, blood test, and specialist referral each generate records across multiple systems. Engineers who connect these systems (a major challenge in healthcare) must balance clinical utility against strict privacy obligations under NHS data governance frameworks.

## Common Mistakes

- **"It's just anonymised"**: Anonymised data can often be re-identified when combined with other sources. Engineers should treat "anonymised" data with the same care as personal data until confident re-identification is impossible.
- **Collecting more than needed**: The principle of data minimisation (collect only what you need for the stated purpose) is both a legal requirement and good engineering practice. Over-collection creates storage, security, and compliance costs.
- **Ignoring the user experience of consent**: GDPR requires informed consent for certain types of processing. Engineers who implement consent mechanisms as an afterthought create poor user experiences and legal risk.

## Mental Model

Think of everyday life as a city full of measurement instruments. Every interaction — a purchase, a journey, a conversation — is simultaneously a human experience and a data-generation event. As a data engineer, you are one of the people who decides which instruments to install, what they measure, where the readings go, who can access them, and for how long they are kept. The technology is yours to design. The ethical responsibility comes with it.

## Mini Summary

- ✔ Everyday digital activity generates vast quantities of personal data continuously
- ✔ Data collection may be active (user input) or passive (automatic background capture)
- ✔ A "digital footprint" is the cumulative record of a person's digital activity
- ✔ GDPR establishes rights including access, rectification, and erasure of personal data
- ✔ Data engineers have an ethical responsibility to handle personal data with transparency, security, and purpose

# Guided Practice Quest

Work through the guided steps to practise identifying passive versus active data collection, and to reflect on the ethical responsibilities of engineers who build data collection systems.

# Solo Practice Quest

Spend 30 minutes mapping your own digital footprint from the past 24 hours. List every app, website, service, and physical device that may have collected data about you during that period. For each one: (1) identify what data was likely collected, (2) classify whether collection was active or passive, (3) identify who holds that data, and (4) note whether you gave explicit consent. Reflect on one data point in your list that surprises you, and consider what engineering decisions a data engineer building that system should make to protect your rights.

# Integration

**Psychology**: The concept of the "observer effect" — the idea that being observed changes behaviour — applies directly to digital data collection. Research in behavioural psychology shows that people make different choices when they know they are being tracked. This creates a paradox for data engineers: the most accurate behavioural data is collected when users are unaware of it, but ethically and legally that awareness (consent) is required. Engineers must design systems that are both legally compliant and that acknowledge this psychological reality.

**Sciences (Environmental)**: Just as environmental scientists study an ecosystem's "data" — temperature, species counts, pollution levels — captured passively from the environment, digital data engineers capture signals from human behaviour. The parallel is useful: just as ecologists must consider whether their measurement instruments are themselves disturbing the ecosystem, data engineers must consider whether their collection systems are affecting the behaviour they purport to measure.

# Lore Conclusion

Master Selvaris placed the leather journal back in her satchel. "Every record in this Archive was made by a person about another person — or about themselves. The census takers, the merchants, the healers — all of them were collecting data about human lives." She stood and looked directly at her apprentice. "We hold that responsibility now. The ink in your quill is not abstract. It touches real lives. Build every system as if the people in your data were standing in the room watching you work." She paused. "Because in a way, they always are."

---
