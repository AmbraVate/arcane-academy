---
id: fe-app-m2-11
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m2
moduleTitle: "Module 2: HTML Foundations"
moduleGlyph: "📄"
moduleSortOrder: 2
topicSlug: semantic_html
topicTitle: "Semantic HTML"
topicSortOrder: 3
lesson: sectioning_content
title: "Sectioning Content"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly identifies when to use <section> vs <article> vs <div>"
    - "Uses <header> and <footer> correctly inside sectioning elements"
    - "Explains what makes a <section> different from a generic <div>"
    - "Names at least three sectioning elements with their purpose"
    - "Gives a real-world example of each element used correctly"
  keywords: [section, article, aside, header, footer, main, landmark, heading, outline]
  modelAnswer: |
    HTML5 sectioning elements create a document outline. <article> is for
    self-contained content (a post, comment, or widget). <section> groups
    thematically related content and should have a heading. <aside> holds tangential
    content like sidebars. Each can contain its own <header> and <footer>.
guidedSteps:
  - id: fe-app-m2-11-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A blog page has a list of posts. Each post could be syndicated to RSS independently. Which element wraps each individual post?
    inputConfig:
      options:
        - "<section>"
        - "<div>"
        - "<article>"
        - "<main>"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["<article>"]
      rejectedFeedback: "<article> is the correct choice — it marks self-contained content that can be independently distributed or reused. A blog post that could appear in an RSS feed is the canonical example. <section> is for thematically grouped content that isn't necessarily self-contained."
    hint: "Could this content make sense if extracted from the page and shown elsewhere?"
    reflectionPrompt: "The acid test for <article>: could this content appear in an RSS feed, be embedded in another page, or be shared independently? If yes, it's an article. If it only makes sense in context, it's a section."

  - id: fe-app-m2-11-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: "<section> elements should always contain a ___ element to describe their purpose."
    inputConfig:
      placeholder: "heading"
    markingRule:
      matchMode: CONTAINS
      accepted: [heading, h1, h2, h3, h4, h5, h6]
      rejectedFeedback: "Every <section> should have a heading (h1–h6) that describes its content. A section without a heading is a signal you may need <div> instead. The heading is what gives the section its identity in the document outline."
    hint: "What element do you use to label a section of content?"
    reflectionPrompt: "The heading inside a <section> is what makes it semantically meaningful. Without a heading, you haven't actually communicated what the section is about — you've just drawn an invisible box."

  - id: fe-app-m2-11-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A news website has a main article about climate change, and a sidebar showing related articles from the same author. Which element wraps the main article? Which wraps the sidebar content? Explain your choices.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [article, aside, sidebar, related, tangential]
      rejectedFeedback: "Main article → <article> (self-contained, syndicate-able). Sidebar → <aside> (tangentially related to main content but not essential to understanding it). <aside> is perfect for related links, author bios, and supplementary content."
    hint: "Is the sidebar essential to the main article, or supplementary to it?"
    reflectionPrompt: "<aside> literally means 'a whispered aside' — content that relates to the main subject but could be removed without losing the core meaning. This is the perfect mental model for sidebar content."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You are building a product page. The page has three sections: Overview, Specifications, and Reviews. Which element wraps each section?"
    options:
      - "<article> for each, since each could be used independently"
      - "<section> for each, since they are thematically grouped parts of the same page"
      - "<div> for each, since they are just layout regions"
      - "<main> for each, since they are all primary content"
    correctIndex: 1
    feedback: "<section> is correct — these are thematically grouped parts of a single product page. They aren't self-contained enough for <article> (you couldn't syndicate 'Specifications' independently). They are more meaningful than plain <div>."
  - type: MULTIPLE_CHOICE
    question: "Can a <footer> element appear inside an <article>?"
    options:
      - "No — <footer> can only be a direct child of <body>"
      - "Yes — sectioning elements can each have their own <header> and <footer>"
      - "Only if the article is inside <main>"
      - "Yes, but only for decorative purposes"
    correctIndex: 1
    feedback: "Sectioning elements (article, section, aside, nav) can each have their own <header> and <footer>. A blog article might have a <footer> containing the publish date, author, and tags — all logically part of the article's footer."

retrieval:
  recall: "Name the five main HTML5 sectioning elements and one use case for each."
  explain: "Explain why a <section> without a heading is usually a sign that <div> would be more appropriate."
  mistakeId:
    code: "Using <section> everywhere instead of <div> to 'be semantic'"
    answer: "<section> has a specific meaning: thematically grouped content with a heading. Using it as a styled container with no heading doesn't add semantic value — it may actually mislead the document outline. Use <div> for layout-only containers."
