---
id: fe-app-m2-13
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m2
moduleTitle: "Module 2: HTML Foundations"
moduleGlyph: "📄"
moduleSortOrder: 2
topicSlug: semantic_html
topicTitle: "Semantic HTML"
topicSortOrder: 3
lesson: forms_introduction
title: "Forms Introduction"
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
    - "Writes a <form> with the correct action and method attributes"
    - "Associates every <input> with a <label> using for/id"
    - "Uses appropriate input types (text, email, password, checkbox, radio, submit)"
    - "Adds required validation to mandatory fields"
    - "Explains why placeholder text alone is not a substitute for a <label>"
  keywords: [form, input, label, for, id, action, method, required, type, placeholder]
  modelAnswer: |
    HTML forms collect user input and submit it to a server. Every input must have
    a <label> connected via matching for and id attributes — this links them for
    screen readers and increases click target size. Input types (email, password,
    number) trigger appropriate mobile keyboards and browser validation. placeholder
    text disappears on typing and is not a replacement for a visible label.
guidedSteps:
  - id: fe-app-m2-13-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which is the correct way to associate a label with its input?
    inputConfig:
      options:
        - "<label>Email</label><input type=\"email\" placeholder=\"Email\">"
        - "<label for=\"email\">Email</label><input type=\"email\" id=\"email\">"
        - "<label><input type=\"email\">Email</label> — wrapping works without for/id"
        - "Both B and C are correct"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Both B and C are correct"]
      rejectedFeedback: "Both the for/id pattern and the wrapping pattern are valid. The for/id pattern is more flexible (label and input don't need to be adjacent). The wrapping pattern is concise. Option A is wrong — no association exists between the label and input."
    hint: "There are actually two valid approaches."
    reflectionPrompt: "The for/id association does two things: it tells screen readers which label belongs to which input, AND clicking the label focuses or activates the input (larger click target). Both benefits disappear without the association."

  - id: fe-app-m2-13-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete this email input that browsers will validate automatically:

      `<input type="___" id="email" name="email" required>`
    inputConfig:
      placeholder: "email"
    markingRule:
      matchMode: CONTAINS
      accepted: [email]
      rejectedFeedback: "type=\"email\" enables browser-built-in validation: the browser checks that the value matches an email format before submission. It also triggers the email keyboard on mobile devices. This is free validation — no JavaScript required."
    hint: "HTML5 added many input types that trigger built-in validation."
    reflectionPrompt: "HTML5 input types provide free validation and better mobile UX. type=\"number\" shows a number pad. type=\"tel\" shows a phone pad. type=\"date\" shows a date picker. Always use the most specific input type available."

  - id: fe-app-m2-13-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why using placeholder text as a substitute for a visible <label> is a poor accessibility and usability practice.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [disappear, placeholder, label, screen reader, visible, clear]
      rejectedFeedback: "Placeholder text: (1) disappears when the user starts typing — they forget what the field is for; (2) has low contrast in most browsers; (3) is not reliably announced by screen readers as a field label. A visible <label> never disappears and is always announced."
    hint: "What happens to placeholder text when a user starts typing?"
    reflectionPrompt: "This is a very common UI anti-pattern on real websites. The pattern feels elegant visually but fails practically. The compromise: use a visible label AND a placeholder for additional hint text — not one or the other."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A form has method=\"post\". Where does the form data go when submitted?"
    options:
      - "Appended to the URL as query parameters"
      - "Sent in the request body — not visible in the URL"
      - "Stored in localStorage automatically"
      - "Emailed to the form owner"
    correctIndex: 1
    feedback: "POST sends data in the request body. GET appends data to the URL as ?name=value pairs. Use POST for sensitive data (passwords, personal info) and for actions that change server state. Use GET for searches and filters."
  - type: MULTIPLE_CHOICE
    question: "What does the `name` attribute on an input do?"
    options:
      - "Sets the visible label text"
      - "Provides the key used when the form data is submitted"
      - "Defines the CSS class for the input"
      - "Sets the placeholder text"
    correctIndex: 1
    feedback: "The name attribute becomes the key in the submitted form data. For email input with name=\"email\" and value \"user@example.com\", the server receives email=user@example.com. Without a name attribute, the input's value is not submitted at all."

