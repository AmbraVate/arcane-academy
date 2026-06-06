---
id: fe-jun-m3-06
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
lesson: textarea_and_select
title: "Textarea and Select"
sortOrder: 3
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
    - "Controls a textarea with value + onChange"
    - "Controls a select with value + onChange (no selected attribute on options)"
    - "Handles multi-select with Array.from(event.target.selectedOptions)"
    - "Notes that textarea value is NOT set in HTML content in React"
  keywords: [textarea, select, value, onChange, multi-select, selectedOptions, option, controlled]
  modelAnswer: |
    In React, textarea is controlled exactly like input: value={state} onChange={...}.
    Unlike HTML where content goes between the tags, React textarea uses the value prop.
    Select is controlled with value on the <select> element (not selected on <option>).
    Multi-select: value is an array; read with Array.from(event.target.selectedOptions).map(o => o.value).
guidedSteps:
  - id: fe-jun-m3-06-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      How do you set the initial value of a controlled React textarea?
    inputConfig:
      options:
        - "<textarea>Initial text</textarea>"
        - "<textarea value={text} onChange={...}>Initial text</textarea>"
        - "<textarea value={text} onChange={...} /> (value prop only)"
        - "<textarea defaultValue='Initial text' />"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["<textarea value={text} onChange={...} /> (value prop only)"]
      rejectedFeedback: "React textarea uses the value prop, not content between tags. Unlike HTML where you write <textarea>Default</textarea>, React ignores children of textarea and uses value exclusively."
    hint: "React textarea uses the same controlled pattern as input."
    reflectionPrompt: "This surprises developers coming from HTML. React standardises all text inputs to use the value prop — textarea, input, contentEditable — for consistency."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "How do you pre-select an option in a controlled React select?"
    options:
      - "Add selected attribute to the <option>"
      - "Set value on the <select> element matching the option's value"
      - "Use defaultSelected on the option"
      - "Use initialValue prop on the select"
    correctIndex: 1
    feedback: "<select value={selectedValue} onChange={...}>. The value prop on <select> determines which option is selected — matching option value attributes. React ignores the selected attribute on individual options."

retrieval:
  recall: "Write a controlled select for country (UK, US, AU) that updates a 'country' state field."
  explain: "Why does React textarea use the value prop rather than content between tags?"
  mistakeId:
    code: "<option value='uk' selected={country === 'uk'}>UK</option>"
    answer: "Don't put selected on options. Put value on the parent <select>: <select value={country} onChange={...}>. React manages which option is selected via the select's value prop."
---

# Hook

Textarea and select look different in HTML but work identically to input in React — controlled via value and onChange. Knowing the few differences prevents common bugs.

# Lore Introduction

*"The Academy's forms use different vessels — scrolls, ledgers, tiles — but all follow the same protocol: one authoritative value, one update channel."*

# Core Learning

## Concept Introduction

```jsx
// Textarea — same as input
<textarea
  value={message}
  onChange={e => setMessage(e.target.value)}
  rows={4}
/>

// Single select
<select value={country} onChange={e => setCountry(e.target.value)}>
  <option value="">-- choose --</option>
  <option value="uk">United Kingdom</option>
  <option value="us">United States</option>
</select>

// Multi-select
<select
  multiple
  value={selected}  // array
  onChange={e => setSelected(Array.from(e.target.selectedOptions, o => o.value))}
>
  <option value="html">HTML</option>
  <option value="css">CSS</option>
  <option value="js">JavaScript</option>
</select>
```

## Mini Summary
- ✔ Textarea: value prop (not content between tags)
- ✔ Select: value on `<select>`, not selected on `<option>`
- ✔ Multi-select: value is an array; read selectedOptions on change

# Solo Practice Quest

Build a feedback form: a textarea for comments, a single select for rating (1–5), and a multi-select for topics. Log all values on submit.

# Integration

**Psychology — Consistency and Mental Models:** React standardises textarea and select to match input — all use value + onChange. This consistency reduces the cognitive overhead of remembering different APIs for different element types.

# Lore Conclusion

*"Different form elements, one protocol. Consistency is a design principle, not an accident."*

---
