---
id: fe-app-m2-07
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m2
moduleTitle: "Module 2: HTML Foundations"
moduleGlyph: "📄"
moduleSortOrder: 2
topicSlug: content_elements
topicTitle: "Content Elements"
topicSortOrder: 2
lesson: lists
title: "Lists"
sortOrder: 3
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m2-06]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Distinguishes between `<ul>`, `<ol>`, and `<dl>` with correct use cases"
    - "Explains the `<li>` element and its role"
    - "Explains when order matters (ordered vs unordered)"
    - "Describes description lists and their components"
    - "Notes that navigation menus are `<ul>` lists"
  keywords: [ul, ol, li, dl, dt, dd, list, ordered, unordered, description, navigation, nested]
  modelAnswer: |
    HTML provides three list types. `<ul>` (unordered list) is for items where order doesn't matter —
    it renders with bullet points. `<ol>` (ordered list) is for items where sequence matters — numbered.
    Both use `<li>` (list item) for each item. `<dl>` (description list) pairs terms (`<dt>`) with
    descriptions (`<dd>`) — used for glossaries and key-value data. Lists can be nested. Navigation
    menus are semantically `<ul>` lists, often without default bullet styling via CSS.
guidedSteps:
  - id: fe-app-m2-07-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You are marking up a recipe's method — 8 steps that must be followed in sequence. Which list type should you use?
    inputConfig:
      options:
        - "`<ul>` — because recipes use bullet points"
        - "`<ol>` — because the steps must be followed in a specific order"
        - "`<dl>` — because each step describes an action"
        - "`<p>` — because each step is a sentence"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["`<ol>` — because the steps must be followed in a specific order"]
      rejectedFeedback: "`<ol>` is correct for ordered sequences where the numbering matters. Recipe steps must be followed in order — step 2 cannot come before step 1. Visually you can style bullets however you like via CSS, but semantically `<ol>` communicates that order is meaningful."
    hint: "Does the sequence matter? Which list type conveys that?"
    reflectionPrompt: "Choosing `<ul>` vs `<ol>` is a semantic decision, not a visual one. Both can be styled identically with CSS. The choice communicates whether order is meaningful to screen readers, search engines, and other developers reading your code."

  - id: fe-app-m2-07-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "In a `<dl>` (description list), `<dt>` marks a ___, and `<dd>` marks its description or value."
    inputConfig:
      placeholder: "term"
    markingRule:
      matchMode: CONTAINS
      accepted: [term, "definition term", name]
      rejectedFeedback: "`<dt>` marks a **term** (or name, or key) and `<dd>` marks its description or definition. Description lists are ideal for: glossaries, metadata displays (author: Jane, published: 2026), and any key-value style content."
    hint: "The 't' in `<dt>` stands for this word."
    reflectionPrompt: "Description lists are underused. Many developers reach for a table for key-value data when a `<dl>` would be more semantically appropriate. If you have pairs of names and values, consider `<dl>`."

  - id: fe-app-m2-07-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A navigation menu contains five links: Home, About, Services, Blog, Contact. Which HTML list type would you use, and why? How would you remove the bullet points visually?
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [ul, unordered, css, list-style, nav, navigation, order, semantic]
      rejectedFeedback: "`<ul>` — navigation items don't have a meaningful order (the order is layout convention, not semantic sequence). To remove bullets: `list-style: none` in CSS. Most navigation patterns combine `<nav>`, `<ul>`, and `<li>` with `<a>` inside each item."
    hint: "Is there a meaningful order to the navigation items? Which CSS property removes bullets?"
    reflectionPrompt: "The `<nav>` + `<ul>` + `<li>` + `<a>` pattern is standard for navigation. Screen readers understand `<nav>` as a landmark and `<ul>` as a list. This gives keyboard and screen reader users correct context for navigating."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What element is always used as a direct child of `<ul>` or `<ol>`?"
    options:
      - "`<p>`"
      - "`<span>`"
      - "`<li>`"
      - "`<div>`"
    correctIndex: 2
    feedback: "`<li>` (list item) is the required direct child of `<ul>` and `<ol>`. Any other element as a direct child of a list is invalid HTML. Content goes inside `<li>`, and `<li>` goes inside `<ul>` or `<ol>`."
  - type: MULTIPLE_CHOICE
    question: "You have a page with a list of programming languages and a description of each. Which list type fits best?"
    options:
      - "`<ul>` — because the order doesn't matter"
      - "`<ol>` — because you should order them by popularity"
      - "`<dl>` — because you have term-description pairs"
      - "`<table>` — because you have two columns"
    correctIndex: 2
    feedback: "`<dl>` is ideal for term-description pairs. Each language name is a `<dt>`; its description is a `<dd>`. This is more semantically appropriate than `<ul>` (which implies simple items) or `<table>` (which implies tabular data with headers)."

