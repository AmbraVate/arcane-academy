---
id: se-app-m2-15
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: control_flow
topicTitle: "Control Flow"
topicSortOrder: 3
lesson: if_statements
title: "If Statements"
sortOrder: 15
difficulty: 1
estimatedMinutes: 20
xpReward: 50
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-14]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly writes `if (condition) { }` syntax with a boolean condition"
    - "Explains that the body only executes when the condition is true"
    - "Describes what happens when the condition evaluates to false (body is skipped)"
    - "Identifies that the condition must be a boolean expression"
    - "Gives a concrete real-world analogy for a single-branch if statement"
  keywords: [if, condition, boolean, block, body, true, false, branch]
  modelAnswer: |
    An `if` statement allows a program to execute a block of code only when a specified condition is true. The syntax is `if (condition) { body }`, where the condition is any boolean expression — one that evaluates to either `true` or `false`.

    When the program reaches an `if` statement, it evaluates the condition. If the result is `true`, every statement inside the curly braces (the block body) is executed. If the result is `false`, the entire block is skipped and execution continues with the first statement after the closing brace.

    This is called a "single-branch" decision because there is only one branch: the "do this" path. There is no alternative path specified — the program simply moves on when the condition is false.

    A real-world analogy: a bouncer at a door checks your age. If you are old enough, you enter. If you are not old enough, nothing special happens — you just don't enter. The bouncer does not need to say "and if you're underage, go home" — not entering is the default.
guidedSteps:
  - id: se-app-m2-15-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Look at this code:
      ```java
      int score = 85;
      if (score >= 80) {
          System.out.println("Well done!");
      }
      System.out.println("Done.");
      ```
      What is printed when this code runs?
    inputConfig:
      options:
        - "Nothing is printed"
        - "Well done! Done."
        - "Done. Well done!"
        - "Well done!"
    markingRule:
      matchMode: EXACT
      accepted: ["Well done! Done."]
      rejectedFeedback: "The condition `score >= 80` evaluates to `true` because 85 is greater than or equal to 80. So the body of the `if` runs, printing 'Well done!'. Then execution continues after the closing brace and prints 'Done.' — that line is always printed, it is outside the `if` block."
    hint: "Is 85 >= 80? What happens to lines inside and outside the if block?"
    reflectionPrompt: "Lines inside the `if` block only run when the condition is true. Lines outside the block always run, regardless of the condition."

  - id: se-app-m2-15-step2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Write an `if` statement that prints "Freezing!" if a variable called `temperature` is less than 0. Assume `temperature` has already been declared as an int.
    inputConfig:
      language: java
      starterCode: |
        int temperature = -5;
        // Write your if statement below
    markingRule:
      matchMode: CONTAINS
      accepted: ["if", "temperature", "<", "0", "Freezing"]
      rejectedFeedback: |
        The correct form is:
        ```java
        if (temperature < 0) {
            System.out.println("Freezing!");
        }
        ```
        Make sure the condition is in parentheses and the body is inside curly braces.
    hint: "Use the < operator to check if temperature is less than 0."
    reflectionPrompt: "The condition `temperature < 0` is a boolean expression. It produces `true` when temperature is negative, and the body runs. It produces `false` when temperature is 0 or above, and the body is skipped."

  - id: se-app-m2-15-step3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In your own words, what happens to the code inside an `if` block when the condition evaluates to `false`?
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: ["skip", "skipped", "not run", "not executed", "ignored", "bypass"]
      rejectedFeedback: "When the condition is false, the entire block inside the curly braces is skipped. The program jumps to the first statement after the closing `}` brace and continues from there. The code inside the block is not executed at all."
    hint: "Think about what the program does with the lines inside { } when the condition is false."
    reflectionPrompt: "Skipping code when a condition is false is just as important as running it when the condition is true. The `if` statement gives the program the ability to do nothing in response to a situation — and sometimes doing nothing is exactly correct."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What must the condition inside `if (condition)` evaluate to?"
    options:
      - "A number (0 for false, non-zero for true)"
      - "A String ('true' or 'false')"
      - "A boolean value (true or false)"
      - "An int value"
    correctIndex: 2
    feedback: "In Java, the condition in an `if` statement must be a boolean expression — something that evaluates to exactly `true` or `false`. Unlike some languages, Java does not treat 0 as false or non-zero as true. The condition must be a proper boolean."

  - type: MULTIPLE_CHOICE
    question: "Given `int x = 5;`, what does `if (x > 10) { System.out.println(\"Big\"); }` print?"
    options:
      - "Big"
      - "x > 10"
      - "false"
      - "Nothing is printed"
    correctIndex: 3
    feedback: "The condition `x > 10` evaluates to `false` because 5 is not greater than 10. When the condition is false, the body of the `if` block is skipped entirely. Nothing is printed."

retrieval:
  recall: "Write the Java syntax for an if statement that executes a block when variable `age` is 18 or more."
  explain: "Explain why Java requires the condition inside an if statement to be a boolean expression, not just any value."
  mistakeId:
    code: |
      int score = 70;
      if (score = 100) {
          System.out.println("Perfect score!");
      }
    answer: "The condition uses `=` (assignment) instead of `==` (equality comparison). `score = 100` assigns 100 to score — it does not compare. This is a compile error in Java because an int assignment does not produce a boolean. The fix is `if (score == 100)`."
---

# Hook

Your program knows a number. It knows a temperature. It knows a score. But knowing is not enough — your program needs to *act differently* based on what it knows. A thermostat that reads the temperature but never turns the heat on is useless. A game that tracks your score but never congratulates you means nothing. The moment you write your first `if` statement, your code stops being a rigid script and starts being a program that responds. This lesson is that moment.

