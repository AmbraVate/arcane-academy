---
id: fe-app-m3-15
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m3
moduleTitle: "Module 3: CSS Foundations"
moduleGlyph: "🎨"
moduleSortOrder: 3
topicSlug: visual_design
topicTitle: "Visual Design"
topicSortOrder: 3
lesson: consistency
title: "Consistency"
sortOrder: 5
difficulty: 2
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
    - "Explains why consistency reduces cognitive load for users"
    - "Uses CSS custom properties to enforce a consistent design system"
    - "Identifies inconsistency as a design smell (indicator of missing abstraction)"
    - "Applies consistent border-radius, shadow, and colour patterns across components"
    - "Explains the difference between visual consistency and visual monotony"
  keywords: [consistency, custom-property, design-system, reuse, cognitive-load, convention, pattern, token]
  modelAnswer: |
    Consistency means applying the same visual decisions (colours, spacing, radius,
    shadows) in the same contexts throughout a UI. It reduces cognitive load because
    users learn patterns once and apply them everywhere. CSS custom properties enforce
    consistency — defining --border-radius-card: 12px once ensures every card has
    the same radius. Inconsistency is a design smell indicating missing abstraction.
guidedSteps:
  - id: fe-app-m3-15-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A user notices that some buttons have 8px border-radius, others have 4px, and others are square. What is the primary problem?
    inputConfig:
      options:
        - "The browser cannot render different border-radius values on the same page"
        - "The inconsistency breaks user expectations — buttons appear to be different components"
        - "8px border-radius fails accessibility requirements"
        - "Round corners use more GPU power"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The inconsistency breaks user expectations — buttons appear to be different components"]
      rejectedFeedback: "Inconsistent styling signals to users that elements are different types with different behaviours. Users build mental models ('all buttons look like X'). Inconsistency breaks those models, causing confusion and reducing trust in the interface."
    hint: "Think about what a user infers when two buttons look different."
    reflectionPrompt: "Users rely on visual consistency to build mental models of an interface. 'This rounded purple thing is always a primary action button.' Break that consistency — even slightly — and users pause, question their model, and lose confidence. Consistency is trust."

  - id: fe-app-m3-15-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To ensure every card on the site has the same border-radius, you define it once:

      `:root { ___: 12px; }` then use `border-radius: var(___);` everywhere.
    inputConfig:
      placeholder: "--border-radius-card"
    markingRule:
      matchMode: CONTAINS
      accepted: ["--border-radius-card", "--border-radius", "--card-radius", "--radius"]
      rejectedFeedback: "CSS custom properties (--variable-name) enforce consistency: define once in :root, use everywhere as var(--variable-name). Change the value in one place — every card updates. This is the CSS equivalent of DRY (Don't Repeat Yourself)."
    hint: "Custom properties use the -- prefix and are accessed with var()."
    reflectionPrompt: "A complete design token system covers: colours, spacing, typography, border-radius, shadows, transitions, and z-index. Every visual decision becomes a named variable. The entire UI's appearance can be changed by updating a single file. This is how design systems work."

  - id: fe-app-m3-15-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the difference between visual consistency and visual monotony, and how to achieve the former without falling into the latter.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [consistent, monoton, variety, hierarchy, same, different, boring, pattern]
      rejectedFeedback: "Consistency = applying the same rules in the same contexts (all primary buttons look the same). Monotony = no visual variation at all. Avoid monotony through hierarchy (different sizes for different levels), emphasis (one dominant element), and purposeful variation (card vs modal have different shadows). The rule: vary intentionally, not accidentally."
    hint: "What makes a page feel 'flat' vs 'consistent'?"
    reflectionPrompt: "Consistency and interest are not opposites. A consistent type scale is consistent but varied (h1 is bigger than h2). A consistent colour palette uses the same hues but varies lightness for hierarchy. Monotony comes from applying everything the same size and weight — lack of hierarchy, not lack of variety."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why is a CSS custom property like `--shadow-card: 0 2px 8px rgba(0,0,0,0.1)` considered better than writing the shadow value directly in each rule?"
    options:
      - "Custom properties load faster than regular values"
      - "Define once, use everywhere — changing the variable updates all cards simultaneously"
      - "Box-shadow values cannot be written inline"
      - "Custom properties apply to all elements automatically"
    correctIndex: 1
    feedback: "Define once, use everywhere (DRY principle). When the design changes — perhaps the shadow needs more blur — update one line in :root. Every element using var(--shadow-card) updates automatically. Hardcoded values require finding and updating every occurrence."
  - type: MULTIPLE_CHOICE
    question: "A new feature has a slightly different shade of purple than the rest of the site. What does this signal about the codebase?"
    options:
      - "The developer intentionally used a different shade for emphasis"
      - "A missing abstraction — the colour should be defined in a variable, not hardcoded"
      - "The feature is in a different colour theme"
      - "Purple shades are automatically inconsistent in CSS"
    correctIndex: 1
    feedback: "A slight colour difference usually means the developer used a hardcoded hex value instead of var(--color-primary). This is a missing abstraction — the colour should be a variable. Inconsistency is a code smell pointing to missing design system adherence."

