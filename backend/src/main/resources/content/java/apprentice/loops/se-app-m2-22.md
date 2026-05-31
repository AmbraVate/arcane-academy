---
id: se-app-m2-22
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: loops
topicTitle: "Loops"
topicSortOrder: 4
lesson: for_loops
title: "For Loops"
sortOrder: 22
difficulty: 1
estimatedMinutes: 22
xpReward: 50
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-21]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly writes `for (init; condition; update)` syntax with all three parts"
    - "Explains the role of each of the three parts: init, condition, update"
    - "Explains the execution order: init once, then check condition, body, update, repeat"
    - "Describes when for is preferred over while (known iteration count)"
    - "Writes a correct for loop that iterates over a range"
  keywords: [for, init, condition, update, counter, range, count, iteration]
  modelAnswer: |
    A `for` loop has three parts: `for (init; condition; update)`. The `init` runs once before the loop starts (usually declares and initialises a counter). The `condition` is evaluated before each iteration — if false, the loop ends. The `update` runs after each iteration (usually increments the counter).

    Execution order:
    1. init runs once
    2. Condition is checked — if false, exit loop
    3. Body runs
    4. Update runs
    5. Go back to step 2

    The for loop is preferred over while when the iteration count is known in advance because all three loop-control elements (initialisation, condition, update) are in one line at the top, making the loop's behaviour immediately visible.

    Example: `for (int i = 0; i < 10; i++)` — clear at a glance: starts at 0, ends before 10, increments by 1. Ten iterations.
guidedSteps:
  - id: se-app-m2-22-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      ```java
      for (int i = 1; i <= 4; i++) {
          System.out.println(i * 2);
      }
      ```
      What does this print?
    inputConfig:
      options:
        - "2 4 6 8"
        - "1 2 3 4"
        - "2 4 6"
        - "0 2 4 6"
    markingRule:
      matchMode: EXACT
      accepted: ["2 4 6 8"]
      rejectedFeedback: "i starts at 1, condition is i <= 4. Iterations: i=1 → 1*2=2; i=2 → 2*2=4; i=3 → 3*2=6; i=4 → 4*2=8. Then i becomes 5, 5 <= 4 is false, loop ends. Output: 2 4 6 8."
    hint: "Trace: what is i on each iteration? What is i * 2?"
    reflectionPrompt: "The for loop's update (`i++`) runs after each iteration's body. By tracing each value of i through the condition and body, you can predict exactly what any for loop produces."

  - id: se-app-m2-22-step2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Write a for loop that prints "Hello" exactly 5 times.
    inputConfig:
      language: java
      starterCode: |
        // Write your for loop below
    markingRule:
      matchMode: CONTAINS
      accepted: ["for", "5", "Hello", "println"]
      rejectedFeedback: |
        ```java
        for (int i = 0; i < 5; i++) {
            System.out.println("Hello");
        }
        ```
        Or equivalently: `for (int i = 1; i <= 5; i++)`. Both iterate exactly 5 times. The loop variable `i` does not need to appear in the body — it is just used for counting.
    hint: "Use `int i = 0; i < 5; i++` or `int i = 1; i <= 5; i++`. Either gives 5 iterations."
    reflectionPrompt: "The loop variable does not have to be used inside the body. It is perfectly valid to use `i` purely as a counter to control how many times the body runs."

  - id: se-app-m2-22-step3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain when you would choose a `for` loop over a `while` loop. Give a specific example of each.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["known", "count", "unknown", "condition", "iteration", "range"]
      rejectedFeedback: "Use `for` when you know the iteration count (e.g., print numbers 1 to 100, process all 12 months, draw a 5×5 grid). Use `while` when the count depends on a condition that changes at runtime (e.g., keep reading until the file ends, keep asking until valid input, keep retrying until connection succeeds)."
    hint: "For = known count. While = unknown count / condition-driven."
    reflectionPrompt: "Choosing the right loop type makes your intent clear to the reader. `for` signals 'I know how many times'; `while` signals 'I am looping until something changes'."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In `for (int i = 0; i < 5; i++)`, which part runs only ONCE at the start?"
    options:
      - "The condition `i < 5`"
      - "The update `i++`"
      - "The body"
      - "The initialisation `int i = 0`"
    correctIndex: 3
    feedback: "The initialisation `int i = 0` runs exactly once, before the first condition check. The condition and update run on every iteration (condition before body, update after body). The body runs on every iteration where the condition is true."

  - type: MULTIPLE_CHOICE
    question: "How many times does this loop run? `for (int i = 10; i >= 1; i--)`"
    options:
      - "9 times"
      - "10 times"
      - "11 times"
      - "Infinite — it never ends"
    correctIndex: 1
    feedback: "i starts at 10 and decrements by 1 each iteration (i--). It runs for i = 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 — that is 10 iterations. When i becomes 0, the condition `i >= 1` is false and the loop ends."

