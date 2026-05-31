---
id: se-app-m1-08
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
lesson: comparisons
title: "Comparisons"
sortOrder: 8
difficulty: 1
estimatedMinutes: 18
xpReward: 40
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [boolean_thinking]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly uses all six comparison operators"
    - "Demonstrates the difference between `==` and `=`"
    - "Evaluates compound comparison expressions by hand"
    - "Explains when to use `==` vs `.equals()` for Strings"
    - "Shows at least two comparisons producing boolean results"
  keywords: [comparison, operator, equal, greater, less, boolean, evaluate, relational]
  modelAnswer: |
    int score = 75;
    boolean passing   = score >= 60;    // true
    boolean perfect   = score == 100;   // false
    boolean notPassed = score < 60;     // false
    String grade = "B";
    boolean isA = grade.equals("A");    // false — use .equals() for Strings
guidedSteps:
  - id: cmp-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the result of `5 >= 5`?
    inputConfig:
      options:
        - "false — 5 is not greater than 5"
        - "true — 5 is equal to 5, and >= includes equality"
        - "Compile error"
        - "5"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["true — 5 is equal to 5, and >= includes equality"]
      rejectedFeedback: "`>=` means 'greater than OR equal to'. Since 5 equals 5, the condition is satisfied. Result: `true`."
    hint: "The `>=` operator returns true if the left side is greater than OR equal to the right side."
    reflectionPrompt: "Exactly. The `>=` (and `<=`) operators are inclusive of equality. Off-by-one errors often arise from using `>` when `>=` was intended."
  - id: cmp-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      What operator checks if two values are **equal** in Java? (Not assignment — comparison)
    inputConfig:
      placeholder: "operator symbol(s)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["=="]
      rejectedFeedback: "`==` is the equality comparison operator. A single `=` is assignment. Using `=` inside an `if` condition is a common bug — the compiler will catch it for booleans."
    hint: "Comparison uses double equals. Assignment uses single."
    reflectionPrompt: "`==` vs `=` is one of the most common mistakes in all programming. `if (x == 5)` checks if x is 5. `x = 5` sets x to 5. The compiler catches most mistakes here, but always double-check."
  - id: cmp-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Why should you use `.equals()` instead of `==` to compare Strings in Java?
      Answer in 1-2 sentences.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [object, reference, memory, equals, content, value, same]
      rejectedFeedback: "`==` compares memory references (are these the exact same object?). `.equals()` compares content (do these Strings contain the same characters?). Two different String objects can contain 'hello' — `==` returns false, `.equals()` returns true."
    hint: "Think about what `==` actually compares for objects — is it the content or the memory location?"
    reflectionPrompt: "Correct. `==` for objects compares identity (same memory address). `.equals()` compares content. For primitives like `int`, `==` compares values. For `String`, always use `.equals()`."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `10 != 10` evaluate to?"
    options:
      - "true"
      - "false"
      - "10"
      - "Compile error"
    correctIndex: 1
    feedback: "`!=` means 'not equal to'. Since 10 equals 10, they are NOT different, so the `!=` condition is false."
  - type: MULTIPLE_CHOICE
    question: "Which operator should you use to compare two String values for equal content?"
    options:
      - "`==`"
      - "`.equals()`"
      - "`>=`"
      - "`!=`"
    correctIndex: 1
    feedback: "`.equals()` compares String content. `==` on Strings checks identity (same object reference in memory), which is not what you usually want."

retrieval:
  recall: "List all six Java comparison operators and what each means."
  explain: "Explain the difference between `=` and `==` to someone who has never programmed."
  mistakeId:
    code: |
      String name = "Alice";
      if (name == "Alice") { System.out.println("Hello!"); }
    answer: "Using `==` to compare Strings checks object identity, not content. Use `.equals()`: `if (name.equals(\"Alice\"))`. With `==`, this might work for string literals but will fail for dynamically created strings."
---

# Hook

Is this password correct? Is the user old enough? Has the score passed the minimum?

Every one of those questions is a comparison: one value measured against another, returning a single `true` or `false`. In Java, six operators do this work — and knowing them precisely is the difference between a program that works and one that almost works.

