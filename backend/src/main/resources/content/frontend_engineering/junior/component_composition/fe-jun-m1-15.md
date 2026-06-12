---
id: fe-jun-m1-15
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m1
moduleTitle: "Module 1: React Foundations"
moduleGlyph: "⚛️"
moduleSortOrder: 1
topicSlug: component_composition
topicTitle: "Component Composition"
topicSortOrder: 5
lesson: reusability_patterns
title: "Reusability Patterns"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-14]
integrationDomains: [design, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies when a component is ready to be extracted"
    - "Applies the rule of three to reusability decisions"
    - "Designs a generic, reusable component with a clear prop API"
    - "Explains the trade-off between flexibility and simplicity in component design"
  keywords: [reuse, extract, generic, rule of three, flexibility, abstraction, DRY, prop API]
  modelAnswer: |
    A component is worth extracting when it is used in more than two places, or when it is complex enough to benefit from isolation. Good reusable components have clear prop APIs, sensible defaults, and don't hard-code context-specific details. The trade-off is that more flexible components are more complex — extract only when the reuse benefit justifies the abstraction cost.
guidedSteps:
  - id: fe-jun-m1-15-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "According to the 'rule of three', when should you extract a reusable component?"
    inputConfig:
      options:
        - "The first time you write a piece of UI"
        - "After you've written the same UI in three different places"
        - "Only when a senior developer tells you to"
        - "Never — always write components inline"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["After you've written the same UI in three different places"]
      rejectedFeedback: "The rule of three: tolerate duplication the first time. Tolerate it the second. Extract on the third. Early extraction creates abstractions based on insufficient information — they often turn out wrong."
    hint: "Premature abstraction is as harmful as none at all."
    reflectionPrompt: "The rule of three prevents premature abstraction. Two instances might be coincidentally similar. Three instances suggest a true pattern worth abstracting."
  - id: fe-jun-m1-15-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "You have a StatusBadge used in three places: order status, user role, and product availability. What props should a generic StatusBadge accept?"
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [label, colour, color, variant, status, type, size]
      rejectedFeedback: "A generic StatusBadge might accept: `label` (text to show), `variant` or `colour` (visual style), optionally `size`. It should NOT have order/user/product-specific logic built in — keep it generic."
    hint: "What do all three use cases have in common? What differs?"
    reflectionPrompt: "The prop API is the component's contract. Design it by finding the intersection of all use cases — the minimal set of props that serves all callers."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the risk of making a reusable component too flexible (too many props)?"
    options:
      - "The component will be slower"
      - "It becomes complex, hard to use, and hard to maintain — a 'prop explosion'"
      - "It can't be typed with TypeScript"
      - "React won't allow more than 10 props"
    correctIndex: 1
    feedback: "Over-flexible components become mini-frameworks. Every caller must understand every prop. Prefer a narrow, focused API and create separate components for genuinely different use cases."
retrieval:
  recall: "What is the rule of three for component extraction?"
  explain: "How do you find the right level of generality for a reusable component?"
  mistakeId:
    code: "Extracting a component the first time you write it"
    answer: "Premature extraction creates abstractions based on one example. When you have one example, you don't know which parts will vary. The rule of three: see the pattern in three places before abstracting. This produces better-designed, more stable abstractions."
---

# Hook

Every experienced React developer has made the same mistake: extracted a component too early, designed it around assumptions, then spent three hours refactoring it when the second use case turned out different. Good reusability isn't about extracting everything — it's about knowing *when* and *how*.

# Lore Introduction

*"The journeyman,"* Aelindra says, *"sees two runes that look similar and immediately creates a template. The master waits. She sees three. She understands what varies and what is fixed. Only then does she reach for the mould."*

# Core Learning

## Concept Introduction

Reusability principles:

| Principle | Meaning |
|---|---|
| **Rule of Three** | Extract on the third occurrence, not the first |
| **Single Responsibility** | One job per component |
| **Prop API design** | Minimal, generic, well-named props |
| **Avoid specificity** | Don't hard-code context into reusable components |

## Why It Matters

Well-designed reusable components save time. Poorly designed ones create debt. The skill is judging when to extract, and what API to design when you do.

## Worked Example

**Three uses of similar UI — time to extract:**
```jsx
// In OrderPage
<span className="badge badge-success">Delivered</span>

// In UserPage
<span className="badge badge-warning">Admin</span>

// In ProductPage
<span className="badge badge-danger">Out of Stock</span>

// Extracted generic component
function StatusBadge({ label, variant = 'default' }) {
  return <span className={`badge badge-${variant}`}>{label}</span>;
}

// Clean usage
<StatusBadge label="Delivered" variant="success" />
<StatusBadge label="Admin" variant="warning" />
<StatusBadge label="Out of Stock" variant="danger" />
```

## Common Mistakes

- **Premature extraction.** Extract after three occurrences, not one.
- **Context leakage.** `OrderStatusBadge` that works only for orders isn't reusable.
- **Prop explosion.** 15 props means your component is trying to do too many things.
- **DRY dogmatism.** Sometimes duplication is fine. Not all repetition needs abstraction.

## Mental Model

Reusability patterns are the difference between a Swiss Army knife and a socket wrench set. The Swiss Army knife approach to reuse — one component, ever more blades — feels efficient ("it already does cards, just add a `horizontal` mode... and a `compact` flag... and `headerless`...") but each blade added makes every blade worse: the component grows conditional thickets, its props become a riddle, and changing any mode risks the others. The socket set approach decomposes by *axis of variation*: a handle that's always the same (the shared structure — Card's border, padding, shadow), and sockets that vary independently (the slots — header, body, footer as children or render props). Each pattern in this lesson is a way of building handles and sockets: children for "vary the contents", slot props for "vary several regions", render props for "vary how each item displays". The judgement call the analogy sharpens: when two UIs share *structure*, extract the handle; when they merely look vaguely similar, they're different tools — forcing them into one component is welding two wrenches together because both are metal. Reuse what genuinely repeats; let coincidences stay separate.

## Mini Summary

- Extract on the third occurrence — not the first
- Keep reusable components generic — no context-specific logic
- Design minimal prop APIs with clear names
- Prop explosion is a sign of over-abstraction

# Guided Practice Quest

Work through the guided steps on deciding when and how to extract a reusable component.

# Solo Practice Quest

You're building an app and you've written the following in three places:
1. A "No results found" empty state message with an icon and description
2. A loading spinner with a message
3. An error card with a title and retry button

For each: decide whether to extract it, design its prop API, and write the component signature. Then write 2–3 sentences on why you chose each prop name and default.

# Integration

**Mathematics — Abstraction and Generalisation**

Mathematical abstraction is the process of extracting the general pattern from specific examples. The number `3` is an abstraction over "three apples", "three people", "three hours". Component extraction is the same: `StatusBadge` is an abstraction over three specific badge instances. Good abstraction captures the essential shared structure while parameterising the varying parts (props). This is precisely what mathematicians do when they go from specific numbers to algebraic variables. Programming and mathematics share the same abstract reasoning process.

# Lore Conclusion

*"The mould,"* Aelindra says, *"is formed from observation, not imagination. See the pattern in the world. Then, and only then, capture it."*

---
