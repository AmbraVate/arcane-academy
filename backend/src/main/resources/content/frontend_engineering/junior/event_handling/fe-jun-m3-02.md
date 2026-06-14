---
id: fe-jun-m3-02
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m3
moduleTitle: "Module 3: Events and Forms"
moduleGlyph: "📝"
moduleSortOrder: 3
topicSlug: event_handling
topicTitle: "Event Handling"
topicSortOrder: 1
lesson: event_handlers
title: "Event Handlers"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Passes additional arguments to event handlers using arrow functions"
    - "Understands event.target and event.currentTarget differences"
    - "Correctly uses event delegation (handling events on a parent)"
    - "Stops event propagation when appropriate"
  keywords: [event-handler, target, currentTarget, delegation, stopPropagation, bubbling, arrow-function, argument]
  modelAnswer: |
    Event handlers receive the event object as their first argument. event.target is the
    element that triggered the event; event.currentTarget is the element the handler is
    attached to. Events bubble up the DOM — a click on a child fires on parents too.
    event.stopPropagation() prevents bubbling. Pass extra arguments via an arrow function:
    onClick={() => handleDelete(item.id)}.
guidedSteps:
  - id: fe-jun-m3-02-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      How do you pass `item.id` to a delete handler when the user clicks a button in a list?
    inputConfig:
      options:
        - "onClick={handleDelete(item.id)} — call the function with the argument"
        - "onClick={() => handleDelete(item.id)} — wrap in arrow function"
        - "data-id={item.id} only — read from event.target.dataset"
        - "onDeleteId={item.id} as a separate prop"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["onClick={() => handleDelete(item.id)} — wrap in arrow function"]
      rejectedFeedback: "Arrow function wrapping creates a closure over item.id. onClick={handleDelete(item.id)} calls the function immediately at render — a very common bug. The arrow function defers the call until click time."
    hint: "The arrow function defers execution until the click happens."
    reflectionPrompt: "This pattern appears constantly: () => fn(arg). The arrow function is a closure — it captures item.id at render time and calls fn(item.id) when the event fires."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A click on a button inside a div fires on: (assuming no stopPropagation)"
    options:
      - "Only the button"
      - "The button, then the div, then any parent with a click handler"
      - "Only the topmost element with an onClick"
      - "All elements on the page"
    correctIndex: 1
    feedback: "DOM events bubble from the target element up through its ancestors. A click on the button fires button.onClick, then div.onClick, then parent.onClick. This is event bubbling — the foundation of event delegation."

retrieval:
  recall: "Write a list item delete button that passes the item's id to handleDelete when clicked."
  explain: "Explain the difference between event.target and event.currentTarget."
  mistakeId:
    code: "onClick={handleDelete(item.id)} in a list of 100 items"
    answer: "handleDelete is called 100 times at render time. Wrap it: onClick={() => handleDelete(item.id)} to defer until click."
---

# Hook

Event handlers are the bridge between user action and application response. Passing the right data to the right handler — without calling it too early — is a skill with a few sharp edges.

# Lore Introduction

*"A trap that springs when set,"* says Master Aelindra, *"is not a trap. It is an accident. A trap that springs only when triggered — that is intentional engineering."*

# Core Learning

## Concept Introduction

```jsx
// Passing arguments via arrow function closure
{items.map(item => (
  <li key={item.id}>
    {item.name}
    <button onClick={() => handleDelete(item.id)}>Delete</button>
  </li>
))}

// event.target vs event.currentTarget
<ul onClick={handleListClick}>          {/* currentTarget = ul */}
  <li>Item 1</li>                       {/* target = li (the clicked element) */}
</ul>

function handleListClick(e) {
  console.log(e.target);         // the element clicked (li, button, etc.)
  console.log(e.currentTarget);  // the element with the handler (ul)
}

// Stop bubbling when needed
<button onClick={e => { e.stopPropagation(); handleClick(); }}>Click</button>
```

**Event delegation:** Attach one handler to a parent to handle events from all children. Efficient for large lists — one handler, not N handlers.

## Common Mistakes

