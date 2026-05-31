---
id: se-app-m2-05
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: variables_and_state
topicTitle: "Variables & State"
topicSortOrder: 1
lesson: assignment
title: "Assignment"
sortOrder: 5
difficulty: 1
estimatedMinutes: 20
xpReward: 40
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-04]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly writes assignment statements using the = operator"
    - "Distinguishes between = (assignment) and == (equality check)"
    - "Evaluates the right-hand side of an assignment before storing the result"
    - "Uses a variable on both sides of = to update its own value"
    - "Code compiles and produces correct output"
  keywords: [assignment, operator, equals, right-hand side, evaluate, store, update, expression]
  modelAnswer: |
    ```java
    public class AssignmentDemo {
        public static void main(String[] args) {
            // Basic assignment
            int gold = 100;
            String hero = "Kira";

            // Assignment using an expression on the right-hand side
            int doubleGold = gold * 2;
            System.out.println("Double gold: " + doubleGold); // 200

            // Updating a variable using itself
            gold = gold + 50;
            System.out.println("After reward: " + gold); // 150

            // Assignment from another variable
            int startingGold = gold;
            System.out.println("Starting gold saved: " + startingGold); // 150

            // Sequential assignments tell a story
            int score = 0;
            score = 10;       // first answer correct
            score = score + 5; // bonus for speed
            System.out.println("Final score: " + score); // 15
        }
    }
    ```
guidedSteps:
  - id: se-app-m2-05-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In Java, what does the `=` operator do?

      ```java
      int health = 85;
      ```
    inputConfig:
      options:
        - "It checks whether health equals 85"
        - "It stores the value 85 into the variable health"
        - "It declares that health and 85 are the same type"
        - "It prints the value 85"
    markingRule:
      matchMode: EXACT
      accepted: ["It stores the value 85 into the variable health"]
      rejectedFeedback: "In Java, `=` is the *assignment* operator. It evaluates the right-hand side and stores the result in the variable on the left. To *check* equality, Java uses `==` (two equals signs)."
    hint: "In Java, `=` and `==` are different. One assigns; the other compares."
    reflectionPrompt: "`=` means 'take the value on the right and put it in the box on the left.' It is a *command* to store, not a *question* about equality."

  - id: se-app-m2-05-step2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      After running this code, what value does `total` hold?

      ```java
      int base = 10;
      int bonus = 5;
      int total = base + bonus;
      ```
    inputConfig:
      options:
        - "base + bonus"
        - "10"
        - "5"
        - "15"
    markingRule:
      matchMode: EXACT
      accepted: ["15"]
      rejectedFeedback: "Java evaluates the right-hand side *first*. `base + bonus` becomes `10 + 5` which is `15`. Then `15` is stored in `total`. The variable `total` holds `15`, not the expression `base + bonus`."
    hint: "Java always evaluates the right-hand side of = before storing anything."
    reflectionPrompt: "The right-hand side is always evaluated to a single value before assignment. `total = base + bonus` does not store the expression — it stores the result: `15`."

  - id: se-app-m2-05-step3
    sortOrder: 3
    inputType: CODE
    instruction: |
      Write a single line of code that updates the variable `score` by adding 10 to its current value. (Assume `score` has already been declared as `int score = 20;`)
    inputConfig:
      placeholder: "score = ..."
    markingRule:
      matchMode: REGEX
      accepted: ["score\\s*=\\s*score\\s*\\+\\s*10\\s*;"]
      rejectedFeedback: "The correct answer is `score = score + 10;`. The right-hand side `score + 10` is evaluated first (20 + 10 = 30), then the result 30 is stored back into `score`."
    hint: "Use `score` on both the left and right sides of `=`. Add 10 to the current value of `score`."
    reflectionPrompt: "`score = score + 10;` is one of the most common patterns in programming: read a value, modify it, write it back. You will use this pattern constantly."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the difference between `=` and `==` in Java?"
    options:
      - "They are identical — both check equality"
      - "`=` assigns a value; `==` checks whether two values are equal"
      - "`==` assigns a value; `=` checks equality"
      - "`=` is for numbers; `==` is for Strings"
    correctIndex: 1
    feedback: "`=` is assignment: store a value in a variable. `==` is comparison: check whether two values are equal. Confusing these two is one of the most common beginner bugs."

  - type: MULTIPLE_CHOICE
    question: "After `int x = 5; x = x * 2;` what is the value of x?"
    options:
      - "5"
      - "2"
      - "10"
      - "x * 2"
    correctIndex: 2
    feedback: "`x * 2` evaluates to `5 * 2 = 10`. Then `10` is stored in `x`. The original value `5` is replaced. After the second line, `x` is `10`."

retrieval:
  recall: "In Java, what does the = operator do, and how is it different from == ?"
  explain: "Explain, step by step, what happens when Java executes the line: `int result = (base + bonus) * 2;`"
  mistakeId:
    code: |
      int lives = 3;
      int lives = lives - 1;
    answer: "The second line tries to *declare* `lives` again (`int lives = ...`). You cannot declare a variable twice in the same scope. To update it, simply write `lives = lives - 1;` without the `int` type keyword."
---

# Hook

In mathematics, `x = 5` is a declaration of truth — x *is* 5. But in Java, the same-looking symbol does something fundamentally different: it is a command. "Take whatever is on the right, evaluate it, and put the result in the box on the left." This simple operation — assignment — is the engine of all computation. Every time a program changes its state, it does so through assignment. Understanding what `=` really means is the difference between a programmer who reasons clearly and one who is perpetually confused.

# Lore Introduction

