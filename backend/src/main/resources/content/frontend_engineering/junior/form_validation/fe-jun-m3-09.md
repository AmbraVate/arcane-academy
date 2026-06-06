---
id: fe-jun-m3-09
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m3
moduleTitle: "Module 3: Events and Forms"
moduleGlyph: "📝"
moduleSortOrder: 3
topicSlug: form_validation
topicTitle: "Form Validation"
topicSortOrder: 3
lesson: submission_handling
title: "Submission Handling"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Prevents default form submission and handles it in JavaScript"
    - "Disables the submit button while submitting to prevent double-submission"
    - "Handles both success and error states after submission"
    - "Provides meaningful feedback after submission completes"
  keywords: [submit, preventDefault, loading, disabled, success, error, double-submission, feedback]
  modelAnswer: |
    On submit: preventDefault(), validate all fields, show loading state (disable button),
    send to API, then handle success (reset form, show confirmation) or error (show server
    error message). Disable the submit button during submission to prevent double-clicks.
    Always give the user feedback — they submitted something and deserve to know what happened.
guidedSteps:
  - id: fe-jun-m3-09-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Why should the submit button be disabled while the form is submitting?
    inputConfig:
      options:
        - "To prevent the user from editing fields during submission"
        - "To prevent double-submission — clicking submit twice before the first response"
        - "Required for accessibility"
        - "To improve performance"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["To prevent double-submission — clicking submit twice before the first response"]
      rejectedFeedback: "Double-submission creates duplicate records, payments, or orders. Disabling the button during the async operation prevents the user from submitting again before the first request resolves."
    hint: "What happens if the user clicks submit twice while waiting for a response?"
    reflectionPrompt: "This is a real-world bug that causes real-world problems: duplicate payments, duplicate accounts, duplicate orders. Disabling during submission and showing a loading state communicates to the user that their action was received."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "After a successful form submission, what should your UI typically do?"
    options:
      - "Nothing — the form just stays as is"
      - "Show a confirmation, reset the form, and optionally redirect"
      - "Refresh the entire page"
      - "Show an error message to be safe"
    correctIndex: 1
    feedback: "Success state: show a confirmation ('Message sent!'), reset form values, and optionally redirect. Users need to know their action succeeded. Leaving the form unchanged after submission is confusing — they may try to submit again."

retrieval:
  recall: "Write the submit handler pattern: validate → loading state → API call → success/error handling."
  explain: "Why is it important to handle both network errors and server validation errors separately?"
  mistakeId:
    code: "No loading state on submit button — user double-clicks and creates two records"
    answer: "Add isSubmitting state. Set true before the API call, false after. Disable the button: disabled={isSubmitting}. Show loading indicator inside the button."
---

# Hook

A form that submits silently — no loading indicator, no confirmation, no error — teaches users to not trust it. Submission handling is where the UX promise is kept or broken.

# Lore Introduction

*"The courier who takes your message and vanishes with no receipt,"* says Master Aelindra, *"is not a reliable courier. Every submission deserves an acknowledgement."*

# Core Learning

## Concept Introduction

```jsx
function ContactForm() {
  const [form, setForm] = useState(initialState);
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitResult, setSubmitResult] = useState(null);  // 'success' | 'error' | null

  async function handleSubmit(e) {
    e.preventDefault();
    const errs = validate(form);
    if (Object.keys(errs).length > 0) { setErrors(errs); return; }

    setIsSubmitting(true);
    try {
      await submitForm(form);
      setSubmitResult('success');
      setForm(initialState);
    } catch {
      setSubmitResult('error');
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      {/* fields */}
      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? 'Sending…' : 'Send'}
      </button>
      {submitResult === 'success' && <p>Message sent!</p>}
      {submitResult === 'error'   && <p>Failed — please try again.</p>}
    </form>
  );
}
```

## Mini Summary
- ✔ validate → isSubmitting → try/catch → success/error state
- ✔ Disable submit button during submission (prevent double-click)
- ✔ Reset form on success; show error on failure
- ✔ Always give the user clear feedback on what happened

# Solo Practice Quest

Build a complete "subscribe to newsletter" form: email input, validated on submit, with loading state, success confirmation (shown after, hiding the form), and error handling. The submit button shows "Subscribing…" during the async mock.

# Integration

**Psychology — Closure and the Zeigarnik Effect:** The Zeigarnik effect shows that incomplete tasks remain in working memory until resolved. A form submission without feedback leaves the task "open" — the user's attention is captured by uncertainty. Success/error feedback closes the loop, releasing working memory. Good form UX is closure: every action has a visible resolution.

# Lore Conclusion

*"Submit. Acknowledge. Confirm. Error or success — the user deserves to know. Silence is not neutral. Silence is failure."*

---
