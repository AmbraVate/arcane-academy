---
id: fe-app-m3-09
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
lesson: css_grid
title: "CSS Grid"
sortOrder: 4
difficulty: 2
estimatedMinutes: 25
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
    - "Applies display:grid and defines columns using grid-template-columns"
    - "Uses repeat() and fr units correctly"
    - "Places an item explicitly using grid-column or grid-row"
    - "Explains when to use Grid vs Flexbox"
    - "Uses gap to add spacing between grid cells"
  keywords: [grid, display, grid-template-columns, fr, repeat, auto-fill, gap, span, grid-column, grid-row]
  modelAnswer: |
    CSS Grid is a two-dimensional layout system. display: grid on a container creates
    a grid; grid-template-columns defines the column structure. The fr unit distributes
    available space proportionally. repeat(3, 1fr) creates three equal columns. Items
    can be placed explicitly with grid-column and grid-row, or placed automatically.
    Grid excels at two-dimensional layouts; Flexbox excels at one-dimensional flow.
guidedSteps:
  - id: fe-app-m3-09-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does `grid-template-columns: repeat(3, 1fr)` create?
    inputConfig:
      options:
        - "Three columns of 1 pixel each"
        - "Three equal columns that share all available space"
        - "Three columns where the first is 1 unit and the others are automatic"
        - "One column repeated three times"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Three equal columns that share all available space"]
      rejectedFeedback: "repeat(3, 1fr) creates 3 columns. 1fr means '1 fraction of available space.' With three 1fr columns, the total space is divided into thirds. repeat() is shorthand for listing the same value multiple times: repeat(3, 1fr) = 1fr 1fr 1fr."
    hint: "fr stands for 'fraction unit' — it divides the available space proportionally."
    reflectionPrompt: "fr units are Grid's killer feature. repeat(12, 1fr) creates a 12-column grid — the standard in most design systems. Columns automatically resize with the container, making fr-based grids inherently responsive."

  - id: fe-app-m3-09-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: `grid-template-columns: ___(auto-fill, minmax(250px, 1fr))` — this creates as many columns as fit, each minimum 250px.
    inputConfig:
      placeholder: "repeat"
    markingRule:
      matchMode: CONTAINS
      accepted: [repeat]
      rejectedFeedback: "repeat(auto-fill, minmax(250px, 1fr)) is a responsive grid pattern that creates as many columns as will fit in the container, each at least 250px wide. Add more screen width — more columns. This is a responsive card grid with no media queries."
    hint: "The same function you used to repeat a column definition."
    reflectionPrompt: "repeat(auto-fill, minmax(250px, 1fr)) is one of the most powerful CSS patterns. A single rule creates a fully responsive grid layout that works from mobile to widescreen without any media queries. Memorise this pattern."

  - id: fe-app-m3-09-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 2-3 sentences when you would choose CSS Grid over Flexbox, and vice versa.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [grid, flex, two-dimensional, one-dimensional, row, column, layout]
      rejectedFeedback: "Grid: two-dimensional layouts where you control both rows AND columns (page layout, card grids, complex forms). Flexbox: one-dimensional layouts where content flows along one axis (nav bars, button groups, lists). Many components use both."
    hint: "Think about whether you need to control rows AND columns simultaneously, or just one direction."
    reflectionPrompt: "The modern answer: use Grid for the page-level layout and major sections. Use Flexbox for component-level layout (the items within a card, a nav bar, a button group). They complement each other — picking one and avoiding the other misses half the toolkit."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "An element needs to span 2 columns in a grid. Which CSS achieves this?"
    options:
      - "width: 2fr"
      - "grid-column: span 2"
      - "column-span: 2"
      - "flex: 2"
    correctIndex: 1
    feedback: "grid-column: span 2 makes the item occupy 2 grid column tracks. You can also use grid-column: 1 / 3 (from line 1 to line 3). grid-row: span 2 does the same for rows. These explicit placements are where Grid truly shines over Flexbox."
  - type: MULTIPLE_CHOICE
    question: "What is the grid-template-areas property used for?"
    options:
      - "Setting the number of template columns"
      - "Naming and visually laying out grid areas using ASCII-art-like syntax"
      - "Creating area charts inside a grid"
      - "Defining the gap between grid areas"
    correctIndex: 1
    feedback: "grid-template-areas lets you name regions and lay them out visually:\n\"header header\"\n\"sidebar main\"\n\"footer footer\"\nChildren then use grid-area: header (or sidebar, main, footer) to place themselves. This makes complex layouts self-documenting."

