---
id: fe-app-m7-06
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m7
moduleTitle: "Module 7: Frontend Engineering Habits"
moduleGlyph: "🛠️"
moduleSortOrder: 7
topicSlug: code_quality
topicTitle: "Code Quality"
topicSortOrder: 2
lesson: reusable_components
title: "Reusable Components"
sortOrder: 3
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
    - "Explains what makes a component reusable (single responsibility, configurable)"
    - "Identifies duplication in HTML/CSS and extracts it into a reusable pattern"
    - "Uses CSS classes to style reusable components rather than element-specific rules"
    - "Explains the DRY principle and how it applies to frontend code"
    - "Recognises when not to abstract (YAGNI — you aren't gonna need it)"
  keywords: [reusable, DRY, single-responsibility, abstraction, component, extract, duplicate, class, configurable, YAGNI]
  modelAnswer: |
    A reusable component has a single responsibility, is configurable through classes or
    attributes, and can be used in multiple contexts without modification. The DRY
    principle (Don't Repeat Yourself) means extracting repeated patterns into a single
    source: one .card class styles all cards. Premature abstraction (abstracting before
    duplication exists) creates complexity without benefit — YAGNI warns against this.
    Abstract when you have 3+ instances of the same pattern.
guidedSteps:
  - id: fe-app-m7-06-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You have three different card types on a page: product card, blog card, team card. All share the same background, border-radius, and shadow. What is the correct DRY approach?
    inputConfig:
      options:
        - "Copy the shared CSS into each card's separate class"
        - "Create a base .card class with shared styles; use modifier classes (.card--product) for differences"
        - "Use inline styles for the shared properties"
        - "These cards are different — they should have completely separate CSS"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Create a base .card class with shared styles; use modifier classes (.card--product) for differences"]
      rejectedFeedback: "DRY: extract shared properties into .card. Differences go in modifier classes (.card--product, .card--blog). If the shared style needs updating (change border-radius), you update one rule. With copied CSS, you update three — and easily miss one, creating inconsistency."
    hint: "The DRY principle: Don't Repeat Yourself."
    reflectionPrompt: "The DRY principle in CSS: every duplicated rule is a future consistency bug. When the design changes (it always does), duplicated rules create divergence — one instance gets updated, others don't. Extract shared styles; only keep what's truly different in each variant."

  - id: fe-app-m7-06-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The rule of thumb for when to abstract: abstract when you have ___ or more instances of the same pattern.
    inputConfig:
      placeholder: "3"
    markingRule:
      matchMode: CONTAINS
      accepted: ["3", "three", "2", "two"]
      rejectedFeedback: "A common rule: abstract at 3 instances (Rule of Three). One instance = no pattern. Two instances = possible coincidence. Three instances = a clear pattern worth abstracting. Abstracting at the first instance (premature abstraction) creates complexity before you know the full pattern."
    hint: "Too early is over-engineering; too late is DRY violation."
    reflectionPrompt: "The Rule of Three (originally from Martin Fowler) balances two failure modes: premature abstraction (too early — the abstraction is wrong or unnecessary) and DRY violation (too late — duplication creates maintenance debt). Three instances establishes a pattern worth abstracting."

  - id: fe-app-m7-06-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 2-3 sentences what YAGNI means and why it matters for component design.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [YAGNI, need, premature, abstract, future, now, complex, simple]
      rejectedFeedback: "YAGNI = You Aren't Gonna Need It. Don't build abstractions for requirements you don't have. If you only have one card type, don't build a complex card variant system for hypothetical future variants. Build the simplest thing that works now. Extend when the need is real."
    hint: "YAGNI is a warning against building things before they're needed."
    reflectionPrompt: "YAGNI and DRY create a productive tension. DRY says: abstract repeated things. YAGNI says: don't abstract things you might repeat. The resolution: wait for the duplication to exist before abstracting. Real repetition is always a better guide than imagined repetition."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of these is a single-responsibility component?"
    options:
      - "A component that handles: user display, data fetching, error states, and routing"
      - "A button component that renders a styled button with optional icon and label"
      - "A component that wraps the entire page including nav, content, and footer"
      - "A 'utility' component that does everything the page needs"
    correctIndex: 1
    feedback: "A button component has one responsibility: render a button. It can be configured (icon, label, variant) but doesn't fetch data, handle routing, or manage global state. Single responsibility makes components testable, reusable, and replaceable. Components with many responsibilities are fragile and hard to reuse."
  - type: MULTIPLE_CHOICE
    question: "DRY stands for:"
    options:
      - "Default Responsive Yield"
      - "Don't Repeat Yourself"
      - "Design, Refactor, Yield"
      - "Dynamic Responsive Yield"
    correctIndex: 1
    feedback: "Don't Repeat Yourself. Every piece of knowledge should have a single, authoritative representation. Repetition creates divergence: two copies of the same logic can evolve independently. DRY is about knowledge/intent, not literally about copying text — the same operation in two genuinely different contexts is not a DRY violation."

retrieval:
  recall: "Explain the DRY principle and give a CSS example of applying it to a shared card style."
  explain: "Explain the Rule of Three for abstraction and why it balances premature abstraction with DRY violations."
  mistakeId:
    code: "Three card types each with identical background: white, border-radius: 12px, box-shadow: ... in separate classes"
    answer: "Extract shared styles: .card { background: white; border-radius: 12px; box-shadow: ...; }. Use modifier classes for differences: .card--product { }, .card--blog { }. Now updating the shared style requires one change instead of three. DRY applied."
---

# Hook

Copy-paste is the fastest way to write code. It is also the fastest way to accumulate maintenance debt.

Every duplicated block of CSS is two things you need to update instead of one. Every duplicated HTML pattern is an inconsistency waiting to happen. Reusable components — clear, composable, configurable — are the antidote.

# Lore Introduction

*"The Academy's scroll-binding workshop,"* says Master Aelindra, *"uses one template for each scroll type. New scrolls are not hand-crafted from scratch — they follow the template. When the Academy updates its seal, every new scroll uses the new version automatically. Reusable components are the Academy's template system."*

# Core Learning

## Concept Introduction

**What makes a component reusable:**
1. **Single responsibility** — does one thing well
2. **Configurable** — accepts variations without modification
3. **Self-contained** — doesn't depend on external state it doesn't control
4. **Named clearly** — its purpose is obvious

**DRY applied to CSS:**
```css
/* Before DRY — three card types, identical base styles repeated */
.product-card { background: white; border-radius: 12px; box-shadow: ...; padding: 24px; }
.blog-card    { background: white; border-radius: 12px; box-shadow: ...; padding: 24px; }
.team-card    { background: white; border-radius: 12px; box-shadow: ...; padding: 24px; }

/* After DRY — shared base, only differences in modifiers */
.card { background: white; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,.08); padding: 24px; }
.card--product { border-top: 4px solid var(--color-primary); }
.card--blog    { display: grid; grid-template-columns: 120px 1fr; }
.card--team    { text-align: center; }
```

**Rule of Three: when to abstract**
- 1 instance: write it directly
- 2 instances: note the similarity; maybe wait
- 3 instances: extract the shared pattern

**YAGNI: when NOT to abstract**
- Don't build a variant system for one card type
- Don't abstract until the duplication exists
- Premature abstraction creates wrong abstractions

## Why It Matters

Reusable components are the core economic idea of modern frontend work — build once, use everywhere, fix once:

- One Button component means one place to fix the focus style, not forty copy-pasted variants drifting apart
- Reuse enforces visual consistency automatically; users experience one product, not a patchwork
- Designing for reuse forces clearer thinking: what varies (props) and what doesn't (the component's core)