"The binding rune," Archmage Veylan says, drawing a single straight line in glowing ink, "is the most powerful mark in all of inscription." He traces it on a vessel, and it immediately fills with the essence he directed toward it. "It does not ask questions — it commands. It does not check — it acts. Whatever you place to the right of this rune will be evaluated and poured into the vessel on the left." In the Academy, all spells that change state — healing, damaging, gaining resources — are governed by the binding rune. Without it, rune vessels would remain forever empty.

# Core Learning

## Concept Introduction

The **assignment operator** in Java is `=`. It evaluates the expression on the right-hand side and stores the result in the variable on the left-hand side.

```
variable = expression;
```

**Critical distinction:**

| Symbol | Meaning | Example |
|--------|---------|---------|
| `=` | Assignment — store a value | `gold = 100;` |
| `==` | Equality check — compare two values | `gold == 100` |

**How Java processes an assignment:**
1. Evaluate the right-hand side completely
2. Store the resulting value in the left-hand variable

```java
int a = 5;
int b = 3;
int result = a + b;   // step 1: evaluate 5 + 3 = 8
                       // step 2: store 8 in result
```

The variable `result` now holds `8`.

## Why It Matters

Assignment is how programs change state. Every time a player's score increases, a timer ticks down, a character takes damage, or a setting is toggled — an assignment is happening. Understanding that `=` means "store the result of evaluating the right side" (rather than "these two things are equal") is fundamental to reasoning about what a program is doing.

## Worked Examples

**Example 1 — Simple assignment:**
```java
int gold = 100;        // gold is now 100
String heroName = "Aria"; // heroName is now "Aria"
boolean isReady = true;   // isReady is now true
```

**Example 2 — Assignment with expression:**
```java
int base = 50;
int bonus = 25;
int total = base + bonus; // right side evaluated: 50 + 25 = 75
                           // 75 stored in total
System.out.println(total); // prints 75
```

**Example 3 — A variable updated using itself:**
```java
int score = 0;
score = score + 10;  // right side: 0 + 10 = 10; stored in score
score = score + 5;   // right side: 10 + 5 = 15; stored in score
System.out.println(score); // prints 15
```
Java reads the *current* value of `score` from the right side, adds to it, then stores the new value back. This is not circular — it happens in strict left-to-right order within the expression.

## Common Mistakes

- **Using `=` when you mean `==`:** Writing `if (x = 5)` instead of `if (x == 5)` causes an error or unexpected behavior.
- **Re-declaring a variable to update it:** Writing `int score = score + 1;` when `score` already exists. Once declared, just use `score = score + 1;`.
- **Thinking `=` checks equality:** `gold = 100` does not ask "is gold 100?" — it *sets* gold to 100.
- **Forgetting the right-hand side is evaluated first:** `total = a + b` stores a number, not the expression.
- **Assigning to the wrong side:** `100 = gold;` will not compile. The variable must be on the left.

## Mental Model

Think of assignment like a **label printer**. You give it an instruction: "Print the label for box number 42." The printer evaluates your instruction, produces output (the label), and puts it on the box. The box on the left receives whatever the printer (right-hand side) produces. The printer does its work first, then the label goes on the box. You cannot label the printer with the box — direction matters.

## Mini Summary

- `=` is the assignment operator: evaluate right side, store result in left-side variable.
- `==` is the equality comparison operator — completely different from `=`.
- The right-hand side is always fully evaluated before storage occurs.
- A variable can appear on both sides: `x = x + 1` adds 1 to x's current value.
- Re-declaring a variable (adding the type) when updating it causes a compile error.
- Assignment is how programs change and update their state.

# Guided Practice Quest

*Archmage Veylan traces the binding rune slowly. "It flows from right to left," he says, "always. Evaluate, then bind. Evaluate, then bind." The binding rune glows as you complete each exercise, filling each vessel with precision.*

# Solo Practice Quest

**The Score Tracker**

Write a small Java program that simulates a player answering three quiz questions. Use the following logic:

1. Start with `int score = 0;`
2. The player gets the first question right: add 10 points.
3. The player gets the second question right and earns a bonus: add 15 points.
4. The player gets the third question wrong: subtract 5 points.
5. Print the score after each step, with a label (e.g., `"After Q1: 10"`).
6. Print the final score with a message: `"Final score: X"`

Use only assignment statements (`score = score + ...` or `score = score - ...`) — no compound operators yet.

# Integration

**Mathematics connection:** In algebra, `=` always denotes *equality* — a symmetrical relationship. `x = 5` and `5 = x` mean the same thing. In Java, `=` is *directional*: `gold = 100` is valid (store 100 in gold), but `100 = gold` is illegal. This asymmetry reflects the difference between a *mathematical truth* and a *computational command*. Early programming language designers debated whether to use a different symbol for assignment (like `:=`, still used in Pascal and Swift) precisely to avoid this confusion with mathematical notation.

**Philosophy connection:** The philosopher John Austin distinguished between *constative* utterances (statements that describe a truth, like "x equals 5") and *performative* utterances (statements that *do* something, like "I promise"). Assignment in programming is performative — `gold = 100` does not *describe* a state of affairs, it *creates* one. This distinction matters: when you read code, some lines are descriptions, but `=` is always an action. Recognising commands versus descriptions in code is a key step in learning to reason about programs accurately.

*Free question: If you had two variables `a` and `b`, and you wanted to swap their values (put `b`'s value in `a` and `a`'s value in `b`), why would you need a third temporary variable to do it correctly?*

# Lore Conclusion

Archmage Veylan watches the binding rune settle into each vessel in turn. "You understand the flow now," he says. "Right side is evaluated; result travels left; vessel receives its new charge." A vessel that once held `0` now holds `150` — the sum of a day's earned gold. The binding rune has done its work. In the next lesson, you will learn the Academy's most efficient spells for changing a vessel's value: not just `gold = gold + 50`, but the shorthand incantations that experienced mages prefer.
