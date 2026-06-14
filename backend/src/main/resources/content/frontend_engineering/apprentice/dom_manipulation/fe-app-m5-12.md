---
id: fe-app-m5-12
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
lesson: updating_content
title: "Updating Content"
sortOrder: 3
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m5-11]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Uses textContent to update an element's text safely"
    - "Explains why innerHTML is risky and when textContent is preferred"
    - "Changes an element's CSS class using classList.add or classList.toggle"
    - "Updates a style property directly using element.style"
    - "Creates and appends a new element to the DOM"
  keywords: [textContent, innerHTML, classList, style, createElement, appendChild, attribute]
  modelAnswer: |
    textContent updates an element's text safely — it treats the value as plain text, not
    HTML. innerHTML parses HTML tags but is vulnerable to XSS if user input is involved.
    classList.add/remove/toggle manage CSS classes. element.style sets inline styles.
    createElement and appendChild create and insert new nodes into the DOM.
guidedSteps:
  - id: js-update-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Why is `textContent` safer than `innerHTML` for displaying user-provided text?
    inputConfig:
      options:
        - "textContent is faster than innerHTML"
        - "textContent treats the value as plain text, preventing HTML injection"
        - "innerHTML does not support Unicode characters"
        - "textContent works in all browsers; innerHTML does not"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["textContent treats the value as plain text, preventing HTML injection"]
      rejectedFeedback: "innerHTML parses the string as HTML — if user input contains <script> tags, they execute. textContent treats everything as literal text, so it is safe against XSS attacks."
    hint: "If a user types <script>alert('hacked')</script> into a form and you display it with innerHTML, what happens?"
    reflectionPrompt: "Exactly. textContent never executes HTML — the string is displayed as plain characters. Use innerHTML only when you control the content and intentionally need to insert HTML."

  - id: js-update-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Toggle a CSS class called `active` on an element called `btn`:

      ```js
      btn.classList.___;
      ```
    inputConfig:
      placeholder: "method call"
    markingRule:
      matchMode: CONTAINS
      accepted: ["toggle('active')", 'toggle("active")']
      rejectedFeedback: "classList.toggle('active') adds the class if it is absent, removes it if it is present — perfect for on/off states like active, open, or visible."
    hint: "toggle switches something on if it's off, and off if it's on."
    reflectionPrompt: "Correct. classList.toggle is ideal for state-based UI: dark mode, expanded menus, active tabs. One method call handles both directions."

  - id: js-update-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe the steps to create a new `<li>` element with the text "New Item"
      and add it to an existing `<ul id="list">`.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [createElement, textContent, appendChild, querySelector, getElementById]
      rejectedFeedback: "Steps: 1) const li = document.createElement('li'); 2) li.textContent = 'New Item'; 3) const ul = document.getElementById('list'); 4) ul.appendChild(li);"
    hint: "You need three steps: create the element, set its content, then append it to the parent."
    reflectionPrompt: "Correct sequence. createElement → set content → appendChild is the standard pattern for adding dynamic content to the DOM."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `classList.remove('hidden')` do?"
    options:
      - "Hides the element by setting display: none"
      - "Removes the CSS class 'hidden' from the element's class list"
      - "Deletes the element from the DOM"
      - "Clears all classes from the element"
    correctIndex: 1
    feedback: "classList.remove('hidden') removes only the 'hidden' class — other classes are unchanged. It does not affect the element's visibility unless your CSS defines what .hidden does."
  - type: MULTIPLE_CHOICE
    question: "Which property would you use to change an element's text without risking HTML injection?"
    options:
      - "innerHTML"
      - "outerHTML"
      - "textContent"
      - "innerText"
    correctIndex: 2
    feedback: "textContent treats the value as plain text — HTML tags are never parsed. It is the safe default for displaying content from unknown sources."

retrieval:
  recall: "List four ways to update a DOM element using JavaScript."
  explain: "Explain the XSS risk with innerHTML and how textContent mitigates it."
  mistakeId:
    code: "const div = document.querySelector('.box'); div.style.background-color = 'red';"
    answer: "CSS property names with hyphens must be written in camelCase in JavaScript: div.style.backgroundColor = 'red'. The hyphen is interpreted as subtraction in JavaScript."
---

# Hook

You can find elements. Now it is time to change them.

Updating DOM elements is the core activity of front-end JavaScript. Swapping text, toggling a class, setting a colour, adding a new list item — every one of these is a DOM update. And the updates are immediate: the moment your code runs, the page reflects the change.

This is where JavaScript becomes truly dynamic.

> Think of a webpage where the content changes in response to what you do — a cart total, a character counter, a filtered list. What kind of updates are happening?

# Lore Introduction

Master Aelindra places an etched stone tablet on the workbench.

*"You have found the node,"* she says. *"Now you must change it. Text can be rewritten. Appearance can be shifted. New nodes can be conjured and attached. The page is not fixed — it is malleable, responsive to your will."*

She presses a rune and the tablet's text shifts before the apprentice's eyes.

