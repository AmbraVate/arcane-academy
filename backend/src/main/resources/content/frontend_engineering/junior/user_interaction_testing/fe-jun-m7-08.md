---
id: fe-jun-m7-08
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m7
moduleTitle: "Module 7: Frontend Testing"
moduleGlyph: "🧪"
moduleSortOrder: 7
topicSlug: user_interaction_testing
topicTitle: "User Interaction Testing"
topicSortOrder: 3
lesson: testing_async_behaviour
title: "Testing Async Behaviour"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m7-07]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what waitFor does and when to use it"
    - "Explains why findBy queries are preferred over waitFor for element appearance"
    - "Describes how to mock fetch in tests using vi.mock or msw"
    - "Identifies the three states (loading/success/error) that need async tests"
  keywords: [waitFor, findBy, mock, fetch, async, loading, success, error, msw, vi.mock]
  modelAnswer: |
    waitFor polls a callback until it stops throwing, useful for complex assertions that depend on async side effects. findBy queries are simpler alternatives for waiting until an element appears. Mocking fetch prevents real network calls in tests — use vi.mock for quick inline mocking or msw (Mock Service Worker) for realistic network interception. All three states of data fetching (loading, success, error) should be tested: the loading spinner appears, then data renders on success, then an error message appears on failure.
guidedSteps:
  - id: fe-jun-m7-08-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "After an API call resolves, a list of users appears. Which is the cleanest way to wait for the list to render?"
    inputConfig:
      options:
        - "setTimeout(() => expect(...), 1000)"
        - "await screen.findByRole('list')"
        - "await waitFor(() => expect(screen.getByRole('list')).toBeInTheDocument())"
        - "expect(screen.getByRole('list')).toBeInTheDocument() — RTL waits automatically"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["await screen.findByRole('list')"]
      rejectedFeedback: "findBy queries poll the DOM until the element appears (or timeout after 1000ms by default). This is the cleanest approach for 'wait for something to appear'. waitFor works too but requires wrapping the assertion in a callback. setTimeout hardcodes a wait time and is fragile. getBy is synchronous — it fails immediately if the element isn't present."
    hint: "Which query family returns a Promise that resolves when the element appears?"
    reflectionPrompt: "findBy is basically waitFor + getBy combined into one clean call. Prefer it when you're waiting for a specific element to appear."
  - id: fe-jun-m7-08-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Explain why you should mock fetch (or use msw) in component tests, rather than making real API calls."
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [network, real, slow, flaky, control, deterministic, server, dependency, mock]
      rejectedFeedback: "Real API calls make tests slow (network latency), flaky (server might be down), and non-deterministic (server data changes). Tests should be fast, reliable, and independent of external systems. Mocking fetch gives you control: you decide what the API returns for each test, including error cases that are hard to reproduce with a real server."
    hint: "What happens to your tests if the API server is down? What if it's slow? What if the data changes?"
    reflectionPrompt: "Tests are a safety net. If your safety net depends on a live server, it'll fail when the server is down — exactly when you need confidence most. Mock the network."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Your component shows a loading spinner while fetching, then replaces it with data. What should you assert in sequence?"
    options:
      - "Only assert the final data — the loading state is irrelevant"
      - "Assert spinner is present, then assert it's gone and data is present"
      - "Assert data is present — RTL handles the loading state automatically"
      - "Assert the fetch call was made, then assert the data"
    correctIndex: 1
    feedback: "Test all meaningful states. First: getByRole('status') or getByLabelText('loading') confirms the loading state. Then: await findByText('User: Aria') confirms the success state. Then: queryByRole('status') is null confirms the spinner is gone. This verifies the full user experience, not just the happy ending."
retrieval:
  recall: "What is the difference between findBy and waitFor in RTL?"
  explain: "Why should tests not make real HTTP requests to a live server?"
  mistakeId:
    code: |
      test('loads users', async () => {
        render(<UserList />);
        await new Promise(resolve => setTimeout(resolve, 500));
        expect(screen.getByText('Aria')).toBeInTheDocument();
      });
    answer: "setTimeout with an arbitrary delay is fragile: too short and the fetch hasn't completed; too long and tests are slow. The delay will need constant adjustment as network conditions or mock speeds change. Replace with: await screen.findByText('Aria') — RTL polls efficiently until the element appears (default 1000ms timeout) without arbitrary delays."
---

# Hook

