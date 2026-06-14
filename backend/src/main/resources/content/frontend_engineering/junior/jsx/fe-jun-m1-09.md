---
id: fe-jun-m1-09
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m1
moduleTitle: "Module 1: React Foundations"
moduleGlyph: "⚛️"
moduleSortOrder: 1
topicSlug: jsx
topicTitle: "JSX"
topicSortOrder: 3
lesson: jsx_rules
title: "JSX Rules"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-07, fe-jun-m1-08]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Lists the core JSX rules from memory"
    - "Explains why each rule exists"
    - "Identifies common JSX errors"
    - "Uses Fragment syntax correctly"
  keywords: [single root, Fragment, self-closing, camelCase, className, expression, return]
  modelAnswer: |
    JSX has five key rules: return a single root element (use Fragment if needed), close all tags, use camelCase for attributes, use className/htmlFor instead of class/for, and only embed expressions (not statements) in {}. These rules exist because JSX compiles to JavaScript function calls, which have the same constraints.
guidedSteps:
  - id: fe-jun-m1-09-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What is wrong with this JSX? `return <h1>Title</h1><p>Subtitle</p>`"
    inputConfig:
      options:
        - "Nothing is wrong"
        - "JSX must return a single root element — wrap in a div or Fragment"
        - "h1 and p cannot be siblings"
        - "The strings need to be in curly braces"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["JSX must return a single root element — wrap in a div or Fragment"]
      rejectedFeedback: "JSX compiles to a function return value. A function can only return one value. Wrap multiple elements: `return <><h1>Title</h1><p>Subtitle</p></>`."
    hint: "What does a function return?"
    reflectionPrompt: "Fragment (<></>) is shorthand for <React.Fragment>. It wraps elements without adding an extra DOM node — perfect for keeping the DOM clean."
  - id: fe-jun-m1-09-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Fix this JSX: `<img src={url} alt={alt}>` and `<input type=\"text\">`. What's wrong and what's the fix?"
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [self-closing, slash, />]
      rejectedFeedback: "Both need self-closing: `<img src={url} alt={alt} />` and `<input type=\"text\" />`. In JSX, all tags must be explicitly closed."
    hint: "In JSX, void elements must be self-closed."
    reflectionPrompt: "Self-closing tags in JSX make structure unambiguous. There's no implicit closing — every element explicitly declares whether it has children."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which JSX attribute name is correct?"
    options:
      - "`<label for=\"email\">`"
      - "`<label htmlFor=\"email\">`"
      - "`<label html-for=\"email\">`"
      - "`<label labelFor=\"email\">`"
    correctIndex: 1
    feedback: "`for` is a reserved keyword in JavaScript (used in `for` loops). JSX uses `htmlFor` to map to the DOM `for` attribute. This is one of several attribute name differences between JSX and HTML."
retrieval:
  recall: "List four rules that JSX enforces that HTML does not."
  explain: "Why must JSX return a single root element?"
  mistakeId:
    code: "<div style=\"color: red\">text</div>"
    answer: "In JSX, the style attribute accepts a JavaScript object, not a CSS string: `<div style={{ color: 'red' }}>text</div>`. The outer `{}` embeds a JS expression; the inner `{}` is the object literal. Property names are camelCase: `backgroundColor`, not `background-color`."
---

# Hook

JSX has rules. Not arbitrary ones — each rule exists because JSX is JavaScript in disguise. Once you understand *why* each rule exists, you stop memorising them and start deriving them. This lesson covers the essential rules you'll hit in your first week of React.

# Lore Introduction

*"Every magical language has its grammar,"* Aelindra says. *"Not to punish the learner — but because the incantation must be unambiguous. An ambiguous spell is a dangerous spell. JSX's rules make its meaning precise."*

# Core Learning

## Concept Introduction

The five JSX rules:

| Rule | Wrong | Right |
|---|---|---|
| Single root | `return <h1/><p/>` | `return <><h1/><p/></>` |
| Close all tags | `<img src={x}>` | `<img src={x} />` |
| camelCase attributes | `onclick` | `onClick` |
| Reserved keywords | `class=`, `for=` | `className=`, `htmlFor=` |
| Style as object | `style="color:red"` | `style={{ color: 'red' }}` |

## Why It Matters

Breaking these rules causes compile-time errors — your app won't build. Understanding why each rule exists means you can fix errors faster and avoid them in the first place.

## Worked Example

```jsx
// All five rules applied correctly
function FormField({ id, label, value, onChange }) {
  return (
    <>
      <label htmlFor={id}>{label}</label>
      <input
        id={id}
        type="text"
        value={value}
        onChange={onChange}
        className="form-input"
        style={{ borderRadius: '4px' }}
      />
    </>
  );
}
```

Fragment wraps two elements. `htmlFor` on label. Self-closed input. camelCase events. `className`. Object-style.

## Common Mistakes

- **`style="..."` as a string.** Must be an object: `style={{ color: 'red' }}`.
- **HTML comments in JSX.** `<!-- comment -->` is invalid. Use `{/* comment */}`.
- **Inline event handlers with quotes.** `onClick="handler()"` is wrong; `onClick={handler}` is correct.

## Mental Model

JSX's rules feel like grammar pedantry until you remember what JSX *is* — function calls in disguise — at which point every rule becomes mechanical necessity. Use customs paperwork as the model: JSX is a declaration form processed by a strict but consistent officer (the compiler), and each rule exists because of how the form is filed. One root element per return: a function call returns *one* value, so multiple siblings need one container (or a Fragment — a box with no label that vanishes after inspection). Every tag closes, including `<img />`: the officer can't infer where your declaration ends the way browsers forgivingly do with HTML. `className` and `htmlFor`: the form is written in JavaScript, where `class` and `for` are already officials with other jobs. Keys on list items: cargo manifests need stable IDs per crate so the officer can track which crate moved rather than re-inspecting the whole shipment. None of these are style preferences — break one and the form simply doesn't parse. Learn the *reason* per rule and you'll never need to memorise the rule itself.

## Mini Summary

- JSX must return one root element — use Fragment `<>` to avoid extra DOM nodes
- All tags must be closed
- Attributes are camelCase; `class` → `className`, `for` → `htmlFor`
- Style is an object, not a string

# Guided Practice Quest

Work through the guided steps to identify and fix JSX rule violations.

# Solo Practice Quest

You receive this broken JSX from a teammate. Fix all errors and explain each fix:

```jsx
function LoginForm() {
  return (
    <h1>Login</h1>
    <form>
      <label for="email">Email</label>
      <input type="text" id="email" class="field">
      <button onclick="handleSubmit()" style="background-color: blue">
        Submit
      </button>
    </form>
  )
}
```

# Integration

**Design — Constraint-Driven Creativity**

JSX's rules are constraints. Design research shows that constraints often improve creative output — they focus effort and eliminate unproductive options. The same principle applies to code: a language with clear, consistent rules is easier to read, teach, and tool. JSX's strictness enables powerful IDE support (autocomplete, error highlighting) that would be impossible with a more permissive syntax. Constraints enable tooling; tooling enables productivity.

# Lore Conclusion

*"The rules are not a cage,"* Aelindra says. *"They are a grammar. And a grammar, once learned, becomes invisible — leaving only the thought it expresses."*

---
