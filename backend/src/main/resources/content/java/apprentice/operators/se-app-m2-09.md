---
id: se-app-m2-09
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
lesson: arithmetic_operators
title: "Arithmetic Operators"
sortOrder: 9
difficulty: 1
estimatedMinutes: 22
xpReward: 40
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-05]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Uses all five arithmetic operators (+, -, *, /, %) correctly"
    - "Demonstrates understanding that integer division truncates (does not round)"
    - "Uses casting to double to get a decimal result from integer division"
    - "Explains what the modulus operator % returns"
    - "Code compiles and produces correct numeric output"
  keywords: [arithmetic, modulus, integer division, truncation, casting, operator, remainder]
  modelAnswer: |
    ```java
    public class ArithmeticDemo {
        public static void main(String[] args) {
            int a = 17;
            int b = 5;

            System.out.println("Add: " + (a + b));        // 22
            System.out.println("Subtract: " + (a - b));   // 12
            System.out.println("Multiply: " + (a * b));   // 85
            System.out.println("Divide (int): " + (a / b));   // 3 (truncated)
            System.out.println("Modulus: " + (a % b));    // 2

            // Casting to get decimal division
            System.out.println("Divide (double): " + ((double) a / b)); // 3.4

            // Practical: check if a number is even
            int number = 24;
            if (number % 2 == 0) {
                System.out.println(number + " is even.");
            }

            // Practical: extract parts of a number
            int totalMinutes = 137;
            int hours = totalMinutes / 60;     // 2
            int minutes = totalMinutes % 60;   // 17
            System.out.println(hours + " hours and " + minutes + " minutes");
        }
    }
    ```
guidedSteps:
  - id: se-app-m2-09-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the result of this Java expression?

      ```java
      int result = 10 / 3;
      ```
    inputConfig:
      options:
        - "3.33"
        - "3"
        - "4"
        - "3.0"
    markingRule:
      matchMode: EXACT
      accepted: ["3"]
      rejectedFeedback: "Integer division in Java *truncates* the decimal part — it does not round. `10 / 3` = 3.333..., but since both operands are `int`, the result is `int`, and the fractional part `.333` is discarded. The result is `3`."
    hint: "When both numbers are integers in Java, division always produces an integer result. What happens to the decimal?"
    reflectionPrompt: "Integer division truncates — it throws away everything after the decimal point. It does NOT round. `10/3` gives `3`, and `11/3` also gives `3`."

  - id: se-app-m2-09-step2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      What does the `%` (modulus) operator return?

      ```java
      int result = 10 % 3;
      ```

      The value of result is ___.
    inputConfig:
      placeholder: "number"
    markingRule:
      matchMode: EXACT
      accepted: ["1"]
      rejectedFeedback: "`10 % 3` is asking: 'how many are left over after dividing 10 by 3 as evenly as possible?' 3 goes into 10 three times (giving 9), with 1 left over. So the result is `1`."
    hint: "Modulus gives the remainder after division. How many times does 3 go into 10? What's left over?"
    reflectionPrompt: "Modulus is one of the most useful operators in programming. `x % 2 == 0` checks if x is even. `x % 60` extracts the minutes part of a total in seconds. You will use `%` constantly."

  - id: se-app-m2-09-step3
    sortOrder: 3
    inputType: CODE
    instruction: |
      Write Java code that divides 7 by 2 and stores the result as a decimal (not an integer). Use a cast to `double` on one of the operands.
    inputConfig:
      placeholder: "double result = ..."
    markingRule:
      matchMode: REGEX
      accepted: ["double\\s+\\w+\\s*=\\s*\\(double\\)\\s*7\\s*/\\s*2", "double\\s+\\w+\\s*=\\s*7\\.0\\s*/\\s*2", "double\\s+\\w+\\s*=\\s*7\\s*/\\s*2\\.0", "double\\s+\\w+\\s*=\\s*\\(double\\)\\s*7\\s*/\\s*\\(double\\)\\s*2"]
      rejectedFeedback: "Use `(double) 7 / 2` or `7.0 / 2`. Casting one operand to `double` forces the division to produce a decimal result. For example: `double result = (double) 7 / 2;` → 3.5"
    hint: "Put `(double)` before one of the numbers, or use `7.0` instead of `7`."
    reflectionPrompt: "If at least one operand is a `double`, Java performs floating-point division and returns a `double`. `(double) 7 / 2` gives `3.5`."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the value of `15 % 4` in Java?"
    options:
      - "3"
      - "3.75"
      - "4"
      - "1"
    correctIndex: 0
    feedback: "4 goes into 15 three times (4 × 3 = 12), leaving a remainder of 3. So `15 % 4 = 3`. Modulus gives the remainder after integer division."

  - type: MULTIPLE_CHOICE
    question: "A programmer writes `int result = 7 / 2;` expecting 3.5. What actually happens?"
    options:
      - "Java rounds up to 4"
      - "Java stores 3 — integer division truncates the decimal"
      - "Java throws a runtime error"
      - "Java stores 3.5 as an integer somehow"
    correctIndex: 1
    feedback: "Integer division truncates (cuts off) the decimal part. `7 / 2` = 3.5, but stored as `int`, it becomes `3`. To get `3.5`, use `double result = (double) 7 / 2;`"