retrieval:
  recall: "Write a for loop that prints every even number from 2 to 20 inclusive."
  explain: "Explain the exact execution order of the three parts of a for loop: init, condition, and update."
  mistakeId:
    code: |
      for (int i = 0; i < 10; i++) {
          System.out.println(i);
          i++;
      }
    answer: "The loop body increments `i` with `i++`, but so does the for loop's update clause. This means i is incremented twice per iteration, printing only even numbers: 0, 2, 4, 6, 8. The update clause already handles incrementing — adding `i++` to the body is a double-increment bug. Remove the `i++` from the body."
---

# Hook

You need to print every number from 1 to 100. You know exactly how many times to repeat: 100 times. You need to fill a 10-by-10 grid: 100 cells. You need to process every month of the year: 12 months. When the count is known in advance, there is a loop built precisely for this job. It bundles the initialisation, the condition, and the update into one clean line — so the reader sees the loop's entire counting logic at a glance. That is the `for` loop.

# Lore Introduction

"The while rune persists until conditions change," Archmage Veylan says, drawing two runes side by side. "The for rune counts." He traces the second rune carefully. "It carries within it three inscriptions: where to begin, when to stop, and how to advance. All three in one rune, visible at once." He stands back. "When you know how many times a spell must be cast, the counting rune is the precise tool. It leaves nothing implicit — an apprentice who reads it knows immediately the range of the repetition."

# Core Learning

## Concept Introduction

A **for loop** has three parts: initialisation, condition, and update:

```java
for (init; condition; update) {
    // body
}
```

**Execution order:**
1. `init` — runs **once** before the loop starts
2. `condition` — checked **before each iteration** (if false → exit)
3. body runs
4. `update` — runs **after each iteration**
5. Back to step 2

**Common form — counting up:**
```java
for (int i = 0; i < n; i++) {
    // runs n times; i goes 0, 1, 2, ..., n-1
}
```

**Common form — counting down:**
```java
for (int i = n; i >= 1; i--) {
    // runs n times; i goes n, n-1, ..., 1
}
```

## Why It Matters

The for loop is the standard tool for iterating a known number of times. It groups all loop-control information in one place (the header), making loops easy to read and reason about. It is the most common loop in Java for processing arrays, ranges, and indices.

## Worked Examples

**Example 1 — Sum 1 to 10:**
```java
int sum = 0;
for (int i = 1; i <= 10; i++) {
    sum += i;
}
System.out.println("Sum: " + sum);
// Prints: Sum: 55
```

**Example 2 — Print a multiplication table row:**
```java
int n = 5;
for (int i = 1; i <= 10; i++) {
    System.out.println(n + " x " + i + " = " + (n * i));
}
// Prints: 5 x 1 = 5, 5 x 2 = 10, ..., 5 x 10 = 50
```

