---
id: fe-jun-m8-08
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
lesson: responsive_layouts
title: "Building Responsive Layouts"
sortOrder: 2
difficulty: 4
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m8-07]
integrationDomains: [design, ux]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Demonstrates a responsive grid that changes column count at breakpoints"
    - "Uses hidden/block (or md:hidden/md:block) to show/hide elements responsively"
    - "Applies responsive typography (different text sizes at different breakpoints)"
    - "Describes a real layout pattern (card grid, hero, dashboard) and how it adapts"
  keywords: [responsive, hidden, block, grid-cols, text-size, breakpoint, mobile, desktop, flex-col, flex-row]
  modelAnswer: |
    A responsive card grid might use `grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4`. The hero section might stack columns on mobile (`flex flex-col`) and go side-by-side on desktop (`lg:flex-row`). Navigation items hidden on mobile (`hidden md:flex`) become visible on desktop. Typography scales up: `text-2xl lg:text-4xl` for a headline. The key insight is always writing the mobile style first, then overriding at larger breakpoints.
guidedSteps:
  - id: fe-jun-m8-08-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A sidebar menu should be hidden on mobile and shown as a flex column on lg screens. Which classes achieve this?"
    inputConfig:
      options:
        - "hidden lg:flex lg:flex-col"
        - "invisible lg:visible flex-col"
        - "display-none lg:display-flex"
        - "sm:hidden lg:flex-col"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["hidden lg:flex lg:flex-col"]
      rejectedFeedback: "`hidden` sets display:none for mobile. `lg:flex` overrides to display:flex at 1024px+. `lg:flex-col` sets the direction. Combined: hidden until lg, then flex column."
    hint: "Two concerns: what display value do you want at each size? Start with mobile, override for desktop."
    reflectionPrompt: "What would `block md:hidden` do? Think of a mobile-only element like a hamburger menu."
  - id: fe-jun-m8-08-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Describe in your own words how you would build a responsive hero section that stacks vertically on mobile and displays side-by-side on lg screens. Include at least two Tailwind class examples."
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [flex-col, lg:flex-row, flex, lg:w-1/2, stack, side, column]
      rejectedFeedback: "The hero container needs `flex flex-col lg:flex-row`. Each child (text and image) starts full width, then becomes `lg:w-1/2` on desktop."
    hint: "Think about the container direction changing from column to row at a breakpoint."
    reflectionPrompt: "Why is flex-col the mobile default for a hero rather than flex-row?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "How do you make an `<h1>` display as `text-2xl` on mobile and `text-5xl` on lg screens?"
    options:
      - "text-2xl lg:text-5xl"
      - "mobile:text-2xl desktop:text-5xl"
      - "text-responsive-2xl-5xl"
      - "text-2xl (Tailwind auto-scales for desktop)"
    correctIndex: 0
    feedback: "Responsive typography is the same pattern: base class first (mobile), then a prefixed override. `text-2xl lg:text-5xl` — 2xl on mobile, 5xl from 1024px up."
retrieval:
  recall: "What classes make a flex container stack vertically on mobile and horizontal on md screens?"
  explain: "Explain how you would hide a desktop sidebar on mobile without JavaScript."
  mistakeId:
    code: |
      // Developer wants to hide a desktop nav on mobile only
      <nav className="md:hidden flex gap-4">
        <a href="/">Home</a>
        <a href="/quests">Quests</a>
      </nav>
    answer: "`md:hidden` hides the nav FROM 768px up, but shows it on mobile — the opposite of the intent. If you want it visible on mobile and hidden on desktop: `flex md:hidden`. If you want it hidden on mobile and visible on desktop: `hidden md:flex`."
---

# Hook

A Senior Artificer reviews the Academy portal and taps a list of requirements: the sidebar must vanish on phones, the card grid must flow from one to three columns as screen space grows, and the hero heading must be twice as large on a desktop. "Three responsive challenges," she says. "Three patterns. Learn them and you can handle any layout."

# Lore Introduction

