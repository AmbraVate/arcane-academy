---
id: se-app-m6-02
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m6
moduleTitle: "Module 6: Debugging and Engineering Habits"
moduleGlyph: "🔬"
moduleSortOrder: 6
topicSlug: errors
topicTitle: "Errors"
topicSortOrder: 1
lesson: runtime_errors
title: "Runtime Errors"
sortOrder: 2
difficulty: 2
estimatedMinutes: 22
xpReward: 40
practiceType: JAVA
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m6-01]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly defines a runtime error as an error that occurs while the program is running"
    - "Names and explains at least three common Java runtime exceptions"
    - "Explains what a stack trace is and how to read it"
    - "Identifies the key information in a stack trace (exception type, message, line number)"
    - "Distinguishes runtime errors from syntax errors"
  keywords: [runtime, exception, NullPointerException, ArrayIndexOutOfBoundsException, ArithmeticException, stack trace, crash, line number]
  modelAnswer: |
    A runtime error occurs after the program successfully compiles and starts running.
    Common examples: NullPointerException (calling a method on null), 
    ArrayIndexOutOfBoundsException (accessing index beyond array size),
    ArithmeticException (dividing by zero).
    A stack trace shows the exception type, its message, and the chain of method calls
    with file names and line numbers. Read from top to bottom: the top line is the exception,
    below it is the method where it happened, then the caller, etc.

guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You compile a program successfully. While running it, it crashes with an error. What type of error is this?"
    inputConfig:
      options:
        - "Syntax error"
        - "Logical error"
        - "Runtime error"
        - "Compilation error"
      correctIndex: 2
    markingRule: EXACT_MATCH
    hint: "The compiler found nothing wrong — the error only appeared when the program was actually executed."
    reflectionPrompt: "Why can't the compiler always detect these kinds of errors before running?"

  - id: step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "What causes a NullPointerException?"
    inputConfig:
      options:
        - "Using a number that is too large"
        - "Accessing an array index that does not exist"
        - "Calling a method or accessing a field on a null reference"
        - "Dividing an integer by zero"
      correctIndex: 2
    markingRule: EXACT_MATCH
    hint: "null means 'no object'. What happens if you try to call a method on something that is not there?"
    reflectionPrompt: "How can using a constructor (rather than leaving fields null) help prevent this error?"

  - id: step-3
    sortOrder: 3
    inputType: SHORT_ANSWER
    instruction: "Given this stack trace, state: (1) the exception type, (2) the message, (3) which line caused it.\n\nException in thread 'main' java.lang.ArithmeticException: / by zero\n\tat Calculator.divide(Calculator.java:12)\n\tat Main.main(Main.java:5)"
    inputConfig:
      placeholder: "Exception type: ...\nMessage: ...\nLine: ..."
    markingRule: KEYWORD_MATCH
    hint: "The first line is always: exception type: message. The 'at' lines are the stack trace."
    reflectionPrompt: "Why does the stack trace show Calculator.java:12 AND Main.java:5?"

microCheckpoint:
  - question: "Which exception is thrown when you try to access index 10 of an array that only has 5 elements?"
    options:
      - "NullPointerException"
      - "ArithmeticException"
      - "IllegalArgumentException"
      - "ArrayIndexOutOfBoundsException"
    correctIndex: 3
    feedback: "Correct — ArrayIndexOutOfBoundsException is thrown when you access an array with an index that is outside its valid range."

  - question: "What is a stack trace?"
    options:
      - "A list of all variables in the program"
      - "A record of the chain of method calls that led to the exception, with file names and line numbers"
      - "A summary of syntax errors in the code"
      - "The output of System.out.println statements"
    correctIndex: 1
    feedback: "Yes — a stack trace shows you the chain of method calls that were active when the exception occurred, helping you trace back to the root cause."

retrieval:
  recall: "Name three common Java runtime exceptions and describe what causes each one."
  explain: "Explain how to read a stack trace to find the line of code that caused a NullPointerException."
  mistakeId:
    code: |
      String name = null;
      System.out.println(name.length());
    answer: "name is null — it does not point to a String object. Calling .length() on null causes a NullPointerException. Fix: initialise name before using it, e.g., String name = \"Alice\";"
---

# Hook

Your code compiled perfectly. You run it. It crashes. A wall of red text appears — different from the compiler's neat messages. This is a **runtime error**: a corruption that hid from the compiler but erupts the moment the spell meets live data. The program was grammatically perfect; it just tried to do something impossible, like dividing by zero or reaching into empty space. Runtime errors are trickier than syntax errors, but they always leave a trail — and that trail is the stack trace.

# Lore Introduction

The Verification Reader clears a Blueprint for use, but it cannot foresee every possible combination of essence a construct might encounter in the field. Some corruptions only manifest at the moment of execution — when a construct reaches for an essence fragment that was never summoned, or attempts an operation that violates the laws of arcane mathematics. These are Runtime Corruptions. Unlike the Verification Reader's clean reports, Runtime Corruptions unfold in a cascade of invocation records: the stack trace. Every Academy adept must learn to read this cascade before they can call themselves a true debugger.

