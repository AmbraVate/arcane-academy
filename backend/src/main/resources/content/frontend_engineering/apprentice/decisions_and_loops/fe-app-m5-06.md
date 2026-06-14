---
id: fe-app-m5-06
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m5
moduleTitle: "Module 5: JavaScript Foundations"
moduleGlyph: "⚡"
moduleSortOrder: 5
topicSlug: decisions_and_loops
topicTitle: "Decisions and Loops"
topicSortOrder: 2
lesson: conditionals
title: "Conditionals"
sortOrder: 1
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m5-04]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes a correct if/else statement with a boolean condition"
    - "Uses a comparison operator (===, >, <) in the condition"
    - "Adds at least one else if branch for a middle case"
    - "Explains what happens when the condition is false and there is no else"
    - "Avoids a common mistake: using = instead of === in the condition"
  keywords: [if, else, condition, boolean, branch, comparison, truthy, falsy]
  modelAnswer: |
    An if statement runs a block of code only when the condition is true. else if
    handles additional cases; else is the fallback. Conditions use comparison operators
    and evaluate to boolean true or false. Using = instead of === in a condition is
    a common bug — assignment always evaluates to truthy, making the condition always
    pass.
guidedSteps:
  - id: js-cond-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does the `else` block in an `if/else` statement do?
    inputConfig:
      options:
        - "Runs when the condition is true"
        - "Runs when the condition is false"
        - "Runs regardless of the condition"
        - "Cancels the if statement"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Runs when the condition is false"]
      rejectedFeedback: "else is the fallback path — it runs only when the if condition evaluates to false."
    hint: "if is the 'yes' path. else is the ___."
    reflectionPrompt: "Correct. if handles the true case; else handles the false case. Together they guarantee a response for every possibility."

  - id: js-cond-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the condition so the message only prints for adults (age 18 or over):

      ```js
      if (age ___ 18) {
        console.log("Welcome!");
      }
      ```
    inputConfig:
      placeholder: "comparison operator"
    markingRule:
      matchMode: NORMALIZED
      accepted: [">=", "> 17"]
      rejectedFeedback: "Use >= (greater than or equal to) to include age 18 exactly. > 17 would also work but is less clear."
    hint: "You want 18 to pass as well as 19, 20, 21... which operator includes the boundary value?"
    reflectionPrompt: "Correct. >= includes the boundary value. > 18 would exclude 18-year-olds, which would be a logic bug."

  - id: js-cond-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      What is a "truthy" value in JavaScript? Give one example of a value that is
      falsy (evaluates as false in a condition).
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [falsy, truthy, false, 0, null, undefined, empty, NaN]
      rejectedFeedback: "Falsy values in JavaScript: false, 0, '', null, undefined, NaN. Everything else is truthy — including '0' (non-empty string) and [] (empty array)."
    hint: "The six falsy values are: false, 0, '', null, undefined, NaN."
    reflectionPrompt: "Good. JavaScript conditions don't require strict booleans — they evaluate any value as truthy or falsy. Knowing the falsy list prevents subtle conditional bugs."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What happens if you write `if (x = 5)` instead of `if (x === 5)`?"
    options:
      - "JavaScript throws an error"
      - "The condition always evaluates to true (5 is truthy)"
      - "The condition always evaluates to false"
      - "It works the same as ==="
    correctIndex: 1
    feedback: "x = 5 is assignment, not comparison. It assigns 5 to x and evaluates to 5, which is truthy — so the if block ALWAYS runs. This is a common bug."
  - type: MULTIPLE_CHOICE
    question: "Which value is falsy in JavaScript?"
    options:
      - "'false'"
      - "[]"
      - "0"
      - "1"
    correctIndex: 2
    feedback: "0 is falsy. '\"false\"' (a non-empty string) is truthy. [] (empty array) is truthy. 1 is truthy."

retrieval:
  recall: "List three falsy values in JavaScript."
  explain: "Explain what 'short-circuit evaluation' means with a logical AND example."
  mistakeId:
    code: "if (username = '') { console.log('Empty!'); }"
    answer: "= is assignment, not comparison. username is set to '' and the condition evaluates to '' (falsy) — so the message never prints. Use === instead: if (username === '')"
---

# Hook

Programs that cannot make decisions are not very useful.

A webpage that shows the same content to everyone, regardless of who they are or what they clicked, is just a document. The moment your code can ask "is this user logged in?" or "is this value above the threshold?" — and behave differently based on the answer — it becomes intelligent.

