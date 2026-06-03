---
id: se-app-m1-12
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m1
moduleTitle: "Module 1: Foundations of Computation"
moduleGlyph: "🧠"
moduleSortOrder: 1
topicSlug: inputs_and_outputs
topicTitle: "Inputs and Outputs"
topicSortOrder: 3
lesson: producing_output
title: "Producing Output"
sortOrder: 12
difficulty: 1
estimatedMinutes: 18
xpReward: 40
practiceType: JAVA
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [variables_and_data_types]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Uses System.out.println() to print a line ending with a newline"
    - "Uses System.out.print() to print without a newline"
    - "Uses System.out.printf() or String.format() with at least one format specifier"
    - "Concatenates strings and variables correctly using +"
    - "Explains the difference between println and print"
  keywords: [println, print, printf, output, concatenation, format, newline, string]
  modelAnswer: |
    String name = "Alice";
    int score = 95;
    double average = 88.5;

    // Basic output with newline
    System.out.println("Results:");

    // Concatenation
    System.out.println("Name: " + name + ", Score: " + score);

    // printf for formatted output
    System.out.printf("Average: %.1f%n", average);

    // print without newline — next output continues on same line
    System.out.print("Loading");
    System.out.print("...");
    System.out.println(" done!");
guidedSteps:
  - id: out-step-1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      What is the difference in output between these two lines?

      ```java
      System.out.print("Hello");
      System.out.println("Hello");
      ```

      Complete: `println` adds a ____ at the end of the output.
    inputConfig:
      placeholder: "what does println add?"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["newline", "new line", "line break", "\\n"]
      rejectedFeedback: "`println` adds a newline (`\\n`) at the end, moving the cursor to the next line. `print` outputs the text and leaves the cursor on the same line."
    hint: "Think about where the cursor ends up after each call — same line or next line?"
    reflectionPrompt: "Correct. `println` = print + newline. The `ln` suffix is the shorthand. This distinction matters when you want output to appear on the same line or across multiple lines."
  - id: out-step-2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Write a Java snippet that prints the following output **exactly**, using `printf`:

      ```
      Name: Alice
      Score: 95
      Average: 88.50
      ```

      Use `%s` for String, `%d` for int, `%.2f` for double with two decimal places, and `%n` for newline in printf.
    inputConfig:
      language: java
      starterCode: |
        String name = "Alice";
        int score = 95;
        double average = 88.5;

        // use printf here
    markingRule:
      matchMode: CONTAINS
      accepted: [printf, "%s", "%d", "%.2f"]
      rejectedFeedback: |
        A correct solution:
        ```java
        System.out.printf("Name: %s%n", name);
        System.out.printf("Score: %d%n", score);
        System.out.printf("Average: %.2f%n", average);
        ```
    hint: "printf uses format specifiers: %s for String, %d for int, %.2f for double to 2 decimal places, %n for newline."
    reflectionPrompt: "Good. `printf` gives you precise control over formatting — essential for tables, reports, and any output where alignment matters."
  - id: out-step-3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does this line print?

      ```java
      System.out.println("Value: " + 3 + 4);
      ```
    inputConfig:
      options:
        - "`Value: 7`"
        - "`Value: 34`"
        - "`Value: 3 4`"
        - "Compile error"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["`Value: 34`"]
      rejectedFeedback: "String concatenation with `+` is left-to-right. `\"Value: \" + 3` produces `\"Value: 3\"`, then `+ 4` appends `4` as a string, giving `\"Value: 34\"`. To force addition, use parentheses: `\"Value: \" + (3 + 4)`."
    hint: "Java evaluates + left to right. Once a String is involved, + becomes concatenation, not addition."
    reflectionPrompt: "This is one of Java's classic traps. Once + encounters a String, it concatenates everything that follows as strings. Use parentheses to force arithmetic: `\"Value: \" + (3 + 4)` gives `\"Value: 7\"`."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which method would you use to print text without moving to a new line afterward?"
    options:
      - "`System.out.println()`"
      - "`System.out.print()`"
      - "`System.out.printf()`"
      - "Both print and printf"
    correctIndex: 3
    feedback: "Both `print()` and `printf()` leave the cursor on the same line. `println()` adds a newline. `printf()` also stays on the same line unless you include `%n` in the format string."
  - type: MULTIPLE_CHOICE
    question: "What format specifier do you use in printf to display a double to 2 decimal places?"
    options:
      - "`%d`"
      - "`%s`"
      - "`%f`"
      - "`%.2f`"
    correctIndex: 3
    feedback: "`%.2f` means: format as a floating-point number with exactly 2 digits after the decimal point. `%f` without precision defaults to 6 decimal places."

retrieval:
  recall: "List three output methods in Java and describe what each one does differently."
  explain: "Why does `System.out.println(\"Total: \" + 2 + 3)` print `Total: 23` instead of `Total: 5`?"
  mistakeId:
    code: |
      double price = 9.99;
      System.out.println("Price: $" + price);
    answer: "For currency display, `println` with concatenation will print `Price: $9.99` which works here but loses control of decimal places. Use `printf` for reliable formatting: `System.out.printf(\"Price: $%.2f%n\", price)` — this ensures exactly two decimal places even if the value were `9.9` or `10.0`."
---

# Hook

