---
id: fe-app-m3-03
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m3
moduleTitle: "Module 3: CSS Foundations"
moduleGlyph: "🎨"
moduleSortOrder: 3
topicSlug: styling_basics
topicTitle: "Styling Basics"
topicSortOrder: 1
lesson: properties
title: "Properties"
sortOrder: 3
difficulty: 1
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
    - "Names and correctly uses at least 5 CSS properties from different categories"
    - "Explains the difference between shorthand and longhand properties"
    - "Uses relative units (rem, em, %) at least once alongside absolute units (px)"
    - "Correctly formats colour values (hex, rgb, named)"
    - "Explains why rem is often preferred over px for font sizes"
  keywords: [color, font-size, margin, padding, background, border, width, rem, px, shorthand]
  modelAnswer: |
    CSS properties control specific aspects of appearance: color (text colour),
    font-size (text size), margin/padding (space outside/inside an element),
    background-color, border. Shorthand properties (margin: 10px 20px) set multiple
    values at once. rem units are relative to the root font size, making designs
    scale with user accessibility settings; px are fixed regardless of user preferences.
guidedSteps:
  - id: fe-app-m3-03-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the difference between `margin` and `padding`?
    inputConfig:
      options:
        - "Margin is inside the element; padding is outside"
        - "Margin is space outside the element's border; padding is space inside"
        - "They are identical — margin is just the older name"
        - "Margin affects layout; padding only affects background colour"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Margin is space outside the element's border; padding is space inside"]
      rejectedFeedback: "Margin = space between this element and its neighbours (outside the border). Padding = space between the content and the border (inside). This is part of the Box Model — the most fundamental concept in CSS layout."
    hint: "Think: padding is like the stuffing inside a box; margin is the space between boxes on a shelf."
    reflectionPrompt: "Margin and padding are the most used CSS properties in real development. Getting them confused causes layout bugs that can take hours to debug. The mnemonic: Padding is Personal (inside), Margin is social (between elements)."

  - id: fe-app-m3-03-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete this shorthand that sets top/bottom margin to 16px and left/right to 0:

      `margin: ___ ___;`
    inputConfig:
      placeholder: "16px 0"
    markingRule:
      matchMode: CONTAINS
      accepted: ["16px 0", "16px 0px"]
      rejectedFeedback: "margin: 16px 0 sets vertical (top/bottom) to 16px and horizontal (left/right) to 0. This is the two-value shorthand. Four values set top, right, bottom, left individually (clockwise from top)."
    hint: "Two values: first is top/bottom, second is left/right."
    reflectionPrompt: "Shorthands are efficient but can be confusing. margin: 10px 20px 30px 40px sets top=10, right=20, bottom=30, left=40 (clockwise). margin: 10px 20px 30px sets top=10, right/left=20, bottom=30. Know your shorthands."

  - id: fe-app-m3-03-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A user in their browser settings increases the base font size from 16px to 20px for accessibility. Explain what happens to text sized with `font-size: 1rem` vs `font-size: 16px`.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [rem, px, scale, accessibility, root, user, 20px, change]
      rejectedFeedback: "font-size: 1rem scales with the user's setting: at 20px root, 1rem = 20px. font-size: 16px is fixed — the user's preference is ignored. rem respects accessibility settings; px overrides them."
    hint: "rem stands for 'root em' — relative to the root (html) font size."
    reflectionPrompt: "Using px for font sizes is an accessibility anti-pattern. It locks out users who have increased their browser font size for readability — exactly the users who most need that accommodation. Use rem for font sizes by default."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which CSS property controls the colour of text?"
    options:
      - "text-color"
      - "font-color"
      - "color"
      - "foreground"
    correctIndex: 2
    feedback: "The property is simply `color` (American English spelling). font-color and text-color do not exist in CSS — a very common mistake for beginners. background-color controls the background behind the text."
  - type: MULTIPLE_CHOICE
    question: "What does `border: 2px solid #000` do?"
    options:
      - "Sets only the border width"
      - "Sets the border width (2px), style (solid), and colour (#000) in one shorthand"
      - "Creates a 2-pixel margin with a solid background"
      - "This syntax is invalid — three values cannot be in one border declaration"
    correctIndex: 1
    feedback: "border is a shorthand that sets width, style, and colour in one rule. The order is conventional: width style color. 'solid' is the most common style; others include dashed, dotted, double."

