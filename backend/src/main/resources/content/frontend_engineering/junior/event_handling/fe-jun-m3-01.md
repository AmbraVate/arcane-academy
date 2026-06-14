---
id: fe-jun-m3-01
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
lesson: react_events
title: "React Events"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Attaches event handlers using JSX syntax (onClick, onChange)"
    - "Understands that React uses camelCase event names"
    - "Explains what SyntheticEvents are and why React uses them"
    - "Knows how to prevent default browser behaviour"
  keywords: [event, onClick, onChange, camelCase, SyntheticEvent, preventDefault, handler, JSX]
  modelAnswer: |
    React events are attached as JSX props using camelCase names (onClick, onChange).
    The handler receives a SyntheticEvent — React's cross-browser wrapper around the
    native DOM event. SyntheticEvents normalise behaviour across browsers. Call
    event.preventDefault() to stop default browser actions (form submission, link navigation).
guidedSteps:
  - id: fe-jun-m3-01-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is wrong with `<button onclick="handleClick()">Click</button>` in React JSX?
    inputConfig:
      options:
        - "Nothing — this is correct React syntax"
        - "React uses camelCase: onClick={handleClick} (no parentheses, no string)"
        - "Buttons cannot have event handlers in React"
        - "You must use addEventListener instead"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["React uses camelCase: onClick={handleClick} (no parentheses, no string)"]
      rejectedFeedback: "React JSX uses camelCase event names as props with function references in curly braces — not strings and not called with (). onClick={handleClick} is correct. onclick='handleClick()' is HTML syntax and a common mistake."
    hint: "Two differences from HTML: case and syntax."
    reflectionPrompt: "onClick={handleClick} passes the function reference — React calls it when clicked. onClick={handleClick()} calls the function immediately at render and passes its return value. This subtle difference causes a very common bug."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "How do you prevent a form from submitting to the server in React?"
    options:
      - "Remove the type='submit' from the button"
      - "Call event.preventDefault() in the onSubmit handler"
      - "Use onChange instead of onSubmit"
      - "Set action='' on the form element"
    correctIndex: 1
    feedback: "event.preventDefault() cancels the default browser behaviour. For forms, the default is submitting to the server and refreshing the page. Calling it in onSubmit lets React handle the submission instead."

retrieval:
  recall: "Write a React button with an onClick handler that logs 'clicked' to the console."
  explain: "Explain the difference between onClick={fn} and onClick={fn()} in JSX."
  mistakeId:
    code: "<button onClick={handleClick()}>Click</button>"
    answer: "handleClick() calls the function at render time and passes its return value. Use onClick={handleClick} to pass the function reference — React calls it on click."
---

# Hook

React events look like HTML events, but they have important differences. Understanding those differences prevents the most common event handling mistakes.

# Lore Introduction

*"The Academy's bell system,"* says Master Aelindra, *"rings when pulled. If you pull the rope at installation time rather than attaching it for later, the bell rings immediately — not when needed."*

# Core Learning

## Concept Introduction

```jsx
// Correct: camelCase, function reference, curly braces
<button onClick={handleClick}>Click me</button>

// Correct: inline arrow function
<button onClick={() => setCount(c => c + 1)}>+1</button>

// Prevent default (form submission)
function handleSubmit(event) {
  event.preventDefault();
  // handle form data...
}
<form onSubmit={handleSubmit}>

// Common events
onClick   onChange   onSubmit   onFocus
onBlur    onKeyDown  onMouseEnter  onScroll
```

**SyntheticEvent:** React's cross-browser event wrapper. Has the same API as native events (target, preventDefault, stopPropagation) but works consistently across all browsers.

## Common Mistakes

- **Calling the handler instead of passing it**: `onClick={handleClick()}` executes the function during render and passes the return value as the handler. Use `onClick={handleClick}` (no parentheses) to pass the function reference.
- **Using lowercase event prop names**: React event props are camelCase — `onClick`, `onChange`, `onSubmit`. Lowercase `onclick` is a plain HTML attribute that React ignores.
- **Forgetting `event.preventDefault()` on form submits**: Without it, the form causes a full page reload, destroying React state and the SPA experience.
- **Attaching event handlers with `addEventListener` when JSX props are available**: Using `addEventListener` directly bypasses React's synthetic event system and requires manual cleanup to avoid memory leaks.

## Mental Model

Think of React events as a hotel's front desk rather than wires soldered to every doorknob. In vanilla JavaScript you walk the building and wire each knob yourself (`addEventListener` per element) — and re-wire whenever a door is replaced. In React you instead leave *standing instructions at reception*: the JSX `onClick={handleOrder}` is a note in the hotel's ledger saying "when anyone presses the button in room 204, call this number". The hotel (React) handles the physical wiring centrally and keeps the ledger synchronised as rooms are renovated, added, or demolished (components re-render, mount, unmount) — your instructions survive every renovation because they're part of the room's *description*, not its wiring. The ledger model explains the two classic beginner bugs precisely. Writing `onClick={handleOrder()}` is not leaving instructions — it's *making the call yourself while filling in the ledger* (the function executes during render), which is why things fire before anyone touches anything; the desk needs the phone number (`handleOrder`) or a sealed note (`() => handleOrder(id)`), not the result of the call. And because instructions are re-submitted with every room description (each render), handlers are cheap to declare inline — but each note is written with *that day's information* (that render's state values), which plants the seed for understanding stale closures later. Describe the room, leave the note, let the desk do the wiring: that's React's entire event model.

## Why It Matters

Events are where an interface stops being a picture and becomes software — everything a user *does* arrives as an event, and React's event layer is the front door of all interactivity:

- Every feature you will ever ship begins with a handler: clicks become navigation, keystrokes become search, submits become saved data — there is no interactive React without this layer, which is why it's the first module-three skill
- React's declarative attachment (`onClick={handleClick}` in JSX) is a genuine upgrade over scattering `addEventListener` calls: the behaviour is visible exactly where the element is described, there's no separate registration step to forget, and React manages attaching and cleaning up listeners so you don't leak them when components unmount
- The naming and wiring conventions are load-bearing: camelCase prop names, passing a function rather than calling one (`onClick={fn}` versus the classic `onClick={fn()}` bug that fires on render), and handler props on components (`onSave`, `onSelect`) are patterns you'll read and write daily
- Misunderstood events produce a recognisable bug family — handlers firing on every render, stale values inside callbacks, clicks doing nothing because the function was invoked too early — and nearly all of them trace back to fuzzy mental models formed (or not) right here

Mastering the event layer early means every later topic — forms, routing, data fetching triggered by interaction — builds on solid ground instead of on copy-pasted incantations.

## Mini Summary
- ✔ JSX: camelCase prop names (onClick not onclick)
- ✔ Pass function reference, not call: {fn} not {fn()}
- ✔ SyntheticEvent wraps native events for cross-browser consistency
- ✔ event.preventDefault() cancels default browser behaviour

# Solo Practice Quest

Build a form with name and email fields. Use onSubmit with preventDefault. Log the field values to the console. Add a button that increments a counter using onClick.

# Integration

**Psychology — Event-Driven Interaction Design:** User interfaces are event-driven by nature — the user acts, the UI responds. React's event system models this directly. Understanding events as the mechanism of user interaction helps you design UI flows as sequences of events and responses rather than static states.

# Lore Conclusion

*"Attach the handler to the rope. Pull when needed. Never ring the bell at installation."*

---
