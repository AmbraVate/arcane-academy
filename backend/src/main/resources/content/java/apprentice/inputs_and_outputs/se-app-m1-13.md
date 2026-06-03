---
id: se-app-m1-13
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
lesson: user_interaction
title: "User Interaction"
sortOrder: 13
difficulty: 2
estimatedMinutes: 22
xpReward: 40
practiceType: JAVA
questType: PRACTICE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [receiving_information, producing_output]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Uses System.out.print() (not println) to display a prompt on the same line as where input will appear"
    - "Reads the user's response immediately after the prompt"
    - "Uses the captured input value in a subsequent calculation or personalised message"
    - "Handles at least two rounds of input-output exchange"
    - "Closes the Scanner after all input has been read"
  keywords: [prompt, scanner, input, output, interaction, nextLine, response, personalise]
  modelAnswer: |
    import java.util.Scanner;

    public class QuizGame {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your score (0-100): ");
            int score = scanner.nextInt();

            String grade;
            if (score >= 90) {
                grade = "A";
            } else if (score >= 70) {
                grade = "B";
            } else {
                grade = "C";
            }

            System.out.println("Well done, " + name + "! Your grade is: " + grade);

            scanner.close();
        }
    }
guidedSteps:
  - id: usr-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      When prompting a user for input, which output method should you use so the cursor stays on the same line as the prompt?

      ```java
      System.out.___("Enter your name: ");
      String name = scanner.nextLine();
      ```
    inputConfig:
      options:
        - "`println`"
        - "`print`"
        - "`printf` with no `%n`"
        - "Both `print` and `printf` with no `%n`"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Both `print` and `printf` with no `%n`"]
      rejectedFeedback: "Use `print()` so the cursor stays on the same line, letting the user type their answer beside the prompt. `println` moves the cursor to a new line first, which looks odd."
    hint: "You want the user's typed text to appear right after the colon, on the same line."
    reflectionPrompt: "Correct. The prompt-then-read pattern uses `print` (not `println`) so the interaction feels like a natural question-and-answer on one line: `Enter your name: Alice`."
  - id: usr-step-2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Complete this interactive program that asks the user for two numbers and prints their sum.

      The output should look like:
      ```
      Enter first number: 5
      Enter second number: 3
      Sum: 8
      ```

      Write the full program body (inside `main`).
    inputConfig:
      language: java
      starterCode: |
        import java.util.Scanner;

        public class SumCalculator {
            public static void main(String[] args) {
                Scanner scanner = new Scanner(System.in);

                // prompt for and read first number

                // prompt for and read second number

                // calculate and print the sum

                scanner.close();
            }
        }
    markingRule:
      matchMode: CONTAINS
      accepted: [Scanner, nextInt, print, sum, scanner.close]
      rejectedFeedback: |
        A correct solution:
        ```java
        System.out.print("Enter first number: ");
        int a = scanner.nextInt();
        System.out.print("Enter second number: ");
        int b = scanner.nextInt();
        int sum = a + b;
        System.out.println("Sum: " + sum);
        scanner.close();
        ```
    hint: "Use System.out.print() for the prompts, scanner.nextInt() to read each number, then print the result."
    reflectionPrompt: "Well done. Each round follows the same prompt → read → store pattern. Chaining these rounds creates an interactive conversation between user and program."
  - id: usr-step-3
    sortOrder: 3
    inputType: CODE
    instruction: |
      Extend the program to personalise the output. Ask the user for their name first, then ask for the two numbers.
      Print: `"[name], the sum of [a] and [b] is [sum]."`
    inputConfig:
      language: java
      starterCode: |
        import java.util.Scanner;

        public class PersonalisedSum {
            public static void main(String[] args) {
                Scanner scanner = new Scanner(System.in);

                // read name, then two numbers, then print personalised result

                scanner.close();
            }
        }
    markingRule:
      matchMode: CONTAINS
      accepted: [nextLine, nextInt, name, sum]
      rejectedFeedback: |
        Remember: after `nextLine()` for the name, `nextInt()` works fine. But if name comes AFTER `nextInt()`, you need an extra `scanner.nextLine()` to consume the buffer newline. Sample:
        ```java
        System.out.print("Your name: ");
        String name = scanner.nextLine();
        System.out.print("First number: ");
        int a = scanner.nextInt();
        System.out.print("Second number: ");
        int b = scanner.nextInt();
        System.out.println(name + ", the sum of " + a + " and " + b + " is " + (a + b) + ".");
        ```
    hint: "Read the name first with nextLine(), then the numbers with nextInt(). Use (a + b) in parentheses inside the println to force addition before concatenation."
    reflectionPrompt: "Perfect. Personalisation is simply using the stored name variable in your output — but it transforms the experience from generic to responsive."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the interaction pattern for prompting and reading user input?"
    options:
      - "Read input first, then print the prompt"
      - "Print prompt with print(), then read with scanner"
      - "Print prompt with println(), then read with scanner"
      - "Use printf() to both prompt and read at once"
    correctIndex: 1
    feedback: "The pattern is: `System.out.print(\"prompt: \")` → `scanner.nextXxx()`. Using `print` (not `println`) keeps the cursor on the same line as the prompt, so the user types right after it."
  - type: MULTIPLE_CHOICE
    question: "Why should you use parentheses when adding numbers inside a println with string concatenation?"
    options:
      - "Parentheses are required by the compiler"
      - "Without parentheses, + between numbers after a String becomes concatenation not addition"
      - "Parentheses make the output larger"
      - "It is just a style preference with no effect"
    correctIndex: 1
    feedback: "`\"Sum: \" + 3 + 4` gives `\"Sum: 34\"` because + is left-to-right and a String is already in play. `\"Sum: \" + (3 + 4)` gives `\"Sum: 7\"` because the parentheses force arithmetic first."

