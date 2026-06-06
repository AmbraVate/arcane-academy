---
id: fe-jun-m8-13
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m8
moduleTitle: "Module 8: Tailwind CSS"
moduleGlyph: "🎨"
moduleSortOrder: 8
topicSlug: component_styling
topicTitle: "Component Styling"
topicSortOrder: 5
lesson: styling_interactive_states
title: "Styling Interactive States"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m8-12]
integrationDomains: [design, ux]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly applies hover: and focus: prefixes with examples"
    - "Explains the disabled: prefix and how it changes cursor and opacity"
    - "Describes group-hover: and a use case for it (e.g. card hover revealing a button)"
    - "Explains why focus: styles are important for accessibility"
  keywords: [hover, focus, active, disabled, group, group-hover, state, interactive, accessible]
  modelAnswer: |
    Tailwind state variants use prefixes: `hover:bg-blue-700` changes background on hover, `focus:ring-2` adds a focus ring, `active:scale-95` gives a press effect. `disabled:opacity-50 disabled:cursor-not-allowed` styles disabled elements without JavaScript. `group` is added to the parent and `group-hover:` on children responds when the parent is hovered — perfect for card hover effects. Focus styles are critical for keyboard navigation accessibility — removing them without a replacement harms keyboard users.
guidedSteps:
  - id: fe-jun-m8-13-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A card should reveal a hidden 'View Details' button when the user hovers over the card. Which Tailwind approach achieves this without JavaScript?"
    inputConfig:
      options:
        - "Add `group` to the card div, and `opacity-0 group-hover:opacity-100` to the button"
        - "Add `hover:block` to the button — it will show on any hover"
        - "Use JavaScript's onMouseEnter to toggle a CSS class"
        - "Add `peer` to the card and `peer-hover:block` to the button"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Add `group` to the card div, and `opacity-0 group-hover:opacity-100` to the button"]
      rejectedFeedback: "`group` on the parent enables `group-hover:` on any descendant. The button starts invisible (`opacity-0`) and fades in when the parent card is hovered (`group-hover:opacity-100`)."
    hint: "You need the parent's hover state to affect a child. That is what `group` is for."
    reflectionPrompt: "What would `transition-opacity duration-200` add to the user experience here?"
  - id: fe-jun-m8-13-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write a fully accessible button with hover, focus, active, and disabled states using only Tailwind classes."
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [hover:, focus:, disabled:, ring, active:]
      rejectedFeedback: "A complete button might look like: `bg-blue-600 text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 active:scale-95 disabled:opacity-50 disabled:cursor-not-allowed transition-all`"
    hint: "Five state variants: base, hover, focus (ring!), active, disabled."
    reflectionPrompt: "Why is `focus:ring-2 focus:ring-offset-2` important for users who navigate with a keyboard?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why should you never remove focus styles with `focus:outline-none` without providing an alternative?"
    options:
      - "Keyboard and assistive technology users rely on focus indicators to know which element is selected"
      - "It causes a Tailwind compilation error"
      - "Browsers require focus outlines for security reasons"
      - "It breaks hover styles on the same element"
    correctIndex: 0
    feedback: "Focus styles are the visual indicator for keyboard navigation. Removing them without a replacement (`focus:ring-2`) makes the interface unusable for keyboard-only users, which is an accessibility failure."
retrieval:
  recall: "What is the difference between hover: and group-hover: in Tailwind?"
  explain: "Why is focus: styling important for accessibility?"
  mistakeId:
    code: |
      <button className="bg-blue-600 text-white px-4 py-2 rounded focus:outline-none">
        Submit
      </button>
    answer: "`focus:outline-none` removes the focus ring with no replacement. Add `focus:ring-2 focus:ring-blue-500 focus:ring-offset-2` to provide a visible, accessible focus indicator for keyboard users. Never remove focus styles without replacing them."
---

# Hook

A button that does not react when you hover over it feels broken. A link that has no focus ring is invisible to keyboard users. An input that does not change on focus feels unresponsive. Interactive states are not decoration — they are the interface communicating its capabilities. Tailwind makes them as easy as a prefix.

