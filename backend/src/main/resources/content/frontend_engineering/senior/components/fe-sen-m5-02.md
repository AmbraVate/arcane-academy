---
id: fe-sen-m5-02
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m5
moduleTitle: "Module 5: Design Systems"
moduleGlyph: "🎨"
moduleSortOrder: 5
topicSlug: components
topicTitle: "Components"
topicSortOrder: 2
lesson: design_system_components
title: "Design System Components"
sortOrder: 1
difficulty: 4
estimatedMinutes: 30
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [design, psychology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Explains what makes a design system component different from an application component
    - Describes variant-based API design and its benefits
    - Explains the compound component pattern and when to use it
    - Explains why design system components must be accessibility-first
    - Synthesises the principles of a well-designed component API
  keywords: [variant, compound, accessibility, aria, polymorphic, forwardRef, className, escape hatch, generic, reusable, API]
  modelAnswer: |
    Design system components differ from application components in scope: they're domain-agnostic, highly configurable, and must work in many contexts across many teams. They're product-agnostic — a Button doesn't know about users or tasks; it knows about variants, sizes, and states.

    Variant-based APIs express semantic intent: variant='primary' | 'secondary' | 'danger'. Consumers declare what they want; the component decides how to achieve it. This encapsulates design decisions and makes design updates propagate automatically.

    Compound components (Tabs, Select, Accordion) use Context to share state between parent and child components: <Tabs>, <Tabs.List>, <Tabs.Panel>. Consumers compose them naturally while internal state management is hidden.

    Accessibility is non-negotiable in design system components: correct ARIA roles, keyboard navigation, focus management. A design system component with accessibility built in means every team that uses it gets accessibility for free. A component without it means every team must implement it separately — and most won't.

    A well-designed component API: accepts className for escape hatches, uses forwardRef for DOM ref access, is polymorphic where appropriate (a Button that renders as an anchor), exports TypeScript types, and documents all props.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A team needs a Button that sometimes renders as an anchor tag for navigation. Which pattern handles this cleanly?"
    options:
      - "Create two separate components: Button and LinkButton"
      - "Use a polymorphic 'as' prop: <Button as='a' href='/home'>Go home</Button>"
      - "Wrap an anchor inside a Button component"
      - "Use CSS to make a div look like an anchor"
    correctIndex: 1
    feedback: "The polymorphic 'as' prop pattern allows the component to render as any HTML element or React component while preserving its styling and behaviour. `<Button as='a' href='/home'>` renders an anchor with button styling. TypeScript inference ensures the correct props are available for the rendered element. This avoids duplicating the Button's API across two components."
  - type: SHORT_TEXT
    prompt: "A Tabs component in your design system needs to allow customisation of active tab styling without exposing internal implementation. How would you design this?"
    hint: "Think about data attributes, CSS custom properties, or className props for the active state."
  - type: FILL_BLANK
    prompt: "Compound components share ___ between parent and child components. The parent provides ___ via Context; children consume it."
    answer: "state; Context"
    hint: "How do Tabs.List and Tabs.Panel know which tab is active without props?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why should design system components accept a className prop?"
    options:
      - "className is required by React"
      - "It allows consumers to apply one-off style overrides without forking the component"
      - "className improves component performance"
      - "It's required for Tailwind to work"
    correctIndex: 1
    feedback: "Design system components serve many contexts. The 80% case is covered by variants and sizes. The 20% case needs a small override — extra margin, a specific width on this one page. The className prop provides an escape hatch: consumers can apply overrides without modifying the component or creating a fork. Without it, every edge case requires a new variant or a fork."
  - type: MULTIPLE_CHOICE
    question: "A Button component in a design system has no keyboard accessibility. What is the systemic impact?"
    options:
      - "Only buttons that use keyboard navigation are affected"
      - "Every product team that uses the Button ships inaccessible buttons — the design system multiplied the problem"
      - "The impact is limited to the design system's own demo page"
      - "Keyboard accessibility is the browser's responsibility"
    correctIndex: 1
    feedback: "Design system components are multiplied across every team that uses them. An accessible Button = every team gets accessibility for free. An inaccessible Button = every team ships an inaccessible button. The design system is the highest-leverage place to build in accessibility — fix it once, fix it everywhere."
retrieval:
  recall: "What is the compound component pattern? Give an example of a component that benefits from it."
  explain: "Why is accessibility non-negotiable in design system components, and what is the leverage argument for building it in?"
  mistakeId:
    code: |
      // Design system Button — accepts raw class strings for styling
      <Button 
        className="bg-blue-600 text-white px-4 py-2 hover:bg-blue-700"
        onClick={handleSubmit}
      >
        Submit
      </Button>
    answer: "The caller is writing raw Tailwind classes — the design system provides no abstraction. This is not a design system component; it's just a div wrapper. Every caller must know which Tailwind classes implement a 'submit' button. When the design changes, every caller must update their class strings. A proper design system component: <Button variant='primary' onClick={handleSubmit}>Submit</Button> — the variant encapsulates the styling decision, the component owns it, and callers use semantic intent."
---

# Hook

Your design system has a Button component. Over the last year, six teams forked it slightly differently. Now there are seven versions of the Button with subtly different styling, behaviour, and accessibility. Updating the brand requires updating seven forks.

A design system component's value is its adoption. A component that's forked isn't adopted — it's copied.

# Lore Introduction

*"The Academy's standard door design must serve every room in the building,"* the Chief Architect explains. *"The lecture hall needs a wide door. The archive needs a narrow one. The grand entrance needs double doors. One design — many configurations."*

She unfurls the component spec. *"Your design system components must be this configurable. Flexible enough to serve every use case. Consistent enough to be recognisably the same component."*

# Core Learning

## Concept Introduction

**Design system components differ from application components:**
- **Domain-agnostic:** no knowledge of users, products, or business logic
- **Highly configurable:** variant, size, state props
- **Accessibility-built-in:** ARIA, keyboard navigation, focus management
- **Escape hatches:** className, style, forwardRef, polymorphic as prop

**Variant-based API:**
```tsx
interface ButtonProps {
  variant?: 'primary' | 'secondary' | 'ghost' | 'danger';
  size?: 'sm' | 'md' | 'lg';
  isLoading?: boolean;
  isDisabled?: boolean;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  className?: string; // escape hatch
  children: React.ReactNode;
}
```

**Compound component pattern (Tabs):**
```tsx
// Parent provides context
const TabsContext = createContext<TabsContextValue>(null);

function Tabs({ defaultTab, children }) {
  const [activeTab, setActiveTab] = useState(defaultTab);
  return (
    <TabsContext.Provider value={{ activeTab, setActiveTab }}>
      {children}
    </TabsContext.Provider>
  );
}

// Children consume context
Tabs.List = function TabsList({ children }) { ... };
Tabs.Panel = function TabsPanel({ id, children }) {
  const { activeTab } = useContext(TabsContext);
  return activeTab === id ? children : null;
};

// Usage
<Tabs defaultTab="overview">
  <Tabs.List>
    <Tabs.Tab id="overview">Overview</Tabs.Tab>
    <Tabs.Tab id="details">Details</Tabs.Tab>
  </Tabs.List>
  <Tabs.Panel id="overview">...</Tabs.Panel>
  <Tabs.Panel id="details">...</Tabs.Panel>
</Tabs>
```

**Accessibility built-in:**
```tsx
function Button({ ...props }) {
  return (
    <button
      role="button"
      aria-disabled={props.isDisabled}
      aria-busy={props.isLoading}
      tabIndex={props.isDisabled ? -1 : 0}
      {...props}
    />
  );
}
```

## Common Mistakes

- **No escape hatch.** Rejecting className means consumers fork the component for edge cases.
- **Too many variants.** Every new variant increases the design system's maintenance surface. Add variants when there's clear recurring need, not for one-off cases.
- **Missing forwardRef.** Parent components need DOM access (focus, scroll) — forwardRef exposes the underlying element.
- **Accessibility as afterthought.** ARIA roles and keyboard handling belong in the initial component, not added later.

## Mini Summary

- ✔ Design system components: domain-agnostic, configurable, accessible, with escape hatches
- ✔ Variant APIs encapsulate design decisions — consumers declare intent, not implementation
- ✔ Compound components use Context to share state between parent and children
- ✔ Accessibility is built in — the leverage of fixing it once for all consumers
- ✔ Accept className for escape hatches; use forwardRef for DOM access

# Guided Practice Quest

Work through the guided steps to understand polymorphic components and the compound component pattern.

# Solo Practice Quest

Design the API for a `Select` dropdown design system component. It needs: single and multi-select modes, grouped options, search/filter, disabled options, and keyboard navigation. Write the TypeScript interface for the props. Don't implement — just design the API and explain each design decision.

# Integration

**Psychology — Affordances and Component APIs**

Don Norman's concept of affordances describes how objects communicate how they should be used. A well-designed door handle affords pushing or pulling without a sign. A well-designed component API affords correct use without documentation. `<Button variant='danger' isLoading>Deleting...</Button>` affords its own purpose — no documentation needed to understand it. A poor API (passing raw Tailwind classes) doesn't afford correct use — consumers must know the implementation details to use it correctly. Designing component APIs with psychological affordances in mind — semantic names, constrained options, good defaults — makes the design system self-documenting.

# Lore Conclusion

*"The door design is complete,"* the Chief Architect says. *"Wide or narrow, single or double — the same door, different configuration. Every room in the Academy uses it. Every room benefits from the accessibility built in at design time."*

---
