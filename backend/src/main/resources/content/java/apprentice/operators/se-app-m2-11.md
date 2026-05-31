---
id: se-app-m2-11
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
lesson: logical_operators_in_code
title: "Logical Operators in Code"
sortOrder: 11
difficulty: 2
estimatedMinutes: 22
xpReward: 40
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-10]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Uses && (AND) correctly — both conditions must be true"
    - "Uses || (OR) correctly — at least one condition must be true"
    - "Uses ! (NOT) correctly — inverts a boolean"
    - "Demonstrates understanding of short-circuit evaluation"
    - "Combines logical operators in a realistic if-statement condition"
  keywords: [logical, AND, OR, NOT, &&, ||, !, short-circuit, condition, boolean]
  modelAnswer: |
    ```java
    public class LogicalOperators {
        public static void main(String[] args) {
            int level = 15;
            int gold = 200;
            boolean hasKey = true;
            boolean isBlocked = false;

            // AND: both must be true
            boolean canEnterDungeon = level >= 10 && hasKey;
            System.out.println("Can enter dungeon: " + canEnterDungeon); // true

            // OR: at least one must be true
            boolean canAffordItem = gold >= 150 || level >= 20;
            System.out.println("Can afford item: " + canAffordItem); // true (gold >= 150)

            // NOT: invert
            boolean canProceed = !isBlocked;
            System.out.println("Can proceed: " + canProceed); // true

            // Combined condition
            if (level >= 10 && gold >= 100 && !isBlocked) {
                System.out.println("Quest accepted!");
            }

            // Short-circuit demo
            int health = 0;
            // Second condition not evaluated if first is false
            if (health > 0 && (100 / health > 5)) {
                System.out.println("Healthy ratio");
            } else {
                System.out.println("Health too low"); // This branch runs safely
            }
        }
    }
    ```
guidedSteps:
  - id: se-app-m2-11-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A door opens if the player has the key AND is level 5 or higher. Which condition correctly checks this?

      ```java
      boolean hasKey = true;
      int level = 4;
      ```
    inputConfig:
      options:
        - "`hasKey || level >= 5`"
        - "`hasKey && level >= 5`"
        - "`hasKey == level >= 5`"
        - "`hasKey + level >= 5`"
    markingRule:
      matchMode: EXACT
      accepted: ["`hasKey && level >= 5`"]
      rejectedFeedback: "`&&` (AND) requires BOTH conditions to be true. Since the door requires BOTH a key AND level 5+, `&&` is correct. With `hasKey = true` and `level = 4`, the result is `true && false` = `false` — door stays shut."
    hint: "The word AND in the requirement maps directly to the `&&` operator."
    reflectionPrompt: "`&&` is true only when BOTH sides are true. If either side is false, the whole expression is false. It perfectly models requirements that have multiple simultaneous conditions."

  - id: se-app-m2-11-step2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does **short-circuit evaluation** mean for the `&&` operator?

      ```java
      boolean result = (x > 0) && (100 / x > 5);
      ```
    inputConfig:
      options:
        - "Java evaluates both sides simultaneously to save time"
        - "If the left side is `false`, Java skips evaluating the right side entirely"
        - "If the left side is `true`, Java skips the right side"
        - "Short-circuit means the expression always evaluates to `false`"
    markingRule:
      matchMode: EXACT
      accepted: ["If the left side is `false`, Java skips evaluating the right side entirely"]
      rejectedFeedback: "For `&&`, if the left side is `false`, the overall result is definitely `false` regardless of the right side. Java skips evaluating the right side (short-circuits). This matters when the right side might cause an error — like dividing by zero."
    hint: "If the first condition already makes the whole AND impossible to be true, why evaluate the second?"
    reflectionPrompt: "Short-circuit evaluation is a safety feature. `if (list != null && list.size() > 0)` safely checks for null first — if list is null, the second part is never evaluated, preventing a crash."

  - id: se-app-m2-11-step3
    sortOrder: 3
    inputType: CODE
    instruction: |
      Write an `if` condition (not the full if statement — just the boolean expression in parentheses) that is true when:
      - `score` is at least 100 OR `bonusActive` is true
      - AND the player is NOT banned

      Assume variables: `int score`, `boolean bonusActive`, `boolean isBanned`
    inputConfig:
      placeholder: "(score >= 100 || ...) && ..."
    markingRule:
      matchMode: REGEX
      accepted: ["\\(score\\s*>=\\s*100\\s*\\|\\|\\s*bonusActive\\)\\s*&&\\s*!isBanned", "!isBanned\\s*&&\\s*\\(score\\s*>=\\s*100\\s*\\|\\|\\s*bonusActive\\)"]
      rejectedFeedback: "The correct expression is `(score >= 100 || bonusActive) && !isBanned`. Parentheses are needed to ensure the OR is evaluated first. `!isBanned` inverts the isBanned flag."
    hint: "Group the OR conditions in parentheses first, then AND with the NOT condition."
    reflectionPrompt: "Parentheses control the order of evaluation, just like in arithmetic. `(a || b) && c` is different from `a || (b && c)`. When mixing `&&` and `||`, always use parentheses for clarity."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "When is `A || B` false?"
    options:
      - "When A is false"
      - "When B is false"
      - "When both A and B are false"
      - "When both A and B are true"
    correctIndex: 2
    feedback: "`||` (OR) is false ONLY when BOTH sides are false. If at least one side is true, the OR is true. This is also called inclusive OR — true when A is true, B is true, or both are true."

  - type: MULTIPLE_CHOICE
    question: "What is the result of `!true`?"
    options:
      - "true"
      - "false"
      - "null"
      - "1"
    correctIndex: 1
    feedback: "`!` is the NOT operator — it inverts a boolean. `!true` is `false`. `!false` is `true`. It is the simplest of the three logical operators."

