---
id: fe-app-m1-06
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m1
moduleTitle: "Module 1: Understanding the Web"
moduleGlyph: "🌐"
moduleSortOrder: 1
topicSlug: browsers
topicTitle: "Browsers"
topicSortOrder: 2
lesson: what_does_a_browser_do
title: "What Does a Browser Do?"
sortOrder: 1
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m1-05]
integrationDomains: [psychology, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Lists the main responsibilities of a browser"
    - "Explains what a browser engine is"
    - "Describes the difference between major browser engines"
    - "Explains why cross-browser differences matter to frontend engineers"
    - "Uses correct terminology (engine, render, DOM, JavaScript engine)"
  keywords: [browser, engine, render, dom, javascript, blink, gecko, webkit, html, css]
  modelAnswer: |
    A browser is a software application that requests web resources via HTTP, parses and renders
    HTML/CSS into visual pages, executes JavaScript, and manages user interactions. At its core is
    a rendering engine (e.g. Blink in Chrome, Gecko in Firefox, WebKit in Safari) that turns markup
    into a visual display. Browsers also include a JavaScript engine (e.g. V8 in Chrome) that executes
    code. Cross-browser differences arise because different engines implement web standards slightly
    differently, requiring frontend engineers to test across browsers.
guidedSteps:
  - id: fe-app-m1-06-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of the following is NOT a primary responsibility of a web browser?
    inputConfig:
      options:
        - "Parsing HTML and constructing the DOM"
        - "Executing JavaScript"
        - "Storing user data in a database on the server"
        - "Rendering a visual page from HTML and CSS"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Storing user data in a database on the server"]
      rejectedFeedback: "Browsers parse HTML, execute JavaScript, and render pages — all locally. Storing data in a server database is a backend concern. Browsers may store data locally (cookies, localStorage), but not in remote databases."
    hint: "Which of these happens on the SERVER, not in the browser?"
    reflectionPrompt: "Understanding what happens in the browser vs on the server is fundamental. Frontend engineers control what happens in the browser. Backend engineers control what happens on the server. The boundary is the HTTP response."

  - id: fe-app-m1-06-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "Chrome and Edge use the ___ rendering engine, Firefox uses Gecko, and Safari uses WebKit."
    inputConfig:
      placeholder: "Blink"
    markingRule:
      matchMode: CONTAINS
      accepted: [Blink, blink]
      rejectedFeedback: "**Blink** is Google's fork of WebKit, used in Chrome, Edge, Opera, and most Chromium-based browsers. Firefox uses **Gecko**. Safari uses **WebKit**. These differences mean the same CSS can render differently across browsers."
    hint: "Google forked WebKit to create this engine for Chrome."
    reflectionPrompt: "Browser engine fragmentation is why frontend engineers test in multiple browsers. A feature that works in Chrome may behave differently in Safari (WebKit) or Firefox (Gecko). This is especially true for newer CSS features."

  - id: fe-app-m1-06-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 2–3 sentences why frontend engineers need to test their code in multiple browsers, even though they only write one codebase.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [engine, browser, render, standard, support, different, css, javascript]
      rejectedFeedback: "Different browsers use different rendering engines that implement web standards with varying degrees of support and subtle differences. A feature available in Chrome may not exist in Safari. CSS that renders correctly in Firefox may look different in Chrome."
    hint: "Think about why different rendering engines lead to different results for the same code."
    reflectionPrompt: "Cross-browser testing is not optional for professional frontend engineers. Tools like BrowserStack, MDN's compatibility tables, and Can I Use help identify where issues might arise before users encounter them."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is V8?"
    options:
      - "The version of HTTP used by Chrome"
      - "Chrome's JavaScript engine"
      - "A CSS rendering standard"
      - "The browser storage API"
    correctIndex: 1
    feedback: "V8 is Google's JavaScript engine, used in Chrome and Node.js. It compiles JavaScript to machine code for fast execution. Firefox uses SpiderMonkey. Safari uses JavaScriptCore. The engine is separate from the rendering engine."
  - type: MULTIPLE_CHOICE
    question: "When a browser receives an HTTP response containing HTML, what is the first thing it does?"
    options:
      - "Executes all JavaScript on the page"
      - "Applies all CSS styles"
      - "Parses the HTML to construct the DOM"
      - "Renders the pixels to the screen"
    correctIndex: 2
    feedback: "The browser parses HTML first to build the DOM tree. Then it parses CSS to build the CSSOM. Then it combines them into the render tree, calculates layout, and paints pixels. JavaScript execution can happen at various stages, which is why script placement matters."

retrieval:
  recall: "Name three things a browser does between receiving an HTTP response and displaying a page."
  explain: "Why might a CSS feature that works in Chrome not work in Safari?"
  mistakeId:
    code: "All modern browsers work the same way because they all implement the same web standards"
    answer: "Browsers implement the same standards but differ in their level of support and interpretation. Different rendering engines (Blink, Gecko, WebKit) and JavaScript engines (V8, SpiderMonkey, JavaScriptCore) can handle edge cases differently. Newer CSS features in particular have varying support across browsers."
---

# Hook

You type a URL. Press Enter. In under a second, a fully rendered, interactive page appears.

But what happened in that second? Your browser received a text file — just characters — and turned it into pixels, interactive elements, animations, and functioning code.

The browser is the most complex piece of software most people use every day, and most people have no idea what it actually does.

As a frontend engineer, it is your primary deployment environment. Understanding it is not optional.

> Close your eyes and try to list everything a browser must do to show a web page. How many steps can you think of?

# Lore Introduction

Apprentices gather in the Academy's observatory, where a crystal lens transforms raw light into detailed images.

*"The browser is like this lens,"* says Master Aelindra. *"It takes raw materials — HTML, CSS, JavaScript — and transforms them into something a person can see and interact with."*

She gestures to different parts of the lens — each section performs a different transformation.

*"The rendering engine is one facet. The JavaScript engine is another. The network layer is another. Together, they perform something remarkable — turning text files into living pages."*

# Core Learning

## Concept Introduction

A **browser** is a software application that:
1. Requests web resources via HTTP/HTTPS
2. Parses HTML into the Document Object Model (DOM)
3. Parses CSS into the CSS Object Model (CSSOM)
4. Executes JavaScript
5. Renders a visual page
6. Manages user interactions

### Major Browser Engines

| Browser | Rendering Engine | JavaScript Engine |
|---|---|---|
| Chrome, Edge, Opera | Blink | V8 |
| Firefox | Gecko | SpiderMonkey |
| Safari | WebKit | JavaScriptCore |

An **engine** is the core software component that implements a specific function. The rendering engine turns HTML/CSS into a visual display. The JavaScript engine compiles and executes JS code.

### The DOM

When a browser parses HTML, it creates the **Document Object Model (DOM)** — a tree structure representing the page. Each HTML element becomes a node in the tree. JavaScript can read and manipulate this tree, which is how dynamic pages work.

```
Document
  └── html
       ├── head
       │    └── title
       └── body
            ├── h1
            └── p
```

### The Browser's Responsibilities

| Responsibility | What it does |
|---|---|
| **Networking** | Makes HTTP requests, handles responses |
| **HTML parsing** | Builds the DOM from HTML text |
| **CSS parsing** | Builds the CSSOM from CSS text |
| **JavaScript execution** | Runs JS code via the JS engine |
| **Rendering** | Calculates layout and paints pixels |
| **Storage** | Manages cookies, localStorage, sessionStorage |
| **Security** | Enforces same-origin policy, HTTPS warnings |

## Why It Matters

Frontend engineers write code that runs inside browsers. Understanding what the browser does — and how it works — helps you:
- Write HTML and CSS that renders correctly across browsers
- Debug rendering issues by understanding the rendering pipeline
- Understand JavaScript's relationship to the DOM
- Optimise performance by knowing what the browser finds expensive
- Write accessible code that browser-based assistive technologies can understand

## Worked Examples

**Example 1 — The critical rendering path:**
1. Browser receives HTML → parses it → builds DOM
2. Encounters `<link rel="stylesheet">` → fetches CSS → builds CSSOM
3. Combines DOM + CSSOM → Render Tree
4. Calculates layout (size/position of each element)
5. Paints pixels to screen

**Example 2 — JavaScript blocks rendering:**
```html
<script src="analytics.js"></script>
<!-- Everything below this is blocked until analytics.js loads and executes -->
<h1>This won't show until the script finishes</h1>
```
This is why you often see scripts at the bottom of `<body>` or with `defer` attribute — to avoid blocking the rendering pipeline.

## Common Mistakes

- **Assuming all browsers work the same.** They don't. Test in Chrome, Firefox, and Safari at minimum.
- **Thinking JavaScript and the DOM are the same thing.** JavaScript is a language. The DOM is a browser API. They work together but are separate concepts.
- **Not considering performance of DOM manipulation.** Each change to the DOM can trigger layout recalculation and repainting — expensive operations. Batch your changes.

## Mental Model

Think of the browser as a **printing press**:
- The manuscript (HTML) arrives
- The typesetter (HTML parser) arranges the type
- The artist (CSS engine) adds colours and formatting
- The press operator (JavaScript engine) makes last-minute changes on request
- The press (render engine) produces the final printed page
- Different presses (browser engines) may produce slightly different outputs from the same manuscript

## Mini Summary

- A browser requests web resources, parses HTML/CSS, executes JavaScript, and renders pages
- Major rendering engines: Blink (Chrome/Edge), Gecko (Firefox), WebKit (Safari)
- The DOM is the browser's internal tree representation of the HTML document
- JavaScript interacts with the DOM to create dynamic behaviour
- Frontend engineers must test across browsers because engines differ

# Guided Practice Quest

**The Lens Calibrators**

The Academy's crystal lenses need calibration. Apprentices must identify which component of the browser handles each task.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Research the browser you use most often. Write a short profile (4–6 sentences) that includes:
1. Its rendering engine and JavaScript engine
2. One CSS feature it supports that another major browser doesn't (or vice versa)
3. One feature of its developer tools you find useful (or would find useful)
4. Its current market share among web users

Use MDN Web Docs and Can I Use as research sources.

# Integration

**Connecting to Sciences — The Eye and the Browser: Parallel Architectures**

The human visual system and the browser rendering pipeline share a remarkable structural similarity. Both receive raw input (light / HTML+CSS), process it through distinct stages (retinal cells, visual cortex layers / parsing, layout, paint), and produce a unified perception or image.

The eye's process: photons hit retinal cells → signals travel via the optic nerve → visual cortex processes edges, colour, motion → conscious perception of a scene.

The browser's process: bytes arrive → parser builds structures → layout engine calculates positions → paint engine produces pixels → display hardware renders the image.

Both systems must perform this process in real time, handling constant updates (eye movements / DOM changes) while maintaining a stable, coherent output.

What does this architectural parallel suggest about the engineering constraints that arise when building systems that must produce visual output in real time?

# Lore Conclusion

The lens is clean. The transformation is clear.

*"The browser is not a black box,"* says Master Aelindra. *"It is a precise machine with defined components, a defined pipeline, and defined constraints. When your page doesn't look right, you know where to look."*

A new rune joins the Frontend path.

*"Next: we go deeper into the rendering pipeline. How does the browser turn your HTML and CSS into pixels? Understanding this changes how you write code."*
