---
id: fe-jun-m7-04
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
lesson: react_testing_library
title: "React Testing Library"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m7-03]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what RTL is and why it differs from Enzyme"
    - "Describes what render() does and what screen provides"
    - "Explains the 'test like users use' philosophy in own words"
    - "Identifies what RTL intentionally does NOT let you test"
  keywords: [render, screen, user, accessible, query, DOM, component, behaviour]
  modelAnswer: |
    React Testing Library renders components into a real DOM and provides utilities to query that DOM the way users would — by visible text, role, or label, not by class names or internal state. Unlike Enzyme, RTL does not expose component internals. This forces tests to verify what the user sees and does, not how the component is implemented. If the implementation changes but the behaviour stays the same, RTL tests still pass. This makes tests more robust and more meaningful as specifications of user behaviour.
guidedSteps:
  - id: fe-jun-m7-04-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "React Testing Library's core philosophy is to test components:"
    inputConfig:
      options:
        - "By inspecting internal state and private methods"
        - "The way users interact with them — through visible text and accessible roles"
        - "By checking component lifecycle methods"
        - "Only through snapshot comparisons"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The way users interact with them — through visible text and accessible roles"]
      rejectedFeedback: "RTL's guiding principle: the more your tests resemble the way your software is used, the more confidence they give you. Tests should verify behaviour visible to the user, not implementation details invisible to them."
    hint: "Think about what a real user sees and does — they don't see state variables or CSS class names."
    reflectionPrompt: "Tests that check internal state break when you refactor internals even if behaviour is unchanged. Tests that check visible behaviour survive refactoring — and that's the confidence you want."
  - id: fe-jun-m7-04-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "You have a `<LoginForm />` component. Using RTL's philosophy, describe two things you should test and one thing you should NOT test."
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [button, submit, text, label, visible, state, internal, implementation, class]
      rejectedFeedback: "Test: that the submit button is visible, that submitting with an empty email shows an error message, that a valid submission calls the handler. Do NOT test: the internal state variable name, CSS class names, or the component's implementation details."
    hint: "What can a user see and interact with? What is invisible to them?"
    reflectionPrompt: "The test suite is documentation of user behaviour. If it reads like a user story ('when I submit without an email, I see an error'), it's good. If it reads like implementation notes ('when isSubmitting is true'), it's fragile."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `render(<Button label='Save' />)` do in React Testing Library?"
    options:
      - "Returns the component's props and state for inspection"
      - "Renders the component into a real DOM and makes it queryable via screen"
      - "Creates a snapshot of the component's HTML"
      - "Mounts the component without a DOM"
    correctIndex: 1
    feedback: "render() mounts the component into a real DOM environment (jsdom). After calling it, you query the DOM via screen — screen.getByText('Save'), screen.getByRole('button'), etc. The component behaves as it would in a browser."
retrieval:
  recall: "What is the key difference in philosophy between React Testing Library and Enzyme?"
  explain: "Why does RTL not expose component internal state? What problem does this constraint solve?"
  mistakeId:
    code: |
      // Testing internal state — RTL anti-pattern
      const { instance } = render(<Counter />);
      expect(instance.state.count).toBe(0);
    answer: "This is Enzyme-style testing, not RTL. RTL deliberately does not expose internal state. Test what the user sees: expect(screen.getByText('Count: 0')).toBeInTheDocument(). This way, if you refactor from class state to a hook, the test still passes — because the behaviour (displaying the count) hasn't changed."
---

# Hook

Your test suite passes. Green across the board. But when you move a state variable from a class component to a `useState` hook, half the tests break — not because the component stopped working, but because the tests were inspecting internal implementation details that no longer exist.

This is the test fragility trap. React Testing Library exists to break you out of it.

# Lore Introduction

*"You tested the inner workings of the spell,"* says Instructor Mireth, studying the broken test output. *"You verified which runes were active at each stage. But you never tested what the spell actually does — what it produces, what the apprentice can see and touch."*

She rewrites one test. It tests the light the spell produces, not the rune configuration inside.

*"Implementation changes. Effect remains. Your tests should verify the effect."*

# Core Learning

## Concept Introduction

**React Testing Library (RTL)** is the standard tool for testing React components. Its core principle:

> *"The more your tests resemble the way your software is used, the more confidence they give you."* — Kent C. Dodds

RTL renders components into a real DOM (via jsdom) and gives you utilities to interact with that DOM the same way a user would — finding elements by visible text, accessible role, or label.

| Feature | RTL | Enzyme |
|---|---|---|
| Access internal state | ❌ Not exposed | ✅ Exposed |
| Access DOM as user sees it | ✅ Primary mode | Limited |
| Tests survive refactoring | ✅ Yes (if behaviour unchanged) | Often ❌ |
| Philosophy | Behaviour-driven | Implementation-driven |

## Why It Matters

Tests that check internal state break when you refactor even if the user experience is identical. RTL's constraints force you to write tests that are specifications of user behaviour — they break when the behaviour breaks, not when the code structure changes.

## Worked Example

```jsx
// Button.jsx
function Button({ label, onClick }) {
  return <button onClick={onClick}>{label}</button>;
}
```

```jsx
// Button.test.jsx
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { Button } from './Button';

test('calls onClick when clicked', async () => {
  const handleClick = vi.fn();
  render(<Button label="Save" onClick={handleClick} />);

  // Query by visible text — what the user sees
  await userEvent.click(screen.getByRole('button', { name: 'Save' }));

  expect(handleClick).toHaveBeenCalledOnce();
});
```

Key imports:
- `render` — mounts the component into jsdom
- `screen` — query the rendered DOM
- `userEvent` — simulate realistic user interactions

## Common Mistakes

- **Using `getByTestId` for everything.** Test IDs are for last resort. Prefer `getByRole`, `getByLabelText`, `getByText` — they test accessibility too.
- **Testing implementation details.** Don't check state variable values or internal method calls.
- **Skipping `await` on userEvent.** `userEvent` functions are async since v14. Always `await userEvent.click(...)`.
- **Not wrapping in `act`.** RTL wraps most things in `act` for you — don't manually wrap unless you have a very specific reason.

## Mini Summary

- RTL renders components into a real DOM and queries it as users would
- The philosophy: test behaviour, not implementation
- Use `screen` to query the DOM; avoid querying by internal state
- Tests that verify behaviour survive refactoring; tests that verify internals don't

# Guided Practice Quest

Work through the two guided steps to confirm you understand RTL's approach before you start writing component tests.

# Solo Practice Quest

Write a test for a `<Greeting name="Aria" />` component that renders `<h1>Hello, Aria!</h1>`. Use RTL: render it, query by role, assert the text. Then describe: what would break this test? What would NOT break it?

# Integration

**Psychology — Cognitive Fidelity in Testing**

RTL's philosophy maps to a concept from cognitive psychology: tests should model how users actually perceive the system. Users perceive visible text, accessible roles, and interactive elements — they do not perceive React state or CSS class names. Tests that model user perception have higher cognitive fidelity: when they fail, the failure is meaningful (something the user would notice broke). Tests that model implementation have low cognitive fidelity: failures are often artefacts of refactoring, not real regressions. Designing tests with high cognitive fidelity is analogous to designing usability studies — both attempt to capture the reality of user experience rather than the internal model of the developer.

# Lore Conclusion

*"Now your tests verify what I care about,"* Mireth says, reviewing the green output. *"Not how the spell is cast — what it does. When you improve the casting method, the test does not break. When the effect changes, it does."*

The component testing tome opens to the next page.

---
