---
id: fe-jun-m1-08
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m1
moduleTitle: "Module 1: React Foundations"
moduleGlyph: "⚛️"
moduleSortOrder: 1
topicSlug: jsx
topicTitle: "JSX"
topicSortOrder: 3
lesson: jsx_expressions
title: "JSX Expressions"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-07]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Uses curly braces correctly to embed JS expressions"
    - "Distinguishes statements from expressions"
    - "Uses ternary and && for conditional rendering"
    - "Maps arrays to JSX correctly"
  keywords: [expression, curly brace, ternary, &&, map, conditional, statement, key]
  modelAnswer: |
    JSX expressions are JavaScript values embedded in JSX using curly braces {}. Only expressions (values) can go inside {}; statements (if/for) cannot. Conditional rendering uses the ternary operator or &&. Array rendering uses .map(), and each element needs a unique key prop.
guidedSteps:
  - id: fe-jun-m1-08-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Which of these can go inside JSX curly braces `{}`?"
    inputConfig:
      options:
        - "if/else statements"
        - "for loops"
        - "JavaScript expressions (values, ternaries, function calls)"
        - "CSS rules"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["JavaScript expressions (values, ternaries, function calls)"]
      rejectedFeedback: "Curly braces in JSX accept *expressions* — anything that evaluates to a value. Statements (if, for, while) don't produce values, so they can't go inside {}. Use ternary or .map() instead."
    hint: "An expression produces a value. A statement is a command."
    reflectionPrompt: "This distinction between expressions and statements is fundamental JavaScript. Understanding it makes JSX restrictions intuitive rather than arbitrary."
  - id: fe-jun-m1-08-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write the JSX for rendering a list of names from this array: `const names = ['Alice', 'Bob', 'Carol']`. Each name should be an `<li>` element with a key."
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: [map, names, li, key]
      rejectedFeedback: "Use .map(): `<ul>{names.map(name => <li key={name}>{name}</li>)}</ul>`. The key prop helps React identify which items changed."
    hint: "Use .map() to transform each string to a JSX element."
    reflectionPrompt: "The `key` prop is required when rendering lists. React uses it to track which items changed, were added, or removed — enabling efficient DOM updates."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why does React require a `key` prop when rendering lists?"
    options:
      - "It's used as the CSS id"
      - "It helps React identify which items changed, enabling efficient DOM updates"
      - "It sets the tab order for accessibility"
      - "It's required by JavaScript's map function"
    correctIndex: 1
    feedback: "Keys help React's reconciliation algorithm. When a list changes, React uses keys to match old and new items — minimising DOM operations. Without keys, React re-renders the whole list on any change."
retrieval:
  recall: "What is the difference between a JavaScript expression and a statement?"
  explain: "How do you conditionally render a component in JSX when a value is false?"
  mistakeId:
    code: "Using array index as key: items.map((item, i) => <div key={i}>)"
    answer: "Using array index as key causes bugs when items are reordered or removed. React uses keys to match items between renders — if the key changes with position, React gets confused. Use a stable, unique ID from the data instead."
---

# Hook

JSX would be useless if it were just static markup. Its power is in expressions: the ability to embed any JavaScript value — a variable, a calculation, a conditional, a mapped array — directly into your template. The curly brace `{}` is the portal between JSX and JavaScript.

# Lore Introduction

*"A static rune is decorative,"* Aelindra says. *"A rune that responds to the state of the world — that is magic. The curly symbol is your bridge: cross it to speak in pure logic, then return to the rune's language."*

# Core Learning

## Concept Introduction

In JSX, `{}` embeds any JavaScript **expression**:

```jsx
// Variables
<h1>{title}</h1>

// Function calls
<p>{formatDate(createdAt)}</p>

// Ternary (conditional)
<span>{isLoggedIn ? 'Welcome back' : 'Please sign in'}</span>

// Short-circuit (render or nothing)
{hasError && <ErrorBanner message={error} />}

// Array mapping
<ul>
  {items.map(item => <li key={item.id}>{item.name}</li>)}
</ul>
```

| Pattern | When to use |
|---|---|
| `{value}` | Render any value |
| `{condition ? a : b}` | Render one of two options |
| `{condition && element}` | Render or nothing |
| `{array.map(...)}` | Render a list |

## Why It Matters

JSX expressions make components dynamic — they respond to props and state, not just static data. Every real component uses expressions to render different content based on its inputs.

## Worked Example

```jsx
function UserGreeting({ user, notifications }) {
  return (
    <header>
      <h1>Hello, {user.name}!</h1>
      {user.isPremium && <Badge label="Premium" />}
      {notifications > 0
        ? <span>{notifications} new messages</span>
        : <span>No new messages</span>
      }
      <ul>
        {user.favourites.map(item => (
          <li key={item.id}>{item.title}</li>
        ))}
      </ul>
    </header>
  );
}
```

## Common Mistakes

- **Using `if` inside JSX.** Not allowed — use ternary or early returns.
- **Missing `key` in lists.** React warns and behaviour becomes unpredictable.
- **Using index as key.** Breaks when list order changes. Use stable IDs.
- **Rendering `false`, `null`, `undefined`.** These render nothing — but `0` does render! Use `count > 0 && ...` not `count && ...`.

## Mini Summary

- `{}` embeds any JS expression into JSX
- Use ternary for conditional rendering
- Use `&&` to render or nothing
- Use `.map()` for lists, always with unique `key` props

# Guided Practice Quest

Work through the guided steps on JSX expressions and list rendering.

# Solo Practice Quest

Write a `ShoppingCart` component that receives a `cartItems` array (objects with `name`, `price`, `qty`) and an `isLoading` boolean. When loading, show a spinner text. When loaded, show each item in a list with its name, quantity, and subtotal (price × qty). Show the total at the bottom. Use all four expression patterns: value, ternary, &&, and map.

# Integration

**Mathematics — Functions and Mappings**

The `.map()` function is a direct application of mathematical mapping: a function applied to every element of a set, producing a new set. `items.map(item => <li>{item.name}</li>)` maps a set of data objects to a set of JSX elements. This is the same concept as applying a function to every element of a mathematical sequence. Understanding functional mappings makes array methods (map, filter, reduce) intuitive — they are set transformations.

# Lore Conclusion

*"The curly gate,"* Aelindra says, *"opens onto the full power of logic. Use it well: conditions, transformations, mappings. The rune becomes alive."*

---
