---
id: fe-app-m6-05
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
lesson: screen_readers
title: "Screen Readers"
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
    - "Describes how screen readers navigate web pages (landmarks, headings, links)"
    - "Explains how semantic HTML directly benefits screen reader users"
    - "Describes the purpose of ARIA roles and when to use them"
    - "Explains visually-hidden content technique and its use case"
    - "Names at least two real screen reader applications"
  keywords: [screen-reader, NVDA, JAWS, VoiceOver, landmark, heading, ARIA, role, announce, visually-hidden]
  modelAnswer: |
    Screen readers (NVDA, JAWS, VoiceOver) convert on-screen content to speech or braille.
    Users navigate by landmarks (header, main, nav), headings (H key to jump between headings),
    and links. Semantic HTML elements automatically create landmarks. ARIA roles add
    accessibility semantics when native HTML cannot. Visually-hidden content (CSS
    clip-path technique) is read by screen readers but not shown visually.
guidedSteps:
  - id: fe-app-m6-05-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A screen reader user presses the 'H' key to navigate a page. What does this do?
    inputConfig:
      options:
        - "Hides the current element"
        - "Jumps to the next heading (h1–h6) on the page"
        - "Opens a help menu"
        - "Goes to the homepage"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Jumps to the next heading (h1–h6) on the page"]
      rejectedFeedback: "Most screen readers support keyboard shortcuts for navigating by landmark type. H jumps between headings. D jumps between landmarks. L jumps between links. B jumps between buttons. This is why heading hierarchy matters: screen reader users skim by jumping through headings, exactly as sighted users visually skim."
    hint: "Screen reader users navigate by element type using single-key shortcuts."
    reflectionPrompt: "This changes how you think about headings. A heading is not just 'big text' — it is a navigation landmark. A page with no headings forces screen reader users to listen to every word linearly. A page with good heading structure lets them jump directly to the section they need."

  - id: fe-app-m6-05-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To hide text visually but keep it accessible to screen readers, use a class called:

      .___ { position: absolute; width: 1px; height: 1px; overflow: hidden; clip: rect(0,0,0,0); white-space: nowrap; }
    inputConfig:
      placeholder: "sr-only"
    markingRule:
      matchMode: CONTAINS
      accepted: [sr-only, sronly, visually-hidden, visually_hidden, screen-reader]
      rejectedFeedback: "The sr-only (or visually-hidden) class is a CSS technique to hide content visually while keeping it in the accessibility tree — screen readers read it, sighted users don't see it. Use it for: icon button labels, skip links when not focused, additional context for assistive technology."
    hint: "The class name is a common abbreviation for 'screen reader only'."
    reflectionPrompt: "display: none and visibility: hidden both hide content from screen readers AND visually. sr-only hides only visually. This distinction is critical: an icon button with no text label needs sr-only text so screen readers can announce the button's purpose."

  - id: fe-app-m6-05-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      When should you use an ARIA role attribute, and when should you use a native semantic HTML element instead?
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [native, semantic, HTML, ARIA, role, cannot, last-resort, prefer, element]
      rejectedFeedback: "Prefer native semantic elements: <button> over <div role='button'>, <nav> over <div role='navigation'>. ARIA adds accessibility semantics but not behaviour — a <div role='button'> still needs JavaScript for click and keyboard handling. Use ARIA only when no suitable native element exists."
    hint: "There is a 'first rule of ARIA' — what does it say about native HTML?"
    reflectionPrompt: "The First Rule of ARIA: 'If you can use a native HTML element or attribute with the semantics and behaviour you require already built in, instead of re-purposing an element and adding an ARIA role, state or property to make it accessible, then do so.' ARIA patches; native HTML solves."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A button only shows an icon (no visible text). What should you add for screen readers?"
    options:
      - "title=\"Description\" on the button"
      - "aria-label=\"Description\" on the button"
      - "A tooltip with display: none"
      - "No change needed — icons are universally understood"
    correctIndex: 1
    feedback: "aria-label provides a text label for the button that screen readers announce. title is not consistently announced by all screen readers. Visible text inside the button (possibly sr-only) is also correct. Icon-only buttons without accessible labels are a very common accessibility failure."
  - type: MULTIPLE_CHOICE
    question: "Which screen reader is built into macOS and iOS?"
    options:
      - "NVDA"
      - "JAWS"
      - "VoiceOver"
      - "TalkBack"
    correctIndex: 2
    feedback: "VoiceOver is built into macOS, iOS, and iPadOS. NVDA (NonVisual Desktop Access) is free and popular on Windows. JAWS (Job Access With Speech) is the dominant commercial screen reader on Windows. TalkBack is Android's built-in screen reader. Testing with real screen readers reveals issues automated tools miss."