> Why might "almost right" be worse than "obviously wrong" when it comes to conditions?

# Lore Introduction

The Academy's gate runes compare the visitor's token against the authorised list. Each comparison is precise: equal, not equal, greater, lesser. A rune that reads 'approximately correct' would be a security disaster.

*"Precision in comparison,"* Archmage Veylan says, *"is precision in decision. Learn the six operators. They will govern every choice your code makes."*

# Core Learning

## Concept Introduction

Java has six **comparison operators**, all returning `boolean`:

| Operator | Meaning | Example | Result |
|---|---|---|---|
| `==` | Equal to | `5 == 5` | `true` |
| `!=` | Not equal to | `5 != 3` | `true` |
| `>` | Greater than | `7 > 5` | `true` |
| `<` | Less than | `3 < 10` | `true` |
| `>=` | Greater than or equal | `5 >= 5` | `true` |
| `<=` | Less than or equal | `4 <= 4` | `true` |

**Special case — Strings:** Use `.equals()`, not `==`:
```java
String name = "Alice";
boolean match = name.equals("Alice");   // true  ✓
boolean wrong = name == "Alice";        // unreliable ✗
```

## Why It Matters

Every decision in your code — every `if`, every `while`, every filter — uses comparison operators. Getting them wrong silently breaks your program.

## Worked Examples

```java
int age = 16;
boolean isAdult     = age >= 18;     // false
boolean isTeen      = age >= 13 && age < 20;  // true
boolean exactlySweet = age == 16;    // true

String status = "active";
boolean canLogin = status.equals("active");   // true
```

## Common Mistakes

- Using `=` instead of `==` — assignment vs comparison.
- Using `==` for Strings — use `.equals()` for content comparison.
- Off-by-one with `>` vs `>=` — "18 or above" needs `>=`, not `>`.
- Comparing incompatible types — `"5" == 5` will not compile; they are different types.

## Mental Model

Comparison operators are a **balance scale**. You place one value on each side. The operator defines what the scale is measuring: exact balance (`==`), tilt direction (`<`, `>`), or whether one side is at least as heavy (`>=`, `<=`). The scale returns one answer: balanced (true) or unbalanced (false).

## Mini Summary

- ✔ Six comparison operators: `==`, `!=`, `>`, `<`, `>=`, `<=`
- ✔ All return `boolean`: `true` or `false`
- ✔ Use `.equals()` (not `==`) to compare String content
- ✔ `=` is assignment; `==` is comparison — never confuse them
- ✔ `>=` and `<=` include equality; `>` and `<` are strictly exclusive

# Guided Practice Quest

**The Comparison Crystal**

Six operators, six patterns. Evaluate each comparison in your head before the crystal reveals the truth value.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Write a `boolean` expression for each of the following conditions. Use appropriate comparison operators:

1. "A user must be at least 13 years old"
2. "A password must be exactly 8 characters long"
3. "A temperature is not exactly 0°C"
4. "A score is between 50 and 100 (inclusive)"
5. "The user's name is not 'admin'"

For each, write the Java boolean expression and say whether it uses `==`, `!=`, `>`, `<`, `>=`, or `<=` (or a combination).

# Integration

**Connecting to Mathematics — Order Relations**

Mathematicians formalise comparison through **order relations**: a set S has a total order if for any two elements a and b, either a ≤ b or b ≤ a. Java's comparison operators implement this for numbers. But note: Strings don't have a natural numeric order — which is why `.compareTo()` uses lexicographic (dictionary) ordering, and content equality uses `.equals()`.

This highlights a deep mathematical point: equality and ordering are distinct concepts, and different types require different comparison semantics. How does understanding this help you choose the right comparison strategy in code?

# Lore Conclusion

Six runes, six truths. The apprentice can now compare any two values and determine their relationship precisely.

*"Six operators is all you need,"* says Archmage Veylan. *"Every condition in every program you will ever write uses one of these. Master them completely — especially the difference between equal and assign."*

The gate runes glow white. Access granted.
---