**Example 3 — Iterate over an array:**
```java
int[] scores = {85, 92, 67, 78, 95};
for (int i = 0; i < scores.length; i++) {
    System.out.println("Score " + i + ": " + scores[i]);
}
// Prints each score with its index
```

## Common Mistakes

- **Double-incrementing `i`:** Adding `i++` in both the update clause and the body causes `i` to increment twice per iteration.
- **Off-by-one in the condition:** `i < 5` gives indices 0–4; `i <= 5` gives indices 0–5. Know which you want.
- **Starting at 1 when 0 is needed:** Arrays are 0-indexed; starting a loop at 0 and using `i < array.length` is the standard pattern.
- **Using the loop variable after the loop:** `i` declared in the for header is scoped to the loop — it does not exist after the closing `}`.
- **Forgetting the semicolons:** `for (int i = 0 i < 5 i++)` — missing semicolons are a syntax error. All three parts need `;` separators.

## Mental Model

A for loop is like a **odometer** on a car. It starts at a reading (init), advances by a fixed amount each turn of the wheel (update), and you know you will stop at a set reading (condition). All the counter information is in one place: start, step size, and stop. You glance at the odometer line and immediately know the journey's range — no need to look at the body to understand how far the loop travels.

## Mini Summary

- `for (init; condition; update)` — three parts, all visible in the header.
- Init runs once; condition is checked before each iteration; update runs after each iteration.
- Use for when you know the iteration count in advance.
- The standard counting form: `for (int i = 0; i < n; i++)` — runs n times with i from 0 to n-1.
- The loop variable `i` is scoped to the loop block.
- Avoid adding manual increments to the body when the update clause handles it.

# Guided Practice Quest

*"The Academy's training schedule requires each apprentice to practice a spell exactly seven times per session," Archmage Veylan says. "Write a for loop that simulates this: print 'Practice [n]: casting spell' for n from 1 to 7. Then modify it to also print the total practice count at the end."*

# Solo Practice Quest

**The Pattern Printer**

Write a Java for loop that prints the following pattern using the variable `i`:

```
Row 1: *
Row 2: **
Row 3: ***
Row 4: ****
Row 5: *****
```

Hint: Use a for loop for rows (i from 1 to 5). For the stars, you can build a String by repeating `"*"` using `"*".repeat(i)` (Java 11+) or a nested loop.

After writing the loop, trace through it for the first 3 iterations, showing the value of `i` and what is printed each time.

# Integration

**Mathematics connection:** The for loop is a direct implementation of mathematical **summation** and **product** notation. ∑_{i=1}^{n} f(i) means "compute f(i) for every i from 1 to n and sum the results" — exactly what a for loop with `sum += f(i)` computes. The loop variable `i` is the summation index; the init sets the lower bound; the condition enforces the upper bound. Many fundamental algorithms (computing factorials, Fibonacci numbers, prime sieves) are for loops in disguise.

**Philosophy connection:** The 18th-century philosopher Immanuel Kant argued that our understanding imposes structure on experience — we do not passively receive data but actively organise it. A for loop is a machine for imposing structure: it takes a raw sequence (numbers, items, indices) and forces the same processing operation onto each element. The "form" (what the loop does) is imposed by the programmer; the "content" (what each element is) comes from the data. This is analogous to Kant's idea that the mind provides form while reality provides content.

*Free question: Java also has an "enhanced for loop" (also called a "for-each loop") for iterating over collections: `for (int score : scores)`. How does this differ from the standard for loop? When would you prefer one over the other?*

# Lore Conclusion

The counting rune completes its seventh inscription and goes still. "Seven times. No more, no less," Archmage Veylan says with satisfaction. "The for rune carries its own termination — the count is inscribed within it." He places the scroll next to the while scroll. "You now possess both: the persistent rune that acts until conditions change, and the counting rune that acts a precise number of times." He folds both scrolls together. "In the next lesson, you will combine them — a loop within a loop — and learn what power and caution that brings."
