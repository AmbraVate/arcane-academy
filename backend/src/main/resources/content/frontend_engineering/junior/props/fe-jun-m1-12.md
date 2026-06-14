---
id: fe-jun-m1-12
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
lesson: default_props
title: "Default Props"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-11]
integrationDomains: [design, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what default props are and why they matter"
    - "Uses parameter default values correctly"
    - "Distinguishes required from optional props"
    - "Applies the principle of least surprise to default values"
  keywords: [default, optional, parameter, destructure, undefined, fallback, DX, API]
  modelAnswer: |
    Default props provide fallback values for optional props when they are not passed. In modern React, they are set via parameter default values in the function signature. Good defaults follow the principle of least surprise — they represent the most common use case so callers don't have to specify obvious values.
guidedSteps:
  - id: fe-jun-m1-12-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What is the modern way to set default prop values in a functional component?"
    inputConfig:
      options:
        - "Using Component.defaultProps = {}"
        - "Using parameter default values in the function signature"
        - "Using a separate defaults object"
        - "Using useState for each default"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Using parameter default values in the function signature"]
      rejectedFeedback: "Parameter defaults in the destructured signature are the modern approach: `function Button({ label = 'Click', size = 'md' })`. `Component.defaultProps` is legacy and deprecated in React 19."
    hint: "ES6 parameter defaults work naturally with destructured props."
    reflectionPrompt: "`defaultProps` is deprecated. Use parameter defaults. They're more readable, closer to the JS standard, and work with TypeScript naturally."
  - id: fe-jun-m1-12-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write a Button component with default props: variant defaults to 'primary', size defaults to 'md', and disabled defaults to false."
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: [primary, md, false, variant, size, disabled]
      rejectedFeedback: "Example: `function Button({ label, variant = 'primary', size = 'md', disabled = false }) { return <button ...>; }`"
    hint: "Set defaults directly in the destructured parameter list."
    reflectionPrompt: "Good defaults make a component easy to use in the common case. Users only specify what differs from the default."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A Button has `disabled = false` as a default. What happens when `<Button />` is rendered without any disabled prop?"
    options:
      - "disabled is undefined"
      - "disabled is false (the default)"
      - "An error is thrown"
      - "disabled is null"
    correctIndex: 1
    feedback: "JavaScript parameter defaults activate when the argument is `undefined`. If no `disabled` prop is passed (undefined), the default `false` is used. If `disabled={null}` is explicitly passed, null overrides the default — a subtle distinction."
retrieval:
  recall: "When does a parameter default value activate in JavaScript?"
  explain: "What makes a good default prop value?"
  mistakeId:
    code: "function Card({ title = '', items = [] }) — using mutable objects/arrays as defaults"
    answer: "Mutable defaults like `[]` and `{}` are technically fine in functional components (unlike class components), because each function call creates a new array. However, they can cause issues with referential equality checks in useEffect and memo. Prefer creating defaults outside the component or handling undefined explicitly."
---

# Hook

The best component APIs are effortless for the common case. A `Button` that requires you to pass `variant="primary" size="md" disabled={false}` every time is exhausting. Default props solve this: define sensible defaults so callers only need to specify what differs.

# Lore Introduction

*"A skilled runesmith,"* Aelindra says, *"designs formulas that work beautifully with minimal input. The common case requires no specification. The special case overrides. This is craftsmanship."*

# Core Learning

## Concept Introduction

Default props provide **fallback values** for optional props:

```jsx
function Alert({
  message,
  type = 'info',
  dismissible = true,
  icon = null
}) {
  return (
    <div className={`alert alert-${type}`}>
      {icon && <span className="icon">{icon}</span>}
      <p>{message}</p>
      {dismissible && <button>×</button>}
    </div>
  );
}

// Minimal usage — defaults handle the rest
<Alert message="Your changes have been saved." />

// Override when needed
<Alert message="Error!" type="error" dismissible={false} icon="⚠️" />
```

| Approach | Status |
|---|---|
| Parameter defaults | Modern — use this |
| `Component.defaultProps = {}` | Legacy — deprecated in React 19 |

## Why It Matters

Good defaults improve **developer experience (DX)**. They encode the most common usage in the component itself — callers get the right behaviour without knowing all the options.

## Worked Example

```tsx
interface ButtonProps {
  label: string;
  variant?: 'primary' | 'secondary' | 'danger';
  size?: 'sm' | 'md' | 'lg';
  isLoading?: boolean;
  onClick?: () => void;
}

function Button({
  label,
  variant = 'primary',
  size = 'md',
  isLoading = false,
  onClick
}: ButtonProps) {
  return (
    <button
      className={`btn btn-${variant} btn-${size}`}
      disabled={isLoading}
      onClick={onClick}
    >
      {isLoading ? 'Loading…' : label}
    </button>
  );
}
```

## Common Mistakes

- **Using `defaultProps` in new code.** Use parameter defaults instead.
- **Over-defaulting required props.** If a prop is truly required, mark it required — don't hide missing data behind a default.
- **Unintuitive defaults.** A `disabled` default of `true` would break every usage. Defaults should represent the expected common case.

## Mental Model

Default props are factory settings. A new phone arrives working — ringtone chosen, brightness reasonable, language guessed from region — because the manufacturer decided every setting must have a sensible answer *before* the customer touches anything. Customers who care override (`<Button size="large" />`); the silent majority get good behaviour for free (`<Button />` just works). Designing a component's defaults is the same act of judgement the manufacturer makes: the default should be the choice *most* users want, safe rather than surprising — a Button defaults to `type="button"` (not the form-submitting `"submit"`), a Modal defaults to closed, a list to empty rather than undefined. The deeper payoff is what defaults do to your component's internals: once every optional prop is guaranteed a value, the code below stops bristling with `if (size === undefined)` guards — the factory settled those questions at the gate. A component without defaults pushes its decisions onto every caller; a component with good defaults makes the common case effortless and the custom case possible. That asymmetry — easy by default, flexible on demand — is what makes a component pleasant to use seventeen times a day.

## Mini Summary

- Default props use ES6 parameter defaults in the function signature
- Only truly optional props get defaults
- Defaults should represent the most common use case
- `Component.defaultProps` is deprecated — don't use it in new code

# Guided Practice Quest

Work through the guided steps on writing and reasoning about default props.

# Solo Practice Quest

Design a `Tooltip` component with these props: `content` (required string), `position` (optional, default 'top'), `delay` (optional ms, default 300), `maxWidth` (optional px, default 200), `children` (required, the element to wrap). Write the full component signature with TypeScript types and defaults. Then write 2–3 sentences explaining how you chose the default values and why.

# Integration

**Psychology — Principle of Least Surprise**

The principle of least surprise (also called the principle of least astonishment) states that a system should behave in a way that most users expect. Good default props embody this: they represent the outcome users expect when they don't specify anything. This is a core principle of both UX design and software API design. When defaults violate expectations — a submit button that's disabled by default, a modal that auto-closes after 1 second — they create cognitive friction and bugs.

# Lore Conclusion

*"The most elegant formula,"* Aelindra says, *"is the one that does exactly what you need without being told. Study the common case. Make it effortless. Reserve configuration for the rare exception."*

---
