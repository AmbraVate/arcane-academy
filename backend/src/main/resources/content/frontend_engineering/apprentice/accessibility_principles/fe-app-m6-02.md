---
id: fe-app-m6-02
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
lesson: inclusive_design
title: "Inclusive Design"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m6-01]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the difference between accessible design and inclusive design"
    - "Describes the curb-cut effect with a web example"
    - "Lists the three inclusive design principles (Microsoft)"
    - "Gives an example of designing for one person that benefits many"
    - "Explains why inclusive design is a design methodology, not a checklist"
  keywords: [inclusive design, curb-cut, universal design, persona, spectrum, methodology, empathy]
  modelAnswer: |
    Accessible design focuses on meeting the needs of disabled users. Inclusive design
    is a broader methodology: recognise exclusion, learn from diverse people, and solve
    for one to extend to many. The curb-cut effect shows that features designed for
    disabled users (ramps, captions, alt text) often benefit a much wider audience.
    Inclusive design is about methodology — understanding who is excluded and why —
    not just compliance.
guidedSteps:
  - id: a11y-incl-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which statement best describes the "curb-cut effect"?
    inputConfig:
      options:
        - "Ramps are cheaper to build than stairs"
        - "Features designed for disability often benefit a wider population"
        - "Inclusive design only applies to physical spaces"
        - "Curb cuts make pavements look more professional"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Features designed for disability often benefit a wider population"]
      rejectedFeedback: "The curb-cut effect: wheelchair ramps were mandated for disabled people — but cyclists, pushchair users, delivery workers, and elderly people use them far more by volume. Designing for disability often produces universally better solutions."
    hint: "Who uses a wheelchair ramp besides wheelchair users?"
    reflectionPrompt: "Correct. The curb-cut effect is foundational to inclusive design thinking. Solving for constrained users produces solutions that are often superior for everyone."

  - id: a11y-incl-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Microsoft's inclusive design framework has three principles.
      The first is: "Recognise ___."
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: [exclusion, "exclusion"]
      rejectedFeedback: "The three Microsoft inclusive design principles are: 1) Recognise exclusion, 2) Learn from diversity, 3) Solve for one, extend to many."
    hint: "The first step is to notice that your design is leaving someone out."
    reflectionPrompt: "Correct. You cannot fix exclusion you don't notice. The first step in inclusive design is developing the awareness to see who a design might be failing."

  - id: a11y-incl-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Give one example from the web where a feature designed for a specific need
      benefits many other users.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [caption, keyboard, alt, contrast, dark mode, voice, search, text]
      rejectedFeedback: "Examples: captions (designed for deaf users, used by people in noisy spaces or learning the language), keyboard navigation (for motor disabilities, used by power users), dark mode (originally for low-light sensitivity, popular generally), search (designed for navigation difficulty, used by everyone)."
    hint: "Think about video captions, keyboard shortcuts, or high contrast mode."
    reflectionPrompt: "Good example. Inclusive features consistently prove more useful than their original scope — because solving constraints forces better general solutions."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the difference between 'universal design' and 'inclusive design'?"
    options:
      - "They are the same concept with different names"
      - "Universal design aims for one solution for everyone; inclusive design embraces multiple solutions for different needs"
      - "Universal design is for physical spaces only; inclusive design is for digital"
      - "Inclusive design is a legal standard; universal design is optional"
    correctIndex: 1
    feedback: "Universal design seeks a single solution that works for everyone. Inclusive design recognises that one size may not fit all, and embraces adapting solutions for diverse needs — it is about the process of including people, not just the output."
  - type: MULTIPLE_CHOICE
    question: "Which best describes the phrase 'solve for one, extend to many'?"
    options:
      - "Design first for your most common user, then add accessibility features"
      - "Design for someone with a specific constraint — the solution often works for many others"
      - "One developer should own all accessibility work"
      - "Write one line of code that works for all browsers"
    correctIndex: 1
    feedback: "Designing for someone with a specific, constrained need forces creative problem-solving that produces solutions applicable far beyond the original user. Siri was developed for blind users; now everyone uses voice assistants."

retrieval:
  recall: "Name the three principles of Microsoft's inclusive design framework."
  explain: "Explain the curb-cut effect with one digital example."
  mistakeId:
    code: "We built our site to WCAG AA standard, so it's inclusive."
    answer: "WCAG compliance is a set of technical standards, not a guarantee of inclusion. Inclusive design goes further — it involves researching who is excluded, testing with diverse users, and making ongoing improvements. Compliance is a baseline, not a destination."
---

# Hook

There is a difference between accessibility and inclusive design.

Accessibility asks: "Does this meet the minimum standards for disabled users?" Inclusive design asks: "Who is being excluded by this design, and how can we solve for them in a way that improves the experience for everyone?"

The distinction matters. Accessibility is often treated as a compliance task — a checklist to complete. Inclusive design is a methodology — a way of thinking about who you are building for, and who you might be forgetting.

