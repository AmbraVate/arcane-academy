---
id: fe-jun-m3-08
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
lesson: error_messages
title: "Error Messages"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes specific, actionable error messages"
    - "Positions error messages adjacent to the invalid field"
    - "Uses aria-invalid and aria-describedby for accessible errors"
    - "Distinguishes field-level errors from form-level error summaries"
  keywords: [error-message, specific, actionable, aria-invalid, aria-describedby, adjacent, summary, accessible]
  modelAnswer: |
    Error messages must be specific ("Email must contain @") not generic ("Invalid input").
    They appear adjacent to the field they describe. Use aria-invalid="true" on the input
    and aria-describedby="error-id" linking to the error text so screen readers announce
    the error when the field receives focus. For multi-field errors, a summary at the top
    helps keyboard users navigate to each error.
guidedSteps:
  - id: fe-jun-m3-08-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which error message is more helpful?
    inputConfig:
      options:
        - "\"Invalid input\""
        - "\"Please enter a valid email address (example: name@domain.com)\""
        - "\"Error in field 2\""
        - "\"Try again\""
    markingRule:
      matchMode: NORMALIZED
      accepted: ["\"Please enter a valid email address (example: name@domain.com)\""]
      rejectedFeedback: "Specific, actionable messages tell the user exactly what is wrong and how to fix it. 'Invalid input' is a developer note, not a user message. Include what format is expected, especially for non-obvious requirements."
    hint: "The best error message tells you what went wrong AND how to fix it."
    reflectionPrompt: "Error messages are the UI's voice. They should be empathetic, specific, and constructive. 'Password must be at least 8 characters' > 'Invalid password'. Don't blame the user; describe the requirement."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Where should a field-level error message be positioned?"
    options:
      - "In a modal at the top of the page"
      - "At the bottom of the form after all fields"
      - "Immediately after (or below) the field it describes"
      - "In the browser's alert() dialog"
    correctIndex: 2
    feedback: "Adjacent positioning creates visual proximity — the error is associated with the field it describes (Gestalt Law of Proximity). Users scan form → field → error → correction naturally when they are co-located."

retrieval:
  recall: "Write the accessible HTML for an email input with an error message, using aria-invalid and aria-describedby."
  explain: "When would you show an error summary at the top of a form in addition to field-level errors?"
  mistakeId:
    code: "Showing one generic 'Please fix the errors above' message with no field-specific details"
    answer: "Show field-specific errors adjacent to each field. The summary can list all errors, but each field needs its own specific message explaining what to fix."
---

# Hook

"Invalid input." Three words that tell the user nothing. Compare: "Email must contain @ and a domain (e.g., name@company.com)." One tells; one guides. Error messages are the difference.

# Lore Introduction

*"A rejection note that says 'wrong' is useless,"* says Master Aelindra. *"A rejection note that says 'the seal is missing from the fourth paragraph, please re-stamp and resubmit' — that one gets results."*

# Core Learning

## Concept Introduction

```jsx
// Accessible error message pattern
<div className="field">
  <label htmlFor="email">Email</label>
  <input
    id="email"
    type="email"
    value={form.email}
    onChange={handleChange}
    aria-invalid={!!errors.email}
    aria-describedby={errors.email ? 'email-error' : undefined}
  />
  {errors.email && (
    <p id="email-error" role="alert" className="error-msg">
      {errors.email}
    </p>
  )}
</div>

// Good error messages
'Name is required'                           // ❌ too vague
'Please enter your full name'                // ✅ specific
'Password must be at least 8 characters'     // ✅ requirement stated
'Contains invalid characters: @ is not allowed' // ✅ explains what and why
```

**Error summary (for multi-field forms):**
```jsx
{Object.keys(errors).length > 0 && (
  <div role="alert" aria-label="Please fix these errors">
    <ul>
      {Object.entries(errors).map(([field, msg]) => (
        <li key={field}><a href={`#${field}`}>{msg}</a></li>
      ))}
    </ul>
  </div>
)}
```

## Mini Summary
- ✔ Specific, actionable messages — not "invalid"
- ✔ Position adjacent to the invalid field
- ✔ aria-invalid + aria-describedby for screen reader accessibility
- ✔ Error summary for complex multi-field forms

# Solo Practice Quest

Build a form with 4 fields and error messages for each. Make all errors accessible (aria-invalid, aria-describedby). On submission failure, show a summary at the top with anchor links to each invalid field.

# Integration

**Psychology — Error Recovery and Learned Helplessness:** Vague error messages produce learned helplessness — users stop trying because they don't know what to do. Specific, actionable messages maintain agency — the user knows exactly how to succeed. UI text is a psychological intervention, not just information display.

# Lore Conclusion

*"The rejection note that guides the applicant to success is worth more than the application itself. Write errors that solve problems, not errors that describe them."*

---
