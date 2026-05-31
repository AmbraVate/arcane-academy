---
id: se-app-m2-10
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: operators
topicTitle: "Operators"
topicSortOrder: 2
lesson: comparison_operators
title: "Comparison Operators"
sortOrder: 10
difficulty: 1
estimatedMinutes: 20
xpReward: 40
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-04, se-app-m2-09]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Uses all six comparison operators (==, !=, >, <, >=, <=) correctly"
    - "Demonstrates that comparison expressions produce boolean results"
    - "Correctly uses .equals() for String comparison instead of =="
    - "Explains why == does not work reliably for String comparison"
    - "Code compiles and produces correct boolean output"
  keywords: [comparison, boolean, equals, ==, !=, >, <, >=, <=, String, .equals]
  modelAnswer: |
    ```java
    public class ComparisonDemo {
        public static void main(String[] args) {
            int health = 75;
            int maxHealth = 100;
            int score = 500;

            // Six comparison operators, each produces a boolean
            System.out.println(health == 75);          // true
            System.out.println(health != maxHealth);    // true
            System.out.println(score > 400);            // true
            System.out.println(health < maxHealth);     // true
            System.out.println(score >= 500);           // true
            System.out.println(health <= 50);           // false

            // String comparison — use .equals(), not ==
            String playerName = "Elara";
            System.out.println(playerName.equals("Elara")); // true
            System.out.println(playerName.equals("elara")); // false (case-sensitive)

            // Storing comparison results
            boolean isAlive = health > 0;
            boolean hasHighScore = score >= 500;
            System.out.println("Alive: " + isAlive);         // true
            System.out.println("High scorer: " + hasHighScore); // true
        }
    }
    ```
guidedSteps:
  - id: se-app-m2-10-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What type does a comparison expression like `5 > 3` return in Java?
    inputConfig:
      options:
        - "int — it returns 1 for true and 0 for false"
        - "String — it returns \"true\" or \"false\""
        - "boolean — it returns true or false"
        - "double — it returns the difference between the values"
    markingRule:
      matchMode: EXACT
      accepted: ["boolean — it returns true or false"]
      rejectedFeedback: "In Java, all comparison operators return a `boolean` value — either `true` or `false`. `5 > 3` evaluates to `true`. `5 < 3` evaluates to `false`. You can store this result in a `boolean` variable."
    hint: "Comparison expressions ask a yes/no question. What Java type represents yes/no?"
    reflectionPrompt: "Every comparison produces a `boolean`. This is why comparison operators pair naturally with `if` statements — `if` needs a boolean condition."

  - id: se-app-m2-10-step2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A player needs at least 50 gold to buy a sword. Which comparison checks this correctly?

      ```java
      int gold = 45;
      ```
    inputConfig:
      options:
        - "`gold > 50`"
        - "`gold >= 50`"
        - "`gold == 50`"
        - "`gold < 50`"
    markingRule:
      matchMode: EXACT
      accepted: ["`gold >= 50`"]
      rejectedFeedback: "`gold >= 50` means 'gold is greater than OR equal to 50'. This is correct because a player with exactly 50 gold can afford the sword. `gold > 50` would require MORE than 50, excluding a player with exactly 50 gold."
    hint: "The player needs AT LEAST 50 — meaning 50 is also acceptable. Which operator includes the boundary value?"
    reflectionPrompt: "`>` means strictly more than. `>=` means at least (greater than or equal). This distinction matters enormously — an off-by-one error here can prevent a player from buying something they should be able to afford."

  - id: se-app-m2-10-step3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      Why should you use `.equals()` instead of `==` to compare two Strings in Java?
    inputConfig:
      options:
        - "`==` compares lowercase strings only; `.equals()` handles uppercase too"
        - "`==` checks if two String variables point to the same memory object; `.equals()` compares the actual text content"
        - "They are identical — both work equally well for Strings"
        - "`.equals()` is slower but more accurate for numbers; `==` is faster for Strings"
    markingRule:
      matchMode: EXACT
      accepted: ["`==` checks if two String variables point to the same memory object; `.equals()` compares the actual text content"]
      rejectedFeedback: "`==` for Strings checks *reference equality* — whether two variables point to the exact same object in memory. `.equals()` checks *content equality* — whether the actual characters are the same. For comparing text, you almost always want `.equals()`."
    hint: "Strings are objects in Java. `==` compares object references, not content."
    reflectionPrompt: "Always use `.equals()` to compare String values. `==` can give surprising false results even when two Strings contain identical text, because they may be stored as different objects in memory."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `10 != 5` evaluate to in Java?"
    options:
      - "false"
      - "true"
      - "5"
      - "An error"
    correctIndex: 1
    feedback: "`!=` means 'not equal to'. Since 10 is not equal to 5, `10 != 5` evaluates to `true`."

  - type: MULTIPLE_CHOICE
    question: "Which operator checks if two values are equal in Java?"
    options:
      - "`=`"
      - "`equals`"
      - "`==`"
      - "`:`"
    correctIndex: 2
    feedback: "`==` is the equality comparison operator for primitive types. It asks 'are these two values the same?' and returns a boolean. Note: `=` is assignment (store a value), completely different."

