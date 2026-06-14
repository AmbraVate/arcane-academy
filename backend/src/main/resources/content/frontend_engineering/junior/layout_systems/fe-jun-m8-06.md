---
id: fe-jun-m8-06
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
lesson: spacing_and_sizing
title: "Spacing and Sizing"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m8-04, fe-jun-m8-05]
integrationDomains: [design, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the difference between padding and margin in Tailwind (p-* vs m-*)"
    - "Uses width and height utilities correctly (w-full, w-1/2, h-screen, h-auto)"
    - "Explains max-width (max-w-*) for constraining content width"
    - "Connects consistent spacing to visual design quality"
  keywords: [padding, margin, width, height, max-w, min-h, spacing, scale, responsive]
  modelAnswer: |
    Tailwind uses the same numeric scale for spacing utilities. `p-4` adds 16px padding on all sides; `px-4` only horizontal; `m-auto` centres a block element. Width utilities: `w-full` fills the container, `w-1/2` takes half, `w-64` is a fixed 256px. `max-w-2xl` caps width to prevent overly wide text. `h-screen` fills the viewport height. `min-h-screen` ensures at least full viewport even with little content. Consistent spacing across a project — always using multiples of 4 from the scale — creates visual rhythm and professional polish.
guidedSteps:
  - id: fe-jun-m8-06-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A content column should be centred on the page and never wider than 768px. Which classes achieve this?"
    inputConfig:
      options:
        - "max-w-3xl mx-auto"
        - "w-768 mx-center"
        - "max-width-768 margin-auto"
        - "w-full text-center"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["max-w-3xl mx-auto"]
      rejectedFeedback: "`max-w-3xl` caps width at 768px; `mx-auto` centres the block by setting horizontal margins to auto. This is the standard content-column pattern."
    hint: "Two utilities: one to cap the width, one to centre horizontally."
    reflectionPrompt: "Why is max-w better than w-768px for a content column that should be full-width on small screens?"
  - id: fe-jun-m8-06-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Explain the difference between `p-4` and `m-4`. When would you use each?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [padding, margin, inside, outside, space, element, between]
      rejectedFeedback: "Padding adds space inside the element (between content and border). Margin adds space outside (between this element and its neighbours)."
    hint: "Think about where the space appears: inside the element, or around it."
    reflectionPrompt: "Why might you prefer `gap-*` over `m-*` for spacing between flex/grid children?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the difference between `min-h-screen` and `h-screen`?"
    options:
      - "min-h-screen ensures at least full viewport height; h-screen fixes the height to exactly the viewport height (scroll is cut off if content overflows)"
      - "They are identical"
      - "h-screen is for mobile; min-h-screen is for desktop"
      - "min-h-screen sets minimum height to 0; h-screen sets it to 100vh"
    correctIndex: 0
    feedback: "h-screen = exactly 100vh (content can overflow or be hidden). min-h-screen = at least 100vh, grows with content. Use min-h-screen for page wrappers."
retrieval:
  recall: "What is the Tailwind class to make an image fill its container width while maintaining aspect ratio?"
  explain: "Why use max-w-* rather than w-* for a page content container?"
  mistakeId:
    code: |
      // Developer wants a full-height page layout and writes:
      <div className="h-screen flex flex-col">
        <Header />
        <main className="flex-1 overflow-y-auto">
          <VeryLongContent />
        </main>
      </div>
    answer: "Actually this is correct! h-screen on the wrapper fixes the total height to the viewport, flex-col stacks header and main, flex-1 makes main take remaining space, overflow-y-auto enables scrolling inside main. A common mistake would be using min-h-screen here — which would allow the page to grow beyond the viewport, breaking the sticky-scroll-main pattern."
---

# Hook

The Academy's welcome page looks amateurish — some sections have cramped content pressed against edges, others have text stretching across the full width of a widescreen monitor becoming impossible to read. The Spacing Artificer arrives with a simple message: "Consistent spacing is the difference between amateur and professional work. Let's fix it."

# Lore Introduction

In the Academy's Proportion Chamber, every measurement is deliberate. "Space is not empty," teaches Master Proportia. "Space is structure. Padding says 'breathe.' Margin says 'distance.' Max-width says 'enough.' Together they create a visual rhythm that the eye recognises as quality, even when the viewer cannot name it."

# Core Learning

## Concept Introduction

**Padding vs Margin:**

| Property | What it does |
|----------|-------------|
| `p-4`    | 16px inside all edges |
| `px-4`   | 16px left + right only |
| `py-4`   | 16px top + bottom only |
| `pt-2`   | 8px top only |
| `m-4`    | 16px outside all edges |
| `mx-auto`| auto horizontal margins (centres block) |
| `mt-8`   | 32px top margin |

**Width utilities:**
```
w-full    → 100% of parent
w-1/2     → 50%
w-1/3     → 33.333%
w-64      → 256px (16rem)
w-screen  → 100vw
w-auto    → auto (default)
```

**Height utilities:**
```
h-full      → 100% of parent
h-screen    → 100vh
h-auto      → auto
h-64        → 256px
min-h-screen → min-height: 100vh (page grows with content)
```

**Max/min constraints:**
```
max-w-sm    → 384px
max-w-md    → 448px
max-w-lg    → 512px
max-w-xl    → 576px
max-w-2xl   → 672px
max-w-3xl   → 768px
max-w-4xl   → 896px
max-w-5xl   → 1024px
max-w-6xl   → 1152px
max-w-7xl   → 1280px
max-w-prose → 65ch (optimal reading line length)
```

## Why It Matters

Consistent spacing creates visual rhythm. Using values outside the 4px scale creates jarring inconsistency that viewers perceive subconsciously as "off." `max-w-prose` is particularly valuable — 65 characters per line is the typographically optimal reading length, and Tailwind bakes this in.

## Worked Example

```jsx
// A page layout with proper spacing and sizing
function PageLayout({ children }) {
  return (
    // Full viewport height, white background
    <div className="min-h-screen bg-gray-50">
      {/* Sticky header with horizontal padding */}
      <header className="sticky top-0 bg-white border-b border-gray-200 px-6 py-4">
        <div className="max-w-7xl mx-auto flex justify-between items-center">
          <span className="text-xl font-bold">Arcane Academy</span>
        </div>
      </header>

      {/* Main content constrained and centred */}
      <main className="max-w-4xl mx-auto px-6 py-10">
        {children}
      </main>

      {/* Footer */}
      <footer className="border-t border-gray-200 px-6 py-8 mt-auto">
        <div className="max-w-7xl mx-auto text-sm text-gray-500">
          © Arcane Academy
        </div>
      </footer>
    </div>
  );
}

// Article with optimal reading width
function ArticleContent({ content }) {
  return (
    <article className="max-w-prose mx-auto space-y-6 text-gray-800 leading-relaxed">
      {content}
    </article>
  );
}
```

## Common Mistakes

- **Using fixed `w-*` for content containers** — `w-3xl` doesn't work (Tailwind doesn't have `w-3xl`). Use `max-w-3xl` for content columns.
- **Forgetting `mx-auto`** — `max-w-*` constrains the width but doesn't centre. Add `mx-auto` to centre block elements.
- **Using `h-screen` on page wrappers with long content** — content overflows invisibly. Use `min-h-screen` instead.
- **Overusing margins when gaps would be cleaner** — prefer `gap-*` on flex/grid parents over `mt-*` on every child.

