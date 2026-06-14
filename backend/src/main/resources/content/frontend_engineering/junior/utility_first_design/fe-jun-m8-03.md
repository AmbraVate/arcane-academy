---
id: fe-jun-m8-03
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m8
moduleTitle: "Module 8: Tailwind CSS"
moduleGlyph: "🎨"
moduleSortOrder: 8
topicSlug: utility_first_design
topicTitle: "Utility-First Design"
topicSortOrder: 1
lesson: extracting_components
title: "Extracting Components to Avoid Repetition"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m8-01, fe-jun-m8-02]
integrationDomains: [design, software_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains why repeating long Tailwind class strings is a maintenance problem"
    - "Describes the React component extraction approach to solving repetition"
    - "Explains what @apply does and identifies a tradeoff of using it"
    - "Recommends the preferred approach (React component extraction over @apply) with reasoning"
  keywords: [extract, component, repeat, apply, DRY, reuse, props, variant, class]
  modelAnswer: |
    When a Tailwind class string is repeated across many elements, a change requires updating every instance. The React-native solution is to extract a component — a `<Button>` or `<Badge>` that encapsulates the class string once. This is DRY and gives you a typed, reusable abstraction. Tailwind also offers `@apply` in CSS files, which extracts utilities into a named class, but this reintroduces the naming problem and is harder to pass props to. For component-based frameworks like React, extracting components is nearly always preferable to @apply.
guidedSteps:
  - id: fe-jun-m8-03-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You have a badge element styled with 8 Tailwind classes, used in 20 places. What is the best way to avoid repeating the class string?"
    inputConfig:
      options:
        - "Extract a <Badge> React component that encapsulates the class string"
        - "Use @apply in a CSS file to create a .badge class"
        - "Store the class string in a JavaScript constant and spread it"
        - "Copy-paste — it's fine for styling"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Extract a <Badge> React component that encapsulates the class string"]
      rejectedFeedback: "In React, extracting a component is the idiomatic solution. You get props, TypeScript types, a single place to change styles, and no new CSS file."
    hint: "Think about what React already gives you for reuse."
    reflectionPrompt: "What else does a React component give you that a CSS class cannot? (Think: props, types, logic)"
  - id: fe-jun-m8-03-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write a simple React Badge component that accepts a `label` prop and `variant` prop (default: 'default', options: 'success' | 'warning'). Apply different Tailwind classes for each variant."
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [variant, className, label, bg-, text-, props]
      rejectedFeedback: "The component should accept variant as a prop and use it to select different Tailwind class strings. Use a conditional or object lookup to pick the right classes."
    hint: "Try an object like `const styles = { default: 'bg-gray-100 text-gray-800', success: 'bg-green-100 text-green-800' }` and index into it with variant."
    reflectionPrompt: "What would you need to change if you later added a 'danger' variant? Notice how isolated that change is."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the main tradeoff of using @apply to extract Tailwind utilities?"
    options:
      - "It reintroduces named CSS classes and a separate CSS file, working against utility-first's co-location benefit"
      - "It permanently deletes the utility classes from Tailwind's output"
      - "It makes the component impossible to override with additional classes"
      - "@apply does not work with modern Tailwind versions"
    correctIndex: 0
    feedback: "@apply works, but it creates a CSS class with a name — the thing utility-first aims to avoid. You lose co-location and gain a CSS file to maintain. Use React components instead."
retrieval:
  recall: "What is the recommended approach in React to avoid repeating Tailwind class strings?"
  explain: "Explain the DRY principle as it applies to Tailwind class strings in a component-based app."
  mistakeId:
    code: |
      /* styles.css */
      .primary-btn {
        @apply bg-blue-600 text-white font-semibold px-4 py-2 rounded-lg hover:bg-blue-700;
      }

      /* Used everywhere as: */
      <button className="primary-btn">Click me</button>
    answer: "This uses @apply, reintroducing a named CSS class and a separate stylesheet. The better approach is a <Button> component: `function Button({ children }) { return <button className='bg-blue-600 text-white font-semibold px-4 py-2 rounded-lg hover:bg-blue-700'>{children}</button>; }`. Now you can add `variant`, `disabled`, and `size` props cleanly."
---

# Hook

An apprentice finishes styling the Academy's quest board. She counts seventeen buttons, each with the same twelve Tailwind classes. The Design Master then asks her to change the border-radius on all buttons. Seventeen find-and-replace operations later, she vows there must be a better way. There is.

# Lore Introduction

In the Academy's Replication Chamber, a powerful truth is carved into the wall: *"Repeat a spell once, it is convenience. Repeat it seventeen times, it is a curse."* The antidote is extraction — distilling the pattern into a named artefact that can be summoned anywhere. In React, that artefact is a component.

# Core Learning

## Concept Introduction

When you use Tailwind in a React app, you will eventually write the same class string multiple times. This is the extraction problem.

**The problem:**
```jsx
// In three different files...
<button className="bg-blue-600 text-white font-semibold px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors">
  Save
</button>
<button className="bg-blue-600 text-white font-semibold px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors">
  Submit
</button>
<button className="bg-blue-600 text-white font-semibold px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors">
  Confirm
</button>
```

Change one thing → update three (or thirty) files. This violates DRY.

**Solution 1: React component extraction (preferred)**
```jsx
// components/Button.tsx
type ButtonVariant = 'primary' | 'secondary' | 'danger';

const variantClasses: Record<ButtonVariant, string> = {
  primary: 'bg-blue-600 text-white hover:bg-blue-700',
  secondary: 'bg-gray-100 text-gray-900 hover:bg-gray-200',
  danger: 'bg-red-600 text-white hover:bg-red-700',
};

interface ButtonProps {
  variant?: ButtonVariant;
  children: React.ReactNode;
  onClick?: () => void;
  disabled?: boolean;
}

export function Button({ variant = 'primary', children, onClick, disabled }: ButtonProps) {
  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className={`${variantClasses[variant]} font-semibold px-4 py-2 rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed`}
    >
      {children}
    </button>
  );
}

// Usage everywhere:
<Button variant="primary">Save</Button>
<Button variant="danger">Delete</Button>
```

Now a style change is a single-file update.

## Why It Matters

**@apply (use sparingly):**
```css
/* globals.css */
.btn-primary {
  @apply bg-blue-600 text-white font-semibold px-4 py-2 rounded-lg hover:bg-blue-700;
}
```

`@apply` extracts utilities into a named CSS class. It works but has downsides:
- Reintroduces named classes (the thing we were escaping)
- No props, no variants, no TypeScript
- Requires a separate CSS file
- Harder to conditionally apply

Tailwind's own documentation recommends preferring component extraction over `@apply` in component frameworks.

## Worked Example

```jsx
// A reusable Badge component with variants
const badgeVariants = {
  default: 'bg-gray-100 text-gray-700',
  success: 'bg-green-100 text-green-700',
  warning: 'bg-yellow-100 text-yellow-800',
  error: 'bg-red-100 text-red-700',
};

function Badge({ label, variant = 'default' }) {
  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${badgeVariants[variant]}`}>
      {label}
    </span>
  );
}

