---
id: se-jun-m3-02
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m3
moduleTitle: "Module 3: Exception Handling"
moduleGlyph: "⚠️"
moduleSortOrder: 3
topicSlug: try_catch
topicTitle: "Try/Catch/Finally"
topicSortOrder: 2
lesson: try_catch_finally
title: "Try/Catch/Finally"
sortOrder: 2
difficulty: 2
estimatedMinutes: 30
xpReward: 70
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m3-01]
integrationDomains: [custom_exceptions, error_strategies]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Uses try/catch with a specific exception type rather than the generic Exception"
    - "Demonstrates multiple catch blocks ordered from specific to general"
    - "Correctly uses finally for cleanup code that must always run"
    - "Explains that finally runs even when an exception is thrown and not caught"
    - "Uses try-with-resources to automatically close a resource"
  keywords: [try, catch, finally, multiple catch, specific exception, try-with-resources, AutoCloseable, cleanup, propagate, re-throw]
  modelAnswer: |
    // Multiple catch blocks — specific before general
    try {
        int result = Integer.parseInt(input);
        int[] arr = new int[result];
        System.out.println(arr[result + 1]);
    } catch (NumberFormatException e) {
        System.err.println("Invalid number: " + e.getMessage());
    } catch (NegativeArraySizeException e) {
        System.err.println("Size must be positive: " + e.getMessage());
    } catch (ArrayIndexOutOfBoundsException e) {
        System.err.println("Index out of range: " + e.getMessage());
    } finally {
        System.out.println("Cleanup always runs");
    }

    // Try-with-resources
    try (BufferedReader reader = new BufferedReader(new FileReader("spell.txt"))) {
        return reader.readLine();
    } catch (IOException e) {
        throw new RuntimeException("Failed to read spell file", e);
    }
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Write a method `parseAndDivide(String numStr, int divisor)` that parses numStr to an int, divides by divisor, and returns the result. Catch NumberFormatException and ArithmeticException separately, printing a descriptive message for each. Add a finally block that prints 'Operation complete'."
    inputConfig:
      language: java
      starterCode: |
        public static int parseAndDivide(String numStr, int divisor) {
            try {
                // parse numStr, divide by divisor, return result
            } catch (/* NumberFormatException */) {
                System.err.println("/* descriptive message */");
            } catch (/* ArithmeticException */) {
                System.err.println("/* descriptive message */");
            } finally {
                // always runs
            }
            return 0; // fallback
        }
    markingRule: "try block contains Integer.parseInt and division, NumberFormatException caught with message about invalid number, ArithmeticException caught with message about division by zero, finally block prints completion message"
    hint: "Integer.parseInt(numStr) can throw NumberFormatException. Division by zero throws ArithmeticException for integers."
    reflectionPrompt: "In what order do the catch blocks execute? What happens if NumberFormatException is placed after a general Exception catch?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Demonstrate that finally always runs by writing a method that throws an exception inside try. Show that finally executes before the exception propagates to the caller."
    inputConfig:
      language: java
      starterCode: |
        public static void demonstrateFinally() {
            try {
                System.out.println("In try");
                throw new RuntimeException("Intentional exception");
            } catch (IllegalArgumentException e) {
                // this won't catch RuntimeException!
                System.out.println("In catch");
            } finally {
                System.out.println("In finally — always runs");
            }
        }
        // Call it and observe: what prints? Does the exception propagate?
    markingRule: "Correctly identifies that 'In try' and 'In finally' print, the catch does NOT run (wrong type), and the exception propagates after finally"
    hint: "The catch block only catches IllegalArgumentException. Since RuntimeException is not a subclass of IllegalArgumentException, it is not caught here. But finally still runs before the exception propagates."
    reflectionPrompt: "Why is it important that finally always runs, even when an exception is not caught? Give a real-world example."
  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "Rewrite a file-reading method using try-with-resources instead of a manual finally block. The resource Scanner implements AutoCloseable."
    inputConfig:
      language: java
      starterCode: |
        import java.util.*;
        import java.io.*;

        // Rewrite using try-with-resources:
        public static String readFirstLine(String filename) throws IOException {
            // try (Scanner scanner = ...) { ... }
            return null;
        }
    markingRule: "Uses try-with-resources syntax with Scanner or BufferedReader, resource declared in try parentheses, catch IOException, scanner/reader is automatically closed, no explicit finally needed"
    hint: "try (Scanner sc = new Scanner(new File(filename))) { return sc.nextLine(); } — the Scanner is automatically closed after the try block."
    reflectionPrompt: "What problem does try-with-resources solve compared to a manual finally block with close()?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In a try/catch/finally block, when does finally NOT execute?"
    options:
      - "When the catch block re-throws an exception"
      - "When the try block throws an unchecked exception"
      - "When System.exit() is called inside the try block"
      - "When the exception type does not match the catch clause"
    correctIndex: 2
    feedback: "Finally always executes — except when System.exit() is called (JVM terminates immediately) or the JVM is killed externally. Re-throwing, unchecked exceptions, and unmatched catches all still trigger finally. This guarantee is what makes finally safe for cleanup code."
  - type: MULTIPLE_CHOICE
    question: "Multiple catch blocks are ordered: general Exception first, then specific NumberFormatException. What happens?"
    options:
      - "Both blocks can execute if multiple exceptions are thrown"
      - "The code compiles and runs; the specific block handles NumberFormatException"
      - "The code does not compile — specific exceptions must come before general ones"
      - "The specific block is ignored; only the general block runs"
    correctIndex: 2
    feedback: "The compiler enforces that catch blocks go from specific to general. If Exception appears before NumberFormatException, the compiler reports an error because NumberFormatException is a subclass of Exception and can never be reached — it would always be caught by the general block first."
