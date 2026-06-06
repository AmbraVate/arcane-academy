---
id: fe-app-m5-14
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
lesson: building_interactivity
title: "Building Interactivity"
sortOrder: 5
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m5-13]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Combines selection, update, and event handling in a single feature"
    - "Uses state (a variable) to track the current condition of the UI"
    - "Updates the DOM based on the current state correctly"
    - "The feature works correctly on repeated interactions, not just the first"
    - "Code is broken into small, named functions rather than one large block"
  keywords: [state, toggle, interactive, event, DOM, update, select, function, UI]
  modelAnswer: |
    Interactivity requires combining three skills: selecting elements, handling events,
    and updating the DOM. State variables track the current condition of the UI (e.g.,
    isOpen, isDarkMode). When an event fires, the handler reads state, updates it, and
    reflects the change in the DOM. Breaking logic into named functions (toggleMenu,
    incrementCounter) keeps code readable and testable.
guidedSteps:
  - id: js-inter-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A counter button should increase a displayed number each time it is clicked.
      What does the JavaScript need to **track** between clicks?
    inputConfig:
      options:
        - "The button's position on the page"
        - "The current count value (state)"
        - "The time of the last click"
        - "The button's CSS class"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The current count value (state)"]
      rejectedFeedback: "The count is the state — a variable that persists between clicks and reflects what the UI should display. Without it, there is nothing to increment."
    hint: "What variable holds the number that the button changes each time it is clicked?"
    reflectionPrompt: "Correct. State is the information your UI needs to remember between interactions. A counter's state is its current value. A menu's state is open/closed."

  - id: js-inter-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the handler that increments a counter and updates the display:

      ```js
      let count = 0;
      btn.addEventListener('click', () => {
        count = count + ___;
        display.textContent = count;
      });
      ```
    inputConfig:
      placeholder: "value to increment by"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["1", "1;"]
      rejectedFeedback: "count = count + 1 increments the state by 1. Then display.textContent = count updates the DOM to reflect the new state."
    hint: "Each click should add 1 to the count."
    reflectionPrompt: "Exactly the pattern: update state → reflect in DOM. This two-step sequence (state then DOM) is the foundation of all interactive front-end development."

  - id: js-inter-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in your own words why breaking interactive code into named functions
      (e.g., `toggleMenu()`, `incrementCounter()`) is better than writing everything
      inside the event listener callback.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [read, test, reuse, name, function, clear, understand, maintain]
      rejectedFeedback: "Named functions are self-documenting — their names explain what they do. They can be reused elsewhere and tested individually. A large anonymous callback is hard to read and impossible to reuse."
    hint: "What are the benefits of a function called toggleMenu() over an unnamed block of code inside a click listener?"
    reflectionPrompt: "Well articulated. Code that names its behaviour is self-documenting. This is the first step toward writing front-end code that scales beyond a few features."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In the pattern 'update state, then update DOM', why must state come first?"
    options:
      - "The DOM loads before JavaScript"
      - "The DOM must reflect the state, so state must be correct first"
      - "State is faster to update than the DOM"
      - "It is just a convention with no practical reason"
    correctIndex: 1
    feedback: "State is the source of truth. The DOM is just a visual representation of the state. If you update the DOM before the state, your display and data can get out of sync."
  - type: MULTIPLE_CHOICE
    question: "A 'show/hide' toggle button needs to remember whether the content is currently visible. How should this be stored?"
    options:
      - "As an inline style on the button"
      - "In a let variable (state)"
      - "In a CSS file"
      - "It doesn't need to be stored — check classList each time"
    correctIndex: 1
    feedback: "A state variable (let isVisible = false) is the clearest approach. Alternatively, checking classList.contains('hidden') is also valid — but explicit state variables make the code's intent clearer."

retrieval:
  recall: "Describe the three-step pattern for building an interactive UI feature."
  explain: "Explain what 'state' means in front-end development and give two examples."
  mistakeId:
    code: "btn.addEventListener('click', () => { count++; }); display.textContent = count;"
    answer: "The display update is outside the event listener — it runs once when the script loads, not on each click. Move display.textContent = count; inside the listener so it updates every time count changes."
---

# Hook

You can select elements. You can update them. You can listen for events.

Now it is time to put all three together — to build something genuinely interactive. This lesson is about the *pattern* that underlies every interactive front-end feature, from a counter button to a shopping cart: select the elements you need, listen for user events, update state, and reflect state in the DOM.

Once you understand this pattern, every interactive feature you ever build will follow it.

> What is the simplest interactive feature you can imagine building with what you know? A counter? A show/hide toggle? A to-do list?

# Lore Introduction

Master Aelindra stands back and looks at the apprentice's workspace — selection runes, update runes, and listener bells, all laid out separately.

*"You have learned the instruments. Now we compose,"* she says. *"Every interactive enchantment follows the same score: find the elements, listen for events, remember the state, and update the display. Three scrolls, one performance."*

She begins to assemble the instruments in sequence.

