---
id: fe-app-m5-10
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
lesson: the_dom
title: "The DOM"
sortOrder: 1
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m5-05]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what the DOM is in your own words"
    - "Describes the tree structure: nodes, parents, children, siblings"
    - "Explains how JavaScript uses the DOM to change page content"
    - "Identifies the root node of every HTML document in the DOM"
    - "Describes what happens to the DOM when JavaScript changes it"
  keywords: [DOM, tree, node, element, document, parent, child, browser, render]
  modelAnswer: |
    The DOM (Document Object Model) is the browser's representation of an HTML page
    as a tree of nodes. JavaScript accesses and modifies this tree through the global
    document object. Changes to the DOM cause the browser to update the rendered page
    immediately. The root is the document node; html is its child; head and body are
    children of html.
guidedSteps:
  - id: js-dom-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does DOM stand for?
    inputConfig:
      options:
        - "Dynamic Object Manipulation"
        - "Document Object Model"
        - "Display Object Manager"
        - "Document Organisation Map"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Document Object Model"]
      rejectedFeedback: "DOM stands for Document Object Model — the browser's tree-structured representation of an HTML page that JavaScript can read and modify."
    hint: "The D is for Document, the O is for Object, the M is for Model."
    reflectionPrompt: "Correct. The DOM is the bridge between HTML (the document) and JavaScript (the programming language). It represents the page as objects that code can manipulate."

  - id: js-dom-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In the DOM tree, every HTML element is called a ___. They are arranged
      in a parent-child hierarchy like a family tree.
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: [node, "node"]
      rejectedFeedback: "Every item in the DOM tree is a node — element nodes, text nodes, and attribute nodes. HTML elements are element nodes."
    hint: "Think of a family tree: the individual members are called ___s."
    reflectionPrompt: "Correct. The DOM tree is made of nodes. An element like <p> is an element node; the text inside it is a text node. JavaScript can access and change both."

  - id: js-dom-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain what happens to the rendered page when JavaScript changes the DOM.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [update, change, re-render, reflect, immediately, browser, display]
      rejectedFeedback: "When JavaScript modifies the DOM, the browser re-renders the affected part of the page. The change appears on screen without a full page reload."
    hint: "Think about what you see in the browser when JavaScript adds a new paragraph to the DOM."
    reflectionPrompt: "Exactly. The DOM is live. When JavaScript changes it, the browser immediately updates the visible page. This is the foundation of dynamic web content."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which global object gives JavaScript access to the DOM?"
    options:
      - "window"
      - "document"
      - "html"
      - "browser"
    correctIndex: 1
    feedback: "The document object is the entry point to the DOM. window is the global browser context. document.body, document.getElementById(), etc. all start with document."
  - type: MULTIPLE_CHOICE
    question: "In the DOM tree, what is the parent of the <head> and <body> elements?"
    options:
      - "document"
      - "<html>"
      - "<root>"
      - "window"
    correctIndex: 1
    feedback: "The <html> element is the parent of both <head> and <body>. The document object itself is above <html> — it is the root of the tree."

retrieval:
  recall: "Describe the DOM tree structure from document down to a paragraph element."
  explain: "Explain why the DOM matters for JavaScript — what would be impossible without it?"
  mistakeId:
    code: "JavaScript changes a CSS file to update the page's appearance."
    answer: "JavaScript does not modify CSS files — it modifies the DOM and/or the element's style properties or class list. The CSS file remains unchanged; JavaScript interacts with the in-memory DOM representation."
---

# Hook

HTML defines what is on a page. But how does JavaScript *reach into* that page and change it?

When the browser loads an HTML file, it does not just paint the screen — it builds a live, structured model of the page in memory. This model is called the DOM, and it is the API through which JavaScript communicates with the page.

Understanding the DOM is understanding the interface between code and browser. Everything in front-end development — event listeners, content updates, animations — goes through it.

> When you click a button and a dropdown appears without the page reloading, what do you think is happening behind the scenes?

# Lore Introduction

Master Aelindra gestures to a large diagram pinned to the workshop wall — a branching tree drawn in glowing ink, with `document` at the top and dozens of nodes descending from it.

*"Every page you will ever enchant is represented twice,"* she says. *"Once as the parchment the user sees. And once as this tree — a living model in the browser's memory, where every element is a node you can reach out and touch."*

