---
id: se-app-m1-14
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
lesson: input_validation
title: "Input Validation"
sortOrder: 14
difficulty: 2
estimatedMinutes: 25
xpReward: 50
practiceType: JAVA
questType: PRACTICE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [user_interaction, comparisons, logical_operators]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Checks whether a String is empty using isEmpty() or a length check before using it"
    - "Validates that a numeric value falls within an expected range using comparison operators"
    - "Prints a specific, descriptive error message when validation fails"
    - "Takes a different code path (if/else) based on whether input is valid or invalid"
    - "Explains why validation protects both the user and the program"
  keywords: [validate, empty, range, check, error, message, condition, input, guard]
  modelAnswer: |
    import java.util.Scanner;

    public class AgeValidator {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your age: ");
            int age = scanner.nextInt();

            if (name.isEmpty()) {
                System.out.println("Error: name cannot be empty.");
            } else if (age < 0 || age > 150) {
                System.out.println("Error: age must be between 0 and 150.");
            } else {
                System.out.println("Welcome, " + name + "! Age " + age + " is valid.");
            }

            scanner.close();
        }
    }
guidedSteps:
  - id: val-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A user is asked to enter their username. Which check correctly detects an empty String?

      ```java
      String username = scanner.nextLine();
      if (____) {
          System.out.println("Username cannot be empty.");
      }
      ```
    inputConfig:
      options:
        - "`username == null`"
        - "`username.isEmpty()`"
        - "`username == \"\"`"
        - "`username.length > 0`"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["`username.isEmpty()`"]
      rejectedFeedback: "`isEmpty()` is the correct method — it returns `true` if the String has zero characters. `== \"\"` is unreliable for String comparison (use `.equals()`). `length` is a field on arrays, not a method on String — use `length()`."
    hint: "String has an isEmpty() method that returns true when there are no characters."
    reflectionPrompt: "Correct. `isEmpty()` is clean and readable. You can also use `username.length() == 0` or `username.equals(\"\")`, but `isEmpty()` communicates the intent most clearly."
  - id: val-step-2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Write a validation block for a percentage score. The valid range is 0 to 100 inclusive.
      If the score is outside this range, print: `"Error: score must be between 0 and 100."`
      Otherwise print: `"Score accepted: "` followed by the score.
    inputConfig:
      language: java
      starterCode: |
        int score = scanner.nextInt();

        // validate score here
    markingRule:
      matchMode: CONTAINS
      accepted: [if, score, 0, 100, Error]
      rejectedFeedback: |
        A correct solution:
        ```java
        if (score < 0 || score > 100) {
            System.out.println("Error: score must be between 0 and 100.");
        } else {
            System.out.println("Score accepted: " + score);
        }
        ```
    hint: "The invalid condition is: score is less than 0 OR score is greater than 100. Use || to combine these."
    reflectionPrompt: "Good. The pattern `value < min || value > max` is the standard range check. The else branch only runs when the value is within the valid range."
  - id: val-step-3
    sortOrder: 3
    inputType: CODE
    instruction: |
      Combine both checks: validate that a name is not empty AND that an age is between 0 and 120.
      Print a specific error for each failure. If both are valid, print a welcome message.
    inputConfig:
      language: java
      starterCode: |
        import java.util.Scanner;

        public class Validator {
            public static void main(String[] args) {
                Scanner scanner = new Scanner(System.in);

                System.out.print("Name: ");
                String name = scanner.nextLine();

                System.out.print("Age: ");
                int age = scanner.nextInt();

                // validate name, then age, then print welcome

                scanner.close();
            }
        }
    markingRule:
      matchMode: CONTAINS
      accepted: [isEmpty, age, if, else, Error]
      rejectedFeedback: |
        A correct solution chains validations with if-else if:
        ```java
        if (name.isEmpty()) {
            System.out.println("Error: name cannot be empty.");
        } else if (age < 0 || age > 120) {
            System.out.println("Error: age must be between 0 and 120.");
        } else {
            System.out.println("Welcome, " + name + "! Age " + age + " recorded.");
        }
        ```
    hint: "Use if → else if → else. The first failing check prints its error and skips the rest. Only the final else runs when everything is valid."
    reflectionPrompt: "Excellent. Chaining validations with if-else if means only one error is shown at a time — the first one found. This is appropriate for simple forms and keeps the logic flat and readable."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why is input validation important even when you trust your users?"
    options:
      - "It is not — trusted users never make mistakes"
      - "Unvalidated input can cause runtime errors or incorrect results even from honest mistakes"
      - "Validation only matters for security, not correctness"
      - "Validation makes programs slower and should be avoided"
    correctIndex: 1
    feedback: "Even well-meaning users make typos, enter numbers out of range, or leave fields blank. Validation protects the program from crashing and the user from seeing confusing errors."
  - type: MULTIPLE_CHOICE
    question: "Which condition correctly checks that an integer `n` is within the range 1 to 10 inclusive?"
    options:
      - "`n > 1 && n < 10`"
      - "`n >= 1 || n <= 10`"
      - "`n >= 1 && n <= 10`"
      - "`n < 1 || n > 10`"
    correctIndex: 2
    feedback: "`n >= 1 && n <= 10` means 'n is at least 1 AND at most 10'. The || version would be true for almost any number. The `> 1 && < 10` version excludes 1 and 10 themselves."