retrieval:
  recall: "Explain how screen reader users typically navigate a page — what keyboard shortcuts do they use?"
  explain: "What is the difference between display: none and the sr-only CSS class, and when do you use each?"
  mistakeId:
    code: "<div role='button' onclick='submit()'>Submit</div>"
    answer: "Use <button type='submit'>Submit</button> instead. A native <button> automatically gets keyboard focus, Enter/Space activation, the button role, and cursor: pointer. The div version requires adding all of these manually via ARIA and JavaScript. Native HTML is always preferable."
---

# Hook

Screen readers convert web pages into speech or braille output, used by blind and low-vision users.

Approximately 7.5 million people in the US alone have a visual disability. A web page that works correctly with a screen reader expands your audience and is often legally required under WCAG and various national laws.

But building for screen readers is not a special case — it is a consequence of using good semantic HTML.

# Lore Introduction

*"The Academy's scrolls,"* says Master Aelindra, *"are read by apprentices using sight. But the oral tradition — the spoken version — must carry the same meaning. When you write for the ear rather than the eye, you discover what is truly essential: structure, sequence, and labels."*

# Core Learning

## Concept Introduction

**How screen readers work:** They traverse the accessibility tree — a parallel structure derived from the DOM. Semantic elements automatically contribute: `<nav>` → navigation landmark. `<h2>` → heading level 2. `<button>` → interactive button.

**Navigation methods:**
- **Landmarks** (D key): jump between page regions (`<header>`, `<nav>`, `<main>`, `<footer>`)
- **Headings** (H key): jump between `h1–h6` elements
- **Links** (K key): navigate between hyperlinks
- **Buttons** (B key): navigate between buttons and controls
- **Tab key**: move between focusable elements

**ARIA (Accessible Rich Internet Applications):**

| Situation | Solution |
|---|---|
| Icon button (no visible text) | `aria-label="Close dialog"` |
| Dynamic content update | `aria-live="polite"` on the region |
| Custom interactive widget | `role="..."` + keyboard handlers |
| Extra context | `aria-describedby="desc-id"` |

**Visually hidden but accessible:**
```css
.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border: 0;
}
```

## Why It Matters

Screen readers are how blind and low-vision users experience your pages — and whether your markup works with them is entirely your choice as its author:

- Millions of people browse with screen readers daily; an inaccessible page isn't ugly to them, it's *silent* or *gibberish*
- Legal exposure is real: accessibility lawsuits over screen-reader-broken sites are routine in several jurisdictions
- The fixes are rarely exotic — real headings, labelled controls, alt text — but you only apply them if you know how a page *sounds*

Hearing your own page through a screen reader once will change how you write HTML forever. That experience is this lesson's point.

## Common Mistakes

- Icon buttons with no accessible label (`aria-label`)
- `display: none` for content you want screen readers to read (use `sr-only` instead)
- ARIA used when native HTML would work
- Missing form labels (`aria-label` or `<label>`)

## Mental Model

A screen reader experiences your page like a phone call to a building's receptionist, not a glance at the building. A sighted user sees the whole lobby at once; a screen reader user hears it one announcement at a time — "heading level one: Orders. Link: New order. Button: unlabelled." Navigation happens by asking the receptionist for lists: all headings, all links, all landmarks. This is why structure is everything: with proper headings and labels the receptionist gives a guided tour; with div-soup the call is just "item, item, clickable, item" — a building described as a pile of bricks.

## Mini Summary

- ✔ Screen readers navigate via landmarks, headings, links, and keyboard
- ✔ Semantic HTML builds the accessibility tree automatically — less ARIA needed
- ✔ `aria-label` for when visible text doesn't describe the element
- ✔ `.sr-only` class hides visually but keeps content in the accessibility tree
- ✔ First Rule of ARIA: prefer native HTML; use ARIA only when native won't work

# Guided Practice Quest

**The Oral Archive** — three questions on screen reader compatibility. Steps in `guidedSteps`.

# Solo Practice Quest

Audit a simple HTML page for screen reader compatibility: check all images have alt text, all buttons have labels, heading hierarchy is correct, form inputs are labelled, and there is at least one landmark structure. List what you find and propose fixes for any issues.

# Integration

**Connecting to Psychology — Auditory Processing and Information Structure**

Research in auditory processing shows that listeners impose structure on continuous speech using prosodic cues (pauses, intonation). Without structure, long audio streams overwhelm working memory. Screen readers create structure through element type announcements ("heading level 2," "navigation landmark") — providing the auditory equivalent of visual hierarchy. Users who navigate by headings are performing the same skim-reading strategy as sighted users but in the auditory domain. This is why heading structure matters equally for visual and non-visual access.

# Lore Conclusion

*"A page that speaks clearly,"* says Master Aelindra, *"does so not because it was built for blind users — but because it was built correctly. The accessibility tree is the skeleton of your HTML. If the skeleton is sound, every rendering — visual, auditory, tactile — is possible. Build the skeleton first."*

---
