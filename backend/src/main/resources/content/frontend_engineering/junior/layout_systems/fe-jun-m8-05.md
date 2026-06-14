---
id: fe-jun-m8-05
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m8
moduleTitle: "Module 8: Tailwind CSS"
moduleGlyph: "🎨"
moduleSortOrder: 8
topicSlug: layout_systems
topicTitle: "Layout Systems"
topicSortOrder: 2
lesson: grid_with_tailwind
title: "Grid with Tailwind"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m8-04]
integrationDomains: [design, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly defines a CSS grid using grid and grid-cols-*"
    - "Explains how col-span-* allows items to span multiple columns"
    - "Describes a layout scenario better suited to grid than flex"
    - "Identifies at least one practical grid pattern (dashboard, gallery, form)"
  keywords: [grid, grid-cols, col-span, row, column, layout, span, two-dimensional]
  modelAnswer: |
    CSS Grid in Tailwind starts with `grid` on the container, then `grid-cols-3` (for example) creates three equal columns. `gap-4` adds gutters. Individual items can use `col-span-2` to span two columns. Grid is best for two-dimensional layouts — where you're controlling both rows and columns simultaneously, like dashboards, image galleries, or form layouts with labels and inputs in aligned columns. Flex is better for one-dimensional flows (a row of buttons, a column of items).
guidedSteps:
  - id: fe-jun-m8-05-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You want a dashboard with a sidebar taking 1/4 of the width and a main content area taking 3/4. Which Tailwind approach is most appropriate?"
    inputConfig:
      options:
        - "grid grid-cols-4 with sidebar using col-span-1 and main using col-span-3"
        - "flex with sidebar using w-1/4 and main using flex-1"
        - "Both approaches work — grid is slightly more semantically appropriate for this two-column layout"
        - "Only absolute positioning can achieve this"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Both approaches work — grid is slightly more semantically appropriate for this two-column layout"]
      rejectedFeedback: "Both grid and flex can achieve this. Grid with col-span is more semantically explicit about a two-column page structure; flex with flex-1 is also valid and commonly used."
    hint: "There is often more than one valid approach. Grid and flex both have strengths here."
    reflectionPrompt: "When would you use grid-cols over flex? Think about alignment of items across rows."
  - id: fe-jun-m8-05-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write the Tailwind classes for a 3-column card grid with 16px gaps, where the first card spans all 3 columns."
    inputConfig:
      minWords: 8
    markingRule:
      matchMode: CONTAINS
      accepted: [grid-cols-3, col-span-3, gap-4, grid]
      rejectedFeedback: "Container needs `grid grid-cols-3 gap-4`. The spanning card needs `col-span-3`."
    hint: "Two parts: the container classes, and the class on the spanning card."
    reflectionPrompt: "What happens visually when a card has col-span-3 in a 3-column grid?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `grid-cols-[repeat(auto-fill,minmax(200px,1fr))]` achieve that `grid-cols-3` cannot?"
    options:
      - "It automatically fills columns based on available space — responsive without breakpoint classes"
      - "It creates exactly 3 columns of 200px width each"
      - "It makes each row auto-height"
      - "It only works with exactly 200px items"
    correctIndex: 0
    feedback: "The arbitrary value syntax unlocks CSS Grid's intrinsic responsiveness. auto-fill with minmax creates as many columns as fit, down to 200px each — automatically responsive."
retrieval:
  recall: "What is the key difference between CSS Grid and Flexbox in terms of dimensionality?"
  explain: "When should you choose grid over flex for a layout?"
  mistakeId:
    code: |
      // Developer wants a 4-column card gallery, but writes:
      <div className="flex flex-wrap">
        {cards.map(card => (
          <div key={card.id} className="w-1/4 p-2">
            <Card {...card} />
          </div>
        ))}
      </div>
    answer: "Using `w-1/4` on flex children doesn't account for gaps — the total is over 100% once gaps are added. Use grid instead: `<div className='grid grid-cols-4 gap-4'>` and let each child fill naturally. This properly handles gutters and keeps items aligned in rows."
---

# Hook

The Academy's Artefact Gallery needs a display for its collection — six items per row, each with equal size, perfectly aligned in both directions. The Flex Weave can do rows, but when an artefact needs to span two columns? When items must align vertically across rows? This calls for the Grid Matrix — a two-dimensional layout system that Tailwind makes surprisingly approachable.

# Lore Introduction

Master Gridwick introduces the Grid Matrix to apprentices who have only known the Flex Weave. "Flex is a river," she says, "flowing in one direction. Grid is a map — rows and columns simultaneously, items placed with precision at any coordinate." The apprentices unfurl a two-dimensional scroll and begin.

# Core Learning

## Concept Introduction

Tailwind's Grid utilities mirror CSS Grid syntax.

**Container:**
```
grid              → display: grid
grid-cols-1       → grid-template-columns: repeat(1, minmax(0,1fr))
grid-cols-2       → two equal columns
grid-cols-3       → three equal columns
grid-cols-12      → twelve-column grid (common in design systems)
gap-4             → gutters between rows and columns
gap-x-4 / gap-y-4 → column-gap / row-gap separately
```

**Item spanning:**
```
col-span-1    → grid-column: span 1
col-span-2    → grid-column: span 2
col-span-full → span all columns
row-span-2    → grid-row: span 2
```

**Placement:**
```
col-start-2   → start in column 2
col-end-4     → end before column 4
```

## Why It Matters

Grid shines when you need:
- **Two-dimensional control** — aligning items in both rows and columns
- **Fixed column definitions** — every item snaps to the same column structure
- **Spanning items** — a featured item taking 2 columns in a 4-column grid

Flex shines when you need:
- **One-dimensional flow** — a row of buttons, a vertical stack
- **Variable-size children** — items that grow/shrink based on content

## Worked Example

```jsx
// Dashboard with sidebar and main content
function Dashboard({ children }) {
  return (
    <div className="grid grid-cols-12 gap-6 p-6">
      {/* Sidebar: 3/12 columns */}
      <aside className="col-span-3 bg-white rounded-xl p-4 shadow">
        <nav>Sidebar nav here</nav>
      </aside>
      {/* Main content: 9/12 columns */}
      <main className="col-span-9 space-y-6">
        {children}
      </main>
    </div>
  );
}

// Card gallery
function CardGallery({ items }) {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
      {items.map((item, i) => (
        <div
          key={item.id}
          className={`bg-white rounded-lg p-4 shadow ${i === 0 ? 'col-span-full sm:col-span-2' : ''}`}
        >
          {item.title}
        </div>
      ))}
    </div>
  );
}
```

## Common Mistakes

- **Using grid for a simple row of buttons** — that is what flex is for. Grid adds complexity you don't need.
- **Forgetting col-span must not exceed grid-cols count** — `col-span-3` in a `grid-cols-2` grid will overflow, not span.
- **Confusing gap with padding** — `gap` adds space between cells; `p-4` on a cell adds space inside it.

## Mini Summary

Tailwind Grid starts with `grid` and `grid-cols-*` on the container, uses `gap-*` for gutters, and lets individual items span with `col-span-*`. Use grid for two-dimensional layouts; use flex for one-dimensional flows.

# Guided Practice Quest

Work through the guided steps to practise dashboard and gallery grid patterns.

# Solo Practice Quest

Build a stats dashboard section with 4 equal-width stat cards in a row, where a summary card spans all 4 columns above them. Use `grid grid-cols-4 gap-4`.

# Integration

**Design:** Grid directly implements the design concept of a column system. Designers work in 12-column grids for print and screen — Tailwind's `grid-cols-12` maps perfectly to this design vocabulary.

**Mathematics:** The `1fr` unit (Tailwind's default column unit) distributes available space using fractional proportions. A `grid-cols-3` grid divides space into three equal thirds — simple fractions applied to responsive layout.

# Lore Conclusion

The Artefact Gallery is styled. Six items per row, each perfectly aligned across rows and columns. The featured artefact spans two columns. Master Gridwick surveys the layout. "The map metaphor holds true — Grid lets you place each item at precise coordinates. Once you know the grid, placement becomes deliberate rather than accidental."

---
