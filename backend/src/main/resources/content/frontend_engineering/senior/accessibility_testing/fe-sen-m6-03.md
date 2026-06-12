---
id: fe-sen-m6-03
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m6
moduleTitle: "Module 6: Advanced Accessibility"
moduleGlyph: "♿"
moduleSortOrder: 6
topicSlug: accessibility_testing
topicTitle: "Accessibility Testing"
topicSortOrder: 3
lesson: accessibility_testing
title: "Accessibility Testing"
sortOrder: 1
difficulty: 4
estimatedMinutes: 30
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Describes the automated testing toolkit (axe-core, jest-axe, Storybook a11y)
    - Explains what automated tools can and cannot detect
    - Describes the manual keyboard testing process
    - Explains how to conduct basic screen reader testing
    - Synthesises a layered accessibility testing strategy
  keywords: [axe-core, jest-axe, keyboard, screen reader, NVDA, VoiceOver, tab, focus, announce, automated, manual, 30-40%]
  modelAnswer: |
    Automated accessibility testing uses axe-core (the underlying library), which runs in jest-axe for unit tests and in the Storybook a11y addon for interactive testing. These catch approximately 30-40% of WCAG issues: missing alt text, incorrect ARIA, colour contrast failures, form label issues. They cannot catch: whether keyboard navigation is logical, whether screen reader announcements are meaningful, whether focus order makes sense, or whether interaction flows are intuitive.

    Manual keyboard testing: disconnect the mouse. Navigate entirely by Tab (forward), Shift+Tab (backward), Enter/Space (activate), arrow keys (in widgets), Escape (dismiss). Verify: can all tasks be completed? Is focus always visible? Does focus order follow visual layout? Is nothing focus-trappable unintentionally?

    Screen reader testing: VoiceOver on macOS/iOS (free), NVDA on Windows (free), JAWS on Windows (paid — enterprise users). Testing process: navigate by headings (H), landmarks (D for NVDA, R for VoiceOver), links (L), form controls (F). Verify: does the heading structure make sense? Are buttons announced with their label? Do form errors get announced? Do modals trap focus and announce their title?

    A complete testing strategy layers: automated (CI + Storybook), keyboard testing (every feature before shipping), screen reader spot-checking (critical paths), and periodic expert audits (quarterly or pre-major-release).
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "jest-axe passes on your form component. A real keyboard user reports they can't complete the form. What does this reveal?"
    options:
      - "jest-axe has a bug — it should have caught this"
      - "Automated tests catch structural issues; keyboard usability requires manual testing"
      - "The keyboard user is using an unsupported browser"
      - "The form has a JavaScript error that jest-axe doesn't detect"
    correctIndex: 1
    feedback: "axe-core tests the DOM for structural accessibility violations — missing labels, incorrect ARIA roles, contrast. It cannot navigate your form and verify that the Tab order makes sense, that error messages are announced at the right moment, or that the submit button is reachable. Manual keyboard testing fills this gap."
  - type: SHORT_TEXT
    prompt: "Describe your keyboard testing process for a dropdown menu component. What keys do you test, and what do you verify at each step?"
    hint: "Start at the trigger, open the menu, navigate options, select one, close without selecting. What should happen at each stage?"
  - type: FILL_BLANK
    prompt: "Screen readers announce content in a ___ that doesn't always match visual order. Verifying the announcement sequence requires ___ with an actual screen reader."
    answer: "DOM order; manual testing"
    hint: "Automated tools see the DOM structure; they don't 'read' it as a screen reader would."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You're testing a data table with a screen reader. The column headers are announced as the user moves through cells. What WCAG criterion and HTML attribute enables this?"
    options:
      - "aria-label on each cell"
      - "scope='col' on th elements (WCAG 1.3.1 — Info and Relationships)"
      - "aria-describedby pointing to the header row"
      - "role='columnheader' on each td"
    correctIndex: 1
    feedback: "Tables use `<th scope='col'>` for column headers and `<th scope='row'>` for row headers. Screen readers use these to announce the relevant header when a user moves to a data cell. Without scope, screen readers may not announce headers, making the table incomprehensible. WCAG 1.3.1 requires information conveyed through structure (table layout) to be programmatically determinable."
  - type: MULTIPLE_CHOICE
    question: "Which screen readers should you test on for broad coverage?"
    options:
      - "Only Chrome's built-in accessibility tools"
      - "VoiceOver on macOS/iOS and NVDA on Windows — covering the majority of screen reader users"
      - "JAWS only — it has the largest enterprise market share"
      - "Any modern screen reader behaves identically"
    correctIndex: 1
    feedback: "WebAIM surveys consistently show NVDA (Windows, free) and VoiceOver (macOS/iOS, free) as the two most-used screen readers. Testing both covers the majority of real users. JAWS is important for enterprise contexts. Screen readers do not behave identically — they have different keyboard shortcuts, different announcement patterns, and different ARIA support. Test in at least two."
retrieval:
  recall: "List three things automated accessibility testing can detect and three things it cannot detect."
  explain: "What is the manual keyboard testing process for verifying a modal dialog?"
  mistakeId:
    code: |
      // Accessibility testing strategy
      "We run the Lighthouse accessibility audit before each release.
       Our score is consistently 95+. We consider this sufficient
       accessibility coverage."
    answer: "Lighthouse scores accessibility based on axe-core — catching ~30-40% of WCAG issues. A score of 95 means the automated tests passed; it doesn't mean the application is accessible. Real accessibility requires: keyboard testing (can all tasks be completed without a mouse?), screen reader testing (are announcements meaningful?), and contrast checking for all states. Lighthouse 95 on a completely keyboard-inaccessible app is entirely possible if the structural markup happens to be correct."
---

# Hook