---

# Hook

A well-structured book has chapters, sections, and subsections — each with a title that tells you what it contains. A book with no chapters, just pages, is harder to navigate even if the content is identical.

HTML's sectioning elements are the chapters of your web page. They create a document outline that browsers, screen readers, and search engines can navigate.

> If you printed your web page and tried to create a table of contents from the HTML alone, could you?

# Lore Introduction

*"The Great Archive,"* says Master Selvaris — visiting from the Data wing — *"is organised by classification: scroll type, date, author, subject. Every section has a label. Without labels, the archive is just a room full of parchment."*

Master Aelindra nods. *"The same principle governs pages. Every section you create must have a name."*

# Core Learning

## Concept Introduction

HTML5 introduced sectioning elements that create a meaningful document outline:

| Element | Purpose | Key rule |
|---|---|---|
| `<main>` | Primary page content | One per page |
| `<article>` | Self-contained, independently usable content | Has its own heading |
| `<section>` | Thematically grouped content | Needs a heading |
| `<aside>` | Tangentially related content | Sidebar, callout, related links |
| `<header>` | Introductory content for its parent | Can appear in any sectioning element |
| `<footer>` | Closing content for its parent | Can appear in any sectioning element |

## Why It Matters

These elements create a **document outline** — a tree structure browsers, screen readers, and search engines use to understand your page. A well-structured outline means users can jump directly to the section they need. A flat `<div>` soup means reading everything from the top.

## Worked Examples

```html
<body>
  <header>
    <h1>Arcane Academy Blog</h1>
    <nav>...</nav>
  </header>

  <main>
    <article>
      <header>
        <h2>Understanding HTML Semantics</h2>
        <p>Published by Aelindra · 3 June 2026</p>
      </header>

      <section>
        <h3>What are Semantic Elements?</h3>
        <p>...</p>
      </section>

      <section>
        <h3>Why They Matter</h3>
        <p>...</p>
      </section>

      <footer>
        <p>Tags: HTML, Accessibility, Beginner</p>
      </footer>
    </article>

    <aside>
      <h2>Related Articles</h2>
      <ul>...</ul>
    </aside>
  </main>

  <footer>
    <p>&copy; 2026 Arcane Academy</p>
  </footer>
</body>
```

## Common Mistakes

- **Section without heading:** A `<section>` without an `h2`–`h6` doesn't contribute to the outline. Use `<div>` instead.
- **Multiple `<main>` elements:** Only one `<main>` per page.
- **`<aside>` for decorative sidebars:** `<aside>` means "related to the main content." A decorative panel with no relationship to the content is just a `<div>`.

## Mental Model

Think of sectioning elements as **chapters in a book with labelled tabs**. `<article>` is a chapter that could be removed and published separately. `<section>` is a labelled chapter within the book. `<aside>` is a margin note.

## Mini Summary

- ✔ `<article>` — self-contained, independently usable; has its own heading
- ✔ `<section>` — thematically grouped; always needs a heading
- ✔ `<aside>` — tangential content (sidebar, callout, related links)
- ✔ `<header>` and `<footer>` can appear inside any sectioning element
- ✔ Only one `<main>` per page

# Guided Practice Quest

**The Document Architect** — classify content into the correct sectioning elements. Steps in the frontmatter `guidedSteps` section.

# Solo Practice Quest

Design the sectioning structure for a news website's homepage. It has: a site header with navigation, a featured article (could appear in RSS), three secondary news sections (World, Tech, Sport), a sidebar with trending topics, and a site footer. Write the HTML skeleton using only sectioning elements and headings (no content, no CSS).

# Integration

**Connecting to Psychology — Chunking and Working Memory**

George Miller's 1956 research showed that human working memory holds approximately 7±2 items. Beyond that, comprehension degrades. "Chunking" — grouping related items into meaningful units — is how we overcome this limit.

HTML sectioning elements enforce chunking at the document level. A page with clearly labelled sections (`<article>`, `<section>`) allows users to mentally group and navigate content without holding the entire page in working memory. A flat `<div>` page forces users to process everything linearly. The structure of your HTML directly affects cognitive load.

# Lore Conclusion

*"A page with sections and headings,"* says Master Aelindra, *"is a conversation with your reader: here is what this page is about, here is how it is organised, here is where to find what you need. A page without sections is a lecture with no paragraphs — technically complete, practically exhausting."*

---
