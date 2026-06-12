---
id: fe-app-m3-10
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m3
moduleTitle: "Module 3: CSS Foundations"
moduleGlyph: "🎨"
moduleSortOrder: 3
topicSlug: layout
topicTitle: "Layout"
topicSortOrder: 2
lesson: layout_patterns
title: "Layout Patterns"
sortOrder: 5
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
    - "Recognises and implements a holy grail layout (header, sidebar, main, footer)"
    - "Implements a card grid that wraps responsively"
    - "Centres content both horizontally and vertically"
    - "Uses max-width with margin: auto to constrain content width"
    - "Combines Grid and Flexbox appropriately (Grid for page, Flex for components)"
  keywords: [holy-grail, card-grid, centring, max-width, container, wrapper, sticky-footer, layout-pattern]
  modelAnswer: |
    Common layout patterns combine Grid and Flexbox appropriately. Page-level layout
    uses Grid (header, sidebar, main, footer). Component-level layout uses Flexbox
    (nav items, card internals). Content width is constrained with max-width and
    centred with margin: auto. Sticky footers use Grid with a stretch row or
    min-height: 100vh on the body with a flex column layout.
guidedSteps:
  - id: fe-app-m3-10-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You want a content area that is at most 1200px wide and centred on wider screens. Which CSS achieves this?
    inputConfig:
      options:
        - "width: 1200px; position: center"
        - "max-width: 1200px; margin: 0 auto"
        - "width: 50%; align: center"
        - "min-width: 1200px; text-align: center"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["max-width: 1200px; margin: 0 auto"]
      rejectedFeedback: "max-width: 1200px limits the element to 1200px on wide screens but allows it to be narrower on small screens. margin: 0 auto centres the element horizontally by distributing remaining space equally on both sides. This is the standard responsive container pattern."
    hint: "You need it to be at MOST 1200px, not exactly 1200px."
    reflectionPrompt: "The max-width + margin: auto pattern is one of the most used in CSS. It creates a responsive content container: full-width on mobile, constrained and centred on desktop. Add padding: 0 16px inside for breathing room at small sizes."

  - id: fe-app-m3-10-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To prevent the footer from floating up when page content is short, you can use: `body { display: flex; flex-direction: column; min-height: ___vh; }` and `main { flex: ___; }`.
    inputConfig:
      placeholder: "100 / 1"
    markingRule:
      matchMode: CONTAINS
      accepted: ["100", "1"]
      rejectedFeedback: "body min-height: 100vh = full viewport height. main flex: 1 = main content grows to fill remaining space. The header and footer stay at their natural size; main expands to push the footer down. This is the sticky footer pattern."
    hint: "The body should be at least the full viewport height, and main should take up available space."
    reflectionPrompt: "Sticky footer was another notoriously tricky CSS problem before Flexbox. With display: flex; flex-direction: column on body and flex: 1 on main, it is trivially solvable. A great example of how modern CSS collapses complex layout problems into simple rules."

  - id: fe-app-m3-10-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe in 2-3 sentences how you would combine Grid and Flexbox to build a blog page: a two-column layout (sidebar + main content) where each blog card in the main area has a title, date, and "Read more" button.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [grid, flex, sidebar, main, card, column]
      rejectedFeedback: "Use Grid for the two-column page layout (sidebar + main). Inside main, use Grid or Flexbox for the card list. Inside each card, use Flexbox (flex-direction: column, justify-content: space-between) so the button sticks to the bottom regardless of content length."
    hint: "Grid for the page structure, Flexbox for the component internals."
    reflectionPrompt: "The best CSS architectures use Grid for page-level structure and Flexbox for component-level layout. They are complementary, not competing. Flexbox inside a Grid cell is normal and expected."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the 'holy grail' layout in web design?"
    options:
      - "A layout with exactly 12 columns"
      - "A page with header, left sidebar, main content, right sidebar, and footer"
      - "A layout with no CSS frameworks"
      - "A layout that requires no CSS"
    correctIndex: 1
    feedback: "The 'holy grail' layout is header + two sidebars + main content + footer — notoriously difficult before Grid and Flexbox. With CSS Grid, it is straightforward: define the columns and use grid-template-areas to name regions."
  - type: MULTIPLE_CHOICE
    question: "Which CSS pattern creates a responsive image grid with no media queries?"
    options:
      - "display: flex; flex-wrap: wrap"
      - "display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr))"
      - "display: inline-block on each image"
      - "float: left on each image"
    correctIndex: 1
    feedback: "repeat(auto-fill, minmax(200px, 1fr)) automatically creates as many columns as fit in the available width, each at least 200px. As the screen narrows, fewer columns fit — the grid adapts without a single media query."

