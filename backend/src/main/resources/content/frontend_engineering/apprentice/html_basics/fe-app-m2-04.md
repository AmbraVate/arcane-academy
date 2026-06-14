---
id: fe-app-m2-04
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
lesson: document_structure
title: "Document Structure"
sortOrder: 4
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m2-03]
integrationDomains: [psychology, history]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Describes the purpose of DOCTYPE"
    - "Explains the difference between `<head>` and `<body>`"
    - "Lists at least four elements that belong in `<head>`"
    - "Explains what metadata is and why it matters"
    - "Explains the `<html lang>` attribute"
  keywords: [doctype, html, head, body, title, meta, charset, viewport, lang, link, script, favicon]
  modelAnswer: |
    Every HTML document starts with `<!DOCTYPE html>` — a declaration that tells the browser to use
    standard HTML5 rendering mode. The `<html>` element is the root. The `<head>` contains metadata:
    information about the page that isn't displayed (title, character encoding, viewport settings,
    CSS links, meta descriptions). The `<body>` contains everything the user sees. The `lang` attribute
    on `<html>` tells screen readers and search engines the document's language.
guidedSteps:
  - id: fe-app-m2-04-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the purpose of `<!DOCTYPE html>` at the start of an HTML file?
    inputConfig:
      options:
        - "It defines the HTML version number"
        - "It tells the browser to use standard HTML5 rendering mode (standards mode)"
        - "It links the HTML file to a CSS stylesheet"
        - "It sets the page title for the browser tab"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It tells the browser to use standard HTML5 rendering mode (standards mode)"]
      rejectedFeedback: "DOCTYPE tells the browser which rendering mode to use. Without it, some browsers switch to 'quirks mode' — legacy behaviour that emulates old browser bugs. `<!DOCTYPE html>` is the HTML5 declaration and triggers standards mode in all modern browsers."
    hint: "Without it, old browsers would render the page differently — inconsistently."
    reflectionPrompt: "Quirks mode exists because early browsers had inconsistent rendering bugs. When the web was standardised, browsers kept quirks mode for backward compatibility with old pages. DOCTYPE is the switch that says 'use the standards, not the quirks'."

  - id: fe-app-m2-04-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "The `<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">` tag tells the browser to set the ___ to the device's screen width, enabling responsive design."
    inputConfig:
      placeholder: "viewport"
    markingRule:
      matchMode: CONTAINS
      accepted: [viewport, "viewport width", width]
      rejectedFeedback: "The **viewport** meta tag controls how the page is scaled on mobile devices. Without it, mobile browsers render pages at desktop width and zoom out — making text tiny. With it, the page renders at the device's actual width, enabling responsive layouts."
    hint: "The tag is literally called the viewport meta tag."
    reflectionPrompt: "This one line is essential for any responsive website. Without it, your carefully crafted media queries will not work correctly on mobile devices. It should be in every HTML document you create."

  - id: fe-app-m2-04-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A colleague asks: "Why does the `<title>` element matter? Nobody reads the browser tab." Give a compelling argument for why `<title>` is important, naming at least two reasons.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [seo, search, tab, bookmark, screen reader, accessibility, google, index]
      rejectedFeedback: "Title matters for: (1) SEO — search engines display it in results, (2) bookmarks — it becomes the bookmark name, (3) screen readers — read aloud to identify the page, (4) browser tabs — helps users identify the page among many tabs, (5) social sharing — used as the default share title."
    hint: "Think beyond the browser tab. Where else does the page title appear or get used?"
    reflectionPrompt: "The `<title>` is your page's identity in multiple contexts — search results, social shares, screen reader announcements, bookmarks. Descriptive, unique titles are an SEO and accessibility requirement, not an optional nicety."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which element belongs in `<head>` rather than `<body>`?"
    options:
      - "`<h1>Page Heading</h1>`"
      - "`<p>Introduction paragraph</p>`"
      - "`<meta charset=\"UTF-8\">`"
      - "`<img src=\"hero.jpg\" alt=\"Hero image\">`"
    correctIndex: 2
    feedback: "`<meta charset=\"UTF-8\">` is metadata — it belongs in `<head>`. Headings, paragraphs, and images are visible content — they belong in `<body>`. The rule: `<head>` = information about the page; `<body>` = the page itself."
  - type: MULTIPLE_CHOICE
    question: "Why is `<html lang=\"en\">` important?"
    options:
      - "It changes the browser's language settings"
      - "It translates page content automatically"
      - "It tells screen readers and search engines the document's language for correct pronunciation and indexing"
      - "It is required for CSS stylesheets to load"
    correctIndex: 2
    feedback: "The `lang` attribute enables correct screen reader pronunciation (different languages have different phonetics) and helps search engines index the page in the right language. It's an accessibility and SEO requirement. Missing `lang` fails WCAG accessibility standards."

