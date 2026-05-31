---
id: se-jun-m3-01
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m3
moduleTitle: "Module 3: Exception Handling"
moduleGlyph: "⚠️"
moduleSortOrder: 3
topicSlug: exceptions
topicTitle: "Exceptions"
topicSortOrder: 1
lesson: exceptions
title: "Exceptions"
sortOrder: 1
difficulty: 2
estimatedMinutes: 25
xpReward: 60
practiceType: JAVA
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m2-06]
integrationDomains: [try_catch, error_strategies]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Draws or describes the Throwable → Exception → RuntimeException hierarchy"
    - "Distinguishes checked exceptions (must be declared or caught) from unchecked (RuntimeException subclasses)"
    - "Names at least three common built-in exceptions and describes when each is thrown"
    - "Explains why unchecked exceptions are used for programming errors, not recoverable conditions"
    - "Identifies whether a given exception is checked or unchecked from its class hierarchy"
  keywords: [Throwable, Exception, RuntimeException, checked, unchecked, NullPointerException, IllegalArgumentException, IOException, StackOverflowError, hierarchy, throw, throws]
  modelAnswer: |
    // Hierarchy:
    // Throwable
    //   ├── Error (JVM errors, do not catch)
    //   └── Exception
    //       ├── IOException (checked)
    //       ├── SQLException (checked)
    //       └── RuntimeException (unchecked)
    //           ├── NullPointerException
    //           ├── IllegalArgumentException
    //           ├── IndexOutOfBoundsException
    //           └── IllegalStateException

    // Checked - must declare or catch:
    public void readFile(String path) throws IOException { ... }

    // Unchecked - no declaration needed:
    throw new IllegalArgumentException("Name cannot be null");
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Write a method `divide(int a, int b)` that throws an `IllegalArgumentException` with a descriptive message when b is zero, otherwise returns the result."
    inputConfig:
      language: java
      starterCode: |
        public static int divide(int a, int b) {
            // throw IllegalArgumentException if b == 0
            return a / b;
        }
    markingRule: "Checks b == 0 before division, throws new IllegalArgumentException with a meaningful message, normal case returns a/b"
    hint: "throw new IllegalArgumentException(\"Divisor cannot be zero\"); — use a clear message describing what went wrong."
    reflectionPrompt: "Why is IllegalArgumentException preferred over returning -1 or 0 to indicate an error?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Write a method `getElement(List<String> list, int index)` that throws `IllegalArgumentException` if the list is null, `IllegalStateException` if the list is empty, and `IndexOutOfBoundsException` if the index is invalid."
    inputConfig:
      language: java
      starterCode: |
        import java.util.*;
        public static String getElement(List<String> list, int index) {
            // validate: null list, empty list, invalid index
            return list.get(index);
        }
    markingRule: "Checks list == null with IllegalArgumentException, list.isEmpty() with IllegalStateException, index < 0 || index >= list.size() with IndexOutOfBoundsException, then returns element"
    hint: "Check preconditions in order: null first, then empty, then index bounds. Use descriptive messages."
    reflectionPrompt: "Is IndexOutOfBoundsException checked or unchecked? How does that affect the method signature?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of these is a checked exception?"
    options:
      - "NullPointerException"
      - "IllegalArgumentException"
      - "IOException"
      - "ArrayIndexOutOfBoundsException"
    correctIndex: 2
    feedback: "IOException is a checked exception — the compiler forces you to either catch it or declare it with 'throws'. NullPointerException, IllegalArgumentException, and ArrayIndexOutOfBoundsException are all subclasses of RuntimeException (unchecked) — no forced handling."
  - type: MULTIPLE_CHOICE
    question: "What is the parent class of all exceptions and errors in Java?"
    options:
      - "Exception"
      - "RuntimeException"
      - "Throwable"
      - "Error"
    correctIndex: 2
    feedback: "Throwable is the root of the Java exception hierarchy. It has two direct subclasses: Error (for serious JVM problems like OutOfMemoryError — do not catch these) and Exception (for conditions that programs should handle). RuntimeException is a subclass of Exception."