Master Adapta is known throughout the Academy for her ability to design magic that reshapes itself to fit any vessel — from the tiniest travel scroll to the grand display crystals in the lecture hall. "Responsive design is not about fighting the screen size," she says, "it is about guiding the layout to its best form at every size."

# Core Learning

## Concept Introduction

Three patterns cover the majority of responsive layout problems:

**Pattern 1: Responsive grid column count**
```jsx
<div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
  {items.map(item => <Card key={item.id} {...item} />)}
</div>
```
Mobile: 1 column. Tablet: 2. Desktop: 3. Wide: 4.

**Pattern 2: Stacked to side-by-side**
```jsx
<section className="flex flex-col lg:flex-row gap-8">
  <div className="w-full lg:w-1/2">Left content</div>
  <div className="w-full lg:w-1/2">Right content</div>
</section>
```

**Pattern 3: Show/hide elements**
```jsx
{/* Mobile hamburger — visible on mobile, hidden on md+ */}
<button className="block md:hidden">☰</button>

{/* Desktop nav — hidden on mobile, visible on md+ */}
<nav className="hidden md:flex items-center gap-6">
  <a href="/quests">Quests</a>
  <a href="/profile">Profile</a>
</nav>
```

## Why It Matters

These patterns handle most real-world layouts. The key insight is that every responsive change is just a class override at a breakpoint — no media queries to write, no separate CSS files.

## Worked Example

```jsx
// A complete responsive page section
function FeatureSection({ features }) {
  return (
    <section className="py-16 px-4 sm:px-6 lg:px-8">
      {/* Responsive heading */}
      <h2 className="text-2xl sm:text-3xl lg:text-4xl font-bold text-center text-gray-900 mb-4">
        Academy Features
      </h2>
      <p className="text-base sm:text-lg text-gray-600 text-center max-w-2xl mx-auto mb-12">
        Everything you need to master frontend engineering.
      </p>

      {/* Responsive feature grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 max-w-6xl mx-auto">
        {features.map(feature => (
          <div key={feature.id} className="bg-white rounded-xl p-6 shadow border border-gray-100">
            <div className="text-3xl mb-3">{feature.icon}</div>
            <h3 className="text-lg font-semibold text-gray-900 mb-2">{feature.title}</h3>
            <p className="text-gray-600 text-sm leading-relaxed">{feature.description}</p>
          </div>
        ))}
      </div>
    </section>
  );
}
```

## Common Mistakes

- **Forgetting responsive padding** — `px-4 sm:px-6 lg:px-8` gives appropriate breathing room at each size; forgetting breakpoints here makes mobile feel cramped.
- **Over-applying breakpoints** — not every element needs responsive classes. `max-w-prose mx-auto` is naturally responsive without any breakpoints.
- **Using `md:hidden` when you mean `hidden md:block`** — the semantics are reversed. Write the mobile state first, then add the desktop override.

## Mini Summary

Three patterns handle most responsive layouts: responsive grid column counts, stacked-to-side-by-side flex, and show/hide with `hidden`/`block`. Apply the mobile state first, override at breakpoints.

# Guided Practice Quest

Work through the guided steps to practise hide/show patterns and responsive flex direction.

# Solo Practice Quest

Build a complete responsive feature section: 1 column on mobile, 2 on sm, 3 on lg. Include responsive heading size (`text-xl lg:text-3xl`) and responsive padding (`px-4 lg:px-8`).

# Integration

**Design:** Responsive design mirrors "adaptive content" in UX — the same information presented at the right density for the context. On mobile: essential information. On desktop: full detail. This mirrors how good writing adapts to the audience.

**UX:** "Content prioritisation" is a core responsive design principle. When you decide what to show/hide at each breakpoint, you are making UX decisions about what matters most to users at each context.

# Lore Conclusion

Master Adapta watches the portal reshape itself across three scrying devices. The grid flows from one column to three. The hero section stacks on the palm crystal and spreads side-by-side on the wall display. The navigation collapses to a menu icon on mobile. "You have not made three designs," she says. "You have made one design that knows where it is."

---
