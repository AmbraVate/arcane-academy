---
id: se-app-m1-06
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m1
moduleTitle: "Module 1: Foundations of Computation"
moduleGlyph: "🧠"
moduleSortOrder: 1
topicSlug: logic_foundations
topicTitle: "Logic Foundations"
topicSortOrder: 2
lesson: what_is_logic
title: "What is Logic?"
sortOrder: 6
difficulty: 1
estimatedMinutes: 18
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [pattern_recognition]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines logic in your own words"
    - "Explains why computers need logical rules"
    - "Gives an example of a logical statement that can be true or false"
    - "Distinguishes logical reasoning from guessing or intuition"
    - "Connects logic to at least one programming concept (if/else, boolean)"
  keywords: [logic, true, false, statement, rule, reasoning, boolean, condition]
  modelAnswer: |
    Logic is a system of reasoning where statements are evaluated as true or false.
    Computers use logic because they cannot guess — every decision must be based on
    a definite true/false evaluation.

    Example: "The user's age is 18 or above" — this is either true or false.
    In code: `if (age >= 18)` evaluates this logical statement and branches accordingly.
guidedSteps:
  - id: logic-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of these is a **logical statement** (can be evaluated as true or false)?
    inputConfig:
      options:
        - "Run faster!"
        - "The sky is blue"
        - "What time is it?"
        - "Please close the door"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The sky is blue"]
      rejectedFeedback: "A logical statement has a truth value — it is either true or false. 'The sky is blue' is either true (daytime, clear) or false (night, storm). Commands and questions are not logical statements."
    hint: "A logical statement can be answered with 'true' or 'false'. Which option is a factual claim?"
    reflectionPrompt: "Correct. 'The sky is blue' is a proposition — it has a definite truth value in any given context. In programming, conditions like `score > 90` are logical statements evaluated at runtime."
  - id: logic-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Write two logical statements about a bank account that a computer might need to evaluate.
      Each should be something that is clearly either true or false.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [balance, amount, limit, overdrawn, enough, greater, less, equal, funds]
      rejectedFeedback: "Examples: 'The balance is above £0' / 'The withdrawal amount is less than the balance' / 'The account is active'. Each is a clear true/false proposition."
    hint: "Think about what a bank needs to check: is there enough money? Is the account valid? Is the amount within limits?"
    reflectionPrompt: "Each of those is a condition your code will evaluate. When you write `if (balance >= amount)`, you're embedding a logical statement into your program."
  - id: logic-step-3
    sortOrder: 3
    inputType: FILL_BLANK
    instruction: |
      In Java, the type used to store a true/false value is called a ___.
    inputConfig:
      placeholder: "type name"
    markingRule:
      matchMode: NORMALIZED
      accepted: [boolean, Boolean]
      rejectedFeedback: "The `boolean` type stores exactly two values: `true` or `false`. It is named after George Boole, the mathematician who formalised logical algebra."
    hint: "It's named after a 19th-century mathematician. It stores exactly two possible values."
    reflectionPrompt: "The `boolean` type is logic made concrete. Every condition you evaluate — every `if`, every `while`, every `&&` — ultimately reduces to a single `boolean`: `true` or `false`."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What makes a statement 'logical' in the computing sense?"
    options:
      - "It is written in code"
      - "It can be evaluated as either true or false"
      - "It contains numbers"
      - "It is a command to the computer"
    correctIndex: 1
    feedback: "A logical statement has a truth value — it is either true or false. In Java, such statements are boolean expressions: `age >= 18`, `name.equals('Alice')`, `count > 0`."
  - type: MULTIPLE_CHOICE
    question: "Why do computers need formal logic rather than common sense?"
    options:
      - "Computers are faster than humans"
      - "Computers cannot guess — every decision must reduce to an unambiguous true or false"
      - "Formal logic makes code shorter"
      - "Common sense is too complicated"
    correctIndex: 1
    feedback: "Computers follow rules precisely but cannot interpret ambiguity. 'Is this reasonable?' is not a question a CPU can answer. 'Is this greater than zero?' is. That precision is why formal logic underpins all computing."

