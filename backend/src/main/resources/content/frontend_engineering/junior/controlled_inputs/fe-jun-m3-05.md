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

## Common Mistakes

- **Forgetting the spread when updating a field in a state object**: `setForm({ [name]: value })` replaces the entire object with a single field. Always spread the previous state: `setForm(prev => ({ ...prev, [name]: value }))`.
- **Initialising string fields with `undefined` instead of `''`**: Starting a field as `undefined` and later setting it to a string triggers React's controlled-to-uncontrolled warning. Always initialise every field as an empty string.
- **Treating `e.target.value` as a number**: Every input value arrives as a string, including numeric inputs. Parse explicitly (`parseInt`, `parseFloat`) when a number is required — do not rely on JavaScript's implicit coercion.
- **Mutating state directly**: `form.name = 'new'` bypasses React's state system and will not cause a re-render. Use `setForm` with a spread every time.

## Mental Model

Managing input state is running a hotel's room-status board, and the scaling lesson is what happens as the hotel grows. One room (a single input) is easy: a sticky note — "room 1: occupied" — one `useState`, one handler, done. The trouble is success: at twelve rooms (a real form), twelve sticky notes with twelve hand-written update routines means the front desk drowns in nearly identical paperwork — every new room demands another note, another routine, another chance to copy-paste the wrong one (the classic bug where the email field's handler updates the name field). The professional fix is the *unified board*: one structured panel holding every room's status (a single state object — `{name, email, phone}`), updated by one generic procedure that reads the room number off the incoming report and flips exactly that slot — the computed-key handler, `[e.target.name]: e.target.value`, where each input's `name` attribute is its room number. Two board disciplines keep it sound. Updates *replace the whole board photo, changed slot included* — `setForm({...form, [name]: value})` — because the board's history (React's state) tracks photographs, not pencil edits; forget the spread and your new photo shows one room's status on an otherwise blank board, the all-fields-vanish bug every junior meets once. And reports arrive as text: everything `e.target.value` delivers is a string — room 12 reporting "4" guests means the *characters* "4" — so numeric fields get converted deliberately at the board, not hopefully at checkout. One board, room-numbered slots, one procedure, photographs not pencil marks: input state at any scale.

## Why It Matters

Input state is where controlled forms become real engineering — one field is a demo; a form's worth of fields with the right state shape is the daily job:

- The `value`/`onChange`/`e.target.value` loop is the most-typed pattern in React form code: making it reflexive (including the variations — `checked` for checkboxes, `valueAsNumber` pitfalls, why everything from the DOM arrives as a string) removes friction from every form you'll ever write
- State shape is a scaling decision made early: separate `useState` per field is fine at two fields and unmanageable at twelve, while a single object with a generic handler (`setForm({...form, [name]: value})` keyed by input `name`) is the pattern that keeps a registration form to one handler instead of nine — knowing both, and the crossover point, is the lesson's core judgement
- The update rules from module two apply with new teeth: spreading the old object is mandatory (forget it and every keystroke erases the other fields — a spectacular, memorable bug), and the single-source principle means formatting, trimming, and constraining input happens in exactly one place, the handler
- This is also the foundation under every form library: Formik, React Hook Form, and friends are abstractions over precisely this state-per-field problem — understanding the manual version is what lets you use the libraries as tools instead of incantations

Every product is forms somewhere — auth, checkout, settings, search. The engineer for whom multi-field input state is mechanical gets to spend their attention on the product instead.

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
