---
id: fe-app-m2-01
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m2
moduleTitle: "Module 2: HTML Foundations"
moduleGlyph: "📄"
moduleSortOrder: 2
topicSlug: html_basics
topicTitle: "HTML Basics"
topicSortOrder: 1
lesson: what_is_html
title: "What is HTML?"
sortOrder: 1
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m1-09]
integrationDomains: [history, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines HTML and what the acronym stands for"
    - "Explains what markup means"
    - "Describes the relationship between HTML, CSS, and JavaScript"
    - "Explains that HTML provides structure and meaning, not appearance"
    - "Gives examples of what HTML describes"
  keywords: [html, markup, structure, element, tag, content, hypertext, language, browser, css, javascript]
  modelAnswer: |
    HTML stands for HyperText Markup Language. It is the language used to structure content on the web.
    Markup means annotating content with tags that describe what the content is (a heading, a paragraph,
    a list, a link). HTML is not about appearance — that is CSS's job. HTML gives the browser a structured
    description of the content. JavaScript adds behaviour. Together, HTML, CSS, and JavaScript form the
    three layers of frontend development.
guidedSteps:
  - id: fe-app-m2-01-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does HTML stand for?
    inputConfig:
      options:
        - "High-Tech Markup Library"
        - "HyperText Markup Language"
        - "HyperText Management Language"
        - "Home Tool Markup Language"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["HyperText Markup Language"]
      rejectedFeedback: "HTML = **HyperText Markup Language**. 'HyperText' refers to text with hyperlinks — text that links to other text. 'Markup' refers to annotating content with tags. 'Language' because it follows defined rules."
    hint: "The H stands for Hyper, T for Text, M for Markup."
    reflectionPrompt: "The 'HyperText' in HTML is historically significant — the web's defining feature was documents that link to other documents. Tim Berners-Lee's original vision was a global information system connected by hyperlinks. That's still what HTML enables at its core."

  - id: fe-app-m2-01-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "In the three-layer model of the web, HTML provides ___, CSS provides presentation, and JavaScript provides behaviour."
    inputConfig:
      placeholder: "structure"
    markingRule:
      matchMode: CONTAINS
      accepted: [structure, "structure and meaning", meaning, content]
      rejectedFeedback: "HTML provides **structure** (and meaning). CSS handles how things look. JavaScript handles what things do. Keeping these concerns separate is a fundamental principle of clean frontend development."
    hint: "HTML describes what content IS, not how it looks."
    reflectionPrompt: "Separation of concerns — keeping structure, presentation, and behaviour in separate layers — is a principle that makes code maintainable. When these layers mix (inline styles, JavaScript-generated HTML), code becomes harder to read, test, and change."

  - id: fe-app-m2-01-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 2–3 sentences why a webpage without CSS still has structure and meaning, but a webpage without HTML would have nothing at all.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [html, structure, content, css, style, meaning, foundation]
      rejectedFeedback: "HTML is the foundation — it defines the content and its structure. CSS only changes how that content looks. Without HTML, there is no content to style. A plain HTML page without CSS is unstyled but still readable and meaningful."
    hint: "Think about which layer is fundamental and which is additive."
    reflectionPrompt: "A useful mental test: if you stripped CSS from a page, would users still be able to read and navigate it? If yes, the HTML is well-structured. If the page becomes completely unusable without CSS, the HTML is likely depending on styles for meaning."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of the three web layers is responsible for making a button red?"
    options:
      - "HTML — through element attributes"
      - "CSS — which handles presentation and appearance"
      - "JavaScript — which controls all visual properties"
      - "HTTP — which sends style information"
    correctIndex: 1
    feedback: "Colour is a presentation concern — CSS's job. HTML should describe *what* something is (a button), not *how* it looks. JavaScript can change styles dynamically, but static visual properties belong in CSS."
  - type: MULTIPLE_CHOICE
    question: "Tim Berners-Lee invented HTML as part of which broader system?"
    options:
      - "The Internet"
      - "The World Wide Web"
      - "The Domain Name System"
      - "The TCP/IP protocol"
    correctIndex: 1
    feedback: "Tim Berners-Lee invented the World Wide Web in 1989–1991 at CERN. HTML was the language he created to write hyperlinked documents. HTTP was the protocol to transfer them. The Internet itself predates the Web by decades."

retrieval:
  recall: "What does the acronym HTML stand for, and what does each word mean?"
  explain: "Why is it good practice to keep HTML, CSS, and JavaScript in separate files rather than mixing them?"
  mistakeId:
    code: "HTML controls how a page looks — if you want something bold, you use HTML"
    answer: "HTML describes structure and meaning, not appearance. The `<b>` and `<strong>` tags exist but best practice is to use CSS (`font-weight: bold`) for visual styling. HTML should describe what content IS; CSS should describe how it looks. Mixing them creates maintenance problems."
---

# Hook

Every page you have ever visited on the web — from a simple blog post to a complex dashboard — started as HTML.

Not JavaScript. Not CSS. HTML.

It is the foundational language of the web. Everything else sits on top of it. Understanding HTML means understanding the raw material that every frontend engineer works with.

And yet HTML is often dismissed as "not real programming." This misunderstands its role. HTML is not about logic — it is about meaning. And meaning is the foundation of everything accessible, searchable, and structurally sound.

> Before reading on: what do you think the word "markup" means? Write your definition.

# Lore Introduction

In the Academy's great archive, every scroll is annotated before it is shelved.

A scroll about the realm's history gets annotated: *"This is a title. This is a chapter. This is an important warning. This is a footnote."* The annotations don't change the words — they describe what the words mean. When a scholar arrives, they can navigate the archive correctly.

*"This is what HTML does,"* says Master Aelindra. *"It annotates content. It tells the browser — and every system that reads your page — what kind of content each part is."*

She opens a fresh scroll.

*"Before you draw a single line or apply a single colour, you describe. That is the art of markup."*

# Core Learning

## Concept Introduction

**HTML** stands for **HyperText Markup Language**.

- **HyperText** — text that links to other text (hyperlinks)
- **Markup** — annotating content with descriptive tags
- **Language** — a defined set of rules for doing so

HTML is the language used to give web content structure and meaning. It tells the browser (and assistive technologies, and search engines) what each piece of content is:

```html
<h1>This is a main heading</h1>
<p>This is a paragraph of text.</p>
<a href="/about">This is a hyperlink</a>
<img src="logo.png" alt="Company logo">
```

### The Three Layers of the Web

| Layer | Technology | Responsibility |
|---|---|---|
| **Structure** | HTML | What content exists and what it is |
| **Presentation** | CSS | How it looks |
| **Behaviour** | JavaScript | What it does when interacted with |

These layers work together but should remain separated. Mixing them creates code that is hard to maintain, test, and understand.

### A Brief History

HTML was invented by **Tim Berners-Lee** in 1989–1991 at CERN, as part of his proposal for the World Wide Web. His original goal was a system for scientists to share documents and link between them. HTML has evolved through multiple versions; the current version is **HTML5**, standardised by the WHATWG.

## Why It Matters

HTML is the foundation. You cannot build a web page without it. CSS and JavaScript enhance HTML — they cannot replace it. Understanding HTML means:
- Writing code that is accessible (screen readers read your HTML)
- Writing code that is searchable (search engines index your HTML)
- Writing code that is maintainable (clean structure enables clean styling)
- Writing code that works when CSS fails to load or JavaScript is disabled

## Worked Examples

**Example 1 — A simple HTML page:**
```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>My First Page</title>
  </head>
  <body>
    <h1>Welcome</h1>
    <p>This is my first web page.</p>
  </body>
</html>
```

**Example 2 — The same content, wrong approach:**
```html
<div style="font-size: 24px; font-weight: bold;">Welcome</div>
<div>This is my first web page.</div>
```

Both render similarly in a browser. But the first uses meaningful HTML (`<h1>`, `<p>`). The second uses `<div>` for everything — no meaning, no structure, not accessible.

## Common Mistakes

- **Using HTML for appearance.** That's CSS's job. Use `<strong>` for emphasis, not to make text bold visually.
- **Using `<div>` for everything.** Divs have no semantic meaning. Use appropriate elements (`<header>`, `<nav>`, `<p>`, `<h1>`) when they apply.
- **Thinking HTML is "not real programming."** HTML's job is to represent structure and meaning. Doing this well requires skill and knowledge.

## Mental Model

Think of HTML as the **architectural blueprint** of a building:
- It describes what rooms exist and what they are for
- It doesn't specify the paint colour (CSS) or what happens when you press a light switch (JavaScript)
- A building without a blueprint is chaotic — a page without solid HTML is fragile

## Mini Summary

- HTML = HyperText Markup Language — describes content structure and meaning
- Three layers of the web: HTML (structure), CSS (presentation), JavaScript (behaviour)
- Invented by Tim Berners-Lee in 1989–1991, current version is HTML5
- HTML is the foundation: accessible, searchable, maintainable
- Tags describe what content IS, not how it looks

# Guided Practice Quest

**The Archive Annotator**

The Academy's archive has acquired new scrolls that need annotating. Apprentices must identify the correct HTML approach for each piece of content.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Take any article from a news website. Read it carefully and write an HTML outline of its structure — just the tags and a short description of the content, not the full text. Include at minimum: the page title, the article headline, at least two sections with subheadings, a paragraph, a list (if applicable), and a link.

Then, in 2–3 sentences, explain your structural choices.

# Integration

**Connecting to History — The Invention of the Web and the Decision to Make it Open**

Tim Berners-Lee invented the World Wide Web at CERN in 1989–1991. He deliberately chose not to patent HTML or HTTP — he released them as open standards that anyone could implement for free.

This decision had enormous consequences. An open standard created a universal platform. Any browser could render HTML. Any server could speak HTTP. No single company owned the web. This openness drove the explosive growth of the internet as a public resource.

The contrast with proprietary systems (where formats are locked to specific vendors) is instructive. HTML's openness means your content is readable in any browser, on any device, for decades to come. Proprietary formats can be abandoned, locked behind fees, or broken when a vendor changes direction.

What does this history suggest about the relationship between open standards and the resilience of technical systems?

# Lore Conclusion

The first scroll is annotated. Every heading marked. Every paragraph identified. Every link labelled.

*"You have taken raw content and given it structure,"* says Master Aelindra. *"The browser can now understand it. The search engine can index it. The screen reader can navigate it."*

She adds a rune to the HTML path.

*"Now we learn the vocabulary. Elements and tags — the building blocks of everything on the web."*
