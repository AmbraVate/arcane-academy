---
id: fe-jun-m2-02
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m2
moduleTitle: "Module 2: State Management"
moduleGlyph: "🔄"
moduleSortOrder: 2
topicSlug: state
topicTitle: "State"
topicSortOrder: 1
lesson: local_vs_global_state
title: "Local vs Global State"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m2-01]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Distinguishes local state from global/shared state"
    - "Identifies which type of state a given data item should be"
    - "Explains when global state management is justified"
    - "Names appropriate tools for each state type"
  keywords: [local, global, shared, component, useState, Context, Redux, Zustand, scope]
  modelAnswer: |
    Local state lives inside a single component and is only accessible there. Global (shared) state is needed when multiple unrelated components need the same data. The general rule is to keep state as local as possible — only lift it to shared storage when truly necessary, because global state increases coupling and makes the system harder to reason about.
guidedSteps:
  - id: fe-jun-m2-02-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A modal's open/closed status. Where should this state live?"
    inputConfig:
      options:
        - "In global state — modals can appear anywhere"
        - "In the component that controls when to show the modal (local state)"
        - "In the backend database"
        - "In localStorage"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["In the component that controls when to show the modal (local state)"]
      rejectedFeedback: "A modal's visibility is typically local to the component that triggers it. Only if multiple unrelated components need to open/close the same modal would global state be justified."
    hint: "Who cares whether the modal is open? Usually just one component."
    reflectionPrompt: "The locality principle: keep state as close to where it's used as possible. Global state is a cost — only pay it when the benefit (sharing) justifies it."
  - id: fe-jun-m2-02-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "The currently logged-in user's data. Where should this state live?"
    inputConfig:
      options:
        - "In the root App component's local state"
        - "In global state — many unrelated components need the user's name, role, and avatar"
        - "In each component that needs it, fetched separately"
        - "In the URL"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["In global state — many unrelated components need the user's name, role, and avatar"]
      rejectedFeedback: "User data is a classic case for global state. The navbar, settings page, and profile page all need it — passing it down as props through unrelated components (prop drilling) is impractical."
    hint: "How many components in a typical app need the current user's details?"
    reflectionPrompt: "Auth state (current user), theme, and cart are classic global state examples. They're needed everywhere, and updating them should immediately affect all consumers."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is prop drilling and why is it a problem?"
    options:
      - "A performance optimisation technique"
      - "Passing props through many intermediate components that don't use them"
      - "Using too many props in a single component"
      - "Drilling down into a component's internal state from outside"
    correctIndex: 1
    feedback: "Prop drilling: passing a prop through 5 levels of components just to reach the one that needs it. The 4 intermediate components become tightly coupled to data they don't use. Context API or global state avoids this."
retrieval:
  recall: "Give two examples of state that belongs locally and two that belong globally."
  explain: "What is the cost of making state global?"
  mistakeId:
    code: "Putting all state in a global store immediately"
    answer: "Global state increases coupling — every component becomes dependent on the global store. This makes components harder to test and reuse in isolation. Start with local state. Lift only when multiple components need the same data."
---

# Hook

Not all state is created equal. The counter inside a card stays in the card. The current user's profile travels everywhere. Understanding the difference between local and shared state — and knowing which tool to use for each — is the state management skill that distinguishes good React engineers.

# Lore Introduction

*"Some runes are private — inscribed on a single parchment, used by one weaver. Others are inscribed in the Academy's central registry, visible to all. The cost of the registry is coordination. Only inscribe there what must be shared."*

# Core Learning

## Concept Introduction

| State Type | Lives in | Accessible by | Tools |
|---|---|---|---|
| **Local** | Single component | That component only | `useState` |
| **Lifted** | Parent component | Parent + children via props | `useState` in parent |
| **Global/Shared** | App-level store | Any component | Context, Redux, Zustand |

**Rule: Keep state as local as possible. Lift only when necessary.**

## Why It Matters

Global state is powerful but expensive. It creates dependencies between components that would otherwise be independent. This makes the system harder to understand, test, and modify. Use global state sparingly and deliberately.

## Worked Example

```
Local state examples:
- Form field values while typing
- Modal open/closed
- Accordion expanded/collapsed
- Tab selection within a component

Global state examples:
- Current logged-in user
- Shopping cart items
- App theme (light/dark)
- Notification list
- Feature flags
```

## Common Mistakes

- **Premature globalisation.** Putting everything in Redux before trying useState.
- **Keeping too much in global state.** UI state (modal open, tab selected) rarely needs to be global.
- **Prop drilling instead of lifting state.** If you're drilling through 4 levels, consider Context.

## Mental Model

Local versus global state is desk drawer versus office filing room. Your desk drawer (local state) holds what only you use — your notes, your stapler, this form's draft: instantly reachable, nobody else's business, and when you leave (the component unmounts), the drawer clears with no ceremony. The filing room (global state) exists for documents *many* people genuinely need — the customer registry (logged-in user), the company-wide settings (theme) — and it charges rent: everything in it needs labelling, access conventions, and coordination, because every change ripples to everyone reading it. The failure pattern this analogy inoculates against is filing-room hoarding: moving your stapler to the filing room "in case someone needs it" — hoisting form inputs, toggles, and hover flags into global stores — which buys no sharing benefit and pays full coordination cost forever. The professional default runs the other way: everything starts in the desk drawer, and a document earns its filing-room slot only when a *second, distant* reader actually appears. State, like paperwork, should live as close to its user as possible and only as far away as genuinely shared.

## Mini Summary

- Local state: lives in one component, `useState`
- Shared/global state: needed by many components, Context/Redux/Zustand
- Rule: keep state as local as possible
- Prop drilling (passing through intermediaries) is the smell that signals a need to lift state

# Guided Practice Quest

Work through the guided steps on categorising state as local or global.

# Solo Practice Quest

Design the state architecture for a simple e-commerce app: product list page, product detail page, shopping cart, user profile page, and checkout flow. For each piece of state you identify, say whether it's local or global and why. Consider: cart items, selected product, filter/sort settings, logged-in user, form inputs, modal visibility.

# Integration

**Design — Information Architecture**

Information architecture asks: where should each piece of information live? This is exactly the local vs global state question. Designers use IA to map content hierarchies; React engineers use it to map state hierarchies. Both disciplines follow the same principle: information should live closest to where it's used and only be elevated when genuinely shared. Good IA and good state architecture both reduce cognitive load by making information findable and predictable.

# Lore Conclusion

*"The registry,"* Aelindra says, *"is a convenience, not a default. Every entry costs coordination. Every private parchment costs nothing but paper. Start private. Elevate only when the sharing is necessary."*

---
