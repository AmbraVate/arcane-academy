---
id: se-app-m1-09
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m1
moduleTitle: "Module 1: Foundations of Computation"
moduleGlyph: "🧠"
moduleSortOrder: 1
topicSlug: logic_foundations
topicTitle: "Logic Foundations"
topicSortOrder: 2
lesson: decision_making
title: "Decision Making"
sortOrder: 9
difficulty: 1
estimatedMinutes: 20
xpReward: 40
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [comparisons]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes a correct if/else structure for a given scenario"
    - "The condition evaluates to a boolean"
    - "Both branches handle their respective cases"
    - "Explains what happens when the condition is true vs false"
    - "Shows awareness of the 'else if' chain for multiple conditions"
  keywords: [if, else, condition, branch, decision, execute, evaluate, flow]
  modelAnswer: |
    int temperature = 25;
    if (temperature > 30) {
        System.out.println("It's hot!");
    } else if (temperature >= 20) {
        System.out.println("It's warm.");
    } else {
        System.out.println("It's cool.");
    }
    // Output: It's warm.
guidedSteps:
  - id: dec-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Given `int score = 55;`, what does this code print?

      ```java
      if (score >= 60) {
          System.out.println("Pass");
      } else {
          System.out.println("Fail");
      }
      ```
    inputConfig:
      options:
        - "Pass"
        - "Fail"
        - "Nothing"
        - "55"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Fail"]
      rejectedFeedback: "`score >= 60` evaluates to `false` (55 is not ≥ 60), so the `else` branch runs and prints 'Fail'."
    hint: "Is 55 >= 60? No. Which branch runs when the condition is false?"
    reflectionPrompt: "Correct. The `else` branch is the fallback when the condition is `false`. Only one of the two branches ever runs — never both."
  - id: dec-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the code so it prints "Discount applied" when `totalPrice` is above £100:

      ```java
      double totalPrice = 120.0;
      ___ (totalPrice > 100) {
          System.out.println("Discount applied");
      }
      ```
    inputConfig:
      placeholder: "keyword"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["if"]
      rejectedFeedback: "The `if` keyword begins a conditional statement. The condition `(totalPrice > 100)` will evaluate to `true` (120 > 100), so the block runs."
    hint: "What keyword starts a conditional statement in Java?"
    reflectionPrompt: "An `if` without an `else` is fine when you only need to act on the true case. The program continues normally after the block whether the condition was true or false."
  - id: dec-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In your own words: when should you use `if/else if/else` instead of multiple separate `if` statements?
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [exclusive, only, one, first, branch, either, match, chain, order]
      rejectedFeedback: "Use `if/else if/else` when conditions are **mutually exclusive** — only one should ever be true at a time, and you want exactly one branch to run. Separate `if` statements would evaluate all conditions independently."
    hint: "What if two conditions could both be true with separate `if` statements? What should `else if` prevent?"
    reflectionPrompt: "Exactly. `if/else if/else` chains ensure **exactly one** branch runs — the first true one. Separate `if` statements would all evaluate independently, potentially running multiple branches."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "How many branches run in an `if/else` when the condition is `true`?"
    options:
      - "Both branches"
      - "Neither branch"
      - "Only the `if` branch"
      - "Only the `else` branch"
    correctIndex: 2
    feedback: "When the condition is `true`, only the `if` branch runs. The `else` branch is skipped. Only one branch ever executes — that's the point of `if/else`."
  - type: MULTIPLE_CHOICE
    question: "What happens after an `if/else if/else` chain when one branch runs?"
    options:
      - "All remaining conditions are still evaluated"
      - "The program stops"
      - "Execution continues with the code after the entire chain"
      - "The first condition is re-evaluated"
    correctIndex: 2
    feedback: "After any one branch executes, the entire chain is exited. Execution continues with whatever code follows the closing `}` of the chain."

retrieval:
  recall: "What is the difference between `if/else if/else` and a sequence of separate `if` statements?"
  explain: "Explain how an if/else statement works using a real-world decision (not programming jargon)."
  mistakeId:
    code: |
      if (score >= 90) { grade = "A"; }
      if (score >= 80) { grade = "B"; }
      if (score >= 70) { grade = "C"; }
    answer: "These are separate `if` statements, not an `else if` chain. A score of 95 would set grade to 'A', then 'B', then 'C' — ending with 'C'. Use `else if` to ensure only the first matching branch runs."