retrieval:
  recall: "List all six comparison operators in Java and give a one-line description of each."
  explain: "Explain why `==` should not be used to compare String values in Java, and what should be used instead."
  mistakeId:
    code: |
      String difficulty = "HARD";
      if (difficulty == "HARD") {
          System.out.println("Brave choice!");
      }
    answer: "Using `==` to compare Strings checks *reference equality* (same object in memory), not content equality. This may work in simple cases due to Java's String pooling, but it is unreliable and a well-known source of bugs. The correct approach is `difficulty.equals(\"HARD\")`."
---

# Hook

Every decision in a program starts with a question. "Is the player's health below zero?" "Has the score reached one thousand?" "Is this character's name equal to 'admin'?" These questions all have one thing in common: they produce a single yes or no. In Java, operators that ask these questions are called comparison operators, and their answers — `true` or `false` — are the signal that drives every branch, every loop, every decision in the program. How does a program distinguish truth from falsehood at the speed of a computer?

# Lore Introduction

"The question glyphs," Archmage Veylan says, turning to a new row of runes. "Where the calculation glyphs produce essence — numbers, amounts, quantities — the question glyphs produce only a single truth-mark." He holds up a glowing rune. "Is this vessel's charge greater than that one? The glyph asks; the universe answers with either a bright rune or a dark one." In Arcane Academy, the truth-marks — `true` and `false` — are the input to every conditional spell. The question glyphs are the lenses through which a program perceives the state of the world.

# Core Learning

## Concept Introduction

**Comparison operators** compare two values and return a `boolean` result (`true` or `false`).

| Operator | Meaning | Example | Result |
|----------|---------|---------|--------|
| `==` | Equal to | `5 == 5` | `true` |
| `!=` | Not equal to | `5 != 3` | `true` |
| `>` | Greater than | `10 > 7` | `true` |
| `<` | Less than | `3 < 1` | `false` |
| `>=` | Greater than or equal | `5 >= 5` | `true` |
| `<=` | Less than or equal | `4 <= 3` | `false` |

**Important: String comparison**

`==` checks *reference equality* (same object in memory). For comparing text content, use `.equals()`:

```java
String name = "Elara";
name == "Elara"        // unreliable — do not use
name.equals("Elara")   // correct — compares content
```

**Storing comparison results:**
```java
boolean isHealthLow = health <= 20;
boolean hasWon = score >= WIN_THRESHOLD;
```

## Why It Matters

Comparison operators are the foundation of all decision-making in programs. Without them, a program executes the same instructions every time with no ability to respond to different situations. With them, programs can branch: do one thing if a condition is true, another if it is false. Every `if` statement, every loop condition, every filter — all rely on comparison operators returning `true` or `false`.

## Worked Examples

**Example 1 — Basic comparisons:**
```java
int gold = 75;
System.out.println(gold > 50);    // true
System.out.println(gold < 50);    // false
System.out.println(gold >= 75);   // true (equal counts)
System.out.println(gold == 100);  // false
System.out.println(gold != 100);  // true
```

**Example 2 — Storing comparisons in booleans:**
```java
int health = 15;
boolean isDangerous = health <= 20;
System.out.println("In danger: " + isDangerous); // true
```

