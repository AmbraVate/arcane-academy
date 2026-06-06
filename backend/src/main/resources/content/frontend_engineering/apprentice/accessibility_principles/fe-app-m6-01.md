---
id: fe-app-m6-01
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m6
moduleTitle: "Module 6: Accessibility Foundations"
moduleGlyph: "♿"
moduleSortOrder: 6
topicSlug: accessibility_principles
topicTitle: "Accessibility Principles"
topicSortOrder: 1
lesson: what_is_accessibility
title: "What is Accessibility?"
sortOrder: 1
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines web accessibility in your own words"
    - "Names at least three categories of disability that accessibility addresses"
    - "Explains the difference between permanent, temporary, and situational disability"
    - "Gives a real-world example of an inaccessible web feature and its impact"
    - "States why accessibility benefits all users, not just disabled users"
  keywords: [accessibility, disability, inclusive, WCAG, screen reader, permanent, situational, barrier]
  modelAnswer: |
    Web accessibility means designing and building websites that people with disabilities
    can use. It covers visual, auditory, motor, and cognitive disabilities. Accessibility
    exists on a spectrum — disabilities can be permanent, temporary, or situational. Good
    accessibility benefits everyone: captions help people in noisy environments; keyboard
    navigation helps power users; high contrast helps people in bright sunlight.
guidedSteps:
  - id: a11y-what-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of the following best describes web accessibility?
    inputConfig:
      options:
        - "Making websites load faster"
        - "Ensuring websites can be used by people with disabilities"
        - "Adding features specifically for blind users"
        - "Making websites compatible with all browsers"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Ensuring websites can be used by people with disabilities"]
      rejectedFeedback: "Accessibility is about removing barriers so that people with disabilities can perceive, understand, navigate, and interact with the web. It covers visual, motor, auditory, and cognitive disabilities."
    hint: "The word 'access' is the root — it is about removing barriers to access."
    reflectionPrompt: "Correct. Accessibility is about equitable access — ensuring the web is usable by people regardless of their abilities or the tools they use."

  - id: a11y-what-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A broken arm is an example of a ___ disability — it is real and limiting,
      but it will heal. The same person with the same limitation would benefit
      from accessible design during that time.
    inputConfig:
      placeholder: "type of disability"
    markingRule:
      matchMode: NORMALIZED
      accepted: [temporary, "temporary disability"]
      rejectedFeedback: "Temporary disabilities (broken arm, eye surgery recovery, illness) create real access needs for a limited time. Accessible design serves these users too."
    hint: "Is a broken arm permanent, temporary, or situational?"
    reflectionPrompt: "Correct. The permanent-temporary-situational spectrum shows that disability is not a fixed category — everyone experiences access limitations at some point."

  - id: a11y-what-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Name one example of an inaccessible web feature and explain who it affects and why.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [image, alt, colour, contrast, keyboard, caption, text, screen reader, button]
      rejectedFeedback: "Examples: an image without alt text (blind users can't perceive it), a form that only uses colour to indicate errors (colour-blind users miss the signal), a video without captions (deaf users can't follow the audio)."
    hint: "Think about something visual-only on a website. What would a blind or partially sighted user experience?"
    reflectionPrompt: "Good example. Accessibility barriers are often invisible to people who don't face them — which is why intentional design is necessary."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of these is an example of a situational disability?"
    options:
      - "Permanent blindness"
      - "A broken wrist"
      - "Using a phone in bright sunlight (hard to read the screen)"
      - "Colour blindness"
    correctIndex: 2
    feedback: "Situational limitations arise from context, not the person's body — bright sunlight making a screen unreadable, a noisy environment where audio can't be heard, one-handed use while carrying shopping."
  - type: MULTIPLE_CHOICE
    question: "Approximately what percentage of the global population has some form of disability?"
    options:
      - "2%"
      - "5%"
      - "15%"
      - "25%"
    correctIndex: 2
    feedback: "The WHO estimates approximately 15% of the global population — over 1 billion people — lives with some form of disability. This is not a niche audience."

retrieval:
  recall: "List the four main categories of disability that web accessibility addresses."
  explain: "Explain the permanent-temporary-situational framework and why it changes how we think about accessibility."
  mistakeId:
    code: "We only need to make our site accessible for blind users."
    answer: "Accessibility covers a broad spectrum: visual (blindness, low vision, colour blindness), motor (limited dexterity, tremors, paralysis), auditory (deafness, hearing loss), and cognitive (dyslexia, ADHD, memory impairment). Designing for only one group leaves large numbers of users underserved."
---

# Hook

Approximately one billion people — 15% of the global population — live with some form of disability.

Every website that ignores accessibility is a website that tells one billion people: *"This was not built for you."*

But accessibility is not just about compliance or ethics. It is about better design. The curb cuts on pavements were designed for wheelchair users — but they are used by cyclists, parents with pushchairs, and delivery workers with trolleys. Captions were designed for deaf viewers — but they are watched by millions of people in noisy environments or learning a second language.

> Think about a time you struggled to use a website. What made it difficult? Could that difficulty relate to an accessibility barrier?

# Lore Introduction

