---
id: fe-app-m2-09
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
lesson: links
title: "Links"
sortOrder: 5
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes a correct <a> element with href attribute"
    - "Explains the difference between absolute and relative URLs"
    - "Describes when to use target=\"_blank\" and the associated security risk"
    - "Writes accessible link text (not 'click here')"
    - "Distinguishes between linking to a page and linking to an anchor"
  keywords: [anchor, href, link, absolute, relative, target, blank, noopener, accessible]
  modelAnswer: |
    The <a> (anchor) element creates a hyperlink using the href attribute. Absolute
    URLs include the full address; relative URLs are paths from the current page.
    target="_blank" opens links in a new tab but requires rel="noopener noreferrer"
    for security. Link text should describe the destination, not just say "click here."
guidedSteps:
  - id: fe-app-m2-09-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of these is the best accessible link text?
    inputConfig:
      options:
        - "<a href=\"/report\">Click here</a>"
        - "<a href=\"/report\">Read the annual report</a>"
        - "<a href=\"/report\">More</a>"
        - "<a href=\"/report\">Link</a>"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["<a href=\"/report\">Read the annual report</a>"]
      rejectedFeedback: "Link text must describe the destination. Screen reader users often navigate by listing all links on a page — a page full of 'click here' links is unusable. Always write text that makes sense out of context."
    hint: "Imagine a screen reader user hearing just the link text, without the surrounding paragraph."
    reflectionPrompt: "Descriptive link text is not just an accessibility win — it also improves SEO. Search engines weight anchor text as a signal of what the target page is about."

  - id: fe-app-m2-09-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete this link that opens in a new tab safely:

      `<a href="https://example.com" target="_blank" rel="___">Visit Example</a>`
    inputConfig:
      placeholder: "noopener noreferrer"
    markingRule:
      matchMode: CONTAINS
      accepted: [noopener, noreferrer]
      rejectedFeedback: "When using target=\"_blank\", always add rel=\"noopener noreferrer\". Without it, the opened page can access your page via window.opener — a security vulnerability called reverse tabnapping."
    hint: "Two values, space-separated: one prevents the opened page accessing the opener, one hides the referrer."
    reflectionPrompt: "This is a common interview question and a real-world gotcha. Any time you write target=\"_blank\", make it a habit to add rel=\"noopener noreferrer\" immediately."

  - id: fe-app-m2-09-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 2–3 sentences the difference between an absolute URL and a relative URL, giving an example of each.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [absolute, relative, http, /, domain]
      rejectedFeedback: "Absolute URL: full address including protocol and domain (https://example.com/page). Relative URL: path from the current page (/about, ../images/logo.png). Relative URLs break if you move the page; absolute URLs always point to the same place."
    hint: "Think about what happens if you move the HTML file to a different folder."
    reflectionPrompt: "Relative URLs are convenient for internal links because they work in both development and production. Absolute URLs are needed for external links. Mixing them up is a common source of broken links when deploying."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does href=\"#contact\" do?"
    options:
      - "Links to a page called contact"
      - "Links to an element with id=\"contact\" on the same page"
      - "Creates an email link"
      - "Does nothing — # is a comment"
    correctIndex: 1
    feedback: "href=\"#id\" scrolls the page to the element with that ID. These are called anchor links or fragment identifiers. They are widely used for in-page navigation, like a table of contents."
  - type: MULTIPLE_CHOICE
    question: "You want to create a link that opens the user's email client. Which href value is correct?"
    options:
      - "href=\"email:hello@example.com\""
      - "href=\"mail:hello@example.com\""
      - "href=\"mailto:hello@example.com\""
      - "href=\"send:hello@example.com\""
    correctIndex: 2
    feedback: "The mailto: scheme opens the user's default email client. Similarly, tel: opens a phone dialler on mobile. These are URL schemes just like https:."

retrieval:
  recall: "Write a link to an external page that opens in a new tab safely."
  explain: "Why is 'click here' poor link text, and what should you write instead?"
  mistakeId:
    code: "<a href=\"/page\" target=\"_blank\">Visit</a>"
    answer: "Missing rel=\"noopener noreferrer\" — without it the linked page can access window.opener, enabling reverse tabnapping attacks. Always pair target=\"_blank\" with rel=\"noopener noreferrer\"."
---

# Hook

The web is not called the web for nothing. What makes it a web — rather than just a collection of documents — is the link.

