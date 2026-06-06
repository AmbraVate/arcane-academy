---
id: fe-app-m2-06
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
lesson: paragraphs
title: "Paragraphs"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m2-05]
integrationDomains: [psychology, history]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains when to use `<p>` vs `<div>` for text"
    - "Explains why `<br>` should not be used to create paragraph spacing"
    - "Describes `<strong>` and `<em>` and their semantic meaning"
    - "Explains `<blockquote>` and `<cite>`"
    - "Connects paragraph semantics to accessibility"
  keywords: [paragraph, p, div, br, strong, em, blockquote, cite, semantic, inline, text]
  modelAnswer: |
    `<p>` is the correct element for paragraphs of text — it conveys semantic meaning (a unit of prose).
    `<div>` has no semantic meaning and should not be used for paragraphs. `<br>` creates a line break
    but should not be used to simulate paragraph spacing — use CSS margin/padding. `<strong>` marks
    text as important (bold); `<em>` marks emphasis (italic). `<blockquote>` marks a quotation from
    another source. These semantic choices matter for screen readers and search engines.
guidedSteps:
  - id: fe-app-m2-06-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A developer writes `<div>This is some introductory text about our company.</div>`. What is wrong with this, and what should be used instead?
    inputConfig:
      options:
        - "Nothing is wrong — `<div>` and `<p>` are interchangeable for text"
        - "`<p>` should be used — it conveys the semantic meaning of a paragraph of prose"
        - "`<span>` should be used for all text content"
        - "`<section>` should be used instead of `<div>`"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["`<p>` should be used — it conveys the semantic meaning of a paragraph of prose"]
      rejectedFeedback: "`<p>` tells the browser, screen reader, and search engine that this is a paragraph — a unit of prose. `<div>` is a generic container with no semantic meaning. Always prefer the most semantically correct element."
    hint: "What element was created specifically to represent paragraphs of text?"
    reflectionPrompt: "The `<div>` soup problem — using `<div>` for everything — is a common beginner pattern. It creates pages that are structurally meaningless. Every time you reach for `<div>`, ask: is there a more specific element for this content?"

  - id: fe-app-m2-06-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "`<strong>` marks text as ___ important — it has semantic meaning beyond just visual boldness, and screen readers may announce it with emphasis."
    inputConfig:
      placeholder: "strongly"
    markingRule:
      matchMode: CONTAINS
      accepted: [strongly, strong, important, "of strong importance"]
      rejectedFeedback: "`<strong>` conveys **strong importance** — a semantic signal that this text matters more than surrounding text. `<b>` makes text bold with no semantic meaning. Use `<strong>` when the text is genuinely important; use `<b>` for stylistic bold without semantic weight."
    hint: "The element's name is a clue to its semantic meaning."
    reflectionPrompt: "`<strong>` vs `<b>` and `<em>` vs `<i>` — each pair has a semantic version and a purely visual version. Prefer the semantic version: `<strong>` for important text, `<em>` for emphasis. Reserve `<b>` and `<i>` for cases where you want the visual style without the semantic meaning."

  - id: fe-app-m2-06-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why using `<br><br>` to create space between paragraphs is bad practice, and describe the correct approach.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [css, margin, spacing, semantic, br, paragraph, presentation, style]
      rejectedFeedback: "`<br>` is for line breaks within content (e.g. poetry, addresses) — not for creating visual spacing. Spacing is a presentation concern — use CSS `margin-bottom` on `<p>` elements. Using `<br><br>` to simulate paragraph breaks also means you're not using `<p>` — so the content has no semantic paragraph structure."
    hint: "Which layer handles spacing? Which element is for actual line breaks in content?"
    reflectionPrompt: "Every time you use HTML to control presentation, you make your code harder to maintain. Spacing, font size, colour — these belong in CSS. HTML's job is meaning. When you need space between paragraphs, write proper `<p>` elements and use CSS for the gap."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "When should you use `<blockquote>`?"
    options:
      - "For any text you want to visually indent"
      - "For a quotation taken from another source"
      - "For code examples"
      - "For sidebar content"
    correctIndex: 1
    feedback: "`<blockquote>` marks a quotation from another source. The `cite` attribute or a `<cite>` element can identify the source. Using `<blockquote>` for visual indentation (without an actual quotation) is a semantic misuse — use CSS `padding-left` instead."
  - type: MULTIPLE_CHOICE
    question: "What is the difference between `<strong>` and `<b>`?"
    options:
      - "They are identical — both make text bold"
      - "`<strong>` conveys semantic importance; `<b>` makes text bold with no semantic meaning"
      - "`<b>` is deprecated and should never be used"
      - "`<strong>` is only for headings"
    correctIndex: 1
    feedback: "`<strong>` = strong importance (semantic). `<b>` = visually bold (no semantic weight). Screen readers may treat `<strong>` differently. Use `<strong>` for genuinely important text; `<b>` for stylistic bold where importance isn't the intent (e.g., a product name in a review)."