retrieval:
  recall: "Write the complete skeleton structure of a valid HTML5 document from memory."
  explain: "Why does `<meta charset='UTF-8'>` need to be one of the first elements in `<head>`?"
  mistakeId:
    code: "The `<title>` element is just for the browser tab and doesn't affect SEO or accessibility"
    answer: "The `<title>` is used by search engines as the link text in results, by screen readers to announce the page, by browsers for bookmarks, and by social media for sharing previews. It is both an SEO signal and an accessibility requirement. Every page should have a unique, descriptive title."
---

# Hook

Before a single word of content, before a heading or a paragraph or an image, every HTML document needs a skeleton.

This skeleton is not visible to users — but it is read by browsers, search engines, screen readers, and social media platforms. It tells them what the document is, what language it's in, how to display it on mobile devices, and what CSS and JavaScript it needs.

Get the skeleton wrong and everything built on top of it is compromised.

> Write what you think a minimal valid HTML document should contain, from memory. Then read on.

# Lore Introduction

In the Academy's construction workshops, every building starts with groundwork — foundations, framing, services (water, power) — before the visible structure appears.

*"HTML documents are the same,"* says Master Aelindra. *"Before you build what users see, you lay the foundations. Metadata. Character encoding. Viewport settings. Language. Linked resources."*

She displays an empty building frame.

*"The `<head>` is the foundation and services. The `<body>` is the building. Both are necessary. The foundation is invisible — but without it, the building is unstable."*

# Core Learning

## Concept Introduction

Every HTML document follows this structure:

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Title</title>
    <meta name="description" content="Brief description of the page">
    <link rel="stylesheet" href="styles.css">
    <link rel="icon" href="favicon.ico">
  </head>
  <body>
    <!-- All visible content goes here -->
    <h1>Hello, World</h1>
  </body>
</html>
```

### The DOCTYPE Declaration

```html
<!DOCTYPE html>
```
Not an HTML element — it's a processing instruction. Tells the browser: use HTML5 standards mode. Always the first line. Never omit it.

### The `<html>` Root

```html
<html lang="en">
```
The root element. All content lives inside it. The `lang` attribute specifies the document language — required for accessibility (correct screen reader pronunciation) and SEO.

### The `<head>` Element

Contains metadata — information about the page, not visible content.

| Element | Purpose |
|---|---|
| `<meta charset="UTF-8">` | Character encoding — supports all Unicode characters |
| `<meta name="viewport">` | Controls mobile rendering — essential for responsive design |
| `<title>` | Page title — browser tab, search results, bookmarks |
| `<meta name="description">` | Search engine snippet text |
| `<link rel="stylesheet">` | Links external CSS |
| `<link rel="icon">` | Page favicon |
| `<script defer>` | Links JavaScript (defer = run after HTML parse) |

### The `<body>` Element

Contains everything users see and interact with. Every heading, paragraph, image, form, and component lives here.

### Character Encoding

```html
<meta charset="UTF-8">
```
Must be within the first 1024 bytes of the document. UTF-8 supports all Unicode characters — essential for any page that might display non-ASCII characters (accented letters, emoji, non-Latin scripts).

## Why It Matters

A well-structured document head:
- Enables correct mobile rendering (viewport meta)
- Enables accessibility (lang attribute, title)
- Improves SEO (title, description)
- Loads CSS correctly (link element)
- Supports international characters (charset)

An incorrectly structured head causes: garbled characters, broken mobile layout, poor search ranking, and accessibility failures.

## Worked Examples

**Example 1 — Full document head for a production page:**
```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="Learn frontend engineering at Arcane Academy">
  <title>Frontend Engineering — Arcane Academy</title>
  <link rel="icon" type="image/svg+xml" href="/favicon.svg">
  <link rel="stylesheet" href="/styles/main.css">