retrieval:
  recall: "Describe the execution order for try/catch/finally when (a) no exception is thrown, and (b) an exception is thrown and caught."
  explain: "Explain what try-with-resources does and why it is preferred over a manual finally block with close()."
  mistakeId:
    code: |
      try {
          String result = riskyOperation();
          System.out.println(result.length());
      } catch (Exception e) {
          // swallow all exceptions silently
      }
    answer: "Catching Exception and silently ignoring it hides all errors — including bugs like NullPointerException that should be fixed, not swallowed. Fix: (1) catch specific exception types, (2) log the error with context, (3) either handle it properly or re-throw: `throw new RuntimeException(\"riskyOperation failed\", e);`"
---

# Hook

Throwing an exception is only half the story. The other half is catching it, handling it gracefully, and ensuring cleanup code always runs. `try/catch/finally` is the mechanism Java provides to separate the happy path from the error path and guarantee that resources are closed and state is cleaned up — regardless of what goes wrong. Done right, it turns a crash into a controlled response. Done wrong (catching everything and saying nothing), it turns real bugs invisible.

# Lore Introduction

The Academy's portal activation system had a problem: when a portal failed to open, the mana conduits remained energised, burning through reserves until someone noticed. The engineer had the right idea — wrap the activation in error handling — but made a classic mistake: a bare catch block with no logging and no cleanup. Portals failed silently, mana drained, and nobody knew why. After adding proper catch blocks with messages, a finally block that deactivated the conduits regardless of success or failure, and try-with-resources for the mana channel, the system became reliable. Errors were visible. Resources were always released.

# Core Learning

## Concept Introduction

**Basic structure:**
```java
try {
    // code that might throw
} catch (SpecificException e) {
    // handle this specific failure
} finally {
    // always runs: cleanup, logging, resource release
}
```

**Execution rules:**
- If no exception: try runs, finally runs, catch skipped
- If exception caught: try stops at throw, matching catch runs, finally runs
- If exception not caught: try stops at throw, no catch runs, finally runs, exception propagates

**Multiple catch blocks — order matters:**
```java
try {
    // ...
} catch (NumberFormatException e) {  // specific first
    // ...
} catch (RuntimeException e) {       // more general second
    // ...
} catch (Exception e) {              // most general last
    // ...
}
```
Catch blocks must go from most specific to most general — the compiler enforces this.

**Multi-catch (Java 7+):**
```java
} catch (NumberFormatException | IllegalArgumentException e) {
    System.err.println("Invalid input: " + e.getMessage());
}
```

**try-with-resources (Java 7+):**
```java
try (ResourceType r = new ResourceType()) {
    // use r
} catch (Exception e) {
    // handle
}
// r.close() called automatically — even if exception thrown
```
The resource must implement `AutoCloseable` (or `Closeable`).

## Why It Matters

Proper exception handling is what separates a program that crashes from one that fails gracefully. `finally` guarantees cleanup (closing connections, releasing locks, deactivating resources) regardless of whether success or failure occurred. Without it, resource leaks are common. try-with-resources eliminates an entire class of resource-leak bugs by making cleanup automatic. Catching specific exceptions (not bare `Exception`) ensures that programming bugs still surface rather than being silently swallowed.

