---
id: fe-app-m5-04
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
lesson: operators
title: "Operators"
sortOrder: 4
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m5-03]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Lists the five arithmetic operators and gives an example of each"
    - "Explains the difference between == and === with an example"
    - "Correctly uses at least one comparison operator in a boolean expression"
    - "Demonstrates the logical AND (&&) and OR (||) operators"
    - "Explains what the result of a comparison expression is (true or false)"
  keywords: [operator, arithmetic, comparison, logical, ===, &&, ||, boolean, expression]
  modelAnswer: |
    Arithmetic operators (+, -, *, /, %) perform maths. Comparison operators (===, !==,
    >, <, >=, <=) compare two values and return a boolean. === checks both value and
    type (strict equality), while == only checks value and can coerce types, causing
    bugs. Logical operators && (AND) and || (OR) combine boolean expressions.
guidedSteps:
  - id: js-ops-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the result of `10 % 3`?
    inputConfig:
      options:
        - "3"
        - "1"
        - "0"
        - "3.33"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["1"]
      rejectedFeedback: "% is the modulo operator — it gives the remainder after division. 10 ÷ 3 = 3 remainder 1, so 10 % 3 = 1."
    hint: "The % operator returns the *remainder* of division, not the quotient."
    reflectionPrompt: "Correct. Modulo is useful for things like checking if a number is even (n % 2 === 0) or cycling through a list of items."

  - id: js-ops-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Which operator should you use to check if two values are **equal in value AND type**?
    inputConfig:
      placeholder: "=== or =="
    markingRule:
      matchMode: NORMALIZED
      accepted: ["==="]
      rejectedFeedback: "=== is strict equality — it checks both value and type. == is loose equality and performs type coercion, which causes surprising results like '5' == 5 being true."
    hint: "The 'strict' equality operator uses three equals signs."
    reflectionPrompt: "Always prefer ===. It avoids JavaScript's type coercion rules, which are a frequent source of bugs for beginners and experts alike."

  - id: js-ops-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      What does `true && false` evaluate to, and why?
    inputConfig:
      minWords: 8
    markingRule:
      matchMode: CONTAINS
      accepted: [false, both, AND, all, one, require]
      rejectedFeedback: "AND (&&) requires BOTH sides to be true. If either side is false, the whole expression is false. true && false → false."
    hint: "Think of AND as a double lock: both locks must be open for the door to open."
    reflectionPrompt: "Correct. && is 'both must be true'. || is 'at least one must be true'. These combinators let you express complex conditions concisely."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `5 === '5'` return in JavaScript?"
    options:
      - "true"
      - "false"
      - "undefined"
      - "NaN"
    correctIndex: 1
    feedback: "=== checks both value and type. 5 is a number and '5' is a string — different types, so the result is false. == would return true, which is why === is safer."
  - type: MULTIPLE_CHOICE
    question: "What does the `!` (NOT) operator do to a boolean?"
    options:
      - "Doubles it"
      - "Inverts it — true becomes false, false becomes true"
      - "Converts it to a number"
      - "Returns undefined"
    correctIndex: 1
    feedback: "! is the logical NOT operator. !true === false, !false === true. It flips the boolean value."

retrieval:
  recall: "What are the six comparison operators in JavaScript?"
  explain: "Explain why === is preferred over == in JavaScript with a concrete example."
  mistakeId:
    code: "if (userInput == true) { // intended to check a boolean }"
    answer: "Using == with true triggers type coercion. '1' == true evaluates to true, as does '1' == 1. Use === true to check that the value is strictly the boolean true, not just truthy."
---

# Hook

Operators are the verbs of programming.

They take values and transform them. They compare things and produce answers. They combine conditions and yield decisions. Without operators, your code can store data but cannot *do* anything with it.

In this lesson you will learn three families of operators: arithmetic (maths), comparison (is this true?), and logical (combining conditions). Together they form the engine of every decision your JavaScript code will ever make.

> If you were designing a simple quiz app, which operators do you think you'd need to check if an answer is correct?

# Lore Introduction

Master Aelindra unrolls a scroll covered in small symbols — plus signs, arrows, ampersands.

*"The vessels hold your power,"* she says, *"but the runes transform it. These are operators — the actions your enchantments can perform. Add one quantity to another. Test whether one thing equals another. Combine two conditions into one verdict."*

She taps a `===` symbol.

*"This rune asks: are these two things truly the same — in value AND in kind? Not merely similar. Truly identical. Ask the right question or receive the wrong answer."*

# Core Learning

## Concept Introduction

### Arithmetic Operators

