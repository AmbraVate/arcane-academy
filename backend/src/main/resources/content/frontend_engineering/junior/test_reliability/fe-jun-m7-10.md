---
id: fe-jun-m7-10
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m7
moduleTitle: "Module 7: Frontend Testing"
moduleGlyph: "🧪"
moduleSortOrder: 7
topicSlug: test_reliability
topicTitle: "Test Reliability"
topicSortOrder: 4
lesson: flaky_tests
title: "Flaky Tests"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m7-09]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines a flaky test and explains why it's harmful"
    - "Identifies timing issues as the most common source of flakiness"
    - "Describes how to fix a timing-based flaky test"
    - "Explains why flaky tests erode team trust in the test suite"
  keywords: [flaky, timing, async, setTimeout, findBy, waitFor, deterministic, trust, CI]
  modelAnswer: |
    A flaky test passes sometimes and fails sometimes without any code changes. It's harmful because it creates noise in CI — developers learn to ignore failures, which means real bugs get ignored too. The most common cause in frontend tests is timing: an assertion runs before an async operation completes. Fix timing flakiness by replacing arbitrary setTimeout delays with findBy queries or waitFor. Other sources include test order dependency (tests that share state) and non-deterministic data. Flaky tests erode team trust: if failures are random, the suite stops being a reliable safety net.
guidedSteps:
  - id: fe-jun-m7-10-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A test passes 8 out of 10 runs with no code changes. This is called a:"
    inputConfig:
      options:
        - "Passing test with edge cases"
        - "Flaky test"
        - "Integration test"
        - "Slow test"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Flaky test"]
      rejectedFeedback: "A flaky test is non-deterministic — it passes or fails based on timing, test order, or external state rather than on the actual correctness of the code. Flaky tests are arguably worse than no tests: they give false confidence when they pass and false alarms when they fail."
    hint: "What do you call a test that doesn't consistently produce the same result?"
    reflectionPrompt: "A test that passes 80% of the time provides 80% of no confidence. The whole point of a test suite is determinism: the same code should always produce the same result."
  - id: fe-jun-m7-10-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "A test has this line: `await new Promise(r => setTimeout(r, 300))`. Why is this a common source of flakiness and how would you fix it?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [timing, delay, slow, CI, findBy, waitFor, arbitrary, timeout]
      rejectedFeedback: "The 300ms delay assumes the async operation completes within 300ms. On a slow CI machine or under load, it might take 400ms — and the test fails. The fix: replace the delay with `await screen.findByText(...)` or `await waitFor(...)`. These poll until the condition is met (up to a configurable timeout) rather than waiting a fixed duration."
    hint: "What happens to this 300ms delay on a CI machine that's running 20 tests in parallel?"
    reflectionPrompt: "Fixed time delays are a form of temporal coupling — you're coupling your test to a specific execution speed. Polling-based waiting is decoupled from speed."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Tests A, B, and C all pass when run individually, but B fails when run after A. What is the most likely cause?"
    options:
      - "Test B has a timing issue"
      - "Test A leaves shared state that B depends on"
      - "The test framework has a bug"
      - "B is a flaky test unrelated to A"
    correctIndex: 1
    feedback: "Test order dependency means tests share state — a global variable, un-cleared mock, or DOM not cleaned up between tests. Each test should be completely independent: it sets up its own state, runs, and cleans up. RTL's cleanup (automatic with Vitest/Jest) handles DOM cleanup; vi.clearAllMocks() or beforeEach resets handle mock cleanup."
retrieval:
  recall: "What are the two most common causes of flaky frontend tests?"
  explain: "Why are flaky tests sometimes described as 'worse than no tests'?"
  mistakeId:
    code: |
      test('shows user name after load', async () => {
        render(<UserProfile id={1} />);
        await new Promise(r => setTimeout(r, 500));
        expect(screen.getByText('Aria Thornwood')).toBeInTheDocument();
      });
    answer: "The 500ms delay is an arbitrary guess. On a fast machine it may be too long (slow test). On a slow CI machine it may be too short (flaky test). Replace with: expect(await screen.findByText('Aria Thornwood')).toBeInTheDocument(). findBy polls until the element appears (default 1000ms), is faster when the element loads quickly, and doesn't fail just because a machine is slow."
---

# Hook

CI is red. You re-run. Green. You push again, the same commit. Red again. The test output says the same assertion failed — but nothing changed.