retrieval:
  recall: "Write a complete login form with email and password fields and a submit button."
  explain: "Explain the difference between GET and POST form methods and when to use each."
  mistakeId:
    code: "<input type=\"text\" placeholder=\"Your email\">"
    answer: "No <label>, no type=\"email\", no name attribute. Correct: <label for=\"email\">Email</label><input type=\"email\" id=\"email\" name=\"email\" required>. The label provides accessibility, type=\"email\" enables validation, name enables form submission."
---

# Hook

Every login, every sign-up, every checkout, every search — they are all forms. Forms are how users communicate with your application.

They are also the most common source of frustration on the web. Fields without labels. Errors that don't say what went wrong. Required fields discovered only after submission. Bad forms lose users. Good forms convert them.

> Think of the last time a form frustrated you. What specifically went wrong?

# Lore Introduction

*"The Academy's enrolment desk,"* says Master Aelindra, *"has one rule: every question must have a visible label. Not a hint in grey text that disappears when you write. A real, permanent label. This is not a design preference — it is a requirement of respectful communication."*

# Core Learning

## Concept Introduction

| Element / Attribute | Purpose |
|---|---|
| `<form action method>` | Container; action = where to send, method = how |
| `<label for="id">` | Labels an input; associates via matching id |
| `<input type="...">` | The input field; type determines validation + keyboard |
| `required` | Browser validates presence before submission |
| `name` | Key in submitted data — must be present to submit |
| `placeholder` | Hint text inside field — NOT a label substitute |

**Common input types:**

| type | Use case | Built-in benefit |
|---|---|---|
| `text` | General short text | None |
| `email` | Email address | Format validation, email keyboard |
| `password` | Passwords | Characters hidden |
| `number` | Numeric input | Number keyboard, min/max |
| `checkbox` | Boolean choice | Toggle |
| `radio` | One of several options | Grouped by name |
| `submit` | Submit button | Submits the form |

## Why It Matters

Forms handle the most sensitive user interactions: login, payment, personal data. Inaccessible, poorly labelled forms exclude users with disabilities and frustrate everyone else. Browser-native validation (required, type="email") is free security and UX that most developers underutilise.

## Worked Examples

```html
<form action="/login" method="post">
  <div>
    <label for="email">Email address</label>
    <input type="email" id="email" name="email" required autocomplete="email">
  </div>

  <div>
    <label for="password">Password</label>
    <input type="password" id="password" name="password" required
           autocomplete="current-password" minlength="8">
  </div>

  <button type="submit">Sign in</button>
</form>
```

## Common Mistakes

- **No `<label>`:** Screen readers cannot announce what the field is for.
- **Using `placeholder` as a label:** Disappears on typing; low contrast; not reliably read.
- **Missing `name` attribute:** The field's value is never submitted.
- **Using `<div>` as a submit button:** Use `<button type="submit">` — it has built-in form submission, keyboard activation, and accessibility.

## Mental Model

Think of a form as a **physical questionnaire**. Every question has a printed label above the blank line. The label never disappears while you write. The `<label>` element is that printed question.

## Mini Summary

- ✔ Every `<input>` needs a `<label>` associated via `for`/`id`
- ✔ Use the most specific `type` (email, number, tel, date) for free validation + mobile keyboards
- ✔ `name` attribute is required for field values to be submitted
- ✔ POST for mutations (login, signup), GET for searches
- ✔ `placeholder` is supplementary hint text — not a label replacement

# Guided Practice Quest

**The Enrolment Form** — three questions on building accessible, valid HTML forms. Steps in the frontmatter `guidedSteps` section.

# Solo Practice Quest

Build a registration form with: first name, last name, email, password (with minlength), a "I agree to the terms" checkbox, and a submit button. Use correct label associations, appropriate input types, and mark required fields. Write 3 sentences explaining your choices.

# Integration

**Connecting to Psychology — The Peak-End Rule**

Daniel Kahneman's Peak-End Rule states that people judge an experience primarily by how it felt at its most intense moment (the peak) and at its end — not the average. In a checkout flow, the "peak" is often the payment form: the most stressful, most consequential step. The "end" is the confirmation screen.

A frustrating payment form — unclear labels, unhelpful error messages, fields that clear on error — creates a negative peak that colours the entire experience. A clear, well-labelled form with inline validation reduces anxiety at the peak. Conversely, a smooth form followed by a confusing confirmation creates a negative end. Both matter.

# Lore Conclusion

*"Forms are a contract,"* says Master Aelindra. *"The user gives you their data. In return, you give them clarity: what you need, what went wrong, what happens next. Break the contract and they leave. Honour it and they return."*

---
