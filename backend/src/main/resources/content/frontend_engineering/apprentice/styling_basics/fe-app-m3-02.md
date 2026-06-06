---
id: fe-app-m3-02
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m3
moduleTitle: "Module 3: CSS Foundations"
moduleGlyph: "🎨"
moduleSortOrder: 3
topicSlug: styling_basics
topicTitle: "Styling Basics"
topicSortOrder: 1
lesson: selectors
title: "Selectors"
sortOrder: 2
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
    - "Correctly writes element, class, and ID selectors"
    - "Explains when to use a class vs an ID selector"
    - "Uses a descendant combinator to target nested elements"
    - "Writes a grouped selector to apply the same styles to multiple elements"
    - "Explains why overusing ID selectors can cause specificity problems"
  keywords: [selector, class, id, element, combinator, descendant, grouped, specificity, dot, hash]
  modelAnswer: |
    CSS selectors target elements to style. Element selectors (h1) target all of
    that type. Class selectors (.card) target any element with that class and can
    be reused. ID selectors (#hero) target one unique element — use sparingly due
    to high specificity. Combinators like the space (descendant) target elements
    nested inside others.
guidedSteps:
  - id: fe-app-m3-02-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which selector targets only elements with class="highlight"?
    inputConfig:
      options:
        - "highlight { }"
        - "#highlight { }"
        - ".highlight { }"
        - "*highlight { }"
    markingRule:
      matchMode: NORMALIZED
      accepted: [".highlight { }"]
      rejectedFeedback: "Class selectors use a dot prefix: .highlight targets any element with class=\"highlight\". # is for IDs. Without a prefix, it is an element selector (targets the <highlight> element, which doesn't exist)."
    hint: "Class selectors have a specific prefix character."
    reflectionPrompt: "Classes are reusable — you can apply .highlight to a <p>, a <span>, a <div>. IDs are unique — one per page. This is why classes are used far more often in real CSS."

  - id: fe-app-m3-02-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete this selector that targets only <p> elements inside a <div class="content">:

      `.content ___ { }`
    inputConfig:
      placeholder: "p"
    markingRule:
      matchMode: CONTAINS
      accepted: [p]
      rejectedFeedback: "The descendant combinator is a space: .content p targets any <p> that is a descendant of an element with class=\"content\". It doesn't have to be a direct child — just anywhere inside."
    hint: "A space between two selectors creates a descendant relationship."
    reflectionPrompt: "Descendant combinators are powerful but can be too broad. .content p matches every <p> no matter how deeply nested. The child combinator (>) is stricter: .content > p only matches direct children."

  - id: fe-app-m3-02-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Write a single CSS rule that gives both h1 and h2 elements the colour #7c3aed (purple).
    inputConfig:
      minWords: 1
    markingRule:
      matchMode: CONTAINS
      accepted: [h1, h2, color, 7c3aed]
      rejectedFeedback: "Use a grouped selector: h1, h2 { color: #7c3aed; }. The comma groups multiple selectors — the same declarations apply to all of them. This avoids duplicating the same rule."
    hint: "CSS has a syntax for applying the same declarations to multiple selectors at once."
    reflectionPrompt: "Grouped selectors (h1, h2, h3) prevent repetition. But be careful: if you add a selector that shouldn't have the styles (h4) just because it's convenient, you've coupled unrelated things. Group only what truly belongs together."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does the selector `nav a` target?"
    options:
      - "Elements with both class 'nav' and class 'a'"
      - "All <a> elements that are descendants of a <nav> element"
      - "Only <a> elements that are direct children of <nav>"
      - "The <nav> and <a> elements themselves"
    correctIndex: 1
    feedback: "nav a is a descendant combinator — it targets any <a> anywhere inside a <nav>. For direct children only, use nav > a. For adjacent siblings, use nav + a."
  - type: MULTIPLE_CHOICE
    question: "You need to style a unique page hero section differently from all others. Which selector is most appropriate?"
    options:
      - ".hero { } — a class selector"
      - "#hero { } — an ID selector"
      - "hero { } — an element selector"
      - "*hero { } — a universal selector"
    correctIndex: 0
    feedback: "Even for unique elements, prefer a class selector. ID selectors have very high specificity that makes them hard to override. Using .hero consistently is simpler and more maintainable — reserve IDs for JavaScript hooks and fragment links."

retrieval:
  recall: "Write selectors for: all paragraphs, elements with class 'card', the element with id 'main-title'."
  explain: "Explain why classes are preferred over IDs for styling, even when an element is unique on the page."
  mistakeId:
    code: "#nav a { color: blue; } — hard to override later"
    answer: "ID selectors have specificity (1,0,0) — far higher than classes (0,1,0). Adding #nav makes the selector very hard to override without another ID or !important. Use .nav a instead for the same targeting with manageable specificity."
---

# Hook

Imagine a CSS file with no way to say *which* elements to style. Every rule would apply to everything. That would be chaos.

Selectors are how you aim CSS. They are the targeting system — the difference between painting an entire building one colour and painting only the front door.

Mastering selectors is mastering control.

# Lore Introduction

*"In the Academy archives,"* says Master Aelindra, *"a scribe who marks 'apply this ink to all parchment' destroys everything. A scribe who marks 'apply this ink to parchments with the blue seal in the eastern wing' is precise. CSS selectors are that precision."*

# Core Learning

## Concept Introduction

| Selector | Syntax | Targets |
|---|---|---|
| Element | `p` | All `<p>` elements |
| Class | `.card` | All elements with `class="card"` |
| ID | `#hero` | The element with `id="hero"` |
| Universal | `*` | All elements |
| Grouped | `h1, h2` | All h1 AND all h2 |
| Descendant | `nav a` | All `<a>` inside any `<nav>` |
| Child | `ul > li` | `<li>` that are direct children of `<ul>` |
| Pseudo-class | `a:hover` | `<a>` when the cursor is over it |
| Attribute | `input[type="email"]` | `<input>` with that attribute value |

## Why It Matters

Selectors determine the precision and reusability of your CSS. Too broad (element selectors for everything) and changes cascade unexpectedly. Too specific (IDs for everything) and overriding becomes a fight. Finding the right level of specificity is a core CSS skill.

## Worked Examples

```css
/* Element selector */
p { line-height: 1.6; }

/* Class selector — reusable across elements */
.card { border: 1px solid #e2e8f0; border-radius: 8px; }

/* ID selector — unique element */
#site-header { background: #0c0a1e; }

/* Descendant combinator */
.nav-list a { text-decoration: none; color: white; }

/* Pseudo-class */
a:hover { text-decoration: underline; }

/* Attribute selector */
input[type="email"] { border-color: #7c3aed; }

/* Grouped */
h1, h2, h3 { font-family: 'Inter', sans-serif; }
```

## Common Mistakes

- **ID selectors for styling:** Specificity is too high — use classes.
- **Overly specific selectors:** `div.container ul.list li a` — fragile, hard to reuse.
- **Forgetting the dot for classes:** `card { }` targets a `<card>` element (doesn't exist). `.card { }` targets elements with `class="card"`.

## Mental Model

Selectors are **search queries** for your HTML. `p` is "find all paragraphs." `.card` is "find everything labelled 'card'." `#hero` is "find the one thing named hero." `nav a` is "find all links inside navigation."

## Mini Summary

- ✔ Element (`p`), class (`.name`), ID (`#name`) — the three core types
- ✔ Classes are reusable; IDs are unique — prefer classes for styling
- ✔ Descendant (` `), child (`>`), grouped (`,`) — the main combinators
- ✔ Pseudo-classes (`:hover`, `:focus`) target state

# Guided Practice Quest

**The Targeting System** — practice writing and identifying selectors. Steps in `guidedSteps`.

# Solo Practice Quest

Given this HTML, write CSS selectors for: all headings, only the hero section, links inside the nav, the email input, and any element with class "featured". Write the selector and one style declaration for each.

# Integration

**Connecting to Mathematics — Set Theory and Intersection**

CSS selectors are set operations. An element selector (`p`) defines a set: all paragraph elements. A class selector (`.card`) defines another set. Combining them (`p.card`) defines the intersection: paragraphs that are also cards. Descendant selectors define filtered subsets. Understanding CSS selectors as set operations makes complex selectors intuitive — you are simply defining which subset of all elements to style.

# Lore Conclusion

*"A selector without precision,"* says Master Aelindra, *"is a spell cast at the horizon. It hits everything and nothing. A selector with precision is a spell cast at the specific rune you need. Learn to aim."*

---