**Example 3 — String comparison with .equals():**
```java
String role = "admin";
if (role.equals("admin")) {
    System.out.println("Access granted.");
}
if (!role.equals("guest")) {
    System.out.println("Not a guest.");
}
```

## Common Mistakes

- **Using `=` instead of `==`:** `if (x = 5)` assigns 5 to x instead of comparing. Use `if (x == 5)`.
- **Using `==` for String comparison:** Use `.equals()` for text content comparison.
- **Confusing `>` and `>=`:** `score > 100` requires strictly more than 100; `score >= 100` includes 100. Off-by-one errors from this confusion are extremely common.
- **Expecting comparison to change a variable:** `gold > 50` does not change `gold`. It only produces `true` or `false`.
- **Case sensitivity with `.equals()`:** `"hello".equals("Hello")` is `false`. Strings are case-sensitive.

## Mental Model

Think of comparison operators as **scales or measuring tapes**. They take two values, place them side by side, and report a fact about their relationship. The answer is always binary — either the left side is bigger, or it isn't; either the values match, or they don't. Unlike arithmetic operators that produce new quantities, comparison operators produce *judgements*: pure yes/no verdicts that allow programs to navigate and decide.

## Mini Summary

- Comparison operators produce a `boolean` result: `true` or `false`.
- The six operators: `==`, `!=`, `>`, `<`, `>=`, `<=`.
- `>=` and `<=` include the boundary value; `>` and `<` exclude it.
- Use `.equals()` to compare String content; `==` compares references, not text.
- Comparison results can be stored in `boolean` variables.
- `=` is assignment; `==` is comparison — never confuse them.

# Guided Practice Quest

*Archmage Veylan sets a series of rune pairs before you. "Each pair must be tested with the appropriate question glyph," he says. "Not all glyphs are equal — knowing which one to use is an art in itself." Complete the exercises to master the question glyphs.*

# Solo Practice Quest

**The Eligibility Checker**

Write a Java program that checks whether a player qualifies for various rewards. Use the following starting values:

```java
int playerLevel = 12;
int playerScore = 850;
int playerGold = 200;
String playerClass = "mage";
```

Declare boolean variables using comparisons to answer:
1. Is the player at least level 10?
2. Does the player have more than 1000 score?
3. Does the player have exactly 200 gold?
4. Is the player a mage? (use `.equals()`)
5. Is the player NOT at the maximum level (max is 50)?

Print each boolean result with a label. Then write one line that prints whether the player qualifies for a "veteran bonus" (level >= 10 AND score > 700) — try combining the two boolean variables you already created using `&&`.

# Integration

**Mathematics connection:** Comparison operators in programming directly implement the mathematical *ordering relations* on numbers. The operators `<`, `>`, `<=`, `>=` define what mathematicians call a *total order* on the real numbers — any two distinct numbers are related by exactly one of these operators. The operators `==` and `!=` implement *equality* — one of the most fundamental relations in mathematics and logic. Programming takes these abstract mathematical relations and makes them concrete computational operations.

**Philosophy connection:** The ancient Greek philosopher Aristotle formulated the *Law of Excluded Middle*: every proposition is either true or false, with no middle ground. Comparison operators enforce exactly this law — they can only return `true` or `false`, never "maybe" or "partially." This binary logic, formalised by George Boole in the 19th century (giving us Boolean algebra), is the logical foundation on which all digital computers are built. Every comparison a program makes is an act of Aristotelian logic.

*Free question: Java's `String.equalsIgnoreCase()` method compares Strings without caring about capitalisation. When would this be more useful than `equals()`, and when could it cause problems?*

# Lore Conclusion

Archmage Veylan surveys the completed tablet of question-glyph exercises and nods. "You have learned to ask the right questions," he says. "But asking is only half the art. The other half is combining answers — joining truth-marks together to create complex judgements." He sketches two truth-marks side by side on the board. "AND. OR. NOT. These are the logical glyphs, and they are the subject of your next lesson." The question glyphs give you facts about individual values. The logical glyphs let you reason about combinations of facts — and from combinations, all real decisions are made.