retrieval:
  recall: "What is the difference between `<p>` and `<div>` for text content?"
  explain: "Why is `<br><br>` a poor substitute for paragraph structure?"
  mistakeId:
    code: "I use `<b>` and `<i>` tags for all bold and italic text because they're shorter to type"
    answer: "`<b>` and `<i>` have no semantic meaning — they're purely visual. `<strong>` (bold) and `<em>` (italic) convey meaning: importance and emphasis respectively. Screen readers and search engines use this meaning. Prefer semantic elements; use `<b>` and `<i>` only when visual styling without semantic meaning is intentional."
---

# Hook

The humble paragraph is the most common HTML element on the web. And yet it's frequently misused — replaced by `<div>`, simulated with `<br>` tags, or ignored in favour of unsemantic containers.

Paragraphs carry meaning. They tell the browser, the screen reader, and the search engine: "This is a complete unit of prose." That meaning matters.

> Look at a webpage's HTML source (right-click → View Page Source on any site). Is the main text in `<p>` tags? Or `<div>` tags?

# Lore Introduction

The Academy's scribes use a precise vocabulary. A paragraph is a paragraph — not a box, not a section, not a container. Calling it anything else confuses the cataloguers.

*"Every word you use to annotate content makes a promise,"* says Master Aelindra. *"A `<p>` tag promises: this is prose. A `<strong>` tag promises: this word matters. Keep your promises, and the systems that read your pages will serve your users well."*

# Core Learning

## Concept Introduction

### The `<p>` Element

The paragraph element represents a unit of prose:

```html
<p>The Internet is a global network of interconnected computers communicating via standardised protocols.</p>
```

Use `<p>` for any block of prose text. Do not use `<div>` as a paragraph replacement — `<div>` has no semantic meaning.

### Inline Text Formatting

| Element | Meaning | Rendered as |
|---|---|---|
| `<strong>` | Strong importance | Bold |
| `<em>` | Stress emphasis | Italic |
| `<b>` | Stylistic bold (no semantic meaning) | Bold |
| `<i>` | Stylistic italic (no semantic meaning) | Italic |
| `<mark>` | Highlighted text | Highlighted |
| `<del>` | Deleted text | Strikethrough |
| `<ins>` | Inserted text | Underline |
| `<code>` | Inline code | Monospace |
| `<abbr>` | Abbreviation | With optional tooltip via `title` attribute |

### `<br>` — Line Break

`<br>` creates a line break within content. Use it for:
- Poetry (where line breaks are part of the meaning)
- Addresses (street / city / postcode)

Do NOT use `<br><br>` as a paragraph separator. Use `<p>` elements and CSS spacing.

### `<blockquote>` and `<cite>`

