---
id: fe-app-m6-07
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m6
moduleTitle: "Module 6: Accessibility Foundations"
moduleGlyph: "♿"
moduleSortOrder: 6
topicSlug: practical_accessibility
topicTitle: "Practical Accessibility"
topicSortOrder: 2
lesson: accessible_forms
title: "Accessible Forms"
sortOrder: 4
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Associates every input with a <label> using for/id or wrapping pattern"
    - "Uses fieldset and legend to group related inputs (radio buttons, checkboxes)"
    - "Adds descriptive error messages linked with aria-describedby"
    - "Marks required fields with both the required attribute and visible text (not just colour)"
    - "Uses autocomplete attributes to help users and password managers"
  keywords: [label, fieldset, legend, aria-describedby, aria-invalid, required, error, autocomplete, group, accessible]
  modelAnswer: |
    Accessible forms associate every input with a visible <label> via for/id.
    Related checkboxes/radios are grouped in <fieldset> with a <legend>.
    Errors use aria-invalid="true" and aria-describedby linking to a visible
    error message — screen readers announce the error when focus arrives.
    required fields are marked with both the HTML attribute and visible text
    (not colour alone). autocomplete helps users and password managers.
guidedSteps:
  - id: fe-app-m6-07-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A group of radio buttons asks "Preferred contact method: Email / Phone / Post". Which HTML structure correctly groups them?
    inputConfig:
      options:
        - "<div class='group'> containing the radio inputs"
        - "<fieldset> with a <legend>Preferred contact method</legend>"
        - "<section aria-label='Preferred contact method'>"
        - "Each radio in its own <form>"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["<fieldset> with a <legend>Preferred contact method</legend>"]
      rejectedFeedback: "<fieldset> + <legend> groups related inputs. Screen readers announce the legend as the group label before reading each radio option: 'Preferred contact method. Email, radio button 1 of 3.' Without fieldset/legend, users hear three unlabelled options with no context about what they're choosing."
    hint: "HTML has a semantic element specifically for grouping related form controls."
    reflectionPrompt: "fieldset/legend is one of the most underused accessibility tools. Every set of radio buttons or checkboxes should be inside a fieldset with a legend. It provides the group context that makes individual options meaningful. Individual labels alone are insufficient for radio groups."

  - id: fe-app-m6-07-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To link an error message to its input, use: `<input aria-___="error-msg-id">` and `<p id="error-msg-id">`.
    inputConfig:
      placeholder: "describedby"
    markingRule:
      matchMode: CONTAINS
      accepted: [describedby, "aria-describedby"]
      rejectedFeedback: "aria-describedby links an input to a description element by ID. When screen readers focus the input, they read the label AND the description (error message). Combined with aria-invalid=\"true\" on the invalid input, this creates a complete accessible error experience."
    hint: "The ARIA attribute that describes an element with additional text."
    reflectionPrompt: "The complete accessible error pattern: (1) aria-invalid='true' on the input, (2) aria-describedby='error-id' on the input, (3) a visible error message with id='error-id', (4) focus moved to the first error on submission. Screen readers automatically read the error when focus lands on the invalid field."

  - id: fe-app-m6-07-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 2-3 sentences why marking a required field with a red asterisk (*) and colour alone is not accessible.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [colour, colour-blind, screen-reader, text, asterisk, required, explain, announce]
      rejectedFeedback: "(1) Screen readers don't announce visual colour. (2) Colour-blind users can't distinguish the red asterisk from other text by colour. Solution: use the HTML required attribute (browser validation + screen reader announcement) AND visible text ('required' or an asterisk explained in a legend)."
    hint: "Two groups of users who can't see colour or visual indicators."
    reflectionPrompt: "The pattern: add the HTML required attribute (gives browser validation and screen reader 'required' announcement), and add a text indicator like '(required)' visible to sighted users. If using an asterisk, explain it: 'Fields marked with * are required'. Never communicate required status through colour alone."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which autocomplete value should you use on a field that collects the user's email address?"
    options:
      - "autocomplete=\"yes\""
      - "autocomplete=\"on\""
      - "autocomplete=\"email\""
      - "autocomplete=\"user-email\""
    correctIndex: 2
    feedback: "autocomplete=\"email\" specifically tells browsers and password managers this is an email field — they can offer the user's stored email address. WCAG 1.3.5 (Identify Input Purpose) requires using specific autocomplete token values for common personal data fields: name, email, address, tel, etc."
  - type: MULTIPLE_CHOICE
    question: "When a form is submitted with errors, keyboard focus should be moved to:"
    options:
      - "The submit button so the user can try again"
      - "The top of the page automatically"
      - "The first invalid field or an error summary at the top of the form"
      - "No focus change — the page scrolls automatically"
    correctIndex: 2
    feedback: "When errors occur on submission: (1) Move focus to the first invalid field (so screen readers announce its error), or (2) Move focus to an error summary at the top of the form listing all errors with links to each field. Without focus management, screen reader users often don't know submission failed."

retrieval:
  recall: "Write the HTML for a radio button group asking for preferred contact method, using fieldset and legend."
  explain: "Explain the complete accessible error pattern using aria-invalid and aria-describedby."
  mistakeId:
    code: "<span style='color:red'>*</span> Email: <input type='email'>"
    answer: "Multiple problems: no <label>, required indicated only by colour (inaccessible), no HTML required attribute. Fix: <label for='email'>Email <span aria-hidden='true'>*</span><span class='sr-only'>(required)</span></label><input type='email' id='email' required autocomplete='email'>. And explain the asterisk convention somewhere visible."
