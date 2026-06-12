---
id: fe-app-m3-07
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
lesson: positioning
title: "Positioning"
sortOrder: 2
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
    - "Explains the difference between static, relative, absolute, fixed, and sticky positioning"
    - "Correctly uses top/right/bottom/left with non-static positioned elements"
    - "Explains what 'containing block' means for absolutely positioned elements"
    - "Gives a real-world use case for each positioning value"
    - "Explains what z-index controls and when to use it"
  keywords: [position, static, relative, absolute, fixed, sticky, top, left, z-index, containing block, flow]
  modelAnswer: |
    CSS position controls how an element is placed in the layout. Static (default)
    follows normal document flow. Relative offsets from the element's normal position.
    Absolute removes from flow and positions relative to the nearest positioned ancestor.
    Fixed positions relative to the viewport (stays on scroll). Sticky combines relative
    and fixed — scrolls normally until it hits a threshold. z-index controls stacking order.
guidedSteps:
  - id: fe-app-m3-07-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You want to create a badge that sits in the top-right corner of a card, regardless of the card's content. Which combination of CSS values achieves this?
    inputConfig:
      options:
        - "Card: position:static. Badge: position:absolute; top:0; right:0"
        - "Card: position:relative. Badge: position:absolute; top:0; right:0"
        - "Card: position:fixed. Badge: position:absolute; top:0; right:0"
        - "Card: position:relative. Badge: position:fixed; top:0; right:0"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Card: position:relative. Badge: position:absolute; top:0; right:0"]
      rejectedFeedback: "The absolute element positions relative to its nearest positioned ancestor. Setting position:relative on the card makes it the containing block — the badge then positions within the card's bounds. Without position:relative on the card, the badge positions relative to the document root."
    hint: "Absolute positioning needs a positioned ancestor to be 'contained' within."
    reflectionPrompt: "The pattern 'parent: relative, child: absolute' is one of the most used patterns in CSS. It is the foundation of tooltips, badges, overlays, dropdowns, and countless other UI components."

  - id: fe-app-m3-07-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A sticky navigation bar should stick to the top of the viewport when scrolled past. Complete: `position: ___; top: 0;`
    inputConfig:
      placeholder: "sticky"
    markingRule:
      matchMode: CONTAINS
      accepted: [sticky]
      rejectedFeedback: "position: sticky makes an element scroll normally until it reaches the specified offset (top: 0), then it 'sticks' and stays at that position while the rest of the page scrolls. Perfect for sticky navigation bars and table headers."
    hint: "This position value combines normal flow with viewport-pinning."
    reflectionPrompt: "Sticky positioning requires a specified offset (top, bottom, left, or right) to work — without it, the element behaves like relative. Also, the element only sticks within its containing block — once its parent scrolls out of view, the sticky element scrolls with it."

  - id: fe-app-m3-07-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the difference between position:fixed and position:sticky in 2-3 sentences.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [fixed, sticky, viewport, scroll, flow, relative]
      rejectedFeedback: "Fixed: always positioned relative to the viewport, removed from document flow, stays in place regardless of scroll or parent position. Sticky: stays in normal flow until it reaches the threshold, then pins — but only within its parent's boundaries."
    hint: "Consider what happens when the parent element scrolls off screen."
    reflectionPrompt: "Fixed elements are always visible at the same screen position — good for persistent UI (cookie banners, chat widgets). Sticky elements are contextual — they pin within a section but disappear when the section leaves the viewport. Choose based on the UX requirement."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which position value removes an element entirely from the normal document flow?"
    options:
      - "relative"
      - "sticky"
      - "absolute"
      - "All of the above"
    correctIndex: 2
    feedback: "absolute (and fixed) remove elements from normal flow — other elements behave as if they don't exist. relative and sticky keep the element in flow (it still occupies its original space). Knowing which values affect flow is critical for understanding layout gaps and overlaps."
  - type: MULTIPLE_CHOICE
    question: "What does z-index control?"
    options:
      - "The element's distance from the page edges"
      - "The stacking order of positioned elements — higher value appears in front"
      - "The zoom level of an element"
      - "The priority of the element in the CSS cascade"
    correctIndex: 1
    feedback: "z-index controls the stacking order along the z-axis (toward/away from the viewer). Higher z-index = appears in front. z-index only works on positioned elements (not static). Stacking contexts can make z-index behave unexpectedly — a common source of 'why is my modal behind everything?' bugs."