Your test suite has a flaky test. And now, every time CI goes red, your team wonders: real bug, or noise? The safety net is no longer trustworthy.

# Lore Introduction

*"An unreliable oracle is more dangerous than no oracle,"* warns the Diviner. *"When the oracle speaks true, you act. When it lies, you act on a lie. But worse — once it has lied to you, you never know which it is. You stop trusting it entirely."*

She gestures to a crystal that pulses irregularly. *"A flaky test is this crystal. Fix it, or remove it. But never tolerate it."*

# Core Learning

## Concept Introduction

A **flaky test** is a test that produces different results (pass/fail) across runs without any change to the code being tested.

**Common causes in frontend tests:**

| Cause | Example | Fix |
|---|---|---|
| **Timing** | `setTimeout(r, 300)` — not long enough | Use `findBy` / `waitFor` |
| **Test order dependency** | Test A sets global state that B reads | Use `beforeEach` to reset; avoid shared state |
| **Un-cleared mocks** | Mock from test A bleeds into test B | `vi.clearAllMocks()` in `afterEach` |
| **External dependencies** | Real API call returns different data | Mock the API |
| **Date/time** | `new Date()` produces different values | Mock `Date.now()` or use fixed timestamps |

## Why It Matters

Flaky tests erode trust. Once developers learn that "CI failing" might just be noise, they start ignoring failures — including real failures. The entire purpose of a test suite is to be a reliable signal. A flaky test turns a signal into noise.

## Worked Example

**Flaky (timing issue):**
```js
test('shows data after load', async () => {
  render(<DataTable />);
  await new Promise(r => setTimeout(r, 500)); // arbitrary!
  expect(screen.getByText('Row 1')).toBeInTheDocument();
});
```

**Fixed:**
```js
test('shows data after load', async () => {
  render(<DataTable />);
  expect(await screen.findByText('Row 1')).toBeInTheDocument();
});
```

**Flaky (test order dependency):**
```js
// Bad: global state shared between tests
let mockData = [];

test('adds item', () => {
  mockData.push('item');
  expect(mockData).toHaveLength(1);
});

test('starts empty', () => {
  expect(mockData).toHaveLength(0); // FAILS if run after 'adds item'
});
```

**Fixed:**
```js
// Each test creates its own data
test('adds item', () => {
  const data = [];
  data.push('item');
  expect(data).toHaveLength(1);
});

test('starts empty', () => {
  const data = [];
  expect(data).toHaveLength(0);
});
```

## Common Mistakes

- **Accepting flakiness as "just how tests are."** Every flaky test has a root cause. Find it and fix it.
- **Increasing the timeout to fix timing flakiness.** This makes tests slower without fixing the real problem.
- **Sharing mutable state between tests.** Always reset state in `beforeEach`.
- **Making real network calls.** External services make tests non-deterministic by nature.

## Mini Summary

- A flaky test is non-deterministic — it passes sometimes and fails sometimes
- Most common causes: timing (use `findBy`), shared state (use `beforeEach`), un-cleared mocks (`vi.clearAllMocks()`)
- Flaky tests erode suite trust — fix or remove them immediately
- Never accept "just re-run it" as a solution

# Guided Practice Quest

Work through the two guided steps to diagnose common flaky test patterns and understand their fixes.

# Solo Practice Quest

You have a test that fails every 5th run or so. Describe your debugging process: what would you look for first, what tools would you use, and what are the three most likely causes you'd investigate?

# Integration

**Psychology — Alarm Fatigue**

Alarm fatigue is a well-documented phenomenon in medicine and aviation: when alarms trigger too frequently — especially for non-critical events — practitioners begin to habituate and ignore them. Studies show that ICU nurses ignore up to 85-99% of clinical alarms because so many are false positives. The same dynamic applies to test suites: once developers learn that red CI might just be a flaky test, they stop treating red as a signal. Flaky tests inject false positives into the signal pipeline. The psychological mechanism (habituation to noise) means that even one or two chronic flaky tests can corrupt the entire team's response to failures. A clean test suite with zero tolerance for flakiness is a psychological investment in team responsiveness.

# Lore Conclusion

*"The crystal is steady now,"* the Diviner says, watching the test pass five times in a row. *"It no longer lies. When it speaks, you can trust it. That trust is what makes it valuable."*

The reliability seal is affixed. The oracle is trustworthy again.

---
