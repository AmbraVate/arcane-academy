---
id: fe-jun-m8-09
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
lesson: dark_mode
title: "Dark Mode"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m8-07, fe-jun-m8-08]
integrationDomains: [design, ux]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the dark: prefix and how it applies styles in dark mode"
    - "Describes the two dark mode strategies (media query vs class)"
    - "Implements a manual toggle by adding/removing the 'dark' class on <html>"
    - "Explains a real UX benefit of dark mode"
  keywords: [dark, dark:, media, class, toggle, prefers-color-scheme, html, strategy]
  modelAnswer: |
    Tailwind's `dark:` prefix applies a style only in dark mode. There are two strategies: `media` mode uses the OS `prefers-color-scheme` media query automatically; `class` mode applies dark styles when the `dark` class is present on the `<html>` element, allowing manual toggle. For user-controlled dark mode, use `class` strategy: add `darkMode: 'class'` to tailwind.config.js, then toggle `document.documentElement.classList.toggle('dark')` in React. Dark mode benefits include reduced eye strain in low-light environments and preference accommodation.
guidedSteps:
  - id: fe-jun-m8-09-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You want to let users manually toggle dark mode. Which tailwind.config.js setting enables this?"
    inputConfig:
      options:
        - "darkMode: 'class'"
        - "darkMode: 'media'"
        - "darkMode: 'manual'"
        - "darkMode: true"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["darkMode: 'class'"]
      rejectedFeedback: "`darkMode: 'class'` makes Tailwind apply dark: styles when the `dark` class is on the html element. You control when to add/remove it. `media` uses OS preference automatically with no manual control."
    hint: "Class strategy = you control it. Media strategy = OS controls it."
    reflectionPrompt: "Which strategy would you pick if you wanted to respect the OS preference AND allow manual override?"
  - id: fe-jun-m8-09-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write a React button that toggles dark mode by adding/removing the 'dark' class on the html element."
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [documentElement, classList, toggle, dark, onClick]
      rejectedFeedback: "The toggle should call `document.documentElement.classList.toggle('dark')` in an onClick handler."
    hint: "document.documentElement is the <html> element. classList.toggle adds if absent, removes if present."
    reflectionPrompt: "How would you persist the user's preference across page reloads?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In a component with `bg-white dark:bg-gray-900 text-gray-900 dark:text-white`, what happens when dark mode is active?"
    options:
      - "Background becomes gray-900 and text becomes white"
      - "Background becomes white and text becomes gray-900"
      - "Both dark: and light classes apply simultaneously"
      - "The dark: classes only apply if the user has a dark OS theme"
    correctIndex: 0
    feedback: "When dark mode is active (via class or media), `dark:` classes override their counterparts. The component inverts: dark background, light text."
retrieval:
  recall: "What are the two Tailwind dark mode strategies and when would you use each?"
  explain: "How does the dark: prefix work in combination with other prefixes like responsive breakpoints?"
  mistakeId:
    code: |
      // In tailwind.config.js:
      module.exports = {
        darkMode: 'media',  // uses OS preference
        // ...
      }

      // In the app, a toggle button:
      function DarkModeToggle() {
        const [dark, setDark] = useState(false);
        return (
          <button onClick={() => {
            setDark(!dark);
            document.documentElement.classList.toggle('dark');
          }}>
            Toggle
          </button>
        );
      }
    answer: "Using `darkMode: 'media'` means Tailwind ignores the `dark` CSS class — it only responds to the OS `prefers-color-scheme` media query. The class toggle has no effect. Change to `darkMode: 'class'` in the config to make the manual toggle work."
---

# Hook

An apprentice stays up late studying scrolls. The Academy portal blazes white light in a darkened study chamber. Their eyes ache. If only the interface could sense the hour and shift to a darker palette... This is exactly what Tailwind's dark mode utilities provide — and they can be triggered automatically or by user preference.

# Lore Introduction

Master Umbra, who prefers to teach in candlelight, introduces dark mode: "Light mode is the sun — bright, energising, suited for daytime reading. Dark mode is the moon — gentle, low-contrast, kind to the eyes in dim environments. A complete interface respects both states. Tailwind makes switching between them as simple as adding a prefix."

# Core Learning

## Concept Introduction

Tailwind's `dark:` prefix applies a style when dark mode is active.

