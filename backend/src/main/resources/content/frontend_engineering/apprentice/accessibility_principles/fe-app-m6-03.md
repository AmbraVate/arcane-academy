---
id: fe-app-m6-03
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
lesson: accessibility_standards
title: "Accessibility Standards"
sortOrder: 3
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m6-02]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what WCAG stands for and who publishes it"
    - "Distinguishes between WCAG levels A, AA, and AAA"
    - "Describes the four POUR principles with one example each"
    - "States the most commonly required WCAG level for compliance"
    - "Explains why AA is considered the practical standard rather than AAA"
  keywords: [WCAG, POUR, perceivable, operable, understandable, robust, W3C, AA, conformance]
  modelAnswer: |
    WCAG (Web Content Accessibility Guidelines) is published by the W3C. It is organised
    around four principles (POUR): Perceivable, Operable, Understandable, Robust. Each
    criterion has three conformance levels: A (minimum), AA (standard), AAA (enhanced).
    Most legal requirements and best practice guidelines require WCAG 2.1 AA. AAA is
    aspirational — some criteria are not achievable for all content.
guidedSteps:
  - id: a11y-std-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does POUR stand for in the WCAG framework?
    inputConfig:
      options:
        - "Personalised, Optimised, Usable, Reliable"
        - "Perceivable, Operable, Understandable, Robust"
        - "Practical, Open, Unified, Responsive"
        - "Precise, Organised, Unique, Readable"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Perceivable, Operable, Understandable, Robust"]
      rejectedFeedback: "POUR is the four-principle framework of WCAG: Perceivable (can users perceive the content?), Operable (can they operate it?), Understandable (can they understand it?), Robust (does it work with assistive technologies?)."
    hint: "Each letter of POUR is a question: Can users perceive it? Operate it? Understand it? Use it with assistive technology?"
    reflectionPrompt: "Correct. POUR is the conceptual framework; the specific WCAG criteria are how each principle is measured. If you understand POUR, you understand why each WCAG criterion exists."

  - id: a11y-std-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Most legal accessibility requirements and industry standards require WCAG 2.1
      level ___.
    inputConfig:
      placeholder: "A, AA, or AAA"
    markingRule:
      matchMode: NORMALIZED
      accepted: [AA, "AA", "Level AA", "WCAG AA"]
      rejectedFeedback: "AA is the standard required by most legal frameworks (UK Equality Act, US ADA, EU directive) and industry best practice. A is too minimal; AAA is aspirational and not always achievable."
    hint: "It is the middle level — more than the minimum (A) but achievable by most sites."
    reflectionPrompt: "Correct. AA is the practical standard. When someone says 'your site must be accessible', they almost always mean WCAG 2.1 AA."

  - id: a11y-std-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe the 'Robust' principle in WCAG in your own words.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [assistive technology, screen reader, browser, compatible, technology, future]
      rejectedFeedback: "Robust means the content can be reliably interpreted by a wide variety of user agents, including assistive technologies like screen readers. This requires using valid, semantic HTML that AT can parse correctly."
    hint: "Robust is about whether the code works reliably with different tools — especially assistive technologies like screen readers."
    reflectionPrompt: "Good. Robust means your code is solid enough for different browsers and assistive technologies to interpret correctly. Semantic HTML is the primary tool for achieving this."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which WCAG principle requires that all functionality must be available to keyboard users?"
    options:
      - "Perceivable"
      - "Understandable"
      - "Operable"
      - "Robust"
    correctIndex: 2
    feedback: "Operable — users must be able to operate all UI components and navigation. Keyboard operability (WCAG 2.1.1) falls under this principle."
  - type: MULTIPLE_CHOICE
    question: "WCAG is published by which organisation?"
    options:
      - "The United Nations"
      - "Google"
      - "The W3C (World Wide Web Consortium)"
      - "The ISO (International Standards Organisation)"
    correctIndex: 2
    feedback: "The W3C (World Wide Web Consortium) develops and maintains WCAG through the Web Accessibility Initiative (WAI). The current version is WCAG 2.1; WCAG 2.2 was released in 2023."

retrieval:
  recall: "Name the four POUR principles and explain each in one sentence."
  explain: "Explain why AAA conformance is considered aspirational rather than required."
  mistakeId:
    code: "Our site passes WCAG A — we're accessible."
    answer: "Level A is only the minimum requirement — it addresses the most critical barriers but is not sufficient for most legal or ethical standards. Most regulations require AA. A site that only passes A may still have significant barriers for screen reader users, keyboard users, and users requiring sufficient colour contrast."
---

# Hook

"Our site is accessible" is a claim that needs evidence.

That evidence comes from a set of internationally recognised standards called WCAG — the Web Content Accessibility Guidelines. These guidelines define what accessible means in precise, testable terms: not "the site works for most people" but "text has a contrast ratio of at least 4.5:1" and "all functionality is operable via keyboard".

Understanding WCAG at a conceptual level — before diving into specific rules — means you understand *why* each criterion exists, not just what it says.

> Why do you think having a specific standard with measurable criteria is more useful than a general goal like "make the site accessible"?

# Lore Introduction

Master Aelindra places a thick, well-worn tome on the workbench.

*"Every craft guild has a codex,"* she says. *"A set of standards against which all work is measured — not to constrain creativity, but to ensure that every piece meets the baseline below which quality cannot fall."*

