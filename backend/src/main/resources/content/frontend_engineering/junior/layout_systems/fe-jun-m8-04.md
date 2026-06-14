---
id: fe-jun-m8-04
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
lesson: flexbox_with_tailwind
title: "Flexbox with Tailwind"
sortOrder: 1
difficulty: 4
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m8-03]
integrationDomains: [design, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly uses flex, flex-row or flex-col to set direction"
    - "Correctly applies justify-* for main axis alignment and items-* for cross axis"
    - "Uses gap-* to add spacing between flex children"
    - "Identifies a real-world layout pattern that suits flexbox (navbar, card row, form row)"
  keywords: [flex, justify, items, gap, row, col, wrap, align, space-between]
  modelAnswer: |
    Flexbox in Tailwind starts with `flex` on the container. `flex-row` (default) aligns children horizontally; `flex-col` stacks them vertically. `justify-between` spaces children to opposite ends on the main axis; `justify-center` centres them. `items-center` centres children on the cross axis. `gap-4` adds 16px between children — cleaner than margins. `flex-wrap` lets children wrap to new lines. Common uses: navbars (`flex justify-between items-center`), card rows (`flex gap-4 flex-wrap`), form rows (`flex items-center gap-2`).
guidedSteps:
  - id: fe-jun-m8-04-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You want a navbar with the logo on the left and nav links on the right, all vertically centred. Which Tailwind classes on the container achieve this?"
    inputConfig:
      options:
        - "flex justify-between items-center"
        - "flex justify-center items-start"
        - "grid grid-cols-2 items-center"
        - "flex flex-col justify-between"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["flex justify-between items-center"]
      rejectedFeedback: "`flex` enables flexbox, `justify-between` pushes children to opposite ends, `items-center` centres them vertically. This is the classic navbar pattern."
    hint: "You need items on opposite ends (justify) and centred on the cross axis (items)."
    reflectionPrompt: "What would `justify-center` give you instead of `justify-between`?"
  - id: fe-jun-m8-04-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write the Tailwind classes for a row of cards that: displays children horizontally, wraps to the next line when space runs out, and has 24px gaps between cards."
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: [flex-wrap, gap-6, flex]
      rejectedFeedback: "You need `flex` to start, `flex-wrap` so items wrap, and `gap-6` for 24px gaps (6 × 4px = 24px)."
    hint: "Three classes: one to enable flex, one to enable wrapping, one for gaps."
    reflectionPrompt: "Why is `gap-6` cleaner than adding `mr-6` to every card?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the difference between `justify-center` and `items-center` in a flex row?"
    options:
      - "justify-center centres on the horizontal (main) axis; items-center centres on the vertical (cross) axis"
      - "They do the same thing — both centre content"
      - "items-center centres on the main axis; justify-center centres on the cross axis"
      - "justify-center applies to the parent; items-center applies to individual children"
    correctIndex: 0
    feedback: "In a flex-row: justify works on the horizontal main axis, items works on the vertical cross axis. In a flex-col, the axes flip."
retrieval:
  recall: "What Tailwind classes do you need to build a simple horizontal navbar with logo left, links right, and content vertically centred?"
  explain: "Explain the difference between gap-* and margin utilities when spacing flex children."
  mistakeId:
    code: |
      // Developer wants cards side by side with space between them
      <div className="flex">
        <div className="mr-4 bg-white p-4">Card 1</div>
        <div className="mr-4 bg-white p-4">Card 2</div>
        <div className="bg-white p-4">Card 3</div>
      </div>
    answer: "Using `mr-4` on each child is brittle — the last child needs a different class (no margin). Use `gap-4` on the parent flex container instead: `<div className='flex gap-4'>`. This applies equal spacing between all children automatically."
---

# Hook

The Academy's Hall of Champions displays portraits of every guild member in a horizontal row — logo on the left, member names in the middle, a settings icon on the right. Without flexbox, this layout would require floats and clearfixes that haunt nightmares. With Tailwind's flex utilities, it takes four classes and thirty seconds.

# Lore Introduction

Master Linearis, the Academy's Layout Artificer, teaches: "Before the Great Flex Reformation, apprentices wrestled with floats and positioning hacks. Now we have the Flex Weave — a system where children arrange themselves along an axis with a few simple commands." The students take their seats in a perfect horizontal row, appropriately demonstrating the point.

# Core Learning

## Concept Introduction

Tailwind maps directly to CSS Flexbox. The container gets `flex`; then alignment utilities control how children are arranged.

**Container utilities:**
```
flex           → display: flex
flex-row       → flex-direction: row (default)
flex-col       → flex-direction: column
flex-wrap      → flex-wrap: wrap
flex-nowrap    → flex-wrap: nowrap (default)
```

**Main axis (justify):**
```
justify-start    → justify-content: flex-start
justify-end      → justify-content: flex-end
justify-center   → justify-content: center
justify-between  → justify-content: space-between
justify-around   → justify-content: space-around
justify-evenly   → justify-content: space-evenly
```

**Cross axis (items):**
```
items-start      → align-items: flex-start
items-center     → align-items: center
items-end        → align-items: flex-end
items-stretch    → align-items: stretch (default)
items-baseline   → align-items: baseline
```

**Gap:**
```
gap-2    → gap: 8px (between all children)
gap-x-4  → column-gap: 16px
gap-y-4  → row-gap: 16px
```

## Why It Matters

`gap-*` replaces fragile margin hacks. `justify-between` replaces absolute positioning for split layouts. The semantic mapping (justify = main axis, items = cross axis) makes it easy to reason about layouts verbally before writing code.

## Worked Example

```jsx
// Navbar pattern
function Navbar() {
  return (
    <nav className="flex justify-between items-center px-6 py-4 bg-white border-b border-gray-200">
      <span className="text-xl font-bold text-indigo-600">Arcane Academy</span>
      <div className="flex items-center gap-6">
        <a href="/quests" className="text-gray-600 hover:text-gray-900">Quests</a>
        <a href="/profile" className="text-gray-600 hover:text-gray-900">Profile</a>
        <button className="bg-indigo-600 text-white px-4 py-2 rounded-lg text-sm font-medium">
          Join
        </button>
      </div>
    </nav>
  );
}

// Card row with wrapping
function CardGrid({ items }) {
  return (
    <div className="flex flex-wrap gap-4">
      {items.map(item => (
        <div key={item.id} className="flex-1 min-w-64 bg-white rounded-lg p-4 shadow">
          {item.title}
        </div>
      ))}
    </div>
  );
}
```

## Common Mistakes

- **Forgetting `flex` on the parent** — `justify-between` and `items-center` do nothing without `flex` (or `grid`) on the container.
- **Using margin instead of gap** — `mr-4` on every child fails on the last child and is harder to maintain.
- **Using `flex-col` when `flex` (row) is wanted** — remember, `flex-row` is the default; you only need `flex-col` when stacking vertically.

## Mini Summary

Tailwind's flex utilities follow a simple pattern: `flex` on the container, `justify-*` for the main axis, `items-*` for the cross axis, `gap-*` for spacing. These four categories cover the vast majority of flex layouts.

# Guided Practice Quest

Work through the guided steps to practise the navbar and card-row patterns.

# Solo Practice Quest

Build a profile header component: avatar on the left, name and role stacked vertically in the centre (flex-grow), and an "Edit" button on the right. All vertically centred.

# Integration

**Design:** Flexbox directly implements the design concept of "alignment axes." Designers think in terms of horizontal/vertical alignment and spacing — flex language maps almost one-to-one.

**Mathematics:** The space-between distribution is equivalent to dividing available space evenly into gaps. Understanding this mathematically helps predict layout behaviour when container size changes.

# Lore Conclusion

The apprentices style the Hall of Champions in minutes. Logo left, names centre, settings right — `flex justify-between items-center` and it is done. Master Linearis nods. "When you understand the axes, the layout arranges itself." The portraits hang in perfect formation.

---
