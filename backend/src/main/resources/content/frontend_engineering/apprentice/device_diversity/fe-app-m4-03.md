---
id: fe-app-m4-03
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m4
moduleTitle: "Module 4: Responsive Design"
moduleGlyph: "📱"
moduleSortOrder: 4
topicSlug: device_diversity
topicTitle: "Device Diversity"
topicSortOrder: 1
lesson: mobile_first_design
title: "Mobile First Design"
sortOrder: 3
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what mobile-first means in CSS terms (base styles = mobile, min-width queries)"
    - "Compares mobile-first vs desktop-first approaches and states the advantages of mobile-first"
    - "Writes a correct mobile-first media query"
    - "Explains how mobile-first CSS leads to simpler, less code"
    - "Describes progressive enhancement in the context of mobile-first"
  keywords: [mobile-first, min-width, max-width, progressive-enhancement, base-styles, media-query, desktop-first, override]
  modelAnswer: |
    Mobile-first CSS writes base styles for mobile viewports and uses min-width media
    queries to add enhancements at larger viewports. This is progressive enhancement:
    the smallest, most constrained experience is the baseline; larger screens add
    complexity. Mobile-first produces less code because simple mobile styles are the
    base; desktop styles only add differences. Desktop-first requires overriding
    complex styles downward, which is harder.
guidedSteps:
  - id: fe-app-m4-03-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which media query approach is mobile-first?
    inputConfig:
      options:
        - "@media (max-width: 768px) { /* mobile styles */ }"
        - "@media (min-width: 768px) { /* tablet and above */ }"
        - "Both are equivalent — mobile-first is just a philosophy"
        - "@media (device-width: 375px) { /* iPhone */ }"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["@media (min-width: 768px) { /* tablet and above */ }"]
      rejectedFeedback: "Mobile-first = base styles are mobile, min-width queries ADD styles for larger screens. max-width queries are desktop-first — base styles are for desktop, max-width REMOVES or overrides styles for smaller screens. min-width is additive; max-width is subtractive."
    hint: "Mobile-first adds enhancements as the screen gets larger."
    reflectionPrompt: "min-width = 'if the screen is at least this wide, apply these additional styles.' max-width = 'if the screen is at most this wide, apply these styles.' Mobile-first code flows: mobile base → tablet additions → desktop additions. Desktop-first code flows backwards, which is harder to maintain."

  - id: fe-app-m4-03-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Mobile-first base styles use ___ media queries to progressively enhance for larger screens.
    inputConfig:
      placeholder: "min-width"
    markingRule:
      matchMode: CONTAINS
      accepted: [min-width, "min-width"]
      rejectedFeedback: "min-width media queries add styles when the viewport is AT LEAST the specified width. Mobile styles are the base (no media query). @media (min-width: 768px) adds tablet layout. @media (min-width: 1024px) adds desktop layout. Always additive — never removing."
    hint: "Minimum width means 'at least this wide.'"
    reflectionPrompt: "Progressive enhancement: start with what works everywhere (mobile), then add what improves the experience on larger, more capable screens. Desktop-first is regressive reduction: start complex, then work downward — a harder and more error-prone process."

  - id: fe-app-m4-03-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 2-3 sentences why mobile-first CSS typically results in smaller, simpler files than desktop-first CSS.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [simple, small, override, add, complex, base, mobile, desktop, less]
      rejectedFeedback: "Mobile layouts are inherently simpler: one column, stacked elements, no complex grid. Desktop-first starts with complexity and uses media queries to undo it (display:none for sidebars, width:100% overrides, etc.). Mobile-first starts simple and only adds complexity where needed. Less override = less code."
    hint: "Which is simpler: adding something or taking something away?"
    reflectionPrompt: "Every override in CSS is technical debt — a declaration that undoes a previous decision. Mobile-first minimises overrides by making the simplest case the baseline. Desktop-first maximises overrides by making the most complex case the baseline. Fewer overrides = less complexity = easier debugging."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You have a sidebar that should appear on tablet and above but not on mobile. Mobile-first approach:"
    options:
      - "Show sidebar by default; add display:none at max-width: 768px"
      - "Hide sidebar (display:none) by default; add display:block at min-width: 768px"
      - "Set sidebar width:0 on mobile; change to width:250px on tablet"
      - "Use JavaScript to toggle the sidebar based on window.innerWidth"
    correctIndex: 1
    feedback: "Mobile-first: hide by default (no media query), show on larger screens (min-width query). This means mobile gets the simpler state without any media query. Desktop-first would show by default and hide on mobile — requiring a max-width query override."
  - type: MULTIPLE_CHOICE
    question: "Mobile-first design forces developers to:"
    options:
      - "Build the mobile version last, which takes more time"
      - "Prioritise content and decisions about what is essential — not what is decorative"
      - "Write twice as much CSS"
      - "Remove features from the desktop version"
    correctIndex: 1
    feedback: "Mobile constraints force prioritisation. On mobile there is no room for 'nice to have' elements — only what matters. This discipline often reveals that some desktop 'features' are actually clutter. Mobile-first is as much a content strategy as a CSS strategy."

