---
id: fe-app-m3-11
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m3
moduleTitle: "Module 3: CSS Foundations"
moduleGlyph: "🎨"
moduleSortOrder: 3
topicSlug: visual_design
topicTitle: "Visual Design"
topicSortOrder: 3
lesson: typography
title: "Typography"
sortOrder: 1
difficulty: 2
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
    - "Explains the difference between serif and sans-serif typefaces and when to use each"
    - "Sets up a type scale using rem units consistently"
    - "Uses line-height correctly to improve readability"
    - "Limits body text line length using max-width or ch units"
    - "Applies font-weight appropriately for hierarchy"
  keywords: [typography, font, serif, sans-serif, line-height, font-size, font-weight, type-scale, ch, readability]
  modelAnswer: |
    Typography controls the readability and hierarchy of text. Serif fonts (with
    small strokes at letter ends) suit long-form reading; sans-serif fonts suit
    screens and UI text. A type scale (e.g., 1rem body, 1.25rem h3, 1.5rem h2,
    2rem h1) creates visual hierarchy. Line-height of 1.5–1.7 improves body text
    readability. Body text lines should be 60–75 characters wide (max-width: 65ch).
guidedSteps:
  - id: fe-app-m3-11-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Why is a line-height of 1.0 (no extra space between lines) considered bad for body text readability?
    inputConfig:
      options:
        - "It makes the text too small"
        - "Lines merge visually — the eye loses its place when moving to the next line"
        - "Browsers do not support line-height: 1.0"
        - "It causes accessibility violations automatically"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Lines merge visually — the eye loses its place when moving to the next line"]
      rejectedFeedback: "Without vertical spacing between lines, adjacent lines of text blur together. The eye struggles to track from the end of one line to the beginning of the next. 1.5–1.7 is the research-backed range for comfortable body text reading."
    hint: "Think about what happens when you read dense, single-spaced text on a screen."
    reflectionPrompt: "WCAG 1.4.12 (Text Spacing) requires that line-height be at least 1.5 times font size for body text. This is not just a design preference — it is a documented accessibility requirement for users with dyslexia and cognitive disabilities."

  - id: fe-app-m3-11-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To limit body text to approximately 65 characters per line (the optimal reading width), use:

      `max-width: 65___;`
    inputConfig:
      placeholder: "ch"
    markingRule:
      matchMode: CONTAINS
      accepted: [ch]
      rejectedFeedback: "The `ch` unit = the width of the '0' character in the current font. max-width: 65ch limits the container to approximately 65 characters per line, which is the research-backed optimal for comfortable reading — narrow enough to prevent eye fatigue."
    hint: "CSS has a unit based on the width of a character."
    reflectionPrompt: "Optimal reading line length is 50–75 characters (Robert Bringhurst's 'The Elements of Typographic Style'). The ch unit makes this constraint font-relative — it scales correctly when users change their font size."

  - id: fe-app-m3-11-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      What is a 'type scale' and why should you use one instead of choosing font sizes arbitrarily?
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [scale, ratio, hierarchy, consistent, size, heading, body, visual]
      rejectedFeedback: "A type scale is a set of font sizes with consistent ratios (e.g., multiplied by 1.25 each step). Using a scale creates visual harmony — sizes feel related rather than random. It establishes clear hierarchy (body < h3 < h2 < h1) and is easy to implement with CSS custom properties."
    hint: "Think about what happens to the visual design if you pick font sizes randomly."
    reflectionPrompt: "Popular scales: Major Second (1.125×), Minor Third (1.2×), Major Third (1.25×). Tools like type-scale.com generate them. Defining a scale in CSS custom properties (--text-sm, --text-base, --text-lg, --text-xl) makes it easy to use consistently."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You want to use a Google Font. Where in your HTML do you place the <link> for it?"
    options:
      - "At the end of <body>"
      - "Inside a <style> tag in the body"
      - "In the <head>, before your stylesheet link"
      - "After the closing </html> tag"
    correctIndex: 2
    feedback: "Font imports go in <head>, ideally before your own stylesheet link so the font is available when your CSS is parsed. Preloading with <link rel=\"preload\"> and using font-display: swap improves performance further."
  - type: MULTIPLE_CHOICE
    question: "What does `font-weight: 700` correspond to?"
    options:
      - "70% opacity"
      - "Bold weight"
      - "Extra light"
      - "7px letter spacing"
    correctIndex: 1
    feedback: "Font weights: 100 (thin) through 900 (black). 400 = regular/normal, 700 = bold. Not all fonts have all weights — check which weights you've loaded. Using a weight that isn't loaded causes the browser to simulate it (poorly)."