```html
<blockquote cite="https://www.w3.org/History/1989/proposal.html">
  <p>This proposal concerns the management of general information about accelerators
  and experiments at CERN.</p>
  <cite>Tim Berners-Lee, 1989</cite>
</blockquote>
```

Use `<blockquote>` for quotations from external sources. Use `<cite>` to attribute the source.

## Why It Matters

Using `<p>` correctly means screen readers announce paragraph boundaries, search engines understand content structure, and your CSS targeting is semantically correct. `<br><br>` mimics the visual appearance of paragraphs but provides none of the structural meaning.

## Worked Examples

**Example 1 — Correctly marked-up text:**
```html
<p>The <strong>critical rendering path</strong> is the sequence of steps
the browser follows to convert HTML and CSS into <em>visible pixels</em>.</p>
```

**Example 2 — Address using `<br>`:**
```html
<address>
  Arcane Academy<br>
  1 Enchanted Courtyard<br>
  London, EC1A 1AA
</address>
```

**Example 3 — Blockquote:**
```html
<blockquote>
  <p>"Programs must be written for people to read, and only incidentally for machines to execute."</p>
  <cite>Harold Abelson, Structure and Interpretation of Computer Programs</cite>
</blockquote>
```

## Common Mistakes

- **Using `<div>` for paragraphs.** No semantic meaning.
- **Using `<br>` for paragraph spacing.** Use `<p>` elements and CSS.
- **Using `<b>` and `<i>` for semantic emphasis.** Use `<strong>` and `<em>`.
- **Using `<blockquote>` for visual indentation.** Only for actual quotations.

## Mental Model

Think of `<p>` as a **sentence in a formal document**: it has defined start and end, and its presence means something specific. Just as you wouldn't format a document by using blank lines and tabs instead of proper paragraphs, don't simulate paragraphs in HTML with `<div>` and `<br>`.

## Mini Summary

- `<p>` is the correct element for prose paragraphs — not `<div>`
- `<strong>` = important text; `<em>` = emphasised text; `<b>` and `<i>` are visual-only
- `<br>` is for line breaks in content, not for creating paragraph spacing
- `<blockquote>` marks external quotations; `<cite>` attributes the source
- Semantic elements serve screen readers, search engines, and maintainability

# Guided Practice Quest

**The Prose Annotator**

The Academy's texts need correct semantic markup applied. Identify the right element for each piece of content.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Write the HTML for a short blog post (invent the content). Include:
- An h1 for the post title
- At least three `<p>` paragraphs of content
- One use of `<strong>` (something genuinely important)
- One use of `<em>` (stress emphasis)
- A `<blockquote>` with a `<cite>`
- An inline `<code>` element
- An `<abbr>` with a `title` attribute

Review: have you used `<div>` anywhere it should be `<p>`? Have you used `<br>` for spacing?

# Integration

**Connecting to History — The Origins of Typographic Conventions**

The paragraph as a visual and conceptual unit has ancient origins. The word comes from the Greek *paragraphos* — a mark drawn beside text to indicate a new thought or topic. Early manuscripts used a symbol (¶, the pilcrow) rather than a blank line to separate paragraphs.

The blank line convention we now associate with "paragraphs" is relatively modern. HTML's `<p>` element abstracts the concept of a paragraph from any particular visual representation — it says what the content *is*, not how it should *look*. CSS then determines the visual rendering.

This separation — semantic identity vs visual representation — is a principle that appears throughout information systems design. When the two are conflated, systems become brittle: changing the appearance requires changing the structure.

What does this historical perspective suggest about the value of semantic markup over presentational markup?

# Lore Conclusion

The texts are annotated correctly. Paragraphs are paragraphs. Important words are strong. Quotations are attributed.

*"Every annotation,"* says Master Aelindra, *"is a contract. A paragraph promises coherent prose. Strong text promises importance. Hold every contract, and your pages will serve every reader — human or machine."*

A rune joins the path.

*"Next: lists. Two of the most used elements in HTML, and two of the most underappreciated."*
