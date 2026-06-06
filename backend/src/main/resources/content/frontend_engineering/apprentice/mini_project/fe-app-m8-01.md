---
id: fe-app-m8-01
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m8
moduleTitle: "Module 8: Apprentice Project"
moduleGlyph: "🏗️"
moduleSortOrder: 8
topicSlug: mini_project
topicTitle: "Mini Project"
topicSortOrder: 1
lesson: the_personal_portfolio
title: "The Personal Portfolio"
sortOrder: 1
difficulty: 3
estimatedMinutes: 120
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: low
questTypes: [solo]
prerequisites:
  - fe-app-m1-01
  - fe-app-m2-01
  - fe-app-m3-01
  - fe-app-m4-01
  - fe-app-m5-01
  - fe-app-m6-01
  - fe-app-m7-01
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "HTML is semantic — header, nav, main, section, article, and footer used correctly"
    - "CSS is in a separate stylesheet with consistent organisation (reset → variables → components)"
    - "Page is responsive — readable on both 375px mobile and 1440px desktop viewports"
    - "At least one JavaScript interaction works (e.g. mobile nav toggle, active section highlight, theme toggle)"
    - "All images have meaningful alt text; decorative images have alt empty string"
    - "All navigation links and interactive elements are keyboard-accessible"
    - "A type scale and spacing scale are defined as CSS custom properties"
    - "Written reflection explains three design/technical decisions and one thing to improve"
  keywords: [HTML, CSS, JavaScript, responsive, semantic, accessibility, flexbox, grid, custom-properties, portfolio]
  modelAnswer: |
    A complete Personal Portfolio uses correct semantic HTML (header, nav, main, footer,
    section, article), an external CSS stylesheet with custom properties for colour and
    spacing, at least one media query for responsiveness, a working JavaScript interaction,
    correct alt text on all images, and keyboard-accessible navigation. The reflection
    demonstrates awareness of design decisions and trade-offs made during the build.
---

# Hook

Seven modules. The Internet. HTML. CSS — cascade, specificity, flexbox, grid. Responsive design. JavaScript and the DOM. Accessibility. Engineering habits.

You have learned each piece separately. Now you build something real.

The Personal Portfolio is your first complete frontend project — a site that exists in a browser, looks good on any device, and demonstrates everything you have learned. It is not a tutorial exercise. It is your work, built your way, with your choices.

> Before you start: think about what story your portfolio tells. What do you want someone to know about you as a developer before they read a single word?

# Lore Introduction

Master Aelindra stands at the entrance of the Academy's workshop.

*"The apprentice has learned the craft. Now they must demonstrate it. Not in an exercise — in a work. A portfolio is how the world knows you. It is the first thing a future employer sees, the first impression a collaborator forms. It speaks before you do."*

She hands you the commission.

*"Build something you are proud of. Build something correct. Build something that works for everyone who might use it. These three standards — pride, correctness, universality — are the standards of the craft."*

# Project Brief

Build a **personal portfolio website** — a real, deployable site that introduces you as a frontend developer.

---

## Required Sections

| Section | Purpose | Required content |
|---|---|---|
| **Header / Navigation** | Site identity + navigation | Your name, nav links, mobile toggle |
| **Hero** | First impression | Brief intro, one clear call-to-action |
| **About** | Your story | 2–3 paragraphs + a photo or avatar |
| **Skills** | Technical skills | Visual skill list (tags, grid, or icons) |
| **Projects** | Portfolio items | 2–3 projects with title, description, links |
| **Contact** | Reach you | Email link or contact form |
| **Footer** | Closing | Copyright, social links |

---

## Technical Requirements

### HTML
- [ ] Semantic elements throughout: `<header>`, `<nav>`, `<main>`, `<section>`, `<article>`, `<footer>`
- [ ] Navigation links are `<a href>` — not `<div>` or `<span>`
- [ ] Contact form (if included) has labels associated with inputs
- [ ] All images have `alt` text (descriptive for informative, `alt=""` for decorative)
- [ ] Viewport meta tag present
- [ ] Correct heading hierarchy (one `<h1>`, logical `<h2>`/`<h3>` nesting)

