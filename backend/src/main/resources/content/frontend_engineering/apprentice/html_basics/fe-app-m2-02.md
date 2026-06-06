---
id: fe-app-m2-02
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
lesson: elements_and_tags
title: "Elements and Tags"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m2-01]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Distinguishes between a tag and an element"
    - "Identifies opening and closing tags correctly"
    - "Names at least two void (self-closing) elements"
    - "Explains nesting and the importance of correct nesting"
    - "Describes what it means for elements to have block vs inline display"
  keywords: [element, tag, opening, closing, void, self-closing, nesting, block, inline, html]
  modelAnswer: |
    A tag is the markup syntax (e.g. `<p>`). An element is the complete unit: opening tag, content,
    and closing tag. Void elements (e.g. `<img>`, `<br>`, `<input>`) have no content and no closing tag.
    Elements can be nested to build structure, but must be properly nested — overlapping tags create
    invalid HTML. Block elements (e.g. `<p>`, `<div>`, `<h1>`) start on a new line and take full width.
    Inline elements (e.g. `<span>`, `<a>`, `<strong>`) flow within text.
guidedSteps:
  - id: fe-app-m2-02-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the difference between a **tag** and an **element**?
    inputConfig:
      options:
        - "They are the same thing — 'tag' and 'element' are interchangeable"
        - "A tag is just the angle-bracket syntax; an element is the complete unit of opening tag, content, and closing tag"
        - "An element is always visible; a tag is always invisible"
        - "Tags are for structure; elements are for styling"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A tag is just the angle-bracket syntax; an element is the complete unit of opening tag, content, and closing tag"]
      rejectedFeedback: "A **tag** is the angle-bracket syntax: `<p>`. An **element** is the complete unit: `<p>This is content.</p>`. Some developers use these interchangeably in conversation, but the precise distinction matters for understanding the DOM and debugging."
    hint: "One is the brackets; the other is everything from open to close."
    reflectionPrompt: "Precision in terminology matters in engineering. When you talk about DOM nodes, you're talking about elements. When you write `<p>`, you're writing a tag. The distinction clarifies conversations with colleagues and documentation."

  - id: fe-app-m2-02-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "Elements like `<img>`, `<br>`, and `<input>` have no content and no closing tag. They are called ___ elements."
    inputConfig:
      placeholder: "void"
    markingRule:
      matchMode: CONTAINS
      accepted: [void, "void elements", "self-closing", self-closing, empty]
      rejectedFeedback: "**Void elements** (also called self-closing or empty elements) cannot have content. They don't need a closing tag. Examples: `<img>`, `<br>`, `<hr>`, `<input>`, `<meta>`, `<link>`. In HTML5, you can write `<img>` or `<img />` — both are valid."
    hint: "These elements have no content and no closing tag. What word describes a container with nothing in it?"
    reflectionPrompt: "Void elements are a special case to remember. Adding a closing tag to one (`</img>`) is technically invalid. In JSX (used with React), you'll write `<img />` — a self-closing syntax. Knowing why it's different helps."

  - id: fe-app-m2-02-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain what is wrong with this HTML and how to fix it:
      ```html
      <p>This is <strong>important and <em>really</strong> important</em>.</p>
      ```
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [nest, overlap, close, order, invalid, wrong, inside, outside]
      rejectedFeedback: "The tags overlap — `<em>` opens inside `<strong>` but closes outside it. Correct nesting: `<p>This is <strong>important and <em>really</em> important</strong>.</p>`. Think of tags like nested boxes — the inner box must close before the outer box."
    hint: "Look at the order in which the tags open and close."
    reflectionPrompt: "Correct nesting is not just about validity — browsers attempt to fix invalid HTML, but their interpretations vary. Malformed nesting can cause layout issues, JavaScript bugs (DOM structure is wrong), and accessibility problems. Write valid HTML from the start."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of these is a block-level element?"
    options:
      - "`<span>`"
      - "`<a>`"
      - "`<strong>`"
      - "`<p>`"
    correctIndex: 3
    feedback: "`<p>` is a block element — it starts on a new line and takes the full available width. `<span>`, `<a>`, and `<strong>` are inline elements — they flow within text without starting new lines."
  - type: MULTIPLE_CHOICE
    question: "Which of these is a void element (has no content and no closing tag)?"
    options:
      - "`<div>`"
      - "`<p>`"
      - "`<img>`"
      - "`<section>`"
    correctIndex: 2
    feedback: "`<img>` is a void element — it cannot have content and doesn't need a closing tag. `<div>`, `<p>`, and `<section>` are all container elements that have opening and closing tags."

retrieval:
  recall: "What is the difference between a tag and an element? Give an example of each."
  explain: "Why must HTML elements be properly nested and not overlapping?"
  mistakeId:
    code: "`<br>` needs a closing tag: `<br></br>`"
    answer: "`<br>` is a void element — it has no content and no closing tag. Writing `<br></br>` is invalid HTML. In HTML5 you can optionally write `<br />` (for compatibility with XML/XHTML tooling), but `<br>` alone is correct. Only container elements with content need closing tags."
---

# Hook

HTML has a small vocabulary — fewer than 120 elements — but each one has a specific meaning and purpose. Using the right element for the right content is the difference between markup that communicates and markup that merely renders.

Every element has a defined structure: an opening tag, content, and a closing tag. Learn this pattern precisely, and you have the foundation for everything you will write.

> Before reading on: look at any webpage. Try to imagine the HTML elements that might represent different parts of it. What would you guess?

# Lore Introduction