retrieval:
  recall: "Write the Java symbols for the three logical operators AND, OR, and NOT."
  explain: "Explain short-circuit evaluation for the `&&` operator. Give an example where short-circuiting prevents a crash."
  mistakeId:
    code: |
      int health = 50;
      int maxHealth = 100;
      if (health > 0 & health < maxHealth) {
          System.out.println("Damaged but alive");
      }
    answer: "The code uses `&` (bitwise AND) instead of `&&` (logical AND). While `&` can work for booleans, it does NOT short-circuit — both sides are always evaluated. This can cause crashes if the right side depends on the left being true. Use `&&` for boolean conditions. The correct line is: `if (health > 0 && health < maxHealth)`."
---

# Hook

Real decisions rarely have a single condition. "You may enter if you have a ticket AND your seat is reserved." "The alarm rings if the time is 7am OR the temperature drops below zero." "Access is denied if the user is NOT authenticated." These compound conditions — and, or, not — are everywhere in real life, and they are equally present in code. Java's logical operators translate these natural-language constructs directly into code. But there is a subtlety: the order in which conditions are checked matters. A computer can be lazy — in a good way.

# Lore Introduction

"Three conjunctive glyphs," Archmage Veylan announces, writing `&&`, `||`, and `!` in the air. "The AND glyph demands that both sides hold true. The OR glyph is satisfied if either side holds true. The NOT glyph inverts — what was true becomes false, and what was false becomes true." He pauses. "And here is the Academy's secret technique: the AND glyph will not even examine the second condition if the first has already proven the result impossible. This is called short-circuit evaluation, and it is both a performance gift and a safety mechanism."

# Core Learning

## Concept Introduction

Java has three **logical operators** for combining or inverting boolean conditions:

| Operator | Name | Meaning | True when |
|----------|------|---------|-----------|
| `&&` | AND | Both conditions true | Left AND right are both `true` |
| `\|\|` | OR | At least one true | Left OR right (or both) is `true` |
| `!` | NOT | Inverts boolean | The operand is `false` |

**Truth tables:**

| A | B | A && B | A \|\| B | !A |
|---|---|--------|----------|-----|
| true | true | true | true | false |
| true | false | false | true | false |
| false | true | false | true | true |
| false | false | false | false | true |

**Short-circuit evaluation:**
- `&&`: if left side is `false`, right side is **NOT evaluated** (result is already `false`)
- `||`: if left side is `true`, right side is **NOT evaluated** (result is already `true`)

## Why It Matters

Logical operators allow programs to express complex, multi-condition decisions in a single `if` statement. Without them, you would need deeply nested `if` statements to express the same logic. Short-circuit evaluation is also a critical safety feature: `if (list != null && list.size() > 0)` — if `list` is null, the second condition is never evaluated, preventing a NullPointerException crash.

## Worked Examples

**Example 1 — AND: all conditions must hold:**
```java
int level = 12;
boolean hasSword = true;

if (level >= 10 && hasSword) {
    System.out.println("Ready for battle!");
}
// Prints "Ready for battle!" only if BOTH are true
```

