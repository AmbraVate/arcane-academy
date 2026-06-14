---
id: fe-app-m2-12
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
lesson: navigation_structures
title: "Navigation Structures"
sortOrder: 3
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
    - "Uses <nav> correctly to wrap navigation link groups"
    - "Uses an unordered list inside <nav> for groups of links"
    - "Explains why a page can have multiple <nav> elements"
    - "Writes a 'skip to main content' link and explains its purpose"
    - "Labels multiple <nav> elements using aria-label"
  keywords: [nav, navigation, skip-link, aria-label, landmark, keyboard, list, primary, breadcrumb]
  modelAnswer: |
    The <nav> element wraps groups of navigation links. A page can have multiple
    <nav> elements (primary nav, footer nav, breadcrumbs) — each labelled with
    aria-label for screen readers. Links inside <nav> are typically an unordered
    list. A skip-to-content link at the top of the page lets keyboard users bypass
    the navigation and jump directly to <main>.
guidedSteps:
  - id: fe-app-m2-12-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A page has a primary header navigation AND a footer navigation. How should they be marked up?
    inputConfig:
      options:
        - "Only one <nav> is allowed per page — use <div> for the footer nav"
        - "Two <nav> elements, each with a distinct aria-label"
        - "Two <nav> elements with the same markup — aria-label is not needed"
        - "Use <menu> for the footer navigation instead"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Two <nav> elements, each with a distinct aria-label"]
      rejectedFeedback: "A page can have multiple <nav> elements. When there are more than one, screen readers need aria-label (or aria-labelledby) to distinguish them — otherwise users hear 'navigation, navigation' with no way to tell them apart."
    hint: "Think about what a screen reader user hears when navigating by landmark."
    reflectionPrompt: "aria-label turns 'navigation' into 'primary navigation' and 'footer navigation'. A small addition with a large impact on usability for keyboard and screen reader users."

  - id: fe-app-m2-12-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete this skip link (the first element in <body>):

      `<a href="#___" class="skip-link">Skip to main content</a>`
    inputConfig:
      placeholder: "main"
    markingRule:
      matchMode: CONTAINS
      accepted: [main, main-content, content]
      rejectedFeedback: "The skip link targets the <main> element (or any element with id=\"main-content\"). It lets keyboard and screen reader users jump past the repeated navigation to the page content — essential for usability on pages with long nav bars."
    hint: "It should jump to the primary content area of the page."
    reflectionPrompt: "Skip links are invisible to sighted mouse users (they are visually hidden but accessible to keyboard focus) but make a huge difference for keyboard-only users. Every multi-page website should have one."

  - id: fe-app-m2-12-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Write the HTML for a primary navigation with three links (Home, About, Contact). Use the correct semantic structure including the list pattern.
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: [nav, ul, li, href]
      rejectedFeedback: "Use <nav aria-label=\"Primary\"> containing a <ul> with <li> items, each containing an <a>. The list pattern communicates to screen readers how many items are in the navigation."
    hint: "Navigation is a list of links — use the appropriate list element."
    reflectionPrompt: "The <ul>/<li> pattern inside <nav> is a convention for good reason: screen readers announce 'list, 3 items' before reading the links, giving users a mental model of the navigation before they start."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why is a 'skip to main content' link valuable even if sighted users never see it?"
    options:
      - "It improves SEO by giving search engines a direct link to main content"
      - "It allows keyboard-only users to bypass navigation on every page load"
      - "It is required by HTML5 specification"
      - "It prevents duplicate content penalties"
    correctIndex: 1
    feedback: "Keyboard users tab through every focusable element. Without a skip link, they must tab through every navigation item on every page before reaching the content. A skip link is hidden visually but becomes visible on focus, allowing direct access to <main>."
  - type: MULTIPLE_CHOICE
    question: "A breadcrumb navigation (Home > Products > Laptops) should be wrapped in which element?"
    options:
      - "<nav aria-label=\"Breadcrumb\">"
      - "<aside aria-label=\"Breadcrumb\">"
      - "<section aria-label=\"Breadcrumb\">"
      - "<div class=\"breadcrumb\">"
    correctIndex: 0
    feedback: "Breadcrumbs are a navigation pattern, so <nav> is correct. The aria-label distinguishes it from the primary navigation. The aria-current=\"page\" attribute on the final item further communicates the current page."

