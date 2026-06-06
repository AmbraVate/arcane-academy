---
id: fe-jun-m1-10
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m1
moduleTitle: "Module 1: React Foundations"
moduleGlyph: "⚛️"
moduleSortOrder: 1
topicSlug: props
topicTitle: "Props"
topicSortOrder: 4
lesson: passing_data_down
title: "Passing Data Down"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-04, fe-jun-m1-06]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what props are and how they flow"
    - "Writes correct prop passing syntax"
    - "Explains why props are read-only"
    - "Uses destructuring to access props"
  keywords: [props, parent, child, read-only, destructure, pass, receive, immutable]
  modelAnswer: |
    Props are the mechanism for passing data from a parent component to a child component in React. They are read-only — a child component can never modify its props. This immutability is intentional: it preserves the predictability of data flow. Parents control what data children receive; children cannot reach up and change it.
guidedSteps:
  - id: fe-jun-m1-10-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A child component receives a `count` prop. What happens if the child tries to do `props.count = 5`?"
    inputConfig:
      options:
        - "The parent's state updates to 5"
        - "Nothing happens silently"
        - "React throws an error — props are read-only"
        - "The child re-renders with count = 5"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["React throws an error — props are read-only"]
      rejectedFeedback: "Props are read-only. Mutating props breaks React's data flow model. If a child needs to change a value, it must call a callback function passed as a prop — which the parent uses to update its own state."
    hint: "Think about the direction of data flow."
    reflectionPrompt: "Immutable props make React components predictable. A parent knows exactly what data its children have — children can't secretly modify it."
  - id: fe-jun-m1-10-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write JSX that renders a `UserCard` component, passing it a `name` prop of 'Alice' and an `age` prop of 30."
    inputConfig:
      minWords: 3
    markingRule:
      matchMode: CONTAINS
      accepted: [UserCard, name, age, Alice, 30]
      rejectedFeedback: "`<UserCard name=\"Alice\" age={30} />` — strings use quotes, numbers use curly braces. All JS values (numbers, booleans, objects, arrays) use `{}`."
    hint: "String props use quotes; all other types use curly braces."
    reflectionPrompt: "The distinction between string props (quotes) and other types (curly braces) comes from JSX's expression syntax. Everything in `{}` is evaluated as JavaScript."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "How does a child component communicate back to its parent?"
    options:
      - "By modifying its props directly"
      - "By calling a callback function passed as a prop"
      - "By modifying the parent's state directly"
      - "By using a global variable"
    correctIndex: 1
    feedback: "Callback props are the React way for children to communicate upward. The parent passes a function as a prop; the child calls it when something happens. The parent's function updates state, which flows back down as new props."
retrieval:
  recall: "Why are props read-only in React?"
  explain: "How do you pass a number and a boolean as props, and why are they different from string props?"
  mistakeId:
    code: "function Child({ count }) { count = count + 1; }"
    answer: "Never mutate props. This modifies a local copy but has no effect on the parent's data. To update the parent's data, call a callback function: `function Child({ count, onIncrement }) { ... onIncrement() ... }`."
---

# Hook

Components without data are static templates. Props are the mechanism that brings them to life — injecting real data from the outside world. Understanding props means understanding how information moves through a React application.

# Lore Introduction

*"A rune formula does nothing until it receives its ingredients,"* Aelindra says. *"You define the formula — the mould. The caller provides the material. Props are the ingredients poured into the mould."*

She adds: *"And the formula never changes the ingredients. It uses them, transforms them into output — but the original material remains untouched."*

# Core Learning

## Concept Introduction

Props (short for *properties*) are **inputs** passed from a parent component to a child. They flow **downward** in the component tree and are **read-only** within the receiving component.

```jsx
// Parent passes props
<Avatar username="aelindra" size={48} isOnline={true} />

// Child receives and uses props
function Avatar({ username, size, isOnline }) {
  return (
    <div>
      <img width={size} alt={username} />
      {isOnline && <span className="online-dot" />}
    </div>
  );
}
```

| Prop type | Syntax |
|---|---|
| String | `name="Alice"` |
| Number | `age={30}` |
| Boolean | `isActive={true}` or just `isActive` |
| Object | `user={{ id: 1, name: 'Bob' }}` |
| Array | `items={[1, 2, 3]}` |
| Function | `onClick={handleClick}` |

## Why It Matters

Props are the primary communication channel in React. Every component's API is its props — knowing a component's props tells you everything you need to know to use it correctly.

## Worked Example

```jsx
function NotificationBadge({ count, onClick }) {
  return (
    <button onClick={onClick} className="badge">
      {count > 0 ? count : null}
    </button>
  );
}

// Usage
<NotificationBadge count={5} onClick={() => markAllRead()} />
```

Data (count) flows down. Behaviour (onClick) flows down as a callback. The badge never modifies either.

## Common Mistakes

- **Mutating props.** Always read-only — call a callback instead.
- **Passing objects/arrays without curly braces.** `items="[1,2,3]"` passes a string. `items={[1,2,3]}` passes an array.
- **Confusing boolean shorthand.** `isActive` (alone) is the same as `isActive={true}`. But `isActive="false"` is a non-empty string — truthy!

## Mini Summary

- Props pass data from parent to child — one direction only
- All prop types except strings need curly brace syntax
- Props are read-only within the receiving component
- Children communicate back via callback function props

# Guided Practice Quest

Work through the guided steps on reading, passing, and understanding props.

# Solo Practice Quest

Design a `ProductCard` component. Define its props interface (name, price, imageUrl, category, onAddToCart). Write the component using destructuring. Then write example usage in a parent component showing correct prop types for each prop. Finally, describe in 2–3 sentences how the parent would handle the onAddToCart callback.

# Integration

**Design — Design Systems and Component APIs**

Props define a component's API — its interface contract. In design systems, components have documented props with types, defaults, and descriptions (like a Storybook story). Good prop API design follows the same principles as good function API design: minimal required props, sensible defaults, clear naming. This parallels industrial design's principle of affordance: well-designed interfaces communicate their own usage. A well-named prop like `isDisabled` needs no documentation to understand.

# Lore Conclusion

*"The ingredient list,"* Aelindra says, *"is the component's contract with the world. Design it carefully. Name each ingredient clearly. Accept no more than you need. Return the transformation faithfully."*

---