// Usage
<Badge label="Active" variant="success" />
<Badge label="Pending" variant="warning" />
<Badge label="Failed" variant="error" />
```

## Common Mistakes

- **Storing class strings in constants** — `const btnClass = "bg-blue-..."` and spreading it avoids components but loses all the benefits (props, types, encapsulation).
- **Using @apply everywhere** — reserve it for global base styles like `body` typography or third-party HTML where you cannot add React components.
- **Giant className strings on every element** — if a component has more than 8–10 classes, consider extracting it even if it's used once, for readability.

## Mini Summary

Repetition in Tailwind class strings is solved by React component extraction. A `<Button variant="primary">` component encapsulates all the styling logic, accepts props for variants, and provides a single place to change styles. `@apply` exists but should be a last resort in component-based apps.

# Guided Practice Quest

Work through the guided steps to practise recognising when to extract a component and how to implement variants.

# Solo Practice Quest

Write a `<Card>` component that accepts `variant` ('default' | 'highlighted'), `title`, and `children` props. Apply different Tailwind backgrounds for each variant and include appropriate padding and rounded corners.

# Integration

**Design:** Component extraction mirrors the concept of design components in Figma or Sketch — a single source of truth for a visual pattern that can be reused across the design. When the designer changes the component, everything updates. The same principle applies in code.

**Software Engineering:** This is DRY (Don't Repeat Yourself) applied to styling. The extract-component refactoring is a standard move in the software engineer's toolkit, and it works identically whether you're extracting logic or Tailwind classes.

# Lore Conclusion

The apprentice extracts a `<QuestButton>` component. Now when the Design Master asks to change the border-radius, she updates one line in one file. The change ripples instantly to all seventeen buttons. She carves her own inscription on the Replication Chamber wall: *"One source of truth, infinite applications."*

---
