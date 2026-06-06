---
id: fe-jun-m7-07
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
lesson: firing_events
title: "Firing Events"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m7-06]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the difference between userEvent and fireEvent"
    - "Explains why userEvent is preferred for simulating user behaviour"
    - "Shows correct async usage of userEvent"
    - "Describes what events userEvent.type() fires internally"
  keywords: [userEvent, fireEvent, click, type, keyboard, async, await, realistic, event, simulate]
  modelAnswer: |
    userEvent simulates realistic user interactions — clicking a button fires mousedown, mouseup, and click events in sequence, exactly as a browser would. fireEvent dispatches a single synthetic event and nothing else. userEvent gives more realistic tests that catch bugs which only occur when the full event sequence fires (e.g., onMouseDown handlers, focus/blur chains). Since userEvent v14, all methods are async — they must be awaited. userEvent.type() fires keydown, keypress, keyup, and input events for each character, simulating real keyboard input.
guidedSteps:
  - id: fe-jun-m7-07-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Which is the correct way to simulate a user clicking a button with userEvent v14+?"
    inputConfig:
      options:
        - "userEvent.click(screen.getByRole('button'))"
        - "await userEvent.click(screen.getByRole('button'))"
        - "fireEvent.click(screen.getByRole('button'))"
        - "screen.getByRole('button').click()"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["await userEvent.click(screen.getByRole('button'))"]
      rejectedFeedback: "userEvent v14 made all methods async. Forgetting await means the interaction hasn't completed before your assertions run — tests may pass for the wrong reason or fail inconsistently. Always await userEvent calls."
    hint: "userEvent v14 changed all methods to return Promises."
    reflectionPrompt: "The async nature of userEvent reflects real browser behaviour — user interactions trigger chains of events that resolve asynchronously. The await ensures your assertion runs after the full event chain completes."
  - id: fe-jun-m7-07-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Why would a bug that only triggers on mousedown go undetected if you use fireEvent.click() instead of userEvent.click()?"
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [mousedown, event, sequence, chain, realistic, single, full, fire]
      rejectedFeedback: "fireEvent.click() dispatches exactly one click event. userEvent.click() fires the full sequence: mouseover, mousemove, mousedown, mouseup, click, focus — mirroring what a browser does. A handler attached to mousedown would never be triggered by fireEvent.click(), but would be triggered by userEvent.click(). Tests using fireEvent.click() give false confidence for interactions involving mousedown."
    hint: "When a user clicks, how many browser events actually fire? What does fireEvent.click() fire?"
    reflectionPrompt: "fireEvent is useful for testing that a specific event handler is wired up. userEvent is for testing that the user can accomplish a task. The distinction matters."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You need to simulate a user typing 'hello' into a text input. Which is correct?"
    options:
      - "fireEvent.change(input, { target: { value: 'hello' } })"
      - "await userEvent.type(input, 'hello')"
      - "input.value = 'hello'"
      - "userEvent.input(input, 'hello')"
    correctIndex: 1
    feedback: "userEvent.type() simulates real keyboard typing — it fires keydown, keypress, input, and keyup for each character. fireEvent.change() fires a single change event without the full keyboard sequence. For testing forms that respond to individual keystrokes, userEvent.type() is the accurate choice."
retrieval:
  recall: "Name two differences between userEvent and fireEvent."
  explain: "Why must userEvent calls be awaited in tests?"
  mistakeId:
    code: |
      test('counter increments when clicked', () => {
        render(<Counter />);
        userEvent.click(screen.getByRole('button', { name: 'Increment' }));
        expect(screen.getByText('Count: 1')).toBeInTheDocument();
      });
    answer: "Missing await before userEvent.click(). Without await, the click hasn't completed before the assertion runs. The assertion may see 'Count: 0' instead of 'Count: 1', making the test flaky or failing. Fix: `await userEvent.click(...)`. Also, the test function needs to be async: `test('...', async () => { ... })`."
---

# Hook

Your toggle button test passes locally. On CI it fails intermittently — sometimes the click hasn't registered before the assertion runs. You added a `fireEvent.click` and it always seems to work, but somehow a bug slipped through where the button only responds correctly on mousedown.

Both problems have the same root: you're not simulating user interactions realistically enough.

