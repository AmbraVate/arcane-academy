---
id: fe-jun-m1-13
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m1
moduleTitle: "Module 1: React Foundations"
moduleGlyph: "⚛️"
moduleSortOrder: 1
topicSlug: component_composition
topicTitle: "Component Composition"
topicSortOrder: 5
lesson: children_props
title: "Children Props"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-10]
integrationDomains: [design, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what the children prop is"
    - "Uses children to create wrapper/container components"
    - "Distinguishes children from explicit props"
    - "Understands when children is the right design"
  keywords: [children, prop, wrapper, container, slot, composition, JSX, nested]
  modelAnswer: |
    The children prop is a special React prop that contains any JSX nested inside a component's tags. It enables wrapper/container components — components that don't know their content in advance but provide a structural shell. This is the foundation of composition: building complex UIs by nesting components inside other components.
guidedSteps:
  - id: fe-jun-m1-13-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What does the `children` prop contain?"
    inputConfig:
      options:
        - "An array of all child component instances"
        - "The JSX elements nested between the component's opening and closing tags"
        - "A list of the component's sub-components"
        - "The component's props as a tree"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The JSX elements nested between the component's opening and closing tags"]
      rejectedFeedback: "`children` is whatever is placed between `<Component>` and `</Component>`. It can be text, JSX elements, other components, or a mix. The receiving component renders it with `{children}`."
    hint: "Think about `<Card>...</Card>` — what's in the `...`?"
    reflectionPrompt: "children is just a regular prop — it happens to be passed automatically from nested JSX rather than explicitly. You could pass the same thing as an explicit prop: `content={<p>Hello</p>}`. But children is the idiomatic way."
  - id: fe-jun-m1-13-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write a `Card` wrapper component that renders its children inside a styled div."
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: [children, Card, return, div]
      rejectedFeedback: "Example: `function Card({ children }) { return <div className=\"card\">{children}</div>; }` — receive children as a prop, render it inside the wrapper."
    hint: "Destructure children from props and render it inside the container."
    reflectionPrompt: "Wrapper components are one of the most useful patterns in React. They apply consistent structure, styles, or behaviour to whatever content is placed inside them."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "When is the `children` prop the right design choice?"
    options:
      - "When the component always renders the same content"
      - "When the component provides structure/behaviour but doesn't know its content in advance"
      - "When you want to avoid passing explicit props"
      - "When a component has more than three props"
    correctIndex: 1
    feedback: "children is perfect for wrapper/container components: Modal, Card, Section, Panel, Layout. They provide shell/behaviour without dictating content. If the content is always the same, a concrete component (not a wrapper) is better."
retrieval:
  recall: "How is the children prop passed compared to regular props?"
  explain: "Name three real-world components where `children` is the right API design."
  mistakeId:
    code: "Avoiding children by always passing content as an explicit `content` prop"
    answer: "While `content` as an explicit prop works for simple text, using children is idiomatic React for JSX content. It enables natural composition syntax: `<Modal><Form /></Modal>` reads more naturally than `<Modal content={<Form />} />`."
---

# Hook

What if a component doesn't know what content it will render — only where to put it? A modal knows it needs a backdrop, a close button, and a content area — but not what goes in the content area. The `children` prop solves this: it's a slot for content the parent defines.

# Lore Introduction

*"Some moulds,"* Aelindra says, *"have a chamber at the centre. The outer form is fixed — the inner content is supplied by whoever pours into it. This is composition. The shell and the filling are separate concerns."*

# Core Learning

## Concept Introduction

`children` is a special prop automatically populated with JSX nested inside a component's tags:

```jsx
// The wrapper component
function Panel({ title, children }) {
  return (
    <section className="panel">
      <h2 className="panel-title">{title}</h2>
      <div className="panel-body">{children}</div>
    </section>
  );
}

// Usage — content goes between the tags
<Panel title="User Settings">
  <Form />
  <p>Changes are saved automatically.</p>
</Panel>
```

The `Panel` provides structure; the caller provides content. Neither knows the internals of the other.

## Why It Matters

Children enable **composition** — building complex UIs by combining simple pieces. This is how design systems build Layout, Modal, Card, and Page components: they define the shell; consumers fill the content.

## Worked Example

```jsx
function Modal({ isOpen, onClose, title, children }) {
  if (!isOpen) return null;
  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <header>
          <h3>{title}</h3>
          <button onClick={onClose}>×</button>
        </header>
        <div className="modal-body">{children}</div>
      </div>
    </div>
  );
}

// Used with completely different content each time
<Modal isOpen={showDelete} onClose={close} title="Confirm Delete">
  <p>Are you sure? This cannot be undone.</p>
  <Button onClick={deleteItem}>Delete</Button>
</Modal>
```

## Common Mistakes

- **Rendering children when it might be undefined.** Add a guard if children is optional.
- **Deeply nested children for structural layouts.** Consider named slot props (`header`, `footer`) for complex layouts.
- **Overusing children for simple text.** A `label` prop is clearer for a single string.

## Mental Model

The `children` prop turns a component from a finished product into a *picture frame*. A regular prop-driven component is like a printed poster — the parent chooses among the variants the designer anticipated (`title="..."`, `variant="dark"`). A frame makes no claim about its contents: it provides the border, the matting, the mounting hardware (the consistent styling, padding, behaviour), and whatever the owner places inside — a photo, a child's drawing, a mirror — it presents beautifully. Writing `<Card>` ... anything ... `</Card>` is placing content into the frame; inside the Card component, `{children}` marks the opening where the contents show through. The mental unlock is realising which problems are frame problems: Modal, Card, Layout, Sidebar — components whose job is *presentation around unknown content* — should almost never enumerate what they might contain via props. The frame-maker doesn't ask you what photo you'll insert; that's precisely what makes one frame design serve a thousand different walls. When you find yourself adding a fourth content prop (`title`, `subtitle`, `body`, `footer`...) to a container, stop: you're carving slots into a poster when you should be building a frame.

## Mini Summary

- `children` is auto-populated from JSX nested inside a component
- Use it for wrapper/container/shell components
- It enables composition — structure and content as separate concerns
- Idiomatic React prefers children over explicit JSX prop for content

# Guided Practice Quest

Work through the guided steps on using the children prop.

# Solo Practice Quest

Build a `Section` component (title, children), a `CalloutBox` component (type: info/warning/danger, children), and a `StackLayout` component (gap, children). Show example usage of each in a parent component. Write 2–3 sentences on how composing these three components together creates a more complex layout than any one of them alone.

# Integration

**Design — Slot-Based Design Systems**

Web Components have explicit named slots (`<slot name="header">`). React's children prop is the equivalent for function components. Both patterns are implementations of the same design concept: separation of structure and content. Design systems (Material, Carbon, Ant Design) use this pattern extensively — their layout components (Grid, Container, Paper) define structure; teams fill them with content. Understanding the pattern makes it easier to use any component library.

# Lore Conclusion

*"The shell does not dictate the pearl,"* Aelindra says. *"Nor does the pearl exist without the shell. Composition is the recognition that some concerns are naturally separate — and should be kept that way."*

---
