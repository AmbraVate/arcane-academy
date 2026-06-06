---
id: fe-jun-m3-07
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
lesson: validation_strategies
title: "Validation Strategies"
sortOrder: 1
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
    - "Distinguishes real-time, on-blur, and on-submit validation timing"
    - "Implements a validate function returning an errors object"
    - "Knows when each validation strategy is appropriate"
    - "Stores errors in state separate from form values"
  keywords: [validation, real-time, on-blur, on-submit, errors, strategy, timing, validate-function]
  modelAnswer: |
    Validation timing affects UX. Real-time (onChange): immediate feedback, but can
    be jarring before the user finishes typing. On-blur: validates when the field loses
    focus — less intrusive. On-submit: validates all fields only on submission — simplest,
    but delayed feedback. Common pattern: on-blur for each field, on-submit to catch
    anything missed. Store errors in a separate { fieldName: errorMessage } object.
guidedSteps:
  - id: fe-jun-m3-07-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A user starts typing their email. The field immediately turns red and shows "Invalid email". This is:
    inputConfig:
      options:
        - "Good UX — immediate feedback"
        - "Poor UX — the user hasn't finished typing; on-blur validation is more appropriate"
        - "Required by WCAG accessibility standards"
        - "The only correct validation approach"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Poor UX — the user hasn't finished typing; on-blur validation is more appropriate"]
      rejectedFeedback: "Real-time email validation before the user finishes typing creates false positives (they haven't typed the domain yet). On-blur validation fires after the user leaves the field — a more natural point to check. Real-time is appropriate for constraints like character count."
    hint: "When is the right moment to tell someone their email is invalid?"
    reflectionPrompt: "UX research shows that premature error messages increase form abandonment. Validate at the right moment: character count (real-time), field validity (on-blur), all fields complete (on-submit). Timing is part of the design."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What should a validate() function return?"
    options:
      - "true/false"
      - "An errors object: { fieldName: errorMessage } — empty if valid"
      - "An array of error strings"
      - "A boolean array matching field order"
    correctIndex: 1
    feedback: "{ fieldName: errorMessage } — present for invalid fields, absent for valid ones. isEmpty(errors) === true means the form is valid. This pattern maps directly to rendering: errors.email && <p>{errors.email}</p>."

retrieval:
  recall: "Write a validate() function for a form with required name and valid email fields."
  explain: "Compare the user experience of real-time, on-blur, and on-submit validation."
  mistakeId:
    code: "Showing 'Email is required' error as soon as the form renders, before the user touches it"
    answer: "Only show errors after the user has interacted with the field. Track touched fields: const [touched, setTouched] = useState({}). Validate on blur and add field to touched. Only show errors for touched fields."
---

# Hook

When should you tell a user their input is wrong? Too soon is annoying. Too late means wasted effort. Validation timing is a UX decision as much as a technical one.

# Lore Introduction

*"The Academy's examiners,"* says Master Aelindra, *"do not mark every word as the student writes. They review the completed answer. Some instructors check at each paragraph. Neither marks each keystroke."*

# Core Learning

## Concept Introduction

```jsx
function validate(form) {
  const errors = {};
  if (!form.name.trim()) errors.name = 'Name is required';
  if (!form.email.includes('@')) errors.email = 'Enter a valid email';
  if (form.password.length < 8) errors.password = 'At least 8 characters';
  return errors;  // empty object = valid
}

function Form() {
  const [form, setForm] = useState({ name: '', email: '', password: '' });
  const [errors, setErrors] = useState({});
  const [touched, setTouched] = useState({});

  // On-blur: validate single field
  const handleBlur = (e) => {
    setTouched(prev => ({ ...prev, [e.target.name]: true }));
    setErrors(validate(form));
  };

  // On-submit: validate all
  const handleSubmit = (e) => {
    e.preventDefault();
    const errs = validate(form);
    if (Object.keys(errs).length > 0) { setErrors(errs); return; }
    // submit...
  };
}
```

## Mini Summary
- ✔ Separate errors state from form values state
- ✔ validate() returns { fieldName: message } — empty = valid
- ✔ Real-time: onChange. Field-level: onBlur. All-fields: onSubmit
- ✔ Track touched fields to avoid showing errors before interaction

# Solo Practice Quest

Build a registration form. Validate: username (required, 3+ chars), email (required, must contain @), password (8+ chars). Show errors only after each field is blurred. Disable submit if errors exist.

# Integration

**Psychology — Signal Detection Theory:** Validation is a signal detection problem: false positives (premature errors) train users to ignore warnings. False negatives (no error on submission) cause frustration. The optimal strategy minimises both — which is why validation timing matters: validate at the moment the user expects feedback, not before or after.

# Lore Conclusion

*"Mark at the right moment. Too early is presumptuous. Too late is unhelpful. The timing is part of the craft."*

---
