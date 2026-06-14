---
id: fe-jun-m8-11
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m8
moduleTitle: "Module 8: Tailwind CSS"
moduleGlyph: "🎨"
moduleSortOrder: 8
topicSlug: theme_customisation
topicTitle: "Theme Customisation"
topicSortOrder: 4
lesson: design_tokens
title: "Design Tokens in Tailwind"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m8-10]
integrationDomains: [design, software_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines what a design token is (a named design decision)"
    - "Maps at least two design token types (colour, spacing, typography) to Tailwind config"
    - "Explains how semantic token naming (e.g. 'surface', 'primary') improves maintainability"
    - "Describes one benefit of having tokens as a single source of truth"
  keywords: [token, design, semantic, colour, spacing, config, brand, consistent, single-source]
  modelAnswer: |
    A design token is a named design decision — a colour, size, font, or space value that has a semantic meaning beyond its raw value. In Tailwind, tokens live in tailwind.config.js as named values under theme.extend. Instead of using `bg-indigo-600` directly, you define `colors: { primary: '#4f46e5' }` and use `bg-primary`. Semantic naming means the name describes the role ('primary', 'surface', 'muted') rather than the appearance ('blue-600'), so changing the primary colour from indigo to teal only requires one config change. This is the single source of truth principle applied to design.
guidedSteps:
  - id: fe-jun-m8-11-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A designer uses the colour #4f46e5 for all primary buttons, active states, and links throughout the app. It is currently referenced as `bg-indigo-600` in 45 components. What is the best improvement?"
    inputConfig:
      options:
        - "Add `primary: '#4f46e5'` to theme.extend.colors and replace all bg-indigo-600 with bg-primary"
        - "Leave it as bg-indigo-600 — it's consistent enough"
        - "Use an arbitrary value [#4f46e5] everywhere instead"
        - "Create a CSS variable and reference it as var(--primary)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Add `primary: '#4f46e5'` to theme.extend.colors and replace all bg-indigo-600 with bg-primary"]
      rejectedFeedback: "A named token (`primary`) gives the colour semantic meaning and creates a single place to change it. If the brand colour ever changes from indigo to teal, you update one line in the config."
    hint: "Think about what happens when the designer decides to change the primary colour next month."
    reflectionPrompt: "Why is `bg-primary` more resilient to brand changes than `bg-indigo-600`?"
  - id: fe-jun-m8-11-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Define a semantic colour palette in tailwind.config.js with tokens: `primary`, `surface`, `muted-text`, and `border`. Explain what each represents."
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [primary, surface, muted, border, colors, extend]
      rejectedFeedback: "Create named tokens under theme.extend.colors: primary for brand actions, surface for card/panel backgrounds, muted for secondary text, border for dividers."
    hint: "Semantic names describe purpose ('primary action colour') not appearance ('blue')."
    reflectionPrompt: "How would you use these tokens in a dark mode setup where each token needs a light and dark value?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the key benefit of semantic token names like 'primary' vs 'indigo-600'?"
    options:
      - "Changing the brand colour requires only one config update rather than finding and replacing every usage"
      - "Semantic names generate smaller CSS files"
      - "Indigo-600 is not a valid Tailwind class"
      - "Semantic names automatically adapt to dark mode"
    correctIndex: 0
    feedback: "Semantic naming decouples the token's identity from its current value. `bg-primary` will always be 'the primary action colour' — even if that colour changes from indigo to teal next quarter."
retrieval:
  recall: "What is a design token, and how does it differ from a raw colour value like #4f46e5?"
  explain: "How do design tokens improve the collaboration between designers and engineers?"
  mistakeId:
    code: |
      // theme.extend.colors
      colors: {
        'button-background': '#4f46e5',
        'button-hover': '#4338ca',
        'link-colour': '#4f46e5',
        'active-state': '#4f46e5',
      }
    answer: "These tokens describe implementation ('button-background') rather than semantics ('primary'). Three tokens all have the same value — they should be `primary` with one definition. Component-specific tokens like 'button-background' belong in component code, not the design token layer. Use semantic tokens: `primary`, `primary-dark`. Components decide how to apply them."
---

# Hook

The Academy's interface has forty-seven different shades of blue scattered across its codebase — some from hex values, some from `bg-blue-500`, some from `bg-indigo-600`. When the Council decrees that the Academy's colour changes to violet, the engineer stares at the codebase in despair. This is what happens without design tokens.

# Lore Introduction