retrieval:
  recall: "List six types of CSS values that should be defined as custom properties for consistency."
  explain: "Explain why 'inconsistency is a design smell' — what does inconsistency usually indicate about the code?"
  mistakeId:
    code: "Hardcoding border-radius: 8px in some cards and 12px in others, with no variable"
    answer: "Define --border-radius-card in :root and use var(--border-radius-card) everywhere. Hardcoded values make future updates require finding every occurrence. Variables enforce consistency and make the single source of truth clear."
---

# Hook

Users do not consciously notice consistency. But they immediately notice inconsistency.

A button that looks slightly different on one page. A heading that uses a slightly different shade of blue. A card with a different border-radius. These micro-inconsistencies accumulate into a feeling of distrust — something is wrong, even if the user can't say what.

Consistency is the foundation of a trustworthy interface.

# Lore Introduction

*"The Academy's seal,"* says Master Aelindra, *"is identical on every scroll, every door, every uniform. Not because repetition is elegant, but because consistency signals reliability. The apprentice who sees the seal trusts the document instantly. Inconsistency would undermine every seal on every scroll."*

# Core Learning

## Concept Introduction

**Design tokens** are named values that store visual decisions:

```css
:root {
  /* Colour tokens */
  --color-primary:    hsl(262, 72%, 58%);
  --color-text:       hsl(220, 30%, 10%);
  --color-bg:         hsl(220, 20%, 98%);
  --color-border:     hsl(220, 13%, 85%);

  /* Spacing tokens */
  --space-4:  1rem;
  --space-6:  1.5rem;
  --space-8:  2rem;

  /* Component tokens */
  --border-radius-sm:   4px;
  --border-radius-md:   8px;
  --border-radius-lg:   12px;
  --shadow-card:        0 2px 8px rgba(0, 0, 0, 0.08);
  --shadow-modal:       0 8px 32px rgba(0, 0, 0, 0.16);
  --transition-default: all 150ms ease;
}

/* Consistent button */
.btn-primary {
  background: var(--color-primary);
  padding: var(--space-4) var(--space-6);
  border-radius: var(--border-radius-md);
  transition: var(--transition-default);
}
```

**Consistency checklist:**
- All primary buttons: same colour, size, radius, shadow
- All cards: same padding, radius, shadow
- All form inputs: same height, border style, focus ring
- All links: same colour, hover state
- All error states: same colour, icon pattern

## Why It Matters

Consistency reduces cognitive load — users learn patterns once and apply them everywhere. It creates trust: a consistent interface feels professional and reliable. It also makes maintenance easier: update a token, update the whole system.

## Common Mistakes

- Hardcoding values (`#7c3aed`) instead of using variables
- Inconsistent component styles caused by different developers using different values
- Confusing consistency with monotony — consistency in rules, variation in application

## Mental Model

Consistency is the grammar of an interface. You don't notice grammar while it's correct — you simply understand sentences effortlessly. Slip into broken grammar mid-paragraph and the reader stops *reading* and starts *decoding*. UIs work identically: when every primary button looks the same, users stop seeing buttons and just act; when the same action is a blue button here, a green link there, and an icon elsewhere, each screen becomes a fresh translation exercise. Style guides and component reuse aren't bureaucracy — they're the shared grammar that keeps users fluent in your product. Every inconsistency taxes them a little; the taxes compound.

## Mini Summary

- ✔ Consistency reduces cognitive load — users apply learned patterns everywhere
- ✔ Design tokens (CSS custom properties) enforce consistency at scale
- ✔ Inconsistency is a design smell — it means missing abstraction
- ✔ Define all visual decisions as named variables in :root
- ✔ Consistent rules + intentional hierarchy = consistency without monotony

# Guided Practice Quest

**The Master Token System** — three questions on enforcing consistency. Steps in `guidedSteps`.

# Solo Practice Quest

Audit a simple web page (or design three components yourself). Identify five places where a hardcoded value should be a CSS custom property. Refactor them: define the tokens in :root and replace the hardcoded values with var(). Show before and after CSS.

# Integration

**Connecting to Psychology — Hick's Law and Decision Fatigue**

Hick's Law states that the time to make a decision increases with the number and complexity of options. A consistent interface minimises decision-making: if all buttons look the same, the user's attention is never diverted to "what does this different button mean?" Inconsistency forces the user to categorise every element encountered — is this a primary action or a secondary one? Does this card behave like other cards? Each decision consumes cognitive resources. Consistency eliminates these micro-decisions, freeing the user's attention for the actual task.

# Lore Conclusion

*"A master craftsperson's work,"* says Master Aelindra, *"is recognisable even unsigned — every joint has the same precision, every surface the same finish. That is what consistency builds: a recognisable quality that users trust without needing to analyse. Build every component with the same care, using the same system. The result is not monotony — it is mastery."*

---
