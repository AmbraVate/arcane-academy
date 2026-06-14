---
id: se-app-m1-11
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m1
moduleTitle: "Module 1: Foundations of Computation"
moduleGlyph: "🧠"
moduleSortOrder: 1
topicSlug: inputs_and_outputs
topicTitle: "Inputs and Outputs"
topicSortOrder: 3
lesson: receiving_information
title: "Receiving Information"
sortOrder: 11
difficulty: 1
estimatedMinutes: 20
xpReward: 40
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [variables_and_data_types]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Creates a Scanner object connected to System.in"
    - "Uses nextLine() to read a String from the user"
    - "Uses nextInt() or nextDouble() to read a number from the user"
    - "Stores the input value in a correctly typed variable"
    - "Closes the Scanner after use"
  keywords: [scanner, input, nextLine, nextInt, System.in, read, user, variable]
  modelAnswer: |
    import java.util.Scanner;

    public class Greeting {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your age: ");
            int age = scanner.nextInt();

            System.out.println("Hello, " + name + "! You are " + age + " years old.");

            scanner.close();
        }
    }
guidedSteps:
  - id: inp-step-1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      To read input from the user in Java, you need a `Scanner` connected to `System.in`.
      Complete the blank:

      ```java
      Scanner scanner = new Scanner(____);
      ```
    inputConfig:
      placeholder: "the input source"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["System.in"]
      rejectedFeedback: "`System.in` is the standard input stream — the keyboard. `new Scanner(System.in)` creates a Scanner that reads from the keyboard."
    hint: "System.in is the keyboard input stream, just as System.out is the screen output stream."
    reflectionPrompt: "Correct. `System.in` is the mirror of `System.out` — one reads, one writes. Always pair your Scanner with `System.in` when reading from the keyboard."
  - id: inp-step-2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Write a complete Java program that:
      1. Creates a `Scanner` reading from `System.in`
      2. Prompts the user: `"Enter a number: "`
      3. Reads an integer using `nextInt()`
      4. Stores it in a variable called `number`
      5. Prints: `"You entered: "` followed by the number

      ```java
      import java.util.Scanner;

      public class ReadNumber {
          public static void main(String[] args) {
              // your code here
          }
      }
      ```
    inputConfig:
      language: java
      starterCode: |
        import java.util.Scanner;

        public class ReadNumber {
            public static void main(String[] args) {
                // your code here
            }
        }
    markingRule:
      matchMode: CONTAINS
      accepted: [Scanner, System.in, nextInt, number, System.out]
      rejectedFeedback: |
        A correct solution looks like:
        ```java
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int number = scanner.nextInt();
        System.out.println("You entered: " + number);
        scanner.close();
        ```
    hint: "Use `new Scanner(System.in)` to create the Scanner, then call `nextInt()` on it to read a number."
    reflectionPrompt: "Well done. The pattern is always: create Scanner → prompt → read → use the value. This pattern repeats in almost every interactive Java program."
  - id: inp-step-3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the difference between `nextLine()` and `nextInt()` on a Scanner?
    inputConfig:
      options:
        - "`nextLine()` reads integers, `nextInt()` reads Strings"
        - "`nextLine()` reads a full line as a String, `nextInt()` reads the next integer"
        - "They do the same thing"
        - "`nextLine()` closes the Scanner after reading"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["`nextLine()` reads a full line as a String, `nextInt()` reads the next integer"]
      rejectedFeedback: "`nextLine()` reads everything up to the newline as a `String`. `nextInt()` parses the next token as an `int`. They read different types."
    hint: "The method name tells you the return type: nextLine → String (a line), nextInt → int."
    reflectionPrompt: "Exactly. The Scanner has a family of `next*()` methods — each reads a different type. `nextLine()` for full strings, `nextInt()` for integers, `nextDouble()` for decimals."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which import statement is required to use the Scanner class?"
    options:
      - "`import java.lang.Scanner;`"
      - "`import java.util.Scanner;`"
      - "`import java.io.Scanner;`"
      - "No import needed — Scanner is built in"
    correctIndex: 1
    feedback: "Scanner lives in the `java.util` package. You must add `import java.util.Scanner;` at the top of your file."
  - type: MULTIPLE_CHOICE
    question: "Which method reads a whole line of text input as a String?"
    options:
      - "`nextInt()`"
      - "`next()`"
      - "`nextLine()`"
      - "`readLine()`"
    correctIndex: 2
    feedback: "`nextLine()` reads everything the user typed up to and including Enter, returning it as a `String`. `next()` only reads one whitespace-delimited token."

retrieval:
  recall: "What class do you use to read keyboard input in Java, and what package is it in?"
  explain: "Explain why you must call `scanner.close()` when you are finished reading input."
  mistakeId:
    code: |
      Scanner scanner = new Scanner(System.in);
      int age = scanner.nextInt();
      String name = scanner.nextLine();
    answer: "After `nextInt()`, the newline character the user pressed is still in the buffer. The subsequent `nextLine()` reads that empty newline instead of waiting for new input. Fix: add an extra `scanner.nextLine()` call after `nextInt()` to consume the leftover newline before reading the String."