In the Academy's Design Sanctum, the ancient Text of Tokens is displayed: *"Name your decisions, not your values. For values change, but decisions endure."* A design token is a decision — 'this is the primary action colour' — expressed as a named constant. Master Tokenwise says: "It is not that the colour is indigo. It is that the primary colour is indigo today. Tomorrow it may be violet. The token survives the change; the raw value does not."

# Core Learning

## Concept Introduction

A **design token** is a named design decision stored in a central location.

**Raw values (brittle):**
```jsx
<button className="bg-indigo-600 hover:bg-indigo-700">Action</button>
// vs
<button style={{ background: '#4f46e5' }}>Action</button>
```

**Design tokens (resilient):**
```js
// tailwind.config.js
theme: {
  extend: {
    colors: {
      // Semantic tokens — describe purpose, not value
      primary: {
        DEFAULT: '#4f46e5',
        hover: '#4338ca',
        light: '#e0e7ff',
      },
      surface: {
        DEFAULT: '#ffffff',
        subtle: '#f9fafb',
        muted: '#f3f4f6',
      },
      content: {
        DEFAULT: '#111827',
        muted: '#6b7280',
        inverted: '#ffffff',
      },
    },
    fontFamily: {
      body: ['Inter', 'sans-serif'],
      display: ['Cinzel', 'serif'],
      mono: ['JetBrains Mono', 'monospace'],
    },
    spacing: {
      'content-padding': '1.5rem',  // consistent page padding
      'section-gap': '4rem',         // between sections
    }
  }
}
```

Usage:
```jsx
<button className="bg-primary hover:bg-primary-hover text-content-inverted">
  Primary Action
</button>

<div className="bg-surface-subtle text-content-muted">
  Secondary content
</div>
```

## Why It Matters

**Single source of truth:** The brand colour lives in one place. Change it once; every component updates.

**Designer-developer language:** Designers name tokens in Figma; engineers use those same names in code. `primary`, `surface`, `muted` are shared vocabulary.

**Dark mode support:** Tokens abstract the colour so dark mode can substitute different values:
```jsx
// Token approach + dark mode:
<div className="bg-surface dark:bg-gray-900 text-content dark:text-gray-100">
```

## Worked Example

```js
// A complete token system for a small app
const tokens = {
  colors: {
    // Brand
    primary: { DEFAULT: '#7c3aed', hover: '#6d28d9', light: '#ede9fe' },
    // Backgrounds
    surface: { DEFAULT: '#ffffff', subtle: '#fafafa', card: '#f5f5f5' },
    // Text
    ink: { DEFAULT: '#1f2937', muted: '#6b7280', hint: '#9ca3af' },
    // Status
    success: { DEFAULT: '#059669', light: '#d1fae5' },
    warning: { DEFAULT: '#d97706', light: '#fef3c7' },
    danger:  { DEFAULT: '#dc2626', light: '#fee2e2' },
    // Border
    border: { DEFAULT: '#e5e7eb', strong: '#d1d5db' },
  }
};
```

## Common Mistakes

- **Using component-specific token names** (`button-background`) — tokens should be semantic and reusable, not tied to a specific component.
- **Mixing raw values and tokens** — if you use `bg-primary` in some places and `bg-indigo-600` in others for the same colour, you lose the single-source benefit.
- **Creating too many tokens** — you don't need a token for every possible colour. Start with the essentials: primary, surface, text, border, and status colours.

## Mini Summary

Design tokens are named design decisions in `tailwind.config.js`. Semantic names (`primary`, `surface`, `muted`) describe role rather than value. They create a single source of truth, enable easy brand changes, and establish shared vocabulary between designers and engineers.

# Guided Practice Quest

Work through the guided steps to practise distinguishing semantic tokens from component-specific names and creating a minimal token system.

# Solo Practice Quest

Create a token set for a fictional app with a primary action colour, two surface levels (default and subtle), two text levels (default and muted), and a danger colour. Use each token in a styled `<Alert>` component.

# Integration

**Design:** Figma's variable system and design tokens are directly analogous. Modern teams export Figma tokens and import them into tailwind.config.js using tools like Style Dictionary, making the design-to-code pipeline nearly automatic.

**Software Engineering:** Design tokens are the same pattern as configuration constants in application code — named values that encode intent, centralised for easy change. The principle is identical: avoid magic numbers; name your decisions.

# Lore Conclusion

The Council decrees the colour change to violet. The engineer opens tailwind.config.js, changes one hex value under `primary`, and refreshes the browser. Every button, every link, every active state shifts to violet simultaneously. The engineer looks at the Tokens text carved in stone. *"Name your decisions, not your values."* Now they understand.

---
