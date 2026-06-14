---
id: fe-jun-m7-11
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
lesson: good_test_design
title: "Good Test Design"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m7-10]
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what 'one assertion per test concept' means"
    - "Explains what 'testing behaviour not internals' means"
    - "Describes what a descriptive test name should contain"
    - "Identifies characteristics of a well-designed test"
  keywords: [behaviour, implementation, name, describe, arrange, act, assert, single, responsibility]
  modelAnswer: |
    Good test design means: each test has one clear responsibility (one behaviour, one scenario). The test name describes that behaviour: 'shows error message when email field is empty'. Tests verify what the user can observe (visible text, accessible roles, called handlers) — not internal state, private methods, or CSS class names. The arrange-act-assert pattern keeps tests readable: set up the scenario, perform the action, check the outcome. Well-designed tests survive refactoring because they don't depend on implementation details that change when you improve the code.
guidedSteps:
  - id: fe-jun-m7-11-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Which test name best describes the test's intent?"
    inputConfig:
      options:
        - "test('Button works')"
        - "test('renders correctly')"
        - "test('shows disabled state when isLoading prop is true')"
        - "test('Button component test')"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["test('shows disabled state when isLoading prop is true')"]
      rejectedFeedback: "'Button works' and 'renders correctly' say nothing specific. 'Button component test' is a folder label, not a behaviour description. 'shows disabled state when isLoading prop is true' describes the specific behaviour, the triggering condition, and the expected outcome — all from the test name alone."
    hint: "A good test name reads like a specification: given X, when Y happens, then Z is visible."
    reflectionPrompt: "When a test fails at 2am in CI, the test name is your first clue. 'Button works FAILED' tells you nothing. 'shows disabled state when isLoading prop is true FAILED' tells you exactly what to investigate."
  - id: fe-jun-m7-11-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "A test checks: the form renders, the email input exists, the button exists, submitting shows a success message, and the handler is called. What is wrong with putting all of this in one test?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [failure, which, single, responsibility, separate, describe, unclear, reason]
      rejectedFeedback: "When this test fails, you know something broke — but which thing? Did the form fail to render? Did the button disappear? Did the success message not appear? Each behaviour should be its own test with its own name. When any one of them fails, you know exactly what broke. One test should test one thing."
    hint: "If this test fails, how quickly can you tell which of the five things broke?"
    reflectionPrompt: "Test granularity is about failure clarity. Fine-grained tests give fine-grained failure information. A monolith test tells you the system broke, but not where."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which is testing behaviour rather than implementation?"
    options:
      - "expect(component.state.isOpen).toBe(true)"
      - "expect(screen.getByRole('dialog')).toBeInTheDocument()"
      - "expect(component.instance().toggleOpen).toHaveBeenCalled()"
      - "expect(wrapper.find('.modal-open')).toHaveLength(1)"
    correctIndex: 1
    feedback: "getByRole('dialog') checks what is visible in the DOM — what the user can see and interact with. The other options check internal state, internal method calls, or CSS class names — all implementation details that can change without changing user-visible behaviour."
retrieval:
  recall: "What is the arrange-act-assert pattern in testing?"
  explain: "Why should tests not check internal component state or CSS class names?"
  mistakeId:
    code: |
      test('component', () => {
        const { container } = render(<UserCard user={mockUser} />);
        expect(container.querySelector('.user-name')).toBeInTheDocument();
        expect(container.querySelector('.user-avatar')).toBeInTheDocument();
        render(<UserCard user={null} />);
        expect(container.querySelector('.user-name')).toBeNull();
      });
    answer: "Three problems: 1) The test name 'component' describes nothing. 2) CSS class name queries (.user-name) are implementation details — renaming a class breaks the test even if the UI is identical. 3) Multiple scenarios in one test. Split into: 'shows user name when user is provided' (use getByText, not querySelector), 'shows avatar when user is provided', and 'shows nothing when user is null'."
---

# Hook

Two developers look at the same failing test. One says, "I know exactly what broke." The other says, "I have no idea where to start."

The difference is test design. The same assertion failure can be crystal-clear or completely opaque depending on how the test was written. Good test design is a skill that pays dividends every time CI turns red.

# Lore Introduction

*"A poorly labelled potion is dangerous,"* the Apothecary says, gesturing to a shelf of unlabelled vials. *"Is it a healing draught or a sleeping draught? You cannot know until you drink it — and by then it is too late."*

She picks up a neatly labelled vial: *"Antidote for Nightshade Poisoning — apply within one hour."*