| Operator | Name | Example | Result |
|----------|------|---------|--------|
| `+` | Addition | `5 + 3` | `8` |
| `-` | Subtraction | `10 - 4` | `6` |
| `*` | Multiplication | `6 * 7` | `42` |
| `/` | Division | `15 / 4` | `3.75` |
| `%` | Modulo (remainder) | `10 % 3` | `1` |

### Comparison Operators — return `true` or `false`

| Operator | Meaning | Example | Result |
|----------|---------|---------|--------|
| `===` | Strict equality | `5 === 5` | `true` |
| `!==` | Strict inequality | `5 !== 6` | `true` |
| `>` | Greater than | `7 > 3` | `true` |
| `<` | Less than | `2 < 9` | `true` |
| `>=` | Greater or equal | `5 >= 5` | `true` |
| `<=` | Less or equal | `3 <= 4` | `true` |

### Logical Operators — combine boolean expressions

| Operator | Meaning | Example | Result |
|----------|---------|---------|--------|
| `&&` | AND — both must be true | `true && false` | `false` |
| `\|\|` | OR — at least one must be true | `true \|\| false` | `true` |
| `!` | NOT — inverts the value | `!true` | `false` |

## Why It Matters

Every condition in your code uses operators. Every calculation uses operators. Every validation — "is this field empty?", "is the score above 100?", "has the user agreed to the terms?" — is built from comparison and logical operators.

Without operators, your code could store information but could never reason about it.

## Worked Examples

**Example 1 — Arithmetic in a shopping cart**

```js
const price = 29.99;
const quantity = 3;
const total = price * quantity;
console.log(total); // 89.97
```

**Example 2 — Comparison for access control**

```js
const userAge = 17;
const canVote = userAge >= 18;
console.log(canVote); // false
```

**Example 3 — Logical combination**

```js
const isLoggedIn = true;
const isAdmin = false;
const canEditContent = isLoggedIn && isAdmin;
console.log(canEditContent); // false — needs BOTH
```

**Example 4 — The strict equality trap**

```js
console.log(5 == "5");   // true  ← loose equality, type coerced
console.log(5 === "5");  // false ← strict equality, types differ
```

Always use `===` unless you have a specific reason for `==`.

## Common Mistakes

- Using `=` for comparison instead of `===` — `=` is assignment, `===` is comparison
- Using `==` instead of `===` — type coercion produces unexpected truths
- Forgetting operator precedence — `*` and `/` are evaluated before `+` and `-`
- Confusing `&&` and `||` — AND requires both; OR requires just one

## Mental Model

Think of `===` as asking: *"Are these two things the same name badge AND the same face?"*

`==` only checks the name badge — even if the face is completely different.

And `&&` is like two security guards both needing to say "yes" before you enter. `||` is like either guard can let you in.

## Mini Summary

- Arithmetic operators: `+`, `-`, `*`, `/`, `%`
- Comparison operators return `true` or `false`: `===`, `!==`, `>`, `<`, `>=`, `<=`
- Always use `===` over `==` to avoid type coercion bugs
- Logical operators: `&&` (AND), `||` (OR), `!` (NOT)
- Expressions using comparison/logical operators always evaluate to a boolean

# Guided Practice Quest

In this quest you will evaluate operator expressions, identify the strict vs loose equality distinction, and combine boolean conditions using logical operators.

These three steps will prepare you to write conditionals — the topic of the next module.

# Solo Practice Quest

Without looking at the notes, write JavaScript expressions that:

1. Calculate the area of a rectangle with width 8 and height 5
2. Check if a score of 75 is greater than or equal to 60
3. Check if a user is both logged in (`true`) and has confirmed their email (`true`)
4. Use `!` to check that an error message variable is NOT an empty string

Print each result with `console.log` and add a comment explaining what you expect.

# Integration

**Connecting to Mathematics — Boolean Algebra**

The logical operators `&&`, `||`, and `!` are not unique to JavaScript — they come from Boolean algebra, developed by George Boole in the 1840s. Boole showed that logical reasoning could be expressed mathematically, using true and false instead of numbers.

This discovery was revolutionary: it meant logic could be mechanised. A century later, engineers built computer circuits using electrical "and gates", "or gates", and "not gates" — direct physical implementations of Boole's algebra. Every `if` statement you write, every condition you evaluate, descends from a nineteenth-century mathematical insight about the nature of reasoning.

# Lore Conclusion

Master Aelindra watches the apprentice trace each operator rune in the air.

*"Now you can transform,"* she says. *"You can ask whether things are equal, whether conditions are met, whether power is sufficient. These runes are the basis of decision — and without decision, an enchantment is merely a statement, not a force."*

The scroll rolls itself back up with a soft snap.

---