## Mini Summary

Tailwind's spacing utilities use the base-4 scale consistently. `p-*` for internal spacing, `m-*` for external, `max-w-*` to constrain content width, `mx-auto` to centre, and `min-h-screen` for full-height page layouts. Consistent use of the scale creates professional visual rhythm.

# Guided Practice Quest

Work through the guided steps to practise the content-column and page-layout patterns.

# Solo Practice Quest

Build a blog post layout: a full-width header image (h-64 object-cover), followed by constrained article content (max-w-prose, centred), with comfortable vertical padding (py-12) and a readable line length.

# Integration

**Design:** Typography research (Bringhurst, 1992) establishes 45–75 characters per line as optimal for comfortable reading. `max-w-prose` (65ch) sits in this range. Spacing systems in design — 8px grids, baseline grids — mirror Tailwind's base-4 approach.

**Mathematics:** The ratio between adjacent spacing steps approximates a harmonic progression, creating visual intervals similar to musical intervals. Steps 4, 6, 8, 12 correspond to padding values that feel harmonious together.

# Lore Conclusion

Master Proportia inspects the refactored welcome page. Content breathes with proper padding. Text columns cap at readable widths. Nothing stretches uncomfortably across a widescreen. "Space is structure," she repeats. The apprentices finally understand — they have not added emptiness, they have added intention.

---