# Lore Introduction

"The most fundamental rune in the academy's library," Archmage Veylan says, holding up a glowing sigil shaped like the letter *if*, "is also the simplest. It asks one question: is this condition true?" He traces the rune slowly. "If yes — cast. If no — do nothing. No complex incantation. No elaborate ritual. Just a question and an answer." In Arcane Academy, apprentices learn early that power is not in complexity but in precision. A well-placed binding rune that asks the right question is worth a thousand unconditional spells.

# Core Learning

## Concept Introduction

An **if statement** executes a block of code only when a condition is `true`.

**Syntax:**
```java
if (condition) {
    // body — runs only when condition is true
}
```

The three parts:

| Part | Description |
|------|-------------|
| `if` keyword | Signals the start of a conditional |
| `(condition)` | A boolean expression in parentheses |
| `{ body }` | Statements to execute if condition is true |

**What "boolean expression" means:** any expression that produces `true` or `false`. Examples: `x > 5`, `name.equals("Alice")`, `isLoggedIn == true`, `count != 0`.

## Why It Matters

Without `if` statements, every line of your program always executes. You cannot respond to different situations. You cannot validate input. You cannot change behaviour based on data. The `if` statement is the smallest useful unit of decision-making in Java, and virtually every non-trivial program uses hundreds of them.

## Worked Examples

**Example 1 — Simple numeric check:**
```java
int age = 20;
if (age >= 18) {
    System.out.println("You are an adult.");
}
// Prints: You are an adult.
```

**Example 2 — Condition is false, body is skipped:**
```java
int temperature = 25;
if (temperature < 0) {
    System.out.println("Freezing outside!");
}
System.out.println("Check complete.");
// Prints: Check complete.
// (The "Freezing" line is skipped because 25 < 0 is false)
```

**Example 3 — Using a boolean variable directly:**
```java
boolean isRaining = true;
if (isRaining) {
    System.out.println("Take an umbrella.");
}
// Prints: Take an umbrella.
// isRaining is already a boolean — no comparison needed
```

## Common Mistakes

- **Using `=` instead of `==` in the condition:** `if (x = 5)` is an assignment, not a comparison. Java will give a compile error. Use `if (x == 5)`.
- **Forgetting the parentheses around the condition:** `if score > 80` is a syntax error. It must be `if (score > 80)`.
- **Forgetting curly braces and assuming indentation matters:** Java ignores indentation. Without `{ }`, only the very next line is treated as the body.
- **Writing `if (x == true)`:** If `x` is already a boolean, just write `if (x)`. The `== true` is redundant.
- **Expecting the body to run when the condition is false:** The body is only for the `true` case. There is no fallback unless you add `else`.

## Mental Model

Think of the `if` statement as a **locked gate**. The condition is the key. When the key works (condition is `true`), the gate opens and the code inside runs. When the key does not work (condition is `false`), the gate stays shut and the program walks past it without entering. The gate is transparent — you can see what's inside — but you only get in if your key works.

## Mini Summary

- `if (condition) { body }` executes the body only when the condition is `true`.
- The condition must be a boolean expression — something that evaluates to `true` or `false`.
- When the condition is `false`, the body is skipped entirely.
- Lines after the closing `}` always run, regardless of the condition.
- A boolean variable can be used directly as a condition: `if (isReady)`.
- This is a "single-branch" decision — there is only the "do this" path; no else is required.

# Guided Practice Quest

*"Inscribe your first binding rune," Archmage Veylan instructs, handing you a blank scroll. "Ask a simple question. Answer it with precision." He taps the scroll. "The rune cares only whether the condition is true. Nothing more."*

# Solo Practice Quest

**The Rune Trial**

Write a small Java program (in pseudocode or real Java) that:

1. Stores a player's `health` as an int (choose any value).
2. Uses an `if` statement to print "Critical health!" if `health` is below 20.
3. Uses a second `if` statement to print "Full health!" if `health` equals 100.
4. Always prints "Health check complete." at the end.

Then explain, in 2-3 sentences, what your program prints for your chosen health value and why.

# Integration

**Mathematics connection:** In mathematics, an **indicator function** (also called a characteristic function) returns 1 if a condition is satisfied and 0 if it is not. The `if` statement is the programming equivalent: it activates a block of logic when a predicate (a boolean-valued function) is true. Formal logic, which underpins mathematics and computer science, represents this as an implication: "IF P, THEN Q" — meaning Q only follows if P is true. The Java `if` statement is a direct implementation of this classical logical form.

**Philosophy connection:** The philosopher Gottfried Leibniz (1646–1716) imagined the world as operating on a principle of "sufficient reason" — nothing happens without a sufficient cause or reason. An `if` statement embodies this exactly: the body executes only if there is sufficient reason (the condition is true). Without the condition being satisfied, there is no reason for the code to run, and it does not. This seemingly simple idea — that actions require justification — is the foundation of both rational philosophy and conditional programming.

*Free question: Can the body of an `if` statement contain another `if` statement? What might that be useful for? Give an example in plain English.*

# Lore Conclusion

You inscribe the rune and speak the incantation. For a moment, nothing happens — the condition you chose is false. "Good," Archmage Veylan says quietly. "You see? It did nothing. That is correct behaviour." He nods with approval. "A rune that fires when it should not is more dangerous than one that never fires at all. You have learned restraint. Now learn to add the other branch — to decide not just when to act, but what to do when you choose not to." He gestures to the scroll for the next lesson.
