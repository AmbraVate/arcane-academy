---
id: fe-jun-m6-01
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m6
moduleTitle: "Module 6: Component Design"
moduleGlyph: "🧩"
moduleSortOrder: 6
topicSlug: reusability
topicTitle: "Reusability"
topicSortOrder: 1
lesson: why_reusability_matters
title: "Why Reusability Matters"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-15]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies a specific component in their own experience that could be or was reused"
    - "Explains the benefit in terms of consistency, maintenance, or speed"
    - "Acknowledges a trade-off or risk of reuse (over-abstraction, wrong props API)"
  keywords: [reuse, props, DRY, consistent, abstract]
  modelAnswer: |
    A reusable component is driven by props rather than hardcoded values, accepts a clear interface, and renders the same structure regardless of where it is placed. The benefit is that fixing a bug or changing a style in one place propagates everywhere the component is used. The risk is designing the wrong abstraction too early, which makes the component hard to adapt and couples unrelated use-cases.
guidedSteps:
  - id: fe-jun-m6-01-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Which of the following best describes a reusable component?"
    inputConfig:
      options:
        - "A component that only renders one specific item, like a single user's name"
        - "A component that accepts props and renders consistently based on them"
        - "A component that fetches its own data internally"
        - "A component with no styling so it can be used anywhere"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A component that accepts props and renders consistently based on them"]
      rejectedFeedback: "Reusability is about a well-defined props interface that lets the same component serve different data and contexts."
    hint: "Think about what makes a Button component work for 'Save', 'Cancel', and 'Delete' without rewriting it."
    reflectionPrompt: "Why does relying on props (rather than internal hardcoding) make a component reusable?"
  - id: fe-jun-m6-01-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Describe in your own words what DRY means in the context of UI components."
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [duplicate, repeat, once, single, source]
      rejectedFeedback: "DRY stands for Don't Repeat Yourself — think about what happens when you copy-paste a component instead of abstracting it."
    hint: "What happens when you have 10 identical buttons defined separately and the design changes?"
    reflectionPrompt: "Where in your current knowledge of React have you already seen the DRY principle at work?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A developer copies a Card component 6 times with minor text differences. What principle does this violate?"
    options: ["Single Responsibility", "DRY", "Open/Closed", "Liskov Substitution"]
    correctIndex: 1
    feedback: "DRY — Don't Repeat Yourself. The card should accept props for the varying text, not be duplicated 6 times."
retrieval:
  recall: "What does DRY stand for and how does it apply to React component design?"
  explain: "Explain why building components driven by props makes them reusable across different contexts."
  mistakeId:
    code: |
      // Developer created three separate components:
      const SaveButton = () => <button className="btn-primary">Save</button>;
      const CancelButton = () => <button className="btn-secondary">Cancel</button>;
      const DeleteButton = () => <button className="btn-danger">Delete</button>;
    answer: "These three components share the same structure. A single Button component with variant and label props would be DRY: <Button variant='primary' label='Save' />."
---

# Hook

You are three weeks into a project. The designer sends a Slack message: "Can you change the border-radius on all the cards?" You open your codebase and discover you have 14 separate Card components, each a slightly different copy. You spend two hours making the same change 14 times — and still miss two of them in production.

This is the cost of ignoring reusability.

# Lore Introduction

In the halls of the Arcane Academy, the senior artificers have a saying: *"Forge the rune once; inscribe it everywhere."* Apprentices who carve the same protective sigil onto every door by hand are not admired — they are pitied. The wise artificer creates a master rune stone, then stamps it. Change the master, and every door updates instantly.

Your React components are rune stones. Forge them well.

# Core Learning

## Concept Introduction

A **reusable component** is one that can be placed in multiple contexts and still work correctly. Instead of hardcoding the content inside the component, you drive it through **props** — the component's public interface to the outside world.

