---
id: fe-jun-m1-02
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m1
moduleTitle: "Module 1: React Foundations"
moduleGlyph: "⚛️"
moduleSortOrder: 1
topicSlug: why_react_exists
topicTitle: "Why React Exists"
topicSortOrder: 1
lesson: component_based_thinking
title: "Component-Based Thinking"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-01]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what a component is in own words"
    - "Identifies components in a real UI"
    - "Explains why components improve reusability"
    - "Distinguishes between a component and a plain HTML element"
  keywords: [component, reuse, encapsulation, composition, props, UI, tree]
  modelAnswer: |
    A component is a self-contained, reusable piece of UI that encapsulates its own structure, style, and behaviour. Components improve reusability by letting you define a UI element once and use it in many places with different data passed as props. This mirrors how physical products are built from interchangeable parts.
guidedSteps:
  - id: fe-jun-m1-02-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Which best describes a React component?"
    inputConfig:
      options:
        - "A CSS class that styles an element"
        - "A self-contained, reusable piece of UI that can accept data and return markup"
        - "A JavaScript function that manipulates the DOM directly"
        - "A server-side template"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A self-contained, reusable piece of UI that can accept data and return markup"]
      rejectedFeedback: "Components are the building blocks of React UIs. They accept data (props) and return what should appear on screen."
    hint: "Think of a component like a custom HTML element you design yourself."
    reflectionPrompt: "Components are functions. They take inputs (props) and return UI. This functional thinking applies everywhere in software engineering."
  - id: fe-jun-m1-02-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Name three components you could identify in a typical e-commerce product page."
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [button, image, nav, header, card, price, review, cart, badge, rating]
      rejectedFeedback: "Examples: ProductImage, PriceTag, AddToCartButton, ReviewList, RatingStars, RelatedProducts. Any self-contained, reusable UI piece counts."
    hint: "Look at the page sections that repeat or could be used on other pages."
    reflectionPrompt: "Component thinking is design thinking. Breaking a UI into components forces you to identify the natural boundaries in your interface."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why is component reusability valuable?"
    options:
      - "It makes the code run faster"
      - "It lets you write a UI element once and use it in many places with different data"
      - "It removes the need for CSS"
      - "It makes components invisible to the browser"
    correctIndex: 1
    feedback: "Reusability means less code, fewer bugs, and consistent UI. A Button component defined once looks and behaves the same everywhere — change it in one place, and it updates everywhere it's used."
retrieval:
  recall: "What are the three things a component typically encapsulates?"
  explain: "Why does breaking a UI into components reduce bugs?"
  mistakeId:
    code: "Every HTML element should be its own component"
    answer: "Components should represent meaningful, reusable UI concepts — not individual HTML tags. A Button component makes sense. A Span component for every span does not. The rule of thumb: extract a component when a piece of UI is reused or complex enough to benefit from isolation."
---

# Hook

Look at any modern website — Twitter, Spotify, GitHub. What do you see? Buttons that look the same everywhere. Cards that repeat in feeds. Navigation bars on every page. These aren't copy-pasted HTML — they're **components**: designed once, used everywhere.

Component-based thinking is not just a React concept. It's how modern software is built. LEGO bricks, not hand-carved sculptures.

# Lore Introduction

Aelindra leads you to the Academy's workshop, where apprentices are carving identical stone runes by hand for the third time this week.

*"Each rune takes an hour,"* she says. *"Yet they use the same ten symbols, carved slightly differently each time. This is waste."*

She holds up a mould. *"A component is this mould. Craft it once. Pour into it as many times as needed."*

# Core Learning

## Concept Introduction

A **component** is a self-contained, reusable piece of UI. It knows:
- What it **looks like** (its JSX/markup)
- What **data it needs** (its props)
- How it **behaves** (its event handlers and state)

| Concept | Meaning |
|---|---|
| **Encapsulation** | Component owns its own markup, logic, and (optionally) style |
| **Reusability** | Same component used in many places with different props |
| **Composition** | Complex UIs built by combining simple components |
| **Tree** | Components nest inside each other, forming a tree |

## Why It Matters

Component-based UIs are:
- **Consistent** — a Button component looks the same everywhere
- **Maintainable** — fix it in one place, fixed everywhere
- **Testable** — test each component in isolation
- **Scalable** — teams can work on different components in parallel

## Worked Example

```jsx
// A reusable Badge component
function Badge({ label, colour }) {
  return <span className={`badge badge-${colour}`}>{label}</span>;
}

// Used in many places with different props
<Badge label="New" colour="green" />
<Badge label="Sale" colour="red" />
<Badge label="Limited" colour="orange" />
```

One component. Three uses. Three different appearances based on props. If the badge design changes, you update one function.

## Common Mistakes

- **Making components too large.** If a component does too many things, it's hard to reuse and test. Aim for single responsibility.
- **Making components too small.** Wrapping every `<p>` tag in a component adds complexity with no benefit.
- **Duplicating logic across components.** If two components have similar logic, extract it into a shared component or hook.

## Mini Summary

- Components are self-contained, reusable UI units
- They encapsulate markup, data needs, and behaviour
- Complex UIs are built by composing simple components
- The rule: extract a component when something is reused or complex

# Guided Practice Quest

Work through the guided steps to identify and describe components from a real-world UI.

# Solo Practice Quest

Choose a web page you use regularly. Draw (or describe in writing) its component tree — break the page into components, then break those into sub-components. Aim for at least two levels of nesting. Write 3–4 sentences explaining where you drew the component boundaries and why.

# Integration

**Design Thinking — Atomic Design**

Brad Frost's Atomic Design methodology (2013) formalised component thinking for designers: atoms (buttons, inputs), molecules (search bar = input + button), organisms (header = logo + nav + search bar), templates (layout), pages. React's component model maps almost perfectly to this. Good collaboration between designers and engineers often begins with agreeing on the component vocabulary — what are the atoms, and what are the organisms? This shared language reduces miscommunication.

# Lore Conclusion

*"You see it now,"* Aelindra says, watching you sketch the component tree. *"The page is not a wall of stone. It is an assembly of crafted pieces. Each piece can be perfected independently, then joined into something greater."*

---