# Lore Introduction

*"You poked the rune with a stick,"* Enchanter Thoss observes drily. *"It activated — but only because you struck the activation point directly. A real user traces their finger across the glyph, triggering the recognition sequence."*

He draws the full gesture sequence on the board. *"Test as the user acts. Not as you think it's convenient."*

# Core Learning

## Concept Introduction

RTL provides two ways to fire events:

| | `fireEvent` | `userEvent` |
|---|---|---|
| **Events fired** | One synthetic event | Full realistic event sequence |
| **Async** | No | Yes (v14+) — must `await` |
| **Keyboard simulation** | No real keystrokes | Full key events per character |
| **Use case** | Low-level, specific event tests | Simulating real user behaviour |

**Setup:**
```bash
npm install --save-dev @testing-library/user-event
```

```js
import userEvent from '@testing-library/user-event';

// Set up once per test or describe block
const user = userEvent.setup();
```

## Why It Matters

`userEvent` catches bugs that `fireEvent` misses because it fires the same event sequences a browser fires. Code that handles `onMouseDown` separately from `onClick`, or that depends on focus/blur order, will only be exercised correctly by `userEvent`.

## Worked Example

```jsx
// Counter.jsx
function Counter() {
  const [count, setCount] = useState(0);
  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={() => setCount(c => c + 1)}>Increment</button>
    </div>
  );
}
```

```js
// Counter.test.jsx
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { Counter } from './Counter';

test('increments count when button is clicked', async () => {
  const user = userEvent.setup();
  render(<Counter />);

  expect(screen.getByText('Count: 0')).toBeInTheDocument();
  await user.click(screen.getByRole('button', { name: 'Increment' }));
  expect(screen.getByText('Count: 1')).toBeInTheDocument();
});

test('user can type into an input', async () => {
  const user = userEvent.setup();
  render(<input placeholder="Enter name" />);

  await user.type(screen.getByPlaceholderText('Enter name'), 'Aria');
  expect(screen.getByDisplayValue('Aria')).toBeInTheDocument();
});
```

## Common Mistakes

- **Missing `async`/`await`.** `userEvent` methods are Promises. Without `await`, assertions run before interactions complete.
- **Using `fireEvent` for user actions.** Reserve `fireEvent` for testing specific DOM events in isolation. Use `userEvent` for everything that simulates what a human would do.
- **Not calling `userEvent.setup()`.** The `setup()` call returns a bound user instance — it's the modern pattern (v14+) and handles cleanup properly.
- **Calling `userEvent.click()` on non-interactive elements.** `userEvent` simulates pointer events. Non-interactive elements (like divs without event listeners) won't respond meaningfully — use `fireEvent` for those edge cases.

## Mini Summary

- `userEvent` simulates realistic user behaviour; `fireEvent` fires one synthetic event
- All `userEvent` methods are async — always `await` them in `async` test functions
- Use `userEvent.setup()` to create a bound user instance
- `userEvent.click()`, `userEvent.type()`, `userEvent.keyboard()`, `userEvent.tab()` cover most interaction needs

# Guided Practice Quest

Work through the two guided steps to practise choosing between userEvent and fireEvent and to understand the async requirement.

# Solo Practice Quest

Write a test for a toggle button that switches between "Show" and "Hide" labels when clicked. Use `userEvent.setup()` and `await user.click()`. Describe what you would assert before and after the click.

# Integration

**Psychology — Ecological Validity in Testing**

Ecological validity measures how well a test mirrors real-world conditions. Low-validity tests (using `fireEvent.click()`) pass in the lab but miss bugs that only emerge in real usage. High-validity tests (using `userEvent` with full event sequences) more accurately represent what users actually do. This mirrors a fundamental challenge in psychological research: the more controlled an experiment, the less it resembles natural behaviour. Testing at a higher fidelity catches real bugs — at the cost of slightly more complex test setup. The tradeoff is usually worth it for interaction tests, just as real-world study designs are worth it for understanding human behaviour.

# Lore Conclusion

*"The glyph now responds to the full gesture,"* Thoss says, watching the test pass. *"Not just the poke. Your test is honest now — it tells you what a real user will experience."*

The interaction testing rune glows steadily.

---