The hyperlink is arguably the most important invention of the modern web. It is what makes the web navigable, connected, and alive. Without links, you have a static document. With links, you have a living network.

But links done badly frustrate users, harm accessibility, and create security vulnerabilities. Done well, they are invisible — users just go where they want to go.

> Think of a time you got lost on a website. Was it a navigation problem? A link problem? Both?

# Lore Introduction

*"The Academy's library,"* says Master Aelindra, gesturing at the infinite shelves, *"contains every scroll ever written. But without the index — the references that lead from one scroll to the next — it is just a pile of parchment. The hyperlink is the index of the web."*

# Core Learning

## Concept Introduction

The `<a>` (anchor) element creates a hyperlink. The `href` attribute defines the destination.

| `href` value | Meaning |
|---|---|
| `href="https://example.com"` | Absolute URL — full external address |
| `href="/about"` | Absolute path — from the root of the same site |
| `href="about.html"` | Relative path — from current file location |
| `href="#section-2"` | Fragment — scrolls to element with `id="section-2"` |
| `href="mailto:a@b.com"` | Opens email client |
| `href="tel:+441234567890"` | Opens phone dialler |

```html
<!-- External link, opens in new tab safely -->
<a href="https://docs.example.com" target="_blank" rel="noopener noreferrer">
  Read the documentation
</a>

<!-- In-page anchor link -->
<a href="#getting-started">Jump to Getting Started</a>
```

## Why It Matters

Links are the navigation fabric of the web. Broken links lose users. Inaccessible link text excludes screen reader users. Missing `rel="noopener noreferrer"` on `target="_blank"` links creates a real security vulnerability. These are not edge cases — they appear on almost every production page.

## Worked Examples

**Good vs bad link text:**
```html
<!-- Bad — meaningless out of context -->
<p>To read the report, <a href="/report">click here</a>.</p>

<!-- Good — descriptive even when read in isolation -->
<p><a href="/report">Download the 2024 Annual Report</a></p>
```

**Fragment navigation (table of contents):**
```html
<nav>
  <a href="#intro">Introduction</a>
  <a href="#setup">Setup</a>
</nav>

<section id="intro">...</section>
<section id="setup">...</section>
```

## Common Mistakes

- **"Click here" / "Read more":** Meaningless to screen readers navigating by links.
- **`target="_blank"` without `rel="noopener noreferrer"`:** Security vulnerability.
- **Relative URLs with wrong path:** `href="about.html"` breaks if the HTML file moves.
- **Using `<a>` without `href` for a button:** Use `<button>` for actions, `<a>` for navigation.

## Mental Model

Think of `<a>` as a **portal spell**. The `href` is the incantation — where it takes you. The link text is the label on the door. A door labelled "Door" tells you nothing. A door labelled "Library — Third Floor" tells you exactly what to expect.

## Mini Summary

- ✔ `<a href="...">Link text</a>` — the fundamental hyperlink
- ✔ Write descriptive link text that makes sense out of context
- ✔ `target="_blank"` always paired with `rel="noopener noreferrer"`
- ✔ `#id` for in-page anchors, `mailto:` for email, `tel:` for phone
- ✔ Use `<a>` for navigation, `<button>` for actions

# Guided Practice Quest

**The Library Index** — three questions on writing correct, accessible, secure hyperlinks. Steps are in the frontmatter `guidedSteps` section.

# Solo Practice Quest

Write the HTML for a navigation bar with four links:
1. Home (relative, current site)
2. Documentation (external, opens in new tab safely)
3. Contact (mailto link)
4. A "Skip to main content" link that jumps to `id="main"`

For each, write a sentence explaining your choices.

# Integration

**Connecting to Mathematics — Graph Theory and the PageRank Algorithm**

Google's PageRank algorithm, which transformed search, is built on graph theory. It treats the web as a directed graph: pages are nodes, links are directed edges. A page with many inbound links from authoritative pages receives a high PageRank score.

The semantic quality of your link text is also a signal. A link that says "Arcane Academy HTML Guide" pointing to a page about HTML sends a stronger signal than a link that says "click here." When you write good link text, you are not just helping users — you are also describing the web's topology for the algorithms that index it.

# Lore Conclusion

*"The link,"* says Master Aelindra, *"is a promise. It says: follow me and you will find what the text describes. Break the promise — with bad link text, broken URLs, or unsafe new tabs — and the user learns not to trust you. Trust, once lost on the web, is almost impossible to recover."*

---