### CSS
- [ ] External stylesheet (no inline styles)
- [ ] CSS custom properties for: colours, spacing scale, border-radius
- [ ] Type scale using `rem` or `clamp()` for at least 3 heading sizes
- [ ] Mobile-first with at least one `min-width` media query
- [ ] Flexbox or Grid used for at least two sections
- [ ] Organised: reset → variables → base → components

### JavaScript
- [ ] At least one working interaction. Choose one or more:
  - Mobile navigation toggle (open/close menu)
  - Active section highlight in the nav (scroll-based)
  - Smooth scroll to sections
  - Light/dark mode toggle
  - Project filter (show/hide by category)

### Accessibility
- [ ] All interactive elements keyboard-focusable and usable with Tab + Enter/Space
- [ ] Navigation includes a skip-to-content link
- [ ] Colour contrast passes WCAG AA (verify with DevTools or WebAIM)
- [ ] Images have correct alt text

---

## Scaffolding

**Suggested file structure:**
```
portfolio/
├── index.html
├── css/
│   └── styles.css
├── js/
│   └── main.js
└── images/
    └── (your images)
```

**CSS starter (add to the top of styles.css):**
```css
/* 1. Reset */
*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

/* 2. Variables */
:root {
  --color-primary:  hsl(262, 72%, 58%);
  --color-text:     hsl(220, 30%, 10%);
  --color-bg:       hsl(220, 20%, 98%);
  --color-muted:    hsl(220, 10%, 50%);
  --space-4:  1rem;
  --space-6:  1.5rem;
  --space-8:  2rem;
  --space-12: 3rem;
  --border-radius: 8px;
}

/* 3. Base */
body { font-family: Inter, system-ui, sans-serif; background: var(--color-bg); color: var(--color-text); }
img  { max-width: 100%; height: auto; display: block; }
```

**Mobile nav toggle JavaScript pattern:**
```javascript
const navToggle = document.querySelector('.nav-toggle');
const navMenu   = document.querySelector('.nav-menu');

navToggle.addEventListener('click', () => {
  const isOpen = navMenu.classList.toggle('is-open');
  navToggle.setAttribute('aria-expanded', isOpen);
});
```

---

## Acceptance Criteria

- [ ] Site opens in a browser and all sections are visible
- [ ] Navigation links scroll to or jump to the correct sections
- [ ] Page reads correctly at 375px (phone) and 1440px (desktop) without horizontal scroll
- [ ] At least one JavaScript interaction works
- [ ] Tab through the page — every link and button is reachable and activatable
- [ ] All images have alt text
- [ ] Custom properties are defined and used (no raw hex values in component rules)
- [ ] Written reflection submitted (see below)

---

## Reflection Prompt

After completing the project, write **4–6 sentences** addressing:

1. What was the hardest technical challenge? How did you solve it?
2. What design decision are you most proud of, and why?
3. What accessibility consideration did you find most difficult to implement?
4. If you had one more day, what would you add or improve?

---

# Integration

**Connecting to Psychology — First Impressions and the Halo Effect**

Research by Nalini Ambady and Robert Rosenthal shows that first impressions form within milliseconds and are remarkably predictive of later judgments. The Halo Effect (Solomon Asch, 1946) means a positive first impression creates a positive bias toward all subsequent content. In portfolio context: a visually polished, fast-loading, accessible first screen creates a halo that improves perception of everything that follows — including your technical work. Conversely, a broken layout or slow load creates a negative halo that the most impressive projects cannot fully overcome.

This is why the hero section matters disproportionately — and why performance, accessibility, and visual hierarchy are not optional extras. They are the engineering of first impressions.

# Lore Conclusion

Master Aelindra reviews the completed portfolio.

*"Does it load quickly? Does it work on all screens? Can someone navigate it without a mouse? Does it represent you accurately? These are the only questions that matter."*

She marks it with the workshop seal.

*"You have moved from learning the craft to practising it. The portfolio will be imperfect — all first works are. What matters is that you built it correctly, deliberately, and with care for everyone who might use it. That is the standard of the craft. You have met it."*

The first seal of the Frontend path is inscribed. The journey to Junior begins.

---