retrieval:
  recall: "Write a mobile-first CSS snippet: base (mobile) styles for a navigation, then a media query that adds a horizontal layout at 768px."
  explain: "Explain why 'progressive enhancement' is a better framing than 'graceful degradation' for responsive design."
  mistakeId:
    code: "@media (max-width: 768px) { .sidebar { display: none; } } — desktop-first, harder to maintain"
    answer: "This is desktop-first — the sidebar shows by default, hidden on mobile. Mobile-first: .sidebar { display: none; } as base (no query), then @media (min-width: 768px) { .sidebar { display: block; } }. Mobile-first is additive and easier to extend."
---

# Hook

Mobile-first is not just a technical choice. It is a discipline.

When you design for the smallest screen first, you are forced to make decisions: what is essential, what is supplementary, what can be removed entirely. These decisions produce better products — leaner, more focused, more useful.

The constraints of mobile make you a better designer.

# Lore Introduction

*"The Academy's field scribes,"* says Master Aelindra, *"carry only what fits in a small pack. Every item is there because it earns its place. Back at the tower, they have room for more. But the essentials are always defined by what fit in the pack. Mobile-first is the pack."*

# Core Learning

## Concept Introduction

**Mobile-first CSS structure:**

```css
/* BASE STYLES — mobile (no media query) */
.nav {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.sidebar {
  display: none; /* hidden on mobile */
}

.card-grid {
  display: grid;
  grid-template-columns: 1fr; /* single column */
  gap: 1rem;
}

/* TABLET ENHANCEMENTS — min-width: 768px */
@media (min-width: 768px) {
  .nav {
    flex-direction: row;
    align-items: center;
  }
  
  .sidebar {
    display: block;
  }

  .card-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

/* DESKTOP ENHANCEMENTS — min-width: 1024px */
@media (min-width: 1024px) {
  .card-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}
```

**Mobile-first vs Desktop-first:**

| | Mobile-first | Desktop-first |
|---|---|---|
| Base styles | Simple (mobile) | Complex (desktop) |
| Media queries | `min-width` — additive | `max-width` — subtractive |
| Code volume | Less | More (more overrides) |
| Debugging | Easier | Harder |
| Default experience | Works on all screens | May break on small screens |

## Why It Matters

Over 60% of users are on mobile. Mobile-first ensures the majority experience works without media queries at all. Desktop enhancements are additive — they never break the mobile baseline.

## Mental Model

Mobile-first is packing a small suitcase first. If you start with a huge case (desktop design), everything goes in unsorted — and when you're forced to repack into a carry-on (mobile), you're cutting and cramming under pressure, which is exactly how desktop sites become broken mobile sites. Start with the carry-on instead: only the essentials fit, so you decide what truly matters. Given a bigger case later (wider screens), you *add* comfort items deliberately with `min-width` queries. Constraint first, enhancement second — the design grows up gracefully instead of shrinking down painfully.

## Mini Summary

- ✔ Mobile-first: base styles = mobile; `min-width` queries add for larger screens
- ✔ Progressive enhancement: simple baseline + additive improvements
- ✔ Mobile-first produces less code (fewer overrides)
- ✔ Mobile constraints force content prioritisation
- ✔ `min-width` = additive; `max-width` = subtractive; prefer additive

# Guided Practice Quest

**The Pack First** — three questions on mobile-first principles and media queries. Steps in `guidedSteps`.

# Solo Practice Quest

Take a three-column desktop layout (header, three content columns, footer) and write it mobile-first: base styles produce a single-column layout on mobile, one media query adds two columns at 768px, another adds three at 1024px. Explain what each media query adds.

# Integration

**Connecting to Psychology — Constraint-Driven Creativity**

Research in creativity (Stokes, 2005; Finke, 1996) consistently shows that constraints enhance rather than limit creative output. Artists given unlimited materials produce less focused work than artists given a limited palette. Engineers given unlimited screen space produce cluttered, unfocused interfaces. Mobile constraints — limited pixels, touch targets, attention — force the question "what actually matters?" consistently producing cleaner, more usable interfaces than desktop-first design. The constraint is the creative tool.

# Lore Conclusion

*"Build for the small screen first. Every element that survives that constraint has earned its place. Every element that only works on a large screen is a luxury — add it after, knowing it is non-essential. The essentials are revealed by the constraints. The luxuries come later."*

---
