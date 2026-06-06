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