retrieval:
  recall: "Name the three HTML list types, their elements, and give one appropriate use case for each."
  explain: "Why is a navigation menu typically marked up as a `<ul>` rather than `<ol>`?"
  mistakeId:
    code: "I use `<ol>` when I want numbered items and `<ul>` when I want bullets — the choice is visual"
    answer: "The choice between `<ol>` and `<ul>` is semantic, not visual. `<ol>` means sequence matters. `<ul>` means order is not significant. CSS controls the visual bullets or numbers. A `<ul>` can display with numbers; an `<ol>` can display with bullets. Choose based on meaning."
---

# Hook

Lists are everywhere on the web — navigation menus, feature lists, step-by-step instructions, search results, comments, product options.

HTML provides three types of lists. Choosing the right one is not a visual decision — it's a semantic one. And getting it wrong has consequences for accessibility, search engines, and the developers who maintain your code.

> Before reading on: what three types of lists do you think HTML supports?

# Lore Introduction

In the Academy's storerooms, inventories come in three forms: unordered manifests (items in no particular sequence), ordered procedures (steps that must be followed exactly), and definition registers (pairs of terms and their descriptions).

*"Use the wrong register,"* says Master Aelindra, *"and the wrong people get the wrong items, in the wrong order, with the wrong descriptions."*

She holds up a recipe written as an unordered list.

*"Step 3 before step 1. Does this work?"*

The apprentices agree: it does not.

*"Then it belongs in an ordered list. The choice is not aesthetic. It is semantic."*

# Core Learning

## Concept Introduction

### Unordered List — `<ul>`

For items where order is not meaningful. Rendered with bullet points by default.

```html
<ul>
  <li>HTML</li>
  <li>CSS</li>
  <li>JavaScript</li>
</ul>
```

Use for: shopping lists, feature lists, navigation menus, tags, any collection where sequence is irrelevant.

### Ordered List — `<ol>`

For items where sequence matters. Rendered with numbers by default.

```html
<ol>
  <li>Preheat oven to 180°C</li>
  <li>Mix dry ingredients</li>
  <li>Add wet ingredients and combine</li>
  <li>Bake for 25 minutes</li>
</ol>
```

Use for: recipe steps, installation instructions, ranked lists, any sequence where order has meaning.

### Description List — `<dl>`

For term-description pairs (key-value style data).

```html
<dl>
  <dt>HTML</dt>
  <dd>HyperText Markup Language — the structure layer of the web</dd>
  <dt>CSS</dt>
  <dd>Cascading Style Sheets — the presentation layer</dd>
</dl>
```

Use for: glossaries, metadata displays, FAQs (term = question, description = answer), configuration documentation.

### Nested Lists

Lists can be nested — place a new list inside an `<li>`:

```html
<ul>
  <li>Frontend Technologies
    <ul>
      <li>HTML</li>
      <li>CSS</li>
    </ul>
  </li>
  <li>Backend Technologies</li>
</ul>
```

### Navigation Menus

