---
id: fe-app-m7-03
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m7
moduleTitle: "Module 7: Frontend Engineering Habits"
moduleGlyph: "🛠️"
moduleSortOrder: 7
topicSlug: debugging
topicTitle: "Debugging"
topicSortOrder: 1
lesson: debugging_layout_issues
title: "Debugging Layout Issues"
sortOrder: 3
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
    - "Uses DevTools box model inspector to diagnose spacing issues"
    - "Applies outline: 1px solid red to visualise element boundaries"
    - "Explains the most common causes of layout overflow"
    - "Understands how to use the Flexbox/Grid inspector in DevTools"
    - "Applies systematic narrowing to isolate layout bugs"
  keywords: [layout, overflow, box-model, outline, inspect, flex, grid, DevTools, margin, positioning, systematic]
  modelAnswer: |
    Layout debugging starts with the DevTools Elements panel: select the element and
    use the box model diagram to see exact margin/padding/border values. Adding
    outline: 1px solid red to elements reveals their boundaries without affecting layout.
    Overflow is commonly caused by: elements wider than their container, fixed widths
    on fluid containers, and missing box-sizing: border-box. The Flexbox inspector
    shows flex item sizes and how space is distributed.
guidedSteps:
  - id: fe-app-m7-03-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A page has unexpected horizontal scrolling. Which is the fastest first step to identify the cause?
    inputConfig:
      options:
        - "Delete elements one by one until the scrolling stops"
        - "Add * { outline: 1px solid red; } temporarily to see all element boundaries"
        - "Rebuild the page from scratch"
        - "Change all widths to 100% and see if it fixes it"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Add * { outline: 1px solid red; } temporarily to see all element boundaries"]
      rejectedFeedback: "The outline technique reveals every element's actual boundary without affecting layout (outline doesn't participate in box model calculations). The element causing horizontal scroll will visually extend past the viewport. This narrows the search from 'somewhere on the page' to 'that specific element.'"
    hint: "A fast visual technique that reveals element boundaries without changing layout."
    reflectionPrompt: "* { outline: 1px solid red; } is one of the most useful debugging one-liners in CSS. It reveals the exact boundaries of every element on the page. You will immediately see which element is overflowing. Remove it once you've found the problem. It's harmless and extremely fast."

  - id: fe-app-m7-03-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In DevTools, you can visually inspect the margin, border, padding, and content dimensions of any element using the ___ diagram in the Computed tab or the bottom of the Styles pane.
    inputConfig:
      placeholder: "box model"
    markingRule:
      matchMode: CONTAINS
      accepted: [box model, box-model, boxmodel]
      rejectedFeedback: "The box model diagram in DevTools shows exact dimensions for content, padding, border, and margin of the selected element. Hover over any region to highlight it on the page. This is far more reliable than guessing pixel values — you see exactly what the browser computed."
    hint: "This diagram from CSS lesson 3.1 is shown visually in DevTools."
    reflectionPrompt: "The box model diagram reveals computed values — not what you wrote in CSS, but what the browser actually applied after cascade, inheritance, and layout calculations. When a spacing value looks wrong, this diagram shows you the exact numbers the browser used."

  - id: fe-app-m7-03-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe a systematic approach to debugging a layout problem where two elements overlap unexpectedly.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [inspect, position, z-index, overlap, absolute, margin, negative, select, DevTools]
      rejectedFeedback: "Systematic approach: (1) Select both overlapping elements in DevTools. (2) Check position values — is one absolute/relative? (3) Check z-index — is one on top of the other? (4) Check margins — are any negative? (5) Check overflow on parent. Overlap is almost always caused by absolute/fixed positioning, negative margins, or transform."
    hint: "Think about the CSS properties most likely to cause overlapping elements."
    reflectionPrompt: "Overlap is almost always caused by absolute/fixed positioning (element removed from flow, overlapping siblings), negative margin, or transform. Inspect both elements: look at their position value and any negative spacing. The DevTools overlay shows if elements are stacked."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "An element is wider than you expect. You set width: 300px but it renders at 360px. The most likely cause is:"
    options:
      - "A browser bug — widths are not reliable"
      - "box-sizing: content-box — the 60px difference is padding (30px each side)"
      - "The parent element is forcing a larger width"
      - "width is a suggestion, not a hard constraint"
    correctIndex: 1
    feedback: "With box-sizing: content-box (default), width: 300px + padding: 30px left/right = 360px total. The box model diagram in DevTools will show this exactly. Solution: add box-sizing: border-box to the element (or globally) so the padding is included in the 300px."
  - type: MULTIPLE_CHOICE
    question: "The DevTools Flexbox inspector shows which flex item sizes by:"
    options:
      - "Highlighting flex items with coloured overlays and showing their computed sizes"
      - "Only showing the container's properties"
      - "Displaying a text list of flex values"
      - "Flexbox inspector only works on Grid layouts"
    correctIndex: 0
    feedback: "Chrome and Firefox DevTools have dedicated Flexbox and Grid inspectors. Click the 'flex' badge on a flex container in the Elements panel to toggle the Flexbox inspector — it shows each item's size, available space, and how it was distributed. Grid inspector shows named areas and line numbers."