Conditionals are how JavaScript makes decisions.

> What is the most important decision that a webpage you use regularly makes about your experience?

# Lore Introduction

Master Aelindra draws a branching path on the workshop's chalkboard.

*"Every enchantment reaches a fork,"* she says. *"One path if the condition is met — another if it is not. An enchantment without branches is rigid, mechanical, identical for every soul who casts it. An enchantment with branches responds."*

She draws a third branch.

*"And sometimes, there are many forks. Many conditions. A cascade of decisions leading to a single outcome. That is the art of the conditional."*

# Core Learning

## Concept Introduction

An `if` statement runs a block of code only when a condition evaluates to `true`.

```js
if (condition) {
  // runs when condition is true
} else if (otherCondition) {
  // runs when condition is false AND otherCondition is true
} else {
  // runs when all conditions are false
}
```

| Part | When it runs |
|------|-------------|
| `if` | Condition is true |
| `else if` | Previous condition(s) false, this one true |
| `else` | All previous conditions false |

## Why It Matters

Decision-making is fundamental to all useful software. Access control, form validation, personalised content, error handling — every one of these requires conditional logic. Without `if/else`, JavaScript can only execute the same sequence every time.

## Worked Examples

**Example 1 — Basic access check**

```js
const age = 20;
if (age >= 18) {
  console.log("Access granted.");
} else {
  console.log("You must be 18 or older.");
}
// Access granted.
```

**Example 2 — Grade classification**

```js
function classify(score) {
  if (score >= 90) {
    return "Distinction";
  } else if (score >= 70) {
    return "Merit";
  } else if (score >= 50) {
    return "Pass";
  } else {
    return "Fail";
  }
}

console.log(classify(85));  // Merit
console.log(classify(42));  // Fail
```

**Example 3 — Truthy/falsy guard**

```js
const username = "";
if (username) {
  console.log("Hello, " + username);
} else {
  console.log("Please enter a username.");
}
// Please enter a username. — empty string is falsy
```

## Common Mistakes

- Using `=` (assignment) instead of `===` (comparison) in a condition
- Forgetting that `else` is optional — if you omit it, nothing happens when the condition is false
- Chaining too many `else if` blocks — consider a `switch` statement or a lookup object for many cases
- Assuming `0`, `""`, `null`, `undefined` are truthy — they are all falsy

## Mental Model

Think of conditionals as a flowchart.

Every diamond on a flowchart is an `if`. The "Yes" arrow is the `if` block. The "No" arrow is the `else`. Additional diamonds in the "No" path are `else if` blocks.

Before writing code, sketch the decision tree. If your flowchart is clear, your conditionals will be too.

## Mini Summary

- `if (condition)` runs code only when condition is true
- `else if` and `else` add fallback paths
- Conditions use comparison and logical operators
- Falsy values: `false`, `0`, `""`, `null`, `undefined`, `NaN`
- Never use `=` in a condition — always `===` or another comparison operator

# Guided Practice Quest

In this quest you will trace through a conditional, complete a condition with the right operator, and explain truthy/falsy behaviour.

Three steps that build the decision-making foundation for all the DOM work ahead.

# Solo Practice Quest

Write a function `trafficLight(colour)` that:
- Returns `"Stop"` if the colour is `"red"`
- Returns `"Get ready"` if the colour is `"amber"`
- Returns `"Go"` if the colour is `"green"`
- Returns `"Unknown signal"` for any other input

Test it with all four cases and log the results. Add a comment explaining what would happen if you forgot the final `else`.

# Integration

**Connecting to Psychology — Decision Fatigue**

Psychologists have found that the quality of decisions deteriorates after a long sequence of choices — a phenomenon called *decision fatigue*. Judges give harsher rulings later in the day; shoppers make worse choices after extended shopping.

Well-designed software reduces decision fatigue by making decisions *for* the user where possible — pre-selecting sensible defaults, hiding irrelevant options, making the most common path the easiest. Every `if/else` you write is a place where the software makes a decision instead of the user. Used thoughtfully, conditionals make software kinder. Used carelessly, they multiply the choices the user has to navigate.

# Lore Conclusion

The apprentice marks both branches of the chalkboard path with runes.

*"You can now write enchantments that respond differently to different people,"* Master Aelindra says. *"An enchantment that treats everyone the same is a notice board. An enchantment that responds to who you are — that is a companion."*

The branching paths on the chalkboard glow.

---
