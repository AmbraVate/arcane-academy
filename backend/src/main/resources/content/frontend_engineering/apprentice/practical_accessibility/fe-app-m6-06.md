---
id: fe-app-m6-06
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m6
moduleTitle: "Module 6: Accessibility Foundations"
moduleGlyph: "♿"
moduleSortOrder: 6
topicSlug: practical_accessibility
topicTitle: "Practical Accessibility"
topicSortOrder: 2
lesson: semantic_html_for_accessibility
title: "Semantic HTML for Accessibility"
sortOrder: 3
difficulty: 2
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
    - "Explains how semantic HTML elements map to ARIA roles automatically"
    - "Uses <button> for actions and <a> for navigation correctly"
    - "Uses correct heading hierarchy (no skipping levels)"
    - "Uses <label> to associate form fields with their descriptions"
    - "Uses <table> with scope, caption, and th correctly for tabular data"
  keywords: [semantic, ARIA, button, link, heading, hierarchy, label, table, th, scope, caption, role]
  modelAnswer: |
    Semantic HTML elements automatically expose ARIA roles to the accessibility tree.
    <button> announces as a button and is keyboard-activatable; <a href> announces as
    a link. Heading levels must not skip (h1→h3 without h2 confuses navigation).
    <label for="id"> links form descriptions to inputs. <table> needs <caption>,
    <th scope="col"> for headers, and <th scope="row"> for row headers.
guidedSteps:
  - id: fe-app-m6-06-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A developer creates a styled <div> that looks like a button. What is missing compared to a native <button>?
    inputConfig:
      options:
        - "Nothing — CSS can make any element look and behave like a button"
        - "Keyboard focus, Enter/Space activation, button role, and cursor style — all must be added manually"
        - "Only the border-radius — buttons have rounded corners by default"
        - "The cursor:pointer style"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Keyboard focus, Enter/Space activation, button role, and cursor style — all must be added manually"]
      rejectedFeedback: "A native <button> provides: keyboard focusability (tabindex), Enter and Space key activation, the 'button' ARIA role, cursor: pointer, and correct accessibility announcements — all for free. A <div> has none of these. Adding them manually with JavaScript and ARIA is possible but complex and error-prone."
    hint: "Think about every capability a button needs beyond appearance."
    reflectionPrompt: "Native HTML elements are pre-packaged accessibility. When you use <button>, you get an entire accessibility specification implemented by the browser for free. When you use <div>, you inherit nothing — you must implement the entire interaction model yourself. Lazy developers use semantic HTML."

  - id: fe-app-m6-06-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Heading levels must be used in order. A page that goes h1 → h3 (skipping h2) creates a ___ in the heading outline.
    inputConfig:
      placeholder: "gap"
    markingRule:
      matchMode: CONTAINS
      accepted: [gap, break, jump, skip, hole, missing]
      rejectedFeedback: "Skipping heading levels (h1→h3) creates gaps in the document outline that confuse screen reader users navigating by headings. They may assume they missed content. Use headings in sequential order: h1 (page title), h2 (major sections), h3 (sub-sections within h2). You can skip levels going back up the hierarchy."
    hint: "What word describes the missing step in a sequence?"
    reflectionPrompt: "You can go up without constraints (an h4 followed by an h2 is fine — you're going 'up' in the hierarchy). You must not go down by skipping (h2 directly to h4 — the h3 level is absent). The outline readers navigate must be complete at each descent."

  - id: fe-app-m6-06-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 2-3 sentences why you should use <button> for actions (submitting a form, opening a modal) and <a href> for navigation, rather than using them interchangeably.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [button, link, navigate, action, screen-reader, keyboard, announce, href, role]
      rejectedFeedback: "Screen readers announce: <button> as 'button' (suggests an action on the current page), <a> as 'link' (suggests navigation to another location). Using a <button> to navigate or a <link> for an action confuses the semantic expectation. Keyboard users expect Enter to follow a link, and Space/Enter to activate a button."
    hint: "Think about what screen readers announce for each and what user expectation that creates."
    reflectionPrompt: "The semantic contract: links navigate (href attribute required); buttons perform actions. A <a> with href=\"#\" used as a button is wrong on two levels: it is announced as a link, and pressing Enter navigates to nowhere. Use <button> for actions, always. Use <a href=\"actual-url\"> for links, always."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A data table needs to communicate that the first row contains column headers. Which HTML achieves this?"
    options:
      - "<tr class=\"header\"> in the first row"
      - "<th scope=\"col\"> for cells in the first row, inside <thead>"
      - "<td header=\"true\"> on each header cell"
      - "The browser automatically detects the first row as a header"
    correctIndex: 1
    feedback: "<th scope=\"col\"> tells screen readers: this is a header for the column below it. scope=\"row\" marks a row header. <thead>/<tbody>/<tfoot> structure the table semantically. Without these, screen readers read tables as flat text — data cells have no relationship to their headers."
  - type: MULTIPLE_CHOICE
    question: "What is the purpose of the <caption> element inside a <table>?"
    options:
      - "To add a visible title below the table"
      - "To provide a description of the table's purpose, announced before table data by screen readers"
      - "To style the table header row"
      - "To add a border around the entire table"
    correctIndex: 1
    feedback: "<caption> is the first child of <table> and provides a heading for the table. Screen readers announce it before reading the table data. Without a caption, users hear table data without context — they don't know what the table is about. <caption> is to tables what alt text is to images."

retrieval:
  recall: "Write the correct HTML for a data table with column headers, caption, and two data rows."
  explain: "Why is using <div> for an action button considered an accessibility anti-pattern?"
  mistakeId:
    code: "<a href='#' onclick='openModal()'>Open</a>"
    answer: "A link with href='#' that triggers a JS action should be a <button>. Screen readers announce it as 'link' — users expect navigation. Use <button type='button' onclick='openModal()'>Open</button>. Accessible, keyboard-activatable, correctly announced."
