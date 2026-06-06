---
id: fe-app-m4-01
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
lesson: screen_sizes
title: "Screen Sizes"
sortOrder: 1
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Names common breakpoint ranges and device categories"
    - "Explains the difference between CSS pixels and physical pixels (device pixel ratio)"
    - "Explains why designing only for desktop is a design failure"
    - "Names the viewport meta tag and explains its purpose"
    - "Identifies statistics about mobile vs desktop web usage"
  keywords: [viewport, breakpoint, mobile, tablet, desktop, device-pixel-ratio, meta-viewport, responsive, CSS-pixel, physical-pixel]
  modelAnswer: |
    Modern users access the web on screens ranging from 320px (small phone) to 2560px+
    (large desktop). Mobile users account for over 60% of global web traffic. CSS pixels
    are logical units; physical pixels on high-DPI screens are 2-3× denser. The viewport
    meta tag (<meta name="viewport" content="width=device-width, initial-scale=1">) is
    required for mobile browsers to display at the correct scale.
guidedSteps:
  - id: fe-app-m4-01-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which meta tag is essential for mobile browsers to render a page at the correct scale?
    inputConfig:
      options:
        - "<meta name=\"mobile\" content=\"true\">"
        - "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
        - "<meta name=\"responsive\" content=\"auto\">"
        - "<meta name=\"scale\" content=\"mobile\">"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"]
      rejectedFeedback: "Without the viewport meta tag, mobile browsers default to a virtual 980px-wide viewport and shrink the page to fit — making everything tiny. The viewport tag tells the browser: 'use the actual device width and don't scale.' This is the first step for any responsive page."
    hint: "This goes in the <head> of every HTML page intended for mobile."
    reflectionPrompt: "The viewport meta tag is so fundamental it is included in every HTML boilerplate. Without it, even a perfectly responsive CSS layout will render incorrectly on mobile. It is a one-line fix for a critical display problem."

  - id: fe-app-m4-01-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A mobile phone has a device pixel ratio of 3. If its CSS viewport is 390px wide, how many physical pixels wide is the screen?

      `390 × ___ = 1170 physical pixels`
    inputConfig:
      placeholder: "3"
    markingRule:
      matchMode: CONTAINS
      accepted: ["3"]
      rejectedFeedback: "CSS pixels are logical units. On a 3× (Retina) display, each CSS pixel maps to 3×3 physical pixels. This is why images look blurry on high-DPI screens if not served at sufficient resolution — 1 CSS pixel at 3× DPR needs a 3× image."
    hint: "Physical pixels = CSS pixels × device pixel ratio."
    reflectionPrompt: "This distinction matters for images: a 100×100px CSS image on a 3× display renders across 300×300 physical pixels. If the image source is only 100×100, it is upscaled and blurry. Serving 2× or 3× images (srcset) solves this."

  - id: fe-app-m4-01-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 2-3 sentences why a developer who only tests on their desktop monitor is not doing their job correctly.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [mobile, 60%, majority, half, most, users, screen, small, viewport, test]
      rejectedFeedback: "Over 60% of global web traffic comes from mobile devices. Designing only for desktop fails the majority of users. Testing only on one viewport size means you only know how the site behaves in one of dozens of real-world contexts. Responsive design is not a feature — it is the baseline."
    hint: "Think about what percentage of web traffic comes from mobile devices."
    reflectionPrompt: "In many markets (India, Southeast Asia, Sub-Saharan Africa), mobile-first is not a preference — it is the only option. Web access on mobile-only devices exceeds 80% in some regions. Desktop-first design is a privilege assumption that fails most of the world's web users."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Common CSS breakpoints are defined in:"
    options:
      - "Screen inches"
      - "Physical pixels"
      - "CSS pixels (logical pixels)"
      - "Viewport percentages"
    correctIndex: 2
    feedback: "Breakpoints are always in CSS pixels — the logical units that CSS uses. @media (max-width: 768px) means '768 CSS pixels or fewer,' regardless of the device pixel ratio. A phone might have 2340 physical pixels but only 390 CSS pixels wide."
  - type: MULTIPLE_CHOICE
    question: "Approximately what percentage of global web traffic comes from mobile devices?"
    options:
      - "20–25%"
      - "35–40%"
      - "55–65%"
      - "80–90%"
    correctIndex: 2
    feedback: "As of 2024, mobile devices account for approximately 55–65% of global web traffic (varies by region, industry, and time of day). In many developing markets, it is significantly higher. Mobile-first is not a trend — it is the current reality of web usage."

