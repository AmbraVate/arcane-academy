---
id: fe-app-m4-04
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m4
moduleTitle: "Module 4: Responsive Design"
moduleGlyph: "📱"
moduleSortOrder: 4
topicSlug: responsive_techniques
topicTitle: "Responsive Techniques"
topicSortOrder: 2
lesson: media_queries
title: "Media Queries"
sortOrder: 1
difficulty: 2
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
    - "Writes syntactically correct media queries for min-width and max-width"
    - "Explains what a breakpoint is and when to add one"
    - "Uses logical operators (and, or, not) in media queries"
    - "Distinguishes between screen width and print media types"
    - "Nests media queries inside CSS rules (modern syntax)"
  keywords: [media-query, breakpoint, min-width, max-width, screen, print, and, or, "@media", condition]
  modelAnswer: |
    Media queries apply CSS rules conditionally based on device or viewport characteristics.
    Syntax: @media (min-width: 768px) { rules }. Breakpoints should be set where content
    breaks, using min-width for mobile-first. Multiple conditions combine with 'and'.
    Modern CSS allows nesting: @media (min-width: 768px) inside a rule. Media types
    (screen, print) target different output contexts.
guidedSteps:
  - id: fe-app-m4-04-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A media query `@media (min-width: 768px) and (max-width: 1023px)` applies when:
    inputConfig:
      options:
        - "Only on devices that are exactly 768px wide"
        - "On viewports between 768px and 1023px inclusive"
        - "On any screen wider than 768px"
        - "On tablets only — this is the official tablet query"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["On viewports between 768px and 1023px inclusive"]
      rejectedFeedback: "This is a range query: min-width: 768px means 768px or wider; max-width: 1023px means 1023px or narrower. Combined with 'and', both conditions must be true — so it applies from 768px to 1023px. Often called a 'tablet-only' query, though real tablet widths vary."
    hint: "Both conditions must be true simultaneously."
    reflectionPrompt: "Range queries are useful for isolated layout changes (a specific tablet layout that you don't want to apply on desktop). But they create more breakpoints to maintain. Mobile-first min-width queries are usually simpler — add the desktop styles, let them cascade down."

  - id: fe-app-m4-04-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the media query that targets print output (so you can hide navigation when the page is printed):

      `@media ___ { .nav { display: none; } }`
    inputConfig:
      placeholder: "print"
    markingRule:
      matchMode: CONTAINS
      accepted: [print]
      rejectedFeedback: "@media print targets print output — printed pages and print preview. Use it to hide navigation, sidebars, and interactive elements; ensure text is black on white; add page-break controls. This is an underused media type that dramatically improves the print experience."
    hint: "The other major media type besides 'screen'."
    reflectionPrompt: "@media print is worth adding to any content-heavy page. Print: hide nav, hide sidebar, make all text black, expand links to show URLs. Users who print pages notice the care taken. It also helps users who export pages to PDF."

  - id: fe-app-m4-04-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Write the CSS (including a media query) to change a navigation from a single-column vertical list on mobile to a horizontal row on screens 768px and wider.
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: [flex, min-width, 768px, row, nav]
      rejectedFeedback: ".nav { display: flex; flex-direction: column; } @media (min-width: 768px) { .nav { flex-direction: row; } } — base styles are mobile (column), media query adds the horizontal layout for tablet+."
    hint: "Start with column layout (mobile), add row layout inside a min-width media query."
    reflectionPrompt: "This is one of the most common responsive patterns: navigation stacks vertically on mobile, goes horizontal on wider screens. The mobile-first approach means the fallback (column) works without JavaScript and without the media query being parsed."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which modern CSS feature allows you to write media queries nested inside a selector rule?"
    options:
      - "This is not possible — media queries must always be at the top level"
      - "CSS Nesting (supported in all modern browsers)"
      - "SCSS only — not in plain CSS"
      - "Only with a CSS preprocessor like PostCSS"
    correctIndex: 1
    feedback: "CSS Nesting is now supported natively in all major browsers. You can write: .nav { flex-direction: column; @media (min-width: 768px) { flex-direction: row; } } — the media query is scoped to the .nav rule. This keeps responsive styles co-located with the component."
  - type: MULTIPLE_CHOICE
    question: "What does `@media (prefers-color-scheme: dark)` do?"
    options:
      - "Adds a dark mode toggle button"
      - "Applies styles when the user's OS is set to dark mode"
      - "Changes the screen brightness"
      - "Targets screens with black backgrounds"
    correctIndex: 1
    feedback: "prefers-color-scheme is a user preference media feature. dark applies when the OS is set to dark mode. This allows you to provide a dark theme automatically without JavaScript — just define :root dark-mode colour variables inside @media (prefers-color-scheme: dark)."

