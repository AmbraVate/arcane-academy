---
id: fe-jun-m2-07
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m2
moduleTitle: "Module 2: State Management"
moduleGlyph: "🔄"
moduleSortOrder: 2
topicSlug: derived_state
topicTitle: "Derived State"
topicSortOrder: 3
lesson: computing_from_state
title: "Computing from State"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m2-04]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what derived state is"
    - "Computes values during render rather than storing in state"
    - "Identifies when state is redundant"
    - "Explains why derived state reduces bugs"
  keywords: [derived, computed, render, redundant, single source of truth, calculate, formula]
  modelAnswer: |
    Derived state is any value that can be computed from existing state or props. It should not be stored in its own state variable — compute it during render instead. Storing derived values in state creates two sources of truth that can go out of sync. The rule: if a value can be calculated from other state or props, don't put it in state.
guidedSteps:
  - id: fe-jun-m2-07-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You have `items` in state (an array). Should `itemCount` also be in state?"
    inputConfig:
      options:
        - "Yes — it's needed in many places"
        - "No — derive it: `const itemCount = items.length`"
        - "Yes — storing it avoids recomputing each render"
        - "Only if the list is very long"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["No — derive it: `const itemCount = items.length`"]
      rejectedFeedback: "`itemCount` is derivable from `items` — it's always `items.length`. Storing it separately creates two sources of truth that can drift. Compute it during render: fast, always correct, no sync bugs."
    hint: "Can you compute it from existing state? Then don't store it."
    reflectionPrompt: "Every redundant state variable is a bug waiting to happen. items.length is always correct; a separate itemCount could be forgotten to update."
  - id: fe-jun-m2-07-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "You have `firstName` and `lastName` in state. List two values you should derive rather than store in state."
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: [fullName, initials, length, uppercase, display]
      rejectedFeedback: "Derivable: fullName (firstName + ' ' + lastName), initials (firstName[0] + lastName[0]), displayName (firstName only), characterCount. None of these need their own state."
    hint: "Any combination or transformation of firstName and lastName is derivable."
    reflectionPrompt: "The principle extends to complex applications: a shopping cart's total, a form's validity, filtered search results — all derivable from existing state."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What problem does storing derived values in state cause?"
    options:
      - "It makes the component slower"
      - "Two sources of truth can go out of sync, causing bugs"
      - "React throws an error for duplicate state"
      - "It prevents re-renders"
    correctIndex: 1
    feedback: "Two sources of truth: if items changes but you forget to update itemCount, they disagree. Bugs from sync drift are subtle and hard to trace. One source of truth — derive the rest — is always correct."
retrieval:
  recall: "What is the 'single source of truth' principle in state management?"
  explain: "Give three examples of values that should be derived rather than stored in state."
  mistakeId:
    code: "const [filteredItems, setFilteredItems] = useState(items)"
    answer: "filteredItems is derivable: `const filteredItems = items.filter(...)`. Storing it in state requires remembering to update it every time items or the filter changes. One state (items) + one filter state + derived filteredItems is the correct pattern."
---

# Hook

Every React developer eventually stores a value in state that they could have computed. The result: two pieces of state that must be kept in sync. Miss an update, and they disagree. The fix: if you can compute it, compute it. This lesson is about knowing what belongs in state and what belongs in the render function.

# Lore Introduction

*"The Academy's ledger records only the raw ingredients,"* Aelindra says. *"Never the totals — totals are computed. If you record the total separately, someone will update the ingredients and forget the total. The computation is always correct. The record can lie."*

# Core Learning

## Concept Introduction

**Derived state**: any value calculable from existing state or props. Compute it during render.

```jsx
// Don't do this
const [items, setItems] = useState([]);
const [itemCount, setItemCount] = useState(0); // ← redundant!
const [total, setTotal] = useState(0); // ← redundant!

// Do this
const [items, setItems] = useState([]);
const itemCount = items.length; // ← derived
const total = items.reduce((sum, item) => sum + item.price, 0); // ← derived
```

## Why It Matters

Derived values computed during render are **always** in sync with their source. Stored copies can drift. Fewer state variables = fewer sync bugs = simpler components.

## Worked Example

```jsx
function ShoppingCart({ cartItems }) {
  // State: only what changes independently
  const [couponCode, setCouponCode] = useState('');
  const [isCheckingOut, setIsCheckingOut] = useState(false);

  // Derived: computed from existing data
  const subtotal = cartItems.reduce((sum, item) => sum + item.price * item.qty, 0);
  const discount = couponCode === 'SAVE10' ? subtotal * 0.1 : 0;
  const total = subtotal - discount;
  const itemCount = cartItems.reduce((sum, item) => sum + item.qty, 0);

  return ( /* render */ );
}
```

## Common Mistakes

- **Caching derived values in state "for performance".** Unnecessary — React renders are fast. Use `useMemo` only if profiling shows it's needed.
- **Deriving from props into state on mount.** If the prop changes, state doesn't update. Just use the prop directly.
- **Complex derivations inside JSX.** Extract them above the return as named variables for readability.

## Mental Model

Derived values are spreadsheet formula cells, and the discipline is never to type a number where a formula belongs. In a well-built sheet, you enter raw facts in a few cells — quantities, prices — and everything else is formulas: subtotal `= qty × price`, tax `= subtotal × rate`. You'd never *also type* the subtotal by hand, because the moment quantity changes, your typed copy is a lie sitting beside the truth. Components work identically: state holds the raw facts (the items array, the search text), and everything computable — the filtered list, the count, the "is the form valid?" flag — should be a plain `const` calculated *during render*, a formula cell that recomputes automatically every time the inputs change. The anti-pattern this kills is storing derived values in their own `useState` and updating them alongside the source ("set the items AND set the count") — double bookkeeping that drifts the instant any code path updates one without the other, producing the classic stale-count bug. The test before every `useState`: can I compute this from existing state or props? Then it's a formula, not an entry. Type only the facts; derive all the rest.

## Mini Summary

- If a value can be computed from state or props, compute it during render
- Never store derived values in state — two sources of truth create sync bugs
- Single source of truth is a foundational principle of good state design
- Use useMemo only when profiling shows the derivation is expensive

# Guided Practice Quest

Work through the guided steps on identifying and computing derived values.

# Solo Practice Quest

You're building a filtering and sorting UI for a product list. You have: `allProducts` (array from API), `searchQuery` (string state), `selectedCategory` (string state), `sortOrder` ('asc' | 'desc' state). Identify: what is state, what is derived. Write the derived computations. Explain in 3 sentences why this approach prevents bugs.

# Integration

**Mathematics — Functions and Referential Transparency**

A derived value is a mathematical function of its inputs: `total = f(items)`. Pure functions are referentially transparent — given the same inputs, always the same output. This is exactly what derived state should be: a pure function of state/props. Referential transparency makes reasoning simple: to know the total, you only need to know the items. No hidden state, no sync dependencies. This mathematical property — functions as the primary tool — is why functional programming and React share so much philosophy.

# Lore Conclusion

*"Trust the computation,"* Aelindra says. *"It is always correct if the inputs are correct. The separate record can drift, be forgotten, or be wrong. The formula cannot lie."*

---
