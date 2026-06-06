---
id: fe-app-m6-04
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m6
moduleTitle: "Module 6: Accessibility Foundations"
moduleGlyph: "♿"
moduleSortOrder: 6
topicSlug: practical_accessibility
topicTitle: "Practical Accessibility"
topicSortOrder: 2
lesson: keyboard_navigation
title: "Keyboard Navigation"
sortOrder: 1
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m6-03]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains who relies on keyboard navigation and why"
    - "Demonstrates that Tab moves focus between interactive elements"
    - "Explains what focus styles are and why they must not be removed"
    - "Describes the tabindex attribute and when to use 0 vs -1"
    - "Identifies at least one keyboard navigation anti-pattern"
  keywords: [keyboard, focus, tab, tabindex, focus styles, interactive, WCAG, accessible]
  modelAnswer: |
    Keyboard navigation is essential for users with motor disabilities, power users, and
    anyone who cannot use a mouse. Tab moves focus through interactive elements in DOM
    order. Focus styles (the visible outline on focused elements) must never be removed
    without replacement — they show keyboard users where they are on the page. tabindex="0"
    makes a custom element keyboard-focusable; tabindex="-1" allows programmatic focus
    without entering the tab order.
guidedSteps:
  - id: a11y-kb-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the most critical reason NOT to use `outline: none` on focused elements?
    inputConfig:
      options:
        - "It makes the page look cluttered"
        - "It removes the visual indicator that shows keyboard users where focus is"
        - "It is not supported in all browsers"
        - "It slows down page rendering"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It removes the visual indicator that shows keyboard users where focus is"]
      rejectedFeedback: "The focus outline is the keyboard user's cursor — it shows them which element they are currently on. Removing it without a replacement makes keyboard navigation impossible or severely degraded."
    hint: "For a keyboard user, the focus outline is what a mouse cursor is for a mouse user."
    reflectionPrompt: "Exactly. outline: none is one of the most damaging accessibility mistakes — it erases keyboard users' ability to track their position on the page. Replace it with a custom style, but never remove it entirely."

  - id: a11y-kb-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To make a custom `<div>` button keyboard-focusable, set its tabindex to ___.
    inputConfig:
      placeholder: "number"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["0", 0]
      rejectedFeedback: "tabindex='0' adds an element to the natural tab order. Use it on custom interactive elements (divs styled as buttons). tabindex='-1' allows programmatic focus but excludes from tab order."
    hint: "Which tabindex value puts an element into the natural keyboard tab order?"
    reflectionPrompt: "Correct. But remember: if you make a div keyboard-focusable, you must also handle keyboard events (Enter/Space) for it — simply adding tabindex is not enough for true keyboard accessibility."

  - id: a11y-kb-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why building interactive features from semantic HTML elements
      (`<button>`, `<a>`, `<input>`) is better for keyboard accessibility than
      using `<div>` elements styled to look interactive.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [focusable, keyboard, role, built-in, native, semantic, tab, Enter]
      rejectedFeedback: "Native HTML interactive elements are focusable and keyboard-operable by default. They also expose their role to assistive technologies. A styled div has none of this — you must implement all of it manually."
    hint: "What do button and a elements give you for free that a div does not?"
    reflectionPrompt: "Correct. Semantic HTML gives you keyboard access, role semantics, and assistive technology support for free. Never use a div when a button or a is the right element."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In what order does Tab move focus through a page's interactive elements?"
    options:
      - "Alphabetical order of element IDs"
      - "The order they appear in the DOM (default) or as modified by tabindex"
      - "From bottom to top of the page"
      - "Randomly — it depends on the browser"
    correctIndex: 1
    feedback: "By default, Tab follows the DOM order. tabindex values > 0 can modify this order (but should be avoided — they make tab order unpredictable). tabindex='0' participates in natural DOM order."
  - type: MULTIPLE_CHOICE
    question: "Which keyboard key activates a focused button or link?"
    options:
      - "Tab"
      - "Escape"
      - "Enter (for links and buttons) or Space (for buttons)"
      - "Arrow keys"
    correctIndex: 2
    feedback: "Enter activates both links and buttons. Space activates buttons (but not links). Arrow keys navigate within components like radio button groups and menus."

retrieval:
  recall: "Describe the keyboard flow for a user completing a login form without a mouse."
  explain: "Explain why using tabindex values greater than 0 is an anti-pattern."
  mistakeId:
    code: "<div onclick='submitForm()' style='...'>Submit</div>"
    answer: "A div is not keyboard-focusable and has no semantic role. Keyboard users cannot trigger it. Screen readers announce it as generic content. Fix: use <button type='submit'>Submit</button> — it is focusable, keyboard-operable, and carries the correct button role."
---

# Hook

Remove your mouse. Now try to use most websites.

Tab, Tab, Tab... where is the focus? Is there a focus style? Can you get to the button? Can you activate it with the keyboard? For millions of users — those with motor disabilities, those using keyboard-only setups, those who simply prefer not to reach for a mouse — this is every visit to every website.

Keyboard accessibility is not a niche requirement. It is a fundamental property of a usable interface.

> Try navigating this page using only the Tab key and Enter. What happens to the visible focus indicator?

# Lore Introduction

Master Aelindra sets a mouse aside and places her hands over the keyboard.

*"Not every scribe carries a wand,"* she says. *"Some command the interface through keystrokes alone — by choice, by need, or by circumstance. Your enchantment must respond to both instruments equally. If it only responds to the wand, half the scribes cannot use it."*

She presses Tab once. Nothing visible changes.

*"No focus outline. Nowhere to begin. The page is dark for keyboard scribes — and this is not an edge case. It is a failure."*

