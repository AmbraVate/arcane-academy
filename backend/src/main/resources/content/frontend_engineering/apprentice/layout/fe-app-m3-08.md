---
id: fe-app-m3-08
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
lesson: flexbox
title: "Flexbox"
sortOrder: 3
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
    - "Correctly applies display:flex to create a flex container"
    - "Uses flex-direction to control axis orientation"
    - "Uses justify-content and align-items to control alignment on both axes"
    - "Explains the difference between justify-content and align-items"
    - "Uses flex-wrap to handle overflow and gap for spacing"
  keywords: [flexbox, flex, display, flex-direction, justify-content, align-items, flex-wrap, gap, main-axis, cross-axis]
  modelAnswer: |
    Flexbox is a one-dimensional layout system. Setting display: flex on a container
    makes its children flex items arranged along the main axis (row by default).
    justify-content controls spacing along the main axis; align-items controls
    alignment on the cross axis. flex-wrap allows items to wrap to a new line.
    gap adds consistent spacing between items without margin hacks.
guidedSteps:
  - id: fe-app-m3-08-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You want to horizontally centre a button inside a div and vertically centre it too. The div is the flex container. Which CSS combination achieves this?
    inputConfig:
      options:
        - "justify-content: center; align-items: center"
        - "text-align: center; vertical-align: middle"
        - "margin: auto; padding: auto"
        - "flex-direction: center; flex-align: center"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["justify-content: center; align-items: center"]
      rejectedFeedback: "justify-content: center centres along the main axis (horizontal in row direction). align-items: center centres on the cross axis (vertical in row direction). Together they achieve perfect centring — one of the most frequent uses of Flexbox."
    hint: "Flexbox has separate properties for the main axis and the cross axis."
    reflectionPrompt: "Centring things was famously difficult before Flexbox. Now it is two lines of CSS. This is one reason Flexbox adoption was so rapid — it solved a common, frustrating problem elegantly."

  - id: fe-app-m3-08-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To make flex items wrap to a new row when the container is too narrow, you add:

      `flex-___: wrap;`
    inputConfig:
      placeholder: "wrap"
    markingRule:
      matchMode: CONTAINS
      accepted: [wrap]
      rejectedFeedback: "flex-wrap: wrap allows items to wrap to a new line when they don't fit. The default is nowrap — items shrink instead. Combined with a min-width on flex items, flex-wrap creates responsive card grids without media queries."
    hint: "By default, flex items never wrap — they shrink instead."
    reflectionPrompt: "flex-wrap is how you turn a horizontal nav bar into a stacking list on small screens — add flex-wrap: wrap and set a min-width on the items. This is a responsive pattern that doesn't need a media query."

  - id: fe-app-m3-08-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the difference between justify-content: space-between and justify-content: space-around using a concrete example with 3 items.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [space-between, space-around, edge, equal, between, gap]
      rejectedFeedback: "space-between: items spread to edges, equal gaps between items (gaps only between, not at edges). space-around: equal space on each side of every item (items at edges have half the space of interior gaps). space-evenly: truly equal space everywhere including edges."
    hint: "Draw three boxes in a row and think about where the spaces go in each case."
    reflectionPrompt: "These three values (space-between, space-around, space-evenly) are commonly confused in interviews and in practice. The mental model: between = no edge space. around = half space at edges. evenly = equal space everywhere."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `flex: 1` mean on a flex item?"
    options:
      - "The item has 1px width"
      - "The item takes 1 unit of available space — items with flex: 1 share space equally"
      - "The item is 1% of its container"
      - "The item has order 1 in the flex flow"
    correctIndex: 1
    feedback: "flex: 1 is shorthand for flex-grow: 1, flex-shrink: 1, flex-basis: 0. An item with flex: 1 takes its share of remaining space. Three items all with flex: 1 each take one-third of the container. An item with flex: 2 takes twice the share of flex: 1 siblings."
  - type: MULTIPLE_CHOICE
    question: "Which property changes the direction flex items are laid out?"
    options:
      - "flex-orientation"
      - "flex-direction"
      - "flex-axis"
      - "flex-flow"
    correctIndex: 1
    feedback: "flex-direction: row (default, left to right), column (top to bottom), row-reverse, column-reverse. Changing flex-direction also changes which axis justify-content and align-items act on — they always refer to main axis and cross axis respectively."

