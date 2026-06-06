---
id: fe-sen-m5-04
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m5
moduleTitle: "Module 5: Design Systems"
moduleGlyph: "🎨"
moduleSortOrder: 5
topicSlug: accessibility_governance
topicTitle: "Accessibility Governance"
topicSortOrder: 4
lesson: accessibility_governance
title: "Accessibility Governance"
sortOrder: 1
difficulty: 4
estimatedMinutes: 30
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Explains why the design system is the highest-leverage place for accessibility
    - Describes how to integrate automated accessibility testing into Storybook and CI
    - Explains the limits of automated accessibility testing
    - Describes what ARIA attributes design system components must include
    - Synthesises a governance model for design system accessibility
  keywords: [axe-core, accessibility, ARIA, role, keyboard, focus, jest-axe, Storybook a11y addon, CI, governance, audit, accessible by default]
  modelAnswer: |
    The design system is the highest-leverage place for accessibility: fix it once in the component, and every team that uses the component gets it for free. Components that are inaccessible multiply the problem — every team ships inaccessible UI without knowing it.

    Automated testing integrates into: Storybook (a11y addon using axe-core — shows violations inline in every story), CI (jest-axe in unit tests — asserts no violations on every component render), and a pre-merge accessibility check. Automated tools catch ~30-40% of WCAG issues: missing labels, incorrect ARIA roles, low contrast, missing alt text.

    The 60-70% that automation misses requires manual testing: keyboard navigation (can every interaction be performed without a mouse?), screen reader testing (does the announcement make sense?), focus management (does focus move to the correct element after interactions?), colour contrast for dynamic states.

    A governance model: (1) no new component ships without passing automated tests; (2) complex interactive components (modal, dropdown, combobox) require manual screen reader testing; (3) accessibility issues blocking WCAG AA compliance are treated as bugs (not enhancements); (4) quarterly accessibility audits of the full design system.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "Your design system's Modal component traps focus correctly when open. A team uses the Modal — they don't need to implement focus trapping. Why does this represent governance at scale?"
    options:
      - "The team saved development time by reusing the Modal"
      - "Every team that uses the Modal gets correct focus trapping automatically, without knowing how to implement it"
      - "The governance prevents the team from changing the Modal"
      - "Focus trapping is a design system responsibility, not a product team responsibility"
    correctIndex: 1
    feedback: "This is the leverage of accessible-by-default components. Focus trapping in a modal is complex (must trap focus, restore it on close, handle nested focus scopes). If the design system handles it correctly, every team gets it for free. Teams that build their own modal typically don't implement focus trapping correctly. The design system scales correct accessibility implementation across the organisation."
  - type: SHORT_TEXT
    prompt: "Your automated accessibility CI check passes on every PR. A screen reader user reports they can't navigate your dropdown. What does this reveal about automated testing?"
    hint: "What can axe-core detect? What does it need a human to verify?"
  - type: FILL_BLANK
    prompt: "Automated tools catch approximately ___% of WCAG issues. The remainder requires ___ testing."
    answer: "30-40; manual (keyboard/screen reader)"
    hint: "No tool can verify that a screen reader announcement makes semantic sense."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which accessibility issue would automated testing miss but a user with a screen reader would immediately notice?"
    options:
      - "Missing alt text on an image"
      - "A button with aria-hidden='true' that's still focusable"
      - "A modal that announces 'dialog opened' but the announcement content is confusing and unhelpful"
      - "Insufficient colour contrast ratio"
    correctIndex: 2
    feedback: "Automated tools check structure and attributes — they cannot evaluate whether an announcement is meaningful or confusing. A screen reader says 'dialog opened — Form — Submit' when it should say 'Delete Account dialog — Are you sure? — Cancel — Delete permanently.' This logical error is invisible to axe-core but immediately obvious to a screen reader user."
  - type: MULTIPLE_CHOICE
    question: "A Button component passes all axe-core checks but has no visible focus ring on keyboard focus. What does this indicate?"
    options:
      - "The automated test is incorrect — it should have caught this"
      - "The missing focus ring is a WCAG violation that automated tools may or may not catch, depending on configuration"
      - "Focus rings are optional for buttons"
      - "The axe-core library doesn't test focus indicators"
    correctIndex: 1
    feedback: "WCAG 2.4.7 (Focus Visible) requires that keyboard-focused elements have a visible indicator. Some automated configurations check this; others don't. Focus ring visibility also depends on browser defaults, CSS overrides, and testing environment. This is a category of issues that requires manual keyboard testing to verify reliably."
