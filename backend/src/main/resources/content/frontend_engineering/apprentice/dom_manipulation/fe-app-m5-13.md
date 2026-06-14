---
id: fe-app-m5-13
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m5
moduleTitle: "Module 5: JavaScript Foundations"
moduleGlyph: "⚡"
moduleSortOrder: 5
topicSlug: dom_manipulation
topicTitle: "DOM Manipulation"
topicSortOrder: 3
lesson: event_handling
title: "Event Handling"
sortOrder: 4
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m5-12]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Attaches an event listener using addEventListener correctly"
    - "Names at least four browser events (e.g. click, keydown, submit, input)"
    - "Uses the event object (e) to access event details"
    - "Prevents default browser behaviour using e.preventDefault()"
    - "Explains why addEventListener is preferred over inline onclick attributes"
  keywords: [addEventListener, event, click, handler, preventDefault, event object, listener]
  modelAnswer: |
    addEventListener(eventType, handler) attaches a function to run when an event occurs
    on an element. The handler receives an event object with details (e.target, e.key, etc).
    e.preventDefault() stops the browser's default action (e.g., form submission or link
    navigation). addEventListener is preferred over inline handlers because it keeps
    JavaScript separate from HTML and allows multiple listeners on the same element.
guidedSteps:
  - id: js-evt-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which method correctly attaches a click handler to a button element `btn`?
    inputConfig:
      options:
        - "btn.onClick = handleClick;"
        - "btn.on('click', handleClick);"
        - "btn.addEventListener('click', handleClick);"
        - "btn.addEvent(click, handleClick);"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["btn.addEventListener('click', handleClick);"]
      rejectedFeedback: "addEventListener is the standard method. It takes two arguments: the event type as a string ('click') and the handler function (without parentheses — you pass the reference, not the result of calling it)."
    hint: "The method is addEventListener, the event name is a string, and you pass the function without calling it."
    reflectionPrompt: "Correct. Note: you pass handleClick (the function reference), not handleClick() (the return value of calling it immediately). This is a common mistake."

  - id: js-evt-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the handler to prevent a form from submitting and log the event:

      ```js
      form.addEventListener('submit', (___) => {
        e.preventDefault();
        console.log('Form intercepted!');
      });
      ```
    inputConfig:
      placeholder: "parameter name"
    markingRule:
      matchMode: NORMALIZED
      accepted: [e, event, evt]
      rejectedFeedback: "The event handler receives an event object as its parameter. It is conventionally named e or event. e.preventDefault() stops the browser's default action."
    hint: "The handler function receives the event object as its first argument — conventionally named e."
    reflectionPrompt: "Correct. The event object is always passed to your handler. e.preventDefault() is essential for form handling — without it, the page reloads on submit."

  - id: js-evt-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why `addEventListener` is better than using `onclick="..."` attributes directly in HTML.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [separate, multiple, JavaScript, HTML, maintain, override, clean]
      rejectedFeedback: "addEventListener keeps JavaScript out of HTML, allows multiple handlers on one element, and is easier to manage. Inline onclick attributes mix concerns and can only attach one handler."
    hint: "Think about the separation of concerns principle — HTML for structure, JavaScript for behaviour."
    reflectionPrompt: "Good reasoning. Separation of concerns makes code easier to test, maintain, and understand. addEventListener also allows multiple listeners on the same element; onclick only allows one."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `e.target` refer to inside an event handler?"
    options:
      - "The event type (e.g., 'click')"
      - "The element that was interacted with"
      - "The parent of the element"
      - "The handler function"
    correctIndex: 1
    feedback: "e.target is the element that triggered the event — the button clicked, the input typed in, the link hovered. It is very useful for event delegation."
  - type: MULTIPLE_CHOICE
    question: "Which event fires when the user presses a key while an input is focused?"
    options:
      - "click"
      - "change"
      - "keydown"
      - "focus"
    correctIndex: 2
    feedback: "keydown fires when a key is pressed. keyup fires when released. input fires when the input value changes (including backspace). change fires when focus leaves the input and the value differs."

retrieval:
  recall: "Write an addEventListener call that logs 'Submitted!' when a form with id 'signup' is submitted."
  explain: "Explain what e.preventDefault() does and give a use case."
  mistakeId:
    code: "btn.addEventListener('click', doSomething());"
    answer: "doSomething() calls the function immediately and passes its return value (likely undefined) as the handler. Remove the parentheses: btn.addEventListener('click', doSomething); — pass the function reference, not its result."
---

# Hook

A page that only changes when you write a script to change it on load is still not truly interactive.

True interactivity means the page responds to what the *user* does: a click, a keystroke, a form submission, a scroll. This is event-driven programming — and it is the dominant programming model for front-end development.

Events are everywhere in the browser. The question is: how do you listen for them?

> Think about every interaction you make on a social media app in a single minute. How many distinct events do you think those interactions fire?