Navigation is semantically `<ul>` (unordered — the order of nav items has no inherent meaning):

```html
<nav>
  <ul>
    <li><a href="/">Home</a></li>
    <li><a href="/about">About</a></li>
    <li><a href="/blog">Blog</a></li>
  </ul>
</nav>
```

CSS removes bullets: `list-style: none; padding: 0; margin: 0;`

## Why It Matters

Screen readers announce list type and count: "List, 5 items." Users know what to expect. Incorrect list types (ordered when unordered, or no list at all) deprive users of this context. Navigation menus without `<ul>` structure are harder to navigate via keyboard and assistive technology.

## Worked Examples

**Example 1 — `<ol>` for instructions:**
```html
<h2>Setting Up Your Development Environment</h2>
<ol>
  <li>Install Node.js from the official website</li>
  <li>Open your terminal and verify: <code>node --version</code></li>
  <li>Install your preferred code editor</li>
</ol>
```

**Example 2 — `<dl>` for metadata:**
```html
<dl>
  <dt>Author</dt>
  <dd>Jane Smith</dd>
  <dt>Published</dt>
  <dd>4 June 2026</dd>
  <dt>Reading time</dt>
  <dd>8 minutes</dd>
</dl>
```

## Common Mistakes

- **Using `<ol>` for visual numbering when order isn't semantic.** Order semantics matter.
- **Putting block elements directly in `<ul>`/`<ol>` without `<li>`.** Invalid HTML.
- **Not using lists for navigation.** Navigation menus are lists of links.
- **Using `<dl>` for definitions only.** It works for any key-value paired content.

## Mental Model

Three list types for three real-world needs:
- **`<ul>`** = grocery bag (items in any order)
- **`<ol>`** = assembly instructions (must follow sequence)
- **`<dl>`** = dictionary (paired terms and definitions)

## Mini Summary

- `<ul>` for unordered items, `<ol>` for ordered sequences, `<dl>` for term-description pairs
- `<li>` is the required child of `<ul>` and `<ol>`; `<dt>` and `<dd>` are `<dl>`'s children
- Lists can be nested
- Navigation menus use `<nav>` + `<ul>` + `<li>` + `<a>`
- The choice is semantic, not visual — CSS controls the appearance

# Guided Practice Quest

**The Inventory Classifier**

The Academy's storerooms have three types of registers. Classify each inventory and write the correct HTML list type for it.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Build the HTML structure for a documentation sidebar that contains:
1. A main navigation (`<nav>`) with 5 links (unordered)
2. A "Quick Steps" section with 4 ordered installation steps
3. A "Glossary" section with 3 term-description pairs

Use correct list types for each section. Style is not required — focus entirely on correct semantic structure.

# Integration

**Connecting to Mathematics — Sets, Sequences, and Ordered Pairs**

The three list types correspond directly to three fundamental mathematical structures:
- `<ul>` corresponds to a **set** — an unordered collection of distinct elements
- `<ol>` corresponds to a **sequence** — an ordered collection where position matters
- `<dl>` corresponds to a **relation** or **function** — pairs of inputs and outputs

Mathematics distinguishes carefully between sets and sequences: {A, B, C} and [A, B, C] are different because one preserves order and one doesn't. HTML makes the same distinction semantically.

When you choose `<ol>` over `<ul>`, you are asserting that the ordering is part of the data's meaning — the same assertion a mathematician makes when using sequence notation instead of set notation.

What does this connection suggest about how data structures in programming relate to mathematical abstractions?

# Lore Conclusion

The storerooms are organised. Unordered manifests are `<ul>`. Procedures are `<ol>`. Definition registers are `<dl>`.

*"Structure enables navigation,"* says Master Aelindra. *"A list is not just a visual grouping. It is a promise: these items are related, here is how, and this is whether their order matters."*

A rune settles.

*"Next: images. The most visually important element — and the one with the most performance implications."*
