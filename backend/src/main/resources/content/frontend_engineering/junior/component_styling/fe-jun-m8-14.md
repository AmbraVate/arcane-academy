---
id: fe-jun-m8-14
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
lesson: conditional_classes
title: "Conditional Classes in React"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m8-13]
integrationDomains: [software_engineering, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the problem with string concatenation for conditional classes"
    - "Correctly uses clsx or classnames to apply conditional classes"
    - "Applies an object syntax with clsx to toggle classes based on boolean props"
    - "Explains why Tailwind's purging can cause issues with dynamic class construction"
  keywords: [clsx, classnames, conditional, string, concatenation, boolean, dynamic, purge, template]
  modelAnswer: |
    String concatenation for conditional classes is fragile — extra spaces, undefined values, and complex conditions make it error-prone. The `clsx` library (or `classnames`) solves this: `clsx('base-class', { 'active-class': isActive, 'disabled-class': isDisabled })`. It handles undefined/null gracefully, supports arrays and objects. An important Tailwind caveat: never construct class names dynamically from variables (e.g. `bg-${colour}-500`) because Tailwind's scanner won't find them — always use complete class strings.
guidedSteps:
  - id: fe-jun-m8-14-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What is wrong with this conditional class approach? `className={'btn ' + (isActive ? 'bg-blue-600' : 'bg-gray-200') + (isDisabled ? ' opacity-50' : '')}`"
    inputConfig:
      options:
        - "It is fragile — extra spaces can appear, undefined creates 'undefined' strings, and it becomes unreadable quickly"
        - "This is fine and idiomatic React"
        - "Tailwind doesn't support conditional classes at all"
        - "The ternary operator doesn't work inside className"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It is fragile — extra spaces can appear, undefined creates 'undefined' strings, and it becomes unreadable quickly"]
      rejectedFeedback: "String concatenation works in simple cases but fails at scale — extra spaces, null/'undefined' literal strings, and unreadable chains of ternaries. `clsx` handles all of this cleanly."
    hint: "What happens if isDisabled is undefined? What does ' ' + undefined give you?"
    reflectionPrompt: "How would you add a third conditional class to the string approach? How does it compare to adding a third entry to a clsx call?"
  - id: fe-jun-m8-14-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Rewrite this using clsx: `className={'tab ' + (isActive ? 'border-b-2 border-blue-500 text-blue-600' : 'text-gray-500 hover:text-gray-700')}`"
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [clsx, isActive, border-b-2, text-gray-500]
      rejectedFeedback: "Using clsx: `className={clsx('tab', { 'border-b-2 border-blue-500 text-blue-600': isActive, 'text-gray-500 hover:text-gray-700': !isActive })}`"
    hint: "clsx takes a base class as the first argument, then an object where keys are class strings and values are boolean conditions."
    reflectionPrompt: "How would you install clsx in a project? (`npm install clsx`)"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why must you avoid `bg-${colour}-500` when constructing Tailwind class names dynamically?"
    options:
      - "Tailwind's static scanner won't detect the complete class name, so it gets purged from the production build"
      - "Template literals don't work in JSX className attributes"
      - "Tailwind doesn't support colour utilities with variables"
      - "The class name is invalid CSS"
    correctIndex: 0
    feedback: "Tailwind scans source files as text to find class names. If the class is constructed at runtime (`bg-${x}-500`), the scanner never sees `bg-red-500` as a complete string — it gets purged. Always use complete class strings in your source."
retrieval:
  recall: "What library provides a clean API for conditional class names in React?"
  explain: "Why does Tailwind's purging break dynamically constructed class names like `text-${size}`?"
  mistakeId:
    code: |
      // A status badge with dynamic colours
      function StatusBadge({ status }) {
        const colour = status === 'active' ? 'green' : 'red';
        return (
          <span className={`bg-${colour}-100 text-${colour}-700 px-2 py-1 rounded`}>
            {status}
          </span>
        );
      }
    answer: "Dynamic class construction breaks Tailwind's purge scanner. In production, `bg-green-100`, `text-green-700`, `bg-red-100`, `text-red-700` will all be purged because the scanner never sees them as complete strings. Fix: use a lookup object with full class strings: `const classes = { active: 'bg-green-100 text-green-700', inactive: 'bg-red-100 text-red-700' }; return <span className={`${classes[status]} px-2 py-1 rounded`}>`"
---

# Hook

A senior artificer reviews an apprentice's component: seventeen ternary operators chained in a className string, creating a tangled string of conditions that nobody can read. "This works," the apprentice says defensively. The senior artificer replies: "Working code is the minimum. Readable code is the standard." She shows the apprentice `clsx`.

# Lore Introduction

Master Classticus teaches the art of clean conditional styling: "A component that changes its appearance based on state is not unusual — it is essential. But the way you express those conditions matters as much as the conditions themselves. Code that is hard to read is code that is hard to maintain, and code that is hard to maintain is a debt paid in future pain."

# Core Learning

## Concept Introduction

**The problem with string concatenation:**
```jsx
// FRAGILE
className={
  'btn ' +
  (variant === 'primary' ? 'bg-blue-600 text-white' : 'bg-gray-100 text-gray-900') +
  (size === 'lg' ? ' px-6 py-3 text-lg' : ' px-4 py-2') +
  (disabled ? ' opacity-50 cursor-not-allowed' : '') +
  (isLoading ? ' animate-pulse' : '')
}
// What if disabled is undefined? What if variant is undefined?
```

**The clsx solution:**
```bash
npm install clsx
```

```jsx
import { clsx } from 'clsx';

// Object syntax: key = class string, value = boolean condition
className={clsx(
  // Base classes always applied
  'btn font-medium rounded-lg transition-all',
  // Variant
  {
    'bg-blue-600 text-white hover:bg-blue-700': variant === 'primary',
    'bg-gray-100 text-gray-900 hover:bg-gray-200': variant === 'secondary',
    'bg-red-600 text-white hover:bg-red-700': variant === 'danger',
  },
  // Size
  {
    'px-6 py-3 text-lg': size === 'lg',
    'px-4 py-2 text-sm': size === 'md',
    'px-2 py-1 text-xs': size === 'sm',
  },
  // States
  {
    'opacity-50 cursor-not-allowed': disabled,
    'animate-pulse': isLoading,
  }
)}
```

`clsx` handles `undefined`, `null`, `false` — none of them produce unexpected strings.

## Why It Matters

**The Tailwind purge caveat:**

Never construct class names from variables:
```jsx
// BROKEN in production — purge removes these classes
const colour = 'blue';
className={`bg-${colour}-500`}  // scanner never sees 'bg-blue-500'

// CORRECT — full class strings the scanner can detect
const classes = {
  blue: 'bg-blue-500 hover:bg-blue-600',
  red: 'bg-red-500 hover:bg-red-600',
};
className={classes[colour]}  // scanner sees the full strings
```

## Worked Example

```tsx
// components/Button.tsx — a fully typed, conditional-class button
import { clsx } from 'clsx';

type Variant = 'primary' | 'secondary' | 'ghost' | 'danger';
type Size = 'sm' | 'md' | 'lg';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: Variant;
  size?: Size;
  isLoading?: boolean;
}

const variantClasses: Record<Variant, string> = {
  primary:   'bg-blue-600 text-white hover:bg-blue-700 focus:ring-blue-500',
  secondary: 'bg-gray-100 text-gray-900 hover:bg-gray-200 focus:ring-gray-400',
  ghost:     'bg-transparent text-blue-600 hover:bg-blue-50 focus:ring-blue-500',
  danger:    'bg-red-600 text-white hover:bg-red-700 focus:ring-red-500',
};

const sizeClasses: Record<Size, string> = {
  sm: 'px-3 py-1.5 text-sm',
  md: 'px-4 py-2 text-sm',
  lg: 'px-6 py-3 text-base',
};

export function Button({
  variant = 'primary',
  size = 'md',
  isLoading = false,
  disabled,
  children,
  className,
  ...props
}: ButtonProps) {
  return (
    <button
      {...props}
      disabled={disabled || isLoading}
      className={clsx(
        'inline-flex items-center justify-center font-medium rounded-lg',
        'focus:outline-none focus:ring-2 focus:ring-offset-2',
        'transition-all duration-150',
        variantClasses[variant],
        sizeClasses[size],
        {
          'opacity-50 cursor-not-allowed': disabled || isLoading,
          'gap-2': isLoading,
        },
        className  // allow override from parent
      )}
    >
      {isLoading && <span className="animate-spin">⟳</span>}
      {children}
    </button>
  );
}
```

## Common Mistakes

- **Constructing class names from variables** — always use complete strings in lookup objects.
- **Template literals for conditions** — `` `${condition ? 'class-a' : 'class-b'}` `` works but gets unwieldy; clsx is cleaner.
- **Forgetting to install clsx** — it's not bundled with Tailwind; `npm install clsx` is required.

## Mini Summary

Use `clsx` for conditional Tailwind classes — it handles undefined/null gracefully and scales cleanly. Always use complete class strings (not constructed ones) so Tailwind's scanner can detect them. Store variant class strings in lookup objects.

# Guided Practice Quest

Work through the guided steps to practise identifying fragile string concatenation and refactoring it with clsx.

# Solo Practice Quest

Build a `<Tab>` component that: takes `label` and `isActive` props; uses `clsx` to apply active/inactive styles; and shows a blue bottom border and bold text when active, grey text when inactive.

# Integration

**Software Engineering:** The pattern of lookup objects (`variantClasses[variant]`) is a strategy pattern — selecting behaviour from a map rather than a chain of if/else. This is the same pattern used throughout application code for polymorphic behaviour.

**Design:** Variant components (primary/secondary/danger buttons) mirror the design system concept of "component variants" — one component, multiple visual expressions, governed by a clear prop API.

# Lore Conclusion

Master Classticus reviews the refactored component. Clean, readable, type-safe — every class string visible to the scanner. "Now," she says, "when a new variant is requested, you add a single entry to an object. When the colours change, you update one string. This is not just cleaner — it is the architecture of a component that can grow." The apprentice deletes their seventeen-ternary string and never looks back.

---