# Lore Introduction

Master Aelindra gestures to a wall covered in small bells, each connected to a different part of the DOM tree.

*"Every element on a page is connected to the world through these bells,"* she says. *"When a user clicks, types, scrolls — a bell rings. Your enchantment must *listen* for the ring, and when it hears the right one, respond."*

She points to a larger bell labelled `click`.

*"addEventListener is the rope that ties your function to the bell. When it rings, your function runs. That is event-driven programming."*

# Core Learning

## Concept Introduction

An **event** is something that happens in the browser — a user action or a system notification.

An **event listener** is a function attached to an element that runs when a specified event occurs.

```js
element.addEventListener(eventType, handlerFunction);
```

| Event | When it fires |
|-------|--------------|
| `click` | User clicks the element |
| `dblclick` | User double-clicks |
| `keydown` | User presses a key |
| `keyup` | User releases a key |
| `input` | Input value changes |
| `change` | Input loses focus with a new value |
| `submit` | Form is submitted |
| `mouseover` | Mouse enters the element |
| `scroll` | User scrolls |
| `DOMContentLoaded` | Page has loaded |

## Why It Matters

Events are the mechanism through which users communicate with your JavaScript code. Without event listeners, your code runs once (on load) and then sits idle. With event listeners, your code wakes up whenever something relevant happens — a click, a key press, a scroll — and responds dynamically.

## Worked Examples

**Example 1 — Button click**

```js
const btn = document.getElementById("greet-btn");
btn.addEventListener("click", () => {
  alert("Hello, apprentice!");
});
```

**Example 2 — Accessing the event object**

```js
document.addEventListener("keydown", (e) => {
  console.log("Key pressed:", e.key);
  if (e.key === "Escape") {
    console.log("Escape pressed — close modal?");
  }
});
```

**Example 3 — Preventing default form submission**

```js
const form = document.getElementById("signup-form");
form.addEventListener("submit", (e) => {
  e.preventDefault();  // stop browser from reloading the page
  const email = document.getElementById("email").value;
  console.log("Submitted email:", email);
  // validate and handle here
});
```

**Example 4 — Live input reaction**

```js
const input = document.getElementById("search");
input.addEventListener("input", (e) => {
  console.log("User is typing:", e.target.value);
});
```

## Common Mistakes

- Passing `handler()` instead of `handler` — calling the function immediately instead of registering it
- Forgetting `e.preventDefault()` on form `submit` — the page reloads and you lose the response
- Attaching event listeners inside a loop without understanding closures — all handlers share the same variable
- Using deprecated `onclick`, `onkeydown` attributes in HTML when JavaScript approach is available

## Mental Model

An event listener is a **security camera**.

You install it once and walk away. When something happens in its field of view (a click, a keypress), it triggers automatically — you don't have to keep checking.

`addEventListener` is the act of mounting the camera. The callback function is the alarm that fires when the camera detects an event.

## Mini Summary

- Events are user actions or system notifications in the browser
- `addEventListener(type, handler)` attaches a listener to an element
- The handler receives an event object with details about the event
- `e.preventDefault()` stops the browser's default action
- `e.target` is the element that triggered the event
- Pass the function reference, not a function call: `handler` not `handler()`

# Guided Practice Quest

In this quest you will identify the correct `addEventListener` syntax, complete an event handler using the event object, and explain why `addEventListener` is preferred over inline event attributes.

These three steps build the event-driven thinking that underpins all interactive JavaScript.

# Solo Practice Quest

Build a small interactive page:
1. Create an `<input>` and a `<p>` below it
2. Use the `input` event to update the `<p>` text in real time as the user types (character counter: "You've typed X characters")
3. Add a `keydown` listener on the whole document that logs which key was pressed to the console
4. Add a form with a submit button; use `e.preventDefault()` to prevent reload and log "Form submitted!" instead

# Integration

**Connecting to Psychology — Stimulus-Response Learning**

Behaviourist psychology describes learning through stimulus-response pairs: a stimulus occurs, a conditioned response follows. B.F. Skinner's operant conditioning showed that reinforced responses become habitual.

Interface design borrows this model deliberately. A click (stimulus) produces an immediate visual change (response). The faster and more satisfying the response, the more the user is reinforced to interact. Buttons that feel unresponsive — where the click fires but nothing visibly changes — violate this model and feel broken.

Event listeners are the mechanism that makes stimulus-response possible in web interfaces. When you attach a click listener that provides instant feedback, you are applying psychological principles of responsiveness.

# Lore Conclusion

The apprentice ties the rope to the first bell and watches it glow softly, waiting.

*"Your enchantments can now listen,"* Master Aelindra says. *"In the final DOM lesson, you will combine everything — selection, update, and events — to build something that truly responds."*

The wall of bells sways gently, ready to ring.

---
