---
id: fe-app-m5-05
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
lesson: functions
title: "Functions"
sortOrder: 5
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m5-04]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines a function using function keyword or arrow syntax correctly"
    - "Function accepts at least one parameter and uses it inside the body"
    - "Function returns a value using the return keyword"
    - "Function is called at least once and the result is logged"
    - "Explains the difference between defining a function and calling it"
  keywords: [function, parameter, argument, return, call, invoke, arrow, define]
  modelAnswer: |
    A function is a named, reusable block of code. It is defined once (with function
    or => syntax) and called whenever needed. Parameters are placeholders in the
    definition; arguments are the actual values passed when calling. The return
    keyword sends a value back to the caller. Functions make code reusable and testable.
guidedSteps:
  - id: js-fn-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the difference between **defining** a function and **calling** it?
    inputConfig:
      options:
        - "There is no difference — writing a function runs it"
        - "Defining creates the function; calling executes it"
        - "Calling creates the function; defining runs it"
        - "Functions run automatically when the page loads"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Defining creates the function; calling executes it"]
      rejectedFeedback: "Defining a function is like writing a recipe — the food isn't made yet. Calling (invoking) the function is actually cooking the recipe."
    hint: "Writing a recipe and cooking the recipe are two different things."
    reflectionPrompt: "Exactly. A function definition is a blueprint. Nothing runs until you call it with functionName()."

  - id: js-fn-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the arrow function so it doubles a number:

      ```js
      const double = (n) => ___;
      ```
    inputConfig:
      placeholder: "return expression"
    markingRule:
      matchMode: CONTAINS
      accepted: ["n * 2", "n*2", "2 * n", "2*n"]
      rejectedFeedback: "The function should return the parameter multiplied by 2: n * 2."
    hint: "The function receives n and should return twice that value."
    reflectionPrompt: "Correct. Arrow functions with a single expression can return it implicitly: const double = n => n * 2; — no curly braces or return keyword needed."

  - id: js-fn-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in your own words why using functions is better than repeating the same
      code in multiple places.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [reuse, repeat, change, once, maintain, update, DRY]
      rejectedFeedback: "Think about what happens when you need to fix a bug or change behaviour. If the same code appears in 10 places, you must fix it 10 times. A function fixes it once."
    hint: "If you had to change the logic, how many places would you update if you had a function versus repeated code?"
    reflectionPrompt: "Well stated. This is the DRY principle — Don't Repeat Yourself. Functions are the primary mechanism for avoiding repetition in code."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does a function return if it has no return statement?"
    options:
      - "0"
      - "null"
      - "undefined"
      - "false"
    correctIndex: 2
    feedback: "A function with no return statement (or an empty return) returns undefined. This is a common source of bugs when you forget to add return."
  - type: MULTIPLE_CHOICE
    question: "What are the values passed to a function when it is called called?"
    options:
      - "Parameters"
      - "Arguments"
      - "Inputs"
      - "Variables"
    correctIndex: 1
    feedback: "Parameters are the named placeholders in the function definition (function add(a, b)). Arguments are the actual values passed when calling (add(3, 5)). Both terms are often used interchangeably in practice."

retrieval:
  recall: "Write the syntax for an arrow function that takes two numbers and returns their sum."
  explain: "Explain what a return value is and why it matters."
  mistakeId:
    code: "function greet(name) { 'Hello, ' + name; }"
    answer: "The function builds the string but never returns it. Without a return statement, the function returns undefined. Fix: return 'Hello, ' + name;"
---

# Hook

Imagine writing the same calculation twelve times in your code. Now imagine discovering a bug in that calculation. You must fix it twelve times. Miss one, and your code is inconsistent.

Functions solve this problem. Write the logic once, give it a name, and call it wherever you need it. Fix a bug once, and it is fixed everywhere.

Functions are not just a convenience — they are the fundamental unit of organisation in all JavaScript code.

> Think of a task you do repeatedly every day. How would you describe it as a recipe that could be followed by anyone?

# Lore Introduction

Master Aelindra places a sealed vial on the workbench. Inside, a tiny flame dances.

*"This encantation burns the same way every time it is opened — precisely the same flame, precisely the same heat. I wrote it once. I have used it a thousand times."*

She gestures to a shelf of similar vials, each labelled with a runic name.

*"A function is a named, sealed enchantment. You craft it once with care. From that moment, any scribe in the workshop can invoke its name and receive its power — without knowing the details of how it was made."*