retrieval:
  recall: "What is the difference between a checked exception and an unchecked exception in Java?"
  explain: "Explain where NullPointerException, IOException, and IllegalArgumentException sit in the exception hierarchy and whether each is checked or unchecked."
  mistakeId:
    code: |
      public void process(String value) {
          if (value.length() > 10) {
              System.out.println("Too long");
          }
      }
    answer: "If `value` is null, `value.length()` throws a NullPointerException. Fix: add a null check before accessing the value — `if (value == null) throw new IllegalArgumentException(\"value must not be null\");` — or use `Objects.requireNonNull(value, \"value must not be null\");` at the start of the method."
---

# Hook

Something will always go wrong: null values appear, users pass bad input, files do not exist, network connections drop. Java's exception system is the language's way of making these failure modes explicit, structured, and impossible to accidentally ignore. Understanding the hierarchy — what is checked, what is not, and why — is the foundation for writing code that fails gracefully rather than crashing silently or producing wrong output.

# Lore Introduction

The Academy's spell casting system processed thousands of incantations per day without incident — until a wizard passed an empty scroll to the casting engine. The system returned a cryptic error: `NullPointerException at line 47`. Nobody knew what the scroll contained or why it failed. A senior engineer refactored the entry point to throw `IllegalArgumentException("Scroll must not be null")` on the spot. The next time a wizard passed an empty scroll, the error message was immediate, clear, and actionable. Clear exceptions are the difference between a one-minute fix and a two-hour investigation.

# Core Learning

## Concept Introduction

**The Exception Hierarchy:**
```
Throwable
├── Error          — JVM-level failures (OutOfMemoryError, StackOverflowError)
│                   Do NOT catch these; they indicate unrecoverable states
└── Exception
    ├── IOException         — checked
    ├── SQLException        — checked
    ├── ParseException      — checked
    └── RuntimeException    — unchecked (no forced handling)
        ├── NullPointerException
        ├── IllegalArgumentException
        ├── IllegalStateException
        ├── IndexOutOfBoundsException
        └── UnsupportedOperationException
```

**Checked exceptions:**
- Subclasses of `Exception` but NOT `RuntimeException`
- The compiler forces you to handle them: either catch or declare with `throws`
- Represent recoverable external conditions (file not found, network error)
- Examples: `IOException`, `SQLException`, `ParseException`

**Unchecked exceptions (RuntimeException):**
- The compiler does NOT force handling
- Represent programming errors or precondition violations
- Examples: `NullPointerException`, `IllegalArgumentException`, `ArrayIndexOutOfBoundsException`
- Prefer unchecked for invalid programmer input; checked for external resource failures

**Common built-in exceptions:**
| Exception | Cause |
|---|---|
| `NullPointerException` | Calling a method on a null reference |
| `IllegalArgumentException` | Method receives an invalid argument |
| `IllegalStateException` | Object is in invalid state for the operation |
| `IndexOutOfBoundsException` | Array/list index outside valid range |
| `ClassCastException` | Invalid cast between types |
| `NumberFormatException` | String cannot be parsed as a number |
| `UnsupportedOperationException` | Operation not supported (e.g., unmodifiable list) |

## Why It Matters

Without exceptions, a method that fails must signal failure by returning a special value (−1, null, false). The caller can easily forget to check. Exceptions make failure impossible to ignore — they propagate up the call stack until caught. Knowing the hierarchy tells you which exceptions require explicit handling in the method signature and which do not. Using the right exception type (not just `new Exception("something failed")`) gives callers the information they need to respond appropriately.

## Worked Examples

**Example 1 — Throwing an unchecked exception for invalid input**

```java
public class AgeValidator {
    public static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException(
                "Age cannot be negative, got: " + age);
        }
        if (age > 150) {
            throw new IllegalArgumentException(
                "Age is unrealistically large: " + age);
        }
    }
}
```

**Example 2 — Checked exception in a method signature**

```java
import java.io.*;

public class SpellLoader {
    // IOException is checked: caller must handle or propagate
    public String loadSpell(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        return reader.readLine();
    }
}
```