Your Lighthouse accessibility score is 98. A user who is blind emails to say they can't use your application. They explain: modals don't focus correctly, form errors aren't announced, and the dropdown menu is unusable with a screen reader.

Lighthouse caught the structure. It didn't catch the experience.

# Lore Introduction

*"The physical audit checked the dimensions of the doors,"* the Academy Inspector explains. *"They meet the minimum requirements. But the auditor never tried to open one — they swing the wrong direction for a person in a wheelchair."*

She sets down the automated report. *"The inspection found no violations. The experience fails. Both the automated check and the manual test are necessary. Neither alone is sufficient."*

# Core Learning

## Concept Introduction

**Accessibility testing is layered:**

| Layer | Tools | What it catches |
|---|---|---|
| Automated (unit) | jest-axe | ~30-40% of WCAG |
| Automated (visual) | Storybook a11y addon | Same, plus visual inspection |
| Manual keyboard | Physical testing (no mouse) | Tab order, focus, keyboard flows |
| Manual screen reader | VoiceOver, NVDA | Announcement quality, flow |
| Expert audit | Specialist review | Systematic, comprehensive |

**Automated testing (jest-axe):**
```tsx
import { render } from '@testing-library/react';
import { axe, toHaveNoViolations } from 'jest-axe';
expect.extend(toHaveNoViolations);

test('Form has no axe violations', async () => {
  const { container } = render(<LoginForm />);
  const results = await axe(container);
  expect(results).toHaveNoViolations();
});
```

**Keyboard testing checklist:**
- [ ] Disconnect mouse. Navigate by Tab only.
- [ ] Tab reaches all interactive elements
- [ ] Focus is always visible (not invisible or clipped)
- [ ] Tab order follows logical reading order
- [ ] Dropdown menus: arrow keys navigate, Enter activates, Escape closes
- [ ] Modals: focus traps inside, Escape closes, focus returns to trigger
- [ ] All tasks completable without a mouse

**Screen reader testing (VoiceOver macOS):**
- Command + F5 to enable
- Navigate by headings: Control + Option + H
- Navigate by links: Control + Option + L
- Navigate by form controls: Control + Option + J
- Verify: headings describe content, links describe destination, errors are announced, modals announce title

## Why It Matters

Accessibility claims are worthless until tested — and testing is where most teams discover the gap between "we use semantic HTML" and "a screen reader user can actually check out":

- Automated tools (axe, Lighthouse) catch only roughly a third of WCAG failures; the rest — focus order, announcement quality, cognitive flow — require a human at a keyboard and a screen reader
- Catching an inaccessible pattern in code review costs minutes; catching it after launch means re-engineering shipped flows under complaint or legal pressure
- Testing builds the team's instincts: engineers who have once navigated their own form by keyboard alone write better markup forever after

A test plan that layers automation (every build), keyboard passes (every feature), and assistive-technology sessions (every major flow) is what turns accessibility from an aspiration into a verified property.

## Common Mistakes

- **Treating Lighthouse score as compliance.** It's one input. Not sufficient alone.
- **Not testing keyboard flow, only tab stop existence.** A page where every element is focusable but the order makes no sense is inaccessible.
- **Using only one screen reader.** VoiceOver and NVDA behave differently. Test both.
- **Not testing dynamic interactions.** Static page testing catches static issues. Live regions, modals, and route changes are dynamic — test them in the running app.

## Mental Model

Accessibility testing is a pre-flight inspection, not a crash investigation. Aviation doesn't rely on one big annual check; it layers fast, frequent, cheap checks (the walk-around before every flight — your automated axe scan in CI) with deeper periodic ones (scheduled maintenance — manual keyboard and screen reader passes) and full teardowns (the heavy audit — expert review with real assistive technology users). Each layer is calibrated to catch what the cheaper layer cannot, and no layer is skipped because the previous one passed. Treat a green axe report as the walk-around: necessary, fast, and nowhere near sufficient to declare the aircraft airworthy.

## Mini Summary

- ✔ Automated: jest-axe catches 30-40% — necessary but not sufficient
- ✔ Keyboard: disconnect mouse, verify all tasks completable, focus always visible
- ✔ Screen reader: VoiceOver (Mac/iOS) and NVDA (Windows) cover most users
- ✔ Test dynamic interactions: modals, live regions, route changes, dropdown menus
- ✔ Layer automated + keyboard + screen reader for comprehensive coverage

# Guided Practice Quest

Work through the guided steps to understand the gap between automated testing and real accessibility and how to conduct keyboard testing.

# Solo Practice Quest

Write a keyboard testing protocol for a checkout flow (cart review → shipping address → payment → confirmation). List each step, what keyboard actions to perform, and what to verify at each point. This is the type of testing checklist a QA engineer or accessibility specialist would follow.

# Integration

**Mathematics — Test Coverage and Fault Detection**

The relationship between automated and manual accessibility testing mirrors test coverage theory. Automated tools provide deterministic coverage of a specific fault class (structural ARIA issues). Manual testing covers a broader, less well-defined fault space (experiential accessibility). The total fault space is not fully coverable by any combination of tools — some faults only manifest with specific screen reader versions, specific browser combinations, or specific user interaction patterns. This is the incompleteness theorem applied to accessibility testing: you cannot prove the absence of all accessibility faults, only increase your confidence through layered testing. The practical implication: define a testing budget, allocate it across automated and manual layers, and accept residual risk proportionate to criticality (higher criticality = more manual testing).

# Lore Conclusion

*"The inspection is complete,"* the Academy Inspector says. *"Automated: 34 criteria checked. Keyboard: all flows passable. Screen reader: announcements correct, modals behave correctly. The combined inspection found 12 issues the automated tools missed. They are corrected."*

---