```jsx
// Light: white bg, dark text. Dark mode: inverted.
<div className="bg-white dark:bg-gray-900 text-gray-900 dark:text-white">
  Content
</div>
```

**Two dark mode strategies:**

**1. Media strategy (default):**
```js
// tailwind.config.js
module.exports = {
  darkMode: 'media', // uses OS prefers-color-scheme
}
```
Dark styles activate automatically when the user's OS is in dark mode. No code needed. No user control.

**2. Class strategy (manual toggle):**
```js
// tailwind.config.js
module.exports = {
  darkMode: 'class',
}
```
Dark styles activate when the `dark` class is present on the `<html>` element. You control when to add/remove it.

## Why It Matters

Dark mode reduces eye strain in low-light environments, is preferred by many developers, and is increasingly expected in modern apps. Tailwind makes dark styling as simple as any other prefix — no separate CSS files, no duplicate rules.

## Worked Example

```jsx
// tailwind.config.js
module.exports = {
  darkMode: 'class',
  content: ['./src/**/*.{js,ts,jsx,tsx}'],
  theme: { extend: {} },
  plugins: [],
};

// hooks/useDarkMode.ts
import { useState, useEffect } from 'react';

export function useDarkMode() {
  const [isDark, setIsDark] = useState(() => {
    // Check localStorage on init
    if (typeof window !== 'undefined') {
      return localStorage.getItem('theme') === 'dark' ||
        (!localStorage.getItem('theme') && window.matchMedia('(prefers-color-scheme: dark)').matches);
    }
    return false;
  });

  useEffect(() => {
    const root = document.documentElement;
    if (isDark) {
      root.classList.add('dark');
      localStorage.setItem('theme', 'dark');
    } else {
      root.classList.remove('dark');
      localStorage.setItem('theme', 'light');
    }
  }, [isDark]);

  return { isDark, toggle: () => setIsDark(prev => !prev) };
}

// DarkModeToggle.tsx
function DarkModeToggle() {
  const { isDark, toggle } = useDarkMode();
  return (
    <button
      onClick={toggle}
      className="p-2 rounded-lg bg-gray-100 dark:bg-gray-800 text-gray-700 dark:text-gray-300 hover:bg-gray-200 dark:hover:bg-gray-700 transition-colors"
      aria-label="Toggle dark mode"
    >
      {isDark ? '☀️' : '🌙'}
    </button>
  );
}

// A card styled for both modes
function ContentCard({ title, body }) {
  return (
    <div className="bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-xl p-6 shadow">
      <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-2">{title}</h3>
      <p className="text-gray-600 dark:text-gray-400">{body}</p>
    </div>
  );
}
```

## Common Mistakes

- **Using `darkMode: 'media'` with a class toggle** — the class has no effect with the media strategy.
- **Forgetting dark variants on text** — it's easy to remember `dark:bg-*` but forget `dark:text-*`, resulting in invisible text.
- **Not persisting preference** — without localStorage, the preference resets on page reload.
- **Using colour-agnostic classes** — `text-gray-900` in light mode needs a light counterpart (`dark:text-white`) — it won't flip automatically.

## Mini Summary

Tailwind's `dark:` prefix applies styles in dark mode. Use `darkMode: 'media'` for OS-automatic, or `darkMode: 'class'` for manual user control via toggling the `dark` class on `<html>`. Persist preference in localStorage for a polished experience.

# Guided Practice Quest

Work through the guided steps to practise configuring the class strategy and implementing a toggle.

# Solo Practice Quest

Build a `ThemeToggle` component using `useDarkMode`. Style a `<nav>` that uses `bg-white dark:bg-gray-900` and `text-gray-900 dark:text-white`. Test the toggle.

# Integration

**Design:** Dark mode is not just colour inversion — it requires deliberate design decisions. Shadows that look good on white become invisible on dark backgrounds; elevation is communicated through lighter backgrounds rather than deeper shadows in dark mode.

**UX:** Research suggests dark mode reduces photoreceptor fatigue in low-light environments. Offering user preference control (rather than forcing a mode) respects user autonomy — a core UX principle.

# Lore Conclusion

Master Umbra's students complete their late-night study session. The portal glows softly in dark mode — dark slate panels, muted text, the toggle button a small moon icon in the corner. The apprentice's eyes are grateful. "Light mode for the sun," Master Umbra intones, "dark mode for the moon. Give the user both, and they will choose their comfort."

---