retrieval:
  recall: "What are two categories of input you should always validate, and what check do you apply to each?"
  explain: "Explain the difference between a guard clause (checking for invalid input first, exiting early) and a normal if-else structure for validation. When might each be preferable?"
  mistakeId:
    code: |
      System.out.print("Enter quantity: ");
      int qty = scanner.nextInt();
      double total = 100.0 / qty;
      System.out.println("Unit price: " + total);
    answer: "If the user enters 0, this causes an `ArithmeticException: / by zero` at runtime. Validate before dividing: `if (qty <= 0) { System.out.println(\"Error: quantity must be positive.\"); } else { double total = 100.0 / qty; ... }`. Always validate before performing operations that could fail on bad input."
---

# Hook

A user types `-999` when asked for their age. Another leaves the name field blank. A third enters `abc` when the program expects a number.

Every interactive program will receive bad input. The only question is whether you planned for it. Unvalidated input silently corrupts calculations, crashes programs, or — in real systems — opens security vulnerabilities. Learning to check input before using it is one of the most valuable habits in software engineering.

> Have you ever seen a program crash or behave strangely because of something you typed? What do you think went wrong?

# Lore Introduction

The Academy's registration desk accepts new apprentices every season. An unguarded registration spell once accepted an applicant with an age of negative forty and a blank name — and admitted them to the wrong year's cohort entirely. The senior archivists were not pleased.

*"Trust no input,"* Archmage Veylan tells the new cohort. *"Not because people are dishonest — but because people make mistakes. It is your program's job to handle those mistakes gracefully."*

# Core Learning

## Concept Introduction

**Input validation** is the practice of checking that received data meets expectations before using it. Two essential categories:

### 1. Empty String Check

```java
String name = scanner.nextLine();
if (name.isEmpty()) {
    System.out.println("Error: name cannot be empty.");
}
```

`String.isEmpty()` returns `true` if the string has zero characters.
You can also check `name.length() == 0` — they are equivalent.

### 2. Numeric Range Check

```java
int age = scanner.nextInt();
if (age < 0 || age > 150) {
    System.out.println("Error: age must be between 0 and 150.");
}
```

The invalid condition uses `||`: out-of-range means below minimum OR above maximum.
The valid range uses `&&`: `age >= 0 && age <= 150`.

### Writing Good Error Messages

A validation error message should tell the user:
- What was wrong
- What the valid options are

```java
// Poor:
System.out.println("Invalid input.");

// Good:
System.out.println("Error: score must be between 0 and 100.");
```

### Chaining Multiple Validations