retrieval:
  recall: "What is a logical statement, and what type does Java use to store its result?"
  explain: "Explain to a friend why computers need logic, using a real-world decision as your example."
  mistakeId:
    code: "boolean result = 'maybe';"
    answer: "`boolean` can only hold `true` or `false` — never 'maybe', 'yes', or any other value. `'maybe'` is a String. This would be a compile error."
---

# Hook

Everything a computer decides, it decides with logic.

Not intuition. Not common sense. Not experience. Pure, formal, true-or-false logic.

When you write `if (password.length() >= 8)`, you are stating a logical proposition that the computer evaluates and acts upon. Understanding logic isn't optional for programmers — it *is* programming.

> Think of a decision you made today. Could you express it as a precise true/false statement?

# Lore Introduction

At the core of every enchanted artifact in the Academy lies a **binding rune** — a truth crystal that glows red for false and white for true. Every spell branches on these runic truths. The crystal cannot glow 'maybe'. It cannot interpret context. It only knows: is this condition met, or is it not?

*"Logic,"* says Archmage Veylan, *"is the language the machine speaks. Learn it, and the machine will obey precisely."*

# Core Learning

## Concept Introduction

**Logic** is a formal system of reasoning where statements are evaluated as either **true** or **false** — nothing in between.

In computing, logic is applied through:
- **Boolean expressions** — conditions that evaluate to `true` or `false`
- **Conditional statements** — actions taken based on a truth value
- **Logical operators** — combining conditions with AND, OR, NOT

The data type `boolean` (named after George Boole, 1815–1864) holds exactly two values: `true` or `false`.

## Why It Matters

Every decision in a program — every branch, every loop condition, every filter — reduces to a boolean. Understanding logic means understanding how your program *thinks*.

## Worked Examples

**Logical statements:**
- `age >= 18` → either true or false
- `username.equals("admin")` → either true or false
- `balance > 0 && accountActive` → true only if both are true

**In Java:**
```java
boolean isAdult = age >= 18;        // true or false
boolean loginValid = username.equals("admin") && password.equals("secret");
if (isAdult) {
    System.out.println("Access granted.");
}
```

## Common Mistakes

- **Using `=` instead of `==` for comparison.** `x = 5` assigns; `x == 5` evaluates.
- **Treating boolean as an integer.** In Java, `boolean` is NOT 1 or 0. It is `true` or `false`.
- **Confusing logical truth with moral truth.** `isAdult = true` means the condition is satisfied, not that the person is admirable.

## Mental Model

Logic is a **light switch**. Up: `true`. Down: `false`. Every boolean is exactly one or the other. There is no dimmer, no flicker, no 'mostly'. The machine lives in a world of crisp certainties — you must learn to describe reality in those terms.

## Mini Summary

- ✔ Logic evaluates statements as true or false — no in-between
- ✔ `boolean` is the Java type for true/false values
- ✔ Every conditional, loop, and filter in code uses boolean logic
- ✔ Boolean expressions: comparisons and logical operators
- ✔ Computers cannot guess — logic must be explicit and unambiguous

# Guided Practice Quest

**The Truth Crystal**

The Academy's truth crystals need calibrating. Your quest: evaluate several logical statements and determine their truth values in context.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

For a simple **library system**, write five logical statements that the system would need to evaluate. For each:
1. State the logical statement in plain English
2. Write it as a Java boolean expression (you can invent variable names)
3. Say when it would be `true` and when `false`

Example: "The book is available" → `book.isAvailable()` → true when book is on the shelf, false when borrowed.

# Integration

**Connecting to Mathematics — Boolean Algebra**

George Boole (1815–1864) created Boolean algebra to formalise logical reasoning using symbols. His algebra treats logical statements like numbers: `AND` is like multiplication, `OR` is like addition, `NOT` flips the value. This mathematical system later became the theoretical foundation of digital circuits and computers.

Every AND gate and OR gate in a CPU is a physical implementation of Boole's algebra, designed over a century ago. The `&&` and `||` operators in Java are direct descendants.

What does this suggest about the relationship between mathematics and the technology you use every day?

# Lore Conclusion

The apprentice holds a truth crystal. It is either bright or dark — nothing else.

*"Good,"* says Archmage Veylan. *"You understand the foundation. Logic is not opinion. It is not feeling. It is the one language the orb speaks fluently. Your next task: learn to think in true and false."*

The next lesson is already waiting. It begins with a question: `true or false?`
---
