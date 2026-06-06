---
id: fe-app-m5-03
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m5
moduleTitle: "Module 5: JavaScript Foundations"
moduleGlyph: "⚡"
moduleSortOrder: 5
topicSlug: javascript_basics
topicTitle: "JavaScript Basics"
topicSortOrder: 1
lesson: data_types
title: "Data Types"
sortOrder: 3
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m5-02]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Names at least four JavaScript primitive types correctly"
    - "Gives a real-world example of when you would use each type"
    - "Distinguishes between a string number ('42') and a numeric number (42)"
    - "Explains what typeof returns and gives one example"
    - "Describes what undefined and null mean and how they differ"
  keywords: [string, number, boolean, undefined, null, typeof, primitive, type]
  modelAnswer: |
    JavaScript has six primitive types: string (text), number (integers and decimals),
    boolean (true/false), undefined (declared but not assigned), null (intentionally
    empty), and symbol. typeof checks a value's type at runtime. '42' is a string —
    it looks like a number but behaves like text; 42 is a number used in arithmetic.
guidedSteps:
  - id: js-types-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the type of the value `"hello"`?
    inputConfig:
      options:
        - "number"
        - "boolean"
        - "string"
        - "object"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["string"]
      rejectedFeedback: "Anything wrapped in quotes — single, double, or backtick — is a string in JavaScript, even if it looks like a number. '42' is a string, not a number."
    hint: "Text in quotation marks is always a ___."
    reflectionPrompt: "Correct. Strings are sequences of characters wrapped in quotes. They can contain letters, numbers, spaces — any text."

  - id: js-types-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      What does `typeof true` return?
    inputConfig:
      placeholder: "type name"
    markingRule:
      matchMode: NORMALIZED
      accepted: [boolean, "boolean"]
      rejectedFeedback: "true and false are boolean values. typeof true returns 'boolean'. Booleans represent on/off, yes/no, true/false conditions."
    hint: "true and false are the only two values of this type — it's named after mathematician George Boole."
    reflectionPrompt: "Correct. typeof returns a string describing the type. typeof true → 'boolean', typeof 42 → 'number', typeof 'hi' → 'string'."

  - id: js-types-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the difference between `undefined` and `null` in 1–2 sentences.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [undefined, null, declared, assigned, intentional, empty, deliberate]
      rejectedFeedback: "undefined means a variable exists but hasn't been given a value yet. null means you deliberately set a variable to 'nothing'. One is accidental, one is intentional."
    hint: "Which one appears automatically when you declare a variable without assigning it? Which one do you set on purpose to mean 'no value'?"
    reflectionPrompt: "Good distinction. undefined is JavaScript's way of saying 'this exists but has no value yet'. null is the developer's way of saying 'this intentionally has no value'."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `typeof 42` return in JavaScript?"
    options:
      - "'integer'"
      - "'number'"
      - "'float'"
      - "'numeric'"
    correctIndex: 1
    feedback: "JavaScript uses a single 'number' type for all numeric values — both integers and decimals. There is no separate 'integer' or 'float' type."
  - type: MULTIPLE_CHOICE
    question: "Which of the following is a boolean value?"
    options:
      - "'true'"
      - "1"
      - "true"
      - "yes"
    correctIndex: 2
    feedback: "'true' (in quotes) is a string. 1 is a number. true without quotes is a boolean. yes is not a valid JavaScript value."

retrieval:
  recall: "List the six primitive data types in JavaScript."
  explain: "Explain why '42' and 42 are different in JavaScript, and what problem mixing them up might cause."
  mistakeId:
    code: "let age = '25'; let nextAge = age + 1; // expected 26"
    answer: "'25' is a string, not a number. Adding 1 to a string concatenates them: '25' + 1 = '251'. You must convert the string first: let nextAge = Number(age) + 1;"
---

# Hook

Not all information is the same kind of thing.

A person's name is text. Their age is a number. Whether they are logged in is a yes-or-no. A missing value is something else entirely. Programming languages — including JavaScript — have distinct types to represent these different kinds of data.

Understanding types prevents a whole class of bugs that beginners hit constantly: adding a number to a string and getting unexpected results, comparing values that should match but don't, or wondering why your calculation returns `NaN`.

> Have you ever typed a number in a text field and then been surprised when a calculation went wrong? That was likely a type mismatch.

# Lore Introduction

Master Aelindra lays six scrolls on the workbench, each a different colour.

*"Enchantments are not all the same substance,"* she says. *"Fire is not the same as ice. Words are not numbers. A yes-or-no is not a poem. The Scribe's first error is treating unlike things alike — pouring water into a flame vessel and wondering why nothing burns."*

She taps each scroll in turn.

*"Six fundamental substances. Learn to identify them by sight, and half your errors vanish before they begin."*