# Core Learning

## Concept Introduction

A **runtime error** (also called an **exception**) occurs after the program has compiled successfully and started running. The compiler saw nothing wrong — the problem only appears when specific code executes with specific data.

**Three common runtime exceptions:**

| Exception | Cause |
|-----------|-------|
| `NullPointerException` | Calling a method or accessing a field on a `null` reference |
| `ArrayIndexOutOfBoundsException` | Accessing an array with an index that is out of range |
| `ArithmeticException` | Illegal arithmetic, most commonly dividing an `int` by zero |

## Why It Matters

Runtime errors crash programs in production — in front of real users. Learning to identify them quickly from their error messages and stack traces is one of the most important debugging skills you will build.

## Worked Examples

**NullPointerException:**
```java
String name = null;           // name does not point to any String
System.out.println(name.length()); // CRASH: NullPointerException
// Fix: initialise name before use
String name = "Alice";
```

**ArrayIndexOutOfBoundsException:**
```java
int[] numbers = {10, 20, 30};  // valid indices: 0, 1, 2
System.out.println(numbers[5]); // CRASH: index 5 does not exist
// Fix: use numbers[2] or check length before accessing
```

**ArithmeticException:**
```java
int a = 10;
int b = 0;
System.out.println(a / b);  // CRASH: cannot divide by zero
// Fix: check that b != 0 before dividing
```

**Reading a stack trace:**
```
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "String.length()" because "name" is null
    at Main.printLength(Main.java:8)
    at Main.main(Main.java:3)
```

Reading this:
1. `NullPointerException` — the type of exception.
2. `Cannot invoke "String.length()" because "name" is null` — the specific message.
3. `Main.java:8` — the file and line where the crash happened (`printLength` method).
4. `Main.java:3` — the caller that triggered it (`main` method).

Go to `Main.java` line 8 first — that is where the crash is.

## Common Mistakes

- **Reading stack traces bottom-up**: The most specific (closest to the crash) information is at the *top*. Start at the top.
- **Panicking at long stack traces**: Library code often adds many lines. Find the first line that references *your* code (your package/class name) — that is usually the culprit.
- **Ignoring the exception message**: The message after the exception type is often a clear description of exactly what went wrong — read it before looking at code.

## Mental Model

Think of a stack trace as a **chain of phone calls**. `main()` called `calculateTotal()`, which called `applyDiscount()`, which crashed. The stack trace shows the entire chain, most-recent call first. You do not need to understand the whole chain — just follow it to the first link that is *your* code, and that is where the problem lives.

## Mini Summary

- ✔ Runtime errors compile fine but crash when executed with specific data.
- ✔ `NullPointerException` — method called on null; `ArrayIndexOutOfBoundsException` — index too large; `ArithmeticException` — divide by zero.
- ✔ A stack trace shows exception type, message, and chain of method calls with line numbers.
- ✔ Read the stack trace from the top; find your code in the trace; go to that line.
- ✔ The exception message often tells you exactly what is null or what was out of bounds.

# Guided Practice Quest

Work through the sidebar steps to classify a runtime error, identify the cause of a NullPointerException, and read a stack trace to extract key information.

# Solo Practice Quest

**Spell: Hunt the Cascade**

For each of these code snippets, state:
1. What runtime exception will occur.
2. The exact line it will occur on.
3. What change would fix it.

**Snippet A:**
```java
String[] spells = {"Fireball", "Ice Shard", "Thunder"};
System.out.println(spells[3]);
```

**Snippet B:**
```java
String wizardName = null;
System.out.println("Name length: " + wizardName.length());
```

**Snippet C:**
```java
int mana = 100;
int cost = 0;
int potency = mana / cost;
System.out.println("Potency: " + potency);
```

# Integration

**Mathematics connection — undefined operations**

Mathematics defines certain operations as undefined: dividing by zero, taking the square root of a negative number (in real arithmetic). When a program attempts an undefined mathematical operation, it throws an exception — the programmatic equivalent of "this is outside the defined domain." `ArithmeticException: / by zero` is Java's way of saying `f(0) = undefined`. Understanding which operations are undefined in mathematics helps you anticipate which code paths might throw exceptions at runtime.

**Psychology connection — error blindness**

Cognitive research shows that humans miss errors more when they are tired, rushed, or have been staring at the same code too long — a phenomenon called inattentional blindness. Stack traces combat this by being explicit and objective: they do not care how tired you are, and they do not point to the wrong line. Training yourself to stop, read the stack trace carefully, and resist the urge to guess is a psychological habit as much as a technical skill.

**Question:** A program crashes with `ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 3`. Without seeing the code, what can you deduce about the array and the attempted access? How would you use this information to find and fix the bug?

# Lore Conclusion

You have learned to read the cascade. Runtime corruptions are dramatic — they halt a construct mid-operation and spill their call history across the output. But they are not chaotic. Every stack trace is an ordered record, readable by any adept who knows the format. In the next lesson you will meet the most insidious corruption of all: the one that leaves no message, no crash, no evidence — except that the spell produces the wrong result.