*"Four instruments: content, class, style, and creation. Master these, and the page obeys."*

# Core Learning

## Concept Introduction

### Updating Text Content

```js
element.textContent = "New text";    // safe — treats as plain text
element.innerHTML = "<b>Bold</b>";   // parses HTML — risky with user input
```

### Managing CSS Classes

```js
element.classList.add("active");       // add a class
element.classList.remove("hidden");    // remove a class
element.classList.toggle("open");      // add if absent, remove if present
element.classList.contains("error");   // returns true/false
```

### Updating Inline Styles

```js
element.style.color = "red";
element.style.backgroundColor = "lightblue";  // camelCase!
element.style.fontSize = "18px";
```

### Creating and Inserting Elements

```js
const newEl = document.createElement("p");
newEl.textContent = "A new paragraph";
parentElement.appendChild(newEl);       // add at end
parentElement.prepend(newEl);           // add at start
existingEl.insertAdjacentElement("afterend", newEl);
```

| Method | When to use |
|--------|------------|
| `textContent` | Displaying text (safe, no HTML parsing) |
| `innerHTML` | Inserting HTML you control |
| `classList.*` | Toggling visual states defined in CSS |
| `style.*` | One-off inline overrides |
| `createElement` + `appendChild` | Building new DOM nodes dynamically |

## Why It Matters

The ability to update the DOM without reloading the page is what distinguishes a web *application* from a web *document*. Every live preview, every instant search result, every dynamically generated list item depends on these techniques.

## Worked Examples

**Example 1 — Update text on button click**

```js
const btn = document.getElementById("toggle-btn");
const msg = document.getElementById("message");
btn.addEventListener("click", () => {
  msg.textContent = "Button was clicked!";
});
```

**Example 2 — Toggle a dark mode class**

```js
const body = document.body;
document.getElementById("theme-btn").addEventListener("click", () => {
  body.classList.toggle("dark-mode");
});
```

CSS defines what `.dark-mode` looks like; JavaScript just adds or removes the class.

**Example 3 — Add a new list item**

```js
const ul = document.getElementById("task-list");
const newItem = document.createElement("li");
newItem.textContent = "Review the DOM lesson";
ul.appendChild(newItem);
```

**Example 4 — Change a style property**

```js
const card = document.querySelector(".card");
card.style.border = "2px solid #6c63ff";
card.style.borderRadius = "8px";
```

## Common Mistakes

- Using hyphenated CSS property names (use camelCase: `backgroundColor` not `background-color`)
- Using `innerHTML` with untrusted user input — a cross-site scripting (XSS) vulnerability
- Forgetting to append a created element — `createElement` alone does not insert it into the page
- Over-using `element.style` — prefer adding/removing CSS classes to keep style logic in CSS files

## Mental Model

Think of DOM updates as three different kinds of editing:

- `textContent` is rewriting the words on a sign
- `classList.toggle` is flipping a light switch — the CSS defines what light-on and light-off look like
- `createElement` + `appendChild` is building a new room and attaching it to the house

The page is the house. JavaScript is the builder.

## Mini Summary

- `textContent` updates text safely; `innerHTML` parses HTML (risky with user input)
- `classList.add/remove/toggle/contains` manage CSS classes without touching CSS files
- `element.style.propertyName` sets inline styles using camelCase property names
- `createElement` + `appendChild` builds and inserts new DOM nodes
- Prefer classList over inline styles — keep visual logic in CSS

# Guided Practice Quest

In this quest you will choose between textContent and innerHTML, toggle a class, and describe the steps to create and append a new element.

These three steps cover the most frequent DOM mutation patterns in web development.

# Solo Practice Quest

Create a minimal HTML page (or use the browser console) with:
- A heading `<h1 id="title">Hello</h1>`
- A button `<button id="btn">Change It</button>`

Write JavaScript that:
1. Changes the heading text to your name when the button is clicked
2. Toggles a CSS class `highlight` on the heading (define `.highlight { color: purple; }` in CSS)
3. Adds a new `<p>` below the heading saying "Updated!" each time the button is clicked

# Integration

**Connecting to Psychology — Immediate Feedback and Engagement**

In user experience design, *immediate feedback* is one of the most powerful engagement mechanisms. Research shows that response times under 100ms feel instantaneous to users; anything over 1 second breaks the flow of thought.

DOM updates via JavaScript are measured in milliseconds. When you toggle a class or update text in response to a user action, the feedback loop is effectively instant — users feel in control, in flow. When DOM updates are delayed (waiting for a server response without a loading indicator), users disengage.

Understanding that DOM manipulation is not just a technical act but a *communication act* — the interface telling the user "I heard you" — shapes how you design interactions.

# Lore Conclusion

The apprentice rewrites the tablet's text with a single rune, and the words shift instantly.

*"Now your enchantments are not frozen in time,"* Master Aelindra says. *"They can grow, they can change, they can respond. The next step: teaching them to *listen*."*

The workshop hums with readiness.

---
