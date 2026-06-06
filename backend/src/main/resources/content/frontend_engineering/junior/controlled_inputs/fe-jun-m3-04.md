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