```tsx
// NOT reusable — hardcoded
const WelcomeBanner = () => (
  <div className="p-4 bg-blue-100 rounded">
    <h2 className="text-xl font-bold">Welcome, Alice!</h2>
  </div>
);

// Reusable — props-driven
interface WelcomeBannerProps {
  name: string;
}

const WelcomeBanner = ({ name }: WelcomeBannerProps) => (
  <div className="p-4 bg-blue-100 rounded">
    <h2 className="text-xl font-bold">Welcome, {name}!</h2>
  </div>
);
```

The second version works for any user. The first only works for Alice.

## Why It Matters

Reusability delivers three compounding benefits:

**1. Consistency** — When all buttons come from one `Button` component, your app's visual language stays uniform. Change the border-radius once; it updates everywhere.

**2. Maintainability** — Bugs are fixed in one place. If a Card component has an accessibility issue, you fix it once and every card in the app is corrected.

**3. Velocity** — Once a solid, reusable set of components exists, new features are assembled rather than built from scratch.

The underlying principle is **DRY: Don't Repeat Yourself**. In UI terms, this means: if you're about to copy a component and change a value, stop — that value should be a prop.

## Worked Example

A team is building a dashboard with several stat cards. A non-reusable approach creates three separate components:

```tsx
// Fragile — three separate components for the same structure
const ActiveUsersCard = () => (
  <div className="rounded-lg border p-6">
    <p className="text-sm text-gray-500">Active Users</p>
    <p className="text-3xl font-bold">1,284</p>
  </div>
);

const RevenueCard = () => (
  <div className="rounded-lg border p-6">
    <p className="text-sm text-gray-500">Revenue</p>
    <p className="text-3xl font-bold">$48,200</p>
  </div>
);
```

A reusable approach:

```tsx
interface StatCardProps {
  label: string;
  value: string;
}

const StatCard = ({ label, value }: StatCardProps) => (
  <div className="rounded-lg border p-6">
    <p className="text-sm text-gray-500">{label}</p>
    <p className="text-3xl font-bold">{value}</p>
  </div>
);

// Usage
<StatCard label="Active Users" value="1,284" />
<StatCard label="Revenue" value="$48,200" />
```

Now the structure lives in one place. If the design changes, you edit `StatCard` once.

## Common Mistakes

**Hardcoding one value while making others props.** A component might accept a label prop but hardcode the icon. This partial reusability is deceptive — the component looks reusable but breaks in unexpected contexts.

**Confusing generic with reusable.** A component can be so generic it becomes useless (a `Box` that just renders a `div` with no opinions). Reusable components have a clear, focused purpose — they are not infinitely flexible.

**Reusing before the pattern is clear.** If you only have one use-case, don't abstract yet. Premature reuse often locks in the wrong API. (More on this in Lesson 3.)

## Mini Summary

Reusable components are driven by props rather than hardcoded values. They embody the DRY principle: define the structure once and vary the content through a well-designed interface. This leads to consistent UIs, easier maintenance, and faster feature development.

# Guided Practice Quest

Work through the steps in the panel. You will identify reusability opportunities and distinguish reusable from non-reusable component designs.

# Solo Practice Quest

Think about a real UI you have built or used recently. Identify one element that appeared in multiple places (a card, a badge, a button, a list item). Reflect on:

1. What props would that component need to serve all its use-cases?
2. What benefit would a single reusable component have provided?
3. Was there any risk of abstracting it too early?

Write 3–5 sentences covering all three points.

# Integration

**Psychology — Cognitive Load:** Consistent components reduce cognitive load for users. When every "danger action" uses the same red button style, users learn the pattern once and apply it everywhere in the app. Inconsistent UI — the result of non-reusable components drifting — forces users to re-evaluate familiar patterns, increasing mental effort and errors.

**Design Systems:** Professional design systems (Material UI, Radix, shadcn/ui) are built entirely on the reusability principle. Each component is a prop-driven primitive. The discipline of designing a component's API before building it is a core skill in design-systems work.

# Lore Conclusion

The Arcane Academy's library contains thousands of scrolls, but each protective ward is cast from a single master formula — copied perfectly each time by the enchanting press. Young artificers who understand this truth spend their energy perfecting the master formula, not carving the same sigil over and over. You have taken your first step toward building your own master rune stones.

---