---

# Hook

Every useful program takes input. A calculator needs numbers. A login screen needs a username. A quiz needs answers. Without the ability to receive information from the outside world, a program can only ever do the same thing every time it runs.

The Scanner class is Java's standard tool for reading keyboard input — and learning it unlocks the door from static programs to interactive ones.

> What is the minimum information a program needs from a user to feel personal rather than generic?

# Lore Introduction

The Academy's enchanted tomes do not merely display fixed text — they respond to the reader's touch, changing their contents based on questions whispered aloud. Every living spell must be able to receive information from its caller.

*"A spell that cannot listen,"* Archmage Veylan explains, *"is a speech, not a conversation. The `Scanner` is how your programs learn to listen."*

# Core Learning

## Concept Introduction

Java reads keyboard input through the **Scanner** class, found in `java.util`:

```java
import java.util.Scanner;
```

To create a Scanner connected to the keyboard:

```java
Scanner scanner = new Scanner(System.in);
```

`System.in` is the standard input stream — the keyboard. `System.out` writes to the screen; `System.in` reads from it.

### Reading Different Types

| Method | Returns | Reads |
|---|---|---|
| `nextLine()` | `String` | Full line until Enter |
| `next()` | `String` | Single word (stops at whitespace) |
| `nextInt()` | `int` | Next integer token |
| `nextDouble()` | `double` | Next decimal token |

### Complete Pattern

```java
import java.util.Scanner;

public class Hello {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("What is your name? ");
        String name = scanner.nextLine();

        System.out.println("Hello, " + name + "!");

        scanner.close();
    }
}
```

### The nextInt + nextLine Pitfall

After `nextInt()`, the newline character from pressing Enter stays in the buffer. If you then call `nextLine()`, it reads that empty line instead of waiting for new input. Fix it by consuming the leftover newline first:

```java
int age = scanner.nextInt();
scanner.nextLine();           // consume leftover newline
String name = scanner.nextLine();  // now reads correctly
```

## Why It Matters

Without input, every program run produces identical output. Input is what makes programs responsive, personalised, and genuinely useful. The Scanner is how Java programs talk back to their users.

## Worked Examples

```java
import java.util.Scanner;

public class Profile {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your age: ");
        int age = scanner.nextInt();

        System.out.print("Enter your GPA: ");
        double gpa = scanner.nextDouble();

        System.out.println(name + " | Age: " + age + " | GPA: " + gpa);

        scanner.close();
    }
}
```

## Common Mistakes

- Forgetting `import java.util.Scanner;` — the class is not imported automatically.
- Using `nextLine()` after `nextInt()` without consuming the leftover newline.
- Not calling `scanner.close()` — this leaks the resource.
- Calling `nextInt()` when the user types a non-numeric value — causes `InputMismatchException`.

## Mental Model

The Scanner is a **reading cursor** on an input stream. It sits at a position in the stream and moves forward as you call `next*()` methods. `nextInt()` reads until it has consumed a full integer token. `nextLine()` reads until it hits a newline. The cursor never goes backwards — missed characters are gone.

## Mini Summary

- ✔ `import java.util.Scanner;` at the top of every file that uses Scanner
- ✔ Create with `new Scanner(System.in)` to read from the keyboard
- ✔ `nextLine()` reads a full line as String; `nextInt()` reads the next integer
- ✔ Always call `scanner.close()` when done
- ✔ After `nextInt()`, add `scanner.nextLine()` before reading a String

# Guided Practice Quest

**The Listening Rune**

The rune needs to hear the user's name and a number before it can activate. Three steps: understand the connection, write the code, distinguish the methods.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Write a complete Java program that:

1. Asks the user for their first name and stores it.
2. Asks the user for their year of birth (as an integer) and stores it.
3. Calculates their approximate age: `2026 - birthYear`.
4. Prints a message: `"Hello [name], you are approximately [age] years old."`

Make sure to import Scanner, close it when done, and handle the `nextInt` + `nextLine` ordering correctly if you read a String after the integer.

# Integration

**Connecting to Psychology — Input and Attention**

Human-computer interaction research shows that users abandon forms and prompts that are confusing or poorly worded. When you write `System.out.print("Enter your age: ")`, you are crafting a micro-interaction. The clarity of that prompt directly affects how correctly users respond.

In psychology, **signal detection theory** distinguishes between the signal (what you want the user to provide) and noise (ambiguity, anxiety, distraction). A well-designed input prompt reduces noise: it specifies the expected type, range, or format. Poor prompts produce bad input data — which then breaks downstream processing.

How does understanding your user's mental state change how you would write input prompts in a program?

# Lore Conclusion

The listening rune flickers and comes alive, waiting. The apprentice has given it ears.

*"The Scanner is the simplest contract between program and person,"* Veylan says. *"The program asks. The user answers. The program remembers. Never take for granted how much trust that exchange requires — from both sides."*

The tome's pages begin to fill with the reader's own words.
---