*"Your tests are labels. Name them so that when one fails, you know immediately what the potion was for — and what went wrong."*

# Core Learning

## Concept Introduction

Good test design has three properties:

**1. One responsibility per test**
Each test verifies one specific behaviour. When it fails, you know exactly what broke.

**2. Descriptive names**
The test name is a specification: `'shows error when email is empty'`, `'calls onSubmit with form data when valid'`. Never: `'works'`, `'renders correctly'`, `'test 1'`.

**3. Behaviour over implementation**
Test what the user sees and does. Not internal state, CSS classes, or private methods.

**The Arrange-Act-Assert pattern:**
```js
test('shows error when submitted with empty email', async () => {
  // Arrange — set up the scenario
  const user = userEvent.setup();
  render(<LoginForm onSubmit={vi.fn()} />);

  // Act — perform the user action
  await user.click(screen.getByRole('button', { name: 'Log in' }));

  // Assert — verify the outcome
  expect(screen.getByRole('alert')).toHaveTextContent('Email is required');
});
```

## Why It Matters

Well-designed tests are:
- **Readable** — a new developer understands what they test without reading the component
- **Maintainable** — they break only when behaviour changes, not when internals change
- **Useful when failing** — the name tells you what broke; the assertion tells you what was expected

## Worked Example

**Poorly designed:**
```js
test('LoginForm', () => {
  const { container } = render(<LoginForm onSubmit={vi.fn()} />);
  expect(container.querySelector('.form-container')).toBeTruthy();
  expect(container.querySelector('input')).toBeTruthy();
  userEvent.click(container.querySelector('button'));
  expect(container.querySelector('.error')).toBeTruthy();
});
```

**Well designed:**
```js
describe('LoginForm', () => {
  test('renders an email input', () => {
    render(<LoginForm onSubmit={vi.fn()} />);
    expect(screen.getByLabelText('Email')).toBeInTheDocument();
  });

  test('shows an error message when submitted with empty email', async () => {
    const user = userEvent.setup();
    render(<LoginForm onSubmit={vi.fn()} />);
    await user.click(screen.getByRole('button', { name: 'Log in' }));
    expect(screen.getByRole('alert')).toHaveTextContent('Email is required');
  });

  test('calls onSubmit with email when form is valid', async () => {
    const user = userEvent.setup();
    const mockSubmit = vi.fn();
    render(<LoginForm onSubmit={mockSubmit} />);
    await user.type(screen.getByLabelText('Email'), 'aria@arcane.academy');
    await user.click(screen.getByRole('button', { name: 'Log in' }));
    expect(mockSubmit).toHaveBeenCalledWith({ email: 'aria@arcane.academy' });
  });
});
```

## Common Mistakes

- **Vague test names.** "renders correctly" tells you nothing when it fails.
- **Mega-tests.** One test with 15 assertions covering multiple behaviours is a maintenance burden and a debugging nightmare.
- **Testing CSS classes.** Classes change when you refactor styles. Test visible text and roles.
- **Skipping describe blocks.** `describe('ComponentName', () => { ... })` groups related tests and improves output readability.

## Mini Summary

- One test = one behaviour = one clear failure message
- Name tests as specifications: `'shows X when Y'`, `'calls Z when A'`
- Use arrange-act-assert to keep tests readable
- Test what users see; avoid internal state and CSS class names

# Guided Practice Quest

Work through the two guided steps to practise identifying good vs bad test design and writing descriptive names.

# Solo Practice Quest

Given these poorly-named tests for a toggle component: `'test 1'`, `'renders'`, `'works when clicked twice'`. Rewrite the test names as proper specifications that describe the expected behaviour. Then describe how you'd split 'works when clicked twice' into two separate tests.

# Integration

**Philosophy — Specification by Example**

Good test names are examples of system behaviour — a form of executable specification. The BDD (Behaviour-Driven Development) movement formalised this with the Given-When-Then structure, which maps directly to Arrange-Act-Assert. Philosophically, this approaches tests as a form of knowledge: they encode what the system should do, in a form that can be mechanically verified. A test suite written in this style is not just a safety net — it is living documentation. The philosophical commitment is to precision: each test name is a precise claim about the system. When the test fails, the claim is falsified, and the investigation is narrowly scoped. This is the scientific method applied to software: falsifiable, specific, observable.

# Lore Conclusion

*"The labels are clear now,"* the Apothecary says, surveying the reorganised shelf. *"Each vial tells me what it contains, when to use it, and what to expect. When something goes wrong, I know exactly which vial to reach for."*

Your test suite is a well-labelled apothecary shelf.

---
