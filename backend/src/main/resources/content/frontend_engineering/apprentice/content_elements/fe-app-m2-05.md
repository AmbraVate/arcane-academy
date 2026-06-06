---
id: fe-app-m2-05
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
lesson: headings
title: "Headings"
sortOrder: 1
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m2-04]
integrationDomains: [psychology, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Names all six heading levels and their hierarchy"
    - "Explains why heading levels should not be skipped"
    - "Describes the role of headings in accessibility"
    - "Explains that headings are for structure, not visual size"
    - "Uses `<h1>` correctly (one per page)"
  keywords: [h1, h2, h3, heading, hierarchy, accessibility, screen reader, outline, structure, seo]
  modelAnswer: |
    HTML provides six heading levels: h1 through h6. They create a document outline — a hierarchy of
    sections that screen readers and search engines use to understand page structure. There should be
    exactly one h1 per page (the main topic). Heading levels should not be skipped (e.g. jumping from
    h1 to h3). Headings are for document structure, not visual size — use CSS for size. Screen reader
    users frequently navigate pages by jumping between headings.
guidedSteps:
  - id: fe-app-m2-05-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      How many `<h1>` elements should a typical web page have?
    inputConfig:
      options:
        - "As many as needed — h1 just means 'large text'"
        - "Exactly one — it represents the main topic of the page"
        - "Zero — h1 is too large for most designs"
        - "Two — one for the site name and one for the page topic"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Exactly one — it represents the main topic of the page"]
      rejectedFeedback: "A page should have exactly **one `<h1>`** — the main heading that describes the page's primary topic. Multiple h1s create ambiguity for search engines and screen readers about what the page is about. Sub-sections use h2, h3, etc."
    hint: "h1 is the top of the hierarchy — there is only one top."
    reflectionPrompt: "Think of your page like a document. A document has one title (h1), then sections (h2), subsections (h3), etc. Multiple h1s are like a book with multiple covers — structurally confusing."

  - id: fe-app-m2-05-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "Screen reader users often navigate a page by jumping between ___ — making their correct use critical for accessibility."
    inputConfig:
      placeholder: "headings"
    markingRule:
      matchMode: CONTAINS
      accepted: [headings, heading, "heading levels"]
      rejectedFeedback: "Screen reader users frequently use the **headings** navigation mode — pressing H to jump to the next heading. This lets them scan a page without reading every word. Missing or incorrectly structured headings make this navigation useless or misleading."
    hint: "What do you press 'H' to jump between in most screen readers?"
    reflectionPrompt: "Heading navigation is like a table of contents. If you wouldn't skip chapters in a table of contents (going from Chapter 1 straight to Chapter 3), don't skip heading levels in HTML."

  - id: fe-app-m2-05-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A designer asks you to make the h3 headings on a page larger than the h2 headings because it "looks better". What is wrong with solving this with HTML, and what should you do instead?
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [css, structure, hierarchy, semantic, meaning, visual, style, appearance]
      rejectedFeedback: "Heading levels define document structure and hierarchy — not visual size. Use CSS to style headings however you want. The HTML heading level should reflect the document outline. A visually larger h3 is fine if styled with CSS — but the h3 must be a subsection of an h2 in the document structure."
    hint: "What is the right tool for controlling visual size? What are headings actually for?"
    reflectionPrompt: "Separating structure (HTML) from presentation (CSS) is fundamental. Heading levels communicate hierarchy to browsers, search engines, and assistive technology. Visual size is CSS's job. Both can be true simultaneously."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A page about a software product has these headings: h1 (Product Name), h2 (Features), h4 (Feature Detail). What is wrong?"
    options:
      - "The page should not have an h2"
      - "Heading levels h3 is skipped — jumping from h2 to h4 breaks the document outline"
      - "h1 should be the company name, not the product name"
      - "Nothing is wrong — heading levels can be used in any order"
    correctIndex: 1
    feedback: "Skipping heading levels (h2 → h4) breaks the document outline. Screen readers and search engines expect a logical hierarchy. The feature detail should be h3, not h4, to correctly indicate it's a subsection of the h2 'Features' section."
  - type: MULTIPLE_CHOICE
    question: "Why should you not use `<h3>` just because you want smaller text than `<h2>`?"
    options:
      - "h3 is not supported in all browsers"
      - "Heading levels convey document structure and hierarchy, not just visual size — use CSS for size"
      - "h3 elements cannot be styled with CSS"
      - "h3 is reserved for image captions"
    correctIndex: 1
    feedback: "Heading levels create a document outline. Using h3 where h2 belongs — because you want smaller text — misleads screen readers and search engines about the page structure. Use the correct heading level for structure, then use CSS `font-size` to achieve the visual size you want."

retrieval:
  recall: "What is the correct hierarchy for HTML headings, and what does each level represent?"
  explain: "Why does skipping a heading level (e.g., h1 → h3) harm accessibility?"
  mistakeId:
    code: "I use whichever heading level looks the right size for my design"
    answer: "Headings define document structure, not visual size. Use the heading level that correctly represents the content hierarchy (h1 = main topic, h2 = sections, h3 = subsections). Then use CSS to control the visual size. Choosing heading levels for appearance breaks screen reader navigation and confuses search engines."
---

# Hook

Screen reader users don't scroll pages like sighted users. They jump between headings — pressing a key to skip from section to section, scanning the document's structure before deciding where to read in detail.

If your headings are wrong — missing, skipped, or used for visual size instead of structure — those users cannot effectively navigate your page. Your content is inaccessible to them, regardless of how well it's written.

Headings are not styling tools. They are the document's table of contents. Get them right.

> Before reading on: open any page you built (or any webpage). How many h1, h2, and h3 elements does it have? Is the hierarchy correct?

# Lore Introduction

In the Academy's library, every text is indexed with a precise outline: part, chapter, section, subsection. The cataloguers use this outline to navigate — they never read the whole text to find what they need.

*"Headings are your document's index,"* says Master Aelindra. *"Not decoration. Not visual formatting. Structure."*

She points to a poorly indexed scroll — sections out of order, sub-chapters where chapters should be.

*"A screen reader navigates by headings the same way a cataloguer navigates by an index. Give them a broken index, and they are lost."*

# Core Learning

## Concept Introduction

HTML provides six heading levels: `<h1>` through `<h6>`. They create a **document outline** — a hierarchical description of the page's content structure.

```html
<h1>Frontend Engineering</h1>
  <h2>Module 1: Understanding the Web</h2>
    <h3>The Internet</h3>
      <h4>What is the Internet?</h4>
    <h3>Browsers</h3>
  <h2>Module 2: HTML Foundations</h2>
    <h3>HTML Basics</h3>
```

### Rules

| Rule | Reason |
|---|---|
| **One `<h1>` per page** | Defines the primary topic of the page |
| **Don't skip levels** | Maintains a coherent outline; required for WCAG |
| **Use for structure, not size** | Use CSS for visual size |
| **Headings should describe their section** | Not decorative, not vague |

### What Headings Affect

- **Screen readers** — users navigate by heading with keyboard shortcuts
- **Search engines** — headings signal what the page and sections are about
- **Browser reader mode** — uses headings to build a simplified reading view
- **Document outline algorithms** — browsers use headings to understand page structure

## Why It Matters

A page without correct heading structure is navigable only for sighted users with a mouse. Screen reader users, keyboard-only users, and search engine crawlers all depend on heading structure. Correct headings are an accessibility, SEO, and structural requirement.

## Worked Examples

**Example 1 — Correct heading hierarchy:**
```html
<h1>The Complete Guide to Coffee</h1>
<h2>Types of Coffee</h2>
  <h3>Espresso</h3>
  <h3>Filter Coffee</h3>
<h2>Brewing Methods</h2>
  <h3>Espresso Machine</h3>
    <h4>Grind Size</h4>
    <h4>Extraction Time</h4>
  <h3>Pour Over</h3>
```

**Example 2 — Incorrect (skipped level):**
```html
<h1>Coffee Guide</h1>
<h2>Espresso</h2>
<h4>Grind Size</h4>  <!-- WRONG: skips h3 -->
```

**Example 3 — Using CSS for visual size (correct approach):**
```css
/* Make h3 visually larger than h2 for design reasons */
h2 { font-size: 1.5rem; }
h3 { font-size: 2rem; } /* Larger visually, but still structurally correct as h3 */
```

## Common Mistakes

- **Multiple h1 elements.** Creates ambiguity about the page's primary topic.
- **Skipping heading levels.** Breaks the document outline.
- **Using headings for visual size.** That's CSS's job.
- **Vague headings.** "Click here" or "More" as headings gives screen reader users no information.

## Mental Model

Think of headings as a **table of contents for a book**:
- One title (h1)
- Chapters (h2)
- Sections within chapters (h3)
- Subsections (h4)
- You wouldn't have two titles. You wouldn't jump from chapters to subsections.

## Mini Summary

- Six heading levels: h1–h6, representing a document hierarchy
- One h1 per page; don't skip levels; use for structure, not size
- Screen readers navigate by headings — correct structure enables this
- Search engines use headings to understand page topics
- Use CSS for visual size; use heading levels for structural meaning

# Guided Practice Quest

**The Cataloguer's Challenge**

The Academy's new content has arrived but is incorrectly indexed. Apprentices must identify structural errors and propose corrections.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You are building a documentation page for a JavaScript library. The page covers: installation, configuration (with sub-sections for basic and advanced config), API reference (with sub-sections for each method), and a FAQ.

Write the complete heading structure for this page — just the headings, no other content. Then review: is the hierarchy correct? Are any levels skipped? Does every heading describe its section clearly?

# Integration

**Connecting to Psychology — Chunking and Cognitive Load in Navigation**

Cognitive psychology research on **chunking** (Miller, 1956) shows that the human mind organises information into meaningful units to overcome short-term memory limits. Navigation using headings is a form of chunking — the page's content is organised into named chunks that can be scanned before committing to reading.

Eye-tracking studies of web users (Nielsen Norman Group) show that users don't read pages — they scan them. They look for headings, links, and bold text to identify relevant sections. Only then do they read linearly.

This is why heading text quality matters. "Section 1" is unchunkable — it conveys no meaning. "Installing the Library" is a meaningful chunk — users can decide in one glance whether to read it.

What does this suggest about how headings should be written to support how users actually navigate information?

# Lore Conclusion

The document outline is clean. The hierarchy is correct. Every heading describes its section.

*"Structure is invisible until it's missing,"* says Master Aelindra. *"Users don't see your h2 tags. But they feel the difference between a page that is easy to navigate and one that is a wall of text."*

A rune settles into the path.

*"Next: paragraphs. The most common element on the web — and still misused more often than you might expect."*