retrieval:
  recall: "Name three common CSS breakpoint widths and the device category each represents."
  explain: "Explain what the viewport meta tag does and why it is required for mobile responsiveness."
  mistakeId:
    code: "Building and testing a site only on a 1920px desktop monitor"
    answer: "You are designing for 35-40% of your users and ignoring 60%+ who use mobile. Test at multiple viewports: 320px (small phone), 375px (iPhone), 768px (tablet), 1024px (laptop), 1440px (desktop). Browser DevTools device emulation makes this easy."
---

# Hook

Your screen is one screen. Your users have thousands.

A 6-year-old budget Android phone at 320px wide. A 16-inch MacBook Pro. A tablet in portrait mode. A 4K desktop monitor. An iPad in split-screen mode at 50% of its width.

Every one of these is a real device used by real users. Responsive design is the discipline of building for all of them.

# Lore Introduction

*"The Academy's master cartographers,"* says Master Aelindra, *"create maps that read clearly at any scale — detailed at close range, coherent from afar. A map that only works at one scale is not a map. It is a picture. Build maps, not pictures."*

# Core Learning

## Concept Introduction

**Common device categories and viewport widths:**

| Device | Typical CSS width |
|---|---|
| Small phone | 320–375px |
| Large phone | 390–430px |
| Tablet portrait | 768px |
| Tablet landscape | 1024px |
| Laptop | 1280–1440px |
| Desktop | 1440–1920px |
| Wide/4K | 2560px+ |

**The viewport meta tag (required in every HTML `<head>`):**
```html
<meta name="viewport" content="width=device-width, initial-scale=1">
```

Without it, mobile browsers render a 980px virtual viewport and shrink everything.

**Device pixel ratio (DPR):**
- Standard displays: 1 CSS pixel = 1 physical pixel
- Retina/HiDPI: 1 CSS pixel = 2–3 physical pixels
- Affects image sharpness — serve higher resolution images on high-DPR screens

**Common breakpoints:**
```css
/* Mobile first */
/* Base styles = mobile */
@media (min-width: 768px) { /* tablet */ }
@media (min-width: 1024px) { /* laptop */ }
@media (min-width: 1440px) { /* desktop */ }
```

## Why It Matters

60%+ of global web traffic comes from mobile. A site that breaks on mobile fails the majority of its users. Responsive design is not a bonus feature — it is the minimum requirement for a production-quality website.

## Mini Summary

- ✔ The viewport meta tag is required for correct mobile rendering
- ✔ CSS pixels ≠ physical pixels on high-DPI screens
- ✔ Over 60% of web traffic is mobile — design for all viewports
- ✔ Common breakpoints: 768px (tablet), 1024px (laptop), 1440px (desktop)

# Guided Practice Quest

**The Multi-Realm Atlas** — three questions on device diversity and viewport fundamentals. Steps in `guidedSteps`.

# Solo Practice Quest

Open your browser DevTools, switch to responsive mode, and test any website at three viewports: 375px, 768px, and 1440px. Document what breaks or changes at each. Write 3–5 sentences describing the layout differences and any issues you found.

# Integration

**Connecting to Sciences — The Long Tail Distribution**

Screen sizes do not cluster neatly around two or three values. They form a long-tail distribution: many screens at common sizes (375px iPhone, 1920px desktop), but also a long tail of unusual sizes. No single layout works for every tail — responsive design handles this by specifying layouts at key breakpoints and allowing content to flow naturally between them. This is equivalent to designing a system that handles common cases efficiently and edge cases gracefully — a principle in both statistics and software engineering.

# Lore Conclusion

*"A page that only works on one screen,"* says Master Aelindra, *"is a scroll that can only be read by one person. The web's power is universality. Build for the many. Test on the small. The small screen reveals every assumption."*

---
