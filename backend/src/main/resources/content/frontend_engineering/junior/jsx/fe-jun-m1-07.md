---
id: fe-jun-m1-07
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
lesson: what_is_jsx
title: "What is JSX?"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-04]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what JSX is and what it compiles to"
    - "Distinguishes JSX from HTML"
    - "Explains why JSX exists"
    - "Identifies JSX-specific syntax differences from HTML"
  keywords: [JSX, HTML, JavaScript, compile, Babel, createElement, syntax, transform]
  modelAnswer: |
    JSX is a syntax extension for JavaScript that looks like HTML but compiles to JavaScript function calls (React.createElement). It lets developers write UI structure in a familiar HTML-like syntax while keeping it in the same file as the logic. JSX is not HTML — it has different attribute names (className, htmlFor) and requires all tags to be closed.
guidedSteps:
  - id: fe-jun-m1-07-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What does JSX compile to?"
    inputConfig:
      options:
        - "HTML strings injected into the DOM"
        - "React.createElement() function calls"
        - "CSS class definitions"
        - "TypeScript type annotations"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["React.createElement() function calls"]
      rejectedFeedback: "Babel transforms JSX into React.createElement() calls. `<div>Hello</div>` becomes `React.createElement('div', null, 'Hello')`. JSX is syntactic sugar."
    hint: "JSX is transformed by a build tool before it reaches the browser."
    reflectionPrompt: "Because JSX is syntactic sugar over function calls, anything you can do in JSX you can do with createElement. JSX just makes it readable."
  - id: fe-jun-m1-07-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Name two differences between JSX and HTML."
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [className, class, htmlFor, for, camelCase, self-closing, expression]
      rejectedFeedback: "Key differences: JSX uses `className` not `class`, `htmlFor` not `for`, camelCase event names (`onClick` not `onclick`), all tags must be self-closed if empty, and JS expressions go in `{}`."
    hint: "Think about attribute names and how to embed JavaScript."
    reflectionPrompt: "JSX differences from HTML exist because JSX is JavaScript. `class` is a reserved JS keyword, so `className` is used instead."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why does JSX use `className` instead of `class`?"
    options:
      - "It's a React convention with no technical reason"
      - "`class` is a reserved keyword in JavaScript, so `className` avoids the conflict"
      - "CSS doesn't support the `class` attribute"
      - "It's required by TypeScript"
    correctIndex: 1
    feedback: "`class` is a reserved JavaScript keyword (used for class declarations). Since JSX is compiled to JavaScript, using `class` as an attribute would cause a syntax conflict. `className` maps to the DOM property of the same name."
retrieval:
  recall: "What build tool transforms JSX into JavaScript?"
  explain: "Write the React.createElement equivalent of `<button className=\"primary\">Click</button>`."
  mistakeId:
    code: "JSX is just HTML inside JavaScript"
    answer: "JSX is a JavaScript syntax extension that *looks* like HTML but has key differences: different attribute names, expression syntax with {}, self-closing required, and it compiles to function calls — not HTML strings."
---

# Hook

When you first see React code, it looks strange: HTML nested inside a JavaScript function, mixed together freely. This is JSX — and it's one of the first things that confuses newcomers. Is it HTML? Is it JavaScript? The answer: it's JavaScript that looks like HTML. A syntax extension. Syntactic sugar over function calls.

Once you understand what JSX *is* and what it *becomes*, it stops being magic.

# Lore Introduction

*"Apprentices often mistake the Academy's runic shorthand for the true incantation,"* Aelindra says. *"The shorthand is faster to write and easier to read — but the library transforms it into the real spell before it is cast. Understanding the transformation is understanding the magic."*

# Core Learning

## Concept Introduction

JSX is a **syntax extension** for JavaScript. It lets you write HTML-like markup inside JS. A build tool (Babel/Vite) transforms it into `React.createElement()` calls before the browser sees it.

```jsx
// What you write (JSX)
const element = <h1 className="title">Hello</h1>;

// What it becomes (JavaScript)
const element = React.createElement('h1', { className: 'title' }, 'Hello');
```

| JSX | HTML equivalent | Reason |
|---|---|---|
| `className` | `class` | `class` is reserved in JS |
| `htmlFor` | `for` | `for` is reserved in JS |
| `onClick` | `onclick` | camelCase convention in JS |
| `{expression}` | N/A | Embed any JS value |
| Self-close `<img />` | `<img>` | Required in JSX |

## Why It Matters

JSX keeps structure and logic together — in the same component. This colocation reduces context-switching. You can see exactly what a component renders and why in one place.

## Worked Example

```jsx
function ProductCard({ name, price, inStock }) {
  return (
    <div className={inStock ? 'card' : 'card card--disabled'}>
      <h2>{name}</h2>
      <p>${price.toFixed(2)}</p>
      {inStock && <span className="badge">In Stock</span>}
    </div>
  );
}
```

JSX expressions: class conditionals, formatted values, conditional rendering — all in a readable, HTML-like structure.

## Common Mistakes

- **Using `class` instead of `className`.** This is a very common beginner error.
- **Forgetting to close self-closing tags.** `<img>` must be `<img />` in JSX.
- **Multiple root elements without a Fragment.** JSX must return a single root. Use `<>...</>`.
- **Treating JSX as a string.** You can't do `"<div>" + variable + "</div>"` — use JSX expressions.

## Mini Summary

- JSX is JavaScript syntax extension that looks like HTML
- It compiles to React.createElement() calls
- Key differences from HTML: className, htmlFor, camelCase events, self-closing
- Use `{expression}` to embed JavaScript values

# Guided Practice Quest

Work through the guided steps to confirm you understand JSX's nature and key differences from HTML.

# Solo Practice Quest

Write a JSX component called `AlertBanner` that accepts `message` (string), `type` (`'success' | 'error' | 'warning'`), and `visible` (boolean) props. It should render nothing when `visible` is false, and a div with a class name based on `type` and the message text when visible. Write it in full JSX, then in a comment describe what the createElement equivalent would look like.

# Integration

**Psychology — Cognitive Fluency**

JSX improves cognitive fluency — the ease with which the brain processes information. UI structure written as markup (JSX) is faster to parse visually than nested function calls (`React.createElement`). The brain recognises tree structures in indented markup far more readily than equivalent function calls. This is why JSX was invented: not for technical necessity, but for human readability. Tool design that reduces cognitive friction leads to fewer errors and faster comprehension.

# Lore Conclusion

*"The shorthand is a gift to the reader,"* Aelindra says. *"Not to the compiler — the compiler is patient. But you, the weaver who reads this code at midnight, need clarity. JSX is clarity."*

---