# Core Learning

## Concept Introduction

A **function** is a named, reusable block of code that performs a specific task.

**Function declaration syntax:**

```js
function functionName(parameter1, parameter2) {
  // code to run
  return result;
}
```

**Arrow function syntax (modern):**

```js
const functionName = (parameter1, parameter2) => {
  // code to run
  return result;
};
```

**Short arrow function (single expression):**

```js
const double = n => n * 2;
```

| Term | Meaning | Example |
|------|---------|---------|
| **Parameter** | Placeholder in the definition | `function add(a, b)` |
| **Argument** | Actual value passed when calling | `add(3, 5)` |
| **Return value** | The result sent back to the caller | `return a + b` |

## Why It Matters

Functions enable:
- **Reuse** — write logic once, call it everywhere
- **Clarity** — a well-named function explains what the code does
- **Testability** — a function can be tested in isolation
- **Maintainability** — change one function, fix everywhere it is used

This is the DRY principle: **Don't Repeat Yourself**. Functions are its primary tool.

## Worked Examples

**Example 1 — A simple greeting function**

```js
function greet(name) {
  return "Hello, " + name + "!";
}

console.log(greet("Elara"));   // Hello, Elara!
console.log(greet("Veylan"));  // Hello, Veylan!
```

Written once, usable anywhere with any name.

**Example 2 — Arrow function for calculation**

```js
const calculateArea = (width, height) => width * height;

console.log(calculateArea(5, 10));  // 50
console.log(calculateArea(3, 7));   // 21
```

The arrow syntax is compact and expressive for simple, single-expression functions.

**Example 3 — Function with multiple steps**

```js
function formatPrice(amount) {
  const rounded = Math.round(amount * 100) / 100;
  return "£" + rounded.toFixed(2);
}

console.log(formatPrice(5));      // £5.00
console.log(formatPrice(12.999)); // £13.00
```

Functions can contain multiple steps, handle edge cases, and encapsulate complex logic behind a simple name.

## Common Mistakes

- Writing a function but forgetting to **call** it — nothing runs unless invoked
- Forgetting the `return` keyword — the function runs but gives back `undefined`
- Using a function before it is defined (works with `function` declarations due to hoisting, but not with `const` arrow functions)
- Putting too much logic in one function — each function should do *one thing well*

## Mental Model

A function is a **vending machine**.

You press a button (call the function with arguments), the machine performs its fixed procedure (the function body), and it dispenses a result (the return value). You don't need to know what happens inside the machine to use it.

Good functions are like good vending machines: you know exactly what to put in, and exactly what you get back.

## Mini Summary

- A function is a named, reusable block of code
- It is **defined** once and **called** whenever needed
- Parameters are placeholders; arguments are the actual values passed
- Use `return` to send a value back to the caller
- Arrow functions (`=>`) are the modern syntax for concise functions
- One function should do one thing — keep them small and focused

# Guided Practice Quest

In this quest you will complete and call a partially-written function, trace through an arrow function, and explain the DRY principle using your own reasoning.

These steps build the muscle memory of writing and calling functions before you apply them in DOM manipulation and interactivity.

# Solo Practice Quest

Write three functions from scratch without guidance:

1. `square(n)` — returns n squared
2. `isEven(n)` — returns `true` if n is even, `false` otherwise
3. `fullName(firstName, lastName)` — returns the full name as a single string

Call each function with at least two different sets of arguments and log the results. Add a comment on each explaining what it does and what it returns.

# Integration

**Connecting to Mathematics — Functions in Algebra**

Mathematical functions (f(x) = 2x + 1) and programming functions share a precise relationship: both map inputs to outputs consistently. For any given input, a *pure* function always returns the same output — just like a mathematical function.

This property is called **referential transparency**. It makes functions predictable: you can substitute a function call with its return value anywhere, and the program behaves identically. Pure functions are easier to test, debug, and reason about because they have no hidden effects.

When you write a function that only depends on its parameters and returns a value — with no side effects — you are writing a pure function. This is considered best practice, and it is the philosophy behind an entire programming paradigm: functional programming.

# Lore Conclusion

The apprentice seals a new vial and places it on the shelf. The runic label reads: `greet`.

*"Your first encantation,"* Master Aelindra says. *"Anyone in the workshop can now call it. You wrote it once, but its usefulness is unbounded."*

The shelf of named vials stretches ahead, awaiting the many functions yet to be crafted.

---
