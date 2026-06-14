---
id: fe-app-m5-11
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
lesson: selecting_elements
title: "Selecting Elements"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m5-10]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Uses getElementById correctly to select a specific element"
    - "Uses querySelector with a CSS selector to find an element"
    - "Uses querySelectorAll and explains what it returns"
    - "Explains when to use getElementById vs querySelector"
    - "Checks if a selected element could be null before using it"
  keywords: [getElementById, querySelector, querySelectorAll, selector, null, NodeList, DOM]
  modelAnswer: |
    getElementById('id') selects a single element by its id attribute — it is fast and
    specific. querySelector(selector) accepts any CSS selector and returns the first match.
    querySelectorAll(selector) returns a NodeList of all matches. If no element is found,
    getElementById and querySelector return null — always check before operating on the result.
guidedSteps:
  - id: js-sel-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which method selects an element by its `id` attribute?
    inputConfig:
      options:
        - "document.querySelector('.myId')"
        - "document.getElementById('myId')"
        - "document.getElement('myId')"
        - "document.select('#myId')"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["document.getElementById('myId')"]
      rejectedFeedback: "getElementById takes the id value directly (without a # prefix) and returns exactly one element or null."
    hint: "This method's name describes exactly what it does: get Element By Id."
    reflectionPrompt: "Correct. getElementById is the fastest and most specific selector — use it when you have an id and need exactly that element."

  - id: js-sel-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the call to select the first `<button>` with class `submit-btn`:

      ```js
      const btn = document.___(___);
      ```
    inputConfig:
      placeholder: "method and selector"
    markingRule:
      matchMode: CONTAINS
      accepted: ["querySelector", ".submit-btn", "button.submit-btn"]
      rejectedFeedback: "Use document.querySelector('.submit-btn') or document.querySelector('button.submit-btn') — querySelector accepts any CSS selector."
    hint: "querySelector accepts a CSS selector string. A class selector starts with a dot."
    reflectionPrompt: "Correct. querySelector('.submit-btn') matches the first element with that class. The selector syntax is identical to CSS."

  - id: js-sel-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      What does `querySelectorAll` return, and how is it different from `querySelector`?
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [NodeList, all, multiple, first, one, collection, list]
      rejectedFeedback: "querySelectorAll returns a NodeList of ALL matching elements. querySelector returns only the FIRST match (or null)."
    hint: "One returns one element; the other returns a collection. Which one has 'All' in the name?"
    reflectionPrompt: "Correct. NodeList is like an array of elements. You can iterate over it with forEach or for...of. It is not a true Array, but it behaves similarly for iteration."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does getElementById return if no element with that id exists?"
    options:
      - "undefined"
      - "null"
      - "an empty array"
      - "throws an error"
    correctIndex: 1
    feedback: "getElementById returns null when no matching element is found. Always check for null before calling methods on the result."
  - type: MULTIPLE_CHOICE
    question: "Which selector syntax finds ALL paragraphs inside a div with class 'content'?"
    options:
      - "document.querySelector('div paragraphs')"
      - "document.querySelectorAll('.content p')"
      - "document.getElementsByTag('p', '.content')"
      - "document.getAll('div.content > p')"
    correctIndex: 1
    feedback: "querySelectorAll('.content p') uses a CSS descendant selector — any <p> inside an element with class 'content'. The CSS selector syntax is identical to stylesheets."

retrieval:
  recall: "What are the three most common DOM selection methods and what does each return?"
  explain: "Explain why you should check if a selected element is null before using it."
  mistakeId:
    code: "const box = document.getElementById('bx'); box.textContent = 'Hello';"
    answer: "If no element with id 'bx' exists, getElementById returns null. Calling .textContent on null throws a TypeError: Cannot set properties of null. Always check: if (box) { box.textContent = 'Hello'; }"
---

# Hook

You now know the DOM exists and that it is a tree. But to do anything useful with it, you first need to *find* the element you want.

A page might have hundreds of elements. How do you reach the exact one you need? JavaScript provides several methods for selecting DOM elements — and understanding which to use, and when, is one of the first practical skills of front-end development.

> When you inspect a webpage's source code in DevTools, how would you describe the process of finding a specific element visually? How might code do the same thing?

# Lore Introduction

Master Aelindra spreads a map of the DOM tree across the workbench.

*"Knowing the tree exists is the first skill,"* she says. *"But to change a node, you must first find it. Some nodes are named — you seek them by name. Some are shaped — you seek them by what they look like. Some you want all of — you cast a wide net and gather the collection."*

