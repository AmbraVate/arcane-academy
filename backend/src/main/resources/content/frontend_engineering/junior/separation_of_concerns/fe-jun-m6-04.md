---
id: fe-jun-m6-04
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m6
moduleTitle: "Module 6: Component Design"
moduleGlyph: "🧩"
moduleSortOrder: 6
topicSlug: separation_of_concerns
topicTitle: "Separation of Concerns"
topicSortOrder: 2
lesson: ui_and_logic
title: "Separating UI and Logic"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m6-03]
integrationDomains: [design, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains why mixing business logic and JSX makes components harder to maintain"
    - "Describes at least one technique for moving logic out of JSX (custom hook, service function, etc.)"
    - "Connects the separation to testability or readability"
  keywords: [logic, JSX, hook, separate, test, readable]
  modelAnswer: |
    When business logic is embedded directly in JSX, the component is harder to read (rendering and decision-making are interleaved), harder to test (logic cannot be tested without rendering), and harder to reuse (the logic is coupled to the specific component). Moving logic to a custom hook or service module isolates the concern, allowing the component to focus purely on rendering.
guidedSteps:
  - id: fe-jun-m6-04-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Which problem does mixing business logic with JSX cause?"
    inputConfig:
      options:
        - "Components render more slowly"
        - "Logic cannot be tested independently of the UI"
        - "TypeScript cannot infer types correctly"
        - "Components become too small"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Logic cannot be tested independently of the UI"]
      rejectedFeedback: "When logic lives inside a component, you must render the component to test it — even if you only care about the logic. Separating concerns makes logic independently testable."
    hint: "Think about what you would need to set up to test a sorting function that lives inside a component's JSX."
    reflectionPrompt: "If you moved the sorting logic to a standalone function, what would a test for it look like?"
  - id: fe-jun-m6-04-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Describe what a component's JSX should ideally express, once logic has been separated out."
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [render, display, show, structure, layout, what]
      rejectedFeedback: "After separation, JSX should express *what* to display and *how* to structure it — not *why* or *when* to show things based on business rules."
    hint: "Think of JSX as a template. Templates describe shape, not decisions."
    reflectionPrompt: "What is the difference between 'show this if the user is an admin' (logic) and rendering an admin panel (UI)?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A component calculates a discount, formats a price, and sorts a product list inside the JSX return. What is the best refactoring step?"
    options:
      - "Break the JSX into smaller fragments"
      - "Move the calculation, formatting, and sorting to a custom hook or utility functions"
      - "Add comments explaining what each section does"
      - "Wrap everything in a useMemo"
    correctIndex: 1
    feedback: "Extraction is the right move. The component should receive processed data and render it — not calculate, format, and sort inline."
retrieval:
  recall: "What does 'separation of concerns' mean in the context of React components?"
  explain: "Why does embedding business logic in JSX make a component harder to test?"
  mistakeId:
    code: |
      const OrderSummary = ({ orders }) => (
        <ul>
          {orders
            .filter(o => o.status !== 'cancelled')
            .sort((a, b) => new Date(b.date) - new Date(a.date))
            .map(o => (
              <li key={o.id}>
                {o.items.reduce((sum, item) => sum + item.price, 0) > 100
                  ? `[VIP] ${o.id}`
                  : o.id}
              </li>
            ))}
        </ul>
      );
    answer: "Filtering, sorting, and the VIP calculation are business logic and should be moved to a hook (useOrderSummary) or utility functions. The component should receive pre-processed data and focus on rendering the list."
---

# Hook

You open a component file to add a small feature. Inside the JSX, you find: a filter expression, a sort expression, a string-formatting ternary, and a date calculation — all tangled together. The component is 120 lines but barely renders anything. You spend 30 minutes understanding what it does before you can safely change 3 lines.

This is the cost of mixed concerns.

# Lore Introduction

At the Academy, the artificers who craft magical lenses and those who brew the focusing elixirs are different guilds. The lens-crafter works in glass and light; the elixir-brewer works in compounds and heat. When a new student tried to do both in the same workshop, they produced lenses that were also explosive. The Academy learned: *separate the concerns, separate the workshops.*

Your component is the lens. Keep the elixir in a different room.

# Core Learning

## Concept Introduction

**Separation of Concerns** is the principle that each part of your code should be responsible for one thing. In React, the most common violation is embedding **business logic** (calculations, sorting, filtering, validation, data transformation) directly inside **JSX** (the rendering description).

The UI's job is to answer: *What does this look like?*
The logic's job is to answer: *What data should be shown and how should it be processed?*

These are different questions and they change for different reasons. Mixing them makes both harder to understand.

## Why It Matters

When logic lives in JSX:
- **Readability suffers:** Rendering and decision-making are interleaved, making it hard to see either clearly.
- **Testability suffers:** Logic cannot be unit-tested without rendering the component. You cannot test a sort function if it only exists as an expression inside `.map()`.
- **Reusability suffers:** The logic is bound to the specific component and cannot be shared with other components.

When logic is separated:
- The component is a clean, readable template.
- Logic functions are small, testable, and reusable.
- Changes to business rules don't require touching the rendering code.

## Worked Example

Before separation — logic embedded in JSX:

```tsx
const ProductList = ({ products }: { products: Product[] }) => (
  <div>
    {products
      .filter(p => p.stock > 0)
      .sort((a, b) => a.name.localeCompare(b.name))
      .map(p => (
        <div key={p.id} className="p-4 border rounded">
          <h3>{p.name}</h3>
          <p>{p.price < 10 ? 'Budget' : p.price < 50 ? 'Mid-range' : 'Premium'}</p>
        </div>
      ))}
  </div>
);
```

After separation — logic extracted to utility functions:

```tsx
// utils/products.ts
export const getInStockProducts = (products: Product[]) =>
  products.filter(p => p.stock > 0);

export const sortByName = (products: Product[]) =>
  [...products].sort((a, b) => a.name.localeCompare(b.name));

export const getPriceTier = (price: number): string => {
  if (price < 10) return 'Budget';
  if (price < 50) return 'Mid-range';
  return 'Premium';
};

// ProductList.tsx — now just rendering
const ProductList = ({ products }: { products: Product[] }) => {
  const displayProducts = sortByName(getInStockProducts(products));

  return (
    <div>
      {displayProducts.map(p => (
        <div key={p.id} className="p-4 border rounded">
          <h3>{p.name}</h3>
          <p>{getPriceTier(p.price)}</p>
        </div>
      ))}
    </div>
  );
};
```

Now `getInStockProducts`, `sortByName`, and `getPriceTier` can each be tested with a single function call. The component is a clean template.

## Common Mistakes

**Moving logic to the top of the component file but not out of the component.** Logic defined inside the component body (but above the return) is better than inside JSX, but it's still coupled to the component. True separation means putting it in a dedicated file.

**Using comments instead of extraction.** Adding `// filter in-stock products` before a filter expression does not separate concerns — it just labels the mess.

**Treating all conditional rendering as logic.** Conditionally rendering a spinner vs content is *UI logic* and belongs in the component. Computing *whether* the user has permission to see the content is *business logic* and belongs outside.

## Mini Summary

Separation of concerns means keeping business logic (calculations, sorting, filtering, transformation) out of JSX. Logic belongs in utility functions or custom hooks; JSX should be a clean template that describes what to render. This improves readability, testability, and reusability.

# Guided Practice Quest

Work through the steps to identify mixed concerns and practise separating logic from rendering.

# Solo Practice Quest

Find (or write) a React component that mixes business logic with rendering. Describe the separation you would make:

1. What logic would you extract?
2. Where would it go (utility function, hook, service module)?
3. What would the cleaned-up component look like?

Write your answer in 4–6 sentences, including any function signatures you would create.

# Integration

**Philosophy — Single Responsibility Principle:** The SRP says a module should have one reason to change. A component that both processes data and renders it has two reasons to change: business rules changing and UI design changing. Separation of concerns is SRP applied at the component level.

**Design — Cognitive Bandwidth:** Designers and product managers care about the UI. Engineers maintaining business rules care about logic. When these are mixed, every stakeholder must understand both layers to safely change anything. Separation creates cleaner collaboration boundaries between disciplines.

# Lore Conclusion

The Academy's most respected artificers maintain clear guildhalls — the lens-crafters stay with light, the elixir-brewers stay with compounds. When your React components maintain equally clear boundaries — rendering here, logic there — your codebase becomes a place where apprentices can navigate with confidence, and masters can work with speed.

---
