---
id: fe-app-m1-07
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m1
moduleTitle: "Module 1: Understanding the Web"
moduleGlyph: "🌐"
moduleSortOrder: 1
topicSlug: browsers
topicTitle: "Browsers"
topicSortOrder: 2
lesson: rendering_a_page
title: "Rendering a Page"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m1-06]
integrationDomains: [sciences, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Describes the critical rendering path in order"
    - "Explains what the DOM and CSSOM are"
    - "Explains what causes render-blocking"
    - "Describes what layout and paint mean"
    - "Connects rendering knowledge to performance decisions"
  keywords: [dom, cssom, render tree, layout, paint, critical rendering path, reflow, repaint, render blocking]
  modelAnswer: |
    The critical rendering path is the sequence of steps the browser follows to convert HTML and CSS
    into pixels: parse HTML to build the DOM, parse CSS to build the CSSOM, combine them into the
    render tree, calculate layout (size and position), and paint pixels. Render-blocking resources
    (CSS, synchronous scripts) pause this process. JavaScript can cause reflows (layout recalculation)
    and repaints (visual updates), which are expensive. Understanding this helps frontend engineers
    write code that renders quickly and efficiently.
guidedSteps:
  - id: fe-app-m1-07-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does the browser build by combining the DOM and CSSOM?
    inputConfig:
      options:
        - "The JavaScript execution context"
        - "The Render Tree"
        - "The HTTP cache"
        - "The accessibility tree"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The Render Tree"]
      rejectedFeedback: "The browser combines the DOM (structure) and CSSOM (styles) into the **Render Tree** — a representation of only the visible elements and their computed styles. Elements with `display: none` are excluded from the render tree."
    hint: "It has 'tree' in the name and is made by combining two other trees."
    reflectionPrompt: "The render tree contains only what will be painted to screen. `display: none` removes elements from the render tree entirely — they don't affect layout. `visibility: hidden` keeps them in but makes them invisible. This distinction matters for performance."

  - id: fe-app-m1-07-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "When JavaScript changes the size or position of a DOM element, the browser must recalculate the layout — a process called ___."
    inputConfig:
      placeholder: "reflow"
    markingRule:
      matchMode: CONTAINS
      accepted: [reflow, "layout recalculation", relayout]
      rejectedFeedback: "**Reflow** (also called layout recalculation) is triggered when a DOM change affects geometry — element size, position, or the layout of surrounding elements. It is expensive because the browser must recalculate positions for potentially many elements."
    hint: "It starts with 're' and ends with 'flow'."
    reflectionPrompt: "Reflows are one of the most expensive operations in browser rendering. Frequently changing element sizes or positions in JavaScript (e.g., in a scroll listener) can cause 'layout thrashing' — dozens of reflows per second, making the page feel sluggish."

  - id: fe-app-m1-07-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A colleague suggests putting a large `<script src="analytics.js"></script>` tag at the top of the `<head>`. Why might this hurt page performance, and what would you suggest instead?
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [block, render, defer, async, bottom, body, performance, parse]
      rejectedFeedback: "Scripts in `<head>` block HTML parsing until they download and execute. Users see a blank page during this time. Solutions: move scripts to end of `<body>`, use `defer` (download in parallel, run after HTML parse), or `async` (download in parallel, run immediately on load)."
    hint: "What does a script in <head> prevent the browser from doing while it loads?"
    reflectionPrompt: "The `defer` attribute is almost always the right choice for scripts that manipulate the DOM. It downloads in parallel with HTML parsing and executes after parsing completes — no blank-page delay, and the DOM is available when the script runs."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "CSS stylesheets are render-blocking. What does this mean?"
    options:
      - "CSS prevents JavaScript from running"
      - "The browser will not render the page until all CSS is parsed"
      - "CSS files must be loaded before HTML"
      - "CSS blocks network requests for images"
    correctIndex: 1
    feedback: "CSS is render-blocking because the browser needs the CSSOM (built from CSS) to construct the render tree. Without it, it can't know how elements should look. This is why optimising CSS delivery (inlining critical CSS, deferring non-critical styles) improves perceived performance."
  - type: MULTIPLE_CHOICE
    question: "Which step in the critical rendering path calculates the exact size and position of every visible element on the page?"
    options:
      - "Parse"
      - "Render Tree construction"
      - "Layout (Reflow)"
      - "Paint"
    correctIndex: 2
    feedback: "Layout (also called Reflow) calculates where each element is on the page — its size, position, and relationship to other elements. This happens after the render tree is built. Paint then converts this geometric information into actual pixels."

retrieval:
  recall: "List the five steps of the critical rendering path in order."
  explain: "Why is it expensive to change the size or position of a DOM element in JavaScript on every scroll event?"
  mistakeId:
    code: "I can freely manipulate the DOM in JavaScript without worrying about performance"
    answer: "DOM manipulation can trigger reflows (layout recalculation) and repaints (pixel updates). These are expensive. Rapid DOM changes (e.g., in scroll or resize event listeners) can cause layout thrashing, making the page sluggish. Batch DOM reads and writes, use CSS transitions where possible, and consider `requestAnimationFrame` for animations."
---

# Hook

You have probably seen a webpage where text appears instantly but images take a moment to fill in. Or a page that goes blank for a second before everything appears. Or a page where scrolling feels jerky.

These are not random — they are symptoms of how the browser's rendering pipeline works. Understanding the pipeline tells you exactly why these things happen, and how to prevent them.

Performance is user experience. A slow render is a broken user experience. And as the frontend engineer, the render is your responsibility.

> Think of the slowest webpage you've ever used. What do you think might have caused it?

# Lore Introduction

Deep in the Academy's forge, materials arrive raw and are transformed through a precise sequence of operations. Skip a step, and the final product is broken. Do a step inefficiently, and the whole forge slows.

*"Rendering,"* says Master Aelindra, *"is a forge. HTML and CSS arrive raw. The browser works through a defined sequence. At the end: pixels. If any step is slow or blocked, the user waits."*

She sketches the pipeline on a blackboard.

*"Understanding this sequence is not academic. It directly determines whether your pages feel fast or sluggish. Whether users trust your work or abandon it."*

# Core Learning

## Concept Introduction

The **critical rendering path** is the sequence of steps the browser follows from receiving HTML to displaying pixels.

```
HTML  →  DOM
CSS   →  CSSOM
             ↓
        Render Tree
             ↓
          Layout
             ↓
           Paint
             ↓
         Composite
```

### Step by Step

**1. Parse HTML → Build the DOM**
The browser reads HTML character by character, constructing a tree of nodes. If it encounters a `<script>` tag without `defer` or `async`, it stops and executes the script before continuing.

**2. Parse CSS → Build the CSSOM**
CSS is parsed into the CSS Object Model — a tree of style rules. This is render-blocking: the browser won't render anything until CSS is fully parsed.

**3. Combine → Render Tree**
The DOM and CSSOM are combined. Only visible nodes are included (elements with `display: none` are excluded). Each node carries its computed styles.

**4. Layout (Reflow)**
The browser calculates the exact position and size of every element. Changing an element's geometry (width, height, position) triggers a reflow of that element and potentially its neighbours.

**5. Paint**
The browser fills in pixels — colours, borders, shadows, text. Changing only visual properties (colour, background) triggers a repaint without reflow.

**6. Composite**
Layers are assembled and drawn to screen. GPU-accelerated properties (transform, opacity) can be composited without triggering layout or paint — making them ideal for animations.

### Performance Implications

| Trigger | Cost | Cause |
|---|---|---|
| Change geometry (width, height, position) | **High** — triggers reflow + repaint |
| Change visual style only (colour, background) | **Medium** — triggers repaint |
| Change transform or opacity | **Low** — compositing only |

## Why It Matters

Knowing the rendering pipeline lets you make informed decisions about:
- Where to place `<script>` tags (avoid blocking HTML parsing)
- What CSS properties to animate (prefer `transform` over `top`/`left`)
- How to batch DOM updates (avoid layout thrashing)
- Why CSS is in `<head>` and scripts at the bottom of `<body>` (or using `defer`)

## Worked Examples

**Example 1 — Layout thrashing (bad):**
```javascript
// Reading then writing in a loop forces reflow on every iteration
elements.forEach(el => {
  const height = el.offsetHeight; // READ (forces layout)
  el.style.height = (height * 2) + 'px'; // WRITE (invalidates layout)
});
```

**Example 2 — Batched updates (good):**
```javascript
// Read all heights first, then write all changes
const heights = elements.map(el => el.offsetHeight);
elements.forEach((el, i) => {
  el.style.height = (heights[i] * 2) + 'px';
});
```

**Example 3 — GPU-friendly animation:**
```css
/* Bad: triggers layout on every frame */
.box { transition: left 0.3s, top 0.3s; }

/* Good: compositing only, no layout or paint */
.box { transition: transform 0.3s; }
```

## Common Mistakes

- **Animating `top`, `left`, `width`, `height`.** These trigger layout and paint on every frame. Use `transform` instead.
- **Interleaving DOM reads and writes.** Forces repeated reflows. Batch reads together, then writes together.
- **Putting scripts in `<head>` without `defer`.** Blocks HTML parsing, increasing time to first render.

## Mental Model

Think of rendering as **building a set for a theatre production**:
- The script arrives (HTML) — you plan what structures are needed (DOM)
- The costume guide arrives (CSS) — you plan how each piece should look (CSSOM)
- You combine these to plan the full stage layout (render tree)
- You measure and build every structure (layout)
- You paint and decorate everything (paint)
- You raise the curtain (composite/display)

Changing a costume colour mid-production (repaint) is quick. Moving the entire set around (reflow) takes much longer.

## Mini Summary

- The critical rendering path: HTML→DOM, CSS→CSSOM, Render Tree, Layout, Paint, Composite
- CSS is render-blocking — it must complete before the browser renders
- Scripts without `defer`/`async` block HTML parsing
- Changing geometry triggers reflow (expensive); changing only visuals triggers repaint; changing transform/opacity is cheapest
- Understanding the pipeline enables targeted performance optimisations

# Guided Practice Quest

**The Forge Sequence**

The Academy's forge must process materials in the correct order. Apprentices must identify which stage of the rendering pipeline handles each operation.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You have been given a webpage that scrolls jerkily. When you inspect it, you find this code in a scroll event listener:

```javascript
document.addEventListener('scroll', () => {
  const boxes = document.querySelectorAll('.card');
  boxes.forEach(box => {
    const top = box.getBoundingClientRect().top;
    box.style.width = (300 + top * 0.1) + 'px';
  });
});
```

Write a short analysis (4–6 sentences) that:
1. Identifies what is causing the poor performance
2. Names the specific rendering concept involved
3. Proposes a fix
4. Explains why your fix is better

# Integration

**Connecting to Sciences — Signal Processing and the Rendering Pipeline**

The rendering pipeline shares structural principles with signal processing pipelines in engineering. In signal processing, a raw signal passes through a defined sequence of transforms — sampling, filtering, amplification, encoding — before becoming useful output.

Both pipelines share key characteristics: each stage depends on the output of the previous stage, bottlenecks at any stage constrain the whole pipeline, and skipping stages produces incorrect output.

In signal processing, engineers identify the bottleneck stage and optimise it first (rather than randomly optimising all stages). The same approach applies to browser rendering: profile first to identify whether the bottleneck is parsing, layout, paint, or compositing — then target optimisation there.

What does this suggest about the value of measuring before optimising in engineering generally?

# Lore Conclusion

The forge runs smoothly. Each step flows into the next. The output is clean, fast, precise.

*"Every millisecond you save in the rendering pipeline is a millisecond returned to your user,"* says Master Aelindra. *"Performance is not a feature. It is respect."*

The rune brightens.

*"Next: the browser stores things — cookies, local data, session state. Understanding storage helps you build applications that remember — without breaking privacy or security."*