She places her hand on a node labelled `<p>`.

*"Change this node, and the parchment changes. Add a child here, and new content appears. Remove a branch, and it vanishes. The tree is the page. The page is the tree."*

# Core Learning

## Concept Introduction

The **Document Object Model (DOM)** is the browser's in-memory, tree-structured representation of an HTML document.

When the browser parses your HTML, it creates a tree of **nodes**:

```
document
└── html
    ├── head
    │   └── title (text: "My Page")
    └── body
        ├── h1 (text: "Hello")
        └── p (text: "Welcome!")
```

| Concept | Meaning |
|---------|---------|
| **Node** | Every item in the DOM tree (elements, text, attributes) |
| **Element node** | An HTML element like `<p>` or `<div>` |
| **Text node** | The text inside an element |
| **document** | The root object — JavaScript's entry point to the DOM |
| **Parent/Child** | A node that contains another (body is parent of p) |

## Why It Matters

Without the DOM, JavaScript would have no way to interact with the page. The DOM is the contract between the browser and your code. Every `getElementById`, `querySelector`, `textContent` change, and event listener goes through it.

The DOM is also **live**: when JavaScript modifies a node, the browser immediately reflects that change in the rendered page — no reload required.

## Worked Examples

**Example 1 — Accessing the document**

```js
console.log(document.title);  // The page's <title> text
console.log(document.body);   // The <body> element object
```

**Example 2 — The tree structure in JavaScript**

```js
// Given: <div id="box"><p>Hello</p></div>
const div = document.getElementById("box");
console.log(div.children[0]);       // <p> element
console.log(div.children[0].textContent); // "Hello"
```

**Example 3 — DOM changes appear on screen**

```js
document.body.style.backgroundColor = "lightblue";
// The page background immediately turns light blue
```

JavaScript modifies the DOM node's style property; the browser re-renders the affected element.

## Common Mistakes

- Thinking HTML files are modified when JavaScript changes the page — they are not; only the in-memory DOM changes
- Running JavaScript before the DOM is ready — scripts in `<head>` may run before `<body>` is parsed
- Confusing the DOM with the source HTML — they start the same but can diverge as JavaScript modifies the DOM
- Treating every node as an element — text inside elements is also a node (a text node)

## Mental Model

Imagine the HTML file as a blueprint for a building.

The DOM is the actual building — constructed from the blueprint, standing in memory. You can knock out a wall (remove a node), add a room (append an element), or repaint a surface (change a style) without changing the original blueprint.

When you revisit the browser and reload, the building is reconstructed from the original blueprint again.

## Mini Summary

- The DOM is the browser's tree-structured model of the HTML page
- Every HTML element becomes a node in the DOM tree
- JavaScript accesses the DOM through the `document` global object
- DOM changes appear immediately in the rendered page
- The DOM is live and in-memory — the HTML file itself is never changed

# Guided Practice Quest

In this quest you will identify what DOM stands for, describe the tree structure, and explain the relationship between DOM changes and the rendered page.

These three conceptual steps prepare you to start selecting elements and modifying them in the next lessons.

# Solo Practice Quest

In a browser tab, open Developer Tools (F12) and go to the Elements panel. Examine the DOM tree of any webpage you have open.

Write 4–5 sentences describing:
- The tree structure you can see (what nests inside what)
- What happens to the Elements panel when you manually edit a node's text in DevTools
- How this differs from editing the original HTML file

# Integration

**Connecting to Mathematics — Tree Structures**

The DOM is a **tree** — a mathematical data structure where each node has exactly one parent (except the root, which has none) and may have many children. Trees appear throughout computer science: file systems, decision trees, syntax trees, and more.

A key property of trees is that they can be traversed systematically — you can visit every node by working from the root downward. This is how browsers render pages (top-down traversal) and how search engines index content. Understanding the DOM as a tree helps you reason about operations like "find all `<a>` elements on this page" — which is a tree traversal.

# Lore Conclusion

The apprentice studies the diagram on the wall, tracing a path from `document` down through `html`, `body`, and into a `<p>` node.

*"Every enchantment you make to a page flows through this tree,"* Master Aelindra says. *"Now that you understand its shape, you are ready to learn how to reach into it."*

The glowing diagram pulses gently, waiting to be touched.

---
