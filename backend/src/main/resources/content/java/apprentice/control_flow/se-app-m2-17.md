---
id: se-app-m2-17
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: control_flow
topicTitle: "Control Flow"
topicSortOrder: 3
lesson: switch_statements
title: "Switch Statements"
sortOrder: 17
difficulty: 2
estimatedMinutes: 22
xpReward: 50
practiceType: JAVA
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-16]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly writes switch-expression arrow syntax with at least two cases and a default"
    - "Explains when switch is clearer than an else-if chain"
    - "Explains the purpose of the default case"
    - "Identifies the types that can be used as switch selectors (int, String, enum)"
    - "Describes the difference between arrow syntax (Java 14+) and traditional fall-through switch"
  keywords: [switch, case, default, arrow, expression, selector, fall-through, pattern]
  modelAnswer: |
    A switch statement (or switch expression in Java 14+) dispatches to one of several branches based on the exact value of a selector expression. The modern arrow syntax is:

    ```java
    switch (selector) {
        case value1 -> action1;
        case value2 -> action2;
        default -> defaultAction;
    }
    ```

    Switch is clearer than `else if` when you are comparing a single variable against a fixed set of known values (like days of the week, command names, or HTTP status codes). With `else if`, every condition repeats the variable name; with switch, the variable is named once.

    The `default` case is a catch-all that runs when no other case matches. It is good practice to always include a `default` so the program has defined behaviour for unexpected values.

    Switch works with `int`, `String`, `char`, `byte`, `short`, `long` (as of Java 21), and enum types. The traditional switch (without arrows) had "fall-through" — execution continued from the matched case into the next case unless a `break` was written. Arrow-syntax switch does not fall through, which eliminates a common source of bugs.
guidedSteps:
  - id: se-app-m2-17-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      ```java
      String day = "MONDAY";
      switch (day) {
          case "SATURDAY", "SUNDAY" -> System.out.println("Weekend");
          case "MONDAY" -> System.out.println("Start of week");
          default -> System.out.println("Weekday");
      }
      ```
      What is printed?
    inputConfig:
      options:
        - "Weekend"
        - "Start of week"
        - "Weekday"
        - "Nothing — switch requires int values only"
    markingRule:
      matchMode: EXACT
      accepted: ["Start of week"]
      rejectedFeedback: "The selector `day` equals 'MONDAY'. The case `\"MONDAY\"` matches, so 'Start of week' is printed. The `default` case is skipped because a match was found. Switch works with String values in Java."
    hint: "Which case label matches the value 'MONDAY'?"
    reflectionPrompt: "Switch expressions work with String values in Java. Multiple values can share a case using a comma: `case \"SATURDAY\", \"SUNDAY\" ->`. This is more concise than two separate cases."

  - id: se-app-m2-17-step2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Write a switch expression for an int variable `statusCode`. Print "OK" for 200, "Not Found" for 404, and "Error" for any other value. Use arrow syntax.
    inputConfig:
      language: java
      starterCode: |
        int statusCode = 404;
        // Write your switch below
    markingRule:
      matchMode: CONTAINS
      accepted: ["switch", "200", "404", "default", "OK", "Not Found", "Error"]
      rejectedFeedback: |
        ```java
        switch (statusCode) {
            case 200 -> System.out.println("OK");
            case 404 -> System.out.println("Not Found");
            default -> System.out.println("Error");
        }
        ```
        Arrow syntax (Java 14+) does not fall through between cases and does not require `break`.
    hint: "Use `case 200 ->` for the 200 case, `case 404 ->` for 404, and `default ->` for everything else."
    reflectionPrompt: "The `default` case handles all values not explicitly listed. It is good practice to always include it, even if you believe you have covered all cases — future values may not be anticipated."

  - id: se-app-m2-17-step3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe ONE situation where a switch is a better choice than an else-if chain, and ONE situation where else-if is better.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["exact", "value", "range", "multiple", "single", "variable"]
      rejectedFeedback: "Switch is better when comparing one variable against exact values (e.g., a menu option, a day name, a command string). Else-if is better when conditions involve ranges (score >= 80), multiple variables, or complex boolean expressions that cannot be expressed as exact-match cases."
    hint: "Switch compares exact values; else-if evaluates arbitrary boolean expressions. Which needs ranges vs. exact values?"
    reflectionPrompt: "Switch excels at 'one variable, many possible values' problems. Else-if excels at 'complex or range-based conditions' problems. Choosing the right tool makes code more readable."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the purpose of the `default` case in a switch statement?"
    options:
      - "It runs first, before any other cases are evaluated"
      - "It runs when no other case label matches the selector value"
      - "It is required; a switch without default will not compile"
      - "It forces all other cases to be skipped"
    correctIndex: 1
    feedback: "The `default` case is a catch-all that runs when the selector value does not match any of the listed case labels. It is like the final `else` in an else-if chain. In Java, `default` is optional but highly recommended."

  - type: MULTIPLE_CHOICE
    question: "What is the advantage of arrow-syntax switch (`case X ->`) over traditional switch with `break`?"
    options:
      - "Arrow syntax is the only syntax that works with Strings"
      - "Arrow syntax automatically falls through to the next case"
      - "Arrow syntax does not fall through, eliminating a major source of bugs"
      - "Arrow syntax is available in all versions of Java"
    correctIndex: 2
    feedback: "Traditional switch without `break` would 'fall through' — execution continues into the next case. This was a frequent source of bugs. Arrow syntax (Java 14+) never falls through: each arrow case is independent. You get the match, it runs, and execution exits the switch."