</head>
<body>
  <h1>Welcome to Arcane Academy</h1>
</body>
</html>
```

**Example 2 — The viewport meta tag explained:**
```html
<!-- Without this on mobile: browser renders at ~980px width, zooms out -->
<!-- With this: browser renders at actual device width -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
```

## Common Mistakes

- **Missing DOCTYPE.** Triggers quirks mode in some browsers.
- **Missing `lang` attribute.** Fails WCAG accessibility standards.
- **Missing viewport meta.** Breaks responsive design on mobile.
- **Vague or missing `<title>`.** Hurts SEO and accessibility. Use descriptive, unique titles.
- **Putting visible content in `<head>`.** It belongs in `<body>`.

## Mental Model

Think of the document structure as an **official letter**:
- The top of the letter has administrative details: date, sender, recipient, reference number (the `<head>`)
- The body of the letter has the actual message content (the `<body>`)
- Without the administrative details, the letter is harder to process, route, and file — even if the message is perfect

## Mini Summary

- Every HTML document starts with `<!DOCTYPE html>`
- `<html lang="en">` is the root; `lang` attribute is required for accessibility
- `<head>` contains metadata: charset, viewport, title, description, CSS links
- `<body>` contains all visible content
- Missing any core head elements can cause rendering, accessibility, or SEO failures

# Guided Practice Quest

**The Blueprint Drafter**

New apprentices at the Academy must learn to lay a proper foundation before building. Draft the head of a document for a given set of requirements.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Create a complete HTML document (head and body) for a personal portfolio page. Include:
- Correct DOCTYPE and language
- Character encoding
- Viewport meta tag
- A descriptive, unique title
- A meta description (good for SEO — 120–160 characters)
- A linked stylesheet (even if the file doesn't exist yet)
- A favicon link
- In the body: your name as an `<h1>`, a short bio as a `<p>`, and a list of skills

Review your work against the document structure checklist above. What did you miss on first attempt?

# Integration

**Connecting to History — Character Encoding and the Tower of Babel Problem**

Before UTF-8 became standard, the web was fragmented by character encoding: ASCII (English only), Latin-1, Shift-JIS (Japanese), GB2312 (Chinese), KOI8-R (Russian). A page written in one encoding would display as garbled symbols in another.

UTF-8, designed by Ken Thompson and Rob Pike in 1992, solved this by encoding all Unicode characters — over 140,000, covering every writing system — in a backward-compatible format. A byte sequence valid in ASCII is valid in UTF-8. Non-ASCII characters use multi-byte sequences.

By 2023, over 98% of web pages use UTF-8. The global web became possible because a single, universal encoding replaced the fragmented landscape.

What does this history suggest about the value of universal standards in enabling global communication systems?

# Lore Conclusion

The blueprint is complete. Foundations laid. Services connected. The building frame stands ready.

*"This document,"* says Master Aelindra, gesturing at the screen, *"can now be indexed by search engines, read by screen readers, displayed correctly on mobile, and styled by CSS. All before a single user sees it."*

A rune settles into the path.

*"Now we fill the building. Headings, paragraphs, lists, images, links — the content elements that make a page real."*
