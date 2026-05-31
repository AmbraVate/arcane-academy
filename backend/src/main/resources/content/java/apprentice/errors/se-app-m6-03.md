---
id: se-app-m6-03
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m6
moduleTitle: "Module 6: Debugging & Engineering Habits"
moduleGlyph: "🔬"
moduleSortOrder: 6
topicSlug: errors
topicTitle: "Errors"
topicSortOrder: 1
lesson: logical_errors
title: "Logical Errors"
sortOrder: 3
difficulty: 2
estimatedMinutes: 22
xpReward: 50
practiceType: JAVA
questType: INVESTIGATION
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m6-02]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly defines a logical error as one where the program runs but produces wrong results"
    - "Explains why logical errors are harder to find than syntax or runtime errors"
    - "Identifies at least three examples of logical errors (off-by-one, wrong operator, wrong order)"
    - "Explains a strategy for finding logical errors (print debugging, desk-checking)"
    - "Correctly traces through a given piece of buggy code to identify the logical flaw"
  keywords: [logical error, wrong result, runs, off-by-one, wrong operator, order, desk check, trace]
  modelAnswer: |
    A logical error means the program compiles and runs without crashing, but produces
    the wrong output. They are hardest to find because no error message is generated —
    you must reason about what the code does versus what you intended.
    Examples: using < instead of <=, initialising a counter at 1 instead of 0 (off-by-one),
    subtracting when you should add. Strategy: desk-check the code by tracing variable
    values step by step, and use print statements to inspect intermediate results.

guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A program is supposed to calculate the average of 5 numbers. It compiles, runs, and outputs 4.2 — but the correct answer is 5.2. What type of error is this?"
    inputConfig:
      options:
        - "Syntax error"
        - "Runtime error"
        - "Logical error"
        - "Compiler error"
      correctIndex: 2
    markingRule: EXACT_MATCH
    hint: "The program ran without crashing — but the answer is wrong. No error message was produced."
    reflectionPrompt: "Why is this type of error harder to find than a syntax or runtime error?"

  - id: step-2
    sortOrder: 2
    inputType: SHORT_ANSWER
    instruction: "This code is supposed to count from 1 to 5, printing each number. What does it actually print? Trace it step by step.\n\nfor (int i = 0; i <= 5; i++) { System.out.println(i); }"
    inputConfig:
      placeholder: "It prints: ..."
    markingRule: KEYWORD_MATCH
    hint: "Start with i=0. Check the condition i <= 5. Print i. Increment. Repeat until condition is false."
    reflectionPrompt: "Should the loop start at 0 or 1? Should the condition be < 5 or <= 5?"

  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "This method should return the area of a rectangle (width * height) but it has a logical error. Find and fix it."
    inputConfig:
      language: java
      starterCode: "int rectangleArea(int width, int height) {\n    return width + height;\n}\n"
      expectedPattern: "width\\s*\\*\\s*height|return\\s+width\\s*\\*"
    markingRule: REGEX_MATCH
    hint: "Area = width * height, not width + height."
    reflectionPrompt: "This compiled and ran without errors — how would you have discovered the bug if you hadn't been told?"

microCheckpoint:
  - question: "What makes logical errors harder to find than syntax errors?"
    options:
      - "Logical errors prevent the program from compiling"
      - "The compiler and runtime give no error — the program runs but produces wrong output"
      - "Logical errors only happen in large programs"
      - "The IDE automatically fixes logical errors"
    correctIndex: 1
    feedback: "Correct — logical errors produce no message. The only sign something is wrong is incorrect output, which requires you to know what the correct output should be."

  - question: "Which of these is a classic off-by-one error?"
    options:
      - "Using + instead of *"
      - "Calling a method on a null object"
      - "A loop that runs 9 times when it should run 10, due to using < instead of <="
      - "Missing a closing brace"
    correctIndex: 2
    feedback: "Yes — off-by-one errors are subtle boundary mistakes where the loop iterates one too many or one too few times, often caused by choosing the wrong comparison operator."

retrieval:
  recall: "Why are logical errors considered the hardest type of error to find?"
  explain: "Explain what an off-by-one error is and give a concrete example of how it could occur in a loop."
  mistakeId:
    code: |
      // Method should check if a student passed (score >= 50)
      boolean hasPassed(int score) {
          return score > 50;
      }
    answer: "Logical error: the condition is score > 50 (strictly greater than), but a score of exactly 50 should also be a pass. The correct condition is score >= 50."
---

# Hook

The most dangerous enemy is the one you cannot see. A syntax error shows itself immediately — red text, compiler refusal. A runtime error crashes loudly and leaves a stack trace. But a logical error? It hides in plain sight. The program compiles perfectly. It runs without complaint. And then it quietly produces the wrong answer — possibly for weeks, possibly undetected, possibly causing real damage before anyone notices. This is the silent corruption: the bug with no error message.

# Lore Introduction

In the Academy's Third Age, a construct was dispatched to count the enemy's supply wagons crossing the Northern Bridge. The construct was well-made — syntactically perfect, no runtime instabilities. But its counter began at 1 instead of 0. For three days it reported one fewer wagon than actually crossed. The commanders made their tactical decisions accordingly and were caught by surprise. The construct had a logical flaw — an off-by-one corruption so small it fit in a single digit. The Academy has never forgotten that lesson.