She places three runic tools beside the map.

*"Three instruments of selection. Know them well."*

# Core Learning

## Concept Introduction

There are three primary methods for selecting DOM elements:

| Method | Selects | Returns |
|--------|---------|---------|
| `getElementById(id)` | One element by id | Element or `null` |
| `querySelector(selector)` | First match of CSS selector | Element or `null` |
| `querySelectorAll(selector)` | All matches of CSS selector | `NodeList` |

```js
// By id
const header = document.getElementById("main-header");

// First matching CSS selector
const firstButton = document.querySelector("button");
const submitBtn = document.querySelector(".submit-btn");
const navLink = document.querySelector("nav a");

// All matching CSS selectors
const allParagraphs = document.querySelectorAll("p");
const allCards = document.querySelectorAll(".card");
```

## Why It Matters

Before you can change, animate, show, hide, or read any element on the page, you must first *select* it. Selection is the starting point of every DOM manipulation. Using the wrong selector returns `null` or the wrong element — and then every operation that follows will fail or behave unexpectedly.

## Worked Examples

**Example 1 — Select by id, update text**

```html
<h1 id="title">Welcome</h1>
```
```js
const title = document.getElementById("title");
if (title) {
  title.textContent = "Hello, Apprentice!";
}
```

**Example 2 — Select the first of many**

```html
<button class="action-btn">Save</button>
<button class="action-btn">Delete</button>
```
```js
const firstBtn = document.querySelector(".action-btn");
// selects only "Save" — the first match
```

**Example 3 — Select all and iterate**

```html
<li class="item">Apple</li>
<li class="item">Banana</li>
<li class="item">Cherry</li>
```
```js
const items = document.querySelectorAll(".item");
items.forEach(item => {
  console.log(item.textContent);
});
// Apple, Banana, Cherry
```

**Example 4 — Null safety guard**

```js
const el = document.getElementById("optional-section");
if (el !== null) {
  el.style.display = "block";
}
```

## Common Mistakes

- Forgetting the `#` prefix is NOT needed for `getElementById` (but IS needed for `querySelector`)
- Assuming `querySelectorAll` returns an Array — it returns a `NodeList` (no `map()`, `filter()` without conversion)
- Not checking for `null` before operating on a selected element
- Selecting elements before the DOM is ready (script runs before the HTML is parsed)

## Mental Model

Think of the DOM selection methods as three different search tools:

- `getElementById` is a **key lookup** — you have the exact key, find the one matching record instantly
- `querySelector` is a **filtered search** — find the first item that matches a description
- `querySelectorAll` is a **filtered collection** — gather everything that matches a description

## Mini Summary

- `getElementById(id)` — fastest, selects one element by exact id
- `querySelector(selector)` — selects first element matching a CSS selector
- `querySelectorAll(selector)` — selects all elements matching a CSS selector, returns NodeList
- Always check for `null` before operating on a selected element
- `querySelector` and `querySelectorAll` accept the same selector syntax as CSS

# Guided Practice Quest

In this quest you will identify the correct selection method for three scenarios, complete a `querySelector` call, and explain what `querySelectorAll` returns.

These steps build the selection fluency that underpins all DOM manipulation work.

# Solo Practice Quest

Open the browser console on any webpage and try these three selection methods live:

1. `document.getElementById("...")` — pick any element id you see in the source
2. `document.querySelector("h1")` — find the first heading
3. `document.querySelectorAll("a")` — find all links; log how many there are with `.length`

Write 3–5 sentences describing what you found and whether any selection returned `null`.

# Integration

**Connecting to Psychology — Attention and Visual Search**

When users scan a webpage for a specific element, they perform a *visual search* — scanning hierarchically from large, distinctive features down to specific details. User interface designers exploit this by making important elements distinctive (large, contrasting, prominently placed).

Developers do the same when selecting elements: `getElementById` is the equivalent of spotting a neon sign (uniquely labelled). `querySelector(".highlight")` is like scanning for a distinctive visual feature. `querySelectorAll` is like collecting everything of a particular kind.

Understanding that both human attention and code selection work by filtering based on identifiable features helps you design pages where both users and code can find the right elements efficiently.

# Lore Conclusion

The apprentice lifts each of the three runic tools and examines them.

*"The first seeks a single name. The second seeks the first of a kind. The third gathers all of a kind."* Master Aelindra names each one. *"Now that you can find what you seek, the next lesson will teach you to change what you find."*

The map of the DOM tree glows faintly at the selected nodes.

---