retrieval:
  recall: "What is the CSS technique to visualise all element boundaries on a page for debugging?"
  explain: "Explain why using outline instead of border for debugging layout is better."
  mistakeId:
    code: "width: 100% on an element that also has padding: 20px (with default content-box) — overflows its container"
    answer: "Add box-sizing: border-box to the element (or *, *::before, *::after globally). With border-box, width: 100% includes the padding — total is still 100% of the container. Without it, 100% + 40px padding > 100% = overflow."
---

# Hook

Layout bugs are among the most frustrating in frontend development. Elements that overlap, content that overflows, spacing that refuses to behave.

The difference between struggling for hours and finding the bug in minutes is approach: systematic observation over random guessing.

# Lore Introduction

*"A watchmaker who shakes a broken watch hoping the parts rearrange correctly,"* says Master Aelindra dryly, *"is not a watchmaker. They are a guesser. A watchmaker opens the case, observes which gear has stopped, and addresses that gear precisely. Layout debugging requires the same discipline."*

# Core Learning

## Concept Introduction

**Layout debugging toolkit:**

**1. The outline technique:**
```css
/* See every element's boundary instantly */
* { outline: 1px solid red; }

/* Or target a specific element */
.suspect { outline: 2px dashed blue; }
```
*Note: `outline` doesn't affect layout (unlike `border`). Safe for debugging.*

**2. DevTools box model:**
- Select element in Elements panel
- Scroll to bottom of Styles pane → box model diagram
- Hover each region to highlight it on page
- See exact computed values: content/padding/border/margin

**3. DevTools Flexbox/Grid inspector:**
- Elements panel → look for `flex` or `grid` badge next to display value
- Click badge → inspector overlays on page
- Shows: item sizes, gutters, alignment, named areas

**4. Systematic narrowing:**
```
Problem: element X has wrong spacing
→ Inspect X in DevTools → check computed margin and padding
→ Not X? → check parent element
→ Still not found? → use * { outline } to see all boundaries
→ Found the overflowing element → inspect its width/box-sizing
```

**Common layout bug causes:**

| Symptom | Likely cause |
|---|---|
| Horizontal scroll | Element wider than viewport; missing box-sizing |
| Elements overlap | position:absolute without intended positioning context |
| Unexpected gap | Default margin on elements (h1, p, body have default margins) |
| Flexbox not aligning | Property on item instead of container |
| Grid not applying | Element already has display set another way |

## Why It Matters

Layout bugs are the most visible class of frontend defects — users may forgive a slow feature, but a button overlapping text looks broken instantly:

- Layout problems rarely produce errors; the page renders, just wrongly — so they require *inspection* skills, not log-reading
- DevTools' box model view turns "why is there a gap there?" from a guessing game into a measurement
- One systematic method (inspect the element, check its size, margins, and parent) replaces an hour of randomly toggling CSS

CSS debugging defeats more beginners than any JavaScript topic. A repeatable diagnosis routine is what carries you past that wall.

## Common Mistakes

- Changing random CSS values hoping something fixes it (guessing, not debugging)
- Forgetting default browser margins (body, h1-h6, p all have default margins)
- Using `border` for debugging (affects layout); use `outline` instead

## Mental Model

Debugging layout is plumbing, not magic. Every element's position is *caused* — by its own box (content, padding, border, margin), by its parent's layout rules, or by a sibling pushing it. When water comes out in the wrong place, a plumber traces the pipes; when an element lands in the wrong place, you trace the chain: inspect the element, then its box, then its container, then what's beside it. Somewhere in that chain is a valve set wrong — a width, a margin, a flex rule. The leak is never random; find the pipe.

## Mini Summary

- ✔ `* { outline: 1px solid red; }` — fastest way to see all element boundaries
- ✔ Box model diagram in DevTools shows exact computed dimensions
- ✔ Systematic approach: observe → hypothesise → test → confirm
- ✔ Horizontal overflow almost always: element wider than container (box-sizing issue)
- ✔ Overlap almost always: unexpected absolute/fixed positioning or negative margin

# Guided Practice Quest

**The Watchmaker's Eye** — three questions on layout debugging technique. Steps in `guidedSteps`.

# Solo Practice Quest

Break a simple layout deliberately (make an element overflow, create an unexpected gap, cause overlap), then debug it using the techniques from this lesson: the outline trick, the box model diagram, and systematic narrowing. Document: what the bug was, what you observed, and what CSS fixed it.

# Integration

**Connecting to Psychology — The Scientific Method and Hypothesis Testing**

Debugging is applied hypothesis testing. The scientific method: observe a phenomenon (layout bug), form a hypothesis (box-sizing issue), design a test (inspect box model diagram), analyse results (confirmed: padding + content > width), conclude (add border-box). The common debugging failure mode — random changes without hypotheses — is equivalent to running experiments without knowing what you're testing. Each CSS change should test a specific hypothesis. If it doesn't fix the bug, the hypothesis was wrong and new information was gained.

# Lore Conclusion

*"The layout bug,"* says Master Aelindra, *"has a cause. The cause has evidence. The evidence is visible in DevTools. Observe before you touch. Hypothesise before you change. Test before you declare victory. The watchmaker who opens the case, sees the broken gear, and replaces it — that is the developer who fixes bugs in minutes."*

---
