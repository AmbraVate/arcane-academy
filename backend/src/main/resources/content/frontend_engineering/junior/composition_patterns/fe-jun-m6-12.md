---
id: fe-jun-m6-12
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m6
moduleTitle: "Module 6: Component Design"
moduleGlyph: "🧩"
moduleSortOrder: 6
topicSlug: composition_patterns
topicTitle: "Composition Patterns"
topicSortOrder: 4
lesson: composition_vs_inheritance
title: "Composition over Inheritance"
sortOrder: 3
difficulty: 5
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m6-10, fe-jun-m6-11]
integrationDomains: [philosophy, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains why React recommends composition over inheritance for component reuse"
    - "Describes a scenario where a developer might be tempted to use inheritance but composition is better"
    - "Explains the flexibility advantage composition provides"
  keywords: [composition, inheritance, children, props, flexible, combine, extend]
  modelAnswer: |
    React recommends composition because it is more flexible: you can combine components in any arrangement by passing them as children or props, without being locked into a class hierarchy. Inheritance creates tight coupling between the base class and the subclass — changing the base can break all subclasses in unexpected ways. With composition, components are independent and can be combined in any combination. React's own team has stated they have never found a use case where component inheritance was a better solution than composition.
guidedSteps:
  - id: fe-jun-m6-12-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "React's documentation recommends composition over inheritance for component reuse. What is the primary reason?"
    inputConfig:
      options:
        - "Inheritance requires class components which are slower than function components"
        - "Composition is more flexible — components can be combined in any arrangement without class coupling"
        - "TypeScript cannot express inheritance correctly for React components"
        - "Inheritance causes more re-renders"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Composition is more flexible — components can be combined in any arrangement without class coupling"]
      rejectedFeedback: "The flexibility argument is the core one. Composition lets you combine independent pieces freely; inheritance locks you into a fixed hierarchy."
    hint: "Consider what happens when a 'ButtonWithIcon' needs to be combined with features from 'LoadingButton'. Inheritance can only have one parent."
    reflectionPrompt: "What problem arises in an inheritance hierarchy if you need a class that inherits from two different base classes?"
  - id: fe-jun-m6-12-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Explain how you would build a SpecialButton that has both a loading state and an icon, using composition rather than inheritance."
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [children, props, compose, wrap, pass, icon, loading, component]
      rejectedFeedback: "With composition, you pass the icon as a prop or child to the Button, and manage loading state via a wrapper or hook — combining independent, focused components rather than extending a class."
    hint: "A Button accepts children. An icon is content. A loading state is a prop or a wrapper. Combine, don't inherit."
    reflectionPrompt: "Could you achieve the same result by extending a class? What would you lose?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The React documentation says 'at Facebook, we use React in thousands of components, and we haven't found any use cases where we would recommend creating component inheritance hierarchies.' What does this imply?"
    options:
      - "Facebook uses a special React version that supports inheritance better"
      - "Composition is sufficient for all component reuse needs in React"
      - "Inheritance works fine but Facebook prefers it stylistically"
      - "Only class components support inheritance anyway"
    correctIndex: 1
    feedback: "The React team's experience across a massive codebase confirms that composition handles all the reuse cases inheritance would, without the coupling and fragility of class hierarchies."
retrieval:
  recall: "What does 'composition over inheritance' mean in the context of React?"
  explain: "Why is the diamond problem not solvable with class inheritance but trivially solvable with composition?"
  mistakeId:
    code: |
      class BaseCard extends React.Component {
        renderHeader() { return <div className="card-header" />; }
        render() { return <div className="card">{this.renderHeader()}</div>; }
      }

      class ProductCard extends BaseCard {
        renderHeader() {
          return <div className="card-header product-header"><img src={this.props.image} /></div>;
        }
      }
    answer: "This is component inheritance via class overriding. Use composition instead: a Card component accepts a header prop (ReactNode). ProductCard renders <Card header={<img src={image} />}> — no inheritance, no coupling, fully composable."
---

# Hook

You have a `Button` component. You need a `LoadingButton` (shows a spinner) and an `IconButton` (shows an icon). Then a manager asks for a `LoadingIconButton`. With inheritance you need a three-level class hierarchy — or worse, a diamond. With composition, you pass an `icon` prop and an `isLoading` prop to the same `Button`. Done in minutes.

Composition scales. Inheritance fractures.

# Lore Introduction

In the Academy's early days, artificers would forge new artefacts by inheriting properties from old ones — a sword that inherited the fire rune, then a sword that inherited both fire and frost. But what of a sword needing frost, fire, AND lightning? Three parents? The hierarchy collapsed. The master artificers convened and declared: *no more inheritance chains. Assemble components. Combine freely.*

This is the doctrine of Composition.

# Core Learning

## Concept Introduction

**Composition** means combining simple, focused components to build complex ones. **Inheritance** means extending a base class to create a specialised version.

React explicitly recommends composition over inheritance for component reuse:

> *"At Facebook, we use React in thousands of components, and we haven't found any use cases where we would recommend creating component inheritance hierarchies."*
> — React Documentation

The reason is simple: composition is more flexible. You can combine any components in any arrangement. Inheritance locks you into a single parent chain.

## Why Inheritance Is Problematic for UI

**1. The Fragile Base Class Problem:** Changes to a base class can break all subclasses in unexpected ways. In a deep component hierarchy, changing the base component's render output ripples through every child.

**2. The Diamond Problem:** In class inheritance, a class can only have one parent. If `LoadingButton` extends `Button` and `IconButton` extends `Button`, how do you create `LoadingIconButton`? You cannot cleanly inherit from both. With composition, this is trivial: `<Button isLoading={loading} leftIcon={<Icon />} />`.

**3. Rigid Coupling:** Subclasses are tightly coupled to the implementation details of their parents. You cannot understand a subclass without understanding its entire parent chain.

## Composition in Practice

Instead of extending components, combine them:

```tsx
// Composition-based Button — flexible via props and children
interface ButtonProps {
  variant?: 'primary' | 'secondary' | 'ghost' | 'danger';
  size?: 'sm' | 'md' | 'lg';
  isLoading?: boolean;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  disabled?: boolean;
  onClick?: () => void;
  children: React.ReactNode;
}

const Button = ({
  variant = 'primary',
  size = 'md',
  isLoading = false,
  leftIcon,
  rightIcon,
  disabled,
  onClick,
  children,
}: ButtonProps) => {
  const variantClasses = {
    primary: 'bg-blue-600 text-white hover:bg-blue-700',
    secondary: 'bg-gray-100 text-gray-900 hover:bg-gray-200',
    ghost: 'bg-transparent text-gray-700 hover:bg-gray-100',
    danger: 'bg-red-600 text-white hover:bg-red-700',
  };

  const sizeClasses = {
    sm: 'px-3 py-1.5 text-sm',
    md: 'px-4 py-2 text-base',
    lg: 'px-6 py-3 text-lg',
  };

  return (
    <button
      className={`inline-flex items-center gap-2 rounded-md font-medium transition-colors
        ${variantClasses[variant]} ${sizeClasses[size]}
        ${disabled || isLoading ? 'opacity-50 cursor-not-allowed' : ''}`}
      disabled={disabled || isLoading}
      onClick={onClick}
    >
      {isLoading && <span className="animate-spin h-4 w-4 border-2 border-current rounded-full border-t-transparent" />}
      {!isLoading && leftIcon}
      {children}
      {rightIcon}
    </button>
  );
};

// All these use ONE component — no inheritance needed:
<Button>Save</Button>
<Button variant="danger">Delete</Button>
<Button isLoading>Saving...</Button>
<Button leftIcon={<PlusIcon />}>Add Item</Button>
<Button variant="secondary" leftIcon={<DownloadIcon />} size="sm">Export</Button>
```

## Composition for Specialisation

When you need a specialised component, wrap the general one:

```tsx
// SubmitButton is not a subclass — it is a specialised wrapper
const SubmitButton = ({ isLoading, children }: { isLoading: boolean; children: React.ReactNode }) => (
  <Button type="submit" variant="primary" isLoading={isLoading}>
    {children}
  </Button>
);

// DeleteButton specialises via composition
const DeleteButton = ({ onClick }: { onClick: () => void }) => (
  <Button variant="danger" leftIcon={<TrashIcon />} onClick={onClick}>
    Delete
  </Button>
);
```

Each specialisation is a thin composition layer. No inheritance, no coupling, no fragile base class.

## Common Mistakes

**Extending React.Component to add behaviour.** Class inheritance was sometimes used to add shared methods. Custom hooks and composition replace this cleanly.

**Building "utility" base components that are never rendered alone.** If a `BaseCard` is only ever subclassed and never used directly, the design is inheritance-shaped. Refactor to a composable `Card` that accepts slot props.

**Treating composition and inheritance as interchangeable.** They are not just different implementations — they model different relationships. Composition is "has-a"; inheritance is "is-a". In UI, almost every relationship is "has-a".

## Mini Summary

React recommends composition over inheritance because composition is more flexible, avoids the fragile base class problem, and scales to any combination of features without class hierarchy complexity. Components are combined via props, children, and slots — not extended via class hierarchies. Specialised components are thin wrappers around general ones.

# Guided Practice Quest

Work through the steps to identify composition opportunities and refactor an inheritance-based design into a composition-based one.

# Solo Practice Quest

Imagine you need three variants of a notification component: `InfoNotification`, `SuccessNotification`, and `ErrorNotification`. Describe two approaches:

1. Using class inheritance
2. Using composition

Explain why the composition approach is preferable. Write 4–6 sentences.

# Integration

**Philosophy — Atomism:** Democritus proposed that matter is composed of indivisible atoms combined in different arrangements — rather than a hierarchy of elements. React's composition model mirrors this: atomic, independent components combine freely into any structure. Inheritance, by contrast, is hierarchical essentialism — the idea that complex things are defined by their relationship to a base essence.

**Design — Design Tokens and Primitives:** Modern design systems build from primitives (tokens, base components) that compose upward. A design system never says "a ProductCard inherits from Card." It says "ProductCard is composed of Card, Badge, Image, and Button." This compositional thinking is native to both design and React — reinforcing each other.

# Lore Conclusion

The doctrine of Composition ended the inheritance wars in the Academy's workshops. Now, artificers combine lightning runes, frost runes, and fire runes freely onto any weapon, in any order, in any combination. No hierarchy. No fragile chains. Just independent, well-crafted runes assembled with skill. Build your components the same way: independent, focused, composable. The most complex UI in the realm can be assembled from the simplest well-designed parts.

---