Master Aelindra leads the apprentice out of the workshop and along a corridor where pages from a hundred different websites line the walls.

*"Look closely,"* she says. *"Every one of these was built by someone who understood code. But how many were built by someone who understood people?"*

She stops at a page with no alt text on its images, no contrast between text and background, and form fields with no labels.

*"This enchantment is complete — every rune is correctly inscribed. And yet it is useless to anyone who cannot see the images, distinguish those colours, or navigate without a mouse. Technically correct. Practically exclusive."*

She turns to the apprentice.

*"A page that cannot be used by everyone is a broken page — regardless of how clean the code is."*

# Core Learning

## Concept Introduction

**Web accessibility** (often abbreviated as **a11y** — the 11 letters between a and y in "accessibility") is the practice of designing and building web content that can be perceived, understood, navigated, and interacted with by people with disabilities.

### Categories of Disability

| Category | Examples |
|----------|---------|
| **Visual** | Blindness, low vision, colour blindness |
| **Motor/Physical** | Limited dexterity, tremors, paralysis, missing limbs |
| **Auditory** | Deafness, hearing loss |
| **Cognitive** | Dyslexia, ADHD, memory impairment, autism |

### The Disability Spectrum

Disability is not binary. It exists on a spectrum that includes:

| Type | Description | Example |
|------|-------------|---------|
| **Permanent** | Long-term or lifelong | Blindness since birth |
| **Temporary** | Limits access for a period | Broken arm, post-surgery |
| **Situational** | Context-dependent limitation | Bright sunlight, noisy environment |

## Why It Matters

Inaccessible websites exclude real people from information, services, and participation. In many countries, accessibility is also a **legal requirement** — the UK Equality Act, the US ADA, and the EU Web Accessibility Directive all mandate accessible digital services for public-sector and often private-sector organisations.

Beyond compliance, accessible design is simply better design. Features built for accessibility — keyboard navigation, captions, high contrast, clear structure — improve the experience for everyone.

## Worked Examples

**Inaccessible pattern 1 — Image without alt text**

```html
<img src="hero-banner.jpg">
<!-- A screen reader says nothing — the image conveys no information -->
```

**Accessible version:**

```html
<img src="hero-banner.jpg" alt="Apprentice scribes working in the Arcane Academy workshop">
```

**Inaccessible pattern 2 — Error shown only with colour**

Using red text alone to indicate a form error excludes colour-blind users. Accessible version adds an icon and text description.

**Inaccessible pattern 3 — Button with no label**

```html
<button><img src="search-icon.svg"></button>
<!-- Screen reader: "button" — no context -->
```

```html
<button aria-label="Search"><img src="search-icon.svg" alt=""></button>
<!-- Screen reader: "Search, button" — clear and useful -->
```

## Common Mistakes

- Thinking accessibility is only for blind users — it covers motor, auditory, and cognitive needs too
- Building accessibility as an afterthought — retro-fitting is far harder than designing inclusively from the start
- Assuming developers with no disabilities cannot understand accessibility needs — empathy and testing bridges this
- Treating accessibility as a compliance box to tick rather than a design quality to build toward

## Mental Model

Think of accessibility as **building without stairs**.

A ramp at the entrance benefits everyone: wheelchair users, people with prams, delivery staff, and tired people who prefer not to climb steps. The ramp did not make the building worse for stair-users — it made it better for everyone else.

Accessible web design works the same way: features designed for access rarely hurt other users and often improve the experience universally.

## Mini Summary

- Accessibility means making web content usable by people with disabilities
- Four categories: visual, motor, auditory, cognitive
- Disability is permanent, temporary, or situational — it affects everyone eventually
- Inaccessible sites exclude users and may violate the law
- Accessible design benefits all users, not only those with disabilities

# Guided Practice Quest

In this quest you will define accessibility, identify the type of disability in a scenario, and describe an inaccessible feature and who it affects.

These three steps build the empathy and awareness that make accessibility thinking automatic rather than reactive.

# Solo Practice Quest

Visit any public-facing website and spend five minutes identifying accessibility issues.

Write 4–6 sentences covering:
- At least two issues you noticed (missing alt text, poor contrast, unlabelled buttons, etc.)
- Which category of disability each issue affects
- What the impact would be on a real user
- One thing you could easily fix

# Integration

**Connecting to Mathematics — Scale and Population**

15% of 8 billion people is 1.2 billion people with some form of disability. If your website serves 10,000 users, approximately 1,500 of them may have an accessibility need. If 20% of those face a significant barrier, that is 300 users your product fails every time they visit.

These are not abstract numbers — they are people who cannot complete a purchase, access information, or use a service. At scale, the cost of inaccessibility is enormous: in lost users, in legal risk, and in reputation.

Framing accessibility as a numbers problem does not capture its human dimension — but it does help organisations understand that accessibility is not charity. It is product quality.

# Lore Conclusion

The apprentice turns away from the inaccessible pages and faces the workshop again.

*"From now on, every enchantment you craft will carry a question,"* Master Aelindra says. *"Not just 'does it work?' — but 'does it work for everyone who needs it?' The answer to that question is what separates a scribe from an architect."*

The corridor of pages glows more softly as the apprentice begins to see them differently.

---