# Core Learning

## Concept Introduction

JavaScript has six **primitive types** — the fundamental building blocks of all values:

| Type | What it holds | Example |
|------|--------------|---------|
| `string` | Text | `"hello"`, `'Elara'`, `` `score: ${n}` `` |
| `number` | Integers and decimals | `42`, `3.14`, `-7` |
| `boolean` | True or false | `true`, `false` |
| `undefined` | Declared, not yet assigned | `let x; // x is undefined` |
| `null` | Intentionally empty | `let user = null;` |
| `symbol` | Unique identifier (advanced) | `Symbol('id')` |

You can check any value's type at runtime with `typeof`:

```js
typeof "hello"    // "string"
typeof 42         // "number"
typeof true       // "boolean"
typeof undefined  // "undefined"
typeof null       // "object"  ← known quirk in JavaScript
```

## Why It Matters

JavaScript is a *dynamically typed* language — you don't declare the type of a variable, you just assign a value and JavaScript infers the type. This is flexible but dangerous: you can accidentally mix types in ways that produce silent bugs.

Understanding types lets you write defensive code, interpret error messages correctly, and avoid the class of bugs known as **type coercion errors**.

## Worked Examples

**Example 1 — Type coercion surprise**

```js
let price = "10";     // string
let quantity = 3;     // number
let total = price * quantity;
console.log(total);   // 30 ← JavaScript converts "10" to 10 automatically
```

Multiplication works because JavaScript converts the string. But:

```js
let a = "5";
let b = 3;
console.log(a + b);  // "53" ← concatenation, not addition!
```

The `+` operator on a string triggers concatenation, not arithmetic. This is one of JavaScript's most common gotchas.

**Example 2 — Checking types**

```js
let age = 25;
console.log(typeof age);        // "number"

let name = "Aelindra";
console.log(typeof name);       // "string"

let isActive = true;
console.log(typeof isActive);   // "boolean"
```

**Example 3 — Converting types explicitly**

```js
let input = "42";           // string from a form field
let numeric = Number(input); // convert to number
let asText = String(42);     // convert to string
```

Explicit conversion is safer than relying on JavaScript to infer the right type.

## Common Mistakes

- Treating `'42'` (string) the same as `42` (number) — they behave differently with `+`
- Confusing `null` and `undefined` — both mean "no value" but in different ways
- Relying on `typeof null === "object"` being meaningful — it is a historical bug in JavaScript
- Forgetting that all numbers in JavaScript are floating-point — there is no separate integer type

## Mental Model

Think of types as the different **containers** in a chemistry lab.

A beaker holds liquid. A gas jar holds gas. A balance holds solids. You cannot put a gas in a beaker and expect it to behave like a liquid.

Types tell JavaScript (and you) what container a value lives in. Mix them without care, and the experiment doesn't go as planned.

## Mini Summary

- JavaScript has six primitive types: `string`, `number`, `boolean`, `undefined`, `null`, `symbol`
- Use `typeof` to inspect a value's type at runtime
- `+` behaves differently depending on types — it concatenates strings but adds numbers
- `undefined` = not yet assigned; `null` = deliberately set to nothing
- Convert explicitly with `Number()`, `String()`, `Boolean()` when needed

# Guided Practice Quest

In this quest you will identify types, predict what `typeof` returns, and spot the type mismatch in a buggy calculation.

Your three guided steps will sharpen your type intuition before you start writing code that depends on it.

# Solo Practice Quest

In the browser console or a text editor, write and run the following:

1. Declare variables of each type: one string, one number, one boolean, one that is `null`, and one that is `undefined`
2. Use `typeof` on each and `console.log` the results
3. Deliberately try `"5" + 3` and `"5" * 3` — observe and write one sentence explaining why the results differ

# Integration

**Connecting to Mathematics — Number Systems and Representation**

In mathematics, numbers are abstract. But computers must represent them in a concrete, finite format. JavaScript's `number` type uses the IEEE 754 double-precision floating-point standard — the same used in most programming languages.

This has a consequence that surprises many beginners:

```js
console.log(0.1 + 0.2); // 0.30000000000000004
```

This is not a bug in your code. It is a consequence of how binary floating-point arithmetic works. The decimal `0.1` cannot be represented exactly in binary — the same way `1/3` cannot be written exactly in decimal. Understanding data types at even a shallow level reveals that computers have limits built into their mathematics.

# Lore Conclusion

The apprentice studies the six scrolls, touching each in turn.

*"Good,"* says Master Aelindra. *"You can name the vessels now. In the next lesson, you will learn what can be done with their contents — how values are combined, compared, and transformed."*

The six scrolls glow softly in their sequence: text, number, truth, absence, nothing, and the subtle shimmer of the symbol.

---
