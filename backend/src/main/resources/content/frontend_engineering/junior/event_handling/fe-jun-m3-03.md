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