# Lore Introduction

Master Interactus teaches a fundamental principle: "An interface that does not respond to touch is a stone wall, not a gateway. Every interactive element must speak to the user — through colour, motion, cursor — acknowledging that the user is engaged and the element is ready." Tailwind's state variants are the vocabulary for this conversation.

# Core Learning

## Concept Introduction

Tailwind's state variants apply styles in specific interactive states using prefixes:

```
hover:     → on mouse hover
focus:     → when element is focused (keyboard or click)
active:    → while element is being clicked/pressed
disabled:  → when element has disabled attribute
focus-visible: → focus only from keyboard (not mouse click)
```

**Basic button states:**
```jsx
<button className="
  bg-blue-600 text-white px-4 py-2 rounded-lg font-medium
  hover:bg-blue-700
  focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2
  active:scale-95
  disabled:opacity-50 disabled:cursor-not-allowed
  transition-all duration-150
">
  Submit Quest
</button>
```

**Input states:**
```jsx
<input
  className="
    w-full border border-gray-300 rounded-lg px-3 py-2
    focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500
    disabled:bg-gray-100 disabled:cursor-not-allowed
    placeholder:text-gray-400
  "
  placeholder="Enter your answer..."
/>
```

**The `group` pattern for parent-child state:**
```jsx
// Hover on the card reveals the overlay button
<div className="group relative overflow-hidden rounded-xl bg-white shadow cursor-pointer">
  <img src={item.image} alt={item.title} className="w-full h-48 object-cover" />

  {/* Hidden until parent is hovered */}
  <div className="
    absolute inset-0 bg-black/50
    opacity-0 group-hover:opacity-100
    transition-opacity duration-200
    flex items-center justify-center
  ">
    <button className="bg-white text-gray-900 px-4 py-2 rounded-lg font-medium">
      View Details
    </button>
  </div>

  <div className="p-4">
    <h3 className="font-semibold text-gray-900 group-hover:text-blue-600 transition-colors">
      {item.title}
    </h3>
  </div>
</div>
```

## Why It Matters

States create the "feel" of an interface. A button with no hover state feels flat and broken. Proper focus styles make the interface usable for keyboard users (a legal accessibility requirement in many jurisdictions). The `group` pattern enables complex hover interactions without JavaScript.

## Common Mistakes

- **Removing focus styles without replacement** — `focus:outline-none` alone is an accessibility failure. Always follow with `focus:ring-2`.
- **Forgetting `transition-*`** — hover colour changes are jarring without `transition-colors duration-150`. Add transitions to all interactive states.
- **Using `hover:` without considering touch devices** — hover states are mouse-only; touch screens don't have hover. Ensure the non-hover state is also usable.

## Mini Summary

Tailwind state variants (`hover:`, `focus:`, `active:`, `disabled:`) style interactive states without CSS. The `group` + `group-hover:` pattern enables parent-child hover interactions. Always include visible focus styles for accessibility.

# Guided Practice Quest

Work through the guided steps to practise the group-hover pattern and accessible button states.

# Solo Practice Quest

Build a `<QuestCard>` component that: shows a quest title and difficulty; reveals a "Start Quest" button on hover (using group-hover); has a proper focus ring on the button; and dims correctly when the card has a `disabled` prop.

# Integration

**Design:** Microinteractions — the small state changes on hover, focus, and press — are what designers call "affordances": visual cues that tell users "this is clickable, this is interactive." Proper state styling is the engineering implementation of affordance design.

**UX:** WCAG 2.1 accessibility guidelines require focus indicators for keyboard navigation (Success Criterion 2.4.7). Providing `focus:ring-2` is not just good practice — it's a legal requirement in many accessibility regulations including EU and UK accessibility legislation.

# Lore Conclusion

Master Interactus hovers over each interactive element on the Academy portal. Every button responds — darkening, scaling, showing focus rings. "The interface speaks," he says with satisfaction. "It says: I am here, I am ready, I am listening." The apprentices have learned that interactive states are not styling — they are communication.

---