retrieval:
  recall: "List the five arithmetic operators in Java and describe what each one does."
  explain: "Explain the difference between integer division and floating-point division in Java. Give an example where the results differ."
  mistakeId:
    code: |
      int totalSeconds = 150;
      int minutes = totalSeconds / 60;
      int seconds = totalSeconds / 60; // programmer wants the leftover seconds
      System.out.println(minutes + "m " + seconds + "s");
    answer: "The second calculation is wrong — it also divides, giving `2` (2 minutes) again instead of the remaining seconds. To get leftover seconds, use modulus: `int seconds = totalSeconds % 60;`. This gives 30 (150 - 2×60 = 30). Output should be `2m 30s`."
---

# Hook

Imagine calculating a player's grade, converting seconds to minutes, or splitting a bill equally — all of these require the same set of fundamental operations: add, subtract, multiply, divide, and find the remainder. These five operations are the arithmetic backbone of almost every program ever written. The most surprising of the five — modulus — is also one of the most powerful. It gives you the remainder after division, and with it you can detect even/odd numbers, wrap values around boundaries, and extract components from times or dates. What patterns do you think the remainder operation could help you find?

# Lore Introduction

"The five great calculation glyphs," Archmage Veylan announces, turning to a board where five runes are inscribed. "Addition — combines essence. Subtraction — reduces it. Multiplication — amplifies it. Division — splits it equally. And the fifth glyph," he pauses, tapping a distinctive mark, "the Remainder Rune. It tells you not how many times the split was made, but what was left after all equal portions were taken." Every formula at the Academy uses these five glyphs in combination. Without them, no spell can be calculated, no quantity measured, no result derived.

# Core Learning

## Concept Introduction

Java provides five **arithmetic operators** for numeric calculations:

| Operator | Name | Example | Result |
|----------|------|---------|--------|
| `+` | Addition | `5 + 3` | `8` |
| `-` | Subtraction | `5 - 3` | `2` |
| `*` | Multiplication | `5 * 3` | `15` |
| `/` | Division | `10 / 3` | `3` (integer truncation!) |
| `%` | Modulus (remainder) | `10 % 3` | `1` |

**Critical: Integer Division Truncation**

When both operands are `int`, `/` produces an `int` result. The decimal part is *discarded* (not rounded):

```java
int result = 10 / 3;   // result is 3, not 3.33
int result2 = 7 / 2;   // result2 is 3, not 3.5
```

**To get a decimal result, use casting:**
```java
double result = (double) 10 / 3;  // result is 3.3333...
double result2 = (double) 7 / 2;  // result2 is 3.5
```

## Why It Matters

Arithmetic operators are used in virtually every program. Score calculations, time conversions, coordinate geometry, financial calculations — all rely on these five operators. The modulus operator `%` is particularly powerful for: checking even/odd (`n % 2 == 0`), wrapping values around (`index % arraySize`), and extracting parts of a value (`totalMinutes % 60` gives the minutes component).

## Worked Examples

**Example 1 — Basic arithmetic:**
```java
int a = 20;
int b = 6;
System.out.println(a + b);  // 26
System.out.println(a - b);  // 14
System.out.println(a * b);  // 120
System.out.println(a / b);  // 3 (truncated from 3.333)
System.out.println(a % b);  // 2 (20 = 6*3 + 2)
```

