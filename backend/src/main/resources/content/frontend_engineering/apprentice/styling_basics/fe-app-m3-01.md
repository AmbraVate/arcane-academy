---
id: fe-app-m3-01
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
lesson: what_is_css
title: "What is CSS?"
sortOrder: 1
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, history]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what CSS does and why it is separate from HTML"
    - "Names the three ways to apply CSS and their trade-offs"
    - "Writes a correct CSS rule with selector, property, and value"
    - "Explains what 'separation of concerns' means in the HTML/CSS relationship"
    - "Describes what happens when CSS is disabled or fails to load"
  keywords: [CSS, stylesheet, selector, property, value, rule, separation, inline, external]
  modelAnswer: |
    CSS (Cascading Style Sheets) controls the visual presentation of HTML.
    It is kept separate from HTML to enforce separation of concerns — HTML
    handles structure and meaning, CSS handles appearance. External stylesheets
    allow one CSS file to style hundreds of pages. A CSS rule consists of a
    selector (what to target) and a declaration block of property: value pairs.
guidedSteps:
  - id: fe-app-m3-01-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the primary reason CSS is kept in separate files from HTML?
    inputConfig:
      options:
        - "Browsers cannot read CSS inside HTML files"
        - "Separation of concerns — structure (HTML) and presentation (CSS) should be independent"
        - "CSS files load faster than inline styles"
        - "External CSS is required by the HTML5 specification"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Separation of concerns — structure (HTML) and presentation (CSS) should be independent"]
      rejectedFeedback: "Separation of concerns is the core principle. HTML defines what content is. CSS defines how it looks. Keeping them separate means you can change the appearance of an entire website by editing one CSS file, without touching any HTML."
    hint: "Think about what happens when a designer wants to redesign the site without touching the content."
    reflectionPrompt: "This is one of the most important software engineering principles applied to the web. A site can have one CSS file styling 10,000 HTML pages. Change the CSS file once — all pages update instantly. This is the power of separation."

  - id: fe-app-m3-01-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete this CSS rule that makes all h1 elements red:

      `___ { color: red; }`
    inputConfig:
      placeholder: "h1"
    markingRule:
      matchMode: CONTAINS
      accepted: [h1]
      rejectedFeedback: "A CSS rule is: selector { property: value; }. The selector targets what to style (h1 = all h1 elements). The property (color) says what to change. The value (red) says what to change it to."
    hint: "The selector is the element you want to target."
    reflectionPrompt: "This is the atomic unit of CSS: selector + declaration block. Every CSS file is a collection of these rules. Understanding this structure lets you read and write any CSS, no matter how complex."

  - id: fe-app-m3-01-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 2–3 sentences why using inline styles (style="...") throughout your HTML is considered a bad practice.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [maintenance, change, update, separate, reuse, scattered, html]
      rejectedFeedback: "Inline styles scatter presentation throughout HTML. To change a colour used 50 times, you must edit 50 places. They also have very high specificity, making them hard to override. External stylesheets centralise all styling in one maintainable file."
    hint: "What happens when you need to change the same colour on 100 elements?"
    reflectionPrompt: "Inline styles break separation of concerns at the most granular level. They are sometimes necessary (email HTML, dynamic JS-driven styles) but should never be the default approach. The cost in maintainability is too high."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "How do you link an external CSS file to an HTML page?"
    options:
      - "<style src=\"styles.css\"></style>"
      - "<link rel=\"stylesheet\" href=\"styles.css\">"
      - "<css href=\"styles.css\">"
      - "<script src=\"styles.css\"></script>"
    correctIndex: 1
    feedback: "The <link> element in the <head> connects external resources. rel=\"stylesheet\" declares it as a stylesheet. href provides the path. This is how virtually every website links CSS."
  - type: MULTIPLE_CHOICE
    question: "CSS stands for:"
    options:
      - "Creative Style System"
      - "Cascading Style Sheets"
      - "Computer Styling Standard"
      - "Custom Site Styling"
    correctIndex: 1
    feedback: "Cascading Style Sheets. The 'Cascading' part is important — it refers to how multiple CSS rules combine and override each other based on specificity and order. You'll learn more about this in the Cascade lesson."

retrieval:
  recall: "Write a CSS rule that makes all paragraphs have a font size of 16px and a grey colour."
  explain: "Explain the three ways to add CSS to an HTML document and when each is appropriate."
  mistakeId:
    code: "Adding style=\"color:red; font-size:16px;\" to every element individually"
    answer: "Inline styles break separation of concerns and make maintenance a nightmare. Use a class in a stylesheet: .highlight { color: red; font-size: 16px; } — then apply the class to any element that needs it."