retrieval:
  recall: "Write a Java switch expression (arrow syntax) that maps an int `month` to a String season: 12, 1, 2 → Winter; 3, 4, 5 → Spring; default → Other."
  explain: "Explain what 'fall-through' means in a traditional switch statement and why arrow syntax eliminates this problem."
  mistakeId:
    code: |
      String command = "start";
      switch (command) {
          case "start":
              System.out.println("Starting...");
          case "stop":
              System.out.println("Stopping...");
          default:
              System.out.println("Unknown command");
      }
    answer: "This is traditional switch syntax without `break` statements. When `command` is 'start', it matches the first case and prints 'Starting...' — but then falls through into the 'stop' case and prints 'Stopping...' and then falls through again to print 'Unknown command'. All three lines print. The fix is to add `break;` after each case, or switch to arrow syntax: `case \"start\" -> System.out.println(\"Starting...\");`"
---

# Hook

Imagine you are building a menu system: the user types 1 for New Game, 2 for Load Game, 3 for Settings, 4 for Quit. You could write four `else if` statements, each checking `option == 1`, `option == 2`, and so on. It works — but it is repetitive. You write `option` four times. You write `==` four times. There is a cleaner tool for exactly this pattern: a single variable, many possible values, each mapped to a specific action. That tool is the `switch`.

# Lore Introduction

"Some decisions are not about greater or less," Archmage Veylan says, unrolling a scroll covered in symbols. "They are about identity. This rune or that rune. This element or that element." He holds up a crystal prism that splits light into distinct beams. "The binding chain you learned compares one thing to another using conditions. But the selector rune — the `switch` — simply asks: which beam is this? And routes accordingly." In Arcane Academy, selectors are used for spell libraries, element mappings, and command routing — anywhere a single value must map to one of many distinct outcomes.

# Core Learning

## Concept Introduction

A **switch expression** (modern arrow syntax, Java 14+) routes execution to one of several branches based on the exact value of a selector:

```java
switch (selector) {
    case value1 -> statement1;
    case value2 -> statement2;
    case value3, value4 -> statement3; // multiple values, one branch
    default -> defaultStatement;
}
```

**Key properties:**
- The selector is a variable or expression (int, String, char, enum, etc.)
- Each `case` matches an exact value
- Only the matching case runs — no fall-through
- `default` is the catch-all for unmatched values

**Multiple values in one case** (comma-separated):
```java
case "SATURDAY", "SUNDAY" -> System.out.println("Weekend");
```

## Why It Matters

When you are dispatching on the exact value of a single variable, `switch` is cleaner and more intention-revealing than `else if`. The reader immediately sees: "this code maps one variable to many outcomes." Switch also enables compiler optimisation — for some types, the JVM can jump directly to the matching case rather than checking conditions one by one.

## Worked Examples

**Example 1 — Day name mapping:**
```java
String day = "WEDNESDAY";
switch (day) {
    case "MONDAY"    -> System.out.println("Start of work week");
    case "FRIDAY"    -> System.out.println("End of work week");
    case "SATURDAY",
         "SUNDAY"   -> System.out.println("Weekend!");
    default          -> System.out.println("Midweek");
}
// Prints: Midweek
```

