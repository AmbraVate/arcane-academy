---
id: fe-jun-m8-15
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
lesson: building_a_component_library
title: "Building a Component Library with Tailwind"
sortOrder: 3
difficulty: 4
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m8-13, fe-jun-m8-14]
integrationDomains: [design, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains how to use variant props to style components differently"
    - "Describes how to create a consistent component API"
    - "Explains why a component library with Tailwind differs from a CSS library"
    - "Identifies what belongs in a reusable component vs what belongs in the page"
  keywords: [variant, props, api, consistent, reusable, Button, Card, Input, clsx, interface]
  modelAnswer: |
    A Tailwind component library means creating React components with well-defined prop interfaces that control styling via variants. A Button component accepts a variant prop ('primary' | 'secondary' | 'danger') and maps each variant to a set of Tailwind classes. The component encapsulates styling decisions — consumers just choose a variant. This is different from a CSS library (like Bootstrap) where classes are applied directly in HTML; here the styling knowledge lives inside the component, not in the caller. A good component API is: small (few required props), flexible (accepts className for overrides), and predictable (same props = same appearance everywhere).
guidedSteps:
  - id: fe-jun-m8-15-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A Button component has primary, secondary, and danger variants. Where should the Tailwind classes for each variant be defined?"
    inputConfig:
      options:
        - "In the page or form component that uses the Button"
        - "Inside the Button component, mapping variant prop values to class strings"
        - "In a separate CSS file imported by Tailwind"
        - "In the Tailwind config as custom utilities"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Inside the Button component, mapping variant prop values to class strings"]
      rejectedFeedback: "Callers should not know which Tailwind classes implement a 'primary' button. That knowledge belongs inside the Button component — it's an implementation detail. Callers just pass variant='primary'. This is encapsulation: the styling contract is expressed as a prop, not as a class string."
    hint: "Who should know that 'primary' means 'bg-blue-600 hover:bg-blue-700'? The button or the page using it?"
    reflectionPrompt: "When you centralise styling decisions in the component, changing the design of 'primary' buttons requires changing one file — not hunting through every page that uses a blue button."
  - id: fe-jun-m8-15-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "What should a reusable Button component do when a consumer passes a `className` prop alongside a `variant` prop?"
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [merge, combine, clsx, override, extend, consumer, additional, alongside]
      rejectedFeedback: "The component should merge the variant classes with the additional className: clsx(variantClasses[variant], className). This lets consumers extend or override styles when needed without losing the variant foundation. Components that ignore className are inflexible; components that only use className have no encapsulation. The pattern: variant handles the common case, className handles the exception."
    hint: "Should the component apply both, ignore one, or let the consumer decide?"
    reflectionPrompt: "Accepting className alongside variants is a principle of composable APIs: provide sensible defaults, allow escape hatches. The variant is the rule; className is the exception."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which pattern makes a Tailwind component reusable across the application?"
    options:
      - "Hardcode all Tailwind classes directly in each page component"
      - "Create a component that accepts variant and size props mapped to Tailwind class sets"
      - "Use a global CSS file with .btn-primary class"
      - "Pass full class strings from parent to child as props"
    correctIndex: 1
    feedback: "Variant + size props encapsulate styling knowledge inside the component. Callers use a semantic API (variant='primary', size='lg') without knowing the underlying Tailwind classes. This makes the design system consistent and the component easy to update."
retrieval:
  recall: "What is a variant prop and how does it map to Tailwind classes?"
  explain: "Why should a reusable Button component accept a className prop in addition to a variant prop?"
  mistakeId:
    code: |
      // Every page uses different ad-hoc button styles
      // HomePage.jsx
      <button className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
        Submit
      </button>
      // ContactPage.jsx
      <button className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">
        Send
      </button>
    answer: "These buttons look similar but use slightly different classes — different shades, different rounding. When the designer changes the primary button colour, every page must be updated manually. The fix: create a <Button variant='primary'> component with the canonical classes in one place. Both pages use <Button variant='primary'>. When the design changes, you update the Button component — everywhere updates instantly."
---

# Hook

Twelve months into the project, your designer wants to change the primary button colour. You search the codebase: `bg-blue-600` appears 47 times. You update 32 of them. Three months later a tester finds buttons that still look old. You missed 15.

This is the cost of no component library. One source of truth would have meant one change.

# Lore Introduction

*"The Artificer's Guild standardises its components,"* Master Quinley explains. *"Every lantern is made from the same design. Every door handle uses the same fitting. When we improve the design, every lantern in the city updates — because they all came from the same blueprint."*

She hands you a set of component blueprints. *"Build your components this way. One blueprint. Infinite instances."*

# Core Learning

## Concept Introduction

A **Tailwind component library** is a set of React components that:
- Accept semantic props (variant, size, disabled) instead of class strings
- Encapsulate Tailwind class decisions internally
- Provide a consistent API across the application

## Why It Matters

Without a component library:
- The same UI element has slightly different classes in every place it's used
- Design changes require hunting through every usage
- Inconsistencies multiply as the team grows

With a component library:
- One source of truth for each component's styling
- Design changes propagate everywhere automatically
- New team members use the library; they don't invent new classes

## Worked Example

```tsx
// components/Button.tsx
import { clsx } from 'clsx';

type ButtonVariant = 'primary' | 'secondary' | 'danger';
type ButtonSize = 'sm' | 'md' | 'lg';

interface ButtonProps {
  variant?: ButtonVariant;
  size?: ButtonSize;
  disabled?: boolean;
  className?: string;
  children: React.ReactNode;
  onClick?: () => void;
}

const variantClasses: Record<ButtonVariant, string> = {
  primary:   'bg-blue-600 text-white hover:bg-blue-700 focus:ring-blue-500',
  secondary: 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50',
  danger:    'bg-red-600 text-white hover:bg-red-700 focus:ring-red-500',
};

const sizeClasses: Record<ButtonSize, string> = {
  sm: 'px-3 py-1.5 text-sm',
  md: 'px-4 py-2 text-base',
  lg: 'px-6 py-3 text-lg',
};

export function Button({
  variant = 'primary',
  size = 'md',
  disabled = false,
  className,
  children,
  onClick,
}: ButtonProps) {
  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className={clsx(
        'inline-flex items-center justify-center rounded-md font-medium',
        'focus:outline-none focus:ring-2 focus:ring-offset-2',
        'transition-colors duration-150',
        variantClasses[variant],
        sizeClasses[size],
        disabled && 'opacity-50 cursor-not-allowed',
        className,
      )}
    >
      {children}
    </button>
  );
}
```

```tsx
// Usage — callers never write Tailwind classes for buttons
<Button variant="primary" size="lg" onClick={handleSubmit}>
  Save Changes
</Button>
<Button variant="danger" onClick={handleDelete}>
  Delete Account
</Button>
<Button variant="secondary" size="sm">
  Cancel
</Button>
```

**A simple Card and Input follow the same pattern:**
```tsx
// Card — wraps content in a consistent container
<Card className="mt-4">
  <CardHeader>User Profile</CardHeader>
  <CardBody>...</CardBody>
</Card>

// Input — accessible, consistently styled
<Input label="Email" type="email" value={email} onChange={setEmail} />
```

## Common Mistakes

- **Exposing Tailwind classes as a prop.** `<Button className="bg-red-600">` defeats the purpose — callers are back to writing raw classes. Use semantic variant props instead.
- **Too many variants.** Keep it simple: primary, secondary, danger covers most cases. Add more only when there's a clear need.
- **Forgetting className for escape hatches.** Some cases need custom spacing or width. Accept className and merge it with clsx.
- **Not documenting the API.** A component library without documentation is unusable. Even simple JSDoc comments on each prop help enormously.

## Mini Summary

- Build components that accept semantic props (variant, size) not raw class strings
- Keep styling decisions inside the component, not in the caller
- Use clsx to merge variant classes with optional consumer className
- A small library (Button, Card, Input, Badge) covers 80% of UI needs

# Guided Practice Quest

Work through the two guided steps to confirm you understand how variant-based component APIs work and how to handle consumer overrides.

# Solo Practice Quest

Design the API for a `<Badge>` component. Decide: what variants (status types) will it support? What size options? What additional props? Write the TypeScript interface for its props and show two examples of using it. No implementation needed — just the API design.

# Integration

**Design — Design Systems as Languages**

Brad Frost's Atomic Design and the broader design systems movement describe component libraries as shared languages between design and engineering. When a designer says "use a primary button" and an engineer writes `<Button variant="primary">`, they're using the same vocabulary to describe the same thing. This shared language reduces translation errors — misunderstandings between what the designer specified and what the engineer built. From a cognitive science perspective, shared vocabularies reduce cognitive load: instead of reasoning about visual appearance (which shade of blue? which border radius?), both parties reason about semantics (primary, secondary, danger). The component library is the dictionary that makes this language precise.

# Lore Conclusion

*"The blueprints are set,"* Master Quinley says, reviewing the component library. *"Every lantern in the city now follows the same design. Tomorrow, we improve the wick — and every lantern in the city burns brighter."*

The Tailwind module is complete.

---
