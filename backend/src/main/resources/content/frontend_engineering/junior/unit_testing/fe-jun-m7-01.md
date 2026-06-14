---
id: fe-jun-m7-01
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m7
moduleTitle: "Module 7: Frontend Testing"
moduleGlyph: "🧪"
moduleSortOrder: 7
topicSlug: unit_testing
topicTitle: "Unit Testing"
topicSortOrder: 1
lesson: why_test_frontends
title: "Why Test Frontends?"
sortOrder: 1
difficulty: 3
estimatedMinutes: 20
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m6-12]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains at least two reasons why frontend testing matters"
    - "Describes the testing pyramid and where unit/component/e2e tests sit"
    - "Identifies the trade-offs between test types (speed vs coverage)"
  keywords: [unit, component, e2e, pyramid, confidence, regression, fast]
  modelAnswer: |
    Frontend tests catch regressions before they reach users, give developers confidence to refactor safely, and document expected behaviour. The testing pyramid has unit tests at the base (many, fast, cheap), component tests in the middle (fewer, moderate), and end-to-end tests at the top (fewest, slow, expensive). A healthy test suite has many fast unit tests, a moderate number of component tests covering key user interactions, and a few critical e2e tests for the most important user flows.
guidedSteps:
  - id: fe-jun-m7-01-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Why does the testing pyramid recommend more unit tests than end-to-end tests?"
    inputConfig:
      options:
        - "Unit tests are easier to write than e2e tests"
        - "Unit tests are faster, cheaper, and more reliable than e2e tests"
        - "E2e tests can only run on real devices"
        - "Unit tests cover more of the application surface"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Unit tests are faster, cheaper, and more reliable than e2e tests"]
      rejectedFeedback: "The pyramid is about trade-offs. Unit tests run in milliseconds and almost never have flaky failures. E2e tests take minutes and are prone to flakiness from timing and environment issues."
    hint: "Think about the feedback loop: how long until you know a test failed?"
    reflectionPrompt: "If all your tests were e2e tests, what would happen to your CI pipeline run time?"
  - id: fe-jun-m7-01-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "What is a regression? Why do tests prevent regressions?"
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [regression, break, existing, already, worked, change, catch]
      rejectedFeedback: "A regression is when a code change breaks something that was previously working. Tests catch regressions by failing when the expected behaviour changes."
    hint: "Think about what happens when you change a utility function and don't realise it affects three different features."
    reflectionPrompt: "Without tests, how would you know whether your last commit broke an existing feature?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which type of test gives the fastest feedback when a utility function is broken?"
    options:
      - "End-to-end test"
      - "Manual browser testing"
      - "Unit test"
      - "Integration test"
    correctIndex: 2
    feedback: "Unit tests run in milliseconds against a single function. They give near-instant feedback without a browser, server, or network."
retrieval:
  recall: "Describe the three levels of the testing pyramid and what each tests."
  explain: "Why should the base of the testing pyramid have more unit tests than e2e tests?"
  mistakeId:
    code: |
      // Team's test suite:
      // - 2 unit tests
      // - 3 component tests
      // - 47 end-to-end tests
    answer: "This is an inverted pyramid (sometimes called an 'ice cream cone'). The CI pipeline will be slow and flaky. The team should add many unit tests for utilities and hooks, add component tests for key UI states, and reduce e2e tests to only the most critical user flows."
---

# Hook

A developer changes one utility function late on a Friday. They refresh the browser, see the main feature still works, and ship it. Monday morning: three other features that used that same function are broken in production. No tests caught it. The deployment is reverted. A post-mortem is written.

This is a preventable story. Tests exist to prevent it.

# Lore Introduction

The Arcane Academy's artificers do not forge artefacts and immediately sell them at the market. First, they test. They apply stress runes, expose the artefact to conditions it might face in the field, and verify that it behaves as designed. An untested artefact is a liability, not a product.

Your code is an artefact. Your tests are the stress runes.

# Core Learning

## Concept Introduction

**Frontend testing** means writing code that verifies your application works correctly — automatically, repeatedly, and without manual browser interaction. Tests provide a safety net that catches problems before they reach users.

Three types of frontend tests form the **testing pyramid**:

```
        /\
       /  \
      / E2E \      ← Few, slow, expensive, high coverage
     /________\
    /          \
   / Component  \  ← Moderate, medium speed
  /______________\
 /                \
/    Unit Tests    \ ← Many, fast, cheap, focused
/__________________\
```

