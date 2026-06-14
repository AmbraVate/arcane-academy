---
id: fe-jun-m3-03
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
lesson: synthetic_events
title: "Synthetic Events"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what a SyntheticEvent is and why React uses it"
    - "Knows that SyntheticEvents are pooled (in older React) and how to handle async access"
    - "Correctly reads event properties (target.value, target.checked)"
    - "Understands that nativeEvent gives access to the underlying DOM event"
  keywords: [SyntheticEvent, nativeEvent, target, value, checked, pool, async, cross-browser, wrapper]
  modelAnswer: |
    A SyntheticEvent is React's cross-browser wrapper around the native DOM event. It has
    the same API (target, preventDefault, stopPropagation) but normalises browser
    inconsistencies. Access the underlying event via event.nativeEvent. For async access
    (in modern React): SyntheticEvents are not pooled — but if using React 16 or below,
    call event.persist() before async operations to prevent recycling.
guidedSteps:
  - id: fe-jun-m3-03-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the value of `event.target.value` in an onChange handler for `<input type="text">`?
    inputConfig:
      options:
        - "The input element's placeholder text"
        - "The current string the user has typed in the input"
        - "The input's name attribute"
        - "A boolean indicating whether the input has changed"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The current string the user has typed in the input"]
      rejectedFeedback: "event.target is the input element. event.target.value is its current value — the string the user typed. For checkboxes, use event.target.checked (boolean). This distinction is critical for controlled inputs."
    hint: "event.target is the DOM element that was interacted with."
    reflectionPrompt: "event.target.value is the single most-used property in form handling. event.target.checked is used for checkboxes. event.target.name is useful when one handler handles multiple inputs."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "How do you handle multiple inputs with a single onChange handler?"
    options:
      - "Use separate handlers for each input"
      - "Read event.target.name to identify which input changed, then update that key in state"
      - "React automatically knows which input changed"
      - "Use a form library — this is too complex for native React"
    correctIndex: 1
    feedback: "With name attributes: function handleChange(e) { setForm(prev => ({ ...prev, [e.target.name]: e.target.value })); }. The computed property key [e.target.name] updates only the changed field."

retrieval:
  recall: "Write an onChange handler that reads event.target.value and updates a state variable."
  explain: "What is event.nativeEvent and when would you access it?"
  mistakeId:
    code: "Reading event.target.value inside a setTimeout after onChange fires"
    answer: "In React 17+, SyntheticEvents are not pooled and persist — but always extract the value synchronously and store it: const { value } = event.target; setTimeout(() => use(value), 1000)."
---

# Hook

The SyntheticEvent is the messenger between the browser's raw event system and your React handlers. Knowing its API — especially target.value and target.checked — is the foundation of all form handling.

# Lore Introduction

*"The Academy's translation office,"* says Master Aelindra, *"converts messages from any dialect into the Academy's standard language. You receive one consistent format regardless of where the message originated. SyntheticEvent is React's translation office."*

# Core Learning

## Concept Introduction

```jsx
// Reading values from SyntheticEvent
function handleChange(event) {
  const { name, value, type, checked } = event.target;

  // For text inputs:    use value
  // For checkboxes:     use checked (boolean)
  // For select:         use value

  setForm(prev => ({
    ...prev,
    [name]: type === 'checkbox' ? checked : value
  }));
}

// Accessing the native DOM event
function handleClick(event) {
  const nativeEvent = event.nativeEvent;  // underlying browser event
}

// Common SyntheticEvent properties
event.target          // DOM element that fired the event
event.currentTarget   // DOM element the handler is attached to
event.preventDefault()
event.stopPropagation()
event.type            // 'click', 'change', etc.
```

## Common Mistakes