```java
if (name.isEmpty()) {
    System.out.println("Error: name cannot be empty.");
} else if (age < 0 || age > 120) {
    System.out.println("Error: age must be between 0 and 120.");
} else {
    // both valid — proceed
    System.out.println("Welcome, " + name + "!");
}
```

The `else if` chain means only the first failing check reports an error. The final `else` runs only when all validations pass.

## Why It Matters

- Prevents runtime crashes (division by zero, index out of bounds)
- Produces clear, helpful feedback instead of cryptic exceptions
- Builds user trust through graceful error handling
- Is a prerequisite for secure software (unvalidated input is the source of many vulnerabilities)

## Worked Examples

```java
import java.util.Scanner;

public class DiscountCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter original price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter discount % (0-100): ");
        int discount = scanner.nextInt();

        if (price <= 0) {
            System.out.println("Error: price must be greater than 0.");
        } else if (discount < 0 || discount > 100) {
            System.out.println("Error: discount must be between 0 and 100.");
        } else {
            double finalPrice = price * (1 - discount / 100.0);
            System.out.printf("Final price: $%.2f%n", finalPrice);
        }

        scanner.close();
    }
}
```

## Common Mistakes

- Using `== ""` instead of `.isEmpty()` to check for empty Strings.
- Writing `age > 0 || age < 120` (OR instead of AND) for valid range — this is always true.
- Printing a generic `"Invalid input."` message with no detail — unhelpful to users.
- Performing calculations before validating — if input is zero and you divide by it, you crash before the check ever runs.

## Mental Model

Think of validation as a **series of gates** at the entrance to your program's logic. Each gate checks one condition. If a value fails a gate, it is turned away immediately with a clear message. Only values that pass all gates reach the actual computation. Validate first — compute second.

## Mini Summary

- ✔ Check empty Strings with `.isEmpty()` before using them
- ✔ Check numeric ranges with `value < min || value > max` for the invalid case
- ✔ Write descriptive error messages that state what was wrong and what is expected
- ✔ Chain validations with if-else if so only the first problem is reported
- ✔ Always validate before performing operations that could fail on bad input

# Guided Practice Quest

**The Gatekeeping Rune**

Three challenges to build the gatekeeping rune: detect empty input, reject out-of-range numbers, combine both into a complete validation sequence.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Build a validated ticket-booking form. The program should:

1. Ask for the user's name — must not be empty.
2. Ask for the number of tickets (1 to 10 inclusive).
3. Ask for a seat row letter (A, B, or C) — read as a String.

Validate each input. For failures, print a specific error message for each field. Only if all three inputs are valid, print a booking confirmation:

```
Booking confirmed: [name], [tickets] ticket(s), Row [row].
```

Use if-else if-else chaining so only the first problem is reported. Close the Scanner when done.

# Integration

**Connecting to Psychology — Error Handling and User Trust**

Psychologists studying human error distinguish between **slips** (correct intention, wrong execution — a typo) and **mistakes** (wrong intention — a misunderstood instruction). Validation must handle both: a slip produces a value that is syntactically fine but semantically wrong (like age 999), while a mistake produces a value that reveals misunderstanding (entering a name where a number was expected).

Well-designed validation feedback helps users self-correct. A message like `"Error: age must be between 0 and 150"` teaches the user what the system expects — reducing future mistakes. A message like `"Invalid"` teaches nothing and erodes trust.

How does the quality of your error messages affect whether users can fix their own mistakes without outside help?

# Lore Conclusion

The gatekeeping rune now stands firm. Empty names are turned away. Ages out of range are politely corrected. Only valid applicants pass through to the Academy's records.

*"Validation is not distrust,"* Veylan says, watching a user receive a clear, helpful error message and immediately correct their entry. *"It is guidance. A well-validated program teaches its users what it needs. A program that crashes silently teaches them nothing — except to distrust you."*

The registration desk glows a steady gold, ready for the new season's apprentices.
---