---

# Hook

Semantic HTML is not just about structure and SEO. Every semantic element is also an accessibility decision.

`<button>` versus `<div>`. `<a href>` versus `<span>`. `<h2>` in the right order versus `<h2>` used for size. Each choice either builds accessibility into your HTML for free or requires you to rebuild it manually — and badly.

# Lore Introduction

*"Every element in the Academy's language,"* says Master Aelindra, *"carries meaning beyond its appearance. A red seal means danger. An open door symbol means enter. The Academy never uses a decorative swirl when a meaningful symbol is required — because meaning matters to those who cannot read the surrounding text."*

# Core Learning

## Concept Introduction

**Semantic element → ARIA role mapping (automatic):**

| HTML element | ARIA role | What screen readers announce |
|---|---|---|
| `<button>` | button | "Submit, button" |
| `<a href="...">` | link | "About us, link" |
| `<input type="checkbox">` | checkbox | "Newsletter signup, checkbox, unchecked" |
| `<nav>` | navigation | "Navigation, landmark" |
| `<main>` | main | "Main, landmark" |
| `<h2>` | heading level 2 | "Section title, heading level 2" |
| `<table>` | table | "Table name, 3 rows, 4 columns" |

**Interactive element rules:**
```html
<!-- Actions → <button> -->
<button type="button" onclick="openModal()">Open details</button>
<button type="submit">Send message</button>

<!-- Navigation → <a href> -->
<a href="/about">About us</a>
<a href="https://docs.example.com">Documentation</a>

<!-- WRONG — div as button (needs manual ARIA + JS) -->
<!-- <div onclick="openModal()">Open details</div> -->
```

**Heading hierarchy (must be sequential):**
```html
<h1>Page Title</h1>          ← one per page
  <h2>Major Section</h2>     ← major topic
    <h3>Sub-section</h3>     ← sub-topic
    <h3>Another sub-topic</h3>
  <h2>Another Section</h2>
```

**Accessible table:**
```html
<table>
  <caption>Academy module completion rates by tier</caption>
  <thead>
    <tr>
      <th scope="col">Module</th>
      <th scope="col">Apprentice %</th>
      <th scope="col">Junior %</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th scope="row">HTML Foundations</th>
      <td>94%</td>
      <td>N/A</td>
    </tr>
  </tbody>
</table>
```

## Why It Matters

Semantic HTML is accessibility you get for free — the browser and assistive tech already know what `<button>`, `<nav>`, and `<h1>` mean:

- A real `<button>` is keyboard-focusable, announces itself correctly, and responds to Enter and Space — a clickable `<div>` does none of that until you rebuild it all by hand
- Landmarks (`<header>`, `<main>`, `<nav>`) let screen reader users jump around a page the way sighted users skim
- Heading hierarchy is the document's table of contents; skipped levels are missing rungs on a ladder

The least accessible sites aren't usually malicious — they're `<div>` soup written by people who never learned this lesson.

## Common Mistakes

- `<a href="#">` for actions (use `<button>`)
- `<button>` for navigation (use `<a href>`)
- Skipping heading levels (`h1` → `h3`)
- Tables without `<th>`, `scope`, or `<caption>`

## Mental Model

Semantic elements are uniforms. In a hospital you instantly know who's a surgeon, a nurse, a visitor — the uniform carries the meaning, no introduction needed. `<button>`, `<nav>`, `<h2>` are uniforms: browsers, screen readers, and search engines recognise the role on sight and grant matching powers (focusability, announcements, shortcuts). A `<div onclick=...>` is a person in plain clothes claiming to be a surgeon — every capability must be proven manually (tabindex, role, key handlers), and something always gets forgotten. Dress your elements in the right uniform and the institution treats them correctly without further effort.

## Mini Summary

- ✔ Each semantic element automatically maps to an ARIA role
- ✔ `<button>` for actions; `<a href>` for navigation — never swap them
- ✔ Headings must be sequential — never skip levels going down
- ✔ Tables need `<caption>`, `<th scope>`, and `<thead>`
- ✔ Native HTML beats manually added ARIA every time

# Guided Practice Quest

**The Semantic Contract** — three questions on semantic HTML and accessibility. Steps in `guidedSteps`.

# Solo Practice Quest

Fix an inaccessible HTML fragment (provided below). Replace all `<div>` buttons, fix heading hierarchy, add table accessibility attributes, and ensure all interactive elements are correctly semantic.

```html
<div style="font-size:24px">Latest Results</div>
<div onclick="show()">View Details</div>
<div style="font-size:18px">Summary</div>
<table>
  <tr><td>Name</td><td>Score</td></tr>
  <tr><td>Alice</td><td>92</td></tr>
</table>
```

# Integration

**Connecting to Mathematics — Type Theory and Correct-by-Construction**

In type theory, a "correct-by-construction" approach ensures that if code compiles, it is correct by design. Semantic HTML applies this principle: if you use `<button>`, accessibility is correct by construction — the browser provides the role, keyboard interaction, and ARIA mapping. If you use `<div>`, nothing is constructed correctly by default. The relationship parallels type safety: a strongly typed language catches errors at compile time; semantic HTML catches accessibility gaps at the markup level. Both reward the developer who uses the available type system (or element set) correctly.

# Lore Conclusion

*"Each element in HTML carries a contract,"* says Master Aelindra. *"<button> promises: I respond to clicks and keyboard. <a> promises: I navigate. Break the contract — use a div where a button belongs — and every user who relied on the promise is let down. Keep the contract, and accessibility emerges naturally from the structure."*

---