Your component test renders a list, but the list appears after an API call. You check immediately — nothing there. You add a `setTimeout`. Tests pass locally but fail on CI (slower machine, timeout too short). You increase the timeout. Tests pass, but now they're 2 seconds slower.

There is a better way. RTL has purpose-built tools for async behaviour.

# Lore Introduction

*"The oracle does not speak immediately,"* says Seer Vael. *"You ask, then wait. But waiting blindly — counting heartbeats, hoping for enough — is not patience. It is hope."*

She shows you a focusing lens. *"This lens turns toward the oracle and watches. The moment speech begins, it signals you. You do not count. You listen."*

That lens is `findBy`.

# Core Learning

## Concept Introduction

When a component fetches data, three things happen in sequence:
1. **Loading** — spinner or skeleton shows
2. **Success** — data renders
3. **Error** — error message shows (if fetch fails)

All three states need tests. The challenge: assertions run before async operations complete.

**Tools for async tests:**

| Tool | What it does |
|---|---|
| `await screen.findBy*` | Polls DOM until element appears |
| `await waitFor(() => ...)` | Polls until assertion doesn't throw |
| `vi.mock` / `vi.fn()` | Mock the fetch/service call |
| `msw` (Mock Service Worker) | Intercept real fetch calls at network level |

## Why It Matters

Without proper async handling, tests are:
- **Flaky** — sometimes the data loads in time, sometimes not
- **Slow** — `setTimeout` adds arbitrary wait times
- **Misleading** — tests pass even when async rendering is broken

## Worked Example

```js
// Mocking with vi.mock (Vitest)
import { render, screen } from '@testing-library/react';
import { userService } from '../services/userService';
import { UserList } from './UserList';

vi.mock('../services/userService');

test('shows loading then user list', async () => {
  // Mock resolves after a tick
  userService.getUsers.mockResolvedValue([
    { id: 1, name: 'Aria' },
    { id: 2, name: 'Bran' },
  ]);

  render(<UserList />);

  // Loading state
  expect(screen.getByRole('status', { name: /loading/i })).toBeInTheDocument();

  // Wait for success state
  expect(await screen.findByText('Aria')).toBeInTheDocument();
  expect(screen.getByText('Bran')).toBeInTheDocument();

  // Loading spinner is gone
  expect(screen.queryByRole('status', { name: /loading/i })).not.toBeInTheDocument();
});

test('shows error message when fetch fails', async () => {
  userService.getUsers.mockRejectedValue(new Error('Network error'));

  render(<UserList />);

  expect(await screen.findByText(/failed to load/i)).toBeInTheDocument();
});
```

## Common Mistakes

- **Using `getBy` for async elements.** It fails immediately if the element isn't present. Use `findBy`.
- **Using `setTimeout`.** Arbitrary delays are fragile. Use `findBy` or `waitFor`.
- **Not testing the error state.** Happy-path tests aren't enough. Mock a rejected promise and verify the error UI.
- **Forgetting `async` on the test function.** If you `await` inside a non-async function, you get a SyntaxError or silent bugs.

## Mini Summary

- Use `await screen.findBy*` to wait for async elements to appear
- Mock fetch/service calls with `vi.mock` or msw — never make real network calls in unit tests
- Test all three async states: loading, success, error
- Always mark test functions `async` when using `await`

# Guided Practice Quest

Work through the two guided steps to verify your understanding of when and why to use async testing tools.

# Solo Practice Quest

Write tests for a `<WeatherCard city="London" />` component that fetches weather data. You should test:
1. A loading indicator appears while fetching
2. Temperature is displayed after a successful fetch
3. An error message appears if the fetch fails

Describe how you would mock the weather service for each case.

# Integration

**Mathematics — Polling and Convergence**

RTL's `findBy` implements exponential backoff polling: it checks the DOM at increasing intervals until the assertion passes or the timeout expires. This is the same mathematical principle used in retry algorithms, network protocols (TCP retransmission), and consensus algorithms. The key insight: polling at a fixed short interval wastes CPU; polling at increasing intervals balances responsiveness with efficiency. Understanding that `findBy` is a bounded polling loop — not magic — helps you reason about its timeout (1000ms by default, configurable) and why increasing it solves some but not all flakiness problems.

# Lore Conclusion

*"The lens waits intelligently,"* Seer Vael says as the test passes cleanly. *"Not blindly. It watches for the signal, then reports. Your tests are now honest about what they wait for — and why."*

The async testing rune hums steadily in your grimoire.

---