- **Calling the handler with arguments directly**: `onClick={handleDelete(item.id)}` calls the function during render, not on click. Use an arrow function wrapper: `onClick={() => handleDelete(item.id)}`.
- **Confusing `event.target` with `event.currentTarget`**: `target` is the element the user actually clicked (could be a child); `currentTarget` is the element the handler is attached to. Using `target` when you need `currentTarget` causes bugs when clicking child elements inside a delegated handler.
- **Calling `stopPropagation` when `preventDefault` is needed**: `stopPropagation` stops the event from bubbling up the component tree; `preventDefault` cancels the browser's default action (e.g. form submission, link navigation). They solve different problems.
- **Assuming event.target is always the element you expect**: In a list with nested markup inside each `<li>`, clicking a child `<span>` sets `event.target` to the span, not the `<li>`. Check `event.target.closest('li')` when you need the nearest ancestor of a specific type.

## Mental Model

An event handler is a recipe card filed under a trigger, and the craft is in what you write on the card versus what you hand the kitchen. The card has a title that says *when* ("on submit", "on change of the email field") and a body that says *what* — and keeping that body coherent is the whole game. Three card-writing rules carry the lesson. First, hand over the card, don't cook the dish at filing time: `onClick={handleSave}` files the recipe; `onClick={handleSave()}` cooks it immediately while the restaurant is still being set up (render), serving food nobody ordered. When the recipe needs ingredients chosen per table — this row's `id` — you wrap it in a fresh card per table: `onClick={() => handleDelete(id)}`, a sealed instruction that says "when called, cook *this* dish for *this* table". Second, the kitchen always sends the card back with a delivery docket attached — the event object — listing where the order came from (`event.target`), what was in it (`.value`, `.checked`), and a stamp you can apply to refuse the venue's default service (`preventDefault()`, essential when the venue's default — a full-page form submission — would demolish your single-page restaurant). Third, real recipes don't live scribbled in the margins of the menu: one-line instructions can sit inline in JSX, but anything with actual steps gets a named card (`handleSubmit`) filed above the return — readable, testable, reusable by both the button and the enter key. Trigger on the title, sealed cards for per-item arguments, read the docket, file real recipes by name: handler craft in four moves.

## Why It Matters

Writing event handlers well is the difference between interactivity that scales and a component that collapses under its own callbacks — this is the daily-bread skill of frontend work:

- Handlers are where state meets the user: nearly every `setState` call in a real app lives inside one, so handler structure *is* application logic structure — a component's quality is often readable directly from how its handlers are organised
- Passing arguments correctly is a daily requirement with a classic trap: list rows need `onClick={() => deleteItem(id)}` — the arrow creating a sealed instruction per row — and the wrong form (`onClick={deleteItem(id)}`) deletes every item during render, a bug every junior ships exactly once
- The event object is a working tool, not trivia: `event.target.value` powers every input handler, `event.preventDefault()` is mandatory for form submits in an SPA, and knowing what's on the object turns "how do I get the checkbox state?" from a search into a reflex
- Extraction discipline keeps components legible: trivial one-liners can stay inline, but logic-bearing handlers pulled into named functions (`handleSubmit`, `handleQuantityChange`) give you testability, reuse across enter-key-and-click paths, and JSX that reads like an outline instead of a script

Forms, lists, modals, drag interactions — all of them are compositions of well-shaped handlers. Sloppy handler habits compound into unreadable components faster than almost any other vice in React.

## Mini Summary
- ✔ Pass arguments via arrow: () => fn(arg)
- ✔ event.target = clicked element; event.currentTarget = handler element
- ✔ Events bubble up — parents catch child events
- ✔ event.stopPropagation() prevents bubbling

# Solo Practice Quest

Build a task list where clicking any task item marks it done. Use event delegation: one onClick on the `<ul>`, read `event.target.dataset.id` to identify which item was clicked.

# Integration

**Mathematics — Graph Traversal:** Event bubbling is a depth-first traversal of the DOM tree from the target node up to the document root. stopPropagation() is a traversal termination condition. Event delegation leverages this traversal to catch events at a higher node.

# Lore Conclusion

*"The event travels upward through its ancestors. Intercept it at the right level — no sooner, no later."*

---
