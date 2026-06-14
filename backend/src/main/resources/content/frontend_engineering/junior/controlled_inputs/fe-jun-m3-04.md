---
id: fe-jun-m3-04
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
lesson: controlled_vs_uncontrolled
title: "Controlled vs Uncontrolled"
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
    - "Defines controlled vs uncontrolled input"
    - "Explains why controlled inputs are the React default preference"
    - "Identifies when uncontrolled inputs (useRef) are appropriate"
    - "Demonstrates the controlled input pattern with value + onChange"
  keywords: [controlled, uncontrolled, value, onChange, useRef, state, single-source-of-truth, DOM]
  modelAnswer: |
    A controlled input has its value driven by React state (value={state} + onChange to update
    state). React is the single source of truth. An uncontrolled input keeps its value in the
    DOM — read via useRef. Controlled inputs are preferred because the UI always reflects
    state. Uncontrolled inputs are simpler for one-time reads (file inputs, simple forms
    where you only need the value on submit).
guidedSteps:
  - id: fe-jun-m3-04-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      `<input value={name} onChange={e => setName(e.target.value)}>` is:
    inputConfig:
      options:
        - "Uncontrolled — the DOM owns the value"
        - "Controlled — React state owns the value"
        - "Semi-controlled — both own it"
        - "Invalid — inputs cannot have both value and onChange"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Controlled — React state owns the value"]
      rejectedFeedback: "value={name} drives the displayed value from React state. onChange updates the state when the user types. The DOM displays whatever React says — React is always the source of truth."
    hint: "Who owns the value — React state or the DOM?"
    reflectionPrompt: "Without onChange, value={name} creates a read-only input — React controls the value but refuses user changes. You need both value AND onChange for a usable controlled input."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "When is an uncontrolled input (using useRef) appropriate?"
    options:
      - "Always — it's simpler than controlled"
      - "For file inputs, or when you only need the value on form submission"
      - "When you don't want to use React state"
      - "Never — always use controlled inputs"
    correctIndex: 1
    feedback: "File inputs must be uncontrolled (you can't set a file value programmatically). For simple forms where you only read values on submit and don't need real-time validation, uncontrolled is simpler."

retrieval:
  recall: "Write a controlled text input for a 'username' field."
  explain: "What happens if you set value on an input without providing onChange?"
  mistakeId:
    code: "<input value={name}> without onChange"
    answer: "The input is read-only. React controls the value but rejects user keystrokes. Add onChange={e => setName(e.target.value)} to allow editing."
---

# Hook

React has two ways to handle form inputs: controlled (React owns the value) and uncontrolled (the DOM owns the value). Understanding the difference prevents the most common form bugs.

# Lore Introduction

*"A controlled ledger,"* says Master Aelindra, *"is updated only through the scribe's office. An uncontrolled ledger can be modified by anyone. One source of truth, or many — choose deliberately."*

# Core Learning

## Concept Introduction

```jsx
// CONTROLLED — React owns the value
function ControlledForm() {
  const [name, setName] = useState('');
  return <input value={name} onChange={e => setName(e.target.value)} />;
}

// UNCONTROLLED — DOM owns the value
function UncontrolledForm() {
  const nameRef = useRef(null);
  const handleSubmit = () => console.log(nameRef.current.value);
  return <input ref={nameRef} defaultValue="" />;
}
```

| | Controlled | Uncontrolled |
|---|---|---|
| Value in | React state | DOM |
| Read with | state variable | ref.current.value |
| Validation | Real-time | On submit only |
| Use when | Most forms | File inputs, simple submit-only |

## Common Mistakes

- **Adding `value` without `onChange`**: An input with `value={state}` but no `onChange` handler is read-only — the user types but nothing changes. React logs a warning; always pair `value` with an `onChange` setter.
- **Switching between controlled and uncontrolled mid-lifecycle**: Changing an input from `value={undefined}` to `value="something"` (or vice versa) triggers React's controlled-to-uncontrolled warning and unpredictable behaviour. Choose one pattern from the start.
- **Using `defaultValue` on a controlled input**: `defaultValue` sets the initial value once and has no further effect. On a controlled input, use `useState('')` to initialise instead.
- **Reaching for `useRef` when live validation is needed**: Refs cannot trigger re-renders. Any UI feature that responds to the input value while the user types requires controlled state.

## Mental Model

The controlled/uncontrolled split is the difference between a bank account and cash in a drawer. A controlled input is a bank account: the balance you see on screen is a *display of the ledger* (React state) — every deposit (keystroke) goes through the teller (`onChange` → `setState`), the ledger updates, and the displayed balance refreshes from the ledger. The display can never disagree with the books, because it has no independent existence: that's `value={state}` — the input shows state, only state, always state. This is also why the half-wired version freezes: a `value` without an `onChange` is a bank that displays the ledger but employs no tellers — deposits are shouted into the void, the ledger never changes, the display never moves. An uncontrolled input is cash in a drawer: the money (value) lives in the drawer itself (the DOM node), accumulating as the user adds to it, and you only count it when you need the total (reading via a ref at submit). Less infrastructure, perfectly sound for simple cases — but the bank doesn't know the balance between counts, so anything requiring *live* knowledge of the money (validation as they type, a running total, disabling a button until the amount is right) is impossible without converting to the ledger system. Choose by the question: does anything need to *react to the value while it's being entered*? Ledger (controlled). Only needed at the end? Drawer (uncontrolled) is honest and cheap. And never run both systems on one account — an input that starts as a drawer and becomes ledger-managed mid-session (undefined value becoming a string) is exactly the accounting confusion React's console warning exists to catch.

## Why It Matters

Controlled versus uncontrolled is the fork in the road for every form you will ever build in React — and choosing consciously is the difference between forms that grow features gracefully and forms that get rewritten:

- The controlled pattern (value from state, onChange writing back) makes React's single-source-of-truth philosophy *physical*: the input displays what state says, period — which is what makes live validation, character counters, conditional fields, instant formatting, and programmatic clearing all one-line features instead of DOM surgery
- Uncontrolled inputs (the DOM keeps the value, you read it when needed, usually via a ref) aren't wrong — they're the lighter tool: less re-rendering, less wiring, perfectly adequate for a simple form read once at submit, and the standard answer for file inputs, which can't be controlled at all
- The bugs live at the unchosen middle: a `value` prop without `onChange` produces the famous frozen input (React faithfully re-rendering state that never changes); switching between `undefined` and a string mid-flight triggers the controlled-to-uncontrolled warning that confuses every junior the first time
- This decision is also a team-reading skill: form libraries split along exactly this line (Formik and controlled-style state versus React Hook Form's ref-based registration), so understanding both models is what lets you read any codebase's forms

Interviewers love this question because it tests whether you understand *where state lives* — which is the actual subject of the entire module.

## Mini Summary
- ✔ Controlled: value={state} + onChange → React owns truth
- ✔ Uncontrolled: ref + defaultValue → DOM owns truth
- ✔ Prefer controlled for validation, dynamic fields, dependent fields

# Solo Practice Quest

Build a login form: email and password as controlled inputs. Show real-time character count under each field. Disable the submit button until both fields are non-empty.

# Integration

**Psychology — Locus of Control:** Controlled inputs externalise control to React state — one place governs the value. Uncontrolled inputs distribute control to the DOM. Centralised control is easier to reason about, test, and validate — at the cost of slightly more boilerplate.

# Lore Conclusion

*"Know who holds the truth. In controlled forms, it is React. In uncontrolled, it is the DOM. Make the choice deliberately."*

---