retrieval:
  recall: "Write the HTML for a primary navigation with three links, including proper semantic structure."
  explain: "Why should navigation links be wrapped in a <ul> list rather than just placed directly inside <nav>?"
  mistakeId:
    code: "Two <nav> elements with no aria-label — screen reader announces 'navigation' twice"
    answer: "Add aria-label to each <nav> to distinguish them: aria-label=\"Primary\" and aria-label=\"Footer\". Screen readers then announce 'Primary navigation' and 'Footer navigation', giving users context about each navigation region."
---

# Hook

Navigation is how users move through your site. Get it wrong and users are stranded. Get it right and it disappears — users find what they need without thinking about the navigation at all.

The best navigation is invisible. The worst navigation is all the user can think about.

> Think of a website with excellent navigation and one with terrible navigation. What's the difference?

# Lore Introduction

*"Every tower in the Academy,"* says Master Aelindra, *"has a map at the entrance showing all the floors. Visitors check the map once, then navigate the tower without looking again. That map is your navigation: clear, consistent, and quickly forgettable — because it worked."*

# Core Learning

## Concept Introduction

| Element / Pattern | Purpose |
|---|---|
| `<nav>` | Wraps a group of navigation links |
| `aria-label="Primary"` | Labels a nav when multiple navs exist on the page |
| `<ul>` inside `<nav>` | List of links — screen readers announce count |
| Skip link | Hidden link at top of page to bypass nav |
| `aria-current="page"` | Marks the currently active link |

## Why It Matters

Navigation is visited on every page load by every user. A screen reader user tabbing through a 20-item nav bar on every page visit is an enormous usability problem. Correct semantic structure and a skip link are the fixes.

## Worked Examples

**Primary navigation:**
```html
<nav aria-label="Primary">
  <ul>
    <li><a href="/" aria-current="page">Home</a></li>
    <li><a href="/courses">Courses</a></li>
    <li><a href="/about">About</a></li>
    <li><a href="/contact">Contact</a></li>
  </ul>
</nav>
```

**Skip to main content link (first element in body):**
```html
<a href="#main-content" class="skip-link">Skip to main content</a>
<!-- CSS hides it visually, shows on :focus -->
```

**Breadcrumb navigation:**
```html
<nav aria-label="Breadcrumb">
  <ol>
    <li><a href="/">Home</a></li>
    <li><a href="/courses">Courses</a></li>
    <li><span aria-current="page">HTML Foundations</span></li>
  </ol>
</nav>
```

## Common Mistakes

- **No skip link:** Every keyboard user re-tabs through the entire nav on every page.
- **Multiple `<nav>` without `aria-label`:** Screen readers announce "navigation" with no distinction.
- **Using `<div>` inside `<nav>` instead of `<ul>`:** Loses the list count announcement.
- **Highlighting active link visually only:** Add `aria-current="page"` for screen readers.

## Mental Model

Think of navigation as **signposting in a building**. Signs at eye level, clearly labelled, consistent on every floor. A skip link is the express lift — it bypasses every floor and takes you directly to where you need to go.

## Mini Summary

- ✔ `<nav>` wraps navigation link groups
- ✔ Multiple `<nav>` elements need `aria-label` to distinguish them
- ✔ Use `<ul>/<li>` inside `<nav>` for the list structure
- ✔ Add a skip-to-content link as the first focusable element on the page
- ✔ Mark the active page with `aria-current="page"`

# Guided Practice Quest

**The Wayfinder** — three questions on structuring navigation correctly. Steps in the frontmatter `guidedSteps` section.

# Solo Practice Quest

Build the complete navigation structure for a three-page website (Home, Blog, Contact). Include: a primary nav with aria-label, a skip-to-content link, and mark the current page. Add a breadcrumb trail for the Blog page showing `Home > Blog > Post Title`.

# Integration

**Connecting to Psychology — Cognitive Maps and Spatial Navigation**

Research by Roger Downs and David Stea on cognitive mapping shows that humans build mental models of spaces to navigate efficiently — we remember "the library is on the left after the main stairs." Web users build the same mental maps of site navigation. Consistent, labelled navigation anchors the user's cognitive map of the site.

When navigation changes between pages, users have to rebuild their mental map. When it is consistent and clearly labelled, the map is built once. Skip links and ARIA labels help users with disabilities build the same cognitive maps that sighted users build effortlessly.

# Lore Conclusion

*"Navigation done well,"* says Master Aelindra, *"is the most invisible code you will ever write. No one praises navigation that works. They only notice when it doesn't. Build it right, then forget about it — and so will your users."*

---