**Example 2 — HTTP status dispatcher:**
```java
int statusCode = 200;
switch (statusCode) {
    case 200 -> System.out.println("OK");
    case 301 -> System.out.println("Moved Permanently");
    case 404 -> System.out.println("Not Found");
    case 500 -> System.out.println("Internal Server Error");
    default  -> System.out.println("Unknown status: " + statusCode);
}
// Prints: OK
```

**Example 3 — Switch expression returning a value:**
```java
int day = 3;
String name = switch (day) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    case 3 -> "Wednesday";
    default -> "Other";
};
System.out.println(name); // Prints: Wednesday
```

## Common Mistakes

- **Using traditional syntax without `break`:** Without `break`, execution falls through to the next case. Prefer arrow syntax to avoid this entirely.
- **Using switch for range-based conditions:** Switch only matches exact values. `case score >= 80` is not valid Java. Use `else if` for ranges.
- **Forgetting `default`:** Without `default`, unmatched values silently do nothing, which can be hard to debug.
- **Using a type not supported as selector:** Objects (other than String and enum) cannot be used as switch selectors in most Java versions.
- **Putting a statement after the arrow where a block is needed:** For multiple statements in a case, use `{ }`: `case "x" -> { doA(); doB(); }`.

## Mental Model

Think of switch as a **hotel key card system**. You have one key (the selector value). The hotel has many doors (the cases). You insert the key and exactly one door opens — the one whose lock matches your key. All other doors stay locked. If your key is not programmed for any room (no matching case), you are directed to the front desk (the `default` case).

## Mini Summary

- Switch routes to one of many branches based on the exact value of a selector.
- Arrow syntax (`case x ->`) is the modern Java 14+ form and does not fall through.
- Multiple values can share a case: `case 1, 2, 3 -> ...`
- `default` handles all unmatched values — always include it.
- Use switch when comparing one variable to a set of exact values.
- Use `else if` when conditions involve ranges, multiple variables, or complex logic.

# Guided Practice Quest

*"The Academy's spell library has five spell categories," Archmage Veylan explains. "Given a category code — 'FIRE', 'ICE', 'LIGHT', 'DARK', or anything else — your binding selector must print the appropriate spell family." Write the switch expression to handle all five cases, using the default for unknown categories.*

# Solo Practice Quest

**Command Interpreter**

Write a Java switch expression that handles user commands for a simple text adventure:

- `"go"` → print "You move forward."
- `"take"` → print "You pick up the item."
- `"look"` → print "You examine your surroundings."
- `"help"` → print "Available commands: go, take, look, help"
- Anything else → print "Unknown command."

Test your code mentally with the inputs `"take"` and `"run"`. Write down what each prints and why.

# Integration

**Mathematics connection:** In formal mathematics, a **function** maps each input to exactly one output: f(x) = y. A switch expression is a direct implementation of a function defined by a lookup table. When every possible selector value has a corresponding case, the switch is a total function. When some values fall to `default`, the `default` is the function's value for all unmapped inputs. Many mathematical operations (e.g., a musical scale, a lookup table of physical constants) are naturally expressed as switch-like mappings.

**Psychology connection:** Cognitive load theory in psychology suggests that humans have a limited capacity for processing competing decisions simultaneously. An `else if` chain with ten conditions forces the reader to evaluate multiple boolean expressions mentally. A switch with ten cases has lower cognitive load: the reader knows immediately that the variable has one value and the question is simply "which one?" This is why experienced developers instinctively reach for switch when the pattern matches — it reduces the mental work of understanding the code.

*Free question: Java 21 introduced "pattern matching for switch" — the ability to use `instanceof` patterns and record patterns in switch cases. Can you guess what this might look like and what problem it would solve?*

# Lore Conclusion

The selector rune pulses with a single colour as the crystal's beam is identified — not by comparison, but by recognition. Archmage Veylan nods. "This is the switch's art: not asking 'is it greater?' or 'is it less?' but simply 'what is it?' And routing accordingly." He rolls up the scroll. "The chain and the selector are the two faces of conditional magic. Master both, and you can map any decision space. Next, you will learn what happens when decisions nest inside one another — and how to keep that from becoming chaos."
