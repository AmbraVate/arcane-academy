---
id: se-app-m1-07
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
lesson: boolean_thinking
title: "Boolean Thinking"
sortOrder: 7
difficulty: 1
estimatedMinutes: 18
xpReward: 40
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [what_is_logic]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "States two situations where boolean values are used in code"
    - "Correctly evaluates at least three boolean expressions by hand"
    - "Explains why `boolean` cannot hold a third value"
    - "Names the two boolean literals in Java"
    - "Shows how a boolean variable is declared and assigned"
  keywords: [boolean, true, false, literal, evaluate, condition, expression, declare]
  modelAnswer: |
    boolean isLoggedIn = true;
    boolean hasPermission = false;
    boolean canEdit = isLoggedIn && hasPermission;  // false
    // boolean values used for: login state, permissions, feature flags, loop conditions
guidedSteps:
  - id: bool-step-1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      Java has a type that stores exactly two values. Complete the declaration:

      ```java
      ___ gameOver = false;
      ```
    inputConfig:
      placeholder: "type name"
    markingRule:
      matchMode: NORMALIZED
      accepted: [boolean, Boolean]
      rejectedFeedback: "The `boolean` type holds `true` or `false`. `Boolean` (capital B) is the wrapper class — use lowercase `boolean` for simple variable declarations."
    hint: "It's the type named after George Boole. Lowercase, 7 letters."
    reflectionPrompt: "`boolean gameOver = false;` — this is one of the most common variable declarations in games. When the player dies, `gameOver = true;` flips it. Simple, but powerful."
  - id: bool-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the value of `result` after this code?

      ```java
      int x = 10;
      boolean result = x > 5;
      ```
    inputConfig:
      options:
        - "10"
        - "5"
        - "true"
        - "false"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["true"]
      rejectedFeedback: "`x > 5` asks: is 10 greater than 5? Yes. So the expression evaluates to `true`. Note: the result is stored as `boolean`, not as the number 10 or 5."
    hint: "Is 10 greater than 5? What boolean value represents 'yes'?"
    reflectionPrompt: "Comparison operators (`>`, `<`, `==`, `!=`, `>=`, `<=`) always return a `boolean`. You can store these directly: `boolean big = x > 5;` or use them directly in conditions: `if (x > 5)`."
  - id: bool-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Name three things in a game or app that would logically be stored as `boolean` values.
      Explain briefly why each is true/false rather than a number or text.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [logged, active, enabled, open, visible, muted, paused, alive, complete, valid]
      rejectedFeedback: "Examples: `isLoggedIn` (yes/no), `isMuted` (yes/no), `levelComplete` (yes/no). These are all binary states — either the condition holds or it doesn't."
    hint: "Think about states that are either on/off, yes/no, true/false — like whether the player is alive, whether sound is enabled."
    reflectionPrompt: "Boolean variables are perfect for **flags** — binary states that can be checked anywhere in the program. Common flags: `isLoggedIn`, `hasWon`, `isMuted`, `darkModeEnabled`."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What are the only two valid values a `boolean` variable can hold in Java?"
    options:
      - "0 and 1"
      - "yes and no"
      - "true and false"
      - "on and off"
    correctIndex: 2
    feedback: "In Java, `boolean` holds exactly `true` or `false` (lowercase literals). Not 0/1, not 'yes'/'no'. Any other value is a compile error."
  - type: MULTIPLE_CHOICE
    question: "What does `boolean result = (7 > 3);` store in `result`?"
    options:
      - "7"
      - "3"
      - "true"
      - "false"
    correctIndex: 2
    feedback: "`7 > 3` is true (7 is greater than 3), so `result` is assigned `true`. The comparison operator evaluates the mathematical relationship and returns a boolean."

retrieval:
  recall: "What is the boolean data type, what values can it hold, and what Java keyword declares it?"
  explain: "Explain boolean values to a non-programmer by describing everyday yes/no decisions a computer makes."
  mistakeId:
    code: "boolean score = 85;"
    answer: "`boolean` can only hold `true` or `false`. A score of 85 is an `int`. If you want to store 'did the player pass?' you'd use `boolean passed = score >= 70;`."