Your program has computed the answer. Now what? A result locked inside a variable is invisible — it has to reach the screen before it is useful to anyone.

Java gives you three output methods, each suited to a different job. Knowing which to use, and understanding how string concatenation with `+` can surprise you, separates clean output from confusing output.

> If you could only see the output of a program and not its code, what would make you trust it?

# Lore Introduction

The crystal display orbs at Arcane Academy show only what they are explicitly told to show. An orb that is never addressed stays dark. Spells that compute great truths but never broadcast them are wasted.

*"Computation without communication is silence,"* Archmage Veylan says. *"Learn to speak clearly to the outside world — in the right format, with the right precision."*

# Core Learning

## Concept Introduction

Java outputs to the screen through `System.out`, which has three main methods:

| Method | Newline? | Best for |
|---|---|---|
| `System.out.println(value)` | Yes | Most single-value output |
| `System.out.print(value)` | No | Output that continues on the same line |
| `System.out.printf(format, args...)` | Only if `%n` included | Formatted, aligned, or precise output |

### String Concatenation with +

```java
String name = "Alice";
int score = 95;
System.out.println("Name: " + name + ", Score: " + score);
// Output: Name: Alice, Score: 95
```

Java converts non-String values to strings automatically when concatenating with `+`. But watch the order of operations:

```java
System.out.println("Total: " + 2 + 3);   // "Total: 23"  — concatenation
System.out.println("Total: " + (2 + 3)); // "Total: 5"   — addition first
```

### printf Format Specifiers

| Specifier | Type | Example |
|---|---|---|
| `%s` | String | `"Alice"` |
| `%d` | int / long | `42` |
| `%f` | double | `3.141593` |
| `%.2f` | double (2 dp) | `3.14` |
| `%n` | Newline | (platform-safe newline) |

```java
double price = 19.99;
System.out.printf("Price: $%.2f%n", price);
// Output: Price: $19.99
```

## Why It Matters

Every user-facing result needs to be communicated clearly. Poor output — wrong decimal places, concatenation mistakes, missing labels — erodes trust in the program even if the computation was correct.

## Worked Examples

```java
public class Receipt {
    public static void main(String[] args) {
        String item = "Spellbook";
        int quantity = 3;
        double unitPrice = 12.50;
        double total = quantity * unitPrice;

        System.out.println("=== Receipt ===");
        System.out.printf("Item:     %s%n", item);
        System.out.printf("Qty:      %d%n", quantity);
        System.out.printf("Price:    $%.2f each%n", unitPrice);
        System.out.printf("Total:    $%.2f%n", total);
        System.out.println("===============");
    }
}
```

Output:
```
=== Receipt ===
Item:     Spellbook
Qty:      3
Price:    $12.50 each
Total:    $37.50
===============
```

## Common Mistakes

- Using `println` with `+` when arithmetic is intended — enclose the calculation in parentheses.
- Forgetting `%n` in `printf` — output appears with no line break.
- Using `%d` for a `double` — mismatched specifier causes a runtime exception.
- Calling `System.out.Println` (capital P) — Java is case-sensitive; this will not compile.

## Mental Model

Think of `print` and `println` as writing on a whiteboard — `print` leaves the pen where it stopped, `println` lifts the pen and starts a new line. `printf` is a **template**: you write the pattern first (`"Score: %d"`) and fill in the blanks later. Templates give you precision and alignment that concatenation cannot easily achieve.

## Mini Summary

- ✔ `println()` prints and moves to the next line; `print()` stays on the same line
- ✔ `printf()` uses format specifiers for precise, aligned output
- ✔ `+` with a String on the left concatenates rather than adds — use parentheses for arithmetic
- ✔ `%.2f` formats a double to exactly 2 decimal places
- ✔ Use `%n` in printf for a portable newline character

# Guided Practice Quest

**The Speaking Crystal**

The crystal must speak clearly: sometimes on one line, sometimes formatted with precision. Three tasks to unlock its voice.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Write a complete Java program that produces a formatted student report card. The program should define (as variables):

- Student name (String)
- Three subject scores (int)
- Average score (double, calculated from the three scores)

Output the report in this exact format:
```
=== Student Report ===
Name:    [name]
Maths:   [score]
Science: [score]
English: [score]
Average: [XX.X]
======================
```

Use `printf` for the numeric lines to control alignment and decimal precision.

# Integration

**Connecting to Mathematics — Significant Figures and Precision**

When you write `%.2f` in `printf`, you are applying the mathematical concept of **significant figures** — a decision about how much precision is meaningful to communicate. Reporting a currency value as `$19.9999999` is technically more precise but practically misleading; `$20.00` communicates the useful truth.

In scientific computing, mismatched precision causes real problems: a result printed to 10 decimal places implies accuracy the underlying measurement may not support. The `printf` format specifier is your tool for aligning displayed precision with actual meaningful precision.

How might the choice of decimal places in your output affect how users interpret and trust the results your program produces?

# Lore Conclusion

The crystal orb now speaks with clarity and form. Numbers appear as numbers, prices carry their decimal crowns, and lines break exactly where the apprentice intends.

*"Output is the face of your program,"* Veylan says, inspecting the formatted receipt. *"The computation inside may be perfect, but if the output is garbled, your users will doubt everything. Format your results with the same care you apply to your logic."*

The orb glows steadily, awaiting the next message to broadcast.
---