**Unit tests** test a single function or hook in isolation. No browser, no rendering, no network.

**Component tests** render a component and test its output and interactions. Uses a virtual DOM.

**End-to-end tests** run the full application in a real browser and simulate real user journeys.

## Why Test Frontends?

**1. Regression prevention.** Once you write a test for a bug, that bug can never silently reappear. The test will catch it.

**2. Refactoring confidence.** Tests let you change implementation details without fear — if the tests still pass, the behaviour is preserved.

**3. Living documentation.** Tests describe *what* the code does from a user's perspective. They are more reliable than comments because they fail if the code changes.

**4. Faster feedback.** A failing test in 50ms is faster than opening a browser, navigating to a feature, and manually verifying it.

## The Testing Pyramid in Practice

```ts
// Unit test — tests a pure function directly
// Fast (< 1ms), no browser needed
import { formatPrice } from '@/utils/formatPrice';
test('formats GBP price', () => {
  expect(formatPrice(49.99, 'GBP')).toBe('£49.99');
});

// Component test — renders UI, tests interactions
// Medium speed (< 100ms), virtual DOM
test('shows error message when email is invalid', async () => {
  render(<LoginForm />);
  await userEvent.type(screen.getByLabelText('Email'), 'not-an-email');
  await userEvent.click(screen.getByRole('button', { name: 'Sign in' }));
  expect(screen.getByText('Please enter a valid email')).toBeInTheDocument();
});

// E2e test — full browser, real app
// Slow (seconds), requires running app
test('user can log in and see dashboard', async ({ page }) => {
  await page.goto('/login');
  await page.fill('[name="email"]', 'user@example.com');
  await page.fill('[name="password"]', 'password');
  await page.click('button[type="submit"]');
  await expect(page).toHaveURL('/dashboard');
});
```

## What to Test

Not everything needs a test. Focus testing effort on:
- **Utility functions:** formatters, validators, calculators — any pure function
- **Custom hooks:** stateful logic shared across components
- **Key user interactions:** login, checkout, form submission
- **Error and edge cases:** empty states, loading states, error messages
- **Business-critical paths:** anything that costs money if it breaks

Do not write tests for:
- Simple wrappers that just pass props through
- Pure rendering with no logic
- Third-party library behaviour

## Common Mistakes

**No tests at all.** The biggest mistake. Even a handful of tests for critical paths is vastly better than none.

**Testing only the happy path.** Edge cases and error states are where bugs hide. A `formatPrice` test that only tests a valid number misses the case where `price` is `null` or `NaN`.

**Over-relying on e2e tests.** E2e tests are valuable but slow and flaky. A balanced pyramid gives faster feedback and more reliability.

## Mini Summary

Frontend tests are automated code that verifies your application's behaviour. They form a pyramid: many fast unit tests, moderate component tests, few e2e tests. Testing brings regression prevention, refactoring confidence, and living documentation. Every frontend developer should be writing tests regularly.

# Guided Practice Quest

Work through the steps to understand the testing pyramid and identify which type of test is appropriate for different scenarios.

# Solo Practice Quest

Think about a React application you have built or worked with. Identify:

1. One pure utility function that would be easy to unit test
2. One component interaction that would benefit from a component test
3. One critical user flow that deserves an e2e test

For each, write one sentence explaining why that test type is appropriate. Then estimate: if you had 50 tests total, how would you distribute them across the three types?

# Integration

**Psychology — Cognitive Safety:** Research on developer productivity shows that developers produce better-quality code when they feel psychologically safe to experiment and refactor. A good test suite provides "technical psychological safety" — developers can make bold changes knowing the tests will catch mistakes. Teams without tests become risk-averse and slow.

**Mathematics — Proof by Contradiction:** Testing is analogous to proof by contradiction: you describe what *should* be true (`expect(formatPrice(10)).toBe('£10.00')`), then verify no contradiction exists. When a test fails, it has found a contradiction — a proof that the implementation does not match the specification.

# Lore Conclusion

The Academy has a rule for every artefact that leaves its gates: it must pass the stress rune trials. Not because every artefact will face all of those conditions — but because the artificer who ran those trials *knows* their artefact's limits. Write your tests not because every path will be taken, but because a developer who has tested their code knows what their code does. And that knowledge is the foundation of professional craftsmanship.

---