retrieval:
  recall: "Write the CSS for a constrained, centred content wrapper that is max 1100px wide."
  explain: "Explain the sticky footer problem and how to solve it using Flexbox."
  mistakeId:
    code: "width: 800px on a content area — breaks on screens narrower than 800px"
    answer: "Use max-width: 800px instead. It allows the element to be narrower than 800px on small screens, preventing horizontal overflow. Always use max-width for content containers, never fixed width."
---

# Hook

Individual CSS properties are ingredients. Layout patterns are recipes.

Knowing what `display: grid` and `justify-content` do is essential — but knowing how to combine them into working layouts is what makes a frontend engineer productive.

This lesson is about the patterns: proven combinations of CSS that solve the most common layout challenges.

# Lore Introduction

*"An apprentice who knows every ingredient but cannot cook a meal,"* says Master Aelindra, *"has learned only half the craft. Today, we cook. We take what you know — Grid, Flexbox, positioning — and combine them into the layouts you will build again and again in your career."*

# Core Learning

## Concept Introduction

**Pattern 1 — Content Container (constrained + centred):**
```css
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 16px; /* breathing room at small sizes */
}
```

**Pattern 2 — Page Layout (holy grail):**
```css
.page {
  display: grid;
  grid-template-columns: 250px 1fr;
  grid-template-rows: auto 1fr auto;
  grid-template-areas:
    "header header"
    "sidebar main"
    "footer footer";
  min-height: 100vh;
}
.header { grid-area: header; }
.sidebar { grid-area: sidebar; }
.main { grid-area: main; }
.footer { grid-area: footer; }
```

**Pattern 3 — Responsive Card Grid:**
```css
.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}
```

**Pattern 4 — Sticky Footer:**
```css
body {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
main { flex: 1; }
```

**Pattern 5 — Card with Button at Bottom:**
```css
.card {
  display: flex;
  flex-direction: column;
}
.card-content { flex: 1; }
/* Button naturally sits at the bottom */
```

**Pattern 6 — Perfect Centring:**
```css
.centred {
  display: flex;
  justify-content: center;
  align-items: center;
}
```

## Why It Matters

These six patterns cover 80% of real-world layout needs. Recognising them when you see a design and reaching for them immediately is the mark of an efficient frontend engineer.

## Common Mistakes

- Using `width` instead of `max-width` for containers
- Reinventing patterns from scratch instead of recognising the standard solution
- Using Grid for everything when Flexbox is simpler for one-dimensional cases

## Mental Model

Layout patterns are chess openings. Every game of chess is different, yet strong players don't reinvent the first ten moves — they recognise the position and play a named, battle-tested line. The holy grail layout, the card grid, the sidebar split, the centred hero: these are openings. A new design lands on your desk and instead of a blank-canvas panic you think "that's a card grid with a sticky header — I know this position." Study the patterns until recognition is instant; then your creative energy goes into what makes this page *unique*, not into re-deriving solved problems.

## Mini Summary

- ✔ Container: `max-width` + `margin: 0 auto`
- ✔ Holy grail: Grid with named areas
- ✔ Card grid: `repeat(auto-fill, minmax(...))`
- ✔ Sticky footer: Flex column + `flex: 1` on main
- ✔ Perfect centring: Flex + `justify-content: center` + `align-items: center`

# Guided Practice Quest

**The Architect's Toolkit** — identify and implement the right pattern for each scenario. Steps in `guidedSteps`.

# Solo Practice Quest

Choose a real website you use (news site, portfolio, dashboard). Identify: what layout pattern is used for the page structure? What pattern is used inside each card/component? Write the CSS skeleton for the page and one component, using the patterns from this lesson.

# Integration

**Connecting to Psychology — Pattern Recognition and Expert Performance**

Research in expertise (Chase & Simon, 1973) found that chess grandmasters recognise positions, not individual pieces — they see meaningful patterns instantly. Frontend experts work the same way: they see a design and immediately recognise "this is a holy grail with a responsive card grid inside main." Pattern recognition reduces cognitive load and increases speed. This lesson is about building that pattern library in your memory — not just understanding the CSS, but recognising which pattern applies on sight.

# Lore Conclusion

*"These patterns,"* says Master Aelindra, closing her notebook, *"you will use thousands of times in your career. They will become automatic — a card grid, a holy grail, a sticky footer — summoned without thought. That is mastery: not knowing every spell, but knowing the right spell for each situation without hesitation."*

---