*"Watch. Then do it yourself."*

# Core Learning

## Concept Introduction

Every interactive UI feature follows this pattern:

1. **Select** the elements you need (once, at setup)
2. **Listen** for user events
3. **Update state** (the data that drives the UI)
4. **Update the DOM** to reflect the new state

```js
// 1. Select
const btn = document.getElementById("counter-btn");
const display = document.getElementById("count-display");

// 2. State
let count = 0;

// 3. Listen + update
btn.addEventListener("click", () => {
  count++;                          // update state
  display.textContent = count;      // reflect in DOM
});
```

**State** is any variable that tracks the current condition of your UI. It is the source of truth. The DOM is just the visual representation of state.

## Why It Matters

This pattern — **select → listen → state → DOM** — is not just an apprentice technique. It is the core idea behind every major JavaScript framework (React, Vue, Angular). React's entire model is "state changes cause re-renders". jQuery, Svelte, and Angular all express variations of the same fundamental idea.

Learn this pattern deeply now, and frameworks will feel familiar later.

## Worked Examples

**Example 1 — Counter**

```js
const btn = document.getElementById("increment");
const display = document.getElementById("value");
let count = 0;

btn.addEventListener("click", () => {
  count++;
  display.textContent = count;
});
```

**Example 2 — Show/Hide Toggle**

```js
const toggleBtn = document.getElementById("toggle");
const panel = document.getElementById("panel");
let isVisible = false;

toggleBtn.addEventListener("click", () => {
  isVisible = !isVisible;
  panel.style.display = isVisible ? "block" : "none";
  toggleBtn.textContent = isVisible ? "Hide" : "Show";
});
```

**Example 3 — Dark Mode Toggle**

```js
const themeBtn = document.getElementById("theme-btn");

themeBtn.addEventListener("click", () => {
  document.body.classList.toggle("dark-mode");
  const isDark = document.body.classList.contains("dark-mode");
  themeBtn.textContent = isDark ? "Light Mode" : "Dark Mode";
});
```

**Example 4 — Simple To-Do Adder**

```js
const form = document.getElementById("todo-form");
const input = document.getElementById("todo-input");
const list = document.getElementById("todo-list");

form.addEventListener("submit", (e) => {
  e.preventDefault();
  const text = input.value.trim();
  if (!text) return;

  const li = document.createElement("li");
  li.textContent = text;
  list.appendChild(li);

  input.value = "";
});
```

This example combines everything: selection, events, DOM creation, state-like conditions, and input handling.

## Common Mistakes

- Selecting elements inside the event listener on every click (inefficient — select once)
- Updating the DOM before updating state — leads to out-of-sync displays
- Not checking for empty/invalid input before processing it
- Putting all logic inside one large anonymous function — break into named functions

## Mental Model

Interactive UI is like a **thermostat**.

The thermostat has state (current temperature setting, current temperature). It listens for events (temperature drops below threshold). It updates state (calls for heat). It reflects state in the display (the dial shows the target temperature).

Your JavaScript is the thermostat's control system: it holds state, listens for signals, and keeps the display in sync.

## Mini Summary

- Every interactive feature: select → listen → update state → update DOM
- **State** is the variable(s) that represent the current condition of the UI
- The DOM reflects state — always update state first, then the DOM
- Break handlers into named functions: `incrementCounter()`, `toggleMenu()`
- Validate input before acting on it (empty string, null, etc.)

# Guided Practice Quest

In this quest you will trace the state-DOM pattern in a counter, complete the increment step, and explain why named functions are better than inline callbacks for complex interactions.

These three steps cement the core interactive pattern you will use in the mini-project and beyond.

# Solo Practice Quest

Build a complete interactive to-do list in a single HTML file:

1. An input field and "Add" button to add tasks
2. Each task appears as a list item with a "Done" button beside it
3. Clicking "Done" marks the task with a strikethrough style
4. A counter showing "X tasks remaining" updates in real time

Use the select → listen → state → DOM pattern throughout. Break your logic into named functions.

# Integration

**Connecting to Psychology — Cognitive Load and Progressive Disclosure**

User interface designers use the principle of *progressive disclosure* — showing users only what they need at the current moment, revealing more as needed. A collapsed section, a tooltip on hover, a form that expands when a checkbox is ticked — all of these reduce cognitive load by limiting the information visible at once.

Every show/hide toggle you build with JavaScript is progressive disclosure. Every modal that appears on a button click is progressive disclosure. Understanding the psychological motivation — reduce overwhelm, focus attention — helps you decide *when* to hide things, not just *how*.

The best interactive UI is not the flashiest. It is the one that shows the user exactly what they need, exactly when they need it.

# Lore Conclusion

The apprentice steps back and watches their first complete interactive feature respond to a click, then another, then another.

*"This is the pattern,"* Master Aelindra says quietly. *"Select, listen, state, display. You will write it a thousand more times in your career — but you will never find a better foundation."*

The workshop instruments settle into place. Module Five is complete.

---