retrieval:
  recall: "How does the jest-axe library enable automated accessibility testing in a component test?"
  explain: "Why does treating accessibility as a design system governance responsibility rather than a product team responsibility produce better outcomes?"
  mistakeId:
    code: |
      // Design system's accessible-by-default checkbox
      function Checkbox({ label, checked, onChange }) {
        return (
          <div onClick={onChange}>
            <div className={checked ? 'checked-icon' : 'unchecked-icon'} />
            <span>{label}</span>
          </div>
        );
      }
    answer: "Multiple accessibility failures: (1) a div, not an input[type=checkbox] — screen readers won't announce it as a checkbox, (2) no role='checkbox' or aria-checked attribute, (3) not keyboard focusable (no tabIndex), (4) no keyboard activation (onClick on div doesn't fire on Space/Enter). Every team using this 'checkbox' ships inaccessible UI. The correct design system component uses a native <input type='checkbox'> (free keyboard support, ARIA semantics, focus management) or at minimum a div with role='checkbox', aria-checked, tabIndex='0', and keyboard event handlers."
---

# Hook

Your company's accessibility audit finds 127 issues. 89 of them are in components from your design system. The same 12 patterns, replicated across 40 products.

You fix the design system. 89 issues disappear across 40 products simultaneously.

This is why accessibility governance belongs in the design system.

# Lore Introduction

*"A flawed rune carved into the master template produces the same flaw in every copy,"* the Rune Master explains. *"Fix the flaw in the master — and every copy is healed."*

She gestures at the design system. *"This is your master template. Its quality determines the quality of everything built from it. Accessibility is not optional in the master."*

# Core Learning

## Concept Introduction

**Accessibility governance in design systems** means: every component ships with accessibility built in, tested, and documented.

**Automated testing in Storybook:**
```bash
npm install --save-dev @storybook/addon-a11y
```
```js
// .storybook/main.js
addons: ['@storybook/addon-a11y']
```
Every story shows an "Accessibility" panel with axe-core violations.

**Automated testing in unit tests (jest-axe):**
```tsx
import { render } from '@testing-library/react';
import { axe, toHaveNoViolations } from 'jest-axe';
expect.extend(toHaveNoViolations);

test('Button has no accessibility violations', async () => {
  const { container } = render(<Button variant="primary">Save</Button>);
  const results = await axe(container);
  expect(results).toHaveNoViolations();
});
```

**What axe-core catches (~30-40% of WCAG):**
- Missing label associations
- Incorrect ARIA roles
- Images without alt text
- Insufficient colour contrast
- Invalid ARIA attributes
- Missing landmark roles

**What requires manual testing (~60-70%):**
- Keyboard navigation flows (can you complete every task without a mouse?)
- Screen reader announcement quality
- Focus management after interactions (modal open/close, route changes)
- Visible focus indicators in all states
- Cognitive load and interaction patterns

**Governance model:**
1. Every new component passes automated axe-core in CI
2. Complex interactive components (Modal, Dropdown, DatePicker, Combobox) require manual screen reader testing before release
3. Accessibility bugs blocking WCAG AA = P1 (treated as critical bugs)
4. Quarterly design system accessibility audit

## Common Mistakes

- **Assuming automated testing = full accessibility compliance.** Automation covers ~30-40%. Manual testing is non-negotiable for complex components.
- **Not testing keyboard navigation.** axe-core doesn't navigate the page — a human must verify the full keyboard flow.
- **Using custom elements without ARIA.** A `<div onClick={...}>` needs `role='button'`, `tabIndex='0'`, and keyboard event handlers. Or better: use the native `<button>`.
- **Treating accessibility violations as enhancement requests.** WCAG AA compliance is a legal requirement in many jurisdictions. Violations are bugs.

## Mini Summary

- ✔ Design system is the highest-leverage place for accessibility: fix once, fix everywhere
- ✔ Automate with axe-core (Storybook addon + jest-axe in CI)
- ✔ Automated tools catch ~30-40% — manual keyboard and screen reader testing covers the rest
- ✔ Treat accessibility violations as bugs, not enhancements
- ✔ Complex interactive components require manual testing before release

# Guided Practice Quest

Work through the guided steps to understand the leverage of design system accessibility and the limits of automated testing.

# Solo Practice Quest

Propose an accessibility governance programme for a design system used by 15 product teams. Include: how to integrate automated testing, what the manual testing process is for complex components, how to handle regressions, what the escalation path is for accessibility bugs, and how you'd audit the existing components.

# Integration

**Philosophy — The Ethics of Platform Responsibility**

Design system accessibility governance raises a question of distributed moral responsibility. If a product team ships inaccessible UI using a design system component that was itself inaccessible, who is responsible? The product team (for using it)? The design system team (for building it)? Leadership (for not prioritising accessibility)? This is the problem of collective action in ethics — moral responsibility distributed across a system rather than attributable to a single actor. The governance framework resolves this by creating clear accountability: the design system team is responsible for component accessibility; product teams are responsible for correct usage. This separation of responsibility is not just organisational — it's ethical. The design system team has the expertise and the leverage; product teams have neither. Clear governance matches responsibility to capability.

# Lore Conclusion

*"The master template is sound,"* the Rune Master says. *"Every copy carved from it carries the correct runes. The flaw is gone — in the master, and in all its progeny."*

---