**Example 2 — OR: at least one must hold:**
```java
boolean isAdmin = false;
boolean isOwner = true;

if (isAdmin || isOwner) {
    System.out.println("Access granted.");
}
// Prints because isOwner is true — OR only needs one to be true
```

**Example 3 — NOT and combining operators:**
```java
boolean isGameOver = false;
int lives = 2;

if (!isGameOver && lives > 0) {
    System.out.println("Keep playing!");
}
// !isGameOver is true (game is NOT over), and lives > 0 is true → prints
```

## Common Mistakes

- **Using `&` instead of `&&`:** `&` is a bitwise operator that does NOT short-circuit. Always use `&&` for boolean logic.
- **Using `|` instead of `||`:** Same issue — use `||` for boolean OR, not `|`.
- **Missing parentheses with mixed operators:** `a || b && c` evaluates `&&` first (higher precedence). Use `(a || b) && c` if you want OR evaluated first.
- **Double-negating confusingly:** `if (!isNotAdmin)` is clearer as `if (isAdmin)` — simplify when possible.
- **Assuming OR requires both to be false for false result:** OR is only false when BOTH sides are false.

## Mental Model

Think of `&&` as a **security checkpoint with multiple gates**. Every gate must be open for you to pass. One closed gate and you are stopped — no need to check the remaining gates (short-circuit). `||` is a **route with multiple paths to the destination** — if any single path is open, you can reach the destination. `!` is a **sign flipper** — "OPEN" becomes "CLOSED" and vice versa.

## Mini Summary

- `&&` (AND): true only when BOTH sides are true.
- `||` (OR): true when at least ONE side is true.
- `!` (NOT): inverts a boolean value.
- Short-circuit: `&&` skips right side if left is false; `||` skips right side if left is true.
- Use `&&` and `||`, not `&` and `|`, for boolean logic.
- Use parentheses to make the order of evaluation explicit and clear.

# Guided Practice Quest

*"The three conjunctive glyphs work in harmony," Archmage Veylan says. "But they have hierarchy — AND binds more tightly than OR. Parentheses override this hierarchy, as they do in all of the Academy's arithmetic." Complete the exercises to demonstrate command of all three glyphs.*

# Solo Practice Quest

**The Quest Eligibility System**

Write a Java program that evaluates whether a player qualifies for three different quests. Use these starting variables:

```java
int playerLevel = 8;
int playerGold = 350;
boolean hasCompletedTutorial = true;
boolean isBanned = false;
String playerClass = "warrior";
```

Determine and print whether the player qualifies for:
1. **Beginner Quest:** Level >= 5 AND tutorial complete
2. **Rich Merchant Quest:** Gold >= 300 OR level >= 15
3. **Warrior Guild Quest:** Class is "warrior" AND level >= 8 AND NOT banned

Use meaningful `boolean` variable names for each result and print them with labels.

# Integration

**Mathematics connection:** The operators `&&`, `||`, and `!` implement **Boolean algebra**, developed by George Boole in the 1850s. Boole showed that logical reasoning could be expressed as algebraic equations using only TRUE, FALSE, AND, OR, and NOT. This mathematical system became the foundation of digital circuit design a century later — every logic gate in a computer chip is an implementation of these operations in silicon. When you write `&&` in Java, you are executing the same logic that flows through billions of transistors in your processor.

**Philosophy connection:** Aristotle's *Prior Analytics* described the rules of syllogistic logic — how conclusions follow from premises. Modern propositional logic extends this with AND, OR, and NOT connectives. The philosopher Gottlob Frege formalised this in the late 1800s, and Bertrand Russell later demonstrated that mathematics itself could be reduced to logic. When you combine conditions with `&&` and `||`, you are performing propositional reasoning — the same kind Aristotle described over two thousand years ago, now executed billions of times per second.

*Free question: How would you write a condition in Java that is true only when exactly one of two conditions is true (but not both)? This is called "exclusive OR" (XOR). Can you express it using the operators you know?*

# Lore Conclusion

Archmage Veylan erases the three glyphs from the air but holds up his hand as they fade. "You have mastered the tools of compound judgement," he says. "AND, OR, NOT — with these three glyphs, you can express any logical condition that can be known." He picks up a new tablet. "The next glyph family is a subset of what you have already learned — the assignment operators you saw earlier, extended with the power to combine operation and assignment in a single mark. But more usefully, you will also meet the increment and decrement glyphs in greater depth." He smiles. "Efficiency is its own kind of wisdom."
