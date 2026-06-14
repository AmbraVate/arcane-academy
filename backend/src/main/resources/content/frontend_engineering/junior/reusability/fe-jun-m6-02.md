---
id: fe-jun-m6-02
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
lesson: designing_for_reuse
title: "Designing for Reuse"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m6-01]
integrationDomains: [design, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Designs a props interface for a realistic component with at least 3 meaningful props"
    - "Includes at least one prop with a sensible default value"
    - "Explains why that interface makes the component flexible without being too generic"
  keywords: [interface, props, default, optional, API]
  modelAnswer: |
    A well-designed component API exposes only the values that legitimately vary between uses. Required props cover the non-negotiable data; optional props with defaults handle common variations. The interface should be discoverable — a developer should be able to use the component correctly without reading its source code.
guidedSteps:
  - id: fe-jun-m6-02-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You are designing an Alert component used to show success, warning, and error messages. Which props interface is best?"
    inputConfig:
      options:
        - "message: string (only)"
        - "message: string, type: 'success' | 'warning' | 'error', dismissible?: boolean"
        - "children: ReactNode, style: CSSProperties"
        - "text: string, color: string, icon: string"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["message: string, type: 'success' | 'warning' | 'error', dismissible?: boolean"]
      rejectedFeedback: "The best interface constrains the type to known values (union type), separates concerns (message vs behaviour), and marks optional props clearly."
    hint: "Which interface gives the component clear semantics while covering all three use-cases?"
    reflectionPrompt: "Why is using a union type ('success' | 'warning' | 'error') better than accepting any string for type?"
  - id: fe-jun-m6-02-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "What is a default prop and when should you use one?"
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [default, optional, common, most, fallback]
      rejectedFeedback: "Think about what happens when a caller doesn't pass a particular prop — a default covers the most common case so callers don't have to."
    hint: "If 90% of buttons in your app are 'primary' variant, what should the default be?"
    reflectionPrompt: "How do default props reduce boilerplate for the callers of your component?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which is the best reason to add a prop with a default value?"
    options:
      - "To make the component's TypeScript types simpler"
      - "To cover the most common usage so callers don't need to pass it every time"
      - "To prevent callers from passing the wrong type"
      - "To make the component render faster"
    correctIndex: 1
    feedback: "Default props represent the most common case. They reduce boilerplate for callers while keeping the component flexible."
retrieval:
  recall: "What are the three things a good component props interface should achieve?"
  explain: "Why is it better to use a union type like 'success' | 'warning' | 'error' instead of accepting any string for a variant prop?"
  mistakeId:
    code: |
      interface ButtonProps {
        label: string;
        onClick: () => void;
        color: string;   // accepts any string
        size: string;    // accepts any string
      }
    answer: "Using string for color and size allows invalid values like color='banana'. Use union types: color: 'primary' | 'danger' | 'ghost' and size: 'sm' | 'md' | 'lg' to constrain to valid options and enable autocomplete."
---

# Hook

A teammate just handed you a `Button` component to use. It has 12 props, none of them optional, with names like `colorHex`, `paddingValue`, and `fontSizeRem`. You spend 20 minutes just trying to figure out how to render a basic primary button. A well-designed API would have taken 20 seconds.

The component *works*. But it's unusable. Designing for reuse means designing a great API.

# Lore Introduction

The Academy's master artificers do not just forge powerful runes — they design them so that apprentices can apply them correctly. A rune with forty required inscriptions, each demanding precise hand measurements, is as useless as no rune at all. The best runes have sensible defaults. Apprentices inscribe them in seconds, customising only when the situation demands it.

Your component API is the rune's instruction scroll.

# Core Learning

## Concept Introduction

Designing for reuse starts before you write any JSX. You begin by asking: **what varies between uses of this component?** Those varying values become props. Everything else is internal implementation.

There are two categories of props:

- **Required props:** values that the component cannot function without. These have no default.
- **Optional props:** values that vary occasionally. These should have sensible defaults.

```tsx
interface BadgeProps {
  label: string;              // required — the text content
  variant?: 'default' | 'success' | 'warning' | 'danger'; // optional
  size?: 'sm' | 'md' | 'lg'; // optional
}

const Badge = ({
  label,
  variant = 'default',
  size = 'md',
}: BadgeProps) => {
  const variantClasses = {
    default: 'bg-gray-100 text-gray-800',
    success: 'bg-green-100 text-green-800',
    warning: 'bg-yellow-100 text-yellow-800',
    danger:  'bg-red-100 text-red-800',
  };

  const sizeClasses = {
    sm: 'px-2 py-0.5 text-xs',
    md: 'px-3 py-1 text-sm',
    lg: 'px-4 py-1.5 text-base',
  };

  return (
    <span className={`rounded-full font-medium ${variantClasses[variant]} ${sizeClasses[size]}`}>
      {label}
    </span>
  );
};
```

The caller only needs `label`. Everything else has a sensible default.

## Why It Matters

A component's props interface is its **public API**. Once other parts of the codebase depend on it, changing the interface becomes costly — just like changing a public API on a backend service. Investing a few minutes in designing the interface upfront pays dividends every time the component is used.

Three principles for a good component API:

**1. Minimal required surface:** Only make props required if the component cannot work without them.

**2. Semantic props, not style props:** Prefer `variant="danger"` over `color="red"`. Semantic props encode intent; style props leak implementation and break under theme changes.

**3. Discoverable defaults:** The most common use-case should require the fewest props. A developer should be able to write `<Button label="Save" />` and get a sensible result.

## Worked Example

Before applying API design thinking:

```tsx
// Unclear interface — too many required props, raw style values
interface CardProps {
  title: string;
  subtitle: string;
  imageUrl: string;
  borderColor: string;
  paddingSize: number;
  showFooter: boolean;
  footerText: string;
}
```

After:

```tsx
interface CardProps {
  title: string;              // required
  subtitle?: string;          // optional — not all cards have subtitles
  imageUrl?: string;          // optional — imageless cards are valid
  variant?: 'default' | 'featured'; // semantic, not raw colour
  footer?: React.ReactNode;   // flexible — caller provides the footer content
}

const Card = ({
  title,
  subtitle,
  imageUrl,
  variant = 'default',
  footer,
}: CardProps) => (
  <div className={`rounded-lg border p-6 ${variant === 'featured' ? 'border-yellow-400 bg-yellow-50' : ''}`}>
    {imageUrl && <img src={imageUrl} alt="" className="mb-4 rounded" />}
    <h3 className="text-lg font-semibold">{title}</h3>
    {subtitle && <p className="text-sm text-gray-500">{subtitle}</p>}
    {footer && <div className="mt-4 border-t pt-4">{footer}</div>}
  </div>
);
```

The caller can now use `<Card title="Getting Started" />` and get a valid card, or progressively add props to unlock more capability.

## Common Mistakes

**Making everything required.** This forces callers to pass props they don't need, creates boilerplate, and makes the component harder to use.

**Accepting raw CSS values as props.** `backgroundColor="#3B82F6"` ties the component to a specific colour system. When the design updates, every call site needs changing.

**Exposing too many props.** A component with 20 props is trying to do too much. Each additional prop is additional complexity the caller must understand. Split the component instead.

**Inconsistent prop naming.** Using `onClick` for one handler and `onPressButton` for another creates friction. Follow React conventions: `onX` for event handlers, `isX` or `hasX` for booleans.

## Mini Summary

Designing for reuse means designing a clear, minimal, semantic props interface before writing JSX. Required props cover non-negotiable data; optional props with defaults cover common variations. Semantic props communicate intent and survive design changes better than raw style values.

# Guided Practice Quest

Work through the steps to practise identifying good component API decisions — required vs optional, semantic vs style props, and where defaults add value.

# Solo Practice Quest

Design a complete props interface (TypeScript `interface`) for one of these components:

- A `Notification` component (toast/alert style)
- A `UserAvatar` component (profile image + fallback initials)
- A `Tag` component (like a category label)

Write the interface with comments explaining why each prop is required or optional, and what defaults you chose and why. Aim for 3–6 props.

# Integration

**Design — Design Tokens:** Professional design systems use tokens (named values like `color.danger` or `space.md`) so that components reference intent, not raw values. This maps directly to semantic props: `variant="danger"` internally resolves to the `color.danger` token. When the token changes (e.g., the danger colour updates from red-600 to red-700), every component using it updates automatically.

**Mathematics — Set Theory:** A union type like `'sm' | 'md' | 'lg'` is literally a finite set. By constraining the prop to a known set of values, you eliminate an entire class of runtime errors. TypeScript's type system applies set-theory reasoning at compile time, catching invalid prop values before the code runs.

# Lore Conclusion

An artificer who designs their rune stones thoughtfully — clear inscriptions, sensible defaults, semantic symbols — earns the respect of every apprentice who uses their work. Your component APIs are your craft signature. Design them with care, and your fellow artificers will thank you for years to come.

---
