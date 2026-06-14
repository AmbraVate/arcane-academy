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

## Common Mistakes

- **Not disabling the submit button during submission**: Without `disabled={isSubmitting}`, a slow network causes users to click multiple times, sending duplicate requests — potentially double-charging or creating duplicate records.
- **Clearing the form on network error**: Clearing form data when the submission fails forces the user to re-type everything. Preserve the form state on error so they can retry with a single click.
- **Missing the `finally` block to reset `isSubmitting`**: If an async error is thrown and `setIsSubmitting(false)` is only in the `try` block, the button stays disabled forever after a failure. Always reset in `finally`.
- **Showing a raw API error message to the user**: Error objects from `catch` often contain technical stack traces or internal codes. Always translate server errors into user-friendly language before displaying them.

## Mental Model

Form submission is sending a package by courier, and the handler's job is everything a sensible sender does around the hand-off. `preventDefault` is declining the postal system's ancient default service (the full-page form POST that demolishes your SPA and everything in it) in favour of your own courier (the async request). Before dispatch, you check the package once more *as a whole* — final validation on the complete state, because individual fields were inspected as they were packed, but only now can you confirm nothing's missing and the contents agree with each other. Then the crucial discipline: the moment the courier takes the package, you *record that it's in transit* (the submitting state) — and that record drives real behaviour: you don't hand over a duplicate package because the doorbell rang twice (the disabled button preventing double-submission — with money, literally double-charging), and you show the tracking status honestly (spinner, "sending…") instead of a frozen interface that invites the second click. Delivery has three distinct outcomes, each with its own protocol. Confirmed receipt (success): tell the sender plainly, and only now is it safe to clear the packing table (reset the form) or send them onward. Refused at the door (server validation rejection): the recipient's specific objections get attached to the specific items they concern — per-field server errors flowing into the same display system as local ones — never read aloud as a raw courier docket (the unformatted API error toast). And lost in transit (network failure): you tell the sender it didn't arrive, you *keep their package intact on the table* — the typed data preserved, the cardinal rule — and you make trying again one button, not a re-packing job. Decline the default post, inspect whole, record in-transit, and script all three endings: that's a submit handler that can be trusted with a checkout.

## Why It Matters

Submission is the moment a form stops being local state and becomes a network transaction — and handling that transition properly is what separates demo forms from production ones:

- `preventDefault` is just the entry fee: real submission handling is a *state machine* — idle, submitting, succeeded, failed — and every state needs UI: a disabled button and spinner while in flight, a clear success signal, and failure handling that returns the user to a recoverable form, data intact
- Double-submission is a real money bug: a user double-clicking "Place order" on a slow connection fires two POSTs unless the submitting state disables the button — the canonical example of why the in-flight state isn't cosmetic
- The failure paths are where craft shows: network errors versus validation rejections versus server faults deserve different treatment (retry guidance, per-field error mapping, and a generic apology respectively), and the cardinal rule — *never lose the user's typed data on failure* — is violated by every form that clears or reloads on error
- Final validation gates the request: client checks run once more before sending (state can be invalid in ways individual field events missed), and the server's response feeds back into the same error display system, closing the loop this module built across three lessons

Checkout flows, signups, support tickets — every conversion event in every product passes through a submit handler. This lesson is where your forms start being trusted with real consequences.

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
