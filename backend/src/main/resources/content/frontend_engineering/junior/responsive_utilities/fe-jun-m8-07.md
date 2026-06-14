---
id: fe-jun-m8-07
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m8
moduleTitle: "Module 8: Tailwind CSS"
moduleGlyph: "🎨"
moduleSortOrder: 8
topicSlug: responsive_utilities
topicTitle: "Responsive Utilities"
topicSortOrder: 3
lesson: breakpoint_system
title: "Tailwind's Breakpoint System"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m8-06]
integrationDomains: [design, ux]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Lists and explains at least 3 of Tailwind's 5 breakpoints with their pixel values"
    - "Explains that Tailwind uses a mobile-first approach (no prefix = mobile)"
    - "Demonstrates correct use of a breakpoint prefix (e.g. md:grid-cols-2)"
    - "Explains why mobile-first is considered best practice"
  keywords: [sm, md, lg, xl, breakpoint, mobile-first, prefix, responsive, viewport]
  modelAnswer: |
    Tailwind has five built-in breakpoints: sm (640px), md (768px), lg (1024px), xl (1280px), 2xl (1536px). Crucially, Tailwind is mobile-first — a class without a prefix applies from 0px up, and a prefix like `md:` overrides it at that breakpoint and above. So `grid-cols-1 md:grid-cols-2 lg:grid-cols-3` starts as one column, becomes two at 768px, and three at 1024px. Mobile-first is best practice because mobile is often the constrained case, and progressive enhancement is more manageable than trying to shrink a desktop layout down.
guidedSteps:
  - id: fe-jun-m8-07-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What does `hidden md:block` mean in Tailwind?"
    inputConfig:
      options:
        - "Hidden on mobile (under 768px), visible as block from 768px and above"
        - "Hidden on desktop, visible on mobile"
        - "Always hidden until the user clicks"
        - "Hidden until the md breakpoint passes, then permanently visible"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Hidden on mobile (under 768px), visible as block from 768px and above"]
      rejectedFeedback: "`hidden` applies display:none from 0px up. `md:block` overrides it at 768px+ with display:block. Mobile-first means the unprefixed class is the base (mobile) state."
    hint: "No prefix = mobile (smallest screens). md: prefix = applies from 768px up."
    reflectionPrompt: "What would `block md:hidden` do? Can you picture a use case for each?"
  - id: fe-jun-m8-07-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write the Tailwind classes for a column count that is 1 on mobile, 2 on tablet (md), and 4 on desktop (lg). The parent is a grid container."
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: [grid-cols-1, md:grid-cols-2, lg:grid-cols-4, grid]
      rejectedFeedback: "You need: `grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4`. Mobile-first means start with the smallest, then override upward."
    hint: "Start with mobile (no prefix), then add md: and lg: overrides."
    reflectionPrompt: "Why don't you need a `sm:` prefix here if your smallest target is mobile?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What pixel value does the `lg:` breakpoint correspond to in default Tailwind?"
    options:
      - "1024px"
      - "768px"
      - "1280px"
      - "900px"
    correctIndex: 0
    feedback: "lg = 1024px. The defaults: sm=640, md=768, lg=1024, xl=1280, 2xl=1536."
retrieval:
  recall: "List Tailwind's five default breakpoints and their pixel values."
  explain: "Explain what 'mobile-first' means in the context of Tailwind's breakpoint system."
  mistakeId:
    code: |
      // Developer wants 3 columns on desktop, 1 on mobile. They write:
      <div className="lg:grid-cols-3 grid-cols-1 grid">
    answer: "The order of classes in the className string doesn't matter — Tailwind's CSS specificity handles it. However, the intent is correct: `grid grid-cols-1 lg:grid-cols-3`. Classes with a prefix override unprefixed classes at the given breakpoint regardless of order in the string."
---

# Hook

An Academy apprentice proudly shows off the guild roster page on his laptop — perfectly styled with a three-column grid. The Head Artificer picks up a small scrying mirror (a mobile phone) and opens the page. The content is crushed and unreadable. The apprentice learns a hard lesson: styling for desktop only is not styling — it's guessing.

