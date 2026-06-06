---
id: fe-jun-m3-05
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m3
moduleTitle: "Module 3: Events and Forms"
moduleGlyph: "📝"
moduleSortOrder: 3
topicSlug: controlled_inputs
topicTitle: "Controlled Inputs"
topicSortOrder: 2
lesson: input_state
title: "Input State"
sortOrder: 2
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
    - "Manages multiple inputs with a single state object"
    - "Uses the spread operator to update one field without losing others"
    - "Initialises form state with correct default values"
    - "Resets form state after successful submission"
  keywords: [state-object, spread, field, update, reset, default, form-state, onChange]
  modelAnswer: |
    Managing multiple inputs: store all field values in one state object
    { name: '', email: '' }. Update one field: setForm(prev => ({ ...prev, [name]: value })).
    The spread operator preserves unchanged fields while updating the target field.
    Reset on submit: setForm(initialState). Initialise with empty strings (not undefined)
    to keep inputs controlled from the start.
guidedSteps:
  - id: fe-jun-m3-05-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does `setForm(prev => ({ ...prev, email: 'new@email.com' }))` do?
    inputConfig:
      options:
        - "Replaces the entire form state with just { email: 'new@email.com' }"
        - "Updates only the email field, preserving all other form fields"
        - "Creates a new form state object from scratch"
        - "Throws an error — useState doesn't accept functions"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Updates only the email field, preserving all other form fields"]
      rejectedFeedback: "The spread operator (...prev) copies all existing fields. The email: 'new' then overwrites only email. Result: all previous fields + updated email. Without spread, you'd lose all other fields."
    hint: "What does the spread operator do to the previous state?"
    reflectionPrompt: "Functional update (prev => ...) is safer than direct update (form => ...) when the new state depends on the previous state — it always uses the most recent state, even if multiple updates are batched."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why should you initialise text input state as '' (empty string) rather than undefined?"
    options:
      - "undefined works fine — React handles it"
      - "undefined makes the input uncontrolled initially, then controlled — React warns about this"
      - "Performance reasons"
      - "undefined cannot be stored in state"
    correctIndex: 1
    feedback: "An input with value={undefined} is uncontrolled. When you later set value to a string, it switches to controlled — React warns 'A component is changing an uncontrolled input to be controlled.' Always initialise with '' to keep the input controlled throughout its lifecycle."

retrieval:
  recall: "Write the state initialisation and onChange handler for a form with name, email, and message fields."
  explain: "Why does form reset work by calling setForm(initialState)?"
  mistakeId:
    code: "const [form, setForm] = useState({ name: undefined, email: undefined })"
    answer: "Use empty strings: useState({ name: '', email: '' }). undefined makes inputs uncontrolled initially, causing React warnings and unexpected behaviour."
---

# Hook

A form with five fields could have five separate useState calls. Or it could have one. Managing form state as a single object scales better and keeps related data together.

# Lore Introduction

*"Five separate ledgers for one contract,"* says Master Aelindra, *"is five times the work to reconcile. One ledger, five entries — coherent, auditable, resettable as a unit."*

# Core Learning

## Concept Introduction

```jsx
const initialState = { name: '', email: '', message: '' };

function ContactForm() {
  const [form, setForm] = useState(initialState);

  function handleChange(e) {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  }

  function handleSubmit(e) {
    e.preventDefault();
    // submit form...
    setForm(initialState);  // reset
  }

  return (
    <form onSubmit={handleSubmit}>
      <input name="name"    value={form.name}    onChange={handleChange} />
      <input name="email"   value={form.email}   onChange={handleChange} />
      <textarea name="message" value={form.message} onChange={handleChange} />
      <button type="submit">Send</button>
    </form>
  );
}
```

## Mini Summary
- ✔ One state object for all form fields
- ✔ Spread + computed key for single-field updates
- ✔ Initialise with empty strings (never undefined)
- ✔ Reset by setting state back to initialState

# Solo Practice Quest

Build a multi-step form (two steps, 3 fields each) using a single form state object. Track which step is shown with a separate stepIndex state. Show a summary of all fields before final submission.

# Integration

**Mathematics — Immutable Updates and Copy-on-Write:** The spread update pattern implements copy-on-write semantics. The previous state is never mutated — a new object is created with the change applied. This is analogous to functional data structures where modification produces a new version, preserving the original.

# Lore Conclusion

*"One ledger, one source, one reset. Coherent state is always easier to manage than fragmented state."*

---