---

# Hook

HTML gives a page structure. Without CSS, every page looks the same: black text, white background, plain links, no layout.

CSS is where a page becomes a *design*. Typography, colour, spacing, layout, animation — all of it is CSS.

But CSS is more than just making things look nice. Done well, CSS makes a site faster to update, more consistent across pages, and more accessible to all users.

> What is your favourite website to look at? What do you think makes it visually effective?

# Lore Introduction

*"The Academy's blueprints,"* says Master Aelindra, unfurling a scroll, *"describe the structure of every tower: walls, windows, doors. But colour, materials, decoration — that is a separate document entirely. Change the decoration, keep the structure. Change the structure, keep the decoration. Never mix them."*

# Core Learning

## Concept Introduction

**CSS (Cascading Style Sheets)** is the language that controls the visual presentation of HTML documents.

A CSS **rule** has two parts:

```css
selector {
  property: value;
  property: value;
}
```

| Part | Example | Meaning |
|---|---|---|
| Selector | `h1` | Which elements to target |
| Property | `color` | What aspect to change |
| Value | `red` | What to change it to |
| Declaration | `color: red;` | One property-value pair |
| Rule | `h1 { color: red; }` | Selector + all its declarations |

**Three ways to apply CSS:**

| Method | Example | Use case |
|---|---|---|
| External stylesheet | `<link rel="stylesheet" href="style.css">` | Always — best practice |
| Internal `<style>` | Inside `<head>` | Prototyping, email templates |
| Inline `style=` | `<p style="color:red">` | Dynamic JS-driven styles only |

## Why It Matters

CSS separates how a page *looks* from what it *is*. One CSS file can style an entire website of thousands of pages. Change a brand colour in one line — every page updates. Without this separation, a design change requires editing every HTML file.

## Worked Examples

```css
/* A complete CSS file for a simple page */

body {
  font-family: 'Inter', sans-serif;
  font-size: 16px;
  color: #1a1a2e;
  background-color: #f8fafc;
}

h1 {
  font-size: 2rem;
  color: #7c3aed;
}

p {
  line-height: 1.6;
  margin-bottom: 1rem;
}

a {
  color: #7c3aed;
  text-decoration: underline;
}
```

## Common Mistakes

- **Inline styles for everything:** Not reusable, hard to maintain, high specificity.
- **Forgetting the semicolon:** `color: red` without `;` — the next property may fail to parse.
- **Wrong property names:** `colour` instead of `color` (CSS uses American English).
- **Linking CSS at the bottom of `<body>`:** Stylesheets should be in `<head>` to prevent flash of unstyled content.

## Mental Model

Think of HTML as the **floor plan** of a building and CSS as the **interior design specifications**. The floor plan says: there is a room here, a door there. The interior design says: this room is painted blue, these floors are oak, these walls are exposed brick. The two documents refer to each other but are maintained separately.

## Mini Summary

- ✔ CSS controls visual presentation — colour, layout, typography, spacing
- ✔ A CSS rule: `selector { property: value; }`
- ✔ External stylesheets (`<link>`) are the best practice — one file, entire site
- ✔ Separation of concerns: HTML = structure, CSS = presentation
- ✔ Inline styles should be avoided except for dynamic JS-driven values

# Guided Practice Quest

**The Decorator's Scroll** — three questions on CSS fundamentals. Steps in the frontmatter `guidedSteps` section.

# Solo Practice Quest

Write a CSS file for a simple blog page that styles: `body` (font, background), `h1` and `h2` (size, colour), `p` (line-height, margin), and `a` (colour, hover state). Explain each decision in a comment above the rule.

# Integration

**Connecting to History — The Web's Unstyled Origins**

The early web (1991–1994) had no CSS. HTML was used for both structure and presentation — `<font>` tags, `bgcolor` attributes, `<center>` elements. Pages were visually chaotic because every developer mixed content and style differently.

Håkon Wium Lie proposed CSS in 1994 to solve this. The W3C published CSS1 in 1996. The principle was simple: let HTML be HTML (structure), let CSS be CSS (style). This separation is now so fundamental that violating it — using `<b>` for importance rather than `<strong>`, or using `<table>` for layout — is considered a technical smell with measurable consequences.

# Lore Conclusion

*"Before CSS,"* says Master Aelindra quietly, *"the web looked like a pile of parchment with the ink still wet. One specification changed everything: keep structure and style separate. Remember that when you are tempted to style a page with inline attributes."*

---