retrieval:
  recall: "Write a media query that changes a grid from 1 column to 3 columns at 1024px."
  explain: "Explain the difference between min-width and max-width in a media query context."
  mistakeId:
    code: "@media (max-width: 1024px) { .sidebar { display: none; } } — hiding sidebar on iPad Pro"
    answer: "max-width: 1024px includes iPad Pro in landscape mode. Consider whether hiding the sidebar there is intentional. If you only want to hide it on phones, use max-width: 767px. Always check what devices fall within your breakpoint range."
---

# Hook

A media query is a conditional statement for CSS. `@media (min-width: 768px)` is CSS saying: "if the viewport is at least 768px wide, apply these rules."

It is the mechanism that makes responsive design possible. Master media queries and you can build layouts that adapt to any screen.

# Lore Introduction

*"The Academy's illuminated manuscripts,"* says Master Aelindra, *"are written differently depending on who will read them. A field report is concise; a ceremonial copy is ornate. The scribe selects the format based on the context. @media is the selector for context in CSS."*

# Core Learning

## Concept Introduction

```css
/* Syntax */
@media media-type and (feature: value) { CSS rules }

/* Most common: viewport width */
@media (min-width: 768px) { }     /* tablet and above */
@media (max-width: 767px) { }     /* mobile only */
@media (min-width: 1024px) { }    /* laptop and above */

/* Media types */
@media screen { }     /* screens (default) */
@media print { }      /* print output */

/* Multiple conditions */
@media (min-width: 768px) and (max-width: 1023px) { } /* tablet only */
@media (min-width: 600px) or (orientation: landscape) { }

/* User preferences */
@media (prefers-color-scheme: dark) { }
@media (prefers-reduced-motion: reduce) { }
@media (hover: hover) { }  /* device has hover capability */
```

**Common mobile-first breakpoint set:**
```css
/* Base = mobile */
@media (min-width: 640px)  { /* sm  */ }
@media (min-width: 768px)  { /* md  */ }
@media (min-width: 1024px) { /* lg  */ }
@media (min-width: 1280px) { /* xl  */ }
```

## Common Mistakes

- **Using `max-width` in a mobile-first codebase**: Mixing `min-width` and `max-width` queries creates conflicting specificity. Pick one direction — prefer `min-width` for mobile-first development.
- **Targeting specific device widths instead of content break points**: `@media (max-width: 768px)` assumes tablets are exactly 768px wide. Device sizes vary; set breakpoints where your specific content breaks.
- **Forgetting `prefers-reduced-motion`**: Animations that run for everyone ignore users who experience motion sickness. `@media (prefers-reduced-motion: reduce)` should always be checked when adding transitions or animations.
- **Writing media queries far from the component they affect**: Keeping all breakpoints in a single file at the bottom makes it hard to understand what changes at each viewport. Co-locating media queries with the affected selector is more maintainable.

## Why It Matters

Media queries are the control flow of responsive design — the `if` statements that adapt layouts to context. Understanding their full capabilities (not just min/max-width) opens up dark mode, reduced motion, print styles, and touch-vs-hover detection.

## Mental Model

Media queries are thermostat rules for your layout. A thermostat doesn't ask *which* house it's in; it reads a measurement — "below 18°? heating on" — and acts. A media query reads the viewport: "narrower than 768px? stack the columns." You aren't writing designs for an iPhone or a desktop; you're writing *condition → response* rules that any current or future device evaluates for itself. The skill is choosing trigger points where your *content* visibly struggles (lines too long, cards too cramped) — set the thermostat where the room actually gets cold, not where a device catalogue says it might.

## Mini Summary

- ✔ `@media (min-width: N)` — mobile-first: styles apply at N and above
- ✔ `@media (max-width: N)` — desktop-first: styles apply at N and below
- ✔ `@media print` — styles for print output
- ✔ `@media (prefers-color-scheme: dark)` — automatic dark mode
- ✔ `@media (prefers-reduced-motion: reduce)` — respect user animation preferences

# Guided Practice Quest

**The Conditional Scribe** — three questions on media query syntax and usage. Steps in `guidedSteps`.

# Solo Practice Quest

Write a complete responsive stylesheet for a simple page with: a mobile-first single-column layout, a two-column layout at 768px, a three-column layout at 1024px, dark mode colour overrides, and print styles that hide navigation.

# Integration

**Connecting to Mathematics — Boolean Logic and Conditional Expressions**

Media queries are boolean expressions evaluated against current conditions. `@media (min-width: 768px) and (prefers-color-scheme: dark)` is a logical AND: both conditions must be true. `@media (orientation: portrait) or (max-width: 600px)` is a logical OR: either condition suffices. NOT inverts: `@media not (hover: hover)`. This maps directly to boolean algebra — the same logical foundations as programming conditionals and database queries. Understanding media queries as boolean expressions makes complex multi-condition queries straightforward to reason about.

# Lore Conclusion

*"Media queries are questions your CSS asks of the world: how wide is the canvas? Is it dark? Will this be printed? The answers determine the presentation. Ask the right questions, apply the right styles. The page adapts not because it guesses — but because it observes and responds."*

---
