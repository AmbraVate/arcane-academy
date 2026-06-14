---
id: fe-jun-m6-10
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
lesson: children_and_slots
title: "Children and Slots"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m6-09]
integrationDomains: [design, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what the children prop does and how it enables composition"
    - "Describes at least one real use-case for named slots (e.g. header, footer, action)"
    - "Compares the flexibility of children/slots vs a single content prop"
  keywords: [children, slot, compose, flexible, layout, render, content]
  modelAnswer: |
    The children prop allows any JSX to be passed into a component, making the component a flexible container rather than a closed box. Named slots (separate named props that accept ReactNode) extend this to multiple insertion points — for example a Modal with header, body, and footer slots. This gives callers far more flexibility than a single content string prop, because they can pass complex JSX, other components, or dynamic content into each slot.
guidedSteps:
  - id: fe-jun-m6-10-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What does the children prop allow a component caller to do?"
    inputConfig:
      options:
        - "Access the component's internal state"
        - "Pass arbitrary JSX content to be rendered inside the component"
        - "Override the component's CSS styles"
        - "Replace the component's event handlers"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Pass arbitrary JSX content to be rendered inside the component"]
      rejectedFeedback: "children is the slot for arbitrary JSX content. The caller decides what goes inside; the component decides where it appears and how it is styled."
    hint: "Think about how HTML nesting works: <div><p>Hello</p></div>. The div doesn't know what its children will be."
    reflectionPrompt: "What would you lose if a Card component only accepted a content: string prop instead of children?"
  - id: fe-jun-m6-10-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Describe a component that would benefit from named slots (multiple named content props like header, body, footer)."
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [modal, dialog, card, layout, header, footer, panel, section]
      rejectedFeedback: "Good examples: Modal (header, body, footer), PageLayout (sidebar, content, navigation), Card (title, body, actions). Any component with multiple distinct content areas benefits from named slots."
    hint: "What UI component has multiple clearly distinct content areas that callers need to fill independently?"
    reflectionPrompt: "How do named slots make a layout component more reusable across different pages?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A Panel component accepts header, children, and footer as props (all ReactNode). What pattern is this?"
    options:
      - "Render props"
      - "Higher-Order Component"
      - "Named slots"
      - "Context API"
    correctIndex: 2
    feedback: "Named slots provide multiple distinct insertion points in a component. header, children, and footer are each a named slot accepting arbitrary JSX."
retrieval:
  recall: "What is the difference between using children and using named slot props?"
  explain: "Why is a Card that accepts children more reusable than one that accepts a content: string prop?"
  mistakeId:
    code: |
      interface ModalProps {
        title: string;
        body: string;
        footerText: string;
      }

      const Modal = ({ title, body, footerText }: ModalProps) => (
        <div className="modal">
          <h2>{title}</h2>
          <p>{body}</p>
          <div>{footerText}</div>
        </div>
      );
    answer: "Accepting string props for body and footer prevents callers from putting complex JSX (forms, lists, buttons) in those areas. Use ReactNode: body: ReactNode, footer: ReactNode — this accepts strings, JSX, and any other React content."
---

# Hook

You need a `Card` component that sometimes contains a paragraph, sometimes a form, sometimes a data table. With a `content: string` prop, this is impossible. But with the `children` prop, the card becomes a flexible container that can hold anything. The children prop is one of React's most powerful composition tools — and it is used in every major component library.

# Lore Introduction

The Academy's master carpenters build the frames. Into those frames — doorways, window apertures, bookshelves — other artisans insert their own work. A carpenter who insists on filling every frame themselves limits the Academy's creativity. The best carpenters build frames that *invite* content, then step back.

The `children` prop is how you build frames in React.

# Core Learning

## Concept Introduction

React's **children prop** allows callers to pass JSX content into a component. The component defines *where* that content appears and how it is styled; the caller defines *what* that content is.

```tsx
interface CardProps {
  children: React.ReactNode;
  className?: string;
}

const Card = ({ children, className = '' }: CardProps) => (
  <div className={`rounded-lg border bg-white shadow-sm p-6 ${className}`}>
    {children}
  </div>
);

// Caller fills the frame however they need
<Card>
  <h3 className="font-bold">Order #1234</h3>
  <p className="text-gray-600">3 items · £49.99</p>
  <button className="mt-4 btn-primary">View Details</button>
</Card>
```

The `Card` component provides structure and styling. It is completely decoupled from what goes inside it.

## Named Slots

For components with multiple distinct content areas, you can create **named slots** — separate props that each accept `ReactNode`:

```tsx
interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: React.ReactNode;
  children: React.ReactNode;
  footer?: React.ReactNode;
}

const Modal = ({ isOpen, onClose, title, children, footer }: ModalProps) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center">
      <div className="bg-white rounded-lg max-w-lg w-full mx-4">
        {/* Header slot */}
        <div className="flex justify-between items-center p-6 border-b">
          <div className="text-lg font-semibold">{title}</div>
          <button onClick={onClose}>✕</button>
        </div>
        {/* Body slot */}
        <div className="p-6">{children}</div>
        {/* Footer slot — optional */}
        {footer && <div className="p-6 border-t bg-gray-50">{footer}</div>}
      </div>
    </div>
  );
};

// Usage
<Modal
  isOpen={isOpen}
  onClose={handleClose}
  title="Confirm Deletion"
  footer={
    <div className="flex gap-2 justify-end">
      <Button variant="ghost" onClick={handleClose}>Cancel</Button>
      <Button variant="danger" onClick={handleDelete}>Delete</Button>
    </div>
  }
>
  <p>Are you sure you want to delete this record? This action cannot be undone.</p>
</Modal>
```

The caller has full control over the title, body, and footer — including passing interactive components into any slot.

## Render Props (Brief Introduction)

A more advanced pattern is **render props**: a prop that is a function returning JSX. This gives the caller access to the component's internal state:

```tsx
interface ListProps<T> {
  items: T[];
  renderItem: (item: T) => React.ReactNode;
}

const List = <T,>({ items, renderItem }: ListProps<T>) => (
  <ul className="space-y-2">
    {items.map((item, i) => (
      <li key={i}>{renderItem(item)}</li>
    ))}
  </ul>
);

// Caller controls how each item renders
<List
  items={products}
  renderItem={(product) => (
    <div className="flex justify-between">
      <span>{product.name}</span>
      <span className="font-bold">{product.price}</span>
    </div>
  )}
/>
```

Render props are powerful for sharing behaviour while delegating rendering — though custom hooks often solve the same problem more cleanly.

## Common Mistakes

**Accepting `children` as `string` or `JSX.Element`.** Use `React.ReactNode` — it accepts strings, numbers, JSX, null, arrays, and fragments. `JSX.Element` excludes strings and null.

**Not rendering children at all.** A common mistake is forgetting to include `{children}` in the JSX. TypeScript won't catch this — the component compiles but the content silently disappears.

**Nesting too deeply with the children pattern.** If you need five levels of composition just to place content, the component tree has become confusing. Named slots are clearer when there are 3+ distinct content areas.

## Mini Summary

The `children` prop makes a component a flexible container for arbitrary JSX. Named slots extend this to multiple content areas via `ReactNode` props. Render props delegate rendering to the caller while sharing behaviour. Together, these patterns enable powerful composition without inheritance.

# Guided Practice Quest

Work through the steps to identify where children and named slots add flexibility to component design.

# Solo Practice Quest

Design a `PageLayout` component with named slots. Describe:

1. The slots it should have (e.g. sidebar, header, content, footer)
2. Which slots should be optional and why
3. How the layout component positions each slot (no need for real CSS — describe it)
4. An example of two different pages using the same layout with different content in each slot

Write 5–8 sentences or use pseudo-code.

# Integration

**Design — Whitespace and Containers:** Designers think in terms of containers and content. A card is a container; what goes in it varies. A modal is a container; its purpose varies. The children/slots pattern directly mirrors this design thinking — the component encodes the container's structure and styling, while the caller provides the content, just as a page layout encodes the grid and a designer populates it with content.

**Philosophy — Open/Closed Principle:** The Open/Closed Principle says software should be open for extension but closed for modification. A Card with a children prop is open for extension (callers can put anything inside) but closed for modification (the card's structure never needs to change to accommodate new content). Named slots are OCP applied to UI layout components.

# Lore Conclusion

The Academy's most beautiful halls were built by carpenters who understood frames. Each arch, window, and bookshelf was a carefully designed opening, ready to receive whatever the scholars, artists, and artificers chose to place within it. Build your components as frames — sturdy, well-designed, open to whatever content the caller brings. The frame provides structure; the content provides meaning.

---