She opens to a page divided into four sections, each with a flowing initial letter: P, O, U, R.

*"The Web Accessibility Initiative produced this codex. Four principles. Dozens of criteria. Three levels of expectation. Understand the principles and the criteria become self-evident."*

# Core Learning

## Concept Introduction

**WCAG** — Web Content Accessibility Guidelines — is the international standard for web accessibility, published by the W3C (World Wide Web Consortium).

### The Four POUR Principles

| Principle | Question it answers | Example |
|-----------|--------------------|---------| 
| **Perceivable** | Can users perceive all content? | Alt text for images, captions for video |
| **Operable** | Can users operate all UI? | Keyboard navigation, sufficient time |
| **Understandable** | Can users understand content and interface? | Plain language, consistent navigation |
| **Robust** | Does the content work with assistive technologies? | Valid semantic HTML, ARIA |

### Conformance Levels

| Level | Description | Required for |
|-------|-------------|-------------|
| **A** | Minimum — critical barriers removed | Baseline |
| **AA** | Standard — most barriers addressed | Legal compliance, industry standard |
| **AAA** | Enhanced — further improvements | Aspiration; not required for all content |

**WCAG 2.1 AA is the target** for most projects. It includes criteria like:
- Text contrast ratio of at least 4.5:1
- All functionality available via keyboard
- No content that flashes more than 3 times per second
- Labels for all form inputs
- Meaningful page titles and headings

## Why It Matters

Without a shared standard, "accessible" means different things to different people. WCAG provides:
- A common language between designers, developers, and clients
- Testable criteria that can be verified automatically and manually
- Legal certainty — courts reference WCAG as the benchmark

Knowing WCAG at the principle level helps you understand *why* a criterion exists, making it easier to apply in novel situations where no specific rule exists.

## Worked Examples

**Perceivable — WCAG 1.1.1 (Level A): Non-text content**

Every image must have a text alternative:

```html
<!-- Fails: no alt -->
<img src="chart.png">

<!-- Passes: meaningful alt text -->
<img src="chart.png" alt="Bar chart showing a 40% increase in enrolment 2023-2025">

<!-- Passes: decorative image with empty alt -->
<img src="divider.png" alt="">
```

**Operable — WCAG 2.1.1 (Level A): Keyboard accessible**

All features must be usable with the Tab key and Enter/Space — no mouse required.

**Understandable — WCAG 3.3.2 (Level A): Labels or instructions**

Every form input must have a visible label — not just a placeholder (which disappears on typing).

**Robust — WCAG 4.1.2 (Level A): Name, Role, Value**

Interactive elements must expose their name, role, and state to assistive technologies.

## Common Mistakes

- Treating WCAG as a checklist to run once rather than a quality standard to design toward
- Relying only on automated tools — automated testing catches ~30% of issues; manual testing is essential
- Achieving AA and considering the work done — AA is a baseline, not an upper bound
- Confusing levels: level A is not "good enough" for most contexts

## Mental Model

Think of WCAG levels as **building fire codes**.

Level A is the minimum legal requirement to be allowed to open the building. Level AA is the standard that most reputable buildings exceed for safety and insurance. Level AAA is the gold standard — a hospital or care home might aim there.

You wouldn't open a building at minimum fire code and call it safe. Similarly, level A alone is not an accessibility achievement — it's the floor.

## Mini Summary

- WCAG is the international standard for web accessibility, published by the W3C
- Four principles (POUR): Perceivable, Operable, Understandable, Robust
- Three levels: A (minimum), AA (standard), AAA (aspirational)
- WCAG 2.1 AA is required by most legal frameworks and industry standards
- Automated testing catches ~30% of issues — manual testing with assistive technologies is essential

# Guided Practice Quest

In this quest you will identify the correct POUR principle for three accessibility criteria, recall the AA conformance level, and explain the Robust principle in plain language.

These three steps build the standards literacy that separates developers who can talk about accessibility from those who can act on it.

# Solo Practice Quest

Choose five criteria from WCAG 2.1 AA (available at w3.org/WAI/WCAG21/quickref/).

For each criterion:
1. State the criterion number and name
2. Identify which POUR principle it belongs to
3. Give one example of a webpage passing it, and one failing it

Write your findings as a short structured list.

# Integration

**Connecting to Mathematics — Measurement and Thresholds**

Many WCAG criteria specify precise mathematical thresholds. Colour contrast, for example, is measured using the contrast ratio formula defined in WCAG 1.4.3: the ratio of relative luminance between two colours. A ratio of 4.5:1 is required for normal text at AA; 3:1 for large text.

Relative luminance is calculated from linearised RGB values — a specific formula that models how human vision perceives brightness. Tools like the WebAIM Contrast Checker compute this automatically, but understanding that a "colour looks fine to me" is not a valid test — because it assumes typical colour perception and viewing conditions — is itself a valuable insight.

WCAG succeeds as a standard partly because it is *measurable*. Subjective criteria ("text should be readable") cannot be enforced. Mathematical thresholds can.

# Lore Conclusion

The apprentice closes the codex, having traced each of the four principles with a finger.

*"These are not arbitrary rules,"* Master Aelindra says. *"Each principle answers a fundamental question about what it means to access something. Can you perceive it? Can you operate it? Can you understand it? Can your tools interpret it?"*

*"Answer yes to all four, and you have built something that truly serves."*

The tome glows with quiet authority.

---
