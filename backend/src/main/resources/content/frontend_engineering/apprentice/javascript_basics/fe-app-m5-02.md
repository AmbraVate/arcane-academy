---
id: fe-app-m5-02
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
lesson: variables
title: "Variables"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m5-01]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Declares a variable using let or const with a meaningful name"
    - "Explains the difference between let and const in your own words"
    - "Assigns a string value and a number value to separate variables"
    - "Uses console.log to print both variables"
    - "Avoids using var and explains why let/const are preferred"
  keywords: [variable, let, const, var, declare, assign, value, console.log]
  modelAnswer: |
    Variables in JavaScript are declared with let (for values that change) or const
    (for values that stay fixed). A declaration like let score = 0; reserves memory,
    names it score, and stores 0. console.log(score) prints the value to the console.
    const prevents reassignment, making code intentions clearer.
guidedSteps:
  - id: js-vars-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which keyword should you use for a variable whose value will **change** over time?
    inputConfig:
      options:
        - "const"
        - "let"
        - "var"
        - "fixed"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["let"]
      rejectedFeedback: "const prevents reassignment — use it when the value is fixed. var is the old way and has scope problems. let is the modern choice for changing values."
    hint: "const means constant — it cannot be reassigned. Which keyword is left for things that change?"
    reflectionPrompt: "Correct. let is for values that will change; const is for values that won't. This distinction makes your intent clear to anyone reading the code."

  - id: js-vars-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the variable declaration:

      ```js
      ___ playerName = "Elara";
      ```

      This variable stores a player's name. It won't change during the game.
    inputConfig:
      placeholder: "let or const"
    markingRule:
      matchMode: NORMALIZED
      accepted: [const]
      rejectedFeedback: "If the player's name won't change, use const — it signals clearly that this value is fixed. Reserve let for things that will be updated."
    hint: "Will playerName change? If not, which keyword signals that it's constant?"
    reflectionPrompt: "Exactly. const communicates intent — it tells the reader: 'this value is set once and never changes.' That clarity is valuable."

  - id: js-vars-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 1–2 sentences why using descriptive variable names matters.
      Use the contrast between `let x = 5` and `let retryCount = 5` in your answer.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [name, describe, understand, read, clear, purpose, meaning]
      rejectedFeedback: "Think about reading the code weeks later. Which version tells you immediately what the value represents?"
    hint: "Imagine you wrote the code and had to read it six months later. Which variable name tells you what it means without looking at the rest of the code?"
    reflectionPrompt: "Well reasoned. Code is read far more often than it is written. A name like retryCount communicates intent; x communicates nothing."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What happens if you try to reassign a const variable?"
    options:
      - "The variable silently ignores the new value"
      - "JavaScript throws a TypeError"
      - "The variable becomes a let automatically"
      - "The page reloads"
    correctIndex: 1
    feedback: "Attempting to reassign a const throws a TypeError at runtime. This is intentional — const guarantees the binding will not change."
  - type: MULTIPLE_CHOICE
    question: "Which of these is a valid JavaScript variable name?"
    options:
      - "2score"
      - "player-name"
      - "playerScore"
      - "let"
    correctIndex: 2
    feedback: "Variable names cannot start with a digit, cannot contain hyphens (that's subtraction), and cannot be reserved keywords like 'let'. camelCase like playerScore is the JavaScript convention."

retrieval:
  recall: "What are the three ways to declare a variable in JavaScript, and which is the modern preferred pair?"
  explain: "Explain the difference between let and const to someone who has never coded before."
  mistakeId:
    code: "var score = 10; var score = 20;"
    answer: "Using var allows re-declaration, which causes confusing bugs. With let, re-declaring the same variable in the same scope throws an error — which is safer. Always prefer let or const over var."
---

# Hook

Every program that does anything useful needs to remember something.

A score. A username. Whether a button has been clicked. The number of items in a cart. Without a way to store and name information, your code is nothing but arithmetic that disappears the moment it finishes.

Variables are how JavaScript remembers.

> What is the most important thing a webpage you use daily needs to keep track of?

# Lore Introduction

Master Aelindra opens a chest of small glowing vials, each labelled with a rune.

*"These are binding vessels,"* she explains. *"You name them, you fill them, and the enchantment holds whatever you pour in. Some vessels are sealed — once filled, they cannot be changed. Others remain open, ready to receive new power."*