The Academy's scribes have a strict rule: every annotation must be properly opened and properly closed. An annotation that overlaps another is invalid — the archive will reject it.

*"Think of elements as boxes,"* says Master Aelindra. *"A box must be fully contained within its parent. You cannot have a box that is half inside and half outside another box."*

She draws a nested diagram on the board.

*"This is HTML nesting. Structure on top of structure, each contained within the one above it. When you follow this rule, everything is readable, navigable, and correct."*

# Core Learning

## Concept Introduction

An HTML **element** has three parts:

```html
<p>This is a paragraph.</p>
│   │                   │
│   content             closing tag
opening tag
```

| Part | Example | Description |
|---|---|---|
| **Opening tag** | `<p>` | Marks where the element starts |
| **Content** | `This is a paragraph.` | The element's content |
| **Closing tag** | `</p>` | Marks where the element ends |

### Void Elements

Some elements have no content and no closing tag:

```html
<img src="photo.jpg" alt="A mountain landscape">
<br>
<hr>
<input type="text">
<meta charset="UTF-8">
```

These are called **void** (or self-closing) elements. They cannot have children.

### Nesting

Elements can be placed inside other elements:

```html
<p>This text has <strong>bold</strong> and <em>italic</em> words.</p>
```

Rules:
- Inner elements must be fully contained within outer elements
- Tags cannot overlap
- The last opened element must be the first closed

**Invalid (overlapping):**
```html
<strong><em>text</strong></em>  <!-- WRONG -->
```

**Valid (properly nested):**
```html
<strong><em>text</em></strong>  <!-- CORRECT -->
```

### Block vs Inline Elements

| Category | Behaviour | Examples |
|---|---|---|
| **Block** | Starts on a new line, takes full width | `<p>`, `<h1>`, `<div>`, `<section>`, `<ul>` |
| **Inline** | Flows within text, only as wide as content | `<span>`, `<a>`, `<strong>`, `<em>`, `<img>` |

Note: this is the *default* display behaviour, which CSS can override.

## Why It Matters

Understanding elements and tags is the foundation of all HTML writing. Incorrect nesting creates an invalid DOM — causing unpredictable rendering, JavaScript errors, and accessibility failures. Knowing block vs inline behaviour helps you understand layout without CSS.

## Worked Examples

**Example 1 — A simple paragraph with inline formatting:**
```html
<p>The <strong>quick</strong> brown fox <em>jumps</em> over the lazy dog.</p>
```

**Example 2 — A nested list:**
```html
<ul>
  <li>Frontend Development
    <ul>
      <li>HTML</li>
      <li>CSS</li>
      <li>JavaScript</li>
    </ul>
  </li>
  <li>Backend Development</li>
</ul>
```

**Example 3 — An image (void element):**
```html
<img src="team.jpg" alt="Our team at the annual conference" width="800" height="600">
```

## Common Mistakes

- **Forgetting closing tags.** Block elements especially need them. Browsers forgive some omissions, but don't rely on it.
- **Overlapping tags.** `<b><i>text</b></i>` is invalid. Proper nesting: `<b><i>text</i></b>`.
- **Putting block elements inside inline elements.** E.g., `<span><p>text</p></span>` is invalid. Block elements cannot be children of inline elements.

## Mental Model

Think of HTML elements as **Russian nesting dolls (matryoshka)**:
- Each doll must fit completely inside the larger one
- No doll can be half inside and half outside
- The innermost doll is a complete unit
- You can have as many levels of nesting as needed

## Mini Summary

- An element = opening tag + content + closing tag
- Void elements have no content and no closing tag (`<img>`, `<br>`, `<input>`)
- Elements must be properly nested — no overlapping tags
- Block elements start on new lines; inline elements flow within text
- Correct nesting is required for valid HTML, correct DOM, and accessible pages

# Guided Practice Quest

**The Structure Inspector**

The Academy's scribes have marked up several scrolls, but some contain structural errors. Identify valid and invalid markup.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Write the HTML for a simple recipe card that contains:
- A recipe name (as a heading)
- A short description paragraph
- An ingredients list (unordered)
- A steps list (ordered)
- An image of the dish

Do not add any CSS. Focus on using appropriate elements and correct nesting. After writing it, review your own work: is every element properly nested? Are you using the most appropriate elements?

# Integration

**Connecting to Mathematics — Trees, Hierarchy, and the DOM**

HTML nesting creates a tree structure. This is not incidental — the Document Object Model is literally a tree data structure, and tree structures appear throughout mathematics and computer science.

In a tree: every node (element) has exactly one parent (except the root), and can have any number of children. The root node in HTML is `<html>`. Nesting rules in HTML are directly equivalent to the rules for well-formed trees.

The mathematical properties of trees have practical implications:
- Traversal algorithms (depth-first, breadth-first) are how CSS selectors and JavaScript query the DOM
- Tree balance affects performance of operations
- Invalid nesting breaks the tree structure, causing incorrect traversal results

Understanding that the DOM is a tree helps you understand why nesting rules matter beyond syntax: they are structural constraints, not arbitrary style rules.

What other systems can you think of that use tree structures? What do they have in common?

# Lore Conclusion

The nested annotations are correct. Every box is contained within its parent. The structure is clean.

*"Good structure is invisible to users,"* says Master Aelindra. *"They don't see your tags. They see your content, shaped by your markup. If the structure is wrong, everything built on top of it is unstable."*

The second rune of HTML glows.

*"Next: the details that live inside tags. Attributes — the properties that give elements their specific behaviour and meaning."*