---

# Hook

True or false: you are reading this lesson.

True or false: the user is logged in. True or false: the file exists. True or false: the password matches.

These binary yes/no questions are the bedrock of every program. And in Java, they have their own type: `boolean`. Two values. No exceptions. No exceptions.

> What yes/no decision does your phone make every time you try to unlock it?

# Lore Introduction

The Guild's simplest but most powerful runes are the truth runes: white for true, black for false. They power every gate, every lock, every decision in the Academy's enchanted infrastructure. A master artificer once built an entire fortress access system from nothing but these two runes, cleverly combined.

*"True and false,"* Archmage Veylan says. *"From these two values, the entire architecture of computing is constructed."*

# Core Learning

## Concept Introduction

The **boolean** type in Java holds exactly two values: `true` or `false`.

```java
boolean isLoggedIn  = true;
boolean hasPremium  = false;
boolean gameOver    = false;
```

Boolean variables are set by:
1. **Literal assignment** — `boolean active = true;`
2. **Comparison expressions** — `boolean isPassing = score >= 60;`
3. **Logical operations** — `boolean canAccess = isLoggedIn && hasPremium;`

## Why It Matters

Every `if` statement, every `while` loop, every conditional expression evaluates to a boolean. Understanding booleans is understanding how your program makes decisions.

## Worked Examples

```java
int age = 20;
boolean isAdult = age >= 18;         // true

String password = "secret";
boolean isValid = password.length() >= 8;   // false — "secret" is 6 chars

boolean bothTrue = isAdult && isValid;       // false — both must be true
```

## Common Mistakes

- Assigning numbers to booleans: `boolean x = 1;` is a compile error in Java.
- Using `=` for comparison: `if (x = true)` assigns, it does not compare.
- Forgetting that `false` is the default for uninitialised boolean fields.
- `Boolean` (capital B) is the wrapper class; lowercase `boolean` is the primitive.

## Mental Model

Boolean is a **light switch**: on (`true`) or off (`false`). Nothing between. Every condition your program evaluates flips a switch, and the program branches based on which position the switch is in.

## Mini Summary

- ✔ `boolean` holds exactly `true` or `false`
- ✔ Declare with: `boolean name = true;` or `boolean name = expression;`
- ✔ Comparison operators return booleans: `age >= 18` → `true` or `false`
- ✔ Booleans power every `if`, `while`, and conditional expression
- ✔ Common uses: login state, game flags, feature toggles, loop guards

# Guided Practice Quest

**The Truth Rune Test**

The Academy's testing chamber needs you to evaluate boolean expressions and predict outcomes before the truth crystals reveal the answer.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Write five boolean variable declarations for a simple quiz game. For each:
1. Choose a meaningful name (e.g. `quizStarted`)
2. Assign an appropriate initial value
3. Write one sentence explaining when it would be flipped to its other value

Then write a single `if` statement that uses at least two of your boolean variables combined with `&&` (AND).

# Integration

**Connecting to Mathematics — Boolean Algebra**

In Boolean algebra (developed by George Boole in 1847), TRUE and FALSE are mathematical values that can be combined with operations: AND (∧), OR (∨), NOT (¬). The laws that govern these operations — like De Morgan's Laws — mirror the laws of ordinary algebra but for logical values.

This algebra is not abstract — it is physically implemented in every transistor in your computer. A transistor is a switch: on (true) or off (false). Billions of such switches, combined through Boolean algebra, produce every computation modern hardware performs.

How does understanding this history change how you think about the `&&` and `||` operators in Java?

# Lore Conclusion

*"Two values,"* Archmage Veylan says, watching the apprentice write the last declaration. *"From two values, you can represent every state, make every decision, and control every flow of execution. The next question is: how do you compare things to produce these values?"*

The truth rune glows white. The next lesson begins.
---