retrieval:
  recall: "Write CSS rules that set: text colour to dark grey, font size to 1rem, 16px padding on all sides, a 1px solid grey border."
  explain: "Explain the difference between `font-size: 16px` and `font-size: 1rem` and which you should prefer for accessibility."
  mistakeId:
    code: "font-color: red; — a property that does not exist"
    answer: "The correct property is `color`. CSS uses 'color' for text colour with no prefix. font-color, text-color, and foreground-color are all invalid. This is one of the most common CSS beginner mistakes."
---

# Hook

CSS has hundreds of properties. But the vast majority of real-world CSS uses fewer than 30 of them.

Learning to use a small set of core properties confidently is more valuable than knowing every obscure property superficially. This lesson covers the properties you will use every day.

# Lore Introduction

*"A painter,"* says Master Aelindra, *"does not need every colour in existence. They need to understand the primary pigments — how to mix them, how they behave on canvas, how they interact. The same is true of CSS properties. Master the fundamentals. The rest follows."*

# Core Learning

## Concept Introduction

**Text and typography:**

| Property | Example | Effect |
|---|---|---|
| `color` | `color: #1a1a2e` | Text colour |
| `font-size` | `font-size: 1rem` | Text size |
| `font-weight` | `font-weight: 700` | Bold (100–900) |
| `font-family` | `font-family: Inter, sans-serif` | Typeface |
| `line-height` | `line-height: 1.6` | Space between lines |
| `text-align` | `text-align: center` | Alignment |

**Spacing:**

| Property | Example | Effect |
|---|---|---|
| `margin` | `margin: 16px 0` | Space outside the border |
| `padding` | `padding: 12px 16px` | Space inside the border |

**Visual:**

| Property | Example | Effect |
|---|---|---|
| `background-color` | `background-color: #f0f4f8` | Background |
| `border` | `border: 1px solid #e2e8f0` | Border (shorthand) |
| `border-radius` | `border-radius: 8px` | Rounded corners |
| `opacity` | `opacity: 0.8` | Transparency (0–1) |

**Sizing:**

| Property | Example | Effect |
|---|---|---|
| `width` / `height` | `width: 100%` | Dimensions |
| `max-width` | `max-width: 800px` | Maximum width |

**Units:**
- `px` — fixed pixels
- `rem` — relative to root font size (accessibility-friendly)
- `em` — relative to the current element's font size
- `%` — relative to the parent element
- `vh` / `vw` — relative to the viewport height/width

## Common Mistakes

- `font-color` instead of `color`
- `px` for all font sizes (inaccessible)
- Not using shorthand when appropriate (`margin-top + margin-right + margin-bottom + margin-left` = `margin`)

## Mini Summary

- ✔ `color`, `font-size`, `font-weight`, `font-family` — core text properties
- ✔ `margin` = outside, `padding` = inside
- ✔ Use `rem` for font sizes, `px` for borders and fine details
- ✔ Shorthand properties (border, margin, padding) set multiple values at once

# Guided Practice Quest

**The Property Palette** — identify and apply the right properties. Steps in `guidedSteps`.

# Solo Practice Quest

Style a blog post card: background white, 24px padding, 8px border-radius, 1px border, a title (1.5rem, bold, dark), and body text (1rem, 1.6 line-height, grey). Use rem for all font sizes. Write the CSS and explain one property choice.

# Integration

**Connecting to Sciences — Colour Perception and Contrast**

The CSS `color` and `background-color` properties control contrast — the ratio between foreground and background brightness. Human visual perception is highly sensitive to contrast; the WCAG accessibility standard requires a minimum contrast ratio of 4.5:1 for normal text. At insufficient contrast, readers with low vision or colour deficiency cannot distinguish text from background. CSS properties are not just aesthetic choices — they directly determine whether your content is perceivable by all users.

# Lore Conclusion

*"Thirty properties,"* says Master Aelindra, *"will take you further than three hundred used badly. Learn `color`, `font-size`, `margin`, `padding`, `border`. Understand units. Then build. The rest of the properties will make themselves known when you need them."*

---