**Example 2 — Time conversion using division and modulus:**
```java
int totalMinutes = 137;
int hours = totalMinutes / 60;    // 2 (how many complete hours)
int minutes = totalMinutes % 60;  // 17 (leftover minutes)
System.out.println(hours + "h " + minutes + "m"); // "2h 17m"
```

**Example 3 — Even/odd check with modulus:**
```java
int number = 42;
if (number % 2 == 0) {
    System.out.println(number + " is even.");
} else {
    System.out.println(number + " is odd.");
}
```

## Common Mistakes

- **Expecting decimal results from integer division:** `7 / 2` is `3`, not `3.5`. Cast to `double` if you need precision.
- **Confusing `/` with `%`:** `/` gives the quotient; `%` gives the *remainder*.
- **Using `%` and expecting a fraction:** `7 % 2` is `1` (the remainder), not `0.5`.
- **Integer overflow with multiplication:** Very large numbers multiplied together can exceed `int`'s limit. Use `long` for very large results.
- **Order of operations:** `2 + 3 * 4` is `14`, not `20`. Multiplication is done before addition.

## Mental Model

Think of division and modulus as a **distribution problem**. You have 17 biscuits to share equally among 5 people. `17 / 5` tells you each person gets 3. `17 % 5` tells you there are 2 left over (17 = 5×3 + 2). Division answers "how many complete shares?" Modulus answers "what was not shared?" Together they give the complete picture of any distribution problem — which is exactly what computers use them for constantly.

## Mini Summary

- Java has five arithmetic operators: `+`, `-`, `*`, `/`, `%`.
- Integer division truncates (discards) the decimal part — `7/2` is `3`.
- To get a decimal result from integer operands, cast one to `double`: `(double) 7 / 2`.
- Modulus `%` returns the *remainder* after division: `10 % 3` is `1`.
- Modulus is used for even/odd checks, time conversions, and wrapping values.
- Operator precedence: `*`, `/`, `%` are evaluated before `+` and `-`.

# Guided Practice Quest

*Archmage Veylan hands you a calculation tablet. "Five glyphs," he says. "Master each." He sets a series of problems before you, each requiring a different arithmetic glyph. The answer glows in the rune once you inscribe it correctly.*

# Solo Practice Quest

**The Score Calculator**

Write a Java program that calculates and displays a player's performance statistics:

1. The player completed `totalLevels = 15` levels out of `maxLevels = 20`.
2. Their total time was `totalSeconds = 4527` seconds.
3. They earned `totalPoints = 1850` points.

Calculate and print:
- Percentage of levels completed (as a `double`, to one meaningful decimal)
- Time in hours, minutes, and seconds (use `/` and `%`)
- Average points per level completed (as a `double`)

Use meaningful variable names and label each output clearly.

# Integration

**Mathematics connection:** The five arithmetic operators map directly to the four basic operations of arithmetic (addition, subtraction, multiplication, division), with modulus added as a computer science essential. The modulus operation is central to **modular arithmetic** — a branch of mathematics foundational to cryptography. The RSA encryption algorithm, which secures most internet traffic, relies heavily on modular arithmetic with very large numbers. Every time you send a secure message, modulus arithmetic is protecting it.

**Philosophy connection:** Gottfried Wilhelm Leibniz — one of the inventors of calculus — also designed early mechanical calculators and dreamed of a "universal calculus" that could mechanically derive all truths through arithmetic. The five arithmetic operators in Java are a direct descendant of this dream: by combining addition, subtraction, multiplication, division, and remainders, programs can calculate anything calculable. Leibniz would likely have found programming a satisfying realisation of his vision, though the scale and complexity would have astonished him.

*Free question: What do you think happens when you divide by zero in Java? Does it crash immediately, produce an error, or something else? What would be the safest way to handle potential division-by-zero situations?*

# Lore Conclusion

Archmage Veylan watches the five calculation glyphs glow on your tablet as you complete the final exercise. "These five," he says, "are the entire vocabulary of numeric spellcraft. With them, you can calculate anything a computer can know." He points to the Remainder Rune last. "Do not underestimate this humble glyph. It has protected kingdoms." In the next lesson, you will learn the comparison glyphs — operators that do not calculate values but instead ask questions, returning either truth or falsehood in response.
