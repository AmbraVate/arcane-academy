---
id: fe-jun-m1-11
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
lesson: prop_types
title: "Prop Types"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-10]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains why type checking props is valuable"
    - "Uses PropTypes correctly to define expected types"
    - "Distinguishes required vs optional props"
    - "Understands TypeScript as an alternative to PropTypes"
  keywords: [PropTypes, TypeScript, type checking, required, optional, string, number, shape, arrayOf]
  modelAnswer: |
    PropTypes provide runtime type checking for React props, producing console warnings when wrong types are passed. They act as documentation and catch common bugs. TypeScript provides compile-time type checking as a stronger alternative. Both approaches make component APIs explicit and safer to use.
guidedSteps:
  - id: fe-jun-m1-11-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "When does PropTypes validation run?"
    inputConfig:
      options:
        - "At compile time, preventing incorrect builds"
        - "At runtime in development, logging console warnings"
        - "Only in production"
        - "When the component is tested"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["At runtime in development, logging console warnings"]
      rejectedFeedback: "PropTypes runs at runtime during development. It checks prop types when the component renders and logs warnings — not errors — if types are wrong. TypeScript does compile-time checking."
    hint: "PropTypes is a runtime library, not a build tool."
    reflectionPrompt: "PropTypes' limitations (runtime only, no build-time safety) are why TypeScript has become the preferred approach in professional teams."
  - id: fe-jun-m1-11-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write PropTypes for a UserCard component that requires a string name, optional number age, and required boolean isActive."
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [PropTypes, string, number, bool, isRequired, name, age, isActive]
      rejectedFeedback: "Example: `UserCard.propTypes = { name: PropTypes.string.isRequired, age: PropTypes.number, isActive: PropTypes.bool.isRequired };`"
    hint: "Add `.isRequired` for required props; omit it for optional ones."
    reflectionPrompt: "Explicit prop types are documentation. Even without PropTypes, naming conventions (is, has, on) communicate a prop's intent."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the advantage of TypeScript over PropTypes for prop type checking?"
    options:
      - "TypeScript is faster at runtime"
      - "TypeScript provides compile-time checking, catching errors before the app runs"
      - "TypeScript is easier to learn"
      - "TypeScript replaces PropTypes exactly"
    correctIndex: 1
    feedback: "TypeScript catches type errors at build time — before you even run the app. PropTypes only catches them at runtime in development. TypeScript also provides IDE autocomplete and refactoring support. Most professional React teams use TypeScript."
retrieval:
  recall: "What is the difference between PropTypes.string and PropTypes.string.isRequired?"
  explain: "Why might a team choose TypeScript over PropTypes?"
  mistakeId:
    code: "Skipping prop type definitions because it's more work"
    answer: "Skipping type definitions trades a small upfront cost for larger downstream costs: bugs from wrong types, no IDE autocomplete, difficulty understanding component APIs. In professional code, type safety is a standard practice."
---

# Hook

You build a beautiful `Button` component. Two weeks later, a colleague passes a number where you expected a string — and the button renders `[object Object]`. No error. Just silent wrongness. Prop types exist to catch this class of bug before it reaches users.

# Lore Introduction

*"A formula without constraints,"* Aelindra says, *"accepts any ingredient — even poison. Type annotations are the formula's immune system: they reject what doesn't belong and name what does."*

# Core Learning

## Concept Introduction

**PropTypes** is a runtime type-checking library. **TypeScript** is a compile-time type checker. Both serve the same purpose: make component APIs explicit and safe.

```jsx
// PropTypes approach
import PropTypes from 'prop-types';

function Avatar({ username, size, isOnline }) {
  return <img width={size} alt={username} />;
}

Avatar.propTypes = {
  username: PropTypes.string.isRequired,
  size: PropTypes.number,
  isOnline: PropTypes.bool
};

Avatar.defaultProps = {
  size: 40,
  isOnline: false
};
```

```tsx
// TypeScript approach (preferred in modern projects)
interface AvatarProps {
  username: string;
  size?: number;
  isOnline?: boolean;
}

function Avatar({ username, size = 40, isOnline = false }: AvatarProps) {
  return <img width={size} alt={username} />;
}
```

| Feature | PropTypes | TypeScript |
|---|---|---|
| When it checks | Runtime (dev only) | Compile time |
| IDE support | Minimal | Full autocomplete |
| Learning curve | Low | Medium |
| Industry standard | Legacy | Preferred |

## Why It Matters

Type checking is documentation that runs. It tells colleagues (and future you) what a component expects — and enforces it automatically.

## Worked Example

```tsx
interface ArticleCardProps {
  title: string;
  excerpt: string;
  author: { name: string; avatarUrl: string };
  publishedAt: Date;
  tags: string[];
  onBookmark: () => void;
}
```

Clear contract. No guessing. IDE will autocomplete all of these.

## Common Mistakes

- **Relying only on PropTypes in a TypeScript project.** Use TypeScript interfaces — PropTypes are redundant.
- **Making all props required.** Use optional props with sensible defaults for better DX.
- **Using PropTypes.object or PropTypes.array.** Too broad. Use `PropTypes.shape({...})` and `PropTypes.arrayOf(...)`.

## Mini Summary

- PropTypes: runtime warnings in development (legacy approach)
- TypeScript interfaces: compile-time safety (modern standard)
- Mark required vs optional explicitly
- Type definitions are documentation that runs

# Guided Practice Quest

Work through the guided steps on defining and interpreting prop types.

# Solo Practice Quest

Design the TypeScript interface for a `DataTable` component that shows rows of data. Define props for: an array of row objects (each with id, label, value), a title string, an optional onRowClick callback, a loading boolean, and an optional error message. Then write 2–3 sentences explaining how TypeScript's interface gives you better guarantees than PropTypes.

# Integration

**Mathematics — Type Theory**

Type systems in programming languages are rooted in formal type theory — a branch of mathematical logic. Types are sets: `string` is the set of all possible strings, `number` is the set of all numbers. Type checking verifies that function arguments belong to the expected sets. TypeScript's type system is a structural type system — compatibility is determined by shape, not name. Understanding this makes TypeScript's more advanced features (generics, union types, mapped types) intuitive rather than magical.

# Lore Conclusion

*"Label your ingredients,"* Aelindra says. *"The formula that accepts anything produces anything — including disaster. The well-typed formula accepts exactly what it needs, and produces exactly what it promises."*

---