> Has a product ever been designed for someone other than you, and yet worked better for you because of it?

# Lore Introduction

Master Aelindra spreads a diagram across the workshop wall: a spectrum from one person with a specific constraint on the left, to a wide population on the right.

*"When you design for the edge,"* she says, *"you unlock the centre. A scribe who cannot hold a quill taught us how to enchant the pen to write without grip. Now every scribe, injured or not, tired or not, uses that enchantment."*

She draws an arrow from the constrained individual to the broad population.

*"Solve for one. Watch how many benefit."*

# Core Learning

## Concept Introduction

**Inclusive design** is a methodology for creating products that include as many people as possible, by starting with users who are most constrained.

### Microsoft's Three Principles of Inclusive Design

| Principle | Meaning |
|-----------|---------|
| **Recognise exclusion** | Identify who is being left out by current design decisions |
| **Learn from diversity** | Involve people with different needs in the design process |
| **Solve for one, extend to many** | Design for constrained users; the solution benefits many |

### The Curb-Cut Effect

Originally, kerb cuts (ramps at street corners) were mandated for wheelchair users. Now they are used by:
- Cyclists
- Parents with pushchairs
- Delivery workers with trolleys
- People with temporary injuries
- Elderly people with reduced mobility

A solution designed for one group became a universal improvement.

### Web Examples of the Curb-Cut Effect

| Originally designed for | Also benefits |
|------------------------|--------------|
| Video captions (deaf users) | People in noisy environments, language learners |
| Keyboard navigation (motor disabilities) | Power users, developers |
| Alt text (blind users) | Slow connections (image doesn't load), SEO |
| High contrast mode (low vision) | Users in bright sunlight |
| Voice input (motor disabilities) | Hands-free users, multitaskers |

## Why It Matters

Inclusive design produces better products — not just more equitable ones. The process of solving for constrained users forces creative solutions that often surpass what you would build when designing only for the "average" user (who does not really exist in your audience).

It also produces more robust, future-proof products. A site that works with a keyboard, a screen reader, voice control, and a touchscreen is a site that will survive the next decade of device diversity.

## Worked Examples

**Designing for someone who can only use one hand:**
- Result: touch gestures and keyboard shortcuts that make the experience faster for everyone

**Designing for someone with dyslexia:**
- Larger font sizes, increased line spacing, left-aligned text, sans-serif fonts
- Result: clearer, more readable content for all users

**Designing for someone with cognitive impairment:**
- Clear, consistent navigation; simple language; no time pressure on interactions
- Result: better user experience for anyone in a hurry, stressed, or unfamiliar with the product

## Common Mistakes

- Treating inclusive design as a separate phase rather than an integrated approach
- Designing "for" disabled people without involving them in the process
- Confusing inclusive design with universal design — the goal is not one perfect solution but a thoughtful process
- Thinking accessibility compliance = inclusive design — compliance is a floor, not a ceiling

## Mental Model

Inclusive design is not a **ramp bolted onto the side of a building** after it was built without one.

It is designing the *entrance* with a ramp and steps together — so both are first-class solutions, not one primary and one afterthought.

The question is not "how do we add accessibility?" but "how do we design so that exclusion is never the default?"

## Mini Summary

- Inclusive design is a methodology, not a checklist
- Three principles: recognise exclusion, learn from diversity, solve for one to extend to many
- The curb-cut effect: solutions for constrained users benefit many more
- Inclusive design produces better products for everyone
- Compliance with WCAG is a baseline — inclusive design goes further

# Guided Practice Quest

In this quest you will identify the curb-cut effect in a digital context, recall the three inclusive design principles, and give your own example of solving for one person that extends to many.

These three steps build the design mindset that makes accessibility second nature.

# Solo Practice Quest

Choose a digital product you use daily. Write 4–6 sentences identifying:
- One group of users who might be excluded by its current design
- One feature that was likely designed for one group but benefits many
- One change that would make it more inclusive — and describe who that change would help

# Integration

**Connecting to Psychology — Empathy and Perspective-Taking**

Inclusive design requires *perspective-taking* — the cognitive ability to mentally step into another person's experience. Psychologists distinguish between affective empathy (feeling what another feels) and cognitive empathy (understanding what another thinks or experiences without necessarily sharing the emotion).

Good designers use cognitive empathy: they systematically ask "what does this person experience?" for users with different needs, abilities, and contexts. Tools like empathy maps, personas, and user testing formalise this process.

The risk of empathy in design is *projection* — assuming others share your experience. "I don't need captions, so captions aren't important" is projection. Inclusive design corrects this by involving real diverse users in the design process, not relying on the designer's imagination alone.

# Lore Conclusion

The apprentice looks at the diagram one more time — the constrained individual on the left, the wide population on the right, the arrow connecting them.

*"Exclusion is not always intentional,"* Master Aelindra says. *"But it is always a choice — made by the things you failed to consider. Inclusive design is the discipline of noticing what you failed to consider."*

The diagram fades from the wall, but the principle remains.

---