# Lore Introduction

Master Viewporter teaches from an ancient truth: "The magic must work on all scrying devices — from the smallest palm crystal to the great wall displays." Tailwind's breakpoint system is the tool for this. And its most important insight is where to start: with the smallest crystal first.

# Core Learning

## Concept Introduction

Tailwind is **mobile-first** by default. This means:
- A class with no prefix applies at all screen sizes (starting from 0px)
- A prefixed class (`sm:`, `md:`, `lg:`, `xl:`, `2xl:`) overrides it at that minimum width

**Default breakpoints:**

| Prefix | Min Width | Typical device |
|--------|-----------|----------------|
| (none) | 0px       | All screens (mobile base) |
| `sm:`  | 640px     | Large phones, small tablets |
| `md:`  | 768px     | Tablets |
| `lg:`  | 1024px    | Small laptops |
| `xl:`  | 1280px    | Desktops |
| `2xl:` | 1536px    | Wide desktops |

**Reading a responsive class string:**
```
grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3
```
- 0–767px: 1 column
- 768–1023px: 2 columns
- 1024px+: 3 columns

## Why It Matters

Mobile-first is best practice for two reasons:
1. **Progressive enhancement** — start with the simplest layout (one column), add complexity as space allows
2. **CSS specificity** — lower breakpoints being the base means overrides layer naturally upward

If you write desktop-first (using `max-` media queries), you must then "undo" styles at smaller breakpoints — messy and error-prone.

## Worked Example

```jsx
// A responsive hero section
function HeroSection() {
  return (
    <section className="flex flex-col lg:flex-row items-center gap-8 py-16 px-6">
      {/* Text: full width on mobile, half on desktop */}
      <div className="w-full lg:w-1/2">
        <h1 className="text-3xl lg:text-5xl font-bold text-gray-900 mb-4">
          Learn Magic, Build Worlds
        </h1>
        <p className="text-lg text-gray-600 max-w-prose">
          Join Arcane Academy and master the craft of frontend engineering.
        </p>
        <button className="mt-6 bg-indigo-600 text-white px-6 py-3 rounded-lg font-semibold">
          Start Your Journey
        </button>
      </div>

      {/* Image: full width on mobile, half on desktop */}
      <div className="w-full lg:w-1/2">
        <img
          src="/hero.png"
          alt="Academy illustration"
          className="w-full rounded-2xl shadow-xl"
        />
      </div>
    </section>
  );
}
```

On mobile: a stacked column layout. On desktop: side-by-side with the text and image each taking half the width.

## Common Mistakes

- **Forgetting the base (mobile) class** — writing `md:grid-cols-2 lg:grid-cols-3` without `grid-cols-1` means mobile has no column definition.
- **Using breakpoints unnecessarily** — many layouts are naturally responsive with `flex-wrap` or `grid auto-fill` without any breakpoint classes.
- **Thinking of breakpoints as ranges** — `md:` means "from 768px upward," not "only at 768px." It's a minimum, not a window.

## Mini Summary

Tailwind's five breakpoints (sm/md/lg/xl/2xl) use a mobile-first approach. Unprefixed classes are the mobile base; prefixed classes override at that minimum width and above. Think: start small, grow upward.

# Guided Practice Quest

Work through the guided steps to practise reading and writing responsive class strings.

# Solo Practice Quest

Build a responsive navigation: on mobile, display a hamburger icon only (hide the nav links). On `md:` and above, show the full nav links and hide the hamburger.

# Integration

**Design:** Mobile-first mirrors the concept of "progressive disclosure" in UX design — start with the essential experience, add detail as context (screen size) allows. Designing mobile-first forces prioritisation of content.

**UX:** Mobile users represent the majority of web traffic globally. Designing mobile-first is not just a technical choice — it's a user empathy choice. Starting with constraints forces better information architecture.

# Lore Conclusion

The apprentice opens the guild roster page on the scrying mirror. One column, readable, beautiful. He opens it on the wall display — three columns, spacious, just as good. Master Viewporter smiles. "Now your magic works everywhere it is cast."

---