**Example 3 — Common exceptions and their causes**

```java
String name = null;
name.length(); // NullPointerException

List<String> list = List.of("a", "b");
list.get(5);   // IndexOutOfBoundsException

Integer.parseInt("abc"); // NumberFormatException

List<String> immutable = List.of("x");
immutable.add("y"); // UnsupportedOperationException
```

## Common Mistakes

- **Catching `Exception` instead of a specific type.** Catching the root `Exception` hides programming bugs (like NullPointerException) that should crash the program and be fixed, not silently swallowed.
- **Using null returns to signal failure instead of throwing.** Callers forget to null-check. Throw `IllegalArgumentException` or `IllegalStateException` to make failure explicit.
- **Throwing `Exception` directly.** `throw new Exception("failed")` forces all callers to handle a checked exception. Throw a specific subclass that describes the failure.
- **Catching `Error`.** Errors like `OutOfMemoryError` indicate the JVM is in a state where your code cannot safely continue. Do not catch them.
- **Confusing the hierarchy.** `RuntimeException` IS-A `Exception` IS-A `Throwable`. A catch for `Exception` will also catch `RuntimeException` — which is usually not intended.

## Mental Model

Think of exceptions as emergency flares. When a method encounters a problem it cannot resolve, it fires a flare (throws). The flare travels up the call stack until someone is equipped to handle it (catches). If nobody catches it, the JVM prints the stack trace and terminates. Checked exceptions are flares that every person in the chain must acknowledge — they are designed for conditions where callers might realistically handle them (retry, use default, log). Unchecked exceptions are programmer mistakes — they should be fixed in the code, not caught at runtime.

## Mini Summary

- `Throwable` → `Exception` → `RuntimeException` is the core hierarchy.
- Checked exceptions (e.g., `IOException`) must be caught or declared with `throws`.
- Unchecked exceptions (e.g., `NullPointerException`) need no forced handling.
- `Error` (e.g., `OutOfMemoryError`) represents JVM failures — do not catch.
- Use specific exception types: `IllegalArgumentException` for bad input, `IllegalStateException` for invalid state.
- Clear exception messages with context make debugging minutes rather than hours.

# Guided Practice Quest

Complete the two steps: implement a `divide()` method that throws `IllegalArgumentException` on zero divisor, then implement a `getElement()` method with layered validation throwing three different exception types.

# Solo Practice Quest

Build a `UserValidator` class. Implement `validateUsername(String username)` that throws `IllegalArgumentException` if null or empty, `IllegalArgumentException` if longer than 20 characters, and `IllegalArgumentException` if it contains spaces. Implement `validateAge(int age)` throwing `IllegalArgumentException` for values outside 0-120. Implement `validateEmail(String email)` throwing `IllegalArgumentException` if null or missing the `@` character. All messages must be descriptive and include the invalid value. Write a `main` method that tests each validation with both valid and invalid input.

# Integration

Exceptions underpin the entire error-handling story. In the **Try/Catch/Finally** lesson you will learn to catch and recover from exceptions. In **Custom Exceptions** you will extend `RuntimeException` to create domain-specific exception types. In **Error Strategies** you will explore when to throw, when to return `Optional`, and when to fail fast. In **APIs**, Spring's `@ExceptionHandler` catches exceptions thrown from service methods and converts them to HTTP responses — so the exception type you throw in a service determines the HTTP status code a client receives.

**Integration question:** A service method calls a database repository. The repository throws a `DataAccessException` (a `RuntimeException`). Should the service catch it and return null, catch it and throw a domain exception, or let it propagate? What are the trade-offs?

# Lore Conclusion

The spell casting system now fails loudly and clearly. Every invalid input produces a named exception with a message that tells the operator exactly what was wrong and where. The two-hour debugging sessions are gone. Clear exceptions, thrown at the right level, are not just defensive programming — they are documentation that runs. The difference between `NullPointerException at line 47` and `IllegalArgumentException: Scroll must not be null` is the difference between a mystery and a solution.