retrieval:
  recall: "List the five CSS position values and one use case for each."
  explain: "Why does position:absolute need a positioned ancestor to work as intended?"
  mistakeId:
    code: "position: absolute; top: 0; right: 0 — but the badge is in the top-right of the PAGE, not the card"
    answer: "The card needs position: relative to become the containing block. Without it, the absolutely positioned badge positions relative to the nearest positioned ancestor — which might be the document root, placing it in the corner of the page."
---

# Hook

Normal document flow is great for most content. But sometimes you need to break out of it — a sticky nav bar, a tooltip, a modal, a floating badge.

CSS positioning gives you precise control over where elements live in space. It is powerful, sometimes confusing, and essential for building real interfaces.

# Lore Introduction

*"Most books sit on the shelf in order,"* says Master Aelindra. *"But some — the reference texts — are pinned to the reading desk so they are always at hand, no matter how deep you go into the archive. Positioning is how you decide which elements follow the flow and which are pinned in place."*

# Core Learning

## Concept Introduction

| Value | Removed from flow? | Positioned relative to | Use case |
|---|---|---|---|
| `static` | No (default) | Normal flow | Default — no positioning |
| `relative` | No | Its own normal position | Offset slightly; create containing block |
| `absolute` | Yes | Nearest positioned ancestor | Badges, tooltips, dropdowns |
| `fixed` | Yes | The viewport | Persistent nav bars, cookie banners |
| `sticky` | No (until threshold) | Scroll position + parent | Sticky headers, table column headers |

```css
/* Parent creates the containing block */
.card { position: relative; }

/* Badge positions within the card */
.badge {
  position: absolute;
  top: 8px;
  right: 8px;
}

/* Always stays at the top of the screen */
.site-nav {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
}

/* Sticks on scroll */
.table-header {
  position: sticky;
  top: 0;
}
```

## Why It Matters

Positioning is how elements escape the normal top-to-bottom flow — and it's behind every overlay, sticky header, and floating button you've ever used:

- Modals, dropdowns, tooltips, and notification badges are all `absolute` or `fixed` positioning at work
- The `relative` parent + `absolute` child pattern is one of the most-used idioms in production CSS
- Misused positioning is also the source of legendary bugs: elements stuck under others, scrolling weirdness, z-index battles

You'll reach for flexbox and grid for structure, but the moment something must sit *on top of* something else, positioning is the only tool — know its rules cold.

## Common Mistakes

- **Forgetting `position: relative` on the parent** of an absolutely positioned child
- **Using `position: absolute` for everything** — it removes elements from flow, breaking surrounding layout
- **z-index without a positioned element** — z-index has no effect on `static` elements

## Mental Model

Normal document flow is a queue of people filing into rows of seats — each element takes the next spot, pushing others along. Positioning hands out special passes. `relative`: you keep your seat reserved but can lean over from it; everyone else stays put. `absolute`: you leave the queue entirely — your seat closes up — and stand wherever you like *within the nearest positioned ancestor's room*. `fixed`: you ignore the room and glue yourself to the window itself, unmoved by scrolling. `sticky`: you walk with the queue until you hit the doorframe, then hold there. Every positioning bug is someone misunderstanding which room a pass refers to.

## Mini Summary

- ✔ `static` = normal flow (default)
- ✔ `relative` = slight offset; makes element a containing block
- ✔ `absolute` = out of flow; positions within nearest positioned ancestor
- ✔ `fixed` = out of flow; always at same viewport position
- ✔ `sticky` = in flow until threshold; then pins

# Guided Practice Quest

**The Pinned Parchment** — three questions on CSS positioning. Steps in `guidedSteps`.

# Solo Practice Quest

Describe the CSS needed to create: (1) a card with a "NEW" badge in the top-right corner, (2) a navbar that stays at the top of the screen when scrolling, (3) a tooltip that appears above a button. Explain the position values you would use and why.

# Integration

**Connecting to Psychology — Spatial Memory and Fixed Landmarks**

Research in cognitive psychology shows that humans navigate both physical and digital spaces using landmarks — fixed reference points. A navigation bar that stays at the top of the screen (position: fixed) is a cognitive landmark: users always know where to look to navigate. Studies of web usability consistently show that persistent, visible navigation reduces cognitive load and task completion time. CSS fixed positioning is the technical implementation of a psychological principle: predictable spatial anchors reduce mental effort.

# Lore Conclusion

*"Most content flows like water — always finding the next available space. But some content must be anchored: the compass always in the same corner, the table of contents always visible. Positioning is how you decide what flows and what stands still."*

---