retrieval:
  recall: "Describe the prompt-then-read pattern. Which output method do you use for the prompt, and why?"
  explain: "Explain why interactive programs feel different from programs that just print fixed text. What role does the user's input play in changing the output?"
  mistakeId:
    code: |
      System.out.println("Enter your age: ");
      int age = scanner.nextInt();
      System.out.println("You are " + age + " years old.");
    answer: "Using `println` for the prompt moves the cursor to a new line, so the user sees the prompt on one line and then types on the blank line below. Use `System.out.print(\"Enter your age: \")` to keep the cursor beside the prompt, which looks like: `Enter your age: 25`."
---

# Hook

A program that prints the same thing every time it runs is a pamphlet. A program that asks questions and responds to the answers is a conversation.

Combining input and output into a coherent interaction loop is the moment programming stops being an exercise and starts being a tool. The pattern is simple, but the details — which `print` method to use for prompts, how to handle multiple rounds — matter more than they appear.

> Think of the last time a poorly designed form or prompt confused you. What specifically made it unclear?

# Lore Introduction

The enchanted kiosks at the Academy entrance do not simply display a welcome message — they ask each visitor a question, listen to the response, and tailor their reply. They are not announcements; they are exchanges.

*"The difference between a notice board and an oracle,"* Archmage Veylan says, *"is whether the oracle waits for you to speak. Build programs that wait — and respond."*

# Core Learning

## Concept Introduction

User interaction is built from the **prompt-read-respond** cycle:

1. **Prompt** — tell the user what you need from them (`System.out.print`)
2. **Read** — capture their response (`scanner.nextLine()` / `scanner.nextInt()`)
3. **Process** — use the captured value in logic or calculations
4. **Respond** — display a result personalised to their input

Repeat this cycle as many times as needed.

### The Prompt Pattern

```java
System.out.print("Enter your name: ");   // print, not println
String name = scanner.nextLine();        // reads immediately after the prompt
```

Using `print` (not `println`) means the cursor stays beside the colon, so the interaction looks like:
```
Enter your name: Alice
```
Rather than:
```
Enter your name:
Alice
```

