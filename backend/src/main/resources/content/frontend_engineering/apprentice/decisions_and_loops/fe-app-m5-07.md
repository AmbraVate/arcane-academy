---
id: fe-app-m5-07
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
lesson: loops
title: "Loops"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m5-06]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes a for loop that counts from 1 to 10 correctly"
    - "Explains the three parts of a for loop header (init, condition, increment)"
    - "Writes a while loop for a condition-driven repetition"
    - "Describes what an infinite loop is and how to avoid it"
    - "Uses a loop to process multiple items rather than repeating code"
  keywords: [loop, for, while, iteration, counter, condition, increment, infinite]
  modelAnswer: |
    A for loop repeats a block of code a known number of times: for (let i = 0; i < n; i++).
    The three parts are initialisation (let i = 0), condition (i < n), and increment (i++).
    A while loop repeats while a condition remains true — useful when the count is unknown.
    An infinite loop occurs when the condition never becomes false, freezing the browser.
guidedSteps:
  - id: js-loop-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What are the three parts of a `for` loop, in order?
    inputConfig:
      options:
        - "condition, increment, initialisation"
        - "initialisation, condition, increment"
        - "increment, condition, initialisation"
        - "initialisation, body, condition"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["initialisation, condition, increment"]
      rejectedFeedback: "The for loop header has three parts separated by semicolons: for (initialisation; condition; increment). Initialisation runs once at the start; condition is checked before each iteration; increment runs after each iteration."
    hint: "for (___; ___; ___) — what goes in each slot?"
    reflectionPrompt: "Correct. Remembering init → condition → increment helps you avoid the common mistake of putting them in the wrong order."

  - id: js-loop-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the for loop so it prints 1 through 5:

      ```js
      for (let i = 1; i ___ 6; i++) {
        console.log(i);
      }
      ```
    inputConfig:
      placeholder: "comparison operator"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["<", "<= 5"]
      rejectedFeedback: "Use i < 6 (or i <= 5) to stop after printing 5. If you use i <= 6, it would also print 6."
    hint: "You want the loop to stop after i reaches 5. Should the condition be < 6 or <= 5?"
    reflectionPrompt: "Either i < 6 or i <= 5 works here. < 6 is slightly more common in practice. The key insight: the loop runs while the condition is true."

  - id: js-loop-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      What is an infinite loop, and what causes one?
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [infinite, forever, condition, never, false, freeze, crash, stop]
      rejectedFeedback: "An infinite loop occurs when the loop's condition never becomes false, so it runs forever — eventually freezing or crashing the browser tab."
    hint: "What happens to a loop that never reaches a state where its condition evaluates to false?"
    reflectionPrompt: "Exactly. Always check: will your loop's condition eventually be false? Common causes: forgetting the increment, or incrementing in the wrong direction."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "When would you use a while loop instead of a for loop?"
    options:
      - "When you want to loop exactly 10 times"
      - "When you need to iterate over an array"
      - "When the number of iterations is not known in advance"
      - "while loops are always faster than for loops"
    correctIndex: 2
    feedback: "Use while when the loop should continue until a condition changes, and the number of repetitions is not known beforehand. Use for when you know the count or are iterating over a collection."
  - type: MULTIPLE_CHOICE
    question: "What does `i++` do in a for loop?"
    options:
      - "Decreases i by 1"
      - "Checks if i is positive"
      - "Increases i by 1"
      - "Resets i to 0"
    correctIndex: 2
    feedback: "i++ is shorthand for i = i + 1 — it increments the counter by 1 after each loop iteration."

retrieval:
  recall: "Write a for loop that logs every even number from 2 to 10."
  explain: "Explain the difference between a for loop and a while loop — when would you choose each?"
  mistakeId:
    code: "for (let i = 0; i > 0; i++) { console.log(i); }"
    answer: "The condition i > 0 is false from the very start (i = 0 is not > 0), so the loop body never runs. The condition should be i < someLimit."
---

# Hook

A program that has to do the same thing five times should not have five identical lines of code.

Not ten lines. Not a hundred. Whether you need to process 5 items or 50,000, the code should be the same length — and it is, when you use loops.

Loops are one of the most powerful ideas in programming: the ability to repeat a block of code precisely as many times as needed, automatically.

> If you had to greet every visitor to a website by name, would you write a separate greeting for each one? What would be the smarter approach?

# Lore Introduction

Master Aelindra rolls a parchment out across the entire length of the workbench.