- **Using `event.target.value` on a checkbox**: Checkboxes report their checked state via `event.target.checked` (a boolean), not `event.target.value`. Using `value` returns the string `"on"` regardless of whether the box is checked.
- **Trying to persist a SyntheticEvent for async use**: In older React versions, SyntheticEvents were pooled and nullified after the handler returned. In React 17+ pooling was removed, but it is still best practice to destructure the values you need (`const { value } = event.target`) at the start of the handler rather than referencing `event` inside a `setTimeout` or async callback.
- **Using `event.nativeEvent` instead of `event` for common properties**: The SyntheticEvent provides all standard properties (`target`, `preventDefault`, `stopPropagation`) — accessing `nativeEvent` is only needed for browser-specific APIs not exposed by the synthetic wrapper.
- **Not using `event.target.name` for multi-field forms**: A single `handleChange` handler can manage all fields if each input has a `name` attribute — reading `event.target.name` eliminates duplicate handlers.

## Mental Model

React's event system is a hotel switchboard, not a phone in every room. The naive picture — your `onClick` wired directly to that button's DOM node — is rooms with private lines. The real architecture: every incoming call (native browser event) rings once at the *switchboard* (React's single listener at the root), where an operator identifies which room was dialled (which component's handler should fire) and connects you — passing the call through a standardising adapter so every room hears the same dial tone and caller-ID format regardless of which phone company (browser) originated it. That adapter is the `SyntheticEvent`: a consistent wrapper around the native call, with the original always available on request (`e.nativeEvent` — asking the operator to patch you through raw). The switchboard model explains the behaviours that otherwise mystify. Efficiency: a ten-thousand-row list doesn't need ten thousand phone lines — one switchboard serves all, which is why React handles huge interactive surfaces cheaply. Ordering and propagation: calls climb the hotel's internal hierarchy (your component tree) under the operator's rules, so React's `stopPropagation` halts the *internal* routing — but a separate phone you personally bolted to the building's exterior wall (`addEventListener` on document) lives partly outside the operator's jurisdiction, which is exactly why mixing the two systems produces the classic "my modal's outside-click handler fired anyway" bug. When events behave strangely at the boundary between React and the raw DOM, stop imagining wires on doorknobs and picture the switchboard: who took the call, in which system, and in what order the connections were made.

## Why It Matters

Synthetic events are React's quiet abstraction layer — invisible while you stay on the happy path, and exactly what you need to understand the day you step off it:

- Every event object your handlers receive is a `SyntheticEvent`: React's wrapper that normalised browser inconsistencies (a real historical pain — event APIs genuinely differed across browsers) and guarantees the same shape and behaviour everywhere your app runs
- The architecture beneath is event delegation: React doesn't attach your thousand `onClick`s to a thousand DOM nodes — it listens once at the root and routes events to your handlers itself, which is why React apps stay fast with enormous interactive lists and why your handlers fire in React's controlled order
- The seams are where the knowledge pays: `e.nativeEvent` when you need the raw browser event, the rules for mixing React handlers with manual `addEventListener` (modals, global key shortcuts, third-party widgets), and why `stopPropagation` in one world doesn't always silence the other
- Interview and debugging relevance is real: "why does my document-level listener fire before/after my React handler?" and "why did my event object behave oddly in async code?" (the now-removed pooling behaviour, still living in older codebases and blog posts) are questions answered entirely by this layer

You can write React for months without thinking about synthetic events — then lose a day to a propagation bug at the React/DOM boundary. This lesson is the inoculation.

## Mini Summary
- ✔ SyntheticEvent wraps native events for cross-browser consistency
- ✔ event.target.value for text inputs; event.target.checked for checkboxes
- ✔ event.target.name enables one handler for multiple inputs
- ✔ event.nativeEvent for raw browser event access

# Solo Practice Quest

Write a single onChange handler for a form with: name (text), email (email), newsletter (checkbox), and country (select). Use event.target.name, .value, and .checked to update the correct state field.

# Integration

**Sciences — Abstraction Layers:** The OSI network model separates network communication into 7 layers, each providing a clean interface to the layer above. SyntheticEvent is a two-layer abstraction: browser implementation → SyntheticEvent API → your handler. Each layer hides complexity from the layer above — the same principle as protocol stack design.

# Lore Conclusion

*"The translation is invisible when done well. You write one handler; it works in every browser. That is what good abstraction provides."*

---