## Worked Examples

**Example 1 — Multiple catch blocks, specific to general**

```java
public static int safeDivide(String numStr, int divisor) {
    try {
        int number = Integer.parseInt(numStr); // may throw NumberFormatException
        return number / divisor;               // may throw ArithmeticException
    } catch (NumberFormatException e) {
        System.err.println("Not a valid number: " + numStr);
        return 0;
    } catch (ArithmeticException e) {
        System.err.println("Cannot divide by zero");
        return 0;
    } finally {
        System.out.println("safeDivide complete"); // always prints
    }
}
```

**Example 2 — Finally for guaranteed cleanup**

```java
Connection conn = null;
try {
    conn = dataSource.getConnection();
    // perform database operations
    conn.commit();
} catch (SQLException e) {
    if (conn != null) conn.rollback();
    throw new RuntimeException("Database operation failed", e);
} finally {
    if (conn != null) conn.close(); // always closes the connection
}
```

**Example 3 — try-with-resources**

```java
public static List<String> readLines(String filename) throws IOException {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
    }
    // reader.close() called automatically here
    return lines;
}
```

## Common Mistakes

- **Catching `Exception` and doing nothing.** Silent catch blocks hide bugs. Always log, re-throw, or handle meaningfully.
- **Placing general catch before specific.** `catch (Exception e)` before `catch (NumberFormatException e)` is a compile error because `NumberFormatException` can never be reached.
- **Returning from inside finally.** A `return` in a `finally` block swallows any exception that was propagating — the exception disappears silently.
- **Forgetting that finally runs before propagation.** Even when an exception is not caught, finally executes before the exception moves up the stack.
- **Using finally instead of try-with-resources for resources.** Manual finally close() can throw its own exception, masking the original. try-with-resources handles this with suppressed exceptions.

## Mental Model

Think of `try` as a controlled experiment. You attempt something that might fail. `catch` is your contingency plan for specific failure modes. `finally` is the lab cleanup checklist that runs regardless of whether the experiment succeeded or exploded. The important rule: even if the experiment explodes and nobody has a contingency for this exact type of failure, the cleanup checklist still runs before the fire alarm sounds and the explosion propagates to the floor above.

## Mini Summary

- `try` wraps code that might throw; execution stops at the first exception.
- `catch` handles specific exception types — order specific to general.
- `finally` always runs: perfect for cleanup, resource release, and logging.
- `System.exit()` is the only common case where finally does not run.
- Try-with-resources automatically calls `close()` on `AutoCloseable` resources.
- Never catch `Exception` and swallow it silently — log or re-throw.

# Guided Practice Quest

Complete the three steps: write a `parseAndDivide` method with multiple specific catch blocks and a finally, demonstrate that finally runs even when an exception propagates past the catch, then rewrite a file-reading method using try-with-resources.

# Solo Practice Quest

Build a `FileProcessor` class with method `processFile(String filename)` that: reads the filename using try-with-resources with a `BufferedReader`, catches `FileNotFoundException` separately from `IOException` with different messages, uses finally to log "Processing complete for: [filename]" regardless of outcome, and returns the file content as a `List<String>` or an empty list on failure. Also add a method `parseNumbers(String[] inputs)` that parses each string to an Integer, catches `NumberFormatException` per element (logging the bad value and continuing), and returns a `List<Integer>` of valid results.

# Integration

Try/catch/finally is foundational across the entire stack. In **Custom Exceptions** you will throw your own exception types that callers then catch here. In **ORMs**, Spring Data JPA wraps JDBC exceptions in Spring's exception hierarchy, which you catch in service methods. In **APIs**, Spring's `@ExceptionHandler` and `@ControllerAdvice` effectively act as a global catch block — you will understand exactly how they work because you understand exception propagation. In **Testing**, `assertThrows()` in JUnit 5 verifies that the right exception type is thrown from the right method.

**Integration question:** A service method opens a database connection, performs a query, and must close the connection regardless of success or failure. Write the skeleton (comments only) showing which code goes in try, catch, and finally. Then explain how try-with-resources would simplify this.

# Lore Conclusion

The portal activation system now fails loudly and cleans up completely. When a portal cannot open, the error message names exactly which component failed and why. The mana conduits are always deactivated — by the finally block that the original engineer forgot. Engineers monitoring the system no longer see mysterious mana drain. They see exactly what failed, which portal it was, and that the system recovered cleanly. Graceful failure is not an accident. It is designed into the code, one try/catch/finally at a time.
