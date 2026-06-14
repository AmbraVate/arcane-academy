---
id: fe-jun-m7-09
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
lesson: testing_forms
title: "Testing Forms"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m7-07, fe-jun-m7-08]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Describes how to fill form inputs with userEvent.type()"
    - "Explains how to submit a form in a test"
    - "Describes what to assert for validation errors"
    - "Explains what to assert after a successful form submission"
  keywords: [type, click, submit, validation, error, success, label, input, button, handler]
  modelAnswer: |
    Fill inputs with await user.type(screen.getByLabelText('Email'), 'test@example.com'). Query inputs by label text for accessibility. Submit by clicking the submit button: await user.click(screen.getByRole('button', { name: /submit/i })). For validation: assert error messages appear when invalid data is submitted. For success: assert a success message or that a mock submit handler was called with the right data. Testing forms by label text also validates accessibility — if you can find inputs by label, screen reader users can too.
guidedSteps:
  - id: fe-jun-m7-09-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A form has an email input with `<label>Email</label>`. How should you query this input in your test?"
    inputConfig:
      options:
        - "screen.getByTestId('email-input')"
        - "screen.getByPlaceholderText('Enter email')"
        - "screen.getByLabelText('Email')"
        - "document.querySelector('input[type=email]')"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["screen.getByLabelText('Email')"]
      rejectedFeedback: "getByLabelText is the preferred way to query form inputs. It finds the input associated with a label — which also validates that the label-input association is correct for accessibility (screen reader users navigate forms by label). getByTestId works but tells you nothing about accessibility; getByPlaceholderText works but placeholder text is not a robust label."
    hint: "Which query method validates the label-input relationship at the same time?"
    reflectionPrompt: "If your test can find the input by label text, a screen reader user can too. Form tests are accessibility tests."
  - id: fe-jun-m7-09-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "A form shows an error 'Email is required' when submitted empty. Describe the test steps: what do you do, and what do you assert?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [click, submit, button, error, assert, findBy, text, required, validation]
      rejectedFeedback: "Steps: 1) render the form, 2) click the submit button without filling the email, 3) assert the error message appears. Use await user.click(screen.getByRole('button', { name: /submit/i })), then expect(await screen.findByText('Email is required')).toBeInTheDocument(). The error may appear asynchronously if validation runs after a state update."
    hint: "Think about: render, interact (without filling), then assert the error."
    reflectionPrompt: "Validation tests are some of the most valuable form tests — they verify that users can't submit bad data and that the error messaging is correct."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "After a successful form submission, your component navigates away. What should you assert?"
    options:
      - "Assert the form's internal validation state is true"
      - "Assert the mock submit handler was called with the correct data"
      - "Assert the form DOM is still present"
      - "Assert the submission count is 1"
    correctIndex: 1
    feedback: "The most useful assertion is that the submit handler (passed as a prop or mocked service) was called with the correct data. Use a vi.fn() as the handler, submit the form, then expect(mockHandler).toHaveBeenCalledWith({ email: 'test@example.com', ... }). This verifies the form collects and passes data correctly."
retrieval:
  recall: "What RTL query type should you use to find form inputs, and why?"
  explain: "Why should you test form validation error messages, not just successful submissions?"
  mistakeId:
    code: |
      test('submits form', async () => {
        const user = userEvent.setup();
        render(<LoginForm onSubmit={vi.fn()} />);
        await user.type(document.querySelector('input'), 'test@example.com');
        fireEvent.submit(document.querySelector('form'));
      });
    answer: "Two problems: 1) document.querySelector skips accessibility validation — use getByLabelText('Email'). 2) fireEvent.submit submits the form but doesn't simulate the user clicking the submit button — which fires focus, validation, and click events first. Use await user.click(screen.getByRole('button', { name: /submit/i })) instead. Also missing: assertions. Tests without assertions are always green — and meaningless."
---

# Hook

You tested that the form submits. But does it validate correctly? Does it show the right error for an empty email? Does it clear errors when you fix your input? Does the submit button disable while submitting?

A form without thorough tests is a form full of silent regressions. Users find the bugs instead of your test suite.

# Lore Introduction

*"The registration scroll must be filled precisely,"* the Registrar explains. *"Empty name? Rejected. Invalid glyph pattern? Rejected. But the scroll itself does not know what rejection looks like — you must verify that the scribe's marks appear in the right place."*