Every design system — from small team libraries to industry giants like Material UI — is this lesson applied at scale. Learning to spot the repeating pattern is the skill itself.

## Common Mistakes

- Abstracting at the first instance (before the pattern is clear)
- Never abstracting (duplication accumulates into maintenance nightmare)
- Over-configuring a component (too many props/classes create complexity)
- Naming components for their appearance (`BigBlueButton`) not purpose (`PrimaryButton`)

## Mental Model

Think of reusable components as LEGO bricks versus hand-carved pieces. A hand-carved part fits one model perfectly — and nothing else, and if it cracks you re-carve it from memory. LEGO bricks are standardised: studs on top, tubes underneath, infinite combinations. A good component is a brick — a defined shape (its appearance and behaviour) with standard connection points (its props). You stop building pages from scratch and start assembling them, and improving one brick instantly upgrades every model that uses it.

## Mini Summary

- ✔ DRY: extract shared CSS into base classes; use modifiers for differences
- ✔ Rule of Three: abstract when you have 3 instances of the same pattern
- ✔ YAGNI: don't build abstractions for requirements you don't have yet
- ✔ Single responsibility: one component does one thing
- ✔ Configure via classes or attributes; avoid hard-coding context-specific values

# Guided Practice Quest

**The Template Workshop** — three questions on component reusability. Steps in `guidedSteps`.

# Solo Practice Quest

Look at the HTML and CSS for a hypothetical page with four buttons (primary, secondary, disabled, danger). Extract the shared CSS into a base `.btn` class and write modifier classes for each variant. Show before and after CSS and explain what changed and why.

# Integration

**Connecting to Mathematics — Abstraction and Function Factoring**

In mathematics, factoring extracts common terms: `ax + ay = a(x + y)`. The shared factor `a` is expressed once. DRY in code is identical: `background: white; border-radius: 12px` appearing in three rules is factored out into `.card { background: white; border-radius: 12px; }`. The modifier rules are the remaining terms after factoring. This is not a coincidence — both are applications of the mathematical principle of expressing shared structure once rather than repeating it.

# Lore Conclusion

*"Every repeated line of CSS is a line that diverges,"* says Master Aelindra. *"Update one copy, forget the other, and the design splits. Extract the shared essence into one place. Let the variants express only what makes them different. That is the template approach — and it has worked for three hundred years."*

---
