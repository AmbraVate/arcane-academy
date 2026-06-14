---
id: fe-jun-m8-10
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
lesson: tailwind_config
title: "The Tailwind Config"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m8-09]
integrationDomains: [design, software_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the structure of tailwind.config.js (content, theme, plugins)"
    - "Distinguishes between theme.extend and theme (override vs extend)"
    - "Adds a custom colour or font family using theme.extend"
    - "Explains why content paths must be configured correctly for purging to work"
  keywords: [config, theme, extend, content, colour, font, custom, override, purge]
  modelAnswer: |
    tailwind.config.js has three main sections: `content` (file paths for purging), `theme` (full override of defaults), and `theme.extend` (additions to the defaults). Use `extend` for adding custom colours, fonts, and spacing without removing Tailwind's defaults. Example: `theme: { extend: { colors: { brand: '#6366f1' } } }` adds `bg-brand`, `text-brand` etc. The `content` array tells Tailwind which files to scan for class usage — missing paths means classes get purged from production builds.
guidedSteps:
  - id: fe-jun-m8-10-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You want to add a custom brand colour WITHOUT removing Tailwind's default colours. Which config section should you add it to?"
    inputConfig:
      options:
        - "theme.extend.colors"
        - "theme.colors"
        - "plugins"
        - "content"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["theme.extend.colors"]
      rejectedFeedback: "`theme.colors` replaces the entire colours object — you lose all built-in colours. `theme.extend.colors` adds to the existing colours, keeping blue, red, green etc."
    hint: "extend = add to; theme directly = replace."
    reflectionPrompt: "What would happen to `bg-blue-500` if you added your colour to `theme.colors` instead of `theme.extend.colors`?"
  - id: fe-jun-m8-10-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write the tailwind.config.js code to add a custom font family called 'arcane' pointing to ['Cinzel', 'serif']."
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [extend, fontFamily, arcane, Cinzel]
      rejectedFeedback: "Inside theme.extend, add: `fontFamily: { arcane: ['Cinzel', 'serif'] }`. This creates `font-arcane` as a utility."
    hint: "The key in fontFamily becomes the utility suffix: { myFont: [...] } creates font-myFont."
    reflectionPrompt: "After adding font-arcane to the config, how do you use it in JSX?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why must the content array in tailwind.config.js include your component file paths?"
    options:
      - "Tailwind scans these files to detect which classes are used, generating only those in the production build"
      - "Tailwind imports these files and runs them to generate styles"
      - "These paths tell Tailwind where to write the output CSS file"
      - "The content array is only needed for dark mode configuration"
    correctIndex: 0
    feedback: "Tailwind uses static analysis (text scanning) to find classes used in your files. Only those classes are included in the CSS output. Missing a path means those classes get purged even if you use them."
retrieval:
  recall: "What is the difference between theme and theme.extend in tailwind.config.js?"
  explain: "Why would adding a path to the content array that doesn't exist cause problems in a production build?"
  mistakeId:
    code: |
      // tailwind.config.js
      module.exports = {
        content: ['./src/**/*.{js,jsx}'],
        theme: {
          colors: {
            brand: '#6366f1',
          }
        },
      }
    answer: "Using `theme.colors` instead of `theme.extend.colors` replaces ALL Tailwind colours with just `brand`. Classes like `bg-blue-500`, `text-red-600` etc. will stop working. Fix: move to `theme: { extend: { colors: { brand: '#6366f1' } } }`."
---

# Hook

Every Academy guild has its own colours, fonts, and visual identity. The Engineering Guild uses indigo and slate; the Alchemy Guild prefers amber and forest green. Tailwind's default palette is rich, but when a guild's brand demands a specific shade of mystical purple that doesn't exist in the defaults — that is what the Tailwind config is for.

# Lore Introduction

Master Configura tends the Academy's Theme Forge — a workshop where the raw material of Tailwind's defaults is shaped to fit each project's identity. "Tailwind's defaults are generous," she says, "but every project deserves its own voice. The config is where you teach Tailwind your project's visual language without discarding the tools it already provides."

# Core Learning

## Concept Introduction

`tailwind.config.js` (or `.ts`) is the heart of Tailwind customisation.

**Basic structure:**
```js
/** @type {import('tailwindcss').Config} */
module.exports = {
  // 1. Content: which files to scan for class names
  content: [
    './index.html',
    './src/**/*.{js,ts,jsx,tsx}',
  ],

  // 2. Theme: customise the design system
  theme: {
    // theme.extend = ADD to defaults (keep existing utilities)
    extend: {
      colors: {},
      fontFamily: {},
      spacing: {},
      borderRadius: {},
    },
    // theme directly (without extend) = REPLACE defaults
  },

  // 3. Plugins: add Tailwind plugins
  plugins: [],
};
```

**Adding custom colours:**
```js
theme: {
  extend: {
    colors: {
      brand: {
        50:  '#eef2ff',
        100: '#e0e7ff',
        500: '#6366f1',  // primary
        600: '#4f46e5',  // darker for hover
        700: '#4338ca',
        900: '#312e81',
      },
      // Simple single value:
      accent: '#f59e0b',
    }
  }
}
```

This creates `bg-brand-500`, `text-brand-600`, `border-brand-100` etc.

**Adding custom fonts:**
```js
theme: {
  extend: {
    fontFamily: {
      sans: ['Inter', 'system-ui', 'sans-serif'],   // override default sans
      display: ['Cinzel', 'serif'],                   // new utility: font-display
      mono: ['JetBrains Mono', 'monospace'],
    }
  }
}
```

## Why It Matters

The config lets you implement a design system's colour palette, typography scale, and spacing values — ensuring your Tailwind classes always reflect real design decisions rather than arbitrary default choices.

## Worked Example

```js
// tailwind.config.js for Arcane Academy
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './index.html',
    './src/**/*.{js,ts,jsx,tsx}',
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        arcane: {
          50:  '#f5f3ff',
          100: '#ede9fe',
          200: '#ddd6fe',
          500: '#8b5cf6',  // primary purple
          600: '#7c3aed',
          700: '#6d28d9',
          900: '#4c1d95',
        }
      },
      fontFamily: {
        display: ['Cinzel', 'Georgia', 'serif'],
        body: ['Inter', 'system-ui', 'sans-serif'],
      },
      spacing: {
        '18': '4.5rem',   // gap between p-16 and p-20
        '88': '22rem',    // sidebar width
      },
      borderRadius: {
        'xl': '1rem',
        '2xl': '1.5rem',
      }
    }
  },
  plugins: [],
};
```

Usage:
```jsx
<h1 className="font-display text-arcane-700 dark:text-arcane-200 text-4xl">
  Arcane Academy
</h1>
```

## Common Mistakes

- **Using `theme.colors` instead of `theme.extend.colors`** — this replaces ALL default colours.
- **Missing content paths** — if Tailwind can't find a file, its classes get removed from the production build.
- **Adding TypeScript files but listing only `.jsx`** — `./src/**/*.{js,ts,jsx,tsx}` catches all variants.

## Mini Summary

`tailwind.config.js` is structured around `content` (file scanning), `theme.extend` (adding to defaults), and `plugins`. Always use `extend` unless you deliberately want to replace defaults entirely.

# Guided Practice Quest

Work through the guided steps to practise distinguishing `theme` from `theme.extend` and adding custom values.

# Solo Practice Quest

Add a custom colour palette with three shades (light, mid, dark) named `guild`, a custom font called `arcane` pointing to 'Cinzel', and a custom spacing value `18` of `4.5rem`. Then use all three in a styled heading component.

# Integration

**Design:** The Tailwind config is the engineering implementation of a design system's tokens. A designer's colour palette maps directly to `theme.extend.colors`; their type scale maps to `fontFamily` and `fontSize`. This creates a shared language between designers and engineers.

**Software Engineering:** The config is a single source of truth for visual decisions — the same principle as constants and configuration in application code. Centralising values means a brand colour change updates everywhere simultaneously.

# Lore Conclusion

Master Configura shows the apprentices Arcane Academy's config file. Every guild colour, every display font, every spacing quirk — all declared in one place. "When the Council decides to change the Academy's purple," she says, "there is one line to change. One source of truth. That is the mark of a thoughtful artificer."

---