She holds up two vials: one etched with `let`, one with `const`.

*"The sealed vessel will never lie to you — its contents are what you first inscribed. The open vessel can grow, diminish, or transform. Choose the right vessel for the right purpose. That is the first discipline of a JavaScript Scribe."*

# Core Learning

## Concept Introduction

A **variable** is a named storage location that holds a value. In JavaScript, you declare variables using two modern keywords:

| Keyword | Meaning | Use when |
|---------|---------|----------|
| `let` | Mutable binding | The value will change over time |
| `const` | Immutable binding | The value is fixed after assignment |
| `var` | Old syntax (avoid) | Legacy code only — has scope bugs |

```js
let score = 0;        // will increase as the player earns points
const playerName = "Elara"; // set once, never changes
```

## Why It Matters

Without variables, you could only write code that processes values immediately — like a calculator that shows an answer but cannot save it. Variables allow programs to accumulate state over time: tracking progress, remembering user input, and keeping multiple pieces of data in play simultaneously.

Choosing `let` vs `const` is not just syntax — it communicates *intent*. `const` tells anyone reading your code: "this value is fixed by design." That clarity prevents bugs and makes code easier to reason about.

## Worked Examples

**Example 1 — Counting clicks**

```js
let clickCount = 0;
// each time a button is clicked:
clickCount = clickCount + 1;
console.log(clickCount); // 1, then 2, then 3...
```

`let` is correct here because `clickCount` changes every time the button is pressed.

**Example 2 — A fixed configuration value**

```js
const maxRetries = 3;
const appName = "Arcane Planner";
```

These values are set once and never change. `const` makes that explicit.

**Example 3 — Printing to the console**

```js
let greeting = "Hello, apprentice!";
console.log(greeting); // Hello, apprentice!
```

`console.log()` is your primary debugging tool — it prints values to the browser's developer console.

## Common Mistakes

- Using `var` in modern JavaScript — it has function scope and allows re-declaration, causing subtle bugs
- Using `let` for everything when `const` is more appropriate — it obscures which values are meant to change
- Using names like `x`, `a`, or `temp` that give no information about the stored value
- Forgetting that `const` prevents reassignment but does NOT make objects or arrays immutable

## Mental Model

Think of `let` as a whiteboard: you can write on it, erase it, and write something new.

`const` is like engraving a name on a trophy: once inscribed, it is permanent. You cannot overwrite it.

Both are *named* — that is the key property. Without a name, you cannot refer back to the value. The name is what turns raw data into something a program can reason about.

## Mini Summary

- Variables store named values in JavaScript
- Use `let` for values that will change, `const` for values that are fixed
- Avoid `var` in modern JavaScript
- Variable names should be descriptive — they communicate intent
- `console.log(variableName)` prints the value to the browser console

# Guided Practice Quest

In this quest you will practice choosing the right variable keyword and naming variables clearly.

Your three guided steps will ask you to identify when to use `let` vs `const`, complete a declaration, and explain why naming matters — preparing you to write your first real JavaScript declarations.

# Solo Practice Quest

Open a text editor or browser console and declare the following without guidance:

- The name of your favourite book or film (use `const`)
- Your current age (use `let`)
- Whether you have completed this lesson today — `true` or `false` (use `const`)

Print all three values to the console with `console.log`. Write a sentence below each explaining why you chose `let` or `const`.

# Integration

**Connecting to Mathematics — Algebra and Substitution**

In algebra, a variable like `x` represents an unknown quantity. In programming, a variable is the opposite — it is a *known* quantity that you have assigned. The behaviour is similar: wherever you write `score` in your code, JavaScript substitutes the current value, just as you substitute `x = 5` into an equation.

The difference is that programming variables change over time. In algebra, `x` has one value for the life of an equation. In JavaScript, `let score` might hold 0, then 10, then 150 as the program runs. This dynamic quality — state that evolves — is what makes programs feel alive.

# Lore Conclusion

The apprentice seals the first vial and watches it glow steadily.

*"You have named your first vessel,"* Master Aelindra says. *"From this point forward, your enchantments can remember. They can accumulate. They can change."*

The chest of vials waits, still largely full. Many values will be named before this craft is mastered.

---