### Multi-Round Interaction

```java
Scanner scanner = new Scanner(System.in);

System.out.print("Username: ");
String username = scanner.nextLine();

System.out.print("Score: ");
int score = scanner.nextInt();

System.out.printf("Welcome back, %s! Your score is %d.%n", username, score);

scanner.close();
```

### Using Input in Logic

The power of interaction comes from using the captured input to make decisions:

```java
System.out.print("Enter temperature in Celsius: ");
double celsius = scanner.nextDouble();
double fahrenheit = celsius * 9.0 / 5.0 + 32;
System.out.printf("%.1f°C = %.1f°F%n", celsius, fahrenheit);
```

## Why It Matters

Nearly every real application — web forms, command-line tools, games, calculators — is built on this cycle. Mastering the prompt-read-respond pattern is the foundation of all interactive software.

## Worked Examples

```java
import java.util.Scanner;

public class GradeChecker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter student name: ");
        String name = scanner.nextLine();

        System.out.print("Enter score (0-100): ");
        int score = scanner.nextInt();

        String grade;
        if (score >= 90) {
            grade = "A — Distinction";
        } else if (score >= 70) {
            grade = "B — Merit";
        } else if (score >= 50) {
            grade = "C — Pass";
        } else {
            grade = "F — Fail";
        }

        System.out.println(name + ": " + grade);
        scanner.close();
    }
}
```

## Common Mistakes

- Using `println` for the prompt — the cursor jumps to a new line, confusing users.
- Forgetting to use the captured variable in the output — the interaction has no effect.
- Not closing the Scanner after all reads are complete.
- Putting `scanner.nextLine()` after `nextInt()` without consuming the leftover newline first.

## Mental Model

Think of each interaction round as a **question card**: you print the question on the front (`print`), the user writes their answer on the back (Scanner reads it), and you file the card (store in a variable). At the end, you use all the filled-in cards to produce your response. Each card is a complete unit: prompt → read → store.

## Mini Summary

- ✔ Use `System.out.print()` for prompts — keeps the cursor beside the colon
- ✔ Immediately call the Scanner method on the next line after the prompt
- ✔ Store every response in a named variable — you will need it later
- ✔ Use the stored values in calculations, conditions, or personalised output
- ✔ Close the Scanner once all input has been read

# Guided Practice Quest

**The Oracle Kiosk**

The kiosk needs three rounds of conversation before it can deliver a personalised result. Each step builds on the previous one.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Build an interactive unit converter. The program should:

1. Ask the user what they want to convert — `"Convert (km/miles): "` — and read their choice as a String.
2. Ask for the numeric value to convert.
3. If the user entered `"km"`, convert to miles (multiply by 0.621371) and display the result.
4. If the user entered `"miles"`, convert to km (multiply by 1.60934) and display the result.
5. If neither, print `"Unknown unit."`.

Use `printf` to display results to 2 decimal places. Use `print` for all prompts.

# Integration

**Connecting to Psychology — Dialogue and Feedback Loops**

Interaction designers study **feedback loops** — how quickly a system responds to a user's action. Poor feedback (slow, vague, or absent) increases cognitive load and frustration. When you write a prompt, you are creating a micro-feedback loop: the user acts (types), the program responds (prints output).

Research in human-computer interaction shows that users form mental models of a system within the first few interactions. A clear, consistent prompt-read-respond cycle builds trust and understanding quickly. A confusing one — prompts that appear on the wrong line, output that ignores what was typed — damages the mental model immediately.

How does thinking about the user's mental model change the way you design the order and wording of your prompts?

# Lore Conclusion

The kiosk now speaks and listens in turn, its responses woven from the visitor's own words.

*"An interactive spell is a pact,"* Veylan observes. *"The caster offers information; the spell offers a result. Neither party can act well without the other. Design that exchange with care — for every confusing prompt is a broken pact."*

The kiosk's rune glows a warm green: ready for the next visitor.
---
