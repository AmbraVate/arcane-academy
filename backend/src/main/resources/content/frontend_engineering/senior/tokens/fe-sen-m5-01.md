---
id: fe-sen-m5-01
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m5
moduleTitle: "Module 5: Design Systems"
moduleGlyph: "🎨"
moduleSortOrder: 5
topicSlug: tokens
topicTitle: "Tokens"
topicSortOrder: 1
lesson: design_tokens
title: "Design Tokens"
sortOrder: 1
difficulty: 4
estimatedMinutes: 30
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Correctly defines design tokens and gives examples of each category
    - Explains the difference between primitive tokens and semantic tokens
    - Describes how tokens connect design tools (Figma) to code
    - Explains how CSS custom properties implement design tokens
    - Synthesises why a token-based approach improves design system maintainability
  keywords: [design token, primitive, semantic, alias, colour, spacing, typography, CSS custom property, variable, Figma, single source of truth]
  modelAnswer: |
    Design tokens are named design decisions — the smallest pieces of a design system. Categories include: colour (brand-primary, text-muted, background), spacing (spacing-xs: 4px, spacing-md: 16px), typography (font-size-body, font-weight-bold), border-radius, shadow, animation duration.

    Primitive tokens are raw values: color-blue-500: #3B82F6. Semantic tokens reference primitives with meaningful names: color-brand-primary: {color-blue-500}, color-interactive-default: {color-blue-500}. Semantic tokens are what components use — they describe purpose, not value. When the brand colour changes from blue to purple, only the primitive token changes; all semantic tokens update automatically.

    CSS custom properties (variables) implement tokens in code: :root { --color-brand-primary: #3B82F6; }. Components reference semantic tokens: background-color: var(--color-brand-primary). In Tailwind, tokens map to the theme config.

    The key benefit: a single source of truth. Change a token, change it everywhere. Without tokens, updating the brand colour means finding every hardcoded hex value across 50 files.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "Your brand's primary colour changes from blue to purple. You use design tokens. How many places in the codebase need to change?"
    options:
      - "Every component that uses the primary colour — potentially hundreds"
      - "One: the primitive token definition. All semantic tokens and components update automatically"
      - "The CSS file for each page"
      - "Every Tailwind class that references the colour"
    correctIndex: 1
    feedback: "With primitive and semantic tokens: change color-brand-blue: #3B82F6 → color-brand-purple: #7C3AED, and update the primitive alias. All semantic tokens (brand-primary, interactive-default, etc.) that reference the primitive update automatically. All components that use the semantic tokens update. One change cascades through the entire system."
  - type: SHORT_TEXT
    prompt: "Explain the difference between a primitive token (`color-blue-500: #3B82F6`) and a semantic token (`color-brand-primary: {color-blue-500}`). Why should components use semantic tokens, not primitive tokens?"
    hint: "What does each token name communicate about purpose vs value?"
  - type: FILL_BLANK
    prompt: "Semantic tokens describe ___, not specific values. Changing a primitive token updates all ___ that reference it."
    answer: "purpose/intent; semantic tokens (and components)"
    hint: "Semantic = meaningful name. Primitive = raw value."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A component uses `color: var(--color-blue-500)` instead of `color: var(--color-text-primary)`. What is the problem?"
    options:
      - "CSS custom properties can't reference colour values"
      - "Using the primitive token couples the component to the specific shade of blue, not to the semantic meaning 'primary text'"
      - "blue-500 is not a valid token name"
      - "Components should use inline hex values instead of variables"
    correctIndex: 1
    feedback: "Referencing a primitive token in a component creates a coupling to a specific colour. When the primary text colour changes from blue to almost-black (#1A1A2E), the component still shows blue because it references the primitive. Semantic tokens (--color-text-primary) describe what the colour is for — components should reference purpose, not value."
  - type: MULTIPLE_CHOICE
    question: "Design tokens are called a 'single source of truth'. What does this mean in practice?"
    options:
      - "Design tokens must be stored in a single file"
      - "Each design decision (colour, spacing, font) has one canonical definition that all components reference"
      - "The designer and engineer must agree on exactly one token per design decision"
      - "Only one developer can edit the token file at a time"
    correctIndex: 1
    feedback: "Single source of truth means a design decision exists in one place. The primary button colour is defined once (a token), referenced everywhere. Without tokens, the same colour might be hardcoded in 47 places — updating one doesn't update the others. The token is the canonical definition; everything else references it."
retrieval:
  recall: "Name five categories of design tokens and give an example token name for each."
  explain: "Why do design systems use two levels of tokens (primitive and semantic) rather than just one?"
  mistakeId:
    code: |
      /* Component CSS — referencing hardcoded values */
      .button-primary {
        background-color: #3B82F6;
        color: #FFFFFF;
        padding: 8px 16px;
        border-radius: 4px;
        font-size: 14px;
      }
    answer: "Every value is hardcoded. When the design system updates the primary colour, border radius, or base font size, this component doesn't update. The developer must find and change each component manually. With design tokens: background-color: var(--color-brand-primary); padding: var(--spacing-sm) var(--spacing-md); border-radius: var(--radius-md); font-size: var(--font-size-sm). Now the component inherits all design system updates automatically."
---

# Hook

Your company rebrands. The primary colour changes from blue to teal. The spacing scale adds a new size. The designer sends a Figma file. You spend two weeks updating hardcoded hex values and pixel values across 300 components.

A design token system means this update is one file change that propagates everywhere.

# Lore Introduction

*"The Academy does not write 'azure-blue' in every illuminated manuscript,"* the Head Illuminator explains. *"We define the colour once — in the pigment register — as 'Academy Azure.' Every illuminator references the register. When the pigment changes, we update the register. Every manuscript updates."*

She holds up the pigment register. *"This is your design token system."*

# Core Learning

## Concept Introduction

**Design tokens** are named design decisions stored as key-value pairs.

**Token categories:**
```json
{
  "color": {
    "primitive": {
      "blue-500": "#3B82F6",
      "blue-600": "#2563EB",
      "neutral-900": "#111827"
    },
    "semantic": {
      "brand-primary": "{color.primitive.blue-600}",
      "text-primary": "{color.primitive.neutral-900}",
      "interactive-hover": "{color.primitive.blue-600}"
    }
  },
  "spacing": {
    "xs": "4px",
    "sm": "8px",
    "md": "16px",
    "lg": "24px",
    "xl": "32px"
  },
  "typography": {
    "font-size-sm": "14px",
    "font-size-md": "16px",
    "font-weight-regular": "400",
    "font-weight-bold": "700"
  }
}
```

**In CSS (custom properties):**
```css
:root {
  --color-brand-primary: #2563EB;
  --color-text-primary: #111827;
  --spacing-md: 16px;
  --font-size-md: 16px;
  --radius-md: 6px;
}

.button {
  background: var(--color-brand-primary);
  padding: var(--spacing-sm) var(--spacing-md);
  font-size: var(--font-size-md);
  border-radius: var(--radius-md);
}
```

**In Tailwind config:**
```js
// tailwind.config.js
theme: {
  extend: {
    colors: {
      brand: { primary: 'var(--color-brand-primary)' }
    }
  }
}
```

**Design-to-code connection:** Tools like Style Dictionary, Token Studio, and Figma Variables convert design tokens from a design tool into CSS custom properties, Tailwind config, or JavaScript objects.

## Common Mistakes

- **Using primitive tokens in components.** Components should reference semantic tokens (purpose), not primitive tokens (specific value).
- **Inconsistent naming.** Token names should be predictable: `[category]-[variant]-[state]`. Not: `blue1`, `myBlue`, `headerColor`.
- **Too many tokens.** A token for every possible shade creates maintenance overhead. Define what you use; add new tokens deliberately.
- **Not connecting to Figma.** If tokens exist only in code, designers use different values — the disconnect reappears.

## Mini Summary

- ✔ Design tokens are named design decisions: colour, spacing, typography, border-radius, shadow
- ✔ Primitive tokens are raw values; semantic tokens describe purpose (reference primitives)
- ✔ Components use semantic tokens — purpose-driven, not value-driven
- ✔ CSS custom properties implement tokens in code
- ✔ Single source of truth: change a token, change it everywhere

# Guided Practice Quest

Work through the guided steps to verify you can explain the cascade from brand change → primitive token → semantic token → component.

# Solo Practice Quest

Design a token system for a fintech app with brand colours (green), neutral greys, a spacing scale, and typography sizes. Define: 5 primitive colour tokens, 5 semantic colour tokens (with meaningful names), 5 spacing tokens, and 3 typography tokens. Explain your semantic token naming strategy — what principles guide the names?

# Integration

**Design — Atomic Design and the Token Layer**

Brad Frost's Atomic Design hierarchy (atoms, molecules, organisms, templates, pages) pairs with a token layer beneath atoms. Tokens are the sub-atomic layer — the raw decisions that atoms are made from. A button atom uses spacing tokens for padding, colour tokens for background, and typography tokens for font. When tokens change, atoms change, and through them, everything that contains those atoms. This is the design system equivalent of changing a physics constant — it propagates through everything. Tokens give design systems their fractal quality: consistent at every level because consistent decisions flow down from the most fundamental level.

# Lore Conclusion

*"The register is complete,"* the Head Illuminator says. *"Academy Azure is defined once. Every illuminator who needs it references the register. The Academy can change its colours without finding every blue stroke in every manuscript."*

---
