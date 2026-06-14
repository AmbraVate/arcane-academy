---
id: fe-jun-m7-05
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m7
moduleTitle: "Module 7: Frontend Testing"
moduleGlyph: "🧪"
moduleSortOrder: 7
topicSlug: component_testing
topicTitle: "Component Testing"
topicSortOrder: 2
lesson: querying_the_dom
title: "Querying the DOM"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m7-04]
integrationDomains: [design, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the difference between getBy, queryBy, and findBy"
    - "Identifies which query type to use for async elements"
    - "Explains why ByRole is preferred over ByTestId"
    - "Describes what ByRole tests that ByTestId does not"
  keywords: [getBy, queryBy, findBy, role, text, label, async, accessible, throw, null]
  modelAnswer: |
    getBy throws an error if the element is not found — use it when you expect the element to exist. queryBy returns null instead of throwing — use it to assert an element is NOT present. findBy returns a Promise — use it for elements that appear asynchronously. ByRole is preferred because it tests accessible semantics: if an element has the right role, it's accessible to screen readers and keyboard users. ByTestId tests a data attribute that users never see — it says nothing about accessibility and can give false confidence.
guidedSteps:
  - id: fe-jun-m7-05-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You want to assert that an error message is NOT shown. Which query should you use?"
    inputConfig:
      options:
        - "getByText('Error') — and check it throws"
        - "queryByText('Error') — and assert the result is null"
        - "findByText('Error') — and catch the rejection"
        - "getByRole('alert') — and expect it to be undefined"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["queryByText('Error') — and assert the result is null"]
      rejectedFeedback: "getBy throws when the element is missing — wrapping it in try/catch to assert absence is awkward and unreliable. queryBy returns null when the element is absent, which is perfect for asserting non-presence: expect(screen.queryByText('Error')).not.toBeInTheDocument()."
    hint: "Which query variant returns null instead of throwing?"
    reflectionPrompt: "The three query families have different contracts. Knowing which to use in each situation is one of the most practical RTL skills."
  - id: fe-jun-m7-05-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Explain why `screen.getByRole('button', { name: 'Submit' })` is better than `screen.getByTestId('submit-btn')` from both a testing and accessibility perspective."
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [accessible, role, screen reader, semantic, aria, label, testId, attribute]
      rejectedFeedback: "getByRole verifies that the element has the correct semantic role (button) and an accessible name ('Submit') — meaning screen readers and keyboard users can find it too. data-testid is invisible to assistive technology; it gives you a passing test but tells you nothing about whether the UI is accessible. A button with the wrong role might pass a testId test but fail for screen reader users."
    hint: "What does a user relying on a screen reader need that a data-testid doesn't verify?"
    reflectionPrompt: "ByRole queries are accessibility auditors. If your component doesn't have the right role and accessible name, the query fails — and so does the accessibility."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A data-fetch completes and a list appears 500ms after render. Which query should you use to find a list item?"
    options:
      - "getByText — it will find it immediately"
      - "queryByText — returns null then retries"
      - "findByText — awaits the element appearing in the DOM"
      - "getByRole — it polls automatically"
    correctIndex: 2
    feedback: "findBy queries return Promises that resolve when the element appears (or reject after a timeout). Use `await screen.findByText('Item 1')` for async elements. getBy and queryBy are synchronous — they fail immediately if the element isn't already present."
retrieval:
  recall: "Name all three query families (getBy/queryBy/findBy) and the scenario each is best suited for."
  explain: "Why is getByRole generally preferred over getByTestId for querying interactive elements?"
  mistakeId:
    code: |
      // Asserting element absence with getBy
      expect(() => screen.getByText('Error')).toThrow();
    answer: "This is fragile — it passes if getByText throws for ANY reason, not just because the element is absent. The correct approach is: expect(screen.queryByText('Error')).not.toBeInTheDocument(). queryBy returns null when the element is absent, giving a clear and intentional assertion."
---

# Hook

Your test says the error message doesn't appear. But it throws a confusing error about `getByText` failing. You stare at it for ten minutes before realising: you used the wrong query. The element is absent — and that's correct — but `getByText` throws when an element is absent.

This is the moment every developer hits. Learning the three query families up front saves you from it.

# Lore Introduction

*"The archive holds three kinds of search,"* the Librarian explains, sliding three keys across the counter.

*"The first: find or fail loudly. The second: find or return nothing. The third: wait, then find."*

She taps each key. *"Use the wrong key, and the archive misleads you. The right key makes your intent clear — and your results trustworthy."*

# Core Learning

## Concept Introduction

RTL's `screen` object provides three **query families**, each with a different contract:

| Family | Found | Not Found | Use When |
|---|---|---|---|
| `getBy*` | Returns element | **Throws** | You expect element to exist now |
| `queryBy*` | Returns element | Returns **null** | You expect element to be absent |
| `findBy*` | Returns Promise resolving to element | Promise **rejects** after timeout | Element appears asynchronously |

Each family has multiple **query types** (suffixes):

| Suffix | What it finds |
|---|---|
| `ByRole` | By ARIA role and accessible name — **preferred** |
| `ByLabelText` | By form label text |
| `ByText` | By visible text content |
| `ByPlaceholderText` | By input placeholder |
| `ByTestId` | By `data-testid` attribute — **last resort** |

## Why It Matters

Using the wrong query family leads to:
- Tests that accidentally pass (asserting absence with `getBy` that throws for the wrong reason)
- Tests that fail unnecessarily (using `getBy` for async elements that haven't loaded yet)
- False confidence (using `getByTestId` when the element has wrong accessibility semantics)

## Worked Example

```jsx
// TaskItem.jsx
function TaskItem({ task, onDelete }) {
  return (
    <li>
      <span>{task.title}</span>
      <button onClick={() => onDelete(task.id)}>Delete</button>
    </li>
  );
}
```

```js
// TaskItem.test.jsx
import { render, screen } from '@testing-library/react';

test('renders the task title', () => {
  render(<TaskItem task={{ id: 1, title: 'Buy milk' }} onDelete={() => {}} />);
  // getBy — expect it to be present right now
  expect(screen.getByText('Buy milk')).toBeInTheDocument();
});

test('does not show a delete confirmation by default', () => {
  render(<TaskItem task={{ id: 1, title: 'Buy milk' }} onDelete={() => {}} />);
  // queryBy — expect it to be absent
  expect(screen.queryByText('Are you sure?')).not.toBeInTheDocument();
});

test('shows updated title after async load', async () => {
  render(<TaskDetail id={42} />); // fetches title from API
  // findBy — waits for it to appear
  const heading = await screen.findByRole('heading', { name: 'Task: Review proposal' });
  expect(heading).toBeInTheDocument();
});
```

## Common Mistakes

- **Using `getBy` to assert absence.** It throws — not because the element is missing, but because that's what `getBy` does. Use `queryBy`.
- **Using `getBy` for async elements.** The element isn't in the DOM yet when `getBy` runs. Use `findBy` with `await`.
- **Defaulting to `ByTestId`.** It tests nothing about accessibility. Reach for `ByRole`, `ByLabelText`, or `ByText` first.
- **Missing `await` with `findBy`.** `findBy` returns a Promise. Without `await`, you get the Promise object, not the element.

## Mini Summary

- `getBy*` — throws if absent; use when you expect presence
- `queryBy*` — returns null if absent; use when you expect absence
- `findBy*` — awaits appearance; use for async elements
- Prefer `ByRole` > `ByLabelText` > `ByText` > `ByTestId`

# Guided Practice Quest

Work through the two guided steps to confirm you can choose the right query type for each scenario.

# Solo Practice Quest

Write three short tests for a `<Alert type="error" message="Something went wrong" />` component:
1. Assert the message is visible (element is present)
2. Assert a success icon is NOT visible (element is absent)
3. (Imagine it fetches its message from an API) — write the query that would handle async rendering

Write which query family you'd use for each and why.

# Integration

**Design — Query Priority as Accessibility Audit**

The RTL query priority (ByRole → ByLabelText → ByText → ByTestId) is not arbitrary — it maps to how accessible a UI is. If you can query an element by role with an accessible name, screen reader users can find it too. If you can only query it by testId, assistive technology probably can't find it either. Writing tests using high-priority queries is simultaneously an accessibility audit. Teams that adopt ByRole-first testing discover inaccessible components that ByTestId tests would have never caught — a virtuous side effect of testing technique.

# Lore Conclusion

*"Three keys, three intentions,"* the Librarian says, watching your tests pass cleanly. *"The right key makes the archive yield its secrets precisely as you asked. No more, no less."*

The query tome snaps shut. You know which key to reach for.

---