retrieval:
  recall: "Write a CSS rule that creates a 3-column grid with equal columns and 24px gaps."
  explain: "Explain what `repeat(auto-fill, minmax(200px, 1fr))` does and why it creates a responsive layout without media queries."
  mistakeId:
    code: "Using display: grid on the item instead of the container"
    answer: "display: grid must be set on the container (the parent). The grid properties (grid-template-columns, gap, justify-items) go on the container. The children become grid items automatically. Placing display: grid on an item only affects that item's children, not the item itself."
---

# Hook

Flexbox laid things out in a line. CSS Grid lays things out in two dimensions simultaneously — rows and columns, controlled together.

Grid is the layout system for pages, dashboards, card grids, and any design where you need precise control over both directions at once.

Together, Flexbox and Grid give you the complete layout toolkit for the modern web.

# Lore Introduction

*"The Academy's great library,"* says Master Aelindra, gesturing at a vast grid of shelves, *"organises every scroll by row and column simultaneously: section, tier, and position. A single look at the coordinates tells you exactly where any scroll is. CSS Grid gives your layout the same precision."*

# Core Learning

## Concept Introduction

Set `display: grid` on a container. Define the structure with `grid-template-columns` and `grid-template-rows`.

**Defining columns:**

```css
/* Three equal columns */
grid-template-columns: 1fr 1fr 1fr;

/* Shorthand with repeat() */
grid-template-columns: repeat(3, 1fr);

/* Fixed + flexible */
grid-template-columns: 200px 1fr;

/* Auto-responsive — no media query needed */
grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
```

**Placing items:**

```css
/* Span 2 columns */
.featured { grid-column: span 2; }

/* Explicit placement: column 1 to 3, row 1 to 2 */
.hero { grid-column: 1 / 3; grid-row: 1 / 2; }
```

**Named areas:**

```css
.layout {
  display: grid;
  grid-template-columns: 250px 1fr;
  grid-template-areas:
    "header header"
    "sidebar main"
    "footer footer";
}
.header { grid-area: header; }
.sidebar { grid-area: sidebar; }
.main { grid-area: main; }
```

## Why It Matters

Grid gives you layout control that was previously only achievable with complex float or table hacks. The `repeat(auto-fill, minmax(...))` pattern alone replaces most responsive grid frameworks.

## Common Mistakes

- Setting `display: grid` on the item instead of the container
- Forgetting that `fr` units only work after fixed sizes are accounted for
- Using Grid when Flexbox is sufficient (adds complexity for one-dimensional layouts)

## Mini Summary

- ✔ `display: grid` on the container; rows and columns defined on the container
- ✔ `fr` = fraction of available space; `repeat()` = shorthand for repeated tracks
- ✔ `grid-column: span 2` spans an item across multiple columns
- ✔ `repeat(auto-fill, minmax(250px, 1fr))` = fully responsive grid, no media queries
- ✔ Grid = 2D; Flexbox = 1D — use both, each where it excels

# Guided Practice Quest

**The Grid Cartographer** — practice defining and using CSS Grid. Steps in `guidedSteps`.

# Solo Practice Quest

Build a page layout with: a full-width header, a sidebar (250px) and main content area side by side, and a full-width footer. Use CSS Grid with named areas. Write all the container and item CSS.

# Integration

**Connecting to Mathematics — Matrix Notation and Coordinate Systems**

CSS Grid is a matrix layout system. A 3×4 grid is a 3-column, 4-row matrix. Grid lines are numbered from 1, creating coordinates: grid-column: 2 / 4 is "from column line 2 to column line 4." Item placement using grid-column and grid-row is coordinate-based positioning in a discrete 2D space. grid-template-areas converts this coordinate system into a human-readable matrix — the ASCII-art layout directly maps to the mathematical matrix of cells. Understanding this connection makes complex grid layouts intuitive rather than magical.

# Lore Conclusion

*"The grid,"* says Master Aelindra, *"is the underlying structure of almost every well-designed page. Even pages that don't look like grids follow one — invisible but consistent. Learn to see the grid in every design. Then build the grid in your CSS."*

---