# Core Learning

## Concept Introduction

A **logical error** is a flaw in the program's reasoning that causes it to produce incorrect results, even though it compiles and runs without crashing. There is no error message — the only evidence is wrong output.

**Why they are the hardest type:**
- The compiler cannot check whether your logic is correct — only whether the code is syntactically valid.
- The runtime does not know what you *intended* — it just executes what you *wrote*.
- You need to *know what the correct answer should be* to detect a logical error.

**Common logical errors:**
1. **Off-by-one**: loop runs once too many or too few (`< 5` vs `<= 5`).
2. **Wrong operator**: `+` instead of `*`, `>` instead of `>=`, `&&` instead of `||`.
3. **Wrong order of operations**: subtracting before dividing, negating after multiplying.
4. **Incorrect formula**: area = width + height instead of width × height.

## Why It Matters

Logical errors in production software cause incorrect bills, wrong medical doses, bad navigation directions, and miscalculated scores. They are responsible for some of the most costly software failures in history. The habit of *testing your understanding* — checking that your output matches your expectation — is what catches them.

## Worked Examples

**Off-by-one in a loop:**
```java
// Intended: print 1 to 5
for (int i = 1; i < 5; i++) {   // LOGICAL ERROR: stops at 4
    System.out.println(i);
}
// Fix:
for (int i = 1; i <= 5; i++) {  // now prints 1, 2, 3, 4, 5
    System.out.println(i);
}
```

**Wrong operator:**
```java
// Intended: check if age is 18 or older
if (age > 18) {   // LOGICAL ERROR: excludes exactly 18
    System.out.println("Welcome");
}
// Fix:
if (age >= 18) {
    System.out.println("Welcome");
}
```

**Wrong formula:**
```java
double average(int a, int b, int c) {
    return a + b + c / 3;   // LOGICAL ERROR: only divides c by 3 due to operator precedence
}
// Fix:
    return (a + b + c) / 3.0;
```

## Common Mistakes

- **Assuming it must be a runtime error**: A wrong result with no crash is almost always logical.
- **Trusting code that looks right**: "It looks correct" is not the same as "it is correct" — trace through it with actual values.
- **Testing only the happy path**: Test boundary values (0, 1, the maximum, the minimum) — logical errors often hide at the edges.

## Mental Model

Think of a logical error as a **wrong recipe instruction**. The recipe compiles (it is written in valid English, all steps are grammatically correct) and it executes (you can follow every instruction without the kitchen catching fire). But if the recipe says "add 2 tablespoons of salt" when it should say "2 teaspoons," the result is wrong but no instruction failed. You must taste the food — run the output through your expectations — to find the problem.

## Mini Summary

- ✔ Logical errors compile and run without error messages — the output is simply wrong.
- ✔ They are the hardest to find because there is no crash or error to point you to the line.
- ✔ Common types: off-by-one, wrong operator, wrong formula, wrong order of operations.
- ✔ Strategy: desk-check by tracing variable values manually; test boundary cases.
- ✔ You need to know what the correct answer is to detect a logical error.

# Guided Practice Quest

Work through the sidebar steps to classify a logical error by its symptoms, trace a loop with an off-by-one error, and fix a method with an incorrect formula.

# Solo Practice Quest

**Spell: Expose the Silent Flaw**

Trace through this code **by hand** (without running it). State what it actually outputs, what it *should* output, and what the logical error is:

```java
int total = 0;
for (int i = 1; i <= 10; i++) {
    total = total + i;
}
System.out.println("Sum of 1 to 9: " + total);
```

Then answer:
1. What does this code actually compute?
2. What does the printed label claim?
3. Are there any other bugs (is the computation itself correct)?
4. How would you fix the misleading label, and separately, how would you change the code to sum only 1 to 9?

# Integration

**Mathematics connection — boundary conditions**

Mathematical definitions often include careful specification of boundaries: is the interval [0, 10] or (0, 10)? Does "at least 18" mean ≥ 18 or > 18? Off-by-one errors in code are exactly the boundary confusion that mathematicians are trained to be precise about. The habit of specifying `< n` vs `<= n` with the same care a mathematician specifies open vs closed intervals would eliminate an enormous class of logical bugs.

**Philosophy connection — the problem of induction**

Philosopher David Hume argued that induction — concluding that all cases follow a pattern based on observed examples — is never logically certain. Testing a program on three cases and concluding it is correct is the same fallacy. A logical error might only appear on case 1000. This is why testing boundary values, edge cases, and unexpected inputs is philosophically justified: you can never observe all inputs, so you must be strategic about which ones you test.

**Question:** Write a method `isEvenCount(int[] numbers)` that is supposed to count how many even numbers are in an array. Describe a logical error you could introduce (without changing the syntax or causing a crash) and explain how you would detect it through testing.

# Lore Conclusion

The silent flaw is the most dangerous opponent in the debugger's arsenal. It leaves no trace except wrong output — and only a developer who knows what the right output looks like can catch it. The next topic gives you two tools for fighting back: first, how to read error messages like a detective; then, the most time-honoured debugging technique in all of programming — the humble print statement, wielded with precision.
