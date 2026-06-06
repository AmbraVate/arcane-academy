---
id: fe-app-m3-12
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m3
moduleTitle: "Module 3: CSS Foundations"
moduleGlyph: "🎨"
moduleSortOrder: 3
topicSlug: visual_design
topicTitle: "Visual Design"
topicSortOrder: 3
lesson: colour_theory
title: "Colour Theory"
sortOrder: 2
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the difference between RGB, hex, and HSL colour formats"
    - "Describes what contrast ratio means and why 4.5:1 is the WCAG AA minimum"
    - "Applies a primary, secondary, and neutral colour palette in CSS"
    - "Explains how colour conveys meaning (red=error, green=success) and cultural limitations"
    - "Uses CSS custom properties to define a colour system"
  keywords: [colour, hex, rgb, hsl, contrast, WCAG, accessibility, palette, custom-properties, semantic]
  modelAnswer: |
    CSS accepts colours as hex (#7c3aed), RGB (rgb(124,58,237)), or HSL (hsl(262,72%,58%)).
    HSL is most intuitive for design: hue (0-360°), saturation (0-100%), lightness (0-100%).
    WCAG AA requires 4.5:1 contrast ratio between text and background for normal text.
    A colour system uses custom properties (--color-primary, --color-error) so colours
    are defined once and updated everywhere by changing the variable.
guidedSteps:
  - id: fe-app-m3-12-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      WCAG AA accessibility requires a minimum contrast ratio of 4.5:1 for normal text. Why does this matter?
    inputConfig:
      options:
        - "It only matters for users with complete colour blindness"
        - "Low contrast makes text harder to read for many users — including those with low vision, in bright light, or on low-quality screens"
        - "It is a browser requirement — pages fail to render below this ratio"
        - "It only applies to headings, not body text"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Low contrast makes text harder to read for many users — including those with low vision, in bright light, or on low-quality screens"]
      rejectedFeedback: "Contrast requirements aren't just for users with disabilities. Bright sunlight, aging eyes, low-quality displays, and cognitive fatigue all reduce effective contrast. High contrast benefits everyone — it is one of the most impactful accessibility requirements because it affects all users."
    hint: "Think about reading your phone screen on a sunny day."
    reflectionPrompt: "Check any colour combination at WebAIM's Contrast Checker or in browser DevTools. Many professional websites fail the 4.5:1 minimum — light grey text on white background is a common culprit. Always verify contrast before shipping."

  - id: fe-app-m3-12-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete: HSL colour format stands for ___, Saturation, Lightness.
    inputConfig:
      placeholder: "Hue"
    markingRule:
      matchMode: CONTAINS
      accepted: [hue, Hue]
      rejectedFeedback: "HSL = Hue (0-360° colour wheel), Saturation (0-100% intensity), Lightness (0-100% from black to white). HSL is easier to reason about than hex: hsl(262, 72%, 58%) is clearly a medium-bright purple. Changing lightness creates tints and shades of the same colour."
    hint: "H = the angle on the colour wheel (0° = red, 120° = green, 240° = blue)."
    reflectionPrompt: "HSL is the designer-friendly colour format. Creating a colour scale is trivial: keep the same hue and saturation, vary the lightness. hsl(262, 72%, 10%) is near-black purple; hsl(262, 72%, 95%) is near-white purple. Perfect for design systems."

  - id: fe-app-m3-12-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why relying solely on colour (e.g., red=error, green=success) to communicate information is an accessibility problem.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [colour blind, colour deficiency, red-green, accessible, icon, text, shape]
      rejectedFeedback: "About 8% of men have red-green colour deficiency — they cannot distinguish red from green. Colour-only indicators (red border = error, green border = success) are invisible to them. Always pair colour with a second indicator: an icon, text label, or pattern."
    hint: "What proportion of people cannot distinguish red from green?"
    reflectionPrompt: "WCAG 1.4.1: 'Use of Color' — colour must not be the only means of conveying information. A red error border must also have an error message, an icon, or another non-colour indicator. This is not a minority concern: 8% of men × your user base is a significant number of affected users."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which CSS colour format is easiest to create systematic tints and shades from a single base colour?"
    options:
      - "Hex (#7c3aed)"
      - "RGB (rgb(124,58,237))"
      - "HSL (hsl(262, 72%, 58%))"
      - "Named colours (purple)"
    correctIndex: 2
    feedback: "HSL makes systematic colour variations trivial: keep hue and saturation constant, adjust lightness. hsl(262, 72%, 90%) is a light tint; hsl(262, 72%, 20%) is a dark shade. With hex or RGB, creating related shades requires a colour tool."
  - type: MULTIPLE_CHOICE
    question: "CSS custom properties for colours are defined with:"
    options:
      - ":root { $primary: #7c3aed; }"
      - ":root { --color-primary: #7c3aed; }"
      - "html { color-primary: #7c3aed; }"
      - "@vars { primary: #7c3aed; }"
    correctIndex: 1
    feedback: "CSS custom properties (variables) use -- prefix: --color-primary: #7c3aed. Defined on :root, they're available everywhere. Used as: color: var(--color-primary). Changing one :root value updates every element using that variable — the entire site's primary colour changes in one edit."

retrieval:
  recall: "Write CSS custom properties for a 3-colour palette (primary, secondary, danger) and use them in a button rule."
  explain: "Why is HSL often preferred over hex for building a design system's colour scale?"
  mistakeId:
    code: "Using only a red border to indicate a form error — no text or icon"
    answer: "Colour-only indicators fail WCAG 1.4.1. About 8% of men have red-green colour deficiency. Always pair colour with text ('Error: please enter a valid email') and optionally an icon. Never rely on colour as the sole signal."
---

# Hook

Colour is one of the most powerful tools in design. It creates mood, guides attention, communicates status, and conveys meaning — all before a word is read.

But colour used badly excludes users, creates confusion, and undermines trust. A thoughtful colour system uses a small palette consistently, maintains sufficient contrast, and never relies on colour alone to communicate.

> Think of a brand you recognise instantly by colour alone. What emotion does that colour create?

# Lore Introduction

*"Colour in the Academy's sigils,"* says Master Aelindra, *"is never arbitrary. Purple for transformation, gold for achievement, red for urgency. But every sigil also bears a symbol — because some apprentices cannot distinguish red from gold. Colour without symbol fails half the audience."*

# Core Learning

## Concept Introduction

**Colour formats in CSS:**

| Format | Example | Notes |
|---|---|---|
| Named | `red`, `rebeccapurple` | Convenient for prototyping |
| Hex | `#7c3aed` | Most common in production |
| RGB | `rgb(124, 58, 237)` | Useful with transparency: `rgba(124, 58, 237, 0.5)` |
| HSL | `hsl(262, 72%, 58%)` | Most intuitive for systematic design |

**Building a colour system with custom properties:**

```css
:root {
  --color-primary:   hsl(262, 72%, 58%);
  --color-primary-light: hsl(262, 72%, 90%);
  --color-primary-dark:  hsl(262, 72%, 30%);

  --color-success:   hsl(142, 71%, 45%);
  --color-warning:   hsl(38, 92%, 50%);
  --color-error:     hsl(0, 72%, 51%);

  --color-text:      hsl(220, 30%, 10%);
  --color-muted:     hsl(220, 10%, 46%);
  --color-bg:        hsl(220, 20%, 98%);
  --color-border:    hsl(220, 13%, 85%);
}
```

**Contrast requirements (WCAG AA):**
- Normal text (< 18px): minimum **4.5:1**
- Large text (≥ 18px regular or ≥ 14px bold): minimum **3:1**
- UI components and icons: minimum **3:1**

## Why It Matters

Colour consistency across a UI creates cohesion and trust. Insufficient contrast makes text unreadable. Colour-only communication excludes colour-blind users. A well-designed colour system prevents all three problems.

## Common Mistakes

- Light grey text on white background (fails contrast)
- Relying on red/green alone for error/success states
- Too many colours — no visual hierarchy
- Not testing in dark mode

## Mini Summary

- ✔ Use HSL for design systems — easy to create systematic tints/shades
- ✔ Define colours as CSS custom properties — change once, update everywhere
- ✔ WCAG AA: 4.5:1 contrast for normal text
- ✔ Never use colour as the only indicator of meaning (add text or icon)
- ✔ Check contrast with browser DevTools or WebAIM Contrast Checker

# Guided Practice Quest

**The Colour Alchemist** — three questions on CSS colour and accessibility. Steps in `guidedSteps`.

# Solo Practice Quest

Design a 6-colour palette for a fictional product using CSS custom properties: primary, primary-light, text, muted, error, success. Use HSL format. Write the :root declaration and verify your text/background contrast would pass WCAG AA. Explain your hue choices.

# Integration

**Connecting to Psychology — Colour Perception and Cultural Meaning**

Colour perception is partly universal (longer wavelengths = red = warm/urgent) and partly cultural (white = mourning in China; green = luck in Ireland; white = purity in Western culture). Research by Elliot et al. (2007) found that red impairs cognitive performance on tests — a finding relevant to UI error states. The Stroop effect (naming the ink colour of a word written in a conflicting colour slows reaction time) demonstrates that colour and meaning interact in the brain. Understanding these effects helps you use colour purposefully: red for errors and warnings not because it is convention, but because it genuinely activates urgency responses.

# Lore Conclusion

*"Colour is not decoration,"* says Master Aelindra. *"It is a communication channel. Every hue you choose sends a signal. The question is not 'which colour looks nice?' — it is 'which colour communicates what I need to communicate, to all users, in all contexts?' Answer that, and your colour system builds itself."*

---