# Core Learning

## Concept Introduction

**Keyboard navigation** means all interactive features on a page can be accessed and operated using the keyboard alone — no mouse required.

### Key Concepts

| Concept | Meaning |
|---------|---------|
| **Focus** | The currently active interactive element |
| **Tab** | Moves focus to the next interactive element |
| **Shift+Tab** | Moves focus to the previous element |
| **Enter** | Activates a focused link or button |
| **Space** | Activates a focused button |
| **Arrow keys** | Navigate within components (menus, radio groups, sliders) |
| **Escape** | Closes modals, dropdowns, tooltips |

### Focus Styles

The visible outline around a focused element is the keyboard user's "cursor". It must:
- Always be visible when an element has focus
- Have sufficient contrast (WCAG 2.4.7 — Level AA; 2.4.11 — Level AA in WCAG 2.2)
- Be replaced, never removed

```css
/* Wrong — destroys keyboard navigation */
:focus { outline: none; }

/* Right — custom but clearly visible */
:focus-visible {
  outline: 3px solid #6c63ff;
  outline-offset: 2px;
}
```

### tabindex

```html
<!-- Makes a non-interactive element keyboard-focusable (in natural tab order) -->
<div tabindex="0" role="button">Click me</div>

<!-- Allows programmatic focus (JS: el.focus()) without entering tab order -->
<div tabindex="-1">Modal heading</div>

<!-- Avoid — disrupts natural tab order -->
<input tabindex="5">
```

## Why It Matters

Keyboard accessibility serves users with motor disabilities, users who cannot use a pointing device, power users and developers who prefer keyboard workflows, and users of screen readers (which are keyboard-driven). It is required by WCAG 2.1 AA (Success Criterion 2.1.1: Keyboard).

## Worked Examples

**Example 1 — Correct focus style**

```css
/* Use :focus-visible to avoid outline on mouse click */
button:focus-visible {
  outline: 3px solid #005fcc;
  outline-offset: 3px;
  border-radius: 3px;
}
```

**Example 2 — Making a custom interactive element accessible**

```html
<!-- A styled div acting as a button — requires role and keyboard handler -->
<div 
  role="button" 
  tabindex="0" 
  aria-label="Close dialog"
  onkeydown="if(e.key==='Enter'||e.key===' ') closeDialog();"
  onclick="closeDialog()">
  ✕
</div>

<!-- Better — use a real button -->
<button type="button" aria-label="Close dialog" onclick="closeDialog()">✕</button>
```

**Example 3 — Skip navigation link**

```html
<!-- Allows keyboard users to skip repetitive navigation -->
<a href="#main-content" class="skip-link">Skip to main content</a>
<nav>...</nav>
<main id="main-content">...</main>
```

```css
.skip-link {
  position: absolute;
  top: -40px;
}
.skip-link:focus {
  top: 0; /* becomes visible on focus */
}
```

## Common Mistakes

- Using `outline: none` without providing a replacement focus style
- Building interactive components from `<div>` elements instead of semantic HTML
- Using `tabindex` values greater than 0 (disrupts natural tab order)
- Not handling keyboard events (Enter/Space) on custom interactive elements
- Trapping focus inside a component without a way to escape

## Mental Model

Tab navigation is like following a **trail of stepping stones** across a river.

Each stone is an interactive element. The focus outline is the glow that shows which stone you're standing on. If someone removed the glow — or hid some of the stones — you could fall in.

Keyboard accessibility means: make sure every stone is visible, lit up when you're on it, and leads logically to the next.

## Mini Summary

- All interactive elements must be reachable and operable by keyboard
- Tab moves forward; Shift+Tab moves backward through focusable elements
- Focus styles (outlines) must always be visible — never `outline: none` without replacement
- Use `:focus-visible` to show focus styles only for keyboard users
- Use semantic HTML elements — `<button>`, `<a>`, `<input>` — they are keyboard-accessible by default
- `tabindex="0"` makes custom elements keyboard-focusable; avoid positive tabindex values

# Guided Practice Quest

In this quest you will identify the consequence of removing focus styles, determine the correct tabindex value, and explain why semantic HTML beats styled divs for keyboard accessibility.

These three steps give you the practical rules needed to audit and fix keyboard accessibility issues.

# Solo Practice Quest

Open your browser and navigate a website using only the keyboard (Tab, Shift+Tab, Enter, Space, Escape).

Write 4–6 sentences covering:
- Was the focus indicator always visible? If not, where did it disappear?
- Were you able to reach all interactive elements (buttons, links, form fields)?
- Was there a skip navigation link?
- What was the most frustrating part of the keyboard-only experience?

# Integration

**Connecting to Psychology — Spatial Memory and Wayfinding**

Sighted mouse users navigate visually — scanning the page and clicking what they see. Keyboard users navigate sequentially — they build a mental map of the tab order and use it to predict where they are.

Cognitive psychology research on *wayfinding* shows that humans build spatial mental models of environments — physical and digital. A consistent, predictable navigation structure reduces cognitive load: the user knows where they are and how to get where they want to go.

Keyboard navigation that follows the visual layout (top to bottom, left to right) supports this mental model. Unpredictable tab order — or elements that appear visually but are skipped by Tab — violates the mental model and forces the user to rebuild it constantly.

# Lore Conclusion

Master Aelindra presses Tab once more — and this time, a clear blue outline appears around the first button.

*"Better,"* she says. *"Now the keyboard scribe knows where they stand. Now they can move forward with confidence."*

The apprentice notes: every interactive element on the page now glows when reached by keyboard.

---