*"A copy-scribe is not a craftsperson,"* she says, gesturing at the long scroll. *"Writing the same rune a hundred times is not skill — it is endurance. A true enchanter writes the pattern once and tells the craft how many times to repeat it."*

She inscribes a looping spiral symbol.

*"This is the iteration rune. It says: perform this action — again, and again, and again — until the condition is met. That is the loop."*

# Core Learning

## Concept Introduction

A **loop** repeats a block of code multiple times.

### The `for` Loop — for a known number of iterations

```js
for (initialisation; condition; increment) {
  // body runs on each iteration
}
```

| Part | Purpose | Example |
|------|---------|---------|
| Initialisation | Set the counter | `let i = 0` |
| Condition | Check before each iteration | `i < 5` |
| Increment | Update the counter after each iteration | `i++` |

### The `while` Loop — for an unknown number of iterations

```js
while (condition) {
  // runs as long as condition is true
}
```

## Why It Matters

Loops allow you to process any number of items with the same code. Displaying a list of products, validating multiple form fields, repeating an animation — all of these require loops. Without loops, your code would be impossibly long and brittle.

## Worked Examples

**Example 1 — Print 1 to 5**

```js
for (let i = 1; i <= 5; i++) {
  console.log(i);
}
// 1, 2, 3, 4, 5
```

**Example 2 — Sum all numbers 1 to 10**

```js
let total = 0;
for (let i = 1; i <= 10; i++) {
  total = total + i;
}
console.log(total); // 55
```

**Example 3 — While loop: count down**

```js
let countdown = 5;
while (countdown > 0) {
  console.log(countdown);
  countdown--;
}
console.log("Liftoff!");
// 5, 4, 3, 2, 1, Liftoff!
```

**Example 4 — Looping over items (preview)**

```js
const names = ["Elara", "Veylan", "Aelindra"];
for (let i = 0; i < names.length; i++) {
  console.log("Hello, " + names[i]);
}
```

This pattern — looping through a collection using its length — is the most common loop in web development.

## Common Mistakes

- Off-by-one errors: using `<` vs `<=` incorrectly, printing one too many or one too few items
- Forgetting the increment — causing an infinite loop that freezes the browser
- Modifying the counter variable inside the loop body — unpredictable iteration count
- Using a `for` loop when a `while` loop is more readable, or vice versa

## Mental Model

A `for` loop is like a production line with a counter.

*"Run this process 10 times. Keep track. Stop when done."*

A `while` loop is like a security guard's shift.

*"Keep checking until you're told to stop — you don't know when that will be."*

Both accomplish repetition. Choose based on whether you know the count in advance.

## Mini Summary

- `for` loops are best when the number of iterations is known
- `while` loops are best when iterating until a condition changes
- The three parts of a `for` loop: initialisation, condition, increment
- Off-by-one errors are the most common loop bug — check your boundary condition
- An infinite loop occurs when the condition never becomes `false` — always ensure progress

# Guided Practice Quest

In this quest you will identify the three parts of a `for` loop, complete a loop condition, and explain what causes an infinite loop.

These steps prepare you to iterate over arrays in the next lesson.

# Solo Practice Quest

Write a for loop that:
1. Counts from 1 to 20
2. Prints `"Fizz"` if the number is divisible by 3
3. Prints `"Buzz"` if divisible by 5
4. Prints `"FizzBuzz"` if divisible by both
5. Otherwise prints the number

This is the classic FizzBuzz problem. Use `%` (modulo) to check divisibility.

# Integration

**Connecting to Mathematics — Summation and Sequences**

In mathematics, summation notation (Σ) expresses the sum of a sequence: Σᵢ₌₁ⁿ i means "add all integers from 1 to n". A for loop is a direct implementation of this concept.

```js
// Σᵢ₌₁¹⁰⁰ i
let sum = 0;
for (let i = 1; i <= 100; i++) sum += i;
console.log(sum); // 5050
```

Carl Friedrich Gauss reportedly computed this sum at age ten by noticing the pattern: 1 + 100 = 101, 2 + 99 = 101, and so on 50 times. The formula n(n+1)/2 gives the same answer instantly. Your loop is more general — it works for any sequence — but the mathematical shortcut is faster. This is the eternal trade-off in computing: generality vs efficiency.

# Lore Conclusion

The apprentice traces the spiral rune and watches it repeat ten times in miniature above the workbench.

*"Once,"* says Master Aelindra, *"and a thousand. The number is yours to choose. The craft is yours to trust."*

The iteration rune settles into the page, ready to be called upon.

---