---

# Hook

A program without decisions is just a calculator.

The moment your code can look at a value and say "if this, then that, otherwise something else" — it becomes intelligent. Not truly intelligent, but *responsive*. Responsive to data, to users, to the world it runs in.

That responsiveness starts with `if`.

> Think of a decision a computer program makes on your behalf every day. What conditions does it check?

# Lore Introduction

The Academy's sorting chamber has a single crystal that evaluates each apprentice's resonance signature. If strong, left corridor. If moderate, right corridor. If weak, the central hall for further assessment. Three paths. One decision rune. The entire sorting process.

*"The if-rune,"* Archmage Veylan says, *"is the first rune of will. Without it, all programs do the same thing, always. With it — they respond."*

# Core Learning

## Concept Introduction

An **if statement** evaluates a boolean condition and runs different code depending on the result.

```java
if (condition) {
    // runs when condition is true
} else if (anotherCondition) {
    // runs when first is false, this is true
} else {
    // runs when all above are false
}
```

Rules:
- `if` is required; `else` and `else if` are optional
- Conditions evaluate top-to-bottom; the **first true** branch runs
- Only **one** branch ever runs in a given execution

## Why It Matters

Without conditionals, programs cannot respond to input, handle errors, enforce rules, or implement any form of decision logic. Every game, every form, every API endpoint uses conditional branching.

## Worked Examples

```java
int age = 17;
if (age >= 18) {
    System.out.println("Adult");
} else {
    System.out.println("Minor");
}
// Output: Minor

// Multi-branch
int score = 85;
if (score >= 90) {
    System.out.println("A");
} else if (score >= 80) {
    System.out.println("B");
} else if (score >= 70) {
    System.out.println("C");
} else {
    System.out.println("F");
}
// Output: B
```

## Common Mistakes

- Missing braces `{}` for multi-line bodies — they are technically optional for single statements but dangerous to omit.
- Using `=` instead of `==` in the condition.
- Writing multiple separate `if` statements when `else if` was intended (see retrieval section).
- Forgetting the `else` — if all conditions are false and there's no `else`, nothing happens silently.

## Mental Model

An if/else chain is a **railway junction**. The train (execution) arrives and the signal (condition) determines which track it takes. Once on a track, it follows it to the end. Separate `if` statements are separate junctions — the train passes through all of them.

## Mini Summary

- ✔ `if (condition) { ... }` runs the block when condition is `true`
- ✔ `else` provides a fallback when the condition is `false`
- ✔ `else if` chains multiple mutually exclusive conditions
- ✔ Only the first true branch in an `if/else if/else` chain runs
- ✔ Use `else if` (not separate `if`s) when conditions are mutually exclusive

# Guided Practice Quest

**The Sorting Crystal**

The Academy's sorting crystal needs programming. It must evaluate an apprentice's resonance score and direct them to the appropriate hall.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Write an `if/else if/else` chain for a **traffic light system** with three states:
- If the light is `"green"` → print "Go"
- If the light is `"amber"` → print "Prepare to stop"  
- If the light is `"red"` → print "Stop"
- Otherwise → print "Unknown signal"

Then answer: Why is it important that only ONE of these messages prints per light state?

# Integration

**Connecting to Psychology — Dual Process Theory**

Psychologist Daniel Kahneman describes two modes of human thinking: System 1 (fast, automatic, intuitive) and System 2 (slow, deliberate, logical). An `if/else` statement is pure System 2 — it evaluates a condition deliberately before acting.

Interestingly, humans often use heuristics (if-like shortcuts) unconsciously. "If this person smiles, trust them" is a fast, automatic if-rule encoded by experience. Computer programs, however, can only use explicit if-rules — they have no System 1. Every decision must be written out.

What are the advantages and disadvantages of a decision system that is entirely explicit and deliberate, compared to one that includes fast intuitive rules?

# Lore Conclusion

The sorting crystal glows. Left, right, or centre — based on a single condition.

*"You have written the first decision,"* Archmage Veylan says. *"Your code can now respond to the world. Next: learn to combine conditions — so your decisions can be more nuanced than a single true or false."*

The junction rune is inscribed. Three paths wait.
---