---

# Hook

Forms handle the most critical user interactions — login, signup, checkout, contact. They are also the most common accessibility failure point.

A form with missing labels, unclear errors, and colour-only required indicators fails a significant portion of users. An accessible form works for everyone — including users with screen readers, motor disabilities, cognitive differences, and those using keyboard-only navigation.

# Lore Introduction

*"The Academy's enrolment form,"* says Master Aelindra, holding up a meticulously labelled document, *"has been used for three hundred years. Every field has a permanent label. Every error has a written explanation. Every required field is marked in plain text. The form works for every apprentice who has ever applied — regardless of their abilities."*

# Core Learning

## Concept Introduction

**Complete accessible form pattern:**

```html
<form novalidate>
  <!-- Text input with visible label -->
  <div class="field">
    <label for="name">
      Full name <span aria-hidden="true">*</span>
      <span class="sr-only">(required)</span>
    </label>
    <input
      type="text"
      id="name"
      name="name"
      required
      autocomplete="name"
      aria-invalid="true"
      aria-describedby="name-error"
    >
    <p id="name-error" role="alert">Please enter your full name.</p>
  </div>

  <!-- Radio group with fieldset/legend -->
  <fieldset>
    <legend>Preferred contact method <span class="sr-only">(required)</span></legend>
    <label><input type="radio" name="contact" value="email" required> Email</label>
    <label><input type="radio" name="contact" value="phone"> Phone</label>
    <label><input type="radio" name="contact" value="post"> Post</label>
  </fieldset>

  <!-- Email with autocomplete -->
  <div class="field">
    <label for="email">Email address</label>
    <input type="email" id="email" name="email" autocomplete="email">
  </div>

  <button type="submit">Send message</button>
</form>
```

**Key accessibility attributes:**

| Attribute | Purpose |
|---|---|
| `aria-invalid="true"` | Marks invalid field — screen readers announce |
| `aria-describedby="id"` | Links field to its error/description text |
| `role="alert"` | Error messages are announced immediately |
| `autocomplete="email"` | Helps browsers/password managers |
| `required` | Browser validation + screen reader "required" announcement |

## Why It Matters

Forms are where access matters most — they're how users log in, buy, register, and get help — and they're the most common accessibility failure on the web:

- An unlabelled input is a mystery box to a screen reader user: "edit text" — of *what*?
- Error messages that only flash red are invisible to colour-blind users and silent to blind ones; errors must be announced and associated with their field
- Accessible forms are better for everyone: labels enlarge click targets, clear errors reduce abandonment for all users

A user who can't complete your form can't become your customer. Form accessibility is conversion work as much as ethics.

## Common Mistakes

- No `<label>` (placeholder as substitute)
- Radio groups with no `<fieldset>`/`<legend>`
- Error messages not linked with `aria-describedby`
- Required-only communicated by colour (red asterisk without text)
- No focus management after form submission errors

## Mental Model

An accessible form is a well-run customs checkpoint; an inaccessible one is the same checkpoint with the lights off. Each field is a window, and the `<label>` is the sign above it — programmatically bolted on (`for`/`id`), not just taped nearby, so the officer's announcement ("Passport number") reaches you no matter how you arrive. Validation errors are the officer telling you *which* window to return to and *what* to fix — not a distant red light meaning "something, somewhere, is wrong." Build every form asking: could someone complete this checkpoint by announcements alone, never seeing the hall?

## Mini Summary

- ✔ Every input needs a `<label>` (for/id or wrapping)
- ✔ Radio/checkbox groups need `<fieldset>` + `<legend>`
- ✔ Errors: `aria-invalid="true"` + `aria-describedby="error-id"` + visible message
- ✔ Required: HTML `required` attribute + visible text indicator (not colour only)
- ✔ Move focus to first error on failed submission

# Guided Practice Quest

**The Accessible Ledger** — three questions on accessible form implementation. Steps in `guidedSteps`.

# Solo Practice Quest

Build a complete accessible contact form: name (text, required), email (email, autocomplete, required), message (textarea), and preferred response time (radio group: Morning, Afternoon, Evening). Include: labels, fieldset/legend, required indicators (non-colour), error message pattern for name field if empty, and submit button. Write a 3-sentence explanation of your accessibility choices.

# Integration

**Connecting to Psychology — The Frustration-Aggression Hypothesis**

Research by Dollard et al. (1939) and subsequent cognitive psychology work shows that frustrated goal pursuit produces negative affect — frustration, then aggression, then disengagement. Forms that fail silently (no error announcement), that clear on submission, or that have unclear requirements are frustrating by design. For users with disabilities who already invest more effort in web interaction, this frustration is compounded. Accessible forms — clear labels, immediate error feedback, focus management — reduce the goal-pursuit friction that creates frustration. Accessible form design is user-respecting design.

# Lore Conclusion

*"The enrolment form,"* says Master Aelindra, *"has one purpose: to gather information from an applicant while making the process as simple as possible. Every label, every error message, every field grouping serves that purpose. When a form is difficult to complete, it is failing at its only job. Build forms that succeed."*

---