retrieval:
  recall: "Write the CSS to create a horizontal navigation bar with items evenly spaced and vertically centred."
  explain: "Explain the difference between justify-content and align-items in a flex container with flex-direction: row."
  mistakeId:
    code: "Using margin: auto between flex items instead of gap"
    answer: "While margin: auto on flex items does distribute space, gap is cleaner: gap: 16px adds consistent spacing between all flex items without affecting outer edges. It is the modern, readable approach for flex item spacing."
---

# Hook

Before Flexbox, creating a horizontal navigation bar with evenly spaced items was surprisingly painful — floats, table display hacks, inline-block with whitespace tricks.

Flexbox solved this. It is a one-dimensional layout system that makes distributing, aligning, and spacing elements almost trivial.

Flexbox is one of the two layout tools you will use on virtually every project.

# Lore Introduction

*"The Academy's great hall,"* says Master Aelindra, *"arranges its benches differently for every occasion — side by side for lectures, in a circle for debates, single file for ceremonies. Flexbox is the enchantment that rearranges the benches instantly."*

# Core Learning

## Concept Introduction

Set `display: flex` on a **container** to make its direct children **flex items**.

**Axis model:**
- **Main axis:** the direction items are laid (default: horizontal)
- **Cross axis:** perpendicular to the main axis

**Container properties:**

| Property | Values | Controls |
|---|---|---|
| `flex-direction` | `row` / `column` / `row-reverse` | Which way items flow |
| `justify-content` | `flex-start`, `center`, `flex-end`, `space-between`, `space-around`, `space-evenly` | Spacing along main axis |
| `align-items` | `flex-start`, `center`, `flex-end`, `stretch` | Alignment on cross axis |
| `flex-wrap` | `nowrap` / `wrap` | Whether items can wrap |
| `gap` | `16px` / `8px 16px` | Space between items |

**Item properties:**

| Property | Use |
|---|---|
| `flex: 1` | Grow to fill available space (equal share) |
| `flex: 0 0 200px` | Fixed width, no grow/shrink |
| `order` | Change visual order without reordering HTML |
| `align-self` | Override align-items for one item |

## Worked Examples

```css
/* Horizontal nav bar — items centred vertically, spaced evenly */
.nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
}

/* Perfect centring (vertical + horizontal) */
.hero {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

/* Responsive card row */
.cards {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
}
.card { flex: 1; min-width: 280px; }
```

## Common Mistakes

- Applying flex properties to the items instead of the container (`justify-content` goes on the container)
- Forgetting `flex-wrap: wrap` — items overflow on small screens
- Not understanding that `justify-content` direction changes with `flex-direction`

## Mini Summary

- ✔ `display: flex` on the container, properties affect direct children
- ✔ `justify-content` = main axis; `align-items` = cross axis
- ✔ `flex: 1` — item takes its share of available space
- ✔ `gap` for consistent spacing between items
- ✔ `flex-wrap: wrap` + `min-width` = responsive layout without media queries

# Guided Practice Quest

**The Arrangement Spell** — practice core Flexbox alignment. Steps in `guidedSteps`.

# Solo Practice Quest

Build a responsive card row using Flexbox: 3 cards that sit side by side on desktop and stack vertically on narrow screens, with 24px gap between them. Write the container and item CSS. Explain your flex properties.

# Integration

**Connecting to Mathematics — Linear Algebra and Axis Systems**

Flexbox operates on a two-axis coordinate system — identical to 2D Cartesian space. The main axis corresponds to one dimension; the cross axis to the perpendicular dimension. justify-content distributes space along the x-axis (when horizontal); align-items positions along the y-axis. When flex-direction changes, the axes swap — the mathematical relationship remains the same, only the orientation changes. Understanding CSS layout as coordinate geometry makes the behaviour predictable: you are distributing positions in a bounded 2D space.

# Lore Conclusion

*"Flexbox,"* says Master Aelindra, *"is the first layout tool that thinks the same way a designer does: place items on a line, space them, align them, wrap them. Before Flexbox, developers were fighting the browser. Now they are working with it."*

---