retrieval:
  recall: "Write the CSS for body text: 1rem size, 1.6 line-height, max 65ch width, dark grey colour."
  explain: "Explain why limiting line length to 65ch improves readability."
  mistakeId:
    code: "font-size: 11px for body text — too small for comfortable reading"
    answer: "Body text below 16px (1rem) is difficult to read for many users, especially on high-density screens. Browser default is 16px for good reason. Use 1rem (inherits user preference) or 16-18px minimum. Never set font sizes below 12px."
---

# Hook

Typography is not decoration. It is communication.

Ninety percent of web design is typography — the choice of typeface, the size relationships between headings and body text, the space between lines. Get typography right and a page feels effortless to read. Get it wrong and users leave, even if the content is excellent.

> Find a webpage you find easy to read. Look at the font, size, line height, and line length. What makes it comfortable?

# Lore Introduction

*"The Academy's scribes,"* says Master Aelindra, *"spend years learning a single craft: how to arrange letters on a page so the reader never thinks about the letters. When typography works, it is invisible. When it fails, the reader puts down the scroll. We will learn to make it invisible."*

# Core Learning

## Concept Introduction

**Typeface categories:**

| Category | Examples | Best for |
|---|---|---|
| Serif | Georgia, Times New Roman | Long-form reading, print |
| Sans-serif | Inter, Helvetica, Arial | Screen UI, headings |
| Monospace | JetBrains Mono, Courier | Code, technical content |

**Key typography CSS:**

```css
body {
  font-family: 'Inter', system-ui, sans-serif;
  font-size: 1rem;         /* 16px base */
  font-weight: 400;
  line-height: 1.6;        /* comfortable reading */
  color: #1a1a2e;
}

/* Limit line length for readability */
.prose { max-width: 65ch; }

/* Type scale (Major Third — 1.25×) */
h1 { font-size: 2.441rem; font-weight: 700; }
h2 { font-size: 1.953rem; font-weight: 700; }
h3 { font-size: 1.563rem; font-weight: 600; }
h4 { font-size: 1.25rem;  font-weight: 600; }
p  { font-size: 1rem; }
small { font-size: 0.8rem; }
```

## Why It Matters

Good typography makes content readable and hierarchy clear without conscious effort from the reader. Bad typography creates friction that competes with the content for the reader's attention.

## Common Mistakes

- Too-small body font (below 16px)
- Line-height below 1.4 for body text
- No maximum line length — 120+ character lines are exhausting to track
- Too many typefaces (max 2 per page)

## Mini Summary

- ✔ Sans-serif for UI, serif for long reading — match typeface to use case
- ✔ Base size: 1rem (16px), line-height: 1.5–1.7 for body
- ✔ Use a type scale for consistent size relationships
- ✔ `max-width: 65ch` on body text — optimal reading line length
- ✔ Maximum 2 typefaces per page

# Guided Practice Quest

**The Typographer's Eye** — three questions on CSS typography. Steps in `guidedSteps`.

# Solo Practice Quest

Define a complete typography system for a blog: base font, type scale (at least 4 sizes), line-height, body max-width, and heading weights. Write it as CSS custom properties and use them in element rules. Justify one font choice in a comment.

# Integration

**Connecting to History — Gutenberg and the Birth of Typography**

Johannes Gutenberg's printing press (c.1440) standardised type — consistent letterforms, measured spacing, deliberate line lengths. Centuries of typographic refinement followed: Garamond's proportions, Bodoni's contrast, Helvetica's neutrality. Every principle in this lesson — optimal line length, consistent type scales, appropriate line spacing — was established by printers who observed how humans actually read. Web typography inherits 600 years of accumulated knowledge. When WCAG requires 1.5× line-height, it echoes what compositors knew in the 17th century.

# Lore Conclusion

*"The best typeface,"* says Master Aelindra, *"is the one the reader never notices. Choose it well, size it correctly, give it room to breathe. Then stand back and let the content speak."*

---