She hands you a checklist. *"Test the happy path. Then test every rejection. That is how you know the form is trustworthy."*

# Core Learning

## Concept Introduction

Form testing covers four scenarios:

| Scenario | What to test |
|---|---|
| **Happy path** | All fields filled correctly → submit handler called with right data |
| **Validation errors** | Required field missing → error message appears |
| **Field-level feedback** | Typing in a field → error clears when field becomes valid |
| **Submission states** | Submit button disabled/loading during submission |

## Why It Matters

Forms are the primary way users give your application data. Bugs in form validation cost real user trust — error messages that don't appear, fields that silently accept bad data, or submit buttons that fire twice. Form tests catch these regressions.

## Worked Example

```jsx
// LoginForm.jsx
function LoginForm({ onSubmit }) {
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');

  function handleSubmit(e) {
    e.preventDefault();
    if (!email) { setError('Email is required'); return; }
    onSubmit({ email });
  }

  return (
    <form onSubmit={handleSubmit}>
      <label htmlFor="email">Email</label>
      <input id="email" value={email} onChange={e => setEmail(e.target.value)} />
      {error && <p role="alert">{error}</p>}
      <button type="submit">Log in</button>
    </form>
  );
}
```

```js
// LoginForm.test.jsx
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { LoginForm } from './LoginForm';

describe('LoginForm', () => {
  test('calls onSubmit with email when form is valid', async () => {
    const user = userEvent.setup();
    const mockSubmit = vi.fn();
    render(<LoginForm onSubmit={mockSubmit} />);

    await user.type(screen.getByLabelText('Email'), 'aria@arcane.academy');
    await user.click(screen.getByRole('button', { name: 'Log in' }));

    expect(mockSubmit).toHaveBeenCalledWith({ email: 'aria@arcane.academy' });
  });

  test('shows error when submitted with empty email', async () => {
    const user = userEvent.setup();
    render(<LoginForm onSubmit={vi.fn()} />);

    await user.click(screen.getByRole('button', { name: 'Log in' }));

    expect(screen.getByRole('alert')).toHaveTextContent('Email is required');
  });

  test('does not call onSubmit when email is empty', async () => {
    const user = userEvent.setup();
    const mockSubmit = vi.fn();
    render(<LoginForm onSubmit={mockSubmit} />);

    await user.click(screen.getByRole('button', { name: 'Log in' }));

    expect(mockSubmit).not.toHaveBeenCalled();
  });
});
```

## Common Mistakes

- **Querying inputs by placeholder, not label.** Use `getByLabelText` — it validates the HTML label association too.
- **Not testing the submit handler call.** A form that shows no errors but doesn't call the handler has a bug. Always assert both: no error AND handler called.
- **Not testing validation errors.** The happy path alone gives false confidence.
- **Using `fireEvent.submit(form)` instead of clicking the button.** This bypasses validation that runs on button click events.

## Mini Summary

- Fill inputs with `await user.type(screen.getByLabelText('Field'), 'value')`
- Submit by clicking the button: `await user.click(screen.getByRole('button', { name: /submit/i }))`
- Test validation errors: submit without filling required fields, assert error messages appear
- Test success: assert mock handler was called with correct data

# Guided Practice Quest

Work through the two guided steps to confirm you understand form querying and validation testing strategies.

# Solo Practice Quest

Design tests for a registration form with fields: Name (required), Email (required, must contain @), Password (required, min 8 characters). List all the test cases you would write — including validation errors for each field and the success scenario.

# Integration

**Design — Forms as User Contracts**

A form is a contract between the user and the system: "if you give me this information in this format, I will do this for you." Form tests verify this contract from the user's perspective. Notably, querying by label text in tests also validates the design of the form — a well-designed form has clear labels that associate with their inputs (both for sighted users and screen readers). Testing a form thoroughly is simultaneously verifying its accessibility design. Bad form design (missing labels, unclear error messages) shows up as test friction before it shows up as user frustration.

# Lore Conclusion

*"The registration scroll is now trustworthy,"* the Registrar says, reviewing the test output. *"Every rejection is verified. Every acceptance is verified. When the apprentices arrive tomorrow, the scroll will serve them correctly — or your tests will tell you otherwise tonight."*

---
